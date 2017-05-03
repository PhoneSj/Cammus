package com.cammuse.intelligence.device;

import android.content.Context;
import android.content.pm.PackageManager;

import com.cammuse.intelligence.entity.BLEData;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.ToastUtils;
import com.cammuse.intelligence.device.BLEUtils;

public class BLEUtils {

    private static final String TAG = "BLEUtils";
    private static String blank = " ";
    private static String head = "SR";
    private static String foot = "EN";

    /**
     * 判断手机是否具有BLE功能
     *
     * @param mContext
     * @return
     */
    public static boolean supportBle(Context mContext) {
        if (mContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            return true;
        }
        return false;
    }

    /**
     * 发送蓝牙数据
     *
     * @param str
     */
    public static void sendBleDate(Context mContext, String data) {
        if (ConstantUtils.mConnected
                && ConstantUtils.mBluetoothLeService != null) {
            ConstantUtils.mBluetoothLeService.WriteValue(data);
            LogUtils.d(TAG, "发送的蓝牙数据：" + data);
        } else {
            ToastUtils.showShort(mContext, "蓝牙未连接");
        }

    }

    /**
     * 构造蓝牙数据 SR 02 %02s %02d %02d 00 EN
     *
     * @return
     */
    public static String constructData(int equitType, String equitId,
                                       int modeId, int modeBar, int gasDegree) {
        String data = String.format("SR %02d %s %02d %02d %02d EN", equitType,
                equitId, modeId, modeBar, gasDegree);
//		LogUtils.d(TAG, "构造的蓝牙数据：" + data);
        return data;
    }

    public static String constructData(int modeId, int modeBar) {
        int equitType = ConstantUtils.EQUIT_TYPE;
        String equitId = Integer.toHexString(ConstantUtils.EQUIT_ID);
        if (equitType == 0 || equitId == "0") {
            LogUtils.d(TAG, "--constructData--出错");
        }
        String data = String.format("SR %02d %s %02d %02d %02d EN", equitType,
                equitId, modeId, modeBar, 0);
//		LogUtils.d(TAG, "构造的蓝牙数据：" + data);
        return data;
    }

    public static String constructData(BLEData mBleData) {
        // int equitType = mBleData.getEquit_type();
        // String equitId = Integer.toHexString(mBleData.getEquit_id());
        int equitType = ConstantUtils.EQUIT_TYPE;
        String equitId = Integer.toHexString(ConstantUtils.EQUIT_ID);
        if (equitType == 0 || equitId == "0") {
//			LogUtils.d(TAG, "--constructData--出错");
        }
        int modeId = mBleData.getMode_id();
        int modeBar = mBleData.getMode_bar();
        int gasDegree = mBleData.getGas_degree();
        String data = String.format("SR %02d %s %02d %02d %02d EN", equitType,
                equitId, modeId, modeBar, gasDegree);
//		LogUtils.d(TAG, "构造的蓝牙数据：" + data);
        return data;
    }

    /**
     * 解析接受的蓝牙数据
     *
     * @param data
     * @return
     */
    public static BLEData parseData(String data) {
        BLEData mBleDate = new BLEData();
        String[] temps;
        if (data != null) {
            temps = null;
            temps = data.split(blank);// 将字符串数据以空格符分割为字符串数组
            if (temps[0].equals("SR")) {// 数据以“SR”开头
                if (temps[temps.length - 1].equals("EN")) {// 数据以“EN”结尾
                    /**
                     * 功能编号: 01--表示该数据为仪表数据 02--表示该数据为智能驾驶数据
                     */
                    int equit_type = Integer.parseInt(temps[1]);
                    mBleDate.setEquit_type(equit_type);
                    /**
                     * 设备编号
                     */
                    int equit_id = Integer.parseInt(temps[2], 16);
                    mBleDate.setEquit_id(equit_id);
                    /**
                     * 智能驾驶模式、手/自动挡： 00-05：表示智能驾驶模式的六种 06：表示at/mt切换
                     */
                    int mode_id = Integer.parseInt(temps[3]);
                    mBleDate.setMode_id(mode_id);
                    /**
                     * 微调：范围1-10
                     */
                    int double_mean = Integer.parseInt(temps[4]);
                    /**
                     * 油门深浅
                     */
                    int gas_degree = Integer.parseInt(temps[5]);
                    mBleDate.setGas_degree(gas_degree);

                    // 当为模式6时，double_mean表示模式微调
                    switch (mode_id) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            mBleDate.setMode_bar(double_mean);
                            break;
                        case 6:
                            // 手/自动挡的切换
                            if (double_mean == 01) {
                                mBleDate.setAT(true);
                            } else if (double_mean == 02) {
                                mBleDate.setAT(false);
                            }
                            break;

                    }
                }
            }
        }
        return mBleDate;
    }

    /**
     * 将mode_id/mode_bar转化为level,不包括at/mt,level的范围1-41
     *
     * @param mBleData
     * @return
     */
    public static int bleData2Level(BLEData mBleData) {
        // 当是AT、MT模式切换时，不处理
        if (mBleData.getMode_id() == 6) {
            return 0;
        }
        int level = 0;
        switch (mBleData.getMode_id()) {
            case ConstantUtils.DRIVE_MODE_SAVE:// 1-10
                level = 0 * 10 + mBleData.getMode_bar();
                break;
            case ConstantUtils.DRIVE_MODE_ORIGINAL:// 11
                level = 1 * 10 + 1;
                break;
            case ConstantUtils.DRIVE_MODE_SPORT:// 12-21
                level = (1 * 10 + 1) + mBleData.getMode_bar();
                break;
            case ConstantUtils.DRIVE_MODE_SUPER:// 22-31
                level = (2 * 10 + 1) + mBleData.getMode_bar();
                break;
            case ConstantUtils.DRIVE_MODE_RACE:// 32-41
                level = (3 * 10 + 1) + mBleData.getMode_bar();
                break;
            case ConstantUtils.DRIVE_MODE_SMART:
                LogUtils.d(TAG, "--odeAndBar2Level--samrt_mode--");
                break;
            default:
                level = ConstantUtils.DEFAULT_AUTOMATIC_CURRENT_LEVEL;
                LogUtils.d(TAG, "--odeAndBar2Level--异常--");
                break;
        }
        return level;
    }

    /**
     * level转换成BLEData对象，不包括at/mt,level的范围1-41
     *
     * @param level
     * @return
     */
    public static BLEData level2BleData(int level) {
        BLEData mBleData = new BLEData();
        mBleData.setEquit_type(ConstantUtils.EQUIT_TYPE);
        mBleData.setEquit_id(ConstantUtils.EQUIT_ID);
        if (level >= ConstantUtils.LEVEL_MIN && level <= 10) {
            mBleData.setMode_id(ConstantUtils.DRIVE_MODE_SAVE);
            mBleData.setMode_bar(level);
        } else if (level == 11) {
            mBleData.setMode_id(ConstantUtils.DRIVE_MODE_ORIGINAL);
        } else if (level >= 12 && level <= 21) {
            mBleData.setMode_id(ConstantUtils.DRIVE_MODE_SPORT);
            mBleData.setMode_bar(level - 11);
        } else if (level >= 22 && level <= 31) {
            mBleData.setMode_id(ConstantUtils.DRIVE_MODE_SUPER);
            mBleData.setMode_bar(level - 21);
        } else if (level >= 32 && level <= ConstantUtils.LEVEL_MAX) {
            mBleData.setMode_id(ConstantUtils.DRIVE_MODE_RACE);
            mBleData.setMode_bar(level - 31);
        } else {
            level2BleData(ConstantUtils.DEFAULT_AUTOMATIC_CURRENT_LEVEL);
            LogUtils.d(TAG, "level2BleData---出错");
        }

        return mBleData;
    }

}

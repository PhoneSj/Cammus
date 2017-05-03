package com.cammuse.intelligence.entity;

import android.content.Context;

import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.SPUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class BleDevice {

    private static final java.lang.String TAG = "BleDevice";
    private String name;
    private String address;
    private boolean state;

    public BleDevice(String name, String address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (address.equals(((BleDevice) o).getAddress())) {
            return true;
        } else {
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * 获取sp中所有连接的历史记录，并解析为BleDevice对象set
     *
     * @return
     */
    public static List<BleDevice> getBleDevicesFromSp(Context context) {
        List<BleDevice> list = new ArrayList<BleDevice>();
        Gson mGson = new Gson();
        String oldString = SPUtils.getString(context, ConstantUtils.BIND_DEVICES, null);
        if (oldString != null) {
            try {
                JSONArray jsonArray = new JSONArray(oldString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    BleDevice mBleDevice = mGson.fromJson(jsonArray.optString(i), BleDevice.class);
                    list.add(mBleDevice);
                }
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtils.d(TAG, "json解析出错");
            }
        }
        return list;
    }

    public static void setBleDevicesToSp(Context context, List<BleDevice> list) {
//        List<BleDevice> onlyList = toOnlyElement(list);
        Gson mGson = new Gson();
        String data = mGson.toJson(list);
        LogUtils.d(TAG, "data:" + data);
        SPUtils.putString(context, ConstantUtils.BIND_DEVICES, data);
    }

    private static List<BleDevice> toOnlyElement(List<BleDevice> list) {
        String address;
        List<BleDevice> needDeletes = new ArrayList<BleDevice>();
        for (int i = 0; i < list.size(); i++) {
            address = list.get(i).getAddress();
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j).getAddress().equals(address)) {
                    needDeletes.add(list.get(i));
                    break;
                }
            }
        }
        LogUtils.d(TAG, "needDeletes.size:" + needDeletes.size());
//        for (int i = needDeletes.size(); i >= 0; i--) {
//            list.remove(needDeletes.get(i));
//            LogUtils.d(TAG, "----" + i);
//        }
        list.removeAll(needDeletes);
        return list;
    }
}

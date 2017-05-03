package com.cammuse.intelligence.tool.accelerator;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cammuse.intelligence.BaseFragmentActivity;
import com.cammuse.intelligence.R;
import com.cammuse.intelligence.device.BLEUtils;
import com.cammuse.intelligence.device.BluetoothLeService;
import com.cammuse.intelligence.entity.BLEData;
import com.cammuse.intelligence.entity.UserLevel;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControlActivity extends BaseFragmentActivity implements
        OnClickListener {

    private static final String TAG = "ControlActivity";

    public static final String FRAGMENT_TITLE = "title";
    private RelativeLayout relativeLayout_title_controlAct;
    private ImageView imageView_controlAct_back;
    private ImageView imageView_share_controlAct;
    private TextView textView_titleName_controlAct;
    private ViewPager viewPager_controlAct;
    private LinearLayout linearLayut_main_tab_manual;
    private LinearLayout linearLayut_main_tab_automatic;
    private ImageButton imageButton_main_tab_manual;
    private ImageButton imageButton_main_tab_automatic;
    private TextView textView_main_tab_manual;
    private TextView textView_main_tab_automatic;

    private List<Fragment> mTabContents = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;

    private List<String> mDatas = Arrays.asList("手动", "自动");
    private List<Toast> list = new ArrayList<Toast>();

    private WakeLock mWakeLock;

    /**
     * 接受蓝牙服务发出的广播信息
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d(TAG, ".......");
            String action = intent.getAction();

            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                LogUtils.d(TAG, "DATA:" + data);
                BLEData mBleData = BLEUtils.parseData(data);
                if (mBleData.getEquit_type() == ConstantUtils.EQUIT_TYPE_ACCELERATOR) {
                    if (ConstantUtils.ENABLE_AUTOMATIC) {
                        //自动模式
                        ((ManualFragment) mTabContents.get(1)).setData(mBleData);
                    } else {
                        //手动模式
                        ((ManualFragment) mTabContents.get(0)).setData(mBleData);
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_control);
        initView();
        initDatas();
        // 设置Tab上的标题
        viewPager_controlAct.setAdapter(mAdapter);
        initEvent();
        selectTab(0);

        registerReceiver(mGattUpdateReceiver,
                makeGattUpdateIntentFilter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver);
    }

    private void initView() {
        relativeLayout_title_controlAct = (RelativeLayout) findViewById(R.id.relativeLayout_title_controlAct);
        imageView_controlAct_back = (ImageView) findViewById(R.id.imageView_controlAct_back);
        imageView_share_controlAct = (ImageView) findViewById(R.id.imageView_share_controlAct);
        textView_titleName_controlAct = (TextView) findViewById(R.id.textView_titleName_controlAct);
        viewPager_controlAct = (ViewPager) findViewById(R.id.viewPager_controlAct);
        linearLayut_main_tab_manual = (LinearLayout) findViewById(R.id.linearLayut_main_tab_manual);
        linearLayut_main_tab_automatic = (LinearLayout) findViewById(R.id.linearLayut_main_tab_automatic);
        imageButton_main_tab_manual = (ImageButton) findViewById(R.id.imageButton_main_tab_manual);
        imageButton_main_tab_automatic = (ImageButton) findViewById(R.id.imageButton_main_tab_automatic);
        textView_main_tab_manual = (TextView) findViewById(R.id.textView_main_tab_manual);
        textView_main_tab_automatic = (TextView) findViewById(R.id.textView_main_tab_automatic);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            System.out.println("second");
            StringBuilder sb = new StringBuilder();
            sb.append("标题栏高度:" + relativeLayout_title_controlAct.getHeight()
                    + "\t宽度：" + relativeLayout_title_controlAct.getWidth());
            sb.append("\n内容区高度" + viewPager_controlAct.getHeight() + "\t宽度："
                    + viewPager_controlAct.getWidth());
            LogUtils.d(TAG, sb.toString());
        }
    }

    /**
     * 初始化ViewPager的数据
     */
    private void initDatas() {

        ManualFragment fragment0 = new ManualFragment(
                textView_titleName_controlAct);
        mTabContents.add(fragment0);
        AutomaticFragment fragment1 = new AutomaticFragment(
                textView_titleName_controlAct);
        mTabContents.add(fragment1);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mTabContents.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mTabContents.get(position);
            }
        };
    }

    private void initEvent() {
        imageView_controlAct_back.setOnClickListener(this);
        imageView_share_controlAct.setOnClickListener(this);
        linearLayut_main_tab_manual.setOnClickListener(this);
        linearLayut_main_tab_automatic.setOnClickListener(this);
        viewPager_controlAct
                .setOnPageChangeListener(new OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        LogUtils.d(TAG, "position:" + position);
                        resetTabs();
                        selectTab(position);
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                    }
                });
    }

    /**
     * 切换到手动控制模式
     */
    protected void changeToManual() {
        LogUtils.d(TAG, "-----changeToManual-----");
        ConstantUtils.ENABLE_AUTOMATIC = false;
        int current_mode = SPUtils.getInt(this,
                ConstantUtils.SP_KEY_MANAUAL_MODE,
                ConstantUtils.DEFAULT_MANAUAL_CURRENT_MODE);
        int current_bar = SPUtils.getInt(this,
                ConstantUtils.SP_KEY_MANAUAL_BAR,
                ConstantUtils.DEFAULT_MANAUAL_CURRENT_BAR);
        String data = BLEUtils.constructData(ConstantUtils.EQUIT_TYPE,
                Integer.toHexString(ConstantUtils.EQUIT_ID), current_mode,
                current_bar, 0);
        BLEUtils.sendBleDate(this, data);
        LogUtils.d(TAG, "current_mode:" + current_mode);
        textView_titleName_controlAct
                .setText(ConstantUtils.manual_mode_name[current_mode]);
//		releaseWakeLock();
    }

    /**
     * 切换到自动控制模式
     */
    protected void changeToAutomatic() {
        LogUtils.d(TAG, "-----changeToAutomatic-----");
        ConstantUtils.ENABLE_AUTOMATIC = true;
        int current_level = SPUtils.getInt(this,
                ConstantUtils.SP_KEY_AUTOMATIC_CURRENT_LEVEL,
                ConstantUtils.DEFAULT_AUTOMATIC_CURRENT_LEVEL);
        String data = BLEUtils.constructData(BLEUtils
                .level2BleData(current_level));
        BLEUtils.sendBleDate(this, data);
        int level = SPUtils.getInt(this,
                ConstantUtils.SP_KEY_AUTOMATIC_CURRENT_LEVEL,
                ConstantUtils.DEFAULT_AUTOMATIC_CURRENT_LEVEL);
        UserLevel mUserLevel = UserLevel.level2UserLevel(level);
        textView_titleName_controlAct
                .setText(ConstantUtils.automatic_level_name[mUserLevel
                        .getLevel_large()]);
//		acquireWakeLock();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_controlAct_back:
                finish();
                break;
            case R.id.imageView_share_controlAct:
//			showShare();
                break;
            case R.id.linearLayut_main_tab_manual:
                resetTabs();
                selectTab(0);
                break;
            case R.id.linearLayut_main_tab_automatic:
                resetTabs();
                selectTab(1);
                break;
        }

    }

//	/**
//	 * 分享
//	 */
//	private void showShare() {
//		ShareSDK.initSDK(this);
//		OnekeyShare oks = new OnekeyShare();
//		// 关闭sso授权
//		oks.disableSSOWhenAuthorize();
//		oks.setSilent(false); // 显示编辑页面
//		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
//		// oks.setNotification(R.drawable.ic_launcher,
//		// getString(R.string.app_name));
//		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		oks.setTitle("卡妙思--为改装而生");
//		// text是分享文本，所有平台都需要这个字段
//		// oks.setText("我是分享文本");
//		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		// oks.setImagePath(Environment.getExternalStorageDirectory()
//		// + File.separator + "1449065090568.jpg");// 确保SDcard下面存在此张图片
//		oks.setImageUrl("http://www.cammus.com.cn/u_file/images/15_09_08/0b6d7d574d.jpg");
//		LogUtils.d(TAG, "path:" + Environment.getExternalStorageDirectory());
//		// url仅在微信（包括好友和朋友圈）中使用
//		oks.setUrl("http://www.cammus.com.cn/");
//		// 启动分享GUI
//		oks.show(this);
//	}

    private void selectTab(int i) {
        viewPager_controlAct.setCurrentItem(i);
        switch (i) {
            case 0:
                imageButton_main_tab_manual
                        .setImageResource(R.mipmap.tab_manual_checked);
                textView_main_tab_manual.setTextColor(getResources().getColor(
                        R.color.white));
                changeToManual();
                break;
            case 1:
                imageButton_main_tab_automatic
                        .setImageResource(R.mipmap.tab_automatic_checked);
                textView_main_tab_automatic.setTextColor(getResources().getColor(
                        R.color.white));
                changeToAutomatic();
                break;
        }
    }

    private void resetTabs() {
        imageButton_main_tab_manual
                .setImageResource(R.mipmap.tab_manual_unchecked);
        imageButton_main_tab_automatic
                .setImageResource(R.mipmap.tab_automatic_unchecked);
        textView_main_tab_manual.setTextColor(getResources().getColor(
                R.color.gray));
        textView_main_tab_automatic.setTextColor(getResources().getColor(
                R.color.gray));
    }

    @Override
    protected void onPause() {
        super.onPause();
//		releaseWakeLock();
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter
                .addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothDevice.ACTION_UUID);
        return intentFilter;
    }

//	/**
//	 * 获取屏幕休眠锁
//	 */
//	private void acquireWakeLock() {
//		LogUtils.d(TAG, "--acquireWakeLock--");
//		if (mWakeLock == null) {
//			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//			mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
//					this.getClass().getCanonicalName());
//			mWakeLock.acquire();
//		}
//
//	}
//
//	/**
//	 * 释放屏幕休眠锁
//	 */
//	private void releaseWakeLock() {
//		LogUtils.d(TAG, "--releaseWakeLock--");
//		if (mWakeLock != null && mWakeLock.isHeld()) {
//			mWakeLock.release();
//			mWakeLock = null;
//		}
//	}
}

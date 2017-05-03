package com.cammuse.intelligence;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cammuse.intelligence.device.BluetoothLeService;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.umeng.update.UmengUpdateAgent;


public class MainActivity extends BaseFragmentActivity implements OnClickListener {
    private static final java.lang.String TAG = "MainActivity";
    public static final int REQUEST_ENABLE_BT = 1;
    private LinearLayout linearLayout_main_tab_device;
    private LinearLayout linearLayout_main_tab_tool;
    private LinearLayout linearLayout_main_tab_mall;
    private LinearLayout linearLayout_main_tab_personal;

    private ImageButton imageButton_main_tab_device;
    private ImageButton imageButton_main_tab_tool;
    private ImageButton imageButton_main_tab_mall;
    private ImageButton imageButton_main_tab_personal;

    private TextView textView_main_tab_device;
    private TextView textView_main_tab_tool;
    private TextView textView_main_tab_mall;
    private TextView textView_main_tab_personal;

    private Fragment mTabDevice;
    private Fragment mTabTool;
    private Fragment mTabMall;
    private Fragment mTabPersonal;

    //ble
    private BluetoothLeService mBluetoothLeService;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
                    .getService();
            ConstantUtils.mBluetoothLeService = mBluetoothLeService;
            LogUtils.d(TAG, "服务已连接");
            if (!mBluetoothLeService.initialize()) {
                System.out.println("Unable to initialize Bluetooth");
                finish();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtils.d(TAG, "服务已断开");
            mBluetoothLeService = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        setSelect(0);
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        startService(gattServiceIntent);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        //自动更新
        UmengUpdateAgent.update(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    private void initView() {
        linearLayout_main_tab_device = (LinearLayout) findViewById(R.id.linearLayout_main_tab_device);
        linearLayout_main_tab_tool = (LinearLayout) findViewById(R.id.linearLayout_main_tab_tool);
        linearLayout_main_tab_mall = (LinearLayout) findViewById(R.id.linearLayout_main_tab_mall);
        linearLayout_main_tab_personal = (LinearLayout) findViewById(R.id.linearLayout_main_tab_personal);

        imageButton_main_tab_device = (ImageButton) findViewById(R.id.imageButton_main_tab_device);
        imageButton_main_tab_tool = (ImageButton) findViewById(R.id.imageButton_main_tab_tool);
        imageButton_main_tab_mall = (ImageButton) findViewById(R.id.imageButton_main_tab_mall);
        imageButton_main_tab_personal = (ImageButton) findViewById(R.id.imageButton_main_tab_personal);

        textView_main_tab_device = (TextView) findViewById(R.id.textView_main_tab_device);
        textView_main_tab_tool = (TextView) findViewById(R.id.textView_main_tab_tool);
        textView_main_tab_mall = (TextView) findViewById(R.id.textView_main_tab_mall);
        textView_main_tab_personal = (TextView) findViewById(R.id.textView_main_tab_personal);

    }

    private void initEvent() {
        linearLayout_main_tab_device.setOnClickListener(this);
        linearLayout_main_tab_tool.setOnClickListener(this);
        linearLayout_main_tab_mall.setOnClickListener(this);
        linearLayout_main_tab_personal.setOnClickListener(this);
    }

    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                if (mTabDevice == null) {
                    mTabDevice = new DeviceFragment();
                    transaction.add(R.id.main_tab_content, mTabDevice);
                } else {
                    transaction.show(mTabDevice);
                }
                imageButton_main_tab_device
                        .setImageResource(R.mipmap.main_button_device_down);
                textView_main_tab_device.setTextColor(getResources().getColor(
                        R.color.pink));
                break;
            case 1:
                if (mTabTool == null) {
                    mTabTool = new ToolFragment();
                    transaction.add(R.id.main_tab_content, mTabTool);
                } else {
                    transaction.show(mTabTool);

                }
                imageButton_main_tab_tool
                        .setImageResource(R.mipmap.main_button_tool_down);
                textView_main_tab_tool.setTextColor(getResources().getColor(
                        R.color.pink));
                break;
            case 2:
                if (mTabMall == null) {
                    mTabMall = new MallFragment();
                    transaction.add(R.id.main_tab_content, mTabMall);
                } else {
                    transaction.show(mTabMall);
                }
                imageButton_main_tab_mall
                        .setImageResource(R.mipmap.main_button_mall_down);
                textView_main_tab_mall.setTextColor(getResources().getColor(
                        R.color.pink));
                break;
            case 3:
                if (mTabPersonal == null) {
                    mTabPersonal = new PersonalFragment();
                    transaction.add(R.id.main_tab_content, mTabPersonal);
                } else {
                    transaction.show(mTabPersonal);
                }
                imageButton_main_tab_personal
                        .setImageResource(R.mipmap.main_button_personal_down);
                textView_main_tab_personal.setTextColor(getResources().getColor(
                        R.color.pink));
                break;

            default:
                break;
        }

        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mTabDevice != null) {
            transaction.hide(mTabDevice);
        }
        if (mTabTool != null) {
            transaction.hide(mTabTool);
        }
        if (mTabMall != null) {
            transaction.hide(mTabMall);
        }
        if (mTabPersonal != null) {
            transaction.hide(mTabPersonal);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayout_main_tab_device:
                resetTabs();
                setSelect(0);
                break;
            case R.id.linearLayout_main_tab_tool:
                resetTabs();
                setSelect(1);
                break;
            case R.id.linearLayout_main_tab_mall:
                resetTabs();
                setSelect(2);
                break;
            case R.id.linearLayout_main_tab_personal:
                resetTabs();
                setSelect(3);
                break;
        }
    }

    /**
     * �л�ͼƬ����ɫ
     */
    private void resetTabs() {
        imageButton_main_tab_device
                .setImageResource(R.mipmap.main_button_device_up);
        imageButton_main_tab_tool
                .setImageResource(R.mipmap.main_button_tool_up);
        imageButton_main_tab_mall
                .setImageResource(R.mipmap.main_button_mall_up);
        imageButton_main_tab_personal.setImageResource(R.mipmap.main_button_personal_up);

        textView_main_tab_device.setTextColor(getResources().getColor(
                R.color.gray));
        textView_main_tab_tool.setTextColor(getResources().getColor(
                R.color.gray));
        textView_main_tab_mall.setTextColor(getResources().getColor(
                R.color.gray));
        textView_main_tab_personal
                .setTextColor(getResources().getColor(R.color.gray));
    }


}

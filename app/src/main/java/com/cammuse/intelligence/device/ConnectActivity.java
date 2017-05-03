package com.cammuse.intelligence.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cammuse.intelligence.BaseActivity;
import com.cammuse.intelligence.R;
import com.cammuse.intelligence.entity.BleDevice;
import com.cammuse.intelligence.popup.LoadingPopupWindow;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.SPUtils;
import com.cammuse.intelligence.utils.ToastUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/25.
 */
public class ConnectActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = "ConnectActivity";
    private static final int REQUEST_ENABLE_BT = 1;
    protected static final int SCAN_FINISHED = 0;
    protected static final int DISCONVER_ONE = SCAN_FINISHED + 1;
    private static final long SCAN_PERIOD = 3000;// 蓝牙搜索的时长
    private boolean mScanning;

    private LinearLayout linearLayout_connectAct_root;
    private ImageView imageView_connectAct_back;
    private ImageView imageView_connectAct_scan;
    private ImageView imageView_connectAct_add;
    private ListView listView_connectAct_devices;

    //Ble
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeService mBluetoothLeService;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private static BluetoothDevice device;


    // Hander
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCAN_FINISHED: // 搜索完毕
                    scanLeDevice(false);
                case DISCONVER_ONE: // Notify change
                    mLeDeviceListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

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

    /**
     * 接收蓝牙数据
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) { // 连接成功
                // 只有找到了服务特性才可以通信，才算连接成功过
                ToastUtils.showShort(ConnectActivity.this, "已连接");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
                    .equals(action)) {
                ToastUtils.showShort(ConnectActivity.this, "已断开");
                ConstantUtils.mConnected = false;
//                startVibrate();
//                stopGasService();// 停止接受油门数据
//                setBindInfo(false);
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                String data = intent
                        .getStringExtra(BluetoothLeService.EXTRA_DATA);// 获得发送过来的数据
                LogUtils.d(TAG, "data:" + data);
                if (data != null) {
                    String[] temps = data.split(" ");
                    if (temps[0].equals("SR")
                            && temps[temps.length - 1].equals("EN")) {
                        ConstantUtils.EQUIT_TYPE = BLEUtils.parseData(data)
                                .getEquit_type();
                        ConstantUtils.EQUIT_ID = BLEUtils.parseData(data)
                                .getEquit_id();
                    }
                }
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
                    .equals(action)) {
                ToastUtils.showShort(ConnectActivity.this, "可通信");
                ConstantUtils.mConnected = true;
//                startVibrate();
//                setBindInfo(true);
                ToastUtils.showShort(ConnectActivity.this, R.string.ble_is_collected);
                mBluetoothLeService.WriteValue("SR CHECK ACCEL ID EN");
//                startGasService();// 开始接收油门数据
            }
        }

    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_connect);
        initView();
        initList();
        initEvent();
        initBLE();
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        startService(gattServiceIntent);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGattUpdateReceiver);
        unbindService(mServiceConnection);
    }

    private void initList() {
        mLeDeviceListAdapter = new LeDeviceListAdapter();// 初始化蓝牙列表适配器
        listView_connectAct_devices.setAdapter(mLeDeviceListAdapter);
    }

    private void initView() {
        linearLayout_connectAct_root = (LinearLayout) findViewById(R.id.linearLayout_connectAct_root);
        imageView_connectAct_back = (ImageView) findViewById(R.id.imageView_connectAct_back);
        imageView_connectAct_scan = (ImageView) findViewById(R.id.imageView_connectAct_scan);
        imageView_connectAct_add = (ImageView) findViewById(R.id.imageView_connectAct_add);
        listView_connectAct_devices = (ListView) findViewById(R.id.listView_connectAct_devices);
    }

    private void initEvent() {
        imageView_connectAct_back.setOnClickListener(this);
        imageView_connectAct_scan.setOnClickListener(this);
        imageView_connectAct_add.setOnClickListener(this);
        listView_connectAct_devices.setOnItemClickListener(this);
    }

    private void initBLE() {

        // 获得系统蓝牙管理器
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        // 获得该设备的蓝牙适配器
        mBluetoothAdapter = bluetoothManager.getAdapter();
        System.out.println(" mBluetoothAdapter  " + mBluetoothAdapter);
        // 检查该设备是否具有蓝牙功能
        if (mBluetoothAdapter == null) {
            ToastUtils.showShort(ConnectActivity.this, R.string.not_support_bluetooth);
            return;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_connectAct_back:
                finish();
                break;
            case R.id.imageView_connectAct_scan:
                //TODO
                ToastUtils.showShort(ConnectActivity.this, "imageView_connectAct_scan");
                // 当android版本小于4.3时不支持ble协议
                if (Build.VERSION.SDK_INT < 18) {
                    ToastUtils.showShort(ConnectActivity.this, R.string.not_support_below_version_android);
                    return;
                } else {
                    ToastUtils.showShort(ConnectActivity.this, R.string.please_connect_ble);
                    if (mLeDeviceListAdapter != null) {
                        mLeDeviceListAdapter.clear();
                        mLeDeviceListAdapter.notifyDataSetChanged();
                    }
                    searchBLE();
                }
                break;
            case R.id.imageView_connectAct_add:
                //TODO
                ToastUtils.showShort(ConnectActivity.this, "imageView_connectAct_add");
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        device = mLeDeviceListAdapter.getDevice(position);
        if (device == null)
            return;
        // 如果还在查找蓝牙设备，则停止查找
        if (mScanning) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mScanning = false;
        }
        LogUtils.d(TAG, "mBluetoothLeService:" + (mBluetoothLeService == null) + ",device:" + (device == null));
        ToastUtils.showShort(this, "mBluetoothLeService:" + (mBluetoothLeService == null) + ",device:" + (device == null));
        //连接到指定蓝牙设备
        mBluetoothLeService.connect(device.getAddress());
        ToastUtils.showShort(this, "正在连接到" + device.getName());
        //将该选择的设备名称、地址写到sp中
        BleDevice mBleDevice = new BleDevice(device.getName(), device.getAddress());
        saveBleDevice2Sp(mBleDevice);
    }


    /**
     * 保存新连接的的蓝牙设备到连接记录集合set中，并格式化为json保存到sp中
     *
     * @param mBleDevice
     */
    private void saveBleDevice2Sp(BleDevice mBleDevice) {
        String oldString = SPUtils.getString(this, ConstantUtils.BIND_DEVICES, null);
        List<BleDevice> list = BleDevice.getBleDevicesFromSp(this);
        if (list == null) {
            list = new ArrayList<BleDevice>();
        }
        list.add(mBleDevice);
        //将更新后的蓝牙设备连接记录集合set格式化为json并保存到sp中
        BleDevice.setBleDevicesToSp(this,list);
        Gson mGson = new Gson();
        String newString = mGson.toJson(list);
        SPUtils.putString(this, ConstantUtils.BIND_DEVICES, newString);

    }


    private void searchBLE() {
        // 判断该蓝牙功能是否已经开启
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        } else {
            scanLeDevice(true);// 游览周围的蓝牙设备
        }
    }

    // 蓝牙设备搜索的回调接口
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             final byte[] scanRecord) {
            // 开启子线程来查询蓝牙设备
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(device);
                    mHandler.sendEmptyMessage(DISCONVER_ONE);
                }
            });
        }
    };

    /**
     * 查找附近的蓝牙设备
     *
     * @param enable
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
//			LogUtils.d(TAG, "开始搜索蓝牙设备");
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScanning) {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mHandler.sendEmptyMessage(SCAN_FINISHED);
                        ToastUtils.showShort(ConnectActivity.this, "搜索完毕");
                    }
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mLeDeviceListAdapter.clear();
            // mHandler.sendEmptyMessage(DISCONVER_ONE);// 通知蓝牙列表适配器，数据发生改变
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            showLoadingView();
        } else {
            // 停止搜索蓝牙设备
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mLoadingPopupWindow.dismiss();
        }
    }

    LoadingPopupWindow mLoadingPopupWindow;

    /**
     * 加载等待界面
     */
    private void showLoadingView() {
        mLoadingPopupWindow = new LoadingPopupWindow(this, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mLoadingPopupWindow.showAtLocation(linearLayout_connectAct_root, Gravity.CENTER, 0, 0);

    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter
                .addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        return intentFilter;
    }


    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = ConnectActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.item_listview_function_act,
                        null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view
                        .findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view
                        .findViewById(R.id.device_name);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            String deviceName = device.getName();

            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);

            viewHolder.deviceAddress.setText(device.getAddress());
            return view;
        }

    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }


}

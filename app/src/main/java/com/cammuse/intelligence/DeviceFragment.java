package com.cammuse.intelligence;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cammuse.intelligence.device.BLEUtils;
import com.cammuse.intelligence.device.BluetoothLeService;
import com.cammuse.intelligence.device.ConnectActivity;
import com.cammuse.intelligence.device.DeletePopupWindow;
import com.cammuse.intelligence.entity.BleDevice;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.SPUtils;
import com.cammuse.intelligence.utils.ToastUtils;

import java.util.List;

public class DeviceFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final java.lang.String TAG = "DeviceFragment";
    private ImageView imageView_deviceFrag_add;
    private ListView listView_deviceFragment_devices;
    private MyAdapter mAdapter;
    private List<BleDevice> list;

    /**
     * 接收蓝牙数据
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) { // 连接成功
                // 只有找到了服务特性才可以通信，才算连接成功过
                ToastUtils.showShort(DeviceFragment.this.getActivity(), "已连接");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
                    .equals(action)) {
                ToastUtils.showShort(DeviceFragment.this.getActivity(), "已断开");
                ConstantUtils.mConnected = false;
                ConstantUtils.connectedBleDevice = null;
                updateListView();
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
                ToastUtils.showShort(DeviceFragment.this.getActivity(), "可通信");
                ConstantUtils.mConnected = true;
                updateListView();
//                startVibrate();
//                setBindInfo(true);
                ToastUtils.showShort(DeviceFragment.this.getActivity(), R.string.ble_is_collected);
//                mBluetoothLeService.WriteValue("SR CHECK ACCEL ID EN");
//                startGasService();// 开始接收油门数据


            }
        }

    };

    /**
     * 更新数据
     */
    private void updateListView() {
        LogUtils.d(TAG, "updateListView");
        list = BleDevice.getBleDevicesFromSp(DeviceFragment.this.getActivity());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_tab_device, container, false);
        initView(view);
        initList();
        initEvent();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        //更新数据
        list = BleDevice.getBleDevicesFromSp(this.getActivity());
        mAdapter.notifyDataSetChanged();
        LogUtils.d(TAG, "onStart");
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mGattUpdateReceiver);
        LogUtils.d(TAG, "onStop");
    }


    private void initList() {
        list = BleDevice.getBleDevicesFromSp(this.getActivity());
        LogUtils.d(TAG, "list.size=" + list.size());
        mAdapter = new MyAdapter();
        listView_deviceFragment_devices.setAdapter(mAdapter);
    }

    private void initView(View view) {
        imageView_deviceFrag_add = (ImageView) view.findViewById(R.id.imageView_deviceFrag_add);
        listView_deviceFragment_devices = (ListView) view.findViewById(R.id.listView_deviceFragment_devices);
    }

    private void initEvent() {
        imageView_deviceFrag_add.setOnClickListener(this);
        listView_deviceFragment_devices.setOnItemClickListener(this);
        listView_deviceFragment_devices.setOnItemLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_deviceFrag_add:
                Intent addIntent = new Intent(DeviceFragment.this.getActivity(), ConnectActivity.class);
                startActivity(addIntent);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtils.d(TAG, "选择连接" + list.get(position).getName());
        // 获得系统蓝牙管理器
        BluetoothManager bluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        // 获得该设备的蓝牙适配器
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, MainActivity.REQUEST_ENABLE_BT);
            }
        } else {
            String name = list.get(position).getName();
            String addresss = list.get(position).getAddress();
            ConstantUtils.mBluetoothLeService.connect(addresss);
//            SPUtils.putString(DeviceFragment.this.getActivity(), ConstantUtils.BIND_DEVICE_NAME, name);
//            SPUtils.putString(DeviceFragment.this.getActivity(), ConstantUtils.BIND_DEVICE_ADDRESS, addresss);
            ConstantUtils.connectedBleDevice = new BleDevice(name, addresss);
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showDeletePopuWindow(view,position);
        return false;
    }


    private DeletePopupWindow mPopupWindow;

    private void showDeletePopuWindow(View target, final int position) {
        View view = LayoutInflater.from(DeviceFragment.this.getActivity()).inflate(R.layout.popupwindow_device_delete, null);
        mPopupWindow = new DeletePopupWindow(DeviceFragment.this.getActivity(), view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, R.style.anim_popup_delete);
        mPopupWindow.setOnDeleteDeviceListener(new DeletePopupWindow.OnDeleteDeviceListener() {
            @Override
            public void delete() {
                if (ConstantUtils.connectedBleDevice==null){
                    list.remove(list.get(position));
                    BleDevice.setBleDevicesToSp(DeviceFragment.this.getActivity(),list);
                }
                else if(list.get(position).getAddress().equals(ConstantUtils.connectedBleDevice.getAddress())){
                    ToastUtils.showShort(DeviceFragment.this.getActivity(), "请断开后再输出");
                }else {
                    list.remove(list.get(position));
                    BleDevice.setBleDevicesToSp(DeviceFragment.this.getActivity(), list);
                }
                updateListView();
                mPopupWindow.dismiss();
            }

            @Override
            public void cancel() {
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.showAsDropDown(target, target.getWidth() - mPopupWindow.getWidth(), -target.getHeight());
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(DeviceFragment.this.getActivity()).inflate(R.layout.item_listview_function_act, null);
                mViewHolder = new ViewHolder();
                mViewHolder.deviceName = (TextView) convertView.findViewById(R.id.device_name);
                mViewHolder.deviceAddress = (TextView) convertView.findViewById(R.id.device_address);
                mViewHolder.deviceState = (TextView) convertView.findViewById(R.id.device_state);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.deviceName.setText(list.get(position).getName());
            mViewHolder.deviceAddress.setText(list.get(position).getAddress());
            if (isConnectedState(list.get(position))) {
                mViewHolder.deviceState.setText("已连接");
            } else {
                mViewHolder.deviceState.setText("");
            }
            mViewHolder.deviceState.setTextSize(12);
            mViewHolder.deviceState.setTextColor(0xff00ff00);
            return convertView;
        }

        class ViewHolder {
            TextView deviceName;
            TextView deviceAddress;
            TextView deviceState;
        }
    }

    /**
     * 指定蓝牙设备是否已经被连接上
     *
     * @param bleDevice
     */
    private boolean isConnectedState(BleDevice bleDevice) {
        String address = SPUtils.getString(this.getActivity(), ConstantUtils.BIND_DEVICE_ADDRESS, null);
        if (ConstantUtils.connectedBleDevice == null) {
            return false;
        } else if (bleDevice.getAddress().equals(ConstantUtils.connectedBleDevice.getAddress())) {
            return true;
        } else {
            return false;
        }
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

}

package com.cammuse.intelligence.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cammuse.intelligence.BaseActivity;
import com.cammuse.intelligence.R;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.SPUtils;
import com.cammuse.intelligence.utils.ToastUtils;

public class BluetoothActivity extends BaseActivity implements OnClickListener {
	// Debug
	protected static final String TAG = "BluetoothActivity";
	// View
	private ImageView imageView_back_bluetoothAct;
	private RelativeLayout relativeLayout_connect_bluetoothAct;
	private RelativeLayout relativeLayout_modify_bluetoothAct;
	private RelativeLayout relativeLayout_unbind_bluetoothAct;
	private ImageView imageView_state_bluetoothAct;
	private TextView textView_deviceName_bluetoothAct;
	// private Dialog dialog;

	private SharedPreferences sp;
	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bluetooth);
		initView();
		initEvent();

		sp = getSharedPreferences(ConstantUtils.SP_FILENAME, MODE_PRIVATE);
		editor = sp.edit();

	}

	@Override
	protected void onStart() {
		super.onStart();
		updateView();
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
	}

	private void initView() {
		imageView_back_bluetoothAct = (ImageView) findViewById(R.id.imageView_back_bluetoothAct);
		relativeLayout_connect_bluetoothAct = (RelativeLayout) findViewById(R.id.relativeLayout_connect_bluetoothAct);
		relativeLayout_modify_bluetoothAct = (RelativeLayout) findViewById(R.id.relativeLayout_modify_bluetoothAct);
		relativeLayout_unbind_bluetoothAct = (RelativeLayout) findViewById(R.id.relativeLayout_unbind_bluetoothAct);
		imageView_state_bluetoothAct = (ImageView) findViewById(R.id.imageView_state_bluetoothAct);
		textView_deviceName_bluetoothAct = (TextView) findViewById(R.id.textView_deviceName_bluetoothAct);
		textView_deviceName_bluetoothAct.setText(SPUtils.getString(this,
				ConstantUtils.BIND_DEVICE_NAME, "未绑定"));
	}

	private void initEvent() {
		imageView_back_bluetoothAct.setOnClickListener(this);
		imageView_state_bluetoothAct.setOnClickListener(this);
		relativeLayout_modify_bluetoothAct.setOnClickListener(this);
		relativeLayout_unbind_bluetoothAct.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back_bluetoothAct:
			finish();
			break;
		case R.id.imageView_state_bluetoothAct:
			changeState();
			break;
		case R.id.relativeLayout_modify_bluetoothAct:
//			showPopupWindow();
			break;
		case R.id.relativeLayout_unbind_bluetoothAct:
			editor.putString(ConstantUtils.BIND_DEVICE_NAME, null);
			editor.putString(ConstantUtils.BIND_DEVICE_ADDRESS, null);
			editor.commit();
			textView_deviceName_bluetoothAct.setText(SPUtils.getString(
					BluetoothActivity.this, ConstantUtils.BIND_DEVICE_NAME,
					"未绑定"));
			ConstantUtils.mBluetoothLeService.disconnect();
			ToastUtils.showShort(BluetoothActivity.this, "已经解除绑定");
			break;
		}

	}

//	private void showPopupWindow() {
//		View mContentView = LayoutInflater.from(this).inflate(R.layout.popup_devicename,
//				null, false);
//		EditPopupWindow mPopupWindow = new EditPopupWindow(mContentView,
//				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);
//		mPopupWindow.setTitle("修改蓝牙名称");
//		mPopupWindow.setOnDismissListener(new OnDismissListener() {
//
//			@Override
//			public void onDismiss() {
//				setBackgroudToDark(false);
//			}
//		});
//
//		mPopupWindow.setOnSubmitListener(new OnSubmitListener() {
//
//			@Override
//			public void onSubmit(String name) {
//				if (name == null || name.trim().length() == 0) {
//					return;
//				}
//				SPUtils.putString(BluetoothActivity.this,
//						ConstantUtils.BIND_DEVICE_NAME, name.trim());
//				textView_deviceName_bluetoothAct.setText(SPUtils.getString(
//						BluetoothActivity.this, ConstantUtils.BIND_DEVICE_NAME,
//						"未绑定"));
//			}
//		});
//		mPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
//
//		// 设置可以获取焦点，否则弹出菜单中的EditText是无法获取输入的
//		mPopupWindow.setFocusable(true);
//		// 防止虚拟软键盘被弹出菜单遮住
//		mPopupWindow
//				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//		// 在底部显示
//		mPopupWindow.showAtLocation(relativeLayout_modify_bluetoothAct,
//				Gravity.BOTTOM, 0, 0);
//		setBackgroudToDark(true);
//	}

	/**
	 * 弹窗出现、消失时，控制背景窗口的变量、变暗
	 * 
	 * @param toDark
	 */
	protected void setBackgroudToDark(boolean toDark) {
		if (toDark) {
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = 0.3f;
			getWindow().setAttributes(lp);
		} else {
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = 1.0f;
			getWindow().setAttributes(lp);
		}

	}

	private void changeState() {
		if (ConstantUtils.mConnected) {
			ConstantUtils.mBluetoothLeService.disconnect();
		} else {
			String deviceName = sp.getString(ConstantUtils.BIND_DEVICE_NAME,
					null);
			String deviceAddress = sp.getString(
					ConstantUtils.BIND_DEVICE_ADDRESS, null);
			if (TextUtils.isEmpty(deviceAddress)) {
				ToastUtils.showShort(BluetoothActivity.this, "当前没有绑定蓝牙设备");
				Log.i(TAG, "当前没有绑定蓝牙设备");
				return;
			}
			ConstantUtils.mBluetoothLeService.connect(deviceAddress);
		}

	}

	/**
	 * 接收蓝牙数据
	 */
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) { // 连接成功
				// 只有找到了服务特性可以通信，才算连接成功

			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				ConstantUtils.mConnected = false;
				updateView();
				Log.i(TAG, "连接断开");
				ToastUtils.showShort(BluetoothActivity.this, "连接断开");
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				Log.i(TAG, "连接设备成功");
				ConstantUtils.mConnected = true;
				updateView();
				ToastUtils.showShort(BluetoothActivity.this, "连接成功");
			}
		}

	};

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		return intentFilter;
	}

	protected void updateView() {
		if (ConstantUtils.mConnected) {
			imageView_state_bluetoothAct
					.setImageResource(R.mipmap.bluetooth_open_bg);
		} else {
			imageView_state_bluetoothAct
					.setImageResource(R.mipmap.bluetooth_close_bg);
		}

	}

}

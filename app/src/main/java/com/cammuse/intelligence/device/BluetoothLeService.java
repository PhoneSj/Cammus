/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cammuse.intelligence.device;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeService extends Service implements Serializable {
	private final static String TAG = "BluetoothLeService";

	static private BluetoothManager mBluetoothManager;
	static private BluetoothAdapter mBluetoothAdapter;
	static private BluetoothGatt mBluetoothGatt;

	public final static String ACTION_GATT_CONNECTED = "com.example.phonetest.bluetooth.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.phonetest.bluetooth.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.phonetest.bluetooth.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE = "com.example.phonetest.bluetooth.ACTION_DATA_AVAILABLE";
	public final static String EXTRA_DATA = "com.example.phonetest.bluetooth.EXTRA_DATA";

	public final static UUID UUID_NOTIFY = UUID
			.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
	public final static UUID UUID_SERVICE = UUID
			.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

	public static BluetoothGattCharacteristic mNotifyCharacteristic;

	/**
	 * 发送蓝牙数据
	 * 
	 * @param strValue
	 */
	public void WriteValue(String data) {
		if (!ConstantUtils.mConnected) {
			return;
		}
		LogUtils.d(TAG, "data:" + data);
		// 将需要发送的数据保存包BluetoothGattCharacteristic实例中
		mNotifyCharacteristic.setValue(data.getBytes());
		// 将存有数据的BluetoothGattCharacteristic实例写入
		mBluetoothGatt.writeCharacteristic(mNotifyCharacteristic);
	}

	/**
	 * 找出Gatt的服务BluetoothGattService
	 * 
	 * @param gattServices
	 */
	public void findService(List<BluetoothGattService> gattServices) {
		Log.i(TAG, "findService........");
		for (BluetoothGattService gattService : gattServices) {
			Log.d(TAG, "服务：" + gattService.getUuid().toString());
			if (gattService.getUuid().toString()
					.equalsIgnoreCase(UUID_SERVICE.toString())) {
				// 获取该服务中的特性BluetoothGattCharacteristic
				List<BluetoothGattCharacteristic> gattCharacteristics = gattService
						.getCharacteristics();
				for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
					Log.d(TAG, "特征：" + gattCharacteristic.getUuid().toString());
					if (gattCharacteristic.getUuid().toString()
							.equalsIgnoreCase(UUID_NOTIFY.toString())) {
						mNotifyCharacteristic = gattCharacteristic;
						setCharacteristicNotification(gattCharacteristic, true);
						broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
						return;
					}
				}
			}
		}
	}

	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			String intentAction;
			Log.i(TAG, "oldStatus=" + status + " NewStates=" + newState);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				if (newState == BluetoothProfile.STATE_CONNECTED) {
					intentAction = ACTION_GATT_CONNECTED;
					broadcastUpdate(intentAction);
					Log.i(TAG, "Connected to GATT server.");
					try {
						Thread.sleep(300);
						mBluetoothGatt.discoverServices();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
					intentAction = ACTION_GATT_DISCONNECTED;
					Log.i(TAG, "Disconnected from GATT server.");
					broadcastUpdate(intentAction);
				}
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			Log.i(TAG, "onServicesDiscovered........");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				Log.w(TAG, "onServicesDiscovered received: " + status);
				findService(gatt.getServices());
			} else {
				if (mBluetoothGatt.getDevice().getUuids() == null)
					Log.w(TAG, "onServicesDiscovered received: " + status);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
			Log.i(TAG, "onCharacteristicRead");
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			Log.i(TAG, "onCharacteristicChanged");
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			Log.i(TAG, "OnCharacteristicWrite");
		}

		@Override
		public void onDescriptorRead(BluetoothGatt gatt,
				BluetoothGattDescriptor bd, int status) {
			Log.i(TAG, "onDescriptorRead");
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor bd, int status) {
			Log.i(TAG, "onDescriptorWrite");
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int a, int b) {
			Log.i(TAG, "onReadRemoteRssi");
		}

		@Override
		public void onReliableWriteCompleted(BluetoothGatt gatt, int a) {
			Log.i(TAG, "onReliableWriteCompleted");
		}

	};

	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}

	private void broadcastUpdate(final String action,
			final BluetoothGattCharacteristic characteristic) {
		final Intent intent = new Intent(action);

		final byte[] data = characteristic.getValue();
		if (data != null && data.length > 0) {
			intent.putExtra(EXTRA_DATA, new String(data));
			Log.i(TAG, "广播数据接收：" + new String(data));
		}
		sendBroadcast(intent);
	}

	public class LocalBinder extends Binder {
		public BluetoothLeService getService() {
			return BluetoothLeService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IBinder mBinder = new LocalBinder();

	/**
	 * 初始化本地蓝牙设备
	 * 
	 * @return Return true if the initialization is successful.
	 */
	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		// System.out.println("aaaaaaaaaa");
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 * 
	 * @param address
	 *            The device address of the destination device.
	 * 
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}
		System.out.println("  " + mBluetoothAdapter + "  " + address);
		/*
		 * // Previously connected device. Try to reconnect. if
		 * (mBluetoothDeviceAddress != null &&
		 * address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
		 * Log.d(TAG,
		 * "Trying to use an existing mBluetoothGatt for connection."); if
		 * (mBluetoothGatt.connect()) { mConnectionState = STATE_CONNECTING;
		 * return true; } else { return false; } }
		 */
		// 通过地址得到远程蓝牙设备
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the
		// autoConnect
		// parameter to false.
		if (mBluetoothGatt != null) {
			mBluetoothGatt.close();
			mBluetoothGatt = null;
		}
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		// mBluetoothGatt.connect();

		Log.d(TAG, "Trying to create a new connection.");
		return true;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 * 
	 * @param characteristic
	 *            The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}

	/**
	 * Enables or disables notification on a give characteristic.
	 * 
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 */
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
	}

	/**
	 * Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 * 
	 * @return A {@code List} of supported services.
	 */
	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;

		return mBluetoothGatt.getServices();
	}
}

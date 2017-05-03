package com.cammuse.intelligence.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cammuse.intelligence.common.CustomDialog;

public class DialogUtils {

	private static CustomDialog dialog;

	private static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == ConstantUtils.HANDLER_DIALOG_TIMEOUT) {
				if (dialog != null) {
					dialog.cancel();
				}
			}
		}

	};

	public static void showLoadingDialog(Context context, int theme,
			int resLayout, int delay_second, String text) {
		dialog = new CustomDialog(context, theme, resLayout,text);
		dialog.setContentView(resLayout);
		dialog.setCanceledOnTouchOutside(false);
		setTimeOut(10);
		dialog.show();

	}

	public static void cancelLoadingdialog() {
		if (dialog != null) {
			dialog.cancel();
		}
	}

	private static void setTimeOut(final int second) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000 * second);
					mHandler.sendEmptyMessage(ConstantUtils.HANDLER_DIALOG_TIMEOUT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}

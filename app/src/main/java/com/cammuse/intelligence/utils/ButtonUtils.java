package com.cammuse.intelligence.utils;

public class ButtonUtils {

	private static long lastClickTime;

	/**
	 * 防止按钮快速点击
	 * 
	 * @return
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}

package com.cammuse.intelligence.entity;

import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;

public class UserLevel {

	private int level_large;// 范围为0-7
	private int level_small;// 范围为0-4

	public UserLevel(int level_large, int level_small) {
		this.level_large = level_large;
		this.level_small = level_small;
	}

	public int getLevel_large() {
		return level_large;
	}

	public void setLevel_large(int level_large) {
		this.level_large = level_large;
	}

	public int getLevel_small() {
		return level_small;
	}

	public void setLevel_small(int level_small) {
		this.level_small = level_small;
	}

	/**
	 * 将油门等级Level转化为用户等价UserLevel
	 * 
	 * @param level
	 * @return
	 */
	public static UserLevel level2UserLevel(int level) {
		UserLevel mUserLevel = null;
		LogUtils.d("Test", "min:" + ConstantUtils.LEVEL_MIN + "  max:"
				+ ConstantUtils.LEVEL_MAX);
		if (level >= ConstantUtils.LEVEL_MIN
				&& level <= ConstantUtils.LEVEL_MAX) {
			mUserLevel = new UserLevel((level - 1) / 5, (level - 1) % 5);
		} else {
			LogUtils.e("---level---出错:" + level);
			mUserLevel = level2UserLevel(ConstantUtils.DEFAULT_AUTOMATIC_CURRENT_LEVEL);
			LogUtils.e("---level2UserLevel---出错");
		}
		return mUserLevel;
	}

	public static int userLevel2Level(UserLevel mUserLevel) {
		if (mUserLevel == null) {
			return 0;
		}
		return mUserLevel.getLevel_large() * 5 + mUserLevel.getLevel_small()
				+ 1;
	}

}

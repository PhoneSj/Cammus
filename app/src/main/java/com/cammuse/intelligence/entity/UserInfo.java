package com.cammuse.intelligence.entity;

import android.content.Context;

import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.SPUtils;

import java.util.regex.Pattern;

public class UserInfo {

    private static final java.lang.String TAG = "UserInfo";
    private String phone;
    private String password;
    private String nickname;
    private String registerTime;
    private static boolean loginState = false;

    public UserInfo(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public static void setLoginState(boolean state) {
        loginState = state;
    }

    public static boolean getLoginState() {
        return loginState;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    /**
     * 判断字符串是否全为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 核对用户名格式是否合法
     */
    public static boolean checkUsername(String username) {
        if (username == null) {
            return false;
        } else if (username.length() != 11) {
            return false;
        } else if (!isNumeric(username)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检查字符串长度
     *
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        if (password == null) {
            return false;
        } else if (password.length() >= 6 && password.length() <= 15) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 保存用户信息到sp中
     *
     * @param userInfo
     */
    public static void saveUserInfo2Sp(Context context, UserInfo userInfo) {
        SPUtils.putString(context, ConstantUtils.SP_KEY_USERNAME, userInfo.getPhone());
        SPUtils.putString(context, ConstantUtils.SP_KEY_PASSWORD, userInfo.getPassword());
        LogUtils.d(TAG, "save...");
    }


}

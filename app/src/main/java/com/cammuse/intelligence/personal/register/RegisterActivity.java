package com.cammuse.intelligence.personal.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.utils.ButtonUtils;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.DialogUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.ToastUtils;

import org.json.JSONObject;


/**
 * Created by Administrator on 2016/2/22.
 */


public class RegisterActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private ImageView imageView_registerAct_back;
    private FrameLayout frameLayout_registerAct_container;

    private RegisterFirstFragment mFirst;
    private RegisterSecondFragment mSecond;
    private RegisterThirdFragment mThird;
    private RegisterFourthFragment mFourth;
    private RegisterFifthFragment mFifth;

    private String phone;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DialogUtils.cancelLoadingdialog();

            JSONObject result = null;
            String status = null;
            switch (msg.what) {
                case ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_SENDCODE:
                    result = (JSONObject) msg.obj;
                    status = result.optString("status");
                    LogUtils.d(TAG, status);
                    if (status.equals("该用户未注册")) {
                        Intent registerIntent = new Intent(
                                RegisterActivity.this, RegisterActivity.class);
                        startActivity(registerIntent);
                    } else if (status.equals("验证码已发送")) {
                        ToastUtils.showShort(RegisterActivity.this, "验证码已发送");
                    } else {
                        ToastUtils.showShort(RegisterActivity.this, status);
                        LogUtils.d(TAG, "请求发送验证码未知错误");
                    }
                    break;
                case ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_CHECKCODE:
                    result = (JSONObject) msg.obj;
                    status = result.optString("status");
                    LogUtils.d(TAG, status);
                    if (status.equals("验证通过")) {
                        setSelect(3);
                    } else if (status.equals("验证码错误")) {
                        ToastUtils.showShort(RegisterActivity.this, status);
                    } else {
                        ToastUtils.showShort(RegisterActivity.this, status);
                        LogUtils.d(TAG, "请求发送验证码未知错误");
                    }
                    break;
                case ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_SUBMIT:
                    result = (JSONObject) msg.obj;
                    status = result.optString("status");
                    LogUtils.d(TAG, status);
                    if (status.equals("用户已经存在")) {
                        setSelect(2);
                    } else if (status.equals("注册成功")) {
                        setSelect(4);
                    } else {
                        ToastUtils.showShort(RegisterActivity.this, status);
                        LogUtils.d(TAG, "注册返回的位置状态");
                    }
                    break;
                case ConstantUtils.HANDLER_WHAT_REQUEST_FAILURE:
                    Log.e(TAG, "网络出现异常");
                    ToastUtils.showShort(RegisterActivity.this, "网络出现异常");
                    break;
                default:
                    ToastUtils.showShort(RegisterActivity.this, "网络请求id出现错误值");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initEvent();
        setSelect(0);
    }

    private void initView() {
        imageView_registerAct_back = (ImageView) findViewById(R.id.imageView_registerAct_back);
        frameLayout_registerAct_container = (FrameLayout) findViewById(R.id.frameLayout_registerAct_container);
    }

    public void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                if (mFirst == null) {
                    mFirst = new RegisterFirstFragment();
                    transaction.add(R.id.frameLayout_registerAct_container, mFirst);
                } else {
                    transaction.show(mFirst);
                }
                break;
            case 1:
                if (mSecond == null) {
                    mSecond = new RegisterSecondFragment();
                    transaction.add(R.id.frameLayout_registerAct_container, mSecond);
                } else {
                    transaction.show(mSecond);
                }
                break;
            case 2:
                if (mThird == null) {
                    mThird = new RegisterThirdFragment();
                    transaction.add(R.id.frameLayout_registerAct_container, mThird);
                } else {
                    transaction.show(mThird);
                }
                break;
            case 3:
                if (mFourth == null) {
                    mFourth = new RegisterFourthFragment();
                    transaction.add(R.id.frameLayout_registerAct_container, mFourth);
                } else {
                    transaction.show(mFourth);
                }
                break;
            case 4:
                if (mFifth == null) {
                    mFifth = new RegisterFifthFragment();
                    transaction.add(R.id.frameLayout_registerAct_container, mFifth);
                } else {
                    transaction.show(mFifth);
                }
                break;
        }
        transaction.commit();

    }


    private void hideFragment(FragmentTransaction transaction) {
        if (mFirst != null) {
            transaction.hide(mFirst);
        }
        if (mSecond != null) {
            transaction.hide(mSecond);
        }
        if (mThird != null) {
            transaction.hide(mThird);
        }
        if (mFourth != null) {
            transaction.hide(mFourth);
        }
        if (mFifth != null) {
            transaction.hide(mFifth);
        }

    }

    private void initEvent() {
        imageView_registerAct_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (ButtonUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.imageView_registerAct_back:
                finish();
                break;
        }
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public Handler getHandler() {
        return handler;
    }
}

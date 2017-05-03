package com.cammuse.intelligence.personal.resetpwd;

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
import com.cammuse.intelligence.personal.register.RegisterActivity;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.DialogUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.ToastUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/2/23.
 */
public class ResetPwdActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "ResetPwdActivity";
    private ImageView imageView_resetPwdAct_back;
    private FrameLayout frameLayout_resetPwdAct_container;

    private ResetPwsFirstFragment mFirst;
    private ResetPwdSecondFragment mSecond;
    private ResetPwdThirdFragment mThird;
    private ResetPwdFourthFragment mFourth;
    private ResetPwdFifthFragment mFifth;

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
                                ResetPwdActivity.this, RegisterActivity.class);
                        startActivity(registerIntent);
                    } else if (status.equals("验证码已发送")) {
                        setSelect(2);
                    } else {
                        ToastUtils.showShort(ResetPwdActivity.this, status);
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
                        ToastUtils.showShort(ResetPwdActivity.this, status);
                    } else {
                        ToastUtils.showShort(ResetPwdActivity.this, status);
                        LogUtils.d(TAG, "请求发送验证码未知错误");
                    }
                    break;
                case ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_SUBMIT:
                    result = (JSONObject) msg.obj;
                    status = result.optString("status");
                    LogUtils.d(TAG, status);
                    if (status.equals("修改密码成功")) {
                        setSelect(4);
                    } else {
                        ToastUtils.showShort(ResetPwdActivity.this, status);
                    }
                    break;
                case ConstantUtils.HANDLER_WHAT_REQUEST_FAILURE:
                    Log.e(TAG, "网络出现异常");
                    ToastUtils.showShort(ResetPwdActivity.this, "网络出现异常");
                    break;
                default:
                    ToastUtils.showShort(ResetPwdActivity.this, "网络请求id出现错误值");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpwd);
        initView();
        initEvent();
        setSelect(0);
    }

    private void initView() {
        imageView_resetPwdAct_back = (ImageView) findViewById(R.id.imageView_resetPwdAct_back);
    }

    private void initEvent() {
        imageView_resetPwdAct_back.setOnClickListener(this);
    }

    public void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                if (mFirst == null) {
                    mFirst = new ResetPwsFirstFragment();
                    transaction.add(R.id.frameLayout_resetPwdAct_container, mFirst);
                } else {
                    transaction.show(mFirst);
                }
                break;
            case 1:
                if (mSecond == null) {
                    mSecond = new ResetPwdSecondFragment();
                    transaction.add(R.id.frameLayout_resetPwdAct_container, mSecond);
                } else {
                    transaction.show(mSecond);
                }
                break;
            case 2:
                if (mThird == null) {
                    mThird = new ResetPwdThirdFragment();
                    transaction.add(R.id.frameLayout_resetPwdAct_container, mThird);
                } else {
                    transaction.show(mThird);
                }
                break;
            case 3:
                if (mFourth == null) {
                    mFourth = new ResetPwdFourthFragment();
                    transaction.add(R.id.frameLayout_resetPwdAct_container, mFourth);
                } else {
                    transaction.show(mFourth);
                }
                break;
            case 4:
                if (mFifth == null) {
                    mFifth = new ResetPwdFifthFragment();
                    transaction.add(R.id.frameLayout_resetPwdAct_container, mFifth);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_resetPwdAct_back:
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

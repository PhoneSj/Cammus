package com.cammuse.intelligence.personal.resetpwd;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.common.EdittextWithClear;
import com.cammuse.intelligence.net.VolleyUtils;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.ToastUtils;


/**
 * Created by Administrator on 2016/2/23.
 */
public class ResetPwdThirdFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ResetPwdThirdFragment";
    private TextView textView_resetPwdFrag2_userInfo;
    private EdittextWithClear edittextWithClear_resetPwdFrag2_remoteCode;
    private Button button_resetPwdFrag2_getRemoteCode;
    private Button button_resetPwdFrag2_ok;


    private TimeCount mTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resetpwd2, container, false);
        mTimer = new TimeCount(2 * 60000, 1000);
        mTimer.start();
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        textView_resetPwdFrag2_userInfo = (TextView) view.findViewById(R.id.textView_resetPwdFrag2_userInfo);
        edittextWithClear_resetPwdFrag2_remoteCode = (EdittextWithClear) view.findViewById(R.id.edittextWithClear_resetPwdFrag2_remoteCode);
        button_resetPwdFrag2_getRemoteCode = (Button) view.findViewById(R.id.button_resetPwdFrag2_getRemoteCode);
        button_resetPwdFrag2_ok = (Button) view.findViewById(R.id.button_resetPwdFrag2_ok);
    }

    private void initEvent() {
        button_resetPwdFrag2_getRemoteCode.setOnClickListener(this);
        button_resetPwdFrag2_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String phoneNumber;
        String url;
        Handler mHandler = ((ResetPwdActivity) ResetPwdThirdFragment.this.getActivity()).getHandler();
        switch (v.getId()) {
            case R.id.button_resetPwdFrag2_getRemoteCode:
                ToastUtils.showShort(ResetPwdThirdFragment.this.getActivity(), "button_resetPwdFrag2_getRemoteCode");
                phoneNumber = ((ResetPwdActivity) ResetPwdThirdFragment.this.getActivity()).getPhone();
                url = ConstantUtils.URL_REQUEST_CODE + "?phone="
                        + phoneNumber;
                LogUtils.d(TAG, "url:" + url);
                VolleyUtils.requestSendCode(ResetPwdThirdFragment.this.getActivity(), url, mHandler);
                mTimer.start();
                break;
            case R.id.button_resetPwdFrag2_ok:
                ToastUtils.showShort(ResetPwdThirdFragment.this.getActivity(), "button_resetPwdFrag2_ok");
                // http://www.cammus.com/appcode/edituserinfo/checkcode.aspx?phone=13924582040&code=2101
                String code = edittextWithClear_resetPwdFrag2_remoteCode.getText().toString().trim();
                phoneNumber = ((ResetPwdActivity) ResetPwdThirdFragment.this.getActivity()).getPhone();
                url = ConstantUtils.URL_CHECK_CODE + "?phone=" + phoneNumber + "&code=" + code;
                LogUtils.d(TAG, "url:" + url);
                VolleyUtils.requestCheckCode(ResetPwdThirdFragment.this.getActivity(), url,
                        mHandler);
                break;
        }
    }

    /**
     * 短信计时器
     */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            button_resetPwdFrag2_getRemoteCode.setText("重新发送("
                    + millisUntilFinished / 1000 + ")");
            button_resetPwdFrag2_getRemoteCode.setEnabled(false);
            button_resetPwdFrag2_ok.setEnabled(true);
        }

        @Override
        public void onFinish() {
            button_resetPwdFrag2_getRemoteCode.setText("获取验证码");
            button_resetPwdFrag2_getRemoteCode.setEnabled(true);
            button_resetPwdFrag2_ok.setEnabled(false);
        }
    }
}

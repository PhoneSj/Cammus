package com.cammuse.intelligence.personal.register;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.common.EdittextWithClear;
import com.cammuse.intelligence.net.VolleyUtils;
import com.cammuse.intelligence.utils.ButtonUtils;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.ToastUtils;

/**
 * Created by Administrator on 2016/2/22.
 */
public class RegisterSecondFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RegisterSecondFragment";
    private TextView textView_registerFrag1_userInfo;
    private EdittextWithClear edittextWithClear_registerFrag1_remoteCode;
    private Button button_registerFrag1_getRemoteCode;
    private LinearLayout linearLayout_registerFrag1_prompt;
    private TextView textView_registerFrag1_prompt;
    private Button button_registerFrag1_next;
    private Button button_registerFrag1_back;

    private TimeCount mTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register1, container, false);
        mTimer = new TimeCount(2 * 60000, 1000);
        mTimer.start();
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        textView_registerFrag1_userInfo = (TextView) view.findViewById(R.id.textView_registerFrag1_userInfo);
        edittextWithClear_registerFrag1_remoteCode = (EdittextWithClear) view.findViewById(R.id.edittextWithClear_registerFrag1_remoteCode);
        button_registerFrag1_getRemoteCode = (Button) view.findViewById(R.id.button_registerFrag1_getRemoteCode);
        linearLayout_registerFrag1_prompt = (LinearLayout) view.findViewById(R.id.linearLayout_registerFrag1_prompt);
        textView_registerFrag1_prompt = (TextView) view.findViewById(R.id.textView_registerFrag1_prompt);
        button_registerFrag1_next = (Button) view.findViewById(R.id.button_registerFrag1_next);
        button_registerFrag1_back = (Button) view.findViewById(R.id.button_registerFrag1_back);
        String content = "已经发送一条验证短信至+86 " + ((RegisterActivity) RegisterSecondFragment.this.getActivity()).getPhone();
        textView_registerFrag1_userInfo.setText(content);
        getRemoteCode();
    }

    private void initEvent() {
        button_registerFrag1_getRemoteCode.setOnClickListener(this);
        button_registerFrag1_next.setOnClickListener(this);
        button_registerFrag1_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isFastDoubleClick()) {
            return;
        }
        String phoneNumber;
        String url;
        Handler mHandler = ((RegisterActivity) RegisterSecondFragment.this.getActivity()).getHandler();
        switch (v.getId()) {
            case R.id.button_registerFrag1_getRemoteCode:
                ToastUtils.showShort(RegisterSecondFragment.this.getActivity(), "button_registerFrag1_getRemoteCode");
                getRemoteCode();
                break;
            case R.id.button_registerFrag1_next:
                ToastUtils.showShort(RegisterSecondFragment.this.getActivity(), "button_registerFrag1_next");
                String code = edittextWithClear_registerFrag1_remoteCode.getText().toString().trim();
                if (code == null) {
                    textView_registerFrag1_prompt.setText("不能为空");
                    linearLayout_registerFrag1_prompt.setVisibility(View.VISIBLE);
                }
                phoneNumber = ((RegisterActivity) RegisterSecondFragment.this.getActivity()).getPhone();
                url = ConstantUtils.URL_CHECK_CODE + "?phone=" + phoneNumber + "&code=" + code;
                LogUtils.d(TAG, "url:" + url);
                VolleyUtils.requestCheckCode(RegisterSecondFragment.this.getActivity(), url, mHandler);
                break;
            case R.id.button_registerFrag1_back:
                ToastUtils.showShort(RegisterSecondFragment.this.getActivity(), "button_registerFrag1_back");
                ((RegisterActivity) getActivity()).setSelect(0);
                break;
        }
    }

    /**
     * 请求发送短信验证码
     */
    public void getRemoteCode() {
        String phoneNumber = ((RegisterActivity) RegisterSecondFragment.this.getActivity()).getPhone();
        String url = ConstantUtils.URL_REQUEST_CODE + "?phone=" + phoneNumber;
        Handler mHandler = ((RegisterActivity) RegisterSecondFragment.this.getActivity()).getHandler();
        LogUtils.d(TAG, "url:" + url);
        VolleyUtils.requestSendCode(RegisterSecondFragment.this.getActivity(), url, mHandler);
        mTimer.start();
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

            button_registerFrag1_getRemoteCode.setText("重新发送("
                    + millisUntilFinished / 1000 + ")");
            button_registerFrag1_getRemoteCode.setEnabled(false);
            button_registerFrag1_next.setEnabled(true);
        }

        @Override
        public void onFinish() {
            button_registerFrag1_getRemoteCode.setText("获取验证码");
            button_registerFrag1_getRemoteCode.setEnabled(true);
            button_registerFrag1_next.setEnabled(false);
        }
    }
}

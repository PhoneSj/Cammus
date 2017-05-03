package com.cammuse.intelligence.personal.resetpwd;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.net.VolleyUtils;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.ToastUtils;

/**
 * Created by Administrator on 2016/2/23.
 */
public class ResetPwdSecondFragment extends Fragment implements View.OnClickListener {

    private static final java.lang.String TAG = "ResetPwdSecondFragment";
    private TextView textView_resetPwdFrag1_userInfo;
    private Button button_resetPwdFrag1_getRemoteCode;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resetpwd1, container, false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        textView_resetPwdFrag1_userInfo = (TextView) view.findViewById(R.id.textView_resetPwdFrag1_userInfo);
        button_resetPwdFrag1_getRemoteCode = (Button) view.findViewById(R.id.button_resetPwdFrag1_getRemoteCode);

        String content = "点击发送短信验证按钮，将会发送一条有验证码的短信至手机" + ((ResetPwdActivity) ResetPwdSecondFragment.this.getActivity()).getPhone();
        textView_resetPwdFrag1_userInfo.setText(content);
    }

    private void initEvent() {
        button_resetPwdFrag1_getRemoteCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_resetPwdFrag1_getRemoteCode:
                ToastUtils.showShort(ResetPwdSecondFragment.this.getActivity(), "button_resetPwdFrag1_getRemoteCode");
                String phoneNumber = ((ResetPwdActivity) ResetPwdSecondFragment.this.getActivity()).getPhone();
                String url = ConstantUtils.URL_REQUEST_CODE + "?phone="
                        + phoneNumber;
                LogUtils.d(TAG, "url:" + url);
                VolleyUtils.requestSendCode(ResetPwdSecondFragment.this.getActivity(), url,
                        ((ResetPwdActivity) ResetPwdSecondFragment.this.getActivity()).getHandler());

                break;
        }
    }
}

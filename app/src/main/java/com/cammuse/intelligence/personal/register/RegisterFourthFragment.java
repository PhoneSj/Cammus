package com.cammuse.intelligence.personal.register;

import android.os.Bundle;
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
import com.cammuse.intelligence.entity.UserInfo;
import com.cammuse.intelligence.net.VolleyUtils;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;


/**
 * Created by Administrator on 2016/2/23.
 */
public class RegisterFourthFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RegisterFourthFragment";
    private TextView textView_registerFrag3_userinfo;
    private EdittextWithClear editTextWithClear_registerFrag3_firstPwd;
    private EdittextWithClear editTextWithClear_registerFrag3_secondPwd;
    private LinearLayout linearLayout_registerFrag3_prompt;
    private TextView textView_registerFrag3_prompt;
    private Button button_registerFrag3_submit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register3, container, false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        textView_registerFrag3_userinfo = (TextView) view.findViewById(R.id.textView_registerFrag3_userinfo);
        editTextWithClear_registerFrag3_firstPwd = (EdittextWithClear) view.findViewById(R.id.editTextWithClear_registerFrag3_firstPwd);
        editTextWithClear_registerFrag3_secondPwd = (EdittextWithClear) view.findViewById(R.id.editTextWithClear_registerFrag3_secondPwd);
        linearLayout_registerFrag3_prompt = (LinearLayout) view.findViewById(R.id.linearLayout_registerFrag3_prompt);
        button_registerFrag3_submit = (Button) view.findViewById(R.id.button_registerFrag3_submit);
        textView_registerFrag3_prompt = (TextView) view.findViewById(R.id.textView_registerFrag3_prompt);
        textView_registerFrag3_userinfo.setText("你注册的手机号为" + ((RegisterActivity) RegisterFourthFragment.this.getActivity()).getPhone());
    }

    private void initEvent() {
        button_registerFrag3_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_registerFrag3_submit:
                //TODO
//                ToastUtils.showShort(RegisterFourthFragment.this.getActivity(), "button_registerFrag3_submit");
//                ((RegisterActivity) RegisterFourthFragment.this.getActivity()).setSelect(4);

                String first = editTextWithClear_registerFrag3_firstPwd.getText().toString().trim();
                String second = editTextWithClear_registerFrag3_secondPwd.getText().toString().trim();
                if (!UserInfo.checkPassword(first)) {
                    textView_registerFrag3_prompt.setText("密码不符合规则");
                    linearLayout_registerFrag3_prompt.setVisibility(View.VISIBLE);
                } else if (!first.equals(second)) {
                    textView_registerFrag3_prompt.setText("两次密码输入不一致");
                    linearLayout_registerFrag3_prompt.setVisibility(View.VISIBLE);
                } else {
                    String phoneNumber = ((RegisterActivity) RegisterFourthFragment.this.getActivity()).getPhone();
                    String password = first;
                    Handler mHandler = ((RegisterActivity) RegisterFourthFragment.this.getActivity()).getHandler();
                    String url = ConstantUtils.URL_REGISTER + "?phone=" + phoneNumber
                            + "&password=" + password;
                    LogUtils.d(TAG, "url:" + url);
                    VolleyUtils.submitUsernameAndPassword(RegisterFourthFragment.this.getActivity(), url, mHandler);

                }

                break;
        }
    }
}

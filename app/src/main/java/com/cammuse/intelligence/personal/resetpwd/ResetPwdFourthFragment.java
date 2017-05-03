package com.cammuse.intelligence.personal.resetpwd;

import android.os.Bundle;
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
 * Created by Administrator on 2016/2/23.
 */
public class ResetPwdFourthFragment extends Fragment implements View.OnClickListener {

    private static final java.lang.String TAG = "ResetPwdFourthFragment";
    private EdittextWithClear editTextWithClear_resetPwdFrag3_firstPwd;
    private EdittextWithClear editTextWithClear_resetPwdFrag3_secondPwd;
    private LinearLayout linearLayout_resetPwdFrag3_prompt;
    private TextView textView_resetPwdFrag3_prompt;
    private Button button_resetPwdFrag3_submit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resetpwd3, container, false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        editTextWithClear_resetPwdFrag3_firstPwd = (EdittextWithClear) view.findViewById(R.id.editTextWithClear_resetPwdFrag3_firstPwd);
        editTextWithClear_resetPwdFrag3_secondPwd = (EdittextWithClear) view.findViewById(R.id.editTextWithClear_resetPwdFrag3_secondPwd);
        linearLayout_resetPwdFrag3_prompt = (LinearLayout) view.findViewById(R.id.linearLayout_resetPwdFrag3_prompt);
        textView_resetPwdFrag3_prompt = (TextView) view.findViewById(R.id.textView_resetPwdFrag3_prompt);
        button_resetPwdFrag3_submit = (Button) view.findViewById(R.id.button_resetPwdFrag3_submit);

    }

    private void initEvent() {
        button_resetPwdFrag3_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.button_resetPwdFrag3_submit:
                //TODO
                ToastUtils.showShort(ResetPwdFourthFragment.this.getActivity(), "button_resetPwdFrag3_submit");
                String first = editTextWithClear_resetPwdFrag3_firstPwd.getText().toString().trim();
                String second = editTextWithClear_resetPwdFrag3_secondPwd.getText().toString().trim();
                if (first.equals("") || second.equals("")) {
                    textView_resetPwdFrag3_prompt.setText("密码不能为空");
                } else if (first.length() != second.length()) {
                    textView_resetPwdFrag3_prompt.setText("两次密码输入不一致");
                } else {
                    //((ResetPwdActivity) ResetPwdFourthFragment.this.getActivity()).setSelect(4);
                    // http://www.cammus.com/appcode/edituserinfo/setpwd.aspx?phone=13924582040&password=111111
                    String phoneNumber = ((ResetPwdActivity) ResetPwdFourthFragment.this.getActivity()).getPhone();
                    android.os.Handler mHandler = ((ResetPwdActivity) ResetPwdFourthFragment.this.getActivity()).getHandler();
                    String password = first;
                    String url = ConstantUtils.URL_MODIFY_CODE + "?phone="
                            + phoneNumber + "&password=" + password;
                    LogUtils.d(TAG, "url:" + url);
                    VolleyUtils.submitUsernameAndPassword(ResetPwdFourthFragment.this.getActivity(),
                            url, mHandler);
                }
                break;
        }
    }


}

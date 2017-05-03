package com.cammuse.intelligence.personal.resetpwd;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.common.EdittextWithClear;
import com.cammuse.intelligence.common.NativeCode;
import com.cammuse.intelligence.entity.UserInfo;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.ToastUtils;

/**
 * Created by Administrator on 2016/2/23.
 */
public class ResetPwsFirstFragment extends Fragment implements View.OnClickListener {

    private static final java.lang.String TAG = "ResetPwsFirstFragment";
    private EdittextWithClear editTextWithClear_resetPwdFrag0_username;
    private EdittextWithClear edittextWithClear_resetPwdFrag0_imgCode;
    private ImageView imageView_resetPwdFrag0_imgCode;
    private LinearLayout linearLayout_resetPwdFrag0_prompt;
    private TextView textView_resetPwdFrag0_prompt;
    private Button button_resetPwdFrag0_goto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resetpwd0, container, false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        editTextWithClear_resetPwdFrag0_username = (EdittextWithClear) view.findViewById(R.id.editTextWithClear_resetPwdFrag0_username);
        edittextWithClear_resetPwdFrag0_imgCode = (EdittextWithClear) view.findViewById(R.id.edittextWithClear_resetPwdFrag0_imgCode);
        imageView_resetPwdFrag0_imgCode = (ImageView) view.findViewById(R.id.imageView_resetPwdFrag0_imgCode);
        linearLayout_resetPwdFrag0_prompt = (LinearLayout) view.findViewById(R.id.linearLayout_resetPwdFrag0_prompt);
        textView_resetPwdFrag0_prompt = (TextView) view.findViewById(R.id.textView_resetPwdFrag0_prompt);
        button_resetPwdFrag0_goto = (Button) view.findViewById(R.id.button_resetPwdFrag0_goto);
        imageView_resetPwdFrag0_imgCode.setImageBitmap(NativeCode.getInstance().createBitmap());
        LogUtils.d(TAG, "native_code:" + NativeCode.getInstance().getCode());
    }


    private void initEvent() {
        button_resetPwdFrag0_goto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_resetPwdFrag0_goto:
                ToastUtils.showShort(ResetPwsFirstFragment.this.getActivity(), "button_resetPwdFrag0_goto");
                String phone = editTextWithClear_resetPwdFrag0_username.getText().toString().trim();
                String code = edittextWithClear_resetPwdFrag0_imgCode.getText().toString().trim();
                LogUtils.d(TAG, "input_code:" + edittextWithClear_resetPwdFrag0_imgCode.getText().toString().trim());
                LogUtils.d(TAG, "native_code:" + NativeCode.getInstance().getCode());
                if (!UserInfo.checkUsername(phone)) {
                    textView_resetPwdFrag0_prompt.setText("账号格式错误");
                    linearLayout_resetPwdFrag0_prompt.setVisibility(View.VISIBLE);
                } else if (!code.equals(NativeCode.getInstance().getCode())) {
                    textView_resetPwdFrag0_prompt.setText("验证码错误");
                    linearLayout_resetPwdFrag0_prompt.setVisibility(View.VISIBLE);
                    imageView_resetPwdFrag0_imgCode.setImageBitmap(NativeCode.getInstance().createBitmap());
                } else {
                    textView_resetPwdFrag0_prompt.setText("");
                    linearLayout_resetPwdFrag0_prompt.setVisibility(View.GONE);
                    imageView_resetPwdFrag0_imgCode.setImageBitmap(NativeCode.getInstance().createBitmap());
                    ((ResetPwdActivity) ResetPwsFirstFragment.this.getActivity()).setPhone(phone);
                    ((ResetPwdActivity) ResetPwsFirstFragment.this.getActivity()).setSelect(1);
                }
                break;
        }
    }
}

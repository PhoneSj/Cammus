package com.cammuse.intelligence.personal.register;

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
import com.cammuse.intelligence.utils.ButtonUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.ToastUtils;

/**
 * Created by Administrator on 2016/2/22.
 */
public class RegisterFirstFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RegisterFirstFragment";
    private EdittextWithClear editTextWithClear_registerFrag0_username;
    private EdittextWithClear edittextWithClear_registerFrag0_imgCode;
    private ImageView imageView_registerFrag0_imgCode;
    private LinearLayout linearLayout_registerFrag0_prompt;
    private TextView textView_registerFrag0_prompt;
    private Button button_registerFrag0_goto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register0, container, false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        editTextWithClear_registerFrag0_username = (EdittextWithClear) view.findViewById(R.id.editTextWithClear_registerFrag0_username);
        edittextWithClear_registerFrag0_imgCode = (EdittextWithClear) view.findViewById(R.id.edittextWithClear_registerFrag0_imgCode);
        imageView_registerFrag0_imgCode = (ImageView) view.findViewById(R.id.imageView_registerFrag0_imgCode);
        linearLayout_registerFrag0_prompt = (LinearLayout) view.findViewById(R.id.linearLayout_registerFrag0_prompt);
        textView_registerFrag0_prompt = (TextView) view.findViewById(R.id.textView_registerFrag0_prompt);
        button_registerFrag0_goto = (Button) view.findViewById(R.id.button_registerFrag0_goto);
        imageView_registerFrag0_imgCode.setImageBitmap(NativeCode.getInstance().createBitmap());
        LogUtils.d(TAG, "native_code:" + NativeCode.getInstance().getCode());
    }

    private void initEvent() {
        button_registerFrag0_goto.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (ButtonUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.button_registerFrag0_goto:
                ToastUtils.showShort(RegisterFirstFragment.this.getActivity(), "button_resetPwdFrag0_goto");
                String phone = editTextWithClear_registerFrag0_username.getText().toString().trim();
                String code = edittextWithClear_registerFrag0_imgCode.getText().toString().trim();
                LogUtils.d(TAG, "input_code:" + edittextWithClear_registerFrag0_imgCode.getText().toString().trim());
                LogUtils.d(TAG, "native_code:" + NativeCode.getInstance().getCode());
                if (!UserInfo.checkUsername(phone)) {
                    textView_registerFrag0_prompt.setText("账号格式错误");
                    linearLayout_registerFrag0_prompt.setVisibility(View.VISIBLE);
                } else if (!code.equals(NativeCode.getInstance().getCode())) {
                    textView_registerFrag0_prompt.setText("验证码错误");
                    linearLayout_registerFrag0_prompt.setVisibility(View.VISIBLE);
                    imageView_registerFrag0_imgCode.setImageBitmap(NativeCode.getInstance().createBitmap());
                } else {
                    textView_registerFrag0_prompt.setText("");
                    linearLayout_registerFrag0_prompt.setVisibility(View.GONE);
                    imageView_registerFrag0_imgCode.setImageBitmap(NativeCode.getInstance().createBitmap());
                    ((RegisterActivity) RegisterFirstFragment.this.getActivity()).setPhone(phone);
                    ((RegisterActivity) RegisterFirstFragment.this.getActivity()).setSelect(1);
                }
                break;
            case R.id.imageView_registerFrag0_imgCode:
                imageView_registerFrag0_imgCode.setImageBitmap(NativeCode.getInstance().createBitmap());
                break;
        }
    }


}

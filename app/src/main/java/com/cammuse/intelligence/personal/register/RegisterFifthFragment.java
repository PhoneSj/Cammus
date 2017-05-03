package com.cammuse.intelligence.personal.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.utils.ToastUtils;

/**
 * Created by Administrator on 2016/2/23.
 */
public class RegisterFifthFragment extends Fragment implements View.OnClickListener {

    private Button button_registerPwdFrag4_login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register4, container, false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        button_registerPwdFrag4_login = (Button) view.findViewById(R.id.button_registerPwdFrag4_login);
    }

    private void initEvent() {
        button_registerPwdFrag4_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_registerPwdFrag4_login:
                //TODO
                ToastUtils.showShort(RegisterFifthFragment.this.getActivity(), "button_registerFrag2_yes");
                RegisterFifthFragment.this.getActivity().finish();
                break;
        }
    }
}

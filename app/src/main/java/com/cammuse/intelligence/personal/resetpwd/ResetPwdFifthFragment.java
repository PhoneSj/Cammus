package com.cammuse.intelligence.personal.resetpwd;

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
public class ResetPwdFifthFragment extends Fragment implements View.OnClickListener {

    private Button button_resetPwdFrag4_backToMain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resetpwd4, container, false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        button_resetPwdFrag4_backToMain = (Button) view.findViewById(R.id.button_resetPwdFrag4_backToMain);

    }

    private void initEvent() {
        button_resetPwdFrag4_backToMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_resetPwdFrag4_backToMain:
                //TODO
                ToastUtils.showShort(ResetPwdFifthFragment.this.getActivity(), "button_resetPwdFrag4_backToMain");
                ((ResetPwdActivity) ResetPwdFifthFragment.this.getActivity()).setSelect(0);
                break;
        }
    }
}

package com.cammuse.intelligence.personal.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.utils.ToastUtils;

/**
 * Created by Administrator on 2016/2/23.
 */
public class RegisterThirdFragment extends Fragment implements View.OnClickListener {

    private ImageView imageView_registerFrag2_icon;
    private TextView textView_registerFrag2_nickname;
    private Button button_registerFrag2_login;
    private Button button_registerFrag2_backToMain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register2, container, false);
        initView(view);
        initEvent();
        return view;
    }

    private void initView(View view) {
        imageView_registerFrag2_icon = (ImageView) view.findViewById(R.id.imageView_registerFrag2_icon);
        textView_registerFrag2_nickname = (TextView) view.findViewById(R.id.textView_registerFrag2_nickname);
        button_registerFrag2_login = (Button) view.findViewById(R.id.button_registerFrag2_login);
        button_registerFrag2_backToMain = (Button) view.findViewById(R.id.button_registerFrag2_backToMain);
    }

    private void initEvent() {
        button_registerFrag2_login.setOnClickListener(this);
        button_registerFrag2_backToMain.setOnClickListener(this);
        imageView_registerFrag2_icon.setImageResource(R.mipmap.smile);
        textView_registerFrag2_nickname.setText(((RegisterActivity) RegisterThirdFragment.this.getActivity()).getPhone());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_registerFrag2_login:
                ToastUtils.showShort(RegisterThirdFragment.this.getActivity(), "button_registerFrag2_login");
                RegisterThirdFragment.this.getActivity().finish();
                break;
            case R.id.button_registerFrag2_backToMain:
                ToastUtils.showShort(RegisterThirdFragment.this.getActivity(), "button_registerFrag2_backToMain");
                ((RegisterActivity) RegisterThirdFragment.this.getActivity()).setSelect(0);
                break;
        }
    }
}

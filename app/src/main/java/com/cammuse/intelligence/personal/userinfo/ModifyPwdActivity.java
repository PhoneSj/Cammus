package com.cammuse.intelligence.personal.userinfo;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.cammuse.intelligence.BaseActivity;
import com.cammuse.intelligence.R;
import com.cammuse.intelligence.common.EdittextWithClear;
import com.cammuse.intelligence.common.EdittextWithPwd;
import com.cammuse.intelligence.utils.ToastUtils;

/**
 * Created by Administrator on 2016/2/23.
 */
public class ModifyPwdActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageView_modifyPwdAct_back;
    private EdittextWithClear editTextWithClear_modifyPwdAct_currentPwd;
    private EdittextWithPwd editTextWithPwd_modifyPwdAct_newPwd;
    private Button button_modifyPwdAct_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modifypwd);
        initView();
        initEvent();
    }

    private void initView() {
        imageView_modifyPwdAct_back = (ImageView) findViewById(R.id.imageView_modifyPwdAct_back);
        editTextWithClear_modifyPwdAct_currentPwd = (EdittextWithClear) findViewById(R.id.editTextWithClear_modifyPwdAct_currentPwd);
        editTextWithPwd_modifyPwdAct_newPwd = (EdittextWithPwd) findViewById(R.id.editTextWithPwd_modifyPwdAct_newPwd);
        button_modifyPwdAct_ok = (Button) findViewById(R.id.button_modifyPwdAct_ok);
    }

    private void initEvent() {
        imageView_modifyPwdAct_back.setOnClickListener(this);
        button_modifyPwdAct_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_modifyPwdAct_back:
                finish();
                ToastUtils.showShort(ModifyPwdActivity.this, "imageView_modifyPwdAct_back");
                break;
            case R.id.button_modifyPwdAct_ok:
                //TODO
                ToastUtils.showShort(ModifyPwdActivity.this, "button_modifyPwdAct_ok");
                break;
        }
    }
}

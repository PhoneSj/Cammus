package com.cammuse.intelligence.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cammuse.intelligence.BaseActivity;
import com.cammuse.intelligence.R;
import com.cammuse.intelligence.personal.setting.AboutActivity;
import com.cammuse.intelligence.personal.setting.LockActivity;
import com.cammuse.intelligence.utils.ToastUtils;
import com.umeng.update.UmengUpdateAgent;

/**
 * Created by Administrator on 2016/2/15.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {


    private ImageView imageView_settingAct_back;
    private RelativeLayout relativeLayout_settingAct_gesturePwd;
    private RelativeLayout relativeLayout_settingAct_checkUpdate;
    private RelativeLayout relativeLayout_setting_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        initView();
        initEvent();
    }

    private void initView() {
        imageView_settingAct_back = (ImageView) findViewById(R.id.imageView_settingAct_back);
        relativeLayout_settingAct_gesturePwd = (RelativeLayout) findViewById(R.id.relativeLayout_settingAct_gesturePwd);
        relativeLayout_settingAct_checkUpdate = (RelativeLayout) findViewById(R.id.relativeLayout_settingAct_checkUpdate);
        relativeLayout_setting_about = (RelativeLayout) findViewById(R.id.relativeLayout_setting_about);
    }

    private void initEvent() {
        imageView_settingAct_back.setOnClickListener(this);
        relativeLayout_settingAct_gesturePwd.setOnClickListener(this);
        relativeLayout_settingAct_checkUpdate.setOnClickListener(this);
        relativeLayout_setting_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_settingAct_back:
                SettingActivity.this.finish();
                break;
            case R.id.relativeLayout_settingAct_gesturePwd:
                //TODO
                ToastUtils.showShort(SettingActivity.this, "relativeLayout_settingAct_gesturePwd");
                Intent lockIntent = new Intent(SettingActivity.this, LockActivity.class);
                startActivity(lockIntent);
                break;
            case R.id.relativeLayout_settingAct_checkUpdate:
                ToastUtils.showShort(SettingActivity.this, "relativeLayout_settingAct_checkUpdate");
                UmengUpdateAgent.forceUpdate(SettingActivity.this);
                break;
            case R.id.relativeLayout_setting_about:
                ToastUtils.showShort(SettingActivity.this, "relativeLayout_setting_about");
                Intent aboutIntnet = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(aboutIntnet);
                break;
        }
    }
}

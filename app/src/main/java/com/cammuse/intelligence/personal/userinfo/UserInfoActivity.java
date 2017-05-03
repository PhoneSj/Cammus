package com.cammuse.intelligence.personal.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.cammuse.intelligence.BaseActivity;
import com.cammuse.intelligence.R;
import com.cammuse.intelligence.entity.UserInfo;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.ToastUtils;

/**
 * Created by Administrator on 2016/2/15.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imageView_userinfoAct_back;
    private RelativeLayout relativeLayout_userinfoAct_icon;
    private RelativeLayout relativeLayout_userinfoAct_nickname;
    private RelativeLayout relativeLayout_userinfoAct_gender;
    private RelativeLayout relativeLayout_userinfoAct_phone;
    private RelativeLayout relativeLayout_userinfoAct_carType;
    private RelativeLayout relativeLayout_userinfoAct_modifyPwd;
    private Button button_userinfoAct_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_userinfo);
        initView();
        initEvent();
    }

    private void initView() {
        imageView_userinfoAct_back = (ImageView) findViewById(R.id.imageView_userinfoAct_back);
        relativeLayout_userinfoAct_icon = (RelativeLayout) findViewById(R.id.relativeLayout_userinfoAct_icon);
        relativeLayout_userinfoAct_nickname = (RelativeLayout) findViewById(R.id.relativeLayout_userinfoAct_nickname);
        relativeLayout_userinfoAct_gender = (RelativeLayout) findViewById(R.id.relativeLayout_userinfoAct_gender);
        relativeLayout_userinfoAct_phone = (RelativeLayout) findViewById(R.id.relativeLayout_userinfoAct_phone);
        relativeLayout_userinfoAct_carType = (RelativeLayout) findViewById(R.id.relativeLayout_userinfoAct_carType);
        relativeLayout_userinfoAct_modifyPwd = (RelativeLayout) findViewById(R.id.relativeLayout_userinfoAct_modifyPwd);
        button_userinfoAct_logout = (Button) findViewById(R.id.button_userinfoAct_logout);
    }

    private void initEvent() {
        imageView_userinfoAct_back.setOnClickListener(this);
        relativeLayout_userinfoAct_icon.setOnClickListener(this);
        relativeLayout_userinfoAct_nickname.setOnClickListener(this);
        relativeLayout_userinfoAct_gender.setOnClickListener(this);
        relativeLayout_userinfoAct_carType.setOnClickListener(this);
        relativeLayout_userinfoAct_modifyPwd.setOnClickListener(this);
        button_userinfoAct_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_userinfoAct_back:
                finish();
                break;
            case R.id.relativeLayout_userinfoAct_icon:
                //TODO
                ToastUtils.showShort(UserInfoActivity.this, "relativeLayout_userinfoAct_icon");
                showIconPopuWindow();
                break;
            case R.id.relativeLayout_userinfoAct_nickname:
                //TODO
                ToastUtils.showShort(UserInfoActivity.this, "relativeLayout_userinfoAct_nickname");
                showNicknamePopuWindow();
                break;
            case R.id.relativeLayout_userinfoAct_gender:
                //TODO
                ToastUtils.showShort(UserInfoActivity.this, "relativeLayout_userinfoAct_gender");
                showGenderPopuWindow();
                break;
            case R.id.relativeLayout_userinfoAct_carType:
                //TODO
                ToastUtils.showShort(UserInfoActivity.this, "relativeLayout_userinfoAct_carType");
                showCarTypePopuWindow();
                break;
            case R.id.relativeLayout_userinfoAct_modifyPwd:
                //TODO
                ToastUtils.showShort(UserInfoActivity.this, "relativeLayout_userinfoAct_modifyPwd");
                Intent modifyPwdIntent = new Intent(UserInfoActivity.this, ModifyPwdActivity.class);
                startActivity(modifyPwdIntent);
                break;
            case R.id.button_userinfoAct_logout:
                ToastUtils.showShort(UserInfoActivity.this, "button_userinfoAct_logout");
                UserInfo.saveUserInfo2Sp(UserInfoActivity.this, new UserInfo(null, null));
                UserInfo.setLoginState(false);
                Intent intent = getIntent();
                setResult(ConstantUtils.RESULT_CODE_USERINFO, intent);
                finish();
                break;
        }
    }

    private void showCarTypePopuWindow() {

    }

    private void showGenderPopuWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_userinfo_gender, null);
        PopupWindow mPopupWindow = new GenderPopupWindow(this, view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.showAtLocation(findViewById(R.id.linearLayout_userinfoAct_root), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
    }

    private void showNicknamePopuWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_userinfo_nickname, null);
        PopupWindow mPopupWindow = new NicknamePopupWindow(this, view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.showAtLocation(findViewById(R.id.linearLayout_userinfoAct_root), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
    }

    private void showIconPopuWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_userinfo_icon, null);
        PopupWindow mPopupWindow = new IconPopupWindow(this, view, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.showAtLocation(findViewById(R.id.linearLayout_userinfoAct_root), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
    }

    /**
     * 设置窗口背景变暗
     *
     * @param alpha
     */
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }


}

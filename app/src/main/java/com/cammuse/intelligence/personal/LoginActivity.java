package com.cammuse.intelligence.personal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cammuse.intelligence.BaseActivity;
import com.cammuse.intelligence.R;
import com.cammuse.intelligence.common.EdittextWithClear;
import com.cammuse.intelligence.common.EdittextWithPwd;
import com.cammuse.intelligence.entity.UserInfo;
import com.cammuse.intelligence.net.VolleyUtils;
import com.cammuse.intelligence.personal.register.RegisterActivity;
import com.cammuse.intelligence.personal.resetpwd.ResetPwdActivity;
import com.cammuse.intelligence.popup.LoadingPopupWindow;
import com.cammuse.intelligence.utils.ButtonUtils;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.DialogUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends BaseActivity implements OnClickListener {

    // Debug
    private static final String TAG = "LoginActivity";
    private LinearLayout linearLayout_loginAct_root;
    private ImageView imageView_loginAct_back;
    private EdittextWithClear editText_loginAct_username;
    private EdittextWithPwd editTextWithPwd_loginAct_password;
    private TextView textView_loginAct_register;
    private TextView textView_loginAct_forgetPwd;
    private Button button_loginAct_goto;

    private LoadingPopupWindow mLoadingPopupWindow;

    private UserInfo userInfo;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DialogUtils.cancelLoadingdialog();
            JSONObject result = null;
            switch (msg.what) {
                case ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_SUBMIT:
                    LogUtils.i(TAG, "网络正常");
                    mLoadingPopupWindow.dismiss();
                    try {
                        result = (JSONObject) msg.obj;
                        parseJSON(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_CHECK_USERNAME:// 检查用户名是否存在
                    result = (JSONObject) msg.obj;
                    String status = result.optString("status");
                    if (status.equals("该用户未注册")) {
                        editText_loginAct_username.setError("该用户不存在");
                    }
                    break;
                case ConstantUtils.HANDLER_WHAT_REQUEST_FAILURE:
                    LogUtils.e(TAG, "网络出现异常");
                    mLoadingPopupWindow.dismiss();
                    ToastUtils.showShort(LoginActivity.this, "网络出现异常");
                    break;
            }

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
        initEvent();

    }

    private void initView() {
        linearLayout_loginAct_root = (LinearLayout) findViewById(R.id.linearLayout_loginAct_root);
        imageView_loginAct_back = (ImageView) findViewById(R.id.imageView_loginAct_back);
        editText_loginAct_username = (EdittextWithClear) findViewById(R.id.editTextWithClear_loginAct_username);
        editTextWithPwd_loginAct_password = (EdittextWithPwd) findViewById(R.id.editTextWithPwd_loginAct_password);
        textView_loginAct_register = (TextView) findViewById(R.id.textView_loginAct_register);
        textView_loginAct_forgetPwd = (TextView) findViewById(R.id.textView_loginAct_forgetPwd);
        button_loginAct_goto = (Button) findViewById(R.id.button_loginAct_goto);
    }

    private void initEvent() {
        imageView_loginAct_back.setOnClickListener(this);
        textView_loginAct_register.setOnClickListener(this);
        textView_loginAct_forgetPwd.setOnClickListener(this);
        button_loginAct_goto.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (ButtonUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.imageView_loginAct_back:
                finish();
                break;
            case R.id.textView_loginAct_register:
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.textView_loginAct_forgetPwd:
                Intent resetPwdIntent = new Intent(LoginActivity.this, ResetPwdActivity.class);
                startActivity(resetPwdIntent);
                break;
            case R.id.button_loginAct_goto:
                String username = editText_loginAct_username.getText()
                        .toString().trim();
                String password = editTextWithPwd_loginAct_password.getText()
                        .toString().trim();
                if (UserInfo.checkUsername(editText_loginAct_username.getText().toString().trim()) && UserInfo.checkPassword(editTextWithPwd_loginAct_password.getText().toString().trim())) {
                    String url = ConstantUtils.URL_LOGIN + "?phone=" + username
                            + "&password=" + password;
                    LogUtils.i(TAG, "url:" + url);
                    showLoadingView();
                    VolleyUtils.submitUsernameAndPassword(LoginActivity.this, url,
                            mHandler);// 结果通过Handler返回
                    userInfo = new UserInfo(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, "用户信息格式错误", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "用户信息格式错误");
                }

                LogUtils.d(TAG, String.valueOf(UserInfo.checkUsername(editText_loginAct_username.getText().toString().trim())));
                LogUtils.d(TAG, String.valueOf(UserInfo.checkPassword(editTextWithPwd_loginAct_password.getText().toString().trim())));

                break;
        }
    }

    /**
     * 加载等待界面
     */
    private void showLoadingView() {
        mLoadingPopupWindow = new LoadingPopupWindow(this, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mLoadingPopupWindow.showAtLocation(linearLayout_loginAct_root, Gravity.CENTER, 0, 0);

    }

    /**
     * 将JSON字符串转化为用户信息对象,并判断是否登录成功
     */
    protected void parseJSON(JSONObject object) throws JSONException {
        String state = object.optString("status");
        if (state.equals("error")) {
            Toast.makeText(LoginActivity.this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();
            UserInfo.setLoginState(false);
        } else {
            ToastUtils.showShort(LoginActivity.this, "登录成功");
            Log.i(TAG, "登录成功");
            JSONObject userInfo_Object = object.optJSONObject("userInfo");
            UserInfo.saveUserInfo2Sp(this, userInfo);
            UserInfo.setLoginState(true);
            Intent intent = getIntent();
            setResult(ConstantUtils.RESULT_CODE_LOGIN, intent);
            finish();
        }

    }

}

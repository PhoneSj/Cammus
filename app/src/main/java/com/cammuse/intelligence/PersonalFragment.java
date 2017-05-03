package com.cammuse.intelligence;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cammuse.intelligence.entity.UserInfo;
import com.cammuse.intelligence.net.VolleyUtils;
import com.cammuse.intelligence.personal.LoginActivity;
import com.cammuse.intelligence.personal.SettingActivity;
import com.cammuse.intelligence.personal.userinfo.UserInfoActivity;
import com.cammuse.intelligence.popup.LoadingPopupWindow;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.DialogUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.SPUtils;
import com.cammuse.intelligence.utils.ToastUtils;

import org.json.JSONObject;


public class PersonalFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PersonalFragment";
    private LinearLayout linearLayout_personalFrag_root;
    private ImageView imageView_personalFrag_userImg;
    private TextView textView_personalFrag_userName;
    private LinearLayout linearLayout_personalFrag_user;
    private LinearLayout linearLayout_personalFrag_score;
    private LinearLayout linearLayout_personalFrag_evaluate;
    private RelativeLayout relativeLayout_personalFrag_forum;
    private RelativeLayout relativeLayout_personalFrag_feedback;
    private RelativeLayout relativeLayout_personalFrag_setting;

    private LoadingPopupWindow mLoadingPopupWindow;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DialogUtils.cancelLoadingdialog();
            JSONObject result = null;
            switch (msg.what) {
                case ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_SUBMIT:
                    LogUtils.i(TAG, "网络正常");
                    mLoadingPopupWindow.dismiss();
                    result = (JSONObject) msg.obj;
                    parseJSON(result);
                    break;
                case ConstantUtils.HANDLER_WHAT_REQUEST_FAILURE:
                    LogUtils.e(TAG, "网络出现异常");
                    mLoadingPopupWindow.dismiss();
                    ToastUtils.showShort(getActivity(), "网络出现异常");
                    UserInfo.setLoginState(false);
                    break;
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_tab_personal, container,
                false);
        initView(view);
        initEvent();
        autoLogin();
        return view;
    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        String username = SPUtils.getString(getContext(), ConstantUtils.SP_KEY_USERNAME, null);
        String password = SPUtils.getString(getContext(), ConstantUtils.SP_KEY_PASSWORD, null);
        LogUtils.d(TAG, "username:" + username + " password:" + password);
        if (UserInfo.checkUsername(username) && UserInfo.checkPassword(password)) {
            String url = ConstantUtils.URL_LOGIN + "?phone=" + username
                    + "&password=" + password;
            LogUtils.i(TAG, "url:" + url);
            showLoadingView();
            VolleyUtils.submitUsernameAndPassword(getActivity(), url, mHandler);// 结果通过Handler返回
        } else {
            LogUtils.i(TAG, "用户信息不符合规则");
        }
        LogUtils.i(TAG, "auto_login....");
    }

    /**
     * 加载等待界面
     */
    private void showLoadingView() {
        mLoadingPopupWindow = new LoadingPopupWindow(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mLoadingPopupWindow.showAtLocation(linearLayout_personalFrag_root, Gravity.CENTER, 0, 0);

    }


    private void initView(View view) {
        linearLayout_personalFrag_root = (LinearLayout) view.findViewById(R.id.linearLayout_personalFrag_root);
        imageView_personalFrag_userImg = (ImageView) view.findViewById(R.id.imageView_personalFrag_userImg);
        textView_personalFrag_userName = (TextView) view.findViewById(R.id.textView_personalFrag_userName);
        linearLayout_personalFrag_user = (LinearLayout) view.findViewById(R.id.linearLayout_personalFrag_user);
        linearLayout_personalFrag_score = (LinearLayout) view.findViewById(R.id.linearLayout_personalFrag_score);
        linearLayout_personalFrag_evaluate = (LinearLayout) view.findViewById(R.id.linearLayout_personalFrag_evaluate);
        relativeLayout_personalFrag_forum = (RelativeLayout) view.findViewById(R.id.relativeLayout_personalFrag_forum);
        relativeLayout_personalFrag_feedback = (RelativeLayout) view.findViewById(R.id.relativeLayout_personalFrag_feedback);
        relativeLayout_personalFrag_setting = (RelativeLayout) view.findViewById(R.id.relativeLayout_personalFrag_setting);

    }

    private void initEvent() {
        linearLayout_personalFrag_user.setOnClickListener(this);
        linearLayout_personalFrag_score.setOnClickListener(this);
        linearLayout_personalFrag_evaluate.setOnClickListener(this);
        relativeLayout_personalFrag_forum.setOnClickListener(this);
        relativeLayout_personalFrag_feedback.setOnClickListener(this);
        relativeLayout_personalFrag_setting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.linearLayout_personalFrag_user:
                // 判断用户是否已经登陆
                if (!UserInfo.getLoginState()) {
                    // 1.未登陆状态，跳转到登陆界面
                    LogUtils.d(TAG, "未登陆");
                    intent = new Intent(PersonalFragment.this.getActivity(), LoginActivity.class);
                    startActivityForResult(intent, ConstantUtils.REQUEST_CODE_LOGIN);
                } else {
                    // 2.已登陆状态，进入用户信息界面
                    LogUtils.d(TAG, "已登陆");
                    intent = new Intent(PersonalFragment.this.getActivity(), UserInfoActivity.class);
                    startActivityForResult(intent, ConstantUtils.REQUEST_CODE_USERINFO);
                }
                break;
            case R.id.linearLayout_personalFrag_score:
                //TODO
                Toast.makeText(getActivity(), "我的积分", Toast.LENGTH_SHORT).show();
                break;
            case R.id.linearLayout_personalFrag_evaluate:
                //TODO
                Toast.makeText(getActivity(), "驾驶评价", Toast.LENGTH_SHORT).show();
                break;
            case R.id.relativeLayout_personalFrag_forum:
                //TODO
                Toast.makeText(getActivity(), "论坛", Toast.LENGTH_SHORT).show();
                break;
            case R.id.relativeLayout_personalFrag_feedback:
                //TODO
                Toast.makeText(getActivity(), "反馈", Toast.LENGTH_SHORT).show();
                break;
            case R.id.relativeLayout_personalFrag_setting:
                //TODO
                Toast.makeText(getActivity(), "设置", Toast.LENGTH_SHORT).show();
                intent = new Intent(PersonalFragment.this.getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case ConstantUtils.RESULT_CODE_LOGIN:
                updateUi();
                break;
            case ConstantUtils.RESULT_CODE_USERINFO:
                updateUi();
                break;
        }
    }

    /**
     * 更新界面数据
     */
    private void updateUi() {
        //TODO
//        imageView_personalFrag_userImg.setImageResource(resId);
        textView_personalFrag_userName.setText(SPUtils.getString(getActivity(), ConstantUtils.SP_KEY_USERNAME, "未登录"));
    }

    /**
     * 将JSON字符串转化为用户信息对象,并判断是否登录成功
     */
    protected void parseJSON(JSONObject object) {
        String state = object.optString("status");
        if (state.equals("error")) {
            Toast.makeText(getActivity(), "用户名或者密码错误", Toast.LENGTH_SHORT).show();
            UserInfo.setLoginState(false);
        } else {
            ToastUtils.showShort(getActivity(), "登录成功");
            Log.i(TAG, "登录成功");
            UserInfo.setLoginState(true);
            updateUi();
        }

    }

}

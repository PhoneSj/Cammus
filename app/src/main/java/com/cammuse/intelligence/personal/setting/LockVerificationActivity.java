package com.cammuse.intelligence.personal.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.gesture.lock.GestureLockViewGroup;
import com.cammuse.intelligence.gesture.lock.GestureLockViewGroup.OnGestureLockViewListener;
import com.cammuse.intelligence.personal.setting.CheckUserInfoPopupWindow.OnCheckListener;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class LockVerificationActivity extends Activity implements
        OnClickListener {

    private static final String TAG = "LockVerificationActivity";
    private LinearLayout linearLayout_lockverificationAct_root;
    private GestureLockViewGroup gestureLockViewGroup_lockverificationAct_lock;
    private TextView textView_lockverificatinAct_forgetLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lockverification);

        initView();
        initEvent();
        initSetting();

    }

    private void initSetting() {
        gestureLockViewGroup_lockverificationAct_lock
                .setAnswer(getAnswerFromSp());
    }

    private void initView() {
        linearLayout_lockverificationAct_root = (LinearLayout) findViewById(R.id.linearLayout_lockverificationAct_root);
        gestureLockViewGroup_lockverificationAct_lock = (GestureLockViewGroup) findViewById(R.id.gestureLockViewGroup_lockverificationAct_lock);
        textView_lockverificatinAct_forgetLock = (TextView) findViewById(R.id.textView_lockverificatinAct_forgetLock);
    }

    private void initEvent() {
        textView_lockverificatinAct_forgetLock.setOnClickListener(this);
        gestureLockViewGroup_lockverificationAct_lock
                .setOnGestureLockViewListener(new OnGestureLockViewListener() {

                    @Override
                    public void onUnmatchedExceedBoundary() {
                        Toast.makeText(LockVerificationActivity.this,
                                "错误5次...", Toast.LENGTH_SHORT).show();
                        gestureLockViewGroup_lockverificationAct_lock
                                .setUnMatchExceedBoundary(5);
                    }

                    @Override
                    public void onGestureEvent(boolean matched) {

                        if (matched) {
                            ConstantUtils.lock_state = true;
                            finish();
                        } else {
                            Toast.makeText(LockVerificationActivity.this,
                                    "图形密码错误", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onBlockSelected(int cId) {
                    }
                });
    }

    /**
     * 从sp中读入手势密码
     *
     * @return
     */
    private int[] getAnswerFromSp() {
        String data = SPUtils.getString(this, ConstantUtils.SP_KEY_LOCK_CHOOSE,
                "-1");
        LogUtils.e(TAG, "从sp中获取的手势密码:" + data);
        String str[] = data.split(",");
        List<Integer> choose = new ArrayList<Integer>();
        for (int i = 0; i < str.length; i++) {
            choose.add(Integer.parseInt(str[i]));
        }
        int chooseArray[] = new int[choose.size()];
        for (int i = 0; i < choose.size(); i++) {
            chooseArray[i] = choose.get(i);
        }
        return chooseArray;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_lockverificatinAct_forgetLock:
                showPopupWindow();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        // 禁止跳过验证
    }

    private void showPopupWindow() {
        LogUtils.d(TAG, "--showPopupWindow--");
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_checkuserinfo, null);
        CheckUserInfoPopupWindow mPopupWindow = new CheckUserInfoPopupWindow(this, view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOnCheckListener(new OnCheckListener() {

            @Override
            public void onCheck(boolean isAllow) {
                if (isAllow) {
                    LogUtils.d(TAG, "关闭验证窗口");
                    resetLock();
                    LockVerificationActivity.this.finish();
                }
            }
        });
        mPopupWindow.setAnimationStyle(R.style.anim_popup_dir);

        // 设置可以获取焦点，否则弹出菜单中的EditText是无法获取输入的
        mPopupWindow.setFocusable(true);
        // 防止虚拟软键盘被弹出菜单遮住
        mPopupWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 在底部显示
        mPopupWindow.showAtLocation(linearLayout_lockverificationAct_root,
                Gravity.BOTTOM, 0, 0);
    }

    /**
     * 重置手势密码
     */
    protected void resetLock() {
        SPUtils.put(this, ConstantUtils.SP_KEY_ENABLE_LOCK, false);
        SPUtils.put(this, ConstantUtils.SP_KEY_LOCK_CHOOSE, "-1");
    }

}

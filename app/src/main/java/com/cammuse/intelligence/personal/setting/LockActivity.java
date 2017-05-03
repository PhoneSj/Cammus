package com.cammuse.intelligence.personal.setting;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ToggleButton;

import com.cammuse.intelligence.BaseActivity;
import com.cammuse.intelligence.R;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.DimensUtils;
import com.cammuse.intelligence.utils.SPUtils;

/**
 * Created by Administrator on 2016/2/29.
 */
public class LockActivity extends BaseActivity implements View.OnClickListener {

    private static final java.lang.String TAG = "LockActivity";
    private LinearLayout linearLayout_lockAct_root;
    private ImageView imageView_lockAct_back;
    private ToggleButton toggleButton_lockAct_enableLock;
    private Button button_lockAct_modifyLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_lock);
        initView();
        initEvent();
    }

    private void initView() {
        linearLayout_lockAct_root = (LinearLayout) findViewById(R.id.linearLayout_lockAct_root);
        imageView_lockAct_back = (ImageView) findViewById(R.id.imageView_lockAct_back);
        toggleButton_lockAct_enableLock = (ToggleButton) findViewById(R.id.toggleButton_lockAct_enableLock);
        button_lockAct_modifyLock = (Button) findViewById(R.id.button_lockAct_modifyLock);
        toggleButton_lockAct_enableLock.setChecked(SPUtils.getBoolean(this, ConstantUtils.SP_KEY_ENABLE_LOCK, false));
    }

    private void initEvent() {
        toggleButton_lockAct_enableLock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPUtils.put(LockActivity.this, ConstantUtils.SP_KEY_ENABLE_LOCK, isChecked);
            }
        });
        imageView_lockAct_back.setOnClickListener(this);
        button_lockAct_modifyLock.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_lockAct_back:
                finish();
                break;
            case R.id.button_lockAct_modifyLock:
                showLockPopupWindow();
                break;
        }
    }

    private void showLockPopupWindow() {
        // 获取状态栏高度:frame.top
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_lock, null);
        PopupWindow mPopupWindow = new LockPopupWindow(this, view, LinearLayout.LayoutParams.MATCH_PARENT,
                DimensUtils.getScreenHeight(this) - frame.top);
        mPopupWindow.showAtLocation(findViewById(R.id.linearLayout_lockAct_root), Gravity.RIGHT | Gravity.BOTTOM, 0, 0);


    }


}

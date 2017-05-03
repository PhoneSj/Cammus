package com.cammuse.intelligence;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.cammuse.intelligence.personal.setting.LockVerificationActivity;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.SPUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/2/29.
 */
public class BaseFragmentActivity extends FragmentActivity {
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        if (!isAppOnForeground()) {
            // app 进入后台
            LogUtils.d("Test", "进入后台");
            ConstantUtils.lock_state = false;
            // 全局变量isActive = false 记录当前已经进入后台
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!ConstantUtils.lock_state
                && SPUtils.getBoolean(this, ConstantUtils.SP_KEY_ENABLE_LOCK,
                false)) {
            Intent intent = new Intent(this, LockVerificationActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        // if (!isActive) {
        // app 从后台唤醒，进入前台
        LogUtils.d("Test", "进入前台");

        // isActive = true;
        // }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
}

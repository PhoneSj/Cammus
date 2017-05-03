package com.cammuse.intelligence.personal.setting;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.gesture.lock.GestureLockViewGroup;
import com.cammuse.intelligence.gesture.lock.GestureLockViewGroup.OnGestureLockViewListener;
import com.cammuse.intelligence.gesture.lock.LockPreView;
import com.cammuse.intelligence.popup.BasePopupWindow;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.SPUtils;
import com.cammuse.intelligence.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/29.
 */
public class LockPopupWindow extends BasePopupWindow implements OnClickListener {

    private static final String TAG = "LockPopupWindow";
    private ImageView imageView_lockPopup_back;
    private LockPreView lockPreView_lockPopup_preview;
    private TextView textView_lockPoput_prompt;
    private GestureLockViewGroup gestureLockViewGroup_lockPopup_setLock;


    private boolean isFirst = true;
    private List<Integer> mFirstChoose = new ArrayList<Integer>();
    private List<Integer> mSecondChoose = new ArrayList<Integer>();

    private OnLockListener mOnLockListener;

    public interface OnLockListener {
        public void lockChoose(List<Integer> mChoose);
    }

    public LockPopupWindow(Context context, View view, int width, int height) {
        super(context, view, width, height, R.style.anim_popup_dir);
    }

    @Override
    public void initView() {
        imageView_lockPopup_back = (ImageView) getRootView().findViewById(R.id.imageView_lockPopup_back);
        lockPreView_lockPopup_preview = (LockPreView) getRootView().findViewById(R.id.lockPreView_lockPopup_preview);
        textView_lockPoput_prompt = (TextView) getRootView().findViewById(R.id.textView_lockPoput_prompt);
        gestureLockViewGroup_lockPopup_setLock = (GestureLockViewGroup) getRootView().findViewById(R.id.gestureLockViewGroup_lockPopup_setLock);
    }

    @Override
    public void initEvent() {
        imageView_lockPopup_back.setOnClickListener(this);
        gestureLockViewGroup_lockPopup_setLock
                .setOnGestureLockViewListener(new OnGestureLockViewListener() {


                    @Override
                    public void onUnmatchedExceedBoundary() {
                    }

                    @Override
                    public void onGestureEvent(boolean matched) {
                        if (isFirst) {
                            LogUtils.d(TAG, "first:");
                            getChoose(mFirstChoose,
                                    gestureLockViewGroup_lockPopup_setLock.getChoose());
                            isFirst = false;
                            textView_lockPoput_prompt.setText("确认解锁图案");
                            lockPreView_lockPopup_preview.setChoose(mFirstChoose);
                        } else {
                            LogUtils.d(TAG, "second:");
                            getChoose(mSecondChoose,
                                    gestureLockViewGroup_lockPopup_setLock.getChoose());
                            if (checkLock(mFirstChoose, mSecondChoose)) {
                                ToastUtils.showShort(LockPopupWindow.this.getContext(), "设置密码成功");
                                saveLock(mFirstChoose);
                                LockPopupWindow.this.dismiss();
                            } else {
                                ToastUtils.showShort(LockPopupWindow.this.getContext(), "两次密码不一致");
                            }
                        }
                    }

                    @Override
                    public void onBlockSelected(int cId) {
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_lockPopup_back:
                dismiss();
                break;
        }
    }

    /**
     * 保存手势密码
     */
    private void saveLock(List<Integer> lock) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lock.size(); i++) {
            if (i == lock.size() - 1) {
                sb.append(lock.get(i));
            } else {
                sb.append(lock.get(i) + ",");
            }
        }
        SPUtils.putString(LockPopupWindow.this.getContext(), ConstantUtils.SP_KEY_LOCK_CHOOSE,
                sb.toString());
        LogUtils.d(TAG, sb.toString());
    }

    /**
     * 获取手势密码
     *
     * @param data
     * @param choose
     */
    protected void getChoose(List<Integer> data, List<Integer> choose) {
        data.clear();
        for (int i = 0; i < choose.size(); i++) {
            data.add(choose.get(i));
        }
    }

//	private void test(List<Integer> data) {
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < data.size(); i++) {
//			sb.append(data.get(i) + " ");
//		}
//		LogUtils.d(TAG, sb.toString());
//	}

    protected boolean checkLock(List<Integer> choose0, List<Integer> choose1) {
        if (choose0.size() > 0 && choose1.size() > 0) {
            if (choose0.size() == choose1.size()) {
                for (int i = 0; i < choose0.size(); i++) {
                    if (choose0.get(i) != choose1.get(i)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;

    }

    public void setOnLockListener(OnLockListener mOnLockListener) {
        this.mOnLockListener = mOnLockListener;
    }

}

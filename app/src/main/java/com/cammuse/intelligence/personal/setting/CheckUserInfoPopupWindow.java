package com.cammuse.intelligence.personal.setting;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.common.EdittextWithClear;
import com.cammuse.intelligence.net.VolleyUtils;
import com.cammuse.intelligence.popup.BasePopupWindow;
import com.cammuse.intelligence.utils.ConstantUtils;
import com.cammuse.intelligence.utils.KeyBoardUtils;
import com.cammuse.intelligence.utils.LogUtils;
import com.cammuse.intelligence.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckUserInfoPopupWindow extends BasePopupWindow implements
        View.OnClickListener {

    public static final java.lang.String TAG = "CheckUserInfoPopupWindow";

    private EdittextWithClear edittextWithClear_checkUserInfoPopup_username;
    private EdittextWithClear edittextWithClear_checkUserInfoPopup_password;
    private Button button_checkUserInfoPopup_cancel;
    private Button button_checkUserInfoPopup_ok;

    private OnCheckListener mOnCheckListener;

    private Handler mHandler = new Handler() {


        public void handleMessage(Message msg) {
            JSONObject result = null;
            switch (msg.what) {
                case ConstantUtils.HANDLER_WHAT_REQUEST_SUCCESS_SUBMIT:
                    try {
                        result = (JSONObject) msg.obj;
                        parseJSON(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case ConstantUtils.HANDLER_WHAT_REQUEST_FAILURE:
                    LogUtils.d(TAG, "网络出现异常");
                    ToastUtils.showShort(getContext(), "网络出现异常");
                    break;
            }
        }

        ;
    };

    public CheckUserInfoPopupWindow(Context context, View view, int width, int height) {
        super(context, view, width, height, R.style.anim_popup_dir);
    }

    @Override
    public void initView() {
        edittextWithClear_checkUserInfoPopup_username = (EdittextWithClear) getRootView().findViewById(R.id.edittextWithClear_checkUserInfoPopup_username);
        edittextWithClear_checkUserInfoPopup_password = (EdittextWithClear) getRootView().findViewById(R.id.edittextWithClear_checkUserInfoPopup_password);
        button_checkUserInfoPopup_cancel = (Button) getRootView().findViewById(R.id.button_checkUserInfoPopup_cancel);
        button_checkUserInfoPopup_ok = (Button) getRootView().findViewById(R.id.button_checkUserInfoPopup_ok);
    }

    @Override
    public void initEvent() {
        button_checkUserInfoPopup_cancel.setOnClickListener(this);
        button_checkUserInfoPopup_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_checkUserInfoPopup_cancel:
                CheckUserInfoPopupWindow.this.dismiss();
                break;
            case R.id.button_checkUserInfoPopup_ok:
                if (mOnCheckListener != null) {
                    String username = edittextWithClear_checkUserInfoPopup_username
                            .getText().toString().trim();
                    String password = edittextWithClear_checkUserInfoPopup_password
                            .getText().toString().trim();
                    String url = ConstantUtils.URL_LOGIN + "?phone=" + username
                            + "&password=" + password;
                    VolleyUtils.submitUsernameAndPassword(getContext(), url, mHandler);
                    LogUtils.d(TAG, "url:" + url);
                }
                break;
        }
    }


    public interface OnCheckListener {
        public void onCheck(boolean isAllow);
    }

    public void setOnCheckListener(OnCheckListener mOnSubmitListener) {
        this.mOnCheckListener = mOnSubmitListener;
    }

    @Override
    public void dismiss() {
        // 关闭软键盘
        KeyBoardUtils.closeKeybord(
                edittextWithClear_checkUserInfoPopup_username, getContext());
        super.dismiss();

    }

    //
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        // 打开软键盘
        KeyBoardUtils.openKeybord(
                edittextWithClear_checkUserInfoPopup_username, getContext());
        super.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 将JSON字符串转化为用户信息对象,并判断是否登录成功
     *
     * @param
     */
    protected void parseJSON(JSONObject object) throws JSONException {
        String state = object.optString("status");
        if (state.equals("error")) {
            ToastUtils.showShort(getContext(), "用户名或者密码错误");
            LogUtils.d(TAG, "用户名或者密码错误");
        } else {
            LogUtils.d(TAG, "用户信息通过验证");
            if (mOnCheckListener != null) {
                mOnCheckListener.onCheck(true);
                dismiss();
            }
        }

    }

}

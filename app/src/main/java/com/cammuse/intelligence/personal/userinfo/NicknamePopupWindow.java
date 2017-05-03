package com.cammuse.intelligence.personal.userinfo;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.common.EdittextWithClear;
import com.cammuse.intelligence.popup.BasePopupWindow;
import com.cammuse.intelligence.utils.ToastUtils;

/**
 * Created by Administrator on 2016/2/23.
 */
public class NicknamePopupWindow extends BasePopupWindow implements View.OnClickListener {

    private LinearLayout linearLayout_userinfoNicknamePopup_root;
    private EdittextWithClear textView_userinfoNicknamePopup_nickname;
    private Button button_userinfoNicknamePopup_cancel;
    private Button button_userinfoNicknamePopup_ok;

    public NicknamePopupWindow(Context context, View view, int width, int height) {
        super(context, view, width, height,R.style.anim_popup_dir);
    }

    @Override
    public void initView() {
        linearLayout_userinfoNicknamePopup_root = (LinearLayout) getRootView().findViewById(R.id.linearLayout_userinfoNicknamePopup_root);
        textView_userinfoNicknamePopup_nickname = (EdittextWithClear) getRootView().findViewById(R.id.textView_userinfoNicknamePopup_nickname);
        button_userinfoNicknamePopup_cancel = (Button) getRootView().findViewById(R.id.button_userinfoNicknamePopup_cancel);
        button_userinfoNicknamePopup_ok = (Button) getRootView().findViewById(R.id.button_userinfoNicknamePopup_ok);

    }

    @Override
    public void initEvent() {
        button_userinfoNicknamePopup_cancel.setOnClickListener(this);
        button_userinfoNicknamePopup_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_userinfoNicknamePopup_cancel:
                //TODO
                ToastUtils.showShort(getContext(), "button_userinfoNicknamePopup_cancel");
                dismiss();
                break;
            case R.id.button_userinfoNicknamePopup_ok:
                //TODO
                ToastUtils.showShort(getContext(), "button_userinfoNicknamePopup_cancel");
                break;
        }
    }
}

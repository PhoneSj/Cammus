package com.cammuse.intelligence.personal.userinfo;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.popup.BasePopupWindow;
import com.cammuse.intelligence.utils.ToastUtils;

/**
 * Created by Administrator on 2016/2/23.
 */
public class GenderPopupWindow extends BasePopupWindow implements View.OnClickListener {

    private LinearLayout linearLayout_userinfoGenderPopup_root;
    private RadioGroup radioGroup_userinfoGenderPopup_gender;
    private Button button_userinfoGenderPopup_cancel;
    private Button button_userinfoGenderPopup_ok;

    public GenderPopupWindow(Context context, View view, int width, int height) {
        super(context, view, width, height,R.style.anim_popup_dir);
    }

    @Override
    public void initView() {
        linearLayout_userinfoGenderPopup_root = (LinearLayout) getRootView().findViewById(R.id.linearLayout_userinfoGenderPopup_root);
        radioGroup_userinfoGenderPopup_gender = (RadioGroup) getRootView().findViewById(R.id.radioGroup_userinfoGenderPopup_gender);
        button_userinfoGenderPopup_cancel = (Button) getRootView().findViewById(R.id.button_userinfoGenderPopup_cancel);
        button_userinfoGenderPopup_ok = (Button) getRootView().findViewById(R.id.button_userinfoGenderPopup_ok);


    }

    @Override
    public void initEvent() {
        button_userinfoGenderPopup_cancel.setOnClickListener(this);
        button_userinfoGenderPopup_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_userinfoGenderPopup_cancel:
                //TODO
                ToastUtils.showShort(getContext(), "button_userinfoGenderPopup_cancel");
                dismiss();
                break;
            case R.id.button_userinfoGenderPopup_ok:
                //TODO
                ToastUtils.showShort(getContext(), "button_userinfoGenderPopup_ok");
                break;
        }
    }
}

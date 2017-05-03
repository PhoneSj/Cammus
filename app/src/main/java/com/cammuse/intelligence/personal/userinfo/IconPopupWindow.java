package com.cammuse.intelligence.personal.userinfo;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.popup.BasePopupWindow;
import com.cammuse.intelligence.utils.ToastUtils;

/**
 * Created by Administrator on 2016/2/23.
 */
public class IconPopupWindow extends BasePopupWindow implements View.OnClickListener {


    private static final java.lang.String TAG = "IconPopupWindow";
    private TextView textView_userinfoIconPopup_photo;
    private TextView textView_userinfoIconPopup_nativeImg;

    public IconPopupWindow(Context context, View view, int width, int height) {
        super(context, view, width, height,R.style.anim_popup_dir);
    }

    @Override
    public void initView() {
        textView_userinfoIconPopup_photo = (TextView) getRootView().findViewById(R.id.textView_userinfoIconPopup_photo);
        textView_userinfoIconPopup_nativeImg = (TextView) getRootView().findViewById(R.id.textView_userinfoIconPopup_nativeImg);
    }

    @Override
    public void initEvent() {
        textView_userinfoIconPopup_photo.setOnClickListener(this);
        textView_userinfoIconPopup_nativeImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_userinfoIconPopup_photo:
                //TODO
                ToastUtils.showShort(getContext(), "textView_userinfoIconPopup_photo");
                IconPopupWindow.this.dismiss();
                break;
            case R.id.textView_userinfoIconPopup_nativeImg:
                //TODO
                ToastUtils.showShort(getContext(), "textView_userinfoIconPopup_nativeImg");
                IconPopupWindow.this.dismiss();
                break;
        }
    }
}

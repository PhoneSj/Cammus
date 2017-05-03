package com.cammuse.intelligence.device;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.popup.BasePopupWindow;

/**
 * Created by Administrator on 2016/2/26.
 */
public class DeletePopupWindow extends BasePopupWindow implements View.OnClickListener {

    private TextView textView_deletePopup_delete;
    private TextView textView_deletePopup_cancel;

    private OnDeleteDeviceListener mOnDeleteDeviceListener;

    public DeletePopupWindow(Context context, View view, int width, int height, int animStyle) {
        super(context, view, width, height, animStyle);
    }

    @Override
    public void initView() {
        textView_deletePopup_delete = (TextView) getRootView().findViewById(R.id.textView_deletePopup_delete);
        textView_deletePopup_cancel = (TextView) getRootView().findViewById(R.id.textView_deletePopup_cancel);
    }

    @Override
    public void initEvent() {
        textView_deletePopup_delete.setOnClickListener(this);
        textView_deletePopup_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_deletePopup_delete:
                if (mOnDeleteDeviceListener!=null){
                    mOnDeleteDeviceListener.delete();
                }
                break;
            case R.id.textView_deletePopup_cancel:
                if (mOnDeleteDeviceListener!=null){
                    mOnDeleteDeviceListener.cancel();
                }
                break;
        }
    }

    public interface OnDeleteDeviceListener {
        public void delete();

        public void cancel();
    }

    public void setOnDeleteDeviceListener(OnDeleteDeviceListener mOnDeleteDeviceListener) {
        this.mOnDeleteDeviceListener = mOnDeleteDeviceListener;
    }
}

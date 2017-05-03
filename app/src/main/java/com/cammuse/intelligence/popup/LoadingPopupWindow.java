package com.cammuse.intelligence.popup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.cammuse.intelligence.R;

/**
 * Created by Administrator on 2016/2/23.
 */
public class LoadingPopupWindow extends BasePopupWindow {


    private Context context;
    private LoadingPopupWindow mLoadingPopupWindow;

    public LoadingPopupWindow(Context context, int width, int height) {
        super(context, getDefaultView(context), width, height, R.style.anim_popup_dir);
        this.context = context;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {

    }

    public static View getDefaultView(Context context) {
        View defaultView = LayoutInflater.from(context).inflate(R.layout.popupwindow_loading, null);
        return defaultView;
    }


}

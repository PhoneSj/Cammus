package com.cammuse.intelligence.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2016/2/23.
 */
public abstract class BasePopupWindow extends PopupWindow {
    private View view;
    private Context context;

    public BasePopupWindow(Context context, View view, int width, int height, int animStyle) {
        super(view, width, height);
        this.view = view;
        this.context = context;
        this.setAnimationStyle(animStyle);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new PaintDrawable());
        initView();
        initEvent();
    }

    public abstract void initView();

    public abstract void initEvent();

    public View getRootView() {
        return view;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setBackgroundAlpha(1.0f);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        setBackgroundAlpha(0.5f);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        setBackgroundAlpha(0.5f);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        setBackgroundAlpha(0.5f);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        setBackgroundAlpha(0.5f);
    }

    /**
     * 设置窗口背景变暗
     *
     * @param alpha
     */
    private void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = alpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }


}

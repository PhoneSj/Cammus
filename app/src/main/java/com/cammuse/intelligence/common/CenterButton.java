package com.cammuse.intelligence.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class CenterButton extends Button {

	private Context context;

	public CenterButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public CenterButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CenterButton(Context context) {
		this(context, null);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// 告诉父控件不要拦截，由子控件自己处理
		getParent().requestDisallowInterceptTouchEvent(true);
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

}

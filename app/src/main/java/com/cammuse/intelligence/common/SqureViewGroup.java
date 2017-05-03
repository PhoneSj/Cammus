package com.cammuse.intelligence.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class SqureViewGroup extends RelativeLayout {

	public SqureViewGroup(Context context) {
		this(context, null);
	}

	public SqureViewGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SqureViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int lineLength = Math.min(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
		setMeasuredDimension(lineLength, lineLength);
	}
}

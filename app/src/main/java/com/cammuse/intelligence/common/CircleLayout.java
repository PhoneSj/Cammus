package com.cammuse.intelligence.common;

/*
 * Copyright 2013 Csaba Szugyiczki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * 
 * @author Szugyi Creates a rotatable circle menu which can be parameterized by
 *         custom attributes. Handles touches and gestures to make the menu
 *         rotatable, and to make the menu items selectable and clickable.
 * 
 */
public class CircleLayout extends ViewGroup {
	// Event listeners

	// Background image
	private Bitmap imageOriginal, imageScaled;
	private Matrix matrix;

	private int mTappedViewsPostition = -1;
	private View mTappedView = null;
	private int selected = 0;

	// Child sizes
	private int mMaxChildWidth = 0;
	private int mMaxChildHeight = 0;
	private int childWidth = 0;
	private int childHeight = 0;

	// Sizes of the ViewGroup
	private int circleWidth, circleHeight;
	private int radius = 0;

	private float percent = 0.9f;

	// Touch detection
	private GestureDetector mGestureDetector;
	// needed for detecting the inversed rotations
	private boolean[] quadrantTouched;

	// Settings of the ViewGroup
	private boolean allowRotating = true;
	private float angle = -90;
	private float firstChildPos = 90;
	private boolean rotateToCenter = true;
	private boolean isRotating = true;

	/**
	 * @param context
	 */
	public CircleLayout(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CircleLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CircleLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	/**
	 * Returns the currently selected menu
	 * 
	 * @return the view which is currently the closest to the start position
	 */
	public View getSelectedItem() {
		return (selected >= 0) ? getChildAt(selected) : null;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mMaxChildWidth = 0;
		mMaxChildHeight = 0;

		// Measure once to find the maximum child size.
		int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);
		int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
				MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.AT_MOST);

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}

			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			mMaxChildWidth = Math.max(mMaxChildWidth, child.getMeasuredWidth());
			mMaxChildHeight = Math.max(mMaxChildHeight,
					child.getMeasuredHeight());
		}

		// Measure again for each child to be exactly the same size.
		childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxChildWidth,
				MeasureSpec.EXACTLY);
		childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxChildHeight,
				MeasureSpec.EXACTLY);

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == GONE) {
				continue;
			}

			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
		}

		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(widthMeasureSpec));

		// setMeasuredDimension(resolveSize(mMaxChildWidth, widthMeasureSpec),
		// resolveSize(mMaxChildHeight, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int layoutWidth = r - l;
		int layoutHeight = b - t;

		// Laying out the child views
		final int childCount = getChildCount();
		int visiable_count = 0;
		for (int i = 0; i < childCount; i++) {
			if (getChildAt(i).getVisibility() == View.VISIBLE) {
				visiable_count = visiable_count + 1;
			}
		}
		int left, top;
		radius = (int) ((layoutWidth <= layoutHeight) ? layoutWidth / 2 * percent
				: layoutHeight / 2*percent);
		// 图标大小
		childWidth = (int) (radius / 3);
		childHeight = (int) (radius / 3);
		// 计算每个子控件间相距的角度差
		float angleDelay = 360 / visiable_count;

		// 计算每个子控件在父容器中的位置
		for (int i = 0; i < childCount; i++) {
			final ImageView child = (ImageView) getChildAt(i);
			if (child.getVisibility() != View.VISIBLE) {
				continue;
			}
			// 计算子控件距离父容器左端的距离
			left = Math
					.round((float) (((layoutWidth / 2) - childWidth / 2) + (radius - childWidth / 2)
							* Math.cos(Math.toRadians(angle))));
			// 计算子控件距离父容器上端的距离
			top = Math
					.round((float) (((layoutHeight / 2) - childHeight / 2) + (radius - childHeight / 2)
							* Math.sin(Math.toRadians(angle))));

			// 将当前子控件放到父容器相应的位置
			child.layout(left, top, left + childWidth, top + childHeight);
			// System.out.println(" "+i);
			angle += angleDelay;
		}
	}

}
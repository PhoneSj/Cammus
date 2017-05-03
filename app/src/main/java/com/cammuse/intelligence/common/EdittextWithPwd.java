package com.cammuse.intelligence.common;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.cammuse.intelligence.R;
import com.cammuse.intelligence.utils.LogUtils;

/**
 * @说明： 自定义带密码可见按钮的EditText
 * 
 */
public class EdittextWithPwd extends EditText implements OnFocusChangeListener,
		TextWatcher {
	// EditText右侧的删除按钮
	private Drawable mPwdDrawable;
	private boolean hasFoucs;

	private boolean isVisiable = false;

	public EdittextWithPwd(Context context) {
		this(context, null);
	}

	public EdittextWithPwd(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.editTextStyle);
	}

	public EdittextWithPwd(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3,）
		mPwdDrawable = getCompoundDrawables()[2];
		if (mPwdDrawable == null) {
			mPwdDrawable = getResources().getDrawable(
					R.mipmap.password_invisible);
		}

		mPwdDrawable.setBounds(0, 0, mPwdDrawable.getIntrinsicWidth(),
				mPwdDrawable.getIntrinsicHeight());
		// 默认设置隐藏图标
		setPwdIconVisible(false);
		// 设置焦点改变的监听
		setOnFocusChangeListener(this);
		// 设置输入框里面内容发生改变的监听
		addTextChangedListener(this);
	}

	/*
	 * @说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标 event.getX()
	 * 获取相对应自身左上角的X坐标 event.getY() 获取相对应自身左上角的Y坐标 getWidth() 获取控件的宽度 getHeight()
	 * 获取控件的高度 getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离 getPaddingRight()
	 * 获取删除图标右边缘到控件右边缘的距离 isInnerWidth: getWidth() - getTotalPaddingRight()
	 * 计算删除图标左边缘到控件左边缘的距离 getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
	 * isInnerHeight: distance 删除图标顶部边缘到控件顶部边缘的距离 distance + height
	 * 删除图标底部边缘到控件顶部边缘的距离
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				Rect rect = getCompoundDrawables()[2].getBounds();
				int height = rect.height();
				int distance = (getHeight() - height) / 2;
				boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight())
						&& x < (getWidth() - getPaddingRight());
				boolean isInnerHeight = y > distance && y < (distance + height);
				if (isInnerWidth && isInnerHeight) {
					// this.setText("");
					LogUtils.d("debug", "-------------");
					if (isVisiable) {

						EdittextWithPwd.this
								.setTransformationMethod(PasswordTransformationMethod
										.getInstance());
						// mPwdDrawable = getResources().getDrawable(
						// R.drawable.password_invisible);
						// setPwdIconVisible(true);
						isVisiable = false;
					} else {
						setInputType(0x90);
						EdittextWithPwd.this
								.setTransformationMethod(HideReturnsTransformationMethod
										.getInstance());
						// mPwdDrawable = getResources().getDrawable(
						// R.drawable.password_visible);
						// setPwdIconVisible(true);
						isVisiable = true;
					}

				}
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 当PwdEditText焦点发生变化的时候， 输入长度为零，隐藏删除图标，否则，显示删除图标
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;
		if (hasFocus) {
			setPwdIconVisible(getText().length() > 0);
		} else {
			setPwdIconVisible(false);
		}
	}

	protected void setPwdIconVisible(boolean visible) {
		if (isVisiable) {
			mPwdDrawable = getResources().getDrawable(
					R.mipmap.password_invisible);
		} else {
			mPwdDrawable = getResources().getDrawable(
					R.mipmap.password_visible);
		}
		mPwdDrawable.setBounds(0, 0, mPwdDrawable.getIntrinsicWidth(),
				mPwdDrawable.getIntrinsicHeight());
		
		Drawable right = visible ? mPwdDrawable : null;
		setCompoundDrawables(getCompoundDrawables()[0],
				getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (hasFoucs) {
			setPwdIconVisible(s.length() > 0);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

}

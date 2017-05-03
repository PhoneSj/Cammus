package com.cammuse.intelligence.common;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.cammuse.intelligence.R;

public class MyProgressBar extends ProgressBar {

	private static final int DEFAULT_TEXT_SIZE = 30;// 默认文本的大小
	private static final int DEFAULT_TEXT_COLOR = 0XFFFC00D1;// 默认到达的进度条的颜色
	private static final int DEFAULT_COLOR_UNREACHED_COLOR = 0xFFd3d6da;// 默认未到达的进度条的颜色
	private static final int DEFAULT_HEIGHT_REACHED_PROGRESS_BAR = 2;// 默认到达部分的进度条的高度
	private static final int DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR = 2;// 默认未到达的进度条的高度
	private static final int DEFAULT_SIZE_TEXT_OFFSET = 10;// 默认文本与进度条之间的间距

	/**
	 * painter of all drawing things
	 */
	protected Paint mPaint = new Paint();
	/**
	 * color of progress number
	 */
	protected int mTextColor = DEFAULT_TEXT_COLOR;
	/**
	 * size of text (sp)
	 */
	protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);

	/**
	 * offset of draw progress
	 */
	protected int mTextOffset = dp2px(DEFAULT_SIZE_TEXT_OFFSET);

	/**
	 * height of reached progress bar
	 */
	protected int mReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_REACHED_PROGRESS_BAR);

	/**
	 * color of reached bar
	 */
	protected int mReachedBarColor = DEFAULT_TEXT_COLOR;
	/**
	 * color of unreached bar
	 */
	protected int mUnReachedBarColor = DEFAULT_COLOR_UNREACHED_COLOR;
	/**
	 * height of unreached progress bar
	 */
	protected int mUnReachedProgressBarHeight = dp2px(DEFAULT_HEIGHT_UNREACHED_PROGRESS_BAR);
	/**
	 * view width except padding
	 */
	protected int mRealWidth;

	protected boolean mIfDrawText = true;

	protected static final int VISIBLE = 0;

	public MyProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		obtainStyledAttributes(attrs);
		mPaint.setTextSize(mTextSize);
		mPaint.setColor(mTextColor);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(width, height);

		mRealWidth = getMeasuredWidth() - getPaddingRight() - getPaddingLeft();
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		if (specMode == MeasureSpec.EXACTLY)// 用户明确指定了该进度条的高度
		{
			result = specSize;
		} else {
			float textHeight = (mPaint.descent() - mPaint.ascent());
			result = (int) (getPaddingTop() + getPaddingBottom() + Math.max(
					Math.max(mReachedProgressBarHeight,
							mUnReachedProgressBarHeight), Math.abs(textHeight)));
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * get the styled attributes
	 * 
	 * @param attrs
	 */
	private void obtainStyledAttributes(AttributeSet attrs) {
		// init values from custom attributes
		final TypedArray attributes = getContext().obtainStyledAttributes(
				attrs, R.styleable.HorizontalProgressBarWithNumber);

		mTextColor = attributes
				.getColor(
						R.styleable.HorizontalProgressBarWithNumber_progress_text_color,
						DEFAULT_TEXT_COLOR);
		mTextSize = (int) attributes.getDimension(
				R.styleable.HorizontalProgressBarWithNumber_progress_text_size,
				mTextSize);

		mReachedBarColor = attributes
				.getColor(
						R.styleable.HorizontalProgressBarWithNumber_progress_reached_color,
						mTextColor);
		mUnReachedBarColor = attributes
				.getColor(
						R.styleable.HorizontalProgressBarWithNumber_progress_unreached_color,
						DEFAULT_COLOR_UNREACHED_COLOR);
		mReachedProgressBarHeight = (int) attributes
				.getDimension(
						R.styleable.HorizontalProgressBarWithNumber_progress_reached_bar_height,
						mReachedProgressBarHeight);
		mUnReachedProgressBarHeight = (int) attributes
				.getDimension(
						R.styleable.HorizontalProgressBarWithNumber_progress_unreached_bar_height,
						mUnReachedProgressBarHeight);
		mTextOffset = (int) attributes
				.getDimension(
						R.styleable.HorizontalProgressBarWithNumber_progress_text_offset,
						mTextOffset);
		int textVisible = attributes
				.getInt(R.styleable.HorizontalProgressBarWithNumber_progress_text_visibility,
						VISIBLE);
		if (textVisible != VISIBLE) {
			mIfDrawText = false;
		}
		attributes.recycle();
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {

		canvas.save();
		// 画板平移，以后坐标原点就在此位置
		canvas.translate(getPaddingLeft(), getHeight() / 2);
		if (mIfDrawText) {
			drawHasText(canvas);
		} else {
			drawNoText(canvas);
		}
		canvas.restore();
	}

	private void drawHasText(Canvas canvas) {
		boolean noNeedBg = false;
		// 进度值(百分值)
		float radio = getProgress() * 1.0f / getMax();
		// 当前进度已经到达的宽度
		float progressPosX = (int) (mRealWidth * radio);
		// 需要绘制的文本
		String text = getProgress() + "%";
		// mPaint.getTextBounds(text, 0, text.length(), mTextBound);
		// 拿到字体的宽度、高度
		float textWidth = mPaint.measureText(text);
		float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;
		// 已经到到达，则后面的进度不用绘制（因为文本占据了这部分区域）
		if (progressPosX + textWidth > mRealWidth) {
			progressPosX = mRealWidth - textWidth;
			noNeedBg = true;
		}

		// 绘制已经到达的进度
		float endX = progressPosX - mTextOffset / 2;
		if (endX > 0) {
			mPaint.setColor(mReachedBarColor);
			mPaint.setStrokeWidth(mReachedProgressBarHeight);// 设置绘制的线条宽度
			canvas.drawLine(0, 0, endX, 0, mPaint);
		}
		// 绘制文本(当布局文件中设置了不现实进度则不绘制)
		if (mIfDrawText) {
			mPaint.setColor(mTextColor);
			canvas.drawText(text, progressPosX, -textHeight, mPaint);
		}

		// 绘制未到达的进度
		if (!noNeedBg) {
			float start = progressPosX + mTextOffset / 2 + textWidth;
			mPaint.setColor(mUnReachedBarColor);
			mPaint.setStrokeWidth(mUnReachedProgressBarHeight);
			canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
		}

	}

	private void drawNoText(Canvas canvas) {
		boolean noNeedBg = false;
		// 进度值(百分值)
		float radio = getProgress() * 1.0f / getMax();
		// 当前进度已经到达的宽度
		float progressPosX = (int) (mRealWidth * radio);
		// 需要绘制的文本
		String text = getProgress() + "%";
		// mPaint.getTextBounds(text, 0, text.length(), mTextBound);
		// 已经到到达，则后面的进度不用绘制（因为文本占据了这部分区域）
		if (progressPosX > mRealWidth) {
			progressPosX = mRealWidth;
			noNeedBg = true;
		}

		// 绘制已经到达的进度
		float endX = progressPosX;
		if (endX > 0) {
			mPaint.setColor(mReachedBarColor);
			mPaint.setStrokeWidth(getHeight());// 设置绘制的线条宽度
			canvas.drawLine(0, 0, endX, 0, mPaint);
		}

		// 绘制未到达的进度
		if (!noNeedBg) {
			float start = progressPosX;
			mPaint.setColor(mUnReachedBarColor);
			mPaint.setStrokeWidth(getHeight());
			canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
		}

	}

	/**
	 * dp 2 px
	 * 
	 * @param dpVal
	 */
	protected int dp2px(int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, getResources().getDisplayMetrics());
	}

	/**
	 * sp 2 px
	 * 
	 * @param spVal
	 * @return
	 */
	protected int sp2px(int spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, getResources().getDisplayMetrics());

	}

}

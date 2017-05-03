package com.cammuse.intelligence.gesture.lock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.cammuse.intelligence.utils.LogUtils;

import java.util.List;

public class LockPreView extends View {

	private static final String TAG = "LockView";
	private int width;
	private int height;

	private Paint mPaint;

	private List<Integer> mChoose;

	public LockPreView(Context context) {
		this(context, null);
	}

	public LockPreView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LockPreView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		width = getWidth();
		height = getHeight();
		int length = Math.min(width, height);
		int radius = length / 10;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int cx = radius * (2 + j * 3);
				int cy = radius * (2 + i * 3);
				mPaint.setColor(0xffaaaaaa);
				canvas.drawCircle(cx, cy, radius, mPaint);
			}
		}

		drawChoose(canvas, radius, mChoose);

		canvas.restore();

	}

	/**
	 * 绘制选中的圆圈
	 * 
	 * @param mChoose
	 */
	private void drawChoose(Canvas canvas, int radius, List<Integer> mChoose) {
		LogUtils.d(TAG, "---drawChoose--");
		if (mChoose != null) {
			for (int i = 0; i < mChoose.size(); i++) {
				int lie = (mChoose.get(i)-1) % 3;
				int hang = (mChoose.get(i)-1) / 3;
				int cx = radius * (2 + lie * 3);
				int cy = radius * (2 + hang * 3);
				mPaint.setColor(0xffaa0000);
				canvas.drawCircle(cx, cy, radius, mPaint);
			}
			LogUtils.d(TAG, "---drawChoose   inner--");
		}

	}

	public List<Integer> getChoose() {
		return mChoose;
	}

	public void setChoose(List<Integer> mChoose) {
		this.mChoose = mChoose;
		invalidate();
	}
}

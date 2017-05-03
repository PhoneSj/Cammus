package com.cammuse.intelligence.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cammuse.intelligence.R;

public class CustomDialog extends Dialog {
	private int layoutRes;// 布局文件
	private Context context;
	private TextView textView;
	private String text;

	public CustomDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CustomDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	public CustomDialog(Context context, int theme, int resLayout) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
	}

	public CustomDialog(Context context, int theme, int resLayout, String text) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		this.text = text;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);
		textView = (TextView) findViewById(R.id.textView_dialog);
		if (text == null) {
			textView.setVisibility(View.GONE);
		} else {
			textView.setText(text);
		}
	}
}

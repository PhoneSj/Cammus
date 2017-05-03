package com.cammuse.intelligence.personal.setting;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cammuse.intelligence.BaseActivity;
import com.cammuse.intelligence.R;
import com.cammuse.intelligence.utils.AppUtils;
import com.cammuse.intelligence.utils.ConstantUtils;

public class AboutActivity extends BaseActivity implements OnClickListener {

    private ImageView imageView_aboutAct_back;
    private TextView textView_aboutAct_website;
    private TextView textView_aboutAct_version;
    private TextView textView_aboutAct_appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        initView();
        initEvent();
    }

    private void initView() {
        imageView_aboutAct_back = (ImageView) findViewById(R.id.imageView_aboutAct_back);
        textView_aboutAct_website = (TextView) findViewById(R.id.textView_aboutAct_website);
        textView_aboutAct_appName = (TextView) findViewById(R.id.textView_aboutAct_appName);
        textView_aboutAct_version = (TextView) findViewById(R.id.textView_aboutAct_version);
        textView_aboutAct_website.setText(Html
                .fromHtml(ConstantUtils.URL_OFFICIAL_WEBSITE));
        textView_aboutAct_appName.setText(AppUtils
                .getAppName(AboutActivity.this));
        textView_aboutAct_version.setText("版本："
                + AppUtils.getVersionName(AboutActivity.this));

    }

    private void initEvent() {
        imageView_aboutAct_back.setOnClickListener(this);
        textView_aboutAct_website.setMovementMethod(LinkMovementMethod
                .getInstance());// 设置点击事件
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_aboutAct_back:
                finish();
                break;

            default:
                break;
        }

    }

}

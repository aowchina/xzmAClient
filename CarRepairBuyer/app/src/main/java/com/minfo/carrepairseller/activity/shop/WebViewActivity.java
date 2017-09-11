package com.minfo.carrepairseller.activity.shop;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.carrepairseller.R;
import com.minfo.carrepairseller.activity.BaseActivity;

public class WebViewActivity extends BaseActivity implements View.OnClickListener{
    private TextView tvTitle;
    private ImageView ivBack;
    private WebView webView;
    private String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_web_view);
    }

    @Override
    protected void findViews() {
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        ivBack = ((ImageView) findViewById(R.id.iv_back));
        webView = ((WebView) findViewById(R.id.webView));

    }

    @Override
    protected void initViews() {

        tvTitle.setText("广告详情");
        ivBack.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        webView.getSettings().setJavaScriptEnabled(true);
        info = getIntent().getStringExtra("info");
        if (info!=null&&!info.equals(""))
        webView.loadUrl(info);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;

        }
    }
}

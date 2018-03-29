package com.app.baselib.widget;

import android.support.v4.content.ContextCompat;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.app.baselib.R;
import com.app.baselib.mvp.BaseActivity;
import com.blankj.utilcode.util.BarUtils;


public class WebActivity extends BaseActivity {

    private WebView mWeb;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void init() {
        BarUtils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.white), 30);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(getIntent().getStringExtra("title"));

        mWeb = (WebView) findViewById(R.id.web);
        WebSettings settings = mWeb.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        mWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWeb.loadUrl(getIntent().getStringExtra("url"));
    }

}

package com.cm.strawberry.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.cm.strawberry.R;
import com.cm.strawberry.base.BaseActivity;

public class NewsdetailsActivity extends BaseActivity {
    WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetails);
        init();
    }

    private void init() {
        webView = (WebView) findViewById(R.id.webview);
        if (!getIntent().getStringExtra("url").equals("")) {
            url=getIntent().getStringExtra("url");
            webView.loadUrl(url);
        }

    }
}

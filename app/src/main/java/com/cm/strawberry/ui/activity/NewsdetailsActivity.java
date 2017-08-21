package com.cm.strawberry.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cm.strawberry.R;
import com.cm.strawberry.base.BaseActivity;

import butterknife.Bind;

public class NewsdetailsActivity extends BaseActivity {
    @Bind(R.id.webview)
    WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetails);
        init();
    }

    private void init() {
        url = getIntent().getStringExtra("url");
        if (url == null){
            return;
        }
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
            }

        });
    }
}

package com.cm.strawberry.ui.activity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cm.strawberry.R;
import com.cm.strawberry.base.BaseActivity;
import com.cm.strawberry.ui.BrowserFragmentWithToolbar;
import com.cm.strawberry.ui.CustomBrowserFragment;
import com.cm.strawberry.util.StringUtil;
import com.cm.strawberry.webview.SimpleWebviewEventsListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NewsdetailsActivity extends BaseActivity {
    private CustomBrowserFragment browserFragment;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetails);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        browserFragment = BrowserFragmentWithToolbar.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.webview_container, browserFragment, "browser")
                .commitAllowingStateLoss();
        url = getIntent().getStringExtra("url");
        browserFragment.setWebViewEventsListener(new SimpleWebviewEventsListener(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        if (!StringUtil.isEmpty(url)) {
            browserFragment.loadUrl(url);
        }
    }
}

package com.cm.strawberry.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cm.strawberry.R;
import com.cm.strawberry.util.Utils;

/**
 * Created by zhouwei on 17-8-22.
 */

public class BrowserFragmentWithToolbar extends CustomBrowserFragment implements View.OnClickListener{
    private TextView closeView;

    public static BrowserFragmentWithToolbar newInstance() {

        Bundle args = new Bundle();

        BrowserFragmentWithToolbar fragment = new BrowserFragmentWithToolbar();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browser_with_toolbar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setProgressDrawable(R.drawable.reading_progress_drawable);
        closeView =  view.findViewById(R.id.iv_close);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        closeView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            switch (v.getId()) {
                case R.id.iv_back:
                    if (getWebView().canGoBack()) {
                        getWebView().goBack();
                    } else {
                        getActivity().finish();
                    }
                    break;
                case R.id.iv_close:
                    getActivity().finish();
                    break;
            }
        }
    }

    @Override
    protected void onProgressChanged(int progress) {
        super.onProgressChanged(progress);
        if (webView.canGoBack()) {
            Utils.setViewVisibility(closeView, View.VISIBLE);
        } else {
            Utils.setViewVisibility(closeView, View.GONE);
        }
    }
}

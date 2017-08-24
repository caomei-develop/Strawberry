package com.cm.strawberry.webview;

import android.graphics.Bitmap;
import android.webkit.WebView;

public class SimpleWebviewEventsListener implements WebViewEventsListener {
	@Override
	public void onReceivedTitle(WebView view, String title) {

	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {

	}

	@Override
	public void onReceivedIcon(WebView view, Bitmap icon) {

	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

	}

	@Override
	public void onPageStarted(WebView webView, String s, Bitmap bitmap) {

	}

	@Override
	public void onPageFinished(WebView view, String url) {

	}

	@Override
	public boolean interceptUrl(WebView view, String url) {
		return false;
	}

	@Override
	public boolean interceptRequest(WebView view, String url) {
		return false;
	}

	@Override
	public void onCreate(WebView view) {

	}

	@Override
	public void onDestroy(WebView view) {

	}
}

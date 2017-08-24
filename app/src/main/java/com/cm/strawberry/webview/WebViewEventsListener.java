package com.cm.strawberry.webview;

import android.graphics.Bitmap;
import android.webkit.WebView;


public interface WebViewEventsListener {
	void onReceivedTitle(WebView view, String title);

	void onProgressChanged(WebView view, int newProgress);

	void onReceivedIcon(WebView view, Bitmap icon);

	void onReceivedError(final WebView view, int errorCode, String description, String failingUrl);

	void onPageStarted(WebView webView, String s, Bitmap bitmap);

	void onPageFinished(WebView view, String url);

	boolean interceptUrl(WebView view, String url);

	boolean interceptRequest(WebView view, String url);

	void onCreate(WebView view);

	void onDestroy(WebView view);

	class WebViewEventsListenerAdapter implements WebViewEventsListener {

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
}

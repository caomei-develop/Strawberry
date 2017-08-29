package com.cm.strawberry.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.WebView;

import java.util.Map;

public class CustomWebView extends WebView {

	private boolean destroyed = false;

	public CustomWebView(Context context) {
		super(context);
	}

	public CustomWebView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	public CustomWebView(Context context, AttributeSet attributeSet, int i) {
		super(context, attributeSet, i);
	}

	@Override
	public void destroy() {
		destroyed = true;
		super.destroy();
	}

	@Override
	public void loadUrl(String s) {
		if (destroyed) {
			return;
		}
		super.loadUrl(s);
	}

	@Override
	public void loadUrl(String s, Map<String, String> map) {
		if (destroyed) {
			return;
		}
		super.loadUrl(s, map);
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		try {
			return super.onCreateInputConnection(outAttrs);
		} catch (Throwable t) {
			return null;
		}
	}
}

package com.cm.strawberry.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;


import java.util.Map;

public class CustomX5WebView extends WebView {

	private boolean destroyed = false;

	public CustomX5WebView(Context context) {
		super(context);
	}

	public CustomX5WebView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	public CustomX5WebView(Context context, AttributeSet attributeSet, int i) {
		super(context, attributeSet, i);
	}

	public CustomX5WebView(Context context, AttributeSet attributeSet, int i, boolean b) {
		super(context, attributeSet, i, b);
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
}

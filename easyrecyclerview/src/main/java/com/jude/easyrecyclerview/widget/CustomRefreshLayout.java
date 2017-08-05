package com.jude.easyrecyclerview.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class CustomRefreshLayout extends SwipeRefreshLayout {

	private CanChildScrollUpCallback mCanChildScrollUpCallback;

	public CustomRefreshLayout(Context context) {
		super(context);
	}

	public CustomRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public interface CanChildScrollUpCallback {
		boolean canSwipeRefreshChildScrollUp();
	}

	public void setCanChildScrollUpCallback(CanChildScrollUpCallback canChildScrollUpCallback) {
		mCanChildScrollUpCallback = canChildScrollUpCallback;
	}

	@Override
	public boolean canChildScrollUp() {
		if (mCanChildScrollUpCallback != null) {
			return mCanChildScrollUpCallback.canSwipeRefreshChildScrollUp();
		}
		return super.canChildScrollUp();
	}
}

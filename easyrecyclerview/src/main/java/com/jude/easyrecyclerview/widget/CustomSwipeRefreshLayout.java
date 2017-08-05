package com.jude.easyrecyclerview.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {

	private int miniDistance = 15;
	private float mPrevX;
	private float mPrevY;
	private boolean flag;

	public CustomSwipeRefreshLayout(Context context) {
		super(context);
	}

	public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				mPrevX = event.getX();
				mPrevY = event.getY();
				flag = false;
				break;

			case MotionEvent.ACTION_MOVE:
				final float eventX = event.getX();
				final float eventY = event.getX();
				float xDiff = Math.abs(eventX - mPrevX);
				float yDiff = Math.abs(eventY - mPrevY);

				if (flag || Math.abs(yDiff) > 60
						|| (Math.abs(yDiff) < Math.abs(xDiff) && Math.abs(xDiff) > miniDistance)) {
					flag = true;
					return false;
				}
		}

		return super.onInterceptTouchEvent(event);
	}
}

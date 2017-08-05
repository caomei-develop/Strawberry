package com.jude.easyrecyclerview.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ToutiaoRecyclerView extends RecyclerView {
	private List<OnItemTouchListener> mOnItemTouchListeners = new ArrayList<>();
	private OnUpListener upListener;

	public ToutiaoRecyclerView(Context context) {
		super(context);
	}

	public ToutiaoRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ToutiaoRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void addOnItemTouchListener(OnItemTouchListener listener) {
		super.addOnItemTouchListener(listener);
		mOnItemTouchListeners.add(listener);
	}

	@Override
	public void removeOnItemTouchListener(OnItemTouchListener listener) {
		super.removeOnItemTouchListener(listener);
		mOnItemTouchListeners.remove(listener);
	}

	public List<OnItemTouchListener> getOnItemTouchListeners() {
		return mOnItemTouchListeners;
	}

	public void setUpListener(OnUpListener upListener) {
		this.upListener = upListener;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		final int action = MotionEventCompat.getActionMasked(ev);
		if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
			if (upListener != null) {
				upListener.onUped();
			}
			resetOtherOnTouchListener(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	public static interface OnUpListener {
		public void onUped();
	}

	private void resetOtherOnTouchListener(MotionEvent event) {
		if (mOnItemTouchListeners != null) {
			for (RecyclerView.OnItemTouchListener itemTouchListener : mOnItemTouchListeners) {
				if (itemTouchListener != this) {
					itemTouchListener.onInterceptTouchEvent(this, event);
				}
			}
		}
	}
}

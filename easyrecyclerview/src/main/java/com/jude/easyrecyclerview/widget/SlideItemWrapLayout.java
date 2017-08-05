package com.jude.easyrecyclerview.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jude.easyrecyclerview.R;

public class SlideItemWrapLayout extends RelativeLayout {

	private View mRightBackView;
	private LinearLayout mFrontView;

	public SlideItemWrapLayout(Context context, int frontViewId, int rightBackViewId) {
		super(context);
		init(frontViewId, rightBackViewId);
	}

	private void init(int frontViewId, int rightBackViewId) {
		setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		View frontView = null;
		if (frontViewId != 0) {
			frontView = LayoutInflater.from(getContext()).inflate(frontViewId, this, false);
		}
		if (frontView == null) {
			throw new NullPointerException("frontView can not be null");
		}
		View rightBackView = null;

		if (rightBackViewId != 0) {
			rightBackView = LayoutInflater.from(getContext()).inflate(rightBackViewId, this, false);
		}

		addFrontView(frontView);
		addRightBackView(rightBackView);
	}

	private void addFrontView(View frontView) {
		LayoutParams params = (LayoutParams) frontView.getLayoutParams();
		if (params == null) {
			params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		}

		LinearLayout wrapLayout = new LinearLayout(getContext());
		wrapLayout.addView(frontView, params);
		wrapLayout.setId(R.id.slide_id_front_view);

		addView(wrapLayout, params);
		mFrontView = wrapLayout;
	}

	private void addRightBackView(View rightBackView) {
		if (rightBackView == null) {
			return;
		}
		LayoutParams params = (LayoutParams) rightBackView.getLayoutParams();
		if (params == null) {
			params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		}
		params.addRule(RelativeLayout.RIGHT_OF, R.id.slide_id_front_view);
		rightBackView.setLayoutParams(params);
		rightBackView.setId(R.id.slide_id_right_back_view);
		addView(rightBackView);
		mRightBackView = rightBackView;
		setRightBackViewShow(false);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int parentWidthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
		int parentHeightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY);
		if (mRightBackView != null) {
			LayoutParams params = (LayoutParams) mRightBackView.getLayoutParams();
			int widthSpec = ViewGroup.getChildMeasureSpec(parentWidthSpec, getPaddingLeft() + getPaddingRight()
					+ params.leftMargin + params.rightMargin, params.width);
			int heightSpec = ViewGroup.getChildMeasureSpec(parentHeightSpec, getPaddingTop() + getPaddingBottom()
					+ params.topMargin + params.bottomMargin, params.height);
			mRightBackView.measure(widthSpec, heightSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		if (mRightBackView != null) {
			int top = (b - t - mRightBackView.getMeasuredHeight()) / 2;
			mRightBackView
					.layout(mFrontView.getRight(), top, mFrontView.getRight() + mRightBackView.getMeasuredWidth(), top
							+ mRightBackView.getMeasuredHeight());
		}
	}

	public View getFrontView() {
		return mFrontView;
	}

	public View getRightBackView() {
		return mRightBackView;
	}

	public void setRightBackViewShow(boolean show) {
		setViewShow(mRightBackView, show);
	}

	private void setViewShow(View view, boolean show) {
		if (view == null) {
			return;
		}
		if (show) {
			if (view.getVisibility() != View.VISIBLE) {
				view.setVisibility(View.VISIBLE);
			}
		} else {
			if (view.getVisibility() != View.INVISIBLE) {
				view.setVisibility(View.INVISIBLE);
			}
		}
	}

}

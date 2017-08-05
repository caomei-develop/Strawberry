package com.jude.easyrecyclerview.helper;

import static android.support.v4.view.ViewCompat.getTranslationX;
import static android.support.v4.view.ViewCompat.setTranslationX;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.widget.SlideItemWrapLayout;

public class SlideOnTouchListener implements RecyclerView.OnItemTouchListener {
	private float startX;
	private float startY;
	private float lastX;

	private static final int ACTIVE_POINTER_ID_NONE = -1;
	int mActivePointerId = ACTIVE_POINTER_ID_NONE;
	private BaseViewHolder openedViewHolder;

	private int mSlop;

	private RecyclerView recyclerView;

	private boolean isAutoSliding;

	public SlideOnTouchListener(RecyclerView recyclerView) {
		this.recyclerView = recyclerView;
		ViewConfiguration vc = ViewConfiguration.get(recyclerView.getContext());
		mSlop = vc.getScaledTouchSlop();
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent event) {
		if (isAutoSliding) {
			return true;
		}
		final int action = MotionEventCompat.getActionMasked(event);
		if (action == MotionEvent.ACTION_DOWN) {
			mActivePointerId = MotionEventCompat.getPointerId(event, 0);
			startX = lastX = event.getX();
			startY = event.getY();
			if (openedViewHolder != null) {
				SlideItemWrapLayout slideView = (SlideItemWrapLayout) openedViewHolder.itemView;
				if (findViewHolder(event) != openedViewHolder) {
					scrollAnimator(slideView, getTranslationX(slideView.getFrontView()), 0, false);
					openedViewHolder = null;
					return true;
				} else {
					return false;
				}
			}
		} else if (action == MotionEvent.ACTION_MOVE) {
			if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
				return false;
			}
			BaseViewHolder currentViewHolder = (BaseViewHolder) findSlideView(event);
			if (currentViewHolder != null && currentViewHolder.itemView instanceof SlideItemWrapLayout) {
				openedViewHolder = currentViewHolder;
			}
		} else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			mActivePointerId = ACTIVE_POINTER_ID_NONE;
			return false;
		}
		return openedViewHolder != null;
	}

	@Override
	public void onTouchEvent(RecyclerView rv, MotionEvent event) {
		if (openedViewHolder == null || !(openedViewHolder.itemView instanceof SlideItemWrapLayout)) {
			return;
		}
		final int action = MotionEventCompat.getActionMasked(event);
		final int activePointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);

		if (isAutoSliding) {
			return;
		}

		SlideItemWrapLayout slideView = (SlideItemWrapLayout) openedViewHolder.itemView;
		switch (action) {
			case MotionEvent.ACTION_MOVE: {
				if (activePointerIndex >= 0) {
					float dx = event.getX() - lastX;
					lastX = event.getX();

					float shouldScrollDistance = getTranslationX(slideView.getFrontView()) + dx;
					if (shouldScrollDistance < -slideView.getRightBackView().getWidth()) {
						shouldScrollDistance = -slideView.getRightBackView().getWidth();
					}

					if (shouldScrollDistance > 0) {
						shouldScrollDistance = 0;
					}
					transX(slideView, shouldScrollDistance);
				}
				break;
			}
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				if (openedViewHolder != null) {
					float totalWidth = slideView.getRightBackView().getWidth();
					if (Math.abs(getTranslationX(slideView.getFrontView())) > totalWidth * 0.5) {
						scrollAnimator(slideView, getTranslationX(slideView.getFrontView()), 0 - totalWidth, true);
					} else {
						scrollAnimator(slideView, getTranslationX(slideView.getFrontView()), 0, false);
						openedViewHolder = null;
					}
				}
				mActivePointerId = ACTIVE_POINTER_ID_NONE;
				break;
		}
	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
		if (!disallowIntercept) {
			return;
		}
		reset();
	}

	private void setExtraShow(View view, boolean show) {
		((SlideItemWrapLayout) view).setRightBackViewShow(show);
	}

	private BaseViewHolder findViewHolder(MotionEvent event) {
		View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
		if (child == null) {
			return null;
		}
		return (BaseViewHolder) recyclerView.getChildViewHolder(child);
	}

	private RecyclerView.ViewHolder findSlideView(MotionEvent motionEvent) {
		final RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
		if (mActivePointerId == ACTIVE_POINTER_ID_NONE) {
			return null;
		}
		final int pointerIndex = MotionEventCompat.findPointerIndex(motionEvent, mActivePointerId);
		if (pointerIndex == ACTIVE_POINTER_ID_NONE) {
			return null;
		}
		final float dx = MotionEventCompat.getX(motionEvent, pointerIndex) - startX;
		final float dy = MotionEventCompat.getY(motionEvent, pointerIndex) - startY;
		final float absDx = Math.abs(dx);
		final float absDy = Math.abs(dy);

		if (absDx < mSlop && absDy < mSlop) {
			return null;
		}
		if (absDx > absDy && lm.canScrollHorizontally()) {
			return null;
		} else if (absDy > absDx && lm.canScrollVertically()) {
			return null;
		}
		return findViewHolder(motionEvent);
	}

	private void scrollAnimator(final View view, float from, float to, final boolean showExtra) {
		if (from == to) {
			isAutoSliding = false;
			return;
		}
		ObjectAnimator transAni = ObjectAnimator.ofFloat(view, "translation_X", from, to).setDuration(
				(long) Math.abs(to - from));
		transAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				if (view instanceof SlideItemWrapLayout) {
					SlideItemWrapLayout slideItemWrapLayout = (SlideItemWrapLayout) view;
					transX(slideItemWrapLayout, (Float) valueAnimator.getAnimatedValue());
				}
			}
		});
		transAni.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
				isAutoSliding = true;
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				isAutoSliding = false;
			}

			@Override
			public void onAnimationCancel(Animator animator) {

			}

			@Override
			public void onAnimationRepeat(Animator animator) {

			}
		});
		transAni.start();
	}

	private void transX(SlideItemWrapLayout slideView, float shouldScrollDistance) {
		setTranslationX(slideView.getFrontView(), shouldScrollDistance);
		setTranslationX(slideView.getRightBackView(), shouldScrollDistance);
		if (shouldScrollDistance < 0) {
			setExtraShow(slideView, true);
		} else {
			setExtraShow(slideView, false);
		}
	}

	public void reset() {
		mActivePointerId = ACTIVE_POINTER_ID_NONE;
		isAutoSliding = false;
		if (openedViewHolder != null) {
			SlideItemWrapLayout slideView = (SlideItemWrapLayout) openedViewHolder.itemView;
			transX(slideView, 0);
		}
		openedViewHolder = null;
	}
}

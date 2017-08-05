package com.cm.strawberry.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cm.strawberry.R;

/**
 * Created by zhouwei on 17-8-5.
 */

public class ErrorViewUtil {
        private static View notificationView;

        public static void showNotificationInView(String text, final ViewGroup parentView, final Runnable reloadRunnable,
                                                  boolean folate) {
            removeNotificationInView(parentView);
            parentView.setVisibility(View.VISIBLE);
            Context context = parentView.getContext();
            if (folate) {
                notificationView = LayoutInflater.from(context).inflate(R.layout.common_notice, null);
            } else {
                notificationView = LayoutInflater.from(context).inflate(R.layout.common_notic_marker, null);
            }
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            TextView textView = (TextView) notificationView.findViewById(R.id.common_notice_text);
            textView.setText(text);
            parentView.addView(notificationView, layoutParams);
            if (reloadRunnable != null) {
                ImageView imageView = (ImageView) notificationView.findViewById(R.id.common_notice_img);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeNotificationInView(parentView);
                        reloadRunnable.run();
                    }
                });
            }
        }

        public static void removeNotificationInView(ViewGroup parentView) {
//            if (parentView.getId() == R.id.notice_container) {
//                parentView.setVisibility(View.GONE);
//            }
            View view = parentView.findViewById(R.id.common_notice_container);
            if (view != null) {
                parentView.removeView(view);
            }
        }

        public static void showNotificationInViewForEmpty(String text, ViewGroup parentView, final Runnable reloadRunnable) {
            removeNotificationInView(parentView);
            parentView.setVisibility(View.VISIBLE);
            Context context = parentView.getContext();
            View notificationView = LayoutInflater.from(context).inflate(R.layout.empty_notice, null);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            TextView textView = (TextView) notificationView.findViewById(R.id.empty_notice_text);
            if (!StringUtil.isEmpty(text)) {
                textView.setText(text);
            }
            parentView.addView(notificationView, layoutParams);
            if (reloadRunnable != null) {
                ImageView imageView = (ImageView) notificationView.findViewById(R.id.empty_notice_img);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reloadRunnable.run();
                    }
                });
            }
        }

        public static void clearErrorView() {
            notificationView = null;
        }

        public static void setNotificationViewBackground() {
            if (notificationView != null) {
                notificationView.setBackgroundColor(Color.WHITE);
            }
        }

        public static void showNotificationInView(String text, View view, final ViewGroup parentView,
                                                  final Runnable reloadRunnable, boolean folate) {
            if (null == view) {
                showNotificationInView(text, parentView, reloadRunnable, folate);
            } else {
                removeNotificationInView(parentView);
                parentView.setVisibility(View.VISIBLE);
                notificationView = view;
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                TextView textView = (TextView) notificationView.findViewById(R.id.common_notice_text);
                textView.setText(text);
                parentView.addView(notificationView, layoutParams);
                if (reloadRunnable != null) {
                    ImageView imageView = (ImageView) notificationView.findViewById(R.id.common_notice_img);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeNotificationInView(parentView);
                            reloadRunnable.run();
                        }
                    });
                }
            }
        }

        public static void showNotificationInViewForEmpty(String text, View view, ViewGroup parentView,
                                                          final Runnable reloadRunnable) {
            if (null == view) {
                showNotificationInViewForEmpty(text, parentView, reloadRunnable);
            } else {
                removeNotificationInView(parentView);
                parentView.setVisibility(View.VISIBLE);
                notificationView = view;
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                TextView textView = (TextView) notificationView.findViewById(R.id.empty_notice_text);
                if (!StringUtil.isEmpty(text)) {
                    textView.setText(text);
                }
                parentView.addView(notificationView, layoutParams);
                if (reloadRunnable != null) {
                    ImageView imageView = (ImageView) notificationView.findViewById(R.id.empty_notice_img);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reloadRunnable.run();
                        }
                    });
                }
            }
        }
}

package com.cm.strawberry.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cm.strawberry.R;

/**
 * Created by zhouwei on 17-8-5.
 */

public class CommonNoticeView extends RelativeLayout {

    private ImageView image;

    private TextView text;

    public CommonNoticeView(Context context) {
        super(context);
        init(context, -1);
    }

    public CommonNoticeView(Context context, int bgResId) {
        super(context);
        init(context, -1);
        findViewById(R.id.common_notice_container).setBackgroundResource(bgResId);
    }

    public CommonNoticeView(Context context, int bgResId, int layoutId) {
        super(context);
        init(context, layoutId);
        if (bgResId != -1) {
            findViewById(R.id.common_notice_container).setBackgroundResource(bgResId);
        }
    }

    public CommonNoticeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, -1);
    }

    private void init(Context context, int layoutId) {
        if (layoutId != -1) {
            LayoutInflater.from(context).inflate(layoutId, this);
        } else {
            LayoutInflater.from(context).inflate(R.layout.common_notice, this);
        }
        image = (ImageView) findViewById(R.id.common_notice_img);
        text = (TextView) findViewById(R.id.common_notice_text);
    }

    public void setNotice(int stringId) {
        text.setText(getContext().getString(stringId));
    }

    public void setNotice(String notice) {
        text.setText(notice);
    }

    public void setImage(int resId) {
        image.setImageResource(resId);
    }

}
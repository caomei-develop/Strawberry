package com.cm.strawberry.view;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cm.strawberry.R;

/**
 * Created by zhouwei on 17-8-5.
 */

public class NewNavigationBar extends RelativeLayout{
    private Context context;

    public TextView title;

    private ImageButton leftBtn;

    private ImageButton rightBtn;

    public TextView rightText;

    private Paint paint;

    private boolean showBackground = false;

    private ImageView titleImg;

    private LinearLayout titleViewParentLinearLayout;

    public NewNavigationBar(Context context) {
        super(context);
        init(context);
    }

    public NewNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.new_navigation_bar, this);
        title = (TextView) findViewById(R.id.navi_bar_title);
        leftBtn = (ImageButton) findViewById(R.id.navi_bar_left_btn);
        rightBtn = (ImageButton) findViewById(R.id.navi_bar_right_btn);
        rightText = (TextView) findViewById(R.id.navi_bar_right_text);
        titleImg = (ImageView) findViewById(R.id.navi_bar_title_img);
        titleViewParentLinearLayout = (LinearLayout) findViewById(R.id.navi_bar_title_parent_view);
    }
    public void setTitle(CharSequence title) {
        this.title.setText(title);
    }

    public void setTitleColor(int color) {
        this.title.setTextColor(color);
    }

    public void setLeftButton(int resId) {
        this.leftBtn.setVisibility(View.VISIBLE);
        this.leftBtn.setImageResource(resId);
    }

    public void setLeftButton(OnClickListener l) {
        this.leftBtn.setVisibility(View.VISIBLE);
        this.leftBtn.setImageResource(R.mipmap.reading_back_icon);
        this.leftBtn.setOnClickListener(l);
    }

    public void setLeftButton(int resId, OnClickListener l) {
        this.leftBtn.setVisibility(View.VISIBLE);
        this.leftBtn.setImageResource(resId);
        this.leftBtn.setOnClickListener(l);
    }

    public void setRightButton(int resId, OnClickListener l) {
        this.rightBtn.setVisibility(View.VISIBLE);
        this.rightBtn.setImageResource(resId);
        this.rightBtn.setOnClickListener(l);
    }

    public ImageButton getRightBtn() {
        return rightBtn;
    }

    public void setRightButton(int resId) {
        this.rightBtn.setVisibility(View.VISIBLE);
        this.rightBtn.setImageResource(resId);
    }

    public void setTitleImage(int resId) {
        this.title.setVisibility(View.GONE);
        this.titleImg.setVisibility(View.VISIBLE);
        this.titleImg.setBackgroundResource(resId);
    }

    public void setTitleAlpha(float alpha) {
        ViewCompat.setAlpha(this.title, alpha);
    }

    public void setTitleImageAlpha(float alpha) {
        ViewCompat.setAlpha(this.titleImg, alpha);
    }

    public void setBackgroundLayout(int resId) {
        this.leftBtn.setBackgroundResource(resId);
        this.setBackgroundResource(resId);
    }

    public void setRightButtonVisible(boolean visible) {
        if (visible) {
            this.rightBtn.setVisibility(View.VISIBLE);
        } else {
            this.rightBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void setRightText(OnClickListener l) {
        this.rightText.setOnClickListener(l);
    }

    public void setRightText(String value, OnClickListener l) {
        this.rightText.setOnClickListener(l);
        this.rightText.setText(value);
    }

    public void setBackgroundEnabl(boolean enabled) {
        showBackground = enabled;
        invalidate();
    }

    public void setRightTextVisible(boolean visible) {
        if (visible) {
            this.rightText.setVisibility(View.VISIBLE);
        } else {
            this.rightText.setVisibility(View.INVISIBLE);
        }
    }

    public void setRightTextBackground(boolean visible) {
        if (visible) {
            this.rightText.setTextColor(getResources().getColor(R.color.white));
        } else {
            this.rightText.setTextColor(getResources().getColor(R.color.dark_white));
        }
    }

    public void setAlphaForLeftBtn(float alpha) {
        if (leftBtn == null) {
            leftBtn = (ImageButton) findViewById(R.id.navi_bar_left_btn);
        }
        if (leftBtn != null) {
            ViewCompat.setAlpha(leftBtn, alpha);
        }
    }

    public void setRightText(CharSequence str) {
        this.rightText.setText(str);
    }

    public void setCustomTitleView(View view) {
        titleViewParentLinearLayout.removeAllViews();
        titleViewParentLinearLayout.addView(view);
    }
}

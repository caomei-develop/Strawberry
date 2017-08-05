package com.cm.strawberry.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
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

public class NavigationBar extends LinearLayout {
    public TextView title;
    private ImageButton leftBtn;

    private ImageButton rightBtn, rightBtnHistory;

    public TextView rightText;

    private Paint paint;

    private boolean showBackground = true;

    private ImageView titleImg;

    private LinearLayout titleViewParentLinearLayout;

    private RelativeLayout backgroundLayout;
    private Context context;

    public NavigationBar(Context context) {
        super(context);
        init(context);
    }

    public NavigationBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.navigation_bar, this);
        title = findViewById(R.id.navi_bar_title);
        leftBtn = findViewById(R.id.navi_bar_left_btn);
        rightBtn = findViewById(R.id.navi_bar_right_btn);
        rightBtnHistory = findViewById(R.id.navi_bar_history_supermarket);
        rightText = findViewById(R.id.navi_bar_right_text);
        titleImg = findViewById(R.id.navi_bar_title_img);
        backgroundLayout = findViewById(R.id.navi_bar_background);
        titleViewParentLinearLayout = findViewById(R.id.navi_bar_title_parent_view);
        paint = new Paint();
    }

    public void setTitle(CharSequence title) {
        this.title.setText(title);
    }

    public void setTitleColor(int resId) {
        this.title.setTextColor(resId);
    }

    public void setLeftButton(int resId) {
        this.leftBtn.setVisibility(View.VISIBLE);
        this.leftBtn.setImageResource(resId);
    }

    public void setLeftButton(OnClickListener l) {
        this.leftBtn.setVisibility(View.VISIBLE);
        this.leftBtn.setImageResource(R.mipmap.navi_bar_back_img_white);
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

    public void setRightButtonHistory(OnClickListener l) {
        this.rightBtnHistory.setVisibility(View.VISIBLE);
        this.rightBtnHistory.setOnClickListener(l);
    }

    public ImageButton getRightBtn() {
        return rightBtn;
    }

    public void setRightButton(int resid) {
        this.rightBtn.setVisibility(View.VISIBLE);
        this.rightBtn.setImageResource(resid);
    }

    public void setTitleImage(int resid) {
        this.title.setVisibility(View.GONE);
        this.titleImg.setVisibility(View.VISIBLE);
        this.titleImg.setBackgroundResource(resid);
    }

    public void setBackgroundLayout(int resid) {
        this.leftBtn.setBackgroundResource(resid);
        this.backgroundLayout.setBackgroundResource(resid);
    }

    public void setRightButtonVisible(boolean visible) {
        if (visible) {
            this.rightBtn.setVisibility(View.VISIBLE);
        } else {
            this.rightBtn.setVisibility(View.INVISIBLE);
        }
    }

    public void hideTitle() {
        titleViewParentLinearLayout.setVisibility(GONE);
    }

    public void setAlphaForLeftBtn(float alpha) {
        if (leftBtn == null) {
            leftBtn = (ImageButton) findViewById(R.id.navi_bar_left_btn);
        }
        if (leftBtn != null) {
            ViewCompat.setAlpha(leftBtn, alpha);
        }
    }

    public void setAlphaForRightBtn(float alpha) {
        if (rightBtn == null) {
            rightBtn = (ImageButton) findViewById(R.id.navi_bar_right_btn);
        }
        if (rightBtn != null) {
            ViewCompat.setAlpha(rightBtn, alpha);
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

    public void setRightText(CharSequence str) {
        this.rightText.setText(str);
    }

    public void setCustomTitleView(View view) {
        titleViewParentLinearLayout.removeAllViews();
        titleViewParentLinearLayout.addView(view);
    }
}

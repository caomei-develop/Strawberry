package com.cm.strawberry.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cm.strawberry.R;
import com.cm.strawberry.app.StrawberryApplication;
import com.cm.strawberry.callback.WaitableActivity;
import com.cm.strawberry.util.ActivityManager;
import com.cm.strawberry.util.ErrorViewUtil;
import com.cm.strawberry.util.StatusBarUtil;
import com.cm.strawberry.util.Utils;
import com.cm.strawberry.view.CommonNoticeView;
import com.cm.strawberry.view.InputMethodManagerUtil;
import com.cm.strawberry.view.NewNavigationBar;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zhouwei on 17-7-31.
 */

public class BaseActivity extends AppCompatActivity {
    private Dialog waitDialog;

    private OnBaseActivityCallbackListener callback;

    private boolean bDestroyed = false;

    public Toolbar mToolbar;

    public LinearLayout rightLayout;

    public NewNavigationBar navi;

    private LayoutInflater inflater;
    private TextView textView;
    private ImageView image;
    public LinearLayout layoutToolbar;

    protected boolean mStateSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(this);
        if (isFinalTranslucentStatusBar()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        ActivityManager.getInstance().addActivity(this);
        if (callback != null) {
            callback.onCreate(savedInstanceState);
        }
    }

    @Override
    public void setContentView(int resId) {
        if (isDefaultToolBar()) {
            super.setContentView(R.layout.activity_app_compat_base);
            FrameLayout container = (FrameLayout) findViewById(R.id.content_container);
            inflater.inflate(resId, container, true);
        } else {
            super.setContentView(resId);
        }
        checkToolBarAndNavigationBar();
    }

    private void checkToolBarAndNavigationBar() {
        if (hasToolBar() && hasNavigationBar()) {
            initToolBar();
            initNavigationBar();
            addNavigationBarToToolBar();
        } else if (hasToolBar()) {
            initToolBar();
        } else if (hasNavigationBar()) {
            initNavigationBar();
            addNavigationBarToContent();
        }
        if (isBlackStatusBar()) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.status_bar_black));
        }
    }

    private void initNavigationBar() {
        navi = new NewNavigationBar(this);
        String title;
        try {
            title = getPackageManager().getActivityInfo(this.getComponentName(), 0).loadLabel(getPackageManager())
                    .toString();
            navi.setTitle(title);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (hasLeftBarButton()) {
            navi.setLeftButton(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    private int getActionBarSize() {
        try {
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }
        } catch (Throwable e) {
        }
        return Utils.dip2px(StrawberryApplication.strawberryApp, 48);
    }

    protected void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setVisibility(View.VISIBLE);
        layoutToolbar = (LinearLayout) findViewById(R.id.toolbar_title_layout);
        rightLayout = (LinearLayout) findViewById(R.id.right_layout);
        image = (ImageView) findViewById(R.id.toolbar_icon);
        image.setImageResource(R.mipmap.reading_back_icon);
        textView = (TextView) findViewById(R.id.toolbar_title);
        String title;
        try {
            title = getPackageManager().getActivityInfo(this.getComponentName(), 0).loadLabel(getPackageManager())
                    .toString();
            textView.setText(title);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        layoutToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setNavigationIcon(null);
        setSupportActionBar(mToolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    public void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    protected int getModuleId() {
        return 0;
    }

    public void setCallback(OnBaseActivityCallbackListener callback) {
        this.callback = callback;
    }

    /**
     * show common notice with default text: network error
     */
    protected void showNotice() {
        CommonNoticeView notice = new CommonNoticeView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        ((FrameLayout) findViewById(android.R.id.content)).addView(notice, 1, lp);
    }

    /**
     * show common notice with special text
     */
    protected void showNotice(String text) {
        CommonNoticeView notice = new CommonNoticeView(this);
        notice.setNotice(text);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        ((FrameLayout) findViewById(android.R.id.content)).addView(notice, 1, lp);
    }

    @Override
    protected void onDestroy() {
        InputMethodManagerUtil.fixInputMethodManagerLeak(this);
        ActivityManager.getInstance().remove(this);
        if (callback != null) {
            callback.onDestroy();
        }
        if (waitDialog != null && waitDialog.isShowing()) {
            try {
                waitDialog.cancel();
            } catch (Exception e) {
            }
            waitDialog = null;
        }
        ErrorViewUtil.clearErrorView();
        super.onDestroy();
        bDestroyed = true;
    }

    public boolean isActivityDestroyed() {
        return bDestroyed;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mStateSaved = true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStateSaved = false;
    }

    public boolean isSaveInstanceState() {
        return mStateSaved;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        try {
            super.onPause();
            MobclickAgent.onPause(this);
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        try {
            super.onBackPressed();
        } catch (Exception e) {
            try {
                this.finish();
            } catch (Exception e1) {
            }
        }
    }

    private boolean createWaitDialog(String message) {
        if (isActivityDestroyed()) {
            return false;
        }
        if (waitDialog != null && waitDialog.isShowing()) {
            ((TextView) waitDialog.findViewById(R.id.tv_wait_message)).setText(message);
            return true;
        }
        waitDialog = new Dialog(this, R.style.dialogProgress);
        waitDialog.setContentView(R.layout.dialog_progress);
        WindowManager.LayoutParams lp = waitDialog.getWindow().getAttributes();
        lp.width = Utils.getDeviceWidth(this) - Utils.dip2px(this, 50);
        waitDialog.getWindow().setAttributes(lp);
        ((TextView) waitDialog.findViewById(R.id.tv_wait_message)).setText(message);
        return true;
    }

    public void showWaitDialog(String message) {
        if (createWaitDialog(message)) {
            waitDialog.setCancelable(false);
            waitDialog.show();
        }
    }

    public void dismissWaitDialog() {
        if (!this.isActivityDestroyed() && waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
        }
        waitDialog = null;
    }

    public void showWaitDialog(String message, boolean isCancelable, DialogInterface.OnCancelListener listener) {
        if (createWaitDialog(message)){
            waitDialog.setCanceledOnTouchOutside(false);
            waitDialog.setCancelable(isCancelable);
            waitDialog.setOnCancelListener(listener);
            waitDialog.show();
        }

    }
    public void showWaitDialog(String message, final Runnable postCancelRunnable) {
        try {
            showWaitDialog(message, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (postCancelRunnable != null) {
                        postCancelRunnable.run();
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    public void showWaitDialog(int stringId, final Runnable postCancelRunnable) {
        try {
            showWaitDialog(getResources().getString(stringId), true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (postCancelRunnable != null) {
                        postCancelRunnable.run();
                    }
                }
            });
        } catch (Exception e) {
        }
    }
    public void setRightButton(String str, View.OnClickListener listener) {
        if (hasNavigationBar()) {
            navi.setRightText(str, listener);
        }
    }

    public void setLeftButton(int resId, View.OnClickListener l) {
        if (hasNavigationBar()) {
            navi.setLeftButton(resId, l);
        }
    }

    public void setLeftButton(View.OnClickListener l) {
        if (hasNavigationBar()) {
            navi.setLeftButton(l);
        }
    }

    public void setNavTitleText(int resId) {
        if (hasNavigationBar()) {
            navi.setTitle(getResources().getString(resId));
        }
    }

    public void setNavTitleText(String title) {
        if (hasNavigationBar()) {
            navi.setTitle(title);
        }
    }

    public void setNavTitleImage(int resId) {
        if (hasNavigationBar()) {
            navi.setTitleImage(resId);
        }
    }

    public void setNavTitleAlpha(float alpha) {
        if (hasNavigationBar()) {
            navi.setTitleAlpha(alpha);
        }
    }

    public void setNavTitleColor(int color) {
        if (hasNavigationBar()) {
            navi.setTitleColor(color);
        }
    }

    public void setNavTitleImageAlpha(float alpha) {
        if (hasNavigationBar()) {
            navi.setTitleImageAlpha(alpha);
        }
    }

    public void fetchToolbarTitle(String title) {
        textView.setText(title);
    }

    public void fetchToolbarIcon(int resId) {
        image.setImageResource(resId);
    }

    public void setNavBackground(int resId) {
        if (hasNavigationBarBackground()) {
            navi.setBackgroundLayout(resId);
        }
    }

    public void setRightTextVisible(boolean visible) {
        navi.setRightTextVisible(visible);
    }

    public void setRightTextColor(boolean visible) {
        navi.setRightTextBackground(visible);
    }

    public void addNavigationBarToToolBar() {
        mToolbar.setLogo(null);
        mToolbar.setNavigationIcon(null);
        mToolbar.setContentInsetsAbsolute(0, 0);
        mToolbar.setContentInsetsRelative(0, 0);
        mToolbar.addView(navi);
    }

    public void addNavigationBarToContent() {
        ((FrameLayout) findViewById(android.R.id.content)).addView(navi, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getActionBarSize()));
    }

    public static interface OnBaseActivityCallbackListener {

        public void onCreate(Bundle savedInstanceState);

        public void onDestroy();
    }

    public boolean hasToolBar() {
        return false;
    }

    public boolean isDefaultToolBar() {
        return false;
    }

    public boolean hasNavigationBar() {
        return false;
    }

    public boolean hasLeftBarButton() {
        return true;
    }

    public boolean isTranslucentStatusBar() {
        return false;
    }

    public boolean hasNavigationBarBackground() {
        return false;
    }

    public boolean hasToolBarTitle() {
        return true;
    }


    protected final boolean isFinalTranslucentStatusBar() {
        return isTranslucentStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public void noAnimationFinish() {
        finish();
        overridePendingTransition(0, R.anim.anim_exit);
    }

    public boolean isBlackStatusBar() {
        return true;
    }
}

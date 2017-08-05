package com.cm.strawberry.base;


import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.cm.strawberry.callback.WaitableActivity;
import com.cm.strawberry.view.NavigationBar;

public class BaseFragment extends Fragment {
    public NavigationBar navi;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFinalTranslucentStatusBar()) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (hasNavigationBar()) {
            navi = new NavigationBar(getActivity());
            String title;
            try {
                title = getActivity().getPackageManager().getActivityInfo(getActivity().getComponentName(), 0)
                        .loadLabel(getActivity().getPackageManager()).toString();
                navi.setTitle(title);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public void setNavTitle(String title) {
        if (hasNavigationBar()) {
            navi.setTitle(title);
        }
    }

    private boolean isViewCreated = false;

    @Override
    public final void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isViewCreated && getView() != null) {
            isViewCreated = true;
        }
        if (isViewCreated) {
            setVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (needSetVisibleHintManually()) {
            setUserVisibleHint(getUserVisibleHint());
        }
    }

    public boolean needSetVisibleHintManually() {
        return true;
    }

    protected void setVisibleHint(boolean isVisiToUser) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
    }

    protected boolean isViewCreated() {
        return isViewCreated;
    }

    @Override
    public void onDestroy() {
        // ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasNavigationBar()) {

            if (getView() instanceof ScrollView) {
                ((ViewGroup) ((ViewGroup) getView()).getChildAt(0)).removeView(navi);
                ((ViewGroup) ((ViewGroup) getView()).getChildAt(0)).addView(navi, 0, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            } else {
                ((ViewGroup) getView()).removeView(navi);
                ((ViewGroup) getView()).addView(navi, 0, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    @Override
    public void onPause() {
        try {
            super.onPause();
        } catch (Exception e) {
        }
    }

    public void setRightButton(String str, View.OnClickListener listener) {
        if (hasNavigationBar()) {
            navi.setRightText(str, listener);
        }
    }

    public void showWaitDialog(String message) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((WaitableActivity) getActivity()).showWaitDialog(message);
        }
    }

    public void dismissWaitDialog() {
        if (getActivity() != null) {
            ((WaitableActivity) getActivity()).dismissWaitDialog();
        }
    }

    public void showWaitDialog(String message, boolean isCancelable, DialogInterface.OnCancelListener listener) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((WaitableActivity) getActivity()).showWaitDialog(message, isCancelable, listener);
        }
    }

    public void showWaitDialog(int stringId, boolean isCancelable, DialogInterface.OnCancelListener listener) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((WaitableActivity) getActivity()).showWaitDialog(stringId, isCancelable, listener);
        }
    }

    public void showWaitDialog(String message, final Runnable postCancelRunnable) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((WaitableActivity) getActivity()).showWaitDialog(message, postCancelRunnable);
        }
    }

    public void showWaitDialog(int stringId, final Runnable postCancelRunnable) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            ((WaitableActivity) getActivity()).showWaitDialog(stringId, postCancelRunnable);
        }
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

    protected final boolean isFinalTranslucentStatusBar() {
        return isTranslucentStatusBar() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public boolean hasNavigatBackground() {
        return false;
    }
}

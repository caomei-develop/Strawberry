package com.cm.strawberry.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.cm.strawberry.R;
import com.cm.strawberry.util.DimenUtil;

/**
 * Created by zw on 2017/8/19.
 */

public abstract class CmDialog extends DialogFragment {
    protected Dialog dialog;

    private int mStyle = R.style.BottomDialog;

    protected View mContentView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = createDialog();
        setGravityBottom();
        mContentView = getContentView();
        dialog.setContentView(mContentView);
        initDialog(dialog);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        return dialog;
    }

    protected void setGravityBottom() {
        setDialogGravity(Gravity.BOTTOM);
    }

    protected void setGravityCenter() {
        setDialogGravity(Gravity.CENTER);
    }

    private View getContentView() {
        return inflateView(getLayoutId());
    }

    protected abstract int getLayoutId();

    /**
     * 初始化dialog
     */
    protected abstract void initDialog(Dialog dialog);

    /**
     * 设置dialog的重�?
     */
    protected void setDialogGravity(int gravity) {
        dialog.getWindow().setGravity(gravity);
    }

    /**
     * dialog的样式，主要是动画，返回0的话，将默认没有动画效果
     */
    protected int getStyle() {
        return mStyle;
    }

    private Dialog createDialog() {
        if (dialog == null) {
            int style = getStyle();
            if (style != 0) {
                dialog = new Dialog(getActivity(), style);
            } else {
                dialog = new Dialog(getActivity());
            }
        }
        return dialog;
    }

    protected View view;

    /**
     * 根据布局文件加载dialog的contentView
     */
    protected View inflateView(int id) {
        view = LayoutInflater.from(getActivity()).inflate(id, null);
        view.setMinimumWidth(getMinimumWidth() - getPadding());
        return view;
    }

    protected int getMinimumWidth() {
        return DimenUtil.getWindowWidth(getActivity());
    }

    protected int getPadding() {
        return DimenUtil.dip2px(getActivity(), 60);
    }

    protected View findViewById(int id) {
        return view.findViewById(id);
    }

    public void show(FragmentActivity context) {
        try {
            show(context.getSupportFragmentManager(), "");
        } catch (Exception e){
            //弹出对话框时出现错误
            Toast.makeText(context,"弹出对话框时出现错误",Toast.LENGTH_LONG).show();
        }
    }

    private OnDismissListener onDismissListener;

    public static class OnDismissListener {
        public void onDismiss(){};
        public void onCancel(){};
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }

    private boolean canceledOnTouchOutside = true;

    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (onDismissListener != null) {
            onDismissListener.onCancel();
        }
    }

}

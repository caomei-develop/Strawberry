package com.cm.strawberry.callback;

import android.content.DialogInterface;

/**
 * Created by zhouwei on 17-8-5.
 */

public interface WaitableActivity {
    public void showWaitDialog(int stringId, boolean isCancelable, DialogInterface.OnCancelListener listener);

    public void showWaitDialog(String message, boolean isCancelable, DialogInterface.OnCancelListener listener);

    public void showWaitDialog(String message);

    public void dismissWaitDialog();

    public void showWaitDialog(String message, final Runnable postCancelRunnable);

    public void showWaitDialog(int stringId, final Runnable postCancelRunnable);
}

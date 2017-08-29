package com.cm.strawberry.view;

import android.app.Dialog;

/**
 * Created by zw on 2017/8/19.
 */

public class MessageDialog extends CmDialog {
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initDialog(Dialog dialog) {
        setGravityCenter();
    }

    @Override
    protected int getStyle() {
        return 0;
    }

}

package com.cm.strawberry.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.cm.strawberry.util.ActivityManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwei on 17-7-31.
 */

public class BaseActivity extends AppCompatActivity{
    protected BaseActivity context;
    private List<String> actions = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        ActivityManager.getInstance().addActivity(this);
        context =this;
    }

    protected int getResourceColor(int id) {
        return getResources().getColor(id);
    }
}

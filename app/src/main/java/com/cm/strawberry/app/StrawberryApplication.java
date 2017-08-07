package com.cm.strawberry.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhouwei on 17-7-27.
 */

public class StrawberryApplication extends Application{
    public static StrawberryApplication strawberryApp;

    public static Context getContext(){
        return strawberryApp;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        strawberryApp = this;
    }
}

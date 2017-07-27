package com.cm.strawberry.callback;

/**
 * Created by zhouwei on 17-7-20.
 */

public abstract class Callback<T> {
    public void onSuccess(T model) {
    }

    public void onFailure( String msg) {
    }

}

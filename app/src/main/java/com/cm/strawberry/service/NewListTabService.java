package com.cm.strawberry.service;

import com.cm.strawberry.api.Api;
import com.cm.strawberry.bean.NewListTab;
import com.cm.strawberry.bean.PerpetualCalendar;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.http.HttpRequest;
import com.cm.strawberry.http.helper.RetrofitHelper;

import rx.Observable;

/**
 * Created by zhouwei on 17-7-28.
 */

public class NewListTabService {
    public void getNewListTabService( String key ,final Callback<NewListTab> callback) {
        Observable<NewListTab> newListTabObservable= RetrofitHelper.getService(Api.BASE_MOB_URL,Api.class)
                .newListTab(key);
        HttpRequest.requestNetByPost(newListTabObservable, new HttpRequest.OnResultListener<NewListTab>() {
            @Override
            public void onSuccess(NewListTab newListTab) {
                if (newListTab != null){
                    if (callback!=null){
                        callback.onSuccess(newListTab);
                    }
                }
            }

            @Override
            public void onError(Throwable error, String msg) {
                if (msg != null){
                    if (callback!=null){
                        callback.onFailure(msg);
                    }
                }
            }
        });

    }

}

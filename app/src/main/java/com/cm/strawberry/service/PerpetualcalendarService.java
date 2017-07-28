package com.cm.strawberry.service;

import com.cm.strawberry.api.Api;
import com.cm.strawberry.bean.PerpetualCalendar;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.http.HttpRequest;
import com.cm.strawberry.http.helper.RetrofitHelper;

import rx.Observable;

/**
 * Created by zhouwei on 17-7-27.
 */

public class PerpetualcalendarService {
    public void getPerpetualcalendarService(String date, String key ,final Callback<PerpetualCalendar> callback) {
        Observable<PerpetualCalendar> perpetualcalendarObservable= RetrofitHelper.getService(Api.BASE_MOB_URL,Api.class)
                .pCalendar(key,date);
        HttpRequest.requestNetByPost(perpetualcalendarObservable, new HttpRequest.OnResultListener<PerpetualCalendar>() {
            @Override
            public void onSuccess(PerpetualCalendar perpetualcalendar) {
                if (perpetualcalendar != null){
                    if (callback!=null){
                        callback.onSuccess(perpetualcalendar);
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

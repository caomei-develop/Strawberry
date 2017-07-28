package com.cm.strawberry.service;

import com.cm.strawberry.api.Api;
import com.cm.strawberry.bean.WeatherForecast;
import com.cm.strawberry.bean.WxAiccle;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.http.HttpRequest;
import com.cm.strawberry.http.helper.RetrofitHelper;

import rx.Observable;

/**
 * Created by zhouwei on 17-7-28.
 */

public class WxAcaleService {
    public void getWXAcaleService(int page, String cid, String key, int size, final Callback<WxAiccle>callback){
        Observable<WxAiccle> wxAiccleObservable = RetrofitHelper.getService(Api.BASE_MOB_URL, Api.class)
                .wxarticle(page,cid,key,size);
        HttpRequest.requestNetByGet(wxAiccleObservable, new HttpRequest.OnResultListener<WxAiccle>() {
            @Override
            public void onSuccess(WxAiccle wxAiccle) {
                if (wxAiccle != null){
                    if (callback != null){
                        callback.onSuccess(wxAiccle);
                    }
                }
            }

            @Override
            public void onError(Throwable error, String msg) {
                if (msg != null) {
                    if (callback != null) {
                        callback.onFailure(msg);
                    }
                }
            }
        });

    }
}

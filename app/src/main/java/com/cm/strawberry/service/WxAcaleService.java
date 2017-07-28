package com.cm.strawberry.service;

import com.cm.strawberry.api.Api;
import com.cm.strawberry.bean.WeatherForecast;
import com.cm.strawberry.bean.WxAiccle;
import com.cm.strawberry.bean.WxSearch;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.http.HttpRequest;
import com.cm.strawberry.http.helper.RetrofitHelper;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by zhouwei on 17-7-28.
 */

public class WxAcaleService {
    public void getWXAcaleService(int page, String cid, String key, int size, final Callback<WxSearch>callback){
        HashMap<String , Object> map = new HashMap<>();
        map.put("page",page);
        map.put("cid",cid);
        map.put("key",key);
        map.put("size",size);
        Observable<WxSearch> wxSearchObservable = RetrofitHelper.getService(Api.BASE_MOB_URL, Api.class)
                .wxarticle(map);
        HttpRequest.requestNetByGet(wxSearchObservable, new HttpRequest.OnResultListener<WxSearch>() {
            @Override
            public void onSuccess(WxSearch wxSearch) {
                if (wxSearch != null){
                    if (callback != null){
                        callback.onSuccess(wxSearch);
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

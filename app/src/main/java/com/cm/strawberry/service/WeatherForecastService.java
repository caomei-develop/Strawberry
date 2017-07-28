package com.cm.strawberry.service;

import com.cm.strawberry.api.Api;
import com.cm.strawberry.bean.WeatherForecast;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.http.HttpRequest;
import com.cm.strawberry.http.helper.RetrofitHelper;

import rx.Observable;

/**
 * Created by zhouwei on 17-7-27.
 */

public class WeatherForecastService {
    public void getWeatherForecastService(String province, String key, String city,final Callback<WeatherForecast> callback) {
        Observable<WeatherForecast> weatherForecastObservable = RetrofitHelper.getService(Api.BASE_MOB_URL, Api.class)
                .wForecast(province,key,city);
        HttpRequest.requestNetByGet(weatherForecastObservable, new HttpRequest.OnResultListener<WeatherForecast>() {

            @Override
            public void onSuccess(WeatherForecast weatherForecast) {
                if (weatherForecast != null){
                    if (callback != null){
                        callback.onSuccess(weatherForecast);
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

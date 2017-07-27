package com.cm.strawberry.api;



import com.cm.strawberry.base.PerpetualCalendar;
import com.cm.strawberry.base.WeatherForecast;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zw on 2017/4/10.
 */
public interface Api {
    String BASE_MOB_URL = "http://apicloud.mob.com";
    String APP_KEY = "175d559eca020";
    @GET("v1/weather/query?")//获取天气预报
    Observable<WeatherForecast>wForecast(@Query("province") String province, @Query("key") String key, @Query("city") String city);
    @GET("appstore/calendar/day?")//获取万年历
    Observable<PerpetualCalendar>pCalendar(@Query("key") String key, @Query("date") String date);
}

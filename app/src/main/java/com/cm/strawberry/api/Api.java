package com.cm.strawberry.api;



import com.cm.strawberry.bean.NewListTab;
import com.cm.strawberry.bean.PerpetualCalendar;
import com.cm.strawberry.bean.WeatherForecast;
import com.cm.strawberry.bean.WxSearch;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by zw on 2017/4/10.
 */
public interface Api {
    String BASE_MOB_URL = "http://apicloud.mob.com";
    String APP_KEY = "175d559eca020";
    String MEI_ZI_URL = "http://www.keaitupian.com/girl/";
    @GET("v1/weather/query?")
    Observable<WeatherForecast>wForecast(@Query("province") String province, @Query("key") String key, @Query("city") String city);
    @GET("appstore/calendar/day?")
    Observable<PerpetualCalendar>pCalendar(@Query("key") String key, @Query("date") String date);
    @GET("v1/cook/category/query?")
    Observable<NewListTab> newListTab(@Query("key") String key);
    @GET("wx/article/search?")
    Observable<WxSearch> wxarticle(@Query("page") int page,@Query("cid") String cid,@Query("key") String key,@Query("size") int size);
}

package com.cm.strawberry.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cm.strawberry.R;
import com.cm.strawberry.api.Api;
import com.cm.strawberry.base.WeatherForecast;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.service.WeatherForecastService;
import com.cm.strawberry.util.PermissionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    /**
     * 高德定位
     */
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String province;//省份
    private String city;//城市

    /**
     * 天气service
     */
    private WeatherForecastService weatherForecastService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        /**
         * 添加权限/启动定位
         */
        PermissionManager.checkPermission(this, PermissionManager.GPS);
        initLocation();
        locationClient.startLocation();

    }

    private void initLocation() {
        locationClient = new AMapLocationClient(this);
        locationOption = new AMapLocationClientOption();
        locationOption.setNeedAddress(true);
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setInterval(2000);
        locationClient.setLocationOption(locationOption);
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                province = location.getProvince();
                city = location.getCity();
                province = province.replace("市", "");
                city = city.replace("市", "");
                /**
                 * 回调请求天气接口
                 */
                getWeatherForecast(province, city);
            } else {
                Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 天气回调
     *
     * @param province
     * @param city
     */
    List<WeatherForecast.ResultBean> resultBean = new ArrayList<>();
    private void getWeatherForecast(String province, String city) {
        weatherForecastService = new WeatherForecastService();
        weatherForecastService.getWeatherForecastService(province, Api.APP_KEY, city, new Callback<WeatherForecast>() {
            @Override
            public void onSuccess(WeatherForecast model) {
                super.onSuccess(model);
                if (model != null) {
                    resultBean = model.getResult();
                }
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        return super.onPrepareOptionsMenu(menu);
//    }

    /**
     * 获取右侧mune
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (resultBean != null){
            if (resultBean.get(0).getWeather() != null) {
                String weather = resultBean.get(0).getWeather();
                if (weather.equals("多云")) {
                    menu.findItem(R.id.weather_forecast).setIcon(R.mipmap.sunny_day);
                } else if (weather.equals("雨")) {
                    menu.findItem(R.id.weather_forecast).setIcon(R.mipmap.rainy_day);
                } else if (weather.equals("雪")) {
                    menu.findItem(R.id.weather_forecast).setIcon(R.mipmap.snowing_day);
                }
            }
        }
        return true;
    }

    /**
     * 右侧mune事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.weather_forecast:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

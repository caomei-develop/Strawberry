package com.cm.strawberry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cm.strawberry.R;
import com.cm.strawberry.adapter.NewListAdapter;
import com.cm.strawberry.api.Api;
import com.cm.strawberry.base.BaseActivity;
import com.cm.strawberry.bean.WeatherForecast;
import com.cm.strawberry.bean.WxAiccle;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.model.TabListModel;
import com.cm.strawberry.service.WeatherForecastService;
import com.cm.strawberry.ui.activity.SwitchActivity;
import com.cm.strawberry.ui.fragment.NewListFragment;
import com.cm.strawberry.util.ActivityUtil;
import com.cm.strawberry.util.PermissionManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    /**
     * 获取tab标签
     */
    private List<Fragment> fragments = new ArrayList<>();
    private NewListAdapter newListAdapter;
    private List<WxAiccle> wxAiccles;
    private TabListModel tabListModel;
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
    private WeatherForecast weatherForecast = new WeatherForecast();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        /**
         * 添加权限/启动定位
         */
        init();
        getNewListTab();
        PermissionManager.checkPermission(this, PermissionManager.GPS);
        initLocation();
        locationClient.startLocation();
    }

    private void init() {
        wxAiccles = new ArrayList<>();
        tabListModel = new TabListModel(this);
        newListAdapter = new NewListAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(newListAdapter);
        tabLayout.setupWithViewPager(viewPager);
        wxAiccles = tabListModel.getWxAiccles();
    }

    /**
     * 启动定位
     */
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
                toolbar.setTitle(city);
                setSupportActionBar(toolbar);
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
     * tab
     */
    private void setupViewPager() {
        if (wxAiccles != null) {
            for (int i = 0; i < wxAiccles.size(); i++) {
                tabLayout.addTab(tabLayout.newTab());
                fragments.add(NewListFragment.newInstance(i, wxAiccles.get(i).getCid()));
            }
        }
    }

    /**
     * 获取tab
     */
    private void getNewListTab() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (viewPager != null) {
                    setupViewPager();
                }
            }
        });
        newListAdapter.setData(wxAiccles);
        newListAdapter.notifyDataSetChanged();
    }

    /**
     * 天气回调
     *
     * @param province
     * @param city
     */
    List<WeatherForecast.ResultBean> resultBean = new ArrayList<>();

    private void getWeatherForecast(String province, String city) {
        weatherForecastService = new WeatherForecastService();
        weatherForecastService.getWeatherForecastService(province, Api.APP_KEY, city,
                new Callback<WeatherForecast>() {
                    @Override
                    public void onSuccess(WeatherForecast model) {
                        super.onSuccess(model);
                        if (model != null) {
                            weatherForecast = model;
                            if (weatherForecast.getResult().get(0).getWeather().equals("多云") ||
                                    weatherForecast.getResult().get(0).getWeather().equals("晴")) {
                                fab.setImageResource(R.mipmap.sunny_day);
                            } else if (weatherForecast.getResult().get(0).getWeather().equals("雨")) {
                                fab.setImageResource(R.mipmap.rainy_day);
                            } else if (weatherForecast.getResult().get(0).getWeather().equals("雪")) {
                                fab.setImageResource(R.mipmap.snowing_day);
                            }
                            resultBean = model.getResult();
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        super.onFailure(msg);
                    }
                });
    }

    /**
     * 获取右侧mune
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 右侧mune事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.weather_switch:
                ActivityUtil.toActivity(this, SwitchActivity.class);
                break;
            case R.id.weather_forecast:
                /**
                 * 二维码操作
                 */
                startActivity(new Intent(this, QrcodeActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

package com.cm.strawberry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.cm.strawberry.bean.NewListTab;
import com.cm.strawberry.bean.WeatherForecast;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.service.NewListTabService;
import com.cm.strawberry.service.WeatherForecastService;
import com.cm.strawberry.bean.WxAiccle;
import com.cm.strawberry.ui.fragment.NewListFragment;
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
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    /**
     *
     */
    /**
     * 获取tab标签
     */
    private List<Fragment> fragments = new ArrayList<>();
    private NewListTabService newListTabService = new NewListTabService();
    private NewListAdapter newListAdapter;
    private FragmentManager fragmentManager;
    private List<NewListTab>newListTab;
    private List<WxAiccle>wxAiccles;
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
        newListAdapter = new NewListAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(newListAdapter);
        tabLayout.setupWithViewPager(viewPager);
        fragmentManager = getSupportFragmentManager();
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
//        newListTab = new ArrayList<>();
        wxAiccles = new ArrayList<>();
//        newListTab.add(new NewListTab("0010001030","鲁菜","0010001004"));
//        newListTab.add(new NewListTab("0010001031","川菜","0010001004"));
//        newListTab.add(new NewListTab("0010001032","粤菜","0010001004"));
//        newListTab.add(new NewListTab("0010001033","闽菜","0010001004"));
//        newListTab.add(new NewListTab("0010001034","浙菜","0010001004"));
//        newListTab.add(new NewListTab("0010001035","湘菜","0010001004"));
//        newListTab.add(new NewListTab("0010001036","上海菜","0010001004"));
//        newListTab.add(new NewListTab("0010001037","徽菜","0010001004"));
//        newListTab.add(new NewListTab("0010001038","京菜","0010001004"));
//        newListTab.add(new NewListTab("0010001039","东北菜","0010001004"));
//        newListTab.add(new NewListTab("0010001040","西北菜","0010001004"));
//        newListTab.add(new NewListTab("0010001041","客家菜","0010001004"));
//        newListTab.add(new NewListTab("0010001042","台湾美食","0010001004"));
//        newListTab.add(new NewListTab("0010001043","泰国菜","0010001004"));
//        newListTab.add(new NewListTab("0010001044","日本料理","0010001004"));
//        newListTab.add(new NewListTab("0010001045","韩国料理","0010001004"));
//        newListTab.add(new NewListTab("0010001046","西餐","0010001004"));
        wxAiccles.add(new WxAiccle("1","时尚"));
        wxAiccles.add(new WxAiccle("2","热点"));
        wxAiccles.add(new WxAiccle("37","段子"));
        wxAiccles.add(new WxAiccle("17","游戏"));
        wxAiccles.add(new WxAiccle("3","健康"));
        wxAiccles.add(new WxAiccle("5","百科"));
        wxAiccles.add(new WxAiccle("7","娱乐"));
        wxAiccles.add(new WxAiccle("8","美文"));
        wxAiccles.add(new WxAiccle("9","旅行"));
        wxAiccles.add(new WxAiccle("10","媒体达人"));
        wxAiccles.add(new WxAiccle("11","搞笑"));
        wxAiccles.add(new WxAiccle("12","影视音乐"));
        wxAiccles.add(new WxAiccle("13","互联网"));
        wxAiccles.add(new WxAiccle("14","文史"));
        wxAiccles.add(new WxAiccle("15","金融"));
        wxAiccles.add(new WxAiccle("16","体育"));
        wxAiccles.add(new WxAiccle("18","两性"));
        wxAiccles.add(new WxAiccle("19","社交交友"));
        wxAiccles.add(new WxAiccle("20","女人"));
        wxAiccles.add(new WxAiccle("23","购物"));
        wxAiccles.add(new WxAiccle("24","美女"));
        wxAiccles.add(new WxAiccle("25","微信技巧"));
        wxAiccles.add(new WxAiccle("26","星座"));
        wxAiccles.add(new WxAiccle("27","美食"));
        wxAiccles.add(new WxAiccle("28","教育"));
        wxAiccles.add(new WxAiccle("29","职场"));
        wxAiccles.add(new WxAiccle("30","酷品"));
        wxAiccles.add(new WxAiccle("31","母婴"));
        wxAiccles.add(new WxAiccle("32","摄影"));
        wxAiccles.add(new WxAiccle("33","创投"));
        wxAiccles.add(new WxAiccle("34","典藏"));
        wxAiccles.add(new WxAiccle("35","家装"));
        wxAiccles.add(new WxAiccle("36","汽车"));
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
        weatherForecastService.getWeatherForecastService(province, Api.APP_KEY, city, new Callback<WeatherForecast>() {
            @Override
            public void onSuccess(WeatherForecast model) {
                super.onSuccess(model);
                if (model != null) {
                    weatherForecast = model;
                    if (weatherForecast.getResult().get(0).getWeather().equals("多云") || weatherForecast.getResult().get(0).getWeather().equals("晴")) {
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

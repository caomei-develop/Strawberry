package com.cm.strawberry.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cm.strawberry.R;
import com.cm.strawberry.adapter.GirlAdapter;
import com.cm.strawberry.api.Api;
import com.cm.strawberry.base.BaseActivity;
import com.cm.strawberry.bean.Meizi;
import com.cm.strawberry.manger.CoverFlowLayoutManger;
import com.cm.strawberry.service.SwitchService;
import com.cm.strawberry.view.RecyclerCoverFlow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SwitchActivity extends BaseActivity {
    @Bind(R.id.RecyclerCoverFlow)
    RecyclerCoverFlow recyclerCoverFlow;
    private GirlAdapter girlAdapter;
    private List<Meizi> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        ButterKnife.bind(this);
        init();
        GirlData();
    }

    private void GirlData() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Document doc = null;
                try {
                    doc = Jsoup.connect(Api.MEI_ZI_URL).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (doc != null) {
                    Element contentloop = doc.getElementById("content-loop");
                    Elements entrytou = contentloop.getElementsByClass("entry-tou");
                    if (entrytou != null) {
                        for (int i = 0; i < entrytou.size(); i++) {
                            Elements a = entrytou.get(i).getElementsByTag("a");
                            for (int j = 0; j < a.size(); j++) {
                                Meizi meizi = new Meizi();
                                Elements img = a.get(j).getElementsByTag("img");
                                meizi.setSrc(img.attr("src"));
                                list.add(meizi);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        girlAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                    }

                }
            }
        }.start();

    }

    private void init() {
        list = new ArrayList<>();
        recyclerCoverFlow.setAdapter(girlAdapter = new GirlAdapter(this, list));
        recyclerCoverFlow.setOnItemSelectedListener(new CoverFlowLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {
                ((TextView) findViewById(R.id.index)).setText((position + 1) + "/" + recyclerCoverFlow.getLayoutManager().getItemCount());
            }
        });

    }
}

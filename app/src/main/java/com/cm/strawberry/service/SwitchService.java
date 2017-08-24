package com.cm.strawberry.service;

import android.content.Context;

import com.cm.strawberry.bean.Meizi;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.ui.activity.SwitchActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2017/8/20.
 */

public class SwitchService {
    private List<Meizi> list;
    public void meizi(final Context context, final String url, final Callback<List<Meizi>>meiziCallback){
        list = new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                super.run();
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(doc != null){
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
                                meiziCallback.onSuccess(list);
                            }
                        }
                    }
                }
            }
        }.start();
    }
}

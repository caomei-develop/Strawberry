package com.cm.strawberry.service;

import com.cm.strawberry.bean.Meizi;
import com.cm.strawberry.callback.Callback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by zw on 2017/8/20.
 */

public class SwitchService {
    public void meizi(String url,Callback<Meizi>meiziCallback){
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(doc != null){

        }
    }
}

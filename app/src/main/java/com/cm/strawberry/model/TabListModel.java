package com.cm.strawberry.model;

import android.content.Context;

import com.cm.strawberry.bean.WxAiccle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw on 2017/8/19.
 */

public class TabListModel {
    private Context context;
    private List<WxAiccle> wxAiccles;

    public TabListModel(Context context) {
        this.context = context;
    }

    public List<WxAiccle> getWxAiccles() {
        wxAiccles = new ArrayList<>();
        for (int i = 0; i < indexString.length; i++) {
            wxAiccles.add(new WxAiccle(indexString[i],tabString[i]));
        }
        return wxAiccles;
    }

    private String[] indexString = {
            "1", "2", "37", "17", "3", "5",
            "7", "8", "9", "10", "11", "12",
            "13", "14", "15", "16", "18", "19",
            "20", "23", "24", "25", "26", "27",
            "28", "29", "30", "31", "32", "33",
            "34", "35", "36"
    };
    private String[] tabString = {
            "时尚", "热点", "段子", "游戏", "健康",
            "百科", "娱乐", "美文", "旅行", "媒体达人",
            "搞笑", "影视音乐", "互联网", "文史", "金融",
            "体育", "两性", "社交交友", "女人", "购物",
            "美女", "微信技巧", "星座", "美食", "教育",
            "职场", "酷品", "母婴", "摄影", "创投", "典藏",
            "家装", "汽车"};
}

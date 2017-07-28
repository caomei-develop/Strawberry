package com.cm.strawberry.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cm.strawberry.bean.WxAiccle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwei on 17-7-28.
 */

public class NewListAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragments;
    private  List<WxAiccle> childs;
    public NewListAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return childs.get(position).getName();
    }
    public void setData(List<WxAiccle> list){
        childs = new ArrayList<>();
        if (list!=null){
            this.childs =list;
        }
        notifyDataSetChanged();
    }
}

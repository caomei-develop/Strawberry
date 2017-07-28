package com.cm.strawberry.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cm.strawberry.R;
import com.cm.strawberry.api.Api;
import com.cm.strawberry.bean.WxAiccle;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.service.WxAcaleService;

/**
 * Created by zhouwei on 17-7-28.
 */

public class NewListFragment extends Fragment{
    private String cid;
    private int  page , size;
    public static NewListFragment newInstance(int tags, String cid) {
        Bundle args = new Bundle();
        //区别不同分类的fragment
        args.putInt("tag", tags);
        args.putString("cid",cid);
        NewListFragment fragment = new NewListFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_list_fragment, container, false);
        init();
        return view;
    }

    private void init() {
        cid = getArguments().getString("cid");
        getWxacale();
    }
    private WxAcaleService WxAcaleService = new WxAcaleService();
    private void getWxacale() {
        page = 1;
        size= 10;
        WxAcaleService.getWXAcaleService(page, cid, Api.APP_KEY, size, new Callback<WxAiccle>() {
            @Override
            public void onSuccess(WxAiccle model) {
                super.onSuccess(model);
                if (model != null){

                }
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

}

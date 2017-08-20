package com.cm.strawberry.ui.fragment;

import android.support.v7.widget.RecyclerView;

import com.cm.strawberry.api.Api;
import com.cm.strawberry.base.AbsListFragment;
import com.cm.strawberry.bean.Meizi;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.service.SwitchService;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by zw on 2017/8/20.
 */

public class SwitchFragment extends AbsListFragment{
    private SwitchService switchService;
    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    protected RecyclerArrayAdapter getRecyclerViewAdapter() {
        return null;
    }

    @Override
    protected void loadData() {
        switchService = new SwitchService();
        switchService.meizi(Api.MEI_ZI_URL, new Callback<Meizi>() {
            @Override
            public void onSuccess(Meizi model) {
                super.onSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });

    }

}

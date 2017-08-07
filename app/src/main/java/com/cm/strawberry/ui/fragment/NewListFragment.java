package com.cm.strawberry.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cm.strawberry.R;
import com.cm.strawberry.adapter.NewsListDilstAdapter;
import com.cm.strawberry.api.Api;
import com.cm.strawberry.base.AbsListFragment;
import com.cm.strawberry.bean.WxSearch;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.service.WxAcaleService;
import com.cm.strawberry.ui.activity.NewsdetailsActivity;
import com.cm.strawberry.util.ActivityUtil;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

/**
 * Created by zhouwei on 17-7-28.
 */

public class NewListFragment extends AbsListFragment {
    private WxAcaleService wxAcaleService;
    private int page = 1 ;
    private String cid;
    public static NewListFragment newInstance(int tags, String cid) {
        Bundle args = new Bundle();
        //区别不同分类的fragment
        args.putInt("tag", tags);
        args.putString("cid", cid);
        NewListFragment fragment = new NewListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cid = getArguments().getString("cid");
    }

    @Override
    public void setVisibleHint(boolean isVisibleToUser) {
        super.setVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            page = 1;
            loadData();
        }
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    protected RecyclerArrayAdapter getRecyclerViewAdapter() {
        return new NewsListDilstAdapter(getContext());
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
    }

    @Override
    public void onLoadMore() {
        page++;
        loadData();
        super.onLoadMore();
    }

    @Override
    protected boolean isHeaderPrior() {
        return false;
    }
    @Override
    protected void loadData() {
        if (isAlreadyToLastPage() || isRequesting()) {
            return;
        }
        wxAcaleService = new WxAcaleService();
        wxAcaleService.getWXAcaleService(page, cid, Api.APP_KEY, 10, new Callback<WxSearch>() {
            @Override
            public void onSuccess(WxSearch model) {
                super.onSuccess(model);
                if (model != null){
                    processSuccess(model.getResult().getList());
                }
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }
    protected void processSuccess(List<WxSearch.ResultBean.ListBean> list) {
        if (getActivity() != null) {
            if (adapter.getCount() > 0 && isRefreshing()) {
                adapter.clear();
            }
            boolean hasMore = false;
            if (list != null && list.size() > 0) {
                if (list.size() == getPageSize()) {
                    hasMore = true;
                }
            }
            setRefreshing(false);
            adapter.addAll(list);
            if (hasMore) {
                return;
            }
            if (adapter.getCount() < getMinSizeToShowFooter()) {
                adapter.setNoMore(null);
            } else {
                adapter.setNoMore(R.layout.view_nomore);
            }
        }
    }

    @Override
    public void onItemViewClick(View view, int position) {
        super.onItemViewClick(view, position);
        WxSearch.ResultBean.ListBean listBean = (WxSearch.ResultBean.ListBean) adapter.getItem(position);
        if (listBean != null){
            ActivityUtil.toActivity(getActivity(),NewsdetailsActivity.class,"url",listBean.getSourceUrl());
        }
    }
}

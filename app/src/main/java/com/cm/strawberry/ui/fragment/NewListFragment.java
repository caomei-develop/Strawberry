package com.cm.strawberry.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cm.strawberry.adapter.NewsListDilstAdapter;
import com.cm.strawberry.base.AbsListFragment;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by zhouwei on 17-7-28.
 */

public class NewListFragment extends AbsListFragment {
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
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    @Override
    protected RecyclerArrayAdapter getRecyclerViewAdapter() {
        return null;
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();

    }
}

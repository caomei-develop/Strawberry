package com.cm.strawberry.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cm.strawberry.R;
import com.cm.strawberry.adapter.NewsListDilstAdapter;
import com.cm.strawberry.api.Api;
import com.cm.strawberry.bean.WxAiccle;
import com.cm.strawberry.bean.WxSearch;
import com.cm.strawberry.callback.Callback;
import com.cm.strawberry.service.WxAcaleService;
import com.cm.strawberry.view.CmRecyclerView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by zhouwei on 17-7-28.
 */

public class NewListFragment extends Fragment {
//    private CmRecyclerView recyclerView;
    private String cid;
    private View view;
    private int page, size;
    private NewsListDilstAdapter newsListDilstAdapter;
    private List<WxSearch.ResultBean.ListBean> list;
    private WxAcaleService WxAcaleService = new WxAcaleService();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_list_fragment, container, false);
//        recyclerView = view.findViewById(R.id.news_list_rview);
        init();
        return view;
    }

    private void init() {
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cid = getArguments().getString("cid");
//        getWxacale();
    }

    private void getWxacale() {
        page = 1;
        size = 10;
        WxAcaleService.getWXAcaleService(page, cid, Api.APP_KEY, size, new Callback<WxSearch>() {
            @Override
            public void onSuccess(final WxSearch model) {
                super.onSuccess(model);
                if (model != null) {
                    if (model != null) {
//                        list = model.getResult().getList();
//                        newsListDilstAdapter.setData(list);
//                        recyclerView.setAdapter(newsListDilstAdapter = new NewsListDilstAdapter(getActivity()));
//                        newsListDilstAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

}

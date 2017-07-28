package com.cm.strawberry.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cm.strawberry.R;
import com.cm.strawberry.bean.WxSearch;

import java.util.List;

/**
 * Created by zhouwei on 17-7-28.
 */

public class NewsListDilstAdapter extends RecyclerView.Adapter<NewsListDilstAdapter.ViewHolder>{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<WxSearch.ResultBean.ListBean>result;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.wx_dialist_layout), parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    private void setData(List<WxSearch.ResultBean.ListBean>list){
        if (list != null){
            this.result = list;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }
}

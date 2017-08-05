package com.cm.strawberry.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cm.strawberry.R;
import com.cm.strawberry.bean.WxSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwei on 17-7-28.
 */

public class NewsListDilstAdapter extends RecyclerView.Adapter<NewsListDilstAdapter.ViewHolder>{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<WxSearch.ResultBean.ListBean>result;
    public NewsListDilstAdapter(Context context){
        this.context= context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.wx_dialist_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(result.get(position).getThumbnails()).into(holder.wx_img);
        holder.wx_title.setText(result.get(position).getSubTitle());
        holder.wx_time.setText(result.get(position).getPubTime());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public void setData(List<WxSearch.ResultBean.ListBean>list){
        result = new ArrayList<>();
        if (list != null){
            this.result = list;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView wx_img;
        private TextView wx_title,wx_time;
        public ViewHolder(View v) {
            super(v);
            wx_img = v.findViewById(R.id.wx_img);
            wx_title = v.findViewById(R.id.wx_title);
            wx_time = v.findViewById(R.id.wx_time);
        }
    }
}

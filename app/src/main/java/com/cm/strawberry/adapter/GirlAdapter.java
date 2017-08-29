package com.cm.strawberry.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cm.strawberry.R;
import com.cm.strawberry.bean.Meizi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouwei on 17-8-22.
 */

public class GirlAdapter extends RecyclerView.Adapter<GirlAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Meizi> list;

    public GirlAdapter(Context context,List<Meizi>list) {
        this.context = context;
        this.list = list;
        this.layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.girl_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load("http://www.keaitupian.com/" + list.get(position).getSrc()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
        }
    }

}

package com.cm.strawberry.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cm.strawberry.R;
import com.cm.strawberry.bean.WxSearch;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhouwei on 17-7-28.
 */

public class NewsListDilstAdapter extends RecyclerArrayAdapter<WxSearch.ResultBean.ListBean> {

    public NewsListDilstAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.wx_dialist_layout);
    }

    public class ViewHolder extends BaseViewHolder<WxSearch.ResultBean.ListBean> {
        @Bind(R.id.wx_img)
        ImageView wx_img;
        @Bind(R.id.wx_title)
        TextView wx_title;
        @Bind(R.id.wx_time)
        TextView wx_time;

        public ViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(WxSearch.ResultBean.ListBean data) {
            super.setData(data);
            if (data != null) {
                if (data.getThumbnails() == null) {
                } else {
                    Glide.with(getContext()).load(data.getThumbnails()).into(wx_img);
                }
                wx_title.setText(data.getTitle());
                wx_time.setText(data.getPubTime());

            }
        }
    }
}

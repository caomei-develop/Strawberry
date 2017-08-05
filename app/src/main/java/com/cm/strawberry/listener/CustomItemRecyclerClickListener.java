package com.cm.strawberry.listener;

import android.view.View;

import com.cm.strawberry.util.ListenerUtil;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * Created by zhouwei on 17-8-5.
 */

public class CustomItemRecyclerClickListener implements RecyclerArrayAdapter.OnItemViewClickListener {
    RecyclerArrayAdapter.OnItemViewClickListener onItemClickListener;

    public CustomItemRecyclerClickListener(RecyclerArrayAdapter.OnItemViewClickListener listener) {
        onItemClickListener = listener;
    }

    @Override
    public void onItemViewClick(View view, int position) {
        if (!ListenerUtil.isMultipleClick()) {
            onItemClickListener.onItemViewClick(view, position);
        }
    }
}

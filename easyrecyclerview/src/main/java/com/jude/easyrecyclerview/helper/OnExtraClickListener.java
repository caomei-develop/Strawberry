package com.jude.easyrecyclerview.helper;

import android.view.View;

public interface OnExtraClickListener<T> {
	public void onExtraClick(View view, T data, int pos);
}

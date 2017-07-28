package com.cm.strawberry.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cm.strawberry.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhouwei on 17-7-28.
 */

public class QRcodeLayout extends LinearLayout {
//    //生成
//    @Bind(R.id.generate_qrcode_btn)
//    TextView generateQRcode;
//    //扫描
//    @Bind(R.id.scanning_qrcode_btn)
//    TextView scanningQRcode;
    private Context context;
    public QRcodeLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
        inflate(context, R.layout.qrcode_layout, this);
    }
    @OnClick({R.id.generate_qrcode_btn,R.id.scanning_qrcode_btn})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.generate_qrcode_btn:
                //生成
                break;
            case R.id.scanning_qrcode_btn:
                //扫描
                break;
        }

    }
}

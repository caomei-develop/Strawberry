package com.cm.strawberry.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cm.strawberry.R;
import com.cm.strawberry.util.QRCode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QrcodeActivity extends AppCompatActivity {
    @Bind(R.id.qrcode_img)
    ImageView qrcodeImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle("扫描二维码");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        init();
    }

    private void init() {
        qrcodeImg.setImageBitmap(QRCode.createQRCode("https://github.com/zwStrawberry"));
//        qrcodeImg.setImageBitmap(QRCode.createQRCodeWithLogo2("https://github.com/zwStrawberry",
//                500, drawableToBitmap(getResources().getDrawable(R.mipmap.ic_launcher))));
    }

    @OnClick(R.id.saomiao)
    public void Onclick(View view){
        switch (view.getId()){
            case R.id.saomiao:
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}

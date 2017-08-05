package com.cm.strawberry.util;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;


import com.cm.strawberry.app.StrawberryApplication;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by zhouwei on 17-7-27.
 */

public class Utils {
    /**
     * 获取高德匹配sha1值
     * @param context
     * @return
     */
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void setViewVisibility(View view, int visibility) {
        if (view != null && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }
    public static int getDeviceWidth(Context context) {
        return getDeviceSize(context)[0];
    }

    public static int getDeviceHeight(Context context) {
        return getDeviceSize(context)[1];
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static int[] getDeviceSize(Context context) {
        int[] size = new int[2];

        int measuredWidth = 0;
        int measuredHeight = 0;
        Point point = new Point();
        WindowManager wm = ((WindowManager) StrawberryApplication.strawberryApp.getSystemService(Context.WINDOW_SERVICE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.getDefaultDisplay().getSize(point);
            measuredWidth = point.x;
            measuredHeight = point.y;
        } else {
            DisplayMetrics dm = StrawberryApplication.strawberryApp.getResources().getDisplayMetrics();
            measuredWidth = dm.widthPixels;
            measuredHeight = dm.heightPixels;
        }

        size[0] = measuredWidth;
        size[1] = measuredHeight;
        return size;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}


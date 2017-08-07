package com.cm.strawberry.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.*;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;


import com.cm.strawberry.R;
import com.cm.strawberry.app.StrawberryApplication;
import com.cm.strawberry.callback.WaitableActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
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

    public static int sp(Context context, int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources()
                .getDisplayMetrics());
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        return w_screen;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int h_screen = dm.heightPixels;
        return h_screen;
    }

    public static int getDeviceWidth(Context context) {
        return getDeviceSize(context)[0];
    }

    public static int getDeviceHeight(Context context) {
        return getDeviceSize(context)[1];
    }

    public static float getDensity(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        wm.getDefaultDisplay().getMetrics(metrics);

        return metrics.density;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @SuppressLint("NewApi")
    public static int getSoftButtonsBarHeight(Activity context) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            context.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    public static boolean isPackageExisted(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        return packageInfo != null;
    }

    public static void openApp(Context context, String packageName) throws PackageManager.NameNotFoundException {
        PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, 0);

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

        Iterator<ResolveInfo> iterator = apps.iterator();
        while (iterator.hasNext()) {
            ResolveInfo ri = iterator.next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName(packageName, className);
                intent.setComponent(cn);
                context.startActivity(intent);
            }
        }
    }

    public static List<PackageInfo> getNonSystemApps(Context context) {
        List<PackageInfo> list = context.getPackageManager().getInstalledPackages(0);
        List<PackageInfo> systemApps = new ArrayList<PackageInfo>();
        for (PackageInfo info : list) {
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                systemApps.add(info);
            }
        }
        list.removeAll(systemApps);
        return list;
    }

    public static void showToast(Context context, int id) {
        Toast.makeText(StrawberryApplication.strawberryApp, StrawberryApplication.strawberryApp.getResources().getString(id), Toast.LENGTH_SHORT)
                .show();
    }

    public static void showToast(Context context, String string) {
        if (!StringUtil.isEmpty(string)) {
            Toast.makeText(StrawberryApplication.strawberryApp, string, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToastLong(Context context, int id) {
        Toast.makeText(StrawberryApplication.strawberryApp, StrawberryApplication.strawberryApp.getResources().getString(id), Toast.LENGTH_LONG)
                .show();
    }

    public static void showToastLong(Context context, String string) {
        if (!StringUtil.isEmpty(string)) {
            Toast.makeText(StrawberryApplication.strawberryApp, string, Toast.LENGTH_LONG).show();
        }
    }

    public static void showToast(String string, long duration) {
        final Toast toast = Toast.makeText(StrawberryApplication.strawberryApp, string, Toast.LENGTH_SHORT);
        toast.show();
        new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            public void onFinish() {
                toast.show();
            }

        }.start();
    }

    public static void showToast(int id) {
        Toast.makeText(StrawberryApplication.strawberryApp, StrawberryApplication.strawberryApp.getResources().getString(id), Toast.LENGTH_SHORT)
                .show();
    }

    public static void showToast(String string) {
        if (!StringUtil.isEmpty(string)) {
            Toast.makeText(StrawberryApplication.strawberryApp, string, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToastLong(int id) {
        Toast.makeText(StrawberryApplication.strawberryApp, StrawberryApplication.strawberryApp.getResources().getString(id), Toast.LENGTH_LONG)
                .show();
    }

    public static void showToastLong(String string) {
        if (!StringUtil.isEmpty(string)) {
            Toast.makeText(StrawberryApplication.strawberryApp, string, Toast.LENGTH_LONG).show();
        }
    }

    public static String map2Form(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        if (map == null) {
            return stringBuilder.toString();
        } else {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }
    }

    public static int getVersionCode() {
        int versionCode = Integer.MAX_VALUE;
        try {
            versionCode = StrawberryApplication.strawberryApp.getPackageManager().getPackageInfo(
                    StrawberryApplication.strawberryApp.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getVersionName(Context context) {
        try {
            String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return versionName;
        } catch (Exception e) {
        }
        return "Unknown";
    }

    public static Map<String, Object> buildMap(Object... keyValues) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        for (int i = 0; i < keyValues.length; i += 2) {
            resultMap.put((String) keyValues[i], keyValues[i + 1]);
        }
        return resultMap;
    }

    public static String getMacAddress() {
        WifiManager wimanager = (WifiManager) StrawberryApplication.strawberryApp.getSystemService(Context.WIFI_SERVICE);

        String macAddress = null;
        try {
            macAddress = wimanager.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
        }

        if (StringUtil.isEmpty(macAddress)) {
            macAddress = "000000000000";
        } else {
            macAddress = macAddress.replace(":", "");
        }

        return macAddress;
    }

    public static String getAndroidId() {
        String androidId = null;
        try {
            Context context = StrawberryApplication.strawberryApp;
            ContentResolver contentResolver = context.getContentResolver();
            androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
        }
        if (StringUtil.isEmpty(androidId)) {
            androidId = "0";
        }

        return androidId;
    }

    public static String convertByte(long total) {
        if (total < 1024) {
            return total + " B";
        } else if (total < 1024 * 1024) {
            return total / 1024 + " KB";
        } else {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);
            return nf.format(total / 1024 / 1024.0) + " MB";
        }
    }

    public static String convertSpeed(long total) {
        if (total < 1024) {
            return total + " B/s";
        } else if (total < 1024 * 1024) {
            return total / 1024 + " KB/s";
        } else {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);
            return nf.format(total / 1024 / 1024.0) + " MB/s";
        }
    }

    public static String convertByte(Long total) {
        if (total == null) {
            return "0 MB";
        }
        return convertByte(total.longValue());
    }

    public static String convertByteToKiloByte(long total) {
        return total / 1024 + "KB";
    }

    public static boolean isMIUI(Context paramContext) {
        if ("xiaomi".equalsIgnoreCase(Build.MANUFACTURER)) {
            return true;
        }
        return false;
    }

    public static boolean isMIUI_V5(Context paramContext) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (isMIUI(paramContext)) && ("V5".equalsIgnoreCase(properties.getProperty("ro.miui.ui.version.name")));
    }

    public static boolean isMIUI_V6(Context paramContext) {
        Properties properties = new Properties();
        int miui = 0;
        try {
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (properties.getProperty("ro.miui.ui.version.name") != null) {
            try {
                miui = Integer.parseInt(properties.getProperty("ro.miui.ui.version.name").substring(1));
            } catch (Exception e) {
            }
        }
        return (isMIUI(paramContext)) && (miui > 5);
    }

    public static String getAppChannel(Context context) {
        String appChannel = "";
        try {
            Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA).metaData;
            appChannel = (String) metaData.get("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appChannel != null ? appChannel : "";
    }

    public static void startApkInstall(Context context, String path) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(i);
        } catch (Exception re) {
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    public static void copyToClipboard(Context context, String content) {
        try {
            if (android.os.Build.VERSION.SDK_INT > 11) {
                android.content.ClipboardManager cmb = (android.content.ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setPrimaryClip(ClipData.newPlainText(null, content));
            } else {
                android.text.ClipboardManager cmb = (android.text.ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(content);
            }
        } catch (Exception e) {
        }
    }

    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]").replaceAll("！", "!").replaceAll("：", ":");
        String regEx = "[『』]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }


    public static void startActivityForUrl(Context context, boolean isAction, String urlOrAction, String exceptionMsg) {
        Intent intent;
        if (!isAction) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlOrAction));
        } else {
            intent = new Intent(urlOrAction);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Utils.showToast(context, exceptionMsg);
        }
    }

    public static long calculateDiskCacheSize(File paramFile) {
        long l1 = 20000000L;
        try {
            StatFs statFs = new StatFs(paramFile.getAbsolutePath());
            long l2 = statFs.getBlockCount() * statFs.getBlockSize() / 50L;
            l1 = l2;
        } catch (IllegalArgumentException e) {
        }
        return Math.max(Math.min(l1, 200000000L), 20000000L);
    }

    public static void setViewVisibility(View view, int visibility) {
        if (view != null && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public static boolean isVersionEqualOrNewerThan(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackground(View view, Drawable drawable) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;

    }

    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static boolean personIdValidation(String text) {
        String regx = "[0-9]{17}x";
        String reg1 = "[0-9]{15}";
        String regex = "[0-9]{18}";
        String regX = "[0-9]{17}X";
        return text.matches(regx) || text.matches(reg1) || text.matches(regex) || text.matches(regX);
    }

    public static boolean checkPhone(String phone) {
        Pattern pattern = Pattern.compile("^(13[0-9]|15[0-9]|17[0-9]|15[6-9]|18[0|2|3|5-9])\\d{8}$");
        Matcher matcher = pattern.matcher(phone);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean simpleCheckPhone(String phone) {
        Pattern pattern = Pattern.compile("^1\\d{10}$");
        Matcher matcher = pattern.matcher(phone);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static Boolean isEmail(String str) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(str);
        return matcher.find();
    }

    public static boolean isClassMember(String key, Class c) {
        if (StringUtil.isEmpty(key)) {
            return false;
        }
        Field[] fs = c.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true);
            try {
                if (key.equals(f.getName())) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    public static boolean isForeground() {
        android.app.ActivityManager manager = (android.app.ActivityManager) StrawberryApplication.strawberryApp.getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(1);
        if (tasks == null || tasks.isEmpty()) {
            return false;
        } else {
            return "com.cashtoutiao".equals(tasks.get(0).topActivity.getPackageName());
        }
    }

    public static boolean colorEqualExcludeAlpha(int c1, int c2) {
        return Color.red(c1) == Color.red(c2) && Color.blue(c1) == Color.blue(c2) && Color.green(c1) == Color.green(c2);
    }

    public static boolean isAccessibilitySettingsOn(Context context, String serviceName) {
        try {
            int accessibilityEnabled = 0;
            final String service = context.getPackageName() + "/" + serviceName;
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

            if (accessibilityEnabled == 1) {
                String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(),
                        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
                if (settingValue != null) {
                    mStringColonSplitter.setString(settingValue);
                    while (mStringColonSplitter.hasNext()) {
                        String accessibilityService = mStringColonSplitter.next();
                        if (accessibilityService.equalsIgnoreCase(service)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean checkServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        android.app.ActivityManager myAM = (android.app.ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(Integer.MAX_VALUE);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public static boolean isComponentDisenabled(Class cls) {
        final PackageManager pm = StrawberryApplication.strawberryApp.getPackageManager();
        return pm.getComponentEnabledSetting(new ComponentName(StrawberryApplication.strawberryApp, cls)) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    public static boolean isComponentEnabled(Class cls) {
        final PackageManager pm = StrawberryApplication.strawberryApp.getPackageManager();
        return pm.getComponentEnabledSetting(new ComponentName(StrawberryApplication.strawberryApp, cls)) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    public static void setComponentEnable(Class cls) {
        final PackageManager pm = StrawberryApplication.strawberryApp.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(StrawberryApplication.strawberryApp, cls),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
        }
        return "";
    }

    public static boolean isUsageAccessGranted(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid,
                        applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (Throwable e) {
            return false;
        }
    }

    public static String getTimeDesc(int seconds) {
        String result = "";
        int second = seconds % 60;
        if (second != 0) {
            result += second + "秒";
        }
        int minute = (seconds / 60) % 60;
        if (minute != 0) {
            result = minute + "分钟" + result;
        }
        int hour = seconds / 3600;
        if (hour > 0) {
            result = hour + "小时" + result;
        }
        if (StringUtil.isEmpty(result)) {
            return "0秒";
        }
        return result;

    }

    public static String getBaiduMacAddress() {
        WifiManager wimanager = (WifiManager) StrawberryApplication.strawberryApp.getSystemService(Context.WIFI_SERVICE);

        String macAddress = null;
        try {
            macAddress = wimanager.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
        }

        if (StringUtil.isEmpty(macAddress)) {
            macAddress = "000000000000";
        }

        return macAddress;
    }
}


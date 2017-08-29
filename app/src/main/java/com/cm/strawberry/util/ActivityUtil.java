package com.cm.strawberry.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cm.strawberry.base.BaseActivity;

public class ActivityUtil {
	
	/**
	 * 跳转到指定activity
	 */
	public static void toActivity(Activity from, Class<? extends Activity> clz) {
		Intent intent = new Intent(from, clz);
		from.startActivity(intent);
	}
	
	/**
	 * 跳转到指定activity，带传值
	 */
	public static void toActivity(Activity from, Class<? extends Activity> clz, Bundle data) {
		Intent intent = new Intent(from, clz);
		intent.putExtras(data);
		from.startActivity(intent);
	}
	
	/**
	 * 跳转到指定activity，等于startActivityForResult，带传值
	 */
	public static void toActivity(Activity from, Class<? extends BaseActivity> clz, Bundle data, int requestCode) {
		Intent intent = new Intent(from, clz);
		intent.putExtras(data);
		from.startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 跳转到指定activity，等于startActivityForResult
	 */
	public static void toActivity(Activity from, Class<? extends Activity> clz, int requestCode) {
		Intent intent = new Intent(from, clz);
		from.startActivityForResult(intent, requestCode);
	}

    /**
     * 跳转带参数
     */
	public static void toActivity(Activity from, Class<? extends Activity> cls, String param, String obj,String obj1, String parame1, String obj2, String parame2) {
		Intent intent = new Intent(from, cls);
		intent.putExtra(param, obj);
		intent.putExtra(parame1, obj1);
		intent.putExtra(parame2, obj2);
        from.startActivity(intent);
	}
    /**
     * 跳转带参数
     */
	public static void toActivity(Activity from, Class<? extends Activity> cls, String obj, String param) {
		Intent intent = new Intent(from, cls);
		intent.putExtra(param, obj);
        from.startActivity(intent);
	}
    /**
     * 跳转带参数
     */
	public static void toActivity(Activity from, Class<? extends Activity> cls, String obj, String param, String obj1, String parame1) {
		Intent intent = new Intent(from, cls);
		intent.putExtra(param, obj);
		intent.putExtra(param, obj1);
        from.startActivity(intent);
	}
}

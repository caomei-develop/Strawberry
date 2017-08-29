package com.cm.strawberry.manger;

import com.cm.strawberry.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by zhouwei on 17-7-31.
 */

public class ActivityManager {
    private static List<BaseActivity> baseActivityList = new ArrayList<>();
    private static ActivityManager instance;
    private Object[] getActivityArray(){
        return baseActivityList.toArray();
    }
    /**
     * 单一是实列
     */
    public static ActivityManager getInstance(){
        if (instance == null){
            instance= new ActivityManager();
        }
        return instance;
    }
    /**
     * 添加activity到堆栈
     */
    public void addActivity(BaseActivity activity){
        if (baseActivityList == null){
            baseActivityList = new Stack<>();
        }
        baseActivityList.add(activity);
    }
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public BaseActivity currentActivity() {
        return baseActivityList == null || baseActivityList.size() == 0 ? null : baseActivityList.get(baseActivityList.size() - 1);
    }
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        finishActivity(currentActivity());
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(BaseActivity activity) {
        if (activity != null) {
            activity.finish();
            baseActivityList.remove(activity);
        }
    }

    public void remove(BaseActivity activity) {
        baseActivityList.remove(activity);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (BaseActivity activity : baseActivityList) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        Object[] activityArray = getActivityArray();
        for (int i = activityArray.length - 1; i >= 0; i--) {
            if (null != activityArray[i]) {
                BaseActivity activity = (BaseActivity) activityArray[i];
                activity.finish();
            }
        }
        baseActivityList.clear();
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     */
    public void finishOthersActivity(Class<?> cls) {
        Object[] array = getActivityArray();
        for (Object activity : array) {
            if (!(activity.getClass().equals(cls))) {
                finishActivity((BaseActivity) activity);
            }
        }
    }

    /**
     * 获取指定的Activity
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseActivity> T getActivity(Class<? extends BaseActivity> cls) {
        for (BaseActivity activity : baseActivityList) {
            if (activity.getClass().equals(cls)) {
                return (T) activity;
            }
        }
        return null;
    }

}

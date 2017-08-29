package com.cm.strawberry.manger;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * Created by zhouwei on 17-8-5.
 */

public class InputMethodManagerUtil {
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        try {
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) {
                return;
            }

            String[] arr = new String[] { "mCurRootView", "mServedView", "mNextServedView" };
            Field f = null;
            Object obj_get = null;
            for (int i = 0; i < arr.length; i++) {
                String param = arr[i];
                f = imm.getClass().getDeclaredField(param);
                if (f.isAccessible() == false) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环
                        break;
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

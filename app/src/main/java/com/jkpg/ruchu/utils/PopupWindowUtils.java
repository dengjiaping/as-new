package com.jkpg.ruchu.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by qindi on 2017/7/13.
 */

public class PopupWindowUtils {
    /**
     * 改变背景颜色
     */
    public static void darkenBackground(Activity activity, Float bgColor) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgColor;
        activity.getWindow().setAttributes(lp);

        if (bgColor == 1f) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }
}


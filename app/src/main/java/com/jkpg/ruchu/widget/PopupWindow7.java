package com.jkpg.ruchu.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by qindi on 2017/7/5.
 */

public class PopupWindow7 extends PopupWindow {
    public PopupWindow7(Context context) {
        super(context);
    }

    public PopupWindow7(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupWindow7(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PopupWindow7(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT >= 24){
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }
}

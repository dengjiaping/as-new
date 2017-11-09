package com.jkpg.ruchu.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;

import com.jkpg.ruchu.R;

/**
 * Created by qindi on 2017/11/7.
 */

public class Loadingutil {
    public static AlertDialog getLoading(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.dialog);
        builder.setView(View.inflate(activity.getApplicationContext(), R.layout.view_animation, null));
        AlertDialog dialog = builder.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    activity.finish();

                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
}

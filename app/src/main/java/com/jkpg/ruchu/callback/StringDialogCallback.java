package com.jkpg.ruchu.callback;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import okhttp3.Call;
import okhttp3.Response;

public abstract class StringDialogCallback extends StringCallback {

    private AlertDialog dialog;
    private Activity activity;

    public StringDialogCallback(final Activity activity) {
        this.activity = activity;
       /* dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中");*/
        if (activity.isDestroyed()) {
            LogUtils.d(activity.getLocalClassName() + "=activity");
            return;

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.dialog);
        builder.setView(View.inflate(activity.getApplicationContext(), R.layout.view_animation, null));
        dialog = builder.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                    Toast.makeText(UIUtils.getContext(), getRunningActivityName(activity), Toast.LENGTH_SHORT).show();
                    if (getRunningActivityName(activity).equals("MainActivity")) {
                        activity.moveTaskToBack(true);
                    } else {
                        activity.finish();

                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }


    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        if (activity.isDestroyed()) {
            LogUtils.d(activity.getLocalClassName() + "=activity");
            return;
        }
        //网络请求前显示对话框
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onAfter(@Nullable String s, @Nullable Exception e) {
        super.onAfter(s, e);
        if (activity.isDestroyed()) {
            LogUtils.d(activity.getLocalClassName() + "=activity");
            return;
        }
        //网络请求结束后关闭对话框
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();

        }
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        if (activity.isDestroyed()) {
            LogUtils.d(activity.getLocalClassName() + "=activity");
            return;
        }
//        if (dialog != null && dialog.isShowing()) {
            //// dialog.dismiss();
            ToastUtils.showShort(UIUtils.getContext(), "服务器开小差了，请稍后再试吧~");
//        }
    }

    private String getRunningActivityName(Activity activity) {

        String contextString = activity.toString();

        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));

    }


}

package com.jkpg.ruchu.callback;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import okhttp3.Call;
import okhttp3.Response;

public abstract class StringDialogCallback extends StringCallback {

    private final AlertDialog.Builder mBuilder;
    private final AlertDialog dialog;

    public StringDialogCallback(final Activity activity) {
       /* dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中");*/
        mBuilder = new AlertDialog.Builder(activity, R.style.dialog);
        mBuilder.setView(View.inflate(activity.getApplicationContext(), R.layout.view_animation, null));
        dialog = mBuilder.show();
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
        //网络请求前显示对话框
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onAfter(@Nullable String s, @Nullable Exception e) {
        super.onAfter(s, e);
        //网络请求结束后关闭对话框
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();

        }
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        if (dialog != null && dialog.isShowing()) {
            //// dialog.dismiss();
            ToastUtils.showShort(UIUtils.getContext(), "网络异常哦");
        }
    }

    private String getRunningActivityName(Activity activity) {

        String contextString = activity.toString();

        return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));

    }

}

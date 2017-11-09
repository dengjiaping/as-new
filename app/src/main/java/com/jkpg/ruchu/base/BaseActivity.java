package com.jkpg.ruchu.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.MainActivity;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by qindi on 2017/8/10.
 */

public class BaseActivity extends SwipeBackActivity {

    private AlertDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        Resources res = super.getResources();
        Configuration c = new Configuration();
//        c.setToDefaults();
        c.fontScale = 1f;
        res.updateConfiguration(c, res.getDisplayMetrics());
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        displayMetrics.scaledDensity = displayMetrics.density;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Resources res = super.getResources();
        Configuration c = new Configuration();
//        c.setToDefaults();
        c.fontScale = 1f;
        res.updateConfiguration(c, res.getDisplayMetrics());
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        displayMetrics.scaledDensity = displayMetrics.density;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showDialog(String ss) {
        if (ss.equals("onForceOffline")) {
             mDialog = new AlertDialog.Builder(this)
                    .setTitle("下线通知")
                    .setMessage("你的账号在另一台手机登录.")
                    .setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EventBus.getDefault().post("LoginIM");
                            dialog.dismiss();
                        }
                    })
                    .show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);

        } else if (ss.equals("onUserSigExpired")) {
            //                            dialog.dismiss();
            mDialog = new AlertDialog.Builder(this)
                    .setTitle("登录过期")
                    .setMessage("请重新登录你的账号.")
                    .setNegativeButton("退出应用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SPUtils.clear();
                            SPUtils.saveBoolean(UIUtils.getContext(), Constants.FIRST, false);
//                            dialog.dismiss();
                            Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                            Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                            startActivities(new Intent[]{intentMain, intentLogin});
                        }
                    }).show();
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dismissDialog(String ss) {
        if (ss.equals("TIMLogin")){
            if (mDialog != null && mDialog.isShowing()){
                mDialog.dismiss();
            }
        }
    }



}

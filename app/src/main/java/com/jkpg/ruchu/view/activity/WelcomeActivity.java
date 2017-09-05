package com.jkpg.ruchu.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.login.LoginActivity;

/**
 * Created by qindi on 2017/5/10.
 */

public class WelcomeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initBar();

        getWindow().setBackgroundDrawable(null);//背景置为空
//        ImageView imageView = (ImageView) findViewById(R.id.welcome_iv);
//        imageView.setBackgroundResource(R.drawable.welcome_bg);
//        //imageView.setFitsSystemWindows(true);
//        //imageView.setCropToPadding(true);

        MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SPUtils.getBoolean(UIUtils.getContext(), Constants.FIRST, true)) {
                    startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
                    finish();
                } else if (StringUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))) {
                    Intent intentLogin = new Intent(WelcomeActivity.this, LoginActivity.class);
                    Intent intentMain = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivities(new Intent[]{intentMain, intentLogin});
                    finish();
                } else {
                    Intent intentMain = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intentMain);
                    finish();
                }

            }
        }, 1600);
    }

    //优化用户体验，禁掉返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    private void initBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}

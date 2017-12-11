package com.jkpg.ruchu.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/10.
 */

public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.welcome)
    ImageView mWelcome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        initBar();
        if (SPUtils.getBoolean(UIUtils.getContext(), "needLogin", true)) {
            SPUtils.clear();
            SPUtils.saveBoolean(UIUtils.getContext(), "needLogin", false);
        }


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
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                }


            }
        }, 1200);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(android.R.id.content);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 19) {
            parentView.setFitsSystemWindows(false);
            parentView.setBackgroundColor(UIUtils.getColor(R.color.colorWhite));
        }
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
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}

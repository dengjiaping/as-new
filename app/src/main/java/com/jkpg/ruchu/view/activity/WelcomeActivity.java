package com.jkpg.ruchu.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.view.activity.login.LoginActivity;

/**
 * Created by qindi on 2017/5/10.
 */

public class WelcomeActivity extends AppCompatActivity {
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
                /*OkGo
                        .post(AppUrl.LOGINSTATUS)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .connTimeOut(3000)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Login login = new Gson().fromJson(s, Login.class);
                                if (!login.success) {
                                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                    finish();
                                } *//*else if(login.birth.equals("")) {
                                    startActivity(new Intent(WelcomeActivity.this, PerfectInfoActivity.class));
                                    finish();
                                } *//* else {
                                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                                    finish();
                                }
                            }

                            @Override
                            public void onError(Call call, Response response, Exception e) {
                                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                finish();
                            }
                        });


*/
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));

            }
        }, 1000);
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

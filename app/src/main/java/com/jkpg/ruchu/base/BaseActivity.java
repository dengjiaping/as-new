package com.jkpg.ruchu.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

/**
 * Created by qindi on 2017/8/10.
 */

public class BaseActivity extends AppCompatActivity {
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



}

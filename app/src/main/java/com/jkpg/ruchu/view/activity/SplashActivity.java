package com.jkpg.ruchu.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.jkpg.ruchu.widget.CircleIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/10.
 */

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.indicator)
    CircleIndicator mIndicator;

    private List<View> viewList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initData();
        initViewPager();
        initIndicator();
        initBar();


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

    private void initData() {
        viewList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            View view = new View(this);
            view.setBackgroundColor(0xff000000 | random.nextInt(0x00ffffff));
            viewList.add(view);
        }
    }

    private void initIndicator() {
        mIndicator.setViewPager(mViewpager);
    }


    private void initViewPager() {
        mViewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                if (viewList == null)
                    return 0;
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));

                return viewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == viewList.size() - 1) {
//                    SPUtils.saveBoolean(UIUtils.getContext(), "isFirst", false);

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                    LogUtils.i("" + position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}

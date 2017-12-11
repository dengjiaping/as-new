package com.jkpg.ruchu.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.jkpg.ruchu.widget.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/10.
 */

public class SplashActivity extends BaseActivity {

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
             getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void initData() {
        viewList = new ArrayList<>();
        View view = new View(this);
        view.setBackgroundResource(R.drawable.splash1);
        View view1 = new View(this);
        view1.setBackgroundResource(R.drawable.splash2);
        View view2 = new View(this);
        view2.setBackgroundResource(R.drawable.splash3);
        viewList.add(view);
        viewList.add(view1);
        viewList.add(view2);
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

                    viewList.get(position).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SPUtils.saveBoolean(UIUtils.getContext(), Constants.FIRST, false);
                            Intent intentLogin = new Intent(SplashActivity.this, LoginActivity.class);
                            Intent intentMain = new Intent(SplashActivity.this, MainActivity.class);
                            startActivities(new Intent[]{intentMain, intentLogin});
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(android.R.id.content);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 19) {
            parentView.setFitsSystemWindows(false);
//            parentView.setBackgroundResource(R.drawable.bg_layout);
        }
    }
}

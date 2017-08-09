package com.jkpg.ruchu.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.fragment.CommunityModuleFragment;
import com.jkpg.ruchu.view.fragment.MyFragment;
import com.jkpg.ruchu.view.fragment.TrainFragment;
import com.jkpg.ruchu.widget.BottomNavigationViewEx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by qindi on 2017/5/16.
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_bottom_navigation_view)
    BottomNavigationViewEx mMainBottomNavigationView;
    @BindView(R.id.main_frame_layout)
    FrameLayout mMainFrameLayout;
    @BindView(R.id.main_view_shop)
    View mMainViewShop;
    private MyFragment mMyFragment;
    //    private CommunityFragment mCommunityFragment;
    private CommunityModuleFragment mCommunityFragment;
    //    private ShopFragment mShopFragment;
    private TrainFragment mTrainFragment;
    private FragmentTransaction mFt;
    private int TAG = R.id.menu_train;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBottomNavigationView();
        if (savedInstanceState != null) {
            mTrainFragment = (TrainFragment) getSupportFragmentManager().findFragmentByTag("mTrainFragment");
            mCommunityFragment = (CommunityModuleFragment) getSupportFragmentManager().findFragmentByTag("mCommunityFragment");
//            mShopFragment = (ShopFragment) getSupportFragmentManager().findFragmentByTag("mShopFragment");
            mMyFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag("mMyFragment");
        }
        initShop();
        switchFragment(TAG);
    }

    private void initShop() {
        mMainViewShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShopActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("TAG", TAG);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        switchFragment(savedInstanceState.getInt("TAG"));
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void switchFragment(int tag) {
        mFt = getSupportFragmentManager().beginTransaction()/*.setCustomAnimations(
                android.R.anim.fade_in, android.R.anim.fade_out)*/;
        if (mMyFragment != null)
            mFt.hide(mMyFragment);
        if (mTrainFragment != null)
            mFt.hide(mTrainFragment);
       /* if (mShopFragment != null)
            mFt.hide(mShopFragment);*/
        if (mCommunityFragment != null)
            mFt.hide(mCommunityFragment);
        mFt.commit();
        switch (tag) {
            case R.id.menu_train:
//                        mFt.replace(R.id.main_frame_layout, mTrainFragment).commit();
                if (mTrainFragment == null) {
                    mTrainFragment = new TrainFragment();
                    mFt.add(R.id.main_frame_layout, mTrainFragment, "mTrainFragment");
                } else {
                    mFt.show(mTrainFragment);
                    LogUtils.i("mTrainFragment");
                }
                TAG = R.id.menu_train;
                break;
            case R.id.menu_shop:
//                        mFt.replace(R.id.main_frame_layout, mShopFragment).commit();
                /*if (mShopFragment == null) {
                    mShopFragment = new ShopFragment();
                    mFt.add(R.id.main_frame_layout, mShopFragment, "mShopFragment");
                } else {
                    mFt.show(mShopFragment);
                LogUtils.i("mShopFragment");
                }
                TAG = R.id.menu_shop;*/
                break;
            case R.id.menu_group:
//                        mFt.replace(R.id.main_frame_layout, mCommunityFragment).commit();
                if (mCommunityFragment == null) {
//                    mCommunityFragment = new CommunityFragment();
                    mCommunityFragment = new CommunityModuleFragment();
                    mFt.add(R.id.main_frame_layout, mCommunityFragment, "mCommunityFragment");
                } else {
                    mFt.show(mCommunityFragment);
                    LogUtils.i("mCommunityFragment");

                }

                TAG = R.id.menu_group;

                break;
            case R.id.menu_my:
//                        mFt.replace(R.id.main_frame_layout, mMyFragment).commit();
                if (mMyFragment == null) {
                    mMyFragment = new MyFragment();
                    mFt.add(R.id.main_frame_layout, mMyFragment, "mMyFragment");
                } else {
                    mFt.show(mMyFragment);
                    LogUtils.i("mMyFragment");

                }
                TAG = R.id.menu_my;

                break;
        }
    }

    private void initBottomNavigationView() {
//        mMainBottomNavigationView.setItemIconTintList(null);
        mMainBottomNavigationView.enableShiftingMode(false);
        mMainBottomNavigationView.enableItemShiftingMode(false);
        mMainBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switchFragment(item.getItemId());
                return true;
            }
        });
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(UIUtils.getContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventMass(String mess) {
        if (mess.equals("Train")) {
            switchFragment(R.id.menu_train);
        }
    }
}


package com.jkpg.ruchu.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.ExperienceBean;
import com.jkpg.ruchu.bean.MyMessageBean;
import com.jkpg.ruchu.bean.SmsEvent;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.PermissionUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.fragment.CommunityModuleFragment;
import com.jkpg.ruchu.view.fragment.MyFragment;
import com.jkpg.ruchu.view.fragment.TrainFragment;
import com.jkpg.ruchu.widget.BottomNavigationViewEx;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.vector.update_app.HttpManager;
import com.vector.update_app.UpdateAppManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by qindi on 2017/5/16.
 */

public class MainActivity extends BaseActivity {
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
    private String[] mMPermissionList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initBottomNavigationView();
        if (savedInstanceState != null) {
            mTrainFragment = (TrainFragment) getSupportFragmentManager().findFragmentByTag("mTrainFragment");
            mCommunityFragment = (CommunityModuleFragment) getSupportFragmentManager().findFragmentByTag("mCommunityFragment");
//            mShopFragment = (ShopFragment) getSupportFragmentManager().findFragmentByTag("mShopFragment");
            mMyFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag("mMyFragment");
        }
        initShop();
        switchFragment(TAG);
//        initSms();
//        Intent startIntent = new Intent(this, SmsService.class);
//        startService(startIntent);

        mMPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP,
                Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WRITE_APN_SETTINGS};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionUtils.requestPermissions(this, 333, mMPermissionList, new PermissionUtils.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {

                }

                @Override
                public void onPermissionDenied(String[] deniedPermissions) {
                    ToastUtils.showShort(UIUtils.getContext(), "拒绝后,App部分功能将受影响,请到应用设置中打开");
                }
            });
        }

        initUpdate();
    }


    private void initSms() {
        OkGo
                .post(AppUrl.MYMASSAGE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("flag", 0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MyMessageBean myMessageBean = new Gson().fromJson(s, MyMessageBean.class);
                        if (myMessageBean.notice || myMessageBean.reply || myMessageBean.zan) {
                            EventBus.getDefault().post(new SmsEvent(true));
                        } else {
                            EventBus.getDefault().post(new SmsEvent(false));
                        }
                    }
                });
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
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelAll();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventMass(String mess) {
        if (mess.equals("Train")) {
            switchFragment(R.id.menu_train);
            mMainBottomNavigationView.setCurrentItem(0);
        }
        if (mess.equals("initSms")) {
            initSms();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initSms();
        EventBus.getDefault().post(new ExperienceBean());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionUtils.onRequestPermissionsResult(this, 333, mMPermissionList);
    }

    private void initUpdate() {
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(this)
                //更新地址
                .setUpdateUrl(AppUrl.UPDATEAPP)
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .build()
                .update();

    }


    private class UpdateAppHttpUtil implements HttpManager {
        @Override
        public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
            OkGo
                    .post(url)
                    .params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            LogUtils.d(s+"---------");
                            callBack.onResponse(s);
                        }
                    });
        }

        @Override
        public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
            OkGo
                    .post(url)
                    .params(params)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            LogUtils.d(s+"----------");
                            callBack.onResponse(s);
                        }
                    });
        }

        @Override
        public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
            OkGo
                    .get(url)
                    .execute(new com.lzy.okgo.callback.FileCallback(path, fileName) {
                        @Override
                        public void onSuccess(File file, Call call, Response response) {
                            callback.onResponse(file);
                        }

                        @Override
                        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                            super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                            callback.onProgress(progress, totalSize);
                        }

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                            callback.onBefore();
                        }
                    });
        }
    }
}


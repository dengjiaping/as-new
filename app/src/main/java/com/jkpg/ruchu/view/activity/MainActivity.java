package com.jkpg.ruchu.view.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.ExperienceBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.MyMessageBean;
import com.jkpg.ruchu.bean.SmsEvent;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.PermissionUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.my.MySMSActivity;
import com.jkpg.ruchu.view.fragment.CommunityModuleFragment;
import com.jkpg.ruchu.view.fragment.MyFragment;
import com.jkpg.ruchu.view.fragment.TrainFragment;
import com.jkpg.ruchu.widget.BottomNavigationViewEx;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMOfflinePushSettings;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.vector.update_app.HttpManager;
import com.vector.update_app.UpdateAppManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        setSwipeBackEnable(false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initBottomNavigationView();
        if (savedInstanceState != null) {
            mTrainFragment = (TrainFragment) getSupportFragmentManager().findFragmentByTag("mTrainFragment");
            mCommunityFragment = (CommunityModuleFragment) getSupportFragmentManager().findFragmentByTag("mCommunityFragment");
            mMyFragment = (MyFragment) getSupportFragmentManager().findFragmentByTag("mMyFragment");
        }
        initShop();
        switchFragment(TAG);

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
        initIM();

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
        if (mCommunityFragment != null)
            mFt.hide(mCommunityFragment);
        mFt.commit();
        switch (tag) {
            case R.id.menu_train:
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
                break;
            case R.id.menu_group:
                if (mCommunityFragment == null) {
                    mCommunityFragment = new CommunityModuleFragment();
                    mFt.add(R.id.main_frame_layout, mCommunityFragment, "mCommunityFragment");
                } else {
                    mFt.show(mCommunityFragment);
                    LogUtils.i("mCommunityFragment");
                }

                TAG = R.id.menu_group;

                break;
            case R.id.menu_my:
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
                            LogUtils.d(s + "---------");
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
                            LogUtils.d(s + "----------");
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


    private void initIM() {

        //设置消息监听器，收到新消息时，通过此监听器回调
        TIMManager.getInstance().addMessageListener(new TIMMessageListener() {//消息监听器
            @Override
            public boolean onNewMessages(List<TIMMessage> list) {
                LogUtils.d("-------新消息");

                ArrayList<TIMMessage> timMessages = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    TIMElem element = list.get(i).getElement(0);
                    TIMElemType elemType = element.getType();
                    if (elemType == TIMElemType.Text || elemType == TIMElemType.Image || elemType == TIMElemType.Custom) {
                        timMessages.add(list.get(i));
                        EventBus.getDefault().post(new SmsEvent(true));
                    }
                }
                EventBus.getDefault().post(timMessages);

//                LogUtils.d("topActivityInfo  " + getTopActivityInfo());
                if (SPUtils.getBoolean(UIUtils.getContext(), "IMSend", true)) {
                    final TIMMessage timMessage = list.get(0);
                    if (timMessage.getElement(0).getType() == TIMElemType.Text
                            || timMessage.getElement(0).getType() == TIMElemType.Image
                            || timMessage.getElement(0).getType() == TIMElemType.Custom) {

                        if (timMessage.isSelf() || getTopActivityInfo().equals("com.jkpg.ruchu.view.activity.my.MySMSActivity")
                                || getTopActivityInfo().equals("com.jkpg.ruchu.view.activity.ChatListActivity")) {
                        } else {
                            final String[] contentStr = {""};
                            final String[] sender = {""};
                            //待获取用户资料的用户列表
                            List<String> users = new ArrayList<>();
                            users.add(timMessage.getSender());
                            //获取用户资料
                            TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
                                @Override
                                public void onError(int code, String desc) {
                                }

                                @Override
                                public void onSuccess(List<TIMUserProfile> result) {
                                    sender[0] = result.get(0).getNickName();
                                    for (int i = 0; i < timMessage.getElementCount(); ++i) {
                                        TIMElem elem = timMessage.getElement(i);

                                        //获取当前元素的类型
                                        TIMElemType elemType = elem.getType();
                                        LogUtils.d("elem type: " + elemType.name());
                                        if (elemType == TIMElemType.Text) {
                                            contentStr[0] = ((TIMTextElem) elem).getText();
                                            //处理文本消息
                                        } else if (elemType == TIMElemType.Image) {
                                            contentStr[0] = "[图片]";
                                            //处理图片消息
                                        } else if (elemType == TIMElemType.Custom) {
                                            contentStr[0] = ((TIMCustomElem) elem).getDesc();
                                        }
                                    }
                                    showPush(sender[0], contentStr[0]);
                                }
                            });

                        }
                    }
                }
                //消息的内容解析请参考消息收发文档中的消息解析说明
                return true; //返回true将终止回调链，不再调用下一个新消息监听器
            }

        });


        loginIM();
    }

    private void OfflinePush() {
        TIMOfflinePushSettings settings = new TIMOfflinePushSettings();
        //开启离线推送
        settings.setEnabled(true);
        //设置收到C2C离线消息时的提示声音，这里把声音文件放到了res/raw文件夹下
        settings.setC2cMsgRemindSound(null);
        TIMManager.getInstance().setOfflinePushSettings(settings);
    }

    public void loginIM() {
        /** 登录
         * @param identifier 用户帐号
         * @param userSig userSig，用户帐号签名，由私钥加密获得，具体请参考文档
         * @param callback 回调接口
         *  */
        TIMManager.getInstance().login(
                SPUtils.getString(UIUtils.getContext(), Constants.IMID, "")
                , SPUtils.getString(UIUtils.getContext(), Constants.IMSIGN, "")
                , new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                        //错误码code和错误描述desc，可用于定位请求失败原因
                        //错误码code列表请参见错误码表
                        LogUtils.d("login failed. code: " + code + " errmsg: " + desc);

                    }

                    @Override
                    public void onSuccess() {
                        OfflinePush();

                        LogUtils.d("login success");

                        EventBus.getDefault().post("TIMLogin");
                        //获取自己的资料
                        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
                            @Override
                            public void onError(int code, String desc) {
                            }

                            @Override
                            public void onSuccess(TIMUserProfile result) {
                                SPUtils.saveString(UIUtils.getContext(), Constants.IMIMAGE, result.getFaceUrl());
                            }
                        });
                    }
                });


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void needLogin(String ss) {
        if (ss.equals("LoginIM")) {
            loginIM();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        /* Do something */
        if (event.message.equals("Login")) {
            MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loginIM();
                }
            }, 500);
        }
    }

    private String getTopActivityInfo() {
        String activity = "";
        ActivityManager manager = ((ActivityManager) UIUtils.getContext().getSystemService(Context.ACTIVITY_SERVICE));
        List localList = manager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo localRunningTaskInfo = (ActivityManager.RunningTaskInfo) localList.get(0);
        activity = localRunningTaskInfo.topActivity.getClassName();
//        }
        return activity;
    }

    public void showPush(String senderStr, String contentStr) {
        NotificationManager mNotificationManager = (NotificationManager) MyApplication.getContext().getSystemService(UIUtils.getContext().NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyApplication.getContext());
        Intent notificationIntent = new Intent(MyApplication.getContext(), MySMSActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(MyApplication.getContext(), 0,
                notificationIntent, 0);
        mBuilder.setContentTitle(senderStr)//设置通知栏标题
                .setContentText(contentStr)
                .setContentIntent(intent) //设置通知栏点击意图
//                .setNumber(++pushNum) //设置通知集合的数量
                .setTicker(senderStr + ":" + contentStr) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON
        Notification notify = mBuilder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(1, notify);
    }
}


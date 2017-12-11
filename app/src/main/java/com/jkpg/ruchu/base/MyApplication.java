package com.jkpg.ruchu.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.Md5Utils;
import com.jkpg.ruchu.widget.nineview.NineGridView;
import com.lzy.okgo.OkGo;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMFriendshipSettings;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdk.basic.YouzanBasicSDKAdapter;

import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by qindi on 2017/4/25.
 */

public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static Handler mMainThreadHandler;
    private static String deviceToken;

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate() {
        //上下文
        mContext = getApplicationContext();

        //主线程的Handler
        mMainThreadHandler = new Handler();

        super.onCreate();

        OkGo.init(this);
        OkGo
                .getInstance()
//                .debug("OkGo", Level.INFO, true)
//                 .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                .setRetryCount(3)
                .setCertificates();//信任所有证书
//        .setCertificates(getAssets().open("ruchu.cer"))使用预埋证书，校验服务端证书（自签名证书）
        NineGridView.setImageLoader(new GlideLoader());

        PlatformConfig.setWeixin(Constants.WX_APP_ID, Constants.WX_SECRET);
        PlatformConfig.setQQZone(Constants.QQ_APP_ID, Constants.QQ_SECRET);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, Constants.UMENG_MESSAGE_SECRET);

        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
        PushAgent mPushAgent = PushAgent.getInstance(this);

//注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                MyApplication.deviceToken = deviceToken;
//                SPUtils.saveString(UIUtils.getContext(), Constants.DEVICETOKEN, deviceToken);
                LogUtils.i("deviceToken" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.d(s+"onFailure"+ s1);
            }
        });
        mPushAgent.setPushCheck(true);
        mPushAgent.setDisplayNotificationNumber(1);
        MiPushRegistar.register(this, Constants.XIAOMI_ID, Constants.XIAOMI_KEY);
        HuaWeiRegister.register(this);

//        YouzanSDK.init(this, Constants.YZ_CLIENT_ID);
        YouzanSDK.init(this, Constants.YZ_CLIENT_ID, new  YouzanBasicSDKAdapter());

        Resources res = super.getResources();
        Configuration c = new Configuration();
        c.fontScale = 1f;
        res.updateConfiguration(c, res.getDisplayMetrics());


        if (MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
                    }
                }
            });
        }
        //TIM初始化SDK基本配置
        TIMSdkConfig timConfig = new TIMSdkConfig(Constants.sdkAppid);
        timConfig.enableLogPrint(false)
                .setLogLevel(TIMLogLevel.OFF)
                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/RuChu/");

        //初始化SDK
        boolean init = TIMManager.getInstance().init(getApplicationContext(), timConfig);
        LogUtils.d("TIMManager init = " + init);


        //设置资料关系链拉取字段，这里只关心好友验证类型、头像URL、昵称和自定义字段"Tag_Profile_Custom_Test"
        TIMFriendshipSettings settings = new TIMFriendshipSettings();
        long flags = 0;
        flags |= TIMFriendshipManager.TIM_PROFILE_FLAG_NICK
                | TIMFriendshipManager.TIM_PROFILE_FLAG_FACE_URL;
        settings.setFlags(flags);
        settings.addCustomTag("Tag_Profile_Custom_ruchuxzs");

        TIMUserConfig userConfig = new TIMUserConfig()
                .setFriendshipSettings(settings)
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        //被其他终端踢下线
                        LogUtils.i("onForceOffline");
                        EventBus.getDefault().post("onForceOffline");

                    }

                    @Override
                    public void onUserSigExpired() {
                        //用户签名过期了，需要刷新userSig重新登录SDK
                        LogUtils.i("onUserSigExpired");
                        EventBus.getDefault().post("onUserSigExpired");


                    }
                })
                //设置连接状态事件监听器
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        LogUtils.i("onConnected");

                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
                        LogUtils.i("onDisconnected--" + code + "--" + desc);

                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
                        LogUtils.i("onWifiNeedAuth--" + name);

                    }
                })
                //设置会话刷新监听器
                .setRefreshListener(new TIMRefreshListener() {
                    @Override
                    public void onRefresh() {
                        LogUtils.i(" 会话刷新监听器  onRefresh");
                    }

                    @Override
                    public void onRefreshConversation(List<TIMConversation> conversations) {
                        LogUtils.i("onRefreshConversation, conversation size: " + conversations.size());
                    }
                });


        //将用户配置与通讯管理器进行绑定
        TIMManager.getInstance().setUserConfig(userConfig);

    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }


    /**
     * 得到主线程里面的创建的一个hanlder
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    class GlideLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .placeholder(R.drawable.gray_bg)
                    .error(R.drawable.gray_bg)//
                    .crossFade()
                    .centerCrop()
                    .thumbnail(0.3f)
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        MyApplication app = (MyApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        HttpProxyCacheServer proxy = new HttpProxyCacheServer.Builder(this)
                .fileNameGenerator(new MyFileNameGenerator())
                .build();
        return proxy;
    }

    public class MyFileNameGenerator implements FileNameGenerator {

        public String generate(String url) {
            String md5 = Md5Utils.getMD5(url);
            return md5 + ".mp4";
        }
    }

    public static String getDeviceToken() {
        return deviceToken;
    }
}

package com.jkpg.ruchu.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.FileNameGenerator;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.Md5Utils;
import com.jkpg.ruchu.widget.nineview.NineGridView;
import com.lzy.okgo.OkGo;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.youzan.sdk.YouzanSDK;

import java.util.logging.Level;

/**
 * Created by qindi on 2017/4/25.
 */

public class MyApplication extends Application {

    private static Context mContext;
    private static Handler mMainThreadHandler;
    private static String userId;


    @Override
    public void onCreate() {
      /*  if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...*/
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        //上下文
        mContext = getApplicationContext();

        //主线程的Handler
        mMainThreadHandler = new Handler();

        super.onCreate();

        OkGo.init(this);
        OkGo
                .getInstance()
                .debug("OkGo", Level.INFO, true)
//                 .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                .setRetryCount(3)
                .setCertificates();//信任所有证书
//        .setCertificates(getAssets().open("ruchu.cer"))使用预埋证书，校验服务端证书（自签名证书）
        NineGridView.setImageLoader(new GlideLoader());

        PlatformConfig.setWeixin(Constants.WX_APP_ID, Constants.WX_SECRET);
        PlatformConfig.setQQZone(Constants.QQ_APP_ID, Constants.QQ_SECRET);
        UMShareAPI.get(this);
//        Config.isJumptoAppStore = true;
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //通知栏可以设置最多显示通知的条数，当有新通知到达时，会把旧的通知隐藏。
        //mPushAgent.setDisplayNotificationNumber(0);
        mPushAgent.setDebugMode(true);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });


        YouzanSDK.init(this, Constants.YZ_CLIENT_ID);


    }

    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        MyApplication.userId = userId;
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
                    .error(R.drawable.photo_error)//
                    .crossFade()
                    .thumbnail(0.5f)
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

}

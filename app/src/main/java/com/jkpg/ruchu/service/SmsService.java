package com.jkpg.ruchu.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.MyMessageBean;
import com.jkpg.ruchu.bean.SmsEvent;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.my.MySMSActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/8/24.
 */

public class SmsService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {

            public void run() {
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
                                    Notification.Builder builder = new Notification.Builder(SmsService.this);
                                    Intent intent = new Intent(SmsService.this, MySMSActivity.class);  //需要跳转指定的页面
                                    PendingIntent pendingIntent = PendingIntent.getActivity(SmsService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    builder.setContentIntent(pendingIntent);
                                    builder.setSmallIcon(R.mipmap.ic_launcher);// 设置图标
                                    builder.setContentTitle("我的消息");// 设置通知的标题
                                    builder.setContentText("你有未读消息");// 设置通知的内容
                                    builder.setWhen(System.currentTimeMillis());// 设置通知来到的时间
                                    builder.setAutoCancel(true); //自己维护通知的消失
                                    builder.setTicker("你有未读消息");// 第一次提示消失的时候显示在通知栏上的
                                    builder.setOngoing(true);
                                    Notification notification = builder.build();
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;  //只有全部清除时，Notification才会清除
                                    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
                                } else {
                                    EventBus.getDefault().post(new SmsEvent(false));
                                }

                            }
                        });
                LogUtils.d("哈哈哈哈");
                handler.postDelayed(this, 1000 * 60 * 10);
            }

        };

        handler.postDelayed(runnable, 1000 * 60 * 10);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

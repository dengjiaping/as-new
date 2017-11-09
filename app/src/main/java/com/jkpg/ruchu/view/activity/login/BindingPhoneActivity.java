package com.jkpg.ruchu.view.activity.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.CodeBean;
import com.jkpg.ruchu.bean.LoginQQBean;
import com.jkpg.ruchu.bean.LoginStateBean;
import com.jkpg.ruchu.bean.LoginWxBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.showBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.Md5Utils;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.RegexUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/7/21.
 */

public class BindingPhoneActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_tv_right)
    TextView mHeaderTvRight;
    @BindView(R.id.forget_et_phone)
    EditText mForgetEtPhone;
    @BindView(R.id.forget_et_code)
    EditText mForgetEtCode;
    @BindView(R.id.forget_tv_send)
    TextView mForgetTvSend;
    @BindView(R.id.binding_et_pwd)
    EditText mBindingEtPwd;
    @BindView(R.id.forget_bt_next)
    Button mForgetBtNext;
    @BindView(R.id.binding_ll_pwd)
    LinearLayout mBindingLl;
    private static final int CODE_ING = 1;//已发送，倒计时
    private static final int CODE_REPEAT = 2;//重新发送
    private int TIME = 60;//倒计时60s
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_ING://已发送,倒计时
                    mForgetTvSend.setText("重新发送(" + --TIME + "s)");
                    mForgetTvSend.setClickable(false);
                    break;
                case CODE_REPEAT://重新发送
                    mForgetTvSend.setText("获取验证码");
                    mForgetTvSend.setClickable(true);
                    TIME = 60;
                    break;
            }
        }
    };
    private String mUid = "";
    private String mName = "";
    private String mIconurl = "";
    private String mUnionid = "";
    private String flag = 3 + "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_phone);
        ButterKnife.bind(this);
        setSwipeBackEnable(false);

        initHeader();
        mUid = getIntent().getStringExtra("uid");
        mName = getIntent().getStringExtra("name");
        mIconurl = getIntent().getStringExtra("iconurl");
        mUnionid = getIntent().getStringExtra("unionid");

    }

    private void initHeader() {
        mHeaderTvTitle.setText("绑定手机号");
        mHeaderIvLeft.setVisibility(View.GONE);
        mHeaderTvRight.setText("跳过");
    }

    @OnClick({R.id.header_tv_right, R.id.forget_tv_send, R.id.forget_bt_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_tv_right:
                if (StringUtils.isEmpty(mUid)) {
                    OkGo
                            .post(AppUrl.WXLOGIN)
                            .params("unionid", mUnionid)
                            .params("phoneflag", 1)
                            .params("devicetoken",MyApplication.getDeviceToken())
                            .params("appwxnikename", mName)
                            .params("appwxurlimage", mIconurl)
                            .params("tele", "")
                            .params("password", "")
                            .params("flag", "3")
                            .execute(new StringDialogCallback(BindingPhoneActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {

                                    LoginWxBean loginWxBean = new Gson().fromJson(s, LoginWxBean.class);

                                    if (loginWxBean.giveVIP) {
                                        LogUtils.d("giveVIP3" + loginWxBean.giveVIP);
                                        EventBus.getDefault().post(new showBean("showMess"));
                                        showMess();
                                    }
                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERID, loginWxBean.backMess.userId);
                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERNANE, loginWxBean.backMess.uNick);
                                    SPUtils.saveString(UIUtils.getContext(), Constants.IMID, loginWxBean.nameid);
                                    SPUtils.saveString(UIUtils.getContext(), Constants.IMSIGN, loginWxBean.usersign);
//                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERIMAGE, loginWxBean.backMess.uImgurl);
                                    EventBus.getDefault().post(new MessageEvent("Login"));
                                    startActivity(new Intent(BindingPhoneActivity.this, PerfectInfoActivity.class));
                                    finish();
                                }
                            });
                } else {
                    OkGo
                            .post(AppUrl.QQLOGIN)
                            .params("uid", mUid)
                            .params("phoneflag", 1)
                            .params("appqqnikename", mName)
                            .params("appqqurlimage", mIconurl)
                            .params("tele", "")
                            .params("password", "")
                            .params("flag", "3")
                            .execute(new StringDialogCallback(BindingPhoneActivity.this) {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {

                                    LoginQQBean loginQqBean = new Gson().fromJson(s, LoginQQBean.class);
                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERID, loginQqBean.backMess.userId);
                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERNANE, loginQqBean.backMess.uNick);
                                    SPUtils.saveString(UIUtils.getContext(), Constants.IMID, loginQqBean.nameid);
                                    SPUtils.saveString(UIUtils.getContext(), Constants.IMSIGN, loginQqBean.usersign);
                                    EventBus.getDefault().post(new MessageEvent("Login"));
                                    startActivity(new Intent(BindingPhoneActivity.this, PerfectInfoActivity.class));
                                    finish();

                                }
                            });
                }
                break;
            case R.id.forget_tv_send:
                sendSMS();
                break;
            case R.id.forget_bt_next:
                next();
                break;
        }
    }


    private void next() {
        final String phone = mForgetEtPhone.getText().toString();
        String mycode = mForgetEtCode.getText().toString();
        if (StringUtils.isEmpty(mycode)) {
            ToastUtils.showShort(UIUtils.getContext(), "请输入验证码");
            return;
        }
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
            return;
        }
        OkGo
                .post(AppUrl.CODE)
                .params("tele", phone)
                .params("code", mycode)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        CodeBean code = new Gson().fromJson(s, CodeBean.class);
                        if (code.success) {
                            String password = "";
                            if (mBindingLl.getVisibility() == View.VISIBLE) {
                                if (StringUtils.isEmpty(mBindingEtPwd.getText().toString())) {

                                    ToastUtils.showShort(UIUtils.getContext(), "请输入密码");
                                    return;
                                }
                                password = mBindingEtPwd.getText().toString();
                                password = Md5Utils.getMD5(password);
                            }
                            if (StringUtils.isEmpty(mUid)) {
                                OkGo
                                        .post(AppUrl.WXLOGIN)
                                        .params("unionid", mUnionid)
                                        .params("phoneflag", 1)
                                        .params("appwxnikename", mName)
                                        .params("appwxurlimage", mIconurl)
                                        .params("tele", phone)
                                        .params("devicetoken",MyApplication.getDeviceToken())
                                        .params("password", password)
                                        .params("flag", flag)
                                        .execute(new StringDialogCallback(BindingPhoneActivity.this) {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {
                                                LoginWxBean loginWxBean = new Gson().fromJson(s, LoginWxBean.class);
                                                LogUtils.d("giveVIP" + loginWxBean.giveVIP);
                                                LogUtils.d("giveVIP" + flag);
                                                if (loginWxBean.giveVIP) {
                                                    EventBus.getDefault().post(new showBean("showMess"));
                                                    showMess();
                                                }
                                                SPUtils.saveString(UIUtils.getContext(), Constants.USERID, loginWxBean.backMess.userId);
                                                SPUtils.saveString(UIUtils.getContext(), Constants.USERNANE, loginWxBean.backMess.uNick);
                                                SPUtils.saveString(UIUtils.getContext(), Constants.IMID, loginWxBean.nameid);
                                                SPUtils.saveString(UIUtils.getContext(), Constants.IMSIGN, loginWxBean.usersign);
                                                EventBus.getDefault().post(new MessageEvent("Login"));
                                                showDialogSuccess();
                                            }
                                        });
                            } else {
                                OkGo
                                        .post(AppUrl.QQLOGIN)
                                        .params("uid", mUid)
                                        .params("phoneflag", 1)

                                        .params("appqqnikename", mName)
                                        .params("appqqurlimage", mIconurl)
                                        .params("tele", phone)
                                        .params("password", password)
                                        .params("flag", flag)
                                        .execute(new StringDialogCallback(BindingPhoneActivity.this) {
                                            @Override
                                            public void onSuccess(String s, Call call, Response response) {

                                                LoginQQBean loginQqBean = new Gson().fromJson(s, LoginQQBean.class);
                                                SPUtils.saveString(UIUtils.getContext(), Constants.USERID, loginQqBean.backMess.userId);
                                                SPUtils.saveString(UIUtils.getContext(), Constants.USERNANE, loginQqBean.backMess.uNick);
                                                SPUtils.saveString(UIUtils.getContext(), Constants.IMID, loginQqBean.nameid);
                                                SPUtils.saveString(UIUtils.getContext(), Constants.IMSIGN, loginQqBean.usersign);
                                                EventBus.getDefault().post(new MessageEvent("Login"));
                                                showDialogSuccess();
                                            }
                                        });

                            }
                        } else if (code.error.equals("05")) {
                            ToastUtils.showShort(UIUtils.getContext(), "验证码错误");
                        } else if (code.error.equals("01")) {
                            ToastUtils.showShort(UIUtils.getContext(), "未找到该用户");
                        }
                    }
                });
        //        String password = "";
        //        if (mForgetBtNext.getVisibility() == View.VISIBLE) {
        //            if (StringUtils.isEmpty(mBindingEtPwd.getText().toString())) {
        //
        //                ToastUtils.showShort(UIUtils.getContext(), "请输入密码");
        //                return;
        //            }
        //            password = mBindingEtPwd.getText().toString();
        //            password = Md5Utils.getMD5(password);
        //        }
        //        if (StringUtils.isEmpty(mUid)) {
        //            OkGo
        //                    .post(AppUrl.WXLOGIN)
        //                    .params("unionid", mUnionid)
        //                    .params("appwxnikename", mName)
        //                    .params("appwxurlimage", mIconurl)
        //                    .params("tele", phone)
        //                    .params("password", password)
        //                    .params("flag", flag)
        //                    .execute(new StringDialogCallback(BindingPhoneActivity.this) {
        //                        @Override
        //                        public void onSuccess(String s, Call call, Response response) {
        //                            LoginWxBean loginWxBean = new Gson().fromJson(s, LoginWxBean.class);
        //                            LogUtils.d("giveVIP" + loginWxBean.giveVIP);
        //                            LogUtils.d("giveVIP" + flag);
        //                            if (loginWxBean.giveVIP) {
        //                                EventBus.getDefault().post(new showBean("showMess"));
        //                                showMess();
        //                            }
//                            SPUtils.saveString(UIUtils.getContext(), Constants.USERID, loginWxBean.backMess.userId);
//                            SPUtils.saveString(UIUtils.getContext(), Constants.USERNANE, loginWxBean.backMess.uNick);
//                            SPUtils.saveString(UIUtils.getContext(), Constants.USERIMAGE, loginWxBean.backMess.uImgurl);
//                            EventBus.getDefault().post(new MessageEvent("Login"));
//                            showDialogSuccess();
//                        }
//                    });
//        } else {
//            OkGo
//                    .post(AppUrl.QQLOGIN)
//                    .params("uid", mUid)
//                    .params("appwxnikename", mName)
//                    .params("appwxurlimage", mIconurl)
//                    .params("tele", phone)
//                    .params("password", password)
//                    .params("flag", flag)
//                    .execute(new StringDialogCallback(BindingPhoneActivity.this) {
//                        @Override
//                        public void onSuccess(String s, Call call, Response response) {
//
//                            LoginQQBean loginQqBean = new Gson().fromJson(s, LoginQQBean.class);
//                            SPUtils.saveString(UIUtils.getContext(), Constants.USERID, loginQqBean.backMess.userId);
//                            SPUtils.saveString(UIUtils.getContext(), Constants.USERNANE, loginQqBean.backMess.uNick);
//                            SPUtils.saveString(UIUtils.getContext(), Constants.USERIMAGE, loginQqBean.backMess.uImgurl);
//                            EventBus.getDefault().post(new MessageEvent("Login"));
//                            showDialogSuccess();
//                        }
//                    });
//
//        }


    }

    private void showMess() {
//        Notification.Builder builder = new Notification.Builder(BindingPhoneActivity.this);
//        Intent intent = new Intent(BindingPhoneActivity.this, VipManageActivity.class);  //需要跳转指定的页面
//        PendingIntent pendingIntent = PendingIntent.getActivity(BindingPhoneActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//        builder.setSmallIcon(R.mipmap.ic_launcher);// 设置图标
//        builder.setContentTitle(getString(R.string.vipTipHeader));// 设置通知的标题
//        builder.setContentText(getString(R.string.vipTip));// 设置通知的内容
//        builder.setWhen(System.currentTimeMillis());// 设置通知来到的时间
//        builder.setAutoCancel(true); //自己维护通知的消失
//        builder.setTicker(getString(R.string.vipTip));// 第一次提示消失的时候显示在通知栏上的
//        builder.setOngoing(true);
//        Notification notification = builder.build();
//        notification.flags = Notification.FLAG_AUTO_CANCEL;  //只有全部清除时，Notification才会清除
//        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
    }

    private void sendSMS() {
        final String phone = mForgetEtPhone.getText().toString();
        if (!RegexUtils.isMatch(RegexUtils.REGEX_PHONE, phone)) {
            ToastUtils.showShort(UIUtils.getContext(), "请输入正确的手机号");
            return;
        }
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
            return;
        }
        if (StringUtils.isEmpty(mUid)) {
            OkGo
                    .post(AppUrl.SELECTTELEUNIONID)
                    .params("tele", phone)
                    .execute(new StringDialogCallback(BindingPhoneActivity.this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            LoginStateBean loginStateBean = new Gson().fromJson(s, LoginStateBean.class);
                            switch (loginStateBean.status) {
                                case 1:
                                    ToastUtils.showShort(UIUtils.getContext(), "该手机号已绑定其他QQ");
                                    mForgetBtNext.setClickable(false);

                                    break;
                                case 2:
                                    mBindingLl.setVisibility(View.GONE);
                                    mForgetBtNext.setClickable(true);
                                    OkGo
                                            .post(AppUrl.SMS)
                                            .params("tele", phone)
                                            .execute(new StringDialogCallback(BindingPhoneActivity.this) {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    CodeBean codeBean = new Gson().fromJson(s, CodeBean.class);
                                                    if (!codeBean.success) {
                                                        ToastUtils.showShort(UIUtils.getContext(), "验证码请求超过5次,请明天重试");
                                                        return;
                                                    }
                                                    time();
                                                }
                                            });
                                    flag = 2 + "";
                                    break;
                                case 3:
                                    mBindingLl.setVisibility(View.VISIBLE);
                                    mForgetBtNext.setClickable(true);
                                    OkGo
                                            .post(AppUrl.SMS)
                                            .params("tele", phone)
                                            .execute(new StringDialogCallback(BindingPhoneActivity.this) {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    CodeBean codeBean = new Gson().fromJson(s, CodeBean.class);
                                                    if (!codeBean.success) {
                                                        ToastUtils.showShort(UIUtils.getContext(), "验证码请求超过5次,请明天重试");
                                                        return;
                                                    }
                                                    time();
                                                }
                                            });
                                    flag = 3 + "";

                                    break;
                            }
                        }
                    });
        } else {
            OkGo
                    .post(AppUrl.SELECTTELEQQUID)
                    .params("tele", phone)
                    .execute(new StringDialogCallback(BindingPhoneActivity.this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            LoginStateBean loginStateBean = new Gson().fromJson(s, LoginStateBean.class);
                            switch (loginStateBean.status) {
                                case 1:
                                    ToastUtils.showShort(UIUtils.getContext(), "该手机号已绑定其他微信");
                                    mForgetBtNext.setClickable(false);
                                    break;
                                case 2:
                                    mBindingLl.setVisibility(View.GONE);
                                    mForgetBtNext.setClickable(true);
                                    OkGo
                                            .post(AppUrl.SMS)
                                            .params("tele", phone)
                                            .execute(new StringDialogCallback(BindingPhoneActivity.this) {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    CodeBean codeBean = new Gson().fromJson(s, CodeBean.class);
                                                    if (!codeBean.success) {
                                                        ToastUtils.showShort(UIUtils.getContext(), "验证码请求超过5次,请明天重试");
                                                        return;
                                                    }
                                                    time();
                                                }
                                            });
                                    flag = 2 + "";
                                    break;
                                case 3:
                                    mBindingLl.setVisibility(View.VISIBLE);
                                    mForgetBtNext.setClickable(true);
                                    OkGo
                                            .post(AppUrl.SMS)
                                            .params("tele", phone)
                                            .execute(new StringDialogCallback(BindingPhoneActivity.this) {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    CodeBean codeBean = new Gson().fromJson(s, CodeBean.class);
                                                    if (!codeBean.success) {
                                                        ToastUtils.showShort(UIUtils.getContext(), "验证码请求超过5次,请明天重试");
                                                        return;
                                                    }
                                                    time();
                                                }
                                            });
                                    flag = 3 + "";

                                    break;
                            }
                        }
                    });

        }


    }

    private void time() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 60; i > 0; i--) {
                    handler.sendEmptyMessage(CODE_ING);
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(CODE_REPEAT);
            }
        }).start();
    }

    private void showDialogSuccess() {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_show_success, null);
        ((TextView) view.findViewById(R.id.show_success_text)).setText("手机号绑定成功");
        AlertDialog show = new AlertDialog.Builder(this)
                .setView(view)
                .show();

        MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag.equals("2")) {
                    finish();
                } else {
                    startActivity(new Intent(BindingPhoneActivity.this, PerfectInfoActivity.class));
                    finish();
                }
            }
        }, 3000);
        show.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (flag.equals("2")) {
                    finish();
                } else {
                    startActivity(new Intent(BindingPhoneActivity.this, PerfectInfoActivity.class));
                    finish();
                }
            }
        });
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                Toast.makeText(UIUtils.getContext(), "确定要放弃注册吗", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

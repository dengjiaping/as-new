package com.jkpg.ruchu.view.activity.my;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.AccountManagementBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.login.LoginActivity;
import com.lzy.okgo.OkGo;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/25.
 */

public class AccountManagementActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.account_manage_rb_wx)
    CheckBox mAccountManageRbWx;
    @BindView(R.id.account_manage_rb_qq)
    CheckBox mAccountManageRbQq;
    @BindView(R.id.account_manage_tv_phone)
    TextView mAccountManageTvPhone;

    private AccountManagementBean mAccountManagementBean;
    private boolean isQQ;
    private boolean isWX;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        ButterKnife.bind(this);
        initHeader();

        initData();
    }

    private void initData() {
        OkGo
                .post(AppUrl.ACCOUNT_MANAGE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringDialogCallback(this) {


                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mAccountManagementBean = new Gson().fromJson(s, AccountManagementBean.class);
                        if (mAccountManagementBean.telflag) {
                            String tele = mAccountManagementBean.tele;
                            tele = tele.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                            mAccountManageTvPhone.setText(tele);
                        } else {
                            mAccountManageTvPhone.setText("点击绑定手机号");
                        }
                        isQQ = mAccountManagementBean.qqflag;
                        if (mAccountManagementBean.qqflag) {
                            mAccountManageRbQq.setChecked(false);
                            mAccountManageRbQq.setText("已绑定");

                        }
                        isWX = mAccountManagementBean.wxflag;
                        if (mAccountManagementBean.wxflag) {
                            mAccountManageRbWx.setChecked(false);
                            mAccountManageRbWx.setText("已绑定");
                        }
                    }
                });
    }

    private void initHeader() {
        mHeaderTvTitle.setText("账号管理");
    }

    @OnClick({R.id.account_manage_btn_logout, R.id.header_iv_left, R.id.account_manage_btn_change, R.id.account_manage_tv_pwd, R.id.account_manage_ll_wx, R.id.account_manage_ll_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.account_manage_btn_change:
                if (mAccountManagementBean.telflag) {
                    startActivity(new Intent(AccountManagementActivity.this, ChangePhoneActivity.class));
                } else {
                    startActivity(new Intent(AccountManagementActivity.this, BindingTeleActivity.class));
                }
                break;
            case R.id.account_manage_tv_pwd:
                if (mAccountManagementBean.telflag) {
                    startActivity(new Intent(AccountManagementActivity.this, RevisePwdActivity.class));
                } else {
                    ToastUtils.showShort(UIUtils.getContext(), getString(R.string.AccountPWD));
                }
                break;
            case R.id.account_manage_ll_wx:
                if (isWX) {
                    new AlertDialog.Builder(AccountManagementActivity.this)
                            .setTitle("确定要解除绑定吗？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确认解绑", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OkGo
                                            .post(AppUrl.CANCELBIND_WECHAT)
                                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                            .execute(new StringDialogCallback(AccountManagementActivity.this) {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                                    if (successBean.success) {
                                                        mAccountManageRbWx.setChecked(true);
                                                        mAccountManageRbWx.setText("绑定");
                                                        isWX = false;
                                                    } else {
                                                        ToastUtils.showShort(UIUtils.getContext(), successBean.msg);
                                                    }
                                                }
                                            });


                                }
                            })
                            .show();
                } else {
                    LoginWX();
                }
                break;
            case R.id.account_manage_ll_qq:
                if (isQQ) {
                    new AlertDialog.Builder(AccountManagementActivity.this)
                            .setTitle("确定要解除绑定吗？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确认解绑", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    OkGo
                                            .post(AppUrl.CANCELBIND_QQ)
                                            .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                                            .execute(new StringDialogCallback(AccountManagementActivity.this) {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                                    if (successBean.success) {
                                                        mAccountManageRbQq.setChecked(true);
                                                        mAccountManageRbQq.setText("绑定");
                                                        isQQ = false;
                                                    } else {
                                                        ToastUtils.showShort(UIUtils.getContext(), successBean.msg);
                                                    }
                                                }
                                            });


                                }
                            })
                            .show();
                } else {
                    LoginQQ();

                }
                break;
            case R.id.account_manage_btn_logout:
                SPUtils.clear();
                startActivity(new Intent(AccountManagementActivity.this, LoginActivity.class));
                SPUtils.saveBoolean(UIUtils.getContext(), Constants.FIRST, false);
                EventBus.getDefault().post(new MessageEvent("Quit"));
                EventBus.getDefault().post(new MessageEvent("Login"));
                finish();
                break;
        }
    }

    private void LoginWX() {
        UMShareAPI mShareAPI = UMShareAPI.get(AccountManagementActivity.this);
        if (mShareAPI.isInstall(AccountManagementActivity.this, SHARE_MEDIA.WEIXIN)) {
            mShareAPI.doOauthVerify(AccountManagementActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
//            mShareAPI.getPlatformInfo(AccountManagementActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
        } else {
            ToastUtils.showShort(UIUtils.getContext(), "未安装微信");
        }
    }

    private void LoginQQ() {
        UMShareAPI mShareAPI = UMShareAPI.get(AccountManagementActivity.this);
        if (mShareAPI.isInstall(AccountManagementActivity.this, SHARE_MEDIA.QQ)) {
            mShareAPI.doOauthVerify(AccountManagementActivity.this, SHARE_MEDIA.QQ, umAuthListener);
//            mShareAPI.getPlatformInfo(AccountManagementActivity.this, SHARE_MEDIA.QQ, umAuthListener);
        } else {
            ToastUtils.showShort(UIUtils.getContext(), "未安装QQ");
        }
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

            if (platform == SHARE_MEDIA.WEIXIN) {

                OkGo
                        .post(AppUrl.BIND_WECHAT)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("unionid", data.get("unionid"))
                        .execute(new StringDialogCallback(AccountManagementActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                if (successBean.success) {
                                    mAccountManageRbWx.setChecked(false);
                                    mAccountManageRbWx.setText("已绑定");
                                    isWX = true;
                                    if (successBean.giveVIP) {
//                                        new AlertDialog.Builder(AccountManagementActivity.this)
//                                                .setMessage(R.string.PresentVipTip)
//                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        dialog.dismiss();
//                                                    }
//                                                })
//                                                .show();
                                        Notification.Builder builder = new Notification.Builder(AccountManagementActivity.this);
                                        Intent intent = new Intent(AccountManagementActivity.this, VipManageActivity.class);  //需要跳转指定的页面
                                        PendingIntent pendingIntent = PendingIntent.getActivity(AccountManagementActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent);
                                        builder.setSmallIcon(R.mipmap.ic_launcher);// 设置图标
                                        builder.setContentTitle(getString(R.string.vipTipHeader));// 设置通知的标题
                                        builder.setContentText(getString(R.string.vipTip));// 设置通知的内容
                                        builder.setWhen(System.currentTimeMillis());// 设置通知来到的时间
                                        builder.setAutoCancel(true); //自己维护通知的消失
                                        builder.setTicker(getString(R.string.vipTip));// 第一次提示消失的时候显示在通知栏上的
                                        builder.setOngoing(true);
                                        Notification notification = builder.build();
                                        notification.flags = Notification.FLAG_AUTO_CANCEL;  //只有全部清除时，Notification才会清除
                                        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(0, notification);
                                    }
                                } else {
                                    ToastUtils.showShort(UIUtils.getContext(), getString(R.string.AccounntWX));
                                }
                            }
                        });
            } else if (platform == SHARE_MEDIA.QQ) {

                OkGo
                        .post(AppUrl.BIND_QQ)
                        .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                        .params("qquid", data.get("uid"))
                        .execute(new StringDialogCallback(AccountManagementActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                                if (successBean.success) {
                                    mAccountManageRbQq.setChecked(false);
                                    mAccountManageRbQq.setText("已绑定");
                                    isQQ = true;
                                } else {
                                    ToastUtils.showShort(UIUtils.getContext(), getString(R.string.AccounntQQ));

                                }
                            }
                        });
            }

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Toast.makeText(getApplicationContext(), "绑定失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Toast.makeText(getApplicationContext(), "绑定取消", Toast.LENGTH_SHORT).show();

        }


    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
        LogUtils.d("-----------");
    }
}

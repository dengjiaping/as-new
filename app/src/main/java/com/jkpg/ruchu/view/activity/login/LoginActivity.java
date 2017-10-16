package com.jkpg.ruchu.view.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.LoginQQBean;
import com.jkpg.ruchu.bean.LoginStateBean;
import com.jkpg.ruchu.bean.LoginWxBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.lzy.okgo.OkGo;
import com.tencent.mm.opensdk.openapi.IWXAPI;
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
 * Created by qindi on 2017/5/11.
 */

public class LoginActivity extends BaseActivity {


    @BindView(R.id.login_tv_wx)
    ImageView mLoginTvWx;
    @BindView(R.id.login_ll_qq)
    LinearLayout mLoginLlQq;
    @BindView(R.id.login_ll_phone)
    LinearLayout mLoginLlPhone;
    @BindView(R.id.login_btn_random)
    Button mLoginBtnRandom;

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initBar();

        UMShareAPI.get(LoginActivity.this).fetchAuthResultWithBundle(LoginActivity.this, savedInstanceState, umAuthListener);
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

    @OnClick({R.id.login_tv_wx, R.id.login_ll_qq, R.id.login_ll_phone, R.id.login_btn_random})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_tv_wx:
                LoginWX();
                break;
            case R.id.login_ll_qq:
                LoginQQ();
                break;
            case R.id.login_ll_phone:
                startActivity(new Intent(LoginActivity.this, LoginPhoneActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
            case R.id.login_btn_random:
                finish();

                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }


    private void LoginQQ() {
        UMShareAPI mShareAPI = UMShareAPI.get(LoginActivity.this);
        if (mShareAPI.isInstall(LoginActivity.this, SHARE_MEDIA.QQ)) {
//             mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
            mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
        } else {
            ToastUtils.showShort(UIUtils.getContext(), "未安装QQ");
           /* Uri uri = Uri.parse("market://details?id=com.tencent.mobileqq");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);*/
        }
    }

    private void LoginWX() {
        UMShareAPI mShareAPI = UMShareAPI.get(LoginActivity.this);
        if (mShareAPI.isInstall(LoginActivity.this, SHARE_MEDIA.WEIXIN)) {
//            mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
            mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
        } else {
            ToastUtils.showShort(UIUtils.getContext(), "未安装微信");
           /* Uri uri = Uri.parse("market://details?id=com.tencent.mm");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);*/
        }


    }

    private UMAuthListener umAuthListener = new UMAuthListener() {


        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, final Map<String, String> data) {
           LogUtils.d(data.toString());
            if (platform == SHARE_MEDIA.WEIXIN) {
                OkGo
                        .post(AppUrl.SELECTUNIONIDORQQUID)
                        .params("unionid", data.get("unionid"))
                        .execute(new StringDialogCallback(LoginActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                LoginStateBean loginStateBean = new Gson().fromJson(s, LoginStateBean.class);
                                if (loginStateBean.isHave) {
                                    OkGo
                                            .post(AppUrl.WXLOGIN)
                                            .params("unionid", data.get("unionid"))
                                            .params("appwxnikename", data.get("name"))
                                            .params("appwxurlimage", data.get("iconurl"))
                                            .params("tele", "")
                                            .params("password", "")
                                            .params("flag", "1")
                                            .execute(new StringDialogCallback(LoginActivity.this) {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    LoginWxBean loginWxBean = new Gson().fromJson(s, LoginWxBean.class);
                                                    if (loginWxBean.state == 200) {
                                                        if (loginWxBean.backMess.loginCount.equals("1")) {
                                                            startActivity(new Intent(LoginActivity.this, BindingPhoneActivity.class));
                                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                        }
                                                        EventBus.getDefault().post(new MessageEvent("Login"));
                                                        SPUtils.saveString(UIUtils.getContext(), Constants.USERID, loginWxBean.backMess.userId);
                                                        finish();
                                                    } else {
                                                        ToastUtils.showShort(UIUtils.getContext(), "登陆失败 +" + loginWxBean.state);
                                                    }
                                                    LogUtils.i(s);
                                                }
                                            });
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, BindingPhoneActivity.class);
                                    intent.putExtra("unionid", data.get("unionid"));
                                    intent.putExtra("name", data.get("name"));
                                    intent.putExtra("iconurl", data.get("iconurl"));
                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERIMAGE, data.get("iconurl"));
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                }
                            }
                        });
            } else if (platform == SHARE_MEDIA.QQ) {
                OkGo
                        .post(AppUrl.SELECTUNIONIDORQQUID)
                        .params("qquid", data.get("uid"))
                        .execute(new StringDialogCallback(LoginActivity.this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                LoginStateBean loginStateBean = new Gson().fromJson(s, LoginStateBean.class);
                                if (loginStateBean.isHave) {
                                    OkGo
                                            .post(AppUrl.QQLOGIN)
                                            .params("uid", data.get("uid"))
                                            .params("appqqnikename", data.get("name"))
                                            .params("appqqurlimage", data.get("iconurl"))
                                            .params("tele", "")
                                            .params("password", "")
                                            .params("flag", "1")
                                            .execute(new StringDialogCallback(LoginActivity.this) {
                                                @Override
                                                public void onSuccess(String s, Call call, Response response) {
                                                    LoginQQBean loginQQBean = new Gson().fromJson(s, LoginQQBean.class);
                                                    if (loginQQBean.state == 200) {
                                                        if (loginQQBean.backMess.loginCount.equals("1")) {
                                                            startActivity(new Intent(LoginActivity.this, BindingPhoneActivity.class));
                                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                        }
                                                        EventBus.getDefault().post(new MessageEvent("Login"));
                                                        SPUtils.saveString(UIUtils.getContext(), Constants.USERID, loginQQBean.backMess.userId);
                                                        finish();
                                                    } else {
                                                        ToastUtils.showShort(UIUtils.getContext(), "登陆失败 +" + loginQQBean.state);
                                                    }
                                                }
                                            });

                                } else {
                                    Intent intent = new Intent(LoginActivity.this, BindingPhoneActivity.class);
                                    intent.putExtra("uid", data.get("uid"));
                                    intent.putExtra("name", data.get("name"));
                                    intent.putExtra("iconurl", data.get("iconurl"));
                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERIMAGE, data.get("iconurl"));
                                    LogUtils.d( data.get("name")+"");
                                    LogUtils.d( data.get("iconurl")+"");
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                }
                            }
                        });
//                mShow.dismiss();
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();
            LogUtils.i(t + "");
//            mShow.dismiss();

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "登陆取消", Toast.LENGTH_SHORT).show();
//            mShow.dismiss();
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
}

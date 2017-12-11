package com.jkpg.ruchu.view.activity.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/11.
 */

public class LoginActivity extends BaseActivity {


    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_v2);
        setSwipeBackEnable(false);

        ButterKnife.bind(this);
        EventBus.getDefault().post(new MessageEvent("Quit"));
        UMShareAPI.get(LoginActivity.this).fetchAuthResultWithBundle(LoginActivity.this, savedInstanceState, umAuthListener);
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


    @OnClick({R.id.login_tv_wx, R.id.login_btn_random, R.id.login_tv_other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_tv_wx:
                LoginWX();
                break;
            case R.id.login_tv_other:
                View loginView = View.inflate(UIUtils.getContext(), R.layout.view_other_login, null);

                final BottomSheetDialog dialog = new BottomSheetDialog(LoginActivity.this);
                dialog.setContentView(loginView);
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                dialog.show();
                loginView.findViewById(R.id.login_tv_qq).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        LoginQQ();
                    }
                });
                loginView.findViewById(R.id.login_tv_phone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(LoginActivity.this, LoginPhoneActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }
                });
                loginView.findViewById(R.id.login_tv_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


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
                                                        SPUtils.saveString(UIUtils.getContext(), Constants.IMID, loginWxBean.nameid);
                                                        SPUtils.saveString(UIUtils.getContext(), Constants.IMSIGN, loginWxBean.usersign);
                                                        finish();
                                                    } else {
                                                        ToastUtils.showShort(UIUtils.getContext(), "登录失败 +" + loginWxBean.state);
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
                                                        SPUtils.saveString(UIUtils.getContext(), Constants.IMID, loginQQBean.nameid);
                                                        SPUtils.saveString(UIUtils.getContext(), Constants.IMSIGN, loginQQBean.usersign);
                                                        finish();
                                                    } else {
                                                        ToastUtils.showShort(UIUtils.getContext(), "登录失败 +" + loginQQBean.state);
                                                    }
                                                }
                                            });

                                } else {
                                    Intent intent = new Intent(LoginActivity.this, BindingPhoneActivity.class);
                                    intent.putExtra("uid", data.get("uid"));
                                    intent.putExtra("name", data.get("name"));
                                    intent.putExtra("iconurl", data.get("iconurl"));
                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERIMAGE, data.get("iconurl"));
                                    LogUtils.d(data.get("name") + "");
                                    LogUtils.d(data.get("iconurl") + "");
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
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
            LogUtils.i(t + "");
//            mShow.dismiss();

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "登录取消", Toast.LENGTH_SHORT).show();
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

package com.jkpg.ruchu.view.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.MainActivity;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/11.
 */

public class LoginActivity extends AppCompatActivity {


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
//        regToWx();
        initBar();

        UMShareAPI.get(LoginActivity.this).fetchAuthResultWithBundle(LoginActivity.this, savedInstanceState, umAuthListener);
    }

    /* private void regToWx() {
         api = WXAPIFactory.createWXAPI(this, APP_ID, true);
         api.registerApp(APP_ID);
     }
 */
    private void initBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @OnClick({R.id.login_tv_wx, R.id.login_ll_qq, R.id.login_ll_phone, R.id.login_btn_random})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_tv_wx:
              /*  SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                api.sendReq(req);*/
                LoginWX();
                break;
            case R.id.login_ll_qq:
                LoginQQ();
                break;

            case R.id.login_ll_phone:
                startActivity(new Intent(LoginActivity.this, LoginPhoneActivity.class));
                finish();
                break;
            case R.id.login_btn_random:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                finish();
                break;
        }
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

    private void LoginQQ() {
        UMShareAPI mShareAPI = UMShareAPI.get(LoginActivity.this);
        if (mShareAPI.isInstall(LoginActivity.this, SHARE_MEDIA.QQ)) {
            mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
            mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
        } else {
            ToastUtils.showShort(UIUtils.getContext(), "未安装QQ");
            Uri uri = Uri.parse("market://details?id=com.tencent.mobileqq");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void LoginWX() {
        UMShareAPI mShareAPI = UMShareAPI.get(LoginActivity.this);
        if (mShareAPI.isInstall(LoginActivity.this, SHARE_MEDIA.WEIXIN)) {
            mShareAPI.doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
            mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
        } else {
            ToastUtils.showShort(UIUtils.getContext(), "未安装微信");
            Uri uri = Uri.parse("market://details?id=com.tencent.mm");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }


    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            String temp = "";
            for (String key : data.keySet()) {
                temp = temp + key + " : " + data.get(key) + "\n";
            }
            LogUtils.i(temp);

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
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

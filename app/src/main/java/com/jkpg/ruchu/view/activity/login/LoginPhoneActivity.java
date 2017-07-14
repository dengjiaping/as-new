package com.jkpg.ruchu.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.Login;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.RegexUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.lzy.okgo.OkGo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class LoginPhoneActivity extends AppCompatActivity {

    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.login_et_phone)
    EditText mLoginEtPhone;
    @BindView(R.id.login_et_password)
    EditText mLoginEtPassword;
    @BindView(R.id.login_btn_register)
    Button mLoginBtnRegister;
    @BindView(R.id.login_btn_ok)
    Button mLoginBtnOk;
    @BindView(R.id.login_tv_forget)
    TextView mLoginTvForget;
    private String mPhone;
    private String mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        ButterKnife.bind(this);
        initHeader();
    }


    private void initHeader() {
        mHeaderTvTitle.setText("手机号登陆");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(LoginPhoneActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.login_tv_forget, R.id.login_btn_ok, R.id.login_btn_register, R.id.header_iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                startActivity(new Intent(LoginPhoneActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.login_btn_register:
                startActivity(new Intent(LoginPhoneActivity.this, RegisterPhoneActivity.class));
                finish();
                break;
            case R.id.login_btn_ok:
                mPhone = mLoginEtPhone.getText().toString().trim();
                mPassword = mLoginEtPassword.getText().toString().trim();
                if (!RegexUtils.isMatch(RegexUtils.REGEX_PHONE, mPhone)) {
                    ToastUtils.showShort(UIUtils.getContext(), "请输入正确的手机号");
                    return;
                }
                if (StringUtils.isEmpty(mPassword)) {
                    ToastUtils.showShort(UIUtils.getContext(), "请输入密码");
                    return;
                }
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
                    return;
                }
                //md5加密
               /* mPassword = EncryptUtils.encryptMD5ToString(mPassword);
                LogUtils.i(mPassword + "-- phone");*/
                OkGo
                        .post(AppUrl.LOGIN)
                        .tag(this)
                        .params("tele", mPhone)
                        .params("password", mPassword)
                        .execute(new StringDialogCallback(this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                Login login = new Gson().fromJson(s, Login.class);
                                SPUtils.saveString(UIUtils.getContext(), Constants.PERSONALINFO, s);
                                if (!login.success) {
                                    ToastUtils.showShort(UIUtils.getContext(), "手机号或密码错误");
                                }
                                // FIXME: 2017/7/4 
                                /*else if (login.birth == "") {
                                    startActivity(new Intent(LoginPhoneActivity.this, PerfectInfoActivity.class));
                                    finish();
                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERID, login.userid);
                                } */
                                else {
                                    startActivity(new Intent(LoginPhoneActivity.this, PerfectInfoActivity.class));
                                    finish();
                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERID, login.userid);
                                }
                            }
                        });
                break;
            case R.id.login_tv_forget:
                startActivity(new Intent(LoginPhoneActivity.this, ForgetPasswordActivity.class));
                finish();
                break;
        }
    }
}

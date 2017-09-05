package com.jkpg.ruchu.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.CodeBean;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
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

/**
 * Created by qindi on 2017/5/13.
 */

public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.forget_et_phone)
    EditText mForgetEtPhone;
    @BindView(R.id.forget_et_code)
    EditText mForgetEtCode;
    @BindView(R.id.forget_tv_send)
    TextView mForgetTvSend;
    @BindView(R.id.forget_bt_next)
    Button mForgetBtNext;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        initHeader();
    }


    private void initHeader() {
        mHeaderTvTitle.setText("忘记密码");
    }

    @OnClick({R.id.forget_tv_send, R.id.forget_bt_next, R.id.header_iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_tv_send:
                sendSMS();
                break;
            case R.id.forget_bt_next:
                next();
                break;
            case R.id.header_iv_left:
                startActivity(new Intent(ForgetPasswordActivity.this, LoginPhoneActivity.class));
                finish();
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
                        LogUtils.i(s + "--------------------");
                        CodeBean code = new Gson().fromJson(s, CodeBean.class);
                        if (code.success) {
                            startActivity(new Intent(ForgetPasswordActivity.this, RevisePasswordActivity.class));
                            SPUtils.saveString(UIUtils.getContext(), Constants.PHONE, phone);
                            finish();
                        } else if (code.error.equals("05")) {
                            ToastUtils.showShort(UIUtils.getContext(), "验证码错误");
                        } else if (code.error.equals("01")) {
                            ToastUtils.showShort(UIUtils.getContext(), "未找到该用户");
                        }
                    }
                });

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
        OkGo
                .post(AppUrl.UPDATE_TEL)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("tele", phone)
                .params("flag", "0")
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                        if (successBean.success) {

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
                            OkGo.
                                    post(AppUrl.SMS)
                                    .params("tele", phone)
                                    .execute(new StringDialogCallback(ForgetPasswordActivity.this) {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            CodeBean codeBean = new Gson().fromJson(s, CodeBean.class);
                                            if (!codeBean.success) {
                                                ToastUtils.showShort(UIUtils.getContext(), "验证码请求超过5次,请明天重试");
                                            }
                                        }
                                    });
                        } else {
                            ToastUtils.showShort(UIUtils.getContext(), "手机号不存在");
                        }
                    }
                });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(ForgetPasswordActivity.this, LoginPhoneActivity.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}

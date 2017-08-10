package com.jkpg.ruchu.view.activity.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.CodeBean;
import com.jkpg.ruchu.bean.RegisterPhoneBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.utils.AnimationUtil;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.Md5Utils;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.RegexUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.CountDownTimer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/5/11.
 */

public class RegisterPhoneActivity extends BaseActivity {


    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.register_et_phone)
    EditText mRegisterEtPhone;
    @BindView(R.id.register_cb)
    CheckBox mRegisterCb;
    @BindView(R.id.register_tv_protocol)
    TextView mRegisterTvProtocol;
    @BindView(R.id.register_bt_obtain)
    Button mRegisterBtObtain;
    @BindView(R.id.register_ll_one)
    LinearLayout mRegisterLlOne;
    @BindView(R.id.register_ll_two)
    LinearLayout mRegisterLlTwo;
    @BindView(R.id.register_tv_info)
    TextView mRegisterTvInfo;
    @BindView(R.id.register_et_code)
    EditText mRegisterEtCode;
    @BindView(R.id.register_bt_time)
    TextView mRegisterBtTime;
    @BindView(R.id.register_et_password_one)
    EditText mRegisterEtPasswordOne;
    @BindView(R.id.register_et_password_two)
    EditText mRegisterEtPasswordTwo;
    @BindView(R.id.register_bt_ok)
    TextView mRegisterBtOk;
    @BindView(R.id.register_bt_info)
    Button mRegisterBtInfo;
    @BindView(R.id.register_ll_success)
    LinearLayout mRegisterLlSuccess;
    @BindView(R.id.register_ll_three)
    LinearLayout mRegisterLlThree;
    private String mPhone;

    CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            mRegisterBtTime.setText("重新发送(" + millisUntilFinished / 1000 + "s)");
            mRegisterBtTime.setClickable(false);
        }

        @Override
        public void onFinish() {
            mRegisterBtTime.setText("发送验证码");
            mRegisterBtTime.setClickable(true);
        }
    };
    private String mCode;
    private String mPwdone;
    private String mPwdtwo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        ButterKnife.bind(this);

        initTitle();
        mRegisterTvProtocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void initTitle() {
        mHeaderTvTitle.setText("注册");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mRegisterLlThree.getVisibility() == View.VISIBLE) {
                moveTaskToBack(false);
                return true;
            }
            if (mRegisterLlOne.getVisibility() == View.VISIBLE) {
                startActivity(new Intent(RegisterPhoneActivity.this, LoginPhoneActivity.class));
                finish();
                return true;
            } else if (mRegisterLlTwo.getVisibility() == View.VISIBLE) {
                mRegisterLlTwo.setVisibility(View.GONE);
                mRegisterLlOne.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.header_iv_left, R.id.register_tv_protocol, R.id.register_bt_obtain, R.id.register_bt_time, R.id.register_bt_ok, R.id.register_bt_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:

                if (mRegisterLlOne.getVisibility() == View.VISIBLE) {
                    startActivity(new Intent(RegisterPhoneActivity.this, LoginPhoneActivity.class));
                    finish();
                } else if (mRegisterLlTwo.getVisibility() == View.VISIBLE) {
                    mRegisterLlTwo.setVisibility(View.GONE);
                    mRegisterLlOne.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.register_tv_protocol:

                // TODO: 2017/5/13
                break;
            case R.id.register_bt_obtain:
                sendPhone();
                break;
            case R.id.register_bt_time:
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
                    return;
                }
                timer.start();
                OkGo
                        .post(AppUrl.SMS)
                        .params("tele", mPhone)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                CodeBean codeBean = new Gson().fromJson(s, CodeBean.class);
                                if (!codeBean.success) {
                                    ToastUtils.showShort(UIUtils.getContext(), "验证码请求超过5次,明天重试");
                                }
                            }
                        });
                break;
            case R.id.register_bt_ok:
                mCode = mRegisterEtCode.getText().toString();
                mPwdone = mRegisterEtPasswordOne.getText().toString();
                mPwdtwo = mRegisterEtPasswordTwo.getText().toString();
                if (StringUtils.isEmpty(mCode)) {
                    ToastUtils.showShort(UIUtils.getContext(), "请输入验证码");
                    return;
                }
                if (!NetworkUtils.isConnected()) {
                    ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
                    return;
                }
                if (!RegexUtils.isMatch(RegexUtils.REGEX_PWD, mPwdone)) {
                    ToastUtils.showShort(UIUtils.getContext(), "6-16位密码，包含数字和字母");
                    return;
                }
                if (!StringUtils.isEquals(mPwdone, mPwdtwo)) {
                    ToastUtils.showShort(UIUtils.getContext(), "两次密码不一致");
                    return;
                }

                mPwdone = Md5Utils.getMD5(mPwdone);

                OkGo
                        .post(AppUrl.REGISTERTEL)
                        .params("tele", mPhone)
                        .params("code", mCode)
                        .params("password", mPwdone)
                        .execute(new StringDialogCallback(this) {
                            @Override
                            public void onSuccess(String s, Call call, Response response) {
                                RegisterPhoneBean registerPhoneBean = new Gson().fromJson(s, RegisterPhoneBean.class);
                                if (!registerPhoneBean.success) {
                                    if (registerPhoneBean.error.equals("04")) {
                                        ToastUtils.showShort(UIUtils.getContext(), "该手机号已被注册");
                                    } else {
                                        ToastUtils.showShort(UIUtils.getContext(), "验证码不正确");
                                    }
                                } else {
                                    mRegisterLlThree.setVisibility(View.VISIBLE);
                                    mHeaderIvLeft.setClickable(false);
                                    mRegisterLlSuccess.setAnimation(AnimationUtil.createPanInAnim(2000));
                                }
                            }
                        });

                break;
            case R.id.register_bt_info:
                startActivity(new Intent(RegisterPhoneActivity.this, PerfectInfoActivity.class));
                finish();
                break;
        }
    }

    private void sendPhone() {
        mPhone = mRegisterEtPhone.getText().toString().trim();
        if (!RegexUtils.isMatch(RegexUtils.REGEX_PHONE, mPhone)) {
            ToastUtils.showShort(UIUtils.getContext(), "请输入正确的手机号");
            return;
        }
        if (!mRegisterCb.isChecked()) {
            ToastUtils.showShort(UIUtils.getContext(), "请同意如初协议");
            return;
        }
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
            return;
        }
        // 网络请求

        OkGo
                .post(AppUrl.SMS)
                .params("tele", mPhone)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtils.i(s + "onSuccess");
                        CodeBean codeBean = new Gson().fromJson(s, CodeBean.class);
                        if (codeBean.success) {
                            mRegisterTvInfo.setText(mPhone);
                            mRegisterLlOne.setVisibility(View.GONE);
                            mRegisterLlTwo.setVisibility(View.VISIBLE);
                            timer.start();
                        } else {
                            ToastUtils.showShort(UIUtils.getContext(), "验证码请求超过5次,明天重试");
                        }


                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}

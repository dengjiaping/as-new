package com.jkpg.ruchu.view.activity.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.bean.CodeBean;
import com.jkpg.ruchu.bean.MessageEvent;
import com.jkpg.ruchu.bean.RegisterPhoneBean;
import com.jkpg.ruchu.bean.SuccessBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.Md5Utils;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.PopupWindowUtils;
import com.jkpg.ruchu.utils.RegexUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.HtmlActivity;
import com.jkpg.ruchu.widget.CountDownTimer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

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
    Button mRegisterBtOk;
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
    private PopupWindow mPopupWindowSuccess;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_phone);
        setSwipeBackEnable(false);


        ButterKnife.bind(this);

        initTitle();
        mRegisterLlThree.setVisibility(View.GONE);
        mRegisterTvProtocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void initTitle() {
        mHeaderTvTitle.setText("注册");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

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
    public void onViewClicked(final View view) {
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
                Intent intent = new Intent(RegisterPhoneActivity.this, HtmlActivity.class);
                intent.putExtra("URL", Constants.XIEYI);
                startActivity(intent);
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
                                LogUtils.d("sms_code="+s);
                                CodeBean codeBean = new Gson().fromJson(s, CodeBean.class);
                                if (!codeBean.success) {
                                    ToastUtils.showShort(UIUtils.getContext(), "验证码获取失败(如请求超过5次,请明天重试)");
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
                        .params("phoneflag", 1)
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
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(RegisterPhoneActivity.this.getCurrentFocus().getWindowToken(), 0);
                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERID, registerPhoneBean.userid);
                                    SPUtils.saveString(UIUtils.getContext(), Constants.USERNANE, registerPhoneBean.username);
                                    SPUtils.saveString(UIUtils.getContext(), Constants.IMID, registerPhoneBean.nameid);
                                    SPUtils.saveString(UIUtils.getContext(), Constants.IMSIGN, registerPhoneBean.usersign);
                                    EventBus.getDefault().post(new MessageEvent("Login"));
                                    mRegisterLlThree.setVisibility(View.GONE);
                                    mHeaderIvLeft.setClickable(false);
                                    mPopupWindowSuccess = new PopupWindow(RegisterPhoneActivity.this);
                                    mPopupWindowSuccess.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                                    mPopupWindowSuccess.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
                                    View viewPay = LayoutInflater.from(RegisterPhoneActivity.this).inflate(R.layout.view_register_success, null);
                                    viewPay.findViewById(R.id.register_bt_info).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mPopupWindowSuccess.dismiss();
                                        }
                                    });
                                    mPopupWindowSuccess.setContentView(viewPay);
                                    mPopupWindowSuccess.setBackgroundDrawable(new ColorDrawable(0x00000000));
                                    mPopupWindowSuccess.setOutsideTouchable(true);
                                    mPopupWindowSuccess.setFocusable(true);
                                    mPopupWindowSuccess.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            startActivity(new Intent(RegisterPhoneActivity.this, PerfectInfoActivity.class));
                                            finish();
                                        }
                                    });
                                    mPopupWindowSuccess.showAtLocation(getLayoutInflater().inflate(R.layout.activity_start_train2, null), Gravity.CENTER, 0, 0);
                                    PopupWindowUtils.darkenBackground(RegisterPhoneActivity.this, .4f);
                                }
                            }
                        });

                break;
            case R.id.register_bt_info:
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
                .post(AppUrl.UPDATE_TEL)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("tele", mPhone)
                .params("flag", "0")
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        SuccessBean successBean = new Gson().fromJson(s, SuccessBean.class);
                        if (!successBean.success) {
                            ToastUtils.showShort(UIUtils.getContext(), "手机号已存在");
                        } else {
                            OkGo
                                    .post(AppUrl.SMS)
                                    .params("tele", mPhone)
                                    .execute(new StringDialogCallback(RegisterPhoneActivity.this) {
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
                                                ToastUtils.showShort(UIUtils.getContext(), "验证码获取失败(如请求超过5次,请明天重试)");
                                            }


                                        }
                                    });
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

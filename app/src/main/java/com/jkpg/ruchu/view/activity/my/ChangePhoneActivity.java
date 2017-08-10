package com.jkpg.ruchu.view.activity.my;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
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
 * Created by qindi on 2017/5/25.
 */

public class ChangePhoneActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.forget_et_phone)
    EditText mEtPhone;
    @BindView(R.id.forget_et_code)
    EditText mEtCode;
    @BindView(R.id.forget_tv_send)
    TextView mTvSend;
    @BindView(R.id.forget_bt_next)
    Button mBtNext;
    private static final int CODE_ING = 1;//已发送，倒计时
    private static final int CODE_REPEAT = 2;//重新发送
    private int TIME = 60;//倒计时60s
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_ING://已发送,倒计时
                    mTvSend.setText("重新发送(" + --TIME + "s)");
                    mTvSend.setClickable(false);
                    break;
                case CODE_REPEAT://重新发送
                    mTvSend.setText("获取验证码");
                    mTvSend.setClickable(true);
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
        mHeaderTvTitle.setText("变更手机号");
    }

    @OnClick({R.id.header_iv_left, R.id.forget_tv_send, R.id.forget_bt_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
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
        final String phone = mEtPhone.getText().toString();
        String mycode = mEtCode.getText().toString();
        if (StringUtils.isEmpty(mycode)) {
            ToastUtils.showShort(UIUtils.getContext(), "请输入验证码");
            return;
        }
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
            return;
        }
    }

    private void sendSMS() {
        final String phone = mEtPhone.getText().toString();
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
                        if (!successBean.success) {
                            ToastUtils.showShort(UIUtils.getContext(), "手机号已存在");
                        } else {
                            OkGo
                                    .post(AppUrl.SMS)
                                    .params("tele", phone)
                                    .execute(new StringDialogCallback(ChangePhoneActivity.this) {
                                        @Override
                                        public void onSuccess(String s, Call call, Response response) {
                                            // TODO: 2017/7/22
                                            LogUtils.i(s);
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


                                            showDialogSuccess();

                                        }
                                    });
                        }
                    }
                });

    }

    private void showDialogSuccess() {
        View view = View.inflate(UIUtils.getContext(), R.layout.view_show_success, null);
        ((TextView) view.findViewById(R.id.show_success_text)).setText("手机号变更成功");
        AlertDialog show = new AlertDialog.Builder(this)
                .setView(view)
                .show();
        show.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }
}

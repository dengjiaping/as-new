package com.jkpg.ruchu.view.activity.login;

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

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.RegexUtils;
import com.jkpg.ruchu.utils.StringUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        setContentView(R.layout.activity_binding_phone);
        ButterKnife.bind(this);
        initHeader();
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


        showDialogSuccess();

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
        new AlertDialog.Builder(this)
                .setView(view)
                .show();
    }
}

package com.jkpg.ruchu.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.RegisterPhoneBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.Md5Utils;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.RegexUtils;
import com.jkpg.ruchu.utils.SPUtils;
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

public class RevisePasswordActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.revise_et_new_one)
    EditText mReviseEtNewOne;
    @BindView(R.id.revise_et_new_two)
    EditText mReviseEtNewTwo;
    @BindView(R.id.revise_btn_next)
    Button mReviseBtnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_password);
        ButterKnife.bind(this);
        initHeader();
    }

    private void initHeader() {
        mHeaderTvTitle.setText("修改密码");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(RevisePasswordActivity.this, ForgetPasswordActivity.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.header_iv_left, R.id.revise_btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                startActivity(new Intent(RevisePasswordActivity.this, ForgetPasswordActivity.class));
                finish();
                break;
            case R.id.revise_btn_next:
                againLogin();
                break;
        }
    }

    private void againLogin() {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(UIUtils.getContext(), "网络未连接");
            return;
        }
        String pwd1 = mReviseEtNewOne.getText().toString().trim();
        if (!RegexUtils.isMatch(RegexUtils.REGEX_PWD, pwd1)) {
            ToastUtils.showShort(UIUtils.getContext(), mReviseEtNewOne.getHint().toString());
            return;
        }
        if (!mReviseEtNewTwo.getText().toString().trim().equals(pwd1)) {
            ToastUtils.showShort(UIUtils.getContext(), "两次密码不一致");
            return;
        }
        pwd1 = Md5Utils.getMD5(pwd1);
        OkGo
                .post(AppUrl.UPDATEPWD)
                .params("password", pwd1)
                .params("tele", SPUtils.getString(UIUtils.getContext(), Constants.PHONE, ""))
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        RegisterPhoneBean code = new Gson().fromJson(s, RegisterPhoneBean.class);
                        if (code.success) {
                            startActivity(new Intent(RevisePasswordActivity.this, LoginPhoneActivity.class));
                            finish();
                        } else {
                            ToastUtils.showShort(UIUtils.getContext(), "修改失败");
                        }
                    }
                });

    }
}

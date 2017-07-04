package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.utils.NetworkUtils;
import com.jkpg.ruchu.utils.RegexUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.login.LoginPhoneActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/25.
 */

public class RevisePwdActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.revise_et_old)
    EditText mReviseEtOld;
    @BindView(R.id.revise_et_new_one)
    EditText mReviseEtNewOne;
    @BindView(R.id.revise_et_new_two)
    EditText mReviseEtNewTwo;
    @BindView(R.id.revise_btn_next)
    Button mReviseBtnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_pwd);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.header_iv_left, R.id.revise_btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.revise_btn_next:
                showDialogSuccess();
                break;
        }
    }

    private void showDialogSuccess() {
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


        View view = View.inflate(UIUtils.getContext(), R.layout.view_show_success, null);
        ((TextView) view.findViewById(R.id.show_success_text)).setText("密码修改成功，请重新登陆");
        new AlertDialog.Builder(this)
                .setView(view)
                .show();

        MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(RevisePwdActivity.this, LoginPhoneActivity.class));
                finish();
            }
        }, 1000);
    }
}

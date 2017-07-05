package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.view.activity.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/25.
 */

public class AccountManagementActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.account_manage_tv_phone)
    TextView mAccountManageTvPhone;
    @BindView(R.id.account_manage_btn_change)
    Button mAccountManageBtnChange;
    @BindView(R.id.account_manage_tv_pwd)
    TextView mAccountManageTvPwd;
    @BindView(R.id.account_manage_rb_wx)
    CheckBox mAccountManageRbWx;
    @BindView(R.id.account_manage_rb_qq)
    CheckBox mAccountManageRbQq;
    @BindView(R.id.account_manage_btn_logout)
    Button mAccountManageBtnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        ButterKnife.bind(this);
        initHeader();
    }

    private void initHeader() {
        mHeaderTvTitle.setText("账号管理");
    }

    @OnClick({R.id.account_manage_btn_logout, R.id.header_iv_left, R.id.account_manage_btn_change, R.id.account_manage_tv_pwd, R.id.account_manage_rb_wx, R.id.account_manage_rb_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.account_manage_btn_change:
                startActivity(new Intent(AccountManagementActivity.this, ChangePhoneActivity.class));
                break;
            case R.id.account_manage_tv_pwd:
                startActivity(new Intent(AccountManagementActivity.this, RevisePwdActivity.class));
                break;
            case R.id.account_manage_rb_wx:
                mAccountManageRbWx.setText((mAccountManageRbWx.isChecked() ? "绑定" : "未绑定"));
                break;
            case R.id.account_manage_rb_qq:
                mAccountManageRbQq.setText((mAccountManageRbQq.isChecked() ? "绑定" : "未绑定"));

                break;
            case R.id.account_manage_btn_logout:
                SPUtils.clear();
                startActivity(new Intent(AccountManagementActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}

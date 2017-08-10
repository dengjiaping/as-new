package com.jkpg.ruchu.view.activity.my;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.utils.PermissionUtils;
import com.jkpg.ruchu.utils.ToastUtils;
import com.jkpg.ruchu.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/24.
 */

public class SetUpAboutActivity extends BaseActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.set_up_about_serve)
    TextView mSetUpAboutServe;
    @BindView(R.id.set_up_about_conceal)
    TextView mSetUpAboutConceal;
    @BindView(R.id.set_up_about_version)
    TextView mSetUpAboutVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_about);
        ButterKnife.bind(this);
        initHeader();

        try {
            mSetUpAboutVersion.setText("当前版本号： " + UIUtils.getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initHeader() {
        mHeaderTvTitle.setText("关于如初");
    }

    @OnClick({R.id.set_up_about_serve, R.id.set_up_about_conceal, R.id.header_iv_left, R.id.set_up_about_phone, R.id.set_up_about_email})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_up_about_serve:
                break;
            case R.id.set_up_about_conceal:
                break;
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.set_up_about_phone:

                PermissionUtils.requestPermissions(SetUpAboutActivity.this, 100, new String[]{Manifest.permission.CALL_PHONE}, new PermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + getString(R.string.about_phone));
                        intent.setData(data);
                        if (ActivityCompat.checkSelfPermission(SetUpAboutActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied(String[] deniedPermissions) {
                        ToastUtils.showShort(UIUtils.getContext(),"请拨打手机号联系我们");
                    }
                });


                break;
            case R.id.set_up_about_email:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822"); // 真机上使用这行
                i.putExtra(Intent.EXTRA_EMAIL,
                        new String[]{getString(R.string.about_email)});
                i.putExtra(Intent.EXTRA_SUBJECT, "您的建议");
                i.putExtra(Intent.EXTRA_TEXT, "我们很希望能得到您的建议！！！");
                startActivity(Intent.createChooser(i,
                        "请选择您的邮箱"));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(SetUpAboutActivity.this, 100, new String[]{Manifest.permission.CALL_PHONE});
    }
}

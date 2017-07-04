package com.jkpg.ruchu.view.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/24.
 */

public class SetUpAboutActivity extends AppCompatActivity {
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

    @OnClick({R.id.set_up_about_serve, R.id.set_up_about_conceal, R.id.header_iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_up_about_serve:
                break;
            case R.id.set_up_about_conceal:
                break;
            case R.id.header_iv_left:
                finish();
                break;
        }
    }
}

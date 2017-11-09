package com.jkpg.ruchu.view.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.view.fragment.MySmsNoticFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/10/18.
 */

public class NoticeActivity extends BaseActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruchu_notice);
        ButterKnife.bind(this);
        mHeaderTvTitle.setText("通知");
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.notice_body, new MySmsNoticFragment()).commit();
    }

    @OnClick(R.id.header_iv_left)
    public void onViewClicked() {
        finish();
    }
}

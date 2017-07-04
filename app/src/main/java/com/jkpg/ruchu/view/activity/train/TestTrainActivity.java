package com.jkpg.ruchu.view.activity.train;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.view.activity.test.TestDetailedActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/18.
 */

public class TestTrainActivity extends AppCompatActivity {
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.test_btn_start)
    Button mTestBtnStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_guide);
        ButterKnife.bind(this);

        initHeader();
    }

    private void initHeader() {
        mHeaderTvTitle.setText("健康测试");
    }

    @OnClick({R.id.header_iv_left, R.id.test_btn_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.test_btn_start:
                startActivity(new Intent(TestTrainActivity.this, TestDetailedActivity.class));
                finish();
                break;
        }
    }
}

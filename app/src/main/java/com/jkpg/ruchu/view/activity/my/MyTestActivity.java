package com.jkpg.ruchu.view.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.view.activity.train.TestTrainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/24.
 */

public class MyTestActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_test_tv_grade)
    TextView mMyTestTvGrade;
    @BindView(R.id.my_test_tv_level)
    TextView mMyTestTvLevel;
    @BindView(R.id.my_test_tv_time)
    TextView mMyTestTvTime;
    @BindView(R.id.my_test_tv_body)
    TextView mMyTestTvBody;
    @BindView(R.id.my_test_sl)
    ScrollView mMyTestSl;
    @BindView(R.id.my_test_btn_test)
    Button mMyTestBtnTest;
    @BindView(R.id.my_test_ll)
    LinearLayout mMyTestLl;
    @BindView(R.id.my_test_ll_btn_again_test)
    LinearLayout mMyTestLlBtnAgainTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test);
        ButterKnife.bind(this);
        initHeader();
        initData();
    }

    private void initData() {

    }

    private void initHeader() {
        mHeaderTvTitle.setText("我的测试");
    }

    @OnClick({R.id.header_iv_left, R.id.my_test_btn_test, R.id.my_test_ll_btn_again_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.my_test_btn_test:
                startActivity(new Intent(MyTestActivity.this, TestTrainActivity.class));
                break;
            case R.id.my_test_ll_btn_again_test:
                startActivity(new Intent(MyTestActivity.this, TestTrainActivity.class));
                break;
        }
    }
}

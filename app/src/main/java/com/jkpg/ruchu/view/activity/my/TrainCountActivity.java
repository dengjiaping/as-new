package com.jkpg.ruchu.view.activity.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.view.fragment.TrainCountMountFragment;
import com.jkpg.ruchu.view.fragment.TrainCountYearFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/31.
 */

public class TrainCountActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.train_count_rb_moon)
    RadioButton mTrainCountRbMoon;
    @BindView(R.id.train_count_rb_year)
    RadioButton mTrainCountRbYear;
    @BindView(R.id.train_count_rg)
    RadioGroup mTrainCountRg;
    @BindView(R.id.train_count_content)
    FrameLayout mTrainCountContent;
    private TrainCountMountFragment mMountFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_count);
        ButterKnife.bind(this);
        initHeader();
        initFragment();
    }



    private void initFragment() {
        mMountFragment = new TrainCountMountFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.train_count_content, mMountFragment).commit();
    }

    private void initHeader() {
        mHeaderTvTitle.setText("训练统计");
    }

    @OnClick({R.id.header_iv_left, R.id.train_count_rb_moon, R.id.train_count_rb_year})
    public void onViewClicked(View view) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {

            case R.id.header_iv_left:
                finish();
                break;
            case R.id.train_count_rb_moon:
                ft.replace(R.id.train_count_content, mMountFragment).commit();
                break;
            case R.id.train_count_rb_year:
                ft.replace(R.id.train_count_content, new TrainCountYearFragment()).commit();
                break;
        }
    }
}

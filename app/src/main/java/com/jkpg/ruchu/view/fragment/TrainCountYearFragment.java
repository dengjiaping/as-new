package com.jkpg.ruchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.widget.Histogram;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qindi on 2017/5/31.
 */

public class TrainCountYearFragment extends Fragment {
    @BindView(R.id.train_count_btn_up)
    Button mTrainCountBtnUp;
    @BindView(R.id.train_count_tv_moon)
    TextView mTrainCountTvMoon;
    @BindView(R.id.train_count_btn_down)
    Button mTrainCountBtnDown;
    @BindView(R.id.train_count_fl_histogram)
    Histogram mTrainCountFlHistogram;
    @BindView(R.id.train_count_tv_time_count)
    TextView mTrainCountTvTimeCount;
    @BindView(R.id.train_count_tv_time_count_number)
    TextView mTrainCountTvTimeCountNumber;
    @BindView(R.id.train_count_tv_time_mean)
    TextView mTrainCountTvTimeMean;
    @BindView(R.id.train_count_tv_time_mean_number)
    TextView mTrainCountTvTimeMeanNumber;
    Unbinder unbinder;

    List<Histogram.PPHistogramBean> mDatas;
    int MAX = 80;
    private int mNowYear;
    private int mNowMonth;
    private int mYear;
    private int mMonth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train_count_moon, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initHistogram();

    }

    private void init() {
        Calendar c = Calendar.getInstance();
        mNowYear = c.get(Calendar.YEAR);
        mNowMonth = c.get(Calendar.MONTH) + 1;
        mYear = mNowYear;
        mTrainCountTvMoon.setText(mNowYear + "年");
        mTrainCountTvTimeCount.setText("本年训练总时间");
        mTrainCountTvTimeMean.setText("本年平均训练时间");
        if (mYear == mNowYear)
            mTrainCountBtnDown.setEnabled(false);
    }

    private void initHistogram() {
        mDatas = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            mDatas.add(new Histogram.PPHistogramBean(new Random().nextInt(81), i + ""));
        }

        mTrainCountFlHistogram.setmDatas(mDatas, MAX);
        mTrainCountFlHistogram.setCountInOne(12);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.train_count_btn_up, R.id.train_count_btn_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.train_count_btn_up:
                initHistogram();
                mTrainCountTvMoon.setText(--mNowYear + "年");
                mTrainCountBtnDown.setEnabled(true);


                break;
            case R.id.train_count_btn_down:

                initHistogram();
                mTrainCountTvMoon.setText(++mNowYear + "年");
                if (mYear == mNowYear)
                    mTrainCountBtnDown.setEnabled(false);
                break;
        }
    }
}

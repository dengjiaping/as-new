package com.jkpg.ruchu.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.bean.TrainYearCountBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.Histogram;
import com.lzy.okgo.OkGo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Response;

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
    @BindView(R.id.train_count_tv_1)
    TextView mTrainCountTv1;
    @BindView(R.id.train_count_tv_2)
    TextView mTrainCountTv2;
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
        initData();
    }

    private void initData() {
        OkGo
                .post(AppUrl.EXERCISEYEAR)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("years", mNowYear)
                .execute(new StringDialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        TrainYearCountBean trainYearCountBean = new Gson().fromJson(s, TrainYearCountBean.class);
                        initHistogram(trainYearCountBean);
                        float i = (float) (trainYearCountBean.countyear / 3600.0);
                        mTrainCountTvTimeCountNumber.setText((new DecimalFormat("0.00").format(i) + ""));
                        float ss = (float) (trainYearCountBean.avmonth / 3600.0);
                        mTrainCountTvTimeMeanNumber.setText((new DecimalFormat("0.00").format(ss) + ""));
                    }
                });
    }

    private void init() {
        Calendar c = Calendar.getInstance();
        mNowYear = c.get(Calendar.YEAR);
        mNowMonth = c.get(Calendar.MONTH) + 1;
        mYear = mNowYear;
        mTrainCountTvMoon.setText(mNowYear + "年");
        mTrainCountTvTimeCount.setText("本年训练总时间");
        mTrainCountTvTimeMean.setText("本年平均训练时间");
        mTrainCountTv1.setText("小时");
        mTrainCountTv2.setText("小时");
        if (mYear == mNowYear)
            mTrainCountBtnDown.setEnabled(false);
    }

    private void initHistogram(TrainYearCountBean trainYearCountBean) {
        List<String> stringX = trainYearCountBean.arraypoint.get(0);
        List<String> stringY = trainYearCountBean.arraypoint.get(1);
        mDatas = new ArrayList<>();
        for (int i = 0; i < trainYearCountBean.arraypoint.get(0).size(); i++) {
            String s = stringY.get(i);
            float m = (float) (Integer.parseInt(s) / 3600.0);
            mDatas.add(new Histogram.PPHistogramBean(m, stringX.get(i)));

        }
        int i = (int) (Integer.parseInt(trainYearCountBean.max) / 3600.0 + .5f);
        LogUtils.d("i =" + i);
        i = i + (5 - i % 5);
        mTrainCountFlHistogram.setmDatas(mDatas, i);
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
                mTrainCountTvMoon.setText(--mNowYear + "年");
                initData();
                mTrainCountBtnDown.setEnabled(true);


                break;
            case R.id.train_count_btn_down:
                mTrainCountTvMoon.setText(++mNowYear + "年");
                initData();
                if (mYear == mNowYear)
                    mTrainCountBtnDown.setEnabled(false);
                break;
        }
    }
}

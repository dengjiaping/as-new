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
import com.jkpg.ruchu.bean.TrainCountBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.DateUtil;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.Histogram;
import com.lzy.okgo.OkGo;

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

public class TrainCountMountFragment extends Fragment {
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
        String month;
        if (mNowMonth < 10) {
            month = "0" + (mNowMonth + "");
        } else {
            month = (mNowMonth + "");

        }
        OkGo
                .post(AppUrl.EXERCISECOUNT)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("yearsmonth", mNowYear + "-" + month)
                .execute(new StringDialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        TrainCountBean trainCountBean = new Gson().fromJson(s, TrainCountBean.class);
                        initHistogram(trainCountBean);
                        int i = trainCountBean.countsm * 1000;
                        mTrainCountTvTimeCountNumber.setText(DateUtil.dateFormat(i + "", "mm"));
                        int ss = Integer.parseInt(DateUtil.dateFormat((trainCountBean.averageday * 1000) + "", "ss")) / 60;
                        mTrainCountTvTimeMeanNumber.setText(DateUtil.dateFormat((trainCountBean.averageday * 1000) +"", "mm")+ "." + ss);
                    }
                });
    }

    private void init() {
        Calendar c = Calendar.getInstance();
        mNowYear = c.get(Calendar.YEAR);
        mNowMonth = c.get(Calendar.MONTH) + 1;

        mTrainCountTvMoon.setText(mNowYear + " 年 " + mNowMonth + " 月");
        mTrainCountBtnDown.setEnabled(false);
//        long nowTime = System.currentTimeMillis();
//        String s = DateUtil.dateFormat(nowTime + "", "yyyy 年 MM 月");
//        mTrainCountTvMoon.setText(s);

    }

    private void initHistogram(TrainCountBean trainCountBean) {
        List<String> stringsX = trainCountBean.arraypoint.get(0);
        List<String> stringsY = trainCountBean.arraypoint.get(1);
        mDatas = new ArrayList<>();
        for (int i = 0; i < stringsX.size(); i++) {
            String s = stringsY.get(i);
            int m = Integer.parseInt(s) * 1000;
            mDatas.add(new Histogram.PPHistogramBean(Integer.parseInt(DateUtil.dateFormat(m + "", "mm")), stringsX.get(i)));
        }
        int i = Integer.parseInt(trainCountBean.max) * 1000;
        mTrainCountFlHistogram.setmDatas(mDatas, Integer.parseInt(DateUtil.dateFormat(i + "", "mm")) + 10);
        mTrainCountFlHistogram.setCountInOne(7);
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
                mNowMonth--;
                if (mNowMonth <= 0) {
                    mNowYear--;
                    mNowMonth = 12;
                }
                mTrainCountTvMoon.setText(mNowYear + " 年 " + mNowMonth + " 月");
                mTrainCountBtnDown.setEnabled(true);
                initData();
                break;
            case R.id.train_count_btn_down:
                mNowMonth++;
                if (mNowMonth > 12) {
                    mNowYear++;
                    mNowMonth = 1;
                }
                mTrainCountTvMoon.setText(mNowYear + " 年 " + mNowMonth + " 月");
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH) + 1;
                if (month == mNowMonth && year == mNowYear)
                    mTrainCountBtnDown.setEnabled(false);
                initData();
                break;
        }
    }
}

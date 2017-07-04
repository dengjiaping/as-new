package com.jkpg.ruchu.view.activity.train;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.adapter.ChartRLAdapter;
import com.jkpg.ruchu.widget.leafchart.LeafLineChart;
import com.jkpg.ruchu.widget.leafchart.LineChart;
import com.jkpg.ruchu.widget.leafchart.bean.Axis;
import com.jkpg.ruchu.widget.leafchart.bean.AxisValue;
import com.jkpg.ruchu.widget.leafchart.bean.Line;
import com.jkpg.ruchu.widget.leafchart.bean.PointValue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/2.
 */

public class StartTrainActivity3 extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.header_iv_right)
    ImageView mHeaderIvRight;
    @BindView(R.id.header_tv_right)
    TextView mHeaderTvRight;
    @BindView(R.id.start_train_title)
    TextView mStartTrainTitle;
    @BindView(R.id.start_train_total_time)
    TextView mStartTrainTotalTime;
    @BindView(R.id.start_train_section)
    TextView mStartTrainSection;
    @BindView(R.id.line_chart)
    LeafLineChart mLineChart;
    @BindView(R.id.start_train_start)
    ImageView mStartTrainStart;
    @BindView(R.id.start_train)
    RelativeLayout mStartTrain;
    @BindView(R.id.start_train_iv_grade)
    ImageView mStartTrainIvGrade;
    @BindView(R.id.start_train_recycle_view)
    RecyclerView mStartTrainRecycleView;
    @BindView(R.id.start_train_tv_progress_time)
    TextView mStartTrainTvProgressTime;
    @BindView(R.id.start_train_tv_progress_total)
    TextView mStartTrainTvProgressTotal;
    @BindView(R.id.start_train_progressBar)
    ProgressBar mStartTrainProgressBar;
    @BindView(R.id.start_train_iv_voice)
    CheckBox mStartTrainIvVoice;

    private AnimatorSet mAnimatorSet; //动画集合
    private List<int[]> charts;       //图表集合
    private List<Animator> mAnimators = new ArrayList<>();//所有的动画列表
    private String totalTime;         //总时间
    private ArrayList<Line> mLine;    // 点的集合
    private List<LineChart> mLines;  //线的集合
    private int nowPosition = 0;
    AutoProgressTask mTask;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_train2);
        ButterKnife.bind(this);

        initData();
        initHeader();
        initRecycleView();

        initLineChart(0, charts.get(0).length);  // 默认显示第一个图
        mAnimatorSet.pause();//打开时设置暂停状态
    }

    private void initData() {
        charts = new ArrayList<>();
        charts.add(new int[]{2, 3, 4, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2});
        charts.add(new int[]{2, 4, 0, 0, 0, 4, 1, 4, 1, 4, 1, /*4, 1, 4, 2*/});
        charts.add(new int[]{2, 3, 4, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2});
        charts.add(new int[]{2, 4, 1, 4, 1, 4, 1, 4, 1, 4, 1, /*4, 1, 4, 2*/});
        if (mTask == null) {
            mTask = new AutoProgressTask();
        }
        mTask.start();
    }

    @OnClick({R.id.header_iv_left, R.id.line_chart, R.id.start_train_iv_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                /*if (mAnimatorSet.isRunning()) {
                    mAnimatorSet.pause();
                    mStartTrainStart.setVisibility(View.VISIBLE);
                    okOut();
                } else {
                    finish();
                }*/
                break;
            case R.id.line_chart:
                chartPauseOrResume();
                break;
            case R.id.start_train_iv_voice:
                break;
        }
    }

    private void chartPauseOrResume() {
        /*if (!mAnimatorSet.isPaused()) {
            mAnimatorSet.pause();
            mTask.stop();
            mStartTrainStart.setVisibility(View.VISIBLE);
        } else if (mAnimatorSet.isPaused()) {
            mAnimatorSet.resume();
            mTask.start();
            mStartTrainStart.setVisibility(View.GONE);
        }*/
    }

    private void initHeader() {
        mHeaderTvTitle.setText("训练中");
    }

    private void initLineChart(int index, int numberX) {
        Axis axisX = new Axis(getAxisValuesX(numberX));
        axisX.setAxisColor(Color.parseColor("#FCA29A")).setTextColor(Color.WHITE)
                .setHasLines(false).setAxisLineColor(Color.parseColor("#FCA29A"));
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#FCA29A")).setTextColor(Color.WHITE).setHasLines(false);
        mLineChart.setAxisX(axisX);
        mLineChart.setAxisY(axisY);

        mLine = new ArrayList<>();
        mLineChart.invalidate();
        mLine.add(getDottedLine(charts.get(index)));
        mLineChart.setChartData(mLine);

        mLines = new ArrayList<>();
        for (int i = 0; i < charts.get(index).length - 1; i++) {
            LogUtils.i(i + "");
            LineChart lineChart = new LineChart(UIUtils.getContext());
            lineChart.setLayoutParams(mStartTrain.getLayoutParams());
            mStartTrain.addView(lineChart);
            mLines.add(lineChart);

        }
        mAnimators = new ArrayList<>();
        for (int i = 0; i < mLines.size(); i++) {
            LineChart lineChart = mLines.get(i);
            lineChart.setLineData(mLine);
            lineChart.setI(i);
            lineChart.showWithAnimation(1000);
            mAnimators.add(lineChart.getAnimator());
        }
      //  setAnimator(mAnimators);
    }

    private List<AxisValue> getAxisValuesX(int numberX) {
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < numberX; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(i + " ");
            axisValues.add(value);
        }
        return axisValues;
    }


    private List<AxisValue> getAxisValuesY() {
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(String.valueOf(i) + "  ");
            axisValues.add(value);
        }
        return axisValues;
    }

    private Line getDottedLine(int[] points) {
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            pointValues.add(new PointValue((i / (float) (points.length - 1)), points[i] / 6f));
        }
        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#FAD719"))
                .setLineWidth(3)
                .setHasPoints(false)
                .setPointRadius(0);
        return line;
    }

    private void setAnimator(List<Animator> animators) {
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playSequentially(animators);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                LogUtils.i("onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtils.i("onAnimationEnd");
                nowPosition++;
                if (nowPosition >= charts.size()) {
                    mAnimatorSet.pause();
                    mStartTrainStart.setVisibility(View.VISIBLE);
                    return;
                }
                for (int i = 0; i < mAnimators.size(); i++) {
                    ((ObjectAnimator) mAnimators.get(i)).setCurrentPlayTime(0);
                }
                initLineChart(nowPosition, charts.get(nowPosition).length);
                mLinearLayoutManager.scrollToPositionWithOffset(nowPosition, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LogUtils.i("onAnimationCancel");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                LogUtils.i("onAnimationRepeat");
            }
        });
        mAnimatorSet.start();
    }

    private void initRecycleView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mStartTrainRecycleView.setLayoutManager(mLinearLayoutManager);
        mStartTrainRecycleView.setAdapter(new ChartRLAdapter(charts));
    }

    private class AutoProgressTask implements Runnable {
        @Override
        public void run() {


            start();
        }

        private void start() {
            stop();
            MyApplication.getMainThreadHandler().postDelayed(this, 100);
        }

        private void stop() {
            MyApplication.getMainThreadHandler().removeCallbacks(this);
        }

    }

    private void okOut() {
        new AlertDialog.Builder(this)
                .setTitle("退出训练")
                .setMessage("本次训练尚未结束，要狠心退出，还是要坚持一下！")
                .setNegativeButton("放弃本次", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setPositiveButton("继续训练", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}

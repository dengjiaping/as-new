package com.jkpg.ruchu.view.activity.train;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/6/2.
 */

public class StartTrainActivity2 extends AppCompatActivity {
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

    private List<int[]> charts;       //图表集合
    private List<Animator> mAnimators = new ArrayList<>();//所有的动画列表
    private String totalTime;         //总时间
    private ArrayList<Line> mLine;    // 点的集合
    private List<LineChart> mLines;  //线的集合
    private AutoProgressTask mTask;
    private LinearLayoutManager mLinearLayoutManager;


    private int timeCount; // 计时
    private int numLine = -1; //第几条线
    private int numChart = 0; //第几个表
    private ObjectAnimator mAnimator;
    private AudioManager mAudioManager;
    private boolean isTrain = true;
    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> soundID = new HashMap<>();
    private int nowSound;
    private Map<Integer, Boolean> mMap;
    private ChartRLAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_train2);
        ButterKnife.bind(this);

        initData();
        initHeader();
        initRecycleView();
        initSound();

        initLineChart(0, charts.get(0).length);  // 默认显示第一个图
        /*
         *判断音量设置音量图标
         */
        mAudioManager = (AudioManager) UIUtils.getContext().getSystemService(Context.AUDIO_SERVICE);
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        LogUtils.i("当前音量" + streamVolume);
        if (streamVolume > 0) {
            mStartTrainIvVoice.setChecked(true);
        }
        mStartTrainSection.setText(1 + "/" + charts.size() + "节"); //初始化小节
    }

    private void initData() {
        charts = new ArrayList<>();
        charts.add(new int[]{2, 3, 4, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2});
        charts.add(new int[]{2, 4, 0, 0, 0, 4, 1, 4, 1, 4, 1, /*4, 1, 4, 2*/});
        charts.add(new int[]{2, 3, 4, 2, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2});
        charts.add(new int[]{2, 4, 1, 4, 1, 4, 1, 4, 1, 4, 1, 2, 3/*4, 1, 4, 2*/});
        if (mTask == null) {
            mTask = new AutoProgressTask();
        }
    }

    private void initSound() {
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundID.put(0, mSoundPool.load(this, R.raw.a, 1));
        soundID.put(1, mSoundPool.load(this, R.raw.b, 1));
        soundID.put(2, mSoundPool.load(this, R.raw.c, 1));

    }

    @OnClick({R.id.header_iv_left, R.id.line_chart, R.id.start_train_iv_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                if (isTrain) {
                    if (mAnimator == null || mTask == null || mSoundPool == null) {
                        finish();
                    } else {
                        mAnimator.pause();
                        mTask.stop();
                        mStartTrainStart.setVisibility(View.VISIBLE);
                        mSoundPool.pause(nowSound);
                        okOut();
                    }
                } else {
                    finish();
                }
                break;
            case R.id.line_chart:
                chartPauseOrResume();
                break;
            case R.id.start_train_iv_voice:
                if (getMediaVolume() > 0) {
                    setMediaVolume(0);
                } else {
                    setMediaVolume(7);
                }
                break;
        }
    }

    private int getMediaVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    private void setMediaVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
    }

    private void chartPauseOrResume() {
        if (!isTrain) {
            numChart = 0;
            for (int i = 0; i < mLines.size(); i++) {
                mStartTrain.removeView(mLines.get(i));
            }
            isTrain = true;
            mMap.put(0, true);
            mMap.put(mMap.size() - 1, false);
            mAdapter.notifyItemChanged(0);
            mAdapter.notifyItemChanged(mMap.size() - 1);
            mTask.start();
            mStartTrainStart.setVisibility(View.GONE);
            initLineChart(numChart, charts.get(numChart).length);
            mLinearLayoutManager.scrollToPositionWithOffset(numChart, 0);
            mStartTrainSection.setText(numChart + 1 + "/" + charts.size() + "节");
        } else {
            mTask.start();
            isTrain = true;
            mStartTrainStart.setVisibility(View.GONE);
            if (mAnimator != null) {
                if (!mAnimator.isPaused()) {
                    mAnimator.pause();
                    mTask.stop();
                    mSoundPool.pause(nowSound);
                    LogUtils.i("stop" + nowSound);
                    mStartTrainStart.setVisibility(View.VISIBLE);
                } else if (mAnimator.isPaused()) {
                    mAnimator.resume();
                    mTask.start();
                    mSoundPool.resume(nowSound);
                    LogUtils.i("start" + nowSound);
                    mStartTrainStart.setVisibility(View.GONE);
                }
            }
        }

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
            LineChart lineChart = new LineChart(UIUtils.getContext());
            lineChart.setLayoutParams(mStartTrain.getLayoutParams());
            mStartTrain.addView(lineChart);
            mLines.add(lineChart);
        }

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


    private void initRecycleView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mStartTrainRecycleView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new ChartRLAdapter(charts);
        mStartTrainRecycleView.setAdapter(mAdapter);
        mMap = mAdapter.getMap();
        mMap.put(0, true);
        mAdapter.notifyItemChanged(0);
    }

    private class AutoProgressTask implements Runnable {
        @Override
        public void run() {

            timeCount += 100;
            if (timeCount == 1000) {
                LogUtils.i("timeCount=" + timeCount);
                timeCount = 0;
                if (numLine >= charts.get(numChart).length - 2) {
                    numChart++;
                    numLine = -1;
                    LogUtils.i("numChart=" + numChart);
                    LogUtils.i("charts.size()=" + (charts.size()));
                    if (numChart >= charts.size()) {
                        mStartTrainStart.setVisibility(View.VISIBLE);
                        LogUtils.i("-----怎么不结束！！！！");
                        isTrain = false;
                        mTask.stop();
                        return;
                    } else {
                       /* for (int i = 0; i < mAnimators.size(); i++) {
                            ((ObjectAnimator) mAnimators.get(i)).setCurrentPlayTime(0);
                        }*/
                        for (int i = 0; i < mLines.size(); i++) {
                            mStartTrain.removeView(mLines.get(i));
                        }
                        initLineChart(numChart, charts.get(numChart).length);
                        mLinearLayoutManager.scrollToPositionWithOffset(numChart, 0);

                        mMap.put(numChart, true);
                        mMap.put(numChart - 1, false);
                        mAdapter.notifyDataSetChanged();

                        mStartTrainSection.setText(numChart + 1 + "/" + charts.size() + "节");

                    }
                }
                if (mAnimator != null) {
                    mAnimator.cancel();
                }
                numLine++;
                LogUtils.i("numLine" + numLine);
                LineChart lineChart = mLines.get(numLine);
                lineChart.setLineData(mLine);
                lineChart.setI(numLine);
                lineChart.showWithAnimation(1000);
                mAnimator = lineChart.getAnimator();
                mAnimators.add(mAnimator);
                mAnimator.start();

                // sound!!
                int[] points = charts.get(numChart);
                if (points[numLine] > points[numLine + 1]) {
                    mSoundPool.stop(soundID.get(1));
                    mSoundPool.stop(soundID.get(2));
                    int play = mSoundPool.play(soundID.get(0), 1, 1, 0, 0, 1);
                    nowSound = play;
                    LogUtils.i(">" + nowSound + "   play=" + play);
                } else if (points[numLine] == points[numLine + 1]) {
                    mSoundPool.stop(soundID.get(0));
                    mSoundPool.stop(soundID.get(2));
                    int play = mSoundPool.play(soundID.get(1), 1, 1, 0, 0, 1);
                    nowSound = play;
                    LogUtils.i("=" + nowSound + "   play=" + play);

                } else if (points[numLine] < points[numLine + 1]) {
                    mSoundPool.stop(soundID.get(0));
                    mSoundPool.stop(soundID.get(1));
                    int play = mSoundPool.play(soundID.get(2), 1, 1, 0, 0, 1);
                    nowSound = play;
                    LogUtils.i("<" + nowSound + "   play=" + play);

                }
            }
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
                        mAnimator.resume();
                        mTask.start();
                        LogUtils.i("start");
                        mStartTrainStart.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getMainThreadHandler().removeCallbacks(mTask);
        mSoundPool.release();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isTrain) {
                    if (mAnimator == null || mTask == null || mSoundPool == null) {
                        finish();

                    } else {
                        mAnimator.pause();
                        mTask.stop();
                        mStartTrainStart.setVisibility(View.VISIBLE);
                        mSoundPool.pause(nowSound);
                        okOut();
                    }
                } else {
                    finish();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (getMediaVolume() > 0)
                    mStartTrainIvVoice.setChecked(true);
                return super.onKeyDown(keyCode, event);
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (getMediaVolume() == 0)
                    mStartTrainIvVoice.setChecked(false);
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

}

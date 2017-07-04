package com.jkpg.ruchu.view.activity.train;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.TrainPointBean;
import com.jkpg.ruchu.utils.DateUtil;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/18.
 */

public class StartTrainActivity extends AppCompatActivity {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.start_train_title)
    TextView mStartTrainTitle;
    @BindView(R.id.start_train_total_time)
    TextView mStartTrainTotalTime;
    @BindView(R.id.start_train_section)
    TextView mStartTrainSection;
    @BindView(R.id.line_chart)
    LeafLineChart mLineChart;
    @BindView(R.id.line0)
    LineChart mLine0;
    @BindView(R.id.line1)
    LineChart mLine1;
    @BindView(R.id.line2)
    LineChart mLine2;
    @BindView(R.id.line3)
    LineChart mLine3;
    @BindView(R.id.line4)
    LineChart mLine4;
    @BindView(R.id.line5)
    LineChart mLine5;
    @BindView(R.id.line6)
    LineChart mLine6;
    @BindView(R.id.line7)
    LineChart mLine7;
    @BindView(R.id.line8)
    LineChart mLine8;
    @BindView(R.id.line9)
    LineChart mLine9;
    @BindView(R.id.start_train_start)
    ImageView mStartTrainStart;
    @BindView(R.id.start_train)
    FrameLayout mStartTrain;
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
    private AnimatorSet mAnimatorSet;


    private List<int[]> charts;
    private List<Animator> mAnimators;

    private int nowPosition = 0; //
    private LinearLayoutManager mLinearLayoutManager;

    private String totalTime;

    private AutoProgressTask mProgressTask = new AutoProgressTask();

    private int progress = 0;
    private AudioManager mAudioManager;

    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> soundID = new HashMap<>();
    private int index;
    private int id;//当前播放的id


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_train);
        ButterKnife.bind(this);

        TrainPointBean trainPointBean = (TrainPointBean) getIntent().getSerializableExtra("trainPointBean");
        charts = trainPointBean.arr;
        totalTime = trainPointBean.arr.size() * 10000 + "";
        initSound();


        //initData();
        if (charts != null && charts.size() != 0) {
            initLineChart(0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mAnimatorSet.pause();
            }

            mStartTrainTotalTime.setText("共计约" + DateUtil.dateFormat(totalTime, "mm分ss秒"));
            mStartTrainSection.setText(1 + "/" + charts.size() + " 节");
        } else {
            mLineChart.setClickable(false);
        }

        initHeader();

        initRecycleView();

        initProgressBar();

        mAudioManager = (AudioManager) UIUtils.getContext().getSystemService(Context.AUDIO_SERVICE);

        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        LogUtils.i("当前音量" + streamVolume);
        if (streamVolume > 0) {
            mStartTrainIvVoice.setChecked(true);
        }

    }

    private void initSound() {
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundID.put(0, mSoundPool.load(this, R.raw.a, 1));
        soundID.put(1, mSoundPool.load(this, R.raw.b, 1));
        soundID.put(2, mSoundPool.load(this, R.raw.c, 1));

    }

    private void initProgressBar() {
        mStartTrainProgressBar.setMax(Integer.parseInt(totalTime));
        mStartTrainProgressBar.setProgress(0);
        mStartTrainTvProgressTotal.setText(DateUtil.dateFormat(totalTime, "mm:ss"));
    }

    private void initRecycleView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mStartTrainRecycleView.setLayoutManager(mLinearLayoutManager);
        mStartTrainRecycleView.setAdapter(new ChartRLAdapter(charts));
    }

    private void initHeader() {
        mHeaderTvTitle.setText("训练中");
    }


    private void initLineChart(int index) {
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#FCA29A")).setTextColor(Color.WHITE)
                .setHasLines(false).setAxisLineColor(Color.parseColor("#FCA29A"));
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#FCA29A")).setTextColor(Color.WHITE).setHasLines(false);
        mLineChart.setAxisX(axisX);
        mLineChart.setAxisY(axisY);

        ArrayList<Line> line = new ArrayList<>();


        mLineChart.invalidate();
        line.add(getDottedLine(charts.get(index)));
        mLineChart.setChartData(line);

        List<LineChart> lines = new ArrayList<>();
        mAnimators = new ArrayList<>();
        lines.add(mLine0);
        lines.add(mLine1);
        lines.add(mLine2);
        lines.add(mLine3);
        lines.add(mLine4);
        lines.add(mLine5);
        lines.add(mLine6);
        lines.add(mLine7);
        lines.add(mLine8);
        lines.add(mLine9);
        for (int i = 0; i < lines.size(); i++) {
            LineChart lineChart = lines.get(i);
            lineChart.setLineData(line);
            lineChart.setI(i);
            lineChart.showWithAnimation(1000);
            mAnimators.add(lineChart.getAnimator());
        }
        setAnimator(mAnimators);

    }

    private void setAnimator(List<Animator> animators) {
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playSequentially(animators);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAnimatorSet.addPauseListener(new Animator.AnimatorPauseListener() {
                @Override
                public void onAnimationPause(Animator animation) {
                    LogUtils.i("animation", "onAnimationPause");

                }

                @Override
                public void onAnimationResume(Animator animation) {
                    LogUtils.i("animation", "onAnimationResume");

                }
            });
        }
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                LogUtils.i("animation", "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ((ObjectAnimator) mAnimators.get(mAnimators.size() - 1)).setCurrentPlayTime(0);
                /*if (nowPosition >= charts.size() - 1) {
                    MyApplication.getMainThreadHandler().removeCallbacks(mProgressTask);

                }*/
                /*for (int i = 0; i < mAnimators.size(); i++) {
                    ((ObjectAnimator) mAnimators.get(i)).setCurrentPlayTime(0);
                }
                if (nowPosition >= charts.size() - 1) {
                    mStartTrainStart.setVisibility(View.VISIBLE);
                    progress = 0;
                    mStartTrainTvProgressTime.setText(DateUtil.dateFormat(totalTime, "mm:ss"));
                    MyApplication.getMainThreadHandler().removeCallbacks(mProgressTask);

                } else {
                    initLineChart(++nowPosition);
                    mLinearLayoutManager.scrollToPositionWithOffset(nowPosition, 0);
                    LogUtils.i(nowPosition * 10000 + "ssssssssssssssssss");
                    progress = nowPosition * 10000;//重新设置时间，忽略误差

                }
                mStartTrainSection.setText(nowPosition + 1 + "/" + charts.size() + "节");*/
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LogUtils.i("animation", "onAnimationCancel");

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                LogUtils.i("animation", "onAnimationRepeat");

            }
        });

        mAnimatorSet.start();
    }

    private List<AxisValue> getAxisValuesX() {
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i <= 10; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(i + " s");
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
            pointValues.add(new PointValue(i / 10f, points[i] / 6f));
        }
        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#FAD719"))
                .setLineWidth(3)
                .setHasPoints(false)
                .setPointRadius(0)
                /*.setHasPoints(false)
                .setPointColor(Color.parseColor("#ffffff"))*/;
        return line;
    }

    @OnClick({R.id.header_iv_left, R.id.line_chart, R.id.start_train_iv_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                if (mAnimatorSet.isRunning()) {
                    mAnimatorSet.pause();
                    mStartTrainStart.setVisibility(View.VISIBLE);
                    okOut();
                } else {
                    finish();
                }
                break;
            case R.id.line_chart:
                mStartTrainStart.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (!mAnimatorSet.isRunning()) {
                        //mAnimatorSet.start();
                        for (int i = 0; i < mAnimators.size(); i++) {
                            ((ObjectAnimator) mAnimators.get(i)).setCurrentPlayTime(0);
                        }
                        nowPosition = 0;
                        initLineChart(0);
                        progress = 0;
                        index = 0;
                        mStartTrainProgressBar.setProgress(0);
                        mStartTrainTvProgressTime.setText("00:00");
                        LogUtils.i("---------重新开始------------");
                        int[] points = charts.get(nowPosition);
                        if (points[index] > points[index + 1]) {
                            mSoundPool.stop(soundID.get(1));
                            mSoundPool.stop(soundID.get(2));
                            mSoundPool.play(soundID.get(0), 1, 1, 0, 0, 1);
                            id = 0;
                            LogUtils.i(">");
                        } else if (points[index] == points[index + 1]) {
                            mSoundPool.stop(soundID.get(0));
                            mSoundPool.stop(soundID.get(2));
                            mSoundPool.play(soundID.get(1), 1, 1, 0, 0, 1);
                            id = 1;
                            LogUtils.i("=");

                        } else if (points[index] < points[index + 1]) {
                            mSoundPool.stop(soundID.get(0));
                            mSoundPool.stop(soundID.get(1));
                            mSoundPool.play(soundID.get(2), 1, 1, 0, 0, 1);
                            id = 2;
                            LogUtils.i("<");
                        }
                        mProgressTask = new AutoProgressTask();
                        mProgressTask.start();
                    } else if (!mAnimatorSet.isPaused()) {
                        LogUtils.i("--------pause--------------");
                        mProgressTask.stop();
                        mAnimatorSet.pause();
                        mStartTrainStart.setVisibility(View.VISIBLE);

//                        mSoundPool.pause(soundID.get(id));

                    } else if (mAnimatorSet.isPaused()) {
                        mAnimatorSet.resume();
                        mProgressTask.start();
//                        mSoundPool.resume(soundID.get(id));
                        mStartTrainStart.setVisibility(View.GONE);
                        if (nowPosition == 0) {
                            int[] points = charts.get(nowPosition);
                            if (points[index] > points[index + 1]) {
                                mSoundPool.stop(soundID.get(1));
                                mSoundPool.stop(soundID.get(2));
                                mSoundPool.play(soundID.get(0), 1, 1, 0, 0, 1);
                                id = 0;
                                LogUtils.i(">");
                            } else if (points[index] == points[index + 1]) {
                                mSoundPool.stop(soundID.get(0));
                                mSoundPool.stop(soundID.get(2));
                                mSoundPool.play(soundID.get(1), 1, 1, 0, 0, 1);
                                id = 1;
                                LogUtils.i("=");

                            } else if (points[index] < points[index + 1]) {
                                mSoundPool.stop(soundID.get(0));
                                mSoundPool.stop(soundID.get(1));
                                mSoundPool.play(soundID.get(2), 1, 1, 0, 0, 1);
                                id = 2;
                                LogUtils.i("<");
                            }
                        }
                        LogUtils.i("--------resume--------------");

                    }
                }
                break;
            case R.id.start_train_iv_voice:
                //  mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, !mStartTrainIvVoice.isChecked());
                if (getMediaVolume() > 0) {
                    setMediaVolume(0);
                } else {
                    setMediaVolume(7);
                }
                break;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mAnimatorSet.isRunning()) {
                    mStartTrainStart.setVisibility(View.VISIBLE);
                    mAnimatorSet.pause();
                    okOut();
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


    private class AutoProgressTask implements Runnable {
        @Override
        public void run() {
            progress += 1000;
            mStartTrainProgressBar.setProgress(progress);
            LogUtils.i(progress + "s");


            index += 1;
            if (index >= 10) {
                index = 0;
            }
            if (index == 0) {
                for (int i = 0; i < mAnimators.size(); i++) {
                    ((ObjectAnimator) mAnimators.get(i)).setCurrentPlayTime(0);
                }

                if (nowPosition >= charts.size() - 1) {
                    mStartTrainStart.setVisibility(View.VISIBLE);
                    // progress = 0;
                    // mStartTrainTvProgressTime.setText(DateUtil.dateFormat(totalTime, "mm:ss"));
//                    mProgressTask.stop();

                } else {
                    initLineChart(++nowPosition);
                    mLinearLayoutManager.scrollToPositionWithOffset(nowPosition, 0);
                    LogUtils.i(nowPosition * 10000 + "ssssssssssssssssss");
                    //    progress = nowPosition * 10000;//重新设置时间，忽略误差

                }

            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mStartTrainTvProgressTime.setText(DateUtil.dateFormat(progress + "", "mm:ss"));
                    mStartTrainSection.setText(nowPosition + 1 + "/" + charts.size() + "节");
                }
            });
            if (progress == Integer.parseInt(totalTime)) {
                mProgressTask.stop();
                LogUtils.i("mProgressTask.stop()" + Integer.parseInt(totalTime));
                return;
            }
            int[] points = charts.get(nowPosition);
            if (points[index] > points[index + 1]) {
                mSoundPool.stop(soundID.get(1));
                mSoundPool.stop(soundID.get(2));
                mSoundPool.play(soundID.get(0), 1, 1, 0, 0, 1);
                LogUtils.i(">");
            } else if (points[index] == points[index + 1]) {
                mSoundPool.stop(soundID.get(0));
                mSoundPool.stop(soundID.get(2));
                mSoundPool.play(soundID.get(1), 1, 1, 0, 0, 1);
                LogUtils.i("=");

            } else if (points[index] < points[index + 1]) {
                mSoundPool.stop(soundID.get(0));
                mSoundPool.stop(soundID.get(1));
                mSoundPool.play(soundID.get(2), 1, 1, 0, 0, 1);
                LogUtils.i("<");
            }
            LogUtils.i(index + "");
            start();
        }

        private void start() {
            stop();
            MyApplication.getMainThreadHandler().postDelayed(this, 1000);
        }

        private void stop() {
            MyApplication.getMainThreadHandler().removeCallbacks(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getMainThreadHandler().removeCallbacks(mProgressTask);
        mSoundPool.release();
    }

    private int getMediaVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    private void setMediaVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
    }
}

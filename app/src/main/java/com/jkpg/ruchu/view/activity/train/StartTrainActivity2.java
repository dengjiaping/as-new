package com.jkpg.ruchu.view.activity.train;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jkpg.ruchu.R;
import com.jkpg.ruchu.base.BaseActivity;
import com.jkpg.ruchu.base.MyApplication;
import com.jkpg.ruchu.bean.StartTrainBean;
import com.jkpg.ruchu.callback.StringDialogCallback;
import com.jkpg.ruchu.config.AppUrl;
import com.jkpg.ruchu.config.Constants;
import com.jkpg.ruchu.utils.DateUtil;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.PopupWindowUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.view.activity.MainActivity;
import com.jkpg.ruchu.view.adapter.ChartRLAdapter;
import com.jkpg.ruchu.widget.leafchart.LeafLineChart;
import com.jkpg.ruchu.widget.leafchart.LineChart;
import com.jkpg.ruchu.widget.leafchart.bean.Axis;
import com.jkpg.ruchu.widget.leafchart.bean.AxisValue;
import com.jkpg.ruchu.widget.leafchart.bean.Line;
import com.jkpg.ruchu.widget.leafchart.bean.PointValue;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by qindi on 2017/6/2.
 */

public class StartTrainActivity2 extends BaseActivity {
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
    @BindView(R.id.line_text)
    TextView mLineText;
    @BindView(R.id.start_train_start)
    ImageView mStartTrainStart;
    @BindView(R.id.start_train)
    RelativeLayout mStartTrain;
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
    @BindView(R.id.start_train_now_one)
    TextView mStartTrainNowOne;
    @BindView(R.id.start_train_total_one)
    TextView mStartTrainTotalOne;
    @BindView(R.id.start_train_tip)
    ImageView mStartTrainTip;

    private List<float[]> charts;       //图表集合
    private List<float[]> chartsX;       //图表x集合
    private List<Animator> mAnimators = new ArrayList<>();//所有的动画列表
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
    private int nowSound;
    private Map<Integer, Boolean> mMap;
    private ChartRLAdapter mAdapter;
    private boolean isPause; //是否暂停
    private PopupWindow mPopupWindowSuccess;
    private StartTrainBean mStartTrainBean;
    private String mStartTime;
    private MyVolumeReceiver mVolumeReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_train2);
        ButterKnife.bind(this);

        initData();
        initHeader();
        initSound();

        /*
         *判断音量设置音量图标
         */
        mAudioManager = (AudioManager) UIUtils.getContext().getSystemService(Context.AUDIO_SERVICE);
        int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        LogUtils.i("当前音量" + streamVolume);
        if (streamVolume > 0) {
            mStartTrainIvVoice.setChecked(true);
        }

        myRegisterReceiver();


        if (mTask == null) {
            mTask = new AutoProgressTask();
        }

        recordTime();
    }

    private void recordTime() {
        mStartTrainTvProgressTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("00:00")) {
                    long timeMillis = System.currentTimeMillis();
                    mStartTime = DateUtil.dateFormat(timeMillis + "", "HH:mm");
                    LogUtils.i(mStartTime + "=timeMillis");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initData() {

        OkGo
                .post(AppUrl.STARTEXERCISE)
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .execute(new StringDialogCallback(StartTrainActivity2.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mStartTrainBean = new Gson().fromJson(s, StartTrainBean.class);
                        init(mStartTrainBean);
                        initDot(mStartTrainBean);

                    }
                });


    }

    private void initDot(StartTrainBean startTrainBean) {
        charts = new ArrayList<>();
        chartsX = new ArrayList<>();
        List<StartTrainBean.ProgrammeBean> programme = startTrainBean.programme;
        for (int i = 0; i < programme.size(); i++) {
            StartTrainBean.ProgrammeBean programmeBean = programme.get(i);
            float[] floatsX = programmeBean.arr.get(0);
            float[] floatsY = programmeBean.arr.get(1);
            charts.add(floatsY);
            chartsX.add(floatsX);
        }
        initRecycleView();
        initLineChart(0, (int) chartsX.get(0)[(chartsX.get(0).length) - 1] * 2); // 默认显示第一个图
//        mStartTrainSection.setText(1 + "/" + charts.size() + "节"); //初始化小节
        mStartTrainNowOne.setText("当前为第1节");
//        mStartTrainTotalOne.setText("共" + charts.size() + "节");
        mStartTrainProgressBar.setMax(Integer.parseInt(startTrainBean.totaltime) * 1000);

        /*charts = new ArrayList<>();
        chartsX = new ArrayList<>();
        charts.add(new float[]{1, 2, 3, 5, 5});
        chartsX.add(new float[]{0, 2, 5, 7, 14});
        charts.add(new float[]{1, 5, 5, 1});
        chartsX.add(new float[]{0, 6, 9, 10});
        charts.add(new float[]{1, 5, 5, 1});
        chartsX.add(new float[]{0, 6, 9, 10});
        charts.add(new float[]{1, 2, 3, 5, 5});
        chartsX.add(new float[]{0, 2, 5, 7, 14});*/


    }

    private void init(StartTrainBean startTrainBean) {
        mStartTrainTitle.setText("产后康复 " + startTrainBean.level);
        String dateFormat = DateUtil.dateFormat(Integer.parseInt(startTrainBean.totaltime) * 1000 + "", "mm分ss秒");
        mStartTrainTotalTime.setText("共计"+dateFormat);
        mStartTrainSection.setText("第" + startTrainBean.excisedays + "天" + "  " + startTrainBean.level_2 +"级");
        mStartTrainTotalOne.setText("共" + startTrainBean.programme.size() + "节");
        mStartTrainTvProgressTotal.setText(DateUtil.dateFormat(Integer.parseInt(startTrainBean.totaltime) * 1000 + "", "mm:ss"));

    }

    private void initSound() {
        HashMap<Integer, Integer> soundID = new HashMap<>();
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundID.put(0, mSoundPool.load(this, R.raw.a, 1));
        soundID.put(1, mSoundPool.load(this, R.raw.b, 1));
        soundID.put(2, mSoundPool.load(this, R.raw.c, 1));

    }

    @OnClick({R.id.header_iv_left, R.id.start_train_start, R.id.start_train_iv_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                if (isTrain) {
                    if (mAnimator == null || mTask == null || mSoundPool == null) {
                        finish();
                    } else {
                        mAnimator.pause();
                        mTask.stop();
                        mStartTrainStart.setBackgroundResource(R.drawable.icon_start);
                        mSoundPool.pause(nowSound);
                        okOut();
                    }
                } else {
                    finish();
                }
                break;
            case R.id.start_train_start:
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
        mStartTrainStart.setBackgroundResource(R.drawable.icon_pause);
        if (!isTrain) {
            numChart = 0;
            timeCount = 0;
            for (int i = 0; i < mLines.size(); i++) {
                mStartTrain.removeView(mLines.get(i));
            }
            isTrain = true;
            mMap.put(mMap.size() - 1, false);
            mMap.put(0, true);
            mAdapter.notifyItemChanged(mMap.size() - 1);
            mAdapter.notifyItemChanged(0);
            mTask.start();
            mStartTrainStart.setBackgroundResource(R.drawable.icon_pause);
            initLineChart(numChart, (int) chartsX.get(numChart)[(chartsX.get(numChart).length - 1)] * 2);
            mLinearLayoutManager.scrollToPositionWithOffset(numChart, 0);
            scrollRecyclerView(numChart);
//            mStartTrainSection.setText(numChart + 1 + "/" + charts.size() + "节");
            mStartTrainNowOne.setText("当前为第" + (numChart + 1) + "节");
        } else {
            mTask.start();
            isTrain = true;
            if (mAnimator != null) {
                mStartTrainStart.setBackgroundResource(R.drawable.icon_pause);
                LogUtils.i("start one -- click");

//                if (mAnimator.isPaused()) {
                if (isPause) {
                    isPause = false;
                    mAnimator.resume();
                    mTask.start();
                    mSoundPool.resume(nowSound);
                    LogUtils.i("start -- click");
                    mStartTrainStart.setBackgroundResource(R.drawable.icon_pause);

//                } else if (!mAnimator.isPaused() && mAnimator.isStarted()) {
                } else /*if (isPause)*/ {
                    isPause = true;
                    mAnimator.pause();
                    mTask.stop();
                    mSoundPool.pause(nowSound);
                    LogUtils.i("stop -- click");
                    mStartTrainStart.setBackgroundResource(R.drawable.icon_start);

                }
            }
        }
    }

    private void initHeader() {
        mHeaderTvTitle.setText("训练中");
    }

    private void initLineChart(int index, int numberX) {
        Axis axisX = new Axis(getAxisValuesX(numberX));
        axisX.setAxisColor(Color.TRANSPARENT).setTextColor(Color.WHITE)
                .setHasLines(false).setAxisWidth(3);
        Axis axisY = new Axis(getAxisValuesY()).setAxisWidth(3);
        axisY.setAxisColor(Color.TRANSPARENT).setTextColor(Color.WHITE).setHasLines(false);
        mLineChart.setAxisX(axisX);
        mLineChart.setAxisY(axisY);

        mLine = new ArrayList<>();
        mLineChart.invalidate();
        mLine.add(getDottedLine(chartsX.get(index), charts.get(index)));
        mLineChart.setChartData(mLine);

        mLines = new ArrayList<>();
//        for (int i = 0; i < charts.get(index).length - 1; i++) {
        for (int i = 0; i < chartsX.get(index)[(chartsX.get(index).length) - 1] * 2; i++) {
            LineChart lineChart = new LineChart(UIUtils.getContext());
            lineChart.setLayoutParams(mStartTrain.getLayoutParams());
            mStartTrain.addView(lineChart);
            mLines.add(lineChart);
        }
        if (mStartTrainBean.programme.get(index).ishavezero.equals("1")){
            mLineText.setText("充分放松盆底肌");
        } else {
            mLineText.setText("");
        }
        LogUtils.i("mLines = " + mLines.size());
        LogUtils.i("index = " + index);
    }

    private List<AxisValue> getAxisValuesX(int numberX) {
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < numberX + 2; i++) {
            if (numberX + 2 <= 22) {
                if (i % 2 != 1) {
                    AxisValue value = new AxisValue();
                    value.setLabel(i / 2 + " ");
                    axisValues.add(value);
                }
            } else {
                if (i % 4 == 0) {
                    AxisValue value = new AxisValue();
                    value.setLabel(i / 2 + " ");
                    axisValues.add(value);
                }
            }
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

    private Line getDottedLine(float[] pointsX, float[] points) {
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            if (i == 0) {
                pointValues.add(new PointValue(pointsX[i] / pointsX[pointsX.length - 1], points[i] / 6f));
                LogUtils.i("x=" + pointsX[i] + "y=" + points[i]);
                continue;
            }
            if (pointsX[i] - pointsX[i - 1] == 0.5f) {
                pointValues.add(new PointValue(pointsX[i] / pointsX[pointsX.length - 1], points[i] / 6f));
                LogUtils.i("x=" + pointsX[i] + "y=" + points[i]);
                continue;
            }
            if (pointsX[i] - pointsX[i - 1] != 0.5f) {
                float v = (pointsX[i] - pointsX[i - 1]) * 2;
                for (float j = 0; j < v; j++) {
                    float y = (points[i] - points[i - 1]) / (pointsX[i] - pointsX[i - 1]) * 0.5f;
                    pointValues.add(new PointValue((.5f * (j + 1) + pointsX[i - 1]) / pointsX[pointsX.length - 1], (y * (j + 1) + points[i - 1]) / 6f));
                    LogUtils.i("x=" + (.5f * (j + 1) + pointsX[i - 1]) + "y=" + (y * (j + 1) + points[i - 1]));
                }
            }
//            pointValues.add(new PointValue(pointsX[i] / pointsX[pointsX.length - 1], points[i] / 6f));


        }
//            pointValues.add(new PointValue((i / (float) (points.length - 1)), points[i] / 6f));
       /* pointValues.add(new PointValue(0 / 10f, 1 / 6f));
        pointValues.add(new PointValue(6 / 10f, 5 / 6f));
        pointValues.add(new PointValue(9 / 10f, 5 / 6f));
        pointValues.add(new PointValue(10 / 10f, 1 / 6f));
*/
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
        mAdapter = new ChartRLAdapter(chartsX, charts);
        mStartTrainRecycleView.setAdapter(mAdapter);
        mMap = mAdapter.getMap();
        mMap.put(0, true);
        mAdapter.notifyItemChanged(0);
    }

    private class AutoProgressTask implements Runnable {
        @Override
        public void run() {
            mStartTrainProgressBar.setProgress(timeCount);
            String s = DateUtil.dateFormat(timeCount + "", "mm:ss");
            mStartTrainTvProgressTime.setText(s);
            LogUtils.i("timeCount=" + timeCount);
            LogUtils.i(s + "= 00:00");
            timeCount += 100;
            // FIXME: 2017/7/5
            if (timeCount % 500 == 0) {
//                timeCount = 0;
                if (numLine >= chartsX.get(numChart)[(chartsX.get(numChart).length) - 1] * 2 - 1) {
                    numChart++;
                    numLine = -1;
                   /* LogUtils.i("numChart=" + numChart);
                    LogUtils.i("charts.size()=" + (charts.size()));*/
                    if (numChart >= charts.size()) {
                        mStartTrainStart.setBackgroundResource(R.drawable.icon_start);

                        LogUtils.i("-----怎么不结束！！！！");
                        isTrain = false;
                        mTask.stop();
                        showSuccess();
                        return;
                    } else {
                       /* for (int i = 0; i < mAnimators.size(); i++) {
                            ((ObjectAnimator) mAnimators.get(i)).setCurrentPlayTime(0);
                        }*/
                        for (int i = 0; i < mLines.size(); i++) {
                            mStartTrain.removeView(mLines.get(i));
                        }
                        initLineChart(numChart, (int) chartsX.get(numChart)[(chartsX.get(numChart).length - 1)] * 2);

//                        mLinearLayoutManager.scrollToPositionWithOffset(numChart, 0);
                        scrollRecyclerView(numChart);
//                        mLinearLayoutManager.scrollToPosition(numChart);

                        mMap.put(numChart, true);
                        mMap.put(numChart - 1, false);
                        mAdapter.notifyDataSetChanged();

//                        mStartTrainSection.setText(numChart + 1 + "/" + charts.size() + "节");
                        mStartTrainNowOne.setText("当前为第" + (numChart + 1) + "节");


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
//                lineChart.showWithAnimation(1000);
                lineChart.showWithAnimation(500);
                // FIXME: 2017/7/5
                mAnimator = lineChart.getAnimator();
                mAnimators.add(mAnimator);
                mAnimator.start();
               /* // sound!!
                float[] points = charts.get(numChart);
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

                }*/
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
                        mStartTrainStart.setBackgroundResource(R.drawable.icon_pause);
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
        unregisterReceiver(mVolumeReceiver);
        EventBus.getDefault().post("Train");
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
                        mStartTrainStart.setBackgroundResource(R.drawable.icon_start);
                        mSoundPool.pause(nowSound);
                        okOut();
                    }
                } else {
                    finish();
                }
                return true;
           /* case KeyEvent.KEYCODE_VOLUME_UP:
                if (getMediaVolume() > 0)
                    mStartTrainIvVoice.setChecked(true);
                return super.onKeyDown(keyCode, event);
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (getMediaVolume() == 0)
                    mStartTrainIvVoice.setChecked(false);
                return super.onKeyDown(keyCode, event);*/
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 注册当音量发生变化时接收的广播
     */

    private void myRegisterReceiver() {
        mVolumeReceiver = new MyVolumeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(mVolumeReceiver, filter);
    }

    /**
     * 处理音量变化时的界面显示
     *
     * @author qindi
     */
    private class MyVolumeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //如果音量发生变化则更改seekbar的位置
            if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
                int currVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);// 当前的媒体音量
                if (currVolume > 0) {
                    mStartTrainIvVoice.setChecked(true);
                } else {
                    mStartTrainIvVoice.setChecked(false);
                }
            }
        }
    }

    private void showSuccess() {
        OkGo
                .post(AppUrl.EXERCISEEND)
                .params("userid",SPUtils.getString(UIUtils.getContext(),Constants.USERID,""))
                .params("starttime",mStartTime)
                .params("alltimelong",mStartTrainBean.totaltime)
                .execute(new StringDialogCallback(StartTrainActivity2.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mPopupWindowSuccess = new PopupWindow(StartTrainActivity2.this);
                        mPopupWindowSuccess.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                        mPopupWindowSuccess.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
                        View viewPay = LayoutInflater.from(StartTrainActivity2.this).inflate(R.layout.view_train_success, null);
                        viewPay.findViewById(R.id.view_success_btn_ok).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopupWindowSuccess.dismiss();

                            }
                        });
                        ((TextView) viewPay.findViewById(R.id.view_success_integral)).setText("+" + mStartTrainBean.integral + "分");
                        ((TextView) viewPay.findViewById(R.id.view_success_experience)).setText("+" + mStartTrainBean.experience + "分");
                        mPopupWindowSuccess.setContentView(viewPay);
                        mPopupWindowSuccess.setBackgroundDrawable(new ColorDrawable(0x00000000));
                        mPopupWindowSuccess.setOutsideTouchable(false);
                        mPopupWindowSuccess.setFocusable(false);
                        mPopupWindowSuccess.showAtLocation(getLayoutInflater().inflate(R.layout.activity_start_train2, null), Gravity.CENTER, 0, 0);
                        PopupWindowUtils.darkenBackground(StartTrainActivity2.this, .4f);
                        mPopupWindowSuccess.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                PopupWindowUtils.darkenBackground(StartTrainActivity2.this, 1f);
                                startActivity(new Intent(StartTrainActivity2.this, MainActivity.class));
                                finish();
                            }
                        });
                    }
                });




    }

    public void scrollRecyclerView(final int item) {
        final ValueAnimator valueAnimator = ValueAnimator.ofInt(0, UIUtils.dip2Px(130));
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                //调用RecyclerView的scrollBy执行滑动
//                mStartTrainRecycleView.smoothScrollBy(item,animatedValue);
                mLinearLayoutManager.scrollToPositionWithOffset(item, UIUtils.dip2Px(130) - animatedValue);

                LogUtils.i("animatedValue=" + animatedValue);
            }

        });
        valueAnimator.start();

    }

}

package com.jkpg.ruchu.view.activity.train;

import android.Manifest;
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
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
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
import com.jkpg.ruchu.utils.PermissionUtils;
import com.jkpg.ruchu.utils.PopupWindowUtils;
import com.jkpg.ruchu.utils.SPUtils;
import com.jkpg.ruchu.utils.ToastUtils;
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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.line_start_iv)
    ImageView mLineStartIv;
    @BindView(R.id.line_start_tv)
    TextView mLineStartTv;


    private List<float[]> charts;       //图表集合
    private List<float[]> chartsX;       //图表x集合
    private List<Animator> mAnimators = new ArrayList<>();//所有的动画列表
    private ArrayList<Line> mLine;    // 点的集合
    private List<LineChart> mLines;  //线的集合
    private AutoProgressTask mTask;
    private LinearLayoutManager mLinearLayoutManager;


    private int timeCount = -500; // 计时
    private int lineTimeCount = -500; // 计时
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
    private HashMap<String, Integer> mSoundID;
    private boolean isFirst = true;
    private AlertDialog dialog;
    private MediaPlayer mMediaPlayer;
    private boolean isLoad;
    @BindView(R.id.header_view)
    RelativeLayout mHeaderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_train2);
        ButterKnife.bind(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mHeaderView.setElevation(0);
        }
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
        mMediaPlayer = MediaPlayer.create(this, R.raw.start);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isFirst = false;
                mTask.start();
                isTrain = true;
                mLineChart.setVisibility(View.VISIBLE);
                mLineStartIv.setVisibility(View.GONE);
                mLineStartTv.setVisibility(View.GONE);

            }
        });
        mLineChart.setVisibility(View.GONE);

        mStartTrainTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupWindow popupWindow = new PopupWindow(StartTrainActivity2.this);
                popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                View inflate = View.inflate(StartTrainActivity2.this, R.layout.view_show_tip, null);
                popupWindow.setContentView(inflate);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
                popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_train_prepare, null), Gravity.CENTER, 0, 0);
                PopupWindowUtils.darkenBackground(StartTrainActivity2.this, .4f);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        PopupWindowUtils.darkenBackground(StartTrainActivity2.this, 1f);
                    }
                });
            }
        });
        PermissionUtils.requestPermissions(StartTrainActivity2.this, 222, new String[]{Manifest.permission.READ_PHONE_STATE}, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(String[] deniedPermissions) {
                ToastUtils.showShort(UIUtils.getContext(), "您拒绝了,就不能来电暂停了哦,如需要,请到设置应用信息中打开.");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionUtils.onRequestPermissionsResult(StartTrainActivity2.this, 222, new String[]{Manifest.permission.READ_PHONE_STATE});
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

                    @Override
                    public void onAfter(@Nullable String s, @Nullable Exception e) {
                        super.onAfter(s, e);
                        if (!isLoad) {
                            final long[] firstTime = {0};
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(StartTrainActivity2.this, R.style.dialog);
                            mBuilder.setView(View.inflate(UIUtils.getContext(), R.layout.view_animation, null));
                            dialog = mBuilder.show();
                            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                        if (System.currentTimeMillis() - firstTime[0] > 2000) {
//                        Toast.makeText(UIUtils.getContext(), "再按一次", Toast.LENGTH_SHORT).show();
                                            firstTime[0] = System.currentTimeMillis();
                                        } else {
                                            finish();
                                        }
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }
                            });
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);


                        }
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
        mStartTrainNowOne.setText("当前为第1节");
        mStartTrainProgressBar.setMax(Integer.parseInt(startTrainBean.totaltime) * 1000);
    }

    private void init(StartTrainBean startTrainBean) {
        mStartTrainTitle.setText("产后康复 " + startTrainBean.level);
        String dateFormat = DateUtil.dateFormat(Integer.parseInt(startTrainBean.totaltime) * 1000 + "", "mm分ss秒");
        mStartTrainTotalTime.setText("共计" + dateFormat);
        mStartTrainSection.setText("第" + startTrainBean.excisedays + "天" + "  " + startTrainBean.level_2 + "级");
        mStartTrainTotalOne.setText("共" + startTrainBean.programme.size() + "节");
        mStartTrainTvProgressTotal.setText(DateUtil.dateFormat(Integer.parseInt(startTrainBean.totaltime) * 1000 + "", "mm:ss"));

    }

    private void initSound() {
        mSoundID = new HashMap<>();
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundID.put("end", mSoundPool.load(this, R.raw.end, 1));
        mSoundID.put("y1", mSoundPool.load(this, R.raw.y1, 1));
        mSoundID.put("y2", mSoundPool.load(this, R.raw.y2, 1));
        mSoundID.put("y3", mSoundPool.load(this, R.raw.y3, 1));
        mSoundID.put("y4", mSoundPool.load(this, R.raw.y4, 1));
        mSoundID.put("y5", mSoundPool.load(this, R.raw.y5, 1));
        mSoundID.put("y6", mSoundPool.load(this, R.raw.y6, 1));
        mSoundID.put("y7", mSoundPool.load(this, R.raw.y7, 1));
        mSoundID.put("y8", mSoundPool.load(this, R.raw.y8, 1));
        mSoundID.put("y9", mSoundPool.load(this, R.raw.y9, 1));
        mSoundID.put("y10", mSoundPool.load(this, R.raw.y10, 1));
        mSoundID.put("y11", mSoundPool.load(this, R.raw.y11, 1));
        mSoundID.put("y12", mSoundPool.load(this, R.raw.y12, 1));
        mSoundID.put("y13", mSoundPool.load(this, R.raw.y13, 1));
        mSoundID.put("y14", mSoundPool.load(this, R.raw.y14, 1));
        mSoundID.put("y15", mSoundPool.load(this, R.raw.y15, 1));
//        mSoundID.put("y16", mSoundPool.load(this, R.raw.y16, 1));
        mSoundID.put("y17", mSoundPool.load(this, R.raw.y17, 1));
        mSoundID.put("y18", mSoundPool.load(this, R.raw.y18, 1));
        mSoundID.put("y19", mSoundPool.load(this, R.raw.y19, 1));
        mSoundID.put("y20", mSoundPool.load(this, R.raw.y20, 1));
        mSoundID.put("yy4", mSoundPool.load(this, R.raw.yy4, 1));
        mSoundID.put("yy3", mSoundPool.load(this, R.raw.yy3, 1));
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                isLoad = true;
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
//                ToastUtils.showShort(UIUtils.getContext(),"------");
            }
        });
    }

    @OnClick({R.id.header_iv_left, R.id.start_train_start, R.id.start_train_iv_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                if (mMediaPlayer.isPlaying()) {
                    okOut();
                    mMediaPlayer.pause();
                } else {
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
            mStartTrainNowOne.setText("当前为第" + (numChart + 1) + "节");
        } else {
            if (isFirst) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mStartTrainStart.setBackgroundResource(R.drawable.icon_start);

                } else {
                    mMediaPlayer.start();
                    mStartTrainStart.setBackgroundResource(R.drawable.icon_pause);
                }
//
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
        if (mStartTrainBean.programme.get(index).ishavezero.equals("1")) {
            mLineText.setText("充分放松盆底肌");
        } else {
            mLineText.setText("");
        }
//        LogUtils.i("mLines = " + mLines.size());
//        LogUtils.i("index = " + index);
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
        for (int i = 0; i < 6; i++) {
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
                pointValues.add(new PointValue(pointsX[i] / pointsX[pointsX.length - 1], points[i] / 5f));
                LogUtils.i("x=" + pointsX[i] + "y=" + points[i]);
                continue;
            }
            if (pointsX[i] - pointsX[i - 1] == 0.5f) {
                pointValues.add(new PointValue(pointsX[i] / pointsX[pointsX.length - 1], points[i] / 5f));
                LogUtils.i("x=" + pointsX[i] + "y=" + points[i]);
                continue;
            }
            if (pointsX[i] - pointsX[i - 1] != 0.5f) {
                float v = (pointsX[i] - pointsX[i - 1]) * 2;
                for (float j = 0; j < v; j++) {
                    float y = (points[i] - points[i - 1]) / (pointsX[i] - pointsX[i - 1]) * 0.5f;
                    pointValues.add(new PointValue((.5f * (j + 1) + pointsX[i - 1]) / pointsX[pointsX.length - 1], (y * (j + 1) + points[i - 1]) / 5f));
                    LogUtils.i("x=" + (.5f * (j + 1) + pointsX[i - 1]) + "y=" + (y * (j + 1) + points[i - 1]));
                }
            }
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
        mAdapter = new ChartRLAdapter(chartsX, charts);
        mStartTrainRecycleView.setAdapter(mAdapter);
        mMap = mAdapter.getMap();
        mMap.put(0, true);
        mAdapter.notifyItemChanged(0);
    }

    private class AutoProgressTask implements Runnable {

        private List<Float> mTimearr;
        private List<String> mVideoarr;

        @Override
        public void run() {
            mStartTrainProgressBar.setProgress(timeCount);
            if (timeCount >= 0) {
                String s = DateUtil.dateFormat(timeCount + "", "mm:ss");
                mStartTrainTvProgressTime.setText(s);
            }
            LogUtils.i("timeCount=" + timeCount);
//            LogUtils.i(s + "= 00:00");
            timeCount += 100;
            lineTimeCount += 100;
            LogUtils.i("lineTimeCount=" + lineTimeCount);
            if (timeCount % 500 == 0) {
//                timeCount = 0;
                if (numLine >= chartsX.get(numChart)[(chartsX.get(numChart).length) - 1] * 2 - 1) {
                    numChart++;
                    numLine = -1;
                    if (numChart >= charts.size()) {
                        mStartTrainStart.setBackgroundResource(R.drawable.icon_start);

                        LogUtils.i("-----怎么不结束！！！！");
                        String s = DateUtil.dateFormat((timeCount + 100) + "", "mm:ss");
                        mStartTrainTvProgressTime.setText(s);
//                        mStartTrainProgressBar.setProgress(timeCount + 1000);
                        mSoundPool.play(mSoundID.get("end"), 1, 1, 0, 0, 1);
                        MyApplication.getMainThreadHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showSuccess();
                                isTrain = false;
                                mTask.stop();
                            }
                        }, 3000);

                        return;
                    } else {
                        for (int i = 0; i < mLines.size(); i++) {
                            mStartTrain.removeView(mLines.get(i));
                        }
                        initLineChart(numChart, (int) chartsX.get(numChart)[(chartsX.get(numChart).length - 1)] * 2);
                        scrollRecyclerView(numChart);
                        mMap.put(numChart, true);
                        mMap.put(numChart - 1, false);
                        mAdapter.notifyDataSetChanged();
                        mStartTrainNowOne.setText("当前为第" + (numChart + 1) + "节");
                        lineTimeCount = 0;
                        LogUtils.d("lineTimeCount = =" + lineTimeCount);


                    }
                }
                if (mAnimator != null) {
                    mAnimator.cancel();
                }
                numLine++;
                LineChart lineChart = mLines.get(numLine);
                lineChart.setLineData(mLine);
                lineChart.setI(numLine);
                lineChart.showWithAnimation(500);
                mAnimator = lineChart.getAnimator();
                mAnimators.add(mAnimator);
                mAnimator.start();

                mTimearr = mStartTrainBean.programme.get(numChart).timearr;
                mVideoarr = mStartTrainBean.programme.get(numChart).videoarr;
                float v = (float) (lineTimeCount / 1000.0);
                LogUtils.d("VV=" + v);
                if (mTimearr.contains(v)) {
                    LogUtils.d("V=" + v);
                    nowSound = mSoundPool.play(mSoundID.get(mVideoarr.get(0)), 1, 1, 0, 0, 1);
                    mTimearr.remove(0);
                    mVideoarr.remove(0);
                }
            }
            start();
        }

        private void start() {
            stop();
            mTimearr = mStartTrainBean.programme.get(numChart).timearr;
            mVideoarr = mStartTrainBean.programme.get(numChart).videoarr;
//            if (mTimearr.contains(0f)) {
//                nowSound = mSoundPool.play(mSoundID.get(mVideoarr.get(0)), 1, 1, 0, 0, 1);
//                mTimearr.remove(0);
//                mVideoarr.remove(0);
//            }
            if (timeCount == -500 && mTimearr.contains(0f)) {
                nowSound = mSoundPool.play(mSoundID.get(mVideoarr.get(0)), 1, 1, 0, 0, 1);
                mTimearr.remove(0);
                mVideoarr.remove(0);
            }

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
                        if (isFirst) {
                            if (mMediaPlayer != null)
                                mMediaPlayer.start();
                        } else {
                            mAnimator.resume();
                            mTask.start();
                            LogUtils.i("start");
                            mStartTrainStart.setBackgroundResource(R.drawable.icon_pause);
                            dialog.dismiss();
                        }
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getMainThreadHandler().removeCallbacks(mTask);
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mSoundPool.release();
        unregisterReceiver(mVolumeReceiver);
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
        EventBus.getDefault().post("Train");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mMediaPlayer.isPlaying()) {
                    okOut();
                    mMediaPlayer.pause();
                } else {
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
                }
                return true;
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
                .params("userid", SPUtils.getString(UIUtils.getContext(), Constants.USERID, ""))
                .params("starttime", mStartTime)
                .params("alltimelong", mStartTrainBean.totaltime)
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

//                LogUtils.i("animatedValue=" + animatedValue);
            }

        });
        valueAnimator.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        ToastUtils.showLong(UIUtils.geContext(),"常驻后台会被系统关闭哦!");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void callPhone(String mess) {
        if (mess.equals("CallPhone")) {
            LogUtils.d("CallPhone----train");
            if (isFirst) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mStartTrainStart.setBackgroundResource(R.drawable.icon_start);
                }
            } else if (isTrain) {
                if (!isPause) {
                    isPause = true;
                    mAnimator.pause();
                    mTask.stop();
                    mSoundPool.pause(nowSound);
                    LogUtils.i("CallPhone -- click");
                    mStartTrainStart.setBackgroundResource(R.drawable.icon_start);
                }
            }
        }
    }

}

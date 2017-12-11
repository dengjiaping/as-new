package com.jkpg.ruchu.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;

import java.util.List;

/**
 * Created by qindi on 2017/5/31.
 */

public class Histogram extends View {

    //用于画矩形色块
//    int[] mColors = {0xff83ccd2, 0xffc0e1ce, 0xfffac55e};
    private int mXDown, mLastX, mLastY;
    float lastStartX = 0;//抬起手指后，当前控件最左边X的坐标
    //数据
    List<PPHistogramBean> mDatas;
    //文字画笔，默认画笔
    Paint textPaint, defaultPaint;

    int MAX = 100;//纵坐标 0~MAX ,MAX必须是5的倍数
    float startX = 100;
    float perPx;//每条宽度
    float borderLeft = 10;
    float borderBottom = 10;
    float XofY = 10;//纵轴线的X坐标
    float YofX = 10;//横轴线的Y坐标
    float rx = UIUtils.dip2Px(9);
    float ry = UIUtils.dip2Px(9);
    float width = UIUtils.dip2Px(18);//柱子宽度

    int selectIndex = -1;
    int state = -100;

    int countInOne = 7;//一屏幕宽度显示的圆柱个数

    HistogramClickListener mListener;
    float[] tempdatas;

    boolean isNoMore = false;//时候滚到边界

    public void setHistogramClickListener(HistogramClickListener mListener) {
        this.mListener = mListener;
    }

    public void setmDatas(List<PPHistogramBean> mDatas, int max) {
        setmDatas(mDatas, max, true);
    }

    public void setmDatas(List<PPHistogramBean> mDatas, int max, boolean isAnim) {
        this.mDatas = mDatas;
        this.MAX = max;
        tempdatas = new float[mDatas.size()];
        for (int i = 0; i < tempdatas.length; i++) {
            if (isAnim) {
                tempdatas[i] = 0;
            } else {
                tempdatas[i] = mDatas.get(i).getValue();
            }
        }
        invalidate();
    }

    public Histogram(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setClickable(true);
    }

    public Histogram(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        setClickable(true);
    }

    //初始化单位
    private void initializeTheUnit() {
        perPx = UIUtils.dip2Px(10);
        borderLeft = UIUtils.dip2Px(3);
        borderBottom = UIUtils.dip2Px(5);
        XofY = textPaint.measureText("" + MAX) + borderLeft + UIUtils.dip2Px(2);
        if (startX > XofY) {
            startX = XofY;
            lastStartX = startX;
        }
        YofX = getHeight() - (textPaint.getFontMetrics().bottom - textPaint.getFontMetrics().top) - borderBottom - UIUtils.dip2Px(2);
    }

    //初始化画笔
    private void initPaint() {
        if (textPaint == null) {
            textPaint = new Paint();
        } else {
            textPaint.reset();
        }
        if (defaultPaint == null) {
            defaultPaint = new Paint();
        } else {
            defaultPaint.reset();
        }
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(getWidth() / 28);
        textPaint.setColor(UIUtils.getColor(R.color.colorGray3));

        defaultPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        initializeTheUnit();

        //画纵坐标轴
        paintingYAxis(canvas);
        //画横坐标轴
        paintingXAxis(canvas);
        //画数据
        paintingDatas(canvas);
        if (isNoMore) {
            paintingWave(canvas);
        }
        postInvalidate();
    }

    //当滚动到边界是显示波浪
    private void paintingWave(Canvas canvas) {
        float waveWidth = getWidth() / 6;//最大宽度约为控件宽度的六分之一
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Path mPath = new Path();

        int width = 0;
        if (wm != null) {
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            width = dm.widthPixels;
        }
        initPaint();
        defaultPaint.setColor(0x43F87C86);
        defaultPaint.setStyle(Paint.Style.FILL);

        if (startX == XofY) {
            //左边
            waveWidth = waveWidth * (mLastX / (float) width);
            mPath.moveTo(XofY, YofX);
            mPath.lineTo(XofY, borderBottom);
            mPath.lineTo(XofY + 30, borderBottom);
            mPath.quadTo(XofY + 30 + waveWidth, mLastY, XofY + 30, YofX);
            mPath.lineTo(XofY, YofX);
            mPath.close();
        } else {
            //右边
            waveWidth = waveWidth * ((width - mLastX) / (float) width);
            mPath.moveTo(getWidth(), YofX);
            mPath.lineTo(getWidth(), borderBottom);
            mPath.lineTo(getWidth() - 30, borderBottom);
            mPath.quadTo(getWidth() - 30 - waveWidth, mLastY, getWidth() - 30, YofX);
            mPath.lineTo(getWidth(), YofX);
            mPath.close();
        }

        canvas.drawPath(mPath, defaultPaint);
    }

    private void paintingXAxis(Canvas canvas) {
        initPaint();
        defaultPaint.setStrokeWidth(2);
        defaultPaint.setColor(0xffffffff);
        canvas.drawLine(XofY, YofX, getWidth(), YofX, defaultPaint);
        defaultPaint.setColor(0xffffffff);
        canvas.drawRect(0, YofX + UIUtils.dip2Px(1), getWidth(), getHeight(), defaultPaint);
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        float step = (getWidth() - XofY - borderLeft) / countInOne;
        //第一行的Y轴坐标
        float textY = YofX + (textPaint.getFontMetrics().bottom - textPaint.getFontMetrics().top) - UIUtils.dip2Px(1);
        //第二行的Y轴坐标
        // float textY2 = textY + (textPaint.getFontMetrics().bottom - textPaint.getFontMetrics().top) - UIUtils.dip2Px(6);
        //X轴日期的颜色
        textPaint.setColor(UIUtils.getColor(R.color.colorGray3));
        for (int i = 0; i < mDatas.size(); i++) {
            PPHistogramBean pphb = mDatas.get(i);
            String text = pphb.getEndTime();
            if (/*i *//*% 12*//* == 0 &&*/ startX + (i + 0.5f) * step > 0 && startX + (i + 0.5f) * step < getWidth()) {
//                textPaint.setTextSize(getWidth() / 34);
//                canvas.drawText(text.split(" ")[0], startX + (i + 0.5f) * step - textPaint.measureText(text.split(" ")[0]) / 2, textY2, textPaint);
                textPaint.setTextSize(getWidth() / 32);
                canvas.drawText(text/*.split(" ")[1]*/, startX + (i + 0.5f) * step - textPaint.measureText(text/*.split(" ")[1]*/) / 2, textY, textPaint);
            }
        }

    }

    private void paintingYAxis(Canvas canvas) {
        initPaint();
//        defaultPaint.setColor(0xff49c29d);
//        canvas.drawLine(XofY, 0, XofY, YofX, defaultPaint);
        defaultPaint.setStrokeWidth(1);
        defaultPaint.setColor(UIUtils.getColor(R.color.colorGray2));

        textPaint.setColor(UIUtils.getColor(R.color.colorGray3));
        textPaint.setTextSize(getWidth() / 32);

        float step = (YofX - borderBottom) / 5;
        float textH = (textPaint.getFontMetrics().bottom + textPaint.getFontMetrics().top) / 2;
        //写纵坐标数字

        for (int i = 0; i <= 5; i++) {
            //文字右对齐
            float x = textPaint.measureText(MAX + "") - textPaint.measureText(i * MAX / 5 + "");

            if (i == 0) {
                //0的位置不居中
                canvas.drawText(i * MAX / 5 + "", borderLeft + x, YofX, textPaint);
            } else {

                canvas.drawText(i * MAX / 5 + "", borderLeft + x, YofX - i * step - textH, textPaint);
            }

            canvas.drawLine(XofY, YofX - i * step, getWidth(), YofX - i * step, defaultPaint);
        }

    }

    private void paintingDatas(Canvas canvas) {
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        initPaint();
        defaultPaint.setColor(UIUtils.getColor(R.color.colorPink));
        float step = (getWidth() - XofY - borderLeft) / countInOne;

//        LinearGradient lg;
//        lg = new LinearGradient(getWidth(), 0, getWidth(), YofX, 0xff23AC75, 0xff95ECD1, Shader.TileMode.CLAMP);
//        defaultPaint.setShader(lg);

        for (int i = 0; i < mDatas.size(); i++) {

            //屏幕之外不用绘制
            if (startX + (i + 0.5f) * step - width / 2 < XofY || startX + (i + 0.5f) * step + width / 2 > getWidth()) {
                continue;
            }

            PPHistogramBean pphb = mDatas.get(i);

            if (tempdatas[i] < pphb.getValue() * 0.9f) {
                int raiseStep = (int) (pphb.getValue() * 0.1f);
                if (raiseStep < 1) {
                    raiseStep = 1;
                }
                tempdatas[i] = tempdatas[i] + raiseStep;
            } else {
                tempdatas[i] = pphb.getValue();
            }
            if (selectIndex != i) {
                //正常颜色
                defaultPaint.setColor(UIUtils.getColor(R.color.colorPink));
            } else {
                //选中颜色
                defaultPaint.setColor(UIUtils.getColor(R.color.colorPink));

            }
            //选择了的与其他的颜色不一样
//            if (selectIndex != i) {
////                lg = new LinearGradient(startX + (i + 0.5f) * step - width / 2, YofX - tempdatas[i] * (YofX - borderBottom) / MAX, startX + (i + 0.5f) * step + width / 2, YofX, 0xff23AC75, 0xff49c29d, Shader.TileMode.CLAMP);
//            } else {
//                lg = new LinearGradient(startX + (i + 0.5f) * step - width / 2, YofX - tempdatas[i] * (YofX - borderBottom) / MAX, startX + (i + 0.5f) * step + width / 2, YofX, 0xff4988c2, 0xffE1EBF5, Shader.TileMode.CLAMP);
//            }
//            defaultPaint.setShader(lg);

            RectF oval = new RectF(startX + (i + 0.5f) * step - width / 2, YofX - tempdatas[i] * (YofX - borderBottom) / MAX, startX + (i + 0.5f) * step + width / 2, YofX);
//            canvas.drawLine(XofY + (i + 0.5f) * step, pphb.getValue() * (YofX - borderBottom) / MAX + borderBottom, XofY + (i + 0.5f) * step, YofX, defaultPaint);
            canvas.drawRoundRect(oval, rx, ry, defaultPaint);
            canvas.save();
            oval = new RectF(
                    startX + (i + 0.5f) * step - width / 2,
                    Math.min(YofX - tempdatas[i] * (YofX - borderBottom) / MAX + ry, YofX),
                    startX + (i + 0.5f) * step + width / 2,
                    YofX);
            canvas.drawRoundRect(oval, rx, ry, defaultPaint);
            canvas.save();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDatas == null || mDatas.size() == 0) {
            selectIndex = -1;
            return super.onTouchEvent(event);
        }
        final int action = event.getAction();
        float step = (getWidth() - XofY - borderLeft) / countInOne;
        isNoMore = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mXDown = (int) event.getRawX();
                state = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastX = (int) event.getRawX();
                mLastY = (int) event.getY();
                //滑动限制
                if (lastStartX + (mLastX - mXDown) * 2 > XofY) {
                    startX = XofY;
                    state = MotionEvent.ACTION_MOVE;
                    isNoMore = true;
                    postInvalidate();
                    break;
                }
                if (lastStartX + (mLastX - mXDown) * 2 + step * (mDatas.size() + 0.5f) < getWidth()) {
                    startX = -step * (mDatas.size() + 0.5f) + getWidth();
                    state = MotionEvent.ACTION_MOVE;
                    isNoMore = true;
                    postInvalidate();
                    break;
                }
                state = MotionEvent.ACTION_MOVE;
                startX = lastStartX + (mLastX - mXDown) * 2;

                postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起
                lastStartX = startX;
                state = MotionEvent.ACTION_UP;
                if (Math.abs(event.getRawX() - mXDown) < 2) {
                    selectIndex = (int) ((event.getRawX() - startX) / step) - 1;
                    if (mListener != null && selectIndex >= 0) {
                        mListener.onHistogramClick(selectIndex);
                    }
                } else {
                    selectIndex = -1;
                }

                postInvalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    public abstract static class HistogramClickListener {
        public abstract void onHistogramClick(int position);
    }

    /**
     * 数据结构
     */
    public static class PPHistogramBean {
        float value;
        String startTime;
        String endTime;

        public PPHistogramBean(float value, String time) {
            this.value = value;
            this.endTime = time;
        }

        public float getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }

    public void setCountInOne(int countInOne) {
        this.countInOne = countInOne;
    }

    public void setLastStartX(float lastStartX) {
        this.lastStartX = lastStartX;
    }
}
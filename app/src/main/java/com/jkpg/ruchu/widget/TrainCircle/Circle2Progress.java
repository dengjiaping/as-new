package com.jkpg.ruchu.widget.TrainCircle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jkpg.ruchu.R;

/**
 * Created by qindi on 2017/11/20.
 */

public class Circle2Progress extends View {

    private static final String TAG = "RuChu";
    private Context mContext;

    //默认大小
    private int mDefaultSize;
    //是否开启抗锯齿
    private boolean antiAlias;


    //绘制圆弧
    private Paint mArcPaint;
    private float mArcWidth;
    private float mStartAngle = 150;
    private float mSweepAngle = 240;
    private RectF mRectF;

    //绘制背景圆弧
    private Paint mBgArcPaint;
    private float mBgArcWidth;

    //圆心坐标，半径
    private Point mCenterPoint;

    public static final boolean ANTI_ALIAS = true;

    public static final int DEFAULT_SIZE = 150;

    public static final int DEFAULT_ARC_WIDTH = 15;

    //一周训练目标
    private int date = 5;
    //一天训练目标
    private int dateNum = 3;
    //当前训练
    private int nowDate = 3;
    //第一个黄还是白
    private boolean isYellow = true;


    private float starAngle = 20;
    private float circleAngle;
    private float circleNumAngle;
    private Paint mStarPaint;

    public Circle2Progress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mDefaultSize = MiscUtil.dipToPx(mContext, DEFAULT_SIZE);
        mRectF = new RectF();
        mCenterPoint = new Point();
        initAttrs(attrs);
        initPaint();
    }

    private void initData() {
        circleAngle = (mSweepAngle - starAngle * (date - 1)) / date;
        circleNumAngle = (mSweepAngle - starAngle * (date - 1)) / (date * dateNum);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        antiAlias = typedArray.getBoolean(R.styleable.CircleProgressBar_antiAlias, ANTI_ALIAS);
        mArcWidth = typedArray.getDimension(R.styleable.CircleProgressBar_arcWidth, DEFAULT_ARC_WIDTH);
        mBgArcWidth = typedArray.getDimension(R.styleable.CircleProgressBar_bgArcWidth, DEFAULT_ARC_WIDTH);
        typedArray.recycle();
    }

    private void initPaint() {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(antiAlias);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setColor(Color.parseColor("#fbe12f"));
        mArcPaint.setStrokeWidth(mArcWidth);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mBgArcPaint = new Paint();
        mBgArcPaint.setAntiAlias(antiAlias);
        mBgArcPaint.setColor(Color.parseColor("#6fffffff"));
        mBgArcPaint.setStyle(Paint.Style.STROKE);
        mBgArcPaint.setStrokeWidth(mBgArcWidth);
        mBgArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mStarPaint = new Paint();
        mStarPaint.setColor(Color.parseColor("#ccffffff"));
        mStarPaint.setAntiAlias(antiAlias);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MiscUtil.measure(widthMeasureSpec, mDefaultSize),
                MiscUtil.measure(heightMeasureSpec, mDefaultSize));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: w = " + w + "; h = " + h + "; oldw = " + oldw + "; oldh = " + oldh);
        //求圆弧和背景圆弧的最大宽度
        float maxArcWidth = Math.max(mArcWidth, mBgArcWidth);
        //求最小值作为实际值
        int minSize = Math.min(w - getPaddingLeft() - getPaddingRight() - 2 * (int) maxArcWidth,
                h - getPaddingTop() - getPaddingBottom() - 2 * (int) maxArcWidth);
        //减去圆弧的宽度，否则会造成部分圆弧绘制在外围
        float radius = minSize / 2 - 16;
        //获取圆的相关参数
        mCenterPoint.x = w / 2;
        mCenterPoint.y = h / 2;
        //绘制圆弧的边界
        mRectF.left = mCenterPoint.x - radius - maxArcWidth / 2;
        mRectF.top = mCenterPoint.y - radius - maxArcWidth / 2;
        mRectF.right = mCenterPoint.x + radius + maxArcWidth / 2;
        mRectF.bottom = mCenterPoint.y + radius + maxArcWidth / 2;

        Log.d(TAG, "onSizeChanged: 控件大小 = " + "(" + w + ", " + h + ")"
                + "圆心坐标 = " + mCenterPoint.toString()
                + ";圆半径 = " + radius
                + ";圆的外接矩形 = " + mRectF.toString());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initData();
        drawArc(canvas);
    }


    public void setYellow(boolean yellow) {
        isYellow = yellow;
        invalidate();
    }

    private void drawArc(Canvas canvas) {
        for (int i = 0; i < date; i++) {
            canvas.drawArc(mRectF, circleAngle * i + (starAngle * i) + mStartAngle,
                    circleAngle, false, mBgArcPaint);
        }
        if (nowDate % dateNum == 0) {
            for (int i = 0; i < nowDate / dateNum; i++) {
                canvas.drawArc(mRectF, circleAngle * i + (starAngle * i) + mStartAngle,
                        circleAngle, false, mArcPaint);
            }
        } else {
            int ii = nowDate - nowDate % dateNum;
            int x = 0;
            for (int i = 0; i < ii / dateNum; i++) {
                canvas.drawArc(mRectF, circleAngle * i + (starAngle * i) + mStartAngle,
                        circleAngle, false, mArcPaint);
                x++;
            }
            int y = nowDate - x * dateNum;

            canvas.drawArc(mRectF, mStartAngle + circleAngle * (ii / dateNum) + (starAngle * (ii / dateNum)),
                    circleNumAngle * y, false, mArcPaint);

        }
        //绘制背景
        for (int i = 0; i < (date); i++) {
            if (i == 0) {
                Bitmap b;
                if (isYellow) {
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_star_start);
                } else {
                    b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_star_start_write);
                }
                Matrix matrix = new Matrix();
                matrix.setRotate(mStartAngle - 90 - starAngle / 2 + circleAngle * (i) + starAngle * (i), mCenterPoint.x, mCenterPoint.y);
                canvas.drawBitmap(b, matrix, mArcPaint);
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_star_write);
                Matrix matrix = new Matrix();
                matrix.setRotate(mStartAngle - 90 - starAngle / 2 + circleAngle * (i) + starAngle * (i), mCenterPoint.x, mCenterPoint.y);
                canvas.drawBitmap(bitmap, matrix, mStarPaint);
            }
        }
        //绘制前景
        if (nowDate == date * dateNum) {
            for (int i = 1; i < (nowDate - nowDate % dateNum) / dateNum; i++) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_star_yellow);
                Matrix matrix = new Matrix();
                matrix.setRotate(mStartAngle - 90 - starAngle / 2 + circleAngle * (i) + starAngle * (i), mCenterPoint.x, mCenterPoint.y);
                canvas.drawBitmap(bitmap, matrix, mArcPaint);
            }
        } else {
            for (int i = 1; i <= (nowDate - nowDate % dateNum) / dateNum; i++) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_star_yellow);
                Matrix matrix = new Matrix();
                matrix.setRotate(mStartAngle - 90 - starAngle / 2 + circleAngle * (i) + starAngle * (i), mCenterPoint.x, mCenterPoint.y);
                canvas.drawBitmap(bitmap, matrix, mArcPaint);
            }
        }
    }


    public void setDate(int date) {
        this.date = date;
        invalidate();
    }

    public void setDateNum(int dateNum) {
        this.dateNum = dateNum;
        invalidate();

    }

    public void setNowDate(int nowDate) {
        this.nowDate = nowDate;
        invalidate();

    }

}

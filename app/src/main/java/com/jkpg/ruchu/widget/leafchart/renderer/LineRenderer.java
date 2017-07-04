package com.jkpg.ruchu.widget.leafchart.renderer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jkpg.ruchu.widget.leafchart.bean.Line;
import com.jkpg.ruchu.widget.leafchart.bean.Point;
import com.jkpg.ruchu.widget.leafchart.support.LeafUtil;


/**
 * Created by qindi on 2017/5/9.
 */

public class LineRenderer extends AbsRenderer {
    private PathMeasure measure;

    /**
     * 动画结束标志
     */
    private boolean isAnimateEnd;

    /**
     * 是否开始绘制，防止动画绘制之前绘制一次
     */
    private boolean isShow;

    private float phase;
    private ObjectAnimator mAnimator;

    public LineRenderer(Context context, View view) {
        super(context, view);
    }

    /**
     * 画折线
     *
     * @param canvas
     */
    public void drawLines(Canvas canvas, Line line, Point pointA, Point pointB) {
        if (line != null && isShow) {
            linePaint.setColor(line.getLineColor());
            linePaint.setStrokeWidth(LeafUtil.dp2px(mContext, line.getLineWidth()));
            linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            Path path = new Path();
            path.moveTo(pointA.x, pointA.y);
            path.lineTo(pointB.x, pointB.y);

            measure = new PathMeasure(path, false);
            linePaint.setPathEffect(createPathEffect(measure.getLength(), phase, 0.0f));
            canvas.drawPath(path, linePaint);
        }
    }

    //showWithAnimation动画开启后会调用该方法
    public void setPhase(float phase) {
        chartView.invalidate();
    }

    private PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[]{phase * pathLength, pathLength}, 0);
    }

    public void showWithAnimation(int duration/*, Line line*/) {
        isAnimateEnd = false;
        mAnimator = ObjectAnimator.ofFloat(this, "phase", 0.0f, 1.0f);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(duration);
        //mAnimator.start();
        isShow = true;

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                phase = (float) animation.getAnimatedValue();
            }
        });

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimateEnd = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    public ObjectAnimator getAnimator() {
        return mAnimator;
    }
}

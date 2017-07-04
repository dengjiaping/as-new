package com.jkpg.ruchu.widget.leafchart;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.jkpg.ruchu.widget.leafchart.bean.Line;
import com.jkpg.ruchu.widget.leafchart.bean.Point;
import com.jkpg.ruchu.widget.leafchart.bean.PointValue;
import com.jkpg.ruchu.widget.leafchart.renderer.LineRenderer;

import java.util.List;

/**
 * Created by qindi on 2017/5/9.
 */

public class LineChart extends AbsLeafChart {

    private List<Line> lines;

    private LineRenderer mLineRenderer;
    private int mI;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initRenderer() {
        mLineRenderer = new LineRenderer(mContext, this);
    }

    @Override
    protected void setRenderer() {
        super.setRenderer(mLineRenderer);
    }

    public void setLineData(List<Line> lineData) {
        lines = lineData;
        resetPointWeight();
    }

    public List<Line> getLineData() {
        return lines;
    }

    @Override
    protected void resetPointWeight() {
        if (lines != null) {
            for (int i = 0, size = lines.size(); i < size; i++) {
                super.resetPointWeight(lines.get(i));
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (lines != null && lines.size() > 0) {
            Line line;
            line = lines.get(0);
            List<PointValue> values = line.getValues();
            Point pointA = new Point(values.get(mI).getOriginX(), values.get(mI).getOriginY());
            Point pointB = new Point(values.get(mI + 1).getOriginX(), values.get(mI + 1).getOriginY());
            if (line != null) {
                mLineRenderer.drawLines(canvas, line, pointA, pointB);
            }
        }
    }

    /**
     * 带动画的绘制
     *
     * @param duration
     */
    public void showWithAnimation(int duration) {
        mLineRenderer.showWithAnimation(duration);
    }


    public int setI(int i) {
        mI = i;
        return mI;
    }

    public ObjectAnimator getAnimator() {
        return mLineRenderer.getAnimator();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

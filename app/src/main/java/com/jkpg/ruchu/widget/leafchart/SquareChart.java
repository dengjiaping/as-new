package com.jkpg.ruchu.widget.leafchart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.widget.leafchart.bean.ChartData;
import com.jkpg.ruchu.widget.leafchart.bean.Point;
import com.jkpg.ruchu.widget.leafchart.bean.PointValue;
import com.jkpg.ruchu.widget.leafchart.bean.Square;
import com.jkpg.ruchu.widget.leafchart.renderer.SquareRenderer;
import com.jkpg.ruchu.widget.leafchart.support.LeafUtil;

import java.util.List;

/**
 * Created by qindi on 2017/5/18.
 */

public class SquareChart extends AbsLeafChart {
    private Square square;

    private int mI;

    private SquareRenderer mSquareRenderer;

    public SquareChart(Context context) {
        this(context, null);
    }

    public SquareChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (startMarginX == 0) startMarginX = (int) LeafUtil.dp2px(context, 20);

    }

    @Override
    protected void initRenderer() {
        mSquareRenderer = new SquareRenderer(mContext, this);
    }

    @Override
    protected void setRenderer() {
        super.setRenderer(mSquareRenderer);
    }

    @Override
    protected void resetPointWeight() {
        super.resetPointWeight(square);

    }

    public void setChartData(ChartData chartData) {
        this.square = (Square) chartData;
        resetPointWeight();
    }

    public ChartData getChartData() {
        return square;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (square != null) {
            /*List<PointValue> values = square.getValues();
            PointValue pointValue = values.get(mI);
            LogUtils.i("point",pointValue.getDiffX()+"");*/
            mSquareRenderer.drawSquares(canvas, square, axisX,mI);

            if (square != null && square.isHasLabels()) {
                mSquareRenderer.drawLabels(canvas, square, axisY);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public int setI(int i) {
        mI = i;
        return mI;
    }
}

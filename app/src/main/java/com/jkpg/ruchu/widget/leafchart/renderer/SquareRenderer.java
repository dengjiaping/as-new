package com.jkpg.ruchu.widget.leafchart.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.jkpg.ruchu.widget.leafchart.bean.Axis;
import com.jkpg.ruchu.widget.leafchart.bean.PointValue;
import com.jkpg.ruchu.widget.leafchart.bean.Square;
import com.jkpg.ruchu.widget.leafchart.support.LeafUtil;

/**
 * Created by qindi on 2017/5/18.
 */

public class SquareRenderer extends AbsRenderer {
    public SquareRenderer(Context context, View view) {
        super(context, view);
    }

    public void drawSquares(Canvas canvas, Square square, Axis axisX, int i) {
        if (square != null) {
            //1.画直方图边界
            linePaint.setColor(Color.parseColor("#FF5070"));
            if (!square.isFill()) {
                linePaint.setStrokeWidth(LeafUtil.dp2px(mContext, square.getBorderWidth()));
                linePaint.setStyle(Paint.Style.STROKE);
            }
            PointValue point = square.getValues().get(i);
            float width = LeafUtil.dp2px(mContext, square.getWidth());
            RectF rectF = new RectF(point.getOriginX() - width / 2,
                    point.getOriginY(), point.getOriginX() + width / 2, axisX.getStartY());

            canvas.drawRect(rectF, linePaint);
        }
    }
}

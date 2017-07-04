package com.jkpg.ruchu.widget.leafchart.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.jkpg.ruchu.widget.leafchart.bean.Axis;
import com.jkpg.ruchu.widget.leafchart.bean.PointValue;
import com.jkpg.ruchu.widget.leafchart.bean.Square;
import com.jkpg.ruchu.widget.leafchart.support.LeafUtil;

import java.util.List;


/**
 * 描述：直方图
 * </br>
 */

public class LeafSquareRenderer extends AbsRenderer {

    public LeafSquareRenderer(Context context, View view) {
        super(context, view);
    }

    public void drawSquares(Canvas canvas, Square square, Axis axisX) {
        if (square != null) {
            //1.画直方图边界
            linePaint.setColor(square.getBorderColor());
            if (!square.isFill()) {
                linePaint.setStrokeWidth(LeafUtil.dp2px(mContext, square.getBorderWidth()));
                linePaint.setStyle(Paint.Style.STROKE);
            }
            List<PointValue> values = square.getValues();
            float width = LeafUtil.dp2px(mContext, square.getWidth());
            for (PointValue point : values) {
                RectF rectF = new RectF(point.getOriginX() - width / 2,
                        point.getOriginY(), point.getOriginX() + width / 2, axisX.getStartY());

                canvas.drawRect(rectF, linePaint);
            }
        }
    }


}

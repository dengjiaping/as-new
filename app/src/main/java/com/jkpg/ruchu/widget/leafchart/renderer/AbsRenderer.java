package com.jkpg.ruchu.widget.leafchart.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.jkpg.ruchu.widget.leafchart.bean.Axis;
import com.jkpg.ruchu.widget.leafchart.bean.AxisValue;
import com.jkpg.ruchu.widget.leafchart.bean.ChartData;
import com.jkpg.ruchu.widget.leafchart.bean.PointValue;
import com.jkpg.ruchu.widget.leafchart.support.LeafUtil;

import java.util.List;

/**
 * 描述：渲染器
 * </br>
 */

public class AbsRenderer {

    protected Context mContext;

    protected View chartView;

    /**
     * 控件宽度
     **/
    protected float mWidth;
    /**
     * 控件高度
     **/
    protected float mHeight;
    /**
     * 控件内部间隔
     **/
    protected float leftPadding, topPadding, rightPadding, bottomPadding;

    /**
     * 坐标轴
     */
    protected Paint coordPaint;

    /**
     * 折线图
     */
    protected Paint linePaint;

    /**
     * 标签
     */
    protected Paint labelPaint;

    /**
     * 填充
     */
    protected Paint fillPaint;


    public AbsRenderer(Context context, View view) {
        this.mContext = context;
        this.chartView = view;
        initPaint();
    }

    protected void initPaint() {
        coordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
    }

    public void setWH(float width, float height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public void setPadding(float l, float t, float r, float b) {
        this.leftPadding = l;
        this.topPadding = t;
        this.rightPadding = r;
        this.bottomPadding = b;
    }

    /**
     * 坐标轴
     *
     * @param canvas
     */
    public void drawCoordinateLines(Canvas canvas, Axis axisX, Axis axisY) {
        if (axisX != null && axisY != null) {
            // 平行于y 轴的坐标轴
            if (axisY.isHasLines()) {
                coordPaint.setColor(axisY.getAxisLineColor());
                coordPaint.setStrokeWidth(LeafUtil.dp2px(mContext, axisY.getAxisLineWidth()));
                List<AxisValue> valuesX = axisX.getValues();
                int sizeX = valuesX.size();
                for (int i = 0; i < sizeX; i++) {
                    AxisValue value = valuesX.get(i);
                    canvas.drawLine(value.getPointX(),
                            axisY.getStartY() - LeafUtil.dp2px(mContext, axisY.getAxisWidth()),
                            value.getPointX(), axisY.getStopY(), coordPaint);
                }
            }

            // 平行于x轴的坐标轴
            if (axisX.isHasLines()) {
                coordPaint.setColor(axisX.getAxisLineColor());
                coordPaint.setStrokeWidth(LeafUtil.dp2px(mContext, axisX.getAxisLineWidth()));
                List<AxisValue> valuesY = axisY.getValues();
                int sizeY = valuesY.size();
                for (int i = 0; i < sizeY; i++) {
                    AxisValue value = valuesY.get(i);
                    canvas.drawLine(axisY.getStartX() + LeafUtil.dp2px(mContext, axisX.getAxisWidth()),
                            value.getPointY(),
                            axisX.getStopX(),
                            value.getPointY(), coordPaint);
                }
            }

            //X坐标轴
            coordPaint.setColor(axisX.getAxisColor());
            coordPaint.setStrokeWidth(LeafUtil.dp2px(mContext, axisX.getAxisWidth()));
            canvas.drawLine(axisX.getStartX(), axisX.getStartY(), axisX.getStopX(), axisX.getStopY(), coordPaint);

            //Y坐标轴
            coordPaint.setColor(axisY.getAxisColor());
            coordPaint.setStrokeWidth(LeafUtil.dp2px(mContext, axisY.getAxisWidth()));
            canvas.drawLine(axisY.getStartX(),
                    axisY.getStartY(), axisY.getStopX(), axisY.getStopY(), coordPaint);
        }
    }

    /**
     * 画坐标轴 刻度值
     *
     * @param canvas
     */
    public void drawCoordinateText(Canvas canvas, Axis axisX, Axis axisY) {
        if (axisX != null && axisY != null) {
            //////// X 轴
            // 1.刻度
            coordPaint.setColor(axisX.getTextColor());
            coordPaint.setTextSize(LeafUtil.sp2px(mContext, axisX.getTextSize()));

            Paint.FontMetrics fontMetrics = coordPaint.getFontMetrics(); // 获取标题文字的高度（fontMetrics.descent - fontMetrics.ascent）
            float textH = fontMetrics.descent - fontMetrics.ascent;

            List<AxisValue> valuesX = axisX.getValues();
            if (axisX.isShowText()) {
                for (int i = 0; i < valuesX.size(); i++) {
                    AxisValue value = valuesX.get(i);
                    if (value.isShowLabel()) {
                        float textW = coordPaint.measureText(value.getLabel());
                        canvas.drawText(value.getLabel(), value.getPointX() - textW / 2, value.getPointY() - textH / 2, coordPaint);
                    }
                }
            }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////// Y 轴
            coordPaint.setColor(axisY.getTextColor());
            coordPaint.setTextSize(LeafUtil.sp2px(mContext, axisY.getTextSize()));

            List<AxisValue> valuesY = axisY.getValues();
            if (axisY.isShowText()) {
                for (AxisValue value : valuesY) {
                    float textW = coordPaint.measureText(value.getLabel());
                    float pointx = value.getPointX() - 1.1f * textW;
                    canvas.drawText(value.getLabel(), pointx, value.getPointY(), coordPaint);
                }
            }
        }
    }

    /**
     * 画标签
     *
     * @param canvas
     * @param chartData
     */
    public void drawLabels(Canvas canvas, ChartData chartData, Axis axisY) {
        if (chartData != null) {
            if (chartData.isHasLabels()) {
                labelPaint.setTextSize(LeafUtil.sp2px(mContext, 12));
                List<PointValue> values = chartData.getValues();
                int size = values.size();
                for (int i = 0; i < size; i++) {
                    PointValue point = values.get(i);
                    String label = point.getLabel();
                    Rect bounds = new Rect();
                    int length = label.length();
                    labelPaint.getTextBounds(label, 0, length, bounds);

                    float textW = bounds.width();
                    float textH = bounds.height();
                    float left, top, right, bottom;
                    if (length == 1) {
                        left = point.getOriginX() - textW * 2.2f;
                        right = point.getOriginX() + textW * 2.2f;
                    } else if (length == 2) {
                        left = point.getOriginX() - textW * 1.0f;
                        right = point.getOriginX() + textW * 1.0f;
                    } else {
                        left = point.getOriginX() - textW * 0.6f;
                        right = point.getOriginX() + textW * 0.6f;
                    }
                    top = point.getOriginY() - 2.5f * textH;
                    bottom = point.getOriginY() - 0.5f * textH;

                    //控制位置
                    if (left < axisY.getStartX()) {
                        left = axisY.getStartX();
                        right += left;
                    }
                    if (top < 0) {
                        top = topPadding;
                        bottom += topPadding;
                    }
                    if (right > mWidth) {
                        right -= rightPadding;
                        left -= rightPadding;
                    }

                    RectF rectF = new RectF(left, top, right, bottom);
                    float labelRadius = LeafUtil.dp2px(mContext, chartData.getLabelRadius());
                    labelPaint.setColor(chartData.getLabelColor());
                    labelPaint.setStyle(Paint.Style.FILL);
                    canvas.drawRoundRect(rectF, labelRadius, labelRadius, labelPaint);

                    //drawText
                    labelPaint.setColor(Color.WHITE);
                    float xCoordinate = left + (right - left - textW) / 2;
                    float yCoordinate = bottom - (bottom - top - textH) / 2;
                    canvas.drawText(point.getLabel(), xCoordinate, yCoordinate, labelPaint);
                }
            }
        }
    }

    private boolean isInArea(float x, float y, float touchX, float touchY, float radius) {
        float diffX = touchX - x;
        float diffY = touchY - y;
        return Math.pow(diffX, 2) + Math.pow(diffY, 2) <= 2 * Math.pow(radius, 2);
    }


}

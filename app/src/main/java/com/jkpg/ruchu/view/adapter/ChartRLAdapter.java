package com.jkpg.ruchu.view.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.LogUtils;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.leafchart.LeafLineChart;
import com.jkpg.ruchu.widget.leafchart.bean.Axis;
import com.jkpg.ruchu.widget.leafchart.bean.AxisValue;
import com.jkpg.ruchu.widget.leafchart.bean.Line;
import com.jkpg.ruchu.widget.leafchart.bean.PointValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qindi on 2017/5/19.
 */

public class ChartRLAdapter extends RecyclerView.Adapter {
    private List<float[]> charts;
    private List<float[]> chartsX;
    private Map<Integer, Boolean> map = new HashMap<>();
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_WHITE = 2;
    private View mNormalView;
    private View mFootView;
    private View mWhiteView;

    public ChartRLAdapter(List<float[]> chartsX, List<float[]> charts) {
        this.chartsX = chartsX;
        this.charts = charts;
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < charts.size(); i++) {
            map.put(i, false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 2) {
            return TYPE_FOOTER;
        } else if (position == getItemCount() - 1) {
            return TYPE_WHITE;
        }
        return TYPE_NORMAL;

    }

    @Override
    public FootViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            mNormalView = View.inflate(UIUtils.getContext(), R.layout.item_chart, null);
            return new ChartViewHolder(mNormalView);
        } else if (viewType == TYPE_WHITE) {
            mWhiteView = View.inflate(UIUtils.getContext(), R.layout.item_chart_white, null);
            return new FootViewHolder(mWhiteView);
        } else {
            mFootView = View.inflate(UIUtils.getContext(), R.layout.item_chart_foot, null);
            return new FootViewHolder(mFootView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != TYPE_NORMAL) {
            return;
        }
        ChartViewHolder chartViewHolder = (ChartViewHolder) holder;
        float[] pointXs = chartsX.get(position);
        float[] points = charts.get(position);
        initChart(chartViewHolder, pointXs, points, (int) pointXs[pointXs.length - 1] * 2);
        if (map.get(position)) {
            chartViewHolder.mItemChartRL.setBackgroundResource(R.drawable.shap_rectangle_pink);
            chartViewHolder.mItemChartIvStart.setVisibility(View.GONE);
        } else {
            chartViewHolder.mItemChartRL.setBackgroundResource(R.drawable.shap_rectangle_yellow);
            chartViewHolder.mItemChartIvStart.setVisibility(View.GONE);
        }

    }

    private void initChart(ChartViewHolder holder, float[] pointsX, float[] points, int numberX) {
        Axis axisX = new Axis(getAxisValuesX(numberX));
        axisX.setAxisColor(Color.parseColor("#00FCA29A")).setTextColor(Color.WHITE)
                .setHasLines(false).setAxisLineColor(Color.parseColor("#00FCA29A")).setShowText(true).setTextSize(5);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#00FCA29A")).setTextColor(Color.WHITE).setHasLines(false).setShowText(false).setTextSize(5)
                .setAxisLineColor(Color.parseColor("#00FCA29A"));
        holder.mItemChartLine.setAxisX(axisX);
        holder.mItemChartLine.setAxisY(axisY);
        ArrayList<Line> line = new ArrayList<>();
        line.add(getDottedLine(pointsX, points));

        holder.mItemChartLine.setChartData(line);
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
            // pointValues.add(new PointValue((i / (float) (points.length - 1)), points[i] / 6f));
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
        }
        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#FAD719"))
                .setLineWidth(1);
                /*.setFill(true)
                .setFillColor(Color.WHITE)
                .setHasPoints(false);*/
        return line;
    }

    @Override
    public int getItemCount() {
        if (charts != null)
            return charts.size() + 2;
        return 0;
    }

    class ChartViewHolder extends FootViewHolder {
        @BindView(R.id.item_chart_line)
        LeafLineChart mItemChartLine;
        @BindView(R.id.item_chart_iv_start)
        ImageView mItemChartIvStart;
        @BindView(R.id.item_chart_rl)
        RelativeLayout mItemChartRL;


        ChartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
    class FootViewHolder extends RecyclerView.ViewHolder {
        FootViewHolder(View view) {
            super(view);
        }
    }

    public Map<Integer, Boolean> getMap() {
        return map;
    }

}

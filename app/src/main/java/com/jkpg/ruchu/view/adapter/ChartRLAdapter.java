package com.jkpg.ruchu.view.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jkpg.ruchu.R;
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

public class ChartRLAdapter extends RecyclerView.Adapter<ChartRLAdapter.ChartViewHolder> {
    private List<float[]> charts;
    private Map<Integer, Boolean> map = new HashMap<>();

    public ChartRLAdapter(List<float[]> charts) {
        this.charts = charts;
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < charts.size(); i++) {
            map.put(i, false);
        }
    }

    @Override
    public ChartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(UIUtils.getContext(), R.layout.item_chart, null);
        return new ChartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChartViewHolder holder, int position) {
        float[] points = charts.get(position);
        initChart(holder, points, points.length);
        if (map.get(position)) {
            holder.mItemChartRL.setBackgroundResource(R.drawable.shap_rectangle_pink);
            holder.mItemChartIvStart.setVisibility(View.GONE);
        } else {
            holder.mItemChartRL.setBackgroundResource(R.drawable.shap_rectangle_yellow);
            holder.mItemChartIvStart.setVisibility(View.VISIBLE);

        }
    }

    private void initChart(ChartViewHolder holder, float[] points, int numberX) {
        Axis axisX = new Axis(getAxisValuesX(numberX));
        axisX.setAxisColor(Color.parseColor("#00FCA29A")).setTextColor(Color.WHITE)
                .setHasLines(false).setAxisLineColor(Color.parseColor("#00FCA29A")).setShowText(true).setTextSize(5);
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#00FCA29A")).setTextColor(Color.WHITE).setHasLines(false).setShowText(false).setTextSize(5)
                .setAxisLineColor(Color.parseColor("#00FCA29A"));
        holder.mItemChartLine.setAxisX(axisX);
        holder.mItemChartLine.setAxisY(axisY);
        ArrayList<Line> lines1 = new ArrayList<>();
        lines1.add(getDottedLine(points));

        holder.mItemChartLine.setChartData(lines1);
    }

    private List<AxisValue> getAxisValuesX(int numberX) {
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < numberX; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(i + "");
            axisValues.add(value);
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

    private Line getDottedLine(float[] points) {
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 0; i < points.length; i++) {
            pointValues.add(new PointValue((i / (float) (points.length - 1)), points[i] / 6f));
        }
        Line line = new Line(pointValues);
        line.setLineColor(Color.parseColor("#FAD719"))
                .setLineWidth(1)
                .setHasPoints(false);
        return line;
    }

    @Override
    public int getItemCount() {
        if (charts != null)
            return charts.size();
        return 0;
    }

    public static class ChartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_chart_line)
        public LeafLineChart mItemChartLine;
        @BindView(R.id.item_chart_iv_start)
        public ImageView mItemChartIvStart;
        @BindView(R.id.item_chart_rl)
        public RelativeLayout mItemChartRL;


        ChartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public Map<Integer, Boolean> getMap() {
        return map;
    }

}

package com.jkpg.ruchu.view.activity.train;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jkpg.ruchu.R;
import com.jkpg.ruchu.utils.UIUtils;
import com.jkpg.ruchu.widget.leafchart.LeafSquareChart;
import com.jkpg.ruchu.widget.leafchart.SquareChart;
import com.jkpg.ruchu.widget.leafchart.bean.Axis;
import com.jkpg.ruchu.widget.leafchart.bean.AxisValue;
import com.jkpg.ruchu.widget.leafchart.bean.PointValue;
import com.jkpg.ruchu.widget.leafchart.bean.Square;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qindi on 2017/5/18.
 */

public class MyTrainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.header_iv_left)
    ImageView mHeaderIvLeft;
    @BindView(R.id.header_tv_title)
    TextView mHeaderTvTitle;
    @BindView(R.id.my_train_tv_grade)
    TextView mMyTrainTvGrade;
    @BindView(R.id.my_train_tv_select_grade)
    TextView mMyTrainTvSelectGrade;
    @BindView(R.id.my_train_tv_body)
    TextView mMyTrainTvBody;
    @BindView(R.id.my_train_btn_start)
    Button mMyTrainBtnStart;
    @BindView(R.id.my_train_tv_edit)
    TextView mMyTrainTvEdit;
    @BindView(R.id.my_train_square_chart)
    LeafSquareChart mMyTrainSquareChart;
    @BindView(R.id.my_train_square1)
    SquareChart mMyTrainSquare1;
    @BindView(R.id.my_train_square2)
    SquareChart mMyTrainSquare2;
    @BindView(R.id.my_train_square3)
    SquareChart mMyTrainSquare3;
    @BindView(R.id.my_train_square4)
    SquareChart mMyTrainSquare4;
    @BindView(R.id.my_train_square5)
    SquareChart mMyTrainSquare5;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_train);
        ButterKnife.bind(this);
        initHeader();
        initSquareChart();
    }

    private void initSquareChart() {
        Axis axisX = new Axis(getAxisValuesX());
        axisX.setAxisColor(Color.parseColor("#FF5070")).setTextColor(Color.DKGRAY).setHasLines(true).setAxisLineColor(Color.parseColor("#9e9e9e"));
        Axis axisY = new Axis(getAxisValuesY());
        axisY.setAxisColor(Color.parseColor("#FF5070")).setTextColor(Color.DKGRAY).setHasLines(false);

        mMyTrainSquareChart.setAxisX(axisX);
        mMyTrainSquareChart.setAxisY(axisY);
        mMyTrainSquareChart.setChartData(getSquares());
        mMyTrainSquare1.setChartData(getSquares());
        mMyTrainSquare1.setI(0);
        mMyTrainSquare1.setAxisX(axisX);
        mMyTrainSquare1.setAxisY(axisY);
        mMyTrainSquare2.setChartData(getSquares());
        mMyTrainSquare2.setI(1);
        mMyTrainSquare2.setAxisX(axisX);
        mMyTrainSquare2.setAxisY(axisY);
        mMyTrainSquare3.setChartData(getSquares());
        mMyTrainSquare3.setI(2);
        mMyTrainSquare3.setAxisX(axisX);
        mMyTrainSquare3.setAxisY(axisY);
        mMyTrainSquare4.setChartData(getSquares());
        mMyTrainSquare4.setI(3);
        mMyTrainSquare4.setAxisX(axisX);
        mMyTrainSquare4.setAxisY(axisY);
        mMyTrainSquare5.setChartData(getSquares());
        mMyTrainSquare5.setI(4);
        mMyTrainSquare5.setAxisX(axisX);
        mMyTrainSquare5.setAxisY(axisY);
    }

    private List<AxisValue> getAxisValuesX() {
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            AxisValue value = new AxisValue();
            value.setLabel("LV " + i);
            axisValues.add(value);
        }
        return axisValues;
    }

    private List<AxisValue> getAxisValuesY() {
        List<AxisValue> axisValues = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            AxisValue value = new AxisValue();
            value.setLabel(String.valueOf(i * 10) + " ");
            axisValues.add(value);
        }
        return axisValues;
    }

    private Square getSquares() {
        List<PointValue> pointValues = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            PointValue pointValue = new PointValue("");
            pointValue.setX((i - 1) / 4f);
            pointValue.setY(i * 10 / 50f);
            pointValues.add(pointValue);
        }

        Square square = new Square(pointValues);
        square
                .setWidth(20)
                .setBorderColor(Color.parseColor("#9e9e9e"))
                .setFill(true);
        return square;
    }


    private void initHeader() {
        mHeaderTvTitle.setText("我的训练");
    }

    @OnClick({R.id.header_iv_left, R.id.my_train_tv_select_grade, R.id.my_train_btn_start, R.id.my_train_tv_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.header_iv_left:
                finish();
                break;
            case R.id.my_train_tv_select_grade:
                selectGrade();
                break;
            case R.id.my_train_btn_start:
                startPlan();
                break;
            case R.id.my_train_tv_edit:
                break;
        }
    }

    private void startPlan() {
        new AlertDialog.Builder(this)
                .setMessage("确定要启用" + mMyTrainTvSelectGrade.getText() + "的训练计划吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2017/5/18
                        mMyTrainTvGrade.setText(mMyTrainTvSelectGrade.getText());
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //// TODO: 2017/5/18  之前的计划
                    }
                })
                .show();
    }

    private void selectGrade() {
        mPopupWindow = new PopupWindow(this);
        mPopupWindow.setWidth(UIUtils.dip2Px(100));
        mPopupWindow.setHeight(LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(UIUtils.getContext(), R.layout.view_select_grade, null);
        view.findViewById(R.id.view_LV1).setOnClickListener(this);
        view.findViewById(R.id.view_LV2).setOnClickListener(this);
        view.findViewById(R.id.view_LV3).setOnClickListener(this);
        view.findViewById(R.id.view_LV4).setOnClickListener(this);
        view.findViewById(R.id.view_LV5).setOnClickListener(this);
        mPopupWindow.setContentView(view);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setFocusable(true);
        mPopupWindow.showAsDropDown(mMyTrainTvSelectGrade);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_LV1:
                mMyTrainTvSelectGrade.setText("LV 1");
                mMyTrainSquare1.setVisibility(View.VISIBLE);
                mMyTrainSquare2.setVisibility(View.GONE);
                mMyTrainSquare3.setVisibility(View.GONE);
                mMyTrainSquare4.setVisibility(View.GONE);
                mMyTrainSquare5.setVisibility(View.GONE);

                break;
            case R.id.view_LV2:
                mMyTrainTvSelectGrade.setText("LV 2");
                mMyTrainSquare2.setVisibility(View.VISIBLE);
                mMyTrainSquare1.setVisibility(View.GONE);
                mMyTrainSquare3.setVisibility(View.GONE);
                mMyTrainSquare4.setVisibility(View.GONE);
                mMyTrainSquare5.setVisibility(View.GONE);
                break;
            case R.id.view_LV3:
                mMyTrainTvSelectGrade.setText("LV 3");
                mMyTrainSquare3.setVisibility(View.VISIBLE);
                mMyTrainSquare2.setVisibility(View.GONE);
                mMyTrainSquare1.setVisibility(View.GONE);
                mMyTrainSquare4.setVisibility(View.GONE);
                mMyTrainSquare5.setVisibility(View.GONE);
                break;
            case R.id.view_LV4:
                mMyTrainTvSelectGrade.setText("LV 4");
                mMyTrainSquare4.setVisibility(View.VISIBLE);
                mMyTrainSquare2.setVisibility(View.GONE);
                mMyTrainSquare3.setVisibility(View.GONE);
                mMyTrainSquare1.setVisibility(View.GONE);
                mMyTrainSquare5.setVisibility(View.GONE);
                break;
            case R.id.view_LV5:
                mMyTrainTvSelectGrade.setText("LV 5");
                mMyTrainSquare5.setVisibility(View.VISIBLE);
                mMyTrainSquare2.setVisibility(View.GONE);
                mMyTrainSquare3.setVisibility(View.GONE);
                mMyTrainSquare4.setVisibility(View.GONE);
                mMyTrainSquare1.setVisibility(View.GONE);
                break;
        }
        mPopupWindow.dismiss();
    }
}

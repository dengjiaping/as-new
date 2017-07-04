package com.jkpg.ruchu.widget.leafchart.support;


import com.jkpg.ruchu.widget.leafchart.bean.Axis;

/**
 * 描述：
 * </br>
 */
public interface Chart {

    void setAxisX(Axis axisX);

    void setAxisY(Axis axisY);

    Axis getAxisX();

    Axis getAxisY();
}

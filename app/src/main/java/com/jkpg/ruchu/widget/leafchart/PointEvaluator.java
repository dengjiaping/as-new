package com.jkpg.ruchu.widget.leafchart;

import android.animation.TypeEvaluator;

import com.jkpg.ruchu.widget.leafchart.bean.Point;

/**
 * Created by qindi on 2017/11/17.
 */

public class PointEvaluator implements TypeEvaluator<Point> {
    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        float resultX = startValue.getX() + fraction * (endValue.getX() - startValue.getX());
        float resultY = startValue.getY() + fraction * (endValue.getY() - startValue.getX());
        return new Point(resultX, resultY);
    }
}
package com.mosl.tabapp.common;

/**
 * Created by raju on 7/17/2017.
 */

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.PieChart;


public class LockedPieChart extends PieChart {

    public LockedPieChart(Context context) {
        super(context);
    }

    public LockedPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockedPieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    /**
     * When it retuens 0f rotaion of pie chart is stoped we had to sto rotation of
     * */
    @Override
    public float getRotationAngle() {
        return isRotationEnabled()?0f:super.getRotationAngle();
    }
}
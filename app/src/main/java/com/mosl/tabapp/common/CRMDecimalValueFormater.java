package com.mosl.tabapp.common;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Raju on 19-03-2018.
 */

public class CRMDecimalValueFormater implements IValueFormatter
{

    private DecimalFormat mFormat;

    public CRMDecimalValueFormater() {
        mFormat = new DecimalFormat("########0.00");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if(value > 0) {
            return mFormat.format(value);
        } else {
            return "";
        }
    }
}


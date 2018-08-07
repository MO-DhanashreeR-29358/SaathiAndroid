package com.mosl.mobile.ekyc.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.EkycAppUtil;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Featsystem on 21-02-2017.
 */

public class CustomDatePickerDialog extends Dialog implements DatePickerDialog.OnDateSetListener {
    Context context;
    DatePickerListener datePickerListener;
    String mainFromDate = "";
    String[] dateSplit;
    String defaultDateSet = "";
    boolean defaultDate;
    String date = "";
    boolean isMaxDate;
    Date maxDate;

    public CustomDatePickerDialog(Context context, boolean defaultDate, String date, boolean isMaxDate, DatePickerListener datePickerListener) {
        super(context);
        this.context = context;
        this.datePickerListener = datePickerListener;
        this.defaultDate = defaultDate;
        this.date = date;
        this.isMaxDate = isMaxDate;
    }
    public CustomDatePickerDialog(Context context, boolean defaultDate, String date, Date maxDate, DatePickerListener datePickerListener) {
        super(context);
        this.context = context;
        this.datePickerListener = datePickerListener;
        this.defaultDate = defaultDate;
        this.date = date;
        this.maxDate = maxDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Calendar today = Calendar.getInstance();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.custom_date_picker);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePickerFromDate);

        ImageView close = (ImageView) findViewById(R.id.ibtnClose);
        Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
        if (isMaxDate) {
            datePicker.setMaxDate(System.currentTimeMillis());
        } else if(maxDate!=null){
            Calendar maxCal = Calendar.getInstance();
            maxCal.setTime(maxDate);
            datePicker.setMaxDate(maxCal.getTimeInMillis());
        }

        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        dateSplit = mainFromDate.split("/");

        if (!mainFromDate.equals("")) {
            dateSplit = mainFromDate.split("/");

            datePicker.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mainFromDate = prefix(dayOfMonth) + "/" + prefix(monthOfYear + 1) + "/" + year;
                }
            });
        } else {
            if (defaultDate) {
                int currentMonth = today.get(Calendar.MONTH) + 1;
                datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mainFromDate = prefix(dayOfMonth) + "/" + prefix(monthOfYear + 1) + "/" + year;
                    }
                });
//                datePicker.updateDate(today.get(Calendar.YEAR),currentMonth,today.get(Calendar.DAY_OF_MONTH));
//                datePicker.init(today.get(Calendar.YEAR), currentMonth, today.getFirstDayOfWeek(), null);
                mainFromDate = today.get(Calendar.DAY_OF_MONTH) + "/" + currentMonth + "/" + today.get(Calendar.YEAR);
            } else {
                Date dob = EkycAppUtil.getDate(date, "dd/MM/yyyy");
                String[] dateSplit = date.split("/");
                datePicker.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mainFromDate = prefix(dayOfMonth) + "/" + prefix(monthOfYear + 1) + "/" + year;
                    }
                });
                datePicker.updateDate(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
            }

            /*datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.getFirstDayOfWeek(), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mainFromDate = prefix(dayOfMonth) + "/" + prefix(monthOfYear + 1) + "/" + year;
                }
            });*/
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                datePickerListener.submitDatePicker(mainFromDate);
                dismiss();

            }

        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public String prefix(int input) {
        if (Integer.toString(input).length() == 1) {
            return "0" + input;
        }
        return input + "";
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        defaultDateSet = prefix(dayOfMonth) + "/" + prefix(month + 1) + "/" + year;
    }
}

package com.mosl.tabapp.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.ui.MOSLCommonFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by raju on 2/20/2017.
 */

public class SnapshotFragment extends MOSLCommonFragment implements View.OnClickListener {
    private View snapshotView;
    static Context mContext;
    private TextView tvStartDate, tvEndDate;
    private String mainFromDate, mainToDate, toPostingDate, fromPostingDate;
    private Date fromDat, toDate;
    private Dialog dialog;
    private Calendar today;
    private DatePicker dpFromDate, dpToDate;
    private ImageView close;
    private Button btnSubmit;
    private TextView tvFamiMapped, tvFamiMappedCount, tvMet, tvMetMTD, tvMetYTD, tvReach, tvReachMTD, tvReachYTD,
            tvActive, tvActiveMTD, tvActiveYTD, tvProfile, tvProfileMTD, tvProfileYTD,
            tvTraded, tvTradedMTD, tvTradedYTD, tvLogged, tvLoggedMTD, tvLoggedYTD, tvTradedMob, tvTradedMobMTD, tvTradedMobYTD;
    private RelativeLayout rlDate;
    private LinearLayout llMet, llReach, llActive, llProfileComplete, llTradedOnline, llLoggedMobile, llTradedMobile;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        snapshotView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_snapshot);
        setUpView(snapshotView);
        hideAppTitle(getClass().toString());
        return snapshotView;
    }

    public static SnapshotFragment newInstance(Context context) {
        SnapshotFragment fragment = new SnapshotFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View snapshotView) {
        tvStartDate = (TextView) snapshotView.findViewById(R.id.tvStartDate);
        //  tvEndDate=(TextView)snapshotView.findViewById(R.id.tvEndDate);
        mainFromDate = CRMAppUtil.getFirstDateOfCurrentMonth("yyyy-MM-dd");
        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
        mainToDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
        toPostingDate = CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy");
        tvStartDate.setText(CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy"));
        rlDate = (RelativeLayout) snapshotView.findViewById(R.id.rlDate);
        rlDate.setOnClickListener(this);
       /* tvEndDate.setText(CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy"));
        tvEndDate.setOnClickListener(this);*/

        tvFamiMapped = (TextView) snapshotView.findViewById(R.id.tvFamiMapped);
        tvFamiMappedCount = (TextView) snapshotView.findViewById(R.id.tvFamiMappedCount);
        tvMet = (TextView) snapshotView.findViewById(R.id.tvMet);
        tvMetMTD = (TextView) snapshotView.findViewById(R.id.tvMetMTD);
        tvMetYTD = (TextView) snapshotView.findViewById(R.id.tvMetYTD);
        tvReach = (TextView) snapshotView.findViewById(R.id.tvReach);
        tvReachMTD = (TextView) snapshotView.findViewById(R.id.tvReachMTD);
        tvReachYTD = (TextView) snapshotView.findViewById(R.id.tvReachYTD);
        tvActive = (TextView) snapshotView.findViewById(R.id.tvActive);
        tvActiveMTD = (TextView) snapshotView.findViewById(R.id.tvActiveMTD);
        tvActiveYTD = (TextView) snapshotView.findViewById(R.id.tvActiveYTD);
        tvProfile = (TextView) snapshotView.findViewById(R.id.tvProfile);
        tvProfileMTD = (TextView) snapshotView.findViewById(R.id.tvProfileMTD);
        tvProfileYTD = (TextView) snapshotView.findViewById(R.id.tvProfileYTD);
        tvTraded = (TextView) snapshotView.findViewById(R.id.tvTraded);
        tvTradedMTD = (TextView) snapshotView.findViewById(R.id.tvTradedMTD);
        tvTradedYTD = (TextView) snapshotView.findViewById(R.id.tvTradedYTD);
        tvLogged = (TextView) snapshotView.findViewById(R.id.tvLogged);
        tvLoggedMTD = (TextView) snapshotView.findViewById(R.id.tvLoggedMTD);
        tvLoggedYTD = (TextView) snapshotView.findViewById(R.id.tvLoggedYTD);
        tvTradedMob = (TextView) snapshotView.findViewById(R.id.tvTradedMob);
        tvTradedMobMTD = (TextView) snapshotView.findViewById(R.id.tvTradedMobMTD);
        tvTradedMobYTD = (TextView) snapshotView.findViewById(R.id.tvTradedMobYTD);
        llMet = (LinearLayout) snapshotView.findViewById(R.id.llMet);
        llReach = (LinearLayout) snapshotView.findViewById(R.id.llReach);
        llActive = (LinearLayout) snapshotView.findViewById(R.id.llActive);
        llProfileComplete = (LinearLayout) snapshotView.findViewById(R.id.llProfileComplete);
        llTradedOnline = (LinearLayout) snapshotView.findViewById(R.id.llTradedOnline);
        llLoggedMobile = (LinearLayout) snapshotView.findViewById(R.id.llLoggedMobile);
        llTradedMobile = (LinearLayout) snapshotView.findViewById(R.id.llTradedMobile);
        llMet.setOnClickListener(this);
        llReach.setOnClickListener(this);
        llActive.setOnClickListener(this);
        llProfileComplete.setOnClickListener(this);
        llTradedOnline.setOnClickListener(this);
        llLoggedMobile.setOnClickListener(this);
        llTradedMobile.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        Bundle args = new Bundle();
        if (id == R.id.rlDate) {
            selectDate();
        } else if (id == R.id.llMet) {
            args.putString("cliencode", "");
            navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
        } else if (id == R.id.llReach) {
            args.putString("cliencode", "");
            navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
        } else if (id == R.id.llActive) {
            args.putString("cliencode", "");
            navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
        } else if (id == R.id.llProfileComplete) {
            args.putString("cliencode", "");
            navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
        } else if (id == R.id.llTradedOnline) {
            args.putString("cliencode", "");
            navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
        } else if (id == R.id.llLoggedMobile) {
            args.putString("cliencode", "");
            navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
        } else if (id == R.id.llTradedMobile) {
            args.putString("cliencode", "");
            navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
        }
    }

    public void selectDate() {
        today = Calendar.getInstance();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.date_picker);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dpFromDate = (DatePicker) dialog.findViewById(R.id.datePickerFromDate);
        dpFromDate.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        // dpToDate = (DatePicker) dialog.findViewById(R.id.datePickerToDate);
        close = (ImageView) dialog.findViewById(R.id.ibtnClose);
        btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);


        if (mainFromDate != null) {
            String[] dateSplit = mainFromDate.split("-");
            dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mainFromDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                }
            });
        } else {
            dpFromDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.getFirstDayOfWeek(), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mainFromDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                }
            });
        }

       /* if (mainFromDate != null) {
            String[] dateSplit = mainToDate.split("-");
            dpToDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                    mainToDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);

                }
            });
        } else {
            dpToDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    today.set(year, monthOfYear, dayOfMonth);
                    long dayy = today.getActualMaximum(Calendar.DATE);
                    mainToDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);


                }
            });
        }*/

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                if (mainFromDate != null) {
                    fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                    fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
                    tvStartDate.setText(fromPostingDate);
                }
                dialog.dismiss();
               /* if (fromDat.after(toDate)) {
                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), "Invalid date range ", "OK", true, null);
                } else {
                    if (mainFromDate != null) {
                        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
                        tvStartDate.setText(fromPostingDate);
                    }
                    if (mainToDate != null) {
                        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                        toPostingDate = CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy");
                        tvEndDate.setText(toPostingDate);
                    }

                    dialog.dismiss();
                }*/


            }
        });

        dialog.show();
    }

    public String prefix(int input) {
        if (Integer.toString(input).length() == 1) {
            return "0" + input;
        }
        return input + "";
    }
}

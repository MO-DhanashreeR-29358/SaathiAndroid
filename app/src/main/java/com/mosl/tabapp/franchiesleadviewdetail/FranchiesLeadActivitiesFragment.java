package com.mosl.tabapp.franchiesleadviewdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.CustomViewPager;
import com.mosl.tabapp.model.LeadActivitiesAppointModel;
import com.mosl.tabapp.model.LeadActivitiesMeetingModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 3/15/2017.
 */

public class FranchiesLeadActivitiesFragment extends MOSLCommonFragment implements View.OnClickListener {
    private View activitiesView;
    private TextView tvAppointment, tvMeeting;
    private LinearLayout llAppointment, llMeeting;
    private RecyclerView rvAppointment, rvMeeting;
    private ArrayList<LeadActivitiesAppointModel> appointmentList;
    private AppointmentAdapter appointmentAdapter;
    private ArrayList<LeadActivitiesMeetingModel> meetingList;
    private MeetingAdapter meetingAdapter;
    private static String leadId;
    private Context mContext;
    private CustomViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activitiesView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_lead_activities);
        setUpView(activitiesView);
        setUserVisibleHint(true);
        return activitiesView;
    }

    public static FranchiesLeadActivitiesFragment newInstance(int position, String type, String leadID) {
        FranchiesLeadActivitiesFragment fragment = new FranchiesLeadActivitiesFragment();
        leadId = leadID;
        Bundle args = new Bundle();
        args.putInt("Position", position);
        args.putString("Type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public void setViewPager(CustomViewPager viewPager) {
        this.viewPager = viewPager;
    }

    private void setUpView(View activitiesView) {
        tvAppointment = (TextView) activitiesView.findViewById(R.id.tvAppointment);
        tvMeeting = (TextView) activitiesView.findViewById(R.id.tvMeeting);
        llAppointment = (LinearLayout) activitiesView.findViewById(R.id.llAppointment);
        llMeeting = (LinearLayout) activitiesView.findViewById(R.id.llMeeting);
        rvAppointment = (RecyclerView) activitiesView.findViewById(R.id.rvAppointment);
        rvMeeting = (RecyclerView) activitiesView.findViewById(R.id.rvMeeting);
        appointmentList = new ArrayList<>();


        appointmentAdapter = new AppointmentAdapter(appointmentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvAppointment.setLayoutManager(mLayoutManager);
        rvAppointment.setAdapter(appointmentAdapter);

        meetingList = new ArrayList<>();


        tvAppointment.setOnClickListener(this);
        tvMeeting.setOnClickListener(this);
        setIcon(llAppointment, tvAppointment);
        setIcon(llMeeting, tvMeeting);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && activitiesView != null && viewPager.getCurrentItem() == 1) {
                appointments();
                meeting();
            }
        } catch (Exception e) {
            e.printStackTrace();
        /*    Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            Crashlytics.logException(e);*/
        }
    }

    private void meeting() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
//            customProgress.setVisibility(View.VISIBLE);
            /*{"LeadID":6028,"PageSize":10,"Page":1,"CalculatedPage":0,"TotalCount":0}*/
            Map inputMap = new HashMap<>();
            // inputMap.put("LeadID", leadId);
            inputMap.put("LeadID", leadId);
            inputMap.put("PageSize", "10");
            inputMap.put("Page", "1");
            inputMap.put("CalculatedPage", "0");
            inputMap.put("TotalCount", "0");

            String serviceMethod = "Meeting/GetMeeting";

            try {
                CRMWebServiceHandler.postWithRequestAndFilterObject(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
                                    meetingList = new ArrayList<>();

                                    JSONObject jsonObject = response.getJSONObject(RESPONSE);
                                    JSONArray dataArray = jsonObject.getJSONArray(DATA);
                                   /* "Status": 1,
                                            "MeetingID": 2004,
                                            "LeadID": 6028,
                                            "Name": "MS Dhoni",
                                            "EMAIL": "msdhoni@gmail.com",
                                            "MOBNO": "8798798797",
                                            "DATE": "2012-12-22",
                                            "TIME": "09:55",
                                            "AttendeeEmp": null,
                                            "Product": "Offline",
                                            "StatusName": "Wrong registration - Survey",
                                            "ActivityType": "Personal Meeting",
                                            "MeetingReason": "Introduction and Customer Profiling",
                                            "MeetingDuration": "15 min",
                                            "Category": null,
                                            "Actionable": null,
                                            "Comments": "Tis si si",
                                            "Stage": "",
                                            "Callback": "",
                                            "QueryDesc": "",*/


                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject objDetail = dataArray.getJSONObject(i);
                                        String Status = (objDetail.isNull(STATUS) || objDetail.getString(STATUS).equals("")) ? "" : objDetail.getString(STATUS);
                                        String MeetingID = (objDetail.isNull("MeetingID") || objDetail.getString("MeetingID").equals("")) ? "" : objDetail.getString("MeetingID");
                                        String LeadID = (objDetail.isNull("LeadID") || objDetail.getString("LeadID").equals("")) ? "" : objDetail.getString("LeadID");
                                        String Name = (objDetail.isNull(NAME) || objDetail.getString(NAME).equals("")) ? "" : objDetail.getString(NAME);
                                        String EMAIL = (objDetail.isNull("EMAIL") || objDetail.getString("EMAIL").equals("")) ? "" : objDetail.getString("EMAIL");
                                        String mobNo = (objDetail.isNull(MOBNO) || objDetail.getString(MOBNO).equals("")) ? "" : objDetail.getString(MOBNO);
                                        String date = (objDetail.isNull("DATE") || objDetail.getString("DATE").equals("")) ? "" : objDetail.getString("DATE");
                                        String time = (objDetail.isNull("TIME") || objDetail.getString("TIME").equals("")) ? "" : objDetail.getString("TIME");
                                        String AttendeeEmp = (objDetail.isNull("AttendeeEmp") || objDetail.getString("AttendeeEmp").equals("")) ? "" : objDetail.getString("AttendeeEmp");
                                        String Product = (objDetail.isNull("Product") || objDetail.getString("Product").equals("")) ? "" : objDetail.getString("Product");
                                        String StatusName = (objDetail.isNull("StatusName") || objDetail.getString("StatusName").equals("")) ? "" : objDetail.getString("StatusName");
                                        String ActivityType = (objDetail.isNull("ActivityType") || objDetail.getString("ActivityType").equals("")) ? "" : objDetail.getString("ActivityType");
                                        String MeetingReason = (objDetail.isNull("MeetingReason") || objDetail.getString("MeetingReason").equals("")) ? "" : objDetail.getString("MeetingReason");
                                        String MeetingDuration = (objDetail.isNull("MeetingDuration") || objDetail.getString("MeetingDuration").equals("")) ? "" : objDetail.getString("MeetingDuration");
                                        String Category = (objDetail.isNull("Category") || objDetail.getString("Category").equals("")) ? "" : objDetail.getString("Category");
                                        String Actionable = (objDetail.isNull("Actionable") || objDetail.getString("Actionable").equals("")) ? "" : objDetail.getString("Actionable");
                                        String Comments = (objDetail.isNull("Comments") || objDetail.getString("Comments").equals("")) ? "" : objDetail.getString("Comments");
                                        String Stage = (objDetail.isNull("Stage") || objDetail.getString("Stage").equals("")) ? "" : objDetail.getString("Stage");
                                        String CSR = (objDetail.isNull("CustomerServiceRemark") || objDetail.getString("CustomerServiceRemark").equals("")) ? "" : objDetail.getString("CustomerServiceRemark");
                                        String QueryDesc = (objDetail.isNull("QueryDesc") || objDetail.getString("QueryDesc").equals("")) ? "" : objDetail.getString("QueryDesc");


                                        meetingList.add(new LeadActivitiesMeetingModel(Status, MeetingID, LeadID, Name, EMAIL, mobNo, CRMMOSLCommon.formatMonth(date), time, AttendeeEmp
                                                , Product, StatusName, ActivityType, MeetingReason, MeetingDuration, Category, Actionable, Comments, Stage, CSR, QueryDesc));
                                    }
                                    meetingAdapter = new MeetingAdapter(meetingList);
                                    RecyclerView.LayoutManager meetingLayoutManager = new LinearLayoutManager(getActivity());
                                    rvMeeting.setLayoutManager(meetingLayoutManager);
                                    rvMeeting.setAdapter(meetingAdapter);
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            //eat it
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void appointments() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
//            customProgress.setVisibility(View.VISIBLE);
            /*{"LeadID":6028,"PageSize":10,"Page":1,"CalculatedPage":0,"TotalCount":0}*/
            Map inputMap = new HashMap<>();
            // inputMap.put("LeadID", leadId);
            inputMap.put("LeadID", leadId);
            inputMap.put("PageSize", "10");
            inputMap.put("Page", "1");
            inputMap.put("CalculatedPage", "0");
            inputMap.put("TotalCount", "0");

            String serviceMethod = "Meeting/GetAppointment";

            try {
                CRMWebServiceHandler.postWithRequestAndFilterObject(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
                                    appointmentList = new ArrayList<>();

                                    JSONObject jsonObject = response.getJSONObject(RESPONSE);
                                    JSONArray dataArray = jsonObject.getJSONArray(DATA);
                                   /* "Status": 1,
                                            "MeetingID": 2004,
                                            "LeadID": 6028,
                                            "Name": "MS Dhoni",
                                            "EMAIL": "msdhoni@gmail.com",
                                            "MOBNO": "8798798797",
                                            "DATE": "2012-12-22",
                                            "TIME": "09:55",
                                            "AttendeeEmp": null,
                                            "Product": "Offline",
                                            "StatusName": "Wrong registration - Survey",
                                            "ActivityType": "Personal Meeting",
                                            "MeetingReason": "Introduction and Customer Profiling",
                                            "MeetingDuration": "15 min",
                                            "Category": null,
                                            "Actionable": null,
                                            "Comments": "Tis si si",
                                            "Stage": "",
                                            "Callback": "",
                                            "QueryDesc": "",*/


                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject objDetail = dataArray.getJSONObject(i);
                                        String Status = (objDetail.isNull(STATUS) || objDetail.getString(STATUS).equals("")) ? "" : objDetail.getString(STATUS);
                                        String MeetingID = (objDetail.isNull("MeetingID") || objDetail.getString("MeetingID").equals("")) ? "" : objDetail.getString("MeetingID");
                                        String LeadID = (objDetail.isNull("LeadID") || objDetail.getString("LeadID").equals("")) ? "" : objDetail.getString("LeadID");
                                        String Name = (objDetail.isNull(NAME) || objDetail.getString(NAME).equals("")) ? "" : objDetail.getString(NAME);
                                        String EMAIL = (objDetail.isNull("EMAIL") || objDetail.getString("EMAIL").equals("")) ? "" : objDetail.getString("EMAIL");
                                        String mobNo = (objDetail.isNull(MOBNO) || objDetail.getString(MOBNO).equals("")) ? "" : objDetail.getString(MOBNO);
                                        String date = (objDetail.isNull("DATE") || objDetail.getString("DATE").equals("")) ? "" : objDetail.getString("DATE");
                                        String time = (objDetail.isNull("TIME") || objDetail.getString("TIME").equals("")) ? "" : objDetail.getString("TIME");
                                        String AttendeeEmp = (objDetail.isNull("AttendeeEmp") || objDetail.getString("AttendeeEmp").equals("")) ? "" : objDetail.getString("AttendeeEmp");
                                        String Product = (objDetail.isNull("Product") || objDetail.getString("Product").equals("")) ? "" : objDetail.getString("Product");
                                        String StatusName = (objDetail.isNull("StatusName") || objDetail.getString("StatusName").equals("")) ? "" : objDetail.getString("StatusName");
                                        String ActivityType = (objDetail.isNull("ActivityType") || objDetail.getString("ActivityType").equals("")) ? "" : objDetail.getString("ActivityType");
                                        String MeetingReason = (objDetail.isNull("MeetingReason") || objDetail.getString("MeetingReason").equals("")) ? "" : objDetail.getString("MeetingReason");
                                        String MeetingDuration = (objDetail.isNull("MeetingDuration") || objDetail.getString("MeetingDuration").equals("")) ? "" : objDetail.getString("MeetingDuration");
                                        String Category = (objDetail.isNull("Category") || objDetail.getString("Category").equals("")) ? "" : objDetail.getString("Category");
                                        String Actionable = (objDetail.isNull("Actionable") || objDetail.getString("Actionable").equals("")) ? "" : objDetail.getString("Actionable");
                                        String Comments = (objDetail.isNull("Comments") || objDetail.getString("Comments").equals("")) ? "" : objDetail.getString("Comments");
                                        String Stage = (objDetail.isNull("Stage") || objDetail.getString("Stage").equals("")) ? "" : objDetail.getString("Stage");
                                        String CSR = (objDetail.isNull("CustomerServiceRemark") || objDetail.getString("CustomerServiceRemark").equals("")) ? "" : objDetail.getString("CustomerServiceRemark");
                                        String QueryDesc = (objDetail.isNull("QueryDesc") || objDetail.getString("QueryDesc").equals("")) ? "" : objDetail.getString("QueryDesc");


                                        appointmentList.add(new LeadActivitiesAppointModel(Status, MeetingID, LeadID, Name, EMAIL, mobNo, CRMMOSLCommon.formatMonth(date), time, AttendeeEmp
                                                , Product, StatusName, ActivityType, MeetingReason, MeetingDuration, Category, Actionable, Comments, Stage, CSR, QueryDesc));
                                    }
                                    appointmentAdapter = new AppointmentAdapter(appointmentList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    rvAppointment.setLayoutManager(mLayoutManager);
                                    rvAppointment.setAdapter(appointmentAdapter);
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            //eat it
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvAppointment:
                toggleSectionExpansion(llAppointment, tvAppointment);
                break;
            case R.id.tvMeeting:
                toggleSectionExpansion(llMeeting, tvMeeting);
                break;
        }

    }

    public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder1> {

        private List<LeadActivitiesAppointModel> appointmentList;

        public class MyViewHolder1 extends RecyclerView.ViewHolder {
            public TextView tvMonthName, tvMonthDate, tvMonthYear, tvTimeRange, tvAttFirstName, tvAttSecondName, tvAttThirdName;
            public ImageView ivRightArrow;
            RelativeLayout relativeLayout;

            public MyViewHolder1(View view) {
                super(view);
                tvMonthName = (TextView) view.findViewById(R.id.tvMonthName);
                tvMonthDate = (TextView) view.findViewById(R.id.tvMonthDate);
                tvMonthYear = (TextView) view.findViewById(R.id.tvMonthYear);
                tvTimeRange = (TextView) view.findViewById(R.id.tvTimeRange);
                tvAttFirstName = (TextView) view.findViewById(R.id.tvAttFirstName);
                tvAttSecondName = (TextView) view.findViewById(R.id.tvAttSecondName);
                tvAttThirdName = (TextView) view.findViewById(R.id.tvAttThirdName);
                ivRightArrow = (ImageView) view.findViewById(R.id.ivRightArrow);
                relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            }
        }


        public AppointmentAdapter(List<LeadActivitiesAppointModel> appointmentList) {
            this.appointmentList = appointmentList;
        }

        @Override
        public AppointmentAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_appointment, parent, false);

            return new AppointmentAdapter.MyViewHolder1(itemView);
        }

        @Override
        public void onBindViewHolder(AppointmentAdapter.MyViewHolder1 holder, int position) {
            final LeadActivitiesAppointModel appointmentModel = appointmentList.get(position);
            String metadataArray[] = appointmentModel.getDate().toString().split(" ");
            holder.tvMonthName.setText(metadataArray[1]);
            holder.tvMonthDate.setText(metadataArray[0]);
            holder.tvMonthYear.setText(metadataArray[2]);
            holder.tvTimeRange.setText(appointmentModel.getTime());
            holder.tvAttFirstName.setText(appointmentModel.getAttendeeEmp());
           /* if (appointmentModel.getAttendeeSecondName().isEmpty()) {
                holder.tvAttSecondName.setText(appointmentModel.getAttendeeSecondName());
            } else {
                holder.tvAttSecondName.setText(", " + appointmentModel.getAttendeeSecondName());
            }
            if (appointmentModel.getAttendeeThirdName().isEmpty()) {
                holder.tvAttThirdName.setText(appointmentModel.getAttendeeThirdName());
            } else {
                holder.tvAttThirdName.setText(", " + appointmentModel.getAttendeeThirdName());
            }*/
            holder.ivRightArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString(STATUS, appointmentModel.getStatus());
                    args.putString(MEETING, appointmentModel.getMeetingid());
                    args.putString(LEAD_ID, appointmentModel.getLeadid());
                    args.putString(NAME, appointmentModel.getName());
                    args.putString(EMAIL, appointmentModel.getEmail());
                    args.putString(MOBNO, appointmentModel.getMobNo());
                    args.putString(DATE, appointmentModel.getDate());
                    args.putString(TIME, appointmentModel.getTime());
                    args.putString("AttendeeEmp", appointmentModel.getAttendeeEmp());
                    args.putString(PRODUCT, appointmentModel.getProduct());
                    args.putString("StatusName", appointmentModel.getStatusName());
                    args.putString("ActivityType", appointmentModel.getActivityType());
                    args.putString("MeetingReason", appointmentModel.getMeetingReason());
                    args.putString("MeetingDuration", appointmentModel.getMeetingDuration());
                    args.putString("Category", appointmentModel.getCategory());
                    args.putString("Actionable", appointmentModel.getActionable());
                    args.putString("Comments", appointmentModel.getComments());
                    args.putString("Stage", appointmentModel.getStage());
                    args.putString("CSR", appointmentModel.getCallback());
                    args.putString("QueryDesc", appointmentModel.getQueryDesc());
                    navigateTo(NAV_TO_UPDATE_MEETING, args, getFragmentManager(), true);
                }
            });

        }

        @Override
        public int getItemCount() {
            return appointmentList.size();
        }
    }


    public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder1> {

        private List<LeadActivitiesMeetingModel> meetingList;

        public class MyViewHolder1 extends RecyclerView.ViewHolder {
            public TextView tvMonthName, tvMonthDate, tvMonthYear, tvAppointmentType, tvTimeRange, tvAttFirstName, tvAttSecondName, tvAttThirdName;
            public ImageView ivRightArrow;
            public RelativeLayout relativeLayout;

            public MyViewHolder1(View view) {
                super(view);
                tvMonthName = (TextView) view.findViewById(R.id.tvMonthName);
                tvMonthDate = (TextView) view.findViewById(R.id.tvMonthDate);
                tvMonthYear = (TextView) view.findViewById(R.id.tvMonthYear);
                tvAppointmentType = (TextView) view.findViewById(R.id.tvAppointmentType);
                tvTimeRange = (TextView) view.findViewById(R.id.tvTimeRange);
                tvAttFirstName = (TextView) view.findViewById(R.id.tvAttFirstName);
                tvAttSecondName = (TextView) view.findViewById(R.id.tvAttSecondName);
                tvAttThirdName = (TextView) view.findViewById(R.id.tvAttThirdName);
                ivRightArrow = (ImageView) view.findViewById(R.id.ivRightArrow);
                relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            }
        }


        public MeetingAdapter(List<LeadActivitiesMeetingModel> meetingList) {
            this.meetingList = meetingList;
        }

        @Override
        public MeetingAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_appointment, parent, false);

            return new MeetingAdapter.MyViewHolder1(itemView);
        }

        @Override
        public void onBindViewHolder(MeetingAdapter.MyViewHolder1 holder, int position) {
            final LeadActivitiesMeetingModel meetingModel = meetingList.get(position);
            String metadataArray[] = meetingModel.getDate().toString().split(" ");
            holder.tvMonthName.setText(metadataArray[1]);
            holder.tvMonthDate.setText(metadataArray[0]);
            holder.tvMonthYear.setText(metadataArray[2]);
            holder.tvAppointmentType.setText(meetingModel.getMeetingReason());
            holder.tvTimeRange.setText(meetingModel.getTime());
            holder.tvAttFirstName.setText(meetingModel.getAttendeeEmp());
           /* if (meetingModel.getAttendeeSecondName().isEmpty()) {
                holder.tvAttSecondName.setText(meetingModel.getAttendeeSecondName());
            } else {
                holder.tvAttSecondName.setText(", " + meetingModel.getAttendeeSecondName());
            }
            if (meetingModel.getAttendeeThirdName().isEmpty()) {
                holder.tvAttThirdName.setText(meetingModel.getAttendeeThirdName());
            } else {
                holder.tvAttThirdName.setText(", " + meetingModel.getAttendeeThirdName());
            }*/
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString(STATUS, meetingModel.getStatus());
                    args.putString(MEETING, meetingModel.getMeetingid());
                    args.putString(LEAD_ID, meetingModel.getLeadid());
                    args.putString(NAME, meetingModel.getName());
                    args.putString(EMAIL, meetingModel.getEmail());
                    args.putString(MOBNO, meetingModel.getMobNo());
                    args.putString(DATE, meetingModel.getDate());
                    args.putString(TIME, meetingModel.getTime());
                    args.putString("AttendeeEmp", meetingModel.getAttendeeEmp());
                    args.putString(PRODUCT, meetingModel.getProduct());
                    args.putString("StatusName", meetingModel.getStatusName());
                    args.putString("ActivityType", meetingModel.getActivityType());
                    args.putString("MeetingReason", meetingModel.getMeetingReason());
                    args.putString("MeetingDuration", meetingModel.getMeetingDuration());
                    args.putString("Category", meetingModel.getCategory());
                    args.putString("Actionable", meetingModel.getActionable());
                    args.putString("Comments", meetingModel.getComments());
                    args.putString("Stage", meetingModel.getStage());
                    args.putString("CSR", meetingModel.getCallback());
                    args.putString("QueryDesc", meetingModel.getQueryDesc());
                    navigateTo(NAV_TO_LEAD_ACTIVITIES_DETAIL, args, getFragmentManager(), true);
                }
            });

        }

        @Override
        public int getItemCount() {
            return meetingList.size();
        }
    }


}

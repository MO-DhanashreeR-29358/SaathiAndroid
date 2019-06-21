package com.mosl.tabapp.leadViewDetail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mosl.app.core.data.UiModel;
import com.mosl.app.core.data.UiModelHelper;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.ui.CustomDialog;
import com.mosl.saathi.api.model.leadviewactivities.AppointmentRequest;
import com.mosl.saathi.api.model.leadviewactivities.AppointmentResponse;
import com.mosl.saathi.api.model.leadviewactivities.DeleteAppointmentRequest;
import com.mosl.saathi.api.model.leadviewactivities.Filter;
import com.mosl.saathi.api.model.leadviewactivities.MeetingRequest;
import com.mosl.saathi.api.model.leadviewactivities.MeetingResponse;
import com.mosl.saathi.api.model.leadviewactivities.Request;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.LeadActivitiesAppointModel;
import com.mosl.tabapp.model.LeadActivitiesMeetingModel;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import io.reactivex.disposables.Disposable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 3/14/2017.
 */

public class LeadViewActivitiesFragment extends MOSLCommonFragment implements View.OnClickListener {
    private View activitiesView;
    private TextView tvAppointment, tvMeeting, tvNoAppointment, tvNoMeeting;
    private LinearLayout llAppointment, llMeeting;
    private RecyclerView rvAppointment, rvMeeting;
    private ArrayList<LeadActivitiesAppointModel> appointmentList = new ArrayList<>();
    private ArrayList<LeadActivitiesMeetingModel> meetingList = new ArrayList<>();
    private AppointmentAdapter appointmentAdapter;
    private MeetingAdapter meetingAdapter;
    private static String leadId;
    Context mContext;
    ViewPager viewPager;
    private static String mUcid;

    public static LeadViewActivitiesFragment newInstance(int position, String type, String leadID,
        String ucid) {
        LeadViewActivitiesFragment fragment = new LeadViewActivitiesFragment();
        leadId = leadID;
        mUcid = ucid;
        Bundle args = new Bundle();
        args.putInt("Position", position);
        args.putString("Type", type);
        args.putString("LeadID", leadID);
        args.putString(UCID, ucid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        activitiesView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_lead_activities);
        setUpView(activitiesView);
        setUserVisibleHint(true);
        return activitiesView;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    private void setUpView(View activitiesView) {
        tvAppointment = (TextView) activitiesView.findViewById(R.id.tvAppointment);
        tvMeeting = (TextView) activitiesView.findViewById(R.id.tvMeeting);
        tvNoMeeting = (TextView) activitiesView.findViewById(R.id.tvNoMeeting);
        tvNoAppointment = (TextView) activitiesView.findViewById(R.id.tvNoAppointment);
        llAppointment = (LinearLayout) activitiesView.findViewById(R.id.llAppointment);
        llMeeting = (LinearLayout) activitiesView.findViewById(R.id.llMeeting);
        rvAppointment = (RecyclerView) activitiesView.findViewById(R.id.rvAppointment);
        rvAppointment.setNestedScrollingEnabled(false);
        rvMeeting = (RecyclerView) activitiesView.findViewById(R.id.rvMeeting);
        rvMeeting.setNestedScrollingEnabled(false);

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
            }
        } catch (Exception e) {
            e.printStackTrace();
        /*    Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            Crashlytics.logException(e);*/
        }
    }

    private void meeting() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            MeetingRequest request =
                new MeetingRequest(new Request(leadId, REPORT_PAGE_SIZE, "0", "0", "0", mUcid),
                    new Filter(null, null, null, null, null, null, null), true);
            Disposable subscribe = saathiApi.getMeetings(request)
                .compose(UiModelHelper.applyUiModel())
                .subscribe(this::renderMeetings);
        } else {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void renderMeetings(UiModel<List<MeetingResponse>> uiModel) throws ParseException {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
            tvNoMeeting.setVisibility(View.VISIBLE);
        }
        List<MeetingResponse> data = uiModel.data;
        if (data != null) {
            meetingList.clear();
            for (int i = 0; i < data.size(); i++) {
                MeetingResponse item = data.get(i);
                String status = stringOrEmpty(item.status);
                String meetingId = stringOrEmpty(item.meetingID);
                String leadId = stringOrEmpty(item.leadID);
                String name = stringOrEmpty(item.name);
                String email = stringOrEmpty(item.eMAIL);
                String mobNo = stringOrEmpty(item.mOBNO);
                String date = stringOrEmpty(item.dATE);
                String time = stringOrEmpty(item.tIME);
                String attendeeEmp = stringOrEmpty(item.attendeeEmp);
                String product = stringOrEmpty(item.product);
                String statusName = stringOrEmpty(item.statusName);
                String activityType = stringOrEmpty(item.activityType);
                String meetingReason = stringOrEmpty(item.meetingReason);
                String meetingDuration = stringOrEmpty(item.meetingDuration);
                String category = stringOrEmpty(item.category);
                String actionable = stringOrEmpty(item.actionable);
                String comments = stringOrEmpty(item.comments);
                String stage = stringOrEmpty(item.stage);
                String customerServiceRemark = stringOrEmpty(item.customerServiceRemark);
                String queryDesc = stringOrEmpty(item.queryDesc);
                String meetingAddress = stringOrEmpty(item.meetingAddress);
                String pincode = stringOrEmpty(item.pincode);
                String travellingMode = stringOrEmpty(item.travellingMode);
                String amount = stringOrEmpty(item.amount);
                String location = stringOrEmpty(item.meetingAddressType);
                String geoMeetingLocation = stringOrEmpty(item.location);

                meetingList.add(
                    new LeadActivitiesMeetingModel(status, meetingId, leadId, name, email, mobNo,
                        CRMMOSLCommon.formatMonth(date), time, attendeeEmp
                        , product, statusName, activityType, meetingReason, meetingDuration,
                        category, actionable, comments, stage, customerServiceRemark, queryDesc,
                        meetingAddress, pincode, travellingMode, amount, "", location,
                        geoMeetingLocation));
            }
            meetingAdapter = new MeetingAdapter(meetingList);
            RecyclerView.LayoutManager meetingLayoutManager =
                new LinearLayoutManager(getActivity());
            rvMeeting.setLayoutManager(meetingLayoutManager);
            rvMeeting.setAdapter(meetingAdapter);
            tvNoMeeting.setVisibility(View.GONE);
            if (data.isEmpty()) {
                tvNoMeeting.setVisibility(View.VISIBLE);
            }
        }
    }

    private void appointments() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            AppointmentRequest request =
                new AppointmentRequest(new Request(leadId, REPORT_PAGE_SIZE, "1", "0", "0", mUcid),
                    new Filter(null, null, null, null, null, null, null), true);
            Disposable subscribe = saathiApi.getAppointments(request)
                .compose(UiModelHelper.applyUiModel())
                .subscribe(this::renderAppointments);
        } else {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void renderAppointments(UiModel<List<AppointmentResponse>> uiModel)
        throws ParseException {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
            tvNoMeeting.setVisibility(View.VISIBLE);
        }
        List<AppointmentResponse> data = uiModel.data;
        if (data != null) {
            meeting();
            appointmentList.clear();
            for (int i = 0; i < data.size(); i++) {
                AppointmentResponse item = data.get(i);

                String status = stringOrEmpty(item.status);
                String meetingId = stringOrEmpty(item.meetingID);
                String leadId = stringOrEmpty(item.leadID);
                String name = stringOrEmpty(item.name);
                String email = stringOrEmpty(item.eMAIL);
                String mobNo = stringOrEmpty(item.mOBNO);
                String date = stringOrEmpty(item.dATE);
                String time = stringOrEmpty(item.tIME);
                String attendeeEmp = stringOrEmpty(item.attendeeEmp);
                String product = stringOrEmpty(item.product);
                String statusName = stringOrEmpty(item.statusName);
                String activityType = stringOrEmpty(item.activityType);
                String ucid = stringOrEmpty(item.ucid);
                String createdBy = stringOrEmpty(item.createdBy);
                String repeatOnCode = stringOrEmpty(item.repeatOnCode);

                appointmentList.add(
                    new LeadActivitiesAppointModel(createdBy,
                        status, meetingId, leadId, name, email,
                        mobNo, CRMMOSLCommon.formatMonth(date),
                        time, attendeeEmp
                        , product, statusName, activityType, ucid,
                        repeatOnCode));
            }
            appointmentAdapter =
                new AppointmentAdapter(appointmentList);
            RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity());
            rvAppointment.setLayoutManager(mLayoutManager);
            rvAppointment.setAdapter(appointmentAdapter);
            tvNoAppointment.setVisibility(View.GONE);
            if (data.isEmpty()) {
                tvNoAppointment.setVisibility(View.VISIBLE);
            }
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

    private void leadViewDialog(final LeadActivitiesAppointModel appointmentModel) {
        final Dialog dialog = new Dialog(getMainActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_appointments);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ImageView ivRescheduleMeeting = (ImageView) dialog.findViewById(R.id.ivRescheduleMeeting);
        ImageView ivUpdateMeeting = (ImageView) dialog.findViewById(R.id.ivUpdateMeeting);
        ImageView ivCancelMeeting = (ImageView) dialog.findViewById(R.id.ivCancelMeeting);

        String dateStr = appointmentModel.getDate(); //07/24/2017
        String timeStr = appointmentModel.getTime(); //4:05PM
        Date apptDate = CRMAppUtil.getDate(dateStr + " " + timeStr, "dd MMM yyyy h:mma");

        if (CRMAppUtil.isBeforeDate(apptDate)) {
            ivUpdateMeeting.setVisibility(
                View.VISIBLE);//updaet meeting allowed only when appointment date is less than current date
        } else {
            ivUpdateMeeting.setVisibility(View.GONE);
        }

        ivRescheduleMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(LEAD_ID, appointmentModel.getLeadid());
                bundle.putString(MEETING_ID, appointmentModel.getMeetingid());
                bundle.putString(UCID, mUcid);
                navigateTo(NAV_TO_SET_APPOINTMENT, bundle, getFragmentManager(), true);
                dialog.dismiss();
            }
        });

        ivCancelMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CRMAppUtil.isBlank(appointmentModel.getRepeatOnCode())) {
                    CustomDialog addSelDialog = new CustomDialog(getContext(),
                        "Are you sure you want to cancel this appointment ?", true, true, true,
                        "Cancel Appointment", YES, NO, new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(MOSLCommon.Action action, Object... data) {
                            if (action == MOSLCommon.Action.OK) {
                                deleteAppintments(appointmentModel.getMeetingid(),
                                    "DeactivateMeeting");
                            }
                        }
                    });
                    dialog.dismiss();
                    addSelDialog.show();
                } else {
                    CustomDialog addSelDialog = new CustomDialog(getContext(),
                        "Are you sure you want to cancel this appointment ?", true, true, true,
                        "Cancel Appointment", "Current", "All",
                        new CustomDialog.CustomDialogListener() {
                            @Override
                            public void dialogAction(MOSLCommon.Action action, Object... data) {
                                if (action == MOSLCommon.Action.OK) {
                                    deleteAppintments(appointmentModel.getMeetingid(),
                                        "DeactivateMeeting");
                                } else if (action == MOSLCommon.Action.NO) {
                                    deleteAppintments(appointmentModel.getMeetingid(),
                                        "DeactivateALLMeeting");
                                }
                            }
                        });
                    dialog.dismiss();
                    addSelDialog.show();
                }

                //deleteAppintments(appointmentModel.getMeetingid());
                //dialog.dismiss();
            }
        });

        ivUpdateMeeting.setOnClickListener(new View.OnClickListener() {
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
                args.putString(UCID, mUcid);
                  /*  args.putString("MeetingReason", appointmentModel.getMeetingReason());
                    args.putString("MeetingDuration", appointmentModel.getMeetingDuration());
                    args.putString("Category", appointmentModel.getCategory());
                    args.putString("Actionable", appointmentModel.getActionable());
                    args.putString("Comments", appointmentModel.getComments());
                    args.putString("Stage", appointmentModel.getStage());
                    args.putString("CSR", appointmentModel.getCallback());
                    args.putString("QueryDesc", appointmentModel.getQueryDesc());*/
                navigateTo(NAV_TO_UPDATE_MEETING, args, getFragmentManager(), true);
                dialog.dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void deleteAppintments(final String meetingId, String url) {
        //login parameters
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            DeleteAppointmentRequest request =
                new DeleteAppointmentRequest(meetingId);
            Disposable subscribe = saathiApi.deleteAppointments(url, request)
                .compose(UiModelHelper.applyUiModel())
                .subscribe(uiModel -> {
                    renderDeleteAppointments(uiModel, meetingId);
                });
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void renderDeleteAppointments(UiModel<String> uiModel, String meetingId) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        String data = uiModel.data;
        if (data != null) {
            appointmentAdapter.deleteAppointment(meetingId);
            alertDialog(getActivity(), 0,
                getResources().getString(R.string.motilal_oswal),
                APPT_CANCELLED, "OK", true,
                (action, data1) -> {
                    if (action == MSFDialog.Action.OK) {

                    }
                });
        }
    }

    public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder1> {

        private List<LeadActivitiesAppointModel> appointmentList;

        public AppointmentAdapter(List<LeadActivitiesAppointModel> appointmentList) {
            this.appointmentList = appointmentList;
        }

        @Override
        public AppointmentAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_meetings, parent, false);

            return new MyViewHolder1(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder1 holder, int position) {
            final LeadActivitiesAppointModel appointmentModel = appointmentList.get(position);
            String metadataArray[] = appointmentModel.getDate().toString().split(" ");
            holder.tvMonthName.setText(metadataArray[1]);
            holder.tvMonthDate.setText(metadataArray[0]);
            holder.tvMonthYear.setText(metadataArray[2]);
            holder.tvTimeRange.setText(appointmentModel.getTime());
            holder.tvAttFirstName.setText(appointmentModel.getAttendeeEmp());
            holder.tvAppointmentType.setText("Appointment for " + appointmentModel.getProduct());
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AccountDetails.getEmployeeCode()
                        .equalsIgnoreCase(appointmentModel.getCreatedBy())) {
                        leadViewDialog(appointmentModel);
                    }
                }
            });
        }

        public LeadActivitiesAppointModel getDocumentById(String meetingId) {
            for (LeadActivitiesAppointModel model : appointmentList) {
                if (model.getMeetingid().equals(meetingId)) {
                    return model;
                }
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return appointmentList.size();
        }

        public void deleteAppointment(String meetingId) {
            LeadActivitiesAppointModel model = getDocumentById(meetingId);
            appointmentList.remove(model);
            notifyDataSetChanged();
            if (appointmentList.size() == 0) {
                tvNoAppointment.setVisibility(View.VISIBLE);
            }
        }

        public class MyViewHolder1 extends RecyclerView.ViewHolder {
            public TextView tvAppointmentType, tvMonthName, tvMonthDate, tvMonthYear, tvTimeRange,
                tvAttFirstName, tvAttSecondName, tvAttThirdName;
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
                tvAppointmentType = (TextView) view.findViewById(R.id.tvAppointmentType);
            }
        }
    }

    public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder1> {

        private List<LeadActivitiesMeetingModel> meetingList;

        public MeetingAdapter(List<LeadActivitiesMeetingModel> meetingList) {
            this.meetingList = meetingList;
        }

        @Override
        public MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_meetings, parent, false);

            return new MyViewHolder1(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder1 holder, int position) {
            final LeadActivitiesMeetingModel meetingModel = meetingList.get(position);
            String metadataArray[] = meetingModel.getDate().toString().split(" ");
            holder.tvMonthName.setText(metadataArray[1]);
            holder.tvMonthDate.setText(metadataArray[0]);
            holder.tvMonthYear.setText(metadataArray[2]);
            holder.tvAppointmentType.setText(meetingModel.getMeetingReason());
            holder.tvTimeRange.setText(meetingModel.getTime());
            holder.tvAttFirstName.setText(meetingModel.getAttendeeEmp());
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
                    args.putString("Location", meetingModel.getLocation());
                    args.putString("MeetingAddress", meetingModel.getMeetingAddress());
                    args.putString("Pincode", meetingModel.getPincode());
                    args.putString("TravellingMode", meetingModel.getTravellingMode());
                    args.putString("Amount", meetingModel.getAmount());
                    //                    args.putString("GeoTagStatus", meetingModel.getGeoTagStatus());
                    args.putString("GeoMeetingLocation", meetingModel.getGeoMeetingLocation());
                    navigateTo(NAV_TO_LEAD_ACTIVITIES_DETAIL, args, getFragmentManager(), true);
                }
            });
        }

        public class MyViewHolder1 extends RecyclerView.ViewHolder {
            public TextView tvMonthName, tvMonthDate, tvMonthYear, tvAppointmentType, tvTimeRange,
                tvAttFirstName, tvAttSecondName, tvAttThirdName;
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

        @Override
        public int getItemCount() {
            return meetingList.size();
        }
    }
}

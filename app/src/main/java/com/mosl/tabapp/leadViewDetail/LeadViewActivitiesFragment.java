package com.mosl.tabapp.leadViewDetail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.mosl.commons.data.UiModel;
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
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.LeadActivitiesAppointModel;
import com.mosl.tabapp.model.LeadActivitiesMeetingModel;
import com.mosl.tabapp.navigation.ScreenNavigatorImpl;
import com.mosl.tabapp.ui.CustomDialog;
import com.mosl.tabapp.ui.MSFDialog;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.constants.MOSLConstants.APPT_CANCELLED;
import static com.mosl.tabapp.constants.MOSLConstants.DATE;
import static com.mosl.tabapp.constants.MOSLConstants.EMAIL;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD_ID;
import static com.mosl.tabapp.constants.MOSLConstants.MEETING;
import static com.mosl.tabapp.constants.MOSLConstants.MEETING_ID;
import static com.mosl.tabapp.constants.MOSLConstants.MOBNO;
import static com.mosl.tabapp.constants.MOSLConstants.NAME;
import static com.mosl.tabapp.constants.MOSLConstants.NO;
import static com.mosl.tabapp.constants.MOSLConstants.PRODUCT;
import static com.mosl.tabapp.constants.MOSLConstants.REPORT_PAGE_SIZE;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static com.mosl.tabapp.constants.MOSLConstants.TIME;
import static com.mosl.tabapp.constants.MOSLConstants.UCID;
import static com.mosl.tabapp.constants.MOSLConstants.YES;
import static com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_LEAD_ACTIVITIES_DETAILS;
import static com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_SET_APPOINTMENT;
import static com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_UPDATE_MEETING;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class LeadViewActivitiesFragment extends CommonFragment implements View.OnClickListener {
	private static String leadId;
	private static String mUcid;
	Context mContext;
	@Inject ScreenNavigatorImpl screenNavigator;
	private View activitiesView;
	private TextView tvAppointment, tvMeeting, tvNoAppointment, tvNoMeeting;
	private LinearLayout llAppointment, llMeeting;
	private RecyclerView rvAppointment, rvMeeting;
	private ArrayList<LeadActivitiesAppointModel> appointmentList = new ArrayList<>();
	private ArrayList<LeadActivitiesMeetingModel> meetingList = new ArrayList<>();
	private AppointmentAdapter appointmentAdapter;
	private ViewPager viewPager;

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

	@Override protected int getLayout() {
		return R.layout.frag_lead_activities;
	}

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		activitiesView = super.onCreateView(inflater, container, savedInstanceState);
		//attachLayout(R.layout.frag_lead_activities);
		setUpView(Objects.requireNonNull(activitiesView));
		setUserVisibleHint(true);
		return activitiesView;
	}

	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
	}

	private void setUpView(View activitiesView) {
		tvAppointment = activitiesView.findViewById(R.id.tvAppointment);
		tvMeeting = activitiesView.findViewById(R.id.tvMeeting);
		tvNoMeeting = activitiesView.findViewById(R.id.tvNoMeeting);
		tvNoAppointment = activitiesView.findViewById(R.id.tvNoAppointment);
		llAppointment = activitiesView.findViewById(R.id.llAppointment);
		llMeeting = activitiesView.findViewById(R.id.llMeeting);
		rvAppointment = activitiesView.findViewById(R.id.rvAppointment);
		rvAppointment.setNestedScrollingEnabled(false);
		rvMeeting = activitiesView.findViewById(R.id.rvMeeting);
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
		}
	}

	private void meeting() {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			MeetingRequest request =
				new MeetingRequest(new Request(leadId, REPORT_PAGE_SIZE, "0", "0", "0", mUcid),
					new Filter(null, null, null, null, null, null, null), true);
			getCompositeDisposable().add(getSaathiApi().getMeetings(request)
				.compose(applyUiModel())
				.subscribe(this::renderMeetings));
		} else {
			noInternetConnectionDialog();
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
			MeetingAdapter meetingAdapter = new MeetingAdapter(meetingList);
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
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			AppointmentRequest request =
				new AppointmentRequest(new Request(leadId, REPORT_PAGE_SIZE, "1", "0", "0", mUcid),
					new Filter(null, null, null, null, null, null, null), true);
			getCompositeDisposable().add(getSaathiApi().getAppointments(request)
				.compose(applyUiModel())
				.subscribe(this::renderAppointments));
		} else {
			noInternetConnectionDialog();
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
		final Dialog dialog = new Dialog(requireContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Objects.requireNonNull(dialog.getWindow())
			.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.dialog_appointments);
		dialog.getWindow()
			.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dialog.setCanceledOnTouchOutside(false);

		ImageView ivClose = dialog.findViewById(R.id.ivClose);
		ImageView ivRescheduleMeeting = dialog.findViewById(R.id.ivRescheduleMeeting);
		ImageView ivUpdateMeeting = dialog.findViewById(R.id.ivUpdateMeeting);
		ImageView ivCancelMeeting = dialog.findViewById(R.id.ivCancelMeeting);

		String dateStr = appointmentModel.getDate(); //07/24/2017
		String timeStr = appointmentModel.getTime(); //4:05PM
		Date apptDate = CRMAppUtil.getDate(dateStr + " " + timeStr, "dd MMM yyyy h:mma");

		if (CRMAppUtil.isBeforeDate(Objects.requireNonNull(apptDate))) {
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
				//navigateTo(NAV_TO_SET_APPOINTMENT, bundle, getFragmentManager(), true);
				screenNavigator.openPage(NAVIGATE_TO_SET_APPOINTMENT, bundle, true);
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
						public void dialogAction(MSFDialog.Action action, Object... data) {
							if (action == MSFDialog.Action.OK) {
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
							public void dialogAction(MSFDialog.Action action, Object... data) {
								if (action == MSFDialog.Action.OK) {
									deleteAppintments(appointmentModel.getMeetingid(),
										"DeactivateMeeting");
								} else if (action == MSFDialog.Action.CANCEL) {
									deleteAppintments(appointmentModel.getMeetingid(),
										"DeactivateALLMeeting");
								}
							}
						});
					dialog.dismiss();
					addSelDialog.show();
				}
			}
		});

		ivUpdateMeeting.setOnClickListener(view -> {
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
			//navigateTo(NAV_TO_UPDATE_MEETING, args, getFragmentManager(), true);
			screenNavigator.openPage(NAVIGATE_TO_UPDATE_MEETING, args, true);
			dialog.dismiss();
		});

		ivClose.setOnClickListener(view -> {
			dialog.dismiss();
		});
		dialog.show();
	}

	private void deleteAppintments(final String meetingId, String url) {
		//login parameters
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			DeleteAppointmentRequest request =
				new DeleteAppointmentRequest(meetingId);
			getCompositeDisposable().add(getSaathiApi().deleteAppointments(url, request)
				.compose(applyUiModel())
				.subscribe(uiModel -> {
					renderDeleteAppointments(uiModel, meetingId);
				}));
		} else {
			noInternetConnectionDialog();
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

				});
		}
	}

	public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder1> {

		private List<LeadActivitiesAppointModel> appointmentList;

		private AppointmentAdapter(List<LeadActivitiesAppointModel> appointmentList) {
			this.appointmentList = appointmentList;
		}

		@NotNull @Override
		public AppointmentAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
			View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_meetings, parent, false);

			return new MyViewHolder1(itemView);
		}

		@Override
		public void onBindViewHolder(MyViewHolder1 holder, int position) {
			final LeadActivitiesAppointModel appointmentModel = appointmentList.get(position);
			String[] metadataArray = appointmentModel.getDate().toString().split(" ");
			holder.tvMonthName.setText(metadataArray[1]);
			holder.tvMonthDate.setText(metadataArray[0]);
			holder.tvMonthYear.setText(metadataArray[2]);
			holder.tvTimeRange.setText(appointmentModel.getTime());
			holder.tvAttFirstName.setText(appointmentModel.getAttendeeEmp());
			holder.tvAppointmentType.setText(
				String.format("Appointment for %s", appointmentModel.getProduct()));
			holder.relativeLayout.setOnClickListener(view -> {
				if (AccountDetails.getEmployeeCode()
					.equalsIgnoreCase(appointmentModel.getCreatedBy())) {
					leadViewDialog(appointmentModel);
				}
			});
		}

		private LeadActivitiesAppointModel getDocumentById(String meetingId) {
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

		private void deleteAppointment(String meetingId) {
			LeadActivitiesAppointModel model = getDocumentById(meetingId);
			appointmentList.remove(model);
			notifyDataSetChanged();
			if (appointmentList.size() == 0) {
				tvNoAppointment.setVisibility(View.VISIBLE);
			}
		}

		private class MyViewHolder1 extends RecyclerView.ViewHolder {
			RelativeLayout relativeLayout;
			private TextView tvAppointmentType, tvMonthName, tvMonthDate, tvMonthYear, tvTimeRange,
				tvAttFirstName;

			private MyViewHolder1(View view) {
				super(view);
				tvMonthName = view.findViewById(R.id.tvMonthName);
				tvMonthDate = view.findViewById(R.id.tvMonthDate);
				tvMonthYear = view.findViewById(R.id.tvMonthYear);
				tvTimeRange = view.findViewById(R.id.tvTimeRange);
				tvAttFirstName = view.findViewById(R.id.tvAttFirstName);
				relativeLayout = view.findViewById(R.id.relativeLayout);
				tvAppointmentType = view.findViewById(R.id.tvAppointmentType);
			}
		}
	}

	public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder1> {

		private List<LeadActivitiesMeetingModel> meetingList;

		private MeetingAdapter(List<LeadActivitiesMeetingModel> meetingList) {
			this.meetingList = meetingList;
		}

		@NotNull @Override
		public MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
			View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_meetings, parent, false);

			return new MyViewHolder1(itemView);
		}

		@Override
		public void onBindViewHolder(MyViewHolder1 holder, int position) {
			final LeadActivitiesMeetingModel meetingModel = meetingList.get(position);
			String[] metadataArray = meetingModel.getDate().toString().split(" ");
			holder.tvMonthName.setText(metadataArray[1]);
			holder.tvMonthDate.setText(metadataArray[0]);
			holder.tvMonthYear.setText(metadataArray[2]);
			holder.tvAppointmentType.setText(meetingModel.getMeetingReason());
			holder.tvTimeRange.setText(meetingModel.getTime());
			holder.tvAttFirstName.setText(meetingModel.getAttendeeEmp());
			holder.relativeLayout.setOnClickListener(view -> {
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
				args.putString("GeoMeetingLocation", meetingModel.getGeoMeetingLocation());
				//navigateTo(NAV_TO_LEAD_ACTIVITIES_DETAIL, args, getFragmentManager(), true);
				screenNavigator.openPage(NAVIGATE_TO_LEAD_ACTIVITIES_DETAILS, args, true);
			});
		}

		@Override
		public int getItemCount() {
			return meetingList.size();
		}

		public class MyViewHolder1 extends RecyclerView.ViewHolder {
			public RelativeLayout relativeLayout;
			private TextView tvMonthName, tvMonthDate, tvMonthYear, tvAppointmentType, tvTimeRange,
				tvAttFirstName;

			private MyViewHolder1(View view) {
				super(view);
				tvMonthName = view.findViewById(R.id.tvMonthName);
				tvMonthDate = view.findViewById(R.id.tvMonthDate);
				tvMonthYear = view.findViewById(R.id.tvMonthYear);
				tvAppointmentType = view.findViewById(R.id.tvAppointmentType);
				tvTimeRange = view.findViewById(R.id.tvTimeRange);
				tvAttFirstName = view.findViewById(R.id.tvAttFirstName);
				relativeLayout = view.findViewById(R.id.relativeLayout);
			}
		}
	}
}

package com.mosl.tabapp.calendar;

import android.app.Dialog;
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
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.calender.CalenderItem;
import com.mosl.saathi.api.model.calender.CalenderRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.CalenderModel;
import com.mosl.tabapp.model.LeadActivitiesAppointModel;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.getDateFromString;
import static com.mosl.tabapp.constants.MOSLConstants.DATE;
import static com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_OF_DD_MMM_YYYY;
import static com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_SERVER_MEETING;
import static com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_SERVER_MEETING_SEND;

public class CalendarAppointsListFragment extends CommonFragment
	implements View.OnClickListener {
	private TextView tvStartDate, tvNochart;
	private String fromPostingDate;
	private Date fromDat;
	private Dialog dialog;
	private RecyclerView recyclerView;
	private ArrayList<LeadActivitiesAppointModel> calenderList;
	private ClientMeetingAdapter calendarAdapter;

	@Override protected int getLayout() {
		return R.layout.frag_client_meeting;
	}

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View calendarView = super.onCreateView(inflater, container, savedInstanceState);
		//attachLayout(R.layout.frag_client_meeting);
		//setAppTitle(getClass().toString(), "Calendar");
		setUpView(Objects.requireNonNull(calendarView));
		return calendarView;
	}

	private void setUpView(View calendarView) {
		tvStartDate = calendarView.findViewById(R.id.tvDate);
		tvNochart = calendarView.findViewById(R.id.tvNochart);
		String mainToDate = Objects.requireNonNull(this.getArguments()).getString(DATE);
		fromPostingDate = mainToDate;
		mainToDate = getDateFromString(mainToDate, DATE_FORMAT_TO_SERVER_MEETING_SEND,
			DATE_FORMAT_OF_DD_MMM_YYYY);
		tvStartDate.setText(mainToDate);

		ImageView ivDatePicker = calendarView.findViewById(R.id.ivDatePicker);
		ivDatePicker.setOnClickListener(this);
		recyclerView = calendarView.findViewById(R.id.recyclerView);
		calenderList = new ArrayList<>();

		for (LeadActivitiesAppointModel list : CalenderModel.getCalenderList()) {
			calenderList.add(new LeadActivitiesAppointModel(list.getStatus(), list.getMeetingid(),
				list.getLeadid(), list.getName(), list.getEmail(), list.getMobNo(), list.getDate(),
				list.getTime(), list.getAttendeeEmp()
				, list.getProduct(), list.getStatusName(), list.getActivityType(),
				list.getMeetingReason(), list.getMeetingDuration(), list.getCategory(),
				list.getActionable(), list.getComments(), list.getStage(), list.getCallback(),
				list.getQueryDesc()));
		}

		calendarAdapter = new ClientMeetingAdapter(calenderList);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setAdapter(calendarAdapter);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.ivDatePicker) {
			selectDate();
		}
	}

	private void selectDate() {
		Calendar today = Calendar.getInstance();
		dialog = new Dialog(requireContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Objects.requireNonNull(dialog.getWindow())
			.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.date_picker);
		dialog.getWindow()
			.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

		DatePicker dpFromDate = dialog.findViewById(R.id.datePickerFromDate);
		LinearLayout linearLayoutOfToDate = dialog.findViewById(R.id.linearLayoutOfToDate);
		linearLayoutOfToDate.setVisibility(View.GONE);
		dpFromDate.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
		ImageView close = dialog.findViewById(R.id.ibtnClose);
		Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

		if (fromPostingDate != null) {
			String[] dateSplit = fromPostingDate.split("-");
			dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
				Integer.parseInt(dateSplit[2]),
				(view, year, monthOfYear, dayOfMonth) -> fromPostingDate =
					year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth));
		} else {
			dpFromDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
				today.get(Calendar.DATE), (view, year, monthOfYear, dayOfMonth) -> fromPostingDate =
					year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth));
		}

		close.setOnClickListener(v -> {
			dialog.dismiss();
		});

		btnSubmit.setOnClickListener(v -> {
			fromDat = CRMAppUtil.getDate(fromPostingDate, "yyyy-MM-dd");
			if (fromPostingDate != null) {
				fromDat = CRMAppUtil.getDate(fromPostingDate, "yyyy-MM-dd");
				fromPostingDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd");
				tvStartDate.setText(CRMAppUtil.getDateStr(fromDat, DATE_FORMAT_OF_DD_MMM_YYYY));
				appointments(fromPostingDate);
			}
			dialog.dismiss();
		});

		dialog.show();
	}

	public String prefix(int input) {
		if (Integer.toString(input).length() == 1) {
			return "0" + input;
		}
		return input + "";
	}

	private void renderCalender(UiModel<List<CalenderItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
			tvNochart.setVisibility(View.VISIBLE);
			calenderList = new ArrayList<>();
			calendarAdapter = new ClientMeetingAdapter(calenderList);
			RecyclerView.LayoutManager mLayoutManager =
				new LinearLayoutManager(getActivity());
			recyclerView.setLayoutManager(mLayoutManager);
			recyclerView.setAdapter(calendarAdapter);
		}

		List<CalenderItem> data = uiModel.data;

		if (data != null) {
			calenderList = new ArrayList<>();
			if (data.size() > 0) {
				for (int i = 0; i < data.size(); i++) {
					CalenderItem item = data.get(i);
					String status = stringOrEmpty(item.status);
					String meetingId = stringOrEmpty(item.meetingID);
					String leadId = stringOrEmpty(item.leadId);
					String name = stringOrEmpty(item.name);
					String email = stringOrEmpty(item.email);
					String mobNo = stringOrEmpty(item.mobNo);
					String meetingDate = stringOrEmpty(item.meetingDate);
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
					String dateDisplay = stringOrEmpty(item.dateDisplay);

					calenderList.add(
						new LeadActivitiesAppointModel(status,
							meetingId, leadId, name, email, mobNo,
							dateDisplay, meetingDate, attendeeEmp
							, product, statusName, activityType,
							meetingReason, meetingDuration, category,
							actionable, comments, stage, customerServiceRemark,
							queryDesc));
				}

				calendarAdapter = new ClientMeetingAdapter(calenderList);
				RecyclerView.LayoutManager mLayoutManager =
					new LinearLayoutManager(getActivity());
				recyclerView.setLayoutManager(mLayoutManager);
				recyclerView.setAdapter(calendarAdapter);
				CalenderModel.setCalenderList(calenderList);
				tvNochart.setVisibility(View.GONE);
			} else {
				tvNochart.setVisibility(View.VISIBLE);
				calendarAdapter = new ClientMeetingAdapter(calenderList);
				RecyclerView.LayoutManager mLayoutManager =
					new LinearLayoutManager(getActivity());
				recyclerView.setLayoutManager(mLayoutManager);
				recyclerView.setAdapter(calendarAdapter);
			}
		}
	}

	private void appointments(String selectionDate) {

		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			CalenderRequest request =
				new CalenderRequest(selectionDate, "A", Objects.requireNonNull(
					userManager.getUser()).employeeCode);
			Disposable subscribe = getSaathiApi().postCalenderByDate(request)
				.compose(applyUiModel())
				.subscribe(this::renderCalender);
		} else {
			noInternetConnectionDialog();
		}
	}

	public class ClientMeetingAdapter
		extends RecyclerView.Adapter<ClientMeetingAdapter.MyViewHolder> {

		private List<LeadActivitiesAppointModel> calenderList;

		private ClientMeetingAdapter(List<LeadActivitiesAppointModel> calenderList) {
			this.calenderList = calenderList;
		}

		@NotNull @Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_client_meeting_list, parent, false);

			return new MyViewHolder(itemView);
		}

		@Override
		public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
			LeadActivitiesAppointModel clientMeetingModel = calenderList.get(position);
			try {
				holder.tvClientCode.setText(clientMeetingModel.getLeadid());
				holder.tvName.setText(clientMeetingModel.getName());
				String dateStr =
					getDateFromString(clientMeetingModel.getTime(), DATE_FORMAT_TO_SERVER_MEETING,
						"dd-MMM-yyyy");
				holder.tvCompleteDate.setText(dateStr);
				String timeStr =
					getDateFromString(clientMeetingModel.getTime(), DATE_FORMAT_TO_SERVER_MEETING,
						"hh:mm a");
				holder.tvTime.setText(timeStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public int getItemCount() {
			return calenderList.size();
		}

		private class MyViewHolder extends RecyclerView.ViewHolder {
			private TextView tvClientCode, tvName, tvCompleteDate, tvTime;

			private MyViewHolder(View view) {
				super(view);
				tvClientCode = view.findViewById(R.id.tvClientCode);
				tvName = view.findViewById(R.id.tvName);
				tvCompleteDate = view.findViewById(R.id.tvCompleteDate);
				tvTime = view.findViewById(R.id.tvTime);
			}
		}
	}
}

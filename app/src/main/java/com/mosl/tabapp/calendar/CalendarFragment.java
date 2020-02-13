package com.mosl.tabapp.calendar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.Disposable;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.constants.MOSLConstants.DATE;
import static com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_SERVER_MEETING;
import static com.mosl.tabapp.constants.MOSLConstants.NAME;
import static com.mosl.tabapp.constants.MOSLConstants.TIME;
import static com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_CALENDER_LIST;

public class CalendarFragment extends CommonFragment implements OnDateSelectedListener {
	@SuppressLint("SimpleDateFormat") private static final DateFormat FORMATTER =
		new SimpleDateFormat("yyyy-MM-dd");
	private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
	private View calendarView;
	private MaterialCalendarView materialCalendarView;

	private TextView tvDay, tvNochart;
	private String selectionDate;
	private RecyclerView recyclerView;
	private Boolean firstLoadDone = true;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		calendarView = super.onCreateView(inflater, container, savedInstanceState);
		container.post(() -> setAppTitle("Calendar"));
		setUpView(calendarView);
		setUserVisibleHint(true);
		return calendarView;
	}

	@Override protected int getLayout() {
		return R.layout.frag_calendar;
	}

	private void setUpView(View calendarView) {
		tvDay = calendarView.findViewById(R.id.tvDay);
		tvNochart = calendarView.findViewById(R.id.tvNochart);
		recyclerView = calendarView.findViewById(R.id.recyclerView);

		materialCalendarView =
			calendarView.findViewById(R.id.materialCalendarView);
		materialCalendarView.setOnDateChangedListener(this);
		materialCalendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

		Calendar instance = Calendar.getInstance();
		materialCalendarView.setSelectedDate(instance.getTime());

		Calendar instance1 = Calendar.getInstance();
		instance1.set(instance1.get(Calendar.YEAR) - 1, Calendar.JANUARY, 1);
		instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);

		Calendar instance2 = Calendar.getInstance();
		instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);

		selectionDate = getSelectedDatesString();
		materialCalendarView.addDecorators(new MySelectorDecorator(requireActivity()),
			new HighlightMonthsDecorator(),
			oneDayDecorator);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		try {
			super.setUserVisibleHint(isVisibleToUser);
			if (isVisibleToUser && calendarView != null) {
				if (firstLoadDone) {
					appointments(selectionDate);
					firstLoadDone = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
		@Nullable CalendarDay date, boolean selected) {
		selectionDate = getSelectedDatesString();
		appointments(selectionDate);
		oneDayDecorator.setDate(date.getDate());
		materialCalendarView.invalidateDecorators();
	}

	private String getSelectedDatesString() {
		CalendarDay date = materialCalendarView.getSelectedDate();
		if (date == null) {
			return "No Selection";
		}
		return FORMATTER.format(date.getDate());
	}

	private Date getSelectedDate() {
		CalendarDay date = materialCalendarView.getSelectedDate();
		if (date == null) {
			return null;
		}
		return date.getDate();
	}

	private void renderCalender(UiModel<List<CalenderItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		ArrayList<LeadActivitiesAppointModel> calenderList;
		CalendarAdapter calendarAdapter;
		if (error != null) {
			showOnFailurePopup(error);
			tvNochart.setVisibility(View.VISIBLE);
			calenderList = new ArrayList<>();
			calendarAdapter = new CalendarAdapter(calenderList);
			RecyclerView.LayoutManager mLayoutManager =
				new LinearLayoutManager(getActivity());
			recyclerView.setLayoutManager(mLayoutManager);
			recyclerView.setAdapter(calendarAdapter);
			CalenderModel.setCalenderList(calenderList);
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

				calendarAdapter = new CalendarAdapter(calenderList);
				RecyclerView.LayoutManager mLayoutManager =
					new LinearLayoutManager(getActivity());
				recyclerView.setLayoutManager(mLayoutManager);
				recyclerView.setAdapter(calendarAdapter);
				CalenderModel.setCalenderList(calenderList);
				tvNochart.setVisibility(View.GONE);
			} else {
				tvNochart.setVisibility(View.VISIBLE);
				calendarAdapter = new CalendarAdapter(calenderList);
				RecyclerView.LayoutManager mLayoutManager =
					new LinearLayoutManager(getActivity());
				recyclerView.setLayoutManager(mLayoutManager);
				recyclerView.setAdapter(calendarAdapter);
				CalenderModel.setCalenderList(calenderList);
			}
		}
	}

	private void appointments(String selectionDate) {
		Date tDate = getSelectedDate();

		tvDay.setText(CRMAppUtil.getDBDate(tDate, "E MMMM d"));
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			CalenderRequest request =
					new CalenderRequest(selectionDate, "A",
							userManager.getUser().employeeCode);
			Disposable subscribe = getSaathiApi().postCalenderByDate(request)
				.compose(applyUiModel())
				.subscribe(this::renderCalender);
		} else {
			noInternetConnectionDialog();
		}
	}

	public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

		private List<LeadActivitiesAppointModel> calenderList;

		private CalendarAdapter(List<LeadActivitiesAppointModel> calenderList) {
			this.calenderList = calenderList;
		}

		@NotNull @Override
		public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_list_calendar, parent, false);

			return new MyViewHolder(itemView);
		}

		@Override
		public void onBindViewHolder(MyViewHolder holder, int position) {
			final LeadActivitiesAppointModel calendarModel = calenderList.get(position);
			holder.tvName.setText(calendarModel.getName());
			String dateStr =
				CRMAppUtil.getDateFromString(calendarModel.getTime(), DATE_FORMAT_TO_SERVER_MEETING,
					"hh:mm a");
			holder.tvTime.setText(dateStr);
			holder.linearLayout.setOnClickListener(view -> {
				Bundle args = new Bundle();
				args.putString(DATE, selectionDate);
				args.putString(NAME, calendarModel.getName());
				args.putString(TIME, calendarModel.getTime());
				//navigateTo(NAV_TO_CLIENT_MEETING, args, getFragmentManager(), true);
				navigator.openPage(NAVIGATE_TO_CALENDER_LIST, args, true, false);

			});
		}

		@Override
		public int getItemCount() {
			return calenderList.size();
		}

		public class MyViewHolder extends RecyclerView.ViewHolder {
			public LinearLayout linearLayout;
			private TextView tvName, tvTime;

			private MyViewHolder(View view) {
				super(view);
				tvName = view.findViewById(R.id.tvName);
				tvTime = view.findViewById(R.id.tvTime);
				linearLayout = view.findViewById(R.id.linearLayout);
			}
		}
	}
}

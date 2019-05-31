package com.mosl.tabapp.calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mosl.app.core.data.UiModel;
import com.mosl.saathi.api.model.calender.CalenderItem;
import com.mosl.saathi.api.model.calender.CalenderRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.CalenderModel;
import com.mosl.tabapp.model.LeadActivitiesAppointModel;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import io.reactivex.disposables.Disposable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.mosl.app.core.data.UiModelHelper.applyUiModel;

/**
 * Created by raju on 3/9/2017.
 */

public class CalendarFragment extends MOSLCommonFragment implements OnDateSelectedListener {
    private View calendarView;
    // private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    private MaterialCalendarView materialCalendarView;

    private TextView tvDay, tvNochart;// tvDate,tvHour;
    private String selectionDate;
    private RecyclerView recyclerView;
    private ArrayList<LeadActivitiesAppointModel> calenderList;
    private CalendarAdapter calendarAdapter;
    Boolean firstLoadDone = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        calendarView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_calendar);
        setAppTitle(getClass().toString(), "Calendar");
        setUpView(calendarView);
        setUserVisibleHint(true);
        return calendarView;
    }

    private void setUpView(View calendarView) {
        tvDay = (TextView) calendarView.findViewById(R.id.tvDay);
        tvNochart = (TextView) calendarView.findViewById(R.id.tvNochart);
        recyclerView = (RecyclerView) calendarView.findViewById(R.id.recyclerView);

        materialCalendarView =
            (MaterialCalendarView) calendarView.findViewById(R.id.materialCalendarView);
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
        materialCalendarView.addDecorators(new MySelectorDecorator(getActivity()),
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
        /*    Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            Crashlytics.logException(e);*/
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
        @Nullable CalendarDay date, boolean selected) {
        // textView.setText(getSelectedDatesString());
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
        //E MMMM d  -->> Fri September 22
        Date tDate = getSelectedDate();

        tvDay.setText(CRMAppUtil.getDBDate(tDate, "E MMMM d"));

        ////////////////////////////////////////////////////////////

        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            CalenderRequest request =
                new CalenderRequest(selectionDate, "A", AccountDetails.getEmployeeCode());
            Disposable subscribe = saathiApi.postCalenderByDate(request)
                .compose(applyUiModel())
                .subscribe(this::renderCalender);
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder> {

        private List<LeadActivitiesAppointModel> calenderList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvName, tvTime;
            public LinearLayout linearLayout;

            public MyViewHolder(View view) {
                super(view);
                tvName = (TextView) view.findViewById(R.id.tvName);
                tvTime = (TextView) view.findViewById(R.id.tvTime);
                linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
            }
        }

        public CalendarAdapter(List<LeadActivitiesAppointModel> calenderList) {
            this.calenderList = calenderList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_calendar, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final LeadActivitiesAppointModel calendarModel = calenderList.get(position);
            holder.tvName.setText(calendarModel.getName());
            // holder.tvTime.setText(calendarModel.getTime());
            String dateStr =
                CRMAppUtil.getDateFromString(calendarModel.getTime(), DATE_FORMAT_TO_SERVER_MEETING,
                    "hh:mm a");
            holder.tvTime.setText(dateStr);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString(DATE, selectionDate);
                    args.putString(NAME, calendarModel.getName());
                    args.putString(TIME, calendarModel.getTime());
                    navigateTo(NAV_TO_CLIENT_MEETING, args, getFragmentManager(), true);
                }
            });
        }

        @Override
        public int getItemCount() {
            return calenderList.size();
        }
    }
}

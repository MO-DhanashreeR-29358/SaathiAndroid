package com.mosl.tabapp.notification;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.notification.NotificationRequest;
import com.mosl.saathi.api.model.notification.NotificationResponse;
import com.mosl.saathi.domain.DateFormatUtils;
import com.mosl.saathi.domain.NewsDate;
import com.mosl.saathi.domain.NewsDateKt;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.leadViewDetail.LeadViewMainFragment;
import com.mosl.tabapp.model.NotificationData;
import com.mosl.tabapp.model.NotificationModel;
import com.mosl.tabapp.navigation.NavigationConstants;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.constants.MOSLConstants.BUSINESS;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD_ID;
import static com.mosl.tabapp.constants.MOSLConstants.NOTIFICATION_DATA;
import static com.mosl.tabapp.constants.MOSLConstants.OPERATIONAL;
import static com.mosl.tabapp.constants.MOSLConstants.UCID;
import static com.mosl.tabapp.model.AccountDetails.isAufsb;

/**
 * Created by raju on 4/20/2017.
 */

public class NotificationFragment extends CommonFragment
    implements AdapterView.OnItemSelectedListener {
    private View notificationView;
    private RecyclerView recyclerView;
    private final ArrayList<NotificationModel> notificationList = new ArrayList<>();
    private NotificationAdapter notificationAdapter;
    private TextView tvNochart;
    private Spinner spCategory;
    private ArrayList<String> arrCategory;
    private ArrayAdapter<String> arrAdapCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        notificationView = super.onCreateView(inflater, container, savedInstanceState);
        setAppTitle("Notification");
        setUpView(notificationView);
        setUserVisibleHint(true);
        return notificationView;
    }

    @Override protected int getLayout() {
        return R.layout.frag_notification;
    }

    private void setUpView(View calendarView) {
        spCategory = calendarView.findViewById(R.id.spCategory);
        recyclerView = calendarView.findViewById(R.id.recyclerView);
        tvNochart = calendarView.findViewById(R.id.tvNochart);

        String[] eventTypeArray =
            getActivity().getResources().getStringArray(R.array.NotificationCategory);
        arrCategory = new ArrayList<String>(Arrays.asList(eventTypeArray));
        arrAdapCategory =
            new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrCategory);
        arrAdapCategory.setDropDownViewResource(R.layout.mdropdown);
        spCategory.setAdapter(arrAdapCategory);

        spCategory.setOnItemSelectedListener(NotificationFragment.this);

        if (isAufsb()) {
            spCategory.setVisibility(View.GONE);
            notification(OPERATIONAL);
        }
    }

    protected void renderNotifications(UiModel<List<NotificationResponse>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
            tvNochart.setVisibility(View.VISIBLE);
        }

        List<NotificationResponse> data = uiModel.data;
        if (data != null) {
            notificationList.clear();
            for (NotificationResponse datum : data) {
                Date creationDate = DateFormatUtils.parseOrDefault(DateFormatUtils.DATE_TIME_UTC,
                    stringOrEmpty(datum.creationDate));
                NewsDate newsDate = NewsDateKt.toNewsDate(creationDate);
                notificationList.add(
                    new NotificationModel(
                        stringOrEmpty(datum.message),
                        newsDate.day,
                        newsDate.time,
                        newsDate.month,
                        stringOrEmpty(datum.additionalData)
                    )
                );
            }
            notificationAdapter = new NotificationAdapter(notificationList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(notificationAdapter);

            if (notificationList.isEmpty()) {
                tvNochart.setVisibility(View.VISIBLE);
            } else {
                tvNochart.setVisibility(View.GONE);
            }
        }
    }

    private void notification(String category) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            Disposable disposable =
                getSaathiApi().getNotifications(new NotificationRequest(category))
                .compose(applyUiModel())
                .subscribe(this::renderNotifications);
        } else {
            noInternetConnectionDialog();
        }
    }

    //endregion
    //region selection logic
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: {
                notification(OPERATIONAL);
            }
            break;
            case 1: {
                notification(BUSINESS);
            }
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void navigateTo(String dataStr) {
        if (dataStr == null || dataStr.length() < 1) {
            //Noproper data goto dsh board
            navigator.openHome();
        }
        NotificationData data = new NotificationData(dataStr);
        if (data.getPage().equalsIgnoreCase("LEADVIEW")) {
            //Goto leadView Detail
            MOSLCommonFragment fragment = new LeadViewMainFragment();
            //BackStack
            Bundle bundle = new Bundle();
            String leadId = data.getParamMap().get(LEAD_ID);
            String ucid = data.getParamMap().get(UCID);
            ucid = (ucid == null || ucid.equalsIgnoreCase("")) ? "0" : ucid;
            leadId = (leadId == null || leadId.equalsIgnoreCase("")) ? "0" : leadId;
            bundle.putString(UCID, ucid);
            bundle.putString(LEAD_ID, leadId);
            bundle.putParcelable(NOTIFICATION_DATA, data);
            fragment.setArguments(bundle);
            //todo open lead view main fragment
            //navigator.openPage();
        }
        //PENDINGAPPROVAL
        else if (data.getPage().equalsIgnoreCase("PENDINGAPPROVAL")) {
            navigator.openPage(NavigationConstants.NAVIGATE_TO_PENDING_APPROVAL, false);
        }
        //BIRTHDAY
        else if (data.getPage().equalsIgnoreCase("BIRTHDAY")) {
            navigator.openPage(NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY, false);
        }
        //DASHBOARD
        else if (data.getPage().equalsIgnoreCase("DASHBOARD")) {
            navigator.openHome();
        } else {
            navigator.openHome();
        }
    }

    public class NotificationAdapter
        extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

        private List<NotificationModel> notificationList;

        public NotificationAdapter(List<NotificationModel> notificationList) {
            this.notificationList = notificationList;
        }

        @Override
        public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list, parent, false);

            return new NotificationAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, int position) {
            final NotificationModel model = notificationList.get(position);
            if (position % 2 == 1) {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));
            }
            holder.txtMessage.setText(model.getMessage());
            holder.timestamp.setText(model.getTime());
            holder.dateUpper.setText(model.getDate());
            holder.dateLower.setText(model.getMonth());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    navigateTo(model.getAdditionalData());
                }
            });
        }

        @Override
        public int getItemCount() {
            return notificationList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout linearLayout;
            TextView timestamp, txtMessage, dateUpper, dateLower;

            public MyViewHolder(View view) {
                super(view);
                timestamp = view.findViewById(R.id.timestamp);
                txtMessage = view.findViewById(R.id.txtMessage);
                dateUpper = view.findViewById(R.id.dateUpper);
                dateLower = view.findViewById(R.id.dateLower);
                linearLayout = view.findViewById(R.id.linearLayoutOfListItem);
            }
        }
    }
}

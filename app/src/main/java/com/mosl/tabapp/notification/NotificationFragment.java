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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mosl.app.coreui.spinner.AdapterUtils;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.notification.NotificationRequest;
import com.mosl.saathi.api.model.notification.NotificationResponse;
import com.mosl.saathi.domain.DateFormatUtils;
import com.mosl.saathi.domain.NewsDate;
import com.mosl.saathi.domain.NewsDateKt;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.NotificationData;
import com.mosl.tabapp.model.NotificationModel;
import com.mosl.tabapp.navigation.NavigationConstants;
import com.mosl.tabapp.util.SpinnerAdapterFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.disposables.Disposable;

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
    private RecyclerView recyclerView;
    private final ArrayList<NotificationModel> notificationList = new ArrayList<>();
    private TextView tvNochart;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View notificationView = super.onCreateView(inflater, container, savedInstanceState);
        container.post(() -> setAppTitle("Notification"));
        setUpView(notificationView);
        return notificationView;
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_notification;
    }

    private void setUpView(View calendarView) {
        Spinner spCategory = calendarView.findViewById(R.id.spCategory);
        recyclerView = calendarView.findViewById(R.id.recyclerView);
        tvNochart = calendarView.findViewById(R.id.tvNochart);

        String[] eventTypeArray =
                requireActivity().getResources().getStringArray(R.array.NotificationCategory);
        ArrayList<String> arrCategory = new ArrayList<>(Arrays.asList(eventTypeArray));

        ArrayAdapter<String> arrAdapCategory = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
        spCategory.setAdapter(arrAdapCategory);
        AdapterUtils.setSpinnerData(arrAdapCategory, spCategory, arrCategory);

        spCategory.setOnItemSelectedListener(NotificationFragment.this);

        if (isAufsb()) {
            spCategory.setVisibility(View.GONE);
            notification(OPERATIONAL);
        }
    }

    private void renderNotifications(UiModel<List<NotificationResponse>> uiModel) {
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
            NotificationAdapter notificationAdapter = new NotificationAdapter(notificationList);
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
            navigator.openHome();
        }
        NotificationData data = new NotificationData(dataStr);
        if (data.getPage().equalsIgnoreCase("LEADVIEW")) {
            Bundle bundle = new Bundle();
            String leadId = data.getParamMap().get(LEAD_ID);
            String ucid = data.getParamMap().get(UCID);
            ucid = (ucid == null || ucid.equalsIgnoreCase("")) ? "0" : ucid;
            leadId = (leadId == null || leadId.equalsIgnoreCase("")) ? "0" : leadId;
            bundle.putString(UCID, ucid);
            bundle.putString(LEAD_ID, leadId);
            bundle.putParcelable(NOTIFICATION_DATA, data);
            navigator.openPage(NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true,
                    false);
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

        NotificationAdapter(List<NotificationModel> notificationList) {
            this.notificationList = notificationList;
        }

        @NotNull
        @Override
        public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notification_list, parent, false);

            return new NotificationAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull NotificationAdapter.MyViewHolder holder, int position) {
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
            public ConstraintLayout linearLayout;
            TextView timestamp, txtMessage, dateUpper, dateLower;

            MyViewHolder(View view) {
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

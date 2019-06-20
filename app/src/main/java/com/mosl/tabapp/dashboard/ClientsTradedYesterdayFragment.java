package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import com.mosl.app.core.data.UiModel;
import com.mosl.saathi.api.model.dashboard.client.ClientRequest;
import com.mosl.saathi.api.model.dashboard.clienttradedyesterday.ClientTradedYesterdayItem;
import com.mosl.saathi.api.model.dashboard.clienttradedyesterday.TableItem;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import io.reactivex.disposables.Disposable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mosl.app.core.data.UiModelHelper.applyUiModel;

/**
 * Created by Sanket Gurav on 7/5/2017.
 */

public class ClientsTradedYesterdayFragment extends MOSLCommonFragment
    implements View.OnClickListener {
    private View clientYesterdayView;
    static Context mContext;
    private DashboardModel dashboardModel;
    private String widgetSystemName;
    private String groupName;
    private Map addtionalParameterMap = new HashMap<>();
    private ArrayList<HashMap<String, String>> arrayListClient =
        new ArrayList<HashMap<String, String>>();
    private TableLayout tableLayout;
    boolean dataFoundFlag;

    private Integer familyCount = 0;
    private View pBar;
    private TextView tvNochart;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        clientYesterdayView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_client_traded);
        setUpView(clientYesterdayView);
        hideAppTitle(getClass().toString());
        setUserVisibleHint(true);
        return clientYesterdayView;
    }

    public static ClientsTradedYesterdayFragment newInstance(Context context) {
        ClientsTradedYesterdayFragment fragment = new ClientsTradedYesterdayFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View clientsView) {
        tableLayout = (TableLayout) clientsView.findViewById(R.id.tableLayout);
        pBar = clientsView.findViewById(R.id.pBar);
        tvNochart = (TextView) clientsView.findViewById(R.id.tvNochart);

        dashboardModel = (DashboardModel) this.getArguments().getSerializable(DASHBOARD_MODEL);
        if (dashboardModel != null) {
            DashboardGroupModel groupModel =
                dashboardModel.getGroupModel(DashboardSystemModel.getClientTradedSystemName());
            WidgetModel model;
            List<WidgetModel> widgetList = groupModel.getWidgetList();
            if (widgetList != null && widgetList.size() > 0) {
                if (widgetList.size() == 1) {
                    model = widgetList.get(0);
                } else {
                    model = widgetList.get(1);
                }
                widgetSystemName = model.getSystemName();
                groupName = model.getName();
            }
        }
    }

    private Map<String, String> getAddtionalParameterMap() {
        Map<String, String> addtionalParameterMap = new HashMap<>();
        try {
            addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return addtionalParameterMap;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    private void renderClientTradedYesterday(ClientTradedYesterdayItem item) {
        if (item.tables != null) {
            List<TableItem> tableList = item.tables;
            for (int i = 0; i < tableList.size(); i++) {
                HashMap<String, String> fieldData = new HashMap<String, String>();
                TableItem tableItem = tableList.get(i);
                String revenueType = stringOrEmpty(tableItem.revenueType);
                String NoOfClients = stringOrZero(String.valueOf(tableItem.nOOFCLIENTS));
                String RevenueGenerated = stringOrZero(String.valueOf(tableItem.rEVENUEGENERATED));
                fieldData.put("REVENUETYPE", revenueType);
                fieldData.put("NOOFCLIENTS", NoOfClients);
                fieldData.put("REVENUEGENERATED", RevenueGenerated);
                arrayListClient.add(fieldData);
            }
            dataFoundFlag = true;
            incentiveDynamicView(arrayListClient);
        }
    }

    private void render(UiModel<ClientTradedYesterdayItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            clientHideProgress();
            arrayListClient.clear();
            tvNochart.setVisibility(View.VISIBLE);
            showOnFailurePopup(error);
        }

        ClientTradedYesterdayItem data = uiModel.data;
        if (data != null) {
            clientHideProgress();
            try {
                renderClientTradedYesterday(data);
            } catch (Exception e) {
                arrayListClient.clear();
                tvNochart.setVisibility(View.VISIBLE);
            }
        }
    }

    private void webServiceCall() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            String businessProfileId = dashboardModel.getBusinessProfileIdForDashboard();
            ClientRequest request = new ClientRequest(
                businessProfileId,
                getAddtionalParameterMap(),
                widgetSystemName,
                dashboardModel.getProductId(),
                dashboardModel.getEmployeeCode()
            );
            Disposable subscribe = saathiApi.getClientsTradedYesterday(request)
                .compose(applyUiModel())
                .subscribe(this::render);
        } else {
            noInternetConnectionDialog();
        }

    }

    private void incentiveDynamicView(ArrayList<HashMap<String, String>> arrayListClient) {
        tableLayout.removeAllViews();
        try {
            LayoutInflater inFla = getActivity().getLayoutInflater();
            View tableRowH = inFla.inflate(R.layout.frag_client_traded_top, null);

            tableLayout.addView(tableRowH);
            if (arrayListClient.size() > 0) {
                tvNochart.setVisibility(View.GONE);
                for (int i = 0; i < arrayListClient.size(); i++) {
                    String revenuetype = arrayListClient.get(i).get("REVENUETYPE");
                    String noofclients = arrayListClient.get(i).get("NOOFCLIENTS");
                    String revenuegenerated = arrayListClient.get(i).get("REVENUEGENERATED");
                    View tableRow = inFla.inflate(R.layout.frag_client_traded_bottom, null);
                    final TextView tvSegment = (TextView) tableRow.findViewById(R.id.tvSegment);
                    final TextView tvNoOfClients =
                        (TextView) tableRow.findViewById(R.id.tvNoOfClients);
                    final TextView tvRevenueGenerated =
                        (TextView) tableRow.findViewById(R.id.tvRevenueGenerated);

                    tvSegment.setText(revenuetype);
                    tvNoOfClients.setText(noofclients);
                    tvRevenueGenerated.setText(revenuegenerated);
                    tvNoOfClients.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addtionalParameterMap.clear();
                            addtionalParameterMap.putAll(getAddtionalParameterMap());
                            addtionalParameterMap.put(ROW_FILTER, tvSegment.getText().toString());
                            Bundle args = new Bundle();
                            args.putString(SYSTEM_NAME, widgetSystemName);
                            args.putString(LABEL, groupName);
                            args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                            if (!tvNoOfClients.getText().toString().trim().equalsIgnoreCase("0")) {
                                navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
                            }
                        }
                    });
                    tvRevenueGenerated.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addtionalParameterMap.clear();
                            addtionalParameterMap.putAll(getAddtionalParameterMap());
                            addtionalParameterMap.put(ROW_FILTER, tvSegment.getText().toString());
                            Bundle args = new Bundle();
                            args.putString(SYSTEM_NAME, widgetSystemName);
                            args.putString(LABEL, groupName);
                            args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                            if (!tvRevenueGenerated.getText()
                                .toString()
                                .trim()
                                .equalsIgnoreCase("0")) {
                                navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
                            }
                        }
                    });
                    //  }
                    tableLayout.addView(tableRow);
                }
            } else {
                tvNochart.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && clientYesterdayView != null) {
                webServiceCall();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clientShowProgress() {
        pBar.setVisibility(View.VISIBLE);
    }

    private void clientHideProgress() {
        pBar.setVisibility(View.GONE);
    }
}

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
import com.mosl.saathi.api.model.dashboard.snapshot.SnapshotItem;
import com.mosl.saathi.api.model.dashboard.snapshot.SnapshotTableItem;
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
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class BusSnapshotAdvFragment extends MOSLCommonFragment {

    static Context mContext;
    private View clientsView;
    private DashboardModel dashboardModel;
    private String widgetSystemName;
    private Map addtionalParameterMap = new HashMap<>();
    private ArrayList<HashMap<String, String>> arrayListClient = new ArrayList<HashMap<String, String>>();
    private TableLayout tableLayout;
    private Integer familyCount = 0;
    private View pBar;
    private TextView tvNochart;

    public static BusSnapshotAdvFragment newInstance(Context context) {
        BusSnapshotAdvFragment fragment = new BusSnapshotAdvFragment();
        mContext = context;
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clientsView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_bus_snapshot_adv);
        setUpView(clientsView);
        hideAppTitle(getClass().toString());
        setUserVisibleHint(true);
        return clientsView;
    }

    private void setUpView(View clientsView) {
        tableLayout = clientsView.findViewById(R.id.tableLayout);
        pBar = clientsView.findViewById(R.id.pBar);
        tvNochart = clientsView.findViewById(R.id.tvNochart);


        dashboardModel = (DashboardModel) this.getArguments().getSerializable(DASHBOARD_MODEL);
        if (dashboardModel != null) {
            DashboardGroupModel groupModel = dashboardModel.getGroupModel(DashboardSystemModel.getBusSnapshotAdvisorSystemName());
            List<WidgetModel> widgetList = groupModel.getWidgetList();
            if (widgetList != null && widgetList.size() > 0) {
                WidgetModel model = widgetList.get(1);
                widgetSystemName = model.getSystemName();
                webServiceCall();
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

    public void renderSnapshot(UiModel<SnapshotItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            clientHideProgress();
            arrayListClient.clear();
            tvNochart.setVisibility(View.VISIBLE);
            showOnFailurePopup(error);
        }

        SnapshotItem data = uiModel.data;
        if (data != null) {
            clientHideProgress();
            arrayListClient.clear();
            List<SnapshotTableItem> tables = data.table;
            if (tables != null && tables.size() != 0) {
                for (int i = 0; i < tables.size(); i++) {
                    HashMap<String, String> fieldData = new HashMap<String, String>();
                    SnapshotTableItem item = tables.get(i);
                    String familyStatus = stringOrEmpty(item.parameter);
                    String mtdCount = stringOrEmpty(item.mTDCNT);
                    String ytdCnt = stringOrEmpty(item.yTDCNT);
                    fieldData.put(PARAMETER, familyStatus);
                    fieldData.put(MTD_CNT, mtdCount);
                    fieldData.put(YTD_CNT, ytdCnt);
                    arrayListClient.add(fieldData);
                }
                incentiveDynamicView();
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

            Disposable subscribe = saathiApi.getSnapshot(request)
                .compose(applyUiModel())
                .subscribe(this::renderSnapshot);
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }

    }


    private void incentiveDynamicView() {
        tableLayout.removeAllViews();
        try {
            LayoutInflater inFla = getActivity().getLayoutInflater();
            View tableRowH = inFla.inflate(R.layout.frag_business_snapshot_top, null);
            tableLayout.addView(tableRowH);
            if (arrayListClient.size() > 0) {
                tvNochart.setVisibility(View.GONE);
                for (int i = 0; i < arrayListClient.size(); i++) {
                    String mtdCount = arrayListClient.get(i).get(MTD_CNT);
                    String ytdCount = arrayListClient.get(i).get(YTD_CNT);
                    String parameters = arrayListClient.get(i).get(PARAMETER);
                    View tableRow = inFla.inflate(R.layout.frag_business_snapshot_bottom, null);
                    final TextView tvParameters = tableRow.findViewById(R.id.tvFamilyStatus);
                    final TextView tvMetMTD = tableRow.findViewById(R.id.tvMetMTD);
                    final TextView tvMetYTD = tableRow.findViewById(R.id.tvMetYTD);

                    tvParameters.setText(parameters);
                    tvMetMTD.setText(mtdCount);
                    tvMetYTD.setText(ytdCount);
                    tvMetMTD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addtionalParameterMap.clear();
                            addtionalParameterMap.putAll(getAddtionalParameterMap());
                            addtionalParameterMap.put(COL_FILTER, MTD_CNT);
                            addtionalParameterMap.put(ROW_FILTER, tvParameters.getText().toString());
                            addtionalParameterMap.put(WIDGETTYPE, tvParameters.getText().toString());
                            Bundle args = new Bundle();
                            args.putString(SYSTEM_NAME, widgetSystemName);
                            args.putString(LABEL, BUSINESS_SNAPSHOT_TITLE);
                            args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                            if (!tvMetMTD.getText().toString().trim().equalsIgnoreCase("0")) {
                                navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
                            }
                        }
                    });
                    tvMetYTD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addtionalParameterMap.clear();
                            addtionalParameterMap.putAll(getAddtionalParameterMap());
                            addtionalParameterMap.put(COL_FILTER, YTD_CNT);
                            addtionalParameterMap.put(ROW_FILTER, tvParameters.getText().toString());
                            addtionalParameterMap.put(WIDGETTYPE, tvParameters.getText().toString());
                            Bundle args = new Bundle();
                            args.putString(SYSTEM_NAME, widgetSystemName);
                            args.putString(LABEL, BUSINESS_SNAPSHOT_TITLE);
                            args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                            if (!tvMetYTD.getText().toString().trim().equalsIgnoreCase("0")) {
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
            if (isVisibleToUser && clientsView != null) {

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

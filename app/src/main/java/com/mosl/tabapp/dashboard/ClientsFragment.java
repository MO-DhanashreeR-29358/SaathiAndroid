package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.dashboard.client.ClientItem;
import com.mosl.saathi.api.model.dashboard.client.ClientRequest;
import com.mosl.saathi.api.model.dashboard.client.Table1Item;
import com.mosl.saathi.api.model.dashboard.client.TableItem;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.navigation.NavigationConstants;
import io.reactivex.disposables.Disposable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.constants.MOSLConstants.COL_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.DASHBOARD_MODEL;
import static com.mosl.tabapp.constants.MOSLConstants.DAY;
import static com.mosl.tabapp.constants.MOSLConstants.FAMILYSTATUS;
import static com.mosl.tabapp.constants.MOSLConstants.LABEL;
import static com.mosl.tabapp.constants.MOSLConstants.MAP;
import static com.mosl.tabapp.constants.MOSLConstants.MTD;
import static com.mosl.tabapp.constants.MOSLConstants.ROW_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.SELF_KEY;
import static com.mosl.tabapp.constants.MOSLConstants.SYSTEM_NAME;
import static com.mosl.tabapp.constants.MOSLConstants.YTD_CNT;

/**
 * Created by raju on 2/14/2017.
 */

public class ClientsFragment extends CommonFragment {
    private View clientsView;
    static Context mContext;
    private Spinner spClients;
    private DashboardModel dashboardModel;
    private String widgetSystemName;
    private Map addtionalParameterMap = new HashMap<>();
    private ArrayList<HashMap<String, String>> arrayListClient = new ArrayList<HashMap<String, String>>();
    private TableLayout tableLayout;
    boolean dataFoundFlag;

    private Integer familyCount = 0;
    private View pBar;
    private TextView tvNochart;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clientsView = super.onCreateView(inflater, container, savedInstanceState);
        setUpView(clientsView);
        hideAppTitle();
        setUserVisibleHint(true);
        return clientsView;
    }

    @Override protected int getLayout() {
        return R.layout.frag_clients;
    }

    public static ClientsFragment newInstance(Context context) {
        ClientsFragment fragment = new ClientsFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View clientsView) {
        spClients = clientsView.findViewById(R.id.spClients);
        tableLayout = clientsView.findViewById(R.id.tableLayout);
        pBar = clientsView.findViewById(R.id.pBar);
        tvNochart = clientsView.findViewById(R.id.tvNochart);
        spClients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                FilterRowData filter1 = (FilterRowData) spClients.getSelectedItem();
                if (dataFoundFlag) {
                    incentiveDynamicView(filter1.getText(), filter1.getValue());
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        dashboardModel = (DashboardModel) this.getArguments().getSerializable(DASHBOARD_MODEL);
        if (dashboardModel != null) {
            DashboardGroupModel groupModel = dashboardModel.getGroupModel(DashboardSystemModel.getClientSystemName());
            List<WidgetModel> widgetList = groupModel.getWidgetList();
            if (widgetList != null && widgetList.size() > 0) {
                WidgetModel model = widgetList.get(0);
                widgetSystemName = model.getSystemName();

                List<WidgetFilterModel> filterList = model.getWidgetFilterList();
                if (filterList != null && filterList.size() == 1) {
                    ArrayAdapter<FilterRowData> adapter1 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(0).getFilterData());
                    adapter1.setDropDownViewResource(R.layout.mdropdown);
                    spClients.setAdapter(adapter1);
                }
            }
        }


    }

    private Map<String, String> getAddtionalParameterMap() {
        Map<String, String> addtionalParameterMap = new HashMap<>();
        try {
            FilterRowData filter1 = (FilterRowData) spClients.getSelectedItem();
            addtionalParameterMap.put(filter1.getKeyValue(), filter1.getValue());
            addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return addtionalParameterMap;
    }

    public void renderClients(UiModel<ClientItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            clientHideProgress();
            arrayListClient.clear();
            tvNochart.setVisibility(View.VISIBLE);
            showOnFailurePopup(error);
        }

        ClientItem data = uiModel.data;
        if (data != null) {
            clientHideProgress();
            arrayListClient.clear();
            if (data.table1 != null) {
                for (int i = 0; i < data.table1.size(); i++) {
                    Table1Item item = data.table1.get(i);
                    familyCount = item.count;
                    break;
                }
            }

            if (data.table != null) {
                for (int i = 0; i < data.table.size(); i++) {
                    HashMap<String, String> fieldData = new HashMap<String, String>();
                    TableItem item = data.table.get(i);
                    String familyStatus = stringOrEmpty(item.fAMILYSTATUS);
                    String dayCount = stringOrEmpty(item.dAYCNT);
                    String mtdCount = stringOrEmpty(item.mTDCNT);
                    String ytdCnt = stringOrEmpty(item.yTDCNT);
                    fieldData.put(FAMILYSTATUS, familyStatus);
                    fieldData.put(DAY, dayCount);
                    fieldData.put(MTD, mtdCount);
                    fieldData.put(YTD_CNT, ytdCnt);
                    arrayListClient.add(fieldData);
                }
                dataFoundFlag = true;
                FilterRowData filter1 = (FilterRowData) spClients.getSelectedItem();
                incentiveDynamicView(filter1.getText(), filter1.getValue());
            }
        }
    }

    private void webServiceCall() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {

            String businessProfileId = dashboardModel.getBusinessProfileIdForDashboard();
            Map<String, String> additionalMap = getAddtionalParameterMap();

            ClientRequest request = new ClientRequest(
                businessProfileId,
                additionalMap,
                widgetSystemName,
                dashboardModel.getProductId(),
                dashboardModel.getEmployeeCode()
            );
            Disposable subscribe = getSaathiApi().getClients(request)
                .compose(applyUiModel())
                .subscribe(this::renderClients);
        } else {
            noInternetConnectionDialog();
        }
    }

    private String getTableHead(String text) {
        try {
            if (text.equalsIgnoreCase(MTD)) {
                return "MTD\nCount";
            } else {
                return "DAY\nCount";
            }
        } catch (IndexOutOfBoundsException oe) {
            return text;
        }
    }

    private void incentiveDynamicView(String text, final String value) {
        tableLayout.removeAllViews();
        try {
            LayoutInflater inFla = getActivity().getLayoutInflater();
            View tableRowH = inFla.inflate(R.layout.frag_client_top, null);
            TextView tvMTDCOUNT = tableRowH.findViewById(R.id.tvMTDCOUNT);
            TextView tvFamilyCount = tableRowH.findViewById(R.id.tvFamiMappedCount);
            LinearLayout llFamiMappedCount = tableRowH.findViewById(R.id.llFamiMappedCount);
            tvFamilyCount.setText(familyCount.toString());
            llFamiMappedCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addtionalParameterMap.clear();
                    addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());
                    Bundle args = new Bundle();
                    args.putString(SYSTEM_NAME, widgetSystemName);
                    args.putString(LABEL, "Clients");
                    args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                    navigator.openPage(NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD, args,
                        true, false);
                }
            });

            tvMTDCOUNT.setText(getTableHead(text));

            tableLayout.addView(tableRowH);
            if (arrayListClient.size() > 0) {
                tvNochart.setVisibility(View.GONE);
                for (int i = 0; i < arrayListClient.size(); i++) {
                    String filterCount = arrayListClient.get(i).get(text);
                    String ytdCount = arrayListClient.get(i).get(YTD_CNT);
                    String famillyStatus = arrayListClient.get(i).get(FAMILYSTATUS);
                    View tableRow = inFla.inflate(R.layout.frag_client_bottom, null);
                    final TextView tvFamilyStatus = tableRow.findViewById(R.id.tvFamilyStatus);
                    final TextView tvMetMTD = tableRow.findViewById(R.id.tvMetMTD);
                    final TextView tvMetYTD = tableRow.findViewById(R.id.tvMetYTD);

                    tvFamilyStatus.setText(famillyStatus);
                    tvMetMTD.setText(filterCount);
                    tvMetYTD.setText(ytdCount);
                    tvMetMTD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addtionalParameterMap.clear();
                            addtionalParameterMap.putAll(getAddtionalParameterMap());
                            addtionalParameterMap.put(COL_FILTER, value.toUpperCase() + "_CNT");
                            addtionalParameterMap.put(ROW_FILTER, tvFamilyStatus.getText().toString());
                            Bundle args = new Bundle();
                            args.putString(SYSTEM_NAME, widgetSystemName);
                            args.putString(LABEL, "Clients");
                            args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                            if (!tvMetMTD.getText().toString().trim().equalsIgnoreCase("0")) {
                                navigator.openPage(
                                    NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD, args,
                                    true, false);
                            }
                        }
                    });
                    tvMetYTD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addtionalParameterMap.clear();
                            addtionalParameterMap.putAll(getAddtionalParameterMap());
                            addtionalParameterMap.put(COL_FILTER, YTD_CNT);
                            addtionalParameterMap.put(ROW_FILTER, tvFamilyStatus.getText().toString());
                            Bundle args = new Bundle();
                            args.putString(SYSTEM_NAME, widgetSystemName);
                            args.putString(LABEL, "Clients");
                            args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                            if (!tvMetYTD.getText().toString().trim().equalsIgnoreCase("0")) {
                                navigator.openPage(
                                    NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD, args,
                                    true, false);
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

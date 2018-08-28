package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.content.Intent;
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

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by raju on 2/14/2017.
 */

public class ClientsFragment extends MOSLCommonFragment implements View.OnClickListener {
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
        attachLayout(R.layout.frag_clients);
        setUpView(clientsView);
        hideAppTitle(getClass().toString());
        setUserVisibleHint(true);
        return clientsView;
    }

    public static ClientsFragment newInstance(Context context) {
        ClientsFragment fragment = new ClientsFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View clientsView) {
        spClients = (Spinner) clientsView.findViewById(R.id.spClients);
        tableLayout = (TableLayout) clientsView.findViewById(R.id.tableLayout);
        pBar = clientsView.findViewById(R.id.pBar);
        tvNochart = (TextView) clientsView.findViewById(R.id.tvNochart);
        spClients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                FilterRowData filter1 = (FilterRowData) spClients.getSelectedItem();
                //  data(filter1.getText());
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

                //set filter data
                List<WidgetFilterModel> filterList = model.getWidgetFilterList();
                if (filterList != null && filterList.size() == 1) {
                    ArrayAdapter<FilterRowData> adapter1 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(0).getFilterData());
                    adapter1.setDropDownViewResource(R.layout.mdropdown);
                    spClients.setAdapter(adapter1);
                }
            }
        }


    }

    private Map getAddtionalParameterMap() {
        Map addtionalParameterMap = new HashMap<>();
        try {
            FilterRowData filter1 = (FilterRowData) spClients.getSelectedItem();
            addtionalParameterMap.put(filter1.getKeyValue(), filter1.getValue());
            addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());
           /* if(dashboardModel.getName().toLowerCase().endsWith("self")){
                addtionalParameterMap.put(SELF_KEY,"SELF");
            }*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return addtionalParameterMap;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

    }

    private void webServiceCall() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            // showProgress();
            clientShowProgress();
            String serviceMethod = "Dashboard/LoadWidgetAsync";
            Map inputMap = new HashMap<>();

            inputMap.put("AddtionalParameters", getAddtionalParameterMap());
            inputMap.put("WidgetSystemName", widgetSystemName);

            //dashboard for
            inputMap.put("BusinessProfileId", dashboardModel.getBusinessProfileIdForDashboard());
            inputMap.put(PRODUCT_ID, dashboardModel.getProductId());
            inputMap.put(EMPLOYEE_CODE, dashboardModel.getEmployeeCode());

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                clientHideProgress();
                                String status = response.getString(STATUS);
                                arrayListClient.clear();
                             /*   "Table":[{"FAMILYSTATUS":"ACTIVE","DAY_CNT":0,"DAY_PER":0,"MTD_CNT":0,"MTD_PER":0,"YTD_CNT":31786,"YTD_PER":0}*/


                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    JSONObject responseJsonObj = response.getJSONObject("Response");

                                    if (!responseJsonObj.isNull("Table1")) {
                                        JSONArray jsonArray = responseJsonObj.getJSONArray("Table1");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            familyCount = jsonObject.getInt("Count");
                                            break;
                                        }
                                    }

                                    if (responseJsonObj.has("Table")) {
                                        JSONArray jsonArray = responseJsonObj.getJSONArray("Table");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            HashMap<String, String> fieldData = new HashMap<String, String>();
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String familyStatus = (jsonObject.isNull("FAMILYSTATUS") || jsonObject.getString("FAMILYSTATUS").equals("")) ? "" : jsonObject.getString("FAMILYSTATUS");
                                            String dayCount = (jsonObject.isNull("DAY_CNT") || jsonObject.getString("DAY_CNT").equals("")) ? "" : jsonObject.getString("DAY_CNT");
                                            String mtdCount = (jsonObject.isNull("MTD_CNT") || jsonObject.getString("MTD_CNT").equals("")) ? "" : jsonObject.getString("MTD_CNT");
                                            String ytdCnt = (jsonObject.isNull("YTD_CNT") || jsonObject.getString("YTD_CNT").equals("")) ? "" : jsonObject.getString("YTD_CNT");

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


                                } else {
                                    clientHideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                    arrayListClient.clear();
                                    tvNochart.setVisibility(View.VISIBLE);
                                }


                            } else {
                                clientHideProgress();
//                                customProgress.setVisibility(View.GONE);
                                arrayListClient.clear();
                                tvNochart.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            clientHideProgress();
                            arrayListClient.clear();
                            tvNochart.setVisibility(View.VISIBLE);
                            //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            clientHideProgress();
                            arrayListClient.clear();
                            tvNochart.setVisibility(View.VISIBLE);
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                // MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        clientHideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        clientHideProgress();
                    }

                });
            } catch (Exception e) {
                clientHideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), ERROR_LOADING_DATA_FOR + widgetSystemName, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

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
            TextView tvMTDCOUNT = (TextView) tableRowH.findViewById(R.id.tvMTDCOUNT);
            TextView tvYTDCOUNT = (TextView) tableRowH.findViewById(R.id.tvYTDCOUNT);
            TextView tvFamilyCount = (TextView) tableRowH.findViewById(R.id.tvFamiMappedCount);
            LinearLayout llFamiMappedCount = (LinearLayout) tableRowH.findViewById(R.id.llFamiMappedCount);
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
                    navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
                }
            });

            tvMTDCOUNT.setText(getTableHead(text));
            //Boald type face

            tableLayout.addView(tableRowH);
            if (arrayListClient.size() > 0) {
                tvNochart.setVisibility(View.GONE);
                for (int i = 0; i < arrayListClient.size(); i++) {
                    String filterCount = arrayListClient.get(i).get(text);
                    String ytdCount = arrayListClient.get(i).get(YTD_CNT);
                    String famillyStatus = arrayListClient.get(i).get(FAMILYSTATUS);
                    View tableRow = inFla.inflate(R.layout.frag_client_bottom, null);
                    LinearLayout llMet = (LinearLayout) tableRow.findViewById(R.id.llMet);
                    final TextView tvFamilyStatus = (TextView) tableRow.findViewById(R.id.tvFamilyStatus);
                    final TextView tvMetMTD = (TextView) tableRow.findViewById(R.id.tvMetMTD);
                    final TextView tvMetYTD = (TextView) tableRow.findViewById(R.id.tvMetYTD);

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
                            addtionalParameterMap.put(ROW_FILTER, tvFamilyStatus.getText().toString());
                            Bundle args = new Bundle();
                            args.putString(SYSTEM_NAME, widgetSystemName);
                            args.putString(LABEL, "Clients");
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
                webServiceCall();
            }
        } catch (Exception e) {
            e.printStackTrace();
        /*    Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            Crashlytics.logException(e);*/
        }
    }

    private void clientShowProgress() {
        pBar.setVisibility(View.VISIBLE);
    }

    private void clientHideProgress() {
        pBar.setVisibility(View.GONE);
    }
}

package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
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
 * Created by Sanket Gurav on 7/5/2017.
 */

public class ClientsTradedYesterdayFragment extends MOSLCommonFragment implements View.OnClickListener {
    private View clientYesterdayView;
    static Context mContext;
    private DashboardModel dashboardModel;
    private String widgetSystemName;
    private String groupName;
    private Map addtionalParameterMap = new HashMap<>();
    private ArrayList<HashMap<String, String>> arrayListClient = new ArrayList<HashMap<String, String>>();
    private TableLayout tableLayout;
    boolean dataFoundFlag;

    private Integer familyCount = 0;
    private View pBar;
    private TextView tvNochart;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
            DashboardGroupModel groupModel = dashboardModel.getGroupModel(DashboardSystemModel.getClientTradedSystemName());
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

    private Map getAddtionalParameterMap() {
        Map addtionalParameterMap = new HashMap<>();
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
                        try {
                            if (statusCode == 200) {
                                clientHideProgress();
                                String status = response.getString(STATUS);
                                arrayListClient.clear();
                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    JSONObject responseJsonObj = response.getJSONObject("Response");

                                    if (responseJsonObj.has("Table")) {
                                        JSONArray jsonArray = responseJsonObj.getJSONArray("Table");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            HashMap<String, String> fieldData = new HashMap<String, String>();
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String revenueType = (jsonObject.isNull("RevenueType") || jsonObject.getString("RevenueType").equals("")) ? "" : jsonObject.getString("RevenueType");
                                            String NoOfClients = (jsonObject.isNull("NO_OF_CLIENTS") || jsonObject.getString("NO_OF_CLIENTS").equals("")) ? "0" : jsonObject.getString("NO_OF_CLIENTS");
                                            String RevenueGenerated = (jsonObject.isNull("REVENUE_GENERATED") || jsonObject.getString("REVENUE_GENERATED").equals("")) ? "0" : jsonObject.getString("REVENUE_GENERATED");

                                            fieldData.put("REVENUETYPE", revenueType);
                                            fieldData.put("NOOFCLIENTS", NoOfClients);
                                            fieldData.put("REVENUEGENERATED", RevenueGenerated);

                                            arrayListClient.add(fieldData);
                                        }
                                        dataFoundFlag = true;

                                        incentiveDynamicView(arrayListClient);
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
                    final TextView tvNoOfClients = (TextView) tableRow.findViewById(R.id.tvNoOfClients);
                    final TextView tvRevenueGenerated = (TextView) tableRow.findViewById(R.id.tvRevenueGenerated);

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
                            if (!tvRevenueGenerated.getText().toString().trim().equalsIgnoreCase("0")) {
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

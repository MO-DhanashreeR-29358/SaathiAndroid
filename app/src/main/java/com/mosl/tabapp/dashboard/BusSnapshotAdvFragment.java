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
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

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

    private Map getAddtionalParameterMap() {
        Map addtionalParameterMap = new HashMap<>();
        try {
            addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return addtionalParameterMap;
    }


    private void webServiceCall() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            clientShowProgress();
            String serviceMethod = "Dashboard/LoadWidgetAsync";
            Map inputMap = new HashMap<>();

            inputMap.put("AddtionalParameters", getAddtionalParameterMap());
            inputMap.put("WidgetSystemName", widgetSystemName);
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
                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    JSONObject responseJsonObj = response.getJSONObject("Response");


                                    if (responseJsonObj.has("Table")) {
                                        JSONArray jsonArray = responseJsonObj.getJSONArray("Table");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            HashMap<String, String> fieldData = new HashMap<String, String>();
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String familyStatus = (jsonObject.isNull("Parameter") || jsonObject.getString("Parameter").equals("")) ? "" : jsonObject.getString("Parameter");
                                            String mtdCount = (jsonObject.isNull("MTD_CNT") || jsonObject.getString("MTD_CNT").equals("")) ? "" : jsonObject.getString("MTD_CNT");
                                            String ytdCnt = (jsonObject.isNull("YTD_CNT") || jsonObject.getString("YTD_CNT").equals("")) ? "" : jsonObject.getString("YTD_CNT");

                                            fieldData.put(PARAMETER, familyStatus);
                                            fieldData.put(MTD_CNT, mtdCount);
                                            fieldData.put(YTD_CNT, ytdCnt);
                                            arrayListClient.add(fieldData);
                                        }
                                        incentiveDynamicView();
                                    }


                                } else {
                                    clientHideProgress();
                                    arrayListClient.clear();
                                    tvNochart.setVisibility(View.VISIBLE);
                                }


                            } else {
                                clientHideProgress();
                                arrayListClient.clear();
                                tvNochart.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            clientHideProgress();
                            arrayListClient.clear();
                            tvNochart.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            clientHideProgress();
                            arrayListClient.clear();
                            tvNochart.setVisibility(View.VISIBLE);
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
								MSFDialog.alertDialog(getMainActivity(), 0,
									getResources().getString(R.string.motilal_oswal),
									UNAUTHORIZE_USER, OK, false, new MSFDialog.DialogListener() {
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
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
						MSFDialog.alertDialog(getMainActivity(), 0,
							getResources().getString(R.string.motilal_oswal),
							SOME_SERVER_ERROR_OCCURRED, OK, true, null);
                        clientHideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
						MSFDialog.alertDialog(getMainActivity(), 0,
							getResources().getString(R.string.motilal_oswal),
							SOME_SERVER_ERROR_OCCURRED, OK, true, null);
                        clientHideProgress();
                    }

                });
            } catch (Exception e) {
                clientHideProgress();
                e.printStackTrace();
				MSFDialog.alertDialog(getMainActivity(), 0,
					getResources().getString(R.string.motilal_oswal),
					ERROR_LOADING_DATA_FOR + widgetSystemName, OK, true, null);
            }
        } else {
			MSFDialog.alertDialog(getMainActivity(), 0,
				getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, OK, true,
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

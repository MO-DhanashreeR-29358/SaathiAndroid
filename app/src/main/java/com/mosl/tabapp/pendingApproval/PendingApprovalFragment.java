package com.mosl.tabapp.pendingApproval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.PendingApprovalModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sun on 3/8/2017.
 */

public class PendingApprovalFragment extends MOSLCommonFragment implements View.OnClickListener {
    View pendingApprovalView;
    TextView tvApproved, tvApprovedValue, tvPendingApproval, tvPendingApprovalValue;
    private RecyclerView RvPendApproval;
    private ArrayList<PendingApprovalModel> pendingApprovalList;
    private PendingApprovalAdapter pendingApprovalAdapter;

    Bundle args = new Bundle();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pendingApprovalView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_pending_approvals);
        setAppTitle(getClass().toString(), "Pending Approvals");
        setUpView(pendingApprovalView);
        setUserVisibleHint(true);
        return pendingApprovalView;
    }

    private void setUpView(View pendingApprovalView) {
        tvApproved = (TextView) pendingApprovalView.findViewById(R.id.tvApproved);
        tvApprovedValue = (TextView) pendingApprovalView.findViewById(R.id.tvApprovedValue);
        tvPendingApproval = (TextView) pendingApprovalView.findViewById(R.id.tvPendingApproval);
        tvPendingApprovalValue = (TextView) pendingApprovalView.findViewById(R.id.tvPendingApprovalValue);
        RvPendApproval = (RecyclerView) pendingApprovalView.findViewById(R.id.RvPendApproval);
        tvApproved.setText("Approved");
        tvPendingApproval.setText("Pending Approvals");


    }

    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.RvPendApproval:
                navigateTo(NAV_TO_Pending_For_Approval, args, getFragmentManager(), true);
                break;*/
        }
    }

    public void getAprovalCoutSummary() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("EmployeeCode", AccountDetails.getEmployeeCode());
            String serviceMethod = "ApprovalRequest/GetAprovalCoutSummary";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (status.equalsIgnoreCase("true")) {

                                    JSONObject jsonObject = response.getJSONObject(RESPONSE);
                                   /* {"Response":{"ApprovedCount":0,"PendingCount":0},"Message":"Process Completed Successfully","Status":true,"Erros":[]}*/

                                    String ApprovedCount = (jsonObject.isNull("ApprovedCount") || jsonObject.getString("ApprovedCount").equals("")) ? "0" : jsonObject.getString("ApprovedCount");
                                    String PendingCount = (jsonObject.isNull("PendingCount") || jsonObject.getString("PendingCount").equals("")) ? "0" : jsonObject.getString("PendingCount");

                                    tvApprovedValue.setText(ApprovedCount);
                                    tvPendingApprovalValue.setText(PendingCount);

                                   /* JSONArray dataArray = jsonObject.getJSONArray(DATA);

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject objDetail = dataArray.getJSONObject(i);
                                        String Status = (objDetail.isNull(STATUS) || objDetail.getString(STATUS).equals("")) ? "" : objDetail.getString(STATUS);
                                        String MeetingID = (objDetail.isNull("MeetingID") || objDetail.getString("MeetingID").equals("")) ? "" : objDetail.getString("MeetingID");
                                        String LeadID = (objDetail.isNull("LeadID") || objDetail.getString("LeadID").equals("")) ? "" : objDetail.getString("LeadID");
                                        String Name = (objDetail.isNull(NAME) || objDetail.getString(NAME).equals("")) ? "" : objDetail.getString(NAME);
                                        String EMAIL = (objDetail.isNull("EMAIL") || objDetail.getString("EMAIL").equals("")) ? "" : objDetail.getString("EMAIL");
                                        String mobNo = (objDetail.isNull(MOBNO) || objDetail.getString(MOBNO).equals("")) ? "" : objDetail.getString(MOBNO);
                                        String date = (objDetail.isNull("DATE") || objDetail.getString("DATE").equals("")) ? "" : objDetail.getString("DATE");
                                        String time = (objDetail.isNull("TIME") || objDetail.getString("TIME").equals("")) ? "" : objDetail.getString("TIME");
                                        String AttendeeEmp = (objDetail.isNull("AttendeeEmp") || objDetail.getString("AttendeeEmp").equals("")) ? "" : objDetail.getString("AttendeeEmp");
                                        String Product = (objDetail.isNull("Product") || objDetail.getString("Product").equals("")) ? "" : objDetail.getString("Product");
                                        String StatusName = (objDetail.isNull("StatusName") || objDetail.getString("StatusName").equals("")) ? "" : objDetail.getString("StatusName");
                                        String ActivityType = (objDetail.isNull("ActivityType") || objDetail.getString("ActivityType").equals("")) ? "" : objDetail.getString("ActivityType");
                                        String MeetingReason = (objDetail.isNull("MeetingReason") || objDetail.getString("MeetingReason").equals("")) ? "" : objDetail.getString("MeetingReason");
                                        String MeetingDuration = (objDetail.isNull("MeetingDuration") || objDetail.getString("MeetingDuration").equals("")) ? "" : objDetail.getString("MeetingDuration");
                                        String Category = (objDetail.isNull("Category") || objDetail.getString("Category").equals("")) ? "" : objDetail.getString("Category");
                                        String Actionable = (objDetail.isNull("Actionable") || objDetail.getString("Actionable").equals("")) ? "" : objDetail.getString("Actionable");
                                        String Comments = (objDetail.isNull("Comments") || objDetail.getString("Comments").equals("")) ? "" : objDetail.getString("Comments");
                                        String Stage = (objDetail.isNull("Stage") || objDetail.getString("Stage").equals("")) ? "" : objDetail.getString("Stage");
                                        String CSR = (objDetail.isNull("CustomerServiceRemark") || objDetail.getString("CustomerServiceRemark").equals("")) ? "" : objDetail.getString("CustomerServiceRemark");
                                        String QueryDesc = (objDetail.isNull("QueryDesc") || objDetail.getString("QueryDesc").equals("")) ? "" : objDetail.getString("QueryDesc");


                                    }*/
                                   /* calendarAdapter = new CalendarFragment.CalendarAdapter(calenderList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setAdapter(calendarAdapter);
                                    CalenderModel.setCalenderList(calenderList);*/
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
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
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }


    public void getAprovalList() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("EmployeeCode", AccountDetails.getEmployeeCode());
            String serviceMethod = "ApprovalRequest/GetApprovalFieldListSummaryByEmployeeCode";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                pendingApprovalList = new ArrayList<>();
                                if (status.equalsIgnoreCase("true")) {
                                    hideProgress();
                                    //  JSONObject jsonObject = response.getJSONObject(RESPONSE);
                                   /* {"Response":{"ApprovedCount":0,"PendingCount":0},"Message":"Process Completed Successfully","Status":true,"Erros":[]}*/

                                    JSONArray dataArray = response.getJSONArray(RESPONSE);
                                  /*  {"Response":[{"PandingCount":7,"ReferenceName":"Company Name"}]*/

                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject objDetail = dataArray.getJSONObject(i);
                                        String pandingCount = (objDetail.isNull("PandingCount") || objDetail.getString("PandingCount").equals("")) ? "" : objDetail.getString("PandingCount");
                                        String referenceName = (objDetail.isNull("ReferenceName") || objDetail.getString("ReferenceName").equals("")) ? "" : objDetail.getString("ReferenceName");
                                        String entityField = (objDetail.isNull("EntityField") || objDetail.getString("EntityField").equals("")) ? "" : objDetail.getString("EntityField");


                                        pendingApprovalList.add(new PendingApprovalModel(referenceName, pandingCount, entityField));
                                    }
                                    pendingApprovalAdapter = new PendingApprovalAdapter(pendingApprovalList);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                    RvPendApproval.setLayoutManager(mLayoutManager);
                                    RvPendApproval.setAdapter(pendingApprovalAdapter);

                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
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
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    public class PendingApprovalAdapter extends RecyclerView.Adapter<PendingApprovalAdapter.MyViewHolder1> {

        private List<PendingApprovalModel> pendingApprovalList;

        public class MyViewHolder1 extends RecyclerView.ViewHolder {
            public TextView tvpendinglead;
            public TextView tvpendingcount;
            public LinearLayout llcounter;

            public MyViewHolder1(View view) {
                super(view);
                tvpendinglead = (TextView) view.findViewById(R.id.tvpendinglead);
                tvpendingcount = (TextView) view.findViewById(R.id.tvpendingcount);
                llcounter = (LinearLayout) view.findViewById(R.id.llcounter);

            }
        }


        public PendingApprovalAdapter(List<PendingApprovalModel> mpendingApprovalList) {
            this.pendingApprovalList = mpendingApprovalList;
        }

        @Override
        public PendingApprovalAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_pending_approvals, parent, false);

            return new PendingApprovalAdapter.MyViewHolder1(itemView);
        }

        @Override
        public void onBindViewHolder(PendingApprovalAdapter.MyViewHolder1 holder, int position) {
            final PendingApprovalModel pendingApprovalModel = pendingApprovalList.get(position);
            holder.tvpendinglead.setText(pendingApprovalModel.getmPendingLead());
            holder.tvpendingcount.setText(pendingApprovalModel.getmPendingLeadCount());
            holder.llcounter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putString(ENTITY_FIELD, pendingApprovalModel.getEntityField());
                    navigateTo(NAV_TO_PENDING_FOR_APPROVAL, args, getFragmentManager(), true);
                }
            });
        }

        @Override
        public int getItemCount() {
            return pendingApprovalList.size();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && pendingApprovalView != null) {
                getAprovalCoutSummary();
                getAprovalList();
            }
        } catch (Exception e) {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
        }
    }
}


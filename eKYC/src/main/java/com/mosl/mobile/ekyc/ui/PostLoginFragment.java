package com.mosl.mobile.ekyc.ui;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.CustomTabsHelper;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.model.BranchLeadsModel;
import com.mosl.mobile.ekyc.model.CommonModel;
import com.mosl.mobile.ekyc.model.FranchiseLeadsModel;
import com.mosl.mobile.ekyc.model.RoleSelectionModel;
import com.mosl.mobile.ekyc.register.PANAdharRegistActivity;
import com.mosl.mobile.ekyc.register.RegisterActivity;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import cz.msebera.android.httpclient.Header;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import timber.log.Timber;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setExistingSegment;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.clearAllCommonModelFields;
import static com.mosl.mobile.ekyc.model.CommonModel.getAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getPANno;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.isAddNewSegment;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommoditySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquitySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isMfSegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isOffline;
import static com.mosl.mobile.ekyc.model.CommonModel.setAadhaarByPass;
import static com.mosl.mobile.ekyc.model.CommonModel.setAadharNo;
import static com.mosl.mobile.ekyc.model.CommonModel.setAddNewSegment;
import static com.mosl.mobile.ekyc.model.CommonModel.setAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.setBaFranchiseeCode;
import static com.mosl.mobile.ekyc.model.CommonModel.setBioMetricVisibleForBranch;
import static com.mosl.mobile.ekyc.model.CommonModel.setCity;
import static com.mosl.mobile.ekyc.model.CommonModel.setDob;
import static com.mosl.mobile.ekyc.model.CommonModel.setDownloadPdfUrl;
import static com.mosl.mobile.ekyc.model.CommonModel.setEqSchemeId;
import static com.mosl.mobile.ekyc.model.CommonModel.setEsignService;
import static com.mosl.mobile.ekyc.model.CommonModel.setGotoFundTransfer;
import static com.mosl.mobile.ekyc.model.CommonModel.setGotoReviewEdit;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAllOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAllSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsBankDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommodityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommoditySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCurrencyOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsDerivativeOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquityIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquitySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsIPVDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsMfIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsNomineeMandatory;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsOffline;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsOfflineCliencodeGenrated;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsOfflineDocumentUploadedDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsPersonalDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsReviewEditDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsSchemeDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsSegmentSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsWorkDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setOtpVisibleForBranch;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANName;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANno;
import static com.mosl.mobile.ekyc.model.CommonModel.setRegisteredName;
import static com.mosl.mobile.ekyc.model.CommonModel.setSipIntent;
import static com.mosl.mobile.ekyc.model.CommonModel.setTokenKey;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserType;

/**
 * Created by raju on 6/24/2016.
 */
public class PostLoginFragment extends Fragment implements View.OnClickListener, MOSLConstants {
    View postView;
    ListView lvLeads;
    TextView tvInProCount, tv, tvLast, tvThirtyDays, tvThirtyText, tvHeading, tvNoResponse, tvOwn, tvDirect, tvVerificationStage, tvProcessTotal, tvVerificationTotal,
            tvCompleted, tvTotal;
    RelativeLayout rlInProcess, rlNotInitiated;
    Button btnGoToKYC, btnCollateral, btnCRM;
    FranchiseAdapter franchiseAdapter;
    BranchAdapter branchAdapter;
    ArrayList<HashMap<String, String>> PostLoginList;
    public LinearLayout llPostLogin;
    List<BranchLeadsModel> branchLeadsModels = new ArrayList<>();
    List<FranchiseLeadsModel> franchiseLeadsModels = new ArrayList<>();
    LinearLayout llButton, llNoResponse, llPostLoginTotal;
    public static Dialog dialog;
    Map<String, Map<String, String>> allStageMap;
    boolean prospectTable;
    Context postLoginContext;
    private CustomTabsClient mClient;
    String clientType = OWN, verficationStation = "0";
    boolean onClickedVarificationStage, isCompletedForms;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            postView = inflater.inflate(R.layout.fragment_postlogin, container, false);
            setPermisson(getActivity(), postLoginContext);
            setUpView(postView);
            PostLoginActivity.check = 0;
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
        return postView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        postLoginContext = context;
    }

    private void setUpView(View postView) {
        btnGoToKYC = (Button) postView.findViewById(R.id.btnEKYC);
        btnCollateral = (Button) postView.findViewById(R.id.btnCollateral);
        btnCRM = (Button) postView.findViewById(R.id.btnCRM);

        tvInProCount = (TextView) postView.findViewById(R.id.tvInProCount);
        tv = (TextView) postView.findViewById(R.id.tv);
        tvLast = (TextView) postView.findViewById(R.id.tvLast);
        tvThirtyDays = (TextView) postView.findViewById(R.id.tvThirtyDays);
        tvThirtyText = (TextView) postView.findViewById(R.id.tvThirtyText);
        tvHeading = (TextView) postView.findViewById(R.id.tvHeading);
        tvNoResponse = (TextView) postView.findViewById(R.id.tvNoResponse);
        tvOwn = (TextView) postView.findViewById(R.id.tvOwn);
        tvDirect = (TextView) postView.findViewById(R.id.tvDirect);
        tvVerificationStage = (TextView) postView.findViewById(R.id.tvVerificationStage);
        tvProcessTotal = (TextView) postView.findViewById(R.id.tvProcessTotal);
        tvVerificationTotal = (TextView) postView.findViewById(R.id.tvVerificationTotal);
        tvCompleted = (TextView) postView.findViewById(R.id.tvCompleted);
        tvTotal = (TextView) postView.findViewById(R.id.tvTotal);

        rlInProcess = (RelativeLayout) postView.findViewById(R.id.rlInProcess);
        rlNotInitiated = (RelativeLayout) postView.findViewById(R.id.rlNotInitiated);

        llPostLogin = (LinearLayout) postView.findViewById(R.id.llPostLogin);
        llButton = (LinearLayout) postView.findViewById(R.id.llButton);
        llNoResponse = (LinearLayout) postView.findViewById(R.id.llNoResponse);
        llPostLoginTotal = (LinearLayout) postView.findViewById(R.id.llPostLoginTotal);

        lvLeads = (ListView) postView.findViewById(R.id.lvLeads);

        tvVerificationStage.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvHeading.setText(PENDING_EKYC_IN_PROCESS_FORMS);
        rlInProcess.setOnClickListener(this);
        rlNotInitiated.setOnClickListener(this);
        btnGoToKYC.setOnClickListener(this);
        btnCollateral.setOnClickListener(this);
        btnCRM.setOnClickListener(this);
        tvOwn.setOnClickListener(this);
        tvDirect.setOnClickListener(this);
        tvVerificationStage.setOnClickListener(this);

        PostLoginList = new ArrayList<HashMap<String, String>>();
        franchiseLeadsModels.clear();
        lvLeads.setAdapter(null);

        if (getAppType().equalsIgnoreCase(PP)) {
            llPostLoginTotal.setVisibility(View.VISIBLE);
            llButton.setVisibility(View.VISIBLE);
            tvThirtyText.setText(R.string.ba_postlogin_status);
            inProcessPPLeadsCall();
        } else if (getAppType().equalsIgnoreCase(CRM)) {
            llPostLoginTotal.setVisibility(View.GONE);
            llButton.setVisibility(View.VISIBLE);
//            tvThirtyText.setText(R.string.branch_postlogin_status);
            tvThirtyText.setText(R.string.ba_postlogin_status);
            inProcessCRMLeadsCall();
        }
        try {
            CustomTabsServiceConnection mConnection = new CustomTabsServiceConnection() {
                @Override
                public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                    mClient = customTabsClient;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    mClient = null;
                }
            };
            String packag = CustomTabsHelper.getPackageNameToUse(getContext());
            CustomTabsClient.bindCustomTabsService(getContext(), packag, mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void completedLeadsPPCall() {

        Map inputMap = new HashMap<>();
        inputMap.put(APP_LOGIN_ID, CommonModel.getAppLoginID());
        inputMap.put(LEAD_TYPE, COMPLETED);
        inputMap.put(SEARCH_DATA, "");
        inputMap.put(APPLICATION_TYPE, PP);
        inputMap.put(CLIENTTYPE, clientType);
        inputMap.put(VERIFICATIONSTAGE, verficationStation);

        String flag = EKYCINPROCESSFORMSPP;
        String category = SIGNUP;
        franchiseLeadsModels.clear();
        if (MOSLCommon.isNetworkEnabled(postLoginContext)) {
            PostLoginActivity.customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(postLoginContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    super.onSuccess(statusCode, headers, response);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            llNoResponse.setVisibility(View.GONE);
                            lvLeads.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = response.getJSONArray(EKYCINPROCESSFORMSPP);
                            if (jsonArray.length() != 0) {


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String userID = (object.isNull("UserID") || object.getString("UserID").equals("")) ? "" : object.getString("UserID");
                                    BigDecimal bd = new BigDecimal(userID);
                                    String clientName = (object.isNull("ClientName") || object.getString("ClientName").equals("")) ? "" : object.getString("ClientName");
                                    String mobNo = (object.isNull("MobileNo") || object.getString("MobileNo").equals("")) ? "" : object.getString("MobileNo");
                                    String modifiedDate = (object.isNull("ModifiedDate") || object.getString("ModifiedDate").equals("")) ? "" : object.getString("ModifiedDate");
                                    String clientStatus = (object.isNull("ClientStatus") || object.getString("ClientStatus").equals("")) ? "" : object.getString("ClientStatus");
                                    String numAccount = (object.isNull("NumAccount") || object.getString("NumAccount").equals("")) ? "" : object.getString("NumAccount");
                                    String clientCode = (object.isNull("Clientcode") || object.getString("Clientcode").equals("")) ? "" : object.getString("Clientcode");
                                    String baCode = (object.isNull("BACODE") || object.getString("BACODE").equals("")) ? "" : object.getString("BACODE");

                                    franchiseLeadsModels.add(new FranchiseLeadsModel(String.valueOf(bd.longValue()), clientName, mobNo, modifiedDate, clientStatus, numAccount,
                                            clientCode, baCode));
                                }
                                respAvailable();
                                franchiseAdapter = new FranchiseAdapter(postLoginContext, franchiseLeadsModels);
                                lvLeads.setAdapter(franchiseAdapter);
                                franchiseAdapter.notifyDataSetChanged();
                            } else {
                                noResponse(NO_LEADS);
                            }

                            if (response.has("EKYCINPROCESSFORMSPP1")) {
                                JSONArray jsonArray1 = response.getJSONArray("EKYCINPROCESSFORMSPP1");
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                    String totalCount = (jsonObject.isNull("TotalCount") || jsonObject.getString("TotalCount").equals("")) ? "" : jsonObject.getString("TotalCount");
                                    String verificationStage = (jsonObject.isNull("VerificationStage") || jsonObject.getString("VerificationStage").equals("")) ? "" : jsonObject.getString("VerificationStage");
                                    String inProcess = (jsonObject.isNull("InProcess") || jsonObject.getString("InProcess").equals("")) ? "" : jsonObject.getString("InProcess");
                                    String completed = (jsonObject.isNull("Completed") || jsonObject.getString("Completed").equals("")) ? "" : jsonObject.getString("Completed");
                                    tvVerificationTotal.setText(verificationStage);
                                    tvProcessTotal.setText(inProcess);
                                    tvCompleted.setText(completed);
                                    tvTotal.setText(totalCount);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        noResponse(NO_LEADS);
                    }
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    noResponse(responseString);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            PostLoginActivity.customProgress.setVisibility(View.GONE);
            MOSLCommon.alertDInternet(postLoginContext);
        }
    }

    private void inProcessPPLeadsCall() {


        Map inputMap = new HashMap<>();
        inputMap.put(APP_LOGIN_ID, CommonModel.getAppLoginID());
        inputMap.put(LEAD_TYPE, PENDING);
        inputMap.put(SEARCH_DATA, "");
        inputMap.put(APPLICATION_TYPE, PP);
        inputMap.put(CLIENTTYPE, clientType);
        inputMap.put(VERIFICATIONSTAGE, verficationStation);

        String flag = EKYCINPROCESSFORMSPP;
        String category = SIGNUP;
        franchiseLeadsModels.clear();
        if (MOSLCommon.isNetworkEnabled(postLoginContext)) {
            PostLoginActivity.customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(postLoginContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                            super.onSuccess(statusCode, headers, response);
                            PostLoginActivity.customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            if (response != null) {
                                try {

                                    JSONArray jsonArray = response.getJSONArray(EKYCINPROCESSFORMSPP);
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String inProcessCount = String.valueOf(jsonArray.length());
                                            tvInProCount.setText(inProcessCount);
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            String userID = (object.isNull("UserID") || object.getString("UserID").equals("")) ? "" : object.getString("UserID");
                                            String clientName = (object.isNull("ClientName") || object.getString("ClientName").equals("")) ? "" : object.getString("ClientName");
                                            String mobNo = (object.isNull("MobileNo") || object.getString("MobileNo").equals("")) ? "" : object.getString("MobileNo");
                                            String modifiedDate = (object.isNull("ModifiedDate") || object.getString("ModifiedDate").equals("")) ? "" : object.getString("ModifiedDate");
                                            String clientStatus = (object.isNull("ClientStatus") || object.getString("ClientStatus").equals("")) ? "" : object.getString("ClientStatus");
                                            String numAccount = (object.isNull("NumAccount") || object.getString("NumAccount").equals("")) ? "" : object.getString("NumAccount");
                                            String clientCode = (object.isNull("Clientcode") || object.getString("Clientcode").equals("")) ? "" : object.getString("Clientcode");
                                            String baCode = (object.isNull("BACODE") || object.getString("BACODE").equals("")) ? "" : object.getString("BACODE");

                                            franchiseLeadsModels.add(new FranchiseLeadsModel(userID, clientName, mobNo, modifiedDate, clientStatus, numAccount,
                                                    clientCode, baCode));
                                        }

                                        respAvailable();
                                        franchiseAdapter = new FranchiseAdapter(postLoginContext, franchiseLeadsModels);
                                        lvLeads.setAdapter(franchiseAdapter);
                                        franchiseAdapter.notifyDataSetChanged();
                                    } else {
                                        noResponse(NO_LEADS);
                                    }

                                    if (response.has("EKYCINPROCESSFORMSPP1")) {
                                        JSONArray jsonArray1 = response.getJSONArray("EKYCINPROCESSFORMSPP1");
                                        for (int i = 0; i < jsonArray1.length(); i++) {
                                            JSONObject jsonObject = jsonArray1.getJSONObject(i);
                                            String totalCount = (jsonObject.isNull("TotalCount") || jsonObject.getString("TotalCount").equals("")) ? "" : jsonObject.getString("TotalCount");
                                            String verificationStage = (jsonObject.isNull("VerificationStage") || jsonObject.getString("VerificationStage").equals("")) ? "" : jsonObject.getString("VerificationStage");
                                            String inProcess = (jsonObject.isNull("InProcess") || jsonObject.getString("InProcess").equals("")) ? "" : jsonObject.getString("InProcess");
                                            String completed = (jsonObject.isNull("Completed") || jsonObject.getString("Completed").equals("")) ? "" : jsonObject.getString("Completed");
                                            tvVerificationTotal.setText(verificationStage);
                                            tvProcessTotal.setText(inProcess);
                                            tvCompleted.setText(completed);
                                            tvTotal.setText(totalCount);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                noResponse(NO_LEADS);
                            }


                            PostLoginActivity.customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            PostLoginActivity.customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String
                                responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            PostLoginActivity.customProgress.setVisibility(View.GONE);
                            noResponse(responseString);
                            commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable
                                throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            PostLoginActivity.customProgress.setVisibility(View.GONE);
                            commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable
                                throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            PostLoginActivity.customProgress.setVisibility(View.GONE);
                            commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }
                    }

            );
        } else {
            MOSLCommon.alertDInternet(postLoginContext);
            PostLoginActivity.customProgress.setVisibility(View.GONE);
        }
    }

    private void inProcessCRMLeadsCall() {

        Map inputMap = new HashMap<>();
        inputMap.put(APP_LOGIN_ID, CommonModel.getAppLoginID());
        inputMap.put(LEAD_TYPE, EKYC);
        inputMap.put(SEARCH_DATA, "");
        inputMap.put(APPLICATION_TYPE, CRM);
        inputMap.put(ROLE_MAPPING_ID, RoleSelectionModel.getRoleMappingID());
        inputMap.put(BUSINESS_UNIT_ID, RoleSelectionModel.getBusinessUnitID());

        String flag = EKYCINPROCESSFORMSCRM;
        String category = SIGNUP;
        branchLeadsModels.clear();

        if (MOSLCommon.isNetworkEnabled(postLoginContext)) {
            PostLoginActivity.customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(postLoginContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    super.onSuccess(statusCode, headers, response);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);


                    if (response != null) {
                        try {

                          /*"LEADID":1802806,
                            "STAGE":"C1",
                            "STATUS":"First Meeting Done",
                            "LEADNAME":"Bhupat Punabhai Baria ",
                            "MOBILENO":"8828343505",
                            "SORTDATE":"2016-08-31T16:58:39",
                            "CREATEDSORT":"2016-08-31T16:58:39",
                            "MODIFIEDDATE":"31/08/2016 16:58:39",

                            "LASTMODIFIEDON":"Aug 31 2016 4:58PM",
                            "CREATEDON":"31/08/2016 16:58:39",
                            "CREATEDDATE":"Aug 31 2016 4:58PM",
                            "USERID":"0",
                            "IS_AADHAR":false,
                            "APPLICATIONFROM":""*/
                            JSONArray jsonArray = response.getJSONArray(EKYCINPROCESSFORMSCRM);
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String inProcessCount = String.valueOf(jsonArray.length());
                                    tvInProCount.setText(inProcessCount);
                                    String leadID = (object.isNull("LEADID") || object.getString("LEADID").equals("")) ? "" : object.getString("LEADID");
                                    String stage = (object.isNull("STAGE") || object.getString("STAGE").equals("")) ? "" : object.getString("STAGE");
                                    String status = (object.isNull("STATUS") || object.getString("STATUS").equals("")) ? "" : object.getString("STATUS");
                                    String leadName = (object.isNull("LEADNAME") || object.getString("LEADNAME").equals("")) ? "" : object.getString("LEADNAME");
                                    String mobNo = (object.isNull("MOBILENO") || object.getString("MOBILENO").equals("")) ? "" : object.getString("MOBILENO");
                                    String sortDate = (object.isNull("SORTDATE") || object.getString("SORTDATE").equals("")) ? "" : object.getString("SORTDATE");
                                    String createdSort = (object.isNull("CREATEDSORT") || object.getString("CREATEDSORT").equals("")) ? "" : object.getString("CREATEDSORT");
                                    String modifiedDate = (object.isNull("MODIFIEDDATE") || object.getString("MODIFIEDDATE").equals("")) ? "" : object.getString("MODIFIEDDATE");
                                    String lastModifiedOn = (object.isNull("LASTMODIFIEDON") || object.getString("LASTMODIFIEDON").equals("")) ? "" : object.getString("LASTMODIFIEDON");
                                    String createdOn = (object.isNull("CREATEDON") || object.getString("CREATEDON").equals("")) ? "" : object.getString("CREATEDON");
                                    String createdDate = (object.isNull("CREATEDDATE") || object.getString("CREATEDDATE").equals("")) ? "" : object.getString("CREATEDDATE");
                                    String userID = (object.isNull("USERID") || object.getString("USERID").equals("")) ? "" : object.getString("USERID");
                                    String isAadhar = (object.isNull("IS_AADHAR") || object.getString("IS_AADHAR").equals("")) ? "" : object.getString("IS_AADHAR");

                                    branchLeadsModels.add(new BranchLeadsModel(leadID, leadName, mobNo, stage, status, createdDate, userID, isAadhar));


                                }
                                respAvailable();
                                branchAdapter = new BranchAdapter(postLoginContext, branchLeadsModels);
                                lvLeads.setAdapter(branchAdapter);
                                branchAdapter.notifyDataSetChanged();
                            } else {
                                noResponse(NO_LEADS);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        noResponse(NO_LEADS);
                    }


                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    noResponse(responseString);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            PostLoginActivity.customProgress.setVisibility(View.GONE);
            MOSLCommon.alertDInternet(postLoginContext);
        }
    }

    private void notInitiatedCRMLeadsCall() {


        Map inputMap = new HashMap<>();
        inputMap.put(APP_LOGIN_ID, getAppLoginID());
        inputMap.put(LEAD_TYPE, PENDING);
        inputMap.put(SEARCH_DATA, "");
        inputMap.put(APPLICATION_TYPE, CRM);
        inputMap.put(ROLE_MAPPING_ID, RoleSelectionModel.getRoleMappingID());
        inputMap.put(BUSINESS_UNIT_ID, RoleSelectionModel.getBusinessUnitID());

        String flag = EKYCINPROCESSFORMSCRM;
        String category = SIGNUP;
        branchLeadsModels.clear();
        if (MOSLCommon.isNetworkEnabled(postLoginContext)) {
            PostLoginActivity.customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(postLoginContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    super.onSuccess(statusCode, headers, response);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response != null) {
                        try {

                          /*"LEADID":1802806,
                            "STAGE":"C1",
                            "STATUS":"First Meeting Done",
                            "LEADNAME":"Bhupat Punabhai Baria ",
                            "MOBILENO":"8828343505",
                            "SORTDATE":"2016-08-31T16:58:39",
                            "CREATEDSORT":"2016-08-31T16:58:39",
                            "MODIFIEDDATE":"31/08/2016 16:58:39",

                            "LASTMODIFIEDON":"Aug 31 2016 4:58PM",
                            "CREATEDON":"31/08/2016 16:58:39",
                            "CREATEDDATE":"Aug 31 2016 4:58PM",
                            "USERID":"0",
                            "IS_AADHAR":false,
                            "APPLICATIONFROM":""*/
                            JSONArray jsonArray = response.getJSONArray(EKYCINPROCESSFORMSCRM);
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);


                                    String leadID = (object.isNull("LEADID") || object.getString("LEADID").equals("")) ? "" : object.getString("LEADID");
                                    String stage = (object.isNull("STAGE") || object.getString("STAGE").equals("")) ? "" : object.getString("STAGE");
                                    String status = (object.isNull("STATUS") || object.getString("STATUS").equals("")) ? "" : object.getString("STATUS");
                                    String leadName = (object.isNull("LEADNAME") || object.getString("LEADNAME").equals("")) ? "" : object.getString("LEADNAME");
                                    String mobNo = (object.isNull("MOBILENO") || object.getString("MOBILENO").equals("")) ? "" : object.getString("MOBILENO");
                                    String sortDate = (object.isNull("SORTDATE") || object.getString("SORTDATE").equals("")) ? "" : object.getString("SORTDATE");
                                    String createdSort = (object.isNull("CREATEDSORT") || object.getString("CREATEDSORT").equals("")) ? "" : object.getString("CREATEDSORT");
                                    String modifiedDate = (object.isNull("MODIFIEDDATE") || object.getString("MODIFIEDDATE").equals("")) ? "" : object.getString("MODIFIEDDATE");
                                    String lastModifiedOn = (object.isNull("LASTMODIFIEDON") || object.getString("LASTMODIFIEDON").equals("")) ? "" : object.getString("LASTMODIFIEDON");
                                    String createdOn = (object.isNull("CREATEDON") || object.getString("CREATEDON").equals("")) ? "" : object.getString("CREATEDON");
                                    String createdDate = (object.isNull("CREATEDDATE") || object.getString("CREATEDDATE").equals("")) ? "" : object.getString("CREATEDDATE");
                                    String userID = (object.isNull("USERID") || object.getString("USERID").equals("")) ? "" : object.getString("USERID");
                                    String isAadhar = (object.isNull("IS_AADHAR") || object.getString("IS_AADHAR").equals("")) ? "" : object.getString("IS_AADHAR");


                                    branchLeadsModels.add(new BranchLeadsModel(leadID, leadName, mobNo, stage, status, createdDate, userID, isAadhar));


                                }
                                respAvailable();
                                branchAdapter = new BranchAdapter(postLoginContext, branchLeadsModels);
                                lvLeads.setAdapter(branchAdapter);
                                branchAdapter.notifyDataSetChanged();
                            } else {
                                noResponse(NO_LEADS);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        noResponse(NO_LEADS);
                    }


                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    noResponse(responseString);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            PostLoginActivity.customProgress.setVisibility(View.GONE);
            MOSLCommon.alertDInternet(postLoginContext);
        }
    }

    private CustomTabsSession getSession() {
        return mClient.newSession(new CustomTabsCallback() {
            @Override
            public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
            }
        });
    }

    private void openCustomTabsIntent(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(getResources().getColor(R.color.secondaryBackground));
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_back_arrow));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
    }

    public void openInBrowser(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnEKYC) {
            Intent intent = new Intent(postLoginContext, RegisterActivity.class);
            startActivity(intent);
        } else if (id == R.id.btnCollateral) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                String packag = CustomTabsHelper.getPackageNameToUse(getContext());
                if (packag == null) {
                    //   openWebview(null, url);
                    Intent intent = new Intent(postLoginContext, WebActivity.class);
                    intent.putExtra(URL_PATH, WebServiceHandler.COLLATERAL_URL);
                    startActivity(intent);
                } else {
                    openCustomTabsIntent(WebServiceHandler.COLLATERAL_URL);
                }
            } else {
                openInBrowser(Uri.parse(WebServiceHandler.COLLATERAL_URL));
            }
            /*Intent intent = new Intent(postLoginContext, CollateralActivity.class);
            startActivity(intent);*/
        } else if (id == R.id.btnCRM) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(WebServiceHandler.CRM_URL));
            startActivity(browserIntent);
//            Intent intent = new Intent(postLoginContext, WebActivity.class);
//            intent.putExtra(URL_PATH,WebServiceHandler.CRM_URL_UAT);
//            startActivity(intent);
        } else if (id == R.id.rlInProcess) {
            tvHeading.setText(PENDING_EKYC_IN_PROCESS_FORMS);
            tvInProCount.setTextColor(getResources().getColor(R.color.black));
            tv.setTextColor(getResources().getColor(R.color.black));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                rlNotInitiated.setBackground(ContextCompat.getDrawable(postLoginContext, R.drawable.hexagon_gray));
                rlInProcess.setBackground(ContextCompat.getDrawable(postLoginContext, R.drawable.hexagonal));
            } else {
                rlNotInitiated.setBackgroundDrawable(ContextCompat.getDrawable(postLoginContext, R.drawable.hexagon_gray));
                rlInProcess.setBackgroundDrawable(ContextCompat.getDrawable(postLoginContext, R.drawable.hexagonal));
            }

            tvLast.setTextColor(ContextCompat.getColor(postLoginContext, R.color.white));
            tvThirtyDays.setTextColor(ContextCompat.getColor(postLoginContext, R.color.white));
            tvThirtyText.setTextColor(ContextCompat.getColor(postLoginContext, R.color.white));
            if (getAppType().equalsIgnoreCase(CRM)) {
                inProcessCRMLeadsCall();
            } else if (getAppType().equalsIgnoreCase(PP)) {
                isCompletedForms = false;
                inProcessPPLeadsCall();
            }

        } else if (id == R.id.rlNotInitiated) {
            if (getAppType().equals(CRM)) {
//                tvHeading.setText(NOT_INITIATED_LEADS);
                tvHeading.setText(CONTINUE_WITH_FORM);
            } else {
                tvHeading.setText(CONTINUE_WITH_FORM);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                rlNotInitiated.setBackground(ContextCompat.getDrawable(postLoginContext, R.drawable.hexagonal));
                rlInProcess.setBackground(ContextCompat.getDrawable(postLoginContext, R.drawable.hexagon_gray));
            } else {
                rlNotInitiated.setBackgroundDrawable(ContextCompat.getDrawable(postLoginContext, R.drawable.hexagonal));
                rlInProcess.setBackgroundDrawable(ContextCompat.getDrawable(postLoginContext, R.drawable.hexagon_gray));
            }
            tvInProCount.setTextColor(ContextCompat.getColor(postLoginContext, R.color.white));
            tv.setTextColor(ContextCompat.getColor(postLoginContext, R.color.white));
            tvLast.setTextColor(ContextCompat.getColor(postLoginContext, R.color.black));
            tvThirtyDays.setTextColor(ContextCompat.getColor(postLoginContext, R.color.black));
            tvThirtyText.setTextColor(ContextCompat.getColor(postLoginContext, R.color.black));

            franchiseLeadsModels.clear();
            if (getAppType().equalsIgnoreCase(CRM)) {
                notInitiatedCRMLeadsCall();
            } else {
                isCompletedForms = true;
                completedLeadsPPCall();
            }
        } else if (id == R.id.tvOwn) {
            clientType = OWN;
            verficationStation = "0";
            if (isCompletedForms) {
                completedLeadsPPCall();
            } else {
                inProcessPPLeadsCall();
            }
            if (Build.VERSION.SDK_INT < 16) {
                tvDirect.setBackgroundDrawable(ContextCompat.getDrawable(postLoginContext, R.drawable.rounded_border_textview));
                tvOwn.setBackgroundDrawable(ContextCompat.getDrawable(postLoginContext, R.drawable.rounded_border_orange_texview));
            } else {
                tvDirect.setBackground(ContextCompat.getDrawable(postLoginContext, R.drawable.rounded_border_textview));
                tvOwn.setBackground(ContextCompat.getDrawable(postLoginContext, R.drawable.rounded_border_orange_texview));
            }
            tvOwn.setTextColor(ContextCompat.getColor(postLoginContext, R.color.white));
            tvDirect.setTextColor(ContextCompat.getColor(postLoginContext, R.color.secondaryBackground));
        } else if (id == R.id.tvDirect) {
            clientType = DIRECT;
            verficationStation = "0";
            if (isCompletedForms) {
                completedLeadsPPCall();
            } else {
                inProcessPPLeadsCall();
            }
            if (Build.VERSION.SDK_INT < 16) {
                tvDirect.setBackgroundDrawable(ContextCompat.getDrawable(postLoginContext, R.drawable.rounded_border_orange_texview));
                tvOwn.setBackgroundDrawable(ContextCompat.getDrawable(postLoginContext, R.drawable.rounded_border_textview));
            } else {
                tvDirect.setBackground(ContextCompat.getDrawable(postLoginContext, R.drawable.rounded_border_orange_texview));
                tvOwn.setBackground(ContextCompat.getDrawable(postLoginContext, R.drawable.rounded_border_textview));
            }
            tvDirect.setTextColor(ContextCompat.getColor(postLoginContext, R.color.white));
            tvOwn.setTextColor(ContextCompat.getColor(postLoginContext, R.color.secondaryBackground));
        } else if (id == R.id.tvVerificationStage) {
            onClickedVarificationStage = true;
            if (onClickedVarificationStage) {
                verficationStation = "1";
                if (isCompletedForms) {
                    completedLeadsPPCall();
                } else {
                    inProcessPPLeadsCall();
                }
            } else {
                verficationStation = "0";
                inProcessPPLeadsCall();
            }
        }
    }

    public class FranchiseAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        Context mContext;
        List<FranchiseLeadsModel> franchiseLeadsModel;


        public FranchiseAdapter(Context c, List<FranchiseLeadsModel> inProcessLeadsList) {
            inflater = LayoutInflater.from(c);
            mContext = c;
            this.franchiseLeadsModel = inProcessLeadsList;
        }

        @Override
        public int getCount() {
            return franchiseLeadsModel.size();
        }

        @Override
        public Object getItem(int position) {
            return franchiseLeadsModel.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder = new Holder();
            view = inflater.inflate(R.layout.item_list_postlogin_fran, parent, false);


            final FranchiseLeadsModel inProcessLeadsPPModel = franchiseLeadsModel.get(position);
            holder.rlLinearData = (RelativeLayout) view.findViewById(R.id.rlLinearData);
          /*  if (position % 2 == 1) {
                holder.rlLinearData.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                holder.rlLinearData.setBackgroundColor(Color.parseColor("#EBEBEB"));
            }*/
            holder.ivRightArrow = (ImageView) view.findViewById(R.id.ivRightArrow);
            holder.txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            holder.txtName = (TextView) view.findViewById(R.id.txtName);
            holder.txtLeadId = (TextView) view.findViewById(R.id.txtLeadId);
            holder.txtMobNum = (TextView) view.findViewById(R.id.txtMobNum);
            holder.txtDate = (TextView) view.findViewById(R.id.txtDate);
            final String clientStatus = inProcessLeadsPPModel.getClientStatus();
            String numAcc = inProcessLeadsPPModel.getNumAccount();
            final String strUserID = inProcessLeadsPPModel.getUserID();
            holder.txtName.setText(inProcessLeadsPPModel.getClientName());
            holder.txtMobNum.setText(inProcessLeadsPPModel.getMobNo());
            if (clientStatus.equalsIgnoreCase(INPROCESS)) {
                holder.txtLeadId.setText(inProcessLeadsPPModel.getUserID());
                holder.txtStatus.setText(getResources().getString(R.string.gen_userid));
                holder.txtStatus.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            } else {
                holder.txtLeadId.setText(inProcessLeadsPPModel.getClientCode());
                holder.txtStatus.setText(clientStatus);
            }
            holder.txtDate.setText(inProcessLeadsPPModel.getModifiedDate());

            if (numAcc.equals("2")) {
                holder.ivRightArrow.setVisibility(View.GONE);
            } else {
                holder.ivRightArrow.setVisibility(View.VISIBLE);
            }
            holder.txtStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isBlank(inProcessLeadsPPModel.getBaCode())) {
                        convertBAtoBAProspect(inProcessLeadsPPModel.getUserID());
                    }
                }
            });
            holder.ivRightArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getProspectDetails(inProcessLeadsPPModel.getUserID());
                }
            });


            return view;
        }

        class Holder {
            TextView txtStatus, txtName, txtStage, txtLeadId, txtMobNum, txtDate;
            ImageView ivRightArrow;
            RelativeLayout rlLinearData;
        }

        // CONVERTBATOBAPROSPECT
        public void convertBAtoBAProspect(String userID) {

            Map inputMap = new HashMap<>();
            inputMap.put(APP_LOGIN_ID, getAppLoginID());
            inputMap.put(APPLICATION_TYPE, getAppType());
            inputMap.put(_userid, userID);

            String flag = CONVERTBATOBAPROSPECT;
            String category = SIGNUP;
            if (MOSLCommon.isNetworkEnabled(postLoginContext)) {
                PostLoginActivity.customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(postLoginContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                        super.onSuccess(statusCode, headers, response);
                        PostLoginActivity.customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        PostLoginActivity.customProgress.setVisibility(View.GONE);
                        if (response != null) {
                            try {
                                String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                                commonMsgDialog(postLoginContext, msg, false, "");
                                notifyDataSetChanged();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Timber.v(INITIATE_LEAD_CRM, NO_RESPONSE);
                        }


                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        PostLoginActivity.customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        PostLoginActivity.customProgress.setVisibility(View.GONE);
                        commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        PostLoginActivity.customProgress.setVisibility(View.GONE);
                        commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        PostLoginActivity.customProgress.setVisibility(View.GONE);
                        commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });
            } else {
                PostLoginActivity.customProgress.setVisibility(View.GONE);
                MOSLCommon.alertDInternet(postLoginContext);
            }
        }

    }

    public void noResponse(String msg) {
        llNoResponse.setVisibility(View.VISIBLE);
        lvLeads.setVisibility(View.GONE);
        tvNoResponse.setText(msg);

    }

    public void respAvailable() {
        llNoResponse.setVisibility(View.GONE);
        lvLeads.setVisibility(View.VISIBLE);

    }

    public class BranchAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        Context mContext;
        List<BranchLeadsModel> branchLeadsModel_1s;


        public BranchAdapter(Context c, List<BranchLeadsModel> branchLeadsModel_1s) {
            inflater = LayoutInflater.from(c);
            mContext = c;
            this.branchLeadsModel_1s = branchLeadsModel_1s;
        }

        @Override
        public int getCount() {
            return branchLeadsModel_1s.size();
        }

        @Override
        public Object getItem(int position) {
            return branchLeadsModel_1s.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder = new Holder();
            view = inflater.inflate(R.layout.item_list_postlogin, parent, false);

             /*"LEADID":1802806,
                            "STAGE":"C1",
                            "STATUS":"First Meeting Done",
                            "LEADNAME":"Bhupat Punabhai Baria ",
                            "MOBILENO":"8828343505",
                            "SORTDATE":"2016-08-31T16:58:39",
                            "CREATEDSORT":"2016-08-31T16:58:39",
                            "MODIFIEDDATE":"31/08/2016 16:58:39",

                            "LASTMODIFIEDON":"Aug 31 2016 4:58PM",
                            "CREATEDON":"31/08/2016 16:58:39",
                            "CREATEDDATE":"Aug 31 2016 4:58PM",
                            "USERID":"0",
                            "IS_AADHAR":false,
                            "APPLICATIONFROM":""*/

            final BranchLeadsModel branchLeadsModel = branchLeadsModel_1s.get(position);
            holder.rlLinearData = (RelativeLayout) view.findViewById(R.id.rlLinearData);
           /* if (position % 2 == 1) {
                holder.rlLinearData.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                holder.rlLinearData.setBackgroundColor(Color.parseColor("#EBEBEB"));
            }*/
            holder.ivRightArrow = (ImageView) view.findViewById(R.id.ivRightArrow);
            holder.txtStatus = (TextView) view.findViewById(R.id.txtStatus);
            holder.txtName = (TextView) view.findViewById(R.id.txtName);
            holder.txtStage = (TextView) view.findViewById(R.id.txtStage);
            holder.txtLeadId = (TextView) view.findViewById(R.id.txtLeadId);
            holder.txtMobNum = (TextView) view.findViewById(R.id.txtMobNum);
            holder.txtDate = (TextView) view.findViewById(R.id.txtDate);


            String stage = branchLeadsModel.getStage();
            final String isAadhar = branchLeadsModel.getIsAadhar();
            final String leadID = branchLeadsModel.getLeadID();
            final String strUserID = branchLeadsModel.getUserID();
            final int userID = Integer.valueOf(strUserID);

            holder.txtName.setText(branchLeadsModel.getLeadName());
            holder.txtMobNum.setText(branchLeadsModel.getMobNo());
            holder.txtStage.setText(stage);
            holder.txtLeadId.setText(branchLeadsModel.getLeadID());
            holder.txtStatus.setText(branchLeadsModel.getStatus());
            holder.txtDate.setText(branchLeadsModel.getCreatedDate());


            holder.ivRightArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (userID > 0) {
                        // lead is already transfered to ekyc
                        getProspectDetails(strUserID);
                    } else {
                        // ekyc not initiated leads

//                        initiateLeadInCRM(leadID, strUserID);
//                        MOSLCommon.showPopUpThreeLine(postLoginContext, lvLeads, getResources().getString(R.string.convert_to_ekyc), MOSLConstants.PANADHAR_ACTIVITY, true, leadID, strUserID);
                    }


                }
            });


            return view;
        }


        class Holder {
            TextView txtStatus, txtName, txtStage, txtLeadId, txtMobNum, txtDate;
            ImageView ivRightArrow;
            RelativeLayout rlLinearData;
        }
    }

    public void initiateLeadInCRM(String leadID, final String userID) {

        Map inputMap = new HashMap<>();
        inputMap.put(APP_LOGIN_ID, CommonModel.getAppLoginID());
        inputMap.put(LEAD_ID, leadID);

        String flag = TRANSFERRETAILTOEKYC;
        String category = SIGNUP;
        if (MOSLCommon.isNetworkEnabled(postLoginContext)) {
            PostLoginActivity.customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(postLoginContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                    super.onSuccess(statusCode, headers, response);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

              /*  {"_userid":null,"_isnewcustomer":null,
                        "_msg":"Lead transferred to ekyc successfully","_Success":0,
                        "_Step":0,"_uniqueId":null,
                        "_Status":"1","_OTP":null,
                        "_BusinessUnitId":null,"_Role":null,
                        "_ShowChangePassword":null}*/
                    if (response != null) {
                        try {
                            String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                            String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                            String initiateLeadUserID = (response.isNull("_userid") || response.getString("_userid").equals("")) ? "" : response.getString("_userid");

                            if (Integer.valueOf(status) == 1) {
                                setUserID(initiateLeadUserID);
                                getProspectDetails(initiateLeadUserID);
                                /*Intent intent = new Intent(postLoginContext, PANAdharRegistActivity.class);
                                intent.putExtra(USER_ID, initiateLeadUserID);
                                postLoginContext.startActivity(intent);
                                getActivity().finish();*/
                            } else {
                                MOSLCommon.showToastMsg(postLoginContext, msg);
                            }

                            MOSLCommon.showToastMsg(postLoginContext, msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Timber.v(INITIATE_LEAD_CRM, NO_RESPONSE);
                    }

                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            PostLoginActivity.customProgress.setVisibility(View.GONE);
            MOSLCommon.alertDInternet(postLoginContext);
        }
    }


    private void getProspectDetails(final String userID) {
        setBooleanValueToFalse();
        setAddNewSegment(true);

        if (MOSLCommon.isNetworkEnabled(postLoginContext)) {
            PostLoginActivity.customProgress.setVisibility(View.VISIBLE);

            Map inputMap = new HashMap<>();
            inputMap.put("_userid", userID);
            String flag = GETPROSPECTDETAILS;
            String category = LOGIN;
            WebServiceHandler.post(postLoginContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            if (response != null) {
                                PostLoginActivity.customProgress.setVisibility(View.GONE);
                                prospectTable = false;
                                allStageMap = new HashMap<String, Map<String, String>>();

                                try {
                                    if (response.has("PROSPECTDETAILS3")) {
                                        JSONArray jsonArraProspectDetails3 = response.getJSONArray("PROSPECTDETAILS3");

                                        if (jsonArraProspectDetails3.length() != 0) {
                                            for (int i = 0; i < jsonArraProspectDetails3.length(); i++) {
                                                JSONObject object = jsonArraProspectDetails3.getJSONObject(i);
                                                String product = (object.isNull(PRODUCT) || object.getString(PRODUCT).equals("")) ? "" : object.getString(PRODUCT);
                                                if (product.equalsIgnoreCase(EQUITY) || product.equalsIgnoreCase(PMS)) {
                                                    setIsEquityOriginal(true);
                                                }
                                                if (product.equalsIgnoreCase(COMMODITY)) {
                                                    setIsCommodityOriginal(true);
                                                }
                                                if (product.equalsIgnoreCase(Derivative)) {
                                                    setIsDerivativeOriginal(true);
                                                }
                                                if (product.equalsIgnoreCase(CURRENCY)) {
                                                    setIsCurrencyOriginal(true);
                                                }
                                            }
                                        }
                                    }

                                    if (response.has("PROSPECTDETAILS2")) {
                                        JSONArray jsonArraProspectDetails2 = response.getJSONArray("PROSPECTDETAILS2");
                                        if (jsonArraProspectDetails2.length() != 0) {
                                            for (int i = 0; i < jsonArraProspectDetails2.length(); i++) {
                                                JSONObject object = jsonArraProspectDetails2.getJSONObject(i);
                                                String userId = (object.isNull("USERID") || object.getString("USERID").equals("")) ? "" : object.getString("USERID");
                                                String aadharNo = (object.isNull("AADHARNO") || object.getString("AADHARNO").equals("")) ? "" : object.getString("AADHARNO");
                                                String panNo = (object.isNull("PANNO") || object.getString("PANNO").equals("")) ? "" : object.getString("PANNO");
                                                String iscvlVerified = (object.isNull("ISCVLVERIFIED") || object.getString("ISCVLVERIFIED").equals("")) ? "" : object.getString("ISCVLVERIFIED").trim();
                                                String isAddharVerified = (object.isNull("ISADDHARVERIFIED") || object.getString("ISADDHARVERIFIED").equals("")) ? "" : object.getString("ISADDHARVERIFIED").trim();
                                                String addressMode = (object.isNull("ADDRESSMODE") || object.getString("ADDRESSMODE").equals("")) ? "" : object.getString("ADDRESSMODE");
                                                String dob = (object.isNull("BIRTHDATE") || object.getString("BIRTHDATE").equals("")) ? "" : object.getString("BIRTHDATE");
                                                String city = (object.isNull("CITY") || object.getString("CITY").equals("")) ? "" : object.getString("CITY");
                                                String registerName = (object.isNull("REGISTERNAME") || object.getString("REGISTERNAME").equals("")) ? "" : object.getString("REGISTERNAME");
                                                String interestedIn = (object.isNull("INTRESTEDIN") || object.getString("INTRESTEDIN").equals("")) ? "" : object.getString("INTRESTEDIN");
                                                String nameOnPan = (object.isNull("Name_on_PAN") || object.getString("Name_on_PAN").equals("")) ? "" : object.getString("Name_on_PAN");
                                                String sipIntent = (object.isNull("SIPINTENT") || object.getString("SIPINTENT").equals("")) ? "" : object.getString("SIPINTENT");
                                                String eqSchemeId = (object.isNull("EQSCHEME_ID") || object.getString("EQSCHEME_ID").equals("")) ? "" : object.getString("EQSCHEME_ID");
                                                String userType = (object.isNull("USERTYPE") || object.getString("USERTYPE").equals("")) ? "" : object.getString("USERTYPE");
                                                String baCode = (object.isNull("BACODE") || object.getString("BACODE").equals("")) ? "" : object.getString("BACODE");
                                                String tokenKey = (object.isNull("TOKENKEY") || object.getString("TOKENKEY").equals("")) ? "" : object.getString("TOKENKEY");
                                                String downloadPdfUrl = (object.isNull("DOWNLOADPDFURL") || object.getString("DOWNLOADPDFURL").equals("")) ? "" : object.getString("DOWNLOADPDFURL");
                                                String esignService = (object.isNull("ESIGNSERVICE") || object.getString("ESIGNSERVICE").equals("")) ? "" : object.getString("ESIGNSERVICE");
                                                String otpVisible = (object.isNull("OTPVISIBLE") || object.getString("OTPVISIBLE").equals("")) ? "" : object.getString("OTPVISIBLE");
                                                String biometricVisible = (object.isNull("BIOMETRICVISIBLE") || object.getString("BIOMETRICVISIBLE").equals("")) ? "" : object.getString("BIOMETRICVISIBLE");
                                                String isNomineeMandatory = (object.isNull(NOMINEE_MANDATORY) || object.getString(NOMINEE_MANDATORY).equals("")) ? "" : object.getString(NOMINEE_MANDATORY);
                                                String isAadhaarByPass =
                                                    (object.isNull("ISAADHAARBYPASS")
                                                        || object.getString("ISAADHAARBYPASS")
                                                        .equals("")) ? ""
                                                        : object.getString("ISAADHAARBYPASS")
                                                            .trim();
                                                if (isBlank(userId)) {
                                                    commonMsgDialog(postLoginContext, DATA_IS_MISSING, false, "");
                                                    return;
                                                } else {
                                                    setUserID(userId);
                                                }
                                                setAadharNo(aadharNo);
                                                setPANno(panNo);
                                                setIsAadharVerified(isAddharVerified.equals("1"));
                                                setIsCVLVerified(iscvlVerified.equals("1"));
                                                setIsNomineeMandatory(
                                                    isNomineeMandatory != null && isNomineeMandatory
                                                        .equals("1"));
                                                setAadhaarByPass(isAadhaarByPass.equals("1"));
                                                setCity(city);
                                                setRegisteredName(registerName);
                                                setAddressMode(addressMode);
                                                setDob(dob);
                                                setPANName(nameOnPan);
                                                setSipIntent(sipIntent);
                                                setEqSchemeId(eqSchemeId);
                                                setUserType(userType);
                                                setBaFranchiseeCode(baCode);
                                                setTokenKey(tokenKey);
                                                setDownloadPdfUrl(downloadPdfUrl);
                                                setEsignService(esignService);
                                                setOtpVisibleForBranch(otpVisible);
                                                setBioMetricVisibleForBranch(biometricVisible);

                                                JSONArray jsonArraProspectDetails3 = response.getJSONArray("PROSPECTDETAILS3");
                                                if ((jsonArraProspectDetails3.length() == 0)) {
                                                    if (interestedIn.equalsIgnoreCase(EQUITY) || interestedIn.equalsIgnoreCase(PMS)) {
                                                        setIsEquitySelected(true);
                                                    }
                                                    if (interestedIn.equalsIgnoreCase(COMMODITY)) {
                                                        setIsCommoditySelected(true);
                                                    }
                                                    if (interestedIn.equalsIgnoreCase(MF)) {
                                                        if (!isMfSegmentExist()) {
                                                            setIsInterestedInMF(true);
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            if (prospectTable == false) {
                                                setGotoReviewEdit(false);
                                                setGotoFundTransfer(false);
                                            }
                                            commonMsgDialog(postLoginContext, DATA_IS_MISSING, false, "");
                                            return;
                                        }
                                    }
                                    if (response.has("PROSPECTDETAILS")) {
                                        JSONArray jsonArraProspectDetails = response.getJSONArray("PROSPECTDETAILS");

                                        if (jsonArraProspectDetails.length() != 0) {
                                            String offlineIpvStatus = null;
                                            for (int i = 0; i < jsonArraProspectDetails.length(); i++) {
                                                prospectTable = true;

                                                JSONObject object = jsonArraProspectDetails.getJSONObject(i);
                                                String segment = (object.isNull(SEGMENT) || object.getString(SEGMENT).equals("")) ? "" : object.getString(SEGMENT);
                                                String registration = (object.isNull(REGISTRATIONPROSPECT) || object.getString(REGISTRATIONPROSPECT).equals("")) ? "" : object.getString(REGISTRATIONPROSPECT);
                                                String segmentSelected = (object.isNull(SEGMENTSELECTEDPROSPECT) || object.getString(SEGMENTSELECTEDPROSPECT).equals("")) ? "" : object.getString(SEGMENTSELECTEDPROSPECT);
                                                String personalDetail = (object.isNull(PERSONALDETAILPROSPECT) || object.getString(PERSONALDETAILPROSPECT).equals("")) ? "" : object.getString(PERSONALDETAILPROSPECT);
                                                String workDetail = (object.isNull(WORKDETAILPROSPECT) || object.getString(WORKDETAILPROSPECT).equals("")) ? "" : object.getString(WORKDETAILPROSPECT);
                                                String demateDetails = (object.isNull(DEMATEDETAILSPROSPECT) || object.getString(DEMATEDETAILSPROSPECT).equals("")) ? "" : object.getString(DEMATEDETAILSPROSPECT);
                                                String bankDetail = (object.isNull(BANKDETAILPROSPECT) || object.getString(BANKDETAILPROSPECT).equals("")) ? "" : object.getString(BANKDETAILPROSPECT);
                                                String schemeDetail = (object.isNull(SCHEMEDETAILPROSPECT) || object.getString(SCHEMEDETAILPROSPECT).equals("")) ? "" : object.getString(SCHEMEDETAILPROSPECT);
                                                String reviewEdit = (object.isNull(REVIEWEDITPROSPECT) || object.getString(REVIEWEDITPROSPECT).equals("")) ? "" : object.getString(REVIEWEDITPROSPECT);
                                                String fundTransfer = (object.isNull(FUNDTRANSFERPROSPECT) || object.getString(FUNDTRANSFERPROSPECT).equals("")) ? "" : object.getString(FUNDTRANSFERPROSPECT);
                                                String documentUploaded = (object.isNull(DOCUMENTUPLOADEDPROSPECT) || object.getString(DOCUMENTUPLOADEDPROSPECT).equals("")) ? "" : object.getString(DOCUMENTUPLOADEDPROSPECT);
                                                String esignCompleted = (object.isNull(ESIGNCOMPLETEDPROSPECT) || object.getString(ESIGNCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(ESIGNCOMPLETEDPROSPECT);
                                                String ipvinitiated = (object.isNull(IPVINITIATEDPROSPECT) || object.getString(IPVINITIATEDPROSPECT).equals("")) ? "" : object.getString(IPVINITIATEDPROSPECT);
                                                String ipvDonebyVerifier = (object.isNull(IPVDONEBYVERIFIERPROSPECT) || object.getString(IPVDONEBYVERIFIERPROSPECT).equals("")) ? "" : object.getString(IPVDONEBYVERIFIERPROSPECT);
                                                String ipvCompleted = (object.isNull(IPVCOMPLETEDPROSPECT) || object.getString(IPVCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(IPVCOMPLETEDPROSPECT);
                                                String client = (object.isNull(CLIENTCODEGENERATEDPROSPECT) || object.getString(CLIENTCODEGENERATEDPROSPECT).equals("")) ? "" : object.getString(CLIENTCODEGENERATEDPROSPECT);
                                                String accountActivated = (object.isNull(ACCOUNTACTIVATEDPROSPECT) || object.getString(ACCOUNTACTIVATEDPROSPECT).equals("")) ? "" : object.getString(ACCOUNTACTIVATEDPROSPECT);
                                                String offlineIpvInitiated = (object.isNull(OFFLINEIPVINITIATEDPROSPECT) || object.getString(OFFLINEIPVINITIATEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEIPVINITIATEDPROSPECT);
                                                offlineIpvStatus = (object.isNull(OFFLINEIPVSTATUSPROSPECT) || object.getString(OFFLINEIPVSTATUSPROSPECT).equals("")) ? "" : object.getString(OFFLINEIPVSTATUSPROSPECT);
                                                String offlineDocumentUploaded = (object.isNull(OFFLINEDOCUMENTUPLOADEDPROSPECT) || object.getString(OFFLINEDOCUMENTUPLOADEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEDOCUMENTUPLOADEDPROSPECT);
                                                String offlineEsignCompleted = (object.isNull(OFFLINEESIGNCOMPLETEDPROSPECT) || object.getString(OFFLINEESIGNCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEESIGNCOMPLETEDPROSPECT);
                                                String offlineIpvCompleted = (object.isNull(OFFLINEIPVCOMPLETEDPROSPECT) || object.getString(OFFLINEIPVCOMPLETEDPROSPECT).equals("")) ? "" : object.getString(OFFLINEIPVCOMPLETEDPROSPECT);
                                                String offlineClientCodeGenerated = (object.isNull(OFFLINECLIENTCODEGENERATEDPROSPECT) || object.getString(OFFLINECLIENTCODEGENERATEDPROSPECT).equals("")) ? "" : object.getString(OFFLINECLIENTCODEGENERATEDPROSPECT);

                                                // Payment status
                                                if (segment.equalsIgnoreCase(EQUITY) || segment.equalsIgnoreCase(PMS)) {
                                                    setIsEquityOriginal(true);
                                                    setIsEquitySelected(true);

                                                    if (fundTransfer.equalsIgnoreCase("Y")) {
                                                        setIsEquityFundTransfered(true);
                                                    } else {
                                                        setIsEquityFundTransfered(false);
                                                    }

                                                } else if (segment.equalsIgnoreCase(COMMODITY)) {
                                                    setIsCommodityOriginal(true);
                                                    setIsCommoditySelected(true);

                                                    if (fundTransfer.equalsIgnoreCase("Y")) {
                                                        setIsCommodityFundTransfered(true);
                                                    } else {
                                                        setIsCommodityFundTransfered(false);
                                                    }
                                                } else if (segment.equalsIgnoreCase(MF)) {
                                                    setIsInterestedInMF(true);
                                                }

                                                if (!isBlank(client) && client.equalsIgnoreCase("Y")) {
                                                    setExistingSegment(segment);
                                                    continue; //segment completed
                                                } else {
                                                    setAddNewSegment(false);
                                                }

                                                Map stageMap = new HashMap();
                                                stageMap.put(REGISTRATION_STAGE, registration);
                                                stageMap.put(SEGMENTSELECTED_STAGE, segmentSelected);
                                                stageMap.put(PERSONALDETAIL_STAGE, personalDetail);
                                                stageMap.put(WORKDETAIL_STAGE, workDetail);
                                                stageMap.put(DEMATEDETAILS_STAGE, demateDetails);
                                                stageMap.put(BANKDETAIL_STAGE, bankDetail);
                                                stageMap.put(SCHEMEDETAIL_STAGE, schemeDetail);
                                                stageMap.put(REVIEWEDIT_STAGE, reviewEdit);
                                                stageMap.put(FUNDTRANSFER_STAGE, fundTransfer);
                                                stageMap.put(DOCUMENTUPLOADED_STAGE, documentUploaded);
                                                stageMap.put(ESIGNCOMPLETED_STAGE, esignCompleted);
                                                stageMap.put(IPVINITIATED_STAGE, ipvinitiated);
                                                stageMap.put(IPVDONEBYVERIFIER_STAGE, ipvDonebyVerifier);
                                                stageMap.put(IPVCOMPLETED_STAGE, ipvCompleted);
                                                stageMap.put(CLIENTCODEGENERATED_STAGE, client);
                                                stageMap.put(ACCOUNTACTIVATED_STAGE, accountActivated);
                                                stageMap.put(OFFLINEIPVINITIATED_STAGE, offlineIpvInitiated);
                                                stageMap.put(OFFLINEIPVSTATUS_STAGE, offlineIpvStatus);
                                                stageMap.put(OFFLINEDOCUMENTUPLOADED_STAGE, offlineDocumentUploaded);
                                                stageMap.put(OFFLINEESIGNCOMPLETED_STAGE, offlineEsignCompleted);
                                                stageMap.put(OFFLINEIPVCOMPLETED_STAGE, offlineIpvCompleted);
                                                stageMap.put(OFFLINECLIENTCODEGENERATED_STAGE, offlineClientCodeGenerated);

                                                if (segment.equalsIgnoreCase(EQUITY) && !isEquitySegmentExist()) {
                                                    allStageMap.put(segment, stageMap);
                                                }
                                                if (segment.equalsIgnoreCase(COMMODITY) && !isCommoditySegmentExist()) {
                                                    allStageMap.put(segment, stageMap);
                                                }
                                                if (segment.equalsIgnoreCase(MF) && !isMfSegmentExist()) {
                                                    allStageMap.put(segment, stageMap);
                                                }
                                            }
                                            setGotoReviewEdit(false);
                                            setGotoFundTransfer(false);
                                        } else {
                                            prospectTable = false;
                                        }
                                    }

                                    if (prospectTable == true) {
                                        setIsOffline(setOffLineStatus());
                                    } else {
                                        setIsOffline(false);
                                    }
                                    if (prospectTable == true && !isBlank(getAddressMode())) {
                                        goToNextPage();
                                    } else {
                                        if (getInterestedInMF()) {
                                            if (getAppType().equalsIgnoreCase(PP) || getAppType().equalsIgnoreCase(CRM)) {

                                                if (isBlank(getPANno())) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(postLoginContext, PANAdharRegistActivity.class);
                                                    intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                } else if (!getIsAadharVerified() && !getIsCVLVerified()) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(postLoginContext, PANAdharRegistActivity.class);
                                                    intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                } else if (isBlank(getAddressMode())) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
//                                                    Intent intent = new Intent(postLoginContext, PANAdharRegistActivity.class);
                                                    Intent intent = new Intent(postLoginContext, AddressDetailsActivity.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                } else {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
                                                    intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                }
                                            }
                                        } else {
                                            //Broking
                                            if (getAppType().equalsIgnoreCase(PP) || getAppType().equalsIgnoreCase(CRM)) {
                                                if (isBlank(getPANno())) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(postLoginContext, PANAdharRegistActivity.class);
                                                    intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                } else if (!getIsAadharVerified()) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(postLoginContext, PANAdharRegistActivity.class);
                                                    intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                } else if (isBlank(getAddressMode())) {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
//                                                    Intent intent = new Intent(postLoginContext, PANAdharRegistActivity.class);
                                                    Intent intent = new Intent(postLoginContext, AddressDetailsActivity.class);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                } else {
                                                    setGotoReviewEdit(false);
                                                    setGotoFundTransfer(false);
                                                    Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
                                                    intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                                                    startActivity(intent);
                                                    getActivity().finish();
                                                }
                                            }

                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                MOSLCommon.showToastMsg(postLoginContext, NO_RESPONSE);
                                PostLoginActivity.customProgress.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String
                                responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable
                                throwable, JSONObject errorResponse) {
                            Log.e("Failure", "no resp in Data");
                            PostLoginActivity.customProgress.setVisibility(View.GONE);
                            commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable
                                throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }
                    }

            );
        } else {
            MOSLCommon.alertDInternet(postLoginContext);
            PostLoginActivity.customProgress.setVisibility(View.GONE);
        }
    }

    private void setBooleanValueToFalse() {
        setGotoFundTransfer(false);
        setGotoReviewEdit(false);
        setIsSegmentSelected(false);
        setIsPersonalDetailDone(false);
        setIsWorkDetailDone(false);
        setIsBankDetailDone(false);
        setIsSchemeDetailDone(false);
        setIsReviewEditDone(false);
//        setIsFundTransferDone(false);
        setIsIPVDone(false);
        setIsOfflineDocumentUploadedDone(false);
        setIsEquityFundTransfered(false);
        setIsCommodityFundTransfered(false);
        setIsEquityIpvDone(false);
        setIsMfIpvDone(false);
    }

    private void goToNextPage() {
        if (isAddNewSegment()) {
            showSecondProductOption(getUserID());
            return;
        }

        Set<String> segmentSet = allStageMap.keySet();
        for (String segment : segmentSet) {
            if (segment.equalsIgnoreCase(EQUITY) && isEquitySegmentExist()) {
                if (allStageMap.size() == 1) {
                    if (getIsAadharVerified()) {
                        Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
                        intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    } else {
                        Intent intent = new Intent(postLoginContext, PANAdharRegistActivity.class);
                        intent.putExtra(SOURCE, ADD_SEGMENT);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    }
                } else {
                    continue;
                }
            } else if (segment.equalsIgnoreCase(COMMODITY) && isCommoditySegmentExist()) {
                if (allStageMap.size() == 1) {
                    if (getIsAadharVerified()) {
                        Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
                        intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    } else {
                        Intent intent = new Intent(postLoginContext, PANAdharRegistActivity.class);
                        intent.putExtra(SOURCE, ADD_SEGMENT);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    }
                } else {
                    continue;
                }
            } else if (segment.equalsIgnoreCase(MF) && isMfSegmentExist()) {
                continue;
            }
            Map<String, String> currentStageMap = (HashMap) allStageMap.get(segment);

            if (isStagePending(REGISTRATION_STAGE)) {
                //goto PAN-ADHAAR
                Intent intent = new Intent(postLoginContext, PANAdharRegistActivity.class);
                intent.putExtra(SOURCE, PAN_AADHAR_PENDING);
//                RegisterModel.setUserID(userID);
                startActivity(intent);
                getActivity().finish();
                return;
            } else if (isStagePending(SEGMENTSELECTED_STAGE)
                    || isStagePending(PERSONALDETAIL_STAGE)
                    || isStagePending(WORKDETAIL_STAGE)) {
                //goto personal detail
                Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                getActivity().finish();
                return;
            } else if (isStagePending(DEMATEDETAILS_STAGE)) {
                //goto demat detail
                setIsSegmentSelected(true);
                Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, DEMAT_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                getActivity().finish();
                return;
            } else if (isStagePending(BANKDETAIL_STAGE)
                    || (isStagePending(SCHEMEDETAIL_STAGE) && !getInterestedInMF())) {
                //goto Bank detail
                setGotoReviewEdit(false);
                setGotoFundTransfer(false);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, BANK_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                getActivity().finish();
                return;
            } else if (isStagePending(REVIEWEDIT_STAGE) && !getInterestedInMF()) {
                //goto Review
                setGotoReviewEdit(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
                Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, BANK_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                getActivity().finish();
                return;
            } else if (isStagePending(FUNDTRANSFER_STAGE) && !getInterestedInMF()) {
                //goto Fundtransfer
                setGotoFundTransfer(true);
                setIsReviewEditDone(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
                Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, BANK_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                getActivity().finish();
                return;
            } else if (isOffline() ? (isStagePending(OFFLINEDOCUMENTUPLOADED_STAGE) || (!isStagePending(OFFLINEESIGNCOMPLETED_STAGE)
                    && !isStagePending(OFFLINECLIENTCODEGENERATED_STAGE)) && isStagePending(OFFLINEIPVCOMPLETED_STAGE))
                    : (isStagePending(DOCUMENTUPLOADED_STAGE)
                    || isStagePending(IPVCOMPLETED_STAGE))) {//offline condition added
                //goto Upload
                setGotoFundTransfer(true);
                setIsReviewEditDone(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
//                setIsFundTransferDone(true);
                setIsIPVDone(false);
                if (isStagePending(OFFLINEDOCUMENTUPLOADED_STAGE)) {
                    setIsOfflineDocumentUploadedDone(false);
                } else {
                    setIsOfflineDocumentUploadedDone(true);
                }
                if (isStagePending(OFFLINECLIENTCODEGENERATED_STAGE)) {
                    setIsOfflineCliencodeGenrated(false);
                } else {
                    setIsOfflineCliencodeGenrated(true);
                }
                Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
                intent.putExtra(TAB_DETAIL, UPLOAD_DETAIL);
                setUserID(getUserID());
                startActivity(intent);
                getActivity().finish();
                return;
            } else if (isOffline() ? (isStagePending(OFFLINEESIGNCOMPLETED_STAGE)
                    || isStagePending(OFFLINECLIENTCODEGENERATED_STAGE))
                    : (isStagePending(ESIGNCOMPLETED_STAGE)
                    || isStagePending(CLIENTCODEGENERATED_STAGE)
                    || isStagePending(ACCOUNTACTIVATED_STAGE))) {//offline condition added
                //goto Esign
                setIsReviewEditDone(true);
                setIsSegmentSelected(true);
                setIsPersonalDetailDone(true);
                setIsWorkDetailDone(true);
                setIsBankDetailDone(true);
                setIsSchemeDetailDone(true);
//                setIsFundTransferDone(true);
                setIsIPVDone(true);
                setIsOfflineDocumentUploadedDone(true);
                if (isStagePending(OFFLINECLIENTCODEGENERATED_STAGE)) {
                    setIsOfflineCliencodeGenrated(false);
                } else {
                    setIsOfflineCliencodeGenrated(true);
                }
                if (segment.equalsIgnoreCase(EQUITY)) {
                    setIsEquityIpvDone(true);
                }
                if (segment.equalsIgnoreCase(MF)) {
                    setIsMfIpvDone(true);
                }
                Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
                intent.putExtra(PDF_PATH, "");
                intent.putExtra(TAB_DETAIL, ESIGN_DETAIL);//todo pass from prospect-API
                //intent.putExtra(AADHAR, aadharNo);
                setUserID(getUserID());
                startActivity(intent);
                getActivity().finish();
                return;
            } else if (!isStagePending(CLIENTCODEGENERATED_STAGE)) {
                setIsMfIpvDone(false);
            }
        }
    }

    /*
    * Set Offline flag form Prospect API
    * */
    private boolean setOffLineStatus() {
        Set<String> segmentSet = allStageMap.keySet();
        if (allStageMap.size() == 1) {
            String segment = segmentSet.iterator().next();
            Map<String, String> currentStageMap = (HashMap) allStageMap.get(segment);
            if (currentStageMap.get(OFFLINEIPVSTATUS_STAGE).equals("0")) {
                return true;
            } else {
                return false;
            }
        } else if (allStageMap.size() == 2) {
            Iterator iterator = segmentSet.iterator();
            String segment1 = iterator.next().toString();
            String segment2 = iterator.next().toString();
            Map<String, String> currentStageMap1 = (HashMap) allStageMap.get(segment1);
            Map<String, String> currentStageMap2 = (HashMap) allStageMap.get(segment2);
            if (currentStageMap1.get(OFFLINEIPVSTATUS_STAGE).equals("0") && currentStageMap2.get(OFFLINEIPVSTATUS_STAGE).equals("0")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * will check only in-process segments
     *
     * @param STAGE
     * @return
     */
    private boolean isStagePending(String STAGE) {
        Set<String> segmentSet = allStageMap.keySet();

        if (allStageMap.size() == 1) {

            String segment = segmentSet.iterator().next();
            Map<String, String> currentStageMap = (HashMap) allStageMap.get(segment);
            if (currentStageMap.get(STAGE).equals("Y")) {
                return false;
            } else {
                return true;
            }
        } else if (allStageMap.size() == 2) {
            Iterator iterator = segmentSet.iterator();
            String segment1 = iterator.next().toString();
            String segment2 = iterator.next().toString();


            Map<String, String> currentStageMap1 = (HashMap) allStageMap.get(segment1);
            Map<String, String> currentStageMap2 = (HashMap) allStageMap.get(segment2);

            if (currentStageMap1.get(STAGE).equals("Y") && currentStageMap2.get(STAGE).equals("Y")) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private void showSecondProductOption(String userId) {
        if (MOSLCommon.isNetworkEnabled(postLoginContext)) {
            PostLoginActivity.customProgress.setVisibility(View.VISIBLE);

            Map inputMap = new HashMap<>();

            inputMap.put("_userid", userId);
            inputMap.put(APP_LOGIN_ID, getAppLoginID());
            inputMap.put(APPLICATION_TYPE, getAppType());
            String flag = GETAVAILABLEPRODUCT;
            String category = SIGNUP;
            WebServiceHandler.post(postLoginContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response.length() != 0) {
                            JSONArray dataArray = response.getJSONArray("AvailableProduct");
                            List<String> productList = new ArrayList();
                            if (dataArray != null && dataArray.length() != 0) {

                                for (int ctr = 0; ctr < dataArray.length(); ctr++) {
                                    JSONObject dataObject = (JSONObject) dataArray.get(ctr);
                                    String product = dataObject.getString("Product");
                                    productList.add(product);
                                }
                            }

                           /* MOSLCommon.showPopUpThreeLine(postLoginContext, lvLeads, ADD_NEW_SEGMENT,
                                    MOSLConstants.PANADHAR_ACTIVITY, false, "", getUserID(), true, productList);*/

                            /*boolean showSecodProductPopup = false;
                            if (productList != null && productList.size() != 0) {

                                for (String prd : productList) {
                                    if (prd.equalsIgnoreCase(EQUITY) && isEquityFundTransfered()) {
                                        showSecodProductPopup = false;
                                        goToNextPage();
                                        break;
                                    } else if (prd.equalsIgnoreCase(COMMODITY) && isCommodityFundTransfered()) {
                                        showSecodProductPopup = false;
                                        goToNextPage();
                                        break;
                                    } else {
                                        showSecodProductPopup = true;
                                    }
                                }
                            } else {
                                secondProductPopup(productList);
                            }

                            if (showSecodProductPopup) {
                                secondProductPopup(productList);
                            }*/
                            secondProductPopup(productList);
                            PostLoginActivity.customProgress.setVisibility(View.GONE);
                        } else {
                            commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            PostLoginActivity.customProgress.setVisibility(View.GONE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        PostLoginActivity.customProgress.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    try {

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                    commonMsgDialog(postLoginContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    try {

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    PostLoginActivity.customProgress.setVisibility(View.GONE);
                }
            });
        } else {
            MOSLCommon.alertDInternet(postLoginContext);
        }

    }

    private void secondProductPopup(List<String> productList) {
        dialog = new Dialog(postLoginContext, android.R.style.Theme_Light);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.second_product_popup);
        //dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(ADD_NEW_SEGMENT);
        ImageView ivEquity = (ImageView) dialog.findViewById(R.id.ivEquity);
        ImageView ivCommodity = (ImageView) dialog.findViewById(R.id.ivCommodity);
        ImageView ivAll = (ImageView) dialog.findViewById(R.id.ivAll);
        ImageView ivTrackApp = (ImageView) dialog.findViewById(R.id.ivTrackApp);
        ImageView btnClose = (ImageView) dialog.findViewById(R.id.btnClose);
        if (productList != null && productList.size() != 0) {
            if (productList.size() > 1) {
                if (!isEquityFundTransfered() && !isCommodityFundTransfered()) {
                    ivAll.setVisibility(View.VISIBLE);
                }
            } else {
                ivAll.setVisibility(View.GONE);
            }

            for (String product : productList) {
                if (product.equalsIgnoreCase(EQUITY) && !isEquityFundTransfered()) {
                    ivEquity.setVisibility(View.VISIBLE);
                } else if (product.equalsIgnoreCase(COMMODITY) && !isCommodityFundTransfered()) {
                    ivCommodity.setVisibility(View.VISIBLE);
                }
            }

        } else {
            tvMessage.setText(ALL_SEGMENT_COMPLETED);
        }
        ivEquity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // dialog.dismiss();
                setIsEquitySelected(true);
                setIsEquityOriginal(true);

                continueWithSecondProduct();
            }
        });
        ivCommodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                setIsCommoditySelected(true);
                setIsCommodityOriginal(true);

                continueWithSecondProduct();
            }
        });
        ivAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                setIsAllSelected(true);
                setIsAllOriginal(true);
                continueWithSecondProduct();
            }
        });
        ivTrackApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog.dismiss();
                Intent intent = new Intent(postLoginContext, TrackApplicationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                postLoginContext.startActivity(intent);

            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllCommonModelFields();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void continueWithSecondProduct() {
        if (getInterestedInMF()) {
            setIsInterestedInMF(false);
        }
        if (!getIsAadharVerified()) {
            Intent intent = new Intent(postLoginContext, PANAdharRegistActivity.class);
            intent.putExtra(SOURCE, ADD_SEGMENT);
            postLoginContext.startActivity(intent);
            getActivity().finish();

        } else {
            Intent intent = new Intent(postLoginContext, EkycMainActivity.class);
            intent.putExtra(TAB_DETAIL, PERSONAL_DETAIL);
            postLoginContext.startActivity(intent);
            getActivity().finish();
        }
    }
}


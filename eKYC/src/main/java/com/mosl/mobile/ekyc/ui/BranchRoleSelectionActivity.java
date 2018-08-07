package com.mosl.mobile.ekyc.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.model.CommonModel;
import com.mosl.mobile.ekyc.model.RoleSelectionModel;
import com.mosl.mobile.ekyc.model.RowData;
import com.mosl.mobile.ekyc.service.WebServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;

/**
 * Created by Vijay on 9/10/2016.
 */
public class BranchRoleSelectionActivity extends Activity implements MOSLConstants, View.OnClickListener {

    Button btnGoToEkyc, btnViewExistingLeads, btnGoToCRM;
    Spinner spRoleSel;
    ArrayList<RowData> arrRoleSel;
    ArrayAdapter<RowData> arrAdapRoleSel;
    String roleID;
    TextView tvBOBranch, tvBOSubBroker, tvBOTraders, tvCRMBranch;
    RoleSelectionModel roleSelectionModel;
    LinearLayout customProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_role_selection);
            setPermisson(BranchRoleSelectionActivity.this,getApplicationContext());
            setUpView();
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID()+"-"+getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    private void setUpView() {
        btnGoToEkyc = (Button) findViewById(R.id.btnGoToEkyc);
        btnViewExistingLeads = (Button) findViewById(R.id.btnViewExistingLeads);
        btnGoToCRM = (Button) findViewById(R.id.btnGoToCRM);

        spRoleSel = (Spinner) findViewById(R.id.spRoleSel);

        tvBOBranch = (TextView) findViewById(R.id.tvBOBranch);
        tvBOSubBroker = (TextView) findViewById(R.id.tvBOSubBroker);
        tvBOTraders = (TextView) findViewById(R.id.tvBOTraders);
        tvCRMBranch = (TextView) findViewById(R.id.tvCRMBranch);

        customProgress = (LinearLayout) findViewById(R.id.customProgress);


        btnViewExistingLeads.setOnClickListener(this);
        getCRMRole(getAppLoginID());

    }

    public void getCRMRole(String appID) {
        if (MOSLCommon.isNetworkEnabled(this)) {
            Map inputMap = new HashMap<>();
            inputMap.put(APP_LOGIN_ID, appID);

            String flag = GETCRMROLE;
            String category = LOGIN;
            customProgress.setVisibility(View.VISIBLE);

            WebServiceHandler.post(BranchRoleSelectionActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response != null) {
                        try {
                            arrRoleSel = new ArrayList<>();
                            arrRoleSel.clear();
                            JSONArray jsonArray = response.getJSONArray(CRMROLE);
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String textStr = (object.isNull("TEXT") || object.getString("TEXT").equals("")) ? "" : object.getString("TEXT");
                                    String roleMappedIDstr = (object.isNull("ROLEMAPPINGID") || object.getString("ROLEMAPPINGID").equals("")) ? "" : object.getString("ROLEMAPPINGID");
                                    String defaultRole = (object.isNull("DEFAULTROLE") || object.getString("DEFAULTROLE").equals("")) ? "" : object.getString("DEFAULTROLE");

                                    arrRoleSel.add(new RowData(roleMappedIDstr, textStr));
                                }
                            }
                            arrAdapRoleSel = new ArrayAdapter<RowData>(BranchRoleSelectionActivity.this, R.layout.simple_spinner_item, arrRoleSel);
                            arrAdapRoleSel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spRoleSel.setAdapter(arrAdapRoleSel);
                            spRoleSel.setOnItemSelectedListener(roleListener);
                            customProgress.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(BranchRoleSelectionActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
                    } else {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(BranchRoleSelectionActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");

                    }
                    customProgress.setVisibility(View.GONE);
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
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchRoleSelectionActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
//                    MOSLCommon.showToastMsg(BranchRoleSelectionActivity.this, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchRoleSelectionActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchRoleSelectionActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    private AdapterView.OnItemSelectedListener roleListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            roleID = ((RowData) spRoleSel.getSelectedItem()).getKey();
            getRoleDetails(roleID);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public void getRoleDetails(String roleID) {
        if (MOSLCommon.isNetworkEnabled(this)) {
            Map inputMap = new HashMap<>();
            inputMap.put(APP_LOGIN_ID, getAppLoginID());
            inputMap.put(ROLE_MAPPING_ID, roleID);

            String flag = GETCRMROLEDETAILS;
            String category = LOGIN;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(BranchRoleSelectionActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response != null) {
                        try {
                        /*{"CRMROLE":[{"BOBRANCH":"RBRVLIOWN ",
                                "BOSUBBROKER":"ONLBRVL","BOTRADERS":"DIRECTBVL",
                                "CRMBRANCH":"ONLAHMEDBD","LOGINUSERID":"4546",
                                "ROLEMAPPINGID":"1871","BUSINESSUNITID":"6",
                                "TEAMID":"45","ROLEID":"51"}]}*/
                            JSONArray jsonArray = response.getJSONArray("CRMROLE");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String boBranch = (object.isNull("BOBRANCH") || object.getString("BOBRANCH").equals("")) ? "" : object.getString("BOBRANCH");
                                    String boSubBroker = (object.isNull("BOSUBBROKER") || object.getString("BOSUBBROKER").equals("")) ? "" : object.getString("BOSUBBROKER");
                                    String boTraders = (object.isNull("BOTRADERS") || object.getString("BOTRADERS").equals("")) ? "" : object.getString("BOTRADERS");
                                    String crmBranch = (object.isNull("CRMBRANCH") || object.getString("CRMBRANCH").equals("")) ? "" : object.getString("CRMBRANCH");
                                    String loginUserID = (object.isNull("LOGINUSERID") || object.getString("LOGINUSERID").equals("")) ? "" : object.getString("LOGINUSERID");
                                    String roleMappingID = (object.isNull("ROLEMAPPINGID") || object.getString("ROLEMAPPINGID").equals("")) ? "" : object.getString("ROLEMAPPINGID");
                                    String businessUnitID = (object.isNull("BUSINESSUNITID") || object.getString("BUSINESSUNITID").equals("")) ? "" : object.getString("BUSINESSUNITID");
                                    String teamID = (object.isNull("TEAMID") || object.getString("TEAMID").equals("")) ? "" : object.getString("TEAMID");
                                    String roleID = (object.isNull("ROLEID") || object.getString("ROLEID").equals("")) ? "" : object.getString("ROLEID");

                                    tvBOBranch.setText(boBranch);
                                    tvBOSubBroker.setText(boSubBroker);
                                    tvBOTraders.setText(boTraders);
                                    tvCRMBranch.setText(crmBranch);
                                    roleSelectionModel = new RoleSelectionModel(boBranch, boSubBroker, boTraders, crmBranch, loginUserID, roleMappingID, businessUnitID, teamID, roleID);

                                    customProgress.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(BranchRoleSelectionActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
                    } else {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(BranchRoleSelectionActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                    customProgress.setVisibility(View.GONE);

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
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchRoleSelectionActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
//                    MOSLCommon.showToastMsg(BranchRoleSelectionActivity.this, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchRoleSelectionActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchRoleSelectionActivity.this, SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnViewExistingLeads) {
            Intent intent = new Intent(BranchRoleSelectionActivity.this, PostLoginActivity.class);
            startActivity(intent);
        }
    }
}

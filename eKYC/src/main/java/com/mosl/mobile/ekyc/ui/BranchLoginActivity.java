package com.mosl.mobile.ekyc.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
import static com.mosl.mobile.ekyc.common.MOSLCommon.removeKeyboard;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppLoginID;
import static com.mosl.mobile.ekyc.model.RoleSelectionModel.roleID;

/**
 * Created by raju on 6/24/2016.
 */
public class BranchLoginActivity extends Activity implements MOSLConstants {
    Button btnLogin;
    EditText etClientCode, etClientPass;
    LinearLayout customProgress;
    ArrayList<RowData> arrRoleSel;
    ArrayAdapter<RowData> arrAdapRoleSel;
    Spinner spRoleSel;
    RoleSelectionModel roleSelectionModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_branch_login);
            setPermisson(BranchLoginActivity.this,getApplicationContext());
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);


            btnLogin = (Button) findViewById(R.id.btnLogin);

            etClientCode = (EditText) findViewById(R.id.etClientCode);
            etClientPass = (EditText) findViewById(R.id.etClientPass);

            customProgress = (LinearLayout) findViewById(R.id.customProgress);

            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etClientCode.getText().toString().trim().equalsIgnoreCase("")) {
                        commonMsgDialog(BranchLoginActivity.this, "Please enter user id", false, "");
    //                    MOSLCommon.alertDialog(BranchLoginActivity.this, 0, "Motilal Oswal", "Please enter userid", "OK", true, null);
                    } else if (etClientPass.getText().toString().equalsIgnoreCase("")) {
                        commonMsgDialog(BranchLoginActivity.this, "Please enter password", false, "");
    //                    MOSLCommon.alertDialog(BranchLoginActivity.this, 0, "Motilal Oswal", "Please enter password", "OK", true, null);
                    } else {
                        customProgress.setVisibility(View.VISIBLE);


                        if (MOSLCommon.isNetworkEnabled(BranchLoginActivity.this)) {

                            performLogin(etClientCode.getText().toString().trim(), etClientPass.getText().toString());
    //                        performLogin_1();
                            removeKeyboard(BranchLoginActivity.this, etClientPass);
                        } else {
                            customProgress.setVisibility(View.GONE);

                            MOSLCommon.alertDInternet(BranchLoginActivity.this);
                        }

                    }
                   /* Intent intent = new Intent(BranchLoginActivity.this, PostLoginFragment.class);
                    startActivity(intent);*/

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID()+"-"+getAppLoginID());
//            Crashlytics.logException(e);
        }
    }


    public void performLogin(final String clientCode, final String password) {
        //login parameters
        if (MOSLCommon.isNetworkEnabled(this)) {
            Map inputMap = new HashMap<>();
            inputMap.put("_userid", clientCode);
            inputMap.put("_password", password);

            String flag = CRMLOGIN;
            String category = LOGIN;

        /*Map inputMap = new HashMap<>();
        inputMap.put("Userid", "1451431");

        String flag = GETESIGNDETAILS;
        String category = ESIGN;*/

            WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    //after success code goes here
                    try {
                        if (statusCode == 200) {

                            if (response.length() != 0) {

                                String success = (response.isNull("_Success") || response.getString("_Success").equals("")) ? "" : response.getString("_Success");
                                String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                                int intSuccess = Integer.parseInt(success);
                                if (intSuccess == 1) {

                                    SharedPreferences.Editor editor = MOSLCommon.getPrefs(BranchLoginActivity.this).edit();
                                    editor.putBoolean("AUTHENTICATED", true);
                                    editor.putString("APP_LOGIN_ID", clientCode);
                                    editor.putString("PASSWORD", password);

                                    setAppLoginID(clientCode);

                                    editor.commit();

                                    customProgress.setVisibility(View.GONE);
                                    onLoginSuccess();
                                } else if (intSuccess == 0) {
                                    customProgress.setVisibility(View.GONE);
                                    commonMsgDialog(BranchLoginActivity.this, msg, false, "");
//                                MOSLCommon.alertDialog(BranchLoginActivity.this, 0, MOTILAL_OSWAL, msg, "OK", true, null);
                                }


                            } else {
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(BranchLoginActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                                MOSLCommon.showToastMsg(BranchLoginActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        commonMsgDialog(BranchLoginActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                    MOSLCommon.alertDialog(BranchLoginActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                    }
                    // MOSLCommon.showToastMsg(BranchLoginActivity.this, "Status Code :" + statusCode);
                    customProgress.setVisibility(View.GONE);
                    //  onLoginSuccess();
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchLoginActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");

                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchLoginActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    commonMsgDialog(BranchLoginActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                    MOSLCommon.showToastMsg(BranchLoginActivity.this, responseString);
                    customProgress.setVisibility(View.GONE);
                }

            });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }


    public void onLoginSuccess() {
//        Intent intent = new Intent(BranchLoginActivity.this, PostLoginActivity.class);
        /*Intent intent = new Intent(BranchLoginActivity.this, BranchRoleSelectionActivity.class);
        startActivity(intent);
        finish();*/
        customProgress.setVisibility(View.VISIBLE);
        getCRMRole(getAppLoginID());


    }

    public void showRoleDialog(Context context, ArrayList<RowData> arrRoleSel) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_role_selection);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        spRoleSel = (Spinner) dialog.findViewById(R.id.spRoleSel);
        Button btnGoToEkyc = (Button) dialog.findViewById(R.id.btnGoToEkyc);

        arrAdapRoleSel = new ArrayAdapter<RowData>(BranchLoginActivity.this, R.layout.simple_spinner_item, arrRoleSel);
        arrAdapRoleSel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRoleSel.setAdapter(arrAdapRoleSel);
        spRoleSel.setOnItemSelectedListener(roleListener);
        customProgress.setVisibility(View.GONE);

        btnGoToEkyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RoleSelectionModel.getRoleMappingID() != null && !RoleSelectionModel.getRoleMappingID().equals("")) {
                    Intent intent = new Intent(BranchLoginActivity.this, PostLoginActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    commonMsgDialog(BranchLoginActivity.this, SELECT_ROLE, false, "");
//                    MOSLCommon.showToastMsg(BranchLoginActivity.this, SELECT_ROLE);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void getCRMRole(String appID) {
        if (MOSLCommon.isNetworkEnabled(this)) {
            Map inputMap = new HashMap<>();
            inputMap.put(APP_LOGIN_ID, appID);

            String flag = GETCRMROLE;
            String category = LOGIN;
            customProgress.setVisibility(View.VISIBLE);

            WebServiceHandler.post(BranchLoginActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
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
                                    roleID = roleMappedIDstr;
                                    String defaultRole = (object.isNull("DEFAULTROLE") || object.getString("DEFAULTROLE").equals("")) ? "" : object.getString("DEFAULTROLE");

                                    arrRoleSel.add(new RowData(roleMappedIDstr, textStr));
                                }
                            }
                            if (jsonArray.length() > 1) {
                                showRoleDialog(BranchLoginActivity.this, arrRoleSel);
                            } else if (jsonArray.length() == 1) {
                                getRoleDetails(roleID);
                                Intent intent = new Intent(BranchLoginActivity.this, PostLoginActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                commonMsgDialog(BranchLoginActivity.this, NO_ROLE, false, "");
//                            MOSLCommon.alertDialog(BranchLoginActivity.this, 0, "Motilal Oswal", NO_ROLE, "OK", true, null);
//                            MOSLCommon.showToastMsg(BranchLoginActivity.this, NO_ROLE);
                            }
                        /*arrAdapRoleSel = new ArrayAdapter<RowData>(BranchLoginActivity.this, R.layout.simple_spinner_item, arrRoleSel);
                        arrAdapRoleSel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spRoleSel.setAdapter(arrAdapRoleSel);
                        spRoleSel.setOnItemSelectedListener(roleListener);
                        customProgress.setVisibility(View.GONE);*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        customProgress.setVisibility(View.GONE);

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
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchLoginActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                    MOSLCommon.showToastMsg(BranchLoginActivity.this, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
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
            WebServiceHandler.post(BranchLoginActivity.this, flag, category, inputMap, new JsonHttpResponseHandler() {
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

                                /*tvBOBranch.setText(boBranch);
                                tvBOSubBroker.setText(boSubBroker);
                                tvBOTraders.setText(boTraders);
                                tvCRMBranch.setText(crmBranch);*/

                                    SharedPreferences.Editor editor = MOSLCommon.getPrefs(BranchLoginActivity.this).edit();
                                    editor.putString(BOBRANCH, boBranch);
                                    editor.putString(BOSUBBROKER, boSubBroker);
                                    editor.putString(BOTRADERS, boTraders);
                                    editor.putString(CRMBRANCH, crmBranch);
                                    editor.putString(LOGINUSERID, loginUserID);
                                    editor.putString(ROLEMAPPINGID, roleMappingID);
                                    editor.putString(BUSINESSUNITID, businessUnitID);
                                    editor.putString(TEAMID, teamID);
                                    editor.putString(ROLEID, roleID);

                                    editor.commit();
                                    roleSelectionModel = new RoleSelectionModel(boBranch, boSubBroker, boTraders, crmBranch, loginUserID, roleMappingID, businessUnitID, teamID, roleID);

                                    customProgress.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        customProgress.setVisibility(View.GONE);
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
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(BranchLoginActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                    MOSLCommon.showToastMsg(BranchLoginActivity.this, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

}

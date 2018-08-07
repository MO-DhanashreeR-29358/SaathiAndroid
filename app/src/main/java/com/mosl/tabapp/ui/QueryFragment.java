package com.mosl.tabapp.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.ws.CRMWebServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mosl.tabapp.common.CRMAppUtil.filterOnlyAlphabetAndNumbersAnsAllowedChar;
import static com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;


/**
 * Created by Raju on 3/10/2017.
 */
public class QueryFragment extends MOSLCommonFragment implements View.OnClickListener {
    View queryView;
    Button btnSubmit, btnReset;
    EditText edttxtYourName, edttxtEmailAddress, edttxtMobileNo, edttxtBranchCode, edQueryText;
    TextView telephoneno1, telephoneno2;

    Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        queryView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_query);
        setUpView(queryView);

        return queryView;

    }

    private void setUpView(View queryView) {
        setAppTitle(getClass().toString(), "Insert Your Query");

        edttxtYourName = (EditText) queryView.findViewById(R.id.edttxtYourName);
        edttxtYourName.setEnabled(false);
        edttxtYourName.setText(AccountDetails.getFirstName());

        edttxtEmailAddress = (EditText) queryView.findViewById(R.id.edttxtEmailAddress);
        edttxtEmailAddress.setEnabled(false);
        edttxtEmailAddress.setText(AccountDetails.getEmail());

        edttxtMobileNo = (EditText) queryView.findViewById(R.id.edttxtMobileNo);
        edttxtMobileNo.setEnabled(false);
        edttxtMobileNo.setText(AccountDetails.getMobileNo());

        edttxtBranchCode = (EditText) queryView.findViewById(R.id.edttxtBranchCode);
        edttxtBranchCode.setEnabled(false);
        edttxtBranchCode.setText(AccountDetails.getBranchCode());


        edQueryText = (EditText) queryView.findViewById(R.id.edQueryText);
        edQueryText.setFilters(new InputFilter[]{filterOnlyAlphabetAndNumbersAnsAllowedChar});
        btnSubmit = (Button) queryView.findViewById(R.id.btnSubmit);
        btnReset = (Button) queryView.findViewById(R.id.btnReset);

        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        telephoneno1 = (TextView) queryView.findViewById(R.id.txtTelephoneNo1);
        telephoneno2 = (TextView) queryView.findViewById(R.id.txtTelephoneNo2);

        telephoneno1.setOnClickListener(this);
        telephoneno2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit: {
                hideKeyboard(v);
                if (validateForm()) {
                    submitQuery();
                }
            }
            case R.id.btnReset: {
                hideKeyboard(v);
                edQueryText.setText("");
                break;
            }

            case R.id.txtTelephoneNo1:
                hideKeyboard(v);
                MSFDialog.alertDialog(getActivity(), 0, getResources().getString(R.string.motilal_oswal), "Do you want to call?", "Yes", "No", true, new MSFDialog.DialogListener() {
                    @Override
                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                        if (action == MSFDialog.Action.OK) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + "02239982515"));
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(getActivity(), "No permission for call", Toast.LENGTH_LONG).show();
                                return;
                            }
                            startActivity(callIntent);
                        }
                    }
                });
                break;
            case R.id.txtTelephoneNo2:
                hideKeyboard(v);
                MSFDialog.alertDialog(getActivity(), 0, getResources().getString(R.string.motilal_oswal), "Do you want to call?", "Yes", "No", true, new MSFDialog.DialogListener() {
                    @Override
                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                        if (action == MSFDialog.Action.OK) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + "02267490600"));
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(getActivity(), "No permission for call", Toast.LENGTH_LONG).show();
                                return;
                            }
                            startActivity(callIntent);
                        }
                    }
                });
                break;
        }
    }


    private void submitQuery() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            Map inputMap = new HashMap<>();
            inputMap.put("Description", getJsonSafeString(edQueryText.getText().toString().trim()));
            inputMap.put("PartnerName", edttxtYourName.getText().toString().trim());
            inputMap.put("Email", edttxtEmailAddress.getText().toString().trim());
            inputMap.put("mobile", edttxtMobileNo.getText().toString().trim());
            inputMap.put("BranchCode", edttxtBranchCode.getText().toString().trim());
            inputMap.put("Status", 1);


            String serviceMethod = "Common/RequetQuery";
            showProgress();
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
//                                    customProgress.setVisibility(View.GONE);
                                    //Log.v(TAG, "Got it");
                                    edQueryText.setText("");
                                    String message = "Thanks for submitting your query.";
                                    alertDialog(getActivity(), 0, getResources().getString(R.string.motilal_oswal), message, "OK", true, new MSFDialog.DialogListener() {
                                        @Override
                                        public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                            if (action == MSFDialog.Action.OK) {
                                              /*  Bundle args = new Bundle();
                                                args.putString("", "");
                                                navigateTo(NAV_TO_ADVISORY_DASHBOARD, args, getFragmentManager(), true);*/
                                                dashbordFragment();

                                            }
                                        }
                                    });

                                } else {
//                                    customProgress.setVisibility(View.GONE);
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hideProgress();

                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();

                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();

                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            //eat it
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);

            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
            return;

        }
    }

    private boolean validateForm() {
        if (edQueryText.getText().toString().equalsIgnoreCase("")) {
            edQueryText.requestFocus();
            return false;
        }
        return true;
    }

}


package com.mosl.tabapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.mosl.app.core.data.UiModel;
import com.mosl.saathi.api.model.query.QueryItem;
import com.mosl.saathi.api.model.query.QueryRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.model.AccountDetails;
import io.reactivex.disposables.Disposable;

import static com.mosl.app.core.data.UiModelHelper.applyUiModel;
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
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + "02239982515"));
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
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + "02267490600"));
                            startActivity(callIntent);
                        }
                    }
                });
                break;
        }
    }

    private void renderQuery(UiModel<QueryItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        QueryItem data = uiModel.data;
        if (data != null) {
            edQueryText.setText("");
            String message = "Thanks for submitting your query.";
            alertDialog(getActivity(), 0, getResources().getString(R.string.motilal_oswal), message,
                "OK", true,
                (action, data1) -> {
                    if (action == MSFDialog.Action.OK) {
                        dashbordFragment();
                    }
                });
        }
    }

    private void submitQuery() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            int status = 1;
            String branchCode = edttxtBranchCode.getText().toString().trim();
            String email = edttxtEmailAddress.getText().toString().trim();
            String mobile = edttxtMobileNo.getText().toString().trim();
            String partnerName = edttxtYourName.getText().toString().trim();
            String description = getJsonSafeString(edQueryText.getText().toString().trim());
            QueryRequest request =
                new QueryRequest(status, description, email, mobile, partnerName, branchCode);
            Disposable subscribe = saathiApi.postQuery(request)
                .compose(applyUiModel())
                .subscribe(this::renderQuery);
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
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


package com.mosl.mobile.ekyc.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.model.RowData;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getPANno;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCVLVerified;

/**
 * Created by Vijay on 8/31/2016.
 */
public class TrackApplicationActivity extends FragmentActivity implements View.OnClickListener, MOSLConstants {

    private ListView lvTrackApplication;
    LinearLayout llClientInfo, llFundTransfer, llProofVerification, llApplicationSubmitted, llDispacthStatus, llKeyProofUploaded, llMFAppilcationStatus, llMFProductStatus, llKyc1, llKyc2, llKyc3, llKyc4, llKyc5;
    //    LinearLayout llFirstProductStatus,llSecondProductStatus;
    RelativeLayout customProgress;
    //    Button btnFundTransfer, btnProofVerification, btnApplicationSubmitted, btnAccountActivated,btnClose;
    Button btnClose;
    TextView tvClientInformation, tvFundTransfer, tvProofVerification, tvApplicationSubmitted, tvDispacthStatusHeader, tvMFApplicationStatus;
    TextView tvName, tvEmailId, tvMobileNumber, tvPANNumber, tvAadhaarNumber, tvFirstProduct, tvSecondProduct, tvTransactionReffNo, tvBankTransID, tvFundTransferThrough,
            tvFundTransferDate, tvBankName, tvAmountTransferred, tvPanStatus, tvBankEqStatus, tvBankCommStatus, tvIncomeProofStatus, tvFormStatus, tvClientCode, tvDPID, tvApplicationSubmittedDate, tvDispatchDate,
            tvCourierName, tvAirwayBillNo, tvDispatchStatus, tvKeyProofUploaded;
    //    TextView tvStatus,tvSecondProductStatus;
    TextView tvKyc1, tvKyc2, tvKyc3, tvKyc4, tvKyc5, tvMFBankProof, tvMFCorresAddressProof, tvMFPermAddressProof, tvPANCopy, tvMFProofVerificationStatus, tvMFBankProofStatus, tvMFClientCode, tvMFAppSubmittedDate;
    Spinner spProduct;
    Map<String, Map<String, String>> allDetailMap, mfAllDetailmap;
    ArrayList<RowData> arrProduct;
    ArrayAdapter<RowData> arrAdapProduct;
    String productValueStr;
    TextView tvMFProductStatus, tvNoDataFound;
    ScrollView scrollView;

    private final AdapterView.OnItemSelectedListener productListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                productValueStr = ((RowData) spProduct.getSelectedItem()).getValue();
                if (productValueStr.equalsIgnoreCase(SELECT)) {
                    llMFProductStatus.setVisibility(View.GONE);
                    // llFirstProductStatus.setVisibility(View.GONE);
//                    llSecondProductStatus.setVisibility(View.GONE);

                    tvMFApplicationStatus.setVisibility(View.GONE);
                    tvKeyProofUploaded.setVisibility(View.GONE);
                    tvDispacthStatusHeader.setVisibility(View.GONE);
                    llDispacthStatus.setVisibility(View.GONE);

                    llMFAppilcationStatus.setVisibility(View.GONE);
                    llFundTransfer.setVisibility(View.GONE);
                    llProofVerification.setVisibility(View.GONE);
                    llApplicationSubmitted.setVisibility(View.GONE);
                    llDispacthStatus.setVisibility(View.GONE);
                    llKeyProofUploaded.setVisibility(View.GONE);
                    llMFAppilcationStatus.setVisibility(View.GONE);
                }

                if (productValueStr.equalsIgnoreCase(MF)) {
                    llMFProductStatus.setVisibility(View.VISIBLE);
                    //  llFirstProductStatus.setVisibility(View.GONE);
//                    llSecondProductStatus.setVisibility(View.GONE);
                    setTrackDetail(productValueStr, mfAllDetailmap);
                } else {
                    llMFProductStatus.setVisibility(View.GONE);
                    // llFirstProductStatus.setVisibility(View.VISIBLE);
//                    llSecondProductStatus.setVisibility(View.VISIBLE);
                    setTrackDetail(productValueStr, allDetailMap);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.track_application);
            setPermisson(TrackApplicationActivity.this, getApplicationContext());
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            setUpView();
            String userId = "";
            if (getIntent().getExtras() != null) {
                userId = getIntent().getExtras().getString(USERID);
            } else {
                userId = getUserID();
            }
            applicationTracker(userId);
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    private void setUpView() {
        llClientInfo = findViewById(R.id.llClientInfo);
        llFundTransfer = findViewById(R.id.llFundTransfer);
        llProofVerification = findViewById(R.id.llProofVerification);
        llApplicationSubmitted = findViewById(R.id.llApplicationSubmitted);
        llDispacthStatus = findViewById(R.id.llDispacthStatus);
        llKeyProofUploaded = findViewById(R.id.llKeyProofUploaded);
        llMFAppilcationStatus = findViewById(R.id.llMFAppilcationStatus);
        llMFProductStatus = findViewById(R.id.llMFProductStatus);
        // llFirstProductStatus = findViewById(R.id.llFirstProductStatus);
        //        llSecondProductStatus = findViewById(R.id.llSecondProductStatus);
        llKyc1 = findViewById(R.id.llKyc1);
        llKyc2 = findViewById(R.id.llKyc2);
        llKyc3 = findViewById(R.id.llKyc3);
        llKyc4 = findViewById(R.id.llKyc4);
        llKyc5 = findViewById(R.id.llKyc5);
        customProgress = findViewById(R.id.customProgress);
        btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*btnFundTransfer = findViewById(R.id.btnFundTransfer);
        btnProofVerification = findViewById(R.id.btnProofVerification);
        btnApplicationSubmitted = findViewById(R.id.btnApplicationSubmitted);
        btnAccountActivated = findViewById(R.id.btnAccountActivated);

        btnFundTransfer.setOnClickListener(this);
        btnProofVerification.setOnClickListener(this);
        btnApplicationSubmitted.setOnClickListener(this);
        btnAccountActivated.setOnClickListener(this);*/

        tvName = findViewById(R.id.tvName);
        tvEmailId = findViewById(R.id.tvEmailId);
        tvMobileNumber = findViewById(R.id.tvMobileNumber);
        tvPANNumber = findViewById(R.id.tvPANNumber);
        tvAadhaarNumber = findViewById(R.id.tvAadhaarNumber);
        //        tvFirstProduct = findViewById(R.id.tvFirstProduct);
        //        tvSecondProduct = findViewById(R.id.tvSecondProduct);
        //        tvStatus = findViewById(R.id.tvStatus);
        //        tvSecondProductStatus = findViewById(R.id.tvSecondProductStatus);
        tvTransactionReffNo = findViewById(R.id.tvTransactionReffNo);
        tvBankTransID = findViewById(R.id.tvBankTransID);
        tvFundTransferThrough = findViewById(R.id.tvFundTransferThrough);
        tvFundTransferDate = findViewById(R.id.tvFundTransferDate);
        tvBankName = findViewById(R.id.tvBankName);
        tvAmountTransferred = findViewById(R.id.tvAmountTransferred);
        tvPanStatus = findViewById(R.id.tvPanStatus);
        tvBankEqStatus = findViewById(R.id.tvBankEqStatus);
        tvBankCommStatus = findViewById(R.id.tvBankCommStatus);
        tvIncomeProofStatus = findViewById(R.id.tvIncomeProofStatus);
        tvFormStatus = findViewById(R.id.tvFormStatus);
        tvClientCode = findViewById(R.id.tvClientCode);
        tvDPID = findViewById(R.id.tvDPID);
        tvApplicationSubmittedDate = findViewById(R.id.tvApplicationSubmittedDate);
        tvDispatchDate = findViewById(R.id.tvDispatchDate);
        tvCourierName = findViewById(R.id.tvCourierName);
        tvAirwayBillNo = findViewById(R.id.tvAirwayBillNo);
        tvDispatchStatus = findViewById(R.id.tvDispatchStatus);

        tvClientInformation = findViewById(R.id.tvClientInformation);
        tvFundTransfer = findViewById(R.id.tvFundTransfer);
        tvProofVerification = findViewById(R.id.tvProofVerification);
        tvApplicationSubmitted = findViewById(R.id.tvApplicationSubmitted);
        tvDispacthStatusHeader = findViewById(R.id.tvDispacthStatusHeader);
        tvMFApplicationStatus = findViewById(R.id.tvMFApplicationStatus);
        tvKeyProofUploaded = findViewById(R.id.tvKeyProofUploaded);

        tvKyc1 = findViewById(R.id.tvKyc1);
        tvKyc2 = findViewById(R.id.tvKyc2);
        tvKyc3 = findViewById(R.id.tvKyc3);
        tvKyc4 = findViewById(R.id.tvKyc4);
        tvKyc5 = findViewById(R.id.tvKyc5);
        tvMFBankProof = findViewById(R.id.tvMFBankProof);
        tvMFCorresAddressProof = findViewById(R.id.tvMFCorresAddressProof);
        tvMFPermAddressProof = findViewById(R.id.tvMFPermAddressProof);
        tvPANCopy = findViewById(R.id.tvPANCopy);
        tvMFProofVerificationStatus = findViewById(R.id.tvMFProofVerificationStatus);
        //        tvMFBankProofStatus = findViewById(R.id.tvMFBankProofStatus);
        tvMFClientCode = findViewById(R.id.tvMFClientCode);
        tvMFAppSubmittedDate = findViewById(R.id.tvMFAppSubmittedDate);
        tvMFProductStatus = findViewById(R.id.tvMFProductStatus);
        tvNoDataFound = findViewById(R.id.tvNoDataFound);

        scrollView = findViewById(R.id.scrollView);

        //        tvAccountActivated = findViewById(R.id.tvAccountActivated);
        spProduct = findViewById(R.id.spProduct);

        tvFundTransfer.setOnClickListener(this);
        tvProofVerification.setOnClickListener(this);
        tvApplicationSubmitted.setOnClickListener(this);
        tvDispacthStatusHeader.setOnClickListener(this);
        tvKeyProofUploaded.setOnClickListener(this);
        tvMFApplicationStatus.setOnClickListener(this);
//        tvAccountActivated.setOnClickListener(this);

        setIcon(llClientInfo, tvClientInformation);
        setIcon(llFundTransfer, tvFundTransfer);
        setIcon(llProofVerification, tvProofVerification);
        setIcon(llApplicationSubmitted, tvApplicationSubmitted);
        setIcon(llDispacthStatus, tvDispacthStatusHeader);
        setIcon(llKeyProofUploaded, tvKeyProofUploaded);
        setIcon(llMFAppilcationStatus, tvMFApplicationStatus);
//        setIcon(llAccountActivated, tvAccountActivated);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        /*if (id == R.id.btnFundTransfer) {
            llFundTransfer.setVisibility(View.VISIBLE);
            llProofVerification.setVisibility(View.GONE);
            llApplicationSubmitted.setVisibility(View.GONE);
            llAccountActivated.setVisibility(View.GONE);
        } else if (id == R.id.btnProofVerification) {
            llProofVerification.setVisibility(View.VISIBLE);
            llFundTransfer.setVisibility(View.GONE);
            llApplicationSubmitted.setVisibility(View.GONE);
            llAccountActivated.setVisibility(View.GONE);
        } else if (id == R.id.btnApplicationSubmitted) {
            llApplicationSubmitted.setVisibility(View.VISIBLE);
            llFundTransfer.setVisibility(View.GONE);
            llProofVerification.setVisibility(View.GONE);
            llAccountActivated.setVisibility(View.GONE);
        } else if (id == R.id.btnAccountActivated) {
            llAccountActivated.setVisibility(View.VISIBLE);
            llFundTransfer.setVisibility(View.GONE);
            llProofVerification.setVisibility(View.GONE);
            llApplicationSubmitted.setVisibility(View.GONE);
        } else*/
        if (id == R.id.tvFundTransfer) {
            toggleSectionExpansion(llFundTransfer, tvFundTransfer);
        } else if (id == R.id.tvProofVerification) {
            toggleSectionExpansion(llProofVerification, tvProofVerification);
        } else if (id == R.id.tvApplicationSubmitted) {
            toggleSectionExpansion(llApplicationSubmitted, tvApplicationSubmitted);
        } else if (id == R.id.tvDispacthStatusHeader) {
            toggleSectionExpansion(llDispacthStatus, tvDispacthStatusHeader);
        } else if (id == R.id.tvKeyProofUploaded) {
            toggleSectionExpansion(llKeyProofUploaded, tvKeyProofUploaded);
        } else if (id == R.id.tvMFApplicationStatus) {
            toggleSectionExpansion(llMFAppilcationStatus, tvMFApplicationStatus);
        }
    }

    private void toggleSectionExpansion(View parent, TextView icon) {
        if (parent.getVisibility() == View.VISIBLE) {
            parent.setVisibility(View.GONE);
            icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0);
        } else {
            parent.setVisibility(View.VISIBLE);
            icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
        }
    }

    private void setIcon(View parent, TextView icon) {
        if (parent.getVisibility() == View.VISIBLE) {
            //parent.setVisibility(View.GONE);
            icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
        } else {
            //  parent.setVisibility(View.VISIBLE);
            icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0);
        }
    }

    public void applicationTracker(String userId) {
        if (MOSLCommon.isNetworkEnabled(this)) {
            customProgress.setVisibility(View.VISIBLE);
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("PanNo", getPANno());
            inputMap.put("UserId", userId);
            inputMap.put("_Otp", "");
            inputMap.put("_uniqueId", "");

            WebServiceHandler.post(TrackApplicationActivity.this, TRACKAPPLICATION, TRACK, inputMap,
                new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    arrProduct = new ArrayList<>();
                    if (response != null) {
                        arrProduct.clear();
                        arrProduct.add(new RowData(0, "Select"));
                        try {
                            if (response.has("ApplicationTracker")) {
                                // product list
                                JSONArray jsonArray = response.getJSONArray("ApplicationTracker");
                                if (jsonArray.length() != 0) {
                                    scrollView.setVisibility(View.VISIBLE);
                                    tvNoDataFound.setVisibility(View.GONE);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String product = (object.isNull("PRODUCT") || object.getString("PRODUCT").equals("")) ? "" : object.getString("PRODUCT");

                                        arrProduct.add(new RowData(i + 1, product));
                                    }
                                    arrAdapProduct = new ArrayAdapter<>(TrackApplicationActivity.this, R.layout.simple_spinner_item, arrProduct);
                                    arrAdapProduct.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spProduct.setAdapter(arrAdapProduct);
                                    spProduct.setOnItemSelectedListener(productListener);

                                    for (int j = 0; j < arrProduct.size(); j++) {
                                        if (arrProduct.size() > 1) {
                                            if (arrAdapProduct.getItem(j).getValue().equalsIgnoreCase(EQUITY)) {
                                                spProduct.setSelection(j);
                                            }
                                        } else {
                                            spProduct.setSelection(j);
                                        }
                                    }
                                } else {
                                    scrollView.setVisibility(View.GONE);
                                    tvNoDataFound.setVisibility(View.VISIBLE);
                                }

                            }
                            if (response.has("ApplicationTracker1")) {
                                JSONArray jsonArray = response.getJSONArray("ApplicationTracker1");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String isAddharVerified = (object.isNull("ISADDHARVERIFIED") || object.getString("ISADDHARVERIFIED").equals("")) ? "" : object.getString("ISADDHARVERIFIED");
                                        if (MOSLCommon.isBlank(isAddharVerified) || isAddharVerified.trim().equals("0")) {
                                            tvDispacthStatusHeader.setVisibility(View.VISIBLE);
//                                            llDispacthStatus.setVisibility(View.VISIBLE);
                                            tvProofVerification.setVisibility(View.GONE);
                                            llProofVerification.setVisibility(View.GONE);
                                        } else if (isAddharVerified.trim().equals("1")) {
                                            tvProofVerification.setVisibility(View.VISIBLE);
//                                            llProofVerification.setVisibility(View.VISIBLE);
                                            tvDispacthStatusHeader.setVisibility(View.GONE);
                                            llDispacthStatus.setVisibility(View.GONE);
                                        }
                                    }
                                }
                            }
                            if (response.has("ApplicationTracker2")) {
                                JSONArray jsonArray = response.getJSONArray("ApplicationTracker2");
                                allDetailMap = new HashMap<>();
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        /*"Name":"Vaibhav Dasharath Chavan",
                                                "EmailID":"vaibhav.chavan@outlook.com",
                                                "MobileNo":"9833864385",
                                                "ProductType":"Equity",
                                                "PANNO":"AKEPC9781A",
                                                "AadhaarNumber":"596342931104",
                                                "Status":"IPV",
                                                "TransRefNo":"",
                                                "BankTransID":"",
                                                "FundTransferredAmount":null,
                                                "FundTransferredDate":null,
                                                "BankName":"ICICI BANK LTD",
                                                "FundTransferThrough":"Net Banking",
                                                "PANSTATUS":"PAN Copy",
                                                "BKEQSTATUS":"Cancel Cheque",
                                                "BKCOMMSTATUS":"N.A",
                                                "INCOMEPROOFSTATUS":"N.A",
                                                "ClientCode":"",
                                                "DPID":"",
                                                "AdvisorName":"",
                                                "ISADDHARVERIFIED":"1"*/
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        Map<String, String> stageMap = new HashMap<>();
                                        String name = (object.isNull(Name) || object.getString(Name).equals("")) ? "" : object.getString(Name);
                                        String emailId = (object.isNull(EmailID) || object.getString(EmailID).equals("")) ? "" : object.getString(EmailID);
                                        String mobileNo = (object.isNull(MobileNo) || object.getString(MobileNo).equals("")) ? "" : object.getString(MobileNo);
                                        String productType = (object.isNull(ProductType) || object.getString(ProductType).equals("")) ? "" : object.getString(ProductType);
                                        String panNo = (object.isNull(PANNO) || object.getString(PANNO).equals("")) ? "" : object.getString(PANNO);
                                        String aadhaarNumber = (object.isNull(AadhaarNumber) || object.getString(AadhaarNumber).equals("")) ? "" : object.getString(AadhaarNumber);
                                        String status = (object.isNull(Status) || object.getString(Status).equals("")) ? "" : object.getString(Status);
                                        String transRefNo = (object.isNull(TransRefNo) || object.getString(TransRefNo).equals("")) ? "" : object.getString(TransRefNo);
                                        String bankTransId = (object.isNull(BankTransID) || object.getString(BankTransID).equals("")) ? "" : object.getString(BankTransID);
                                        String fundTransferredAmount = (object.isNull(FundTransferredAmount) || object.getString(FundTransferredAmount).equals("")) ? "" : object.getString(FundTransferredAmount);
                                        String fundTransferredDate = (object.isNull(FundTransferredDate) || object.getString(FundTransferredDate).equals("")) ? "" : object.getString(FundTransferredDate);
                                        String bankName = (object.isNull(BankName) || object.getString(BankName).equals("")) ? "" : object.getString(BankName);
                                        String fundTransferThrough = (object.isNull(FundTransferThrough) || object.getString(FundTransferThrough).equals("")) ? "" : object.getString(FundTransferThrough);
                                        String isAddharVerified = (object.isNull(ISADDHARVERIFIED) || object.getString(ISADDHARVERIFIED).equals("")) ? "" : object.getString(ISADDHARVERIFIED);
                                        if (MOSLCommon.isBlank(isAddharVerified) || isAddharVerified.trim().equals("0")) {
                                            String dispatchDate = (object.isNull(DispatchDate) || object.getString(DispatchDate).equals("")) ? "" : object.getString(DispatchDate);
                                            String dispatchStatus = (object.isNull(DispatchStatus) || object.getString(DispatchStatus).equals("")) ? "" : object.getString(DispatchStatus);
                                            String formNo = (object.isNull(FormNo) || object.getString(FormNo).equals("")) ? "" : object.getString(FormNo);
                                            String courierName = (object.isNull(CourierName) || object.getString(CourierName).equals("")) ? "" : object.getString(CourierName);
                                            String airwayBillNo = (object.isNull(AirwayBillNo) || object.getString(AirwayBillNo).equals("")) ? "" : object.getString(AirwayBillNo);
                                            String dispatchAddress = (object.isNull(DispatchAddress) || object.getString(DispatchAddress).equals("")) ? "" : object.getString(DispatchAddress);
                                            String rtoReason = (object.isNull(RTOReason) || object.getString(RTOReason).equals("")) ? "" : object.getString(RTOReason);
                                            stageMap.put(DispatchDate, dispatchDate);
                                            stageMap.put(DispatchStatus, dispatchStatus);
                                            stageMap.put(CourierName, courierName);
                                            stageMap.put(AirwayBillNo, airwayBillNo);
                                            stageMap.put(PANSTATUS, "");
                                            stageMap.put(BKEQSTATUS, "");
                                            stageMap.put(BKCOMMSTATUS, "");
                                            stageMap.put(INCOMEPROOFSTATUS, "");
                                        } else if (isAddharVerified.trim().equals("1")) {
                                            String panStatus = (object.isNull(PANSTATUS) || object.getString(PANSTATUS).equals("")) ? "" : object.getString(PANSTATUS);
                                            String bkEqStatus = (object.isNull(BKEQSTATUS) || object.getString(BKEQSTATUS).equals("")) ? "" : object.getString(BKEQSTATUS);
                                            String bkCommStatus = (object.isNull(BKCOMMSTATUS) || object.getString(BKCOMMSTATUS).equals("")) ? "" : object.getString(BKCOMMSTATUS);
                                            String incomeProofStatus = (object.isNull(INCOMEPROOFSTATUS) || object.getString(INCOMEPROOFSTATUS).equals("")) ? "" : object.getString(INCOMEPROOFSTATUS);
                                            stageMap.put(PANSTATUS, panStatus);
                                            stageMap.put(BKEQSTATUS, bkEqStatus);
                                            stageMap.put(BKCOMMSTATUS, bkCommStatus);
                                            stageMap.put(INCOMEPROOFSTATUS, incomeProofStatus);
                                            stageMap.put(DispatchDate, "");
                                            stageMap.put(DispatchStatus, "");
                                            stageMap.put(CourierName, "");
                                            stageMap.put(AirwayBillNo, "");
                                        }
                                        String clientCode = (object.isNull(ClientCode) || object.getString(ClientCode).equals("")) ? "" : object.getString(ClientCode);
                                        String dpid = (object.isNull(DPID) || object.getString(DPID).equals("")) ? "" : object.getString(DPID);
                                        String advisorName = (object.isNull(AdvisorName) || object.getString(AdvisorName).equals("")) ? "" : object.getString(AdvisorName);
                                        String applicationSubmitDate = (object.isNull(ApplicationSubmitDate) || object.getString(ApplicationSubmitDate).equals("")) ? "" : object.getString(ApplicationSubmitDate);


                                        stageMap.put(Name, name);
                                        stageMap.put(EmailID, emailId);
                                        stageMap.put(MobileNo, mobileNo);
                                        stageMap.put(ProductType, productType);
                                        stageMap.put(PANNO, panNo);
                                        if (aadhaarNumber.equals("0")) {
                                            aadhaarNumber = "";
                                        }
                                        stageMap.put(AadhaarNumber, aadhaarNumber);
                                        stageMap.put(Status, status);
                                        stageMap.put(TransRefNo, transRefNo);
                                        stageMap.put(BankTransID, bankTransId);
                                        stageMap.put(FundTransferredAmount, fundTransferredAmount);
                                        stageMap.put(FundTransferredDate, fundTransferredDate);
                                        stageMap.put(BankName, bankName);
                                        stageMap.put(FundTransferThrough, fundTransferThrough);
                                        stageMap.put(ClientCode, clientCode);
                                        stageMap.put(DPID, dpid);
                                        stageMap.put(AdvisorName, advisorName);
                                        stageMap.put(ApplicationSubmitDate, applicationSubmitDate);
                                        stageMap.put(ISADDHARVERIFIED, isAddharVerified);

                                        allDetailMap.put(productType, stageMap);
                                        if (i == 0) {
                                            tvName.setText(name);
                                            tvEmailId.setText(emailId);
                                            tvMobileNumber.setText(mobileNo);
                                            tvPANNumber.setText(panNo);
                                            tvAadhaarNumber.setText(aadhaarNumber);
//                                            tvFirstProduct.setText(productType);
//                                            tvStatus.setText(status);
                                        } else if (i == 1) {
//                                            tvSecondProduct.setText(productType);
//                                            tvSecondProductStatus.setText(status);
                                        }

                                    }
                                }
                            }

                            if (response.has("MFDATA")) {
                                JSONArray jsonArray = response.getJSONArray("MFDATA");
                                mfAllDetailmap = new HashMap<String, Map<String, String>>();
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        Map<String, String> stageMap = new HashMap();
                                        String name = (object.isNull("NAME") || object.getString("NAME").equals("")) ? "" : object.getString("NAME");
                                        String emailId = (object.isNull("EMAILID") || object.getString("EMAILID").equals("")) ? "" : object.getString("EMAILID");
                                        String mobileNo = (object.isNull("MOBILENO") || object.getString("MOBILENO").equals("")) ? "" : object.getString("MOBILENO");
                                        String productType = (object.isNull("PRODUCTTYPE") || object.getString("PRODUCTTYPE").equals("")) ? "" : object.getString("PRODUCTTYPE");
                                        String panNo = (object.isNull("PANNO") || object.getString("PANNO").equals("")) ? "" : object.getString("PANNO");
                                        String aadharNo = (object.isNull("AADHAARNUMBER") || object.getString("AADHAARNUMBER").equals("")) ? "" : object.getString("AADHAARNUMBER");
                                        String status = (object.isNull("STATUS") || object.getString("STATUS").equals("")) ? "" : object.getString("STATUS");

                                        String panStatus = (object.isNull("PANSTATUS") || object.getString("PANSTATUS").equals("")) ? "" : object.getString("PANSTATUS");
                                        String corresAddProof = (object.isNull("CORRES_ADD_PROOF") || object.getString("CORRES_ADD_PROOF").equals("")) ? "" : object.getString("CORRES_ADD_PROOF");
                                        String permAddProof = (object.isNull("PERMANENT_ADD_PROOF") || object.getString("PERMANENT_ADD_PROOF").equals("")) ? "" : object.getString("PERMANENT_ADD_PROOF");
                                        String bkmfStatus = (object.isNull("BKMFSTATUS") || object.getString("BKMFSTATUS").equals("")) ? "" : object.getString("BKMFSTATUS");
                                        String clientCode = (object.isNull("CLIENTCODE") || object.getString("CLIENTCODE").equals("")) ? "" : object.getString("CLIENTCODE");
                                        String appSubmitDate = (object.isNull("ApplicationSubmitDate") || object.getString("ApplicationSubmitDate").equals("")) ? "" : object.getString("ApplicationSubmitDate");
                                        String isAadharVerified = (object.isNull("ISADDHARVERIFIED") || object.getString("ISADDHARVERIFIED").equals("")) ? "" : object.getString("ISADDHARVERIFIED");
                                        String isCVLVerified = (object.isNull("ISCVLVERIFIED") || object.getString("ISCVLVERIFIED").equals("")) ? "" : object.getString("ISCVLVERIFIED");
                                        String dispatchDate = (object.isNull("DISPATCHDATE") || object.getString("DISPATCHDATE").equals("")) ? "" : object.getString("DISPATCHDATE");
                                        String dispatchStatus = (object.isNull("DISPATCHSTATUS") || object.getString("DISPATCHSTATUS").equals("")) ? "" : object.getString("DISPATCHSTATUS");
                                        String formNo = (object.isNull("FORM_NO") || object.getString("FORM_NO").equals("")) ? "" : object.getString("FORM_NO");
                                        String courierName = (object.isNull("COURIER_NAME") || object.getString("COURIER_NAME").equals("")) ? "" : object.getString("COURIER_NAME");
                                        String airwayBillNo = (object.isNull("AIRWAY_BILL_NO") || object.getString("AIRWAY_BILL_NO").equals("")) ? "" : object.getString("AIRWAY_BILL_NO");
                                        String dispatchAddress = (object.isNull("DISPATCHADDRESS") || object.getString("DISPATCHADDRESS").equals("")) ? "" : object.getString("DISPATCHADDRESS");
                                        String rtoReason = (object.isNull("RTO_REASON") || object.getString("RTO_REASON").equals("")) ? "" : object.getString("RTO_REASON");
                                        String kyc1 = (object.isNull("KYC1") || object.getString("KYC1").equals("")) ? "" : object.getString("KYC1");
                                        String kyc2 = (object.isNull("KYC2") || object.getString("KYC2").equals("")) ? "" : object.getString("KYC2");
                                        String kyc3 = (object.isNull("KYC3") || object.getString("KYC3").equals("")) ? "" : object.getString("KYC3");
                                        String kyc4 = (object.isNull("KYC4") || object.getString("KYC4").equals("")) ? "" : object.getString("KYC4");
                                        String kyc5 = (object.isNull("KYC5") || object.getString("KYC5").equals("")) ? "" : object.getString("KYC5");

                                        tvFundTransfer.setVisibility(View.GONE);
                                        llFundTransfer.setVisibility(View.GONE);
                                        tvApplicationSubmitted.setVisibility(View.GONE);
                                        llApplicationSubmitted.setVisibility(View.GONE);
                                        llMFProductStatus.setVisibility(View.VISIBLE);
                                        //   llFirstProductStatus.setVisibility(View.GONE);
//                                        llSecondProductStatus.setVisibility(View.GONE);
                                        tvName.setText(name);
                                        tvEmailId.setText(emailId);
                                        tvMobileNumber.setText(mobileNo);
                                        tvPANNumber.setText(panNo);
                                        if (aadharNo.equals("0")) {
                                            aadharNo = "";
                                        }
                                        tvAadhaarNumber.setText(aadharNo);
                                        tvMFProductStatus.setText(status);
                                        tvMFClientCode.setText(clientCode);

                                        stageMap.put(PANSTATUS, panStatus);
                                        stageMap.put(CORRESS_ADD_PROOF, corresAddProof);
                                        stageMap.put(PERM_ADD_PROOF, permAddProof);
                                        stageMap.put(BANK_STATUS, bkmfStatus);
                                        stageMap.put(CLIENT_CODE, clientCode);
                                        stageMap.put(STATUS, status);
                                        stageMap.put(APP_SUBMIT_DATE, appSubmitDate);
                                        stageMap.put(IS_AADHAAR_VERIFIED, isAadharVerified);
                                        stageMap.put(IS_CVL_VERIFIED, isCVLVerified);
                                        stageMap.put(DispatchDate, dispatchDate);
                                        stageMap.put(DispatchStatus, dispatchStatus);
                                        stageMap.put(CourierName, courierName);
                                        stageMap.put(AirwayBillNo, airwayBillNo);
                                        stageMap.put(KYC_1, kyc1);
                                        stageMap.put(KYC_2, kyc2);
                                        stageMap.put(KYC_3, kyc3);
                                        stageMap.put(KYC_4, kyc4);
                                        stageMap.put(KYC_5, kyc5);

                                        mfAllDetailmap.put(productType, stageMap);

                                        /*if (!isCVLVerified.equals("")) {
                                            if (isCVLVerified.equals("1")) {
                                                setIsCVLVerified(true);
                                            } else {
                                                setIsCVLVerified(false);
                                            }
                                        }
                                        if (!isAadharVerified.equals("")) {
                                            if (isAadharVerified.equals("1")) {
                                                setIsAadharVerified(true);
                                            } else {
                                                setIsAadharVerified(false);
                                            }
                                        }
                                        if (getIsCVLVerified() && !getIsAadharVerified()) {
                                            tvMFApplicationStatus.setVisibility(View.VISIBLE);
                                            tvKeyProofUploaded.setVisibility(View.GONE);
                                            tvDispacthStatusHeader.setVisibility(View.GONE);

                                            tvMFProofVerificationStatus.setText(status);
                                            tvMFBankProofStatus.setText(bkmfStatus);
                                            tvMFClientCode.setText(clientCode);
                                            tvMFAppSubmittedDate.setText(appSubmitDate);
                                        } else if (!getIsCVLVerified() && !getIsAadharVerified()) {
                                            tvKeyProofUploaded.setVisibility(View.VISIBLE);
                                            tvDispacthStatusHeader.setVisibility(View.VISIBLE);
                                            tvMFApplicationStatus.setVisibility(View.VISIBLE);

                                            tvKyc1.setText(kyc1);
                                            tvKyc2.setText(kyc2);
                                            tvKyc3.setText(kyc3);
                                            tvKyc4.setText(kyc4);
                                            tvKyc5.setText(kyc5);
                                            tvMFBankProof.setText(bkmfStatus);
                                            tvMFCorresAddressProof.setText(corresAddProof);
                                            tvMFPermAddressProof.setText(permAddProof);
                                            tvPANCopy.setText(panStatus);

                                            tvDispatchDate.setText(dispatchDate);
                                            tvCourierName.setText(courierName);
                                            tvAirwayBillNo.setText(airwayBillNo);
                                            tvDispatchStatus.setText(dispatchStatus);

                                            tvMFProofVerificationStatus.setText(status);
                                            tvMFBankProofStatus.setText(bkmfStatus);
                                            tvMFClientCode.setText(clientCode);
                                            tvMFAppSubmittedDate.setText(appSubmitDate);

                                        } else if (getIsCVLVerified() && getIsAadharVerified()) {
                                            tvKeyProofUploaded.setVisibility(View.GONE);
                                            tvDispacthStatusHeader.setVisibility(View.GONE);
                                            tvMFApplicationStatus.setVisibility(View.VISIBLE);

                                            tvMFProofVerificationStatus.setText(status);
                                            tvMFBankProofStatus.setText(bkmfStatus);
                                            tvMFClientCode.setText(clientCode);
                                            tvMFAppSubmittedDate.setText(appSubmitDate);
                                        } else if (!getIsCVLVerified() && getIsAadharVerified()) {
                                            tvKeyProofUploaded.setVisibility(View.VISIBLE);
                                            tvDispacthStatusHeader.setVisibility(View.GONE);
                                            tvMFApplicationStatus.setVisibility(View.VISIBLE);

                                            tvKyc1.setVisibility(View.GONE);
                                            tvKyc2.setVisibility(View.GONE);
                                            tvKyc3.setVisibility(View.GONE);
                                            tvKyc4.setVisibility(View.GONE);
                                            tvKyc5.setVisibility(View.GONE);
                                            tvMFBankProof.setText(bkmfStatus);
                                            tvMFCorresAddressProof.setText(corresAddProof);
                                            tvMFPermAddressProof.setText(permAddProof);
                                            tvPANCopy.setText(panStatus);
                                        }*/


                                    }

                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        customProgress.setVisibility(View.GONE);
                    } else {
                        MOSLCommon.showToastMsg(TrackApplicationActivity.this, NO_RESPONSE);
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
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("Failure", "no resp in Data");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });
        } else {
            MOSLCommon.alertDInternet(this);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void setTrackDetail(String productValueStr, Map map) {
        //Fund transaction details
        tvTransactionReffNo.setText("");
        tvBankTransID.setText("");
        tvFundTransferThrough.setText("");
        tvFundTransferDate.setText("");
        tvBankName.setText("");
        tvAmountTransferred.setText("");
        //Proof details
        tvPanStatus.setText("");
        tvBankEqStatus.setText("");
        tvBankCommStatus.setText("");
        tvIncomeProofStatus.setText("");
        //Application details
        tvFormStatus.setText("");
        tvClientCode.setText("");
        tvDPID.setText("");
        tvApplicationSubmittedDate.setText("");
        if (map == null) {
            return;
        }
        if (productValueStr.equalsIgnoreCase(CURRENCY) || productValueStr.equalsIgnoreCase(Derivative)) {
            productValueStr = EQUITY;
        }
        Set<String> segmentSet = map.keySet();
        for (String segment : segmentSet) {
            Map<String, String> productMap = (HashMap) map.get(segment);

            if (segment.equalsIgnoreCase(productValueStr)) {
                if (segment.equalsIgnoreCase(MF)) {
                    if (!productMap.get(IS_CVL_VERIFIED).equals("")) {
                        if (productMap.get(IS_CVL_VERIFIED).equals("1")) {
                            setIsCVLVerified(true);
                        } else {
                            setIsCVLVerified(false);
                        }
                    }
                    if (!productMap.get(IS_AADHAAR_VERIFIED).equals("")) {
                        if (productMap.get(IS_AADHAAR_VERIFIED).equals("1")) {
                            setIsAadharVerified(true);
                        } else {
                            setIsAadharVerified(false);
                        }
                    }
                    tvFundTransfer.setVisibility(View.GONE);
                    llFundTransfer.setVisibility(View.GONE);
                    tvApplicationSubmitted.setVisibility(View.GONE);
                    llApplicationSubmitted.setVisibility(View.GONE);
                    tvProofVerification.setVisibility(View.GONE);
                    llProofVerification.setVisibility(View.GONE);

                    if (getIsCVLVerified() && !getIsAadharVerified()) {
                        tvMFApplicationStatus.setVisibility(View.VISIBLE);
                        tvKeyProofUploaded.setVisibility(View.GONE);
                        tvDispacthStatusHeader.setVisibility(View.GONE);
                        llDispacthStatus.setVisibility(View.GONE);

                        tvMFProofVerificationStatus.setText(productMap.get(STATUS));
//                        tvMFBankProofStatus.setText(productMap.get(BANK_STATUS));
                        tvMFClientCode.setText(productMap.get(CLIENT_CODE));
                        tvMFAppSubmittedDate.setText(productMap.get(APP_SUBMIT_DATE));
                    } else if (!getIsCVLVerified() && !getIsAadharVerified()) {
                        tvKeyProofUploaded.setVisibility(View.VISIBLE);
                        tvDispacthStatusHeader.setVisibility(View.VISIBLE);
                        tvMFApplicationStatus.setVisibility(View.VISIBLE);

                        tvKyc1.setText(productMap.get(KYC_1));
                        tvKyc2.setText(productMap.get(KYC_2));
                        tvKyc3.setText(productMap.get(KYC_3));
                        tvKyc4.setText(productMap.get(KYC_4));
                        tvKyc5.setText(productMap.get(KYC_5));
                        tvMFBankProof.setText(productMap.get(BANK_STATUS));
                        tvMFCorresAddressProof.setText(productMap.get(CORRESS_ADD_PROOF));
                        tvMFPermAddressProof.setText(productMap.get(PERM_ADD_PROOF));
                        tvPANCopy.setText(productMap.get(PANSTATUS));

                        tvDispatchDate.setText(productMap.get(DispatchDate));
                        tvCourierName.setText(productMap.get(CourierName));
                        tvAirwayBillNo.setText(productMap.get(AirwayBillNo));
                        tvDispatchStatus.setText(productMap.get(DispatchStatus));

                        tvMFProofVerificationStatus.setText(productMap.get(STATUS));
//                        tvMFBankProofStatus.setText(productMap.get(BANK_STATUS));
                        tvMFClientCode.setText(productMap.get(CLIENT_CODE));
                        tvMFAppSubmittedDate.setText(productMap.get(APP_SUBMIT_DATE));

                    } else if (getIsCVLVerified() && getIsAadharVerified()) {
                        tvKeyProofUploaded.setVisibility(View.GONE);
                        tvDispacthStatusHeader.setVisibility(View.GONE);
                        llDispacthStatus.setVisibility(View.GONE);
                        tvMFApplicationStatus.setVisibility(View.VISIBLE);

                        tvMFProofVerificationStatus.setText(productMap.get(STATUS));
//                        tvMFBankProofStatus.setText(BANK_STATUS);
                        tvMFClientCode.setText(productMap.get(CLIENT_CODE));
                        tvMFAppSubmittedDate.setText(productMap.get(APP_SUBMIT_DATE));
                    } else if (!getIsCVLVerified() && getIsAadharVerified()) {
                        tvKeyProofUploaded.setVisibility(View.VISIBLE);
                        tvDispacthStatusHeader.setVisibility(View.GONE);
                        llDispacthStatus.setVisibility(View.GONE);
                        tvMFApplicationStatus.setVisibility(View.VISIBLE);

                        llKyc1.setVisibility(View.GONE);
                        llKyc2.setVisibility(View.GONE);
                        llKyc3.setVisibility(View.GONE);
                        llKyc4.setVisibility(View.GONE);
                        llKyc5.setVisibility(View.GONE);
                        tvMFBankProof.setText(productMap.get(BANK_STATUS));
                        tvMFCorresAddressProof.setText(productMap.get(CORRESS_ADD_PROOF));
                        tvMFPermAddressProof.setText(productMap.get(PERM_ADD_PROOF));
                        tvPANCopy.setText(productMap.get(PANSTATUS));

                        tvMFProofVerificationStatus.setText(productMap.get(STATUS));
//                        tvMFBankProofStatus.setText(productMap.get(BANK_STATUS));
                        tvMFClientCode.setText(productMap.get(CLIENT_CODE));
                        tvMFAppSubmittedDate.setText(productMap.get(APP_SUBMIT_DATE));
                    }
                } else {
                    //Fund transaction details
                    tvFundTransfer.setVisibility(View.VISIBLE);
//                    llFundTransfer.setVisibility(View.VISIBLE);
                    tvApplicationSubmitted.setVisibility(View.VISIBLE);
//                    llApplicationSubmitted.setVisibility(View.VISIBLE);
                    tvTransactionReffNo.setText(productMap.get(TransRefNo));
                    tvBankTransID.setText(productMap.get(BankTransID));
                    tvFundTransferThrough.setText(productMap.get(FundTransferThrough));
                    tvFundTransferDate.setText(productMap.get(FundTransferredDate));
                    tvBankName.setText(productMap.get(BankName));
                    tvAmountTransferred.setText(productMap.get(FundTransferredAmount));
                    //Proof details
                    tvPanStatus.setText(productMap.get(PANSTATUS));
                    tvBankEqStatus.setText(productMap.get(BKEQSTATUS));
                    tvBankCommStatus.setText(productMap.get(BKCOMMSTATUS));
                    tvIncomeProofStatus.setText(productMap.get(INCOMEPROOFSTATUS));
                    //Application details
                    tvFormStatus.setText(productMap.get(Status));
                    tvClientCode.setText(productMap.get(ClientCode));
                    tvDPID.setText(productMap.get(DPID));
                    tvApplicationSubmittedDate.setText(productMap.get(ApplicationSubmitDate));
                }
            }
        }
    }
}

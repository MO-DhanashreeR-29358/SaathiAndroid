package com.mosl.mobile.ekyc.bankdetail;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.CustomTabsHelper;
import com.mosl.mobile.ekyc.common.CustomViewPager;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.directmodule.DirectResumeApplicationActivity;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.CustomDialog;
import com.mosl.mobile.ekyc.ui.WebActivity;
import com.viewpagerindicator.TabPageIndicator;
import cz.msebera.android.httpclient.Header;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.mosl.mobile.ekyc.common.EkycAppUtil.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.disableField;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getBankTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCommoditySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsEquitySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getUploadTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquityFundTransfered;
import static com.mosl.mobile.ekyc.ui.EkycMainActivity.customProgress;

public class FTUpiFragment extends Fragment implements MOSLConstants {

    static Context mContext;
    static int mEquStatus, mCommStatus;
    TabPageIndicator tabPageIndicator;
    View upiView;
    CustomViewPager viewPager;
    ScrollView scrollView;
    Button btnPayment, btnSkipPayment, btnNext;
    LinearLayout llFundOption, llPaymentButton;
    RelativeLayout rlSkipPayment;
    TextView etFundTransferredError, tvFundTransfered;
    TextInputLayout input_layout_FundTransferred, input_layout_VpaNo;
    EditText etFundTransferred, etBankName, etAccNo, etVpaNo;
    Boolean equMakePaymentButton = false, commMakePaymentButton = false;
    String userID, eqPaymentMode, commPaymentMode;
    int eqPaymentId, commPaymentId, hideProgressCounter = 0;
    String strEquBankName = "", strEquAccNo = "";
    private CustomTabsClient mClient;
    private boolean onlineEquFlag, onlineCommFlag;

    public static FTUpiFragment newInstance(Context context, int equityStatus,
        int commodityStatus) {

        FTUpiFragment fragment = new FTUpiFragment();
        Bundle args = new Bundle();
        mContext = context;
        mEquStatus = equityStatus;
        mCommStatus = commodityStatus;
        fragment.setArguments(args);
        return fragment;
    }

    public void setTabPageIndicator(TabPageIndicator tabPageIndicator) {
        this.tabPageIndicator = tabPageIndicator;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        try {
            upiView = inflater.inflate(R.layout.frag_upi, container, false);
            setUpView(upiView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return upiView;
    }

    @Override public void onResume() {
        super.onResume();
        if (!getInterestedInMF()
            && viewPager != null
            && viewPager.getCurrentItem() == getBankTab()) {
            getPaymentStatus();
        }
    }

    private void setUpView(View netBankView) {
        userID = getUserID();
        try {
            CustomTabsServiceConnection mConnection = new CustomTabsServiceConnection() {
                @Override public void onCustomTabsServiceConnected(ComponentName componentName,
                    CustomTabsClient customTabsClient) {
                    mClient = customTabsClient;
                }

                @Override public void onServiceDisconnected(ComponentName componentName) {
                    mClient = null;
                }
            };
            String packag = CustomTabsHelper.getPackageNameToUse(getContext());
            CustomTabsClient.bindCustomTabsService(mContext, packag, mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        scrollView = netBankView.findViewById(R.id.scrollView);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        viewPager = getActivity().findViewById(R.id.vpApply);
        llFundOption = netBankView.findViewById(R.id.llFundOption);
        llPaymentButton = netBankView.findViewById(R.id.llPaymentButton);
        rlSkipPayment = netBankView.findViewById(R.id.rlSkipPayment);

        tvFundTransfered = netBankView.findViewById(R.id.tvFundTransfered);
        input_layout_FundTransferred =
            netBankView.findViewById(R.id.input_layout_FundTransferred);
        input_layout_VpaNo = netBankView.findViewById(R.id.input_layout_VpaNo);

        etFundTransferred = netBankView.findViewById(R.id.etFundTransferred);
        etFundTransferredError = netBankView.findViewById(R.id.etFundTransferredError);
        etBankName = netBankView.findViewById(R.id.etBankName);
        etAccNo = netBankView.findViewById(R.id.etAccNo);
        etVpaNo = netBankView.findViewById(R.id.etVpaNo);

        etBankName.setEnabled(false);
        etAccNo.setEnabled(false);

        btnPayment = netBankView.findViewById(R.id.btnPayment);
        btnSkipPayment = netBankView.findViewById(R.id.btnSkipPayment);
        btnNext = netBankView.findViewById(R.id.btnNext);

        if (getAppType().equals(PP)) {
            rlSkipPayment.setVisibility(View.VISIBLE);
            etFundTransferred.setEnabled(true);
        } else {
            rlSkipPayment.setVisibility(View.GONE);
            etFundTransferred.setEnabled(false);
        }

        etFundTransferred.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etFundTransferred.getText().toString())) {
                    input_layout_FundTransferred.setErrorEnabled(false);
                    if (!getAppType().equalsIgnoreCase(PP)
                        && !getAppType().equalsIgnoreCase(DIRECT)
                        && etFundTransferred.length() < 5) {
                        etFundTransferredError.setVisibility(View.VISIBLE);
                        etFundTransferredError.setText(
                            getResources().getString(R.string.fund_transfer_error_msg));
                        etFundTransferred.requestFocus();
                    } else if (getAppType().equalsIgnoreCase(DIRECT)
                        && etFundTransferred.length() < 3) {
                        etFundTransferredError.setVisibility(View.VISIBLE);
                        etFundTransferredError.setText(
                            getResources().getString(R.string.fund_transfer_error_msg_3digits));
                        etFundTransferred.requestFocus();
                    } else if (getAppType().equalsIgnoreCase(PP) && !getAppType().equalsIgnoreCase(
                        DIRECT) && !(Integer.parseInt(etFundTransferred.getText().toString())
                        > 0)) {
                        etFundTransferredError.setVisibility(View.VISIBLE);
                        etFundTransferredError.setText(
                            getResources().getString(R.string.fund_transfer_error_msg_PP));
                        etFundTransferred.requestFocus();
                    } else {
                        etFundTransferredError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_FundTransferred.setErrorEnabled(false);
                    etFundTransferredError.setVisibility(View.GONE);
                }
            }

            @Override public void afterTextChanged(Editable s) {

            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (onlineEquFlag) {
                    makePayment("Equity");
                    equMakePaymentButton = true;
                } else if (onlineCommFlag) {
                    makePayment("Commodity");
                    commMakePaymentButton = true;
                } else {
                    commonMsgDialog(getActivity(), OFFLINE_BANK_FUND_TRANSFER, false, "");
                }
            }
        });

        btnSkipPayment.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (isEquityOriginal() && isCommodityOriginal()) {
                    skipEquPayment();
                    equMakePaymentButton = true;
                    skipCommPayment();
                    commMakePaymentButton = true;
                } else if (isEquityOriginal()) {
                    skipEquPayment();
                    equMakePaymentButton = true;
                } else if (isCommodityOriginal()) {
                    skipCommPayment();
                    commMakePaymentButton = true;
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                tabPageIndicator.enableTab(getUploadTab());
                viewPager.setCurrentItem(getUploadTab(), false);
            }
        });

        if (mEquStatus == 0) {
            showEquitypending();
        } else {
            equityPaid();
        }
        if (mCommStatus == 0) {
            showCommPending();
        } else {
            commodityPaid();
        }
    }

    public void getPaymentStatus() {
        if (viewPager == null || viewPager.getCurrentItem() != getBankTab()) {
            return;
        }

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("_userId", userID);

            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, GETPAYMENTSTATUS, FUNDTRANSFER, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {
                            try {
                                if (response.has("FUNDTRANSFER")) {
                                    JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");
                                    BigDecimal bdEqPaymentStatus, bdCommPaymentStatus;
                                    String eqPaymentStatus, commPaymentStatus;
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            if (object.has("EQPAYMENTSTATUS") && object.has(
                                                "COMMPAYMENTSTATUS")) {
                                                eqPaymentStatus = (object.isNull("EQPAYMENTSTATUS")
                                                    || object.getString("EQPAYMENTSTATUS")
                                                    .equals("")) ? ""
                                                    : object.getString("EQPAYMENTSTATUS");
                                                commPaymentStatus =
                                                    (object.isNull("COMMPAYMENTSTATUS")
                                                        || object.getString("COMMPAYMENTSTATUS")
                                                        .equals("")) ? ""
                                                        : object.getString("COMMPAYMENTSTATUS");
                                                if (!eqPaymentStatus.equals("")
                                                    && !commPaymentStatus.equals("")) {
                                                    bdEqPaymentStatus =
                                                        new BigDecimal(eqPaymentStatus);
                                                    bdCommPaymentStatus =
                                                        new BigDecimal(commPaymentStatus);
                                                    // 4 set for no segment selected
                                                    getUserpaymentDetail(
                                                        (int) bdEqPaymentStatus.longValue(),
                                                        (int) bdCommPaymentStatus.longValue());
                                                } else {
                                                    MOSLCommon.showToastMsg(mContext,
                                                        "Status is null");
                                                }
                                            } else if (object.has("EQPAYMENTSTATUS")) {
                                                eqPaymentStatus = (object.isNull("EQPAYMENTSTATUS")
                                                    || object.getString("EQPAYMENTSTATUS")
                                                    .equals("")) ? ""
                                                    : object.getString("EQPAYMENTSTATUS");
                                                if (!eqPaymentStatus.equals("")) {
                                                    bdEqPaymentStatus =
                                                        new BigDecimal(eqPaymentStatus);
                                                    // 4 set for no segment selected
                                                    getUserpaymentDetail(
                                                        (int) bdEqPaymentStatus.longValue(), 4);
                                                }
                                            } else if (object.has("COMMPAYMENTSTATUS")) {
                                                commPaymentStatus =
                                                    (object.isNull("COMMPAYMENTSTATUS")
                                                        || object.getString("COMMPAYMENTSTATUS")
                                                        .equals("")) ? ""
                                                        : object.getString("COMMPAYMENTSTATUS");
                                                if (!commPaymentStatus.equals("")) {
                                                    bdCommPaymentStatus =
                                                        new BigDecimal(commPaymentStatus);
                                                    // 4 set for no segment selected
                                                    getUserpaymentDetail(4,
                                                        (int) bdCommPaymentStatus.longValue());
                                                }
                                            }

                                            hideProgress();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY,
                                    false, "");
                            }
                        } else {
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                            //                        MOSLCommon.showToastMsg(mContext, NO_RESPONSE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }
                });
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    public void getUserpaymentDetail(final int eqPaymentStatus, final int commPaymentStatus) {
        if (viewPager == null || viewPager.getCurrentItem() != getBankTab()) {
            return;
        }

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("_userId", userID);

            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, GETUSERPAYMENTDETAILS, FUNDTRANSFER, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {
                            try {
                                if (response.has("FUNDTRANSFER")) {
                                    JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");
                                    if (jsonArray.length() != 0) {
                                        String upiStatus = "", upiMessage = "";
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            String bankName =
                                                (object.isNull("BANK_NAME") || object.getString(
                                                    "BANK_NAME").equals("")) ? ""
                                                    : object.getString("BANK_NAME");
                                            String bankAccNo =
                                                (object.isNull("BANK_ACC_NO") || object.getString(
                                                    "BANK_ACC_NO").equals("")) ? ""
                                                    : object.getString("BANK_ACC_NO");
                                            String marginDeposit = (object.isNull("MARGIN_DEPOSIT")
                                                || object.getString("MARGIN_DEPOSIT").equals(""))
                                                ? "" : object.getString("MARGIN_DEPOSIT");
                                            String product =
                                                (object.isNull("PRODUCT") || object.getString(
                                                    "PRODUCT").equals("")) ? ""
                                                    : object.getString("PRODUCT");
                                            String bankflag =
                                                (object.isNull("BANKFLAG") || object.getString(
                                                    "BANKFLAG").equals("")) ? ""
                                                    : object.getString("BANKFLAG");
                                            String amtRequested = (object.isNull("AMOUNTREQUESTED")
                                                || object.getString("AMOUNTREQUESTED").equals(""))
                                                ? "" : object.getString("AMOUNTREQUESTED");
                                            String fundTransferThrough =
                                                (object.isNull("FUNDTRANSFERTHROUGH")
                                                    || object.getString("FUNDTRANSFERTHROUGH")
                                                    .equals("")) ? ""
                                                    : object.getString("FUNDTRANSFERTHROUGH");
                                            String fundTransferThroughId =
                                                (object.isNull("FUNDTRANSFERTHROUGHID")
                                                    || object.getString("FUNDTRANSFERTHROUGHID")
                                                    .equals("")) ? ""
                                                    : object.getString("FUNDTRANSFERTHROUGHID");
                                            upiStatus =
                                                (object.isNull("UPI_STATUS")
                                                    || object.getString("UPI_STATUS")
                                                    .equals("")) ? ""
                                                    : object.getString("UPI_STATUS");
                                            upiMessage =
                                                (object.isNull("UPI_MESSAGE")
                                                    || object.getString("UPI_MESSAGE")
                                                    .equals("")) ? ""
                                                    : object.getString("UPI_MESSAGE");
                                            /*
                                             *  fundTransferThroughId 1 fundTransferThrough Net Banking
                                             *  fundTransferThroughId 2 fundTransferThrough Debit Card
                                             *  fundTransferThroughId 3 fundTransferThrough Cheque
                                             *  fundTransferThroughId 4 fundTransferThrough Skipped Payment
                                             *  fundTransferThroughId 5 fundTransferThrough UPI Payment
                                             *  fundTransferThroughId 0 fundTransferThrough Payment Pending
                                             */
                                            if (product.equalsIgnoreCase(EQUITY)) {
                                                eqPaymentMode = fundTransferThrough;
                                                eqPaymentId =
                                                    Integer.parseInt(fundTransferThroughId);
                                                etBankName.setText(bankName);
                                                etAccNo.setText(bankAccNo);
                                                if (getAppType().equals(PP)) {
                                                    if (amtRequested.equalsIgnoreCase("0")) {
                                                        etFundTransferred.setText("");
                                                    } else {
                                                        etFundTransferred.setText(amtRequested);
                                                    }
                                                } else {
                                                    etFundTransferred.setText(marginDeposit);
                                                }
                                                if (bankflag.equalsIgnoreCase("1")) {
                                                    onlineEquFlag = true;
                                                } else if (bankflag.equalsIgnoreCase("0")) {
                                                    onlineEquFlag = false;
                                                    //                                                        btnEquPayment.setEnabled(false);
                                                    btnPayment.setBackgroundResource(
                                                        R.drawable.ic_disable_submit_btn);
                                                    etFundTransferred.setEnabled(false);
                                                }
                                            } else if (product.equalsIgnoreCase(COMMODITY)) {
                                                commPaymentMode = fundTransferThrough;
                                                commPaymentId =
                                                    Integer.parseInt(fundTransferThroughId);
                                                etBankName.setText(bankName);
                                                etAccNo.setText(bankAccNo);
                                                if (getAppType().equals(PP)) {
                                                    if (amtRequested.equalsIgnoreCase("0")) {
                                                        etFundTransferred.setText("");
                                                    } else {
                                                        etFundTransferred.setText(amtRequested);
                                                    }
                                                } else {
                                                    etFundTransferred.setText(marginDeposit);
                                                }
                                                if (bankflag.equalsIgnoreCase("1")) {
                                                    onlineCommFlag = true;
                                                } else if (bankflag.equalsIgnoreCase("0")) {
                                                    onlineCommFlag = false;
                                                    btnPayment.setBackgroundResource(
                                                        R.drawable.ic_disable_submit_btn);
                                                    etFundTransferred.setEnabled(false);
                                                }
                                            }
                                            if (Integer.parseInt(upiStatus) == 1) {
                                                showView(eqPaymentStatus, commPaymentStatus);
                                            } else {
                                                llFundOption.setVisibility(View.GONE);
                                                llPaymentButton.setVisibility(View.GONE);
                                                tvFundTransfered.setVisibility(View.VISIBLE);
                                                tvFundTransfered.setText(upiMessage);
                                            }
                                        }
                                        hideProgress();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY,
                                    false, "");
                            }
                        } else {
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }
                }
            );
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void makePayment(String productType) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            String strEquFundAmount = etFundTransferred.getText().toString();
            if (!validateEditText(etVpaNo, input_layout_VpaNo,
                "Enter Virtual Payment Address")) {
                etVpaNo.requestFocus();
            } else if (etVpaNo.length() < 3) {
                input_layout_VpaNo.setError("Enter Valid Virtual Payment Address");
            } else if (!validateEditText(etFundTransferred, input_layout_FundTransferred,
                "Enter Amount")) {
                input_layout_VpaNo.setErrorEnabled(false);
            } else if (!(Integer.parseInt(strEquFundAmount) > 0) && !getAppType().equalsIgnoreCase(
                DIRECT)) {
                etFundTransferred.requestFocus();
                //  validateEditText(etEquFundTransferred, input_layout_equFundTransferred,);
            } else if (!getAppType().equalsIgnoreCase(PP)
                && !getAppType().equalsIgnoreCase(DIRECT)
                && etFundTransferred.length() < 5) {
                etFundTransferredError.setText(
                    getResources().getString(R.string.fund_transfer_error_msg));
                etFundTransferredError.setVisibility(View.VISIBLE);
                etFundTransferred.requestFocus();
            } else if (getAppType().equalsIgnoreCase(DIRECT) && etFundTransferred.length() < 3) {
                etFundTransferredError.setVisibility(View.VISIBLE);
                etFundTransferredError.setText(
                    getResources().getString(R.string.fund_transfer_error_msg_3digits));
                etFundTransferred.requestFocus();
            } else {
                Map<String, String> inputMap = new HashMap<>();
                inputMap.put("_userId", userID);
                inputMap.put("_bankName", strEquBankName);
                inputMap.put("_accountNo", strEquAccNo);
                inputMap.put("_initialMargin", strEquFundAmount);
                inputMap.put("_addtionalMargin", "0");
                inputMap.put("_totalAmount", strEquFundAmount);
                inputMap.put("_segmentType", productType);
                inputMap.put("_VPA", etVpaNo.getText().toString());
                //inputMap.put("_VPA", "abc@okhdfcbank");
                //inputMap.put("_VPA", "vishnuthiyyattil@okhdfcbank");

                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, UPDATEUPIPAYMENT, FUNDTRANSFER, inputMap,
                    new JsonHttpResponseHandler() {
                        @Override public void onSuccess(int statusCode, Header[] headers,
                            JSONObject response) {
                            if (response != null && response.length() != 0) {

                                try {
                                    if (response.has("FUNDTRANSFER")) {
                                        JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");
                                        if (jsonArray.length() != 0) {

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);

                                                String status =
                                                    (object.isNull("STATUS") || object.getString(
                                                        "STATUS").equals("")) ? ""
                                                        : object.getString("STATUS");
                                                if (Integer.valueOf(status) == 1) {
                                                    getPaymentStatus();
                                                } else {
                                                    informationDialog(COULD_NOT_SKIP_PAY);
                                                }
                                            }
                                        }
                                    }
                                    if (response.has("URLTable")) {
                                        JSONArray jsonArray = response.getJSONArray("URLTable");
                                        if (jsonArray.length() != 0) {

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);

                                                String urlPath =
                                                    (object.isNull("URLPATH") || object.getString(
                                                        "URLPATH").equals("")) ? ""
                                                        : object.getString("URLPATH");
                                                openWebUrl(Uri.parse(urlPath));
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY,
                                        false, "");
                                }
                            } else {
                                commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false,
                                    "");
                                customProgress.setVisibility(View.GONE);
                            }
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override public void onSuccess(int statusCode, Header[] headers,
                            JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override public void onSuccess(int statusCode, Header[] headers,
                            String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override public void onFailure(int statusCode, Header[] headers,
                            String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                    });
            }
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void skipEquPayment() {

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("_userId", userID);
            inputMap.put("_segmentType", "Equity");

            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, UPDATESKIPPAYMENT, FUNDTRANSFER, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {

                            try {
                                JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");
                                if (jsonArray.length() != 0) {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String status =
                                            (object.isNull("STATUS") || object.getString("STATUS")
                                                .equals("")) ? "" : object.getString("STATUS");
                                        if (Integer.valueOf(status) == 1) {
                                            //                                        EkycMainActivity.vpApply.setCurrentItem(2);
                                            getPaymentStatus();
                                        } else {
                                            informationDialog(COULD_NOT_SKIP_PAY);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY,
                                    false, "");
                            }
                        } else {
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                            //                        MOSLCommon.showToastMsg(mContext, NO_RESPONSE);
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }
                });
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void skipCommPayment() {

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("_userId", userID);
            inputMap.put("_segmentType", "Commodity");

            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, UPDATESKIPPAYMENT, FUNDTRANSFER, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {

                            try {
                                JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");
                                if (jsonArray.length() != 0) {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String status =
                                            (object.isNull("STATUS") || object.getString("STATUS")
                                                .equals("")) ? "" : object.getString("STATUS");
                                        if (Integer.valueOf(status) == 1) {
                                            //                                        EkycMainActivity.vpApply.setCurrentItem(2);
                                            getPaymentStatus();
                                        } else {
                                            informationDialog(COULD_NOT_SKIP_PAY);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY,
                                    false, "");
                            }
                        } else {
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                            //                        MOSLCommon.showToastMsg(mContext, NO_RESPONSE);
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }
                });
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void showView(int mEquStatus, int mCommStatus) {
        if (getIsEquitySelected() && getIsCommoditySelected()) {
            if (mEquStatus != 0 && mCommStatus != 0) {
                //both segment selected and both are paid
                //goto upload
                equityPaid();
                commodityPaid();
                if (getAppType().equals(DIRECT)) {
                    if (getIsAadharVerified()) {
                        if (equMakePaymentButton || commMakePaymentButton) {
                            tabPageIndicator.enableTab(getUploadTab());
                            viewPager.setCurrentItem(getUploadTab(), false);
                        }
                    } else {
                        CustomDialog cd =
                            new CustomDialog(mContext, Message_Fund_Transfer_For_No_Aadhaar, true,
                                false, false, "", "OK", "",
                                new CustomDialog.CustomDialogListener() {
                                    @Override public void dialogAction(MOSLCommon.Action action,
                                        Object... data) {
                                        Intent intent = new Intent(mContext,
                                            DirectResumeApplicationActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                        cd.show();
                    }
                } else {
                    if (equMakePaymentButton || commMakePaymentButton) {
                        tabPageIndicator.enableTab(getUploadTab());
                        viewPager.setCurrentItem(getUploadTab(), false);
                    }
                }
            }
        }
        if (getIsEquitySelected()) {
            if (mEquStatus == 0) {
                showEquitypending();
            } else { //paid
                if (getIsCommoditySelected()) { //if commodity is also selected
                    equityPaid();
                } else {
                    equityPaid();
                    if (getAppType().equals(DIRECT)) {
                        if (getIsAadharVerified()) {
                            if (equMakePaymentButton) {
                                tabPageIndicator.enableTab(getUploadTab());
                                viewPager.setCurrentItem(getUploadTab(), false);
                            }
                        } else {
                            CustomDialog cd =
                                new CustomDialog(mContext, Message_Fund_Transfer_For_No_Aadhaar,
                                    true, false, false, "", "OK", "",
                                    new CustomDialog.CustomDialogListener() {
                                        @Override public void dialogAction(MOSLCommon.Action action,
                                            Object... data) {
                                            Intent intent = new Intent(mContext,
                                                DirectResumeApplicationActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    });
                            cd.show();
                        }
                    } else {
                        if (equMakePaymentButton) {
                            tabPageIndicator.enableTab(getUploadTab());
                            viewPager.setCurrentItem(getUploadTab(), false);
                        }
                    }
                }
            }
        }
        if (getIsCommoditySelected()) {
            if (mCommStatus == 0) {
                showCommPending();
            } else { //paid
                if (getIsEquitySelected()) { //if equity is also selected
                    commodityPaid();
                } else {
                    commodityPaid();
                    if (getAppType().equals(DIRECT)) {
                        if (getIsAadharVerified()) {
                            if (commMakePaymentButton) {
                                tabPageIndicator.enableTab(getUploadTab());
                                viewPager.setCurrentItem(getUploadTab(), false);
                            }
                        } else {
                            CustomDialog cd =
                                new CustomDialog(mContext, Message_Fund_Transfer_For_No_Aadhaar,
                                    true, false, false, "", "OK", "",
                                    new CustomDialog.CustomDialogListener() {
                                        @Override public void dialogAction(MOSLCommon.Action action,
                                            Object... data) {
                                            Intent intent = new Intent(mContext,
                                                DirectResumeApplicationActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    });
                            cd.show();
                        }
                    } else {
                        if (commMakePaymentButton) {
                            tabPageIndicator.enableTab(getUploadTab());
                            viewPager.setCurrentItem(getUploadTab(), false);
                        }
                    }
                }
            }
        }
    }

    private void showEquitypending() {
        llFundOption.setVisibility(View.VISIBLE);
        tvFundTransfered.setVisibility(View.GONE);
    }

    //private void noEquitySegment() {
    //    llEquityNetBanking.setVisibility(View.GONE);
    //}

    private void showCommPending() {
        llFundOption.setVisibility(View.VISIBLE);
        tvFundTransfered.setVisibility(View.GONE);
    }

    private void equityPaid() {
        setIsEquityFundTransfered(true);
        tvFundTransfered.setVisibility(View.VISIBLE);
        tvFundTransfered.setText("Fund Transferred - " + eqPaymentMode);
        /*
         *  fundTransferThroughId 1 fundTransferThrough Net Banking
         *  fundTransferThroughId 2 fundTransferThrough Debit Card
         *  fundTransferThroughId 3 fundTransferThrough Cheque
         *  fundTransferThroughId 4 fundTransferThrough Skipped Payment
         *  fundTransferThroughId 5 fundTransferThrough UPI Payment
         *  fundTransferThroughId 0 fundTransferThrough Payment Pending
         */
        if (eqPaymentId == 5) {
            disableField(llFundOption);
            llFundOption.setVisibility(View.GONE);
            llPaymentButton.setVisibility(View.GONE);
            if (getIsEquitySelected() && getIsCommoditySelected()) {
                if (isEquityFundTransfered() && isCommodityFundTransfered()) {
                    btnNext.setVisibility(View.VISIBLE);
                    btnNext.setEnabled(true);
                }
            } else if (getIsEquitySelected() && isEquityFundTransfered()) {
                btnNext.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
            }
        } else {
            llFundOption.setVisibility(View.GONE);
            llPaymentButton.setVisibility(View.GONE);
            if (getIsEquitySelected() && getIsCommoditySelected()) {
                if (isEquityFundTransfered() && isCommodityFundTransfered()) {
                    btnNext.setVisibility(View.VISIBLE);
                    btnNext.setEnabled(true);
                }
            } else if (getIsEquitySelected() && isEquityFundTransfered()) {
                btnNext.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
            }
        }
    }

    private void commodityPaid() {
        setIsCommodityFundTransfered(true);
        tvFundTransfered.setVisibility(View.VISIBLE);
        tvFundTransfered.setText("Fund Transferred - " + commPaymentMode);
        /*
         *  fundTransferThroughId 1 fundTransferThrough Net Banking
         *  fundTransferThroughId 2 fundTransferThrough Debit Card
         *  fundTransferThroughId 3 fundTransferThrough Cheque
         *  fundTransferThroughId 4 fundTransferThrough Skipped Payment
         *  fundTransferThroughId 5 fundTransferThrough UPI Payment
         *  fundTransferThroughId 0 fundTransferThrough Payment Pending
         */
        if (commPaymentId == 5) {
            disableField(llFundOption);
            llFundOption.setVisibility(View.GONE);
            llPaymentButton.setVisibility(View.GONE);
            if (getIsEquitySelected() && getIsCommoditySelected()) {
                if (isEquityFundTransfered() && isCommodityFundTransfered()) {
                    btnNext.setVisibility(View.VISIBLE);
                    btnNext.setEnabled(true);
                }
            } else if (getIsCommoditySelected() && isCommodityFundTransfered()) {
                btnNext.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
            }
        } else {
            llFundOption.setVisibility(View.GONE);
            llPaymentButton.setVisibility(View.GONE);
            if (getIsEquitySelected() && getIsCommoditySelected()) {
                if (isEquityFundTransfered() && isCommodityFundTransfered()) {
                    btnNext.setVisibility(View.VISIBLE);
                    btnNext.setEnabled(true);
                }
            } else if (getIsCommoditySelected() && isCommodityFundTransfered()) {
                btnNext.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
            }
        }
    }

    public void informationDialog(String msg) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_info);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);

        tvMsg.setText(msg);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                customProgress.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
                customProgress.setVisibility(View.GONE);
            }
        });

        dialog.show();
    }

    public void openWebUrl(Uri url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            String packag = CustomTabsHelper.getPackageNameToUse(getContext());
            if (packag == null) {
                //   openWebview(null, url);
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(URL_PATH, url.toString());
                startActivity(intent);
            } else {
                openCustomTabsIntent(url.toString());
            }
        } else {
            openInBrowser(url);
        }
    }

    private void openCustomTabsIntent(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(getResources().getColor(R.color.secondaryBackground));
        builder.setCloseButtonIcon(
            BitmapFactory.decodeResource(getResources(), R.drawable.ic_back_arrow));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
    }

    private CustomTabsSession getSession() {
        return mClient.newSession(new CustomTabsCallback() {
            @Override public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
                //   getPaymentStatus();
            }
        });
    }

    public void openInBrowser(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private boolean validateEditText(EditText edietText, TextInputLayout textInputLayout,
        String error) {
        if (edietText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(error);
            edietText.requestFocus();
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getPaymentStatus();
        }
    }

    /**
     * this method will hide progress when received the response of GETPAYMENTSTATUS &
     * GETUSERPAYMENTDETAILS
     */
    private void hideProgress() {
        hideProgressCounter++;
        if (hideProgressCounter == 2) {
            hideProgressCounter = 0;
            customProgress.setVisibility(View.GONE);
        }
    }
}

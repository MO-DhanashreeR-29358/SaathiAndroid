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

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.disableField;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getBankTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCommoditySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsDerivativeSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsEquitySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getUploadTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquityFundTransfered;
import static com.mosl.mobile.ekyc.ui.EkycMainActivity.customProgress;

/**
 * Created by Owner on 7/8/2016.
 */
public class FTNetBankingFragment extends Fragment implements MOSLConstants {

    View netBankView;
    static Context mContext;
    LinearLayout llEquityNetBanking, llCommodityNetBanking;
    EditText etEquFundTransferred, etCommFundTransferred, etEquBankName, etEquAccNo, etCommBankName,
        etCommAccNo;
    Button btnEquPayment, btnEquSkipPayment, btnCommPayment, btnCommSkipPayment, btnEquNext,
        btnCommNext;
    String userID;
    TextView etEquFundTransferredError, etCommFundTransferredError;
    String strEquBankName = "", strCommBankName = "", strEquAccNo = "", strCommAccNo = "",
        equMargin = "", equTotalAmount = "", commMargin = "", commTotalAmount = "";
    LinearLayout llEquFundOption, llCommFundOption, llEquPaymentButton, llCommPaymentButton;
    TextView tvEquFundTransfered, tvCommFundTransfered;
    static int mEquStatus, mCommStatus;
    CustomViewPager viewPager;
    TabPageIndicator tabPageIndicator;
    RelativeLayout rlEquSkipPayment, rlCommSkipPayment;
    TextInputLayout input_layout_equFundTransferred, input_layout_commFundTransferred;
    private CustomTabsClient mClient;
    private boolean onlineEquFlag;
    private boolean onlineCommFlag;
    Boolean equMakePaymentButton = false;
    Boolean commMakePaymentButton = false;
    ScrollView scrollView;
    int hideProgressCounter = 0;
    String eqPaymentMode, commPaymentMode;
    int eqPaymentId, commPaymentId;

    public void setTabPageIndicator(TabPageIndicator tabPageIndicator) {
        this.tabPageIndicator = tabPageIndicator;
    }

    public static FTNetBankingFragment newInstance(Context context, int equityStatus,
        int commodityStatus) {

        FTNetBankingFragment fragment = new FTNetBankingFragment();
        Bundle args = new Bundle();
        mContext = context;
        mEquStatus = equityStatus;
        mCommStatus = commodityStatus;
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        try {
            netBankView = inflater.inflate(R.layout.frag_netbanking, container, false);
            setUpView(netBankView);
        } catch (Exception e) {
            e.printStackTrace();
            //            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            //            Crashlytics.logException(e);
        }

        return netBankView;
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
        //        userID = "1451500";
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
            CustomTabsClient.bindCustomTabsService(getContext(), packag, mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*strEquBankName = CommonModel.equBankName;
        strCommBankName = CommonModel.commBankName;
        strEquAccNo = CommonModel.equAccNo;
        strCommAccNo = CommonModel.commAccNo;
        equMargin = CommonModel.equMargin;
        equTotalAmount = CommonModel.equTotalAmount;
        commMargin = CommonModel.commMargin;
        commTotalAmount = CommonModel.commTotalAmount;*/

        /*strEquBankName = "HDFC Bank ltd.";
        strCommBankName = "HDFC Bank ltd.";
        strEquAccNo = "4523";
        strCommAccNo = "2356";
        equMargin = "10000";
        equTotalAmount = "10000";
        commMargin = "10000";
        commTotalAmount = "10000";*/
        scrollView = (ScrollView) netBankView.findViewById(R.id.scrollView);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        viewPager = (CustomViewPager) getActivity().findViewById(R.id.vpApply);
        llEquityNetBanking = (LinearLayout) netBankView.findViewById(R.id.llEquityNetBanking);
        llCommodityNetBanking = (LinearLayout) netBankView.findViewById(R.id.llCommodityNetBanking);
        llEquFundOption = (LinearLayout) netBankView.findViewById(R.id.llEquFundOption);
        llEquPaymentButton = (LinearLayout) netBankView.findViewById(R.id.llEquPaymentButton);
        llCommFundOption = (LinearLayout) netBankView.findViewById(R.id.llCommFundOption);
        rlEquSkipPayment = (RelativeLayout) netBankView.findViewById(R.id.rlEquSkipPayment);
        rlCommSkipPayment = (RelativeLayout) netBankView.findViewById(R.id.rlCommSkipPayment);
        llCommPaymentButton = (LinearLayout) netBankView.findViewById(R.id.llCommPaymentButton);

        tvEquFundTransfered = (TextView) netBankView.findViewById(R.id.tvEquFundTransfered);
        tvCommFundTransfered = (TextView) netBankView.findViewById(R.id.tvCommFundTransfered);
        input_layout_equFundTransferred =
            (TextInputLayout) netBankView.findViewById(R.id.input_layout_equFundTransferred);
        input_layout_commFundTransferred =
            (TextInputLayout) netBankView.findViewById(R.id.input_layout_commFundTransferred);

        etEquFundTransferred = (EditText) netBankView.findViewById(R.id.etEquFundTransferred);
        etCommFundTransferred = (EditText) netBankView.findViewById(R.id.etCommFundTransferred);
        etEquFundTransferredError =
            (TextView) netBankView.findViewById(R.id.etEquFundTransferredError);
        etCommFundTransferredError =
            (TextView) netBankView.findViewById(R.id.etCommFundTransferredError);
        etEquBankName = (EditText) netBankView.findViewById(R.id.etEquBankName);
        etEquAccNo = (EditText) netBankView.findViewById(R.id.etEquAccNo);
        etCommBankName = (EditText) netBankView.findViewById(R.id.etCommBankName);
        etCommAccNo = (EditText) netBankView.findViewById(R.id.etCommAccNo);

        etEquBankName.setEnabled(false);
        etCommBankName.setEnabled(false);
        etEquAccNo.setEnabled(false);
        etCommAccNo.setEnabled(false);

        btnEquPayment = (Button) netBankView.findViewById(R.id.btnEquPayment);
        btnEquSkipPayment = (Button) netBankView.findViewById(R.id.btnEquSkipPayment);
        btnEquNext = (Button) netBankView.findViewById(R.id.btnEquNext);
        btnCommPayment = (Button) netBankView.findViewById(R.id.btnCommPayment);
        btnCommSkipPayment = (Button) netBankView.findViewById(R.id.btnCommSkipPayment);
        btnCommNext = (Button) netBankView.findViewById(R.id.btnCommNext);

        if (getIsEquitySelected() && getIsCommoditySelected()) {
            llEquityNetBanking.setVisibility(View.VISIBLE);
            //            llCommodityNetBanking.setVisibility(View.VISIBLE);
        } else if (getIsEquitySelected() || getIsDerivativeSelected()) {
            llEquityNetBanking.setVisibility(View.VISIBLE);
            llCommodityNetBanking.setVisibility(View.GONE);
        } else if (getIsCommoditySelected()) {
            llEquityNetBanking.setVisibility(View.GONE);
            llCommodityNetBanking.setVisibility(View.VISIBLE);
        }

        if (getAppType().equals(PP)) {
            rlEquSkipPayment.setVisibility(View.VISIBLE);
            rlCommSkipPayment.setVisibility(View.VISIBLE);
            etEquFundTransferred.setEnabled(true);
            etCommFundTransferred.setEnabled(true);
        } else {
            rlEquSkipPayment.setVisibility(View.GONE);
            rlCommSkipPayment.setVisibility(View.GONE);
            etEquFundTransferred.setEnabled(false);
            etCommFundTransferred.setEnabled(false);
        }
        btnEquPayment.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (onlineEquFlag) {
                    equMakePayment();
                    equMakePaymentButton = true;
                    /*if (isCommodityOriginal()) {
                        commMakePayment();
                        commMakePaymentButton = true;
                    }*/
                } else {
                    commonMsgDialog(getActivity(), OFFLINE_BANK_FUND_TRANSFER, false, "");
                }
            }
        });

        etEquFundTransferred.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etEquFundTransferred.getText().toString())) {
                    input_layout_equFundTransferred.setErrorEnabled(false);
                    if (!getAppType().equalsIgnoreCase(PP)
                        && !getAppType().equalsIgnoreCase(DIRECT)
                        && etEquFundTransferred.length() < 4) {
                        etEquFundTransferredError.setVisibility(View.VISIBLE);
                        etEquFundTransferredError.setText(
                            getResources().getString(R.string.fund_transfer_error_msg));
                        etEquFundTransferred.requestFocus();
                    } else if (getAppType().equalsIgnoreCase(DIRECT)
                        && etEquFundTransferred.length() < 3) {
                        etEquFundTransferredError.setVisibility(View.VISIBLE);
                        etEquFundTransferredError.setText(
                            getResources().getString(R.string.fund_transfer_error_msg_3digits));
                        etEquFundTransferred.requestFocus();
                    } else if (getAppType().equalsIgnoreCase(PP) && !getAppType().equalsIgnoreCase(
                        DIRECT) && !(Integer.parseInt(etEquFundTransferred.getText().toString())
                        > 0)) {
                        etEquFundTransferredError.setVisibility(View.VISIBLE);
                        etEquFundTransferredError.setText(
                            getResources().getString(R.string.fund_transfer_error_msg_PP));
                        etEquFundTransferred.requestFocus();
                    } else {
                        etEquFundTransferredError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_equFundTransferred.setErrorEnabled(false);
                    etEquFundTransferredError.setVisibility(View.GONE);
                }
            }

            @Override public void afterTextChanged(Editable s) {

            }
        });

        etCommFundTransferred.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etCommFundTransferred.getText().toString())) {
                    input_layout_commFundTransferred.setErrorEnabled(false);
                    if (!getAppType().equalsIgnoreCase(PP)
                        && !getAppType().equalsIgnoreCase(DIRECT)
                        && etCommFundTransferred.length() < 4) {
                        etCommFundTransferredError.setVisibility(View.VISIBLE);
                        etCommFundTransferredError.setText(
                            getResources().getString(R.string.fund_transfer_error_msg));
                        etCommFundTransferred.requestFocus();
                    } else if (getAppType().equalsIgnoreCase(DIRECT)
                        && etCommFundTransferred.length() < 3) {
                        etCommFundTransferredError.setVisibility(View.VISIBLE);
                        etCommFundTransferredError.setText(
                            getResources().getString(R.string.fund_transfer_error_msg_3digits));
                        etCommFundTransferred.requestFocus();
                    } else if (getAppType().equalsIgnoreCase(PP) && !getAppType().equalsIgnoreCase(
                        DIRECT) && !(Integer.parseInt(etCommFundTransferred.getText().toString())
                        > 0)) {
                        etCommFundTransferredError.setVisibility(View.VISIBLE);
                        etCommFundTransferredError.setText(
                            getResources().getString(R.string.fund_transfer_error_msg_PP));
                        etCommFundTransferred.requestFocus();
                    } else {
                        etCommFundTransferredError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_commFundTransferred.setErrorEnabled(false);
                    etCommFundTransferredError.setVisibility(View.GONE);
                }
            }

            @Override public void afterTextChanged(Editable s) {

            }
        });

        btnEquSkipPayment.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                skipEquPayment();
                equMakePaymentButton = true;
                if (isCommodityOriginal()) {
                    skipCommPayment();
                    commMakePaymentButton = true;
                }
            }
        });

        btnEquNext.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                tabPageIndicator.enableTab(getUploadTab());
                viewPager.setCurrentItem(getUploadTab(), false);
            }
        });

        btnCommPayment.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (onlineCommFlag) {
                    commMakePayment();
                    commMakePaymentButton = true;
                } else {
                    commonMsgDialog(getActivity(), OFFLINE_BANK_FUND_TRANSFER, false, "");
                }
            }
        });
        btnCommSkipPayment.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                skipCommPayment();
                commMakePaymentButton = true;
            }
        });

        btnCommNext.setOnClickListener(new View.OnClickListener() {
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

    private void skipCommPayment() {

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("_userId", userID);
            inputMap.put("_segmentType", "Commodity");

            String flag = UPDATESKIPPAYMENT;
            String category = FUNDTRANSFER;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
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

    private void commMakePayment() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            String strCommFundAmount = etCommFundTransferred.getText().toString();
            /*if (isEquityOriginal())
                strCommFundAmount = etEquFundTransferred.getText().toString();*/
            /*if (isEquityOriginal() && !validateEditText(etEquFundTransferred, input_layout_equFundTransferred, "Enter Amount")) {
                return;
            } else if (isEquityOriginal() && !(Integer.parseInt(strCommFundAmount) > 0)) {
                etEquFundTransferred.requestFocus();
                //  validateEditText(etEquFundTransferred, input_layout_equFundTransferred,);
            } else if (isEquityOriginal() && !getAppType().equalsIgnoreCase(PP) && etEquFundTransferred.length() < 4) {
                etEquFundTransferredError.setText(getResources().getString(R.string.fund_transfer_error_msg));
                etEquFundTransferredError.setVisibility(View.VISIBLE);
                etEquFundTransferred.requestFocus();
            } else*/
            if (!validateEditText(etCommFundTransferred, input_layout_commFundTransferred,
                "Enter Amount")) {
                return;
            } else if (!(Integer.parseInt(strCommFundAmount) > 0) && !getAppType().equalsIgnoreCase(
                DIRECT)) {
                etCommFundTransferred.requestFocus();
                //  validateEditText(etEquFundTransferred, input_layout_equFundTransferred,);
            } else if (!getAppType().equalsIgnoreCase(PP)
                && !getAppType().equalsIgnoreCase(DIRECT)
                && etCommFundTransferred.length() < 4) {
                etCommFundTransferredError.setText(
                    getResources().getString(R.string.fund_transfer_error_msg));
                etCommFundTransferredError.setVisibility(View.VISIBLE);
                etCommFundTransferred.requestFocus();
            } else if (getAppType().equalsIgnoreCase(DIRECT)
                && etCommFundTransferred.length() < 3) {
                etCommFundTransferredError.setVisibility(View.VISIBLE);
                etCommFundTransferredError.setText(
                    getResources().getString(R.string.fund_transfer_error_msg_3digits));
                etCommFundTransferred.requestFocus();
            } else {
                Map inputMap = new HashMap();

                inputMap.put("_userId", userID);
                /*if (isEquityOriginal()) {
                    inputMap.put("_bankName", etEquBankName.getText().toString().trim());
                    inputMap.put("_accountNo", etEquAccNo.getText().toString().trim());
                } else {*/
                inputMap.put("_bankName", etCommBankName.getText().toString().trim());
                inputMap.put("_accountNo", etCommAccNo.getText().toString().trim());
                //                }
                inputMap.put("_initialMargin", strCommFundAmount);
                inputMap.put("_addtionalMargin", "0");
                inputMap.put("_totalAmount", strCommFundAmount);
                inputMap.put("_segmentType", "Commodity");

                String flag = UPDATENBPAYMENT;
                String category = FUNDTRANSFER;
                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, flag, category, inputMap,
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
                                                    //                                                EkycMainActivity.vpApply.setCurrentItem(2);
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
                                            /*Intent intent = new Intent(mContext, WebActivity.class);
                                            intent.putExtra(URL_PATH, urlPath);
                                            startActivity(intent);*/
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
                                //                            MOSLCommon.showToastMsg(mContext, NO_RESPONSE);
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
            Map inputMap = new HashMap();
            inputMap.put("_userId", userID);
            inputMap.put("_segmentType", "Equity");

            String flag = UPDATESKIPPAYMENT;
            String category = FUNDTRANSFER;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
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

    private void equMakePayment() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            String strEquFundAmount = etEquFundTransferred.getText().toString();
            if (!validateEditText(etEquFundTransferred, input_layout_equFundTransferred,
                "Enter Amount")) {
                return;
            } else if (!(Integer.parseInt(strEquFundAmount) > 0) && !getAppType().equalsIgnoreCase(
                DIRECT)) {
                etEquFundTransferred.requestFocus();
                //  validateEditText(etEquFundTransferred, input_layout_equFundTransferred,);
            } else if (!getAppType().equalsIgnoreCase(PP)
                && !getAppType().equalsIgnoreCase(DIRECT)
                && etEquFundTransferred.length() < 4) {
                etEquFundTransferredError.setText(
                    getResources().getString(R.string.fund_transfer_error_msg));
                etEquFundTransferredError.setVisibility(View.VISIBLE);
                etEquFundTransferred.requestFocus();
            } else if (getAppType().equalsIgnoreCase(DIRECT) && etEquFundTransferred.length() < 3) {
                etEquFundTransferredError.setVisibility(View.VISIBLE);
                etEquFundTransferredError.setText(
                    getResources().getString(R.string.fund_transfer_error_msg_3digits));
                etEquFundTransferred.requestFocus();
            } else {
                Map inputMap = new HashMap();
                inputMap.put("_userId", userID);
                inputMap.put("_bankName", strEquBankName);
                inputMap.put("_accountNo", strEquAccNo);
                inputMap.put("_initialMargin", strEquFundAmount);
                inputMap.put("_addtionalMargin", "0");
                inputMap.put("_totalAmount", strEquFundAmount);
                inputMap.put("_segmentType", "Equity");

                String flag = UPDATENBPAYMENT;
                String category = FUNDTRANSFER;
                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, flag, category, inputMap,
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
                                                    //EkycMainActivity.vpApply.setCurrentItem(2);
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
                                            /* Intent intent = new Intent(mContext, WebActivity.class);
                                            intent.putExtra(URL_PATH, urlPath);
                                            startActivity(intent);*/
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
                                //                            MOSLCommon.showToastMsg(mContext, NO_RESPONSE);
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

    public void getPaymentStatus() {
        if (viewPager == null || viewPager.getCurrentItem() != getBankTab()) {
            return;
        }

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("_userId", userID);

            String flag = GETPAYMENTSTATUS;
            String category = FUNDTRANSFER;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {

                            try {
                                //                             {"FUNDTRANSFER":[{"EQPAYMENTSTATUS":3.0,"COMMPAYMENTSTATUS":3.0}]}
                                if (response.has("FUNDTRANSFER")) {
                                    JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");
                                    BigDecimal bdEqPaymentStatus = null, bdCommPaymentStatus = null;
                                    String eqPaymentStatus = "", commPaymentStatus = "";
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
                                                /*setIsEquityFundTransfered(true);
                                                setIsCommodityFundTransfered(true);*/
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
                                                    //   setIsEquityFundTransfered(true);
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
                                                    // setIsCommodityFundTransfered(true);
                                                    getUserpaymentDetail(4,
                                                        (int) bdCommPaymentStatus.longValue());
                                                }
                                            }

                                            //                                        getUserpaymentDetail();
                                       /* mEquStatus = (int) bdEqPaymentStatus.longValue();
                                        mCommStatus = (int) bdCommPaymentStatus.longValue();*/
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

    public void getUserpaymentDetail(final int eqPaymentStatus, final int commPaymentStatus) {
        if (viewPager == null || viewPager.getCurrentItem() != getBankTab()) {
            return;
        }

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("_userId", userID);

            String flag = GETUSERPAYMENTDETAILS;
            String category = FUNDTRANSFER;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {

                            try {


                             /*{
                                "FUNDTRANSFER":[
                                {
                                    "BANK_NAME":"ICICI BANK LTD",
                                        "BANK_ACC_NO":"112211111111",
                                        "CHEQUENO":"",
                                        "CHEQUEDATE":"28 Oct 2016",
                                        "MARGIN_DEPOSIT":"50000",
                                        "SCHEME_NAME":"Margin 50,000",
                                        "AMOUNTREQUESTED":0,
                                        "PRODUCT":"EQUITY"
                                },
                                {
                                    "BANK_NAME":"ICICI BANK LTD",
                                        "BANK_ACC_NO":"2222222222222",
                                        "CHEQUENO":"",
                                        "CHEQUEDATE":"28 Oct 2016",
                                        "MARGIN_DEPOSIT":"",
                                        "SCHEME_NAME":"",
                                        "AMOUNTREQUESTED":0,
                                        "PRODUCT":"COMMODITY"
                                }
                                ]
                            }*/
                                if (response.has("FUNDTRANSFER")) {
                                    JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");
                                    if (jsonArray.length() != 0) {

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
                                            String chequeNo =
                                                (object.isNull("CHEQUENO") || object.getString(
                                                    "CHEQUENO").equals("")) ? ""
                                                    : object.getString("CHEQUENO");
                                            String chequeDate =
                                                (object.isNull("CHEQUEDATE") || object.getString(
                                                    "CHEQUEDATE").equals("")) ? ""
                                                    : object.getString("CHEQUEDATE");
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
                                            /*
                                             *  fundTransferThroughId 1 fundTransferThrough Net Banking
                                             *  fundTransferThroughId 2 fundTransferThrough Debit Card
                                             *  fundTransferThroughId 3 fundTransferThrough Cheque
                                             *  fundTransferThroughId 4 fundTransferThrough Skipped Payment
                                             *  fundTransferThroughId 0 fundTransferThrough Payment Pending
                                             */
                                            if (product.equalsIgnoreCase(EQUITY)) {
                                                eqPaymentMode = fundTransferThrough;
                                                eqPaymentId =
                                                    Integer.parseInt(fundTransferThroughId);
                                                etEquBankName.setText(bankName);
                                                etEquAccNo.setText(bankAccNo);
                                                if (getAppType().equals(PP)) {
                                                    if (amtRequested.equalsIgnoreCase("0")) {
                                                        etEquFundTransferred.setText("");
                                                    } else {
                                                        etEquFundTransferred.setText(amtRequested);
                                                    }
                                                } else {
                                                    etEquFundTransferred.setText(marginDeposit);
                                                }
                                                if (bankflag.equalsIgnoreCase("1")) {
                                                    onlineEquFlag = true;
                                                } else if (bankflag.equalsIgnoreCase("0")) {
                                                    onlineEquFlag = false;
                                                    //                                                        btnEquPayment.setEnabled(false);
                                                    btnEquPayment.setBackgroundResource(
                                                        R.drawable.ic_disable_submit_btn);
                                                    etEquFundTransferred.setEnabled(false);
                                                }
                                            } else if (product.equalsIgnoreCase(COMMODITY)) {
                                                commPaymentMode = fundTransferThrough;
                                                commPaymentId =
                                                    Integer.parseInt(fundTransferThroughId);
                                                etCommBankName.setText(bankName);
                                                etCommAccNo.setText(bankAccNo);
                                                if (getAppType().equals(PP)) {
                                                    if (amtRequested.equalsIgnoreCase("0")) {
                                                        etCommFundTransferred.setText("");
                                                    } else {
                                                        etCommFundTransferred.setText(amtRequested);
                                                    }
                                                } else {
                                                    etCommFundTransferred.setText(marginDeposit);
                                                }
                                                if (bankflag.equalsIgnoreCase("1")) {
                                                    onlineCommFlag = true;
                                                } else if (bankflag.equalsIgnoreCase("0")) {
                                                    onlineCommFlag = false;
                                                    //                                                        btnCommPayment.setEnabled(false);
                                                    btnCommPayment.setBackgroundResource(
                                                        R.drawable.ic_disable_submit_btn);
                                                    etCommFundTransferred.setEnabled(false);
                                                }
                                            }
                                        }
                                        showView(eqPaymentStatus, commPaymentStatus);
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
                            //                                MOSLCommon.showToastMsg(mContext, NO_RESPONSE);
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
                }

            );
        } else

        {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void showView(int mEquStatus, int mCommStatus) {
        if (getIsEquitySelected() && getIsCommoditySelected()) {
            if (mEquStatus != 0 && mCommStatus != 0) {
                //both segment selected and both are paid
                //goto upload
                //                EkycMainActivity.vpApply.setCurrentItem(2,false);

                equityPaid();
                commodityPaid();
                if (getAppType().equals(DIRECT)) {
                    if (getIsAadharVerified()) {
                        if (equMakePaymentButton || commMakePaymentButton) {
                            tabPageIndicator.enableTab(getUploadTab());
                            viewPager.setCurrentItem(getUploadTab(), false);
                        }
                    } else if (!getIsAadharVerified()) {
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
            } else if (mEquStatus == 4) { // Equity not selected
                noEquitySegment();
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
                        } else if (!getIsAadharVerified()) {
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
            } else if (mCommStatus == 4) { // Commodity not selected
                noCommSegment();
            } else { //paid
                if (getIsEquitySelected()) { //if equity is also selected
                    commodityPaid();
                } else {
                    //                    EkycMainActivity.vpApply.setCurrentItem(2,false);
                    commodityPaid();
                    if (getAppType().equals(DIRECT)) {
                        if (getIsAadharVerified()) {
                            if (commMakePaymentButton) {
                                tabPageIndicator.enableTab(getUploadTab());
                                viewPager.setCurrentItem(getUploadTab(), false);
                            }
                        } else if (!getIsAadharVerified()) {
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
        /*if (mEquStatus == 0 && mCommStatus == 0) {
            showEquitypending();
            showCommPending();

        } else {
            if ((mEquStatus == 1 || mEquStatus == 2 || mEquStatus == 3) && (mCommStatus == 1 || mCommStatus == 2 || mCommStatus == 3)) {
                EkycMainActivity.vpApply.setCurrentItem(2);
            }
            if (mEquStatus == 1 || mEquStatus == 2 || mEquStatus == 3) {
                equityPaid();
                //EkycMainActivity.vpApply.setCurrentItem(2);
            } else if (mEquStatus == 0) {
                showEquitypending();
                if (mCommStatus == 4) {
                    noCommSegment();
                }
            }
            //no equity segment and commodity paid
            if (mCommStatus == 1 || mCommStatus == 2 || mCommStatus == 3) {
                commodityPaid();
                //EkycMainActivity.vpApply.setCurrentItem(2);
            } else if (mCommStatus == 0) {
                showCommPending();
                if (mEquStatus == 4) {
                    noEquitySegment();
                }

            }
        }*/

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

    private void showEquitypending() {
        llEquFundOption.setVisibility(View.VISIBLE);
        tvEquFundTransfered.setVisibility(View.GONE);
        //        etEquBankName.setText(strEquBankName);
        //        etEquAccNo.setText(strEquAccNo);
        //        etEquFundTransferred.setText(equTotalAmount);
    }

    private void noEquitySegment() {
        llEquityNetBanking.setVisibility(View.GONE);
    }

    private void showCommPending() {
        llCommFundOption.setVisibility(View.VISIBLE);
        tvCommFundTransfered.setVisibility(View.GONE);
        //        etCommBankName.setText(strCommBankName);
        //        etCommAccNo.setText(strCommAccNo);
        //        etCommFundTransferred.setText(commTotalAmount);
    }

    private void noCommSegment() {
        llCommodityNetBanking.setVisibility(View.GONE);
    }

    private void equityPaid() {
        setIsEquityFundTransfered(true);
        tvEquFundTransfered.setVisibility(View.VISIBLE);
        //        tvEquFundTransfered.setText("Equity Fund Transferred - " + eqPaymentMode);
        tvEquFundTransfered.setText("Fund Transferred - " + eqPaymentMode);
        /*
         *  fundTransferThroughId 1 fundTransferThrough Net Banking
         *  fundTransferThroughId 2 fundTransferThrough Debit Card
         *  fundTransferThroughId 3 fundTransferThrough Cheque
         *  fundTransferThroughId 4 fundTransferThrough Skipped Payment
         *  fundTransferThroughId 0 fundTransferThrough Payment Pending
         */
        if (eqPaymentId == 1) {
            disableField(llEquFundOption);
            llEquPaymentButton.setVisibility(View.GONE);
            if (getIsEquitySelected() && getIsCommoditySelected()) {
                if (isEquityFundTransfered() && isCommodityFundTransfered()) {
                    btnEquNext.setVisibility(View.VISIBLE);
                    btnEquNext.setEnabled(true);
                }
            } else if (getIsEquitySelected() && isEquityFundTransfered()) {
                btnEquNext.setVisibility(View.VISIBLE);
                btnEquNext.setEnabled(true);
            }
        } else {
            llEquFundOption.setVisibility(View.GONE);
            llEquPaymentButton.setVisibility(View.GONE);
            if (getIsEquitySelected() && getIsCommoditySelected()) {
                if (isEquityFundTransfered() && isCommodityFundTransfered()) {
                    btnEquNext.setVisibility(View.VISIBLE);
                    btnEquNext.setEnabled(true);
                }
            } else if (getIsEquitySelected() && isEquityFundTransfered()) {
                btnEquNext.setVisibility(View.VISIBLE);
                btnEquNext.setEnabled(true);
            }
        }
    }

    private void commodityPaid() {
        setIsCommodityFundTransfered(true);
        tvCommFundTransfered.setVisibility(View.VISIBLE);
        tvCommFundTransfered.setText("Fund Transferred - " + commPaymentMode);
        /*
         *  fundTransferThroughId 1 fundTransferThrough Net Banking
         *  fundTransferThroughId 2 fundTransferThrough Debit Card
         *  fundTransferThroughId 3 fundTransferThrough Cheque
         *  fundTransferThroughId 4 fundTransferThrough Skipped Payment
         *  fundTransferThroughId 0 fundTransferThrough Payment Pending
         */
        if (commPaymentId == 1) {
            disableField(llCommFundOption);
            llCommPaymentButton.setVisibility(View.GONE);
            if (getIsEquitySelected() && getIsCommoditySelected()) {
                if (isEquityFundTransfered() && isCommodityFundTransfered()) {
                    btnCommNext.setVisibility(View.VISIBLE);
                    btnCommNext.setEnabled(true);
                }
            } else if (getIsCommoditySelected() && isCommodityFundTransfered()) {
                btnCommNext.setVisibility(View.VISIBLE);
                btnCommNext.setEnabled(true);
            }
        } else {
            llCommFundOption.setVisibility(View.GONE);
            llCommPaymentButton.setVisibility(View.GONE);
            if (getIsEquitySelected() && getIsCommoditySelected()) {
                if (isEquityFundTransfered() && isCommodityFundTransfered()) {
                    btnCommNext.setVisibility(View.VISIBLE);
                    btnCommNext.setEnabled(true);
                }
            } else if (getIsCommoditySelected() && isCommodityFundTransfered()) {
                btnCommNext.setVisibility(View.VISIBLE);
                btnCommNext.setEnabled(true);
            }
        }
    }

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getPaymentStatus();
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

    /**
     * this method will hide progress when received the response of GETPAYMENTSTATUS & GETUSERPAYMENTDETAILS
     */
    private void hideProgress() {
        hideProgressCounter++;
        if (hideProgressCounter == 2) {
            hideProgressCounter = 0;
            customProgress.setVisibility(View.GONE);
        }
    }
}

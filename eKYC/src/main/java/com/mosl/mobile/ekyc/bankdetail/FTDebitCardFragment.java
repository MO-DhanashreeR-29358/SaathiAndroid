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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.CustomTabsHelper;
import com.mosl.mobile.ekyc.common.CustomViewPager;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.directmodule.DirectResumeApplicationActivity;
import com.mosl.mobile.ekyc.model.RowData;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.CustomDialog;
import com.mosl.mobile.ekyc.ui.WebActivity;
import com.viewpagerindicator.TabPageIndicator;
import cz.msebera.android.httpclient.Header;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class FTDebitCardFragment extends Fragment implements MOSLConstants, View.OnClickListener {

    static Context mContext;
    View debitCardView;
    private final AdapterView.OnItemSelectedListener equCommListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCommBankShortName = (arrCommodityBankName.get(position)).getKey();
                strCommBankName = (arrCommodityBankName.get(position)).getValue();
                etCommDebitBankNameError.setVisibility(View.GONE);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    RadioButton rbEquityATM, rbEquityVisa, rbCommodityATM, rbCommodityVisa;
    Button btnEquPayment, btnEquSkipPayment, btnCommPayment, btnCommSkipPayment, btnEquNext,
        btnCommNext;
    Dialog dialog;
    Spinner spEquDebitBankName, spCommDebitBankName;
    LinearLayout llEquityFundTransfer, llCommodityFundTransfer, /*customProgress,*/
        llEquDebitBankName, llCommDebitBankName, llCommFundOption, llEquFundOption, llEquityVisa,
        llCommodityVisa, llEquPaymentButton, llCommPaymentButton;
    String userID;
    ArrayAdapter<RowData> arrAdapEquityBankName, arrAdapCommodityBankName;
    ArrayList<RowData> arrEquityBankName, arrCommodityBankName;
    TextView etEquDebitBankNameError, etCommDebitBankNameError, etEquFundTransferredError,
        etCommFundTransferredError;
    static int mEquStatus, mCommStatus;
    TextView tvEquFundTransfered, tvCommFundTransfered;
    RelativeLayout rlEquSkipPayment, rlCommSkipPayment;
    TextInputLayout input_layout_equFundTransferred, input_layout_commFundTransferred;
    private CustomTabsClient mClient;
    String strEquBankShortName, strEquBankName, strCommBankShortName, strCommBankName, equMargin =
        "", equTotalAmount = "", strEquAccNo = "", strCommAccNo = "", commMargin = "",
        commTotalAmount = "";
    //Boolean equPayed, commPayed, equPaySkipped, commPaySkipped;
    CustomViewPager viewPager;
    private final AdapterView.OnItemSelectedListener equBankListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strEquBankShortName = (arrEquityBankName.get(position)).getKey();
                strEquBankName = (arrEquityBankName.get(position)).getValue();
                etEquDebitBankNameError.setVisibility(View.GONE);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    TabPageIndicator tabPageIndicator;
    private boolean onlineEquFlag;
    private boolean onlineCommFlag;
    Boolean equMakePaymentButton = false;
    Boolean commMakePaymentButton = false;
    ScrollView scrollView;
    int hideProgressCounter = 0;
    String eqPaymentMode, commPaymentMode;
    int eqPaymentId, commPaymentId;
    private View layoutMergeNote;

    public void setTabPageIndicator(TabPageIndicator tabPageIndicator) {
        this.tabPageIndicator = tabPageIndicator;
    }

    /* ,equMargin="10000",equTotalAmount="10000",
     commMargin="10000",commTotalAmount="10000";*/ EditText etEquFundTransferred,
        etCommFundTransferred;

    public static FTDebitCardFragment newInstance(Context context, int equityStatus,
        int commodityStatus) {

        FTDebitCardFragment fragment = new FTDebitCardFragment();
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
            debitCardView = inflater.inflate(R.layout.frag_debitcard, container, false);
            setUpView(debitCardView);
        } catch (Exception e) {
            e.printStackTrace();
            //            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            //            Crashlytics.logException(e);
        }
        return debitCardView;
    }

    @Override public void onResume() {
        super.onResume();
        if (!getInterestedInMF()
            && viewPager != null
            && viewPager.getCurrentItem() == getBankTab()) {
            getPaymentStatus();
        }
    }

    public void getPaymentStatus() {
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
                                               /* setIsEquityFundTransfered(true);
                                                setIsCommodityFundTransfered(true);*/
                                                    //                                                showView((int) bdEqPaymentStatus.longValue(), (int) bdCommPaymentStatus.longValue());
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
                                                    //                                                setIsEquityFundTransfered(true);
                                                    //                                                showView((int) bdEqPaymentStatus.longValue(), 4);
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
                                                    //                                                setIsCommodityFundTransfered(true);
                                                    //                                                showView(4, (int) bdCommPaymentStatus.longValue());
                                                    getUserpaymentDetail(4,
                                                        (int) bdCommPaymentStatus.longValue());
                                                }
                                            }
                                            //                                        getUserpaymentDetail();
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
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("_userId", userID);

            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, GETUSERPAYMENTDETAILS, FUNDTRANSFER, inputMap,
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
                                        String dcStatus = "", dcMessage = "";
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            String bankName =
                                                (object.isNull("BANK_NAME") || object.getString(
                                                    "BANK_NAME").equals("")) ? ""
                                                    : object.getString("BANK_NAME");
                                            if (bankName != null) {
                                                bankName = bankName.replace("\t", "");
                                            }

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
                                            dcStatus =
                                                (object.isNull("DC_STATUS")
                                                    || object.getString("DC_STATUS")
                                                    .equals("")) ? ""
                                                    : object.getString("DC_STATUS");
                                            dcMessage =
                                                (object.isNull("DC_MESSAGE")
                                                    || object.getString("DC_MESSAGE")
                                                    .equals("")) ? ""
                                                    : object.getString("DC_MESSAGE");
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
                                                RowData equBankNameRowData =
                                                    RowData.getSpinnerRowDataByValue(bankName,
                                                        arrAdapEquityBankName);
                                                if (equBankNameRowData != null) {
                                                    spEquDebitBankName.setSelection(
                                                        equBankNameRowData.getPosition());
                                                }
                                                equMargin = marginDeposit;
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
                                                    //                                                btnEquPayment.setEnabled(false);
                                                    btnEquPayment.setBackgroundResource(
                                                        R.drawable.ic_disable_submit_btn);
                                                    etEquFundTransferred.setEnabled(false);
                                                    rbEquityATM.setEnabled(false);
                                                    rbEquityVisa.setEnabled(false);
                                                    llEquityVisa.setEnabled(false);
                                                    spEquDebitBankName.setEnabled(false);
                                                }
                                            } else if (product.equalsIgnoreCase(COMMODITY)) {
                                                commPaymentMode = fundTransferThrough;
                                                commPaymentId =
                                                    Integer.parseInt(fundTransferThroughId);
                                                RowData commBankNameRowData =
                                                    RowData.getSpinnerRowDataByValue(bankName,
                                                        arrAdapCommodityBankName);
                                                if (commBankNameRowData != null) {
                                                    spCommDebitBankName.setSelection(
                                                        commBankNameRowData.getPosition());
                                                }
                                                commMargin = marginDeposit;
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
                                                    //                                                btnCommPayment.setEnabled(false);
                                                    btnCommPayment.setBackgroundResource(
                                                        R.drawable.ic_disable_submit_btn);
                                                    etCommFundTransferred.setEnabled(false);
                                                    rbCommodityATM.setEnabled(false);
                                                    rbCommodityVisa.setEnabled(false);
                                                    llCommodityVisa.setEnabled(false);
                                                    spCommDebitBankName.setEnabled(false);
                                                }
                                            }
                                        }
                                        if (Integer.parseInt(dcStatus) == 1) {
                                            showView(eqPaymentStatus, commPaymentStatus);
                                        } else {
                                            llEquFundOption.setVisibility(View.GONE);
                                            llEquPaymentButton.setVisibility(View.GONE);
                                            //layoutMergeNote.setVisibility(View.GONE);
                                            tvEquFundTransfered.setVisibility(View.VISIBLE);
                                            tvEquFundTransfered.setText(dcMessage);
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
    }

    private void showEquitypending() {
        llEquFundOption.setVisibility(View.VISIBLE);
        tvEquFundTransfered.setVisibility(View.GONE);
    }

    private void showCommPending() {
        llCommFundOption.setVisibility(View.VISIBLE);
        tvCommFundTransfered.setVisibility(View.GONE);
    }

    private void setUpView(View debitCardView) {
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

       /* strEquBankName = CommonModel.equBankName;
        strCommBankName = CommonModel.commBankName;
        strEquAccNo = CommonModel.equAccNo;
        strCommAccNo = CommonModel.commAccNo;
        equMargin = CommonModel.equMargin;
        equTotalAmount = CommonModel.equTotalAmount;
        commMargin = CommonModel.commMargin;
        commTotalAmount = BankDetailModel.commTotalAmount;*/
        scrollView = (ScrollView) debitCardView.findViewById(R.id.scrollView);
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
        layoutMergeNote = debitCardView.findViewById(R.id.layout_merge_note);
        btnEquPayment = debitCardView.findViewById(R.id.btnEquPayment);
        btnCommPayment = debitCardView.findViewById(R.id.btnCommPayment);
        btnEquSkipPayment = debitCardView.findViewById(R.id.btnEquSkipPayment);
        btnCommSkipPayment = debitCardView.findViewById(R.id.btnCommSkipPayment);
        btnEquNext = debitCardView.findViewById(R.id.btnEquNext);
        btnCommNext = debitCardView.findViewById(R.id.btnCommNext);

        rbEquityATM = debitCardView.findViewById(R.id.rbEquityATM);
        rbCommodityATM = debitCardView.findViewById(R.id.rbCommodityATM);
        rbEquityVisa = debitCardView.findViewById(R.id.rbEquityVisa);
        rbCommodityVisa = debitCardView.findViewById(R.id.rbCommodityVisa);

        llEquDebitBankName = debitCardView.findViewById(R.id.llEquDebitBankName);
        llEquityVisa = debitCardView.findViewById(R.id.llEquityVisa);
        llCommodityVisa = debitCardView.findViewById(R.id.llCommodityVisa);
        llCommDebitBankName = debitCardView.findViewById(R.id.llCommDebitBankName);
        llEquityFundTransfer = debitCardView.findViewById(R.id.llEquityFundTransfer);
        llCommodityFundTransfer =
            debitCardView.findViewById(R.id.llCommodityFundTransfer);
        // customProgress = debitCardView.findViewById(R.id.customProgress);
        llEquFundOption = debitCardView.findViewById(R.id.llEquFundOption);
        llCommFundOption = debitCardView.findViewById(R.id.llCommFundOption);
        llEquPaymentButton = debitCardView.findViewById(R.id.llEquPaymentButton);
        llCommPaymentButton = debitCardView.findViewById(R.id.llCommPaymentButton);

        rbEquityATM.setOnClickListener(this);
        llEquityVisa.setOnClickListener(this);
        rbEquityVisa.setOnClickListener(this);
        rbCommodityATM.setOnClickListener(this);
        llCommodityVisa.setOnClickListener(this);
        rbCommodityVisa.setOnClickListener(this);

        spEquDebitBankName = debitCardView.findViewById(R.id.spEquDebitBankName);
        spCommDebitBankName = debitCardView.findViewById(R.id.spCommDebitBankName);

        etEquFundTransferred = debitCardView.findViewById(R.id.etEquFundTransferred);
        etCommFundTransferred = debitCardView.findViewById(R.id.etCommFundTransferred);
        etEquDebitBankNameError =
            debitCardView.findViewById(R.id.etEquDebitBankNameError);
        etCommDebitBankNameError =
            debitCardView.findViewById(R.id.etCommDebitBankNameError);
        etEquFundTransferredError =
            debitCardView.findViewById(R.id.etEquFundTransferredError);
        etCommFundTransferredError =
            debitCardView.findViewById(R.id.etCommFundTransferredError);

        tvCommFundTransfered = debitCardView.findViewById(R.id.tvCommFundTransfered);
        tvEquFundTransfered = debitCardView.findViewById(R.id.tvEquFundTransfered);
        rlEquSkipPayment = debitCardView.findViewById(R.id.rlEquSkipPayment);
        rlCommSkipPayment = debitCardView.findViewById(R.id.rlCommSkipPayment);
        input_layout_equFundTransferred =
            debitCardView.findViewById(R.id.input_layout_equFundTransferred);
        input_layout_commFundTransferred =
            debitCardView.findViewById(R.id.input_layout_commFundTransferred);
        if (getIsEquitySelected() && getIsCommoditySelected()) {
            llEquityFundTransfer.setVisibility(View.VISIBLE);
            //            llCommodityFundTransfer.setVisibility(View.VISIBLE);
            equATMSelected();
            commATMSelected();
        } else if (getIsEquitySelected() || getIsDerivativeSelected()) {
            llEquityFundTransfer.setVisibility(View.VISIBLE);
            llCommodityFundTransfer.setVisibility(View.GONE);
            equATMSelected();
        } else if (getIsCommoditySelected()) {
            llEquityFundTransfer.setVisibility(View.GONE);
            llCommodityFundTransfer.setVisibility(View.VISIBLE);
            commATMSelected();
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

        /*rbEquityATM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    rbEquityVisa.setChecked(false);
                    rbEquityATM.setChecked(true);
                    llEquDebitBankName.setVisibility(View.VISIBLE);
                    equATMSelected();
                }
            }
        });
        llEquityVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbEquityVisa.setChecked(true);
                rbEquityATM.setChecked(false);
                llEquDebitBankName.setVisibility(View.GONE);
            }
        });*/

        /*rbEquityVisa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    rbEquityVisa.setChecked(true);
                    rbEquityATM.setChecked(false);
                    llEquDebitBankName.setVisibility(View.GONE);
                }
            }
        });*/
       /* rbCommodityATM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    rbCommodityVisa.setChecked(false);
                    rbCommodityATM.setChecked(true);
                    llCommDebitBankName.setVisibility(View.VISIBLE);
                }

            }
        });*/

        /*rbCommodityVisa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    rbCommodityVisa.setChecked(true);
                    rbCommodityATM.setChecked(false);
                    llCommDebitBankName.setVisibility(View.GONE);
                }
            }
        });*/
       /* llCommodityVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbCommodityVisa.setChecked(true);
                rbCommodityATM.setChecked(false);
                llCommDebitBankName.setVisibility(View.GONE);
            }
        });*/
        etEquFundTransferred.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etEquFundTransferred.getText().toString())) {
                    input_layout_equFundTransferred.setErrorEnabled(false);
                    if (!getAppType().equalsIgnoreCase(PP)
                        && !getAppType().equalsIgnoreCase(DIRECT)
                        && etEquFundTransferred.length() < 5) {
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
                        && etCommFundTransferred.length() < 5) {
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

        btnEquPayment.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
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

        btnCommSkipPayment.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                skipCommPayment();
                commMakePaymentButton = true;
            }
        });

        btnEquNext.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                tabPageIndicator.enableTab(getUploadTab());
                viewPager.setCurrentItem(getUploadTab(), false);
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
            getEquDebitCardBank();
        } else {
            equityPaid();
        }
        if (mCommStatus == 0) {
            showCommPending();
            getEquDebitCardBank();
        } else {
            commodityPaid();
        }
    }

    @Override public void onClick(View v) {

        if (v.getId() == R.id.rbEquityATM) {
            rbEquityVisa.setChecked(false);
            rbEquityATM.setChecked(true);
            llEquDebitBankName.setVisibility(View.VISIBLE);
            equATMSelected();
        } else if (v.getId() == R.id.llEquityVisa) {
            rbEquityVisa.setChecked(true);
            rbEquityATM.setChecked(false);
            llEquDebitBankName.setVisibility(View.GONE);
        } else if (v.getId() == R.id.rbEquityVisa) {
            rbEquityVisa.setChecked(true);
            rbEquityATM.setChecked(false);
            llEquDebitBankName.setVisibility(View.GONE);
        } else if (v.getId() == R.id.rbCommodityATM) {
            rbCommodityVisa.setChecked(false);
            rbCommodityATM.setChecked(true);
            llCommDebitBankName.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.rbCommodityVisa) {
            rbCommodityVisa.setChecked(true);
            rbCommodityATM.setChecked(false);
            llCommDebitBankName.setVisibility(View.GONE);
        } else if (v.getId() == R.id.llCommodityVisa) {
            rbCommodityVisa.setChecked(true);
            rbCommodityATM.setChecked(false);
            llCommDebitBankName.setVisibility(View.GONE);
        }

        /*rbEquityATM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    rbEquityVisa.setChecked(false);
                    rbEquityATM.setChecked(true);
                    llEquDebitBankName.setVisibility(View.VISIBLE);
                    equATMSelected();
                }
            }
        });
        llEquityVisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbEquityVisa.setChecked(true);
                rbEquityATM.setChecked(false);
                llEquDebitBankName.setVisibility(View.GONE);
            }
        });*/

    }

    private void equATMSelected() {
        //region Radio button visibility changes 10-01-2019
        //rbEquityATM.setChecked(true);
        //llEquDebitBankName.setVisibility(View.VISIBLE);
        //endregion
        etEquFundTransferred.setText(equMargin);
    }

    private void noCommSegment() {
        llCommodityFundTransfer.setVisibility(View.GONE);
    }

    private void noEquitySegment() {
        llEquityFundTransfer.setVisibility(View.GONE);
    }

    private void commATMSelected() {
        //region Radio button visibility changes 10-01-2019
        //rbCommodityATM.setChecked(true);
        //llCommDebitBankName.setVisibility(View.VISIBLE);
        //endregion
        etCommFundTransferred.setText(commMargin);
    }

    private void equityPaid() {
        setIsEquityFundTransfered(true);
        tvEquFundTransfered.setVisibility(View.VISIBLE);
        tvEquFundTransfered.setText("Fund Transferred - " + eqPaymentMode);
        /*
         *  fundTransferThroughId 1 fundTransferThrough Net Banking
         *  fundTransferThroughId 2 fundTransferThrough Debit Card
         *  fundTransferThroughId 3 fundTransferThrough Cheque
         *  fundTransferThroughId 4 fundTransferThrough Skipped Payment
         *  fundTransferThroughId 0 fundTransferThrough Payment Pending
         */
        if (eqPaymentId == 2) {
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
        if (commPaymentId == 2) {
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

    private void getEquDebitCardBank() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put("", "");

            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, GETDCBANK, FUNDTRANSFER, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {

                            try {
                                if (response.has("FUNDTRANSFER")) {
                                    JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");

                                    arrEquityBankName = new ArrayList<>();
                                    arrCommodityBankName = new ArrayList<>();
                                    if (jsonArray.length() != 0) {
                                        arrEquityBankName.add(new RowData(0, "-1", "Select"));
                                        arrCommodityBankName.add(new RowData(0, "-1", "Select"));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                        /*{
                                            "BankName": "RuPay Debit Card",
                                                "BankShortName": "URP",
                                                "ProductId": "NA",
                                                "Categroy": "Debit Card"
                                        }*/
                                            String bankName =
                                                (object.isNull("BankName") || object.getString(
                                                    "BankName").equals("")) ? ""
                                                    : object.getString("BankName");
                                            String bankShortName =
                                                (object.isNull("BankShortName") || object.getString(
                                                    "BankShortName").equals("")) ? ""
                                                    : object.getString("BankShortName");
                                            String productId =
                                                (object.isNull("ProductId") || object.getString(
                                                    "ProductId").equals("")) ? ""
                                                    : object.getString("ProductId");
                                            String categroy =
                                                (object.isNull("Categroy") || object.getString(
                                                    "Categroy").equals("")) ? ""
                                                    : object.getString("Categroy");

                                            arrEquityBankName.add(
                                                new RowData((i + 1), bankShortName, bankName));
                                            arrCommodityBankName.add(
                                                new RowData((i + 1), bankShortName, bankName));
                                        }
                                    }
                                    arrAdapEquityBankName = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrEquityBankName);
                                    arrAdapEquityBankName.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    arrAdapEquityBankName.notifyDataSetChanged();
                                    spEquDebitBankName.setAdapter(arrAdapEquityBankName);
                                    spEquDebitBankName.setOnItemSelectedListener(equBankListener);

                                    arrAdapCommodityBankName = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrCommodityBankName);
                                    arrAdapCommodityBankName.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);

                                    spCommDebitBankName.setAdapter(arrAdapCommodityBankName);
                                    spCommDebitBankName.setOnItemSelectedListener(equCommListener);
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
            if (rbCommodityATM.isChecked() && spCommDebitBankName.getSelectedItem()
                .toString()
                .equalsIgnoreCase("Select")) {
                etCommDebitBankNameError.setVisibility(View.VISIBLE);
                etCommDebitBankNameError.setText("Select Bank for ATM cum-Debit Card");
                etCommDebitBankNameError.requestFocus();
            } else if (!validateEditText(etCommFundTransferred, input_layout_commFundTransferred,
                "Enter Amount")) {
                return;
            } else if (!(Integer.parseInt(strCommFundAmount) > 0)) {
                etCommFundTransferred.requestFocus();
                //  validateEditText(etEquFundTransferred, input_layout_equFundTransferred,);
            } else if (!getAppType().equalsIgnoreCase(PP)
                && !getAppType().equalsIgnoreCase(DIRECT)
                && etCommFundTransferred.length() < 5) {
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
                etCommDebitBankNameError.setVisibility(View.GONE);
                Map<String, String> inputMap = new HashMap<>();
                String debitCardBankValue = "", debitCardBankName = "", cardType;

                if (rbCommodityATM.isChecked()) {
                    debitCardBankValue = strCommBankShortName;
                    debitCardBankName = strCommBankName;
                    cardType = ATM;
                } else {
                    cardType = VISA;
                }

                inputMap.put("_userId", userID);
                inputMap.put("_bankName", strCommBankName);
                inputMap.put("_accountNo", strCommAccNo);
                inputMap.put("_initialMargin", strCommFundAmount);
                inputMap.put("_addtionalMargin", "0");
                inputMap.put("_totalAmount", strCommFundAmount);
                inputMap.put("_segmentType", "Commodity");
                inputMap.put("_debitCardBankValue", debitCardBankValue);
                inputMap.put("_debitCardBankName", debitCardBankName);
                inputMap.put("_cardType", cardType);

                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, UPDATEDCPAYMENT, FUNDTRANSFER, inputMap,
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
                                                    // EkycMainActivity.vpApply.setCurrentItem(2);
                                                    getPaymentStatus();
                                                } else {
                                                    informationDialog(
                                                        SOME_ERROR_OCCURED_WHILE_FUND_TRANSFER);
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
                                                if (!urlPath.equals("")) {
                                               /* Intent intent = new Intent(mContext, WebActivity.class);
                                                intent.putExtra(URL_PATH, urlPath);
                                                startActivity(intent);*/
                                                    openWebUrl(Uri.parse(urlPath));
                                                }
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

    private void equMakePayment() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            String strEquFundAmount = etEquFundTransferred.getText().toString();

            if (rbEquityATM.isChecked() && spEquDebitBankName.getSelectedItem()
                .toString()
                .equalsIgnoreCase("Select")) {
                etEquDebitBankNameError.setVisibility(View.VISIBLE);
                etEquDebitBankNameError.setText("Select Bank for ATM cum-Debit Card");
                etEquDebitBankNameError.requestFocus();
            } else if (!validateEditText(etEquFundTransferred, input_layout_equFundTransferred,
                "Enter Amount")) {
                return;
            } else if (!(Integer.parseInt(strEquFundAmount) > 0) && !getAppType().equalsIgnoreCase(
                DIRECT)) {
                etEquFundTransferred.requestFocus();
                //  validateEditText(etEquFundTransferred, input_layout_equFundTransferred,);
            } else if (!getAppType().equalsIgnoreCase(PP)
                && !getAppType().equalsIgnoreCase(DIRECT)
                && etEquFundTransferred.length() < 5) {
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
                etEquDebitBankNameError.setVisibility(View.GONE);
                Map<String, String> inputMap = new HashMap<>();
                String debitCardBankValue = "", debitCardBankName = "", cardType;

                if (rbEquityATM.isChecked()) {
                    debitCardBankValue = strEquBankShortName;
                    debitCardBankName = strEquBankName;
                    cardType = ATM;
                } else {
                    cardType = VISA;
                }

                inputMap.put("_userId", userID);
                inputMap.put("_bankName", strEquBankName);
                inputMap.put("_accountNo", strEquAccNo);
                inputMap.put("_initialMargin", strEquFundAmount);
                inputMap.put("_addtionalMargin", "0");
                inputMap.put("_totalAmount", strEquFundAmount);
                inputMap.put("_segmentType", "Equity");
                inputMap.put("_debitCardBankValue", debitCardBankValue);
                inputMap.put("_debitCardBankName", debitCardBankName);
                inputMap.put("_cardType", cardType);

                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, UPDATEDCPAYMENT, FUNDTRANSFER, inputMap,
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
                                                    informationDialog(
                                                        SOME_ERROR_OCCURED_WHILE_FUND_TRANSFER);
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

                                                if (!urlPath.equals("")) {
                                               /* Intent intent = new Intent(mContext, WebActivity.class);
                                                intent.putExtra(URL_PATH, urlPath);
                                                startActivity(intent);*/
                                                    openWebUrl(Uri.parse(urlPath));
                                                }
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
            }
        });
    }

    public void openInBrowser(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
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

    public void informationDialog(String msg) {
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_info);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMsg = dialog.findViewById(R.id.tvMsg);
        Button btnOK = dialog.findViewById(R.id.btnOK);
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

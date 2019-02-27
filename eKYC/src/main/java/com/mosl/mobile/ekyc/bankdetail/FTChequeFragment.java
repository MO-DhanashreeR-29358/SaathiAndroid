package com.mosl.mobile.ekyc.bankdetail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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
import com.mosl.mobile.ekyc.common.CustomViewPager;
import com.mosl.mobile.ekyc.common.EkycAppUtil;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.CustomDatePickerDialog;
import com.mosl.mobile.ekyc.ui.DatePickerListener;
import com.viewpagerindicator.TabPageIndicator;
import cz.msebera.android.httpclient.Header;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.disableField;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.removeKeyboard;
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
public class FTChequeFragment extends Fragment implements MOSLConstants {

    View chequeView;
    EditText etEquityChequeDate, etCommodityChequeDate;
    LinearLayout llEquityCheque, llCommodityCheque, llCommFundOption, llEquFundOption, llEquPaymentButton, llCommPaymentButton;
    //    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    Boolean eqCheqClicked = false;
    Boolean commCheqClicked = false;
    Boolean equMakePaymentButton = false;
    Boolean commMakePaymentButton = false;
    EditText etEquFundTransferred, etCommFundTransferred;
    Button btnEquPayment, btnEquSkipPayment, btnCommPayment, btnCommSkipPayment, btnEquNext, btnCommNext;
    EditText etEquBankName, etEquAccno, etEquCheqNo, etcommBankName, etCommAccNo, etCommCheqNo;
    String strEquBankShortName, strEquBankName, strCommBankShortName, strCommBankName, equMargin = "", equTotalAmount = "", strEquAccNo = "", strCommAccNo = "",
            commMargin = "", commTotalAmount = "";
    //    LinearLayout customProgress;
    static Context mContext;
    static int mEquStatus, mCommStatus;
    TextView tvEquFundTransfered, tvCommFundTransfered;
    String userID;
    CustomViewPager viewPager;
    TabPageIndicator tabPageIndicator;
    RelativeLayout rlEquSkipPayment, rlCommSkipPayment;
    TextInputLayout input_layout_equFundTransferred, input_layout_commFundTransferred, input_layout_commCheqNo, input_layout_equCheqNo;
    ScrollView scrollView;
    TextView etEquCheqNoError, etCommCheqNoError, etEquFundTransferredError, etCommFundTransferredError;
    int hideProgressCounter = 0;
    String eqPaymentMode, commPaymentMode;
    int eqPaymentId, commPaymentId;
    boolean showDefalutDate;
    String date = "";
    private boolean auSmall;

    public void setTabPageIndicator(TabPageIndicator tabPageIndicator) {
        this.tabPageIndicator = tabPageIndicator;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            chequeView = inflater.inflate(R.layout.frag_cheque, container, false);
            setUpView(chequeView);
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
        return chequeView;

    }

    public static FTChequeFragment newInstance(Context context, int equityStatus, int commodityStatus) {

        FTChequeFragment fragment = new FTChequeFragment();
        Bundle args = new Bundle();
        mContext = context;
        mEquStatus = equityStatus;
        mCommStatus = commodityStatus;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View chequeView) {
//        userID = "1451500";
        userID = getUserID();

        Calendar newCalendar = Calendar.getInstance();
//        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        /*fromDatePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Date date = newDate.getTime();
                // current date after 3 months
                Calendar currentDateAfter2Months = Calendar.getInstance();
                currentDateAfter2Months.add(Calendar.MONTH, 2);

                // current date before 3 months
                Calendar currentDateBefore3Months = Calendar.getInstance();
                currentDateBefore3Months.add(Calendar.MONTH, -3);
                Log.d("\n\ncurrentDate : ", "" + Calendar.getInstance().getTime());

                Log.d("curDtAfter2Months : ", "" + dateFormatter.format(currentDateAfter2Months.getTime()));
                Log.d("currDateBef3Months : ", "" + dateFormatter.format(currentDateBefore3Months.getTime()));
                Log.d("dateToValidate : ", dateFormatter.format(newDate.getTime()));

                if (date.before(currentDateAfter2Months.getTime())
                        && date.after(currentDateBefore3Months.getTime())) {
                    if (eqCheqClicked) {
                        etEquityChequeDate.setText(dateFormatter.format(newDate.getTime()));
                    } else {
                        etCommodityChequeDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                } else {
                    commonMsgDialog(getActivity(), "Cheque date must be 3 months backdated or 2 month postdated", false, "");
                }

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));*/
        scrollView = (ScrollView) chequeView.findViewById(R.id.scrollView);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        viewPager = (CustomViewPager) getActivity().findViewById(R.id.vpApply);
        etEquityChequeDate = (EditText) chequeView.findViewById(R.id.etEquityChequeDate);
        etCommodityChequeDate = (EditText) chequeView.findViewById(R.id.etCommodityChequeDate);

        llEquFundOption = (LinearLayout) chequeView.findViewById(R.id.llEquFundOption);
        llCommFundOption = (LinearLayout) chequeView.findViewById(R.id.llCommFundOption);
        llEquPaymentButton = (LinearLayout) chequeView.findViewById(R.id.llEquPaymentButton);
        llCommPaymentButton = (LinearLayout) chequeView.findViewById(R.id.llCommPaymentButton);
        llEquityCheque = (LinearLayout) chequeView.findViewById(R.id.llEquityCheque);
        llCommodityCheque = (LinearLayout) chequeView.findViewById(R.id.llCommodityCheque);

        etEquFundTransferred = (EditText) chequeView.findViewById(R.id.etEquFundTransferred);
        etCommFundTransferred = (EditText) chequeView.findViewById(R.id.etCommFundTransferred);
        etEquFundTransferredError = (TextView) chequeView.findViewById(R.id.etEquFundTransferredError);
        etCommFundTransferredError = (TextView) chequeView.findViewById(R.id.etCommFundTransferredError);

        etEquBankName = (EditText) chequeView.findViewById(R.id.etEquBankName);
        etEquAccno = (EditText) chequeView.findViewById(R.id.etEquAccno);
        etEquCheqNo = (EditText) chequeView.findViewById(R.id.etEquCheqNo);

        etcommBankName = (EditText) chequeView.findViewById(R.id.etcommBankName);
        etCommAccNo = (EditText) chequeView.findViewById(R.id.etCommAccNo);
        etCommCheqNo = (EditText) chequeView.findViewById(R.id.etCommCheqNo);
        etEquCheqNoError = (TextView) chequeView.findViewById(R.id.etEquCheqNoError);
        etCommCheqNoError = (TextView) chequeView.findViewById(R.id.etCommCheqNoError);

        etcommBankName.setEnabled(false);
        etCommAccNo.setEnabled(false);

        btnEquPayment = (Button) chequeView.findViewById(R.id.btnEquPayment);
        btnCommPayment = (Button) chequeView.findViewById(R.id.btnCommPayment);
        btnEquSkipPayment = (Button) chequeView.findViewById(R.id.btnEquSkipPayment);
        btnCommSkipPayment = (Button) chequeView.findViewById(R.id.btnCommSkipPayment);
        btnEquNext = (Button) chequeView.findViewById(R.id.btnEquNext);
        btnCommNext = (Button) chequeView.findViewById(R.id.btnCommNext);
//        customProgress = (LinearLayout) chequeView.findViewById(R.id.customProgress);
        tvCommFundTransfered = (TextView) chequeView.findViewById(R.id.tvCommFundTransfered);
        tvEquFundTransfered = (TextView) chequeView.findViewById(R.id.tvEquFundTransfered);
        rlEquSkipPayment = (RelativeLayout) chequeView.findViewById(R.id.rlEquSkipPayment);
        rlCommSkipPayment = (RelativeLayout) chequeView.findViewById(R.id.rlCommSkipPayment);
        input_layout_equFundTransferred = (TextInputLayout) chequeView.findViewById(R.id.input_layout_equFundTransferred);
        input_layout_commFundTransferred = (TextInputLayout) chequeView.findViewById(R.id.input_layout_commFundTransferred);
        input_layout_equCheqNo = (TextInputLayout) chequeView.findViewById(R.id.input_layout_equCheqNo);
        input_layout_commCheqNo = (TextInputLayout) chequeView.findViewById(R.id.input_layout_commCheqNo);

        etEquFundTransferred.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etEquFundTransferred.getText().toString())) {
                    input_layout_equFundTransferred.setErrorEnabled(false);
                    if (!getAppType().equalsIgnoreCase(PP)
                        && !auSmall
                        && etEquFundTransferred.length() < 5) {
                        etEquFundTransferredError.setVisibility(View.VISIBLE);
                        etEquFundTransferredError.setText(getResources().getString(R.string.fund_transfer_error_msg));
                        etEquFundTransferred.requestFocus();
                    } else if (getAppType().equalsIgnoreCase(PP) && !(Integer.parseInt(etEquFundTransferred.getText().toString()) > 0)) {
                        etEquFundTransferredError.setVisibility(View.VISIBLE);
                        etEquFundTransferredError.setText(getResources().getString(R.string.fund_transfer_error_msg_PP));
                        etEquFundTransferred.requestFocus();
                    } else {
                        etEquFundTransferredError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_equFundTransferred.setErrorEnabled(false);
                    etEquFundTransferredError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etCommFundTransferred.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etCommFundTransferred.getText().toString())) {
                    input_layout_commFundTransferred.setErrorEnabled(false);
                    if (!getAppType().equalsIgnoreCase(PP)
                        && !auSmall
                        && etCommFundTransferred.length() < 5) {
                        etCommFundTransferredError.setVisibility(View.VISIBLE);
                        etCommFundTransferredError.setText(getResources().getString(R.string.fund_transfer_error_msg));
                        etCommFundTransferred.requestFocus();
                    } else if (getAppType().equalsIgnoreCase(PP) && !(Integer.parseInt(etCommFundTransferred.getText().toString()) > 0)) {
                        etCommFundTransferredError.setVisibility(View.VISIBLE);
                        etCommFundTransferredError.setText(getResources().getString(R.string.fund_transfer_error_msg_PP));
                        etCommFundTransferred.requestFocus();
                    } else {
                        etCommFundTransferredError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_commFundTransferred.setErrorEnabled(false);
                    etCommFundTransferredError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (getIsEquitySelected() && getIsCommoditySelected()) {
            llEquityCheque.setVisibility(View.VISIBLE);
//            llCommodityCheque.setVisibility(View.VISIBLE);
            etEquFundTransferred.setText(equMargin);
            etCommFundTransferred.setText(commMargin);
        } else if (getIsEquitySelected() || getIsDerivativeSelected()) {
            llEquityCheque.setVisibility(View.VISIBLE);
            llCommodityCheque.setVisibility(View.GONE);
            etEquFundTransferred.setText(equMargin);
        } else if (getIsCommoditySelected()) {
            llEquityCheque.setVisibility(View.GONE);
            llCommodityCheque.setVisibility(View.VISIBLE);
            etCommFundTransferred.setText(commMargin);
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
        etCommCheqNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etCommCheqNo.getText().toString())) {
                    input_layout_commCheqNo.setErrorEnabled(false);
                    if (etCommCheqNo.length() < 6) {
                        etCommCheqNoError.setVisibility(View.VISIBLE);
                        etCommCheqNoError.setText("Invalid cheque no.");
                        etCommCheqNo.requestFocus();
                    } else {
                        input_layout_commCheqNo.setErrorEnabled(false);
                        etCommCheqNoError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_commCheqNo.setErrorEnabled(false);
                    etCommCheqNoError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEquCheqNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etEquCheqNo.getText().toString())) {
                    input_layout_equCheqNo.setErrorEnabled(false);
                    if (etEquCheqNo.length() < 6) {
                        etEquCheqNoError.setVisibility(View.VISIBLE);
                        etEquCheqNoError.setText("Invalid cheque no.");
                        etEquCheqNo.requestFocus();
                    } else {
                        input_layout_equCheqNo.setErrorEnabled(false);
                        etEquCheqNoError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_equCheqNo.setErrorEnabled(false);
                    etEquCheqNoError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etEquityChequeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (!isBlank(etEquityChequeDate.getText().toString())) {
                        /*Date dob = EkycAppUtil.getDate(etEquityChequeDate.getText().toString(), "dd/MM/yyyy");
                        fromDatePickerDialog.updateDate(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));*/

                        showDefalutDate = false;
                        date = etEquityChequeDate.getText().toString();
                    } else {
                        showDefalutDate = true;
                        date = "";
                    }
                    CustomDatePickerDialog dialog = new CustomDatePickerDialog(mContext, showDefalutDate, date, false, new DatePickerListener() {
                        @Override
                        public void submitDatePicker(String strDate) {
                            if (!strDate.equals("")) {
                                Calendar newDate = Calendar.getInstance();
                                Date dob = EkycAppUtil.getDate(strDate, "dd/MM/yyyy");
                                newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                                Date date = newDate.getTime();
                                // current date after 3 months
                                Calendar currentDateAfter2Months = Calendar.getInstance();
                                currentDateAfter2Months.add(Calendar.MONTH, 2);

                                // current date before 3 months
                                Calendar currentDateBefore3Months = Calendar.getInstance();
                                currentDateBefore3Months.add(Calendar.MONTH, -3);
                                Log.d("\n\ncurrentDate : ", "" + Calendar.getInstance().getTime());

                                Log.d("curDtAfter2Months : ", "" + dateFormatter.format(currentDateAfter2Months.getTime()));
                                Log.d("currDateBef3Months : ", "" + dateFormatter.format(currentDateBefore3Months.getTime()));
                                Log.d("dateToValidate : ", dateFormatter.format(newDate.getTime()));

                                if (date.before(currentDateAfter2Months.getTime())
                                        && date.after(currentDateBefore3Months.getTime())) {
                                    if (eqCheqClicked) {
                                        etEquityChequeDate.setText(dateFormatter.format(newDate.getTime()));
                                    } else {
                                        etCommodityChequeDate.setText(dateFormatter.format(newDate.getTime()));
                                    }
                                } else {
                                    commonMsgDialog(getActivity(), "Cheque date must be 3 months backdated or 2 month postdated", false, "");
                                }
                            }
                        }
                    });
                    dialog.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//                fromDatePickerDialog.show();
               /* MOSLCommon.showDatePickerDialog(getContext());
                etDOB.setText(MOSLCommon.date_1);*/
                eqCheqClicked = true;
                commCheqClicked = false;
                removeKeyboard(mContext, etEquityChequeDate);
            }
        });
        etEquityChequeDate.setInputType(InputType.TYPE_NULL);
        etCommodityChequeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!isBlank(etCommodityChequeDate.getText().toString())) {
                       /* Date dob = EkycAppUtil.getDate(etCommodityChequeDate.getText().toString(), "dd/MM/yyyy");
                        fromDatePickerDialog.updateDate(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));*/
                        showDefalutDate = false;
                        date = etCommodityChequeDate.getText().toString();
                    } else {
                        showDefalutDate = true;
                        date = "";
                    }
                    CustomDatePickerDialog dialog = new CustomDatePickerDialog(mContext, showDefalutDate, date, false, new DatePickerListener() {
                        @Override
                        public void submitDatePicker(String strDate) {
                            if (!strDate.equals("")) {
                                Calendar newDate = Calendar.getInstance();
                                Date dob = EkycAppUtil.getDate(strDate, "dd/MM/yyyy");
                                newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                                Date date = newDate.getTime();
                                // current date after 3 months
                                Calendar currentDateAfter2Months = Calendar.getInstance();
                                currentDateAfter2Months.add(Calendar.MONTH, 2);

                                // current date before 3 months
                                Calendar currentDateBefore3Months = Calendar.getInstance();
                                currentDateBefore3Months.add(Calendar.MONTH, -3);
                                Log.d("\n\ncurrentDate : ", "" + Calendar.getInstance().getTime());

                                Log.d("curDtAfter2Months : ", "" + dateFormatter.format(currentDateAfter2Months.getTime()));
                                Log.d("currDateBef3Months : ", "" + dateFormatter.format(currentDateBefore3Months.getTime()));
                                Log.d("dateToValidate : ", dateFormatter.format(newDate.getTime()));

                                if (date.before(currentDateAfter2Months.getTime())
                                        && date.after(currentDateBefore3Months.getTime())) {
                                    if (eqCheqClicked) {
                                        etEquityChequeDate.setText(dateFormatter.format(newDate.getTime()));
                                    } else {
                                        etCommodityChequeDate.setText(dateFormatter.format(newDate.getTime()));
                                    }
                                } else {
                                    commonMsgDialog(getActivity(), "Cheque date must be 3 months backdated or 2 month postdated", false, "");
                                }
                            }
                        }
                    });
                    dialog.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//                fromDatePickerDialog.show();
               /* MOSLCommon.showDatePickerDialog(getContext());
                etNWDate.setText(MOSLCommon.date_1);*/
                eqCheqClicked = false;
                commCheqClicked = true;
                removeKeyboard(mContext, etCommodityChequeDate);
            }
        });
        etCommodityChequeDate.setInputType(InputType.TYPE_NULL);

        btnEquPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equMakePayment();
                equMakePaymentButton = true;
                /*if (isCommodityOriginal()) {
                    commMakePayment();
                    commMakePaymentButton = true;
                }*/
            }
        });

        btnCommPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commMakePayment();
                commMakePaymentButton = true;
            }
        });
        btnEquSkipPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipEquPayment();
                equMakePaymentButton = true;
                if (isCommodityOriginal()) {
                    skipCommPayment();
                    commMakePaymentButton = true;
                }
            }
        });
        btnCommSkipPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipCommPayment();
                commMakePaymentButton = true;
            }
        });

        btnEquNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabPageIndicator.enableTab(getUploadTab());
                viewPager.setCurrentItem(getUploadTab(), false);
            }
        });

        btnCommNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabPageIndicator.enableTab(getUploadTab());
                viewPager.setCurrentItem(getUploadTab(), false);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        if (!getInterestedInMF() && viewPager.getCurrentItem() == getBankTab()) {
            getPaymentStatus();
        }
    }


    public void getPaymentStatus() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("_userId", userID);

            String flag = GETPAYMENTSTATUS;
            String category = FUNDTRANSFER;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
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

                                        if (object.has("EQPAYMENTSTATUS") && object.has("COMMPAYMENTSTATUS")) {
                                            eqPaymentStatus = (object.isNull("EQPAYMENTSTATUS") || object.getString("EQPAYMENTSTATUS").equals("")) ? "" : object.getString("EQPAYMENTSTATUS");
                                            commPaymentStatus = (object.isNull("COMMPAYMENTSTATUS") || object.getString("COMMPAYMENTSTATUS").equals("")) ? "" : object.getString("COMMPAYMENTSTATUS");
                                            if (!eqPaymentStatus.equals("") && !commPaymentStatus.equals("")) {
                                                bdEqPaymentStatus = new BigDecimal(eqPaymentStatus);
                                                bdCommPaymentStatus = new BigDecimal(commPaymentStatus);
                                                // 4 set for no segment selected
                                              /*  setIsEquityFundTransfered(true);
                                                setIsCommodityFundTransfered(true);*/
//                                                showView((int) bdEqPaymentStatus.longValue(), (int) bdCommPaymentStatus.longValue());
                                                getUserpaymentDetail((int) bdEqPaymentStatus.longValue(), (int) bdCommPaymentStatus.longValue());
                                            } else {
                                                MOSLCommon.showToastMsg(mContext, "Status is null");
                                            }

                                        } else if (object.has("EQPAYMENTSTATUS")) {
                                            eqPaymentStatus = (object.isNull("EQPAYMENTSTATUS") || object.getString("EQPAYMENTSTATUS").equals("")) ? "" : object.getString("EQPAYMENTSTATUS");
                                            if (!eqPaymentStatus.equals("")) {
                                                bdEqPaymentStatus = new BigDecimal(eqPaymentStatus);
                                                // 4 set for no segment selected
                                                // setIsEquityFundTransfered(true);
//                                                showView((int) bdEqPaymentStatus.longValue(), 4);
                                                getUserpaymentDetail((int) bdEqPaymentStatus.longValue(), 4);
                                            }
                                        } else if (object.has("COMMPAYMENTSTATUS")) {
                                            commPaymentStatus = (object.isNull("COMMPAYMENTSTATUS") || object.getString("COMMPAYMENTSTATUS").equals("")) ? "" : object.getString("COMMPAYMENTSTATUS");
                                            if (!commPaymentStatus.equals("")) {
                                                bdCommPaymentStatus = new BigDecimal(commPaymentStatus);
                                                // 4 set for no segment selected
                                                // setIsCommodityFundTransfered(true);
//                                                showView(4, (int) bdCommPaymentStatus.longValue());
                                                getUserpaymentDetail(4, (int) bdCommPaymentStatus.longValue());
                                            }
                                        }
//                                        getUserpaymentDetail();
                                        hideProgress();
                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }

                    } else {
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
//                        MOSLCommon.showToastMsg(getContext(), NO_RESPONSE);
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
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }
            });

        } else {
            MOSLCommon.alertDInternet(getContext());
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
                                    String chequeStatus = "", chequeMessage = "";
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        String bankName = (object.isNull("BANK_NAME") || object.getString("BANK_NAME").equals("")) ? "" : object.getString("BANK_NAME");
                                        String bankAccNo = (object.isNull("BANK_ACC_NO") || object.getString("BANK_ACC_NO").equals("")) ? "" : object.getString("BANK_ACC_NO");
                                        String chequeNo = (object.isNull("CHEQUENO") || object.getString("CHEQUENO").equals("")) ? "" : object.getString("CHEQUENO");
                                        String chequeDate = (object.isNull("CHEQUEDATE") || object.getString("CHEQUEDATE").equals("")) ? "" : object.getString("CHEQUEDATE");
                                        String marginDeposit = (object.isNull("MARGIN_DEPOSIT") || object.getString("MARGIN_DEPOSIT").equals("")) ? "" : object.getString("MARGIN_DEPOSIT");
                                        String product = (object.isNull("PRODUCT") || object.getString("PRODUCT").equals("")) ? "" : object.getString("PRODUCT");
                                        String amtRequested = (object.isNull("AMOUNTREQUESTED") || object.getString("AMOUNTREQUESTED").equals("")) ? "" : object.getString("AMOUNTREQUESTED");
                                        String fundTransferThrough = (object.isNull("FUNDTRANSFERTHROUGH") || object.getString("FUNDTRANSFERTHROUGH").equals("")) ? "" : object.getString("FUNDTRANSFERTHROUGH");
                                        String fundTransferThroughId = (object.isNull("FUNDTRANSFERTHROUGHID") || object.getString("FUNDTRANSFERTHROUGHID").equals("")) ? "" : object.getString("FUNDTRANSFERTHROUGHID");
                                        chequeStatus =
                                            (object.isNull("CHEQUE_STATUS")
                                                || object.getString("CHEQUE_STATUS")
                                                .equals("")) ? ""
                                                : object.getString("CHEQUE_STATUS");
                                        chequeMessage =
                                            (object.isNull("CHEQUE_MESSAGE")
                                                || object.getString("CHEQUE_MESSAGE")
                                                .equals("")) ? ""
                                                : object.getString("CHEQUE_MESSAGE");
                                        String auStatus =
                                            (object.isNull("AU_STATUS")
                                                || object.getString("AU_STATUS")
                                                .equals("")) ? ""
                                                : object.getString("AU_STATUS");

                                        auSmall = auStatus.equals("1");
                                        /*
                                        *  fundTransferThroughId 1 fundTransferThrough Net Banking
                                        *  fundTransferThroughId 2 fundTransferThrough Debit Card
                                        *  fundTransferThroughId 3 fundTransferThrough Cheque
                                        *  fundTransferThroughId 4 fundTransferThrough Skipped Payment
                                        *  fundTransferThroughId 0 fundTransferThrough Payment Pending
                                        */
                                        if (product.equalsIgnoreCase(EQUITY)) {
                                            eqPaymentMode = fundTransferThrough;
                                            eqPaymentId = Integer.parseInt(fundTransferThroughId);
                                            etEquBankName.setText(bankName);
                                            etEquAccno.setText(bankAccNo);
                                            if (getAppType().equals(PP)) {
                                                if (amtRequested.equalsIgnoreCase("0")) {
                                                    etEquFundTransferred.setText("");
                                                } else {
                                                    etEquFundTransferred.setText(amtRequested);
                                                }
                                            } else {
                                                etEquFundTransferred.setText(marginDeposit);
                                            }
                                            etEquCheqNo.setText(chequeNo);
                                            Date date = EkycAppUtil.getDate(chequeDate, "yyyy-MM-dd");
                                            etEquityChequeDate.setText(EkycAppUtil.getDate(date, "dd/MM/yyyy"));
//                                            etEquityChequeDate.setText(chequeDate);
                                        } else if (product.equalsIgnoreCase(COMMODITY)) {
                                            commPaymentMode = fundTransferThrough;
                                            commPaymentId = Integer.parseInt(fundTransferThroughId);
                                            etcommBankName.setText(bankName);
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
                                            etCommCheqNo.setText(chequeNo);
                                            Date date = EkycAppUtil.getDate(chequeDate, "yyyy-MM-dd");
                                            etCommodityChequeDate.setText(EkycAppUtil.getDate(date, "dd/MM/yyyy"));
                                        }
                                    }
                                    if (Integer.parseInt(chequeStatus) == 1) {
                                        showView(eqPaymentStatus, commPaymentStatus);
                                    } else {
                                        if (getIsCommoditySelected()
                                            && !getIsEquitySelected()) {
                                            llCommFundOption.setVisibility(View.GONE);
                                            llCommPaymentButton.setVisibility(View.GONE);
                                            tvCommFundTransfered.setVisibility(View.VISIBLE);
                                            tvCommFundTransfered.setText(chequeMessage);
                                        } else {
                                            llEquFundOption.setVisibility(View.GONE);
                                            llEquPaymentButton.setVisibility(View.GONE);
                                            tvEquFundTransfered.setVisibility(View.VISIBLE);
                                            tvEquFundTransfered.setText(chequeMessage);
                                        }
                                    }
                                    hideProgress();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }

                    } else {
//                        MOSLCommon.showToastMsg(getContext(), NO_RESPONSE);

                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
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
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }
            });

        } else {
            MOSLCommon.alertDInternet(getContext());
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
        ////////////////////////////////////
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

    private void noEquitySegment() {
        llEquityCheque.setVisibility(View.GONE);
    }

    private void noCommSegment() {
        llCommodityCheque.setVisibility(View.GONE);
    }

    private void showEquitypending() {
        llEquFundOption.setVisibility(View.VISIBLE);
        tvEquFundTransfered.setVisibility(View.GONE);
//        etEquBankName.setText(strEquBankName);
//        etEquAccno.setText(strEquAccNo);
//        etEquFundTransferred.setText(equTotalAmount);

    }

    private void showCommPending() {
        llCommFundOption.setVisibility(View.VISIBLE);
        tvCommFundTransfered.setVisibility(View.GONE);
//        etcommBankName.setText(strCommBankName);
//        etCommAccNo.setText(strCommAccNo);
//        etCommFundTransferred.setText(commTotalAmount);

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
        if (eqPaymentId == 3) {
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
        if (commPaymentId == 3) {
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

    private void skipEquPayment() {

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("_userId", userID);
            inputMap.put("_segmentType", "Equity");

            String flag = UPDATESKIPPAYMENT;
            String category = FUNDTRANSFER;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null && response.length() != 0) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");
                            if (jsonArray.length() != 0) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String status = (object.isNull("STATUS") || object.getString("STATUS").equals("")) ? "" : object.getString("STATUS");
                                    if (Integer.valueOf(status) == 1) {
                                        getPaymentStatus();
                                    } else {
                                        informationDialog(COULD_NOT_SKIP_PAY);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
                    } else {
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
//                        MOSLCommon.showToastMsg(getContext(), NO_RESPONSE);
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
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }
            });

        } else {
            MOSLCommon.alertDInternet(getContext());
            customProgress.setVisibility(View.GONE);
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
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null && response.length() != 0) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");
                            if (jsonArray.length() != 0) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String status = (object.isNull("STATUS") || object.getString("STATUS").equals("")) ? "" : object.getString("STATUS");
                                    if (Integer.valueOf(status) == 1) {
                                        getPaymentStatus();
                                    } else {
                                        informationDialog(COULD_NOT_SKIP_PAY);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
                    } else {
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        //                        MOSLCommon.showToastMsg(getContext(), NO_RESPONSE);
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
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }
            });

        } else {
            MOSLCommon.alertDInternet(getContext());
            customProgress.setVisibility(View.GONE);
        }

    }

    public void equMakePayment() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            String strEquBankName = etEquBankName.getText().toString();
            String strEquCheqNo = etEquCheqNo.getText().toString();
            String strEquAccno = etEquAccno.getText().toString();
            String strEquityChequeDate = etEquityChequeDate.getText().toString();
            String strEquFundAmount = etEquFundTransferred.getText().toString();
            if (!validateEditText(etEquCheqNo, input_layout_equCheqNo, ENTER_CHEQUE_NO)) {
                etEquCheqNoError.setVisibility(View.GONE);
                return;
            } else if (etEquCheqNo.length() < 6) {
//                input_layout_equCheqNo.setError("Invalid cheque no.");
                etEquCheqNoError.setText("Invalid cheque no.");
                etEquCheqNoError.setVisibility(View.VISIBLE);
                etEquCheqNo.requestFocus();
            } else if (!validateEditText(etEquFundTransferred, input_layout_equFundTransferred, "Enter Amount")) {
                etEquCheqNoError.setVisibility(View.GONE);
                return;
            } else if (!(Integer.parseInt(strEquFundAmount) > 0)) {
                etEquCheqNoError.setVisibility(View.GONE);
                etEquFundTransferred.requestFocus();
                //  validateEditText(etEquFundTransferred, input_layout_equFundTransferred,);
            } else if (!getAppType().equalsIgnoreCase(PP)
                && !auSmall
                && etEquFundTransferred.length() < 5) {
                etEquFundTransferredError.setText(getResources().getString(R.string.fund_transfer_error_msg));
                etEquFundTransferredError.setVisibility(View.VISIBLE);
                etEquFundTransferred.requestFocus();
            } else {
                etEquCheqNoError.setVisibility(View.GONE);
                Map inputMap = new HashMap();

                inputMap.put("_userId", userID);
                inputMap.put("_chequeNo", strEquCheqNo);
                Date chequeDate = EkycAppUtil.getDate(strEquityChequeDate, "dd/MM/yyyy");
                inputMap.put("_chequeDate", String.valueOf(EkycAppUtil.getDateStr(chequeDate, "yyyy-MM-dd")));
                inputMap.put("_chequeAmount", etEquFundTransferred.getText().toString());
                inputMap.put("_segmentType", "Equity");

                String flag = UPDATECHEQUEPAYMENT;
                String category = FUNDTRANSFER;
                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {

                            try {
                                if (response.has("FUNDTRANSFER")) {
                                    JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");
                                    if (jsonArray.length() != 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            String status = (object.isNull("STATUS") || object.getString("STATUS").equals("")) ? "" : object.getString("STATUS");
                                            if (Integer.valueOf(status) == 1) {
                                                getPaymentStatus();
                                            } else {
                                                informationDialog(COULD_NOT_SKIP_PAY);
                                            }
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                            }
                        } else {
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                            //MOSLCommon.showToastMsg(getContext(), NO_RESPONSE);
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
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }
                });
            }

        } else {
            MOSLCommon.alertDInternet(getContext());
            customProgress.setVisibility(View.GONE);
        }
    }

    public void commMakePayment() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            String strCommFundAmount = etCommFundTransferred.getText().toString();
            //String strCommBankName = etcommBankName.getText().toString();
            String strCommCheqNo = etCommCheqNo.getText().toString();
            //String strCommAccno = etCommAccNo.getText().toString();
            String strCommChequeDate = etCommodityChequeDate.getText().toString();
            /*if (isEquityOriginal()) {
                strCommFundAmount = etEquFundTransferred.getText().toString();
                strCommCheqNo = etEquCheqNo.getText().toString();
                strCommChequeDate = etEquityChequeDate.getText().toString();
            }*/

            /*if (isEquityOriginal() && !validateEditText(etEquCheqNo, input_layout_equCheqNo, ENTER_CHEQUE_NO)) {
                etEquCheqNoError.setVisibility(View.GONE);
                return;
            } else if (isEquityOriginal() && etEquCheqNo.length() < 6) {
//                input_layout_equCheqNo.setError("Invalid cheque no.");
                etEquCheqNoError.setText("Invalid cheque no.");
                etEquCheqNoError.setVisibility(View.VISIBLE);
                etEquCheqNo.requestFocus();
            } else if (isEquityOriginal() && !validateEditText(etEquFundTransferred, input_layout_equFundTransferred, "Enter Amount")) {
                etEquCheqNoError.setVisibility(View.GONE);
                return;
            } else if (isEquityOriginal() && !(Integer.parseInt(strCommFundAmount) > 0)) {
                etEquCheqNoError.setVisibility(View.GONE);
                etEquFundTransferred.requestFocus();
                //  validateEditText(etEquFundTransferred, input_layout_equFundTransferred,);
            } else if (isEquityOriginal() && !getAppType().equalsIgnoreCase(PP) && etEquFundTransferred.length() < 5) {
                etEquFundTransferredError.setText(getResources().getString(R.string.fund_transfer_error_msg));
                etEquFundTransferredError.setVisibility(View.VISIBLE);
                etEquFundTransferred.requestFocus();
            } else*/ if (!validateEditText(etCommCheqNo, input_layout_commCheqNo, ENTER_CHEQUE_NO)) {
                etCommCheqNoError.setVisibility(View.GONE);
                return;
            } else if (etCommCheqNo.length() < 6) {
//                input_layout_commCheqNo.setError("Invalid cheque no.");
                etCommCheqNoError.setText("Invalid cheque no.");
                etCommCheqNoError.setVisibility(View.VISIBLE);
                etCommCheqNo.requestFocus();
            } else if (!validateEditText(etCommFundTransferred, input_layout_commFundTransferred, "Enter Amount")) {
                etCommCheqNoError.setVisibility(View.GONE);
                return;
            } else if (!(Integer.parseInt(strCommFundAmount) > 0)) {
                etCommCheqNoError.setVisibility(View.GONE);
                etCommFundTransferred.requestFocus();
                //  validateEditText(etEquFundTransferred, input_layout_equFundTransferred,);
            } else if (!getAppType().equalsIgnoreCase(PP)
                && !auSmall
                && etCommFundTransferred.length() < 5) {
                etCommFundTransferredError.setText(getResources().getString(R.string.fund_transfer_error_msg));
                etCommFundTransferredError.setVisibility(View.VISIBLE);
                etCommFundTransferred.requestFocus();
            } else {
//                etEquCheqNoError.setVisibility(View.GONE);
                etCommCheqNoError.setVisibility(View.GONE);
                Map inputMap = new HashMap();


                inputMap.put("_userId", userID);
                inputMap.put("_chequeNo", strCommCheqNo);
                Date chequeDate = EkycAppUtil.getDate(strCommChequeDate, "dd/MM/yyyy");
                inputMap.put("_chequeDate", String.valueOf(EkycAppUtil.getDateStr(chequeDate, "yyyy-MM-dd")));
                /*if (isEquityOriginal())
                    inputMap.put("_chequeAmount", etEquFundTransferred.getText().toString());
                else*/
                    inputMap.put("_chequeAmount", etCommFundTransferred.getText().toString());
                inputMap.put("_segmentType", "Commodity");

                String flag = UPDATECHEQUEPAYMENT;
                String category = FUNDTRANSFER;
                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {

                            try {
                                if (response.has("FUNDTRANSFER")) {
                                    JSONArray jsonArray = response.getJSONArray("FUNDTRANSFER");
                                    if (jsonArray.length() != 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            String status = (object.isNull("STATUS") || object.getString("STATUS").equals("")) ? "" : object.getString("STATUS");
                                            if (Integer.valueOf(status) == 1) {
                                                getPaymentStatus();
                                            } else {
                                                informationDialog(COULD_NOT_SKIP_PAY);
                                            }
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                            }
                        } else {
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            //                            MOSLCommon.showToastMsg(getContext(), NO_RESPONSE);
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
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }
                });
            }

        } else {
            MOSLCommon.alertDInternet(getContext());
            customProgress.setVisibility(View.GONE);
        }
    }

    public void informationDialog(String msg) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);


        tvMsg.setText(msg);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgress.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                customProgress.setVisibility(View.GONE);

            }
        });

        dialog.show();
    }

    private boolean validateEditText(EditText edietText, TextInputLayout textInputLayout, String error) {
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

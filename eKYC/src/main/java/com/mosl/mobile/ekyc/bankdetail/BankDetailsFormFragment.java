package com.mosl.mobile.ekyc.bankdetail;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.adapter.ReviewEditCommoditySchemeAdapter;
import com.mosl.mobile.ekyc.adapter.ReviewEditEquitySchemeAdapter;
import com.mosl.mobile.ekyc.common.BankDetailModel;
import com.mosl.mobile.ekyc.common.CustomFieldCommodity;
import com.mosl.mobile.ekyc.common.CustomFieldEquity;
import com.mosl.mobile.ekyc.common.CustomViewPager;
import com.mosl.mobile.ekyc.common.EkycAppUtil;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.common.SearchableSpinner;
import com.mosl.mobile.ekyc.common.ValidationRule;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.model.BankBranchModel;
import com.mosl.mobile.ekyc.model.CommonModel;
import com.mosl.mobile.ekyc.model.RowData;
import com.mosl.mobile.ekyc.model.SchemeDetailsModel;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.CustomDialog;
import com.viewpagerindicator.TabPageIndicator;
import cz.msebera.android.httpclient.Header;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;

import static com.mosl.mobile.ekyc.R.id.rbBankSelect;
import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.disableField;
import static com.mosl.mobile.ekyc.common.MOSLCommon.enableField;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isIpvORDocumentStageDone;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isNomineeTabApplicable;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isThisSecondProductFlow;
import static com.mosl.mobile.ekyc.common.MOSLCommon.minMarginValueForOtherAmount;
import static com.mosl.mobile.ekyc.common.MOSLCommon.removeKeyboard;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setListViewHeightBasedOnChildren;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getBankTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getComMarginDeposit;
import static com.mosl.mobile.ekyc.model.CommonModel.getComSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getComSchemeName;
import static com.mosl.mobile.ekyc.model.CommonModel.getCommDelNCXSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getCommDelSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getCommFutNCXSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getCommFutSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getCommOptNCXSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getCommOptSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getDematTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getEqSchemeId;
import static com.mosl.mobile.ekyc.model.CommonModel.getEquCurrFutSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getEquCurrOptSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getEquDPSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getEquDelSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getEquFutSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getEquMarginDeposit;
import static com.mosl.mobile.ekyc.model.CommonModel.getEquOptSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getEquTradeSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getPersonalTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getUploadTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserType;
import static com.mosl.mobile.ekyc.model.CommonModel.isAllOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommoditySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isCurrencyOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isDerivativeOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isDirectProspect;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquitySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isFundTransferDone;
import static com.mosl.mobile.ekyc.model.CommonModel.isGotoFundTransfer;
import static com.mosl.mobile.ekyc.model.CommonModel.isGotoReviewEdit;
import static com.mosl.mobile.ekyc.model.CommonModel.isMfIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.isOfflineDocumentUploadedDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setEquCurrFutSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.setEquCurrOptSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.setEquDPSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.setEquDelSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.setEquFutSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.setEquOptSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.setEquTradeSchSelID;
import static com.mosl.mobile.ekyc.model.CommonModel.setGotoReviewEdit;
import static com.mosl.mobile.ekyc.model.RowData.getSpinnerRowDataById;
import static com.mosl.mobile.ekyc.ui.EkycMainActivity.customProgress;

/**
 * Created by Shefali on 28/08/16.
 */
public class BankDetailsFormFragment extends Fragment
    implements TextWatcher, MOSLConstants, View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {
    boolean loadData = true;
    View bankDetailFormView;
    public static String strEquMarginDep, strCommMarginDep, strEquBankName, strCommBankName,
        strEquAccNo, strCommAccNo;
    SearchableSpinner spCommFuture, spCommOption, spCommDPAMC, spNCXFuture, spNCXOption, spNCXDPAMC;
    private final AdapterView.OnItemSelectedListener dpSchemeListener =
        new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strDpSchName = arrDpScheme.get(i).getValue();
                strDpSchID = arrDpScheme.get(i).getKey();
                etDpSchemeError.setVisibility(View.GONE);
                if (!strDpSchID.equalsIgnoreCase("-1")) {
                    tvEquAccOpenNote.setText("DP-AMC " + strDpSchName);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    private final AdapterView.OnTouchListener equBrokerageTouchListener =
        new AdapterView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                setEquTradeSchSelID("");
                setEquDelSchSelID("");
                setEquFutSchSelID("");
                setEquOptSchSelID("");
                setEquDPSchSelID("");
                setEquCurrFutSchSelID("");
                setEquCurrOptSchSelID("");

                return false;
            }
        };
    ArrayList<String> arrListAccType;
    private final AdapterView.OnTouchListener commMCXBrokTouchListener =
        new AdapterView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                CommonModel.setCommFutSchSelID("");
                CommonModel.setCommOptSchSelID("");
                CommonModel.setCommDelSchSelID("");

                return false;
            }
        };
    private final AdapterView.OnTouchListener commNCXBrokTouchListener =
        new AdapterView.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                CommonModel.setCommFutNCXSchSelID("");
                CommonModel.setCommOptNCXSchSelID("");
                CommonModel.setCommDelNCXSchSelID("");

                return false;
            }
        };
    private final AdapterView.OnItemSelectedListener equTradListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strTradeProName = ((RowData) arrTradingEQ.get(position)).getValue();
                strTradeProID = ((RowData) arrTradingEQ.get(position)).getKey();

                if (strTradeProName != null && !strTradeProName.equals("")) {
                    RowData rdEquTradeScheme =
                        RowData.getSpinnerRowDataByValue(strTradeProName, arrAdapTradingEQ);
                    if (rdEquTradeScheme != null) {
                        spEquTrading.setSelection(rdEquTradeScheme.getPosition());
                    }
                }
                tvTradeSummary.setText(spEquTrading.getSelectedItem().toString());
                etEquTradingError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private final AdapterView.OnItemSelectedListener equDeliveryListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strDelProID = ((RowData) arrDeliveryEQ.get(position)).getKey();
                strDelProName = ((RowData) arrDeliveryEQ.get(position)).getValue();


                if (strDelProName != null && !strDelProName.equals("")) {
                    RowData rdEquDelScheme =
                        RowData.getSpinnerRowDataByValue(strDelProName, arrAdapDeliveryEQ);
                    if (rdEquDelScheme != null) {
                        spEquDelivery.setSelection(rdEquDelScheme.getPosition());
                    }
                }
                tvDelSummary.setText(spEquDelivery.getSelectedItem().toString());
                etEquDeliveryError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    List<BankBranchModel> branchModelList;
    BankBranchAdapter bankBranchAdapter;
    private final AdapterView.OnItemSelectedListener equFutureListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strFutProID = ((RowData) arrFuturesEQ.get(position)).getKey();
                strFutProName = ((RowData) arrFuturesEQ.get(position)).getValue();
            /*String strFutSelID = ((RowData) arrFuturesEQ.get(position)).getSchemeSelID();
            String strFutSchID = ((RowData) arrFuturesEQ.get(position)).getSchmeID();
            if (isEQFutSchIDSel) {
//                spEquFuture.setSelection(arrFuturesEQ.get(position).getPosition());
                spinnerSchemeSelect(strFutSelID, arrAdapFuturesEQ, spEquFuture);
                isEQFutSchIDSel = false;
            } else {
                spinnerSchemeSelect(strFutSchID, arrAdapFuturesEQ, spEquFuture);
            }*/

                if (strFutProName != null && !strFutProName.equals("")) {
                    RowData rdEquFutScheme =
                        RowData.getSpinnerRowDataByValue(strFutProName, arrAdapFuturesEQ);
                    if (rdEquFutScheme != null) {
                        spEquFuture.setSelection(rdEquFutScheme.getPosition());
                    }
                }
                tvFutureSummary.setText(spEquFuture.getSelectedItem().toString());
                etEquFutureError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    List<CustomFieldEquity> customFieldList;
    List<CustomFieldCommodity> customFieldCommList;
    TextView btnBankBack, tvBankDetailTitle;
    LinearLayout llBankBack, llMfBankDetails, llEquBankDetail, llCommBankDetails;
    private final AdapterView.OnItemSelectedListener equOptionListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strOptProID = ((RowData) arrOptionsEQ.get(position)).getKey();
                strOptProName = ((RowData) arrOptionsEQ.get(position)).getValue();

            /*String strOptSelID = ((RowData) arrOptionsEQ.get(position)).getSchemeSelID();
            String strOptID = ((RowData) arrOptionsEQ.get(position)).getSchmeID();
            if (isEQOptSchIDSel) {
                spinnerSchemeSelect(strOptSelID, arrAdapOptionsEQ, spEquOption);
                isEQOptSchIDSel = false;
            } else {
                spinnerSchemeSelect(strOptID, arrAdapOptionsEQ, spEquOption);
            }*/
                if (strOptProName != null && !strOptProName.equals("")) {
                    RowData rdEquOptScheme =
                        RowData.getSpinnerRowDataByValue(strOptProName, arrAdapOptionsEQ);
                    if (rdEquOptScheme != null) {
                        spEquOption.setSelection(rdEquOptScheme.getPosition());
                    }
                }

                tvOptionsSummary.setText(spEquOption.getSelectedItem().toString());
                etEquOptionError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    //    ArrayList<String> arrListBrokerage;
    LinearLayout llBACommodityBankScheme, llBAEquityBankScheme, llSelectedScheme, llMarginDeposit;
    FundTransferFragment fragFundTransfer;
    Context mContext;
    List<SchemeDetailsModel> modelList;
    List<SchemeDetailsModel> currencyOptionmodelList;
    private final AdapterView.OnItemSelectedListener equDPEQListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strDPProID = ((RowData) arrDPEQ.get(position)).getKey();
                strDPProName = ((RowData) arrDPEQ.get(position)).getValue();
            /*String strDPSelID = ((RowData) arrDPEQ.get(position)).getSchemeSelID();
            String strDPID = ((RowData) arrDPEQ.get(position)).getSchmeID();
            if (isEQDPSchIDSel) {
                spinnerSchemeSelect(strDPSelID, arrAdapDPEQ, spEquDPAMC);
                isEQDPSchIDSel = false;
            } else {
                spinnerSchemeSelect(strDPID, arrAdapDPEQ, spEquDPAMC);
            }*/

                if (strDPProName != null && !strDPProName.equals("")) {
                    RowData rdEquFutScheme =
                        RowData.getSpinnerRowDataByValue(strDPProName, arrAdapDPEQ);
                    if (rdEquFutScheme != null) {
                        spEquDPAMC.setSelection(rdEquFutScheme.getPosition());
                    }
                }

                tvDPSummary.setText(spEquDPAMC.getSelectedItem().toString());
                etEquDPAMCError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private final AdapterView.OnItemSelectedListener equCurrencyFutureListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCurrFutProID = ((RowData) arrCurrencyFuturesEQ.get(position)).getKey();
                strCurrFutProName = ((RowData) arrCurrencyFuturesEQ.get(position)).getValue();
                tvCurrencyFutureSummary.setText(spEquCurrencyFuture.getSelectedItem().toString());

            /*String strCurrFutSelID = ((RowData) arrCurrencyFuturesEQ.get(position)).getSchemeSelID();
            String strCurrFutID = ((RowData) arrCurrencyFuturesEQ.get(position)).getSchmeID();
            if (isEQCurFutSchIDSel) {
//                spEquCurrencyFuture.setSelection(arrCurrencyFuturesEQ.get(position).getPosition());
                spinnerSchemeSelect(strCurrFutSelID, arrAdapCurrencyFuturesEQ, spEquCurrencyFuture);

            } else {
                spinnerSchemeSelect(strCurrFutID, arrAdapCurrencyFuturesEQ, spEquCurrencyFuture);
            }*/
                if (strCurrFutProName != null && !strCurrFutProName.equals("")) {
                    RowData rdEquCurrFutScheme = RowData.getSpinnerRowDataByValue(strCurrFutProName,
                        arrAdapCurrencyFuturesEQ);
                    if (rdEquCurrFutScheme != null) {
                        spEquCurrencyFuture.setSelection(rdEquCurrFutScheme.getPosition());
                    }
                }
                etEquCurrencyFutureError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    boolean reviewDialogIsShowing = false;
    Dialog dialog, dialogReview, dialogValuePackScheme, dialogMarginScheme, dialogCommScheme;
    private final AdapterView.OnItemSelectedListener equCurrencyOptionsListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strCurrOptProID = ((RowData) arrCurrencyOptionsEQ.get(position)).getKey();
                strCurrOptProName = ((RowData) arrCurrencyOptionsEQ.get(position)).getValue();
                tvCurrencyOptionSummary.setText(spEquCurrencyOption.getSelectedItem().toString());

           /* String strCurrOptID = ((RowData) arrCurrencyOptionsEQ.get(position)).getSchmeID();
            String strCurrOptSelID = ((RowData) arrCurrencyOptionsEQ.get(position)).getSchemeSelID();
            if (isEQCurOptSchIDSel) {
//                spEquCurrencyOption.setSelection(arrCurrencyOptionsEQ.get(position).getPosition());
                spinnerSchemeSelect(strCurrOptSelID, arrAdapCurrencyOptionsEQ, spEquCurrencyOption);
            } else {
                spinnerSchemeSelect(strCurrOptID, arrAdapCurrencyOptionsEQ, spEquCurrencyOption);
            }*/
                if (strCurrOptProName != null && !strCurrOptProName.equals("")) {
                    RowData rdEquCurrOptScheme = RowData.getSpinnerRowDataByValue(strCurrOptProName,
                        arrAdapCurrencyOptionsEQ);
                    if (rdEquCurrOptScheme != null) {
                        spEquCurrencyOption.setSelection(rdEquCurrOptScheme.getPosition());
                    }
                }
                etEquCurrencyOptionError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private final AdapterView.OnItemSelectedListener commSelectBankItemListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCommSelectBankStr = spCommSelectBank.getSelectedItem().toString();
                etCommSelectBankError.setVisibility(View.GONE);
                // etCommMICR.setText("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    LinearLayout llequSchInfoFutures1stLeg, llequSchInfoFutures2ndLeg, llequSchInfoOptions,
        llequSchInfoCurrFuture, llequSchInfoCurrOption,
        llAdminCharges, llBrokingAmc;

    //Button btnUpdateScheme;
    Button btnBankAndSchemeUpdate;
    private final AdapterView.OnItemSelectedListener commFutListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                strFutProfIdCOMM = ((RowData) arrFuturesCOMM.get(position)).getKey();
                strFutProfNameCOMM = ((RowData) arrFuturesCOMM.get(position)).getValue();
                if (strFutProfNameCOMM != null && !strFutProfNameCOMM.equals("")) {
                    RowData rdCommFutScheme =
                        RowData.getSpinnerRowDataByValue(strFutProfNameCOMM, arrAdapFuturesCOMM);
                    if (rdCommFutScheme != null) {
                        spCommFuture.setSelection(rdCommFutScheme.getPosition());
                    }
                }
                tvBAMCXFutSummary.setText(spCommFuture.getSelectedItem().toString());
                etCommFutureError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    String strEquAccNumber = "", strEquMICR = "", strCommAccNumber = "", strCommMICR = "";
    String strcbEqu = "", strcbComm = "";
    private final AdapterView.OnItemSelectedListener commOptListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strOptProfIdCOMM = ((RowData) arrOptionsCOMM.get(position)).getKey();
                strOptProfNameCOMM = ((RowData) arrOptionsCOMM.get(position)).getValue();
                if (strOptProfNameCOMM != null && !strOptProfNameCOMM.equals("")) {
                    RowData rdCommOptScheme =
                        RowData.getSpinnerRowDataByValue(strOptProfNameCOMM, arrAdapOptionsCOMM);
                    if (rdCommOptScheme != null) {
                        spCommOption.setSelection(rdCommOptScheme.getPosition());
                    }
                }
                tvBAMCXOptSummary.setText(spCommOption.getSelectedItem().toString());
                etCommOptionError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private final AdapterView.OnItemSelectedListener commDelListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strDelCOMMProfIdCOMM = ((RowData) arrDeliveryCOMM.get(position)).getKey();
                strDelCOMMProfNameCOMM = ((RowData) arrDeliveryCOMM.get(position)).getValue();
                if (strDelCOMMProfNameCOMM != null && !strDelCOMMProfNameCOMM.equals("")) {
                    RowData rdCommDelScheme =
                        RowData.getSpinnerRowDataByValue(strDelCOMMProfNameCOMM,
                            arrAdapDeliveryCOMM);
                    if (rdCommDelScheme != null) {
                        spCommDPAMC.setSelection(rdCommDelScheme.getPosition());
                    }
                }

                tvBAMCXDPSummary.setText(spCommDPAMC.getSelectedItem().toString());
                etCommDPAMCError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    //Review and Edit
    LinearLayout llCustomProgress, llCountryCitizenship, llCountryBirth, llCountryTax;
    private final AdapterView.OnItemSelectedListener commNCXFutListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strNCXFutProfIdCOMM = ((RowData) arrFuturesNCXCOMM.get(position)).getKey();
                strNCXFutProfNameCOMM = ((RowData) arrFuturesNCXCOMM.get(position)).getValue();
                if (strNCXFutProfNameCOMM != null && !strNCXFutProfNameCOMM.equals("")) {
                    RowData rdCommNCXFutScheme =
                        RowData.getSpinnerRowDataByValue(strNCXFutProfNameCOMM,
                            arrAdapFuturesNCXCOMM);
                    if (rdCommNCXFutScheme != null) {
                        spNCXFuture.setSelection(rdCommNCXFutScheme.getPosition());
                    }
                }
                tvBANCXFutSummary.setText(spNCXFuture.getSelectedItem().toString());
                etNCXFutureError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private final AdapterView.OnItemSelectedListener commNCXOptListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strNCXOptProfIdCOMM = ((RowData) arrOptionsNCXCOMM.get(position)).getKey();
                strNCXOptProfNameCOMM = ((RowData) arrOptionsNCXCOMM.get(position)).getValue();
                if (strNCXOptProfNameCOMM != null && !strNCXOptProfNameCOMM.equals("")) {
                    RowData rdCommNCXOptScheme =
                        RowData.getSpinnerRowDataByValue(strNCXOptProfNameCOMM,
                            arrAdapOptionsNCXCOMM);
                    if (rdCommNCXOptScheme != null) {
                        spNCXOption.setSelection(rdCommNCXOptScheme.getPosition());
                    }
                }
                tvBANCXOptSummary.setText(spNCXOption.getSelectedItem().toString());
                etNCXOptionError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    //    TextView tvEqPhotoProofReview,tvCommPhotoProofReview,tvCommBankProofReview,tvEqBankProofReview;

    TextView tvNomineeDetailMsg, tvSecondHolderNotProvidedMsg, tvThirdHolderNotProvidedMsg;
    private final AdapterView.OnItemSelectedListener commNCXDelListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strNCXDelCOMMProfId = ((RowData) arrDeliveryNCXCOMM.get(position)).getKey();
                strNCXDelCOMMProfName = ((RowData) arrDeliveryNCXCOMM.get(position)).getValue();
                if (strNCXDelCOMMProfName != null && !strNCXDelCOMMProfName.equals("")) {
                    RowData rdCommNCXDelScheme =
                        RowData.getSpinnerRowDataByValue(strNCXDelCOMMProfName, arrAdapDelNCXCOMM);
                    if (rdCommNCXDelScheme != null) {
                        spNCXDPAMC.setSelection(rdCommNCXDelScheme.getPosition());
                    }
                }
                tvBANCXDPAMCSummary.setText(spNCXDPAMC.getSelectedItem().toString());
                etNCXDPAMCError.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private SimpleDateFormat dateFormatter;
    TextView tvequSchValidity, tvequSchDpAmc, tvequSchUpfrontFees;
    private final AdapterView.OnItemSelectedListener commNCXBrokSchListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvBANCXBrokSummary.setText(spNCXBrokerageScheme.getSelectedItem().toString());
                RowData selectedItem = (RowData) spNCXBrokerageScheme.getSelectedItem();
                String strBANCXCommBrokSch = selectedItem.getValue();
                String strSelectedID = selectedItem.getKey();

                etNCXBrokerageSchemeError.setVisibility(View.GONE);
                fillCommNCXBASchemeDetails(strSelectedID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    ImageView tvEditPersonalDetail, tvEditNomineeJointDetail, tvEditBankDetail;
    Button btnNext;
    //    Context mContext;
    SearchableSpinner spEquSelectBank, spCommSelectBank, spEquTrading, spEquDelivery, spEquFuture,
        spEquOption,
        spEquDPAMC, spEquCurrencyFuture, spEquCurrencyOption, spMFSelectBank;
    Spinner spEquScheme, spCommScheme, spEquBankProof, spEquPhotoProof, spCommBankProof,
        spCommPhotoProof;
    private final AdapterView.OnItemSelectedListener commSchemeListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                commSchMarDep = ((RowData) arrCommScheme.get(position)).getMarDep();
                strComSchID = ((RowData) arrCommScheme.get(position)).getSchmeID();
                strCommSchSelID = ((RowData) arrCommScheme.get(position)).getSchemeSelID();
                strCommSchKey = ((RowData) arrCommScheme.get(position)).getKey();
                etCommSchemeError.setVisibility(View.GONE);
                customProgress.setVisibility(View.GONE);

                setCommodityScheme();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    HashMap<String, String> map, eqMap, commMap, mfMap;
    CheckBox cbEquCurrent, cbEquSaving, cbCommCurrent, cbCommSaving, cbTC, cbSameAsEquity,
        cbSameAsMCX, cbMFCurrent, cbMFSaving, cbMFTC, cbMarginAcc, cbValuePacAcc;
    ReviewEditEquitySchemeAdapter reviewEditEqSchemeAdapter;
    ReviewEditCommoditySchemeAdapter reviewEditCommSchemeAdapter;
    MFBankListAdapter mfBankListAdapter;

    //    String selectedBankcbdID = "", selectedBankAccType = "";
    String selectedBankAccType = "";
    EditText etEquityMICR, etCommMICR, etEquMarginDeposit, etCommMarginDeposit, etEquityAccNumber,
        etCommAccNumber, etMFMICR, etMFAccNumber, etConfirmEquityAccNumber,
        etConfirmCommAccNumber, etConfirmMFAccNumber;
    String userID = getUserID();

    ListView lvEqSchemeDetailReview, lvCommSchemeDetailReview, lvMFBankDetail;
    CustomViewPager viewPager;
    TabPageIndicator tabPageIndicator;
    boolean isEquOtherSchSelected, isCommOtherSchSelected, isOtherAmountSubmitted = false;
    TextView tvEquSchemeTermCondition, tvCommSchemeTermCondition, tvMFTC,
        tvEquValuePackTermCondition;
    TextView tvEquityBankName, tvEquityBranchName, tvEquityMICR, tvEquityIFSC, tvEquityBankAddress,
        tvCommBankName, tvCommBranchName, tvCommMICR, tvCommIFSC, tvCommBankAddress, tvMFBankName,
        tvMFBranchName, tvMFMICR, tvMFIFSC, tvMFBankAddress, tvMFReviewForm, tvMFReviewMICR,
        tvMFReviewIFSC, tvMFReviewAccType, tvMFReviewBankName, tvMFReviewBranchName,
        tvMFReviewBankAddress, tvMFReviewAccNumber;
    Button btnEquityBankDetails, btnCommBankDetails, btnGetEquScheme, btnGetCommScheme,
        btnMFBankDetails, btnMFAddBankDetails, btnSelectedScheme, btnAllScheme, btnMarginAllScheme,
        btnCommMarginAllScheme, btnMarginSelectedScheme, btnCommMarginSelectedScheme;

    String bankStatus, bankMessage;
    boolean isGetSchemeCalled = false, isSameAsMCXCheck = false;
    RadioGroup currencyoptionRadiogroup, currencyfutureRadiogroup;
    private LinearLayout llSpinnerCommScheme; //Commodity scheme changes


   /* public void setContext(Context context) {
        this.mContext = context;
    }*/

    public void setReviewDialog(Dialog dialog) {
        this.dialogReview = dialog;
    }

    public void setViewPager(CustomViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setTabPageIndicator(TabPageIndicator tabPageIndicator) {
        this.tabPageIndicator = tabPageIndicator;
    }

    ScrollView svBankForm;
    LinearLayout llEquityBankDetail, llCommodityBankDetail, llEquityBankScheme,
        llCommodityBankScheme, llBranchCommodityBankScheme, llSchemeDetailValues,
        llBranchEquityBankScheme, llFundTransferNote, llFuture, llOption, llCurrencyFuture,
        llCurrencyOption, llCurrencyOptionSummary, llCurrencyFutureSummary, llOptionSummary,
        llFutureSummary;

    @Override
    public void onResume() {

        super.onResume();

        /*if (loadData) {
            showBankDetailForm();
        }*/
    }

    public void showBankDetailForm() {
        llFundTransferNote.setVisibility(View.VISIBLE);
        llFundTransfer.setVisibility(View.GONE);
        llBankDetailForm.setVisibility(View.VISIBLE);
        if (viewPager == null || viewPager.getCurrentItem() != getBankTab()) {
            return;
        }
        if (getInterestedInMF()) {
            showMFBankDetails();
            tvBankDetailTitle.setText("Bank Details");
            /*if (isGotoReviewEdit()) {
                getReviewDetail();
            }*/
        } else {
            tvBankDetailTitle.setText("Bank & Scheme Details");
            if (isGotoFundTransfer()) {
                getPaymentStatus();
            } else if (isGotoReviewEdit()) {
                if (dialogReview != null && !reviewDialogIsShowing) {
                    getReviewDetail();
                }
            } else if (isEquityOriginal() && isCommodityOriginal()) {
                showBothBankDetails();
                getSchemeDetails(getAppType(), userID);
            } else if (isEquityOriginal() || isDerivativeOriginal()) {
                showEquityBankDetails();
                getSchemeDetails(getAppType(), userID);
            } else if (isCommodityOriginal()) {
                showCommdityBankDetails();
                getSchemeDetails(getAppType(), userID);
            }
        }

        if (isDerivativeOriginal()) {
            llequSchInfoFutures1stLeg.setVisibility(View.VISIBLE);
            llequSchInfoFutures2ndLeg.setVisibility(View.VISIBLE);
            llequSchInfoOptions.setVisibility(View.VISIBLE);
            if (getAppType().equals(PP)) {
                llFuture.setVisibility(View.VISIBLE);
                llFutureSummary.setVisibility(View.VISIBLE);
                llOption.setVisibility(View.VISIBLE);
                llOptionSummary.setVisibility(View.VISIBLE);
            }
        } else {
            llequSchInfoFutures1stLeg.setVisibility(View.GONE);
            llequSchInfoFutures2ndLeg.setVisibility(View.GONE);
            llequSchInfoOptions.setVisibility(View.GONE);
            if (getAppType().equals(PP)) {
                llFuture.setVisibility(View.GONE);
                llFutureSummary.setVisibility(View.GONE);
                llOption.setVisibility(View.GONE);
                llOptionSummary.setVisibility(View.GONE);
            }
        }

        if (isCurrencyOriginal()) {
            llequSchInfoCurrFuture.setVisibility(View.VISIBLE);
            llequSchInfoCurrOption.setVisibility(View.VISIBLE);
            if (getAppType().equals(PP)) {
                llCurrencyFuture.setVisibility(View.VISIBLE);
                llCurrencyFutureSummary.setVisibility(View.VISIBLE);
                llCurrencyOption.setVisibility(View.VISIBLE);
                llCurrencyOptionSummary.setVisibility(View.VISIBLE);
            }
        } else {
            llequSchInfoCurrFuture.setVisibility(View.GONE);
            llequSchInfoCurrOption.setVisibility(View.GONE);
            if (getAppType().equals(PP)) {
                llCurrencyFuture.setVisibility(View.GONE);
                llCurrencyFutureSummary.setVisibility(View.GONE);
                llCurrencyOption.setVisibility(View.GONE);
                llCurrencyOptionSummary.setVisibility(View.GONE);
            }
        }

        if (getEqSchemeId().equalsIgnoreCase(MF_CUM_EQUITY)) {
            llSchemeDetailValues.setVisibility(View.GONE);
        } else {
            llSchemeDetailValues.setVisibility(View.VISIBLE);
        }

        if (getAppType().equalsIgnoreCase(CRM)) {
            llAdminCharges.setVisibility(View.VISIBLE);
            llBrokingAmc.setVisibility(View.VISIBLE);
        } else {
            llAdminCharges.setVisibility(View.GONE);
            llBrokingAmc.setVisibility(View.GONE);
        }
    }
    //BA Module Scheme
    Spinner spEquBrokerage, spCommBrokerageScheme, spCommTrading, spCommDelivery,
        spNCXBrokerageScheme, spDpScheme;
    ArrayList<RowData> arrEquityBankName, arrCommodityBankName, arrEquityBankDetail,
        arrEquBankProof, arrCommBankProof, arrEquPhotoProof, arrCommPhotoProof,
        arrEquScheme, arrCommScheme, arrEquSchMarginDeposit, arrCommSchMarginDeposit,
        arrTradingEQ, arrDeliveryEQ, arrFuturesEQ, arrOptionsEQ, arrDPEQ, arrCurrencyFuturesEQ,
        arrCurrencyOptionsEQ,
        arrFuturesCOMM, arrOptionsCOMM, arrDeliveryCOMM, arrFuturesNCXCOMM, arrOptionsNCXCOMM,
        arrDeliveryNCXCOMM, arrListBrokerage, arrMFBankName, arrDpScheme;

    private final AdapterView.OnItemSelectedListener equSchemeListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strEquSchMarDep = ((RowData) arrEquScheme.get(position)).getMarDep();
                strEquSchID = ((RowData) arrEquScheme.get(position)).getSchmeID();
                strEquSchSelID = ((RowData) arrEquScheme.get(position)).getSchemeSelID();
                strEquSchKey = ((RowData) arrEquScheme.get(position)).getKey();
                customProgress.setVisibility(View.GONE);
                etEquSchemeError.setVisibility(View.GONE);

                if (isCommodityOriginal()) {
                    for (RowData i : arrCommScheme) {
                        if (strEquSchSelID.equalsIgnoreCase(MF_CUM_EQUITY) && i.getSchemeSelID()
                            .equals(i.getSchmeID())) {
                            commSchMarDep = i.getMarDep();
                            strComSchID = i.getSchmeID();
                            strCommSchSelID = i.getSchemeSelID();
                            strCommSchKey = i.getKey();
                            etCommSchemeError.setVisibility(View.GONE);
                        } else if (i.getMarDep()
                            .equals(((RowData) arrEquScheme.get(position)).getMarDep())) {
                            commSchMarDep = i.getMarDep();
                            strComSchID = i.getSchmeID();
                            strCommSchSelID = i.getSchemeSelID();
                            strCommSchKey = i.getKey();
                            etCommSchemeError.setVisibility(View.GONE);
                        }
                    }
                    setCommodityScheme();
                }

                if (getAppType().equals("DIRECT") && !getIsAadharVerified()) {
                    if (rbValuePacAcc.isChecked()) {
                        if (strEquSchKey.equalsIgnoreCase("-1")) {
                            btnSelectedScheme.setVisibility(View.GONE);
                            bindEquDetailsOnSchemeSelection(strEquSchID);
                        } else {
                            btnSelectedScheme.setVisibility(View.VISIBLE);
                            bindEquDetailsOnSchemeSelection(strEquSchID);
                        }
                    } else {
                        if (strEquSchKey.equalsIgnoreCase("-1")) {
                            btnMarginSelectedScheme.setVisibility(View.GONE);
                            bindEquDetailsOnSchemeSelection(strEquSchID);
                        } else {
                            //btnMarginSelectedScheme.setVisibility(View.VISIBLE);
                            btnSelectedSchemeDetailVisibilty();
                            bindEquDetailsOnSchemeSelection(strEquSchID);
                        }
                    }

                    if (strEquSchID.equals("9999")) {
                        if (!isEquOtherSchSelected) {
                            etEquMarginDeposit.setText("");
                        } else {
                            etEquMarginDeposit.setText(getEquMarginDeposit());
                            getOtherEquSchDetail();
                            isEquOtherSchSelected = false;
                        }
                        btnGetEquScheme.setVisibility(View.VISIBLE);
                        etEquMarginDeposit.setEnabled(true);
                    } else {
                        etEquMarginDeposit.setEnabled(false);
                        etEquMarginDeposit.setText(strEquSchMarDep);
                        btnGetEquScheme.setVisibility(View.GONE);
                    }
                    // btnMarginSelectedScheme.setVisibility(View.GONE);

                } else if (getAppType().equals("DIRECT") && getIsAadharVerified()) {
                    if (strEquSchKey.equalsIgnoreCase("-1")) {
                        btnMarginSelectedScheme.setVisibility(View.GONE);
                        bindEquDetailsOnSchemeSelection(strEquSchID);
                    } else {
                        //btnMarginSelectedScheme.setVisibility(View.VISIBLE);
                        btnSelectedSchemeDetailVisibilty();
                        bindEquDetailsOnSchemeSelection(strEquSchID);
                    }
                    if (strEquSchID.equals("9999")) {
                        if (!isEquOtherSchSelected) {
                            etEquMarginDeposit.setText("");
                        } else {
                            etEquMarginDeposit.setText(getEquMarginDeposit());
                            getOtherEquSchDetail();
                            isEquOtherSchSelected = false;
                        }
                        btnGetEquScheme.setVisibility(View.VISIBLE);
                        etEquMarginDeposit.setEnabled(true);
                    } else {
                        etEquMarginDeposit.setEnabled(false);
                        etEquMarginDeposit.setText(strEquSchMarDep);
                        btnGetEquScheme.setVisibility(View.GONE);
                    }
                } else {
                    if (strEquSchID.equals("9999")) {
                        if (!isEquOtherSchSelected) {
                            etEquMarginDeposit.setText("");
                        } else {
                            etEquMarginDeposit.setText(getEquMarginDeposit());
                            getOtherEquSchDetail();
                            isEquOtherSchSelected = false;
                        }
                        btnGetEquScheme.setVisibility(View.VISIBLE);
                        etEquMarginDeposit.setEnabled(true);
                        bindEquDetailsOnSchemeSelection(strEquSchSelID);
                    } else {
                        etEquMarginDeposit.setEnabled(false);
                        etEquMarginDeposit.setText(strEquSchMarDep);
                        btnGetEquScheme.setVisibility(View.GONE);
                        bindEquDetailsOnSchemeSelection(strEquSchID);
                    }
                }

           /* if (strEquSchSelID.equals("9999")) {
                btnGetEquScheme.setVisibility(View.VISIBLE);
                etEquMarginDeposit.setEnabled(true);
                getOtherEquSchDetail();
            }*/
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    private final AdapterView.OnItemSelectedListener equSelectBankItemListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strEquSelectBankStr = spEquSelectBank.getSelectedItem().toString();
                etEquSelectBankError.setVisibility(View.GONE);
                //  etEquityMICR.setText("");
                //updateBankDetail = false;
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private final AdapterView.OnItemSelectedListener mfSelectBankItemListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etMFSelectBankError.setVisibility(View.GONE);
                llMfBankDetails.setVisibility(View.GONE);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private final AdapterView.OnItemSelectedListener equBankProofListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strequBankProof = spEquBankProof.getSelectedItem().toString();
                etEquBankProoflError.setVisibility(View.GONE);
                //updateBankDetail = false;
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private final AdapterView.OnItemSelectedListener commBankProofListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCommBankProof = spCommBankProof.getSelectedItem().toString();
                etCommBankProoflError.setVisibility(View.GONE);
                //updateBankDetail = false;
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private final AdapterView.OnItemSelectedListener equPhotoProofListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strEquPhotoProof = spEquPhotoProof.getSelectedItem().toString();
                etEquPhotoProofError.setVisibility(View.GONE);
                //updateBankDetail = false;
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private final AdapterView.OnItemSelectedListener commPhotoProofListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCommPhotoProof = spCommPhotoProof.getSelectedItem().toString();
                etCommPhotoProofError.setVisibility(View.GONE);
                //updateBankDetail = false;
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    ArrayAdapter<RowData> arrAdapCommodityBankName, arrAdapEquScheme, arrAdapCommScheme,
        arrAdapEquityBankName, arrAdapEquBankProof, arrAdapCommBankProof, arrAdapEquPhotoProof,
        arrAdapCommPhotoProof,
        arrAdapTradingEQ, arrAdapDeliveryEQ, arrAdapFuturesEQ, arrAdapOptionsEQ, arrAdapDPEQ,
        arrAdapCurrencyFuturesEQ,
        arrAdapCurrencyOptionsEQ, arrAdapFuturesCOMM, arrAdapOptionsCOMM, arrAdapDeliveryCOMM,
        arrAdapFuturesNCXCOMM, arrAdapOptionsNCXCOMM, arrAdapDelNCXCOMM,
        arrAdapBrokerage, arrAdapMFBankName, arrAdapDpScheme;
    String strEquSelectBankStr = "", strCommSelectBankStr = "", strEquSchSelID = "", strEquSchKey =
        "", strCommSchSelID = "", strCommSchKey = "", strEquSchMarDep = "", commSchMarDep = "",
        strEquSchID = "0", strComSchID = "0", strTradeProID = "", strTradeProName = "",
        strDelProID = "", strDelProName = "", strFutProID = "", strFutProName = "", strOptProID =
        "", strOptProName = "", strDPProID = "", strDPProName = "", strCurrFutProID = "",
        strCurrFutProName = "", strCurrOptProID = "", strCurrOptProName = "", strequBankProof = "",
        strCommBankProof = "", strEquPhotoProof = "", strCommPhotoProof = "", strFutProfIdCOMM = "",
        strFutProfNameCOMM = "", strOptProfIdCOMM = "",
        strOptProfNameCOMM = "", strDelCOMMProfIdCOMM = "", strDelCOMMProfNameCOMM = "",
        strNCXFutProfIdCOMM = "", strNCXFutProfNameCOMM = "", strNCXOptProfIdCOMM = "",
        strNCXOptProfNameCOMM = "", strNCXDelCOMMProfId = "", strNCXDelCOMMProfName = "",
        strDpSchName = "", strDpSchID = "0";
    TextView tvequSchInfoAccOpenCharge, tvequSchInfoSoftCharge, tvequSchInfoIntraday1stLeg,
        tvequSchInfoIntraday2ndLeg, tvequSchInfoDelivery, tvequSchInfoFutures1stLeg,
        tvequSchInfoFutures2ndLeg, tvEqAdminCharges, tvEqBrokingAmc,
        tvequSchInfoOptions, tvequSchInfoCurrFuture, tvequSchInfoCurrOption, tvEquAccOpenNote,
        tvCommAccOpenNote, tvCommDelBrok, tvCommIntradayBrok;
    //static boolean updateBankDetail = false, updateSchemeDetail = false, bankDetailFormUpdated = false;
    TextView tvBrokSchSummary, tvTradeSummary, tvDelSummary, tvFutureSummary, tvOptionsSummary,
        tvDPSummary, tvBAMCXBrokSummary, tvBAMCXFutSummary,
        tvBAMCXOptSummary, tvBAMCXDPSummary, tvBANCXBrokSummary, tvBANCXFutSummary,
        tvBANCXOptSummary, tvBANCXDPAMCSummary, tvCurrencyFutureSummary, tvCurrencyOptionSummary;
    String equBankName = "", equBankBranch = "", equBankAdd = "", equBankAccNo = "", equBankAcType =
        "", equBankMICRCode = "", equBankIFSCCode = "", equBankProof = "", equBankPhotoProof = "";
    String commBankName = "", commBankBranch = "", commBankAdd = "", commBankAccNo = "",
        commBankAcType = "", commBankMICRCode = "", commBankIFSCCode = "", commBankProof = "",
        commBankPhotoProof = "";
    //Personal
    TextView tvResidentStatus, tvSubStatus, tvName, tvGender, tvRelation, tvFatherSpouseName,
        tvMotherName, tvMaidenName, tvDOB, tvPAN, tvAadhaarNumber, tvFlatNoCorres, tvBldgNoCorres,
        tvCountryCorres, tvAreaCorres, tvCityCorres,
        tvStateCorres, tvPincodeCorres, tvAddProofCorres, tvAddProofPerm, tvFlatNoPerm,
        tvBldgNoPerm, tvCountryPerm, tvAreaPerm, tvCityPerm, tvStatePerm, tvPincodePerm,
        tvMaritalStatus, tvMobile, tvEmail, tvNameOnPAN, tvTelNo, tvFbId, tvTwitterId, tvLinkedinID,
        tvOccupation, tvQualification, tvIncome, tvContract,/* tvStockPortfolio,*/
        tvPoliticalyExposed, tvRelatedPolitical, tvNetWorth, tvNWDate, tvBrokerName, tvClientCode,
        tvSebiAuthority, tvTC, tvCountryCitizenshipQuestion, tvCountryBirthQuestion,
        tvCountryTaxQuestion, tvPoaAddressQuestion, tvAddressOutsideIndiaQuestion,
        tvCounrtyOfCitizen, tvCountryOfBirth, tvPlaceOfBirth, tvCountryOfTaXRes, tvTaxIDNum,
        tvNomineeName, tvNomineePAN, tvNomineeAadhaar, tvNomineeDOB, tvNomineeEmail, tvNomineeTelNo,
        tvNomineeRelationship, tvNomineeApplicantRelationship, tvNomineeFlatNoCorres,
        tvNomineeBldgNoCorres, tvNomineeCountryCorres, tvNomineeAreaCorres, tvNomineeCityCorres,
        tvNomineeStateCorres, tvNomineePincodeCorres, tvNomineeGuardianName, tvNomineeGuardianAge,
        tvNomineeGuardianEmail, tvNomineeGuardianTelNo, tvNomineeGuardianRelationship,
        tvNomineeGuardianFlatNoCorres, tvNomineeGuardianBldgNoCorres,
        tvNomineeGuardianCountryCorres, tvNomineeGuardianAreaCorres, tvNomineeGuardianCityCorres,
        tvNomineeGuardianStateCorres,
        tvNomineeGuardianPincodeCorres, tvSecHoldEditPersonalDetail, tvSecHoldResidentStatus,
        tvSecHoldName, tvSecHoldGender, tvSecHoldFatherorSpouseName, tvSecHolddob,
        tvSecHoldMaritalStatus, tvSecHoldPan,
        tvSecHoldAadhaar, tvSecHoldMobile, tvSecHoldEmail, tvSecHoldTel, tvSecHoldCorrAddProof,
        tvSecHoldCorrBldgName, tvSecHoldCorrFlatNo, tvSecHoldCorrArea, tvSecHoldCorrPincode,
        tvSecHoldCorrState, tvSecHoldCorrCity,
        tvSecHoldCorrCountry, tvSecHoldPermAddProof, tvSecHoldPermBldgName, tvSecHoldPermFlatNo,
        tvSecHoldPermArea, tvSecHoldPermPincode, tvSecHoldPermState, tvSecHoldPermCity,
        tvSecHoldPermCountry, tvSecHoldAnnualIncome,
        tvSecHoldOccDetail, tvSecHoldPoliticalExposedPerson, tvSecHoldRelatedPoliticalExposedPerson,
        tvThirdHoldEditPersonalDetail, tvThirdHoldResidentStatus, tvThirdHoldName,
        tvThirdHoldGender, tvThirdHoldFatherorSpouseName,
        tvThirdHolddob, tvThirdHoldMaritalStatus, tvThirdHoldPan, tvThirdHoldAadhaar,
        tvThirdHoldMobile, tvThirdHoldEmail, tvThirdHoldTel, tvThirdHoldCorrAddProof,
        tvThirdHoldCorrBldgName, tvThirdHoldCorrFlatNo, tvThirdHoldCorrArea,
        tvThirdHoldCorrPincode, tvThirdHoldCorrState, tvThirdHoldCorrCity, tvThirdHoldCorrCountry,
        tvThirdHoldPermAddProof, tvThirdHoldPermBldgName, tvThirdHoldPermFlatNo,
        tvThirdHoldPermArea, tvThirdHoldPermPincode,
        tvThirdHoldPermState, tvThirdHoldPermCity, tvThirdHoldPermCountry, tvThirdHoldAnnualIncome,
        tvThirdHoldOccDetail, tvThirdHoldPoliticalExposedPerson,
        tvThirdHoldRelatedPoliticalExposedPerson, tvNomineeJointTitle;
    //Bank Detail
    TextView tvEqBankNameReview, tvEqMICRReview, tvEqIFSCReview, tvEqBranchNameReview,
        tvEqBankAddressReview, tvEqAccNumberReview, tvEqAccTypeReview, tvEqProductReview,
        tvCommBankNameReview, tvCommMICRReview, tvCommIFSCReview, tvCommBranchNameReview,
        tvCommBankAddressReview, tvCommAccNumberReview, tvCommAccTypeReview, tvCommProductReview;
    LinearLayout llNomineeDematMain, llNomineeDetail, llNomineeGuardianDetails,
        llSecondHolderLayoutDetail, llThirdHolderLayoutDetail;
    LinearLayout llEqBankDetail, llCommBankDetail, llBankDetailForm, llFundTransfer,
        llEqSchemeDetail, llCommSchemeDetail, llEqSchemeDetailReview, llCommSchemeDetailReview,
        llSchemeDetail,
        llReviewTC, llMFBankDetail, llValuePack, llSecondHolderLayout, llThirdHolderLayout,
        llContractNo;
    String bankNameReview, micrCodeReview, ifscCodeReview, bankBranchReview, bankAddReview,
        accNumberReview, accTypeReview, photoProofReview, bankProofReview, productReview,
        bankNameReviewFirst, micrCodeReviewFirst, ifscCodeReviewFirst, bankBranchReviewFirst,
        bankAddReviewFirst, accNumberReviewFirst, accTypeReviewFirst, photoProofReviewFirst,
        bankProofReviewFirst, productReviewFirst;
    TextInputLayout input_layout_equityMICR, input_layout_commMICR, input_layout_equityAccNumber,
        input_layout_commAccNumber, input_layout_equMarginDeposit, input_layout_commMarginDeposit,
        input_layout_MFMICR, input_layout_MFAccNumber;
    ArrayList<HashMap<String, String>> arrayListEqSchemeReview, arrayListCommSchemeReview,
        arrayListProductReview, arrayListMFBankList;
    //    ArrayAdapter<String> arrAdapBrokerage;
    RadioButton rbEquCurrent, rbEquSaving, rbCommCurrent, rbCommSaving, rbMFCurrent, rbMFSaving,
        rbMarginAcc, rbValuePacAcc;
    TextView etEquBankProoflError, etEquPhotoProofError, etEquSelectBankError,
        etCommBankProoflError, etCommPhotoProofError, etCommSelectBankError, etMFSelectBankError;
    TextView etEquBrokerageError, etEquTradingError, etEquDeliveryError, etEquFutureError,
        etEquOptionError,
        etEquDPAMCError, etEquCurrencyFutureError, etEquCurrencyOptionError, etEquSchemeError,
        etEquMarginDepositError, etEquAccountError;
    private final AdapterView.OnItemSelectedListener equBrokerageListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvBrokSchSummary.setText(spEquBrokerage.getSelectedItem().toString());
                RowData selectedItem = (RowData) spEquBrokerage.getSelectedItem();
                String strBAEquBrokSch = selectedItem.getValue();
                String strSelectedID = selectedItem.getKey();

                etEquBrokerageError.setVisibility(View.GONE);
                fillEquBASchemeDetails(strSelectedID);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    TextView etCommBrokerageSchemeError, etCommFutureError, etCommOptionError, etCommDPAMCError,
        etEquityAccNumberError, etCommAccNumberError,
        etNCXBrokerageSchemeError, etNCXFutureError, etNCXOptionError, etNCXDPAMCError,
        etCommSchemeError, etCommMarginDepositError, etMFAccNumberError,
        etConfirmEquityAccNumberError, etConfirmCommAccNumberError, etConfirmMFAccNumberError,
        etDpSchemeError;
    private final AdapterView.OnItemSelectedListener commMCXBrokSchListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvBAMCXBrokSummary.setText(spCommBrokerageScheme.getSelectedItem().toString());
                RowData selectedItem = (RowData) spCommBrokerageScheme.getSelectedItem();
                String strBAMCXCommBrokSch = selectedItem.getValue();
                String strSelectedID = selectedItem.getKey();

                etCommBrokerageSchemeError.setVisibility(View.GONE);

                fillCommBASchemeDetails(strSelectedID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        try {
            bankDetailFormView = inflater.inflate(R.layout.frag_bank_detail_form, container, false);
            setUpView(bankDetailFormView);
        /*if (getInterestedInMF()) {
            showMFBankDetails();
            *//*if (isGotoReviewEdit()) {
                getReviewDetail();
            }*//*
        } else if (isGotoFundTransfer()) {
            getPaymentStatus();
        } else if (isGotoReviewEdit()) {
            getReviewDetail();
        } else {
            setUserVisibleHint(true);
        }*/

            setUserVisibleHint(true);
        } catch (Exception e) {
            e.printStackTrace();
            //            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            //            Crashlytics.logException(e);
        }
        return bankDetailFormView;
    }

    private void setUpView(View bankDetailFormView) {
       /* dialogReview = new Dialog(mContext);
        dialogReview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogReview.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogReview.setContentView(R.layout.frag_review_and_edit);
        dialogReview.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogReview.setCanceledOnTouchOutside(false);*/
        svBankForm = (ScrollView) bankDetailFormView.findViewById(R.id.svBankForm);
        svBankForm.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        svBankForm.setFocusable(true);
        svBankForm.setFocusableInTouchMode(true);
        svBankForm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        currencyoptionRadiogroup =
            (RadioGroup) bankDetailFormView.findViewById(R.id.currencyoptionRadiogroup);
        currencyfutureRadiogroup =
            (RadioGroup) bankDetailFormView.findViewById(R.id.currencyfutureRadiogroup);
        spEquSelectBank = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spEquSelectBank);
        spMFSelectBank = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spMFSelectBank);
        spEquBankProof = (Spinner) bankDetailFormView.findViewById(R.id.spEquBankProof);
        spEquPhotoProof = (Spinner) bankDetailFormView.findViewById(R.id.spEquPhotoProof);
        spCommBankProof = (Spinner) bankDetailFormView.findViewById(R.id.spCommBankProof);
        spCommPhotoProof = (Spinner) bankDetailFormView.findViewById(R.id.spCommPhotoProof);
        spCommSelectBank =
            (SearchableSpinner) bankDetailFormView.findViewById(R.id.spCommSelectBank);

        spEquScheme = (Spinner) bankDetailFormView.findViewById(R.id.spEquScheme);
        spCommScheme = (Spinner) bankDetailFormView.findViewById(R.id.spCommScheme);
        spEquTrading = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spEquTrading);
        spEquDelivery = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spEquDelivery);
        spEquFuture = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spEquFuture);
        spEquOption = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spEquOption);
        spEquDPAMC = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spEquDPAMC);
        spEquCurrencyFuture =
            (SearchableSpinner) bankDetailFormView.findViewById(R.id.spEquCurrencyFuture);
        spEquCurrencyOption =
            (SearchableSpinner) bankDetailFormView.findViewById(R.id.spEquCurrencyOption);

        spEquBrokerage = (Spinner) bankDetailFormView.findViewById(R.id.spEquBrokerage);
        spCommBrokerageScheme =
            (Spinner) bankDetailFormView.findViewById(R.id.spCommBrokerageScheme);
        spCommFuture = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spCommFuture);
        spCommOption = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spCommOption);
        spCommDPAMC = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spCommDPAMC);
        spNCXBrokerageScheme = (Spinner) bankDetailFormView.findViewById(R.id.spNCXBrokerageScheme);
        spNCXFuture = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spNCXFuture);
        spNCXOption = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spNCXOption);
        spNCXDPAMC = (SearchableSpinner) bankDetailFormView.findViewById(R.id.spNCXDPAMC);

        etEquityMICR = (EditText) bankDetailFormView.findViewById(R.id.etEquityMICR);
        etMFMICR = (EditText) bankDetailFormView.findViewById(R.id.etMFMICR);
        etCommMICR = (EditText) bankDetailFormView.findViewById(R.id.etCommMICR);
        etEquityAccNumber = (EditText) bankDetailFormView.findViewById(R.id.etEquityAccNumber);
        etCommAccNumber = (EditText) bankDetailFormView.findViewById(R.id.etCommAccNumber);
        etMFAccNumber = (EditText) bankDetailFormView.findViewById(R.id.etMFAccNumber);
        etEquMarginDeposit = (EditText) bankDetailFormView.findViewById(R.id.etEquMarginDeposit);
        etConfirmMFAccNumber =
            (EditText) bankDetailFormView.findViewById(R.id.etConfirmMFAccNumber);
        etConfirmCommAccNumber =
            (EditText) bankDetailFormView.findViewById(R.id.etConfirmCommAccNumber);
        etConfirmEquityAccNumber =
            (EditText) bankDetailFormView.findViewById(R.id.etConfirmEquityAccNumber);

        etCommMarginDeposit = (EditText) bankDetailFormView.findViewById(R.id.etCommMarginDeposit);
        etCommMarginDepositError =
            (TextView) bankDetailFormView.findViewById(R.id.etCommMarginDepositError);
        etEquMarginDepositError =
            (TextView) bankDetailFormView.findViewById(R.id.etEquMarginDepositError);

        etEquBankProoflError =
            (TextView) bankDetailFormView.findViewById(R.id.etEquBankProoflError);
        etEquPhotoProofError =
            (TextView) bankDetailFormView.findViewById(R.id.etEquPhotoProofError);
        etEquSelectBankError =
            (TextView) bankDetailFormView.findViewById(R.id.etEquSelectBankError);
        etEquBrokerageError = (TextView) bankDetailFormView.findViewById(R.id.etEquBrokerageError);
        etEquAccountError = (TextView) bankDetailFormView.findViewById(R.id.etEquAccountError);
        etEquTradingError = (TextView) bankDetailFormView.findViewById(R.id.etEquTradingError);
        etEquDeliveryError = (TextView) bankDetailFormView.findViewById(R.id.etEquDeliveryError);
        etEquFutureError = (TextView) bankDetailFormView.findViewById(R.id.etEquFutureError);
        etEquOptionError = (TextView) bankDetailFormView.findViewById(R.id.etEquOptionError);
        etEquDPAMCError = (TextView) bankDetailFormView.findViewById(R.id.etEquDPAMCError);
        etEquCurrencyFutureError =
            (TextView) bankDetailFormView.findViewById(R.id.etEquCurrencyFutureError);
        etEquCurrencyOptionError =
            (TextView) bankDetailFormView.findViewById(R.id.etEquCurrencyOptionError);
        etEquSchemeError = (TextView) bankDetailFormView.findViewById(R.id.etEquSchemeError);
        etEquityAccNumberError =
            (TextView) bankDetailFormView.findViewById(R.id.etEquityAccNumberError);

        etCommBankProoflError =
            (TextView) bankDetailFormView.findViewById(R.id.etCommBankProoflError);
        etCommPhotoProofError =
            (TextView) bankDetailFormView.findViewById(R.id.etCommPhotoProofError);
        etCommSelectBankError =
            (TextView) bankDetailFormView.findViewById(R.id.etCommSelectBankError);
        etCommSchemeError = (TextView) bankDetailFormView.findViewById(R.id.etCommSchemeError);
        etCommBrokerageSchemeError =
            (TextView) bankDetailFormView.findViewById(R.id.etCommBrokerageSchemeError);
        etCommFutureError = (TextView) bankDetailFormView.findViewById(R.id.etCommFutureError);
        etCommOptionError = (TextView) bankDetailFormView.findViewById(R.id.etCommOptionError);
        etCommDPAMCError = (TextView) bankDetailFormView.findViewById(R.id.etCommDPAMCError);
        etNCXBrokerageSchemeError =
            (TextView) bankDetailFormView.findViewById(R.id.etNCXBrokerageSchemeError);
        etNCXFutureError = (TextView) bankDetailFormView.findViewById(R.id.etNCXFutureError);
        etNCXOptionError = (TextView) bankDetailFormView.findViewById(R.id.etNCXOptionError);
        etNCXDPAMCError = (TextView) bankDetailFormView.findViewById(R.id.etNCXDPAMCError);
        etCommAccNumberError =
            (TextView) bankDetailFormView.findViewById(R.id.etCommAccNumberError);

        etMFSelectBankError = (TextView) bankDetailFormView.findViewById(R.id.etMFSelectBankError);
        etMFAccNumberError = (TextView) bankDetailFormView.findViewById(R.id.etMFAccNumberError);
        etConfirmEquityAccNumberError =
            (TextView) bankDetailFormView.findViewById(R.id.etConfirmEquityAccNumberError);
        etConfirmCommAccNumberError =
            (TextView) bankDetailFormView.findViewById(R.id.etConfirmCommAccNumberError);
        etConfirmMFAccNumberError =
            (TextView) bankDetailFormView.findViewById(R.id.etConfirmMFAccNumberError);

        input_layout_equityMICR =
            (TextInputLayout) bankDetailFormView.findViewById(R.id.input_layout_equityMICR);
        input_layout_equityAccNumber =
            (TextInputLayout) bankDetailFormView.findViewById(R.id.input_layout_equityAccNumber);
        input_layout_equMarginDeposit =
            (TextInputLayout) bankDetailFormView.findViewById(R.id.input_layout_equMarginDeposit);

        input_layout_commMICR =
            (TextInputLayout) bankDetailFormView.findViewById(R.id.input_layout_commMICR);
        input_layout_commAccNumber =
            (TextInputLayout) bankDetailFormView.findViewById(R.id.input_layout_commAccNumber);
        input_layout_commMarginDeposit =
            (TextInputLayout) bankDetailFormView.findViewById(R.id.input_layout_commMarginDeposit);

        input_layout_MFMICR =
            (TextInputLayout) bankDetailFormView.findViewById(R.id.input_layout_MFMICR);
        input_layout_MFAccNumber =
            (TextInputLayout) bankDetailFormView.findViewById(R.id.input_layout_MFAccNumber);

        etEquityMICR.addTextChangedListener(this);
        etCommMICR.addTextChangedListener(this);
        etMFMICR.addTextChangedListener(this);
        etEquMarginDeposit.addTextChangedListener(this);
        etCommMarginDeposit.addTextChangedListener(this);

        btnEquityBankDetails = (Button) bankDetailFormView.findViewById(R.id.btnEquityBankDetails);
        btnMFBankDetails = (Button) bankDetailFormView.findViewById(R.id.btnMFBankDetails);
        btnMFAddBankDetails = (Button) bankDetailFormView.findViewById(R.id.btnMFAddBankDetails);
        btnCommBankDetails = (Button) bankDetailFormView.findViewById(R.id.btnCommBankDetails);
        btnGetEquScheme = (Button) bankDetailFormView.findViewById(R.id.btnGetEquScheme);
        btnGetCommScheme = (Button) bankDetailFormView.findViewById(R.id.btnGetCommScheme);
        btnBankBack = (TextView) bankDetailFormView.findViewById(R.id.btnBankBack);
        tvBankDetailTitle = (TextView) bankDetailFormView.findViewById(R.id.tvBankDetailTitle);
        btnBankBack.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btnSelectedScheme = (Button) bankDetailFormView.findViewById(R.id.btnSelectedScheme);
        btnAllScheme = (Button) bankDetailFormView.findViewById(R.id.btnAllScheme);
        btnMarginAllScheme = (Button) bankDetailFormView.findViewById(R.id.btnMarginAllScheme);
        btnCommMarginAllScheme =
            (Button) bankDetailFormView.findViewById(R.id.btnCommMarginAllScheme);
        btnMarginSelectedScheme =
            (Button) bankDetailFormView.findViewById(R.id.btnMarginSelectedScheme);
        btnCommMarginSelectedScheme =
            (Button) bankDetailFormView.findViewById(R.id.btnCommMarginSelectedScheme);

        btnBankAndSchemeUpdate =
            (Button) bankDetailFormView.findViewById(R.id.btnBankAndSchemeUpdate);
        if (getAppType().equals("DIRECT") && !getIsAadharVerified()) {
            btnBankAndSchemeUpdate.setText("Next");
        } else if (getInterestedInMF()) {
            btnBankAndSchemeUpdate.setText("Next");

        } else {
            //            btnBankAndSchemeUpdate.setText("Fund Transfer");
            btnBankAndSchemeUpdate.setText("Next");

        }
        //btnUpdateScheme = (Button) bankDetailFormView.findViewById(R.id.btnUpdateScheme);

        tvBrokSchSummary = (TextView) bankDetailFormView.findViewById(R.id.tvBrokSchSummary);
        tvTradeSummary = (TextView) bankDetailFormView.findViewById(R.id.tvTradeSummary);
        tvDelSummary = (TextView) bankDetailFormView.findViewById(R.id.tvDelSummary);
        tvFutureSummary = (TextView) bankDetailFormView.findViewById(R.id.tvFutureSummary);
        tvOptionsSummary = (TextView) bankDetailFormView.findViewById(R.id.tvOptionsSummary);
        tvDPSummary = (TextView) bankDetailFormView.findViewById(R.id.tvDPSummary);
        tvCurrencyFutureSummary =
            (TextView) bankDetailFormView.findViewById(R.id.tvCurrencyFutureSummary);
        tvCurrencyOptionSummary =
            (TextView) bankDetailFormView.findViewById(R.id.tvCurrencyOptionSummary);
        tvBAMCXBrokSummary = (TextView) bankDetailFormView.findViewById(R.id.tvBAMCXBrokSummary);
        tvBAMCXFutSummary = (TextView) bankDetailFormView.findViewById(R.id.tvBAMCXFutSummary);
        tvBAMCXOptSummary = (TextView) bankDetailFormView.findViewById(R.id.tvBAMCXOptSummary);
        tvBAMCXDPSummary = (TextView) bankDetailFormView.findViewById(R.id.tvBAMCXDPSummary);
        tvBANCXBrokSummary = (TextView) bankDetailFormView.findViewById(R.id.tvBANCXBrokSummary);
        tvBANCXFutSummary = (TextView) bankDetailFormView.findViewById(R.id.tvBANCXFutSummary);
        tvBANCXOptSummary = (TextView) bankDetailFormView.findViewById(R.id.tvBANCXOptSummary);
        tvBANCXDPAMCSummary = (TextView) bankDetailFormView.findViewById(R.id.tvBANCXDPAMCSummary);

        tvequSchValidity = (TextView) bankDetailFormView.findViewById(R.id.tvequSchValidity);
        tvequSchDpAmc = (TextView) bankDetailFormView.findViewById(R.id.tvequSchDpAmc);
        tvequSchUpfrontFees = (TextView) bankDetailFormView.findViewById(R.id.tvequSchUpfrontFees);

        //  cbEquCurrent = (CheckBox) bankDetailFormView.findViewById(R.id.cbEquCurrent);
        //  cbCommCurrent = (CheckBox) bankDetailFormView.findViewById(R.id.cbCommCurrent);
        //  cbEquSaving = (CheckBox) bankDetailFormView.findViewById(R.id.cbEquSaving);
        //  cbCommSaving = (CheckBox) bankDetailFormView.findViewById(R.id.cbCommSaving);
        cbSameAsEquity = (CheckBox) bankDetailFormView.findViewById(R.id.cbSameAsEquity);
        cbSameAsMCX = (CheckBox) bankDetailFormView.findViewById(R.id.cbSameAsMCX);

        //  cbMarginAcc = (CheckBox) bankDetailFormView.findViewById(R.id.cbMarginAcc);
        //  cbValuePacAcc = (CheckBox) bankDetailFormView.findViewById(R.id.cbValuePacAcc);
        //  cbMFCurrent = (CheckBox) bankDetailFormView.findViewById(R.id.cbMFCurrent);
        //  cbMFSaving = (CheckBox) bankDetailFormView.findViewById(R.id.cbMFSaving);
        cbMFTC = (CheckBox) bankDetailFormView.findViewById(R.id.cbMFTC);

        //  cbEquSaving.setChecked(true);
        //  cbCommSaving.setChecked(true);
        //  cbMFSaving.setChecked(true);
        cbMFTC.setChecked(true);
        //  cbMarginAcc.setOnCheckedChangeListener(this);
        //  cbValuePacAcc.setOnCheckedChangeListener(this);

        cbMFTC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbMFTC.setChecked(true);
                } else {
                    cbMFTC.setChecked(false);
                }
            }
        });
        //          tvTC = (TextView) bankDetailFormView.findViewById(R.id.tvTC);

        tvEquityBankName = (TextView) bankDetailFormView.findViewById(R.id.tvEquityBankName);
        tvEquityBranchName = (TextView) bankDetailFormView.findViewById(R.id.tvEquityBranchName);
        tvEquityMICR = (TextView) bankDetailFormView.findViewById(R.id.tvEquityMICR);
        tvEquityIFSC = (TextView) bankDetailFormView.findViewById(R.id.tvEquityIFSC);
        tvEquityBankAddress = (TextView) bankDetailFormView.findViewById(R.id.tvEquityBankAddress);

        tvCommBankName = (TextView) bankDetailFormView.findViewById(R.id.tvCommBankName);
        tvCommBranchName = (TextView) bankDetailFormView.findViewById(R.id.tvCommBranchName);
        tvCommMICR = (TextView) bankDetailFormView.findViewById(R.id.tvCommMICR);
        tvCommIFSC = (TextView) bankDetailFormView.findViewById(R.id.tvCommIFSC);
        tvCommBankAddress = (TextView) bankDetailFormView.findViewById(R.id.tvCommBankAddress);

        tvMFBankName = (TextView) bankDetailFormView.findViewById(R.id.tvMFBankName);
        tvMFBranchName = (TextView) bankDetailFormView.findViewById(R.id.tvMFBranchName);
        tvMFMICR = (TextView) bankDetailFormView.findViewById(R.id.tvMFMICR);
        tvMFIFSC = (TextView) bankDetailFormView.findViewById(R.id.tvMFIFSC);
        tvMFBankAddress = (TextView) bankDetailFormView.findViewById(R.id.tvMFBankAddress);
        // tvMFReviewForm = (TextView) bankDetailFormView.findViewById(R.id.tvMFReviewForm);

        tvequSchInfoAccOpenCharge =
            (TextView) bankDetailFormView.findViewById(R.id.tvequSchInfoAccOpenCharge);
        tvequSchInfoSoftCharge =
            (TextView) bankDetailFormView.findViewById(R.id.tvequSchInfoSoftCharge);
        tvequSchInfoIntraday1stLeg =
            (TextView) bankDetailFormView.findViewById(R.id.tvequSchInfoIntraday1stLeg);
        tvequSchInfoIntraday2ndLeg =
            (TextView) bankDetailFormView.findViewById(R.id.tvequSchInfoIntraday2ndLeg);
        tvequSchInfoDelivery =
            (TextView) bankDetailFormView.findViewById(R.id.tvequSchInfoDelivery);
        tvequSchInfoFutures1stLeg =
            (TextView) bankDetailFormView.findViewById(R.id.tvequSchInfoFutures1stLeg);
        tvequSchInfoFutures2ndLeg =
            (TextView) bankDetailFormView.findViewById(R.id.tvequSchInfoFutures2ndLeg);
        tvequSchInfoOptions = (TextView) bankDetailFormView.findViewById(R.id.tvequSchInfoOptions);
        tvequSchInfoCurrFuture =
            (TextView) bankDetailFormView.findViewById(R.id.tvequSchInfoCurrFuture);
        tvequSchInfoCurrOption =
            (TextView) bankDetailFormView.findViewById(R.id.tvequSchInfoCurrOption);
        tvCommDelBrok = (TextView) bankDetailFormView.findViewById(R.id.tvCommDelBrok);
        tvCommIntradayBrok = (TextView) bankDetailFormView.findViewById(R.id.tvCommIntradayBrok);
        tvEquAccOpenNote = (TextView) bankDetailFormView.findViewById(R.id.tvEquAccOpenNote);
        tvCommAccOpenNote = (TextView) bankDetailFormView.findViewById(R.id.tvCommAccOpenNote);
        tvEquSchemeTermCondition =
            (TextView) bankDetailFormView.findViewById(R.id.tvEquSchemeTermCondition);
        tvEquValuePackTermCondition =
            (TextView) bankDetailFormView.findViewById(R.id.tvEquValuePackTermCondition);
        tvCommSchemeTermCondition =
            (TextView) bankDetailFormView.findViewById(R.id.tvCommSchemeTermCondition);
        tvMFTC = (TextView) bankDetailFormView.findViewById(R.id.tvMFTC);

        llEquityBankDetail =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llEquityBankDetail);
        llValuePack = (LinearLayout) bankDetailFormView.findViewById(R.id.llValuePack);
        llCommodityBankDetail =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llCommodityBankDetail);
        llEquityBankScheme =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llEquityBankScheme);
        llCommodityBankScheme =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llCommodityBankScheme);
        llBranchCommodityBankScheme =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llBranchCommodityBankScheme);
        llBACommodityBankScheme =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llBACommodityBankScheme);
        llSchemeDetailValues =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llSchemeDetailValues);
        llSelectedScheme = (LinearLayout) bankDetailFormView.findViewById(R.id.llSelectedScheme);
        llMarginDeposit = (LinearLayout) bankDetailFormView.findViewById(R.id.llMarginDeposit);
        llBAEquityBankScheme =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llBAEquityBankScheme);
        llBranchEquityBankScheme =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llBranchEquityBankScheme);
        llMFBankDetail = (LinearLayout) bankDetailFormView.findViewById(R.id.llMFBankDetail);
        // customProgress = (LinearLayout) bankDetailFormView.findViewById(R.id.customProgress);
        llBankDetailForm = (LinearLayout) bankDetailFormView.findViewById(R.id.llBankDetailForm);
        llFundTransfer = (LinearLayout) bankDetailFormView.findViewById(R.id.llFundTransfer);
        llFundTransferNote =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llFundTransferNote);
        llBankBack = (LinearLayout) bankDetailFormView.findViewById(R.id.llBankBack);
        llMfBankDetails = (LinearLayout) bankDetailFormView.findViewById(R.id.llMfBankDetails);
        llEquBankDetail = (LinearLayout) bankDetailFormView.findViewById(R.id.llEquBankDetail);
        llCommBankDetails = (LinearLayout) bankDetailFormView.findViewById(R.id.llCommBankDetails);

        lvMFBankDetail = (ListView) bankDetailFormView.findViewById(R.id.lvMFBankDetail);

        llequSchInfoFutures1stLeg =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llequSchInfoFutures1stLeg);
        llequSchInfoFutures2ndLeg =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llequSchInfoFutures2ndLeg);
        llequSchInfoOptions =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llequSchInfoOptions);
        llequSchInfoCurrFuture =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llequSchInfoCurrFuture);
        llequSchInfoCurrOption =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llequSchInfoCurrOption);


        llFuture = (LinearLayout) bankDetailFormView.findViewById(R.id.llFuture);
        llOption = (LinearLayout) bankDetailFormView.findViewById(R.id.llOption);
        llCurrencyFuture = (LinearLayout) bankDetailFormView.findViewById(R.id.llCurrencyFuture);
        llCurrencyOption = (LinearLayout) bankDetailFormView.findViewById(R.id.llCurrencyOption);
        llCurrencyOptionSummary =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llCurrencyOptionSummary);
        llCurrencyFutureSummary =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llCurrencyFutureSummary);
        llOptionSummary = (LinearLayout) bankDetailFormView.findViewById(R.id.llOptionSummary);
        llFutureSummary = (LinearLayout) bankDetailFormView.findViewById(R.id.llFutureSummary);
        llSpinnerCommScheme =
            (LinearLayout) bankDetailFormView.findViewById(R.id.llSpinnerCommScheme);

        // radio button

        rbEquCurrent = (RadioButton) bankDetailFormView.findViewById(R.id.rbEquCurrent);
        rbEquSaving = (RadioButton) bankDetailFormView.findViewById(R.id.rbEquSaving);
        rbCommCurrent = (RadioButton) bankDetailFormView.findViewById(R.id.rbCommCurrent);
        rbCommSaving = (RadioButton) bankDetailFormView.findViewById(R.id.rbCommSaving);
        rbMFCurrent = (RadioButton) bankDetailFormView.findViewById(R.id.rbMFCurrent);
        rbMFSaving = (RadioButton) bankDetailFormView.findViewById(R.id.rbMFSaving);
        rbMarginAcc = (RadioButton) bankDetailFormView.findViewById(R.id.rbMarginAcc);
        rbValuePacAcc = (RadioButton) bankDetailFormView.findViewById(R.id.rbValuePacAcc);
        spDpScheme = (Spinner) bankDetailFormView.findViewById(R.id.spDpScheme);
        etDpSchemeError = (TextView) bankDetailFormView.findViewById(R.id.etDpSchemeError);

        llAdminCharges = bankDetailFormView.findViewById(R.id.llAdminCharges);
        llBrokingAmc = bankDetailFormView.findViewById(R.id.llBrokingAmc);
        tvEqAdminCharges = bankDetailFormView.findViewById(R.id.tvEqAdminCharges);
        tvEqBrokingAmc = bankDetailFormView.findViewById(R.id.tvEqBrokingAmc);

        tvEquSchemeTermCondition.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvEquValuePackTermCondition.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCommSchemeTermCondition.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvMFTC.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        rbValuePacAcc.setOnClickListener(this);
        rbMarginAcc.setOnClickListener(this);

        if (getAppType().equals(DIRECT) && !getIsAadharVerified()) {
            //            llValuePack.setVisibility(View.VISIBLE);//No Value pack commodity scheme changes 14-03-2018
            llValuePack.setVisibility(View.GONE);
        } else {
            llValuePack.setVisibility(View.GONE);
            //  radio.setVisibility(View.VISIBLE);
        }
        if (getAppType().equals("CRM")) {
            btnMarginAllScheme.setVisibility(View.GONE);
            btnMarginSelectedScheme.setVisibility(View.GONE);
        }
        if (getAppType().equals("CRM")) {
            spDpScheme.setVisibility(View.VISIBLE);
        } else {
            spDpScheme.setVisibility(View.GONE);
        }
        if (getAppType().equals("DIRECT")) {
            btnMarginAllScheme.setVisibility(View.VISIBLE);
        } else {
            btnMarginAllScheme.setVisibility(View.GONE);
        }
        if (getAppType().equals("DIRECT")) {
            btnCommMarginAllScheme.setVisibility(View.VISIBLE);
        } else {
            btnCommMarginAllScheme.setVisibility(View.GONE);
        }

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        //Equity BA Scheme array
        arrTradingEQ = new ArrayList<>();
        arrDeliveryEQ = new ArrayList<>();
        arrFuturesEQ = new ArrayList<>();
        arrOptionsEQ = new ArrayList<>();
        arrDPEQ = new ArrayList<>();
        arrCurrencyFuturesEQ = new ArrayList<>();
        arrCurrencyOptionsEQ = new ArrayList<>();
        //Commodity BA MCX Scheme array
        arrFuturesCOMM = new ArrayList<>();
        arrOptionsCOMM = new ArrayList<>();
        arrDeliveryCOMM = new ArrayList<>();
        //Commodity BA NCX Scheme array
        arrFuturesNCXCOMM = new ArrayList<>();
        arrOptionsNCXCOMM = new ArrayList<>();
        arrDeliveryNCXCOMM = new ArrayList<>();

        modelList = new ArrayList<>();
        currencyOptionmodelList = new ArrayList<>();


        strEquMICR = etEquityMICR.getText().toString();
        strEquAccNumber = etEquityAccNumber.getText().toString();
        strCommAccNumber = etCommAccNumber.getText().toString();
        strCommMICR = etCommMICR.getText().toString();

        if (getAppType().equalsIgnoreCase(PP)) {
            llBACommodityBankScheme.setVisibility(View.VISIBLE);
            llBAEquityBankScheme.setVisibility(View.VISIBLE);

            llBranchCommodityBankScheme.setVisibility(View.GONE);
            llBranchEquityBankScheme.setVisibility(View.GONE);
        } else if (getAppType().equalsIgnoreCase(CRM)) {
            llBranchEquityBankScheme.setVisibility(View.VISIBLE);
            llBranchCommodityBankScheme.setVisibility(View.VISIBLE);

            llBACommodityBankScheme.setVisibility(View.GONE);
            llBAEquityBankScheme.setVisibility(View.GONE);
        }

        arrListAccType =
            new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.accountType)));

        ArrayAdapter<String> arrAdapAccType =
            new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrListAccType);

        arrAdapAccType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //BA Module Scheme
        arrListBrokerage = new ArrayList<>();

        if (!isBlank(getUserType()) && getUserType().equalsIgnoreCase(
            IFAUSER)) {//condition for ifa to show only default value
            arrListBrokerage.add(new RowData(0, SELECT, SELECT));
            arrListBrokerage.add(new RowData(1, "D", DEFAULT));
        } else {
            arrListBrokerage.add(new RowData(0, SELECT, SELECT));
            arrListBrokerage.add(new RowData(1, "D", DEFAULT));
            arrListBrokerage.add(new RowData(2, "MR", MAX_RATE));
        }
        arrAdapBrokerage =
            new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrListBrokerage);
        arrAdapBrokerage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spEquBrokerage.setAdapter(arrAdapBrokerage);
        spCommBrokerageScheme.setAdapter(arrAdapBrokerage);
        spNCXBrokerageScheme.setAdapter(arrAdapBrokerage);

        spEquBrokerage.setOnItemSelectedListener(equBrokerageListener);
        spEquBrokerage.setOnTouchListener(equBrokerageTouchListener);
        spCommBrokerageScheme.setOnItemSelectedListener(commMCXBrokSchListener);
        spCommBrokerageScheme.setOnTouchListener(commMCXBrokTouchListener);
        spNCXBrokerageScheme.setOnItemSelectedListener(commNCXBrokSchListener);
        spNCXBrokerageScheme.setOnTouchListener(commNCXBrokTouchListener);

        btnEquityBankDetails.setOnClickListener(this);
        btnAllScheme.setOnClickListener(this);
        btnSelectedScheme.setOnClickListener(this);
        btnMarginAllScheme.setOnClickListener(this);
        btnCommMarginAllScheme.setOnClickListener(this);
        btnMarginSelectedScheme.setOnClickListener(this);
        btnCommMarginSelectedScheme.setOnClickListener(this);
        btnCommBankDetails.setOnClickListener(this);
        btnMFBankDetails.setOnClickListener(this);
        btnMFAddBankDetails.setOnClickListener(this);
        //        btnMakePayment.setOnClickListener(this);
        btnBankAndSchemeUpdate.setOnClickListener(this);
        // btnSkipPayment.setOnClickListener(this);
        //btnUpdateScheme.setOnClickListener(this);
        tvEquSchemeTermCondition.setOnClickListener(this);
        tvEquValuePackTermCondition.setOnClickListener(this);
        tvCommSchemeTermCondition.setOnClickListener(this);
        // tvMFReviewForm.setOnClickListener(this);

       /* cbEquCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strcbEqu = "Current";
                    cbEquCurrent.setChecked(true);
                    cbEquSaving.setChecked(false);
                    if (cbSameAsEquity.isChecked()) {
                        strcbComm = "Current";
                        cbCommCurrent.setChecked(true);
                        cbCommSaving.setChecked(false);
                    }
                } else {
                    strcbEqu = "Saving";
                    cbEquCurrent.setChecked(false);
                    cbEquSaving.setChecked(true);
                }
            }
        });*/
        rbEquCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbEquCurrent.isChecked()) {
                    strcbEqu = "Current";
                    rbEquCurrent.setChecked(true);
                    rbEquSaving.setChecked(false);
                    if (cbSameAsEquity.isChecked()) {
                        strcbComm = "Current";
                        rbCommCurrent.setChecked(true);
                        rbCommSaving.setChecked(false);
                    }
                } else {
                    strcbEqu = "Saving";
                    rbEquCurrent.setChecked(false);
                    rbEquSaving.setChecked(true);
                }
            }
        });
      /*  cbMFSaving.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbMFSaving.setChecked(true);
                    cbMFCurrent.setChecked(false);
                } else {
                    cbMFSaving.setChecked(false);
                    cbMFCurrent.setChecked(true);
                }
            }
        });
        cbMFCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbMFSaving.setChecked(false);
                    cbMFCurrent.setChecked(true);
                } else {
                    cbMFSaving.setChecked(true);
                    cbMFCurrent.setChecked(false);
                }
            }
        });*/
       /* cbCommCurrent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strcbComm = "Current";
                    cbCommCurrent.setChecked(true);
                    cbCommSaving.setChecked(false);
                } else {
                    strcbComm = "Saving";
                    cbCommCurrent.setChecked(false);
                    cbCommSaving.setChecked(true);
                }
            }
        });*/
        rbCommCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbCommCurrent.isChecked()) {
                    strcbComm = "Current";
                    rbCommCurrent.setChecked(true);
                    rbCommSaving.setChecked(false);
                } else {
                    strcbComm = "Saving";
                    rbCommCurrent.setChecked(false);
                    rbCommSaving.setChecked(true);
                }
            }
        });
       /* cbEquSaving.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    strcbEqu = "Saving";
                    cbEquCurrent.setChecked(false);
                    cbEquSaving.setChecked(true);
                    if (cbSameAsEquity.isChecked()) {
                        strcbComm = "Saving";
                        cbCommCurrent.setChecked(false);
                        cbCommSaving.setChecked(true);
                    }
                } else {
                    strcbEqu = "Current";
                    cbEquCurrent.setChecked(true);
                    cbEquSaving.setChecked(false);
                }
            }
        });*/
        rbEquSaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbEquSaving.isChecked()) {
                    strcbEqu = "Saving";
                    rbEquCurrent.setChecked(false);
                    rbEquSaving.setChecked(true);
                    if (cbSameAsEquity.isChecked()) {
                        strcbComm = "Saving";
                        rbCommCurrent.setChecked(false);
                        rbCommSaving.setChecked(true);
                    }
                } else {
                    strcbEqu = "Current";
                    rbEquCurrent.setChecked(true);
                    rbEquSaving.setChecked(false);
                }
            }
        });
      /*  cbCommSaving.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strcbComm = "Saving";
                    cbCommCurrent.setChecked(false);
                    cbCommSaving.setChecked(true);
                } else {
                    strcbComm = "Current";
                    cbCommCurrent.setChecked(true);
                    cbCommSaving.setChecked(false);
                }
            }
        });*/
        rbCommSaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbCommSaving.isChecked()) {
                    strcbComm = "Saving";
                    rbCommCurrent.setChecked(false);
                    rbCommSaving.setChecked(true);
                } else {
                    strcbComm = "Current";
                    rbCommCurrent.setChecked(true);
                    rbCommSaving.setChecked(false);
                }
            }
        });
        cbSameAsEquity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!isBlank(tvEquityBankName.getText().toString())
                        && !isBlank(etEquityAccNumber.getText().toString())
                        && spEquBankProof.getSelectedItem() != null
                        && spEquPhotoProof.getSelectedItem() != null
                        && spEquSelectBank.getSelectedItem() != null) {
                        disableField(llCommodityBankDetail);
                        cbSameAsEquity.setEnabled(true);
                    } else {
                        cbSameAsEquity.setChecked(false);
                        return;
                    }
                    if (rbEquCurrent.isChecked()) {
                        rbCommCurrent.setChecked(true);
                        rbCommSaving.setChecked(false);
                        strcbComm = "Current";
                    } else if (rbEquSaving.isChecked()) {
                        rbCommCurrent.setChecked(false);
                        rbCommSaving.setChecked(true);
                        strcbComm = "Saving";
                    }
                    tvCommBankName.setText(tvEquityBankName.getText().toString());
                    tvCommBranchName.setText(tvEquityBranchName.getText().toString());
                    tvCommMICR.setText(tvEquityMICR.getText().toString());
                    tvCommIFSC.setText(tvEquityIFSC.getText().toString());
                    tvCommBankAddress.setText(tvEquityBankAddress.getText().toString());
                    if (spEquBankProof.getSelectedItem() != null) {
                        int corrSelectedbankProofPos =
                            ((RowData) spEquBankProof.getSelectedItem()).getPosition();
                        spCommBankProof.setSelection(corrSelectedbankProofPos);
                    }
                    if (spEquPhotoProof.getSelectedItem() != null) {
                        int corrSelectedPhotoPos =
                            ((RowData) spEquPhotoProof.getSelectedItem()).getPosition();
                        spCommPhotoProof.setSelection(corrSelectedPhotoPos);
                    }
                    if (spEquSelectBank.getSelectedItem() != null) {
                        int corrSelectedBankPos =
                            ((RowData) spEquSelectBank.getSelectedItem()).getPosition();
                        spCommSelectBank.setSelection(corrSelectedBankPos);
                    }
                    etCommAccNumber.setText(etEquityAccNumber.getText().toString());
                    etConfirmCommAccNumber.setText(etEquityAccNumber.getText().toString());
                } else {
                    spCommBankProof.setSelection(0);
                    spCommPhotoProof.setSelection(0);
                    spCommSelectBank.setSelection(0);
                    tvCommBankName.setText("");
                    tvCommBranchName.setText("");
                    tvCommMICR.setText("");
                    tvCommIFSC.setText("");
                    tvCommBankAddress.setText("");
                    etCommAccNumber.setText("");
                    etConfirmCommAccNumber.setText("");
                    rbCommCurrent.setChecked(false);
                    rbCommSaving.setChecked(true);
                    enableField(llCommodityBankDetail);
                }
            }
        });

        cbSameAsMCX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (cbSameAsMCX.isChecked()) {
                        isSameAsMCXCheck = true;
                        if (!(arrAdapBrokerage == null)) {
                            //                        getSpinnerData(arrAdapBrokerage, spNCXBrokerageScheme, spCommBrokerageScheme.getSelectedItem().toString());
                            RowData rdBrokSchNCX = getSpinnerRowDataById(
                                ((RowData) spCommBrokerageScheme.getSelectedItem()).getKey(),
                                arrAdapBrokerage);

                            if (rdBrokSchNCX != null) {
                                spNCXBrokerageScheme.setSelection(rdBrokSchNCX.getPosition());
                            } else {
                                commonMsgDialog(mContext, NO_VALUE_FOR_BROKERAGE_SCHEME, false, "");
                            }
                            if (!(arrAdapFuturesNCXCOMM == null) && (spCommFuture.getSelectedItem()
                                != null)) {
                                RowData rdFutNCX = getSpinnerRowDataById(
                                    ((RowData) spCommFuture.getSelectedItem()).getKey(),
                                    arrAdapFuturesCOMM);
                                if (rdFutNCX != null) {
                                    RowData rdCommNCXFutScheme =
                                        RowData.getSpinnerRowDataByValue(String.valueOf(rdFutNCX),
                                            arrAdapFuturesNCXCOMM);

                                    if (rdCommNCXFutScheme != null) {
                                        spNCXFuture.setSelection(rdCommNCXFutScheme.getPosition());
                                    } else {
                                        //                                        commonMsgDialog(mContext, NO_VALUE_FOR_FUTURE, false, "");
                                        spNCXFuture.setSelection(0);
                                    }
                                }
                            } else {
                                //                                commonMsgDialog(mContext, NO_VALUE_FOR_FUTURE, false, "");
                            }
                            if (!(arrAdapOptionsNCXCOMM == null) && (spCommOption.getSelectedItem()
                                != null)) {
                                RowData rdOptNCX = getSpinnerRowDataById(
                                    ((RowData) spCommOption.getSelectedItem()).getKey(),
                                    arrAdapOptionsCOMM);
                                if (rdOptNCX != null) {
                                    RowData rdCommNCXOptScheme =
                                        RowData.getSpinnerRowDataByValue(String.valueOf(rdOptNCX),
                                            arrAdapOptionsNCXCOMM);
                                    if (rdCommNCXOptScheme != null) {
                                        spNCXOption.setSelection(rdOptNCX.getPosition());
                                    } else {
                                        spNCXOption.setSelection(0);
                                        //                                        commonMsgDialog(mContext, NO_VALUE_FOR_OPTION, false, "");
                                    }
                                }
                            } else {
                                //                                commonMsgDialog(mContext, NO_VALUE_FOR_OPTION, false, "");
                            }
                            if (!(arrAdapDelNCXCOMM == null) && (spCommDPAMC.getSelectedItem()
                                != null)) {
                                RowData rdDelNCX = getSpinnerRowDataById(
                                    ((RowData) spCommDPAMC.getSelectedItem()).getKey(),
                                    arrAdapDeliveryCOMM);
                                if (rdDelNCX != null) {
                                    RowData rdCommNCXDelScheme =
                                        RowData.getSpinnerRowDataByValue(String.valueOf(rdDelNCX),
                                            arrAdapDelNCXCOMM);
                                    if (rdCommNCXDelScheme != null) {
                                        spNCXDPAMC.setSelection(rdCommNCXDelScheme.getPosition());
                                    } else {
                                        spNCXDPAMC.setSelection(0);
                                        //                                        commonMsgDialog(mContext, NO_VALUE_FOR_DPAMC, false, "");
                                    }
                                }
                            } else {
                                //                                commonMsgDialog(mContext, NO_VALUE_FOR_DPAMC, false, "");
                            }
                        }
                    } else {
                        isSameAsMCXCheck = false;
                        spNCXBrokerageScheme.setSelection(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        llBankBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llBankDetailForm.setVisibility(View.VISIBLE);
                llFundTransfer.setVisibility(View.GONE);
                if ((isEquityOriginal() && isCommodityOriginal()) || isAllOriginal()) {
                    showBothBankDetails();
                } else if (isEquityOriginal() || isDerivativeOriginal()) {
                    showEquityBankDetails();
                } else if (isCommodityOriginal()) {
                    showCommdityBankDetails();
                } else if (getInterestedInMF()) {
                    showMFBankDetails();
                }

                getSchemeDetails(getAppType(), userID);
            }
        });

        btnGetEquScheme.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (etEquMarginDeposit.getText().toString().equals("")) {
                    etEquMarginDepositError.setVisibility(View.VISIBLE);
                    etEquMarginDepositError.setText(
                        getAppType().equalsIgnoreCase(DIRECT) ? getResources().getString(
                            R.string.margin_25000)
                            : getResources().getString(R.string.margin_50000));
                    //                    btnGetEquScheme.setBackgroundColor(getResources().getColor(R.color.light_grey_menu));
                    //                    btnGetEquScheme.setEnabled(false);
                } else if (Integer.parseInt(etEquMarginDeposit.getText().toString())
                    < minMarginValueForOtherAmount()) {
                    etEquMarginDepositError.setVisibility(View.VISIBLE);
                    etEquMarginDepositError.setText(
                        getAppType().equalsIgnoreCase(DIRECT) ? getResources().getString(
                            R.string.margin_25000)
                            : getResources().getString(R.string.margin_50000));
                    //                    btnGetEquScheme.setBackgroundColor(getResources().getColor(R.color.light_grey_menu));
                    //                    btnGetEquScheme.setEnabled(false);
                } else {
                    etEquMarginDepositError.setVisibility(View.GONE);
                    removeKeyboard(mContext, btnGetEquScheme);
                    isOtherAmountSubmitted = false;
                    getOtherEquSchDetail();
                    if (isCommodityOriginal()) {
                        etCommMarginDeposit.setText(etEquMarginDeposit.getText().toString());
                        etCommMarginDepositError.setVisibility(View.GONE);
                        getOtherCommSchDetail();
                    }
                }

            }
        });
        btnGetCommScheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etCommMarginDeposit.getText().toString().equals("")) {
                    etCommMarginDepositError.setVisibility(View.VISIBLE);
                    etCommMarginDepositError.setText(
                        getAppType().equalsIgnoreCase(DIRECT) ? getResources().getString(
                            R.string.margin_25000)
                            : getResources().getString(R.string.margin_50000));
                    //                    btnGetCommScheme.setBackgroundColor(getResources().getColor(R.color.light_grey_menu));
                    //                    btnGetCommScheme.setEnabled(false);
                } else if (Integer.parseInt(etCommMarginDeposit.getText().toString())
                    < minMarginValueForOtherAmount()) {
                    etCommMarginDepositError.setVisibility(View.VISIBLE);
                    etCommMarginDepositError.setText(
                        getAppType().equalsIgnoreCase(DIRECT) ? getResources().getString(
                            R.string.margin_25000)
                            : getResources().getString(R.string.margin_50000));
                    //                    btnGetCommScheme.setBackgroundColor(getResources().getColor(R.color.light_grey_menu));
                    //                    btnGetCommScheme.setEnabled(false);
                } else {
                    etCommMarginDepositError.setVisibility(View.GONE);
                    removeKeyboard(mContext, btnGetCommScheme);
                    getOtherCommSchDetail();
                }

            }
        });

        etEquityAccNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr =
                    ValidationRule.trimFullWithMid(etEquityAccNumber.getText().toString());
                if (!isBlank(etEquityAccNumber.getText().toString())) {
                    input_layout_equityAccNumber.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etEquityAccNumber.setText("");
                        etEquityAccNumber.append(newStr);
                    } else if (etEquityAccNumber.length() < 4) {
                        etEquityAccNumberError.setVisibility(View.VISIBLE);
                        etEquityAccNumberError.setText(ErrorMsgAccountNoLength);
                        etEquityAccNumber.requestFocus();
                    } else if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
                        etEquityAccNumber.getText().toString().trim())) {
                        etEquityAccNumberError.setVisibility(View.VISIBLE);
                        etEquityAccNumberError.setText(ErrorMsgAccountNo);
                        etEquityAccNumber.requestFocus();
                    } else if (ValidationRule.validateOnlyAlphabetWithoutSplChar(
                        etEquityAccNumber.getText().toString().trim())) {
                        etEquityAccNumberError.setVisibility(View.VISIBLE);
                        etEquityAccNumberError.setText(ErrorMsgAccountNo);
                        etEquityAccNumber.requestFocus();
                    } else {
                        etEquityAccNumberError.setVisibility(View.GONE);
                    }
                } else {
                    etEquityAccNumberError.setVisibility(View.GONE);
                    input_layout_equityAccNumber.setErrorEnabled(false);
                }
            }
        });

        etCommAccNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (!isBlank(etCommAccNumber.getText().toString())) {
                    input_layout_commAccNumber.setErrorEnabled(false);
                    if (etCommAccNumber.length() < 4) {
                        etCommAccNumberError.setVisibility(View.VISIBLE);
                        etCommAccNumberError.setText(ErrorMsgAccountNoLength);
                        etCommAccNumber.requestFocus();
                    } else {
                        etCommAccNumberError.setVisibility(View.GONE);
                    }
                } else {
                    etCommAccNumberError.setVisibility(View.GONE);
                    input_layout_commAccNumber.setErrorEnabled(false);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr =
                    ValidationRule.trimFullWithMid(etCommAccNumber.getText().toString());
                if (!isBlank(etCommAccNumber.getText().toString())) {
                    input_layout_commAccNumber.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etCommAccNumber.setText("");
                        etCommAccNumber.append(newStr);
                    } else if (etCommAccNumber.length() < 4) {
                        etCommAccNumberError.setVisibility(View.VISIBLE);
                        etCommAccNumberError.setText(ErrorMsgAccountNoLength);
                        etCommAccNumber.requestFocus();
                    } else if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
                        etCommAccNumber.getText().toString().trim())) {
                        etCommAccNumberError.setVisibility(View.VISIBLE);
                        etCommAccNumberError.setText(ErrorMsgAccountNo);
                        etCommAccNumber.requestFocus();
                    } else if (ValidationRule.validateOnlyAlphabetWithoutSplChar(
                        etCommAccNumber.getText().toString().trim())) {
                        etCommAccNumberError.setVisibility(View.VISIBLE);
                        etCommAccNumberError.setText(ErrorMsgAccountNo);
                        etCommAccNumber.requestFocus();
                    } else {
                        etCommAccNumberError.setVisibility(View.GONE);
                    }
                } else {
                    etCommAccNumberError.setVisibility(View.GONE);
                    input_layout_commAccNumber.setErrorEnabled(false);
                }
            }
        });

        etMFAccNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*if (!isBlank(etMFAccNumber.getText().toString())) {
                    input_layout_MFAccNumber.setErrorEnabled(false);
                    if (etMFAccNumber.length() < 4) {
                        etMFAccNumberError.setVisibility(View.VISIBLE);
                        etMFAccNumberError.setText(ErrorMsgAccountNoLength);
                        etMFAccNumber.requestFocus();
                    } else {
                        etMFAccNumberError.setVisibility(View.GONE);
                    }
                } else {
                    etMFAccNumberError.setVisibility(View.GONE);
                    input_layout_MFAccNumber.setErrorEnabled(false);
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFullWithMid(etMFAccNumber.getText().toString());
                if (!isBlank(etMFAccNumber.getText().toString())) {
                    input_layout_MFAccNumber.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etMFAccNumber.setText("");
                        etMFAccNumber.append(newStr);
                    } else if (etMFAccNumber.length() < 4) {
                        etMFAccNumberError.setVisibility(View.VISIBLE);
                        etMFAccNumberError.setText(ErrorMsgAccountNoLength);
                        etMFAccNumber.requestFocus();
                    } else if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
                        etMFAccNumber.getText().toString().trim())) {
                        etMFAccNumberError.setVisibility(View.VISIBLE);
                        etMFAccNumberError.setText(ErrorMsgAccountNo);
                        etMFAccNumber.requestFocus();
                    } else if (ValidationRule.validateOnlyAlphabetWithoutSplChar(
                        etMFAccNumber.getText().toString().trim())) {
                        etMFAccNumberError.setVisibility(View.VISIBLE);
                        etMFAccNumberError.setText(ErrorMsgAccountNo);
                        etMFAccNumber.requestFocus();
                    } else {
                        etMFAccNumberError.setVisibility(View.GONE);
                    }
                } else {
                    etMFAccNumberError.setVisibility(View.GONE);
                    input_layout_MFAccNumber.setErrorEnabled(false);
                }
            }
        });

        etConfirmEquityAccNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr =
                    ValidationRule.trimFullWithMid(etConfirmEquityAccNumber.getText().toString());
                etConfirmEquityAccNumberError.setVisibility(View.GONE);
            }
        });

        etConfirmCommAccNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr =
                    ValidationRule.trimFullWithMid(etConfirmCommAccNumber.getText().toString());
                etConfirmCommAccNumberError.setVisibility(View.GONE);
            }
        });

        etConfirmMFAccNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr =
                    ValidationRule.trimFullWithMid(etConfirmMFAccNumber.getText().toString());
                etConfirmMFAccNumberError.setVisibility(View.GONE);
            }
        });
    }

    public void getOtherCommSchDetail() {
        if (MOSLCommon.isNetworkEnabled(mContext) && !isBlank(
            etCommMarginDeposit.getText().toString())) {
            Map inputMap = new HashMap();
            inputMap.put("_Amount", etCommMarginDeposit.getText().toString());
            inputMap.put("_Segment", "Commodity");
            inputMap.put("_userId", getUserID());
            inputMap.put("_userType", getAppType());

            String flag = GETDETAILSBYOTHERAMOUNT;
            String category = SCHEMEDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {

                            try {
                                    /*{
                                        "AdditionalSchemeInfoCOMM":[
                                        {
                                            "Mapping_Id":1,
                                                "Amount":10000,
                                                "Delivery":0.5,
                                                "Intraday1stLegMax":0.03,
                                                "Intraday2ndLegMax":0.03,
                                                "FuturesMinPaisa":3
                                        }
                                        ]
                                    }*/

                                JSONArray jsonArray =
                                    response.getJSONArray("AdditionalSchemeInfoCOMM");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String delivery = object.getString("Delivery");
                                        BigDecimal bdDelivery = new BigDecimal(delivery);
                                        String intraday = object.getString("Intraday1stLegMax");
                                        BigDecimal bdIntraday = new BigDecimal(intraday);

                                        tvCommDelBrok.setText(String.valueOf(bdDelivery));
                                        tvCommIntradayBrok.setText(String.valueOf(bdIntraday));
                                    }
                                    disableEnableField();
                                }

                                customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

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
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });

        } else {
            if (!MOSLCommon.isNetworkEnabled(mContext)) {
                MOSLCommon.alertDInternet(mContext);
            }
            customProgress.setVisibility(View.GONE);
        }
    }

    public void getOtherEquSchDetail() {
        if (MOSLCommon.isNetworkEnabled(mContext) && !isBlank(
            etEquMarginDeposit.getText().toString())) {
            //            if (isOtherAmountSubmitted)
            //                return;
            Map inputMap = new HashMap();
            inputMap.put("_Amount", etEquMarginDeposit.getText().toString());
            inputMap.put("_Segment", "Equity");
            inputMap.put("_userId", getUserID());
            inputMap.put("_userType", getAppType());

            String flag = GETDETAILSBYOTHERAMOUNT;
            String category = SCHEMEDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response.length() != 0) {
                                        /*"Mapping_Id":1,
                                        "Range_Id":1,
                                        "Intraday1stLeg":0.05,
                                        "Intraday1stLegMinPaise":5,
                                        "Intraday2ndLeg":0.05,
                                        "Intraday2ndLegMinPaise":5,
                                        "Delivery":0.5,
                                        "Futures1stLeg":0.05,
                                        "Futures1stLegMinPaise":5,
                                        "Futures2ndLeg":0.05,
                                        "Futures2ndLegMinPaise":5,
                                        "Options":100,
                                        "CurrencyFutures":20,
                                        "CurrencyOptions":20,
                                        "Created_By":1,
                                        "Created_Date":"2014-06-06T11:52:19.273"*/

                            try {
                                JSONArray jsonArray =
                                    response.getJSONArray("AdditionalSchemeInfoEQ");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String accountOpeningFee =
                                            (object.isNull("AccountOpeningFee") || object.getString(
                                                "AccountOpeningFee").equals("")) ? ""
                                                : object.getString("AccountOpeningFee");
                                        //                                    BigDecimal bdAccountOpeningFee = new BigDecimal(accountOpeningFee);
                                        String softwareCharges =
                                            (object.isNull("SoftwareCharges") || object.getString(
                                                "SoftwareCharges").equals("")) ? ""
                                                : object.getString("SoftwareCharges");
                                        //                                    BigDecimal bdSoftwareCharges = new BigDecimal(softwareCharges);
                                        String intradayCashSegment1stLegMAX =
                                            (object.isNull("Intraday1stLeg") || object.getString(
                                                "Intraday1stLeg").equals("")) ? ""
                                                : object.getString("Intraday1stLeg");
                                        BigDecimal bdIntradayCashSegment1stLegMAX =
                                            new BigDecimal(intradayCashSegment1stLegMAX);
                                        String intradayCashSegment2ndLegMAX =
                                            (object.isNull("Intraday2ndLeg") || object.getString(
                                                "Intraday2ndLeg").equals("")) ? ""
                                                : object.getString("Intraday2ndLeg");
                                        BigDecimal bdIntradayCashSegment2ndLegMAX =
                                            new BigDecimal(intradayCashSegment2ndLegMAX);
                                        String deliveryBrokerageStandard =
                                            (object.isNull("Delivery") || object.getString(
                                                "Delivery").equals("")) ? ""
                                                : object.getString("Delivery");
                                        BigDecimal bdDeliveryBrokerageStandard =
                                            new BigDecimal(deliveryBrokerageStandard);
                                        String intradayFutures1stLegMAX =
                                            (object.isNull("Futures1stLeg") || object.getString(
                                                "Futures1stLeg").equals("")) ? ""
                                                : object.getString("Futures1stLeg");
                                        BigDecimal bdIntradayFutures1stLegMAX =
                                            new BigDecimal(intradayFutures1stLegMAX);
                                        String intradayFutures2ndLegMAX =
                                            (object.isNull("Intraday2ndLeg") || object.getString(
                                                "Intraday2ndLeg").equals("")) ? ""
                                                : object.getString("Intraday2ndLeg");
                                        BigDecimal bdIntradayFutures2ndLegMAX =
                                            new BigDecimal(intradayFutures2ndLegMAX);
                                        String options1stLegAnd2ndLegINR =
                                            (object.isNull("Options") || object.getString("Options")
                                                .equals("")) ? "" : object.getString("Options");
                                        BigDecimal bdOptions1stLegAnd2ndLegINR =
                                            new BigDecimal(options1stLegAnd2ndLegINR);
                                        String currencyFut1st2ndLegINR =
                                            (object.isNull("CurrencyFutures") || object.getString(
                                                "CurrencyFutures").equals("")) ? ""
                                                : object.getString("CurrencyFutures");
                                        BigDecimal bdCurrencyFut1st2ndLegINR =
                                            new BigDecimal(currencyFut1st2ndLegINR);
                                        String currencyOptions =
                                            (object.isNull("CurrencyOptions") || object.getString(
                                                "CurrencyOptions").equals("")) ? ""
                                                : object.getString("CurrencyOptions");
                                        BigDecimal bdCurrencyOptions =
                                            new BigDecimal(currencyOptions);
                                        String dpAmc =
                                            (object.isNull("DPAMC") || object.getString("DPAMC")
                                                .equals("")) ? "" : object.getString("DPAMC");

                                        //                                    tvequSchInfoAccOpenCharge.setText(String.valueOf(bdAccountOpeningFee));
                                        //                                    tvequSchInfoSoftCharge.setText(String.valueOf(bdSoftwareCharges));
                                        tvequSchInfoAccOpenCharge.setText(accountOpeningFee);
                                        tvequSchInfoSoftCharge.setText(softwareCharges);
                                        tvequSchInfoIntraday1stLeg.setText(
                                            String.valueOf(bdIntradayCashSegment1stLegMAX));
                                        tvequSchInfoIntraday2ndLeg.setText(
                                            String.valueOf(bdIntradayCashSegment2ndLegMAX));
                                        tvequSchInfoDelivery.setText(
                                            String.valueOf(bdDeliveryBrokerageStandard));
                                        tvequSchInfoFutures1stLeg.setText(
                                            String.valueOf(bdIntradayFutures1stLegMAX));
                                        tvequSchInfoFutures2ndLeg.setText(
                                            String.valueOf(bdIntradayFutures2ndLegMAX));
                                        tvequSchInfoOptions.setText(
                                            String.valueOf(bdOptions1stLegAnd2ndLegINR));
                                        tvequSchInfoCurrFuture.setText(
                                            String.valueOf(bdCurrencyFut1st2ndLegINR));
                                        tvequSchInfoCurrOption.setText(
                                            String.valueOf(bdCurrencyOptions));
                                        tvEquAccOpenNote.setText("DP-AMC " + dpAmc);

                                    }
                                    disableEnableField();
                                }

                                if (response.has("CurrencyFuture")) {
                                    try {
                                        jsonArray = response.getJSONArray("CurrencyFuture");
                                        SchemeDetailsModel detailsModel;
                                        if (jsonArray.length() != 0) {
                                            modelList = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                detailsModel = new SchemeDetailsModel();
                                                detailsModel.setBrok_TEXT(
                                                    (object.isNull("BROK_TEXT") || object.getString(
                                                        "BROK_TEXT").equals("")) ? ""
                                                        : object.getString("BROK_TEXT").trim());
                                                detailsModel.setBrok_VAL(
                                                    (object.isNull("BROK_VAL") || object.getString(
                                                        "BROK_VAL").equals("")) ? ""
                                                        : object.getString("BROK_VAL").trim());
                                                detailsModel.setCurr_TYPE(
                                                    (object.isNull("CURR_TYPE") || object.getString(
                                                        "CURR_TYPE").equals("")) ? ""
                                                        : object.getString("CURR_TYPE").trim());
                                                detailsModel.setCurrFutureSelectedVal(
                                                    (object.isNull("CURRFutureSelectedVal")
                                                        || object.getString("CURRFutureSelectedVal")
                                                        .equals("")) ? ""
                                                        : object.getString("CURRFutureSelectedVal")
                                                            .trim());
                                                detailsModel.setCurrFutureShow(
                                                    (object.isNull("CURRFutureShow")
                                                        || object.getString("CURRFutureShow")
                                                        .equals("")) ? ""
                                                        : object.getString("CURRFutureShow")
                                                            .trim());
                                                modelList.add(detailsModel);
                                            }
                                            currencyFutureShowRadioButton(modelList);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    currencyfutureRadiogroup.setVisibility(View.GONE);
                                }
                                if (response.has("CurrencyOption")) {
                                    try {
                                        jsonArray = response.getJSONArray("CurrencyOption");
                                        SchemeDetailsModel currencyOptionModel;

                                        if (jsonArray.length() != 0) {
                                            currencyOptionmodelList = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                currencyOptionModel = new SchemeDetailsModel();
                                                currencyOptionModel.setBrok_TEXT(
                                                    (object.isNull("BROK_TEXT") || object.getString(
                                                        "BROK_TEXT").equals("")) ? ""
                                                        : object.getString("BROK_TEXT").trim());
                                                currencyOptionModel.setBrok_VAL(
                                                    (object.isNull("BROK_VAL") || object.getString(
                                                        "BROK_VAL").equals("")) ? ""
                                                        : object.getString("BROK_VAL").trim());
                                                currencyOptionModel.setCurr_TYPE(
                                                    (object.isNull("CURR_TYPE") || object.getString(
                                                        "CURR_TYPE").equals("")) ? ""
                                                        : object.getString("CURR_TYPE").trim());
                                                currencyOptionModel.setCurrOptionSelectedVal(
                                                    (object.isNull("CURROptionSelectedVal")
                                                        || object.getString("CURROptionSelectedVal")
                                                        .equals("")) ? ""
                                                        : object.getString("CURROptionSelectedVal")
                                                            .trim());
                                                currencyOptionModel.setCurrOptionShow(
                                                    (object.isNull("CURROptionShow")
                                                        || object.getString("CURROptionShow")
                                                        .equals("")) ? ""
                                                        : object.getString("CURROptionShow")
                                                            .trim());
                                                currencyOptionmodelList.add(currencyOptionModel);
                                            }
                                            currOptionShowRadioButton(currencyOptionmodelList);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    currencyoptionRadiogroup.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false,
                                    "");
                                //                            customProgress.setVisibility(View.GONE);
                            }
                        } else {
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
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
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });

        } else {
            if (!MOSLCommon.isNetworkEnabled(mContext)) {
                MOSLCommon.alertDInternet(mContext);
            }
            if (isBlank(etEquMarginDeposit.getText().toString())) {
                tvequSchInfoAccOpenCharge.setText("");
                tvequSchInfoSoftCharge.setText("");
                tvequSchInfoIntraday1stLeg.setText("");
                tvequSchInfoIntraday2ndLeg.setText("");
                tvequSchInfoDelivery.setText("");
                tvequSchInfoFutures1stLeg.setText("");
                tvequSchInfoFutures2ndLeg.setText("");
                tvequSchInfoOptions.setText("");
                tvequSchInfoCurrFuture.setText("");
                tvequSchInfoCurrOption.setText("");
                currencyoptionRadiogroup.setVisibility(View.GONE);
                currencyfutureRadiogroup.setVisibility(View.GONE);
            }
            customProgress.setVisibility(View.GONE);
        }
    }

    private void bindValuePackOnSelectedScheme(final String schemeID) {
        dialogValuePackScheme = new Dialog(mContext);
        dialogValuePackScheme.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogValuePackScheme.getWindow()
            .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogValuePackScheme.setContentView(R.layout.frag_value_pack_scheme);
        dialogValuePackScheme.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogValuePackScheme.setCanceledOnTouchOutside(false);
        final TextView tvSchemeName, tvUpfrontFees, tvDPAMC, tvEXECharges,
            tvValidity, tvMaxBrokerage, tvDeliveryYBrokerageMax, tvDeliveryYBrokerageMinPaise,
            tvFirstLegMax,
            tvSecondLegMax, tvCashMinFirstLeg, tvCashMinSecondLeg,
            tvFirstLegMaxFurure, tvSecondLegMaxFuture, tvFutMinFirstLeg, tvFutureCarried,
            tvOPTIONS, tvFirstSecondLeg, tvTermandCondition;
        tvSchemeName = (TextView) dialogValuePackScheme.findViewById(R.id.tvSchemeName);
        tvUpfrontFees = (TextView) dialogValuePackScheme.findViewById(R.id.tvUpfrontFees);
        tvDPAMC = (TextView) dialogValuePackScheme.findViewById(R.id.tvDPAMC);
        tvEXECharges = (TextView) dialogValuePackScheme.findViewById(R.id.tvEXECharges);
        tvValidity = (TextView) dialogValuePackScheme.findViewById(R.id.tvValidity);
        tvMaxBrokerage = (TextView) dialogValuePackScheme.findViewById(R.id.tvMaxBrokerage);
        tvDeliveryYBrokerageMax =
            (TextView) dialogValuePackScheme.findViewById(R.id.tvDeliveryYBrokerageMax);
        tvDeliveryYBrokerageMinPaise =
            (TextView) dialogValuePackScheme.findViewById(R.id.tvDeliveryYBrokerageMinPaise);

        tvFirstLegMax = (TextView) dialogValuePackScheme.findViewById(R.id.tvFirstLegMax);
        tvSecondLegMax = (TextView) dialogValuePackScheme.findViewById(R.id.tvSecondLegMax);
        tvCashMinFirstLeg = (TextView) dialogValuePackScheme.findViewById(R.id.tvCashMinFirstLeg);
        tvCashMinSecondLeg = (TextView) dialogValuePackScheme.findViewById(R.id.tvCashMinSecondLeg);
        tvFirstLegMaxFurure =
            (TextView) dialogValuePackScheme.findViewById(R.id.tvFirstLegMaxFurure);
        tvSecondLegMaxFuture =
            (TextView) dialogValuePackScheme.findViewById(R.id.tvSecondLegMaxFuture);
        tvFutMinFirstLeg = (TextView) dialogValuePackScheme.findViewById(R.id.tvFutMinFirstLeg);
        tvFutureCarried = (TextView) dialogValuePackScheme.findViewById(R.id.tvFutureCarried);
        tvOPTIONS = (TextView) dialogValuePackScheme.findViewById(R.id.tvOPTIONS);
        tvFirstSecondLeg = (TextView) dialogValuePackScheme.findViewById(R.id.tvFirstSecondLeg);
        tvTermandCondition = (TextView) dialogValuePackScheme.findViewById(R.id.tvTermandCondition);
        tvTermandCondition.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        ImageView btnClose = (ImageView) dialogValuePackScheme.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogValuePackScheme.dismiss();
            }
        });
        tvTermandCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog schemeEquTermConditionDialog = new Dialog(mContext);
                schemeEquTermConditionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                schemeEquTermConditionDialog.setContentView(R.layout.frag_term_and_condition);
                schemeEquTermConditionDialog.getWindow()
                    .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                schemeEquTermConditionDialog.setCanceledOnTouchOutside(false);
                Button btnClose = (Button) schemeEquTermConditionDialog.findViewById(R.id.btnClose);
                TextView tvTerms =
                    (TextView) schemeEquTermConditionDialog.findViewById(R.id.tvTerms);
                tvTerms.setText(
                    getResources().getString(R.string.terms_value_pack_equity_margin_scheme));
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        schemeEquTermConditionDialog.dismiss();
                    }
                });
                schemeEquTermConditionDialog.show();
            }
        });
        String productid = "";
        if (rbMarginAcc.isChecked()) {
            productid = "1";
        } else if (rbValuePacAcc.isChecked()) {
            productid = "2";
        }
        if (strEquSchKey.equalsIgnoreCase("-1")) {
            tvequSchValidity.setText("");
            tvequSchUpfrontFees.setText("");
            tvequSchDpAmc.setText("");
        } else {
            if (MOSLCommon.isNetworkEnabled(mContext)) {
                Map inputMap = new HashMap();
                inputMap.put("_SchemeID", schemeID);
                inputMap.put("_ProductID", productid);
                inputMap.put("_userId", getUserID());
                inputMap.put("_userType", getAppType());
                String flag = BINDDETAILSONSCHEMESELECTIONMARGINORVALUEPAC;
                String category = SCHEMEDETAILS;
                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, flag, category, inputMap,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONObject response) {
                            if (response != null && response.length() != 0) {
                      /*  {"SchemeDetails":
                            [{"MarginProductId":34,
                                "SchemeName":"Margin 100,000",
                                "MarginDepositINR":"100,000",
                                "DeliveryBrokerageStandard":"0.25%",
                                "DeliveryBrokerageMinPaise":"2",
                                "DeliveryBrokerageMinINR":"20",
                                "IntradayCashSegment1stLegMAX":"0.025%",
                                "IntradayCashSegment2ndLegMAX":"0.025%",
                                "IntradayCashSegmentCashMin1stLegPaise":"2",
                                "IntradayCashSegmentCashMin2ndLegPaise":"2",
                                "IntradayCashSegmentMinINR":"20",
                                "IntradayFutures1stLegMAX":"0.025%",
                                "IntradayFutures2ndLegMAX":"0.025%",
                                "IntradayFuturesIfCarriedForward":"2",
                                "IntradayFuturesFutMinPaise":"2",
                                "Options1stLegAnd2ndLegINR":"50",
                                "ServicesOfferedPlatform":"Web",
                                "ServicesOfferedDeliveryPlus":"Y",
                                "ServicesOfferedMobileAccess":"Y",
                                "ServicesOfferedSMSAlertsBuyAndSell":"Y",
                                "ServicesOfferedSMSAlertsAdvice":"N/A",
                                "ServicesOfferedCallNTrade":"Y",
                                "ServicesOfferedAdvisoryServices":"N/A",
                                "ServicesOfferedResearchReports":"Y",
                                "ServicesOfferedChartingTool":"N/A",
                                "CURRENCYFUT1ST2NDLEGINR":"20","CURRENCYOPTIONS":"20"}]}*/

                        /*"SchemeDetails":[{"ValuePackFee":25000.00,
                                "SchemeName":"NVP25KY",
                                "AccountOpeningFee":"Free",
                                "EXECharges":"N/A",
                                "ValidityDays":"365",
                                "MaxBrokerageReversedOnExpiryOfTheScheme":"25,000",
                                "DeliveryYBrokerageMax":"0.22%",
                                "DeliveryYBrokerageMinPaise":"3",
                                "IntradayBrokerage2ndLegMax":"0.02%",
                                "IntradayBrokerage1stLegMax":"0.02%",
                                "IntradayBrokerageCashMin1stLegPerSharePaise":"3",
                                "IntradayBrokerageCashMin2ndLegPerSharePaise":"3",
                                "FuturesBrokerage1stLegMAX":"0.04%",
                                "FuturesBrokerage2ndLegMAXIntraday":"0.01%",
                                "FuturesBrokerageIfCarriedForward":"0.04%",
                                "FuturesBrokerageFutMinPaise":"1",
                                "OptionsBrokerage1stLegAnd2ndLegEachINR":"50",
                                "ExePlatform":"N",
                                "WebPlatform":"Y",
                                "StaticPlatform":"Y",
                                "MobileAccessPlatform":"Y",
                                "SMSAlertsBbuyAndsell":"Y",
                                "SMSAlertsAdvice":"N",
                                "CallN_Trade":"Y",
                                "AdvisoryServices":"N",
                                "OnlineResearchReports":"Y",
                                "ChartingTool":"N"}]}*/


                                try {
                                    JSONArray jsonArray = response.getJSONArray("SchemeDetails");
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String Validitydays =
                                                (object.isNull("ValidityDays") || object.getString(
                                                    "ValidityDays").equals("")) ? ""
                                                    : object.getString("ValidityDays");
                                            String SchemeName =
                                                (object.isNull("SchemeName") || object.getString(
                                                    "SchemeName").equals("")) ? ""
                                                    : object.getString("SchemeName");
                                            String accountOpeningFee =
                                                (object.isNull("AccountOpeningFee")
                                                    || object.getString("AccountOpeningFee")
                                                    .equals("")) ? ""
                                                    : object.getString("AccountOpeningFee");
                                            String EXECharges =
                                                (object.isNull("EXECharges") || object.getString(
                                                    "EXECharges").equals("")) ? ""
                                                    : object.getString("EXECharges");
                                            String valuePackFee =
                                                (object.isNull("ValuePackFee") || object.getString(
                                                    "ValuePackFee").equals("")) ? ""
                                                    : object.getString("ValuePackFee");
                                            String MaxBrokerageReversedOnExpiryOfTheScheme =
                                                (object.isNull(
                                                    "MaxBrokerageReversedOnExpiryOfTheScheme") ||
                                                    object.getString(
                                                        "MaxBrokerageReversedOnExpiryOfTheScheme")
                                                        .equals("")) ? "" : object.getString(
                                                    "MaxBrokerageReversedOnExpiryOfTheScheme");
                                            String DeliveryYBrokerageMax =
                                                (object.isNull("DeliveryYBrokerageMax")
                                                    || object.getString("DeliveryYBrokerageMax")
                                                    .equals("")) ? ""
                                                    : object.getString("DeliveryYBrokerageMax");
                                            String DeliveryYBrokerageMinPaise =
                                                (object.isNull("DeliveryYBrokerageMinPaise")
                                                    || object.getString(
                                                    "DeliveryYBrokerageMinPaise").equals("")) ? ""
                                                    : object.getString(
                                                        "DeliveryYBrokerageMinPaise");
                                            String IntradayBrokerage2ndLegMax =
                                                (object.isNull("IntradayBrokerage2ndLegMax")
                                                    || object.getString(
                                                    "IntradayBrokerage2ndLegMax").equals("")) ? ""
                                                    : object.getString(
                                                        "IntradayBrokerage2ndLegMax");
                                            String IntradayBrokerage1stLegMax =
                                                (object.isNull("IntradayBrokerage1stLegMax")
                                                    || object.getString(
                                                    "IntradayBrokerage1stLegMax").equals("")) ? ""
                                                    : object.getString(
                                                        "IntradayBrokerage1stLegMax");
                                            String IntradayBrokerageCashMin1stLegPerSharePaise =
                                                (object.isNull(
                                                    "IntradayBrokerageCashMin1stLegPerSharePaise")
                                                    ||
                                                    object.getString(
                                                        "IntradayBrokerageCashMin1stLegPerSharePaise")
                                                        .equals("")) ? "" : object.getString(
                                                    "IntradayBrokerageCashMin1stLegPerSharePaise");
                                            String IntradayBrokerageCashMin2ndLegPerSharePaise =
                                                (object.isNull(
                                                    "IntradayBrokerageCashMin2ndLegPerSharePaise")
                                                    ||
                                                    object.getString(
                                                        "IntradayBrokerageCashMin2ndLegPerSharePaise")
                                                        .equals("")) ? "" : object.getString(
                                                    "IntradayBrokerageCashMin2ndLegPerSharePaise");
                                            String FuturesBrokerage1stLegMAX =
                                                (object.isNull("FuturesBrokerage1stLegMAX") ||
                                                    object.getString("FuturesBrokerage1stLegMAX")
                                                        .equals("")) ? ""
                                                    : object.getString("FuturesBrokerage1stLegMAX");
                                            String FuturesBrokerage2ndLegMAXIntraday =
                                                (object.isNull("FuturesBrokerage2ndLegMAXIntraday")
                                                    ||
                                                    object.getString(
                                                        "FuturesBrokerage2ndLegMAXIntraday")
                                                        .equals("")) ? "" : object.getString(
                                                    "FuturesBrokerage2ndLegMAXIntraday");
                                            String FuturesBrokerageIfCarriedForward =
                                                (object.isNull("FuturesBrokerageIfCarriedForward")
                                                    ||
                                                    object.getString(
                                                        "FuturesBrokerageIfCarriedForward")
                                                        .equals("")) ? "" : object.getString(
                                                    "FuturesBrokerageIfCarriedForward");
                                            String FuturesBrokerageFutMinPaise =
                                                (object.isNull("FuturesBrokerageFutMinPaise") ||
                                                    object.getString("FuturesBrokerageFutMinPaise")
                                                        .equals("")) ? "" : object.getString(
                                                    "FuturesBrokerageFutMinPaise");
                                            String OptionsBrokerage1stLegAnd2ndLegEachINR =
                                                (object.isNull(
                                                    "OptionsBrokerage1stLegAnd2ndLegEachINR") ||
                                                    object.getString(
                                                        "OptionsBrokerage1stLegAnd2ndLegEachINR")
                                                        .equals("")) ? "" : object.getString(
                                                    "OptionsBrokerage1stLegAnd2ndLegEachINR");
                                            String ExePlatform =
                                                (object.isNull("ExePlatform") || object.getString(
                                                    "ExePlatform").equals("")) ? ""
                                                    : object.getString("ExePlatform");
                                            String WebPlatform =
                                                (object.isNull("WebPlatform") || object.getString(
                                                    "WebPlatform").equals("")) ? ""
                                                    : object.getString("WebPlatform");
                                            String StaticPlatform = (object.isNull("StaticPlatform")
                                                || object.getString("StaticPlatform").equals(""))
                                                ? "" : object.getString("StaticPlatform");
                                            String MobileAccessPlatform =
                                                (object.isNull("MobileAccessPlatform")
                                                    || object.getString("MobileAccessPlatform")
                                                    .equals("")) ? ""
                                                    : object.getString("MobileAccessPlatform");
                                            String SMSAlertsBbuyAndsell =
                                                (object.isNull("SMSAlertsBbuyAndsell")
                                                    || object.getString("SMSAlertsBbuyAndsell")
                                                    .equals("")) ? ""
                                                    : object.getString("SMSAlertsBbuyAndsell");
                                            String SMSAlertsAdvice =
                                                (object.isNull("SMSAlertsAdvice")
                                                    || object.getString("SMSAlertsAdvice")
                                                    .equals("")) ? ""
                                                    : object.getString("SMSAlertsAdvice");
                                            String CallN_Trade =
                                                (object.isNull("CallN_Trade") || object.getString(
                                                    "CallN_Trade").equals("")) ? ""
                                                    : object.getString("CallN_Trade");
                                            String AdvisoryServices =
                                                (object.isNull("AdvisoryServices")
                                                    || object.getString("AdvisoryServices")
                                                    .equals("")) ? ""
                                                    : object.getString("AdvisoryServices");
                                            String OnlineResearchReports =
                                                (object.isNull("OnlineResearchReports")
                                                    || object.getString("OnlineResearchReports")
                                                    .equals("")) ? ""
                                                    : object.getString("OnlineResearchReports");
                                            String ChartingTool =
                                                (object.isNull("ChartingTool") || object.getString(
                                                    "ChartingTool").equals("")) ? ""
                                                    : object.getString("ChartingTool");
                                            String dpAmc =
                                                (object.isNull("DPAMC") || object.getString("DPAMC")
                                                    .equals("")) ? "" : object.getString("DPAMC");
                                            tvSchemeName.setText(SchemeName);
                                            tvUpfrontFees.setText(valuePackFee);
                                            tvDPAMC.setText(dpAmc);
                                            tvEXECharges.setText(EXECharges);
                                            tvValidity.setText(Validitydays);
                                            tvMaxBrokerage.setText(
                                                MaxBrokerageReversedOnExpiryOfTheScheme);
                                            tvDeliveryYBrokerageMax.setText(DeliveryYBrokerageMax);
                                            tvDeliveryYBrokerageMinPaise.setText(
                                                DeliveryYBrokerageMinPaise);
                                            tvFirstLegMax.setText(IntradayBrokerage1stLegMax);
                                            tvSecondLegMax.setText(IntradayBrokerage2ndLegMax);
                                            tvCashMinFirstLeg.setText(
                                                IntradayBrokerageCashMin1stLegPerSharePaise);
                                            tvCashMinSecondLeg.setText(
                                                IntradayBrokerageCashMin2ndLegPerSharePaise);
                                            tvFirstLegMaxFurure.setText(FuturesBrokerage1stLegMAX);
                                            tvSecondLegMaxFuture.setText(
                                                FuturesBrokerage2ndLegMAXIntraday);
                                            tvFutMinFirstLeg.setText(FuturesBrokerageFutMinPaise);
                                            tvFutureCarried.setText(
                                                FuturesBrokerageIfCarriedForward);
                                            tvFirstSecondLeg.setText(
                                                OptionsBrokerage1stLegAnd2ndLegEachINR);
                                            tvEquAccOpenNote.setText("DP-AMC " + dpAmc);

                                        }
                                        customProgress.setVisibility(View.GONE);
                                    } else {
                                        customProgress.setVisibility(View.GONE);

                                    }

                                    customProgress.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                customProgress.setVisibility(View.GONE);


                      /*  llNoResponse.setVisibility(View.VISIBLE);
                        lvLeads.setVisibility(View.GONE);
                        tvNoResponse.setText(NO_LEADS);*/
                            }
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                            String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }
                    });

            } else {
                MOSLCommon.alertDInternet(mContext);
                customProgress.setVisibility(View.GONE);
            }
        }
        dialogValuePackScheme.show();
    }

    private void bindEquDetailsOnSelectedScheme(final String schemeID) {
        dialogMarginScheme = new Dialog(mContext);
        dialogMarginScheme.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMarginScheme.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogMarginScheme.setContentView(R.layout.frag_margin_scheme);
        dialogMarginScheme.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogMarginScheme.setCanceledOnTouchOutside(false);
        final TextView tvSchemeName, tvMarginDeposit, tvDPAMC, tvBrokerage, tvDelevaryBrokerage,
            tvStandard, tvMinPaise, tvMinPerDay, tvIntradayCashSegment, tvFirstLegMax,
            tvSecondLegMax, tvCashMinFirstLeg, tvCashMinSecondLeg, tvMinPerDaySecond,
            tvIntradayFuture,
            tvFirstLegMaxFurure, tvSecondLegMaxFuture, tvFutMinFirstLeg, tvFutMinSecondLeg,
            tvOPTIONS, tvFirstSecondLeg, tvCurrency, tvFutureLeg, tvOptions, tvTermandCondition;
        tvSchemeName = (TextView) dialogMarginScheme.findViewById(R.id.tvSchemeName);
        tvMarginDeposit = (TextView) dialogMarginScheme.findViewById(R.id.tvMarginDeposit);
        tvDPAMC = (TextView) dialogMarginScheme.findViewById(R.id.tvDPAMC);
        tvBrokerage = (TextView) dialogMarginScheme.findViewById(R.id.tvBrokerage);
        tvDelevaryBrokerage = (TextView) dialogMarginScheme.findViewById(R.id.tvDelevaryBrokerage);
        tvStandard = (TextView) dialogMarginScheme.findViewById(R.id.tvStandard);
        tvMinPaise = (TextView) dialogMarginScheme.findViewById(R.id.tvMinPaise);
        tvMinPerDay = (TextView) dialogMarginScheme.findViewById(R.id.tvMinPerDay);
        tvIntradayCashSegment =
            (TextView) dialogMarginScheme.findViewById(R.id.tvIntradayCashSegment);
        tvFirstLegMax = (TextView) dialogMarginScheme.findViewById(R.id.tvFirstLegMax);
        tvSecondLegMax = (TextView) dialogMarginScheme.findViewById(R.id.tvSecondLegMax);
        tvCashMinFirstLeg = (TextView) dialogMarginScheme.findViewById(R.id.tvCashMinFirstLeg);
        tvCashMinSecondLeg = (TextView) dialogMarginScheme.findViewById(R.id.tvCashMinSecondLeg);
        tvMinPerDaySecond = (TextView) dialogMarginScheme.findViewById(R.id.tvMinPerDaySecond);
        tvIntradayFuture = (TextView) dialogMarginScheme.findViewById(R.id.tvIntradayFuture);
        tvFirstLegMaxFurure = (TextView) dialogMarginScheme.findViewById(R.id.tvFirstLegMaxFurure);
        tvSecondLegMaxFuture =
            (TextView) dialogMarginScheme.findViewById(R.id.tvSecondLegMaxFuture);
        tvFutMinFirstLeg = (TextView) dialogMarginScheme.findViewById(R.id.tvFutMinFirstLeg);
        tvFutMinSecondLeg = (TextView) dialogMarginScheme.findViewById(R.id.tvFutMinSecondLeg);
        tvOPTIONS = (TextView) dialogMarginScheme.findViewById(R.id.tvOPTIONS);
        tvFirstSecondLeg = (TextView) dialogMarginScheme.findViewById(R.id.tvFirstSecondLeg);
        tvCurrency = (TextView) dialogMarginScheme.findViewById(R.id.tvCurrency);
        tvFutureLeg = (TextView) dialogMarginScheme.findViewById(R.id.tvFutureLeg);
        tvOptions = (TextView) dialogMarginScheme.findViewById(R.id.tvOptions);
        tvTermandCondition = (TextView) dialogMarginScheme.findViewById(R.id.tvTermandCondition);
        tvTermandCondition.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        ImageView btnClose = (ImageView) dialogMarginScheme.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMarginScheme.dismiss();
            }
        });
        tvTermandCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog schemeEquTermConditionDialog = new Dialog(mContext);
                schemeEquTermConditionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                schemeEquTermConditionDialog.setContentView(R.layout.frag_term_and_condition);
                schemeEquTermConditionDialog.getWindow()
                    .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                schemeEquTermConditionDialog.setCanceledOnTouchOutside(false);
                Button btnClose = (Button) schemeEquTermConditionDialog.findViewById(R.id.btnClose);
                TextView tvTerms =
                    (TextView) schemeEquTermConditionDialog.findViewById(R.id.tvTerms);
                tvTerms.setText(getResources().getString(R.string.terms_equity_margin_scheme));
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        schemeEquTermConditionDialog.dismiss();
                    }
                });
                schemeEquTermConditionDialog.show();
            }
        });
        String productid = "1";
        if (strEquSchKey.equalsIgnoreCase("-1")) {
            tvequSchValidity.setText("");
            tvequSchUpfrontFees.setText("");
            tvequSchDpAmc.setText("");
        } else {
            if (MOSLCommon.isNetworkEnabled(mContext)) {
                Map inputMap = new HashMap();
                inputMap.put("_SchemeID", schemeID);
                inputMap.put("_ProductID", productid);
                inputMap.put("_userId", getUserID());
                inputMap.put("_userType", getAppType());
                inputMap.put("_Amount", etEquMarginDeposit.getText().toString());
                String flag = BINDDETAILSONSCHEMESELECTIONMARGINORVALUEPAC;
                String category = SCHEMEDETAILS;
                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, flag, category, inputMap,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONObject response) {
                            if (response != null && response.length() != 0) {
                      /*  {"SchemeDetails":
                            [{"MarginProductId":34,
                                "SchemeName":"Margin 100,000",
                                "MarginDepositINR":"100,000",
                                "DeliveryBrokerageStandard":"0.25%",
                                "DeliveryBrokerageMinPaise":"2",
                                "DeliveryBrokerageMinINR":"20",
                                "IntradayCashSegment1stLegMAX":"0.025%",
                                "IntradayCashSegment2ndLegMAX":"0.025%",
                                "IntradayCashSegmentCashMin1stLegPaise":"2",
                                "IntradayCashSegmentCashMin2ndLegPaise":"2",
                                "IntradayCashSegmentMinINR":"20",
                                "IntradayFutures1stLegMAX":"0.025%",
                                "IntradayFutures2ndLegMAX":"0.025%",
                                "IntradayFuturesIfCarriedForward":"2",
                                "IntradayFuturesFutMinPaise":"2",
                                "Options1stLegAnd2ndLegINR":"50",
                                "ServicesOfferedPlatform":"Web",
                                "ServicesOfferedDeliveryPlus":"Y",
                                "ServicesOfferedMobileAccess":"Y",
                                "ServicesOfferedSMSAlertsBuyAndSell":"Y",
                                "ServicesOfferedSMSAlertsAdvice":"N/A",
                                "ServicesOfferedCallNTrade":"Y",
                                "ServicesOfferedAdvisoryServices":"N/A",
                                "ServicesOfferedResearchReports":"Y",
                                "ServicesOfferedChartingTool":"N/A",
                                "CURRENCYFUT1ST2NDLEGINR":"20","CURRENCYOPTIONS":"20"}]}*/


                                try {
                                    JSONArray jsonArray = response.getJSONArray("SchemeDetails");
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String SchemeName =
                                                (object.isNull("SchemeName") || object.getString(
                                                    "SchemeName").equals("")) ? ""
                                                    : object.getString("SchemeName");
                                            String marginDepositINR =
                                                (object.isNull("MarginDepositINR")
                                                    || object.getString("MarginDepositINR")
                                                    .equals("")) ? ""
                                                    : object.getString("MarginDepositINR");
                                            String accountOpeningFee =
                                                (object.isNull("AccountOpeningFee")
                                                    || object.getString("AccountOpeningFee")
                                                    .equals("")) ? ""
                                                    : object.getString("AccountOpeningFee");
                                            String deliveryBrokerageStandard =
                                                (object.isNull("DeliveryBrokerageStandard")
                                                    || object.getString("DeliveryBrokerageStandard")
                                                    .equals("")) ? ""
                                                    : object.getString("DeliveryBrokerageStandard");
                                            String deliveryBrokerageMinPaise =
                                                (object.isNull("DeliveryBrokerageMinPaise")
                                                    || object.getString("DeliveryBrokerageMinPaise")
                                                    .equals("")) ? ""
                                                    : object.getString("DeliveryBrokerageMinPaise");
                                            String deliveryBrokerageMinINR =
                                                (object.isNull("DeliveryBrokerageMinINR")
                                                    || object.getString("DeliveryBrokerageMinINR")
                                                    .equals("")) ? ""
                                                    : object.getString("DeliveryBrokerageMinINR");
                                            String intradayCashSegment1stLegMAX =
                                                (object.isNull("IntradayCashSegment1stLegMAX")
                                                    || object.getString(
                                                    "IntradayCashSegment1stLegMAX").equals("")) ? ""
                                                    : object.getString(
                                                        "IntradayCashSegment1stLegMAX");
                                            String intradayCashSegment2ndLegMAX =
                                                (object.isNull("IntradayCashSegment2ndLegMAX")
                                                    || object.getString(
                                                    "IntradayCashSegment2ndLegMAX").equals("")) ? ""
                                                    : object.getString(
                                                        "IntradayCashSegment2ndLegMAX");

                                            String intradayCashSegmentCashMin1stLegPaise =
                                                (object.isNull(
                                                    "IntradayCashSegmentCashMin1stLegPaise")
                                                    || object.getString(
                                                    "IntradayCashSegmentCashMin1stLegPaise")
                                                    .equals("")) ? "" : object.getString(
                                                    "IntradayCashSegmentCashMin1stLegPaise");
                                            String intradayCashSegmentCashMin2ndLegPaise =
                                                (object.isNull(
                                                    "IntradayCashSegmentCashMin2ndLegPaise")
                                                    || object.getString(
                                                    "IntradayCashSegmentCashMin2ndLegPaise")
                                                    .equals("")) ? "" : object.getString(
                                                    "IntradayCashSegmentCashMin2ndLegPaise");
                                            String intradayCashSegmentMinINR =
                                                (object.isNull("IntradayCashSegmentMinINR")
                                                    || object.getString("IntradayCashSegmentMinINR")
                                                    .equals("")) ? ""
                                                    : object.getString("IntradayCashSegmentMinINR");
                                            String intradayFutures1stLegMAX =
                                                (object.isNull("IntradayFutures1stLegMAX") || object
                                                    .getString("IntradayFutures1stLegMAX")
                                                    .equals("")) ? ""
                                                    : object.getString("IntradayFutures1stLegMAX");
                                            String intradayFutures2ndLegMAX =
                                                (object.isNull("IntradayFutures2ndLegMAX") || object
                                                    .getString("IntradayFutures2ndLegMAX")
                                                    .equals("")) ? ""
                                                    : object.getString("IntradayFutures2ndLegMAX");
                                            String intradayFuturesIfCarriedForward =
                                                (object.isNull("IntradayFuturesIfCarriedForward")
                                                    || object.getString(
                                                    "IntradayFuturesIfCarriedForward").equals(""))
                                                    ? "" : object.getString(
                                                    "IntradayFuturesIfCarriedForward");
                                            String intradayFuturesFutMinPaise =
                                                (object.isNull("IntradayFuturesFutMinPaise")
                                                    || object.getString(
                                                    "IntradayFuturesFutMinPaise").equals("")) ? ""
                                                    : object.getString(
                                                        "IntradayFuturesFutMinPaise");
                                            String options1stLegAnd2ndLegINR =
                                                (object.isNull("Options1stLegAnd2ndLegINR")
                                                    || object.getString("Options1stLegAnd2ndLegINR")
                                                    .equals("")) ? ""
                                                    : object.getString("Options1stLegAnd2ndLegINR");
                                            String CURRENCYFUT1ST2NDLEGINR =
                                                (object.isNull("CURRENCYFUT1ST2NDLEGINR")
                                                    || object.getString("CURRENCYFUT1ST2NDLEGINR")
                                                    .equals("")) ? ""
                                                    : object.getString("CURRENCYFUT1ST2NDLEGINR");
                                            String CURRENCYOPTIONS =
                                                (object.isNull("CURRENCYOPTIONS")
                                                    || object.getString("CURRENCYOPTIONS")
                                                    .equals("")) ? ""
                                                    : object.getString("CURRENCYOPTIONS");
                                            String dpAmc =
                                                (object.isNull("DPAMC") || object.getString("DPAMC")
                                                    .equals("")) ? "" : object.getString("DPAMC");
                                            tvSchemeName.setText(SchemeName);
                                            tvMarginDeposit.setText(marginDepositINR);
                                            tvDPAMC.setText(dpAmc);
                                            tvStandard.setText(deliveryBrokerageStandard);
                                            tvMinPaise.setText(deliveryBrokerageMinPaise);
                                            tvMinPerDay.setText(deliveryBrokerageMinINR);
                                            tvFirstLegMax.setText(intradayCashSegment1stLegMAX);
                                            tvSecondLegMax.setText(intradayCashSegment2ndLegMAX);
                                            tvCashMinFirstLeg.setText(
                                                intradayCashSegmentCashMin1stLegPaise);
                                            tvCashMinSecondLeg.setText(
                                                intradayCashSegmentCashMin2ndLegPaise);
                                            tvMinPerDaySecond.setText(intradayCashSegmentMinINR);
                                            tvFirstLegMaxFurure.setText(intradayFutures1stLegMAX);
                                            tvSecondLegMaxFuture.setText(intradayFutures2ndLegMAX);
                                            tvFutMinFirstLeg.setText(
                                                intradayFuturesIfCarriedForward);
                                            tvFutMinSecondLeg.setText(
                                                intradayFuturesIfCarriedForward);
                                            tvFirstSecondLeg.setText(options1stLegAnd2ndLegINR);
                                            tvFutureLeg.setText(CURRENCYFUT1ST2NDLEGINR);
                                            tvOptions.setText(CURRENCYOPTIONS);
                                            tvEquAccOpenNote.setText("DP-AMC " + dpAmc);

                                        }
                                        customProgress.setVisibility(View.GONE);
                                    } else {
                                        customProgress.setVisibility(View.GONE);

                                    }

                                    customProgress.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                customProgress.setVisibility(View.GONE);


                      /*  llNoResponse.setVisibility(View.VISIBLE);
                        lvLeads.setVisibility(View.GONE);
                        tvNoResponse.setText(NO_LEADS);*/
                            }
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                            String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }
                    });

            } else {
                MOSLCommon.alertDInternet(mContext);
                customProgress.setVisibility(View.GONE);
            }
        }
        dialogMarginScheme.show();
    }

    private void bindCommDetailsOnSelectedScheme(final String schemeID) {
        dialogCommScheme = new Dialog(mContext);
        dialogCommScheme.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCommScheme.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCommScheme.setContentView(R.layout.frag_comm_margin_scheme);
        dialogCommScheme.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogCommScheme.setCanceledOnTouchOutside(false);
        final TextView tvSchemeName, tvMarginDeposit, tvCommDelevaryBrokerage, tvFirstLegMax,
            tvFUTMin, tvSecondLegMax, tvTermandCondition;

        tvSchemeName = (TextView) dialogCommScheme.findViewById(R.id.tvSchemeName);
        tvMarginDeposit = (TextView) dialogCommScheme.findViewById(R.id.tvMarginDeposit);
        tvCommDelevaryBrokerage =
            (TextView) dialogCommScheme.findViewById(R.id.tvCommDelevaryBrokerage);
        tvFirstLegMax = (TextView) dialogCommScheme.findViewById(R.id.tvFirstLegMax);
        tvFUTMin = (TextView) dialogCommScheme.findViewById(R.id.tvFUTMin);
        tvSecondLegMax = (TextView) dialogCommScheme.findViewById(R.id.tvSecondLegMax);
        tvTermandCondition = (TextView) dialogCommScheme.findViewById(R.id.tvTermandCondition);
        tvTermandCondition.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        ImageView btnClose = (ImageView) dialogCommScheme.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCommScheme.dismiss();
            }
        });
        tvTermandCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog schemeEquTermConditionDialog = new Dialog(mContext);
                schemeEquTermConditionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                schemeEquTermConditionDialog.setContentView(R.layout.frag_term_and_condition);
                schemeEquTermConditionDialog.getWindow()
                    .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                schemeEquTermConditionDialog.setCanceledOnTouchOutside(false);
                Button btnClose = (Button) schemeEquTermConditionDialog.findViewById(R.id.btnClose);
                TextView tvTerms =
                    (TextView) schemeEquTermConditionDialog.findViewById(R.id.tvTerms);
                tvTerms.setText(getResources().getString(R.string.terms_commodity_margin_scheme));
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        schemeEquTermConditionDialog.dismiss();
                    }
                });
                schemeEquTermConditionDialog.show();
            }
        });
        String productid = "1";

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("_SchemeID", schemeID);
            inputMap.put("_ProductID", productid);
            inputMap.put("_userId", getUserID());
            inputMap.put("_userType", getAppType());
            inputMap.put("_Amount", etCommMarginDeposit.getText().toString());
            String flag = BINDDETAILSONSCHEMESELECTIONMARGINORVALUEPAC;
            customProgress.setVisibility(View.VISIBLE);
            String category = SCHEMEDETAILS;
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {
                          /*  {"SchemeDetails":
                            [{"MarginProductId":34,
                                "SchemeName":"Margin 100,000",
                                "MarginDepositINR":"100,000",
                                "DeliveryBrokerageStandard":"0.25%",
                                "DeliveryBrokerageMinPaise":"2",
                                "DeliveryBrokerageMinINR":"20",
                                "IntradayCashSegment1stLegMAX":"0.025%",
                                "IntradayCashSegment2ndLegMAX":"0.025%",
                                "IntradayCashSegmentCashMin1stLegPaise":"2",
                                "IntradayCashSegmentCashMin2ndLegPaise":"2",
                                "IntradayCashSegmentMinINR":"20",
                                "IntradayFutures1stLegMAX":"0.025%",
                                "IntradayFutures2ndLegMAX":"0.025%",
                                "IntradayFuturesIfCarriedForward":"2",
                                "IntradayFuturesFutMinPaise":"2",
                                "Options1stLegAnd2ndLegINR":"50",
                                "ServicesOfferedPlatform":"Web",
                                "ServicesOfferedDeliveryPlus":"Y",
                                "ServicesOfferedMobileAccess":"Y",
                                "ServicesOfferedSMSAlertsBuyAndSell":"Y",
                                "ServicesOfferedSMSAlertsAdvice":"N/A",
                                "ServicesOfferedCallNTrade":"Y",
                                "ServicesOfferedAdvisoryServices":"N/A",
                                "ServicesOfferedResearchReports":"Y",
                                "ServicesOfferedChartingTool":"N/A",
                                "CURRENCYFUT1ST2NDLEGINR":"20","CURRENCYOPTIONS":"20"}]}*/
                            try {
                                JSONArray jsonArray = response.getJSONArray("SchemeDetails");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String SchemeName =
                                            (object.isNull("SchemeName") || object.getString(
                                                "SchemeName").equals("")) ? ""
                                                : object.getString("SchemeName");
                                        String marginDepositINR =
                                            (object.isNull("MarginDepositINR") || object.getString(
                                                "MarginDepositINR").equals("")) ? ""
                                                : object.getString("MarginDepositINR");
                                        String accountOpeningFee =
                                            (object.isNull("AccountOpeningFee") || object.getString(
                                                "AccountOpeningFee").equals("")) ? ""
                                                : object.getString("AccountOpeningFee");
                                        String deliveryBrokerageStandard =
                                            (object.isNull("DeliveryBrokerageStandard")
                                                || object.getString("DeliveryBrokerageStandard")
                                                .equals("")) ? ""
                                                : object.getString("DeliveryBrokerageStandard");
                                        String deliveryBrokerageMinPaise =
                                            (object.isNull("DeliveryBrokerageMinPaise")
                                                || object.getString("DeliveryBrokerageMinPaise")
                                                .equals("")) ? ""
                                                : object.getString("DeliveryBrokerageMinPaise");
                                        String deliveryBrokerageMinINR =
                                            (object.isNull("DeliveryBrokerageMinINR")
                                                || object.getString("DeliveryBrokerageMinINR")
                                                .equals("")) ? ""
                                                : object.getString("DeliveryBrokerageMinINR");
                                        String intradayCashSegment1stLegMAX =
                                            (object.isNull("IntradayCashSegment1stLegMAX") || object
                                                .getString("IntradayCashSegment1stLegMAX")
                                                .equals("")) ? ""
                                                : object.getString("IntradayCashSegment1stLegMAX");
                                        String intradayCashSegment2ndLegMAX =
                                            (object.isNull("IntradayCashSegment2ndLegMAX") || object
                                                .getString("IntradayCashSegment2ndLegMAX")
                                                .equals("")) ? ""
                                                : object.getString("IntradayCashSegment2ndLegMAX");
                                        String intradayCashSegmentCashMin1stLegPaise =
                                            (object.isNull("IntradayCashSegmentCashMin1stLegPaise")
                                                || object.getString(
                                                "IntradayCashSegmentCashMin1stLegPaise").equals(""))
                                                ? "" : object.getString(
                                                "IntradayCashSegmentCashMin1stLegPaise");
                                        String intradayCashSegmentCashMin2ndLegPaise =
                                            (object.isNull("IntradayCashSegmentCashMin2ndLegPaise")
                                                || object.getString(
                                                "IntradayCashSegmentCashMin2ndLegPaise").equals(""))
                                                ? "" : object.getString(
                                                "IntradayCashSegmentCashMin2ndLegPaise");
                                        String intradayCashSegmentMinINR =
                                            (object.isNull("IntradayCashSegmentMinINR")
                                                || object.getString("IntradayCashSegmentMinINR")
                                                .equals("")) ? ""
                                                : object.getString("IntradayCashSegmentMinINR");
                                        String intradayFutures1stLegMAX =
                                            (object.isNull("IntradayFutures1stLegMAX")
                                                || object.getString("IntradayFutures1stLegMAX")
                                                .equals("")) ? ""
                                                : object.getString("IntradayFutures1stLegMAX");
                                        String intradayFutures2ndLegMAX =
                                            (object.isNull("IntradayFutures2ndLegMAX")
                                                || object.getString("IntradayFutures2ndLegMAX")
                                                .equals("")) ? ""
                                                : object.getString("IntradayFutures2ndLegMAX");
                                        String intradayFuturesIfCarriedForward =
                                            (object.isNull("IntradayFuturesIfCarriedForward")
                                                || object.getString(
                                                "IntradayFuturesIfCarriedForward").equals("")) ? ""
                                                : object.getString(
                                                    "IntradayFuturesIfCarriedForward");
                                        String intradayFuturesFutMinPaise =
                                            (object.isNull("IntradayFuturesFutMinPaise")
                                                || object.getString("IntradayFuturesFutMinPaise")
                                                .equals("")) ? ""
                                                : object.getString("IntradayFuturesFutMinPaise");
                                        String options1stLegAnd2ndLegINR =
                                            (object.isNull("Options1stLegAnd2ndLegINR")
                                                || object.getString("Options1stLegAnd2ndLegINR")
                                                .equals("")) ? ""
                                                : object.getString("Options1stLegAnd2ndLegINR");
                                        String CURRENCYFUT1ST2NDLEGINR =
                                            (object.isNull("CURRENCYFUT1ST2NDLEGINR")
                                                || object.getString("CURRENCYFUT1ST2NDLEGINR")
                                                .equals("")) ? ""
                                                : object.getString("CURRENCYFUT1ST2NDLEGINR");
                                        String CURRENCYOPTIONS =
                                            (object.isNull("CURRENCYOPTIONS") || object.getString(
                                                "CURRENCYOPTIONS").equals("")) ? ""
                                                : object.getString("CURRENCYOPTIONS");

                                        tvSchemeName.setText(SchemeName);
                                        tvMarginDeposit.setText(marginDepositINR);
                                        tvCommDelevaryBrokerage.setText(deliveryBrokerageStandard);
                                        tvFirstLegMax.setText(intradayFutures1stLegMAX);
                                        tvSecondLegMax.setText(intradayFutures2ndLegMAX);
                                        tvFUTMin.setText(intradayFuturesIfCarriedForward);
                                    }
                                    customProgress.setVisibility(View.GONE);
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                }
                                customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            customProgress.setVisibility(View.GONE);
                      /*  llNoResponse.setVisibility(View.VISIBLE);
                        lvLeads.setVisibility(View.GONE);
                        tvNoResponse.setText(NO_LEADS);*/
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
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });


        }
        dialogCommScheme.show();
    }

    private void bindEquDetailsOnSchemeSelection(final String schemeID) {
        if (isGetSchemeCalled && schemeID.equals("9999")) {
            getOtherEquSchDetail();
        } else {

            String productid = "";
            if (rbMarginAcc.isChecked()) {
                productid = "1";
            } else if (rbValuePacAcc.isChecked()) {
                productid = "2";
            } else {
                productid = "1";

            }

            if (productid.equals("2") && strEquSchKey.equalsIgnoreCase("-1")) {
                tvequSchValidity.setText("");
                tvequSchUpfrontFees.setText("");
                tvequSchDpAmc.setText("");
            } else if (productid.equals("1") && strEquSchKey.equalsIgnoreCase("-1")) {
                tvequSchInfoAccOpenCharge.setText("");
                tvequSchInfoSoftCharge.setText("");
                tvequSchInfoIntraday1stLeg.setText("");
                tvequSchInfoIntraday2ndLeg.setText("");
                tvequSchInfoDelivery.setText("");
                tvequSchInfoFutures1stLeg.setText("");
                tvequSchInfoFutures2ndLeg.setText("");
                tvequSchInfoOptions.setText("");
                tvequSchInfoCurrFuture.setText("");
                tvequSchInfoCurrOption.setText("");
                currencyoptionRadiogroup.setVisibility(View.GONE);
                currencyfutureRadiogroup.setVisibility(View.GONE);

            } else {
                if (MOSLCommon.isNetworkEnabled(mContext)) {
                    Map inputMap = new HashMap();
                    inputMap.put("_SchemeID", schemeID);
                    inputMap.put("_ProductID", productid);
                    inputMap.put("_userId", getUserID());
                    inputMap.put("_userType", getAppType());
                    String flag = BINDDETAILSONSCHEMESELECTIONMARGINORVALUEPAC;
                    String category = SCHEMEDETAILS;

                    customProgress.setVisibility(View.VISIBLE);
                    WebServiceHandler.post(mContext, flag, category, inputMap,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers,
                                JSONObject response) {
                                if (response != null && response.length() != 0) {
                                    String accountOpeningFee = "";
                                    try {
                                        JSONArray jsonArray =
                                            response.getJSONArray("SchemeDetails");
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                if (rbMarginAcc.isChecked()) {
                                                    String marginProductId =
                                                        (object.isNull("MarginProductId")
                                                            || object.getString("MarginProductId")
                                                            .equals("")) ? ""
                                                            : object.getString("MarginProductId");
                                                    String schemeName = (object.isNull("SchemeName")
                                                        || object.getString("SchemeName")
                                                        .equals("")) ? ""
                                                        : object.getString("SchemeName");
                                                    String marginDepositINR =
                                                        (object.isNull("MarginDepositINR") || object
                                                            .getString("MarginDepositINR")
                                                            .equals("")) ? ""
                                                            : object.getString("MarginDepositINR");
                                                    String intradayCashSegment1stLegMAX =
                                                        (object.isNull(
                                                            "IntradayCashSegment1stLegMAX")
                                                            || object.getString(
                                                            "IntradayCashSegment1stLegMAX")
                                                            .equals("")) ? "" : object.getString(
                                                            "IntradayCashSegment1stLegMAX");
                                                    String intradayCashSegment2ndLegMAX =
                                                        (object.isNull(
                                                            "IntradayCashSegment2ndLegMAX")
                                                            || object.getString(
                                                            "IntradayCashSegment2ndLegMAX")
                                                            .equals("")) ? "" : object.getString(
                                                            "IntradayCashSegment2ndLegMAX");
                                                    String deliveryBrokerageStandard =
                                                        (object.isNull("DeliveryBrokerageStandard")
                                                            || object.getString(
                                                            "DeliveryBrokerageStandard").equals(""))
                                                            ? "" : object.getString(
                                                            "DeliveryBrokerageStandard");
                                                    String intradayFutures1stLegMAX =
                                                        (object.isNull("IntradayFutures1stLegMAX")
                                                            || object.getString(
                                                            "IntradayFutures1stLegMAX").equals(""))
                                                            ? "" : object.getString(
                                                            "IntradayFutures1stLegMAX");
                                                    String intradayFutures2ndLegMAX =
                                                        (object.isNull("IntradayFutures2ndLegMAX")
                                                            || object.getString(
                                                            "IntradayFutures2ndLegMAX").equals(""))
                                                            ? "" : object.getString(
                                                            "IntradayFutures2ndLegMAX");
                                                    String options1stLegAnd2ndLegINR =
                                                        (object.isNull("Options1stLegAnd2ndLegINR")
                                                            || object.getString(
                                                            "Options1stLegAnd2ndLegINR").equals(""))
                                                            ? "" : object.getString(
                                                            "Options1stLegAnd2ndLegINR");
                                                    String currencyFut1st2ndLegINR =
                                                        (object.isNull("CURRENCYFUT1ST2NDLEGINR")
                                                            || object.getString(
                                                            "CURRENCYFUT1ST2NDLEGINR").equals(""))
                                                            ? "" : object.getString(
                                                            "CURRENCYFUT1ST2NDLEGINR");
                                                    String currencyOptions =
                                                        (object.isNull("CURRENCYOPTIONS")
                                                            || object.getString("CURRENCYOPTIONS")
                                                            .equals("")) ? ""
                                                            : object.getString("CURRENCYOPTIONS");
                                                    accountOpeningFee =
                                                        (object.isNull("AccountOpeningFee")
                                                            || object.getString("AccountOpeningFee")
                                                            .equals("")) ? ""
                                                            : object.getString("AccountOpeningFee");
                                                    String softwareCharges =
                                                        (object.isNull("SoftwareCharges")
                                                            || object.getString("SoftwareCharges")
                                                            .equals("")) ? ""
                                                            : object.getString("SoftwareCharges");
                                                    String dpAmc =
                                                        (object.isNull("DPAMC") || object.getString(
                                                            "DPAMC").equals("")) ? ""
                                                            : object.getString("DPAMC");

                                                    tvequSchInfoIntraday1stLeg.setText(
                                                        intradayCashSegment1stLegMAX);
                                                    tvequSchInfoIntraday2ndLeg.setText(
                                                        intradayCashSegment2ndLegMAX);
                                                    tvequSchInfoDelivery.setText(
                                                        deliveryBrokerageStandard);
                                                    tvequSchInfoFutures1stLeg.setText(
                                                        intradayFutures1stLegMAX);
                                                    tvequSchInfoFutures2ndLeg.setText(
                                                        intradayFutures2ndLegMAX);
                                                    tvequSchInfoOptions.setText(
                                                        options1stLegAnd2ndLegINR);
                                                    tvequSchInfoCurrFuture.setText(
                                                        currencyFut1st2ndLegINR);
                                                    tvequSchInfoCurrOption.setText(currencyOptions);
                                                    tvequSchInfoAccOpenCharge.setText(
                                                        accountOpeningFee);
                                                    tvequSchInfoSoftCharge.setText(softwareCharges);
                                                    tvEquAccOpenNote.setText("DP-AMC " + dpAmc);
                                                } else if (rbValuePacAcc.isChecked()) {
                                                    String Validitydays =
                                                        (object.isNull("ValidityDays")
                                                            || object.getString("ValidityDays")
                                                            .equals("")) ? ""
                                                            : object.getString("ValidityDays");
                                                    String valuePackFee =
                                                        (object.isNull("ValuePackFee")
                                                            || object.getString("ValuePackFee")
                                                            .equals("")) ? ""
                                                            : object.getString("ValuePackFee");
                                                    accountOpeningFee =
                                                        (object.isNull("AccountOpeningFee")
                                                            || object.getString("AccountOpeningFee")
                                                            .equals("")) ? ""
                                                            : object.getString("AccountOpeningFee");

                                                    tvequSchUpfrontFees.setText(valuePackFee);
                                                    tvequSchDpAmc.setText(accountOpeningFee);
                                                    tvequSchValidity.setText(Validitydays);
                                                } else {
                                                    String marginProductId =
                                                        (object.isNull("MarginProductId")
                                                            || object.getString("MarginProductId")
                                                            .equals("")) ? ""
                                                            : object.getString("MarginProductId");
                                                    String schemeName = (object.isNull("SchemeName")
                                                        || object.getString("SchemeName")
                                                        .equals("")) ? ""
                                                        : object.getString("SchemeName");
                                                    String marginDepositINR =
                                                        (object.isNull("MarginDepositINR") || object
                                                            .getString("MarginDepositINR")
                                                            .equals("")) ? ""
                                                            : object.getString("MarginDepositINR");
                                                    String intradayCashSegment1stLegMAX =
                                                        (object.isNull(
                                                            "IntradayCashSegment1stLegMAX")
                                                            || object.getString(
                                                            "IntradayCashSegment1stLegMAX")
                                                            .equals("")) ? "" : object.getString(
                                                            "IntradayCashSegment1stLegMAX");
                                                    String intradayCashSegment2ndLegMAX =
                                                        (object.isNull(
                                                            "IntradayCashSegment2ndLegMAX")
                                                            || object.getString(
                                                            "IntradayCashSegment2ndLegMAX")
                                                            .equals("")) ? "" : object.getString(
                                                            "IntradayCashSegment2ndLegMAX");
                                                    String deliveryBrokerageStandard =
                                                        (object.isNull("DeliveryBrokerageStandard")
                                                            || object.getString(
                                                            "DeliveryBrokerageStandard").equals(""))
                                                            ? "" : object.getString(
                                                            "DeliveryBrokerageStandard");
                                                    String intradayFutures1stLegMAX =
                                                        (object.isNull("IntradayFutures1stLegMAX")
                                                            || object.getString(
                                                            "IntradayFutures1stLegMAX").equals(""))
                                                            ? "" : object.getString(
                                                            "IntradayFutures1stLegMAX");
                                                    String intradayFutures2ndLegMAX =
                                                        (object.isNull("IntradayFutures2ndLegMAX")
                                                            || object.getString(
                                                            "IntradayFutures2ndLegMAX").equals(""))
                                                            ? "" : object.getString(
                                                            "IntradayFutures2ndLegMAX");
                                                    String options1stLegAnd2ndLegINR =
                                                        (object.isNull("Options1stLegAnd2ndLegINR")
                                                            || object.getString(
                                                            "Options1stLegAnd2ndLegINR").equals(""))
                                                            ? "" : object.getString(
                                                            "Options1stLegAnd2ndLegINR");
                                                    String currencyFut1st2ndLegINR =
                                                        (object.isNull("CURRENCYFUT1ST2NDLEGINR")
                                                            || object.getString(
                                                            "CURRENCYFUT1ST2NDLEGINR").equals(""))
                                                            ? "" : object.getString(
                                                            "CURRENCYFUT1ST2NDLEGINR");
                                                    String currencyOptions =
                                                        (object.isNull("CURRENCYOPTIONS")
                                                            || object.getString("CURRENCYOPTIONS")
                                                            .equals("")) ? ""
                                                            : object.getString("CURRENCYOPTIONS");
                                                    accountOpeningFee =
                                                        (object.isNull("AccountOpeningFee")
                                                            || object.getString("AccountOpeningFee")
                                                            .equals("")) ? ""
                                                            : object.getString("AccountOpeningFee");
                                                    String softwareCharges =
                                                        (object.isNull("SoftwareCharges")
                                                            || object.getString("SoftwareCharges")
                                                            .equals("")) ? ""
                                                            : object.getString("SoftwareCharges");

                                                    tvequSchInfoIntraday1stLeg.setText(
                                                        intradayCashSegment1stLegMAX);
                                                    tvequSchInfoIntraday2ndLeg.setText(
                                                        intradayCashSegment2ndLegMAX);
                                                    tvequSchInfoDelivery.setText(
                                                        deliveryBrokerageStandard);
                                                    tvequSchInfoFutures1stLeg.setText(
                                                        intradayFutures1stLegMAX);
                                                    tvequSchInfoFutures2ndLeg.setText(
                                                        intradayFutures2ndLegMAX);
                                                    tvequSchInfoOptions.setText(
                                                        options1stLegAnd2ndLegINR);
                                                    tvequSchInfoCurrFuture.setText(
                                                        currencyFut1st2ndLegINR);
                                                    tvequSchInfoCurrOption.setText(currencyOptions);
                                                    tvequSchInfoAccOpenCharge.setText(
                                                        accountOpeningFee);
                                                    tvequSchInfoSoftCharge.setText(softwareCharges);
                                                }
                                            }
                                            customProgress.setVisibility(View.GONE);
                                        } else {
                                            tvequSchInfoIntraday1stLeg.setText("");
                                            tvequSchInfoIntraday2ndLeg.setText("");
                                            tvequSchInfoDelivery.setText("");
                                            tvequSchInfoFutures1stLeg.setText("");
                                            tvequSchInfoFutures2ndLeg.setText("");
                                            tvequSchInfoOptions.setText("");
                                            tvequSchInfoCurrFuture.setText("");
                                            tvequSchInfoCurrOption.setText("");
                                            tvequSchValidity.setText("");
                                            tvequSchDpAmc.setText("");
                                            tvequSchUpfrontFees.setText("");
                                        }

                                        if (response.has("CurrencyFuture")) {
                                            try {
                                                jsonArray = response.getJSONArray("CurrencyFuture");
                                                SchemeDetailsModel detailsModel;
                                                if (jsonArray.length() != 0) {
                                                    modelList = new ArrayList<>();
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject object =
                                                            jsonArray.getJSONObject(i);
                                                        detailsModel = new SchemeDetailsModel();
                                                        detailsModel.setBrok_TEXT(
                                                            (object.isNull("BROK_TEXT")
                                                                || object.getString("BROK_TEXT")
                                                                .equals("")) ? ""
                                                                : object.getString("BROK_TEXT")
                                                                    .trim());
                                                        detailsModel.setBrok_VAL(
                                                            (object.isNull("BROK_VAL")
                                                                || object.getString("BROK_VAL")
                                                                .equals("")) ? ""
                                                                : object.getString("BROK_VAL")
                                                                    .trim());
                                                        detailsModel.setCurr_TYPE(
                                                            (object.isNull("CURR_TYPE")
                                                                || object.getString("CURR_TYPE")
                                                                .equals("")) ? ""
                                                                : object.getString("CURR_TYPE")
                                                                    .trim());
                                                        detailsModel.setCurrFutureSelectedVal((
                                                            object.isNull("CURRFutureSelectedVal")
                                                                || object.getString(
                                                                "CURRFutureSelectedVal").equals(""))
                                                            ? "" : object.getString(
                                                            "CURRFutureSelectedVal").trim());
                                                        detailsModel.setCurrFutureShow(
                                                            (object.isNull("CURRFutureShow")
                                                                || object.getString(
                                                                "CURRFutureShow").equals("")) ? ""
                                                                : object.getString("CURRFutureShow")
                                                                    .trim());
                                                        modelList.add(detailsModel);
                                                    }
                                                    currencyFutureShowRadioButton(modelList);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            currencyfutureRadiogroup.setVisibility(View.GONE);
                                        }
                                        if (response.has("CurrencyOption")) {
                                            try {
                                                jsonArray = response.getJSONArray("CurrencyOption");
                                                SchemeDetailsModel currencyOptionModel;

                                                if (jsonArray.length() != 0) {
                                                    currencyOptionmodelList = new ArrayList<>();
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject object =
                                                            jsonArray.getJSONObject(i);
                                                        currencyOptionModel =
                                                            new SchemeDetailsModel();
                                                        currencyOptionModel.setBrok_TEXT(
                                                            (object.isNull("BROK_TEXT")
                                                                || object.getString("BROK_TEXT")
                                                                .equals("")) ? ""
                                                                : object.getString("BROK_TEXT")
                                                                    .trim());
                                                        currencyOptionModel.setBrok_VAL(
                                                            (object.isNull("BROK_VAL")
                                                                || object.getString("BROK_VAL")
                                                                .equals("")) ? ""
                                                                : object.getString("BROK_VAL")
                                                                    .trim());
                                                        currencyOptionModel.setCurr_TYPE(
                                                            (object.isNull("CURR_TYPE")
                                                                || object.getString("CURR_TYPE")
                                                                .equals("")) ? ""
                                                                : object.getString("CURR_TYPE")
                                                                    .trim());
                                                        currencyOptionModel.setCurrOptionSelectedVal(
                                                            (object.isNull("CURROptionSelectedVal")
                                                                || object.getString(
                                                                "CURROptionSelectedVal").equals(""))
                                                                ? "" : object.getString(
                                                                "CURROptionSelectedVal").trim());
                                                        currencyOptionModel.setCurrOptionShow((
                                                            object.isNull("CURROptionShow")
                                                                || object.getString(
                                                                "CURROptionShow").equals("")) ? ""
                                                            : object.getString("CURROptionShow")
                                                                .trim());
                                                        currencyOptionmodelList.add(
                                                            currencyOptionModel);
                                                    }
                                                    currOptionShowRadioButton(
                                                        currencyOptionmodelList);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            currencyoptionRadiogroup.setVisibility(View.GONE);
                                        }
                                        customProgress.setVisibility(View.GONE);
                                        if (getAppType().equals(CRM)) {
                                            getDpSchemeDetails(accountOpeningFee, schemeID);
                                        }
                                    } catch (Exception e) {
                                        Timber.e(e.toString());
                                        System.out.println("ArrayoutofBondError" + e.toString());
                                        e.printStackTrace();
                                    }
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                    if (schemeID.equals("9999")) {
                                        tvequSchInfoIntraday1stLeg.setText("");
                                        tvequSchInfoIntraday2ndLeg.setText("");
                                        tvequSchInfoDelivery.setText("");
                                        tvequSchInfoFutures1stLeg.setText("");
                                        tvequSchInfoFutures2ndLeg.setText("");
                                        tvequSchInfoOptions.setText("");
                                        tvequSchInfoCurrFuture.setText("");
                                        tvequSchInfoCurrOption.setText("");
                                    }

                      /*  llNoResponse.setVisibility(View.VISIBLE);
                        lvLeads.setVisibility(View.GONE);
                        tvNoResponse.setText(NO_LEADS);*/
                                }
                                customProgress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers,
                                JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers,
                                String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers,
                                String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers,
                                Throwable throwable, JSONObject errorResponse) {
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers,
                                Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            }
                        });

                } else {
                    MOSLCommon.alertDInternet(mContext);
                    customProgress.setVisibility(View.GONE);
                }
            }
        }
    }

    private void getSchemeDetailsForValuePack() {

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("_userId", userID);
            inputMap.put("_userType", getAppType());
            inputMap.put("IS_DIRECT_BA", isDirectProspect() ? "1" : "0");

            String flag = GETSCHEMEDETAILS;
            String category = SCHEMEDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {
                      /*  {"ViewAllSchemePDF":
                            [{"EquitySchemePDF":"https://www.motilaloswal.com/open-an-account/margin_scheme.pdf",
                                "ValuePackEquitySchemePDF":"https://www.motilaloswal.com/open-an-account/value pack.pdf",
                                "CommoditySchemePDF":"https://www.motilaloswal.com/open-an-account/margin_scheme_Commodity.pdf"}]}*/
                            customProgress.setVisibility(View.VISIBLE);
                            try {
                                JSONArray jsonArray = response.getJSONArray("ViewAllSchemePDF");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String urlParh = "";
                                        String EquitySchemePDF =
                                            (object.isNull("EquitySchemePDF") || object.getString(
                                                "EquitySchemePDF").equals("")) ? ""
                                                : object.getString("EquitySchemePDF");
                                        String ValuePackEquitySchemePDF =
                                            (object.isNull("ValuePackEquitySchemePDF")
                                                || object.getString("ValuePackEquitySchemePDF")
                                                .equals("")) ? ""
                                                : object.getString("ValuePackEquitySchemePDF");
                                        String CommoditySchemePDF =
                                            (object.isNull("CommoditySchemePDF")
                                                || object.getString("CommoditySchemePDF")
                                                .equals("")) ? ""
                                                : object.getString("CommoditySchemePDF");
                                        if (rbValuePacAcc.isChecked()
                                            && getAppType().equals(DIRECT)
                                            && !getIsAadharVerified()) {
                                            urlParh =
                                                ValuePackEquitySchemePDF.replaceAll(" ", "%20");
                                        } else {
                                            urlParh = EquitySchemePDF.replaceAll(" ", "%20");
                                        }
                                        downloadPDF(urlParh);
                                       /* Intent intent = new Intent(mContext, WebActivity.class);
                                        intent.putExtra(URL_PATH, urlPath);
                                        startActivity(intent);*/


                                    }
                                }

                                customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
                            }
                        } else {
                            customProgress.setVisibility(View.GONE);

                      /*  llNoResponse.setVisibility(View.VISIBLE);
                        lvLeads.setVisibility(View.GONE);
                        tvNoResponse.setText(NO_LEADS);*/
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
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });

        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void getSchemeDetailsForCommodity() {

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("_userId", userID);
            inputMap.put("_userType", getAppType());
            inputMap.put("IS_DIRECT_BA", isDirectProspect() ? "1" : "0");

            String flag = GETSCHEMEDETAILS;
            String category = SCHEMEDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {
                            customProgress.setVisibility(View.VISIBLE);
                      /*  {"ViewAllSchemePDF":
                            [{"EquitySchemePDF":"https://www.motilaloswal.com/open-an-account/margin_scheme.pdf",
                                "ValuePackEquitySchemePDF":"https://www.motilaloswal.com/open-an-account/value pack.pdf",
                                "CommoditySchemePDF":"https://www.motilaloswal.com/open-an-account/margin_scheme_Commodity.pdf"}]}*/
                            try {
                                JSONArray jsonArray = response.getJSONArray("ViewAllSchemePDF");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String urlParh = "";
                                        String EquitySchemePDF =
                                            (object.isNull("EquitySchemePDF") || object.getString(
                                                "EquitySchemePDF").equals("")) ? ""
                                                : object.getString("EquitySchemePDF");
                                        String ValuePackEquitySchemePDF =
                                            (object.isNull("ValuePackEquitySchemePDF")
                                                || object.getString("ValuePackEquitySchemePDF")
                                                .equals("")) ? ""
                                                : object.getString("ValuePackEquitySchemePDF");
                                        String CommoditySchemePDF =
                                            (object.isNull("CommoditySchemePDF")
                                                || object.getString("CommoditySchemePDF")
                                                .equals("")) ? ""
                                                : object.getString("CommoditySchemePDF");
                                        urlParh = CommoditySchemePDF.replaceAll(" ", "%20");

                                        downloadPDF(urlParh);
                                       /* Intent intent = new Intent(mContext, WebActivity.class);
                                        intent.putExtra(URL_PATH, urlPath);
                                        startActivity(intent);*/


                                    }
                                }

                                customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
                            }
                        } else {
                            customProgress.setVisibility(View.GONE);

                      /*  llNoResponse.setVisibility(View.VISIBLE);
                        lvLeads.setVisibility(View.GONE);
                        tvNoResponse.setText(NO_LEADS);*/
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
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });

        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void bindCommDetailsOnSchemeSelection(String schemeID) {
        if (isGetSchemeCalled && schemeID.equals("9999")) {
            getOtherCommSchDetail();
        } else {
            if (schemeID.equals("")) {
                tvCommDelBrok.setText("");
                tvCommIntradayBrok.setText("");
            } else if (MOSLCommon.isNetworkEnabled(mContext)) {
                Map inputMap = new HashMap();
                inputMap.put("_SchemeID", schemeID);
                inputMap.put("_userId", getUserID());
                inputMap.put("_userType", getAppType());
                String flag = BINDDETAILSONSCHEMESELECTION;
                String category = SCHEMEDETAILS;
                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, flag, category, inputMap,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONObject response) {
                            if (response != null && response.length() != 0) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("SchemeDetails");
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            String deliveryBrokerageStandard =
                                                (object.isNull("DeliveryBrokerageStandard")
                                                    || object.getString("DeliveryBrokerageStandard")
                                                    .equals("")) ? ""
                                                    : object.getString("DeliveryBrokerageStandard");
                                            String IntradayCashSegment1stLegMAX =
                                                (object.isNull("IntradayCashSegment1stLegMAX")
                                                    || object.getString(
                                                    "IntradayCashSegment1stLegMAX").equals("")) ? ""
                                                    : object.getString(
                                                        "IntradayCashSegment1stLegMAX");

                                            tvCommDelBrok.setText(deliveryBrokerageStandard);
                                            tvCommIntradayBrok.setText(
                                                IntradayCashSegment1stLegMAX);

                                        }
                                    }

                                    customProgress.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                customProgress.setVisibility(View.GONE);

                      /*  llNoResponse.setVisibility(View.VISIBLE);
                        lvLeads.setVisibility(View.GONE);
                        tvNoResponse.setText(NO_LEADS);*/
                            }
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                            String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }
                    });

            } else {
                MOSLCommon.alertDInternet(mContext);
                customProgress.setVisibility(View.GONE);
            }
        }
    }

    private void fillEquBASchemeDetails(String strBrokerageScheme) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap();

            /*if (spEquBrokerage.getSelectedItem().toString().contains("D")) {
                strBrokerageScheme = "D";
            } else if (spEquBrokerage.getSelectedItem().toString().contains("M")) {
                strBrokerageScheme = "MR";
            }*/
            /*if (strBrokerageScheme.contains("D")) {
                strBrokerageScheme = "D";
            } else {
                strBrokerageScheme = "MR";
            }*/

            if (strBrokerageScheme.equals(SELECT)) {
                arrTradingEQ.clear();
                arrAdapTradingEQ =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrTradingEQ);
                arrAdapTradingEQ.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spEquTrading.setAdapter(arrAdapTradingEQ);
                arrAdapTradingEQ.notifyDataSetChanged();
                tvTradeSummary.setText("");

                arrDeliveryEQ.clear();
                arrAdapDeliveryEQ =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item,
                        arrDeliveryEQ);
                arrAdapDeliveryEQ.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spEquDelivery.setAdapter(arrAdapDeliveryEQ);
                arrAdapDeliveryEQ.notifyDataSetChanged();
                tvDelSummary.setText("");

                arrFuturesEQ.clear();
                arrAdapFuturesEQ =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrFuturesEQ);
                arrAdapFuturesEQ.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spEquFuture.setAdapter(arrAdapFuturesEQ);
                arrAdapFuturesEQ.notifyDataSetChanged();
                tvFutureSummary.setText("");

                arrOptionsEQ.clear();
                arrAdapOptionsEQ =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrOptionsEQ);
                arrAdapOptionsEQ.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spEquOption.setAdapter(arrAdapOptionsEQ);
                arrAdapOptionsEQ.notifyDataSetChanged();
                tvOptionsSummary.setText("");

                arrDPEQ.clear();
                arrAdapDPEQ =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrDPEQ);
                arrAdapDPEQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spEquDPAMC.setAdapter(arrAdapDPEQ);
                arrAdapDPEQ.notifyDataSetChanged();
                tvDPSummary.setText("");

                arrCurrencyFuturesEQ.clear();
                arrAdapCurrencyFuturesEQ =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item,
                        arrCurrencyFuturesEQ);
                arrAdapCurrencyFuturesEQ.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spEquCurrencyFuture.setAdapter(arrAdapCurrencyFuturesEQ);
                arrAdapCurrencyFuturesEQ.notifyDataSetChanged();
                tvCurrencyFutureSummary.setText("");


                arrCurrencyOptionsEQ.clear();
                arrAdapCurrencyOptionsEQ =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item,
                        arrCurrencyOptionsEQ);
                arrAdapCurrencyOptionsEQ.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spEquCurrencyOption.setAdapter(arrAdapCurrencyOptionsEQ);
                arrAdapCurrencyOptionsEQ.notifyDataSetChanged();
                tvCurrencyOptionSummary.setText("");


                customProgress.setVisibility(View.GONE);

            } else {
                inputMap.put("_USERID", userID);
                inputMap.put("_BrokerageScheme", strBrokerageScheme);
                inputMap.put("_Segment", "Equity");
                inputMap.put("_BrokerageExchange", "");


                String flag = FILLSCHEMEDETAILS;
                String category = SCHEMEDETAILS;
                customProgress.setVisibility(View.VISIBLE);
                WebServiceHandler.post(mContext, flag, category, inputMap,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONObject response) {

                        /*arrTradingEQ = new ArrayList<>();
                        arrDeliveryEQ = new ArrayList<>();
                        arrFuturesEQ = new ArrayList<>();
                        arrOptionsEQ = new ArrayList<>();
                        arrDPEQ = new ArrayList<>();
                        arrCurrencyFuturesEQ = new ArrayList<>();
                        arrCurrencyOptionsEQ = new ArrayList<>();*/


                            arrTradingEQ.clear();
                            arrDeliveryEQ.clear();
                            arrFuturesEQ.clear();
                            arrOptionsEQ.clear();
                            arrDPEQ.clear();
                            arrCurrencyFuturesEQ.clear();
                            arrCurrencyOptionsEQ.clear();

                            if (response != null && response.length() != 0) {
                                try {
                                    if (response.has("TradingEQ")) {
                                        JSONArray jsonArrTradingEQ =
                                            response.getJSONArray("TradingEQ");
                                        if (jsonArrTradingEQ.length() != 0) {
                                            arrTradingEQ.add(new RowData(0, "-1", "Select"));
                                            for (int i = 0; i < jsonArrTradingEQ.length(); i++) {
                                                JSONObject object =
                                                    jsonArrTradingEQ.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                                String profileName = (object.isNull("Profile_Name")
                                                    || object.getString("Profile_Name").equals(""))
                                                    ? "" : object.getString("Profile_Name");
                                                String segment =
                                                    (object.isNull("Segment") || object.getString(
                                                        "Segment").equals("")) ? ""
                                                        : object.getString("Segment");
                                                String userId =
                                                    (object.isNull("UserId") || object.getString(
                                                        "UserId").equals("")) ? ""
                                                        : object.getString("UserId");

                                                arrTradingEQ.add(
                                                    new RowData((i + 1), profileId, profileName));
                                            }
                                        }
                                        arrAdapTradingEQ = new ArrayAdapter<RowData>(mContext,
                                            R.layout.simple_spinner_item, arrTradingEQ);
                                        arrAdapTradingEQ.setDropDownViewResource(
                                            android.R.layout.simple_spinner_dropdown_item);

                                        spEquTrading.setAdapter(arrAdapTradingEQ);
                                        spEquTrading.setOnItemSelectedListener(equTradListener);

                                        spinnerSchemeSelect(getEquTradeSchSelID(), arrAdapTradingEQ,
                                            spEquTrading);
                                        //                                    setEquTradeSchSelID("");
                                    }
                                    if (response.has("DeliveryEQ")) {
                                        JSONArray jsonArrDeliveryEQ =
                                            response.getJSONArray("DeliveryEQ");

                                        if (jsonArrDeliveryEQ.length() != 0) {
                                            arrDeliveryEQ.add(new RowData(0, "-1", "Select"));
                                            for (int i = 0; i < jsonArrDeliveryEQ.length(); i++) {
                                                JSONObject object =
                                                    jsonArrDeliveryEQ.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                                String profileName = (object.isNull("Profile_Name")
                                                    || object.getString("Profile_Name").equals(""))
                                                    ? "" : object.getString("Profile_Name");
                                                String segment =
                                                    (object.isNull("Segment") || object.getString(
                                                        "Segment").equals("")) ? ""
                                                        : object.getString("Segment");
                                                String userId =
                                                    (object.isNull("UserId") || object.getString(
                                                        "UserId").equals("")) ? ""
                                                        : object.getString("UserId");

                                                arrDeliveryEQ.add(
                                                    new RowData((i + 1), profileId, profileName));
                                            }
                                        }
                                        arrAdapDeliveryEQ = new ArrayAdapter<RowData>(mContext,
                                            R.layout.simple_spinner_item, arrDeliveryEQ);
                                        arrAdapDeliveryEQ.setDropDownViewResource(
                                            android.R.layout.simple_spinner_dropdown_item);

                                        spEquDelivery.setAdapter(arrAdapDeliveryEQ);
                                        spEquDelivery.setOnItemSelectedListener(
                                            equDeliveryListener);
                                        spinnerSchemeSelect(getEquDelSchSelID(), arrAdapDeliveryEQ,
                                            spEquDelivery);
                                        //                                    setEquDelSchSelID("");
                                    }
                                    if (isDerivativeOriginal()) {
                                        if (!isEquitySegmentExist()) {
                                            spEquFuture.setEnabled(true);
                                            spEquOption.setEnabled(true);
                                        } else {
                                            spEquOption.setEnabled(false);
                                            spEquFuture.setEnabled(false);
                                        }
                                        if (response.has("FuturesEQ")) {
                                            JSONArray jsonArrFuturesEQ =
                                                response.getJSONArray("FuturesEQ");

                                            if (jsonArrFuturesEQ.length() != 0) {
                                                arrFuturesEQ.add(new RowData(0, "-1", "Select"));
                                                for (int i = 0; i < jsonArrFuturesEQ.length();
                                                    i++) {
                                                    JSONObject object =
                                                        jsonArrFuturesEQ.getJSONObject(i);
                                                    String profileId = (object.isNull("Profile_Id")
                                                        || object.getString("Profile_Id")
                                                        .equals("")) ? ""
                                                        : object.getString("Profile_Id");
                                                    String profileName =
                                                        (object.isNull("Profile_Name")
                                                            || object.getString("Profile_Name")
                                                            .equals("")) ? ""
                                                            : object.getString("Profile_Name");
                                                    String segment = (object.isNull("Segment")
                                                        || object.getString("Segment").equals(""))
                                                        ? "" : object.getString("Segment");
                                                    String userId = (object.isNull("UserId")
                                                        || object.getString("UserId").equals(""))
                                                        ? "" : object.getString("UserId");

                                                    arrFuturesEQ.add(new RowData((i + 1), profileId,
                                                        profileName));
                                                }
                                            }
                                            arrAdapFuturesEQ = new ArrayAdapter<RowData>(mContext,
                                                R.layout.simple_spinner_item, arrFuturesEQ);
                                            arrAdapFuturesEQ.setDropDownViewResource(
                                                android.R.layout.simple_spinner_dropdown_item);

                                            spEquFuture.setAdapter(arrAdapFuturesEQ);
                                            spEquFuture.setOnItemSelectedListener(
                                                equFutureListener);
                                            spinnerSchemeSelect(getEquFutSchSelID(),
                                                arrAdapFuturesEQ, spEquFuture);
                                            //                                        setEquFutSchSelID("");
                                        }
                                        if (response.has("OptionsEQ")) {
                                            JSONArray jsonArrOptionsEQ =
                                                response.getJSONArray("OptionsEQ");

                                            if (jsonArrOptionsEQ.length() != 0) {
                                                arrOptionsEQ.add(new RowData(0, "-1", "Select"));
                                                for (int i = 0; i < jsonArrOptionsEQ.length();
                                                    i++) {
                                                    JSONObject object =
                                                        jsonArrOptionsEQ.getJSONObject(i);
                                                    String profileId = (object.isNull("Profile_Id")
                                                        || object.getString("Profile_Id")
                                                        .equals("")) ? ""
                                                        : object.getString("Profile_Id");
                                                    String profileName =
                                                        (object.isNull("Profile_Name")
                                                            || object.getString("Profile_Name")
                                                            .equals("")) ? ""
                                                            : object.getString("Profile_Name");
                                                    String segment = (object.isNull("Segment")
                                                        || object.getString("Segment").equals(""))
                                                        ? "" : object.getString("Segment");
                                                    String userId = (object.isNull("UserId")
                                                        || object.getString("UserId").equals(""))
                                                        ? "" : object.getString("UserId");

                                                    arrOptionsEQ.add(new RowData((i + 1), profileId,
                                                        profileName));
                                                }
                                            }
                                            arrAdapOptionsEQ = new ArrayAdapter<RowData>(mContext,
                                                R.layout.simple_spinner_item, arrOptionsEQ);
                                            arrAdapOptionsEQ.setDropDownViewResource(
                                                android.R.layout.simple_spinner_dropdown_item);

                                            spEquOption.setAdapter(arrAdapOptionsEQ);
                                            spEquOption.setOnItemSelectedListener(
                                                equOptionListener);
                                            spinnerSchemeSelect(getEquOptSchSelID(),
                                                arrAdapOptionsEQ, spEquOption);
                                            //                                        setEquOptSchSelID("");

                                        }
                                    } else {
                                        spEquFuture.setEnabled(false);
                                        spEquOption.setEnabled(false);
                                        //                                spEquFuture.setBackgroundColor(getResources().getColor(R.color.grey_text));
                                        //                                spEquOption.setBackgroundColor(getResources().getColor(R.color.grey_text));
                                    }
                                    if (response.has("DPEQ")) {
                                        JSONArray jsonArrDPEQ = response.getJSONArray("DPEQ");

                                        if (jsonArrDPEQ.length() != 0) {
                                            arrDPEQ.add(new RowData(0, "-1", "Select"));
                                            for (int i = 0; i < jsonArrDPEQ.length(); i++) {
                                                JSONObject object = jsonArrDPEQ.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                                String profileName = (object.isNull("Profile_Name")
                                                    || object.getString("Profile_Name").equals(""))
                                                    ? "" : object.getString("Profile_Name");
                                                String segment =
                                                    (object.isNull("Segment") || object.getString(
                                                        "Segment").equals("")) ? ""
                                                        : object.getString("Segment");
                                                String userId =
                                                    (object.isNull("UserId") || object.getString(
                                                        "UserId").equals("")) ? ""
                                                        : object.getString("UserId");

                                                arrDPEQ.add(
                                                    new RowData((i + 1), profileId, profileName));
                                            }
                                        }
                                        arrAdapDPEQ = new ArrayAdapter<RowData>(mContext,
                                            R.layout.simple_spinner_item, arrDPEQ);
                                        arrAdapDPEQ.setDropDownViewResource(
                                            android.R.layout.simple_spinner_dropdown_item);

                                        spEquDPAMC.setAdapter(arrAdapDPEQ);
                                        spEquDPAMC.setOnItemSelectedListener(equDPEQListener);
                                        spinnerSchemeSelect(getEquDPSchSelID(), arrAdapDPEQ,
                                            spEquDPAMC);
                                        //                                    setEquDPSchSelID("");

                                    }
                                    if (isCurrencyOriginal()) {
                                        if (!isEquitySegmentExist()) {
                                            spEquCurrencyFuture.setEnabled(true);
                                            spEquCurrencyOption.setEnabled(true);
                                        } else {
                                            spEquCurrencyFuture.setEnabled(false);
                                            spEquCurrencyOption.setEnabled(false);
                                        }
                                        if (response.has("CurrencyFuturesEQ")) {
                                            JSONArray jsonArrCurrencyFuturesEQ =
                                                response.getJSONArray("CurrencyFuturesEQ");

                                            if (jsonArrCurrencyFuturesEQ.length() != 0) {
                                                arrCurrencyFuturesEQ.add(
                                                    new RowData(0, "-1", "Select"));
                                                for (int i = 0;
                                                    i < jsonArrCurrencyFuturesEQ.length(); i++) {
                                                    JSONObject object =
                                                        jsonArrCurrencyFuturesEQ.getJSONObject(i);
                                                    String profileId = (object.isNull("Profile_Id")
                                                        || object.getString("Profile_Id")
                                                        .equals("")) ? ""
                                                        : object.getString("Profile_Id");
                                                    String profileName =
                                                        (object.isNull("Profile_Name")
                                                            || object.getString("Profile_Name")
                                                            .equals("")) ? ""
                                                            : object.getString("Profile_Name");
                                                    String segment = (object.isNull("Segment")
                                                        || object.getString("Segment").equals(""))
                                                        ? "" : object.getString("Segment");
                                                    String userId = (object.isNull("UserId")
                                                        || object.getString("UserId").equals(""))
                                                        ? "" : object.getString("UserId");

                                                    arrCurrencyFuturesEQ.add(
                                                        new RowData((i + 1), profileId,
                                                            profileName));
                                                }
                                            }
                                            arrAdapCurrencyFuturesEQ =
                                                new ArrayAdapter<RowData>(mContext,
                                                    R.layout.simple_spinner_item,
                                                    arrCurrencyFuturesEQ);
                                            arrAdapCurrencyFuturesEQ.setDropDownViewResource(
                                                android.R.layout.simple_spinner_dropdown_item);

                                            spEquCurrencyFuture.setAdapter(
                                                arrAdapCurrencyFuturesEQ);
                                            spEquCurrencyFuture.setOnItemSelectedListener(
                                                equCurrencyFutureListener);
                                            spinnerSchemeSelect(getEquCurrFutSchSelID(),
                                                arrAdapCurrencyFuturesEQ, spEquCurrencyFuture);
                                            //                                        setEquCurrFutSchSelID("");
                                        }
                                        if (response.has("CurrencyOptionsEQ")) {
                                            JSONArray jsonArrCurrencyOptionsEQ =
                                                response.getJSONArray("CurrencyOptionsEQ");

                                            if (jsonArrCurrencyOptionsEQ.length() != 0) {
                                                arrCurrencyOptionsEQ.add(
                                                    new RowData(0, "-1", "Select"));
                                                for (int i = 0;
                                                    i < jsonArrCurrencyOptionsEQ.length(); i++) {
                                                    JSONObject object =
                                                        jsonArrCurrencyOptionsEQ.getJSONObject(i);
                                                    String profileId = (object.isNull("Profile_Id")
                                                        || object.getString("Profile_Id")
                                                        .equals("")) ? ""
                                                        : object.getString("Profile_Id");
                                                    String profileName =
                                                        (object.isNull("Profile_Name")
                                                            || object.getString("Profile_Name")
                                                            .equals("")) ? ""
                                                            : object.getString("Profile_Name");
                                                    String segment = (object.isNull("Segment")
                                                        || object.getString("Segment").equals(""))
                                                        ? "" : object.getString("Segment");
                                                    String userId = (object.isNull("UserId")
                                                        || object.getString("UserId").equals(""))
                                                        ? "" : object.getString("UserId");

                                                    arrCurrencyOptionsEQ.add(
                                                        new RowData((i + 1), profileId,
                                                            profileName));
                                                }
                                            }
                                            arrAdapCurrencyOptionsEQ =
                                                new ArrayAdapter<RowData>(mContext,
                                                    R.layout.simple_spinner_item,
                                                    arrCurrencyOptionsEQ);
                                            arrAdapCurrencyOptionsEQ.setDropDownViewResource(
                                                android.R.layout.simple_spinner_dropdown_item);

                                            spEquCurrencyOption.setAdapter(
                                                arrAdapCurrencyOptionsEQ);
                                            spEquCurrencyOption.setOnItemSelectedListener(
                                                equCurrencyOptionsListener);
                                            spinnerSchemeSelect(getEquCurrOptSchSelID(),
                                                arrAdapCurrencyOptionsEQ, spEquCurrencyOption);
                                            //                                        setEquCurrOptSchSelID("");
                                        }
                                    } else {
                                        spEquCurrencyFuture.setEnabled(false);
                                        spEquCurrencyOption.setEnabled(false);
                                    }
                                    customProgress.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    customProgress.setVisibility(View.GONE);
                                    commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY,
                                        false, "");
                                }
                            } else {
                                // MOSLCommon.showToastMsg(mContext, NO_RESPONSE + "for scheme");
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            }
                            //                        customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                            String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }
                    });
            }
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void fillCommBASchemeDetails(String strBrokerageScheme) {

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap();


            if (strBrokerageScheme.equals(SELECT)) {
                arrFuturesCOMM.clear();
                arrAdapFuturesCOMM =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item,
                        arrFuturesCOMM);
                arrAdapFuturesCOMM.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spCommFuture.setAdapter(arrAdapFuturesCOMM);
                arrAdapFuturesCOMM.notifyDataSetChanged();
                tvBAMCXFutSummary.setText("");

                arrOptionsCOMM.clear();
                arrAdapOptionsCOMM =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item,
                        arrOptionsCOMM);
                arrAdapOptionsCOMM.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spCommOption.setAdapter(arrAdapOptionsCOMM);
                arrAdapOptionsCOMM.notifyDataSetChanged();
                tvBAMCXOptSummary.setText("");

                arrDeliveryCOMM.clear();
                arrAdapDeliveryCOMM =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item,
                        arrDeliveryCOMM);
                arrAdapDeliveryCOMM.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spCommDPAMC.setAdapter(arrAdapDeliveryCOMM);
                arrAdapDeliveryCOMM.notifyDataSetChanged();
                tvBAMCXDPSummary.setText("");
                customProgress.setVisibility(View.GONE);
            } else {
                customProgress.setVisibility(View.VISIBLE);

                inputMap.put("_USERID", userID);
                inputMap.put("_BrokerageScheme", strBrokerageScheme);
                inputMap.put("_Segment", "Commodity");
                inputMap.put("_BrokerageExchange", "MCX");


                String flag = FILLSCHEMEDETAILS;
                String category = SCHEMEDETAILS;

                WebServiceHandler.post(mContext, flag, category, inputMap,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONObject response) {
                            arrFuturesCOMM.clear();
                            arrOptionsCOMM.clear();
                            arrDeliveryCOMM.clear();

                            if (response != null && response.length() != 0) {
                                try {
                                    if (response.has("FuturesCOMM")) {
                                        JSONArray jsonArrFuturesCOMM =
                                            response.getJSONArray("FuturesCOMM");
                                        if (jsonArrFuturesCOMM.length() != 0) {
                                            arrFuturesCOMM.add(new RowData(0, "-1", "Select"));
                                            for (int i = 0; i < jsonArrFuturesCOMM.length(); i++) {
                                                JSONObject object =
                                                    jsonArrFuturesCOMM.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                                String profileName = (object.isNull("Profile_Name")
                                                    || object.getString("Profile_Name").equals(""))
                                                    ? "" : object.getString("Profile_Name");
                                                String segment =
                                                    (object.isNull("Segment") || object.getString(
                                                        "Segment").equals("")) ? ""
                                                        : object.getString("Segment");
                                                String userId =
                                                    (object.isNull("UserId") || object.getString(
                                                        "UserId").equals("")) ? ""
                                                        : object.getString("UserId");

                                                arrFuturesCOMM.add(
                                                    new RowData((i + 1), profileId, profileName));
                                            }
                                        }
                                        arrAdapFuturesCOMM = new ArrayAdapter<RowData>(mContext,
                                            R.layout.simple_spinner_item, arrFuturesCOMM);
                                        arrAdapFuturesCOMM.setDropDownViewResource(
                                            android.R.layout.simple_spinner_dropdown_item);

                                        spCommFuture.setAdapter(arrAdapFuturesCOMM);
                                        spCommFuture.setOnItemSelectedListener(commFutListener);

                                        spinnerSchemeSelect(getCommFutSchSelID(),
                                            arrAdapFuturesCOMM, spCommFuture);
                                        //                                    setCommFutSchSelID("");
                                    }
                                    if (response.has("OptionsCOMM")) {
                                        JSONArray jsonArrOptionsCOMM =
                                            response.getJSONArray("OptionsCOMM");
                                        if (jsonArrOptionsCOMM.length() != 0) {
                                            arrOptionsCOMM.add(new RowData(0, "-1", "Select"));
                                            for (int i = 0; i < jsonArrOptionsCOMM.length(); i++) {
                                                JSONObject object =
                                                    jsonArrOptionsCOMM.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                                String profileName = (object.isNull("Profile_Name")
                                                    || object.getString("Profile_Name").equals(""))
                                                    ? "" : object.getString("Profile_Name");
                                                String segment =
                                                    (object.isNull("Segment") || object.getString(
                                                        "Segment").equals("")) ? ""
                                                        : object.getString("Segment");
                                                String userId =
                                                    (object.isNull("UserId") || object.getString(
                                                        "UserId").equals("")) ? ""
                                                        : object.getString("UserId");

                                                arrOptionsCOMM.add(
                                                    new RowData((i + 1), profileId, profileName));
                                            }
                                        }
                                        arrAdapOptionsCOMM = new ArrayAdapter<RowData>(mContext,
                                            R.layout.simple_spinner_item, arrOptionsCOMM);
                                        arrAdapOptionsCOMM.setDropDownViewResource(
                                            android.R.layout.simple_spinner_dropdown_item);

                                        spCommOption.setAdapter(arrAdapOptionsCOMM);
                                        spCommOption.setOnItemSelectedListener(commOptListener);
                                        spinnerSchemeSelect(getCommOptSchSelID(),
                                            arrAdapOptionsCOMM, spCommOption);
                                        //                                    setCommOptSchSelID("");

                                    }
                                    if (response.has("DeliveryCOMM")) {
                                        JSONArray jsonArrDeliveryCOMM =
                                            response.getJSONArray("DeliveryCOMM");

                                        if (jsonArrDeliveryCOMM.length() != 0) {
                                            arrDeliveryCOMM.add(new RowData(0, "-1", "Select"));
                                            for (int i = 0; i < jsonArrDeliveryCOMM.length(); i++) {
                                                JSONObject object =
                                                    jsonArrDeliveryCOMM.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                                String profileName = (object.isNull("Profile_Name")
                                                    || object.getString("Profile_Name").equals(""))
                                                    ? "" : object.getString("Profile_Name");
                                                String segment =
                                                    (object.isNull("Segment") || object.getString(
                                                        "Segment").equals("")) ? ""
                                                        : object.getString("Segment");
                                                String userId =
                                                    (object.isNull("UserId") || object.getString(
                                                        "UserId").equals("")) ? ""
                                                        : object.getString("UserId");

                                                arrDeliveryCOMM.add(
                                                    new RowData((i + 1), profileId, profileName));
                                            }
                                        }
                                        arrAdapDeliveryCOMM = new ArrayAdapter<RowData>(mContext,
                                            R.layout.simple_spinner_item, arrDeliveryCOMM);
                                        arrAdapDeliveryCOMM.setDropDownViewResource(
                                            android.R.layout.simple_spinner_dropdown_item);

                                        spCommDPAMC.setAdapter(arrAdapDeliveryCOMM);
                                        spCommDPAMC.setOnItemSelectedListener(commDelListener);
                                        spinnerSchemeSelect(getCommDelSchSelID(),
                                            arrAdapDeliveryCOMM, spCommDPAMC);
                                        //                                    setCommDelSchSelID("");

                                    }
                                    customProgress.setVisibility(View.GONE);
                                    // getSchemeDetails(getAppType(), userID);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY,
                                        false, "");
                                    customProgress.setVisibility(View.GONE);
                                }
                            } else {
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            }
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                            String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }
                    });
            }
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void fillCommNCXBASchemeDetails(String strBrokerageScheme) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap();
            if (strBrokerageScheme.equals(SELECT)) {
                arrFuturesNCXCOMM.clear();
                arrAdapFuturesNCXCOMM =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item,
                        arrFuturesNCXCOMM);
                arrAdapFuturesNCXCOMM.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spNCXFuture.setAdapter(arrAdapFuturesNCXCOMM);
                arrAdapFuturesNCXCOMM.notifyDataSetChanged();
                tvBANCXFutSummary.setText("");

                arrOptionsNCXCOMM.clear();
                arrAdapOptionsNCXCOMM =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item,
                        arrOptionsNCXCOMM);
                arrAdapOptionsNCXCOMM.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spNCXOption.setAdapter(arrAdapOptionsNCXCOMM);
                arrAdapOptionsNCXCOMM.notifyDataSetChanged();
                tvBANCXOptSummary.setText("");

                arrDeliveryNCXCOMM.clear();
                arrAdapDelNCXCOMM =
                    new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item,
                        arrDeliveryNCXCOMM);
                arrAdapDelNCXCOMM.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
                spNCXDPAMC.setAdapter(arrAdapDelNCXCOMM);
                arrAdapDelNCXCOMM.notifyDataSetChanged();
                tvBANCXDPAMCSummary.setText("");

                customProgress.setVisibility(View.GONE);

            } else {
                customProgress.setVisibility(View.VISIBLE);
                inputMap.put("_USERID", userID);
                inputMap.put("_BrokerageScheme", strBrokerageScheme);
                inputMap.put("_Segment", "Commodity");
                inputMap.put("_BrokerageExchange", "NCDX");


                String flag = FILLSCHEMEDETAILS;
                String category = SCHEMEDETAILS;

                WebServiceHandler.post(mContext, flag, category, inputMap,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONObject response) {

                            arrFuturesNCXCOMM.clear();
                            arrOptionsNCXCOMM.clear();
                            arrDeliveryNCXCOMM.clear();

                            if (response != null && response.length() != 0) {


                                try {

                                    if (response.has("FuturesNCXCOMM")) {
                                        JSONArray jsonArrFuturesNCXCOMM =
                                            response.getJSONArray("FuturesNCXCOMM");

                                        if (jsonArrFuturesNCXCOMM.length() != 0) {
                                            arrFuturesNCXCOMM.add(new RowData(0, "-1", "Select"));
                                            for (int i = 0; i < jsonArrFuturesNCXCOMM.length();
                                                i++) {
                                                JSONObject object =
                                                    jsonArrFuturesNCXCOMM.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                                String profileName = (object.isNull("Profile_Name")
                                                    || object.getString("Profile_Name").equals(""))
                                                    ? "" : object.getString("Profile_Name");
                                                String segment =
                                                    (object.isNull("Segment") || object.getString(
                                                        "Segment").equals("")) ? ""
                                                        : object.getString("Segment");
                                                String userId =
                                                    (object.isNull("UserId") || object.getString(
                                                        "UserId").equals("")) ? ""
                                                        : object.getString("UserId");

                                                arrFuturesNCXCOMM.add(
                                                    new RowData((i + 1), profileId, profileName));
                                            }
                                        }
                                        arrAdapFuturesNCXCOMM = new ArrayAdapter<RowData>(mContext,
                                            R.layout.simple_spinner_item, arrFuturesNCXCOMM);
                                        arrAdapFuturesNCXCOMM.setDropDownViewResource(
                                            android.R.layout.simple_spinner_dropdown_item);

                                        spNCXFuture.setAdapter(arrAdapFuturesNCXCOMM);
                                        spNCXFuture.setOnItemSelectedListener(commNCXFutListener);

                                        spinnerSchemeSelect(getCommFutNCXSchSelID(),
                                            arrAdapFuturesNCXCOMM, spNCXFuture);
                                        //                                    setCommFutNCXSchSelID("");
                                    }

                                    if (response.has("OptionsNCXCOMM")) {
                                        JSONArray jsonArrOptionsNCXCOMM =
                                            response.getJSONArray("OptionsNCXCOMM");

                                        if (jsonArrOptionsNCXCOMM.length() != 0) {
                                            arrOptionsNCXCOMM.add(new RowData(0, "-1", "Select"));
                                            for (int i = 0; i < jsonArrOptionsNCXCOMM.length();
                                                i++) {
                                                JSONObject object =
                                                    jsonArrOptionsNCXCOMM.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                                String profileName = (object.isNull("Profile_Name")
                                                    || object.getString("Profile_Name").equals(""))
                                                    ? "" : object.getString("Profile_Name");
                                                String segment =
                                                    (object.isNull("Segment") || object.getString(
                                                        "Segment").equals("")) ? ""
                                                        : object.getString("Segment");
                                                String userId =
                                                    (object.isNull("UserId") || object.getString(
                                                        "UserId").equals("")) ? ""
                                                        : object.getString("UserId");

                                                arrOptionsNCXCOMM.add(
                                                    new RowData((i + 1), profileId, profileName));
                                            }
                                        }
                                        arrAdapOptionsNCXCOMM = new ArrayAdapter<RowData>(mContext,
                                            R.layout.simple_spinner_item, arrOptionsNCXCOMM);
                                        arrAdapOptionsNCXCOMM.setDropDownViewResource(
                                            android.R.layout.simple_spinner_dropdown_item);

                                        spNCXOption.setAdapter(arrAdapOptionsNCXCOMM);
                                        spNCXOption.setOnItemSelectedListener(commNCXOptListener);

                                        spinnerSchemeSelect(getCommOptNCXSchSelID(),
                                            arrAdapOptionsNCXCOMM, spNCXOption);
                                        //                                    setCommOptNCXSchSelID("");
                                    }
                                    if (response.has("DeliveryNCXCOMM")) {
                                        JSONArray jsonArrDeliveryNCXCOMM =
                                            response.getJSONArray("DeliveryNCXCOMM");


                                        if (jsonArrDeliveryNCXCOMM.length() != 0) {
                                            arrDeliveryNCXCOMM.add(new RowData(0, "-1", "Select"));
                                            for (int i = 0; i < jsonArrDeliveryNCXCOMM.length();
                                                i++) {
                                                JSONObject object =
                                                    jsonArrDeliveryNCXCOMM.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                                String profileName = (object.isNull("Profile_Name")
                                                    || object.getString("Profile_Name").equals(""))
                                                    ? "" : object.getString("Profile_Name");
                                                String segment =
                                                    (object.isNull("Segment") || object.getString(
                                                        "Segment").equals("")) ? ""
                                                        : object.getString("Segment");
                                                String userId =
                                                    (object.isNull("UserId") || object.getString(
                                                        "UserId").equals("")) ? ""
                                                        : object.getString("UserId");

                                                arrDeliveryNCXCOMM.add(
                                                    new RowData((i + 1), profileId, profileName));
                                            }
                                        }
                                        arrAdapDelNCXCOMM = new ArrayAdapter<RowData>(mContext,
                                            R.layout.simple_spinner_item, arrDeliveryNCXCOMM);
                                        arrAdapDelNCXCOMM.setDropDownViewResource(
                                            android.R.layout.simple_spinner_dropdown_item);

                                        spNCXDPAMC.setAdapter(arrAdapDelNCXCOMM);
                                        spNCXDPAMC.setOnItemSelectedListener(commNCXDelListener);

                                        spinnerSchemeSelect(getCommDelNCXSchSelID(),
                                            arrAdapDelNCXCOMM, spNCXDPAMC);
                                        //                                    setCommDelNCXSchSelID("");
                                    }

                                    // Same as MCX selected
                                    if (isSameAsMCXCheck) {
                                        if (!(arrAdapFuturesNCXCOMM == null)
                                            && (spCommFuture.getSelectedItem() != null)) {
                                            RowData rdFutNCX = getSpinnerRowDataById(
                                                ((RowData) spCommFuture.getSelectedItem()).getKey(),
                                                arrAdapFuturesCOMM);
                                            if (rdFutNCX != null) {
                                                RowData rdCommNCXFutScheme =
                                                    RowData.getSpinnerRowDataByValue(
                                                        String.valueOf(rdFutNCX),
                                                        arrAdapFuturesNCXCOMM);

                                                if (rdCommNCXFutScheme != null) {
                                                    spNCXFuture.setSelection(
                                                        rdCommNCXFutScheme.getPosition());
                                                } else {
                                                    spNCXFuture.setSelection(0);
                                                    commonMsgDialog(mContext, NO_VALUE_FOR_FUTURE,
                                                        false, "");
                                                }
                                            }
                                        } else {
                                            commonMsgDialog(mContext, NO_VALUE_FOR_FUTURE, false,
                                                "");
                                        }
                                        if (!(arrAdapOptionsNCXCOMM == null)
                                            && (spCommOption.getSelectedItem() != null)) {
                                            RowData rdOptNCX = getSpinnerRowDataById(
                                                ((RowData) spCommOption.getSelectedItem()).getKey(),
                                                arrAdapOptionsCOMM);
                                            if (rdOptNCX != null) {
                                                RowData rdCommNCXOptScheme =
                                                    RowData.getSpinnerRowDataByValue(
                                                        String.valueOf(rdOptNCX),
                                                        arrAdapOptionsNCXCOMM);
                                                if (rdCommNCXOptScheme != null) {
                                                    spNCXOption.setSelection(
                                                        rdOptNCX.getPosition());
                                                } else {
                                                    spNCXOption.setSelection(0);
                                                    commonMsgDialog(mContext, NO_VALUE_FOR_OPTION,
                                                        false, "");
                                                }
                                            }
                                        } else {
                                            commonMsgDialog(mContext, NO_VALUE_FOR_OPTION, false,
                                                "");
                                        }
                                        if (!(arrAdapDelNCXCOMM == null)
                                            && (spCommDPAMC.getSelectedItem() != null)) {
                                            RowData rdDelNCX = getSpinnerRowDataById(
                                                ((RowData) spCommDPAMC.getSelectedItem()).getKey(),
                                                arrAdapDeliveryCOMM);
                                            if (rdDelNCX != null) {
                                                RowData rdCommNCXDelScheme =
                                                    RowData.getSpinnerRowDataByValue(
                                                        String.valueOf(rdDelNCX),
                                                        arrAdapDelNCXCOMM);
                                                if (rdCommNCXDelScheme != null) {
                                                    spNCXDPAMC.setSelection(
                                                        rdCommNCXDelScheme.getPosition());
                                                } else {
                                                    spNCXDPAMC.setSelection(0);
                                                    commonMsgDialog(mContext, NO_VALUE_FOR_DPAMC,
                                                        false, "");
                                                }
                                            }
                                        } else {
                                            commonMsgDialog(mContext, NO_VALUE_FOR_DPAMC, false,
                                                "");
                                        }
                                    }
                                    customProgress.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY,
                                        false, "");
                                    customProgress.setVisibility(View.GONE);
                                }
                            } else {
                                // MOSLCommon.showToastMsg(mContext, NO_RESPONSE + "for scheme");
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            }
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                            String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers,
                            String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }
                    });
            }
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void fillBothBankDropDown() {


        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put(APPLICATION_TYPE, getAppType());
            //            inputMap.put("_USERID", "2233");
            inputMap.put("_USERID", userID);

            customProgress.setVisibility(View.VISIBLE);

            WebServiceHandler.post(mContext, BANKLISTDD, BANKDETAILS, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        customProgress.setVisibility(View.VISIBLE);
                        arrEquityBankName = new ArrayList<>();
                        arrEquBankProof = new ArrayList<>();
                        arrCommBankProof = new ArrayList<>();
                        arrCommodityBankName = new ArrayList<>();
                        arrEquPhotoProof = new ArrayList<>();
                        arrCommPhotoProof = new ArrayList<>();
                        if (response != null && response.length() != 0) {

                            arrEquityBankName.clear();
                            arrCommodityBankName.clear();
                            arrEquBankProof.clear();
                            arrCommBankProof.clear();
                            arrEquPhotoProof.clear();
                            arrCommPhotoProof.clear();
                            try {

                                if (response.has("BANKLIST")) {
                                    JSONArray jsonArray = response.getJSONArray("BANKLIST");
                                    if (jsonArray.length() != 0) {
                                        arrEquityBankName.add(new RowData(0, "-1", "Select"));
                                        arrCommodityBankName.add(new RowData(0, "-1", "Select"));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String bankName = object.getString("BANKNAME");
                                            String bankID = object.getString("BANKNAME");

                                            arrEquityBankName.add(
                                                new RowData((i + 1), bankID, bankName));
                                            arrCommodityBankName.add(
                                                new RowData((i + 1), bankID, bankName));
                                        }
                                    }

                                    arrAdapEquityBankName = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrEquityBankName);
                                    arrAdapEquityBankName.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spEquSelectBank.setAdapter(arrAdapEquityBankName);
                                    spEquSelectBank.setOnItemSelectedListener(
                                        equSelectBankItemListener);

                                    arrAdapCommodityBankName = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrCommodityBankName);
                                    arrAdapCommodityBankName.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spCommSelectBank.setAdapter(arrAdapCommodityBankName);
                                    spCommSelectBank.setOnItemSelectedListener(
                                        commSelectBankItemListener);
                                }

                                if (response.has("BANKLIST1")) {
                                    JSONArray jArrayBankProof = response.getJSONArray("BANKLIST1");
                                    if (jArrayBankProof.length() != 0) {
                                        if (jArrayBankProof.length() > 1) {
                                            arrEquBankProof.add(new RowData(0, "-1", "Select"));
                                            arrCommBankProof.add(new RowData(0, "-1", "Select"));
                                        }
                                        for (int i = 0; i < jArrayBankProof.length(); i++) {
                                            JSONObject object = jArrayBankProof.getJSONObject(i);
                                            String text = object.getString("TEXT");
                                            String id = object.getString("ID");
                                            if (jArrayBankProof.length() > 1) {
                                                arrEquBankProof.add(new RowData((i + 1), id, text));
                                                arrCommBankProof.add(
                                                    new RowData((i + 1), id, text));
                                            } else {
                                                arrEquBankProof.add(new RowData((i), id, text));
                                                arrCommBankProof.add(new RowData((i), id, text));
                                            }

                                        }
                                    }
                                    arrAdapEquBankProof = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrEquBankProof);
                                    arrAdapEquBankProof.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spEquBankProof.setAdapter(arrAdapEquBankProof);
                                    spEquBankProof.setOnItemSelectedListener(equBankProofListener);

                                    arrAdapCommBankProof = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrCommBankProof);
                                    arrAdapCommBankProof.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spCommBankProof.setAdapter(arrAdapCommBankProof);
                                    spCommBankProof.setOnItemSelectedListener(
                                        commBankProofListener);

                                }
                                if (response.has("BANKLIST2")) {
                                    JSONArray jArrayPhotoProof = response.getJSONArray("BANKLIST2");
                                    if (jArrayPhotoProof.length() != 0) {
                                        if (jArrayPhotoProof.length() > 1) {
                                            arrEquPhotoProof.add(new RowData(0, "-1", "Select"));
                                            arrCommPhotoProof.add(new RowData(0, "-1", "Select"));
                                        }
                                        for (int i = 0; i < jArrayPhotoProof.length(); i++) {
                                            JSONObject object = jArrayPhotoProof.getJSONObject(i);
                                            String text = object.getString("TEXT");
                                            String id = object.getString("ID");

                                            if (jArrayPhotoProof.length() > 1) {
                                                arrEquPhotoProof.add(
                                                    new RowData((i + 1), id, text));
                                                arrCommPhotoProof.add(
                                                    new RowData((i + 1), id, text));
                                            } else {
                                                arrEquPhotoProof.add(new RowData((i), id, text));
                                                arrCommPhotoProof.add(new RowData((i), id, text));
                                            }

                                        }
                                    }
                                    arrAdapEquPhotoProof = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrEquPhotoProof);
                                    arrAdapEquPhotoProof.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spEquPhotoProof.setAdapter(arrAdapEquPhotoProof);
                                    spEquPhotoProof.setOnItemSelectedListener(
                                        equPhotoProofListener);

                                    arrAdapCommPhotoProof = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrCommPhotoProof);
                                    arrAdapCommPhotoProof.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spCommPhotoProof.setAdapter(arrAdapCommPhotoProof);
                                    spCommPhotoProof.setOnItemSelectedListener(
                                        commPhotoProofListener);

                                }
                                getBankDataByID();

                                // customProgress.setVisibility(View.GONE);
                                customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
                            }

                        } else {
                      /*  llNoResponse.setVisibility(View.VISIBLE);
                        lvLeads.setVisibility(View.GONE);
                        tvNoResponse.setText(NO_LEADS);*/
                            customProgress.setVisibility(View.GONE);
                        }
                        //                    customProgress.setVisibility(View.GONE);
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
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });

        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }


    private void fillMFBankDropDown() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);

            Map<String, String> inputMap = new HashMap<>();
            inputMap.put(APPLICATION_TYPE, getAppType());
            inputMap.put("_USERID", userID);

            WebServiceHandler.post(mContext, BANKLISTDD, BANKDETAILS, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        customProgress.setVisibility(View.VISIBLE);
                        arrMFBankName = new ArrayList<>();

                        if (response != null && response.length() != 0) {

                            arrMFBankName.clear();

                            try {

                                if (response.has("BANKLIST")) {
                                    JSONArray jsonArray = response.getJSONArray("BANKLIST");
                                    if (jsonArray.length() != 0) {
                                        arrMFBankName.add(new RowData(0, "-1", "Select"));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String bankName = object.getString("BANKNAME");
                                            String bankID = object.getString("BANKNAME");

                                            arrMFBankName.add(
                                                new RowData((i + 1), bankID, bankName));
                                        }
                                    }

                                    arrAdapMFBankName = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrMFBankName);
                                    arrAdapMFBankName.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spMFSelectBank.setAdapter(arrAdapMFBankName);
                                    spMFSelectBank.setOnItemSelectedListener(
                                        mfSelectBankItemListener);

                                }

                                customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //getBankDataByID();
                            insertDeleteBankDetail("", "", "", "", "", "", "", "");
                        } else {
                      /*  llNoResponse.setVisibility(View.VISIBLE);
                        lvLeads.setVisibility(View.GONE);
                        tvNoResponse.setText(NO_LEADS);*/
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
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void fillEquityBankDropDown() {

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put(APPLICATION_TYPE, getAppType());
            inputMap.put("_USERID", userID);

            WebServiceHandler.post(mContext, BANKLISTDD, BANKDETAILS, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        customProgress.setVisibility(View.VISIBLE);
                        arrEquityBankName = new ArrayList<>();
                        arrEquBankProof = new ArrayList<>();
                        arrEquPhotoProof = new ArrayList<>();
                        if (response != null && response.length() != 0) {

                            arrEquityBankName.clear();
                            arrEquBankProof.clear();
                            arrEquPhotoProof.clear();
                            try {

                                if (response.has("BANKLIST")) {
                                    JSONArray jsonArray = response.getJSONArray("BANKLIST");
                                    if (jsonArray.length() != 0) {
                                        arrEquityBankName.add(new RowData(0, "-1", "Select"));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String bankName = object.getString("BANKNAME");
                                            String bankID = object.getString("BANKNAME");

                                            arrEquityBankName.add(
                                                new RowData((i + 1), bankID, bankName));
                                        }
                                    }

                                    arrAdapEquityBankName = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrEquityBankName);
                                    arrAdapEquityBankName.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spEquSelectBank.setAdapter(arrAdapEquityBankName);
                                    spEquSelectBank.setOnItemSelectedListener(
                                        equSelectBankItemListener);

                                }

                                if (response.has("BANKLIST1")) {
                                    JSONArray jArrayBankProof = response.getJSONArray("BANKLIST1");
                                    if (jArrayBankProof.length() != 0) {
                                        if (jArrayBankProof.length() > 1) {
                                            arrEquBankProof.add(new RowData(0, "-1", "Select"));
                                        }
                                        for (int i = 0; i < jArrayBankProof.length(); i++) {
                                            JSONObject object = jArrayBankProof.getJSONObject(i);
                                            String text = object.getString("TEXT");
                                            String id = object.getString("ID");

                                            if (jArrayBankProof.length() > 1) {
                                                arrEquBankProof.add(new RowData((i + 1), id, text));
                                            } else {
                                                arrEquBankProof.add(new RowData((i), id, text));
                                            }

                                        }
                                    }
                                    arrAdapEquBankProof = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrEquBankProof);
                                    arrAdapEquBankProof.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spEquBankProof.setAdapter(arrAdapEquBankProof);
                                    spEquBankProof.setOnItemSelectedListener(equBankProofListener);


                                }
                                if (response.has("BANKLIST2")) {
                                    JSONArray jArrayPhotoProof = response.getJSONArray("BANKLIST2");
                                    if (jArrayPhotoProof.length() != 0) {
                                        if (jArrayPhotoProof.length() > 1) {
                                            arrEquPhotoProof.add(new RowData(0, "-1", "Select"));
                                        }
                                        for (int i = 0; i < jArrayPhotoProof.length(); i++) {
                                            JSONObject object = jArrayPhotoProof.getJSONObject(i);
                                            String text = object.getString("TEXT");
                                            String id = object.getString("ID");

                                            if (jArrayPhotoProof.length() > 1) {
                                                arrEquPhotoProof.add(
                                                    new RowData((i + 1), id, text));
                                            } else {
                                                arrEquPhotoProof.add(new RowData((i), id, text));
                                            }
                                        }
                                    }
                                    arrAdapEquPhotoProof = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrEquPhotoProof);
                                    arrAdapEquPhotoProof.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spEquPhotoProof.setAdapter(arrAdapEquPhotoProof);
                                    spEquPhotoProof.setOnItemSelectedListener(
                                        equPhotoProofListener);
                                }

                                customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
                            }
                            getBankDataByID();
                        } else {
                      /*  llNoResponse.setVisibility(View.VISIBLE);
                        lvLeads.setVisibility(View.GONE);
                        tvNoResponse.setText(NO_LEADS);*/
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        Log.e("Failure", "no resp in Data");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });

        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void fillCommodityBankDropDown() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put(APPLICATION_TYPE, getAppType());
            inputMap.put("_USERID", userID);

            WebServiceHandler.post(mContext, BANKLISTDD, BANKDETAILS, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        customProgress.setVisibility(View.VISIBLE);
                        arrCommBankProof = new ArrayList<>();
                        arrCommodityBankName = new ArrayList<>();
                        arrCommPhotoProof = new ArrayList<>();

                        if (response != null && response.length() != 0) {


                            arrCommodityBankName.clear();

                            arrCommBankProof.clear();

                            arrCommPhotoProof.clear();
                            try {

                                if (response.has("BANKLIST")) {
                                    JSONArray jsonArray = response.getJSONArray("BANKLIST");
                                    if (jsonArray.length() != 0) {
                                        arrCommodityBankName.add(new RowData(0, "-1", "Select"));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String bankName = object.getString("BANKNAME");
                                            String bankID = object.getString("BANKNAME");

                                            arrCommodityBankName.add(
                                                new RowData((i + 1), bankID, bankName));
                                        }
                                    }
                                    arrAdapCommodityBankName = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrCommodityBankName);
                                    arrAdapCommodityBankName.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spCommSelectBank.setAdapter(arrAdapCommodityBankName);
                                    spCommSelectBank.setOnItemSelectedListener(
                                        commSelectBankItemListener);
                                }

                                if (response.has("BANKLIST1")) {
                                    JSONArray jArrayBankProof = response.getJSONArray("BANKLIST1");
                                    if (jArrayBankProof.length() != 0) {
                                        if (jArrayBankProof.length() > 1) {
                                            arrCommBankProof.add(new RowData(0, "-1", "Select"));
                                        }
                                        for (int i = 0; i < jArrayBankProof.length(); i++) {
                                            JSONObject object = jArrayBankProof.getJSONObject(i);
                                            String text = object.getString("TEXT");
                                            String id = object.getString("ID");

                                            if (jArrayBankProof.length() > 1) {
                                                arrCommBankProof.add(
                                                    new RowData((i + 1), id, text));
                                            } else {
                                                arrCommBankProof.add(new RowData((i), id, text));
                                            }
                                        }
                                    }

                                    arrAdapCommBankProof = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrCommBankProof);
                                    arrAdapCommBankProof.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spCommBankProof.setAdapter(arrAdapCommBankProof);
                                    spCommBankProof.setOnItemSelectedListener(
                                        commBankProofListener);

                                }
                                if (response.has("BANKLIST2")) {
                                    JSONArray jArrayPhotoProof = response.getJSONArray("BANKLIST2");
                                    if (jArrayPhotoProof.length() != 0) {
                                        if (jArrayPhotoProof.length() > 1) {
                                            arrCommPhotoProof.add(new RowData(0, "-1", "Select"));
                                        }
                                        for (int i = 0; i < jArrayPhotoProof.length(); i++) {
                                            JSONObject object = jArrayPhotoProof.getJSONObject(i);
                                            String text = object.getString("TEXT");
                                            String id = object.getString("ID");

                                            if (jArrayPhotoProof.length() > 1) {
                                                arrCommPhotoProof.add(
                                                    new RowData((i + 1), id, text));
                                            } else {
                                                arrCommPhotoProof.add(new RowData((i), id, text));
                                            }
                                        }
                                    }

                                    arrAdapCommPhotoProof = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrCommPhotoProof);
                                    arrAdapCommPhotoProof.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spCommPhotoProof.setAdapter(arrAdapCommPhotoProof);
                                    spCommPhotoProof.setOnItemSelectedListener(
                                        commPhotoProofListener);

                                }

                                // customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            getBankDataByID();
                        } else {
                      /*  llNoResponse.setVisibility(View.VISIBLE);
                        lvLeads.setVisibility(View.GONE);
                        tvNoResponse.setText(NO_LEADS);*/
                        }
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        Log.e("Failure", "no resp in Data");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });

        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void getBankDataByID() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put(APPLICATION_TYPE, getAppType());
            //            inputMap.put("UserID", BANK_DATA_BY_ID);
            inputMap.put("UserID", userID);

            WebServiceHandler.post(mContext, FILLBANKDETAILS, BANKDETAILS, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        if (response.has("BANKUSERDATA")) {
                            customProgress.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = response.getJSONArray("BANKUSERDATA");

                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        if (jsonArray.length() == 2) {

                                            JSONObject obj_1 = jsonArray.getJSONObject(0);
                                            JSONObject obj_2 = jsonArray.getJSONObject(1);

                                            String prod_1 = obj_1.getString("PRODUCT");
                                            String prod_2 = obj_2.getString("PRODUCT");

                                            if (prod_1.equalsIgnoreCase(EQUITY)) {
                                                equBankName =
                                                    (obj_1.isNull("BANK_NAME") || obj_1.getString(
                                                        "BANK_NAME").equals("")) ? ""
                                                        : obj_1.getString("BANK_NAME");

                                            /*if (equBankName != null && equBankName.length() > 0) {
                                                updateBankDetail = true;
                                            } else {
                                                updateBankDetail = false;
                                            }*/

                                                equBankBranch =
                                                    (obj_1.isNull("BANK_BRANCH") || obj_1.getString(
                                                        "BANK_BRANCH").equals("")) ? ""
                                                        : obj_1.getString("BANK_BRANCH");
                                                equBankAdd =
                                                    (obj_1.isNull("BANK_ADD") || obj_1.getString(
                                                        "BANK_ADD").equals("")) ? ""
                                                        : obj_1.getString("BANK_ADD");
                                                equBankAccNo =
                                                    (obj_1.isNull("BANK_ACC_NO") || obj_1.getString(
                                                        "BANK_ACC_NO").equals("")) ? ""
                                                        : obj_1.getString("BANK_ACC_NO");
                                                equBankAcType = (obj_1.isNull("BANK_AC_TYPE")
                                                    || obj_1.getString("BANK_AC_TYPE").equals(""))
                                                    ? "" : obj_1.getString("BANK_AC_TYPE");
                                                equBankMICRCode = (obj_1.isNull("BANK_MICR_CODE")
                                                    || obj_1.getString("BANK_MICR_CODE").equals(""))
                                                    ? "" : obj_1.getString("BANK_MICR_CODE");
                                                equBankIFSCCode = (obj_1.isNull("BANK_IFSC_CODE")
                                                    || obj_1.getString("BANK_IFSC_CODE").equals(""))
                                                    ? "" : obj_1.getString("BANK_IFSC_CODE");
                                                equBankProof =
                                                    (obj_1.isNull("BANK_PROOF") || obj_1.getString(
                                                        "BANK_PROOF").equals("")) ? ""
                                                        : obj_1.getString("BANK_PROOF");
                                                equBankPhotoProof =
                                                    (obj_1.isNull("PHOTO_PROOF") || obj_1.getString(
                                                        "PHOTO_PROOF").equals("")) ? ""
                                                        : obj_1.getString("PHOTO_PROOF");

                                                equBankData(equBankName, equBankBranch, equBankAdd,
                                                    equBankAccNo, equBankAcType, equBankMICRCode,
                                                    equBankIFSCCode, equBankProof,
                                                    equBankPhotoProof);


                                            } else if (prod_1.equalsIgnoreCase(COMMODITY)) {
                                                commBankName =
                                                    (obj_1.isNull("BANK_NAME") || obj_1.getString(
                                                        "BANK_NAME").equals("")) ? ""
                                                        : obj_1.getString("BANK_NAME");
                                                commBankBranch =
                                                    (obj_1.isNull("BANK_BRANCH") || obj_1.getString(
                                                        "BANK_BRANCH").equals("")) ? ""
                                                        : obj_1.getString("BANK_BRANCH");
                                                commBankAdd =
                                                    (obj_1.isNull("BANK_ADD") || obj_1.getString(
                                                        "BANK_ADD").equals("")) ? ""
                                                        : obj_1.getString("BANK_ADD");
                                                commBankAccNo =
                                                    (obj_1.isNull("BANK_ACC_NO") || obj_1.getString(
                                                        "BANK_ACC_NO").equals("")) ? ""
                                                        : obj_1.getString("BANK_ACC_NO");
                                                commBankAcType = (obj_1.isNull("BANK_AC_TYPE")
                                                    || obj_1.getString("BANK_AC_TYPE").equals(""))
                                                    ? "" : obj_1.getString("BANK_AC_TYPE");
                                                commBankMICRCode = (obj_1.isNull("BANK_MICR_CODE")
                                                    || obj_1.getString("BANK_MICR_CODE").equals(""))
                                                    ? "" : obj_1.getString("BANK_MICR_CODE");
                                                commBankIFSCCode = (obj_1.isNull("BANK_IFSC_CODE")
                                                    || obj_1.getString("BANK_IFSC_CODE").equals(""))
                                                    ? "" : obj_1.getString("BANK_IFSC_CODE");
                                                commBankProof =
                                                    (obj_1.isNull("BANK_PROOF") || obj_1.getString(
                                                        "BANK_PROOF").equals("")) ? ""
                                                        : obj_1.getString("BANK_PROOF");
                                                commBankPhotoProof =
                                                    (obj_1.isNull("PHOTO_PROOF") || obj_1.getString(
                                                        "PHOTO_PROOF").equals("")) ? ""
                                                        : obj_1.getString("PHOTO_PROOF");

                                                commBankData(commBankName, commBankBranch,
                                                    commBankAdd, commBankAccNo, commBankAcType,
                                                    commBankMICRCode, commBankIFSCCode,
                                                    commBankProof, commBankPhotoProof);


                                            }
                                            if (prod_2.equalsIgnoreCase(EQUITY)) {
                                                equBankName =
                                                    (obj_2.isNull("BANK_NAME") || obj_2.getString(
                                                        "BANK_NAME").equals("")) ? ""
                                                        : obj_2.getString("BANK_NAME");

                                            /*if (equBankName != null && equBankName.length() > 0) {
                                                updateBankDetail = true;
                                            } else {
                                                updateBankDetail = false;
                                            }*/

                                                equBankBranch =
                                                    (obj_2.isNull("BANK_BRANCH") || obj_2.getString(
                                                        "BANK_BRANCH").equals("")) ? ""
                                                        : obj_2.getString("BANK_BRANCH");
                                                equBankAdd =
                                                    (obj_2.isNull("BANK_ADD") || obj_2.getString(
                                                        "BANK_ADD").equals("")) ? ""
                                                        : obj_2.getString("BANK_ADD");
                                                equBankAccNo =
                                                    (obj_2.isNull("BANK_ACC_NO") || obj_2.getString(
                                                        "BANK_ACC_NO").equals("")) ? ""
                                                        : obj_2.getString("BANK_ACC_NO");
                                                equBankAcType = (obj_2.isNull("BANK_AC_TYPE")
                                                    || obj_2.getString("BANK_AC_TYPE").equals(""))
                                                    ? "" : obj_2.getString("BANK_AC_TYPE");
                                                equBankMICRCode = (obj_2.isNull("BANK_MICR_CODE")
                                                    || obj_2.getString("BANK_MICR_CODE").equals(""))
                                                    ? "" : obj_2.getString("BANK_MICR_CODE");
                                                equBankIFSCCode = (obj_2.isNull("BANK_IFSC_CODE")
                                                    || obj_2.getString("BANK_IFSC_CODE").equals(""))
                                                    ? "" : obj_2.getString("BANK_IFSC_CODE");
                                                equBankProof =
                                                    (obj_2.isNull("BANK_PROOF") || obj_2.getString(
                                                        "BANK_PROOF").equals("")) ? ""
                                                        : obj_2.getString("BANK_PROOF");
                                                equBankPhotoProof =
                                                    (obj_2.isNull("PHOTO_PROOF") || obj_2.getString(
                                                        "PHOTO_PROOF").equals("")) ? ""
                                                        : obj_2.getString("PHOTO_PROOF");

                                                equBankData(equBankName, equBankBranch, equBankAdd,
                                                    equBankAccNo, equBankAcType, equBankMICRCode,
                                                    equBankIFSCCode, equBankProof,
                                                    equBankPhotoProof);
                                            } else if (prod_2.equalsIgnoreCase(COMMODITY)) {
                                                commBankName =
                                                    (obj_2.isNull("BANK_NAME") || obj_2.getString(
                                                        "BANK_NAME").equals("")) ? ""
                                                        : obj_2.getString("BANK_NAME");
                                                commBankBranch =
                                                    (obj_2.isNull("BANK_BRANCH") || obj_2.getString(
                                                        "BANK_BRANCH").equals("")) ? ""
                                                        : obj_2.getString("BANK_BRANCH");
                                                commBankAdd =
                                                    (obj_2.isNull("BANK_ADD") || obj_2.getString(
                                                        "BANK_ADD").equals("")) ? ""
                                                        : obj_2.getString("BANK_ADD");
                                                commBankAccNo =
                                                    (obj_2.isNull("BANK_ACC_NO") || obj_2.getString(
                                                        "BANK_ACC_NO").equals("")) ? ""
                                                        : obj_2.getString("BANK_ACC_NO");
                                                commBankAcType = (obj_2.isNull("BANK_AC_TYPE")
                                                    || obj_2.getString("BANK_AC_TYPE").equals(""))
                                                    ? "" : obj_2.getString("BANK_AC_TYPE");
                                                commBankMICRCode = (obj_2.isNull("BANK_MICR_CODE")
                                                    || obj_2.getString("BANK_MICR_CODE").equals(""))
                                                    ? "" : obj_2.getString("BANK_MICR_CODE");
                                                commBankIFSCCode = (obj_2.isNull("BANK_IFSC_CODE")
                                                    || obj_2.getString("BANK_IFSC_CODE").equals(""))
                                                    ? "" : obj_2.getString("BANK_IFSC_CODE");
                                                commBankProof =
                                                    (obj_2.isNull("BANK_PROOF") || obj_2.getString(
                                                        "BANK_PROOF").equals("")) ? ""
                                                        : obj_2.getString("BANK_PROOF");
                                                commBankPhotoProof =
                                                    (obj_2.isNull("PHOTO_PROOF") || obj_2.getString(
                                                        "PHOTO_PROOF").equals("")) ? ""
                                                        : obj_2.getString("PHOTO_PROOF");

                                                commBankData(commBankName, commBankBranch,
                                                    commBankAdd, commBankAccNo, commBankAcType,
                                                    commBankMICRCode, commBankIFSCCode,
                                                    commBankProof, commBankPhotoProof);
                                            }

                                        } else if (jsonArray.length() == 1) {
                                            JSONObject obj_1 = jsonArray.getJSONObject(0);
                                            String prod_1 = obj_1.getString("PRODUCT");
                                            //                                        Commodity scheme changes 14-03-2018
                                        /*if (prod_1.equalsIgnoreCase(EQUITY)) {
                                            equBankName = (obj_1.isNull("BANK_NAME") || obj_1.getString("BANK_NAME").equals("")) ? "" : obj_1.getString("BANK_NAME");

                                            *//*if (equBankName != null && equBankName.length() > 0) {
                                                updateBankDetail = true;
                                            } else {
                                                updateBankDetail = false;
                                            }*//*

                                            equBankBranch = (obj_1.isNull("BANK_BRANCH") || obj_1.getString("BANK_BRANCH").equals("")) ? "" : obj_1.getString("BANK_BRANCH");
                                            equBankAdd = (obj_1.isNull("BANK_ADD") || obj_1.getString("BANK_ADD").equals("")) ? "" : obj_1.getString("BANK_ADD");
                                            equBankAccNo = (obj_1.isNull("BANK_ACC_NO") || obj_1.getString("BANK_ACC_NO").equals("")) ? "" : obj_1.getString("BANK_ACC_NO");
                                            equBankAcType = (obj_1.isNull("BANK_AC_TYPE") || obj_1.getString("BANK_AC_TYPE").equals("")) ? "" : obj_1.getString("BANK_AC_TYPE");
                                            equBankMICRCode = (obj_1.isNull("BANK_MICR_CODE") || obj_1.getString("BANK_MICR_CODE").equals("")) ? "" : obj_1.getString("BANK_MICR_CODE");
                                            equBankIFSCCode = (obj_1.isNull("BANK_IFSC_CODE") || obj_1.getString("BANK_IFSC_CODE").equals("")) ? "" : obj_1.getString("BANK_IFSC_CODE");
                                            equBankProof = (obj_1.isNull("BANK_PROOF") || obj_1.getString("BANK_PROOF").equals("")) ? "" : obj_1.getString("BANK_PROOF");
                                            equBankPhotoProof = (obj_1.isNull("PHOTO_PROOF") || obj_1.getString("PHOTO_PROOF").equals("")) ? "" : obj_1.getString("PHOTO_PROOF");
                                            if (arrAdapEquityBankName == null) {
                                                fillEquityBankDropDown();
                                                equBankData(equBankName, equBankBranch, equBankAdd, equBankAccNo, equBankAcType, equBankMICRCode, equBankIFSCCode, equBankProof, equBankPhotoProof);
                                            } else {
                                                equBankData(equBankName, equBankBranch, equBankAdd, equBankAccNo, equBankAcType, equBankMICRCode, equBankIFSCCode, equBankProof, equBankPhotoProof);
                                            }
                                        } else {
                                            commBankName = (obj_1.isNull("BANK_NAME") || obj_1.getString("BANK_NAME").equals("")) ? "" : obj_1.getString("BANK_NAME");
                                            commBankBranch = (obj_1.isNull("BANK_BRANCH") || obj_1.getString("BANK_BRANCH").equals("")) ? "" : obj_1.getString("BANK_BRANCH");
                                            commBankAdd = (obj_1.isNull("BANK_ADD") || obj_1.getString("BANK_ADD").equals("")) ? "" : obj_1.getString("BANK_ADD");
                                            commBankAccNo = (obj_1.isNull("BANK_ACC_NO") || obj_1.getString("BANK_ACC_NO").equals("")) ? "" : obj_1.getString("BANK_ACC_NO");
                                            commBankAcType = (obj_1.isNull("BANK_AC_TYPE") || obj_1.getString("BANK_AC_TYPE").equals("")) ? "" : obj_1.getString("BANK_AC_TYPE");
                                            commBankMICRCode = (obj_1.isNull("BANK_MICR_CODE") || obj_1.getString("BANK_MICR_CODE").equals("")) ? "" : obj_1.getString("BANK_MICR_CODE");
                                            commBankIFSCCode = (obj_1.isNull("BANK_IFSC_CODE") || obj_1.getString("BANK_IFSC_CODE").equals("")) ? "" : obj_1.getString("BANK_IFSC_CODE");
                                            commBankProof = (obj_1.isNull("BANK_PROOF") || obj_1.getString("BANK_PROOF").equals("")) ? "" : obj_1.getString("BANK_PROOF");
                                            commBankPhotoProof = (obj_1.isNull("PHOTO_PROOF") || obj_1.getString("PHOTO_PROOF").equals("")) ? "" : obj_1.getString("PHOTO_PROOF");

                                            if (arrAdapCommodityBankName == null) {
                                                fillCommodityBankDropDown();
                                                commBankData(commBankName, commBankBranch, commBankAdd, commBankAccNo, commBankAcType, commBankMICRCode, commBankIFSCCode, commBankProof, commBankPhotoProof);
                                            } else {
                                                commBankData(commBankName, commBankBranch, commBankAdd, commBankAccNo, commBankAcType, commBankMICRCode, commBankIFSCCode, commBankProof, commBankPhotoProof);
                                            }
                                        }*/
                                            equBankName =
                                                (obj_1.isNull("BANK_NAME") || obj_1.getString(
                                                    "BANK_NAME").equals("")) ? ""
                                                    : obj_1.getString("BANK_NAME");
                                            equBankBranch =
                                                (obj_1.isNull("BANK_BRANCH") || obj_1.getString(
                                                    "BANK_BRANCH").equals("")) ? ""
                                                    : obj_1.getString("BANK_BRANCH");
                                            equBankAdd =
                                                (obj_1.isNull("BANK_ADD") || obj_1.getString(
                                                    "BANK_ADD").equals("")) ? ""
                                                    : obj_1.getString("BANK_ADD");
                                            equBankAccNo =
                                                (obj_1.isNull("BANK_ACC_NO") || obj_1.getString(
                                                    "BANK_ACC_NO").equals("")) ? ""
                                                    : obj_1.getString("BANK_ACC_NO");
                                            equBankAcType =
                                                (obj_1.isNull("BANK_AC_TYPE") || obj_1.getString(
                                                    "BANK_AC_TYPE").equals("")) ? ""
                                                    : obj_1.getString("BANK_AC_TYPE");
                                            equBankMICRCode =
                                                (obj_1.isNull("BANK_MICR_CODE") || obj_1.getString(
                                                    "BANK_MICR_CODE").equals("")) ? ""
                                                    : obj_1.getString("BANK_MICR_CODE");
                                            equBankIFSCCode =
                                                (obj_1.isNull("BANK_IFSC_CODE") || obj_1.getString(
                                                    "BANK_IFSC_CODE").equals("")) ? ""
                                                    : obj_1.getString("BANK_IFSC_CODE");
                                            equBankProof =
                                                (obj_1.isNull("BANK_PROOF") || obj_1.getString(
                                                    "BANK_PROOF").equals("")) ? ""
                                                    : obj_1.getString("BANK_PROOF");
                                            equBankPhotoProof =
                                                (obj_1.isNull("PHOTO_PROOF") || obj_1.getString(
                                                    "PHOTO_PROOF").equals("")) ? ""
                                                    : obj_1.getString("PHOTO_PROOF");
                                            if (arrAdapEquityBankName == null) {
                                                fillEquityBankDropDown();
                                                equBankData(equBankName, equBankBranch, equBankAdd,
                                                    equBankAccNo, equBankAcType, equBankMICRCode,
                                                    equBankIFSCCode, equBankProof,
                                                    equBankPhotoProof);
                                            } else {
                                                equBankData(equBankName, equBankBranch, equBankAdd,
                                                    equBankAccNo, equBankAcType, equBankMICRCode,
                                                    equBankIFSCCode, equBankProof,
                                                    equBankPhotoProof);
                                            }
                                        }
                                    }
                                }
                                customProgress.setVisibility(View.GONE);

                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
                            }
                        }
                        //                    customProgress.setVisibility(View.GONE);
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
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        Log.e("Failure", "no resp in Data");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    public void spinnerSchemeSelect(String ID, ArrayAdapter arrayAdapter, Spinner spinner) {
        if (ID != null) {
            RowData equSchemeName = RowData.getSpinnerRowDataById(ID, arrayAdapter);

            if (equSchemeName != null) {
                spinner.setSelection(equSchemeName.getPosition());
            }
        }
    }

    public void equSchemeData(String ID, String schemeName, String margindeposit) {
        RowData equSchemeName = RowData.getSpinnerRowDataById(ID, arrAdapEquScheme);

        if (equSchemeName != null) {
            spEquScheme.setSelection(equSchemeName.getPosition());
            etEquMarginDeposit.setText(margindeposit);
            bindEquDetailsOnSchemeSelection(ID);

        }
    }

    public void comSchemeData(String schemeSelectedID, String schemeName, String margindeposit) {
        RowData comSchemeName = RowData.getSpinnerRowDataById(schemeSelectedID, arrAdapCommScheme);

        if (comSchemeName != null) {
            spCommScheme.setSelection(comSchemeName.getPosition());
            etCommMarginDeposit.setText(margindeposit);
            bindCommDetailsOnSchemeSelection(schemeSelectedID);
        }
    }

    public void equBankData(String equBankName, String equBankBranch, String equBankAdd,
        String equBankAccNo, String equBankAcType, String equBankMICRCode, String equBankIFSCCode,
        String equBankProof, String equBankPhotoProof) {

        RowData equBankaNameRowData =
            RowData.getSpinnerRowDataByValue(equBankName, arrAdapEquityBankName);
        RowData equBankProofRowData =
            RowData.getSpinnerRowDataByValue(equBankProof, arrAdapEquBankProof);
        RowData equPhotoProofRowData =
            RowData.getSpinnerRowDataByValue(equBankPhotoProof, arrAdapEquPhotoProof);
        if (equBankaNameRowData != null) {
            spEquSelectBank.setSelection(equBankaNameRowData.getPosition());
        }
        if (equBankProofRowData != null) {
            spEquBankProof.setSelection(equBankProofRowData.getPosition());
        }
        if (equPhotoProofRowData != null) {
            spEquPhotoProof.setSelection(equPhotoProofRowData.getPosition());
        }
        if (!equBankAcType.equalsIgnoreCase("")) {
            if (equBankAcType.contains("Saving")) {
                rbEquSaving.setChecked(true);
            } else {
                rbEquCurrent.setChecked(true);
            }
        } else {
            rbEquSaving.setChecked(true);
        }

        tvEquityBankName.setText(equBankName);
        tvEquityBranchName.setText(equBankBranch);
        tvEquityBankAddress.setText(equBankAdd);
        etEquityAccNumber.setText(equBankAccNo);
        etConfirmEquityAccNumber.setText(equBankAccNo);
        tvEquityMICR.setText(equBankMICRCode);
        tvEquityIFSC.setText(equBankIFSCCode);
        // etEquityMICR.setText(equBankMICRCode);

    }

    public void commBankData(String commBankName, String commBankBranch, String commBankAdd,
        String commBankAccNo, String commBankAcType, String commBankMICRCode,
        String commBankIFSCCode, String commBankProof, String commBankPhotoProof) {


        RowData commBankNameRowData =
            RowData.getSpinnerRowDataByValue(commBankName, arrAdapCommodityBankName);
        RowData commBankProofRowData =
            RowData.getSpinnerRowDataByValue(commBankProof, arrAdapCommBankProof);
        RowData commPhotoProofRowData =
            RowData.getSpinnerRowDataByValue(commBankPhotoProof, arrAdapCommPhotoProof);

        if (commBankNameRowData != null) {
            spCommSelectBank.setSelection(commBankNameRowData.getPosition());
        }
        if (commBankProofRowData != null) {
            spCommBankProof.setSelection(commBankProofRowData.getPosition());
        }
        if (commPhotoProofRowData != null) {
            spCommPhotoProof.setSelection(commPhotoProofRowData.getPosition());
        }
        if (!commBankAcType.equalsIgnoreCase("")) {
            if (commBankAcType.contains("Saving")) {
                rbCommSaving.setChecked(true);
            } else {
                rbCommCurrent.setChecked(true);
            }
        } else {
            rbCommSaving.setChecked(true);
        }
        tvCommBankName.setText(commBankName);
        tvCommBranchName.setText(commBankBranch);
        tvCommBankAddress.setText(commBankAdd);
        etCommAccNumber.setText(commBankAccNo);
        etConfirmCommAccNumber.setText(commBankAccNo);
        tvCommMICR.setText(commBankMICRCode);
        tvCommIFSC.setText(commBankIFSCCode);
        //  etCommMICR.setText(commBankMICRCode);
    }

    private void getBankDetailsSearch(String type) {
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_bank_details);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText editextSearch = (EditText) dialog.findViewById(R.id.editextSearch);
        final ListView lvBankBranch = (ListView) dialog.findViewById(R.id.lvBankBranch);
        //  final LinearLayout EkycMainActivity.customProgress = (LinearLayout) dialog.findViewById(R.id.customProgress);
        ImageView btnClose = (ImageView) dialog.findViewById(R.id.btnClose);


        Map<String, String> inputMap = new HashMap<>();
        String flag = BANKSEARCH;
        String category = BANKDETAILS;

        inputMap.put("_USERID", userID);
        if (type.equalsIgnoreCase(EQUITY)) {
            if (spEquSelectBank.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
                inputMap.put(BANK_NAME, "");
            } else {
                inputMap.put(BANK_NAME, spEquSelectBank.getSelectedItem().toString());
            }
            inputMap.put(BANK_BRANCH, etEquityMICR.getText().toString());
            //            inputMap.put(BANK_BRANCH, "400240003");
            inputMap.put(APPLICATION_TYPE, getAppType());

            lvBankBranch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final TextView tvBankName, tvAddress, tvBranchName, tvMICR, tvIFSC,
                        tvEqOnlineBankFlag, tvCommOnlineBankFlag;
                    tvBankName = (TextView) view.findViewById(R.id.tvBankName);
                    tvAddress = (TextView) view.findViewById(R.id.tvAddress);
                    tvBranchName = (TextView) view.findViewById(R.id.tvBranchName);
                    tvMICR = (TextView) view.findViewById(R.id.tvMICR);
                    tvIFSC = (TextView) view.findViewById(R.id.tvIFSC);
                    tvEqOnlineBankFlag = (TextView) view.findViewById(R.id.tvEqOnlineBankFlag);
                    tvCommOnlineBankFlag = (TextView) view.findViewById(R.id.tvCommOnlineBankFlag);



                   /* if (tvEqOnlineBankFlag.getText().toString().equals("1")) {
                        setCommOnlineBank(true);
                    } else if (tvEqOnlineBankFlag.getText().toString().equals("0")) {
                        setCommOnlineBank(false);
                    }*/
                    etEquityAccNumber.requestFocus();
                    //                    etEquityMICR.setText("");
                    dialog.dismiss();
                    if (!tvIFSC.getText().toString().equals("")) {
                        llEquBankDetail.setVisibility(View.VISIBLE);
                        if (tvEqOnlineBankFlag.getText().toString().equals("0")) {
                            final CustomDialog dialogCheckOffline = new CustomDialog(mContext,
                                tvBankName.getText().toString() + OFFLINE_BANK, true, true, false,
                                "", YES, NO, new CustomDialog.CustomDialogListener() {
                                @Override
                                public void dialogAction(MOSLCommon.Action action, Object... data) {

                                    if (action == MOSLCommon.Action.OK) {
                                        tvEquityBankName.setText(tvBankName.getText().toString());
                                        tvEquityBankAddress.setText(tvAddress.getText().toString());
                                        tvEquityBranchName.setText(
                                            tvBranchName.getText().toString());
                                        tvEquityIFSC.setText(tvIFSC.getText().toString());
                                        tvEquityMICR.setText(tvMICR.getText().toString());
                                        etEquityAccNumber.setText("");
                                        etConfirmEquityAccNumber.setText("");
                                    } else {
                                        tvEquityBankName.setText("");
                                        tvEquityBankAddress.setText("");
                                        tvEquityBranchName.setText("");
                                        tvEquityIFSC.setText("");
                                        tvEquityMICR.setText("");
                                        etEquityAccNumber.setText("");
                                        etConfirmEquityAccNumber.setText("");
                                        //   dialogCheckOffline.dismiss();
                                    }

                                }
                            });
                            dialogCheckOffline.show();
                            //   informationDialog(tvBankName.getText().toString()+OFFLINE_BANK);

                        } else {
                            tvEquityBankName.setText(tvBankName.getText().toString());
                            tvEquityBankAddress.setText(tvAddress.getText().toString());
                            tvEquityBranchName.setText(tvBranchName.getText().toString());
                            tvEquityIFSC.setText(tvIFSC.getText().toString());
                            tvEquityMICR.setText(tvMICR.getText().toString());
                            etEquityAccNumber.setText("");
                            etConfirmEquityAccNumber.setText("");
                        }
                    } else {
                        llEquBankDetail.setVisibility(View.GONE);
                        commonMsgDialog(mContext, IFSC_code_blank_msg, false, "");
                    }
                }
            });
        } else if (type.equalsIgnoreCase(COMMODITY)) {
            if (spCommSelectBank.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
                inputMap.put(BANK_NAME, "");
            } else {
                inputMap.put(BANK_NAME, spCommSelectBank.getSelectedItem().toString());
            }
            inputMap.put(BANK_BRANCH, etCommMICR.getText().toString());
            //            inputMap.put(BANK_BRANCH, "400240003");
            inputMap.put(APPLICATION_TYPE, getAppType());

            lvBankBranch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final TextView tvBankName, tvAddress, tvBranchName, tvMICR, tvIFSC,
                        tvEqOnlineBankFlag, tvCommOnlineBankFlag;
                    tvBankName = (TextView) view.findViewById(R.id.tvBankName);
                    tvAddress = (TextView) view.findViewById(R.id.tvAddress);
                    tvBranchName = (TextView) view.findViewById(R.id.tvBranchName);
                    tvMICR = (TextView) view.findViewById(R.id.tvMICR);
                    tvIFSC = (TextView) view.findViewById(R.id.tvIFSC);
                    tvEqOnlineBankFlag = (TextView) view.findViewById(R.id.tvEqOnlineBankFlag);
                    tvCommOnlineBankFlag = (TextView) view.findViewById(R.id.tvCommOnlineBankFlag);


                  /*  if (tvEqOnlineBankFlag.getText().toString().equals("1")) {
                        setEqOnlineBank(true);
                    } else if (tvEqOnlineBankFlag.getText().toString().equals("0")) {
                        setEqOnlineBank(false);
                    }*/

                    etCommAccNumber.requestFocus();
                    //                    etCommMICR.setText("");
                    dialog.dismiss();
                    if (!tvIFSC.getText().toString().equals("")) {
                        llCommBankDetails.setVisibility(View.VISIBLE);
                        if (tvCommOnlineBankFlag.getText().toString().equals("0")) {
                            final CustomDialog dialogCheckOffline = new CustomDialog(mContext,
                                tvBankName.getText().toString() + OFFLINE_BANK, true, true, false,
                                "", YES, NO, new CustomDialog.CustomDialogListener() {
                                @Override
                                public void dialogAction(MOSLCommon.Action action, Object... data) {

                                    if (action == MOSLCommon.Action.OK) {
                                        tvCommBankName.setText(tvBankName.getText().toString());
                                        tvCommBranchName.setText(tvBranchName.getText().toString());
                                        tvCommMICR.setText(tvMICR.getText().toString());
                                        tvCommIFSC.setText(tvIFSC.getText().toString());
                                        tvCommBankAddress.setText(tvAddress.getText().toString());
                                        etCommAccNumber.setText("");
                                        etConfirmCommAccNumber.setText("");
                                    } else {
                                        tvCommBankName.setText("");
                                        tvCommBranchName.setText("");
                                        tvCommMICR.setText("");
                                        tvCommIFSC.setText("");
                                        tvCommBankAddress.setText("");
                                        etCommAccNumber.setText("");
                                        etConfirmCommAccNumber.setText("");
                                        //   dialogCheckOffline.dismiss();
                                    }

                                }
                            });
                            dialogCheckOffline.show();
                            //   informationDialog(tvBankName.getText().toString()+OFFLINE_BANK);

                        } else {
                            tvCommBankName.setText(tvBankName.getText().toString());
                            tvCommBranchName.setText(tvBranchName.getText().toString());
                            tvCommMICR.setText(tvMICR.getText().toString());
                            tvCommIFSC.setText(tvIFSC.getText().toString());
                            tvCommBankAddress.setText(tvAddress.getText().toString());
                            etCommAccNumber.setText("");
                            etConfirmCommAccNumber.setText("");
                        }
                    } else {
                        llCommBankDetails.setVisibility(View.GONE);
                        commonMsgDialog(mContext, IFSC_code_blank_msg, false, "");
                    }
                }
            });
        } else if (type.equalsIgnoreCase(MF)) {
            if (spMFSelectBank.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
                inputMap.put(BANK_NAME, "");
            } else {
                inputMap.put(BANK_NAME, spMFSelectBank.getSelectedItem().toString());
            }
            inputMap.put(BANK_BRANCH, etMFMICR.getText().toString());
            //            inputMap.put(BANK_BRANCH, "400240003");
            inputMap.put(APPLICATION_TYPE, getAppType());

            lvBankBranch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tvBankName, tvAddress, tvBranchName, tvMICR, tvIFSC;
                    tvBankName = (TextView) view.findViewById(R.id.tvBankName);
                    tvAddress = (TextView) view.findViewById(R.id.tvAddress);
                    tvBranchName = (TextView) view.findViewById(R.id.tvBranchName);
                    tvMICR = (TextView) view.findViewById(R.id.tvMICR);
                    tvIFSC = (TextView) view.findViewById(R.id.tvIFSC);
                    if (!tvIFSC.getText().toString().equals("")) {
                        llMfBankDetails.setVisibility(View.VISIBLE);
                        tvMFBankName.setText(tvBankName.getText().toString());
                        tvMFBranchName.setText(tvBranchName.getText().toString());
                        tvMFMICR.setText(tvMICR.getText().toString());
                        tvMFIFSC.setText(tvIFSC.getText().toString());
                        tvMFBankAddress.setText(tvAddress.getText().toString());
                        etMFAccNumber.setText("");
                        etConfirmMFAccNumber.setText("");
                        etMFAccNumber.requestFocus();
                        //                    etCommMICR.setText("");
                        dialog.dismiss();
                    } else {
                        llMfBankDetails.setVisibility(View.GONE);
                        commonMsgDialog(mContext, IFSC_code_blank_msg, false, "");
                    }
                }
            });
        }
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        branchModelList = new ArrayList<BankBranchModel>();
                        if (response != null && response.length() != 0) {
                            //                        arrEquityBankDetail.clear();
                            try {
                                JSONArray jsonArray = response.getJSONArray("BANKSEARCHDATA");
                                if (jsonArray.length() != 0) {


                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        if (!getAppType().equalsIgnoreCase(DIRECT)) {
                                            String micrCode = object.getString("MICR CODE");
                                            String ifscCode = object.getString("IFSC CODE");
                                            String bankName = object.getString("BANK NAME");
                                            String bankAdd = object.getString("BANK ADDRESS");
                                            String branchName = object.getString("BRANCH NAME");
                                            String eqOnlineBankFlag =
                                                object.getString("EQONLINEBANKFLAG");
                                            String commOnlineBankFlag =
                                                object.getString("COMMONLINEBANKFLAG");
                                            branchModelList.add(
                                                new BankBranchModel(bankName, branchName, micrCode,
                                                    ifscCode, bankAdd, eqOnlineBankFlag,
                                                    commOnlineBankFlag));
                                        } else {
                                            String micrCode = object.getString("MICR CODE");
                                            String ifscCode = object.getString("IFSC CODE");
                                            String bankName = object.getString("BANK NAME");
                                            String bankAdd = object.getString("BANK ADDRESS");
                                            String branchName = object.getString("BRANCH NAME");
                                            String eqOnlineBankFlag = "";
                                            String commOnlineBankFlag = "";
                                            branchModelList.add(
                                                new BankBranchModel(bankName, branchName, micrCode,
                                                    ifscCode, bankAdd, eqOnlineBankFlag,
                                                    commOnlineBankFlag));
                                        }
                                    }
                                    dialog.show();
                                    bankBranchAdapter =
                                        new BankBranchAdapter(mContext, branchModelList);
                                    lvBankBranch.setAdapter(bankBranchAdapter);
                                    bankBranchAdapter.notifyDataSetChanged();
                                } else {
                                    informationDialog(NO_BANK_BRANCH_FOUND);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
                            }
                            customProgress.setVisibility(View.GONE);
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        if (statusCode == 0) {
                            CustomDialog customDialog =
                                new CustomDialog(mContext, CONNECTION_TIMEOUT, true, false, false,
                                    "", OK, "", new CustomDialog.CustomDialogListener() {
                                    @Override
                                    public void dialogAction(MOSLCommon.Action action,
                                        Object... data) {

                                    }
                                });
                            customDialog.show();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
        } else {
            MOSLCommon.alertDInternet(mContext);
        }
        // Capture Text in EditText
        editextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = editextSearch.getText().toString().toLowerCase(Locale.getDefault());
                bankBranchAdapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                int arg3) {
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onClick(View v) {
        try {
            int id = v.getId();
            if (id == R.id.btnCommBankDetails) {
                if (etCommMICR.getText().toString().equals("")
                    && (spCommSelectBank.getSelectedItem() == null
                    || ((RowData) spCommSelectBank.getSelectedItem()).getKey().equals("-1"))) {
                    etCommSelectBankError.setVisibility(View.VISIBLE);
                    etCommSelectBankError.setText(SELECT_BANK_DETAIL_FOR_SEARCH);
                    etCommSelectBankError.requestFocus();
                    return;
                } else {
                    etCommSelectBankError.setVisibility(View.GONE);
                    getBankDetailsSearch(COMMODITY);
                }
            } else if (id == R.id.btnEquityBankDetails) {
                if (etEquityMICR.getText().toString().equals("")
                    && (spEquSelectBank.getSelectedItem() == null
                    || ((RowData) spEquSelectBank.getSelectedItem()).getKey().equals("-1"))) {
                    etEquSelectBankError.setVisibility(View.VISIBLE);
                    etEquSelectBankError.setText(SELECT_BANK_DETAIL_FOR_SEARCH);
                    etEquSelectBankError.requestFocus();
                    return;
                } else {
                    etEquSelectBankError.setVisibility(View.GONE);
                    getBankDetailsSearch(EQUITY);
                }
            } else if (id == R.id.btnMFBankDetails) {
                if (etMFMICR.getText().toString().equals("") && (spMFSelectBank.getSelectedItem()
                    == null || ((RowData) spMFSelectBank.getSelectedItem()).getKey()
                    .equals("-1"))) {
                    etMFSelectBankError.setVisibility(View.VISIBLE);
                    etMFSelectBankError.setText(SELECT_BANK_DETAIL_FOR_SEARCH);
                    etMFSelectBankError.requestFocus();
                    return;
                } else {
                    etMFSelectBankError.setVisibility(View.GONE);
                    getBankDetailsSearch(MF);
                }
            } else if (id == R.id.btnMFAddBankDetails) {
                if (validateMF()) {
                    removeKeyboard(mContext, btnMFAddBankDetails);
                    if (arrayListMFBankList != null) {
                        if (arrayListMFBankList.size() == 5) {
                            CustomDialog only5BankDialog = new CustomDialog(mContext,
                                "Maximum selection of 5 Banks is allowed", true, false, false, "",
                                OK, "", new CustomDialog.CustomDialogListener() {
                                @Override
                                public void dialogAction(MOSLCommon.Action action, Object... data) {

                                }
                            });
                            only5BankDialog.show();
                        } else {
                            insertDeleteBankDetail("I", "", tvMFBankName.getText().toString(),
                                tvMFBranchName.getText().toString(),
                                tvMFBankAddress.getText().toString(),
                                etMFAccNumber.getText().toString(), tvMFMICR.getText().toString(),
                                tvMFIFSC.getText().toString());
                        }
                    } else {
                        insertDeleteBankDetail("I", "", tvMFBankName.getText().toString(),
                            tvMFBranchName.getText().toString(),
                            tvMFBankAddress.getText().toString(),
                            etMFAccNumber.getText().toString(), tvMFMICR.getText().toString(),
                            tvMFIFSC.getText().toString());
                    }

                }

            } else if (id == R.id.btnBankAndSchemeUpdate) {
                if (getInterestedInMF()) {
                    // if (cbMFTC.isChecked()) {
                    if (arrayListMFBankList.size() != 0) {
                        //                        if (!selectedBankcbdID.equals("")) {
                        if (!mfBankListAdapter.getSelectedItemCDBID().equals("")) {
                            ValidateBankDetailForMF();
                        } else {
                            CustomDialog cd =
                                new CustomDialog(mContext, "Select default bank detail", true,
                                    false, false, "", OK, "",
                                    new CustomDialog.CustomDialogListener() {
                                        @Override
                                        public void dialogAction(MOSLCommon.Action action,
                                            Object... data) {

                                        }
                                    });
                            cd.show();
                        }
                    } else {
                        CustomDialog cd = new CustomDialog(mContext,
                            "Kindly select at least single bank before moving to next step.", true,
                            false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                            @Override
                            public void dialogAction(MOSLCommon.Action action, Object... data) {

                            }
                        });
                        cd.show();
                    }
                   /* } else {
                        tcNotAcceptedDialog();
                    }*/
                } else {
                    boolean isAllPass = false;

                    if (isEquityOriginal() && isCommodityOriginal()) {
                        isAllPass = validateEquity();

                        if (isAllPass) {
                            isAllPass = validateCommodity();
                        }
                    } else if (isEquityOriginal()) {
                        isAllPass = validateEquity();
                    } else if (isCommodityOriginal()) {
                        isAllPass = validateCommodity();
                    } else if (getInterestedInMF()) {
                        isAllPass = validateMF();
                    }

                    if (isAllPass) {
                        updateClientBankData();
                    }
                }
            } else if (id == R.id.tvEquSchemeTermCondition) {
                final Dialog schemeEquTermConditionDialog = new Dialog(mContext);
                schemeEquTermConditionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                schemeEquTermConditionDialog.setContentView(R.layout.frag_term_and_condition);
                schemeEquTermConditionDialog.getWindow()
                    .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                schemeEquTermConditionDialog.setCanceledOnTouchOutside(false);
                Button btnClose = (Button) schemeEquTermConditionDialog.findViewById(R.id.btnClose);
                TextView tvTerms =
                    (TextView) schemeEquTermConditionDialog.findViewById(R.id.tvTerms);
                tvTerms.setText(getResources().getString(R.string.terms_equity_margin_scheme));
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        schemeEquTermConditionDialog.dismiss();
                    }
                });
                schemeEquTermConditionDialog.show();
            } else if (id == R.id.tvEquValuePackTermCondition) {
                final Dialog schemeEquTermConditionDialog = new Dialog(mContext);
                schemeEquTermConditionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                schemeEquTermConditionDialog.setContentView(R.layout.frag_term_and_condition);
                schemeEquTermConditionDialog.getWindow()
                    .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                schemeEquTermConditionDialog.setCanceledOnTouchOutside(false);
                Button btnClose = (Button) schemeEquTermConditionDialog.findViewById(R.id.btnClose);
                TextView tvTerms =
                    (TextView) schemeEquTermConditionDialog.findViewById(R.id.tvTerms);
                tvTerms.setText(
                    getResources().getString(R.string.terms_value_pack_equity_margin_scheme));
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        schemeEquTermConditionDialog.dismiss();
                    }
                });
                schemeEquTermConditionDialog.show();
            } else if (id == R.id.tvCommSchemeTermCondition) {
                final Dialog schemeCommTermConditionDialog = new Dialog(mContext);
                schemeCommTermConditionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                schemeCommTermConditionDialog.setContentView(R.layout.frag_term_and_condition);
                schemeCommTermConditionDialog.getWindow()
                    .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                schemeCommTermConditionDialog.setCanceledOnTouchOutside(false);
                Button btnClose =
                    (Button) schemeCommTermConditionDialog.findViewById(R.id.btnClose);
                TextView tvTerms =
                    (TextView) schemeCommTermConditionDialog.findViewById(R.id.tvTerms);
                tvTerms.setText(getResources().getString(R.string.terms_equity_margin_scheme));
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        schemeCommTermConditionDialog.dismiss();
                    }
                });
                schemeCommTermConditionDialog.show();
            } else if (id == R.id.btnAllScheme) {
                getSchemeDetailsForValuePack();
            } else if (id == R.id.btnMarginAllScheme) {
                getSchemeDetailsForValuePack();
            } else if (id == R.id.btnCommMarginAllScheme) {
                getSchemeDetailsForCommodity();
            } else if (id == R.id.btnSelectedScheme) {
                bindValuePackOnSelectedScheme(strEquSchID);
            } else if (id == R.id.btnMarginSelectedScheme) {
                bindEquDetailsOnSelectedScheme(strEquSchID);
            } else if (id == R.id.btnCommMarginSelectedScheme) {
                bindCommDetailsOnSelectedScheme(strComSchID);
            }
            /*else if (id == R.id.tvMFReviewForm) {
                getReviewDetail();
            }*/
            if (id == R.id.rbMarginAcc) {
                btnMarginAllScheme.setVisibility(View.VISIBLE);
                btnMarginSelectedScheme.setVisibility(View.VISIBLE);
                if (rbMarginAcc.isChecked()) {
                    etEquAccountError.setVisibility(View.GONE);
                    removeKeyboard(mContext, getView());
                    rbMarginAcc.setChecked(true);
                    rbValuePacAcc.setChecked(false);
                    llSelectedScheme.setVisibility(View.GONE);
                    llMarginDeposit.setVisibility(View.VISIBLE);
                    getBindMarginSchemeDetails("");
                } else {
                    btnMarginAllScheme.setVisibility(View.GONE);
                    btnMarginSelectedScheme.setVisibility(View.GONE);
                    rbValuePacAcc.setChecked(true);
                    rbMarginAcc.setChecked(false);
                    llSelectedScheme.setVisibility(View.VISIBLE);
                    llMarginDeposit.setVisibility(View.GONE);
                }
            } else if (id == R.id.rbValuePacAcc) {
                btnMarginAllScheme.setVisibility(View.GONE);
                btnMarginSelectedScheme.setVisibility(View.GONE);
                if (rbValuePacAcc.isChecked()) {
                    etEquAccountError.setVisibility(View.GONE);
                    removeKeyboard(mContext, getView());
                    rbMarginAcc.setChecked(false);
                    rbValuePacAcc.setChecked(true);
                    llSelectedScheme.setVisibility(View.VISIBLE);
                    llMarginDeposit.setVisibility(View.GONE);
                    getBindValuePacSchemeDetails("");
                } else {
                    btnMarginAllScheme.setVisibility(View.VISIBLE);
                    rbValuePacAcc.setChecked(false);
                    rbMarginAcc.setChecked(true);
                    llSelectedScheme.setVisibility(View.GONE);
                    llMarginDeposit.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception ex) {
            commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
            //            MOSLCommon.showToastMsg(mContext, "Error Occurred");
            ex.printStackTrace();
        }
    }

    public boolean validateMF() {
        if (tvMFBankName.getText().toString().equals("")) {
            etMFSelectBankError.setVisibility(View.VISIBLE);
            etMFSelectBankError.setText(SELECT_BANK_DETAIL_FOR_SEARCH);
            etMFSelectBankError.requestFocus();
            return false;
        } else if (!validateEditText(etMFAccNumber, input_layout_MFAccNumber,
            "Acc no." + CANNOT_BE_LEFT_BLANK)) {

            //  informationDialog("Equity Acc no." + CANNOT_BE_LEFT_BLANK);
            return false;
        } else if (etMFAccNumber.length() < 4) {
            etMFAccNumberError.setText(ErrorMsgAccountNoLength);
            etMFAccNumberError.setVisibility(View.VISIBLE);
            etMFAccNumber.requestFocus();
            return false;
        } else if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
            etMFAccNumber.getText().toString().trim())) {
            etMFAccNumberError.setVisibility(View.VISIBLE);
            etMFAccNumberError.setText(ErrorMsgAccountNo);
            etMFAccNumber.requestFocus();
            return false;
        } else if (ValidationRule.validateOnlyAlphabetWithoutSplChar(
            etMFAccNumber.getText().toString().trim())) {
            etMFAccNumberError.setVisibility(View.VISIBLE);
            etMFAccNumberError.setText(ErrorMsgAccountNo);
            etMFAccNumber.requestFocus();
            return false;
        } else if (!etMFAccNumber.getText()
            .toString()
            .trim()
            .equals(etConfirmMFAccNumber.getText().toString().trim())) {
            etConfirmMFAccNumberError.setVisibility(View.VISIBLE);
            etConfirmMFAccNumberError.setText(ErrorMsgConfirmAccountNo);
            etConfirmMFAccNumber.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validateCommodity() {
        //        Commodity scheme changes 14-03-2018
        /*if (tvCommBankName.getText().toString().equals("")) {
            etCommSelectBankError.setVisibility(View.VISIBLE);
            etCommSelectBankError.setText("Select bank");
            etCommSelectBankError.requestFocus();
            return false;
        } else if (!validateEditText(etCommAccNumber, input_layout_commAccNumber, "Acc no." + CANNOT_BE_LEFT_BLANK)) {

            //  informationDialog("Equity Acc no." + CANNOT_BE_LEFT_BLANK);
            return false;
        } else if (etCommAccNumber.length() < 4) {
            etCommAccNumberError.setText(ErrorMsgAccountNoLength);
            etCommAccNumberError.setVisibility(View.VISIBLE);
            etCommAccNumber.requestFocus();
            return false;
        } else if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(etCommAccNumber.getText().toString().trim())) {
            etCommAccNumberError.setVisibility(View.VISIBLE);
            etCommAccNumberError.setText(ErrorMsgAccountNo);
            etCommAccNumber.requestFocus();
            return false;
        } else if (ValidationRule.validateOnlyAlphabetWithoutSplChar(etCommAccNumber.getText().toString().trim())) {
            etCommAccNumberError.setVisibility(View.VISIBLE);
            etCommAccNumberError.setText(ErrorMsgAccountNo);
            etCommAccNumber.requestFocus();
            return false;
        } else if (!etCommAccNumber.getText().toString().trim().equals(etConfirmCommAccNumber.getText().toString().trim())) {
            etConfirmCommAccNumberError.setVisibility(View.VISIBLE);
            etConfirmCommAccNumberError.setText(ErrorMsgConfirmAccountNo);
            etConfirmCommAccNumber.requestFocus();
            return false;
        } else if (spCommBankProof.getSelectedItem() == null || ((RowData) spCommBankProof.getSelectedItem()).getKey().equals("-1")) {
            etCommBankProoflError.setVisibility(View.VISIBLE);
            etCommBankProoflError.setText("Select" + MOSLConstants.STR_BANK_PROOF);
            etCommBankProoflError.requestFocus();
            return false;
        } else if (spCommPhotoProof.getSelectedItem() == null || ((RowData) spCommPhotoProof.getSelectedItem()).getKey().equals("-1")) {
            etCommPhotoProofError.setVisibility(View.VISIBLE);
            etCommPhotoProofError.setText("Select" + MOSLConstants.STR_PHOTO_PROOF);
            etCommPhotoProofError.requestFocus();
            return false;
        }*/
        if (tvEquityBankName.getText().toString().equals("")) {
            etEquSelectBankError.setVisibility(View.VISIBLE);
            etEquSelectBankError.setText("Select bank");
            etEquSelectBankError.requestFocus();
            return false;
        } else if (!validateEditText(etEquityAccNumber, input_layout_equityAccNumber,
            "Acc no." + CANNOT_BE_LEFT_BLANK)) {
            return false;
        } else if (etEquityAccNumber.length() < 4) {
            etEquityAccNumberError.setText(ErrorMsgAccountNoLength);
            etEquityAccNumberError.setVisibility(View.VISIBLE);
            etEquityAccNumber.requestFocus();
            return false;
        } else if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
            etEquityAccNumber.getText().toString().trim())) {
            etEquityAccNumberError.setVisibility(View.VISIBLE);
            etEquityAccNumberError.setText(ErrorMsgAccountNo);
            etEquityAccNumber.requestFocus();
            return false;
        } else if (ValidationRule.validateOnlyAlphabetWithoutSplChar(
            etEquityAccNumber.getText().toString().trim())) {
            etEquityAccNumberError.setVisibility(View.VISIBLE);
            etEquityAccNumberError.setText(ErrorMsgAccountNo);
            etEquityAccNumber.requestFocus();
            return false;
        } else if (!etEquityAccNumber.getText()
            .toString()
            .trim()
            .equals(etConfirmEquityAccNumber.getText().toString().trim())) {
            etConfirmEquityAccNumberError.setVisibility(View.VISIBLE);
            etConfirmEquityAccNumberError.setText(ErrorMsgConfirmAccountNo);
            etConfirmEquityAccNumber.requestFocus();
            return false;
        } else if (spEquBankProof.getSelectedItem() == null
            || ((RowData) spEquBankProof.getSelectedItem()).getKey().equals("-1")) {
            etEquBankProoflError.setVisibility(View.VISIBLE);
            etEquBankProoflError.setText("Select" + MOSLConstants.STR_BANK_PROOF);
            etEquBankProoflError.requestFocus();
            return false;
        } else if (spEquPhotoProof.getSelectedItem() == null
            || ((RowData) spEquPhotoProof.getSelectedItem()).getKey().equals("-1")) {
            etEquPhotoProofError.setVisibility(View.VISIBLE);
            etEquPhotoProofError.setText("Select" + MOSLConstants.STR_PHOTO_PROOF);
            etEquPhotoProofError.requestFocus();
            return false;
        }
        //scheme
        else if (getAppType().equals(DIRECT) || getAppType().equals(CRM)) {
            if (strComSchID.equals("9999")) {
                String otherAmountComm = etCommMarginDeposit.getText().toString();
                if (otherAmountComm.equals("")) {
                    validateEditText(etCommMarginDeposit, input_layout_commMarginDeposit,
                        getAppType().equalsIgnoreCase(DIRECT) ? getResources().getString(
                            R.string.margin_25000)
                            : getResources().getString(R.string.margin_50000));
                    return false;
                } else if (etCommMarginDeposit.isEnabled()
                    && Double.parseDouble(otherAmountComm) < minMarginValueForOtherAmount()) {
                    validateEditText(etCommMarginDeposit, input_layout_commMarginDeposit,
                        getAppType().equalsIgnoreCase(DIRECT) ? getResources().getString(
                            R.string.margin_25000)
                            : getResources().getString(R.string.margin_50000));
                    return false;
                }

                /*if (otherAmountComm != null && otherAmountComm.trim().length() != 0 && Double.parseDouble(otherAmountComm) < 10000) {
                    validateEditText(etCommMarginDeposit, input_layout_commMarginDeposit, "Commodity Margin Deposit cannot be less than 10,000");

                    // informationDialog("Commodity Margin Deposit cannot be less than 10,000");
                    return false;
                }*/
            } else if (isEquityOriginal() && spEquScheme.getSelectedItem()
                .toString()
                .equalsIgnoreCase(SELECT)) {
                //  informationDialog("Commodity Scheme." + CANNOT_BE_LEFT_BLANK);
                etEquSchemeError.setVisibility(View.VISIBLE);
                etEquSchemeError.setText("Select scheme");
                etEquSchemeError.requestFocus();
                return false;
            } else if (!isEquityOriginal() && spCommScheme.getSelectedItem()
                .toString()
                .equalsIgnoreCase(SELECT)) {
                //  informationDialog("Commodity Scheme." + CANNOT_BE_LEFT_BLANK);
                etCommSchemeError.setVisibility(View.VISIBLE);
                etCommSchemeError.setText("Select scheme");
                etCommSchemeError.requestFocus();
                return false;
            }
        } else {
            if (spCommBrokerageScheme.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
                etCommBrokerageSchemeError.setVisibility(View.VISIBLE);
                etCommBrokerageSchemeError.setText("Select MCX Brokerage Scheme");
                etCommBrokerageSchemeError.requestFocus();
                return false;
            } else if (spCommFuture.getSelectedItem() == null
                || ((RowData) spCommFuture.getSelectedItem()).getKey().equals("-1")) {
                etCommFutureError.setVisibility(View.VISIBLE);
                etCommFutureError.setText("Select MCX Future Scheme");
                etCommFutureError.requestFocus();
                return false;
            } else if (spCommOption.getSelectedItem() == null
                || ((RowData) spCommOption.getSelectedItem()).getKey().equals("-1")) {
                etCommOptionError.setVisibility(View.VISIBLE);
                etCommOptionError.setText("Select MCX Option Scheme");
                etCommOptionError.requestFocus();
                return false;
            } else if (spCommDPAMC.getSelectedItem() == null
                || ((RowData) spCommDPAMC.getSelectedItem()).getKey().equals("-1")) {
                etCommDPAMCError.setVisibility(View.VISIBLE);
                etCommDPAMCError.setText("Select DP AMC Scheme");
                etCommDPAMCError.requestFocus();
                return false;
            } else if (spNCXBrokerageScheme.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
                etNCXBrokerageSchemeError.setVisibility(View.VISIBLE);
                etNCXBrokerageSchemeError.setText("Select NCX Brokerage Scheme");
                etNCXBrokerageSchemeError.requestFocus();
                return false;
            } else if (spNCXFuture.getSelectedItem() == null
                || ((RowData) spNCXFuture.getSelectedItem()).getKey().equals("-1")) {
                etNCXFutureError.setVisibility(View.VISIBLE);
                etNCXFutureError.setText("Select NCX Future Scheme");
                etNCXFutureError.requestFocus();
                return false;
            } else if (spNCXOption.getSelectedItem() == null
                || ((RowData) spNCXOption.getSelectedItem()).getKey().equals("-1")) {
                etNCXOptionError.setVisibility(View.VISIBLE);
                etNCXOptionError.setText("Select NCX Option Scheme");
                etNCXOptionError.requestFocus();
                return false;
            } else if (spNCXDPAMC.getSelectedItem() == null
                || ((RowData) spNCXDPAMC.getSelectedItem()).getKey().equals("-1")) {
                etNCXDPAMCError.setVisibility(View.VISIBLE);
                etNCXDPAMCError.setText("Select NCX DP AMC Scheme");
                etNCXDPAMCError.requestFocus();
                return false;
            }
        }

        return true;
    }

    public boolean validateEquity() {

        //bank
       /* if (spEquBankProof.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
            informationDialog("Select equity" + MOSLConstants.STR_BANK_PROOF);
            return false;
        } else if (spEquPhotoProof.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
            informationDialog("Select equity" + MOSLConstants.STR_PHOTO_PROOF);
            return false;
        } else if (tvEquityMICR.getText().toString().equals("")) {
            informationDialog("Please search and select your bank for equity");
            return false;
        } else if (etEquityAccNumber.getText().toString().equals("")) {
            informationDialog("Equity Acc no." + CANNOT_BE_LEFT_BLANK);
            return false;
        }*/

        /*if (spEquSelectBank.getSelectedItem() == null || ((RowData) spEquSelectBank.getSelectedItem()).getKey().equals("-1")) {
            etEquSelectBankError.setVisibility(View.VISIBLE);
            etEquSelectBankError.setText("Select bank");
            etEquSelectBankError.requestFocus();
            return false;
        }else if (tvEquityMICR.getText().toString().equals("")) {
            informationDialog("Please search and select your bank for equity");
            return false;
        }  else */
        if (tvEquityBankName.getText().toString().equals("")) {
            etEquSelectBankError.setVisibility(View.VISIBLE);
            etEquSelectBankError.setText("Select bank");
            etEquSelectBankError.requestFocus();
            return false;
        } else if (!validateEditText(etEquityAccNumber, input_layout_equityAccNumber,
            "Acc no." + CANNOT_BE_LEFT_BLANK)) {
            return false;
        } else if (etEquityAccNumber.length() < 4) {
            etEquityAccNumberError.setText(ErrorMsgAccountNoLength);
            etEquityAccNumberError.setVisibility(View.VISIBLE);
            etEquityAccNumber.requestFocus();
            return false;
        } else if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
            etEquityAccNumber.getText().toString().trim())) {
            etEquityAccNumberError.setVisibility(View.VISIBLE);
            etEquityAccNumberError.setText(ErrorMsgAccountNo);
            etEquityAccNumber.requestFocus();
            return false;
        } else if (ValidationRule.validateOnlyAlphabetWithoutSplChar(
            etEquityAccNumber.getText().toString().trim())) {
            etEquityAccNumberError.setVisibility(View.VISIBLE);
            etEquityAccNumberError.setText(ErrorMsgAccountNo);
            etEquityAccNumber.requestFocus();
            return false;
        } else if (!etEquityAccNumber.getText()
            .toString()
            .trim()
            .equals(etConfirmEquityAccNumber.getText().toString().trim())) {
            etConfirmEquityAccNumberError.setVisibility(View.VISIBLE);
            etConfirmEquityAccNumberError.setText(ErrorMsgConfirmAccountNo);
            etConfirmEquityAccNumber.requestFocus();
            return false;
        } else if (spEquBankProof.getSelectedItem() == null
            || ((RowData) spEquBankProof.getSelectedItem()).getKey().equals("-1")) {
            etEquBankProoflError.setVisibility(View.VISIBLE);
            etEquBankProoflError.setText("Select" + MOSLConstants.STR_BANK_PROOF);
            etEquBankProoflError.requestFocus();
            return false;
        } else if (spEquPhotoProof.getSelectedItem() == null
            || ((RowData) spEquPhotoProof.getSelectedItem()).getKey().equals("-1")) {
            etEquPhotoProofError.setVisibility(View.VISIBLE);
            etEquPhotoProofError.setText("Select" + MOSLConstants.STR_PHOTO_PROOF);
            etEquPhotoProofError.requestFocus();
            return false;
        }
        ///scheme
        else if ((getAppType().equals("DIRECT") && !getIsAadharVerified())
            && (!rbMarginAcc.isChecked() && !rbValuePacAcc.isChecked())) {
            etEquAccountError.setVisibility(View.VISIBLE);
            etEquAccountError.setText("Select Account");
            etEquAccountError.requestFocus();
            //   informationDialog("Equity Scheme." + CANNOT_BE_LEFT_BLANK);
            return false;
        } else if (getAppType().equals(DIRECT) || getAppType().equals(CRM)) {
            String otherAmountEqu = etEquMarginDeposit.getText().toString();
            if (strEquSchID.equals("9999") && otherAmountEqu.equals("")) {
                validateEditText(etEquMarginDeposit, input_layout_equMarginDeposit,
                    getAppType().equalsIgnoreCase(DIRECT) ? getResources().getString(
                        R.string.margin_25000) : getResources().getString(R.string.margin_50000));
                return false;
            } else if (strEquSchID.equals("9999")
                && etEquMarginDeposit.isEnabled()
                && Double.parseDouble(otherAmountEqu) < minMarginValueForOtherAmount()) {
                validateEditText(etEquMarginDeposit, input_layout_equMarginDeposit,
                    getAppType().equalsIgnoreCase(DIRECT) ? getResources().getString(
                        R.string.margin_25000) : getResources().getString(R.string.margin_50000));
                return false;
            } else if (spEquScheme.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
                etEquSchemeError.setVisibility(View.VISIBLE);
                etEquSchemeError.setText("Select scheme");
                etEquSchemeError.requestFocus();
                //   informationDialog("Equity Scheme." + CANNOT_BE_LEFT_BLANK);
                return false;
            } else if (getAppType().equals(CRM) && spDpScheme.getSelectedItem()
                .toString()
                .equalsIgnoreCase(SELECT)) {
                etDpSchemeError.setVisibility(View.VISIBLE);
                etDpSchemeError.setText("Select DP scheme");
                etDpSchemeError.requestFocus();
                return false;
            }
        } else {
            if (spEquBrokerage.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
                etEquBrokerageError.setVisibility(View.VISIBLE);
                etEquBrokerageError.setText("Select Brokerage Scheme");
                etEquBrokerageError.requestFocus();
                return false;
            } else if (spEquTrading.getSelectedItem() == null
                || ((RowData) spEquTrading.getSelectedItem()).getKey().equals("-1")) {
                etEquTradingError.setVisibility(View.VISIBLE);
                etEquTradingError.setText("Select Trading Scheme");
                etEquTradingError.requestFocus();
                return false;
            } else if (spEquDelivery.getSelectedItem() == null
                || ((RowData) spEquDelivery.getSelectedItem()).getKey().equals("-1")) {
                etEquDeliveryError.setVisibility(View.VISIBLE);
                etEquDeliveryError.setText("Select Delivery Scheme");
                etEquDeliveryError.requestFocus();
                return false;
            } else if (isDerivativeOriginal() && (spEquFuture.getSelectedItem() == null
                || ((RowData) spEquFuture.getSelectedItem()).getKey().equals("-1"))) {
                etEquFutureError.setVisibility(View.VISIBLE);
                etEquFutureError.setText("Select Future Scheme");
                etEquFutureError.requestFocus();
                return false;
            } else if (isDerivativeOriginal() && (spEquOption.getSelectedItem() == null
                || ((RowData) spEquOption.getSelectedItem()).getKey().equals("-1"))) {
                etEquOptionError.setVisibility(View.VISIBLE);
                etEquOptionError.setText("Select Option Scheme");
                etEquOptionError.requestFocus();
                return false;
            } else if (spEquDPAMC.getSelectedItem() == null
                || ((RowData) spEquDPAMC.getSelectedItem()).getKey().equals("-1")) {
                etEquDPAMCError.setVisibility(View.VISIBLE);
                etEquDPAMCError.setText("Select DP AMC Scheme");
                etEquDPAMCError.requestFocus();
                return false;
            } else if (isCurrencyOriginal() && (spEquCurrencyFuture.getSelectedItem() == null
                || ((RowData) spEquCurrencyFuture.getSelectedItem()).getKey().equals("-1"))) {
                etEquCurrencyFutureError.setVisibility(View.VISIBLE);
                etEquCurrencyFutureError.setText("Select Currency Future Scheme");
                etEquCurrencyFutureError.requestFocus();
                return false;
            } else if (isCurrencyOriginal() && (spEquCurrencyOption.getSelectedItem() == null
                || ((RowData) spEquCurrencyOption.getSelectedItem()).getKey().equals("-1"))) {
                etEquCurrencyOptionError.setVisibility(View.VISIBLE);
                etEquCurrencyOptionError.setText("Select Currency Option Scheme");
                etEquCurrencyOptionError.requestFocus();
                return false;
            }
        }

        return true;
    }

    public void insertDeleteBankDetail(String opertion, String cbdID, String bankName,
        String branchName, String bankAdd, String accNum, String micr, String ifsc) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();

            inputMap.put("Operation", opertion);
            inputMap.put("CBD_ID", cbdID);
            inputMap.put("Userid", userID);
            inputMap.put("Bank_Name", bankName);
            inputMap.put("Bank_Branch", branchName);
            inputMap.put("Bank_ADD", bankAdd);
            inputMap.put("BANK_ACC_NO", accNum);
            String bankACType = "";
            if (rbMFCurrent.isChecked()) {
                bankACType = CURRENT;
            } else {
                bankACType = SAVING;
            }

            inputMap.put("BANK_AC_TYPE", bankACType);
            inputMap.put("BANK_MICR_CODE", micr);
            inputMap.put("BANK_IFSC_CODE", ifsc);
            inputMap.put("BANK_PROOF", "");
            inputMap.put("PHOTO_PROOF", "");
            inputMap.put("FIRST_RELATIVE_NAME", "");
            inputMap.put("FIRST_RELATIONSHIP", "");
            inputMap.put("FIRST_UCC", "");
            inputMap.put("SECOND_RELATIVE_NAME", "");
            inputMap.put("SECOND_RELATIONSHIP", "");
            inputMap.put("SECOND_UCC", "");

            String flag = CRUDBankDetails_MF;
            String category = BANKDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);

                    /*[
                    {
                        "CBD_ID":857,
                            "BANK_NAME":"BANK OF BAHRAIN AND KUWAIT",
                            "BANK_BRANCH":"NEW DELHI",
                            "BANK_ADD":"OPP NEHRU PLACE",
                            "BANK_ACC_NO":"4645758",
                            "BANK_AC_TYPE":"Saving",
                            "BANK_MICR_CODE":"110150002",
                            "BANK_IFSC_CODE":"BBKM0000005",
                            "BANK_PROOF":"","PHOTO_PROOF":"",
                            "FIRST_RELATIVE_NAME":"",
                            "FIRST_RELATIONSHIP":"",
                            "FIRST_UCC":"",
                            "SECOND_RELATIVE_NAME":"",
                            "SECOND_RELATIONSHIP":"",
                            "SECOND_UCC":"",
                            "CREATED_BY":1630194,
                            "CREATED_DATE":"2016-12-08T17:40:39.07",
                            "MODIFIED_BY":1630194,
                            "MODIFIED_DATE":"2016-12-08T17:40:39.07",
                            "DEFAULT_FLG":"0",
                            "CHECK_FLG":""
                    },
                    ]*/
                        arrayListMFBankList = new ArrayList<>();
                        if (response != null && response.length() != 0) {

                            for (int i = 0; i < response.length(); i++) {
                                mfMap = new HashMap<String, String>();
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    String cbdID =
                                        (object.isNull("CBD_ID") || object.getString("CBD_ID")
                                            .equals("")) ? "" : object.getString("CBD_ID");
                                    String bankName =
                                        (object.isNull("BANK_NAME") || object.getString("BANK_NAME")
                                            .equals("")) ? "" : object.getString("BANK_NAME");
                                    String branchName =
                                        (object.isNull("BANK_BRANCH") || object.getString(
                                            "BANK_BRANCH").equals("")) ? ""
                                            : object.getString("BANK_BRANCH");
                                    String bankAdd =
                                        (object.isNull("BANK_ADD") || object.getString("BANK_ADD")
                                            .equals("")) ? "" : object.getString("BANK_ADD");
                                    String bankMICRCode =
                                        (object.isNull("BANK_MICR_CODE") || object.getString(
                                            "BANK_MICR_CODE").equals("")) ? ""
                                            : object.getString("BANK_MICR_CODE");
                                    String bankIFSCCode =
                                        (object.isNull("BANK_IFSC_CODE") || object.getString(
                                            "BANK_IFSC_CODE").equals("")) ? ""
                                            : object.getString("BANK_IFSC_CODE");
                                    String bankAccNum =
                                        (object.isNull("BANK_ACC_NO") || object.getString(
                                            "BANK_ACC_NO").equals("")) ? ""
                                            : object.getString("BANK_ACC_NO");
                                    String defaultFlag =
                                        (object.isNull("DEFAULT_FLG") || object.getString(
                                            "DEFAULT_FLG").equals("")) ? ""
                                            : object.getString("DEFAULT_FLG");
                                    String checkFlag =
                                        (object.isNull("CHECK_FLG") || object.getString("CHECK_FLG")
                                            .equals("")) ? "" : object.getString("CHECK_FLG");
                                    String bankAccType =
                                        (object.isNull("BANK_AC_TYPE") || object.getString(
                                            "BANK_AC_TYPE").equals("")) ? ""
                                            : object.getString("BANK_AC_TYPE");
                                    bankStatus =
                                        (object.isNull("Status") || object.getString("Status")
                                            .equals("")) ? "" : object.getString("Status");
                                    bankMessage =
                                        (object.isNull("message") || object.getString("message")
                                            .equals("")) ? "" : object.getString("message");

                                    mfMap.put(CBD_ID, cbdID);
                                    mfMap.put(BANK_NAME, bankName);
                                    mfMap.put(BRANCH_NAME, branchName);
                                    mfMap.put(BANK_ADDRESS, bankName);
                                    mfMap.put(BANK_ACC_NUM, bankAccNum);
                                    mfMap.put(BANK_ACC_TYPE, bankAccType);
                                    mfMap.put(MICR, bankMICRCode);
                                    mfMap.put(IFSC, bankIFSCCode);
                                    mfMap.put(DEFAULT_FLAG, defaultFlag);
                                    mfMap.put(CHECK_FLAG, checkFlag);
                                    mfMap.put(STATUS, bankStatus);
                                    mfMap.put(MESSAGE, bankMessage);


                                    arrayListMFBankList.add(mfMap);
                                    llMfBankDetails.setVisibility(View.GONE);
                                    etMFAccNumber.setText("");
                                    etConfirmMFAccNumber.setText("");
                                /*if (defaultFlag.equals("1")) {
                                    RowData rdBankName = RowData.getSpinnerRowDataByValue(bankName, arrAdapMFBankName);
                                    if (rdBankName != null) {
                                        spMFSelectBank.setSelection(rdBankName.getPosition());
                                    }
                                    tvMFBankName.setText(bankName);
                                    tvMFBranchName.setText(branchName);
                                    tvMFMICR.setText(bankMICRCode);
                                    tvMFIFSC.setText(bankIFSCCode);
                                    tvMFBankAddress.setText(bankAdd);
//                                    etMFAccNumber.setText(bankAccNum);
                                    etMFAccNumber.setText("");
                                    if (bankAccType.equalsIgnoreCase(SAVING)) {
                                        rbMFSaving.setChecked(true);
                                    } else {
                                        rbMFCurrent.setChecked(true);
                                    }

                                } else {
                                    etMFAccNumber.setText("");
                                }*/
                                    customProgress.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    customProgress.setVisibility(View.GONE);
                                }

                            }
                            if (!bankStatus.equals("")) {
                                if (bankStatus.equals("0")) {
                                    commonMsgDialog(mContext, bankMessage, false, "");
                                }
                            }
                            mfBankListAdapter =
                                new MFBankListAdapter(mContext, arrayListMFBankList);
                            lvMFBankDetail.setAdapter(mfBankListAdapter);
                            setListViewHeightBasedOnChildren(lvMFBankDetail);
                            mfBankListAdapter.notifyDataSetChanged();
                            //                        customProgress.setVisibility(View.GONE);
                        } else {
                            /*when there is no bank added*/
                            arrayListMFBankList.clear();
                            mfBankListAdapter =
                                new MFBankListAdapter(mContext, arrayListMFBankList);
                            lvMFBankDetail.setAdapter(mfBankListAdapter);
                            setListViewHeightBasedOnChildren(lvMFBankDetail);
                            mfBankListAdapter.notifyDataSetChanged();
                            customProgress.setVisibility(View.GONE);
                            //                        customProgress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        if (statusCode == 0) {
                            commonMsgDialog(mContext, CONNECTION_TIMEOUT, false, "");
                        }
                        Log.e("Failure", "no resp in Data");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });

        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    public void ValidateBankDetailForMF() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();

            inputMap.put("Userid", userID);
            inputMap.put("BANK_ACC_NO", "");
            inputMap.put("BANK_IFSC_CODE", "");
            inputMap.put("Level", "F");
            //            inputMap.put("CBD_ID", selectedBankcbdID);
            inputMap.put("CBD_ID", mfBankListAdapter.getSelectedItemCDBID());


            String flag = ValidateBankDtls;
            String category = BANKDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            String success =
                                (response.isNull("_Success") || response.getString("_Success")
                                    .equals("")) ? "" : response.getString("_Success");
                            String msg =
                                (response.isNull("_Msg") || response.getString("_Msg").equals(""))
                                    ? "" : response.getString("_Msg");

                            if (success.equals("0") || success.equals("")) {
                                CustomDialog cdMsg =
                                    new CustomDialog(mContext, msg, true, false, false, "", OK, "",
                                        new CustomDialog.CustomDialogListener() {
                                            @Override
                                            public void dialogAction(MOSLCommon.Action action,
                                                Object... data) {

                                            }
                                        });
                                cdMsg.show();

                            } else {
                                getReviewDetail();
                                //viewPager.setCurrentItem(Upload_Tab, false);
                            }
                            customProgress.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            customProgress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        customProgress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        Log.e("Failure", "no resp in Data");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                    }
                });

        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    public void insertUpdateSchDetailsForDirCRM(final String bankMsg) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            String otherAmountComm = "", otherAmount = "", productId = "", flag = "";
            if (isEquityOriginal()
                && isCommodityOriginal()
                && llEquityBankScheme.getVisibility() == View.VISIBLE
                && llCommodityBankScheme.getVisibility() == View.VISIBLE) {
                if (strComSchID.equals("9999")) {
                    otherAmountComm = etCommMarginDeposit.getText().toString();
                } else {
                    otherAmountComm = "";
                }
                if (strEquSchID.equals("9999")) {
                    otherAmount = etEquMarginDeposit.getText().toString();
                } else {
                    otherAmount = "";
                }

                if (getAppType().equals(DIRECT) && !getIsAadharVerified()) {
                    if (rbMarginAcc.isChecked()) {
                        productId = "1";
                    } else if (rbValuePacAcc.isChecked()) {
                        productId = "2";
                    }
                } else {
                    productId = "1";
                }
                //            inputMap.put("_userId", MOSLCommon.USER_ID);
                inputMap.put("_userId", userID);
                inputMap.put("_userType", getAppType());
                inputMap.put("_SchemeID", strEquSchID);
                inputMap.put("_OtherAmount", otherAmount);
                inputMap.put("_SchemeIDComm", strComSchID);
                inputMap.put("_ProductID", productId);
                inputMap.put("_OtherAmountComm", otherAmountComm);
                if (llequSchInfoCurrFuture.getVisibility() == View.VISIBLE) {
                    inputMap.put("_CurrencyFuture", tvequSchInfoCurrFuture.getText().toString());
                    inputMap.put("_CurrencyOption", tvequSchInfoCurrOption.getText().toString());
                } else {
                    inputMap.put("_CurrencyFuture", "");
                    inputMap.put("_CurrencyOption", "");
                }
                if (getAppType().equalsIgnoreCase(DIRECT)) {
                    inputMap.put("DPSCHEME", "");
                } else if (getAppType().equalsIgnoreCase(CRM)) {
                    inputMap.put("DPSCHEME", spDpScheme.getSelectedItem());
                }
            } else if (isEquityOriginal() && llEquityBankScheme.getVisibility() == View.VISIBLE) {
                if (strEquSchID.equals("9999")) {
                    otherAmount = etEquMarginDeposit.getText().toString();
                } else {
                    otherAmount = "";
                }
                if (getAppType().equals(DIRECT) && !getIsAadharVerified()) {
                    if (rbMarginAcc.isChecked()) {
                        productId = "1";
                    } else if (rbValuePacAcc.isChecked()) {
                        productId = "2";
                    }
                } else {
                    productId = "1";
                }

                //            inputMap.put("_userId", MOSLCommon.USER_ID);
                inputMap.put("_userId", userID);
                inputMap.put("_userType", getAppType());
                inputMap.put("_SchemeID", strEquSchID);
                inputMap.put("_OtherAmount", otherAmount);
                inputMap.put("_SchemeIDComm", "");
                inputMap.put("_ProductID", productId);
                inputMap.put("_OtherAmountComm", "");
                if (llequSchInfoCurrFuture.getVisibility() == View.VISIBLE) {
                    inputMap.put("_CurrencyFuture", tvequSchInfoCurrFuture.getText().toString());
                    inputMap.put("_CurrencyOption", tvequSchInfoCurrOption.getText().toString());
                } else {
                    inputMap.put("_CurrencyFuture", "");
                    inputMap.put("_CurrencyOption", "");
                }
                if (getAppType().equalsIgnoreCase(DIRECT)) {
                    inputMap.put("DPSCHEME", "");
                } else if (getAppType().equalsIgnoreCase(CRM)) {
                    inputMap.put("DPSCHEME", spDpScheme.getSelectedItem());
                }
            } else if (isCommodityOriginal()
                && llCommodityBankScheme.getVisibility() == View.VISIBLE) {
                if (strComSchID.equals("9999")) {
                    otherAmountComm = etCommMarginDeposit.getText().toString();
                } else {
                    otherAmountComm = "";
                }
                if (getAppType().equals(DIRECT) && !getIsAadharVerified()) {
                    if (rbMarginAcc.isChecked()) {
                        productId = "1";
                    } else if (rbValuePacAcc.isChecked()) {
                        productId = "2";
                    }
                } else {
                    productId = "1";
                }
                //            inputMap.put("_userId", MOSLCommon.USER_ID);
                inputMap.put("_userId", userID);
                inputMap.put("_userType", getAppType());
                inputMap.put("_SchemeID", "");
                inputMap.put("_OtherAmount", "");
                inputMap.put("_SchemeIDComm", strComSchID);
                inputMap.put("_ProductID", productId);
                inputMap.put("_OtherAmountComm", otherAmountComm);
                inputMap.put("_CurrencyFuture", "");
                inputMap.put("_CurrencyOption", "");
            }


            if (rbValuePacAcc.isChecked()) {
                flag = INSERTUPDATESCHEMEDETAILSMARGINORVALUEPAC;
            } else {
                flag = INSERTUPDATESCHEMEDETAILS;
            }


            String category = SCHEMEDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("Table1");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String status =
                                            (object.isNull("Status") || object.getString("Status")
                                                .equals("")) ? "" : object.getString("Status");
                                        String msg =
                                            (object.isNull("Message") || object.getString("Message")
                                                .equals("")) ? "" : object.getString("Message");
                                        if (Integer.parseInt(status) != 1) {
                                            //                                        MOSLCommon.alertDialog(mContext, 0, MOTILAL_OSWAL, msg, OK, false, null);
                                            informationDialog(msg);
                                            //updateSchemeDetail = false;
                                        } else {
                                            //updateSchemeDetail = true;

                                            //if (updateBankDetail && updateSchemeDetail) {
                                            getReviewDetail();
                                            //}
                                            //                                        informationDialog("Bank: " + bankMsg + "\nScheme: " + msg);
                                        }
                                    }
                                }

                                customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            customProgress.setVisibility(View.GONE);
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
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        Log.e("Failure", "no resp in Data");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });

        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }


    private void skipPayment() {

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            //            inputMap.put("_userId", "1451500");
            inputMap.put("_userId", userID);
            inputMap.put("_segmentType", EQUITY);

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
                                            viewPager.setCurrentItem(getUploadTab());

                                        } else {
                                            informationDialog(COULD_NOT_SKIP_PAY);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false,
                                    "");
                                customProgress.setVisibility(View.GONE);
                            }
                        } else {
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
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
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
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


    private void updateClientBankData() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();

            if (rbEquCurrent.isChecked()) {
                strcbEqu = CURRENT;
            } else {
                strcbEqu = SAVING;
            }
            if (rbCommCurrent.isChecked()) {
                strcbComm = CURRENT;
            } else {
                strcbComm = SAVING;
            }

            if (isEquityOriginal()
                && isCommodityOriginal()
                && llEquityBankScheme.getVisibility() == View.VISIBLE
                && llCommodityBankScheme.getVisibility() == View.VISIBLE) {
                inputMap.put("BankName", tvEquityBankName.getText().toString());
                inputMap.put("BankBranch", tvEquityBranchName.getText().toString());
                inputMap.put("BankAdd", tvEquityBankAddress.getText().toString());
                inputMap.put("BankAccNo", etEquityAccNumber.getText().toString());
                inputMap.put("BankAccType", strcbEqu);
                inputMap.put("BankMicrCode", tvEquityMICR.getText().toString());
                inputMap.put("BankIfscCode", tvEquityIFSC.getText().toString());
                inputMap.put("BankProof", spEquBankProof.getSelectedItem().toString());
                inputMap.put("PhotoProof", spEquPhotoProof.getSelectedItem().toString());
                inputMap.put("CreatedBy", userID);
                inputMap.put("SchemeType", getAppType());
                //                Commodity scheme changes 14-03-2018
                /*inputMap.put("BankNameCOMM", tvCommBankName.getText().toString());
                inputMap.put("BankBranchCOMM", tvCommBranchName.getText().toString());
                inputMap.put("BankAddCOMM", tvCommBankAddress.getText().toString());
                inputMap.put("BankAccNoCOMM", etCommAccNumber.getText().toString());
                inputMap.put("BankAccTypeCOMM", strcbComm);
                inputMap.put("BankMicrCodeCOMM", tvCommMICR.getText().toString());
                inputMap.put("BankIfscCodeCOMM", tvCommIFSC.getText().toString());
                inputMap.put("BankProofCOMM", spCommBankProof.getSelectedItem().toString());
                inputMap.put("PhotoProofCOMM", spCommPhotoProof.getSelectedItem().toString());*/
                inputMap.put("BankNameCOMM", tvEquityBankName.getText().toString());
                inputMap.put("BankBranchCOMM", tvEquityBranchName.getText().toString());
                inputMap.put("BankAddCOMM", tvEquityBankAddress.getText().toString());
                inputMap.put("BankAccNoCOMM", etEquityAccNumber.getText().toString());
                inputMap.put("BankAccTypeCOMM", strcbEqu);
                inputMap.put("BankMicrCodeCOMM", tvEquityMICR.getText().toString());
                inputMap.put("BankIfscCodeCOMM", tvEquityIFSC.getText().toString());
                inputMap.put("BankProofCOMM", spEquBankProof.getSelectedItem().toString());
                inputMap.put("PhotoProofCOMM", spEquPhotoProof.getSelectedItem().toString());
            } else if (isEquityOriginal() && llEquityBankScheme.getVisibility() == View.VISIBLE) {
                inputMap.put("BankName", tvEquityBankName.getText().toString());
                inputMap.put("BankBranch", tvEquityBranchName.getText().toString());
                inputMap.put("BankAdd", tvEquityBankAddress.getText().toString());
                inputMap.put("BankAccNo", etEquityAccNumber.getText().toString());
                inputMap.put("BankAccType", strcbEqu);
                inputMap.put("BankMicrCode", tvEquityMICR.getText().toString());
                inputMap.put("BankIfscCode", tvEquityIFSC.getText().toString());
                inputMap.put("BankProof", spEquBankProof.getSelectedItem().toString());
                inputMap.put("PhotoProof", spEquPhotoProof.getSelectedItem().toString());
                inputMap.put("CreatedBy", userID);
                inputMap.put("SchemeType", getAppType());
                inputMap.put("BankNameCOMM", "");
                inputMap.put("BankBranchCOMM", "");
                inputMap.put("BankAddCOMM", "");
                inputMap.put("BankAccNoCOMM", "");
                inputMap.put("BankAccTypeCOMM", "");
                inputMap.put("BankMicrCodeCOMM", "");
                inputMap.put("BankIfscCodeCOMM", "");
                inputMap.put("BankProofCOMM", "");
                inputMap.put("PhotoProofCOMM", "");
            } else if (isCommodityOriginal()
                && llCommodityBankScheme.getVisibility() == View.VISIBLE) {
                inputMap.put("BankName", "");
                inputMap.put("BankBranch", "");
                inputMap.put("BankAdd", "");
                inputMap.put("BankAccNo", "");
                inputMap.put("BankAccType", "");
                inputMap.put("BankMicrCode", "");
                inputMap.put("BankIfscCode", "");
                inputMap.put("BankProof", "");
                inputMap.put("PhotoProof", "");
                inputMap.put("CreatedBy", userID);
                inputMap.put("SchemeType", getAppType());
                //                Commodity scheme changes 14-03-2018
                /*inputMap.put("BankNameCOMM", tvCommBankName.getText().toString());
                inputMap.put("BankBranchCOMM", tvCommBranchName.getText().toString());
                inputMap.put("BankAddCOMM", tvCommBankAddress.getText().toString());
                inputMap.put("BankAccNoCOMM", etCommAccNumber.getText().toString());
                inputMap.put("BankAccTypeCOMM", strcbComm);
                inputMap.put("BankMicrCodeCOMM", tvCommMICR.getText().toString());
                inputMap.put("BankIfscCodeCOMM", tvCommIFSC.getText().toString());
                inputMap.put("BankProofCOMM", spCommBankProof.getSelectedItem().toString());
                inputMap.put("PhotoProofCOMM", spCommPhotoProof.getSelectedItem().toString());*/
                inputMap.put("BankNameCOMM", tvEquityBankName.getText().toString());
                inputMap.put("BankBranchCOMM", tvEquityBranchName.getText().toString());
                inputMap.put("BankAddCOMM", tvEquityBankAddress.getText().toString());
                inputMap.put("BankAccNoCOMM", etEquityAccNumber.getText().toString());
                inputMap.put("BankAccTypeCOMM", strcbEqu);
                inputMap.put("BankMicrCodeCOMM", tvEquityMICR.getText().toString());
                inputMap.put("BankIfscCodeCOMM", tvEquityIFSC.getText().toString());
                inputMap.put("BankProofCOMM", spEquBankProof.getSelectedItem().toString());
                inputMap.put("PhotoProofCOMM", spEquPhotoProof.getSelectedItem().toString());
            }


            String flag = UPDATEBANKDETAILS;
            String category = BANKDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {
                            try {
                            /*{"BANKUSERUPDTDATA":[{"SucessFlag":"FALSE",
                            "Message":"8114 - 16 - 5 - USP_MOBILEAPP_INSERT_UPDATE_BANK_DETAILS - 29 - " +
                                    "Error converting data type varchar to numeric."}]}*/
                                if (response.has("BANKUSERUPDTDATA")) {
                                    JSONArray jsonArray = response.getJSONArray("BANKUSERUPDTDATA");
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);
                                            String sucessFlag =
                                                (object.isNull("SucessFlag") || object.getString(
                                                    "SucessFlag").equals("")) ? ""
                                                    : object.getString("SucessFlag");
                                            String msg =
                                                (object.isNull("Message") || object.getString(
                                                    "Message").equals("")) ? ""
                                                    : object.getString("Message");
                                            if (sucessFlag.equalsIgnoreCase("false")) {
                                                informationDialog(msg);
                                            } else {

                                                if ((isEquityOriginal() && isCommodityOriginal())
                                                    || isAllOriginal()) {
                                                    BankDetailModel.setEquMargin(
                                                        etEquMarginDeposit.getText().toString());
                                                    BankDetailModel.setEquBankName(
                                                        spEquSelectBank.getSelectedItem()
                                                            .toString());
                                                    BankDetailModel.setEquAccNo(
                                                        etEquityAccNumber.getText().toString());
                                                    BankDetailModel.setEquTotalAmount(
                                                        etEquMarginDeposit.getText().toString());
                                                    //                                                Commodity scheme changes 14-03-2018
                                                /*BankDetailModel.setCommMargin(etCommMarginDeposit.getText().toString());
                                                BankDetailModel.setCommBankName(spCommSelectBank.getSelectedItem().toString());
                                                BankDetailModel.setCommAccNo(etCommAccNumber.getText().toString());
                                                BankDetailModel.setCommTotalAmount(etCommMarginDeposit.getText().toString());*/

                                                    BankDetailModel.setCommMargin(
                                                        etEquMarginDeposit.getText().toString());
                                                    BankDetailModel.setCommBankName(
                                                        spEquSelectBank.getSelectedItem()
                                                            .toString());
                                                    BankDetailModel.setCommAccNo(
                                                        etEquityAccNumber.getText().toString());
                                                    BankDetailModel.setCommTotalAmount(
                                                        etEquMarginDeposit.getText().toString());

                                                } else if (isEquityOriginal()
                                                    || isDerivativeOriginal()) {
                                                    BankDetailModel.setEquMargin(
                                                        etEquMarginDeposit.getText().toString());
                                                    BankDetailModel.setEquBankName(
                                                        spEquSelectBank.getSelectedItem()
                                                            .toString());
                                                    BankDetailModel.setEquAccNo(
                                                        etEquityAccNumber.getText().toString());
                                                    BankDetailModel.setEquTotalAmount(
                                                        etEquMarginDeposit.getText().toString());
                                                } else if (isCommodityOriginal()) {
                                                    //                                                Commodity scheme changes 14-03-2018
                                                /*BankDetailModel.setCommMargin(etCommMarginDeposit.getText().toString());
                                                BankDetailModel.setCommBankName(spCommSelectBank.getSelectedItem().toString());
                                                BankDetailModel.setCommAccNo(etCommAccNumber.getText().toString());
                                                BankDetailModel.setCommTotalAmount(etCommMarginDeposit.getText().toString());*/

                                                    BankDetailModel.setCommMargin(
                                                        etEquMarginDeposit.getText().toString());
                                                    BankDetailModel.setCommBankName(
                                                        spEquSelectBank.getSelectedItem()
                                                            .toString());
                                                    BankDetailModel.setCommAccNo(
                                                        etEquityAccNumber.getText().toString());
                                                    BankDetailModel.setCommTotalAmount(
                                                        etEquMarginDeposit.getText().toString());
                                                }


                                                //updateBankDetail = true;
                                            /*if (updateBankDetail && updateSchemeDetail) {
                                                getReviewDetail();
                                            }
                                            informationDialog(msg);*/
                                                //btnCommUpdateSchemeDisable();

                                                if (getAppType().equals(DIRECT)
                                                    || getAppType().equals(CRM)) {
                                                    insertUpdateSchDetailsForDirCRM(msg);

                                                } else {
                                                    insertUpdateSchemeDetailsForBA(msg);
                                                }
                                            }
                                        }
                                    }
                                }

                                customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            customProgress.setVisibility(View.GONE);
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
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });

        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }


    private void insertUpdateSchemeDetailsForBA(final String bankMsg) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();

            String strCommBrokScheme = "", strCommBrokSchemeNCX = "", strEquBrokScheme = "";
            if ((isEquityOriginal() && isCommodityOriginal()) || isAllOriginal()) {
                if (spCommBrokerageScheme.getSelectedItem().toString().contains("D")) {
                    strCommBrokScheme = "D";
                } else if (spCommBrokerageScheme.getSelectedItem().toString().contains("M")) {
                    strCommBrokScheme = "MR";
                }
                if (spNCXBrokerageScheme.getSelectedItem().toString().contains("D")) {
                    strCommBrokSchemeNCX = "D";
                } else if (spNCXBrokerageScheme.getSelectedItem().toString().contains("M")) {
                    strCommBrokSchemeNCX = "MR";
                }
                if (spEquBrokerage.getSelectedItem().toString().contains("D")) {
                    strEquBrokScheme = "D";
                } else if (spEquBrokerage.getSelectedItem().toString().contains("M")) {
                    strEquBrokScheme = "MR";
                }

                inputMap.put("_userId", userID);
                inputMap.put("_TradingProfileId", strTradeProID);
                inputMap.put("_TradingProfileName", strTradeProName);
                inputMap.put("_DeliveryProfileId", strDelProID);
                inputMap.put("_DeliveryProfileName", strDelProName);
                inputMap.put("_FutureProfileId", strFutProID);
                inputMap.put("_FutureProfileName", strFutProName);
                inputMap.put("_OptionProfileId", strOptProID);
                inputMap.put("_OptionProfileName", strOptProName);
                inputMap.put("_DPProfileId", strDPProID);
                inputMap.put("_DPProfileName", strDPProName);
                inputMap.put("_CurrencyFutureProfileId", strCurrFutProID);
                inputMap.put("_CurrencyProfileName", strCurrFutProName);
                inputMap.put("_CurrencyOptionsProfileId", strCurrOptProID);
                inputMap.put("_CurrencyOptionsProfileName", strCurrOptProName);
                inputMap.put("_BrokerageScheme", strEquBrokScheme);
                inputMap.put("_FutureProfileIdCOMM", strFutProfIdCOMM);
                inputMap.put("_FutureProfileNameCOMM", strFutProfNameCOMM);
                inputMap.put("_OptionProfileIdCOMM", strOptProfIdCOMM);
                inputMap.put("_OptionProfileNameCOMM", strOptProfNameCOMM);
                inputMap.put("_DelCOMMProfileIdCOMM", strDelCOMMProfIdCOMM);
                inputMap.put("_DelCOMMProfileNameCOMM", strDelCOMMProfNameCOMM);
                inputMap.put("_NCXFutureProfileIdCOMM", strNCXFutProfIdCOMM);
                inputMap.put("_NCXFutureProfileNameCOMM", strNCXFutProfNameCOMM);
                inputMap.put("_NCXOptionProfileIdCOMM", strNCXOptProfIdCOMM);
                inputMap.put("_NCXOptionProfileNameCOMM", strNCXOptProfNameCOMM);
                inputMap.put("_NCXDelCOMMProfileId", strNCXDelCOMMProfId);
                inputMap.put("_NCXDelCOMMProfileName", strNCXDelCOMMProfName);
                inputMap.put("_BrokerageSchemeCOMM", strCommBrokScheme);
                inputMap.put("_BrokerageSchemeCOMMNCX", strCommBrokSchemeNCX);
            } else if (isEquityOriginal() || isDerivativeOriginal()) {

                if (spEquBrokerage.getSelectedItem().toString().contains("D")) {
                    strEquBrokScheme = "D";
                } else if (spEquBrokerage.getSelectedItem().toString().contains("M")) {
                    strEquBrokScheme = "MR";
                }
                inputMap.put("_userId", userID);
                inputMap.put("_TradingProfileId", strTradeProID);
                inputMap.put("_TradingProfileName", strTradeProName);
                inputMap.put("_DeliveryProfileId", strDelProID);
                inputMap.put("_DeliveryProfileName", strDelProName);
                inputMap.put("_FutureProfileId", strFutProID);
                inputMap.put("_FutureProfileName", strFutProName);
                inputMap.put("_OptionProfileId", strOptProID);
                inputMap.put("_OptionProfileName", strOptProName);
                inputMap.put("_DPProfileId", strDPProID);
                inputMap.put("_DPProfileName", strDPProName);
                inputMap.put("_CurrencyFutureProfileId", strCurrFutProID);
                inputMap.put("_CurrencyProfileName", strCurrFutProName);
                inputMap.put("_CurrencyOptionsProfileId", strCurrOptProID);
                inputMap.put("_CurrencyOptionsProfileName", strCurrOptProName);
                inputMap.put("_BrokerageScheme", strEquBrokScheme);
                inputMap.put("_FutureProfileIdCOMM", "");
                inputMap.put("_FutureProfileNameCOMM", "");
                inputMap.put("_OptionProfileIdCOMM", "");
                inputMap.put("_OptionProfileNameCOMM", "");
                inputMap.put("_DelCOMMProfileIdCOMM", "");
                inputMap.put("_DelCOMMProfileNameCOMM", "");
                inputMap.put("_NCXFutureProfileIdCOMM", "");
                inputMap.put("_NCXFutureProfileNameCOMM", "");
                inputMap.put("_NCXOptionProfileIdCOMM", "");
                inputMap.put("_NCXOptionProfileNameCOMM", "");
                inputMap.put("_NCXDelCOMMProfileId", "");
                inputMap.put("_NCXDelCOMMProfileName", "");
                inputMap.put("_BrokerageSchemeCOMM", "");
                inputMap.put("_BrokerageSchemeCOMMNCX", "");
            } else if (isCommodityOriginal()) {
                if (spCommBrokerageScheme.getSelectedItem().toString().contains("D")) {
                    strCommBrokScheme = "D";
                } else if (spCommBrokerageScheme.getSelectedItem().toString().contains("M")) {
                    strCommBrokScheme = "MR";
                }
                if (spNCXBrokerageScheme.getSelectedItem().toString().contains("D")) {
                    strCommBrokSchemeNCX = "D";
                } else if (spNCXBrokerageScheme.getSelectedItem().toString().contains("M")) {
                    strCommBrokSchemeNCX = "MR";
                }
                inputMap.put("_userId", userID);
                inputMap.put("_TradingProfileId", "");
                inputMap.put("_TradingProfileName", "");
                inputMap.put("_DeliveryProfileId", "");
                inputMap.put("_DeliveryProfileName", "");
                inputMap.put("_FutureProfileId", "");
                inputMap.put("_FutureProfileName", "");
                inputMap.put("_OptionProfileId", "");
                inputMap.put("_OptionProfileName", "");
                inputMap.put("_DPProfileId", "");
                inputMap.put("_DPProfileName", "");
                inputMap.put("_CurrencyFutureProfileId", "");
                inputMap.put("_CurrencyProfileName", "");
                inputMap.put("_CurrencyOptionsProfileId", "");
                inputMap.put("_CurrencyOptionsProfileName", "");
                inputMap.put("_BrokerageScheme", "");
                inputMap.put("_FutureProfileIdCOMM", strFutProfIdCOMM);
                inputMap.put("_FutureProfileNameCOMM", strFutProfNameCOMM);
                inputMap.put("_OptionProfileIdCOMM", strOptProfIdCOMM);
                inputMap.put("_OptionProfileNameCOMM", strOptProfNameCOMM);
                inputMap.put("_DelCOMMProfileIdCOMM", strDelCOMMProfIdCOMM);
                inputMap.put("_DelCOMMProfileNameCOMM", strDelCOMMProfNameCOMM);
                inputMap.put("_NCXFutureProfileIdCOMM", strNCXFutProfIdCOMM);
                inputMap.put("_NCXFutureProfileNameCOMM", strNCXFutProfNameCOMM);
                inputMap.put("_NCXOptionProfileIdCOMM", strNCXOptProfIdCOMM);
                inputMap.put("_NCXOptionProfileNameCOMM", strNCXOptProfNameCOMM);
                inputMap.put("_NCXDelCOMMProfileId", strNCXDelCOMMProfId);
                inputMap.put("_NCXDelCOMMProfileName", strNCXDelCOMMProfName);
                inputMap.put("_BrokerageSchemeCOMM", strCommBrokScheme);
                inputMap.put("_BrokerageSchemeCOMMNCX", strCommBrokSchemeNCX);

            }

            String flag = INSERTUPDATESCHEMEDETAILSFORBA;
            String category = SCHEMEDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null && response.length() != 0) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("Table1");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String status =
                                            (object.isNull("Status") || object.getString("Status")
                                                .equals("")) ? "" : object.getString("Status");
                                        String msg =
                                            (object.isNull("Message") || object.getString("Message")
                                                .equals("")) ? "" : object.getString("Message");
                                        if (Integer.parseInt(status) != 1) {
                                            informationDialog(msg);
                                            //updateSchemeDetail = false;

                                        } else {
                                       /* strEquBankName = spEquSelectBank.getSelectedItem().toString();

                                        strCommBankName = spCommSelectBank.getSelectedItem() == null ? "" : spCommSelectBank.getSelectedItem().toString();

                                        strEquAccNo = etEquityAccNumber.getText().toString();
                                        strCommAccNo = etCommAccNumber.getText().toString();*/
                                            //updateSchemeDetail = true;
                                            // if (updateBankDetail && updateSchemeDetail) {
                                            getReviewDetail();
                                            //}
                                            //                                        informationDialog("Bank: " + bankMsg + "\nScheme: " + msg);
                                        }
                                    }
                                }

                                customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            customProgress.setVisibility(View.GONE);
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
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });

        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }


    private void getSchemeDetails(final String userType, String userID) {
        isGetSchemeCalled = true;

        if (MOSLCommon.isNetworkEnabled(mContext)) {

            Map inputMap = new HashMap<>();
            inputMap.put("_userId", userID);
            inputMap.put("_userType", userType);
            inputMap.put("IS_DIRECT_BA", isDirectProspect() ? "1" : "0");

            String flag = GETSCHEMEDETAILS;
            String category = SCHEMEDETAILS;

            customProgress.setVisibility(View.VISIBLE);
            if (userType.equals(CRM) || userType.equals(DIRECT)) {
                getCRMOrDirectScheme(flag, category, inputMap);
            } else {
                getFranchiseScheme(flag, category, inputMap);
            }
        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    private void getBindMarginSchemeDetails(final String SelectedID) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
            inputMap.put("_userId", userID);
            String flag = BINDMARGINSCHEMEDETAILS;
            String category = SCHEMEDETAILS;
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        arrEquScheme = new ArrayList<>();
                        if (response != null && response.length() != 0) {
                            try {
                                JSONArray jsonArrScheme = response.getJSONArray("SchemeMarginEQ");
                                if (jsonArrScheme.length() != 0) {
                                    arrEquScheme.add(
                                        new RowData(0, "-1", "Select", "", "Select", "", ""));
                                    // arrEquSchMarginDeposit.add(new RowData(0, "-1", "Select"));
                                    for (int i = 0; i < jsonArrScheme.length(); i++) {
                                        JSONObject object = jsonArrScheme.getJSONObject(i);
                                        String id = (object.isNull("ID") || object.getString("ID")
                                            .equals("")) ? "" : object.getString("ID");
                                        BigDecimal bdID = new BigDecimal(id);
                                        String product_id =
                                            (object.isNull("Product_id") || object.getString(
                                                "Product_id").equals("")) ? ""
                                                : object.getString("Product_id");
                                        String productID =
                                            (object.isNull("ProductID") || object.getString(
                                                "ProductID").equals("")) ? ""
                                                : object.getString("ProductID");
                                        String schemeName =
                                            (object.isNull("Scheme_name") || object.getString(
                                                "Scheme_name").equals("")) ? ""
                                                : object.getString("Scheme_name");
                                        String marginDeposit =
                                            (object.isNull("Margin_Deposit") || object.getString(
                                                "Margin_Deposit").equals("")) ? ""
                                                : object.getString("Margin_Deposit");
                                        BigDecimal bdMarginDeposit = new BigDecimal(marginDeposit);

                                        String accountOpeningFee =
                                            (object.isNull("Account_Opening_Fee")
                                                || object.getString("Account_Opening_Fee")
                                                .equals("")) ? ""
                                                : object.getString("Account_Opening_Fee");
                                        String segment =
                                            (object.isNull("Segment") || object.getString("Segment")
                                                .equals("")) ? "" : object.getString("Segment");
                                        String userId =
                                            (object.isNull("UserId") || object.getString("UserId")
                                                .equals("")) ? "" : object.getString("UserId");
                                        String schemeSelectedID =
                                            (object.isNull("SchemeSelectedID") || object.getString(
                                                "SchemeSelectedID").equals("")) ? ""
                                                : object.getString("SchemeSelectedID");
                                        // tvEquAccOpenNote.setText(accountOpeningFee);
                                        if (id.equalsIgnoreCase(MF_CUM_EQUITY)) {
                                            if (schemeSelectedID.equalsIgnoreCase(MF_CUM_EQUITY)) {
                                                arrEquScheme.add(new RowData((i + 1),
                                                    String.valueOf(bdID.longValue()), schemeName,
                                                    String.valueOf(bdID.longValue()), schemeName,
                                                    schemeSelectedID,
                                                    String.valueOf(bdMarginDeposit.longValue())));
                                                mfEquitySchemeSelected();
                                            }
                                        } else {
                                            arrEquScheme.add(new RowData((i + 1),
                                                String.valueOf(bdID.longValue()), schemeName,
                                                String.valueOf(bdID.longValue()), schemeName,
                                                schemeSelectedID,
                                                String.valueOf(bdMarginDeposit.longValue())));
                                        }
                                        //                                            arrEquScheme.add(new RowData((i + 1), String.valueOf(bdID.longValue()), schemeName, String.valueOf(bdID.longValue()), schemeName, schemeSelectedID, String.valueOf(bdMarginDeposit.longValue())));

                                            /*  if (productID.equalsIgnoreCase("1")) {
                                                   cbMarginAcc.setChecked(true);
                                                   cbValuePacAcc.setChecked(false);
                                               } else if (productID.equalsIgnoreCase("2")) {
                                                   cbMarginAcc.setChecked(false);
                                                   cbValuePacAcc.setChecked(true);
                                               }*/
                                    }
                                    arrAdapEquScheme = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrEquScheme);
                                    arrAdapEquScheme.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spEquScheme.setAdapter(arrAdapEquScheme);
                                    spEquScheme.setOnItemSelectedListener(equSchemeListener);
                                    spinnerSchemeSelect(SelectedID, arrAdapEquScheme, spEquScheme);
                                    customProgress.setVisibility(View.GONE);
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
                            }
                        } else {
                            customProgress.setVisibility(View.GONE);
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
                        Throwable
                            throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject
                            errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray
                            errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                }

            );

        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    private void getBindValuePacSchemeDetails(final String SelectedID) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
            inputMap.put("_userId", userID);
            String flag = BINDVALUEPACSCHEMEDETAILS;
            String category = SCHEMEDETAILS;
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        arrEquScheme = new ArrayList<>();
                        if (response != null && response.length() != 0) {
                            try {
                                JSONArray jsonArrScheme = response.getJSONArray("SchemeValuePACEQ");
                                if (jsonArrScheme.length() != 0) {
                                    arrEquScheme.add(
                                        new RowData(0, "-1", "Select", "", "Select", "", ""));
                                    // arrEquSchMarginDeposit.add(new RowData(0, "-1", "Select"));
                                    for (int i = 0; i < jsonArrScheme.length(); i++) {
                                        JSONObject object = jsonArrScheme.getJSONObject(i);
                                        String id = (object.isNull("ID") || object.getString("ID")
                                            .equals("")) ? "" : object.getString("ID");
                                        BigDecimal bdID = new BigDecimal(id);
                                        String product_id =
                                            (object.isNull("Product_id") || object.getString(
                                                "Product_id").equals("")) ? ""
                                                : object.getString("Product_id");
                                        String productID =
                                            (object.isNull("ProductID") || object.getString(
                                                "ProductID").equals("")) ? ""
                                                : object.getString("ProductID");
                                        String schemeName =
                                            (object.isNull("Scheme_name") || object.getString(
                                                "Scheme_name").equals("")) ? ""
                                                : object.getString("Scheme_name");
                                        String marginDeposit =
                                            (object.isNull("Margin_Deposit") || object.getString(
                                                "Margin_Deposit").equals("")) ? ""
                                                : object.getString("Margin_Deposit");
                                        BigDecimal bdMarginDeposit = new BigDecimal(marginDeposit);

                                        String accountOpeningFee =
                                            (object.isNull("Account_Opening_Fee")
                                                || object.getString("Account_Opening_Fee")
                                                .equals("")) ? ""
                                                : object.getString("Account_Opening_Fee");
                                        String segment =
                                            (object.isNull("Segment") || object.getString("Segment")
                                                .equals("")) ? "" : object.getString("Segment");
                                        String userId =
                                            (object.isNull("UserId") || object.getString("UserId")
                                                .equals("")) ? "" : object.getString("UserId");
                                        String schemeSelectedID =
                                            (object.isNull("SchemeSelectedID") || object.getString(
                                                "SchemeSelectedID").equals("")) ? ""
                                                : object.getString("SchemeSelectedID");
                                        // tvEquAccOpenNote.setText(accountOpeningFee);
                                        if (id.equalsIgnoreCase(MF_CUM_EQUITY)) {
                                            if (schemeSelectedID.equalsIgnoreCase(MF_CUM_EQUITY)) {
                                                arrEquScheme.add(new RowData((i + 1),
                                                    String.valueOf(bdID.longValue()), schemeName,
                                                    String.valueOf(bdID.longValue()), schemeName,
                                                    schemeSelectedID,
                                                    String.valueOf(bdMarginDeposit.longValue())));
                                                mfEquitySchemeSelected();
                                            }
                                        } else {
                                            arrEquScheme.add(new RowData((i + 1),
                                                String.valueOf(bdID.longValue()), schemeName,
                                                String.valueOf(bdID.longValue()), schemeName,
                                                schemeSelectedID,
                                                String.valueOf(bdMarginDeposit.longValue())));
                                        }
                                        //                                            arrEquScheme.add(new RowData((i + 1), String.valueOf(bdID.longValue()), schemeName, String.valueOf(bdID.longValue()), schemeName, schemeSelectedID, String.valueOf(bdMarginDeposit.longValue())));
                                             /*  if (productID.equalsIgnoreCase("1")) {
                                                   cbMarginAcc.setChecked(true);
                                                   cbValuePacAcc.setChecked(false);
                                               } else if (productID.equalsIgnoreCase("2")) {
                                                   cbMarginAcc.setChecked(false);
                                                   cbValuePacAcc.setChecked(true);
                                               }*/
                                    }
                                    arrAdapEquScheme = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrEquScheme);
                                    arrAdapEquScheme.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spEquScheme.setAdapter(arrAdapEquScheme);
                                    spEquScheme.setOnItemSelectedListener(equSchemeListener);
                                    spinnerSchemeSelect(SelectedID, arrAdapEquScheme, spEquScheme);

                                    customProgress.setVisibility(View.GONE);
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
                            }
                        } else {
                            customProgress.setVisibility(View.GONE);
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
                        Throwable
                            throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject
                            errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray
                            errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                }

            );

        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    public void getCRMOrDirectScheme(String flag, String category, Map inputMap) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        arrEquScheme = new ArrayList<>();
                        arrCommScheme = new ArrayList<>();
                        arrEquSchMarginDeposit = new ArrayList<>();
                        arrCommSchMarginDeposit = new ArrayList<>();
                        boolean isEquSchemeSelectedID = false;
                        boolean isCommSchemeSelectedID = false;

                        if (response != null && response.length() != 0) {

                            try {
                                if (response.has("SchemeEQ")) {
                                    JSONArray jsonArrScheme = response.getJSONArray("SchemeEQ");
                                    String schemeSelectedID = null, productID = null,
                                        accountOpeningFee = null;
                                    if (jsonArrScheme.length() != 0) {
                                        arrEquScheme.add(
                                            new RowData(0, "-1", "Select", "", "Select", "", ""));
                                        // arrEquSchMarginDeposit.add(new RowData(0, "-1", "Select"));
                                        for (int i = 0; i < jsonArrScheme.length(); i++) {
                                            JSONObject object = jsonArrScheme.getJSONObject(i);
                                            String id =
                                                (object.isNull("ID") || object.getString("ID")
                                                    .equals("")) ? "" : object.getString("ID");
                                            BigDecimal bdID = new BigDecimal(id);

                                            String product_id =
                                                (object.isNull("Product_id") || object.getString(
                                                    "Product_id").equals("")) ? ""
                                                    : object.getString("Product_id");
                                            productID =
                                                (object.isNull("ProductID") || object.getString(
                                                    "ProductID").equals("")) ? ""
                                                    : object.getString("ProductID");
                                            String schemeName =
                                                (object.isNull("Scheme_name") || object.getString(
                                                    "Scheme_name").equals("")) ? ""
                                                    : object.getString("Scheme_name");
                                            String marginDeposit = (object.isNull("Margin_Deposit")
                                                || object.getString("Margin_Deposit").equals(""))
                                                ? "" : object.getString("Margin_Deposit");
                                            BigDecimal bdMarginDeposit =
                                                new BigDecimal(marginDeposit);
                                            accountOpeningFee =
                                                (object.isNull("Account_Opening_Fee")
                                                    || object.getString("Account_Opening_Fee")
                                                    .equals("")) ? ""
                                                    : object.getString("Account_Opening_Fee");
                                            String segment =
                                                (object.isNull("Segment") || object.getString(
                                                    "Segment").equals("")) ? ""
                                                    : object.getString("Segment");
                                            String userId =
                                                (object.isNull("UserId") || object.getString(
                                                    "UserId").equals("")) ? ""
                                                    : object.getString("UserId");
                                            schemeSelectedID = (object.isNull("SchemeSelectedID")
                                                || object.getString("SchemeSelectedID").equals(""))
                                                ? "" : object.getString("SchemeSelectedID");

                                            if (id.equalsIgnoreCase(MF_CUM_EQUITY)) {
                                                if (schemeSelectedID.equalsIgnoreCase(
                                                    MF_CUM_EQUITY)) {
                                                    arrEquScheme.add(new RowData((i + 1),
                                                        String.valueOf(bdID.longValue()),
                                                        schemeName,
                                                        String.valueOf(bdID.longValue()),
                                                        schemeName, schemeSelectedID,
                                                        String.valueOf(
                                                            bdMarginDeposit.longValue())));
                                                    mfEquitySchemeSelected();
                                                }
                                            } else {
                                                arrEquScheme.add(new RowData((i + 1),
                                                    String.valueOf(bdID.longValue()), schemeName,
                                                    String.valueOf(bdID.longValue()), schemeName,
                                                    schemeSelectedID,
                                                    String.valueOf(bdMarginDeposit.longValue())));
                                            }
                                            if (schemeSelectedID.equals(
                                                String.valueOf(bdID.longValue()))) {
                                                isEquSchemeSelectedID = true;
                                                if (schemeSelectedID.equals("9999")) {
                                                    if (response.has("OtherSchemeEQ")) {
                                                        JSONArray jsonArrOherSch =
                                                            response.getJSONArray("OtherSchemeEQ");

                                                        if (jsonArrOherSch.length() != 0) {
                                                            for (int j = 0;
                                                                j < jsonArrOherSch.length(); j++) {
                                                                JSONObject objOtherSchEqu =
                                                                    jsonArrOherSch.getJSONObject(j);
                                                                String otherMarginDeposit =
                                                                    (objOtherSchEqu.isNull(
                                                                        "Margin_Deposit")
                                                                        || objOtherSchEqu.getString(
                                                                        "Margin_Deposit")
                                                                        .equals("")) ? ""
                                                                        : objOtherSchEqu.getString(
                                                                            "Margin_Deposit");
                                                                BigDecimal bdOtherMarginDeposit =
                                                                    new BigDecimal(
                                                                        otherMarginDeposit);

                                                                String otherSchemeName =
                                                                    (objOtherSchEqu.isNull(
                                                                        "Scheme_Name")
                                                                        || objOtherSchEqu.getString(
                                                                        "Scheme_Name").equals(""))
                                                                        ? ""
                                                                        : objOtherSchEqu.getString(
                                                                            "Scheme_Name");
                                                                String otherAccOpeningFee =
                                                                    (objOtherSchEqu.isNull(
                                                                        "Account_Opening_Fee")
                                                                        || objOtherSchEqu.getString(
                                                                        "Account_Opening_Fee")
                                                                        .equals("")) ? ""
                                                                        : objOtherSchEqu.getString(
                                                                            "Account_Opening_Fee");
                                                                CommonModel.setEquSchSelID(
                                                                    schemeSelectedID);
                                                                CommonModel.setEquSchemeName(
                                                                    otherSchemeName);
                                                                CommonModel.setEquMarginDeposit(
                                                                    String.valueOf(
                                                                        bdOtherMarginDeposit.longValue()));
                                                                isEquOtherSchSelected = true;
                                                                isOtherAmountSubmitted = true;
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    isOtherAmountSubmitted = false;
                                                    isEquOtherSchSelected = false;
                                                    CommonModel.setEquSchSelID(schemeSelectedID);
                                                    CommonModel.setEquSchemeName(schemeName);
                                                    CommonModel.setEquMarginDeposit(String.valueOf(
                                                        bdMarginDeposit.longValue()));
                                                }
                                            } else if (!isEquSchemeSelectedID) {
                                                CommonModel.setEquSchSelID(
                                                    String.valueOf(bdID.longValue()));
                                                CommonModel.setEquSchemeName(schemeName);
                                                CommonModel.setEquMarginDeposit(
                                                    String.valueOf(bdMarginDeposit.longValue()));
                                            }
                                        }
                                    }
                                    getDpSchemeDetails(accountOpeningFee, schemeSelectedID);
                                    arrAdapEquScheme = new ArrayAdapter<>(mContext,
                                        R.layout.simple_spinner_item, arrEquScheme);
                                    arrAdapEquScheme.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spEquScheme.setAdapter(arrAdapEquScheme);
                                    spEquScheme.setOnItemSelectedListener(equSchemeListener);
                                    if (getAppType().equals("DIRECT") && !getIsAadharVerified()) {
                                        if (productID.equalsIgnoreCase("1")) {
                                            rbMarginAcc.setChecked(true);
                                            rbValuePacAcc.setChecked(false);
                                            getBindMarginSchemeDetails(schemeSelectedID);
                                            llMarginDeposit.setVisibility(View.VISIBLE);
                                            llSelectedScheme.setVisibility(View.GONE);
                                        } else if (productID.equalsIgnoreCase("2")) {
                                            rbMarginAcc.setChecked(false);
                                            rbValuePacAcc.setChecked(true);
                                            getBindValuePacSchemeDetails(schemeSelectedID);
                                            btnMarginAllScheme.setVisibility(View.GONE);
                                            btnMarginSelectedScheme.setVisibility(View.GONE);
                                            llMarginDeposit.setVisibility(View.GONE);
                                            llSelectedScheme.setVisibility(View.VISIBLE);

                                        }
                                    } else {
                                        spinnerSchemeSelect(schemeSelectedID, arrAdapEquScheme,
                                            spEquScheme);

                                        //  equSchemeData(getEquSchSelID(), getEquSchemeName(), getEquMarginDeposit());
                                    }


                                    // spinnerSchemeSelect(schemeSelectedID, arrAdapEquScheme, spEquScheme);
                                   /* RowData corresCityData = getSpinnerRowDataById(schemeSelectedID, arrAdapEquScheme);
                                    if (corresCityData != null) {
                                        spEquScheme.setSelection(corresCityData.getPosition());
                                    }*/
                                }
                                if (response.has("AdditionalSchemeInfoEQ")) {

                                    JSONArray jsonArrSchInfoEqu =
                                        response.getJSONArray("AdditionalSchemeInfoEQ");

                                    if (jsonArrSchInfoEqu.length() != 0) {

                                        for (int i = 0; i < jsonArrSchInfoEqu.length(); i++) {
                                            JSONObject object = jsonArrSchInfoEqu.getJSONObject(i);
                                            String accountOpeningFee =
                                                (object.isNull("AccountOpeningFee")
                                                    || object.getString("AccountOpeningFee")
                                                    .equals("")) ? ""
                                                    : object.getString("AccountOpeningFee");
                                            String softwareCharges =
                                                (object.isNull("SoftwareCharges")
                                                    || object.getString("SoftwareCharges")
                                                    .equals("")) ? ""
                                                    : object.getString("SoftwareCharges");
                                            String marginProductId =
                                                (object.isNull("MarginProductId")
                                                    || object.getString("MarginProductId")
                                                    .equals("")) ? ""
                                                    : object.getString("MarginProductId");
                                            String schemeName =
                                                (object.isNull("SchemeName") || object.getString(
                                                    "SchemeName").equals("")) ? ""
                                                    : object.getString("SchemeName");
                                            String marginDepositINR =
                                                (object.isNull("MarginDepositINR")
                                                    || object.getString("MarginDepositINR")
                                                    .equals("")) ? ""
                                                    : object.getString("MarginDepositINR");
                                            String intradayCashSegment1stLegMAX =
                                                (object.isNull("IntradayCashSegment1stLegMAX")
                                                    || object.getString(
                                                    "IntradayCashSegment1stLegMAX").equals("")) ? ""
                                                    : object.getString(
                                                        "IntradayCashSegment1stLegMAX");
                                            String intradayCashSegment2ndLegMAX =
                                                (object.isNull("IntradayCashSegment2ndLegMAX")
                                                    || object.getString(
                                                    "IntradayCashSegment2ndLegMAX").equals("")) ? ""
                                                    : object.getString(
                                                        "IntradayCashSegment2ndLegMAX");
                                            String deliveryBrokerageStandard =
                                                (object.isNull("DeliveryBrokerageStandard")
                                                    || object.getString("DeliveryBrokerageStandard")
                                                    .equals("")) ? ""
                                                    : object.getString("DeliveryBrokerageStandard");
                                            String intradayFutures1stLegMAX =
                                                (object.isNull("IntradayFutures1stLegMAX") || object
                                                    .getString("IntradayFutures1stLegMAX")
                                                    .equals("")) ? ""
                                                    : object.getString("IntradayFutures1stLegMAX");
                                            String intradayFutures2ndLegMAX =
                                                (object.isNull("IntradayFutures2ndLegMAX") || object
                                                    .getString("IntradayFutures2ndLegMAX")
                                                    .equals("")) ? ""
                                                    : object.getString("IntradayFutures2ndLegMAX");
                                            String options1stLegAnd2ndLegINR =
                                                (object.isNull("Options1stLegAnd2ndLegINR")
                                                    || object.getString("Options1stLegAnd2ndLegINR")
                                                    .equals("")) ? ""
                                                    : object.getString("Options1stLegAnd2ndLegINR");
                                            String currencyFut1st2ndLegINR =
                                                (object.isNull("CURRENCYFUT1ST2NDLEGINR")
                                                    || object.getString("CURRENCYFUT1ST2NDLEGINR")
                                                    .equals("")) ? ""
                                                    : object.getString("CURRENCYFUT1ST2NDLEGINR");
                                            String currencyOptions =
                                                (object.isNull("CURRENCYOPTIONS")
                                                    || object.getString("CURRENCYOPTIONS")
                                                    .equals("")) ? ""
                                                    : object.getString("CURRENCYOPTIONS");
                                            String adminCharges =
                                                (object.isNull("Admin Charges")
                                                    || object.getString("Admin Charges")
                                                    .equals("")) ? ""
                                                    : object.getString("Admin Charges");
                                            String brokingAMC =
                                                (object.isNull("Broking AMC(Annually)")
                                                    || object.getString("Broking AMC(Annually)")
                                                    .equals("")) ? ""
                                                    : object.getString("Broking AMC(Annually)");

                                            tvequSchInfoAccOpenCharge.setText(accountOpeningFee);
                                            tvequSchInfoSoftCharge.setText(softwareCharges);
                                            tvequSchInfoIntraday1stLeg.setText(
                                                intradayCashSegment1stLegMAX);
                                            tvequSchInfoIntraday2ndLeg.setText(
                                                intradayCashSegment2ndLegMAX);
                                            tvequSchInfoDelivery.setText(deliveryBrokerageStandard);
                                            tvequSchInfoFutures1stLeg.setText(
                                                intradayFutures1stLegMAX);
                                            tvequSchInfoFutures2ndLeg.setText(
                                                intradayFutures2ndLegMAX);
                                            tvequSchInfoOptions.setText(options1stLegAnd2ndLegINR);
                                            tvequSchInfoCurrFuture.setText(currencyFut1st2ndLegINR);
                                            tvequSchInfoCurrOption.setText(currencyOptions);
                                            tvEqAdminCharges.setText(adminCharges);
                                            tvEqBrokingAmc.setText(brokingAMC);
                                        }
                                    }
                                }
                                if (response.has("SchemeCOMM")) {
                                    JSONArray jsonArrSchComm = response.getJSONArray("SchemeCOMM");

                                    if (jsonArrSchComm.length() != 0) {
                                        arrCommScheme.add(
                                            new RowData(0, "-1", "Select", "0", "Select", "", ""));
                                        arrCommSchMarginDeposit.add(new RowData(0, "-1", ""));
                                        for (int i = 0; i < jsonArrSchComm.length(); i++) {
                                            JSONObject object = jsonArrSchComm.getJSONObject(i);
                                            String id = object.getString("ID");
                                            BigDecimal bdID = new BigDecimal(id);

                                            String productID = object.getString("Product_id");
                                            String schemeName = object.getString("Scheme_name");
                                            String marginDeposit =
                                                object.getString("Margin_Deposit");
                                            BigDecimal bdMarginDeposit =
                                                new BigDecimal(marginDeposit);

                                            String accountOpeningFee =
                                                object.getString("Account_Opening_Fee");
                                            String segment = object.getString("Segment");
                                            String userId = object.getString("UserId");
                                            String schemeSelectedID =
                                                object.getString("SchemeSelectedID");

                                            tvCommAccOpenNote.setText(
                                                "DP-AMC " + accountOpeningFee);

                                            arrCommScheme.add(new RowData((i + 1),
                                                String.valueOf(bdID.longValue()), schemeName,
                                                String.valueOf(bdID.longValue()), schemeName,
                                                schemeSelectedID,
                                                String.valueOf(bdMarginDeposit.longValue())));

                                            if (schemeSelectedID.equals("0")) {
                                                CommonModel.setComSchSelID("0");
                                                CommonModel.setComSchemeName(SELECT);
                                                CommonModel.setComMarginDeposit("");

                                            } else if (schemeSelectedID.equals(
                                                String.valueOf(bdID.longValue()))) {
                                                isCommSchemeSelectedID = true;
                                                if (schemeSelectedID.equals("9999")) {
                                                    if (response.has("OtherSchemeComm")) {
                                                        JSONArray jsonArrOherSchComm =
                                                            response.getJSONArray(
                                                                "OtherSchemeComm");

                                                        if (jsonArrOherSchComm.length() != 0) {
                                                            for (int j = 0;
                                                                j < jsonArrOherSchComm.length();
                                                                j++) {
                                                                JSONObject objOtherSchComm =
                                                                    jsonArrOherSchComm.getJSONObject(
                                                                        j);
                                                                String otherMarginDeposit =
                                                                    (objOtherSchComm.isNull(
                                                                        "Margin_Deposit")
                                                                        || objOtherSchComm.getString(
                                                                        "Margin_Deposit")
                                                                        .equals("")) ? ""
                                                                        : objOtherSchComm.getString(
                                                                            "Margin_Deposit");
                                                                BigDecimal bdOtherMarginDeposit =
                                                                    new BigDecimal(
                                                                        otherMarginDeposit);

                                                                String otherSchemeName =
                                                                    (objOtherSchComm.isNull(
                                                                        "Scheme_Name")
                                                                        || objOtherSchComm.getString(
                                                                        "Scheme_Name").equals(""))
                                                                        ? ""
                                                                        : objOtherSchComm.getString(
                                                                            "Scheme_Name");
                                                                String otherAccOpeningFee =
                                                                    (objOtherSchComm.isNull(
                                                                        "Account_Opening_Fee")
                                                                        || objOtherSchComm.getString(
                                                                        "Account_Opening_Fee")
                                                                        .equals("")) ? ""
                                                                        : objOtherSchComm.getString(
                                                                            "Account_Opening_Fee");
                                                                CommonModel.setComSchSelID(
                                                                    schemeSelectedID);
                                                                CommonModel.setComSchemeName(
                                                                    otherSchemeName);
                                                                CommonModel.setComMarginDeposit(
                                                                    String.valueOf(
                                                                        bdOtherMarginDeposit.longValue()));

                                                                isCommOtherSchSelected = true;
                                                            }
                                                        }
                                                    }
                                                } else {

                                                    isCommOtherSchSelected = false;
                                                    CommonModel.setComSchSelID(schemeSelectedID);
                                                    CommonModel.setComSchemeName(schemeName);
                                                    CommonModel.setComMarginDeposit(String.valueOf(
                                                        bdMarginDeposit.longValue()));
                                                }
                                            } else if (!isCommSchemeSelectedID) {
                                                CommonModel.setComSchSelID(
                                                    String.valueOf(bdID.longValue()));
                                                CommonModel.setComSchemeName(schemeName);
                                                CommonModel.setComMarginDeposit(
                                                    String.valueOf(bdMarginDeposit.longValue()));

                                            }
                                        }
                                    }
                                    arrAdapCommScheme = new ArrayAdapter<RowData>(mContext,
                                        R.layout.simple_spinner_item, arrCommScheme);
                                    arrAdapCommScheme.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spCommScheme.setAdapter(arrAdapCommScheme);
                                    spCommScheme.setOnItemSelectedListener(commSchemeListener);
                                    comSchemeData(getComSchSelID(), getComSchemeName(),
                                        getComMarginDeposit());

                                }
                                if (response.has("AdditionalSchemeInfoCOMM")) {
                                    JSONArray jsonArrSchInfoComm =
                                        response.getJSONArray("AdditionalSchemeInfoCOMM");

                                    if (jsonArrSchInfoComm.length() != 0) {

                                        for (int i = 0; i < jsonArrSchInfoComm.length(); i++) {
                                            JSONObject object = jsonArrSchInfoComm.getJSONObject(i);
                                            String deliveryBrokerageStandard =
                                                (object.isNull("DeliveryBrokerageStandard")
                                                    || object.getString("DeliveryBrokerageStandard")
                                                    .equals("")) ? ""
                                                    : object.getString("DeliveryBrokerageStandard");
                                            String IntradayCashSegment1stLegMAX =
                                                (object.isNull("IntradayCashSegment1stLegMAX")
                                                    || object.getString(
                                                    "IntradayCashSegment1stLegMAX").equals("")) ? ""
                                                    : object.getString(
                                                        "IntradayCashSegment1stLegMAX");

                                            tvCommDelBrok.setText(deliveryBrokerageStandard);
                                            tvCommIntradayBrok.setText(
                                                IntradayCashSegment1stLegMAX);
                                        }
                                    }
                                }

                                if (response.has("CurrencyFuture")) {
                                    try {
                                        JSONArray jsonArray =
                                            response.getJSONArray("CurrencyFuture");
                                        SchemeDetailsModel detailsModel;
                                        if (jsonArray.length() != 0) {
                                            modelList = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                detailsModel = new SchemeDetailsModel();
                                                detailsModel.setBrok_TEXT(
                                                    (object.isNull("BROK_TEXT") || object.getString(
                                                        "BROK_TEXT").equals("")) ? ""
                                                        : object.getString("BROK_TEXT").trim());
                                                detailsModel.setBrok_VAL(
                                                    (object.isNull("BROK_VAL") || object.getString(
                                                        "BROK_VAL").equals("")) ? ""
                                                        : object.getString("BROK_VAL").trim());
                                                detailsModel.setCurr_TYPE(
                                                    (object.isNull("CURR_TYPE") || object.getString(
                                                        "CURR_TYPE").equals("")) ? ""
                                                        : object.getString("CURR_TYPE").trim());
                                                detailsModel.setCurrFutureSelectedVal(
                                                    (object.isNull("CURRFutureSelectedVal")
                                                        || object.getString("CURRFutureSelectedVal")
                                                        .equals("")) ? ""
                                                        : object.getString("CURRFutureSelectedVal")
                                                            .trim());
                                                detailsModel.setCurrFutureShow(
                                                    (object.isNull("CURRFutureShow")
                                                        || object.getString("CURRFutureShow")
                                                        .equals("")) ? ""
                                                        : object.getString("CURRFutureShow")
                                                            .trim());
                                                modelList.add(detailsModel);
                                            }
                                            currencyFutureShowRadioButton(modelList);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    currencyfutureRadiogroup.setVisibility(View.GONE);
                                }
                                if (response.has("CurrencyOption")) {
                                    try {
                                        JSONArray jsonArray =
                                            response.getJSONArray("CurrencyOption");
                                        SchemeDetailsModel currencyOptionModel;

                                        if (jsonArray.length() != 0) {
                                            currencyOptionmodelList = new ArrayList<>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                currencyOptionModel = new SchemeDetailsModel();
                                                currencyOptionModel.setBrok_TEXT(
                                                    (object.isNull("BROK_TEXT") || object.getString(
                                                        "BROK_TEXT").equals("")) ? ""
                                                        : object.getString("BROK_TEXT").trim());
                                                currencyOptionModel.setBrok_VAL(
                                                    (object.isNull("BROK_VAL") || object.getString(
                                                        "BROK_VAL").equals("")) ? ""
                                                        : object.getString("BROK_VAL").trim());
                                                currencyOptionModel.setCurr_TYPE(
                                                    (object.isNull("CURR_TYPE") || object.getString(
                                                        "CURR_TYPE").equals("")) ? ""
                                                        : object.getString("CURR_TYPE").trim());
                                                currencyOptionModel.setCurrOptionSelectedVal(
                                                    (object.isNull("CURROptionSelectedVal")
                                                        || object.getString("CURROptionSelectedVal")
                                                        .equals("")) ? ""
                                                        : object.getString("CURROptionSelectedVal")
                                                            .trim());
                                                currencyOptionModel.setCurrOptionShow(
                                                    (object.isNull("CURROptionShow")
                                                        || object.getString("CURROptionShow")
                                                        .equals("")) ? ""
                                                        : object.getString("CURROptionShow")
                                                            .trim());
                                                currencyOptionmodelList.add(currencyOptionModel);
                                            }
                                            currOptionShowRadioButton(currencyOptionmodelList);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    currencyoptionRadiogroup.setVisibility(View.GONE);
                                }
                                customProgress.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable
                            throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject
                            errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray
                            errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                }

            );
        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }


    public void getFranchiseScheme(String flag, String category, Map inputMap) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        if (response != null && response.length() != 0) {
                            customProgress.setVisibility(View.VISIBLE);
                            try {
                                if (response.has("BrokerageScheme")) {
                                    JSONArray jsonArrBrokScheme =
                                        response.getJSONArray("BrokerageScheme");


                                    if (jsonArrBrokScheme.length() != 0) {
                                        for (int i = 0; i < jsonArrBrokScheme.length(); i++) {
                                            JSONObject object = jsonArrBrokScheme.getJSONObject(i);
                                            String equBrokSch = (object.isNull("EQBrokerageScheme")
                                                || object.getString("EQBrokerageScheme").equals(""))
                                                ? "" : object.getString("EQBrokerageScheme");
                                            String commMCXBrokSch =
                                                (object.isNull("COMMMCXBrokerageScheme")
                                                    || object.getString("COMMMCXBrokerageScheme")
                                                    .equals("")) ? ""
                                                    : object.getString("COMMMCXBrokerageScheme");
                                            String commNCXBrokSch =
                                                (object.isNull("COMMNCXBrokerageScheme")
                                                    || object.getString("COMMNCXBrokerageScheme")
                                                    .equals("")) ? ""
                                                    : object.getString("COMMNCXBrokerageScheme");

                                            if (!equBrokSch.equals("")) {
                                                if (equBrokSch.equalsIgnoreCase("D")) {
                                                    spEquBrokerage.setSelection(1);
                                                } else if (equBrokSch.equalsIgnoreCase("MR")) {
                                                    spEquBrokerage.setSelection(2);
                                                } else {
                                                    spEquBrokerage.setSelection(0);
                                                }
                                            } else {
                                                CommonModel.setEquTradeSchSelID("");
                                                CommonModel.setEquDelSchSelID("");
                                                CommonModel.setEquFutSchSelID("");
                                                CommonModel.setEquOptSchSelID("");
                                                CommonModel.setEquDPSchSelID("");
                                                CommonModel.setEquCurrFutSchSelID("");
                                                CommonModel.setEquCurrOptSchSelID("");

                                            }
                                            if (!commMCXBrokSch.equals("")) {
                                                if (commMCXBrokSch.equalsIgnoreCase("D")) {
                                                    spCommBrokerageScheme.setSelection(1);
                                                } else if (commMCXBrokSch.equalsIgnoreCase("MR")) {
                                                    spCommBrokerageScheme.setSelection(2);
                                                } else {
                                                    spCommBrokerageScheme.setSelection(0);
                                                }
                                            } else {
                                                CommonModel.setCommFutSchSelID("");
                                                CommonModel.setCommOptSchSelID("");
                                                CommonModel.setCommDelSchSelID("");
                                            }
                                            if (!commNCXBrokSch.equals("")) {
                                                if (commNCXBrokSch.equalsIgnoreCase("D")) {
                                                    spNCXBrokerageScheme.setSelection(1);
                                                } else if (commNCXBrokSch.equalsIgnoreCase("MR")) {
                                                    spNCXBrokerageScheme.setSelection(2);
                                                } else {
                                                    spNCXBrokerageScheme.setSelection(0);
                                                }
                                            } else {

                                                CommonModel.setCommFutNCXSchSelID("");
                                                CommonModel.setCommOptNCXSchSelID("");
                                                CommonModel.setCommDelNCXSchSelID("");
                                            }


                                           /* spEquBrokerage.setOnItemSelectedListener(equBrokerageListener);
                                            spCommBrokerageScheme.setOnItemSelectedListener(commMCXBrokSchListener);
                                            spNCXBrokerageScheme.setOnItemSelectedListener(commNCXBrokSchListener);*/

                                        }
                                    }

                                }
                                if (response.has("TradingEQ")) {
                                    JSONArray jsonArrTradingEQ = response.getJSONArray("TradingEQ");
                                    if (jsonArrTradingEQ.length() != 0) {
                                        //   arrTradingEQ.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                        for (int i = 0; i < jsonArrTradingEQ.length(); i++) {
                                            JSONObject object = jsonArrTradingEQ.getJSONObject(i);
                                            String profileId =
                                                (object.isNull("Profile_Id") || object.getString(
                                                    "Profile_Id").equals("")) ? ""
                                                    : object.getString("Profile_Id");
                                            //                                    String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                            //                                    String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                            //                                    String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");
                                            String selectedProfileId =
                                                (object.isNull("SelectedProfileId")
                                                    || object.getString("SelectedProfileId")
                                                    .equals("")) ? ""
                                                    : object.getString("SelectedProfileId");

                                            if (selectedProfileId != null
                                                && !selectedProfileId.equals("")) {
                                                // spinnerSchemeSelect(selectedProfileId, arrAdapTradingEQ, spEquTrading);
                                                CommonModel.setEquTradeSchSelID(selectedProfileId);
                                                break;
                                            }

                                            // arrTradingEQ.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));


                                    /*if (selectedProfileId.equals(profileId)) {
                                        CommonModel.setEquTradeSchSelID(selectedProfileId);
                                        isEQTradingSchIDSel = true;
                                    }*/
                                    /*else {
                                        isEQTradingSchIDSel = false;
                                    }*/
                                    /*else {
                                        CommonModel.setEquTradeSchSelID(profileId);
                                    }*/

                                        }
                                    }
                           /* arrAdapTradingEQ = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrTradingEQ);
                            arrAdapTradingEQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spEquTrading.setAdapter(arrAdapTradingEQ);
                            spEquTrading.setOnItemSelectedListener(equTradListener);*/
                                    //spinnerSchemeSelect(getEquTradeSchSelID(), arrAdapTradingEQ, spEquTrading);
                                }
                                if (response.has("DeliveryEQ")) {
                                    JSONArray jsonArrDeliveryEQ =
                                        response.getJSONArray("DeliveryEQ");

                                    if (jsonArrDeliveryEQ.length() != 0) {
                                        //  arrDeliveryEQ.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                        for (int i = 0; i < jsonArrDeliveryEQ.length(); i++) {
                                            JSONObject object = jsonArrDeliveryEQ.getJSONObject(i);
                                            String profileId =
                                                (object.isNull("Profile_Id") || object.getString(
                                                    "Profile_Id").equals("")) ? ""
                                                    : object.getString("Profile_Id");
                                         /*   String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                            String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                            String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                            String selectedProfileId =
                                                (object.isNull("SelectedProfileId")
                                                    || object.getString("SelectedProfileId")
                                                    .equals("")) ? ""
                                                    : object.getString("SelectedProfileId");

                                            if (selectedProfileId != null
                                                && !selectedProfileId.equals("")) {
                                                //spinnerSchemeSelect(selectedProfileId, arrAdapDeliveryEQ, spEquDelivery);
                                                CommonModel.setEquDelSchSelID(selectedProfileId);

                                                break;
                                            }
                                    /*else {
                                        isEQDelSchIDSel = false;
                                    }*/
                                    /*else {
                                        CommonModel.setEquDelSchSelID(profileId);
                                    }*/
                                        }
                                    }
                                  /*  arrAdapDeliveryEQ = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrDeliveryEQ);
                                    arrAdapDeliveryEQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spEquDelivery.setAdapter(arrAdapDeliveryEQ);
                                    spEquDelivery.setOnItemSelectedListener(equDeliveryListener);*/
                                    // spinnerSchemeSelect(getEquDelSchSelID(), arrAdapDeliveryEQ, spEquDelivery);
                                }
                                if (isDerivativeOriginal()) {
                                    if (!isEquitySegmentExist()) {
                                        spEquFuture.setEnabled(true);
                                        spEquOption.setEnabled(true);
                                    } else {
                                        spEquOption.setEnabled(false);
                                        spEquFuture.setEnabled(false);
                                    }
                                    if (response.has("FuturesEQ")) {
                                        JSONArray jsonArrFuturesEQ =
                                            response.getJSONArray("FuturesEQ");

                                        if (jsonArrFuturesEQ.length() != 0) {
                                            // arrFuturesEQ.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                            for (int i = 0; i < jsonArrFuturesEQ.length(); i++) {
                                                JSONObject object =
                                                    jsonArrFuturesEQ.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                                /*String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                                String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                                String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                                String selectedProfileId =
                                                    (object.isNull("SelectedProfileId")
                                                        || object.getString("SelectedProfileId")
                                                        .equals("")) ? ""
                                                        : object.getString("SelectedProfileId");

                                                //                                                arrFuturesEQ.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));
                                                if (selectedProfileId != null
                                                    && !selectedProfileId.equals("")) {
                                                    //                                                    spinnerSchemeSelect(selectedProfileId, arrAdapFuturesEQ, spEquFuture);
                                                    CommonModel.setEquFutSchSelID(
                                                        selectedProfileId);
                                                    break;
                                                }
                                        /*else {
                                            isEQFutSchIDSel = false;
                                        }*/
                                        /*else {
                                            CommonModel.setEquDelSchSelID(profileId);
                                        }*/

                                            }
                                        }
                                        /*arrAdapFuturesEQ = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrFuturesEQ);
                                        arrAdapFuturesEQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                        spEquFuture.setAdapter(arrAdapFuturesEQ);
                                        spEquFuture.setOnItemSelectedListener(equFutureListener);*/
                                        // spinnerSchemeSelect(getEquFutSchSelID(), arrAdapFuturesEQ, spEquFuture);
                                    }
                                    if (response.has("OptionsEQ")) {
                                        JSONArray jsonArrOptionsEQ =
                                            response.getJSONArray("OptionsEQ");

                                        if (jsonArrOptionsEQ.length() != 0) {
                                            //   arrOptionsEQ.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                            for (int i = 0; i < jsonArrOptionsEQ.length(); i++) {
                                                JSONObject object =
                                                    jsonArrOptionsEQ.getJSONObject(i);
                                               /* String profileId = (object.isNull("Profile_Id") || object.getString("Profile_Id").equals("")) ? "" : object.getString("Profile_Id");
                                                String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                                String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                                String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                                String selectedProfileId =
                                                    (object.isNull("SelectedProfileId")
                                                        || object.getString("SelectedProfileId")
                                                        .equals("")) ? ""
                                                        : object.getString("SelectedProfileId");

                                                //                                                arrOptionsEQ.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));

                                                if (selectedProfileId != null
                                                    && !selectedProfileId.equals("")) {
                                                    //                                                    spinnerSchemeSelect(selectedProfileId, arrAdapOptionsEQ, spEquOption);
                                                    CommonModel.setEquOptSchSelID(
                                                        selectedProfileId);
                                                    break;
                                                }
                                        /*else {
                                            isEQOptSchIDSel = false;
                                        }*/
                                        /*else {
                                            CommonModel.setEquDelSchSelID(profileId);
                                        }*/
                                            }
                                        }
                                        /*arrAdapOptionsEQ = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrOptionsEQ);
                                        arrAdapOptionsEQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                        spEquOption.setAdapter(arrAdapOptionsEQ);
                                        spEquOption.setOnItemSelectedListener(equOptionListener);*/
                                        // spinnerSchemeSelect(getEquOptSchSelID(), arrAdapOptionsEQ, spEquOption);

                                    }
                                } else {
                                    spEquFuture.setEnabled(false);
                                    spEquOption.setEnabled(false);

                                }
                                if (response.has("DPEQ")) {
                                    JSONArray jsonArrDPEQ = response.getJSONArray("DPEQ");

                                    if (jsonArrDPEQ.length() != 0) {
                                        //arrDPEQ.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                        for (int i = 0; i < jsonArrDPEQ.length(); i++) {
                                            JSONObject object = jsonArrDPEQ.getJSONObject(i);
                                            String profileId =
                                                (object.isNull("Profile_Id") || object.getString(
                                                    "Profile_Id").equals("")) ? ""
                                                    : object.getString("Profile_Id");
                                           /* String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                            String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                            String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                            String selectedProfileId =
                                                (object.isNull("SelectedProfileId")
                                                    || object.getString("SelectedProfileId")
                                                    .equals("")) ? ""
                                                    : object.getString("SelectedProfileId");

                                            //                                            arrDPEQ.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));

                                            if (selectedProfileId != null
                                                && !selectedProfileId.equals("")) {
                                                //                                                spinnerSchemeSelect(selectedProfileId, arrAdapDPEQ, spEquDPAMC);
                                                CommonModel.setEquDPSchSelID(selectedProfileId);
                                                break;
                                            }
                                    /*else {
                                        isEQDPSchIDSel = false;
                                    }*/
                                    /*else {
                                        CommonModel.setEquDelSchSelID(profileId);
                                    }*/
                                        }
                                    }
                                 /*   arrAdapDPEQ = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrDPEQ);
                                    arrAdapDPEQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spEquDPAMC.setAdapter(arrAdapDPEQ);
                                    spEquDPAMC.setOnItemSelectedListener(equDPEQListener);*/
                                    //spinnerSchemeSelect(getEquDPSchSelID(), arrAdapDPEQ, spEquDPAMC);
                                }
                                if (isCurrencyOriginal()) {
                                    if (!isEquitySegmentExist()) {
                                        spEquCurrencyFuture.setEnabled(true);
                                        spEquCurrencyOption.setEnabled(true);
                                    } else {
                                        spEquCurrencyFuture.setEnabled(false);
                                        spEquCurrencyOption.setEnabled(false);
                                    }
                                    if (response.has("CurrencyFuturesEQ")) {
                                        JSONArray jsonArrCurrencyFuturesEQ =
                                            response.getJSONArray("CurrencyFuturesEQ");

                                        if (jsonArrCurrencyFuturesEQ.length() != 0) {
                                            // arrCurrencyFuturesEQ.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                            for (int i = 0; i < jsonArrCurrencyFuturesEQ.length();
                                                i++) {
                                                JSONObject object =
                                                    jsonArrCurrencyFuturesEQ.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                            /*String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                            String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                            String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                                String selectedProfileId =
                                                    (object.isNull("SelectedProfileId")
                                                        || object.getString("SelectedProfileId")
                                                        .equals("")) ? ""
                                                        : object.getString("SelectedProfileId");

                                                //                                            arrCurrencyFuturesEQ.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));

                                                if (selectedProfileId != null
                                                    && !selectedProfileId.equals("")) {
                                                    //                                                spinnerSchemeSelect(selectedProfileId, arrAdapCurrencyFuturesEQ, spEquCurrencyFuture);
                                                    CommonModel.setEquCurrFutSchSelID(
                                                        selectedProfileId);
                                                    break;
                                                }
                                    /*else {
                                        isEQCurFutSchIDSel = false;
                                    }*/
                                    /*else {
                                        CommonModel.setEquDelSchSelID(profileId);
                                    }*/
                                            }
                                        }
                                  /*  arrAdapCurrencyFuturesEQ = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrCurrencyFuturesEQ);
                                    arrAdapCurrencyFuturesEQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spEquCurrencyFuture.setAdapter(arrAdapCurrencyFuturesEQ);
                                    spEquCurrencyFuture.setOnItemSelectedListener(equCurrencyFutureListener);*/
                                        //spinnerSchemeSelect(getEquCurrFutSchSelID(), arrAdapCurrencyFuturesEQ, spEquCurrencyFuture);
                                    }
                                    if (response.has("CurrencyOptionsEQ")) {
                                        JSONArray jsonArrCurrencyOptionsEQ =
                                            response.getJSONArray("CurrencyOptionsEQ");

                                        if (jsonArrCurrencyOptionsEQ.length() != 0) {
                                            //   arrCurrencyOptionsEQ.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                            for (int i = 0; i < jsonArrCurrencyOptionsEQ.length();
                                                i++) {
                                                JSONObject object =
                                                    jsonArrCurrencyOptionsEQ.getJSONObject(i);
                                                String profileId = (object.isNull("Profile_Id")
                                                    || object.getString("Profile_Id").equals(""))
                                                    ? "" : object.getString("Profile_Id");
                                           /* String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                            String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                            String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                                String selectedProfileId =
                                                    (object.isNull("SelectedProfileId")
                                                        || object.getString("SelectedProfileId")
                                                        .equals("")) ? ""
                                                        : object.getString("SelectedProfileId");

                                                //                                            arrCurrencyOptionsEQ.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));
                                                if (selectedProfileId != null
                                                    && !selectedProfileId.equals("")) {
                                                    //                                                spinnerSchemeSelect(selectedProfileId, arrAdapCurrencyOptionsEQ, spEquCurrencyOption);
                                                    CommonModel.setEquCurrOptSchSelID(
                                                        selectedProfileId);
                                                    break;
                                                }
                                    /*else {
                                        CommonModel.setEquDelSchSelID(profileId);
                                    }*/
                                            }
                                        }
                                   /* arrAdapCurrencyOptionsEQ = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrCurrencyOptionsEQ);
                                    arrAdapCurrencyOptionsEQ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spEquCurrencyOption.setAdapter(arrAdapCurrencyOptionsEQ);
                                    spEquCurrencyOption.setOnItemSelectedListener(equCurrencyOptionsListener);*/
                                        //spinnerSchemeSelect(getEquCurrOptSchSelID(), arrAdapCurrencyOptionsEQ, spEquCurrencyOption);
                                    }
                                } else {
                                    spEquCurrencyFuture.setEnabled(false);
                                    spEquCurrencyOption.setEnabled(false);
                                }
                                if (response.has("FuturesCOMM")) {
                                    JSONArray jsonArrFuturesCOMM =
                                        response.getJSONArray("FuturesCOMM");

                                    if (jsonArrFuturesCOMM.length() != 0) {
                                        //                                        arrFuturesCOMM.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                        for (int i = 0; i < jsonArrFuturesCOMM.length(); i++) {
                                            JSONObject object = jsonArrFuturesCOMM.getJSONObject(i);
                                            String profileId =
                                                (object.isNull("Profile_Id") || object.getString(
                                                    "Profile_Id").equals("")) ? ""
                                                    : object.getString("Profile_Id");
                                           /* String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                            String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                            String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                            String selectedProfileId =
                                                (object.isNull("SelectedProfileId")
                                                    || object.getString("SelectedProfileId")
                                                    .equals("")) ? ""
                                                    : object.getString("SelectedProfileId");

                                            //                                            arrFuturesCOMM.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));
                                            if (selectedProfileId != null
                                                && !selectedProfileId.equals("")) {
                                                //                                                spinnerSchemeSelect(selectedProfileId, arrAdapFuturesCOMM, spCommFuture);
                                                CommonModel.setCommFutSchSelID(selectedProfileId);
                                                break;
                                            }

                                        }
                                    }
                                   /* arrAdapFuturesCOMM = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrFuturesCOMM);
                                    arrAdapFuturesCOMM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spCommFuture.setAdapter(arrAdapFuturesCOMM);
                                    spCommFuture.setOnItemSelectedListener(commFutListener);
                                    spinnerSchemeSelect(getCommFutSchSelID(), arrAdapFuturesCOMM, spCommFuture);*/
                                }
                                if (response.has("OptionsCOMM")) {
                                    JSONArray jsonArrOptionsCOMM =
                                        response.getJSONArray("OptionsCOMM");
                                    if (jsonArrOptionsCOMM.length() != 0) {
                                        //                                        arrOptionsCOMM.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                        for (int i = 0; i < jsonArrOptionsCOMM.length(); i++) {
                                            JSONObject object = jsonArrOptionsCOMM.getJSONObject(i);
                                            String profileId =
                                                (object.isNull("Profile_Id") || object.getString(
                                                    "Profile_Id").equals("")) ? ""
                                                    : object.getString("Profile_Id");
                                           /* String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                            String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                            String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                            String selectedProfileId =
                                                (object.isNull("SelectedProfileId")
                                                    || object.getString("SelectedProfileId")
                                                    .equals("")) ? ""
                                                    : object.getString("SelectedProfileId");

                                            //                                            arrOptionsCOMM.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));
                                            if (selectedProfileId.equals(profileId)) {
                                                CommonModel.setCommOptSchSelID(selectedProfileId);
                                                break;
                                            }

                                        }
                                    }
                                    /*arrAdapOptionsCOMM = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrOptionsCOMM);
                                    arrAdapOptionsCOMM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spCommOption.setAdapter(arrAdapOptionsCOMM);
                                    spCommOption.setOnItemSelectedListener(commOptListener);
                                    spinnerSchemeSelect(getCommOptSchSelID(), arrAdapOptionsCOMM, spCommOption);*/

                                }
                                if (response.has("DeliveryCOMM")) {
                                    JSONArray jsonArrDeliveryCOMM =
                                        response.getJSONArray("DeliveryCOMM");


                                    if (jsonArrDeliveryCOMM.length() != 0) {
                                        //                                        arrDeliveryCOMM.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                        for (int i = 0; i < jsonArrDeliveryCOMM.length(); i++) {
                                            JSONObject object =
                                                jsonArrDeliveryCOMM.getJSONObject(i);
                                            String profileId =
                                                (object.isNull("Profile_Id") || object.getString(
                                                    "Profile_Id").equals("")) ? ""
                                                    : object.getString("Profile_Id");
                                          /*  String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                            String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                            String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                            String selectedProfileId =
                                                (object.isNull("SelectedProfileId")
                                                    || object.getString("SelectedProfileId")
                                                    .equals("")) ? ""
                                                    : object.getString("SelectedProfileId");

                                            //  arrDeliveryCOMM.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));
                                            if (selectedProfileId.equals(profileId)) {
                                                CommonModel.setCommDelSchSelID(selectedProfileId);
                                            }

                                        }
                                    }
                                   /* arrAdapDeliveryCOMM = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrDeliveryCOMM);
                                    arrAdapDeliveryCOMM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spCommDPAMC.setAdapter(arrAdapDeliveryCOMM);
                                    spCommDPAMC.setOnItemSelectedListener(commDelListener);
                                    spinnerSchemeSelect(getCommDelSchSelID(), arrAdapDeliveryCOMM, spCommDPAMC);*/

                                }
                                if (response.has("FuturesNCXCOMM")) {
                                    JSONArray jsonArrFuturesNCXCOMM =
                                        response.getJSONArray("FuturesNCXCOMM");

                                    if (jsonArrFuturesNCXCOMM.length() != 0) {
                                        //                                        arrFuturesNCXCOMM.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                        for (int i = 0; i < jsonArrFuturesNCXCOMM.length(); i++) {
                                            JSONObject object =
                                                jsonArrFuturesNCXCOMM.getJSONObject(i);
                                            String profileId =
                                                (object.isNull("Profile_Id") || object.getString(
                                                    "Profile_Id").equals("")) ? ""
                                                    : object.getString("Profile_Id");
                                          /*  String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                            String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                            String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                            String selectedProfileId =
                                                (object.isNull("SelectedProfileId")
                                                    || object.getString("SelectedProfileId")
                                                    .equals("")) ? ""
                                                    : object.getString("SelectedProfileId");

                                            //  arrFuturesNCXCOMM.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));
                                            if (selectedProfileId.equals(profileId)) {
                                                CommonModel.setCommFutNCXSchSelID(
                                                    selectedProfileId);
                                            }
                                        }
                                    }
                                   /* arrAdapFuturesNCXCOMM = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrFuturesNCXCOMM);
                                    arrAdapFuturesNCXCOMM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spNCXFuture.setAdapter(arrAdapFuturesNCXCOMM);
                                    spNCXFuture.setOnItemSelectedListener(commNCXFutListener);
                                    spinnerSchemeSelect(getCommFutNCXSchSelID(), arrAdapFuturesNCXCOMM, spNCXFuture);*/
                                }

                                if (response.has("OptionsNCXCOMM")) {
                                    JSONArray jsonArrOptionsNCXCOMM =
                                        response.getJSONArray("OptionsNCXCOMM");

                                    if (jsonArrOptionsNCXCOMM.length() != 0) {
                                        //                                        arrOptionsNCXCOMM.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                        for (int i = 0; i < jsonArrOptionsNCXCOMM.length(); i++) {
                                            JSONObject object =
                                                jsonArrOptionsNCXCOMM.getJSONObject(i);
                                            String profileId =
                                                (object.isNull("Profile_Id") || object.getString(
                                                    "Profile_Id").equals("")) ? ""
                                                    : object.getString("Profile_Id");
                                            /*String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                            String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                            String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                            String selectedProfileId =
                                                (object.isNull("SelectedProfileId")
                                                    || object.getString("SelectedProfileId")
                                                    .equals("")) ? ""
                                                    : object.getString("SelectedProfileId");

                                            // arrOptionsNCXCOMM.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));
                                            if (selectedProfileId.equals(profileId)) {
                                                CommonModel.setCommOptNCXSchSelID(
                                                    selectedProfileId);
                                            }
                                        }
                                    }
                                    /*arrAdapOptionsNCXCOMM = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrOptionsNCXCOMM);
                                    arrAdapOptionsNCXCOMM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spNCXOption.setAdapter(arrAdapOptionsNCXCOMM);
                                    spNCXOption.setOnItemSelectedListener(commNCXOptListener);
                                    spinnerSchemeSelect(getCommOptNCXSchSelID(), arrAdapOptionsNCXCOMM, spNCXOption);*/
                                }
                                if (response.has("DeliveryNCXCOMM")) {
                                    JSONArray jsonArrDeliveryNCXCOMM =
                                        response.getJSONArray("DeliveryNCXCOMM");


                                    if (jsonArrDeliveryNCXCOMM.length() != 0) {
                                        //arrDeliveryNCXCOMM.add(new RowData(0, "-1", "Select", "", "Select", "", ""));
                                        for (int i = 0; i < jsonArrDeliveryNCXCOMM.length(); i++) {
                                            JSONObject object =
                                                jsonArrDeliveryNCXCOMM.getJSONObject(i);
                                            String profileId =
                                                (object.isNull("Profile_Id") || object.getString(
                                                    "Profile_Id").equals("")) ? ""
                                                    : object.getString("Profile_Id");
                                            /*String profileName = (object.isNull("Profile_Name") || object.getString("Profile_Name").equals("")) ? "" : object.getString("Profile_Name");
                                            String segment = (object.isNull("Segment") || object.getString("Segment").equals("")) ? "" : object.getString("Segment");
                                            String userId = (object.isNull("UserId") || object.getString("UserId").equals("")) ? "" : object.getString("UserId");*/
                                            String selectedProfileId =
                                                (object.isNull("SelectedProfileId")
                                                    || object.getString("SelectedProfileId")
                                                    .equals("")) ? ""
                                                    : object.getString("SelectedProfileId");

                                            // arrDeliveryNCXCOMM.add(new RowData((i + 1), profileId, profileName, profileId, profileName, selectedProfileId, ""));
                                            if (selectedProfileId.equals(profileId)) {
                                                CommonModel.setCommDelNCXSchSelID(
                                                    selectedProfileId);
                                            }
                                        }
                                    }
                                   /* arrAdapDelNCXCOMM = new ArrayAdapter<RowData>(mContext, R.layout.simple_spinner_item, arrDeliveryNCXCOMM);
                                    arrAdapDelNCXCOMM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spNCXDPAMC.setAdapter(arrAdapDelNCXCOMM);
                                    spNCXDPAMC.setOnItemSelectedListener(commNCXDelListener);
                                    spinnerSchemeSelect(getCommDelNCXSchSelID(), arrAdapDelNCXCOMM, spNCXDPAMC);*/
                                }
                                customProgress.setVisibility(View.GONE);

                            } catch (Exception e) {
                                e.printStackTrace();
                                commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false,
                                    "");
                                customProgress.setVisibility(View.GONE);
                            }
                        } else {
                            // MOSLCommon.showToastMsg(mContext, NO_RESPONSE + " for scheme");
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                        //customProgress.setVisibility(View.GONE);
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
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable
                        throwable, JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable
                        throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                }

            );
        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String strEquityMICR = etEquityMICR.getText().toString();
        String strEquityAccNumber = etEquityAccNumber.getText().toString();
        String strCommMICR = etCommMICR.getText().toString();
        String strCommAccNumber = etCommAccNumber.getText().toString();
        String strEquMarginDeposit = etEquMarginDeposit.getText().toString();
        String strCommMarginDeposit = etCommMarginDeposit.getText().toString();


        if (!etEquityMICR.getText().toString().equals("")) {
            etEquSelectBankError.setVisibility(View.GONE);
        }
        if (!etCommMICR.getText().toString().equals("")) {
            etCommSelectBankError.setVisibility(View.GONE);
        }
        if (!etMFMICR.getText().toString().equals("")) {
            etMFSelectBankError.setVisibility(View.GONE);
        }

        if (etEquMarginDeposit.isEnabled() && !strEquMarginDeposit.equals("")) {
            if (!getEqSchemeId().equalsIgnoreCase(MF_CUM_EQUITY)
                && Integer.parseInt(strEquMarginDeposit) < minMarginValueForOtherAmount()) {
                etEquMarginDepositError.setVisibility(View.VISIBLE);
                etEquMarginDepositError.setText(
                    getAppType().equalsIgnoreCase(DIRECT) ? getResources().getString(
                        R.string.margin_25000) : getResources().getString(R.string.margin_50000));
                btnGetEquScheme.setBackgroundColor(
                    getResources().getColor(R.color.light_grey_menu));
                btnGetEquScheme.setEnabled(false);
            } else if (Integer.parseInt(strEquMarginDeposit) >= minMarginValueForOtherAmount()) {
                input_layout_equMarginDeposit.setErrorEnabled(false);
                etEquMarginDepositError.setVisibility(View.GONE);
                btnGetEquScheme.setEnabled(true);
                btnGetEquScheme.setBackgroundColor(
                    getResources().getColor(R.color.secondaryBackground));
                etCommMarginDeposit.setError(null);
            }
        } else {
            etEquMarginDepositError.setVisibility(View.GONE);
        }
        if (etCommMarginDeposit.isEnabled() && !strCommMarginDeposit.equals("")) {

            if (Integer.parseInt(strCommMarginDeposit) < minMarginValueForOtherAmount()) {
                etCommMarginDepositError.setVisibility(View.VISIBLE);
                etCommMarginDepositError.setText(
                    getAppType().equalsIgnoreCase(DIRECT) ? getResources().getString(
                        R.string.margin_25000) : getResources().getString(R.string.margin_50000));
                btnGetCommScheme.setBackgroundColor(
                    getResources().getColor(R.color.light_grey_menu));
                btnGetCommScheme.setEnabled(false);
            } else if (Integer.parseInt(strCommMarginDeposit) >= minMarginValueForOtherAmount()) {
                input_layout_commMarginDeposit.setErrorEnabled(false);
                etCommMarginDepositError.setVisibility(View.GONE);
                btnGetCommScheme.setEnabled(true);
                btnGetCommScheme.setBackgroundColor(
                    getResources().getColor(R.color.secondaryBackground));
                etCommMarginDeposit.setError(null);
                // etCommMarginDeposit.setText(strCommMarginDeposit);
            }
        } else {
            etCommMarginDepositError.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
       /* if (i == R.id.cbMarginAcc) {
            btnMarginAllScheme.setVisibility(View.VISIBLE);
            btnMarginSelectedScheme.setVisibility(View.VISIBLE);
            if (isChecked) {
                etEquAccountError.setVisibility(View.GONE);
                removeKeyboard(mContext, getView());
                cbMarginAcc.setChecked(true);
                cbValuePacAcc.setChecked(false);
                llSelectedScheme.setVisibility(View.GONE);
                llMarginDeposit.setVisibility(View.VISIBLE);
                getBindMarginSchemeDetails("");
            } else {
                btnMarginAllScheme.setVisibility(View.GONE);
                btnMarginSelectedScheme.setVisibility(View.GONE);
                cbValuePacAcc.setChecked(true);
                cbMarginAcc.setChecked(false);
                llSelectedScheme.setVisibility(View.VISIBLE);
                llMarginDeposit.setVisibility(View.GONE);
            }
        } else if (i == R.id.cbValuePacAcc) {
            btnMarginAllScheme.setVisibility(View.GONE);
            btnMarginSelectedScheme.setVisibility(View.GONE);
            if (isChecked) {
                etEquAccountError.setVisibility(View.GONE);
                removeKeyboard(mContext, getView());
                cbMarginAcc.setChecked(false);
                cbValuePacAcc.setChecked(true);
                llSelectedScheme.setVisibility(View.VISIBLE);
                llMarginDeposit.setVisibility(View.GONE);
                getBindValuePacSchemeDetails("");
            } else {
                btnMarginAllScheme.setVisibility(View.VISIBLE);
                cbValuePacAcc.setChecked(false);
                cbMarginAcc.setChecked(true);
                llSelectedScheme.setVisibility(View.GONE);
                llMarginDeposit.setVisibility(View.VISIBLE);
            }
        }*/
    }

    public void showCommdityBankDetails() {
        llEquityBankDetail.setVisibility(View.VISIBLE);
        llEquityBankScheme.setVisibility(View.GONE);
        cbSameAsEquity.setVisibility(View.GONE);
        //        llCommodityBankDetail.setVisibility(View.VISIBLE);
        llCommodityBankScheme.setVisibility(View.VISIBLE);
        if (!isEquityOriginal()) {
            llSpinnerCommScheme.setVisibility(View.VISIBLE);
        }
        //        fillCommodityBankDropDown();
        fillEquityBankDropDown();
    }

    public void showEquityBankDetails() {
        llEquityBankDetail.setVisibility(View.VISIBLE);
        llEquityBankScheme.setVisibility(View.VISIBLE);
        llCommodityBankDetail.setVisibility(View.GONE);
        llCommodityBankScheme.setVisibility(View.GONE);
        fillEquityBankDropDown();
    }

    public void showBothBankDetails() {
        llEquityBankDetail.setVisibility(View.VISIBLE);
        llEquityBankScheme.setVisibility(View.VISIBLE);
        //        llCommodityBankDetail.setVisibility(View.VISIBLE);
        llCommodityBankScheme.setVisibility(View.VISIBLE);
        //        cbSameAsEquity.setVisibility(View.VISIBLE);
        //        fillBothBankDropDown();
        llSpinnerCommScheme.setVisibility(View.GONE);
        fillEquityBankDropDown();
    }

    public void getReviewDetail() {
        //  dialogReview = new Dialog(mContext);
        /*dialogReview.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogReview.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogReview.setContentView(R.layout.frag_review_and_edit);
        dialogReview.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogReview.setCanceledOnTouchOutside(false);*/

        //        tvResidentStatus = (TextView) dialogReview.findViewById(R.id.tvResidentStatus);
        //        tvSubStatus = (TextView) dialogReview.findViewById(R.id.tvSubStatus);
        llCustomProgress = (LinearLayout) dialogReview.findViewById(R.id.customProgress);

        llCountryCitizenship = (LinearLayout) dialogReview.findViewById(R.id.llCountryCitizenship);
        llCountryBirth = (LinearLayout) dialogReview.findViewById(R.id.llCountryBirth);
        llCountryTax = (LinearLayout) dialogReview.findViewById(R.id.llCountryTax);

        tvName = (TextView) dialogReview.findViewById(R.id.tvName);
        tvGender = (TextView) dialogReview.findViewById(R.id.tvGender);
        tvRelation = (TextView) dialogReview.findViewById(R.id.tvRelation);
        tvFatherSpouseName = (TextView) dialogReview.findViewById(R.id.tvFatherSpouseName);
        tvMotherName = (TextView) dialogReview.findViewById(R.id.tvMotherName);
        tvMaidenName = (TextView) dialogReview.findViewById(R.id.tvMaidenName);
        tvDOB = (TextView) dialogReview.findViewById(R.id.tvDOB);
        tvPAN = (TextView) dialogReview.findViewById(R.id.tvPAN);
        tvNameOnPAN = (TextView) dialogReview.findViewById(R.id.tvNameOnPAN);
        tvMaritalStatus = (TextView) dialogReview.findViewById(R.id.tvMaritalStatus);
        tvAadhaarNumber = (TextView) dialogReview.findViewById(R.id.tvAadhaarNumber);

        tvFlatNoCorres = (TextView) dialogReview.findViewById(R.id.tvFlatNoCorres);
        tvBldgNoCorres = (TextView) dialogReview.findViewById(R.id.tvBldgNoCorres);
        tvAreaCorres = (TextView) dialogReview.findViewById(R.id.tvAreaCorres);
        tvCityCorres = (TextView) dialogReview.findViewById(R.id.tvCityCorres);
        tvStateCorres = (TextView) dialogReview.findViewById(R.id.tvStateCorres);
        tvCountryCorres = (TextView) dialogReview.findViewById(R.id.tvCountryCorres);
        tvPincodeCorres = (TextView) dialogReview.findViewById(R.id.tvPincodeCorres);
        tvAddProofCorres = (TextView) dialogReview.findViewById(R.id.tvAddProofCorres);

        tvFlatNoPerm = (TextView) dialogReview.findViewById(R.id.tvFlatNoPerm);
        tvBldgNoPerm = (TextView) dialogReview.findViewById(R.id.tvBldgNoPerm);
        tvAreaPerm = (TextView) dialogReview.findViewById(R.id.tvAreaPerm);
        tvCityPerm = (TextView) dialogReview.findViewById(R.id.tvCityPerm);
        tvStatePerm = (TextView) dialogReview.findViewById(R.id.tvStatePerm);
        tvCountryPerm = (TextView) dialogReview.findViewById(R.id.tvCountryPerm);
        tvPincodePerm = (TextView) dialogReview.findViewById(R.id.tvPincodePerm);
        tvAddProofPerm = (TextView) dialogReview.findViewById(R.id.tvAddProofPerm);

        tvMobile = (TextView) dialogReview.findViewById(R.id.tvMobile);
        tvEmail = (TextView) dialogReview.findViewById(R.id.tvEmail);
        //        tvTelNo = (TextView) dialogReview.findViewById(R.id.tvTelNo);
        //        tvFbId = (TextView) dialogReview.findViewById(R.id.tvFbId);
        //        tvTwitterId = (TextView) dialogReview.findViewById(R.id.tvTwitterId);
        //        tvLinkedinID = (TextView) dialogReview.findViewById(R.id.tvLinkedinID);

        tvOccupation = (TextView) dialogReview.findViewById(R.id.tvOccupation);
        tvQualification = (TextView) dialogReview.findViewById(R.id.tvQualification);
        tvIncome = (TextView) dialogReview.findViewById(R.id.tvIncome);
        tvContract = (TextView) dialogReview.findViewById(R.id.tvContract);
        //        tvStockPortfolio = (TextView) dialogReview.findViewById(R.id.tvStockPortfolio);
        tvNetWorth = (TextView) dialogReview.findViewById(R.id.tvNetWorth);
        tvNWDate = (TextView) dialogReview.findViewById(R.id.tvNWDate);
        tvPoliticalyExposed = (TextView) dialogReview.findViewById(R.id.tvPoliticalyExposed);
        tvRelatedPolitical = (TextView) dialogReview.findViewById(R.id.tvRelatedPolitical);
        //        tvBrokerName = (TextView) dialogReview.findViewById(R.id.tvBrokerName);
        //        tvClientCode = (TextView) dialogReview.findViewById(R.id.tvClientCode);
        //        tvSebiAuthority = (TextView) dialogReview.findViewById(R.id.tvSebiAuthority);

        //Bank details
        tvEqBankNameReview = (TextView) dialogReview.findViewById(R.id.tvEqBankName);
        tvEqMICRReview = (TextView) dialogReview.findViewById(R.id.tvEqMICR);
        tvEqIFSCReview = (TextView) dialogReview.findViewById(R.id.tvEqIFSC);
        tvEqBranchNameReview = (TextView) dialogReview.findViewById(R.id.tvEqBranchName);
        tvEqBankAddressReview = (TextView) dialogReview.findViewById(R.id.tvEqBankAddress);
        tvEqAccNumberReview = (TextView) dialogReview.findViewById(R.id.tvEqAccNumber);
        tvEqAccTypeReview = (TextView) dialogReview.findViewById(R.id.tvEqAccType);
        //        tvEqPhotoProofReview = (TextView) dialogReview.findViewById(R.id.tvEqPhotoProof);
        //        tvEqBankProofReview = (TextView) dialogReview.findViewById(R.id.tvEqBankProof);
        tvEqProductReview = (TextView) dialogReview.findViewById(R.id.tvEqProduct);

        tvCommBankNameReview = (TextView) dialogReview.findViewById(R.id.tvCommBankName);
        tvCommMICRReview = (TextView) dialogReview.findViewById(R.id.tvCommMICR);
        tvCommIFSCReview = (TextView) dialogReview.findViewById(R.id.tvCommIFSC);
        tvCommBranchNameReview = (TextView) dialogReview.findViewById(R.id.tvCommBranchName);
        tvCommBankAddressReview = (TextView) dialogReview.findViewById(R.id.tvCommBankAddress);
        tvCommAccNumberReview = (TextView) dialogReview.findViewById(R.id.tvCommAccNumber);
        tvCommAccTypeReview = (TextView) dialogReview.findViewById(R.id.tvCommAccType);
        //        tvCommPhotoProofReview = (TextView) dialogReview.findViewById(R.id.tvCommPhotoProof);
        //        tvCommBankProofReview = (TextView) dialogReview.findViewById(R.id.tvCommBankProof);
        tvCommProductReview = (TextView) dialogReview.findViewById(R.id.tvCommProduct);

        //Edit
        tvEditPersonalDetail = (ImageView) dialogReview.findViewById(R.id.tvEditPersonalDetail);
        tvEditNomineeJointDetail =
            (ImageView) dialogReview.findViewById(R.id.tvEditNomineeJointDetail);
        tvNomineeJointTitle = (TextView) dialogReview.findViewById(R.id.tvNomineeJointTitle);
        tvEditBankDetail = (ImageView) dialogReview.findViewById(R.id.tvEditBankDetail);
        btnNext = (Button) dialogReview.findViewById(R.id.btnNext);

        llEqBankDetail = (LinearLayout) dialogReview.findViewById(R.id.llEqBankDetail);
        llCommBankDetail = (LinearLayout) dialogReview.findViewById(R.id.llCommBankDetail);
        llSchemeDetail = (LinearLayout) dialogReview.findViewById(R.id.llSchemeDetail);
        llReviewTC = (LinearLayout) dialogReview.findViewById(R.id.llReviewTC);
        llEqSchemeDetail = (LinearLayout) dialogReview.findViewById(R.id.llEqSchemeDetail);
        llCommSchemeDetail = (LinearLayout) dialogReview.findViewById(R.id.llCommSchemeDetail);
        llEqSchemeDetailReview =
            (LinearLayout) dialogReview.findViewById(R.id.llEqSchemeDetailReview);
        llCommSchemeDetailReview =
            (LinearLayout) dialogReview.findViewById(R.id.llCommSchemeDetailReview);
        llMFBankDetail = (LinearLayout) dialogReview.findViewById(R.id.llMFBankDetail);
        llContractNo = (LinearLayout) dialogReview.findViewById(R.id.llContractNo);

        tvMFReviewBankName = (TextView) dialogReview.findViewById(R.id.tvMFReviewBankName);
        tvMFReviewBranchName = (TextView) dialogReview.findViewById(R.id.tvMFReviewBranchName);
        tvMFReviewMICR = (TextView) dialogReview.findViewById(R.id.tvMFReviewMICR);
        tvMFReviewIFSC = (TextView) dialogReview.findViewById(R.id.tvMFReviewIFSC);
        tvMFReviewBankAddress = (TextView) dialogReview.findViewById(R.id.tvMFReviewBankAddress);
        tvMFReviewAccNumber = (TextView) dialogReview.findViewById(R.id.tvMFReviewAccNumber);
        tvMFReviewAccType = (TextView) dialogReview.findViewById(R.id.tvMFReviewAccType);

        tvCountryCitizenshipQuestion =
            (TextView) dialogReview.findViewById(R.id.tvCountryCitizenshipQuestion);
        tvCountryBirthQuestion = (TextView) dialogReview.findViewById(R.id.tvCountryBirthQuestion);
        tvCountryTaxQuestion = (TextView) dialogReview.findViewById(R.id.tvCountryTaxQuestion);
        tvPoaAddressQuestion = (TextView) dialogReview.findViewById(R.id.tvPoaAddressQuestion);
        tvAddressOutsideIndiaQuestion =
            (TextView) dialogReview.findViewById(R.id.tvAddressOutsideIndiaQuestion);
        tvCounrtyOfCitizen = (TextView) dialogReview.findViewById(R.id.tvCounrtyOfCitizen);
        tvCountryOfBirth = (TextView) dialogReview.findViewById(R.id.tvCountryOfBirth);
        tvPlaceOfBirth = (TextView) dialogReview.findViewById(R.id.tvPlaceOfBirth);
        tvCountryOfTaXRes = (TextView) dialogReview.findViewById(R.id.tvCountryOfTaXRes);
        tvTaxIDNum = (TextView) dialogReview.findViewById(R.id.tvTaxIDNum);

        llNomineeDematMain = (LinearLayout) dialogReview.findViewById(R.id.llNomineeDematMain);
        llNomineeDetail =
            (LinearLayout) dialogReview.findViewById(R.id.llNomineeDetail);//Nominee details
        llNomineeGuardianDetails =
            (LinearLayout) dialogReview.findViewById(R.id.llNomineeGuardianDetails);
        llSecondHolderLayoutDetail =
            (LinearLayout) dialogReview.findViewById(R.id.llSecondHolderLayoutDetail);
        llThirdHolderLayoutDetail =
            (LinearLayout) dialogReview.findViewById(R.id.llThirdHolderLayoutDetail);
        tvNomineeDetailMsg = (TextView) dialogReview.findViewById(R.id.tvNomineeDetailMsg);
        tvSecondHolderNotProvidedMsg =
            (TextView) dialogReview.findViewById(R.id.tvSecondHolderNotProvidedMsg);
        tvThirdHolderNotProvidedMsg =
            (TextView) dialogReview.findViewById(R.id.tvThirdHolderNotProvidedMsg);
        tvNomineeName = (TextView) dialogReview.findViewById(R.id.tvNomineeName);
        tvNomineePAN = (TextView) dialogReview.findViewById(R.id.tvNomineePAN);
        tvNomineeAadhaar = (TextView) dialogReview.findViewById(R.id.tvNomineeAadhaar);
        tvNomineeDOB = (TextView) dialogReview.findViewById(R.id.tvNomineeDOB);
        tvNomineeEmail = (TextView) dialogReview.findViewById(R.id.tvNomineeEmail);
        tvNomineeTelNo = (TextView) dialogReview.findViewById(R.id.tvNomineeTelNo);
        tvNomineeRelationship = (TextView) dialogReview.findViewById(R.id.tvNomineeRelationship);
        //        tvNomineeApplicantRelationship = (TextView) dialogReview.findViewById(R.id.tvNomineeApplicantRelationship);
        tvNomineeFlatNoCorres = (TextView) dialogReview.findViewById(R.id.tvNomineeFlatNoCorres);
        tvNomineeBldgNoCorres = (TextView) dialogReview.findViewById(R.id.tvNomineeBldgNoCorres);
        tvNomineeCountryCorres = (TextView) dialogReview.findViewById(R.id.tvNomineeCountryCorres);
        tvNomineeAreaCorres = (TextView) dialogReview.findViewById(R.id.tvNomineeAreaCorres);
        tvNomineeCityCorres = (TextView) dialogReview.findViewById(R.id.tvNomineeCityCorres);
        tvNomineeStateCorres = (TextView) dialogReview.findViewById(R.id.tvNomineeStateCorres);
        tvNomineePincodeCorres = (TextView) dialogReview.findViewById(R.id.tvNomineePincodeCorres);
        tvNomineeGuardianName =
            (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianName);//Guardian details
        tvNomineeGuardianAge = (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianAge);
        tvNomineeGuardianEmail = (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianEmail);
        tvNomineeGuardianTelNo = (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianTelNo);
        tvNomineeGuardianRelationship =
            (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianRelationship);
        tvNomineeGuardianFlatNoCorres =
            (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianFlatNoCorres);
        tvNomineeGuardianBldgNoCorres =
            (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianBldgNoCorres);
        tvNomineeGuardianCountryCorres =
            (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianCountryCorres);
        tvNomineeGuardianAreaCorres =
            (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianAreaCorres);
        tvNomineeGuardianCityCorres =
            (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianCityCorres);
        tvNomineeGuardianStateCorres =
            (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianStateCorres);
        tvNomineeGuardianPincodeCorres =
            (TextView) dialogReview.findViewById(R.id.tvNomineeGuardianPincodeCorres);

        //tvSecHoldEditPersonalDetail = (TextView) dialogReview.findViewById(R.id.tvSecHoldEditPersonalDetail);
        tvSecHoldResidentStatus =
            (TextView) dialogReview.findViewById(R.id.tvSecHoldResidentStatus);
        tvSecHoldName = (TextView) dialogReview.findViewById(R.id.tvSecHoldName);
        tvSecHoldGender = (TextView) dialogReview.findViewById(R.id.tvSecHoldGender);
        tvSecHoldFatherorSpouseName =
            (TextView) dialogReview.findViewById(R.id.tvSecHoldFatherorSpouseName);
        tvSecHolddob = (TextView) dialogReview.findViewById(R.id.tvSecHolddob);
        tvSecHoldMaritalStatus = (TextView) dialogReview.findViewById(R.id.tvSecHoldMaritalStatus);
        tvSecHoldPan = (TextView) dialogReview.findViewById(R.id.tvSecHoldPan);
        tvSecHoldAadhaar = (TextView) dialogReview.findViewById(R.id.tvSecHoldAadhaar);
        tvSecHoldMobile = (TextView) dialogReview.findViewById(R.id.tvSecHoldMobile);
        tvSecHoldEmail = (TextView) dialogReview.findViewById(R.id.tvSecHoldEmail);
        tvSecHoldTel = (TextView) dialogReview.findViewById(R.id.tvSecHoldTel);
        tvSecHoldCorrAddProof = (TextView) dialogReview.findViewById(R.id.tvSecHoldCorrAddProof);
        tvSecHoldCorrBldgName = (TextView) dialogReview.findViewById(R.id.tvSecHoldCorrBldgName);
        tvSecHoldCorrFlatNo = (TextView) dialogReview.findViewById(R.id.tvSecHoldCorrFlatNo);
        tvSecHoldCorrArea = (TextView) dialogReview.findViewById(R.id.tvSecHoldCorrArea);
        tvSecHoldCorrPincode = (TextView) dialogReview.findViewById(R.id.tvSecHoldCorrPincode);
        tvSecHoldCorrState = (TextView) dialogReview.findViewById(R.id.tvSecHoldCorrState);
        tvSecHoldCorrCity = (TextView) dialogReview.findViewById(R.id.tvSecHoldCorrCity);
        tvSecHoldCorrCountry = (TextView) dialogReview.findViewById(R.id.tvSecHoldCorrCountry);
        tvSecHoldPermAddProof = (TextView) dialogReview.findViewById(R.id.tvSecHoldPermAddProof);
        tvSecHoldPermBldgName = (TextView) dialogReview.findViewById(R.id.tvSecHoldPermBldgName);
        tvSecHoldPermFlatNo = (TextView) dialogReview.findViewById(R.id.tvSecHoldPermFlatNo);
        tvSecHoldPermPincode = (TextView) dialogReview.findViewById(R.id.tvSecHoldPermPincode);
        tvSecHoldPermState = (TextView) dialogReview.findViewById(R.id.tvSecHoldPermState);
        tvSecHoldPermCity = (TextView) dialogReview.findViewById(R.id.tvSecHoldPermCity);
        tvSecHoldPermCountry = (TextView) dialogReview.findViewById(R.id.tvSecHoldPermCountry);
        tvSecHoldPermArea = (TextView) dialogReview.findViewById(R.id.tvSecHoldPermArea);
        tvSecHoldAnnualIncome = (TextView) dialogReview.findViewById(R.id.tvSecHoldAnnualIncome);
        tvSecHoldOccDetail = (TextView) dialogReview.findViewById(R.id.tvSecHoldOccDetail);
        tvSecHoldPoliticalExposedPerson =
            (TextView) dialogReview.findViewById(R.id.tvSecHoldPoliticalExposedPerson);
        tvSecHoldRelatedPoliticalExposedPerson =
            (TextView) dialogReview.findViewById(R.id.tvSecHoldRelatedPoliticalExposedPerson);
        // tvThirdHoldEditPersonalDetail = (TextView) dialogReview.findViewById(R.id.tvThirdHoldEditPersonalDetail);
        tvThirdHoldResidentStatus =
            (TextView) dialogReview.findViewById(R.id.tvThirdHoldResidentStatus);
        tvThirdHoldName = (TextView) dialogReview.findViewById(R.id.tvThirdHoldName);
        tvThirdHoldGender = (TextView) dialogReview.findViewById(R.id.tvThirdHoldGender);
        tvThirdHoldFatherorSpouseName =
            (TextView) dialogReview.findViewById(R.id.tvThirdHoldFatherorSpouseName);
        tvThirdHolddob = (TextView) dialogReview.findViewById(R.id.tvThirdHolddob);
        tvThirdHoldMaritalStatus =
            (TextView) dialogReview.findViewById(R.id.tvThirdHoldMaritalStatus);
        tvThirdHoldPan = (TextView) dialogReview.findViewById(R.id.tvThirdHoldPan);
        tvThirdHoldAadhaar = (TextView) dialogReview.findViewById(R.id.tvThirdHoldAadhaar);
        tvThirdHoldMobile = (TextView) dialogReview.findViewById(R.id.tvThirdHoldMobile);
        tvThirdHoldEmail = (TextView) dialogReview.findViewById(R.id.tvThirdHoldEmail);
        tvThirdHoldTel = (TextView) dialogReview.findViewById(R.id.tvThirdHoldTel);
        tvThirdHoldCorrAddProof =
            (TextView) dialogReview.findViewById(R.id.tvThirdHoldCorrAddProof);
        tvThirdHoldCorrBldgName =
            (TextView) dialogReview.findViewById(R.id.tvThirdHoldCorrBldgName);
        tvThirdHoldCorrFlatNo = (TextView) dialogReview.findViewById(R.id.tvThirdHoldCorrFlatNo);
        tvThirdHoldCorrArea = (TextView) dialogReview.findViewById(R.id.tvThirdHoldCorrArea);
        tvThirdHoldCorrPincode = (TextView) dialogReview.findViewById(R.id.tvThirdHoldCorrPincode);
        tvThirdHoldCorrState = (TextView) dialogReview.findViewById(R.id.tvThirdHoldCorrState);
        tvThirdHoldCorrCity = (TextView) dialogReview.findViewById(R.id.tvThirdHoldCorrCity);
        tvThirdHoldCorrCountry = (TextView) dialogReview.findViewById(R.id.tvThirdHoldCorrCountry);
        tvThirdHoldPermAddProof =
            (TextView) dialogReview.findViewById(R.id.tvThirdHoldPermAddProof);
        tvThirdHoldPermBldgName =
            (TextView) dialogReview.findViewById(R.id.tvThirdHoldPermBldgName);
        tvThirdHoldPermFlatNo = (TextView) dialogReview.findViewById(R.id.tvThirdHoldPermFlatNo);
        tvThirdHoldPermArea = (TextView) dialogReview.findViewById(R.id.tvThirdHoldPermArea);
        tvThirdHoldPermPincode = (TextView) dialogReview.findViewById(R.id.tvThirdHoldPermPincode);
        tvThirdHoldPermState = (TextView) dialogReview.findViewById(R.id.tvThirdHoldPermState);
        tvThirdHoldPermCity = (TextView) dialogReview.findViewById(R.id.tvThirdHoldPermCity);
        tvThirdHoldPermCountry = (TextView) dialogReview.findViewById(R.id.tvThirdHoldPermCountry);
        tvThirdHoldAnnualIncome =
            (TextView) dialogReview.findViewById(R.id.tvThirdHoldAnnualIncome);
        tvThirdHoldOccDetail = (TextView) dialogReview.findViewById(R.id.tvThirdHoldOccDetail);
        tvThirdHoldPoliticalExposedPerson =
            (TextView) dialogReview.findViewById(R.id.tvThirdHoldPoliticalExposedPerson);
        tvThirdHoldRelatedPoliticalExposedPerson =
            (TextView) dialogReview.findViewById(R.id.tvThirdHoldRelatedPoliticalExposedPerson);

        llSecondHolderLayout = (LinearLayout) dialogReview.findViewById(R.id.llSecondHolderLayout);
        llThirdHolderLayout = (LinearLayout) dialogReview.findViewById(R.id.llThirdHolderLayout);

        if (getAppType().equals("DIRECT") && !getIsAadharVerified() && !getInterestedInMF()) {
            btnNext.setText("Fund Transfer");
        } else if (getInterestedInMF()) {
            btnNext.setText("Next");
        } else {
            btnNext.setText("Fund Transfer");
        }

        if (isNomineeTabApplicable()) {
            llNomineeDematMain.setVisibility(View.VISIBLE);
        } else {
            llNomineeDematMain.setVisibility(View.GONE);
        }

        ImageView btnClose = (ImageView) dialogReview.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReview.dismiss();
                reviewDialogIsShowing = false;
                if (llEquityBankDetail.getVisibility() != View.VISIBLE ||
                    llEquityBankScheme.getVisibility() != View.VISIBLE ||
                    llCommodityBankDetail.getVisibility() != View.VISIBLE ||
                    llCommodityBankScheme.getVisibility() != View.VISIBLE ||
                    llMFBankDetail.getVisibility() != View.VISIBLE) {
                    if ((isEquityOriginal() && isCommodityOriginal()) || isAllOriginal()) {
                        showBothBankDetails();
                        getSchemeDetails(getAppType(), userID);
                    } else if (isEquityOriginal() || isDerivativeOriginal()) {
                        showEquityBankDetails();
                        getSchemeDetails(getAppType(), userID);
                    } else if (isCommodityOriginal()) {
                        showCommdityBankDetails();
                        getSchemeDetails(getAppType(), userID);
                    } else if (getInterestedInMF()) {
                        showMFBankDetails();
                    }
                }

            }
        });

        /*tvMFReviewBankName.setText(tvMFBankName.getText().toString());
        tvMFReviewBranchName.setText(tvMFBranchName.getText().toString());
        tvMFReviewMICR.setText(tvMFMICR.getText().toString());
        tvMFReviewIFSC.setText(tvMFIFSC.getText().toString());
        tvMFReviewBankAddress.setText(tvMFBankAddress.getText().toString());
        tvMFReviewAccNumber.setText(etMFAccNumber.getText().toString());
        tvMFReviewAccType.setText(selectedBankAccType);*/
        //  lvEqSchemeDetailReview = (ListView) dialogReview.findViewById(R.id.lvEqSchemeDetailReview);
        //  lvCommSchemeDetailReview = (ListView) dialogReview.findViewById(R.id.lvCommSchemeDetailReview);

        cbTC = (CheckBox) dialogReview.findViewById(R.id.cbTC);
        tvTC = (TextView) dialogReview.findViewById(R.id.tvTC);
        tvTC.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        //  EkycMainActivity.customProgress = (LinearLayout) dialogReview.findViewById(R.id.customProgress);

        cbTC.setChecked(true);
        cbTC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbTC.setChecked(true);
                } else {
                    cbTC.setChecked(false);
                }
            }
        });
        tvTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  final Dialog termConditionDialog = new Dialog(mContext);
                termConditionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                termConditionDialog.setContentView(R.layout.frag_term_and_condition);
                termConditionDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                termConditionDialog.setCanceledOnTouchOutside(false);
                Button btnClose = (Button) termConditionDialog.findViewById(R.id.btnClose);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        termConditionDialog.dismiss();
                    }
                });
                termConditionDialog.show();*/
                termsConditionDialog(BROKING);

            }
        });
        tvMFTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termsConditionDialog(MF);
            }
        });
        tvEditPersonalDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGotoReviewEdit(true);
                dialogReview.dismiss();
                reviewDialogIsShowing = false;
                viewPager.setCurrentItem(getPersonalTab());
            }
        });

        tvEditNomineeJointDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGotoReviewEdit(true);
                dialogReview.dismiss();
                reviewDialogIsShowing = false;
                viewPager.setCurrentItem(getDematTab());
            }
        });

        tvEditBankDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGotoReviewEdit(true);
                dialogReview.dismiss();
                backToBank();
                reviewDialogIsShowing = false;
                viewPager.setCurrentItem(getBankTab());
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (isInterestedInMF) {
                    updateReviewDetail();
                } else */
                if (cbTC.isChecked()) {
                    updateReviewDetail();
                } else {
                    tcNotAcceptedDialog();

                }

            }
        });

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
            //            inputMap.put("userid", "1593361");
            //            inputMap.put(APPLICATION_TYPE, PP);
            //            inputMap.put("userid", userID);
            inputMap.put("userid", getUserID());
            if (getAppType().equalsIgnoreCase(PP)) {
                inputMap.put(APPLICATION_TYPE, PP);
            } else if (getAppType().equalsIgnoreCase(DIRECT)) {
                inputMap.put(APPLICATION_TYPE, DIRECT);
            } else if (getAppType().equalsIgnoreCase(CRM)) {
                inputMap.put(APPLICATION_TYPE, CRM);
            }
            String flag = "";
            String category = "";

            /*flag = GETREVIEWDETAILS;
            category = REVIEWDETAILS;*/
            if (getInterestedInMF()) {
                flag = GETREVIEWDETAILS_MF;
                category = BANKDETAILS;
            } else {
                flag = GETREVIEWDETAILS;
                category = REVIEWDETAILS;
            }

            llCustomProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (response != null) {
                            try {
                                if (getInterestedInMF()) {

                                    llEqBankDetail.setVisibility(View.GONE);
                                    llCommBankDetail.setVisibility(View.GONE);
                                    llSchemeDetail.setVisibility(View.GONE);
                                    llContractNo.setVisibility(View.GONE);
                                    //llReviewTC.setVisibility(View.GONE);
                                    llMFBankDetail.setVisibility(View.VISIBLE);
                                    if (response.has("REVIEWDETAILS")) {
                                        JSONArray jsonArray =
                                            response.getJSONArray("REVIEWDETAILS");
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);

                                                String residentialStatus =
                                                    (object.isNull("RESIDENTIAL_STATUS")
                                                        || object.getString("RESIDENTIAL_STATUS")
                                                        .equals("")) ? ""
                                                        : object.getString("RESIDENTIAL_STATUS");
                                                String residentialSubStatus =
                                                    (object.isNull("RESIDENTIAL_SUB_STATUS")
                                                        || object.getString(
                                                        "RESIDENTIAL_SUB_STATUS").equals("")) ? ""
                                                        : object.getString(
                                                            "RESIDENTIAL_SUB_STATUS");
                                                String title =
                                                    (object.isNull("TITLE") || object.getString(
                                                        "TITLE").equals("")) ? ""
                                                        : object.getString("TITLE");
                                                String fName =
                                                    (object.isNull("FNAME") || object.getString(
                                                        "FNAME").equals("")) ? ""
                                                        : object.getString("FNAME");
                                                String mName =
                                                    (object.isNull("MNAME") || object.getString(
                                                        "MNAME").equals("")) ? ""
                                                        : object.getString("MNAME");
                                                String lName =
                                                    (object.isNull("LNAME") || object.getString(
                                                        "LNAME").equals("")) ? ""
                                                        : object.getString("LNAME");
                                                String gender =
                                                    (object.isNull("GENDER") || object.getString(
                                                        "GENDER").equals("")) ? ""
                                                        : object.getString("GENDER");
                                                String relation =
                                                    (object.isNull("RELATION") || object.getString(
                                                        "RELATION").equals("")) ? ""
                                                        : object.getString("RELATION");
                                                String maritalStatus =
                                                    (object.isNull("MARITAL_STATUS")
                                                        || object.getString("MARITAL_STATUS")
                                                        .equals("")) ? ""
                                                        : object.getString("MARITAL_STATUS");
                                                String dateOfBirth = (object.isNull("DATE_OF_BIRTH")
                                                    || object.getString("DATE_OF_BIRTH").equals(""))
                                                    ? "" : object.getString("DATE_OF_BIRTH");
                                                String fhTitleName =
                                                    (object.isNull("FHTITLE") || object.getString(
                                                        "FHTITLE").equals("")) ? ""
                                                        : object.getString("FHTITLE");
                                                String fhfName =
                                                    (object.isNull("FHFNAME") || object.getString(
                                                        "FHFNAME").equals("")) ? ""
                                                        : object.getString("FHFNAME");
                                                String fhmName =
                                                    (object.isNull("FHMNAME") || object.getString(
                                                        "FHMNAME").equals("")) ? ""
                                                        : object.getString("FHMNAME");
                                                String fhlName =
                                                    (object.isNull("FHLNAME") || object.getString(
                                                        "FHLNAME").equals("")) ? ""
                                                        : object.getString("FHLNAME");
                                                String motherTitle = (object.isNull("MOTHER_TITLE")
                                                    || object.getString("MOTHER_TITLE").equals(""))
                                                    ? "" : object.getString("MOTHER_TITLE");
                                                String motherFName = (object.isNull("MOTHER_FNAME")
                                                    || object.getString("MOTHER_FNAME").equals(""))
                                                    ? "" : object.getString("MOTHER_FNAME");
                                                String motherMName = (object.isNull("MOTHER_MNAME")
                                                    || object.getString("MOTHER_MNAME").equals(""))
                                                    ? "" : object.getString("MOTHER_MNAME");
                                                String motherLName = (object.isNull("MOTHER_LNAME")
                                                    || object.getString("MOTHER_LNAME").equals(""))
                                                    ? "" : object.getString("MOTHER_LNAME");
                                                String maidenTitle = (object.isNull("MAIDEN_TITLE")
                                                    || object.getString("MAIDEN_TITLE").equals(""))
                                                    ? "" : object.getString("MAIDEN_TITLE");
                                                String maidenFName = (object.isNull("MAIDEN_FNAME")
                                                    || object.getString("MAIDEN_FNAME").equals(""))
                                                    ? "" : object.getString("MAIDEN_FNAME");
                                                String maidenMName = (object.isNull("MAIDEN_MNAME")
                                                    || object.getString("MAIDEN_MNAME").equals(""))
                                                    ? "" : object.getString("MAIDEN_MNAME");
                                                String maidenLName = (object.isNull("MAIDEN_LNAME")
                                                    || object.getString("MAIDEN_LNAME").equals(""))
                                                    ? "" : object.getString("MAIDEN_LNAME");
                                                String panNo =
                                                    (object.isNull("PANNO") || object.getString(
                                                        "PANNO").equals("")) ? ""
                                                        : object.getString("PANNO");
                                                String uid =
                                                    (object.isNull("UID") || object.getString("UID")
                                                        .equals("")) ? "" : object.getString("UID");
                                                String namePan =
                                                    (object.isNull("NAME_PAN") || object.getString(
                                                        "NAME_PAN").equals("")) ? ""
                                                        : object.getString("NAME_PAN");
                                                String corresAdd1 = (object.isNull("CORRES_ADD1")
                                                    || object.getString("CORRES_ADD1").equals(""))
                                                    ? "" : object.getString("CORRES_ADD1");
                                                String corresAdd2 = (object.isNull("CORRES_ADD2")
                                                    || object.getString("CORRES_ADD2").equals(""))
                                                    ? "" : object.getString("CORRES_ADD2");
                                                String corresAdd3 = (object.isNull("CORRES_ADD3")
                                                    || object.getString("CORRES_ADD3").equals(""))
                                                    ? "" : object.getString("CORRES_ADD3");
                                                String corresLandmark =
                                                    (object.isNull("CORRES_LANDMARK")
                                                        || object.getString("CORRES_LANDMARK")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_LANDMARK");
                                                String corresCity = (object.isNull("CORRES_CITY")
                                                    || object.getString("CORRES_CITY").equals(""))
                                                    ? "" : object.getString("CORRES_CITY");
                                                String corresPinCode =
                                                    (object.isNull("CORRES_PIN_CODE")
                                                        || object.getString("CORRES_PIN_CODE")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_PIN_CODE");
                                                String corresState = (object.isNull("CORRES_STATE")
                                                    || object.getString("CORRES_STATE").equals(""))
                                                    ? "" : object.getString("CORRES_STATE");
                                                String corresCountry =
                                                    (object.isNull("CORRES_COUNTRY")
                                                        || object.getString("CORRES_COUNTRY")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_COUNTRY");
                                                String corresTelOffice =
                                                    (object.isNull("CORRES_TEL_OFFICE")
                                                        || object.getString("CORRES_TEL_OFFICE")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_TEL_OFFICE");
                                                String corresTelResident =
                                                    (object.isNull("CORRES_TEL_RESIDENT")
                                                        || object.getString("CORRES_TEL_RESIDENT")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_TEL_RESIDENT");
                                                String corresMobileNo =
                                                    (object.isNull("CORRES_MOBILE_NO")
                                                        || object.getString("CORRES_MOBILE_NO")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_MOBILE_NO");
                                                String corresEmailId =
                                                    (object.isNull("CORRES_EMAIL_ID")
                                                        || object.getString("CORRES_EMAIL_ID")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_EMAIL_ID");
                                                String corresAddProof =
                                                    (object.isNull("CORRES_ADD_PROOF")
                                                        || object.getString("CORRES_ADD_PROOF")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_ADD_PROOF");
                                                String permanentAdd1 =
                                                    (object.isNull("PERMANENT_ADD1")
                                                        || object.getString("PERMANENT_ADD1")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_ADD1");
                                                String permanentAdd2 =
                                                    (object.isNull("PERMANENT_ADD2")
                                                        || object.getString("PERMANENT_ADD2")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_ADD2");
                                                String permanentAdd3 =
                                                    (object.isNull("PERMANENT_ADD3")
                                                        || object.getString("PERMANENT_ADD3")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_ADD3");
                                                String permanentLandmark =
                                                    (object.isNull("PERMANENT_LANDMARK")
                                                        || object.getString("PERMANENT_LANDMARK")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_LANDMARK");
                                                String permanentPinCode =
                                                    (object.isNull("PERMANENT_PIN_CODE")
                                                        || object.getString("PERMANENT_PIN_CODE")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_PIN_CODE");
                                                String permanentCity =
                                                    (object.isNull("PERMANENT_CITY")
                                                        || object.getString("PERMANENT_CITY")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_CITY");
                                                String permanentState =
                                                    (object.isNull("PERMANENT_STATE")
                                                        || object.getString("PERMANENT_STATE")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_STATE");
                                                String permanentCountry =
                                                    (object.isNull("PERMANENT_COUNTRY")
                                                        || object.getString("PERMANENT_COUNTRY")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_COUNTRY");
                                                String permanentTelResident =
                                                    (object.isNull("PERMANENT_TEL_RESIDENT")
                                                        || object.getString(
                                                        "PERMANENT_TEL_RESIDENT").equals("")) ? ""
                                                        : object.getString(
                                                            "PERMANENT_TEL_RESIDENT");
                                                String otherCityCorres =
                                                    (object.isNull("OTHER_CITY_CORRES")
                                                        || object.getString("OTHER_CITY_CORRES")
                                                        .equals("")) ? ""
                                                        : object.getString("OTHER_CITY_CORRES");
                                                String otherCityPermanent =
                                                    (object.isNull("OTHER_CITY_PERMANENT") || object
                                                        .getString("OTHER_CITY_PERMANENT")
                                                        .equals("")) ? ""
                                                        : object.getString("OTHER_CITY_PERMANENT");
                                                String permanentAddProof =
                                                    (object.isNull("PERMANENT_ADD_PROOF")
                                                        || object.getString("PERMANENT_ADD_PROOF")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_ADD_PROOF");
                                                String occupationDetails =
                                                    (object.isNull("OCCUPATION_DETAILS")
                                                        || object.getString("OCCUPATION_DETAILS")
                                                        .equals("")) ? ""
                                                        : object.getString("OCCUPATION_DETAILS");
                                                String educationQualification =
                                                    (object.isNull("EDUCATION_QUALIFICATION")
                                                        || object.getString(
                                                        "EDUCATION_QUALIFICATION").equals("")) ? ""
                                                        : object.getString(
                                                            "EDUCATION_QUALIFICATION");
                                                String businessEmpName =
                                                    (object.isNull("BUSINESS_EMP_NAME")
                                                        || object.getString("BUSINESS_EMP_NAME")
                                                        .equals("")) ? ""
                                                        : object.getString("BUSINESS_EMP_NAME");
                                                String businessTel = (object.isNull("BUSINESS_TEL")
                                                    || object.getString("BUSINESS_TEL").equals(""))
                                                    ? "" : object.getString("BUSINESS_TEL");
                                                String businessMobile =
                                                    (object.isNull("BUSINESS_MOBILE")
                                                        || object.getString("BUSINESS_MOBILE")
                                                        .equals("")) ? ""
                                                        : object.getString("BUSINESS_MOBILE");
                                                String businessFax = (object.isNull("BUSINESS_FAX")
                                                    || object.getString("BUSINESS_FAX").equals(""))
                                                    ? "" : object.getString("BUSINESS_FAX");
                                                String businessEmailId =
                                                    (object.isNull("BUSINESS_EMAIL_ID")
                                                        || object.getString("BUSINESS_EMAIL_ID")
                                                        .equals("")) ? ""
                                                        : object.getString("BUSINESS_EMAIL_ID");
                                                String annualIncome =
                                                    (object.isNull("ANNUAL_INCOME")
                                                        || object.getString("ANNUAL_INCOME")
                                                        .equals("")) ? ""
                                                        : object.getString("ANNUAL_INCOME");
                                                String stockPortfolio =
                                                    (object.isNull("STOCK_PORTFOLIO")
                                                        || object.getString("STOCK_PORTFOLIO")
                                                        .equals("")) ? ""
                                                        : object.getString("STOCK_PORTFOLIO");
                                                String brokerName = (object.isNull("BROKER_NAME")
                                                    || object.getString("BROKER_NAME").equals(""))
                                                    ? "" : object.getString("BROKER_NAME");
                                                String brokerClientCode =
                                                    (object.isNull("BROKER_CLIENT_CODE")
                                                        || object.getString("BROKER_CLIENT_CODE")
                                                        .equals("")) ? ""
                                                        : object.getString("BROKER_CLIENT_CODE");
                                                String actionBySebi =
                                                    (object.isNull("ACTION_BY_SEBI")
                                                        || object.getString("ACTION_BY_SEBI")
                                                        .equals("")) ? ""
                                                        : object.getString("ACTION_BY_SEBI");
                                                String natureBusinessEmp =
                                                    (object.isNull("NATURE_BUSINESS_EMP")
                                                        || object.getString("NATURE_BUSINESS_EMP")
                                                        .equals("")) ? ""
                                                        : object.getString("NATURE_BUSINESS_EMP");
                                                String politicallyExposedPerson =
                                                    (object.isNull("POLITICALLY_EXPOSED_PERSON")
                                                        || object.getString(
                                                        "POLITICALLY_EXPOSED_PERSON").equals(""))
                                                        ? "" : object.getString(
                                                        "POLITICALLY_EXPOSED_PERSON");
                                                String contractNote =
                                                    (object.isNull("CONTRACT_NOTE")
                                                        || object.getString("CONTRACT_NOTE")
                                                        .equals("")) ? ""
                                                        : object.getString("CONTRACT_NOTE");
                                                String qualificationProfessional =
                                                    (object.isNull("QUALIFICATION_PROFESSIONAL")
                                                        || object.getString(
                                                        "QUALIFICATION_PROFESSIONAL").equals(""))
                                                        ? "" : object.getString(
                                                        "QUALIFICATION_PROFESSIONAL");
                                                String otherOccupation =
                                                    (object.isNull("OTHER_OCCUPATION")
                                                        || object.getString("OTHER_OCCUPATION")
                                                        .equals("")) ? ""
                                                        : object.getString("OTHER_OCCUPATION");
                                                String relatedPoliticallyExposedPerson =
                                                    (object.isNull(
                                                        "RELATED_POLITICALLY_EXPOSED_PERSON")
                                                        || object.getString(
                                                        "RELATED_POLITICALLY_EXPOSED_PERSON")
                                                        .equals("")) ? "" : object.getString(
                                                        "RELATED_POLITICALLY_EXPOSED_PERSON");
                                                String facebookId = (object.isNull("FACEBOOKID")
                                                    || object.getString("FACEBOOKID").equals(""))
                                                    ? "" : object.getString("FACEBOOKID");
                                                String twitterId =
                                                    (object.isNull("TWITTERID") || object.getString(
                                                        "TWITTERID").equals("")) ? ""
                                                        : object.getString("TWITTERID");
                                                String linkedinId = (object.isNull("LINKEDINID")
                                                    || object.getString("LINKEDINID").equals(""))
                                                    ? "" : object.getString("LINKEDINID");
                                                String networth =
                                                    (object.isNull("NETWORTH") || object.getString(
                                                        "NETWORTH").equals("")) ? ""
                                                        : object.getString("NETWORTH");
                                                String asOnDate =
                                                    (object.isNull("ASONDATE") || object.getString(
                                                        "ASONDATE").equals("")) ? ""
                                                        : object.getString("ASONDATE");

                                                //                                            tvResidentStatus.setText(residentialStatus);
                                                //                                            tvSubStatus.setText(residentialSubStatus);
                                                tvName.setText(title
                                                    + " "
                                                    + fName
                                                    + " "
                                                    + mName
                                                    + " "
                                                    + lName);
                                                tvGender.setText(gender);
                                                tvRelation.setText(relation);
                                                tvFatherSpouseName.setText(fhTitleName
                                                    + " "
                                                    + fhfName
                                                    + " "
                                                    + fhmName
                                                    + " "
                                                    + fhlName);
                                                tvMotherName.setText(motherTitle
                                                    + " "
                                                    + motherFName
                                                    + " "
                                                    + motherMName
                                                    + " "
                                                    + motherLName);
                                                if (isBlank(maidenFName)) {
                                                    tvMaidenName.setText("");
                                                } else {
                                                    tvMaidenName.setText(maidenTitle
                                                        + " "
                                                        + maidenFName
                                                        + " "
                                                        + maidenMName
                                                        + " "
                                                        + maidenLName);
                                                }
                                                tvDOB.setText(dateOfBirth);
                                                tvPAN.setText(panNo);
                                                tvNameOnPAN.setText(namePan);
                                                if (uid.equalsIgnoreCase("")
                                                    || uid.equals("0")
                                                    || uid.equals("0.0")) {
                                                    tvAadhaarNumber.setText("");
                                                } else {
                                                    //                                                BigDecimal bd = new BigDecimal(uid);
                                                    //                                                tvAadhaarNumber.setText(String.valueOf(bd.longValue()));
                                                    tvAadhaarNumber.setText(uid);
                                                }
                                                tvMaritalStatus.setText(maritalStatus);
                                                //Correspondence Address
                                                tvFlatNoCorres.setText(corresAdd2);
                                                tvBldgNoCorres.setText(corresAdd1);
                                                tvAreaCorres.setText(corresAdd3);
                                                tvCityCorres.setText(corresCity);
                                                tvStateCorres.setText(corresState);
                                                tvPincodeCorres.setText(corresPinCode);
                                                tvCountryCorres.setText(corresCountry);
                                                tvAddProofCorres.setText(corresAddProof);
                                                //Permanent Address
                                                tvFlatNoPerm.setText(permanentAdd2);
                                                tvBldgNoPerm.setText(permanentAdd1);
                                                tvAreaPerm.setText(permanentAdd3);
                                                tvCityPerm.setText(permanentCity);
                                                tvStatePerm.setText(permanentState);
                                                tvPincodePerm.setText(permanentPinCode);
                                                tvCountryPerm.setText(permanentCountry);
                                                tvAddProofPerm.setText(permanentAddProof);
                                                //Contact Info
                                                tvMobile.setText(corresMobileNo);
                                                tvEmail.setText(corresEmailId);
                                                //                                            tvTelNo.setText(corresTelResident);
                                                //                                            tvFbId.setText(facebookId);
                                                //                                            tvTwitterId.setText(twitterId);
                                                //                                            tvLinkedinID.setText(linkedinId);
                                                //Work Detail
                                                tvOccupation.setText(occupationDetails);
                                                tvQualification.setText(educationQualification);
                                                tvIncome.setText(annualIncome);
                                                tvContract.setText(contractNote);
                                                //                                            tvStockPortfolio.setText(stockPortfolio);
                                                if (networth.equalsIgnoreCase("")
                                                    || networth.equalsIgnoreCase("0")
                                                    || networth.equalsIgnoreCase("0.0")) {
                                                    tvNetWorth.setText("");
                                                } else {
                                                    BigDecimal bd = new BigDecimal(networth);
                                                    tvNetWorth.setText(
                                                        String.valueOf(bd.longValue()));
                                                }
                                                tvNWDate.setText(asOnDate);
                                                tvPoliticalyExposed.setText(
                                                    politicallyExposedPerson);
                                                tvRelatedPolitical.setText(
                                                    relatedPoliticallyExposedPerson);
                                                //                                            tvBrokerName.setText(brokerName);
                                                //                                            tvClientCode.setText(brokerClientCode);
                                                //                                            tvSebiAuthority.setText(actionBySebi);
                                            }
                                        }
                                    }
                                    if (response.has("REVIEWDETAILS2")) {
                                        JSONArray jsonArray =
                                            response.getJSONArray("REVIEWDETAILS2");
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                String countryOfCitizen =
                                                    (object.isNull("Answer_desc")
                                                        || object.getString("Answer_desc")
                                                        .equals("")) ? ""
                                                        : object.getString("Answer_desc");
                                                String ansFlag = (object.isNull("Answer_Flag")
                                                    || object.getString("Answer_Flag").equals(""))
                                                    ? "" : object.getString("Answer_Flag");
                                                if (ansFlag.equals("1")) {
                                                    tvCountryCitizenshipQuestion.setText(
                                                        getResources().getString(
                                                            R.string.other_country_citizen)
                                                            + " :  Yes");
                                                    llCountryCitizenship.setVisibility(
                                                        View.VISIBLE);
                                                    tvCounrtyOfCitizen.setText(countryOfCitizen);
                                                } else {
                                                    tvCountryCitizenshipQuestion.setText(
                                                        getResources().getString(
                                                            R.string.other_country_citizen)
                                                            + " :  No");
                                                    llCountryCitizenship.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    }
                                    if (response.has("REVIEWDETAILS3")) {
                                        JSONArray jsonArray =
                                            response.getJSONArray("REVIEWDETAILS3");
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                String subQuesId = (object.isNull("SubQues_Id")
                                                    || object.getString("SubQues_Id").equals(""))
                                                    ? "" : object.getString("SubQues_Id");
                                                String ansDesc = (object.isNull("Answer_desc")
                                                    || object.getString("Answer_desc").equals(""))
                                                    ? "" : object.getString("Answer_desc");
                                                String ansFlag = (object.isNull("Answer_Flag")
                                                    || object.getString("Answer_Flag").equals(""))
                                                    ? "" : object.getString("Answer_Flag");
                                                if (ansFlag.equals("1")) {
                                                    tvCountryBirthQuestion.setText(
                                                        getResources().getString(
                                                            R.string.country_birth) + " :  Yes");
                                                    llCountryBirth.setVisibility(View.VISIBLE);
                                                    if (subQuesId.equals(
                                                        "2")) { //2 for country of birth
                                                        tvCountryOfBirth.setText(ansDesc);
                                                    }
                                                    if (subQuesId.equals(
                                                        "3")) { //3 for Place of Birth
                                                        tvPlaceOfBirth.setText(ansDesc);
                                                    }
                                                } else {
                                                    tvCountryBirthQuestion.setText(
                                                        getResources().getString(
                                                            R.string.country_birth) + " :  No");
                                                    llCountryBirth.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    }
                                    if (response.has("REVIEWDETAILS4")) {
                                        JSONArray jsonArray =
                                            response.getJSONArray("REVIEWDETAILS4");
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                String subQuesId = (object.isNull("SubQues_Id")
                                                    || object.getString("SubQues_Id").equals(""))
                                                    ? "" : object.getString("SubQues_Id");
                                                String ansDesc = (object.isNull("Answer_desc")
                                                    || object.getString("Answer_desc").equals(""))
                                                    ? "" : object.getString("Answer_desc");
                                                String ansFlag = (object.isNull("Answer_Flag")
                                                    || object.getString("Answer_Flag").equals(""))
                                                    ? "" : object.getString("Answer_Flag");
                                                if (ansFlag.equals("1")) {
                                                    tvCountryTaxQuestion.setText(
                                                        getResources().getString(
                                                            R.string.other_country_tax)
                                                            + " :  Yes");
                                                    llCountryTax.setVisibility(View.VISIBLE);
                                                    if (subQuesId.equals(
                                                        "4")) { //4 for Country of tax residency
                                                        tvCountryOfTaXRes.setText(ansDesc);
                                                    }
                                                    if (subQuesId.equals(
                                                        "5")) { //5 for Tax identification num
                                                        tvTaxIDNum.setText(ansDesc);
                                                    }
                                                } else {
                                                    tvCountryTaxQuestion.setText(
                                                        getResources().getString(
                                                            R.string.other_country_tax) + " :  No");
                                                    llCountryTax.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    }
                                    if (response.has("REVIEWDETAILS5")) {
                                        JSONArray jsonArray =
                                            response.getJSONArray("REVIEWDETAILS5");
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                String ansDesc = (object.isNull("Answer_desc")
                                                    || object.getString("Answer_desc").equals(""))
                                                    ? "" : object.getString("Answer_desc");
                                                String ansFlag = (object.isNull("Answer_Flag")
                                                    || object.getString("Answer_Flag").equals(""))
                                                    ? "" : object.getString("Answer_Flag");
                                                if (ansFlag.equals("1")) {
                                                    tvPoaAddressQuestion.setText(
                                                        getResources().getString(
                                                            R.string.poa_address) + " :  Yes");
                                                } else {
                                                    tvPoaAddressQuestion.setText(
                                                        getResources().getString(
                                                            R.string.poa_address) + " :  No");
                                                }
                                            }
                                        }
                                    }
                                    if (response.has("REVIEWDETAILS6")) {
                                        JSONArray jsonArray =
                                            response.getJSONArray("REVIEWDETAILS6");
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                String ansDesc = (object.isNull("Answer_desc")
                                                    || object.getString("Answer_desc").equals(""))
                                                    ? "" : object.getString("Answer_desc");
                                                String ansFlag = (object.isNull("Answer_Flag")
                                                    || object.getString("Answer_Flag").equals(""))
                                                    ? "" : object.getString("Answer_Flag");
                                                if (ansFlag.equals("1")) {
                                                    tvAddressOutsideIndiaQuestion.setText(
                                                        getResources().getString(
                                                            R.string.address_outside_india)
                                                            + " :  Yes");
                                                } else {
                                                    tvAddressOutsideIndiaQuestion.setText(
                                                        getResources().getString(
                                                            R.string.address_outside_india)
                                                            + " :  No");
                                                }
                                            }
                                        }
                                    }
                                    if (response.has("REVIEWDETAILS7")) {
                                        //Bank Detail
                                        JSONArray jsonArray =
                                            response.getJSONArray("REVIEWDETAILS7");
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);

                                            /*"CBD_ID":1045,
                                                    "BANK_NAME":"A P MAHAJANS CO-OPERATIVE URBAN BANK LTD",
                                                    "BANK_BRANCH":"GHASMANDI MAIN ROAD, SECUNDERABAD",
                                                    "BANK_ADD":"GHASMANDI MAIN ROAD,,,SECUNDERABAD",
                                                    "BANK_ACC_NO":"gdydy",
                                                    "BANK_AC_TYPE":"Saving",
                                                    "BANK_MICR_CODE":"500015251",
                                                    "BANK_IFSC_CODE":"null",
                                                    "BANK_PROOF":"",
                                                    "PHOTO_PROOF":"",
                                                    "FIRST_RELATIVE_NAME":"",
                                                    "FIRST_RELATIONSHIP":"",
                                                    "FIRST_UCC":"",
                                                    "SECOND_RELATIVE_NAME":"",
                                                    "SECOND_RELATIONSHIP":"",
                                                    "SECOND_UCC":"",
                                                    "CREATED_BY":1632850,
                                                    "CREATED_DATE":"2016-12-22T10:21:44.013",
                                                    "MODIFIED_BY":1632850,
                                                    "MODIFIED_DATE":"2016-12-22T10:21:44.013",
                                                    "DEFAULT_FLG":"1",
                                                    "CHECK_FLG":"checked"*/

                                                String bankName =
                                                    (object.isNull("BANK_NAME") || object.getString(
                                                        "BANK_NAME").equals("")) ? ""
                                                        : object.getString("BANK_NAME");
                                                String branchName = (object.isNull("BANK_BRANCH")
                                                    || object.getString("BANK_BRANCH").equals(""))
                                                    ? "" : object.getString("BANK_BRANCH");
                                                String bankAdd =
                                                    (object.isNull("BANK_ADD") || object.getString(
                                                        "BANK_ADD").equals("")) ? ""
                                                        : object.getString("BANK_ADD");
                                                String accNum = (object.isNull("BANK_ACC_NO")
                                                    || object.getString("BANK_ACC_NO").equals(""))
                                                    ? "" : object.getString("BANK_ACC_NO");
                                                String abnkAccType = (object.isNull("BANK_AC_TYPE")
                                                    || object.getString("BANK_AC_TYPE").equals(""))
                                                    ? "" : object.getString("BANK_AC_TYPE");
                                                String micr = (object.isNull("BANK_MICR_CODE")
                                                    || object.getString("BANK_MICR_CODE")
                                                    .equals("")) ? ""
                                                    : object.getString("BANK_MICR_CODE");
                                                String ifsc = (object.isNull("BANK_IFSC_CODE")
                                                    || object.getString("BANK_IFSC_CODE")
                                                    .equals("")) ? ""
                                                    : object.getString("BANK_IFSC_CODE");
                                                String defaultFlag = (object.isNull("DEFAULT_FLG")
                                                    || object.getString("DEFAULT_FLG").equals(""))
                                                    ? "" : object.getString("DEFAULT_FLG");
                                                String checkFlag =
                                                    (object.isNull("BANK_NAME") || object.getString(
                                                        "BANK_NAME").equals("")) ? ""
                                                        : object.getString("BANK_NAME");

                                                if (Integer.valueOf(defaultFlag) == 1) {
                                                    tvMFReviewBankName.setText(bankName);
                                                    tvMFReviewBranchName.setText(branchName);
                                                    tvMFReviewMICR.setText(micr);
                                                    tvMFReviewIFSC.setText(ifsc);
                                                    tvMFReviewBankAddress.setText(bankAdd);
                                                    tvMFReviewAccNumber.setText(accNum);
                                                    tvMFReviewAccType.setText(abnkAccType);

                                                }
                                            }
                                        }
                                    }
                                    if (response.has("REVIEWDETAILS8")) {
                                        JSONArray jsonArray =
                                            response.getJSONArray("REVIEWDETAILS8");
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                String nomineeFName =
                                                    (object.isNull(NOMINEE_FNAME_REVIEW)
                                                        || object.getString(NOMINEE_FNAME_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_FNAME_REVIEW);
                                                String nomineeMName =
                                                    (object.isNull(NOMINEE_MNAME_REVIEW)
                                                        || object.getString(NOMINEE_MNAME_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_MNAME_REVIEW);
                                                String nomineeLName =
                                                    (object.isNull(NOMINEE_LNAME_REVIEW)
                                                        || object.getString(NOMINEE_LNAME_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_LNAME_REVIEW);
                                                String nomineeAdd1 =
                                                    (object.isNull(NOMINEE_ADD1_REVIEW)
                                                        || object.getString(NOMINEE_ADD1_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_ADD1_REVIEW);
                                                String nomineeAdd2 =
                                                    (object.isNull(NOMINEE_ADD2_REVIEW)
                                                        || object.getString(NOMINEE_ADD2_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_ADD2_REVIEW);
                                                String nomineeAdd3 =
                                                    (object.isNull(NOMINEE_ADD3_REVIEW)
                                                        || object.getString(NOMINEE_ADD3_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_ADD3_REVIEW);
                                                String nomineeLandmark =
                                                    (object.isNull(NOMINEE_LANDMARK_REVIEW)
                                                        || object.getString(NOMINEE_LANDMARK_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_LANDMARK_REVIEW);
                                                String nomineeCity =
                                                    (object.isNull(NOMINEE_CITY_REVIEW)
                                                        || object.getString(NOMINEE_CITY_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_CITY_REVIEW);
                                                String nomineeState =
                                                    (object.isNull(NOMINEE_STATE_REVIEW)
                                                        || object.getString(NOMINEE_STATE_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_STATE_REVIEW);
                                                String nomineePin =
                                                    (object.isNull(NOMINEE_PIN_REVIEW)
                                                        || object.getString(NOMINEE_PIN_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_PIN_REVIEW);
                                                String nomineeCountry =
                                                    (object.isNull(NOMINEE_COUNTRY_REVIEW) || object
                                                        .getString(NOMINEE_COUNTRY_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_COUNTRY_REVIEW);
                                                String nomineeDOB =
                                                    (object.isNull(NOMINEE_DATE_OF_BIRTH_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_DATE_OF_BIRTH_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        NOMINEE_DATE_OF_BIRTH_REVIEW);
                                                String nomineeTele =
                                                    (object.isNull(NOMINEE_TELE_REVIEW)
                                                        || object.getString(NOMINEE_TELE_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_TELE_REVIEW);
                                                String nomineeRel =
                                                    (object.isNull(NOMINEE_REL_BO_REVIEW)
                                                        || object.getString(NOMINEE_REL_BO_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_REL_BO_REVIEW);
                                                String nomineeMinorFName =
                                                    (object.isNull(NOMINEE_MINOR_FNAME_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_FNAME_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_FNAME_REVIEW);
                                                String nomineeMinorMName =
                                                    (object.isNull(NOMINEE_MINOR_MNAME_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_MNAME_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_MNAME_REVIEW);
                                                String nomineeMinorLName =
                                                    (object.isNull(NOMINEE_MINOR_LNAME_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_LNAME_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_LNAME_REVIEW);
                                                String nomineeMinorAdd1 =
                                                    (object.isNull(NOMINEE_MINOR_ADD1_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_ADD1_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_ADD1_REVIEW);
                                                String nomineeMinorAdd2 =
                                                    (object.isNull(NOMINEE_MINOR_ADD2_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_ADD2_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_ADD2_REVIEW);
                                                String nomineeMinorAdd3 =
                                                    (object.isNull(NOMINEE_MINOR_ADD3_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_ADD3_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_ADD3_REVIEW);
                                                String nomineeMinorLandmark =
                                                    (object.isNull(NOMINEE_MINOR_LANDMARK_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_LANDMARK_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        NOMINEE_MINOR_LANDMARK_REVIEW);
                                                String nomineeMinorCity =
                                                    (object.isNull(NOMINEE_MINOR_CITY_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_CITY_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_CITY_REVIEW);
                                                String nomineeMinorState =
                                                    (object.isNull(NOMINEE_MINOR_STATE_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_STATE_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_STATE_REVIEW);
                                                String nomineeMinorPin =
                                                    (object.isNull(NOMINEE_MINOR_PIN_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_PIN_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_PIN_REVIEW);
                                                String nomineeMinorCountry =
                                                    (object.isNull(NOMINEE_MINOR_COUNTRY_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_COUNTRY_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        NOMINEE_MINOR_COUNTRY_REVIEW);
                                                String sameAsCorresNominee =
                                                    (object.isNull(SAME_AS_CORRES_NOMINEE_REVIEW)
                                                        || object.getString(
                                                        SAME_AS_CORRES_NOMINEE_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        SAME_AS_CORRES_NOMINEE_REVIEW);
                                                String sameAsCorresPerm =
                                                    (object.isNull(SAME_AS_CORRES_PERM_REVIEW)
                                                        || object.getString(
                                                        SAME_AS_CORRES_PERM_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            SAME_AS_CORRES_PERM_REVIEW);
                                                String sameAsCorresNomineeGuardian = (object.isNull(
                                                    SAME_AS_CORRES_NOMINEE_GUARDIAN_REVIEW)
                                                    || object.getString(
                                                    SAME_AS_CORRES_NOMINEE_GUARDIAN_REVIEW)
                                                    .equals("")) ? "" : object.getString(
                                                    SAME_AS_CORRES_NOMINEE_GUARDIAN_REVIEW);
                                                String otherCityNominee =
                                                    (object.isNull(OTHER_CITY_NOMINEE_REVIEW)
                                                        || object.getString(
                                                        OTHER_CITY_NOMINEE_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            OTHER_CITY_NOMINEE_REVIEW);
                                                String otherCityNomineeMinor =
                                                    (object.isNull(OTHER_CITY_NOMINEE_MINOR_REVIEW)
                                                        || object.getString(
                                                        OTHER_CITY_NOMINEE_MINOR_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        OTHER_CITY_NOMINEE_MINOR_REVIEW);
                                                String nomineeDeclaration =
                                                    (object.isNull(NOMINEE_DECLARATION_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_DECLARATION_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_DECLARATION_REVIEW);
                                                String nomineeGuardianAge =
                                                    (object.isNull(NOMINEE_GUARDIAN_AGE_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_GUARDIAN_AGE_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        NOMINEE_GUARDIAN_AGE_REVIEW);
                                                String nomineeGuardianRelation =
                                                    (object.isNull(NOMINEE_GUARDIAN_RELATION_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_GUARDIAN_RELATION_REVIEW)
                                                        .equals("")) ? "" : object.getString(
                                                        NOMINEE_GUARDIAN_RELATION_REVIEW);
                                                String nomineeApplicantRelation = (object.isNull(
                                                    NOMINEE_APPLICANT_RELATION_REVIEW)
                                                    || object.getString(
                                                    NOMINEE_APPLICANT_RELATION_REVIEW).equals(""))
                                                    ? "" : object.getString(
                                                    NOMINEE_APPLICANT_RELATION_REVIEW);
                                                String nomineeEmail =
                                                    (object.isNull(NOMINEE_EMAIL_ID_REVIEW)
                                                        || object.getString(NOMINEE_EMAIL_ID_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_EMAIL_ID_REVIEW);
                                                String nomineeMinorEmail =
                                                    (object.isNull(NOMINEE_MINOR_EMAIL_ID_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_EMAIL_ID_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        NOMINEE_MINOR_EMAIL_ID_REVIEW);
                                                String nomineeMinorTelno =
                                                    (object.isNull(NOMINEE_MINOR_TELNO_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_TELNO_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_TELNO_REVIEW);
                                                String nomineePAN =
                                                    (object.isNull(NOMINEE_PAN_REVIEW)
                                                        || object.getString(NOMINEE_PAN_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_PAN_REVIEW);
                                                String nomineeAadhar =
                                                    (object.isNull(NOMINEE_UID) || object.getString(
                                                        NOMINEE_UID).equals("")) ? ""
                                                        : object.getString(NOMINEE_UID);

                                                if (nomineeDeclaration.equalsIgnoreCase(NO)) {
                                                    tvNomineeDetailMsg.setVisibility(View.VISIBLE);
                                                    llNomineeDetail.setVisibility(View.GONE);
                                                } else if (nomineeDeclaration.equalsIgnoreCase(
                                                    YES)) {
                                                    llNomineeDetail.setVisibility(View.VISIBLE);
                                                    tvNomineeDetailMsg.setVisibility(View.GONE);
                                                }
                                                tvNomineeName.setText(nomineeFName
                                                    + " "
                                                    + nomineeMName
                                                    + " "
                                                    + nomineeLName);
                                                tvNomineeDOB.setText(nomineeDOB);
                                                tvNomineePAN.setText(nomineePAN);
                                                tvNomineeAadhaar.setText(nomineeAadhar);
                                                tvNomineeEmail.setText(nomineeEmail);
                                                tvNomineeTelNo.setText(nomineeTele);
                                                tvNomineeRelationship.setText(nomineeRel);
                                                //                                            tvNomineeApplicantRelationship.setText(nomineeApplicantRelation);
                                                tvNomineeFlatNoCorres.setText(nomineeAdd2);
                                                tvNomineeBldgNoCorres.setText(nomineeAdd1);
                                                tvNomineeAreaCorres.setText(nomineeAdd3);
                                                tvNomineeCityCorres.setText(nomineeCity);
                                                tvNomineeStateCorres.setText(nomineeState);
                                                tvNomineePincodeCorres.setText(nomineePin);
                                                tvNomineeCountryCorres.setText(nomineeCountry);

                                                Calendar minAdultAge = new GregorianCalendar();
                                                minAdultAge.add(Calendar.YEAR, -18);
                                                Date adultAge = EkycAppUtil.getDate(
                                                    dateFormatter.format(minAdultAge.getTime()),
                                                    "dd/MM/yyyy");
                                                Date dateDOB =
                                                    EkycAppUtil.getDate(nomineeDOB, "dd/MM/yyyy");
                                                if (!isBlank(nomineeDOB) && adultAge.before(
                                                    dateDOB)) {
                                                    llNomineeGuardianDetails.setVisibility(
                                                        View.VISIBLE);
                                                    tvNomineeGuardianName.setText(nomineeMinorFName
                                                        + " "
                                                        + nomineeMinorMName
                                                        + " "
                                                        + nomineeMinorLName);
                                                    tvNomineeGuardianEmail.setText(
                                                        nomineeMinorEmail);
                                                    tvNomineeGuardianTelNo.setText(
                                                        nomineeMinorTelno);
                                                    tvNomineeGuardianAge.setText(
                                                        nomineeGuardianAge);
                                                    tvNomineeGuardianFlatNoCorres.setText(
                                                        nomineeMinorAdd2);
                                                    tvNomineeGuardianBldgNoCorres.setText(
                                                        nomineeMinorAdd1);
                                                    tvNomineeGuardianAreaCorres.setText(
                                                        nomineeMinorAdd3);
                                                    tvNomineeGuardianCityCorres.setText(
                                                        nomineeMinorCity);
                                                    tvNomineeGuardianStateCorres.setText(
                                                        nomineeMinorState);
                                                    tvNomineeGuardianPincodeCorres.setText(
                                                        nomineeMinorPin);
                                                    tvNomineeGuardianCountryCorres.setText(
                                                        nomineeMinorCountry);
                                                    tvNomineeGuardianRelationship.setText(
                                                        nomineeGuardianRelation);
                                                } else {
                                                    llNomineeGuardianDetails.setVisibility(
                                                        View.GONE);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    llContractNo.setVisibility(View.VISIBLE);
                                    llMFBankDetail.setVisibility(View.GONE);
                                    if (response.has("ReviewDetails")) {
                                        JSONArray jsonArray =
                                            response.getJSONArray("ReviewDetails");
                                        if (jsonArray.length() != 0) {
                                            String product = "";
                                            arrayListProductReview =
                                                new ArrayList<HashMap<String, String>>();
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                map = new HashMap<String, String>();
                                                product = (object.isNull(PRODUCT_REVIEW)
                                                    || object.getString(PRODUCT_REVIEW).equals(""))
                                                    ? "" : object.getString(PRODUCT_REVIEW);
                                                map.put(PRODUCT_REVIEW, product);
                                                arrayListProductReview.add(map);
                                            }

                                            if (isEquityOriginal() && isCommodityOriginal()) {
                                                llEqBankDetail.setVisibility(View.VISIBLE);
                                                //                                            llCommBankDetail.setVisibility(View.VISIBLE);
                                                llCommBankDetail.setVisibility(View.GONE);
                                                llEqSchemeDetail.setVisibility(View.VISIBLE);
                                                llCommSchemeDetail.setVisibility(View.VISIBLE);
                                            } else {
                                                if (isEquityOriginal()) {
                                                    llEqBankDetail.setVisibility(View.VISIBLE);
                                                    llCommBankDetail.setVisibility(View.GONE);
                                                    llEqSchemeDetail.setVisibility(View.VISIBLE);
                                                    llCommSchemeDetail.setVisibility(View.GONE);
                                                    llMFBankDetail.setVisibility(View.GONE);
                                                } else if (isCommodityOriginal()) {
                                                    llCommBankDetail.setVisibility(View.VISIBLE);
                                                    llEqBankDetail.setVisibility(View.GONE);
                                                    llCommSchemeDetail.setVisibility(View.VISIBLE);
                                                    llEqSchemeDetail.setVisibility(View.GONE);
                                                    llMFBankDetail.setVisibility(View.GONE);
                                                }
                                            /*else if (product.equalsIgnoreCase(MF)) {
                                                llEqBankDetail.setVisibility(View.GONE);
                                                llCommBankDetail.setVisibility(View.GONE);
                                                llSchemeDetail.setVisibility(View.GONE);
                                                llReviewTC.setVisibility(View.GONE);
                                                llMFBankDetail.setVisibility(View.VISIBLE);


                                            }*/
                                            }
                                        }
                                    }

                                    if (response.has("ReviewDetails1")) {
                                        JSONArray jsonArray1 =
                                            response.getJSONArray("ReviewDetails1");
                                        if (jsonArray1.length() != 0) {
                                            for (int i = 0; i < jsonArray1.length(); i++) {
                                                JSONObject object = jsonArray1.getJSONObject(i);
                                    /*"TITLE": "Mr.",
                                            "GENDER": "Male",
                                            "FNAME": "Vinod",
                                            "MNAME": "Ramraj",
                                            "LNAME": "Yadav",
                                            "RELATION": "Father",
                                            "FHFNAME": "Ramraj",
                                            "FHMNAME": "Banshi",
                                            "FHLNAME": "Yadav",
                                            "DATE_OF_BIRTH": "14/08/1987",
                                            "MARITAL_STATUS": "Single",
                                            "PANNO": "BTMPP8324E",
                                            "UID": 885076278974,
                                            "Name_Pan": "AMRUTA AASHISH PANDE",
                                            "CORRES_ADD1": "Mhada Complex",
                                            "CORRES_ADD2": "Room no.10, Kaludin Chawl",
                                            "CORRES_ADD3": "Behram Baug, Veera Desai Road, MG Vidyalaya, Jogeshwari (West),",
                                            "CORRES_LANDMARK": "",
                                            "CORRES_CITY": "Mumbai",
                                            "CORRES_PIN_CODE": "400102",
                                            "CORRES_STATE": "Maharashtra",
                                            "CORRES_COUNTRY": "India",
                                            "CORRES_TEL_OFFICE": null,
                                            "CORRES_TEL_RESIDENT": "022~30784732",
                                            "CORRES_MOBILE_NO": "9619059221",
                                            "CORRES_EMAIL_ID": "vinod.yadav@motilaloswal.com",
                                            "CORRES_ADD_PROOF": "Aadhaar",
                                            "PERMANENT_ADD1": "Mhada Complex",
                                            "PERMANENT_ADD2": "Room no.10, Kaludin Chawl",
                                            "PERMANENT_ADD3": "Behram Baug, Veera Desai Road, MG Vidyalaya, Jogeshwari (West),",
                                            "PERMANENT_LANDMARK": "",
                                            "PERMANENT_PIN_CODE": "400102",
                                            "PERMANENT_CITY": "Mumbai",
                                            "PERMANENT_STATE": "Maharashtra",
                                            "PERMANENT_COUNTRY": "India",
                                            "PERMANENT_TEL_RESIDENT": "022~30784732",
                                            "OTHER_CITY_CORRES": "",
                                            "OTHER_CITY_PERMANENT": "",
                                            "PERMANENT_ADD_PROOF": "Aadhaar",
                                            "OCCUPATION_DETAILS": "Business",
                                            "EDUCATION_QUALIFICATION": "Graduate",
                                            "BUSINESS_EMP_NAME": "",
                                            "BUSINESS_TEL": "",
                                            "BUSINESS_MOBILE": "",
                                            "BUSINESS_FAX": "",
                                            "BUSINESS_EMAIL_ID": "amruta@whitepearls.in",
                                            "ANNUAL_INCOME": "1-5",
                                            "STOCK_PORTFOLIO": "<5",
                                            "BROKER_NAME": "",
                                            "BROKER_CLIENT_CODE": "",
                                            "ACTION_BY_SEBI": "",
                                            "NATURE_BUSINESS_EMP": "",

                                            "POLITICALLY_EXPOSED_PERSON": "No",
                                            "CONTRACT_NOTE": "Electronic Contract Note (ECN)",
                                            "RESIDENTIAL_STATUS": "Resident Individual",
                                            "RESIDENTIAL_SUB_STATUS": "Individual Resident",
                                            "QUALIFICATION_PROFESSIONAL": "",
                                            "OTHER_OCCUPATION": "",
                                            "RELATED_POLITICALLY_EXPOSED_PERSON": "No",
                                            "FACEBOOKID": "",
                                            "TWITTERID": "",
                                            "LINKEDINID": "",
                                            "NETWORTH": 60000,
                                            "ASONDATE": "03/04/2016"*/
                                                String title =
                                                    (object.isNull("TITLE") || object.getString(
                                                        "TITLE").equals("")) ? ""
                                                        : object.getString("TITLE");
                                                String gender =
                                                    (object.isNull("GENDER") || object.getString(
                                                        "GENDER").equals("")) ? ""
                                                        : object.getString("GENDER");
                                                String fName =
                                                    (object.isNull("FNAME") || object.getString(
                                                        "FNAME").equals("")) ? ""
                                                        : object.getString("FNAME");
                                                String mName =
                                                    (object.isNull("MNAME") || object.getString(
                                                        "MNAME").equals("")) ? ""
                                                        : object.getString("MNAME");
                                                String lName =
                                                    (object.isNull("LNAME") || object.getString(
                                                        "LNAME").equals("")) ? ""
                                                        : object.getString("LNAME");
                                                String relation =
                                                    (object.isNull("RELATION") || object.getString(
                                                        "RELATION").equals("")) ? ""
                                                        : object.getString("RELATION");
                                                String fhTitleName =
                                                    (object.isNull("FHTITLE") || object.getString(
                                                        "FHTITLE").equals("")) ? ""
                                                        : object.getString("FHTITLE");
                                                String fhfName =
                                                    (object.isNull("FHFNAME") || object.getString(
                                                        "FHFNAME").equals("")) ? ""
                                                        : object.getString("FHFNAME");
                                                String fhmName =
                                                    (object.isNull("FHMNAME") || object.getString(
                                                        "FHMNAME").equals("")) ? ""
                                                        : object.getString("FHMNAME");
                                                String fhlName =
                                                    (object.isNull("FHLNAME") || object.getString(
                                                        "FHLNAME").equals("")) ? ""
                                                        : object.getString("FHLNAME");
                                                String motherTitle = (object.isNull("MOTHER_TITLE")
                                                    || object.getString("MOTHER_TITLE").equals(""))
                                                    ? "" : object.getString("MOTHER_TITLE");
                                                String motherFName = (object.isNull("MOTHER_FNAME")
                                                    || object.getString("MOTHER_FNAME").equals(""))
                                                    ? "" : object.getString("MOTHER_FNAME");
                                                String motherMName = (object.isNull("MOTHER_MNAME")
                                                    || object.getString("MOTHER_MNAME").equals(""))
                                                    ? "" : object.getString("MOTHER_MNAME");
                                                String motherLName = (object.isNull("MOTHER_LNAME")
                                                    || object.getString("MOTHER_LNAME").equals(""))
                                                    ? "" : object.getString("MOTHER_LNAME");
                                                String maidenTitle = (object.isNull("MAIDEN_TITLE")
                                                    || object.getString("MAIDEN_TITLE").equals(""))
                                                    ? "" : object.getString("MAIDEN_TITLE");
                                                String maidenFName = (object.isNull("MAIDEN_FNAME")
                                                    || object.getString("MAIDEN_FNAME").equals(""))
                                                    ? "" : object.getString("MAIDEN_FNAME");
                                                String maidenMName = (object.isNull("MAIDEN_MNAME")
                                                    || object.getString("MAIDEN_MNAME").equals(""))
                                                    ? "" : object.getString("MAIDEN_MNAME");
                                                String maidenLName = (object.isNull("MAIDEN_LNAME")
                                                    || object.getString("MAIDEN_LNAME").equals(""))
                                                    ? "" : object.getString("MAIDEN_LNAME");
                                                String dateOfBirth = (object.isNull("DATE_OF_BIRTH")
                                                    || object.getString("DATE_OF_BIRTH").equals(""))
                                                    ? "" : object.getString("DATE_OF_BIRTH");
                                                String maritalStatus =
                                                    (object.isNull("MARITAL_STATUS")
                                                        || object.getString("MARITAL_STATUS")
                                                        .equals("")) ? ""
                                                        : object.getString("MARITAL_STATUS");
                                                String panNo =
                                                    (object.isNull("PANNO") || object.getString(
                                                        "PANNO").equals("")) ? ""
                                                        : object.getString("PANNO");
                                                String uid =
                                                    (object.isNull("UID") || object.getString("UID")
                                                        .equals("")) ? "" : object.getString("UID");
                                                String namePan =
                                                    (object.isNull("Name_Pan") || object.getString(
                                                        "Name_Pan").equals("")) ? ""
                                                        : object.getString("Name_Pan");
                                                String corresAdd1 = (object.isNull("CORRES_ADD1")
                                                    || object.getString("CORRES_ADD1").equals(""))
                                                    ? "" : object.getString("CORRES_ADD1");
                                                String corresAdd2 = (object.isNull("CORRES_ADD2")
                                                    || object.getString("CORRES_ADD2").equals(""))
                                                    ? "" : object.getString("CORRES_ADD2");
                                                String corresAdd3 = (object.isNull("CORRES_ADD3")
                                                    || object.getString("CORRES_ADD3").equals(""))
                                                    ? "" : object.getString("CORRES_ADD3");
                                                String corresLandmark =
                                                    (object.isNull("CORRES_LANDMARK")
                                                        || object.getString("CORRES_LANDMARK")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_LANDMARK");
                                                String corresCity = (object.isNull("CORRES_CITY")
                                                    || object.getString("CORRES_CITY").equals(""))
                                                    ? "" : object.getString("CORRES_CITY");
                                                String corresPinCode =
                                                    (object.isNull("CORRES_PIN_CODE")
                                                        || object.getString("CORRES_PIN_CODE")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_PIN_CODE");
                                                String corresState = (object.isNull("CORRES_STATE")
                                                    || object.getString("CORRES_STATE").equals(""))
                                                    ? "" : object.getString("CORRES_STATE");
                                                String corresCountry =
                                                    (object.isNull("CORRES_COUNTRY")
                                                        || object.getString("CORRES_COUNTRY")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_COUNTRY");
                                                String corresTelOffice =
                                                    (object.isNull("CORRES_TEL_OFFICE")
                                                        || object.getString("CORRES_TEL_OFFICE")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_TEL_OFFICE");
                                                String corresTelResident =
                                                    (object.isNull("CORRES_TEL_RESIDENT")
                                                        || object.getString("CORRES_TEL_RESIDENT")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_TEL_RESIDENT");
                                                String corresMobileNo =
                                                    (object.isNull("CORRES_MOBILE_NO")
                                                        || object.getString("CORRES_MOBILE_NO")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_MOBILE_NO");
                                                String corresEmailId =
                                                    (object.isNull("CORRES_EMAIL_ID")
                                                        || object.getString("CORRES_EMAIL_ID")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_EMAIL_ID");
                                                String corresAddProof =
                                                    (object.isNull("CORRES_ADD_PROOF")
                                                        || object.getString("CORRES_ADD_PROOF")
                                                        .equals("")) ? ""
                                                        : object.getString("CORRES_ADD_PROOF");
                                                String permanentAdd1 =
                                                    (object.isNull("PERMANENT_ADD1")
                                                        || object.getString("PERMANENT_ADD1")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_ADD1");
                                                String permanentAdd2 =
                                                    (object.isNull("PERMANENT_ADD2")
                                                        || object.getString("PERMANENT_ADD2")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_ADD2");
                                                String permanentAdd3 =
                                                    (object.isNull("PERMANENT_ADD3")
                                                        || object.getString("PERMANENT_ADD3")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_ADD3");
                                                String permanentLandmark =
                                                    (object.isNull("PERMANENT_LANDMARK")
                                                        || object.getString("PERMANENT_LANDMARK")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_LANDMARK");
                                                String permanentPinCode =
                                                    (object.isNull("PERMANENT_PIN_CODE")
                                                        || object.getString("PERMANENT_PIN_CODE")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_PIN_CODE");
                                                String permanentCity =
                                                    (object.isNull("PERMANENT_CITY")
                                                        || object.getString("PERMANENT_CITY")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_CITY");
                                                String permanentState =
                                                    (object.isNull("PERMANENT_STATE")
                                                        || object.getString("PERMANENT_STATE")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_STATE");
                                                String permanentCountry =
                                                    (object.isNull("PERMANENT_COUNTRY")
                                                        || object.getString("PERMANENT_COUNTRY")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_COUNTRY");
                                                String permanentTelResident =
                                                    (object.isNull("PERMANENT_TEL_RESIDENT")
                                                        || object.getString(
                                                        "PERMANENT_TEL_RESIDENT").equals("")) ? ""
                                                        : object.getString(
                                                            "PERMANENT_TEL_RESIDENT");
                                                String otherCityCorres =
                                                    (object.isNull("OTHER_CITY_CORRES")
                                                        || object.getString("OTHER_CITY_CORRES")
                                                        .equals("")) ? ""
                                                        : object.getString("OTHER_CITY_CORRES");
                                                String otherCityPermanent =
                                                    (object.isNull("OTHER_CITY_PERMANENT") || object
                                                        .getString("OTHER_CITY_PERMANENT")
                                                        .equals("")) ? ""
                                                        : object.getString("OTHER_CITY_PERMANENT");
                                                String permanentAddProof =
                                                    (object.isNull("PERMANENT_ADD_PROOF")
                                                        || object.getString("PERMANENT_ADD_PROOF")
                                                        .equals("")) ? ""
                                                        : object.getString("PERMANENT_ADD_PROOF");
                                                String occupationDetails =
                                                    (object.isNull("OCCUPATION_DETAILS")
                                                        || object.getString("OCCUPATION_DETAILS")
                                                        .equals("")) ? ""
                                                        : object.getString("OCCUPATION_DETAILS");
                                                String educationQualification =
                                                    (object.isNull("EDUCATION_QUALIFICATION")
                                                        || object.getString(
                                                        "EDUCATION_QUALIFICATION").equals("")) ? ""
                                                        : object.getString(
                                                            "EDUCATION_QUALIFICATION");
                                                String businessEmpName =
                                                    (object.isNull("BUSINESS_EMP_NAME")
                                                        || object.getString("BUSINESS_EMP_NAME")
                                                        .equals("")) ? ""
                                                        : object.getString("BUSINESS_EMP_NAME");
                                                String businessTel = (object.isNull("BUSINESS_TEL")
                                                    || object.getString("BUSINESS_TEL").equals(""))
                                                    ? "" : object.getString("BUSINESS_TEL");
                                                String businessMobile =
                                                    (object.isNull("BUSINESS_MOBILE")
                                                        || object.getString("BUSINESS_MOBILE")
                                                        .equals("")) ? ""
                                                        : object.getString("BUSINESS_MOBILE");
                                                String businessFax = (object.isNull("BUSINESS_FAX")
                                                    || object.getString("BUSINESS_FAX").equals(""))
                                                    ? "" : object.getString("BUSINESS_FAX");
                                                String businessEmailId =
                                                    (object.isNull("BUSINESS_EMAIL_ID")
                                                        || object.getString("BUSINESS_EMAIL_ID")
                                                        .equals("")) ? ""
                                                        : object.getString("BUSINESS_EMAIL_ID");
                                                String annualIncome =
                                                    (object.isNull("ANNUAL_INCOME")
                                                        || object.getString("ANNUAL_INCOME")
                                                        .equals("")) ? ""
                                                        : object.getString("ANNUAL_INCOME");
                                                String stockPortfolio =
                                                    (object.isNull("STOCK_PORTFOLIO")
                                                        || object.getString("STOCK_PORTFOLIO")
                                                        .equals("")) ? ""
                                                        : object.getString("STOCK_PORTFOLIO");
                                                String brokerName = (object.isNull("BROKER_NAME")
                                                    || object.getString("BROKER_NAME").equals(""))
                                                    ? "" : object.getString("BROKER_NAME");
                                                String brokerClientCode =
                                                    (object.isNull("BROKER_CLIENT_CODE")
                                                        || object.getString("BROKER_CLIENT_CODE")
                                                        .equals("")) ? ""
                                                        : object.getString("BROKER_CLIENT_CODE");
                                                String actionBySebi =
                                                    (object.isNull("ACTION_BY_SEBI")
                                                        || object.getString("ACTION_BY_SEBI")
                                                        .equals("")) ? ""
                                                        : object.getString("ACTION_BY_SEBI");
                                                String natureBusinessEmp =
                                                    (object.isNull("NATURE_BUSINESS_EMP")
                                                        || object.getString("NATURE_BUSINESS_EMP")
                                                        .equals("")) ? ""
                                                        : object.getString("NATURE_BUSINESS_EMP");
                                                String politicallyExposedPerson =
                                                    (object.isNull("POLITICALLY_EXPOSED_PERSON")
                                                        || object.getString(
                                                        "POLITICALLY_EXPOSED_PERSON").equals(""))
                                                        ? "" : object.getString(
                                                        "POLITICALLY_EXPOSED_PERSON");
                                                String contractNote =
                                                    (object.isNull("CONTRACT_NOTE")
                                                        || object.getString("CONTRACT_NOTE")
                                                        .equals("")) ? ""
                                                        : object.getString("CONTRACT_NOTE");
                                                String residentialStatus =
                                                    (object.isNull("RESIDENTIAL_STATUS")
                                                        || object.getString("RESIDENTIAL_STATUS")
                                                        .equals("")) ? ""
                                                        : object.getString("RESIDENTIAL_STATUS");
                                                String residentialSubStatus =
                                                    (object.isNull("RESIDENTIAL_SUB_STATUS")
                                                        || object.getString(
                                                        "RESIDENTIAL_SUB_STATUS").equals("")) ? ""
                                                        : object.getString(
                                                            "RESIDENTIAL_SUB_STATUS");
                                                String qualificationProfessional =
                                                    (object.isNull("QUALIFICATION_PROFESSIONAL")
                                                        || object.getString(
                                                        "QUALIFICATION_PROFESSIONAL").equals(""))
                                                        ? "" : object.getString(
                                                        "QUALIFICATION_PROFESSIONAL");
                                                String otherOccupation =
                                                    (object.isNull("OTHER_OCCUPATION")
                                                        || object.getString("OTHER_OCCUPATION")
                                                        .equals("")) ? ""
                                                        : object.getString("OTHER_OCCUPATION");
                                                String relatedPoliticallyExposedPerson =
                                                    (object.isNull(
                                                        "RELATED_POLITICALLY_EXPOSED_PERSON")
                                                        || object.getString(
                                                        "RELATED_POLITICALLY_EXPOSED_PERSON")
                                                        .equals("")) ? "" : object.getString(
                                                        "RELATED_POLITICALLY_EXPOSED_PERSON");
                                                String facebookId = (object.isNull("FACEBOOKID")
                                                    || object.getString("FACEBOOKID").equals(""))
                                                    ? "" : object.getString("FACEBOOKID");
                                                String twitterId =
                                                    (object.isNull("TWITTERID") || object.getString(
                                                        "TWITTERID").equals("")) ? ""
                                                        : object.getString("TWITTERID");
                                                String linkedinId = (object.isNull("LINKEDINID")
                                                    || object.getString("LINKEDINID").equals(""))
                                                    ? "" : object.getString("LINKEDINID");
                                                String networth =
                                                    (object.isNull("NETWORTH") || object.getString(
                                                        "NETWORTH").equals("")) ? ""
                                                        : object.getString("NETWORTH");
                                                String asOnDate =
                                                    (object.isNull("ASONDATE") || object.getString(
                                                        "ASONDATE").equals("")) ? ""
                                                        : object.getString("ASONDATE");

                                                //                                            tvResidentStatus.setText(residentialStatus);
                                                //                                            tvSubStatus.setText(residentialSubStatus);
                                                tvName.setText(title
                                                    + " "
                                                    + fName
                                                    + " "
                                                    + mName
                                                    + " "
                                                    + lName);
                                                tvGender.setText(gender);
                                                tvRelation.setText(relation);
                                                tvFatherSpouseName.setText(fhTitleName
                                                    + " "
                                                    + fhfName
                                                    + " "
                                                    + fhmName
                                                    + " "
                                                    + fhlName);
                                                tvMotherName.setText(motherTitle
                                                    + " "
                                                    + motherFName
                                                    + " "
                                                    + motherMName
                                                    + " "
                                                    + motherLName);
                                                if (isBlank(maidenFName)) {
                                                    tvMaidenName.setText("");
                                                } else {
                                                    tvMaidenName.setText(maidenTitle
                                                        + " "
                                                        + maidenFName
                                                        + " "
                                                        + maidenMName
                                                        + " "
                                                        + maidenLName);
                                                }
                                                tvDOB.setText(dateOfBirth);
                                                tvPAN.setText(panNo);
                                                tvNameOnPAN.setText(namePan);
                                                if (uid.equalsIgnoreCase("")) {
                                                    tvAadhaarNumber.setText(uid);
                                                } else {
                                                    //                                                BigDecimal bd = new BigDecimal(uid);
                                                    //                                                tvAadhaarNumber.setText(String.valueOf(bd.longValue()));
                                                    tvAadhaarNumber.setText(uid);
                                                }
                                                tvMaritalStatus.setText(maritalStatus);
                                                //Correspondence Address
                                                tvFlatNoCorres.setText(corresAdd2);
                                                tvBldgNoCorres.setText(corresAdd1);
                                                tvAreaCorres.setText(corresAdd3);
                                                tvCityCorres.setText(corresCity);
                                                tvStateCorres.setText(corresState);
                                                tvPincodeCorres.setText(corresPinCode);
                                                tvCountryCorres.setText(corresCountry);
                                                tvAddProofCorres.setText(corresAddProof);
                                                //Permanent Address
                                                tvFlatNoPerm.setText(permanentAdd2);
                                                tvBldgNoPerm.setText(permanentAdd1);
                                                tvAreaPerm.setText(permanentAdd3);
                                                tvCityPerm.setText(permanentCity);
                                                tvStatePerm.setText(permanentState);
                                                tvPincodePerm.setText(permanentPinCode);
                                                tvCountryPerm.setText(permanentCountry);
                                                tvAddProofPerm.setText(permanentAddProof);
                                                //Contact Info
                                                tvMobile.setText(corresMobileNo);
                                                tvEmail.setText(corresEmailId);
                                                //                                            tvTelNo.setText(corresTelResident);
                                                //                                            tvFbId.setText(facebookId);
                                                //                                            tvTwitterId.setText(twitterId);
                                                //                                            tvLinkedinID.setText(linkedinId);
                                                //Work Detail
                                                tvOccupation.setText(occupationDetails);
                                                tvQualification.setText(educationQualification);
                                                tvIncome.setText(annualIncome);
                                                tvContract.setText(contractNote);
                                                //                                            tvStockPortfolio.setText(stockPortfolio);
                                                if (networth.equalsIgnoreCase("")
                                                    || networth.equalsIgnoreCase("0")
                                                    || networth.equalsIgnoreCase("0.0")) {
                                                    tvNetWorth.setText("");
                                                } else {
                                                    BigDecimal bd = new BigDecimal(networth);
                                                    tvNetWorth.setText(
                                                        String.valueOf(bd.longValue()));
                                                }
                                                tvNWDate.setText(asOnDate);
                                                tvPoliticalyExposed.setText(
                                                    politicallyExposedPerson);
                                                tvRelatedPolitical.setText(
                                                    relatedPoliticallyExposedPerson);
                                                //                                            tvBrokerName.setText(brokerName);
                                                //                                            tvClientCode.setText(brokerClientCode);
                                                //                                            tvSebiAuthority.setText(actionBySebi);
                                            }
                                        }
                                    }

                                    if (response.has("ReviewDetails2")) {
                                        JSONArray jsonArray2 =
                                            response.getJSONArray("ReviewDetails2");
                                        if (jsonArray2.length() != 0) {
                                            for (int i = 0; i < jsonArray2.length(); i++) {
                                                JSONObject object = jsonArray2.getJSONObject(0);
                                    /*"BANK_NAME": "ALLAHABAD BANK",
                                            "BANK_BRANCH": "ADCHINI",
                                            "BANK_ADD": "ADCHINI (ADC),,,Delhi",
                                            "BANK_ACC_NO": "",
                                            "BANK_AC_TYPE": "Saving",
                                            "BANK_MICR_CODE": "110010002",
                                            "BANK_IFSC_CODE": "ALLA0210835",
                                            "BANK_PROOF": "",
                                            "PHOTO_PROOF": "",
                                            "PRODUCT": "EQUITY"*/
                                                bankNameReview = (object.isNull(BANK_NAME_REVIEW)
                                                    || object.getString(BANK_NAME_REVIEW)
                                                    .equals("")) ? ""
                                                    : object.getString(BANK_NAME_REVIEW);
                                                bankBranchReview =
                                                    (object.isNull(BANK_BRANCH_REVIEW)
                                                        || object.getString(BANK_BRANCH_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(BANK_BRANCH_REVIEW);
                                                bankAddReview = (object.isNull(BANK_ADD_REVIEW)
                                                    || object.getString(BANK_ADD_REVIEW).equals(""))
                                                    ? "" : object.getString(BANK_ADD_REVIEW);
                                                accNumberReview = (object.isNull(ACC_NO_REVIEW)
                                                    || object.getString(ACC_NO_REVIEW).equals(""))
                                                    ? "" : object.getString(ACC_NO_REVIEW);
                                                accTypeReview = (object.isNull(AC_TYPE_REVIEW)
                                                    || object.getString(AC_TYPE_REVIEW).equals(""))
                                                    ? "" : object.getString(AC_TYPE_REVIEW);
                                                micrCodeReview = (object.isNull(MICR_CODE_REVIEW)
                                                    || object.getString(MICR_CODE_REVIEW)
                                                    .equals("")) ? ""
                                                    : object.getString(MICR_CODE_REVIEW);
                                                ifscCodeReview = (object.isNull(IFSC_CODE_REVIEW)
                                                    || object.getString(IFSC_CODE_REVIEW)
                                                    .equals("")) ? ""
                                                    : object.getString(IFSC_CODE_REVIEW);
                                                bankProofReview = (object.isNull(BANK_PROOF_REVIEW)
                                                    || object.getString(BANK_PROOF_REVIEW)
                                                    .equals("")) ? ""
                                                    : object.getString(BANK_PROOF_REVIEW);
                                                photoProofReview =
                                                    (object.isNull(PHOTO_PROOF_REVIEW)
                                                        || object.getString(PHOTO_PROOF_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(PHOTO_PROOF_REVIEW);
                                                productReview = (object.isNull(PRODUCT_REVIEW)
                                                    || object.getString(PRODUCT_REVIEW).equals(""))
                                                    ? "" : object.getString(PRODUCT_REVIEW);

                                                if (productReview.equalsIgnoreCase(EQUITY)) {
                                                    setEquityData(0);
                                                } else if (productReview.equalsIgnoreCase(
                                                    COMMODITY)) {
                                                    setCommodityData(0);
                                                }

                                                if (jsonArray2.length() > 1) {
                                                    JSONObject object1 =
                                                        jsonArray2.getJSONObject(1);
                                                    bankNameReviewFirst =
                                                        (object1.isNull(BANK_NAME_REVIEW) || object1
                                                            .getString(BANK_NAME_REVIEW)
                                                            .equals("")) ? ""
                                                            : object1.getString(BANK_NAME_REVIEW);
                                                    bankBranchReviewFirst =
                                                        (object1.isNull(BANK_BRANCH_REVIEW)
                                                            || object1.getString(BANK_BRANCH_REVIEW)
                                                            .equals("")) ? ""
                                                            : object1.getString(BANK_BRANCH_REVIEW);
                                                    bankAddReviewFirst =
                                                        (object1.isNull(BANK_ADD_REVIEW)
                                                            || object1.getString(BANK_ADD_REVIEW)
                                                            .equals("")) ? ""
                                                            : object1.getString(BANK_ADD_REVIEW);
                                                    accNumberReviewFirst =
                                                        (object1.isNull(ACC_NO_REVIEW)
                                                            || object1.getString(ACC_NO_REVIEW)
                                                            .equals("")) ? ""
                                                            : object1.getString(ACC_NO_REVIEW);
                                                    accTypeReviewFirst =
                                                        (object1.isNull(AC_TYPE_REVIEW)
                                                            || object1.getString(AC_TYPE_REVIEW)
                                                            .equals("")) ? ""
                                                            : object1.getString(AC_TYPE_REVIEW);
                                                    micrCodeReviewFirst =
                                                        (object1.isNull(MICR_CODE_REVIEW) || object1
                                                            .getString(MICR_CODE_REVIEW)
                                                            .equals("")) ? ""
                                                            : object1.getString(MICR_CODE_REVIEW);
                                                    ifscCodeReviewFirst =
                                                        (object1.isNull(IFSC_CODE_REVIEW) || object1
                                                            .getString(IFSC_CODE_REVIEW)
                                                            .equals("")) ? ""
                                                            : object1.getString(IFSC_CODE_REVIEW);
                                                    bankProofReviewFirst =
                                                        (object1.isNull(BANK_PROOF_REVIEW)
                                                            || object1.getString(BANK_PROOF_REVIEW)
                                                            .equals("")) ? ""
                                                            : object1.getString(BANK_PROOF_REVIEW);
                                                    photoProofReviewFirst =
                                                        (object1.isNull(PHOTO_PROOF_REVIEW)
                                                            || object1.getString(PHOTO_PROOF_REVIEW)
                                                            .equals("")) ? ""
                                                            : object1.getString(PHOTO_PROOF_REVIEW);
                                                    productReviewFirst =
                                                        (object1.isNull(PRODUCT_REVIEW)
                                                            || object1.getString(PRODUCT_REVIEW)
                                                            .equals("")) ? ""
                                                            : object1.getString(PRODUCT_REVIEW);

                                                    if (productReviewFirst.equalsIgnoreCase(
                                                        EQUITY)) {
                                                        setEquityData(1);
                                                    } else if (productReviewFirst.equalsIgnoreCase(
                                                        COMMODITY)) {
                                                        setCommodityData(1);
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (response.has("ReviewDetails3")) {
                                        JSONArray jsonArray3 =
                                            response.getJSONArray("ReviewDetails3");
                                        if (jsonArray3.length() != 0) {
                                            arrayListEqSchemeReview =
                                                new ArrayList<HashMap<String, String>>();
                                            arrayListCommSchemeReview =
                                                new ArrayList<HashMap<String, String>>();
                                            llEqSchemeDetailReview.removeAllViews();
                                            llCommSchemeDetailReview.removeAllViews();
                                            customFieldList = new ArrayList<CustomFieldEquity>();
                                            customFieldCommList =
                                                new ArrayList<CustomFieldCommodity>();
                                            CustomFieldEquity customField = null;
                                            CustomFieldCommodity customFieldCommodity = null;
                                            int counterEqu = 0;
                                            int counterComm = 0;
                                            for (int i = 0; i < jsonArray3.length(); i++) {
                                                JSONObject object = jsonArray3.getJSONObject(i);
                                                eqMap = new HashMap<String, String>();
                                                commMap = new HashMap<String, String>();
                                                String cat =
                                                    (object.isNull(CAT) || object.getString(CAT)
                                                        .equals("")) ? "" : object.getString(CAT);
                                                String profileName = (object.isNull(PROFILE_NAME)
                                                    || object.getString(PROFILE_NAME).equals(""))
                                                    ? "" : object.getString(PROFILE_NAME);
                                                String productScheme =
                                                    (object.isNull(PRODUCT_REVIEW_SCHEME)
                                                        || object.getString(PRODUCT_REVIEW_SCHEME)
                                                        .equals("")) ? ""
                                                        : object.getString(PRODUCT_REVIEW_SCHEME);

                                                if (productScheme.equalsIgnoreCase(EQUITY)) {
                                                    eqMap.put(CAT, cat);
                                                    eqMap.put(PROFILE_NAME, profileName);
                                                    eqMap.put(PRODUCT_REVIEW_SCHEME, productScheme);
                                                    customField =
                                                        new CustomFieldEquity(cat, profileName,
                                                            productScheme);
                                                    customFieldList.add(customField);
                                                } else if (productScheme.equalsIgnoreCase(
                                                    COMMODITY)) {
                                                    commMap.put(CAT, cat);
                                                    commMap.put(PROFILE_NAME, profileName);
                                                    commMap.put(PRODUCT_REVIEW_SCHEME,
                                                        productScheme);
                                                    customFieldCommodity =
                                                        new CustomFieldCommodity(cat, profileName,
                                                            productScheme);
                                                    customFieldCommList.add(customFieldCommodity);
                                                }
                                                if (!eqMap.isEmpty()) {
                                                    arrayListEqSchemeReview.add(eqMap);
                                                }
                                                if (!commMap.isEmpty()) {
                                                    arrayListCommSchemeReview.add(commMap);
                                                }
                                            }

                                            for (CustomFieldEquity field : customFieldList) {
                                                LinearLayout layoutForHeader =
                                                    new LinearLayout(mContext);
                                                int padding =
                                                    getResources().getDimensionPixelOffset(
                                                        R.dimen.padding);
                                                layoutForHeader.setPadding(padding, padding,
                                                    padding, padding);
                                                layoutForHeader.setOrientation(
                                                    LinearLayout.HORIZONTAL);
                                                //  layoutForHeader.setBackgroundColor(getResources().getColor(R.color.secondaryBackground));
                                                if (counterEqu % 2 == 1) {
                                                    layoutForHeader.setBackgroundColor(
                                                        getResources().getColor(
                                                            R.color.scheme_grey));
                                                } else {
                                                    layoutForHeader.setBackgroundColor(
                                                        Color.parseColor("#f8f6f7"));
                                                }
                                                LinearLayout.LayoutParams linear =
                                                    new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.MATCH_PARENT
                                                    );
                                                linear.weight = 1.0f;
                                                layoutForHeader.setLayoutParams(linear);
                                                layoutForHeader.setLayoutParams(
                                                    new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                                llEqSchemeDetailReview.addView(layoutForHeader);
                                   /* TextView bullet = new TextView(mContext);
                                    bullet.setText("hi");
                                    layoutForHeader.addView(bullet);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT
                                    );
                                    params.setMargins(10, 0, 10, 0);
                                    bullet.setLayoutParams(params);*/
                                                //label

                                                TextView category = new TextView(mContext);
                                                category.setText(field.getCategory());
                                                category.setTextAppearance(mContext,
                                                    R.style.TextViewScheme);
                                                category.setLayoutParams(linear);
                                                TextView colon = new TextView(mContext);
                                                colon.setText(":");
                                                colon.setTextAppearance(mContext,
                                                    R.style.TextViewScheme);

                                                TextView profileName = new TextView(mContext);
                                                profileName.setText(field.getProfileName());
                                                profileName.setTextAppearance(mContext,
                                                    R.style.TextViewScheme);

                                                profileName.setLayoutParams(linear);
                                                layoutForHeader.addView(category);
                                                layoutForHeader.addView(colon);
                                                layoutForHeader.addView(profileName);

                                                counterEqu++;

                                            }
                                            for (CustomFieldCommodity field : customFieldCommList) {
                                                LinearLayout layoutForHeader =
                                                    new LinearLayout(mContext);
                                                int padding =
                                                    getResources().getDimensionPixelOffset(
                                                        R.dimen.padding);
                                                layoutForHeader.setPadding(padding, padding,
                                                    padding, padding);
                                                layoutForHeader.setOrientation(
                                                    LinearLayout.HORIZONTAL);
                                                //  layoutForHeader.setBackgroundColor(getResources().getColor(R.color.secondaryBackground));
                                                if (counterComm % 2 == 1) {
                                                    layoutForHeader.setBackgroundColor(
                                                        getResources().getColor(
                                                            R.color.scheme_grey));
                                                } else {
                                                    layoutForHeader.setBackgroundColor(
                                                        Color.parseColor("#f8f6f7"));
                                                }
                                                LinearLayout.LayoutParams linear =
                                                    new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.MATCH_PARENT
                                                    );
                                                linear.weight = 1.0f;
                                                layoutForHeader.setLayoutParams(linear);
                                                layoutForHeader.setLayoutParams(
                                                    new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                                                llCommSchemeDetailReview.addView(layoutForHeader);
                                   /* TextView bullet = new TextView(mContext);
                                    bullet.setText("hi");
                                    layoutForHeader.addView(bullet);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT
                                    );
                                    params.setMargins(10, 0, 10, 0);
                                    bullet.setLayoutParams(params);*/
                                                //label

                                                TextView category = new TextView(mContext);
                                                category.setText(field.getCategory());
                                                category.setTextAppearance(mContext,
                                                    R.style.TextViewScheme);
                                                category.setLayoutParams(linear);
                                                TextView colon = new TextView(mContext);
                                                colon.setText(":");
                                                colon.setTextAppearance(mContext,
                                                    R.style.TextViewScheme);
                                                TextView profileName = new TextView(mContext);
                                                profileName.setText(field.getProfileName());
                                                profileName.setTextAppearance(mContext,
                                                    R.style.TextViewScheme);

                                                profileName.setLayoutParams(linear);
                                                layoutForHeader.addView(category);
                                                layoutForHeader.addView(colon);
                                                layoutForHeader.addView(profileName);
                                                counterComm++;

                                            }
                                   /* reviewEditEqSchemeAdapter = new ReviewEditEquitySchemeAdapter(mContext, arrayListEqSchemeReview);
                                    lvEqSchemeDetailReview.setAdapter(reviewEditEqSchemeAdapter);
                                    reviewEditEqSchemeAdapter.notifyDataSetChanged();
                                    setListViewHeightBasedOnChildren(lvEqSchemeDetailReview);*/

                                    /*reviewEditCommSchemeAdapter = new ReviewEditCommoditySchemeAdapter(mContext, arrayListCommSchemeReview);
                                    lvCommSchemeDetailReview.setAdapter(reviewEditCommSchemeAdapter);
                                    reviewEditCommSchemeAdapter.notifyDataSetChanged();
                                    setListViewHeightBasedOnChildren(lvCommSchemeDetailReview);*/
                                            tvName.setFocusable(true);
                                            tvName.setFocusableInTouchMode(true);
                                        }
                                    }
                                    if (response.has("ReviewDetails4")) {
                                        JSONArray jsonArray4 =
                                            response.getJSONArray("ReviewDetails4");
                                        if (jsonArray4.length() != 0) {
                                            for (int i = 0; i < jsonArray4.length(); i++) {
                                                JSONObject object = jsonArray4.getJSONObject(i);
                                                String quesId =
                                                    (object.isNull("QUES_Id") || object.getString(
                                                        "QUES_Id").equals("")) ? ""
                                                        : object.getString("QUES_Id");
                                                String subQuesId = (object.isNull("SubQues_Id")
                                                    || object.getString("SubQues_Id").equals(""))
                                                    ? "" : object.getString("SubQues_Id");
                                                String ansDesc = (object.isNull("Answer_desc")
                                                    || object.getString("Answer_desc").equals(""))
                                                    ? "" : object.getString("Answer_desc");
                                                String ansFlag = (object.isNull("Answer_Flag")
                                                    || object.getString("Answer_Flag").equals(""))
                                                    ? "" : object.getString("Answer_Flag");

                                                if (quesId.equals("1")) {
                                                    if (subQuesId.equals("1") && ansFlag.equals(
                                                        "1")) {
                                                        tvCountryCitizenshipQuestion.setText(
                                                            getResources().getString(
                                                                R.string.other_country_citizen)
                                                                + " :  Yes");
                                                        llCountryCitizenship.setVisibility(
                                                            View.VISIBLE);
                                                        tvCounrtyOfCitizen.setText(ansDesc);
                                                    } else {
                                                        tvCountryCitizenshipQuestion.setText(
                                                            getResources().getString(
                                                                R.string.other_country_citizen)
                                                                + " :  No");
                                                        llCountryCitizenship.setVisibility(
                                                            View.GONE);
                                                    }
                                                }

                                                if (quesId.equals("2")) {
                                                    if (ansFlag.equals("1")) {
                                                        tvCountryBirthQuestion.setText(
                                                            getResources().getString(
                                                                R.string.country_birth)
                                                                + " :  Yes");
                                                        llCountryBirth.setVisibility(View.VISIBLE);
                                                        if (subQuesId.equals("2")) {
                                                            tvCountryOfBirth.setText(ansDesc);
                                                        }
                                                        if (subQuesId.equals("3")) {
                                                            tvPlaceOfBirth.setText(ansDesc);
                                                        }
                                                    } else {
                                                        tvCountryBirthQuestion.setText(
                                                            getResources().getString(
                                                                R.string.country_birth) + " :  No");
                                                        llCountryBirth.setVisibility(View.GONE);
                                                    }
                                                }

                                                if (quesId.equals("3")) {
                                                    if (ansFlag.equals("1")) {
                                                        tvCountryTaxQuestion.setText(
                                                            getResources().getString(
                                                                R.string.other_country_tax)
                                                                + " :  Yes");
                                                        llCountryTax.setVisibility(View.VISIBLE);
                                                        if (subQuesId.equals("4")) {
                                                            tvCountryOfTaXRes.setText(ansDesc);
                                                        }
                                                        if (subQuesId.equals("5")) {
                                                            tvTaxIDNum.setText(ansDesc);
                                                        }
                                                    } else {
                                                        tvCountryTaxQuestion.setText(
                                                            getResources().getString(
                                                                R.string.other_country_tax)
                                                                + " :  No");
                                                        llCountryTax.setVisibility(View.GONE);
                                                    }
                                                }
                                                if (quesId.equals("4")) {
                                                    if (ansFlag.equals("1")) {
                                                        tvPoaAddressQuestion.setText(
                                                            getResources().getString(
                                                                R.string.poa_address) + " :  Yes");
                                                    } else {
                                                        tvPoaAddressQuestion.setText(
                                                            getResources().getString(
                                                                R.string.poa_address) + " :  No");
                                                    }
                                                }
                                                if (quesId.equals("5")) {
                                                    if (ansFlag.equals("1")) {
                                                        tvAddressOutsideIndiaQuestion.setText(
                                                            getResources().getString(
                                                                R.string.address_outside_india)
                                                                + " :  Yes");
                                                    } else {
                                                        tvAddressOutsideIndiaQuestion.setText(
                                                            getResources().getString(
                                                                R.string.address_outside_india)
                                                                + " :  No");
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    if (getAppType().equalsIgnoreCase(DIRECT)
                                        && !getIsAadharVerified()
                                        && !getInterestedInMF()) {
                                        tvNomineeJointTitle.setText(
                                            "Nominee and Joint Holder Details");
                                        if (response.has("ReviewDetails10")) {
                                            //Bank Detail
                                            JSONArray jsonArray =
                                                response.getJSONArray("ReviewDetails10");
                                            if (jsonArray.length() != 0) {
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject object = jsonArray.getJSONObject(i);
                                                    /* tvSecHoldEditPersonalDetail,tvThirdHoldEditPersonalDetail,                     */

                                                    String dematAccType =
                                                        (object.isNull(DEMAT_ACC_TYPE)
                                                            || object.getString(DEMAT_ACC_TYPE)
                                                            .equals("")) ? ""
                                                            : object.getString(DEMAT_ACC_TYPE);
                                                    String dematFirstAccTitle =
                                                        (object.isNull(DEMAT_ACC_TITLE)
                                                            || object.getString(DEMAT_ACC_TITLE)
                                                            .equals("")) ? ""
                                                            : object.getString(DEMAT_ACC_TITLE);
                                                    String dematFirstJointFirstName =
                                                        (object.isNull(
                                                            DEMAT_FIRST_JOINT_ACC_HOLDER_FNAME)
                                                            || object.getString(
                                                            DEMAT_FIRST_JOINT_ACC_HOLDER_FNAME)
                                                            .equals("")) ? "" : object.getString(
                                                            DEMAT_FIRST_JOINT_ACC_HOLDER_FNAME);
                                                    String dematFirstJointMiddleName =
                                                        (object.isNull(
                                                            DEMAT_FIRST_JOINT_ACC_HOLDER_MNAME)
                                                            || object.getString(
                                                            DEMAT_FIRST_JOINT_ACC_HOLDER_MNAME)
                                                            .equals("")) ? "" : object.getString(
                                                            DEMAT_FIRST_JOINT_ACC_HOLDER_MNAME);
                                                    String dematFirstJointLastName = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_LNAME)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_LNAME)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_LNAME);
                                                    String dematFirstJointRelation = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_RELATION)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_RELATION)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_RELATION);
                                                    String dematFirstJointFHFName = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_FHFNAME)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_FHFNAME)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_FHFNAME);
                                                    String dematFirstJointFHMName = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_FHMNAME)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_FHMNAME)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_FHMNAME);
                                                    String dematFirstJointFHLName = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_FHLNAME)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_FHLNAME)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_FHLNAME);
                                                    String dematFirstJointAdd1 = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_ADD1) || object
                                                        .getString(
                                                            DEMAT_FIRST_JOINT_ACC_HOLDER_ADD1)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_ADD1);
                                                    String dematFirstJointAdd2 = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_ADD2) || object
                                                        .getString(
                                                            DEMAT_FIRST_JOINT_ACC_HOLDER_ADD2)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_ADD2);
                                                    String dematFirstJointAdd3 = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_ADD3) || object
                                                        .getString(
                                                            DEMAT_FIRST_JOINT_ACC_HOLDER_ADD3)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_ADD3);
                                                    String dematFirstJointLandmark = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_LANDMARK)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_LANDMARK)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_LANDMARK);
                                                    String dematFirstJointPincode = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_PIN)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_PIN)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_PIN);
                                                    String dematFirstJointCity = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_CITY) || object
                                                        .getString(
                                                            DEMAT_FIRST_JOINT_ACC_HOLDER_CITY)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_CITY);
                                                    String dematFirstJointState = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_STATE)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_STATE)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_STATE);
                                                    String dematFirstJointCountry = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_COUNTRY)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_COUNTRY)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_COUNTRY);
                                                    String dematFirstJointPan = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_PAN)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_PAN)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_PAN);
                                                    String dematFirstJointSex = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_SEX)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_SEX)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_SEX);
                                                    String dematFirstJointDob = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_DATE_OF_BIRTH)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_DATE_OF_BIRTH)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_DATE_OF_BIRTH);
                                                    String dematFirstJointOcc = (object.isNull(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_OCCUPATION)
                                                        || object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_OCCUPATION)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_FIRST_JOINT_ACC_HOLDER_OCCUPATION);
                                                    String dematSecondAccTitle =
                                                        (object.isNull(DEMAT_SECOND_ACC_TITLE)
                                                            || object.getString(
                                                            DEMAT_SECOND_ACC_TITLE).equals("")) ? ""
                                                            : object.getString(
                                                                DEMAT_SECOND_ACC_TITLE);
                                                    String dematSecondJointFirstName =
                                                        (object.isNull(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_FNAME)
                                                            || object.getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_FNAME)
                                                            .equals("")) ? "" : object.getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_FNAME);
                                                    String dematSecondJointMiddleName =
                                                        (object.isNull(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_MNAME)
                                                            || object.getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_MNAME)
                                                            .equals("")) ? "" : object.getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_MNAME);
                                                    String dematSecondJointLastName =
                                                        (object.isNull(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_LNAME)
                                                            || object.getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_LNAME)
                                                            .equals("")) ? "" : object.getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_LNAME);
                                                    String dematSecondJointRelation =
                                                        (object.isNull(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_RELATION)
                                                            || object.getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_RELATION)
                                                            .equals("")) ? "" : object.getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_RELATION);
                                                    String dematSecondJointFHFName = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_FHFNAME)
                                                        || object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_FHFNAME)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_FHFNAME);
                                                    String dematSecondJointFHMName = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_FHMNAME)
                                                        || object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_FHMNAME)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_FHMNAME);
                                                    String dematSecondJointFHLName = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_FHLNAME)
                                                        || object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_FHLNAME)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_FHLNAME);
                                                    String dematSecondJointAdd1 = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_ADD1)
                                                        || object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_ADD1)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_ADD1);
                                                    String dematSecondJointAdd2 = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_ADD2)
                                                        || object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_ADD2)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_ADD2);
                                                    String dematSecondJointAdd3 = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_ADD3)
                                                        || object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_ADD3)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_ADD3);
                                                    String dematSecondJointLandmark =
                                                        (object.isNull(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_LANDMARK)
                                                            || object.getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_LANDMARK)
                                                            .equals("")) ? "" : object.getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_LANDMARK);
                                                    String dematSecondJointPincode = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_PIN) || object
                                                        .getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_PIN)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_PIN);
                                                    String dematSecondJointCity = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_CITY)
                                                        || object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_CITY)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_CITY);
                                                    String dematSecondJointState = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_STATE)
                                                        || object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_STATE)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_STATE);
                                                    String dematSecondJointCountry = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_COUNTRY)
                                                        || object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_COUNTRY)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_COUNTRY);
                                                    String dematSecondJointPan = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_PAN) || object
                                                        .getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_PAN)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_PAN);
                                                    String dematSecondJointSex = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_SEX) || object
                                                        .getString(
                                                            DEMAT_SECOND_JOINT_ACC_HOLDER_SEX)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_SEX);
                                                    String dematSecondJointDob = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_DATE_OF_BIRTH)
                                                        || object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_DATE_OF_BIRTH)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_DATE_OF_BIRTH);
                                                    String dematSecondJointOcc = (object.isNull(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_OCCUPATION)
                                                        || object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_OCCUPATION)
                                                        .equals("")) ? "" : object.getString(
                                                        DEMAT_SECOND_JOINT_ACC_HOLDER_OCCUPATION);
                                                    //                                                String numberOfJointHolder = (object.isNull(NUMBER_OF_JOINT_HOLDER) || object.getString(NUMBER_OF_JOINT_HOLDER).equals("")) ? "" : object.getString(NUMBER_OF_JOINT_HOLDER);
                                                    int numberOfJointHolder =
                                                        (object.isNull(NUMBER_OF_JOINT_HOLDER)
                                                            || object.getInt(NUMBER_OF_JOINT_HOLDER)
                                                            == 0) ? 0
                                                            : object.getInt(NUMBER_OF_JOINT_HOLDER);
                                                    String firstJointMaritalStatus =
                                                        (object.isNull(FJ_MARITAL_STATUS)
                                                            || object.getString(FJ_MARITAL_STATUS)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_MARITAL_STATUS);
                                                    String firstJointResidentialStatus =
                                                        (object.isNull(FJ_RESIDENTIAL_STATUS)
                                                            || object.getString(
                                                            FJ_RESIDENTIAL_STATUS).equals("")) ? ""
                                                            : object.getString(
                                                                FJ_RESIDENTIAL_STATUS);
                                                    String firstJointUID =
                                                        (object.isNull(FJ_UID) || object.getString(
                                                            FJ_UID).equals("")) ? ""
                                                            : object.getString(FJ_UID);
                                                    String firstJointEmail =
                                                        (object.isNull(FJ_EMAIL_ID)
                                                            || object.getString(FJ_EMAIL_ID)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_EMAIL_ID);
                                                    String firstJointTel = (object.isNull(FJ_TELENO)
                                                        || object.getString(FJ_TELENO).equals(""))
                                                        ? "" : object.getString(FJ_TELENO);
                                                    String firstJointAddProof =
                                                        (object.isNull(FJ_ADDRESSPROOF)
                                                            || object.getString(FJ_ADDRESSPROOF)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_ADDRESSPROOF);
                                                    String firstJointAddProofPerm =
                                                        (object.isNull(FJ_ADDRESSPROOF_PERM)
                                                            || object.getString(
                                                            FJ_ADDRESSPROOF_PERM).equals("")) ? ""
                                                            : object.getString(
                                                                FJ_ADDRESSPROOF_PERM);
                                                    String firstJointAnnualIncome =
                                                        (object.isNull(FJ_ANNUAL_INCOME)
                                                            || object.getString(FJ_ANNUAL_INCOME)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_ANNUAL_INCOME);
                                                    String firstJointPEP =
                                                        (object.isNull(FJ_PEP) || object.getString(
                                                            FJ_PEP).equals("")) ? ""
                                                            : object.getString(FJ_PEP);
                                                    String firstJointRPER =
                                                        (object.isNull(FJ_RPER) || object.getString(
                                                            FJ_RPER).equals("")) ? ""
                                                            : object.getString(FJ_RPER);
                                                    String secondJointMaritalStatus =
                                                        (object.isNull(SJ_MARITAL_STATUS)
                                                            || object.getString(SJ_MARITAL_STATUS)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_MARITAL_STATUS);
                                                    String secondJointResidentialStatus =
                                                        (object.isNull(SJ_RESIDENTIAL_STATUS)
                                                            || object.getString(
                                                            SJ_RESIDENTIAL_STATUS).equals("")) ? ""
                                                            : object.getString(
                                                                SJ_RESIDENTIAL_STATUS);
                                                    String secondJointUID =
                                                        (object.isNull(SJ_UID) || object.getString(
                                                            SJ_UID).equals("")) ? ""
                                                            : object.getString(SJ_UID);
                                                    String secondJointEmail =
                                                        (object.isNull(SJ_EMAIL_ID)
                                                            || object.getString(SJ_EMAIL_ID)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_EMAIL_ID);
                                                    String secondJointTel =
                                                        (object.isNull(SJ_TELENO)
                                                            || object.getString(SJ_TELENO)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_TELENO);
                                                    String secondJointAddProof =
                                                        (object.isNull(SJ_ADDRESSPROOF)
                                                            || object.getString(SJ_ADDRESSPROOF)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_ADDRESSPROOF);
                                                    String secondJointAddProofPerm =
                                                        (object.isNull(SJ_ADDRESSPROOF_PERM)
                                                            || object.getString(
                                                            SJ_ADDRESSPROOF_PERM).equals("")) ? ""
                                                            : object.getString(
                                                                SJ_ADDRESSPROOF_PERM);
                                                    String secondJointAnnualIncome =
                                                        (object.isNull(SJ_ANNUAL_INCOME)
                                                            || object.getString(SJ_ANNUAL_INCOME)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_ANNUAL_INCOME);
                                                    String secondJointPEP =
                                                        (object.isNull(SJ_PEP) || object.getString(
                                                            SJ_PEP).equals("")) ? ""
                                                            : object.getString(SJ_PEP);
                                                    String secondJointRPER =
                                                        (object.isNull(SJ_RPER) || object.getString(
                                                            SJ_RPER).equals("")) ? ""
                                                            : object.getString(SJ_RPER);
                                                    String firstJointMobile =
                                                        (object.isNull(FJ_MOBILE)
                                                            || object.getString(FJ_MOBILE)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_MOBILE);
                                                    String secondJointMobile =
                                                        (object.isNull(SJ_MOBILE)
                                                            || object.getString(SJ_MOBILE)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_MOBILE);

                                                    String firstJointAdd1Perm =
                                                        (object.isNull(FJ_ADD1_PERM)
                                                            || object.getString(FJ_ADD1_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_ADD1_PERM);
                                                    String firstJointAdd2Perm =
                                                        (object.isNull(FJ_ADD2_PERM)
                                                            || object.getString(FJ_ADD2_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_ADD2_PERM);
                                                    String firstJointAdd3Perm =
                                                        (object.isNull(FJ_ADD3_PERM)
                                                            || object.getString(FJ_ADD3_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_ADD3_PERM);
                                                    String firstJointLandmarkPerm =
                                                        (object.isNull(FJ_LANDMARK_PERM)
                                                            || object.getString(FJ_LANDMARK_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_LANDMARK_PERM);
                                                    String firstJointPincodePerm =
                                                        (object.isNull(FJ_PINCODE_PERM)
                                                            || object.getString(FJ_PINCODE_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_PINCODE_PERM);
                                                    String firstJointStatePerm =
                                                        (object.isNull(FJ_STATE_PERM)
                                                            || object.getString(FJ_STATE_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_STATE_PERM);
                                                    String firstJointCityPerm =
                                                        (object.isNull(FJ_CITY_PERM)
                                                            || object.getString(FJ_CITY_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_CITY_PERM);
                                                    String firstJointCountryPerm =
                                                        (object.isNull(FJ_COUNTRY_PERM)
                                                            || object.getString(FJ_COUNTRY_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(FJ_COUNTRY_PERM);
                                                    String secondJointAdd1Perm =
                                                        (object.isNull(SJ_ADD1_PERM)
                                                            || object.getString(SJ_ADD1_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_ADD1_PERM);
                                                    String secondJointAdd2Perm =
                                                        (object.isNull(SJ_ADD2_PERM)
                                                            || object.getString(SJ_ADD2_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_ADD2_PERM);
                                                    String secondJointAdd3Perm =
                                                        (object.isNull(SJ_ADD3_PERM)
                                                            || object.getString(SJ_ADD3_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_ADD3_PERM);
                                                    String secondJointPincodePerm =
                                                        (object.isNull(SJ_PINCODE_PERM)
                                                            || object.getString(SJ_PINCODE_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_PINCODE_PERM);
                                                    String secondJointStatePerm =
                                                        (object.isNull(SJ_STATE_PERM)
                                                            || object.getString(SJ_STATE_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_STATE_PERM);
                                                    String secondJointCityPerm =
                                                        (object.isNull(SJ_CITY_PERM)
                                                            || object.getString(SJ_CITY_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_CITY_PERM);
                                                    String secondJointCountryPerm =
                                                        (object.isNull(SJ_COUNTRY_PERM)
                                                            || object.getString(SJ_COUNTRY_PERM)
                                                            .equals("")) ? ""
                                                            : object.getString(SJ_COUNTRY_PERM);

                                                    if (numberOfJointHolder == 0) {
                                                        tvSecondHolderNotProvidedMsg.setVisibility(
                                                            View.VISIBLE);
                                                        tvThirdHolderNotProvidedMsg.setVisibility(
                                                            View.VISIBLE);
                                                        llSecondHolderLayoutDetail.setVisibility(
                                                            View.GONE);
                                                        llThirdHolderLayoutDetail.setVisibility(
                                                            View.GONE);
                                                    } else if (numberOfJointHolder == 1) {
                                                        tvSecondHolderNotProvidedMsg.setVisibility(
                                                            View.GONE);
                                                        tvThirdHolderNotProvidedMsg.setVisibility(
                                                            View.VISIBLE);
                                                        llSecondHolderLayoutDetail.setVisibility(
                                                            View.VISIBLE);
                                                        llThirdHolderLayoutDetail.setVisibility(
                                                            View.GONE);
                                                    } else {
                                                        tvSecondHolderNotProvidedMsg.setVisibility(
                                                            View.GONE);
                                                        tvThirdHolderNotProvidedMsg.setVisibility(
                                                            View.GONE);
                                                        llSecondHolderLayoutDetail.setVisibility(
                                                            View.VISIBLE);
                                                        llThirdHolderLayoutDetail.setVisibility(
                                                            View.VISIBLE);
                                                    }
                                                    llSecondHolderLayout.setVisibility(
                                                        View.VISIBLE);
                                                    llThirdHolderLayout.setVisibility(View.VISIBLE);
                                                    //                                            if(numberOfJointHolder)
                                                    tvSecHoldResidentStatus.setText(
                                                        firstJointResidentialStatus);
                                                    tvSecHoldName.setText(dematFirstJointFirstName
                                                        + " "
                                                        + dematFirstJointMiddleName
                                                        + " "
                                                        + dematFirstJointLastName);
                                                    tvSecHoldGender.setText(dematFirstJointSex);
                                                    tvSecHoldFatherorSpouseName.setText(
                                                        dematFirstJointFHFName
                                                            + " "
                                                            + dematFirstJointFHMName
                                                            + " "
                                                            + dematFirstJointFHLName);
                                                    tvSecHolddob.setText(dematFirstJointDob);
                                                    tvSecHoldMaritalStatus.setText(
                                                        firstJointMaritalStatus);
                                                    tvSecHoldPan.setText(dematFirstJointPan);
                                                    tvSecHoldMobile.setText(firstJointMobile);
                                                    if (firstJointUID.equalsIgnoreCase("")) {
                                                        tvSecHoldAadhaar.setText(firstJointUID);
                                                    } else {
                                                        //                                                    BigDecimal bd = new BigDecimal(firstJointUID);
                                                        //                                                    tvSecHoldAadhaar.setText(String.valueOf(bd.longValue()));
                                                        tvSecHoldAadhaar.setText(firstJointUID);
                                                    }
                                                    tvSecHoldEmail.setText(firstJointEmail);
                                                    tvSecHoldTel.setText(firstJointTel);
                                                    tvSecHoldCorrAddProof.setText(
                                                        firstJointAddProof);
                                                    tvSecHoldCorrBldgName.setText(
                                                        dematFirstJointAdd1);
                                                    tvSecHoldCorrFlatNo.setText(
                                                        dematFirstJointAdd2);
                                                    tvSecHoldCorrArea.setText(dematFirstJointAdd3);
                                                    tvSecHoldCorrPincode.setText(
                                                        dematFirstJointPincode);
                                                    tvSecHoldCorrState.setText(
                                                        dematFirstJointState);
                                                    tvSecHoldCorrCity.setText(dematFirstJointCity);
                                                    tvSecHoldPermAddProof.setText(
                                                        firstJointAddProofPerm);
                                                    tvSecHoldCorrCountry.setText(
                                                        dematFirstJointCountry);
                                                    tvSecHoldPermBldgName.setText(
                                                        firstJointAdd1Perm);
                                                    tvSecHoldPermFlatNo.setText(firstJointAdd2Perm);
                                                    tvSecHoldPermArea.setText(firstJointAdd3Perm);
                                                    tvSecHoldPermPincode.setText(
                                                        firstJointPincodePerm);
                                                    tvSecHoldPermState.setText(firstJointStatePerm);
                                                    tvSecHoldPermCity.setText(firstJointCityPerm);
                                                    tvSecHoldPermCountry.setText(
                                                        firstJointCountryPerm);
                                                    tvSecHoldAnnualIncome.setText(
                                                        firstJointAnnualIncome);
                                                    tvSecHoldOccDetail.setText(dematFirstJointOcc);
                                                    tvSecHoldPoliticalExposedPerson.setText(
                                                        firstJointPEP);
                                                    tvSecHoldRelatedPoliticalExposedPerson.setText(
                                                        firstJointRPER);
                                                    tvThirdHoldResidentStatus.setText(
                                                        secondJointResidentialStatus);
                                                    tvThirdHoldName.setText(
                                                        dematSecondJointFirstName
                                                            + " "
                                                            + dematSecondJointMiddleName
                                                            + " "
                                                            + dematSecondJointLastName);
                                                    tvThirdHoldGender.setText(dematSecondJointSex);
                                                    tvThirdHoldFatherorSpouseName.setText(
                                                        dematSecondJointFHFName
                                                            + " "
                                                            + dematSecondJointFHMName
                                                            + " "
                                                            + dematSecondJointFHLName);
                                                    tvThirdHolddob.setText(dematSecondJointDob);
                                                    tvThirdHoldMaritalStatus.setText(
                                                        secondJointMaritalStatus);
                                                    tvThirdHoldPan.setText(dematSecondJointPan);
                                                    if (secondJointUID.equalsIgnoreCase("")) {
                                                        tvThirdHoldAadhaar.setText(secondJointUID);
                                                    } else {
                                                        //                                                    BigDecimal bd = new BigDecimal(secondJointUID);
                                                        //                                                    tvThirdHoldAadhaar.setText(String.valueOf(bd.longValue()));
                                                        tvThirdHoldAadhaar.setText(secondJointUID);
                                                    }
                                                    tvThirdHoldMobile.setText(secondJointMobile);
                                                    tvThirdHoldEmail.setText(secondJointEmail);
                                                    tvThirdHoldTel.setText(secondJointTel);
                                                    tvThirdHoldCorrAddProof.setText(
                                                        secondJointAddProof);
                                                    tvThirdHoldCorrBldgName.setText(
                                                        dematSecondJointAdd1);
                                                    tvThirdHoldCorrPincode.setText(
                                                        dematSecondJointPincode);
                                                    tvThirdHoldCorrFlatNo.setText(
                                                        dematSecondJointAdd2);
                                                    tvThirdHoldCorrArea.setText(
                                                        dematSecondJointAdd3);
                                                    tvThirdHoldCorrState.setText(
                                                        dematSecondJointState);
                                                    tvThirdHoldCorrCity.setText(
                                                        dematSecondJointCity);
                                                    tvThirdHoldCorrCountry.setText(
                                                        dematSecondJointCountry);
                                                    tvThirdHoldPermAddProof.setText(
                                                        secondJointAddProofPerm);
                                                    tvThirdHoldPermBldgName.setText(
                                                        secondJointAdd1Perm);
                                                    tvThirdHoldPermFlatNo.setText(
                                                        secondJointAdd2Perm);
                                                    tvThirdHoldPermArea.setText(
                                                        secondJointAdd3Perm);
                                                    tvThirdHoldPermPincode.setText(
                                                        secondJointPincodePerm);
                                                    tvThirdHoldPermState.setText(
                                                        secondJointStatePerm);
                                                    tvThirdHoldPermCity.setText(
                                                        secondJointCityPerm);
                                                    tvThirdHoldPermCountry.setText(
                                                        secondJointCountryPerm);
                                                    tvThirdHoldAnnualIncome.setText(
                                                        secondJointAnnualIncome);
                                                    tvThirdHoldOccDetail.setText(
                                                        dematSecondJointOcc);
                                                    tvThirdHoldPoliticalExposedPerson.setText(
                                                        secondJointPEP);
                                                    tvThirdHoldRelatedPoliticalExposedPerson.setText(
                                                        secondJointRPER);
                                                }
                                            }
                                        }
                                    }
                                    if (response.has("ReviewDetails11")) {
                                        JSONArray jsonArray =
                                            response.getJSONArray("ReviewDetails11");
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                String nomineeFName =
                                                    (object.isNull(NOMINEE_FNAME_REVIEW)
                                                        || object.getString(NOMINEE_FNAME_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_FNAME_REVIEW);
                                                String nomineeMName =
                                                    (object.isNull(NOMINEE_MNAME_REVIEW)
                                                        || object.getString(NOMINEE_MNAME_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_MNAME_REVIEW);
                                                String nomineeLName =
                                                    (object.isNull(NOMINEE_LNAME_REVIEW)
                                                        || object.getString(NOMINEE_LNAME_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_LNAME_REVIEW);
                                                String nomineeAdd1 =
                                                    (object.isNull(NOMINEE_ADD1_REVIEW)
                                                        || object.getString(NOMINEE_ADD1_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_ADD1_REVIEW);
                                                String nomineeAdd2 =
                                                    (object.isNull(NOMINEE_ADD2_REVIEW)
                                                        || object.getString(NOMINEE_ADD2_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_ADD2_REVIEW);
                                                String nomineeAdd3 =
                                                    (object.isNull(NOMINEE_ADD3_REVIEW)
                                                        || object.getString(NOMINEE_ADD3_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_ADD3_REVIEW);
                                                String nomineeLandmark =
                                                    (object.isNull(NOMINEE_LANDMARK_REVIEW)
                                                        || object.getString(NOMINEE_LANDMARK_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_LANDMARK_REVIEW);
                                                String nomineeCity =
                                                    (object.isNull(NOMINEE_CITY_REVIEW)
                                                        || object.getString(NOMINEE_CITY_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_CITY_REVIEW);
                                                String nomineeState =
                                                    (object.isNull(NOMINEE_STATE_REVIEW)
                                                        || object.getString(NOMINEE_STATE_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_STATE_REVIEW);
                                                String nomineePin =
                                                    (object.isNull(NOMINEE_PIN_REVIEW)
                                                        || object.getString(NOMINEE_PIN_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_PIN_REVIEW);
                                                String nomineeCountry =
                                                    (object.isNull(NOMINEE_COUNTRY_REVIEW) || object
                                                        .getString(NOMINEE_COUNTRY_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_COUNTRY_REVIEW);
                                                String nomineeDOB =
                                                    (object.isNull(NOMINEE_DATE_OF_BIRTH_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_DATE_OF_BIRTH_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        NOMINEE_DATE_OF_BIRTH_REVIEW);
                                                String nomineeTele =
                                                    (object.isNull(NOMINEE_TELE_REVIEW)
                                                        || object.getString(NOMINEE_TELE_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_TELE_REVIEW);
                                                String nomineeRel =
                                                    (object.isNull(NOMINEE_REL_BO_REVIEW)
                                                        || object.getString(NOMINEE_REL_BO_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_REL_BO_REVIEW);
                                                String nomineeMinorFName =
                                                    (object.isNull(NOMINEE_MINOR_FNAME_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_FNAME_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_FNAME_REVIEW);
                                                String nomineeMinorMName =
                                                    (object.isNull(NOMINEE_MINOR_MNAME_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_MNAME_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_MNAME_REVIEW);
                                                String nomineeMinorLName =
                                                    (object.isNull(NOMINEE_MINOR_LNAME_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_LNAME_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_LNAME_REVIEW);
                                                String nomineeMinorAdd1 =
                                                    (object.isNull(NOMINEE_MINOR_ADD1_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_ADD1_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_ADD1_REVIEW);
                                                String nomineeMinorAdd2 =
                                                    (object.isNull(NOMINEE_MINOR_ADD2_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_ADD2_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_ADD2_REVIEW);
                                                String nomineeMinorAdd3 =
                                                    (object.isNull(NOMINEE_MINOR_ADD3_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_ADD3_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_ADD3_REVIEW);
                                                String nomineeMinorLandmark =
                                                    (object.isNull(NOMINEE_MINOR_LANDMARK_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_LANDMARK_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        NOMINEE_MINOR_LANDMARK_REVIEW);
                                                String nomineeMinorCity =
                                                    (object.isNull(NOMINEE_MINOR_CITY_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_CITY_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_CITY_REVIEW);
                                                String nomineeMinorState =
                                                    (object.isNull(NOMINEE_MINOR_STATE_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_STATE_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_STATE_REVIEW);
                                                String nomineeMinorPin =
                                                    (object.isNull(NOMINEE_MINOR_PIN_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_PIN_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_PIN_REVIEW);
                                                String nomineeMinorCountry =
                                                    (object.isNull(NOMINEE_MINOR_COUNTRY_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_COUNTRY_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        NOMINEE_MINOR_COUNTRY_REVIEW);
                                                String sameAsCorresNominee =
                                                    (object.isNull(SAME_AS_CORRES_NOMINEE_REVIEW)
                                                        || object.getString(
                                                        SAME_AS_CORRES_NOMINEE_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        SAME_AS_CORRES_NOMINEE_REVIEW);
                                                String sameAsCorresPerm =
                                                    (object.isNull(SAME_AS_CORRES_PERM_REVIEW)
                                                        || object.getString(
                                                        SAME_AS_CORRES_PERM_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            SAME_AS_CORRES_PERM_REVIEW);
                                                String sameAsCorresNomineeGuardian = (object.isNull(
                                                    SAME_AS_CORRES_NOMINEE_GUARDIAN_REVIEW)
                                                    || object.getString(
                                                    SAME_AS_CORRES_NOMINEE_GUARDIAN_REVIEW)
                                                    .equals("")) ? "" : object.getString(
                                                    SAME_AS_CORRES_NOMINEE_GUARDIAN_REVIEW);
                                                String otherCityNominee =
                                                    (object.isNull(OTHER_CITY_NOMINEE_REVIEW)
                                                        || object.getString(
                                                        OTHER_CITY_NOMINEE_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            OTHER_CITY_NOMINEE_REVIEW);
                                                String otherCityNomineeMinor =
                                                    (object.isNull(OTHER_CITY_NOMINEE_MINOR_REVIEW)
                                                        || object.getString(
                                                        OTHER_CITY_NOMINEE_MINOR_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        OTHER_CITY_NOMINEE_MINOR_REVIEW);
                                                String nomineeDeclaration =
                                                    (object.isNull(NOMINEE_DECLARATION_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_DECLARATION_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_DECLARATION_REVIEW);
                                                String nomineeGuardianAge =
                                                    (object.isNull(NOMINEE_GUARDIAN_AGE_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_GUARDIAN_AGE_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        NOMINEE_GUARDIAN_AGE_REVIEW);
                                                String nomineeGuardianRelation =
                                                    (object.isNull(NOMINEE_GUARDIAN_RELATION_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_GUARDIAN_RELATION_REVIEW)
                                                        .equals("")) ? "" : object.getString(
                                                        NOMINEE_GUARDIAN_RELATION_REVIEW);
                                                String nomineeApplicantRelation = (object.isNull(
                                                    NOMINEE_APPLICANT_RELATION_REVIEW)
                                                    || object.getString(
                                                    NOMINEE_APPLICANT_RELATION_REVIEW).equals(""))
                                                    ? "" : object.getString(
                                                    NOMINEE_APPLICANT_RELATION_REVIEW);
                                                String nomineeEmail =
                                                    (object.isNull(NOMINEE_EMAIL_ID_REVIEW)
                                                        || object.getString(NOMINEE_EMAIL_ID_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_EMAIL_ID_REVIEW);
                                                String nomineeMinorEmail =
                                                    (object.isNull(NOMINEE_MINOR_EMAIL_ID_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_EMAIL_ID_REVIEW).equals(""))
                                                        ? "" : object.getString(
                                                        NOMINEE_MINOR_EMAIL_ID_REVIEW);
                                                String nomineeMinorTelno =
                                                    (object.isNull(NOMINEE_MINOR_TELNO_REVIEW)
                                                        || object.getString(
                                                        NOMINEE_MINOR_TELNO_REVIEW).equals("")) ? ""
                                                        : object.getString(
                                                            NOMINEE_MINOR_TELNO_REVIEW);
                                                String nomineePAN =
                                                    (object.isNull(NOMINEE_PAN_REVIEW)
                                                        || object.getString(NOMINEE_PAN_REVIEW)
                                                        .equals("")) ? ""
                                                        : object.getString(NOMINEE_PAN_REVIEW);
                                                String nomineeAadhar =
                                                    (object.isNull(NOMINEE_UID) || object.getString(
                                                        NOMINEE_UID).equals("")) ? ""
                                                        : object.getString(NOMINEE_UID);

                                                if (nomineeDeclaration.equalsIgnoreCase(NO)) {
                                                    tvNomineeDetailMsg.setVisibility(View.VISIBLE);
                                                    llNomineeDetail.setVisibility(View.GONE);
                                                } else if (nomineeDeclaration.equalsIgnoreCase(
                                                    YES)) {
                                                    llNomineeDetail.setVisibility(View.VISIBLE);
                                                    tvNomineeDetailMsg.setVisibility(View.GONE);
                                                }
                                                tvNomineeName.setText(nomineeFName
                                                    + " "
                                                    + nomineeMName
                                                    + " "
                                                    + nomineeLName);
                                                tvNomineeDOB.setText(nomineeDOB);
                                                tvNomineePAN.setText(nomineePAN);
                                                tvNomineeAadhaar.setText(nomineeAadhar);
                                                tvNomineeEmail.setText(nomineeEmail);
                                                tvNomineeTelNo.setText(nomineeTele);
                                                tvNomineeRelationship.setText(nomineeRel);
                                                //                                            tvNomineeApplicantRelationship.setText(nomineeApplicantRelation);
                                                tvNomineeFlatNoCorres.setText(nomineeAdd2);
                                                tvNomineeBldgNoCorres.setText(nomineeAdd1);
                                                tvNomineeAreaCorres.setText(nomineeAdd3);
                                                tvNomineeCityCorres.setText(nomineeCity);
                                                tvNomineeStateCorres.setText(nomineeState);
                                                tvNomineePincodeCorres.setText(nomineePin);
                                                tvNomineeCountryCorres.setText(nomineeCountry);

                                                Calendar minAdultAge = new GregorianCalendar();
                                                minAdultAge.add(Calendar.YEAR, -18);
                                                Date adultAge = EkycAppUtil.getDate(
                                                    dateFormatter.format(minAdultAge.getTime()),
                                                    "dd/MM/yyyy");
                                                Date dateDOB =
                                                    EkycAppUtil.getDate(nomineeDOB, "dd/MM/yyyy");
                                                if (!isBlank(nomineeDOB) && adultAge.before(
                                                    dateDOB)) {
                                                    llNomineeGuardianDetails.setVisibility(
                                                        View.VISIBLE);
                                                    tvNomineeGuardianName.setText(nomineeMinorFName
                                                        + " "
                                                        + nomineeMinorMName
                                                        + " "
                                                        + nomineeMinorLName);
                                                    tvNomineeGuardianEmail.setText(
                                                        nomineeMinorEmail);
                                                    tvNomineeGuardianTelNo.setText(
                                                        nomineeMinorTelno);
                                                    tvNomineeGuardianAge.setText(
                                                        nomineeGuardianAge);
                                                    tvNomineeGuardianFlatNoCorres.setText(
                                                        nomineeMinorAdd2);
                                                    tvNomineeGuardianBldgNoCorres.setText(
                                                        nomineeMinorAdd1);
                                                    tvNomineeGuardianAreaCorres.setText(
                                                        nomineeMinorAdd3);
                                                    tvNomineeGuardianCityCorres.setText(
                                                        nomineeMinorCity);
                                                    tvNomineeGuardianStateCorres.setText(
                                                        nomineeMinorState);
                                                    tvNomineeGuardianPincodeCorres.setText(
                                                        nomineeMinorPin);
                                                    tvNomineeGuardianCountryCorres.setText(
                                                        nomineeMinorCountry);
                                                    tvNomineeGuardianRelationship.setText(
                                                        nomineeGuardianRelation);
                                                } else {
                                                    llNomineeGuardianDetails.setVisibility(
                                                        View.GONE);
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            llCustomProgress.setVisibility(View.GONE);
                        } else {
                            llCustomProgress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        llCustomProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        llCustomProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        llCustomProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        llCustomProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        llCustomProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });
        } else {
            MOSLCommon.alertDInternet(mContext);
            llCustomProgress.setVisibility(View.GONE);
        }
        dialogReview.show();
        reviewDialogIsShowing = true;
        dialogReview.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    dialogReview.dismiss();
                    reviewDialogIsShowing = false;
                    backToBank();
                    /*if (llEquityBankDetail.getVisibility() != View.VISIBLE ||
                            llEquityBankScheme.getVisibility() != View.VISIBLE ||
                            llCommodityBankDetail.getVisibility() != View.VISIBLE ||
                            llCommodityBankScheme.getVisibility() != View.VISIBLE ||
                            llMFBankDetail.getVisibility() != View.VISIBLE) {
                        if ((isEquityOriginal() && isCommodityOriginal()) || isAllOriginal()) {
                            showBothBankDetails();
                            getSchemeDetails(getAppType(), userID);
                        } else if (isEquityOriginal() || isDerivativeOriginal()) {
                            showEquityBankDetails();
                            getSchemeDetails(getAppType(), userID);
                        } else if (isCommodityOriginal()) {
                            showCommdityBankDetails();
                            getSchemeDetails(getAppType(), userID);
                        } else if (getInterestedInMF()) {
                            showMFBankDetails();
                        }
                    }*/

                }
                return true;
            }
        });

    }

    public void showMFBankDetails() {
        llMFBankDetail.setVisibility(View.VISIBLE);
        llFundTransferNote.setVisibility(View.GONE);
        fillMFBankDropDown();
    }

    private void backToBank() {
        if (llEquityBankDetail.getVisibility() != View.VISIBLE ||
            llEquityBankScheme.getVisibility() != View.VISIBLE ||
            llCommodityBankDetail.getVisibility() != View.VISIBLE ||
            llCommodityBankScheme.getVisibility() != View.VISIBLE ||
            llMFBankDetail.getVisibility() != View.VISIBLE) {
            if ((isEquityOriginal() && isCommodityOriginal()) || isAllOriginal()) {
                showBothBankDetails();
                getSchemeDetails(getAppType(), userID);
            } else if (isEquityOriginal() || isDerivativeOriginal()) {
                showEquityBankDetails();
                getSchemeDetails(getAppType(), userID);
            } else if (isCommodityOriginal()) {
                showCommdityBankDetails();
                getSchemeDetails(getAppType(), userID);
            } else if (getInterestedInMF()) {
                showMFBankDetails();
            }
        }
    }

    private void tcNotAcceptedDialog() {
        CustomDialog cdTC =
            new CustomDialog(mContext, TERMS_COND_NOT_ACCEPTED, true, false, false, "", OK, "",
                new CustomDialog.CustomDialogListener() {
                    @Override
                    public void dialogAction(MOSLCommon.Action action, Object... data) {

                    }
                });
        cdTC.show();
    }

    private void termsConditionDialog(String product) {
        final Dialog termConditionDialog = new Dialog(mContext);
        termConditionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (product.equalsIgnoreCase(BROKING)) {
            termConditionDialog.setContentView(R.layout.frag_term_and_condition_review_edit);
        } else if (product.equalsIgnoreCase(MF)) {
            termConditionDialog.setContentView(R.layout.frag_term_and_condition_review_edit_mf);
        }
        termConditionDialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        termConditionDialog.setCanceledOnTouchOutside(false);
        Button btnClose = (Button) termConditionDialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termConditionDialog.dismiss();
            }
        });
        termConditionDialog.show();
    }

    private void updateReviewDetail() {

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            //            inputMap.put("userid", "1593361");
            //            inputMap.put(APPLICATION_TYPE, PP);
            inputMap.put("userid", getUserID());
            if (getAppType().equalsIgnoreCase(PP)) {
                inputMap.put(APPLICATION_TYPE, PP);
            } else if (getAppType().equalsIgnoreCase(DIRECT)) {
                inputMap.put(APPLICATION_TYPE, DIRECT);
            } else if (getAppType().equalsIgnoreCase(CRM)) {
                inputMap.put(APPLICATION_TYPE, CRM);
            }

            String flag = UPDATEREVIEWDETAILS;
            String category = REVIEWDETAILS;

            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (statusCode == 200) {
                            if (response.length() != 0) {
                                try {
                                    String status = (response.isNull(PRODUCT_REVIEW_STATUS)
                                        || response.getString(PRODUCT_REVIEW_STATUS).equals(""))
                                        ? "" : response.getString(PRODUCT_REVIEW_STATUS);
                                    String msg =
                                        (response.isNull(PRODUCT_REVIEW_MSG) || response.getString(
                                            PRODUCT_REVIEW_MSG).equals("")) ? ""
                                            : response.getString(PRODUCT_REVIEW_MSG);

                                    if (status.equalsIgnoreCase("1")) {
                                        dialogReview.dismiss();
                                        reviewDialogIsShowing = false;
                                        if (getInterestedInMF()) {
                                            tabPageIndicator.enableTab(getUploadTab());
                                            viewPager.setCurrentItem(getUploadTab(), false);
                                        } else if (getEqSchemeId().equalsIgnoreCase(MF_CUM_EQUITY)
                                            && !isCommodityOriginal()) {
                                            tabPageIndicator.enableTab(getUploadTab());
                                            viewPager.setCurrentItem(getUploadTab(), false);
                                        } else {
                                            getPaymentStatus();
                                        }
                                    } else {
                                        commonMsgDialog(mContext, msg, false, "");
                                        customProgress.setVisibility(View.GONE);
                                        //                                    MOSLCommon.showToastMsg(mContext, msg);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY,
                                        false, "");

                                }
                            } else {
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                                customProgress.setVisibility(View.GONE);
                            }

                            customProgress.setVisibility(View.GONE);
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    public void getPaymentStatus() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("_userId", userID);
            //            inputMap.put("_segmentType", userID);

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
                                                    navigateToFundTransfer(
                                                        (int) bdEqPaymentStatus.longValue(),
                                                        (int) bdCommPaymentStatus.longValue());
                                                } else {
                                                    //                                                MOSLCommon.showToastMsg(mContext, "Status is null");
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
                                                    navigateToFundTransfer(
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
                                                    navigateToFundTransfer(4,
                                                        (int) bdCommPaymentStatus.longValue());
                                                }
                                            }


                                        }
                                        customProgress.setVisibility(View.GONE);
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                customProgress.setVisibility(View.GONE);
                            }

                        } else {
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                            //                        MOSLCommon.showToastMsg(mContext, NO_RESPONSE);
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
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });

        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void navigateToFundTransfer(int equityStatus, int commodityStatus) {
        llFundTransfer.setVisibility(View.VISIBLE);
        llBankDetailForm.setVisibility(View.GONE);
        customProgress.setVisibility(View.GONE);
        fragFundTransfer = (FundTransferFragment) getChildFragmentManager().findFragmentById(
            R.id.fragFundTransfer);
        fragFundTransfer.setTabPageIndicator(tabPageIndicator);
        fragFundTransfer.replaceNetBankingFragment();


    }

    //set Equity bank info Review and edit
    private void setEquityData(int position) {
        if (position == 0) {
            tvEqBankNameReview.setText(bankNameReview);
            tvEqMICRReview.setText(micrCodeReview);
            tvEqIFSCReview.setText(ifscCodeReview);
            tvEqBranchNameReview.setText(bankBranchReview);
            tvEqBankAddressReview.setText(bankAddReview);
            tvEqAccNumberReview.setText(accNumberReview);
            tvEqAccTypeReview.setText(accTypeReview);
            //            tvEqPhotoProofReview.setText(photoProofReview);
            //            tvEqBankProofReview.setText(bankProofReview);
            tvEqProductReview.setText(productReview);
        } else if (position == 1) {
            tvEqBankNameReview.setText(bankNameReviewFirst);
            tvEqMICRReview.setText(micrCodeReviewFirst);
            tvEqIFSCReview.setText(ifscCodeReviewFirst);
            tvEqBranchNameReview.setText(bankBranchReviewFirst);
            tvEqBankAddressReview.setText(bankAddReviewFirst);
            tvEqAccNumberReview.setText(accNumberReviewFirst);
            tvEqAccTypeReview.setText(accTypeReviewFirst);
            //            tvEqPhotoProofReview.setText(photoProofReviewFirst);
            //            tvEqBankProofReview.setText(bankProofReviewFirst);
            tvEqProductReview.setText(productReviewFirst);
        }
    }

    //set Commodity bank info Review and edit
    private void setCommodityData(int position) {
        if (position == 0) {
            tvCommBankNameReview.setText(bankNameReview);
            tvCommMICRReview.setText(micrCodeReview);
            tvCommIFSCReview.setText(ifscCodeReview);
            tvCommBranchNameReview.setText(bankBranchReview);
            tvCommBankAddressReview.setText(bankAddReview);
            tvCommAccNumberReview.setText(accNumberReview);
            tvCommAccTypeReview.setText(accTypeReview);
            //            tvCommPhotoProofReview.setText(photoProofReview);
            //            tvCommBankProofReview.setText(bankProofReview);
            tvCommProductReview.setText(productReview);
        } else if (position == 1) {
            tvCommBankNameReview.setText(bankNameReviewFirst);
            tvCommMICRReview.setText(micrCodeReviewFirst);
            tvCommIFSCReview.setText(ifscCodeReviewFirst);
            tvCommBranchNameReview.setText(bankBranchReviewFirst);
            tvCommBankAddressReview.setText(bankAddReviewFirst);
            tvCommAccNumberReview.setText(accNumberReviewFirst);
            tvCommAccTypeReview.setText(accTypeReviewFirst);
            //            tvCommPhotoProofReview.setText(photoProofReviewFirst);
            //            tvCommBankProofReview.setText(bankProofReviewFirst);
            tvCommProductReview.setText(productReviewFirst);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser
                && bankDetailFormView != null
                && viewPager.getCurrentItem() == getBankTab()) {
                setPermisson(getActivity(), mContext);
                disableEnableField();
                showBankDetailForm();
                /*if (MOSLCommon.fundTransferStatus()) {
                    btnBankAndSchemeUpdate.setVisibility(View.GONE);
                } else {
                    btnBankAndSchemeUpdate.setVisibility(View.VISIBLE);
                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            //            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            //            Crashlytics.logException(e);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void downloadPDF(String resumeLink) {
        try {
            String nameOfFile = URLUtil.guessFileName(String.valueOf(resumeLink), null,
                MimeTypeMap.getFileExtensionFromUrl(String.valueOf(resumeLink)));
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(resumeLink));
            request.setTitle(nameOfFile);
            request.setDescription("download");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile);
            DownloadManager manager =
                (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);


        } catch (Exception ex) {
            commonMsgDialog(mContext, SOME_ERROR_OCCURED_DURING_DOWNLOAD, false, "");
            //            MOSLCommon.alertDialog(mContext, 0, "Motilal Oswal", SOME_ERROR_OCCURED_DURING_DOWNLOAD, "OK", true, null);
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

    private void equityPaid() {
        disableField(llEquityBankDetail);
        disableField(llEquityBankScheme);
        if (getAppType().equalsIgnoreCase(PP)) {
            if (!isThisSecondProductFlow()
                && !isIpvORDocumentStageDone()) {//enable scheme if IPV pending
                enableField(llEquityBankScheme);
            }
            if (!isEquitySegmentExist()) {
                if (isDerivativeOriginal()) {
                    spEquOption.setEnabled(true);
                    spEquFuture.setEnabled(true);
                } else {
                    spEquOption.setEnabled(false);
                    spEquFuture.setEnabled(false);
                }
                if (isCurrencyOriginal()) {
                    spEquCurrencyFuture.setEnabled(true);
                    spEquCurrencyOption.setEnabled(true);
                } else {
                    spEquCurrencyFuture.setEnabled(false);
                    spEquCurrencyOption.setEnabled(false);
                }
            } else {
                spEquOption.setEnabled(false);
                spEquFuture.setEnabled(false);
                spEquCurrencyFuture.setEnabled(false);
                spEquCurrencyOption.setEnabled(false);
            }
        }
        tvEquSchemeTermCondition.setEnabled(true);
        tvEquValuePackTermCondition.setEnabled(true);
    }

    public void disableEnableField() {
        if (getInterestedInMF() && (isMfIpvDone() || isOfflineDocumentUploadedDone())) {
            mfIpvDone();
        } else {
            mfIpvPending();
        }

        if (!isEquitySegmentExist() && !isEquityFundTransfered()) {
            showEquitypending();
        } else {
            equityPaid();
        }

        if (!isCommoditySegmentExist() && !isCommodityFundTransfered()) {
            showCommPending();
        } else {
            commodityPaid();
        }
    }

    private void mfEquitySchemeSelected() {
        disableField(llEquityBankScheme);
        tvEquSchemeTermCondition.setEnabled(true);
        tvEquValuePackTermCondition.setEnabled(true);
    }

    private void commodityPaid() {
        disableField(llEquityBankDetail);
        disableField(llCommodityBankDetail);
        disableField(llCommodityBankScheme);
        if (getAppType().equalsIgnoreCase(PP)) {
            if (!isThisSecondProductFlow()
                && !isIpvORDocumentStageDone()) {//enable scheme if IPV pending
                enableField(llCommodityBankScheme);
            }
        }
        tvCommSchemeTermCondition.setEnabled(true);
        if (!getAppType().equalsIgnoreCase(PP) && isEquityOriginal()) {
            disableField(llEquityBankScheme);
        }
        tvEquSchemeTermCondition.setEnabled(true);
        tvEquValuePackTermCondition.setEnabled(true);
    }

    private void showEquitypending() {
        enableField(llEquityBankDetail);
        enableField(llEquityBankScheme);
        if (!strEquSchID.equals("9999")) {
            etEquMarginDeposit.setEnabled(false);
        }
    }

    private void showCommPending() {
        if (!isFundTransferDone()) {
            enableField(llEquityBankDetail);
        }
        enableField(llCommodityBankDetail);
        enableField(llCommodityBankScheme);
        if (!strComSchID.equals("9999") || isEquityOriginal()) {
            etCommMarginDeposit.setEnabled(false);
        }
    }

    private void currencyFutureShowRadioButton(List<SchemeDetailsModel> detailsModelList) {
        if (detailsModelList.size() != 0) {
            RadioGroup.LayoutParams rprms;
            currencyfutureRadiogroup.removeAllViews();
            currencyfutureRadiogroup.setOrientation(RadioGroup.HORIZONTAL);
            currencyfutureRadiogroup.setVisibility(View.VISIBLE);
            RadioButton[] rb = new RadioButton[detailsModelList.size()];
            for (int j = 0; j < detailsModelList.size(); j++) {
                rb[j] = new RadioButton(getActivity());
                rprms = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                currencyfutureRadiogroup.addView(rb[j], rprms);
                rb[j].setText(detailsModelList.get(j).getBrok_VAL());
                if (!detailsModelList.get(j).getCurrFutureSelectedVal().equals("0") &&
                    detailsModelList.get(j)
                        .getCurrFutureSelectedVal()
                        .equals(detailsModelList.get(j).getBrok_VAL())) {
                    rb[j].setChecked(true);
                }
            }

            for (int j = 0; j < detailsModelList.size(); j++) {
                if (detailsModelList.get(j).getCurrFutureSelectedVal().equals("0")) {
                    rb[j].setChecked(true);
                }
            }
            currencyfutureRadiogroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                        tvequSchInfoCurrFuture.setText(radioButton.getText().toString());
                    }
                });
        }
    }

    private void mfIpvDone() {
        disableField(llMFBankDetail);
        tvMFTC.setEnabled(true);
    }

    private void mfIpvPending() {
        enableField(llMFBankDetail);
    }

    private void currOptionShowRadioButton(List<SchemeDetailsModel> schemeDetailsModelList) {
        if (schemeDetailsModelList.size() != 0) {
            RadioGroup.LayoutParams rprms;
            currencyoptionRadiogroup.removeAllViews();
            currencyoptionRadiogroup.setOrientation(RadioGroup.HORIZONTAL);
            currencyoptionRadiogroup.setVisibility(View.VISIBLE);
            RadioButton[] rb = new RadioButton[schemeDetailsModelList.size()];
            for (int j = 0; j < schemeDetailsModelList.size(); j++) {
                rb[j] = new RadioButton(getActivity());
                rprms = new RadioGroup.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                currencyoptionRadiogroup.addView(rb[j], rprms);
                rb[j].setText(schemeDetailsModelList.get(j).getBrok_VAL());
                if (!schemeDetailsModelList.get(j).getCurrOptionSelectedVal().equals("0") &&
                    schemeDetailsModelList.get(j)
                        .getCurrOptionSelectedVal()
                        .equals(schemeDetailsModelList.get(j).getBrok_VAL())) {
                    rb[j].setChecked(true);
                }
            }

            for (int j = 0; j < schemeDetailsModelList.size(); j++) {
                if (schemeDetailsModelList.get(j).getCurrOptionSelectedVal().equals("0")) {
                    rb[j].setChecked(true);
                }
            }
            currencyoptionRadiogroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                        tvequSchInfoCurrOption.setText(radioButton.getText().toString());
                    }
                });
            disableEnableField();
        }
    }

    public void getDpSchemeDetails(final String accountOpeningFee, String schemeSelectedID) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap();
            inputMap.put("_userId", getUserID());
            inputMap.put("_SchemeID", schemeSelectedID);

            String flag = GETDPSCHEMEDETAILS;
            String category = SCHEMEDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        arrDpScheme = new ArrayList<>();
                        if (response != null && response.length() != 0) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("GETDPSCHEMEDETAILS");
                                if (jsonArray.length() != 0) {
                                    arrDpScheme.add(new RowData(0, "-1", "Select"));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String id =
                                            (object.isNull("Profile_ID") || object.getString(
                                                "Profile_ID").equals("")) ? ""
                                                : object.getString("Profile_ID");
                                        String name =
                                            (object.isNull("PROFILE_NAME") || object.getString(
                                                "PROFILE_NAME").equals("")) ? ""
                                                : object.getString("PROFILE_NAME");
                                        arrDpScheme.add(new RowData((i + 1), id, name));
                                    }
                                    arrAdapDpScheme = new ArrayAdapter<>(mContext,
                                        R.layout.simple_spinner_item, arrDpScheme);
                                    arrAdapDpScheme.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                    spDpScheme.setAdapter(arrAdapDpScheme);
                                    spDpScheme.setOnItemSelectedListener(dpSchemeListener);
                                    RowData dpScheme =
                                        RowData.getSpinnerRowDataByValue(accountOpeningFee,
                                            arrAdapDpScheme);
                                    if (dpScheme != null) {
                                        spDpScheme.setSelection(dpScheme.getPosition());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONObject errorResponse) {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                        JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                });
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    public class BankBranchAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        Context mContext;
        List<BankBranchModel> branchModelList;
        private ArrayList<BankBranchModel> arraylist;

        public BankBranchAdapter(Context c, List<BankBranchModel> branchModelList) {
            inflater = LayoutInflater.from(c);
            mContext = c;
            this.branchModelList = branchModelList;
            this.arraylist = new ArrayList<BankBranchModel>();
            this.arraylist.addAll(branchModelList);
        }

        @Override
        public int getCount() {
            return branchModelList.size();
        }

        @Override
        public Object getItem(int position) {
            return branchModelList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder = new Holder();
            view = inflater.inflate(R.layout.item_list_bank_branch_new, parent, false);

            BankBranchModel bankBranchModel = branchModelList.get(position);

            holder.tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            holder.tvBranchName = (TextView) view.findViewById(R.id.tvBranchName);
            holder.tvBankName = (TextView) view.findViewById(R.id.tvBankName);
            holder.tvMICR = (TextView) view.findViewById(R.id.tvMICR);
            holder.tvIFSC = (TextView) view.findViewById(R.id.tvIFSC);
            holder.tvEqOnlineBankFlag = (TextView) view.findViewById(R.id.tvEqOnlineBankFlag);
            holder.tvCommOnlineBankFlag = (TextView) view.findViewById(R.id.tvCommOnlineBankFlag);

            holder.tvBankName.setText(bankBranchModel.getBankName());
            holder.tvBranchName.setText(bankBranchModel.getBranchName());
            holder.tvIFSC.setText(bankBranchModel.getIfscCode());
            holder.tvMICR.setText(bankBranchModel.getMicrCode());
            holder.tvAddress.setText(bankBranchModel.getBankAdd());
            holder.tvEqOnlineBankFlag.setText(bankBranchModel.getEqOnlineBankFlag());
            holder.tvCommOnlineBankFlag.setText(bankBranchModel.getCommOnlineBankFlag());

            return view;
        }


        class Holder {
            TextView tvBankName, tvAddress, tvBranchName, tvMICR, tvIFSC, tvEqOnlineBankFlag,
                tvCommOnlineBankFlag;
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            branchModelList.clear();
            if (charText.length() == 0) {
                branchModelList.addAll(arraylist);
            } else {
                for (BankBranchModel wp : arraylist) {
                    if (wp.getBranchName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchModelList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    private void setCommodityScheme() {
        if (strComSchID.equals("0")) {
            etCommMarginDeposit.setEnabled(false);
            etCommMarginDeposit.setText("");
            tvCommDelBrok.setText("");
            tvCommIntradayBrok.setText("");
        } else if (strComSchID.equals("9999")) {
            if (!isCommOtherSchSelected) {
                etCommMarginDeposit.setText("");
            } else {
                etCommMarginDeposit.setText(getComMarginDeposit());
                getOtherCommSchDetail();
                isCommOtherSchSelected = false;
            }
            if (!isEquityOriginal()) {
                btnGetCommScheme.setVisibility(View.VISIBLE);
                etCommMarginDeposit.setEnabled(true);
            }
            bindCommDetailsOnSchemeSelection(strComSchID);
        } else {
            etCommMarginDeposit.setEnabled(false);
            etCommMarginDeposit.setText(commSchMarDep);
            btnGetCommScheme.setVisibility(View.GONE);
            bindCommDetailsOnSchemeSelection(strComSchID);
        }
            /*if (strCommSchSelID.equals("9999")) {
                btnGetCommScheme.setVisibility(View.VISIBLE);
                etCommMarginDeposit.setEnabled(true);
                getOtherCommSchDetail();
            }*/
        if (getAppType().equals(DIRECT)) {
            if (strCommSchKey.equalsIgnoreCase("-1")) {
                etCommMarginDepositError.setVisibility(View.GONE);
                btnCommMarginSelectedScheme.setVisibility(View.GONE);
            } else {
                //btnCommMarginSelectedScheme.setVisibility(View.VISIBLE);
                btnSelectedSchemeDetailVisibilty();
            }
        }
    }

    public class MFBankListAdapter extends BaseAdapter implements MOSLConstants {
        private final LayoutInflater inflater;
        Context mContext;
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        private RadioButton selected = null;

        public MFBankListAdapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
            inflater = LayoutInflater.from(c);
            mContext = c;
            this.arrayList = arrayList;
        }

        private void selectItem(int posNum) {
            for (HashMap<String, String> item : arrayList) {
                item.put(MOSLConstants.DEFAULT_FLAG, "0");
            }
            arrayList.get(posNum).put(MOSLConstants.DEFAULT_FLAG, "1");
            notifyDataSetChanged();
        }

        public String getSelectedItemCDBID() {
            for (HashMap<String, String> item : arrayList) {
                if (item.get(MOSLConstants.DEFAULT_FLAG).equals("1")) {
                    return item.get(MOSLConstants.CBD_ID);
                }
            }
            return "";
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;

            view = inflater.inflate(R.layout.item_list_mf_bank_saved, parent, false);
            final Holder holder = new Holder();
            holder.tvBankName = (TextView) view.findViewById(R.id.tvBankName);
            holder.tvBranchName = (TextView) view.findViewById(R.id.tvBranchName);
            holder.tvAccNo = (TextView) view.findViewById(R.id.tvAccNo);
            holder.tvMICR = (TextView) view.findViewById(R.id.tvMICR);
            holder.tvIFSC = (TextView) view.findViewById(R.id.tvIFSC);
            holder.llItemMFBankDetail = (LinearLayout) view.findViewById(R.id.llItemMFBankDetail);
            holder.rbBankSelect = (RadioButton) view.findViewById(rbBankSelect);
            holder.ivBankDelete = (ImageView) view.findViewById(R.id.ivBankDelete);

            if (position % 2 == 1) {
                holder.llItemMFBankDetail.setBackgroundColor(
                    getResources().getColor(R.color.scheme_grey));
            } else {
                holder.llItemMFBankDetail.setBackgroundColor(Color.parseColor("#f8f6f7"));
            }
            final String cbdID = arrayList.get(position).get(CBD_ID).equals("") ? ""
                : arrayList.get(position).get(CBD_ID);
            final String bankName = arrayList.get(position).get(BANK_NAME).equals("") ? ""
                : arrayList.get(position).get(BANK_NAME);
            final String branchName = arrayList.get(position).get(BRANCH_NAME).equals("") ? ""
                : arrayList.get(position).get(BRANCH_NAME);
            final String bankAdd = arrayList.get(position).get(BANK_ADDRESS).equals("") ? ""
                : arrayList.get(position).get(BANK_ADDRESS);
            final String accNum = arrayList.get(position).get(BANK_ACC_NUM).equals("") ? ""
                : arrayList.get(position).get(BANK_ACC_NUM);
            final String abnkAccType = arrayList.get(position).get(BANK_ACC_TYPE).equals("") ? ""
                : arrayList.get(position).get(BANK_ACC_TYPE);
            final String micr = arrayList.get(position).get(MOSLConstants.MICR).equals("") ? ""
                : arrayList.get(position).get(MOSLConstants.MICR);
            final String ifsc = arrayList.get(position).get(MOSLConstants.IFSC).equals("") ? ""
                : arrayList.get(position).get(MOSLConstants.IFSC);
            final String defaultFlag =
                arrayList.get(position).get(MOSLConstants.DEFAULT_FLAG).equals("") ? ""
                    : arrayList.get(position).get(MOSLConstants.DEFAULT_FLAG);
            String checkFlag = arrayList.get(position).get(MOSLConstants.CHECK_FLAG).equals("") ? ""
                : arrayList.get(position).get(MOSLConstants.CHECK_FLAG);
            String status = arrayList.get(position).get(MOSLConstants.STATUS).equals("") ? ""
                : arrayList.get(position).get(MOSLConstants.STATUS);
            String message = arrayList.get(position).get(MOSLConstants.MESSAGE).equals("") ? ""
                : arrayList.get(position).get(MOSLConstants.MESSAGE);

            holder.tvBankName.setText(bankName);
            holder.tvBranchName.setText(branchName);
            holder.tvAccNo.setText(accNum);
            holder.tvMICR.setText(micr);
            holder.tvIFSC.setText(ifsc);

            if (Integer.valueOf(defaultFlag) == 1) {
                holder.rbBankSelect.setChecked(true);
                //                selectedBankcbdID = cbdID;
                //                selectedBankcbdID = cbdID;

                selectedBankAccType = abnkAccType;
            }
            /*else {
                selectedBankcbdID = "";
                holder.rbBankSelect.setChecked(false);
            }*/
            if (arrayList.size() == 1) {
                selectItem(position);
                // holder.rbBankSelect.setChecked(true);
            }
            if ((getInterestedInMF() && isMfIpvDone())) {
                holder.rbBankSelect.setEnabled(false);
                holder.ivBankDelete.setEnabled(false);
            } else {
                holder.rbBankSelect.setEnabled(true);
                holder.ivBankDelete.setEnabled(true);
            }
            holder.ivBankDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final CustomDialog cdDelete =
                        new CustomDialog(mContext, BANK_DELETE_CONFIRM + bankName, true, false,
                            false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                            @Override
                            public void dialogAction(MOSLCommon.Action action, Object... data) {
                                if (action == MOSLCommon.Action.OK) {
                                    insertDeleteBankDetail("D", cbdID, bankName, branchName,
                                        bankAdd, accNum, micr, ifsc);

                                }
                            }
                        });
                    cdDelete.show();
                }
            });

            /*holder.rbBankSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });*/
            holder.rbBankSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (selected != null) {
                        selected.setChecked(false);
                    }
                    if (defaultFlag.equals("1")) {
                        holder.rbBankSelect.setChecked(false);
                    }
                    holder.rbBankSelect.setChecked(true);
                    selected = holder.rbBankSelect;
                    if (holder.rbBankSelect.isChecked()) {
                        selectedBankcbdID = cbdID;
                    } else {
                        selectedBankcbdID = "";
                    }*/

                    selectItem(position);
                }
            });

            return view;
        }

        class Holder {
            TextView tvBankName, tvBranchName, tvAccNo, tvMICR, tvIFSC;
            LinearLayout llItemMFBankDetail;
            RadioButton rbBankSelect;
            ImageView ivBankDelete;
        }
    }

    private void btnSelectedSchemeDetailVisibilty() {
        if (spEquScheme.getSelectedItem().toString().equalsIgnoreCase("Other amount")) {
            btnMarginSelectedScheme.setVisibility(View.GONE);
            btnCommMarginSelectedScheme.setVisibility(View.GONE);
        } else {
            btnMarginSelectedScheme.setVisibility(View.VISIBLE);
            btnCommMarginSelectedScheme.setVisibility(View.VISIBLE);
        }
    }
}

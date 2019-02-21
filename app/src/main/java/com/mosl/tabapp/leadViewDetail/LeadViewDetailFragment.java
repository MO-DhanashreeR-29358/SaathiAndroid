package com.mosl.tabapp.leadViewDetail;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.common.ValidationRule;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.CRMValidationRule;
import com.mosl.tabapp.common.CustomDatePickerDialog;
import com.mosl.tabapp.common.CustomListPickerDialog;
import com.mosl.tabapp.common.DatePickerListener;
import com.mosl.tabapp.common.MSFStatistics;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.common.SearchableSpinner;
import com.mosl.tabapp.common.StringStuff;
import com.mosl.tabapp.common.UiModel;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.AddressDetailModel;
import com.mosl.tabapp.model.ContactDetailModel;
import com.mosl.tabapp.model.EmployeeModel;
import com.mosl.tabapp.model.LeadFieldSectionModel;
import com.mosl.tabapp.model.LeadFieldValueModel;
import com.mosl.tabapp.model.leaddetail.CrossSellingProductsModel;
import com.mosl.tabapp.model.leaddetail.LeadDetailAddProductModel;
import com.mosl.tabapp.model.leaddetail.LeadDetailModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import com.mosl.tabapp.ws.RefreeSyncWebServiceCall;
import cz.msebera.android.httpclient.Header;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.droidparts.widget.ClearableEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mosl.mobile.ekyc.constants.MOSLConstants.ErrorMsgName;
import static com.mosl.tabapp.common.CRMAppUtil.filterOnlyAlphabetAndNumbersAnsAllowedChar;
import static com.mosl.tabapp.common.CRMAppUtil.getDBDateFrom;
import static com.mosl.tabapp.common.CRMAppUtil.getDateFromString;
import static com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.common.CRMMOSLCommon.disableField;
import static com.mosl.tabapp.common.CRMMOSLCommon.setListViewHeightBasedOnChildrenDetail;
import static com.mosl.tabapp.common.CRMValidationRule.isValidEmail;
import static com.mosl.tabapp.common.CRMValidationRule.validateAadhar;
import static com.mosl.tabapp.common.CRMValidationRule.validateTel;
import static com.mosl.tabapp.common.RowData.getSpinnerRowDataById;
import static com.mosl.tabapp.common.UiModelHelper.applyUiModel;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Vijay on 10/03/2017.
 */

public class LeadViewDetailFragment extends MOSLCommonFragment
    implements View.OnClickListener, MOSLConstants {

    final public static String TAG = LeadViewDetailFragment.class.getSimpleName();
    public static final String SectionTagPrefix = "Section";
    public static final String TextBox = "TextBox";
    public static final String DropDown = "DropDown";
    private static String ucid, mLeadId;
    private LinearLayout NewDataLinearLayout;
    private View leadViewDetailView;
    private ViewPager viewPager;
    private TextView tvClientDetail, tvClientPortfolioDetails, tvLeadDetail, tvDocuments;
    private LinearLayout llClient, llClientPortfolioDetails, llClientDetail, llClientPortfolio,
        llLeadDetail, llEditLeadDetail;
    private LinearLayout customProgress;
    private TextView tvClientCode, tvClientName, tvClientAddress, tvChannel, tvDefaultBank,
        tvPanCard, tvAccountType, tvUCID, tvFamilyUCID, tvBranchCode, tvAdvisoryType,
        tvAccountNumber, tvAadharCard, tvProfile;
    private EditText etLeadID, etLeadCategory, etLastModifiedBy, etPmsProduct, etEkycMobileNumber,
        etPanNumber,
        etAadharNumber, etLeadName, etCreationDate, etCreatedBy, etSource, etProspectSource,
        etProspectSourceDetail, etCreatedThrough, etMobileNumber, etLastModifiedDate,
        etLandlineNumber, etEmail, etRefereeCode, etRefereeName, etRefereeEmail, etRefereeMobile,
        etAlternateMobileNumber, etAlternateEmail, etContactPerson, etComments;
    private Spinner spProfile, spCountry, spOffCountry, spProfession;
    private org.droidparts.widget.ClearableEditText etDob, etAnniversary;
    private TextView tvAnniversaryError;
    private TextView tvspIntrestError, tvspStatusError, tvCrossSellingProductIdError,
        tvCrossSellingStatusError, tvspStageError;
    private RelativeLayout rlAddProduct;
    private Button btnSubmit;
    private TextInputLayout ilLeadName;
    private String
        interestInCodeStr, stageCodeStr, newStageCodeStr, newStageNameStr, countryCodeStr,
        stateCodeStr, cityCodeStr,
        offCountryCodeStr, offStateCodeStr,
        professionCodeStr = "", profileCodeStr;
    private ArrayList<RowData> arrProfession, arrSelectProfile,
        arrInterested, arrProductType, arrStage, arrStatus, arrCrossSellingStatus,
        arrCrossSellingProductId, arrCountry, arrState, arrCity, arrOffState, arrOffCity;
    private ArrayAdapter<RowData> arrAdapProfession, arrAdapProfile, arrAdapInterested,
        arrAdapProductType, arrAdapStage, arrAdapStatus,
        arrAdapCrossSellingStatus, arrAdapCrossSellingProductId, arrAdapCountry, arrAdapState,
        arrAdapCity, arrAdapOffState, arrAdapOffCity;
    private String interestedIn, sourceID, leadProspetSourceID,
        primaryRm, stageId, statusId, creationDate, profile, contactPerson, profession;
    private ArrayList<ContactDetailModel> contactDetailList = new ArrayList<>();
    private ArrayList<AddressDetailModel> addressDetailList = new ArrayList<>();
    private ListView lvAddProduct;
    private ArrayList<LeadDetailAddProductModel> leadDetailAddProductList;
    private LeadDetailAddProductAdapter leadDetailAddProductAdapter;
    private String specialistAdd = "";
    private String specialistEdit = "";
    private String primaryRMAdd = "";
    private String primaryRMEdit = "", advisorCode = "";
    private TextView tvLandLineError, tvAlternateMobileNumberError, tvEmailIdError,
        tvAlternateEmailIdError, tvPincodeError, tvOffPincodeError, tvNameError,
        tvContactPersonError, tvPanNumError, tvAadharNumError;
    private NestedScrollView nestedScrollView;
    private List<LeadFieldSectionModel> mLeadFieldSectionModelList;
    private Map<String, View> newSectionMap;
    private CheckBox cbPrimaryAddress, cbSameAs, cbOffPrimaryAddress;
    private SearchableSpinner spState, spCity, spOffState, spOffCity;
    private EditText etAdd1, etAdd2, etAdd3, etPincode, etOffAdd1, etOffAdd2, etOffAdd3,
        etOffPincode;
    private String stateStr, cityStr, offStateStr, offCityStr, offAdd1, offAdd2, offAdd3,
        offPincode, offCountryId;
    private LinearLayout llResAdd, llOffAdd;
    private LinearLayout llPortfolio, llProfitLoss, llAvailableLimits, llBrokingPortfolio,
        llAssestPortfolio, llCash;
    private LinearLayout llBrokingProducts, llAssestProducts, llProfile;
    private String outerSectionExpansion = "OuterSectionExpansion", innerSectionExpansion =
        "InnerSectionExpansion";
    private TextView txtNetworth, txtDaychgamt, txtDaychgperst, txtOverallchgamt,
        txt_overallchgperst, tvSTGainLoss, tvTotalGainLoss, tvSTGainLossPer, tvLTGainLoss,
        tvLTGainLossPer;
    private Button btnBrokPortfolio, btnAssetPortfolio;
    private HashMap<String, String> brokingMap, assestMap, cashMap, brokPLHashMap, assetPLHashMap,
        limitsPLHashMap;
    private ArrayList<HashMap<String, String>> arrayList_broking, arrayList_cash, arrayList_asset,
        arrLstPLBrok, arrLstPLAsset, arrLstPLLimits;
    private ListView lvBrok, lvAsset, lvCash, lvPLBrok, lvPLAsset, lvPLLimits;
    private TextView txtPortfolioSnapshot, txtPorfitLoss, txtAvailableLimits, txtBrokingPortfolio,
        txtAssestPortfolio, txtCash;
    private TextView txtBrokingProducts, txtAssestProducts;
    private boolean isIndividualsFlag;
    private boolean editableFlag = true;
    private String refereeCode, refereeName;
    private boolean isRefreeCode;
    private boolean isLowMargin;
    private List<LowMargin> lowMarginList = new ArrayList<>();
    private ProgressBar pbLoadingIndicatorRefreeCode, pbLoadingIndicatorRefreeName;
    private boolean stateFlag, officeStateFlag;
    private String leadOwner = "";

    private boolean leadEditable = true, isPotentialPMSProduct = false;

    public boolean isLeadEditable() {
        return leadEditable;
    }

    public void setLeadEditable(boolean leadEditable) {
        this.leadEditable = leadEditable;
    }

    private AdapterView.OnItemSelectedListener profileListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    profileCodeStr = ((RowData) spProfile.getSelectedItem()).getKey();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    private AdapterView.OnItemSelectedListener professionListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    professionCodeStr = ((RowData) spProfession.getSelectedItem()).getKey();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    private AdapterView.OnItemSelectedListener cityListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    cityCodeStr = ((RowData) spCity.getSelectedItem()).getKey();
                    cityStr = cityCodeStr;
                    if (cbSameAs.isChecked()) {
                        RowData dataCity = RowData.getSpinnerRowDataById(cityCodeStr, arrAdapCity);
                        if (dataCity != null) {
                            spOffCity.setSelection(dataCity.getPosition());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    private AdapterView.OnItemSelectedListener offStateListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    offStateCodeStr = ((RowData) spOffState.getSelectedItem()).getKey();
                    if (!offStateCodeStr.equalsIgnoreCase("-1") && !cbSameAs.isChecked()) {
                        getCity(offStateCodeStr, OFFICE, offCityStr);
                    } else {
                        arrOffCity = new ArrayList();
                        arrOffCity.add(new RowData(0, "-1", "Select"));
                        arrAdapOffCity =
                            new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
                                arrOffCity);
                        arrAdapOffCity.setDropDownViewResource(R.layout.mdropdown);
                        spOffCity.setAdapter(arrAdapOffCity);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener stateListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    stateCodeStr = ((RowData) spState.getSelectedItem()).getKey();
                    arrCity = new ArrayList<>();
                    arrCity.add(new RowData(0, "-1", "Select"));
                    arrAdapCity =
                        new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
                            arrCity);
                    arrAdapCity.setDropDownViewResource(R.layout.mdropdown);
                    spCity.setAdapter(arrAdapCity);
                    if (!stateCodeStr.equalsIgnoreCase("-1")) {
                        getCity(stateCodeStr, RESIDENCE, cityStr);
                    } else {
                        arrCity = new ArrayList();
                        arrCity.add(new RowData(0, "-1", "Select"));
                        arrAdapCity =
                            new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
                                arrCity);
                        arrAdapCity.setDropDownViewResource(R.layout.mdropdown);
                        spCity.setAdapter(arrAdapCity);
                    }
                    if (cbSameAs.isChecked()) {
                        RowData dataState =
                            RowData.getSpinnerRowDataById(stateCodeStr, arrAdapState);
                        if (dataState != null) {
                            spOffState.setSelection(dataState.getPosition());
                            spOffState.setOnItemSelectedListener(offStateListener);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener offCountryListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    offCountryCodeStr = ((RowData) spOffCountry.getSelectedItem()).getKey();
                    if (officeStateFlag) {
                        getState(offCountryCodeStr, OFFICE, null);
                    } else {
                        officeStateFlag = true;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener countryListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    countryCodeStr = ((RowData) spCountry.getSelectedItem()).getKey();
                    if (stateFlag) {
                        if (!countryCodeStr.equalsIgnoreCase("-1")) {
                            getState(countryCodeStr, RESIDENCE, null);
                        }
                    } else {
                        stateFlag = true;
                    }
                    if (cbSameAs.isChecked()) {
                        RowData dataCountry =
                            RowData.getSpinnerRowDataById(countryCodeStr, arrAdapCountry);
                        if (dataCountry != null) {
                            spOffCountry.setSelection(dataCountry.getPosition());
                            spOffCountry.setOnItemSelectedListener(offCountryListener);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    public static LeadViewDetailFragment newInstance(int position, String type, String leadID,
        String Ucid) {
        LeadViewDetailFragment fragment = new LeadViewDetailFragment();
        Bundle args = new Bundle();
        args.putInt("Position", position);
        args.putString("Type", type);
        mLeadId = leadID;
        ucid = Ucid;
        args.putString(LEAD_ID, leadID);
        args.putString(UCID, Ucid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        leadViewDetailView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_leadview_detail);
        setUpView(leadViewDetailView);
        setUserVisibleHint(true);
        return leadViewDetailView;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && leadViewDetailView != null && viewPager.getCurrentItem() == 0) {
                if (!this.getArguments().getString(LEAD_ID).equalsIgnoreCase("0")) {
                    setEnableDisable(false);
                    leadSection();
                } else {
                    clientSection();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEnableDisable(boolean flag) {
        etLeadName.setEnabled(flag);
        etPanNumber.setEnabled(flag);
        etAadharNumber.setEnabled(flag);
        etContactPerson.setEnabled(flag);
        etLandlineNumber.setEnabled(flag);
        etRefereeCode.setEnabled(flag);
        etRefereeName.setEnabled(flag);
        etLandlineNumber.setEnabled(flag);
        etDob.setEnabled(flag);
        etAnniversary.setEnabled(flag);
        etAlternateMobileNumber.setEnabled(flag);
        etAlternateEmail.setEnabled(flag);
        etEmail.setEnabled(flag);
        etContactPerson.setEnabled(flag);
        etComments.setEnabled(flag);
        btnSubmit.setEnabled(flag);
        spProfile.setEnabled(flag);
        spProfession.setEnabled(flag);
        cbSameAs.setEnabled(flag);
        cbPrimaryAddress.setEnabled(flag);
        cbOffPrimaryAddress.setEnabled(flag);
        if (flag) {
            if (cbPrimaryAddress.isChecked()) {
                enableField(llResAdd);
                enableField(llOffAdd);
            }
            if (cbOffPrimaryAddress.isChecked()) {
                enableField(llOffAdd);
                enableField(llResAdd);
            }
        } else {
            disableField(llOffAdd);
            disableField(llResAdd);
        }

        if (mLeadFieldSectionModelList != null) {
            for (LeadFieldSectionModel mSection : mLeadFieldSectionModelList) {
                if (mSection.getSelectionList() != null) {
                    for (LeadFieldValueModel modal : mSection.getSelectionList()) {
                        Object cObj = modal.getControlObject();
                        if (cObj != null) {
                            if (cObj instanceof EditText) {
                                ((EditText) cObj).setEnabled(flag);
                            } else if (cObj instanceof Spinner) {
                                ((Spinner) cObj).setEnabled(flag);
                            }
                        }
                    }
                }
            }
        }
    }

    private void leadSection() {
        getInterestIn();
        getCountry();
        selectProfile();
        getLeadDetail();
        getProfession();
        btnSubmit.setEnabled(false);
    }

    private void clientSection() {
        getCountry();
        selectProfile();
        setEnableDisable(false);
        getInterestIn();
        llClient.setVisibility(View.VISIBLE);
        llClientPortfolioDetails.setVisibility(View.VISIBLE);
        webServiceportfolioSnapshot(this.getArguments().getString(UCID));
        webServicePL(this.getArguments().getString(UCID));
        getClientDetail(Integer.parseInt(this.getArguments().getString(UCID)));
        toggleSectionExpansion(llClientDetail, tvClientDetail);
    }

    private void setUpView(View leadViewDetailView) {
        nestedScrollView = leadViewDetailView.findViewById(R.id.nestedScrollView);
        nestedScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        nestedScrollView.setFocusable(true);
        nestedScrollView.setFocusableInTouchMode(true);
        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        lvAddProduct = leadViewDetailView.findViewById(R.id.lvAddProduct);
        btnSubmit = leadViewDetailView.findViewById(R.id.btnSubmit);
        llEditLeadDetail = leadViewDetailView.findViewById(R.id.llEditLeadDetail);
        llClientDetail = leadViewDetailView.findViewById(R.id.llClientDetail);
        llClientPortfolio = leadViewDetailView.findViewById(R.id.llClientPortfolio);
        llClient = leadViewDetailView.findViewById(R.id.llClient);
        llClientPortfolioDetails =
            leadViewDetailView.findViewById(R.id.llClientPortfolioDetails);
        llLeadDetail = leadViewDetailView.findViewById(R.id.llLeadDetail);
        llResAdd = leadViewDetailView.findViewById(R.id.llResAdd);
        llOffAdd = leadViewDetailView.findViewById(R.id.llOffAdd);
        rlAddProduct = leadViewDetailView.findViewById(R.id.rlAddProduct);

        tvClientDetail = leadViewDetailView.findViewById(R.id.tvClientDetail);
        tvClientPortfolioDetails =
            leadViewDetailView.findViewById(R.id.tvClientPortfolioDetails);
        tvLeadDetail = leadViewDetailView.findViewById(R.id.tvLeadDetail);
        tvDocuments = leadViewDetailView.findViewById(R.id.tvDocuments);

        tvClientCode = leadViewDetailView.findViewById(R.id.tvClientCode);
        tvClientName = leadViewDetailView.findViewById(R.id.tvClientName);
        tvClientAddress = leadViewDetailView.findViewById(R.id.tvClientAddress);
        tvChannel = leadViewDetailView.findViewById(R.id.tvChannel);
        tvDefaultBank = leadViewDetailView.findViewById(R.id.tvDefaultBank);
        tvPanCard = leadViewDetailView.findViewById(R.id.tvPanCard);
        tvAccountType = leadViewDetailView.findViewById(R.id.tvAccountType);
        tvUCID = leadViewDetailView.findViewById(R.id.tvUCID);
        tvFamilyUCID = leadViewDetailView.findViewById(R.id.tvFamilyUCID);
        tvBranchCode = leadViewDetailView.findViewById(R.id.tvBranchCode);
        tvAdvisoryType = leadViewDetailView.findViewById(R.id.tvAdvisoryType);
        tvAccountNumber = leadViewDetailView.findViewById(R.id.tvAccountNumber);
        tvAadharCard = leadViewDetailView.findViewById(R.id.tvAadharCard);
        tvProfile = leadViewDetailView.findViewById(R.id.tvProfile);
        llProfile = leadViewDetailView.findViewById(R.id.llProfile);
        tvAnniversaryError = leadViewDetailView.findViewById(R.id.tvAnniversaryError);
        etLeadID = leadViewDetailView.findViewById(R.id.etLeadID);
        etLeadCategory = leadViewDetailView.findViewById(R.id.etLeadCategory);
        etLastModifiedBy = leadViewDetailView.findViewById(R.id.etLastModifiedBy);
        etEkycMobileNumber = leadViewDetailView.findViewById(R.id.etEkycMobileNumber);
        etPanNumber = leadViewDetailView.findViewById(R.id.etPanNumber);
        etAadharNumber = leadViewDetailView.findViewById(R.id.etAadharNumber);
        etLeadName = leadViewDetailView.findViewById(R.id.etLeadName);
        etPmsProduct = leadViewDetailView.findViewById(R.id.etPmsProduct);
        etCreationDate = leadViewDetailView.findViewById(R.id.etCreationDate);
        etCreatedBy = leadViewDetailView.findViewById(R.id.etCreatedBy);
        etSource = leadViewDetailView.findViewById(R.id.etSource);
        etProspectSource = leadViewDetailView.findViewById(R.id.etProspectSource);
        etProspectSourceDetail =
            leadViewDetailView.findViewById(R.id.etProspectSourceDetail);
        etCreatedThrough = leadViewDetailView.findViewById(R.id.etCreatedThrough);
        spProfile = leadViewDetailView.findViewById(R.id.spProfile);
        spProfession = leadViewDetailView.findViewById(R.id.spProfession);
        etMobileNumber = leadViewDetailView.findViewById(R.id.etMobileNumber);
        etLandlineNumber = leadViewDetailView.findViewById(R.id.etLandlineNumber);
        etEmail = leadViewDetailView.findViewById(R.id.etEmail);
        etRefereeCode = leadViewDetailView.findViewById(R.id.etRefereeCode);
        etRefereeCode.setFilters(new InputFilter[] {
            CRMAppUtil.filterOnlyAlphabetAndNumbers, new InputFilter.AllCaps()
        });
        pbLoadingIndicatorRefreeCode =
            leadViewDetailView.findViewById(R.id.pbLoadingIndicatorRefreeCode);
        pbLoadingIndicatorRefreeName =
            leadViewDetailView.findViewById(R.id.pbLoadingIndicatorRefreeName);
        etRefereeName = leadViewDetailView.findViewById(R.id.etRefereeName);
        etRefereeEmail = leadViewDetailView.findViewById(R.id.etRefereeEmail);
        etRefereeMobile = leadViewDetailView.findViewById(R.id.etRefereeNumber);
        etDob = leadViewDetailView.findViewById(R.id.etDob);
        etAnniversary = leadViewDetailView.findViewById(R.id.etAnniversary);
        etLastModifiedDate = leadViewDetailView.findViewById(R.id.etLastModifiedDate);
        etAlternateMobileNumber =
            leadViewDetailView.findViewById(R.id.etAlternateMobileNumber);
        etAlternateEmail = leadViewDetailView.findViewById(R.id.etAlternateEmail);
        etContactPerson = leadViewDetailView.findViewById(R.id.etContactPerson);

        etContactPerson.setFilters(new InputFilter[] { CRMAppUtil.filterOnlyAlphabetAndNumbers });
        etRefereeName.setFilters(new InputFilter[] { CRMAppUtil.filterOnlyAlphabetAndNumbers });
        etLeadName.setFilters(new InputFilter[] { CRMAppUtil.filterOnlyAlphabetAndNumbers });

        ilLeadName = leadViewDetailView.findViewById(R.id.ilLeadName);
        NewDataLinearLayout =
            leadViewDetailView.findViewById(R.id.newDataLinearLayout);

        etAdd1 = leadViewDetailView.findViewById(R.id.etAdd1);
        etAdd1.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
        etAdd2 = leadViewDetailView.findViewById(R.id.etAdd2);
        etAdd2.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
        etAdd3 = leadViewDetailView.findViewById(R.id.etAdd3);
        etAdd3.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
        etPincode = leadViewDetailView.findViewById(R.id.etPincode);

        etOffAdd1 = leadViewDetailView.findViewById(R.id.etOffAdd1);
        etOffAdd1.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
        etOffAdd2 = leadViewDetailView.findViewById(R.id.etOffAdd2);
        etOffAdd2.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
        etOffAdd3 = leadViewDetailView.findViewById(R.id.etOffAdd3);
        etOffAdd3.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
        etOffPincode = leadViewDetailView.findViewById(R.id.etOffPincode);
        etComments = leadViewDetailView.findViewById(R.id.etComments);
        etComments.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });

        spCity = leadViewDetailView.findViewById(R.id.spCity);
        spState = leadViewDetailView.findViewById(R.id.spState);
        spCountry = leadViewDetailView.findViewById(R.id.spCountry);
        spOffCity = leadViewDetailView.findViewById(R.id.spOffCity);
        spOffState = leadViewDetailView.findViewById(R.id.spOffState);
        spOffCountry = leadViewDetailView.findViewById(R.id.spOffCountry);

        cbPrimaryAddress = leadViewDetailView.findViewById(R.id.cbPrimaryAddress);
        cbOffPrimaryAddress = leadViewDetailView.findViewById(R.id.cbOffPrimaryAddress);
        cbSameAs = leadViewDetailView.findViewById(R.id.cbSameAs);

        setIcon(llClientDetail, tvClientDetail);
        setIcon(llLeadDetail, tvLeadDetail);
        setIcon(llClientPortfolio, tvClientPortfolioDetails);

        etDob.setInputType(InputType.TYPE_NULL);
        etAnniversary.setInputType(InputType.TYPE_NULL);
        cbPrimaryAddress.setOnClickListener(this);
        cbOffPrimaryAddress.setOnClickListener(this);
        cbSameAs.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        llProfile.setOnClickListener(this);
        rlAddProduct.setOnClickListener(this);
        llEditLeadDetail.setOnClickListener(this);
        tvClientDetail.setOnClickListener(this);
        tvClientPortfolioDetails.setOnClickListener(this);
        tvLeadDetail.setOnClickListener(this);
        tvDocuments.setOnClickListener(this);
        etDob.setOnClickListener(this);
        etAnniversary.setOnClickListener(this);
        tvLandLineError = leadViewDetailView.findViewById(R.id.tvLandLineError);
        tvNameError = leadViewDetailView.findViewById(R.id.tvNameError);
        tvContactPersonError =
            leadViewDetailView.findViewById(R.id.tvContactPersonError);
        tvPanNumError = leadViewDetailView.findViewById(R.id.tvPanNumError);
        tvAadharNumError = leadViewDetailView.findViewById(R.id.tvAadharNumError);
        tvAlternateMobileNumberError =
            leadViewDetailView.findViewById(R.id.tvAlternateMobileNumberError);
        tvAlternateEmailIdError =
            leadViewDetailView.findViewById(R.id.tvAlternateEmailIdError);
        tvEmailIdError = leadViewDetailView.findViewById(R.id.tvEmailIdError);
        tvPincodeError = leadViewDetailView.findViewById(R.id.tvPincodeError);
        tvOffPincodeError = leadViewDetailView.findViewById(R.id.tvOffPincodeError);

        etRefereeCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (etRefereeCode.getText().toString().length() < 4) {
                    refereeCode = "";
                    refereeName = "";
                    etRefereeName.setText("");
                    etRefereeEmail.setText("");
                    etRefereeMobile.setText("");
                }
            }
        });
        etRefereeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (etRefereeName.getText().toString().length() < 3) {
                    refereeCode = "";
                    refereeName = "";
                    etRefereeCode.setText("");
                    etRefereeEmail.setText("");
                    etRefereeMobile.setText("");
                }
            }
        });

        etLeadName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isBlank(etLeadName.getText().toString().trim())) {
                    tvNameError.setVisibility(View.GONE);
                    if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
                        etLeadName.getText().toString().trim())) {
                        tvNameError.setText(ErrorMsgName);
                        tvNameError.setVisibility(View.VISIBLE);
                    } else {
                        tvNameError.setVisibility(View.GONE);
                    }
                } else {
                    tvNameError.setVisibility(View.GONE);
                    tvNameError.setText("Enter name");
                }
            }
        });
        etContactPerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isBlank(etContactPerson.getText().toString().trim())) {
                    tvContactPersonError.setVisibility(View.GONE);
                    if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
                        etContactPerson.getText().toString().trim())) {
                        tvContactPersonError.setText(ErrorMsgName);
                        tvContactPersonError.setVisibility(View.VISIBLE);
                    } else {
                        tvContactPersonError.setVisibility(View.GONE);
                    }
                } else {
                    tvContactPersonError.setVisibility(View.GONE);
                    tvContactPersonError.setText("Enter contact person");
                }
            }
        });
        etLandlineNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (etLandlineNumber.isEnabled() == true) {
                    if (!isBlank(etLandlineNumber.getText().toString())) {
                        if (!validateTel(etLandlineNumber.getText().toString().trim())) {
                            tvLandLineError.setVisibility(View.VISIBLE);
                            tvLandLineError.setText("Invalid Tel number");
                            etLandlineNumber.requestFocus();
                        } else {
                            tvLandLineError.setVisibility(View.GONE);
                        }
                    } else {
                        tvLandLineError.setVisibility(View.GONE);
                    }
                }
            }
        });

        etAlternateMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etAlternateMobileNumber.isEnabled() == true) {
                    if (!isBlank(etAlternateMobileNumber.getText().toString())) {
                        if (!CRMValidationRule.validateMobile(
                            etAlternateMobileNumber.getText().toString())) {
                            tvAlternateMobileNumberError.setVisibility(View.VISIBLE);
                            tvAlternateMobileNumberError.setText("Invalid mobile no.");
                            etAlternateMobileNumber.requestFocus();
                        } else {
                            tvAlternateMobileNumberError.setVisibility(View.GONE);
                        }
                    } else {
                        tvAlternateMobileNumberError.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etAlternateEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (llEditLeadDetail.isEnabled() == true) {
                    if (!isBlank(etAlternateEmail.getText().toString().trim())) {
                        if (!isValidEmail(etAlternateEmail.getText())) {
                            tvAlternateEmailIdError.setVisibility(View.VISIBLE);
                            tvAlternateEmailIdError.setText("Invalid email id");
                            etAlternateEmail.requestFocus();
                        } else {
                            tvAlternateEmailIdError.setVisibility(View.GONE);
                        }
                    } else {
                        tvAlternateEmailIdError.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (llEditLeadDetail.isEnabled() == true) {
                    if (!isBlank(etEmail.getText().toString().trim())) {
                        if (!isValidEmail(etEmail.getText())) {
                            tvEmailIdError.setVisibility(View.VISIBLE);
                            tvEmailIdError.setText("Invalid email id");
                            etEmail.requestFocus();
                        } else {
                            tvEmailIdError.setVisibility(View.GONE);
                        }
                    } else {
                        tvEmailIdError.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPanNumber.setFilters(
            new InputFilter[] { new InputFilter.LengthFilter(10), new InputFilter.AllCaps() });
        etPanNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etPanNumber.getText().toString())) {
                    if (!CRMValidationRule.isValidPannumber((etPanNumber.getText().toString()))) {
                        tvPanNumError.setVisibility(View.VISIBLE);
                        tvPanNumError.setText("Invalid PAN number");
                        etPanNumber.requestFocus();
                    } else {
                        tvPanNumError.setVisibility(View.GONE);
                    }
                } else {
                    tvPanNumError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etAadharNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isBlank(etAadharNumber.getText().toString())) {
                    if (!validateAadhar(etAadharNumber.getText().toString().trim())) {
                        tvAadharNumError.setVisibility(View.VISIBLE);
                        tvAadharNumError.setText("Invalid Aadhar number");
                        etAadharNumber.requestFocus();
                    } else {
                        tvAadharNumError.setVisibility(View.GONE);
                    }
                } else {
                    tvAadharNumError.setVisibility(View.GONE);
                }
            }
        });
        etPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etPincode.getText().toString())) {
                    if (etPincode.length() < 6) {
                        tvPincodeError.setVisibility(View.VISIBLE);
                        tvPincodeError.setVisibility(View.VISIBLE);
                        tvPincodeError.setText("Invalid pincode.");
                        etPincode.requestFocus();
                    } else {
                        tvPincodeError.setVisibility(View.GONE);
                    }
                } else {
                    tvPincodeError.setVisibility(View.GONE);
                }
                if (cbSameAs.isChecked()) {
                    etOffPincode.setText(etPincode.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etOffPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etOffPincode.getText().toString())) {
                    if (etOffPincode.length() < 6) {
                        tvOffPincodeError.setVisibility(View.VISIBLE);
                        tvOffPincodeError.setText("Invalid pincode.");
                    } else {
                        tvOffPincodeError.setVisibility(View.GONE);
                    }
                } else {
                    tvOffPincodeError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etAdd1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cbSameAs.isChecked()) {
                    etOffAdd1.setText(etAdd1.getText().toString());
                }
            }
        });
        etAdd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cbSameAs.isChecked()) {
                    etOffAdd2.setText(etAdd2.getText().toString());
                }
            }
        });
        etAdd3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cbSameAs.isChecked()) {
                    etOffAdd3.setText(etAdd3.getText().toString());
                }
            }
        });

        Observable<String> refreeNameObs = RxTextView.afterTextChangeEvents(etRefereeName)
            .filter(event -> event.view().isFocused())
            .map(query -> query.editable().toString().trim())
            .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .share();

        compositeDisposable.add(
            refreeNameObs.filter(query -> query.length() == 2).subscribe(ignored -> {
                etRefereeEmail.setText("");
                etRefereeMobile.setText("");
                etRefereeCode.setText("");
            }));

        compositeDisposable.add(
            refreeNameObs.filter(query -> query.length() > 2).switchMap(query -> {
                pbLoadingIndicatorRefreeName.setVisibility(View.VISIBLE);
                etRefereeEmail.setText("");
                etRefereeMobile.setText("");
                etRefereeCode.setText("");
                final Map inputMap = new HashMap<>();
                inputMap.put("SearchType", "ByName");
                inputMap.put("SearchHint", query);
                return RefreeSyncWebServiceCall.getObservableData(getMainActivity(),
                    "Common/GetRefereeCode", inputMap, pbLoadingIndicatorRefreeName)
                    .compose(applyUiModel());
            }).subscribe(data -> {
                UiModel<JSONObject> uiModel = (UiModel<JSONObject>) data;
                JSONObject data1 = uiModel.data;
                if (data1 != null) {
                    RefreeData(data1, "ByName", pbLoadingIndicatorRefreeName);
                }
            }));

        Observable<String> refreeCodeObs = RxTextView.afterTextChangeEvents(etRefereeCode)
            .filter(event -> event.view().isFocused())
            .map(query -> query.editable().toString().trim())
            .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .share();

        compositeDisposable.add(
            refreeCodeObs.filter(query -> query.length() == 3).subscribe(ignored -> {
                etRefereeName.setText("");
                etRefereeMobile.setText("");
                etRefereeEmail.setText("");
            }));

        compositeDisposable.add(
            refreeCodeObs.filter(query -> query.length() > 3).switchMap(query -> {
                pbLoadingIndicatorRefreeCode.setVisibility(View.VISIBLE);
                etRefereeName.setText("");
                etRefereeMobile.setText("");
                etRefereeEmail.setText("");
                final Map inputMap = new HashMap<>();
                inputMap.put("SearchType", "ByCode");
                inputMap.put("SearchHint", query);
                return RefreeSyncWebServiceCall.getObservableData(getMainActivity(),
                    "Common/GetRefereeCode", inputMap, pbLoadingIndicatorRefreeCode)
                    .compose(applyUiModel());
            }).subscribe(data -> {
                UiModel<JSONObject> uiModel = (UiModel<JSONObject>) data;
                JSONObject data1 = uiModel.data;
                if (data1 != null) {
                    RefreeData(data1, "ByCode", pbLoadingIndicatorRefreeCode);
                }
            }));

        lvBrok = leadViewDetailView.findViewById(R.id.listBrokingPortfolio);
        lvAsset = leadViewDetailView.findViewById(R.id.listAssestPortfolio);
        lvCash = leadViewDetailView.findViewById(R.id.listCashPortfolio);
        lvPLBrok = leadViewDetailView.findViewById(R.id.lvPLBrok);
        lvPLAsset = leadViewDetailView.findViewById(R.id.lvPLAsset);
        lvPLLimits = leadViewDetailView.findViewById(R.id.lvPLLimits);

        txtPortfolioSnapshot =
            leadViewDetailView.findViewById(R.id.txtPortfolioSnapshot);
        txtPorfitLoss = leadViewDetailView.findViewById(R.id.txtPorfitLoss);
        txtAvailableLimits = leadViewDetailView.findViewById(R.id.txtAvailableLimits);
        txtBrokingPortfolio = leadViewDetailView.findViewById(R.id.txtBrokingPortfolio);
        txtAssestPortfolio = leadViewDetailView.findViewById(R.id.txtAssestPortfolio);
        txtCash = leadViewDetailView.findViewById(R.id.txtCash);
        txtBrokingProducts = leadViewDetailView.findViewById(R.id.txtBrokingProducts);
        txtAssestProducts = leadViewDetailView.findViewById(R.id.txtAssestProducts);
        txtNetworth = leadViewDetailView.findViewById(R.id.networth);
        txtDaychgamt = leadViewDetailView.findViewById(R.id.daychgamt);
        txtDaychgperst = leadViewDetailView.findViewById(R.id.daychgperst);
        txtOverallchgamt = leadViewDetailView.findViewById(R.id.overallchgamt);
        txt_overallchgperst = leadViewDetailView.findViewById(R.id.overallchgperst);

        tvTotalGainLoss = leadViewDetailView.findViewById(R.id.tvTotalGainLoss);
        tvSTGainLossPer = leadViewDetailView.findViewById(R.id.tvSTGainLossPer);
        tvSTGainLoss = leadViewDetailView.findViewById(R.id.tvSTGainLoss);
        tvLTGainLoss = leadViewDetailView.findViewById(R.id.tvLTGainLoss);
        tvLTGainLossPer = leadViewDetailView.findViewById(R.id.tvLTGainLossPer);

        llPortfolio = leadViewDetailView.findViewById(R.id.llPortfolio);
        llProfitLoss = leadViewDetailView.findViewById(R.id.llProfitLoss);
        llAvailableLimits = leadViewDetailView.findViewById(R.id.llAvailableLimits);
        llBrokingPortfolio =
            leadViewDetailView.findViewById(R.id.llBorkingPortfolio);
        llAssestPortfolio = leadViewDetailView.findViewById(R.id.llAssestPortfolio);
        llCash = leadViewDetailView.findViewById(R.id.llCash);
        llBrokingProducts = leadViewDetailView.findViewById(R.id.llBorkingProducts);
        llAssestProducts = leadViewDetailView.findViewById(R.id.llAssestProducts);

        btnBrokPortfolio = leadViewDetailView.findViewById(R.id.btnBrokPortfolio);
        btnAssetPortfolio = leadViewDetailView.findViewById(R.id.btnAssetPortfolio);
        btnBrokPortfolio.setVisibility(View.GONE);
        btnAssetPortfolio.setVisibility(View.GONE);
        txtPortfolioSnapshot.setOnClickListener(this);
        txtPorfitLoss.setOnClickListener(this);
        txtAvailableLimits.setOnClickListener(this);
        txtBrokingPortfolio.setOnClickListener(this);
        txtAssestPortfolio.setOnClickListener(this);
        txtCash.setOnClickListener(this);
        txtBrokingProducts.setOnClickListener(this);
        txtAssestProducts.setOnClickListener(this);
        btnBrokPortfolio.setOnClickListener(this);
        btnAssetPortfolio.setOnClickListener(this);

        llPortfolio.setVisibility(View.VISIBLE);
        llBrokingPortfolio.setVisibility(View.VISIBLE);
        llCash.setVisibility(View.VISIBLE);
        llAssestPortfolio.setVisibility(View.VISIBLE);
        setIcon(llPortfolio, txtPortfolioSnapshot, "OUTER");
        setIcon(llProfitLoss, txtPorfitLoss, "OUTER");
        setIcon(llAvailableLimits, txtAvailableLimits, "OUTER");

        setIcon(llBrokingPortfolio, txtBrokingPortfolio, "INNER");
        setIcon(llAssestPortfolio, txtAssestPortfolio, "INNER");
        setIcon(llCash, txtCash, "INNER");

        setIcon(llBrokingProducts, txtBrokingProducts, "INNER");
        setIcon(llAssestProducts, txtAssestProducts, "INNER");
    }

    private void getCountry() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();

            String serviceMethod = "Country/GetCountryList";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            arrCountry = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        stateFlag = false;
                                        officeStateFlag = false;
                                        hideProgress();
                                        JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject objDetail = jsonArray.getJSONObject(i);
                                            String countryId =
                                                objDetail.isNull(COUNTRYID) ? ""
                                                    : objDetail.getString(COUNTRYID);
                                            String countryName = objDetail.isNull(COUNTRYNAME) ? ""
                                                : objDetail.getString(COUNTRYNAME);
                                            arrCountry.add(new RowData(i, countryId, countryName));
                                        }
                                        arrAdapCountry = new ArrayAdapter<>(getMainActivity(),
                                            R.layout.simple_spinner_item, arrCountry);
                                        arrAdapCountry.setDropDownViewResource(R.layout.mdropdown);
                                        spCountry.setAdapter(arrAdapCountry);
                                        spCountry.setOnItemSelectedListener(countryListener);
                                        spOffCountry.setAdapter(arrAdapCountry);
                                        spOffCountry.setOnItemSelectedListener(offCountryListener);
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        MSFDialog.alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                MSFDialog.alertDialog(getMainActivity(), 0,
                    getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void getState(String id, final String type, final String value) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("CountryId", id);

            String serviceMethod = "StateProvince/GetStateProvinceList";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            arrState = new ArrayList<>();
                            arrOffState = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        arrState.add(new RowData(0, "-1", "Select"));
                                        arrOffState.add(new RowData(0, "-1", "Select"));
                                        JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject objDetail = jsonArray.getJSONObject(i);
                                            String stateId = objDetail.isNull(STATEID) ? ""
                                                : objDetail.getString(STATEID);
                                            String stateName = objDetail.isNull(STATENAME) ? ""
                                                : objDetail.getString(STATENAME);
                                            if (cbSameAs.isChecked()) {
                                                arrState.add(
                                                    new RowData(i + 1, stateId, stateName));
                                                arrOffState.add(
                                                    new RowData(i + 1, stateId, stateName));
                                            } else {
                                                if (type.equals(RESIDENCE)) {
                                                    arrState.add(
                                                        new RowData(i + 1, stateId, stateName));
                                                }
                                                if (type.equals(OFFICE)) {
                                                    arrOffState.add(
                                                        new RowData(i + 1, stateId, stateName));
                                                }
                                            }
                                        }
                                        if (cbSameAs.isChecked()) {

                                            arrAdapState = new ArrayAdapter<>(getMainActivity(),
                                                R.layout.simple_spinner_item, arrState);
                                            arrAdapState.setDropDownViewResource(
                                                R.layout.mdropdown);
                                            spState.setAdapter(arrAdapState);
                                            RowData dataPrimary =
                                                RowData.getSpinnerRowDataById(value, arrAdapState);
                                            if (dataPrimary != null) {
                                                spState.setSelection(dataPrimary.getPosition());
                                            }
                                            spState.setOnItemSelectedListener(stateListener);

                                            arrAdapOffState = new ArrayAdapter<>(getMainActivity(),
                                                R.layout.simple_spinner_item, arrOffState);
                                            arrAdapOffState.setDropDownViewResource(
                                                R.layout.mdropdown);
                                            spOffState.setAdapter(arrAdapOffState);
                                            RowData dataOffice =
                                                RowData.getSpinnerRowDataById(value,
                                                    arrAdapOffState);
                                            if (dataOffice != null) {
                                                spOffState.setSelection(dataOffice.getPosition());
                                            }
                                            spOffState.setOnItemSelectedListener(offStateListener);
                                        } else {
                                            if (type.equals(RESIDENCE)) {
                                                arrAdapState = new ArrayAdapter<>(getMainActivity(),
                                                    R.layout.simple_spinner_item, arrState);
                                                arrAdapState.setDropDownViewResource(
                                                    R.layout.mdropdown);
                                                spState.setAdapter(arrAdapState);
                                                RowData data = RowData.getSpinnerRowDataById(value,
                                                    arrAdapState);
                                                if (data != null) {
                                                    spState.setSelection(data.getPosition());
                                                }
                                                spState.setOnItemSelectedListener(stateListener);
                                            }
                                            if (type.equals(OFFICE)) {
                                                arrAdapOffState =
                                                    new ArrayAdapter<>(getMainActivity(),
                                                        R.layout.simple_spinner_item, arrOffState);
                                                arrAdapOffState.setDropDownViewResource(
                                                    R.layout.mdropdown);
                                                spOffState.setAdapter(arrAdapOffState);
                                                RowData data = RowData.getSpinnerRowDataById(value,
                                                    arrAdapOffState);
                                                if (data != null) {
                                                    spOffState.setSelection(data.getPosition());
                                                }
                                                spOffState.setOnItemSelectedListener(
                                                    offStateListener);
                                            }
                                        }
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        MSFDialog.alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                MSFDialog.alertDialog(getMainActivity(), 0,
                    getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void getCity(String id, final String type, final String value) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("StateId", id);

            String serviceMethod = "City/GetCityList";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            arrCity = new ArrayList<>();
                            arrOffCity = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        arrCity.add(new RowData(0, "-1", "Select"));
                                        arrOffCity.add(new RowData(0, "-1", "Select"));
                                        JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject objDetail = jsonArray.getJSONObject(i);
                                            String cityId = objDetail.isNull(CITYID) ? ""
                                                : objDetail.getString(CITYID);
                                            String cityName = objDetail.isNull(CITYNAME) ? ""
                                                : objDetail.getString(CITYNAME);
                                            if (cbSameAs.isChecked()) {
                                                arrCity.add(new RowData(i + 1, cityId, cityName));
                                                arrOffCity.add(
                                                    new RowData(i + 1, cityId, cityName));
                                            } else {
                                                if (type.equals(RESIDENCE)) {
                                                    arrCity.add(
                                                        new RowData(i + 1, cityId, cityName));
                                                }
                                                if (type.equals(OFFICE)) {
                                                    arrOffCity.add(
                                                        new RowData(i + 1, cityId, cityName));
                                                }
                                            }
                                        }
                                        if (cbSameAs.isChecked()) {
                                            arrAdapCity = new ArrayAdapter<>(getMainActivity(),
                                                R.layout.simple_spinner_item, arrCity);
                                            arrAdapCity.setDropDownViewResource(R.layout.mdropdown);
                                            spCity.setAdapter(arrAdapCity);
                                            if (!isBlank(value)) {
                                                RowData data = RowData.getSpinnerRowDataById(value,
                                                    arrAdapCity);
                                                if (data != null) {
                                                    spCity.setSelection(data.getPosition());
                                                }
                                                spCity.setOnItemSelectedListener(cityListener);
                                            }

                                            arrAdapOffCity = new ArrayAdapter<>(getMainActivity(),
                                                R.layout.simple_spinner_item, arrOffCity);
                                            arrAdapOffCity.setDropDownViewResource(
                                                R.layout.mdropdown);
                                            spOffCity.setAdapter(arrAdapOffCity);
                                            int pos =
                                                ((RowData) spCity.getSelectedItem()).getPosition();
                                            spOffCity.setSelection(pos);
                                        } else {
                                            if (type.equals(RESIDENCE)) {
                                                arrAdapCity = new ArrayAdapter<>(getMainActivity(),
                                                    R.layout.simple_spinner_item, arrCity);
                                                arrAdapCity.setDropDownViewResource(
                                                    R.layout.mdropdown);
                                                spCity.setAdapter(arrAdapCity);
                                                if (!isBlank(value)) {
                                                    RowData data =
                                                        RowData.getSpinnerRowDataById(value,
                                                            arrAdapCity);
                                                    if (data != null) {
                                                        spCity.setSelection(data.getPosition());
                                                    }
                                                    spCity.setOnItemSelectedListener(cityListener);
                                                }
                                            }
                                            if (type.equals(OFFICE)) {
                                                arrAdapOffCity =
                                                    new ArrayAdapter<>(getMainActivity(),
                                                        R.layout.simple_spinner_item, arrOffCity);
                                                arrAdapOffCity.setDropDownViewResource(
                                                    R.layout.mdropdown);
                                                spOffCity.setAdapter(arrAdapOffCity);
                                                if (!isBlank(value)) {
                                                    RowData data =
                                                        RowData.getSpinnerRowDataById(value,
                                                            arrAdapOffCity);
                                                    if (data != null) {
                                                        spOffCity.setSelection(data.getPosition());
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        MSFDialog.alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                MSFDialog.alertDialog(getMainActivity(), 0,
                    getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void getProfession() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", PROFESSIONID);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            arrProfession = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        arrProfession.add(new RowData(0, "-1", "Select"));
                                        JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject objDetail = jsonArray.getJSONObject(i);
                                            String lookUpId = objDetail.isNull(LOOKUPID) ? ""
                                                : objDetail.getString(LOOKUPID);
                                            String lookUpName = objDetail.isNull(LOOKUPNAME) ? ""
                                                : objDetail.getString(LOOKUPNAME);
                                            arrProfession.add(
                                                new RowData(i + 1, lookUpId, lookUpName));
                                        }
                                        arrAdapProfession = new ArrayAdapter<>(getMainActivity(),
                                            R.layout.simple_spinner_item, arrProfession);
                                        arrAdapProfession.setDropDownViewResource(
                                            R.layout.mdropdown);
                                        spProfession.setAdapter(arrAdapProfession);
                                        spProfession.setOnItemSelectedListener(professionListener);
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        MSFDialog.alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                MSFDialog.alertDialog(getMainActivity(), 0,
                    getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void getInterestIn() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", INTERESTEDIN);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //after success code goes here
                            arrInterested = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        arrInterested.add(new RowData(0, "-1", "Select"));
                                        JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject objDetail = jsonArray.getJSONObject(i);
                                            String lookUpId = objDetail.isNull(LOOKUPID) ? ""
                                                : objDetail.getString(LOOKUPID);
                                            String lookUpName = objDetail.isNull(LOOKUPNAME) ? ""
                                                : objDetail.getString(LOOKUPNAME);
                                            arrInterested.add(
                                                new RowData(i + 1, lookUpId, lookUpName));
                                        }
                                        arrAdapInterested = new ArrayAdapter<>(getMainActivity(),
                                            R.layout.simple_spinner_item, arrInterested);
                                        arrAdapInterested.setDropDownViewResource(
                                            R.layout.mdropdown);
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void getProductType(String productId) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", PRODUCTTYPE);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());
            inputMap.put("ParentId", productId);

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //after success code goes here
                            arrProductType = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        arrProductType.add(new RowData(0, "-1", "Select"));
                                        JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject objDetail = jsonArray.getJSONObject(i);
                                            String lookUpId = objDetail.isNull(LOOKUPID) ? ""
                                                : objDetail.getString(LOOKUPID);
                                            String lookUpName = objDetail.isNull(LOOKUPNAME) ? ""
                                                : objDetail.getString(LOOKUPNAME);
                                            arrProductType.add(
                                                new RowData(i + 1, lookUpId, lookUpName));
                                        }
                                        arrAdapProductType = new ArrayAdapter<>(getMainActivity(),
                                            R.layout.simple_spinner_item, arrProductType);
                                        arrAdapProductType.setDropDownViewResource(
                                            R.layout.mdropdown);
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void getStatus(final Spinner spStatus, String productId) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            if (customProgress != null) {
                customProgress.setVisibility(View.VISIBLE);
            }
            Map inputMap = new HashMap<>();
            inputMap.put("StageID", productId);
            inputMap.put("Status", "1");

            String serviceMethod = "LeadStatus/GetStatusList";
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //after success code goes here
                            arrStatus = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        if (customProgress != null) {
                                            customProgress.setVisibility(View.GONE);
                                        }
                                        arrStatus.add(new RowData(0, "-1", "Select"));
                                        JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject objDetail = jsonArray.getJSONObject(i);
                                            String statusId = objDetail.isNull(STATUSID) ? ""
                                                : objDetail.getString(STATUSID);
                                            String statusName = objDetail.isNull(STATUSNAME) ? ""
                                                : objDetail.getString(STATUSNAME);
                                            arrStatus.add(new RowData(i + 1, statusId, statusName));
                                        }
                                        arrAdapStatus = new ArrayAdapter<>(getMainActivity(),
                                            R.layout.simple_spinner_item, arrStatus);
                                        arrAdapStatus.setDropDownViewResource(R.layout.mdropdown);
                                        spStatus.setAdapter(arrAdapStatus);
                                    } else {
                                        hideProgress();
                                        if (customProgress != null) {
                                            customProgress.setVisibility(View.GONE);
                                        }
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                    if (customProgress != null) {
                                        customProgress.setVisibility(View.GONE);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                if (customProgress != null) {
                                    customProgress.setVisibility(View.GONE);
                                }
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                            if (customProgress != null) {
                                customProgress.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                            if (customProgress != null) {
                                customProgress.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (customProgress != null) {
                                    customProgress.setVisibility(View.GONE);
                                }
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            if (customProgress != null) {
                                customProgress.setVisibility(View.GONE);
                            }
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            if (customProgress != null) {
                                customProgress.setVisibility(View.GONE);
                            }
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                if (customProgress != null) {
                    customProgress.setVisibility(View.GONE);
                }
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            if (customProgress != null) {
                customProgress.setVisibility(View.GONE);
            }
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void getLeadDetail() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Lead/GetLeadView";
            String tmpReq = String.format(ID_OBJECT, mLeadId);

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), tmpReq,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {

                                if (statusCode == 200) {
                                    leadDetailAddProductList = new ArrayList<>();
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        JSONObject objDetail = response.getJSONObject(RESPONSE);

                                        String leadId = objDetail.isNull(LEADID) ? ""
                                            : objDetail.getString(LEADID);
                                        String leadCategoryText =
                                            objDetail.isNull("LeadCategoryText")
                                                ? "" : objDetail.getString("LeadCategoryText");

                                        leadOwner = objDetail.isNull("LeadOwner") ? ""
                                            : objDetail.getString("LeadOwner");
                                        String businessUnitId = objDetail.isNull(BUSINESSUNITID)
                                            ? "" : objDetail.getString(BUSINESSUNITID);
                                        String channelId = objDetail.isNull(CHANNELID) ? ""
                                            : objDetail.getString(CHANNELID);
                                        String branchCode = objDetail.isNull(BRANCHCODE) ? ""
                                            : objDetail.getString(BRANCHCODE);
                                        String leadCategoryId = objDetail.isNull(LEADCATEGORYID)
                                            ? "" : objDetail.getString(LEADCATEGORYID);
                                        contactPerson = objDetail.isNull(CONTACTPERSON) ? ""
                                            : objDetail.getString(CONTACTPERSON);
                                        sourceID = objDetail.isNull(SOURCEID) ? ""
                                            : objDetail.getString(SOURCEID);
                                        leadProspetSourceID = objDetail.isNull(LEADSOURCEID) ? ""
                                            : objDetail.getString(LEADSOURCEID);
                                        String sourceName = objDetail.isNull("SourceName") ? ""
                                            : objDetail.getString("SourceName");
                                        refereeCode = objDetail.isNull(REFEREECODE) ? ""
                                            : objDetail.getString(REFEREECODE);
                                        refereeName = objDetail.isNull(REFEREENAME) ? ""
                                            : objDetail.getString(REFEREENAME);
                                        String refereeEmail = objDetail.isNull(REFEREEEMAIL) ? ""
                                            : objDetail.getString(REFEREEEMAIL);
                                        String refereeMobile = objDetail.isNull(REFEREEMOBILE) ? ""
                                            : objDetail.getString(REFEREEMOBILE);
                                        String createdBy = objDetail.isNull("CreatedBy") ? ""
                                            : objDetail.getString("CreatedBy");
                                        String createdThrough = objDetail.isNull("CreatedThrough")
                                            ? "" : objDetail.getString("CreatedThrough");
                                        profile = objDetail.isNull("Profile") ? ""
                                            : objDetail.getString("Profile");
                                        profession = objDetail.isNull("ProfessionID")
                                            ? "" : objDetail.getString("ProfessionID");
                                        creationDate = objDetail.isNull("CreationDate")
                                            ? "" : objDetail.getString("CreationDate");
                                        String lastModificationDate =
                                            objDetail.isNull("LastModificationDate")
                                                ? "" : objDetail.getString("LastModificationDate");
                                        String lastModifiedBy = objDetail.isNull("LastModifiedBy")
                                            ? "" : objDetail.getString("LastModifiedBy");
                                        String prospectSource = objDetail.isNull("ProspectSource")
                                            ? "" : objDetail.getString("ProspectSource");
                                        String comments = objDetail.isNull("Comments") ? ""
                                            : objDetail.getString("Comments");
                                        String leadSourceDetails =
                                            objDetail.isNull("LeadSourceDetails")
                                                ? "" : objDetail.getString("LeadSourceDetails");
                                        String canEditLead = "1";
                                        if (objDetail.has("CanEditLead")) {
                                            canEditLead = objDetail.isNull("CanEditLead")
                                                ? "" : objDetail.getString("CanEditLead");
                                        }
                                        String isProductSpecialist = "N";
                                        if (objDetail.has("IsProductSpecialist")) {
                                            isProductSpecialist =
                                                objDetail.isNull("IsProductSpecialist")
                                                    ? ""
                                                    : objDetail.getString("IsProductSpecialist");
                                        }
                                        if (canEditLead.equalsIgnoreCase("0") || isProductSpecialist
                                            .equalsIgnoreCase("Y")) {
                                            setLeadEditable(false);
                                        } else {
                                            setLeadEditable(true);
                                        }
                                        String IsPotentialPMSProduct =
                                            objDetail.isNull(ISPOTENTIALPMSPRODUCT)
                                                ? "" : objDetail.getString(ISPOTENTIALPMSPRODUCT);
                                        isPotentialPMSProduct = IsPotentialPMSProduct.isEmpty();
                                        etPmsProduct.setText(IsPotentialPMSProduct);
                                        etRefereeCode.setText(refereeCode);
                                        etRefereeName.setText(refereeName);
                                        if (!refereeCode.isEmpty() || !refereeName.isEmpty()) {
                                            isRefreeCode = true;
                                        } else {
                                            isRefreeCode = false;
                                        }
                                        etRefereeEmail.setText(refereeEmail);
                                        etRefereeMobile.setText(refereeMobile);
                                        etCreatedBy.setText(createdBy);
                                        etCreatedThrough.setText(createdThrough);
                                        etLastModifiedBy.setText(lastModifiedBy);

                                        try {
                                            etLastModifiedDate.setText(
                                                getDateFromString(lastModificationDate,
                                                    "yyyy-MM-dd'T'kk:mm:ss",
                                                    DATE_FORMAT_TO_DISPLAY));
                                        } catch (Exception e) {
                                            lastModificationDate =
                                                lastModificationDate.replace("T", " ");
                                            etLastModifiedDate.setText(lastModificationDate);
                                        }
                                        etProspectSource.setText(prospectSource);
                                        etProspectSourceDetail.setText(leadSourceDetails);
                                        etComments.setText(comments);
                                        if (leadCategoryText.equalsIgnoreCase("Individual")) {
                                            isIndividualsFlag = true;
                                            etContactPerson.setVisibility(View.GONE);
                                        } else {
                                            etContactPerson.setVisibility(View.VISIBLE);
                                            isIndividualsFlag = false;
                                        }
                                        etLeadCategory.setText(leadCategoryText);
                                        if (objDetail.has("leadProductMapping")) {
                                            JSONArray jsonArray =
                                                objDetail.getJSONArray("leadProductMapping");
                                            if (jsonArray.length() != 0) {
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject objDetail1 =
                                                        jsonArray.getJSONObject(i);
                                                    String leadProductMappingId =
                                                        objDetail1.isNull("LeadProductMappingID")
                                                            ? "" : objDetail1.getString(
                                                            "LeadProductMappingID");
                                                    interestedIn = objDetail1.isNull("InterestedIn")
                                                        ? "" : objDetail1.getString("InterestedIn");
                                                    String productName =
                                                        objDetail1.isNull("ProductName")
                                                            ? ""
                                                            : objDetail1.getString("ProductName");
                                                    String productId =
                                                        objDetail1.isNull("ProductID")
                                                            ? ""
                                                            : objDetail1.getString("ProductID");
                                                    primaryRm = objDetail1.isNull("PrimaryRM_Name")
                                                        ? ""
                                                        : objDetail1.getString("PrimaryRM_Name");
                                                    String secondaryRm =
                                                        objDetail1.isNull("SecondaryRM")
                                                            ? ""
                                                            : objDetail1.getString("SecondaryRM");
                                                    String clientCode =
                                                        objDetail1.isNull("ClientCode")
                                                            ? ""
                                                            : objDetail1.getString("ClientCode");
                                                    String assignedBy =
                                                        objDetail1.isNull("AssignedBy")
                                                            ? ""
                                                            : objDetail1.getString("AssignedBy");
                                                    String assignedByDate =
                                                        objDetail1.isNull("AssignedByDate")
                                                            ? "" : objDetail1.getString(
                                                            "AssignedByDate");
                                                    stageId = objDetail1.isNull("StageID")
                                                        ? "" : objDetail1.getString("StageID");
                                                    String stageName =
                                                        objDetail1.isNull("StageName")
                                                            ? ""
                                                            : objDetail1.getString("StageName");
                                                    statusId = objDetail1.isNull("StatusID") ? ""
                                                        : objDetail1.getString("StatusID");
                                                    String statusName =
                                                        objDetail1.isNull("StatusName")
                                                            ? ""
                                                            : objDetail1.getString("StatusName");

                                                    String lead = objDetail1.isNull("lead")
                                                        ? "" : objDetail1.getString("lead");
                                                    String isAdminMode =
                                                        objDetail1.isNull("IsAdminMode")
                                                            ? ""
                                                            : objDetail1.getString("IsAdminMode");
                                                    String inApproval =
                                                        objDetail1.isNull("InApproval")
                                                            ? ""
                                                            : objDetail1.getString("InApproval");
                                                    String productSpecialist =
                                                        objDetail1.isNull("ProductSpecialist_Name")
                                                            ? "" : objDetail1.getString(
                                                            "ProductSpecialist_Name");
                                                    boolean isDefault =
                                                        objDetail1.getBoolean("IsDefault");
                                                    boolean IsCrossSellingProduct =
                                                        objDetail1.getBoolean(
                                                            "IsCrossSellingProduct");
                                                    tvClientCode.setText(clientCode);
                                                    List<CrossSellingProductsModel>
                                                        crossSellingProductsModelList =
                                                        new ArrayList<CrossSellingProductsModel>();
                                                    JSONArray jsonArrayCrossSellingProducts =
                                                        objDetail1.getJSONArray(
                                                            "CrossSellingProducts");
                                                    if (jsonArrayCrossSellingProducts.length()
                                                        != 0) {
                                                        for (int k = 0; k
                                                            < jsonArrayCrossSellingProducts.length();
                                                            k++) {
                                                            JSONObject
                                                                objDetail1CrossSellingProducts =
                                                                jsonArrayCrossSellingProducts.getJSONObject(
                                                                    k);
                                                            String LeadCrossSellingProductId =
                                                                objDetail1CrossSellingProducts.isNull(
                                                                    "LeadCrossSellingProductId")
                                                                    ? ""
                                                                    : objDetail1CrossSellingProducts
                                                                        .getString(
                                                                            "LeadCrossSellingProductId");
                                                            String CrossSellingProductId =
                                                                objDetail1CrossSellingProducts.isNull(
                                                                    "CrossSellingProductId") ? ""
                                                                    : objDetail1CrossSellingProducts
                                                                        .getString(
                                                                            "CrossSellingProductId");
                                                            String CrossSellingStatusId =
                                                                objDetail1CrossSellingProducts.isNull(
                                                                    "CrossSellingStatusId") ? ""
                                                                    : objDetail1CrossSellingProducts
                                                                        .getString(
                                                                            "CrossSellingStatusId");
                                                            String CrossSellingProductName =
                                                                objDetail1CrossSellingProducts.isNull(
                                                                    "CrossSellingProductName")
                                                                    ? ""
                                                                    : objDetail1CrossSellingProducts
                                                                        .getString(
                                                                            "CrossSellingProductName");
                                                            String CrossSellingStatusName =
                                                                objDetail1CrossSellingProducts.isNull(
                                                                    "CrossSellingStatusName")
                                                                    ? ""
                                                                    : objDetail1CrossSellingProducts
                                                                        .getString(
                                                                            "CrossSellingStatusName");
                                                            crossSellingProductsModelList.add(
                                                                new CrossSellingProductsModel(
                                                                    LeadCrossSellingProductId,
                                                                    CrossSellingProductId,
                                                                    CrossSellingStatusId,
                                                                    CrossSellingProductName,
                                                                    CrossSellingStatusName));
                                                        }
                                                    }

                                                    leadDetailAddProductList.add(
                                                        new LeadDetailAddProductModel(stageId,
                                                            stageName, statusId, statusName,
                                                            interestedIn, productName, primaryRm,
                                                            productSpecialist, assignedBy,
                                                            assignedByDate, clientCode,
                                                            leadProductMappingId, isDefault,
                                                            crossSellingProductsModelList,
                                                            IsCrossSellingProduct));
                                                }
                                                leadDetailAddProductAdapter =
                                                    new LeadDetailAddProductAdapter(
                                                        getMainActivity(),
                                                        leadDetailAddProductList);
                                                lvAddProduct.setAdapter(
                                                    leadDetailAddProductAdapter);
                                                setListViewHeightBasedOnChildrenDetail(
                                                    lvAddProduct);
                                            }
                                        }
                                        if (objDetail.has("contact")) {
                                            JSONObject objDetail1 =
                                                objDetail.getJSONObject("contact");
                                            String name =
                                                objDetail1.isNull("Name") ? ""
                                                    : objDetail1.getString("Name");
                                            String panNumber = objDetail1.isNull("PanNumber")
                                                ? "" : objDetail1.getString("PanNumber");
                                            String aadharNumber = objDetail1.isNull("AadharNumber")
                                                ? "" : objDetail1.getString("AadharNumber");
                                            String dob = objDetail1.isNull("DOB") ? ""
                                                : objDetail1.getString("DOB");
                                            String doa = objDetail1.isNull("DOA") ? ""
                                                : objDetail1.getString("DOA");
                                            String companyName = objDetail1.isNull("CompanyName")
                                                ? "" : objDetail1.getString("CompanyName");
                                            String designation = objDetail1.isNull("Designation")
                                                ? "" : objDetail1.getString("Designation");
                                            String annualCashFlow =
                                                objDetail1.isNull("AnnualCashFlow")
                                                    ? "" : objDetail1.getString("AnnualCashFlow");
                                            String alternateEmailId =
                                                objDetail1.isNull("AlternateEmailId") ? ""
                                                    : objDetail1.getString("AlternateEmailId");
                                            String mobile = objDetail1.isNull("Mobile")
                                                ? "" : objDetail1.getString("Mobile");
                                            String eKYCMobileNo = objDetail1.isNull("EKYCMobileNo")
                                                ? "" : objDetail1.getString("EKYCMobileNo");
                                            String landlineNo = objDetail1.isNull("LandlineNo")
                                                ? "" : objDetail1.getString("LandlineNo");
                                            String EmailId = objDetail1.isNull("EmailId")
                                                ? "" : objDetail1.getString("EmailId");
                                            String AlternateMobileNo =
                                                objDetail1.isNull("AlternateMobileNo")
                                                    ? ""
                                                    : objDetail1.getString("AlternateMobileNo");
                                            String isSameAsAbove =
                                                objDetail1.isNull("IsSameAsAbove")
                                                    ? "" : objDetail1.getString("IsSameAsAbove");
                                            cbSameAs.setChecked(
                                                Boolean.parseBoolean(isSameAsAbove));
                                            int uCIID = objDetail1.isNull(UCID) ? 0
                                                : objDetail1.getInt(UCID);
                                            if (uCIID > 0) {
                                                llClient.setVisibility(View.VISIBLE);
                                                llClientPortfolioDetails.setVisibility(
                                                    View.VISIBLE);
                                                webServiceportfolioSnapshot(String.valueOf(uCIID));
                                                webServicePL(String.valueOf(uCIID));
                                                getClientDetail(uCIID);
                                                toggleSectionExpansion(llClientDetail,
                                                    tvClientDetail);
                                            }
                                            etPanNumber.setText(panNumber);
                                            etAadharNumber.setText(aadharNumber);
                                            etEkycMobileNumber.setText(eKYCMobileNo);
                                            etMobileNumber.setText(mobile);
                                            etEmail.setText(EmailId);
                                            etAlternateEmail.setText(alternateEmailId);
                                            etLandlineNumber.setText(landlineNo);
                                            etAlternateMobileNumber.setText(AlternateMobileNo);
                                            String newDob[] = dob.split("T");
                                            String newDoa[] = doa.split("T");

                                            try {
                                                if (!newDob[0].isEmpty()) {
                                                    Date Dob =
                                                        CRMAppUtil.getDate(newDob[0], "yyyy-MM-dd");
                                                    etDob.setText(
                                                        CRMAppUtil.getDateStr(Dob, "dd-MM-yyyy"));
                                                }
                                                if (!newDoa[0].isEmpty()) {
                                                    Date Doa =
                                                        CRMAppUtil.getDate(newDoa[0], "yyyy-MM-dd");
                                                    etAnniversary.setText(
                                                        CRMAppUtil.getDateStr(Doa, "dd-MM-yyyy"));
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            JSONArray jsonArray1 =
                                                objDetail1.getJSONArray("contactAddress");
                                            if (jsonArray1.length() != 0) {
                                                for (int i = 0; i < jsonArray1.length(); i++) {
                                                    JSONObject objDetail2 =
                                                        jsonArray1.getJSONObject(i);
                                                    String contactAddressId =
                                                        objDetail2.isNull("ContactAddressID")
                                                            ? "" : objDetail2.getString(
                                                            "ContactAddressID");
                                                    String addressTypeId =
                                                        objDetail2.isNull("AddressTypeID")
                                                            ? ""
                                                            : objDetail2.getString("AddressTypeID");
                                                    String address1 =
                                                        objDetail2.isNull("Address1") ? ""
                                                            : objDetail2.getString("Address1");
                                                    String address2 =
                                                        objDetail2.isNull("Address2") ? ""
                                                            : objDetail2.getString("Address2");
                                                    String address3 =
                                                        objDetail2.isNull("Address3") ? ""
                                                            : objDetail2.getString("Address3");
                                                    String stateId = objDetail2.isNull("StateID")
                                                        ? "" : objDetail2.getString("StateID");
                                                    String cityId = objDetail2.isNull("CityID")
                                                        ? "" : objDetail2.getString("CityID");
                                                    String countryID =
                                                        objDetail2.isNull("CountryID") ? ""
                                                            : objDetail2.getString("CountryID");
                                                    String pincode =
                                                        objDetail2.isNull("Pincode") ? ""
                                                            : objDetail2.getString("Pincode");
                                                    String isPrimary =
                                                        objDetail2.isNull("isPrimary") ? ""
                                                            : objDetail2.getString("isPrimary");
                                                    String isAdminMode =
                                                        objDetail2.isNull("IsAdminMode") ? ""
                                                            : objDetail2.getString("IsAdminMode");
                                                    String isPrimaryAdd =
                                                        objDetail2.isNull("isPrimary") ? ""
                                                            : objDetail2.getString("isPrimary");
                                                    if (Integer.parseInt(addressTypeId)
                                                        == RESIDENCEADDRESS) {
                                                        cbPrimaryAddress.setChecked(
                                                            Boolean.parseBoolean(isPrimary));
                                                        etAdd1.setText(address1);
                                                        etAdd2.setText(address2);
                                                        etAdd3.setText(address3);
                                                        etPincode.setText(pincode);
                                                        stateStr = stateId;
                                                        cityStr = cityId;
                                                        if (arrAdapCountry == null) {
                                                            getCountry();
                                                        } else {
                                                            RowData dataCountry =
                                                                RowData.getSpinnerRowDataById(
                                                                    countryID, arrAdapCountry);
                                                            if (dataCountry != null) {
                                                                spCountry.setSelection(
                                                                    dataCountry.getPosition());
                                                            }
                                                            getState(
                                                                ((RowData) spCountry.getSelectedItem())
                                                                    .getKey(), RESIDENCE, stateStr);
                                                        }
                                                        if (cbPrimaryAddress.isChecked()) {
                                                            cbOffPrimaryAddress.setVisibility(
                                                                View.GONE);
                                                            cbSameAs.setVisibility(View.VISIBLE);
                                                        } else {
                                                            cbOffPrimaryAddress.setVisibility(
                                                                View.VISIBLE);
                                                            cbSameAs.setVisibility(View.GONE);
                                                        }
                                                    } else if (Integer.parseInt(addressTypeId)
                                                        == OFFICEADDRESS) {
                                                        cbOffPrimaryAddress.setChecked(
                                                            Boolean.parseBoolean(isPrimary));
                                                        etOffAdd1.setText(address1);
                                                        etOffAdd2.setText(address2);
                                                        etOffAdd3.setText(address3);
                                                        etOffPincode.setText(pincode);
                                                        offAdd1 = address1;
                                                        offAdd2 = address2;
                                                        offAdd3 = address3;
                                                        offPincode = pincode;
                                                        offCountryId = countryID;
                                                        offStateStr = stateId;
                                                        offCityStr = cityId;
                                                        if (arrAdapCountry == null) {
                                                            getCountry();
                                                        } else {
                                                            RowData dataCountry =
                                                                RowData.getSpinnerRowDataById(
                                                                    countryID, arrAdapCountry);
                                                            if (dataCountry != null) {
                                                                spOffCountry.setSelection(
                                                                    dataCountry.getPosition());
                                                            }
                                                            if (cbSameAs.isChecked()) {
                                                            } else {
                                                                getState(
                                                                    ((RowData) spOffCountry.getSelectedItem())
                                                                        .getKey(), OFFICE,
                                                                    offStateStr);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            tvPanCard.setText(panNumber);
                                            tvAadharCard.setText(aadharNumber);
                                            etLeadName.setText(name);
                                            ilLeadName.setHint(leadCategoryText + " Name*");
                                            try {
                                                etCreationDate.setText(
                                                    getDateFromString(creationDate,
                                                        "yyyy-MM-dd'T'kk:mm:ss",
                                                        DATE_FORMAT_TO_DISPLAY));
                                            } catch (Exception e) {
                                                creationDate = creationDate.replace("T", " ");
                                                etCreationDate.setText(creationDate);
                                            }
                                            if (arrAdapProfession == null) {
                                                getProfession();
                                            } else {
                                                RowData dataProfile =
                                                    RowData.getSpinnerRowDataById(profession,
                                                        arrAdapProfession);
                                                if (dataProfile != null) {
                                                    spProfession.setSelection(
                                                        dataProfile.getPosition());
                                                }
                                                spProfession.setOnItemSelectedListener(
                                                    professionListener);
                                            }
                                            if (arrAdapProfile == null) {
                                                selectProfile();
                                            } else {
                                                RowData dataProfile =
                                                    RowData.getSpinnerRowDataByValue(profile,
                                                        arrAdapProfile);
                                                if (dataProfile != null) {
                                                    spProfile.setSelection(
                                                        dataProfile.getPosition());
                                                }
                                                spProfile.setOnItemSelectedListener(
                                                    profileListener);
                                            }
                                            etContactPerson.setText(contactPerson);
                                        }
                                        if (objDetail.has(LEADFIELDSECTIONMODELS)) {
                                            JSONArray jsonArrayLeadDescModel =
                                                objDetail.getJSONArray(LEADFIELDSECTIONMODELS);
                                            if (jsonArrayLeadDescModel.length() != 0) {
                                                mLeadFieldSectionModelList = new ArrayList<>();
                                                //TODO : docode
                                                for (int i = 0; i < jsonArrayLeadDescModel.length();
                                                    i++) {
                                                    JSONObject objLeadDetail2 =
                                                        jsonArrayLeadDescModel.getJSONObject(i);
                                                    int fieldSectionId =
                                                        objLeadDetail2.getInt(FieldSectionId);
                                                    String sectionName =
                                                        objLeadDetail2.isNull(SectionName)
                                                            ? ""
                                                            : objLeadDetail2.getString(SectionName);
                                                    String sectionCode =
                                                        objLeadDetail2.isNull(SectionCode)
                                                            ? ""
                                                            : objLeadDetail2.getString(SectionCode);
                                                    String sectionHeader =
                                                        objLeadDetail2.isNull(SectionHeader) ? ""
                                                            : objLeadDetail2.getString(
                                                                SectionHeader);
                                                    LeadFieldSectionModel tmpObj =
                                                        new LeadFieldSectionModel();
                                                    tmpObj.setFieldSectionId(fieldSectionId);
                                                    tmpObj.setSectionName(sectionName);
                                                    tmpObj.setSectionCode(sectionCode);
                                                    tmpObj.setSectionHeader(sectionHeader);
                                                    mLeadFieldSectionModelList.add(tmpObj);
                                                }
                                            }
                                        }
                                        if (objDetail.has(leadFieldValueModels)) {
                                            JSONArray jsonArrayLeadDescModel =
                                                objDetail.getJSONArray(leadFieldValueModels);
                                            if (jsonArrayLeadDescModel.length() != 0) {
                                                for (int i = 0; i < jsonArrayLeadDescModel.length();
                                                    i++) {
                                                    JSONObject objLeadDetail2 =
                                                        jsonArrayLeadDescModel.getJSONObject(i);
                                                    String lFSysName =
                                                        objLeadDetail2.isNull(LFSysName)
                                                            ? ""
                                                            : objLeadDetail2.getString(LFSysName);
                                                    String leadFieldValue =
                                                        objLeadDetail2.isNull(LeadFieldValue)
                                                            ? "" : objLeadDetail2.getString(
                                                            LeadFieldValue);
                                                    int leadFieldId =
                                                        objLeadDetail2.getInt(LeadFieldId);
                                                    int fieldSectionId =
                                                        objLeadDetail2.getInt(FieldSectionId);
                                                    String fieldName =
                                                        objLeadDetail2.isNull(FieldName)
                                                            ? ""
                                                            : objLeadDetail2.getString(FieldName);
                                                    String controlType =
                                                        objLeadDetail2.isNull(ControlType)
                                                            ? ""
                                                            : objLeadDetail2.getString(ControlType);
                                                    String dropdownValue =
                                                        objLeadDetail2.isNull(DropdownValue) ? ""
                                                            : objLeadDetail2.getString(
                                                                DropdownValue);
                                                    boolean isMandatory =
                                                        (objLeadDetail2.isNull(MANDATORY)
                                                            || objLeadDetail2.getString(MANDATORY)
                                                            .equals("")) ? false : true;
                                                    LeadFieldSectionModel tempSection =
                                                        LeadFieldSectionModel.getSectionFromListWithId(
                                                            mLeadFieldSectionModelList,
                                                            fieldSectionId);
                                                    if (tempSection != null) {
                                                        LeadFieldValueModel tmpObj =
                                                            new LeadFieldValueModel();
                                                        tmpObj.setLeadField_Value(leadFieldValue);
                                                        tmpObj.setFieldName(fieldName);
                                                        tmpObj.setControlType(controlType);
                                                        tmpObj.setLF_SysName(lFSysName);
                                                        tmpObj.setLeadid(mLeadId);
                                                        tmpObj.setMandatory(isMandatory);
                                                        tmpObj.setLeadFieldId(leadFieldId);
                                                        tmpObj.setDropdownValueArr(dropdownValue,
                                                            "\\|");
                                                        tempSection.addSelectionList(tmpObj);
                                                    } else {
                                                        Log.w("Section list", " Dangling Feild");
                                                    }
                                                }//For end

                                                addNewSections();
                                            } else {
                                                Log.w(TAG, leadFieldValueModels + " Was Empty");
                                            }
                                        } else {
                                            Log.w(TAG, leadFieldValueModels + " Not found");
                                        }
                                        etLeadID.setText(leadId);
                                        etRefereeCode.setText(refereeCode);
                                        etRefereeName.setText(refereeName);
                                        etRefereeEmail.setText(refereeEmail);
                                        etRefereeMobile.setText(refereeMobile);
                                        etSource.setText(sourceName);
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void updateLead(LeadDetailModel leadDetailModel) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String requestJson = leadDetailModel.getJson();
            String serviceMethod = "Lead/UpdateLead";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(),
                    requestJson, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    String message = response.getString(MESSAGE);
                                    JSONObject jsonObject = response.getJSONObject(RESPONSE);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        String approvalMessage =
                                            jsonObject.isNull("ApprovalMessage")
                                                ? "" : jsonObject.getString("ApprovalMessage");
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            (approvalMessage.isEmpty() ? "Lead Updated Successfully"
                                                : approvalMessage), "Ok", true,
                                            new MSFDialog.DialogListener() {
                                                @Override
                                                public void alertDialogAction(
                                                    MSFDialog.Action action, Object... data) {
                                                    if (action == MSFDialog.Action.OK) {
                                                        setEnableDisable(false);
                                                        editableFlag = true;
                                                        leadSection();
                                                    }
                                                }
                                            });
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void selectProfile() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("LookupTypeId", LEADPROFILE);
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

            String serviceMethod = "Master/GetAllLookup";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            arrSelectProfile = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    hideProgress();
                                    if (status.equalsIgnoreCase("true")) {
                                        arrSelectProfile.add(new RowData(0, "-1", SELECT_PROFILE));
                                        JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject objDetail = jsonArray.getJSONObject(i);
                                            String lookUpId = objDetail.isNull(LOOKUPID) ? ""
                                                : objDetail.getString(LOOKUPID);
                                            String lookUpName = objDetail.isNull(LOOKUPNAME)
                                                ? "" : objDetail.getString(LOOKUPNAME);
                                            arrSelectProfile.add(
                                                new RowData(i + 1, lookUpId, lookUpName));
                                        }
                                        arrAdapProfile = new ArrayAdapter<>(getMainActivity(),
                                            R.layout.simple_spinner_item, arrSelectProfile);
                                        arrAdapProfile.setDropDownViewResource(R.layout.mdropdown);
                                        spProfile.setAdapter(arrAdapProfile);
                                        spProfile.setOnItemSelectedListener(profileListener);
                                        arrAdapProfile.notifyDataSetChanged();
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                hideProgress();
                                e.printStackTrace();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void addProductDialog() {
        final Dialog dialog = new Dialog(getMainActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_leadview_add_product);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final Spinner spInterestedIn = dialog.findViewById(R.id.spInterestedIn);
        final Spinner spStage = dialog.findViewById(R.id.spStage);
        final Spinner spStatus = dialog.findViewById(R.id.spStatus);
        tvspIntrestError = dialog.findViewById(R.id.tvspIntrestError);
        tvspStatusError = dialog.findViewById(R.id.tvspStatusError);
        tvspStageError = dialog.findViewById(R.id.tvspStageError);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        final LinearLayout progressLayout = dialog.findViewById(R.id.customProgress);
        final ClearableEditText productSpecialistAdd =
            dialog.findViewById(R.id.dacProductSpecialist);
        final ClearableEditText actAssignTo =
            dialog.findViewById(R.id.actAssignTo);
        productSpecialistAdd.setHint(getResources().getString(R.string.str_prod_spl));
        actAssignTo.setHint(getResources().getString(R.string.str_assing));
        productSpecialistAdd.setListener(new ClearableEditText.Listener() {
            @Override
            public void didClearText() {
                specialistAdd = "";
            }
        });
        productSpecialistAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                final CustomListPickerDialog searchListProductSpecialist =
                    new CustomListPickerDialog(getMainActivity());

                searchListProductSpecialist.setThreshold(SEARCH_LENTH);

                searchListProductSpecialist.show();
                searchListProductSpecialist.setCustomTitle(
                    getResources().getString(R.string.str_prod_spl));
                CustomListPickerDialog.ListPickerListneable productSpecialistViewListner =
                    new CustomListPickerDialog.ListPickerListneable() {
                        @Override
                        public void onListItemSelect(Parcelable data) {
                            EmployeeModel clientModel = (EmployeeModel) data;
                            if (clientModel.isNoData()) {
                                return;
                            }
                            productSpecialistAdd.setText(clientModel.getAttendeeName());
                            specialistAdd = clientModel.getAttendeeCode();
                            productSpecialistAdd.setClearIconVisible(true);
                            searchListProductSpecialist.dismiss();
                        }

                        @Override
                        public void onSearchTextChanged(final CharSequence constraint) {

                            final ArrayList<EmployeeModel> modelList = new ArrayList<>();
                            try {
                                if (constraint != null) {
                                    if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                                        Map requestMap = new HashMap();
                                        requestMap.put("EmployeeName",
                                            URLEncoder.encode(constraint.toString(), "utf-8"));
                                        requestMap.put("EmployeeCode",
                                            URLEncoder.encode(constraint.toString(), "utf-8"));
                                        searchListProductSpecialist.showProgress(View.VISIBLE);
                                        String serviceMethod =
                                            "UserApi/AutoCompleteProductSpecialist";
                                        primaryRMAdd =
                                            CRMAppUtil.isBlank(leadOwner.trim()) ? advisorCode
                                                : leadOwner.trim();
                                        requestMap.put("PrimaryRM", primaryRMAdd);
                                        hideKeyboard(leadViewDetailView);
                                        try {
                                            CRMWebServiceHandler.post(getMainActivity(),
                                                serviceMethod, getHeader(), requestMap,
                                                new JsonHttpResponseHandler() {
                                                    @Override
                                                    public void onSuccess(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        JSONObject response) {
                                                        searchListProductSpecialist.showProgress(
                                                            View.GONE);
                                                        super.onSuccess(statusCode, headers,
                                                            response);
                                                        try {
                                                            if (statusCode == 200) {
                                                                String status =
                                                                    response.getString(STATUS);
                                                                if (status.equalsIgnoreCase(
                                                                    "true")) {
                                                                    JSONArray jsonArray =
                                                                        response.getJSONArray(
                                                                            RESPONSE);
                                                                    for (int i = 0;
                                                                        i < jsonArray.length();
                                                                        i++) {
                                                                        JSONObject jsonObject =
                                                                            jsonArray.getJSONObject(
                                                                                i);
                                                                        String employeeName =
                                                                            jsonObject.getString(
                                                                                EMPLOYEE_NAME)
                                                                                == null ? ""
                                                                                : jsonObject.getString(
                                                                                    EMPLOYEE_NAME);
                                                                        String employeeCode =
                                                                            jsonObject.getString(
                                                                                EMPLOYEE_CODE)
                                                                                == null ? ""
                                                                                : jsonObject.getString(
                                                                                    EMPLOYEE_CODE);
                                                                        String BranchCode =
                                                                            jsonObject.getString(
                                                                                BRANCH_Code) == null
                                                                                ? ""
                                                                                : jsonObject.getString(
                                                                                    BRANCH_Code);//region Check self Empcode and don't add to list
                                                                        if (employeeCode.equalsIgnoreCase(
                                                                            AccountDetails.getEmployeeCode())) {
                                                                            continue;
                                                                        }
                                                                        modelList.add(
                                                                            new EmployeeModel(
                                                                                employeeName,
                                                                                employeeCode,
                                                                                BranchCode, false));
                                                                    }
                                                                } else {
                                                                    searchListProductSpecialist.showProgress(
                                                                        View.GONE);
                                                                    modelList.add(
                                                                        EmployeeModel.getNoData(
                                                                            CRMMOSLCommon.getErrorMsgFromResponse(
                                                                                response)));
                                                                }
                                                                if (modelList.isEmpty()) {
                                                                    modelList.add(
                                                                        EmployeeModel.getNoData(
                                                                            ATTENDEEE_NOT_FOUND_MESSAGE));
                                                                }
                                                                final AttendeeAdapter mYAdapter =
                                                                    new AttendeeAdapter(
                                                                        getMainActivity(),
                                                                        modelList);

                                                                getMainActivity().runOnUiThread(
                                                                    new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            searchListProductSpecialist
                                                                                .setAdapter(
                                                                                    mYAdapter);
                                                                        }
                                                                    });
                                                            } else {
                                                                searchListProductSpecialist.showProgress(
                                                                    View.GONE);
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            searchListProductSpecialist.showProgress(
                                                                View.GONE);
                                                            alertDialog(getMainActivity(), 0,
                                                                getResources().getString(
                                                                    R.string.motilal_oswal),
                                                                SOME_ERROR_OCCURED_PLEASE_RETRY,
                                                                "OK", true, null);
                                                        }
                                                    }

                                                    @Override
                                                    public void onSuccess(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        JSONArray response) {
                                                        super.onSuccess(statusCode, headers,
                                                            response);
                                                        searchListProductSpecialist.showProgress(
                                                            View.GONE);
                                                    }

                                                    @Override
                                                    public void onSuccess(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        String responseString) {
                                                        super.onSuccess(statusCode, headers,
                                                            responseString);
                                                        searchListProductSpecialist.showProgress(
                                                            View.GONE);
                                                    }

                                                    @Override
                                                    public void onFailure(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        Throwable throwable,
                                                        JSONObject errorResponse) {
                                                        super.onFailure(statusCode, headers,
                                                            throwable, errorResponse);
                                                        try {
                                                            hideProgress();
                                                            if (statusCode
                                                                == UNAUTHORIZE_STATUS_CODE) {
                                                                alertDialog(getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    UNAUTHORIZE_USER, "OK", false,
                                                                    new MSFDialog.DialogListener() {
                                                                        @Override
                                                                        public void alertDialogAction(
                                                                            MSFDialog.Action action,
                                                                            Object... data) {
                                                                            if (action
                                                                                == MSFDialog.Action.OK) {
                                                                                Intent i =
                                                                                    new Intent(
                                                                                        getMainActivity(),
                                                                                        LoginActivity.class);
                                                                                startActivity(i);
                                                                                getActivity().finish();
                                                                            }
                                                                        }
                                                                    });
                                                                return;
                                                            } else {
                                                                String error =
                                                                    CRMMOSLCommon.getErrorMsgFromResponse(
                                                                        errorResponse);
                                                                if (CRMAppUtil.isBlank(error)) {
                                                                    error =
                                                                        SOME_SERVER_ERROR_OCCURRED;
                                                                }
                                                                MSFDialog.alertDialog(
                                                                    getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    error, "OK", true, null);
                                                                return;
                                                            }
                                                        } catch (Exception ex) {
                                                            ex.printStackTrace();
                                                            MSFDialog.alertDialog(getMainActivity(),
                                                                0, getResources().getString(
                                                                    R.string.motilal_oswal),
                                                                SOME_SERVER_ERROR_OCCURRED, "OK",
                                                                true, null);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        Throwable throwable,
                                                        JSONArray errorResponse) {
                                                        super.onFailure(statusCode, headers,
                                                            throwable, errorResponse);
                                                        searchListProductSpecialist.showProgress(
                                                            View.GONE);
                                                        alertDialog(getMainActivity(), 0,
                                                            getResources().getString(
                                                                R.string.motilal_oswal),
                                                            SOME_SERVER_ERROR_OCCURRED, "OK", true,
                                                            null);
                                                    }

                                                    @Override
                                                    public void onFailure(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        String responseString,
                                                        Throwable throwable) {
                                                        super.onFailure(statusCode, headers,
                                                            responseString, throwable);
                                                        searchListProductSpecialist.showProgress(
                                                            View.GONE);
                                                        alertDialog(getMainActivity(), 0,
                                                            getResources().getString(
                                                                R.string.motilal_oswal),
                                                            SOME_SERVER_ERROR_OCCURRED, "OK", true,
                                                            null);
                                                    }
                                                });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            searchListProductSpecialist.showProgress(View.GONE);
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                        }
                                    } else {
                                        searchListProductSpecialist.showProgress(View.GONE);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            INTERNET_CONNECTION, "OK", true, null);
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    };
                searchListProductSpecialist.setmListner(productSpecialistViewListner);
            }
        });
        actAssignTo.setListener(new ClearableEditText.Listener() {
            @Override
            public void didClearText() {
                primaryRMAdd = "";
            }
        });
        actAssignTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                final CustomListPickerDialog searchList =
                    new CustomListPickerDialog(getMainActivity());
                searchList.show();
                searchList.setThreshold(SEARCH_LENTH);
                searchList.setCustomTitle(getResources().getString(R.string.str_assing));
                CustomListPickerDialog.ListPickerListneable assingViewListner =
                    new CustomListPickerDialog.ListPickerListneable() {
                        @Override
                        public void onListItemSelect(Parcelable data) {
                            EmployeeModel clientModel = (EmployeeModel) data;
                            if (clientModel.isNoData()) {
                                return;
                            }
                            actAssignTo.setText(clientModel.getAttendeeName());
                            primaryRMAdd = clientModel.getAttendeeCode();
                            actAssignTo.setClearIconVisible(true);
                            searchList.dismiss();
                        }

                        @Override
                        public void onSearchTextChanged(final CharSequence constraint) {

                            final ArrayList<EmployeeModel> modelList = new ArrayList<>();
                            try {
                                if (constraint != null) {
                                    if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                                        searchList.showProgress(View.VISIBLE);
                                        Map requestMap = new HashMap();
                                        requestMap.put("EmployeeName",
                                            URLEncoder.encode(constraint.toString(), "utf-8"));
                                        requestMap.put("EmployeeCode",
                                            URLEncoder.encode(constraint.toString(), "utf-8"));

                                        String serviceMethod = "UserApi/GetUserBusinesprofile";
                                        hideKeyboard(leadViewDetailView);
                                        try {
                                            CRMWebServiceHandler.post(getMainActivity(),
                                                serviceMethod, getHeader(), requestMap,
                                                new JsonHttpResponseHandler() {

                                                    @Override
                                                    public void onSuccess(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        JSONObject response) {
                                                        super.onSuccess(statusCode, headers,
                                                            response);
                                                        try {
                                                            searchList.showProgress(View.GONE);
                                                            if (statusCode == 200) {
                                                                String status =
                                                                    response.getString(STATUS);
                                                                if (status.equalsIgnoreCase(
                                                                    "true")) {
                                                                    JSONArray jsonArray =
                                                                        response.getJSONArray(
                                                                            RESPONSE);
                                                                    for (int i = 0;
                                                                        i < jsonArray.length();
                                                                        i++) {
                                                                        JSONObject jsonObject =
                                                                            jsonArray.getJSONObject(
                                                                                i);
                                                                        String employeeName =
                                                                            jsonObject.getString(
                                                                                EMPLOYEE_NAME)
                                                                                == null ? ""
                                                                                : jsonObject.getString(
                                                                                    EMPLOYEE_NAME);
                                                                        String employeeCode =
                                                                            jsonObject.getString(
                                                                                EMPLOYEE_CODE)
                                                                                == null ? ""
                                                                                : jsonObject.getString(
                                                                                    EMPLOYEE_CODE);
                                                                        String BranchCode =
                                                                            jsonObject.getString(
                                                                                BRANCH_Code) == null
                                                                                ? ""
                                                                                : jsonObject.getString(
                                                                                    BRANCH_Code);//region Check self Empcode and don't add to list
                                                                        if (employeeCode.equalsIgnoreCase(
                                                                            AccountDetails.getEmployeeCode())) {
                                                                            continue;
                                                                        }
                                                                        modelList.add(
                                                                            new EmployeeModel(
                                                                                employeeName,
                                                                                employeeCode,
                                                                                BranchCode, false));
                                                                    }
                                                                } else {
                                                                    searchList.showProgress(
                                                                        View.GONE);
                                                                    modelList.add(
                                                                        EmployeeModel.getNoData(
                                                                            CRMMOSLCommon.getErrorMsgFromResponse(
                                                                                response)));
                                                                }
                                                                if (modelList.isEmpty()) {
                                                                    modelList.add(
                                                                        EmployeeModel.getNoData(
                                                                            ATTENDEEE_NOT_FOUND_MESSAGE));
                                                                }
                                                                final AttendeeAdapter mYAdapter =
                                                                    new AttendeeAdapter(
                                                                        getMainActivity(),
                                                                        modelList);

                                                                getMainActivity().runOnUiThread(
                                                                    new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            searchList.setAdapter(
                                                                                mYAdapter);
                                                                        }
                                                                    });
                                                            } else {
                                                                searchList.showProgress(View.GONE);
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            searchList.showProgress(View.GONE);
                                                            alertDialog(getMainActivity(), 0,
                                                                getResources().getString(
                                                                    R.string.motilal_oswal),
                                                                SOME_ERROR_OCCURED_PLEASE_RETRY,
                                                                "OK", true, null);
                                                        }
                                                    }

                                                    @Override
                                                    public void onSuccess(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        JSONArray response) {
                                                        super.onSuccess(statusCode, headers,
                                                            response);
                                                        searchList.showProgress(View.GONE);
                                                    }

                                                    @Override
                                                    public void onSuccess(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        String responseString) {
                                                        super.onSuccess(statusCode, headers,
                                                            responseString);
                                                        searchList.showProgress(View.GONE);
                                                    }

                                                    @Override
                                                    public void onFailure(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        Throwable throwable,
                                                        JSONObject errorResponse) {
                                                        super.onFailure(statusCode, headers,
                                                            throwable, errorResponse);
                                                        try {
                                                            searchList.showProgress(View.GONE);
                                                            if (statusCode
                                                                == UNAUTHORIZE_STATUS_CODE) {
                                                                alertDialog(getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    UNAUTHORIZE_USER, "OK", false,
                                                                    new MSFDialog.DialogListener() {
                                                                        @Override
                                                                        public void alertDialogAction(
                                                                            MSFDialog.Action action,
                                                                            Object... data) {
                                                                            if (action
                                                                                == MSFDialog.Action.OK) {
                                                                                Intent i =
                                                                                    new Intent(
                                                                                        getMainActivity(),
                                                                                        LoginActivity.class);
                                                                                startActivity(i);
                                                                                getActivity().finish();
                                                                            }
                                                                        }
                                                                    });
                                                                return;
                                                            } else {
                                                                String error =
                                                                    CRMMOSLCommon.getErrorMsgFromResponse(
                                                                        errorResponse);
                                                                if (CRMAppUtil.isBlank(error)) {
                                                                    error =
                                                                        SOME_SERVER_ERROR_OCCURRED;
                                                                }
                                                                MSFDialog.alertDialog(
                                                                    getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    error, "OK", true, null);
                                                                return;
                                                            }
                                                        } catch (Exception ex) {
                                                            ex.printStackTrace();
                                                            MSFDialog.alertDialog(getMainActivity(),
                                                                0, getResources().getString(
                                                                    R.string.motilal_oswal),
                                                                SOME_SERVER_ERROR_OCCURRED, "OK",
                                                                true, null);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        Throwable throwable,
                                                        JSONArray errorResponse) {
                                                        super.onFailure(statusCode, headers,
                                                            throwable, errorResponse);
                                                        searchList.showProgress(View.GONE);
                                                        alertDialog(getMainActivity(), 0,
                                                            getResources().getString(
                                                                R.string.motilal_oswal),
                                                            SOME_SERVER_ERROR_OCCURRED, "OK", true,
                                                            null);
                                                    }

                                                    @Override
                                                    public void onFailure(int statusCode,
                                                        cz.msebera.android.httpclient.Header[] headers,
                                                        String responseString,
                                                        Throwable throwable) {
                                                        super.onFailure(statusCode, headers,
                                                            responseString, throwable);
                                                        searchList.showProgress(View.GONE);
                                                        alertDialog(getMainActivity(), 0,
                                                            getResources().getString(
                                                                R.string.motilal_oswal),
                                                            SOME_SERVER_ERROR_OCCURRED, "OK", true,
                                                            null);
                                                    }
                                                });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            searchList.showProgress(View.GONE);
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                        }
                                    } else {
                                        searchList.showProgress(View.GONE);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            INTERNET_CONNECTION, "OK", true, null);
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    };
                searchList.setmListner(assingViewListner);
            }
        });
        //endregion

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        spInterestedIn.setAdapter(arrAdapInterested);
        spInterestedIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (tvspIntrestError != null) {
                        tvspIntrestError.setVisibility(View.GONE);
                    }
                    interestInCodeStr = ((RowData) spInterestedIn.getSelectedItem()).getKey();
                    if (!interestInCodeStr.equalsIgnoreCase("-1")) {
                        getProductType(interestInCodeStr);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        GetStage(spStage, "", progressLayout);
        spStage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (tvspStageError != null) {
                        tvspStageError.setVisibility(View.GONE);
                    }

                    stageCodeStr = ((RowData) spStage.getSelectedItem()).getKey();

                    if (!stageCodeStr.equalsIgnoreCase("-1")) {
                        getStatus(spStatus, stageCodeStr);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Mandatory check
                if (spInterestedIn.getSelectedItemPosition() < 1) {
                    tvspIntrestError.setVisibility(View.VISIBLE);
                    tvspIntrestError.setText("Select Interested Product");
                    //TODO Show Error
                    return;
                }
                if (spStage.getSelectedItemPosition() < 1) {
                    tvspStageError.setVisibility(View.VISIBLE);
                    tvspStageError.setText("Select Stage");
                    //TODO Show Error
                    return;
                }
                if (spStatus.getSelectedItemPosition() < 1) {
                    //TODO Show Error
                    tvspStatusError.setVisibility(View.VISIBLE);
                    tvspStatusError.setText("Select Status");
                    return;
                }
                //Mandatory chec end

                String interestedInKey = ((RowData) spInterestedIn.getSelectedItem()).getKey();
                String interestedInDisplayText =
                    ((RowData) spInterestedIn.getSelectedItem()).getValue();
                if (leadDetailAddProductList.size() == 0) {
                    leadDetailAddProductAdapter = new LeadDetailAddProductAdapter(getMainActivity(),
                        leadDetailAddProductList);
                }
                if (leadDetailAddProductAdapter.getAvailableAddnDoc(interestedInKey)) {
                    alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        interestedInDisplayText + " is already selected", "OK", true, null);
                } else {
                    if (isPotentialPMSProduct) {
                        MSFDialog.alertDialog(getActivity(), 0,
                            getResources().getString(R.string.motilal_oswal),
                            POTENTIAL_INVEST_IN_PRODUCT, YES, NO, true,
                            new MSFDialog.DialogListener() {
                                @Override
                                public void alertDialogAction(MSFDialog.Action action,
                                    Object... data) {
                                    if (action == MSFDialog.Action.OK) {
                                        addProduct(
                                            ((RowData) spInterestedIn.getSelectedItem()).getKey(),
                                            ((RowData) spStage.getSelectedItem()).getKey(),
                                            ((RowData) spStatus.getSelectedItem()).getKey(), YES);
                                    } else {
                                        addProduct(
                                            ((RowData) spInterestedIn.getSelectedItem()).getKey(),
                                            ((RowData) spStage.getSelectedItem()).getKey(),
                                            ((RowData) spStatus.getSelectedItem()).getKey(), NO);
                                    }
                                }
                            });
                    } else {
                        addProduct(((RowData) spInterestedIn.getSelectedItem()).getKey(),
                            ((RowData) spStage.getSelectedItem()).getKey(),
                            ((RowData) spStatus.getSelectedItem()).getKey(), "");
                    }
                }
                dialog.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(leadViewDetailView);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addProduct(String interestedIn, String stageId, String statusId,
        String productMapping) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put(LEAD_ID, mLeadId);
            inputMap.put(UCID, ucid);

            inputMap.put("InterestedIn",
                interestedIn);
            inputMap.put("ProductID",
                interestedIn);
            primaryRMAdd =
                CRMAppUtil.isBlank(leadOwner.trim()) ? advisorCode : leadOwner.trim();
            inputMap.put("PrimaryRM", primaryRMAdd);
            inputMap.put("ProductSpecialist", specialistAdd);
            inputMap.put("AssignedBy", AccountDetails.getEmployeeCode());
            inputMap.put("AssignedByDate", CRMAppUtil.getDBDate());
            inputMap.put("StageID", stageId);
            inputMap.put("StatusID", statusId);
            inputMap.put("IsPotentialPMSProduct", productMapping);
            String serviceMethod = "Lead/InsertLeadProductMapping";
            hideKeyboard(leadViewDetailView);
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(),
                    inputMap, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers,
                            JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //after success code goes here
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();

                                        if (mLeadId.equalsIgnoreCase("0")) {
                                            JSONObject Response_obj =
                                                response.getJSONObject(RESPONSE);
                                            final String LeadId =
                                                (Response_obj.isNull(LEADID)
                                                    || Response_obj.getString(LEADID)
                                                    .equals("")) ? "0"
                                                    : Response_obj.getString(LEADID);
                                            final String Ucid =
                                                (Response_obj.isNull(UCID)
                                                    || Response_obj.getString(UCID)
                                                    .equals("")) ? "0"
                                                    : Response_obj.getString(UCID);
                                            alertDialog(getActivity(), 0,
                                                getResources().getString(
                                                    R.string.motilal_oswal),
                                                "Product Added Successfully with leadId"
                                                    + LeadId, "OK", true,
                                                new MSFDialog.DialogListener() {
                                                    @Override
                                                    public void alertDialogAction(
                                                        MSFDialog.Action action,
                                                        Object... data) {
                                                        if (action
                                                            == MSFDialog.Action.OK) {
                                                            Bundle bundle =
                                                                new Bundle();
                                                            bundle.putString(LEAD_ID,
                                                                LeadId);
                                                            bundle.putString(UCID,
                                                                Ucid);
                                                            navigateTo(
                                                                NAV_TO_LEAD_VIEW_MAIN,
                                                                bundle,
                                                                getFragmentManager(),
                                                                true);
                                                        }
                                                    }
                                                });
                                        } else {
                                            alertDialog(getActivity(), 0,
                                                getResources().getString(
                                                    R.string.motilal_oswal),
                                                "Product Added Successfully", "OK",
                                                true, new MSFDialog.DialogListener() {
                                                    @Override
                                                    public void alertDialogAction(
                                                        MSFDialog.Action action,
                                                        Object... data) {
                                                        if (action
                                                            == MSFDialog.Action.OK) {
                                                            leadSection();
                                                        }
                                                    }
                                                });
                                        }
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(
                                                response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(
                                                R.string.motilal_oswal), errorMsg, "OK",
                                            true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers,
                            JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers,
                            String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers,
                            Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable,
                                errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(
                                            R.string.motilal_oswal), UNAUTHORIZE_USER,
                                        "OK", false, new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(
                                                MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i =
                                                        new Intent(getMainActivity(),
                                                            LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(
                                            errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(
                                            R.string.motilal_oswal), error, "OK", true,
                                        null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers,
                            Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable,
                                errorResponse);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers,
                            String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString,
                                throwable);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0,
                    getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION,
                "OK", true, null);
        }
    }

    private boolean validateLead() {

        if (isBlank(etLeadName.getText().toString().trim())) {
            tvNameError.setText("Enter name");
            tvNameError.setVisibility(View.VISIBLE);
            tvNameError.requestFocus();
            return false;
        } else
            //
            if (!isBlank(etDob.getText().toString()) & !isBlank(
                etAnniversary.getText().toString())) {
                try {
                    Date dobDate = getDBDateFrom(etDob.getText().toString().trim(), "dd-MM-yyyy");
                    Date anniDate =
                        getDBDateFrom(etAnniversary.getText().toString().trim(), "dd-MM-yyyy");

                    if (dobDate.after(anniDate)) {
                        tvAnniversaryError.setText(MESSAGE_DOB_ANNI_COMPARE);
                        tvAnniversaryError.setVisibility(View.VISIBLE);
                        tvAnniversaryError.requestFocus();

                        return false;
                    } else if (dobDate.getTime() == anniDate.getTime()) {
                        tvAnniversaryError.setText(MESSAGE_DOB_ANNI_EQUAL);
                        tvAnniversaryError.setVisibility(View.VISIBLE);
                        tvAnniversaryError.requestFocus();
                        return false;
                    }
                } catch (java.text.ParseException e) {
                    tvAnniversaryError.setText("Date not in proper format");
                    tvAnniversaryError.setVisibility(View.VISIBLE);
                    tvAnniversaryError.requestFocus();
                    return false;
                }
            } else if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
                etLeadName.getText().toString().trim())) {

                tvNameError.setText(ErrorMsgName);
                tvNameError.setVisibility(View.VISIBLE);
            } else if (!isIndividualsFlag && isBlank(etContactPerson.getText().toString().trim())) {
                tvContactPersonError.setText("Enter contact person");
                tvContactPersonError.setVisibility(View.VISIBLE);
                tvContactPersonError.requestFocus();
                return false;
            } else if (!isIndividualsFlag
                && !ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
                etContactPerson.getText().toString().trim())) {
                tvContactPersonError.setText(ErrorMsgName);
                tvContactPersonError.setVisibility(View.VISIBLE);
                tvContactPersonError.requestFocus();
                return false;
            } else if (!isBlank(etAlternateMobileNumber.getText().toString())
                && !CRMValidationRule.validateMobile(
                etAlternateMobileNumber.getText().toString())) {
                tvAlternateMobileNumberError.setText("Invalid mobile number");
                etAlternateMobileNumber.requestFocus();
                tvAlternateMobileNumberError.setVisibility(View.VISIBLE);
                return false;
            } else if (!isBlank(etAlternateMobileNumber.getText().toString())
                && etMobileNumber.getText()
                .toString()
                .equalsIgnoreCase(etAlternateMobileNumber.getText().toString())) {
                tvAlternateMobileNumberError.setText(
                    "Mobile and Alternate mobile number are not Same");
                etAlternateMobileNumber.requestFocus();
                tvAlternateMobileNumberError.setVisibility(View.VISIBLE);
                return false;
            } else if (!isBlank(etLandlineNumber.getText().toString()) && !validateTel(
                etLandlineNumber.getText().toString().trim())) {
                tvLandLineError.setText("Invalid Tel number");
                etLandlineNumber.requestFocus();
                tvLandLineError.setVisibility(View.VISIBLE);
                return false;
            } else if (!isBlank(etEmail.getText().toString()) && !isValidEmail(
                etEmail.getText().toString().trim())) {
                tvEmailIdError.setText("Invalid email id");
                tvEmailIdError.setVisibility(View.VISIBLE);
                etEmail.requestFocus();
                return false;
            } else if (!isBlank(etAlternateEmail.getText().toString()) && !isValidEmail(
                etAlternateEmail.getText().toString().trim())) {
                tvAlternateEmailIdError.setText("Invalid email id");
                tvAlternateEmailIdError.setVisibility(View.VISIBLE);
                etAlternateEmail.requestFocus();
                return false;
            } else if (!isBlank(etAlternateEmail.getText().toString()) && etEmail.getText()
                .toString()
                .equalsIgnoreCase(etAlternateEmail.getText().toString())) {
                tvAlternateEmailIdError.setText("Email and Alternate email id are not same");
                tvAlternateEmailIdError.setVisibility(View.VISIBLE);
                etAlternateEmail.requestFocus();
                return false;
            } else if (!isBlank(etPanNumber.getText().toString())
                && !CRMValidationRule.isValidPannumber(etPanNumber.getText().toString())) {
                tvPanNumError.setText("Invalid PAN number");
                etPanNumber.requestFocus();
                tvPanNumError.setVisibility(View.VISIBLE);
                return false;
            } else if (!isBlank(etAadharNumber.getText().toString()) && !validateAadhar(
                etAadharNumber.getText().toString().trim())) {
                tvAadharNumError.setText("Invalid Aadhar number");
                etAadharNumber.requestFocus();
                tvAadharNumError.setVisibility(View.VISIBLE);
                return false;
            } else if (cbPrimaryAddress.isChecked()
                && !isBlank(etPincode.getText().toString())
                && etPincode.length() < 6
                && etPincode.length() != 0) {
                tvPincodeError.setVisibility(View.VISIBLE);
                tvPincodeError.setText("Invalid pincode");
                tvPincodeError.requestFocus();
                return false;
            } else if ((cbOffPrimaryAddress.isChecked() || cbSameAs.isChecked())
                && !isBlank(etOffPincode.getText().toString())
                && etOffPincode.length() < 6
                && etOffPincode.length() != 0) {
                tvOffPincodeError.setVisibility(View.VISIBLE);
                tvOffPincodeError.setText("Invalid pincode");
                etOffPincode.requestFocus();
                return false;
            }

        return true;
    }

    @Override
    public void onClick(View view) {
        Bundle args = new Bundle();
        switch (view.getId()) {
            case R.id.llEditLeadDetail:
                if (!this.getArguments().getString(LEAD_ID).equalsIgnoreCase("0")) {
                    if (isLeadEditable()) {
                        if (editableFlag) {
                            setEnableDisable(true);
                            editableFlag = false;
                        } else {
                            setEnableDisable(false);
                            editableFlag = true;
                        }
                    }
                }
                break;
            case R.id.etDob:
                try {
                    CustomDatePickerDialog dobDatePicker =
                        new CustomDatePickerDialog(getContext(), false, etDob.getText().toString(),
                            true, new DatePickerListener() {
                            @Override
                            public void submitDatePicker(String date) {
                                tvAnniversaryError.setVisibility(View.GONE);
                                etDob.setText(date);
                                etDob.setClearIconVisible(true);
                            }
                        });
                    dobDatePicker.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.etAnniversary:
                try {
                    CustomDatePickerDialog anniversaryDatePicker =
                        new CustomDatePickerDialog(getContext(), false,
                            etAnniversary.getText().toString(), true, new DatePickerListener() {
                            @Override
                            public void submitDatePicker(String date) {
                                tvAnniversaryError.setVisibility(View.GONE);
                                etAnniversary.setText(date);

                                etAnniversary.setClearIconVisible(true);
                            }
                        });
                    anniversaryDatePicker.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tvClientDetail:
                toggleSectionExpansion(llClientDetail, tvClientDetail);
                break;
            case R.id.tvClientPortfolioDetails:
                toggleSectionExpansion(llClientPortfolio, tvClientPortfolioDetails);
                break;

            case R.id.tvLeadDetail:
                toggleSectionExpansion(llLeadDetail, tvLeadDetail);
                break;
            case R.id.rlAddProduct: {
                if (isLeadEditable()) {
                    addProductDialog();
                }
            }
            break;
            case R.id.tvDocuments:
                args.putString(LEAD_ID, this.getArguments().getString(LEAD_ID));
                args.putString(UCID, this.getArguments().getString(UCID));
                navigateTo(NAV_TO_LEAD_DOCUMENT, args, getFragmentManager(), true);
                break;
            case R.id.cbPrimaryAddress:
                if (cbPrimaryAddress.isChecked()) {
                    enableField(llResAdd);
                    enableField(llOffAdd);
                    cbOffPrimaryAddress.setVisibility(View.GONE);
                    cbSameAs.setVisibility(View.VISIBLE);
                } else {
                    disableField(llResAdd);
                    disableField(llOffAdd);
                    cbOffPrimaryAddress.setVisibility(View.VISIBLE);
                    cbSameAs.setVisibility(View.GONE);
                    cbSameAs.setChecked(false);
                    cbOffPrimaryAddress.setChecked(false);
                    unCheckedSameasAbove();
                }
                break;
            case R.id.cbOffPrimaryAddress:
                if (cbOffPrimaryAddress.isChecked()) {
                    enableField(llOffAdd);
                    enableField(llResAdd);
                } else {
                    disableField(llResAdd);
                    disableField(llOffAdd);
                }
                break;
            case R.id.cbSameAs:
                if (cbSameAs.isChecked()) {
                    etOffAdd1.setText(etAdd1.getText().toString());
                    etOffAdd2.setText(etAdd2.getText().toString());
                    etOffAdd3.setText(etAdd3.getText().toString());
                    etOffPincode.setText(etPincode.getText().toString());
                    RowData dataCountry =
                        RowData.getSpinnerRowDataById(countryCodeStr, arrAdapCountry);
                    if (dataCountry != null) {
                        spOffCountry.setSelection(dataCountry.getPosition());
                        getState(((RowData) spOffCountry.getSelectedItem()).getKey(), OFFICE,
                            stateCodeStr);
                    }

                    disableField(llOffAdd);
                } else {
                    if (!isBlank(offStateStr)
                        || !isBlank(offCityStr)
                        || !isBlank(offAdd1)
                        || !isBlank(offAdd2)
                        || !isBlank(offAdd3)
                        || !isBlank(offPincode)) {
                        if (offStateStr.equalsIgnoreCase("-1") || offCityStr.equalsIgnoreCase(
                            "-1")) {
                            enableField(llOffAdd);
                            unCheckedSameasAbove();
                        } else {
                            etOffAdd1.setText(offAdd1);
                            etOffAdd2.setText(offAdd2);
                            etOffAdd3.setText(offAdd3);
                            etOffPincode.setText(offPincode);
                            if (arrAdapCountry == null) {
                                getCountry();
                            } else {
                                RowData dataCountry =
                                    RowData.getSpinnerRowDataById(offCountryId, arrAdapCountry);
                                if (dataCountry != null) {
                                    spOffCountry.setSelection(dataCountry.getPosition());
                                }
                                spOffCountry.setOnItemSelectedListener(offCountryListener);
                            }
                            enableField(llOffAdd);
                        }
                    } else {
                        enableField(llOffAdd);
                        unCheckedSameasAbove();
                    }
                }
                break;
            case R.id.btnSubmit:
                tvAnniversaryError.setVisibility(View.GONE);
                if (isLeadEditable()) {
                    if (validateLead()) {
                        MSFDialog.alertDialog(getMainActivity(), 0,
                            getResources().getString(R.string.motilal_oswal), CONFIRM_LEAD_UPDATE,
                            "YES", "NO", true, new MSFDialog.DialogListener() {
                                @Override
                                public void alertDialogAction(MSFDialog.Action action,
                                    Object... data) {
                                    if (action == MSFDialog.Action.OK) {
                                        updateLead();
                                        return;
                                    } else if (action == MSFDialog.Action.CANCEL) {
                                        return;
                                    }
                                }
                            });
                        // updateLead();
                    }
                }
                break;
            case R.id.txtPortfolioSnapshot:
                toggleSectionExpansion(llPortfolio, txtPortfolioSnapshot, outerSectionExpansion);
                break;
            case R.id.llProfile:
                dialogUpdateProfile();
                break;
            case R.id.txtPorfitLoss:
                toggleSectionExpansion(llProfitLoss, txtPorfitLoss, outerSectionExpansion);
                break;
            case R.id.txtAvailableLimits:
                toggleSectionExpansion(llAvailableLimits, txtAvailableLimits,
                    outerSectionExpansion);
                break;
            case R.id.txtBrokingPortfolio:
                toggleSectionExpansion(llBrokingPortfolio, txtBrokingPortfolio,
                    innerSectionExpansion);
                break;
            case R.id.txtAssestPortfolio:
                toggleSectionExpansion(llAssestPortfolio, txtAssestPortfolio,
                    innerSectionExpansion);
                break;
            case R.id.txtCash:
                toggleSectionExpansion(llCash, txtCash, innerSectionExpansion);
                break;
            case R.id.txtBrokingProducts:
                toggleSectionExpansion(llBrokingProducts, txtBrokingProducts,
                    innerSectionExpansion);
                break;
            case R.id.txtAssestProducts:
                toggleSectionExpansion(llAssestProducts, txtAssestProducts, innerSectionExpansion);
                break;
            case R.id.btnBrokPortfolio:
                Toast.makeText(getMainActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnAssetPortfolio:
                Toast.makeText(getMainActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            default: {
                if (view instanceof TextView) {
                    if (newSectionMap == null) {
                        return;
                    }
                    String mTag = view.getTag().toString();
                    if (mTag.startsWith(SectionTagPrefix)) {//listner for new sections
                        if (newSectionMap.containsKey(mTag)) {
                            if (newSectionMap.get(mTag) != null) {
                                toggleSectionExpansion(newSectionMap.get(mTag), (TextView) view);
                            } else {
                                Log.w(TAG, "NoLinear lauout");
                            }
                        }
                    } else {
                        Log.v(TAG, mTag + " Not found");
                    }
                }
            }
            break;
        }
    }

    private void updateLead() {
        addressDetailList.clear();
        addressDetailList.add(new AddressDetailModel(RESIDENCEADDRESS,
            getJsonSafeString(etAdd1.getText().toString().trim()),
            getJsonSafeString(etAdd2.getText().toString().trim()),
            getJsonSafeString(etAdd3.getText().toString().trim()), countryCodeStr,
            ((RowData) spState.getSelectedItem()).getKey(),
            ((RowData) spCity.getSelectedItem()).getKey(), etPincode.getText().toString().trim(),
            cbPrimaryAddress.isChecked(), AccountDetails.getBusinessUnitId()));
        addressDetailList.add(new AddressDetailModel(OFFICEADDRESS,
            getJsonSafeString(etOffAdd1.getText().toString().trim()),
            getJsonSafeString(etOffAdd2.getText().toString().trim()),
            getJsonSafeString(etOffAdd3.getText().toString().trim()), offCountryCodeStr,
            ((RowData) spOffState.getSelectedItem()).getKey(),
            ((RowData) spOffCity.getSelectedItem()).getKey(),
            etOffPincode.getText().toString().trim(), cbOffPrimaryAddress.isChecked(),
            AccountDetails.getBusinessUnitId()));

        LeadDetailModel leadDetailModel = new LeadDetailModel();
        leadDetailModel.setSameAsAbove(cbSameAs.isChecked());
        leadDetailModel.seteMail(etEmail.getText().toString().trim());
        leadDetailModel.setAlternateEmail(etAlternateEmail.getText().toString().trim());
        leadDetailModel.setAlternateMobileNo(etAlternateMobileNumber.getText().toString().trim());
        leadDetailModel.setMobileNo(etMobileNumber.getText().toString().trim());
        leadDetailModel.setEkycMobileNo(etEkycMobileNumber.getText().toString().trim());
        leadDetailModel.setLandLineNo(etLandlineNumber.getText().toString().trim());
        leadDetailModel.setAadharNum(etAadharNumber.getText().toString().trim());
        leadDetailModel.setPanNum(etPanNumber.getText().toString().trim());
        try {
            if (!isBlank(etDob.getText().toString())) {
                String strDob = etDob.getText().toString().trim();
                leadDetailModel.setDob(CRMAppUtil.getDateFromString(strDob, CAL_DATE_FORMAT,
                    DATE_FORMAT_TO_SERVER_MEETING_SEND) + "T00:00:00");
            } else {
                leadDetailModel.setDob("");
            }
            if (!isBlank(etAnniversary.getText().toString())) {
                String strANN = etAnniversary.getText().toString().trim();
                leadDetailModel.setAnniversary(CRMAppUtil.getDateFromString(strANN, CAL_DATE_FORMAT,
                    DATE_FORMAT_TO_SERVER_MEETING_SEND) + "T00:00:00");
            } else {
                leadDetailModel.setAnniversary("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        leadDetailModel.setLeadID(etLeadID.getText().toString().trim());
        leadDetailModel.setBusinessUnitID(AccountDetails.getBusinessUnitId());
        leadDetailModel.setContactPerson(etContactPerson.getText().toString().trim());
        leadDetailModel.setSourceID(sourceID);
        leadDetailModel.setLeadSourceID(leadProspetSourceID);
        leadDetailModel.setLeadSourceDetailsID("0");
        leadDetailModel.setLeadSourceDetails(etProspectSourceDetail.getText().toString());
        leadDetailModel.setName(etLeadName.getText().toString().trim());
        if (etRefereeCode.getText().toString().trim().length() >= 3 && isRefreeCode) {
            leadDetailModel.setRefereeCode(etRefereeCode.getText().toString().trim());
        }
        if (etRefereeName.getText().toString().trim().length() >= 3 && isRefreeCode) {
            leadDetailModel.setRefereeName(etRefereeName.getText().toString().trim());
        }
        leadDetailModel.setRefereeEmail(etRefereeEmail.getText().toString().trim());
        leadDetailModel.setRefereeMobile(etRefereeMobile.getText().toString().trim());
        leadDetailModel.setComments(getJsonSafeString(etComments.getText().toString().trim()));
        leadDetailModel.setProfileID(profileCodeStr);
        leadDetailModel.setProfessionID(professionCodeStr);
        leadDetailModel.setCreatedBy(etCreatedBy.getText().toString().trim());
        leadDetailModel.setCreatedThrough("0");
        leadDetailModel.setCreationDate(
            etCreationDate.getText().toString().trim().replace("p.m.", "PM").replace("a.m", "AM"));
        leadDetailModel.setContactDetailModels(contactDetailList);
        leadDetailModel.setAddressDetailModels(addressDetailList);
        leadDetailModel.setSecondaryRM(null);
        leadDetailModel.setLeadDetailAddProductModels(leadDetailAddProductList);
        leadDetailModel.setLeadFieldValueModels(getSectionJson());
        updateLead(leadDetailModel);
    }

    private void GetStage(final Spinner spStageDialog, final String stageId,
        final LinearLayout progressLayout) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            progressLayout.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());
            if (!isBlank(stageId)) {
                inputMap.put("StageID", stageId);
            }
            String serviceMethod = "Stage/GetStageList";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            arrStage = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        progressLayout.setVisibility(View.GONE);
                                        arrStage.add(new RowData(0, "-1", "Select"));
                                        JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject objDetail = jsonArray.getJSONObject(i);
                                            String stageId = objDetail.isNull(STAGEID) ? ""
                                                : objDetail.getString(STAGEID);
                                            String stageCode = objDetail.isNull(STAGECODE) ? ""
                                                : objDetail.getString(STAGECODE);
                                            String stageName = objDetail.isNull(STAGENAME) ? ""
                                                : objDetail.getString(STAGENAME);
                                            arrStage.add(new RowData(i + 1, stageId, stageName));
                                        }
                                        arrAdapStage = new ArrayAdapter<>(getMainActivity(),
                                            R.layout.simple_spinner_item, arrStage);
                                        arrAdapStage.setDropDownViewResource(R.layout.mdropdown);
                                        spStageDialog.setAdapter(arrAdapStage);
                                        RowData stageData =
                                            getSpinnerRowDataById(stageId, arrAdapStage);
                                        if (stageData != null) {
                                            spStageDialog.setSelection(stageData.getPosition());
                                        }
                                    } else {
                                        progressLayout.setVisibility(View.GONE);
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    progressLayout.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                progressLayout.setVisibility(View.GONE);
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            progressLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            progressLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                progressLayout.setVisibility(View.GONE);
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                //eat it
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            progressLayout.setVisibility(View.GONE);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            progressLayout.setVisibility(View.GONE);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                progressLayout.setVisibility(View.GONE);
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            progressLayout.setVisibility(View.GONE);
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void addNewSections() {
        if (mLeadFieldSectionModelList == null || mLeadFieldSectionModelList.size() <= 0) {
            Log.w(TAG, "mLeadFieldSectionModelList Was null or empty");
            return;
        }
        NewDataLinearLayout.removeAllViews();
        newSectionMap = new HashMap<>();
        newSectionMap.clear();
        for (LeadFieldSectionModel mSection : mLeadFieldSectionModelList) {

            ViewGroup childSection = (ViewGroup) getActivity().getLayoutInflater()
                .inflate(R.layout.include_section, null);
            TextView tvSectionHead = childSection.findViewById(R.id.tvSectionName);
            LinearLayout llSectionContent =
                childSection.findViewById(R.id.llSectionContent);
            tvSectionHead.setTag(SectionTagPrefix + mSection.getSectionCode());
            tvSectionHead.setText(mSection.getSectionHeader());
            toggleSectionExpansion(llSectionContent, tvSectionHead);
            newSectionMap.put(SectionTagPrefix + mSection.getSectionCode(), llSectionContent);
            tvSectionHead.setOnClickListener(this);
            if (mSection.getSelectionList() != null) {
                for (LeadFieldValueModel modal : mSection.getSelectionList()) {
                    switch (modal.getControlType()) {
                        case TextBox: {
                            View childValue = getActivity().getLayoutInflater()
                                .inflate(R.layout.include_edittext_section, null);
                            TextView tvHead = childValue.findViewById(R.id.tvHead);
                            tvHead.setText(modal.getFieldName());

                            EditText edHead = childValue.findViewById(R.id.etSection);
                            modal.setControlObject(edHead);
                            edHead.setText(modal.getLeadField_Value());
                            edHead.setHint(modal.getFieldName());
                            llSectionContent.addView(childValue);
                        }
                        break;
                        case DropDown: {
                            View childValue = getActivity().getLayoutInflater()
                                .inflate(R.layout.include_spiner, null);
                            TextView tvHead = childValue.findViewById(R.id.tvHead);
                            Spinner spSection = childValue.findViewById(R.id.spSection);
                            tvHead.setText(modal.getFieldName());

                            ArrayAdapter<String> arrAdap =
                                new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
                                    modal.getDropdownValueArr());
                            arrAdap.setDropDownViewResource(R.layout.mdropdown);
                            spSection.setAdapter(arrAdap);
                            modal.setControlObject(spSection);
                            llSectionContent.addView(childValue);
                            int pos = modal.getDropdownValueArrPosition();
                            if (pos >= 0) {
                                spSection.setSelection(pos);
                            }
                        }
                        break;
                        default:
                            Log.w(TAG, "Diffrent Control type :" + modal.getControlType());
                            break;
                    }
                }
            }
            NewDataLinearLayout.addView(childSection);
            setEnableDisable(false);
        }
    }

    private CharSequence getSectionJson() {
        StringBuilder sB = new StringBuilder();
        sB.append("[");
        if (mLeadFieldSectionModelList != null) {
            for (LeadFieldSectionModel mSection : mLeadFieldSectionModelList) {
                if (mSection.getSelectionList() != null) {
                    for (LeadFieldValueModel modal : mSection.getSelectionList()) {
                        sB.append(modal.getJson()).append(",");
                    }
                    sB.setLength(sB.length() - 1);
                }
            }
        }
        sB.append("]");
        return sB;
    }

    private void getClientDetail(int uCid) {
        if (uCid <= 0) {
            return;
        }
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put(UCID, String.valueOf(uCid));
            String serviceMethod = "Client/GetClientViewByCommand/";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {

                                if (statusCode == 200) {
                                    leadDetailAddProductList = new ArrayList<>();
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        JSONObject objDetail = response.getJSONObject(RESPONSE);

                                        String strBranchCode = "";
                                        String strUCID = "";
                                        String strChannel = "";
                                        String strClientCode = "";
                                        String strFamilyUCID =
                                            objDetail.getString(FAMILYUCID) == null ? ""
                                                : objDetail.getString(FAMILYUCID);
                                        JSONArray productsArray = objDetail.getJSONArray(PRODUCTS);
                                        for (int productsI = 0; productsI < productsArray.length();
                                            productsI++) {
                                            if (productsI > 0) {
                                                break;
                                            }
                                            JSONObject productsObj =
                                                productsArray.getJSONObject(productsI);
                                            strChannel = (objDetail.isNull(CHANNEL)
                                                || objDetail.getString(CHANNEL) == null) ? ""
                                                : objDetail.getString(CHANNEL);
                                            strUCID = (productsObj.isNull(BRANCH_Code)
                                                || productsObj.getString(UCID) == null) ? ""
                                                : productsObj.getString(UCID);
                                            strBranchCode = (productsObj.isNull(BRANCH_Code)
                                                || productsObj.getString(BRANCH_Code) == null) ? ""
                                                : productsObj.getString(BRANCH_Code);
                                            strClientCode = (productsObj.isNull("HoldingNumber")
                                                || productsObj.getString("HoldingNumber") == null)
                                                ? "" : productsObj.getString("HoldingNumber");

                                            JSONArray advisorsArray =
                                                productsObj.getJSONArray("Advisors");
                                            if (advisorsArray.length() > 0) {
                                                for (int advisorCodeI = 0;
                                                    advisorCodeI < advisorsArray.length();
                                                    advisorCodeI++) {
                                                    JSONObject advisorObj =
                                                        advisorsArray.getJSONObject(advisorCodeI);
                                                    advisorCode = (advisorObj.isNull("AdvisorCode")
                                                        || advisorObj.getString("AdvisorCode")
                                                        == null) ? ""
                                                        : advisorObj.getString("AdvisorCode");
                                                }
                                            }
                                        }

                                        String strAdvisoryType = (objDetail.isNull(ADVISORYTYPE)
                                            || objDetail.getString(ADVISORYTYPE) == null) ? ""
                                            : objDetail.getString(ADVISORYTYPE);

                                        String strPANNumber = (objDetail.isNull(PANNUMBER)
                                            || objDetail.getString(PANNUMBER) == null) ? ""
                                            : objDetail.getString(PANNUMBER);

                                        String strAccountType = (objDetail.isNull(ACCOUNTTYPE)
                                            || objDetail.getString(ACCOUNTTYPE) == null) ? ""
                                            : objDetail.getString(ACCOUNTTYPE);

                                        String strAadhaarNumber = (objDetail.isNull(AADHAARNUMBER)
                                            || objDetail.getString(AADHAARNUMBER) == null) ? ""
                                            : objDetail.getString(AADHAARNUMBER);

                                        String strAccountNo = (objDetail.isNull(ACCOUNT_NO)
                                            || objDetail.getString(ACCOUNT_NO) == null) ? ""
                                            : objDetail.getString(ACCOUNT_NO);
                                        String strProfile = (objDetail.isNull(PROFILE)
                                            || objDetail.getString(PROFILE) == null) ? ""
                                            : objDetail.getString(PROFILE);
                                        String strDefaultBank = (objDetail.isNull(DEFAULT_BANK)
                                            || objDetail.getString(DEFAULT_BANK) == null) ? ""
                                            : objDetail.getString(DEFAULT_BANK);
                                        String clientName = (objDetail.isNull(CLIENT_NAME)
                                            || objDetail.getString(CLIENT_NAME) == null) ? ""
                                            : objDetail.getString(CLIENT_NAME);
                                        String address = (objDetail.isNull(ADDRESS)
                                            || objDetail.getString(ADDRESS) == null) ? ""
                                            : objDetail.getString(ADDRESS);

                                        // tvClientAddress.setText(address+ " " + "Pincode: " + " " + pincode);
                                        tvClientAddress.setText(address);
                                        tvClientName.setText(clientName);
                                        tvPanCard.setText(strPANNumber);
                                        tvUCID.setText(strUCID);
                                        tvClientCode.setText(strClientCode);
                                        tvAccountType.setText(strAccountType);
                                        tvFamilyUCID.setText(strFamilyUCID);
                                        tvBranchCode.setText(strBranchCode);
                                        tvAdvisoryType.setText(strAdvisoryType);
                                        tvAadharCard.setText(strAadhaarNumber);
                                        tvChannel.setText(strChannel);
                                        tvAccountNumber.setText(strAccountNo);
                                        tvProfile.setText(strProfile);
                                        tvDefaultBank.setText(strDefaultBank);
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setIcon(View parent, TextView icon, String Section) {
        if (Section.equalsIgnoreCase("OUTER")) {
            if (parent.getVisibility() == View.VISIBLE) {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_upload_minus, 0);
            } else {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_upload_plus, 0);
            }
        } else if (Section.equalsIgnoreCase("INNER")) {
            if (parent.getVisibility() == View.VISIBLE) {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_button, 0);
            } else {
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_button, 0);
            }
        }
    }

    private void toggleSectionExpansion(View parent, TextView icon, String Expansion) {

        if (Expansion.equalsIgnoreCase("OuterSectionExpansion")) {

            if (parent.getVisibility() == View.VISIBLE) {
                parent.setVisibility(View.GONE);
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.
                    ic_upload_plus, 0);
            } else {
                parent.setVisibility(View.VISIBLE);
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_upload_minus, 0);
            }
        } else if (Expansion.equalsIgnoreCase("InnerSectionExpansion")) {

            if (parent.getVisibility() == View.VISIBLE) {
                parent.setVisibility(View.GONE);
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_button, 0);
            } else {
                parent.setVisibility(View.VISIBLE);
                icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_button, 0);
            }
        }
    }

    public void webServiceportfolioSnapshot(String ucid) {
        final String url =
            "GetClientSummary?Userid=" + AccountDetails.getUserId() + "&ClientUCID=" + ucid;
        showProgress();
        if (MSFStatistics.isNetworkEnabled(getMainActivity())) {
            CRMWebServiceHandler.get(url, null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (statusCode == 200) {
                            if (response.length() != 0) {

                                int bgcolor = 0;

                                JSONArray headerArray = response.getJSONArray("Portfolio Snapshot");
                                JSONObject jsonObject = headerArray.getJSONObject(0);

                                String networth = jsonObject.getString("Networth").equals("") ? ""
                                    : jsonObject.getString("Networth");
                                String daychangeamount =
                                    jsonObject.getString("DayChangeAmount").equals("") ? ""
                                        : jsonObject.getString("DayChangeAmount");
                                String daychangeupdown =
                                    jsonObject.getString("DayChangeUpdown").equals("") ? ""
                                        : jsonObject.getString("DayChangeUpdown");
                                String daychangeper =
                                    jsonObject.getString("DayChangePercentage").equals("") ? ""
                                        : jsonObject.getString("DayChangePercentage");
                                String overallchangeamt =
                                    jsonObject.getString("OverallChangeAmount").equals("") ? ""
                                        : jsonObject.getString("OverallChangeAmount");
                                String overallchangeupdown =
                                    jsonObject.getString("OverallChangeUpdown").equals("") ? ""
                                        : jsonObject.getString("OverallChangeUpdown");
                                String overallchangeperst =
                                    jsonObject.getString("OverallChangePercentage").equals("") ? ""
                                        : jsonObject.getString("OverallChangePercentage");

                                txtNetworth.setText(StringStuff.commaDecorator(networth));
                                txtDaychgamt.setText(StringStuff.commaDecorator(daychangeamount));
                                txtDaychgperst.setText(
                                    "(" + StringStuff.commaDecorator(daychangeper) + " %)");
                                if (daychangeamount.startsWith("-")) {

                                    bgcolor = R.color.action_bar_negative;
                                } else {

                                    bgcolor = R.color.action_bar_positive;
                                }
                                txtDaychgamt.setBackgroundColor(getResources().getColor(bgcolor));

                                if (daychangeper.startsWith("-")) {

                                    bgcolor = R.color.action_bar_negative;
                                } else {

                                    bgcolor = R.color.action_bar_positive;
                                }
                                txtDaychgperst.setTextColor(getResources().getColor(bgcolor));

                                txtOverallchgamt.setText(
                                    StringStuff.commaDecorator(overallchangeamt));
                                txt_overallchgperst.setText(
                                    "(" + StringStuff.commaDecorator(overallchangeperst) + " %)");
                                if (overallchangeamt.startsWith("-")) {

                                    bgcolor = R.color.action_bar_negative;
                                } else {

                                    bgcolor = R.color.action_bar_positive;
                                }
                                txtOverallchgamt.setBackgroundColor(
                                    getResources().getColor(bgcolor));

                                if (overallchangeperst.startsWith("-")) {

                                    bgcolor = R.color.action_bar_negative;
                                } else {

                                    bgcolor = R.color.action_bar_positive;
                                }
                                txt_overallchgperst.setTextColor(getResources().getColor(bgcolor));

                                arrayList_broking = new ArrayList<HashMap<String, String>>();
                                JSONArray headerArray_broking =
                                    response.getJSONArray("Broking Product");
                                for (int x = 0; x < headerArray_broking.length(); x++) {
                                    JSONObject jsonObject_broking =
                                        headerArray_broking.getJSONObject(x);

                                    String product = jsonObject_broking.getString("product");
                                    String marketVal = jsonObject_broking.getString("MarketValue");
                                    String investmentValue =
                                        jsonObject_broking.getString("PurchaseValue");
                                    String overallGainLoss =
                                        jsonObject_broking.getString("OverallGainLoss");
                                    String percentageGainLoss =
                                        jsonObject_broking.getString("PercentageGainLoss");
                                    String productID = jsonObject_broking.getString("ProductID");

                                    brokingMap = new HashMap<String, String>();
                                    brokingMap.put(PRODUCT, product);
                                    brokingMap.put(INVEST_AMT,
                                        StringStuff.commaDecorator(investmentValue));
                                    brokingMap.put(PURCHASE_VALUE,
                                        StringStuff.commaDecorator(marketVal));
                                    brokingMap.put(OVERALL_GAIN_LOSS,
                                        StringStuff.commaDecorator(overallGainLoss));
                                    brokingMap.put(PERCENTAGE_GAIN_LOSS,
                                        StringStuff.commaDecorator(percentageGainLoss) + " %");
                                    brokingMap.put(PRODUCT_ID, productID);
                                    arrayList_broking.add(brokingMap);
                                }

                                final PortfolioSnapshotBrokAdapter navListAdapter =
                                    new PortfolioSnapshotBrokAdapter(
                                        getActivity().getApplicationContext(), arrayList_broking);
                                lvBrok.setAdapter(navListAdapter);

                                navListAdapter.notifyDataSetChanged();
                                setListViewHeightBasedOnChildren(lvBrok);
                                arrayList_asset = new ArrayList<HashMap<String, String>>();
                                JSONArray headerArray_asset =
                                    response.getJSONArray("Asset Product");
                                for (int x = 0; x < headerArray_asset.length(); x++) {
                                    JSONObject jsonObject_asset =
                                        headerArray_asset.getJSONObject(x);

                                    String ProductID = jsonObject_asset.getString("ProductID");
                                    String product_aaset = jsonObject_asset.getString("product");
                                    String investmentValue_aaset =
                                        jsonObject_asset.getString("MarketValue");
                                    String purchaseValue_aaset =
                                        jsonObject_asset.getString("PurchaseValue");
                                    String overallGainLoss_aaset =
                                        jsonObject_asset.getString("OverallGainLoss");
                                    String percentageGainLoss_aaset =
                                        jsonObject_asset.getString("PercentageGainLoss");
                                    String productType = jsonObject_asset.getString("Producttype");

                                    assestMap = new HashMap<String, String>();
                                    assestMap.put(MOSLConstants.PRODUCT_ID, ProductID);
                                    assestMap.put(MOSLConstants.PRODUCT, product_aaset);
                                    assestMap.put(MOSLConstants.INVEST_AMT,
                                        StringStuff.commaDecorator(investmentValue_aaset));
                                    assestMap.put(MOSLConstants.PURCHASE_VALUE,
                                        StringStuff.commaDecorator(purchaseValue_aaset));
                                    assestMap.put(MOSLConstants.OVERALL_GAIN_LOSS,
                                        StringStuff.commaDecorator(overallGainLoss_aaset));
                                    assestMap.put(MOSLConstants.PERCENTAGE_GAIN_LOSS,
                                        StringStuff.commaDecorator(percentageGainLoss_aaset)
                                            + " %");
                                    assestMap.put(MOSLConstants.PRODUCT_TYPE, productType);
                                    arrayList_asset.add(assestMap);
                                }
                            }

                            PortfolioSnapshotAssetAdapter navListAssetAdapter =
                                new PortfolioSnapshotAssetAdapter(
                                    getActivity().getApplicationContext(), arrayList_asset);
                            navListAssetAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(lvAsset);
                            arrayList_cash = new ArrayList<HashMap<String, String>>();

                            JSONArray headerArray_cash = response.getJSONArray("Cash Portfolio");
                            for (int x = 0; x < headerArray_cash.length(); x++) {
                                JSONObject jsonObject_cash = headerArray_cash.getJSONObject(x);

                                String product_cash = jsonObject_cash.getString("Product");
                                String totalLedgerBalance_cash =
                                    jsonObject_cash.getString("TotalLedgerBalance");

                                cashMap = new HashMap<String, String>();
                                cashMap.put(MOSLConstants.PRODUCT, product_cash);
                                cashMap.put(MOSLConstants.TOTAL,
                                    StringStuff.commaDecorator(totalLedgerBalance_cash));

                                arrayList_cash.add(cashMap);
                            }

                            PortfolioSnapshotCashAdapter navListCashAdapter =
                                new PortfolioSnapshotCashAdapter(
                                    getActivity().getApplicationContext(), arrayList_cash);
                            lvCash.setAdapter(navListCashAdapter);
                            navListAssetAdapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(lvCash);
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    hideProgress();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                    hideProgress();
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
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
            hideProgress();
        }
    }

    public void webServicePL(String ucid) {
        final String url =
            "GetClientGainLossSummary?Userid=" + AccountDetails.getUserId() + "&ClientUCID=" + ucid;
        showProgress();
        if (MSFStatistics.isNetworkEnabled(getMainActivity())) {

            CRMWebServiceHandler.get(url, null, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            if (statusCode == 200) {
                                if (response.length() != 0) {

                                    int bgcolor = 0;

                                    JSONArray headerArray = response.getJSONArray("Total Gain Loss");
                                    JSONObject jsonObject = headerArray.getJSONObject(0);

                                    String totalSTGain =
                                        jsonObject.getString("TOTALST_GAIN").equals("") ? ""
                                            : jsonObject.getString("TOTALST_GAIN");
                                    String totalLTGain =
                                        jsonObject.getString("TOTALLT_GAIN").equals("") ? ""
                                            : jsonObject.getString("TOTALLT_GAIN");
                                    String totalGain =
                                        jsonObject.getString("TOTAL_GAIN").equals("") ? ""
                                            : jsonObject.getString("TOTAL_GAIN");
                                    String STGainPer =
                                        jsonObject.getString("ST_GAIN_PER").equals("") ? ""
                                            : jsonObject.getString("ST_GAIN_PER");
                                    String LTGainPer =
                                        jsonObject.getString("LT_GAIN_PER").equals("") ? ""
                                            : jsonObject.getString("LT_GAIN_PER");
                                    tvTotalGainLoss.setText(StringStuff.commaDecorator(totalGain));

                                    tvSTGainLoss.setText(StringStuff.commaDecorator(totalSTGain));
                                    tvSTGainLossPer.setText(
                                        "(" + StringStuff.commaDecorator(STGainPer) + " %)");
                                    tvLTGainLoss.setText(StringStuff.commaDecorator(totalLTGain));
                                    tvLTGainLossPer.setText(
                                        "(" + StringStuff.commaDecorator(LTGainPer) + " %)");

                                    if (totalSTGain.startsWith("-")) {

                                        bgcolor = R.color.action_bar_negative;
                                    } else {

                                        bgcolor = R.color.action_bar_positive;
                                    }
                                    tvSTGainLoss.setBackgroundColor(getResources().getColor(bgcolor));

                                    if (STGainPer.startsWith("-")) {

                                        bgcolor = R.color.action_bar_negative;
                                    } else {

                                        bgcolor = R.color.action_bar_positive;
                                    }
                                    tvSTGainLossPer.setTextColor(getResources().getColor(bgcolor));

                                    if (totalLTGain.startsWith("-")) {

                                        bgcolor = R.color.action_bar_negative;
                                    } else {

                                        bgcolor = R.color.action_bar_positive;
                                    }
                                    tvLTGainLoss.setBackgroundColor(getResources().getColor(bgcolor));

                                    if (LTGainPer.startsWith("-")) {

                                        bgcolor = R.color.action_bar_negative;
                                    } else {

                                        bgcolor = R.color.action_bar_positive;
                                    }
                                    tvLTGainLossPer.setTextColor(getResources().getColor(bgcolor));
                                }
                                arrLstPLBrok = new ArrayList<HashMap<String, String>>();
                                JSONArray headerArray_broking =
                                    response.getJSONArray("Broking Productwise Gainloss");
                                for (int x = 0; x < headerArray_broking.length(); x++) {
                                    JSONObject jsonObject_broking =
                                        headerArray_broking.getJSONObject(x);

                                    String productId =
                                        jsonObject_broking.getString("ASSET_SUB_TYPE_ID");
                                    String product = jsonObject_broking.getString("PRODUCT");
                                    String STGainLoss = jsonObject_broking.getString("ST_GAIN");
                                    String LTGainLoss = jsonObject_broking.getString("LT_GAIN");
                                    String totalGainLoss = jsonObject_broking.getString("TOTAL_GAIN");
                                    String assetSubTypeID =
                                        jsonObject_broking.getString("ASSET_SUB_TYPE_ID");

                                    brokPLHashMap = new HashMap<String, String>();
                                    brokPLHashMap.put(PRODUCT_ID, productId);
                                    brokPLHashMap.put(PRODUCT, product);
                                    brokPLHashMap.put(STGL, StringStuff.commaDecorator(STGainLoss));
                                    brokPLHashMap.put(LTGL, StringStuff.commaDecorator(LTGainLoss));
                                    brokPLHashMap.put(TOTAL_GAIN_LOSS,
                                        StringStuff.commaDecorator(totalGainLoss));
                                    brokPLHashMap.put(ASSET_SUB_TYPE_ID, assetSubTypeID);

                                    arrLstPLBrok.add(brokPLHashMap);
                                }

                                PortfolioPLBrokingAdapter plBrokingAdapter =
                                    new PortfolioPLBrokingAdapter(getActivity().getApplicationContext(),
                                        arrLstPLBrok);
                                lvPLBrok.setAdapter(plBrokingAdapter);
                                plBrokingAdapter.notifyDataSetChanged();
                                setListViewHeightBasedOnChildren(lvPLBrok);
                                arrLstPLAsset = new ArrayList<HashMap<String, String>>();
                                JSONArray jsonArrPLAsset =
                                    response.getJSONArray("Asset Productwise Gainloss");
                                for (int x = 0; x < jsonArrPLAsset.length(); x++) {
                                    JSONObject jsonObject_broking = jsonArrPLAsset.getJSONObject(x);
                                    String productId =
                                        jsonObject_broking.getString("ASSET_SUB_TYPE_ID");
                                    String product = jsonObject_broking.getString("PRODUCT");
                                    String STGainLoss = jsonObject_broking.getString("ST_GAIN");
                                    String LTGainLoss = jsonObject_broking.getString("LT_GAIN");
                                    String totalGainLoss = jsonObject_broking.getString("TOTAL_GAIN");

                                    assetPLHashMap = new HashMap<String, String>();
                                    assetPLHashMap.put(PRODUCT_ID, productId);
                                    assetPLHashMap.put(PRODUCT, product);
                                    assetPLHashMap.put(STGL, StringStuff.commaDecorator(STGainLoss));
                                    assetPLHashMap.put(LTGL, StringStuff.commaDecorator(LTGainLoss));
                                    assetPLHashMap.put(TOTAL_GAIN_LOSS,
                                        StringStuff.commaDecorator(totalGainLoss));

                                    arrLstPLAsset.add(assetPLHashMap);
                                }
                                PortfolioPLAssetAdapter plAssetAdapter =
                                    new PortfolioPLAssetAdapter(getActivity().getApplicationContext(),
                                        arrLstPLAsset);
                                lvPLAsset.setAdapter(plAssetAdapter);
                                plAssetAdapter.notifyDataSetChanged();
                                setListViewHeightBasedOnChildren(lvPLAsset);

                                arrLstPLLimits = new ArrayList<HashMap<String, String>>();
                                JSONArray jsonArrPLLimits = response.getJSONArray("Available Limits");
                                for (int x = 0; x < jsonArrPLLimits.length(); x++) {
                                    JSONObject jsonObject_broking = jsonArrPLLimits.getJSONObject(x);

                                    String cash = jsonObject_broking.getString("CASH");
                                    String amt = jsonObject_broking.getString("AMOUNT");

                                    limitsPLHashMap = new HashMap<String, String>();
                                    limitsPLHashMap.put(CASH, cash);
                                    limitsPLHashMap.put(AMOUNT, StringStuff.commaDecorator(amt));

                                    arrLstPLLimits.add(limitsPLHashMap);
                                }
                                PortfolioPLLimitsAdapter plLimitsAdapter =
                                    new PortfolioPLLimitsAdapter(getActivity().getApplicationContext(),
                                        arrLstPLLimits);
                                lvPLLimits.setAdapter(plLimitsAdapter);
                                plAssetAdapter.notifyDataSetChanged();
                                setListViewHeightBasedOnChildren(lvPLLimits);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                        Throwable throwable) {
                        hideProgress();
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
                }

            );
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
            hideProgress();
        }
    }

    private void RefreeData(JSONObject response, String type,
        final ProgressBar pbLoadingIndicatorRefreeCode) {
        try {
            String status = response.getString(STATUS);
            if (status.equalsIgnoreCase("true")) {
                isRefreeCode = true;
                pbLoadingIndicatorRefreeCode.setVisibility(View.GONE);
                JSONObject jsonObject = response.getJSONObject("Response");
                refereeCode =
                    jsonObject.isNull("RefereeCode") ? "" : jsonObject.getString("RefereeCode");
                refereeName =
                    jsonObject.isNull("RefereeName") ? "" : jsonObject.getString("RefereeName");
                String RefereeEmail =
                    jsonObject.isNull("RefereeEmail") ? "" : jsonObject.getString("RefereeEmail");
                String RefereeMobile =
                    jsonObject.isNull("RefereeMobile") ? "" : jsonObject.getString("RefereeMobile");
                if (type.equalsIgnoreCase("BYCODE")) {
                    etRefereeName.setText(refereeName);
                } else {
                    etRefereeCode.setText(refereeCode);
                }
                etRefereeEmail.setText(RefereeEmail);
                etRefereeMobile.setText(RefereeMobile);
            } else {
                isRefreeCode = false;
                pbLoadingIndicatorRefreeCode.setVisibility(View.GONE);
                refereeCode = "";
                refereeName = "";
                etRefereeEmail.setText("");
                etRefereeMobile.setText("");
                if (type.equalsIgnoreCase("BYCODE")) {
                    etRefereeName.setText("");
                } else {
                    etRefereeCode.setText("");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void unCheckedSameasAbove() {
        etOffAdd1.setText("");
        etOffAdd2.setText("");
        etOffAdd3.setText("");
        etOffPincode.setText("");
        spOffCountry.setSelection(0);
        spOffState.setSelection(0);
        spOffCity.setSelection(0);
    }

    private void dialogUpdateProfile() {
        final Dialog dialog = new Dialog(getMainActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_update_profile);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final Spinner spProfileUpdate = dialog.findViewById(R.id.spProfile);
        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        arrAdapProfile =
            new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrSelectProfile);
        arrAdapProfile.setDropDownViewResource(R.layout.mdropdown);
        spProfileUpdate.setAdapter(arrAdapProfile);
        spProfileUpdate.setOnItemSelectedListener(profileListener);
        arrAdapProfile.notifyDataSetChanged();
        RowData productData =
            RowData.getSpinnerRowDataByValue(tvProfile.getText().toString().trim(), arrAdapProfile);
        if (productData != null) {
            spProfileUpdate.setSelection(productData.getPosition());
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                    //TODO call progress bar
                    showProgress();
                    Map inputMap = new HashMap<>();
                    inputMap.put("UCID", tvUCID.getText().toString());
                    inputMap.put("ClientPotential",
                        ((RowData) spProfileUpdate.getSelectedItem()).getValue());
                    inputMap.put("IsPotentialAutoUpdated", false);
                    String serviceMethod = "Client/SaveClientProfile";
                    try {
                        CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(),
                            inputMap, new JsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode,
                                    cz.msebera.android.httpclient.Header[] headers,
                                    JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    try {
                                        if (statusCode == 200) {
                                            String status = response.getString(STATUS);
                                            hideProgress();
                                            if (status.equalsIgnoreCase("true")) {
                                                JSONObject Response_obj =
                                                    response.getJSONObject(RESPONSE);
                                                String clientPotential =
                                                    Response_obj.isNull("ClientPotential") ? ""
                                                        : Response_obj.getString("ClientPotential");
                                                String ApprovalMessage =
                                                    Response_obj.isNull("ApprovalMessage")
                                                        ? ""
                                                        : Response_obj.getString("ApprovalMessage");
                                                if (!ApprovalMessage.isEmpty()) {
                                                    alertDialog(getMainActivity(), 0,
                                                        getResources().getString(
                                                            R.string.motilal_oswal),
                                                        ApprovalMessage, "OK", true, null);
                                                } else {
                                                    tvProfile.setText(clientPotential);
                                                }
                                            } else {
                                                hideProgress();
                                                String errorMsg =
                                                    CRMMOSLCommon.getErrorMsgFromResponse(response);
                                                alertDialog(getMainActivity(), 0,
                                                    getResources().getString(
                                                        R.string.motilal_oswal), errorMsg, "OK",
                                                    true, null);
                                            }
                                        } else {
                                            hideProgress();
                                        }
                                        hideProgress();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        hideProgress();
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                    }
                                }

                                @Override
                                public void onSuccess(int statusCode,
                                    cz.msebera.android.httpclient.Header[] headers,
                                    JSONArray response) {
                                    super.onSuccess(statusCode, headers, response);
                                }

                                @Override
                                public void onSuccess(int statusCode,
                                    cz.msebera.android.httpclient.Header[] headers,
                                    String responseString) {
                                    super.onSuccess(statusCode, headers, responseString);
                                    hideProgress();
                                }

                                @Override
                                public void onFailure(int statusCode,
                                    cz.msebera.android.httpclient.Header[] headers,
                                    Throwable throwable, JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    try {
                                        hideProgress();
                                        if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                UNAUTHORIZE_USER, "OK", false,
                                                new MSFDialog.DialogListener() {
                                                    @Override
                                                    public void alertDialogAction(
                                                        MSFDialog.Action action, Object... data) {
                                                        if (action == MSFDialog.Action.OK) {
                                                            Intent i = new Intent(getMainActivity(),
                                                                LoginActivity.class);
                                                            startActivity(i);
                                                            getActivity().finish();
                                                        }
                                                    }
                                                });
                                            return;
                                        } else {
                                            String error = CRMMOSLCommon.getErrorMsgFromResponse(
                                                errorResponse);
                                            if (CRMAppUtil.isBlank(error)) {
                                                error = SOME_SERVER_ERROR_OCCURRED;
                                            }
                                            MSFDialog.alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                error, "OK", true, null);
                                            return;
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        MSFDialog.alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                    }
                                }

                                @Override
                                public void onFailure(int statusCode,
                                    cz.msebera.android.httpclient.Header[] headers,
                                    Throwable throwable, JSONArray errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    hideProgress();
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                }

                                @Override
                                public void onFailure(int statusCode,
                                    cz.msebera.android.httpclient.Header[] headers,
                                    String responseString, Throwable throwable) {
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                    hideProgress();
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                }
                            });
                    } catch (Exception e) {
                        e.printStackTrace();
                        alertDialog(getMainActivity(), 0,
                            getResources().getString(R.string.motilal_oswal),
                            SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        hideProgress();
                    }
                } else {
                    hideProgress();
                    MSFDialog.alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK",
                        true, null);
                }
                dialog.dismiss();
            }
        });
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private Map lowMarginSubmit(List<LowMargin> lowMarginList, boolean isDefault) {
        Map inputMap = new HashMap();
        for (LowMargin model : lowMarginList) {
            if (model.isMandatory()) {
                if (model.getEditText().getText().toString().trim().length() < 3) {
                    model.getEditText().requestFocus();
                    model.getEditText().setError("Atleast enter 3 digits");
                    inputMap.clear();
                    return inputMap;
                }
                inputMap.put(model.getKeyName(), model.getEditText().getText().toString().trim());
            }
        }
        return inputMap;
    }

    public class LeadDetailAddProductAdapter extends BaseAdapter {

        private final LayoutInflater inflater;
        Context mContext;
        private List<LeadDetailAddProductModel> leadDetailAddProductModelList;

        public LeadDetailAddProductAdapter(Context c,
            List<LeadDetailAddProductModel> leadDetailAddProductModelList) {
            inflater = LayoutInflater.from(c);
            mContext = c;
            this.leadDetailAddProductModelList = leadDetailAddProductModelList;
        }

        @Override
        public int getCount() {
            return leadDetailAddProductModelList.size();
        }

        @Override
        public Object getItem(int i) {
            return leadDetailAddProductModelList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final LeadDetailAddProductModel leadDetailAddProductModel =
                leadDetailAddProductModelList.get(i);

            view = inflater.inflate(R.layout.item_list_add_product, viewGroup, false);

            final Holder holder = new Holder();
            holder.tvProduct = view.findViewById(R.id.tvProduct);
            holder.tvProductSpecialist = view.findViewById(R.id.tvProductSpecialist);
            holder.tvPrimaryRM = view.findViewById(R.id.tvPrimaryRM);
            holder.tvStage = view.findViewById(R.id.tvStage);
            holder.tvProductID = view.findViewById(R.id.tvProductID);
            holder.tvStatus = view.findViewById(R.id.tvStatus);
            holder.llDeleteLeadDetail = view.findViewById(R.id.llDeleteLeadDetail);
            holder.llAddProduct = view.findViewById(R.id.llAddProduct);
            holder.llCrossSellingProducts =
                view.findViewById(R.id.llCrossSellingProducts);
            holder.llChildCrossSellingProduct =
                view.findViewById(R.id.llChildCrossSellingProduct);
            holder.tvAddInvestmentProduct =
                view.findViewById(R.id.tvAddInvestmentProduct);
            holder.tvProduct.setText(leadDetailAddProductModel.getInterestedInValue());
            holder.tvProductSpecialist.setText(leadDetailAddProductModel.getProductSpecialist());
            holder.tvPrimaryRM.setText(leadDetailAddProductModel.getPrimaryRM());
            holder.tvStage.setText(leadDetailAddProductModel.getStageValue());
            holder.tvProductID.setText(leadDetailAddProductModel.getInterestedInValue());
            holder.tvStatus.setText(leadDetailAddProductModel.getStatusValue());
            if (leadDetailAddProductModel.getInterestedInValue().equalsIgnoreCase("Equity")
                || leadDetailAddProductModel.getInterestedInValue().equalsIgnoreCase("MF")) {
                holder.llCrossSellingProducts.setVisibility(View.VISIBLE);
            } else {
                holder.llCrossSellingProducts.setVisibility(View.GONE);
            }

            for (final CrossSellingProductsModel mSection : leadDetailAddProductModel.getCrossSellingProductsModelList()) {
                ViewGroup childSection = (ViewGroup) getActivity().getLayoutInflater()
                    .inflate(R.layout.child_cross_selling_product, null);
                TextView tvCrossSellingProduct =
                    childSection.findViewById(R.id.tvCrossSellingProduct);
                TextView tvCrosellingStatus =
                    childSection.findViewById(R.id.tvCrosellingStatus);
                LinearLayout llCrossSellingUpdateProduct =
                    childSection.findViewById(R.id.llCrossSellingUpdateProduct);
                tvCrosellingStatus.setText(mSection.getCrossSellingStatusName());
                tvCrossSellingProduct.setText(mSection.getCrossSellingProductName());
                llCrossSellingUpdateProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(getMainActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow()
                            .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(
                            R.layout.dialog_leadview_update_cross_selling_product);
                        dialog.getWindow()
                            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.getWindow()
                            .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                        final Spinner spProductIdDialog =
                            dialog.findViewById(R.id.spProductId);
                        final Spinner spStatusDialog = dialog.findViewById(R.id.spStatus);
                        final LinearLayout progressLayout =
                            dialog.findViewById(R.id.customProgress);
                        spProductIdDialog.setEnabled(false);
                        //Text errors
                        tvCrossSellingProductIdError =
                            dialog.findViewById(R.id.tvspProductIdError);
                        tvCrossSellingStatusError =
                            dialog.findViewById(R.id.tvspStatusError);

                        Button btnSave = dialog.findViewById(R.id.btnSave);
                        Button btnClose = dialog.findViewById(R.id.btnClose);

                        newGetCrossSellingStatus(spStatusDialog, mSection.getCrossSellingStatusId(),
                            progressLayout);
                        newGetCrossSellingProductId(leadDetailAddProductModel.getInterestedIn(),
                            spProductIdDialog, mSection.getCrossSellingProductId(), progressLayout);

                        spStatusDialog.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                    int position, long id) {
                                    if (position > 0) {
                                        tvCrossSellingStatusError.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (spStatusDialog.getSelectedItemPosition() < 1) {
                                    //TODO Show Error
                                    tvCrossSellingStatusError.setVisibility(View.VISIBLE);
                                    tvCrossSellingStatusError.setText("Select Status");
                                    return;
                                }

                                if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                                    showProgress();
                                    Map inputMap = new HashMap<>();
                                    inputMap.put("LeadId", etLeadID.getText().toString().trim());
                                    inputMap.put("InterestedInId",
                                        leadDetailAddProductModel.getInterestedIn());
                                    inputMap.put("CrossSellingStatusId",
                                        ((RowData) spStatusDialog.getSelectedItem()).getKey());
                                    inputMap.put("CrossSellingProductId",
                                        ((RowData) spProductIdDialog.getSelectedItem()).getKey());
                                    inputMap.put("LeadCrossSellingProductId",
                                        mSection.getLeadCrossSellingProductId());

                                    String serviceMethod = "Lead/SaveLeadCrossSellingProduct";
                                    hideKeyboard(leadViewDetailView);
                                    try {
                                        CRMWebServiceHandler.post(getMainActivity(), serviceMethod,
                                            getHeader(), inputMap, new JsonHttpResponseHandler() {

                                                @Override
                                                public void onSuccess(int statusCode,
                                                    cz.msebera.android.httpclient.Header[] headers,
                                                    JSONObject response) {
                                                    super.onSuccess(statusCode, headers, response);
                                                    try {
                                                        if (statusCode == 200) {
                                                            String status =
                                                                response.getString(STATUS);
                                                            if (status.equalsIgnoreCase("true")) {
                                                                hideProgress();
                                                                alertDialog(getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    "Cross Selling Product Saved successfully",
                                                                    "Ok", true,
                                                                    new MSFDialog.DialogListener() {
                                                                        @Override
                                                                        public void alertDialogAction(
                                                                            MSFDialog.Action action,
                                                                            Object... data) {
                                                                            if (action
                                                                                == MSFDialog.Action.OK) {
                                                                                leadSection();
                                                                            }
                                                                        }
                                                                    });
                                                            } else {
                                                                hideProgress();
                                                                String errorMsg =
                                                                    CRMMOSLCommon.getErrorMsgFromResponse(
                                                                        response);
                                                                alertDialog(getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    errorMsg, "OK", true, null);
                                                            }
                                                        } else {
                                                            hideProgress();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        hideProgress();
                                                        alertDialog(getMainActivity(), 0,
                                                            getResources().getString(
                                                                R.string.motilal_oswal),
                                                            SOME_ERROR_OCCURED_PLEASE_RETRY, "OK",
                                                            true, null);
                                                    }
                                                }

                                                @Override
                                                public void onSuccess(int statusCode,
                                                    cz.msebera.android.httpclient.Header[] headers,
                                                    JSONArray response) {
                                                    super.onSuccess(statusCode, headers, response);
                                                    hideProgress();
                                                }

                                                @Override
                                                public void onSuccess(int statusCode,
                                                    cz.msebera.android.httpclient.Header[] headers,
                                                    String responseString) {
                                                    super.onSuccess(statusCode, headers,
                                                        responseString);
                                                    hideProgress();
                                                }

                                                @Override
                                                public void onFailure(int statusCode,
                                                    cz.msebera.android.httpclient.Header[] headers,
                                                    Throwable throwable, JSONObject errorResponse) {
                                                    super.onFailure(statusCode, headers, throwable,
                                                        errorResponse);
                                                    try {
                                                        hideProgress();
                                                        if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                                            alertDialog(getMainActivity(), 0,
                                                                getResources().getString(
                                                                    R.string.motilal_oswal),
                                                                UNAUTHORIZE_USER, "OK", false,
                                                                new MSFDialog.DialogListener() {
                                                                    @Override
                                                                    public void alertDialogAction(
                                                                        MSFDialog.Action action,
                                                                        Object... data) {
                                                                        if (action
                                                                            == MSFDialog.Action.OK) {
                                                                            Intent i = new Intent(
                                                                                getMainActivity(),
                                                                                LoginActivity.class);
                                                                            startActivity(i);
                                                                            getActivity().finish();
                                                                        }
                                                                    }
                                                                });
                                                            return;
                                                        } else {
                                                            String error =
                                                                CRMMOSLCommon.getErrorMsgFromResponse(
                                                                    errorResponse);
                                                            if (CRMAppUtil.isBlank(error)) {
                                                                error = SOME_SERVER_ERROR_OCCURRED;
                                                            }
                                                            MSFDialog.alertDialog(getMainActivity(),
                                                                0, getResources().getString(
                                                                    R.string.motilal_oswal), error,
                                                                "OK", true, null);
                                                            return;
                                                        }
                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                        MSFDialog.alertDialog(getMainActivity(), 0,
                                                            getResources().getString(
                                                                R.string.motilal_oswal),
                                                            SOME_SERVER_ERROR_OCCURRED, "OK", true,
                                                            null);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(int statusCode,
                                                    cz.msebera.android.httpclient.Header[] headers,
                                                    Throwable throwable, JSONArray errorResponse) {
                                                    super.onFailure(statusCode, headers, throwable,
                                                        errorResponse);
                                                    hideProgress();
                                                    alertDialog(getMainActivity(), 0,
                                                        getResources().getString(
                                                            R.string.motilal_oswal),
                                                        SOME_SERVER_ERROR_OCCURRED, "OK", true,
                                                        null);
                                                }

                                                @Override
                                                public void onFailure(int statusCode,
                                                    cz.msebera.android.httpclient.Header[] headers,
                                                    String responseString, Throwable throwable) {
                                                    super.onFailure(statusCode, headers,
                                                        responseString, throwable);
                                                    hideProgress();
                                                    alertDialog(getMainActivity(), 0,
                                                        getResources().getString(
                                                            R.string.motilal_oswal),
                                                        SOME_SERVER_ERROR_OCCURRED, "OK", true,
                                                        null);
                                                }
                                            });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        hideProgress();
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        INTERNET_CONNECTION, "OK", true, null);
                                }

                                dialog.dismiss();
                            }
                        });

                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                hideKeyboard(leadViewDetailView);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                holder.llChildCrossSellingProduct.addView(childSection);
            }
            holder.tvAddInvestmentProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(getMainActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setContentView(R.layout.dialog_leadview_add_cross_selling_product);
                    dialog.getWindow()
                        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow()
                        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    final Spinner spProductIdDialog =
                        dialog.findViewById(R.id.spProductId);
                    final Spinner spStatusDialog = dialog.findViewById(R.id.spStatus);
                    final LinearLayout progressLayout =
                        dialog.findViewById(R.id.customProgress);
                    //Text errors
                    tvCrossSellingProductIdError =
                        dialog.findViewById(R.id.tvspProductIdError);
                    tvCrossSellingStatusError =
                        dialog.findViewById(R.id.tvspStatusError);

                    Button btnSave = dialog.findViewById(R.id.btnSave);
                    Button btnClose = dialog.findViewById(R.id.btnClose);

                    newGetCrossSellingStatus(spStatusDialog, "", progressLayout);
                    newGetCrossSellingProductId(leadDetailAddProductModel.getInterestedIn(),
                        spProductIdDialog, "", progressLayout);

                    spProductIdDialog.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                int position, long id) {
                                if (position > 0) {
                                    tvCrossSellingProductIdError.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    spStatusDialog.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                int position, long id) {
                                if (position > 0) {
                                    tvCrossSellingStatusError.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (spProductIdDialog.getSelectedItemPosition() < 1) {
                                //TODO Show Error
                                tvCrossSellingProductIdError.setVisibility(View.VISIBLE);
                                tvCrossSellingProductIdError.setText("Select Product Id");
                                return;
                            } else if (spStatusDialog.getSelectedItemPosition() < 1) {
                                //TODO Show Error
                                tvCrossSellingStatusError.setVisibility(View.VISIBLE);
                                tvCrossSellingStatusError.setText("Select Status");
                                return;
                            }

                            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                                showProgress();
                                Map inputMap = new HashMap<>();
                                inputMap.put("LeadId", etLeadID.getText().toString().trim());
                                inputMap.put("InterestedInId",
                                    leadDetailAddProductModel.getInterestedIn());

                                inputMap.put("CrossSellingStatusId",
                                    ((RowData) spStatusDialog.getSelectedItem()).getKey());
                                inputMap.put("CrossSellingProductId",
                                    ((RowData) spProductIdDialog.getSelectedItem()).getKey());

                                String serviceMethod = "Lead/SaveLeadCrossSellingProduct";
                                hideKeyboard(leadViewDetailView);
                                try {
                                    CRMWebServiceHandler.post(getMainActivity(), serviceMethod,
                                        getHeader(), inputMap, new JsonHttpResponseHandler() {

                                            @Override
                                            public void onSuccess(int statusCode,
                                                cz.msebera.android.httpclient.Header[] headers,
                                                JSONObject response) {
                                                super.onSuccess(statusCode, headers, response);
                                                //after success code goes here
                                                try {
                                                    if (statusCode == 200) {
                                                        String status = response.getString(STATUS);
                                                        if (status.equalsIgnoreCase("true")) {
                                                            hideProgress();
                                                            alertDialog(getMainActivity(), 0,
                                                                getResources().getString(
                                                                    R.string.motilal_oswal),
                                                                "Cross Selling Product Saved Successfully",
                                                                "Ok", true,
                                                                new MSFDialog.DialogListener() {
                                                                    @Override
                                                                    public void alertDialogAction(
                                                                        MSFDialog.Action action,
                                                                        Object... data) {
                                                                        if (action
                                                                            == MSFDialog.Action.OK) {
                                                                            leadSection();
                                                                        }
                                                                    }
                                                                });
                                                        } else {
                                                            hideProgress();
                                                            String errorMsg =
                                                                CRMMOSLCommon.getErrorMsgFromResponse(
                                                                    response);
                                                            alertDialog(getMainActivity(), 0,
                                                                getResources().getString(
                                                                    R.string.motilal_oswal),
                                                                errorMsg, "OK", true, null);
                                                        }
                                                    } else {
                                                        hideProgress();
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    hideProgress();
                                                    alertDialog(getMainActivity(), 0,
                                                        getResources().getString(
                                                            R.string.motilal_oswal),
                                                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true,
                                                        null);
                                                }
                                            }

                                            @Override
                                            public void onSuccess(int statusCode,
                                                cz.msebera.android.httpclient.Header[] headers,
                                                JSONArray response) {
                                                super.onSuccess(statusCode, headers, response);
                                                hideProgress();
                                            }

                                            @Override
                                            public void onSuccess(int statusCode,
                                                cz.msebera.android.httpclient.Header[] headers,
                                                String responseString) {
                                                super.onSuccess(statusCode, headers,
                                                    responseString);
                                                hideProgress();
                                            }

                                            @Override
                                            public void onFailure(int statusCode,
                                                cz.msebera.android.httpclient.Header[] headers,
                                                Throwable throwable, JSONObject errorResponse) {
                                                super.onFailure(statusCode, headers, throwable,
                                                    errorResponse);
                                                try {
                                                    hideProgress();
                                                    if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                                        alertDialog(getMainActivity(), 0,
                                                            getResources().getString(
                                                                R.string.motilal_oswal),
                                                            UNAUTHORIZE_USER, "OK", false,
                                                            new MSFDialog.DialogListener() {
                                                                @Override
                                                                public void alertDialogAction(
                                                                    MSFDialog.Action action,
                                                                    Object... data) {
                                                                    if (action
                                                                        == MSFDialog.Action.OK) {
                                                                        Intent i = new Intent(
                                                                            getMainActivity(),
                                                                            LoginActivity.class);
                                                                        startActivity(i);
                                                                        getActivity().finish();
                                                                    }
                                                                }
                                                            });
                                                        return;
                                                    } else {
                                                        String error =
                                                            CRMMOSLCommon.getErrorMsgFromResponse(
                                                                errorResponse);
                                                        if (CRMAppUtil.isBlank(error)) {
                                                            error = SOME_SERVER_ERROR_OCCURRED;
                                                        }
                                                        MSFDialog.alertDialog(getMainActivity(), 0,
                                                            getResources().getString(
                                                                R.string.motilal_oswal), error,
                                                            "OK", true, null);
                                                        return;
                                                    }
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                                        getResources().getString(
                                                            R.string.motilal_oswal),
                                                        SOME_SERVER_ERROR_OCCURRED, "OK", true,
                                                        null);
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode,
                                                cz.msebera.android.httpclient.Header[] headers,
                                                Throwable throwable, JSONArray errorResponse) {
                                                super.onFailure(statusCode, headers, throwable,
                                                    errorResponse);
                                                hideProgress();
                                                alertDialog(getMainActivity(), 0,
                                                    getResources().getString(
                                                        R.string.motilal_oswal),
                                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                            }

                                            @Override
                                            public void onFailure(int statusCode,
                                                cz.msebera.android.httpclient.Header[] headers,
                                                String responseString, Throwable throwable) {
                                                super.onFailure(statusCode, headers, responseString,
                                                    throwable);
                                                hideProgress();
                                                alertDialog(getMainActivity(), 0,
                                                    getResources().getString(
                                                        R.string.motilal_oswal),
                                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                            }
                                        });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    hideProgress();
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            } else {
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    INTERNET_CONNECTION, "OK", true, null);
                            }

                            dialog.dismiss();
                        }
                    });

                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            hideKeyboard(leadViewDetailView);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
            if (leadDetailAddProductModel.isDefault()
                || leadDetailAddProductModel.isCrossSellingProduct()) {
                holder.llDeleteLeadDetail.setVisibility(View.GONE);
            } else {
                holder.llDeleteLeadDetail.setVisibility(View.VISIBLE);
            }
            holder.llDeleteLeadDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isLeadEditable()) {
                        if (!leadDetailAddProductModel.isDefault()) {
                            alertDialog(getMainActivity(), 0, "Delete Confirmation",
                                "Are you sure you want to delete the product", "Yes", "No", true,
                                new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action,
                                        Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                                                showProgress();
                                                Map inputMap = new HashMap<>();
                                                inputMap.put(LEAD_ID,
                                                    etLeadID.getText().toString().trim());
                                                inputMap.put("InterestedIn",
                                                    leadDetailAddProductModel.getInterestedIn());
                                                inputMap.put("LeadProductMappingID",
                                                    leadDetailAddProductModel.getLeadMappingProductId());

                                                inputMap.put("ProductID",
                                                    leadDetailAddProductModel.getInterestedIn());

                                                inputMap.put("PrimaryRM",
                                                    leadDetailAddProductModel.getPrimaryRM());
                                                inputMap.put("AssignedBy",
                                                    AccountDetails.getEmployeeCode());
                                                inputMap.put("AssignedByDate",
                                                    CRMAppUtil.getDBDate());
                                                inputMap.put("StageID",
                                                    leadDetailAddProductModel.getStageId());
                                                inputMap.put("StatusID",
                                                    leadDetailAddProductModel.getStatusId());
                                                String serviceMethod =
                                                    "Lead/DeleteLeadProductMapping";

                                                try {
                                                    CRMWebServiceHandler.post(getMainActivity(),
                                                        serviceMethod, getHeader(), inputMap,
                                                        new JsonHttpResponseHandler() {

                                                            @Override
                                                            public void onSuccess(int statusCode,
                                                                cz.msebera.android.httpclient.Header[] headers,
                                                                JSONObject response) {
                                                                super.onSuccess(statusCode, headers,
                                                                    response);
                                                                //after success code goes here
                                                                try {
                                                                    if (statusCode == 200) {
                                                                        String status =
                                                                            response.getString(
                                                                                STATUS);
                                                                        if (status.equalsIgnoreCase(
                                                                            "true")) {
                                                                            hideProgress();
                                                                            alertDialog(
                                                                                getMainActivity(),
                                                                                0,
                                                                                getResources().getString(
                                                                                    R.string.motilal_oswal),
                                                                                "Products Deleted Successfully",
                                                                                "Ok", true, null);
                                                                            LeadDetailAddProductModel
                                                                                model = getProduct(
                                                                                leadDetailAddProductModel
                                                                                    .getInterestedIn());
                                                                            leadDetailAddProductModelList
                                                                                .remove(model);
                                                                            notifyDataSetChanged();
                                                                            setListViewHeightBasedOnChildren(
                                                                                lvAddProduct);
                                                                        } else {
                                                                            hideProgress();
                                                                            String errorMsg =
                                                                                CRMMOSLCommon.getErrorMsgFromResponse(
                                                                                    response);
                                                                            alertDialog(
                                                                                getMainActivity(),
                                                                                0,
                                                                                getResources().getString(
                                                                                    R.string.motilal_oswal),
                                                                                errorMsg, "OK",
                                                                                true, null);
                                                                        }
                                                                    } else {
                                                                        hideProgress();
                                                                    }
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                    hideProgress();
                                                                    alertDialog(getMainActivity(),
                                                                        0, getResources().getString(
                                                                            R.string.motilal_oswal),
                                                                        SOME_ERROR_OCCURED_PLEASE_RETRY,
                                                                        "OK", true, null);
                                                                }
                                                            }

                                                            @Override
                                                            public void onSuccess(int statusCode,
                                                                cz.msebera.android.httpclient.Header[] headers,
                                                                JSONArray response) {
                                                                super.onSuccess(statusCode, headers,
                                                                    response);
                                                                hideProgress();
                                                            }

                                                            @Override
                                                            public void onSuccess(int statusCode,
                                                                cz.msebera.android.httpclient.Header[] headers,
                                                                String responseString) {
                                                                super.onSuccess(statusCode, headers,
                                                                    responseString);
                                                                hideProgress();
                                                            }

                                                            @Override
                                                            public void onFailure(int statusCode,
                                                                cz.msebera.android.httpclient.Header[] headers,
                                                                Throwable throwable,
                                                                JSONObject errorResponse) {
                                                                super.onFailure(statusCode, headers,
                                                                    throwable, errorResponse);
                                                                try {
                                                                    hideProgress();
                                                                    if (statusCode
                                                                        == UNAUTHORIZE_STATUS_CODE) {
                                                                        alertDialog(
                                                                            getMainActivity(), 0,
                                                                            getResources().getString(
                                                                                R.string.motilal_oswal),
                                                                            UNAUTHORIZE_USER, "OK",
                                                                            false,
                                                                            new MSFDialog.DialogListener() {
                                                                                @Override
                                                                                public void alertDialogAction(
                                                                                    MSFDialog.Action action,
                                                                                    Object... data) {
                                                                                    if (action
                                                                                        == MSFDialog.Action.OK) {
                                                                                        Intent i =
                                                                                            new Intent(
                                                                                                getMainActivity(),
                                                                                                LoginActivity.class);
                                                                                        startActivity(
                                                                                            i);
                                                                                        getActivity()
                                                                                            .finish();
                                                                                    }
                                                                                }
                                                                            });
                                                                        return;
                                                                    } else {
                                                                        alertDialog(
                                                                            getMainActivity(), 0,
                                                                            getResources().getString(
                                                                                R.string.motilal_oswal),
                                                                            SOME_SERVER_ERROR_OCCURRED,
                                                                            "OK", true, null);
                                                                        return;
                                                                    }
                                                                } catch (Exception ex) {
                                                                    //eat it
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(int statusCode,
                                                                cz.msebera.android.httpclient.Header[] headers,
                                                                Throwable throwable,
                                                                JSONArray errorResponse) {
                                                                super.onFailure(statusCode, headers,
                                                                    throwable, errorResponse);
                                                                hideProgress();
                                                                alertDialog(getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    SOME_SERVER_ERROR_OCCURRED,
                                                                    "OK", true, null);
                                                            }

                                                            @Override
                                                            public void onFailure(int statusCode,
                                                                cz.msebera.android.httpclient.Header[] headers,
                                                                String responseString,
                                                                Throwable throwable) {
                                                                super.onFailure(statusCode, headers,
                                                                    responseString, throwable);
                                                                hideProgress();
                                                                alertDialog(getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    SOME_SERVER_ERROR_OCCURRED,
                                                                    "OK", true, null);
                                                            }
                                                        });
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    hideProgress();
                                                    alertDialog(getMainActivity(), 0,
                                                        getResources().getString(
                                                            R.string.motilal_oswal),
                                                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true,
                                                        null);
                                                }
                                            } else {
                                                hideProgress();
                                                alertDialog(getMainActivity(), 0,
                                                    getResources().getString(
                                                        R.string.motilal_oswal),
                                                    INTERNET_CONNECTION, "OK", true, null);
                                            }
                                        }
                                    }
                                });
                        } else {
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                "Can't Delete All Products", "OK", true, null);
                        }
                    }
                }
            });

            holder.llAddProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isLeadEditable()) {
                        editProduct(leadDetailAddProductModel.getInterestedIn(),
                            leadDetailAddProductModel.getStageId(),
                            leadDetailAddProductModel.getStatusId(),
                            leadDetailAddProductModel.getProductSpecialist(),
                            leadDetailAddProductModel.getLeadMappingProductId(),
                            leadDetailAddProductModel.getPrimaryRM(),
                            leadDetailAddProductModel.isDefault());
                    }
                }
            });
            return view;
        }

        public LeadDetailAddProductModel getProduct(String id) {
            for (LeadDetailAddProductModel model : leadDetailAddProductModelList) {
                if (model.getInterestedIn().equals(id)) {
                    return model;
                }
            }
            return null;
        }

        public boolean getAvailableAddnDoc(String productName) {
            for (LeadDetailAddProductModel model : leadDetailAddProductModelList) {
                if (model.getInterestedIn().equalsIgnoreCase(productName)) {
                    return true;
                }
            }
            return false;
        }

        public void updateProductData(String interestIn, String stageId, String stageValue,
            String statusId, String statusValue, String productSpecialist, String primaryRM) {
            for (LeadDetailAddProductModel model : leadDetailAddProductModelList) {
                if (model.getInterestedIn().equals(interestIn)) {
                    model.setStageId(stageId);
                    model.setStageValue(stageValue);
                    model.setStatusId(statusId);
                    model.setStatusValue(statusValue);
                    model.setProductSpecialist(productSpecialist);
                    model.setPrimaryRM(primaryRM);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        private void newGetStatus(String productId, final Spinner spStatus, final String statusId,
            final LinearLayout progressLayout) {
            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                progressLayout.setVisibility(View.VISIBLE);
                Map inputMap = new HashMap<>();
                inputMap.put("StageID", productId);
                inputMap.put("Status", "1");

                String serviceMethod = "LeadStatus/GetStatusList";

                try {
                    CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(),
                        inputMap, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                //after success code goes here
                                arrStatus = new ArrayList<>();
                                try {
                                    if (statusCode == 200) {
                                        String status = response.getString(STATUS);
                                        if (status.equalsIgnoreCase("true")) {
                                            progressLayout.setVisibility(View.GONE);
                                            arrStatus.add(new RowData(0, "-1", "Select"));
                                            JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject objDetail = jsonArray.getJSONObject(i);
                                                String statusId = objDetail.isNull(STATUSID)
                                                    ? "" : objDetail.getString(STATUSID);
                                                String statusName = objDetail.isNull(STATUSNAME)
                                                    ? "" : objDetail.getString(STATUSNAME);
                                                arrStatus.add(
                                                    new RowData(i + 1, statusId, statusName));
                                            }
                                            arrAdapStatus = new ArrayAdapter<>(getMainActivity(),
                                                R.layout.simple_spinner_item, arrStatus);
                                            arrAdapStatus.setDropDownViewResource(
                                                R.layout.mdropdown);
                                            spStatus.setAdapter(arrAdapStatus);
                                            RowData stageData =
                                                getSpinnerRowDataById(statusId, arrAdapStatus);
                                            if (stageData != null) {
                                                spStatus.setSelection(stageData.getPosition());
                                            }
                                        } else {
                                            progressLayout.setVisibility(View.GONE);
                                            String errorMsg =
                                                CRMMOSLCommon.getErrorMsgFromResponse(response);
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                errorMsg, "OK", true, null);
                                        }
                                    } else {
                                        progressLayout.setVisibility(View.GONE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    progressLayout.setVisibility(View.GONE);
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                                progressLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                                progressLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                try {
                                    progressLayout.setVisibility(View.GONE);
                                    if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            UNAUTHORIZE_USER, "OK", false,
                                            new MSFDialog.DialogListener() {
                                                @Override
                                                public void alertDialogAction(
                                                    MSFDialog.Action action, Object... data) {
                                                    if (action == MSFDialog.Action.OK) {
                                                        Intent i = new Intent(getMainActivity(),
                                                            LoginActivity.class);
                                                        startActivity(i);
                                                        getActivity().finish();
                                                    }
                                                }
                                            });
                                        return;
                                    } else {
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                        return;
                                    }
                                } catch (Exception ex) {
                                    //eat it
                                    ex.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                progressLayout.setVisibility(View.GONE);
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                progressLayout.setVisibility(View.GONE);
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        });
                } catch (Exception e) {
                    e.printStackTrace();
                    progressLayout.setVisibility(View.GONE);
                    alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                }
            } else {
                progressLayout.setVisibility(View.GONE);
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    INTERNET_CONNECTION, "OK", true, null);
            }
        }

        private void newGetCrossSellingProductId(final String intrestedIn, final Spinner spStatus,
            final String statusId, final LinearLayout progressLayout) {
            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                progressLayout.setVisibility(View.VISIBLE);
                Map inputMap = new HashMap<>();
                inputMap.put("InterestedInId", intrestedIn);
                inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

                String serviceMethod = "CrossSellingProductMapping/GetCrossSellingProduct";

                try {
                    CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(),
                        inputMap, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                arrCrossSellingProductId = new ArrayList<>();
                                try {
                                    if (statusCode == 200) {
                                        String status = response.getString(STATUS);
                                        if (status.equalsIgnoreCase("true")) {
                                            progressLayout.setVisibility(View.GONE);
                                            arrCrossSellingProductId.add(
                                                new RowData(0, "-1", "Select"));
                                            JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject objDetail = jsonArray.getJSONObject(i);
                                                String statusId =
                                                    objDetail.isNull("CrossSellingProductId") ?
                                                        "" : objDetail.getString(
                                                        "CrossSellingProductId");
                                                String statusName =
                                                    objDetail.isNull("CrossSellingProductName") ?
                                                        "" : objDetail.getString(
                                                        "CrossSellingProductName");
                                                arrCrossSellingProductId.add(
                                                    new RowData(i + 1, statusId, statusName));
                                            }
                                            arrAdapCrossSellingProductId =
                                                new ArrayAdapter<>(getMainActivity(),
                                                    R.layout.simple_spinner_item,
                                                    arrCrossSellingProductId);
                                            arrAdapCrossSellingProductId.setDropDownViewResource(
                                                R.layout.mdropdown);
                                            spStatus.setAdapter(arrAdapCrossSellingProductId);
                                            RowData stageData = getSpinnerRowDataById(statusId,
                                                arrAdapCrossSellingProductId);
                                            if (stageData != null) {
                                                spStatus.setSelection(stageData.getPosition());
                                            }
                                        } else {
                                            progressLayout.setVisibility(View.GONE);
                                            String errorMsg =
                                                CRMMOSLCommon.getErrorMsgFromResponse(response);
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                errorMsg, "OK", true, null);
                                        }
                                    } else {
                                        progressLayout.setVisibility(View.GONE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    progressLayout.setVisibility(View.GONE);
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                                progressLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                                progressLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                try {
                                    progressLayout.setVisibility(View.GONE);
                                    if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            UNAUTHORIZE_USER, "OK", false,
                                            new MSFDialog.DialogListener() {
                                                @Override
                                                public void alertDialogAction(
                                                    MSFDialog.Action action, Object... data) {
                                                    if (action == MSFDialog.Action.OK) {
                                                        Intent i = new Intent(getMainActivity(),
                                                            LoginActivity.class);
                                                        startActivity(i);
                                                        getActivity().finish();
                                                    }
                                                }
                                            });
                                        return;
                                    } else {
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                        return;
                                    }
                                } catch (Exception ex) {
                                    //eat it
                                    ex.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                progressLayout.setVisibility(View.GONE);
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                progressLayout.setVisibility(View.GONE);
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        });
                } catch (Exception e) {
                    e.printStackTrace();
                    progressLayout.setVisibility(View.GONE);
                    alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                }
            } else {
                progressLayout.setVisibility(View.GONE);
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    INTERNET_CONNECTION, "OK", true, null);
            }
        }

        private void newGetCrossSellingStatus(final Spinner spStatus, final String statusId,
            final LinearLayout progressLayout) {
            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                progressLayout.setVisibility(View.VISIBLE);
                Map inputMap = new HashMap<>();
                inputMap.put("LookupTypeId", "111");
                inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

                String serviceMethod = "Master/GetAllLookup";

                try {
                    CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(),
                        inputMap, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                arrCrossSellingStatus = new ArrayList<>();
                                try {
                                    if (statusCode == 200) {
                                        String status = response.getString(STATUS);
                                        if (status.equalsIgnoreCase("true")) {
                                            progressLayout.setVisibility(View.GONE);
                                            arrCrossSellingStatus.add(
                                                new RowData(0, "-1", "Select"));
                                            JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject objDetail = jsonArray.getJSONObject(i);
                                                String statusId = objDetail.isNull(LOOKUPID) ?
                                                    "" : objDetail.getString(LOOKUPID);
                                                String statusName = objDetail.isNull(LOOKUPNAME) ?
                                                    "" : objDetail.getString(LOOKUPNAME);
                                                arrCrossSellingStatus.add(
                                                    new RowData(i + 1, statusId, statusName));
                                            }
                                            arrAdapCrossSellingStatus =
                                                new ArrayAdapter<>(getMainActivity(),
                                                    R.layout.simple_spinner_item,
                                                    arrCrossSellingStatus);
                                            arrAdapCrossSellingStatus.setDropDownViewResource(
                                                R.layout.mdropdown);
                                            spStatus.setAdapter(arrAdapCrossSellingStatus);
                                            RowData stageData = getSpinnerRowDataById(statusId,
                                                arrAdapCrossSellingStatus);
                                            if (stageData != null) {
                                                spStatus.setSelection(stageData.getPosition());
                                            }
                                        } else {
                                            progressLayout.setVisibility(View.GONE);
                                            String errorMsg =
                                                CRMMOSLCommon.getErrorMsgFromResponse(response);
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                errorMsg, "OK", true, null);
                                        }
                                    } else {
                                        progressLayout.setVisibility(View.GONE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    progressLayout.setVisibility(View.GONE);
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                                progressLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                                progressLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                try {
                                    progressLayout.setVisibility(View.GONE);
                                    if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            UNAUTHORIZE_USER, "OK", false,
                                            new MSFDialog.DialogListener() {
                                                @Override
                                                public void alertDialogAction(
                                                    MSFDialog.Action action, Object... data) {
                                                    if (action == MSFDialog.Action.OK) {
                                                        Intent i = new Intent(getMainActivity(),
                                                            LoginActivity.class);
                                                        startActivity(i);
                                                        getActivity().finish();
                                                    }
                                                }
                                            });
                                        return;
                                    } else {
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                        return;
                                    }
                                } catch (Exception ex) {
                                    //eat it
                                    ex.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                progressLayout.setVisibility(View.GONE);
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                progressLayout.setVisibility(View.GONE);
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        });
                } catch (Exception e) {
                    e.printStackTrace();
                    progressLayout.setVisibility(View.GONE);
                    alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                }
            } else {
                progressLayout.setVisibility(View.GONE);
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    INTERNET_CONNECTION, "OK", true, null);
            }
        }

        private void lowMargin(String productId, final LinearLayout progressLayout,
            final LinearLayout llLowMarginAdd, final boolean isDefault) {
            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                progressLayout.setVisibility(View.VISIBLE);
                Map inputMap = new HashMap<>();
                inputMap.put("Id", productId);

                String serviceMethod = "Lead/GetTbl_StageWiseFieldConfigurationByStatus";

                try {
                    CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(),
                        inputMap, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                arrStatus = new ArrayList<>();
                                try {
                                    if (statusCode == 200) {
                                        String status = response.getString(STATUS);
                                        //todo changes
                                        if (status.equalsIgnoreCase("true")) {
                                            progressLayout.setVisibility(View.GONE);
                                            lowMarginList.clear();
                                            JSONArray jsonArray = response.getJSONArray(RESPONSE);
                                            if (jsonArray.length() > 0) {
                                                isLowMargin = true;
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject objDetail =
                                                        jsonArray.getJSONObject(i);
                                                    String viewName = "Low Margin";
                                                    boolean ForDefaultProductOnly =
                                                        objDetail.getBoolean(
                                                            "ForDefaultProductOnly");
                                                    boolean IsMandetory =
                                                        objDetail.getBoolean("IsMandetory");
                                                    String keyName =
                                                        objDetail.isNull("FieldSystemName") ? ""
                                                            : objDetail.getString(
                                                                "FieldSystemName");
                                                    if (objDetail.has("FieldDisplayName")) {
                                                        viewName =
                                                            objDetail.isNull("FieldDisplayName")
                                                                ? ""
                                                                : objDetail.getString(
                                                                    "FieldDisplayName");
                                                    }
                                                    lowMarginList.add(
                                                        new LowMargin(viewName, keyName,
                                                            IsMandetory, ForDefaultProductOnly));
                                                }
                                                llLowMarginAdd.removeAllViews();
                                                for (int lowMarginIndex = 0;
                                                    lowMarginIndex < lowMarginList.size();
                                                    lowMarginIndex++) {
                                                    LowMargin modal =
                                                        lowMarginList.get(lowMarginIndex);
                                                    if (isDefault == modal.isDefaultProduct()) {
                                                        isLowMargin = true;
                                                        View childValue =
                                                            getActivity().getLayoutInflater()
                                                                .inflate(
                                                                    R.layout.include_edittext_lowmargin,
                                                                    null);
                                                        TextView tvHead =
                                                            childValue.findViewById(
                                                                R.id.tvHead);
                                                        final EditText edHead =
                                                            childValue.findViewById(
                                                                R.id.etSection);
                                                        if (modal.isMandatory()) {
                                                            tvHead.setText(
                                                                modal.getViewName() + "*");
                                                            edHead.setHint(
                                                                "Atleast enter 3 digits");
                                                        } else {
                                                            tvHead.setText(modal.getViewName());
                                                        }

                                                        modal.setEditText(edHead);
                                                        llLowMarginAdd.addView(childValue);
                                                    } else {
                                                        isLowMargin = false;
                                                        llLowMarginAdd.removeAllViews();
                                                        lowMarginList.clear();
                                                    }
                                                }
                                            } else {
                                                llLowMarginAdd.removeAllViews();
                                                lowMarginList.clear();
                                                isLowMargin = false;
                                            }
                                        } else {
                                            llLowMarginAdd.removeAllViews();
                                            lowMarginList.clear();
                                            isLowMargin = false;
                                            progressLayout.setVisibility(View.GONE);
                                            String errorMsg =
                                                CRMMOSLCommon.getErrorMsgFromResponse(response);
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                errorMsg, "OK", true, null);
                                        }
                                    } else {
                                        llLowMarginAdd.removeAllViews();
                                        lowMarginList.clear();
                                        isLowMargin = false;
                                        progressLayout.setVisibility(View.GONE);
                                    }
                                } catch (Exception e) {
                                    llLowMarginAdd.removeAllViews();
                                    lowMarginList.clear();
                                    isLowMargin = false;
                                    e.printStackTrace();
                                    progressLayout.setVisibility(View.GONE);
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                                progressLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                                progressLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                try {
                                    llLowMarginAdd.removeAllViews();
                                    progressLayout.setVisibility(View.GONE);
                                    isLowMargin = false;
                                    lowMarginList.clear();
                                    if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            UNAUTHORIZE_USER, "OK", false,
                                            new MSFDialog.DialogListener() {
                                                @Override
                                                public void alertDialogAction(
                                                    MSFDialog.Action action, Object... data) {
                                                    if (action == MSFDialog.Action.OK) {
                                                        Intent i = new Intent(getMainActivity(),
                                                            LoginActivity.class);
                                                        startActivity(i);
                                                        getActivity().finish();
                                                    }
                                                }
                                            });
                                        return;
                                    } else {
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                        return;
                                    }
                                } catch (Exception ex) {
                                    //eat it
                                    ex.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                progressLayout.setVisibility(View.GONE);
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                progressLayout.setVisibility(View.GONE);
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        });
                } catch (Exception e) {
                    e.printStackTrace();
                    progressLayout.setVisibility(View.GONE);
                    alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                }
            } else {
                progressLayout.setVisibility(View.GONE);
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    INTERNET_CONNECTION, "OK", true, null);
            }
        }

        private void editProduct(final String interestIn, final String stageId,
            final String statusId, String productSpecialist, final String leadMappingId,
            final String asignedTo, final boolean isDefault) {
            final Dialog dialog = new Dialog(getMainActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_leadview_add_product);
            dialog.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            final Spinner spStageDialog = dialog.findViewById(R.id.spStage);
            final Spinner spStatusDialog = dialog.findViewById(R.id.spStatus);
            final LinearLayout llLowMarginAdd =
                dialog.findViewById(R.id.lowMarginAdd);
            tvspIntrestError = dialog.findViewById(R.id.tvspIntrestError);
            tvspStatusError = dialog.findViewById(R.id.tvspStatusError);
            tvspStageError = dialog.findViewById(R.id.tvspStageError);
            final LinearLayout progressLayout =
                dialog.findViewById(R.id.customProgress);
            LinearLayout llInterestedIn = dialog.findViewById(R.id.llInterestedIn);
            Button btnSave = dialog.findViewById(R.id.btnSave);
            Button btnClose = dialog.findViewById(R.id.btnClose);
            final ClearableEditText productSpecialistEdit =
                dialog.findViewById(R.id.dacProductSpecialist);
            productSpecialistEdit.setText(productSpecialist);
            productSpecialistEdit.setClearIconVisible(true);
            productSpecialistEdit.setHint(getResources().getString(R.string.str_prod_spl));
            productSpecialistEdit.setListener(new ClearableEditText.Listener() {
                @Override
                public void didClearText() {
                    specialistEdit = "";
                }
            });
            productSpecialistEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getWindow()
                        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    final CustomListPickerDialog searchListProductSpecialist =
                        new CustomListPickerDialog(getMainActivity());

                    searchListProductSpecialist.setThreshold(SEARCH_LENTH);
                    searchListProductSpecialist.show();
                    searchListProductSpecialist.setCustomTitle(
                        getResources().getString(R.string.str_prod_spl));
                    CustomListPickerDialog.ListPickerListneable productSpecialistViewListner =
                        new CustomListPickerDialog.ListPickerListneable() {
                            @Override
                            public void onListItemSelect(Parcelable data) {
                                EmployeeModel clientModel = (EmployeeModel) data;
                                if (clientModel.isNoData()) {
                                    return;
                                }
                                productSpecialistEdit.setText(clientModel.getAttendeeName());
                                productSpecialistEdit.setClearIconVisible(true);
                                specialistEdit = clientModel.getAttendeeCode();
                                searchListProductSpecialist.dismiss();
                            }

                            @Override
                            public void onSearchTextChanged(final CharSequence constraint) {

                                final ArrayList<EmployeeModel> modelList = new ArrayList<>();
                                try {
                                    if (constraint != null) {
                                        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                                            searchListProductSpecialist.showProgress(View.VISIBLE);
                                            Map requestMap = new HashMap();
                                            requestMap.put("EmployeeName",
                                                URLEncoder.encode(constraint.toString(), "utf-8"));
                                            requestMap.put("EmployeeCode",
                                                URLEncoder.encode(constraint.toString(), "utf-8"));

                                            primaryRMAdd =
                                                CRMAppUtil.isBlank(leadOwner.trim()) ? advisorCode
                                                    : leadOwner.trim();
                                            requestMap.put("PrimaryRM", primaryRMAdd);
                                            String serviceMethod =
                                                "UserApi/AutoCompleteProductSpecialist";
                                            hideKeyboard(leadViewDetailView);
                                            try {
                                                CRMWebServiceHandler.post(getMainActivity(),
                                                    serviceMethod, getHeader(), requestMap,
                                                    new JsonHttpResponseHandler() {

                                                        @Override
                                                        public void onSuccess(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            JSONObject response) {
                                                            super.onSuccess(statusCode, headers,
                                                                response);
                                                            try {
                                                                searchListProductSpecialist.showProgress(
                                                                    View.GONE);
                                                                if (statusCode == 200) {
                                                                    String status =
                                                                        response.getString(STATUS);
                                                                    if (status.equalsIgnoreCase(
                                                                        "true")) {
                                                                        JSONArray jsonArray =
                                                                            response.getJSONArray(
                                                                                RESPONSE);
                                                                        for (int i = 0;
                                                                            i < jsonArray.length();
                                                                            i++) {
                                                                            JSONObject jsonObject =
                                                                                jsonArray.getJSONObject(
                                                                                    i);

                                                                            String employeeName =
                                                                                jsonObject.getString(
                                                                                    EMPLOYEE_NAME)
                                                                                    == null ? ""
                                                                                    : jsonObject.getString(
                                                                                        EMPLOYEE_NAME);
                                                                            String employeeCode =
                                                                                jsonObject.getString(
                                                                                    EMPLOYEE_CODE)
                                                                                    == null ? ""
                                                                                    : jsonObject.getString(
                                                                                        EMPLOYEE_CODE);
                                                                            String BranchCode =
                                                                                jsonObject.getString(
                                                                                    BRANCH_Code)
                                                                                    == null ? ""
                                                                                    : jsonObject.getString(
                                                                                        BRANCH_Code);//region Check self Empcode and don't add to list
                                                                            if (employeeCode.equalsIgnoreCase(
                                                                                AccountDetails.getEmployeeCode())) {
                                                                                continue;
                                                                            }
                                                                            modelList.add(
                                                                                new EmployeeModel(
                                                                                    employeeName,
                                                                                    employeeCode,
                                                                                    BranchCode,
                                                                                    false));
                                                                        }
                                                                    } else {
                                                                        searchListProductSpecialist.showProgress(
                                                                            View.GONE);
                                                                        modelList.add(
                                                                            EmployeeModel.getNoData(
                                                                                CRMMOSLCommon.getErrorMsgFromResponse(
                                                                                    response)));
                                                                    }
                                                                    if (modelList.isEmpty()) {
                                                                        modelList.add(
                                                                            EmployeeModel.getNoData(
                                                                                ATTENDEEE_NOT_FOUND_MESSAGE));
                                                                    }
                                                                    final AttendeeAdapter
                                                                        mYAdapter =
                                                                        new AttendeeAdapter(
                                                                            getMainActivity(),
                                                                            modelList);

                                                                    getMainActivity().runOnUiThread(
                                                                        new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                searchListProductSpecialist
                                                                                    .setAdapter(
                                                                                        mYAdapter);
                                                                            }
                                                                        });
                                                                } else {
                                                                    searchListProductSpecialist.showProgress(
                                                                        View.GONE);
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                searchListProductSpecialist.showProgress(
                                                                    View.GONE);
                                                                alertDialog(getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    SOME_ERROR_OCCURED_PLEASE_RETRY,
                                                                    "OK", true, null);
                                                            }
                                                        }

                                                        @Override
                                                        public void onSuccess(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            JSONArray response) {
                                                            super.onSuccess(statusCode, headers,
                                                                response);
                                                            searchListProductSpecialist.showProgress(
                                                                View.GONE);
                                                        }

                                                        @Override
                                                        public void onSuccess(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            String responseString) {
                                                            super.onSuccess(statusCode, headers,
                                                                responseString);
                                                            searchListProductSpecialist.showProgress(
                                                                View.GONE);
                                                        }

                                                        @Override
                                                        public void onFailure(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            Throwable throwable,
                                                            JSONObject errorResponse) {
                                                            super.onFailure(statusCode, headers,
                                                                throwable, errorResponse);
                                                            try {
                                                                searchListProductSpecialist.showProgress(
                                                                    View.GONE);
                                                                if (statusCode
                                                                    == UNAUTHORIZE_STATUS_CODE) {
                                                                    alertDialog(getMainActivity(),
                                                                        0, getResources().getString(
                                                                            R.string.motilal_oswal),
                                                                        UNAUTHORIZE_USER, "OK",
                                                                        false,
                                                                        new MSFDialog.DialogListener() {
                                                                            @Override
                                                                            public void alertDialogAction(
                                                                                MSFDialog.Action action,
                                                                                Object... data) {
                                                                                if (action
                                                                                    == MSFDialog.Action.OK) {
                                                                                    Intent i =
                                                                                        new Intent(
                                                                                            getMainActivity(),
                                                                                            LoginActivity.class);
                                                                                    startActivity(
                                                                                        i);
                                                                                    getActivity().finish();
                                                                                }
                                                                            }
                                                                        });
                                                                    return;
                                                                } else {
                                                                    String error =
                                                                        CRMMOSLCommon.getErrorMsgFromResponse(
                                                                            errorResponse);
                                                                    if (CRMAppUtil.isBlank(error)) {
                                                                        error =
                                                                            SOME_SERVER_ERROR_OCCURRED;
                                                                    }
                                                                    MSFDialog.alertDialog(
                                                                        getMainActivity(), 0,
                                                                        getResources().getString(
                                                                            R.string.motilal_oswal),
                                                                        error, "OK", true, null);
                                                                    return;
                                                                }
                                                            } catch (Exception ex) {
                                                                ex.printStackTrace();
                                                                MSFDialog.alertDialog(
                                                                    getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    SOME_SERVER_ERROR_OCCURRED,
                                                                    "OK", true, null);
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            Throwable throwable,
                                                            JSONArray errorResponse) {
                                                            super.onFailure(statusCode, headers,
                                                                throwable, errorResponse);
                                                            searchListProductSpecialist.showProgress(
                                                                View.GONE);
                                                            alertDialog(getMainActivity(), 0,
                                                                getResources().getString(
                                                                    R.string.motilal_oswal),
                                                                SOME_SERVER_ERROR_OCCURRED, "OK",
                                                                true, null);
                                                        }

                                                        @Override
                                                        public void onFailure(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            String responseString,
                                                            Throwable throwable) {
                                                            super.onFailure(statusCode, headers,
                                                                responseString, throwable);
                                                            searchListProductSpecialist.showProgress(
                                                                View.GONE);
                                                            alertDialog(getMainActivity(), 0,
                                                                getResources().getString(
                                                                    R.string.motilal_oswal),
                                                                SOME_SERVER_ERROR_OCCURRED, "OK",
                                                                true, null);
                                                        }
                                                    });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                searchListProductSpecialist.showProgress(View.GONE);
                                                alertDialog(getMainActivity(), 0,
                                                    getResources().getString(
                                                        R.string.motilal_oswal),
                                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true,
                                                    null);
                                            }
                                        } else {
                                            searchListProductSpecialist.showProgress(View.GONE);
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                INTERNET_CONNECTION, "OK", true, null);
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    searchListProductSpecialist.showProgress(View.GONE);
                                }
                            }
                        };
                    searchListProductSpecialist.setmListner(productSpecialistViewListner);
                }
            });
            final ClearableEditText actAssignTo =
                dialog.findViewById(R.id.actAssignTo);
            actAssignTo.setText(asignedTo);
            actAssignTo.setClearIconVisible(true);
            actAssignTo.setHint(getResources().getString(R.string.str_assing));
            actAssignTo.setListener(new ClearableEditText.Listener() {
                @Override
                public void didClearText() {
                    primaryRMEdit = "";
                }
            });
            actAssignTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getWindow()
                        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    final CustomListPickerDialog searchList =
                        new CustomListPickerDialog(getMainActivity());
                    searchList.show();
                    searchList.setCustomTitle(getResources().getString(R.string.str_assing));

                    searchList.setThreshold(SEARCH_LENTH);
                    CustomListPickerDialog.ListPickerListneable assingViewListner =
                        new CustomListPickerDialog.ListPickerListneable() {
                            @Override
                            public void onListItemSelect(Parcelable data) {
                                EmployeeModel clientModel = (EmployeeModel) data;
                                if (clientModel.isNoData()) {
                                    return;
                                }
                                actAssignTo.setText(clientModel.getAttendeeName());
                                primaryRMEdit = clientModel.getAttendeeCode();
                                actAssignTo.setClearIconVisible(true);
                                searchList.dismiss();
                            }

                            @Override
                            public void onSearchTextChanged(final CharSequence constraint) {

                                final ArrayList<EmployeeModel> modelList = new ArrayList<>();
                                try {
                                    if (constraint != null) {
                                        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                                            searchList.showProgress(View.VISIBLE);
                                            Map requestMap = new HashMap();
                                            requestMap.put("EmployeeName",
                                                URLEncoder.encode(constraint.toString(), "utf-8"));
                                            requestMap.put("EmployeeCode",
                                                URLEncoder.encode(constraint.toString(), "utf-8"));

                                            String serviceMethod = "UserApi/GetUserBusinesprofile";
                                            hideKeyboard(leadViewDetailView);
                                            try {
                                                CRMWebServiceHandler.post(getMainActivity(),
                                                    serviceMethod, getHeader(), requestMap,
                                                    new JsonHttpResponseHandler() {

                                                        @Override
                                                        public void onSuccess(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            JSONObject response) {
                                                            super.onSuccess(statusCode, headers,
                                                                response);
                                                            searchList.showProgress(View.GONE);
                                                            try {
                                                                if (statusCode == 200) {
                                                                    String status =
                                                                        response.getString(STATUS);
                                                                    if (status.equalsIgnoreCase(
                                                                        "true")) {
                                                                        JSONArray jsonArray =
                                                                            response.getJSONArray(
                                                                                RESPONSE);
                                                                        for (int i = 0;
                                                                            i < jsonArray.length();
                                                                            i++) {
                                                                            JSONObject jsonObject =
                                                                                jsonArray.getJSONObject(
                                                                                    i);
                                                                            String employeeName =
                                                                                jsonObject.getString(
                                                                                    EMPLOYEE_NAME)
                                                                                    == null ? ""
                                                                                    : jsonObject.getString(
                                                                                        EMPLOYEE_NAME);
                                                                            String employeeCode =
                                                                                jsonObject.getString(
                                                                                    EMPLOYEE_CODE)
                                                                                    == null ? ""
                                                                                    : jsonObject.getString(
                                                                                        EMPLOYEE_CODE);
                                                                            String BranchCode =
                                                                                jsonObject.getString(
                                                                                    BRANCH_Code)
                                                                                    == null ? ""
                                                                                    : jsonObject.getString(
                                                                                        BRANCH_Code);//region Check self Empcode and don't add to list
                                                                            if (employeeCode.equalsIgnoreCase(
                                                                                AccountDetails.getEmployeeCode())) {
                                                                                continue;
                                                                            }
                                                                            modelList.add(
                                                                                new EmployeeModel(
                                                                                    employeeName,
                                                                                    employeeCode,
                                                                                    BranchCode,
                                                                                    false));
                                                                        }
                                                                    } else {
                                                                        searchList.showProgress(
                                                                            View.GONE);
                                                                        modelList.add(
                                                                            EmployeeModel.getNoData(
                                                                                CRMMOSLCommon.getErrorMsgFromResponse(
                                                                                    response)));
                                                                    }
                                                                    if (modelList.isEmpty()) {
                                                                        modelList.add(
                                                                            EmployeeModel.getNoData(
                                                                                ATTENDEEE_NOT_FOUND_MESSAGE));
                                                                    }
                                                                    final AttendeeAdapter
                                                                        mYAdapter =
                                                                        new AttendeeAdapter(
                                                                            getMainActivity(),
                                                                            modelList);

                                                                    getMainActivity().runOnUiThread(
                                                                        new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                searchList.setAdapter(
                                                                                    mYAdapter);
                                                                            }
                                                                        });
                                                                } else {
                                                                    searchList.showProgress(
                                                                        View.GONE);
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                searchList.showProgress(View.GONE);
                                                                alertDialog(getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    SOME_ERROR_OCCURED_PLEASE_RETRY,
                                                                    "OK", true, null);
                                                            }
                                                        }

                                                        @Override
                                                        public void onSuccess(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            JSONArray response) {
                                                            super.onSuccess(statusCode, headers,
                                                                response);
                                                            searchList.showProgress(View.GONE);
                                                        }

                                                        @Override
                                                        public void onSuccess(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            String responseString) {
                                                            super.onSuccess(statusCode, headers,
                                                                responseString);
                                                            searchList.showProgress(View.GONE);
                                                        }

                                                        @Override
                                                        public void onFailure(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            Throwable throwable,
                                                            JSONObject errorResponse) {
                                                            super.onFailure(statusCode, headers,
                                                                throwable, errorResponse);
                                                            try {
                                                                searchList.showProgress(View.GONE);
                                                                if (statusCode
                                                                    == UNAUTHORIZE_STATUS_CODE) {
                                                                    alertDialog(getMainActivity(),
                                                                        0, getResources().getString(
                                                                            R.string.motilal_oswal),
                                                                        UNAUTHORIZE_USER, "OK",
                                                                        false,
                                                                        new MSFDialog.DialogListener() {
                                                                            @Override
                                                                            public void alertDialogAction(
                                                                                MSFDialog.Action action,
                                                                                Object... data) {
                                                                                if (action
                                                                                    == MSFDialog.Action.OK) {
                                                                                    Intent i =
                                                                                        new Intent(
                                                                                            getMainActivity(),
                                                                                            LoginActivity.class);
                                                                                    startActivity(
                                                                                        i);
                                                                                    getActivity().finish();
                                                                                }
                                                                            }
                                                                        });
                                                                    return;
                                                                } else {
                                                                    String error =
                                                                        CRMMOSLCommon.getErrorMsgFromResponse(
                                                                            errorResponse);
                                                                    if (CRMAppUtil.isBlank(error)) {
                                                                        error =
                                                                            SOME_SERVER_ERROR_OCCURRED;
                                                                    }
                                                                    MSFDialog.alertDialog(
                                                                        getMainActivity(), 0,
                                                                        getResources().getString(
                                                                            R.string.motilal_oswal),
                                                                        error, "OK", true, null);
                                                                    return;
                                                                }
                                                            } catch (Exception ex) {
                                                                ex.printStackTrace();
                                                                searchList.showProgress(View.GONE);
                                                                MSFDialog.alertDialog(
                                                                    getMainActivity(), 0,
                                                                    getResources().getString(
                                                                        R.string.motilal_oswal),
                                                                    SOME_SERVER_ERROR_OCCURRED,
                                                                    "OK", true, null);
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            Throwable throwable,
                                                            JSONArray errorResponse) {
                                                            super.onFailure(statusCode, headers,
                                                                throwable, errorResponse);
                                                            searchList.showProgress(View.GONE);
                                                            alertDialog(getMainActivity(), 0,
                                                                getResources().getString(
                                                                    R.string.motilal_oswal),
                                                                SOME_SERVER_ERROR_OCCURRED, "OK",
                                                                true, null);
                                                        }

                                                        @Override
                                                        public void onFailure(int statusCode,
                                                            cz.msebera.android.httpclient.Header[] headers,
                                                            String responseString,
                                                            Throwable throwable) {
                                                            super.onFailure(statusCode, headers,
                                                                responseString, throwable);
                                                            searchList.showProgress(View.GONE);
                                                            alertDialog(getMainActivity(), 0,
                                                                getResources().getString(
                                                                    R.string.motilal_oswal),
                                                                SOME_SERVER_ERROR_OCCURRED, "OK",
                                                                true, null);
                                                        }
                                                    });
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                searchList.showProgress(View.GONE);
                                                alertDialog(getMainActivity(), 0,
                                                    getResources().getString(
                                                        R.string.motilal_oswal),
                                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true,
                                                    null);
                                            }
                                        } else {
                                            searchList.showProgress(View.GONE);
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                INTERNET_CONNECTION, "OK", true, null);
                                        }
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    searchList.showProgress(View.GONE);
                                }
                            }
                        };
                    searchList.setmListner(assingViewListner);
                }
            });
            llInterestedIn.setVisibility(View.GONE);
            GetStage(spStageDialog, stageId, progressLayout);

            spStageDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position,
                    long id) {
                    try {
                        if (tvspStageError != null) {
                            tvspStageError.setVisibility(View.GONE);
                        }
                        llLowMarginAdd.removeAllViews();
                        lowMarginList.clear();
                        newStageCodeStr = ((RowData) spStageDialog.getSelectedItem()).getKey();
                        newStageNameStr = ((RowData) spStageDialog.getSelectedItem()).getValue();
                        if (!newStageCodeStr.equalsIgnoreCase("-1")) {
                            newGetStatus(newStageCodeStr, spStatusDialog, statusId, progressLayout);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

            spStatusDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position,
                    long id) {
                    try {
                        if (tvspStatusError != null) {
                            tvspStatusError.setVisibility(View.GONE);
                        }
                        llLowMarginAdd.removeAllViews();
                        lowMarginList.clear();
                        if (position != 0) {
                            lowMargin(((RowData) spStatusDialog.getSelectedItem()).getKey(),
                                progressLayout, llLowMarginAdd, isDefault);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spStageDialog.getSelectedItemPosition() < 1) {
                        tvspStageError.setVisibility(View.VISIBLE);
                        tvspStageError.setText("Select Stage");
                        //TODO Show Error
                        return;
                    }
                    if (spStatusDialog.getSelectedItemPosition() < 1) {
                        //TODO Show Error
                        tvspStatusError.setVisibility(View.VISIBLE);
                        tvspStatusError.setText("Select Status");
                        return;
                    }
                    Map dataMap = lowMarginSubmit(lowMarginList, isDefault);
                    if (isLowMargin && dataMap.size() == 0) {
                        return;
                    }
                    final String primaryRM =
                        CRMAppUtil.isBlank(leadOwner.trim()) ? advisorCode : leadOwner.trim();

                    if (isPotentialPMSProduct) {
                        MSFDialog.alertDialog(getActivity(), 0,
                            getResources().getString(R.string.motilal_oswal),
                            POTENTIAL_INVEST_IN_PRODUCT, YES, NO, true,
                            new MSFDialog.DialogListener() {
                                @Override
                                public void alertDialogAction(MSFDialog.Action action,
                                    Object... data) {
                                    if (action == MSFDialog.Action.OK) {
                                        updateProduct(interestIn, leadMappingId, primaryRM,
                                            specialistEdit, newStageCodeStr,
                                            ((RowData) spStatusDialog.getSelectedItem()).getKey(),
                                            dataMap,
                                            ((RowData) spStatusDialog.getSelectedItem()).getValue(),
                                            actAssignTo.getText()
                                                .toString(), YES);
                                    } else {
                                        updateProduct(interestIn, leadMappingId, primaryRM,
                                            specialistEdit, newStageCodeStr,
                                            ((RowData) spStatusDialog.getSelectedItem()).getKey(),
                                            dataMap,
                                            ((RowData) spStatusDialog.getSelectedItem()).getValue(),
                                            actAssignTo.getText()
                                                .toString(), NO);
                                    }
                                }
                            });
                    } else {
                        updateProduct(interestIn, leadMappingId, primaryRM, specialistEdit,
                            newStageCodeStr,
                            ((RowData) spStatusDialog.getSelectedItem()).getKey(), dataMap,
                            ((RowData) spStatusDialog.getSelectedItem()).getValue(),
                            actAssignTo.getText()
                                .toString(), "");
                    }
                    dialog.dismiss();
                }
            });

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyboard(leadViewDetailView);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        private void updateProduct(String interestIn, String leadMappingId,
            String primaryRM, String specialistEdit, String newStageCodeStr,
            String statusID, Map dataMap, String statusValue, String actAssignTo,
            String pmsProductStatus) {
            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                showProgress();
                Map inputMap = new HashMap<>();
                inputMap.put(LEAD_ID, etLeadID.getText().toString().trim());
                inputMap.put("InterestedIn", interestIn);
                inputMap.put("LeadProductMappingID", leadMappingId);

                inputMap.put("ProductID", interestIn);

                inputMap.put("PrimaryRM", primaryRM);
                inputMap.put("ProductSpecialist", specialistEdit);
                inputMap.put("AssignedBy", AccountDetails.getEmployeeCode());
                inputMap.put("AssignedByDate", CRMAppUtil.getDBDate());
                inputMap.put("StageID", newStageCodeStr);
                inputMap.put("IsPotentialPMSProduct", pmsProductStatus);
                inputMap.put("StatusID",
                    statusID);
                if (dataMap != null) {
                    inputMap.putAll(dataMap);
                }
                String serviceMethod = "Lead/UpdateLeadProductMapping";
                hideKeyboard(leadViewDetailView);
                try {
                    CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(),
                        inputMap, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    if (statusCode == 200) {
                                        String status = response.getString(STATUS);
                                        if (status.equalsIgnoreCase("true")) {
                                            hideProgress();
                                            if (isLowMargin) {
                                                dashbordFragment();
                                            } else {
                                                alertDialog(getMainActivity(), 0,
                                                    getResources().getString(
                                                        R.string.motilal_oswal),
                                                    "Products Updated Successfully", "Ok",
                                                    true, new MSFDialog.DialogListener() {
                                                        @Override
                                                        public void alertDialogAction(
                                                            MSFDialog.Action action,
                                                            Object... data) {
                                                            if (action
                                                                == MSFDialog.Action.OK) {

                                                                if (isPotentialPMSProduct) {

                                                                    getLeadDetail();
                                                                } else {
                                                                    updateProductData(
                                                                        interestIn,
                                                                        newStageCodeStr,
                                                                        newStageNameStr,
                                                                        statusID,
                                                                        statusValue,
                                                                        specialistEdit,
                                                                        actAssignTo
                                                                    );
                                                                }
                                                            }
                                                        }
                                                    });
                                            }
                                        } else {
                                            hideProgress();
                                            String errorMsg =
                                                CRMMOSLCommon.getErrorMsgFromResponse(
                                                    response);
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(
                                                    R.string.motilal_oswal), errorMsg, "OK",
                                                true, null);
                                        }
                                    } else {
                                        hideProgress();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    hideProgress();
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                                hideProgress();
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                                hideProgress();
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable,
                                    errorResponse);
                                try {
                                    hideProgress();
                                    if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(
                                                R.string.motilal_oswal), UNAUTHORIZE_USER,
                                            "OK", false, new MSFDialog.DialogListener() {
                                                @Override
                                                public void alertDialogAction(
                                                    MSFDialog.Action action,
                                                    Object... data) {
                                                    if (action == MSFDialog.Action.OK) {
                                                        Intent i =
                                                            new Intent(getMainActivity(),
                                                                LoginActivity.class);
                                                        startActivity(i);
                                                        getActivity().finish();
                                                    }
                                                }
                                            });
                                        return;
                                    } else {
                                        String error =
                                            CRMMOSLCommon.getErrorMsgFromResponse(
                                                errorResponse);
                                        if (CRMAppUtil.isBlank(error)) {
                                            error = SOME_SERVER_ERROR_OCCURRED;
                                        }
                                        MSFDialog.alertDialog(getMainActivity(), 0,
                                            getResources().getString(
                                                R.string.motilal_oswal), error, "OK", true,
                                            null);
                                        return;
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                }
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable,
                                    errorResponse);
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                cz.msebera.android.httpclient.Header[] headers,
                                String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString,
                                    throwable);
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        });
                } catch (Exception e) {
                    e.printStackTrace();
                    hideProgress();
                    alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                }
            } else {
                hideProgress();
                alertDialog(getMainActivity(), 0,
                    getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION,
                    "OK", true, null);
            }
        }

        public class Holder {
            TextView tvProduct, tvProductSpecialist, tvPrimaryRM, tvStage, tvProductID, tvStatus,
                tvAddInvestmentProduct;
            LinearLayout llDeleteLeadDetail, llAddProduct, llCrossSellingProducts,
                llChildCrossSellingProduct;
        }
    }
}
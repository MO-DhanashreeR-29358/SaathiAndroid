package com.mosl.mobile.ekyc.personaldetail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.CustomViewPager;
import com.mosl.mobile.ekyc.common.EkycAppUtil;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.common.SearchableSpinner;
import com.mosl.mobile.ekyc.common.ValidationRule;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.model.FamilyDeclarationModel;
import com.mosl.mobile.ekyc.model.RowData;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.CustomDatePickerDialog;
import com.mosl.mobile.ekyc.ui.CustomDialog;
import com.mosl.mobile.ekyc.ui.CustomDialogFamilyRelationship;
import com.mosl.mobile.ekyc.ui.DatePickerListener;
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
import org.json.JSONObject;

import static com.mosl.mobile.ekyc.common.EkycAppUtil.getAdultDate;
import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.disableField;
import static com.mosl.mobile.ekyc.common.MOSLCommon.enableField;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isDateIWthinYear;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isIpvORDocumentStageDone;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isNomineeTabApplicable;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isThisSecondProductFlow;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isValidEmail;
import static com.mosl.mobile.ekyc.common.MOSLCommon.loadCountryArray;
import static com.mosl.mobile.ekyc.common.MOSLCommon.removeKeyboard;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.getAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getBankTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getDematTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getEsignTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAllSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCommoditySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCurrencySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsDerivativeSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsEquitySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getPersonalTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getUploadTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.isAllOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommodityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isCommoditySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isCurrencyOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isDerivativeOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isDerivativeSegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityFundTransfered;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.isEquitySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isMfIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.isMfSegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.isOffline;
import static com.mosl.mobile.ekyc.model.CommonModel.isOfflineDocumentUploadedDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setAadharNo;
import static com.mosl.mobile.ekyc.model.CommonModel.setClientName;
import static com.mosl.mobile.ekyc.model.CommonModel.setCurrencySegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.setDerivativeSegmentExist;
import static com.mosl.mobile.ekyc.model.CommonModel.setEqSchemeId;
import static com.mosl.mobile.ekyc.model.CommonModel.setGotoFundTransfer;
import static com.mosl.mobile.ekyc.model.CommonModel.setGotoReviewEdit;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAllOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsAllSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommodityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCommoditySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCurrencyOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsCurrencySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsDerivativeOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsDerivativeSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquityOriginal;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquitySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsPersonalDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsPersonalDetailUpdated;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsSegmentSelected;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsWorkDetailDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANName;
import static com.mosl.mobile.ekyc.model.CommonModel.setPANno;
import static com.mosl.mobile.ekyc.model.RowData.getSpinnerRowDataById;
import static com.mosl.mobile.ekyc.model.RowData.getSpinnerRowDataByValue;
import static com.mosl.mobile.ekyc.ui.EkycMainActivity.customProgress;


/**
 * Created by Feat on 6/13/2016.
 */
public class PersonalDetailFragment extends Fragment implements TextWatcher, MOSLConstants, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private int hideProgressCounter = 0, hideUpdateProgressCounter = 0;
    private Context mContext;
    private View personalDetailView;
    private EditText etMobile, etEmail, etFirstName, etMiddleName, etLastName, etDOB, etBirthPlace, etPAN, etAadhar, etNameOnPAN, etNWDate, etAltTelNo, etAltEmailID,
            etBldgNameCorres, etFlatNoCorres, etAreaCorres, /*etAddProofCorres,*/
            etGST, etPincodeCorres, etFatherSpouseFirstName, etFatherSpouseMidName, etMotherFirstName, etMotherMiddleName, etMotherLastName, etMaidenFirstName,
            etMaidenMiddleName, etMaidenLastName, etFatherSpouseLastName, etBldgNamePerm, etFlatNamePerm, etAreaPerm, /*etAddProofPerm,*/
            etNetWorth, etBrokerName, etClientCode, etPincodePerm, etResidentStatus, etSubStatus, etFbId, etTwitterId, etLinkedinID, etSTDNo, etTelNo, etCommOther,
            etSebiAuthority, etFmcSebiAuthority, etElectronicNote;
    private RelativeLayout llPersonalDetail, rlFamilyDeclaration;
    private LinearLayout llBrokerInfo, llSebiAuthority, llFmcSebiAuthority, llEqSpinners, llCommSpinners, llDealingWithEquity, llDealWithDeri, llDealWithComm,
            llSEBIAuthorityMain, llFmcSEBIAuthorityMain, llEqExperience, llCommExperience, llStockPortfolio, llCorresAddress, llPermAddress, llCategorySelection,
            llSubCategoryCommercial, llSubCategoryNonCommercial;
    private CheckBox cbSameAsCorrespondence, cbEqIntraday, cbEqPositional, cbEqInvestment, cbDevTrading, cbDevHedging, cbCommInvestment, cbCommTrading, cbCommHedging,
            cbCommOther, cbFamilyDeclaration;
    private Button btnNext;
    private TextView tvMobile, tvEmail, tvFamilyDeclaration;
    private ImageView ivViewFamilyDeclaration;
    //    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Boolean DOBClicked = false, NWClicked = false, cityFoundFlag = false, personalFlag;
    private int eqBranchCodeCount = 0, commBranchCodeCount = 0;
    private boolean mobile = true, email = true, loadedAPIOnStart;
    //   LinearLayout EkycMainActivity.customProgress;
    private Spinner spTitle, spFatherSpouseTitle, spOccupation, spIncome, spStockPortfolio, spEqDerivativeExp, spMotherTitle, spMaidenTitle,
            spEqExperience, spCommOtherExp, spCommExperience, spFatherSpouseSel, spOtherResidence, spQualification, /*spNationality,*/
            spBirthCountry, spEqSubBroker, spEqTrader, spEqArea, spEqRegion, spEqBranchCode,
            spCommSubBroker, spCommTrader, spCommArea, spCommRegion, spCommBranchCode, spCorresAddProof, spPermAddProof, spCorresCountry, spPermCountry;
    private SearchableSpinner spCorresState, spCorresCity, spPermState, spPermCity, spCountryCitizenship, spCountryBirth,
            spCountryTax;
    private ScrollView scrollview;
    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> arrayList;
    private ArrayList<RowData> arrState, arrCorresCity, arrPermCity, arrEqSubBroker, arrCommSubBroker, arrEqTrader, arrCommTrader, arrEqRegion, arrCommRegion,
            arrEqArea, arrCommArea, arrEqBranch, arrCommBranch, arrCorresAddressProof, arrPermAddressProof;
    private ArrayAdapter<RowData> arrAdapState, arrAdapCorresCity, arrAdapPermCity, arrAdapEqSubBroker, arrAdapCommSubBroker, arrAdapEqTrader, arrAdapCommTrader,
            arrAdapEqRegion, arrAdapCommRegion, arrAdapEqArea, arrAdapCommArea, arrAdapEqBranch, arrAdapCommBranch, arrAdapCorresAddressProof, arrAdapPermAddressProof;
    private ArrayAdapter<String> arrAdapOcc, arrAdapIncome, arrAdapStockPortfolio, arrAdapNetWorth, arrAdapMaritalStatus, arrAdapEqExperience,
            arrAdapDerivativeExp, arrAdapCommExperience, arrAdapCommOtherExp, arrAdapTitle, arrAdapFatherSpouseSel, arrAdapFemaleTitle, arrAdapMaidenTitle,
            arrAdapResForOtherCountryTax, arrAdapQualification, arrAdapCountryBirth, arrAdapNationality, arrAdapCountry, arrAdapCountryFatca;
    private String corresStateCodeStr, corresCityCodeStr, permStateCodeStr, permCityCodeStr, eqSubBrokerCodeStr, commSubBrokerCodeStr,
            eqTraderCodeStr, commTraderCodeStr, eqAreaCodeStr, commAreaCodeStr, eqRegionCodeStr, commRegionCodeStr,
            eqBranchCodeStr, commBranchCodeStr;
    private String corresStateNameStr, corresCityNameStr, permStateNameStr, permCityNameStr, eqSubBrokerNameStr, commSubBrokerNameStr,
            eqTraderNameStr, commTraderNameStr, eqAreaNameStr, commAreaNameStr, eqRegionNameStr, commRegionNameStr,
            eqBranchNameStr, commBranchNameStr;
    private ImageView cbEquity, cbCommodity, cbDerivative, cbAll, ivNote, cbCurrency, cbMF;
    private CustomViewPager viewPager;
    private TabPageIndicator tabPageIndicator;
    private TextInputLayout input_layout_residentStatus, input_layout_subStatus, input_layout_lastname, input_layout_firstname, input_layout_dob, input_layout_birthPlace,
            input_layout_fatherSpouseFirstName, input_layout_fatherSpouseMidName, input_layout_fatherSpouseLastName, input_layout_PAN, input_layout_aadhar, input_layout_nameOnPAN,
            input_layout_bldgNameCorres, input_layout_flatNoCorres, input_layout_areaCorres,/*input_layout_addProofCorres,*/
            input_layout_pincodeCorres, input_layout_bldgNamePerm, input_layout_flatNamePerm, input_layout_areaPerm,/*input_layout_addProofPerm,*/
            input_layout_pincodePerm, input_layout_MotherFirstname, input_layout_MotherMiddlename, input_layout_MotherLastname,
            input_layout_mobile, input_layout_email, input_layout_std, input_layout_telNo, input_layout_fbId, input_layout_twitterId, input_layout_MaidenFirstname, input_layout_MaidenMiddlename, input_layout_MaidenLastname,
            input_layout_linkedinID, input_layout_netWorth, input_layout_NWDate, input_layout_commOther, input_layout_brokerName, input_layout_clientCode, input_layout_electronicNote, input_layout_sebiAuthority, input_layout_fmcSebiAuthority,
            inputLayoutAltTelNo, inputLayoutAltEmailID, input_layout_GST;
    private TextView etGenderError, etmaritalError, etPurposeForCommodityError, etPurposeForDerivativeError, etPurposeForEquityError, etCorresCityError, etPermStateError, etPermCityError,
            etCorresStateError, etEqBranchCodeError, etEqSubrokerCodeError, etEqTraderCodeError, etEqAreaError, etEqRegionError, etCommBranchCodeError, etCommSubrokerCodeError,
            etCommTraderCodeError, etCommAreaError, etCommRegionError, etFatherSpouseSelError, /*etOtherResidenceError,*/
            etOccupationError, etQualificationError, etIncomeError, etStockPortfolioError, etEqExperienceError, etEqDerivativeExpError, etCommExperienceError, etCommOtherExpError,
            etCorresAddProofError, etPermAddProofError, etMaidenTitleError, etMotherTitleError, etMFPEPStatusError, etPincodePermError, etPincodeCorresError, etLinkedinIDError,
            etFbIdError, etMobileError, etEmailError, etNetWorthError, etFatherSpouseFirstNameError, etFatherSpouseMidNameError,
            etMotherFirstNameError, etMotherMiddleNameError, etMotherLastNameError, etMaidenFirstNameError, etMaidenMiddleNameError, etMaidenLastNameError, etFatherSpouseLastNameError,
            etCountryBirthError, etSTDTelError, etTAXIdentificationNumError, etCountryTaxError, etPlaceBirthError, etFatherSpouseTitleError, etCountryCitizenshipError,
            etClientCodeError, etBrokerNameError, etAltMobileError, etAltEmailError, etCorresCountryError, etPermCountryError, etBirthPlaceError, etLastNameError, etMiddleNameError,
            etFirstNameError, etTitleError, etNWDateError, etNWDateRequestFocus, etDOBRequestFocus, etCategorySelectionError;

    private EditText etCorresState, etCorresCity, etPermState, etPermCity, etMFPEPRemark;
    private String corresAddProof, permanentAddProof, politicallyExposedPerson, relatedPoliticallyExposedPerson, brokerName, comStocExp, comOtherExp, comPastAction, comInvestment, comTrading, comHedging, comOther, eqStockExp, derivativeExp, eqPastAction,
            eqIntraDay, eqPositional, eqInvestment, devTrading, devHedging, title, relation, fhTitle, motherTitle, maidenTitle, occupationDetails, educationQualification, annualIncome, stockPortfolio, pepRemark;
    //for Spinner value for franchise
    private String branch, subBroker, trader, branchRMEmpcode;
    private TextView tvPrimayDetails, tvFatcaDeclaration, tvBranchCodeTitle, tvSubrokerCodeTitle, tvTraderCodeTitle, tvAreaTitle, tvRegionTitle, tvPersonalDetailNote, tvRelationshipDetails;
    private Dialog dialogDocList;
    private boolean isFatcaCountryCitizenBlank, isFatcaCountryBirthBlank, isFatcaTAXBlank, isFatcaPOABlank, isFatcaAddOutsideIndiaBlank;
    private Dialog dialog;
    private boolean visibilityFlagFD = false, aadhaarByPass = false;
    /**
     * FATCA Detail
     */

    private CheckBox cbDeclaration, cbCommercial, cbCommercialBullion, cbCommercialBaseMetals, cbCommercialEnergy, cbCommercialAgriCommodities, cbNonCommercial, cbNonCommercialBullion,
            cbNonCommercialBaseMetals, cbNonCommercialEnergy, cbNonCommercialAgriCommodities;
    private ImageView ivCategoryInfo;
    private EditText /*etCountryCitizenship, etCountryBirth, , etCountryTax,*/ etPlaceBirth, etTAXIdentificationNum;
    private TextInputLayout /*input_layout_CountryCitizenship, input_layout_CountryBirth, , input_layout_CountryTax,*/input_layout_PlaceBirth, input_layout_TAXIdentificationNum, inputLayoutMFPEPRemark;
    private LinearLayout llCountryCitizenship, llCountryBirth, llCountryTax, llDeclaration;

    private RelativeLayout rlSegmentTitle;
    private LinearLayout llSegmentSelection, llResidentInOtherCountry, llPEP, llRelatedPEP, llRegisterToOtherBroker, llMFPEPStatus, llMFPEPRemark, llMaidenName;
    private Spinner spMFPEPStatus;
    private ToggleButton togglePoliticaly, toggleRelatedPolitical, toggleRegWithBroker, toggleSEBIAuthority, toggleFmcSebiAuthority, toggleAddressOutsideIndia, togglePoaAddress, toggleCountryTax, toggleCountryBirth, toggleCountryCitizen;
    private RadioButton rbMale, rbFemale, rbUnMarried, rbMarried;
    private ArrayList countryList = new ArrayList();
    private ArrayList countryListWithoutIndia = new ArrayList();
    private LinearLayout llNameDetail, llFatherSpouseDetail, llMotherDetail, llWorkDetail, llContactDetail, llFatcaDetail;
    private List<String> arrInterested;
    private ArrayAdapter<RowData> arrAdapInterested;
    private String strNetWorth;
    private ArrayAdapter<String> spOccArrayAdapter;
    private boolean isDisableEnableFieldCalled = false;
    private int updateFamilyDetailsCount = 1;
    private boolean familyDeclarationChangedFlag = false;
    private CustomDialogFamilyRelationship customdailogFamilyRelation;
    private int motherCount = 0, fatherCount = 0, selfCount = 0, spouseCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            personalDetailView = inflater.inflate(R.layout.frag_per_detail, container, false);
            setUpView(personalDetailView);
            spCorresCity.setAdapter(arrAdapCorresCity);
            spPermCity.setAdapter(arrAdapPermCity);
            setUserVisibleHint(true);
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
        return personalDetailView;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setViewPager(CustomViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setTabPageIndicator(TabPageIndicator tabPageIndicator) {
        this.tabPageIndicator = tabPageIndicator;
    }

    //State City spinner listener
    private AdapterView.OnItemSelectedListener corresStateListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                corresStateCodeStr = ((RowData) spCorresState.getSelectedItem()).getKey();
                corresStateNameStr = ((RowData) spCorresState.getSelectedItem()).getValue();
                etCorresStateError.setVisibility(View.GONE);
                if (cityFoundFlag) {
                    getCorresCity(corresStateCodeStr, null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener permStateListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                permStateCodeStr = ((RowData) spPermState.getSelectedItem()).getKey();
                permStateNameStr = ((RowData) spPermState.getSelectedItem()).getValue();
                etPermStateError.setVisibility(View.GONE);
                if (cityFoundFlag) {
                    getPermanentCity(permStateCodeStr, null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener corresCityListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                corresCityCodeStr = ((RowData) spCorresCity.getSelectedItem()).getKey();
                corresCityNameStr = ((RowData) spCorresCity.getSelectedItem()).getValue();
                etCorresCityError.setVisibility(View.GONE);
                if (cbSameAsCorrespondence.isChecked()) {
                    int corrSelectedStatePos = ((RowData) spCorresState.getSelectedItem()).getPosition();
                    spPermState.setSelection(corrSelectedStatePos);
                    getPermanentCity(corresStateCodeStr, null);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener permCityListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                permCityCodeStr = ((RowData) spPermCity.getSelectedItem()).getKey();
                permCityNameStr = ((RowData) spPermCity.getSelectedItem()).getValue();
                etPermCityError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener eqSubBrokerCodeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                eqSubBrokerCodeStr = ((RowData) spEqSubBroker.getSelectedItem()).getKey();
                eqSubBrokerNameStr = ((RowData) spEqSubBroker.getSelectedItem()).getValue();
                etEqSubrokerCodeError.setVisibility(View.GONE);
//                getFrachiseeDetail(EQUITY);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener commSubBrokerCodeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                commSubBrokerCodeStr = ((RowData) spCommSubBroker.getSelectedItem()).getKey();
                commSubBrokerNameStr = ((RowData) spCommSubBroker.getSelectedItem()).getValue();
                etCommSubrokerCodeError.setVisibility(View.GONE);
//                getFrachiseeDetail(COMMODITY);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener eqTraderCodeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                eqTraderCodeStr = ((RowData) spEqTrader.getSelectedItem()).getKey();
                eqTraderNameStr = ((RowData) spEqTrader.getSelectedItem()).getValue();
                etEqTraderCodeError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener commTraderCodeListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                commTraderCodeStr = ((RowData) spCommTrader.getSelectedItem()).getKey();
                commTraderNameStr = ((RowData) spCommTrader.getSelectedItem()).getValue();
                etCommTraderCodeError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener eqAreaListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                eqAreaCodeStr = ((RowData) spEqArea.getSelectedItem()).getKey();
                eqAreaNameStr = ((RowData) spEqArea.getSelectedItem()).getValue();
                etEqAreaError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener commAreaListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                commAreaCodeStr = ((RowData) spCommArea.getSelectedItem()).getKey();
                commAreaNameStr = ((RowData) spCommArea.getSelectedItem()).getValue();
                etCommAreaError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener eqRegionListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                eqRegionCodeStr = ((RowData) spEqRegion.getSelectedItem()).getKey();
                eqRegionNameStr = ((RowData) spEqRegion.getSelectedItem()).getValue();
                etEqRegionError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener commRegionListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                commRegionCodeStr = ((RowData) spCommRegion.getSelectedItem()).getKey();
                commRegionNameStr = ((RowData) spCommRegion.getSelectedItem()).getValue();
                etCommRegionError.setVisibility(View.GONE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final AdapterView.OnItemSelectedListener eqBranchListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                eqBranchCodeStr = ((RowData) spEqBranchCode.getSelectedItem()).getKey();
                eqBranchNameStr = ((RowData) spEqBranchCode.getSelectedItem()).getValue();
                etEqBranchCodeError.setVisibility(View.GONE);
                if (branchRMEmpcode.equalsIgnoreCase("ranchi")) {
                    eqBranchCodeCount++;
                    if (eqBranchCodeCount > 2) {
                        getFrachiseeDetail(eqBranchNameStr, EQUITY);
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

    private final AdapterView.OnItemSelectedListener commBranchListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                commBranchCodeStr = ((RowData) spCommBranchCode.getSelectedItem()).getKey();
                commBranchNameStr = ((RowData) spCommBranchCode.getSelectedItem()).getValue();
                etCommBranchCodeError.setVisibility(View.GONE);
                if (branchRMEmpcode.equalsIgnoreCase("ranchi")) {
                    commBranchCodeCount++;
                    if (commBranchCodeCount > 2) {
                        getFrachiseeDetail(commBranchNameStr, COMMODITY);
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

    private final AdapterView.OnItemSelectedListener commonListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            Spinner spinner = (Spinner) parent;
            if (spinner.getId() == R.id.spTitle) {
                etTitleError.setVisibility(View.GONE);
                //do this
            } else if (spinner.getId() == R.id.spFatherSpouseTitle) {
                etFatherSpouseTitleError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spMotherTitle) {
                etMotherTitleError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spMaidenTitle) {
                etMaidenTitleError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spFatherSpouseSel) {
                etFatherSpouseSelError.setVisibility(View.GONE);
            }/* else if (spinner.getId() == R.id.spOtherResidence) {
                etOtherResidenceError.setVisibility(View.GONE);
            }*/ else if (spinner.getId() == R.id.spOccupation) {
                etOccupationError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spQualification) {
                etQualificationError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spIncome) {
                etIncomeError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spStockPortfolio) {
                etStockPortfolioError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spEqExperience) {
                etEqExperienceError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spEqDerivativeExp) {
                etEqDerivativeExpError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spCommExperience) {
                etCommExperienceError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spCommOtherExp) {
                etCommOtherExpError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spMFPEPStatus) {
                etPermAddProofError.setVisibility(View.GONE);

                if (!spMFPEPStatus.getSelectedItem().toString().equals(getResources().getString(R.string.not_applicable))) {
                    llMFPEPRemark.setVisibility(View.VISIBLE);
                    etMFPEPStatusError.setVisibility(View.GONE);
                    etMFPEPRemark.setVisibility(View.VISIBLE);
                    etMFPEPRemark.setText(pepRemark);
//                    etMFPEPRemark.setText("");

                } else {
                    etMFPEPRemark.setVisibility(View.GONE);
                    llMFPEPRemark.setVisibility(View.GONE);
                }
            } else if (spinner.getId() == R.id.spCorresAddProof) {
                etCorresAddProofError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spPermAddProof) {
                etPermAddProofError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spCountryCitizenship) {
                etCountryCitizenshipError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spCountryBirth) {
                etCountryBirthError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spCountryTax) {
                etCountryTaxError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spCorresCountry) {
                etCorresCountryError.setVisibility(View.GONE);
            } else if (spinner.getId() == R.id.spPermCountry) {
                etPermCountryError.setVisibility(View.GONE);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    public void loadPersonalDetails() {
        if (viewPager == null || viewPager.getCurrentItem() != getPersonalTab()) {
            return;
        }
        getState();

    }

    public void setUpView(View personalDetailView) {
        scrollview = (ScrollView) personalDetailView.findViewById(R.id.scrollview);
        scrollview.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollview.setFocusable(true);
        scrollview.setFocusableInTouchMode(true);
        scrollview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        viewPager = (CustomViewPager) getActivity().findViewById(R.id.vpApply);
        tvPrimayDetails = (TextView) personalDetailView.findViewById(R.id.tvPrimayDetails);
        tvFatcaDeclaration = (TextView) personalDetailView.findViewById(R.id.tvFatcaDeclaration);
        tvBranchCodeTitle = (TextView) personalDetailView.findViewById(R.id.tvBranchCodeTitle);
        tvSubrokerCodeTitle = (TextView) personalDetailView.findViewById(R.id.tvSubrokerCodeTitle);
        tvTraderCodeTitle = (TextView) personalDetailView.findViewById(R.id.tvTraderCodeTitle);
        tvAreaTitle = (TextView) personalDetailView.findViewById(R.id.tvAreaTitle);
        tvRegionTitle = (TextView) personalDetailView.findViewById(R.id.tvRegionTitle);
        tvPersonalDetailNote = (TextView) personalDetailView.findViewById(R.id.tvPersonalDetailNote);
        tvRelationshipDetails = (TextView) personalDetailView.findViewById(R.id.tvRelationshipDetails);

        llPersonalDetail = (RelativeLayout) personalDetailView.findViewById(R.id.llPersonalDetail);
        llBrokerInfo = (LinearLayout) personalDetailView.findViewById(R.id.llBrokerInfo);
        llSebiAuthority = (LinearLayout) personalDetailView.findViewById(R.id.llSebiAuthority);
        llFmcSebiAuthority = (LinearLayout) personalDetailView.findViewById(R.id.llFmcSebiAuthority);
        llEqSpinners = (LinearLayout) personalDetailView.findViewById(R.id.llEqSpinners);
        llCommSpinners = (LinearLayout) personalDetailView.findViewById(R.id.llCommSpinners);

        llDealingWithEquity = (LinearLayout) personalDetailView.findViewById(R.id.llDealingWithEquity);
        llDealWithDeri = (LinearLayout) personalDetailView.findViewById(R.id.llDealWithDeri);
        llDealWithComm = (LinearLayout) personalDetailView.findViewById(R.id.llDealWithComm);
        llSEBIAuthorityMain = (LinearLayout) personalDetailView.findViewById(R.id.llSEBIAuthorityMain);
        llFmcSEBIAuthorityMain = (LinearLayout) personalDetailView.findViewById(R.id.llFmcSEBIAuthorityMain);
        llEqExperience = (LinearLayout) personalDetailView.findViewById(R.id.llEqExperience);
        llCommExperience = (LinearLayout) personalDetailView.findViewById(R.id.llCommExperience);
        llStockPortfolio = (LinearLayout) personalDetailView.findViewById(R.id.llStockPortfolio);
        llCorresAddress = (LinearLayout) personalDetailView.findViewById(R.id.llCorresAddress);
        llPermAddress = (LinearLayout) personalDetailView.findViewById(R.id.llPermAddress);
        rlFamilyDeclaration = (RelativeLayout) personalDetailView.findViewById(R.id.rlFamilyDeclaration);
        tvFamilyDeclaration = (TextView) personalDetailView.findViewById(R.id.tvFamilyDeclaration);
        ivViewFamilyDeclaration = (ImageView) personalDetailView.findViewById(R.id.ivViewFamilyDeclaration);
        cbFamilyDeclaration = (CheckBox) personalDetailView.findViewById(R.id.cbFamilyDeclaration);

        llPEP = (LinearLayout) personalDetailView.findViewById(R.id.llPEP);
        llRelatedPEP = (LinearLayout) personalDetailView.findViewById(R.id.llRelatedPEP);
        llRegisterToOtherBroker = (LinearLayout) personalDetailView.findViewById(R.id.llRegisterToOtherBroker);
        llMFPEPStatus = (LinearLayout) personalDetailView.findViewById(R.id.llMFPEPStatus);
        llMFPEPRemark = (LinearLayout) personalDetailView.findViewById(R.id.llMFPEPRemark);
        llMaidenName = (LinearLayout) personalDetailView.findViewById(R.id.llMaidenName);

        llNameDetail = (LinearLayout) personalDetailView.findViewById(R.id.llNameDetail);
        llFatherSpouseDetail = (LinearLayout) personalDetailView.findViewById(R.id.llFatherSpouseDetail);
        llMotherDetail = (LinearLayout) personalDetailView.findViewById(R.id.llMotherDetail);
        llWorkDetail = (LinearLayout) personalDetailView.findViewById(R.id.llWorkDetail);
        llContactDetail = (LinearLayout) personalDetailView.findViewById(R.id.llContactDetail);
        llFatcaDetail = (LinearLayout) personalDetailView.findViewById(R.id.llFatcaDetail);

        spMFPEPStatus = (Spinner) personalDetailView.findViewById(R.id.spMFPEPStatus);

        input_layout_lastname = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_lastname);
        input_layout_firstname = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_firstname);
        input_layout_dob = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_dob);
        input_layout_birthPlace = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_birthPlace);
        input_layout_fatherSpouseFirstName = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_fatherSpouseFirstName);
        input_layout_fatherSpouseMidName = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_fatherSpouseMidName);
        input_layout_fatherSpouseLastName = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_fatherSpouseLastName);
        input_layout_MotherFirstname = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_MotherFirstname);
        input_layout_MotherMiddlename = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_MotherMiddlename);
        input_layout_MotherLastname = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_MotherLastname);
        input_layout_MaidenFirstname = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_MaidenFirstname);
        input_layout_MaidenMiddlename = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_MaidenMiddlename);
        input_layout_MaidenLastname = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_MaidenLastname);
        input_layout_PAN = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_PAN);
        input_layout_aadhar = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_aadhar);
        input_layout_nameOnPAN = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_nameOnPAN);
        input_layout_bldgNameCorres = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_bldgNameCorres);
        input_layout_flatNoCorres = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_flatNoCorres);
//        input_layout_addProofCorres=(TextInputLayout)personalDetailView.findViewById(R.id.input_layout_addProofCorres);
        input_layout_pincodeCorres = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_pincodeCorres);
        input_layout_bldgNamePerm = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_bldgNamePerm);
        input_layout_flatNamePerm = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_flatNamePerm);
        input_layout_areaPerm = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_areaPerm);
//        input_layout_addProofPerm=(TextInputLayout)personalDetailView.findViewById(R.id.input_layout_addProofPerm);
        input_layout_pincodePerm = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_pincodePerm);
        input_layout_mobile = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_mobile);
        input_layout_email = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_email);
        input_layout_std = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_std);
        input_layout_telNo = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_telNo);
        input_layout_fbId = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_fbId);
        input_layout_twitterId = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_twitterId);
        input_layout_linkedinID = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_linkedinID);
        input_layout_netWorth = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_netWorth);
        input_layout_NWDate = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_NWDate);
        input_layout_commOther = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_commOther);
        input_layout_brokerName = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_brokerName);
        input_layout_clientCode = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_clientCode);
        input_layout_electronicNote = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_electronicNote);
        input_layout_sebiAuthority = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_sebiAuthority);
        input_layout_fmcSebiAuthority = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_fmcSebiAuthority);
        input_layout_residentStatus = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_residentStatus);
        input_layout_subStatus = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_subStatus);
        input_layout_areaCorres = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_areaCorres);
        inputLayoutAltTelNo = (TextInputLayout) personalDetailView.findViewById(R.id.inputLayoutAltTelNo);
        inputLayoutAltEmailID = (TextInputLayout) personalDetailView.findViewById(R.id.inputLayoutAltEmailID);
        input_layout_GST = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_GST);

        etResidentStatus = (EditText) personalDetailView.findViewById(R.id.etResidentStatus);
        etSubStatus = (EditText) personalDetailView.findViewById(R.id.etSubStatus);
        etMobile = (EditText) personalDetailView.findViewById(R.id.etMobile);
        etEmail = (EditText) personalDetailView.findViewById(R.id.etEmail);
        etFbId = (EditText) personalDetailView.findViewById(R.id.etFbId);
        etTwitterId = (EditText) personalDetailView.findViewById(R.id.etTwitterId);
        etLinkedinID = (EditText) personalDetailView.findViewById(R.id.etLinkedinID);
        etLinkedinIDError = (TextView) personalDetailView.findViewById(R.id.etLinkedinIDError);
        etFbIdError = (TextView) personalDetailView.findViewById(R.id.etFbIdError);
        etSTDTelError = (TextView) personalDetailView.findViewById(R.id.etSTDTelError);
        etMobileError = (TextView) personalDetailView.findViewById(R.id.etMobileError);
        etAltMobileError = (TextView) personalDetailView.findViewById(R.id.etAltMobileError);
        etEmailError = (TextView) personalDetailView.findViewById(R.id.etEmailError);
        etAltEmailError = (TextView) personalDetailView.findViewById(R.id.etAltEmailError);
        etCorresCountryError = (TextView) personalDetailView.findViewById(R.id.etCorresCountryError);
        etPermCountryError = (TextView) personalDetailView.findViewById(R.id.etPermCountryError);
        etNWDateError = (TextView) personalDetailView.findViewById(R.id.etNWDateError);
        etNWDateRequestFocus = (TextView) personalDetailView.findViewById(R.id.etNWDateRequestFocus);
        etDOBRequestFocus = (TextView) personalDetailView.findViewById(R.id.etDOBRequestFocus);

        etSTDNo = (EditText) personalDetailView.findViewById(R.id.etSTDNo);
        etTelNo = (EditText) personalDetailView.findViewById(R.id.etTelNo);
        etFirstName = (EditText) personalDetailView.findViewById(R.id.etFirstName);
        etMiddleName = (EditText) personalDetailView.findViewById(R.id.etMiddleName);
        etLastName = (EditText) personalDetailView.findViewById(R.id.etLastName);
        etDOB = (EditText) personalDetailView.findViewById(R.id.etDOB);
        etBirthPlace = (EditText) personalDetailView.findViewById(R.id.etBirthPlace);
        etFatherSpouseFirstName = (EditText) personalDetailView.findViewById(R.id.etFatherSpouseFirstName);
        etFatherSpouseMidName = (EditText) personalDetailView.findViewById(R.id.etFatherSpouseMidName);
        etFatherSpouseLastName = (EditText) personalDetailView.findViewById(R.id.etFatherSpouseLastName);
        etMotherFirstName = (EditText) personalDetailView.findViewById(R.id.etMotherFirstName);
        etMotherMiddleName = (EditText) personalDetailView.findViewById(R.id.etMotherMiddleName);
        etMotherLastName = (EditText) personalDetailView.findViewById(R.id.etMotherLastName);
        etMaidenFirstName = (EditText) personalDetailView.findViewById(R.id.etMaidenFirstName);
        etMaidenMiddleName = (EditText) personalDetailView.findViewById(R.id.etMaidenMiddleName);
        etMaidenLastName = (EditText) personalDetailView.findViewById(R.id.etMaidenLastName);
        etPAN = (EditText) personalDetailView.findViewById(R.id.etPAN);
        etAadhar = (EditText) personalDetailView.findViewById(R.id.etAadhar);
        etNameOnPAN = (EditText) personalDetailView.findViewById(R.id.etNameOnPAN);
        etGST = (EditText) personalDetailView.findViewById(R.id.etGST);
        etNetWorth = (EditText) personalDetailView.findViewById(R.id.etNetWorth);
        etNWDate = (EditText) personalDetailView.findViewById(R.id.etNWDate);
        etBldgNameCorres = (EditText) personalDetailView.findViewById(R.id.etBldgNameCorres);
        etFlatNoCorres = (EditText) personalDetailView.findViewById(R.id.etFlatNoCorres);
        etAreaCorres = (EditText) personalDetailView.findViewById(R.id.etAreaCorres);
        etCorresState = (EditText) personalDetailView.findViewById(R.id.etCorresState);
        etCorresCity = (EditText) personalDetailView.findViewById(R.id.etCorresCity);
        etPermState = (EditText) personalDetailView.findViewById(R.id.etPermState);
        etPermCity = (EditText) personalDetailView.findViewById(R.id.etPermCity);
        etAltTelNo = (EditText) personalDetailView.findViewById(R.id.etAltTelNo);
        etAltEmailID = (EditText) personalDetailView.findViewById(R.id.etAltEmailID);
//        etAddProofCorres = (EditText) personalDetailView.findViewById(R.id.etAddProofCorres);
        etPincodeCorres = (EditText) personalDetailView.findViewById(R.id.etPincodeCorres);
        etPincodeCorresError = (TextView) personalDetailView.findViewById(R.id.etPincodeCorresError);
        etBldgNamePerm = (EditText) personalDetailView.findViewById(R.id.etBldgNamePerm);
        etFlatNamePerm = (EditText) personalDetailView.findViewById(R.id.etFlatNamePerm);
        etAreaPerm = (EditText) personalDetailView.findViewById(R.id.etAreaPerm);
//        etAddProofPerm = (EditText) personalDetailView.findViewById(R.id.etAddProofPerm);
        etPincodePerm = (EditText) personalDetailView.findViewById(R.id.etPincodePerm);
        etPincodePermError = (TextView) personalDetailView.findViewById(R.id.etPincodePermError);
        etNetWorthError = (TextView) personalDetailView.findViewById(R.id.etNetWorthError);
        etCommOther = (EditText) personalDetailView.findViewById(R.id.etCommOther);
        etSebiAuthority = (EditText) personalDetailView.findViewById(R.id.etSebiAuthority);
        etFmcSebiAuthority = (EditText) personalDetailView.findViewById(R.id.etFmcSebiAuthority);
        etBrokerName = (EditText) personalDetailView.findViewById(R.id.etBrokerName);
        etClientCode = (EditText) personalDetailView.findViewById(R.id.etClientCode);
        etElectronicNote = (EditText) personalDetailView.findViewById(R.id.etElectronicNote);

        cbEquity = (ImageView) personalDetailView.findViewById(R.id.cbEquity);
        cbCommodity = (ImageView) personalDetailView.findViewById(R.id.cbCommodity);
        cbMF = (ImageView) personalDetailView.findViewById(R.id.cbMF);
        cbDerivative = (ImageView) personalDetailView.findViewById(R.id.cbDerivative);
        cbCurrency = (ImageView) personalDetailView.findViewById(R.id.cbCurrency);
        cbAll = (ImageView) personalDetailView.findViewById(R.id.cbAll);
        cbSameAsCorrespondence = (CheckBox) personalDetailView.findViewById(R.id.cbSameAsCorrespondence);
        cbEqIntraday = (CheckBox) personalDetailView.findViewById(R.id.cbEqIntraday);
        cbEqPositional = (CheckBox) personalDetailView.findViewById(R.id.cbEqPositional);
        cbEqInvestment = (CheckBox) personalDetailView.findViewById(R.id.cbEqInvestment);
        cbDevTrading = (CheckBox) personalDetailView.findViewById(R.id.cbDevTrading);
        cbDevHedging = (CheckBox) personalDetailView.findViewById(R.id.cbDevHedging);
        cbCommInvestment = (CheckBox) personalDetailView.findViewById(R.id.cbCommInvestment);
        cbCommTrading = (CheckBox) personalDetailView.findViewById(R.id.cbCommTrading);
        cbCommHedging = (CheckBox) personalDetailView.findViewById(R.id.cbCommHedging);
        cbCommOther = (CheckBox) personalDetailView.findViewById(R.id.cbCommOther);
        //Client categorization for Commodity
        cbCommercial = (CheckBox) personalDetailView.findViewById(R.id.cbCommercial);
        cbCommercialBullion = (CheckBox) personalDetailView.findViewById(R.id.cbCommercialBullion);
        cbCommercialBaseMetals = (CheckBox) personalDetailView.findViewById(R.id.cbCommercialBaseMetals);
        cbCommercialEnergy = (CheckBox) personalDetailView.findViewById(R.id.cbCommercialEnergy);
        cbCommercialAgriCommodities = (CheckBox) personalDetailView.findViewById(R.id.cbCommercialAgriCommodities);
        cbNonCommercial = (CheckBox) personalDetailView.findViewById(R.id.cbNonCommercial);
        cbNonCommercialBullion = (CheckBox) personalDetailView.findViewById(R.id.cbNonCommercialBullion);
        cbNonCommercialBaseMetals = (CheckBox) personalDetailView.findViewById(R.id.cbNonCommercialBaseMetals);
        cbNonCommercialEnergy = (CheckBox) personalDetailView.findViewById(R.id.cbNonCommercialEnergy);
        cbNonCommercialAgriCommodities = (CheckBox) personalDetailView.findViewById(R.id.cbNonCommercialAgriCommodities);
        etCategorySelectionError = (TextView) personalDetailView.findViewById(R.id.etCategorySelectionError);
        ivCategoryInfo = (ImageView) personalDetailView.findViewById(R.id.ivCategoryInfo);
        llCategorySelection = (LinearLayout) personalDetailView.findViewById(R.id.llCategorySelection);
        llSubCategoryCommercial = (LinearLayout) personalDetailView.findViewById(R.id.llSubCategoryCommercial);
        llSubCategoryNonCommercial = (LinearLayout) personalDetailView.findViewById(R.id.llSubCategoryNonCommercial);

        etGenderError = (TextView) personalDetailView.findViewById(R.id.etGenderError);
        etmaritalError = (TextView) personalDetailView.findViewById(R.id.etmaritalError);
        etPurposeForCommodityError = (TextView) personalDetailView.findViewById(R.id.etPurposeForCommodityError);
        etPurposeForDerivativeError = (TextView) personalDetailView.findViewById(R.id.etPurposeForDerivativeError);
        etPurposeForEquityError = (TextView) personalDetailView.findViewById(R.id.etPurposeForEquityError);
        etCorresCityError = (TextView) personalDetailView.findViewById(R.id.etCorresCityError);
        etPermStateError = (TextView) personalDetailView.findViewById(R.id.etPermStateError);
        etPermCityError = (TextView) personalDetailView.findViewById(R.id.etPermCityError);
        etPermAddProofError = (TextView) personalDetailView.findViewById(R.id.etPermAddProofError);
        etCorresAddProofError = (TextView) personalDetailView.findViewById(R.id.etCorresAddProofError);
        etCorresStateError = (TextView) personalDetailView.findViewById(R.id.etCorresStateError);
        etEqBranchCodeError = (TextView) personalDetailView.findViewById(R.id.etEqBranchCodeError);
        etEqSubrokerCodeError = (TextView) personalDetailView.findViewById(R.id.etEqSubrokerCodeError);
        etEqTraderCodeError = (TextView) personalDetailView.findViewById(R.id.etEqTraderCodeError);
        etEqAreaError = (TextView) personalDetailView.findViewById(R.id.etEqAreaError);
        etEqRegionError = (TextView) personalDetailView.findViewById(R.id.etEqRegionError);
        etCommBranchCodeError = (TextView) personalDetailView.findViewById(R.id.etCommBranchCodeError);
        etCommSubrokerCodeError = (TextView) personalDetailView.findViewById(R.id.etCommSubrokerCodeError);
        etCommTraderCodeError = (TextView) personalDetailView.findViewById(R.id.etCommTraderCodeError);
        etCommAreaError = (TextView) personalDetailView.findViewById(R.id.etCommAreaError);
        etCommRegionError = (TextView) personalDetailView.findViewById(R.id.etCommRegionError);
        etCountryCitizenshipError = (TextView) personalDetailView.findViewById(R.id.etCountryCitizenshipError);
        etClientCodeError = (TextView) personalDetailView.findViewById(R.id.etClientCodeError);
        etBrokerNameError = (TextView) personalDetailView.findViewById(R.id.etBrokerNameError);

        etTitleError = (TextView) personalDetailView.findViewById(R.id.etTitleError);
        etFatherSpouseTitleError = (TextView) personalDetailView.findViewById(R.id.etFatherSpouseTitleError);
        etMotherTitleError = (TextView) personalDetailView.findViewById(R.id.etMotherTitleError);
        etMaidenTitleError = (TextView) personalDetailView.findViewById(R.id.etMaidenTitleError);
        etFatherSpouseSelError = (TextView) personalDetailView.findViewById(R.id.etFatherSpouseSelError);
//        etOtherResidenceError = (EditText) personalDetailView.findViewById(R.id.etOtherResidenceError);
        etOccupationError = (TextView) personalDetailView.findViewById(R.id.etOccupationError);
        etQualificationError = (TextView) personalDetailView.findViewById(R.id.etQualificationError);
        etIncomeError = (TextView) personalDetailView.findViewById(R.id.etIncomeError);
        etStockPortfolioError = (TextView) personalDetailView.findViewById(R.id.etStockPortfolioError);
        etEqExperienceError = (TextView) personalDetailView.findViewById(R.id.etEqExperienceError);
        etEqDerivativeExpError = (TextView) personalDetailView.findViewById(R.id.etEqDerivativeExpError);
        etCommOtherExpError = (TextView) personalDetailView.findViewById(R.id.etCommOtherExpError);
        etCommExperienceError = (TextView) personalDetailView.findViewById(R.id.etCommExperienceError);
        etMFPEPStatusError = (TextView) personalDetailView.findViewById(R.id.etMFPEPStatusError);
        etFirstNameError = (TextView) personalDetailView.findViewById(R.id.etFirstNameError);
        etMiddleNameError = (TextView) personalDetailView.findViewById(R.id.etMiddleNameError);
        etLastNameError = (TextView) personalDetailView.findViewById(R.id.etLastNameError);
        etFatherSpouseFirstNameError = (TextView) personalDetailView.findViewById(R.id.etFatherSpouseFirstNameError);
        etFatherSpouseMidNameError = (TextView) personalDetailView.findViewById(R.id.etFatherSpouseMidNameError);
        etMotherFirstNameError = (TextView) personalDetailView.findViewById(R.id.etMotherFirstNameError);
        etMotherMiddleNameError = (TextView) personalDetailView.findViewById(R.id.etMotherMiddleNameError);
        etMotherLastNameError = (TextView) personalDetailView.findViewById(R.id.etMotherLastNameError);
        etMaidenFirstNameError = (TextView) personalDetailView.findViewById(R.id.etMaidenFirstNameError);
        etMaidenMiddleNameError = (TextView) personalDetailView.findViewById(R.id.etMaidenMiddleNameError);
        etMaidenLastNameError = (TextView) personalDetailView.findViewById(R.id.etMaidenLastNameError);
        etFatherSpouseLastNameError = (TextView) personalDetailView.findViewById(R.id.etFatherSpouseLastNameError);
        etCountryBirthError = (TextView) personalDetailView.findViewById(R.id.etCountryBirthError);
        etTAXIdentificationNumError = (TextView) personalDetailView.findViewById(R.id.etTAXIdentificationNumError);
        etCountryTaxError = (TextView) personalDetailView.findViewById(R.id.etCountryTaxError);
        etPlaceBirthError = (TextView) personalDetailView.findViewById(R.id.etPlaceBirthError);
        etBirthPlaceError = (TextView) personalDetailView.findViewById(R.id.etBirthPlaceError);

        btnNext = (Button) personalDetailView.findViewById(R.id.btnNext);
        tvMobile = (TextView) personalDetailView.findViewById(R.id.tvMobile);
        tvEmail = (TextView) personalDetailView.findViewById(R.id.tvEmail);
        ivNote = (ImageView) personalDetailView.findViewById(R.id.ivNote);
        tvMobile.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvEmail.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvFamilyDeclaration.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvFamilyDeclaration.setOnClickListener(this);
        cbFamilyDeclaration.setOnClickListener(this);
        ivViewFamilyDeclaration.setOnClickListener(this);

//        etMobile.setText("9999999999");
//        etEmail.setText("abc@example.com");
        etEmail.setEnabled(false);
        etMobile.setEnabled(false);
        etDOB.setEnabled(false);

        //Address Spinner
        spCorresCountry = (Spinner) personalDetailView.findViewById(R.id.spCorresCountry);
        spCorresState = (SearchableSpinner) personalDetailView.findViewById(R.id.spCorresState);
        spCorresCity = (SearchableSpinner) personalDetailView.findViewById(R.id.spCorresCity);
        spPermCountry = (Spinner) personalDetailView.findViewById(R.id.spPermCountry);
        spPermState = (SearchableSpinner) personalDetailView.findViewById(R.id.spPermState);
        spPermCity = (SearchableSpinner) personalDetailView.findViewById(R.id.spPermCity);
//        spBirthCountry = (Spinner) personalDetailView.findViewById(R.id.spBirthCountry);
//        spNationality = (Spinner) personalDetailView.findViewById(R.id.spNationality);
        //Experience
        spEqDerivativeExp = (Spinner) personalDetailView.findViewById(R.id.spEqDerivativeExp);
        spEqExperience = (Spinner) personalDetailView.findViewById(R.id.spEqExperience);
        spCommOtherExp = (Spinner) personalDetailView.findViewById(R.id.spCommOtherExp);
        spCommExperience = (Spinner) personalDetailView.findViewById(R.id.spCommExperience);

        spTitle = (Spinner) personalDetailView.findViewById(R.id.spTitle);
        spFatherSpouseTitle = (Spinner) personalDetailView.findViewById(R.id.spFatherSpouseTitle);
        spMotherTitle = (Spinner) personalDetailView.findViewById(R.id.spMotherTitle);
        spMaidenTitle = (Spinner) personalDetailView.findViewById(R.id.spMaidenTitle);
        spFatherSpouseSel = (Spinner) personalDetailView.findViewById(R.id.spFatherSpouseSel);
//        spOtherResidence = (Spinner) personalDetailView.findViewById(R.id.spOtherResidence);
        //Work Detail
        spOccupation = (Spinner) personalDetailView.findViewById(R.id.spOccupation);
        spIncome = (Spinner) personalDetailView.findViewById(R.id.spIncome);
        spStockPortfolio = (Spinner) personalDetailView.findViewById(R.id.spStockPortfolio);
        spQualification = (Spinner) personalDetailView.findViewById(R.id.spQualification);
        spCountryCitizenship = (SearchableSpinner) personalDetailView.findViewById(R.id.spCountryCitizenship);
        spCountryBirth = (SearchableSpinner) personalDetailView.findViewById(R.id.spCountryBirth);
        spCountryTax = (SearchableSpinner) personalDetailView.findViewById(R.id.spCountryTax);
        //Franchise Broker Spinner
        spEqSubBroker = (Spinner) personalDetailView.findViewById(R.id.spEqSubBroker);
        spEqTrader = (Spinner) personalDetailView.findViewById(R.id.spEqTrader);
        spEqArea = (Spinner) personalDetailView.findViewById(R.id.spEqArea);
        spEqRegion = (Spinner) personalDetailView.findViewById(R.id.spEqRegion);
        spEqBranchCode = (Spinner) personalDetailView.findViewById(R.id.spEqBranchCode);
        spCommSubBroker = (Spinner) personalDetailView.findViewById(R.id.spCommSubBroker);
        spCommTrader = (Spinner) personalDetailView.findViewById(R.id.spCommTrader);
        spCommArea = (Spinner) personalDetailView.findViewById(R.id.spCommArea);
        spCommRegion = (Spinner) personalDetailView.findViewById(R.id.spCommRegion);
        spCommBranchCode = (Spinner) personalDetailView.findViewById(R.id.spCommBranchCode);
        spCorresAddProof = (Spinner) personalDetailView.findViewById(R.id.spCorresAddProof);
        spPermAddProof = (Spinner) personalDetailView.findViewById(R.id.spPermAddProof);

        rlSegmentTitle = (RelativeLayout) personalDetailView.findViewById(R.id.rlSegmentTitle);
        llSegmentSelection = (LinearLayout) personalDetailView.findViewById(R.id.llSegmentSelection);

        cbDeclaration = (CheckBox) personalDetailView.findViewById(R.id.cbDeclaration);

        // etCountryCitizenship = (EditText) personalDetailView.findViewById(R.id.etCountryCitizenship);
//        etCountryBirth = (EditText) personalDetailView.findViewById(R.id.etCountryBirth);
        etPlaceBirth = (EditText) personalDetailView.findViewById(R.id.etPlaceBirth);
//        etCountryTax = (EditText) personalDetailView.findViewById(R.id.etCountryTax);
        etTAXIdentificationNum = (EditText) personalDetailView.findViewById(R.id.etTAXIdentificationNum);
        etMFPEPRemark = (EditText) personalDetailView.findViewById(R.id.etMFPEPRemark);

        //  input_layout_CountryCitizenship = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_CountryCitizenship);
//        input_layout_CountryBirth = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_CountryBirth);
        input_layout_PlaceBirth = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_PlaceBirth);
//        input_layout_CountryTax = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_CountryTax);
        input_layout_TAXIdentificationNum = (TextInputLayout) personalDetailView.findViewById(R.id.input_layout_TAXIdentificationNum);
        inputLayoutMFPEPRemark = (TextInputLayout) personalDetailView.findViewById(R.id.inputLayoutMFPEPRemark);

        llCountryCitizenship = (LinearLayout) personalDetailView.findViewById(R.id.llCountryCitizenship);
        llCountryBirth = (LinearLayout) personalDetailView.findViewById(R.id.llCountryBirth);
        llCountryTax = (LinearLayout) personalDetailView.findViewById(R.id.llCountryTax);
        llDeclaration = (LinearLayout) personalDetailView.findViewById(R.id.llDeclaration);
        //RadioButton

        rbMale = (RadioButton) personalDetailView.findViewById(R.id.rbMale);
        rbFemale = (RadioButton) personalDetailView.findViewById(R.id.rbFemale);
        rbUnMarried = (RadioButton) personalDetailView.findViewById(R.id.rbUnMarried);
        rbMarried = (RadioButton) personalDetailView.findViewById(R.id.rbMarried);
        rbMale.setOnClickListener(this);
        rbFemale.setOnClickListener(this);
        rbUnMarried.setOnClickListener(this);
        rbMarried.setOnClickListener(this);
        spMFPEPStatus.setOnTouchListener(mfPEPRemarlListener);

        etBldgNamePerm.addTextChangedListener(this);
        etFlatNamePerm.addTextChangedListener(this);
        etAreaPerm.addTextChangedListener(this);
        etSebiAuthority.addTextChangedListener(this);
        etFmcSebiAuthority.addTextChangedListener(this);
        etNWDate.addTextChangedListener(this);

        //Toggle button
        togglePoliticaly = (ToggleButton) personalDetailView.findViewById(R.id.togglePoliticaly);
        toggleRelatedPolitical = (ToggleButton) personalDetailView.findViewById(R.id.toggleRelatedPolitical);
        toggleRegWithBroker = (ToggleButton) personalDetailView.findViewById(R.id.toggleRegWithBroker);
        toggleSEBIAuthority = (ToggleButton) personalDetailView.findViewById(R.id.toggleSEBIAuthority);
        toggleFmcSebiAuthority = (ToggleButton) personalDetailView.findViewById(R.id.toggleFmcSebiAuthority);
        togglePoliticaly.setOnClickListener(this);
        toggleRelatedPolitical.setOnClickListener(this);
        toggleRegWithBroker.setOnClickListener(this);
        toggleSEBIAuthority.setOnClickListener(this);
        toggleFmcSebiAuthority.setOnClickListener(this);
        //Toggle button fatca
        toggleAddressOutsideIndia = (ToggleButton) personalDetailView.findViewById(R.id.toggleAddressOutsideIndia);
        togglePoaAddress = (ToggleButton) personalDetailView.findViewById(R.id.togglePoaAddress);
        toggleCountryTax = (ToggleButton) personalDetailView.findViewById(R.id.toggleCountryTax);
        toggleCountryBirth = (ToggleButton) personalDetailView.findViewById(R.id.toggleCountryBirth);
        toggleCountryCitizen = (ToggleButton) personalDetailView.findViewById(R.id.toggleCountryCitizen);
        toggleAddressOutsideIndia.setOnClickListener(this);
        togglePoaAddress.setOnClickListener(this);
        toggleCountryTax.setOnClickListener(this);
        toggleCountryBirth.setOnClickListener(this);
        toggleCountryCitizen.setOnClickListener(this);
       /* togglePoliticaly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(togglePoliticaly.isChecked()){
                    Toast.makeText(mContext,"yes",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext,"no",Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        cbDeclaration.setOnCheckedChangeListener(this);

        arrAdapOcc = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.occupation))));
        arrAdapIncome = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.income))));
        arrAdapStockPortfolio = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.stockPortfolio))));
        arrAdapNetWorth = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.netWorth))));
        arrAdapMaritalStatus = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.maritalStatus))));
        arrAdapEqExperience = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.equTradExp))));
        arrAdapDerivativeExp = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.deriativeExp))));
        arrAdapCommExperience = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.commTradExp))));
        arrAdapCommOtherExp = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.otherExp))));
        arrAdapTitle = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.title))));
        arrAdapFemaleTitle = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.femaleTitle))));
        arrAdapMaidenTitle = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.maidenTitle))));
        arrAdapFatherSpouseSel = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.fatherSpouseSel))));
        arrAdapResForOtherCountryTax = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.resForOtherCountryTax))));
        arrAdapQualification = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.qualification))));
        arrAdapCountryBirth = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.country))));
        arrAdapNationality = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.nationality))));


        spTitle.setOnItemSelectedListener(commonListener);
        spFatherSpouseTitle.setOnItemSelectedListener(commonListener);
        spMotherTitle.setOnItemSelectedListener(commonListener);
        spMaidenTitle.setOnItemSelectedListener(commonListener);
        spFatherSpouseSel.setOnItemSelectedListener(commonListener);
//        spOtherResidence.setOnItemSelectedListener(commonListener);
        spOccupation.setOnItemSelectedListener(commonListener);
        spQualification.setOnItemSelectedListener(commonListener);
        spIncome.setOnItemSelectedListener(commonListener);
        spStockPortfolio.setOnItemSelectedListener(commonListener);
        spEqExperience.setOnItemSelectedListener(commonListener);
        spEqDerivativeExp.setOnItemSelectedListener(commonListener);
        spCommExperience.setOnItemSelectedListener(commonListener);
        spCommOtherExp.setOnItemSelectedListener(commonListener);
        spMFPEPStatus.setOnItemSelectedListener(commonListener);
        spCorresAddProof.setOnItemSelectedListener(commonListener);
        spPermAddProof.setOnItemSelectedListener(commonListener);
        spCorresCountry.setOnItemSelectedListener(commonListener);
        spPermCountry.setOnItemSelectedListener(commonListener);
        spCountryCitizenship.setOnItemSelectedListener(commonListener);
        spCountryBirth.setOnItemSelectedListener(commonListener);
        spCountryTax.setOnItemSelectedListener(commonListener);

        tvFatcaDeclaration.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        //OnClickListener
        tvMobile.setOnClickListener(this);
        tvEmail.setOnClickListener(this);
        etDOB.setOnClickListener(this);
        etNWDate.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        ivNote.setOnClickListener(this);
        tvFatcaDeclaration.setOnClickListener(this);
        cbEquity.setOnClickListener(this);
        //OnCheckedChangeListener Ckeckbox

        //  cbEquity.setOnCheckedChangeListener(this);
        cbAll.setOnClickListener(this);
        cbDerivative.setOnClickListener(this);
        cbCurrency.setOnClickListener(this);
        cbCommodity.setOnClickListener(this);
        cbSameAsCorrespondence.setOnCheckedChangeListener(this);
        cbCommOther.setOnCheckedChangeListener(this);
        cbEqIntraday.setOnCheckedChangeListener(this);
        cbEqInvestment.setOnCheckedChangeListener(this);
        cbEqPositional.setOnCheckedChangeListener(this);
        cbDevHedging.setOnCheckedChangeListener(this);
        cbDevTrading.setOnCheckedChangeListener(this);
        cbCommHedging.setOnCheckedChangeListener(this);
        cbCommInvestment.setOnCheckedChangeListener(this);
        cbCommTrading.setOnCheckedChangeListener(this);
        cbCommOther.setOnCheckedChangeListener(this);
        //Client categorization for Commodity
        cbCommercial.setOnCheckedChangeListener(this);
        cbCommercialBullion.setOnCheckedChangeListener(this);
        cbCommercialBaseMetals.setOnCheckedChangeListener(this);
        cbCommercialEnergy.setOnCheckedChangeListener(this);
        cbCommercialAgriCommodities.setOnCheckedChangeListener(this);
        cbNonCommercial.setOnCheckedChangeListener(this);
        cbNonCommercialBullion.setOnCheckedChangeListener(this);
        cbNonCommercialBaseMetals.setOnCheckedChangeListener(this);
        cbNonCommercialEnergy.setOnCheckedChangeListener(this);
        cbNonCommercialAgriCommodities.setOnCheckedChangeListener(this);
        ivCategoryInfo.setOnClickListener(this);

//        etPAN.setText(PANAdharDetails.getPANNo());
//        etAadhar.setText(PANAdharDetails.getAadharNo());
//        etDOB.setText(PANAdharDetails.getDOB());

        if (getAppType().equalsIgnoreCase(PP)) {
            tvRelationshipDetails.setVisibility(View.VISIBLE);
        } else {
            tvRelationshipDetails.setVisibility(View.GONE);
        }

        etDOB.setFocusable(false);
        Calendar newCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        /*fromDatePickerDialog = new DatePickerDialog(mContext, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Calendar minNetworthDate = new GregorianCalendar();
                Calendar maxNetworthDate = new GregorianCalendar();
                Calendar minAdultAge = new GregorianCalendar();

                String financial = getStartDtFinancialYear("dd/MM/yyyy");//get Financial date
                Date currentDate = EkycAppUtil.getDate(dateFormatter.format(newDate.getTime()), "dd/MM/yyyy");//curent date
                Date financialDate = EkycAppUtil.getDate(financial, "dd/MM/yyyy");//financial date
                minNetworthDate.add(Calendar.YEAR, -1);//condition for future date
                maxNetworthDate.add(Calendar.DAY_OF_MONTH, 0);//previous date 1 year
                minAdultAge.add(Calendar.YEAR, -18);
                if (DOBClicked) {
//                    etDOB.setText(PANAdharDetails.getDOB());
                    if (minAdultAge.before(newDate)) {
                        final Dialog dialog = new Dialog(mContext);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialog.getWindow().setWindowAnimations(R.style.animationName);
                        dialog.setContentView(R.layout.layout_dialog_info);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCanceledOnTouchOutside(false);

                        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
                        Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
                        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);

                        tvMsg.setText(getResources().getString(R.string.minor_age_bracket_note));
                        ivClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    } else {
                        etDOB.setText(dateFormatter.format(newDate.getTime()));
                    }
                } else {
                    if (currentDate.before(financialDate) || minNetworthDate.after(newDate)) {
                        commonMsgDialog(mContext, Date_should_not_be_less_than_one_year, false, "");
                    } else {
                        etNWDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());*/

        /*fromDatePickerDialog = new CustomDatePickerDialog(mContext, new CustomDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etNWDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));*/
        etDOB.setInputType(InputType.TYPE_NULL);
        etNWDate.setInputType(InputType.TYPE_NULL);
        etGST.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15), new InputFilter.AllCaps()});

        if (getInterestedInMF()) {

            //tick MF segment icon
            spOccArrayAdapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.mf_occupation));
            spOccupation.setAdapter(spOccArrayAdapter);

            if (!isMfSegmentExist()) {
                //MF in process
                equitySpinner();
                baMFSpinnerTitle();
                /*rlSegmentTitle.setVisibility(View.GONE);
                llSegmentSelection.setVisibility(View.GONE);*/
                showSegmentSelectionSection(false);
//                llResidentInOtherCountry.setVisibility(View.GONE);

                llPEP.setVisibility(View.GONE);
                llRelatedPEP.setVisibility(View.GONE);
                llRegisterToOtherBroker.setVisibility(View.GONE);
                llMFPEPStatus.setVisibility(View.VISIBLE);
                inputLayoutAltTelNo.setVisibility(View.VISIBLE);
                inputLayoutAltEmailID.setVisibility(View.VISIBLE);
                tvPersonalDetailNote.setVisibility(View.GONE);
                etElectronicNote.setVisibility(View.GONE);
            } else {
                //MF + broking as 2nd product in process
                inputLayoutAltTelNo.setVisibility(View.GONE);
                inputLayoutAltEmailID.setVisibility(View.GONE);
                llMFPEPStatus.setVisibility(View.GONE);
                llPEP.setVisibility(View.VISIBLE);
                /*rlSegmentTitle.setVisibility(View.VISIBLE);
                llSegmentSelection.setVisibility(View.VISIBLE);*/
                showSegmentSelectionSection(true);
//                llResidentInOtherCountry.setVisibility(View.VISIBLE);
            }
            llStockPortfolio.setVisibility(View.GONE);
        } else {
            spOccArrayAdapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.occupation));
            spOccupation.setAdapter(spOccArrayAdapter);
            //Broking and in-process
            llStockPortfolio.setVisibility(View.VISIBLE);
            inputLayoutAltTelNo.setVisibility(View.GONE);
            inputLayoutAltEmailID.setVisibility(View.GONE);
            llMFPEPStatus.setVisibility(View.GONE);
            llPEP.setVisibility(View.VISIBLE);
            /*rlSegmentTitle.setVisibility(View.VISIBLE);
            llSegmentSelection.setVisibility(View.VISIBLE);*/
            showSegmentSelectionSection(true);
            tvPersonalDetailNote.setVisibility(View.VISIBLE);
            etElectronicNote.setVisibility(View.VISIBLE);
//            llResidentInOtherCountry.setVisibility(View.VISIBLE);
            //setIsEquitySelected(true);
        }

        etBldgNameCorres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etBldgNameCorres.getText().toString().equals("") && input_layout_bldgNameCorres.isErrorEnabled()) {
                    input_layout_bldgNameCorres.setErrorEnabled(false);
                    setPermentAddress(etBldgNameCorres, etBldgNamePerm);
                } else if ((isDisableEnableFieldCalled && etBldgNameCorres.isEnabled()) && !etBldgNameCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etBldgNameCorres.getText().toString())) {
                    input_layout_bldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
//                    etBldgNameCorres.requestFocus();
                } else {
                    input_layout_bldgNameCorres.setErrorEnabled(false);
                    setPermentAddress(etBldgNameCorres, etBldgNamePerm);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etFlatNoCorres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etFlatNoCorres.getText().toString().equals("") && input_layout_flatNoCorres.isErrorEnabled()) {
                    input_layout_flatNoCorres.setErrorEnabled(false);
                    setPermentAddress(etFlatNoCorres, etFlatNamePerm);
                } else if ((isDisableEnableFieldCalled && etFlatNoCorres.isEnabled()) && !etFlatNoCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etFlatNoCorres.getText().toString())) {
                    input_layout_flatNoCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                } else {
                    input_layout_flatNoCorres.setErrorEnabled(false);
                    setPermentAddress(etFlatNoCorres, etFlatNamePerm);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etAreaCorres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etAreaCorres.getText().toString().equals("") && input_layout_areaCorres.isErrorEnabled()) {
                    input_layout_areaCorres.setErrorEnabled(false);
                    setPermentAddress(etAreaCorres, etAreaPerm);
                } else if ((isDisableEnableFieldCalled && etAreaCorres.isEnabled()) && !etAreaCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etAreaCorres.getText().toString())) {
                    input_layout_areaCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                } else {
                    input_layout_areaCorres.setErrorEnabled(false);
                    setPermentAddress(etAreaCorres, etAreaPerm);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPincodePerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etPincodePerm.getText().toString())) {
                    input_layout_pincodePerm.setErrorEnabled(false);
                    if (etPincodePerm.length() < 6) {
                        etPincodePermError.setVisibility(View.VISIBLE);
                        etPincodePermError.setText("Invalid pincode.");
                        etPincodePerm.requestFocus();
                    } else {
                        etPincodePermError.setVisibility(View.GONE);
                        input_layout_pincodePerm.setErrorEnabled(false);
                    }
                } else {
                    etPincodePermError.setVisibility(View.GONE);
                    input_layout_pincodePerm.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPincodeCorres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etPincodeCorres.getText().toString())) {
                    input_layout_pincodeCorres.setErrorEnabled(false);
                    if (etPincodeCorres.length() < 6) {
                        etPincodeCorresError.setVisibility(View.VISIBLE);
                        etPincodeCorresError.setText("Invalid pincode.");
                        etPincodeCorres.requestFocus();
                    } else {
                        etPincodeCorresError.setVisibility(View.GONE);
                        input_layout_pincodeCorres.setErrorEnabled(false);
                    }
                } else {
                    etPincodeCorresError.setVisibility(View.GONE);
                    input_layout_pincodeCorres.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etFirstName.getText().toString());
                if (!isBlank(etFirstName.getText().toString())) {
                    input_layout_firstname.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etFirstName.setText("");
                        etFirstName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etFirstName.getText().toString().trim())) {
                        etFirstNameError.setVisibility(View.VISIBLE);
                        etFirstNameError.setText(ErrorMsgName);
                        etFirstName.requestFocus();
                    } else {
                        etFirstNameError.setVisibility(View.GONE);
                    }
                } else {
                    etFirstNameError.setVisibility(View.GONE);
                }
            }
        });

        etMiddleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etMiddleName.getText().toString());
                if (!isBlank(etMiddleName.getText().toString())) {
                    if (!s.toString().equals(newStr)) {
                        etMiddleName.setText("");
                        etMiddleName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etMiddleName.getText().toString().trim())) {
                        etMiddleNameError.setVisibility(View.VISIBLE);
                        etMiddleNameError.setText(ErrorMsgName);
                        etMiddleName.requestFocus();
                    } else {
                        etMiddleNameError.setVisibility(View.GONE);
                    }
                } else {
                    etMiddleNameError.setVisibility(View.GONE);
                }
            }
        });

        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etLastName.getText().toString());
                if (!isBlank(etLastName.getText().toString())) {
                    input_layout_lastname.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etLastName.setText("");
                        etLastName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etLastName.getText().toString().trim())) {
                        etLastNameError.setVisibility(View.VISIBLE);
                        etLastNameError.setText(ErrorMsgName);
                        etLastName.requestFocus();
                    } else {
                        etLastNameError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_lastname.setErrorEnabled(false);
                    etLastNameError.setVisibility(View.GONE);
                }
            }
        });
        etFatherSpouseFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etFatherSpouseFirstName.getText().toString());
                if (!isBlank(etFatherSpouseFirstName.getText().toString())) {
                    input_layout_fatherSpouseFirstName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etFatherSpouseFirstName.setText("");
                        etFatherSpouseFirstName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etFatherSpouseFirstName.getText().toString().trim())) {
                        etFatherSpouseFirstNameError.setVisibility(View.VISIBLE);
                        etFatherSpouseFirstNameError.setText(ErrorMsgName);
                        etFatherSpouseFirstName.requestFocus();
                    } else {
                        etFatherSpouseFirstNameError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_fatherSpouseFirstName.setErrorEnabled(false);
                    etFatherSpouseFirstNameError.setVisibility(View.GONE);
                }
            }
        });

        etFatherSpouseMidName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etFatherSpouseMidName.getText().toString());
                if (!isBlank(etFatherSpouseMidName.getText().toString())) {
                    input_layout_fatherSpouseMidName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etFatherSpouseMidName.setText("");
                        etFatherSpouseMidName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etFatherSpouseMidName.getText().toString().trim())) {
                        etFatherSpouseMidNameError.setVisibility(View.VISIBLE);
                        etFatherSpouseMidNameError.setText(ErrorMsgName);
                        etFatherSpouseMidName.requestFocus();
                    } else {
                        etFatherSpouseMidNameError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_fatherSpouseMidName.setErrorEnabled(false);
                    etFatherSpouseMidNameError.setVisibility(View.GONE);
                }
            }
        });
        etFatherSpouseLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etFatherSpouseLastName.getText().toString());
                if (!isBlank(etFatherSpouseLastName.getText().toString())) {
                    input_layout_fatherSpouseLastName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etFatherSpouseLastName.setText("");
                        etFatherSpouseLastName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etFatherSpouseLastName.getText().toString().trim())) {
                        etFatherSpouseLastNameError.setVisibility(View.VISIBLE);
                        etFatherSpouseLastNameError.setText(ErrorMsgName);
                        etFatherSpouseLastName.requestFocus();
                    } else {
                        etFatherSpouseLastNameError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_fatherSpouseLastName.setErrorEnabled(false);
                    etFatherSpouseLastNameError.setVisibility(View.GONE);
                }
            }
        });

        etMotherFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etMotherFirstName.getText().toString().trim());
                if (!isBlank(etMotherFirstName.getText().toString())) {
                    input_layout_MotherFirstname.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etMotherFirstName.setText("");
                        etMotherFirstName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etMotherFirstName.getText().toString().trim())) {
                        etMotherFirstNameError.setVisibility(View.VISIBLE);
                        etMotherFirstNameError.setText(ErrorMsgName);
                        etMotherFirstName.requestFocus();
                    } else {
                        etMotherFirstNameError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_MotherFirstname.setErrorEnabled(false);
                    etMotherFirstNameError.setVisibility(View.GONE);
                }
            }
        });

        etMotherMiddleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etMotherMiddleName.getText().toString().trim());
                if (!isBlank(etMotherMiddleName.getText().toString())) {
                    input_layout_MotherMiddlename.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etMotherMiddleName.setText("");
                        etMotherMiddleName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etMotherMiddleName.getText().toString().trim())) {
                        etMotherMiddleNameError.setVisibility(View.VISIBLE);
                        etMotherMiddleNameError.setText(ErrorMsgName);
                        etMotherMiddleName.requestFocus();
                    } else {
                        etMotherMiddleNameError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_MotherMiddlename.setErrorEnabled(false);
                    etMotherMiddleNameError.setVisibility(View.GONE);
                }
            }
        });

        etMotherLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etMotherLastName.getText().toString().trim());
                if (!isBlank(etMotherLastName.getText().toString())) {
                    input_layout_MotherLastname.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etMotherLastName.setText("");
                        etMotherLastName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etMotherLastName.getText().toString().trim())) {
                        etMotherLastNameError.setVisibility(View.VISIBLE);
                        etMotherLastNameError.setText(ErrorMsgName);
                        etMotherLastName.requestFocus();
                    } else {
                        etMotherLastNameError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_MotherLastname.setErrorEnabled(false);
                    etMotherLastNameError.setVisibility(View.GONE);
                }
            }
        });

        etMaidenFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etMaidenFirstName.getText().toString());
                if (!isBlank(etMaidenFirstName.getText().toString())) {
                    input_layout_MaidenFirstname.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etMaidenFirstName.setText("");
                        etMaidenFirstName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etMaidenFirstName.getText().toString().trim())) {
                        etMaidenFirstNameError.setVisibility(View.VISIBLE);
                        etMaidenFirstNameError.setText(ErrorMsgName);
                        etMaidenFirstName.requestFocus();
                    } else {
                        etMaidenFirstNameError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_MaidenFirstname.setErrorEnabled(false);
                    etMaidenFirstNameError.setVisibility(View.GONE);
                }
            }
        });

        etMaidenMiddleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etMaidenMiddleName.getText().toString());
                if (!isBlank(etMaidenMiddleName.getText().toString())) {
                    input_layout_MaidenMiddlename.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etMaidenMiddleName.setText("");
                        etMaidenMiddleName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etMaidenMiddleName.getText().toString().trim())) {
                        etMaidenMiddleNameError.setVisibility(View.VISIBLE);
                        etMaidenMiddleNameError.setText(ErrorMsgName);
                        etMaidenMiddleName.requestFocus();
                    } else {
                        etMaidenMiddleNameError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_MaidenMiddlename.setErrorEnabled(false);
                    etMaidenMiddleNameError.setVisibility(View.GONE);
                }
            }
        });

        etMaidenLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etMaidenLastName.getText().toString());
                if (!isBlank(etMaidenLastName.getText().toString())) {
                    input_layout_MaidenLastname.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etMaidenLastName.setText("");
                        etMaidenLastName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etMaidenLastName.getText().toString().trim())) {
                        etMaidenLastNameError.setVisibility(View.VISIBLE);
                        etMaidenLastNameError.setText(ErrorMsgName);
                        etMaidenLastName.requestFocus();
                    } else {
                        etMaidenLastNameError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_MaidenLastname.setErrorEnabled(false);
                    etMaidenLastNameError.setVisibility(View.GONE);
                }
            }
        });


        etBirthPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isBlank(etBirthPlace.getText().toString())) {
                    input_layout_birthPlace.setErrorEnabled(false);
                    if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etBirthPlace.getText().toString().trim())) {
                        etBirthPlaceError.setVisibility(View.VISIBLE);
                        etBirthPlaceError.setText(ErrorMsgName);
                        etBirthPlace.requestFocus();
                    } else {
                        etBirthPlaceError.setVisibility(View.GONE);
                    }
                } else {
                    etBirthPlaceError.setVisibility(View.GONE);
                    input_layout_birthPlace.setErrorEnabled(false);
                }
            }
        });

        etGST.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFullWithMid(etGST.getText().toString());
                if (!isBlank(etGST.getText().toString())) {
                    input_layout_GST.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etGST.setText("");
                        etGST.append(newStr);
                    } else if (!ValidationRule.validateGST(etGST.getText().toString().trim())) {
                        input_layout_GST.setError(ErrorMsgGST);
                    }
                } else {
                    input_layout_GST.setErrorEnabled(false);
                }
            }
        });

        etTAXIdentificationNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isBlank(etTAXIdentificationNum.getText().toString())) {
                    input_layout_TAXIdentificationNum.setErrorEnabled(false);
                    if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(etTAXIdentificationNum.getText().toString().trim())) {
                        etTAXIdentificationNumError.setVisibility(View.VISIBLE);
                        etTAXIdentificationNumError.setText(ErrorMsgTaxIde);
                        etTAXIdentificationNum.requestFocus();
                    } else {
                        etTAXIdentificationNumError.setVisibility(View.GONE);
                    }
                } else {
                    etTAXIdentificationNumError.setVisibility(View.GONE);
                    input_layout_TAXIdentificationNum.setErrorEnabled(false);
                }
            }
        });
        etFbId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etFbId.getText().toString())) {
                    input_layout_fbId.setErrorEnabled(false);
                    if (etFbId.length() == 0) {
                        etFbIdError.setVisibility(View.VISIBLE);
                        etFbIdError.setText("Enter facebook id");
                    } else if (!isValidEmail(etFbId.getText())) {
                        etFbIdError.setVisibility(View.VISIBLE);
                        etFbIdError.setText("Facebook id is not valid");
                    } else {
                        //  removeKeyboard(mContext, dialog.getCurrentFocus());
                        etFbIdError.setVisibility(View.GONE);
                    }
                } else {
                    etFbIdError.setVisibility(View.GONE);
                    input_layout_fbId.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etLinkedinID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etLinkedinID.getText().toString())) {
                    input_layout_linkedinID.setErrorEnabled(false);
                    if (etLinkedinID.length() == 0) {
                        etLinkedinIDError.setVisibility(View.VISIBLE);
                        etLinkedinIDError.setText("Enter linkedin id");
                    } else if (!isValidEmail(etLinkedinID.getText())) {
                        etLinkedinIDError.setVisibility(View.VISIBLE);
                        etLinkedinIDError.setText("Linkedin id is not valid");
                    } else {
                        //  removeKeyboard(mContext, dialog.getCurrentFocus());
                        etLinkedinIDError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_linkedinID.setErrorEnabled(false);
                    etLinkedinIDError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etMobile.getText().toString())) {
                    input_layout_mobile.setErrorEnabled(false);
                    if (!ValidationRule.validateMobile(etMobile.getText().toString())) {
                        etMobileError.setVisibility(View.VISIBLE);
                        etMobileError.setText("Invalid mobile no.");
                        etMobile.requestFocus();
                        mobile = true;
                    } else {
                        etMobileError.setVisibility(View.GONE);
                        input_layout_mobile.setErrorEnabled(false);
                        if (mobile) {
                            //   checkMobileEmail("Mobile",etMobile.getText().toString());
                            mobile = false;
                        }

                    }
                } else {
                    etMobileError.setVisibility(View.GONE);
                    input_layout_mobile.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etAltTelNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etAltTelNo.getText().toString())) {
                    inputLayoutAltTelNo.setErrorEnabled(false);
                    if (!ValidationRule.validateMobile(etAltTelNo.getText().toString())) {
                        etAltMobileError.setVisibility(View.VISIBLE);
                        etAltMobileError.setText("Invalid mobile no.");
                        etAltTelNo.requestFocus();
                    } else {
                        etAltMobileError.setVisibility(View.GONE);
                        inputLayoutAltTelNo.setErrorEnabled(false);
//                    checkMobileEmail("Mobile",etMobile.getText().toString());
                    }
                } else {
                    etAltMobileError.setVisibility(View.GONE);
                    inputLayoutAltTelNo.setErrorEnabled(false);
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
                if (!isBlank(etEmail.getText().toString())) {
                    input_layout_email.setErrorEnabled(false);
                    if (!isValidEmail(etEmail.getText())) {
                        etEmailError.setVisibility(View.VISIBLE);
                        etEmailError.setText("Invalid email id");
                        etEmail.requestFocus();
                        email = true;
                    } else {
                        etEmailError.setVisibility(View.GONE);
                        etElectronicNote.setText(etEmail.getText().toString());
                        if (email) {
                            //  checkMobileEmail("Email",etEmail.getText().toString());
                            email = false;
                        }
                    }
                } else {
                    etEmailError.setVisibility(View.GONE);
                    input_layout_email.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etAltEmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etAltEmailID.getText().toString())) {
                    inputLayoutAltEmailID.setErrorEnabled(false);
                    if (!isValidEmail(etAltEmailID.getText())) {
                        etAltEmailError.setVisibility(View.VISIBLE);
                        etAltEmailError.setText("Invalid email id");
                        etAltEmailID.requestFocus();
                    } else {
                        etAltEmailError.setVisibility(View.GONE);
//                      checkMobileEmail("Email",etEmail.getText().toString());
                    }
                } else {
                    inputLayoutAltEmailID.setErrorEnabled(false);
                    etAltEmailError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNetWorth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                strNetWorth = etNetWorth.getText().toString();
                if (!isBlank(etNetWorth.getText().toString())) {
                    input_layout_netWorth.setErrorEnabled(false);
                    if (etNetWorth.length() < 1) {
                        etNetWorthError.setVisibility(View.VISIBLE);
                        etNetWorthError.setText("Net worth should be one digit");
                        etNetWorth.requestFocus();
                    } else if (etNetWorth.getText().toString().equalsIgnoreCase("0")) {
                        etNetWorthError.setVisibility(View.VISIBLE);
                        etNetWorthError.setText("Net worth should be greater than 0");
                        etNetWorth.requestFocus();
                    } else if (!strNetWorth.equals("")) {
                        if (Double.parseDouble(strNetWorth) <= 0) {
                            etNetWorthError.setVisibility(View.VISIBLE);
                            etNetWorthError.setText("Net worth should be greater than 0");
                            etNetWorth.requestFocus();
                        } else {
                            etNetWorthError.setVisibility(View.GONE);
                        }
                    } else {
                        etNetWorthError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_netWorth.setErrorEnabled(false);
                    etNetWorthError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etPlaceBirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isBlank(etPlaceBirth.getText().toString())) {
                    input_layout_PlaceBirth.setErrorEnabled(false);
                    if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etPlaceBirth.getText().toString().trim())) {
                        etPlaceBirthError.setVisibility(View.VISIBLE);
                        etPlaceBirthError.setText(ErrorMsgName);
                        etPlaceBirth.requestFocus();
                    } else {
                        etPlaceBirthError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_PlaceBirth.setErrorEnabled(false);
                    etPlaceBirthError.setVisibility(View.GONE);
                }
            }
        });


        etBrokerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etBrokerName.getText().toString());
                if (!isBlank(etBrokerName.getText().toString())) {
                    input_layout_brokerName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etBrokerName.setText("");
                        etBrokerName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(etBrokerName.getText().toString().trim())) {
                        etBrokerNameError.setVisibility(View.VISIBLE);
                        etBrokerNameError.setText(ErrorMsgTaxIde);
                        etBrokerName.requestFocus();
                    } else {
                        etBrokerNameError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_brokerName.setErrorEnabled(false);
                    etBrokerNameError.setVisibility(View.GONE);
                }
            }
        });


        etClientCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isBlank(etClientCode.getText().toString())) {
                    input_layout_clientCode.setErrorEnabled(false);
                    if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(etClientCode.getText().toString().trim())) {
                        etClientCodeError.setVisibility(View.VISIBLE);
                        etClientCodeError.setText(ErrorMsgTaxIde);
                        etClientCode.requestFocus();
                    } else {
                        etClientCodeError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_clientCode.setErrorEnabled(false);
                    etClientCodeError.setVisibility(View.GONE);
                }
            }
        });

        etSTDNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etSTDNo.getText().toString())) {
                    input_layout_std.setErrorEnabled(false);
                    if (etSTDNo.getText().toString().length() < 3) {
                        etSTDTelError.setVisibility(View.VISIBLE);
                        etSTDTelError.setText("Invalid STD code");
                        etSTDNo.requestFocus();
                    } else {
                        etSTDTelError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_std.setErrorEnabled(false);
                    etSTDTelError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTelNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etTelNo.getText().toString())) {
                    input_layout_telNo.setErrorEnabled(false);
                    if (etTelNo.getText().toString().length() < 6) {
                        etSTDTelError.setVisibility(View.VISIBLE);
                        etSTDTelError.setText(Res_Telephone_number_min_11_digit);
                        etTelNo.requestFocus();
                    } else {
                        etSTDTelError.setVisibility(View.GONE);
                    }
                } else {
                    input_layout_telNo.setErrorEnabled(false);
                    etSTDTelError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etAadhar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etAadhar.getText().toString())) {
                    input_layout_aadhar.setErrorEnabled(false);
                    if (etAadhar.length() < 12) {
                        input_layout_aadhar.setError("Enter valid aadhaar number");
                        etAadhar.requestFocus();
                    } else {
                        input_layout_aadhar.setErrorEnabled(false);
                    }
                } else {
                    input_layout_aadhar.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        countryList = loadCountryArray(getContext());
        countryListWithoutIndia = loadCountryArray(getContext(), true);
//        arrAdapCountry = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item,
//                loadCountryArray(getContext()));
        arrAdapCountry = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.country))));
        arrAdapCountryFatca = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item,
                loadCountryArray(getContext(), true));

//        arrAdapCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrAdapCountryFatca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCountryCitizenship.setAdapter(arrAdapCountryFatca);
        spCountryBirth.setAdapter(arrAdapCountryFatca);
        spCountryTax.setAdapter(arrAdapCountryFatca);

//        spCorresCountry.setAdapter(arrAdapCountry);
//        spPermCountry.setAdapter(arrAdapCountry);
    }

    public void getState() {
        Map inputMap = new HashMap<>();
        inputMap.put("", "");

        String flag = BINDSTATE;
        String category = SIGNUP;
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            arrState = new ArrayList<>();
                            if (statusCode == 200) {
                                if (response != null) {
                                    String stateCode, stateName;
                                    arrState.clear();
                                    arrState.add(new RowData(0, "-1", "Select"));
                                    try {
                                        JSONArray jsonArray = response.getJSONArray(STATE_DETAILS);
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);

                                                stateCode = object.getString("STATE_CODE");
                                                stateName = object.getString("STATE_NAME");
                                                arrState.add(new RowData(i + 1, stateCode, stateName));
                                            }
                                        }
                                        arrAdapState = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrState);
                                        arrAdapState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCorresState.setAdapter(arrAdapState);
                                        spCorresState.setOnItemSelectedListener(corresStateListener);
                                        spPermState.setAdapter(arrAdapState);
                                        spPermState.setOnItemSelectedListener(permStateListener);
                                        cityFoundFlag = false;

                                        getInterestedIn();


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                                        alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                    }
                                    hideProgress();
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                    MOSLCommon.showToastMsg(mContext, "No state");
                                }
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
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                                throwable) {
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                                errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray
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
///

    private void getFamilyRelationDialog(final List<FamilyDeclarationModel> dataList, ArrayAdapter relationAdapter, final CustomDialogFamilyRelationship resendCd, final String status) {
        final FamilyDeclarationAdapter declarationAdapter;
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_family_decleration);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button sumitButton = (Button) dialog.findViewById(R.id.btnsubmit);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ImageView ivEditFamilyDeclaration = (ImageView) dialog.findViewById(R.id.ivEditFamilyDeclaration);
        final LinearLayout dailog_custom_progress = (LinearLayout) dialog.findViewById(R.id.customProgress);
        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.rvFamilyrelation);

        declarationAdapter = new FamilyDeclarationAdapter(dataList, relationAdapter);
        /*
        * First time hide the button
        * If OfflineDocumentUploaded Or IPV Stage Done then hide the button
        * */
        if (visibilityFlagFD && !isIpvORDocumentStageDone()) {
            ivEditFamilyDeclaration.setVisibility(View.VISIBLE);
        } else if (visibilityFlagFD && isIpvORDocumentStageDone() && ((getIsEquitySelected() && !isEquityFundTransfered()) ||
                (getIsCommoditySelected() && !isCommodityFundTransfered()))) {
            ivEditFamilyDeclaration.setVisibility(View.VISIBLE);
        } else {
            ivEditFamilyDeclaration.setVisibility(View.GONE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(declarationAdapter);
        recyclerView.addItemDecoration(new RecycyclerDecorator(getActivity()));
        sumitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = false;
                selfCount = 0;
                fatherCount = 0;
                motherCount = 0;
                spouseCount = 0;
                updateFamilyDetailsCount = 0;
                if (dataList.size() != 0) {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).getRelationship().equalsIgnoreCase("Select")) {
                            flag = false;
                            commonMsgDialog(getActivity(), getResources().getString(R.string.family_declaration_error), false, "");
                            return;
                        } else if (dataList.get(i).getRelationship().equalsIgnoreCase("Not a family member")) {
                            flag = false;
//                            commonMsgDialog(getActivity(), getResources().getString(R.string.not_family_error_message), false, "");
                            CustomDialog customDialog = new CustomDialog(mContext, getResources().getString(R.string.not_family_error_message), true, false, false, "", OK, "",
                                    new CustomDialog.CustomDialogListener() {
                                        @Override
                                        public void dialogAction(MOSLCommon.Action action, Object... data) {
                                            if (!isBlank(status)) {
                                                dialog.dismiss();
                                                if (resendCd != null)
                                                    resendCd.dismiss();
                                            }
                                            if (!status.equalsIgnoreCase("") && status.equalsIgnoreCase("2")) {
                                                etEmail.setText("");
                                                etEmail.requestFocus();
                                                etEmail.setEnabled(true);
                                            } else if (!status.equalsIgnoreCase("") && status.equalsIgnoreCase("3")) {
                                                etMobile.setText("");
                                                etMobile.requestFocus();
                                                etMobile.setEnabled(true);
                                            } else if (!status.equalsIgnoreCase("") && status.equalsIgnoreCase("4")) {
                                                etEmail.setText("");
                                                etMobile.setText("");
                                                etEmail.requestFocus();
                                                etEmail.setEnabled(true);
                                                etMobile.setEnabled(true);
                                            }
                                        }
                                    });
                            customDialog.show();
                            return;
                        } else if (dataList.get(i).getRelationship().equalsIgnoreCase("Self")) {
                            selfCount++;
                        } else if (dataList.get(i).getRelationship().equalsIgnoreCase("Mother")) {
                            motherCount++;
                        } else if (dataList.get(i).getRelationship().equalsIgnoreCase("Father")) {
                            fatherCount++;
                        } else if (dataList.get(i).getRelationship().equalsIgnoreCase("spouse")) {
                            spouseCount++;
                        }
                    }
                    if (selfCount == 0) {
                        flag = false;
                        commonMsgDialog(getActivity(), getResources().getString(R.string.self_mandatory), false, "");
                        return;
                    } else if (selfCount > 1) {
                        flag = false;
                        commonMsgDialog(getActivity(), getResources().getString(R.string.self_error_message), false, "");
                        return;
                    } else if (motherCount > 1) {
                        flag = false;
                        commonMsgDialog(getActivity(), getResources().getString(R.string.mother_error_message), false, "");
                        return;
                    } else if (fatherCount > 1) {
                        flag = false;
                        commonMsgDialog(getActivity(), getResources().getString(R.string.father_error_message), false, "");
                        return;
                    } else if (spouseCount > 1) {
                        flag = false;
                        commonMsgDialog(getActivity(), getResources().getString(R.string.spouse_error_message), false, "");
                        return;
                    }
                    flag = true;
                }
                if (flag) {
                    updateFamilyDetailsRecersiveCall(dataList, updateFamilyDetailsCount, dailog_custom_progress, dialog, resendCd);
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ivEditFamilyDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < dataList.size(); i++) {
                    dataList.get(i).setEnable(true);
                    declarationAdapter.notifyDataSetChanged();
                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private void getState(final String correState, final String permState, final String correCity, final String permCity) {
        Map inputMap = new HashMap<>();
        inputMap.put("", "");

        String flag = BINDSTATE;
        String category = SIGNUP;
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            arrState = new ArrayList<>();
                            if (statusCode == 200) {
                                if (response != null) {
                                    String stateCode, stateName;
                                    // arrState.clear();
                                    arrState.add(new RowData(0, "-1", "Select"));
                                    try {
                                        JSONArray jsonArray = response.getJSONArray(STATE_DETAILS);
                                        if (jsonArray.length() != 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject object = jsonArray.getJSONObject(i);
                                                stateCode = object.getString("STATE_CODE");
                                                stateName = object.getString("STATE_NAME");
                                                arrState.add(new RowData(i + 1, stateCode, stateName));
                                            }
                                        }
                                        arrAdapState = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrState);
                                        arrAdapState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCorresState.setAdapter(arrAdapState);
                                        spCorresState.setOnItemSelectedListener(corresStateListener);
                                        spPermState.setAdapter(arrAdapState);
                                        spPermState.setOnItemSelectedListener(permStateListener);
                                        RowData corresStateData = getSpinnerRowDataByValue(correState, arrAdapState);
                                        if (corresStateData != null) {
                                            spCorresState.setSelection(corresStateData.getPosition());
                                            getCorresCity(((RowData) spCorresState.getSelectedItem()).getKey(), correCity);
                                        } else {
                                            if (arrAdapCorresCity == null) {
                                                arrCorresCity = new ArrayList<>();
                                                arrAdapCorresCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresCity);
                                                arrAdapCorresCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spCorresCity.setAdapter(arrAdapCorresCity);
                                            }
                                            arrAdapCorresCity.add(new RowData(0, "-1", "Select"));
                                            arrAdapCorresCity.notifyDataSetChanged();
                                            cityFoundFlag = true;
                                        }
                                        RowData permanentStateData = getSpinnerRowDataByValue(permState, arrAdapState);
                                        if (permanentStateData != null) {
                                            spPermState.setSelection(permanentStateData.getPosition());
                                            getPermanentCity(((RowData) spPermState.getSelectedItem()).getKey(), permCity);
                                        } else {
                                            if (arrAdapPermCity == null) {
                                                arrPermCity = new ArrayList<>();
                                                arrAdapPermCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrPermCity);
                                                arrAdapPermCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spPermCity.setAdapter(arrAdapPermCity);
                                            }
                                            arrAdapPermCity.add(new RowData(0, "-1", "Select"));
                                            arrAdapPermCity.notifyDataSetChanged();
                                            cityFoundFlag = true;
                                        }
                                        customProgress.setVisibility(View.GONE);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                                        alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                    }

                                } else {
                                    customProgress.setVisibility(View.GONE);
                                    MOSLCommon.showToastMsg(mContext, "No state");
                                }
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
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                                throwable) {
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                                errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray
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

    private void getCorresCity(String stateCodeStr, final String city) {
        if (viewPager == null || viewPager.getCurrentItem() != getPersonalTab()) {
            return;
        }
        Map inputMap = new HashMap<>();
        inputMap.put("_stateid", stateCodeStr);

        String input = "";
        String flag = BINDALLCITY;
        String category = SIGNUP;

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            arrCorresCity = new ArrayList<>();
                            if (statusCode == 200) {
                                if (response.length() != 0 || response != null) {
                                    String cityCode, cityName;
                                    try {
                                        JSONArray jsonArray = response.getJSONArray(CITY_DETAILS);
                                        arrCorresCity.add(new RowData(0, "-1", "Select"));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            cityCode = object.getString("CITY_CODE");
                                            cityName = object.getString("CITY_NAME");
                                            arrCorresCity.add(new RowData((i + 1), cityCode, cityName));

                                        }
                                        arrAdapCorresCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresCity);
                                        arrAdapCorresCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCorresCity.setAdapter(arrAdapCorresCity);
                                        spCorresCity.setOnItemSelectedListener(corresCityListener);
                                        cityFoundFlag = true;
                                        RowData corresCityData = getSpinnerRowDataByValue(city, arrAdapCorresCity);
                                        if (corresCityData != null) {
                                            spCorresCity.setSelection(corresCityData.getPosition());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                                        alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                    }
                                    customProgress.setVisibility(View.GONE);
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                    MOSLCommon.showToastMsg(mContext, "NO CITY");
                                    cityFoundFlag = false;
                                }
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
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                                throwable) {
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                                errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray
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

    private void getPermanentCity(String stateCodeStr, final String city) {
        if (viewPager == null || viewPager.getCurrentItem() != getPersonalTab()) {
            return;
        }
        Map inputMap = new HashMap<>();
        inputMap.put("_stateid", stateCodeStr);

        String input = "";
        String flag = BINDALLCITY;
        String category = SIGNUP;

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            arrPermCity = new ArrayList<>();
                            if (statusCode == 200) {
                                if (response.length() != 0 || response != null) {
                                    String cityCode, cityName;
                                    try {
                                        JSONArray jsonArray = response.getJSONArray(CITY_DETAILS);
                                        arrPermCity.add(new RowData(0, "-1", "Select"));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            cityCode = object.getString("CITY_CODE");
                                            cityName = object.getString("CITY_NAME");
                                            arrPermCity.add(new RowData((i + 1), cityCode, cityName));

                                        }
                                        arrAdapPermCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrPermCity);
                                        arrAdapPermCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spPermCity.setAdapter(arrAdapPermCity);
                                        spPermCity.setOnItemSelectedListener(permCityListener);
                                        cityFoundFlag = true;
                                        RowData permanentCityData = getSpinnerRowDataByValue(city, arrAdapPermCity);
                                        if (permanentCityData != null) {
                                            spPermCity.setSelection(permanentCityData.getPosition());
                                        }

                                        if (cbSameAsCorrespondence.isChecked() && spCorresCity.getSelectedItem() != null) {
                                            int corrSelectedCityPos = ((RowData) spCorresCity.getSelectedItem()).getPosition();
                                            spPermCity.setSelection(corrSelectedCityPos);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                                        alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                    }
                                    customProgress.setVisibility(View.GONE);
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                    MOSLCommon.showToastMsg(mContext, "NO CITY");
                                    cityFoundFlag = false;
                                }
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
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                                throwable) {
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                                errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray
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

    public void checkMobileEmail(final String key, final String value) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
            if (key.equalsIgnoreCase("Mobile")) {
                inputMap.put("_parameterType", "Mobile");
                inputMap.put("_parameterValue", value);
            } else if (key.equalsIgnoreCase("Email")) {
                inputMap.put("_parameterType", "Email");
                inputMap.put("_parameterValue", value);

            }
            String flag = "CHECKMOBILEEMAIL";
            String category = PERSONAL;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("Personal");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String panNumber = (object.isNull("PAN_NUMBER") || object.getString("PAN_NUMBER").equals("")) ? "" : object.getString("PAN_NUMBER");
                                    String val = (object.isNull("VAL") || object.getString("VAL").equals("")) ? "" : object.getString("VAL");
                                    String message = (object.isNull("Message") || object.getString("Message").equals("")) ? "" : object.getString("Message");
                                    if (key.equalsIgnoreCase("Email")) {
                                        //  etEmail.setText("");
                                    } else if (key.equalsIgnoreCase("Mobile")) {
                                        //etMobile.setText("");
                                    }
                                    submitDialog(val, message);
                                }
                            } else {
                                if (key.equalsIgnoreCase("Mobile")) {
                                    etMobile.setText(value);
                                    etMobile.requestFocus();
                                    mobile = false;
                                } else if (key.equalsIgnoreCase("Email")) {
                                    etEmail.setText(value);
                                    etEmail.requestFocus();
                                    etElectronicNote.setText(value);
                                    email = false;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                            alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                        }
                        customProgress.setVisibility(View.GONE);
                    } else {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
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
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
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

    private void getInterestedIn() {
        Map inputMap = new HashMap<>();
        inputMap.put(APPLICATION_TYPE, getAppType());
        inputMap.put(COMMAND_TEXT, FILL_INTERESTEDIN_REGISTRATION);
        inputMap.put(APP_LOGIN_ID, getAppLoginID());

        String flag = BINDINTERESTEDIN;
        String category = SIGNUP;

        if (MOSLCommon.isNetworkEnabled(mContext)) {
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
//                            customProgress.setVisibility(View.VISIBLE);
                            arrInterested = new ArrayList<>();

                            if (statusCode == 200) {
                                if (response.length() != 0) {
                                    String interestedID, interestedText;

                                    try {
                                        JSONArray jsonArray = response.getJSONArray(INTERESTED_IN_DETAILS);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            interestedID = object.getString("ID");
                                            interestedText = object.getString("TEXT");
                                            arrInterested.add(interestedID);
                                        }
                                        showSegmentVisibility(arrInterested);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        customProgress.setVisibility(View.GONE);
                                        commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                                        MOSLCommon.alertDialog(RegisterActivity.this, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                    }
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                    MOSLCommon.showToastMsg(mContext, "NO Interested fields");

                                }
                            }
                            hideProgress();

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
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                                throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                                errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray
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

    private void showSegmentVisibility(List<String> productList) {

        if (productList != null && productList.size() != 0) {
            for (String product : productList) {

                if (getInterestedInMF()) {
                    showSegmentSelectionSection(false);
                } else if (isMfSegmentExist()) {
                    showSegmentSelectionSection(true);
                    cbMF.setVisibility(View.VISIBLE);
                    if (product.equalsIgnoreCase(EQUITY)) {

                        cbEquity.setVisibility(View.VISIBLE);
                        cbCurrency.setVisibility(View.VISIBLE);
                        cbDerivative.setVisibility(View.VISIBLE);
                    }
                    /*else {
                        cbEquity.setVisibility(View.GONE);
                        cbCurrency.setVisibility(View.GONE);
                        cbDerivative.setVisibility(View.GONE);
                    }*/
                    if (product.equalsIgnoreCase(COMMODITY)) {
                        cbCommodity.setVisibility(View.VISIBLE);
                    }
                    /*else {
                        cbCommodity.setVisibility(View.GONE);
                    }*/
                    if (product.equalsIgnoreCase(ALL)) {
                        cbAll.setVisibility(View.VISIBLE);
                    }
                    /*else {
                        cbAll.setVisibility(View.GONE);
                    }*/
                } else {

                    showSegmentSelectionSection(true);
                    if (product.equalsIgnoreCase(EQUITY)) {

                        cbEquity.setVisibility(View.VISIBLE);
                        cbCurrency.setVisibility(View.VISIBLE);
                        cbDerivative.setVisibility(View.VISIBLE);
                    }
                    /*else {
                        cbEquity.setVisibility(View.GONE);
                        cbCurrency.setVisibility(View.GONE);
                        cbDerivative.setVisibility(View.GONE);
                    }*/
                    if (product.equalsIgnoreCase(COMMODITY)) {
                        cbCommodity.setVisibility(View.VISIBLE);
                    }
                    /*else {
                        cbCommodity.setVisibility(View.GONE);
                    }*/
                    if (product.equalsIgnoreCase(ALL)) {
                        cbAll.setVisibility(View.VISIBLE);
                    }
                    /*else {
                        cbAll.setVisibility(View.GONE);
                    }*/
                }

            }
            getPersonalDetails();
        } else {
            commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
//            tvMessage.setText(ALL_SEGMENT_COMPLETED);
        }

    }

    public void getPersonalDetails() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            final Map inputMap = new HashMap<>();
            inputMap.put("_userid", getUserID());

            String flag = "";
            if (getInterestedInMF()) {

                flag = GETPERSONALDETAILS_MF;
            } else {
                flag = GETPERSONALDETAILS;
            }
            String category = PERSONAL;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            getFatcaAnswers();
                            if (response.has("Personal")) {
                                JSONArray jsonArray = response.getJSONArray("Personal");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                    /*{
                                        "RESIDENTIAL_STATUS": "Resident Individual",
                                            "RESIDENTIAL_SUB_STATUS": "Individual Resident",
                                            "TITLE": "Mr.",
                                            "FNAME": "Vinod",
                                            "MNAME": "Ramraj",
                                            "LNAME": "Yadav",
                                            "GENDER": "Male",
                                            "RELATION": "Father",
                                            "MARITAL_STATUS": "Single",
                                            "DATE_OF_BIRTH": "1987-08-14T00:00:00",
                                            "COUNTRY_OF_BIRTH": "India",
                                            "PLACE_OF_BIRTH": "Mumbai",
                                            "NATIONALITY": "Indian",
                                            "OTHER_RESIDENCE": false,
                                            "FHFNAME": "Ramraj",
                                            "FHMNAME": "Banshi",
                                            "FHLNAME": "Yadav",
                                            "MOTHER_NAME": "Lalmani",
                                            "MAIDEN_NAME": "NULL",
                                            "PANNO": "AMZPM3807G",
                                            "NAME_PAN": "PIYUSH PRAMESH MATHUR",
                                            "UID": 738006160413,
                                            "CORRES_ADD1": "Mhada Complex",
                                            "CORRES_ADD2": "Room no.10, Kaludin Chawl",
                                            "CORRES_ADD3": "Behram Baug, Veera Desai Road, MG Vidyalaya, Jogeshwari (West),",
                                            "CORRES_ADD_PROOF": "Aadhaar",
                                            "CORRES_CITY": "Mumbai",
                                            "CORRES_PIN_CODE": "400102",
                                            "CORRES_STATE": "Maharashtra",
                                            "CORRES_COUNTRY": "India",
                                            "CORRES_MOBILE_NO": "9619059221",
                                            "CORRES_EMAIL_ID": "vinod.yadav@motilaloswal.com",
                                            "PERMANENT_ADD1": "Mhada Complex",
                                            "PERMANENT_ADD2": "Room no.10, Kaludin Chawl",
                                            "PERMANENT_ADD3": "Behram Baug, Veera Desai Road, MG Vidyalaya, Jogeshwari (West),",
                                            "PERMANENT_CITY": "Mumbai",
                                            "PERMANENT_PIN_CODE": "400102",
                                            "PERMANENT_STATE": "Maharashtra",
                                            "PERMANENT_COUNTRY": "India",
                                            "PERMANENT_ADD_PROOF": "Aadhaar",
                                            "SAME_AS_CORRES_PERM": true,
                                            "CORRES_TEL_RESIDENT": "022~30784732",
                                            "FACEBOOKID": "null",
                                            "TWITTERID": "null",
                                            "LINKEDINID": "null",
                                            "OCCUPATION_DETAILS": "Business",
                                            "EDUCATION_QUALIFICATION": "Graduate",
                                            "ANNUAL_INCOME": "1-5",
                                            "NETWORTH": 60000,
                                            "ASONDATE": "2016-04-03T00:00:00",
                                            "STOCK_PORTFOLIO": "<5",
                                            "POLITICALLY_EXPOSED_PERSON": "No",
                                            "RELATED_POLITICALLY_EXPOSED_PERSON": "No",
                                            "EQSTOCKEXP": 2,
                                            "COMSTOCEXP": 3,
                                            "COMOTHEREXP": 0,
                                            "EQINTRADAY": true,
                                            "EQPOSITIONAL": false,
                                            "EQINVESTMENT": false,
                                            "DEVTRADING": false,
                                            "DEVHEDGING": false,
                                            "COMINVESTMENT": true,
                                            "COMTRADING": false,
                                            "COMHEDGING": false,
                                            "COMOTHER": "0",
                                            "CONTRACT_NOTE": "Electronic Contract Note (ECN)",
                                            "BROKER_NAME": "Vinod",
                                            "BROKER_CLIENT_CODE": "EAOPE0005",
                                            "EQPASTACTION": "",
                                            "COMPASTACTION": "null"
                                    }*/
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String residentialStatus = (object.isNull("RESIDENTIAL_STATUS") || object.getString("RESIDENTIAL_STATUS").equals(""))
                                                ? "Resident Individual" : object.getString("RESIDENTIAL_STATUS");
                                        String residentialSubStatus = (object.isNull("RESIDENTIAL_SUB_STATUS") || object.getString("RESIDENTIAL_SUB_STATUS").equals(""))
                                                ? "Individual Resident" : object.getString("RESIDENTIAL_SUB_STATUS");
                                        title = (object.isNull("TITLE") || object.getString("TITLE").equals(""))
                                                ? "" : object.getString("TITLE");
                                        String fName = (object.isNull("FNAME") || object.getString("FNAME").equals(""))
                                                ? "" : object.getString("FNAME");
                                        String mName = (object.isNull("MNAME") || object.getString("MNAME").equals(""))
                                                ? "" : object.getString("MNAME");
                                        String lName = (object.isNull("LNAME") || object.getString("LNAME").equals(""))
                                                ? "" : object.getString("LNAME");
                                        String gender = (object.isNull("GENDER") || object.getString("GENDER").equals(""))
                                                ? "" : object.getString("GENDER");
                                        relation = (object.isNull("RELATION") || object.getString("RELATION").equals(""))
                                                ? "" : object.getString("RELATION");
                                        String maritalStatus = (object.isNull("MARITAL_STATUS") || object.getString("MARITAL_STATUS").equals(""))
                                                ? "" : object.getString("MARITAL_STATUS");
                                        String dob = (object.isNull("DATE_OF_BIRTH") || object.getString("DATE_OF_BIRTH").equals(""))
                                                ? "" : object.getString("DATE_OF_BIRTH");
                                        String countryOfBirth = (object.isNull("COUNTRY_OF_BIRTH") || object.getString("COUNTRY_OF_BIRTH").equals(""))
                                                ? "" : object.getString("COUNTRY_OF_BIRTH");
                                        String placeOfBirth = (object.isNull("PLACE_OF_BIRTH") || object.getString("PLACE_OF_BIRTH").equals(""))
                                                ? "" : object.getString("PLACE_OF_BIRTH");
                                        String nationality = (object.isNull("NATIONALITY") || object.getString("NATIONALITY").equals(""))
                                                ? "" : object.getString("NATIONALITY");
                                        String otherResidence = (object.isNull("OTHER_RESIDENCE") || object.getString("OTHER_RESIDENCE").equals(""))
                                                ? "" : object.getString("OTHER_RESIDENCE");
                                        fhTitle = (object.isNull("FHTITLE") || object.getString("FHTITLE").equals(""))
                                                ? "" : object.getString("FHTITLE");
                                        String fhfName = (object.isNull("FHFNAME") || object.getString("FHFNAME").equals(""))
                                                ? "" : object.getString("FHFNAME");
                                        String fhmName = (object.isNull("FHMNAME") || object.getString("FHMNAME").equals(""))
                                                ? "" : object.getString("FHMNAME");
                                        String fhlName = (object.isNull("FHLNAME") || object.getString("FHLNAME").equals(""))
                                                ? "" : object.getString("FHLNAME");
                                        /*String motherName = (object.isNull("MOTHER_NAME") || object.getString("MOTHER_NAME").equals(""))
                                                ? "" : object.getString("MOTHER_NAME");*/
                                        motherTitle = (object.isNull("MOTHER_TITLE") || object.getString("MOTHER_TITLE").equals(""))
                                                ? "" : object.getString("MOTHER_TITLE");
                                        String motherFname = (object.isNull("MOTHER_FNAME") || object.getString("MOTHER_FNAME").equals(""))
                                                ? "" : object.getString("MOTHER_FNAME");
                                        String motherMname = (object.isNull("MOTHER_MNAME") || object.getString("MOTHER_MNAME").equals(""))
                                                ? "" : object.getString("MOTHER_MNAME");
                                        String motherLname = (object.isNull("MOTHER_LNAME") || object.getString("MOTHER_LNAME").equals(""))
                                                ? "" : object.getString("MOTHER_LNAME");
                                        /*String maidenName = (object.isNull("MAIDEN_NAME") || object.getString("MAIDEN_NAME").equals(""))
                                                ? "" : object.getString("MAIDEN_NAME");*/
                                        maidenTitle = (object.isNull("MAIDEN_TITLE") || object.getString("MAIDEN_TITLE").equals(""))
                                                ? "" : object.getString("MAIDEN_TITLE");
                                        String maidenFname = (object.isNull("MAIDEN_FNAME") || object.getString("MAIDEN_FNAME").equals(""))
                                                ? "" : object.getString("MAIDEN_FNAME");
                                        String maidenMname = (object.isNull("MAIDEN_MNAME") || object.getString("MAIDEN_MNAME").equals(""))
                                                ? "" : object.getString("MAIDEN_MNAME");
                                        String maidenLname = (object.isNull("MAIDEN_LNAME") || object.getString("MAIDEN_LNAME").equals(""))
                                                ? "" : object.getString("MAIDEN_LNAME");
                                        String panNo = (object.isNull("PANNO") || object.getString("PANNO").equals(""))
                                                ? "" : object.getString("PANNO");
                                        String namePan = (object.isNull("NAME_PAN") || object.getString("NAME_PAN").equals(""))
                                                ? "" : object.getString("NAME_PAN");
                                        String uid = (object.isNull("UID") || object.getString("UID").equals(""))
                                                ? "" : object.getString("UID");
                                        String corresAdd1 = (object.isNull("CORRES_ADD1") || object.getString("CORRES_ADD1").equals(""))
                                                ? "" : object.getString("CORRES_ADD1");
                                        String corresAdd2 = (object.isNull("CORRES_ADD2") || object.getString("CORRES_ADD2").equals(""))
                                                ? "" : object.getString("CORRES_ADD2");
                                        String corresAdd3 = (object.isNull("CORRES_ADD3") || object.getString("CORRES_ADD3").equals(""))
                                                ? "" : object.getString("CORRES_ADD3");
                                        corresAddProof = (object.isNull("CORRES_ADD_PROOF") || object.getString("CORRES_ADD_PROOF").equals(""))
                                                ? "" : object.getString("CORRES_ADD_PROOF");
                                        String corresCity = (object.isNull("CORRES_CITY") || object.getString("CORRES_CITY").equals(""))
                                                ? "" : object.getString("CORRES_CITY");
                                        String corresPinCode = (object.isNull("CORRES_PIN_CODE") || object.getString("CORRES_PIN_CODE").equals(""))
                                                ? "" : object.getString("CORRES_PIN_CODE");
                                        String corresState = (object.isNull("CORRES_STATE") || object.getString("CORRES_STATE").equals(""))
                                                ? "" : object.getString("CORRES_STATE");
                                        String corresCountry = (object.isNull("CORRES_COUNTRY") || object.getString("CORRES_COUNTRY").equals(""))
                                                ? "" : object.getString("CORRES_COUNTRY");
                                        String corresMobileNo = (object.isNull("CORRES_MOBILE_NO") || object.getString("CORRES_MOBILE_NO").equals(""))
                                                ? "" : object.getString("CORRES_MOBILE_NO");
                                        String corresEmailId = (object.isNull("CORRES_EMAIL_ID") || object.getString("CORRES_EMAIL_ID").equals(""))
                                                ? "" : object.getString("CORRES_EMAIL_ID");
                                        String permanentAdd1 = (object.isNull("PERMANENT_ADD1") || object.getString("PERMANENT_ADD1").equals(""))
                                                ? "" : object.getString("PERMANENT_ADD1");
                                        String permanentAdd2 = (object.isNull("PERMANENT_ADD2") || object.getString("PERMANENT_ADD2").equals(""))
                                                ? "" : object.getString("PERMANENT_ADD2");
                                        String permanentAdd3 = (object.isNull("PERMANENT_ADD3") || object.getString("PERMANENT_ADD3").equals(""))
                                                ? "" : object.getString("PERMANENT_ADD3");
                                        String permanentCity = (object.isNull("PERMANENT_CITY") || object.getString("PERMANENT_CITY").equals(""))
                                                ? "" : object.getString("PERMANENT_CITY");
                                        String permanentPinCode = (object.isNull("PERMANENT_PIN_CODE") || object.getString("PERMANENT_PIN_CODE").equals(""))
                                                ? "" : object.getString("PERMANENT_PIN_CODE");
                                        String permanentState = (object.isNull("PERMANENT_STATE") || object.getString("PERMANENT_STATE").equals(""))
                                                ? "" : object.getString("PERMANENT_STATE");
                                        String permanentCountry = (object.isNull("PERMANENT_COUNTRY") || object.getString("PERMANENT_COUNTRY").equals(""))
                                                ? "" : object.getString("PERMANENT_COUNTRY");
                                        permanentAddProof = (object.isNull("PERMANENT_ADD_PROOF") || object.getString("PERMANENT_ADD_PROOF").equals(""))
                                                ? "" : object.getString("PERMANENT_ADD_PROOF");
                                        String sameAsCorresPerm = (object.isNull("SAME_AS_CORRES_PERM") || object.getString("SAME_AS_CORRES_PERM").equals(""))
                                                ? "" : object.getString("SAME_AS_CORRES_PERM");
                                        String corresTelResident = (object.isNull("CORRES_TEL_RESIDENT") || object.getString("CORRES_TEL_RESIDENT").equals(""))
                                                ? "" : object.getString("CORRES_TEL_RESIDENT");
                                        String facebookId = (object.isNull("FACEBOOKID") || object.getString("FACEBOOKID").equals(""))
                                                ? "" : object.getString("FACEBOOKID");
                                        String twitterId = (object.isNull("TWITTERID") || object.getString("TWITTERID").equals(""))
                                                ? "" : object.getString("TWITTERID");
                                        String linkedinId = (object.isNull("LINKEDINID") || object.getString("LINKEDINID").equals(""))
                                                ? "" : object.getString("LINKEDINID");
                                        occupationDetails = (object.isNull("OCCUPATION_DETAILS") || object.getString("OCCUPATION_DETAILS").equals(""))
                                                ? "" : object.getString("OCCUPATION_DETAILS");
                                        educationQualification = (object.isNull("EDUCATION_QUALIFICATION") || object.getString("EDUCATION_QUALIFICATION").equals(""))
                                                ? "" : object.getString("EDUCATION_QUALIFICATION");
                                        annualIncome = (object.isNull("ANNUAL_INCOME") || object.getString("ANNUAL_INCOME").equals(""))
                                                ? "" : object.getString("ANNUAL_INCOME");
                                        String networth = (object.isNull("NETWORTH") || object.getString("NETWORTH").equals(""))
                                                ? "" : object.getString("NETWORTH");
                                        String asOnDate = (object.isNull("ASONDATE") || object.getString("ASONDATE").equals(""))
                                                ? "" : object.getString("ASONDATE");
                                        stockPortfolio = (object.isNull("STOCK_PORTFOLIO") || object.getString("STOCK_PORTFOLIO").equals(""))
                                                ? "" : object.getString("STOCK_PORTFOLIO");
                                        politicallyExposedPerson = (object.isNull("POLITICALLY_EXPOSED_PERSON") || object.getString("POLITICALLY_EXPOSED_PERSON").equals(""))
                                                ? "" : object.getString("POLITICALLY_EXPOSED_PERSON");
                                        relatedPoliticallyExposedPerson = (object.isNull("RELATED_POLITICALLY_EXPOSED_PERSON") || object.getString("RELATED_POLITICALLY_EXPOSED_PERSON").equals(""))
                                                ? "" : object.getString("RELATED_POLITICALLY_EXPOSED_PERSON");
                                        eqStockExp = (object.isNull("EQSTOCKEXP") || object.getString("EQSTOCKEXP").equals(""))
                                                ? "" : object.getString("EQSTOCKEXP");
                                        derivativeExp = (object.isNull("DERIVATIVEEXP") || object.getString("DERIVATIVEEXP").equals(""))
                                                ? "" : object.getString("DERIVATIVEEXP");
                                        comStocExp = (object.isNull("COMSTOCEXP") || object.getString("COMSTOCEXP").equals(""))
                                                ? "" : object.getString("COMSTOCEXP");
                                        comOtherExp = (object.isNull("COMOTHEREXP") || object.getString("COMOTHEREXP").equals(""))
                                                ? "" : object.getString("COMOTHEREXP");
                                        eqIntraDay = (object.isNull("EQINTRADAY") || object.getString("EQINTRADAY").equals(""))
                                                ? "" : object.getString("EQINTRADAY");
                                        eqPositional = (object.isNull("EQPOSITIONAL") || object.getString("EQPOSITIONAL").equals(""))
                                                ? "" : object.getString("EQPOSITIONAL");
                                        eqInvestment = (object.isNull("EQINVESTMENT") || object.getString("EQINVESTMENT").equals(""))
                                                ? "" : object.getString("EQINVESTMENT");
                                        devTrading = (object.isNull("DEVTRADING") || object.getString("DEVTRADING").equals(""))
                                                ? "" : object.getString("DEVTRADING");
                                        devHedging = (object.isNull("DEVHEDGING") || object.getString("DEVHEDGING").equals(""))
                                                ? "" : object.getString("DEVHEDGING");
                                        comInvestment = (object.isNull("COMINVESTMENT") || object.getString("COMINVESTMENT").equals(""))
                                                ? "" : object.getString("COMINVESTMENT");
                                        comTrading = (object.isNull("COMTRADING") || object.getString("COMTRADING").equals(""))
                                                ? "" : object.getString("COMTRADING");
                                        comHedging = (object.isNull("COMHEDGING") || object.getString("COMHEDGING").equals(""))
                                                ? "" : object.getString("COMHEDGING");
                                        comOther = (object.isNull("COMOTHER") || object.getString("COMOTHER").equals(""))
                                                ? "" : object.getString("COMOTHER");
                                        String contractNote = (object.isNull("CONTRACT_NOTE") || object.getString("CONTRACT_NOTE").equals(""))
                                                ? "" : object.getString("CONTRACT_NOTE");
                                        brokerName = (object.isNull("BROKER_NAME") || object.getString("BROKER_NAME").equals(""))
                                                ? "" : object.getString("BROKER_NAME");
                                        String brokerClientCode = (object.isNull("BROKER_CLIENT_CODE") || object.getString("BROKER_CLIENT_CODE").equals(""))
                                                ? "" : object.getString("BROKER_CLIENT_CODE");
                                        eqPastAction = (object.isNull("EQPASTACTION") || object.getString("EQPASTACTION").equals(""))
                                                ? "" : object.getString("EQPASTACTION");
                                        comPastAction = (object.isNull("COMPASTACTION") || object.getString("COMPASTACTION").equals(""))
                                                ? "" : object.getString("COMPASTACTION");
                                        String altMobNo = (object.isNull("CORRES_ALTMOBILE_NO") || object.getString("CORRES_ALTMOBILE_NO").equals(""))
                                                ? "" : object.getString("CORRES_ALTMOBILE_NO");
                                        String altEmailNo = (object.isNull("CORRES_ALTEmailID") || object.getString("CORRES_ALTEmailID").equals(""))
                                                ? "" : object.getString("CORRES_ALTEmailID");
                                        pepRemark = (object.isNull("PEPREMARK") || object.getString("PEPREMARK").equals(""))
                                                ? "" : object.getString("PEPREMARK");
                                        String familyDeclaration = (object.isNull("FAMILY_DECLARATION") || object.getString("FAMILY_DECLARATION").equals(""))
                                                ? "" : object.getString("FAMILY_DECLARATION");
                                        String eqSchemeId = (object.isNull("EQSCHEME_ID") || object.getString("EQSCHEME_ID").equals(""))
                                                ? "" : object.getString("EQSCHEME_ID");
                                        String commercial = (object.isNull("Commercial") || object.getString("Commercial").equals("")) ? "" : object.getString("Commercial");
                                        String commercial_Bullion = (object.isNull("Commercial_Bullion") || object.getString("Commercial_Bullion").equals(""))
                                                ? "" : object.getString("Commercial_Bullion");
                                        String commercial_Basemetals = (object.isNull("Commercial_Basemetals") || object.getString("Commercial_Basemetals").equals(""))
                                                ? "" : object.getString("Commercial_Basemetals");
                                        String commercial_Energy = (object.isNull("Commercial_Energy") || object.getString("Commercial_Energy").equals(""))
                                                ? "" : object.getString("Commercial_Energy");
                                        String commercial_AgriCommodities = (object.isNull("Commercial_AgriCommodities") || object.getString("Commercial_AgriCommodities").equals(""))
                                                ? "" : object.getString("Commercial_AgriCommodities");
                                        String nonCommercial = (object.isNull("NONCommercial") || object.getString("NONCommercial").equals(""))
                                                ? "" : object.getString("NONCommercial");
                                        String nonCommercial_Bullion = (object.isNull("NONCommercial_Bullion") || object.getString("NONCommercial_Bullion").equals(""))
                                                ? "" : object.getString("NONCommercial_Bullion");
                                        String nonCommercial_Basemetals = (object.isNull("NONCommercial_Basemetals") || object.getString("NONCommercial_Basemetals").equals(""))
                                                ? "" : object.getString("NONCommercial_Basemetals");
                                        String nonCommercial_Energy = (object.isNull("NONCommercial_Energy") || object.getString("NONCommercial_Energy").equals(""))
                                                ? "" : object.getString("NONCommercial_Energy");
                                        String nonCommercial_AgriCommodities = (object.isNull("NONCommercial_AgriCommodities") || object.getString("NONCommercial_AgriCommodities").equals(""))
                                                ? "" : object.getString("NONCommercial_AgriCommodities");
                                        String gstLocation = (object.isNull("GSTLocation") || object.getString("GSTLocation").equals("")) ? "" : object.getString("GSTLocation");
                                        String gstNumber = (object.isNull("GSTNumber") || object.getString("GSTNumber").equals("")) ? "" : object.getString("GSTNumber");
                                        String isAadhaarByPass =
                                            (object.isNull("ISAADHAARBYPASS") || object.getString(
                                                "ISAADHAARBYPASS").equals("")) ? ""
                                                : object.getString("ISAADHAARBYPASS").trim();
                                        String onlineOffline =
                                            (object.isNull("ONLINEOFFLINE") || object.getString(
                                                "ONLINEOFFLINE").equals("")) ? ""
                                                : object.getString("ONLINEOFFLINE").trim();

                                        aadhaarByPass = isAadhaarByPass.equals("1");
                                        setEqSchemeId(eqSchemeId);
                                        etGST.setText(gstNumber);
                                        if (!isBlank(familyDeclaration) && Integer.parseInt(familyDeclaration) == 1) { //if 1 then Family declaration selected
                                            showFamilyDetailLayout(true);
                                        } else {
                                            showFamilyDetailLayout(false);
                                        }

                                        if (getInterestedInMF()) {
                                            if (politicallyExposedPerson.equalsIgnoreCase("Yes")) {
                                                spMFPEPStatus.setSelection(1);
                                                etMFPEPRemark.setText(pepRemark);
                                            } else if (relatedPoliticallyExposedPerson.equalsIgnoreCase("Yes")) {
                                                spMFPEPStatus.setSelection(2);
                                                etMFPEPRemark.setText(pepRemark);
                                            } else {
                                                etMFPEPRemark.setVisibility(View.GONE);
                                                spMFPEPStatus.setSelection(0);
                                            }
                                        } else {
                                            if (politicallyExposedPerson.equalsIgnoreCase("Yes")) {
                                                togglePoliticaly.setChecked(true);
                                               /* cbPoliticalyYes.setChecked(true);
                                                cbPoliticalyNo.setChecked(false);*/
                                            } else {
                                                togglePoliticaly.setChecked(false);
                                                /*  cbPoliticalyNo.setChecked(true);
                                                cbPoliticalyYes.setChecked(false);*/
                                            }
                                            if (relatedPoliticallyExposedPerson.equalsIgnoreCase("Yes")) {
                                                toggleRelatedPolitical.setChecked(true);
                                            } else {
                                                toggleRelatedPolitical.setChecked(false);
                                            }

                                            if (eqIntraDay.equalsIgnoreCase("true")) {
                                                cbEqIntraday.setChecked(true);
                                            } else {
                                                cbEqIntraday.setChecked(false);
                                            }

                                            if (eqPositional.equalsIgnoreCase("true")) {
                                                cbEqPositional.setChecked(true);
                                            } else {
                                                cbEqPositional.setChecked(false);
                                            }

                                            if (eqInvestment.equalsIgnoreCase("true")) {
                                                cbEqInvestment.setChecked(true);
                                            } else {
                                                cbEqInvestment.setChecked(false);
                                            }

                                            if (devTrading.equalsIgnoreCase("true")) {
                                                cbDevTrading.setChecked(true);
                                            } else {
                                                cbDevTrading.setChecked(false);
                                            }

                                            if (devHedging.equalsIgnoreCase("true")) {
                                                cbDevHedging.setChecked(true);
                                            } else {
                                                cbDevHedging.setChecked(false);
                                            }

                                            if (comInvestment.equalsIgnoreCase("true")) {
                                                cbCommInvestment.setChecked(true);
                                            } else {
                                                cbCommInvestment.setChecked(false);
                                            }

                                            if (comHedging.equalsIgnoreCase("true")) {
                                                cbCommHedging.setChecked(true);
                                            } else {
                                                cbCommHedging.setChecked(false);
                                            }

                                            if (comTrading.equalsIgnoreCase("true")) {
                                                cbCommTrading.setChecked(true);
                                            } else {
                                                cbCommTrading.setChecked(false);
                                            }

                                            if (comOther.equalsIgnoreCase("")) {
                                                cbCommOther.setChecked(false);
                                                etCommOther.setVisibility(View.GONE);
                                            } else {
                                                cbCommOther.setChecked(true);
                                                etCommOther.setVisibility(View.VISIBLE);
                                                etCommOther.setText(comOther);
                                            }

                                            if (eqPastAction.equalsIgnoreCase("")) {
                                                toggleSEBIAuthority.setChecked(false);
                                                llSebiAuthority.setVisibility(View.GONE);
                                            } else {
                                                toggleSEBIAuthority.setChecked(true);
                                                llSebiAuthority.setVisibility(View.VISIBLE);
                                                etSebiAuthority.setText(eqPastAction);
                                            }

                                            if (comPastAction.equalsIgnoreCase("")) {
                                                toggleFmcSebiAuthority.setChecked(false);
                                                llFmcSebiAuthority.setVisibility(View.GONE);
                                            } else {
                                                toggleFmcSebiAuthority.setChecked(true);
                                                llFmcSebiAuthority.setVisibility(View.VISIBLE);
                                                etFmcSebiAuthority.setText(comPastAction);
                                            }
//                                            set values here for  check box commerial and noncommerial
                                            if (commercial.equalsIgnoreCase("1")) {
                                                cbCommercial.setChecked(true);
                                            } else {
                                                cbCommercial.setChecked(false);
                                            }
                                            if (commercial_Bullion.equalsIgnoreCase("1")) {
                                                cbCommercialBullion.setChecked(true);
                                            } else {
                                                cbCommercialBullion.setChecked(false);
                                            }
                                            if (commercial_Basemetals.equalsIgnoreCase("1")) {
                                                cbCommercialBaseMetals.setChecked(true);
                                            } else {
                                                cbCommercialBaseMetals.setChecked(false);
                                            }
                                            if (commercial_Energy.equalsIgnoreCase("1")) {
                                                cbCommercialEnergy.setChecked(true);
                                            } else {
                                                cbCommercialEnergy.setChecked(false);
                                            }
                                            if (commercial_AgriCommodities.equalsIgnoreCase("1")) {
                                                cbCommercialAgriCommodities.setChecked(true);
                                            } else {
                                                cbCommercialAgriCommodities.setChecked(false);
                                            }
                                            if (nonCommercial.equalsIgnoreCase("1")) {
                                                cbNonCommercial.setChecked(true);
                                            } else {
                                                cbNonCommercial.setChecked(false);
                                            }
                                            if (nonCommercial_Bullion.equalsIgnoreCase("1")) {
                                                cbNonCommercialBullion.setChecked(true);
                                            } else {
                                                cbNonCommercialBullion.setChecked(false);
                                            }
                                            if (nonCommercial_Basemetals.equalsIgnoreCase("1")) {
                                                cbNonCommercialBaseMetals.setChecked(true);
                                            } else {
                                                cbNonCommercialBaseMetals.setChecked(false);
                                            }
                                            if (nonCommercial_Energy.equalsIgnoreCase("1")) {
                                                cbNonCommercialEnergy.setChecked(true);
                                            } else {
                                                cbNonCommercialEnergy.setChecked(false);
                                            }
                                            if (nonCommercial_AgriCommodities.equalsIgnoreCase("1")) {
                                                cbNonCommercialAgriCommodities.setChecked(true);
                                            } else {
                                                cbNonCommercialAgriCommodities.setChecked(false);
                                            }
                                        }

                                        etAltTelNo.setText(altMobNo);
                                        etAltEmailID.setText(altEmailNo);
                                        etResidentStatus.setText(residentialStatus);
                                        etSubStatus.setText(residentialSubStatus);
                                        etFirstName.setText(fName);
                                        etMiddleName.setText(mName);
                                        etLastName.setText(lName);
                                        if (gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("M")) {
                                            rbMale.setChecked(true);
                                            rbFemale.setChecked(false);
                                        } else if (gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("F")) {
                                            rbFemale.setChecked(true);
                                            rbMale.setChecked(false);

                                        }

                                        if (!(maritalStatus.equalsIgnoreCase(""))) {
                                            if (maritalStatus.equalsIgnoreCase("Single")) {
                                                rbUnMarried.setChecked(true);
                                                rbMarried.setChecked(false);
                                            } else if (maritalStatus.equalsIgnoreCase("MARRIED")) {
                                                rbMarried.setChecked(true);
                                                rbUnMarried.setChecked(false);
                                            }
                                        }
                                        if ((gender.equalsIgnoreCase("Female") || gender.equalsIgnoreCase("F")) && maritalStatus.equalsIgnoreCase("MARRIED")) {
                                            llMaidenName.setVisibility(View.VISIBLE);
                                        } else {
                                            llMaidenName.setVisibility(View.GONE);
                                        }

                                        Date date = EkycAppUtil.getDate(dob, "dd-MM-yyyy");
                                        etDOB.setText(EkycAppUtil.getDate(date, "dd/MM/yyyy"));
                                        etBirthPlace.setText(placeOfBirth);

                                        etFatherSpouseFirstName.setText(fhfName);
                                        etFatherSpouseMidName.setText(fhmName);
                                        etFatherSpouseLastName.setText(fhlName);
                                        etMotherFirstName.setText(motherFname);
                                        etMotherMiddleName.setText(motherMname);
                                        etMotherLastName.setText(motherLname);
                                        etMaidenFirstName.setText(maidenFname);
                                        etMaidenMiddleName.setText(maidenMname);
                                        etMaidenLastName.setText(maidenLname);
                                        etPAN.setText(panNo);
                                        setAadharNo(uid);
                                        setPANno(panNo);
                                        setPANName(namePan);
                                        if (uid.equalsIgnoreCase("") || uid.equals("0") || uid.equals("0.0")) {
                                            etAadhar.setText("");
                                        } else {
//                                            BigDecimal bd = new BigDecimal(uid);
//                                            etAadhar.setText(String.valueOf(bd.longValue()));
                                            etAadhar.setText(uid);
                                        }
                                        if (!getInterestedInMF() && !getIsAadharVerified()) {
                                            if (isThisSecondProductFlow()) {
                                                if (isBlank(uid)) {
                                                    etAadhar.setEnabled(true);
                                                }
                                            } else {
                                                etAadhar.setEnabled(true);
                                            }
                                        } else if (aadhaarByPass) {
                                            etAadhar.setEnabled(true);
                                        }
                                        etNameOnPAN.setText(namePan);
                                        etBldgNameCorres.setText(corresAdd1);
                                        etFlatNoCorres.setText(corresAdd2);
                                        etAreaCorres.setText(corresAdd3);

//                                        etAddProofCorres.setText(corresAddProof);
                                        etPincodeCorres.setText(corresPinCode);
                                        if (isBlank(corresMobileNo)) {
                                            etMobile.setEnabled(true);
                                            tvMobile.setEnabled(false);
                                            if (getInterestedInMF() && getIsAadharVerified()) {
                                                tvMobile.setEnabled(true);
                                            }
                                        } else {
                                            if (getInterestedInMF() && getIsCVLVerified()) {
//                                                tvMobile.setEnabled(false);
                                                etAltTelNo.setEnabled(false);
                                                etAltEmailID.setEnabled(false);
                                            } else {
                                                tvMobile.setEnabled(true);
                                                etAltTelNo.setEnabled(true);
                                                etAltEmailID.setEnabled(true);
                                            }
                                            etMobile.setEnabled(false);
                                            etMobile.setText(corresMobileNo);
                                            mobile = false;
                                        }
                                        if (isBlank(corresEmailId)) {
                                            etEmail.setEnabled(true);
                                            tvEmail.setEnabled(false);
                                            if (getInterestedInMF() && getIsAadharVerified()) {
                                                tvEmail.setEnabled(true);
                                            }
                                        } else {
                                            if (getInterestedInMF() && getIsCVLVerified()) {
//                                                tvEmail.setEnabled(false);
                                                etAltTelNo.setEnabled(false);
                                                etAltEmailID.setEnabled(false);
                                            } else {
                                                tvEmail.setEnabled(true);
                                                etAltTelNo.setEnabled(true);
                                                etAltEmailID.setEnabled(true);
                                            }
                                            etEmail.setEnabled(false);
                                            etEmail.setText(corresEmailId);
                                            email = false;
                                        }

                                        if (!isBlank(corresTelResident)) {
                                            String[] parts = corresTelResident.split("~");
                                            if (parts.length > 1) {
                                                String std = parts[0];
                                                String tel = parts[1];
                                                etSTDNo.setText(std);
                                                etTelNo.setText(tel);
                                            } else {
                                                if (corresTelResident.equals("~")) {
                                                    etTelNo.setText("");
                                                } else {
                                                    etTelNo.setText(corresTelResident);
                                                }
                                            }
                                        } else {
                                            etSTDNo.setText("");
                                            etTelNo.setText("");
                                        }
//                                        etTelNo.setText(corresTelResident);

                                        if (sameAsCorresPerm.equalsIgnoreCase("True")) {
                                            cbSameAsCorrespondence.setChecked(true);
                                            if (isBlank(permanentAdd1)) {
                                                etBldgNamePerm.setEnabled(true);
                                            } else {
                                                etBldgNamePerm.setEnabled(false);
                                            }
                                            if (isBlank(permanentAdd2)) {
                                                etFlatNamePerm.setEnabled(true);
                                            } else {
                                                etFlatNamePerm.setEnabled(false);
                                            }
                                            if (isBlank(permanentAdd3)) {
                                                etAreaPerm.setEnabled(true);
                                            } else {
                                                etAreaPerm.setEnabled(false);
                                            }
                                            if (isBlank(permanentPinCode)) {
                                                etPincodePerm.setEnabled(true);
                                            } else {
                                                etPincodePerm.setEnabled(false);
                                            }
                                            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                                                if (isBlank(permanentCity)) {
                                                    etPermCity.setEnabled(true);
                                                } else {
                                                    etPermCity.setEnabled(false);
                                                }
                                                if (isBlank(permanentState)) {
                                                    etPermState.setEnabled(true);
                                                } else {
                                                    etPermState.setEnabled(false);
                                                }
                                            } else {
                                                if (isBlank(permanentCity)) {
                                                    spPermCity.setEnabled(true);
                                                } else {
                                                    spPermCity.setEnabled(false);
                                                }
                                                if (isBlank(permanentState)) {
                                                    spPermState.setEnabled(true);
                                                } else {
                                                    spPermState.setEnabled(false);
                                                }
                                            }
//                                            etAddProofPerm.setEnabled(false);
                                            if (isBlank(permanentAddProof)) {
                                                spPermAddProof.setEnabled(true);
                                            } else {
                                                spPermAddProof.setEnabled(false);
                                            }
                                            if (isBlank(permanentCountry)) {
                                                spPermCountry.setEnabled(true);
                                            } else {
                                                spPermCountry.setEnabled(false);
                                            }
                                        } else {
                                            cbSameAsCorrespondence.setChecked(false);
                                            etBldgNamePerm.setEnabled(true);
                                            etFlatNamePerm.setEnabled(true);
                                            etAreaPerm.setEnabled(true);
//                                            etAddProofPerm.setEnabled(true);
                                            spPermAddProof.setEnabled(true);
                                            etPincodePerm.setEnabled(true);
                                            spPermCountry.setEnabled(true);
                                            spPermCity.setEnabled(true);
                                            spPermState.setEnabled(true);
                                        }
                                        etBldgNamePerm.setText(permanentAdd1);
                                        etFlatNamePerm.setText(permanentAdd2);
                                        etAreaPerm.setText(permanentAdd3);
//                                        etAddProofPerm.setText(permanentAddProof);
                                        etPincodePerm.setText(permanentPinCode);
                                        etFbId.setText(facebookId);
                                        etTwitterId.setText(twitterId);
                                        etLinkedinID.setText(linkedinId);
                                        if (networth.equalsIgnoreCase("") || networth.equalsIgnoreCase("0") || networth.equalsIgnoreCase("0.0")) {
                                            etNetWorth.setText("");
                                        } else {
                                            BigDecimal bd = new BigDecimal(networth);
                                            etNetWorth.setText(String.valueOf(bd.longValue()));
                                        }
                                        Date dateNW = EkycAppUtil.getDate(asOnDate, "dd-MM-yyyy");
                                        etNWDate.setText(EkycAppUtil.getDate(dateNW, "dd/MM/yyyy"));
                                        etElectronicNote.setText(etEmail.getText());

                                        //not working
                                        if (brokerName.equalsIgnoreCase("") && brokerClientCode.equalsIgnoreCase("")) {
                                            toggleRegWithBroker.setChecked(false);
                                            /* cbRegWithBrokerYes.setChecked(false);
                                            cbRegWithBrokerNo.setChecked(true);*/
                                            llBrokerInfo.setVisibility(View.GONE);

                                        } else {
                                            toggleRegWithBroker.setChecked(true);
                                            /*cbRegWithBrokerYes.setChecked(true);
                                            cbRegWithBrokerNo.setChecked(false);*/
                                            llBrokerInfo.setVisibility(View.VISIBLE);
                                            etBrokerName.setText(brokerName);
                                            etClientCode.setText(brokerClientCode);
                                        }

                                        //Spinner Data
                                        if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                                            spCorresState.setVisibility(View.GONE);
                                            spCorresCity.setVisibility(View.GONE);
                                            spPermState.setVisibility(View.GONE);
                                            spPermCity.setVisibility(View.GONE);
                                            etCorresState.setVisibility(View.VISIBLE);
                                            etCorresCity.setVisibility(View.VISIBLE);
                                            etPermState.setVisibility(View.VISIBLE);
                                            etPermCity.setVisibility(View.VISIBLE);
                                            etCorresState.setText(corresState);
                                            etCorresCity.setText(corresCity);
                                            etPermState.setText(permanentState);
                                            etPermCity.setText(permanentCity);
                                        } else {
                                            spCorresState.setVisibility(View.VISIBLE);
                                            spCorresCity.setVisibility(View.VISIBLE);
                                            spPermState.setVisibility(View.VISIBLE);
                                            spPermCity.setVisibility(View.VISIBLE);
                                            etCorresState.setVisibility(View.GONE);
                                            etCorresCity.setVisibility(View.GONE);
                                            etPermState.setVisibility(View.GONE);
                                            etPermCity.setVisibility(View.GONE);
                                            if (arrAdapState == null) {
                                                getState(corresState, permanentState, corresCity, permanentCity);
                                            } else {
                                                RowData corresStateData = getSpinnerRowDataByValue(corresState, arrAdapState);
                                                if (corresStateData != null) {
                                                    spCorresState.setSelection(corresStateData.getPosition());
                                                    getCorresCity(((RowData) spCorresState.getSelectedItem()).getKey(), corresCity);
                                                } else {
                                                    if (arrAdapCorresCity == null) {
                                                        arrCorresCity = new ArrayList<>();
                                                        arrAdapCorresCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresCity);
                                                        arrAdapCorresCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spCorresCity.setAdapter(arrAdapCorresCity);
                                                    }
                                                    arrAdapCorresCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapCorresCity.notifyDataSetChanged();
                                                    cityFoundFlag = true;
                                                }

                                                RowData permanentStateData = getSpinnerRowDataByValue(permanentState, arrAdapState);
                                                if (permanentStateData != null) {
                                                    spPermState.setSelection(permanentStateData.getPosition());
                                                    getPermanentCity(((RowData) spPermState.getSelectedItem()).getKey(), permanentCity);
                                                } else {
                                                    if (arrAdapPermCity == null) {
                                                        arrPermCity = new ArrayList<>();
                                                        arrAdapPermCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrPermCity);
                                                        arrAdapPermCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spPermCity.setAdapter(arrAdapPermCity);
                                                    }
                                                    arrAdapPermCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapPermCity.notifyDataSetChanged();
                                                    cityFoundFlag = true;
                                                }
                                            }
                                        }

                                        getSpinnerData(arrAdapTitle, spTitle, title);
                                        getSpinnerData(arrAdapTitle, spFatherSpouseTitle, fhTitle);
                                        getSpinnerData(arrAdapFemaleTitle, spMotherTitle, motherTitle);
                                        getSpinnerData(arrAdapMaidenTitle, spMaidenTitle, maidenTitle);
//                                        getSpinnerData(arrAdapCountryBirth, spBirthCountry, countryOfBirth);
//                                        getSpinnerData(arrAdapNationality, spNationality, nationality);
                                        getSpinnerData(arrAdapFatherSpouseSel, spFatherSpouseSel, relation);
                                        getSpinnerData(spOccArrayAdapter, spOccupation, occupationDetails);
                                        getSpinnerData(arrAdapQualification, spQualification, educationQualification);
                                        getSpinnerData(arrAdapIncome, spIncome, annualIncome);
                                        getSpinnerData(arrAdapStockPortfolio, spStockPortfolio, stockPortfolio);
                                        getSpinnerData(arrAdapEqExperience, spEqExperience, eqStockExp);
                                        getSpinnerData(arrAdapDerivativeExp, spEqDerivativeExp, derivativeExp);
                                        getSpinnerData(arrAdapCommExperience, spCommExperience, comStocExp);
                                        getSpinnerData(arrAdapCommOtherExp, spCommOtherExp, comOtherExp);
                                        getSpinnerData(arrAdapCountry, spPermCountry, permanentCountry);
                                        getSpinnerData(arrAdapCountry, spCorresCountry, corresCountry);

                                        if (isBlank(dob)) {
                                            etDOB.setEnabled(true);
                                        } else {
                                            etDOB.setEnabled(false);
                                        }
                                        //if (corresAddProof.equalsIgnoreCase("Aadhaar") || getAddressMode().equalsIgnoreCase(CVL)) {
                                        if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                                            cbSameAsCorrespondence.setEnabled(false);
                                            addressFieldDisable(etBldgNameCorres, corresAdd1);
                                            addressFieldDisable(etFlatNoCorres, corresAdd2);
                                            addressFieldDisable(etAreaCorres, corresAdd3);
                                            addressFieldDisable(etPincodeCorres, corresPinCode);
                                            addressFieldDisable(etCorresCity, corresCity);
                                            addressFieldDisable(etCorresState, corresState);
                                            addressFieldDisable(spCorresAddProof, corresAddProof);
                                            addressFieldDisable(spCorresCountry, corresCountry);
                                            addressFieldDisable(etBldgNamePerm, permanentAdd1);
                                            addressFieldDisable(etFlatNamePerm, permanentAdd2);
                                            addressFieldDisable(etAreaPerm, permanentAdd3);
                                            addressFieldDisable(etPincodePerm, permanentPinCode);
                                            addressFieldDisable(etPermCity, permanentCity);
                                            addressFieldDisable(etPermState, permanentState);
                                            addressFieldDisable(spPermAddProof, permanentAddProof);
                                            addressFieldDisable(spPermCountry, permanentCountry);
                                        }

                                        if (getInterestedInMF() && getAddressMode().equalsIgnoreCase(CVL)) {
                                            otherFieldDisable(spCorresAddProof, corresAddProof);
                                            otherFieldDisable(spPermAddProof, permanentAddProof);
                                            otherFieldDisable(etAltTelNo, altMobNo);
                                            otherFieldDisable(etAltEmailID, altEmailNo);
                                            otherFieldDisable(etFirstName, fName);
                                            otherFieldDisable(etMiddleName, mName);
                                            otherFieldDisable(etLastName, lName);
                                            otherFieldDisable(etDOB, dob);
                                            otherFieldDisable(etBirthPlace, placeOfBirth);
                                            otherFieldDisable(etFatherSpouseFirstName, fhfName);
                                            otherFieldDisable(etFatherSpouseMidName, fhmName);
                                            otherFieldDisable(etFatherSpouseLastName, fhlName);
                                            otherFieldDisable(etMotherFirstName, motherFname);
                                            otherFieldDisable(etMotherMiddleName, motherMname);
                                            otherFieldDisable(etMotherLastName, motherLname);
                                            otherFieldDisable(etMaidenFirstName, maidenFname);
                                            otherFieldDisable(etMaidenMiddleName, maidenMname);
                                            otherFieldDisable(etMaidenLastName, maidenLname);

//                                            Change for Work details
                                            /*otherFieldDisable(spOccupation, occupationDetails);//WorkDetails
                                            otherFieldDisable(spQualification, educationQualification);
                                            otherFieldDisable(spIncome, annualIncome);
                                            otherFieldDisable(spStockPortfolio, stockPortfolio);
                                            otherFieldDisable(spStockPortfolio, stockPortfolio);
                                            otherFieldDisable(etNWDate, asOnDate);
                                            if (networth.equalsIgnoreCase("") || networth.equalsIgnoreCase("0") || networth.equalsIgnoreCase("0.0")) {
                                                etNetWorth.setEnabled(true);
                                            } else {
                                                etNetWorth.setEnabled(false);
                                            }*/
                                            if (getInterestedInMF()) {
                                                /*if (politicallyExposedPerson.equalsIgnoreCase("Yes")) {
                                                    spMFPEPStatus.setEnabled(false);
                                                    etMFPEPRemark.setEnabled(false);
                                                } else if (relatedPoliticallyExposedPerson.equalsIgnoreCase("Yes")) {
                                                    spMFPEPStatus.setEnabled(false);
                                                    etMFPEPRemark.setEnabled(false);
                                                } else {
                                                    spMFPEPStatus.setEnabled(true);
                                                }*/

                                                if (rbMale.isChecked() || rbFemale.isChecked()) {
                                                    rbMale.setEnabled(false);
                                                    rbFemale.setEnabled(false);
                                                } else {
                                                    rbMale.setEnabled(true);
                                                    rbFemale.setEnabled(true);
                                                }
                                                if (rbMarried.isChecked() || rbUnMarried.isChecked()) {
                                                    rbMarried.setEnabled(false);
                                                    rbUnMarried.setEnabled(false);
                                                } else {
                                                    rbMarried.setEnabled(true);
                                                    rbUnMarried.setEnabled(true);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    cityFoundFlag = true;
                                }
                            }

                            if (response.has("Personal1")) {
                                JSONArray jsonArray1 = response.getJSONArray("Personal1");
                                if (jsonArray1.length() != 0) {
                                    arrayList = new ArrayList<HashMap<String, String>>();
                                    for (int i = 0; i < jsonArray1.length(); i++) {
                                        JSONObject object = jsonArray1.getJSONObject(i);
                                        map = new HashMap<String, String>();
                                        String selectedSegment = (object.isNull("SELECTEDSEGMENT") || object.getString("SELECTEDSEGMENT").equals("")) ? "" : object.getString("SELECTEDSEGMENT");
                                        map.put("SELECTEDSEGMENT", selectedSegment);
                                        if (isCommoditySegmentExist() || isCommodityOriginal() || selectedSegment.equalsIgnoreCase(COMMODITY)) {
                                            cbCommodity.setImageResource(R.drawable.ic_commodity_select);
                                            setIsCommoditySelected(true);
                                            commoditySpinner();
                                            commoditySelect();
                                        } else {
                                            cbCommodity.setImageResource(R.drawable.ic_commodity_dselect);
                                            setIsCommoditySelected(false);
                                            commodityDeselectSpinner();
                                            commodityDeselect();
                                        }
                                        if (isEquitySegmentExist() || isEquityOriginal() || selectedSegment.equalsIgnoreCase(EQUITY)) {
                                            cbEquity.setImageResource(R.drawable.ic_equity_select);
                                            setIsEquitySelected(true);
                                            equitySpinner();
                                            equitySelect();
                                            baEquitySpinnerTitle();
                                        } else {
                                            cbEquity.setImageResource(R.drawable.ic_equity_dselect);
                                            setIsEquitySelected(false);
                                            equityDeselectSpinner();
                                            equityDeselect();
                                        }
                                        /*if (getIsAllSelected() || selectedSegment.equalsIgnoreCase("All")) {
                                            setAll(false);
                                            allSpinner();
                                            allSelect();
                                        }*/
                                        if (selectedSegment.equalsIgnoreCase(Derivative)) {
                                            cbEquity.setImageResource(R.drawable.ic_equity_select);
                                            cbDerivative.setImageResource(R.drawable.ic_derivative_select);
                                            setIsDerivativeSelected(true);
                                            setDerivativeSegmentExist(true);
                                            setIsEquitySelected(true);
                                            equitySpinner();
                                            equitySelect();
                                            baEquitySpinnerTitle();
                                        } else {
                                            if (!isDerivativeOriginal()) {
                                                cbDerivative.setImageResource(R.drawable.ic_derivative_dselect);
                                                setIsDerivativeSelected(false);
                                                setDerivativeSegmentExist(false);
                                            }
                                        }
                                        if (selectedSegment.equalsIgnoreCase(CURRENCY)) {
                                            cbEquity.setImageResource(R.drawable.ic_equity_select);
                                            cbCurrency.setImageResource(R.drawable.ic_currency_select);
                                            setIsCurrencySelected(true);
                                            setCurrencySegmentExist(true);
                                            setIsEquitySelected(true);
                                            equitySpinner();
                                            equitySelect();
                                            baEquitySpinnerTitle();
                                        } else {
                                            if (!isCurrencyOriginal()) {
                                                cbCurrency.setImageResource(R.drawable.ic_currency_dselect);
                                                setIsCurrencySelected(false);
                                                setCurrencySegmentExist(false);
                                            }
                                        }
                                        if (selectedSegment.equalsIgnoreCase(MF)) {

                                            if (isMfSegmentExist()) {
                                                setIsInterestedInMF(false);
                                                cbMF.setVisibility(View.VISIBLE);
                                            } else {
                                                cbMF.setVisibility(View.GONE);
                                                equitySpinner();
                                                baMFSpinnerTitle();
                                                /*rlSegmentTitle.setVisibility(View.GONE);
                                                llSegmentSelection.setVisibility(View.GONE);*/
                                                showSegmentSelectionSection(false);
//                                                llResidentInOtherCountry.setVisibility(View.GONE);

                                                llPEP.setVisibility(View.GONE);
                                                llRelatedPEP.setVisibility(View.GONE);
                                                llRegisterToOtherBroker.setVisibility(View.GONE);
                                                llMFPEPStatus.setVisibility(View.VISIBLE);
                                                inputLayoutAltTelNo.setVisibility(View.VISIBLE);
                                                inputLayoutAltEmailID.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        if (isCurrencyOriginal() && isDerivativeOriginal() && isCommodityOriginal()) {
                                            setIsAllOriginal(true);
                                            setAll(false);
                                            allSpinner();
                                            allSelect();
                                        } else {
                                            cbAll.setImageResource(R.drawable.ic_all_dselect);
                                            setIsAllSelected(false);
                                        }
                                        if (isAllOriginal()) {
                                            setAll(false);
                                            allSpinner();
                                            allSelect();
                                        } else {
                                            cbAll.setImageResource(R.drawable.ic_all_dselect);
                                            setIsAllSelected(false);
                                        }
                                        arrayList.add(map);
                                    }
                                    disableEnableField();
                                } else {
                                    /*Forccefully calling this method to set flag  isDisableEnableFieldCalled=true*/
                                    disableEnableField();

                                }
                            }

                            if (response.has("Personal2")) {
                                JSONArray jsonArray2 = response.getJSONArray("Personal2");
                                if (jsonArray2.length() != 0) {
                                    for (int i = 0; i < jsonArray2.length(); i++) {
                                        JSONObject object = jsonArray2.getJSONObject(i);
                                        String interestedIn = (object.isNull("INTERESTEDIN") || object.getString("INTERESTEDIN").equals("")) ? "" : object.getString("INTERESTEDIN");

                                        if (response.has("Personal1")) {
                                            JSONArray jsonArray = response.getJSONArray("Personal1");
                                            if (jsonArray.length() != 0) {

                                            } else {
                                                if (interestedIn.equalsIgnoreCase(COMMODITY)) {
                                                    cbCommodity.setImageResource(R.drawable.ic_commodity_select);
                                                    setIsCommoditySelected(true);
                                                    commoditySpinner();
                                                    commoditySelect();
                                                } else if (interestedIn.equalsIgnoreCase(EQUITY) || interestedIn.equalsIgnoreCase(PMS)) {
                                                    cbEquity.setImageResource(R.drawable.ic_equity_select);
                                                    setIsEquitySelected(true);
                                                    equitySpinner();
                                                    equitySelect();
                                                    baEquitySpinnerTitle();
                                                } else if (interestedIn.equalsIgnoreCase("All")) {
                                                    setAll(false);
                                                    allSpinner();
                                                    allSelect();
                                                } else if (interestedIn.equalsIgnoreCase(Derivative)) {
                                                    cbEquity.setImageResource(R.drawable.ic_equity_select);
                                                    cbDerivative.setImageResource(R.drawable.ic_derivative_select);
                                                    setIsDerivativeSelected(true);
                                                    setIsEquitySelected(true);
                                                    equitySpinner();
                                                    equitySelect();
                                                    baEquitySpinnerTitle();
                                                } else if (interestedIn.equalsIgnoreCase(CURRENCY)) {
                                                    cbEquity.setImageResource(R.drawable.ic_equity_select);
                                                    cbCurrency.setImageResource(R.drawable.ic_currency_select);
                                                    setIsCurrencySelected(true);
                                                    setIsEquitySelected(true);
                                                    equitySpinner();
                                                    equitySelect();
                                                    baEquitySpinnerTitle();
                                                } else if (interestedIn.equalsIgnoreCase(MF)) {
                                                    if (!isMfSegmentExist()) {
                                                        equitySpinner();
                                                        baMFSpinnerTitle();
                                                    }
                                                }
                                                if (getIsDerivativeSelected() && getIsCurrencySelected() && getIsCommoditySelected()) {
                                                    setAll(false);
                                                    allSpinner();
                                                    allSelect();
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (response.has("Personal3")) {
                                JSONArray jsonArray3 = response.getJSONArray("Personal3");
                                if (jsonArray3.length() != 0) {
                                    for (int i = 0; i < jsonArray3.length(); i++) {
                                        JSONObject object = jsonArray3.getJSONObject(i);
                                        String interestedin = (object.isNull("INTERESTEDIN") || object.getString("INTERESTEDIN").equals("")) ? "" : object.getString("INTERESTEDIN");
                                        String existingSegment = (object.isNull("EXISTINGSEGMENT") || object.getString("EXISTINGSEGMENT").equals("")) ? "" : object.getString("EXISTINGSEGMENT");

                                        if (existingSegment.equalsIgnoreCase(EQUITY)) {
                                            cbEquity.setEnabled(false);
                                        } else if (existingSegment.equalsIgnoreCase(COMMODITY)) {
                                            cbCommodity.setEnabled(false);
                                        } else if (existingSegment.equalsIgnoreCase(Derivative)) {
                                            cbEquity.setEnabled(false);
                                            cbDerivative.setEnabled(false);
                                        } else if (existingSegment.equalsIgnoreCase(CURRENCY)) {
                                            cbEquity.setEnabled(false);
                                            cbCurrency.setEnabled(false);
                                        } else if (existingSegment.equalsIgnoreCase(MF)) {
                                            cbMF.setVisibility(View.VISIBLE);
                                            cbMF.setEnabled(false);

                                        }
                                    }
                                }
                            }
                            /**
                             * Address proof spinner data for direct and CRM
                             */
                            if (getAppType().equalsIgnoreCase(DIRECT) || getAppType().equalsIgnoreCase(CRM)) {
                                if (response.has("Personal4")) {
                                    arrCorresAddressProof = new ArrayList<>();
                                    arrCorresAddressProof.add(new RowData(0, "-1", "Select"));
                                    arrPermAddressProof = new ArrayList<>();
                                    arrPermAddressProof.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArray4 = response.getJSONArray("Personal4");
                                    if (jsonArray4.length() != 0) {
                                        for (int i = 0; i < jsonArray4.length(); i++) {
                                            JSONObject object = jsonArray4.getJSONObject(i);
                                            String id = (object.isNull("ID") || object.getString("ID").equals("")) ? "" : object.getString("ID");
                                            String text = (object.isNull("TEXT") || object.getString("TEXT").equals("")) ? "" : object.getString("TEXT");
                                            arrCorresAddressProof.add(new RowData(i + 1, id, text));
                                            arrPermAddressProof.add(new RowData(i + 1, id, text));
                                        }
                                        arrAdapCorresAddressProof = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresAddressProof);
                                        arrAdapCorresAddressProof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCorresAddProof.setAdapter(arrAdapCorresAddressProof);
                                        arrAdapCorresAddressProof.notifyDataSetChanged();

                                        arrAdapPermAddressProof = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrPermAddressProof);
                                        arrAdapPermAddressProof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spPermAddProof.setAdapter(arrAdapPermAddressProof);
                                        arrAdapPermAddressProof.notifyDataSetChanged();
//                                        spCorresAddProof.setOnItemSelectedListener(eqBranchListener);
                                        if (!(arrAdapCorresAddressProof == null)) {
                                            RowData corresAdd = getSpinnerRowDataById(corresAddProof, arrAdapCorresAddressProof);
                                            if (corresAdd != null) {
                                                spCorresAddProof.setSelection(corresAdd.getPosition());
                                            }
                                        }
                                        if (!(arrAdapPermAddressProof == null)) {
                                            RowData permAdd = getSpinnerRowDataById(permanentAddProof, arrAdapCorresAddressProof);
                                            if (permAdd != null) {
                                                spPermAddProof.setSelection(permAdd.getPosition());
                                            }
                                        }
                                    }
                                }
                            }

                            if (getAppType().equalsIgnoreCase(PP)) {
                                if (response.has("Personal4")) {
                                    JSONArray jsonArray4 = response.getJSONArray("Personal4");
                                    if (jsonArray4.length() != 0) {
                                        for (int i = 0; i < jsonArray4.length(); i++) {
                                            JSONObject object = jsonArray4.getJSONObject(i);
                                            String userType = (object.isNull("USERTYPE") || object.getString("USERTYPE").equals("")) ? "" : object.getString("USERTYPE");
                                            branch = (object.isNull("BRANCH") || object.getString("BRANCH").equals("")) ? "" : object.getString("BRANCH");
                                            subBroker = (object.isNull("SUBBROKER") || object.getString("SUBBROKER").equals("")) ? "" : object.getString("SUBBROKER");
                                            trader = (object.isNull("Trader") || object.getString("Trader").equals("")) ? "" : object.getString("Trader");
                                            branchRMEmpcode = (object.isNull("BranchRM_Empcode") || object.getString("BranchRM_Empcode").equals("")) ? "" : object.getString("BranchRM_Empcode");

                                            if (branchRMEmpcode.equalsIgnoreCase("ranchi")) {
                                                enableField(llEqSpinners);
                                                enableField(llCommSpinners);
                                            } else {
                                                if (userType.equalsIgnoreCase("FRANCHISEE") || userType.equalsIgnoreCase("FRANCHISEESTAFF")) {
                                                    if (!isEquitySegmentExist()) {
                                                        if (isIpvORDocumentStageDone() || isMfIpvDone()) {//paid or MF ipv done (change to ipv done 13-July)
                                                            disableField(llEqSpinners);
                                                        } else {
                                                            spEqBranchCode.setEnabled(false);
                                                            spEqSubBroker.setEnabled(true);
                                                            spEqTrader.setEnabled(true);
                                                            spEqArea.setEnabled(true);
                                                            spEqRegion.setEnabled(true);
                                                        }
                                                    } else {
                                                        disableField(llEqSpinners);
                                                    }

                                                    if (!isCommoditySegmentExist()) {
                                                        if (isIpvORDocumentStageDone() || isMfIpvDone()) {//paid or MF ipv done (change to ipv done 13-July)
                                                            disableField(llCommSpinners);
                                                        } else {
                                                            spCommBranchCode.setEnabled(false);
                                                            spCommSubBroker.setEnabled(true);
                                                            spCommTrader.setEnabled(true);
                                                            spCommArea.setEnabled(true);
                                                            spCommRegion.setEnabled(true);
                                                        }
                                                    } else {
                                                        disableField(llCommSpinners);
                                                    }
                                                } else if (userType.equalsIgnoreCase("SUBBROKER")) {
                                                    if (!isEquitySegmentExist()) {
                                                        if (isIpvORDocumentStageDone() || isMfIpvDone()) {//paid or MF ipv done (change to ipv done 13-July)
                                                            disableField(llEqSpinners);
                                                        } else {
                                                            spEqBranchCode.setEnabled(false);
                                                            spEqSubBroker.setEnabled(false);
                                                            spEqTrader.setEnabled(true);
                                                            spEqArea.setEnabled(true);
                                                            spEqRegion.setEnabled(true);
                                                        }
                                                    } else {
                                                        disableField(llEqSpinners);
                                                    }

                                                    if (!isCommoditySegmentExist()) {
                                                        if (isIpvORDocumentStageDone() || isMfIpvDone()) {//paid or MF ipv done (change to ipv done 13-July)
                                                            disableField(llCommSpinners);
                                                        } else {
                                                            spCommBranchCode.setEnabled(false);
                                                            spCommSubBroker.setEnabled(false);
                                                            spCommTrader.setEnabled(true);
                                                            spCommArea.setEnabled(true);
                                                            spCommRegion.setEnabled(true);
                                                        }
                                                    } else {
                                                        disableField(llCommSpinners);
                                                    }
                                                } else if (userType.equalsIgnoreCase("TRADER")) {
                                                    if (!isEquitySegmentExist()) {
                                                        if (isIpvORDocumentStageDone() || isMfIpvDone()) {//paid or MF ipv done (change to ipv done 13-July)
                                                            disableField(llEqSpinners);
                                                        } else {
                                                            spEqBranchCode.setEnabled(false);
                                                            spEqSubBroker.setEnabled(false);
                                                            spEqTrader.setEnabled(false);
                                                            spEqArea.setEnabled(true);
                                                            spEqRegion.setEnabled(true);
                                                        }
                                                    } else {
                                                        disableField(llEqSpinners);
                                                    }

                                                    if (!isCommoditySegmentExist()) {
                                                        if (isIpvORDocumentStageDone() || isMfIpvDone()) {//paid or MF ipv done (change to ipv done 13-July)
                                                            disableField(llCommSpinners);
                                                        } else {
                                                            spCommBranchCode.setEnabled(false);
                                                            spCommSubBroker.setEnabled(false);
                                                            spCommTrader.setEnabled(false);
                                                            spCommArea.setEnabled(true);
                                                            spCommRegion.setEnabled(true);
                                                        }
                                                    } else {
                                                        disableField(llCommSpinners);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                if (response.has("Personal5")) {
                                    JSONArray jsonArray5 = response.getJSONArray("Personal5");
                                    if (jsonArray5.length() != 0) {
                                        for (int i = 0; i < jsonArray5.length(); i++) {
                                            JSONObject object = jsonArray5.getJSONObject(i);
                                        }
                                    }
                                }
                                //equity branch
                                if (response.has("Personal6")) {
                                    arrEqBranch = new ArrayList<>();
                                    arrEqBranch.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayEqBranch = response.getJSONArray("Personal6");
                                    if (jsonArrayEqBranch.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqBranch.length(); i++) {
                                            JSONObject object = jsonArrayEqBranch.getJSONObject(i);
                                            String branchCodeEq = (object.isNull("BRANCH_CODEEQ") || object.getString("BRANCH_CODEEQ").equals("")) ? "" : object.getString("BRANCH_CODEEQ");
                                            String branchEq = (object.isNull("BRANCHEQ") || object.getString("BRANCHEQ").equals("")) ? "" : object.getString("BRANCHEQ");
                                            arrEqBranch.add(new RowData(i + 1, branchCodeEq, branchEq));
                                        }
                                        arrAdapEqBranch = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrEqBranch);
                                        arrAdapEqBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spEqBranchCode.setAdapter(arrAdapEqBranch);
                                        arrAdapEqBranch.notifyDataSetChanged();
                                        spEqBranchCode.setOnItemSelectedListener(eqBranchListener);
                                    }
                                }

                                if (response.has("Personal7")) {
                                    arrCommBranch = new ArrayList<>();
                                    arrCommBranch.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayCommBranch = response.getJSONArray("Personal7");
                                    if (jsonArrayCommBranch.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommBranch.length(); i++) {
                                            JSONObject object = jsonArrayCommBranch.getJSONObject(i);
                                            String branchCodeComm = (object.isNull("BRANCH_CODECOMM") || object.getString("BRANCH_CODECOMM").equals("")) ? "" : object.getString("BRANCH_CODECOMM");
                                            String branchComm = (object.isNull("BRANCHCOMM") || object.getString("BRANCHCOMM").equals("")) ? "" : object.getString("BRANCHCOMM");
                                            arrCommBranch.add(new RowData(i + 1, branchCodeComm, branchComm));
                                        }
                                        arrAdapCommBranch = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCommBranch);
                                        arrAdapCommBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCommBranchCode.setAdapter(arrAdapCommBranch);
                                        arrAdapCommBranch.notifyDataSetChanged();
                                        spCommBranchCode.setOnItemSelectedListener(commBranchListener);
                                    }
                                }

                                //SubBroker Spinner
                                if (response.has("Personal8")) {
                                    arrEqSubBroker = new ArrayList<>();
                                    arrEqSubBroker.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayEqSubBroker = response.getJSONArray("Personal8");
                                    if (jsonArrayEqSubBroker.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqSubBroker.length(); i++) {
                                            JSONObject object = jsonArrayEqSubBroker.getJSONObject(i);
                                            String subBrokerIdEq = (object.isNull("SUBBROKERIDEQ") || object.getString("SUBBROKERIDEQ").equals("")) ? "" : object.getString("SUBBROKERIDEQ");
                                            String subBrokerNameEq = (object.isNull("SUBBROKERNAMEEQ") || object.getString("SUBBROKERNAMEEQ").equals("")) ? "" : object.getString("SUBBROKERNAMEEQ");
                                            arrEqSubBroker.add(new RowData(i + 1, subBrokerIdEq, subBrokerNameEq));
                                        }
                                        arrAdapEqSubBroker = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrEqSubBroker);
                                        arrAdapEqSubBroker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spEqSubBroker.setAdapter(arrAdapEqSubBroker);
                                        spEqSubBroker.setOnItemSelectedListener(null);
                                        arrAdapEqSubBroker.notifyDataSetChanged();
//                                        spEqSubBroker.setSelection(0, false);
                                        spEqSubBroker.setOnItemSelectedListener(eqSubBrokerCodeListener);
//                                    arrAdapEqSubBroker.notifyDataSetChanged();
//                                    spEqSubBroker.setOnItemSelectedListener(eqSubBrokerCodeListener);
                                    }
                                }

                                if (response.has("Personal9")) {
                                    arrCommSubBroker = new ArrayList<>();
                                    arrCommSubBroker.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayCommSubBroker = response.getJSONArray("Personal9");
                                    if (jsonArrayCommSubBroker.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommSubBroker.length(); i++) {
                                            JSONObject object = jsonArrayCommSubBroker.getJSONObject(i);
                                            String subBrokerIdComm = (object.isNull("SUBBROKERIDCOMM") || object.getString("SUBBROKERIDCOMM").equals("")) ? "" : object.getString("SUBBROKERIDCOMM");
                                            String subBrokerNameComm = (object.isNull("SUBBROKERNAMECOMM") || object.getString("SUBBROKERNAMECOMM").equals("")) ? "" : object.getString("SUBBROKERNAMECOMM");
                                            arrCommSubBroker.add(new RowData(i + 1, subBrokerIdComm, subBrokerNameComm));
                                        }
                                        arrAdapCommSubBroker = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCommSubBroker);
                                        arrAdapCommSubBroker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCommSubBroker.setAdapter(arrAdapCommSubBroker);
                                        arrAdapCommSubBroker.notifyDataSetChanged();
                                        spCommSubBroker.setOnItemSelectedListener(commSubBrokerCodeListener);
                                    }
                                }

                                //Trader Spinner
                                if (response.has("Personal10")) {
                                    arrEqTrader = new ArrayList<>();
                                    arrEqTrader.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayEqTrader = response.getJSONArray("Personal10");
                                    if (jsonArrayEqTrader.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqTrader.length(); i++) {
                                            JSONObject object = jsonArrayEqTrader.getJSONObject(i);
                                            String traderIdEq = (object.isNull("TRADERIDEQ") || object.getString("TRADERIDEQ").equals("")) ? "" : object.getString("TRADERIDEQ");
                                            String traderNameEq = (object.isNull("TRADERNAMEEQ") || object.getString("TRADERNAMEEQ").equals("")) ? "" : object.getString("TRADERNAMEEQ");
                                            arrEqTrader.add(new RowData(i + 1, traderIdEq, traderNameEq));
                                        }
                                        arrAdapEqTrader = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrEqTrader);
                                        arrAdapEqTrader.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spEqTrader.setAdapter(arrAdapEqTrader);
                                        arrAdapEqTrader.notifyDataSetChanged();
                                        spEqTrader.setOnItemSelectedListener(eqTraderCodeListener);
                                    }
                                }

                                if (response.has("Personal11")) {
                                    arrCommTrader = new ArrayList<>();
                                    arrCommTrader.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayCommTrader = response.getJSONArray("Personal11");
                                    if (jsonArrayCommTrader.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommTrader.length(); i++) {
                                            JSONObject object = jsonArrayCommTrader.getJSONObject(i);
                                            String traderIdComm = (object.isNull("TRADERIDCOMM") || object.getString("TRADERIDCOMM").equals("")) ? "" : object.getString("TRADERIDCOMM");
                                            String traderNameComm = (object.isNull("TRADERNAMECOMM") || object.getString("TRADERNAMECOMM").equals("")) ? "" : object.getString("TRADERNAMECOMM");
                                            arrCommTrader.add(new RowData(i + 1, traderIdComm, traderNameComm));
                                        }
                                        arrAdapCommTrader = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCommTrader);
                                        arrAdapCommTrader.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCommTrader.setAdapter(arrAdapCommTrader);
                                        arrAdapCommTrader.notifyDataSetChanged();
                                        spCommTrader.setOnItemSelectedListener(commTraderCodeListener);
                                    }
                                }

                                //Region Spinner
                                if (response.has("Personal12")) {
                                    arrEqRegion = new ArrayList<>();
                                    arrEqRegion.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayEqRegion = response.getJSONArray("Personal12");
                                    if (jsonArrayEqRegion.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqRegion.length(); i++) {
                                            JSONObject object = jsonArrayEqRegion.getJSONObject(i);
                                            String regionIdEq = (object.isNull("REGIONIDEQ") || object.getString("REGIONIDEQ").equals("")) ? "" : object.getString("REGIONIDEQ");
                                            String regionNameEq = (object.isNull("REGIONNAMEEQ") || object.getString("REGIONNAMEEQ").equals("")) ? "" : object.getString("REGIONNAMEEQ");
                                            arrEqRegion.add(new RowData(i + 1, regionIdEq, regionNameEq));
                                        }
                                        arrAdapEqRegion = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrEqRegion);
                                        arrAdapEqRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spEqRegion.setAdapter(arrAdapEqRegion);
                                        arrAdapEqRegion.notifyDataSetChanged();
                                        spEqRegion.setOnItemSelectedListener(eqRegionListener);
                                    }
                                }

                                if (response.has("Personal13")) {
                                    arrCommRegion = new ArrayList<>();
                                    arrCommRegion.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayCommRegion = response.getJSONArray("Personal13");
                                    if (jsonArrayCommRegion.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommRegion.length(); i++) {
                                            JSONObject object = jsonArrayCommRegion.getJSONObject(i);
                                            String regionIdComm = (object.isNull("REGIONIDCOMM") || object.getString("REGIONIDCOMM").equals("")) ? "" : object.getString("REGIONIDCOMM");
                                            String regionNameComm = (object.isNull("REGIONNAMECOMM") || object.getString("REGIONNAMECOMM").equals("")) ? "" : object.getString("REGIONNAMECOMM");
                                            arrCommRegion.add(new RowData(i + 1, regionIdComm, regionNameComm));
                                        }
                                        arrAdapCommRegion = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCommRegion);
                                        arrAdapCommRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCommRegion.setAdapter(arrAdapCommRegion);
                                        arrAdapCommRegion.notifyDataSetChanged();
                                        spCommRegion.setOnItemSelectedListener(commRegionListener);
                                    }
                                }

                                //Area Spinner
                                if (response.has("Personal14")) {
                                    arrEqArea = new ArrayList<>();
                                    arrEqArea.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayEqArea = response.getJSONArray("Personal14");
                                    if (jsonArrayEqArea.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqArea.length(); i++) {
                                            JSONObject object = jsonArrayEqArea.getJSONObject(i);
                                            String areaIdEq = (object.isNull("AREAIDEQ") || object.getString("AREAIDEQ").equals("")) ? "" : object.getString("AREAIDEQ");
                                            String areaNameEq = (object.isNull("AREANAMEEQ") || object.getString("AREANAMEEQ").equals("")) ? "" : object.getString("AREANAMEEQ");
                                            arrEqArea.add(new RowData(i + 1, areaIdEq, areaNameEq));
                                        }
                                        arrAdapEqArea = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrEqArea);
                                        arrAdapEqArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spEqArea.setAdapter(arrAdapEqArea);
                                        arrAdapEqArea.notifyDataSetChanged();
                                        spEqArea.setOnItemSelectedListener(eqAreaListener);
                                    }
                                }

                                if (response.has("Personal15")) {
                                    arrCommArea = new ArrayList<>();
                                    arrCommArea.add(new RowData(0, "-1", "Select"));

                                    JSONArray jsonArrayCommArea = response.getJSONArray("Personal15");
                                    if (jsonArrayCommArea.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommArea.length(); i++) {
                                            JSONObject object = jsonArrayCommArea.getJSONObject(i);
                                            String areaIdComm = (object.isNull("AREAIDCOMM") || object.getString("AREAIDCOMM").equals("")) ? "" : object.getString("AREAIDCOMM");
                                            String areaNameComm = (object.isNull("AREANAMECOMM") || object.getString("AREANAMECOMM").equals("")) ? "" : object.getString("AREANAMECOMM");
                                            arrCommArea.add(new RowData(i + 1, areaIdComm, areaNameComm));
                                        }
                                        arrAdapCommArea = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCommArea);
                                        arrAdapCommArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCommArea.setAdapter(arrAdapCommArea);
                                        arrAdapCommArea.notifyDataSetChanged();
                                        spCommArea.setOnItemSelectedListener(commAreaListener);
                                    }
                                }

                                //User Info For Spinner
                                if (response.has("Personal16")) {
                                    JSONArray jsonArrayEqDetail = response.getJSONArray("Personal16");
                                    if (jsonArrayEqDetail.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqDetail.length(); i++) {
                                            JSONObject object = jsonArrayEqDetail.getJSONObject(i);
                                            String eqUserId = (object.isNull("USERID") || object.getString("USERID").equals("")) ? "" : object.getString("USERID");
                                            String eqSegment = (object.isNull("SEGMENT") || object.getString("SEGMENT").equals("")) ? "" : object.getString("SEGMENT");
                                            String eqBranch = (object.isNull("BRANCH") || object.getString("BRANCH").equals("")) ? "" : object.getString("BRANCH");
                                            String eqSubBroker = (object.isNull("SUBBROKER") || object.getString("SUBBROKER").equals("")) ? "" : object.getString("SUBBROKER");
                                            String eqTrader = (object.isNull("TRADER") || object.getString("TRADER").equals("")) ? "" : object.getString("TRADER");
                                            String eqArea = (object.isNull("AREA") || object.getString("AREA").equals("")) ? "" : object.getString("AREA");
                                            String eqRegion = (object.isNull("REGION") || object.getString("REGION").equals("")) ? "" : object.getString("REGION");

                                            if (!(arrAdapEqBranch == null)) {
                                                RowData eqBranchData = getSpinnerRowDataById(eqBranch, arrAdapEqBranch);
                                                if (eqBranchData != null) {
                                                    spEqBranchCode.setSelection(eqBranchData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapEqSubBroker == null)) {
                                                RowData eqSubBrokerData = getSpinnerRowDataById(eqSubBroker, arrAdapEqSubBroker);
                                                if (eqSubBrokerData != null) {
                                                    spEqSubBroker.setSelection(eqSubBrokerData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapEqTrader == null)) {
                                                RowData eqTraderData = getSpinnerRowDataById(eqTrader, arrAdapEqTrader);
                                                if (eqTraderData != null) {
                                                    spEqTrader.setSelection(eqTraderData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapEqArea == null)) {
                                                RowData eqAreaData = getSpinnerRowDataById(eqArea, arrAdapEqArea);
                                                if (eqAreaData != null) {
                                                    spEqArea.setSelection(eqAreaData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapEqRegion == null)) {
                                                RowData eqRegionData = getSpinnerRowDataById(eqRegion, arrAdapEqRegion);
                                                if (eqRegionData != null) {
                                                    spEqRegion.setSelection(eqRegionData.getPosition());
                                                }
                                            }
                                        }
                                    } else {
                                        if (!(arrAdapEqBranch == null) && !(branch.equalsIgnoreCase(""))) {
                                            RowData eqBranchData = getSpinnerRowDataById(branch, arrAdapEqBranch);
                                            if (eqBranchData != null) {
                                                spEqBranchCode.setSelection(eqBranchData.getPosition());
                                            }
                                        }
                                        if (!(arrAdapEqSubBroker == null) && !(subBroker.equalsIgnoreCase(""))) {
                                            RowData eqSubBrokerData = getSpinnerRowDataById(subBroker, arrAdapEqSubBroker);
                                            if (eqSubBrokerData != null) {
                                                spEqSubBroker.setSelection(eqSubBrokerData.getPosition());
                                            }
                                        }
                                        if (!(arrAdapEqTrader == null) && !(trader.equalsIgnoreCase(""))) {
                                            RowData eqTraderData = getSpinnerRowDataById(trader, arrAdapEqTrader);
                                            if (eqTraderData != null) {
                                                spEqTrader.setSelection(eqTraderData.getPosition());
                                            }
                                        }
                                        if (!(arrAdapEqArea == null)) {
                                            RowData eqAreaData = getSpinnerRowDataById("FRANCHISEE", arrAdapEqArea);
                                            if (eqAreaData != null) {
                                                spEqArea.setSelection(eqAreaData.getPosition());
                                            }
                                        }
                                        if (!(arrAdapEqRegion == null)) {
                                            RowData eqRegionData = getSpinnerRowDataById("FRANCHISEE", arrAdapEqRegion);
                                            if (eqRegionData != null) {
                                                spEqRegion.setSelection(eqRegionData.getPosition());
                                            }
                                        }
                                    }
                                }

                                if (response.has("Personal17")) {
                                    JSONArray jsonArrayCommDetail = response.getJSONArray("Personal17");
                                    if (jsonArrayCommDetail.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommDetail.length(); i++) {
                                            JSONObject object = jsonArrayCommDetail.getJSONObject(i);
                                            String commIserId = (object.isNull("USERID") || object.getString("USERID").equals("")) ? "" : object.getString("USERID");
                                            String commSegment = (object.isNull("SEGMENT") || object.getString("SEGMENT").equals("")) ? "" : object.getString("SEGMENT");
                                            String commBranch = (object.isNull("BRANCH") || object.getString("BRANCH").equals("")) ? "" : object.getString("BRANCH");
                                            String commSubBroker = (object.isNull("SUBBROKER") || object.getString("SUBBROKER").equals("")) ? "" : object.getString("SUBBROKER");
                                            String commTrader = (object.isNull("TRADER") || object.getString("TRADER").equals("")) ? "" : object.getString("TRADER");
                                            String commArea = (object.isNull("AREA") || object.getString("AREA").equals("")) ? "" : object.getString("AREA");
                                            String commRegion = (object.isNull("REGION") || object.getString("REGION").equals("")) ? "" : object.getString("REGION");

                                            if (!(arrAdapCommBranch == null)) {
                                                RowData commBranchData = getSpinnerRowDataById(commBranch, arrAdapCommBranch);
                                                if (commBranchData != null) {
                                                    spCommBranchCode.setSelection(commBranchData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapCommSubBroker == null)) {
                                                RowData commSubBrokerData = getSpinnerRowDataById(commSubBroker, arrAdapCommSubBroker);
                                                if (commSubBrokerData != null) {
                                                    spCommSubBroker.setSelection(commSubBrokerData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapCommTrader == null)) {
                                                RowData commTraderData = getSpinnerRowDataById(commTrader, arrAdapCommTrader);
                                                if (commTraderData != null) {
                                                    spCommTrader.setSelection(commTraderData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapCommArea == null)) {
                                                RowData commAreaData = getSpinnerRowDataById(commArea, arrAdapCommArea);
                                                if (commAreaData != null) {
                                                    spCommArea.setSelection(commAreaData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapCommRegion == null)) {
                                                RowData commRegionData = getSpinnerRowDataById(commRegion, arrAdapCommRegion);
                                                if (commRegionData != null) {
                                                    spCommRegion.setSelection(commRegionData.getPosition());
                                                }
                                            }
                                        }
                                    } else {
                                        if (!(arrAdapCommBranch == null) && !(branch.equalsIgnoreCase(""))) {
                                            RowData commBranchData = getSpinnerRowDataById(branch, arrAdapCommBranch);
                                            if (commBranchData != null) {
                                                spCommBranchCode.setSelection(commBranchData.getPosition());
                                            }
                                        }
                                        if (!(arrAdapCommSubBroker == null) && !(subBroker.equalsIgnoreCase(""))) {
                                            RowData commSubBrokerData = getSpinnerRowDataById(subBroker, arrAdapCommSubBroker);
                                            if (commSubBrokerData != null) {
                                                spCommSubBroker.setSelection(commSubBrokerData.getPosition());
                                            }
                                        }
                                        if (!(arrAdapCommTrader == null) && !(trader.equalsIgnoreCase(""))) {
                                            RowData commTraderData = getSpinnerRowDataById(trader, arrAdapCommTrader);
                                            if (commTraderData != null) {
                                                spCommTrader.setSelection(commTraderData.getPosition());
                                            }
                                        }
                                        if (!(arrAdapCommArea == null)) {
                                            RowData commAreaData = getSpinnerRowDataById("FRANCHISEE", arrAdapCommArea);
                                            if (commAreaData != null) {
                                                spCommArea.setSelection(commAreaData.getPosition());
                                            }
                                        }
                                        if (!(arrAdapCommRegion == null)) {
                                            RowData commRegionData = getSpinnerRowDataById("FRANCHISEE", arrAdapCommRegion);
                                            if (commRegionData != null) {
                                                spCommRegion.setSelection(commRegionData.getPosition());
                                            }
                                        }
                                    }
                                }
                                /**
                                 * Address proof spinner data for Franchise
                                 */
                                if (response.has("Personal18")) {
                                    arrCorresAddressProof = new ArrayList<>();
                                    arrCorresAddressProof.add(new RowData(0, "-1", "Select"));
                                    arrPermAddressProof = new ArrayList<>();
                                    arrPermAddressProof.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArray4 = response.getJSONArray("Personal18");
                                    if (jsonArray4.length() != 0) {
                                        for (int i = 0; i < jsonArray4.length(); i++) {
                                            JSONObject object = jsonArray4.getJSONObject(i);
                                            String id = (object.isNull("ID") || object.getString("ID").equals("")) ? "" : object.getString("ID");
                                            String text = (object.isNull("TEXT") || object.getString("TEXT").equals("")) ? "" : object.getString("TEXT");
                                            arrCorresAddressProof.add(new RowData(i + 1, id, text));
                                            arrPermAddressProof.add(new RowData(i + 1, id, text));
                                        }
                                        arrAdapCorresAddressProof = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresAddressProof);
                                        arrAdapCorresAddressProof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCorresAddProof.setAdapter(arrAdapCorresAddressProof);
                                        arrAdapCorresAddressProof.notifyDataSetChanged();

                                        arrAdapPermAddressProof = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrPermAddressProof);
                                        arrAdapPermAddressProof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spPermAddProof.setAdapter(arrAdapPermAddressProof);
                                        arrAdapPermAddressProof.notifyDataSetChanged();
//                                        spCorresAddProof.setOnItemSelectedListener(eqBranchListener);
                                        if (!(arrAdapCorresAddressProof == null)) {
                                            RowData corresAdd = getSpinnerRowDataById(corresAddProof, arrAdapCorresAddressProof);
                                            if (corresAdd != null) {
                                                spCorresAddProof.setSelection(corresAdd.getPosition());
                                            }
                                        }
                                        if (!(arrAdapPermAddressProof == null)) {
                                            RowData permAdd = getSpinnerRowDataById(permanentAddProof, arrAdapCorresAddressProof);
                                            if (permAdd != null) {
                                                spPermAddProof.setSelection(permAdd.getPosition());
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                            alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                        }
                        hideProgress();
                    } else {
//                        MOSLCommon.showToastMsg(mContext, NO_RESPONSE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
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
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
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

    public void getFrachiseeDetail(String branchCode, final String type) {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
//            inputMap.put("_userId", "1451389");
            inputMap.put("_userId", getUserID());
            inputMap.put("_loginType", "FRANCHISEE");
            if (type.equalsIgnoreCase(EQUITY)) {
                inputMap.put("_branchCode", ((RowData) spEqBranchCode.getSelectedItem()).getKey());
                inputMap.put("_subBroker", ((RowData) spEqSubBroker.getSelectedItem()).getKey());
            } else if (type.equalsIgnoreCase(COMMODITY)) {
                inputMap.put("_branchCode", ((RowData) spCommBranchCode.getSelectedItem()).getKey());
                inputMap.put("_subBroker", ((RowData) spCommSubBroker.getSelectedItem()).getKey());
            }
            String flag = "GETFRANCHISEEDETAILS";
            String category = PERSONAL;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            if (type.equalsIgnoreCase(EQUITY)) {
                                //equity branch
                                /*if (response.has("Personal")) {
                                    arrEqBranch = new ArrayList<>();
                                    arrEqBranch.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayEqBranch = response.getJSONArray("Personal");
                                    if (jsonArrayEqBranch.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqBranch.length(); i++) {
                                            JSONObject object = jsonArrayEqBranch.getJSONObject(i);
                                            String branchCodeEq = (object.isNull("BRANCH_CODEEQ") || object.getString("BRANCH_CODEEQ").equals("")) ? "" : object.getString("BRANCH_CODEEQ");
                                            String branchEq = (object.isNull("BRANCHEQ") || object.getString("BRANCHEQ").equals("")) ? "" : object.getString("BRANCHEQ");
                                            arrEqBranch.add(new RowData(i + 1, branchCodeEq, branchEq));
                                        }
                                        arrAdapEqBranch = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrEqBranch);
                                        arrAdapEqBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spEqBranchCode.setAdapter(arrAdapEqBranch);
                                        arrAdapEqBranch.notifyDataSetChanged();
                                    }
                                }*/

                                //SubBroker Spinner
                                if (response.has("Personal2")) {
                                    arrEqSubBroker = new ArrayList<>();
                                    arrEqSubBroker.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayEqSubBroker = response.getJSONArray("Personal2");
                                    if (jsonArrayEqSubBroker.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqSubBroker.length(); i++) {
                                            JSONObject object = jsonArrayEqSubBroker.getJSONObject(i);
                                            String subBrokerIdEq = (object.isNull("SUBBROKERIDEQ") || object.getString("SUBBROKERIDEQ").equals("")) ? "" : object.getString("SUBBROKERIDEQ");
                                            String subBrokerNameEq = (object.isNull("SUBBROKERNAMEEQ") || object.getString("SUBBROKERNAMEEQ").equals("")) ? "" : object.getString("SUBBROKERNAMEEQ");
                                            arrEqSubBroker.add(new RowData(i + 1, subBrokerIdEq, subBrokerNameEq));
                                        }
                                        arrAdapEqSubBroker = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrEqSubBroker);
                                        arrAdapEqSubBroker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spEqSubBroker.setAdapter(arrAdapEqSubBroker);
                                        spEqSubBroker.setOnItemSelectedListener(null);
                                        arrAdapEqSubBroker.notifyDataSetChanged();
                                    }
                                }

                                //Trader Spinner
                                if (response.has("Personal4")) {
                                    arrEqTrader = new ArrayList<>();
                                    arrEqTrader.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayEqTrader = response.getJSONArray("Personal4");
                                    if (jsonArrayEqTrader.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqTrader.length(); i++) {
                                            JSONObject object = jsonArrayEqTrader.getJSONObject(i);
                                            String traderIdEq = (object.isNull("TRADERIDEQ") || object.getString("TRADERIDEQ").equals("")) ? "" : object.getString("TRADERIDEQ");
                                            String traderNameEq = (object.isNull("TRADERNAMEEQ") || object.getString("TRADERNAMEEQ").equals("")) ? "" : object.getString("TRADERNAMEEQ");
                                            arrEqTrader.add(new RowData(i + 1, traderIdEq, traderNameEq));
                                        }
                                        arrAdapEqTrader = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrEqTrader);
                                        arrAdapEqTrader.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spEqTrader.setAdapter(arrAdapEqTrader);
                                        arrAdapEqTrader.notifyDataSetChanged();
                                    }
                                }

                                //Region Spinner
                                if (response.has("Personal6")) {
                                    arrEqRegion = new ArrayList<>();
                                    arrEqRegion.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayEqRegion = response.getJSONArray("Personal6");
                                    if (jsonArrayEqRegion.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqRegion.length(); i++) {
                                            JSONObject object = jsonArrayEqRegion.getJSONObject(i);
                                            String regionIdEq = (object.isNull("REGIONIDEQ") || object.getString("REGIONIDEQ").equals("")) ? "" : object.getString("REGIONIDEQ");
                                            String regionNameEq = (object.isNull("REGIONNAMEEQ") || object.getString("REGIONNAMEEQ").equals("")) ? "" : object.getString("REGIONNAMEEQ");
                                            arrEqRegion.add(new RowData(i + 1, regionIdEq, regionNameEq));
                                        }
                                        arrAdapEqRegion = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrEqRegion);
                                        arrAdapEqRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spEqRegion.setAdapter(arrAdapEqRegion);
                                        arrAdapEqRegion.notifyDataSetChanged();
                                    }
                                }

                                //Area Spinner
                                if (response.has("Personal8")) {
                                    arrEqArea = new ArrayList<>();
                                    arrEqArea.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayEqArea = response.getJSONArray("Personal8");
                                    if (jsonArrayEqArea.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqArea.length(); i++) {
                                            JSONObject object = jsonArrayEqArea.getJSONObject(i);
                                            String areaIdEq = (object.isNull("AREAIDEQ") || object.getString("AREAIDEQ").equals("")) ? "" : object.getString("AREAIDEQ");
                                            String areaNameEq = (object.isNull("AREANAMEEQ") || object.getString("AREANAMEEQ").equals("")) ? "" : object.getString("AREANAMEEQ");
                                            arrEqArea.add(new RowData(i + 1, areaIdEq, areaNameEq));
                                        }
                                        arrAdapEqArea = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrEqArea);
                                        arrAdapEqArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spEqArea.setAdapter(arrAdapEqArea);
                                        arrAdapEqArea.notifyDataSetChanged();
                                    }
                                }

                                //User Info For Spinner
                                if (response.has("Personal10")) {
                                    JSONArray jsonArrayEqDetail = response.getJSONArray("Personal10");
                                    if (jsonArrayEqDetail.length() != 0) {
                                        for (int i = 0; i < jsonArrayEqDetail.length(); i++) {
                                            JSONObject object = jsonArrayEqDetail.getJSONObject(i);
                                            String eqUserId = (object.isNull("USERID") || object.getString("USERID").equals("")) ? "" : object.getString("USERID");
                                            String eqSegment = (object.isNull("SEGMENT") || object.getString("SEGMENT").equals("")) ? "" : object.getString("SEGMENT");
                                            String eqBranch = (object.isNull("BRANCH") || object.getString("BRANCH").equals("")) ? "" : object.getString("BRANCH");
                                            String eqSubBroker = (object.isNull("SUBBROKER") || object.getString("SUBBROKER").equals("")) ? "" : object.getString("SUBBROKER");
                                            String eqTrader = (object.isNull("TRADER") || object.getString("TRADER").equals("")) ? "" : object.getString("TRADER");
                                            String eqArea = (object.isNull("AREA") || object.getString("AREA").equals("")) ? "" : object.getString("AREA");
                                            String eqRegion = (object.isNull("REGION") || object.getString("REGION").equals("")) ? "" : object.getString("REGION");

                                            /*if (!(arrAdapEqBranch == null)) {
                                                RowData eqBranchData = getSpinnerRowDataById(eqBranch, arrAdapEqBranch);
                                                if (eqBranchData != null) {
                                                    spEqBranchCode.setSelection(eqBranchData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapEqSubBroker == null)) {
                                                RowData eqSubBrokerData = getSpinnerRowDataById(eqSubBroker, arrAdapEqSubBroker);
                                                if (eqSubBrokerData != null) {
                                                    spEqSubBroker.setSelection(eqSubBrokerData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapEqTrader == null)) {
                                                RowData eqTraderData = getSpinnerRowDataById(eqTrader, arrAdapEqTrader);
                                                if (eqTraderData != null) {
                                                    spEqTrader.setSelection(eqTraderData.getPosition());
                                                }
                                            }*/
                                            if (!(arrAdapEqArea == null)) {
                                                RowData eqAreaData = getSpinnerRowDataById(eqArea, arrAdapEqArea);
                                                if (eqAreaData != null) {
                                                    spEqArea.setSelection(eqAreaData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapEqRegion == null)) {
                                                RowData eqRegionData = getSpinnerRowDataById(eqRegion, arrAdapEqRegion);
                                                if (eqRegionData != null) {
                                                    spEqRegion.setSelection(eqRegionData.getPosition());
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (type.equalsIgnoreCase(COMMODITY)) {
                                /*if (response.has("Personal1")) {
                                    arrCommBranch = new ArrayList<>();
                                    arrCommBranch.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayCommBranch = response.getJSONArray("Personal1");
                                    if (jsonArrayCommBranch.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommBranch.length(); i++) {
                                            JSONObject object = jsonArrayCommBranch.getJSONObject(i);
                                            String branchCodeComm = (object.isNull("BRANCH_CODECOMM") || object.getString("BRANCH_CODECOMM").equals("")) ? "" : object.getString("BRANCH_CODECOMM");
                                            String branchComm = (object.isNull("BRANCHCOMM") || object.getString("BRANCHCOMM").equals("")) ? "" : object.getString("BRANCHCOMM");
                                            arrCommBranch.add(new RowData(i + 1, branchCodeComm, branchComm));
                                        }
                                        arrAdapCommBranch = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCommBranch);
                                        arrAdapCommBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCommBranchCode.setAdapter(arrAdapCommBranch);
                                        arrAdapCommBranch.notifyDataSetChanged();
                                    }
                                }*/

                                //SubBroker Spinner
                                if (response.has("Personal3")) {
                                    arrCommSubBroker = new ArrayList<>();
                                    arrCommSubBroker.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayCommSubBroker = response.getJSONArray("Personal3");
                                    if (jsonArrayCommSubBroker.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommSubBroker.length(); i++) {
                                            JSONObject object = jsonArrayCommSubBroker.getJSONObject(i);
                                            String subBrokerIdComm = (object.isNull("SUBBROKERIDCOMM") || object.getString("SUBBROKERIDCOMM").equals("")) ? "" : object.getString("SUBBROKERIDCOMM");
                                            String subBrokerNameComm = (object.isNull("SUBBROKERNAMECOMM") || object.getString("SUBBROKERNAMECOMM").equals("")) ? "" : object.getString("SUBBROKERNAMECOMM");
                                            arrCommSubBroker.add(new RowData(i + 1, subBrokerIdComm, subBrokerNameComm));
                                        }
                                        arrAdapCommSubBroker = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCommSubBroker);
                                        arrAdapCommSubBroker.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCommSubBroker.setAdapter(arrAdapCommSubBroker);
                                        arrAdapCommSubBroker.notifyDataSetChanged();
                                    }
                                }

                                //Trader Spinner
                                if (response.has("Personal5")) {
                                    arrCommTrader = new ArrayList<>();
                                    arrCommTrader.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayCommTrader = response.getJSONArray("Personal5");
                                    if (jsonArrayCommTrader.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommTrader.length(); i++) {
                                            JSONObject object = jsonArrayCommTrader.getJSONObject(i);
                                            String traderIdComm = (object.isNull("TRADERIDCOMM") || object.getString("TRADERIDCOMM").equals("")) ? "" : object.getString("TRADERIDCOMM");
                                            String traderNameComm = (object.isNull("TRADERNAMECOMM") || object.getString("TRADERNAMECOMM").equals("")) ? "" : object.getString("TRADERNAMECOMM");
                                            arrCommTrader.add(new RowData(i + 1, traderIdComm, traderNameComm));
                                        }
                                        arrAdapCommTrader = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCommTrader);
                                        arrAdapCommTrader.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCommTrader.setAdapter(arrAdapCommTrader);
                                        arrAdapCommTrader.notifyDataSetChanged();
                                    }
                                }

                                //Region Spinner
                                if (response.has("Personal7")) {
                                    arrCommRegion = new ArrayList<>();
                                    arrCommRegion.add(new RowData(0, "-1", "Select"));
                                    JSONArray jsonArrayCommRegion = response.getJSONArray("Personal7");
                                    if (jsonArrayCommRegion.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommRegion.length(); i++) {
                                            JSONObject object = jsonArrayCommRegion.getJSONObject(i);
                                            String regionIdComm = (object.isNull("REGIONIDCOMM") || object.getString("REGIONIDCOMM").equals("")) ? "" : object.getString("REGIONIDCOMM");
                                            String regionNameComm = (object.isNull("REGIONNAMECOMM") || object.getString("REGIONNAMECOMM").equals("")) ? "" : object.getString("REGIONNAMECOMM");
                                            arrCommRegion.add(new RowData(i + 1, regionIdComm, regionNameComm));
                                        }
                                        arrAdapCommRegion = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCommRegion);
                                        arrAdapCommRegion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCommRegion.setAdapter(arrAdapCommRegion);
                                        arrAdapCommRegion.notifyDataSetChanged();
                                    }
                                }

                                //Area Spinner
                                if (response.has("Personal9")) {
                                    arrCommArea = new ArrayList<>();
                                    arrCommArea.add(new RowData(0, "-1", "Select"));

                                    JSONArray jsonArrayCommArea = response.getJSONArray("Personal9");
                                    if (jsonArrayCommArea.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommArea.length(); i++) {
                                            JSONObject object = jsonArrayCommArea.getJSONObject(i);
                                            String areaIdComm = (object.isNull("AREAIDCOMM") || object.getString("AREAIDCOMM").equals("")) ? "" : object.getString("AREAIDCOMM");
                                            String areaNameComm = (object.isNull("AREANAMECOMM") || object.getString("AREANAMECOMM").equals("")) ? "" : object.getString("AREANAMECOMM");
                                            arrCommArea.add(new RowData(i + 1, areaIdComm, areaNameComm));
                                        }
                                        arrAdapCommArea = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCommArea);
                                        arrAdapCommArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spCommArea.setAdapter(arrAdapCommArea);
                                        arrAdapCommArea.notifyDataSetChanged();
                                    }
                                }

                                //User Info For Spinner
                                if (response.has("Personal11")) {
                                    JSONArray jsonArrayCommDetail = response.getJSONArray("Personal11");
                                    if (jsonArrayCommDetail.length() != 0) {
                                        for (int i = 0; i < jsonArrayCommDetail.length(); i++) {
                                            JSONObject object = jsonArrayCommDetail.getJSONObject(i);
                                            String commIserId = (object.isNull("USERID") || object.getString("USERID").equals("")) ? "" : object.getString("USERID");
                                            String commSegment = (object.isNull("SEGMENT") || object.getString("SEGMENT").equals("")) ? "" : object.getString("SEGMENT");
                                            String commBranch = (object.isNull("BRANCH") || object.getString("BRANCH").equals("")) ? "" : object.getString("BRANCH");
                                            String commSubBroker = (object.isNull("SUBBROKER") || object.getString("SUBBROKER").equals("")) ? "" : object.getString("SUBBROKER");
                                            String commTrader = (object.isNull("TRADER") || object.getString("TRADER").equals("")) ? "" : object.getString("TRADER");
                                            String commArea = (object.isNull("AREA") || object.getString("AREA").equals("")) ? "" : object.getString("AREA");
                                            String commRegion = (object.isNull("REGION") || object.getString("REGION").equals("")) ? "" : object.getString("REGION");

                                            /*if (!(arrAdapCommBranch == null)) {
                                                RowData commBranchData = getSpinnerRowDataById(commBranch, arrAdapCommBranch);
                                                if (commBranchData != null) {
                                                    spCommBranchCode.setSelection(commBranchData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapCommSubBroker == null)) {
                                                RowData commSubBrokerData = getSpinnerRowDataById(commSubBroker, arrAdapCommSubBroker);
                                                if (commSubBrokerData != null) {
                                                    spCommSubBroker.setSelection(commSubBrokerData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapCommTrader == null)) {
                                                RowData commTraderData = getSpinnerRowDataById(commTrader, arrAdapCommTrader);
                                                if (commTraderData != null) {
                                                    spCommTrader.setSelection(commTraderData.getPosition());
                                                }
                                            }*/
                                            if (!(arrAdapCommArea == null)) {
                                                RowData commAreaData = getSpinnerRowDataById(commArea, arrAdapCommArea);
                                                if (commAreaData != null) {
                                                    spCommArea.setSelection(commAreaData.getPosition());
                                                }
                                            }
                                            if (!(arrAdapCommRegion == null)) {
                                                RowData commRegionData = getSpinnerRowDataById(commRegion, arrAdapCommRegion);
                                                if (commRegionData != null) {
                                                    spCommRegion.setSelection(commRegionData.getPosition());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
                        customProgress.setVisibility(View.GONE);
                    } else {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
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
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
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

    public void updatePersonalDetails() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
//        spEqBranchCode.getSelectedItem().toString();
//        Log.d("HI",((RowData)spEqBranchCode.getSelectedItem()).getKey());
            Map inputMap = new HashMap<>();
//        inputMap.put("_userid", "1450542");
            inputMap.put("_userid", getUserID());
            inputMap.put("_residentialstatus", "Resident Individual");
            inputMap.put("_residentialsubstatus", "Individual Resident");
            inputMap.put("_title", spTitle.getSelectedItem().toString());
            inputMap.put("_fname", etFirstName.getText().toString());
            inputMap.put("_mname", etMiddleName.getText().toString());
            inputMap.put("_lname", etLastName.getText().toString());
            if (rbMale.isChecked()) {
                inputMap.put("_gender", "Male");
            } else if (rbFemale.isChecked()) {
                inputMap.put("_gender", "Female");
            }
            inputMap.put("_relation", spFatherSpouseSel.getSelectedItem().toString());
            if (rbMarried.isChecked()) {
                inputMap.put("_maritalstatus", "Married");
            } else if (rbUnMarried.isChecked()) {
                inputMap.put("_maritalstatus", "Single");
            }
//            inputMap.put("_dob", etDOB.getText().toString());
            Date dob = EkycAppUtil.getDate(etDOB.getText().toString(), "dd/MM/yyyy");
            inputMap.put("_dob", String.valueOf(EkycAppUtil.getDateStr(dob, "yyyy-MM-dd")));
            inputMap.put("_countryofBirth", "");
//            inputMap.put("_countryofBirth", spBirthCountry.getSelectedItem().toString());
//            inputMap.put("_placeofBirth", "");
            inputMap.put("_placeofBirth", etBirthPlace.getText().toString());
//            inputMap.put("_nationality", spNationality.getSelectedItem().toString());
            inputMap.put("_nationality", "");
            inputMap.put("_otherResidence", "");
            inputMap.put("_fhTitle", spFatherSpouseTitle.getSelectedItem().toString());
            inputMap.put("_fhfname", etFatherSpouseFirstName.getText().toString());
            inputMap.put("_fhmname", etFatherSpouseMidName.getText().toString());
            inputMap.put("_fhlname", etFatherSpouseLastName.getText().toString());
            inputMap.put("_mothertitle", spMotherTitle.getSelectedItem().toString());
            inputMap.put("_motherfname", etMotherFirstName.getText().toString());
            inputMap.put("_mothermname", etMotherMiddleName.getText().toString());
            inputMap.put("_motherlname", etMotherLastName.getText().toString());
            if (rbFemale.isChecked() && rbMarried.isChecked()) {
                inputMap.put("_maidentitle", spMaidenTitle.getSelectedItem().toString());
            } else {
                inputMap.put("_maidentitle", "");
            }
            /*if (spMaidenTitle.getSelectedItem().toString().equalsIgnoreCase("Select")) {// in not female and not mrried condition pass blank value
                inputMap.put("_maidentitle", "");
            } else {
                inputMap.put("_maidentitle", spMaidenTitle.getSelectedItem().toString());
            }*/

            inputMap.put("_maidenfname", etMaidenFirstName.getText().toString());
            inputMap.put("_maidenmname", etMaidenMiddleName.getText().toString());
            inputMap.put("_maidenlname", etMaidenLastName.getText().toString());

            inputMap.put("_corresadd1", etBldgNameCorres.getText().toString().trim());
            inputMap.put("_corresadd2", etFlatNoCorres.getText().toString().trim());
            inputMap.put("_corresadd3", etAreaCorres.getText().toString().trim());
//            inputMap.put("_corresaddproof", etAddProofCorres.getText().toString());
            inputMap.put("_corresaddproof", spCorresAddProof.getSelectedItem().toString());
            inputMap.put("_correspincode", etPincodeCorres.getText().toString());
            inputMap.put("_correscountry", spCorresCountry.getSelectedItem().toString());
            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                inputMap.put("_correscity", etCorresCity.getText().toString());
                inputMap.put("_corresstate", etCorresState.getText().toString());
                inputMap.put("_permanentcity", etPermCity.getText().toString());
                inputMap.put("_permanentstate", etPermState.getText().toString());
            } else {
                inputMap.put("_correscity", spCorresCity.getSelectedItem().toString());
                inputMap.put("_corresstate", spCorresState.getSelectedItem().toString());
                inputMap.put("_permanentcity", spPermCity.getSelectedItem().toString());
                inputMap.put("_permanentstate", spPermState.getSelectedItem().toString());
            }
            inputMap.put("_corresmobileno", etMobile.getText().toString());
            inputMap.put("_corresemailid", etEmail.getText().toString());
            inputMap.put("_permanentadd1", etBldgNamePerm.getText().toString().trim());
            inputMap.put("_permanentadd2", etFlatNamePerm.getText().toString().trim());
            inputMap.put("_permanentadd3", etAreaPerm.getText().toString().trim());
            inputMap.put("_permanentpincode", etPincodePerm.getText().toString());
            inputMap.put("_permanentcountry", spPermCountry.getSelectedItem().toString());
            inputMap.put("_permanentaddproof", spPermAddProof.getSelectedItem().toString());
//            inputMap.put("_permanentaddproof", etAddProofPerm.getText().toString());
            if (cbSameAsCorrespondence.isChecked()) {
                inputMap.put("_sameAsCorres", "1");
            } else {
                inputMap.put("_sameAsCorres", "0");
            }
            if (!etSTDNo.getText().toString().isEmpty()) {
                inputMap.put("_correstelresident", etSTDNo.getText().toString() + "~" + etTelNo.getText().toString());
                inputMap.put("_permanenttelresident", etSTDNo.getText().toString() + "~" + etTelNo.getText().toString());
            } else {
                inputMap.put("_correstelresident", "");
                inputMap.put("_permanenttelresident", "");
            }
            inputMap.put("_facebookid", etFbId.getText().toString());
            inputMap.put("_twitterid", etTwitterId.getText().toString());
            inputMap.put("_linkedinid", etLinkedinID.getText().toString());
            inputMap.put("_occupation", spOccupation.getSelectedItem().toString());
            inputMap.put("_education", spQualification.getSelectedItem().toString());
            inputMap.put("_annualIncome", spIncome.getSelectedItem().toString());
            inputMap.put("_netWorth", etNetWorth.getText().toString());
//            inputMap.put("_asOnDate", etNWDate.getText().toString());
            Date asOnDate = EkycAppUtil.getDate(etNWDate.getText().toString(), "dd/MM/yyyy");
            inputMap.put("_asOnDate", String.valueOf(EkycAppUtil.getDateStr(asOnDate, "yyyy-MM-dd")));
            if (rlFamilyDeclaration.getVisibility() == View.VISIBLE && cbFamilyDeclaration.isChecked()) {
                inputMap.put("_familyDeclaration", "1");
            } else {
                inputMap.put("_familyDeclaration", "0");
            }
            if (familyDeclarationChangedFlag) {
                inputMap.put("_familyDeclarationChanged", "Y");
            } else {
                inputMap.put("_familyDeclarationChanged", "N");
            }
            inputMap.put("_GSTNumber", etGST.getText().toString().trim());//GST field
            if (getInterestedInMF()) {
                if (!spMFPEPStatus.getSelectedItem().toString().equals(getResources().getString(R.string.not_applicable))) {
                    if (spMFPEPStatus.getSelectedItem().toString().equals(getResources().getString(R.string.i_am_politically_exposed))) {
                        inputMap.put("_politicallyExposed", "Yes");
                    } else {
                        inputMap.put("_politicallyExposed", "No");
                    }
                    if (spMFPEPStatus.getSelectedItem().toString().equals(getResources().getString(R.string.i_am_related_politically_exposed))) {
                        inputMap.put("_relatedPolitically", "Yes");
                    } else {
                        inputMap.put("_relatedPolitically", "No");
                    }
                }
                inputMap.put("_eqStockexp", null);
                inputMap.put("_derivativeexp", null);
                inputMap.put("_comstocexp", null);
                inputMap.put("_comotherexp", null);
                inputMap.put("_eqintraday", "0");
                inputMap.put("_eqpositional", "0");
                inputMap.put("_eqinvestment", "0");
                inputMap.put("_devtrading", "0");
                inputMap.put("_devhedging", "0");
                inputMap.put("_cominvestment", "0");
                inputMap.put("_comtrading", "0");
                inputMap.put("_comhedging", "0");
                inputMap.put("_comother", "");
                inputMap.put("_contractnote", "Electronic Contract Note (ECN)");
                inputMap.put("_eqpastaction", "");
                inputMap.put("_compastaction", "");
                inputMap.put("_Commercial", "0");
                inputMap.put("_Commercial_Bullion", "0");
                inputMap.put("_Commercial_Basemetals", "0");
                inputMap.put("_Commercial_Energy", "0");
                inputMap.put("_Commercial_AgriCommodities", "0");
                inputMap.put("_NONCommercial", "0");
                inputMap.put("_NONCommercial_Bullion", "0");
                inputMap.put("_NONCommercial_Basemetals", "0");
                inputMap.put("_NONCommercial_Energy", "0");
                inputMap.put("_NONCommercial_AgriCommodities", "0");
               /* if (toggleRegWithBroker.isChecked()) {
                    inputMap.put("_brokername", etBrokerName.getText().toString());
                    inputMap.put("_brokerclientcode", etClientCode.getText().toString());
                } else  {
                    inputMap.put("_brokername", "");
                    inputMap.put("_brokerclientcode", "");
                }*/
                inputMap.put("_panno", etPAN.getText().toString());
//                inputMap.put("_aadharno", removeExponential(etAadhar.getText().toString()));
                inputMap.put("_aadharno", etAadhar.getText().toString());
                if (!spMFPEPStatus.getSelectedItem().toString().equals(getResources().getString(R.string.not_applicable))) {
                    inputMap.put("PEPRemark", etMFPEPRemark.getText().toString());
                } else if (spMFPEPStatus.getSelectedItem().toString().equals(getResources().getString(R.string.not_applicable))) {
                    inputMap.put("PEPRemark", "");
                }
                inputMap.put("_altMobileNo", etAltTelNo.getText().toString());
                inputMap.put("_altEmail", etAltEmailID.getText().toString());
                inputMap.put("_stockPortFolio", "");
            } else {
                inputMap.put("_aadharno", etAadhar.getText().toString());
                inputMap.put("_stockPortFolio", spStockPortfolio.getSelectedItem().toString());
                if (togglePoliticaly.isChecked()) {
                    inputMap.put("_politicallyExposed", "Yes");
                } else {
                    inputMap.put("_politicallyExposed", "No");
                }
                if (toggleRelatedPolitical.isChecked()) {
                    inputMap.put("_relatedPolitically", "Yes");
                } else {
                    inputMap.put("_relatedPolitically", "No");
                }
                if (getIsEquitySelected()) {
                    inputMap.put("_eqStockexp", spEqExperience.getSelectedItem().toString());
                    inputMap.put("_derivativeexp", spEqDerivativeExp.getSelectedItem().toString());
                } else {
                    inputMap.put("_eqStockexp", null);
                    inputMap.put("_derivativeexp", null);
                }
                if (getIsCommoditySelected()) {
                    inputMap.put("_comstocexp", spCommExperience.getSelectedItem().toString());
                    inputMap.put("_comotherexp", spCommOtherExp.getSelectedItem().toString());
                } else {
                    inputMap.put("_comstocexp", null);
                    inputMap.put("_comotherexp", null);
                }
                if (llDealingWithEquity.getVisibility() == View.VISIBLE && cbEqIntraday.isChecked()) {
                    inputMap.put("_eqintraday", "1");
                } else {
                    inputMap.put("_eqintraday", "0");
                }
                if (llDealingWithEquity.getVisibility() == View.VISIBLE && cbEqPositional.isChecked()) {
                    inputMap.put("_eqpositional", "1");
                } else {
                    inputMap.put("_eqpositional", "0");
                }
                if (llDealingWithEquity.getVisibility() == View.VISIBLE && cbEqInvestment.isChecked()) {
                    inputMap.put("_eqinvestment", "1");
                } else {
                    inputMap.put("_eqinvestment", "0");
                }
                if (llDealWithDeri.getVisibility() == View.VISIBLE && cbDevTrading.isChecked()) {
                    inputMap.put("_devtrading", "1");
                } else {
                    inputMap.put("_devtrading", "0");
                }
                if (llDealWithDeri.getVisibility() == View.VISIBLE && cbDevHedging.isChecked()) {
                    inputMap.put("_devhedging", "1");
                } else {
                    inputMap.put("_devhedging", "0");
                }
                if (llDealWithComm.getVisibility() == View.VISIBLE && cbCommInvestment.isChecked()) {
                    inputMap.put("_cominvestment", "1");
                } else {
                    inputMap.put("_cominvestment", "0");
                }
                if (llDealWithComm.getVisibility() == View.VISIBLE && cbCommTrading.isChecked()) {
                    inputMap.put("_comtrading", "1");
                } else {
                    inputMap.put("_comtrading", "0");
                }
                if (llDealWithComm.getVisibility() == View.VISIBLE && cbCommHedging.isChecked()) {
                    inputMap.put("_comhedging", "1");
                } else {
                    inputMap.put("_comhedging", "0");
                }
                if (llDealWithComm.getVisibility() == View.VISIBLE && cbCommOther.isChecked()) {
                    inputMap.put("_comother", etCommOther.getText().toString());
                } else {
                    inputMap.put("_comother", "");
                }
                //new Client categorization feilds
                if (llCategorySelection.getVisibility() == View.VISIBLE && cbCommercial.isChecked()) {
                    inputMap.put("_Commercial", "1");
                } else {
                    inputMap.put("_Commercial", "0");
                }
                if (llCategorySelection.getVisibility() == View.VISIBLE && cbCommercialBullion.isChecked()) {
                    inputMap.put("_Commercial_Bullion", "1");
                } else {
                    inputMap.put("_Commercial_Bullion", "0");
                }
                if (llCategorySelection.getVisibility() == View.VISIBLE && cbCommercialBaseMetals.isChecked()) {
                    inputMap.put("_Commercial_Basemetals", "1");
                } else {
                    inputMap.put("_Commercial_Basemetals", "0");
                }
                if (llCategorySelection.getVisibility() == View.VISIBLE && cbCommercialEnergy.isChecked()) {
                    inputMap.put("_Commercial_Energy", "1");
                } else {
                    inputMap.put("_Commercial_Energy", "0");
                }
                if (llCategorySelection.getVisibility() == View.VISIBLE && cbCommercialAgriCommodities.isChecked()) {
                    inputMap.put("_Commercial_AgriCommodities", "1");
                } else {
                    inputMap.put("_Commercial_AgriCommodities", "0");
                }
                if (llCategorySelection.getVisibility() == View.VISIBLE && cbNonCommercial.isChecked()) {
                    inputMap.put("_NONCommercial", "1");
                } else {
                    inputMap.put("_NONCommercial", "0");
                }
                if (llCategorySelection.getVisibility() == View.VISIBLE && cbNonCommercialBullion.isChecked()) {
                    inputMap.put("_NONCommercial_Bullion", "1");
                } else {
                    inputMap.put("_NONCommercial_Bullion", "0");
                }
                if (llCategorySelection.getVisibility() == View.VISIBLE && cbNonCommercialBaseMetals.isChecked()) {
                    inputMap.put("_NONCommercial_Basemetals", "1");
                } else {
                    inputMap.put("_NONCommercial_Basemetals", "0");
                }
                if (llCategorySelection.getVisibility() == View.VISIBLE && cbNonCommercialEnergy.isChecked()) {
                    inputMap.put("_NONCommercial_Energy", "1");
                } else {
                    inputMap.put("_NONCommercial_Energy", "0");
                }
                if (llCategorySelection.getVisibility() == View.VISIBLE && cbNonCommercialAgriCommodities.isChecked()) {
                    inputMap.put("_NONCommercial_AgriCommodities", "1");
                } else {
                    inputMap.put("_NONCommercial_AgriCommodities", "0");
                }

                inputMap.put("_contractnote", "Electronic Contract Note (ECN)");
                if (toggleSEBIAuthority.isChecked()) {
                    inputMap.put("_eqpastaction", etSebiAuthority.getText().toString());
                } else {
                    inputMap.put("_eqpastaction", "");
                }
                if (toggleFmcSebiAuthority.isChecked()) {
                    inputMap.put("_compastaction", etFmcSebiAuthority.getText().toString());
                } else {
                    inputMap.put("_compastaction", "");
                }
                if (toggleRegWithBroker.isChecked()) {
                    inputMap.put("_brokername", etBrokerName.getText().toString());
                    inputMap.put("_brokerclientcode", etClientCode.getText().toString());
                } else {
                    inputMap.put("_brokername", "");
                    inputMap.put("_brokerclientcode", "");
                }
            }
            //Spinner data for PP
            if (getAppType().equalsIgnoreCase(PP)) {
                if (getIsEquitySelected() && getIsCommoditySelected()) {
                    if (arrAdapEqBranch == null) {
                        inputMap.put("_branch", "");
                    } else {
                        inputMap.put("_branch", ((RowData) spEqBranchCode.getSelectedItem()).getKey());
                    }
                    if (arrAdapEqSubBroker == null) {
                        inputMap.put("_subBrokerEq", "");
                    } else {
                        inputMap.put("_subBrokerEq", ((RowData) spEqSubBroker.getSelectedItem()).getKey());
                    }
                    if (arrAdapEqTrader == null) {
                        inputMap.put("_traderEq", "");
                    } else {
                        inputMap.put("_traderEq", ((RowData) spEqTrader.getSelectedItem()).getKey());
                    }
                    if (arrAdapEqArea == null) {
                        inputMap.put("_areaEq", "");
                    } else {
                        inputMap.put("_areaEq", ((RowData) spEqArea.getSelectedItem()).getKey());
                    }
                    if (arrAdapEqRegion == null) {
                        inputMap.put("_regionEq", "");
                    } else {
                        inputMap.put("_regionEq", ((RowData) spEqRegion.getSelectedItem()).getKey());
                    }
                    if (arrAdapCommSubBroker == null) {
                        inputMap.put("_subBrokerComm", "");
                    } else {
                        inputMap.put("_subBrokerComm", ((RowData) spCommSubBroker.getSelectedItem()).getKey());
                    }
                    if (arrAdapCommTrader == null) {
                        inputMap.put("_traderComm", "");
                    } else {
                        inputMap.put("_traderComm", ((RowData) spCommTrader.getSelectedItem()).getKey());
                    }
                    if (arrAdapCommArea == null) {
                        inputMap.put("_areaComm", "");
                    } else {
                        inputMap.put("_areaComm", ((RowData) spCommArea.getSelectedItem()).getKey());
                    }
                    if (arrAdapCommRegion == null) {
                        inputMap.put("_regionComm", "");
                    } else {
                        inputMap.put("_regionComm", ((RowData) spCommRegion.getSelectedItem()).getKey());
                    }
                    inputMap.put("_eqSegment", "1");
                    inputMap.put("_commSegment", "1");
                } else if (getIsEquitySelected() || getInterestedInMF()) {
                    if (arrAdapEqBranch == null) {
                        inputMap.put("_branch", "");
                    } else {
                        inputMap.put("_branch", ((RowData) spEqBranchCode.getSelectedItem()).getKey());
                    }
                    if (arrAdapEqSubBroker == null) {
                        inputMap.put("_subBrokerEq", "");
                    } else {
                        inputMap.put("_subBrokerEq", ((RowData) spEqSubBroker.getSelectedItem()).getKey());
                    }
                    if (arrAdapEqTrader == null) {
                        inputMap.put("_traderEq", "");
                    } else {
                        inputMap.put("_traderEq", ((RowData) spEqTrader.getSelectedItem()).getKey());
                    }
                    if (arrAdapEqArea == null) {
                        inputMap.put("_areaEq", "");
                    } else {
                        inputMap.put("_areaEq", ((RowData) spEqArea.getSelectedItem()).getKey());
                    }
                    if (arrAdapEqRegion == null) {
                        inputMap.put("_regionEq", "");
                    } else {
                        inputMap.put("_regionEq", ((RowData) spEqRegion.getSelectedItem()).getKey());
                    }
                    inputMap.put("_subBrokerComm", "");
                    inputMap.put("_traderComm", "");
                    inputMap.put("_areaComm", "");
                    inputMap.put("_regionComm", "");
                    inputMap.put("_eqSegment", "1");
                    inputMap.put("_commSegment", "0");
                } else if (getIsCommoditySelected()) {
                    if (arrAdapCommBranch == null) {
                        inputMap.put("_branch", "");
                    } else {
                        inputMap.put("_branch", ((RowData) spCommBranchCode.getSelectedItem()).getKey());
                    }
                    if (arrAdapCommSubBroker == null) {
                        inputMap.put("_subBrokerComm", "");
                    } else {
                        inputMap.put("_subBrokerComm", ((RowData) spCommSubBroker.getSelectedItem()).getKey());
                    }
                    if (arrAdapCommTrader == null) {
                        inputMap.put("_traderComm", "");
                    } else {
                        inputMap.put("_traderComm", ((RowData) spCommTrader.getSelectedItem()).getKey());
                    }
                    if (arrAdapCommArea == null) {
                        inputMap.put("_areaComm", "");
                    } else {
                        inputMap.put("_areaComm", ((RowData) spCommArea.getSelectedItem()).getKey());
                    }
                    if (arrAdapCommRegion == null) {
                        inputMap.put("_regionComm", "");
                    } else {
                        inputMap.put("_regionComm", ((RowData) spCommRegion.getSelectedItem()).getKey());
                    }
                    inputMap.put("_subBrokerEq", "");
                    inputMap.put("_traderEq", "");
                    inputMap.put("_areaEq", "");
                    inputMap.put("_regionEq", "");
                    inputMap.put("_eqSegment", "0");
                    inputMap.put("_commSegment", "1");
                }
            } else {
                inputMap.put("_branch", "");
                inputMap.put("_subBrokerEq", "");
                inputMap.put("_traderEq", "");
                inputMap.put("_areaEq", "");
                inputMap.put("_regionEq", "");
                inputMap.put("_subBrokerComm", "");
                inputMap.put("_traderComm", "");
                inputMap.put("_areaComm", "");
                inputMap.put("_regionComm", "");
                inputMap.put("_eqSegment", "0");
                inputMap.put("_commSegment", "0");
            }
            if (getAppType().equalsIgnoreCase(PP)) {
                inputMap.put("_clientType", PP);
                inputMap.put("_AppLoginId", getAppLoginID());
            } else if (getAppType().equalsIgnoreCase(DIRECT)) {
                inputMap.put("_clientType", DIRECT);
            } else if (getAppType().equalsIgnoreCase(CRM)) {
                inputMap.put("_clientType", CRM);
                inputMap.put("_AppLoginId", getAppLoginID());
            }
//            set segmentStr
            String segmentStr = "";
            if (getIsEquitySelected()) {
                if (segmentStr.equals("")) {
                    segmentStr = segmentStr + Equity;
                } else {
                    segmentStr = segmentStr + "," + Equity;
                }
            }
            if (getIsCommoditySelected()) {
                if (segmentStr.equals("")) {
                    segmentStr = segmentStr + Commodity;
                } else {
                    segmentStr = segmentStr + "," + Commodity;
                }
            }
            if (getIsDerivativeSelected()) {
                if (segmentStr.equals("")) {
                    segmentStr = segmentStr + Derivative;
                } else {
                    segmentStr = segmentStr + "," + Derivative;
                }
            }
            if (getIsCurrencySelected()) {
                if (segmentStr.equals("")) {
                    segmentStr = segmentStr + Currency;
                } else {
                    segmentStr = segmentStr + "," + Currency;
                }
            }
            if (getInterestedInMF() || isMfSegmentExist()) {
                if (segmentStr.equals("")) {
                    segmentStr = segmentStr + MF;
                } else {
                    segmentStr = segmentStr + "," + MF;
                }
            }
            inputMap.put("_segmentList", segmentStr);

            String flag = "";
            if (getInterestedInMF()) {
                flag = UPDATEPERSONALDETAILS_MF;
            } else {
                flag = UPDATEPERSONALDETAILS;
            }

            String category = PERSONAL;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            if (response.has("Personal")) {
                                JSONArray jsonArray = response.getJSONArray("Personal");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String status = (object.isNull("STATUS") || object.getString("STATUS").equals("")) ? "" : object.getString("STATUS");
                                        String msg = (object.isNull("Message") || object.getString("Message").equals("")) ? "" : object.getString("Message");
                                        if (status.equalsIgnoreCase("1")) {//Success
                                            //vpApply.setCurrentItem(1);
                                            setGotoReviewEdit(false);
                                            setGotoFundTransfer(false);
                                            setIsPersonalDetailUpdated(true);
                                            setIsSegmentSelected(true);
                                            setIsPersonalDetailDone(true);
                                            setIsWorkDetailDone(true);

                                            //set flag for bank layout
                                            if (getIsEquitySelected()) {
                                                setIsEquityOriginal(true);
                                            } else {
                                                setIsEquityOriginal(false);
                                            }
                                            if (getIsCommoditySelected()) {
                                                setIsCommodityOriginal(true);
                                            } else {
                                                setIsCommodityOriginal(false);
                                            }
                                            if (getIsCurrencySelected()) {
                                                setIsCurrencyOriginal(true);
                                            } else {
                                                setIsCurrencyOriginal(false);
                                            }
                                            if (getIsDerivativeSelected()) {
                                                setIsDerivativeOriginal(true);
                                            } else {
                                                setIsDerivativeOriginal(false);
                                            }
                                            if (getIsAllSelected()) {
                                                setIsAllOriginal(true);
                                            } else {
                                                setIsAllOriginal(false);
                                            }
                                            tabPageIndicator.disableTab(getUploadTab());
                                            tabPageIndicator.disableTab(getEsignTab());
                                            if (isNomineeTabApplicable()) {
                                                tabPageIndicator.enableTab(getDematTab());
                                                viewPager.setCurrentItem(getDematTab(), false);
                                            } else {
                                                tabPageIndicator.enableTab(getBankTab());
                                                viewPager.setCurrentItem(getBankTab(), false);
                                            }
                                            MOSLCommon.removeKeyboard(mContext, btnNext);
//                                            Intent intent = new Intent(mContext, EkycMainActivity.class);
//                                            intent.putExtra(TAB_DETAIL, BANK_DETAIL);
//                                            startActivity(intent);
                                            //finish();

                                        } else if (status.equalsIgnoreCase("2")) {//Email exists
                                            if (getAppType().equalsIgnoreCase(DIRECT)) {
                                                etEmail.setText("");
                                                etEmail.requestFocus();
                                                etEmail.setEnabled(true);
                                                MOSLCommon.removeKeyboard(mContext, btnNext);
                                                commonMsgDialog(mContext, msg, false, "");
                                            } else {
                                                familyDeclarationPopup(status, "", msg + "\n" + getResources().getString(R.string.family_declaration_popup));
                                            }
                                        } else if (status.equalsIgnoreCase("3")) {//mobile exists
                                            if (getAppType().equalsIgnoreCase(DIRECT)) {
                                                etMobile.setText("");
                                                etMobile.requestFocus();
                                                etMobile.setEnabled(true);
                                                MOSLCommon.removeKeyboard(mContext, btnNext);
                                                commonMsgDialog(mContext, msg, false, "");
                                            } else {
                                                familyDeclarationPopup(status, "", msg + "\n" + getResources().getString(R.string.family_declaration_popup));
                                            }
                                        } else if (status.equalsIgnoreCase("4")) {//Email and mobile both exists
                                            if (getAppType().equalsIgnoreCase(DIRECT)) {
                                                etEmail.setText("");
                                                etMobile.setText("");
                                                etEmail.requestFocus();
                                                etEmail.setEnabled(true);
                                                etMobile.setEnabled(true);
                                                MOSLCommon.removeKeyboard(mContext, btnNext);
                                                commonMsgDialog(mContext, msg, false, "");
                                            } else {
                                                familyDeclarationPopup(status, "", msg + "\n" + getResources().getString(R.string.family_declaration_popup));
                                            }
                                        } else {
                                            setIsPersonalDetailUpdated(false);
                                            setIsSegmentSelected(false);
                                            setIsPersonalDetailDone(false);
                                            setIsWorkDetailDone(false);
                                            commonMsgDialog(mContext, msg, false, "");
                                        }
                                        hideUpdateProgress();
                                    }
                                }
                            } else {
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
//                                alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            customProgress.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                            alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                        }
                        customProgress.setVisibility(View.GONE);
                    } else {
                        customProgress.setVisibility(View.GONE);
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
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
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                    Log.e("Failure", "no resp in Data");
                    customProgress.setVisibility(View.GONE);
                    if (statusCode == 0) {
                        CustomDialog customDialog = new CustomDialog(mContext, "Connection time out, Please try again later.", true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                            @Override
                            public void dialogAction(MOSLCommon.Action action, Object... data) {

                            }
                        });
                        customDialog.show();
                    } else {

                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
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

    private void getSpinnerData(ArrayAdapter<String> arrayAdapter, Spinner spinner, String string) {
        int spPosition = arrayAdapter.getPosition(string);
        spinner.setSelection(spPosition);
    }

    public void submitDialog(String val, final String message) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);

        if (val.equalsIgnoreCase(" ")) {
//            tvMsg.setText("Data Updated");
        } else {
            tvMsg.setText(message);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

   /* public void allSelected() {
        if (cbDerivative.isChecked() *//*&& cbCommodity.isChecked()*//*) {
            cbAll.setChecked(true);

          //  cbCommodity.setChecked(true);
//            cbCurrency.setChecked(false);
            cbDerivative.setChecked(true);
        }

        if (cbDerivative.isChecked()) {
            equityIsSelected = true;
            derivativeIsSelected = true;

            commodityIsSelected = false;
            allIsSelected = false;
        }

       *//* if (cbCommodity.isChecked()) {
            commodityIsSelected = true;

            equityIsSelected = false;
            allIsSelected = false;
            derivativeIsSelected = false;
        }

        if (cbCommodity.isChecked()) {
            allIsSelected = true;

            equityIsSelected = false;
            derivativeIsSelected = false;
            commodityIsSelected = false;
        }*//*
    }*/

    public void editTextDisable() {
        etFirstName.setEnabled(false);
        etMiddleName.setEnabled(false);
        etLastName.setEnabled(false);
        etBldgNameCorres.setEnabled(false);
        etFlatNoCorres.setEnabled(false);
        etAreaCorres.setEnabled(false);
//        etAddProofCorres.setEnabled(false);
        spCorresAddProof.setEnabled(false);
        etPincodeCorres.setEnabled(false);
        etBldgNamePerm.setEnabled(false);
        etFlatNamePerm.setEnabled(false);
        etAreaPerm.setEnabled(false);
//        etAddProofPerm.setEnabled(false);
        spPermAddProof.setEnabled(false);
        etPincodePerm.setEnabled(false);
//        spTitle.setEnabled(false);
    }

    public void editTextEnable() {
        etFirstName.setEnabled(true);
        etMiddleName.setEnabled(true);
        etLastName.setEnabled(true);
        etBldgNameCorres.setEnabled(true);
        etFlatNoCorres.setEnabled(true);
        etAreaCorres.setEnabled(true);
//        etAddProofCorres.setEnabled(true);
        spCorresAddProof.setEnabled(true);
        etPincodeCorres.setEnabled(true);
        etBldgNamePerm.setEnabled(true);
        etFlatNamePerm.setEnabled(true);
        etAreaPerm.setEnabled(true);
//        etAddProofPerm.setEnabled(true);
        spPermAddProof.setEnabled(true);
        etPincodePerm.setEnabled(true);
//        spTitle.setEnabled(true);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tvMobile) {
            removeKeyboard(mContext, getView());
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.popup_change_email_mobile);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(true);
            final EditText etChangeDetail = (EditText) dialog.findViewById(R.id.etChangeDetail);
            final EditText etError = (EditText) dialog.findViewById(R.id.etError);

            etChangeDetail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!isBlank(etChangeDetail.getText().toString())) {
                        if (!ValidationRule.validateMobile(etChangeDetail.getText().toString())) {
                            etError.setVisibility(View.VISIBLE);
                            etError.setText("Invalid mobile no.");
                            etChangeDetail.requestFocus();
                        } else {
                            etError.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
            etChangeDetail.setHint("Enter Your Mobile No.");
            etChangeDetail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            etChangeDetail.setInputType(InputType.TYPE_CLASS_NUMBER);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ValidationRule.validateMobile(etChangeDetail.getText().toString())) {
                        removeKeyboard(mContext, dialog.getCurrentFocus());
                        etError.setVisibility(View.GONE);
                        if (getAppType().equalsIgnoreCase(DIRECT)) {
                            checkMobileEmail("Mobile", etChangeDetail.getText().toString());
                        } else {
                            etMobile.setText(etChangeDetail.getText().toString());
                            showFamilyDetailLayout(false);
                        }
                        dialog.dismiss();
                    } else if (!ValidationRule.validateMobile(etChangeDetail.getText().toString())) {
                        etError.setVisibility(View.VISIBLE);
                        etError.setText("Enter mobile no.");
                    }
                }
            });
            dialog.show();
        } else if (i == R.id.tvEmail) {
            removeKeyboard(mContext, getView());
            final Dialog dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.popup_change_email_mobile);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(true);
            final EditText etChangeDetail = (EditText) dialog.findViewById(R.id.etChangeDetail);
            Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);

            final EditText etError = (EditText) dialog.findViewById(R.id.etError);
            etChangeDetail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!isBlank(etChangeDetail.getText().toString())) {
                        if (etChangeDetail.length() == 0) {
                            etError.setVisibility(View.VISIBLE);
                            etError.setText("Enter email id");
                        } else if (!isValidEmail(etChangeDetail.getText())) {
                            etError.setVisibility(View.VISIBLE);
                            etError.setText("Email is not valid");
                        } else {
                            //  removeKeyboard(mContext, dialog.getCurrentFocus());
                            etError.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etChangeDetail.setHint("Enter Your Email.");
            etChangeDetail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isValidEmail(etChangeDetail.getText())) {
                        removeKeyboard(mContext, dialog.getCurrentFocus());
                        if (getAppType().equalsIgnoreCase(DIRECT)) {
                            checkMobileEmail("Email", etChangeDetail.getText().toString());
                        } else {
                            etEmail.setText(etChangeDetail.getText().toString());
                            showFamilyDetailLayout(false);
                        }
                        dialog.dismiss();
                    } else {
                        etError.setVisibility(View.VISIBLE);
                        etError.setText("Email is not valid");
                    }
                }
            });
            dialog.show();
        } else if (i == R.id.etDOB) {
            removeKeyboard(mContext, getView());
            try {
                if (!isBlank(etDOB.getText().toString())) {
                    CustomDatePickerDialog dobDatePickernew = new CustomDatePickerDialog(mContext, false, etDOB.getText().toString(), true, new DatePickerListener() {
                        @Override
                        public void submitDatePicker(String date) {
                            Calendar minAdultAge = new GregorianCalendar();
                            minAdultAge.add(Calendar.YEAR, -18);
                            Date dob = EkycAppUtil.getDate(date, "dd/MM/yyyy");
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                            if (minAdultAge.before(newDate)) {
                                commonMsgDialog(mContext, getResources().getString(R.string.minor_age_bracket_note), false, "");
                            } else {
                                etDOB.setText(date);
                            }
                        }
                    });
                    dobDatePickernew.show();


                    //Date dob = EkycAppUtil.getDate(etDOB.getText().toString(), "dd/MM/yyyy");
                    //fromDatePickerDialog.updateDate(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                } else {
                    String adultDate = getAdultDate("dd/MM/yyyy");

                    CustomDatePickerDialog defaultDate = new CustomDatePickerDialog(mContext, false, adultDate, true, new DatePickerListener() {
                        @Override
                        public void submitDatePicker(String date) {
                            Calendar minAdultAge = new GregorianCalendar();
                            minAdultAge.add(Calendar.YEAR, -18);
                            Date dob = EkycAppUtil.getDate(date, "dd/MM/yyyy");
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                            if (minAdultAge.before(newDate)) {
                                commonMsgDialog(mContext, getResources().getString(R.string.minor_age_bracket_note), false, "");
                            } else {
                                etDOB.setText(date);
                            }
                        }
                    });
                    defaultDate.show();

                    //Date dob = EkycAppUtil.getDate(adultDate, "dd/MM/yyyy");
                    //fromDatePickerDialog.updateDate(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //  fromDatePickerDialog.show();
               /* MOSLCommon.showDatePickerDialog(mContext);
                etDOB.setText(MOSLCommon.date_1);*/
            DOBClicked = true;
            NWClicked = false;
            removeKeyboard(mContext, etDOB);
        } else if (i == R.id.etNWDate) {
            removeKeyboard(mContext, getView());
            etNetWorth.clearFocus();
            try {
                if (!isBlank(etNWDate.getText().toString())) {
                    CustomDatePickerDialog networthDatePicker = new CustomDatePickerDialog(mContext, false, etNWDate.getText().toString(), true, new DatePickerListener() {
                        @Override
                        public void submitDatePicker(String date) {
                            /*dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                            Date dob = EkycAppUtil.getDate(date, "dd/MM/yyyy");
                            Calendar newDate = Calendar.getInstance();
                            Calendar minNetworthDate = new GregorianCalendar();
                            Calendar maxNetworthDate = new GregorianCalendar();
                            newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));

                            Date currentDate = EkycAppUtil.getDate(dateFormatter.format(newDate.getTime()), "dd/MM/yyyy");//curent date
                            String financial = getStartDtFinancialYear("dd/MM/yyyy");//get Financial date
                            Date financialDate = EkycAppUtil.getDate(financial, "dd/MM/yyyy");//financial date
                            minNetworthDate.add(Calendar.YEAR, -1);//condition for future date
                            maxNetworthDate.add(Calendar.DAY_OF_MONTH, 0);//previous date 1 year

                            if (currentDate.before(financialDate) || minNetworthDate.after(newDate)) {
                                commonMsgDialog(mContext, Date_should_not_be_less_than_one_year, false, "");
                            } else {
                                etNWDate.setText(date);
                                if (!MOSLCommon.isFutureDate(etNWDate.getText().toString()) || !MOSLCommon.isFinacialDate(etNWDate.getText().toString())) {
                                    etNWDateError.setVisibility(View.GONE);
                                }
                            }*/

                            if (!isDateIWthinYear(date)) {
                                commonMsgDialog(mContext, Date_should_not_be_less_than_one_year, false, "");
                            } else {
                                etNWDate.setText(date);
                                etNWDateError.setVisibility(View.GONE);
                            }


                        }
                    });
                    networthDatePicker.show();

                } else {
                    CustomDatePickerDialog networthDatePicker = new CustomDatePickerDialog(mContext, true, "", true, new DatePickerListener() {
                        @Override
                        public void submitDatePicker(String date) {
                           /* dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                            Date dob = EkycAppUtil.getDate(date, "dd/MM/yyyy");
                            Calendar newDate = Calendar.getInstance();
                            Calendar minNetworthDate = new GregorianCalendar();
                            Calendar maxNetworthDate = new GregorianCalendar();
                            newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));

                            Date currentDate = EkycAppUtil.getDate(dateFormatter.format(newDate.getTime()), "dd/MM/yyyy");//curent date
                            String financial = getStartDtFinancialYear("dd/MM/yyyy");//get Financial date
                            Date financialDate = EkycAppUtil.getDate(financial, "dd/MM/yyyy");//financial date
                            minNetworthDate.add(Calendar.YEAR, -1);//condition for future date
                            maxNetworthDate.add(Calendar.DAY_OF_MONTH, 0);//previous date 1 year

                            if (currentDate.before(financialDate) || minNetworthDate.after(newDate)) {
                                commonMsgDialog(mContext, Date_should_not_be_less_than_one_year, false, "");
                            } else {
                                etNWDate.setText(date);
                                if (!MOSLCommon.isFutureDate(etNWDate.getText().toString()) || !MOSLCommon.isFinacialDate(etNWDate.getText().toString())) {
                                    etNWDateError.setVisibility(View.GONE);
                                }
                            }
*/

                            if (!isDateIWthinYear(date)) {
                                commonMsgDialog(mContext, Date_should_not_be_less_than_one_year, false, "");
                            } else {
                                etNWDate.setText(date);
//                                if (!MOSLCommon.isFutureDate(etNWDate.getText().toString()) || !MOSLCommon.isFinacialDate(etNWDate.getText().toString())) {
                                etNWDateError.setVisibility(View.GONE);
//                                }
                            }

                        }
                    });
                    networthDatePicker.show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //fromDatePickerDialog.show();
               /* MOSLCommon.showDatePickerDialog(mContext);
                etNWDate.setText(MOSLCommon.date_1);*/
            DOBClicked = false;
            NWClicked = true;
            removeKeyboard(mContext, etNWDate);
        } else if (i == R.id.btnNext) {
            removeKeyboard(mContext, getView());
            validateData();
        } else if (i == R.id.ivNote) {
            removeKeyboard(mContext, getView());
            dialogDocList = new Dialog(mContext);
            dialogDocList.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogDocList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogDocList.setCanceledOnTouchOutside(false);

            dialogDocList.getWindow().setWindowAnimations(R.style.animationName);
            dialogDocList.setContentView(R.layout.dialog_doc_list);
            dialogDocList.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogDocList.setCanceledOnTouchOutside(false);

            Button btnAgree = (Button) dialogDocList.findViewById(R.id.btnAgree);
            TextView tvHeading = (TextView) dialogDocList.findViewById(R.id.tvHeading);
            TextView tvHeading1 = (TextView) dialogDocList.findViewById(R.id.tvHeading1);

            tvHeading1.setText(getResources().getString(R.string.segment_note));
            tvHeading.setText("Note");

            btnAgree.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialogDocList.dismiss();

                }
            });

            ImageView btnClose = (ImageView) dialogDocList.findViewById(R.id.btnClose);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDocList.dismiss();
                }
            });
            dialogDocList.show();

//            alertDialog(getActivity(), 0, "Note", getResources().getString(R.string.segment_note), "OK", true, null);

          /*  CustomDialog noteDialog = new CustomDialog(getActivity(), getResources().getString(R.string.segment_note), true, false, true, "Note", OK, "", new CustomDialog.CustomDialogListener() {
                @Override
                public void dialogAction(MOSLCommon.Action action, Object... data) {

                }
            });
            noteDialog.show();*/
        } else if (i == R.id.tvFatcaDeclaration) {
            removeKeyboard(mContext, getView());
//            alertDialog(getActivity(), 0, "Declaration", getResources().getString(R.string.fatca_declaration), "OK", true, null);

            CustomDialog declarationDialog = new CustomDialog(getActivity(), getResources().getString(R.string.fatca_declaration), true, false, true, "Declaration", OK, "", new CustomDialog.CustomDialogListener() {
                @Override
                public void dialogAction(MOSLCommon.Action action, Object... data) {

                }
            });
            declarationDialog.show();
        } else if (i == R.id.cbEquity) {
            if (isOffline() && isOfflineDocumentUploadedDone()) {
                if (!getIsEquitySelected()) {
                    commonMsgDialog(mContext, getResources().getString(R.string.offline_segment_msg), false, "");
                }
            }
            if (isOffline() ? isOfflineDocumentUploadedDone() : isEquitySegmentExist() || isEquityFundTransfered()) {
                return;
            }

            removeKeyboard(mContext, getView());
            cbEquity.setEnabled(true);
            if (getIsEquitySelected()) {
                cbEquity.setImageResource(R.drawable.ic_equity_dselect);
                cbDerivative.setImageResource(R.drawable.ic_derivative_dselect);
                cbCurrency.setImageResource(R.drawable.ic_currency_dselect);
                setIsEquitySelected(false);
                setIsDerivativeSelected(false);
                setIsCurrencySelected(false);
                deSelectAll();
                equityDeselectSpinner();
                equityDeselect();
            } else {
                cbEquity.setImageResource(R.drawable.ic_equity_select);
                setIsEquitySelected(true);
                equitySpinner();
                equitySelect();
            }
            disableEnableField();
        } else if (i == R.id.cbCommodity) {
            if (isOffline() && isOfflineDocumentUploadedDone()) {
                if (!getIsCommoditySelected()) {
                    commonMsgDialog(mContext, getResources().getString(R.string.offline_segment_msg), false, "");
                }
            }
            if (isOffline() ? isOfflineDocumentUploadedDone() : isCommoditySegmentExist() || isCommodityFundTransfered()) {
                return;
            }

            removeKeyboard(mContext, getView());
            cbCommodity.setEnabled(true);
            if (getIsCommoditySelected()) {
                cbCommodity.setImageResource(R.drawable.ic_commodity_dselect);
                setIsCommoditySelected(false);
                deSelectAll();
                commodityDeselectSpinner();
                commodityDeselect();
            } else {
                cbCommodity.setImageResource(R.drawable.ic_commodity_select);
                setIsCommoditySelected(true);
                commoditySpinner();
                commoditySelect();
                if (getIsDerivativeSelected() && getIsCurrencySelected() && getIsCommoditySelected()) {
                    // cbAll.setImageResource(R.drawable.all_select);
                    setAll(false);
                    allSpinner();
                    allSelect();
                }
            }
            disableEnableField();
        } else if (i == R.id.cbDerivative) {
            if (isOffline() && isOfflineDocumentUploadedDone()) {
                if (!getIsDerivativeSelected()) {
                    commonMsgDialog(mContext, getResources().getString(R.string.offline_segment_msg), false, "");
                }
            }
            if (isOffline() ? isOfflineDocumentUploadedDone() : isEquitySegmentExist() || isEquityIpvDone()) {
                return;
            }
            removeKeyboard(mContext, getView());
            cbDerivative.setEnabled(true);
            cbEquity.setEnabled(true);
            if (getIsDerivativeSelected()) {
                cbDerivative.setImageResource(R.drawable.ic_derivative_dselect);
                setIsDerivativeSelected(false);
                setDerivativeSegmentExist(false);
                llDealWithDeri.setVisibility(View.GONE);
                if (!getIsEquitySelected()) {
                    // cbAll.setImageResource(R.drawable.all_select);
                    equityDeselectSpinner();
                    equityDeselect();
                }
                deSelectAll();
            } else {
                commonMsgDialog(mContext, ON_OPT_DERIVATIVE_SEG_MSG, false, "");
                cbDerivative.setImageResource(R.drawable.ic_derivative_select);
                cbEquity.setImageResource(R.drawable.ic_equity_select);
                setIsDerivativeSelected(true);
                setIsEquitySelected(true);
                equitySpinner();
                equitySelect();
                if (getIsDerivativeSelected() && getIsCurrencySelected() && getIsCommoditySelected()) {
                    // cbAll.setImageResource(R.drawable.all_select);
                    setAll(false);
                    allSpinner();
                    allSelect();
                }
                if (getAppType().equalsIgnoreCase(DIRECT) || getAppType().equalsIgnoreCase(CRM)) {
                    if (getIsDerivativeSelected() && getIsCurrencySelected()) {
                        cbCommodity.setEnabled(true);
                        cbCommodity.setImageResource(R.drawable.ic_commodity_select);
                        setIsCommoditySelected(true);
                        commoditySpinner();
                        commoditySelect();
                    } else if (getIsDerivativeSelected()) {
                        cbCommodity.setEnabled(true);
                        cbCommodity.setImageResource(R.drawable.ic_commodity_select);
                        setIsCommoditySelected(true);
                        commoditySpinner();
                        commoditySelect();
                    }
                }
            }
            disableEnableField();
        } else if (i == R.id.cbCurrency) {
            if (isOffline() && isOfflineDocumentUploadedDone()) {
                if (!getIsCurrencySelected()) {
                    commonMsgDialog(mContext, getResources().getString(R.string.offline_segment_msg), false, "");
                }
            }
            if (isOffline() ? isOfflineDocumentUploadedDone() : isEquitySegmentExist() || isEquityIpvDone()) {
                return;
            }
            removeKeyboard(mContext, getView());
            cbCurrency.setEnabled(true);
            cbEquity.setEnabled(true);
            if (getIsCurrencySelected()) {
                cbCurrency.setImageResource(R.drawable.ic_currency_dselect);
                setIsCurrencySelected(false);
                setCurrencySegmentExist(false);
                if (!getIsEquitySelected()) {
                    // cbAll.setImageResource(R.drawable.all_select);
                    equityDeselectSpinner();
                    equityDeselect();
                }
                deSelectAll();
            } else {
                commonMsgDialog(mContext, ON_OPT_CURRENCY_SEG_MSG, false, "");
                cbCurrency.setImageResource(R.drawable.ic_currency_select);
                cbEquity.setImageResource(R.drawable.ic_equity_select);
                setIsCurrencySelected(true);
                setIsEquitySelected(true);
                equitySpinner();
                equitySelect();
                if (getIsDerivativeSelected() && getIsCommoditySelected() && getIsCurrencySelected()) {
                    // cbAll.setImageResource(R.drawable.all_select);
                    setAll(false);
                    allSpinner();
                    allSelect();
                }
                if (getAppType().equalsIgnoreCase(DIRECT) || getAppType().equalsIgnoreCase(CRM)) {
                    if (getIsDerivativeSelected() && getIsCurrencySelected()) {
                        cbCommodity.setEnabled(true);
                        cbCommodity.setImageResource(R.drawable.ic_commodity_select);
                        setIsCommoditySelected(true);
                        commoditySpinner();
                        commoditySelect();
                    } else if (getIsCurrencySelected()) {
                        cbCommodity.setEnabled(true);
                        cbCommodity.setImageResource(R.drawable.ic_commodity_select);
                        setIsCommoditySelected(true);
                        commoditySpinner();
                        commoditySelect();
                    }
                }
            }
            disableEnableField();
        } else if (i == R.id.cbAll) {
            if (isOffline() && isOfflineDocumentUploadedDone()) {
                if (!getIsAllSelected()) {
                    commonMsgDialog(mContext, getResources().getString(R.string.offline_segment_msg), false, "");
                }
            }
            if (isOffline() ? isOfflineDocumentUploadedDone() : isEquitySegmentExist() && getIsCommoditySelected()) {
                return;
            }
            removeKeyboard(mContext, getView());
            cbAll.setEnabled(true);
            if (!isEquitySegmentExist() && !isCommoditySegmentExist()) {
                cbEquity.setEnabled(true);
                cbDerivative.setEnabled(true);
                cbCurrency.setEnabled(true);
                cbCommodity.setEnabled(true);
            }
            if (!isEquitySegmentExist()) {
                if (isEquityFundTransfered()) {
                    cbEquity.setEnabled(true);
                    cbDerivative.setEnabled(true);
                    cbCurrency.setEnabled(true);
                }
            }
            if (!isCommoditySegmentExist()) {
                if (isCommodityFundTransfered()) {
                    cbCommodity.setEnabled(true);
                }
            }
            if (getIsAllSelected()) {
                if (!isEquitySegmentExist()) {
                    if (isEquityFundTransfered()) {
                        if (!isEquityIpvDone()) {//create flag in personal detail
                            cbDerivative.setImageResource(R.drawable.ic_derivative_dselect);
                            setIsDerivativeSelected(false);
                        }
                        if (!isEquityIpvDone()) {
                            cbCurrency.setImageResource(R.drawable.ic_currency_dselect);
                            setIsCurrencySelected(false);
                        }
                    } else {
                        cbCurrency.setImageResource(R.drawable.ic_currency_dselect);
                        cbDerivative.setImageResource(R.drawable.ic_derivative_dselect);
                        cbEquity.setImageResource(R.drawable.ic_equity_dselect);
                        setIsEquitySelected(false);
                        setIsDerivativeSelected(false);
                        setIsCurrencySelected(false);
                    }
                }
                if (!isCommoditySegmentExist()) {
                    if (!isCommodityFundTransfered()) {
                        cbCommodity.setImageResource(R.drawable.ic_commodity_dselect);
                        setIsCommoditySelected(false);
                    }
                }
                if (getIsEquitySelected() && getIsDerivativeSelected() && getIsCommoditySelected() && getIsCurrencySelected()) {
                    cbAll.setImageResource(R.drawable.ic_all_select);
                } else {
                    cbAll.setImageResource(R.drawable.ic_all_dselect);
                }
                setIsAllSelected(false);
                allDeselectSpinner();
                allDeselect();
                disableEnableField();
            } else {
//                commonMsgDialog(mContext, ON_OPT_ALL_SEG_MSG,false,"");
                setIsAllSelected(true);
                setAll(true);
                allSpinner();
                allSelect();
                disableEnableField();
            }
        } else if (i == R.id.toggleFmcSebiAuthority) {
            if (toggleFmcSebiAuthority.isChecked()) {
                llFmcSebiAuthority.setVisibility(View.VISIBLE);
                etFmcSebiAuthority.setText("");
            } else {
                removeKeyboard(mContext, getView());
                llFmcSebiAuthority.setVisibility(View.GONE);
                etFmcSebiAuthority.setText("");
            }
        } else if (i == R.id.toggleSEBIAuthority) {
            if (toggleSEBIAuthority.isChecked()) {
                llSebiAuthority.setVisibility(View.VISIBLE);
                etSebiAuthority.setText("");
            } else {
                removeKeyboard(mContext, getView());
                llSebiAuthority.setVisibility(View.GONE);
                etSebiAuthority.setText("");
            }
        } else if (i == R.id.toggleRegWithBroker) {
            if (toggleRegWithBroker.isChecked()) {
                llBrokerInfo.setVisibility(View.VISIBLE);
                etBrokerName.setText("");
                etClientCode.setText("");
            } else {
                removeKeyboard(mContext, getView());
                llBrokerInfo.setVisibility(View.GONE);
                etBrokerName.setText("");
                etClientCode.setText("");
            }
        } else if (i == R.id.toggleCountryCitizen) {
            if (toggleCountryCitizen.isChecked()) {
                removeKeyboard(mContext, getView());
                llCountryCitizenship.setVisibility(View.VISIBLE);
                // etCountryCitizenship.setText("");
//                declarationSelect();
            } else {
                llCountryCitizenship.setVisibility(View.GONE);
                //etCountryCitizenship.setText("");
//                declarationSelect();
            }
        } else if (i == R.id.toggleCountryBirth) {
            if (toggleCountryBirth.isChecked()) {
                llCountryBirth.setVisibility(View.VISIBLE);
//                etCountryBirth.setText("");
                etPlaceBirth.setText("");
//                declarationSelect();
            } else {
                removeKeyboard(mContext, getView());
                llCountryBirth.setVisibility(View.GONE);
//                etCountryBirth.setText("");
                etPlaceBirth.setText("");
//                declarationSelect();
            }
        } else if (i == R.id.toggleCountryTax) {
            if (toggleCountryTax.isChecked()) {
                llCountryTax.setVisibility(View.VISIBLE);
//                etCountryTax.setText("");
                etTAXIdentificationNum.setText("");
//                declarationSelect();
            } else {
                removeKeyboard(mContext, getView());
                llCountryTax.setVisibility(View.GONE);
//                etCountryTax.setText("");
                etTAXIdentificationNum.setText("");
//                declarationSelect();
            }
        } else if (i == R.id.togglePoaAddress) {
            if (togglePoaAddress.isChecked()) {
//                declarationSelect();
            } else {
//                declarationSelect();

            }

        } else if (i == R.id.toggleAddressOutsideIndia) {
            if (toggleAddressOutsideIndia.isChecked()) {
//                declarationSelect();
            } else {
//                declarationSelect();
            }
        } else if (i == R.id.rbMale) {
            if (rbMale.isChecked()) {
                rbFemale.setChecked(false);
                etGenderError.setVisibility(View.GONE);
                llMaidenName.setVisibility(View.GONE);
            }
        } else if (i == R.id.rbFemale) {
            if (rbFemale.isChecked()) {
                rbMale.setChecked(false);
                etGenderError.setVisibility(View.GONE);
                if (rbMarried.isChecked()) {
                    llMaidenName.setVisibility(View.VISIBLE);
                } else {
                    llMaidenName.setVisibility(View.GONE);
                }
            }
        } else if (i == R.id.rbMarried) {
            if (rbMarried.isChecked()) {
                rbUnMarried.setChecked(false);
                etmaritalError.setVisibility(View.GONE);
                if (rbFemale.isChecked()) {
                    llMaidenName.setVisibility(View.VISIBLE);
                } else {
                    llMaidenName.setVisibility(View.GONE);
                }
            }
        } else if (i == R.id.rbUnMarried) {
            if (rbUnMarried.isChecked()) {
                rbMarried.setChecked(false);
                etmaritalError.setVisibility(View.GONE);
                llMaidenName.setVisibility(View.GONE);
            }
        } else if (i == R.id.tvFamilyDeclaration) {
            familyDeclarationNoteDialog();
        } else if (i == R.id.ivViewFamilyDeclaration) {
            getFamilyDetails(customdailogFamilyRelation, false, "");
            visibilityFlagFD = true;
        } else if (i == R.id.cbFamilyDeclaration) {
            if (!cbFamilyDeclaration.isChecked()) {
                CustomDialog resendCd = new CustomDialog(mContext, getResources().getString(R.string.deselect_family_declaration), true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                    @Override
                    public void dialogAction(MOSLCommon.Action action, Object... data) {
                        if (action == MOSLCommon.Action.OK) {
                            familyDeclarationChangedFlag = false;
//                            deleteFamilyDetails();
                            showFamilyDetailLayout(false);
                        } else if (action == MOSLCommon.Action.NO) {
                            showFamilyDetailLayout(true);
                        } else if (action == MOSLCommon.Action.CANCEL) {
                            showFamilyDetailLayout(true);
                        }
                    }
                });
                resendCd.show();
            }
        } else if (i == R.id.ivCategoryInfo) {
            removeKeyboard(mContext, getView());
            final Dialog dialogCategoryInfo = new Dialog(mContext);
            dialogCategoryInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogCategoryInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogCategoryInfo.setCanceledOnTouchOutside(false);
            dialogCategoryInfo.getWindow().setWindowAnimations(R.style.animationName);
            dialogCategoryInfo.setContentView(R.layout.dialog_doc_list);
            dialogCategoryInfo.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogCategoryInfo.setCanceledOnTouchOutside(false);

            Button btnAgree = (Button) dialogCategoryInfo.findViewById(R.id.btnAgree);
            ImageView btnClose = (ImageView) dialogCategoryInfo.findViewById(R.id.btnClose);
            TextView tvHeading = (TextView) dialogCategoryInfo.findViewById(R.id.tvHeading);
            TextView tvHeading1 = (TextView) dialogCategoryInfo.findViewById(R.id.tvHeading1);

            tvHeading1.setText(getResources().getString(R.string.category_selection_note));
            tvHeading.setText("Note");

            btnAgree.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialogCategoryInfo.dismiss();
                }
            });

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogCategoryInfo.dismiss();
                }
            });
            dialogCategoryInfo.show();
        }
    }

    private final AdapterView.OnTouchListener mfPEPRemarlListener = new AdapterView.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            pepRemark = "";

            return false;
        }
    };

    public void setAll(boolean isAllSegmentClicked) {
        if (!isEquitySegmentExist()) {
            if (isEquityFundTransfered()) {
                if (!isEquityIpvDone()) {
                    cbDerivative.setImageResource(R.drawable.ic_derivative_select);
                    setIsDerivativeSelected(true);
                }
                if (!isEquityIpvDone()) {
                    cbCurrency.setImageResource(R.drawable.ic_currency_select);
                    setIsCurrencySelected(true);
                }
                if (isAllSegmentClicked && !isEquityIpvDone()) {
                    commonMsgDialog(mContext, ON_OPT_ALL_SEG_MSG, false, "");
                }
            } else {
                cbEquity.setImageResource(R.drawable.ic_equity_select);
                cbDerivative.setImageResource(R.drawable.ic_derivative_select);
                cbCurrency.setImageResource(R.drawable.ic_currency_select);
                setIsEquitySelected(true);
                setIsDerivativeSelected(true);
                setIsCurrencySelected(true);
                if (isAllSegmentClicked) {
                    commonMsgDialog(mContext, ON_OPT_ALL_SEG_MSG, false, "");
                }
            }
        }
        if (!isCommoditySegmentExist()) {
            cbCommodity.setImageResource(R.drawable.ic_commodity_select);
            setIsCommoditySelected(true);
        }
        if (getIsEquitySelected() && getIsDerivativeSelected() && getIsCommoditySelected() && getIsCurrencySelected()) {
            cbAll.setImageResource(R.drawable.ic_all_select);
            setIsAllSelected(true);
        }
    }

    public void deSelectAll() {
        if (getIsEquitySelected() && getIsDerivativeSelected() && getIsCommoditySelected() && getIsCurrencySelected()) {
            setIsAllSelected(true);
            cbAll.setImageResource(R.drawable.ic_all_select);
        } else {
            setIsAllSelected(false);
            cbAll.setImageResource(R.drawable.ic_all_dselect);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int i = buttonView.getId();
        if (i == R.id.cbSameAsCorrespondence) {
            removeKeyboard(mContext, getView());
            try {
                if (isChecked) {
                    input_layout_bldgNamePerm.setErrorEnabled(false);
                    input_layout_flatNamePerm.setErrorEnabled(false);
                    input_layout_areaPerm.setErrorEnabled(false);
                    input_layout_pincodePerm.setErrorEnabled(false);
                    cbSameAsCorrespondence.setChecked(true);
                    etBldgNamePerm.setEnabled(false);
                    etFlatNamePerm.setEnabled(false);
                    etAreaPerm.setEnabled(false);
//                    etAddProofPerm.setEnabled(false);
                    etPincodePerm.setEnabled(false);
                    etBldgNamePerm.setText(etBldgNameCorres.getText().toString());
                    etFlatNamePerm.setText(etFlatNoCorres.getText().toString());
                    etAreaPerm.setText(etAreaCorres.getText().toString());
//                    etAddProofPerm.setText(etAddProofCorres.getText().toString());
                    etPincodePerm.setText(etPincodeCorres.getText().toString());
                    if (spPermCountry.getSelectedItem() != null) {
                        String corrSelectedCountry = spCorresCountry.getSelectedItem().toString();
                        getSpinnerData(arrAdapCountry, spPermCountry, corrSelectedCountry);
                    }
                    if (spPermAddProof.getSelectedItem() != null) {
                        int permSelectedAddPos = ((RowData) spCorresAddProof.getSelectedItem()).getPosition();
                        spPermAddProof.setSelection(permSelectedAddPos);
                    }
                    if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                        etPermState.setVisibility(View.VISIBLE);
                        etPermCity.setVisibility(View.VISIBLE);
                        spPermState.setVisibility(View.GONE);
                        spPermCity.setVisibility(View.GONE);
                        etPermState.setText(etCorresState.getText().toString());
                        etPermCity.setText(etCorresCity.getText().toString());
                    } else {
                        etPermState.setVisibility(View.GONE);
                        etPermCity.setVisibility(View.GONE);
                        spPermState.setVisibility(View.VISIBLE);
                        spPermCity.setVisibility(View.VISIBLE);
                        if (spCorresState.getSelectedItem() != null) {
                            int corrSelectedStatePos = ((RowData) spCorresState.getSelectedItem()).getPosition();
                            spPermState.setSelection(corrSelectedStatePos);
                        }
                        if (spCorresCity.getSelectedItem() != null) {
                            arrAdapPermCity.clear();
                            arrAdapPermCity.addAll(arrCorresCity);
                            arrAdapPermCity.notifyDataSetChanged();
                            int corrSelectedCityPos = ((RowData) spCorresCity.getSelectedItem()).getPosition();
                            spPermCity.setSelection(corrSelectedCityPos);
                        }
                    }
                    if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                        etPermState.setEnabled(false);
                        etPermCity.setEnabled(false);
                    } else {
                        spPermState.setEnabled(false);
                        spPermCity.setEnabled(false);
                    }
                    spPermCountry.setEnabled(false);
                    spPermAddProof.setEnabled(false);
                } else {
                    cbSameAsCorrespondence.setChecked(false);
                    if (arrAdapPermCity == null) {
                        arrPermCity = new ArrayList<>();
                        arrAdapPermCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrPermCity);
                        arrAdapPermCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spPermCity.setAdapter(arrAdapPermCity);
                    }
                    arrAdapPermCity.add(new RowData(0, "-1", "Select"));
                    arrAdapPermCity.notifyDataSetChanged();
                    cityFoundFlag = true;
                    etPermState.setVisibility(View.GONE);
                    etPermCity.setVisibility(View.GONE);
                    spPermState.setVisibility(View.VISIBLE);
                    spPermCity.setVisibility(View.VISIBLE);
                    etBldgNamePerm.setEnabled(true);
                    etFlatNamePerm.setEnabled(true);
                    etAreaPerm.setEnabled(true);
                    spPermAddProof.setEnabled(true);
                    spPermAddProof.setSelection(0);
                    spPermCountry.setEnabled(true);
                    spPermCountry.setSelection(0);
                    etPincodePerm.setEnabled(true);
                    spPermState.setEnabled(true);
                    spPermState.setSelection(0);
                    spPermCity.setEnabled(true);
                    spPermCity.setSelection(0);
                    etBldgNamePerm.setText("");
                    etFlatNamePerm.setText("");
                    etAreaPerm.setText("");
                    etPincodePerm.setText("");
                }
            } catch (Exception ex) {
                MOSLCommon.showToastMsg(mContext, "Some error occurred, retry");
                ex.printStackTrace();
            }
        } else if (i == R.id.cbCommOther) {
            removeKeyboard(mContext, getView());
            if (isChecked) {
                cbCommOther.setChecked(true);
                etCommOther.setVisibility(View.VISIBLE);
                etCommOther.setText("");
            } else {
                cbCommOther.setChecked(false);
                etCommOther.setVisibility(View.GONE);
                input_layout_commOther.setErrorEnabled(false);
            }
        } else if (i == R.id.cbEqIntraday) {
            if (isChecked) {
                etPurposeForEquityError.setVisibility(View.GONE);

            }
        } else if (i == R.id.cbEqInvestment) {
            if (isChecked) {
                etPurposeForEquityError.setVisibility(View.GONE);

            }
        } else if (i == R.id.cbEqPositional) {
            if (isChecked) {
                etPurposeForEquityError.setVisibility(View.GONE);

            }
        } else if (i == R.id.cbDevHedging) {
            if (isChecked) {
                etPurposeForDerivativeError.setVisibility(View.GONE);

            }
        } else if (i == R.id.cbDevTrading) {
            if (isChecked) {
                etPurposeForDerivativeError.setVisibility(View.GONE);

            }
        } else if (i == R.id.cbCommHedging) {
            if (isChecked) {
                etPurposeForCommodityError.setVisibility(View.GONE);

            }
        } else if (i == R.id.cbCommInvestment) {
            if (isChecked) {
                etPurposeForCommodityError.setVisibility(View.GONE);

            }
        } else if (i == R.id.cbCommTrading) {
            if (isChecked) {
                etPurposeForCommodityError.setVisibility(View.GONE);

            }
        } else if (i == R.id.cbCommOther) {
            if (isChecked) {
                etPurposeForCommodityError.setVisibility(View.GONE);

            }
        } else if (i == R.id.cbDeclaration) {
            if (isChecked) {
                cbDeclaration.setChecked(true);
            } else {
                cbDeclaration.setChecked(false);
            }
        } else if (i == R.id.cbCommercial) {
            if (isChecked) {
                etCategorySelectionError.setVisibility(View.GONE);
                enableField(llSubCategoryCommercial);
            } else {
                cbCommercialBullion.setChecked(false);
                cbCommercialBaseMetals.setChecked(false);
                cbCommercialEnergy.setChecked(false);
                cbCommercialAgriCommodities.setChecked(false);
                disableField(llSubCategoryCommercial);
            }
        } else if (i == R.id.cbCommercialBullion) {
            if (isChecked) {
                etCategorySelectionError.setVisibility(View.GONE);
            }
        } else if (i == R.id.cbCommercialBaseMetals) {
            if (isChecked) {
                etCategorySelectionError.setVisibility(View.GONE);
            }
        } else if (i == R.id.cbCommercialEnergy) {
            if (isChecked) {
                etCategorySelectionError.setVisibility(View.GONE);
            }
        } else if (i == R.id.cbCommercialAgriCommodities) {
            if (isChecked) {
                etCategorySelectionError.setVisibility(View.GONE);
            }
        } else if (i == R.id.cbNonCommercial) {
            if (isChecked) {
                etCategorySelectionError.setVisibility(View.GONE);
                enableField(llSubCategoryNonCommercial);
            } else {
                cbNonCommercialBullion.setChecked(false);
                cbNonCommercialBaseMetals.setChecked(false);
                cbNonCommercialEnergy.setChecked(false);
                cbNonCommercialAgriCommodities.setChecked(false);
                disableField(llSubCategoryNonCommercial);
            }
        } else if (i == R.id.cbNonCommercialBullion) {
            if (isChecked) {
                etCategorySelectionError.setVisibility(View.GONE);
            }
        } else if (i == R.id.cbNonCommercialBaseMetals) {
            if (isChecked) {
                etCategorySelectionError.setVisibility(View.GONE);
            }
        } else if (i == R.id.cbNonCommercialEnergy) {
            if (isChecked) {
                etCategorySelectionError.setVisibility(View.GONE);
            }
        } else if (i == R.id.cbNonCommercialAgriCommodities) {
            if (isChecked) {
                etCategorySelectionError.setVisibility(View.GONE);
            }
        }
    }

   /* public void declarationSelect() {
        if (toggleCountryCitizen.isChecked() || toggleCountryBirth.isChecked() || toggleCountryTax.isChecked() || togglePoaAddress.isChecked() || toggleAddressOutsideIndia.isChecked()) {
            llDeclaration.setVisibility(View.VISIBLE);
        } else {
            llDeclaration.setVisibility(View.GONE);
        }
    }*/

    /**
     * Validation function for update API
     */

    public void validateMFData() {
        if (getAppType().equalsIgnoreCase(PP) && getInterestedInMF() && (spEqBranchCode.getSelectedItem() == null || ((RowData) spEqBranchCode.getSelectedItem()).getKey().equals("-1"))) {
            etEqBranchCodeError.setVisibility(View.VISIBLE);
            etEqBranchCodeError.setText("Select MF branch code");
            etEqBranchCodeError.requestFocus();
            return;
        } else if (getAppType().equalsIgnoreCase(PP) && getInterestedInMF() && (spEqSubBroker.getSelectedItem() == null || ((RowData) spEqSubBroker.getSelectedItem()).getKey().equals("-1"))) {
            etEqSubrokerCodeError.setVisibility(View.VISIBLE);
            etEqSubrokerCodeError.setText("Select MF subbroker code");
            etEqSubrokerCodeError.requestFocus();
            return;
        } else if (getAppType().equalsIgnoreCase(PP) && getInterestedInMF() && (spEqTrader.getSelectedItem() == null || ((RowData) spEqTrader.getSelectedItem()).getKey().equals("-1"))) {
            etEqTraderCodeError.setVisibility(View.VISIBLE);
            etEqTraderCodeError.setText("Select MF trader code");
            etEqTraderCodeError.requestFocus();
            return;
        } else if (getAppType().equalsIgnoreCase(PP) && getInterestedInMF() && (spEqArea.getSelectedItem() == null || ((RowData) spEqArea.getSelectedItem()).getKey().equals("-1"))) {
            etEqAreaError.setVisibility(View.VISIBLE);
            etEqAreaError.setText("Select MF area");
            etEqAreaError.requestFocus();
            return;
        } else if (getAppType().equalsIgnoreCase(PP) && getInterestedInMF() && (spEqRegion.getSelectedItem() == null || ((RowData) spEqRegion.getSelectedItem()).getKey().equals("-1"))) {
            etEqRegionError.setVisibility(View.VISIBLE);
            etEqRegionError.setText("Select MF region");
            etEqRegionError.requestFocus();
            return;
        } else if (spTitle.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            etTitleError.setVisibility(View.VISIBLE);
            etTitleError.setText("Select title");
            etTitleError.requestFocus();
            return;
        } else if (!validateEditText(etFirstName, input_layout_firstname, "Enter first name")) {
            return;
        } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etFirstName.getText().toString().trim())) {
            etFirstNameError.setVisibility(View.VISIBLE);
            etFirstNameError.setText(ErrorMsgName);
            etFirstName.requestFocus();
            return;
        } else if (etMiddleName.getText().toString().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etMiddleName.getText().toString().trim())) {
            etMiddleNameError.setVisibility(View.VISIBLE);
            etMiddleNameError.setText(ErrorMsgName);
            etMiddleName.requestFocus();
            return;
        } else if (!validateEditText(etLastName, input_layout_lastname, "Enter last name")) {
            return;
        } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etLastName.getText().toString().trim())) {
            etLastNameError.setVisibility(View.VISIBLE);
            etLastNameError.setText(ErrorMsgName);
            etLastName.requestFocus();
            return;
        } else if (!(rbMale.isChecked()) && !(rbFemale.isChecked())) {
            etGenderError.setVisibility(View.VISIBLE);
            etGenderError.setText("Select gender");
            etGenderError.requestFocus();
            return;
        } else if (!(rbMarried.isChecked()) && !(rbUnMarried.isChecked())) {
            etmaritalError.setVisibility(View.VISIBLE);
            etmaritalError.setText("Select marital status");
            etmaritalError.requestFocus();
            return;
        } else if (!validateEditText(etDOB, input_layout_dob, "Date of birth( as per PAN)")) {
            etDOB.setEnabled(true);
            etDOBRequestFocus.requestFocus();
            return;
        } else if (!validateEditText(etBirthPlace, input_layout_birthPlace, "Enter birth place")) {
            return;
        } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etBirthPlace.getText().toString().trim())) {
            etBirthPlaceError.setVisibility(View.VISIBLE);
            etBirthPlaceError.setText(ErrorMsgName);
            etBirthPlace.requestFocus();
            return;
        } else if (spFatherSpouseSel.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            etFatherSpouseSelError.setVisibility(View.VISIBLE);
            etFatherSpouseSelError.setText("Select father/spouse");
            etFatherSpouseSelError.requestFocus();
            return;
        } else if (spFatherSpouseTitle.getSelectedItem().toString().equalsIgnoreCase(SELECT_TITLE)) {
            etFatherSpouseTitleError.setVisibility(View.VISIBLE);
            etFatherSpouseTitleError.setText("Select title");
            etFatherSpouseTitleError.requestFocus();
            return;
        } else if (!validateEditText(etFatherSpouseFirstName, input_layout_fatherSpouseFirstName, "Enter father/spouse first name")) {
            return;
        } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etFatherSpouseFirstName.getText().toString().trim())) {
            etFatherSpouseFirstNameError.setVisibility(View.VISIBLE);
            etFatherSpouseFirstNameError.setText(ErrorMsgName);
            etFatherSpouseFirstName.requestFocus();
            return;
        } else if (etFatherSpouseMidName.getText().toString().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etFatherSpouseMidName.getText().toString().trim())) {
            etFatherSpouseMidNameError.setVisibility(View.VISIBLE);
            etFatherSpouseMidNameError.setText(ErrorMsgName);
            etFatherSpouseMidName.requestFocus();
            return;
        } else if (!validateEditText(etFatherSpouseLastName, input_layout_fatherSpouseLastName, "Enter father/spouse last name")) {
            return;
        } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etFatherSpouseLastName.getText().toString().trim())) {
            etFatherSpouseLastNameError.setVisibility(View.VISIBLE);
            etFatherSpouseLastNameError.setText(ErrorMsgName);
            etFatherSpouseLastName.requestFocus();
            return;
        } else if (spMotherTitle.getSelectedItem().toString().equalsIgnoreCase(SELECT_TITLE)) {
            etMotherTitleError.setVisibility(View.VISIBLE);
            etMotherTitleError.setText("Select title");
            etMotherTitleError.requestFocus();
            return;
        } else if (!validateEditText(etMotherFirstName, input_layout_MotherFirstname, "Enter mother first name")) {
            return;
        } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etMotherFirstName.getText().toString().trim())) {
            etMotherFirstNameError.setVisibility(View.VISIBLE);
            etMotherFirstNameError.setText(ErrorMsgName);
            etMotherFirstName.requestFocus();
            return;
        } else if (etMotherMiddleName.getText().toString().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etMotherMiddleName.getText().toString().trim())) {
            etMotherMiddleNameError.setVisibility(View.VISIBLE);
            etMotherMiddleNameError.setText(ErrorMsgName);
            etMotherMiddleName.requestFocus();
            return;
        } else if (!validateEditText(etMotherLastName, input_layout_MotherLastname, "Enter mother last name")) {
            return;
        } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etMotherLastName.getText().toString().trim())) {
            etMotherLastNameError.setVisibility(View.VISIBLE);
            etMotherLastNameError.setText(ErrorMsgName);
            etMotherLastName.requestFocus();
            return;
        } else if (rbFemale.isChecked() && rbMarried.isChecked() && spMaidenTitle.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            etMaidenTitleError.setVisibility(View.VISIBLE);
            etMaidenTitleError.setText("Select title");
            etMaidenTitleError.requestFocus();
            return;
        } else if (rbFemale.isChecked() && rbMarried.isChecked() && !validateEditText(etMaidenFirstName, input_layout_MaidenFirstname, "Enter maiden first name")) {
            return;
        } else if (rbFemale.isChecked() && rbMarried.isChecked() && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etMaidenFirstName.getText().toString().trim())) {
            etMaidenFirstNameError.setVisibility(View.VISIBLE);
            etMaidenFirstNameError.setText(ErrorMsgName);
            etMaidenFirstName.requestFocus();
            return;
        } else if (rbFemale.isChecked() && rbMarried.isChecked() && etMaidenMiddleName.getText().toString().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etMaidenMiddleName.getText().toString().trim())) {
            etMaidenMiddleNameError.setVisibility(View.VISIBLE);
            etMaidenMiddleNameError.setText(ErrorMsgName);
            etMaidenMiddleName.requestFocus();
            return;
        } else if (rbFemale.isChecked() && rbMarried.isChecked() && !validateEditText(etMaidenLastName, input_layout_MaidenLastname, "Enter maiden last name")) {
            return;
        } else if (rbFemale.isChecked() && rbMarried.isChecked() && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etMaidenLastName.getText().toString().trim())) {
            etMaidenLastNameError.setVisibility(View.VISIBLE);
            etMaidenLastNameError.setText(ErrorMsgName);
            etMaidenLastName.requestFocus();
            return;
        } else if (aadhaarByPass && etAadhar.getText().toString().length() < 12) {
            input_layout_aadhar.setError("Invalid aadhaar number");
            etAadhar.requestFocus();
        } else if (!isBlank(etGST.getText().toString()) && !ValidationRule.validateGST(etGST.getText().toString().trim())) {
            input_layout_GST.setError(ErrorMsgGST);
            etGST.requestFocus();
            return;
        }/* else if (spOtherResidence.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            etOtherResidenceError.setVisibility(View.VISIBLE);
            etOtherResidenceError.setText("Select Country");
            etOtherResidenceError.requestFocus();
            return;
        }*/ else if (etBldgNameCorres.isEnabled() && !validateEditText(etBldgNameCorres, input_layout_bldgNameCorres, "Enter bldg no/name")) {
            return;
        } else if (etBldgNameCorres.isEnabled() && !etBldgNameCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etBldgNameCorres.getText().toString())) {
            input_layout_bldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
            etBldgNameCorres.requestFocus();
            return;
        } else if (etFlatNoCorres.isEnabled() && !validateEditText(etFlatNoCorres, input_layout_flatNoCorres, "Enter flat no/road name")) {
            return;
        } else if (etFlatNoCorres.isEnabled() && !etFlatNoCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etFlatNoCorres.getText().toString())) {
            input_layout_flatNoCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
            etFlatNoCorres.requestFocus();
            return;
        } else if (etAreaCorres.isEnabled() && !validateEditText(etAreaCorres, input_layout_areaCorres, "Enter Area")) {
            return;
        } else if (etAreaCorres.isEnabled() && !etAreaCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etAreaCorres.getText().toString())) {
            input_layout_areaCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
            etAreaCorres.requestFocus();
            return;
        } else if (spCorresAddProof.isEnabled() && (spCorresAddProof.getSelectedItem() == null || ((RowData) spCorresAddProof.getSelectedItem()).getKey().equals("-1"))) {
            etCorresAddProofError.setVisibility(View.VISIBLE);
            etCorresAddProofError.setText("Select adderss proof");
            etCorresAddProofError.requestFocus();
            spCorresAddProof.setEnabled(true);
            return;
        } else if (etPincodeCorres.isEnabled() && !validateEditText(etPincodeCorres, input_layout_pincodeCorres, "Enter pincode")) {
            return;
        } else if (etPincodeCorres.length() < 6) {
            etPincodeCorres.requestFocus();
            return;
        } else if (!(getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && (spCorresState.getSelectedItem() == null || ((RowData) spCorresState.getSelectedItem()).getKey().equals("-1"))) {
            etCorresStateError.setVisibility(View.VISIBLE);
            etCorresStateError.setText("Select state");
            etCorresStateError.requestFocus();
            cityFoundFlag = true;
            spCorresState.setEnabled(true);
            return;
        } else if ((getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && etCorresState.isEnabled() && etCorresState.getText().toString().trim().equals("")) {
            etCorresStateError.setVisibility(View.VISIBLE);
            etCorresStateError.setText("Select state");
            etCorresStateError.requestFocus();
//                cityFoundFlag = true;
            etCorresState.setEnabled(true);
            return;
        } else if ((getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && etCorresCity.isEnabled() && etCorresCity.getText().toString().trim().equals("")) {
            etCorresCityError.setVisibility(View.VISIBLE);
            etCorresCityError.setText("Select city");
            etCorresCityError.requestFocus();
            etCorresCity.setEnabled(true);
            return;
        } else if (!(getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && (spCorresCity.getSelectedItem() == null || ((RowData) spCorresCity.getSelectedItem()).getKey().equals("-1"))) {
            etCorresCityError.setVisibility(View.VISIBLE);
            etCorresCityError.setText("Select city");
            etCorresCityError.requestFocus();
            cityFoundFlag = true;
            spCorresState.setEnabled(true);
            spCorresCity.setEnabled(true);
            return;
        } else if (spCorresCountry.isEnabled() && spCorresCountry.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            etCorresCountryError.setVisibility(View.VISIBLE);
            etCorresCountryError.setText("Select Country");
            etCorresCountryError.requestFocus();
            spCorresCountry.setEnabled(true);
            return;
        } else if (etBldgNamePerm.isEnabled() && !validateEditText(etBldgNamePerm, input_layout_bldgNamePerm, "Enter bldg no/name")) {
            return;
        } else if (etBldgNamePerm.isEnabled() && !etBldgNamePerm.getText().toString().equals("") & !ValidationRule.validateAddress(etBldgNamePerm.getText().toString())) {
            input_layout_bldgNamePerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
            etBldgNamePerm.requestFocus();
            return;
        } else if (etFlatNamePerm.isEnabled() && !validateEditText(etFlatNamePerm, input_layout_flatNamePerm, "Enter flat no/road name")) {
            return;
        } else if (etFlatNamePerm.isEnabled() && !etFlatNamePerm.getText().toString().equals("") && !ValidationRule.validateAddress(etFlatNamePerm.getText().toString())) {
            input_layout_flatNamePerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
            etFlatNamePerm.requestFocus();
            return;
        } else if (etAreaPerm.isEnabled() && !validateEditText(etAreaPerm, input_layout_areaPerm, "Enter Area")) {
            return;
        } else if (etAreaPerm.isEnabled() && !etAreaPerm.getText().toString().equals("") && !ValidationRule.validateAddress(etAreaPerm.getText().toString())) {
            input_layout_areaPerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
            etAreaPerm.requestFocus();
            return;
        } else if (spPermAddProof.isEnabled() && (spPermAddProof.getSelectedItem() == null || ((RowData) spPermAddProof.getSelectedItem()).getKey().equals("-1"))) {
            etPermAddProofError.setVisibility(View.VISIBLE);
            etPermAddProofError.setText("Select adderss proof");
            etPermAddProofError.requestFocus();
            spPermAddProof.setEnabled(true);
            return;
        } else if (etPincodePerm.isEnabled() && !validateEditText(etPincodePerm, input_layout_pincodePerm, "Enter pincode")) {
            return;
        } else if (etPincodePerm.length() < 6) {
            etPincodePerm.requestFocus();
            return;
        } else if (!(getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && (spPermState.getSelectedItem() == null || ((RowData) spPermState.getSelectedItem()).getKey().equals("-1"))) {
            etPermStateError.setVisibility(View.VISIBLE);
            etPermStateError.setText("Select state");
            etPermStateError.requestFocus();
            cityFoundFlag = true;
            spPermState.setEnabled(true);
            return;
        } else if ((getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && etPermState.isEnabled() && etPermState.getText().toString().trim().equals("")) {
            etPermStateError.setVisibility(View.VISIBLE);
            etPermStateError.setText("Select state");
            etPermStateError.requestFocus();
            etPermState.setEnabled(true);
            return;
//                cityFoundFlag = true;
        } else if ((getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && etPermCity.isEnabled() && etPermCity.getText().toString().trim().equals("")) {
            etPermCityError.setVisibility(View.VISIBLE);
            etPermCityError.setText("Select city");
            etPermCityError.requestFocus();
            etPermCity.setEnabled(true);
            return;
//                cityFoundFlag = true;
        } else if (!(getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && (spPermCity.getSelectedItem() == null || ((RowData) spPermCity.getSelectedItem()).getKey().equals("-1"))) {
            etPermCityError.setVisibility(View.VISIBLE);
            etPermCityError.setText("Select city");
            etPermCityError.requestFocus();
            cityFoundFlag = true;
            spPermCity.requestFocus();
            spPermState.setEnabled(true);
            spPermCity.setEnabled(true);
            return;
        } else if (spPermCountry.isEnabled() && spPermCountry.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            etPermCountryError.setVisibility(View.VISIBLE);
            etPermCountryError.setText("Select Country");
            etPermCountryError.requestFocus();
            spPermCountry.setEnabled(true);
            return;
        } else if (!validateEditText(etMobile, input_layout_mobile, "Enter mobile number")) {
            etMobile.setEnabled(true);
            return;
        } else if (etMobile.length() < 10) {
            etMobile.setEnabled(true);
            return;
        } else if (!ValidationRule.validateMobile(etMobile.getText().toString())) {
            etMobile.setEnabled(true);
            etMobile.requestFocus();
            return;
        } else if (!validateEditText(etEmail, input_layout_email, "Enter email id")) {
            etEmail.setEnabled(true);
            return;
        } else if (!isValidEmail(etEmail.getText())) {
            etEmail.setEnabled(true);
            etEmail.requestFocus();
            return;
        } else if ((etSTDNo.getText().toString().trim().length() > 0 || etTelNo.getText().toString().trim().length() > 0) && (!ValidationRule.validateTel(etSTDNo.getText().toString() + etTelNo.getText().toString()))) {
            etSTDTelError.setVisibility(View.VISIBLE);
            etSTDTelError.setText(Res_Telephone_number_min_11_digit);
            etSTDNo.requestFocus();
        } else if (!isBlank(etAltTelNo.getText().toString()) && etAltTelNo.length() < 10) {
            etAltTelNo.requestFocus();
            return;
        } else if (!isBlank(etAltTelNo.getText().toString()) && !ValidationRule.validateMobile(etAltTelNo.getText().toString())) {
            etAltTelNo.requestFocus();
            return;
        } else if (!isBlank(etAltEmailID.getText().toString()) && !isValidEmail(etAltEmailID.getText())) {
            etAltEmailID.requestFocus();
            return;
        } else if (etFbId.getText().toString().trim().length() > 0 && !isValidEmail(etFbId.getText())) {
            etFbIdError.setVisibility(View.VISIBLE);
            etFbIdError.setText("Facebook id is not valid");
            etFbId.requestFocus();
        } else if (etLinkedinID.getText().toString().trim().length() > 0 && !isValidEmail(etLinkedinID.getText())) {
            etLinkedinIDError.setVisibility(View.VISIBLE);
            etLinkedinIDError.setText("Linkedin id is not valid");
            etLinkedinID.requestFocus();
        } else if (spOccupation.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            etOccupationError.setVisibility(View.VISIBLE);
            etOccupationError.setText("Select occupation");
            etOccupationError.requestFocus();
            spOccupation.setEnabled(true);
            return;
        } else if (spQualification.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            etQualificationError.setVisibility(View.VISIBLE);
            etQualificationError.setText("Select qualification");
            etQualificationError.requestFocus();
            spQualification.setEnabled(true);
            return;
        } else if (spIncome.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            etIncomeError.setVisibility(View.VISIBLE);
            etIncomeError.setText("Select income");
            etIncomeError.requestFocus();
            spIncome.setEnabled(true);
            return;
        } else if (!validateEditText(etNetWorth, input_layout_netWorth, "Enter networth")) {
            return;
        } else if (etNetWorth.getText().toString().equalsIgnoreCase("0") || (Double.parseDouble(etNetWorth.getText().toString()) <= 0)) {
            etNetWorthError.setVisibility(View.VISIBLE);
            etNetWorthError.setText("Net worth should be greater than 0");
            etNetWorth.requestFocus();
            return;
        } else if (!validateEditText(etNWDate, input_layout_NWDate, "Enter Networth as on date")) {
            etNWDateRequestFocus.requestFocus();
            return;
        } else if (!isDateIWthinYear(etNWDate.getText().toString())) {
            etNWDateError.setVisibility(View.VISIBLE);
            etNWDateError.setText(Date_should_not_be_less_than_one_year);
            etNWDateError.requestFocus();
            return;
        }
        /*else if (spStockPortfolio.getSelectedItem().toString().equalsIgnoreCase("Stock Portfolio (Rs.in Lakhs)")) {
            etStockPortfolioError.setVisibility(View.VISIBLE);
            etStockPortfolioError.setText("Select stock portfolio");
            etStockPortfolioError.requestFocus();
            return;
        }*/
        /*else if (!spMFPEPStatus.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.not_applicable))) {
            etMFPEPStatusError.setVisibility(View.VISIBLE);
            etMFPEPStatusError.setText("Select PEP Status");
            etMFPEPStatusError.requestFocus();

        }*/
        else if (etMFPEPRemark.getVisibility() == View.VISIBLE && !validateEditText(etMFPEPRemark, inputLayoutMFPEPRemark, "Entry Remark")) {
            return;
        } else if (toggleCountryCitizen.isChecked() && spCountryCitizenship.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            etCountryCitizenshipError.setVisibility(View.VISIBLE);
            etCountryCitizenshipError.setText("Entry your Country of Citizenship");
            etCountryCitizenshipError.requestFocus();
            return;
        } else if (toggleCountryBirth.isChecked() && spCountryBirth.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
            etCountryBirthError.setVisibility(View.VISIBLE);
            etCountryBirthError.setText("Entry your Country of birth");
            etCountryBirthError.requestFocus();
            return;
        }
       /* else if (toggleCountryBirth.isChecked() && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etCountryBirth.getText().toString().trim())) {
            etCountryBirthError.setVisibility(View.VISIBLE);
            etCountryBirthError.setText(ErrorMsgName);
            etCountryBirth.requestFocus();
            return;
        } else if (toggleCountryBirth.isChecked() && !validateEditText(etPlaceBirth, input_layout_PlaceBirth, "Entry your Place of birth")) {
            return;
        } else if (toggleCountryBirth.isChecked() && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etPlaceBirth.getText().toString().trim())) {
            etPlaceBirthError.setVisibility(View.VISIBLE);
            etPlaceBirthError.setText(ErrorMsgName);
            etPlaceBirth.requestFocus();
            return;
        }*/
        else if (toggleCountryBirth.isChecked() && !validateEditText(etPlaceBirth, input_layout_PlaceBirth, "Entry your Place of birth")) {
            return;
        } else if (toggleCountryBirth.isChecked() && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etPlaceBirth.getText().toString().trim())) {
            etPlaceBirthError.setVisibility(View.VISIBLE);
            etPlaceBirthError.setText(ErrorMsgName);
            etPlaceBirth.requestFocus();
            return;
        } else if (toggleCountryTax.isChecked() && spCountryTax.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
            etCountryTaxError.setVisibility(View.VISIBLE);
            etCountryTaxError.setText("Entry your Country of tax residency");
            etCountryTaxError.requestFocus();
            return;
        } else if (toggleCountryTax.isChecked() && !validateEditText(etTAXIdentificationNum, input_layout_TAXIdentificationNum, "Entry your TAX Identification Num")) {
            return;
        } else if (toggleCountryTax.isChecked() && !ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(etTAXIdentificationNum.getText().toString().trim())) {
            etTAXIdentificationNumError.setVisibility(View.VISIBLE);
            etTAXIdentificationNumError.setText(ErrorMsgTaxIde);
            etTAXIdentificationNum.requestFocus();
            return;
        }
       /* else if (toggleCountryTax.isChecked() && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etCountryTax.getText().toString().trim())) {
            etCountryTaxError.setVisibility(View.VISIBLE);
            etCountryTaxError.setText(ErrorMsgName);
            etCountryTax.requestFocus();
            return;
        } else if (toggleCountryTax.isChecked() && !validateEditText(etTAXIdentificationNum, input_layout_TAXIdentificationNum, "Entry your TAX Identification Num")) {
            return;
        } else if (toggleCountryTax.isChecked() && !ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(etTAXIdentificationNum.getText().toString().trim())) {
            etTAXIdentificationNumError.setVisibility(View.VISIBLE);
            etTAXIdentificationNumError.setText(ErrorMsgTaxIde);
            etTAXIdentificationNum.requestFocus();
            return;
        } */
        else if (!cbDeclaration.isChecked()) {
            commonMsgDialog(mContext, getResources().getString(R.string.fatca_declaration_warning), false, "");
            return;
        } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etBldgNameCorres.getText()
            .toString()
            .trim()
            .equalsIgnoreCase(etFlatNoCorres.getText().toString().trim())) {
            commonMsgDialog(mContext, Correspondence_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
            return;
        } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etFlatNoCorres.getText()
            .toString()
            .trim()
            .equalsIgnoreCase(etAreaCorres.getText().toString().trim())) {
            commonMsgDialog(mContext, Correspondence_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
            return;
        } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etBldgNameCorres.getText()
            .toString()
            .trim()
            .equalsIgnoreCase(etAreaCorres.getText().toString().trim())) {
            commonMsgDialog(mContext, Correspondence_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
            return;
        } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etBldgNamePerm.getText()
            .toString()
            .trim()
            .equalsIgnoreCase(etFlatNamePerm.getText().toString().trim())) {
            commonMsgDialog(mContext, Permanent_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
            return;
        } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etFlatNamePerm.getText()
            .toString()
            .trim()
            .equalsIgnoreCase(etAreaPerm.getText().toString().trim())) {
            commonMsgDialog(mContext, Permanent_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
            return;
        } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etBldgNamePerm.getText()
            .toString()
            .trim()
            .equalsIgnoreCase(etAreaPerm.getText().toString().trim())) {
            commonMsgDialog(mContext, Permanent_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
            return;
        } else {

            //                EkycMainActivity.vpApply.setCurrentItem(1);
            //if (isPersonalDetailDone() && isSegmentSelected() && isWorkDetailDone()) {
            //    EkycMainActivity.vpApply.setCurrentItem(1);
            //    setIsPersonalDetailUpdated(true);
            //} else {
            etTitleError.setVisibility(View.GONE);
            etMotherTitleError.setVisibility(View.GONE);
            etMaidenTitleError.setVisibility(View.GONE);
            etFatherSpouseSelError.setVisibility(View.GONE);
//            etOtherResidenceError.setVisibility(View.GONE);
            etOccupationError.setVisibility(View.GONE);
            etQualificationError.setVisibility(View.GONE);
            etStockPortfolioError.setVisibility(View.GONE);
            etEqExperienceError.setVisibility(View.GONE);
            etEqDerivativeExpError.setVisibility(View.GONE);
            etCommExperienceError.setVisibility(View.GONE);
            etCommOtherExpError.setVisibility(View.GONE);
            etMFPEPStatusError.setVisibility(View.GONE);

            removeKeyboard(mContext, btnNext);
            updateFatcaAnswers();
            setClientName(etFirstName.getText().toString() + " " + etMiddleName.getText().toString() + " " + etLastName.getText().toString());
        }

    }

    public void validateData() {
        try {
            //check if new segment is selected in new_segment_flow
            if (isThisSecondProductFlow()) {
                if (isEquitySegmentExist()) {
                    if (!getIsCommoditySelected()) {
                        commonMsgDialog(mContext, SELECT_ATLEAST_ONE_SEG, false, "");
                        return;
                    }
                } else if (isCommoditySegmentExist()) {
                    if (!getIsEquitySelected()) {
                        commonMsgDialog(mContext, SELECT_ATLEAST_ONE_SEG, false, "");
                        return;
                    }
                }
            }

            if (getInterestedInMF()) {
                validateMFData();
            } else {
                if (!getIsEquitySelected() && !getIsCommoditySelected() && !getIsDerivativeSelected() && !getIsAllSelected() && !getIsCurrencySelected()) {
                    commonMsgDialog(mContext, SELECT_ATLEAST_ONE_SEG, false, "");
                    return;
                } else if (getAppType().equalsIgnoreCase(PP) && getIsEquitySelected() && (spEqBranchCode.getSelectedItem() == null || ((RowData) spEqBranchCode.getSelectedItem()).getKey().equals("-1"))) {
                    etEqBranchCodeError.setVisibility(View.VISIBLE);
                    etEqBranchCodeError.setText("Select equity branch code");
                    etEqBranchCodeError.requestFocus();
                    return;
                } else if (getAppType().equalsIgnoreCase(PP) && getIsEquitySelected() && (spEqSubBroker.getSelectedItem() == null || ((RowData) spEqSubBroker.getSelectedItem()).getKey().equals("-1"))) {
                    etEqSubrokerCodeError.setVisibility(View.VISIBLE);
                    etEqSubrokerCodeError.setText("Select equity subbroker code");
                    etEqSubrokerCodeError.requestFocus();
                    return;
                } else if (getAppType().equalsIgnoreCase(PP) && getIsEquitySelected() && (spEqTrader.getSelectedItem() == null || ((RowData) spEqTrader.getSelectedItem()).getKey().equals("-1"))) {
                    etEqTraderCodeError.setVisibility(View.VISIBLE);
                    etEqTraderCodeError.setText("Select equity trader code");
                    etEqTraderCodeError.requestFocus();
                    return;
                } else if (getAppType().equalsIgnoreCase(PP) && getIsEquitySelected() && (spEqArea.getSelectedItem() == null || ((RowData) spEqArea.getSelectedItem()).getKey().equals("-1"))) {
                    etEqAreaError.setVisibility(View.VISIBLE);
                    etEqAreaError.setText("Select equity area");
                    etEqAreaError.requestFocus();
                    return;
                } else if (getAppType().equalsIgnoreCase(PP) && getIsEquitySelected() && (spEqRegion.getSelectedItem() == null || ((RowData) spEqRegion.getSelectedItem()).getKey().equals("-1"))) {
                    etEqRegionError.setVisibility(View.VISIBLE);
                    etEqRegionError.setText("Select equity region");
                    etEqRegionError.requestFocus();
                    return;
                } else if (getAppType().equalsIgnoreCase(PP) && getIsCommoditySelected() && (spCommBranchCode.getSelectedItem() == null || ((RowData) spCommBranchCode.getSelectedItem()).getKey().equals("-1"))) {
                    etCommBranchCodeError.setVisibility(View.VISIBLE);
                    etCommBranchCodeError.setText("Select commodity branch code");
                    etCommBranchCodeError.requestFocus();
                    return;
                } else if (getAppType().equalsIgnoreCase(PP) && getIsCommoditySelected() && (spCommSubBroker.getSelectedItem() == null || ((RowData) spCommSubBroker.getSelectedItem()).getKey().equals("-1"))) {
                    etCommSubrokerCodeError.setVisibility(View.VISIBLE);
                    etCommSubrokerCodeError.setText("Select commodity subbroker code");
                    etCommSubrokerCodeError.requestFocus();
                    return;
                } else if (getAppType().equalsIgnoreCase(PP) && getIsCommoditySelected() && (spCommTrader.getSelectedItem() == null || ((RowData) spCommTrader.getSelectedItem()).getKey().equals("-1"))) {
                    etCommTraderCodeError.setVisibility(View.VISIBLE);
                    etCommTraderCodeError.setText("Select commodity trader code");
                    etCommTraderCodeError.requestFocus();
                    return;
                } else if (getAppType().equalsIgnoreCase(PP) && getIsCommoditySelected() && (spCommArea.getSelectedItem() == null || ((RowData) spCommArea.getSelectedItem()).getKey().equals("-1"))) {
                    etCommAreaError.setVisibility(View.VISIBLE);
                    etCommAreaError.setText("Select commodity area");
                    etCommAreaError.requestFocus();
                    return;
                } else if (getAppType().equalsIgnoreCase(PP) && getIsCommoditySelected() && (spCommRegion.getSelectedItem() == null || ((RowData) spCommRegion.getSelectedItem()).getKey().equals("-1"))) {
                    etCommRegionError.setVisibility(View.VISIBLE);
                    etCommRegionError.setText("Select commodity region");
                    etCommRegionError.requestFocus();
                    return;
                } else if (spTitle.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    etTitleError.setVisibility(View.VISIBLE);
                    etTitleError.setText("Select title");
                    etTitleError.requestFocus();
                    return;
                } else if (!validateEditText(etFirstName, input_layout_firstname, "Enter first name")) {
                    return;
                } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etFirstName.getText().toString().trim())) {
                    etFirstNameError.setVisibility(View.VISIBLE);
                    etFirstNameError.setText(ErrorMsgName);
                    etFirstName.requestFocus();
                    return;
                } else if (etMiddleName.getText().toString().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etMiddleName.getText().toString().trim())) {
                    etMiddleNameError.setVisibility(View.VISIBLE);
                    etMiddleNameError.setText(ErrorMsgName);
                    etMiddleName.requestFocus();
                    return;
                } else if (!validateEditText(etLastName, input_layout_lastname, "Enter last name")) {
                    return;
                } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etLastName.getText().toString().trim())) {
                    etLastNameError.setVisibility(View.VISIBLE);
                    etLastNameError.setText(ErrorMsgName);
                    etLastName.requestFocus();
                    return;
                } else if (!(rbMale.isChecked()) && !(rbFemale.isChecked())) {
                    etGenderError.setVisibility(View.VISIBLE);
                    etGenderError.setText("Select gender");
                    etGenderError.requestFocus();
                    return;
                } else if (!(rbMarried.isChecked()) && !(rbUnMarried.isChecked())) {
                    etmaritalError.setVisibility(View.VISIBLE);
                    etmaritalError.setText("Select marital status");
                    etmaritalError.requestFocus();
                    return;
                } else if (!validateEditText(etDOB, input_layout_dob, "Date of birth( as per PAN)")) {
                    etDOB.setEnabled(true);
                    etDOBRequestFocus.requestFocus();
                    return;
                } else if (!validateEditText(etBirthPlace, input_layout_birthPlace, "Enter birth place")) {
                    return;
                } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etBirthPlace.getText().toString().trim())) {
                    etBirthPlaceError.setVisibility(View.VISIBLE);
                    etBirthPlaceError.setText(ErrorMsgName);
                    etBirthPlace.requestFocus();
                    return;
                } else if (spFatherSpouseSel.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    etFatherSpouseSelError.setVisibility(View.VISIBLE);
                    etFatherSpouseSelError.setText("Select father/spouse");
                    etFatherSpouseSelError.requestFocus();
                    return;
                } else if (spFatherSpouseTitle.getSelectedItem().toString().equalsIgnoreCase(SELECT_TITLE)) {
                    etFatherSpouseTitleError.setVisibility(View.VISIBLE);
                    etFatherSpouseTitleError.setText("Select title");
                    etFatherSpouseTitleError.requestFocus();
                    return;
                } else if (!validateEditText(etFatherSpouseFirstName, input_layout_fatherSpouseFirstName, "Enter father/spouse first name")) {
                    return;
                } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etFatherSpouseFirstName.getText().toString().trim())) {
                    etFatherSpouseFirstNameError.setVisibility(View.VISIBLE);
                    etFatherSpouseFirstNameError.setText(ErrorMsgName);
                    etFatherSpouseFirstName.requestFocus();
                    return;
                } else if (etFatherSpouseMidName.getText().toString().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etFatherSpouseMidName.getText().toString().trim())) {
                    etFatherSpouseMidNameError.setVisibility(View.VISIBLE);
                    etFatherSpouseMidNameError.setText(ErrorMsgName);
                    etFatherSpouseMidName.requestFocus();
                    return;
                } else if (!validateEditText(etFatherSpouseLastName, input_layout_fatherSpouseLastName, "Enter father/spouse last name")) {
                    return;
                } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etFatherSpouseLastName.getText().toString().trim())) {
                    etFatherSpouseLastNameError.setVisibility(View.VISIBLE);
                    etFatherSpouseLastNameError.setText(ErrorMsgName);
                    etFatherSpouseLastName.requestFocus();
                    return;
                } else if (spMotherTitle.getSelectedItem().toString().equalsIgnoreCase(SELECT_TITLE)) {
                    etMotherTitleError.setVisibility(View.VISIBLE);
                    etMotherTitleError.setText("Select title");
                    etMotherTitleError.requestFocus();
                    return;
                } else if (!validateEditText(etMotherFirstName, input_layout_MotherFirstname, "Enter mother first name")) {
                    return;
                } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etMotherFirstName.getText().toString().trim())) {
                    etMotherFirstNameError.setVisibility(View.VISIBLE);
                    etMotherFirstNameError.setText(ErrorMsgName);
                    etMotherFirstName.requestFocus();
                    return;
                } else if (etMotherMiddleName.getText().toString().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etMotherMiddleName.getText().toString().trim())) {
                    etMotherMiddleNameError.setVisibility(View.VISIBLE);
                    etMotherMiddleNameError.setText(ErrorMsgName);
                    etMotherMiddleName.requestFocus();
                    return;
                } else if (!validateEditText(etMotherLastName, input_layout_MotherLastname, "Enter mother last name")) {
                    return;
                } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etMotherLastName.getText().toString().trim())) {
                    etMotherLastNameError.setVisibility(View.VISIBLE);
                    etMotherLastNameError.setText(ErrorMsgName);
                    etMotherLastName.requestFocus();
                    return;
                } else if (rbFemale.isChecked() && rbMarried.isChecked() && spMaidenTitle.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    etMaidenTitleError.setVisibility(View.VISIBLE);
                    etMaidenTitleError.setText("Select title");
                    etMaidenTitleError.requestFocus();
                    return;
                } else if (rbFemale.isChecked() && rbMarried.isChecked() && !validateEditText(etMaidenFirstName, input_layout_MaidenFirstname, "Enter maiden first name")) {
                    return;
                } else if (rbFemale.isChecked() && rbMarried.isChecked() && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etMaidenFirstName.getText().toString().trim())) {
                    etMaidenFirstNameError.setVisibility(View.VISIBLE);
                    etMaidenFirstNameError.setText(ErrorMsgName);
                    etMaidenFirstName.requestFocus();
                    return;
                } else if (rbFemale.isChecked() && rbMarried.isChecked() && etMaidenMiddleName.getText().toString().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etMaidenMiddleName.getText().toString().trim())) {
                    etMaidenMiddleNameError.setVisibility(View.VISIBLE);
                    etMaidenMiddleNameError.setText(ErrorMsgName);
                    etMaidenMiddleName.requestFocus();
                    return;
                } else if (rbFemale.isChecked() && rbMarried.isChecked() && !validateEditText(etMaidenLastName, input_layout_MaidenLastname, "Enter maiden last name")) {
                    return;
                } else if (rbFemale.isChecked() && rbMarried.isChecked() && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etMaidenLastName.getText().toString().trim())) {
                    etMaidenLastNameError.setVisibility(View.VISIBLE);
                    etMaidenLastNameError.setText(ErrorMsgName);
                    etMaidenLastName.requestFocus();
                    return;
                } else if (!isBlank(etAadhar.getText().toString()) && !getIsAadharVerified() && !getIsCVLVerified() && etAadhar.getText().toString().length() < 12) {
                    input_layout_aadhar.setError("Enter valid aadhaar number");
                    etAadhar.requestFocus();
                    return;
                } else if (aadhaarByPass && etAadhar.getText().toString().length() < 12) {
                    input_layout_aadhar.setError("Enter valid aadhaar number");
                    etAadhar.requestFocus();
                } else if (!isBlank(etGST.getText().toString()) && !ValidationRule.validateGST(etGST.getText().toString().trim())) {
                    input_layout_GST.setError(ErrorMsgGST);
                    etGST.requestFocus();
                    return;
                } /*else if (!validateEditText(etPAN, input_layout_PAN, "Enter PAN number")) {
                etPAN.setEnabled(true);
                return;
              *//*  input_layout_PAN.setError("Enter PAN number");
                input_layout_PAN.setEnabled(true);
                input_layout_PAN.requestFocus();*//*
            }*/ /*else if (!validateEditText(etAadhar, input_layout_aadhar, "Enter Aadhar number")) {
                etAadhar.setEnabled(true);
                return;
               *//* input_layout_aadhar.setError("Enter Aadhar number");
                input_layout_aadhar.setEnabled(true);
                input_layout_aadhar.requestFocus();*//*
            } */ /*else if (!validateEditText(etNameOnPAN, input_layout_nameOnPAN, "Enter name as on PAN")) {
                etNameOnPAN.setEnabled(true);
                return;
              *//*  input_layout_nameOnPAN.setError("Enter name as on PAN");
                input_layout_nameOnPAN.setEnabled(true);
                input_layout_nameOnPAN.requestFocus();*//*
            }*/ /*else if (spOtherResidence.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    etOtherResidenceError.setVisibility(View.VISIBLE);
                    etOtherResidenceError.setText("Select");
                    etOtherResidenceError.requestFocus();
                    return;
                }*/ else if (etBldgNameCorres.isEnabled() && !validateEditText(etBldgNameCorres, input_layout_bldgNameCorres, "Enter bldg no/name")) {
                    input_layout_aadhar.setErrorEnabled(false);
                    return;
                } else if (etBldgNameCorres.isEnabled() && !etBldgNameCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etBldgNameCorres.getText().toString())) {
                    input_layout_bldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                    etBldgNameCorres.requestFocus();
                    return;
                } else if (etFlatNoCorres.isEnabled() && !validateEditText(etFlatNoCorres, input_layout_flatNoCorres, "Enter flat no/road name")) {
                    return;
                } else if (etFlatNoCorres.isEnabled() && !etFlatNoCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etFlatNoCorres.getText().toString())) {
                    input_layout_flatNoCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                    etFlatNoCorres.requestFocus();
                    return;
                } else if (etAreaCorres.isEnabled() && !validateEditText(etAreaCorres, input_layout_areaCorres, "Enter Area")) {
                    return;
                } else if (etAreaCorres.isEnabled() && !etAreaCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etAreaCorres.getText().toString())) {
                    input_layout_areaCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                    etAreaCorres.requestFocus();
                    return;
                } else if (spCorresAddProof.getSelectedItem() == null || ((RowData) spCorresAddProof.getSelectedItem()).getKey().equals("-1")) {
                    etCorresAddProofError.setVisibility(View.VISIBLE);
                    etCorresAddProofError.setText("Select adderss proof");
                    etCorresAddProofError.requestFocus();
                    spCorresAddProof.setEnabled(true);
                    return;
                }/* else if (!validateEditText(etAddProofCorres,input_layout_addProofCorres,"Enter adderss proof")) {
               return;
            }*/ else if (etPincodeCorres.isEnabled() && !validateEditText(etPincodeCorres, input_layout_pincodeCorres, "Enter pincode")) {
                    return;
                } else if (etPincodeCorres.length() < 6) {
                    etPincodeCorres.requestFocus();
                    return;
                } else if (!(getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && (spCorresState.getSelectedItem() == null || ((RowData) spCorresState.getSelectedItem()).getKey().equals("-1"))) {
                    etCorresStateError.setVisibility(View.VISIBLE);
                    etCorresStateError.setText("Select state");
                    etCorresStateError.requestFocus();
                    cityFoundFlag = true;
                    spCorresState.setEnabled(true);
                    return;
                } else if ((getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && etCorresState.isEnabled() && etCorresState.getText().toString().trim().equals("")) {
                    etCorresStateError.setVisibility(View.VISIBLE);
                    etCorresStateError.setText("Select state");
                    etCorresStateError.requestFocus();
//                cityFoundFlag = true;
                    etCorresState.setEnabled(true);
                    return;
                } else if ((getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && etCorresCity.isEnabled() && etCorresCity.getText().toString().trim().equals("")) {
                    etCorresCityError.setVisibility(View.VISIBLE);
                    etCorresCityError.setText("Select city");
                    etCorresCityError.requestFocus();
                    etCorresCity.setEnabled(true);
                    return;
                } else if (!(getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && (spCorresCity.getSelectedItem() == null || ((RowData) spCorresCity.getSelectedItem()).getKey().equals("-1"))) {
                    etCorresCityError.setVisibility(View.VISIBLE);
                    etCorresCityError.setText("Select city");
                    etCorresCityError.requestFocus();
                    cityFoundFlag = true;
                    spCorresState.setEnabled(true);
                    spCorresCity.setEnabled(true);
                    return;
                } else if (spCorresCountry.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    etCorresCountryError.setVisibility(View.VISIBLE);
                    etCorresCountryError.setText("Select Country");
                    etCorresCountryError.requestFocus();
                    spCorresCountry.setEnabled(true);
                    return;
                } else if (etBldgNamePerm.isEnabled() && !validateEditText(etBldgNamePerm, input_layout_bldgNamePerm, "Enter bldg no/name")) {
                    return;
                } else if (etBldgNamePerm.isEnabled() && !etBldgNamePerm.getText().toString().equals("") & !ValidationRule.validateAddress(etBldgNamePerm.getText().toString())) {
                    input_layout_bldgNamePerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                    etBldgNamePerm.requestFocus();
                    return;
                } else if (etFlatNamePerm.isEnabled() && !validateEditText(etFlatNamePerm, input_layout_flatNamePerm, "Enter flat no/road name")) {
                    return;
                } else if (etFlatNamePerm.isEnabled() && !etFlatNamePerm.getText().toString().equals("") && !ValidationRule.validateAddress(etFlatNamePerm.getText().toString())) {
                    input_layout_flatNamePerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                    etFlatNamePerm.requestFocus();
                    return;
                } else if (etAreaPerm.isEnabled() && !validateEditText(etAreaPerm, input_layout_areaPerm, "Enter Area")) {
                    return;
                } else if (etAreaPerm.isEnabled() && !etAreaPerm.getText().toString().equals("") && !ValidationRule.validateAddress(etAreaPerm.getText().toString())) {
                    input_layout_areaPerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                    etAreaPerm.requestFocus();
                    return;
                } else if (spPermAddProof.getSelectedItem() == null || ((RowData) spPermAddProof.getSelectedItem()).getKey().equals("-1")) {
                    etPermAddProofError.setVisibility(View.VISIBLE);
                    etPermAddProofError.setText("Select adderss proof");
                    etPermAddProofError.requestFocus();
                    spPermAddProof.setEnabled(true);
                    return;
                } else if (etPincodePerm.isEnabled() && !validateEditText(etPincodePerm, input_layout_pincodePerm, "Enter pincode")) {
                    return;
                } else if (etPincodePerm.length() < 6) {
                    etPincodePerm.requestFocus();
                    return;
                } else if (!(getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && (spPermState.getSelectedItem() == null || ((RowData) spPermState.getSelectedItem()).getKey().equals("-1"))) {
                    etPermStateError.setVisibility(View.VISIBLE);
                    etPermStateError.setText("Select state");
                    etPermStateError.requestFocus();
                    cityFoundFlag = true;
                    spPermState.setEnabled(true);
                    return;
                } else if ((getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && etPermState.isEnabled() && etPermState.getText().toString().trim().equals("")) {
                    etPermStateError.setVisibility(View.VISIBLE);
                    etPermStateError.setText("Select state");
                    etPermStateError.requestFocus();
                    etPermState.setEnabled(true);
                    return;
//                cityFoundFlag = true;
                } else if ((getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && etPermCity.isEnabled() && etPermCity.getText().toString().trim().equals("")) {
                    etPermCityError.setVisibility(View.VISIBLE);
                    etPermCityError.setText("Select city");
                    etPermCityError.requestFocus();
                    etPermCity.setEnabled(true);
                    return;
//                cityFoundFlag = true;
                } else if (!(getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) && (spPermCity.getSelectedItem() == null || ((RowData) spPermCity.getSelectedItem()).getKey().equals("-1"))) {
                    etPermCityError.setVisibility(View.VISIBLE);
                    etPermCityError.setText("Select city");
                    etPermCityError.requestFocus();
                    cityFoundFlag = true;
                    spPermCity.requestFocus();
                    spPermState.setEnabled(true);
                    spPermCity.setEnabled(true);
                    return;
                } else if (spPermCountry.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    etPermCountryError.setVisibility(View.VISIBLE);
                    etPermCountryError.setText("Select Country");
                    etPermCountryError.requestFocus();
                    spPermCountry.setEnabled(true);
                    return;
                } else if (!validateEditText(etMobile, input_layout_mobile, "Enter mobile number")) {
                    etMobile.setEnabled(true);
                    return;
                } else if (etMobile.length() < 10) {
                    etMobile.setEnabled(true);
                    etMobile.requestFocus();
                    return;
                } else if (!ValidationRule.validateMobile(etMobile.getText().toString())) {
                    etMobile.setEnabled(true);
                    etMobile.requestFocus();
                    return;
                } else if (!validateEditText(etEmail, input_layout_email, "Enter email id")) {
                    etEmail.setEnabled(true);
                    return;
                } else if (!isValidEmail(etEmail.getText())) {
                    etEmail.setEnabled(true);
                    etEmail.requestFocus();
                    return;
                } else if ((etSTDNo.getText().toString().trim().length() > 0 || etTelNo.getText().toString().trim().length() > 0) && (!ValidationRule.validateTel(etSTDNo.getText().toString() + etTelNo.getText().toString()))) {
                    etSTDTelError.setVisibility(View.VISIBLE);
                    etSTDTelError.setText(Res_Telephone_number_min_11_digit);
                    etSTDNo.requestFocus();
                } else if (etFbId.getText().toString().trim().length() > 0 && !isValidEmail(etFbId.getText())) {
                    etFbIdError.setVisibility(View.VISIBLE);
                    etFbIdError.setText("Facebook id is not valid");
                    etFbId.requestFocus();
                } else if (etLinkedinID.getText().toString().trim().length() > 0 && !isValidEmail(etLinkedinID.getText())) {
                    etLinkedinIDError.setVisibility(View.VISIBLE);
                    etLinkedinIDError.setText("Linkedin id is not valid");
                    etLinkedinID.requestFocus();
                } else if (spOccupation.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    etOccupationError.setVisibility(View.VISIBLE);
                    etOccupationError.setText("Select occupation");
                    etOccupationError.requestFocus();
                    return;
                } else if (spQualification.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    etQualificationError.setVisibility(View.VISIBLE);
                    etQualificationError.setText("Select qualification");
                    etQualificationError.requestFocus();
                    return;
                } else if (spIncome.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    etIncomeError.setVisibility(View.VISIBLE);
                    etIncomeError.setText("Select income");
                    etIncomeError.requestFocus();
                    return;
                } else if (!validateEditText(etNetWorth, input_layout_netWorth, "Enter networth")) {
                    return;
                } else if (etNetWorth.getText().toString().equalsIgnoreCase("0") || (Double.parseDouble(etNetWorth.getText().toString()) <= 0)) {
                    etNetWorthError.setVisibility(View.VISIBLE);
                    etNetWorthError.setText("Net worth should be greater than 0");
                    etNetWorth.requestFocus();
                    return;
                } else if (!validateEditText(etNWDate, input_layout_NWDate, "Enter Networth as on date")) {
                    etNWDateRequestFocus.requestFocus();
                    return;
                } else if (!isDateIWthinYear(etNWDate.getText().toString())) {
                    etNWDateError.setVisibility(View.VISIBLE);
                    etNWDateError.setText(Date_should_not_be_less_than_one_year);
                    etNWDateError.requestFocus();
                    return;
                } else if (spStockPortfolio.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    etStockPortfolioError.setVisibility(View.VISIBLE);
                    etStockPortfolioError.setText("Select stock portfolio");
                    etStockPortfolioError.requestFocus();
                    return;
                } else if (getIsEquitySelected() && spEqExperience.getSelectedItem().toString().equalsIgnoreCase("Years in Stocks")) {
                    etEqExperienceError.setVisibility(View.VISIBLE);
                    etEqExperienceError.setText("Select equity experience");
                    etEqExperienceError.requestFocus();
                    return;
                } else if (getIsEquitySelected() && spEqDerivativeExp.getSelectedItem().toString().equalsIgnoreCase("Years in Derivative")) {
                    etEqDerivativeExpError.setVisibility(View.VISIBLE);
                    etEqDerivativeExpError.setText("Select derivative experience");
                    etEqDerivativeExpError.requestFocus();
                    return;
                } else if (getIsCommoditySelected() && spCommExperience.getSelectedItem().toString().equalsIgnoreCase("Years in Commodity")) {
                    etCommExperienceError.setVisibility(View.VISIBLE);
                    etCommExperienceError.setText("Select commodity investment experience");
                    etCommExperienceError.requestFocus();
                    return;
                } else if (getIsCommoditySelected() && spCommOtherExp.getSelectedItem().toString().equalsIgnoreCase("Years in Other Investment")) {
                    etCommOtherExpError.setVisibility(View.VISIBLE);
                    etCommOtherExpError.setText("Select commodity trading experience");
                    etCommOtherExpError.requestFocus();
                    return;
                } else if (getIsEquitySelected() && !cbEqIntraday.isChecked() && !cbEqPositional.isChecked() && !cbEqInvestment.isChecked()) {
                    etPurposeForEquityError.setVisibility(View.VISIBLE);
                    etPurposeForEquityError.setText("Select purpose for Equity");
                    etPurposeForEquityError.requestFocus();
                    return;
                } else if (getIsDerivativeSelected() && !cbDevTrading.isChecked() && !cbDevHedging.isChecked()) {
                    etPurposeForDerivativeError.setVisibility(View.VISIBLE);
                    etPurposeForDerivativeError.setText("Select purpose for Derivative");
                    etPurposeForDerivativeError.requestFocus();
                    return;
                } else if (getIsCommoditySelected() && !cbCommInvestment.isChecked() && !cbCommTrading.isChecked() && !cbCommHedging.isChecked() && !cbCommOther.isChecked()) {
                    etPurposeForCommodityError.setVisibility(View.VISIBLE);
                    etPurposeForCommodityError.setText("Select purpose for Commodity");
                    etPurposeForCommodityError.requestFocus();
                    return;
                } else if (cbCommOther.isChecked() && !validateEditText(etCommOther, input_layout_commOther, "Enter the purpose")) {
                    return;
                } else if (getIsCommoditySelected() && !cbCommercial.isChecked() && !cbNonCommercial.isChecked()) {
                    etCategorySelectionError.setVisibility(View.VISIBLE);
                    etCategorySelectionError.setText(getResources().getString(R.string.category_selection_error));
                    etCategorySelectionError.requestFocus();
                    return;
                } else if (getIsCommoditySelected() && ((cbCommercial.isChecked() && !cbCommercialBullion.isChecked() && !cbCommercialBaseMetals.isChecked() && !cbCommercialEnergy.isChecked()
                    && !cbCommercialAgriCommodities.isChecked()) || (cbNonCommercial.isChecked()
                    && !cbNonCommercialBullion.isChecked()
                    && !cbNonCommercialBaseMetals.isChecked()
                    && !cbNonCommercialEnergy.isChecked()
                    && !cbNonCommercialAgriCommodities.isChecked()))) {
                    etCategorySelectionError.setVisibility(View.VISIBLE);
                    etCategorySelectionError.setText(getResources().getString(R.string.category_selection_sub_error));
                    etCategorySelectionError.requestFocus();
                    return;
                } else if (toggleRegWithBroker.isChecked() && !validateEditText(etBrokerName, input_layout_brokerName, "Enter the broker name")) {
                    return;
                } else if (toggleRegWithBroker.isChecked() && !ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(etBrokerName.getText().toString().trim())) {
                    etBrokerNameError.setVisibility(View.VISIBLE);
                    etBrokerNameError.setText(ErrorMsgTaxIde);
                    etBrokerName.requestFocus();
                    return;
                } else if (toggleRegWithBroker.isChecked() && !validateEditText(etClientCode, input_layout_clientCode, "Enter the client code")) {
                    return;
                } else if (toggleRegWithBroker.isChecked() && !ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(etClientCode.getText().toString().trim())) {
                    etClientCodeError.setVisibility(View.VISIBLE);
                    etClientCodeError.setText(ErrorMsgTaxIde);
                    etClientCode.requestFocus();
                    return;
                } else if (toggleSEBIAuthority.isChecked() && !validateEditText(etSebiAuthority, input_layout_sebiAuthority, "Specify your action")) {
                    return;
                } else if (toggleFmcSebiAuthority.isChecked() && !validateEditText(etFmcSebiAuthority, input_layout_fmcSebiAuthority, "Specify your action")) {
                    return;
                } else if (toggleCountryCitizen.isChecked() && spCountryCitizenship.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    etCountryCitizenshipError.setVisibility(View.VISIBLE);
                    etCountryCitizenshipError.setText("Entry your Country of Citizenship");
                    etCountryCitizenshipError.requestFocus();
                    return;
                } else if (toggleCountryBirth.isChecked() && spCountryBirth.getSelectedItem().toString().endsWith(SELECT)) {
                    etCountryCitizenshipError.setVisibility(View.VISIBLE);
                    etCountryCitizenshipError.setText("Entry your Country of birth");
                    etCountryCitizenshipError.requestFocus();
                    return;
                }
                /*else if (toggleCountryBirth.isChecked() && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etCountryBirth.getText().toString().trim())) {
                    etCountryBirthError.setVisibility(View.VISIBLE);
                    etCountryBirthError.setText(ErrorMsgName);
                    etCountryBirth.requestFocus();
                    return;
                }*/
                else if (toggleCountryBirth.isChecked() && !validateEditText(etPlaceBirth, input_layout_PlaceBirth, "Entry your Place of birth")) {
                    return;
                } else if (toggleCountryBirth.isChecked() && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etPlaceBirth.getText().toString().trim())) {
                    etPlaceBirthError.setVisibility(View.VISIBLE);
                    etPlaceBirthError.setText(ErrorMsgName);
                    etPlaceBirth.requestFocus();
                    return;
                } else if (toggleCountryTax.isChecked() && spCountryTax.getSelectedItem().toString().equalsIgnoreCase(SELECT)) {
                    etCountryTaxError.setVisibility(View.VISIBLE);
                    etCountryTaxError.setText("Entry your Country of tax residency");
                    etCountryTaxError.requestFocus();
                    return;
                }
                /*else if (toggleCountryTax.isChecked() && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etCountryTax.getText().toString().trim())) {
                    etCountryTaxError.setVisibility(View.VISIBLE);
                    etCountryTaxError.setText(ErrorMsgName);
                    etCountryTax.requestFocus();
                    return;
                }*/
                else if (toggleCountryTax.isChecked() && !validateEditText(etTAXIdentificationNum, input_layout_TAXIdentificationNum, "Entry your TAX Identification Num")) {
                    return;
                } else if (toggleCountryTax.isChecked() && !ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(etTAXIdentificationNum.getText().toString().trim())) {
                    etTAXIdentificationNumError.setVisibility(View.VISIBLE);
                    etTAXIdentificationNumError.setText(ErrorMsgTaxIde);
                    etTAXIdentificationNum.requestFocus();
                    return;
                } else if (!cbDeclaration.isChecked()) {
                    commonMsgDialog(mContext, getResources().getString(R.string.fatca_declaration_warning), false, "");
                    return;
                } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etBldgNameCorres.getText()
                    .toString()
                    .trim()
                    .equalsIgnoreCase(etFlatNoCorres.getText().toString().trim())) {
                    commonMsgDialog(mContext, Correspondence_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    return;
                } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etFlatNoCorres.getText()
                    .toString()
                    .trim()
                    .equalsIgnoreCase(etAreaCorres.getText().toString().trim())) {
                    commonMsgDialog(mContext, Correspondence_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    return;
                } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etBldgNameCorres.getText()
                    .toString()
                    .trim()
                    .equalsIgnoreCase(etAreaCorres.getText().toString().trim())) {
                    commonMsgDialog(mContext, Correspondence_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    return;
                } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etBldgNamePerm.getText()
                    .toString()
                    .trim()
                    .equalsIgnoreCase(etFlatNamePerm.getText().toString().trim())) {
                    commonMsgDialog(mContext, Permanent_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    return;
                } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etFlatNamePerm.getText()
                    .toString()
                    .trim()
                    .equalsIgnoreCase(etAreaPerm.getText().toString().trim())) {
                    commonMsgDialog(mContext, Permanent_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    return;
                } else if (getAddressMode().equalsIgnoreCase(MANUAL) && etBldgNamePerm.getText()
                    .toString()
                    .trim()
                    .equalsIgnoreCase(etAreaPerm.getText().toString().trim())) {
                    commonMsgDialog(mContext, Permanent_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    return;
                } else {
//                EkycMainActivity.vpApply.setCurrentItem(1);
                    //if (isPersonalDetailDone() && isSegmentSelected() && isWorkDetailDone()) {
                    //    EkycMainActivity.vpApply.setCurrentItem(1);
                    //    setIsPersonalDetailUpdated(true);
                    //} else {
                    etTitleError.setVisibility(View.GONE);
                    etMotherTitleError.setVisibility(View.GONE);
                    etMaidenTitleError.setVisibility(View.GONE);
                    etFatherSpouseSelError.setVisibility(View.GONE);
//                    etOtherResidenceError.setVisibility(View.GONE);
                    etOccupationError.setVisibility(View.GONE);
                    etQualificationError.setVisibility(View.GONE);
                    etStockPortfolioError.setVisibility(View.GONE);
                    etEqExperienceError.setVisibility(View.GONE);
                    etEqDerivativeExpError.setVisibility(View.GONE);
                    etCommExperienceError.setVisibility(View.GONE);
                    etCommOtherExpError.setVisibility(View.GONE);

                    removeKeyboard(mContext, btnNext);
                    updateFatcaAnswers();
                    setClientName(etFirstName.getText().toString() + " " + etMiddleName.getText().toString() + " " + etLastName.getText().toString());
                }
            }
        } catch (Exception ex) {
            MOSLCommon.showToastMsg(mContext, "Some error occurred");
            ex.printStackTrace();
        }
    }

    private void franchiseeValidateData() {

        if (getIsEquitySelected() && (spEqBranchCode.getSelectedItem() == null || ((RowData) spEqBranchCode.getSelectedItem()).getKey().equals("-1"))) {
            etEqBranchCodeError.setVisibility(View.VISIBLE);
            etEqBranchCodeError.setText("Select equity branch code");
            etEqBranchCodeError.requestFocus();
        } else if (getIsEquitySelected() && (spEqSubBroker.getSelectedItem() == null || ((RowData) spEqSubBroker.getSelectedItem()).getKey().equals("-1"))) {
            etEqSubrokerCodeError.setVisibility(View.VISIBLE);
            etEqSubrokerCodeError.setText("Select equity subbroker code");
            etEqSubrokerCodeError.requestFocus();
        } else if (getIsEquitySelected() && (spEqTrader.getSelectedItem() == null || ((RowData) spEqTrader.getSelectedItem()).getKey().equals("-1"))) {
            etEqTraderCodeError.setVisibility(View.VISIBLE);
            etEqTraderCodeError.setText("Select equity trader code");
            etEqTraderCodeError.requestFocus();
        } else if (getIsEquitySelected() && (spEqArea.getSelectedItem() == null || ((RowData) spEqArea.getSelectedItem()).getKey().equals("-1"))) {
            etEqAreaError.setVisibility(View.VISIBLE);
            etEqAreaError.setText("Select equity area");
            etEqAreaError.requestFocus();
        } else if (getIsEquitySelected() && (spEqRegion.getSelectedItem() == null || ((RowData) spEqRegion.getSelectedItem()).getKey().equals("-1"))) {
            etEqRegionError.setVisibility(View.VISIBLE);
            etEqRegionError.setText("Select equity region");
            etEqRegionError.requestFocus();
        } else if (getIsCommoditySelected() && (spCommBranchCode.getSelectedItem() == null || ((RowData) spCommBranchCode.getSelectedItem()).getKey().equals("-1"))) {
            etCommBranchCodeError.setVisibility(View.VISIBLE);
            etCommBranchCodeError.setText("Select commodity branch code");
            etCommBranchCodeError.requestFocus();
        } else if (getIsCommoditySelected() && (spCommSubBroker.getSelectedItem() == null || ((RowData) spCommSubBroker.getSelectedItem()).getKey().equals("-1"))) {
            etCommSubrokerCodeError.setVisibility(View.VISIBLE);
            etCommSubrokerCodeError.setText("Select commodity subbroker code");
            etCommSubrokerCodeError.requestFocus();
        } else if (getIsCommoditySelected() && (spCommTrader.getSelectedItem() == null || ((RowData) spCommTrader.getSelectedItem()).getKey().equals("-1"))) {
            etCommTraderCodeError.setVisibility(View.VISIBLE);
            etCommTraderCodeError.setText("Select commodity trader code");
            etCommTraderCodeError.requestFocus();
        } else if (getIsCommoditySelected() && (spCommArea.getSelectedItem() == null || ((RowData) spCommArea.getSelectedItem()).getKey().equals("-1"))) {
            etCommAreaError.setVisibility(View.VISIBLE);
            etCommAreaError.setText("Select commodity area");
            etCommAreaError.requestFocus();
        } else if (getIsCommoditySelected() && (spCommRegion.getSelectedItem() == null || ((RowData) spCommRegion.getSelectedItem()).getKey().equals("-1"))) {
            etCommRegionError.setVisibility(View.VISIBLE);
            etCommRegionError.setText("Select commodity region");
            etCommRegionError.requestFocus();
        }

    }

    /**
     * Fatca details API
     */
    public void getFatcaAnswers() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
//            inputMap.put("UserId", "1627924");
//            inputMap.put("UserId", "1619879");
            inputMap.put("UserId", getUserID());
            String flag = "";
            if (getInterestedInMF()) {
                flag = GET_FATCADTLS_MF;
            } else {
                flag = GETFATCADTLS;
            }
            String category = PERSONAL;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            /**
                             * Country citizen table QuesID = 1 SubquesID 1
                             */
                            if (response.has("FATCADTLS1")) {
                                JSONArray jsonArray = response.getJSONArray("FATCADTLS1");
                                if (jsonArray.length() != 0) {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String quesId = (object.isNull("QUES_Id") || object.getString("QUES_Id").equals("")) ? "" : object.getString("QUES_Id").trim();
                                        String subquesId = (object.isNull("SubQues_Id") || object.getString("SubQues_Id").equals("")) ? "" : object.getString("SubQues_Id").trim();
                                        String answerFlag = (object.isNull("Answer_Flag") || object.getString("Answer_Flag").equals("")) ? "" : object.getString("Answer_Flag").trim();
                                        String answerDesc = (object.isNull("Answer_desc") || object.getString("Answer_desc").equals("")) ? "" : object.getString("Answer_desc").trim();
                                        String submitDeclarationFlg = (object.isNull("SUBMIT_DECLARATION_FLG") || object.getString("SUBMIT_DECLARATION_FLG").equals("")) ? "" : object.getString("SUBMIT_DECLARATION_FLG").trim();
                                        String answerChck = (object.isNull("ANSWER_CHCK") || object.getString("ANSWER_CHCK").equals("")) ? "" : object.getString("ANSWER_CHCK").trim();
                                        String declartionChck = (object.isNull("DECLARTION_CHCK") || object.getString("DECLARTION_CHCK").equals("")) ? "" : object.getString("DECLARTION_CHCK").trim();

                                        if (Integer.parseInt(answerFlag) == 1) {
                                            toggleCountryCitizen.setChecked(true);
                                            /*cbCountryCitizenYes.setChecked(true);
                                            cbCountryCitizenNo.setChecked(false);*/
                                            llCountryCitizenship.setVisibility(View.VISIBLE);
                                            // etCountryCitizenship.setText(answerDesc);
                                            getSpinnerData(arrAdapCountryFatca, spCountryCitizenship, answerDesc);
                                        } else if (Integer.parseInt(answerFlag) == 0) {
                                            toggleCountryCitizen.setChecked(false);
                                            /*cbCountryCitizenYes.setChecked(false);
                                            cbCountryCitizenNo.setChecked(true);*/
                                            llCountryCitizenship.setVisibility(View.GONE);
                                            // etCountryCitizenship.setText("");
                                            getSpinnerData(arrAdapCountryFatca, spCountryCitizenship, answerDesc);
                                        }
//                                        declarationSelect();
                                    }
                                    isFatcaCountryCitizenBlank = false;
                                } else {
                                    isFatcaCountryCitizenBlank = true;
                                }
                            }
                            /**
                             * Country birth table QuesID = 2 SubquesID 2 & 3 (2 for CountryBirth, 3 for PlaceBirth)
                             */
                            if (response.has("FATCADTLS2")) {
                                JSONArray jsonArray = response.getJSONArray("FATCADTLS2");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String quesId = (object.isNull("QUES_Id") || object.getString("QUES_Id").equals("")) ? "" : object.getString("QUES_Id").trim();
                                        String subquesId = (object.isNull("SubQues_Id") || object.getString("SubQues_Id").equals("")) ? "" : object.getString("SubQues_Id").trim();
                                        String answerFlag = (object.isNull("Answer_Flag") || object.getString("Answer_Flag").equals("")) ? "" : object.getString("Answer_Flag").trim();
                                        String answerDesc = (object.isNull("Answer_desc") || object.getString("Answer_desc").equals("")) ? "" : object.getString("Answer_desc").trim();
                                        String submitDeclarationFlg = (object.isNull("SUBMIT_DECLARATION_FLG") || object.getString("SUBMIT_DECLARATION_FLG").equals("")) ? "" : object.getString("SUBMIT_DECLARATION_FLG").trim();
                                        String answerChck = (object.isNull("ANSWER_CHCK") || object.getString("ANSWER_CHCK").equals("")) ? "" : object.getString("ANSWER_CHCK").trim();
                                        String declartionChck = (object.isNull("DECLARTION_CHCK") || object.getString("DECLARTION_CHCK").equals("")) ? "" : object.getString("DECLARTION_CHCK").trim();

                                        if (Integer.parseInt(answerFlag) == 1) {
                                            toggleCountryBirth.setChecked(true);
                                            /*cbCountryBirthYes.setChecked(true);
                                            cbCountryBirthNo.setChecked(false);*/
                                            llCountryBirth.setVisibility(View.VISIBLE);
                                            if (Integer.parseInt(quesId) == 2 && Integer.parseInt(subquesId) == 2) {
//                                                etCountryBirth.setText(answerDesc);
                                                getSpinnerData(arrAdapCountryFatca, spCountryBirth, answerDesc);
                                            } else if (Integer.parseInt(quesId) == 2 && Integer.parseInt(subquesId) == 3) {
                                                etPlaceBirth.setText(answerDesc);
                                            }
                                        } else if (Integer.parseInt(answerFlag) == 0) {
                                            toggleCountryBirth.setChecked(false);
                                           /* cbCountryBirthYes.setChecked(false);
                                            cbCountryBirthNo.setChecked(true);*/
                                            getSpinnerData(arrAdapCountryFatca, spCountryBirth, answerDesc);
                                            llCountryBirth.setVisibility(View.GONE);
//                                            etCountryBirth.setText("");
                                            etPlaceBirth.setText("");
                                        }
//                                        declarationSelect();
                                    }
                                    isFatcaCountryBirthBlank = false;
                                } else {
                                    isFatcaCountryBirthBlank = true;
                                }
                            }
                            /**
                             * Country tax table QuesID = 3 SubquesID 4 & 5(4 for CountryTax, 5 for TAXIdentificationNum)
                             */
                            if (response.has("FATCADTLS3")) {
                                JSONArray jsonArray = response.getJSONArray("FATCADTLS3");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String quesId = (object.isNull("QUES_Id") || object.getString("QUES_Id").equals("")) ? "" : object.getString("QUES_Id").trim();
                                        String subquesId = (object.isNull("SubQues_Id") || object.getString("SubQues_Id").equals("")) ? "" : object.getString("SubQues_Id").trim();
                                        String answerFlag = (object.isNull("Answer_Flag") || object.getString("Answer_Flag").equals("")) ? "" : object.getString("Answer_Flag").trim();
                                        String answerDesc = (object.isNull("Answer_desc") || object.getString("Answer_desc").equals("")) ? "" : object.getString("Answer_desc").trim();
                                        String submitDeclarationFlg = (object.isNull("SUBMIT_DECLARATION_FLG") || object.getString("SUBMIT_DECLARATION_FLG").equals("")) ? "" : object.getString("SUBMIT_DECLARATION_FLG").trim();
                                        String answerChck = (object.isNull("ANSWER_CHCK") || object.getString("ANSWER_CHCK").equals("")) ? "" : object.getString("ANSWER_CHCK").trim();
                                        String declartionChck = (object.isNull("DECLARTION_CHCK") || object.getString("DECLARTION_CHCK").equals("")) ? "" : object.getString("DECLARTION_CHCK").trim();

                                        if (Integer.parseInt(answerFlag) == 1) {
                                            toggleCountryTax.setChecked(true);
                                           /* cbCountryTaxYes.setChecked(true);
                                            cbCountryTaxNo.setChecked(false);*/
                                            llCountryTax.setVisibility(View.VISIBLE);
                                            if (Integer.parseInt(quesId) == 3 && Integer.parseInt(subquesId) == 4) {
//                                                etCountryTax.setText(answerDesc);
                                                getSpinnerData(arrAdapCountryFatca, spCountryTax, answerDesc);
                                            } else if (Integer.parseInt(quesId) == 3 && Integer.parseInt(subquesId) == 5) {
                                                etTAXIdentificationNum.setText(answerDesc);
                                            }
                                        } else if (Integer.parseInt(answerFlag) == 0) {
                                            toggleCountryTax.setChecked(false);
                                            /*cbCountryTaxYes.setChecked(false);
                                            cbCountryTaxNo.setChecked(true);*/
                                            llCountryTax.setVisibility(View.GONE);
//                                            etCountryTax.setText("");
                                            etTAXIdentificationNum.setText("");
                                            getSpinnerData(arrAdapCountryFatca, spCountryTax, answerDesc);
                                        }
//                                        declarationSelect();
                                    }
                                    isFatcaTAXBlank = false;
                                } else {
                                    isFatcaTAXBlank = true;
                                }
                            }
                            /**
                             * POA or a Mandate Holder table QuesID = 4 SubquesID ""
                             */
                            if (response.has("FATCADTLS4")) {
                                JSONArray jsonArray = response.getJSONArray("FATCADTLS4");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String quesId = (object.isNull("QUES_Id") || object.getString("QUES_Id").equals("")) ? "" : object.getString("QUES_Id").trim();
                                        String subquesId = (object.isNull("SubQues_Id") || object.getString("SubQues_Id").equals("")) ? "" : object.getString("SubQues_Id").trim();
                                        String answerFlag = (object.isNull("Answer_Flag") || object.getString("Answer_Flag").equals("")) ? "" : object.getString("Answer_Flag").trim();
                                        String answerDesc = (object.isNull("Answer_desc") || object.getString("Answer_desc").equals("")) ? "" : object.getString("Answer_desc").trim();
                                        String submitDeclarationFlg = (object.isNull("SUBMIT_DECLARATION_FLG") || object.getString("SUBMIT_DECLARATION_FLG").equals("")) ? "" : object.getString("SUBMIT_DECLARATION_FLG").trim();
                                        String answerChck = (object.isNull("ANSWER_CHCK") || object.getString("ANSWER_CHCK").equals("")) ? "" : object.getString("ANSWER_CHCK").trim();
                                        String declartionChck = (object.isNull("DECLARTION_CHCK") || object.getString("DECLARTION_CHCK").equals("")) ? "" : object.getString("DECLARTION_CHCK").trim();

                                        if (Integer.parseInt(answerFlag) == 1) {
                                            togglePoaAddress.setChecked(true);
                                           /* cbPoaAddressYes.setChecked(true);
                                            cbPoaAddressNo.setChecked(false);*/
                                        } else if (Integer.parseInt(answerFlag) == 0) {
                                            togglePoaAddress.setChecked(false);
                                           /* cbPoaAddressYes.setChecked(false);
                                            cbPoaAddressNo.setChecked(true);*/
                                        }
//                                        declarationSelect();
                                    }
                                    isFatcaPOABlank = false;
                                } else {
                                    isFatcaPOABlank = true;
                                }
                            }
                            /**
                             * Address or telephone number table QuesID = 5 SubquesID ""
                             */
                            if (response.has("FATCADTLS5")) {
                                JSONArray jsonArray = response.getJSONArray("FATCADTLS5");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String quesId = (object.isNull("QUES_Id") || object.getString("QUES_Id").equals("")) ? "" : object.getString("QUES_Id").trim();
                                        String subquesId = (object.isNull("SubQues_Id") || object.getString("SubQues_Id").equals("")) ? "" : object.getString("SubQues_Id").trim();
                                        String answerFlag = (object.isNull("Answer_Flag") || object.getString("Answer_Flag").equals("")) ? "" : object.getString("Answer_Flag").trim();
                                        String answerDesc = (object.isNull("Answer_desc") || object.getString("Answer_desc").equals("")) ? "" : object.getString("Answer_desc").trim();
                                        String submitDeclarationFlg = (object.isNull("SUBMIT_DECLARATION_FLG") || object.getString("SUBMIT_DECLARATION_FLG").equals("")) ? "" : object.getString("SUBMIT_DECLARATION_FLG").trim();
                                        String answerChck = (object.isNull("ANSWER_CHCK") || object.getString("ANSWER_CHCK").equals("")) ? "" : object.getString("ANSWER_CHCK").trim();
                                        String declartionChck = (object.isNull("DECLARTION_CHCK") || object.getString("DECLARTION_CHCK").equals("")) ? "" : object.getString("DECLARTION_CHCK").trim();

                                        if (Integer.parseInt(answerFlag) == 1) {
                                            toggleAddressOutsideIndia.setChecked(true);
                                           /* cbAddressOutsideIndiaYes.setChecked(true);
                                            cbAddressOutsideIndiaNo.setChecked(false);*/
                                        } else if (Integer.parseInt(answerFlag) == 0) {
                                            toggleAddressOutsideIndia.setChecked(false);
                                           /* cbAddressOutsideIndiaYes.setChecked(false);
                                            cbAddressOutsideIndiaNo.setChecked(true);*/
                                        }
//                                        declarationSelect();
                                    }
                                    isFatcaAddOutsideIndiaBlank = false;
                                } else {
                                    isFatcaAddOutsideIndiaBlank = true;
                                }
                            }
                            if (isFatcaCountryCitizenBlank && isFatcaCountryBirthBlank && isFatcaTAXBlank && isFatcaPOABlank && isFatcaAddOutsideIndiaBlank) {
                                enableField(llFatcaDetail);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                            alertDialog(mContext, 0, "Oops", SOME_ERROR_OCCURRED, "OK", true, null);
                        }
                        hideProgress();
                    } else {
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
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
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
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

    public void updateFatcaAnswers() {
        /**
         * Country citizen QuesID = 1 SubquesID 1
         * Country birth QuesID = 2 SubquesID 2 & 3 (2 for CountryBirth, 3 for PlaceBirth)
         * Country tax QuesID = 3 SubquesID 4 & 5(4 for CountryTax, 5 for TAXIdentificationNum)
         * POA or a Mandate Holder QuesID = 4 SubquesID ""
         * Address or telephone number QuesID = 5 SubquesID ""
         */
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
            inputMap.put("UserId", getUserID());
//            inputMap.put("UserId", "1627924");
            if (toggleCountryCitizen.isChecked()) {
                inputMap.put("CountryCit_QuesId", "1");
                inputMap.put("CountryCit_SubQId", "1");
                inputMap.put("Othr_CountCit_Flg", "1");
                inputMap.put("CountryCitizen", spCountryCitizenship.getSelectedItem().toString());
            } else {
                inputMap.put("CountryCit_QuesId", "1");
                inputMap.put("CountryCit_SubQId", "");
                inputMap.put("Othr_CountCit_Flg", "0");
                inputMap.put("CountryCitizen", "");
            }
            if (toggleCountryBirth.isChecked()) {
                inputMap.put("BirthCount_QuesId", "2");
                inputMap.put("BirthCount_SubQId", "2");
                inputMap.put("BirthCountPlace_SubQuesId", "3");
                inputMap.put("Othr_CountBirth_Flg", "1");
                inputMap.put("BirthCountry", spCountryBirth.getSelectedItem().toString().trim());
                inputMap.put("BirthPlace", etPlaceBirth.getText().toString().trim());
            } else {
                inputMap.put("BirthCount_QuesId", "2");
                inputMap.put("BirthCount_SubQId", "");
                inputMap.put("BirthCountPlace_SubQuesId", "");
                inputMap.put("Othr_CountBirth_Flg", "0");
                inputMap.put("BirthCountry", "");
                inputMap.put("BirthPlace", "");
            }
            if (toggleCountryTax.isChecked()) {
                inputMap.put("CountTax_QuesId", "3");
                inputMap.put("CountTax_SubQId", "4");
                inputMap.put("CountryTaxIDNum_SubQuesId", "5");
                inputMap.put("Othr_CountTax_Flg", "1");
                inputMap.put("CountryTax_Res", spCountryTax.getSelectedItem().toString());
                inputMap.put("TaxIdNum", etTAXIdentificationNum.getText().toString().trim());
            } else {
                inputMap.put("CountTax_QuesId", "3");
                inputMap.put("CountTax_SubQId", "");
                inputMap.put("CountryTaxIDNum_SubQuesId", "");
                inputMap.put("Othr_CountTax_Flg", "0");
                inputMap.put("CountryTax_Res", "");
                inputMap.put("TaxIdNum", "");
            }
            if (togglePoaAddress.isChecked()) {
                inputMap.put("POAHolder_QuesId", "4");
                inputMap.put("POAHolder_SubQId", "");
                inputMap.put("Othr_POAHolder_Flg", "1");
            } else {
                inputMap.put("POAHolder_QuesId", "4");
                inputMap.put("POAHolder_SubQId", "");
                inputMap.put("Othr_POAHolder_Flg", "0");
            }
            if (toggleAddressOutsideIndia.isChecked()) {
                inputMap.put("TelAddrs_QuesId", "5");
                inputMap.put("TelAddrs_SubQId", "");
                inputMap.put("Othr_Tel_Addrs_Flg", "1");
            } else {
                inputMap.put("TelAddrs_QuesId", "5");
                inputMap.put("TelAddrs_SubQId", "");
                inputMap.put("Othr_Tel_Addrs_Flg", "0");
            }
            if (cbDeclaration.isChecked()) {
                inputMap.put("Sub_Decl_Flg", "1");
            } else {
                inputMap.put("Sub_Decl_Flg", "0");
            }
            String flag = "";
            if (getInterestedInMF()) {
                flag = FATCAANSWER_INSERT_MF;
            } else {
                flag = FATCAANSWERINSERT;
            }
            String category = PERSONAL;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            /*{"UserId":null,"_Status":"1","_msg":""}*/
                            String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status").trim();
                            String msg = (response.isNull("_msg") || response.getString("_msg").equals("")) ? "" : response.getString("_msg");
                            if (Integer.parseInt(status) == 1) {
                                updatePersonalDetails();
                                hideUpdateProgress();
                            } else {
                                commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                    } else {
                        commonMsgDialog(mContext, SOME_SERVER_ERROR_OCCURRED, false, "");
//                        hideUpdateProgress();
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
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("Failure", "no resp in Data");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                }
            });
        } else {
            MOSLCommon.alertDInternet(mContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    /**
     * this method will hide progress when received the response of BINDSTATE, GETPERSONALDETAILS & GETFATCADTLS
     */
    private void hideProgress() {
        hideProgressCounter++;
        if (hideProgressCounter == 4) {
            hideProgressCounter = 0;
            customProgress.setVisibility(View.GONE);
        }
    }

    /**
     * this method will hide progress when update the response of UPDATEPERSONALDETAILS & FATCAANSWERINSERT
     */
    private void hideUpdateProgress() {
        hideUpdateProgressCounter++;
        if (hideUpdateProgressCounter == 2) {
            hideUpdateProgressCounter = 0;
            customProgress.setVisibility(View.GONE);
        }
    }

    private void showSegmentSelectionSection(boolean showSection) {
        if (showSection) {
            rlSegmentTitle.setVisibility(View.VISIBLE);
            llSegmentSelection.setVisibility(View.VISIBLE);
        } else {
            rlSegmentTitle.setVisibility(View.GONE);
            llSegmentSelection.setVisibility(View.GONE);
        }
    }

    public void equitySpinner() {
        if (getAppType().equalsIgnoreCase(PP)) {
            llEqSpinners.setVisibility(View.VISIBLE);
            tvRelationshipDetails.requestFocus();
            tvRelationshipDetails.setFocusable(true);
            tvRelationshipDetails.setFocusableInTouchMode(true);
        }
    }

    private void baEquitySpinnerTitle() {
        tvBranchCodeTitle.setText("Equity Branch Code");
        tvSubrokerCodeTitle.setText("Equity Subroker Code");
        tvTraderCodeTitle.setText("Equity Trader Code");
        tvAreaTitle.setText("Equity Area");
        tvRegionTitle.setText("Equity Region");
    }

    private void baMFSpinnerTitle() {
        tvBranchCodeTitle.setText("Branch Code");
        tvSubrokerCodeTitle.setText("Subroker Code");
        tvTraderCodeTitle.setText("Trader Code");
        tvAreaTitle.setText("Area");
        tvRegionTitle.setText("Region");
    }

    public void equitySelect() {
        llSEBIAuthorityMain.setVisibility(View.VISIBLE);
        llDealingWithEquity.setVisibility(View.VISIBLE);
        llEqExperience.setVisibility(View.VISIBLE);
        if (getIsDerivativeSelected()) {
            llDealWithDeri.setVisibility(View.VISIBLE);
        } else {
            llDealWithDeri.setVisibility(View.GONE);
        }
    }

    public void commoditySpinner() {
        if (getAppType().equalsIgnoreCase(PP)) {
            llCommSpinners.setVisibility(View.VISIBLE);
            tvRelationshipDetails.requestFocus();
            tvRelationshipDetails.setFocusable(true);
            tvRelationshipDetails.setFocusableInTouchMode(true);
        }
    }

    public void commoditySelect() {
        llFmcSEBIAuthorityMain.setVisibility(View.VISIBLE);
        llDealWithComm.setVisibility(View.VISIBLE);
        llCommExperience.setVisibility(View.VISIBLE);
        llCategorySelection.setVisibility(View.VISIBLE);
    }

    public void allSpinner() {
        if (getAppType().equalsIgnoreCase(PP)) {
            if (!isEquitySegmentExist()) {
                llEqSpinners.setVisibility(View.VISIBLE);
            }
            if (!isCommoditySegmentExist()) {
                llCommSpinners.setVisibility(View.VISIBLE);
            }
            tvRelationshipDetails.requestFocus();
            tvRelationshipDetails.setFocusable(true);
            tvRelationshipDetails.setFocusableInTouchMode(true);
        }
    }

    public void allSelect() {
        equitySelect();
        commoditySelect();
    }

    public void equityDeselectSpinner() {
        llEqSpinners.setVisibility(View.GONE);
    }

    public void equityDeselect() {
        llSEBIAuthorityMain.setVisibility(View.GONE);
        llDealingWithEquity.setVisibility(View.GONE);
        llDealWithDeri.setVisibility(View.GONE);
        llEqExperience.setVisibility(View.GONE);
    }

    public void commodityDeselectSpinner() {
        if (getAppType().equalsIgnoreCase(PP)) {
            llCommSpinners.setVisibility(View.GONE);
        }
    }

    public void commodityDeselect() {
        llFmcSEBIAuthorityMain.setVisibility(View.GONE);
        llDealWithComm.setVisibility(View.GONE);
        llCommExperience.setVisibility(View.GONE);
        llCategorySelection.setVisibility(View.GONE);
    }

    public void allDeselectSpinner() {
        if (getAppType().equalsIgnoreCase(PP)) {
            if (!isEquitySegmentExist() && !isEquityFundTransfered()) {
                llEqSpinners.setVisibility(View.GONE);
            }
            if (!isCommoditySegmentExist() && !isCommodityFundTransfered()) {
                llCommSpinners.setVisibility(View.GONE);
            }
        }
    }

    public void allDeselect() {
        if (!isEquitySegmentExist()) {
            if (!getIsEquitySelected()) {
                llEqExperience.setVisibility(View.GONE);
                llSEBIAuthorityMain.setVisibility(View.GONE);
                llDealingWithEquity.setVisibility(View.GONE);
            }
            if (!getIsDerivativeSelected()) {
                llDealWithDeri.setVisibility(View.GONE);
            }
        }
        if (!isCommoditySegmentExist() && !getIsCommoditySelected()) {
            llFmcSEBIAuthorityMain.setVisibility(View.GONE);
            llDealWithComm.setVisibility(View.GONE);
            llCommExperience.setVisibility(View.GONE);
            llCategorySelection.setVisibility(View.GONE);
        }
    }

    public void disableEnableField() {
        isDisableEnableFieldCalled = true;
        if (getInterestedInMF()) {
            if (isMfIpvDone() || isOfflineDocumentUploadedDone()) {//First product MF and Ipv is done
                showPaid();
                spTitle.setEnabled(false);
                rbMarried.setEnabled(false);
                rbUnMarried.setEnabled(false);
                tvMobile.setEnabled(false);
                tvEmail.setEnabled(false);
                disableField(llContactDetail);
                disableField(llWorkDetail);
                disableField(llFatcaDetail);
                tvFatcaDeclaration.setEnabled(true);
                disableField(llMFPEPStatus);
            }
            setErrorMessage();
            return;
        }

        //Second product check
        if (isThisSecondProductFlow()) {
            if (isMfSegmentExist() && !isEquitySegmentExist() && !isCommoditySegmentExist()) {//First product MF
                rbMale.setEnabled(false);
                rbFemale.setEnabled(false);
                etBirthPlace.setEnabled(false);
                disableField(llMotherDetail);
                disableField(llFatcaDetail);
                tvFatcaDeclaration.setEnabled(true);
                if (llMaidenName.getVisibility() == View.VISIBLE && !isBlank(etMaidenFirstName.getText().toString())) {
                    disableField(llMaidenName);
                }
                if (!isBlank(etGST.getText().toString())) {
                    etGST.setEnabled(false);
                }
            } else if (isEquitySegmentExist() || isCommoditySegmentExist()) {
                //First product equity or commodity
                showPaid();
                if (isEquitySegmentExist() && isEquityFundTransfered()) {//Equity exist and fund transfer
                    equityPaid();
                }
                if (isCommoditySegmentExist() && isCommodityFundTransfered()) {//commodity exist and fund transfer
                    commodityPaid();
                }
            }
        }

        if (isEquityFundTransfered() && isCommodityFundTransfered()) {
            //both segment selected and both are paid
            equityPaid();
            commodityPaid();
            showPaid();
            setErrorMessage();
            return;
        }

        if (getIsEquitySelected()) {
            if (isEquityFundTransfered()) { //paid
                showPaid();
                equityPaid();
                if (getIsCommoditySelected() && !isCommodityFundTransfered()) {
                    showCommPending();
                }
            } else {
                showEquitypending();
            }
        }

        if (getIsCommoditySelected()) {
            if (isCommodityFundTransfered()) { //paid
                showPaid();
                commodityPaid();
                if (getIsEquitySelected() && !isEquityFundTransfered()) {
                    showEquitypending();
                }
            } else {
                showCommPending();
            }
        }
        setErrorMessage();
        if (!(getInterestedInMF() && getAddressMode().equalsIgnoreCase(CVL))) {
            makeEnableIFDisableBroking();
        }

        if (!isIpvORDocumentStageDone()) {
            cbFamilyDeclaration.setEnabled(true);
        } else if (isIpvORDocumentStageDone() && ((getIsEquitySelected() && !isEquityFundTransfered()) ||
                (getIsCommoditySelected() && !isCommodityFundTransfered()))) {
            cbFamilyDeclaration.setEnabled(true);
        } else {
            cbFamilyDeclaration.setEnabled(false);
        }
    }

    /*
    * All common fields
    * */
    private void showPaid() {
        rbMale.setEnabled(false);
        rbFemale.setEnabled(false);
        etBirthPlace.setEnabled(false);
        disableField(llNameDetail);
        disableField(llFatherSpouseDetail);
        disableField(llMotherDetail);
        disableField(llFatcaDetail);
        tvFatcaDeclaration.setEnabled(true);

        if (llMaidenName.getVisibility() == View.VISIBLE && !isBlank(etMaidenFirstName.getText().toString())) {
            disableField(llMaidenName);
        }
        /*if (isEquitySegmentExist() || isCommoditySegmentExist() || isEquityFundTransfered() || isCommodityFundTransfered()) {
            disableField(llRegisterToOtherBroker);
            disableField(llBrokerInfo);
        }*/
        if (getAddressMode().equalsIgnoreCase("MANUAL")) {
            disableField(llCorresAddress);
            disableField(llPermAddress);
        }
        if (!isBlank(etGST.getText().toString())) {
            etGST.setEnabled(false);
        }
    }

    private void equityPaid() {
        if (isEquitySegmentExist() || isIpvORDocumentStageDone()) {
            disableField(llEqSpinners);
        }
        disableField(llContactDetail);
        disableField(llWorkDetail);
        disableField(llPEP);
        disableField(llRelatedPEP);

        disableField(llRegisterToOtherBroker);
        disableField(llBrokerInfo);
        spTitle.setEnabled(false);
        rbMarried.setEnabled(false);
        rbUnMarried.setEnabled(false);
        spEqExperience.setEnabled(false);
        spEqDerivativeExp.setEnabled(false);
        cbEqIntraday.setEnabled(false);
        cbEqPositional.setEnabled(false);
        cbEqInvestment.setEnabled(false);
        toggleSEBIAuthority.setEnabled(false);
        etSebiAuthority.setEnabled(false);
        if (isIpvORDocumentStageDone()) {
            tvMobile.setEnabled(false);
            tvEmail.setEnabled(false);
        }
        if (isEquitySegmentExist()) {
            cbDevTrading.setEnabled(false);
            cbDevHedging.setEnabled(false);
        } else {
            if (!isDerivativeSegmentExist()) {
                cbDevTrading.setEnabled(true);
                cbDevHedging.setEnabled(true);
            } else {
                cbDevTrading.setEnabled(false);
                cbDevHedging.setEnabled(false);
            }
        }
    }

    private void showEquitypending() {
        enableField(llEqSpinners);
        enableField(llContactDetail);
        enableField(llWorkDetail);
        enableField(llPEP);
        enableField(llRelatedPEP);
        enableField(llRegisterToOtherBroker);
        enableField(llBrokerInfo);
        spTitle.setEnabled(true);
        rbMarried.setEnabled(true);
        rbUnMarried.setEnabled(true);
        tvMobile.setEnabled(true);
        tvEmail.setEnabled(true);
        spEqExperience.setEnabled(true);
        spEqDerivativeExp.setEnabled(true);
        cbEqIntraday.setEnabled(true);
        cbEqPositional.setEnabled(true);
        cbEqInvestment.setEnabled(true);
        toggleSEBIAuthority.setEnabled(true);
        etSebiAuthority.setEnabled(true);
        if (!isDerivativeSegmentExist()) {
            cbDevTrading.setEnabled(true);
            cbDevHedging.setEnabled(true);
        } else {
            cbDevTrading.setEnabled(false);
            cbDevHedging.setEnabled(false);
        }
    }

    private void commodityPaid() {
        if (isCommoditySegmentExist() || isIpvORDocumentStageDone()) {
            disableField(llCommSpinners);
        }
        disableField(llContactDetail);
        disableField(llWorkDetail);
        disableField(llPEP);
        disableField(llRelatedPEP);
        disableField(llRegisterToOtherBroker);
        disableField(llBrokerInfo);
        disableField(llCategorySelection);
        ivCategoryInfo.setEnabled(true);
        spTitle.setEnabled(false);
        rbMarried.setEnabled(false);
        rbUnMarried.setEnabled(false);
        spCommExperience.setEnabled(false);
        spCommOtherExp.setEnabled(false);
        cbCommInvestment.setEnabled(false);
        cbCommTrading.setEnabled(false);
        cbCommHedging.setEnabled(false);
        cbCommOther.setEnabled(false);
        etCommOther.setEnabled(false);
        toggleFmcSebiAuthority.setEnabled(false);
        etFmcSebiAuthority.setEnabled(false);
        if (isIpvORDocumentStageDone()) {
            tvMobile.setEnabled(false);
            tvEmail.setEnabled(false);
        }
    }

    private void showCommPending() {
        enableField(llCommSpinners);
        enableField(llContactDetail);
        enableField(llWorkDetail);
        enableField(llPEP);
        enableField(llRelatedPEP);
        enableField(llRegisterToOtherBroker);
        enableField(llBrokerInfo);
//        enableField(llCategorySelection);
        cbCommercial.setEnabled(true);
        cbNonCommercial.setEnabled(true);
        spTitle.setEnabled(true);
        rbMarried.setEnabled(true);
        rbUnMarried.setEnabled(true);
        tvMobile.setEnabled(true);
        tvEmail.setEnabled(true);
        spCommExperience.setEnabled(true);
        spCommOtherExp.setEnabled(true);
        cbCommInvestment.setEnabled(true);
        cbCommTrading.setEnabled(true);
        cbCommHedging.setEnabled(true);
        cbCommOther.setEnabled(true);
        etCommOther.setEnabled(true);
        toggleFmcSebiAuthority.setEnabled(true);
        etFmcSebiAuthority.setEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && personalDetailView != null && viewPager.getCurrentItem() == getPersonalTab()) {
                loadPersonalDetails();
                setPermisson(getActivity(), mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    private boolean validateEditText(EditText editText, TextInputLayout textInputLayout, String error) {
        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(error);
            editText.requestFocus();
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }

    /*
    *  Address field enable disable code in cvl and aadhar verified condition for both MF and Broking product
    * */
    private void addressFieldDisable(View view, String value) {
        if (getInterestedInMF() && getIsCVLVerified()) {
            view.setEnabled(false);
        } else {
            if (isBlank(value)) {
                view.setEnabled(true);
            } else {
                view.setEnabled(false);
            }
        }

    }


    /*
    * Other field enable disable code in cvl verified condition for MF product
    * */
    private void otherFieldDisable(View view, String value) {
        if (isBlank(value) && getInterestedInMF() && getAddressMode().equalsIgnoreCase(CVL)) {
            view.setEnabled(true);
        } else {
            view.setEnabled(false);
        }
    }

    private void setErrorMessage() {
        if (!etBldgNameCorres.isEnabled()) {
            input_layout_bldgNameCorres.setErrorEnabled(false);
        }
        if (!etFlatNoCorres.isEnabled()) {
            input_layout_flatNoCorres.setErrorEnabled(false);
        }
        if (!etAreaCorres.isEnabled()) {
            input_layout_areaCorres.setErrorEnabled(false);
        }
        if (!etBldgNamePerm.isEnabled()) {
            input_layout_bldgNamePerm.setErrorEnabled(false);
        }
        if (!etFlatNamePerm.isEnabled()) {
            input_layout_flatNamePerm.setErrorEnabled(false);
        }
        if (!etAreaPerm.isEnabled()) {
            input_layout_areaPerm.setErrorEnabled(false);
        }
    }

    private void makeEnableIFDisableBroking() {

        if (isBlank(title)) {
            spTitle.setEnabled(true);
        }
        if (isBlank(etFirstName.getText().toString())) {
            etFirstName.setEnabled(true);
        }
        if (isBlank(etMiddleName.getText().toString())) {
            etMiddleName.setEnabled(true);
        }
        if (isBlank(etLastName.getText().toString())) {
            etLastName.setEnabled(true);
        }
        if (!rbMale.isChecked() && !rbFemale.isChecked()) {
            rbMale.setEnabled(true);
            rbFemale.setEnabled(true);
        }
        if (isBlank(etDOB.getText().toString())) {
            etDOB.setEnabled(true);
        }
        if (!rbMarried.isChecked() && !rbUnMarried.isChecked()) {
            rbMarried.setEnabled(true);
            rbUnMarried.setEnabled(true);
        }
        if (isBlank(etNameOnPAN.getText().toString())) {
            etNameOnPAN.setEnabled(true);
        }
        if (isBlank(etBirthPlace.getText().toString())) {
            etBirthPlace.setEnabled(true);
        }
        if (isBlank(relation)) {
            spFatherSpouseSel.setEnabled(true);
        }
        if (isBlank(fhTitle)) {
            spFatherSpouseTitle.setEnabled(true);
        }
        if (isBlank(etFatherSpouseFirstName.getText().toString())) {
            etFatherSpouseFirstName.setEnabled(true);
        }
        if (isBlank(etFatherSpouseMidName.getText().toString())) {
            etFatherSpouseMidName.setEnabled(true);
        }
        if (isBlank(etFatherSpouseLastName.getText().toString())) {
            etFatherSpouseLastName.setEnabled(true);
        }
        if (isBlank(motherTitle)) {
            spMotherTitle.setEnabled(true);
        }
        if (isBlank(etMotherFirstName.getText().toString())) {
            etMotherFirstName.setEnabled(true);
        }
        if (isBlank(etMotherMiddleName.getText().toString())) {
            etMotherMiddleName.setEnabled(true);
        }
        if (isBlank(etMotherLastName.getText().toString())) {
            etMotherLastName.setEnabled(true);
        }
        if (llMaidenName.getVisibility() == View.VISIBLE && isBlank(maidenTitle)) {
            spMaidenTitle.setEnabled(true);
        }
        if (llMaidenName.getVisibility() == View.VISIBLE && isBlank(etMaidenFirstName.getText().toString())) {
            etMaidenFirstName.setEnabled(true);
        }
        if (llMaidenName.getVisibility() == View.VISIBLE && isBlank(etMaidenMiddleName.getText().toString())) {
            etMaidenMiddleName.setEnabled(true);
        }
        if (llMaidenName.getVisibility() == View.VISIBLE && isBlank(etMaidenLastName.getText().toString())) {
            etMaidenLastName.setEnabled(true);
        }
        if (isBlank(etSTDNo.getText().toString())) {
            etSTDNo.setEnabled(true);
        }
        if (isBlank(etTelNo.getText().toString())) {
            etTelNo.setEnabled(true);
        }
        if (isBlank(etFbId.getText().toString())) {
            etFbId.setEnabled(true);
        }
        if (isBlank(etTwitterId.getText().toString())) {
            etTwitterId.setEnabled(true);
        }
        if (isBlank(etLinkedinID.getText().toString())) {
            etLinkedinID.setEnabled(true);
        }
        if (isBlank(occupationDetails)) {
            spOccupation.setEnabled(true);
        }
        if (isBlank(educationQualification)) {
            spQualification.setEnabled(true);
        }
        if (isBlank(annualIncome)) {
            spIncome.setEnabled(true);
        }
        if (isBlank(etNetWorth.getText().toString())) {
            etNetWorth.setEnabled(true);
        }
        if (isBlank(etNWDate.getText().toString())) {
            etNWDate.setEnabled(true);
        }
        if (isBlank(stockPortfolio)) {
            spStockPortfolio.setEnabled(true);
        }
        if (togglePoliticaly.getVisibility() == View.VISIBLE && isBlank(politicallyExposedPerson)) {
            togglePoliticaly.setEnabled(true);
        }
        if (toggleRelatedPolitical.getVisibility() == View.VISIBLE && isBlank(relatedPoliticallyExposedPerson)) {
            toggleRelatedPolitical.setEnabled(true);
        }
        if (toggleRegWithBroker.getVisibility() == View.VISIBLE && isBlank(brokerName)) {
            toggleRegWithBroker.setEnabled(true);
        }
        if (spCommExperience.getVisibility() == View.VISIBLE && isBlank(comStocExp)) {
            spCommExperience.setEnabled(true);
        }
        if (spCommOtherExp.getVisibility() == View.VISIBLE && isBlank(comOtherExp)) {
            spCommOtherExp.setEnabled(true);
        }
        if (toggleSEBIAuthority.getVisibility() == View.VISIBLE && isBlank(eqPastAction) && !isEquitySegmentExist()) {
            toggleSEBIAuthority.setEnabled(true);
        }
        if (toggleFmcSebiAuthority.getVisibility() == View.VISIBLE && isBlank(comPastAction) && !isCommoditySegmentExist()) {
            toggleFmcSebiAuthority.setEnabled(true);
        }
        if (llDealWithComm.getVisibility() == View.VISIBLE && !cbCommInvestment.isChecked() && !cbCommTrading.isChecked() && !cbCommHedging.isChecked() && !cbCommOther.isChecked()) {
            cbCommInvestment.setEnabled(true);
            cbCommTrading.setEnabled(true);
            cbCommHedging.setEnabled(true);
            cbCommOther.setEnabled(true);
        }
        if (llCategorySelection.getVisibility() == View.VISIBLE && !cbCommercial.isChecked() && !cbNonCommercial.isChecked()) {
            cbCommercial.setEnabled(true);
            cbNonCommercial.setEnabled(true);
        }
        if (spEqExperience.getVisibility() == View.VISIBLE && isBlank(eqStockExp)) {
            spEqExperience.setEnabled(true);
        }
        if (spEqDerivativeExp.getVisibility() == View.VISIBLE && isBlank(derivativeExp)) {
            spEqDerivativeExp.setEnabled(true);
        }
        /*if (toggleSEBIAuthority.getVisibility() == View.VISIBLE && isBlank(eqIntraDay)) {
            toggleSEBIAuthority.setEnabled(true);
        }*/
        if (llDealingWithEquity.getVisibility() == View.VISIBLE && !cbEqIntraday.isChecked() && !cbEqPositional.isChecked() && !cbEqInvestment.isChecked()) {
            cbEqIntraday.setEnabled(true);
            cbEqPositional.setEnabled(true);
            cbEqInvestment.setEnabled(true);
        }
        if (llDealWithDeri.getVisibility() == View.VISIBLE && !cbDevTrading.isChecked() && !cbDevHedging.isChecked()) {
            cbDevTrading.setEnabled(true);
            cbDevHedging.setEnabled(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (etBldgNamePerm.getText().toString().equals("") && input_layout_bldgNamePerm.isErrorEnabled()) {
            input_layout_bldgNamePerm.setErrorEnabled(false);
        } else if ((isDisableEnableFieldCalled && etBldgNamePerm.isEnabled()) && !etBldgNamePerm.getText().toString().equals("") & !ValidationRule.validateAddress(etBldgNamePerm.getText().toString())) {
            input_layout_bldgNamePerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            input_layout_bldgNamePerm.setErrorEnabled(false);
        }
        if (etFlatNamePerm.getText().toString().equals("") && input_layout_flatNamePerm.isErrorEnabled()) {
            input_layout_flatNamePerm.setErrorEnabled(false);
        } else if ((isDisableEnableFieldCalled && etFlatNamePerm.isEnabled()) && !etFlatNamePerm.getText().toString().equals("") && !ValidationRule.validateAddress(etFlatNamePerm.getText().toString())) {
            input_layout_flatNamePerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            input_layout_flatNamePerm.setErrorEnabled(false);
        }
        if (etAreaPerm.getText().toString().equals("") && input_layout_areaPerm.isErrorEnabled()) {
            input_layout_areaPerm.setErrorEnabled(false);
        } else if ((isDisableEnableFieldCalled && etAreaPerm.isEnabled()) && !etAreaPerm.getText().toString().equals("") && !ValidationRule.validateAddress(etAreaPerm.getText().toString())) {
            input_layout_areaPerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            input_layout_areaPerm.setErrorEnabled(false);
        }
        if (!etSebiAuthority.getText().toString().equals("") && input_layout_sebiAuthority.isErrorEnabled()) {
            input_layout_sebiAuthority.setErrorEnabled(false);
        }
        if (!etFmcSebiAuthority.getText().toString().equals("") && input_layout_fmcSebiAuthority.isErrorEnabled()) {
            input_layout_fmcSebiAuthority.setErrorEnabled(false);
        }
        if (!etNWDate.getText().toString().equals("") && input_layout_NWDate.isErrorEnabled()) {
            input_layout_NWDate.setErrorEnabled(false);
        }


    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /*
    * Method check if checkbox is checked then corres address is set in perment feild
    * */
    private void setPermentAddress(EditText corres, EditText perment) {
        if (cbSameAsCorrespondence.isChecked()) {
            perment.removeTextChangedListener(this);
            perment.setText(corres.getText().toString());
            perment.addTextChangedListener(this);
            if (!isBlank(perment.getText().toString())) {
                perment.setEnabled(false);
            } else {
                perment.setEnabled(true);
            }
        }
    }

    private void familyDeclarationNoteDialog() {
        final Dialog familyNoteDialog = new Dialog(mContext);
        familyNoteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        familyNoteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        familyNoteDialog.setContentView(R.layout.dialog_family_declaration_note);
        familyNoteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        familyNoteDialog.setCanceledOnTouchOutside(false);

        Button btnOk = (Button) familyNoteDialog.findViewById(R.id.btnOk);
        ImageView ivClose = (ImageView) familyNoteDialog.findViewById(R.id.ivClose);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyNoteDialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyNoteDialog.dismiss();
            }
        });
        familyNoteDialog.show();
    }

    //yes or no pop
    private void familyDeclarationPopup(final String status, String message, String popMassage) {

        customdailogFamilyRelation = new CustomDialogFamilyRelationship(mContext, popMassage, true, true, false, message, YES, NO, new CustomDialog.CustomDialogListener() {
            @Override
            public void dialogAction(MOSLCommon.Action action, Object... data) {
                visibilityFlagFD = false;
                if (action == MOSLCommon.Action.OK1) {
                    getFamilyDetails(customdailogFamilyRelation, true, status);
                } else if (action == MOSLCommon.Action.NO) {
                    if (!status.equalsIgnoreCase("") && status.equalsIgnoreCase("2")) {
                        etEmail.setText("");
                        etEmail.requestFocus();
                        etEmail.setEnabled(true);
                    } else if (!status.equalsIgnoreCase("") && status.equalsIgnoreCase("3")) {
                        etMobile.setText("");
                        etMobile.requestFocus();
                        etMobile.setEnabled(true);
                    } else if (!status.equalsIgnoreCase("") && status.equalsIgnoreCase("4")) {
                        etEmail.setText("");
                        etMobile.setText("");
                        etEmail.requestFocus();
                        etEmail.setEnabled(true);
                        etMobile.setEnabled(true);
                    }
                }
            }
        });
        customdailogFamilyRelation.show();
    }

    public void getFamilyDetails(final CustomDialogFamilyRelationship resendCd, final boolean disableFlag, final String status) {

        if (MOSLCommon.isNetworkEnabled(getActivity())) {
            Map inputMap = new HashMap<>();
            inputMap.put("_userid", getUserID());
            inputMap.put("_corresmobileno", etMobile.getText().toString().trim());
            inputMap.put("_corresemailid", etEmail.getText().toString().trim());

            String flag = GETFAMILYDETAILS;
            String category = PERSONAL;

            customProgress.setVisibility(View.VISIBLE);
            if (resendCd != null) {
                resendCd.showProgressBar();
            }
            WebServiceHandler.post(getActivity(), flag, category, inputMap, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    //after success code goes here

                    customProgress.setVisibility(View.GONE);
                    if (resendCd != null) {
                        resendCd.hideProgressbar();
                    }
                    try {
                        if (statusCode == 200) {
                            if (response.length() != 0) {

                                List<FamilyDeclarationModel> dataList = new ArrayList<FamilyDeclarationModel>();
                                JSONArray jsonArray = response.getJSONArray("FAMILYDETAILS_BACKOFFICE");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    FamilyDeclarationModel dataModel = new FamilyDeclarationModel();
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    dataModel.setClientName((object.isNull("ClientName") || object.getString("ClientName").equals("")) ? "" : object.getString("ClientName").trim());
                                    dataModel.setClientCode((object.isNull("ClientCode") || object.getString("ClientCode").equals("")) ? "" : object.getString("ClientCode").trim());
                                    dataModel.setDpid((object.isNull("DPID") || object.getString("DPID").equals("")) ? "" : object.getString("DPID").trim());
                                    dataModel.setRelationship((object.isNull("Relationship") || object.getString("Relationship").equals("")) ? "" : object.getString("Relationship").trim());
                                    dataModel.setMobilePhone((object.isNull("MobilePhone") || object.getString("MobilePhone").equals("")) ? "" : object.getString("MobilePhone").trim());
                                    dataModel.setEmail((object.isNull("Email") || object.getString("Email").equals("")) ? "" : object.getString("Email").trim());
                                    dataModel.setPanNo((object.isNull("PANNo") || object.getString("PANNo").equals("")) ? "" : object.getString("PANNo").trim());
                                    dataModel.setEnable(disableFlag);
                                    dataModel.setPosition(i);
                                    dataList.add(dataModel);

                                }
                                JSONArray jsonArray1 = response.getJSONArray("FAMILYDETAILS_BACKOFFICE1");
                                ArrayList<String> relationList = new ArrayList<String>();
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject object = jsonArray1.getJSONObject(i);
                                    relationList.add((object.isNull("RELATION") || object.getString("RELATION").equals("")) ? "" : object.getString("RELATION").trim());
                                }
                                ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), R.layout.simple_spinner_item, relationList);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                getFamilyRelationDialog(dataList, spinnerAdapter, resendCd, status);
                            } else {
                                customProgress.setVisibility(View.GONE);
                                if (resendCd != null) {
                                    resendCd.hideProgressbar();
                                }
                                commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                    }
                    customProgress.setVisibility(View.GONE);
                    if (resendCd != null) {
                        resendCd.hideProgressbar();
                    }
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                    if (resendCd != null) {
                        resendCd.hideProgressbar();
                    }
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                    if (resendCd != null) {
                        resendCd.hideProgressbar();
                    }
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    if (resendCd != null) {
                        resendCd.hideProgressbar();
                    }
                    commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");

                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    if (resendCd != null) {
                        resendCd.hideProgressbar();
                    }
                    commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                    customProgress.setVisibility(View.GONE);
                    if (resendCd != null) {
                        resendCd.hideProgressbar();
                    }
                }
            });
        } else {
            MOSLCommon.alertDInternet(getActivity());
        }
    }

    //update family details
    public void updateFamilyDetails(Map inputMap, final List<FamilyDeclarationModel> dataList, final LinearLayout dailog_custom_progress, final Dialog dialog, final CustomDialogFamilyRelationship resendCd) {
        if (MOSLCommon.isNetworkEnabled(getActivity())) {

            String flag = INSERTFAMILYDETAILS;
            String category = PERSONAL;

            WebServiceHandler.post(getActivity(), flag, category, inputMap, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    //after success code goes here

                    try {
                        if (statusCode == 200) {

                            if (response.length() != 0) {

                                String onResponseSuccess = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                                String msg = (response.isNull("_Message") || response.getString("_Message").equals("")) ? "" : response.getString("_Message");
                                int intSuccess = Integer.parseInt(onResponseSuccess);
                                if (intSuccess == 1) {
                                    updateFamilyDetailsCount++;
                                    updateFamilyDetailsRecersiveCall(dataList, updateFamilyDetailsCount, dailog_custom_progress, dialog, resendCd);

                                } else if (intSuccess == 0) {
                                    dailog_custom_progress.setVisibility(View.GONE);
                                    commonMsgDialog(getActivity(), msg, false, "");
                                }
                            } else {
                                dailog_custom_progress.setVisibility(View.GONE);
                                commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        dailog_custom_progress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    dailog_custom_progress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    dailog_custom_progress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    dailog_custom_progress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");

                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    dailog_custom_progress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                    dailog_custom_progress.setVisibility(View.GONE);
                }
            });
        } else {
            MOSLCommon.alertDInternet(getActivity());
        }
    }

    private void updateFamilyDetailsRecersiveCall(List<FamilyDeclarationModel> dataList, int updateFamilyDetailsCount, LinearLayout dailog_custom_progress, Dialog dialog, CustomDialogFamilyRelationship resendCd) {
        FamilyDeclarationModel declarationModel = new FamilyDeclarationModel();
        if (updateFamilyDetailsCount < dataList.size()) {
            dailog_custom_progress.setVisibility(View.VISIBLE);
            Map inputMap = new HashMap<>();

            inputMap.put("_Operation", "I");
            inputMap.put("_userId", getUserID());
            inputMap.put("_ClientName", dataList.get(updateFamilyDetailsCount).getClientName());
            inputMap.put("_ClientCode", dataList.get(updateFamilyDetailsCount).getClientCode());
            inputMap.put("_dpID", dataList.get(updateFamilyDetailsCount).getDpid());
            inputMap.put("_Relationship", dataList.get(updateFamilyDetailsCount).getRelationship());
            inputMap.put("_corresmobileno", dataList.get(updateFamilyDetailsCount).getMobilePhone());
            inputMap.put("_corresemailid", dataList.get(updateFamilyDetailsCount).getEmail());
            inputMap.put("_panno", dataList.get(updateFamilyDetailsCount).getPanNo());

            updateFamilyDetails(inputMap, dataList, dailog_custom_progress, dialog, resendCd);
        } else {
            dailog_custom_progress.setVisibility(View.GONE);
            showFamilyDetailLayout(true);
            familyDeclarationChangedFlag = true;
            declarationModel.setEnable(false);
            dialog.dismiss();
            if (resendCd != null) {
                resendCd.dismiss();
            }
        }
    }

    //Delete family Declaration
    public void deleteFamilyDetails() {
        if (MOSLCommon.isNetworkEnabled(getActivity())) {
            Map inputMap = new HashMap<>();
            inputMap.put("_Operation", "D");
            inputMap.put("_userId", getUserID());

            String flag = INSERTFAMILYDETAILS;
            String category = PERSONAL;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(getActivity(), flag, category, inputMap, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    //after success code goes here
                    customProgress.setVisibility(View.GONE);
                    try {
                        if (statusCode == 200) {
                            if (response.length() != 0) {
                                String onResponseSuccess = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                                String msg = (response.isNull("_Message") || response.getString("_Message").equals("")) ? "" : response.getString("_Message");
                                int intSuccess = Integer.parseInt(onResponseSuccess);
                                if (intSuccess == 1) {
                                    showFamilyDetailLayout(false);
                                } else if (intSuccess == 0) {
                                    showFamilyDetailLayout(true);
                                    customProgress.setVisibility(View.GONE);
                                    commonMsgDialog(getActivity(), msg, false, "");
                                }
                            } else {
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        customProgress.setVisibility(View.GONE);
                    }
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
                    commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                    customProgress.setVisibility(View.GONE);
                }
            });
        } else {
            MOSLCommon.alertDInternet(getActivity());
        }
    }

    private void showFamilyDetailLayout(boolean value) {
        if (value) {
            rlFamilyDeclaration.setVisibility(View.VISIBLE);
            cbFamilyDeclaration.setChecked(true);
            if (!isIpvORDocumentStageDone()) {
                cbFamilyDeclaration.setEnabled(true);
            } else if (isIpvORDocumentStageDone() && ((getIsEquitySelected() && !isEquityFundTransfered()) ||
                    (getIsCommoditySelected() && !isCommodityFundTransfered()))) {
                cbFamilyDeclaration.setEnabled(true);
            } else {
                cbFamilyDeclaration.setEnabled(false);
            }
        } else {
            rlFamilyDeclaration.setVisibility(View.GONE);
            cbFamilyDeclaration.setChecked(false);
        }
    }
}

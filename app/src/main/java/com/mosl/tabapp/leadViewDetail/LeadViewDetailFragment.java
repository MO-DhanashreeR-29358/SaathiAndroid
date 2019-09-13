package com.mosl.tabapp.leadViewDetail;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.mosl.commons.data.UiModel;
import com.mosl.mobile.user.login.UserManager;
import com.mosl.saathi.api.model.assignto.UserBusinessProfileRequest;
import com.mosl.saathi.api.model.clientdetail.AdvisorsItem;
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem;
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest;
import com.mosl.saathi.api.model.clientdetail.ProductsItem;
import com.mosl.saathi.api.model.clientprofile.ClientProfileRequest;
import com.mosl.saathi.api.model.clientprofile.ClientProfileResponse;
import com.mosl.saathi.api.model.crosssellingproduct.CrossSellingProductRequest;
import com.mosl.saathi.api.model.crosssellingproduct.DeleteLeadCrossSellingProductRequest;
import com.mosl.saathi.api.model.crosssellingproduct.SaveLeadCrossSellingProductRequest;
import com.mosl.saathi.api.model.lead.AssetGainLossItem;
import com.mosl.saathi.api.model.lead.AssetProductItem;
import com.mosl.saathi.api.model.lead.AvailableLimitsItem;
import com.mosl.saathi.api.model.lead.BrokingGainLossItem;
import com.mosl.saathi.api.model.lead.BrokingProductItem;
import com.mosl.saathi.api.model.lead.BusinessProfile;
import com.mosl.saathi.api.model.lead.CashPortfolioItem;
import com.mosl.saathi.api.model.lead.CityItem;
import com.mosl.saathi.api.model.lead.CityListRequest;
import com.mosl.saathi.api.model.lead.ClientGainLossSummaryResponse;
import com.mosl.saathi.api.model.lead.ClientSummaryResponse;
import com.mosl.saathi.api.model.lead.ConfigurationByStatusRequest;
import com.mosl.saathi.api.model.lead.Contact;
import com.mosl.saathi.api.model.lead.ContactAddressItem;
import com.mosl.saathi.api.model.lead.ContactAddressRequest;
import com.mosl.saathi.api.model.lead.ContactRequest;
import com.mosl.saathi.api.model.lead.CountryItem;
import com.mosl.saathi.api.model.lead.CrossSellingProduct;
import com.mosl.saathi.api.model.lead.InsertLeadProductRequest;
import com.mosl.saathi.api.model.lead.LeadFieldSection;
import com.mosl.saathi.api.model.lead.LeadFieldValueData;
import com.mosl.saathi.api.model.lead.LeadFieldValueRequest;
import com.mosl.saathi.api.model.lead.LeadProductMappingItem;
import com.mosl.saathi.api.model.lead.LeadViewItem;
import com.mosl.saathi.api.model.lead.LeadViewRequest;
import com.mosl.saathi.api.model.lead.LookupItem;
import com.mosl.saathi.api.model.lead.LookupRequest;
import com.mosl.saathi.api.model.lead.PortfolioSnapshotItem;
import com.mosl.saathi.api.model.lead.StageItem;
import com.mosl.saathi.api.model.lead.StageMobileItem;
import com.mosl.saathi.api.model.lead.StageMobileRequest;
import com.mosl.saathi.api.model.lead.StageRequest;
import com.mosl.saathi.api.model.lead.StageWiseConfiguration;
import com.mosl.saathi.api.model.lead.StateProvinceItem;
import com.mosl.saathi.api.model.lead.StateProvinceRequest;
import com.mosl.saathi.api.model.lead.StatusItem;
import com.mosl.saathi.api.model.lead.StatusRequest;
import com.mosl.saathi.api.model.lead.TotalGainLossItem;
import com.mosl.saathi.api.model.lead.UpdateLeadRequest;
import com.mosl.saathi.api.model.lead.UpdateLeadResponse;
import com.mosl.saathi.api.model.lead.request.UpdateLeadMappingRequest;
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistItem;
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMValidationRule;
import com.mosl.tabapp.common.CustomDatePickerDialog;
import com.mosl.tabapp.common.CustomListPickerDialog;
import com.mosl.tabapp.common.DatePickerListener;
import com.mosl.tabapp.common.MSFStatistics;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.common.SearchableSpinner;
import com.mosl.tabapp.common.StringStuff;
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
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.RefreeSyncWebServiceCall;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.droidparts.widget.ClearableEditText;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
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
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class LeadViewDetailFragment extends MOSLCommonFragment
    implements View.OnClickListener, MOSLConstants {

    final public static String TAG = LeadViewDetailFragment.class.getSimpleName();
    public static final String SectionTagPrefix = "Section";
    public static final String TextBox = "TextBox";
    public static final String DropDown = "DropDown";
    private static String ucid, mLeadId;
    private final ArrayList<RowData> arrProfession = new ArrayList<>();
    private final ArrayList<RowData> arrSelectProfile = new ArrayList<>();
    private final ArrayList<RowData> arrInterested = new ArrayList<>();
    private final ArrayList<RowData> arrCrossSellingProductId = new ArrayList<>();
    private final ArrayList<RowData> arrState = new ArrayList<>();
    private final ArrayList<RowData> arrCity = new ArrayList<>();
    private final ArrayList<RowData> arrOffState = new ArrayList<>();
    private final ArrayList<RowData> arrOffCity = new ArrayList<>();
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
    private EditText etLeadID, etLeadCategory, etLastModifiedBy, etEkycMobileNumber,
        etPanNumber, etAadharNumber, etLeadName, etCreationDate, etCreatedBy, etSource,
        etProspectSource,
        etProspectSourceDetail, etCreatedThrough, etMobileNumber, etLastModifiedDate,
        etLandlineNumber, etEmail, etRefereeCode, etRefereeName, etRefereeEmail, etRefereeMobile,
        etAlternateMobileNumber, etAlternateEmail, etContactPerson, etComments,
        etProspectRefereeName, etProspectRefereerMobile, etProspectRefereerEmail;
    private Spinner spProfile, spCountry, spOffCountry, spProfession, spPmsProduct;
    private ClearableEditText etDob, etAnniversary;
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
    private ArrayList<RowData> arrProductType;
    private ArrayList<RowData> arrStage;
    private ArrayList<RowData> arrStatus;
    private ArrayList<RowData> arrCrossSellingStatus;
    private ArrayList<RowData> arrCountry;
    private ArrayAdapter<RowData> arrAdapProfession, arrAdapProfile, arrAdapInterested,
        arrAdapProductType, arrAdapStage, arrAdapStatus,
        arrAdapCrossSellingStatus, arrAdapCrossSellingProductId, arrAdapCountry, arrAdapState,
        arrAdapCity, arrAdapOffState, arrAdapOffCity;
    private String interestedIn, sourceID, leadProspetSourceID,
        primaryRm, stageId, statusId, creationDate, profile, contactPerson, profession;
    private ArrayList<ContactDetailModel> contactDetailList = new ArrayList<>();
    private ArrayList<AddressDetailModel> addressDetailList = new ArrayList<>();
    private ListView lvAddProduct;
    private ArrayList<LeadDetailAddProductModel> leadDetailAddProductList = new ArrayList<>();
    private LeadDetailAddProductAdapter leadDetailAddProductAdapter;
    private String specialistAdd = "";
    private String specialistEdit = "";
    private String primaryRMAdd = "";
    private String primaryRMEdit = "", advisorCode = "";
    private TextView tvLandLineError, tvAlternateMobileNumberError, tvEmailIdError,
        tvAlternateEmailIdError, tvPincodeError, tvOffPincodeError, tvNameError,
        tvContactPersonError, tvPanNumError, tvAadharNumError,
        tvProspectRefereeNameError, tvProspectRefereerMobileError, tvProspectRefereerEmailError;
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
    private ArrayList<String> arrPmsProduct;
	@Inject UserManager userManager;
    private boolean leadEditable = true, isPotentialPMSProduct = false, isPMSProductStageStatus;
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
                        arrOffCity.clear();
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
                    arrCity.clear();
                    arrCity.add(new RowData(0, "-1", "Select"));
                    arrAdapCity =
                        new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
                            arrCity);
                    arrAdapCity.setDropDownViewResource(R.layout.mdropdown);
                    spCity.setAdapter(arrAdapCity);
                    if (!stateCodeStr.equalsIgnoreCase("-1")) {
                        getCity(stateCodeStr, RESIDENCE, cityStr);
                    } else {
                        arrCity.clear();
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

    public boolean isLeadEditable() {
        return leadEditable;
    }

    public void setLeadEditable(boolean leadEditable) {
        this.leadEditable = leadEditable;
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

    private void setEnableDisable(boolean status) {
        etLeadName.setEnabled(status);
        etPanNumber.setEnabled(status);
        etAadharNumber.setEnabled(status);
        etContactPerson.setEnabled(status);
        etLandlineNumber.setEnabled(status);
        etRefereeCode.setEnabled(status);
        etRefereeName.setEnabled(status);
        etProspectRefereeName.setEnabled(status);
        etProspectRefereerMobile.setEnabled(status);
        etProspectRefereerEmail.setEnabled(status);
        etLandlineNumber.setEnabled(status);
        etDob.setEnabled(status);
        etAnniversary.setEnabled(status);
        etAlternateMobileNumber.setEnabled(status);
        etAlternateEmail.setEnabled(status);
        etEmail.setEnabled(status);
        etContactPerson.setEnabled(status);
        etComments.setEnabled(status);
        btnSubmit.setEnabled(status);
        spProfile.setEnabled(status);
        spProfession.setEnabled(status);
        cbSameAs.setEnabled(status);
        cbPrimaryAddress.setEnabled(status);
        cbOffPrimaryAddress.setEnabled(status);
        spPmsProduct.setEnabled(status);
        if (status) {
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
                                ((EditText) cObj).setEnabled(status);
                            } else if (cObj instanceof Spinner) {
                                ((Spinner) cObj).setEnabled(status);
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
        etCreationDate = leadViewDetailView.findViewById(R.id.etCreationDate);
        etCreatedBy = leadViewDetailView.findViewById(R.id.etCreatedBy);
        etSource = leadViewDetailView.findViewById(R.id.etSource);
        etProspectSource = leadViewDetailView.findViewById(R.id.etProspectSource);
        etProspectSourceDetail =
            leadViewDetailView.findViewById(R.id.etProspectSourceDetail);
        etCreatedThrough = leadViewDetailView.findViewById(R.id.etCreatedThrough);
        spProfile = leadViewDetailView.findViewById(R.id.spProfile);
        spPmsProduct = leadViewDetailView.findViewById(R.id.spPmsProduct);
        String[] arrayPmsProduct =
            getActivity().getResources().getStringArray(R.array.selectPmsProduct);
        arrPmsProduct = new ArrayList<>(Arrays.asList(arrayPmsProduct));
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
        etProspectRefereeName = leadViewDetailView.findViewById(R.id.etProspectRefereeName);
        etProspectRefereerEmail = leadViewDetailView.findViewById(R.id.etProspectRefereerEmail);
        etRefereeMobile = leadViewDetailView.findViewById(R.id.etRefereeNumber);
        etDob = leadViewDetailView.findViewById(R.id.etDob);
        etAnniversary = leadViewDetailView.findViewById(R.id.etAnniversary);
        etLastModifiedDate = leadViewDetailView.findViewById(R.id.etLastModifiedDate);
        etAlternateMobileNumber =
            leadViewDetailView.findViewById(R.id.etAlternateMobileNumber);
        etAlternateEmail = leadViewDetailView.findViewById(R.id.etAlternateEmail);
        etContactPerson = leadViewDetailView.findViewById(R.id.etContactPerson);
        etProspectRefereeName = leadViewDetailView.findViewById(R.id.etProspectRefereeName);
        etProspectRefereerMobile = leadViewDetailView.findViewById(R.id.etProspectRefereerMobile);
        etProspectRefereerEmail = leadViewDetailView.findViewById(R.id.etProspectRefereerEmail);
        etContactPerson.setFilters(new InputFilter[] { CRMAppUtil.filterOnlyAlphabetAndNumbers });
        etRefereeName.setFilters(new InputFilter[] { CRMAppUtil.filterOnlyAlphabetAndNumbers });
        etProspectRefereeName.setFilters(
            new InputFilter[] { CRMAppUtil.filterOnlyAlphabetAndNumbers });
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
        tvProspectRefereeNameError =
            leadViewDetailView.findViewById(R.id.tvProspectRefereeNameError);
        tvProspectRefereerMobileError =
            leadViewDetailView.findViewById(R.id.tvProspectRefereerMobileError);
        tvProspectRefereerEmailError =
            leadViewDetailView.findViewById(R.id.tvProspectRefereerEmailError);
        tvAlternateMobileNumberError =
            leadViewDetailView.findViewById(R.id.tvAlternateMobileNumberError);
        tvAlternateEmailIdError =
            leadViewDetailView.findViewById(R.id.tvAlternateEmailIdError);
        tvEmailIdError = leadViewDetailView.findViewById(R.id.tvEmailIdError);
        tvPincodeError = leadViewDetailView.findViewById(R.id.tvPincodeError);
        tvOffPincodeError = leadViewDetailView.findViewById(R.id.tvOffPincodeError);
        tvProspectRefereeNameError =
            leadViewDetailView.findViewById(R.id.tvProspectRefereeNameError);
        tvProspectRefereerMobileError =
            leadViewDetailView.findViewById(R.id.tvProspectRefereerMobileError);
        tvProspectRefereerEmailError =
            leadViewDetailView.findViewById(R.id.tvProspectRefereerEmailError);
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
                    if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
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
        etProspectRefereeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override public void afterTextChanged(Editable s) {
                if (!isBlank(etProspectRefereeName.getText().toString().trim())) {
                    tvProspectRefereeNameError.setVisibility(View.GONE);

                    //format check
                    if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                        etProspectRefereeName.getText().toString().trim())) {
                        tvProspectRefereeNameError.setText(ErrorMsgName);
                        tvProspectRefereeNameError.setVisibility(View.VISIBLE);
                    } else {
                        tvProspectRefereeNameError.setVisibility(View.GONE);
                    }
                } else {
                    tvProspectRefereeNameError.setVisibility(View.GONE);
                }
            }
        });

        etProspectRefereerMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etProspectRefereerMobile.getText().toString())) {
                    if (!CRMValidationRule.validateMobile(
                        etProspectRefereerMobile.getText().toString())) {
                        tvProspectRefereerMobileError.setVisibility(View.VISIBLE);
                        tvProspectRefereerMobileError.setText(INVALID_MOBILE_NO);
                        etProspectRefereerMobile.requestFocus();
                    } else {
                        tvProspectRefereerMobileError.setVisibility(View.GONE);
                    }
                } else {
                    tvProspectRefereerMobileError.setVisibility(View.GONE);
                }
            }

            @Override public void afterTextChanged(Editable s) {

            }
        });

        etProspectRefereerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etProspectRefereerEmail.getText().toString().trim())) {
                    if (!isValidEmail(etProspectRefereerEmail.getText())) {
                        tvProspectRefereerEmailError.setVisibility(View.VISIBLE);
                        tvProspectRefereerEmailError.setText(INVALID_EMAIL_ID);
                        etProspectRefereerEmail.requestFocus();
                    } else {
                        tvProspectRefereerEmailError.setVisibility(View.GONE);
                    }
                } else {
                    tvProspectRefereerEmailError.setVisibility(View.GONE);
                }
            }

            @Override public void afterTextChanged(Editable s) {

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
                    if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
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
                            tvAlternateMobileNumberError.setText(INVALID_MOBILE_NO);
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
                            tvAlternateEmailIdError.setText(INVALID_EMAIL_ID);
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
                            tvEmailIdError.setText(INVALID_EMAIL_ID);
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
            .filter(event -> event.getView().isFocused())
            .map(query -> query.getEditable().toString().trim())
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
					"Common/GetRefereeCode", inputMap, pbLoadingIndicatorRefreeName, userManager)
                    .compose(applyUiModel());
            }).subscribe(uiModel -> {
                JSONObject data1 = uiModel.data;
                if (data1 != null) {
                    RefreeData(data1, "ByName", pbLoadingIndicatorRefreeName);
                }
            }));

        Observable<String> refreeCodeObs = RxTextView.afterTextChangeEvents(etRefereeCode)
            .filter(event -> event.getView().isFocused())
            .map(query -> query.getEditable().toString().trim())
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
					"Common/GetRefereeCode", inputMap, pbLoadingIndicatorRefreeCode, userManager)
                    .compose(applyUiModel());
            }).subscribe(uiModel -> {
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
            compositeDisposable.add(saathiApi.getCountries()
                .compose(applyUiModel())
                .subscribe(this::renderCountries));
        } else {
            noInternetConnectionDialog();
        }
    }

    protected void renderCountries(UiModel<List<CountryItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        List<CountryItem> data = uiModel.data;
        if (data != null) {
            stateFlag = false;
            officeStateFlag = false;
            arrCountry = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                CountryItem item = data.get(i);
                arrCountry.add(new RowData(
                    i,
                    stringOrEmpty(item.countryId),
                    stringOrEmpty(item.countryName))
                );
            }
            arrAdapCountry = new ArrayAdapter<>(getMainActivity(),
                R.layout.simple_spinner_item, arrCountry);
            arrAdapCountry.setDropDownViewResource(R.layout.mdropdown);
            spCountry.setAdapter(arrAdapCountry);
            spCountry.setOnItemSelectedListener(countryListener);
            spOffCountry.setAdapter(arrAdapCountry);
            spOffCountry.setOnItemSelectedListener(offCountryListener);
        }
    }

    private void getState(String id, final String type, final String value) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            compositeDisposable.add(saathiApi.getStateProvinces(new StateProvinceRequest(id))
                .compose(applyUiModel())
                .subscribe(uiModel -> renderStates(uiModel, type, value)));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderStates(UiModel<List<StateProvinceItem>> uiModel, final String type,
        final String value) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        List<StateProvinceItem> data = uiModel.data;
        if (data != null) {
            arrState.clear();
            arrOffState.clear();
            arrState.add(new RowData(0, "-1", "Select"));
            arrOffState.add(new RowData(0, "-1", "Select"));

            for (int i = 0; i < data.size(); i++) {
                StateProvinceItem item = data.get(i);
                String stateId = String.valueOf(item.stateId);
                String stateName = stringOrEmpty(item.stateName);
                if (cbSameAs.isChecked()) {
                    arrState.add(new RowData(i + 1, stateId, stateName));
                    arrOffState.add(new RowData(i + 1, stateId, stateName));
                } else {
                    if (type.equals(RESIDENCE)) {
                        arrState.add(new RowData(i + 1, stateId, stateName));
                    } else if (type.equals(OFFICE)) {
                        arrOffState.add(new RowData(i + 1, stateId, stateName));
                    }
                }
            }

            if (cbSameAs.isChecked()) {
                arrAdapState =
                    new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrState);
                arrAdapState.setDropDownViewResource(R.layout.mdropdown);
                spState.setAdapter(arrAdapState);
                RowData dataPrimary = RowData.getSpinnerRowDataById(value, arrAdapState);
                if (dataPrimary != null) {
                    spState.setSelection(dataPrimary.getPosition());
                }
                spState.setOnItemSelectedListener(stateListener);

                arrAdapOffState =
                    new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
                        arrOffState);
                arrAdapOffState.setDropDownViewResource(R.layout.mdropdown);
                spOffState.setAdapter(arrAdapOffState);
                RowData dataOffice = RowData.getSpinnerRowDataById(value, arrAdapOffState);
                if (dataOffice != null) {
                    spOffState.setSelection(dataOffice.getPosition());
                }
                spOffState.setOnItemSelectedListener(offStateListener);
            } else {
                if (type.equals(RESIDENCE)) {
                    arrAdapState =
                        new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
                            arrState);
                    arrAdapState.setDropDownViewResource(R.layout.mdropdown);
                    spState.setAdapter(arrAdapState);
                    RowData rowData = RowData.getSpinnerRowDataById(value, arrAdapState);
                    if (rowData != null) {
                        spState.setSelection(rowData.getPosition());
                    }
                    spState.setOnItemSelectedListener(stateListener);
                } else if (type.equals(OFFICE)) {
                    arrAdapOffState =
                        new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
                            arrOffState);
                    arrAdapOffState.setDropDownViewResource(R.layout.mdropdown);
                    spOffState.setAdapter(arrAdapOffState);
                    RowData rowData = RowData.getSpinnerRowDataById(value, arrAdapOffState);
                    if (rowData != null) {
                        spOffState.setSelection(rowData.getPosition());
                    }
                    spOffState.setOnItemSelectedListener(offStateListener);
                }
            }
        }
    }

    private void getCity(String id, final String type, final String value) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            compositeDisposable.add(saathiApi.getCityList(new CityListRequest(id))
                .compose(applyUiModel())
                .subscribe(uiModel -> renderCities(uiModel, type, value)));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderCities(UiModel<List<CityItem>> uiModel, final String type,
        final String value) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrCity.clear();
            arrOffCity.clear();
            showOnFailurePopup(error);
        }

        List<CityItem> data = uiModel.data;
        if (data != null) {
            arrCity.clear();
            arrOffCity.clear();
            arrCity.add(new RowData(0, "-1", "Select"));
            arrOffCity.add(new RowData(0, "-1", "Select"));

            for (int i = 0; i < data.size(); i++) {
                CityItem item = data.get(i);
                String cityId = String.valueOf(item.cityId);
                String cityName = stringOrEmpty(item.cityName);

                if (cbSameAs.isChecked()) {
                    arrCity.add(new RowData(i + 1, cityId, cityName));
                    arrOffCity.add(new RowData(i + 1, cityId, cityName));
                } else {
                    if (type.equals(RESIDENCE)) {
                        arrCity.add(new RowData(i + 1, cityId, cityName));
                    }
                    if (type.equals(OFFICE)) {
                        arrOffCity.add(new RowData(i + 1, cityId, cityName));
                    }
                }
            }

            if (cbSameAs.isChecked()) {
                arrAdapCity =
                    new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrCity);
                arrAdapCity.setDropDownViewResource(R.layout.mdropdown);
                spCity.setAdapter(arrAdapCity);
                if (!isBlank(value)) {
                    RowData rowData = RowData.getSpinnerRowDataById(value, arrAdapCity);
                    if (rowData != null) {
                        spCity.setSelection(rowData.getPosition());
                    }
                    spCity.setOnItemSelectedListener(cityListener);
                }

                arrAdapOffCity =
                    new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrOffCity);
                arrAdapOffCity.setDropDownViewResource(R.layout.mdropdown);
                spOffCity.setAdapter(arrAdapOffCity);
                int pos = ((RowData) spCity.getSelectedItem()).getPosition();
                spOffCity.setSelection(pos);
            } else {
                if (type.equals(RESIDENCE)) {
                    arrAdapCity =
                        new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
                            arrCity);
                    arrAdapCity.setDropDownViewResource(R.layout.mdropdown);
                    spCity.setAdapter(arrAdapCity);
                    if (!isBlank(value)) {
                        RowData rowData = RowData.getSpinnerRowDataById(value, arrAdapCity);
                        if (rowData != null) {
                            spCity.setSelection(rowData.getPosition());
                        }
                        spCity.setOnItemSelectedListener(cityListener);
                    }
                }
                if (type.equals(OFFICE)) {
                    arrAdapOffCity =
                        new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
                            arrOffCity);
                    arrAdapOffCity.setDropDownViewResource(R.layout.mdropdown);
                    spOffCity.setAdapter(arrAdapOffCity);
                    if (!isBlank(value)) {
                        RowData rowData = RowData.getSpinnerRowDataById(value, arrAdapOffCity);
                        if (rowData != null) {
                            spOffCity.setSelection(rowData.getPosition());
                        }
                    }
                }
            }
        }
    }

    private void getProfession() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), PROFESSIONID, null, null);
            compositeDisposable.add(saathiApi.getAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderProfession));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderProfession(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrProfession.clear();
            showOnFailurePopup(error);
        }

        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrProfession.clear();
            arrProfession.add(new RowData(0, "-1", "Select"));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                arrProfession.add(
                    new RowData(i + 1,
                        String.valueOf(item.lookUpid),
                        stringOrEmpty(item.lookUpName))
                );
            }
            arrAdapProfession =
                new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrProfession);
            arrAdapProfession.setDropDownViewResource(R.layout.mdropdown);
            spProfession.setAdapter(arrAdapProfession);
            spProfession.setOnItemSelectedListener(professionListener);
        }
    }

    private void getInterestIn() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), INTERESTEDIN, null, null);
            compositeDisposable.add(saathiApi.getAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderInterests));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderInterests(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrInterested.clear();
            showOnFailurePopup(error);
        }

        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrInterested.clear();
            arrInterested.add(new RowData(0, "-1", "Select"));

            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                arrInterested.add(
                    new RowData(i + 1,
                        String.valueOf(item.lookUpid),
                        stringOrEmpty(item.lookUpName)
                    ));
            }
            arrAdapInterested =
                new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrInterested);
            arrAdapInterested.setDropDownViewResource(R.layout.mdropdown);
        }
    }

    private void getProductType(String productId) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest lookupRequest =
                new LookupRequest(AccountDetails.getBusinessUnitId(), PRODUCTTYPE, productId, null);
            compositeDisposable.add(saathiApi.getAllLookup(lookupRequest)
                .compose(applyUiModel())
                .subscribe(this::renderProductTypes));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderProductTypes(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            arrProductType = new ArrayList<>();
            showOnFailurePopup(error);
        }

        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrProductType = new ArrayList<>();
            arrProductType.add(new RowData(0, "-1", "Select"));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                arrProductType.add(
                    new RowData(i + 1,
                        String.valueOf(item.lookUpid),
                        stringOrEmpty(item.lookUpName)
                    ));
            }
            arrAdapProductType =
                new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrProductType);
            arrAdapProductType.setDropDownViewResource(R.layout.mdropdown);
        }
    }

    private void getStatus(final Spinner spStatus, String productId) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            compositeDisposable.add(saathiApi.getStatusList(new StatusRequest(productId, "1"))
                .compose(applyUiModel())
                .subscribe(uiModel -> renderStatus(uiModel, spStatus)));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderStatus(UiModel<List<StatusItem>> uiModel, final Spinner spStatus) {
        toggleProgress(uiModel.inProgress);
        if (uiModel.inProgress) {
            if (customProgress != null) {
                customProgress.setVisibility(View.VISIBLE);
            }
        } else {
            if (customProgress != null) {
                customProgress.setVisibility(View.GONE);
            }
        }

        Throwable error = uiModel.error;
        if (error != null) {
            arrStatus = new ArrayList<>();
            showOnFailurePopup(error);
        }

        List<StatusItem> data = uiModel.data;
        if (data != null) {
            arrStatus = new ArrayList<>();
            arrStatus.add(new RowData(0, "-1", "Select"));

            for (int i = 0; i < data.size(); i++) {
                StatusItem item = data.get(i);
                arrStatus.add(new RowData(i + 1,
                    String.valueOf(item.statusID),
                    stringOrEmpty(item.statusName)
                ));
            }
            arrAdapStatus =
                new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrStatus);
            arrAdapStatus.setDropDownViewResource(R.layout.mdropdown);
            spStatus.setAdapter(arrAdapStatus);
        }
    }

    private void getLeadDetail() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            compositeDisposable.add(saathiApi.getLeadView(new LeadViewRequest(mLeadId))
                .compose(applyUiModel())
                .subscribe(this::renderLeads));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderLeads(UiModel<LeadViewItem> uiModel) {
        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        LeadViewItem data = uiModel.data;
        if (data != null) {
            leadDetailAddProductList.clear();

            leadOwner = stringOrEmpty(data.leadOwner);
            contactPerson = stringOrEmpty(data.contactPerson);
            sourceID = String.valueOf(data.sourceID);
            leadProspetSourceID = String.valueOf(data.leadSourceID);

            refereeCode = stringOrEmpty(data.refereeCode);
            refereeName = stringOrEmpty(data.refereeName);
            isRefreeCode = !(refereeCode.isEmpty() && refereeName.isEmpty());

            profile = stringOrEmpty(data.profile);
            profession = stringOrEmpty(data.professionID);
            creationDate = stringOrEmpty(data.creationDate);
            isPotentialPMSProduct = stringOrEmpty(data.isPotentialPMSProduct).isEmpty();

            String leadId = String.valueOf(data.leadID);
            String leadCategoryText = stringOrEmpty(data.leadCategoryText);
            String sourceName = stringOrEmpty(data.sourceName);
            String refereeEmail = stringOrEmpty(data.refereeEmail);
            String refereeMobile = stringOrEmpty(data.refereeMobile);
            String createdBy = stringOrEmpty(data.createdBy);
            String createdThrough = stringOrEmpty(data.createdThrough);
            String lastModificationDate = stringOrEmpty(data.lastModificationDate);
            String lastModifiedBy = stringOrEmpty(data.lastModifiedBy);
            String prospectSource = stringOrEmpty(data.prospectSource);
            String comments = stringOrEmpty(data.comments);
            String leadSourceDetails = stringOrEmpty(data.leadSourceDetails);
            String canEditLead = stringOrDefault(String.valueOf(data.canEditLead), "1");
            String isProductSpecialist = stringOrDefault(data.isProductSpecialist, "N");
            if (canEditLead.equalsIgnoreCase("0")
                || isProductSpecialist.equalsIgnoreCase("Y")) {
                setLeadEditable(false);
            } else {
                setLeadEditable(true);
            }
            String isPotentialPmsProduct = stringOrEmpty(data.isPotentialPMSProduct);

            spPmsProduct.setSelection(getSpinnerPosition(isPotentialPmsProduct, arrPmsProduct));
            etProspectRefereerEmail.setText(data.prospectRefereEmail);
            etProspectRefereerMobile.setText(data.prospectRefereMobile);
            etProspectRefereeName.setText(data.prospectRefereName);
            etRefereeCode.setText(refereeCode);
            etRefereeName.setText(refereeName);
            etRefereeEmail.setText(refereeEmail);
            etRefereeMobile.setText(refereeMobile);
            etCreatedBy.setText(createdBy);
            etCreatedThrough.setText(createdThrough);
            etLastModifiedBy.setText(lastModifiedBy);
            try {
                etLastModifiedDate.setText(
                    getDateFromString(lastModificationDate,
                        "yyyy-MM-dd'T'kk:mm:ss", DATE_FORMAT_TO_DISPLAY));
            } catch (Exception e) {
                etLastModifiedDate.setText(lastModificationDate.replace("T", " "));
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

            List<LeadProductMappingItem> mapping = data.leadProductMapping;
            if (mapping != null && mapping.size() > 0) {
                renderLeadProductMapping(mapping);
            }

            Contact contact = data.contact;
            if (contact != null) {
                renderContact(contact, leadCategoryText);
            }

            List<LeadFieldSection> sectionModels = data.leadFieldSectionModels;
            if (sectionModels != null && sectionModels.size() > 0) {
                mLeadFieldSectionModelList = new ArrayList<>();
                for (LeadFieldSection sectionModel : sectionModels) {
                    LeadFieldSectionModel tmpObj =
                        new LeadFieldSectionModel();
                    tmpObj.setFieldSectionId(sectionModel.fieldSectionId);
                    tmpObj.setSectionName(stringOrEmpty(sectionModel.sectionName));
                    tmpObj.setSectionCode(stringOrEmpty(sectionModel.sectionCode));
                    tmpObj.setSectionHeader(stringOrEmpty(sectionModel.sectionHeader));
                    mLeadFieldSectionModelList.add(tmpObj);
                }
            }

            List<LeadFieldValueData> valueDataList = data.leadFieldValueModels;
            if (valueDataList != null && valueDataList.size() > 0) {
                for (LeadFieldValueData valueData : valueDataList) {
                    LeadFieldSectionModel tempSection =
                        LeadFieldSectionModel.getSectionFromListWithId(
                            mLeadFieldSectionModelList, valueData.fieldSectionId);
                    if (tempSection != null) {
                        boolean isMandatory = stringOrEmpty(valueData.mandatory).length() > 0;
                        LeadFieldValueModel.setLeadid(mLeadId);
                        LeadFieldValueModel valueModel = new LeadFieldValueModel();
                        valueModel.setLeadField_Value(stringOrEmpty(valueData.leadFieldValue));
                        valueModel.setFieldName(stringOrEmpty(valueData.fieldName));
                        valueModel.setControlType(stringOrEmpty(valueData.controlType));
                        valueModel.setLF_SysName(stringOrEmpty(valueData.lFSysName));
                        valueModel.setMandatory(isMandatory);
                        valueModel.setLeadFieldId(valueData.leadFieldId);
                        valueModel.setDropdownValueArr(stringOrEmpty(valueData.dropdownValue),
                            "\\|");
                        tempSection.addSelectionList(valueModel);
                    } else {
                        Log.w("Section list", " Dangling Failed");
                    }
                }
                addNewSections();
            }

            etLeadID.setText(leadId);
            etRefereeCode.setText(refereeCode);
            etRefereeName.setText(refereeName);
            etRefereeEmail.setText(refereeEmail);
            etRefereeMobile.setText(refereeMobile);
            etSource.setText(sourceName);
        }
    }

    public void renderLeadProductMapping(
        @NonNull List<LeadProductMappingItem> mappingItems
    ) {
        for (LeadProductMappingItem item : mappingItems) {
            interestedIn = String.valueOf(item.interestedIn);
            primaryRm = stringOrEmpty(item.primaryRMName);
            stageId = String.valueOf(item.stageID);
            statusId = String.valueOf(item.statusID);
            leadDetailAddProductList.add(getLeadDetailAddProductModel(item));
            tvClientCode.setText(item.clientCode);
        }
        leadDetailAddProductAdapter =
            new LeadDetailAddProductAdapter(getMainActivity(), leadDetailAddProductList);
        lvAddProduct.setAdapter(leadDetailAddProductAdapter);
        setListViewHeightBasedOnChildrenDetail(lvAddProduct);
    }

    public LeadDetailAddProductModel getLeadDetailAddProductModel(
        @NonNull LeadProductMappingItem item
    ) {
        List<CrossSellingProductsModel> crossSellingProducts =
            getCrossSellingProductsModel(item.crossSellingProducts);
        return new LeadDetailAddProductModel(
            String.valueOf(item.stageID),
            stringOrEmpty(item.stageName),
            String.valueOf(item.statusID),
            stringOrEmpty(item.statusName),
            String.valueOf(item.interestedIn),
            stringOrEmpty(item.productName),
            stringOrEmpty(item.primaryRMName),
            stringOrEmpty(item.productSpecialistName),
            stringOrEmpty(item.assignedBy),
            stringOrEmpty(item.assignedByDate),
            stringOrEmpty(item.clientCode),
            String.valueOf(item.leadProductMappingID),
            item.isDefault,
            crossSellingProducts,
            item.isCrossSellingProduct,
            stringOrEmpty(item.isProductSpecialistEditDisabled),
            stringOrEmpty(item.isProductDisabled),
            stringOrEmpty(item.productSpecialist)
        );
    }

    public List<CrossSellingProductsModel> getCrossSellingProductsModel(
        List<CrossSellingProduct> products) {
        List<CrossSellingProductsModel> crossSellingProducts = new ArrayList<>();
        if (products != null && products.size() > 0) {
            for (CrossSellingProduct sellingProduct : products) {
                crossSellingProducts.add(
                    new CrossSellingProductsModel(
                        sellingProduct.leadProductId,
                        sellingProduct.productId,
                        sellingProduct.statusId,
                        sellingProduct.productName,
                        sellingProduct.statusName)
                );
            }
        }
        return crossSellingProducts;
    }

    public void renderContact(Contact contact, String leadCategoryText) {
        cbSameAs.setChecked(contact.isSameAsAbove);
        int ucid = Integer.valueOf(stringOrZero(contact.uCID));
        if (ucid > 0) {
            llClient.setVisibility(View.VISIBLE);
            llClientPortfolioDetails.setVisibility(View.VISIBLE);
            webServiceportfolioSnapshot(String.valueOf(ucid));
            webServicePL(String.valueOf(ucid));
            getClientDetail(ucid);
            toggleSectionExpansion(llClientDetail, tvClientDetail);
        }
        etPanNumber.setText(contact.panNumber);
        etAadharNumber.setText(contact.aadharNumber);
        etEkycMobileNumber.setText(contact.eKYCMobileNo);
        etMobileNumber.setText(contact.mobile);
        etEmail.setText(contact.emailId);
        etAlternateEmail.setText(contact.alternateEmailId);
        etLandlineNumber.setText(contact.landlineNo);
        etAlternateMobileNumber.setText(contact.alternateMobileNo);

        String newDob[] = stringOrEmpty(contact.dOB).split("T");
        String newDoa[] = stringOrEmpty(contact.dOA).split("T");

        try {
            if (!newDob[0].isEmpty()) {
                Date Dob = CRMAppUtil.getDate(newDob[0], "yyyy-MM-dd");
                etDob.setText(CRMAppUtil.getDateStr(Dob, "dd-MM-yyyy"));
            }
            if (!newDoa[0].isEmpty()) {
                Date Doa = CRMAppUtil.getDate(newDoa[0], "yyyy-MM-dd");
                etAnniversary.setText(CRMAppUtil.getDateStr(Doa, "dd-MM-yyyy"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<ContactAddressItem> contactAddress = contact.contactAddress;
        if (contactAddress != null && contactAddress.size() > 0) {
            renderContactAddress(contactAddress);
        }
        tvPanCard.setText(contact.panNumber);
        tvAadharCard.setText(contact.aadharNumber);
        etLeadName.setText(contact.name);
        ilLeadName.setHint(leadCategoryText + " Name*");
        try {
            etCreationDate.setText(
                getDateFromString(creationDate,
                    "yyyy-MM-dd'T'kk:mm:ss", DATE_FORMAT_TO_DISPLAY));
        } catch (Exception e) {
            creationDate = creationDate.replace("T", " ");
            etCreationDate.setText(creationDate);
        }
        if (arrAdapProfession == null) {
            getProfession();
        } else {
            RowData dataProfile = RowData.getSpinnerRowDataById(profession, arrAdapProfession);
            if (dataProfile != null) {
                spProfession.setSelection(dataProfile.getPosition());
            }
            spProfession.setOnItemSelectedListener(professionListener);
        }
        if (arrAdapProfile == null) {
            selectProfile();
        } else {
            RowData dataProfile = RowData.getSpinnerRowDataByValue(profile, arrAdapProfile);
            if (dataProfile != null) {
                spProfile.setSelection(dataProfile.getPosition());
            }
            spProfile.setOnItemSelectedListener(profileListener);
        }
        etContactPerson.setText(contactPerson);
    }

    public void renderContactAddress(List<ContactAddressItem> contactAddress) {
        for (ContactAddressItem address : contactAddress) {
            int addressTypeId = address.addressTypeID;
            String address1 = stringOrEmpty(address.address1);
            String address2 = stringOrEmpty(address.address2);
            String address3 = stringOrEmpty(address.address3);
            String stateId = String.valueOf(address.stateID);
            String cityId = String.valueOf(address.cityID);
            String countryID = String.valueOf(address.countryID);
            String pinCode = stringOrEmpty(address.pincode);
            boolean isPrimary = address.isPrimary;
            if (addressTypeId == RESIDENCEADDRESS) {
                cbPrimaryAddress.setChecked(isPrimary);
                etAdd1.setText(address1);
                etAdd2.setText(address2);
                etAdd3.setText(address3);
                etPincode.setText(pinCode);
                stateStr = stateId;
                cityStr = cityId;
                if (arrAdapCountry == null) {
                    getCountry();
                } else {
                    RowData dataCountry = RowData.getSpinnerRowDataById(countryID, arrAdapCountry);
                    if (dataCountry != null) {
                        spCountry.setSelection(dataCountry.getPosition());
                    }
                    String key = ((RowData) spCountry.getSelectedItem()).getKey();
                    getState(key, RESIDENCE, stateStr);
                }
                if (cbPrimaryAddress.isChecked()) {
                    cbOffPrimaryAddress.setVisibility(View.GONE);
                    cbSameAs.setVisibility(View.VISIBLE);
                } else {
                    cbOffPrimaryAddress.setVisibility(View.VISIBLE);
                    cbSameAs.setVisibility(View.GONE);
                }
            } else if (addressTypeId == OFFICEADDRESS) {
                cbOffPrimaryAddress.setChecked(isPrimary);
                etOffAdd1.setText(address1);
                etOffAdd2.setText(address2);
                etOffAdd3.setText(address3);
                etOffPincode.setText(pinCode);
                offAdd1 = address1;
                offAdd2 = address2;
                offAdd3 = address3;
                offPincode = pinCode;
                offCountryId = countryID;
                offStateStr = stateId;
                offCityStr = cityId;
                if (arrAdapCountry == null) {
                    getCountry();
                } else {
                    RowData dataCountry = RowData.getSpinnerRowDataById(countryID, arrAdapCountry);
                    if (dataCountry != null) {
                        spOffCountry.setSelection(dataCountry.getPosition());
                    }
                    if (!cbSameAs.isChecked()) {
                        String key = ((RowData) spOffCountry.getSelectedItem()).getKey();
                        getState(key, OFFICE, offStateStr);
                    }
                }
            }
        }
    }

    public int getSpinnerPosition(String value, ArrayList<String> arrayList) {

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

    private void updateLead(LeadDetailModel model) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            List<ContactAddressRequest> contactAddresses = new ArrayList<>();
            List<AddressDetailModel> detailModels = model.getAddressDetailModels();
            for (AddressDetailModel detailModel : detailModels) {
                contactAddresses.add(new ContactAddressRequest(
                    String.valueOf(detailModel.getAddressTypeID()),
                    detailModel.getAddress1(),
                    detailModel.getAddress2(),
                    detailModel.getAddress3(),
                    detailModel.getStateID(),
                    detailModel.getCountryID(),
                    detailModel.getCityID(),
                    detailModel.getPincode(),
                    detailModel.isPrimary(),
                    detailModel.getBusinessUnitID()
                ));
            }
            ContactRequest contactRequest = new ContactRequest(
                model.getName(),
                model.geteMail(),
                model.getAlternateEmail(),
                model.getMobileNo(),
                model.getEkycMobileNo(),
                model.getLandLineNo(),
                model.getAlternateMobileNo(),
                model.getAadharNum(),
                model.getPanNum(),
                model.getDob(),
                model.getAnniversary(),
                model.isSameAsAbove(),
                contactAddresses
            );
            UpdateLeadRequest updateLeadRequest = new UpdateLeadRequest(
                model.getLeadID(), model.getBusinessUnitID(), model.getIsPotentialPMSProduct(),
                model.getLeadCategoryID(), model.getContactPerson(), model.getSourceID(),
                model.getLeadSourceID(), model.getLeadSourceDetailsID(),
                model.getLeadSourceDetails(), model.getRefereeCode(), model.getRefereeName(),
                model.getRefereeEmail(), model.getRefereeMobile(), model.getComments(),
                model.getProfileID(),
                model.getProfessionID(), model.getCreatedBy(), model.getProspectRefereName(),
                model.getProspectRefereMobile(), model.getProspectRefereEmail(),
                model.getCreatedThrough(), model.getCreationDate(),
                contactRequest,
                model.getLeadFieldValueModels()
            );
            compositeDisposable.add(saathiApi.updateLead(updateLeadRequest)
                .compose(applyUiModel())
                .subscribe(this::renderUpdateLead));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderUpdateLead(UiModel<UpdateLeadResponse> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        UpdateLeadResponse data = uiModel.data;
        if (data != null) {
            String approvalMessage = stringOrEmpty(data.approvalMessage);
            alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal),
                (approvalMessage.isEmpty() ? "Lead Updated Successfully"
                    : approvalMessage), "Ok", true,
                (action, a2) -> {
                    if (action == MSFDialog.Action.OK) {
                        setEnableDisable(false);
                        editableFlag = true;
                        leadSection();
                    }
                });
        }
    }

    private void selectProfile() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), LEADPROFILE, null, null);
            compositeDisposable.add(saathiApi.getAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderLeadProfile));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderLeadProfile(UiModel<List<LookupItem>> uiModel) {
        Throwable error = uiModel.error;
        if (error != null) {
            arrSelectProfile.clear();
            showOnFailurePopup(error);
        }

        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrSelectProfile.clear();
            arrSelectProfile.add(new RowData(0, "-1", SELECT_PROFILE));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                arrSelectProfile.add(
                    new RowData(i + 1,
                        String.valueOf(item.lookUpid),
                        stringOrEmpty(item.lookUpName)
                    ));
            }
            arrAdapProfile = new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item,
                arrSelectProfile);
            arrAdapProfile.setDropDownViewResource(R.layout.mdropdown);
            spProfile.setAdapter(arrAdapProfile);
            spProfile.setOnItemSelectedListener(profileListener);
            arrAdapProfile.notifyDataSetChanged();
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
                            searchProductSpecialist(constraint, searchListProductSpecialist);
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
                            searchUserBusinessProfile(constraint, searchList);
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

        getStage(spStage, "", progressLayout);
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
                    return;
                }
                if (spStage.getSelectedItemPosition() < 1) {
                    tvspStageError.setVisibility(View.VISIBLE);
                    tvspStageError.setText("Select Stage");
                    return;
                }
                if (spStatus.getSelectedItemPosition() < 1) {
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
                if (leadDetailAddProductAdapter.checkProduct(interestedInKey)) {
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

    void searchProductSpecialist(@Nullable CharSequence constraint,
        CustomListPickerDialog productSpecialistDialog) {
        if (constraint == null) return;
        ProductSpecialistRequest specialistRequest;
        try {
            primaryRMAdd =
                CRMAppUtil.isBlank(leadOwner.trim()) ? advisorCode
                    : leadOwner.trim();
            specialistRequest =
                new ProductSpecialistRequest(
                    URLEncoder.encode(constraint.toString(), "utf-8"),
                    URLEncoder.encode(constraint.toString(), "utf-8"),
                    primaryRMAdd
                );
        } catch (Exception ex) {
            return;
        }

        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            compositeDisposable.add(
                saathiApi.getProductSpecialist(specialistRequest)
                    .compose(applyUiModel())
                    .subscribe(uiModel -> {
                        if (uiModel.inProgress) {
                            productSpecialistDialog.showProgress(View.VISIBLE);
                        } else {
                            productSpecialistDialog.showProgress(View.GONE);
                        }

                        Throwable error = uiModel.error;
                        if (error != null) {
                            final ArrayList<EmployeeModel> modelList = new ArrayList<>();
                            modelList.add(EmployeeModel.getNoData(getErrorOnFailure(error)));
                            final AttendeeAdapter mYAdapter =
                                new AttendeeAdapter(getMainActivity(), modelList);
                            productSpecialistDialog.setAdapter(
                                mYAdapter);
                        }

                        List<ProductSpecialistItem> data = uiModel.data;
                        if (data != null) {
                            final ArrayList<EmployeeModel> modelList = new ArrayList<>();
                            for (ProductSpecialistItem datum : data) {
                                if (AccountDetails.getEmployeeCode()
                                    .equalsIgnoreCase(datum.employeeCode)) {
                                    continue;
                                }
                                modelList.add(new EmployeeModel(
                                    stringOrEmpty(datum.employeeName),
                                    stringOrEmpty(datum.employeeCode),
                                    stringOrEmpty(datum.branchCode),
                                    false));
                            }
                            if (modelList.isEmpty()) {
                                modelList.add(EmployeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE));
                            }
                            final AttendeeAdapter mYAdapter =
                                new AttendeeAdapter(getMainActivity(), modelList);
                            productSpecialistDialog.setAdapter(mYAdapter);
                        }
                    }));
        } else {
            noInternetConnectionDialog();
        }
    }

    void searchUserBusinessProfile(@Nullable CharSequence constraint,
        CustomListPickerDialog searchList) {
        if (constraint == null) return;
        UserBusinessProfileRequest request;
        try {
            request = new UserBusinessProfileRequest(
                URLEncoder.encode(constraint.toString(), "utf-8"),
                URLEncoder.encode(constraint.toString(), "utf-8")
            );
        } catch (Exception ex) {
            return;
        }
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            compositeDisposable.add(saathiApi.getUserBusinessProfile(request)
                .compose(applyUiModel())
                .subscribe(uiModel -> {
                    if (uiModel.inProgress) {
                        hideKeyboard(leadViewDetailView);
                        searchList.showProgress(View.VISIBLE);
                    } else {
                        searchList.showProgress(View.GONE);
                    }

                    Throwable error = uiModel.error;
                    if (error != null) {
                        final ArrayList<EmployeeModel> modelList = new ArrayList<>();
                        modelList.add(EmployeeModel.getNoData(getErrorOnFailure(error)));
                        final AttendeeAdapter mYAdapter =
                            new AttendeeAdapter(getMainActivity(), modelList);
                        searchList.setAdapter(mYAdapter);
                    }

                    List<BusinessProfile> data = uiModel.data;
                    if (data != null) {
                        final ArrayList<EmployeeModel> modelList = new ArrayList<>();
                        for (BusinessProfile datum : data) {
                            if (AccountDetails.getEmployeeCode()
                                .equalsIgnoreCase(datum.employeeCode)) {
                                continue;
                            }
                            modelList.add(
                                new EmployeeModel(
                                    stringOrEmpty(datum.employeeName),
                                    stringOrEmpty(datum.employeeCode),
                                    stringOrEmpty(datum.branchCode),
                                    false));
                        }
                        if (modelList.isEmpty()) {
                            modelList.add(EmployeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE));
                        }
                        final AttendeeAdapter mYAdapter =
                            new AttendeeAdapter(getMainActivity(), modelList);
                        searchList.setAdapter(mYAdapter);
                    }
                }));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void addProduct(String interestedIn, String stageId, String statusId,
        String productMapping) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            primaryRMAdd =
                CRMAppUtil.isBlank(leadOwner.trim()) ? advisorCode : leadOwner.trim();
            InsertLeadProductRequest leadProductRequest = new InsertLeadProductRequest(
                mLeadId,
                ucid,
                interestedIn,
                interestedIn,
                primaryRMAdd,
                specialistAdd,
                AccountDetails.getEmployeeCode(),
                CRMAppUtil.getDBDate(),
                stageId,
                statusId,
                productMapping
            );
            compositeDisposable.add(saathiApi.insertLeadProductMapping(leadProductRequest)
                .compose(applyUiModel())
                .subscribe(this::renderInsertLeadMapping));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderInsertLeadMapping(UiModel<List<LeadProductMappingItem>> uiModel) {
        toggleProgress(uiModel.inProgress);
        if (uiModel.inProgress) {
            hideKeyboard(leadViewDetailView);
        }

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        List<LeadProductMappingItem> data = uiModel.data;
        if (data != null) {
            if ("0".equalsIgnoreCase(mLeadId)) {
                LeadProductMappingItem productItem = data.get(0);
                String leadID = String.valueOf(productItem.leadID);
                String uCID = String.valueOf(productItem.uCID);
                alertDialog(getActivity(), 0,
                    getResources().getString(
                        R.string.motilal_oswal),
                    "Product Added Successfully with leadId "
                        + leadID, "OK", true,
                    (action, data1) -> {
                        if (action == MSFDialog.Action.OK) {
                            Bundle bundle = new Bundle();
                            bundle.putString(LEAD_ID, leadID);
                            bundle.putString(UCID, uCID);
                            navigateTo(
                                NAV_TO_LEAD_VIEW_MAIN,
                                bundle,
                                getFragmentManager(),
                                true);
                        }
                    });
            } else {
                if (data.size() > 0) {
                    for (LeadProductMappingItem datum : data) {
                        String potentialPMSProduct = stringOrEmpty(datum.isPotentialPMSProduct);
                        isPotentialPMSProduct = potentialPMSProduct.isEmpty();
                        spPmsProduct.setSelection(
                            getSpinnerPosition(potentialPMSProduct, arrPmsProduct));

                        leadDetailAddProductList.add(getLeadDetailAddProductModel(datum));
                    }
                    leadDetailAddProductAdapter =
                        new LeadDetailAddProductAdapter(requireContext(), leadDetailAddProductList);
                    lvAddProduct.setAdapter(leadDetailAddProductAdapter);
                    setListViewHeightBasedOnChildrenDetail(lvAddProduct);
                }
                alertDialog(getActivity(), 0,
                    getResources().getString(R.string.motilal_oswal),
                    "Product Added Successfully", "OK",
                    true, (action, data12) -> {
                    });
            }
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
            } else if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                etLeadName.getText().toString().trim())) {

                tvNameError.setText(ErrorMsgName);
                tvNameError.setVisibility(View.VISIBLE);
            } else if (!isIndividualsFlag && isBlank(etContactPerson.getText().toString().trim())) {
                tvContactPersonError.setText("Enter contact person");
                tvContactPersonError.setVisibility(View.VISIBLE);
                tvContactPersonError.requestFocus();
                return false;
            } else if (!isIndividualsFlag
                && !CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
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
                tvEmailIdError.setText(INVALID_EMAIL_ID);
                tvEmailIdError.setVisibility(View.VISIBLE);
                etEmail.requestFocus();
                return false;
            } else if (!isBlank(etAlternateEmail.getText().toString()) && !isValidEmail(
                etAlternateEmail.getText().toString().trim())) {
                tvAlternateEmailIdError.setText(INVALID_EMAIL_ID);
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
            } else if (!isBlank(etProspectRefereeName.getText().toString().trim()) &&
                !CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
                    etProspectRefereeName.getText().toString().trim())) {
                tvProspectRefereeNameError.setText(ErrorMsgName);
                tvProspectRefereeNameError.setVisibility(View.VISIBLE);
                return false;
            } else if (etProspectSource.getText().toString().equals(WEBSITE_PROSPECT_REFERRAL)
                && isBlank(etProspectRefereeName.getText().toString().trim())) {
                tvProspectRefereeNameError.setText(ENTER_PROSPECT_REFEREE_NAME);
                tvProspectRefereeNameError.setVisibility(View.VISIBLE);
                etProspectRefereeName.requestFocus();
                return false;
            } else if (!isBlank(etProspectRefereerMobile.getText().toString())
                && !CRMValidationRule.validateMobile(
                etProspectRefereerMobile.getText().toString().trim())) {
                tvProspectRefereerMobileError.setText(INVALID_MOBILE_NO);
                tvProspectRefereerMobileError.setVisibility(View.VISIBLE);
                etProspectRefereerMobile.requestFocus();
                return false;
            } else if (etProspectSource.getText().toString().equals(WEBSITE_PROSPECT_REFERRAL)
                && isBlank(etProspectRefereerMobile.getText().toString())) {
                tvProspectRefereerMobileError.setText(ENTER_MOBILE_NUMBER);
                tvProspectRefereerMobileError.setVisibility(View.VISIBLE);
                etProspectRefereerMobile.requestFocus();
                return false;
            } else if (!isBlank(etProspectRefereerEmail.getText().toString()) && !isValidEmail(
                etProspectRefereerEmail.getText().toString().trim())) {
                tvProspectRefereerEmailError.setText(INVALID_EMAIL_ID);
                tvProspectRefereerEmailError.setVisibility(View.VISIBLE);
                etProspectRefereerEmail.requestFocus();
                return false;
            } else if (etProspectSource.getText().toString().equals(WEBSITE_PROSPECT_REFERRAL)
                && isBlank(etProspectRefereerEmail.getText().toString())) {
                tvProspectRefereerEmailError.setText(ENTER_EMAIL_ID);
                tvProspectRefereerEmailError.setVisibility(View.VISIBLE);
                etProspectRefereerEmail.requestFocus();
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
            stateCodeStr,
            cityCodeStr, etPincode.getText().toString().trim(),
            cbPrimaryAddress.isChecked(), AccountDetails.getBusinessUnitId()));
        addressDetailList.add(new AddressDetailModel(OFFICEADDRESS,
            getJsonSafeString(etOffAdd1.getText().toString().trim()),
            getJsonSafeString(etOffAdd2.getText().toString().trim()),
            getJsonSafeString(etOffAdd3.getText().toString().trim()), offCountryCodeStr,
            stateCodeStr,
            cityCodeStr,
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
        leadDetailModel.setIsPotentialPMSProduct(spPmsProduct.getSelectedItem().toString());
        leadDetailModel.setProspectRefereName(etProspectRefereeName.getText().toString().trim());
        leadDetailModel.setProspectRefereMobile(
            etProspectRefereerMobile.getText().toString().trim());
        leadDetailModel.setProspectRefereEmail(etProspectRefereerEmail.getText().toString().trim());
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
        leadDetailModel.setCreationDate(etCreationDate.getText().toString().trim()
            .replace("p.m.", "PM")
            .replace("a.m", "AM"));
        leadDetailModel.setContactDetailModels(contactDetailList);
        leadDetailModel.setAddressDetailModels(addressDetailList);
        leadDetailModel.setSecondaryRM(null);
        leadDetailModel.setLeadDetailAddProductModels(leadDetailAddProductList);
        leadDetailModel.setLeadFieldValueModels(getLeadFieldValues(mLeadFieldSectionModelList));
        updateLead(leadDetailModel);
    }

    private void getStage(final Spinner spStageDialog, final String stageId,
        final LinearLayout progressLayout) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            String stageID = isBlank(stageId) ? null : stageId;
            compositeDisposable.add(saathiApi.getStageList(
                new StageRequest(AccountDetails.getBusinessUnitId(), stageID))
                .compose(applyUiModel())
                .subscribe(uiModel -> renderStage(uiModel, spStageDialog, progressLayout)));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderStage(UiModel<List<StageItem>> uiModel, final Spinner spStageDialog,
        final LinearLayout progressLayout) {
        if (uiModel.inProgress) {
            progressLayout.setVisibility(View.VISIBLE);
        } else {
            progressLayout.setVisibility(View.GONE);
        }

        Throwable error = uiModel.error;
        if (error != null) {
            arrStage = new ArrayList<>();
            showOnFailurePopup(error);
        }

        List<StageItem> data = uiModel.data;
        if (data != null) {
            arrStage = new ArrayList<>();
            arrStage.add(new RowData(0, "-1", "Select"));
            for (int i = 0; i < data.size(); i++) {
                StageItem item = data.get(i);
                arrStage.add(
                    new RowData(i + 1,
                        String.valueOf(item.stageID),
                        stringOrEmpty(item.stageName)
                    ));
            }

            arrAdapStage =
                new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrStage);
            arrAdapStage.setDropDownViewResource(R.layout.mdropdown);
            spStageDialog.setAdapter(arrAdapStage);
            RowData stageData = getSpinnerRowDataById(stageId, arrAdapStage);
            if (stageData != null) {
                spStageDialog.setSelection(stageData.getPosition());
            }
        }
    }

    private void GetStageSelectList(final Spinner spStageDialog, final String stageId,
        final LinearLayout progressLayout) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            String stageID = isBlank(stageId) ? null : stageId;
            StageMobileRequest mobileRequest =
                new StageMobileRequest(AccountDetails.getBusinessUnitId(), stageID);
            compositeDisposable.add(saathiApi.getStageMobileList(mobileRequest)
                .compose(applyUiModel())
                .subscribe(uiModel -> renderStageMobile(uiModel, spStageDialog, progressLayout)));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderStageMobile(UiModel<List<StageMobileItem>> uiModel,
        final Spinner spStageDialog, final LinearLayout progressLayout) {
        if (uiModel.inProgress) {
            progressLayout.setVisibility(View.VISIBLE);
        } else {
            progressLayout.setVisibility(View.GONE);
        }

        Throwable error = uiModel.error;
        if (error != null) {
            arrStage = new ArrayList<>();
            showOnFailurePopup(error);
        }

        List<StageMobileItem> data = uiModel.data;
        if (data != null) {
            arrStage = new ArrayList<>();
            arrStage.add(new RowData(0, "-1", "Select", ""));
            for (int i = 0; i < data.size(); i++) {
                StageMobileItem item = data.get(i);
                arrStage.add(new RowData(i + 1,
                    String.valueOf(item.stageID),
                    stringOrEmpty(item.stageName),
                    stringOrEmpty(item.showPotentialPopup)
                ));
            }
            arrAdapStage =
                new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrStage);
            arrAdapStage.setDropDownViewResource(R.layout.mdropdown);
            spStageDialog.setAdapter(arrAdapStage);
            RowData stageData = getSpinnerRowDataById(stageId, arrAdapStage);
            if (stageData != null) {
                spStageDialog.setSelection(stageData.getPosition());
            }
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

    private List<LeadFieldValueRequest> getLeadFieldValues(
        @Nullable List<LeadFieldSectionModel> fieldSectionModels) {
        List<LeadFieldValueRequest> requests = new ArrayList<>();
        if (fieldSectionModels == null) {
            return requests;
        }
        for (LeadFieldSectionModel mSection : fieldSectionModels) {
            List<LeadFieldValueModel> selectionList = mSection.getSelectionList();
            if (selectionList == null) {
                continue;
            }
            for (LeadFieldValueModel modal : selectionList) {
                requests.add(new LeadFieldValueRequest(
                    modal.getLF_SysName(),
                    modal.getUiValue(),
                    modal.getLeadFieldId(),
                    LeadFieldValueModel.getLeadid()
                ));
            }
        }
        return requests;
    }

    private void getClientDetail(int uCid) {
        if (uCid <= 0) return;
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            compositeDisposable.add(
                saathiApi.getClientDetail(new ClientDetailRequest(uCid))
                    .compose(applyUiModel())
                    .subscribe(this::renderClientDetail));
        } else {
            noInternetConnectionDialog();
        }
    }

    public void renderClientDetail(UiModel<ClientDetailItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        ClientDetailItem data = uiModel.data;
        if (data != null) {
            List<ProductsItem> products = data.products;
            if (products != null && products.size() > 0) {
                ProductsItem product = products.get(0);
                tvChannel.setText(product.channel);
                tvUCID.setText(String.valueOf(product.uCID));
                tvBranchCode.setText(product.branchCode);
                tvClientCode.setText(product.holdingNumber);

                List<AdvisorsItem> advisors = product.advisors;
                if (advisors != null && advisors.size() > 0) {
                    advisorCode = stringOrEmpty(advisors.get(0).advisorCode);
                }
            }
            tvClientAddress.setText(data.address);
            tvClientName.setText(data.clientName);
            tvPanCard.setText(data.pANNumber);
            tvAccountType.setText(data.bankAccountType);
            tvFamilyUCID.setText(data.familyUCID);
            tvAdvisoryType.setText(data.advisoryType);
            tvAadharCard.setText(data.aadhaarNumber);
            tvAccountNumber.setText(data.bankAccountNumber);
            tvProfile.setText(data.profile);
            tvDefaultBank.setText(data.bankName);
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
        if (MSFStatistics.isNetworkEnabled(getMainActivity())) {
            compositeDisposable.add(saathiApi.getClientSummary(
                "https://login.motilaloswal.com/Partner/PartnerMobileWebServices.asmx/GetClientSummary",
                AccountDetails.getUserId(), ucid)
                .compose(applyUiModel())
                .subscribe(this::renderClientSummary));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderClientSummary(UiModel<ClientSummaryResponse> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            Timber.e(error);
        }
        ClientSummaryResponse data = uiModel.data;
        if (data != null) {
            int bgcolor = 0;
            List<PortfolioSnapshotItem> snapshotProduct = data.portfolioSnapshot;
            PortfolioSnapshotItem item = snapshotProduct.get(0);
            String netWorth = stringOrEmpty(item.networth);
            String dayChangeAmount = stringOrEmpty(item.dayChangeAmount);
            String dayChangePer = stringOrEmpty(item.dayChangePercentage);
            String overallChangeAmt = stringOrEmpty(item.overallChangeAmount);
            String overallChangePer = stringOrEmpty(item.overallChangePercentage);

            txtNetworth.setText(StringStuff.commaDecorator(netWorth));
            txtDaychgamt.setText(StringStuff.commaDecorator(dayChangeAmount));
            txtDaychgperst.setText(
                "(" + StringStuff.commaDecorator(dayChangePer) + " %)");

            if (dayChangeAmount.startsWith("-")) {

                bgcolor = R.color.action_bar_negative;
            } else {

                bgcolor = R.color.action_bar_positive;
            }
            txtDaychgamt.setBackgroundColor(getResources().getColor(bgcolor));

            if (dayChangePer.startsWith("-")) {

                bgcolor = R.color.action_bar_negative;
            } else {

                bgcolor = R.color.action_bar_positive;
            }
            txtDaychgperst.setTextColor(getResources().getColor(bgcolor));

            txtOverallchgamt.setText(
                StringStuff.commaDecorator(overallChangeAmt));
            txt_overallchgperst.setText(
                "(" + StringStuff.commaDecorator(overallChangePer) + " %)");
            if (overallChangeAmt.startsWith("-")) {

                bgcolor = R.color.action_bar_negative;
            } else {

                bgcolor = R.color.action_bar_positive;
            }
            txtOverallchgamt.setBackgroundColor(
                getResources().getColor(bgcolor));

            if (overallChangePer.startsWith("-")) {

                bgcolor = R.color.action_bar_negative;
            } else {

                bgcolor = R.color.action_bar_positive;
            }
            txt_overallchgperst.setTextColor(getResources().getColor(bgcolor));

            arrayList_broking = new ArrayList<>();
            List<BrokingProductItem> brokingProduct = data.brokingProduct;
            for (int i = 0; i < brokingProduct.size(); i++) {
                BrokingProductItem brokingItem = brokingProduct.get(i);
                String product = brokingItem.product;
                String marketVal = brokingItem.marketValue;
                String investmentValue = brokingItem.purchaseValue;
                String overallGainLoss = brokingItem.overallGainLoss;
                String percentageGainLoss = brokingItem.percentageGainLoss;
                String productID = brokingItem.productId;

                brokingMap = new HashMap<>();
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
            arrayList_asset = new ArrayList<>();

            List<AssetProductItem> assetProduct = data.assetProduct;
            for (int x = 0; x < assetProduct.size(); x++) {
                AssetProductItem assetItem = assetProduct.get(x);
                String ProductID = assetItem.productID;
                String product_aaset = assetItem.product;
                String marketValue_asset = assetItem.marketValue;
                String purchaseValue_asset = assetItem.purchaseValue;
                String overallGainLoss_asset = assetItem.overallGainLoss;
                String percentGainLoss_asset = assetItem.percentageGainLoss;
                String productType = assetItem.producttype;

                assestMap = new HashMap<>();
                assestMap.put(MOSLConstants.PRODUCT_ID, ProductID);
                assestMap.put(MOSLConstants.PRODUCT, product_aaset);
                assestMap.put(MOSLConstants.INVEST_AMT,
                    StringStuff.commaDecorator(marketValue_asset));
                assestMap.put(MOSLConstants.PURCHASE_VALUE,
                    StringStuff.commaDecorator(purchaseValue_asset));
                assestMap.put(MOSLConstants.OVERALL_GAIN_LOSS,
                    StringStuff.commaDecorator(overallGainLoss_asset));
                assestMap.put(MOSLConstants.PERCENTAGE_GAIN_LOSS,
                    StringStuff.commaDecorator(percentGainLoss_asset)
                        + " %");
                assestMap.put(MOSLConstants.PRODUCT_TYPE, productType);
                arrayList_asset.add(assestMap);
            }

            PortfolioSnapshotAssetAdapter navListAssetAdapter =
                new PortfolioSnapshotAssetAdapter(
                    getActivity().getApplicationContext(), arrayList_asset);
            navListAssetAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(lvAsset);

            arrayList_cash = new ArrayList<>();

            List<CashPortfolioItem> cashPortfolio = data.cashPortfolio;
            for (int i = 0; i < cashPortfolio.size(); i++) {
                CashPortfolioItem cashItem = cashPortfolio.get(i);

                String productCash = cashItem.product;
                String totalLedger = cashItem.totalLedgerBalance;

                cashMap = new HashMap<>();
                cashMap.put(MOSLConstants.PRODUCT, productCash);
                cashMap.put(MOSLConstants.TOTAL,
                    StringStuff.commaDecorator(totalLedger));

                arrayList_cash.add(cashMap);
            }

            PortfolioSnapshotCashAdapter navListCashAdapter =
                new PortfolioSnapshotCashAdapter(
                    getActivity().getApplicationContext(), arrayList_cash);
            lvCash.setAdapter(navListCashAdapter);
            navListAssetAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(lvCash);
        }
    }

    public void webServicePL(String ucid) {
        if (MSFStatistics.isNetworkEnabled(getMainActivity())) {
            compositeDisposable.add(saathiApi.getClientGainLossSummary(
                "https://login.motilaloswal.com/Partner/PartnerMobileWebServices.asmx/GetClientGainLossSummary",
                AccountDetails.getUserId(), ucid)
                .compose(applyUiModel())
                .subscribe(this::renderGainLossSummary));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderGainLossSummary(UiModel<ClientGainLossSummaryResponse> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            Timber.e(error);
        }
        ClientGainLossSummaryResponse data = uiModel.data;
        if (data != null) {
            int bgcolor = 0;
            List<TotalGainLossItem> totalGainLossProduct = data.totalGainLoss;
            TotalGainLossItem gainLossItem = totalGainLossProduct.get(0);

            String totalSTGain = stringOrEmpty(gainLossItem.tOTALSTGAIN);
            String totalLTGain = stringOrEmpty(gainLossItem.tOTALLTGAIN);
            String totalGain = stringOrEmpty(gainLossItem.tOTALGAIN);
            String stGainPer = stringOrEmpty(gainLossItem.sTGAINPER);
            String ltGainPer = stringOrEmpty(gainLossItem.lTGAINPER);

            tvTotalGainLoss.setText(StringStuff.commaDecorator(totalGain));

            tvSTGainLoss.setText(StringStuff.commaDecorator(totalSTGain));
            tvSTGainLossPer.setText(
                "(" + StringStuff.commaDecorator(stGainPer) + " %)");
            tvLTGainLoss.setText(StringStuff.commaDecorator(totalLTGain));
            tvLTGainLossPer.setText(
                "(" + StringStuff.commaDecorator(ltGainPer) + " %)");

            if (totalSTGain.startsWith("-")) {

                bgcolor = R.color.action_bar_negative;
            } else {

                bgcolor = R.color.action_bar_positive;
            }
            tvSTGainLoss.setBackgroundColor(getResources().getColor(bgcolor));

            if (stGainPer.startsWith("-")) {

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

            if (ltGainPer.startsWith("-")) {

                bgcolor = R.color.action_bar_negative;
            } else {

                bgcolor = R.color.action_bar_positive;
            }
            tvLTGainLossPer.setTextColor(getResources().getColor(bgcolor));

            arrLstPLBrok = new ArrayList<>();
            List<BrokingGainLossItem> brokingGainloss = data.brokingProductwiseGainloss;
            for (int i = 0; i < brokingGainloss.size(); i++) {
                BrokingGainLossItem brokingItem = brokingGainloss.get(i);

                String productId = brokingItem.assetSubTypeId;
                String product = brokingItem.assetSubTypeId;
                String stGainLoss = brokingItem.assetSubTypeId;
                String ltGainLoss = brokingItem.assetSubTypeId;
                String totalGainLoss = brokingItem.assetSubTypeId;
                String assetSubTypeID = brokingItem.assetSubTypeId;

                brokPLHashMap = new HashMap<>();
                brokPLHashMap.put(PRODUCT_ID, productId);
                brokPLHashMap.put(PRODUCT, product);
                brokPLHashMap.put(STGL, StringStuff.commaDecorator(stGainLoss));
                brokPLHashMap.put(LTGL, StringStuff.commaDecorator(ltGainLoss));
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

            arrLstPLAsset = new ArrayList<>();
            List<AssetGainLossItem> assetGainloss = data.assetProductwiseGainloss;
            for (int i = 0; i < assetGainloss.size(); i++) {
                AssetGainLossItem assetItem = assetGainloss.get(i);
                String productId = assetItem.assetSubTypeId;
                String product = assetItem.assetSubTypeId;
                String stGainLoss = assetItem.assetSubTypeId;
                String ltGainLoss = assetItem.assetSubTypeId;
                String totalGainLoss = assetItem.assetSubTypeId;

                assetPLHashMap = new HashMap<>();
                assetPLHashMap.put(PRODUCT_ID, productId);
                assetPLHashMap.put(PRODUCT, product);
                assetPLHashMap.put(STGL, StringStuff.commaDecorator(stGainLoss));
                assetPLHashMap.put(LTGL, StringStuff.commaDecorator(ltGainLoss));
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

            arrLstPLLimits = new ArrayList<>();
            List<AvailableLimitsItem> availableLimits = data.availableLimits;
            for (int i = 0; i < availableLimits.size(); i++) {
                AvailableLimitsItem limitsItem = availableLimits.get(i);

                String cash = limitsItem.cASH;
                String amount = limitsItem.aMOUNT;

                limitsPLHashMap = new HashMap<>();
                limitsPLHashMap.put(CASH, cash);
                limitsPLHashMap.put(AMOUNT, StringStuff.commaDecorator(amount));

                arrLstPLLimits.add(limitsPLHashMap);
            }
            PortfolioPLLimitsAdapter plLimitsAdapter =
                new PortfolioPLLimitsAdapter(getActivity().getApplicationContext(),
                    arrLstPLLimits);
            lvPLLimits.setAdapter(plLimitsAdapter);
            plAssetAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(lvPLLimits);
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
                    ClientProfileRequest profileRequest = new ClientProfileRequest(
                        ((RowData) spProfileUpdate.getSelectedItem()).getValue(),
                        false,
                        tvUCID.getText().toString().trim()
                    );
                    compositeDisposable.add(saathiApi.saveClientProfile(profileRequest)
                        .compose(applyUiModel())
                        .subscribe(this::renderUpdateProfile));
                } else {
                    noInternetConnectionDialog();
                }
                dialog.dismiss();
            }

            private void renderUpdateProfile(UiModel<ClientProfileResponse> uiModel) {
                toggleProgress(uiModel.inProgress);

                Throwable error = uiModel.error;
                if (error != null) {
                    showOnFailurePopup(error);
                }

                ClientProfileResponse data = uiModel.data;
                if (data != null) {
                    String approvalMessage = stringOrEmpty(data.approvalMessage);
                    if (approvalMessage.isEmpty()) {
                        tvProfile.setText(data.clientPotential);
                    } else {
                        alertDialog(getMainActivity(), 0,
                            getResources().getString(R.string.motilal_oswal),
                            approvalMessage, "OK", true, null);
                    }
                }
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

        public void addProduct(LeadDetailAddProductModel addProduct) {
            if (this.leadDetailAddProductModelList == null) {
                this.leadDetailAddProductModelList = new ArrayList<>();
            }

            this.leadDetailAddProductModelList.add(addProduct);
            notifyDataSetChanged();
            setListViewHeightBasedOnChildrenDetail(
                lvAddProduct);
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
                                    SaveLeadCrossSellingProductRequest productRequest =
                                        new SaveLeadCrossSellingProductRequest(
                                            etLeadID.getText().toString().trim(),
                                            leadDetailAddProductModel.getInterestedIn(),
                                            ((RowData) spStatusDialog.getSelectedItem()).getKey(),
                                            ((RowData) spProductIdDialog.getSelectedItem()).getKey(),
                                            mSection.getLeadCrossSellingProductId()
                                        );

                                    compositeDisposable.add(
                                        saathiApi.saveLeadCrossSellingProduct(productRequest)
                                            .compose(applyUiModel())
                                            .subscribe(uiModel -> {
                                                toggleProgress(uiModel.inProgress);

                                                Throwable error = uiModel.error;
                                                if (error != null) {
                                                    showOnFailurePopup(error);
                                                }

                                                Object data = uiModel.data;
                                                if (data != null) {
                                                    alertDialog(requireContext(), 0,
                                                        getResources().getString(
                                                            R.string.motilal_oswal),
                                                        "Cross Selling Product Saved successfully",
                                                        "Ok",
                                                        true,
                                                        (action, data1) -> {
                                                            if (action == MSFDialog.Action.OK) {
                                                                leadSection();
                                                            }
                                                        });
                                                }
                                            }));
                                } else {
                                    noInternetConnectionDialog();
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
                    if (isLeadEditable() && !leadDetailAddProductModel.getIsProductDisabled()
                        .equals("1")) {
                        final Dialog dialog = new Dialog(getMainActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow()
                            .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                                    SaveLeadCrossSellingProductRequest productRequest =
                                        new SaveLeadCrossSellingProductRequest(
                                            etLeadID.getText().toString().trim(),
                                            leadDetailAddProductModel.getInterestedIn(),
                                            ((RowData) spStatusDialog.getSelectedItem()).getKey(),
                                            ((RowData) spProductIdDialog.getSelectedItem()).getKey()
                                        );
                                    compositeDisposable.add(
                                        saathiApi.saveLeadCrossSellingProduct(productRequest)
                                            .compose(applyUiModel())
                                            .subscribe(uiModel -> {
                                                toggleProgress(uiModel.inProgress);
                                                if (uiModel.inProgress) {
                                                    hideKeyboard(leadViewDetailView);
                                                }

                                                Throwable error = uiModel.error;
                                                if (error != null) {
                                                    showOnFailurePopup(error);
                                                }

                                                Object data = uiModel.data;
                                                if (data != null) {
                                                    alertDialog(getMainActivity(), 0,
                                                        getResources().getString(
                                                            R.string.motilal_oswal),
                                                        "Cross Selling Product Saved Successfully",
                                                        "Ok", true,
                                                        (action, data12) -> {
                                                            if (action == MSFDialog.Action.OK) {
                                                                leadSection();
                                                            }
                                                        });
                                                }
                                            }));
                                } else {
                                    noInternetConnectionDialog();
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
                    if (isLeadEditable()
                        && !leadDetailAddProductModel.getIsProductDisabled().equals("1")) {
                        if (!leadDetailAddProductModel.isDefault()) {
                            alertDialog(getMainActivity(), 0, "Delete Confirmation",
                                "Are you sure you want to delete the product", "Yes", "No", true,
                                new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action,
                                        Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                                                DeleteLeadCrossSellingProductRequest
                                                    productRequest =
                                                    new DeleteLeadCrossSellingProductRequest(
                                                        etLeadID.getText().toString().trim(),
                                                        leadDetailAddProductModel.getInterestedIn(),
                                                        leadDetailAddProductModel.getLeadMappingProductId(),
                                                        leadDetailAddProductModel.getInterestedIn(),
                                                        leadDetailAddProductModel.getPrimaryRM(),
                                                        AccountDetails.getEmployeeCode(),
                                                        CRMAppUtil.getDBDate(),
                                                        leadDetailAddProductModel.getStageId(),
                                                        leadDetailAddProductModel.getStatusId()
                                                    );
                                                compositeDisposable.add(
                                                    saathiApi.deleteLeadCrossSellingProduct(
                                                        productRequest)
                                                        .compose(applyUiModel())
                                                        .subscribe(
                                                            this::renderDeleteCrossSellingProduct));
                                            } else {
                                                noInternetConnectionDialog();
                                            }
                                        }
                                    }

                                    private void renderDeleteCrossSellingProduct(
                                        UiModel<Object> uiModel) {
                                        toggleProgress(uiModel.inProgress);

                                        Throwable error = uiModel.error;
                                        if (error != null) {
                                            showOnFailurePopup(error);
                                        }

                                        Object data = uiModel.data;
                                        if (data != null) {
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                "Products Deleted Successfully",
                                                "Ok", true, null);
                                            LeadDetailAddProductModel
                                                model = getProduct(
                                                leadDetailAddProductModel.getInterestedIn());
                                            leadDetailAddProductModelList.remove(model);
                                            notifyDataSetChanged();
                                            setListViewHeightBasedOnChildren(lvAddProduct);
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
                    if (isLeadEditable() && !leadDetailAddProductModel.getIsProductDisabled()
                        .equals("1")) {
                        editProduct(leadDetailAddProductModel.getInterestedIn(),
                            leadDetailAddProductModel.getStageId(),
                            leadDetailAddProductModel.getStatusId(),
                            leadDetailAddProductModel.getProductSpecialist(),
                            leadDetailAddProductModel.getLeadMappingProductId(),
                            leadDetailAddProductModel.getPrimaryRM(),
                            leadDetailAddProductModel.isDefault(),
                            leadDetailAddProductModel.getIsProSpeEditDisabled(),
                            leadDetailAddProductModel.getProductSpecialistId());
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

        public boolean checkProduct(String productName) {
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
                compositeDisposable.add(saathiApi.getStatusList(new StatusRequest(productId, "1"))
                    .compose(applyUiModel())
                    .subscribe(uiModel -> renderNewStatus(uiModel, spStatus, progressLayout)));
            } else {
                noInternetConnectionDialog();
            }
        }

        private void renderNewStatus(UiModel<List<StatusItem>> uiModel,
            Spinner spStatus, final LinearLayout progressLayout) {
            if (uiModel.inProgress) {
                progressLayout.setVisibility(View.VISIBLE);
            } else {
                progressLayout.setVisibility(View.GONE);
            }

            Throwable error = uiModel.error;
            if (error != null) {
                arrStatus = new ArrayList<>();
                showOnFailurePopup(error);
            }

            List<StatusItem> data = uiModel.data;
            if (data != null) {
                arrStatus = new ArrayList<>();
                arrStatus.add(new RowData(0, "-1", "Select"));
                for (int i = 0; i < data.size(); i++) {
                    StatusItem statusItem = data.get(i);
                    arrStatus.add(
                        new RowData(i + 1,
                            String.valueOf(statusItem.statusID),
                            stringOrEmpty(statusItem.statusName)
                        ));
                }
                arrAdapStatus =
                    new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrStatus);
                arrAdapStatus.setDropDownViewResource(R.layout.mdropdown);
                spStatus.setAdapter(arrAdapStatus);
                RowData stageData = getSpinnerRowDataById(statusId, arrAdapStatus);
                if (stageData != null) {
                    spStatus.setSelection(stageData.getPosition());
                }
            }
        }

        private void newGetCrossSellingProductId(final String intrestedIn, final Spinner spStatus,
            final String statusId, final LinearLayout progressLayout) {
            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                CrossSellingProductRequest request =
                    new CrossSellingProductRequest(AccountDetails.getBusinessUnitId(), intrestedIn);
                compositeDisposable.add(saathiApi.getCrossSellingProduct(request)
                    .compose(applyUiModel())
                    .subscribe(uiModel ->
                        renderNewCrossSellingProduct(uiModel, statusId, spStatus, progressLayout)));
            } else {
                noInternetConnectionDialog();
            }
        }

        private void renderNewCrossSellingProduct(UiModel<List<CrossSellingProduct>> uiModel,
            final String statusId, final Spinner spStatus, final LinearLayout progressLayout) {
            if (uiModel.inProgress) {
                progressLayout.setVisibility(View.VISIBLE);
            } else {
                progressLayout.setVisibility(View.GONE);
            }

            Throwable error = uiModel.error;
            if (error != null) {
                arrCrossSellingProductId.clear();
                showOnFailurePopup(error);
            }

            List<CrossSellingProduct> data = uiModel.data;
            if (data != null) {
                arrCrossSellingProductId.clear();
                arrCrossSellingProductId.add(new RowData(0, "-1", "Select"));

                for (int i = 0; i < data.size(); i++) {
                    CrossSellingProduct sellingProduct = data.get(i);
                    arrCrossSellingProductId.add(
                        new RowData(i + 1,
                            stringOrEmpty(sellingProduct.productId),
                            stringOrEmpty(sellingProduct.productName)
                        ));
                }
                arrAdapCrossSellingProductId = new ArrayAdapter<>(requireContext(),
                    R.layout.simple_spinner_item, arrCrossSellingProductId);
                arrAdapCrossSellingProductId.setDropDownViewResource(R.layout.mdropdown);
                spStatus.setAdapter(arrAdapCrossSellingProductId);
                RowData stageData = getSpinnerRowDataById(statusId, arrAdapCrossSellingProductId);
                if (stageData != null) {
                    spStatus.setSelection(stageData.getPosition());
                }
            }
        }

        private void newGetCrossSellingStatus(final Spinner spStatus, final String statusId,
            final LinearLayout progressLayout) {
            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                LookupRequest lookupRequest =
                    new LookupRequest(AccountDetails.getBusinessUnitId(), 111, null, null);
                compositeDisposable.add(saathiApi.getAllLookup(lookupRequest)
                    .compose(applyUiModel())
                    .subscribe(
                        uiModel -> renderCrossSellingStatus(uiModel, spStatus, progressLayout)));
            } else {
                noInternetConnectionDialog();
            }
        }

        private void renderCrossSellingStatus(UiModel<List<LookupItem>> uiModel,
            final Spinner spStatus, final LinearLayout progressLayout) {
            if (uiModel.inProgress) {
                progressLayout.setVisibility(View.VISIBLE);
            } else {
                progressLayout.setVisibility(View.GONE);
            }

            Throwable error = uiModel.error;
            if (error != null) {
                arrCrossSellingStatus = new ArrayList<>();
                showOnFailurePopup(error);
            }

            List<LookupItem> data = uiModel.data;
            if (data != null) {
                arrCrossSellingStatus = new ArrayList<>();
                arrCrossSellingStatus.add(new RowData(0, "-1", "Select"));
                for (int i = 0; i < data.size(); i++) {
                    LookupItem item = data.get(i);
                    arrCrossSellingStatus.add(
                        new RowData(i + 1,
                            String.valueOf(item.lookUpid),
                            stringOrEmpty(item.lookUpName)
                        ));
                }
                arrAdapCrossSellingStatus =
                    new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item,
                        arrCrossSellingStatus);
                arrAdapCrossSellingStatus.setDropDownViewResource(R.layout.mdropdown);
                spStatus.setAdapter(arrAdapCrossSellingStatus);
                RowData stageData = getSpinnerRowDataById(statusId, arrAdapCrossSellingStatus);
                if (stageData != null) {
                    spStatus.setSelection(stageData.getPosition());
                }
            }
        }

        private void lowMargin(String productId, final LinearLayout progressLayout,
            final LinearLayout llLowMarginAdd, final boolean isDefault) {
            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                ConfigurationByStatusRequest request =
                    new ConfigurationByStatusRequest(productId);
                compositeDisposable.add(saathiApi.getStageWiseConfigurationByStatus(request)
                    .compose(applyUiModel())
                    .subscribe(uiModel -> renderStageWiseConfiguration(uiModel, progressLayout,
                        llLowMarginAdd, isDefault)));
            } else {
                noInternetConnectionDialog();
            }
        }

        public void renderStageWiseConfiguration(UiModel<List<StageWiseConfiguration>> uiModel,
            final LinearLayout progressLayout, final LinearLayout llLowMarginAdd,
            final boolean isDefault) {
            if (uiModel.inProgress) {
                progressLayout.setVisibility(View.VISIBLE);
            } else {
                progressLayout.setVisibility(View.GONE);
            }

            Throwable error = uiModel.error;
            if (error != null) {
                arrStatus = new ArrayList<>();
                llLowMarginAdd.removeAllViews();
                lowMarginList.clear();
                isLowMargin = false;
                showOnFailurePopup(error);
            }

            List<StageWiseConfiguration> data = uiModel.data;
            if (data != null) {
                arrStatus = new ArrayList<>();
                lowMarginList.clear();
                if (data.size() > 0) {
                    isLowMargin = true;
                    for (StageWiseConfiguration configuration : data) {
                        lowMarginList.add(
                            new LowMargin(
                                stringOrDefault(configuration.fieldDisplayName, "Low Margin"),
                                stringOrEmpty(configuration.fieldSystemName),
                                configuration.isMandatory,
                                configuration.forDefaultProductOnly
                            ));
                    }
                    llLowMarginAdd.removeAllViews();

                    for (LowMargin lowMargin : lowMarginList) {
                        if (isDefault == lowMargin.isDefaultProduct()) {
                            isLowMargin = true;
                            View childValue = getLayoutInflater()
                                .inflate(R.layout.include_edittext_lowmargin, null);
                            TextView tvHead = childValue.findViewById(R.id.tvHead);
                            EditText edHead = childValue.findViewById(R.id.etSection);
                            if (lowMargin.isMandatory()) {
                                tvHead.setText(String.format("%s*", lowMargin.getViewName()));
                                edHead.setHint("At least enter 3 digits");
                            } else {
                                tvHead.setText(lowMargin.getViewName());
                            }
                            lowMargin.setEditText(edHead);
                            llLowMarginAdd.addView(childValue);
                        } else {
                            llLowMarginAdd.removeAllViews();
                            lowMarginList.clear();
                            isLowMargin = false;
                        }
                    }
                } else {
                    llLowMarginAdd.removeAllViews();
                    lowMarginList.clear();
                    isLowMargin = false;
                }
            }
        }

        private void editProduct(final String interestIn, final String stageId,
            final String statusId, String productSpecialist, final String leadMappingId,
            final String asignedTo, final boolean isDefault, String isProductSpecEnabled,
            String productSpecialistId) {
            final Dialog dialog = new Dialog(getMainActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(dialog.getWindow())
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
            specialistEdit = productSpecialistId;
            productSpecialistEdit.setClearIconVisible(true);
            productSpecialistEdit.setHint(getResources().getString(R.string.str_prod_spl));
            if (isProductSpecEnabled.equals("1")) {
                productSpecialistEdit.setEnabled(false);
            } else {
                productSpecialistEdit.setEnabled(true);
            }
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
                                searchProductSpecialist(constraint, searchListProductSpecialist);
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
                                searchUserBusinessProfile(constraint, searchList);
                            }
                        };
                    searchList.setmListner(assingViewListner);
                }
            });
            llInterestedIn.setVisibility(View.GONE);
            GetStageSelectList(spStageDialog, stageId, progressLayout);

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
                        // getSchemeId is ShowPotentialPopup
                        if (((RowData) spStageDialog.getSelectedItem()).getSchmeID()
                            .equalsIgnoreCase(YES)) {
                            if (spPmsProduct.getSelectedItem().toString().equals(NO)) {
                                isPMSProductStageStatus = true;
                            }
                        } else {
                            isPMSProductStageStatus = false;
                        }
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

                    RowData selectedStatus = (RowData) spStatusDialog.getSelectedItem();
                    if (isPotentialPMSProduct || isPMSProductStageStatus) {
                        MSFDialog.alertDialog(getActivity(), 0,
                            getResources().getString(R.string.motilal_oswal),
                            POTENTIAL_INVEST_IN_PRODUCT, YES, NO, true,
                            new MSFDialog.DialogListener() {
                                @Override
                                public void alertDialogAction(MSFDialog.Action action,
                                    Object... data) {
                                    if (action == MSFDialog.Action.OK) {
                                        updateProduct(
                                            interestIn,
                                            leadMappingId,
                                            primaryRM,
                                            specialistEdit,
                                            newStageCodeStr,
                                            selectedStatus.getKey(),
                                            selectedStatus.getValue(),
                                            actAssignTo.getText().toString(), YES, dataMap
                                        );
                                    } else {
                                        updateProduct(
                                            interestIn,
                                            leadMappingId,
                                            primaryRM,
                                            specialistEdit,
                                            newStageCodeStr,
                                            selectedStatus.getKey(),
                                            selectedStatus.getValue(),
                                            actAssignTo.getText().toString(), NO, dataMap
                                        );
                                    }
                                }
                            });
                    } else {
                        updateProduct(
                            interestIn,
                            leadMappingId,
                            primaryRM,
                            specialistEdit,
                            newStageCodeStr,
                            selectedStatus.getKey(),
                            selectedStatus.getValue(),
                            actAssignTo.getText().toString(), "", dataMap
                        );
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

        void updateProduct(String interestIn, String leadMappingId,
            String primaryRM, String productSpecialistId, String newStageCodeStr,
            String statusID, String statusValue, String actAssignTo, String pmsProductStatus,
            Map dataMap) {
            if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
                showProgress();
                UpdateLeadMappingRequest mappingRequest = new UpdateLeadMappingRequest(
                    etLeadID.getText().toString().trim(),
                    interestIn,
                    leadMappingId,
                    interestIn,
                    primaryRM,
                    productSpecialistId,
                    AccountDetails.getEmployeeCode(),
                    CRMAppUtil.getDBDate(),
                    newStageCodeStr,
                    newStageNameStr,
                    pmsProductStatus,
                    statusID,
                    statusValue,
                    actAssignTo,
                    dataMap
                );
                compositeDisposable.add(saathiApi.updateLeadProductMapping(mappingRequest)
                    .compose(applyUiModel())
                    .subscribe(uiModel -> renderUpdateLeadProductMapping(uiModel, mappingRequest)));
            } else {
                noInternetConnectionDialog();
            }
        }

        private void renderUpdateLeadProductMapping(UiModel<List<LeadProductMappingItem>> uiModel,
            UpdateLeadMappingRequest mappingRequest) {
            toggleProgress(uiModel.inProgress);
            if (uiModel.inProgress) {
                hideKeyboard(leadViewDetailView);
            }

            Throwable error = uiModel.error;
            if (error != null) {
                showOnFailurePopup(error);
            }

            List<LeadProductMappingItem> data = uiModel.data;
            if (data != null) {
                if (isLowMargin) {
                    dashbordFragment();
                } else {
                    for (LeadProductMappingItem datum : data) {
                        String potentialPMSProduct = stringOrEmpty(datum.isPotentialPMSProduct);
                        isPotentialPMSProduct = potentialPMSProduct.isEmpty();
                        spPmsProduct.setSelection(
                            getSpinnerPosition(potentialPMSProduct, arrPmsProduct));

                        if (String.valueOf(datum.interestedIn)
                            .equals(mappingRequest.interestedIn)) {
                            updateProductData(
                                mappingRequest.interestedIn,
                                mappingRequest.stageID,
                                mappingRequest.stageName,
                                mappingRequest.statusID,
                                mappingRequest.statusValue,
                                mappingRequest.productSpecialist,
                                mappingRequest.actAssignTo
                            );
                        } else {
                            leadDetailAddProductAdapter.addProduct(
                                getLeadDetailAddProductModel(datum));
                        }
                    }

                    alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        "Products Updated Successfully", "Ok", true, null);
                }
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
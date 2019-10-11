package com.mosl.tabapp.leadcreation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.commons.data.UiModel;
import com.mosl.mobile.user.login.UserManager;
import com.mosl.saathi.api.model.assignto.UserBusinessProfileRequest;
import com.mosl.saathi.api.model.common.SaathiCommonResponse;
import com.mosl.saathi.api.model.createlead.CreateLeadAddressDetails;
import com.mosl.saathi.api.model.createlead.CreateLeadContact;
import com.mosl.saathi.api.model.createlead.CreateLeadProduct;
import com.mosl.saathi.api.model.createlead.CreateLeadRequest;
import com.mosl.saathi.api.model.createlead.CreateLeadResponse;
import com.mosl.saathi.api.model.createlead.DedupeModelResponse;
import com.mosl.saathi.api.model.createlead.LeadSourceItem;
import com.mosl.saathi.api.model.createlead.LeadSourceListRequest;
import com.mosl.saathi.api.model.createlead.SourceListItem;
import com.mosl.saathi.api.model.createlead.SourceListRequest;
import com.mosl.saathi.api.model.createlead.TransferLeadRequest;
import com.mosl.saathi.api.model.createlead.TransferLeadResponse;
import com.mosl.saathi.api.model.lead.BusinessProfile;
import com.mosl.saathi.api.model.lead.CityItem;
import com.mosl.saathi.api.model.lead.CityListRequest;
import com.mosl.saathi.api.model.lead.CountryItem;
import com.mosl.saathi.api.model.lead.LookupItem;
import com.mosl.saathi.api.model.lead.LookupRequest;
import com.mosl.saathi.api.model.lead.StageItem;
import com.mosl.saathi.api.model.lead.StageRequest;
import com.mosl.saathi.api.model.lead.StateProvinceItem;
import com.mosl.saathi.api.model.lead.StateProvinceRequest;
import com.mosl.saathi.api.model.lead.StatusItem;
import com.mosl.saathi.api.model.lead.StatusRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMValidationRule;
import com.mosl.tabapp.common.CustomListPickerDialog;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.common.SearchableSpinner;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.leadViewDetail.AttendeeAdapter;
import com.mosl.tabapp.model.AddressDetailModel;
import com.mosl.tabapp.model.ContactDetailModel;
import com.mosl.tabapp.model.EmployeeModel;
import com.mosl.tabapp.model.LeadCreationContact;
import com.mosl.tabapp.model.LeadCreationModel;
import com.mosl.tabapp.model.LeadCreationProductMapping;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.RefreeSyncWebServiceCall;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.droidparts.widget.ClearableEditText;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.filterOnlyAlphabetAndNumbersAnsAllowedChar;
import static com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.common.CRMMOSLCommon.disableField;
import static com.mosl.tabapp.common.CRMValidationRule.isValidEmail;
import static com.mosl.tabapp.common.CRMValidationRule.validateAadhar;
import static com.mosl.tabapp.common.CRMValidationRule.validateTel;
import static com.mosl.tabapp.constants.MOSLConstants.ATTENDEEE_NOT_FOUND_MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.CONFIRM_LEAD;
import static com.mosl.tabapp.constants.MOSLConstants.ENTER_EMAIL_ID;
import static com.mosl.tabapp.constants.MOSLConstants.ENTER_MOBILE_NUMBER;
import static com.mosl.tabapp.constants.MOSLConstants.ENTER_PROSPECT_REFEREE_NAME;
import static com.mosl.tabapp.constants.MOSLConstants.ErrorMsgName;
import static com.mosl.tabapp.constants.MOSLConstants.INTERESTEDIN;
import static com.mosl.tabapp.constants.MOSLConstants.INVALID_EMAIL_ID;
import static com.mosl.tabapp.constants.MOSLConstants.INVALID_MOBILE_NO;
import static com.mosl.tabapp.constants.MOSLConstants.LEADCATEGORY;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD_ID;
import static com.mosl.tabapp.constants.MOSLConstants.LOGOUT_CODE;
import static com.mosl.tabapp.constants.MOSLConstants.OFFICE;
import static com.mosl.tabapp.constants.MOSLConstants.OFFICEADDRESS;
import static com.mosl.tabapp.constants.MOSLConstants.PRODUCTTYPE;
import static com.mosl.tabapp.constants.MOSLConstants.PROFESSIONID;
import static com.mosl.tabapp.constants.MOSLConstants.RESIDENCE;
import static com.mosl.tabapp.constants.MOSLConstants.RESIDENCEADDRESS;
import static com.mosl.tabapp.constants.MOSLConstants.SAME_EMAILMESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.SAME_MOBILEMESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.SEARCH_LENTH;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static com.mosl.tabapp.constants.MOSLConstants.UCID;
import static com.mosl.tabapp.constants.MOSLConstants.WEBSITE_CLIENT_REFERRAL;
import static com.mosl.tabapp.constants.MOSLConstants.WEBSITE_PROSPECT_REFERRAL;
import static com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_LEAD_CREATION;
import static com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class LeadCreationFragment extends CommonFragment
	implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

	private final ArrayList<RowData> arrCategory = new ArrayList<>();
	private final ArrayList<RowData> arrProfession = new ArrayList<>();
	private final ArrayList<RowData> arrInterested = new ArrayList<>();
	private final ArrayList<RowData> arrProductType = new ArrayList<>();
	private final ArrayList<RowData> arrSource = new ArrayList<>();
	private final ArrayList<RowData> arrProspectSource = new ArrayList<>();
	private final ArrayList<RowData> arrStage = new ArrayList<>();
	private final ArrayList<RowData> arrStatus = new ArrayList<>();
	private final ArrayList<RowData> arrState = new ArrayList<>();
	private final ArrayList<RowData> arrCity = new ArrayList<>();
	private final ArrayList<RowData> arrOffState = new ArrayList<>();
	private final ArrayList<RowData> arrOffCity = new ArrayList<>();
	private final ArrayList<RowData> arrCountry = new ArrayList<>();
	@Inject UserManager userManager;
	private View leadCreationView;
	private TextView tvPersonalInfo, tvAdditionalInfo;
	private LinearLayout llPersonalInformation, llAdditionInfo;
	private EditText etName, etEmailId, etAlternateEmailId, etMobile, etEkycMobile, etPanNum,
		etAadharNum, etLandLine, etRefreeCode, etRefreeName, etRefreeEmail, etRefreeMobile,
		etAlternateMobile, etAdd1, etAdd2, etAdd3, etPincode, etOffAdd1, etOffAdd2, etOffAdd3,
		etOffPincode, etComments, etContactPerson, etProspectSourceDetails, etProspectRefereeName,
		etProspectRefereerMobile, etProspectRefereerEmail;
	private Spinner spSource, spProspectSource, spStage, spStatus, spCategory,
		spInterestIn, spProductType, spCountry, spOffCountry, spProfession;
	private SearchableSpinner spState, spCity, spOffState, spOffCity;
	private TextInputLayout ilName, ilRefereeCode;
	private ClearableEditText actAssignTo;
	private CheckBox cbPrimaryAddress, cbSameAs, cbOffPrimaryAddress;
	private Button btnCreate, btnCancel;
	private LinearLayout llResAdd, llOffAdd;
	private ArrayList<ContactDetailModel> contactDetailList = new ArrayList<>();
	private ArrayList<AddressDetailModel> addressDetailModels = new ArrayList<>();
	private ArrayList<LeadCreationProductMapping> productDetailList = new ArrayList<>();
	private AddressDetailModel residenceAddress, officeAddress;
	private ScrollView scrollView;
	private String categoryCodeStr, categoryNameStr = " ", professionCodeStr = "",
		interestInCodeStr, interestInNameStr, productTypeCodeStr, sourceCodeStr,
		prospectSourceCodeStr, prospectSourceNameStr, stageCodeStr,
		statusCodeStr, countryCodeStr, stateCodeStr,
		cityCodeStr, offCountryCodeStr, offStateCodeStr, offCityCodeStr;
	private ArrayAdapter<RowData> arrAdapCategory, arrAdapProfession, arrAdapInterested,
		arrAdapProductType, arrAdapSource, arrAdapProspectSource, arrAdapStage, arrAdapStatus,
		arrAdapCountry, arrAdapState, arrAdapCity, arrAdapOffState, arrAdapOffCity;
	private String primaryRM;
	private TextView tvCategoryError, tvInterestInError, tvProductTypeError, tvSourceError,
		tvProspectSourceError, tvStageError, tvStatusError, tvCountryError, tvStateError,
		tvCityError, tvOffCountryError, tvOffStateError, tvOffCityError,
		tvNameError, tvEmailIdError, tvAlternateEmailIdError, tvMobileError, tvEkycMobileError,
		tvPanNumError, tvLandLineError, tvAadharNumError, tvRefreeEmailError, tvAdd1Error,
		tvAdd2Error, tvPincodeError, tvOffPincodeError, tvRefereeMobileError, tvAlternateMobError,
		tvContactPersonError, tvRefereeCodeError, tvProspectRefereeNameError,
		tvProspectRefereerMobileError,
		tvProspectRefereerEmailError;
	private boolean isIndividualsFlag;
	private String RefereeCode, RefereeName;
	private boolean isRefreeCode;
	private ProgressBar pbLoadingIndicatorRefreeCode, pbLoadingIndicatorRefreeName;
	private AdapterView.OnItemSelectedListener categoryInListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					categoryCodeStr = ((RowData) spCategory.getSelectedItem()).getKey();
					categoryNameStr = ((RowData) spCategory.getSelectedItem()).getValue();
					String sysName = ((RowData) spCategory.getSelectedItem()).getSchmeID();
					tvCategoryError.setVisibility(View.GONE);
					if (!sysName.equalsIgnoreCase("INDIVIDUAL")) {
						isIndividualsFlag = false;
						etContactPerson.setVisibility(View.VISIBLE);
					} else {
						isIndividualsFlag = true;
						etContactPerson.setVisibility(View.GONE);
					}

					ilName.setHint(categoryNameStr + " Name*");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

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

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener productTypeListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					productTypeCodeStr = ((RowData) spProductType.getSelectedItem()).getKey();
					tvProductTypeError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener interestInListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					interestInCodeStr = ((RowData) spInterestIn.getSelectedItem()).getKey();
					interestInNameStr = ((RowData) spInterestIn.getSelectedItem()).getValue();
					if (!interestInCodeStr.equalsIgnoreCase("-1")) {
						getProductType(interestInCodeStr);
						spProductType.setEnabled(true);
					} else {
						spProductType.setSelection(0, true);
						spProductType.setEnabled(false);
					}
					tvInterestInError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener prospectSourceListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					prospectSourceCodeStr = ((RowData) spProspectSource.getSelectedItem()).getKey();
					prospectSourceNameStr =
						((RowData) spProspectSource.getSelectedItem()).getValue();
					if (prospectSourceNameStr.equals(WEBSITE_CLIENT_REFERRAL) ||
						prospectSourceNameStr.equals(WEBSITE_PROSPECT_REFERRAL)) {
						ilRefereeCode.setHint("Referee Code*");
						openSectionExpansion(llAdditionInfo, tvAdditionalInfo);
					} else {
						ilRefereeCode.setHint("Referee Code");
						closedSectionExpansion(llAdditionInfo, tvAdditionalInfo);
						tvRefereeCodeError.setVisibility(View.GONE);
					}
					tvProspectSourceError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener sourceListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					sourceCodeStr = ((RowData) spSource.getSelectedItem()).getKey();
					getProspectSource(sourceCodeStr);
					tvSourceError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener statusListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					statusCodeStr = ((RowData) spStatus.getSelectedItem()).getKey();
					tvStatusError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener stageListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					stageCodeStr = ((RowData) spStage.getSelectedItem()).getKey();
					getStatus(stageCodeStr);
					tvStageError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener cityListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					cityCodeStr = ((RowData) spCity.getSelectedItem()).getKey();
					tvCityError.setVisibility(View.GONE);
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

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener offCityListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					offCityCodeStr = ((RowData) spOffCity.getSelectedItem()).getKey();
					tvOffCityError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

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
						new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item,
							arrCity);
					arrAdapCity.setDropDownViewResource(R.layout.mdropdown);
					spCity.setAdapter(arrAdapCity);
					if (!stateCodeStr.equalsIgnoreCase("-1")) {
						getCity(stateCodeStr, RESIDENCE, null);
					}
					tvStateError.setVisibility(View.GONE);
					if (cbSameAs.isChecked()) {
						RowData dataState =
							RowData.getSpinnerRowDataById(stateCodeStr, arrAdapState);
						if (dataState != null) {
							spOffState.setSelection(dataState.getPosition());
							getCity(((RowData) spOffState.getSelectedItem()).getKey(), OFFICE,
								null);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener offStateListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					offStateCodeStr = ((RowData) spOffState.getSelectedItem()).getKey();
					if (!offStateCodeStr.equalsIgnoreCase("-1")) {
						if (cbSameAs.isChecked()) {
							getCity(offStateCodeStr, OFFICE, cityCodeStr);
						} else {
							getCity(offStateCodeStr, OFFICE, null);
						}
					}
					tvOffStateError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener countryListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					countryCodeStr = ((RowData) spCountry.getSelectedItem()).getKey();
					if (!countryCodeStr.equalsIgnoreCase("-1")) {
						getState(countryCodeStr, RESIDENCE, null);
					}
					tvCountryError.setVisibility(View.GONE);
					if (cbSameAs.isChecked()) {
						RowData dataCountry =
							RowData.getSpinnerRowDataById(countryCodeStr, arrAdapCountry);
						if (dataCountry != null) {
							spOffCountry.setSelection(dataCountry.getPosition());
							getState(((RowData) spOffCountry.getSelectedItem()).getKey(), OFFICE,
								null);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener offCountryListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					offCountryCodeStr = ((RowData) spOffCountry.getSelectedItem()).getKey();
					if (!offCountryCodeStr.equalsIgnoreCase("-1")) {
						if (cbSameAs.isChecked()) {
							getState(offCountryCodeStr, OFFICE, stateCodeStr);
						} else {
							getState(offCountryCodeStr, OFFICE, null);
						}
					}
					tvOffCountryError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		};

	@Override protected int getLayout() {
		return R.layout.frag_lead_creation;
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		leadCreationView = super.onCreateView(inflater, container, savedInstanceState);
		container.post(() -> setAppTitle("Lead Creation"));
		setUpView(leadCreationView);
		setUserVisibleHint(true);
		return leadCreationView;
	}

	@Override public void setUserVisibleHint(boolean isVisibleToUser) {
		try {
			super.setUserVisibleHint(isVisibleToUser);
			if (isVisibleToUser && leadCreationView != null) {
				getCategory();
				getProfession();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("ClickableViewAccessibility") private void setUpView(View leadCreationView) {
		scrollView = leadCreationView.findViewById(R.id.scrollView);
		scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		scrollView.setFocusable(true);
		scrollView.setFocusableInTouchMode(true);
		scrollView.setOnTouchListener((v, event) -> {
			v.requestFocusFromTouch();
			return false;
		});
		llPersonalInformation =
			leadCreationView.findViewById(R.id.llPersonalInformation);
		llAdditionInfo = leadCreationView.findViewById(R.id.llAdditionInfo);

		tvPersonalInfo = leadCreationView.findViewById(R.id.tvPersonalInfo);
		tvAdditionalInfo = leadCreationView.findViewById(R.id.tvAdditionInfo);

		actAssignTo = leadCreationView.findViewById(R.id.actAssignTo);

		ilName = leadCreationView.findViewById(R.id.ilName);
		ilRefereeCode = leadCreationView.findViewById(R.id.ilRefereeCode);
		etName = leadCreationView.findViewById(R.id.etName);
		etProspectSourceDetails =
			leadCreationView.findViewById(R.id.etProspectSourceDetails);

		etContactPerson = leadCreationView.findViewById(R.id.etContactPerson);

		etEmailId = leadCreationView.findViewById(R.id.etEmailId);
		etAlternateEmailId = leadCreationView.findViewById(R.id.etAlternateEmailId);
		etAlternateMobile = leadCreationView.findViewById(R.id.etAlternateMobno);
		etMobile = leadCreationView.findViewById(R.id.etMobile);
		etEkycMobile = leadCreationView.findViewById(R.id.etEkycMobile);
		etPanNum = leadCreationView.findViewById(R.id.etPanNum);
		etAadharNum = leadCreationView.findViewById(R.id.etAadharNum);
		etLandLine = leadCreationView.findViewById(R.id.etLandLine);
		pbLoadingIndicatorRefreeCode =
			(ProgressBar) leadCreationView.findViewById(R.id.pbLoadingIndicatorRefreeCode);
		pbLoadingIndicatorRefreeName =
			(ProgressBar) leadCreationView.findViewById(R.id.pbLoadingIndicatorRefreeName);
		etRefreeCode = leadCreationView.findViewById(R.id.etRefereeCode);
		etRefreeName = leadCreationView.findViewById(R.id.etRefereeName);
		etRefreeEmail = leadCreationView.findViewById(R.id.etRefereeEmail);
		etRefreeMobile = leadCreationView.findViewById(R.id.etRefereeMobile);
		etProspectRefereeName = leadCreationView.findViewById(R.id.etProspectRefereeName);
		etProspectRefereerMobile = leadCreationView.findViewById(R.id.etProspectRefereerMobile);
		etProspectRefereerEmail = leadCreationView.findViewById(R.id.etProspectRefereerEmail);

		etAdd1 = leadCreationView.findViewById(R.id.etAdd1);
		etAdd1.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
		etAdd2 = leadCreationView.findViewById(R.id.etAdd2);
		etAdd2.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
		etAdd3 = leadCreationView.findViewById(R.id.etAdd3);
		etAdd3.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
		etPincode = leadCreationView.findViewById(R.id.etPincode);

		etOffAdd1 = leadCreationView.findViewById(R.id.etOffAdd1);
		etOffAdd1.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
		etOffAdd2 = leadCreationView.findViewById(R.id.etOffAdd2);
		etOffAdd2.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
		etOffAdd3 = leadCreationView.findViewById(R.id.etOffAdd3);
		etOffAdd3.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
		etOffPincode = leadCreationView.findViewById(R.id.etOffPincode);
		etComments = leadCreationView.findViewById(R.id.etComments);
		etComments.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });

		ilName = leadCreationView.findViewById(R.id.ilName);
		tvAlternateMobError = leadCreationView.findViewById(R.id.tvAlternateMobnoError);
		String[] strArr = getResources().getStringArray(R.array.Select);
		ArrayAdapter<String> arrBlankAdapter =
			new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, strArr);
		arrBlankAdapter.setDropDownViewResource(R.layout.mdropdown);
		spSource = leadCreationView.findViewById(R.id.spSource);
		spSource.setAdapter(arrBlankAdapter);
		spProspectSource = leadCreationView.findViewById(R.id.spProspectSource);
		spProspectSource.setAdapter(arrBlankAdapter);
		spStage = leadCreationView.findViewById(R.id.spStage);
		spStage.setAdapter(arrBlankAdapter);
		spStatus = leadCreationView.findViewById(R.id.spStatus);
		spStatus.setAdapter(arrBlankAdapter);
		spCategory = leadCreationView.findViewById(R.id.spCategory);
		spCategory.setAdapter(arrBlankAdapter);
		spInterestIn = leadCreationView.findViewById(R.id.spInterestIn);
		spInterestIn.setAdapter(arrBlankAdapter);

		spProductType = leadCreationView.findViewById(R.id.spProductType);
		spProductType.setAdapter(arrBlankAdapter);
		spProfession = leadCreationView.findViewById(R.id.spProfession);
		spProfession.setAdapter(arrBlankAdapter);

		spProductType.setEnabled(false);
		spCity = leadCreationView.findViewById(R.id.spCity);
		spCity.setAdapter(arrBlankAdapter);

		spState = leadCreationView.findViewById(R.id.spState);
		spState.setAdapter(arrBlankAdapter);

		spCountry = leadCreationView.findViewById(R.id.spCountry);
		spCountry.setAdapter(arrBlankAdapter);

		spOffCity = leadCreationView.findViewById(R.id.spOffCity);
		spOffCity.setAdapter(arrBlankAdapter);

		spOffState = leadCreationView.findViewById(R.id.spOffState);
		spOffState.setAdapter(arrBlankAdapter);

		spOffCountry = leadCreationView.findViewById(R.id.spOffCountry);
		spOffCountry.setAdapter(arrBlankAdapter);

		cbPrimaryAddress = leadCreationView.findViewById(R.id.cbPrimaryAddress);
		cbOffPrimaryAddress = leadCreationView.findViewById(R.id.cbOffPrimaryAddress);
		cbSameAs = leadCreationView.findViewById(R.id.cbSameAs);

		btnCreate = leadCreationView.findViewById(R.id.btnCreate);
		btnCancel = leadCreationView.findViewById(R.id.btnCancel);

		tvCategoryError = leadCreationView.findViewById(R.id.tvCategoryError);
		tvInterestInError = leadCreationView.findViewById(R.id.tvInterestInError);
		tvNameError = leadCreationView.findViewById(R.id.tvNameError);
		tvContactPersonError = leadCreationView.findViewById(R.id.tvContactPersonError);
		tvEmailIdError = leadCreationView.findViewById(R.id.tvEmailIdError);
		tvAlternateEmailIdError =
			leadCreationView.findViewById(R.id.tvAlternateEmailIdError);
		tvMobileError = leadCreationView.findViewById(R.id.tvMobileError);
		tvEkycMobileError = leadCreationView.findViewById(R.id.tvEkycMobileError);
		tvPanNumError = leadCreationView.findViewById(R.id.tvPanNumError);
		tvProductTypeError = leadCreationView.findViewById(R.id.tvProductTypeError);
		tvSourceError = leadCreationView.findViewById(R.id.tvSourceError);
		tvProspectSourceError =
			leadCreationView.findViewById(R.id.tvProspectSourceError);
		tvStageError = leadCreationView.findViewById(R.id.tvStageError);
		tvStatusError = leadCreationView.findViewById(R.id.tvStatusError);
		tvCountryError = leadCreationView.findViewById(R.id.tvCountryError);
		tvStateError = leadCreationView.findViewById(R.id.tvStateError);
		tvCityError = leadCreationView.findViewById(R.id.tvCityError);
		tvOffCountryError = leadCreationView.findViewById(R.id.tvOffCountryError);
		tvOffStateError = leadCreationView.findViewById(R.id.tvOffStateError);
		tvOffCityError = leadCreationView.findViewById(R.id.tvOffCityError);
		tvLandLineError = leadCreationView.findViewById(R.id.tvLandLineError);
		tvAadharNumError = leadCreationView.findViewById(R.id.tvAadharNumError);
		tvRefereeCodeError = leadCreationView.findViewById(R.id.tvRefereeCodeError);
		tvRefreeEmailError = leadCreationView.findViewById(R.id.tvRefreeEmailError);
		tvRefereeMobileError = leadCreationView.findViewById(R.id.tvRefereeMobileError);
		tvAdd1Error = leadCreationView.findViewById(R.id.tvAdd1Error);
		tvAdd2Error = leadCreationView.findViewById(R.id.tvAdd2Error);
		tvPincodeError = leadCreationView.findViewById(R.id.tvPincodeError);
		tvOffPincodeError = leadCreationView.findViewById(R.id.tvOffPincodeError);
		tvProspectRefereeNameError = leadCreationView.findViewById(R.id.tvProspectRefereeNameError);
		tvProspectRefereerMobileError =
			leadCreationView.findViewById(R.id.tvProspectRefereerMobileError);
		tvProspectRefereerEmailError =
			leadCreationView.findViewById(R.id.tvProspectRefereerEmailError);
		llResAdd = leadCreationView.findViewById(R.id.llResAdd);
		llOffAdd = leadCreationView.findViewById(R.id.llOffAdd);
		disableField(llResAdd);
		disableField(llOffAdd);
		cbPrimaryAddress.setOnCheckedChangeListener(this);
		cbOffPrimaryAddress.setOnCheckedChangeListener(this);
		tvPersonalInfo.setOnClickListener(this);
		tvAdditionalInfo.setOnClickListener(this);
		btnCreate.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		cbSameAs.setOnClickListener(this);

		setIcon(llPersonalInformation, tvPersonalInfo);
		setIcon(llAdditionInfo, tvAdditionalInfo);
		actAssignTo.setHint(getResources().getString(R.string.str_assing));
		actAssignTo.setListener(new ClearableEditText.Listener() {
			@Override public void didClearText() {
				primaryRM = null;
			}
		});
		actAssignTo.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				getActivity().getWindow()
					.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
				final CustomListPickerDialog searchList =
					new CustomListPickerDialog(requireContext());
				searchList.setThreshold(SEARCH_LENTH);

				searchList.show();
				searchList.setCustomTitle(getResources().getString(R.string.str_assing));
				CustomListPickerDialog.ListPickerListneable assingViewListner =
					new CustomListPickerDialog.ListPickerListneable() {
						@Override public void onListItemSelect(Parcelable data) {
							EmployeeModel clientModel = (EmployeeModel) data;
							if (clientModel.isNoData()) {
								return;
							}
							actAssignTo.setText(clientModel.getAttendeeName());
							primaryRM = clientModel.getAttendeeCode();
							searchList.dismiss();
							actAssignTo.setClearIconVisible(true);
						}

						@Override public void onSearchTextChanged(final CharSequence constraint) {
							searchUserBusinessProfile(constraint, searchList);
						}
					};
				searchList.setmListner(assingViewListner);
			}
		});
		etRefreeName.setFilters(new InputFilter[] { CRMAppUtil.filterOnlyAlphabetAndNumbers });
		etContactPerson.setFilters(new InputFilter[] { CRMAppUtil.filterOnlyAlphabetAndNumbers });
		etName.setFilters(new InputFilter[] { CRMAppUtil.filterOnlyAlphabetAndNumbers });
		etProspectRefereeName.setFilters(
			new InputFilter[] { CRMAppUtil.filterOnlyAlphabetAndNumbers });
		etProspectSourceDetails.setFilters(
			new InputFilter[] { CRMAppUtil.filterOnlyAlphabetAndNumbers });
		etRefreeCode.setFilters(new InputFilter[] {
			CRMAppUtil.filterOnlyAlphabetAndNumbers, new InputFilter.AllCaps()
		});

		etRefreeCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override public void onFocusChange(View view, boolean b) {
				if (etRefreeCode.getText().toString().length() < 4) {
					RefereeCode = "";
					RefereeName = "";
					etRefreeName.setText("");
					etRefreeEmail.setText("");
					etRefreeMobile.setText("");
				}
			}
		});
		etRefreeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override public void onFocusChange(View view, boolean b) {
				if (etRefreeName.getText().toString().length() < 3) {
					RefereeCode = "";
					RefereeName = "";
					etRefreeEmail.setText("");
					etRefreeMobile.setText("");
					etRefreeCode.setText("");
				}
			}
		});

		etPincode.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!isBlank(etPincode.getText().toString())) {
					if (etPincode.length() < 6) {
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

			@Override public void afterTextChanged(Editable s) {
				if (cbSameAs.isChecked()) {
					etOffPincode.setText(etPincode.getText().toString());
				}
			}
		});
		etOffPincode.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
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

			@Override public void afterTextChanged(Editable s) {

			}
		});

		etLandLine.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override public void afterTextChanged(Editable s) {

				if (!isBlank(etLandLine.getText().toString())) {
					if (!validateTel(etLandLine.getText().toString().trim())) {
						tvLandLineError.setVisibility(View.VISIBLE);
						tvLandLineError.setText("Invalid Tel number");
						etLandLine.requestFocus();
					} else {
						tvLandLineError.setVisibility(View.GONE);
					}
				} else {
					tvLandLineError.setVisibility(View.GONE);
				}
			}
		});

		etName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override public void afterTextChanged(Editable s) {
				if (!isBlank(etName.getText().toString().trim())) {
					tvNameError.setVisibility(View.GONE);

					//format check
					if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
						etName.getText().toString().trim())) {
						tvNameError.setText(ErrorMsgName);
						tvNameError.setVisibility(View.VISIBLE);
					} else {
						tvNameError.setVisibility(View.GONE);
					}
				} else {
					tvNameError.setVisibility(View.GONE);
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

		etContactPerson.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override public void afterTextChanged(Editable s) {
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

		etAdd1.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override public void afterTextChanged(Editable s) {
				if (!isBlank(etAdd1.getText().toString())) {

					tvAdd1Error.setVisibility(View.GONE);
				} else {
					tvAdd1Error.setVisibility(View.GONE);
					tvAdd1Error.setText("Enter address");
				}
				if (cbSameAs.isChecked()) {
					etOffAdd1.setText(etAdd1.getText().toString());
				}
			}
		});
		etAdd2.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override public void afterTextChanged(Editable s) {
				if (!isBlank(etAdd2.getText().toString())) {

					tvAdd2Error.setVisibility(View.GONE);
				} else {
					tvAdd2Error.setVisibility(View.GONE);
					tvAdd2Error.setText("Enter address");
				}
				if (cbSameAs.isChecked()) {
					etOffAdd2.setText(etAdd2.getText().toString());
				}
			}
		});
		etAdd3.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override public void afterTextChanged(Editable s) {
				if (cbSameAs.isChecked()) {
					etOffAdd3.setText(etAdd1.getText().toString());
				}
			}
		});

		etMobile.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (CRMValidationRule.validateMobile(etMobile.getText().toString())) {
					tvMobileError.setVisibility(View.GONE);
				}
			}

			@Override public void afterTextChanged(Editable s) {

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

		Observable<String> refreeNameObs = RxTextView.afterTextChangeEvents(etRefreeName)
			.filter(event -> event.getView().isFocused())
			.map(query -> query.getEditable().toString().trim())
			.debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
			.share();
		getCompositeDisposable().add(
			refreeNameObs.filter(query -> query.length() == 2).subscribe(ignored -> {
				etRefreeEmail.setText("");
				etRefreeMobile.setText("");
				etRefreeCode.setText("");
			}));
		getCompositeDisposable().add(
			refreeNameObs.filter(query -> query.length() > 2).switchMap(query -> {
				pbLoadingIndicatorRefreeName.setVisibility(View.VISIBLE);
				etRefreeEmail.setText("");
				etRefreeMobile.setText("");
				etRefreeCode.setText("");
				final Map inputMap = new HashMap<>();
				inputMap.put("SearchType", "ByName");
				inputMap.put("SearchHint", query);
				return RefreeSyncWebServiceCall.getObservableData(requireContext(),
					"Common/GetRefereeCode", inputMap, pbLoadingIndicatorRefreeName, userManager)
					.compose(applyUiModel());
			}).subscribe(data -> {
				UiModel<JSONObject> uiModel = (UiModel<JSONObject>) data;
				JSONObject data1 = uiModel.data;
				if (data1 != null) {
					RefreeData(data1, "ByName", pbLoadingIndicatorRefreeName);
				}
			}));

		Observable<String> refreeCodeObs = RxTextView.afterTextChangeEvents(etRefreeCode)
			.filter(event -> event.getView().isFocused())
			.map(query -> query.getEditable().toString().trim())
			.debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
			.share();

		getCompositeDisposable().add(
			refreeCodeObs.filter(query -> query.length() == 3).subscribe(ignored -> {
				etRefreeName.setText("");
				etRefreeMobile.setText("");
				etRefreeEmail.setText("");
			}));

		getCompositeDisposable().add(
			refreeCodeObs.filter(query -> query.length() > 3).switchMap(query -> {
				pbLoadingIndicatorRefreeCode.setVisibility(View.VISIBLE);
				etRefreeName.setText("");
				etRefreeMobile.setText("");
				etRefreeEmail.setText("");
				final Map inputMap = new HashMap<>();
				inputMap.put("SearchType", "ByCode");
				inputMap.put("SearchHint", query);
				return RefreeSyncWebServiceCall.getObservableData(requireContext(),
					"Common/GetRefereeCode", inputMap, pbLoadingIndicatorRefreeCode, userManager)
					.compose(applyUiModel());
			}).subscribe(data -> {
				UiModel<JSONObject> uiModel = (UiModel<JSONObject>) data;
				JSONObject data1 = uiModel.data;
				if (data1 != null) {
					RefreeData(data1, "ByCode", pbLoadingIndicatorRefreeCode);
				}
			}));

		etAlternateMobile.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!isBlank(etAlternateMobile.getText().toString())) {
					if (!CRMValidationRule.validateMobile(etAlternateMobile.getText().toString())) {
						tvAlternateMobError.setVisibility(View.VISIBLE);
						tvAlternateMobError.setText(INVALID_MOBILE_NO);
						etAlternateMobile.requestFocus();
					} else {
						tvAlternateMobError.setVisibility(View.GONE);
					}
				} else {
					tvAlternateMobError.setVisibility(View.VISIBLE);
				}
			}

			@Override public void afterTextChanged(Editable s) {

			}
		});
		etEkycMobile.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!isBlank(etEkycMobile.getText().toString())) {
					if (!CRMValidationRule.validateMobile(etEkycMobile.getText().toString())) {
						tvEkycMobileError.setVisibility(View.VISIBLE);
						tvEkycMobileError.setText(INVALID_MOBILE_NO);
						etEkycMobile.requestFocus();
					} else {
						tvEkycMobileError.setVisibility(View.GONE);
					}
				} else {
					tvEkycMobileError.setVisibility(View.VISIBLE);
				}
			}

			@Override public void afterTextChanged(Editable s) {

			}
		});
		etPanNum.setFilters(
			new InputFilter[] { new InputFilter.LengthFilter(10), new InputFilter.AllCaps() });
		etPanNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!isBlank(etPanNum.getText().toString())) {
					if (!CRMValidationRule.isValidPannumber((etPanNum.getText().toString()))) {
						tvPanNumError.setVisibility(View.VISIBLE);
						tvPanNumError.setText("Invalid PAN number");
						etPanNum.requestFocus();
					} else {
						tvPanNumError.setVisibility(View.GONE);
					}
				} else {
					tvPanNumError.setVisibility(View.GONE);
				}
			}

			@Override public void afterTextChanged(Editable s) {

			}
		});
		etAadharNum.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override public void afterTextChanged(Editable s) {
				if (!isBlank(etAadharNum.getText().toString())) {
					if (!validateAadhar(etAadharNum.getText().toString().trim())) {
						tvAadharNumError.setVisibility(View.VISIBLE);
						tvAadharNumError.setText("Invalid Aadhar number");
						etAadharNum.requestFocus();
					} else {
						tvAadharNumError.setVisibility(View.GONE);
					}
				} else {
					tvAadharNumError.setVisibility(View.GONE);
				}
			}
		});

		etEmailId.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!isBlank(etEmailId.getText().toString().trim())) {
					if (!isValidEmail(etEmailId.getText())) {
						tvEmailIdError.setVisibility(View.VISIBLE);
						tvEmailIdError.setText(INVALID_EMAIL_ID);
						etEmailId.requestFocus();
					} else {
						tvEmailIdError.setVisibility(View.GONE);
					}
				} else {
					tvEmailIdError.setVisibility(View.GONE);
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

		etAlternateEmailId.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!isBlank(etAlternateEmailId.getText().toString().trim())) {
					if (!isValidEmail(etAlternateEmailId.getText())) {
						tvAlternateEmailIdError.setVisibility(View.VISIBLE);
						tvAlternateEmailIdError.setText(INVALID_EMAIL_ID);
						etAlternateEmailId.requestFocus();
					} else {
						tvAlternateEmailIdError.setVisibility(View.GONE);
					}
				} else {
					tvAlternateEmailIdError.setVisibility(View.GONE);
				}
			}

			@Override public void afterTextChanged(Editable s) {

			}
		});

		etRefreeEmail.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!isBlank(etRefreeEmail.getText().toString().trim())) {
					if (!isValidEmail(etRefreeEmail.getText())) {
						tvRefreeEmailError.setVisibility(View.VISIBLE);
						tvRefreeEmailError.setText(INVALID_EMAIL_ID);
						etRefreeEmail.requestFocus();
					} else {
						tvRefreeEmailError.setVisibility(View.GONE);
					}
				} else {
					tvRefreeEmailError.setVisibility(View.GONE);
				}
			}

			@Override public void afterTextChanged(Editable s) {

			}
		});

		etRefreeMobile.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!isBlank(etRefreeMobile.getText().toString())) {
					if (!CRMValidationRule.validateMobile(etRefreeMobile.getText().toString())) {
						tvRefereeMobileError.setVisibility(View.VISIBLE);
						tvRefereeMobileError.setText(INVALID_MOBILE_NO);
						etRefreeMobile.requestFocus();
					} else {
						tvRefereeMobileError.setVisibility(View.GONE);
					}
				} else {
					tvRefereeMobileError.setVisibility(View.VISIBLE);
				}
			}

			@Override public void afterTextChanged(Editable s) {

			}
		});
	}

	void searchUserBusinessProfile(CharSequence constraint, CustomListPickerDialog searchList) {
		if (constraint == null) return;
		UserBusinessProfileRequest profileRequest;
		try {
			profileRequest = new UserBusinessProfileRequest(
				URLEncoder.encode(constraint.toString(), "utf-8"),
				URLEncoder.encode(constraint.toString(), "utf-8")
			);
		} catch (UnsupportedEncodingException e) {
			return;
		}
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			Disposable disposable = getSaathiApi().getUserBusinessProfile(profileRequest)
				.compose(applyUiModel())
				.subscribe(uiModel -> {
					if (uiModel.inProgress) {
						hideKeyboard(actAssignTo);
						searchList.showProgress(View.VISIBLE);
					} else {
						searchList.showProgress(View.GONE);
					}

					Throwable error = uiModel.error;
					if (error != null) {
						final ArrayList<EmployeeModel> modelList = new ArrayList<>();
						modelList.add(EmployeeModel.getNoData(getErrorOnFailureNew(error)));
						final AttendeeAdapter myAssineeAdapter =
							new AttendeeAdapter(requireContext(), modelList);
						searchList.setAdapter(myAssineeAdapter);
					}

					List<BusinessProfile> data = uiModel.data;
					if (data != null) {
						final ArrayList<EmployeeModel> modelList = new ArrayList<>();
						for (BusinessProfile datum : data) {
							if (userManager.getUser().employeeCode
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
						final AttendeeAdapter
							myAssineeAdapter = new AttendeeAdapter(requireContext(), modelList);
						searchList.setAdapter(myAssineeAdapter);
					}
				});
		} else {
			noInternetConnectionDialog();
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
				RefereeCode =
					(jsonObject.isNull("RefereeCode") || jsonObject.getString("RefereeCode")
						.equals("")) ? "" : jsonObject.getString("RefereeCode");
				RefereeName =
					(jsonObject.isNull("RefereeName") || jsonObject.getString("RefereeName")
						.equals("")) ? "" : jsonObject.getString("RefereeName");
				String RefereeEmail =
					(jsonObject.isNull("RefereeEmail") || jsonObject.getString("RefereeEmail")
						.equals("")) ? "" : jsonObject.getString("RefereeEmail");
				String RefereeMobile =
					(jsonObject.isNull("RefereeMobile") || jsonObject.getString("RefereeMobile")
						.equals("")) ? "" : jsonObject.getString("RefereeMobile");
				if (type.equalsIgnoreCase("BYCODE")) {
					etRefreeName.setText(RefereeName);
				} else {
					etRefreeCode.setText(RefereeCode);
				}
				etRefreeEmail.setText(RefereeEmail);
				etRefreeMobile.setText(RefereeMobile);
			} else {
				isRefreeCode = false;
				pbLoadingIndicatorRefreeCode.setVisibility(View.GONE);
				RefereeCode = "";
				RefereeName = "";
				etRefreeEmail.setText("");
				etRefreeMobile.setText("");
				if (type.equalsIgnoreCase("BYCODE")) {
					etRefreeName.setText("");
				} else {
					etRefreeCode.setText("");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void getCategory() {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId), LEADCATEGORY,
					null, null);
			Disposable disposable = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(this::renderLeadCategories);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderLeadCategories(UiModel<List<LookupItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			arrCategory.clear();
			showOnFailurePopup(error);
		}

		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrCategory.clear();
			arrCategory.add(new RowData(0, "-1", "Select"));

			for (int i = 0; i < data.size(); i++) {
				LookupItem lookupItem = data.get(i);
				arrCategory.add(
					new RowData(i + 1,
						String.valueOf(lookupItem.lookUpid),
						stringOrEmpty(lookupItem.lookUpName),
						stringOrEmpty(lookupItem.sysName)
					));
			}
			arrAdapCategory = new ArrayAdapter<>(requireContext(),
				R.layout.simple_spinner_item, arrCategory);
			arrAdapCategory.setDropDownViewResource(R.layout.mdropdown);
			spCategory.setAdapter(arrAdapCategory);
			spCategory.setOnItemSelectedListener(categoryInListener);
			getInterestIn();
		}
	}

	private void getProfession() {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId), PROFESSIONID,
					null, null);
			Disposable disposable = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(this::renderProfession);
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
				new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrProfession);
			arrAdapProfession.setDropDownViewResource(R.layout.mdropdown);
			spProfession.setAdapter(arrAdapProfession);
			spProfession.setOnItemSelectedListener(professionListener);
		}
	}

	private void getInterestIn() {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId), INTERESTEDIN,
					null, null);
			Disposable disposable = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(this::renderInterests);
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
				new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrInterested);
			arrAdapInterested.setDropDownViewResource(R.layout.mdropdown);
			spInterestIn.setAdapter(arrAdapInterested);
			spInterestIn.setOnItemSelectedListener(interestInListener);

			getSourceList();
		}
	}

	private void getProductType(String productId) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			LookupRequest lookupRequest =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId), PRODUCTTYPE,
					productId, null);
			Disposable disposable = getSaathiApi().getAllLookup(lookupRequest)
				.compose(applyUiModel())
				.subscribe(this::renderProductTypes);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderProductTypes(UiModel<List<LookupItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			arrProductType.clear();
			showOnFailurePopup(error);
		}

		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrProductType.clear();
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
				new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrProductType);
			arrAdapProductType.setDropDownViewResource(R.layout.mdropdown);
			spProductType.setAdapter(arrAdapProductType);
			spProductType.setOnItemSelectedListener(productTypeListener);
		}
	}

	private void getSourceList() {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			Disposable disposable =
				getSaathiApi().getSourceList(
					new SourceListRequest(Objects.requireNonNull(
						Objects.requireNonNull(
							userManager.getUser()).currentBusinessProfile.businessUnitId)))
					.compose(applyUiModel())
					.subscribe(this::renderSourceList);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderSourceList(UiModel<List<SourceListItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			arrSource.clear();
			showOnFailurePopup(error);
		}

		List<SourceListItem> data = uiModel.data;
		if (data != null) {
			//TODO BUG with RowData position @lalkishor
			for (int i = 0; i < data.size(); i++) {
				SourceListItem listItem = data.get(i);
				arrSource.add(new RowData(i + 1,
					String.valueOf(listItem.sourceID),
					stringOrEmpty(listItem.sourceName)
				));
			}

			arrAdapSource =
				new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrSource);
			arrAdapSource.setDropDownViewResource(R.layout.mdropdown);
			spSource.setAdapter(arrAdapSource);
			spSource.setOnItemSelectedListener(sourceListener);

			getStage();
		}
	}

	private void getProspectSource(String productId) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			Disposable disposable =
				getSaathiApi().getLeadSourceList(new LeadSourceListRequest(productId))
					.compose(applyUiModel())
					.subscribe(this::renderLeadSourceList);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderLeadSourceList(UiModel<List<LeadSourceItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			arrProspectSource.clear();
			showOnFailurePopup(error);
		}

		List<LeadSourceItem> data = uiModel.data;
		if (data != null) {
			arrProspectSource.clear();

			for (int i = 0; i < data.size(); i++) {
				LeadSourceItem listItem = data.get(i);
				arrProspectSource.add(
					new RowData(i + 1,
						stringOrEmpty(listItem.leadSourceID),
						stringOrEmpty(listItem.leadSourceName)
					));
			}
			arrAdapProspectSource =
				new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item,
					arrProspectSource);
			arrAdapProspectSource.setDropDownViewResource(R.layout.mdropdown);
			spProspectSource.setAdapter(arrAdapProspectSource);
			spProspectSource.setOnItemSelectedListener(prospectSourceListener);
		}
	}

	private void getStage() {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			Disposable disposable = getSaathiApi().getStageList(
				new StageRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId)))
				.compose(applyUiModel())
				.subscribe(this::renderStage);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderStage(UiModel<List<StageItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			arrStage.clear();
			showOnFailurePopup(error);
		}

		List<StageItem> data = uiModel.data;
		if (data != null) {
			arrStage.clear();
			for (int i = 0; i < data.size(); i++) {
				StageItem stageItem = data.get(i);
				arrStage.add(new RowData(i + 1,
					String.valueOf(stageItem.stageID),
					stageItem.stageName
				));
			}

			arrAdapStage = new ArrayAdapter<>(requireContext(),
				R.layout.simple_spinner_item, arrStage);
			arrAdapStage.setDropDownViewResource(R.layout.mdropdown);
			spStage.setAdapter(arrAdapStage);
			spStage.setOnItemSelectedListener(stageListener);

			getCountry();
		}
	}

	private void getStatus(String productId) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			Disposable disposable = getSaathiApi().getStatusList(new StatusRequest(productId, "1"))
				.compose(applyUiModel())
				.subscribe(this::renderStatus);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderStatus(UiModel<List<StatusItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			arrStatus.clear();
			showOnFailurePopup(error);
		}

		List<StatusItem> data = uiModel.data;
		if (data != null) {
			arrStatus.clear();

			for (int i = 0; i < data.size(); i++) {
				StatusItem item = data.get(i);
				arrStatus.add(new RowData(i + 1,
					String.valueOf(item.statusID),
					stringOrEmpty(item.statusName)
				));
			}

			arrAdapStatus =
				new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrStatus);
			arrAdapStatus.setDropDownViewResource(R.layout.mdropdown);
			spStatus.setAdapter(arrAdapStatus);
			spStatus.setOnItemSelectedListener(statusListener);
		}
	}

	private void getCountry() {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			Disposable subscribe = getSaathiApi().getCountries()
				.compose(applyUiModel())
				.subscribe(this::renderCountryList);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderCountryList(UiModel<List<CountryItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			arrCountry.clear();
			showOnFailurePopup(error);
		}

		List<CountryItem> data = uiModel.data;
		if (data != null) {
			arrCountry.clear();
			for (int i = 0; i < data.size(); i++) {
				CountryItem countryItem = data.get(i);
				String countryId = stringOrEmpty(countryItem.countryId);
				String countryName = stringOrEmpty(countryItem.countryName);
				arrCountry.add(new RowData(i, countryId, countryName));
			}
			arrAdapCountry = new ArrayAdapter<>(requireContext(),
				R.layout.simple_spinner_item, arrCountry);
			arrAdapCountry.setDropDownViewResource(R.layout.mdropdown);
			spCountry.setAdapter(arrAdapCountry);
			spCountry.setOnItemSelectedListener(countryListener);
			spOffCountry.setAdapter(arrAdapCountry);
			spOffCountry.setOnItemSelectedListener(offCountryListener);
		}
	}

	private void getState(String id, final String type, final String value) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			Disposable disposable = getSaathiApi().getStateProvinces(new StateProvinceRequest(id))
				.compose(applyUiModel())
				.subscribe(uiModel -> renderStates(uiModel, type, value));
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderStates(UiModel<List<StateProvinceItem>> uiModel, final String type,
		final String value) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			arrState.clear();
			arrOffState.clear();
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
				if (type.equals(RESIDENCE)) {
					arrState.add(
						new RowData(i + 1, stateId, stateName));
				} else if (type.equals(OFFICE)) {
					arrOffState.add(
						new RowData(i + 1, stateId, stateName));
				}
			}

			if (type.equals(RESIDENCE)) {
				arrAdapState =
					new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrState);
				arrAdapState.setDropDownViewResource(R.layout.mdropdown);
				spState.setAdapter(arrAdapState);
				spState.setOnItemSelectedListener(stateListener);
			} else if (type.equals(OFFICE)) {
				arrAdapOffState =
					new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item,
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

	private void getCity(String id, final String type, final String value) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			Disposable disposable = getSaathiApi().getCityList(new CityListRequest(id))
				.compose(applyUiModel())
				.subscribe(uiModel -> renderCities(uiModel, type, value));
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderCities(UiModel<List<CityItem>> uiModel, String type, String value) {
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
				if (type.equals(RESIDENCE)) {
					arrCity.add(new RowData(i + 1, cityId, cityName));
				} else if (type.equals(OFFICE)) {
					arrOffCity.add(new RowData(i + 1, cityId, cityName));
				}
			}

			if (type.equals(RESIDENCE)) {
				arrAdapCity =
					new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrCity);
				arrAdapCity.setDropDownViewResource(R.layout.mdropdown);
				spCity.setAdapter(arrAdapCity);
				spCity.setOnItemSelectedListener(cityListener);
			} else if (type.equals(OFFICE)) {
				arrAdapOffCity =
					new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrOffCity);
				arrAdapOffCity.setDropDownViewResource(R.layout.mdropdown);
				spOffCity.setAdapter(arrAdapOffCity);
				RowData rowData = RowData.getSpinnerRowDataById(value, arrAdapOffCity);
				if (rowData != null) {
					spOffCity.setSelection(rowData.getPosition());
				}
				spOffCity.setOnItemSelectedListener(offCityListener);
			}
		}
	}

	private void updateLead(LeadCreationModel model) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			List<CreateLeadAddressDetails> leadAddresses = new ArrayList<>();
			List<AddressDetailModel> addressDetails = model.getContact().getAddress();
			for (AddressDetailModel detailModel : addressDetails) {
				leadAddresses.add(new CreateLeadAddressDetails(
					detailModel.getAddressTypeID(),
					detailModel.getAddress1(),
					detailModel.getAddress2(),
					valueOrNull(detailModel.getAddress3()),
					detailModel.getStateID(),
					detailModel.getCityID(),
					detailModel.getCountryID(),
					detailModel.getPincode(),
					detailModel.isPrimary(),
					detailModel.getBusinessUnitID()
				));
			}
			LeadCreationContact contact = model.getContact();
			CreateLeadContact createLeadContact = new CreateLeadContact(
				contact.getName(),
				contact.geteMail(),
				contact.getAlternateEmail(),
				contact.getAlternateMobileNo(),
				contact.getMobileNo(),
				contact.getLandLineNo(),
				contact.getEkycMobileNo(),
				contact.getPanNo(),
				contact.getAadharNo(),
				contact.isSameAsAbove(),
				leadAddresses
			);

			LeadCreationProductMapping productMapping = model.getProductMapping();
			List<CreateLeadProduct> leadProducts = Arrays.asList(new CreateLeadProduct(
				productMapping.getInterestedIn(),
				valueOrNull(productMapping.getProductID()),
				valueOrNull(productMapping.getPrimaryRM()),
				valueOrNull(productMapping.getClientCode()),
				valueOrNull(productMapping.getAssignedBy()),
				valueOrNull(productMapping.getAssignedByDate()),
				productMapping.getStageID(),
				productMapping.getStatusID()
			));

			CreateLeadRequest createLeadRequest = new CreateLeadRequest(
				model.getBusinessUnitID(), model.getLeadCategoryID(),
				valueOrNull(model.getContactPerson()),
				model.getSourceID(),
				model.getLeadSourceDetails(), model.getLeadSourceID(),
				model.getLeadSourceDetailsID(), model.getProfessionid(),
				valueOrNull(model.getRefereeCode()),
				valueOrNull(model.getRefereeName()), valueOrNull(model.getRefereeEmail()),
				valueOrNull(model.getRefereeMobile()),
				model.getComments(), valueOrNull(model.getCreatedBy()), model.getCreatedThrough(),
				model.getProspectRefereName(),
				model.getProspectRefereMobile(),
				model.getProspectRefereEmail(),
				model.getCreationDate(),
				createLeadContact, leadProducts
			);
			Disposable disposable = getSaathiApi().addUpdateLead(createLeadRequest)
				.compose(applyUiModel())
				.subscribe(this::renderUpdateLead);
		} else {
			noInternetConnectionDialog();
		}
	}

	@Nullable
	String valueOrNull(@Nullable String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		} else {
			return value;
		}
	}

	void renderUpdateLead(UiModel<SaathiCommonResponse<CreateLeadResponse>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}

		SaathiCommonResponse<CreateLeadResponse> data = uiModel.data;
		if (data != null) {
			Context context = requireContext();
			CreateLeadResponse response = data.response;
			if (data.status && response != null) {
				MSFDialog.alertDialog(context, 0,
					getString(R.string.motilal_oswal),
					"Lead Created Successfully with lead Id " + response.leadID,
					"OK", true, (action, data1) -> {
						if (action == MSFDialog.Action.OK) {
							//dashbordFragment();
							navigator.openHome();
						}
					});
			} else if (response != null) {
				DedupeModelResponse dedupeModel = response.dedupeModel;
				if (dedupeModel != null && data.errors != null) {
					dedupeDialog(dedupeModel);
				} else if (data.errors != null) {
					String errorMsg = data.errors.get(0).message;
					alertDialog(context, 0,
						getString(R.string.motilal_oswal),
						errorMsg, "OK", true, null);
				}
			}
		}
	}

	private boolean validateLead() {
		if (spCategory.getSelectedItem().toString().equalsIgnoreCase("Select")) {
			tvCategoryError.setVisibility(View.VISIBLE);
			tvCategoryError.setText("Select Category");
			tvCategoryError.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (isBlank(etName.getText().toString().trim())) {
			tvNameError.setText("Enter " + categoryNameStr + " name");
			tvNameError.setVisibility(View.VISIBLE);
			tvNameError.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
			etName.getText().toString().trim())) {
			tvNameError.setText(ErrorMsgName);
			tvNameError.setVisibility(View.VISIBLE);
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isIndividualsFlag && isBlank(etContactPerson.getText().toString().trim())) {
			tvContactPersonError.setText("Enter contact person");
			tvContactPersonError.setVisibility(View.VISIBLE);
			tvContactPersonError.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isIndividualsFlag
			&& !CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
			etContactPerson.getText().toString().trim())) {
			tvContactPersonError.setText(ErrorMsgName);
			tvContactPersonError.setVisibility(View.VISIBLE);
			tvContactPersonError.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (spInterestIn.getSelectedItem().toString().equalsIgnoreCase("Select")) {
			tvInterestInError.setVisibility(View.VISIBLE);
			tvInterestInError.setText("Select Interest In");
			tvInterestInError.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (interestInNameStr.equalsIgnoreCase("PMS") && spProductType.getSelectedItem()
			.toString()
			.equalsIgnoreCase("Select")) {
			tvProductTypeError.setVisibility(View.VISIBLE);
			tvProductTypeError.setText("Select ProductType");
			tvProductTypeError.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etEmailId.getText().toString().trim()) && !isValidEmail(
			etEmailId.getText().toString().trim())) {
			tvEmailIdError.setText(INVALID_EMAIL_ID);
			tvEmailIdError.setVisibility(View.VISIBLE);
			etEmailId.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etAlternateEmailId.getText().toString()) && !isValidEmail(
			etAlternateEmailId.getText().toString().trim())) {
			tvAlternateEmailIdError.setText(INVALID_EMAIL_ID);
			tvAlternateEmailIdError.setVisibility(View.VISIBLE);
			etAlternateEmailId.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etAlternateEmailId.getText().toString()) && etEmailId.getText()
			.toString()
			.equalsIgnoreCase(etAlternateEmailId.getText().toString())) {
			tvAlternateEmailIdError.setText(SAME_EMAILMESSAGE);
			tvAlternateEmailIdError.setVisibility(View.VISIBLE);
			etAlternateEmailId.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (isBlank(etMobile.getText().toString().trim())) {
			tvMobileError.setText(ENTER_MOBILE_NUMBER);
			tvMobileError.setVisibility(View.VISIBLE);
			etMobile.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (etMobile.length() < 10 && etMobile.length() != 0) {
			tvMobileError.setText(INVALID_MOBILE_NO);
			tvMobileError.setVisibility(View.VISIBLE);
			etMobile.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!CRMValidationRule.validateMobile(etMobile.getText().toString())) {
			etMobile.setEnabled(true);
			etMobile.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etAlternateMobile.getText().toString())
			&& !CRMValidationRule.validateMobile(etAlternateMobile.getText().toString())) {
			tvAlternateMobError.setText(INVALID_MOBILE_NO);
			etAlternateMobile.requestFocus();
			tvAlternateMobError.setVisibility(View.VISIBLE);
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etAlternateMobile.getText().toString()) && etMobile.getText()
			.toString()
			.equalsIgnoreCase(etAlternateMobile.getText().toString())) {
			tvAlternateMobError.setText(SAME_MOBILEMESSAGE);
			etAlternateMobile.requestFocus();
			tvAlternateMobError.setVisibility(View.VISIBLE);
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etLandLine.getText().toString()) && !validateTel(
			etLandLine.getText().toString().trim())) {
			tvLandLineError.setText("Invalid Tel number");
			etLandLine.requestFocus();
			tvLandLineError.setVisibility(View.VISIBLE);
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (etEkycMobile.length() < 10 && etEkycMobile.length() != 0) {
			tvEkycMobileError.setText(INVALID_MOBILE_NO);
			tvEkycMobileError.setVisibility(View.VISIBLE);
			etEkycMobile.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etEkycMobile.getText().toString()) && !CRMValidationRule.validateMobile(
			etEkycMobile.getText().toString())) {
			etEkycMobile.setEnabled(true);
			etEkycMobile.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etPanNum.getText().toString()) && !CRMValidationRule.isValidPannumber(
			etPanNum.getText().toString())) {
			etPanNum.setEnabled(true);
			etPanNum.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etAadharNum.getText().toString()) && !validateAadhar(
			etAadharNum.getText().toString().trim())) {
			tvAadharNumError.setText("Invalid Aadhar number");
			etAadharNum.requestFocus();
			tvAadharNumError.setVisibility(View.VISIBLE);
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (prospectSourceNameStr.equalsIgnoreCase(WEBSITE_CLIENT_REFERRAL)
			&& etRefreeCode.length() < 4) {
			tvRefereeCodeError.setText("Enter referee code ");
			tvRefereeCodeError.setVisibility(View.VISIBLE);
			etRefreeCode.requestFocus();
			return false;
		} else if (!isBlank(etProspectRefereeName.getText().toString().trim()) &&
			!CRMAppUtil.validateOnlyAlphabetAndNumberWithoutSplChar(
				etProspectRefereeName.getText().toString().trim())) {
			tvProspectRefereeNameError.setText(ErrorMsgName);
			tvProspectRefereeNameError.setVisibility(View.VISIBLE);
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (prospectSourceNameStr.equalsIgnoreCase(WEBSITE_PROSPECT_REFERRAL)
			&& isBlank(etProspectRefereeName.getText().toString().trim())) {
			tvProspectRefereeNameError.setText(ENTER_PROSPECT_REFEREE_NAME);
			tvProspectRefereeNameError.setVisibility(View.VISIBLE);
			etProspectRefereeName.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etProspectRefereerMobile.getText().toString())
			&& !CRMValidationRule.validateMobile(
			etProspectRefereerMobile.getText().toString().trim())) {
			tvProspectRefereerMobileError.setText(INVALID_MOBILE_NO);
			tvProspectRefereerMobileError.setVisibility(View.VISIBLE);
			etProspectRefereerMobile.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (prospectSourceNameStr.equalsIgnoreCase(WEBSITE_PROSPECT_REFERRAL)
			&& isBlank(etProspectRefereerMobile.getText().toString())) {
			tvProspectRefereerMobileError.setText(ENTER_MOBILE_NUMBER);
			tvProspectRefereerMobileError.setVisibility(View.VISIBLE);
			etProspectRefereerMobile.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etProspectRefereerEmail.getText().toString()) && !isValidEmail(
			etProspectRefereerEmail.getText().toString().trim())) {
			tvProspectRefereerEmailError.setText(INVALID_EMAIL_ID);
			tvProspectRefereerEmailError.setVisibility(View.VISIBLE);
			etProspectRefereerEmail.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (prospectSourceNameStr.equalsIgnoreCase(WEBSITE_PROSPECT_REFERRAL)
			&& isBlank(etProspectRefereerEmail.getText().toString())) {
			tvProspectRefereerEmailError.setText(ENTER_EMAIL_ID);
			tvProspectRefereerEmailError.setVisibility(View.VISIBLE);
			etProspectRefereerEmail.requestFocus();
			openSectionExpansion(llPersonalInformation, tvPersonalInfo);
			return false;
		} else if (!isBlank(etPincode.getText().toString())
			&& etPincode.length() < 6
			&& etPincode.length() != 0) {
			tvPincodeError.setVisibility(View.VISIBLE);
			tvPincodeError.setText("Invalid pincode");
			tvPincodeError.requestFocus();
			return false;
		} else if (!isBlank(etOffPincode.getText().toString())
			&& etOffPincode.length() < 6
			&& etOffPincode.length() != 0) {
			tvOffPincodeError.setVisibility(View.VISIBLE);
			tvOffPincodeError.setText("Invalid pincode");
			etOffPincode.requestFocus();
			return false;
		}
		return true;
	}

	@Override public void onClick(View view) {
		try {
			Bundle args = new Bundle();
			switch (view.getId()) {
				case R.id.tvPersonalInfo:
					toggleSectionExpansion(llPersonalInformation, tvPersonalInfo);
					break;
				case R.id.tvAdditionInfo:
					toggleSectionExpansion(llAdditionInfo, tvAdditionalInfo);
					break;
				case R.id.btnCreate:
					if (validateLead()) {
						MSFDialog.alertDialog(requireContext(), 0,
							getResources().getString(R.string.motilal_oswal), CONFIRM_LEAD, "YES",
							"NO", true, new MSFDialog.DialogListener() {
								@Override public void alertDialogAction(MSFDialog.Action action,
									Object... data) {
									if (action == MSFDialog.Action.OK) {
										createLead();
									}
								}
							});
					}
					break;
				case R.id.btnCancel:
					//dashbordFragment();
					navigator.openHome();
					break;
				case R.id.cbSameAs:
					if (cbSameAs.isChecked()) {
						disableField(llOffAdd);
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
					} else {
						enableField(llOffAdd);
						unCheckedSameasAbove();
					}
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void createLead() {
		residenceAddress =
			new AddressDetailModel(RESIDENCEADDRESS, etAdd1.getText().toString().trim(),
				etAdd2.getText().toString().trim(), etAdd3.getText().toString().trim(),
				countryCodeStr, stateCodeStr, cityCodeStr, etPincode.getText().toString().trim(),
				cbPrimaryAddress.isChecked(), Objects.requireNonNull(
				Objects.requireNonNull(
					userManager.getUser()).currentBusinessProfile.businessUnitId));
		officeAddress = new AddressDetailModel(OFFICEADDRESS, etOffAdd1.getText().toString().trim(),
			etOffAdd2.getText().toString().trim(), etOffAdd3.getText().toString().trim(),
			offCountryCodeStr, offStateCodeStr, offCityCodeStr,
			etOffPincode.getText().toString().trim(), cbOffPrimaryAddress.isChecked(),
			Objects.requireNonNull(
				Objects.requireNonNull(
					userManager.getUser()).currentBusinessProfile.businessUnitId));

		LeadCreationModel leadCreationModel = new LeadCreationModel();
		leadCreationModel.seteMail(etEmailId.getText().toString().trim());
		leadCreationModel.setSameAsAbove(cbSameAs.isChecked());
		leadCreationModel.setAlternateEmail(etAlternateEmailId.getText().toString().trim());
		leadCreationModel.setMobileNo(etMobile.getText().toString().trim());
		leadCreationModel.setLandLineNo(etLandLine.getText().toString().trim());
		leadCreationModel.setEkycMobileNo(etEkycMobile.getText().toString().trim());
		leadCreationModel.setPanNo(etPanNum.getText().toString().trim());
		leadCreationModel.setAadharNo(etAadharNum.getText().toString().trim());
		leadCreationModel.setBusinessUnitID(Objects.requireNonNull(
			Objects.requireNonNull(userManager.getUser()).currentBusinessProfile.businessUnitId));
		leadCreationModel.setLeadCategoryID(categoryCodeStr);
		leadCreationModel.setContactPerson(etContactPerson.getText().toString().trim());
		leadCreationModel.setSourceID(sourceCodeStr);
		leadCreationModel.setLeadSourceID(prospectSourceCodeStr);
		leadCreationModel.setLeadSourceDetailsID("0");
		leadCreationModel.setName(etName.getText().toString().trim());
		leadCreationModel.setLeadSourceDetails(etProspectSourceDetails.getText().toString().trim());
		if (etRefreeCode.getText().toString().trim().length() >= 4 && isRefreeCode) {
			leadCreationModel.setRefereeCode(etRefreeCode.getText().toString().trim());
		}
		if (etRefreeName.getText().toString().trim().length() >= 3 && isRefreeCode) {
			leadCreationModel.setRefereeName(etRefreeName.getText().toString().trim());
		}
		leadCreationModel.setAlternateMobileNo(etAlternateMobile.getText().toString().trim());
		leadCreationModel.setRefereeEmail(etRefreeEmail.getText().toString().trim());
		leadCreationModel.setRefereeMobile(etRefreeMobile.getText().toString().trim());
		leadCreationModel.setProspectRefereName(etProspectRefereeName.getText().toString().trim());
		leadCreationModel.setProspectRefereMobile(
			etProspectRefereerMobile.getText().toString().trim());
		leadCreationModel.setProspectRefereEmail(
			etProspectRefereerEmail.getText().toString().trim());
		leadCreationModel.setComments(getJsonSafeString(etComments.getText().toString().trim()));
		leadCreationModel.setProfileID(
			userManager.getUser().currentBusinessProfile.businessProfileId);
		leadCreationModel.setCreatedBy(userManager.getUser().employeeCode);
		leadCreationModel.setCreatedThrough("SelfCreated");
		leadCreationModel.setCreationDate(CRMAppUtil.getDBDate());
		leadCreationModel.setContactDetailModels(contactDetailList);
		leadCreationModel.setAddressDetailModels(addressDetailModels);
		leadCreationModel.setProductDetailModels(productDetailList);
		leadCreationModel.setAddresses(officeAddress, residenceAddress);
		leadCreationModel.setInterestedIn(interestInCodeStr);
		leadCreationModel.setProfessionid(professionCodeStr);
		leadCreationModel.setProductID(productTypeCodeStr);
		if (primaryRM != null) {
			leadCreationModel.setPrimaryRM(primaryRM);
		} else {
			leadCreationModel.setPrimaryRM(userManager.getUser().employeeCode);
		}
		leadCreationModel.setClientCode(null);
		leadCreationModel.setAssignedBy(userManager.getUser().employeeCode);
		leadCreationModel.setAssignedByDate(CRMAppUtil.getDBDate());
		leadCreationModel.setStageID(stageCodeStr);
		leadCreationModel.setStatusID(statusCodeStr);
		updateLead(leadCreationModel);
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

	@Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
		int id = compoundButton.getId();
		if (id == R.id.cbPrimaryAddress) {
			if (isChecked) {
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
		} else if (id == R.id.cbOffPrimaryAddress) {
			if (isChecked) {
				enableField(llResAdd);
				enableField(llOffAdd);
			} else {
				disableField(llResAdd);
				disableField(llOffAdd);
			}
		}
	}

	private void dedupeDialog(DedupeModelResponse dedupeModel) {
		final String leadId = stringOrEmpty(dedupeModel.leadID);
		final String CheckRemark = stringOrEmpty(dedupeModel.checkRemark);
		final String Stage = stringOrEmpty(dedupeModel.stage);
		final String BranchCode = stringOrEmpty(dedupeModel.branchCode);
		final String DedupeWith = stringOrEmpty(dedupeModel.dedupeWith);
		final String ClientName = stringOrEmpty(dedupeModel.clientName);
		final String ClientCode = stringOrEmpty(dedupeModel.clientCode);
		final String ProductName = stringOrEmpty(dedupeModel.productName);
		final String LeadName = stringOrEmpty(dedupeModel.leadName);
		final String IsTransfer = stringOrEmpty(dedupeModel.isTransfer);

		final Dialog dialog = new Dialog(requireContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(R.layout.dialog_dedupe);
		dialog.getWindow()
			.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LinearLayout llLeadID = dialog.findViewById(R.id.llLeadID);
		LinearLayout llLeadName = dialog.findViewById(R.id.llLeadName);
		LinearLayout llProduct = dialog.findViewById(R.id.llProduct);
		LinearLayout llStage = dialog.findViewById(R.id.llStage);
		LinearLayout llClientName = dialog.findViewById(R.id.llClientName);
		LinearLayout llClientCode = dialog.findViewById(R.id.llClientCode);
		LinearLayout llBranch = dialog.findViewById(R.id.llBranch);
		LinearLayout llDedupeWith = dialog.findViewById(R.id.llDedupeWith);

		if (DedupeWith.equalsIgnoreCase("Lead")) {
			llLeadID.setVisibility(View.VISIBLE);
			llStage.setVisibility(View.VISIBLE);
			llProduct.setVisibility(View.VISIBLE);
			llLeadName.setVisibility(View.VISIBLE);
			llClientName.setVisibility(View.GONE);
			llClientCode.setVisibility(View.GONE);
		} else if (DedupeWith.equalsIgnoreCase("Prospect")) {
			llLeadID.setVisibility(View.GONE);
			llStage.setVisibility(View.GONE);
			llProduct.setVisibility(View.GONE);
			llClientName.setVisibility(View.GONE);
			llLeadName.setVisibility(View.GONE);
			llClientCode.setVisibility(View.GONE);
		} else if (DedupeWith.equalsIgnoreCase("Client")) {
			llClientName.setVisibility(View.VISIBLE);
			llClientCode.setVisibility(View.VISIBLE);
			llLeadID.setVisibility(View.GONE);
			llStage.setVisibility(View.GONE);
			llProduct.setVisibility(View.GONE);
			llLeadName.setVisibility(View.GONE);
		}
		TextView tvCheckMark = dialog.findViewById(R.id.tvCheckMark);
		TextView tvLeadId = dialog.findViewById(R.id.tvLeadId);
		TextView tvLeadName = dialog.findViewById(R.id.tvLeadName);
		TextView tvProduct = dialog.findViewById(R.id.tvProduct);
		TextView tvStage = dialog.findViewById(R.id.tvStage);
		TextView tvClientName = dialog.findViewById(R.id.tvClientName);
		TextView tvClientCode = dialog.findViewById(R.id.tvClientCode);
		TextView tvBranch = dialog.findViewById(R.id.tvBranch);
		TextView tvDedupeWith = dialog.findViewById(R.id.tvDedupeWith);
		tvLeadName.setText(LeadName);
		tvClientName.setText(ClientName);
		tvClientCode.setText(ClientCode);
		tvProduct.setText(ProductName);
		tvCheckMark.setText(CheckRemark);
		tvLeadId.setText(leadId);
		tvStage.setText(Stage);
		tvDedupeWith.setText(DedupeWith);
		tvBranch.setText(BranchCode);
		Button btnTransfer = dialog.findViewById(R.id.btnTransfer);
		Button btnClose = dialog.findViewById(R.id.btnClose);
		if (IsTransfer.equalsIgnoreCase("1")) {
			btnTransfer.setVisibility(View.VISIBLE);
		} else {
			btnTransfer.setVisibility(View.GONE);
		}
		btnTransfer.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				transferLead(leadId);
				dialog.dismiss();
			}
		});
		btnClose.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				navigator.openPage(NAVIGATE_TO_LEAD_CREATION, new Bundle(), true, false);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void transferLead(final String leadId) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			TransferLeadRequest transferLeadRequest = new TransferLeadRequest(
				etMobile.getText().toString().trim(),
				Objects.requireNonNull(userManager.getUser()).employeeCode
			);
			Disposable disposable = getSaathiApi().transferLead(transferLeadRequest)
				.map(response -> {
					if (response.status) {
						return Pair.create(response.response, response.message);
					} else {
						throw new ServiceFailureException(response.message, response.message);
					}
				})
				.compose(applyUiModel())
				.subscribe(uiModel -> renderTransferLead(leadId, uiModel));
		} else {
			noInternetConnectionDialog();
		}
	}

	public void renderTransferLead(String leadId,
		UiModel<Pair<TransferLeadResponse, String>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}

		Pair<TransferLeadResponse, String> data = uiModel.data;
		if (data != null) {
			String message = data.second;
			TransferLeadResponse response = data.first;
			if (response.isPaymentDone) {
				AlertDialog alertDialog =
					MSFDialog.alertDialog(getActivity(), LOGOUT_CODE,
						getString(R.string.MOSL), message, "OK", true,
						(action, data12) -> {
							if (action == MSFDialog.Action.OK) {
								etMobile.setText("");
								etEmailId.setText("");
							}
						});
				alertDialog.setCancelable(false);
			} else {
				AlertDialog alertDialog =
					MSFDialog.alertDialog(getActivity(), LOGOUT_CODE,
						getString(R.string.MOSL), message, "OK", true,
						(action, data1) -> {
							if (action == MSFDialog.Action.OK) {
								Bundle bundle = new Bundle();
								bundle.putString(LEAD_ID, leadId);
								bundle.putString(UCID, "0");
								//navigateTo(NAV_TO_LEAD_VIEW_MAIN,
								//    bundle, getFragmentManager(),
								//    false);
								navigator.openPage(NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true,
									false);
							}
						});
				alertDialog.setCancelable(false);
			}
		}
	}
}

package com.mosl.tabapp.leadcreation;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.app.core.data.UiModel;
import com.mosl.mobile.ekyc.common.ValidationRule;
import com.mosl.saathi.api.model.lead.CountryItem;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.CRMValidationRule;
import com.mosl.tabapp.common.CustomListPickerDialog;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.common.SearchableSpinner;
import com.mosl.tabapp.leadViewDetail.AttendeeAdapter;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.AddressDetailModel;
import com.mosl.tabapp.model.ContactDetailModel;
import com.mosl.tabapp.model.EmployeeModel;
import com.mosl.tabapp.model.LeadCreationModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import com.mosl.tabapp.ws.RefreeSyncWebServiceCall;
import cz.msebera.android.httpclient.Header;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.droidparts.widget.ClearableEditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mosl.app.core.data.UiModelHelper.applyUiModel;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.ErrorMsgName;
import static com.mosl.tabapp.common.CRMAppUtil.filterOnlyAlphabetAndNumbersAnsAllowedChar;
import static com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.common.CRMMOSLCommon.disableField;
import static com.mosl.tabapp.common.CRMValidationRule.isValidEmail;
import static com.mosl.tabapp.common.CRMValidationRule.validateAadhar;
import static com.mosl.tabapp.common.CRMValidationRule.validateTel;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class LeadCreationFragment extends MOSLCommonFragment
	implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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
	private org.droidparts.widget.ClearableEditText actAssignTo;
	private CheckBox cbPrimaryAddress, cbSameAs, cbOffPrimaryAddress;
	private Button btnCreate, btnCancel;

	private LinearLayout llResAdd, llOffAdd;
	private ArrayList<ContactDetailModel> contactDetailList = new ArrayList<>();
	private AddressDetailModel residenceAddress, officeAddress;

	private ScrollView scrollView;
	private String categoryCodeStr, categoryNameStr = " ", professionCodeStr = "",
		interestInCodeStr, interestInNameStr, productTypeCodeStr, sourceCodeStr,
		prospectSourceCodeStr, prospectSourceNameStr, stageCodeStr,
		statusCodeStr, countryCodeStr, stateCodeStr,
		cityCodeStr, offCountryCodeStr, offStateCodeStr, offCityCodeStr;
	private ArrayList<RowData> arrCategory, arrProfession, arrInterested, arrProductType, arrSource,
		arrProspectSource, arrStage, arrStatus, arrState, arrCity, arrOffState,
		arrOffCity;
	private ArrayList<RowData> arrCountry = new ArrayList<>();
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
					arrCity = new ArrayList<>();
					arrCity.add(new RowData(0, "-1", "Select"));
					arrAdapCity =
						new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item,
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

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		leadCreationView = super.onCreateView(inflater, container, savedInstanceState);
		attachLayout(R.layout.frag_lead_creation);
		setAppTitle(getClass().toString(), "Lead Creation");
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

	private void setUpView(View leadCreationView) {
		scrollView = leadCreationView.findViewById(R.id.scrollView);
		scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		scrollView.setFocusable(true);
		scrollView.setFocusableInTouchMode(true);
		scrollView.setOnTouchListener(new View.OnTouchListener() {
			@Override public boolean onTouch(View v, MotionEvent event) {
				v.requestFocusFromTouch();
				return false;
			}
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
			new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, strArr);
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
					new CustomListPickerDialog(getMainActivity());
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

							final ArrayList<EmployeeModel> modelList = new ArrayList<>();
							try {
								searchList.showProgress(View.VISIBLE);
								if (constraint != null) {
									if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
										Map requestMap = new HashMap();
										requestMap.put("EmployeeName",
											URLEncoder.encode(constraint.toString(), "utf-8"));
										requestMap.put("EmployeeCode",
											URLEncoder.encode(constraint.toString(), "utf-8"));

										String serviceMethod = "UserApi/GetUserBusinesprofile";
										hideKeyboard(actAssignTo);
										try {
											CRMWebServiceHandler.post(getMainActivity(),
												serviceMethod, getHeader(), requestMap,
												new JsonHttpResponseHandler() {

													@Override public void onSuccess(int statusCode,
														Header[] headers, JSONObject response) {
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
																final AttendeeAdapter
																	myAssineeAdapter =
																	new AttendeeAdapter(
																		getMainActivity(),
																		modelList);

																getMainActivity().runOnUiThread(
																	new Runnable() {
																		@Override
																		public void run() {
																			searchList.setAdapter(
																				myAssineeAdapter);
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

													@Override public void onSuccess(int statusCode,
														Header[] headers, JSONArray response) {
														super.onSuccess(statusCode, headers,
															response);
														searchList.showProgress(View.GONE);
													}

													@Override public void onSuccess(int statusCode,
														Header[] headers, String responseString) {
														super.onSuccess(statusCode, headers,
															responseString);
														searchList.showProgress(View.GONE);
													}

													@Override public void onFailure(int statusCode,
														Header[] headers, Throwable throwable,
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

													@Override public void onFailure(int statusCode,
														Header[] headers, Throwable throwable,
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

													@Override public void onFailure(int statusCode,
														Header[] headers, String responseString,
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
					if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
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
					if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
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
			.filter(event -> event.view().isFocused())
			.map(query -> query.editable().toString().trim())
			.debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
			.share();
		compositeDisposable.add(
			refreeNameObs.filter(query -> query.length() == 2).subscribe(ignored -> {
				etRefreeEmail.setText("");
				etRefreeMobile.setText("");
				etRefreeCode.setText("");
			}));
		compositeDisposable.add(
			refreeNameObs.filter(query -> query.length() > 2).switchMap(query -> {
				pbLoadingIndicatorRefreeName.setVisibility(View.VISIBLE);
				etRefreeEmail.setText("");
				etRefreeMobile.setText("");
				etRefreeCode.setText("");
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

		Observable<String> refreeCodeObs = RxTextView.afterTextChangeEvents(etRefreeCode)
			.filter(event -> event.view().isFocused())
			.map(query -> query.editable().toString().trim())
			.debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
			.share();

		compositeDisposable.add(
			refreeCodeObs.filter(query -> query.length() == 3).subscribe(ignored -> {
				etRefreeName.setText("");
				etRefreeMobile.setText("");
				etRefreeEmail.setText("");
			}));

		compositeDisposable.add(
			refreeCodeObs.filter(query -> query.length() > 3).switchMap(query -> {
				pbLoadingIndicatorRefreeCode.setVisibility(View.VISIBLE);
				etRefreeName.setText("");
				etRefreeMobile.setText("");
				etRefreeEmail.setText("");
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
		if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
			showProgress();
			Map inputMap = new HashMap<>();
			inputMap.put("LookupTypeId", LEADCATEGORY);
			inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

			String serviceMethod = "Master/GetAllLookup";

			try {
				CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
					new JsonHttpResponseHandler() {

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							//after success code goes here
							arrCategory = new ArrayList<>();
							try {
								if (statusCode == 200) {
									String status = response.getString(STATUS);
									if (status.equalsIgnoreCase("true")) {
										hideProgress();
										arrCategory.add(new RowData(0, "-1", "Select"));
										JSONArray jsonArray = response.getJSONArray(RESPONSE);

										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject objDetail = jsonArray.getJSONObject(i);
											String lookUpId =
												(objDetail.isNull(LOOKUPID) || objDetail.getString(
													LOOKUPID).equals("")) ? ""
													: objDetail.getString(LOOKUPID);
											String lookUpName = (objDetail.isNull(LOOKUPNAME)
												|| objDetail.getString(LOOKUPNAME).equals("")) ? ""
												: objDetail.getString(LOOKUPNAME);
											String sysName =
												(objDetail.isNull(SYS_NAME) || objDetail.getString(
													SYS_NAME).equals("")) ? ""
													: objDetail.getString(SYS_NAME);
											arrCategory.add(
												new RowData(i + 1, lookUpId, lookUpName, sysName));
										}
										arrAdapCategory = new ArrayAdapter<>(getMainActivity(),
											R.layout.simple_spinner_item, arrCategory);
										arrAdapCategory.setDropDownViewResource(R.layout.mdropdown);
										spCategory.setAdapter(arrAdapCategory);
										spCategory.setOnItemSelectedListener(categoryInListener);
										getInterestIn();
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onSuccess(int statusCode,
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
											String lookUpId =
												(objDetail.isNull(LOOKUPID) || objDetail.getString(
													LOOKUPID).equals("")) ? ""
													: objDetail.getString(LOOKUPID);
											String lookUpName = (objDetail.isNull(LOOKUPNAME)
												|| objDetail.getString(LOOKUPNAME).equals("")) ? ""
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
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
											String lookUpId =
												(objDetail.isNull(LOOKUPID) || objDetail.getString(
													LOOKUPID).equals("")) ? ""
													: objDetail.getString(LOOKUPID);
											String lookUpName = (objDetail.isNull(LOOKUPNAME)
												|| objDetail.getString(LOOKUPNAME).equals("")) ? ""
												: objDetail.getString(LOOKUPNAME);
											arrInterested.add(
												new RowData(i + 1, lookUpId, lookUpName));
										}
										arrAdapInterested = new ArrayAdapter<>(getMainActivity(),
											R.layout.simple_spinner_item, arrInterested);
										arrAdapInterested.setDropDownViewResource(
											R.layout.mdropdown);
										spInterestIn.setAdapter(arrAdapInterested);
										spInterestIn.setOnItemSelectedListener(interestInListener);

										getSourceList();
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onSuccess(int statusCode,
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
											String lookUpId =
												(objDetail.isNull(LOOKUPID) || objDetail.getString(
													LOOKUPID).equals("")) ? ""
													: objDetail.getString(LOOKUPID);
											String lookUpName = (objDetail.isNull(LOOKUPNAME)
												|| objDetail.getString(LOOKUPNAME).equals("")) ? ""
												: objDetail.getString(LOOKUPNAME);
											arrProductType.add(
												new RowData(i + 1, lookUpId, lookUpName));
										}
										arrAdapProductType = new ArrayAdapter<>(getMainActivity(),
											R.layout.simple_spinner_item, arrProductType);
										arrAdapProductType.setDropDownViewResource(
											R.layout.mdropdown);
										spProductType.setAdapter(arrAdapProductType);
										spProductType.setOnItemSelectedListener(
											productTypeListener);
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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

	private void getSourceList() {
		if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
			showProgress();
			Map inputMap = new HashMap<>();
			inputMap.put("id", AccountDetails.getBusinessUnitId());

			String serviceMethod = "Source/GetSourceList";

			try {
				CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
					new JsonHttpResponseHandler() {

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							//after success code goes here
							arrSource = new ArrayList<>();
							try {
								if (statusCode == 200) {
									String status = response.getString(STATUS);
									if (status.equalsIgnoreCase("true")) {
										hideProgress();
										JSONArray jsonArray = response.getJSONArray(RESPONSE);

										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject objDetail = jsonArray.getJSONObject(i);
											String sourceId =
												(objDetail.isNull(SOURCEID) || objDetail.getString(
													SOURCEID).equals("")) ? ""
													: objDetail.getString(SOURCEID);
											String sourceName = (objDetail.isNull(SOURCENAME)
												|| objDetail.getString(SOURCENAME).equals("")) ? ""
												: objDetail.getString(SOURCENAME);
											arrSource.add(new RowData(i + 1, sourceId, sourceName));
										}
										arrAdapSource = new ArrayAdapter<>(getMainActivity(),
											R.layout.simple_spinner_item, arrSource);
										arrAdapSource.setDropDownViewResource(R.layout.mdropdown);
										spSource.setAdapter(arrAdapSource);
										spSource.setOnItemSelectedListener(sourceListener);

										getStage();
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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

	private void getProspectSource(String productId) {
		if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
			showProgress();
			Map inputMap = new HashMap<>();
			inputMap.put("SourceId", productId);

			String serviceMethod = "LeadSource/GetLeadSourceList";

			try {
				CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
					new JsonHttpResponseHandler() {

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							arrProspectSource = new ArrayList<>();
							try {
								if (statusCode == 200) {
									String status = response.getString(STATUS);
									if (status.equalsIgnoreCase("true")) {
										hideProgress();
										JSONArray jsonArray = response.getJSONArray(RESPONSE);
										// arrProspectSource.add(new RowData(0, "-1", "Select"));

										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject objDetail = jsonArray.getJSONObject(i);
											String leadSourceId = (objDetail.isNull(LEADSOURCEID)
												|| objDetail.getString(LEADSOURCEID).equals(""))
												? "" : objDetail.getString(LEADSOURCEID);
											String leadSourceName =
												(objDetail.isNull(LEADSOURCENAME)
													|| objDetail.getString(LEADSOURCENAME)
													.equals("")) ? ""
													: objDetail.getString(LEADSOURCENAME);
											arrProspectSource.add(
												new RowData(i + 1, leadSourceId, leadSourceName));
										}
										arrAdapProspectSource =
											new ArrayAdapter<>(getMainActivity(),
												R.layout.simple_spinner_item, arrProspectSource);
										arrAdapProspectSource.setDropDownViewResource(
											R.layout.mdropdown);
										spProspectSource.setAdapter(arrAdapProspectSource);
										spProspectSource.setOnItemSelectedListener(
											prospectSourceListener);
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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

	private void getStage() {
		if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
			showProgress();
			Map inputMap = new HashMap<>();
			inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());

			String serviceMethod = "Stage/GetStageList";

			try {
				CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
					new JsonHttpResponseHandler() {

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							arrStage = new ArrayList<>();

							try {
								if (statusCode == 200) {
									String status = response.getString(STATUS);
									if (status.equalsIgnoreCase("true")) {
										hideProgress();
										JSONArray jsonArray = response.getJSONArray(RESPONSE);

										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject objDetail = jsonArray.getJSONObject(i);
											String stageId =
												(objDetail.isNull(STAGEID) || objDetail.getString(
													STAGEID).equals("")) ? ""
													: objDetail.getString(STAGEID);
											String stageName =
												(objDetail.isNull(STAGENAME) || objDetail.getString(
													STAGENAME).equals("")) ? ""
													: objDetail.getString(STAGENAME);
											arrStage.add(new RowData(i + 1, stageId, stageName));
										}
										arrAdapStage = new ArrayAdapter<>(getMainActivity(),
											R.layout.simple_spinner_item, arrStage);
										arrAdapStage.setDropDownViewResource(R.layout.mdropdown);
										spStage.setAdapter(arrAdapStage);
										spStage.setOnItemSelectedListener(stageListener);

										getCountry();
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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

	private void getStatus(String productId) {
		if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
			showProgress();
			Map inputMap = new HashMap<>();
			inputMap.put("StageID", productId);
			inputMap.put("Status", "1");

			String serviceMethod = "LeadStatus/GetStatusList";

			try {
				CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
					new JsonHttpResponseHandler() {

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							arrStatus = new ArrayList<>();
							try {
								if (statusCode == 200) {
									String status = response.getString(STATUS);
									if (status.equalsIgnoreCase("true")) {
										hideProgress();
										// arrStatus.add(new RowData(0, "-1", "Select"));
										JSONArray jsonArray = response.getJSONArray(RESPONSE);

										for (int i = 0; i < jsonArray.length(); i++) {
											JSONObject objDetail = jsonArray.getJSONObject(i);
											String statusId =
												(objDetail.isNull(STATUSID) || objDetail.getString(
													STATUSID).equals("")) ? ""
													: objDetail.getString(STATUSID);
											String statusName = (objDetail.isNull(STATUSNAME)
												|| objDetail.getString(STATUSNAME).equals("")) ? ""
												: objDetail.getString(STATUSNAME);
											arrStatus.add(new RowData(i + 1, statusId, statusName));
										}
										arrAdapStatus = new ArrayAdapter<>(getMainActivity(),
											R.layout.simple_spinner_item, arrStatus);
										arrAdapStatus.setDropDownViewResource(R.layout.mdropdown);
										spStatus.setAdapter(arrAdapStatus);
										spStatus.setOnItemSelectedListener(statusListener);
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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

	private void renderCountryList(com.mosl.app.core.data.UiModel<List<CountryItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
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
			arrAdapCountry = new ArrayAdapter<>(getMainActivity(),
				R.layout.simple_spinner_item, arrCountry);
			arrAdapCountry.setDropDownViewResource(R.layout.mdropdown);
			spCountry.setAdapter(arrAdapCountry);
			spCountry.setOnItemSelectedListener(countryListener);
			spOffCountry.setAdapter(arrAdapCountry);
			spOffCountry.setOnItemSelectedListener(offCountryListener);
		}
	}

	private void getCountry() {

		if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
			Disposable subscribe = saathiApi.postCountryList()
				.compose(applyUiModel())
				.subscribe(this::renderCountryList);
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

						@Override public void onSuccess(int statusCode,
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
											String stateId =
												(objDetail.isNull(STATEID) || objDetail.getString(
													STATEID).equals("")) ? ""
													: objDetail.getString(STATEID);
											String stateName =
												(objDetail.isNull(STATENAME) || objDetail.getString(
													STATENAME).equals("")) ? ""
													: objDetail.getString(STATENAME);
											if (type.equals(RESIDENCE)) {
												arrState.add(
													new RowData(i + 1, stateId, stateName));
											} else if (type.equals(OFFICE)) {
												arrOffState.add(
													new RowData(i + 1, stateId, stateName));
											}
										}
										if (type.equals(RESIDENCE)) {
											arrAdapState = new ArrayAdapter<>(getMainActivity(),
												R.layout.simple_spinner_item, arrState);
											arrAdapState.setDropDownViewResource(
												R.layout.mdropdown);
											spState.setAdapter(arrAdapState);
											spState.setOnItemSelectedListener(stateListener);
										} else if (type.equals(OFFICE)) {
											arrAdapOffState = new ArrayAdapter<>(getMainActivity(),
												R.layout.simple_spinner_item, arrOffState);
											arrAdapOffState.setDropDownViewResource(
												R.layout.mdropdown);
											spOffState.setAdapter(arrAdapOffState);
											RowData data = RowData.getSpinnerRowDataById(value,
												arrAdapOffState);
											if (data != null) {
												spOffState.setSelection(data.getPosition());
											}
											spOffState.setOnItemSelectedListener(offStateListener);
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onSuccess(int statusCode,
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
											String cityId =
												(objDetail.isNull(CITYID) || objDetail.getString(
													CITYID).equals("")) ? ""
													: objDetail.getString(CITYID);
											String cityName =
												(objDetail.isNull(CITYNAME) || objDetail.getString(
													CITYNAME).equals("")) ? ""
													: objDetail.getString(CITYNAME);
											if (type.equals(RESIDENCE)) {
												arrCity.add(new RowData(i + 1, cityId, cityName));
											} else if (type.equals(OFFICE)) {
												arrOffCity.add(
													new RowData(i + 1, cityId, cityName));
											}
										}
										if (type.equals(RESIDENCE)) {
											arrAdapCity = new ArrayAdapter<>(getMainActivity(),
												R.layout.simple_spinner_item, arrCity);
											arrAdapCity.setDropDownViewResource(R.layout.mdropdown);
											spCity.setAdapter(arrAdapCity);
											spCity.setOnItemSelectedListener(cityListener);
										} else if (type.equals(OFFICE)) {
											arrAdapOffCity = new ArrayAdapter<>(getMainActivity(),
												R.layout.simple_spinner_item, arrOffCity);
											arrAdapOffCity.setDropDownViewResource(
												R.layout.mdropdown);
											spOffCity.setAdapter(arrAdapOffCity);
											RowData data = RowData.getSpinnerRowDataById(value,
												arrAdapOffCity);
											if (data != null) {
												spOffCity.setSelection(data.getPosition());
											}
											spOffCity.setOnItemSelectedListener(offCityListener);
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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

	private void updateLead(LeadCreationModel leadCreationModel) {
		if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
			showProgress();
			String requestJson = leadCreationModel.getJson();
			String serviceMethod = "Lead/AddUpdateLead";

			try {
				CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(),
					requestJson, new JsonHttpResponseHandler() {

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							try {
								if (statusCode == 200) {
									String status = response.getString(STATUS);
									String message = response.getString(MESSAGE);
									if (status.equalsIgnoreCase("true")) {
										hideProgress();
										JSONObject jsonObject = response.getJSONObject("Response");
										String leadId = jsonObject.getString("LeadID");
										MSFDialog.alertDialog(getMainActivity(), 0,
											getResources().getString(R.string.motilal_oswal),
											"Lead Created Successfully with lead Id " + leadId,
											"OK", true, new MSFDialog.DialogListener() {
												@Override public void alertDialogAction(
													MSFDialog.Action action, Object... data) {
													if (action == MSFDialog.Action.OK) {
														dashbordFragment();
													}
												}
											});
									} else {
										hideProgress();
										JSONObject jsonObject = response.getJSONObject("Response");
										if (jsonObject.has("DedupeModel") && response.has(
											"Erros")) {
											String dedupeModel =
												jsonObject.getString("DedupeModel");
											JSONObject jsonObjectDedup =
												new JSONObject(dedupeModel);
											dedupeDialog(jsonObjectDedup);
										} else if (response.has("Erros")) {
											String errorMsg =
												CRMMOSLCommon.getErrorMsgFromResponse(response);
											alertDialog(getMainActivity(), 0,
												getResources().getString(R.string.motilal_oswal),
												errorMsg, "OK", true, null);
										}
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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
		} else if (!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
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
			&& !ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
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
			!ValidationRule.validateOnlyAlphabetAndNumberWithoutSplChar(
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
						MSFDialog.alertDialog(getMainActivity(), 0,
							getResources().getString(R.string.motilal_oswal), CONFIRM_LEAD, "YES",
							"NO", true, new MSFDialog.DialogListener() {
								@Override public void alertDialogAction(MSFDialog.Action action,
									Object... data) {
									if (action == MSFDialog.Action.OK) {
										createLead();
										return;
									} else if (action == MSFDialog.Action.CANCEL) {
										return;
									}
								}
							});
					}
					break;
				case R.id.btnCancel:
					dashbordFragment();
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

	private void createLead() {
		residenceAddress =
			new AddressDetailModel(RESIDENCEADDRESS, etAdd1.getText().toString().trim(),
				etAdd2.getText().toString().trim(), etAdd3.getText().toString().trim(),
				countryCodeStr, stateCodeStr, cityCodeStr, etPincode.getText().toString().trim(),
				cbPrimaryAddress.isChecked(), AccountDetails.getBusinessUnitId());
		officeAddress = new AddressDetailModel(OFFICEADDRESS, etOffAdd1.getText().toString().trim(),
			etOffAdd2.getText().toString().trim(), etOffAdd3.getText().toString().trim(),
			offCountryCodeStr, offStateCodeStr, offCityCodeStr,
			etOffPincode.getText().toString().trim(), cbOffPrimaryAddress.isChecked(),
			AccountDetails.getBusinessUnitId());

		LeadCreationModel leadCreationModel = new LeadCreationModel();
		leadCreationModel.seteMail(etEmailId.getText().toString().trim());
		leadCreationModel.setSameAsAbove(cbSameAs.isChecked());
		leadCreationModel.setAlternateEmail(etAlternateEmailId.getText().toString().trim());
		leadCreationModel.setMobileNo(etMobile.getText().toString().trim());
		leadCreationModel.setLandLineNo(etLandLine.getText().toString().trim());
		leadCreationModel.setEkycMobileNo(etEkycMobile.getText().toString().trim());
		leadCreationModel.setPanNo(etPanNum.getText().toString().trim());
		leadCreationModel.setAadharNo(etAadharNum.getText().toString().trim());
		leadCreationModel.setBusinessUnitID(AccountDetails.getBusinessUnitId());
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
		leadCreationModel.setProfileID(AccountDetails.getBusinessProfileId());
		leadCreationModel.setCreatedBy(AccountDetails.getEmployeeCode());
		leadCreationModel.setCreatedThrough("SelfCreated");
		leadCreationModel.setCreationDate(CRMAppUtil.getDBDate());
		leadCreationModel.setContactDetailModels(contactDetailList);
		leadCreationModel.setResidenceAddress(residenceAddress);
		leadCreationModel.setOfficeAddress(officeAddress);
		leadCreationModel.setInterestedIn(interestInCodeStr);
		leadCreationModel.setProfessionid(professionCodeStr);
		leadCreationModel.setProductID(productTypeCodeStr);
		if (primaryRM != null) {
			leadCreationModel.setPrimaryRM(primaryRM);
		} else {
			leadCreationModel.setPrimaryRM(AccountDetails.getEmployeeCode());
		}
		leadCreationModel.setSecondaryRM(null);
		leadCreationModel.setClientCode(null);
		leadCreationModel.setAssignedBy(AccountDetails.getEmployeeCode());
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

	private void dedupeDialog(JSONObject jsonObject) {
		try {
			final String leadId = jsonObject.getString("LeadID");
			final String CheckRemark = jsonObject.getString("CheckRemark");
			final String Stage = jsonObject.getString("Stage");
			final String BranchCode = jsonObject.getString("BranchCode");
			final String DedupeWith = jsonObject.getString("DedupeWith");
			final String ClientName = jsonObject.getString("ClientName");
			final String ClientCode = jsonObject.getString("ClientCode");
			final String ProductName = jsonObject.getString("ProductName");
			final String LeadName = jsonObject.getString("LeadName");
			String IsTransfer = jsonObject.getString("IsTransfer");

			final Dialog dialog = new Dialog(getMainActivity());
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
					navigateTo(NAV_TO_LEAD_CREATION, new Bundle(), getFragmentManager(), false);
					dialog.dismiss();
				}
			});
			dialog.show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void transferLead(final String leadId) {
		if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
			showProgress();
			final Map inputMap = new HashMap<>();
			inputMap.put(MOBILE, etMobile.getText().toString().trim());
			inputMap.put("CurrentUser", AccountDetails.getEmployeeCode());
			String serviceMethod = "/Lead/TransferLeadCaughtInC2";
			try {
				CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
					new JsonHttpResponseHandler() {

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							try {

								if (statusCode == 200) {
									String status = response.getString(STATUS);
									String message = response.getString(MESSAGE);
									if (status.equalsIgnoreCase("true")) {
										hideProgress();
										JSONObject jsonObject = response.getJSONObject(RESPONSE);
										boolean isPaymentDone =
											jsonObject.getBoolean("IsPaymentDone");
										if (isPaymentDone) {
											AlertDialog alertDialog =
												MSFDialog.alertDialog(getActivity(), LOGOUT_CODE,
													getString(R.string.MOSL), message, "OK", true,
													new MSFDialog.DialogListener() {
														@Override public void alertDialogAction(
															MSFDialog.Action action,
															Object... data) {
															if (action == MSFDialog.Action.OK) {
																etMobile.setText("");
																etEmailId.setText("");
															}
														}
													});
											alertDialog.setCancelable(false);
										} else {
											AlertDialog alertDialog =
												MSFDialog.alertDialog(getActivity(), LOGOUT_CODE,
													getString(R.string.MOSL), message, "OK", true,
													new MSFDialog.DialogListener() {
														@Override public void alertDialogAction(
															MSFDialog.Action action,
															Object... data) {
															if (action == MSFDialog.Action.OK) {
																Bundle bundle = new Bundle();
																bundle.putString(LEAD_ID, leadId);
																bundle.putString(UCID, "0");
																navigateTo(NAV_TO_LEAD_VIEW_MAIN,
																	bundle, getFragmentManager(),
																	false);
															}
														}
													});
											alertDialog.setCancelable(false);
										}
									} else {
										hideProgress();
										String errorMsg =
											CRMMOSLCommon.getErrorMsgFromResponse(response);
										AlertDialog dd = MSFDialog.alertDialog(getMainActivity(), 0,
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

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override public void onFailure(int statusCode,
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

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
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
}

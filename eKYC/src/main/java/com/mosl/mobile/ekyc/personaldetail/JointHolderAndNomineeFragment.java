package com.mosl.mobile.ekyc.personaldetail;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.mosl.mobile.ekyc.model.RowData;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.CustomDatePickerDialog;
import com.mosl.mobile.ekyc.ui.CustomDialog;
import com.mosl.mobile.ekyc.ui.DatePickerListener;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.disableField;
import static com.mosl.mobile.ekyc.common.MOSLCommon.enableField;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isValidEmail;
import static com.mosl.mobile.ekyc.common.MOSLCommon.loadCountryArray;
import static com.mosl.mobile.ekyc.common.MOSLCommon.removeKeyboard;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.getAddressMode;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.*;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.JointHolderModel.*;
import static com.mosl.mobile.ekyc.model.JointHolderModel.getDematFirstJointHolderFHFname;
import static com.mosl.mobile.ekyc.model.JointHolderModel.getDematFirstJointHolderFHMname;
import static com.mosl.mobile.ekyc.model.JointHolderModel.getDematFirstJointHolderFname;
import static com.mosl.mobile.ekyc.model.JointHolderModel.getDematSecondJointHolderFHFname;
import static com.mosl.mobile.ekyc.model.JointHolderModel.getDematSecondJointHolderFHLname;
import static com.mosl.mobile.ekyc.model.JointHolderModel.getDematSecondJointHolderFHMname;
import static com.mosl.mobile.ekyc.model.JointHolderModel.getDematSecondJointHolderFname;
import static com.mosl.mobile.ekyc.model.JointHolderModel.getDematSecondJointHolderLname;
import static com.mosl.mobile.ekyc.model.RowData.getSpinnerRowDataByValue;
import static com.mosl.mobile.ekyc.ui.EkycMainActivity.customProgress;

/**
 * Created by Vijay on 09/12/2016.
 */

public class JointHolderAndNomineeFragment extends Fragment implements TextWatcher, MOSLConstants, View.OnClickListener {

    Context mContext;
    CustomViewPager viewPager;
    TabPageIndicator tabPageIndicator;
    View jointHolderNomineeView;
    int hideProgressCounter = 0, hideUpdateProgressCounter = 0;
    boolean firstUpdate = false, secondUpdate = false;
    boolean jointHolderUpdate = false, nomineeUpdate = false;
    boolean nomineeDOBClicked = false;
    boolean nomineeAddress = false;
    Button btnSecondHolder, btnThirdHolder, /*btnSaveJointHolder, btnSaveNominee,*/
            btnSaveJointNominee;
    RelativeLayout rlSecondJointHolder, rlThirdJointHolder;
    TextView etNumberOfJointHolderError;
    Spinner spNumberOfJointHolder;
    //    CheckBox cbNomineeYes, cbNomineeNo, cbIndividual, cbJoint;
    ToggleButton toggleNominee, toggleJointHolder;
    //    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    LinearLayout llNumberOfJointHolder, llNomineeDetail, llNomineeGuardian, llJointHolderDetail, llJointHolderQuestion, llNomineeQuestion;
    TextView tvNomineeJointTitle;

    EditText etSJHFirstName, etSJHMiddleName, etSJHLastName, etSJHFatherSpouseFirstName, etSJHFatherSpouseMidName, etSJHFatherSpouseLastName, etSJHDOB, etSJHPAN, etSJHAadhar,
            etSJHMobile, etSJHEmail, etSJHSTDNo, etSJHTelNo, etSJHBldgNameCorres, etSJHFlatNoCorres, etSJHAreaCorres, etSJHPincodeCorres, etSJHCorresCityOther, etSJHBldgNamePerm, etSJHFlatNamePerm, etSJHAreaPerm,
            etSJHPermCityOther, etSJHPincodePerm, etTJHFirstName, etTJHMiddleName, etTJHLastName, etTJHFatherSpouseFirstName, etTJHFatherSpouseMidName, etTJHFatherSpouseLastName, etTJHDOB, etTJHPAN,
            etTJHAadhar, etTJHMobile, etTJHEmail, etTJHSTDNo, etTJHTelNo, etTJHBldgNameCorres, etTJHFlatNoCorres, etTJHAreaCorres, etTJHCorresCityOther, etTJHPincodeCorres, etTJHBldgNamePerm, etTJHFlatNamePerm,
            etTJHAreaPerm, etTJHPermCityOther, etTJHPincodePerm, etSJHCorresState, etTJHCorresState, etSJHCorresCity, etTJHCorresCity, etSJHPermState, etTJHPermState, etSJHPermCity, etTJHPermCity;

    Spinner spSJHResidentStatus, spSJHTitle, spSJHFatherSpouseSel, spSJHAddProofCorres, spSJHAddProofPerm, spSJHIncome, spSJHOccupation,
            spTJHResidentStatus, spTJHTitle, spTJHFatherSpouseSel, spTJHAddProofCorres, spTJHAddProofPerm, spTJHIncome, spTJHOccupation,
            spTJHCorresCountry, spTJHPermCountry, spSJHCorresCountry, spSJHPermCountry;

    SearchableSpinner spSJHCorresState, spSJHCorresCity, spSJHPermState, spSJHPermCity, spTJHCorresState, spTJHCorresCity, spTJHPermState, spTJHPermCity;

    CheckBox cbSJHMale, cbSJHFemale, cbSJHMarried, cbSJHUnMarried, cbSJHSameAsPrimaryCorrespondence, cbSJHSameAsCorrespondence, cbSJHPoliticalyNo, cbSJHPoliticalyYes, cbSJHRelatedPoliticalNo,
            cbSJHRelatedPoliticalYes, cbTJHMale, cbTJHFemale, cbTJHMarried, cbTJHUnMarried, cbTJHSameAsPrimaryCorrespondence, cbTJHSameAsCorrespondence, cbTJHPoliticalyNo, cbTJHPoliticalyYes,
            cbTJHRelatedPoliticalNo, cbTJHRelatedPoliticalYes;

    LinearLayout llSJHCorrespondenceAddress, llSJHPermanentAddress, llSJHWorkDetail, llTJHCorrespondenceAddress, llTJHPermanentAddress, llTJHWorkDetail;

    TextInputLayout inputLayoutSJHFirstName, inputLayoutSJHMiddleName, inputLayoutSJHLastName, inputLayoutSJHFatherSpouseFirstName, inputLayoutSJHFatherSpouseMidName,
            inputLayoutSJHFatherSpouseLastName, inputLayoutSJHDob, inputLayoutSJHPAN, inputLayoutSJHAadhar, inputLayoutSJHMobile, inputLayoutSJHEmail, inputLayoutSJHSTD, inputLayoutSJHTelNo,
            inputLayoutSJHBldgNameCorres, inputLayoutSJHFlatNoCorres, inputLayoutSJHAreaCorres, inputLayoutSJHCorresCityOther, inputLayoutSJHPincodeCorres, inputLayoutSJHBldgNamePerm, inputLayoutSJHFlatNamePerm,
            inputLayoutSJHAreaPerm, inputLayoutSJHPermCityOther, inputLayoutSJHPincodePerm, inputLayoutTJHFirstName, inputLayoutTJHMiddleName, inputLayoutTJHLastName, inputLayoutTJHFatherSpouseFirstName,
            inputLayoutTJHFatherSpouseMidName, inputLayoutTJHFatherSpouseLastName, inputLayoutTJHDob, inputLayoutTJHPAN, inputLayoutTJHAadhar, inputLayoutTJHMobile, inputLayoutTJHEmail,
            inputLayoutTJHSTD, inputLayoutTJHTelNo, inputLayoutTJHBldgNameCorres, inputLayoutTJHFlatNoCorres, inputLayoutTJHAreaCorres, inputLayoutTJHCorresCityOther, inputLayoutTJHPincodeCorres, inputLayoutTJHBldgNamePerm,
            inputLayoutTJHFlatNamePerm, inputLayoutTJHAreaPerm, inputLayoutTJHPermCityOther, inputLayoutTJHPincodePerm;

    TextView etSJHResidentStatusError, etSJHTitleError, etSJHGenderError, etSJHFatherSpouseSelError, etSJHMaritalError, etSJHAddProofCorresError, etSJHAddProofPermError,
            etSJHCorresStateError, etSJHCorresCityError, etSJHCorresCountryError, etSJHPermStateError, etSJHPermCityError, etSJHPermCountryError, etSJHIncomeError, etSJHOccupationError, etTJHResidentStatusError, etTJHTitleError,
            etTJHGenderError, etTJHFatherSpouseSelError, etTJHmaritalError, etTJHAddProofCorresError, etTJHAddProofPermError, etTJHCorresStateError, etTJHCorresCityError, etTJHCorresCountryError,
            etTJHPermStateError, etTJHPermCountryError, etTJHPermCityError, etTJHIncomeError, etTJHOccupationError, etSJHSTDTelError, etTJHSTDTelError, etNomineeCorresCountryError, etNomineeGuardianCorresCountryError;

    ArrayAdapter<String> arrAdapResidentStatus, arrAdapOcc, arrAdapIncome, arrAdapTitle, arrAdapFatherSpouseSel, arrAdapCountryBirth, arrAdapGuardianAge;
    ArrayList<RowData> arrState, arrCorresCity, arrNomineeCity, arrPermCity, arrCorresAddressProof, arrPermAddressProof, arrRelationship;
    ArrayAdapter<RowData> arrAdapState, arrAdapCorresCity, arrAdapNomineeCity, arrAdapPermCity, arrAdapCorresAddressProof, arrAdapPermAddressProof, arrAdapRelationship;
    String corresStateCodeStr, corresCityCodeStr, permStateCodeStr, permCityCodeStr, tjhCorresStateCodeStr, tjhCorresCityCodeStr, tjhPermStateCodeStr, tjhPermCityCodeStr;
    String corresStateNameStr, corresCityNameStr, permStateNameStr, permCityNameStr, tjhCorresStateNameStr, tjhCorresCityNameStr, tjhPermStateNameStr, tjhPermCityNameStr;
    String nomineeStateCodeStr, nomineeCityCodeStr, nomineeGuardianStateCodeStr, nomineeGuardianCityCodeStr, nomineeRelationCodeStr;
    String nomineeStateNameStr, nomineeCityNameStr, nomineeGuardianStateNameStr, nomineeGuardianCityNameStr, nomineeRelationNameStr;
    //Boolean cityFoundFlag = false, nomineeCityFoundFlag = false;
    Boolean cityFoundFlag = false;

    String nomineeCitySaved = null, nomineeGuardianCitySaved = null;

    //Nominee
    TextView tvNomineeDetails, tvNomineeGuardianDetails;

    Spinner spNomineesRelationship, spNomineesGuardianRelationship, spNomineeGuardianAge;

    TextInputLayout inputLayoutNomineeFirstName, inputLayoutNomineeGuardianFirstName, inputLayoutNomineeMiddleName, inputLayoutNomineeGuardianMiddleName, inputLayoutNomineeLastName,
            inputLayoutNomineeGuardianLastName, inputLayoutNomineePAN, inputLayoutNomineesGuardianRelationship, inputLayoutNomineeBldgNameCorres, inputLayoutNomineeGuardianBldgNameCorres,
            inputLayoutNomineeFlatNoCorres, inputLayoutNomineeGuardianFlatNoCorres, inputLayoutNomineeAreaCorres, inputLayoutNomineeGuardianAreaCorres, inputLayoutNomineePincodeCorres,
            inputLayoutNomineeGuardianPincodeCorres, inputLayoutNomineeSTD, inputLayoutNomineeTelNo, inputLayoutNomineeGuardianSTD, inputLayoutNomineeGuardianTelNo, inputLayoutNomineeDob, inputLayoutNomineesRelationship,
            inputLayoutNomineeGuardianEmail, /*inputLayoutNomineePrimaryApplicantsRelationship,*/
            inputLayoutNomineeEmail, inputLayoutNomineeCorresCityOther, inputLayoutNomineeGuardianCorresCityOther;

    TextInputLayout inputLayoutNomineeAadhaar;

    EditText etNomineeFirstName, etNomineeGuardianFirstName, etNomineeMiddleName, etNomineeGuardianMiddleName, etNomineeLastName, etNomineeGuardianLastName,
            etNomineesGuardianRelationship, etNomineePAN, etNomineeBldgNameCorres, etNomineeGuardianBldgNameCorres, etNomineeFlatNoCorres, etNomineeGuardianFlatNoCorres, etNomineeAreaCorres,
            etNomineeGuardianAreaCorres, etNomineePincodeCorres, etNomineeGuardianPincodeCorres, etNomineeSTDNo, etNomineeTelNo, etNomineeGuardianSTDNo, etNomineeGuardianTelNo, etNomineeDOB,
            etNomineeGuardianEmail, etNomineesRelationship, /*etNomineePrimaryApplicantsRelationship,*/
            etNomineeEmail, etNomineeCorresCityOther, etNomineeGuardianCorresCityOther,
            etNomineeCorresState, etNomineeCorresCity, etNomineeGuardianCorresState, etNomineeGuardianCorresCity;
    EditText etNomineeAadhar;

    CheckBox cbNomineeSameAsPrimaryCorrespondence, cbNomineeGuardianSameAsPrimaryCorrespondence;
    TextView etNomineePincodeCorresError, etNomineeEmailError, etNomineePANError, etNomineeCorresStateError, etNomineeGuardianPincodeCorresError, etNomineeGuardianEmailError,
            etNomineeGuardianCorresStateError, etNomineeCorresCityError, etNomineeGuardianCorresCityError, etNomineeGuardianAgeError, etNomineesRelationshipError, etNomineesGuardianRelationshipError;
    TextView tvNomineeAadharError;
    SearchableSpinner spNomineeCorresState, spNomineeGuardianCorresState, spNomineeCorresCity, spNomineeGuardianCorresCity;
    Spinner spNomineeGuardianCorresCountry, spNomineeCorresCountry;
    boolean adultFlag;
    ToggleButton toggleSJHPoliticaly, toggleSJHRelatedPolitical, toggleTJHPoliticaly, toggleTJHRelatedPolitical;
    RadioButton rbSJHMale, rbSJHFemale, rbTJHMale, rbTJHFemale, rbSJHMarried, rbSJHUnMarried, rbTJHMarried, rbTJHUnMarried;
    TextView etSJHAadharError, etTJHAadharError, etSJHMobileError, etTJHMobileError, etSJHEmailError, etTJHEmailError, etTJHPincodePermError,
            etSJHPincodePermError, etTJHPincodeCorresError, etSJHPincodeCorresError, etSJHPANError, etTJHPANError,
            etNomineeFirstNameError, etNomineeGuardianFirstNameError, etNomineeMiddleNameError, etNomineeGuardianMiddleNameError, etNomineeLastNameError, etNomineeGuardianLastNameError,
            etSJHFirstNameError, etSJHMiddleNameError, etSJHLastNameError, etSJHFatherSpouseFirstNameError, etSJHFatherSpouseMidNameError, etSJHFatherSpouseLastNameError,
            etTJHFirstNameError, etTJHMiddleNameError, etTJHLastNameError, etTJHFatherSpouseFirstNameError, etTJHFatherSpouseMidNameError, etTJHFatherSpouseLastNameError, etNomineeSTDTelError,
            etNomineeGuardianSTDTelError;
    ArrayList countryList = new ArrayList();
    ArrayAdapter<String> arrAdapCountry;
    ScrollView scrollview;
    TextView etNomineeDOBRequestFocus;
    boolean showDefalutDate;
    String date = "";
    String secondJointHolderName = "", thirdJointHolderName = "",
            nomineeName = "", guardianName = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            jointHolderNomineeView = inflater.inflate(R.layout.joint_holder_nominee_detail, container, false);
            setUpView(jointHolderNomineeView);
            setUserVisibleHint(true);
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
        return jointHolderNomineeView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setTabPageIndicator(TabPageIndicator tabPageIndicator) {
        this.tabPageIndicator = tabPageIndicator;
    }

    public void loadJointNomineeDetails() {
        if (viewPager == null || viewPager.getCurrentItem() != getDematTab()) {
            return;
        }
        clearJointHolderModelData();
        clearNomineeFields();
        clearJointHolderFields();
        getState();
    }

    private AdapterView.OnItemSelectedListener numberOfJointHolder = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String jointHolderNo = spNumberOfJointHolder.getSelectedItem().toString();
            etNumberOfJointHolderError.setVisibility(View.GONE);
            if (jointHolderNo.equalsIgnoreCase("select")) {
                llJointHolderDetail.setVisibility(View.GONE);
            } else if (jointHolderNo.equals("1")) {
                llJointHolderDetail.setVisibility(View.VISIBLE);
                btnSecondHolder.setVisibility(View.VISIBLE);
                btnThirdHolder.setVisibility(View.GONE);
                rlSecondJointHolder.setVisibility(View.VISIBLE);
                rlThirdJointHolder.setVisibility(View.GONE);
            } else if (jointHolderNo.equals("2")) {
                llJointHolderDetail.setVisibility(View.VISIBLE);
                btnSecondHolder.setVisibility(View.VISIBLE);
                btnThirdHolder.setVisibility(View.VISIBLE);
                rlSecondJointHolder.setVisibility(View.VISIBLE);
                rlThirdJointHolder.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    // Relationship spinner listener
    private AdapterView.OnItemSelectedListener nomineeRelationshipListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                nomineeRelationCodeStr = ((RowData) spNomineesRelationship.getSelectedItem()).getKey();
                nomineeRelationNameStr = ((RowData) spNomineesRelationship.getSelectedItem()).getValue();
                etNomineesRelationshipError.setVisibility(View.GONE);
                if (nomineeRelationNameStr.equalsIgnoreCase("Others")) {
                    etNomineesRelationship.setVisibility(View.VISIBLE);
                    inputLayoutNomineesRelationship.setVisibility(View.VISIBLE);
                } else {
                    etNomineesRelationship.setVisibility(View.GONE);
                    etNomineesRelationshipError.setVisibility(View.GONE);
                    inputLayoutNomineesRelationship.setVisibility(View.GONE);
                    inputLayoutNomineesRelationship.setErrorEnabled(false);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener nomineeGuardianRelationshipListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                nomineeRelationCodeStr = ((RowData) spNomineesGuardianRelationship.getSelectedItem()).getKey();
                nomineeRelationNameStr = ((RowData) spNomineesGuardianRelationship.getSelectedItem()).getValue();
                etNomineesGuardianRelationshipError.setVisibility(View.GONE);
                if (nomineeRelationNameStr.equalsIgnoreCase("Others")) {
                    etNomineesGuardianRelationship.setVisibility(View.VISIBLE);
                } else {
                    etNomineesGuardianRelationship.setVisibility(View.GONE);
                    etNomineesGuardianRelationshipError.setVisibility(View.GONE);
                    inputLayoutNomineesGuardianRelationship.setErrorEnabled(false);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    //State City spinner listener
    private AdapterView.OnItemSelectedListener corresStateListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                corresStateCodeStr = ((RowData) spSJHCorresState.getSelectedItem()).getKey();
                corresStateNameStr = ((RowData) spSJHCorresState.getSelectedItem()).getValue();
                etSJHCorresStateError.setVisibility(View.GONE);
                //if (cityFoundFlag) {
                getCorresCity(corresStateCodeStr, getDematFirstJointHolderCity(), FirstJoint);
                //}
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
                permStateCodeStr = ((RowData) spSJHPermState.getSelectedItem()).getKey();
                permStateNameStr = ((RowData) spSJHPermState.getSelectedItem()).getValue();
                etSJHPermStateError.setVisibility(View.GONE);
//                if (cityFoundFlag) {
                getPermanentCity(permStateCodeStr, getDematFirstJointHolderCityPerm(), FirstJoint);
//                }
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
                corresCityCodeStr = ((RowData) spSJHCorresCity.getSelectedItem()).getKey();
                corresCityNameStr = ((RowData) spSJHCorresCity.getSelectedItem()).getValue();
                etSJHCorresCityError.setVisibility(View.GONE);
                if (cbSJHSameAsCorrespondence.isChecked()) {
                    int corrSelectedStatePos = ((RowData) spSJHCorresState.getSelectedItem()).getPosition();
                    spSJHPermState.setSelection(corrSelectedStatePos);
                    getPermanentCity(corresStateCodeStr, null, null);
                }
                if (corresCityNameStr.equalsIgnoreCase("Other")) {
                    etSJHCorresCityOther.setVisibility(View.VISIBLE);
                } else {
                    etSJHCorresCityOther.setVisibility(View.GONE);
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
                permCityCodeStr = ((RowData) spSJHPermCity.getSelectedItem()).getKey();
                permCityNameStr = ((RowData) spSJHPermCity.getSelectedItem()).getValue();
                etSJHPermCityError.setVisibility(View.GONE);
                if (permCityNameStr.equalsIgnoreCase("Other")) {
                    etSJHPermCityOther.setVisibility(View.VISIBLE);
                } else {
                    etSJHPermCityOther.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener tjhCorresStateListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                tjhCorresStateCodeStr = ((RowData) spTJHCorresState.getSelectedItem()).getKey();
                tjhCorresStateNameStr = ((RowData) spTJHCorresState.getSelectedItem()).getValue();
                etTJHCorresStateError.setVisibility(View.GONE);
//                if (cityFoundFlag) {
                getCorresCity(tjhCorresStateCodeStr, getDematSecondJointHolderCity(), SecondJoint);
//                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener tjhPermStateListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                tjhPermStateCodeStr = ((RowData) spTJHPermState.getSelectedItem()).getKey();
                tjhPermStateNameStr = ((RowData) spTJHPermState.getSelectedItem()).getValue();
                etTJHPermStateError.setVisibility(View.GONE);
//                if (cityFoundFlag) {
                getPermanentCity(tjhPermStateCodeStr, getDematSecondJointHolderCityPerm(), SecondJoint);
//                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener tjhCorresCityListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                tjhCorresCityCodeStr = ((RowData) spTJHCorresCity.getSelectedItem()).getKey();
                tjhCorresCityNameStr = ((RowData) spTJHCorresCity.getSelectedItem()).getValue();
                etSJHCorresCityError.setVisibility(View.GONE);
                if (cbTJHSameAsCorrespondence.isChecked()) {
                    int corrSelectedStatePos = ((RowData) spTJHCorresState.getSelectedItem()).getPosition();
                    spTJHPermState.setSelection(corrSelectedStatePos);
                    getPermanentCity(tjhCorresStateCodeStr, null, null);
                }
                if (tjhCorresCityNameStr.equalsIgnoreCase("Other")) {
                    etTJHCorresCityOther.setVisibility(View.VISIBLE);
                } else {
                    etTJHCorresCityOther.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener tjhPermCityListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                tjhPermCityCodeStr = ((RowData) spTJHPermCity.getSelectedItem()).getKey();
                tjhPermCityNameStr = ((RowData) spTJHPermCity.getSelectedItem()).getValue();
                etTJHPermCityError.setVisibility(View.GONE);
                if (tjhPermCityNameStr.equalsIgnoreCase("Other")) {
                    etTJHPermCityOther.setVisibility(View.VISIBLE);
                } else {
                    etTJHPermCityOther.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener nomineeStateListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                nomineeStateCodeStr = ((RowData) spNomineeCorresState.getSelectedItem()).getKey();
                nomineeStateNameStr = ((RowData) spNomineeCorresState.getSelectedItem()).getValue();
                etNomineeCorresStateError.setVisibility(View.GONE);
                //if (nomineeCityFoundFlag) {
                getNomineeCity(nomineeStateCodeStr, nomineeCitySaved, Nominee);
                //}
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener nomineeGuardianStateListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                nomineeGuardianStateCodeStr = ((RowData) spNomineeGuardianCorresState.getSelectedItem()).getKey();
                nomineeGuardianStateNameStr = ((RowData) spNomineeGuardianCorresState.getSelectedItem()).getValue();
                etNomineeGuardianCorresStateError.setVisibility(View.GONE);
                //if (nomineeCityFoundFlag) {
                getNomineeCity(nomineeGuardianStateCodeStr, nomineeGuardianCitySaved, NomineeGuardian);
                //}
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener nomineeCityListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                nomineeCityCodeStr = ((RowData) spNomineeCorresCity.getSelectedItem()).getKey();
                nomineeCityNameStr = ((RowData) spNomineeCorresCity.getSelectedItem()).getValue();
                etNomineeCorresCityError.setVisibility(View.GONE);
                if (nomineeCityNameStr.equalsIgnoreCase("Other")) {
                    etNomineeCorresCityOther.setVisibility(View.VISIBLE);
                } else {
                    etNomineeCorresCityOther.setVisibility(View.GONE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener nomineeGuardianCityListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            try {
                nomineeGuardianCityCodeStr = ((RowData) spNomineeGuardianCorresCity.getSelectedItem()).getKey();
                nomineeGuardianCityNameStr = ((RowData) spNomineeGuardianCorresCity.getSelectedItem()).getValue();
                etNomineeGuardianCorresCityError.setVisibility(View.GONE);
                if (nomineeGuardianCityNameStr.equalsIgnoreCase("Other")) {
                    etNomineeGuardianCorresCityOther.setVisibility(View.VISIBLE);
                } else {
                    etNomineeGuardianCorresCityOther.setVisibility(View.GONE);
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
            try {
                Spinner spinner = (Spinner) parent;
                if (spinner.getId() == R.id.spSJHResidentStatus) {
                    etSJHResidentStatusError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spSJHTitle) {
                    etSJHTitleError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spSJHFatherSpouseSel) {
                    etSJHFatherSpouseSelError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spSJHAddProofCorres) {
                    etSJHAddProofCorresError.setVisibility(View.GONE);
                    if (cbSJHSameAsCorrespondence.isChecked()) {
                        int addProof = ((RowData) spSJHAddProofCorres.getSelectedItem()).getPosition();
                        spSJHAddProofPerm.setSelection(addProof);
                    }
                } else if (spinner.getId() == R.id.spSJHCorresCountry) {
                    etSJHCorresCountryError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spSJHAddProofPerm) {
                    etSJHAddProofPermError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spSJHPermCountry) {
                    etSJHPermCountryError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spSJHIncome) {
                    etSJHIncomeError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spSJHOccupation) {
                    etSJHOccupationError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spTJHResidentStatus) {
                    etTJHResidentStatusError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spTJHTitle) {
                    etTJHTitleError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spTJHFatherSpouseSel) {
                    etTJHFatherSpouseSelError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spTJHAddProofCorres) {
                    etTJHAddProofCorresError.setVisibility(View.GONE);
                    if (cbTJHSameAsCorrespondence.isChecked()) {
                        int addProof = ((RowData) spTJHAddProofCorres.getSelectedItem()).getPosition();
                        spTJHAddProofPerm.setSelection(addProof);
                    }
                } else if (spinner.getId() == R.id.spTJHCorresCountry) {
                    etTJHCorresCountryError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spTJHAddProofPerm) {
                    etTJHAddProofPermError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spTJHPermCountry) {
                    etTJHPermCountryError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spTJHIncome) {
                    etTJHIncomeError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spTJHOccupation) {
                    etTJHOccupationError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spNomineeGuardianAge) {
                    etNomineeGuardianAgeError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spNomineeCorresCountry) {
                    etNomineeCorresCountryError.setVisibility(View.GONE);
                } else if (spinner.getId() == R.id.spNomineeGuardianCorresCountry) {
                    etNomineeGuardianCorresCountryError.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void setUpView(View jointHolderNomineeView) {
        scrollview = (ScrollView) jointHolderNomineeView.findViewById(R.id.scrollview);
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
        spNumberOfJointHolder = (Spinner) jointHolderNomineeView.findViewById(R.id.spNumberOfJointHolder);
        btnSecondHolder = (Button) jointHolderNomineeView.findViewById(R.id.btnSecondHolder);
        btnThirdHolder = (Button) jointHolderNomineeView.findViewById(R.id.btnThirdHolder);
//        btnSaveJointHolder = (Button) jointHolderNomineeView.findViewById(R.id.btnSaveJointHolder);
//        btnSaveNominee = (Button) jointHolderNomineeView.findViewById(R.id.btnSaveNominee);
        btnSaveJointNominee = (Button) jointHolderNomineeView.findViewById(R.id.btnSaveJointNominee);
        rlSecondJointHolder = (RelativeLayout) jointHolderNomineeView.findViewById(R.id.rlSecondJointHolder);
        rlThirdJointHolder = (RelativeLayout) jointHolderNomineeView.findViewById(R.id.rlThirdJointHolder);
        etNumberOfJointHolderError = (TextView) jointHolderNomineeView.findViewById(R.id.etNumberOfJointHolderError);
        llNomineeQuestion = (LinearLayout) jointHolderNomineeView.findViewById(R.id.llNomineeQuestion);
        llJointHolderQuestion = (LinearLayout) jointHolderNomineeView.findViewById(R.id.llJointHolderQuestion);
        llNumberOfJointHolder = (LinearLayout) jointHolderNomineeView.findViewById(R.id.llNumberOfJointHolder);
        llNomineeDetail = (LinearLayout) jointHolderNomineeView.findViewById(R.id.llNomineeDetail);
        llNomineeGuardian = (LinearLayout) jointHolderNomineeView.findViewById(R.id.llNomineeGuardian);
        llJointHolderDetail = (LinearLayout) jointHolderNomineeView.findViewById(R.id.llJointHolderDetail);
//        cbNomineeYes = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbNomineeYes);
//        cbNomineeNo = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbNomineeNo);
//        cbIndividual = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbIndividual);
//        cbJoint = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbJoint);
        toggleNominee = (ToggleButton) jointHolderNomineeView.findViewById(R.id.toggleNominee);
        toggleJointHolder = (ToggleButton) jointHolderNomineeView.findViewById(R.id.toggleJointHolder);
        tvNomineeJointTitle = (TextView) jointHolderNomineeView.findViewById(R.id.tvNomineeJointTitle);

        btnSecondHolder.setOnClickListener(this);
        btnThirdHolder.setOnClickListener(this);
//        btnSaveJointHolder.setOnClickListener(this);
//        btnSaveNominee.setOnClickListener(this);
        btnSaveJointNominee.setOnClickListener(this);
        toggleNominee.setOnClickListener(this);
        toggleJointHolder.setOnClickListener(this);

//        cbNomineeYes.setOnCheckedChangeListener(this);
//        cbNomineeNo.setOnCheckedChangeListener(this);
//        cbIndividual.setOnCheckedChangeListener(this);
//        cbJoint.setOnCheckedChangeListener(this);
        spNumberOfJointHolder.setOnItemSelectedListener(numberOfJointHolder);

        //Nominee
        spNomineeCorresCountry = (Spinner) jointHolderNomineeView.findViewById(R.id.spNomineeCorresCountry);
        spNomineeCorresState = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spNomineeCorresState);
        spNomineeCorresCity = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spNomineeCorresCity);
        spNomineesRelationship = (Spinner) jointHolderNomineeView.findViewById(R.id.spNomineesRelationship);
        spNomineesGuardianRelationship = (Spinner) jointHolderNomineeView.findViewById(R.id.spNomineesGuardianRelationship);

        etNomineeFirstName = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeFirstName);
        etNomineeMiddleName = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeMiddleName);
        etNomineeLastName = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeLastName);
        etNomineePAN = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineePAN);
        etNomineeAadhar = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeAadhar);
        etNomineeBldgNameCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeBldgNameCorres);
        etNomineeFlatNoCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeFlatNoCorres);
        etNomineeAreaCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeAreaCorres);
        etNomineeCorresState = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeCorresState);
        etNomineeCorresCity = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeCorresCity);
        etNomineePincodeCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineePincodeCorres);
        etNomineeCorresCityOther = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeCorresCityOther);
        etNomineeSTDNo = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeSTDNo);
        etNomineeTelNo = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeTelNo);
        etNomineeDOB = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeDOB);
        etNomineeDOBRequestFocus = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeDOBRequestFocus);
        etNomineesRelationship = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineesRelationship);
//        etNomineePrimaryApplicantsRelationship = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineePrimaryApplicantsRelationship);
        etNomineeEmail = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeEmail);

        cbNomineeSameAsPrimaryCorrespondence = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbNomineeSameAsPrimaryCorrespondence);

        etNomineeCorresStateError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeCorresStateError);
        etNomineeCorresCityError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeCorresCityError);
        etNomineeCorresCountryError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeCorresCountryError);
        etNomineePincodeCorresError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineePincodeCorresError);
        etNomineeEmailError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeEmailError);
        etNomineeGuardianEmailError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianEmailError);
        etNomineePANError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineePANError);
        tvNomineeAadharError = (TextView) jointHolderNomineeView.findViewById(R.id.tvNomineeAadharError);
        etNomineeGuardianPincodeCorresError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianPincodeCorresError);
        etNomineesRelationshipError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineesRelationshipError);
        etNomineesGuardianRelationshipError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineesGuardianRelationshipError);
        inputLayoutNomineeFirstName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeFirstName);
        inputLayoutNomineeMiddleName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeMiddleName);
        inputLayoutNomineeLastName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeLastName);
        inputLayoutNomineePAN = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineePAN);
        inputLayoutNomineeAadhaar = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeAadhaar);
        inputLayoutNomineeBldgNameCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeBldgNameCorres);
        inputLayoutNomineeFlatNoCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeFlatNoCorres);
        inputLayoutNomineeAreaCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeAreaCorres);
        inputLayoutNomineePincodeCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineePincodeCorres);
        inputLayoutNomineeCorresCityOther = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeCorresCityOther);
        inputLayoutNomineeSTD = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeSTD);
        inputLayoutNomineeTelNo = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeTelNo);
        inputLayoutNomineeDob = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeDob);
        inputLayoutNomineesRelationship = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineesRelationship);
//        inputLayoutNomineePrimaryApplicantsRelationship = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineePrimaryApplicantsRelationship);
        inputLayoutNomineeEmail = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeEmail);


        //Nominee guardian
        spNomineeGuardianCorresCountry = (Spinner) jointHolderNomineeView.findViewById(R.id.spNomineeGuardianCorresCountry);
        spNomineeGuardianAge = (Spinner) jointHolderNomineeView.findViewById(R.id.spNomineeGuardianAge);

        spNomineeGuardianCorresState = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spNomineeGuardianCorresState);
        spNomineeGuardianCorresCity = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spNomineeGuardianCorresCity);

        etNomineeGuardianFirstName = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianFirstName);
        etNomineeGuardianMiddleName = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianMiddleName);
        etNomineeGuardianLastName = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianLastName);
        etNomineesGuardianRelationship = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineesGuardianRelationship);
        etNomineeGuardianBldgNameCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianBldgNameCorres);
        etNomineeGuardianFlatNoCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianFlatNoCorres);
        etNomineeGuardianAreaCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianAreaCorres);
        etNomineeGuardianCorresState = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianCorresState);
        etNomineeGuardianCorresCity = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianCorresCity);
        etNomineeGuardianPincodeCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianPincodeCorres);
        etNomineeGuardianCorresCityOther = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianCorresCityOther);
        etNomineeGuardianSTDNo = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianSTDNo);
        etNomineeGuardianTelNo = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianTelNo);
        etNomineeGuardianEmail = (EditText) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianEmail);

        cbNomineeGuardianSameAsPrimaryCorrespondence = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbNomineeGuardianSameAsPrimaryCorrespondence);

        etNomineeGuardianCorresStateError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianCorresStateError);
        etNomineeGuardianCorresCityError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianCorresCityError);
        etNomineeGuardianCorresCountryError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianCorresCountryError);
        etNomineeGuardianAgeError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianAgeError);

        inputLayoutNomineeGuardianFirstName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeGuardianFirstName);
        inputLayoutNomineeGuardianMiddleName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeGuardianMiddleName);
        inputLayoutNomineeGuardianLastName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeGuardianLastName);
        inputLayoutNomineesGuardianRelationship = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineesGuardianRelationship);
        inputLayoutNomineeGuardianBldgNameCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeGuardianBldgNameCorres);
        inputLayoutNomineeGuardianFlatNoCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeGuardianFlatNoCorres);
        inputLayoutNomineeGuardianAreaCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeGuardianAreaCorres);
        inputLayoutNomineeGuardianPincodeCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeGuardianPincodeCorres);
        inputLayoutNomineeGuardianCorresCityOther = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeGuardianCorresCityOther);
        inputLayoutNomineeGuardianSTD = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeGuardianSTD);
        inputLayoutNomineeGuardianTelNo = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeGuardianTelNo);
        inputLayoutNomineeGuardianEmail = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutNomineeGuardianEmail);

        //Second holder
        etSJHFirstName = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHFirstName);
        etSJHMiddleName = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHMiddleName);
        etSJHLastName = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHLastName);
        etSJHFatherSpouseFirstName = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHFatherSpouseFirstName);
        etSJHFatherSpouseMidName = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHFatherSpouseMidName);
        etSJHFatherSpouseLastName = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHFatherSpouseLastName);
        etSJHDOB = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHDOB);
        etSJHPAN = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHPAN);
        etSJHAadhar = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHAadhar);
        etSJHMobile = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHMobile);
        etSJHEmail = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHEmail);
        etSJHSTDNo = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHSTDNo);
        etSJHTelNo = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHTelNo);
        etSJHBldgNameCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHBldgNameCorres);
        etSJHFlatNoCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHFlatNoCorres);
        etSJHAreaCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHAreaCorres);
        etSJHPincodeCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHPincodeCorres);
        etSJHCorresCityOther = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHCorresCityOther);
        etSJHBldgNamePerm = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHBldgNamePerm);
        etSJHFlatNamePerm = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHFlatNamePerm);
        etSJHAreaPerm = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHAreaPerm);
        etSJHPincodePerm = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHPincodePerm);
        etSJHCorresState = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHCorresState);
        etSJHCorresCity = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHCorresCity);
        etSJHPermState = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHPermState);
        etSJHPermCity = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHPermCity);
        etSJHAadharError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHAadharError);
        etSJHMobileError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHMobileError);
        etSJHEmailError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHEmailError);
        etSJHPincodePermError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHPincodePermError);
        etSJHPincodeCorresError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHPincodeCorresError);
        etSJHPANError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHPANError);

        spSJHResidentStatus = (Spinner) jointHolderNomineeView.findViewById(R.id.spSJHResidentStatus);
        spSJHTitle = (Spinner) jointHolderNomineeView.findViewById(R.id.spSJHTitle);
        spSJHFatherSpouseSel = (Spinner) jointHolderNomineeView.findViewById(R.id.spSJHFatherSpouseSel);
        spSJHAddProofCorres = (Spinner) jointHolderNomineeView.findViewById(R.id.spSJHAddProofCorres);
        spSJHAddProofPerm = (Spinner) jointHolderNomineeView.findViewById(R.id.spSJHAddProofPerm);
        spSJHCorresState = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spSJHCorresState);
        spSJHCorresCity = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spSJHCorresCity);
        spSJHCorresCountry = (Spinner) jointHolderNomineeView.findViewById(R.id.spSJHCorresCountry);
        spSJHPermState = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spSJHPermState);
        spSJHPermCity = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spSJHPermCity);
        spSJHPermCountry = (Spinner) jointHolderNomineeView.findViewById(R.id.spSJHPermCountry);
        spSJHIncome = (Spinner) jointHolderNomineeView.findViewById(R.id.spSJHIncome);
        spSJHOccupation = (Spinner) jointHolderNomineeView.findViewById(R.id.spSJHOccupation);

        // cbSJHMale = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbSJHMale);
        // cbSJHFemale = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbSJHFemale);
        //  cbSJHMarried = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbSJHMarried);
        //  cbSJHUnMarried = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbSJHUnMarried);
        cbSJHSameAsPrimaryCorrespondence = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbSJHSameAsPrimaryCorrespondence);
        cbSJHSameAsCorrespondence = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbSJHSameAsCorrespondence);
        //  cbSJHPoliticalyNo = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbSJHPoliticalyNo);
        //cbSJHPoliticalyYes = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbSJHPoliticalyYes);
        // cbSJHRelatedPoliticalNo = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbSJHRelatedPoliticalNo);
        //  cbSJHRelatedPoliticalYes = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbSJHRelatedPoliticalYes);

        inputLayoutSJHFirstName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHFirstName);
        inputLayoutSJHMiddleName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHMiddleName);
        inputLayoutSJHLastName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHLastName);
        inputLayoutSJHFatherSpouseFirstName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHFatherSpouseFirstName);
        inputLayoutSJHFatherSpouseMidName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHFatherSpouseMidName);
        inputLayoutSJHFatherSpouseLastName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHFatherSpouseLastName);
        inputLayoutSJHDob = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHDob);
        inputLayoutSJHPAN = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHPAN);
        inputLayoutSJHAadhar = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHAadhar);
        inputLayoutSJHMobile = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHMobile);
        inputLayoutSJHEmail = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHEmail);
        inputLayoutSJHSTD = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHSTD);
        inputLayoutSJHTelNo = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHTelNo);
        inputLayoutSJHBldgNameCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHBldgNameCorres);
        inputLayoutSJHFlatNoCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHFlatNoCorres);
        inputLayoutSJHAreaCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHAreaCorres);
        inputLayoutSJHCorresCityOther = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHCorresCityOther);
        inputLayoutSJHPincodeCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHPincodeCorres);
        inputLayoutSJHBldgNamePerm = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHBldgNamePerm);
        inputLayoutSJHFlatNamePerm = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHFlatNamePerm);
        inputLayoutSJHAreaPerm = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHAreaPerm);
        inputLayoutSJHPermCityOther = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHPermCityOther);
        inputLayoutSJHPincodePerm = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutSJHPincodePerm);

        etSJHResidentStatusError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHResidentStatusError);
        etSJHTitleError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHTitleError);
        etSJHGenderError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHGenderError);
        etSJHFatherSpouseSelError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHFatherSpouseSelError);
        etSJHMaritalError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHMaritalError);
        etSJHAddProofCorresError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHAddProofCorresError);
        etSJHAddProofPermError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHAddProofPermError);
        etSJHCorresStateError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHCorresStateError);
        etSJHCorresCityError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHCorresCityError);
        etSJHCorresCountryError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHCorresCountryError);
        etSJHPermStateError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHPermStateError);
        etSJHPermCityError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHPermCityError);
        etSJHPermCountryError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHPermCountryError);
        etSJHIncomeError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHIncomeError);
        etSJHOccupationError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHOccupationError);
        etSJHSTDTelError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHSTDTelError);

        //Third holder
        etTJHFirstName = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHFirstName);
        etTJHMiddleName = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHMiddleName);
        etTJHLastName = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHLastName);
        etTJHFatherSpouseFirstName = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHFatherSpouseFirstName);
        etTJHFatherSpouseMidName = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHFatherSpouseMidName);
        etTJHFatherSpouseLastName = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHFatherSpouseLastName);
        etTJHDOB = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHDOB);
        etTJHPAN = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHPAN);
        etTJHAadhar = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHAadhar);
        etTJHMobile = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHMobile);
        etTJHEmail = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHEmail);
        etTJHSTDNo = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHSTDNo);
        etTJHTelNo = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHTelNo);
        etTJHBldgNameCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHBldgNameCorres);
        etTJHFlatNoCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHFlatNoCorres);
        etTJHAreaCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHAreaCorres);
        etTJHCorresCityOther = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHCorresCityOther);
        etTJHPincodeCorres = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHPincodeCorres);
        etTJHBldgNamePerm = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHBldgNamePerm);
        etTJHFlatNamePerm = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHFlatNamePerm);
        etTJHAreaPerm = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHAreaPerm);
        etSJHPermCityOther = (EditText) jointHolderNomineeView.findViewById(R.id.etSJHPermCityOther);
        etTJHPermCityOther = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHPermCityOther);
        etTJHPincodePerm = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHPincodePerm);
        etTJHCorresState = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHCorresState);
        etTJHCorresCity = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHCorresCity);
        etTJHPermState = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHPermState);
        etTJHPermCity = (EditText) jointHolderNomineeView.findViewById(R.id.etTJHPermCity);

        spTJHResidentStatus = (Spinner) jointHolderNomineeView.findViewById(R.id.spTJHResidentStatus);
        spTJHTitle = (Spinner) jointHolderNomineeView.findViewById(R.id.spTJHTitle);
        spTJHFatherSpouseSel = (Spinner) jointHolderNomineeView.findViewById(R.id.spTJHFatherSpouseSel);
        spTJHAddProofCorres = (Spinner) jointHolderNomineeView.findViewById(R.id.spTJHAddProofCorres);
        spTJHAddProofPerm = (Spinner) jointHolderNomineeView.findViewById(R.id.spTJHAddProofPerm);
        spTJHCorresState = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spTJHCorresState);
        spTJHCorresCity = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spTJHCorresCity);
        spTJHCorresCountry = (Spinner) jointHolderNomineeView.findViewById(R.id.spTJHCorresCountry);
        spTJHPermState = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spTJHPermState);
        spTJHPermCity = (SearchableSpinner) jointHolderNomineeView.findViewById(R.id.spTJHPermCity);
        spTJHPermCountry = (Spinner) jointHolderNomineeView.findViewById(R.id.spTJHPermCountry);
        spTJHIncome = (Spinner) jointHolderNomineeView.findViewById(R.id.spTJHIncome);
        spTJHOccupation = (Spinner) jointHolderNomineeView.findViewById(R.id.spTJHOccupation);

        // cbTJHMale = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbTJHMale);
        //  cbTJHFemale = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbTJHFemale);
        //  cbTJHMarried = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbTJHMarried);
        //  cbTJHUnMarried = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbTJHUnMarried);
        cbTJHSameAsPrimaryCorrespondence = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbTJHSameAsPrimaryCorrespondence);
        cbTJHSameAsCorrespondence = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbTJHSameAsCorrespondence);
        //  cbTJHPoliticalyNo = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbTJHPoliticalyNo);
        //  cbTJHPoliticalyYes = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbTJHPoliticalyYes);
        // cbTJHRelatedPoliticalNo = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbTJHRelatedPoliticalNo);
        // cbTJHRelatedPoliticalYes = (CheckBox) jointHolderNomineeView.findViewById(R.id.cbTJHRelatedPoliticalYes);

        inputLayoutTJHFirstName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHFirstName);
        inputLayoutTJHMiddleName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHMiddleName);
        inputLayoutTJHLastName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHLastName);
        inputLayoutTJHFatherSpouseFirstName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHFatherSpouseFirstName);
        inputLayoutTJHFatherSpouseMidName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHFatherSpouseMidName);
        inputLayoutTJHFatherSpouseLastName = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHFatherSpouseLastName);
        inputLayoutTJHDob = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHDob);
        inputLayoutTJHPAN = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHPAN);
        inputLayoutTJHAadhar = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHAadhar);
        inputLayoutTJHMobile = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHMobile);
        inputLayoutTJHEmail = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHEmail);
        inputLayoutTJHSTD = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHSTD);
        inputLayoutTJHTelNo = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHTelNo);
        inputLayoutTJHBldgNameCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHBldgNameCorres);
        inputLayoutTJHFlatNoCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHFlatNoCorres);
        inputLayoutTJHAreaCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHAreaCorres);
        inputLayoutTJHCorresCityOther = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHCorresCityOther);
        inputLayoutTJHPincodeCorres = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHPincodeCorres);
        inputLayoutTJHBldgNamePerm = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHBldgNamePerm);
        inputLayoutTJHFlatNamePerm = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHFlatNamePerm);
        inputLayoutTJHAreaPerm = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHAreaPerm);
        inputLayoutTJHPermCityOther = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHPermCityOther);
        inputLayoutTJHPincodePerm = (TextInputLayout) jointHolderNomineeView.findViewById(R.id.inputLayoutTJHPincodePerm);

        etTJHResidentStatusError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHResidentStatusError);
        etTJHTitleError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHTitleError);
        etTJHGenderError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHGenderError);
        etTJHFatherSpouseSelError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHFatherSpouseSelError);
        etTJHmaritalError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHmaritalError);
        etTJHAddProofCorresError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHAddProofCorresError);
        etTJHAddProofPermError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHAddProofPermError);
        etTJHCorresStateError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHCorresStateError);
        etTJHCorresCountryError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHCorresCountryError);
        etTJHCorresCityError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHCorresCityError);
        etTJHPermStateError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHPermStateError);
        etTJHPermCountryError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHPermCountryError);
        etTJHPermCityError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHPermCityError);
        etTJHIncomeError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHIncomeError);
        etTJHOccupationError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHOccupationError);
        etTJHAadharError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHAadharError);
        etTJHMobileError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHMobileError);
        etTJHEmailError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHEmailError);
        etTJHPincodePermError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHPincodePermError);
        etTJHPincodeCorresError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHPincodeCorresError);
        etTJHPANError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHPANError);
        etTJHSTDTelError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHSTDTelError);

        etNomineeFirstNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeFirstNameError);
        etNomineeGuardianFirstNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianFirstNameError);
        etNomineeMiddleNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeMiddleNameError);
        etNomineeGuardianMiddleNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianMiddleNameError);
        etNomineeLastNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeLastNameError);
        etNomineeGuardianLastNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianLastNameError);
        etSJHFirstNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHFirstNameError);
        etSJHMiddleNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHMiddleNameError);
        etSJHLastNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHLastNameError);
        etSJHFatherSpouseFirstNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHFatherSpouseFirstNameError);
        etSJHFatherSpouseMidNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHFatherSpouseMidNameError);
        etSJHFatherSpouseLastNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etSJHFatherSpouseLastNameError);
        etTJHFirstNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHFirstNameError);
        etTJHMiddleNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHMiddleNameError);
        etTJHLastNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHLastNameError);
        etTJHFatherSpouseFirstNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHFatherSpouseFirstNameError);
        etTJHFatherSpouseMidNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHFatherSpouseMidNameError);
        etTJHFatherSpouseLastNameError = (TextView) jointHolderNomineeView.findViewById(R.id.etTJHFatherSpouseLastNameError);
        etNomineeSTDTelError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeSTDTelError);
        etNomineeGuardianSTDTelError = (TextView) jointHolderNomineeView.findViewById(R.id.etNomineeGuardianSTDTelError);

        //Toggle Button

        toggleSJHPoliticaly = (ToggleButton) jointHolderNomineeView.findViewById(R.id.toggleSJHPoliticaly);
        toggleSJHRelatedPolitical = (ToggleButton) jointHolderNomineeView.findViewById(R.id.toggleSJHRelatedPolitical);
        toggleTJHPoliticaly = (ToggleButton) jointHolderNomineeView.findViewById(R.id.toggleTJHPoliticaly);
        toggleTJHRelatedPolitical = (ToggleButton) jointHolderNomineeView.findViewById(R.id.toggleTJHRelatedPolitical);
        toggleSJHPoliticaly.setOnClickListener(this);
        toggleSJHRelatedPolitical.setOnClickListener(this);
        toggleTJHPoliticaly.setOnClickListener(this);
        toggleTJHRelatedPolitical.setOnClickListener(this);

        //Radio Button

        rbSJHMale = (RadioButton) jointHolderNomineeView.findViewById(R.id.rbSJHMale);
        rbSJHFemale = (RadioButton) jointHolderNomineeView.findViewById(R.id.rbSJHFemale);
        rbTJHMale = (RadioButton) jointHolderNomineeView.findViewById(R.id.rbTJHMale);
        rbTJHFemale = (RadioButton) jointHolderNomineeView.findViewById(R.id.rbTJHFemale);
        rbSJHMale.setOnClickListener(this);
        rbSJHFemale.setOnClickListener(this);
        rbTJHMale.setOnClickListener(this);
        rbTJHFemale.setOnClickListener(this);

        rbSJHMarried = (RadioButton) jointHolderNomineeView.findViewById(R.id.rbSJHMarried);
        rbSJHUnMarried = (RadioButton) jointHolderNomineeView.findViewById(R.id.rbSJHUnMarried);
        rbTJHMarried = (RadioButton) jointHolderNomineeView.findViewById(R.id.rbTJHMarried);
        rbTJHUnMarried = (RadioButton) jointHolderNomineeView.findViewById(R.id.rbTJHUnMarried);
        rbSJHMarried.setOnClickListener(this);
        rbSJHUnMarried.setOnClickListener(this);
        rbTJHMarried.setOnClickListener(this);
        rbTJHUnMarried.setOnClickListener(this);

        etSJHPAN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10), new InputFilter.AllCaps()});
        etTJHPAN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10), new InputFilter.AllCaps()});
        etNomineePAN.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10), new InputFilter.AllCaps()});

        countryList = loadCountryArray(getContext());
        arrAdapCountry = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.country))));
        /*arrAdapCountry = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item,
                countryList);
        arrAdapCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNomineeGuardianCorresCountry.setAdapter(arrAdapCountry);
        spNomineeCorresCountry.setAdapter(arrAdapCountry);
        spTJHCorresCountry.setAdapter(arrAdapCountry);
        spTJHPermCountry.setAdapter(arrAdapCountry);
        spSJHCorresCountry.setAdapter(arrAdapCountry);
        spSJHPermCountry.setAdapter(arrAdapCountry);*/

        if (getAppType().equalsIgnoreCase(DIRECT) && !getIsAadharVerified() && !getInterestedInMF()) {
            llJointHolderQuestion.setVisibility(View.VISIBLE);
            tvNomineeJointTitle.setText("Nominee & Joint Holder Details");
        } else {
            llJointHolderQuestion.setVisibility(View.GONE);
            tvNomineeJointTitle.setText("Nominee Details");
        }
        etSJHDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomineeDOBClicked = false;
                try {

                    if (!isBlank(etSJHDOB.getText().toString())) {
                      /*  Date dob = EkycAppUtil.getDate(etSJHDOB.getText().toString(), "dd/MM/yyyy");
                        fromDatePickerDialog.updateDate(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));*/
                        showDefalutDate = false;
                        date = etSJHDOB.getText().toString();

                    } else {
                        showDefalutDate = true;
                        date = "";

                    }
                    Calendar maxCal = Calendar.getInstance();
                    maxCal.set(Calendar.YEAR, maxCal.get(Calendar.YEAR) - 18);
                    CustomDatePickerDialog customDatePickerDialog = new CustomDatePickerDialog(mContext, showDefalutDate, date, maxCal.getTime(), new DatePickerListener() {
                        @Override
                        public void submitDatePicker(String date) {

                            if (!date.equals("")) {
                                Calendar minAdultAge = new GregorianCalendar();
                                minAdultAge.add(Calendar.YEAR, -18);
                                Date dob = EkycAppUtil.getDate(date, "dd/MM/yyyy");
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                                if (minAdultAge.before(newDate)) {
                                    commonMsgDialog(mContext, getResources().getString(R.string.minor_age_joint_note), false, "");
                                    etSJHDOB.setText("");
                                } else {
                                    etSJHDOB.setText(date);
                                }
                            }
                        }
                    });
                    customDatePickerDialog.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // fromDatePickerDialog.show();
                removeKeyboard(mContext, etSJHPAN);
            }
        });
        etTJHDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomineeDOBClicked = false;
                try {
                    if (!isBlank(etTJHDOB.getText().toString())) {
                      /*  Date dob = EkycAppUtil.getDate(etTJHDOB.getText().toString(), "dd/MM/yyyy");
                        fromDatePickerDialog.updateDate(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));*/
                        showDefalutDate = false;
                        date = etTJHDOB.getText().toString();
                    } else {
                        showDefalutDate = true;
                        date = "";
                    }
                    Calendar maxCal = Calendar.getInstance();
                    maxCal.set(Calendar.YEAR, maxCal.get(Calendar.YEAR) - 18);
                    CustomDatePickerDialog customDatePickerDialog = new CustomDatePickerDialog(mContext, showDefalutDate, date, maxCal.getTime(), new DatePickerListener() {
                        @Override
                        public void submitDatePicker(String date) {

                            if (!date.equals("")) {
                                Calendar minAdultAge = new GregorianCalendar();
                                minAdultAge.add(Calendar.YEAR, -18);
                                Date dob = EkycAppUtil.getDate(date, "dd/MM/yyyy");
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                                if (minAdultAge.before(newDate)) {
                                    commonMsgDialog(mContext, getResources().getString(R.string.minor_age_joint_note), false, "");
                                    etTJHDOB.setText("");
                                } else {
                                    etTJHDOB.setText(date);
                                }
                            }
                        }
                    });
                    customDatePickerDialog.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // fromDatePickerDialog.show();
                removeKeyboard(mContext, etTJHPAN);
            }
        });
        etNomineeDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomineeDOBClicked = true;
                try {
                    if (!isBlank(etNomineeDOB.getText().toString())) {
                       /* Date dob = EkycAppUtil.getDate(etNomineeDOB.getText().toString(), "dd/MM/yyyy");
                        fromDatePickerDialog.updateDate(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));*/
                        showDefalutDate = false;
                        date = etNomineeDOB.getText().toString();


                    } else {
                        showDefalutDate = true;
                        date = "";
                    }

                    CustomDatePickerDialog nomieeDatePicker = new CustomDatePickerDialog(mContext, showDefalutDate, date, true, new DatePickerListener() {
                        @Override
                        public void submitDatePicker(String date) {
                            if (!date.equals("")) {
                                Calendar minAdultAge = new GregorianCalendar();
                                minAdultAge.add(Calendar.YEAR, -18);
                                Date dob = EkycAppUtil.getDate(date, "dd/MM/yyyy");
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(EkycAppUtil.getYear(dob), EkycAppUtil.getMonth(dob) - 1, EkycAppUtil.getDay(dob));
                                if (minAdultAge.before(newDate)) {
                                    adultFlag = true;
                                    llNomineeGuardian.setVisibility(View.VISIBLE);
                                } else {
                                    adultFlag = false;
                                    llNomineeGuardian.setVisibility(View.GONE);
                                }
                                etNomineeDOB.setText(date);
                            }
                        }
                    });
                    nomieeDatePicker.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//                fromDatePickerDialog.show();
                removeKeyboard(mContext, etTJHPAN);
            }
        });
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//        Calendar newCalendar = Calendar.getInstance();

       /* fromDatePickerDialog = new DatePickerDialog(mContext, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                if (nomineeDOBClicked) {
                    etNomineeDOB.setText(dateFormatter.format(newDate.getTime()));
                    if (minAdultAge.before(newDate)) {
                        adultFlag = true;
                        llNomineeGuardian.setVisibility(View.VISIBLE);
                    } else {
                        adultFlag = false;
                        llNomineeGuardian.setVisibility(View.GONE);
                    }
                }
                if (rlSecondJointHolder.getVisibility() == View.VISIBLE) {
                    etSJHDOB.setText(dateFormatter.format(newDate.getTime()));
                } else if (rlThirdJointHolder.getVisibility() == View.VISIBLE) {
                    etTJHDOB.setText(dateFormatter.format(newDate.getTime()));
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());*/

        cbSJHSameAsPrimaryCorrespondence.setOnClickListener(this);
        cbSJHSameAsCorrespondence.setOnClickListener(this);
        cbTJHSameAsPrimaryCorrespondence.setOnClickListener(this);
        cbTJHSameAsCorrespondence.setOnClickListener(this);
        cbNomineeSameAsPrimaryCorrespondence.setOnClickListener(this);
        cbNomineeGuardianSameAsPrimaryCorrespondence.setOnClickListener(this);

        arrAdapResidentStatus = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.residentStatus))));
        arrAdapOcc = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.occupation))));
        arrAdapIncome = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.income))));
        arrAdapTitle = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.title))));
        arrAdapFatherSpouseSel = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.fatherSpouseSel))));
        arrAdapCountryBirth = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.country))));
        arrAdapGuardianAge = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.ageOfGuardian))));


        spSJHResidentStatus.setOnItemSelectedListener(commonListener);
        spSJHTitle.setOnItemSelectedListener(commonListener);
        spSJHFatherSpouseSel.setOnItemSelectedListener(commonListener);
        spSJHAddProofCorres.setOnItemSelectedListener(commonListener);
        spSJHCorresCountry.setOnItemSelectedListener(commonListener);
        spSJHAddProofPerm.setOnItemSelectedListener(commonListener);
        spSJHPermCountry.setOnItemSelectedListener(commonListener);
        spSJHIncome.setOnItemSelectedListener(commonListener);
        spSJHOccupation.setOnItemSelectedListener(commonListener);
        spNomineeGuardianAge.setOnItemSelectedListener(commonListener);
        spNomineeCorresCountry.setOnItemSelectedListener(commonListener);
        spNomineeGuardianCorresCountry.setOnItemSelectedListener(commonListener);

        spTJHResidentStatus.setOnItemSelectedListener(commonListener);
        spTJHTitle.setOnItemSelectedListener(commonListener);
        spTJHFatherSpouseSel.setOnItemSelectedListener(commonListener);
        spTJHAddProofCorres.setOnItemSelectedListener(commonListener);
        spTJHCorresCountry.setOnItemSelectedListener(commonListener);
        spTJHAddProofPerm.setOnItemSelectedListener(commonListener);
        spTJHPermCountry.setOnItemSelectedListener(commonListener);
        spTJHIncome.setOnItemSelectedListener(commonListener);
        spTJHOccupation.setOnItemSelectedListener(commonListener);

        etNomineeMiddleName.addTextChangedListener(this);
        etNomineeBldgNameCorres.addTextChangedListener(this);
        etNomineeFlatNoCorres.addTextChangedListener(this);
        etNomineeAreaCorres.addTextChangedListener(this);
        etNomineeDOB.addTextChangedListener(this);
        etNomineesRelationship.addTextChangedListener(this);
//        etNomineePrimaryApplicantsRelationship.addTextChangedListener(this);

        etNomineeGuardianMiddleName.addTextChangedListener(this);
        etNomineeGuardianBldgNameCorres.addTextChangedListener(this);
        etNomineeGuardianFlatNoCorres.addTextChangedListener(this);
        etNomineeGuardianAreaCorres.addTextChangedListener(this);
        etNomineesGuardianRelationship.addTextChangedListener(this);

        etSJHBldgNameCorres.addTextChangedListener(this);
        etSJHFlatNoCorres.addTextChangedListener(this);
        etSJHAreaCorres.addTextChangedListener(this);
        etSJHBldgNamePerm.addTextChangedListener(this);
        etSJHFlatNamePerm.addTextChangedListener(this);
        etSJHAreaPerm.addTextChangedListener(this);
        etTJHBldgNameCorres.addTextChangedListener(this);
        etTJHFlatNoCorres.addTextChangedListener(this);
        etTJHAreaCorres.addTextChangedListener(this);
        etTJHBldgNamePerm.addTextChangedListener(this);
        etTJHFlatNamePerm.addTextChangedListener(this);
        etTJHAreaPerm.addTextChangedListener(this);


        etNomineeFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etNomineeFirstName.getText().toString());
                if (!isBlank(etNomineeFirstName.getText().toString())) {
                    inputLayoutNomineeFirstName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etNomineeFirstName.setText("");
                        etNomineeFirstName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeFirstName.getText().toString().trim())) {
                        etNomineeFirstNameError.setVisibility(View.VISIBLE);
                        etNomineeFirstNameError.setText(ErrorMsgName);
                        etNomineeFirstName.requestFocus();
                    } else {
                        etNomineeFirstNameError.setVisibility(View.GONE);
                    }
                } else {
                    etNomineeFirstNameError.setVisibility(View.GONE);
                    inputLayoutNomineeFirstName.setErrorEnabled(false);
                }
            }
        });

        etNomineeMiddleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etNomineeMiddleName.getText().toString());
                if (!isBlank(etNomineeMiddleName.getText().toString())) {
                    inputLayoutNomineeMiddleName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etNomineeMiddleName.setText("");
                        etNomineeMiddleName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeMiddleName.getText().toString().trim())) {
                        etNomineeMiddleNameError.setVisibility(View.VISIBLE);
                        etNomineeMiddleNameError.setText(ErrorMsgName);
                        etNomineeMiddleName.requestFocus();
                    } else {
                        etNomineeMiddleNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeMiddleName.setErrorEnabled(false);
                    etNomineeMiddleNameError.setVisibility(View.GONE);
                }
            }
        });

        etNomineeLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etNomineeLastName.getText().toString());
                if (!isBlank(etNomineeLastName.getText().toString())) {
                    inputLayoutNomineeLastName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etNomineeLastName.setText("");
                        etNomineeLastName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeLastName.getText().toString().trim())) {
                        etNomineeLastNameError.setVisibility(View.VISIBLE);
                        etNomineeLastNameError.setText(ErrorMsgName);
                        etNomineeLastName.requestFocus();
                    } else {
                        etNomineeLastNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeLastName.setErrorEnabled(false);
                    etNomineeLastNameError.setVisibility(View.GONE);
                }
            }
        });

        etNomineePincodeCorres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etNomineePincodeCorres.getText().toString())) {
                    inputLayoutNomineePincodeCorres.setErrorEnabled(false);
                    if (etNomineePincodeCorres.length() < 6) {
                        etNomineePincodeCorresError.setVisibility(View.VISIBLE);
                        etNomineePincodeCorresError.setText("Invalid pincode.");
                        etNomineePincodeCorres.requestFocus();
                    } else {
                        etNomineePincodeCorresError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineePincodeCorres.setErrorEnabled(false);
                    etNomineePincodeCorresError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNomineePAN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etNomineePAN.getText().toString())) {
                    inputLayoutNomineePAN.setErrorEnabled(false);
                    if (!isValidPannumber((etNomineePAN.getText().toString()))) {
                        etNomineePANError.setVisibility(View.VISIBLE);
                        etNomineePANError.setText("Invalid PAN number");
                        etNomineePAN.requestFocus();
                    } else {
                        etNomineePANError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineePAN.setErrorEnabled(false);
                    etNomineePANError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNomineeEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etNomineeEmail.getText().toString())) {
                    inputLayoutNomineeEmail.setErrorEnabled(false);
                    if (!isValidEmail(etNomineeEmail.getText())) {
                        etNomineeEmailError.setVisibility(View.VISIBLE);
                        etNomineeEmailError.setText("Invalid email id");
                        etNomineeEmail.requestFocus();
                    } else {
                        etNomineeEmailError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeEmail.setErrorEnabled(false);
                    etNomineeEmailError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNomineeSTDNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etNomineeSTDNo.getText().toString())) {
                    inputLayoutNomineeSTD.setErrorEnabled(false);
                    if (etNomineeSTDNo.getText().toString().length() < 3) {
                        etNomineeSTDTelError.setVisibility(View.VISIBLE);
                        etNomineeSTDTelError.setText("Invalid STD code");
                        etNomineeSTDNo.requestFocus();
                    } else {
                        etNomineeSTDTelError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeSTD.setErrorEnabled(false);
                    etNomineeSTDTelError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNomineeTelNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etNomineeTelNo.getText().toString())) {
                    inputLayoutNomineeTelNo.setErrorEnabled(false);
                    if (etNomineeTelNo.getText().toString().length() < 6) {
                        etNomineeSTDTelError.setVisibility(View.VISIBLE);
                        etNomineeSTDTelError.setText(Res_Telephone_number_min_11_digit);
                        etNomineeTelNo.requestFocus();
                    } else {
                        etNomineeSTDTelError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeTelNo.setErrorEnabled(false);
                    etNomineeSTDTelError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNomineesRelationship.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etNomineesRelationship.getText().toString());
                if (!isBlank(etNomineesRelationship.getText().toString())) {
                    inputLayoutNomineesRelationship.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etNomineesRelationship.setText("");
                        etNomineesRelationship.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineesRelationship.getText().toString().trim())) {
                        inputLayoutNomineesRelationship.setError(ErrorMsgName);
                        etNomineesRelationship.requestFocus();
                    } else {
                        inputLayoutNomineesRelationship.setErrorEnabled(false);
                    }
                } else {
                    inputLayoutNomineesRelationship.setErrorEnabled(false);
                }
            }
        });

        etNomineesGuardianRelationship.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etNomineesGuardianRelationship.getText().toString());
                if (!isBlank(etNomineesGuardianRelationship.getText().toString())) {
                    inputLayoutNomineesGuardianRelationship.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etNomineesGuardianRelationship.setText("");
                        etNomineesGuardianRelationship.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineesGuardianRelationship.getText().toString().trim())) {
                        inputLayoutNomineesGuardianRelationship.setError(ErrorMsgName);
                        etNomineesGuardianRelationship.requestFocus();
                    } else {
                        inputLayoutNomineesGuardianRelationship.setErrorEnabled(false);
                    }
                } else {
                    inputLayoutNomineesGuardianRelationship.setErrorEnabled(false);
                }
            }
        });

        etNomineeGuardianFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etNomineeGuardianFirstName.getText().toString());
                if (!isBlank(etNomineeGuardianFirstName.getText().toString())) {
                    inputLayoutNomineeGuardianFirstName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etNomineeGuardianFirstName.setText("");
                        etNomineeGuardianFirstName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeGuardianFirstName.getText().toString().trim())) {
                        etNomineeGuardianFirstNameError.setVisibility(View.VISIBLE);
                        etNomineeGuardianFirstNameError.setText(ErrorMsgName);
                        etNomineeGuardianFirstName.requestFocus();
                    } else {
                        etNomineeGuardianFirstNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeGuardianFirstName.setErrorEnabled(false);
                    etNomineeGuardianFirstNameError.setVisibility(View.GONE);
                }
            }
        });

        etNomineeGuardianMiddleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etNomineeGuardianMiddleName.getText().toString());
                if (!isBlank(etNomineeGuardianMiddleName.getText().toString())) {
                    inputLayoutNomineeGuardianMiddleName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etNomineeGuardianMiddleName.setText("");
                        etNomineeGuardianMiddleName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeGuardianMiddleName.getText().toString().trim())) {
                        etNomineeGuardianMiddleNameError.setVisibility(View.VISIBLE);
                        etNomineeGuardianMiddleNameError.setText(ErrorMsgName);
                        etNomineeGuardianMiddleName.requestFocus();
                    } else {
                        etNomineeGuardianMiddleNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeGuardianMiddleName.setErrorEnabled(false);
                    etNomineeGuardianMiddleNameError.setVisibility(View.GONE);
                }
            }
        });

        etNomineeGuardianLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etNomineeGuardianLastName.getText().toString());
                if (!isBlank(etNomineeGuardianLastName.getText().toString())) {
                    inputLayoutNomineeGuardianLastName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etNomineeGuardianLastName.setText("");
                        etNomineeGuardianLastName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeGuardianLastName.getText().toString().trim())) {
                        etNomineeGuardianLastNameError.setVisibility(View.VISIBLE);
                        etNomineeGuardianLastNameError.setText(ErrorMsgName);
                        etNomineeGuardianLastName.requestFocus();
                    } else {
                        etNomineeGuardianLastNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeGuardianLastName.setErrorEnabled(false);
                    etNomineeGuardianLastNameError.setVisibility(View.GONE);
                }
            }
        });

        etNomineeGuardianPincodeCorres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etNomineeGuardianPincodeCorres.getText().toString())) {
                    inputLayoutNomineeGuardianPincodeCorres.setErrorEnabled(false);
                    if (etNomineeGuardianPincodeCorres.length() < 6) {
                        etNomineeGuardianPincodeCorresError.setVisibility(View.VISIBLE);
                        etNomineeGuardianPincodeCorresError.setText("Invalid pincode.");
                        etNomineeGuardianPincodeCorres.requestFocus();
                    } else {
                        etNomineeGuardianPincodeCorresError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeGuardianPincodeCorres.setErrorEnabled(false);
                    etNomineeGuardianPincodeCorresError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNomineeGuardianEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etNomineeGuardianEmail.getText().toString())) {
                    inputLayoutNomineeGuardianEmail.setErrorEnabled(false);
                    if (!isValidEmail(etNomineeGuardianEmail.getText())) {
                        etNomineeGuardianEmailError.setVisibility(View.VISIBLE);
                        etNomineeGuardianEmailError.setText("Invalid email id");
                        etNomineeGuardianEmail.requestFocus();
                    } else {
                        etNomineeGuardianEmailError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeGuardianEmail.setErrorEnabled(false);
                    etNomineeGuardianEmailError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNomineeGuardianSTDNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etNomineeGuardianSTDNo.getText().toString())) {
                    inputLayoutNomineeGuardianSTD.setErrorEnabled(false);
                    if (etNomineeGuardianSTDNo.getText().toString().length() < 3) {
                        etNomineeGuardianSTDTelError.setVisibility(View.VISIBLE);
                        etNomineeGuardianSTDTelError.setText("Invalid STD code");
                        etNomineeGuardianSTDNo.requestFocus();
                    } else {
                        etNomineeGuardianSTDTelError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeGuardianSTD.setErrorEnabled(false);
                    etNomineeGuardianSTDTelError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNomineeGuardianTelNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etNomineeGuardianTelNo.getText().toString())) {
                    inputLayoutNomineeGuardianTelNo.setErrorEnabled(false);
                    if (etNomineeGuardianTelNo.getText().toString().length() < 6) {
                        etNomineeGuardianSTDTelError.setVisibility(View.VISIBLE);
                        etNomineeGuardianSTDTelError.setText(Res_Telephone_number_min_11_digit);
                        etNomineeGuardianTelNo.requestFocus();
                    } else {
                        etNomineeGuardianSTDTelError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutNomineeGuardianTelNo.setErrorEnabled(false);
                    etNomineeGuardianSTDTelError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSJHSTDNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etSJHSTDNo.getText().toString())) {
                    inputLayoutSJHSTD.setErrorEnabled(false);
                    if (etSJHSTDNo.getText().toString().length() < 3) {
                        etSJHSTDTelError.setVisibility(View.VISIBLE);
                        etSJHSTDTelError.setText("Invalid STD code");
                        etSJHSTDNo.requestFocus();
                    } else {
                        etSJHSTDTelError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutSJHSTD.setErrorEnabled(false);
                    etSJHSTDTelError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSJHTelNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etSJHTelNo.getText().toString())) {
                    inputLayoutSJHTelNo.setErrorEnabled(false);
                    if (etSJHTelNo.getText().toString().length() < 6) {
                        etSJHSTDTelError.setVisibility(View.VISIBLE);
                        etSJHSTDTelError.setText(Res_Telephone_number_min_11_digit);
                        etSJHTelNo.requestFocus();
                    } else {
                        etSJHSTDTelError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutSJHTelNo.setErrorEnabled(false);
                    etSJHSTDTelError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTJHSTDNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etTJHSTDNo.getText().toString())) {
                    inputLayoutTJHSTD.setErrorEnabled(false);
                    if (etTJHSTDNo.getText().toString().length() < 3) {
                        etTJHSTDTelError.setVisibility(View.VISIBLE);
                        etTJHSTDTelError.setText("Invalid STD code");
                        etTJHSTDNo.requestFocus();
                    } else {
                        etTJHSTDTelError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHSTD.setErrorEnabled(false);
                    etTJHSTDTelError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTJHTelNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etTJHTelNo.getText().toString())) {
                    inputLayoutTJHTelNo.setErrorEnabled(false);
                    if (etTJHTelNo.getText().toString().length() < 6) {
                        etTJHSTDTelError.setVisibility(View.VISIBLE);
                        etTJHSTDTelError.setText(Res_Telephone_number_min_11_digit);
                        etTJHTelNo.requestFocus();
                    } else {
                        etTJHSTDTelError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHTelNo.setErrorEnabled(false);
                    etTJHSTDTelError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSJHFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etSJHFirstName.getText().toString());
                if (!isBlank(etSJHFirstName.getText().toString())) {
                    inputLayoutSJHFirstName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etSJHFirstName.setText("");
                        etSJHFirstName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etSJHFirstName.getText().toString().trim())) {
                        etSJHFirstNameError.setVisibility(View.VISIBLE);
                        etSJHFirstNameError.setText(ErrorMsgName);
                        etSJHFirstName.requestFocus();
                    } else {
                        etSJHFirstNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutSJHFirstName.setErrorEnabled(false);
                    etSJHFirstNameError.setVisibility(View.GONE);
                }
            }
        });


        etTJHFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etTJHFirstName.getText().toString());
                if (!isBlank(etTJHFirstName.getText().toString())) {
                    inputLayoutTJHFirstName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etTJHFirstName.setText("");
                        etTJHFirstName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etTJHFirstName.getText().toString().trim())) {
                        etTJHFirstNameError.setVisibility(View.VISIBLE);
                        etTJHFirstNameError.setText(ErrorMsgName);
                        etTJHFirstName.requestFocus();
                    } else {
                        etTJHFirstNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHFirstName.setErrorEnabled(false);
                    etTJHFirstNameError.setVisibility(View.GONE);
                }
            }
        });

        etSJHMiddleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etSJHMiddleName.getText().toString());
                if (!isBlank(etSJHMiddleName.getText().toString())) {
                    inputLayoutSJHMiddleName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etSJHMiddleName.setText("");
                        etSJHMiddleName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etSJHMiddleName.getText().toString().trim())) {
                        etSJHMiddleNameError.setVisibility(View.VISIBLE);
                        etSJHMiddleNameError.setText(ErrorMsgName);
                        etSJHMiddleName.requestFocus();
                    } else {
                        etSJHMiddleNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutSJHMiddleName.setErrorEnabled(false);
                    etSJHMiddleNameError.setVisibility(View.GONE);
                }
            }
        });

        etTJHMiddleName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etTJHMiddleName.getText().toString());
                if (!isBlank(etTJHMiddleName.getText().toString())) {
                    inputLayoutTJHMiddleName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etTJHMiddleName.setText("");
                        etTJHMiddleName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etTJHMiddleName.getText().toString().trim())) {
                        etTJHMiddleNameError.setVisibility(View.VISIBLE);
                        etTJHMiddleNameError.setText(ErrorMsgName);
                        etTJHMiddleName.requestFocus();
                    } else {
                        etTJHMiddleNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHMiddleName.setErrorEnabled(false);
                    etTJHMiddleNameError.setVisibility(View.GONE);
                }
            }
        });

        etTJHLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etTJHLastName.getText().toString());
                if (!isBlank(etTJHLastName.getText().toString())) {
                    inputLayoutTJHLastName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etTJHLastName.setText("");
                        etTJHLastName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etTJHLastName.getText().toString().trim())) {
                        etTJHLastNameError.setVisibility(View.VISIBLE);
                        etTJHLastNameError.setText(ErrorMsgName);
                        etTJHLastName.requestFocus();
                    } else {
                        etTJHLastNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHLastName.setErrorEnabled(false);
                    etTJHLastNameError.setVisibility(View.GONE);
                }
            }
        });

        etSJHLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etSJHLastName.getText().toString());
                if (!isBlank(etSJHLastName.getText().toString())) {
                    inputLayoutSJHLastName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etSJHLastName.setText("");
                        etSJHLastName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etSJHLastName.getText().toString().trim())) {
                        etSJHLastNameError.setVisibility(View.VISIBLE);
                        etSJHLastNameError.setText(ErrorMsgName);
                        etSJHLastName.requestFocus();
                    } else {
                        etSJHLastNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutSJHLastName.setErrorEnabled(false);
                    etSJHLastNameError.setVisibility(View.GONE);
                }
            }
        });

        //father
        etSJHFatherSpouseFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etSJHFatherSpouseFirstName.getText().toString());
                if (!isBlank(etSJHFatherSpouseFirstName.getText().toString())) {
                    inputLayoutSJHFatherSpouseFirstName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etSJHFatherSpouseFirstName.setText("");
                        etSJHFatherSpouseFirstName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etSJHFatherSpouseFirstName.getText().toString().trim())) {
                        etSJHFatherSpouseFirstNameError.setVisibility(View.VISIBLE);
                        etSJHFatherSpouseFirstNameError.setText(ErrorMsgName);
                        etSJHFatherSpouseFirstName.requestFocus();
                    } else {
                        etSJHFatherSpouseFirstNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutSJHFatherSpouseFirstName.setErrorEnabled(false);
                    etSJHFatherSpouseFirstNameError.setVisibility(View.GONE);
                }
            }
        });

        etTJHFatherSpouseFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etTJHFatherSpouseFirstName.getText().toString());
                if (!isBlank(etTJHFatherSpouseFirstName.getText().toString())) {
                    inputLayoutTJHFatherSpouseFirstName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etTJHFatherSpouseFirstName.setText("");
                        etTJHFatherSpouseFirstName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etTJHFatherSpouseFirstName.getText().toString().trim())) {
                        etTJHFatherSpouseFirstNameError.setVisibility(View.VISIBLE);
                        etTJHFatherSpouseFirstNameError.setText(ErrorMsgName);
                        etTJHFatherSpouseFirstName.requestFocus();
                    } else {
                        etTJHFatherSpouseFirstNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHFatherSpouseFirstName.setErrorEnabled(false);
                    etTJHFatherSpouseFirstNameError.setVisibility(View.GONE);
                }
            }
        });

        etSJHFatherSpouseMidName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etSJHFatherSpouseMidName.getText().toString());
                if (!isBlank(etSJHFatherSpouseMidName.getText().toString())) {
                    inputLayoutSJHFatherSpouseMidName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etSJHFatherSpouseMidName.setText("");
                        etSJHFatherSpouseMidName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etSJHFatherSpouseMidName.getText().toString().trim())) {
                        etSJHFatherSpouseMidNameError.setVisibility(View.VISIBLE);
                        etSJHFatherSpouseMidNameError.setText(ErrorMsgName);
                        etSJHFatherSpouseMidName.requestFocus();
                    } else {
                        etSJHFatherSpouseMidNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutSJHFatherSpouseMidName.setErrorEnabled(false);
                    etSJHFatherSpouseMidNameError.setVisibility(View.GONE);
                }
            }
        });

        etTJHFatherSpouseMidName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etTJHFatherSpouseMidName.getText().toString());
                if (!isBlank(etTJHFatherSpouseMidName.getText().toString())) {
                    inputLayoutTJHFatherSpouseMidName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etTJHFatherSpouseMidName.setText("");
                        etTJHFatherSpouseMidName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etTJHFatherSpouseMidName.getText().toString().trim())) {
                        etTJHFatherSpouseMidNameError.setVisibility(View.VISIBLE);
                        etTJHFatherSpouseMidNameError.setText(ErrorMsgName);
                        etTJHFatherSpouseMidName.requestFocus();
                    } else {
                        etTJHFatherSpouseMidNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHFatherSpouseMidName.setErrorEnabled(false);
                    etTJHFatherSpouseMidNameError.setVisibility(View.GONE);
                }
            }
        });

        etTJHFatherSpouseLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etTJHFatherSpouseLastName.getText().toString());
                if (!isBlank(etTJHFatherSpouseLastName.getText().toString())) {
                    inputLayoutTJHFatherSpouseLastName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etTJHFatherSpouseLastName.setText("");
                        etTJHFatherSpouseLastName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etTJHFatherSpouseLastName.getText().toString().trim())) {
                        etTJHFatherSpouseLastNameError.setVisibility(View.VISIBLE);
                        etTJHFatherSpouseLastNameError.setText(ErrorMsgName);
                        etTJHFatherSpouseLastName.requestFocus();
                    } else {
                        etTJHFatherSpouseLastNameError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHFatherSpouseLastName.setErrorEnabled(false);
                    etTJHFatherSpouseLastNameError.setVisibility(View.GONE);
                }
            }
        });

        etSJHFatherSpouseLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newStr = ValidationRule.trimFull(etSJHFatherSpouseLastName.getText().toString());
                if (!isBlank(etSJHFatherSpouseLastName.getText().toString())) {
                    inputLayoutSJHFatherSpouseLastName.setErrorEnabled(false);
                    if (!s.toString().equals(newStr)) {
                        etSJHFatherSpouseLastName.setText("");
                        etSJHFatherSpouseLastName.append(newStr);
                    } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etSJHFatherSpouseLastName.getText().toString().trim())) {
                        etSJHFatherSpouseLastNameError.setVisibility(View.VISIBLE);
                        etSJHFatherSpouseLastNameError.setText(ErrorMsgName);
                        etSJHFatherSpouseLastName.requestFocus();
                    } else {
                        etSJHFatherSpouseLastNameError.setVisibility(View.GONE);
                    }
                } else {
                    etSJHFatherSpouseLastNameError.setVisibility(View.GONE);
                }
            }
        });


        etSJHAadhar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etSJHAadhar.getText().toString())) {
                    inputLayoutSJHAadhar.setErrorEnabled(false);
                    if (etSJHAadhar.length() < 12) {
                        etSJHAadharError.setVisibility(View.VISIBLE);
                        etSJHAadharError.setText("Invalid aadhar number");
                        etSJHAadhar.requestFocus();
                    } else {
                        etSJHAadharError.setVisibility(View.GONE);
                    }
                } else {
                    etSJHAadharError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNomineeAadhar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etNomineeAadhar.getText().toString())) {
                    inputLayoutNomineeAadhaar.setErrorEnabled(false);
                    if (etNomineeAadhar.length() < 12) {
                        tvNomineeAadharError.setVisibility(View.VISIBLE);
                        tvNomineeAadharError.setText("Invalid aadhar number");
                        etNomineeAadhar.requestFocus();
                    } else {
                        tvNomineeAadharError.setVisibility(View.GONE);
                    }
                } else {
                    tvNomineeAadharError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTJHAadhar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etTJHAadhar.getText().toString())) {
                    inputLayoutTJHAadhar.setErrorEnabled(false);
                    if (etTJHAadhar.length() < 12) {
                        etTJHAadharError.setVisibility(View.VISIBLE);
                        etTJHAadharError.setText("Invalid aadhar number");
                        etTJHAadhar.requestFocus();
                    } else {
                        etTJHAadharError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHAadhar.setErrorEnabled(false);
                    etTJHAadharError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etTJHMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etTJHMobile.getText().toString())) {
                    inputLayoutTJHMobile.setErrorEnabled(false);
                    if (!ValidationRule.validateMobile(etTJHMobile.getText().toString())) {
                        etTJHMobileError.setVisibility(View.VISIBLE);
                        etTJHMobileError.setText("Invalid mobile no.");
                        etTJHMobile.requestFocus();
                    } else {
                        etTJHMobileError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHMobile.setErrorEnabled(false);
                    etTJHMobileError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSJHMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etSJHMobile.getText().toString())) {
                    inputLayoutSJHMobile.setErrorEnabled(false);
                    if (!ValidationRule.validateMobile(etSJHMobile.getText().toString())) {
                        etSJHMobileError.setVisibility(View.VISIBLE);
                        etSJHMobileError.setText("Invalid mobile no.");
                        etSJHMobile.requestFocus();
                    } else {
                        etSJHMobileError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutSJHMobile.setErrorEnabled(false);
                    etSJHMobileError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSJHEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etSJHEmail.getText().toString())) {
                    inputLayoutSJHEmail.setErrorEnabled(false);
                    if (!isValidEmail(etSJHEmail.getText())) {
                        etSJHEmailError.setVisibility(View.VISIBLE);
                        etSJHEmailError.setText("Invalid email id");
                        etSJHEmail.requestFocus();
                    } else {
                        etSJHEmailError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutSJHEmail.setErrorEnabled(false);
                    etSJHEmailError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTJHEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etTJHEmail.getText().toString())) {
                    inputLayoutTJHEmail.setErrorEnabled(false);
                    if (!isValidEmail(etTJHEmail.getText())) {
                        etTJHEmailError.setVisibility(View.VISIBLE);
                        etTJHEmailError.setText("Invalid email id");
                        etTJHEmail.requestFocus();
                    } else {
                        etTJHEmailError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHEmail.setErrorEnabled(false);
                    etTJHEmailError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSJHPincodeCorres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etSJHPincodeCorres.getText().toString())) {
                    inputLayoutSJHPincodeCorres.setErrorEnabled(false);
                    if (etSJHPincodeCorres.length() < 6) {
                        etSJHPincodeCorresError.setVisibility(View.VISIBLE);
                        etSJHPincodeCorresError.setText("Invalid pincode.");
                        etSJHPincodeCorres.requestFocus();
                    } else {
                        etSJHPincodeCorresError.setVisibility(View.GONE);

                    }
                } else {
                    inputLayoutSJHPincodeCorres.setErrorEnabled(false);
                    etSJHPincodeCorresError.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTJHPincodeCorres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etTJHPincodeCorres.getText().toString())) {
                    inputLayoutTJHPincodeCorres.setErrorEnabled(false);
                    if (etTJHPincodeCorres.length() < 6) {
                        etTJHPincodeCorresError.setVisibility(View.VISIBLE);
                        etTJHPincodeCorresError.setText("Invalid pincode.");
                        etTJHPincodeCorres.requestFocus();
                    } else {
                        etTJHPincodeCorresError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHPincodeCorres.setErrorEnabled(false);
                    etTJHPincodeCorresError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSJHPincodePerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etSJHPincodePerm.getText().toString())) {
                    inputLayoutSJHPincodePerm.setErrorEnabled(false);
                    if (etSJHPincodePerm.length() < 6) {
                        etSJHPincodePermError.setVisibility(View.VISIBLE);
                        etSJHPincodePermError.setText("Invalid pincode.");
                        etSJHPincodePerm.requestFocus();
                    } else {
                        etSJHPincodePermError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutSJHPincodePerm.setErrorEnabled(false);
                    etSJHPincodePermError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTJHPincodePerm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etTJHPincodePerm.getText().toString())) {
                    inputLayoutTJHPincodePerm.setErrorEnabled(false);
                    if (etTJHPincodePerm.length() < 6) {
                        etTJHPincodePermError.setVisibility(View.VISIBLE);
                        etTJHPincodePermError.setText("Invalid pincode.");
                        etTJHPincodePerm.requestFocus();
                    } else {
                        etTJHPincodePermError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHPincodePerm.setErrorEnabled(false);
                    etTJHPincodePermError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etSJHPAN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etSJHPAN.getText().toString())) {
                    inputLayoutSJHPAN.setErrorEnabled(false);
                    if (!isValidPannumber((etSJHPAN.getText().toString()))) {
                        etSJHPANError.setVisibility(View.VISIBLE);
                        etSJHPANError.setText("Invalid PAN number");
                        etSJHPAN.requestFocus();
                    } else {
                        etSJHPANError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutSJHPAN.setErrorEnabled(false);
                    etSJHPANError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etTJHPAN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isBlank(etTJHPAN.getText().toString())) {
                    inputLayoutTJHPAN.setErrorEnabled(false);
                    if (!isValidPannumber((etTJHPAN.getText().toString()))) {
                        etTJHPANError.setVisibility(View.VISIBLE);
                        etTJHPANError.setText("Invalid PAN number");
                        etTJHPAN.requestFocus();
                    } else {
                        etTJHPANError.setVisibility(View.GONE);
                    }
                } else {
                    inputLayoutTJHPAN.setErrorEnabled(false);
                    etTJHPANError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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
                                        cityFoundFlag = false;
                                        //nomineeCityFoundFlag = false;
                                        setSpinnerState();

                                        getNomineeDetails();
                                        getDematDetails();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        commonMsgDialog(getContext(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                                        customProgress.setVisibility(View.GONE);
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
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                                errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray
                                errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                    }
            );
        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    private void getState(final String correState, final String permState, final String correCity, final String permCity, final String type) {
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
                                        spSJHCorresState.setAdapter(arrAdapState);
                                        spSJHCorresState.setOnItemSelectedListener(corresStateListener);
                                        spSJHPermState.setAdapter(arrAdapState);
                                        spSJHPermState.setOnItemSelectedListener(permStateListener);
                                        cityFoundFlag = false;
                                        RowData corresStateData = getSpinnerRowDataByValue(correState, arrAdapState);
                                        if (corresStateData != null) {
                                            spSJHCorresState.setSelection(corresStateData.getPosition());
                                            getCorresCity(((RowData) spSJHCorresState.getSelectedItem()).getKey(), correCity, FirstJoint);
                                        } else {
                                            if (arrAdapCorresCity == null) {
                                                arrCorresCity = new ArrayList<>();
                                                arrAdapCorresCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresCity);
                                                arrAdapCorresCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spSJHCorresCity.setAdapter(arrAdapCorresCity);
                                            }
                                            arrAdapCorresCity.add(new RowData(0, "-1", "Select"));
                                            arrAdapCorresCity.notifyDataSetChanged();
                                            cityFoundFlag = true;
                                        }
                                        RowData permanentStateData = getSpinnerRowDataByValue(permState, arrAdapState);
                                        if (permanentStateData != null) {
                                            spSJHPermState.setSelection(permanentStateData.getPosition());
                                            getPermanentCity(((RowData) spSJHPermState.getSelectedItem()).getKey(), permCity, FirstJoint);
                                        } else {
                                            if (arrAdapPermCity == null) {
                                                arrPermCity = new ArrayList<>();
                                                arrAdapPermCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrPermCity);
                                                arrAdapPermCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spSJHPermCity.setAdapter(arrAdapPermCity);
                                            }
                                            arrAdapPermCity.add(new RowData(0, "-1", "Select"));
                                            arrAdapPermCity.notifyDataSetChanged();
                                            cityFoundFlag = true;
                                        }
                                        customProgress.setVisibility(View.GONE);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        commonMsgDialog(getContext(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                                        alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                    }

                                } else {
                                    customProgress.setVisibility(View.GONE);
                                    MOSLCommon.showToastMsg(mContext, "No state");
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
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable
                                throwable) {
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                                errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                    }
            );
        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    private void getNomineeState(final String correState, final String correCity, final String type) {
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
                                        if (!isBlank(type)) {
                                            if (type.equalsIgnoreCase(Nominee)) {
                                                spNomineeCorresCity.setAdapter(arrAdapNomineeCity);
                                                spNomineeCorresCity.setOnItemSelectedListener(nomineeCityListener);
                                                RowData corresStateData = getSpinnerRowDataByValue(correState, arrAdapState);
                                                if (corresStateData != null) {
                                                    spNomineeCorresState.setSelection(corresStateData.getPosition());
                                                    getNomineeCity(((RowData) spNomineeCorresState.getSelectedItem()).getKey(), correCity, type);
                                                } else {
                                                    if (arrAdapNomineeCity == null) {
                                                        arrNomineeCity = new ArrayList<>();
                                                        arrAdapNomineeCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrNomineeCity);
                                                        arrAdapNomineeCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spNomineeCorresCity.setAdapter(arrAdapNomineeCity);
                                                    }
                                                    arrAdapNomineeCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapNomineeCity.notifyDataSetChanged();
                                                    //nomineeCityFoundFlag = true;
                                                }
                                            } else if (type.equalsIgnoreCase(NomineeGuardian)) {
                                                //Nominee Guardian
                                                spNomineeGuardianCorresCity.setAdapter(arrAdapNomineeCity);
                                                spNomineeGuardianCorresCity.setOnItemSelectedListener(nomineeGuardianCityListener);
                                                RowData corresStateData = getSpinnerRowDataByValue(correState, arrAdapState);
                                                if (corresStateData != null) {
                                                    spNomineeGuardianCorresState.setSelection(corresStateData.getPosition());
                                                    getNomineeCity(((RowData) spNomineeGuardianCorresState.getSelectedItem()).getKey(), correCity, type);
                                                } else {
                                                    if (arrAdapNomineeCity == null) {
                                                        arrNomineeCity = new ArrayList<>();
                                                        arrAdapNomineeCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrNomineeCity);
                                                        arrAdapNomineeCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spNomineeGuardianCorresCity.setAdapter(arrAdapNomineeCity);
                                                    }
                                                    arrAdapNomineeCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapNomineeCity.notifyDataSetChanged();
                                                    //nomineeCityFoundFlag = true;
                                                }
                                            }
                                        } else {
                                            spNomineeCorresCity.setAdapter(arrAdapNomineeCity);
                                            spNomineeCorresCity.setOnItemSelectedListener(nomineeCityListener);
                                            spNomineeGuardianCorresCity.setAdapter(arrAdapNomineeCity);
                                            spNomineeGuardianCorresCity.setOnItemSelectedListener(nomineeGuardianCityListener);
                                        }
                                        customProgress.setVisibility(View.GONE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        commonMsgDialog(getContext(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
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
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                    }
            );
        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    private void getCorresCity(String stateCodeStr, final String city, final String type) {
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
                                        if (!isBlank(type)) {
                                            if (type.equalsIgnoreCase(FirstJoint)) {
                                                spSJHCorresCity.setAdapter(arrAdapCorresCity);
                                                spSJHCorresCity.setOnItemSelectedListener(corresCityListener);
                                                RowData corresCityData = getSpinnerRowDataByValue(city, arrAdapCorresCity);
                                                if (corresCityData != null) {
                                                    spSJHCorresCity.setSelection(corresCityData.getPosition());
                                                }
                                            } else if (type.equalsIgnoreCase(SecondJoint)) {
                                                //Third joint city
                                                spTJHCorresCity.setAdapter(arrAdapCorresCity);
                                                spTJHCorresCity.setOnItemSelectedListener(tjhCorresCityListener);
                                                RowData corresCityData = getSpinnerRowDataByValue(city, arrAdapCorresCity);
                                                if (corresCityData != null) {
                                                    spTJHCorresCity.setSelection(corresCityData.getPosition());
                                                }
                                            }
                                        } else {
                                            spSJHCorresCity.setAdapter(arrAdapCorresCity);
                                            spSJHCorresCity.setOnItemSelectedListener(corresCityListener);
                                            spTJHCorresCity.setAdapter(arrAdapCorresCity);
                                            spTJHCorresCity.setOnItemSelectedListener(tjhCorresCityListener);
                                        }
                                        cityFoundFlag = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        commonMsgDialog(getContext(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
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
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                    }
            );
        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    private void getPermanentCity(String stateCodeStr, final String city, final String type) {
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
                                        if (!isBlank(type)) {
                                            if (type.equalsIgnoreCase(FirstJoint)) {
                                                spSJHPermCity.setAdapter(arrAdapPermCity);
                                                spSJHPermCity.setOnItemSelectedListener(permCityListener);
                                                RowData permanentCityData = getSpinnerRowDataByValue(city, arrAdapPermCity);
                                                if (permanentCityData != null) {
                                                    spSJHPermCity.setSelection(permanentCityData.getPosition());
                                                }
                                                if (cbSJHSameAsCorrespondence.isChecked() && spSJHCorresCity.getSelectedItem() != null) {
                                                    int corrSelectedCityPos = ((RowData) spSJHCorresCity.getSelectedItem()).getPosition();
                                                    spSJHPermCity.setSelection(corrSelectedCityPos);
                                                }
                                            } else if (type.equalsIgnoreCase(SecondJoint)) {
                                                //Third join perm city
                                                spTJHPermCity.setAdapter(arrAdapPermCity);
                                                spTJHPermCity.setOnItemSelectedListener(tjhPermCityListener);
                                                RowData permanentCityData = getSpinnerRowDataByValue(city, arrAdapPermCity);
                                                if (permanentCityData != null) {
                                                    spTJHPermCity.setSelection(permanentCityData.getPosition());
                                                }
                                                if (cbTJHSameAsCorrespondence.isChecked() && spTJHCorresCity.getSelectedItem() != null) {
                                                    int corrSelectedCityPos = ((RowData) spTJHCorresCity.getSelectedItem()).getPosition();
                                                    spTJHPermCity.setSelection(corrSelectedCityPos);
                                                }
                                            }
                                        } else {
                                            spSJHPermCity.setAdapter(arrAdapPermCity);
                                            spSJHPermCity.setOnItemSelectedListener(permCityListener);
                                            spTJHPermCity.setAdapter(arrAdapPermCity);
                                            spTJHPermCity.setOnItemSelectedListener(tjhPermCityListener);
                                        }
                                        cityFoundFlag = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        commonMsgDialog(getContext(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
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
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
//                            alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject
                                errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
//                            alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                    }
            );
        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    private void getNomineeCity(String stateCodeStr, final String city, final String type) {
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
                            arrNomineeCity = new ArrayList<>();
                            if (statusCode == 200) {
                                if (response.length() != 0 || response != null) {
                                    String cityCode, cityName;
                                    try {
                                        JSONArray jsonArray = response.getJSONArray(CITY_DETAILS);
                                        arrNomineeCity.add(new RowData(0, "-1", "Select"));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            cityCode = object.getString("CITY_CODE");
                                            cityName = object.getString("CITY_NAME");
                                            arrNomineeCity.add(new RowData((i + 1), cityCode, cityName));

                                        }
                                        arrAdapNomineeCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrNomineeCity);
                                        arrAdapNomineeCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        if (!isBlank(type)) {
                                            if (type.equalsIgnoreCase(Nominee)) {
                                                spNomineeCorresCity.setAdapter(arrAdapNomineeCity);
                                                spNomineeCorresCity.setOnItemSelectedListener(nomineeCityListener);
                                                RowData corresCityData = getSpinnerRowDataByValue(city, arrAdapNomineeCity);
                                                if (corresCityData != null) {
                                                    spNomineeCorresCity.setSelection(corresCityData.getPosition());
                                                }
                                            } else if (type.equalsIgnoreCase(NomineeGuardian)) {
                                                //Nominee Guardian
                                                spNomineeGuardianCorresCity.setAdapter(arrAdapNomineeCity);
                                                spNomineeGuardianCorresCity.setOnItemSelectedListener(nomineeGuardianCityListener);
                                                RowData corresCityData = getSpinnerRowDataByValue(city, arrAdapNomineeCity);
                                                if (corresCityData != null) {
                                                    spNomineeGuardianCorresCity.setSelection(corresCityData.getPosition());
                                                }
                                            }
                                        } else {
                                            spNomineeCorresCity.setAdapter(arrAdapNomineeCity);
                                            spNomineeCorresCity.setOnItemSelectedListener(nomineeCityListener);
                                            spNomineeGuardianCorresCity.setAdapter(arrAdapNomineeCity);
                                            spNomineeGuardianCorresCity.setOnItemSelectedListener(nomineeGuardianCityListener);
                                        }
                                        //nomineeCityFoundFlag = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        commonMsgDialog(getContext(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                                        alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                                    }
                                    customProgress.setVisibility(View.GONE);
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                    MOSLCommon.showToastMsg(mContext, "NO CITY");
                                    //nomineeCityFoundFlag = false;
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
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            customProgress.setVisibility(View.GONE);
                        }
                    }
            );
        } else {
            MOSLCommon.alertDInternet(mContext);
        }
    }

    public void getAddressDetails() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
            inputMap.put("_userid", getUserID());
//            inputMap.put("_userid", "1625707");

            String flag = GETCORRADDRESSDETAILS;
            String category = GETDEMATDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(getContext(), flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            if (response.has("AddressDetails")) {
                                JSONArray jsonArray = response.getJSONArray("AddressDetails");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String corresTelResident = (object.isNull(CORRES_TEL_RESIDENT) || object.getString(CORRES_TEL_RESIDENT).equals("")) ? "" : object.getString(CORRES_TEL_RESIDENT);
                                        String corresAdd1 = (object.isNull(CORRES_ADD1) || object.getString(CORRES_ADD1).equals("")) ? "" : object.getString(CORRES_ADD1);
                                        String corresAdd2 = (object.isNull(CORRES_ADD2) || object.getString(CORRES_ADD2).equals("")) ? "" : object.getString(CORRES_ADD2);
                                        String corresAdd3 = (object.isNull(CORRES_ADD3) || object.getString(CORRES_ADD3).equals("")) ? "" : object.getString(CORRES_ADD3);
                                        String corresLandMark = (object.isNull(CORRES_LANDMARK) || object.getString(CORRES_LANDMARK).equals("")) ? "" : object.getString(CORRES_LANDMARK);
                                        String corresCity = (object.isNull(CORRES_CITY) || object.getString(CORRES_CITY).equals("")) ? "" : object.getString(CORRES_CITY);
                                        String corresPinCode = (object.isNull(CORRES_PIN_CODE) || object.getString(CORRES_PIN_CODE).equals("")) ? "" : object.getString(CORRES_PIN_CODE);
                                        String corresState = (object.isNull(CORRES_STATE) || object.getString(CORRES_STATE).equals("")) ? "" : object.getString(CORRES_STATE);
                                        String corresCountry = (object.isNull(CORRES_COUNTRY) || object.getString(CORRES_COUNTRY).equals("")) ? "" : object.getString(CORRES_COUNTRY);
                                        String otherCityCorres = (object.isNull(OTHER_CITY_CORRES) || object.getString(OTHER_CITY_CORRES).equals("")) ? "" : object.getString(OTHER_CITY_CORRES);
                                        cityFoundFlag = false;
                                        if (rlSecondJointHolder.getVisibility() == View.VISIBLE) {
                                            setDematFirstJointHolderCity(corresCity);
                                            if (!isBlank(corresTelResident)) {
                                                String[] parts = corresTelResident.split("~");
                                                if (parts.length > 1) {
                                                    String std = parts[0];
                                                    String tel = parts[1];
                                                    etSJHSTDNo.setText(std);
                                                    etSJHTelNo.setText(tel);
                                                } else {
                                                    if (corresTelResident.equals("~")) {
                                                        etSJHTelNo.setText("");
                                                    } else {
                                                        etSJHTelNo.setText(corresTelResident);
                                                    }
                                                }
                                            } else {
                                                etSJHSTDNo.setText("");
                                                etSJHTelNo.setText("");
                                            }
//                                            etSJHTelNo.setText(corresTelResident);
                                            etSJHBldgNameCorres.setText(corresAdd1);
                                            etSJHFlatNoCorres.setText(corresAdd2);
                                            etSJHAreaCorres.setText(corresAdd3);
                                            etSJHPincodeCorres.setText(corresPinCode);
                                            getSpinnerData(arrAdapCountry, spSJHCorresCountry, corresCountry);

                                            //If address mode is manual spinner will be visible otherwise edittext
                                            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                                                spSJHCorresState.setVisibility(View.GONE);
                                                spSJHCorresCity.setVisibility(View.GONE);
                                                etSJHCorresState.setVisibility(View.VISIBLE);
                                                etSJHCorresCity.setVisibility(View.VISIBLE);
                                                etSJHCorresState.setText(corresState);
                                                etSJHCorresCity.setText(corresCity);
                                            } else {
                                                RowData corresStateData = getSpinnerRowDataByValue(corresState, arrAdapState);
                                                if (corresStateData != null) {
                                                    spSJHCorresState.setSelection(corresStateData.getPosition());
//                                                getCorresCity(((RowData) spSJHCorresState.getSelectedItem()).getKey(), corresCity, FirstJoint);
                                                } else {
                                                    spSJHCorresState.setVisibility(View.VISIBLE);
                                                    spSJHCorresCity.setVisibility(View.VISIBLE);
                                                    etSJHCorresState.setVisibility(View.GONE);
                                                    etSJHCorresCity.setVisibility(View.GONE);
                                                    if (arrAdapCorresCity == null) {
                                                        arrCorresCity = new ArrayList<>();
                                                        arrAdapCorresCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresCity);
                                                        arrAdapCorresCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spSJHCorresCity.setAdapter(arrAdapCorresCity);
                                                    }
                                                    arrAdapCorresCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapCorresCity.notifyDataSetChanged();
                                                    cityFoundFlag = true;
                                                }
                                                if (corresCity.equalsIgnoreCase("Other")) {
                                                    etSJHCorresCityOther.setVisibility(View.VISIBLE);
                                                    etSJHCorresCityOther.setText(otherCityCorres);
                                                } else {
                                                    etSJHCorresCityOther.setVisibility(View.GONE);
                                                }
                                            }
                                        } else if (rlThirdJointHolder.getVisibility() == View.VISIBLE) {
                                            setDematSecondJointHolderCity(corresCity);
                                            if (!isBlank(corresTelResident)) {
                                                String[] parts = corresTelResident.split("~");
                                                if (parts.length > 1) {
                                                    String std = parts[0];
                                                    String tel = parts[1];
                                                    etTJHSTDNo.setText(std);
                                                    etTJHTelNo.setText(tel);
                                                } else {
                                                    if (corresTelResident.equals("~")) {
                                                        etTJHTelNo.setText("");
                                                    } else {
                                                        etTJHTelNo.setText(corresTelResident);
                                                    }
                                                }
                                            } else {
                                                etTJHSTDNo.setText("");
                                                etTJHTelNo.setText("");
                                            }
//                                            etTJHTelNo.setText(corresTelResident);
                                            etTJHBldgNameCorres.setText(corresAdd1);
                                            etTJHFlatNoCorres.setText(corresAdd2);
                                            etTJHAreaCorres.setText(corresAdd3);
                                            etTJHPincodeCorres.setText(corresPinCode);
                                            getSpinnerData(arrAdapCountry, spTJHCorresCountry, corresCountry);

                                            //If address mode is manual spinner will be visible otherwise edittext
                                            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                                                spTJHCorresState.setVisibility(View.GONE);
                                                spTJHCorresCity.setVisibility(View.GONE);
                                                etTJHCorresState.setVisibility(View.VISIBLE);
                                                etTJHCorresCity.setVisibility(View.VISIBLE);
                                                etTJHCorresState.setText(corresState);
                                                etTJHCorresCity.setText(corresCity);
                                            } else {
                                                spTJHCorresState.setVisibility(View.VISIBLE);
                                                spTJHCorresCity.setVisibility(View.VISIBLE);
                                                etTJHCorresState.setVisibility(View.GONE);
                                                etTJHCorresCity.setVisibility(View.GONE);
                                                RowData corresStateData = getSpinnerRowDataByValue(corresState, arrAdapState);
                                                if (corresStateData != null) {
                                                    spTJHCorresState.setSelection(corresStateData.getPosition());
//                                                getCorresCity(((RowData) spTJHCorresState.getSelectedItem()).getKey(), corresCity, SecondJoint);
                                                } else {
                                                    if (arrAdapCorresCity == null) {
                                                        arrCorresCity = new ArrayList<>();
                                                        arrAdapCorresCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresCity);
                                                        arrAdapCorresCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spTJHCorresCity.setAdapter(arrAdapCorresCity);
                                                    }
                                                    arrAdapCorresCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapCorresCity.notifyDataSetChanged();
                                                    cityFoundFlag = true;
                                                }
                                                if (corresCity.equalsIgnoreCase("Other")) {
                                                    etTJHCorresCityOther.setVisibility(View.VISIBLE);
                                                    etTJHCorresCityOther.setText(otherCityCorres);
                                                } else {
                                                    etTJHCorresCityOther.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
                        customProgress.setVisibility(View.GONE);
                    } else {
                        //MOSLCommon.showToastMsg(mContext, NO_RESPONSE);
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
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
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

    public void getNomineeeAddressDetails() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
            inputMap.put("_userid", getUserID());

            String flag = GETCORRADDRESSDETAILS;
            String category = GETDEMATDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(getContext(), flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            if (response.has("AddressDetails")) {
                                JSONArray jsonArray = response.getJSONArray("AddressDetails");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String corresTelResident = (object.isNull(CORRES_TEL_RESIDENT) || object.getString(CORRES_TEL_RESIDENT).equals("")) ? "" : object.getString(CORRES_TEL_RESIDENT);
                                        String corresAdd1 = (object.isNull(CORRES_ADD1) || object.getString(CORRES_ADD1).equals("")) ? "" : object.getString(CORRES_ADD1);
                                        String corresAdd2 = (object.isNull(CORRES_ADD2) || object.getString(CORRES_ADD2).equals("")) ? "" : object.getString(CORRES_ADD2);
                                        String corresAdd3 = (object.isNull(CORRES_ADD3) || object.getString(CORRES_ADD3).equals("")) ? "" : object.getString(CORRES_ADD3);
                                        String corresLandMark = (object.isNull(CORRES_LANDMARK) || object.getString(CORRES_LANDMARK).equals("")) ? "" : object.getString(CORRES_LANDMARK);
                                        String corresCity = (object.isNull(CORRES_CITY) || object.getString(CORRES_CITY).equals("")) ? "" : object.getString(CORRES_CITY);
                                        String corresPinCode = (object.isNull(CORRES_PIN_CODE) || object.getString(CORRES_PIN_CODE).equals("")) ? "" : object.getString(CORRES_PIN_CODE);
                                        String corresState = (object.isNull(CORRES_STATE) || object.getString(CORRES_STATE).equals("")) ? "" : object.getString(CORRES_STATE);
                                        String corresCountry = (object.isNull(CORRES_COUNTRY) || object.getString(CORRES_COUNTRY).equals("")) ? "" : object.getString(CORRES_COUNTRY);
                                        String otherCityCorres = (object.isNull(OTHER_CITY_CORRES) || object.getString(OTHER_CITY_CORRES).equals("")) ? "" : object.getString(OTHER_CITY_CORRES);
                                        //nomineeCityFoundFlag = false;
                                        if (nomineeAddress) {
                                            if (!isBlank(corresCity)) {
                                                nomineeCitySaved = corresCity;
                                            } else {
                                                nomineeCitySaved = null;
                                            }
                                            if (!isBlank(corresTelResident)) {
                                                String[] parts = corresTelResident.split("~");
                                                if (parts.length > 1) {
                                                    String std = parts[0];
                                                    String tel = parts[1];
                                                    etNomineeSTDNo.setText(std);
                                                    etNomineeTelNo.setText(tel);
                                                } else {
                                                    if (corresTelResident.equals("~")) {
                                                        etNomineeTelNo.setText("");
                                                    } else {
                                                        etNomineeTelNo.setText(corresTelResident);
                                                    }
                                                }
                                            } else {
                                                etNomineeSTDNo.setText("");
                                                etNomineeTelNo.setText("");
                                            }
//                                            etNomineeTelNo.setText(corresTelResident);
                                            etNomineeBldgNameCorres.setText(corresAdd1);
                                            etNomineeFlatNoCorres.setText(corresAdd2);
                                            etNomineeAreaCorres.setText(corresAdd3);
                                            etNomineePincodeCorres.setText(corresPinCode);
                                            getSpinnerData(arrAdapCountry, spNomineeCorresCountry, corresCountry);

                                            //If address mode is manual spinner will be visible otherwise edittext
                                            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                                                spNomineeCorresState.setVisibility(View.GONE);
                                                spNomineeCorresCity.setVisibility(View.GONE);
                                                etNomineeCorresState.setVisibility(View.VISIBLE);
                                                etNomineeCorresCity.setVisibility(View.VISIBLE);
                                                etNomineeCorresState.setText(corresState);
                                                etNomineeCorresCity.setText(corresCity);
                                            } else {
                                                spNomineeCorresState.setVisibility(View.VISIBLE);
                                                spNomineeCorresCity.setVisibility(View.VISIBLE);
                                                etNomineeCorresState.setVisibility(View.GONE);
                                                etNomineeCorresCity.setVisibility(View.GONE);
                                                RowData corresStateData = getSpinnerRowDataByValue(corresState, arrAdapState);
                                                if (corresStateData != null) {
                                                    spNomineeCorresState.setSelection(corresStateData.getPosition());
//                                                getNomineeCity(((RowData) spNomineeCorresState.getSelectedItem()).getKey(), corresCity, Nominee);
                                                } else {
                                                    if (arrAdapNomineeCity == null) {
                                                        arrNomineeCity = new ArrayList<>();
                                                        arrAdapNomineeCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrNomineeCity);
                                                        arrAdapNomineeCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spNomineeCorresCity.setAdapter(arrAdapNomineeCity);
                                                    }
                                                    arrAdapNomineeCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapNomineeCity.notifyDataSetChanged();
                                                    //nomineeCityFoundFlag = true;
                                                }
                                                if (corresCity.equalsIgnoreCase("Other")) {
                                                    etNomineeCorresCityOther.setVisibility(View.VISIBLE);
                                                    etNomineeCorresCityOther.setText(otherCityCorres);
                                                } else {
                                                    etNomineeCorresCityOther.setVisibility(View.GONE);
                                                }
                                            }
                                        } else {
                                            if (!isBlank(corresCity)) {
                                                nomineeGuardianCitySaved = corresCity;
                                            } else {
                                                nomineeGuardianCitySaved = null;
                                            }
                                            if (!isBlank(corresTelResident)) {
                                                String[] parts = corresTelResident.split("~");
                                                if (parts.length > 1) {
                                                    String std = parts[0];
                                                    String tel = parts[1];
                                                    etNomineeGuardianSTDNo.setText(std);
                                                    etNomineeGuardianTelNo.setText(tel);
                                                } else {
                                                    if (corresTelResident.equals("~")) {
                                                        etNomineeGuardianTelNo.setText("");
                                                    } else {
                                                        etNomineeGuardianTelNo.setText(corresTelResident);
                                                    }
                                                }
                                            } else {
                                                etNomineeGuardianSTDNo.setText("");
                                                etNomineeGuardianTelNo.setText("");
                                            }
//                                            etNomineeGuardianTelNo.setText(corresTelResident);
                                            etNomineeGuardianBldgNameCorres.setText(corresAdd1);
                                            etNomineeGuardianFlatNoCorres.setText(corresAdd2);
                                            etNomineeGuardianAreaCorres.setText(corresAdd3);
                                            etNomineeGuardianPincodeCorres.setText(corresPinCode);
                                            getSpinnerData(arrAdapCountry, spNomineeGuardianCorresCountry, corresCountry);

                                            //If address mode is manual spinner will be visible otherwise edittext
                                            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                                                spNomineeGuardianCorresState.setVisibility(View.GONE);
                                                spNomineeGuardianCorresCity.setVisibility(View.GONE);
                                                etNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                                                etNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                                                etNomineeGuardianCorresState.setText(corresState);
                                                etNomineeGuardianCorresCity.setText(corresCity);
                                            } else {
                                                spNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                                                spNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                                                etNomineeGuardianCorresState.setVisibility(View.GONE);
                                                etNomineeGuardianCorresCity.setVisibility(View.GONE);

                                                RowData corresStateData = getSpinnerRowDataByValue(corresState, arrAdapState);
                                                if (corresStateData != null) {
                                                    spNomineeGuardianCorresState.setSelection(corresStateData.getPosition());
//                                                getNomineeCity(((RowData) spNomineeGuardianCorresState.getSelectedItem()).getKey(), corresCity, NomineeGuardian);
                                                } else {
                                                    if (arrAdapNomineeCity == null) {
                                                        arrNomineeCity = new ArrayList<>();
                                                        arrAdapNomineeCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrNomineeCity);
                                                        arrAdapNomineeCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spNomineeGuardianCorresCity.setAdapter(arrAdapNomineeCity);
                                                    }
                                                    arrAdapNomineeCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapNomineeCity.notifyDataSetChanged();
                                                    cityFoundFlag = true;
                                                }
                                                if (corresCity.equalsIgnoreCase("Other")) {
                                                    etNomineeGuardianCorresCityOther.setVisibility(View.VISIBLE);
                                                    etNomineeGuardianCorresCityOther.setText(otherCityCorres);
                                                } else {
                                                    etNomineeGuardianCorresCityOther.setVisibility(View.GONE);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
                        customProgress.setVisibility(View.GONE);
                    } else {
                        // MOSLCommon.showToastMsg(mContext, NO_RESPONSE);
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

    public void getNomineeDetails() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
            inputMap.put("_userid", getUserID());
//            inputMap.put("_userid", "1625707");

            String flag = "GETNOMINEEDETAILS_NEW";
//            String flag = GETNOMINEEDETAILS;
            String category = GETDEMATDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(getContext(), flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (response != null) {
                        try {
                            /**
                             * Nominne Relationship spinner data
                             */
                            if (response.has("GetNominneRelationship")) {
                                arrRelationship = new ArrayList<>();
                                arrRelationship.add(new RowData(0, "-1", "Select"));
                                JSONArray jsonArray = response.getJSONArray("GetNominneRelationship");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String text = (object.isNull("TEXT") || object.getString("TEXT").equals("")) ? "" : object.getString("TEXT");
                                        String value = (object.isNull("VALUE") || object.getString("VALUE").equals("")) ? "" : object.getString("VALUE");
                                        arrRelationship.add(new RowData(i + 1, text, value));
                                    }
                                    arrAdapRelationship = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrRelationship);
                                    arrAdapRelationship.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    //Nominee Relationship
                                    spNomineesRelationship.setAdapter(arrAdapRelationship);
                                    spNomineesRelationship.setOnItemSelectedListener(nomineeRelationshipListener);
                                    //Nominee Guardian Relationship
                                    spNomineesGuardianRelationship.setAdapter(arrAdapRelationship);
                                    spNomineesGuardianRelationship.setOnItemSelectedListener(nomineeGuardianRelationshipListener);
                                    arrAdapRelationship.notifyDataSetChanged();
                                }
                            }

                            if (response.has("GetNominnedetails")) {
                                JSONArray jsonArray = response.getJSONArray("GetNominnedetails");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String nomineeFname = (object.isNull(NOMINEE_FNAME) || object.getString(NOMINEE_FNAME).equals("")) ? "" : object.getString(NOMINEE_FNAME);
                                        String nomineeMname = (object.isNull(NOMINEE_MNAME) || object.getString(NOMINEE_MNAME).equals("")) ? "" : object.getString(NOMINEE_MNAME);
                                        String nomineeLname = (object.isNull(NOMINEE_LNAME) || object.getString(NOMINEE_LNAME).equals("")) ? "" : object.getString(NOMINEE_LNAME);
                                        String nomineeAdd1 = (object.isNull(NOMINEE_ADD1) || object.getString(NOMINEE_ADD1).equals("")) ? "" : object.getString(NOMINEE_ADD1);
                                        String nomineeAdd2 = (object.isNull(NOMINEE_ADD2) || object.getString(NOMINEE_ADD2).equals("")) ? "" : object.getString(NOMINEE_ADD2);
                                        String nomineeAdd3 = (object.isNull(NOMINEE_ADD3) || object.getString(NOMINEE_ADD3).equals("")) ? "" : object.getString(NOMINEE_ADD3);
                                        String nomineeLandmark = (object.isNull(NOMINEE_LANDMARK) || object.getString(NOMINEE_LANDMARK).equals("")) ? "" : object.getString(NOMINEE_LANDMARK);
                                        String nomineeCity = (object.isNull(NOMINEE_CITY) || object.getString(NOMINEE_CITY).equals("")) ? "" : object.getString(NOMINEE_CITY);
                                        String nomineeState = (object.isNull(NOMINEE_STATE) || object.getString(NOMINEE_STATE).equals("")) ? "" : object.getString(NOMINEE_STATE);
                                        String nomineePin = (object.isNull(NOMINEE_PIN) || object.getString(NOMINEE_PIN).equals("")) ? "" : object.getString(NOMINEE_PIN);
                                        String nomineeCountry = (object.isNull(NOMINEE_COUNTRY) || object.getString(NOMINEE_COUNTRY).equals("")) ? "" : object.getString(NOMINEE_COUNTRY);
                                        String nomineeDOB = (object.isNull(NOMINEE_DATE_OF_BIRTH) || object.getString(NOMINEE_DATE_OF_BIRTH).equals("")) ? "" : object.getString(NOMINEE_DATE_OF_BIRTH);
                                        String nomineeTele = (object.isNull(NOMINEE_TELE) || object.getString(NOMINEE_TELE).equals("")) ? "" : object.getString(NOMINEE_TELE);
                                        String nomineeRelBo = (object.isNull(NOMINEE_REL_BO) || object.getString(NOMINEE_REL_BO).equals("")) ? "" : object.getString(NOMINEE_REL_BO);
                                        String nomineeMinorFname = (object.isNull(NOMINEE_MINOR_FNAME) || object.getString(NOMINEE_MINOR_FNAME).equals("")) ? "" : object.getString(NOMINEE_MINOR_FNAME);
                                        String nomineeMinorMname = (object.isNull(NOMINEE_MINOR_MNAME) || object.getString(NOMINEE_MINOR_MNAME).equals("")) ? "" : object.getString(NOMINEE_MINOR_MNAME);
                                        String nomineeMinorLname = (object.isNull(NOMINEE_MINOR_LNAME) || object.getString(NOMINEE_MINOR_LNAME).equals("")) ? "" : object.getString(NOMINEE_MINOR_LNAME);
                                        String nomineeMinorAdd1 = (object.isNull(NOMINEE_MINOR_ADD1) || object.getString(NOMINEE_MINOR_ADD1).equals("")) ? "" : object.getString(NOMINEE_MINOR_ADD1);
                                        String nomineeMinorAdd2 = (object.isNull(NOMINEE_MINOR_ADD2) || object.getString(NOMINEE_MINOR_ADD2).equals("")) ? "" : object.getString(NOMINEE_MINOR_ADD2);
                                        String nomineeMinorAdd3 = (object.isNull(NOMINEE_MINOR_ADD3) || object.getString(NOMINEE_MINOR_ADD3).equals("")) ? "" : object.getString(NOMINEE_MINOR_ADD3);
                                        String nomineeMinorLandmark = (object.isNull(NOMINEE_MINOR_LANDMARK) || object.getString(NOMINEE_MINOR_LANDMARK).equals("")) ? "" : object.getString(NOMINEE_MINOR_LANDMARK);
                                        String nomineeMinorCity = (object.isNull(NOMINEE_MINOR_CITY) || object.getString(NOMINEE_MINOR_CITY).equals("")) ? "" : object.getString(NOMINEE_MINOR_CITY);
                                        String nomineeMinorState = (object.isNull(NOMINEE_MINOR_STATE) || object.getString(NOMINEE_MINOR_STATE).equals("")) ? "" : object.getString(NOMINEE_MINOR_STATE);
                                        String nomineeMinorPin = (object.isNull(NOMINEE_MINOR_PIN) || object.getString(NOMINEE_MINOR_PIN).equals("")) ? "" : object.getString(NOMINEE_MINOR_PIN);
                                        String nomineeMinorCountry = (object.isNull(NOMINEE_MINOR_COUNTRY) || object.getString(NOMINEE_MINOR_COUNTRY).equals("")) ? "" : object.getString(NOMINEE_MINOR_COUNTRY);
                                        String sameAsCorresNominee = (object.isNull(SAME_AS_CORRES_NOMINEE) || object.getString(SAME_AS_CORRES_NOMINEE).equals("")) ? "" : object.getString(SAME_AS_CORRES_NOMINEE);
                                        String sameAsCorresNomineeGuardian = (object.isNull(SAME_AS_CORRES_NOMINEE_GUARDIAN) || object.getString(SAME_AS_CORRES_NOMINEE_GUARDIAN).equals("")) ? "" : object.getString(SAME_AS_CORRES_NOMINEE_GUARDIAN);
                                        String otherCityNominee = (object.isNull(OTHER_CITY_NOMINEE) || object.getString(OTHER_CITY_NOMINEE).equals("")) ? "" : object.getString(OTHER_CITY_NOMINEE);
                                        String otherCityNomineeMinor = (object.isNull(OTHER_CITY_NOMINEE_MINOR) || object.getString(OTHER_CITY_NOMINEE_MINOR).equals("")) ? "" : object.getString(OTHER_CITY_NOMINEE_MINOR);
                                        String sameAsCorresFirstHolder = (object.isNull(SAME_AS_CORRES_FIRST_HOLDER) || object.getString(SAME_AS_CORRES_FIRST_HOLDER).equals("")) ? "" : object.getString(SAME_AS_CORRES_FIRST_HOLDER);
                                        String sameAsCorresSecondHolder = (object.isNull(SAME_AS_CORRES_SECOND_HOLDER) || object.getString(SAME_AS_CORRES_SECOND_HOLDER).equals("")) ? "" : object.getString(SAME_AS_CORRES_SECOND_HOLDER);
                                        String otherCityDematFirstHolder = (object.isNull(OTHER_CITY_DEMAT_FIRST_HOLDER) || object.getString(OTHER_CITY_DEMAT_FIRST_HOLDER).equals("")) ? "" : object.getString(OTHER_CITY_DEMAT_FIRST_HOLDER);
                                        String otherCityDematSecondHolder = (object.isNull(OTHER_CITY_DEMAT_SECOND_HOLDER) || object.getString(OTHER_CITY_DEMAT_SECOND_HOLDER).equals("")) ? "" : object.getString(OTHER_CITY_DEMAT_SECOND_HOLDER);
                                        String numberOfJointHolder = (object.isNull(NUMBER_OF_JOINT_HOLDER) || object.getString(NUMBER_OF_JOINT_HOLDER).equals("")) ? "" : object.getString(NUMBER_OF_JOINT_HOLDER);
                                        String nomineeDeclaration = (object.isNull(NOMINEE_DECLARATION) || object.getString(NOMINEE_DECLARATION).equals("")) ? "" : object.getString(NOMINEE_DECLARATION);
                                        String nomineePan = (object.isNull(NOMINEE_PAN) || object.getString(NOMINEE_PAN).equals("")) ? "" : object.getString(NOMINEE_PAN);
                                        String nomineeGuardianAge = (object.isNull(NOMINEE_GUARDIAN_AGE) || object.getString(NOMINEE_GUARDIAN_AGE).equals("")) ? "" : object.getString(NOMINEE_GUARDIAN_AGE);
                                        String nomineeGuardianRelation = (object.isNull(NOMINEE_GUARDIAN_RELATION) || object.getString(NOMINEE_GUARDIAN_RELATION).equals("")) ? "" : object.getString(NOMINEE_GUARDIAN_RELATION);
                                        String nomineeApplicantRelation = (object.isNull(NOMINEE_APPLICANT_RELATION) || object.getString(NOMINEE_APPLICANT_RELATION).equals("")) ? "" : object.getString(NOMINEE_APPLICANT_RELATION);
                                        String nomineeEmailId = (object.isNull(NOMINEE_EMAIL_ID) || object.getString(NOMINEE_EMAIL_ID).equals("")) ? "" : object.getString(NOMINEE_EMAIL_ID);
                                        String nomineeMinorEmailId = (object.isNull(NOMINEE_MINOR_EMAIL_ID) || object.getString(NOMINEE_MINOR_EMAIL_ID).equals("")) ? "" : object.getString(NOMINEE_MINOR_EMAIL_ID);
                                        String nomineeMinorTelno = (object.isNull(NOMINEE_MINOR_TELNO) || object.getString(NOMINEE_MINOR_TELNO).equals("")) ? "" : object.getString(NOMINEE_MINOR_TELNO);
                                        String nomineeRelBoOther = (object.isNull(NOMINEE_REL_BO_OTHER) || object.getString(NOMINEE_REL_BO_OTHER).equals("")) ? "" : object.getString(NOMINEE_REL_BO_OTHER);
                                        String nomineeApplicantRelationOther = (object.isNull(NOMINEE_APPLICANT_RELATION_OTHER) || object.getString(NOMINEE_APPLICANT_RELATION_OTHER).equals("")) ? "" : object.getString(NOMINEE_APPLICANT_RELATION_OTHER);
                                        String nomineeGuardianRelationOther = (object.isNull(NOMINEE_GUARDIAN_RELATION_OTHER) || object.getString(NOMINEE_GUARDIAN_RELATION_OTHER).equals("")) ? "" : object.getString(NOMINEE_GUARDIAN_RELATION_OTHER);
                                        String nomineeUID = (object.isNull(NOMINEE_UID) || object.getString(NOMINEE_UID).equals("")) ? "" : object.getString(NOMINEE_UID);
                                        String isNomineeMandatory = (object.isNull(NOMINEE_MANDATORY) || object.getString(NOMINEE_MANDATORY).equals("")) ? "" : object.getString(NOMINEE_MANDATORY);
                                        if (nomineeUID.equals("0")) {
                                            nomineeUID = "";
                                        }
                                        if (!isBlank(nomineeCity)) {
                                            nomineeCitySaved = nomineeCity;
                                        } else {
                                            nomineeCitySaved = null;
                                        }
                                        setIsNomineeMandatory(isNomineeMandatory != null && isNomineeMandatory.equals("1") ? true : false);
                                        if (isNomineeMandatory()) {
                                            toggleNominee.setChecked(true);
                                        } else {
                                            toggleNominee.setChecked(false);
                                        }
                                        if (!isBlank(nomineeMinorCity)) {
                                            nomineeGuardianCitySaved = nomineeMinorCity;
                                        } else {
                                            nomineeGuardianCitySaved = null;
                                        }
                                        if (nomineeDeclaration.equalsIgnoreCase(NO)) {
                                            toggleNominee.setChecked(false);
                                            llNomineeDetail.setVisibility(View.GONE);
//                                            cbNomineeNo.setChecked(true);
//                                            cbNomineeYes.setChecked(false);
                                        } else if (nomineeDeclaration.equalsIgnoreCase(YES)) {
                                            toggleNominee.setChecked(true);
                                            llNomineeDetail.setVisibility(View.VISIBLE);
//                                            cbNomineeYes.setChecked(true);
//                                            cbNomineeNo.setChecked(false);
                                        }
                                        etNomineeFirstName.setText(nomineeFname);
                                        etNomineeMiddleName.setText(nomineeMname);
                                        etNomineeLastName.setText(nomineeLname);
                                        etNomineePAN.setText(nomineePan);
                                        etNomineeAadhar.setText(nomineeUID);
                                        setNomineeAadhaar(nomineeUID);

                                        if (sameAsCorresNominee.equalsIgnoreCase(True)) {
                                            cbNomineeSameAsPrimaryCorrespondence.setChecked(true);
                                            etNomineeBldgNameCorres.setEnabled(false);
                                            etNomineeFlatNoCorres.setEnabled(false);
                                            etNomineeAreaCorres.setEnabled(false);
                                            etNomineePincodeCorres.setEnabled(false);
                                            //If address mode is manual spinner will be visible otherwise edittext
                                            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                                                spNomineeCorresState.setVisibility(View.GONE);
                                                spNomineeCorresCity.setVisibility(View.GONE);
                                                etNomineeCorresState.setVisibility(View.VISIBLE);
                                                etNomineeCorresCity.setVisibility(View.VISIBLE);
                                                etNomineeCorresState.setEnabled(false);
                                                etNomineeCorresCity.setEnabled(false);
                                            } else {
                                                spNomineeCorresState.setVisibility(View.VISIBLE);
                                                spNomineeCorresCity.setVisibility(View.VISIBLE);
                                                etNomineeCorresState.setVisibility(View.GONE);
                                                etNomineeCorresCity.setVisibility(View.GONE);
                                                spNomineeCorresState.setEnabled(false);
                                                spNomineeCorresCity.setEnabled(false);
                                            }
                                            spNomineeCorresCountry.setEnabled(false);
                                        } else if (sameAsCorresNominee.equalsIgnoreCase(False) || sameAsCorresNominee.equalsIgnoreCase("")) {
                                            cbNomineeSameAsPrimaryCorrespondence.setChecked(false);
                                            etNomineeBldgNameCorres.setEnabled(true);
                                            etNomineeFlatNoCorres.setEnabled(true);
                                            etNomineeAreaCorres.setEnabled(true);
                                            etNomineePincodeCorres.setEnabled(true);
                                            spNomineeCorresState.setEnabled(true);
                                            spNomineeCorresCity.setEnabled(true);
                                            spNomineeCorresCountry.setEnabled(true);
                                            spNomineeCorresState.setVisibility(View.VISIBLE);
                                            spNomineeCorresCity.setVisibility(View.VISIBLE);
                                            etNomineeCorresState.setVisibility(View.GONE);
                                            etNomineeCorresCity.setVisibility(View.GONE);
                                        }
                                        etNomineeBldgNameCorres.setText(nomineeAdd1);
                                        etNomineeFlatNoCorres.setText(nomineeAdd2);
                                        etNomineeAreaCorres.setText(nomineeAdd3);
                                        etNomineePincodeCorres.setText(nomineePin);
                                        if (nomineeTele.equalsIgnoreCase("") || nomineeTele.equalsIgnoreCase("0") || nomineeTele.equalsIgnoreCase("0.0")) {
                                            etNomineeSTDNo.setText("");
                                            etNomineeTelNo.setText("");
                                        } else {
                                            if (!isBlank(nomineeTele)) {
                                                String[] parts = nomineeTele.split("~");
                                                if (parts.length > 1) {
                                                    String std = parts[0];
                                                    String tel = parts[1];
                                                    etNomineeSTDNo.setText(std);
                                                    etNomineeTelNo.setText(tel);
                                                } else {
                                                    if (nomineeTele.equals("~")) {
                                                        etNomineeTelNo.setText("");
                                                    } else {
                                                        etNomineeTelNo.setText(nomineeTele);
                                                    }
                                                }
                                            } else {
                                                etNomineeSTDNo.setText("");
                                                etNomineeTelNo.setText("");
                                            }
//                                            etNomineeTelNo.setText(nomineeTele);
                                        }
                                        etNomineeDOB.setText(nomineeDOB);
//                                        etNomineesRelationship.setText(nomineeRelBo);
//                                        etNomineePrimaryApplicantsRelationship.setText(nomineeApplicantRelation);
                                        etNomineeEmail.setText(nomineeEmailId);
                                        getSpinnerData(arrAdapCountry, spNomineeCorresCountry, nomineeCountry);
                                        if (!(arrAdapRelationship == null)) {
                                            RowData relation = getSpinnerRowDataByValue(nomineeRelBo, arrAdapRelationship);
                                            if (relation != null) {
                                                spNomineesRelationship.setSelection(relation.getPosition());
                                            }
                                        }
                                        etNomineesRelationship.setText(nomineeRelBoOther);
                                        //If address mode is manual spinner will be visible otherwise edittext
                                        if (cbNomineeSameAsPrimaryCorrespondence.isChecked() && (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL))) {
                                            spNomineeCorresState.setVisibility(View.GONE);
                                            spNomineeCorresCity.setVisibility(View.GONE);
                                            etNomineeCorresState.setVisibility(View.VISIBLE);
                                            etNomineeCorresCity.setVisibility(View.VISIBLE);
                                            etNomineeCorresState.setText(nomineeState);
                                            etNomineeCorresCity.setText(nomineeCity);
                                        } else {
                                            spNomineeCorresState.setVisibility(View.VISIBLE);
                                            spNomineeCorresCity.setVisibility(View.VISIBLE);
                                            etNomineeCorresState.setVisibility(View.GONE);
                                            etNomineeCorresCity.setVisibility(View.GONE);
                                            if (arrAdapState == null) {
                                                getNomineeState(nomineeState, nomineeCity, Nominee);
                                            } else {
                                                RowData corresStateData = getSpinnerRowDataByValue(nomineeState, arrAdapState);
                                                if (corresStateData != null) {
                                                    spNomineeCorresState.setSelection(corresStateData.getPosition());
                                                    //getNomineeCity(((RowData) spNomineeCorresState.getSelectedItem()).getKey(), nomineeCity, Nominee);
                                                } else {
                                                    if (arrAdapNomineeCity == null) {
                                                        arrNomineeCity = new ArrayList<>();
                                                        arrAdapNomineeCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrNomineeCity);
                                                        arrAdapNomineeCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spNomineeCorresCity.setAdapter(arrAdapNomineeCity);
                                                    }
                                                    arrAdapNomineeCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapNomineeCity.notifyDataSetChanged();
                                                    //nomineeCityFoundFlag = true;
                                                }
                                            }
                                        }
                                        Calendar minAdultAge = new GregorianCalendar();
                                        minAdultAge.add(Calendar.YEAR, -18);
                                        Date adultAge = EkycAppUtil.getDate(dateFormatter.format(minAdultAge.getTime()), "dd/MM/yyyy");
                                        Date dateDOB = EkycAppUtil.getDate(nomineeDOB, "dd/MM/yyyy");
                                        if (!isBlank(nomineeDOB) && adultAge.before(dateDOB)) {
                                            adultFlag = true;
//                                        if (!isBlank(nomineeMinorFname)) {
                                            llNomineeGuardian.setVisibility(View.VISIBLE);
                                            etNomineeGuardianFirstName.setText(nomineeMinorFname);
                                            etNomineeGuardianMiddleName.setText(nomineeMinorMname);
                                            etNomineeGuardianLastName.setText(nomineeMinorLname);
                                            etNomineeGuardianBldgNameCorres.setText(nomineeMinorAdd1);
                                            etNomineeGuardianFlatNoCorres.setText(nomineeMinorAdd2);
                                            etNomineeGuardianAreaCorres.setText(nomineeMinorAdd3);
                                            etNomineeGuardianPincodeCorres.setText(nomineeMinorPin);
                                            etNomineeGuardianEmail.setText(nomineeMinorEmailId);
                                            if (nomineeMinorTelno.equalsIgnoreCase("") || nomineeMinorTelno.equalsIgnoreCase("0") || nomineeMinorTelno.equalsIgnoreCase("0.0")) {
                                                etNomineeGuardianSTDNo.setText("");
                                                etNomineeGuardianTelNo.setText("");
                                            } else {
                                                if (!isBlank(nomineeMinorTelno)) {
                                                    String[] parts = nomineeMinorTelno.split("~");
                                                    if (parts.length > 1) {
                                                        String std = parts[0];
                                                        String tel = parts[1];
                                                        etNomineeGuardianSTDNo.setText(std);
                                                        etNomineeGuardianTelNo.setText(tel);
                                                    } else {
                                                        if (nomineeMinorTelno.equals("~")) {
                                                            etNomineeGuardianTelNo.setText("");
                                                        } else {
                                                            etNomineeGuardianTelNo.setText(nomineeMinorTelno);
                                                        }
                                                    }
                                                } else {
                                                    etNomineeGuardianSTDNo.setText("");
                                                    etNomineeGuardianTelNo.setText("");
                                                }
//                                                etNomineeGuardianTelNo.setText(nomineeMinorTelno);
                                            }
//                                            etNomineesGuardianRelationship.setText(nomineeGuardianRelation);
                                            getSpinnerData(arrAdapCountry, spNomineeGuardianCorresCountry, nomineeMinorCountry);
                                            if (!(arrAdapRelationship == null)) {
                                                RowData relation = getSpinnerRowDataByValue(nomineeGuardianRelation, arrAdapRelationship);
                                                if (relation != null) {
                                                    spNomineesGuardianRelationship.setSelection(relation.getPosition());
                                                }
                                            }
                                            etNomineesGuardianRelationship.setText(nomineeGuardianRelationOther);
                                            if (sameAsCorresNomineeGuardian.equalsIgnoreCase(True)) {
                                                cbNomineeGuardianSameAsPrimaryCorrespondence.setChecked(true);
                                                etNomineeGuardianBldgNameCorres.setEnabled(false);
                                                etNomineeGuardianFlatNoCorres.setEnabled(false);
                                                etNomineeGuardianAreaCorres.setEnabled(false);
                                                etNomineeGuardianPincodeCorres.setEnabled(false);
                                                //If address mode is manual spinner will be visible otherwise edittext
                                                if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                                                    spNomineeGuardianCorresState.setVisibility(View.GONE);
                                                    spNomineeGuardianCorresCity.setVisibility(View.GONE);
                                                    etNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                                                    etNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                                                    etNomineeGuardianCorresState.setEnabled(false);
                                                    etNomineeGuardianCorresCity.setEnabled(false);
                                                } else {
                                                    spNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                                                    spNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                                                    etNomineeGuardianCorresState.setVisibility(View.GONE);
                                                    etNomineeGuardianCorresCity.setVisibility(View.GONE);
                                                    spNomineeGuardianCorresState.setEnabled(false);
                                                    spNomineeGuardianCorresCity.setEnabled(false);
                                                }
                                                spNomineeGuardianCorresCountry.setEnabled(false);
                                            } else if (sameAsCorresNomineeGuardian.equalsIgnoreCase(False) || sameAsCorresNomineeGuardian.equalsIgnoreCase("")) {
                                                cbNomineeGuardianSameAsPrimaryCorrespondence.setChecked(false);
                                                etNomineeGuardianBldgNameCorres.setEnabled(true);
                                                etNomineeGuardianFlatNoCorres.setEnabled(true);
                                                etNomineeGuardianAreaCorres.setEnabled(true);
                                                etNomineeGuardianPincodeCorres.setEnabled(true);
                                                spNomineeGuardianCorresState.setEnabled(true);
                                                spNomineeGuardianCorresCity.setEnabled(true);
                                                spNomineeGuardianCorresCountry.setEnabled(true);
                                                spNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                                                spNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                                                etNomineeGuardianCorresState.setVisibility(View.GONE);
                                                etNomineeGuardianCorresCity.setVisibility(View.GONE);
                                            }
                                            int age = (int) Double.parseDouble(nomineeGuardianAge);
                                            getSpinnerData(arrAdapGuardianAge, spNomineeGuardianAge, String.valueOf(age));

                                            //If address mode is manual spinner will be visible otherwise edittext
                                            if (cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked() && (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL))) {
                                                spNomineeGuardianCorresState.setVisibility(View.GONE);
                                                spNomineeGuardianCorresCity.setVisibility(View.GONE);
                                                etNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                                                etNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                                                etNomineeGuardianCorresState.setText(nomineeMinorState);
                                                etNomineeGuardianCorresCity.setText(nomineeMinorCity);
                                            } else {
                                                spNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                                                spNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                                                etNomineeGuardianCorresState.setVisibility(View.GONE);
                                                etNomineeGuardianCorresCity.setVisibility(View.GONE);

                                                if (arrAdapState == null) {
                                                    getNomineeState(nomineeMinorState, nomineeMinorCity, NomineeGuardian);
                                                } else {
                                                    RowData corresStateData = getSpinnerRowDataByValue(nomineeMinorState, arrAdapState);
                                                    if (corresStateData != null) {
                                                        spNomineeGuardianCorresState.setSelection(corresStateData.getPosition());
//                                                getNomineeCity(((RowData) spNomineeGuardianCorresState.getSelectedItem()).getKey(), nomineeCity, NomineeGuardian);
                                                    } else {
                                                        if (arrAdapNomineeCity == null) {
                                                            arrNomineeCity = new ArrayList<>();
                                                            arrAdapNomineeCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrNomineeCity);
                                                            arrAdapNomineeCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                            spNomineeGuardianCorresCity.setAdapter(arrAdapNomineeCity);
                                                        }
                                                        arrAdapNomineeCity.add(new RowData(0, "-1", "Select"));
                                                        arrAdapNomineeCity.notifyDataSetChanged();
                                                        //nomineeCityFoundFlag = true;
                                                    }
                                                }
                                            }
                                        } else {
                                            adultFlag = false;
                                        }
                                    }
                                }
                            }
                            disableEnableField();
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
                        hideProgress();
                    } else {
                        //MOSLCommon.showToastMsg(mContext, NO_RESPONSE);
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

    public void getDematDetails() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
            inputMap.put("_userid", getUserID());
//            inputMap.put("_userid", "1625707");

            String flag = GETDEMATDETAILS;
            String category = GETDEMATDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(getContext(), flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            if (response.has("AddressProof")) {
                                arrCorresAddressProof = new ArrayList<>();
                                arrCorresAddressProof.add(new RowData(0, "-1", "Select"));
                                arrPermAddressProof = new ArrayList<>();
                                arrPermAddressProof.add(new RowData(0, "-1", "Select"));
                                JSONArray jsonArray = response.getJSONArray("AddressProof");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String id = (object.isNull("Id") || object.getString("Id").equals("")) ? "" : object.getString("Id");
                                        String value = (object.isNull("Value") || object.getString("Value").equals("")) ? "" : object.getString("Value");
                                        arrCorresAddressProof.add(new RowData(i + 1, id, value));
                                        arrPermAddressProof.add(new RowData(i + 1, id, value));
                                    }
                                    arrAdapCorresAddressProof = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresAddressProof);
                                    arrAdapCorresAddressProof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spSJHAddProofCorres.setAdapter(arrAdapCorresAddressProof);
                                    spTJHAddProofCorres.setAdapter(arrAdapCorresAddressProof);
                                    arrAdapCorresAddressProof.notifyDataSetChanged();

                                    arrAdapPermAddressProof = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrPermAddressProof);
                                    arrAdapPermAddressProof.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spSJHAddProofPerm.setAdapter(arrAdapPermAddressProof);
                                    spTJHAddProofPerm.setAdapter(arrAdapPermAddressProof);
                                    arrAdapPermAddressProof.notifyDataSetChanged();
                                }
                            }
                            if (response.has("AccountDetails")) {
                                JSONArray jsonArray = response.getJSONArray("AccountDetails");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);

                                        setDematAccType((object.isNull(DEMAT_ACC_TYPE) || object.getString(DEMAT_ACC_TYPE).equals("")) ? "" : object.getString(DEMAT_ACC_TYPE));
                                        setDematAccTitle((object.isNull(DEMAT_ACC_TITLE) || object.getString(DEMAT_ACC_TITLE).equals("")) ? "" : object.getString(DEMAT_ACC_TITLE));
                                        setDematFirstJointHolderFname((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_FNAME) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_FNAME).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_FNAME));
                                        setDematFirstJointHolderMname((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_MNAME) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_MNAME).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_MNAME));
                                        setDematFirstJointHolderLname((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_LNAME) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_LNAME).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_LNAME));
                                        setDematFirstJointHolderRelation((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_RELATION) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_RELATION).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_RELATION));
                                        setDematFirstJointHolderFHFname((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_FHFNAME) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_FHFNAME).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_FHFNAME));
                                        setDematFirstJointHolderFHMname((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_FHMNAME) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_FHMNAME).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_FHMNAME));
                                        setDematFirstJointHolderFHLname((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_FHLNAME) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_FHLNAME).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_FHLNAME));
                                        setDematFirstJointHolderAdd1((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_ADD1) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_ADD1).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_ADD1));
                                        setDematFirstJointHolderAdd2((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_ADD2) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_ADD2).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_ADD2));
                                        setDematFirstJointHolderAdd3((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_ADD3) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_ADD3).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_ADD3));
                                        setDematFirstJointHolderLandmark((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_LANDMARK) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_LANDMARK).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_LANDMARK));
                                        setDematFirstJointHolderPin((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_PIN) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_PIN).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_PIN));
                                        setDematFirstJointHolderCity((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_CITY) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_CITY).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_CITY));
                                        setDematFirstJointHolderState((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_STATE) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_STATE).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_STATE));
                                        setDematFirstJointHolderCountry((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_COUNTRY) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_COUNTRY).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_COUNTRY));
                                        setDematFirstJointHolderPan((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_PAN) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_PAN).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_PAN));
                                        setDematFirstJointHolderSex((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_SEX) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_SEX).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_SEX));
                                        setDematFirstJointHolderDOB((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_DATE_OF_BIRTH) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_DATE_OF_BIRTH).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_DATE_OF_BIRTH));
                                        setDematFirstJointHolderOccupation((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_OCCUPATION) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_OCCUPATION).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_OCCUPATION));
                                        setDematFirstJointHolderNatureBus((object.isNull(DEMAT_FIRST_JOINT_ACC_HOLDER_NATURE_BUS) || object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_NATURE_BUS).equals("")) ? "" : object.getString(DEMAT_FIRST_JOINT_ACC_HOLDER_NATURE_BUS));
                                        setDematFirstJointHolderMaritalStatus((object.isNull(FJ_MARITAL_STATUS) || object.getString(FJ_MARITAL_STATUS).equals("")) ? "" : object.getString(FJ_MARITAL_STATUS));
                                        setDematFirstJointHolderResidentialStatus((object.isNull(FJ_RESIDENTIAL_STATUS) || object.getString(FJ_RESIDENTIAL_STATUS).equals("")) ? "" : object.getString(FJ_RESIDENTIAL_STATUS));
                                        setDematFirstJointHolderUID((object.isNull(FJ_UID) || String.valueOf(object.getInt(FJ_UID)).equals("0")) ? "" : object.getString(FJ_UID));
                                        setDematFirstJointHolderEmail((object.isNull(FJ_EMAIL_ID) || object.getString(FJ_EMAIL_ID).equals("")) ? "" : object.getString(FJ_EMAIL_ID));
                                        setDematFirstJointHolderTeleNo((object.isNull(FJ_TELENO) || object.getString(FJ_TELENO).equals("")) ? "" : object.getString(FJ_TELENO));
                                        setDematFirstJointHolderAddressProof((object.isNull(FJ_ADDRESSPROOF) || object.getString(FJ_ADDRESSPROOF).equals("")) ? "" : object.getString(FJ_ADDRESSPROOF));
                                        setDematFirstJointHolderAddressProofPerm((object.isNull(FJ_ADDRESSPROOF_PERM) || object.getString(FJ_ADDRESSPROOF_PERM).equals("")) ? "" : object.getString(FJ_ADDRESSPROOF_PERM));
                                        setDematFirstJointHolderAnnualIncome((object.isNull(FJ_ANNUAL_INCOME) || object.getString(FJ_ANNUAL_INCOME).equals("")) ? "" : object.getString(FJ_ANNUAL_INCOME));
                                        setDematFirstJointHolderPEP((object.isNull(FJ_PEP) || object.getString(FJ_PEP).equals("")) ? "" : object.getString(FJ_PEP));
                                        setDematFirstJointHolderRPER((object.isNull(FJ_RPER) || object.getString(FJ_RPER).equals("")) ? "" : object.getString(FJ_RPER));
                                        setDematFirstJointHolderSameAsPerm((object.isNull(FJ_SAME_AS_PERM) || object.getString(FJ_SAME_AS_PERM).equals("")) ? "" : object.getString(FJ_SAME_AS_PERM));
                                        setDematFirstJointHolderMobile((object.isNull(FJ_MOBILE) || object.getString(FJ_MOBILE).equals("")) ? "" : object.getString(FJ_MOBILE));
                                        setDematSecondAccTitle((object.isNull(DEMAT_SECOND_ACC_TITLE) || object.getString(DEMAT_SECOND_ACC_TITLE).equals("")) ? "" : object.getString(DEMAT_SECOND_ACC_TITLE));
                                        setDematSecondJointHolderFname((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_FNAME) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_FNAME).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_FNAME));
                                        setDematSecondJointHolderMname((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_MNAME) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_MNAME).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_MNAME));
                                        setDematSecondJointHolderLname((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_LNAME) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_LNAME).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_LNAME));
                                        setDematSecondJointHolderRelation((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_RELATION) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_RELATION).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_RELATION));
                                        setDematSecondJointHolderFHFname((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_FHFNAME) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_FHFNAME).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_FHFNAME));
                                        setDematSecondJointHolderFHMname((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_FHMNAME) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_FHMNAME).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_FHMNAME));
                                        setDematSecondJointHolderFHLname((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_FHLNAME) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_FHLNAME).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_FHLNAME));
                                        setDematSecondJointHolderAdd1((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_ADD1) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_ADD1).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_ADD1));
                                        setDematSecondJointHolderAdd2((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_ADD2) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_ADD2).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_ADD2));
                                        setDematSecondJointHolderAdd3((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_ADD3) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_ADD3).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_ADD3));
                                        setDematSecondJointHolderLandmark((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_LANDMARK) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_LANDMARK).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_LANDMARK));
                                        setDematSecondJointHolderPin((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_PIN) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_PIN).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_PIN));
                                        setDematSecondJointHolderCity((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_CITY) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_CITY).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_CITY));
                                        setDematSecondJointHolderState((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_STATE) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_STATE).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_STATE));
                                        setDematSecondJointHolderCountry((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_COUNTRY) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_COUNTRY).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_COUNTRY));
                                        setDematSecondJointHolderPan((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_PAN) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_PAN).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_PAN));
                                        setDematSecondJointHolderSex((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_SEX) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_SEX).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_SEX));
                                        setDematSecondJointHolderDOB((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_DATE_OF_BIRTH) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_DATE_OF_BIRTH).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_DATE_OF_BIRTH));
                                        setDematSecondJointHolderOccupation((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_OCCUPATION) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_OCCUPATION).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_OCCUPATION));
                                        setDematSecondJointHolderNatureBus((object.isNull(DEMAT_SECOND_JOINT_ACC_HOLDER_NATURE_BUS) || object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_NATURE_BUS).equals("")) ? "" : object.getString(DEMAT_SECOND_JOINT_ACC_HOLDER_NATURE_BUS));
                                        setDematSecondJointHolderMaritalStatus((object.isNull(SJ_MARITAL_STATUS) || object.getString(SJ_MARITAL_STATUS).equals("")) ? "" : object.getString(SJ_MARITAL_STATUS));
                                        setDematSecondJointHolderResidentialStatus((object.isNull(SJ_RESIDENTIAL_STATUS) || object.getString(SJ_RESIDENTIAL_STATUS).equals("")) ? "" : object.getString(SJ_RESIDENTIAL_STATUS));
                                        setDematSecondJointHolderUID((object.isNull(SJ_UID) || String.valueOf(object.getInt(SJ_UID)).equals("0")) ? "" : object.getString(SJ_UID));
                                        setDematSecondJointHolderEmail((object.isNull(SJ_EMAIL_ID) || object.getString(SJ_EMAIL_ID).equals("")) ? "" : object.getString(SJ_EMAIL_ID));
                                        setDematSecondJointHolderTeleNo((object.isNull(SJ_TELENO) || object.getString(SJ_TELENO).equals("")) ? "" : object.getString(SJ_TELENO));
                                        setDematSecondJointHolderAddressProof((object.isNull(SJ_ADDRESSPROOF) || object.getString(SJ_ADDRESSPROOF).equals("")) ? "" : object.getString(SJ_ADDRESSPROOF));
                                        setDematSecondJointHolderAddressProofPerm((object.isNull(SJ_ADDRESSPROOF_PERM) || object.getString(SJ_ADDRESSPROOF_PERM).equals("")) ? "" : object.getString(SJ_ADDRESSPROOF_PERM));
                                        setDematSecondJointHolderAnnualIncome((object.isNull(SJ_ANNUAL_INCOME) || object.getString(SJ_ANNUAL_INCOME).equals("")) ? "" : object.getString(SJ_ANNUAL_INCOME));
                                        setDematSecondJointHolderPEP((object.isNull(SJ_PEP) || object.getString(SJ_PEP).equals("")) ? "" : object.getString(SJ_PEP));
                                        setDematSecondJointHolderRPER((object.isNull(SJ_RPER) || object.getString(SJ_RPER).equals("")) ? "" : object.getString(SJ_RPER));
                                        setDematSecondJointHolderSameAsPerm((object.isNull(SJ_SAME_AS_PERM) || object.getString(SJ_SAME_AS_PERM).equals("")) ? "" : object.getString(SJ_SAME_AS_PERM));
                                        setDematSecondJointHolderMobile((object.isNull(SJ_MOBILE) || object.getString(SJ_MOBILE).equals("")) ? "" : object.getString(SJ_MOBILE));
                                        setDematFirstJointHolderAdd1Perm((object.isNull(FJ_ADD1_PERM) || object.getString(FJ_ADD1_PERM).equals("")) ? "" : object.getString(FJ_ADD1_PERM));
                                        setDematFirstJointHolderAdd2Perm((object.isNull(FJ_ADD2_PERM) || object.getString(FJ_ADD2_PERM).equals("")) ? "" : object.getString(FJ_ADD2_PERM));
                                        setDematFirstJointHolderAdd3Perm((object.isNull(FJ_ADD3_PERM) || object.getString(FJ_ADD3_PERM).equals("")) ? "" : object.getString(FJ_ADD3_PERM));
                                        setDematFirstJointHolderLandmarkPerm((object.isNull(FJ_LANDMARK_PERM) || object.getString(FJ_LANDMARK_PERM).equals("")) ? "" : object.getString(FJ_LANDMARK_PERM));
                                        setDematFirstJointHolderPinPerm((object.isNull(FJ_PINCODE_PERM) || object.getString(FJ_PINCODE_PERM).equals("")) ? "" : object.getString(FJ_PINCODE_PERM));
                                        setDematFirstJointHolderStatePerm((object.isNull(FJ_STATE_PERM) || object.getString(FJ_STATE_PERM).equals("")) ? "" : object.getString(FJ_STATE_PERM));
                                        setDematFirstJointHolderCityPerm((object.isNull(FJ_CITY_PERM) || object.getString(FJ_CITY_PERM).equals("")) ? "" : object.getString(FJ_CITY_PERM));
                                        setFjOtherCityDematFirstHolderPerm((object.isNull(FJ_OTHER_CITY_DEMAT_FIRST_HOLDER_PERM) || object.getString(FJ_OTHER_CITY_DEMAT_FIRST_HOLDER_PERM).equals("")) ? "" : object.getString(FJ_OTHER_CITY_DEMAT_FIRST_HOLDER_PERM));
                                        setDematFirstJointHolderCountryPerm((object.isNull(FJ_COUNTRY_PERM) || object.getString(FJ_COUNTRY_PERM).equals("")) ? "" : object.getString(FJ_COUNTRY_PERM));
                                        setDematSecondJointHolderAdd1Perm((object.isNull(SJ_ADD1_PERM) || object.getString(SJ_ADD1_PERM).equals("")) ? "" : object.getString(SJ_ADD1_PERM));
                                        setDematSecondJointHolderAdd2Perm((object.isNull(SJ_ADD2_PERM) || object.getString(SJ_ADD2_PERM).equals("")) ? "" : object.getString(SJ_ADD2_PERM));
                                        setDematSecondJointHolderAdd3Perm((object.isNull(SJ_ADD3_PERM) || object.getString(SJ_ADD3_PERM).equals("")) ? "" : object.getString(SJ_ADD3_PERM));
                                        setDematSecondJointHolderLandmarkPerm((object.isNull(SJ_LANDMARK_PERM) || object.getString(SJ_LANDMARK_PERM).equals("")) ? "" : object.getString(SJ_LANDMARK_PERM));
                                        setDematSecondJointHolderPinPerm((object.isNull(SJ_PINCODE_PERM) || object.getString(SJ_PINCODE_PERM).equals("")) ? "" : object.getString(SJ_PINCODE_PERM));
                                        setDematSecondJointHolderStatePerm((object.isNull(SJ_STATE_PERM) || object.getString(SJ_STATE_PERM).equals("")) ? "" : object.getString(SJ_STATE_PERM));
                                        setDematSecondJointHolderCityPerm((object.isNull(SJ_CITY_PERM) || object.getString(SJ_CITY_PERM).equals("")) ? "" : object.getString(SJ_CITY_PERM));
                                        setSjOtherCityDematFirstHolderPerm((object.isNull(SJ_OTHER_CITY_DEMAT_FIRST_HOLDER_PERM) || object.getString(SJ_OTHER_CITY_DEMAT_FIRST_HOLDER_PERM).equals("")) ? "" : object.getString(SJ_OTHER_CITY_DEMAT_FIRST_HOLDER_PERM));
                                        setDematSecondJointHolderCountryPerm((object.isNull(SJ_COUNTRY_PERM) || object.getString(SJ_COUNTRY_PERM).equals("")) ? "" : object.getString(SJ_COUNTRY_PERM));

                                        if (getDematAccType().equalsIgnoreCase("Joint")) {
                                            toggleJointHolder.setChecked(true);
                                        } else if (getDematAccTitle().equalsIgnoreCase("Individual")) {
                                            toggleJointHolder.setChecked(false);
                                        }
                                        if (getDematFirstJointHolderFname().equals("") && getDematSecondJointHolderFname().equals("")) {
                                            llNumberOfJointHolder.setVisibility(View.GONE);
                                            spNumberOfJointHolder.setSelection(0);
                                            llJointHolderDetail.setVisibility(View.GONE);
                                            rlSecondJointHolder.setVisibility(View.GONE);
                                        } else if (!getDematSecondJointHolderFname().equals("")) {
                                            llNumberOfJointHolder.setVisibility(View.VISIBLE);
                                            spNumberOfJointHolder.setSelection(2);
                                            llJointHolderDetail.setVisibility(View.VISIBLE);
                                            rlSecondJointHolder.setVisibility(View.VISIBLE);
                                        } else {
                                            llNumberOfJointHolder.setVisibility(View.VISIBLE);
                                            spNumberOfJointHolder.setSelection(1);
                                            llJointHolderDetail.setVisibility(View.VISIBLE);
                                            rlSecondJointHolder.setVisibility(View.VISIBLE);
                                        }
                                        //Second holder
                                        etSJHFirstName.setText(getDematFirstJointHolderFname());
                                        etSJHMiddleName.setText(getDematFirstJointHolderMname());
                                        etSJHLastName.setText(getDematFirstJointHolderLname());
                                        etSJHFatherSpouseFirstName.setText(getDematFirstJointHolderFHFname());
                                        etSJHFatherSpouseMidName.setText(getDematFirstJointHolderFHMname());
                                        etSJHFatherSpouseLastName.setText(getDematFirstJointHolderFHLname());
                                        etSJHBldgNameCorres.setText(getDematFirstJointHolderAdd1());
                                        etSJHFlatNoCorres.setText(getDematFirstJointHolderAdd2());
                                        etSJHAreaCorres.setText(getDematFirstJointHolderAdd3());
                                        etSJHPincodeCorres.setText(getDematFirstJointHolderPin());
                                        etSJHBldgNamePerm.setText(getDematFirstJointHolderAdd1Perm());
                                        etSJHFlatNamePerm.setText(getDematFirstJointHolderAdd2Perm());
                                        etSJHAreaPerm.setText(getDematFirstJointHolderAdd3Perm());
                                        etSJHPincodePerm.setText(getDematFirstJointHolderPinPerm());
                                        etSJHPAN.setText(getDematFirstJointHolderPan());
                                        if (getDematFirstJointHolderUID().equalsIgnoreCase("")) {
                                            etSJHAadhar.setText(getDematFirstJointHolderUID());
                                        } else {
//                                            BigDecimal bd = new BigDecimal(getDematFirstJointHolderUID());
//                                            etSJHAadhar.setText(String.valueOf(bd.longValue()));
                                            etSJHAadhar.setText(getDematFirstJointHolderUID());
                                        }
                                        etSJHEmail.setText(getDematFirstJointHolderEmail());
                                        if (!isBlank(getDematFirstJointHolderTeleNo())) {
                                            String[] parts = getDematFirstJointHolderTeleNo().split("~");
                                            if (parts.length > 1) {
                                                String std = parts[0];
                                                String tel = parts[1];
                                                etSJHSTDNo.setText(std);
                                                etSJHTelNo.setText(tel);
                                            } else {
                                                if (getDematFirstJointHolderTeleNo().equals("~")) {
                                                    etSJHTelNo.setText("");
                                                } else {
                                                    etSJHTelNo.setText(getDematFirstJointHolderTeleNo());
                                                }
                                            }
                                        } else {
                                            etSJHSTDNo.setText("");
                                            etSJHTelNo.setText("");
                                        }
//                                        etSJHTelNo.setText(getDematFirstJointHolderTeleNo());
                                        etSJHMobile.setText(getDematFirstJointHolderMobile());
                                        etSJHDOB.setText(getDematFirstJointHolderDOB());
                                        if (getDematFirstJointHolderSex().equalsIgnoreCase("Male") || getDematFirstJointHolderSex().equalsIgnoreCase("M")) {
                                            rbSJHMale.setChecked(true);
                                            rbSJHFemale.setChecked(false);
                                            /* cbSJHMale.setChecked(true);
                                            cbSJHFemale.setChecked(false);*/
                                        } else if (getDematFirstJointHolderSex().equalsIgnoreCase("Female") || getDematFirstJointHolderSex().equalsIgnoreCase("F")) {
                                            rbSJHFemale.setChecked(true);
                                            rbSJHMale.setChecked(false);
                                           /* cbSJHFemale.setChecked(true);
                                            cbSJHMale.setChecked(false);*/
                                        }

                                        if (!(getDematFirstJointHolderMaritalStatus().equalsIgnoreCase(""))) {
                                            if (getDematFirstJointHolderMaritalStatus().equalsIgnoreCase("Single")) {
                                                rbSJHUnMarried.setChecked(true);
                                                rbSJHMarried.setChecked(false);
                                               /* cbSJHUnMarried.setChecked(true);
                                                cbSJHMarried.setChecked(false);*/
                                            } else if (getDematFirstJointHolderMaritalStatus().equalsIgnoreCase("MARRIED")) {
                                                rbSJHMarried.setChecked(true);
                                                rbSJHUnMarried.setChecked(false);
                                              /*  cbSJHMarried.setChecked(true);
                                                cbSJHUnMarried.setChecked(false);*/
                                            }
                                        }

                                        if (getDematFirstJointHolderPEP().equalsIgnoreCase("Yes")) {
                                            toggleSJHPoliticaly.setChecked(true);
                                           /* cbSJHPoliticalyYes.setChecked(true);
                                            cbSJHPoliticalyNo.setChecked(false);*/
                                        } else {
                                            toggleSJHPoliticaly.setChecked(false);
                                            /*cbSJHPoliticalyNo.setChecked(true);
                                            cbSJHPoliticalyYes.setChecked(false);*/
                                        }
                                        if (getDematFirstJointHolderRPER().equalsIgnoreCase("Yes")) {
                                            toggleSJHRelatedPolitical.setChecked(true);
                                           /* cbSJHRelatedPoliticalYes.setChecked(true);
                                            cbSJHRelatedPoliticalNo.setChecked(false);*/
                                        } else {
                                            toggleSJHRelatedPolitical.setChecked(false);
                                          /*  cbSJHRelatedPoliticalNo.setChecked(true);
                                            cbSJHRelatedPoliticalYes.setChecked(false);*/
                                        }

                                        if (getDematFirstJointHolderSameAsPerm().equalsIgnoreCase(True)) {
                                            cbSJHSameAsPrimaryCorrespondence.setChecked(true);
                                            if (MOSLCommon.isBlank(getDematFirstJointHolderAdd1())) {
                                                etSJHBldgNameCorres.setEnabled(true);
                                            } else {
                                                etSJHBldgNameCorres.setEnabled(false);
                                            }
                                            if (MOSLCommon.isBlank(getDematFirstJointHolderAdd2())) {
                                                etSJHFlatNoCorres.setEnabled(true);
                                            } else {
                                                etSJHFlatNoCorres.setEnabled(false);
                                            }
                                            if (MOSLCommon.isBlank(getDematFirstJointHolderAdd3())) {
                                                etSJHAreaCorres.setEnabled(true);
                                            } else {
                                                etSJHAreaCorres.setEnabled(false);
                                            }
                                            if (MOSLCommon.isBlank(getDematFirstJointHolderPin())) {
                                                etSJHPincodeCorres.setEnabled(true);
                                            } else {
                                                etSJHPincodeCorres.setEnabled(false);
                                            }
                                            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                                                spSJHCorresState.setVisibility(View.GONE);
                                                spSJHCorresCity.setVisibility(View.GONE);
                                                etSJHCorresState.setVisibility(View.VISIBLE);
                                                etSJHCorresCity.setVisibility(View.VISIBLE);
                                                if (MOSLCommon.isBlank(getDematFirstJointHolderCity())) {
                                                    etSJHCorresCity.setEnabled(true);
                                                } else {
                                                    etSJHCorresCity.setEnabled(false);
                                                }
                                                if (MOSLCommon.isBlank(getDematFirstJointHolderState())) {
                                                    etSJHCorresState.setEnabled(true);
                                                } else {
                                                    etSJHCorresState.setEnabled(false);
                                                }
                                            } else {
                                                spSJHCorresState.setVisibility(View.VISIBLE);
                                                spSJHCorresCity.setVisibility(View.VISIBLE);
                                                etSJHCorresState.setVisibility(View.GONE);
                                                etSJHCorresCity.setVisibility(View.GONE);
                                                if (MOSLCommon.isBlank(getDematFirstJointHolderCity())) {
                                                    spSJHCorresCity.setEnabled(true);
                                                } else {
                                                    spSJHCorresCity.setEnabled(false);
                                                }
                                                if (MOSLCommon.isBlank(getDematFirstJointHolderState())) {
                                                    spSJHCorresState.setEnabled(true);
                                                } else {
                                                    spSJHCorresState.setEnabled(false);
                                                }
                                            }
                                            if (MOSLCommon.isBlank(getDematFirstJointHolderCountry())) {
                                                spSJHCorresCountry.setEnabled(true);
                                            } else {
                                                spSJHCorresCountry.setEnabled(false);
                                            }
                                        } else {
                                            cbSJHSameAsPrimaryCorrespondence.setChecked(false);
                                            etSJHBldgNameCorres.setEnabled(true);
                                            etSJHFlatNoCorres.setEnabled(true);
                                            etSJHAreaCorres.setEnabled(true);
                                            spSJHAddProofCorres.setEnabled(true);
                                            etSJHPincodeCorres.setEnabled(true);
                                            spSJHCorresCity.setEnabled(true);
                                            spSJHCorresState.setEnabled(true);
                                            spSJHCorresCountry.setEnabled(true);
                                            spSJHCorresState.setVisibility(View.VISIBLE);
                                            spSJHCorresCity.setVisibility(View.VISIBLE);
                                            etSJHCorresState.setVisibility(View.GONE);
                                            etSJHCorresCity.setVisibility(View.GONE);
                                        }

                                        //If address mode is manual spinner will be visible otherwise edittext
                                        if (cbSJHSameAsPrimaryCorrespondence.isChecked() && (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL))) {
                                            spSJHCorresState.setVisibility(View.GONE);
                                            spSJHCorresCity.setVisibility(View.GONE);
                                            spSJHPermState.setVisibility(View.GONE);
                                            spSJHPermCity.setVisibility(View.GONE);
                                            etSJHCorresState.setVisibility(View.VISIBLE);
                                            etSJHCorresCity.setVisibility(View.VISIBLE);
                                            etSJHPermState.setVisibility(View.VISIBLE);
                                            etSJHPermCity.setVisibility(View.VISIBLE);
                                            etSJHCorresState.setText(getDematFirstJointHolderState());
                                            etSJHCorresCity.setText(getDematFirstJointHolderCity());
                                            etSJHPermState.setText(getDematFirstJointHolderStatePerm());
                                            etSJHPermCity.setText(getDematFirstJointHolderCityPerm());
                                        } else {
                                            spSJHCorresState.setVisibility(View.VISIBLE);
                                            spSJHCorresCity.setVisibility(View.VISIBLE);
                                            spSJHPermState.setVisibility(View.VISIBLE);
                                            spSJHPermCity.setVisibility(View.VISIBLE);
                                            etSJHCorresState.setVisibility(View.GONE);
                                            etSJHCorresCity.setVisibility(View.GONE);
                                            etSJHPermState.setVisibility(View.GONE);
                                            etSJHPermCity.setVisibility(View.GONE);
                                            if (arrAdapState == null) {
                                                getState(getDematFirstJointHolderState(), getDematFirstJointHolderStatePerm(), getDematFirstJointHolderCity(), getDematFirstJointHolderCityPerm(), FirstJoint);
                                            } else {
                                                RowData corresStateData = getSpinnerRowDataByValue(getDematFirstJointHolderState(), arrAdapState);
                                                if (corresStateData != null) {
                                                    spSJHCorresState.setSelection(corresStateData.getPosition());
//                                                getCorresCity(((RowData) spSJHCorresState.getSelectedItem()).getKey(), getDematFirstJointHolderCity(), FirstJoint);
                                                } else {
                                                    if (arrAdapCorresCity == null) {
                                                        arrCorresCity = new ArrayList<>();
                                                        arrAdapCorresCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresCity);
                                                        arrAdapCorresCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spSJHCorresCity.setAdapter(arrAdapCorresCity);
                                                    }
                                                    arrAdapCorresCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapCorresCity.notifyDataSetChanged();
                                                    cityFoundFlag = true;
                                                }

                                                RowData permanentStateData = getSpinnerRowDataByValue(getDematFirstJointHolderStatePerm(), arrAdapState);
                                                if (permanentStateData != null) {
                                                    spSJHPermState.setSelection(permanentStateData.getPosition());
//                                                getPermanentCity(((RowData) spSJHPermState.getSelectedItem()).getKey(), getDematFirstJointHolderCityPerm(), FirstJoint);
                                                } else {
                                                    if (arrAdapPermCity == null) {
                                                        arrPermCity = new ArrayList<>();
                                                        arrAdapPermCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrPermCity);
                                                        arrAdapPermCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spSJHPermCity.setAdapter(arrAdapPermCity);
                                                    }
                                                    arrAdapPermCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapPermCity.notifyDataSetChanged();
                                                    cityFoundFlag = true;
                                                }
                                            }

                                        }

                                        RowData corresAddressProofData = getSpinnerRowDataByValue(getDematFirstJointHolderAddressProof(), arrAdapCorresAddressProof);
                                        if (corresAddressProofData != null) {
                                            spSJHAddProofCorres.setSelection(corresAddressProofData.getPosition());
                                        }

                                        RowData permanentAddressProofData = getSpinnerRowDataByValue(getDematFirstJointHolderAddressProofPerm(), arrAdapPermAddressProof);
                                        if (permanentAddressProofData != null) {
                                            spSJHAddProofPerm.setSelection(permanentAddressProofData.getPosition());
                                        }

                                        RowData tjhCorresAddressProofData = getSpinnerRowDataByValue(getDematSecondJointHolderAddressProof(), arrAdapCorresAddressProof);
                                        if (tjhCorresAddressProofData != null) {
                                            spTJHAddProofCorres.setSelection(tjhCorresAddressProofData.getPosition());
                                        }

                                        RowData tjhPermanentAddressProofData = getSpinnerRowDataByValue(getDematSecondJointHolderAddressProofPerm(), arrAdapPermAddressProof);
                                        if (tjhPermanentAddressProofData != null) {
                                            spTJHAddProofPerm.setSelection(tjhPermanentAddressProofData.getPosition());
                                        }

                                        getSpinnerData(arrAdapResidentStatus, spSJHResidentStatus, getDematFirstJointHolderResidentialStatus());
                                        getSpinnerData(arrAdapTitle, spSJHTitle, getDematAccTitle());
                                        getSpinnerData(arrAdapFatherSpouseSel, spSJHFatherSpouseSel, getDematFirstJointHolderRelation());
                                        getSpinnerData(arrAdapOcc, spSJHOccupation, getDematFirstJointHolderOccupation());
                                        getSpinnerData(arrAdapIncome, spSJHIncome, getDematFirstJointHolderAnnualIncome());
                                        getSpinnerData(arrAdapCountry, spSJHCorresCountry, getDematFirstJointHolderCountry());
                                        getSpinnerData(arrAdapCountry, spSJHPermCountry, getDematFirstJointHolderCountryPerm());

                                        //Third holder
                                        etTJHFirstName.setText(getDematSecondJointHolderFname());
                                        etTJHMiddleName.setText(getDematSecondJointHolderMname());
                                        etTJHLastName.setText(getDematSecondJointHolderLname());
                                        etTJHFatherSpouseFirstName.setText(getDematSecondJointHolderFHFname());
                                        etTJHFatherSpouseMidName.setText(getDematSecondJointHolderFHMname());
                                        etTJHFatherSpouseLastName.setText(getDematSecondJointHolderFHLname());
                                        etTJHBldgNameCorres.setText(getDematSecondJointHolderAdd1());
                                        etTJHFlatNoCorres.setText(getDematSecondJointHolderAdd2());
                                        etTJHAreaCorres.setText(getDematSecondJointHolderAdd3());
                                        etTJHPincodeCorres.setText(getDematSecondJointHolderPin());
                                        etTJHBldgNamePerm.setText(getDematSecondJointHolderAdd1Perm());
                                        etTJHFlatNamePerm.setText(getDematSecondJointHolderAdd2Perm());
                                        etTJHAreaPerm.setText(getDematSecondJointHolderAdd3Perm());
                                        etTJHPincodePerm.setText(getDematSecondJointHolderPinPerm());
                                        etTJHPAN.setText(getDematSecondJointHolderPan());
                                        if (getDematSecondJointHolderUID().equalsIgnoreCase("")) {
                                            etTJHAadhar.setText(getDematSecondJointHolderUID());
                                        } else {
//                                            BigDecimal bd = new BigDecimal(getDematSecondJointHolderUID());
//                                            etTJHAadhar.setText(String.valueOf(bd.longValue()));
                                            etTJHAadhar.setText(getDematSecondJointHolderUID());
                                        }
                                        etTJHEmail.setText(getDematSecondJointHolderEmail());
                                        if (!isBlank(getDematSecondJointHolderTeleNo())) {
                                            String[] parts = getDematSecondJointHolderTeleNo().split("~");
                                            if (parts.length > 1) {
                                                String std = parts[0];
                                                String tel = parts[1];
                                                etTJHSTDNo.setText(std);
                                                etTJHTelNo.setText(tel);
                                            } else {
                                                if (getDematSecondJointHolderTeleNo().equals("~")) {
                                                    etTJHTelNo.setText("");
                                                } else {
                                                    etTJHTelNo.setText(getDematSecondJointHolderTeleNo());
                                                }
                                            }
                                        } else {
                                            etTJHSTDNo.setText("");
                                            etTJHTelNo.setText("");
                                        }
//                                        etTJHTelNo.setText(getDematSecondJointHolderTeleNo());
                                        etTJHMobile.setText(getDematSecondJointHolderMobile());
                                        etTJHDOB.setText(getDematSecondJointHolderDOB());

                                        if (getDematSecondJointHolderSex().equalsIgnoreCase("Male") || getDematSecondJointHolderSex().equalsIgnoreCase("M")) {
                                            rbTJHMale.setChecked(true);
                                            rbTJHFemale.setChecked(false);
                                            /*  cbTJHMale.setChecked(true);
                                            cbTJHFemale.setChecked(false);*/
                                        } else if (getDematSecondJointHolderSex().equalsIgnoreCase("Female") || getDematSecondJointHolderSex().equalsIgnoreCase("F")) {
                                            rbTJHFemale.setChecked(true);
                                            rbTJHMale.setChecked(false);
                                           /* cbTJHFemale.setChecked(true);
                                            cbTJHMale.setChecked(false);*/
                                        }

                                        if (!(getDematSecondJointHolderMaritalStatus().equalsIgnoreCase(""))) {
                                            if (getDematSecondJointHolderMaritalStatus().equalsIgnoreCase("Single")) {
                                                rbTJHUnMarried.setChecked(true);
                                                rbTJHMarried.setChecked(false);
                                               /* cbTJHUnMarried.setChecked(true);
                                                cbTJHMarried.setChecked(false);*/
                                            } else if (getDematSecondJointHolderMaritalStatus().equalsIgnoreCase("MARRIED")) {
                                                rbTJHMarried.setChecked(true);
                                                rbTJHUnMarried.setChecked(false);
                                               /* cbTJHMarried.setChecked(true);
                                                cbTJHUnMarried.setChecked(false);*/
                                            }
                                        }

                                        if (getDematSecondJointHolderPEP().equalsIgnoreCase("Yes")) {
                                            toggleTJHPoliticaly.setChecked(true);
                                            /*cbTJHPoliticalyYes.setChecked(true);
                                            cbTJHPoliticalyNo.setChecked(false);*/
                                        } else {
                                            toggleTJHPoliticaly.setChecked(false);
                                           /* cbTJHPoliticalyNo.setChecked(true);
                                            cbTJHPoliticalyYes.setChecked(false);*/
                                        }
                                        if (getDematSecondJointHolderRPER().equalsIgnoreCase("Yes")) {
                                            toggleTJHRelatedPolitical.setChecked(true);
                                           /* cbTJHRelatedPoliticalYes.setChecked(true);
                                            cbTJHRelatedPoliticalNo.setChecked(false);*/
                                        } else {
                                            toggleTJHRelatedPolitical.setChecked(false);
                                            /* cbTJHRelatedPoliticalNo.setChecked(true);
                                            cbTJHRelatedPoliticalYes.setChecked(false);*/
                                        }

                                        if (getDematSecondJointHolderSameAsPerm().equalsIgnoreCase(True)) {
                                            cbTJHSameAsPrimaryCorrespondence.setChecked(true);
                                            if (MOSLCommon.isBlank(getDematSecondJointHolderAdd1())) {
                                                etTJHBldgNameCorres.setEnabled(true);
                                            } else {
                                                etTJHBldgNameCorres.setEnabled(false);
                                            }
                                            if (MOSLCommon.isBlank(getDematSecondJointHolderAdd2())) {
                                                etTJHFlatNoCorres.setEnabled(true);
                                            } else {
                                                etTJHFlatNoCorres.setEnabled(false);
                                            }
                                            if (MOSLCommon.isBlank(getDematSecondJointHolderAdd3())) {
                                                etTJHAreaCorres.setEnabled(true);
                                            } else {
                                                etTJHAreaCorres.setEnabled(false);
                                            }
                                            if (MOSLCommon.isBlank(getDematSecondJointHolderPin())) {
                                                etTJHPincodeCorres.setEnabled(true);
                                            } else {
                                                etTJHPincodeCorres.setEnabled(false);
                                            }
                                            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                                                spTJHCorresState.setVisibility(View.GONE);
                                                spTJHCorresCity.setVisibility(View.GONE);
                                                etTJHCorresState.setVisibility(View.VISIBLE);
                                                etTJHCorresCity.setVisibility(View.VISIBLE);
                                                if (MOSLCommon.isBlank(getDematSecondJointHolderCity())) {
                                                    etTJHCorresCity.setEnabled(true);
                                                } else {
                                                    etTJHCorresCity.setEnabled(false);
                                                }
                                                if (MOSLCommon.isBlank(getDematSecondJointHolderState())) {
                                                    etTJHCorresState.setEnabled(true);
                                                } else {
                                                    etTJHCorresState.setEnabled(false);
                                                }
                                            } else {
                                                spTJHCorresState.setVisibility(View.VISIBLE);
                                                spTJHCorresCity.setVisibility(View.VISIBLE);
                                                etTJHCorresState.setVisibility(View.GONE);
                                                etTJHCorresCity.setVisibility(View.GONE);
                                                if (MOSLCommon.isBlank(getDematSecondJointHolderCity())) {
                                                    spTJHCorresCity.setEnabled(true);
                                                } else {
                                                    spTJHCorresCity.setEnabled(false);
                                                }
                                                if (MOSLCommon.isBlank(getDematSecondJointHolderState())) {
                                                    spTJHCorresState.setEnabled(true);
                                                } else {
                                                    spTJHCorresState.setEnabled(false);
                                                }
                                            }
                                            if (MOSLCommon.isBlank(getDematSecondJointHolderCountry())) {
                                                spTJHCorresCountry.setEnabled(true);
                                            } else {
                                                spTJHCorresCountry.setEnabled(false);
                                            }
                                        } else {
                                            cbTJHSameAsPrimaryCorrespondence.setChecked(false);
                                            etTJHBldgNameCorres.setEnabled(true);
                                            etTJHFlatNoCorres.setEnabled(true);
                                            etTJHAreaCorres.setEnabled(true);
                                            spTJHAddProofCorres.setEnabled(true);
                                            etTJHPincodeCorres.setEnabled(true);
                                            spTJHCorresCity.setEnabled(true);
                                            spTJHCorresState.setEnabled(true);
                                            spTJHCorresCountry.setEnabled(true);
                                            spTJHCorresState.setVisibility(View.VISIBLE);
                                            spTJHCorresCity.setVisibility(View.VISIBLE);
                                            etTJHCorresState.setVisibility(View.GONE);
                                            etTJHCorresCity.setVisibility(View.GONE);
                                        }

                                        //If address mode is manual spinner will be visible otherwise edittext
                                        if (cbTJHSameAsPrimaryCorrespondence.isChecked() && (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL))) {
                                            spTJHCorresState.setVisibility(View.GONE);
                                            spTJHCorresCity.setVisibility(View.GONE);
                                            spTJHPermState.setVisibility(View.GONE);
                                            spTJHPermCity.setVisibility(View.GONE);
                                            etTJHCorresState.setVisibility(View.VISIBLE);
                                            etTJHCorresCity.setVisibility(View.VISIBLE);
                                            etTJHPermState.setVisibility(View.VISIBLE);
                                            etTJHPermCity.setVisibility(View.VISIBLE);
                                            etTJHCorresState.setText(getDematSecondJointHolderState());
                                            etTJHCorresCity.setText(getDematSecondJointHolderCity());
                                            etTJHPermState.setText(getDematSecondJointHolderStatePerm());
                                            etTJHPermCity.setText(getDematSecondJointHolderCityPerm());
                                        } else {
                                            spTJHCorresState.setVisibility(View.VISIBLE);
                                            spTJHCorresCity.setVisibility(View.VISIBLE);
                                            spTJHPermState.setVisibility(View.VISIBLE);
                                            spTJHPermCity.setVisibility(View.VISIBLE);
                                            etTJHCorresState.setVisibility(View.GONE);
                                            etTJHCorresCity.setVisibility(View.GONE);
                                            etTJHPermState.setVisibility(View.GONE);
                                            etTJHPermCity.setVisibility(View.GONE);
                                            if (arrAdapState == null) {
                                                getState(getDematSecondJointHolderState(), getDematSecondJointHolderStatePerm(), getDematSecondJointHolderCity(), getDematSecondJointHolderCityPerm(), SecondJoint);
                                            } else {
                                                RowData corresStateData = getSpinnerRowDataByValue(dematSecondJointHolderState, arrAdapState);
                                                if (corresStateData != null) {
                                                    spTJHCorresState.setSelection(corresStateData.getPosition());
//                                                getCorresCity(((RowData) spTJHCorresState.getSelectedItem()).getKey(), getDematSecondJointHolderCity(), SecondJoint);
                                                } else {
                                                    if (arrAdapCorresCity == null) {
                                                        arrCorresCity = new ArrayList<>();
                                                        arrAdapCorresCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresCity);
                                                        arrAdapCorresCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spTJHCorresCity.setAdapter(arrAdapCorresCity);
                                                    }
                                                    arrAdapCorresCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapCorresCity.notifyDataSetChanged();
                                                    cityFoundFlag = true;
                                                }

                                                RowData permanentStateData = getSpinnerRowDataByValue(getDematSecondJointHolderStatePerm(), arrAdapState);
                                                if (permanentStateData != null) {
                                                    spTJHPermState.setSelection(permanentStateData.getPosition());
//                                                getPermanentCity(((RowData) spTJHPermState.getSelectedItem()).getKey(), getDematSecondJointHolderCityPerm(), SecondJoint);
                                                } else {
                                                    if (arrAdapPermCity == null) {
                                                        arrPermCity = new ArrayList<>();
                                                        arrAdapPermCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrPermCity);
                                                        arrAdapPermCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spTJHPermCity.setAdapter(arrAdapPermCity);
                                                    }
                                                    arrAdapPermCity.add(new RowData(0, "-1", "Select"));
                                                    arrAdapPermCity.notifyDataSetChanged();
                                                    cityFoundFlag = true;
                                                }
                                            }
                                        }

                                        getSpinnerData(arrAdapResidentStatus, spTJHResidentStatus, getDematSecondJointHolderResidentialStatus());
                                        getSpinnerData(arrAdapTitle, spTJHTitle, getDematSecondAccTitle());
                                        getSpinnerData(arrAdapFatherSpouseSel, spTJHFatherSpouseSel, getDematSecondJointHolderRelation());
                                        getSpinnerData(arrAdapOcc, spTJHOccupation, getDematSecondJointHolderOccupation());
                                        getSpinnerData(arrAdapIncome, spTJHIncome, getDematSecondJointHolderAnnualIncome());
                                        getSpinnerData(arrAdapCountry, spTJHCorresCountry, getDematSecondJointHolderCountry());
                                        getSpinnerData(arrAdapCountry, spTJHPermCountry, getDematSecondJointHolderCountryPerm());
                                        getSpinnerData(arrAdapCountry, spTJHCorresCountry, getDematSecondJointHolderCountry());
                                        getSpinnerData(arrAdapCountry, spTJHPermCountry, getDematSecondJointHolderCountryPerm());
                                    }
                                }
                            } else {
                                if (arrAdapCorresCity == null) {
                                    arrCorresCity = new ArrayList<>();
                                    arrAdapCorresCity = new ArrayAdapter<>(mContext, R.layout.simple_spinner_item, arrCorresCity);
                                    arrAdapCorresCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spSJHCorresCity.setAdapter(arrAdapCorresCity);
                                    spTJHCorresCity.setAdapter(arrAdapCorresCity);
                                }
                                arrAdapCorresCity.add(new RowData(0, "-1", "Select"));
                                arrAdapCorresCity.notifyDataSetChanged();
                                cityFoundFlag = true;
                            }
                            disableEnableField();
                        } catch (Exception e) {
                            e.printStackTrace();
                            commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
                        hideProgress();
                    } else {
                        customProgress.setVisibility(View.GONE);
                        //  commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
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
                    super.onFailure(statusCode, headers, throwable, errorResponse);
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

    private void setSpinnerState() {
        //SJH state
        spSJHCorresState.setAdapter(arrAdapState);
        spSJHCorresState.setOnItemSelectedListener(corresStateListener);
        spSJHPermState.setAdapter(arrAdapState);
        spSJHPermState.setOnItemSelectedListener(permStateListener);
        //TJH state
        spTJHCorresState.setAdapter(arrAdapState);
        spTJHCorresState.setOnItemSelectedListener(tjhCorresStateListener);
        spTJHPermState.setAdapter(arrAdapState);
        spTJHPermState.setOnItemSelectedListener(tjhPermStateListener);
        //Nominee
        spNomineeCorresState.setAdapter(arrAdapState);
        spNomineeCorresState.setOnItemSelectedListener(nomineeStateListener);
        //Nominee Guardian
        spNomineeGuardianCorresState.setAdapter(arrAdapState);
        spNomineeGuardianCorresState.setOnItemSelectedListener(nomineeGuardianStateListener);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        try {
            if (i == R.id.btnSecondHolder) {
                setFirstHolderData();
                rlSecondJointHolder.setVisibility(View.VISIBLE);
                rlThirdJointHolder.setVisibility(View.GONE);
            } else if (i == R.id.btnThirdHolder) {
                setSecondHolderData();
                rlSecondJointHolder.setVisibility(View.GONE);
                rlThirdJointHolder.setVisibility(View.VISIBLE);
            } else if (i == R.id.toggleNominee) {
                if (toggleNominee.isChecked()) {
                    llNomineeDetail.setVisibility(View.VISIBLE);
                    toggleNominee.setChecked(true);
                } else {
                    if (isNomineeMandatory()) {
                        toggleNominee.setChecked(true);
                    } else {
                        final CustomDialog clearNomineeDialog = new CustomDialog(getActivity(), getResources().getString(R.string.nominee_unselect_warning), true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                            @Override
                            public void dialogAction(MOSLCommon.Action action, Object... data) {
                                if (action == MOSLCommon.Action.OK) {
                                    clearNomineeFields();
                                    llNomineeDetail.setVisibility(View.GONE);
                                    llNomineeGuardian.setVisibility(View.GONE);
                                    toggleNominee.setChecked(false);
                                } else {
                                    toggleNominee.setChecked(true);
                                }
                            }
                        });
                        clearNomineeDialog.show();
                    }
                }
            } else if (i == R.id.toggleJointHolder) {
                if (toggleJointHolder.isChecked()) {
                    llNumberOfJointHolder.setVisibility(View.VISIBLE);
                    spNumberOfJointHolder.setSelection(0);
                    toggleJointHolder.setChecked(true);
                } else {
                    final CustomDialog clearJointDialog = new CustomDialog(getActivity(), getResources().getString(R.string.joint_unselect_warning), true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(MOSLCommon.Action action, Object... data) {
                            if (action == MOSLCommon.Action.OK) {
                                clearJointHolderFields();
                                llNumberOfJointHolder.setVisibility(View.GONE);
                                llJointHolderDetail.setVisibility(View.GONE);
                                toggleJointHolder.setChecked(false);
                            } else {
                                toggleJointHolder.setChecked(true);
                            }
                        }
                    });
                    clearJointDialog.show();
                }
            } else if (i == R.id.rbSJHMale) {
                if (rbSJHMale.isChecked()) {
                    etSJHGenderError.setVisibility(View.GONE);
                    rbSJHFemale.setChecked(false);
                }
            } else if (i == R.id.rbSJHFemale) {
                if (rbSJHFemale.isChecked()) {
                    etSJHGenderError.setVisibility(View.GONE);
                    rbSJHMale.setChecked(false);
                }
            } else if (i == R.id.rbTJHMale) {
                if (rbTJHMale.isChecked()) {
                    etTJHGenderError.setVisibility(View.GONE);
                    rbTJHFemale.setChecked(false);
                }
            } else if (i == R.id.rbTJHFemale) {
                if (rbTJHFemale.isChecked()) {
                    etTJHGenderError.setVisibility(View.GONE);
                    rbTJHMale.setChecked(false);
                }
            } else if (i == R.id.rbSJHMarried) {
                if (rbSJHMarried.isChecked()) {
                    etSJHMaritalError.setVisibility(View.GONE);
                    rbSJHUnMarried.setChecked(false);
                }
            } else if (i == R.id.rbSJHUnMarried) {
                if (rbSJHUnMarried.isChecked()) {
                    etSJHMaritalError.setVisibility(View.GONE);
                    rbSJHMarried.setChecked(false);
                }
            } else if (i == R.id.rbTJHMarried) {
                if (rbTJHMarried.isChecked()) {
                    etTJHmaritalError.setVisibility(View.GONE);
                    rbTJHUnMarried.setChecked(false);
                }
            } else if (i == R.id.rbTJHUnMarried) {
                if (rbTJHUnMarried.isChecked()) {
                    etTJHmaritalError.setVisibility(View.GONE);
                    rbTJHMarried.setChecked(false);
                }
            } else if (i == R.id.btnSaveJointNominee) {
                if (getAppType().equalsIgnoreCase(DIRECT) && !getIsAadharVerified() && !getInterestedInMF()) {
                    if (toggleJointHolder.isChecked() && !toggleNominee.isChecked()) {
                        if (toggleJointHolder.isChecked() && spNumberOfJointHolder.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            etNumberOfJointHolderError.setText("Select number of joint holder");
                            etNumberOfJointHolderError.setVisibility(View.VISIBLE);
                            etNumberOfJointHolderError.requestFocus();
                        } else {
                            if (rlSecondJointHolder.getVisibility() == View.VISIBLE) {
                                setFirstHolderData();
                                validateFirstJointHolderData();
                            } else if (rlThirdJointHolder.getVisibility() == View.VISIBLE) {
                                setSecondHolderData();
                                validateSecondJointHolderData();
                            }

                            if (spNumberOfJointHolder.getSelectedItem().toString().equalsIgnoreCase("1")) {
                                if (firstUpdate) {

                                    if (!isBlank(getAadharNo()) && etSJHAadhar.getText().toString().trim().equals(getAadharNo())) {
                                        //applicant aadhar same as second holder aadhar
                                        commonMsgDialog(mContext, Second_Joint_Holder_Aadhaar_Number_cannot_be_same_as_Primary_applicant_Aadhaar_Number_Please_enter_unique_details, false, "");
                                    } else if (etSJHPAN.getText().toString().trim().equalsIgnoreCase(getPANno())) {
                                        //second holder pan same as applicant pan
                                        commonMsgDialog(mContext, Second_Joint_Holder_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHFlatNoCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd2())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNoCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd2().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHFlatNamePerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd2Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd2Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else {
                                        updateNomineeDetails();
                                        updateDematDetails();
                                    }
                                }
                            } else if (spNumberOfJointHolder.getSelectedItem().toString().equalsIgnoreCase("2")) {
                                if (firstUpdate && secondUpdate) {
                                    if (!secondJointHolderName.equals("") && secondJointHolderName.equalsIgnoreCase(thirdJointHolderName)) {
                                        //  second name same as third holder name
                                        commonMsgDialog(mContext, Second_Joint_Holder_name_cannot_be_same_as_Third_Joint_holder_name_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(getAadharNo()) && etSJHAadhar.getText().toString().trim().equals(getAadharNo())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_Aadhaar_Number_cannot_be_same_as_Primary_applicant_Aadhaar_Number_Please_enter_unique_details, false, "");
                                    } else if (etSJHPAN.getText().toString().trim().equalsIgnoreCase(getPANno())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                                    } else if (getPANno().equalsIgnoreCase(etTJHPAN.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Third_Joint_Holder_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(getAadharNo()) && getAadharNo().equals(etTJHAadhar.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Third_Joint_Holder_Aadhaar_number_cannot_be_same_as_primary_applicant_Aadhaar_number_Please_enter_unique_details, false, "");
                                    } else if (etSJHPAN.getText().toString().trim().equalsIgnoreCase(etTJHPAN.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_PAN_cannot_be_same_as_Third_Joint_holder_PAN_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(etSJHAadhar.getText().toString()) && etSJHAadhar.getText().toString().trim().equals(etTJHAadhar.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_Aadhaar_Number_cannot_be_same_as_Third_Joint_holder_Aadhaar_Number_Please_enter_unique_details, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHFlatNoCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd2())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNoCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd2().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHFlatNamePerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd2Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd2Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNameCorres.isEnabled() || etTJHFlatNoCorres.isEnabled()) && getDematSecondJointHolderAdd1().equalsIgnoreCase(getDematSecondJointHolderAdd2())) {
                                        //Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNameCorres.isEnabled() || etTJHAreaCorres.isEnabled()) && getDematSecondJointHolderAdd1().equalsIgnoreCase(getDematSecondJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");//
                                    } else if ((etTJHFlatNoCorres.isEnabled() || etTJHAreaCorres.isEnabled()) && getDematSecondJointHolderAdd2().equalsIgnoreCase(getDematSecondJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNamePerm.isEnabled() || etTJHFlatNamePerm.isEnabled()) && getDematSecondJointHolderAdd1Perm().equalsIgnoreCase(getDematSecondJointHolderAdd2Perm())) {
                                        //Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNamePerm.isEnabled() || etTJHAreaPerm.isEnabled()) && getDematSecondJointHolderAdd1Perm().equalsIgnoreCase(getDematSecondJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHFlatNamePerm.isEnabled() || etTJHAreaPerm.isEnabled()) && getDematSecondJointHolderAdd2Perm().equalsIgnoreCase(getDematSecondJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else {
                                        updateNomineeDetails();
                                        updateDematDetails();
                                    }
                                }
                            }
                        }
                    } else if (!toggleJointHolder.isChecked() && !toggleNominee.isChecked()) {
                        updateNomineeDetails();
                        updateDematDetails();
                    } else {
                        validateNominee();
                    }
                } else {
                    if (toggleNominee.isChecked()) {
                        validateNominee();
                    } else {
                        updateNomineeDetails();
                    }
                }
            } else if (i == R.id.cbSJHSameAsPrimaryCorrespondence)

            {
                if (cbSJHSameAsPrimaryCorrespondence.isChecked()) {
                    inputLayoutSJHBldgNameCorres.setErrorEnabled(false);
                    inputLayoutSJHFlatNoCorres.setErrorEnabled(false);
                    inputLayoutSJHAreaCorres.setErrorEnabled(false);
                    inputLayoutSJHPincodeCorres.setErrorEnabled(false);
                    etSJHCorresStateError.setVisibility(View.GONE);
                    etSJHCorresCityError.setVisibility(View.GONE);
                    etSJHCorresCountryError.setVisibility(View.GONE);
                    getAddressDetails();
                    cbSJHSameAsPrimaryCorrespondence.setChecked(true);
                    etSJHBldgNameCorres.setEnabled(false);
                    etSJHFlatNoCorres.setEnabled(false);
                    etSJHAreaCorres.setEnabled(false);
//                    spSJHAddProofCorres.setEnabled(false);
                    etSJHPincodeCorres.setEnabled(false);
                    spSJHCorresState.setEnabled(false);
                    etSJHCorresState.setEnabled(false);
                    spSJHCorresCity.setEnabled(false);
                    etSJHCorresCity.setEnabled(false);
                    spSJHCorresCountry.setEnabled(false);
                } else {
                    cbSJHSameAsPrimaryCorrespondence.setChecked(false);
                    spSJHCorresState.setVisibility(View.VISIBLE);
                    spSJHCorresCity.setVisibility(View.VISIBLE);
                    etSJHCorresState.setVisibility(View.GONE);
                    etSJHCorresCity.setVisibility(View.GONE);
                    etSJHBldgNameCorres.setEnabled(true);
                    etSJHFlatNoCorres.setEnabled(true);
                    etSJHAreaCorres.setEnabled(true);
                    spSJHAddProofCorres.setEnabled(true);
                    etSJHPincodeCorres.setEnabled(true);
                    spSJHCorresState.setEnabled(true);
                    spSJHCorresCity.setEnabled(true);
                    spSJHCorresCountry.setEnabled(true);
                    etSJHBldgNameCorres.setText("");
                    etSJHFlatNoCorres.setText("");
                    etSJHAreaCorres.setText("");
                    etSJHPincodeCorres.setText("");
                    RowData permanentCityData = getSpinnerRowDataByValue("Select", arrAdapCorresCity);
                    if (permanentCityData != null) {
                        spSJHCorresCity.setSelection(permanentCityData.getPosition());
                    }
                    RowData permanentStateData = getSpinnerRowDataByValue("Select", arrAdapState);
                    if (permanentStateData != null) {
                        spSJHCorresState.setSelection(permanentStateData.getPosition());
                    }
                }
            } else if (i == R.id.cbSJHSameAsCorrespondence)

            {
                if (cbSJHSameAsCorrespondence.isChecked()) {
                    inputLayoutSJHBldgNamePerm.setErrorEnabled(false);
                    inputLayoutSJHFlatNamePerm.setErrorEnabled(false);
                    inputLayoutSJHAreaPerm.setErrorEnabled(false);
                    inputLayoutSJHPincodePerm.setErrorEnabled(false);
                    etSJHPermStateError.setVisibility(View.GONE);
                    etSJHPermCityError.setVisibility(View.GONE);
                    etSJHPermCountryError.setVisibility(View.GONE);
                    etSJHAddProofPermError.setVisibility(View.GONE);
                    cbSJHSameAsCorrespondence.setChecked(true);
                    etSJHBldgNamePerm.setEnabled(false);
                    etSJHFlatNamePerm.setEnabled(false);
                    etSJHAreaPerm.setEnabled(false);
                    etSJHPincodePerm.setEnabled(false);
                    spSJHPermState.setEnabled(false);
                    etSJHPermState.setEnabled(false);
                    spSJHPermCity.setEnabled(false);
                    etSJHPermCity.setEnabled(false);
                    spSJHAddProofPerm.setEnabled(false);
                    spSJHPermCountry.setEnabled(false);
                    etSJHBldgNamePerm.setText(etSJHBldgNameCorres.getText().toString());
                    etSJHFlatNamePerm.setText(etSJHFlatNoCorres.getText().toString());
                    etSJHAreaPerm.setText(etSJHAreaCorres.getText().toString());
                    etSJHPincodePerm.setText(etSJHPincodeCorres.getText().toString());

                    if (cbSJHSameAsPrimaryCorrespondence.isChecked() && (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL))) {
                        spSJHPermState.setVisibility(View.GONE);
                        spSJHPermCity.setVisibility(View.GONE);
                        etSJHPermState.setVisibility(View.VISIBLE);
                        etSJHPermCity.setVisibility(View.VISIBLE);
                        etSJHPermState.setText(etSJHCorresState.getText().toString());
                        etSJHPermCity.setText(etSJHCorresCity.getText().toString());
                    } else {
                        spSJHPermState.setVisibility(View.VISIBLE);
                        spSJHPermCity.setVisibility(View.VISIBLE);
                        etSJHPermState.setVisibility(View.GONE);
                        etSJHPermCity.setVisibility(View.GONE);
                        if (spSJHCorresState.getSelectedItem() != null) {
                            int corrSelectedStatePos = ((RowData) spSJHCorresState.getSelectedItem()).getPosition();
                            spSJHPermState.setSelection(corrSelectedStatePos);
                        }
                        if (spSJHCorresCity.getSelectedItem() != null) {
                            arrAdapPermCity.clear();
                            arrAdapPermCity.addAll(arrCorresCity);
                            arrAdapPermCity.notifyDataSetChanged();
                            int corrSelectedCityPos = ((RowData) spSJHCorresCity.getSelectedItem()).getPosition();
                            spSJHPermCity.setSelection(corrSelectedCityPos);
                        }
                    }
                    if (spSJHAddProofCorres.getSelectedItem() != null) {
                        int permSelectedAddPos = ((RowData) spSJHAddProofCorres.getSelectedItem()).getPosition();
                        spSJHAddProofPerm.setSelection(permSelectedAddPos);
                    }
                    if (spSJHCorresCountry.getSelectedItem() != null) {
                        String corrSelectedCountryPos = spSJHCorresCountry.getSelectedItem().toString();
                        getSpinnerData(arrAdapCountry, spSJHPermCountry, corrSelectedCountryPos);
                    }
                } else {
                    cbSJHSameAsCorrespondence.setChecked(false);
                    etSJHBldgNamePerm.setEnabled(true);
                    etSJHFlatNamePerm.setEnabled(true);
                    etSJHAreaPerm.setEnabled(true);
                    spSJHAddProofPerm.setEnabled(true);
                    etSJHPincodePerm.setEnabled(true);
                    spSJHPermState.setEnabled(true);
                    spSJHPermCity.setEnabled(true);
                    spSJHPermCountry.setEnabled(true);
                    spSJHPermState.setVisibility(View.VISIBLE);
                    spSJHPermCity.setVisibility(View.VISIBLE);
                    etSJHPermState.setVisibility(View.GONE);
                    etSJHPermCity.setVisibility(View.GONE);
                    etSJHBldgNamePerm.setText("");
                    etSJHFlatNamePerm.setText("");
                    etSJHAreaPerm.setText("");
                    etSJHPincodePerm.setText("");
                    spSJHPermCountry.setSelection(0);
                    RowData permanentCityData = getSpinnerRowDataByValue("Select", arrAdapPermCity);
                    if (permanentCityData != null) {
                        spSJHPermCity.setSelection(permanentCityData.getPosition());
                    }
                    RowData permanentStateData = getSpinnerRowDataByValue("Select", arrAdapState);
                    if (permanentStateData != null) {
                        spSJHPermState.setSelection(permanentStateData.getPosition());
                    }
                    RowData addressData = getSpinnerRowDataByValue("Select", arrAdapPermAddressProof);
                    if (addressData != null) {
                        spSJHAddProofPerm.setSelection(addressData.getPosition());
                    }
                }
            } else if (i == R.id.cbTJHSameAsPrimaryCorrespondence)

            {
                if (cbTJHSameAsPrimaryCorrespondence.isChecked()) {
                    inputLayoutTJHBldgNameCorres.setErrorEnabled(false);
                    inputLayoutTJHFlatNoCorres.setErrorEnabled(false);
                    inputLayoutTJHAreaCorres.setErrorEnabled(false);
                    inputLayoutTJHPincodeCorres.setErrorEnabled(false);
                    etTJHCorresStateError.setVisibility(View.GONE);
                    etTJHCorresCityError.setVisibility(View.GONE);
                    etTJHCorresCountryError.setVisibility(View.GONE);
                    getAddressDetails();
                    cbTJHSameAsPrimaryCorrespondence.setChecked(true);
                    etTJHBldgNameCorres.setEnabled(false);
                    etTJHFlatNoCorres.setEnabled(false);
                    etTJHAreaCorres.setEnabled(false);
//                    spTJHAddProofCorres.setEnabled(false);
                    etTJHPincodeCorres.setEnabled(false);
                    spTJHCorresState.setEnabled(false);
                    etTJHCorresState.setEnabled(false);
                    spTJHCorresCity.setEnabled(false);
                    etTJHCorresCity.setEnabled(false);
                    spSJHCorresCountry.setEnabled(false);
                } else {
                    cbTJHSameAsPrimaryCorrespondence.setChecked(false);
                    spTJHCorresState.setVisibility(View.VISIBLE);
                    spTJHCorresCity.setVisibility(View.VISIBLE);
                    etTJHCorresState.setVisibility(View.GONE);
                    etTJHCorresCity.setVisibility(View.GONE);
                    etTJHBldgNameCorres.setEnabled(true);
                    etTJHFlatNoCorres.setEnabled(true);
                    etTJHAreaCorres.setEnabled(true);
                    spTJHAddProofCorres.setEnabled(true);
                    etTJHPincodeCorres.setEnabled(true);
                    spTJHCorresState.setEnabled(true);
                    spTJHCorresCity.setEnabled(true);
                    spSJHCorresCountry.setEnabled(true);
                    etTJHBldgNameCorres.setText("");
                    etTJHFlatNoCorres.setText("");
                    etTJHAreaCorres.setText("");
                    etTJHPincodeCorres.setText("");
                    RowData permanentCityData = getSpinnerRowDataByValue("Select", arrAdapCorresCity);
                    if (permanentCityData != null) {
                        spTJHCorresCity.setSelection(permanentCityData.getPosition());
                    }
                    RowData permanentStateData = getSpinnerRowDataByValue("Select", arrAdapState);
                    if (permanentStateData != null) {
                        spTJHCorresState.setSelection(permanentStateData.getPosition());
                    }
                }
            } else if (i == R.id.cbTJHSameAsCorrespondence)

            {
                if (cbTJHSameAsCorrespondence.isChecked()) {
                    cbTJHSameAsCorrespondence.setChecked(true);
                    inputLayoutTJHBldgNamePerm.setErrorEnabled(false);
                    inputLayoutTJHFlatNamePerm.setErrorEnabled(false);
                    inputLayoutTJHAreaPerm.setErrorEnabled(false);
                    inputLayoutTJHPincodePerm.setErrorEnabled(false);
                    etTJHPermStateError.setVisibility(View.GONE);
                    etTJHPermCityError.setVisibility(View.GONE);
                    etTJHPermCountryError.setVisibility(View.GONE);
                    etTJHAddProofPermError.setVisibility(View.GONE);
                    etTJHBldgNamePerm.setEnabled(false);
                    etTJHFlatNamePerm.setEnabled(false);
                    etTJHAreaPerm.setEnabled(false);
                    etTJHPincodePerm.setEnabled(false);
                    spTJHAddProofPerm.setEnabled(false);
                    spTJHPermState.setEnabled(false);
                    etTJHPermState.setEnabled(false);
                    spTJHPermCity.setEnabled(false);
                    etTJHPermCity.setEnabled(false);
                    spTJHPermCountry.setEnabled(false);
                    etTJHBldgNamePerm.setText(etTJHBldgNameCorres.getText().toString());
                    etTJHFlatNamePerm.setText(etTJHFlatNoCorres.getText().toString());
                    etTJHAreaPerm.setText(etTJHAreaCorres.getText().toString());
                    etTJHPincodePerm.setText(etTJHPincodeCorres.getText().toString());

                    if (cbTJHSameAsPrimaryCorrespondence.isChecked() && (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL))) {
                        spTJHPermState.setVisibility(View.GONE);
                        spTJHPermCity.setVisibility(View.GONE);
                        etTJHPermState.setVisibility(View.VISIBLE);
                        etTJHPermCity.setVisibility(View.VISIBLE);
                        etTJHPermState.setText(etTJHCorresState.getText().toString());
                        etTJHPermCity.setText(etTJHCorresCity.getText().toString());
                    } else {
                        spTJHPermState.setVisibility(View.VISIBLE);
                        spTJHPermCity.setVisibility(View.VISIBLE);
                        etTJHPermState.setVisibility(View.GONE);
                        etTJHPermCity.setVisibility(View.GONE);
                        if (spTJHCorresState.getSelectedItem() != null) {
                            int corrSelectedStatePos = ((RowData) spTJHCorresState.getSelectedItem()).getPosition();
                            spTJHPermState.setSelection(corrSelectedStatePos);
                        }
                        if (spTJHCorresCity.getSelectedItem() != null) {
                            arrAdapPermCity.clear();
                            arrAdapPermCity.addAll(arrCorresCity);
                            arrAdapPermCity.notifyDataSetChanged();
                            int corrSelectedCityPos = ((RowData) spTJHCorresCity.getSelectedItem()).getPosition();
                            spTJHPermCity.setSelection(corrSelectedCityPos);
                        }
                    }
                    if (spTJHAddProofPerm.getSelectedItem() != null) {
                        int permSelectedAddPos = ((RowData) spTJHAddProofCorres.getSelectedItem()).getPosition();
                        spTJHAddProofPerm.setSelection(permSelectedAddPos);
                    }
                    if (spTJHCorresCountry.getSelectedItem() != null) {
                        String corrSelectedCountryPos = spTJHCorresCountry.getSelectedItem().toString();
                        getSpinnerData(arrAdapCountry, spTJHPermCountry, corrSelectedCountryPos);
                    }
                } else {
                    cbTJHSameAsCorrespondence.setChecked(false);
                    etTJHBldgNamePerm.setEnabled(true);
                    etTJHFlatNamePerm.setEnabled(true);
                    etTJHAreaPerm.setEnabled(true);
                    spTJHAddProofPerm.setEnabled(true);
                    etTJHPincodePerm.setEnabled(true);
                    spTJHPermState.setEnabled(true);
                    spTJHPermCity.setEnabled(true);
                    spTJHPermCountry.setEnabled(true);
                    spTJHPermState.setVisibility(View.VISIBLE);
                    spTJHPermCity.setVisibility(View.VISIBLE);
                    etTJHPermState.setVisibility(View.GONE);
                    etTJHPermCity.setVisibility(View.GONE);
                    etTJHBldgNamePerm.setText("");
                    etTJHFlatNamePerm.setText("");
                    etTJHAreaPerm.setText("");
                    etTJHPincodePerm.setText("");
                    spTJHPermCountry.setSelection(0);
                    RowData permanentCityData = getSpinnerRowDataByValue("Select", arrAdapPermCity);
                    if (permanentCityData != null) {
                        spTJHPermCity.setSelection(permanentCityData.getPosition());
                    }
                    RowData permanentStateData = getSpinnerRowDataByValue("Select", arrAdapState);
                    if (permanentStateData != null) {
                        spTJHPermState.setSelection(permanentStateData.getPosition());
                    }
                    RowData addressData = getSpinnerRowDataByValue("Select", arrAdapPermAddressProof);
                    if (addressData != null) {
                        spTJHAddProofPerm.setSelection(addressData.getPosition());
                    }
                }
            } else if (i == R.id.cbNomineeSameAsPrimaryCorrespondence) {
                if (cbNomineeSameAsPrimaryCorrespondence.isChecked()) {
                    nomineeAddress = true;
                    inputLayoutNomineeBldgNameCorres.setErrorEnabled(false);
                    inputLayoutNomineeFlatNoCorres.setErrorEnabled(false);
                    inputLayoutNomineeAreaCorres.setErrorEnabled(false);
                    inputLayoutNomineePincodeCorres.setErrorEnabled(false);
                    etNomineeCorresStateError.setVisibility(View.GONE);
                    etNomineeCorresCityError.setVisibility(View.GONE);
                    etNomineeCorresCountryError.setVisibility(View.GONE);
                    getNomineeeAddressDetails();
                    etNomineeBldgNameCorres.setEnabled(false);
                    etNomineeFlatNoCorres.setEnabled(false);
                    etNomineeAreaCorres.setEnabled(false);
                    etNomineePincodeCorres.setEnabled(false);
                    //If address mode is manual spinner will be visible otherwise edittext
                    if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                        spNomineeCorresState.setVisibility(View.GONE);
                        spNomineeCorresCity.setVisibility(View.GONE);
                        etNomineeCorresState.setVisibility(View.VISIBLE);
                        etNomineeCorresCity.setVisibility(View.VISIBLE);
                        etNomineeCorresState.setEnabled(false);
                        etNomineeCorresCity.setEnabled(false);
                    } else {
                        spNomineeCorresState.setVisibility(View.VISIBLE);
                        spNomineeCorresCity.setVisibility(View.VISIBLE);
                        etNomineeCorresState.setVisibility(View.GONE);
                        etNomineeCorresCity.setVisibility(View.GONE);
                        spNomineeCorresState.setEnabled(false);
                        spNomineeCorresCity.setEnabled(false);
                    }
                    spNomineeCorresCountry.setEnabled(false);
                } else {
                    cbNomineeSameAsPrimaryCorrespondence.setChecked(false);
                    etNomineeBldgNameCorres.setEnabled(true);
                    etNomineeFlatNoCorres.setEnabled(true);
                    etNomineeAreaCorres.setEnabled(true);
                    etNomineePincodeCorres.setEnabled(true);
                    spNomineeCorresState.setEnabled(true);
                    spNomineeCorresCity.setEnabled(true);
                    spNomineeCorresCountry.setEnabled(true);
                    etNomineeBldgNameCorres.setText("");
                    etNomineeFlatNoCorres.setText("");
                    etNomineeAreaCorres.setText("");
                    etNomineePincodeCorres.setText("");
                    spNomineeCorresCountry.setSelection(0);
                    spNomineeCorresState.setVisibility(View.VISIBLE);
                    spNomineeCorresCity.setVisibility(View.VISIBLE);
                    etNomineeCorresState.setVisibility(View.GONE);
                    etNomineeCorresCity.setVisibility(View.GONE);
                    etNomineeCorresState.setText("");
                    etNomineeCorresCity.setText("");
                    RowData permanentCityData = getSpinnerRowDataByValue("Select", arrAdapNomineeCity);
                    if (permanentCityData != null) {
                        spNomineeCorresCity.setSelection(permanentCityData.getPosition());
                    }
                    RowData permanentStateData = getSpinnerRowDataByValue("Select", arrAdapState);
                    if (permanentStateData != null) {
                        spNomineeCorresState.setSelection(permanentStateData.getPosition());
                    }
                }
            } else if (i == R.id.cbNomineeGuardianSameAsPrimaryCorrespondence) {
                if (cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) {
                    nomineeAddress = false;
                    inputLayoutNomineeGuardianBldgNameCorres.setErrorEnabled(false);
                    inputLayoutNomineeGuardianFlatNoCorres.setErrorEnabled(false);
                    inputLayoutNomineeGuardianAreaCorres.setErrorEnabled(false);
                    inputLayoutNomineeGuardianPincodeCorres.setErrorEnabled(false);
                    etNomineeGuardianCorresStateError.setVisibility(View.GONE);
                    etNomineeGuardianCorresCityError.setVisibility(View.GONE);
                    etNomineeGuardianCorresCountryError.setVisibility(View.GONE);
                    getNomineeeAddressDetails();
                    etNomineeGuardianBldgNameCorres.setEnabled(false);
                    etNomineeGuardianFlatNoCorres.setEnabled(false);
                    etNomineeGuardianAreaCorres.setEnabled(false);
                    etNomineeGuardianPincodeCorres.setEnabled(false);
                    //If address mode is manual spinner will be visible otherwise edittext
                    if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                        spNomineeGuardianCorresState.setVisibility(View.GONE);
                        spNomineeGuardianCorresCity.setVisibility(View.GONE);
                        etNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                        etNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                        etNomineeGuardianCorresState.setEnabled(false);
                        etNomineeGuardianCorresCity.setEnabled(false);
                    } else {
                        spNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                        spNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                        etNomineeGuardianCorresState.setVisibility(View.GONE);
                        etNomineeGuardianCorresCity.setVisibility(View.GONE);
                        spNomineeGuardianCorresState.setEnabled(false);
                        spNomineeGuardianCorresCity.setEnabled(false);
                    }
                    spNomineeGuardianCorresCountry.setEnabled(false);
                } else {
                    cbNomineeGuardianSameAsPrimaryCorrespondence.setChecked(false);
                    etNomineeGuardianBldgNameCorres.setEnabled(true);
                    etNomineeGuardianFlatNoCorres.setEnabled(true);
                    etNomineeGuardianAreaCorres.setEnabled(true);
                    etNomineeGuardianPincodeCorres.setEnabled(true);
                    spNomineeGuardianCorresState.setEnabled(true);
                    spNomineeGuardianCorresCity.setEnabled(true);
                    etNomineeGuardianBldgNameCorres.setText("");
                    etNomineeGuardianFlatNoCorres.setText("");
                    etNomineeGuardianAreaCorres.setText("");
                    etNomineeGuardianPincodeCorres.setText("");
                    spNomineeGuardianCorresCountry.setEnabled(true);
                    spNomineeGuardianCorresCountry.setSelection(0);
                    spNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                    spNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                    etNomineeGuardianCorresState.setVisibility(View.GONE);
                    etNomineeGuardianCorresCity.setVisibility(View.GONE);
                    etNomineeGuardianCorresState.setText("");
                    etNomineeGuardianCorresCity.setText("");
                    RowData permanentCityData = getSpinnerRowDataByValue("Select", arrAdapNomineeCity);
                    if (permanentCityData != null) {
                        spNomineeGuardianCorresCity.setSelection(permanentCityData.getPosition());
                    }
                    RowData permanentStateData = getSpinnerRowDataByValue("Select", arrAdapState);
                    if (permanentStateData != null) {
                        spNomineeGuardianCorresState.setSelection(permanentStateData.getPosition());
                    }
                }
            }
        } catch (Exception ex) {
            customProgress.setVisibility(View.GONE);
            commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
            ex.printStackTrace();
        }
    }

    public void updateDematDetails() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
            inputMap.put("_userid", getUserID());
//            inputMap.put(_userid, "1625707");
            if (!toggleJointHolder.isChecked()) {
                inputMap.put(DematAccType, "Individual");
            } else {
                inputMap.put(_JointHolder, spNumberOfJointHolder.getSelectedItem().toString());
                inputMap.put(NumberOfJointHolder, spNumberOfJointHolder.getSelectedItem().toString());
                inputMap.put(DematAccType, "Joint");//Individual /Joint
                inputMap.put(ResidentialStatusFJ, getDematFirstJointHolderResidentialStatus());
                inputMap.put(DematAccTitle, getDematAccTitle());
                inputMap.put(DematFirstJointAccHolderFName, getDematFirstJointHolderFname());
                inputMap.put(DematFirstJointAccHolderMName, getDematFirstJointHolderMname());
                inputMap.put(DematFirstJointAccHolderLName, getDematFirstJointHolderLname());
                inputMap.put(DematFirstJointAccHolderSex, getDematFirstJointHolderSex());
                inputMap.put(MaritalStatusFJ, getDematFirstJointHolderMaritalStatus());
                inputMap.put(DematFirstJointAccHolderRelation, getDematFirstJointHolderRelation());
                inputMap.put(DematFirstJointAccHolderFHFName, getDematFirstJointHolderFHFname());
                inputMap.put(DematFirstJointAccHolderFHMName, getDematFirstJointHolderFHMname());
                inputMap.put(DematFirstJointAccHolderFHLName, getDematFirstJointHolderFHLname());
                inputMap.put(DematFirstJointAccHolderAddr1, getDematFirstJointHolderAdd1());
                inputMap.put(DematFirstJointAccHolderAddr2, getDematFirstJointHolderAdd2());
                inputMap.put(DematFirstJointAccHolderAddr3, getDematFirstJointHolderAdd3());
                inputMap.put(DematFirstJointAccHolderLandmark, "");
                inputMap.put(DematFirstJointAccHolderPin, getDematFirstJointHolderPin());
                inputMap.put(DematFirstJointAccHolderCity, getDematFirstJointHolderCity());
                inputMap.put(DematFirstJointAccHolderState, getDematFirstJointHolderState());
                if (spSJHCorresCity.getVisibility() == View.VISIBLE && spSJHCorresCity.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                    inputMap.put(OtherCityFirstHolder, etSJHCorresCityOther.getText().toString());
                } else {
                    inputMap.put(OtherCityFirstHolder, "");
                }
                inputMap.put(DematFirstJointAccHolderCountry, getDematFirstJointHolderCountry());
                inputMap.put(AddressProofFJ, getDematFirstJointHolderAddressProof());
                inputMap.put(DematFirstJointAccHolderPan, getDematFirstJointHolderPan());
                inputMap.put(_Pan, "");//blank
//                inputMap.put(UIDFJ, removeExponential(getDematFirstJointHolderUID()));//first
                inputMap.put(UIDFJ, getDematFirstJointHolderUID());//first
//            Date dob = EkycAppUtil.getDate(etSJHDOB.getText().toString(), "dd/MM/yyyy");
                inputMap.put(DematFirstJointAccHolderDateOfBirth, getDematFirstJointHolderDOB());
                inputMap.put(DematFirstJointAccHolderOccupation, getDematFirstJointHolderOccupation());
                inputMap.put(DematFirstJointAccHolderNatureOfBus, "");
                inputMap.put(SameAsCorrFirstHolder, getDematFirstJointHolderSameAsPerm());//Primary
                inputMap.put(SameAsCorrHolderAddrFJ, getDematFirstJointHolderSameAsPerm());//Primary

                inputMap.put(Add1FJ, getDematFirstJointHolderAdd1Perm());
                inputMap.put(Add2FJ, getDematFirstJointHolderAdd2Perm());
                inputMap.put(Add3FJ, getDematFirstJointHolderAdd3Perm());
                inputMap.put(LandmarkFJ, "");
                inputMap.put(PinFJ, getDematFirstJointHolderPinPerm());
                inputMap.put(CityFJ, getDematFirstJointHolderCityPerm());
                if (spSJHPermCity.getVisibility() == View.VISIBLE && spSJHPermCity.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                    inputMap.put(OtherCityFJ, etSJHPermCityOther.getText().toString());
                } else {
                    inputMap.put(OtherCityFJ, "");
                }
                inputMap.put(StateFJ, getDematFirstJointHolderStatePerm());
                inputMap.put(CountryFJ, getDematFirstJointHolderCountryPerm());
                inputMap.put(AnnualIncomeFJ, getDematFirstJointHolderAnnualIncome());
                inputMap.put(AddressProofPermFJ, getDematFirstJointHolderAddressProofPerm());
                inputMap.put(EmailFJ, getDematFirstJointHolderEmail());
                inputMap.put(MobileNoFJ, getDematFirstJointHolderMobile());
                inputMap.put(PermTeleNoFJ, getDematFirstJointHolderTeleNo());
                inputMap.put(PepFJ, getDematFirstJointHolderPEP());
                inputMap.put(RPepFJ, getDematFirstJointHolderRPER());

                if (spNumberOfJointHolder.getSelectedItem().toString().equals("2")) {//Third Joint Holder
                    inputMap.put(NumberOfSecJointHolder, spNumberOfJointHolder.getSelectedItem().toString());
                    inputMap.put(DematSecondAccType, "Joint");//Individual /Joint
                    inputMap.put(ResidentialStatusSJ, getDematSecondJointHolderResidentialStatus());
                    inputMap.put(DematSecondJointHolderRelation, getDematSecondJointHolderRelation());
                    inputMap.put(DematSecondAccTitle, getDematSecondAccTitle());//Same
                    inputMap.put(DematSecAccTitle, getDematSecondAccTitle());//Same
                    inputMap.put(DematSecondJointAccHolderFName, getDematSecondJointHolderFname());
                    inputMap.put(DematSecondJointAccHolderMName, getDematSecondJointHolderMname());
                    inputMap.put(DematSecondJointAccHolderLName, getDematSecondJointHolderLname());
                    inputMap.put(DematSecondJointAccHolderSex, getDematSecondJointHolderSex());
                    inputMap.put(MaritalStatusSJ, getDematSecondJointHolderMaritalStatus());
                    inputMap.put(DematSecondJointAccHolderFHFName, getDematSecondJointHolderFHFname());
                    inputMap.put(DematSecondJointAccHolderFHMName, getDematSecondJointHolderFHMname());
                    inputMap.put(DematSecondJointAccHolderFHLName, getDematSecondJointHolderFHLname());
                    inputMap.put(DematSecondJointAccHolderAdd1, getDematSecondJointHolderAdd1());
                    inputMap.put(DematSecondJointAccHolderAdd2, getDematSecondJointHolderAdd2());
                    inputMap.put(DematSecondJointAccHolderAdd3, getDematSecondJointHolderAdd3());
                    inputMap.put(DematSecondJointAccHolderLandmark, "");
                    inputMap.put(DematSecondJointAccHolderPin, getDematSecondJointHolderPin());
                    inputMap.put(DematSecondJointAccHolderCity, getDematSecondJointHolderCity());
                    inputMap.put(DematSecondJointAccHolderState, getDematSecondJointHolderState());
                    inputMap.put(DematSecondJointAccHolderCountry, getDematSecondJointHolderCountry());
                    inputMap.put(AddressProofSJ, getDematSecondJointHolderAddressProof());
                    inputMap.put(DematSecondJointAccHolderPan, getDematSecondJointHolderPan());
//                    inputMap.put(UIDSJ, removeExponential(getDematSecondJointHolderUID()));//second
//                    inputMap.put(SHUID, removeExponential(getDematSecondJointHolderUID()));//second
                    inputMap.put(UIDSJ, getDematSecondJointHolderUID());//second
                    inputMap.put(SHUID, getDematSecondJointHolderUID());//second
                    inputMap.put(DematSecondJointAccHolderDateOfBirth, getDematSecondJointHolderDOB());
                    inputMap.put(DematSecondJointAccHolderOccupation, getDematSecondJointHolderOccupation());
                    inputMap.put(DematSecondJointAccHolderNatureOfBus, "");
                    inputMap.put(SameAsCorrSecondHolder, getDematSecondJointHolderSameAsPerm());//primary
                    inputMap.put(SameAsCorrHolderSJ, getDematSecondJointHolderSameAsPerm());//primary
                    if (spTJHCorresCity.getVisibility() == View.VISIBLE && spTJHCorresCity.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                        inputMap.put(OtherCitySecondHolder, etTJHCorresCityOther.getText().toString());
                    } else {
                        inputMap.put(OtherCitySecondHolder, "");
                    }
                    if (spTJHPermCity.getVisibility() == View.VISIBLE && spTJHPermCity.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                        inputMap.put(OtherCitySJ, etTJHPermCityOther.getText().toString());
                    } else {
                        inputMap.put(OtherCitySJ, "");
                    }
                    inputMap.put(Add1SJ, getDematSecondJointHolderAdd1Perm());
                    inputMap.put(Add2SJ, getDematSecondJointHolderAdd2Perm());
                    inputMap.put(Add3SJ, getDematSecondJointHolderAdd3Perm());
                    inputMap.put(LandMarkSJ, "");
                    inputMap.put(PinSJ, getDematSecondJointHolderPinPerm());
                    inputMap.put(CitySJ, getDematSecondJointHolderCityPerm());
                    inputMap.put(StateSJ, getDematSecondJointHolderStatePerm());
                    inputMap.put(CountrySJ, getDematSecondJointHolderCountryPerm());
                    inputMap.put(AnnualIncomeSJ, getDematSecondJointHolderAnnualIncome());
                    inputMap.put(AddressProofPermSJ, getDematSecondJointHolderAddressProofPerm());
                    inputMap.put(EmailSJ, getDematSecondJointHolderEmail());
                    inputMap.put(MobileNoSJ, getDematSecondJointHolderMobile());
                    inputMap.put(PermteleNoSJ, getDematSecondJointHolderTeleNo());
                    inputMap.put(PepSJ, getDematSecondJointHolderPEP());
                    inputMap.put(RPepSJ, getDematSecondJointHolderRPER());

                } else {
                    inputMap.put(NumberOfSecJointHolder, "");
                    inputMap.put(DematSecondAccType, "");//Individual /Joint
                    inputMap.put(ResidentialStatusSJ, "");
                    inputMap.put(DematSecondJointHolderRelation, "");
                    inputMap.put(DematSecondAccTitle, "");//Same
                    inputMap.put(DematSecAccTitle, "");//Same
                    inputMap.put(DematSecondJointAccHolderFName, "");
                    inputMap.put(DematSecondJointAccHolderMName, "");
                    inputMap.put(DematSecondJointAccHolderLName, "");
                    inputMap.put(DematSecondJointAccHolderSex, "");
                    inputMap.put(MaritalStatusSJ, "");
                    inputMap.put(DematSecondJointAccHolderFHFName, "");
                    inputMap.put(DematSecondJointAccHolderFHMName, "");
                    inputMap.put(DematSecondJointAccHolderFHLName, "");
                    inputMap.put(DematSecondJointAccHolderAdd1, "");
                    inputMap.put(DematSecondJointAccHolderAdd2, "");
                    inputMap.put(DematSecondJointAccHolderAdd3, "");
                    inputMap.put(DematSecondJointAccHolderLandmark, "");
                    inputMap.put(DematSecondJointAccHolderPin, "");
                    inputMap.put(DematSecondJointAccHolderCity, "");
                    inputMap.put(DematSecondJointAccHolderState, "");
                    inputMap.put(DematSecondJointAccHolderCountry, "");
                    inputMap.put(AddressProofSJ, "");
                    inputMap.put(DematSecondJointAccHolderPan, "");
                    inputMap.put(UIDSJ, "");//second
                    inputMap.put(SHUID, "");//second
                    inputMap.put(DematSecondJointAccHolderDateOfBirth, "");
                    inputMap.put(DematSecondJointAccHolderOccupation, "");
                    inputMap.put(DematSecondJointAccHolderNatureOfBus, "");
                    inputMap.put(SameAsCorrSecondHolder, "");//primary
                    inputMap.put(SameAsCorrHolderSJ, "");//primary
                    inputMap.put(OtherCitySecondHolder, "");
                    inputMap.put(OtherCitySJ, "");
                    inputMap.put(Add1SJ, "");
                    inputMap.put(Add2SJ, "");
                    inputMap.put(Add3SJ, "");
                    inputMap.put(LandMarkSJ, "");
                    inputMap.put(PinSJ, "");
                    inputMap.put(CitySJ, "");
                    inputMap.put(StateSJ, "");
                    inputMap.put(CountrySJ, "");
                    inputMap.put(AnnualIncomeSJ, "");
                    inputMap.put(AddressProofPermSJ, "");
                    inputMap.put(EmailSJ, "");
                    inputMap.put(MobileNoSJ, "");
                    inputMap.put(PermteleNoSJ, "");
                    inputMap.put(PepSJ, "");
                    inputMap.put(RPepSJ, "");
                }
            }

            String flag = INSERTUPDATEDEMATDETAILS;
            String category = GETDEMATDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            if (response.has("Table1")) {
                                JSONArray jsonArray = response.getJSONArray("Table1");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String status = (object.isNull("Status") || object.getString("Status").equals("")) ? "" : object.getString("Status");
                                        String message = (object.isNull("Message") || object.getString("Message").equals("")) ? "" : object.getString("Message");
                                        if (status.equalsIgnoreCase("1")) {
                                            setGotoReviewEdit(false);
                                            setGotoFundTransfer(false);
                                            jointHolderUpdate = true;

                                            if (jointHolderUpdate && nomineeUpdate) {
                                                tabPageIndicator.enableTab(getBankTab());
                                                viewPager.setCurrentItem(getBankTab());
                                            }
                                        } else {
                                            commonMsgDialog(getContext(), message, false, "");
//                                            alertDialog(mContext, 0, "", message, "OK", true, null);

                                        }
                                    }
                                }
                            } else {
                                //Remove ToastMsg
                                commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                                customProgress.setVisibility(View.GONE);
//                                alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            customProgress.setVisibility(View.GONE);
//                            hideUpdateProgress();
                            commonMsgDialog(getContext(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
                        hideUpdateProgress();
                    } else {
                        customProgress.setVisibility(View.GONE);
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
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    customProgress.setVisibility(View.GONE);
//                    hideUpdateProgress();
                    if (statusCode == 0) {
                        CustomDialog customDialog = new CustomDialog(mContext, "Connection time out, Please try again later.", true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                            @Override
                            public void dialogAction(MOSLCommon.Action action, Object... data) {

                            }
                        });
                        customDialog.show();
                    } else {
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
//                    hideUpdateProgress();
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(getContext());
        }
    }

    private void clearNomineeFields() {
        nomineeUpdate = false;
        toggleNominee.setChecked(false);
        llNomineeDetail.setVisibility(View.GONE);
        etNomineeFirstName.setText("");
        etNomineeMiddleName.setText("");
        etNomineeLastName.setText("");
        etNomineePAN.setText("");
        etNomineeAadhar.setText("");
        cbNomineeSameAsPrimaryCorrespondence.setChecked(false);
        etNomineeBldgNameCorres.setText("");
        etNomineeFlatNoCorres.setText("");
        etNomineeAreaCorres.setText("");
        etNomineePincodeCorres.setText("");
        etNomineeSTDNo.setText("");
        etNomineeTelNo.setText("");
        etNomineeDOB.setText("");
        etNomineesRelationship.setText("");
//        etNomineePrimaryApplicantsRelationship.setText("");
        etNomineeEmail.setText("");
        spNomineeCorresState.setSelection(0);
        spNomineeCorresCity.setSelection(0);
        spNomineeCorresCountry.setSelection(0);
        spNomineeGuardianCorresState.setSelection(0);
        spNomineeGuardianCorresCity.setSelection(0);
        spNomineeGuardianCorresCountry.setSelection(0);
        etNomineeGuardianFirstName.setText("");
        etNomineeGuardianMiddleName.setText("");
        etNomineeGuardianLastName.setText("");
        etNomineeGuardianBldgNameCorres.setText("");
        etNomineeGuardianFlatNoCorres.setText("");
        etNomineeGuardianAreaCorres.setText("");
        etNomineeGuardianPincodeCorres.setText("");
        etNomineeGuardianEmail.setText("");
        etNomineeGuardianSTDNo.setText("");
        etNomineeGuardianTelNo.setText("");
        etNomineesGuardianRelationship.setText("");
        cbNomineeGuardianSameAsPrimaryCorrespondence.setChecked(false);
        spNomineeGuardianAge.setSelection(0);
        spNomineesRelationship.setSelection(0);
        spNomineesGuardianRelationship.setSelection(0);

        etNomineeFirstNameError.setVisibility(View.GONE);
        etNomineeMiddleNameError.setVisibility(View.GONE);
        etNomineeLastNameError.setVisibility(View.GONE);
        etNomineePANError.setVisibility(View.GONE);
        tvNomineeAadharError.setVisibility(View.GONE);
        etNomineePincodeCorresError.setVisibility(View.GONE);
        etNomineeCorresStateError.setVisibility(View.GONE);
        etNomineeCorresCityError.setVisibility(View.GONE);
        etNomineeSTDTelError.setVisibility(View.GONE);
        etNomineeEmailError.setVisibility(View.GONE);
        etNomineeGuardianFirstNameError.setVisibility(View.GONE);
        etNomineeGuardianMiddleNameError.setVisibility(View.GONE);
        etNomineeGuardianLastNameError.setVisibility(View.GONE);
        etNomineeGuardianPincodeCorresError.setVisibility(View.GONE);
        etNomineeGuardianCorresStateError.setVisibility(View.GONE);
        etNomineeGuardianCorresCityError.setVisibility(View.GONE);
        etNomineeGuardianAgeError.setVisibility(View.GONE);
        etNomineeGuardianSTDTelError.setVisibility(View.GONE);
        etNomineeGuardianEmailError.setVisibility(View.GONE);
        etNomineesRelationshipError.setVisibility(View.GONE);
        etNomineesGuardianRelationshipError.setVisibility(View.GONE);

        spNomineeCorresState.setVisibility(View.VISIBLE);
        spNomineeCorresCity.setVisibility(View.VISIBLE);
        etNomineeCorresState.setVisibility(View.GONE);
        etNomineeCorresCity.setVisibility(View.GONE);
        spNomineeGuardianCorresState.setVisibility(View.VISIBLE);
        spNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
        etNomineeGuardianCorresState.setVisibility(View.GONE);
        etNomineeGuardianCorresCity.setVisibility(View.GONE);

        etNomineeBldgNameCorres.setEnabled(true);
        etNomineeFlatNoCorres.setEnabled(true);
        etNomineeAreaCorres.setEnabled(true);
        etNomineePincodeCorres.setEnabled(true);
        spNomineeCorresState.setEnabled(true);
        spNomineeCorresCity.setEnabled(true);
        spNomineeCorresCountry.setEnabled(true);
        etNomineeGuardianBldgNameCorres.setEnabled(true);
        etNomineeGuardianFlatNoCorres.setEnabled(true);
        etNomineeGuardianAreaCorres.setEnabled(true);
        etNomineeGuardianPincodeCorres.setEnabled(true);
        spNomineeGuardianCorresState.setEnabled(true);
        spNomineeGuardianCorresCity.setEnabled(true);
        spNomineeGuardianCorresCountry.setEnabled(true);
    }

    public void updateNomineeDetails() {
        if (MOSLCommon.isNetworkEnabled(mContext)) {
            Map inputMap = new HashMap<>();
            inputMap.put("userId", getUserID());
//            inputMap.put(userId, "1625707");
            if (!toggleNominee.isChecked()) {
                inputMap.put(NomineeDeclaration, "No");
            } else /*if (cbNomineeYes.isChecked())*/ {
                inputMap.put(NomineeDeclaration, "Yes");
                inputMap.put(NomineePan, etNomineePAN.getText().toString().trim());
//                inputMap.put(NomineeUID, removeExponential(etNomineeAadhar.getText().toString().trim()));
                inputMap.put(NomineeUID, etNomineeAadhar.getText().toString().trim());
                inputMap.put(NomineeFName, etNomineeFirstName.getText().toString().trim());
                inputMap.put(NomineeMName, etNomineeMiddleName.getText().toString().trim());
                inputMap.put(NomineeLName, etNomineeLastName.getText().toString().trim());
                inputMap.put(NomineeAddr1, etNomineeBldgNameCorres.getText().toString().trim());
                inputMap.put(NomineeAddr2, etNomineeFlatNoCorres.getText().toString().trim());
                inputMap.put(NomineeAddr3, etNomineeAreaCorres.getText().toString().trim());
                inputMap.put(NomineeLandMark, "");
                inputMap.put(NomineePin, etNomineePincodeCorres.getText().toString().trim());
                inputMap.put(NomineeRelBO, spNomineesRelationship.getSelectedItem().toString());
                inputMap.put(ApplicantRelNominee, "");
//                inputMap.put(NomineeRelBO, etNomineesRelationship.getText().toString().trim());
//                inputMap.put(ApplicantRelNominee, etNomineePrimaryApplicantsRelationship.getText().toString().trim());
                if (etNomineesRelationship.getVisibility() == View.VISIBLE) {
                    inputMap.put(NOMINEE_REL_BO_OTHER, etNomineesRelationship.getText().toString().trim());
                } else {
                    inputMap.put(NOMINEE_REL_BO_OTHER, "");
                }
                inputMap.put(NOMINEE_APPLICANT_RELATION_OTHER, "");
                /*if (etNomineePrimaryApplicantsRelationship.getVisibility() == View.VISIBLE) {
                    inputMap.put(NOMINEE_APPLICANT_RELATION_OTHER, etNomineePrimaryApplicantsRelationship.getText().toString().trim());
                } else {
                    inputMap.put(NOMINEE_APPLICANT_RELATION_OTHER, "");
                }*/
                inputMap.put(NomineeMailId, etNomineeEmail.getText().toString().trim());
                if (etNomineeCorresCity.getVisibility() == View.VISIBLE) {
                    inputMap.put(NomineeCity, etNomineeCorresCity.getText().toString().trim());
                    inputMap.put(NomineeState, etNomineeCorresState.getText().toString().trim());
                    inputMap.put(OtherCityNominee, "");
                } else if (spNomineeCorresCity.getVisibility() == View.VISIBLE) {
                    inputMap.put(NomineeCity, spNomineeCorresCity.getSelectedItem().toString());
                    inputMap.put(NomineeState, spNomineeCorresState.getSelectedItem().toString());
                    if (spNomineeCorresCity.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                        inputMap.put(OtherCityNominee, etNomineeCorresCityOther.getText().toString().trim());
                    } else {
                        inputMap.put(OtherCityNominee, "");
                    }
                }
                inputMap.put(NomineeCountry, spNomineeCorresCountry.getSelectedItem().toString());
                if (!etNomineeSTDNo.getText().toString().isEmpty()) {
                    inputMap.put(NomineeTele, etNomineeSTDNo.getText().toString().trim() + "~" + etNomineeTelNo.getText().toString().trim());
                } else {
                    inputMap.put(NomineeTele, "");
                }
                Date dob = EkycAppUtil.getDate(etNomineeDOB.getText().toString().trim(), "dd/MM/yyyy");
                inputMap.put(NomineeDateOfBirth, String.valueOf(EkycAppUtil.getDateStr(dob, "yyyy-MM-dd")));
                if (cbNomineeSameAsPrimaryCorrespondence.isChecked()) {
                    inputMap.put(SameAsCorrNominee, true);
                } else {
                    inputMap.put(SameAsCorrNominee, false);
                }

                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                Date adultAge = EkycAppUtil.getDate(dateFormatter.format(minAdultAge.getTime()), "dd/MM/yyyy");
                Date dateDOB = EkycAppUtil.getDate(etNomineeDOB.getText().toString().trim(), "dd/MM/yyyy");
                if (adultAge.before(dateDOB)) {
//                    inputMap.put(NomineeGuardianRelation, etNomineesGuardianRelationship.getText().toString().trim());
                    inputMap.put(NomineeGuardianRelation, spNomineesGuardianRelationship.getSelectedItem().toString());
                    if (etNomineesGuardianRelationship.getVisibility() == View.VISIBLE) {
                        inputMap.put(NOMINEE_GUARDIAN_RELATION_OTHER, etNomineesGuardianRelationship.getText().toString().trim());
                    } else {
                        inputMap.put(NOMINEE_GUARDIAN_RELATION_OTHER, "");
                    }
                    inputMap.put(NomineeMinorFName, etNomineeGuardianFirstName.getText().toString().trim());
                    inputMap.put(NomineeMinorMName, etNomineeGuardianMiddleName.getText().toString().trim());
                    inputMap.put(NomineeMinorLName, etNomineeGuardianLastName.getText().toString().trim());
                    inputMap.put(NomineeMinorPin, etNomineeGuardianPincodeCorres.getText().toString().trim());
                    inputMap.put(NomineeMinorAddr1, etNomineeGuardianBldgNameCorres.getText().toString().trim());
                    inputMap.put(NomineeMinorAddr2, etNomineeGuardianFlatNoCorres.getText().toString().trim());
                    inputMap.put(NomineeMinorAddr3, etNomineeGuardianAreaCorres.getText().toString().trim());
                    inputMap.put(NomineeMinorLandmark, "");
                    if (etNomineeGuardianCorresCity.getVisibility() == View.VISIBLE) {
                        inputMap.put(NomineeMinorCity, etNomineeGuardianCorresCity.getText().toString().trim());
                        inputMap.put(NomineeMinorState, etNomineeGuardianCorresState.getText().toString().trim());
                        inputMap.put(OtherCityNomineeMinor, "");
                    } else if (spNomineeGuardianCorresCity.getVisibility() == View.VISIBLE) {
                        inputMap.put(NomineeMinorCity, spNomineeGuardianCorresCity.getSelectedItem().toString());
                        inputMap.put(NomineeMinorState, spNomineeGuardianCorresState.getSelectedItem().toString());
                        if (spNomineeGuardianCorresCity.getSelectedItem().toString().equalsIgnoreCase("Other")) {
                            inputMap.put(OtherCityNomineeMinor, etNomineeGuardianCorresCityOther.getText().toString().trim());
                        } else {
                            inputMap.put(OtherCityNomineeMinor, "");
                        }
                    }
                    inputMap.put(NomineeMinorCountry, spNomineeGuardianCorresCountry.getSelectedItem().toString());
                    inputMap.put(NomineeGuardianAge, spNomineeGuardianAge.getSelectedItem().toString());
                    if (!etNomineeGuardianSTDNo.getText().toString().isEmpty()) {
                        inputMap.put(NomineeMinorTel, etNomineeGuardianSTDNo.getText().toString().trim() + "~" + etNomineeGuardianTelNo.getText().toString().trim());
                    } else {
                        inputMap.put(NomineeMinorTel, "");
                    }
                    inputMap.put(NomineeMinorMailId, etNomineeGuardianEmail.getText().toString().trim());
                    if (cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) {
                        inputMap.put(SameAsCorrNomineeGuardian, true);
                    } else {
                        inputMap.put(SameAsCorrNomineeGuardian, false);
                    }
                } else {
                    inputMap.put(NomineeGuardianRelation, "");
                    inputMap.put(NomineeMinorFName, "");
                    inputMap.put(NomineeMinorMName, "");
                    inputMap.put(NomineeMinorLName, "");
                    inputMap.put(NomineeMinorPin, "");
                    inputMap.put(NomineeMinorAddr1, "");
                    inputMap.put(NomineeMinorAddr2, "");
                    inputMap.put(NomineeMinorAddr3, "");
                    inputMap.put(NomineeMinorLandmark, "");
                    inputMap.put(NomineeMinorCity, "");
                    inputMap.put(NomineeMinorState, "");
                    inputMap.put(NomineeMinorCountry, "");
                    inputMap.put(NomineeGuardianAge, "");
                    inputMap.put(NomineeMinorTel, "");
                    inputMap.put(NomineeMinorMailId, "");
                    inputMap.put(SameAsCorrNomineeGuardian, false);
                    inputMap.put(OtherCityNomineeMinor, "");
                }
            }
            String flag = INSERTUPDATENOMINEEDETAILS;
            String category = GETDEMATDETAILS;
            customProgress.setVisibility(View.VISIBLE);
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            if (response.has("Table1")) {
                                JSONArray jsonArray = response.getJSONArray("Table1");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String status = (object.isNull("Status") || object.getString("Status").equals("")) ? "" : object.getString("Status");
                                        String message = (object.isNull("Message") || object.getString("Message").equals("")) ? "" : object.getString("Message");
                                        if (status.equalsIgnoreCase("1")) {
                                            setGotoReviewEdit(false);
                                            setGotoFundTransfer(false);
                                            nomineeUpdate = true;
                                            if (getAppType().equalsIgnoreCase(DIRECT) && !getIsAadharVerified() && !getInterestedInMF()) {
                                                if (toggleJointHolder.isChecked() && toggleNominee.isChecked() && jointHolderUpdate && nomineeUpdate) {
                                                    tabPageIndicator.enableTab(getBankTab());
                                                    viewPager.setCurrentItem(getBankTab());
                                                } else if (toggleJointHolder.isChecked() && !toggleNominee.isChecked() && jointHolderUpdate && nomineeUpdate) {
                                                    tabPageIndicator.enableTab(getBankTab());
                                                    viewPager.setCurrentItem(getBankTab());
                                                } else if (!toggleJointHolder.isChecked() && !toggleNominee.isChecked()) {
                                                    tabPageIndicator.enableTab(getBankTab());
                                                    viewPager.setCurrentItem(getBankTab());
                                                } else if (!toggleJointHolder.isChecked() && toggleNominee.isChecked()) {
                                                    tabPageIndicator.enableTab(getBankTab());
                                                    viewPager.setCurrentItem(getBankTab());
                                                }
                                            } else {
                                                tabPageIndicator.enableTab(getBankTab());
                                                viewPager.setCurrentItem(getBankTab());
                                            }
                                        } else {
                                            commonMsgDialog(getContext(), message, false, "");
//                                            alertDialog(mContext, 0, "", message, "OK", true, null);
                                        }
                                    }
                                }
                            } else {
                                commonMsgDialog(getContext(), SOME_SERVER_ERROR_OCCURRED, false, "");
                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            customProgress.setVisibility(View.GONE);
//                            hideUpdateProgress();
                            commonMsgDialog(getContext(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
//                        customProgress.setVisibility(View.GONE);
                        hideUpdateProgress();
                    } else {
                        customProgress.setVisibility(View.GONE);
//                        hideUpdateProgress();
                        commonMsgDialog(getContext(), SOME_SERVER_ERROR_OCCURRED, false, "");
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
//                    hideUpdateProgress();
                    commonMsgDialog(getContext(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    customProgress.setVisibility(View.GONE);
//                    hideUpdateProgress();

                    if (statusCode == 0) {
                        CustomDialog customDialog = new CustomDialog(mContext, "Connection time out, Please try again later.", true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                            @Override
                            public void dialogAction(MOSLCommon.Action action, Object... data) {

                            }
                        });
                        customDialog.show();
                    } else {
                        commonMsgDialog(getContext(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
//                    hideUpdateProgress();
                    commonMsgDialog(getContext(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(getContext());
        }
    }

    public void validateFirstJointHolderData() {
        try {
            firstUpdate = false;
            if (isBlank(getDematFirstJointHolderResidentialStatus()) || getDematFirstJointHolderResidentialStatus().equalsIgnoreCase("Select")) {
                validateSpinner(spSJHResidentStatus, etSJHResidentStatusError, "Select resident status");
                return;
            } else if (isBlank(getDematAccTitle()) || getDematAccTitle().equalsIgnoreCase("Select")) {
                validateSpinner(spSJHTitle, etSJHTitleError, "Select title");
                return;
            } else if (isBlank(getDematFirstJointHolderFname())) {
                validateEditText(etSJHFirstName, inputLayoutSJHFirstName, "Enter first name");
                return;
            } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematFirstJointHolderFname().trim())) {
                etSJHFirstNameError.setVisibility(View.VISIBLE);
                etSJHFirstNameError.setText(ErrorMsgName);
                etSJHFirstName.requestFocus();
                return;
            } else if (getDematFirstJointHolderMname().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematFirstJointHolderMname().trim())) {
                etSJHMiddleNameError.setVisibility(View.VISIBLE);
                etSJHMiddleNameError.setText(ErrorMsgName);
                etSJHMiddleName.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderLname())) {
                validateEditText(etSJHLastName, inputLayoutSJHLastName, "Enter last name");
                return;
            } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematFirstJointHolderLname().trim())) {
                etSJHLastNameError.setVisibility(View.VISIBLE);
                etSJHLastNameError.setText(ErrorMsgName);
                etSJHLastName.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderSex())) {
                etSJHGenderError.setVisibility(View.VISIBLE);
                etSJHGenderError.setText("Select gender");
                etSJHGenderError.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderRelation()) || getDematFirstJointHolderRelation().equalsIgnoreCase("Select")) {
                validateSpinner(spSJHFatherSpouseSel, etSJHFatherSpouseSelError, "Select father/spouse");
                return;
            } else if (isBlank(getDematFirstJointHolderFHFname())) {
                validateEditText(etSJHFatherSpouseFirstName, inputLayoutSJHFatherSpouseFirstName, "Enter father/spouse first name");
                return;
            } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematFirstJointHolderFHFname().trim())) {
                etSJHFatherSpouseFirstNameError.setVisibility(View.VISIBLE);
                etSJHFatherSpouseFirstNameError.setText(ErrorMsgName);
                etSJHFatherSpouseFirstName.requestFocus();
                return;
            }/* else if (isBlank(getDematFirstJointHolderFHMname())) {
                validateEditText(etSJHFatherSpouseMidName, inputLayoutSJHFatherSpouseMidName, "Enter father/spouse middle name");
                return;
            }*/ else if (getDematFirstJointHolderFHMname().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematFirstJointHolderFHMname().trim())) {
                etSJHFatherSpouseMidNameError.setVisibility(View.VISIBLE);
                etSJHFatherSpouseMidNameError.setText(ErrorMsgName);
                etSJHFatherSpouseMidName.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderFHLname())) {
                validateEditText(etSJHFatherSpouseLastName, inputLayoutSJHFatherSpouseLastName, "Enter father/spouse last name");
                return;
            } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematFirstJointHolderFHLname().trim())) {
                etSJHFatherSpouseLastNameError.setVisibility(View.VISIBLE);
                etSJHFatherSpouseLastNameError.setText(ErrorMsgName);
                etSJHFatherSpouseLastName.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderDOB())) {
                inputLayoutSJHDob.setError("Enter Date of birth");
                etSJHDOB.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderMaritalStatus())) {
                etSJHMaritalError.setVisibility(View.VISIBLE);
                etSJHMaritalError.setText("Select marital status");
                etSJHMaritalError.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderPan())) {
                validateEditText(etSJHPAN, inputLayoutSJHPAN, "Enter PAN number");
                return;
            } else if (!isValidPannumber(getDematFirstJointHolderPan())) {
                etSJHPAN.requestFocus();
                return;
            }/* else if (isBlank(getDematFirstJointHolderUID())) {
                validateEditText(etSJHAadhar, inputLayoutSJHAadhar, "Enter Aadhar number");
                return;
            }*/ else if (!isBlank(getDematFirstJointHolderUID()) && getDematFirstJointHolderUID().length() < 12) {
                etSJHAadhar.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderMobile())) {
                validateEditText(etSJHMobile, inputLayoutSJHMobile, "Enter mobile number");
                return;
            } else if (getDematFirstJointHolderMobile().length() < 10) {
                etSJHMobile.requestFocus();
                return;
            } else if (!ValidationRule.validateMobile(getDematFirstJointHolderMobile())) {
                etSJHMobile.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderEmail())) {
                validateEditText(etSJHEmail, inputLayoutSJHEmail, "Enter email id");
                return;
            } else if (!isValidEmail(getDematFirstJointHolderEmail())) {
                etSJHEmail.requestFocus();
                return;
            } else if ((etSJHSTDNo.getText().toString().trim().length() > 0 || etSJHTelNo.getText().toString().trim().length() > 0) && (!ValidationRule.validateTel(getDematFirstJointHolderTeleNo()))) {
                etSJHSTDTelError.setVisibility(View.VISIBLE);
                etSJHSTDTelError.setText(Res_Telephone_number_min_11_digit);
                etSJHSTDNo.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderAddressProof()) || getDematFirstJointHolderAddressProof().equalsIgnoreCase("Select")) {
                validateSpinner(spSJHAddProofCorres, etSJHAddProofCorresError, "Select adderss proof");
                spSJHAddProofCorres.setEnabled(true);
                return;
            } else if (isBlank(getDematFirstJointHolderAdd1())) {
                validateEditText(etSJHBldgNameCorres, inputLayoutSJHBldgNameCorres, "Enter bldg no/name");
                etSJHBldgNameCorres.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbSJHSameAsPrimaryCorrespondence, etSJHBldgNameCorres)) {
                showErrorMsg(true, etSJHBldgNameCorres, inputLayoutSJHBldgNameCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematFirstJointHolderAdd2())) {
                validateEditText(etSJHFlatNoCorres, inputLayoutSJHFlatNoCorres, "Enter flat no/road name");
                etSJHFlatNoCorres.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbSJHSameAsPrimaryCorrespondence, etSJHFlatNoCorres)) {
                showErrorMsg(true, etSJHFlatNoCorres, inputLayoutSJHFlatNoCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematFirstJointHolderAdd3())) {
                validateEditText(etSJHAreaCorres, inputLayoutSJHAreaCorres, "Enter area");
                etSJHAreaCorres.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbSJHSameAsPrimaryCorrespondence, etSJHAreaCorres)) {
                showErrorMsg(true, etSJHAreaCorres, inputLayoutSJHAreaCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematFirstJointHolderPin())) {
                validateEditText(etSJHPincodeCorres, inputLayoutSJHPincodeCorres, "Enter pincode");
                etSJHPincodeCorres.setEnabled(true);
                return;
            } else if (getDematFirstJointHolderPin().length() < 6) {
                etSJHPincodeCorres.requestFocus();
                return;
            } else if (spSJHCorresState.getVisibility() == View.VISIBLE && (isBlank(getDematFirstJointHolderState()) || getDematFirstJointHolderState().equalsIgnoreCase("Select"))) {
                validateSpinner(spSJHCorresState, etSJHCorresStateError, "Select state");
                spSJHCorresState.setEnabled(true);
                return;
            } else if (etSJHCorresState.getVisibility() == View.VISIBLE && (isBlank(getDematFirstJointHolderState()) || getDematFirstJointHolderState().equalsIgnoreCase("Select"))) {
                etSJHCorresState.setEnabled(true);
                etSJHCorresStateError.setVisibility(View.VISIBLE);
                etSJHCorresStateError.setText("Select State");
                etSJHCorresStateError.requestFocus();
                return;
            } else if (spSJHCorresCity.getVisibility() == View.VISIBLE && (isBlank(getDematFirstJointHolderCity()) || getDematFirstJointHolderCity().equalsIgnoreCase("Select"))) {
                validateSpinner(spSJHCorresCity, etSJHCorresCityError, "Select city");
                spSJHCorresState.setEnabled(true);
                spSJHCorresCity.setEnabled(true);
                return;
            } else if (etSJHCorresCity.getVisibility() == View.VISIBLE && (isBlank(getDematFirstJointHolderCity()) || getDematFirstJointHolderCity().equalsIgnoreCase("Select"))) {
                etSJHCorresCity.setEnabled(true);
                etSJHCorresCityError.setVisibility(View.VISIBLE);
                etSJHCorresCityError.setText("Select city");
                etSJHCorresCityError.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderCountry()) || getDematFirstJointHolderCountry().equalsIgnoreCase("Select")) {
                validateSpinner(spSJHCorresCountry, etSJHCorresCountryError, "Select country");
                spSJHCorresCountry.setEnabled(true);
                return;
            } else if (isBlank(getDematFirstJointHolderAddressProofPerm()) || getDematFirstJointHolderAddressProofPerm().equalsIgnoreCase("Select")) {
                validateSpinner(spSJHAddProofPerm, etSJHAddProofPermError, "Select adderss proof");
                spSJHAddProofPerm.setEnabled(true);
                return;
            } else if (isBlank(getDematFirstJointHolderAdd1Perm())) {
                validateEditText(etSJHBldgNamePerm, inputLayoutSJHBldgNamePerm, "Enter bldg no/name");
                etSJHBldgNamePerm.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbSJHSameAsCorrespondence, etSJHBldgNamePerm)) {
                showErrorMsg(true, etSJHBldgNamePerm, inputLayoutSJHBldgNamePerm, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematFirstJointHolderAdd2Perm())) {
                validateEditText(etSJHFlatNamePerm, inputLayoutSJHFlatNamePerm, "Enter flat no/road name");
                etSJHFlatNamePerm.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbSJHSameAsCorrespondence, etSJHFlatNamePerm)) {
                showErrorMsg(true, etSJHFlatNamePerm, inputLayoutSJHFlatNamePerm, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematFirstJointHolderAdd3Perm())) {
                validateEditText(etSJHAreaPerm, inputLayoutSJHAreaPerm, "Enter area");
                etSJHAreaPerm.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbSJHSameAsCorrespondence, etSJHAreaPerm)) {
                showErrorMsg(true, etSJHAreaPerm, inputLayoutSJHAreaPerm, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematFirstJointHolderPinPerm())) {
                validateEditText(etSJHPincodePerm, inputLayoutSJHPincodePerm, "Enter pincode");
                etSJHPincodePerm.setEnabled(true);
                return;
            } else if (getDematFirstJointHolderPinPerm().length() < 6) {
                etSJHPincodePerm.requestFocus();
                return;
            } else if (spSJHPermState.getVisibility() == View.VISIBLE && (isBlank(getDematFirstJointHolderStatePerm()) || getDematFirstJointHolderStatePerm().equalsIgnoreCase("Select"))) {
                validateSpinner(spSJHPermState, etSJHPermStateError, "Select state");
                spSJHPermState.setEnabled(true);
                return;
            } else if (etSJHPermState.getVisibility() == View.VISIBLE && (isBlank(getDematFirstJointHolderStatePerm()) || getDematFirstJointHolderStatePerm().equalsIgnoreCase("Select"))) {
                etSJHPermState.setEnabled(true);
                etSJHPermStateError.setVisibility(View.VISIBLE);
                etSJHPermStateError.setText("Select State");
                etSJHPermStateError.requestFocus();
                return;
            } else if (spSJHPermCity.getVisibility() == View.VISIBLE && (isBlank(getDematFirstJointHolderCityPerm()) || getDematFirstJointHolderCityPerm().equalsIgnoreCase("Select"))) {
                validateSpinner(spSJHPermCity, etSJHPermCityError, "Select city");
                spSJHPermState.setEnabled(true);
                spSJHPermCity.setEnabled(true);
                return;
            } else if (etSJHPermCity.getVisibility() == View.VISIBLE && (isBlank(getDematFirstJointHolderCityPerm()) || getDematFirstJointHolderCityPerm().equalsIgnoreCase("Select"))) {
                etSJHPermCity.setEnabled(true);
                etSJHPermCityError.setVisibility(View.VISIBLE);
                etSJHPermCityError.setText("Select city");
                etSJHPermCityError.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderCountryPerm()) || getDematFirstJointHolderCountryPerm().equalsIgnoreCase("Select")) {
                validateSpinner(spSJHPermCountry, etSJHPermCountryError, "Select country");
                spSJHPermCountry.setEnabled(true);
                return;
            } else if (isBlank(getDematFirstJointHolderAnnualIncome()) || getDematFirstJointHolderAnnualIncome().equalsIgnoreCase("Select")) {
                etSJHIncomeError.setVisibility(View.VISIBLE);
                etSJHIncomeError.setText("Select income");
                etSJHIncomeError.requestFocus();
                return;
            } else if (isBlank(getDematFirstJointHolderOccupation()) || getDematFirstJointHolderOccupation().equalsIgnoreCase("Select")) {
                etSJHOccupationError.setVisibility(View.VISIBLE);
                etSJHOccupationError.setText("Select occupation");
                etSJHOccupationError.requestFocus();
                return;
            } else {
                firstUpdate = true;
                if (spNumberOfJointHolder.getSelectedItem().toString().equalsIgnoreCase("2") && !secondUpdate) {
                    rlSecondJointHolder.setVisibility(View.GONE);
                    rlThirdJointHolder.setVisibility(View.VISIBLE);
                    CustomDialog cd = new CustomDialog(mContext, "Set third joint holder detail", true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                        @Override
                        public void dialogAction(MOSLCommon.Action action, Object... data) {

                        }
                    });
                    cd.show();
                }
            }
            secondJointHolderName = etSJHFirstName.getText().toString() + " " + etSJHMiddleName.getText().toString() + " " + etSJHLastName.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validateSecondJointHolderData() {
        try {
            secondUpdate = false;
            if (isBlank(getDematSecondJointHolderResidentialStatus()) || getDematSecondJointHolderResidentialStatus().equalsIgnoreCase("Select")) {
                validateSpinner(spTJHResidentStatus, etTJHResidentStatusError, "Select resident status");
                return;
            } else if (isBlank(getDematSecondAccTitle()) || getDematSecondAccTitle().equalsIgnoreCase("Select")) {
                validateSpinner(spTJHTitle, etTJHTitleError, "Select title");
                return;
            } else if (isBlank(getDematSecondJointHolderFname())) {
                validateEditText(etTJHFirstName, inputLayoutTJHFirstName, "Enter first name");
                return;
            } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematSecondJointHolderFname().trim())) {
                etTJHFirstNameError.setVisibility(View.VISIBLE);
                etTJHFirstNameError.setText(ErrorMsgName);
                etTJHFirstName.requestFocus();
                return;
            } else if (getDematSecondJointHolderMname().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematSecondJointHolderMname().trim())) {
                etTJHMiddleNameError.setVisibility(View.VISIBLE);
                etTJHMiddleNameError.setText(ErrorMsgName);
                etTJHMiddleName.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderLname())) {
                validateEditText(etTJHLastName, inputLayoutTJHLastName, "Enter last name");
                return;
            } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematSecondJointHolderLname().trim())) {
                etTJHLastNameError.setVisibility(View.VISIBLE);
                etTJHLastNameError.setText(ErrorMsgName);
                etTJHLastName.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderSex())) {
                etTJHGenderError.setVisibility(View.VISIBLE);
                etTJHGenderError.setText("Select gender");
                etTJHGenderError.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderRelation()) || getDematSecondJointHolderRelation().equalsIgnoreCase("Select")) {
                validateSpinner(spTJHFatherSpouseSel, etTJHFatherSpouseSelError, "Select father/spouse");
                return;
            } else if (isBlank(getDematSecondJointHolderFHFname())) {
                validateEditText(etTJHFatherSpouseFirstName, inputLayoutTJHFatherSpouseFirstName, "Enter father/spouse first name");
                return;
            } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematSecondJointHolderFHFname().trim())) {
                etTJHFatherSpouseFirstNameError.setVisibility(View.VISIBLE);
                etTJHFatherSpouseFirstNameError.setText(ErrorMsgName);
                etTJHFatherSpouseFirstName.requestFocus();
                return;
            }/* else if (isBlank(getDematSecondJointHolderFHMname())) {
                validateEditText(etTJHFatherSpouseMidName, inputLayoutTJHFatherSpouseMidName, "Enter father/spouse middle name");
                return;
            }*/ else if (getDematSecondJointHolderFHMname().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematSecondJointHolderFHMname().trim())) {
                etTJHFatherSpouseMidNameError.setVisibility(View.VISIBLE);
                etTJHFatherSpouseMidNameError.setText(ErrorMsgName);
                etTJHFatherSpouseMidName.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderFHLname())) {
                validateEditText(etTJHFatherSpouseLastName, inputLayoutTJHFatherSpouseLastName, "Enter father/spouse last name");
                return;
            } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(getDematSecondJointHolderFHLname().trim())) {
                etTJHFatherSpouseLastNameError.setVisibility(View.VISIBLE);
                etTJHFatherSpouseLastNameError.setText(ErrorMsgName);
                etTJHFatherSpouseLastName.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderDOB())) {
                inputLayoutTJHDob.setError("Enter Date of birth");
                etTJHDOB.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderMaritalStatus())) {
                etTJHmaritalError.setVisibility(View.VISIBLE);
                etTJHmaritalError.setText("Select marital status");
                etTJHmaritalError.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderPan())) {
                validateEditText(etTJHPAN, inputLayoutTJHPAN, "Enter PAN number");
                return;
            } else if (!isValidPannumber(getDematSecondJointHolderPan())) {
                etTJHPAN.requestFocus();
                return;
            }/* else if (isBlank(getDematSecondJointHolderUID())) {
                validateEditText(etTJHAadhar, inputLayoutTJHAadhar, "Enter aadhar number");
                return;
            }*/ else if (!isBlank(getDematSecondJointHolderUID()) && getDematSecondJointHolderUID().length() < 12) {
                etTJHAadhar.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderMobile())) {
                validateEditText(etTJHMobile, inputLayoutTJHMobile, "Enter mobile number");
                return;
            } else if (getDematSecondJointHolderMobile().length() < 10) {
                etTJHMobile.requestFocus();
                return;
            } else if (!ValidationRule.validateMobile(getDematSecondJointHolderMobile())) {
                etTJHMobile.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderEmail())) {
                validateEditText(etTJHEmail, inputLayoutTJHEmail, "Enter email id");
                return;
            } else if (!isValidEmail(getDematSecondJointHolderEmail())) {
                etTJHEmail.requestFocus();
                return;
            } else if ((etTJHSTDNo.getText().toString().trim().length() > 0 || etTJHTelNo.getText().toString().trim().length() > 0) && (!ValidationRule.validateTel(getDematSecondJointHolderTeleNo()))) {
                etTJHSTDTelError.setVisibility(View.VISIBLE);
                etTJHSTDTelError.setText(Res_Telephone_number_min_11_digit);
                etTJHSTDNo.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderAddressProof()) || getDematSecondJointHolderAddressProof().equalsIgnoreCase("Select")) {
                validateSpinner(spTJHAddProofCorres, etTJHAddProofCorresError, "Select adderss proof");
                spTJHAddProofCorres.setEnabled(true);
                return;
            } else if (isBlank(getDematSecondJointHolderAdd1())) {
                validateEditText(etTJHBldgNameCorres, inputLayoutTJHBldgNameCorres, "Enter bldg no/name");
                etTJHBldgNameCorres.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbTJHSameAsPrimaryCorrespondence, etTJHBldgNameCorres)) {
                showErrorMsg(true, etTJHBldgNameCorres, inputLayoutTJHBldgNameCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematSecondJointHolderAdd2())) {
                validateEditText(etTJHFlatNoCorres, inputLayoutTJHFlatNoCorres, "Enter flat no/road name");
                etTJHFlatNoCorres.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbTJHSameAsPrimaryCorrespondence, etTJHFlatNoCorres)) {
                showErrorMsg(true, etTJHFlatNoCorres, inputLayoutTJHFlatNoCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematSecondJointHolderAdd3())) {
                validateEditText(etTJHAreaCorres, inputLayoutTJHAreaCorres, "Enter area");
                etTJHAreaCorres.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbTJHSameAsPrimaryCorrespondence, etTJHAreaCorres)) {
                showErrorMsg(true, etTJHAreaCorres, inputLayoutTJHAreaCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematSecondJointHolderPin())) {
                validateEditText(etTJHPincodeCorres, inputLayoutTJHPincodeCorres, "Enter pincode");
                etTJHPincodeCorres.setEnabled(true);
                return;
            } else if (getDematSecondJointHolderPin().length() < 6) {
                etTJHPincodeCorres.requestFocus();
                return;
            } else if (spTJHCorresState.getVisibility() == View.VISIBLE && (isBlank(getDematSecondJointHolderState()) || getDematSecondJointHolderState().equalsIgnoreCase("Select"))) {
                validateSpinner(spTJHCorresState, etTJHCorresStateError, "Select state");
                spTJHCorresState.setEnabled(true);
                return;
            } else if (etTJHCorresState.getVisibility() == View.VISIBLE && (isBlank(getDematSecondJointHolderState()) || getDematSecondJointHolderState().equalsIgnoreCase("Select"))) {
                etTJHCorresState.setEnabled(true);
                etTJHCorresStateError.setVisibility(View.VISIBLE);
                etTJHCorresStateError.setText("Select State");
                etTJHCorresStateError.requestFocus();
                return;
            } else if (spTJHCorresCity.getVisibility() == View.VISIBLE && (isBlank(getDematSecondJointHolderCity()) || getDematSecondJointHolderCity().equalsIgnoreCase("Select"))) {
                validateSpinner(spTJHCorresCity, etTJHCorresCityError, "Select city");
                spTJHCorresState.setEnabled(true);
                spTJHCorresCity.setEnabled(true);
                return;
            } else if (etTJHCorresCity.getVisibility() == View.VISIBLE && (isBlank(getDematSecondJointHolderCity()) || getDematSecondJointHolderCity().equalsIgnoreCase("Select"))) {
                etTJHCorresCity.setEnabled(true);
                etTJHCorresCityError.setVisibility(View.VISIBLE);
                etTJHCorresCityError.setText("Select city");
                etTJHCorresCityError.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderCountry()) || getDematSecondJointHolderCountry().equalsIgnoreCase("Select")) {
                validateSpinner(spTJHCorresCountry, etTJHCorresCountryError, "Select country");
                spTJHCorresCountry.setEnabled(true);
                return;
            } else if (isBlank(getDematSecondJointHolderAddressProofPerm()) || getDematSecondJointHolderAddressProofPerm().equalsIgnoreCase("Select")) {
                validateSpinner(spTJHAddProofPerm, etTJHAddProofPermError, "Select adderss proof");
                spTJHAddProofPerm.setEnabled(true);
                return;
            } else if (isBlank(getDematSecondJointHolderAdd1Perm())) {
                validateEditText(etTJHBldgNamePerm, inputLayoutTJHBldgNamePerm, "Enter bldg no/name");
                etTJHBldgNamePerm.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbTJHSameAsCorrespondence, etTJHBldgNamePerm)) {
                showErrorMsg(true, etTJHBldgNamePerm, inputLayoutTJHBldgNamePerm, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematSecondJointHolderAdd2Perm())) {
                validateEditText(etTJHFlatNamePerm, inputLayoutTJHFlatNamePerm, "Enter flat no/road name");
                etTJHFlatNamePerm.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbTJHSameAsCorrespondence, etTJHFlatNamePerm)) {
                showErrorMsg(true, etTJHFlatNamePerm, inputLayoutTJHFlatNamePerm, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematSecondJointHolderAdd3Perm())) {
                validateEditText(etTJHAreaPerm, inputLayoutTJHAreaPerm, "Enter area");
                etTJHAreaPerm.setEnabled(true);
                return;
            } else if (!isJointHolderAddressValidate(cbTJHSameAsCorrespondence, etTJHAreaPerm)) {
                showErrorMsg(true, etTJHAreaPerm, inputLayoutTJHAreaPerm, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (isBlank(getDematSecondJointHolderPinPerm())) {
                validateEditText(etTJHPincodePerm, inputLayoutTJHPincodePerm, "Enter pincode");
                etTJHPincodePerm.setEnabled(true);
                return;
            } else if (getDematSecondJointHolderPinPerm().length() < 6) {
                etTJHPincodePerm.requestFocus();
                return;
            } else if (spTJHPermState.getVisibility() == View.VISIBLE && (isBlank(getDematSecondJointHolderStatePerm()) || getDematSecondJointHolderStatePerm().equalsIgnoreCase("Select"))) {
                validateSpinner(spTJHPermState, etTJHPermStateError, "Select state");
                spTJHPermState.setEnabled(true);
                return;
            } else if (etTJHPermState.getVisibility() == View.VISIBLE && (isBlank(getDematSecondJointHolderStatePerm()) || getDematSecondJointHolderStatePerm().equalsIgnoreCase("Select"))) {
                etTJHPermState.setEnabled(true);
                etTJHPermStateError.setVisibility(View.VISIBLE);
                etTJHPermStateError.setText("Select State");
                etTJHPermStateError.requestFocus();
                return;
            } else if (spTJHPermCity.getVisibility() == View.VISIBLE && (isBlank(getDematSecondJointHolderCityPerm()) || getDematSecondJointHolderCityPerm().equalsIgnoreCase("Select"))) {
                validateSpinner(spTJHPermCity, etTJHPermCityError, "Select city");
                spTJHPermState.setEnabled(true);
                spTJHPermCity.setEnabled(true);
                return;
            } else if (etTJHPermCity.getVisibility() == View.VISIBLE && (isBlank(getDematSecondJointHolderCityPerm()) || getDematSecondJointHolderCityPerm().equalsIgnoreCase("Select"))) {
                etTJHPermCity.setEnabled(true);
                etTJHPermCityError.setVisibility(View.VISIBLE);
                etTJHPermCityError.setText("Select city");
                etTJHPermCityError.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderCountryPerm()) || getDematSecondJointHolderCountryPerm().equalsIgnoreCase("Select")) {
                validateSpinner(spTJHPermCountry, etTJHPermCountryError, "Select country");
                spTJHPermCountry.setEnabled(true);
                return;
            } else if (isBlank(getDematSecondJointHolderAnnualIncome()) || getDematSecondJointHolderAnnualIncome().equalsIgnoreCase("Select")) {
                etTJHIncomeError.setVisibility(View.VISIBLE);
                etTJHIncomeError.setText("Select income");
                etTJHIncomeError.requestFocus();
                return;
            } else if (isBlank(getDematSecondJointHolderOccupation()) || getDematSecondJointHolderOccupation().equalsIgnoreCase("Select")) {
                etTJHOccupationError.setVisibility(View.VISIBLE);
                etTJHOccupationError.setText("Select occupation");
                etTJHOccupationError.requestFocus();
                return;
            } else {
                if (firstUpdate) {
                    secondUpdate = true;
                } else {
                    secondUpdate = true;
                    if (spNumberOfJointHolder.getSelectedItem().toString().equalsIgnoreCase("2") && !firstUpdate) {
                        rlThirdJointHolder.setVisibility(View.GONE);
                        rlSecondJointHolder.setVisibility(View.VISIBLE);
                        CustomDialog cd = new CustomDialog(mContext, "Set second joint holder detail", true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                            @Override
                            public void dialogAction(MOSLCommon.Action action, Object... data) {

                            }
                        });
                    }
                }
            }
            thirdJointHolderName = etTJHFirstName.getText().toString() + " " + etTJHMiddleName.getText().toString() + " " + etTJHLastName.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateNominee() {
        try {
            if (adultFlag) {
                guardianName = etNomineeGuardianFirstName.getText().toString() + " " + etNomineeGuardianMiddleName.getText().toString() + " " + etNomineeGuardianLastName.getText().toString();
            }
            nomineeName = etNomineeFirstName.getText().toString() + " " + etNomineeMiddleName.getText().toString() + " " + etNomineeLastName.getText().toString();
            if (!validateEditText(etNomineeFirstName, inputLayoutNomineeFirstName, "Enter first name")) {
                return;
            } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeFirstName.getText().toString().trim())) {
                etNomineeFirstNameError.setVisibility(View.VISIBLE);
                etNomineeFirstNameError.setText(ErrorMsgName);
                etNomineeFirstName.requestFocus();
                return;
            } else if (etNomineeMiddleName.getText().toString().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeMiddleName.getText().toString().trim())) {
                etNomineeMiddleNameError.setVisibility(View.VISIBLE);
                etNomineeMiddleNameError.setText(ErrorMsgName);
                etNomineeMiddleName.requestFocus();
                return;
            } else if (!validateEditText(etNomineeLastName, inputLayoutNomineeLastName, "Enter last name")) {
                return;
            } else if (!ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeLastName.getText().toString().trim())) {
                etNomineeLastNameError.setVisibility(View.VISIBLE);
                etNomineeLastNameError.setText(ErrorMsgName);
                etNomineeLastName.requestFocus();
                return;
            }
            /*else if (!adultFlag && !validateEditText(etNomineePAN, inputLayoutNomineePAN, "Enter PAN number")) {
                return;
            }*/
            else if ((etNomineePAN.getText().toString().trim().length() == 0) &&
                    (etNomineeAadhar.getText().toString().trim().length() == 0)) {
                commonMsgDialog(mContext, "Either PAN or Aadhar is required in Nominee details", false, "");
            } else if (etNomineePAN.getText().toString().trim().length() > 0 && !isValidPannumber(etNomineePAN.getText().toString().trim())) {
                etNomineePANError.setVisibility(View.VISIBLE);
                etNomineePANError.setText("Invalid PAN number");
                etNomineePAN.requestFocus();
                return;
            } else if (etNomineeAadhar.getText().toString().trim().length() > 0 && etNomineeAadhar.getText().toString().trim().length() < 12) {
                tvNomineeAadharError.setVisibility(View.VISIBLE);
                tvNomineeAadharError.setText("Invalid aadhar number");
                etNomineeAadhar.requestFocus();
                return;
            } else if (!validateEditText(etNomineeDOB, inputLayoutNomineeDob, "Enter Date of Birth")) {
                etNomineeDOBRequestFocus.requestFocus();
                return;
            } else if (!(getInterestedInMF() && cbNomineeSameAsPrimaryCorrespondence.isChecked()) && !validateEditText(etNomineeBldgNameCorres, inputLayoutNomineeBldgNameCorres, "Enter Bldg No/Bldg Name")) {
                etNomineeBldgNameCorres.setEnabled(true);
                return;
            } else if (!(getInterestedInMF() && cbNomineeSameAsPrimaryCorrespondence.isChecked()) && etNomineeBldgNameCorres.isEnabled() && !etNomineeBldgNameCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeBldgNameCorres.getText().toString())) {
                /*inputLayoutNomineeBldgNameCorres.setErrorEnabled(true);
                inputLayoutNomineeBldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);*/
                showErrorMsg(true, etNomineeBldgNameCorres, inputLayoutNomineeBldgNameCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (!(getInterestedInMF() && cbNomineeSameAsPrimaryCorrespondence.isChecked()) && !validateEditText(etNomineeFlatNoCorres, inputLayoutNomineeFlatNoCorres, "Enter Flat No/Road Name")) {
                etNomineeFlatNoCorres.setEnabled(true);
                return;
            } else if (!(getInterestedInMF() && cbNomineeSameAsPrimaryCorrespondence.isChecked()) && etNomineeFlatNoCorres.isEnabled() && !etNomineeFlatNoCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeFlatNoCorres.getText().toString())) {
                /*inputLayoutNomineeBldgNameCorres.setErrorEnabled(true);
                inputLayoutNomineeBldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);*/
                showErrorMsg(true, etNomineeFlatNoCorres, inputLayoutNomineeFlatNoCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (!(getInterestedInMF() && cbNomineeSameAsPrimaryCorrespondence.isChecked()) && !validateEditText(etNomineeAreaCorres, inputLayoutNomineeAreaCorres, "Area/Locality")) {
                etNomineeAreaCorres.setEnabled(true);
                return;
            } else if (!(getInterestedInMF() && cbNomineeSameAsPrimaryCorrespondence.isChecked()) && etNomineeAreaCorres.isEnabled() && !etNomineeAreaCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeAreaCorres.getText().toString())) {
                /*inputLayoutNomineeBldgNameCorres.setErrorEnabled(true);
                inputLayoutNomineeBldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);*/
                showErrorMsg(true, etNomineeAreaCorres, inputLayoutNomineeAreaCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (!(getInterestedInMF() && cbNomineeSameAsPrimaryCorrespondence.isChecked()) && !validateEditText(etNomineePincodeCorres, inputLayoutNomineePincodeCorres, "Enter Pin Code")) {
                etNomineePincodeCorres.setEnabled(true);
                return;
            } else if (etNomineePincodeCorres.length() < 6) {
                etNomineePincodeCorres.setEnabled(true);
                etNomineePincodeCorres.requestFocus();
                return;
            } else if ((spNomineeCorresState.getVisibility() == View.VISIBLE) && spNomineeCorresState.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                spNomineeCorresState.setEnabled(true);
                etNomineeCorresStateError.setVisibility(View.VISIBLE);
                etNomineeCorresStateError.setText("Select State");
                etNomineeCorresStateError.requestFocus();
                return;
            } else if (!(getInterestedInMF() && cbNomineeSameAsPrimaryCorrespondence.isChecked()) && (etNomineeCorresState.getVisibility() == View.VISIBLE) && etNomineeCorresState.getText().toString().trim().equals("")) {
                etNomineeCorresState.setEnabled(true);
                etNomineeCorresStateError.setVisibility(View.VISIBLE);
                etNomineeCorresStateError.setText("Select State");
                etNomineeCorresStateError.requestFocus();
                return;
            } else if ((spNomineeCorresCity.getVisibility() == View.VISIBLE) && spNomineeCorresCity.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                spNomineeCorresState.setEnabled(true);
                spNomineeCorresCity.setEnabled(true);
                etNomineeCorresCityError.setVisibility(View.VISIBLE);
                etNomineeCorresCityError.setText("Select City");
                etNomineeCorresCityError.requestFocus();
                return;
            } else if (!(getInterestedInMF() && cbNomineeSameAsPrimaryCorrespondence.isChecked()) && (etNomineeCorresCity.getVisibility() == View.VISIBLE) && etNomineeCorresCity.getText().toString().trim().equals("")) {
                etNomineeCorresCity.setEnabled(true);
                etNomineeCorresCityError.setVisibility(View.VISIBLE);
                etNomineeCorresCityError.setText("Select City");
                etNomineeCorresCityError.requestFocus();
                return;
            } else if (!(getInterestedInMF() && cbNomineeSameAsPrimaryCorrespondence.isChecked()) && spNomineeCorresCountry.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                spNomineeCorresCountry.setEnabled(true);
                etNomineeCorresCountryError.setVisibility(View.VISIBLE);
                etNomineeCorresCountryError.setText("Select Country");
                etNomineeCorresCountryError.requestFocus();
                return;
            } else if ((etNomineeSTDNo.getText().toString().trim().length() > 0 || etNomineeTelNo.getText().toString().trim().length() > 0) && (!ValidationRule.validateTel(etNomineeSTDNo.getText().toString() + etNomineeTelNo.getText().toString()))) {
                etNomineeSTDTelError.setVisibility(View.VISIBLE);
                etNomineeSTDTelError.setText(Res_Telephone_number_min_11_digit);
                etNomineeSTDNo.requestFocus();
                return;
            } else if (etNomineeEmail.getText().toString().trim().length() > 0 && !validateEditText(etNomineeEmail, inputLayoutNomineeEmail, "Enter Email Id")) {
                return;
            } else if (etNomineeEmail.getText().toString().trim().length() > 0 && !isValidEmail(etNomineeEmail.getText().toString())) {
                etNomineeEmail.requestFocus();
                return;
            } else if (spNomineesRelationship.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                etNomineesRelationshipError.setVisibility(View.VISIBLE);
                etNomineesRelationshipError.setText("Select Nominees Relationship with Primary Applicant");
                etNomineesRelationshipError.requestFocus();
                return;
            } else if ((etNomineesRelationship.getVisibility() == View.VISIBLE) && !validateEditText(etNomineesRelationship, inputLayoutNomineesRelationship, "Enter Nominees Relationship with Primary Applicant")) {
                return;
            }/* else if (!validateEditText(etNomineePrimaryApplicantsRelationship, inputLayoutNomineePrimaryApplicantsRelationship, "Enter Primary Applicants relationship with Nominee")) {
                return;
            }*/ else if (adultFlag && !validateEditText(etNomineeGuardianFirstName, inputLayoutNomineeGuardianFirstName, "Enter first name")) {
                return;
            } else if (adultFlag && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeGuardianFirstName.getText().toString().trim())) {
                etNomineeGuardianFirstNameError.setVisibility(View.VISIBLE);
                etNomineeGuardianFirstNameError.setText(ErrorMsgName);
                etNomineeGuardianFirstName.requestFocus();
                return;
            } else if (adultFlag && etNomineeGuardianMiddleName.getText().toString().trim().length() > 0 && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeGuardianMiddleName.getText().toString().trim())) {
                etNomineeGuardianMiddleNameError.setVisibility(View.VISIBLE);
                etNomineeGuardianMiddleNameError.setText(ErrorMsgName);
                etNomineeGuardianMiddleName.requestFocus();
                return;
            } else if (adultFlag && !validateEditText(etNomineeGuardianLastName, inputLayoutNomineeGuardianLastName, "Enter last name")) {
                return;
            } else if (adultFlag && !ValidationRule.validateOnlyAlphabetWithoutSplChar(etNomineeGuardianLastName.getText().toString().trim())) {
                etNomineeGuardianLastNameError.setVisibility(View.VISIBLE);
                etNomineeGuardianLastNameError.setText(ErrorMsgName);
                etNomineeGuardianLastName.requestFocus();
                return;
            } else if (adultFlag && spNomineesGuardianRelationship.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                etNomineesGuardianRelationshipError.setVisibility(View.VISIBLE);
                etNomineesGuardianRelationshipError.setText("Select Relationship with Client");
                etNomineesGuardianRelationshipError.requestFocus();
                return;
            } else if (adultFlag && (etNomineesGuardianRelationship.getVisibility() == View.VISIBLE) && !validateEditText(etNomineesGuardianRelationship, inputLayoutNomineesGuardianRelationship, "Enter Realtionship with Client")) {
                return;
            } else if (adultFlag && (!(getInterestedInMF() && cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) && !validateEditText(etNomineeGuardianBldgNameCorres, inputLayoutNomineeGuardianBldgNameCorres, "Enter Bldg No/Bldg Name"))) {
                etNomineeGuardianBldgNameCorres.setEnabled(true);
                return;
            } else if (adultFlag && !(getInterestedInMF() && cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) && etNomineeGuardianBldgNameCorres.isEnabled() && !etNomineeGuardianBldgNameCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeGuardianBldgNameCorres.getText().toString())) {
                /*inputLayoutNomineeBldgNameCorres.setErrorEnabled(true);
                inputLayoutNomineeBldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);*/
                showErrorMsg(true, etNomineeGuardianBldgNameCorres, inputLayoutNomineeGuardianBldgNameCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (adultFlag && (!(getInterestedInMF() && cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) && !validateEditText(etNomineeGuardianFlatNoCorres, inputLayoutNomineeGuardianFlatNoCorres, "Enter Flat No/Road Name"))) {
                etNomineeGuardianFlatNoCorres.setEnabled(true);
                return;
            } else if (adultFlag && !(getInterestedInMF() && cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) && etNomineeGuardianFlatNoCorres.isEnabled() && !etNomineeGuardianFlatNoCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeGuardianFlatNoCorres.getText().toString())) {
                /*inputLayoutNomineeBldgNameCorres.setErrorEnabled(true);
                inputLayoutNomineeBldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);*/
                showErrorMsg(true, etNomineeGuardianFlatNoCorres, inputLayoutNomineeGuardianFlatNoCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (adultFlag && (!(getInterestedInMF() && cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) && !validateEditText(etNomineeGuardianAreaCorres, inputLayoutNomineeGuardianAreaCorres, "Area/Locality"))) {
                etNomineeGuardianAreaCorres.setEnabled(true);
                return;
            } else if (adultFlag && !(getInterestedInMF() && cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) && etNomineeGuardianAreaCorres.isEnabled() && !etNomineeGuardianAreaCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeGuardianAreaCorres.getText().toString())) {
                /*inputLayoutNomineeBldgNameCorres.setErrorEnabled(true);
                inputLayoutNomineeBldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);*/
                showErrorMsg(true, etNomineeGuardianAreaCorres, inputLayoutNomineeGuardianAreaCorres, Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
                return;
            } else if (adultFlag && (!(getInterestedInMF() && cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) && !validateEditText(etNomineeGuardianPincodeCorres, inputLayoutNomineeGuardianPincodeCorres, "Enter Pin Code"))) {
                etNomineeGuardianPincodeCorres.setEnabled(true);
                return;
            } else if (adultFlag && etNomineeGuardianPincodeCorres.length() < 6) {
                etNomineeGuardianPincodeCorres.setEnabled(true);
                etNomineeGuardianPincodeCorres.requestFocus();
                return;
            } else if (adultFlag && (spNomineeGuardianCorresState.getVisibility() == View.VISIBLE) && spNomineeGuardianCorresState.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                spNomineeGuardianCorresState.setEnabled(true);
                etNomineeGuardianCorresStateError.setVisibility(View.VISIBLE);
                etNomineeGuardianCorresStateError.setText("Select State");
                etNomineeGuardianCorresStateError.requestFocus();
                return;
            } else if (adultFlag && (!(getInterestedInMF() && cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) && (etNomineeGuardianCorresState.getVisibility() == View.VISIBLE) && etNomineeGuardianCorresState.getText().toString().trim().equals(""))) {
                etNomineeGuardianCorresState.setEnabled(true);
                etNomineeGuardianCorresStateError.setVisibility(View.VISIBLE);
                etNomineeGuardianCorresStateError.setText("Select State");
                etNomineeGuardianCorresStateError.requestFocus();
                return;
            } else if (adultFlag && (spNomineeGuardianCorresCity.getVisibility() == View.VISIBLE) && spNomineeGuardianCorresCity.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                spNomineeGuardianCorresState.setEnabled(true);
                spNomineeGuardianCorresCity.setEnabled(true);
                etNomineeGuardianCorresCityError.setVisibility(View.VISIBLE);
                etNomineeGuardianCorresCityError.setText("Select City");
                etNomineeGuardianCorresCityError.requestFocus();
                return;
            } else if (adultFlag && (!(getInterestedInMF() && cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) && (etNomineeGuardianCorresCity.getVisibility() == View.VISIBLE) && etNomineeGuardianCorresCity.getText().toString().trim().equals(""))) {
                etNomineeGuardianCorresCity.setEnabled(true);
                etNomineeGuardianCorresCityError.setVisibility(View.VISIBLE);
                etNomineeGuardianCorresCityError.setText("Select City");
                etNomineeGuardianCorresCityError.requestFocus();
                return;
            } else if (adultFlag && (!(getInterestedInMF() && cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) && spNomineeGuardianCorresCountry.getSelectedItem().toString().equalsIgnoreCase("Select"))) {
                spNomineeGuardianCorresCountry.setEnabled(true);
                etNomineeGuardianCorresCountryError.setVisibility(View.VISIBLE);
                etNomineeGuardianCorresCountryError.setText("Select Country");
                etNomineeGuardianCorresCountryError.requestFocus();
                return;
            } else if (adultFlag && spNomineeGuardianAge.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                etNomineeGuardianAgeError.setVisibility(View.VISIBLE);
                etNomineeGuardianAgeError.setText("Select Age");
                etNomineeGuardianAgeError.requestFocus();
                return;
            } else if ((etNomineeGuardianSTDNo.getText().toString().trim().length() > 0 || etNomineeGuardianTelNo.getText().toString().trim().length() > 0) && (!ValidationRule.validateTel(etNomineeGuardianSTDNo.getText().toString() + etNomineeGuardianTelNo.getText().toString()))) {
                etNomineeGuardianSTDTelError.setVisibility(View.VISIBLE);
                etNomineeGuardianSTDTelError.setText(Res_Telephone_number_min_11_digit);
                etNomineeGuardianSTDNo.requestFocus();
                return;
            }
            /*else if (adultFlag && !validateEditText(etNomineeGuardianEmail, inputLayoutNomineeGuardianEmail, "Enter Email Id")) {
                return;
            }*/
            else if (adultFlag && etNomineeGuardianEmail.getText().toString().trim().length() > 0 && !validateEditText(etNomineeGuardianEmail, inputLayoutNomineeGuardianEmail, "Enter Email Id")) {
                return;
            } else if (adultFlag && etNomineeGuardianEmail.getText().toString().trim().length() > 0 && !isValidEmail(etNomineeGuardianEmail.getText().toString())) {
                etNomineeGuardianEmail.requestFocus();
                return;
            }


           /* else if (adultFlag && !isValidEmail(etNomineeGuardianEmail.getText().toString())) {
                etNomineeGuardianEmail.requestFocus();
                return;
            } */
            else {
                if (getAppType().equalsIgnoreCase(DIRECT) && !getIsAadharVerified() && !getInterestedInMF()) {
                    //Update both nominee and joint for Direct without aadhaar and not MF
                    if (toggleNominee.isChecked() && toggleJointHolder.isChecked()) {//Update both nominee and joint
                        if (toggleJointHolder.isChecked() && spNumberOfJointHolder.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            etNumberOfJointHolderError.setText("Select number of joint holder");
                            etNumberOfJointHolderError.setVisibility(View.VISIBLE);
                            etNumberOfJointHolderError.requestFocus();
                        } else {
                            if (rlSecondJointHolder.getVisibility() == View.VISIBLE) {
                                setFirstHolderData();
                                validateFirstJointHolderData();
                            } else if (rlThirdJointHolder.getVisibility() == View.VISIBLE) {
                                setSecondHolderData();
                                validateSecondJointHolderData();
                            }

                            if (spNumberOfJointHolder.getSelectedItem().toString().equalsIgnoreCase("1")) {
                                if (firstUpdate) {
                                    if ((adultFlag && !guardianName.equals("")) && nomineeName.equalsIgnoreCase(guardianName)) {
                                        //guardian name same as nominee name
                                        commonMsgDialog(mContext, Nominee_guardian_name_cannot_be_same_as_Nominee_name_Please_enter_unique_details, false, "");
                                    } else if (!nomineeName.equals("") && nomineeName.equalsIgnoreCase(secondJointHolderName)) {
                                        //  nominee name same as second holder name
                                        commonMsgDialog(mContext, Second_Joint_Holder_name_cannot_be_same_as_Nominee_name_Please_enter_unique_details, false, "");
                                    } else if ((adultFlag && !guardianName.equals("")) && secondJointHolderName.equals(guardianName)) {
                                        //guardian name same as second holder name
                                        commonMsgDialog(mContext, Second_Joint_Holder_name_cannot_be_same_as_Nominee_guardian_name_Please_enter_unique_details, false, "");
                                    } else if (getPANno().equalsIgnoreCase(etNomineePAN.getText().toString().trim())) {
                                        //applicant pan same as nominee pan
                                        commonMsgDialog(mContext, Nominee_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(getAadharNo()) && getAadharNo().equals(etNomineeAadhar.getText().toString().trim())) {
                                        //applicant aadhar same as nominee aadhar
                                        commonMsgDialog(mContext, Nominee_Aadhaar_number_cannot_be_same_as_Primary_applicant_Aadhaar_number_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(getAadharNo()) && etSJHAadhar.getText().toString().trim().equals(getAadharNo())) {
                                        //applicant aadhar same as second holder aadhar
                                        commonMsgDialog(mContext, Second_Joint_Holder_Aadhaar_Number_cannot_be_same_as_Primary_applicant_Aadhaar_Number_Please_enter_unique_details, false, "");
                                    } else if (etSJHPAN.getText().toString().trim().equalsIgnoreCase(getPANno())) {
                                        //second holder pan same as applicant pan
                                        commonMsgDialog(mContext, Second_Joint_Holder_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                                    } else if (etSJHPAN.getText().toString().trim().equalsIgnoreCase(etNomineePAN.getText().toString().trim())) {
                                        //second holder pan same as nominee pan
                                        commonMsgDialog(mContext, Second_Joint_Holder_PAN_cannot_be_same_as_Nominee_PAN_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(etSJHAadhar.getText().toString()) && etSJHAadhar.getText().toString().trim().equals(etNomineeAadhar.getText().toString().trim())) {
                                        //second holder aadhar same as nominee aadhar
                                        commonMsgDialog(mContext, Second_Joint_Holder_Aadhaar_Number_cannot_be_same_as_Nominee_Aadhaar_Number_Please_enter_unique_details, false, "");
                                    } else if ((etNomineeBldgNameCorres.isEnabled() || etNomineeFlatNoCorres.isEnabled()) && etNomineeBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeFlatNoCorres.getText().toString().trim())) {
                                        //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etNomineeBldgNameCorres.isEnabled() || etNomineeAreaCorres.isEnabled()) && etNomineeBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeAreaCorres.getText().toString().trim())) {
                                        //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etNomineeFlatNoCorres.isEnabled() || etNomineeAreaCorres.isEnabled()) && etNomineeFlatNoCorres.getText().toString().trim().equalsIgnoreCase(etNomineeAreaCorres.getText().toString().trim())) {
                                        //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((adultFlag && (etNomineeGuardianBldgNameCorres.isEnabled() || etNomineeGuardianFlatNoCorres.isEnabled()))
                                            && etNomineeGuardianBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianFlatNoCorres.getText().toString().trim())) {
                                        //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((adultFlag && (etNomineeGuardianBldgNameCorres.isEnabled() || etNomineeGuardianAreaCorres.isEnabled()))
                                            && etNomineeGuardianBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianAreaCorres.getText().toString().trim())) {
                                        //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((adultFlag && (etNomineeGuardianFlatNoCorres.isEnabled() || etNomineeGuardianAreaCorres.isEnabled()))
                                            && etNomineeGuardianFlatNoCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianAreaCorres.getText().toString().trim())) {
                                        //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHFlatNoCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd2())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNoCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd2().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHFlatNamePerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd2Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd2Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else {
                                        updateNomineeDetails();
                                        updateDematDetails();
                                    }
                                }
                            } else if (spNumberOfJointHolder.getSelectedItem().toString().equalsIgnoreCase("2")) {
                                if (firstUpdate && secondUpdate) {
                                    if ((adultFlag && !guardianName.equals("")) && nomineeName.equalsIgnoreCase(guardianName)) {
                                        //guardian name same as nominee name
                                        commonMsgDialog(mContext, Nominee_guardian_name_cannot_be_same_as_Nominee_name_Please_enter_unique_details, false, "");
                                    } else if ((adultFlag && !guardianName.equals("")) && secondJointHolderName.equalsIgnoreCase(guardianName)) {
                                        //guardian name same as second holder name
                                        commonMsgDialog(mContext, Second_Joint_Holder_name_cannot_be_same_as_Nominee_guardian_name_Please_enter_unique_details, false, "");
                                    } else if ((adultFlag && !guardianName.equals("")) && thirdJointHolderName.equalsIgnoreCase(guardianName)) {
                                        //guardian name same as third holder name
                                        commonMsgDialog(mContext, Third_Joint_Holder_name_cannot_be_same_as_Nominee_guardian_name_Please_enter_unique_details, false, "");
                                    } else if (!nomineeName.equals("") && nomineeName.equalsIgnoreCase(secondJointHolderName)) {
                                        //  nominee name same as second holder name
                                        commonMsgDialog(mContext, Second_Joint_Holder_name_cannot_be_same_as_Nominee_name_Please_enter_unique_details, false, "");
                                    } else if (!nomineeName.equals("") && nomineeName.equalsIgnoreCase(thirdJointHolderName)) {
                                        //  nominee name same as third holder name
                                        commonMsgDialog(mContext, Third_Joint_Holder_name_cannot_be_same_as_Nominee_name_Please_enter_unique_details, false, "");
                                    } else if (!secondJointHolderName.equals("") && secondJointHolderName.equalsIgnoreCase(thirdJointHolderName)) {
                                        //  second name same as third holder name
                                        commonMsgDialog(mContext, Second_Joint_Holder_name_cannot_be_same_as_Third_Joint_holder_name_Please_enter_unique_details, false, "");
                                    } else if (getPANno().equalsIgnoreCase(etNomineePAN.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Nominee_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(getAadharNo()) && getAadharNo().equals(etNomineeAadhar.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Nominee_Aadhaar_number_cannot_be_same_as_Primary_applicant_Aadhaar_number_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(getAadharNo()) && etSJHAadhar.getText().toString().trim().equals(getAadharNo())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_Aadhaar_Number_cannot_be_same_as_Primary_applicant_Aadhaar_Number_Please_enter_unique_details, false, "");
                                    } else if (etSJHPAN.getText().toString().trim().equalsIgnoreCase(getPANno())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                                    } else if (getPANno().equalsIgnoreCase(etTJHPAN.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Third_Joint_Holder_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(etTJHPAN.getText().toString()) && etTJHPAN.getText().toString().trim().equals(etNomineePAN.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Third_Joint_Holder_PAN_cannot_be_same_as_Nominee_PAN_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(getAadharNo()) && getAadharNo().equals(etTJHAadhar.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Third_Joint_Holder_Aadhaar_number_cannot_be_same_as_primary_applicant_Aadhaar_number_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(etNomineeAadhar.getText().toString().trim()) && etNomineeAadhar.getText().toString().trim().equals(etTJHAadhar.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Third_Joint_Holder_Aadhaar_number_cannot_be_same_as_Nominee_Aadhaar_number_Please_enter_unique_details, false, "");
                                    } else if (etSJHPAN.getText().toString().trim().equalsIgnoreCase(etNomineePAN.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_PAN_cannot_be_same_as_Nominee_PAN_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(etSJHAadhar.getText().toString()) && etSJHAadhar.getText().toString().trim().equals(etNomineeAadhar.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_Aadhaar_Number_cannot_be_same_as_Nominee_Aadhaar_Number_Please_enter_unique_details, false, "");
                                    } else if (etSJHPAN.getText().toString().trim().equalsIgnoreCase(etTJHPAN.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_PAN_cannot_be_same_as_Third_Joint_holder_PAN_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(etSJHAadhar.getText().toString()) && etSJHAadhar.getText().toString().trim().equals(etTJHAadhar.getText().toString().trim())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_Aadhaar_Number_cannot_be_same_as_Third_Joint_holder_Aadhaar_Number_Please_enter_unique_details, false, "");
                                    } else if ((etNomineeBldgNameCorres.isEnabled() || etNomineeFlatNoCorres.isEnabled()) && etNomineeBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeFlatNoCorres.getText().toString().trim())) {
                                        //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etNomineeBldgNameCorres.isEnabled() || etNomineeAreaCorres.isEnabled()) && etNomineeBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeAreaCorres.getText().toString().trim())) {
                                        //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etNomineeFlatNoCorres.isEnabled() || etNomineeAreaCorres.isEnabled()) && etNomineeFlatNoCorres.getText().toString().trim().equalsIgnoreCase(etNomineeAreaCorres.getText().toString().trim())) {
                                        //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((adultFlag && (etNomineeGuardianBldgNameCorres.isEnabled() || etNomineeGuardianFlatNoCorres.isEnabled()))
                                            && etNomineeGuardianBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianFlatNoCorres.getText().toString().trim())) {
                                        //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((adultFlag && (etNomineeGuardianBldgNameCorres.isEnabled() || etNomineeGuardianAreaCorres.isEnabled()))
                                            && etNomineeGuardianBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianAreaCorres.getText().toString().trim())) {
                                        //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((adultFlag && (etNomineeGuardianFlatNoCorres.isEnabled() || etNomineeGuardianAreaCorres.isEnabled()))
                                            && etNomineeGuardianFlatNoCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianAreaCorres.getText().toString().trim())) {
                                        //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHFlatNoCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd2())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNoCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd2().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHFlatNamePerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd2Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd2Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNameCorres.isEnabled() || etTJHFlatNoCorres.isEnabled()) && getDematSecondJointHolderAdd1().equalsIgnoreCase(getDematSecondJointHolderAdd2())) {
                                        //Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNameCorres.isEnabled() || etTJHAreaCorres.isEnabled()) && getDematSecondJointHolderAdd1().equalsIgnoreCase(getDematSecondJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHFlatNoCorres.isEnabled() || etTJHAreaCorres.isEnabled()) && getDematSecondJointHolderAdd2().equalsIgnoreCase(getDematSecondJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNamePerm.isEnabled() || etTJHFlatNamePerm.isEnabled()) && getDematSecondJointHolderAdd1Perm().equalsIgnoreCase(getDematSecondJointHolderAdd2Perm())) {
                                        //Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNamePerm.isEnabled() || etTJHAreaPerm.isEnabled()) && getDematSecondJointHolderAdd1Perm().equalsIgnoreCase(getDematSecondJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHFlatNamePerm.isEnabled() || etTJHAreaPerm.isEnabled()) && getDematSecondJointHolderAdd2Perm().equalsIgnoreCase(getDematSecondJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else {
                                        updateNomineeDetails();
                                        updateDematDetails();
                                    }
                                }
                            }
                        }
                    } else if (toggleNominee.isChecked() && !toggleJointHolder.isChecked()) {//Update nominee
                        if ((adultFlag && !guardianName.equals("")) && nomineeName.equalsIgnoreCase(guardianName)) {
                            //guardian name same as nominee name
                            commonMsgDialog(mContext, Nominee_guardian_name_cannot_be_same_as_Nominee_name_Please_enter_unique_details, false, "");
                        } else if (getPANno().equalsIgnoreCase(etNomineePAN.getText().toString().trim())) {
                            commonMsgDialog(mContext, Nominee_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                        } else if (!isBlank(getAadharNo()) && getAadharNo().equals(etNomineeAadhar.getText().toString().trim())) {
                            commonMsgDialog(mContext, Nominee_Aadhaar_number_cannot_be_same_as_Primary_applicant_Aadhaar_number_Please_enter_unique_details, false, "");
                        } else if ((etNomineeBldgNameCorres.isEnabled() || etNomineeFlatNoCorres.isEnabled()) && etNomineeBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeFlatNoCorres.getText().toString().trim())) {
                            //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else if ((etNomineeBldgNameCorres.isEnabled() || etNomineeAreaCorres.isEnabled()) && etNomineeBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeAreaCorres.getText().toString().trim())) {
                            //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else if ((etNomineeFlatNoCorres.isEnabled() || etNomineeAreaCorres.isEnabled()) && etNomineeFlatNoCorres.getText().toString().trim().equalsIgnoreCase(etNomineeAreaCorres.getText().toString().trim())) {
                            //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else if ((adultFlag && (etNomineeGuardianBldgNameCorres.isEnabled() || etNomineeGuardianFlatNoCorres.isEnabled()))
                                && etNomineeGuardianBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianFlatNoCorres.getText().toString().trim())) {
                            //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else if ((adultFlag && (etNomineeGuardianBldgNameCorres.isEnabled() || etNomineeGuardianAreaCorres.isEnabled()))
                                && etNomineeGuardianBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianAreaCorres.getText().toString().trim())) {
                            //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else if ((adultFlag && (etNomineeGuardianFlatNoCorres.isEnabled() || etNomineeGuardianAreaCorres.isEnabled()))
                                && etNomineeGuardianFlatNoCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianAreaCorres.getText().toString().trim())) {
                            //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else {
                            updateNomineeDetails();
                            updateDematDetails();
                        }
                    } else if (toggleJointHolder.isChecked() && !toggleNominee.isChecked()) {//Update joint holder
                        if (toggleJointHolder.isChecked() && spNumberOfJointHolder.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                            etNumberOfJointHolderError.setText("Select number of joint holder");
                            etNumberOfJointHolderError.setVisibility(View.VISIBLE);
                            etNumberOfJointHolderError.requestFocus();
                        } else {
                            if (rlSecondJointHolder.getVisibility() == View.VISIBLE) {
                                setFirstHolderData();
                                validateFirstJointHolderData();
                            } else if (rlThirdJointHolder.getVisibility() == View.VISIBLE) {
                                setSecondHolderData();
                                validateSecondJointHolderData();
                            }

                            if (spNumberOfJointHolder.getSelectedItem().toString().equalsIgnoreCase("1")) {
                                if (firstUpdate) {
                                    if (!isBlank(getAadharNo()) && etSJHAadhar.getText().toString().trim().equals(getAadharNo())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_Aadhaar_Number_cannot_be_same_as_Primary_applicant_Aadhaar_Number_Please_enter_unique_details, false, "");
                                    } else if (etSJHPAN.getText().toString().trim().equalsIgnoreCase(getPANno())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHFlatNoCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd2())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNoCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd2().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHFlatNamePerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd2Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd2Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else {
                                        updateNomineeDetails();
                                        updateDematDetails();
                                    }
                                }
                            } else if (spNumberOfJointHolder.getSelectedItem().toString().equalsIgnoreCase("2")) {
                                if (firstUpdate && secondUpdate) {
                                    if (!secondJointHolderName.equals("") && secondJointHolderName.equalsIgnoreCase(thirdJointHolderName)) {
                                        //  second name same as third holder name
                                        commonMsgDialog(mContext, Second_Joint_Holder_name_cannot_be_same_as_Third_Joint_holder_name_Please_enter_unique_details, false, "");
                                    } else if (!isBlank(getAadharNo()) && etSJHAadhar.getText().toString().trim().equals(getAadharNo())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_Aadhaar_Number_cannot_be_same_as_Primary_applicant_Aadhaar_Number_Please_enter_unique_details, false, "");
                                    } else if (etSJHPAN.getText().toString().trim().equalsIgnoreCase(getPANno())) {
                                        commonMsgDialog(mContext, Second_Joint_Holder_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHFlatNoCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd2())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNameCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd1().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNoCorres.isEnabled() || etSJHAreaCorres.isEnabled()) && getDematFirstJointHolderAdd2().equalsIgnoreCase(getDematFirstJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHFlatNamePerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd2Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHBldgNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd1Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etSJHFlatNamePerm.isEnabled() || etSJHAreaPerm.isEnabled()) && getDematFirstJointHolderAdd2Perm().equalsIgnoreCase(getDematFirstJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Second_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNameCorres.isEnabled() || etTJHFlatNoCorres.isEnabled()) && getDematSecondJointHolderAdd1().equalsIgnoreCase(getDematSecondJointHolderAdd2())) {
                                        //Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNameCorres.isEnabled() || etTJHAreaCorres.isEnabled()) && getDematSecondJointHolderAdd1().equalsIgnoreCase(getDematSecondJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHFlatNoCorres.isEnabled() || etTJHAreaCorres.isEnabled()) && getDematSecondJointHolderAdd2().equalsIgnoreCase(getDematSecondJointHolderAdd3())) {
                                        //Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Correspondence_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNamePerm.isEnabled() || etTJHFlatNamePerm.isEnabled()) && getDematSecondJointHolderAdd1Perm().equalsIgnoreCase(getDematSecondJointHolderAdd2Perm())) {
                                        //Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHBldgNamePerm.isEnabled() || etTJHAreaPerm.isEnabled()) && getDematSecondJointHolderAdd1Perm().equalsIgnoreCase(getDematSecondJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else if ((etTJHFlatNamePerm.isEnabled() || etTJHAreaPerm.isEnabled()) && getDematSecondJointHolderAdd2Perm().equalsIgnoreCase(getDematSecondJointHolderAdd3Perm())) {
                                        //Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field
                                        commonMsgDialog(mContext, Permanent_address_fields_for_Third_Joint_holder_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                                    } else {
                                        updateNomineeDetails();
                                        updateDematDetails();
                                    }
                                }
                            }
                        }
                    } else if (!toggleNominee.isChecked() && !toggleJointHolder.isChecked()) {//Update nominee for no

                        if ((adultFlag && !guardianName.equals("")) && nomineeName.equalsIgnoreCase(guardianName)) {
                            //guardian name same as nominee name
                            commonMsgDialog(mContext, Nominee_guardian_name_cannot_be_same_as_Nominee_name_Please_enter_unique_details, false, "");
                        } else if (getPANno().equalsIgnoreCase(etNomineePAN.getText().toString().trim())) {
                            commonMsgDialog(mContext, Nominee_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                        } else if (!isBlank(getAadharNo()) && getAadharNo().equals(etNomineeAadhar.getText().toString().trim())) {
                            commonMsgDialog(mContext, Nominee_Aadhaar_number_cannot_be_same_as_Primary_applicant_Aadhaar_number_Please_enter_unique_details, false, "");
                        } else if ((etNomineeBldgNameCorres.isEnabled() || etNomineeFlatNoCorres.isEnabled()) && etNomineeBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeFlatNoCorres.getText().toString().trim())) {
                            //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else if ((etNomineeBldgNameCorres.isEnabled() || etNomineeAreaCorres.isEnabled()) && etNomineeBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeAreaCorres.getText().toString().trim())) {
                            //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else if ((etNomineeFlatNoCorres.isEnabled() || etNomineeAreaCorres.isEnabled()) && etNomineeFlatNoCorres.getText().toString().trim().equalsIgnoreCase(etNomineeAreaCorres.getText().toString().trim())) {
                            //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else if ((adultFlag && (etNomineeGuardianBldgNameCorres.isEnabled() || etNomineeGuardianFlatNoCorres.isEnabled()))
                                && etNomineeGuardianBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianFlatNoCorres.getText().toString().trim())) {
                            //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else if ((adultFlag && (etNomineeGuardianBldgNameCorres.isEnabled() || etNomineeGuardianAreaCorres.isEnabled()))
                                && etNomineeGuardianBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianAreaCorres.getText().toString().trim())) {
                            //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else if ((adultFlag && (etNomineeGuardianFlatNoCorres.isEnabled() || etNomineeGuardianAreaCorres.isEnabled()))
                                && etNomineeGuardianFlatNoCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianAreaCorres.getText().toString().trim())) {
                            //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                            commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                        } else {
                            updateNomineeDetails();
                            updateDematDetails();
                        }
                    }
                } else {//Update Nominee for Branch and PP

                    if ((adultFlag && !guardianName.equals("")) && nomineeName.equalsIgnoreCase(guardianName)) {
                        //guardian name same as nominee name
                        commonMsgDialog(mContext, Nominee_guardian_name_cannot_be_same_as_Nominee_name_Please_enter_unique_details, false, "");
                    } else if (getPANno().equalsIgnoreCase(etNomineePAN.getText().toString().trim())) {
                        commonMsgDialog(mContext, Nominee_PAN_cannot_be_same_as_Primary_applicant_PAN_Please_enter_unique_details, false, "");
                    } else if (!isBlank(getAadharNo()) && getAadharNo().equals(etNomineeAadhar.getText().toString().trim())) {
                        commonMsgDialog(mContext, Nominee_Aadhaar_number_cannot_be_same_as_Primary_applicant_Aadhaar_number_Please_enter_unique_details, false, "");
                    } else if ((etNomineeBldgNameCorres.isEnabled() || etNomineeFlatNoCorres.isEnabled()) && etNomineeBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeFlatNoCorres.getText().toString().trim())) {
                        //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                        commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    } else if ((etNomineeBldgNameCorres.isEnabled() || etNomineeAreaCorres.isEnabled()) && etNomineeBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeAreaCorres.getText().toString().trim())) {
                        //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                        commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    } else if ((etNomineeFlatNoCorres.isEnabled() || etNomineeAreaCorres.isEnabled()) && etNomineeFlatNoCorres.getText().toString().trim().equalsIgnoreCase(etNomineeAreaCorres.getText().toString().trim())) {
                        //Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                        commonMsgDialog(mContext, Nominee_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    } else if ((adultFlag && (etNomineeGuardianBldgNameCorres.isEnabled() || etNomineeGuardianFlatNoCorres.isEnabled()))
                            && etNomineeGuardianBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianFlatNoCorres.getText().toString().trim())) {
                        //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                        commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    } else if ((adultFlag && (etNomineeGuardianBldgNameCorres.isEnabled() || etNomineeGuardianAreaCorres.isEnabled()))
                            && etNomineeGuardianBldgNameCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianAreaCorres.getText().toString().trim())) {
                        //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                        commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    } else if ((adultFlag && (etNomineeGuardianFlatNoCorres.isEnabled() || etNomineeGuardianAreaCorres.isEnabled()))
                            && etNomineeGuardianFlatNoCorres.getText().toString().trim().equalsIgnoreCase(etNomineeGuardianAreaCorres.getText().toString().trim())) {
                        //Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field
                        commonMsgDialog(mContext, Guardian_address_fields_cannot_be_same_Please_enter_unique_details_in_each_address_field, false, "");
                    } else {
                        updateNomineeDetails();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFirstHolderData() {
        try {
            inputLayoutSJHFirstName.setErrorEnabled(false);
            inputLayoutSJHMiddleName.setErrorEnabled(false);
            inputLayoutSJHLastName.setErrorEnabled(false);
            inputLayoutSJHFatherSpouseFirstName.setErrorEnabled(false);
            inputLayoutSJHFatherSpouseMidName.setErrorEnabled(false);
            inputLayoutSJHFatherSpouseLastName.setErrorEnabled(false);
            inputLayoutSJHDob.setErrorEnabled(false);
            inputLayoutSJHPAN.setErrorEnabled(false);
            inputLayoutSJHAadhar.setErrorEnabled(false);
            inputLayoutSJHMobile.setErrorEnabled(false);
            inputLayoutSJHEmail.setErrorEnabled(false);
            inputLayoutSJHSTD.setErrorEnabled(false);
            inputLayoutSJHTelNo.setErrorEnabled(false);
            inputLayoutSJHBldgNameCorres.setErrorEnabled(false);
            inputLayoutSJHFlatNoCorres.setErrorEnabled(false);
            inputLayoutSJHAreaCorres.setErrorEnabled(false);
            inputLayoutSJHCorresCityOther.setErrorEnabled(false);
            inputLayoutSJHPincodeCorres.setErrorEnabled(false);
            inputLayoutSJHBldgNamePerm.setErrorEnabled(false);
            inputLayoutSJHFlatNamePerm.setErrorEnabled(false);
            inputLayoutSJHAreaPerm.setErrorEnabled(false);
            inputLayoutSJHPermCityOther.setErrorEnabled(false);
            inputLayoutSJHPincodePerm.setErrorEnabled(false);

            setDematFirstJointHolderResidentialStatus(spSJHResidentStatus.getSelectedItem().toString());
            setDematAccTitle(spSJHTitle.getSelectedItem().toString());
            setDematFirstJointHolderFname(etSJHFirstName.getText().toString());
            setDematFirstJointHolderMname(etSJHMiddleName.getText().toString());
            setDematFirstJointHolderLname(etSJHLastName.getText().toString());
            if (rbSJHMale.isChecked()) {
                setDematFirstJointHolderSex("Male");
            } else if (rbSJHFemale.isChecked()) {
                setDematFirstJointHolderSex("Female");
            }
            if (rbSJHMarried.isChecked()) {
                setDematFirstJointHolderMaritalStatus("Married");
            } else if (rbSJHUnMarried.isChecked()) {
                setDematFirstJointHolderMaritalStatus("Single");
            }
            setDematFirstJointHolderRelation(spSJHFatherSpouseSel.getSelectedItem().toString());
            setDematFirstJointHolderFHFname(etSJHFatherSpouseFirstName.getText().toString());
            setDematFirstJointHolderFHMname(etSJHFatherSpouseMidName.getText().toString());
            setDematFirstJointHolderFHLname(etSJHFatherSpouseLastName.getText().toString());
            setDematFirstJointHolderAdd1(etSJHBldgNameCorres.getText().toString());
            setDematFirstJointHolderAdd2(etSJHFlatNoCorres.getText().toString());
            setDematFirstJointHolderAdd3(etSJHAreaCorres.getText().toString());
            setDematFirstJointHolderLandmark("");
            setDematFirstJointHolderPin(etSJHPincodeCorres.getText().toString());
            if (spSJHCorresState.getVisibility() == View.VISIBLE) {
                setDematFirstJointHolderState(spSJHCorresState.getSelectedItem().toString());
            } else if (etSJHCorresState.getVisibility() == View.VISIBLE) {
                setDematFirstJointHolderState(etSJHCorresState.getText().toString());
            }
            if (spSJHCorresCity.getVisibility() == View.VISIBLE) {
                setDematFirstJointHolderCity(spSJHCorresCity.getSelectedItem().toString());
            } else if (etSJHCorresCity.getVisibility() == View.VISIBLE) {
                setDematFirstJointHolderCity(etSJHCorresCity.getText().toString());
            }
            setDematFirstJointHolderCountry(spSJHCorresCountry.getSelectedItem().toString());
            setDematFirstJointHolderAdd1Perm(etSJHBldgNamePerm.getText().toString());
            setDematFirstJointHolderAdd2Perm(etSJHFlatNamePerm.getText().toString());
            setDematFirstJointHolderAdd3Perm(etSJHAreaPerm.getText().toString());
            setDematFirstJointHolderLandmarkPerm("");
            setDematFirstJointHolderPinPerm(etSJHPincodePerm.getText().toString());
            if (spSJHPermCity.getVisibility() == View.VISIBLE) {
                setDematFirstJointHolderCityPerm(spSJHPermCity.getSelectedItem().toString());
            } else if (etSJHPermCity.getVisibility() == View.VISIBLE) {
                setDematFirstJointHolderCityPerm(etSJHPermCity.getText().toString());
            }
            if (spSJHPermState.getVisibility() == View.VISIBLE) {
                setDematFirstJointHolderStatePerm(spSJHPermState.getSelectedItem().toString());
            } else if (etSJHPermState.getVisibility() == View.VISIBLE) {
                setDematFirstJointHolderStatePerm(etSJHPermState.getText().toString());
            }
            setDematFirstJointHolderCountryPerm(spSJHPermCountry.getSelectedItem().toString());
            setDematFirstJointHolderAddressProof(spSJHAddProofCorres.getSelectedItem().toString());
            setDematFirstJointHolderPan(etSJHPAN.getText().toString().trim());
            setDematFirstJointHolderUID(etSJHAadhar.getText().toString().trim());
            if (!etSJHDOB.getText().toString().equals("")) {
                Date dob = EkycAppUtil.getDate(etSJHDOB.getText().toString(), "dd/MM/yyyy");
                setDematFirstJointHolderDOB(String.valueOf(EkycAppUtil.getDateStr(dob, "yyyy-MM-dd")));
            }
            setDematFirstJointHolderOccupation(spSJHOccupation.getSelectedItem().toString());
            setDematFirstJointHolderNatureBus("");
            if (cbSJHSameAsPrimaryCorrespondence.isChecked()) {
                setDematFirstJointHolderSameAsPerm("1");
            } else {
                setDematFirstJointHolderSameAsPerm("0");
            }
            setDematFirstJointHolderAnnualIncome(spSJHIncome.getSelectedItem().toString());
            setDematFirstJointHolderAddressProofPerm(spSJHAddProofPerm.getSelectedItem().toString());
            setDematFirstJointHolderEmail(etSJHEmail.getText().toString());
            setDematFirstJointHolderMobile(etSJHMobile.getText().toString());
            if (!etSJHSTDNo.getText().toString().isEmpty()) {
                setDematFirstJointHolderTeleNo(etSJHSTDNo.getText().toString() + "~" + etSJHTelNo.getText().toString());
            } else {
                setDematFirstJointHolderTeleNo("");
            }
            if (toggleSJHPoliticaly.isChecked()) {
                setDematFirstJointHolderPEP("Yes");
            } else {
                setDematFirstJointHolderPEP("No");
            }
            if (toggleSJHRelatedPolitical.isChecked()) {
                setDematFirstJointHolderRPER("Yes");
            } else {
                setDematFirstJointHolderRPER("No");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSecondHolderData() {
        try {
            inputLayoutTJHFirstName.setErrorEnabled(false);
            inputLayoutTJHMiddleName.setErrorEnabled(false);
            inputLayoutTJHLastName.setErrorEnabled(false);
            inputLayoutTJHFatherSpouseFirstName.setErrorEnabled(false);
            inputLayoutTJHFatherSpouseMidName.setErrorEnabled(false);
            inputLayoutTJHFatherSpouseLastName.setErrorEnabled(false);
            inputLayoutTJHDob.setErrorEnabled(false);
            inputLayoutTJHPAN.setErrorEnabled(false);
            inputLayoutTJHAadhar.setErrorEnabled(false);
            inputLayoutTJHMobile.setErrorEnabled(false);
            inputLayoutTJHEmail.setErrorEnabled(false);
            inputLayoutTJHSTD.setErrorEnabled(false);
            inputLayoutTJHTelNo.setErrorEnabled(false);
            inputLayoutTJHBldgNameCorres.setErrorEnabled(false);
            inputLayoutTJHFlatNoCorres.setErrorEnabled(false);
            inputLayoutTJHAreaCorres.setErrorEnabled(false);
            inputLayoutTJHCorresCityOther.setErrorEnabled(false);
            inputLayoutTJHPincodeCorres.setErrorEnabled(false);
            inputLayoutTJHBldgNamePerm.setErrorEnabled(false);
            inputLayoutTJHFlatNamePerm.setErrorEnabled(false);
            inputLayoutTJHAreaPerm.setErrorEnabled(false);
            inputLayoutTJHPermCityOther.setErrorEnabled(false);
            inputLayoutTJHPincodePerm.setErrorEnabled(false);

            setDematSecondJointHolderResidentialStatus(spTJHResidentStatus.getSelectedItem().toString());
            setDematSecondAccTitle(spTJHTitle.getSelectedItem().toString());
            setDematSecondJointHolderFname(etTJHFirstName.getText().toString());
            setDematSecondJointHolderMname(etTJHMiddleName.getText().toString());
            setDematSecondJointHolderLname(etTJHLastName.getText().toString());
            if (rbTJHMale.isChecked()) {
                setDematSecondJointHolderSex("Male");
            } else if (rbTJHFemale.isChecked()) {
                setDematSecondJointHolderSex("Female");
            }
            if (rbTJHMarried.isChecked()) {
                setDematSecondJointHolderMaritalStatus("Married");
            } else if (rbTJHUnMarried.isChecked()) {
                setDematSecondJointHolderMaritalStatus("Single");
            }
            setDematSecondJointHolderRelation(spTJHFatherSpouseSel.getSelectedItem().toString());
            setDematSecondJointHolderFHFname(etTJHFatherSpouseFirstName.getText().toString());
            setDematSecondJointHolderFHMname(etTJHFatherSpouseMidName.getText().toString());
            setDematSecondJointHolderFHLname(etTJHFatherSpouseLastName.getText().toString());
            setDematSecondJointHolderAdd1(etTJHBldgNameCorres.getText().toString());
            setDematSecondJointHolderAdd2(etTJHFlatNoCorres.getText().toString());
            setDematSecondJointHolderAdd3(etTJHAreaCorres.getText().toString());
            setDematSecondJointHolderLandmark("");
            setDematSecondJointHolderPin(etTJHPincodeCorres.getText().toString());
            if (spTJHCorresCity.getVisibility() == View.VISIBLE) {
                setDematSecondJointHolderCity(spTJHCorresCity.getSelectedItem().toString());
            } else if (etTJHCorresCity.getVisibility() == View.VISIBLE) {
                setDematSecondJointHolderCity(etTJHCorresCity.getText().toString());
            }
            if (spTJHCorresState.getVisibility() == View.VISIBLE) {
                setDematSecondJointHolderState(spTJHCorresState.getSelectedItem().toString());
            } else if (etTJHCorresState.getVisibility() == View.VISIBLE) {
                setDematSecondJointHolderState(etTJHCorresState.getText().toString());
            }
            setDematSecondJointHolderCountry(spTJHCorresCountry.getSelectedItem().toString());
            setDematSecondJointHolderAdd1Perm(etTJHBldgNamePerm.getText().toString());
            setDematSecondJointHolderAdd2Perm(etTJHFlatNamePerm.getText().toString());
            setDematSecondJointHolderAdd3Perm(etTJHAreaPerm.getText().toString());
            setDematSecondJointHolderLandmarkPerm("");
            setDematSecondJointHolderPinPerm(etTJHPincodePerm.getText().toString());
            if (spTJHPermCity.getVisibility() == View.VISIBLE) {
                setDematSecondJointHolderCityPerm(spTJHPermCity.getSelectedItem().toString());
            } else if (etTJHPermCity.getVisibility() == View.VISIBLE) {
                setDematSecondJointHolderCityPerm(etTJHPermCity.getText().toString());
            }
            if (spTJHPermState.getVisibility() == View.VISIBLE) {
                setDematSecondJointHolderStatePerm(spTJHPermState.getSelectedItem().toString());
            } else if (etTJHPermState.getVisibility() == View.VISIBLE) {
                setDematSecondJointHolderStatePerm(etTJHPermState.getText().toString());
            }
            setDematSecondJointHolderCountryPerm(spTJHPermCountry.getSelectedItem().toString());
            setDematSecondJointHolderAddressProof(spTJHAddProofCorres.getSelectedItem().toString());
            setDematSecondJointHolderPan(etTJHPAN.getText().toString().trim());
            setDematSecondJointHolderUID(etTJHAadhar.getText().toString().trim());
            if (!etTJHDOB.getText().toString().equals("")) {
                Date dob = EkycAppUtil.getDate(etTJHDOB.getText().toString(), "dd/MM/yyyy");
                setDematSecondJointHolderDOB(String.valueOf(EkycAppUtil.getDateStr(dob, "yyyy-MM-dd")));
            }
            setDematSecondJointHolderOccupation(spTJHOccupation.getSelectedItem().toString());
            setDematSecondJointHolderNatureBus("");
            if (cbTJHSameAsPrimaryCorrespondence.isChecked()) {
                setDematSecondJointHolderSameAsPerm("1");
            } else {
                setDematSecondJointHolderSameAsPerm("0");
            }
            setDematSecondJointHolderAnnualIncome(spTJHIncome.getSelectedItem().toString());
            setDematSecondJointHolderAddressProofPerm(spTJHAddProofPerm.getSelectedItem().toString());
            setDematSecondJointHolderEmail(etTJHEmail.getText().toString());
            setDematSecondJointHolderMobile(etTJHMobile.getText().toString());
            if (!etTJHSTDNo.getText().toString().isEmpty()) {
                setDematSecondJointHolderTeleNo(etTJHSTDNo.getText().toString() + "~" + etTJHTelNo.getText().toString());
            } else {
                setDematSecondJointHolderTeleNo("");
            }
            if (toggleTJHPoliticaly.isChecked()) {
                setDematSecondJointHolderPEP("Yes");
            } else {
                setDematSecondJointHolderPEP("No");
            }
            if (toggleTJHRelatedPolitical.isChecked()) {
                setDematSecondJointHolderRPER("Yes");
            } else {
                setDematSecondJointHolderRPER("No");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSpinnerData(ArrayAdapter<String> arrayAdapter, Spinner spinner, String string) {
        int spPosition = arrayAdapter.getPosition(string);
        spinner.setSelection(spPosition);
    }

    /**
     * this method will hide progress when received the response of BINDSTATE & Getdemat
     */
    private void hideProgress() {
        hideProgressCounter++;
        if (hideProgressCounter == 3) {
            hideProgressCounter = 0;
            customProgress.setVisibility(View.GONE);
        }
    }

    /**
     * this method will hide progress when update the response of Nominee & JointHolder
     */
    private void hideUpdateProgress() {
        hideUpdateProgressCounter++;
        if (getAppType().equalsIgnoreCase(DIRECT) && !getIsAadharVerified() && !getInterestedInMF()) {
            if (toggleNominee.isChecked() && toggleJointHolder.isChecked()) {
                if (hideUpdateProgressCounter == 2) {
                    hideUpdateProgressCounter = 0;
                    customProgress.setVisibility(View.GONE);
                }
            } else {
                if (hideUpdateProgressCounter == 2) {
                    hideUpdateProgressCounter = 0;
                    customProgress.setVisibility(View.GONE);
                }
            }
        } else {
            if (hideUpdateProgressCounter == 1) {
                hideUpdateProgressCounter = 0;
                customProgress.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && jointHolderNomineeView != null && viewPager.getCurrentItem() == getDematTab()) {
                setPermisson(getActivity(), mContext);
                disableEnableField();
                loadJointNomineeDetails();
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    private boolean validateSpinner(Spinner spinner, TextView editText, String error) {
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            editText.setVisibility(View.VISIBLE);
            editText.setText(error);
            editText.requestFocus();
            return false;
        } else {
            editText.setVisibility(View.GONE);
        }
        return true;
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

    private void showErrorMsg(boolean isError, EditText editText, TextInputLayout textInputLayout, String error) {
        if (isError) {
            textInputLayout.setError(error);
            editText.requestFocus();
        } else {
            textInputLayout.setErrorEnabled(false);
        }
    }

    private boolean isJointHolderAddressValidate(CheckBox cb, EditText editText) {
        if (!(getInterestedInMF() && cb.isChecked()) && editText.isEnabled() && !editText.getText().toString().equals("") && !ValidationRule.validateAddress(editText.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

    //Pancard number Format
    private boolean isValidPannumber(String pan) {

        String PAN_PATTEN = ("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Pattern pattern = Pattern.compile(PAN_PATTEN);
        Matcher matcher = pattern.matcher(pan);
        return matcher.matches();
    }

    private static void clearJointHolderModelData() {
        setDematAccType("");
        setDematAccTitle("");
        setDematFirstJointHolderFname("");
        setDematFirstJointHolderMname("");
        setDematFirstJointHolderLname("");
        setDematFirstJointHolderRelation("");
        setDematFirstJointHolderFHFname("");
        setDematFirstJointHolderFHMname("");
        setDematFirstJointHolderFHLname("");
        setDematFirstJointHolderAdd1("");
        setDematFirstJointHolderAdd2("");
        setDematFirstJointHolderAdd3("");
        setDematFirstJointHolderLandmark("");
        setDematFirstJointHolderPin("");
        setDematFirstJointHolderCity("");
        setDematFirstJointHolderState("");
        setDematFirstJointHolderCountry("");
        setDematFirstJointHolderPan("");
        setDematFirstJointHolderSex("");
        setDematFirstJointHolderDOB("");
        setDematFirstJointHolderOccupation("");
        setDematFirstJointHolderNatureBus("");
        setDematFirstJointHolderMaritalStatus("");
        setDematFirstJointHolderResidentialStatus("");
        setDematFirstJointHolderUID("");
        setDematFirstJointHolderEmail("");
        setDematFirstJointHolderTeleNo("");
        setDematFirstJointHolderAddressProof("");
        setDematFirstJointHolderAddressProofPerm("");
        setDematFirstJointHolderAnnualIncome("");
        setDematFirstJointHolderPEP("");
        setDematFirstJointHolderRPER("");
        setDematFirstJointHolderSameAsPerm("");
        setDematFirstJointHolderMobile("");
        setDematSecondAccTitle("");
        setDematSecondJointHolderFname("");
        setDematSecondJointHolderMname("");
        setDematSecondJointHolderLname("");
        setDematSecondJointHolderRelation("");
        setDematSecondJointHolderFHFname("");
        setDematSecondJointHolderFHMname("");
        setDematSecondJointHolderFHLname("");
        setDematSecondJointHolderAdd1("");
        setDematSecondJointHolderAdd2("");
        setDematSecondJointHolderAdd3("");
        setDematSecondJointHolderLandmark("");
        setDematSecondJointHolderPin("");
        setDematSecondJointHolderCity("");
        setDematSecondJointHolderState("");
        setDematSecondJointHolderCountry("");
        setDematSecondJointHolderPan("");
        setDematSecondJointHolderSex("");
        setDematSecondJointHolderDOB("");
        setDematSecondJointHolderOccupation("");
        setDematSecondJointHolderNatureBus("");
        setDematSecondJointHolderMaritalStatus("");
        setDematSecondJointHolderResidentialStatus("");
        setDematSecondJointHolderUID("");
        setDematSecondJointHolderEmail("");
        setDematSecondJointHolderTeleNo("");
        setDematSecondJointHolderAddressProof("");
        setDematSecondJointHolderAddressProofPerm("");
        setDematSecondJointHolderAnnualIncome("");
        setDematSecondJointHolderPEP("");
        setDematSecondJointHolderRPER("");
        setDematSecondJointHolderSameAsPerm("");
        setDematSecondJointHolderMobile("");
        setDematFirstJointHolderAdd1Perm("");
        setDematFirstJointHolderAdd2Perm("");
        setDematFirstJointHolderAdd3Perm("");
        setDematFirstJointHolderLandmarkPerm("");
        setDematFirstJointHolderPinPerm("");
        setDematFirstJointHolderCityPerm("");
        setDematFirstJointHolderStatePerm("");
        setDematFirstJointHolderCountryPerm("");
        setDematSecondJointHolderAdd1Perm("");
        setDematSecondJointHolderAdd2Perm("");
        setDematSecondJointHolderAdd3Perm("");
        setDematSecondJointHolderLandmarkPerm("");
        setDematSecondJointHolderPinPerm("");
        setDematSecondJointHolderCityPerm("");
        setDematSecondJointHolderStatePerm("");
        setDematSecondJointHolderCountryPerm("");
        setFjOtherCityDematFirstHolderPerm("");
        setSjOtherCityDematFirstHolderPerm("");
    }

    private void clearJointHolderFields() {
        try {
//            firstUpdate = false;
//            secondUpdate = false;
            jointHolderUpdate = false;
            toggleJointHolder.setChecked(false);
            llNumberOfJointHolder.setVisibility(View.GONE);
//            llJointHolderDetail.setVisibility(View.GONE);
//            rlSecondJointHolder.setVisibility(View.GONE);
//            rlThirdJointHolder.setVisibility(View.GONE);
            inputLayoutSJHFirstName.setErrorEnabled(false);
            inputLayoutSJHMiddleName.setErrorEnabled(false);
            inputLayoutSJHLastName.setErrorEnabled(false);
            inputLayoutSJHFatherSpouseFirstName.setErrorEnabled(false);
            inputLayoutSJHFatherSpouseMidName.setErrorEnabled(false);
            inputLayoutSJHFatherSpouseLastName.setErrorEnabled(false);
            inputLayoutSJHDob.setErrorEnabled(false);
            inputLayoutSJHPAN.setErrorEnabled(false);
            inputLayoutSJHAadhar.setErrorEnabled(false);
            inputLayoutSJHMobile.setErrorEnabled(false);
            inputLayoutSJHEmail.setErrorEnabled(false);
            inputLayoutSJHSTD.setErrorEnabled(false);
            inputLayoutSJHTelNo.setErrorEnabled(false);
            inputLayoutSJHBldgNameCorres.setErrorEnabled(false);
            inputLayoutSJHFlatNoCorres.setErrorEnabled(false);
            inputLayoutSJHAreaCorres.setErrorEnabled(false);
            inputLayoutSJHCorresCityOther.setErrorEnabled(false);
            inputLayoutSJHPincodeCorres.setErrorEnabled(false);
            inputLayoutSJHBldgNamePerm.setErrorEnabled(false);
            inputLayoutSJHFlatNamePerm.setErrorEnabled(false);
            inputLayoutSJHAreaPerm.setErrorEnabled(false);
            inputLayoutSJHPermCityOther.setErrorEnabled(false);
            inputLayoutSJHPincodePerm.setErrorEnabled(false);
            etSJHResidentStatusError.setVisibility(View.GONE);
            etSJHTitleError.setVisibility(View.GONE);
            etSJHGenderError.setVisibility(View.GONE);
            etSJHFatherSpouseSelError.setVisibility(View.GONE);
            etSJHMaritalError.setVisibility(View.GONE);
            etSJHAddProofCorresError.setVisibility(View.GONE);
            etSJHAddProofPermError.setVisibility(View.GONE);
            etSJHCorresStateError.setVisibility(View.GONE);
            etSJHCorresCityError.setVisibility(View.GONE);
            etSJHCorresCountryError.setVisibility(View.GONE);
            etSJHPermStateError.setVisibility(View.GONE);
            etSJHPermCityError.setVisibility(View.GONE);
            etSJHPermCountryError.setVisibility(View.GONE);
            etSJHIncomeError.setVisibility(View.GONE);
            etSJHOccupationError.setVisibility(View.GONE);
            etSJHSTDTelError.setVisibility(View.GONE);

            spSJHResidentStatus.setSelection(0);
            spSJHTitle.setSelection(0);
            etSJHFirstName.setText("");
            etSJHMiddleName.setText("");
            etSJHLastName.setText("");
            rbSJHMale.setChecked(false);
            rbSJHFemale.setChecked(false);
            rbSJHMarried.setChecked(false);
            rbSJHUnMarried.setChecked(false);
            spSJHFatherSpouseSel.setSelection(0);
            etSJHFatherSpouseFirstName.setText("");
            etSJHFatherSpouseMidName.setText("");
            etSJHFatherSpouseLastName.setText("");
            cbSJHSameAsPrimaryCorrespondence.setChecked(false);
            etSJHBldgNameCorres.setText("");
            etSJHFlatNoCorres.setText("");
            etSJHAreaCorres.setText("");
            etSJHPincodeCorres.setText("");
            spSJHCorresState.setSelection(0);
            etSJHCorresState.setText("");
            spSJHCorresCity.setSelection(0);
            etSJHCorresCity.setText("");
            spSJHCorresCountry.setSelection(0);
            spSJHAddProofCorres.setSelection(0);
            cbSJHSameAsCorrespondence.setChecked(false);
            etSJHBldgNamePerm.setText("");
            etSJHFlatNamePerm.setText("");
            etSJHAreaPerm.setText("");
            etSJHPincodePerm.setText("");
            spSJHPermState.setSelection(0);
            etSJHPermState.setText("");
            spSJHPermCity.setSelection(0);
            etSJHPermCity.setText("");
            spSJHPermCountry.setSelection(0);
            spSJHAddProofPerm.setSelection(0);
            etSJHPAN.setText("");
            etSJHAadhar.setText("");
            etSJHDOB.setText("");
            spSJHOccupation.setSelection(0);
            spSJHIncome.setSelection(0);
            etSJHEmail.setText("");
            etSJHMobile.setText("");
            etSJHSTDNo.setText("");
            etSJHTelNo.setText("");
            toggleSJHPoliticaly.setChecked(false);
            toggleSJHRelatedPolitical.setChecked(false);

            spSJHCorresState.setVisibility(View.VISIBLE);
            etSJHCorresState.setVisibility(View.GONE);
            spSJHCorresCity.setVisibility(View.VISIBLE);
            etSJHCorresCity.setVisibility(View.GONE);
            etSJHBldgNameCorres.setEnabled(true);
            etSJHFlatNoCorres.setEnabled(true);
            etSJHAreaCorres.setEnabled(true);
            etSJHPincodeCorres.setEnabled(true);
            spSJHCorresState.setEnabled(true);
            spSJHCorresCity.setEnabled(true);
            spSJHCorresCountry.setEnabled(true);
            spSJHAddProofCorres.setEnabled(true);

            spSJHPermState.setVisibility(View.VISIBLE);
            etSJHPermState.setVisibility(View.GONE);
            spSJHPermCity.setVisibility(View.VISIBLE);
            etSJHPermCity.setVisibility(View.GONE);
            etSJHBldgNamePerm.setEnabled(true);
            etSJHFlatNamePerm.setEnabled(true);
            etSJHAreaPerm.setEnabled(true);
            etSJHPincodePerm.setEnabled(true);
            spSJHPermState.setEnabled(true);
            spSJHPermCity.setEnabled(true);
            spSJHPermCountry.setEnabled(true);
            spSJHAddProofPerm.setEnabled(true);

            //Third holder data
            inputLayoutTJHFirstName.setErrorEnabled(false);
            inputLayoutTJHMiddleName.setErrorEnabled(false);
            inputLayoutTJHLastName.setErrorEnabled(false);
            inputLayoutTJHFatherSpouseFirstName.setErrorEnabled(false);
            inputLayoutTJHFatherSpouseMidName.setErrorEnabled(false);
            inputLayoutTJHFatherSpouseLastName.setErrorEnabled(false);
            inputLayoutTJHDob.setErrorEnabled(false);
            inputLayoutTJHPAN.setErrorEnabled(false);
            inputLayoutTJHAadhar.setErrorEnabled(false);
            inputLayoutTJHMobile.setErrorEnabled(false);
            inputLayoutTJHEmail.setErrorEnabled(false);
            inputLayoutTJHSTD.setErrorEnabled(false);
            inputLayoutTJHTelNo.setErrorEnabled(false);
            inputLayoutTJHBldgNameCorres.setErrorEnabled(false);
            inputLayoutTJHFlatNoCorres.setErrorEnabled(false);
            inputLayoutTJHAreaCorres.setErrorEnabled(false);
            inputLayoutTJHCorresCityOther.setErrorEnabled(false);
            inputLayoutTJHPincodeCorres.setErrorEnabled(false);
            inputLayoutTJHBldgNamePerm.setErrorEnabled(false);
            inputLayoutTJHFlatNamePerm.setErrorEnabled(false);
            inputLayoutTJHAreaPerm.setErrorEnabled(false);
            inputLayoutTJHPermCityOther.setErrorEnabled(false);
            inputLayoutTJHPincodePerm.setErrorEnabled(false);
            etTJHResidentStatusError.setVisibility(View.GONE);
            etTJHTitleError.setVisibility(View.GONE);
            etTJHGenderError.setVisibility(View.GONE);
            etTJHFatherSpouseSelError.setVisibility(View.GONE);
            etTJHmaritalError.setVisibility(View.GONE);
            etTJHAddProofCorresError.setVisibility(View.GONE);
            etTJHAddProofPermError.setVisibility(View.GONE);
            etTJHCorresStateError.setVisibility(View.GONE);
            etTJHCorresCityError.setVisibility(View.GONE);
            etTJHCorresCountryError.setVisibility(View.GONE);
            etTJHPermStateError.setVisibility(View.GONE);
            etTJHPermCityError.setVisibility(View.GONE);
            etTJHPermCountryError.setVisibility(View.GONE);
            etTJHIncomeError.setVisibility(View.GONE);
            etTJHOccupationError.setVisibility(View.GONE);
            etTJHSTDTelError.setVisibility(View.GONE);

            spTJHResidentStatus.setSelection(0);
            spTJHTitle.setSelection(0);
            etTJHFirstName.setText("");
            etTJHMiddleName.setText("");
            etTJHLastName.setText("");
            rbTJHMale.setChecked(false);
            rbTJHFemale.setChecked(false);
            rbTJHMarried.setChecked(false);
            rbTJHUnMarried.setChecked(false);
            spTJHFatherSpouseSel.setSelection(0);
            etTJHFatherSpouseFirstName.setText("");
            etTJHFatherSpouseMidName.setText("");
            etTJHFatherSpouseLastName.setText("");
            cbTJHSameAsPrimaryCorrespondence.setChecked(false);
            etTJHBldgNameCorres.setText("");
            etTJHFlatNoCorres.setText("");
            etTJHAreaCorres.setText("");
            etTJHPincodeCorres.setText("");
            spTJHCorresState.setSelection(0);
            etTJHCorresState.setText("");
            spTJHCorresCity.setSelection(0);
            etTJHCorresCity.setText("");
            spTJHCorresCountry.setSelection(0);
            spTJHAddProofCorres.setSelection(0);
            cbTJHSameAsCorrespondence.setChecked(false);
            etTJHBldgNamePerm.setText("");
            etTJHFlatNamePerm.setText("");
            etTJHAreaPerm.setText("");
            etTJHPincodePerm.setText("");
            spTJHPermState.setSelection(0);
            etTJHPermState.setText("");
            spTJHPermCity.setSelection(0);
            etTJHPermCity.setText("");
            spTJHPermCountry.setSelection(0);
            spTJHAddProofPerm.setSelection(0);
            etTJHPAN.setText("");
            etTJHAadhar.setText("");
            etTJHDOB.setText("");
            spTJHOccupation.setSelection(0);
            spTJHIncome.setSelection(0);
            etTJHEmail.setText("");
            etTJHMobile.setText("");
            etTJHSTDNo.setText("");
            etTJHTelNo.setText("");
            toggleTJHPoliticaly.setChecked(false);
            toggleTJHRelatedPolitical.setChecked(false);

            spTJHCorresState.setVisibility(View.VISIBLE);
            etTJHCorresState.setVisibility(View.GONE);
            spTJHCorresCity.setVisibility(View.VISIBLE);
            etTJHCorresCity.setVisibility(View.GONE);
            etTJHBldgNameCorres.setEnabled(true);
            etTJHFlatNoCorres.setEnabled(true);
            etTJHAreaCorres.setEnabled(true);
            etTJHPincodeCorres.setEnabled(true);
            spTJHCorresState.setEnabled(true);
            spTJHCorresCity.setEnabled(true);
            spTJHCorresCountry.setEnabled(true);
            spTJHAddProofCorres.setEnabled(true);

            spTJHPermState.setVisibility(View.VISIBLE);
            etTJHPermState.setVisibility(View.GONE);
            spTJHPermCity.setVisibility(View.VISIBLE);
            etTJHPermCity.setVisibility(View.GONE);
            etTJHBldgNamePerm.setEnabled(true);
            etTJHFlatNamePerm.setEnabled(true);
            etTJHAreaPerm.setEnabled(true);
            etTJHPincodePerm.setEnabled(true);
            spTJHPermState.setEnabled(true);
            spTJHPermCity.setEnabled(true);
            spTJHPermCountry.setEnabled(true);
            spTJHAddProofPerm.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


/*        if (!etNomineeBldgNameCorres.getText().toString().equals("") && inputLayoutNomineeBldgNameCorres.isErrorEnabled()) {
            inputLayoutNomineeBldgNameCorres.setErrorEnabled(false);
        } else*/
        if ((etNomineeBldgNameCorres.isEnabled()) && !etNomineeBldgNameCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeBldgNameCorres.getText().toString())) {
            inputLayoutNomineeBldgNameCorres.setErrorEnabled(true);
            inputLayoutNomineeBldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutNomineeBldgNameCorres.setErrorEnabled(false);
        }
       /* if (!etNomineeFlatNoCorres.getText().toString().equals("") && inputLayoutNomineeFlatNoCorres.isErrorEnabled()) {
            inputLayoutNomineeFlatNoCorres.setErrorEnabled(false);
        } else */
        if ((etNomineeFlatNoCorres.isEnabled()) && !etNomineeFlatNoCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeFlatNoCorres.getText().toString())) {
            inputLayoutNomineeFlatNoCorres.setErrorEnabled(true);
            inputLayoutNomineeFlatNoCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutNomineeFlatNoCorres.setErrorEnabled(false);
        }
       /* if (!etNomineeAreaCorres.getText().toString().equals("") && inputLayoutNomineeAreaCorres.isErrorEnabled()) {
            inputLayoutNomineeAreaCorres.setErrorEnabled(false);
        } else*/
        if ((etNomineeAreaCorres.isEnabled()) && !etNomineeAreaCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeAreaCorres.getText().toString())) {
            inputLayoutNomineeAreaCorres.setErrorEnabled(true);
            inputLayoutNomineeAreaCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutNomineeAreaCorres.setErrorEnabled(false);
        }
        if (!etNomineeMiddleName.getText().toString().equals("") && inputLayoutNomineeMiddleName.isErrorEnabled()) {
            inputLayoutNomineeMiddleName.setErrorEnabled(false);
        }

        if (!etNomineeDOB.getText().toString().equals("") && inputLayoutNomineeDob.isErrorEnabled()) {
            inputLayoutNomineeDob.setErrorEnabled(false);
        }
        if (!etNomineesRelationship.getText().toString().equals("") && inputLayoutNomineesRelationship.isErrorEnabled()) {
            inputLayoutNomineesRelationship.setErrorEnabled(false);
        }
        /*if (!etNomineePrimaryApplicantsRelationship.getText().toString().equals("") && inputLayoutNomineePrimaryApplicantsRelationship.isErrorEnabled()) {
            inputLayoutNomineePrimaryApplicantsRelationship.setErrorEnabled(false);
        }*/
        if (!etNomineeGuardianMiddleName.getText().toString().equals("") && inputLayoutNomineeGuardianMiddleName.isErrorEnabled()) {
            inputLayoutNomineeGuardianMiddleName.setErrorEnabled(false);
        }
       /* if (!etNomineeGuardianBldgNameCorres.getText().toString().equals("") && inputLayoutNomineeGuardianBldgNameCorres.isErrorEnabled()) {
            inputLayoutNomineeGuardianBldgNameCorres.setErrorEnabled(false);
        }*/
        if ((etNomineeGuardianBldgNameCorres.isEnabled()) && !etNomineeGuardianBldgNameCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeGuardianBldgNameCorres.getText().toString())) {
            inputLayoutNomineeGuardianBldgNameCorres.setErrorEnabled(true);
            inputLayoutNomineeGuardianBldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutNomineeGuardianBldgNameCorres.setErrorEnabled(false);
        }
        /*if (!etNomineeGuardianFlatNoCorres.getText().toString().equals("") && inputLayoutNomineeGuardianFlatNoCorres.isErrorEnabled()) {
            inputLayoutNomineeGuardianFlatNoCorres.setErrorEnabled(false);
        }*/
        if ((etNomineeGuardianFlatNoCorres.isEnabled()) && !etNomineeGuardianFlatNoCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeGuardianFlatNoCorres.getText().toString())) {
            inputLayoutNomineeGuardianFlatNoCorres.setErrorEnabled(true);
            inputLayoutNomineeGuardianFlatNoCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutNomineeGuardianFlatNoCorres.setErrorEnabled(false);
        }

       /* if (!etNomineeGuardianAreaCorres.getText().toString().equals("") && inputLayoutNomineeGuardianAreaCorres.isErrorEnabled()) {
            inputLayoutNomineeGuardianAreaCorres.setErrorEnabled(false);
        }*/
        if ((etNomineeGuardianAreaCorres.isEnabled()) && !etNomineeGuardianAreaCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etNomineeGuardianAreaCorres.getText().toString())) {
            inputLayoutNomineeGuardianAreaCorres.setErrorEnabled(true);
            inputLayoutNomineeGuardianAreaCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutNomineeGuardianAreaCorres.setErrorEnabled(false);
        }
        if (!etNomineesGuardianRelationship.getText().toString().equals("") && inputLayoutNomineesGuardianRelationship.isErrorEnabled()) {
            inputLayoutNomineesGuardianRelationship.setErrorEnabled(false);
        }
        /*if (!etSJHBldgNameCorres.getText().toString().equals("") && inputLayoutSJHBldgNameCorres.isErrorEnabled()) {
            inputLayoutSJHBldgNameCorres.setErrorEnabled(false);
        }*/
        if ((etSJHBldgNameCorres.isEnabled()) && !etSJHBldgNameCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etSJHBldgNameCorres.getText().toString())) {
            inputLayoutSJHBldgNameCorres.setErrorEnabled(true);
            inputLayoutSJHBldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutSJHBldgNameCorres.setErrorEnabled(false);
        }
       /* if (!etSJHFlatNoCorres.getText().toString().equals("") && inputLayoutSJHFlatNoCorres.isErrorEnabled()) {
            inputLayoutSJHFlatNoCorres.setErrorEnabled(false);
        }*/
        if ((etSJHFlatNoCorres.isEnabled()) && !etSJHFlatNoCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etSJHFlatNoCorres.getText().toString())) {
            inputLayoutSJHFlatNoCorres.setErrorEnabled(true);
            inputLayoutSJHFlatNoCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutSJHFlatNoCorres.setErrorEnabled(false);
        }
        /*if (!etSJHAreaCorres.getText().toString().equals("") && inputLayoutSJHAreaCorres.isErrorEnabled()) {
            inputLayoutSJHAreaCorres.setErrorEnabled(false);
        }*/
        if ((etSJHAreaCorres.isEnabled()) && !etSJHAreaCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etSJHAreaCorres.getText().toString())) {
            inputLayoutSJHAreaCorres.setErrorEnabled(true);
            inputLayoutSJHAreaCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutSJHAreaCorres.setErrorEnabled(false);
        }
       /* if (!etSJHBldgNamePerm.getText().toString().equals("") && inputLayoutSJHBldgNamePerm.isErrorEnabled()) {
            inputLayoutSJHBldgNamePerm.setErrorEnabled(false);
        }*/
        if ((etSJHBldgNamePerm.isEnabled()) && !etSJHBldgNamePerm.getText().toString().equals("") && !ValidationRule.validateAddress(etSJHBldgNamePerm.getText().toString())) {
            inputLayoutSJHBldgNamePerm.setErrorEnabled(true);
            inputLayoutSJHBldgNamePerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutSJHBldgNamePerm.setErrorEnabled(false);
        }
        /*if (!etSJHFlatNamePerm.getText().toString().equals("") && inputLayoutSJHFlatNamePerm.isErrorEnabled()) {
            inputLayoutSJHFlatNamePerm.setErrorEnabled(false);
        }*/
        if ((etSJHFlatNamePerm.isEnabled()) && !etSJHFlatNamePerm.getText().toString().equals("") && !ValidationRule.validateAddress(etSJHFlatNamePerm.getText().toString())) {
            inputLayoutSJHFlatNamePerm.setErrorEnabled(true);
            inputLayoutSJHFlatNamePerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutSJHFlatNamePerm.setErrorEnabled(false);
        }
        /*if (!etSJHAreaPerm.getText().toString().equals("") && inputLayoutSJHAreaPerm.isErrorEnabled()) {
            inputLayoutSJHAreaPerm.setErrorEnabled(false);
        }*/
        if ((etSJHAreaPerm.isEnabled()) && !etSJHAreaPerm.getText().toString().equals("") && !ValidationRule.validateAddress(etSJHAreaPerm.getText().toString())) {
            inputLayoutSJHAreaPerm.setErrorEnabled(true);
            inputLayoutSJHAreaPerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutSJHAreaPerm.setErrorEnabled(false);
        }

       /* if (!etTJHBldgNameCorres.getText().toString().equals("") && inputLayoutTJHBldgNameCorres.isErrorEnabled()) {
            inputLayoutTJHBldgNameCorres.setErrorEnabled(false);
        }*/
        if ((etTJHBldgNameCorres.isEnabled()) && !etTJHBldgNameCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etTJHBldgNameCorres.getText().toString())) {
            inputLayoutTJHBldgNameCorres.setErrorEnabled(true);
            inputLayoutTJHBldgNameCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutTJHBldgNameCorres.setErrorEnabled(false);
        }
        /*if (!etTJHFlatNoCorres.getText().toString().equals("") && inputLayoutTJHFlatNoCorres.isErrorEnabled()) {
            inputLayoutTJHFlatNoCorres.setErrorEnabled(false);
        }*/
        if ((etTJHFlatNoCorres.isEnabled()) && !etTJHFlatNoCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etTJHFlatNoCorres.getText().toString())) {
            inputLayoutTJHFlatNoCorres.setErrorEnabled(true);
            inputLayoutTJHFlatNoCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutTJHFlatNoCorres.setErrorEnabled(false);
        }
        /*if (!etTJHAreaCorres.getText().toString().equals("") && inputLayoutTJHAreaCorres.isErrorEnabled()) {
            inputLayoutTJHAreaCorres.setErrorEnabled(false);
        }*/
        if ((etTJHAreaCorres.isEnabled()) && !etTJHAreaCorres.getText().toString().equals("") && !ValidationRule.validateAddress(etTJHAreaCorres.getText().toString())) {
            inputLayoutTJHAreaCorres.setErrorEnabled(true);
            inputLayoutTJHAreaCorres.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutTJHAreaCorres.setErrorEnabled(false);
        }
       /* if (!etTJHBldgNamePerm.getText().toString().equals("") && inputLayoutTJHBldgNamePerm.isErrorEnabled()) {
            inputLayoutTJHBldgNamePerm.setErrorEnabled(false);
        }*/
        if ((etTJHBldgNamePerm.isEnabled()) && !etTJHBldgNamePerm.getText().toString().equals("") && !ValidationRule.validateAddress(etTJHBldgNamePerm.getText().toString())) {
            inputLayoutTJHBldgNamePerm.setErrorEnabled(true);
            inputLayoutTJHBldgNamePerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutTJHBldgNamePerm.setErrorEnabled(false);
        }
       /* if (!etTJHFlatNamePerm.getText().toString().equals("") && inputLayoutTJHFlatNamePerm.isErrorEnabled()) {
            inputLayoutTJHFlatNamePerm.setErrorEnabled(false);
        }*/
        if ((etTJHFlatNamePerm.isEnabled()) && !etTJHFlatNamePerm.getText().toString().equals("") && !ValidationRule.validateAddress(etTJHFlatNamePerm.getText().toString())) {
            inputLayoutTJHFlatNamePerm.setErrorEnabled(true);
            inputLayoutTJHFlatNamePerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutTJHFlatNamePerm.setErrorEnabled(false);
        }
       /* if (!etTJHAreaPerm.getText().toString().equals("") && inputLayoutTJHAreaPerm.isErrorEnabled()) {
            inputLayoutTJHAreaPerm.setErrorEnabled(false);
        }*/
        if ((etTJHAreaPerm.isEnabled()) && !etTJHAreaPerm.getText().toString().equals("") && !ValidationRule.validateAddress(etTJHAreaPerm.getText().toString())) {
            inputLayoutTJHAreaPerm.setErrorEnabled(true);
            inputLayoutTJHAreaPerm.setError(Details_entered_should_be_of_minimum_5_and_max_30_characters_without_special_characters);
        } else {
            inputLayoutTJHAreaPerm.setErrorEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public void disableEnableField() {
        if (getInterestedInMF() && (isMfIpvDone() || isOfflineDocumentUploadedDone())) {
            showPaid();
            return;
        } else if (getInterestedInMF() && !isMfIpvDone()) {
            showpending();
            return;
        }
        if (isEquityOriginal() && isCommodityOriginal()) {
            if (isEquityFundTransfered() && isCommodityFundTransfered()) {
                //both segment selected and both are paid
                showPaid();
            }
        }
        if (isEquityOriginal() && !isEquityFundTransfered()) {
            showpending();
        } else { //paid
            if (isCommodityOriginal() && !isCommodityFundTransfered()) {
                showpending();
            } else {
                showPaid();
            }
        }
        if (isCommodityOriginal() && !isCommodityFundTransfered()) {
            showpending();
        } else { //paid
            if (isEquityOriginal() && !isEquityFundTransfered()) {
                showpending();
            } else {
                showPaid();
            }
        }
    }

    private void showPaid() {
        disableField(llNomineeQuestion);
        disableField(llNomineeDetail);
        disableField(llJointHolderQuestion);
        disableField(llJointHolderDetail);
    }

    private void showpending() {
        enableField(llNomineeQuestion);
        enableField(llNomineeDetail);
        enableField(llJointHolderQuestion);
        enableField(llJointHolderDetail);


        if (cbNomineeSameAsPrimaryCorrespondence.isChecked()) {
            cbNomineeSameAsPrimaryCorrespondence.setChecked(true);
            etNomineeBldgNameCorres.setEnabled(false);
            etNomineeFlatNoCorres.setEnabled(false);
            etNomineeAreaCorres.setEnabled(false);
            etNomineePincodeCorres.setEnabled(false);
            //If address mode is manual spinner will be visible otherwise edittext
            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                spNomineeCorresState.setVisibility(View.GONE);
                spNomineeCorresCity.setVisibility(View.GONE);
                etNomineeCorresState.setVisibility(View.VISIBLE);
                etNomineeCorresCity.setVisibility(View.VISIBLE);
                etNomineeCorresState.setEnabled(false);
                etNomineeCorresCity.setEnabled(false);
            } else {
                spNomineeCorresState.setVisibility(View.VISIBLE);
                spNomineeCorresCity.setVisibility(View.VISIBLE);
                etNomineeCorresState.setVisibility(View.GONE);
                etNomineeCorresCity.setVisibility(View.GONE);
                spNomineeCorresState.setEnabled(false);
                spNomineeCorresCity.setEnabled(false);
            }
            spNomineeCorresCountry.setEnabled(false);

        }
        if (cbNomineeGuardianSameAsPrimaryCorrespondence.isChecked()) {
            cbNomineeGuardianSameAsPrimaryCorrespondence.setChecked(true);
            etNomineeGuardianBldgNameCorres.setEnabled(false);
            etNomineeGuardianFlatNoCorres.setEnabled(false);
            etNomineeGuardianAreaCorres.setEnabled(false);
            etNomineeGuardianPincodeCorres.setEnabled(false);
            //If address mode is manual spinner will be visible otherwise edittext
            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                spNomineeGuardianCorresState.setVisibility(View.GONE);
                spNomineeGuardianCorresCity.setVisibility(View.GONE);
                etNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                etNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                etNomineeGuardianCorresState.setEnabled(false);
                etNomineeGuardianCorresCity.setEnabled(false);
            } else {
                spNomineeGuardianCorresState.setVisibility(View.VISIBLE);
                spNomineeGuardianCorresCity.setVisibility(View.VISIBLE);
                etNomineeGuardianCorresState.setVisibility(View.GONE);
                etNomineeGuardianCorresCity.setVisibility(View.GONE);
                spNomineeGuardianCorresState.setEnabled(false);
                spNomineeGuardianCorresCity.setEnabled(false);
            }
            spNomineeGuardianCorresCountry.setEnabled(false);

        }

        if (cbSJHSameAsPrimaryCorrespondence.isChecked()) {

            if (MOSLCommon.isBlank(getDematFirstJointHolderAdd1())) {
                etSJHBldgNameCorres.setEnabled(true);
            } else {
                etSJHBldgNameCorres.setEnabled(false);
            }
            if (MOSLCommon.isBlank(getDematFirstJointHolderAdd2())) {
                etSJHFlatNoCorres.setEnabled(true);
            } else {
                etSJHFlatNoCorres.setEnabled(false);
            }
            if (MOSLCommon.isBlank(getDematFirstJointHolderAdd3())) {
                etSJHAreaCorres.setEnabled(true);
            } else {
                etSJHAreaCorres.setEnabled(false);
            }
            if (MOSLCommon.isBlank(getDematFirstJointHolderPin())) {
                etSJHPincodeCorres.setEnabled(true);
            } else {
                etSJHPincodeCorres.setEnabled(false);
            }
            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                spSJHCorresState.setVisibility(View.GONE);
                spSJHCorresCity.setVisibility(View.GONE);
                etSJHCorresState.setVisibility(View.VISIBLE);
                etSJHCorresCity.setVisibility(View.VISIBLE);
                if (MOSLCommon.isBlank(getDematFirstJointHolderCity())) {
                    etSJHCorresCity.setEnabled(true);
                } else {
                    etSJHCorresCity.setEnabled(false);
                }
                if (MOSLCommon.isBlank(getDematFirstJointHolderState())) {
                    etSJHCorresState.setEnabled(true);
                } else {
                    etSJHCorresState.setEnabled(false);
                }
            } else {
                spSJHCorresState.setVisibility(View.VISIBLE);
                spSJHCorresCity.setVisibility(View.VISIBLE);
                etSJHCorresState.setVisibility(View.GONE);
                etSJHCorresCity.setVisibility(View.GONE);
                if (MOSLCommon.isBlank(getDematFirstJointHolderCity())) {
                    spSJHCorresCity.setEnabled(true);
                } else {
                    spSJHCorresCity.setEnabled(false);
                }
                if (MOSLCommon.isBlank(getDematFirstJointHolderState())) {
                    spSJHCorresState.setEnabled(true);
                } else {
                    spSJHCorresState.setEnabled(false);
                }
            }
            if (MOSLCommon.isBlank(getDematFirstJointHolderCountry())) {
                spSJHCorresCountry.setEnabled(true);
            } else {
                spSJHCorresCountry.setEnabled(false);
            }

        }
        if (cbTJHSameAsPrimaryCorrespondence.isChecked()) {

            if (MOSLCommon.isBlank(getDematSecondJointHolderAdd1())) {
                etTJHBldgNameCorres.setEnabled(true);
            } else {
                etTJHBldgNameCorres.setEnabled(false);
            }
            if (MOSLCommon.isBlank(getDematSecondJointHolderAdd2())) {
                etTJHFlatNoCorres.setEnabled(true);
            } else {
                etTJHFlatNoCorres.setEnabled(false);
            }
            if (MOSLCommon.isBlank(getDematSecondJointHolderAdd3())) {
                etTJHAreaCorres.setEnabled(true);
            } else {
                etTJHAreaCorres.setEnabled(false);
            }
            if (MOSLCommon.isBlank(getDematSecondJointHolderPin())) {
                etTJHPincodeCorres.setEnabled(true);
            } else {
                etTJHPincodeCorres.setEnabled(false);
            }
            if (getAddressMode().equalsIgnoreCase(AADHARADDDETAIL) || getAddressMode().equalsIgnoreCase(CVL)) {
                spTJHCorresState.setVisibility(View.GONE);
                spTJHCorresCity.setVisibility(View.GONE);
                etTJHCorresState.setVisibility(View.VISIBLE);
                etTJHCorresCity.setVisibility(View.VISIBLE);
                if (MOSLCommon.isBlank(getDematSecondJointHolderCity())) {
                    etTJHCorresCity.setEnabled(true);
                } else {
                    etTJHCorresCity.setEnabled(false);
                }
                if (MOSLCommon.isBlank(getDematSecondJointHolderState())) {
                    etTJHCorresState.setEnabled(true);
                } else {
                    etTJHCorresState.setEnabled(false);
                }
            } else {
                spTJHCorresState.setVisibility(View.VISIBLE);
                spTJHCorresCity.setVisibility(View.VISIBLE);
                etTJHCorresState.setVisibility(View.GONE);
                etTJHCorresCity.setVisibility(View.GONE);
                if (MOSLCommon.isBlank(getDematSecondJointHolderCity())) {
                    spTJHCorresCity.setEnabled(true);
                } else {
                    spTJHCorresCity.setEnabled(false);
                }
                if (MOSLCommon.isBlank(getDematSecondJointHolderState())) {
                    spTJHCorresState.setEnabled(true);
                } else {
                    spTJHCorresState.setEnabled(false);
                }
            }
            if (MOSLCommon.isBlank(getDematSecondJointHolderCountry())) {
                spTJHCorresCountry.setEnabled(true);
            } else {
                spTJHCorresCountry.setEnabled(false);
            }

        }
    }
}

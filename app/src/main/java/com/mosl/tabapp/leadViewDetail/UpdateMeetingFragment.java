package com.mosl.tabapp.leadViewDetail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeItem;
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest;
import com.mosl.saathi.api.model.createmeeting.MeetingAttendeeItem;
import com.mosl.saathi.api.model.createmeeting.MeetingByIdItem;
import com.mosl.saathi.api.model.createmeeting.MeetingByIdRequest;
import com.mosl.saathi.api.model.lead.LookupItem;
import com.mosl.saathi.api.model.lead.LookupRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.clientsearch.DelayAutoCompleteTextView;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.AddedAttendeeModel;
import com.mosl.tabapp.model.AddedSplAttendeeModel;
import com.mosl.tabapp.model.EmployeeModel;
import com.mosl.tabapp.model.MeetingApptModel;
import com.mosl.tabapp.model.SplAttendeeModel;
import com.mosl.tabapp.ui.MSFDialog;
import io.reactivex.disposables.Disposable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.filterOnlyAlphabetAndNumbersAnsAllowedChar;
import static com.mosl.tabapp.common.CRMAppUtil.getJsonSafeString;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.common.RowData.getSpinnerRowDataById;
import static com.mosl.tabapp.constants.MOSLConstants.ACTIONABLES;
import static com.mosl.tabapp.constants.MOSLConstants.ACTIVITYTYPE;
import static com.mosl.tabapp.constants.MOSLConstants.ATLEAST_TEN_CHARACTERS;
import static com.mosl.tabapp.constants.MOSLConstants.ATTENDEEE_NOT_FOUND_MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.ATTENDEE_ALREADY_ADDED;
import static com.mosl.tabapp.constants.MOSLConstants.BACLIENTMEETING;
import static com.mosl.tabapp.constants.MOSLConstants.CATEGORY;
import static com.mosl.tabapp.constants.MOSLConstants.DATE;
import static com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY;
import static com.mosl.tabapp.constants.MOSLConstants.INTERESTEDIN;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD_ID;
import static com.mosl.tabapp.constants.MOSLConstants.MEETING;
import static com.mosl.tabapp.constants.MOSLConstants.MEETINGDURATION;
import static com.mosl.tabapp.constants.MOSLConstants.MEETINGOBJECTIVE;
import static com.mosl.tabapp.constants.MOSLConstants.MEETINGREASON;
import static com.mosl.tabapp.constants.MOSLConstants.MEETING_DESCRIPTION_CANNOT_BE_LEFT_BLANK;
import static com.mosl.tabapp.constants.MOSLConstants.OUTSTATIONMEETING;
import static com.mosl.tabapp.constants.MOSLConstants.PRODUCT;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_ACTIONABLES;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_ACTIVITY_TYPE;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_BA_CLIENT_MEETING;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_CATEGORY;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_MEETING_DURATION;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_MEETING_OBJECTIVE;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_MEETING_REASON;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_OUT_STATION_MEETING;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_PRODUCT;
import static com.mosl.tabapp.constants.MOSLConstants.SELECT_SUB_CATEGORY;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_SERVER_ERROR_OCCURRED;
import static com.mosl.tabapp.constants.MOSLConstants.SPL_ATTENDEE_ALREADY_ADDED;
import static com.mosl.tabapp.constants.MOSLConstants.SUBCATEGORY;
import static com.mosl.tabapp.constants.MOSLConstants.TIME;
import static com.mosl.tabapp.constants.MOSLConstants.UCID;
import static com.mosl.tabapp.constants.MOSLConstants.UPDATED_MEETING;
import static com.mosl.tabapp.leadViewDetail.SetAppointmentFragment.listHasAttende;
import static com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class UpdateMeetingFragment extends CommonFragment
	implements View.OnClickListener, TextView.OnEditorActionListener {
	private View createMeetingView;
	private TextView tvSplAttendeeCount, tvAttendeeCount, tvStartDate;
	private View llSubcategory;
	private Spinner spProduct, spMeetingDuration, spActivityType, spMeetingReason, spActionables,
		spSubCategory, spCategory, spBAClient, spOutStaition;
	private EditText etCustomerComment, etMeetingDescription, etCategory, etSubCategory,
		etClientCode, etClientName;
	private AttendeeAdapter attendeeAdapter;
	private SplAttendeeAdapter splAttendeeAdapter;
	private ArrayList<AddedAttendeeModel> addedAttendeeModelList = new ArrayList<>();
	private ArrayList<AddedSplAttendeeModel> addedSplAttendeeModelList = new ArrayList<>();
	private int addedAttendeeCount;
	private int addedSplAttendeeCount;
	private String productCodeStr;
	private String meetingDurationCodeStr;
	private String activityTypeCodeStr;
	private String meetingReasonCodeStr;
	private String actionablesCodeStr;
	private String categoryCodeStr;
	private String subCategoryCodestr;
	private String outStaitionCodeStr;
	private String baClientCodeStr;
	private ArrayList<RowData> arrOutStation = new ArrayList<>();
	private ArrayList<RowData> arrSelectProduct = new ArrayList<>();
	private ArrayList<RowData> arrMeetingDuration = new ArrayList<>();
	private ArrayList<RowData> arrMeetingObjective = new ArrayList<>();
	private ArrayList<RowData> arrActivityType = new ArrayList<>();
	private ArrayList<RowData> arrMeetingReason = new ArrayList<>();
	private ArrayList<RowData> arrCategory = new ArrayList<>();
	private ArrayList<RowData> arrSubCategory = new ArrayList<>();
	private ArrayList<RowData> arrBAClient = new ArrayList<>();
	private ArrayList<RowData> arrActionables = new ArrayList<>();
	private String leadId;
	private String meetingId;
	private String SubCategoryID;
	private String ActivityTypeID;
	private String MeetingReasonID;
	private String ActionableID;
	private String CategoryID;
	private String MeetingDate;
	private int currentYear, month, day, hour, minute;
	private Calendar calendar = Calendar.getInstance();
	private TextView tvProductError, tvMeetingDurationError, tvMeetingReasonError,
		tvActivityTypeError, tvActionablesError, tvMeetingDescriptionError, tvBAClientError,
		tvOutStaitionError, tvClientCodeError, tvClientNameError, tvCategoryError,
		tvCategoryCommentsError, tvSubCategoryError, tvSubCategoryCommentsError;
	private LinearLayout llActionables;
	private boolean profileType;
	private String mUCID = "";
	private String meetingObjectiveIdStr = "0";
	private Spinner spMeetingObjective;
	private TextView tvMeetingObjectiveError;
	private boolean mandatory = true;
	private AdapterView.OnItemSelectedListener selectSubCategoryListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					subCategoryCodestr = ((RowData) spSubCategory.getSelectedItem()).getKey();
					tvSubCategoryError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener selectCategoryListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					categoryCodeStr = ((RowData) spCategory.getSelectedItem()).getKey();
					tvCategoryError.setVisibility(View.GONE);
					subCategory(SubCategoryID);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener selectProductListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					productCodeStr = ((RowData) spProduct.getSelectedItem()).getKey();
					tvProductError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener selectMeetingDurationListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					meetingDurationCodeStr =
						((RowData) spMeetingDuration.getSelectedItem()).getKey();
					tvMeetingDurationError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener selectMeetingReasonListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					meetingReasonCodeStr = ((RowData) spMeetingReason.getSelectedItem()).getKey();
					String meetingReasonNameStr =
						((RowData) spMeetingReason.getSelectedItem()).getValue();
					tvMeetingReasonError.setVisibility(View.GONE);
					if (meetingReasonNameStr.equalsIgnoreCase("Customer Service Related")) {
						llActionables.setVisibility(View.VISIBLE);
					} else {
						llActionables.setVisibility(View.GONE);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener selectActionablesListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					actionablesCodeStr = ((RowData) spActionables.getSelectedItem()).getKey();
					tvActionablesError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener selectBAClientListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					baClientCodeStr = ((RowData) spBAClient.getSelectedItem()).getKey();
					tvBAClientError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener selectOutStationListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					outStaitionCodeStr = ((RowData) spOutStaition.getSelectedItem()).getKey();
					tvOutStaitionError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener selectActivityTypeListener =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					activityTypeCodeStr = ((RowData) spActivityType.getSelectedItem()).getKey();
					tvActivityTypeError.setVisibility(View.GONE);
					if (!spActivityType.getSelectedItem()
						.toString()
						.equalsIgnoreCase(SELECT_ACTIVITY_TYPE)) {
						meetingReason(MeetingReasonID);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};
	private AdapterView.OnItemSelectedListener selectMeetingObjective =
		new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					//TODO meeting objective on select
					meetingObjectiveIdStr = ((RowData) parent.getSelectedItem()).getKey();
					tvMeetingObjectiveError.setVisibility(View.GONE);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		};

	@Override protected int getLayout() {
		return R.layout.frag_update_meeting;
	}

	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		createMeetingView = super.onCreateView(inflater, container, savedInstanceState);
		meetingId = Objects.requireNonNull(this.getArguments()).getString(MEETING, "0");
		getMeetingById(meetingId);
		container.post(() -> setAppTitle("Update Meeting"));
		meetingObjective();
		setUpView(createMeetingView);
		return createMeetingView;
	}

	@SuppressLint("ClickableViewAccessibility") private void setUpView(View createMeetingView) {
		ScrollView scrollView = createMeetingView.findViewById(R.id.scrollView);
		scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		scrollView.setFocusable(true);
		scrollView.setFocusableInTouchMode(true);
		scrollView.setOnTouchListener((v, event) -> {
			v.requestFocusFromTouch();
			return false;
		});
		RelativeLayout rlDate = createMeetingView.findViewById(R.id.rlDate);
		tvStartDate = createMeetingView.findViewById(R.id.tvDate);
		rlDate.setOnClickListener(this);

		tvSplAttendeeCount = createMeetingView.findViewById(R.id.tvSplAttendeeCount);
		tvAttendeeCount = createMeetingView.findViewById(R.id.tvAttendeeCount);
		llSubcategory = createMeetingView.findViewById(R.id.llSubcategory);
		TextView tvMeetingTime = createMeetingView.findViewById(R.id.tvMeetingTime);
		TextView tvMeetingDate = createMeetingView.findViewById(R.id.tvMeetingDate);
		TextView tvProduct = createMeetingView.findViewById(R.id.tvProduct);
		tvMeetingObjectiveError =
			createMeetingView.findViewById(R.id.tvMeetingObjectiveError);
		tvProductError = createMeetingView.findViewById(R.id.tvProductError);
		tvMeetingDurationError =
			createMeetingView.findViewById(R.id.tvMeetingDurationError);
		tvMeetingReasonError = createMeetingView.findViewById(R.id.tvMeetingReasonError);
		tvActivityTypeError = createMeetingView.findViewById(R.id.tvActivityTypeError);
		tvActionablesError = createMeetingView.findViewById(R.id.tvActionablesError);
		tvMeetingDescriptionError =
			createMeetingView.findViewById(R.id.tvMeetingDescriptionError);
		tvCategoryError = createMeetingView.findViewById(R.id.tvCategoryError);
		tvSubCategoryError = createMeetingView.findViewById(R.id.tvSubCategoryError);
		tvSubCategoryCommentsError =
			createMeetingView.findViewById(R.id.tvSubCategoryCommentsError);
		tvCategoryCommentsError =
			createMeetingView.findViewById(R.id.tvCategoryCommentsError);

		Button btnSave = createMeetingView.findViewById(R.id.btnSave);
		Button btnReset = createMeetingView.findViewById(R.id.btnReset);
		btnSave.setOnClickListener(this);
		btnReset.setOnClickListener(this);

		tvBAClientError = createMeetingView.findViewById(R.id.tvBAClientError);
		tvOutStaitionError = createMeetingView.findViewById(R.id.tvOutStaitionError);
		tvClientCodeError = createMeetingView.findViewById(R.id.tvClientCodeError);
		tvClientNameError = createMeetingView.findViewById(R.id.tvClientNameError);

		ImageButton ibSplAttendee =
			createMeetingView.findViewById(R.id.ibSplAttendee);
		ImageButton ibAttendee = createMeetingView.findViewById(R.id.ibAttendee);

		etCustomerComment = createMeetingView.findViewById(R.id.etCustomerComment);
		etCustomerComment.setFilters(
			new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
		etMeetingDescription = createMeetingView.findViewById(R.id.etMeetingDescription);
		etMeetingDescription.setFilters(
			new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
		etCategory = createMeetingView.findViewById(R.id.etCategory);
		etCategory.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
		etSubCategory = createMeetingView.findViewById(R.id.etSubCategory);
		etSubCategory.setFilters(new InputFilter[] { filterOnlyAlphabetAndNumbersAnsAllowedChar });
		etClientCode = createMeetingView.findViewById(R.id.etClientCode);
		etClientName = createMeetingView.findViewById(R.id.etClientName);

		spProduct = createMeetingView.findViewById(R.id.spProduct);
		spMeetingDuration = createMeetingView.findViewById(R.id.spMeetingDuration);
		spActivityType = createMeetingView.findViewById(R.id.spActivityType);
		spMeetingReason = createMeetingView.findViewById(R.id.spMeetingReason);
		spActionables = createMeetingView.findViewById(R.id.spActionables);
		spCategory = createMeetingView.findViewById(R.id.spCategory);
		spSubCategory = createMeetingView.findViewById(R.id.spSubCategory);
		spMeetingObjective = createMeetingView.findViewById(R.id.spMeetingObjective);
		spBAClient = createMeetingView.findViewById(R.id.spBAClient);
		spOutStaition = createMeetingView.findViewById(R.id.spOutStaition);
		LinearLayout linaerLayoutNew =
			createMeetingView.findViewById(R.id.linaerLayoutNew);
		llActionables = createMeetingView.findViewById(R.id.llActionables);
		ibAttendee.setOnClickListener(this);
		ibSplAttendee.setOnClickListener(this);
		tvAttendeeCount.setOnClickListener(this);
		tvSplAttendeeCount.setOnClickListener(this);
		try {
			leadId = Objects.requireNonNull(this.getArguments()).getString(LEAD_ID, "0");
			tvProduct.setText(
				String.format("Appointments-%s", this.getArguments().getString(PRODUCT)));
			tvMeetingDate.setText(this.getArguments().getString(DATE));
			tvMeetingTime.setText(String.format("Time-%s", this.getArguments().getString(TIME)));
			mUCID = this.getArguments().getString(UCID, "0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		minute = calendar.get(Calendar.MINUTE);
		AddedAttendeeModel.setAttendeeUserEmplyeeCode(
			Objects.requireNonNull(userManager.getUser()).employeeCode);
		//  selectProduct(null);
		if (userManager.getUser().isFranchise()
			|| userManager.getUser().isIFA()) {
			linaerLayoutNew.setVisibility(View.VISIBLE);
			profileType = true;
		}
		etClientName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!isBlank(etClientName.getText().toString().trim())) {
					tvClientNameError.setVisibility(View.GONE);
				} else {
					tvClientNameError.setVisibility(View.GONE);
					tvClientNameError.setText(getString(R.string.validation_msg_enter_clien_name));
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
				if (!isBlank(etClientCode.getText().toString().trim())) {
					tvClientCodeError.setVisibility(View.GONE);
				} else {
					tvClientCodeError.setVisibility(View.GONE);
					tvClientCodeError.setText(getString(R.string.validation_msg_enter_client_code));
				}
			}
		});

		etMeetingDescription.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!isBlank(etMeetingDescription.getText().toString().trim())) {
					if (etMeetingDescription.getText().length() >= 10) {
						tvMeetingDescriptionError.setVisibility(View.GONE);
					} else {
						tvMeetingDescriptionError.setVisibility(View.VISIBLE);
						tvMeetingDescriptionError.setText(ATLEAST_TEN_CHARACTERS);
					}
				} else {
					tvMeetingDescriptionError.setVisibility(View.GONE);
				}
			}
		});

		etSubCategory.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!isBlank(etSubCategory.getText().toString().trim())) {
					if (etSubCategory.getText().length() >= 10) {
						tvSubCategoryCommentsError.setVisibility(View.GONE);
					} else {
						tvSubCategoryCommentsError.setVisibility(View.VISIBLE);
						tvSubCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
					}
				} else {
					tvSubCategoryCommentsError.setVisibility(View.GONE);
				}
			}
		});

		etCategory.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!isBlank(etCategory.getText().toString().trim())) {
					if (etCategory.getText().length() >= 10) {
						tvCategoryCommentsError.setVisibility(View.GONE);
					} else {
						tvCategoryCommentsError.setVisibility(View.VISIBLE);
						tvCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
					}
				} else {
					tvCategoryCommentsError.setVisibility(View.GONE);
				}
			}
		});
	}

	private void resetData() {
		addedAttendeeModelList.clear();
		addedSplAttendeeModelList.clear();
		tvAttendeeCount.setText("");
		tvSplAttendeeCount.setText("");
		tvStartDate.setText(MeetingDate);
		spProduct.setSelection(0);
		spMeetingDuration.setSelection(0);
		spActivityType.setSelection(0);
		spMeetingReason.setSelection(0);
		etMeetingDescription.setText("");
		etCustomerComment.setText("");
		spActionables.setSelection(0);
		spCategory.setSelection(0);
		etCategory.setText("");
		spSubCategory.setSelection(0);
		etSubCategory.setText("");
		if (profileType) {
			etClientName.setText("");
			etClientCode.setText("");
			spOutStaition.setSelection(0);
			spBAClient.setSelection(0);
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		try {
			if (id == R.id.ibAttendee) {
				dialogAttendee();
			} else if (id == R.id.ibSplAttendee) {
				dialogSplAttendee();
			} else if (id == R.id.tvAttendeeCount) {
				dialogAddedAttendee();
			} else if (id == R.id.tvSplAttendeeCount) {
				dialogAddedSplAttendee();
			} else if (id == R.id.btnReset) {
				resetData();
			} else if (id == R.id.btnSave) {

				if (validateForm()) {
					MeetingApptModel meetingApptModel = new MeetingApptModel();
					meetingApptModel.setMeetingType("M");
					meetingApptModel.setDisplayType("U");
					meetingApptModel.setLeadId(leadId);
					meetingApptModel.setUcid(mUCID);
					meetingApptModel.setMeetingID(meetingId);
					meetingApptModel.setMeetingDate(
						CRMAppUtil.getDBDate(currentYear, month, day, hour, minute));
					meetingApptModel.setProduct(productCodeStr);
					meetingApptModel.setMeetingDuration(meetingDurationCodeStr);
					meetingApptModel.setMeetingObjectiveID(meetingObjectiveIdStr);
					meetingApptModel.setActivityTypeID(activityTypeCodeStr);
					meetingApptModel.setMeetingReason(meetingReasonCodeStr);
					meetingApptModel.setMeetingDes(
						getJsonSafeString(etMeetingDescription.getText().toString().trim()));
					meetingApptModel.setMeetingAttendee(addedAttendeeModelList);
					meetingApptModel.setSplMeetingAttendee(addedSplAttendeeModelList);
					meetingApptModel.setCustomerComment(
						getJsonSafeString(etCustomerComment.getText().toString().trim()));
					meetingApptModel.setActionables(actionablesCodeStr);
					meetingApptModel.setCategory(categoryCodeStr);
					meetingApptModel.setEtCategory(etCategory.getText().toString().trim());
					meetingApptModel.setSubCategory(subCategoryCodestr);
					meetingApptModel.setEtSubCategory(etSubCategory.getText().toString().trim());
					meetingApptModel.setBaClient(baClientCodeStr);
					meetingApptModel.setOutStation(outStaitionCodeStr);
					meetingApptModel.setClientCode(etClientCode.getText().toString().trim());
					meetingApptModel.setClientName(etClientName.getText().toString().trim());
					updateMeetingSave(meetingApptModel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			hideProgress();
			alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
				SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
		}
	}

	/**
	 * form validation
	 */
	private boolean validateForm() {
		if (spProduct.getSelectedItem().toString().equalsIgnoreCase(SELECT_PRODUCT)) {
			tvProductError.setVisibility(View.VISIBLE);
			tvProductError.setText(SELECT_PRODUCT);
			tvProductError.requestFocus();
			return false;
		} else if (spMeetingDuration.getSelectedItem()
			.toString()
			.equalsIgnoreCase(SELECT_MEETING_DURATION)) {
			tvMeetingDurationError.setVisibility(View.VISIBLE);
			tvMeetingDurationError.setText(SELECT_MEETING_DURATION);
			tvMeetingDurationError.requestFocus();
			return false;
		} else if (spMeetingObjective.getSelectedItem()
			.toString()
			.equalsIgnoreCase(SELECT_MEETING_OBJECTIVE)) {
			tvMeetingObjectiveError.setVisibility(View.VISIBLE);
			tvMeetingObjectiveError.setText(SELECT_MEETING_OBJECTIVE);
			tvMeetingObjectiveError.requestFocus();
			return false;
		} else if (spActivityType.getSelectedItem()
			.toString()
			.equalsIgnoreCase(SELECT_ACTIVITY_TYPE)) {
			tvActivityTypeError.setVisibility(View.VISIBLE);
			tvActivityTypeError.setText(SELECT_ACTIVITY_TYPE);
			tvActivityTypeError.requestFocus();
			return false;
		} else if (spMeetingReason.getSelectedItem()
			.toString()
			.equalsIgnoreCase(SELECT_MEETING_REASON)) {
			tvMeetingReasonError.setVisibility(View.VISIBLE);
			tvMeetingReasonError.setText(SELECT_MEETING_REASON);
			tvMeetingReasonError.requestFocus();
			return false;
		} else if (etMeetingDescription.getText().toString().trim().isEmpty()) {
			tvMeetingDescriptionError.setVisibility(View.VISIBLE);
			tvMeetingDescriptionError.setText(MEETING_DESCRIPTION_CANNOT_BE_LEFT_BLANK);
			tvMeetingDescriptionError.requestFocus();
			return false;
		} else if (etMeetingDescription.getText().length() <= 10) {
			tvMeetingDescriptionError.setVisibility(View.VISIBLE);
			tvMeetingDescriptionError.setText(ATLEAST_TEN_CHARACTERS);
			tvMeetingDescriptionError.requestFocus();
			return false;
		} else if (profileType && spBAClient.getSelectedItem()
			.toString()
			.equalsIgnoreCase(SELECT_BA_CLIENT_MEETING)) {
			tvBAClientError.setVisibility(View.VISIBLE);
			tvBAClientError.setText(SELECT_BA_CLIENT_MEETING);
			tvBAClientError.requestFocus();
			return false;
		} else if (profileType && spOutStaition.getSelectedItem()
			.toString()
			.equalsIgnoreCase(SELECT_OUT_STATION_MEETING)) {
			tvOutStaitionError.setVisibility(View.VISIBLE);
			tvOutStaitionError.setText(SELECT_OUT_STATION_MEETING);
			tvOutStaitionError.requestFocus();
			return false;
		} else if (profileType && isBlank(etClientCode.getText().toString().trim())) {
			tvClientCodeError.setText(R.string.validation_msg_enter_client_code);
			tvClientCodeError.setVisibility(View.VISIBLE);
			tvClientCodeError.requestFocus();
			return false;
		} else if (profileType && isBlank(etClientName.getText().toString().trim())) {
			tvClientNameError.setText(R.string.validation_msg_enter_clien_name);
			tvClientNameError.setVisibility(View.VISIBLE);
			tvClientNameError.requestFocus();
			return false;
		} else if ((llActionables.getVisibility() == View.VISIBLE) &&
			spActionables.getSelectedItem().toString().equalsIgnoreCase(SELECT_ACTIONABLES)) {
			tvActionablesError.setVisibility(View.VISIBLE);
			tvActionablesError.setText(SELECT_ACTIONABLES);
			tvActionablesError.requestFocus();
			return false;
		} else if ((llActionables.getVisibility() == View.VISIBLE) &&
			spCategory.getSelectedItem().toString().equalsIgnoreCase(SELECT_CATEGORY)) {
			tvCategoryError.setVisibility(View.VISIBLE);
			tvCategoryError.setText(SELECT_CATEGORY);
			tvCategoryError.requestFocus();
			return false;
		} else if ((llActionables.getVisibility() == View.VISIBLE) &&
			etCategory.getText().length() <= 10) {
			tvCategoryCommentsError.setVisibility(View.VISIBLE);
			tvCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
			tvCategoryCommentsError.requestFocus();
			return false;
		} else if (mandatory && (llActionables.getVisibility() == View.VISIBLE) &&
			spSubCategory.getSelectedItem().toString().equalsIgnoreCase(SELECT_SUB_CATEGORY)) {
			tvSubCategoryError.setVisibility(View.VISIBLE);
			tvSubCategoryError.setText(SELECT_SUB_CATEGORY);
			tvSubCategoryError.requestFocus();
			return false;
		} else if (mandatory
			&& (llActionables.getVisibility() == View.VISIBLE)
			&& etSubCategory.getText().length() <= 10) {
			tvSubCategoryCommentsError.setVisibility(View.VISIBLE);
			tvSubCategoryCommentsError.setText(ATLEAST_TEN_CHARACTERS);
			tvSubCategoryCommentsError.requestFocus();
			return false;
		}
		return true;
	}

	public String prefix(int input) {
		if (Integer.toString(input).length() == 1) {
			return "0" + input;
		}
		return input + "";
	}

	private void renderMeetingById(UiModel<MeetingByIdItem> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		MeetingByIdItem data = uiModel.data;
		if (data != null) {
			MeetingDate = stringOrEmpty(data.meetingDate);
			ActivityTypeID = stringOrEmpty(data.activityTypeID);
			String productTypeID = stringOrEmpty(data.productTypeID);
			MeetingReasonID = stringOrEmpty(data.meetingReasonID);
			String MeetingDescription = stringOrEmpty(data.meetingDescription);
			String CustomerRemark = stringOrEmpty(data.customerRemark);
			ActionableID = stringOrEmpty(data.actionableID);
			CategoryID = stringOrEmpty(data.categoryID);
			String CategoryDescription = stringOrEmpty(data.categoryDescription);
			SubCategoryID = stringOrEmpty(data.subCategoryID);
			String SubCategoryDescription = stringOrEmpty(data.subCategoryDescription);

			List<MeetingAttendeeItem> meetingAttendee = data.meetingAttendee;
			for (int i = 0; i < meetingAttendee.size(); i++) {
				MeetingAttendeeItem item = meetingAttendee.get(i);
				String MeetingAttendeeID = stringOrEmpty(item.meetingAttendeeID);
				String AttendeeEmpcode = stringOrEmpty(item.attendeeEmpcode);
				String AttendeeType = stringOrEmpty(item.attendeeType);
				boolean DefaultAttendee = (item.defaultAttendee);
				String AttendeeEmpName = stringOrEmpty(item.attendeeEmpName);

				if (AttendeeType.equalsIgnoreCase("S")) {
					addedSplAttendeeModelList.add(
						new AddedSplAttendeeModel(AttendeeType, true,
							AttendeeEmpName, AttendeeEmpcode,
							MeetingAttendeeID));
					addedSplAttendeeCount++;
				} else {
					if (DefaultAttendee) {
						addedAttendeeModelList.add(
							new AddedAttendeeModel(AttendeeType, true,
								AttendeeEmpName, AttendeeEmpcode,
								MeetingAttendeeID));
						addedAttendeeCount++;
					}
				}
			}
			setCountForSpecial(addedSplAttendeeCount);
			setCount(addedAttendeeCount);
			selectProduct(productTypeID);

			String[] metadataArray = MeetingDate.split("T");
			String[] dateArray = metadataArray[0].split("-");
			String[] timeArray = metadataArray[1].split(":");
			currentYear = ((Integer.parseInt(dateArray[0])));
			month = ((Integer.parseInt(dateArray[1])));
			day = ((Integer.parseInt(dateArray[2])));
			hour = ((Integer.parseInt(timeArray[0])));
			minute = ((Integer.parseInt(timeArray[1])));
			etMeetingDescription.setText(MeetingDescription);
			etCustomerComment.setText(CustomerRemark);
			etCategory.setText(CategoryDescription);
			etSubCategory.setText(SubCategoryDescription);
			MeetingDate = MeetingDate.replace("T", " ");
			tvStartDate.setText(CRMAppUtil.getDBDate(
				CRMAppUtil.getDBDateObj(currentYear, month, day, hour,
					minute), DATE_FORMAT_TO_DISPLAY));
		}
	}

	private void getMeetingById(String meetingId) {

		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			baClient();
			MeetingByIdRequest request =
				new MeetingByIdRequest(meetingId);
			Disposable subscribe = getSaathiApi().postMeetingById(request)
				.compose(applyUiModel())
				.subscribe(this::renderMeetingById);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderCategory(UiModel<List<LookupItem>> uiModel,
		String id) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrCategory.clear();
			arrCategory.add(new RowData(0, "-1", SELECT_CATEGORY));
			for (int i = 0; i < data.size(); i++) {
				LookupItem item = data.get(i);
				String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
				String lookUpName = stringOrEmpty(item.lookUpName);
				arrCategory.add(
					new RowData(i + 1, lookUpId, lookUpName));
			}
			ArrayAdapter<RowData> arrAdapCategory =
				new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrCategory);
			arrAdapCategory.setDropDownViewResource(R.layout.mdropdown);
			spCategory.setAdapter(arrAdapCategory);
			spCategory.setOnItemSelectedListener(selectCategoryListener);
			arrAdapCategory.notifyDataSetChanged();
			RowData categoryData = getSpinnerRowDataById(id, arrAdapCategory);
			if (categoryData != null) {
				spCategory.setSelection(categoryData.getPosition());
			}
		}
	}

	private void category(final String id) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId), CATEGORY,
					null, null);
			Disposable subscribe = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(uiModelList -> {
					renderCategory(uiModelList, id);
				});
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderProduct(UiModel<List<LookupItem>> uiModel,
		String id) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrSelectProduct.clear();
			arrSelectProduct.add(new RowData(0, "-1", SELECT_PRODUCT));
			for (int i = 0; i < data.size(); i++) {
				LookupItem item = data.get(i);
				String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
				String lookUpName = stringOrEmpty(item.lookUpName);
				arrSelectProduct.add(
					new RowData(i + 1, lookUpId, lookUpName));
			}
			ArrayAdapter<RowData> arrAdapSelectProduct =
				new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item,
					arrSelectProduct);
			arrAdapSelectProduct.setDropDownViewResource(R.layout.mdropdown);
			spProduct.setAdapter(arrAdapSelectProduct);
			spProduct.setOnItemSelectedListener(selectProductListener);
			arrAdapSelectProduct.notifyDataSetChanged();
			RowData productData = getSpinnerRowDataById(id, arrAdapSelectProduct);
			if (productData != null) {
				spProduct.setSelection(productData.getPosition());
			}
		}
	}

	private void selectProduct(final String id) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			meetingDuration(meetingId);
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId), INTERESTEDIN,
					null, null);
			Disposable subscribe = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(uiModelList -> {
					renderProduct(uiModelList, id);
				});
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderSubcategory(UiModel<List<LookupItem>> uiModel,
		String id) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrSubCategory.clear();
			arrSubCategory.add(new RowData(0, "-1", SELECT_SUB_CATEGORY));
			mandatory = (data.size() > 1);
			if (mandatory) {
				llSubcategory.setVisibility(View.VISIBLE);
			} else {
				etSubCategory.setText("");
				llSubcategory.setVisibility(View.GONE);
			}
			for (int i = 0; i < data.size(); i++) {
				LookupItem item = data.get(i);
				String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
				String lookUpName = stringOrEmpty(item.lookUpName);
				arrSubCategory.add(
					new RowData(i + 1, lookUpId, lookUpName));
			}
			ArrayAdapter<RowData> arrAdapSubCategory = new ArrayAdapter<>(requireContext(),
				R.layout.simple_spinner_item, arrSubCategory);
			arrAdapSubCategory.setDropDownViewResource(R.layout.mdropdown);
			spSubCategory.setAdapter(arrAdapSubCategory);
			spSubCategory.setOnItemSelectedListener(selectSubCategoryListener);
			arrAdapSubCategory.notifyDataSetChanged();
			RowData subCategoryData = getSpinnerRowDataById(id, arrAdapSubCategory);
			if (subCategoryData != null) {
				spSubCategory.setSelection(
					subCategoryData.getPosition());
			}
		}
	}

	private void subCategory(final String id) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId), SUBCATEGORY,
					categoryCodeStr,
					null);
			Disposable subscribe = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(uiModelList -> {
					renderSubcategory(uiModelList, id);
				});
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderMeetingDuration(UiModel<List<LookupItem>> uiModel,
		String id) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrMeetingDuration.clear();
			arrMeetingDuration.add(
				new RowData(0, "-1", SELECT_MEETING_DURATION));
			for (int i = 0; i < data.size(); i++) {
				LookupItem item = data.get(i);
				String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
				String lookUpName = stringOrEmpty(item.lookUpName);
				arrMeetingDuration.add(
					new RowData(i + 1, lookUpId, lookUpName));
			}
			ArrayAdapter<RowData> arrAdapMeetingDuration = new ArrayAdapter<>(requireContext(),
				R.layout.simple_spinner_item, arrMeetingDuration);
			arrAdapMeetingDuration.setDropDownViewResource(R.layout.mdropdown);
			spMeetingDuration.setAdapter(arrAdapMeetingDuration);
			spMeetingDuration.setOnItemSelectedListener(selectMeetingDurationListener);
			arrAdapMeetingDuration.notifyDataSetChanged();
			RowData meetingDurationData = getSpinnerRowDataById(id, arrAdapMeetingDuration);
			if (meetingDurationData != null) {
				spMeetingDuration.setSelection(
					meetingDurationData.getPosition());
			}
		}
	}

	private void meetingDuration(final String id) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			activityType(ActivityTypeID);
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId),
					MEETINGDURATION, null, null);
			Disposable subscribe = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(uiModelList -> {
					renderMeetingDuration(uiModelList, id);
				});
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderMeetingObjective(UiModel<List<LookupItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrMeetingObjective.clear();
			arrMeetingObjective.add(
				new RowData(0, "-1", SELECT_MEETING_OBJECTIVE));
			for (int i = 0; i < data.size(); i++) {
				LookupItem item = data.get(i);
				String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
				String lookUpName = stringOrEmpty(item.lookUpName);
				arrMeetingObjective.add(
					new RowData(i + 1, lookUpId, lookUpName));
			}
			ArrayAdapter<RowData> arrAdapMeetingsObjective = new ArrayAdapter<>(requireContext(),
				R.layout.simple_spinner_item, arrMeetingObjective);
			arrAdapMeetingsObjective.setDropDownViewResource(
				R.layout.mdropdown);
			spMeetingObjective.setAdapter(arrAdapMeetingsObjective);
			spMeetingObjective.setOnItemSelectedListener(
				selectMeetingObjective);
			arrAdapMeetingsObjective.notifyDataSetChanged();
		}
	}

	private void meetingObjective() {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId),
					MEETINGOBJECTIVE, null, null);
			Disposable subscribe = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(this::renderMeetingObjective);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderActivityType(UiModel<List<LookupItem>> uiModel,
		String id) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrActivityType.clear();
			arrActivityType.add(new RowData(0, "-1", SELECT_ACTIVITY_TYPE));
			for (int i = 0; i < data.size(); i++) {
				LookupItem item = data.get(i);
				String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
				String lookUpName = stringOrEmpty(item.lookUpName);
				arrActivityType.add(
					new RowData(i + 1, lookUpId, lookUpName));
			}
			ArrayAdapter<RowData> arrAdapActivityType = new ArrayAdapter<>(requireContext(),
				R.layout.simple_spinner_item, arrActivityType);
			arrAdapActivityType.setDropDownViewResource(
				R.layout.mdropdown);
			spActivityType.setAdapter(arrAdapActivityType);
			spActivityType.setOnItemSelectedListener(
				selectActivityTypeListener);
			arrAdapActivityType.notifyDataSetChanged();
			RowData activityTypeData =
				getSpinnerRowDataById(id, arrAdapActivityType);
			if (activityTypeData != null) {
				spActivityType.setSelection(
					activityTypeData.getPosition());
			}
		}
	}

	private void activityType(final String id) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			actionables(ActionableID);
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId), ACTIVITYTYPE,
					null, null);
			Disposable subscribe = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(uiModelList -> {
					renderActivityType(uiModelList, id);
				});
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderMeetingReason(UiModel<List<LookupItem>> uiModel,
		String id) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrMeetingReason.clear();
			arrMeetingReason.add(
				new RowData(0, "-1", SELECT_MEETING_REASON));
			for (int i = 0; i < data.size(); i++) {
				LookupItem item = data.get(i);
				String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
				String lookUpName = stringOrEmpty(item.lookUpName);
				arrMeetingReason.add(
					new RowData(i + 1, lookUpId, lookUpName));
			}
			ArrayAdapter<RowData> arrAdapMeetingReason = new ArrayAdapter<>(requireContext(),
				R.layout.simple_spinner_item, arrMeetingReason);
			arrAdapMeetingReason.setDropDownViewResource(R.layout.mdropdown);
			spMeetingReason.setAdapter(arrAdapMeetingReason);
			spMeetingReason.setOnItemSelectedListener(selectMeetingReasonListener);
			arrAdapMeetingReason.notifyDataSetChanged();
			RowData meetingReasonData = getSpinnerRowDataById(id, arrAdapMeetingReason);
			if (meetingReasonData != null) {
				spMeetingReason.setSelection(
					meetingReasonData.getPosition());
			}
		}
	}

	private void meetingReason(final String id) {

		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			baClient();
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId),
					MEETINGREASON,
					activityTypeCodeStr, null);
			Disposable subscribe = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(uiModelList -> {
					renderMeetingReason(uiModelList, id);
				});
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderBaClient(UiModel<List<LookupItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrBAClient.clear();
			arrBAClient.add(new RowData(0, "-1", SELECT_BA_CLIENT_MEETING));
			for (int i = 0; i < data.size(); i++) {
				LookupItem item = data.get(i);
				String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
				String lookUpName = stringOrEmpty(item.lookUpName);
				arrBAClient.add(
					new RowData(i + 1, lookUpId, lookUpName));
			}
			ArrayAdapter<RowData> arrAdapBAClient = new ArrayAdapter<>(requireContext(),
				R.layout.simple_spinner_item, arrBAClient);
			arrAdapBAClient.setDropDownViewResource(R.layout.mdropdown);
			spBAClient.setAdapter(arrAdapBAClient);
			spBAClient.setOnItemSelectedListener(
				selectBAClientListener);
			arrAdapBAClient.notifyDataSetChanged();
		}
	}

	private void baClient() {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			outStation();
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId),
					BACLIENTMEETING,
					null, null);
			Disposable subscribe = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(this::renderBaClient);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderOutStation(UiModel<List<LookupItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrOutStation.clear();
			arrOutStation.add(new RowData(0, "-1", SELECT_OUT_STATION_MEETING));
			for (int i = 0; i < data.size(); i++) {
				LookupItem item = data.get(i);
				String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
				String lookUpName = stringOrEmpty(item.lookUpName);
				arrOutStation.add(
					new RowData(i + 1, lookUpId, lookUpName));
			}
			ArrayAdapter<RowData> arrAdapOutStation = new ArrayAdapter<>(requireContext(),
				R.layout.simple_spinner_item, arrOutStation);
			arrAdapOutStation.setDropDownViewResource(
				R.layout.mdropdown);
			spOutStaition.setAdapter(arrAdapOutStation);
			spOutStaition.setOnItemSelectedListener(
				selectOutStationListener);
			arrAdapOutStation.notifyDataSetChanged();
		}
	}

	private void outStation() {

		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId),
					OUTSTATIONMEETING,
					null, null);
			Disposable subscribe = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(this::renderOutStation);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderActionables(UiModel<List<LookupItem>> uiModel,
		String id) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		List<LookupItem> data = uiModel.data;
		if (data != null) {
			arrActionables.clear();
			arrActionables.add(new RowData(0, "-1", SELECT_ACTIONABLES));
			for (int i = 0; i < data.size(); i++) {
				LookupItem item = data.get(i);
				String lookUpId = stringOrEmpty(String.valueOf(item.lookUpid));
				String lookUpName = stringOrEmpty(item.lookUpName);
				arrActionables.add(
					new RowData(i + 1, lookUpId, lookUpName));
			}
			ArrayAdapter<RowData> arrAdapActionables = new ArrayAdapter<>(requireContext(),
				R.layout.simple_spinner_item, arrActionables);
			arrAdapActionables.setDropDownViewResource(R.layout.mdropdown);
			spActionables.setAdapter(arrAdapActionables);
			spActionables.setOnItemSelectedListener(selectActionablesListener);
			arrAdapActionables.notifyDataSetChanged();
			RowData actionablesData = getSpinnerRowDataById(id, arrAdapActionables);
			if (actionablesData != null) {
				spActionables.setSelection(
					actionablesData.getPosition());
			}
		}
	}

	private void actionables(final String id) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			category(CategoryID);
			LookupRequest request =
				new LookupRequest(Objects.requireNonNull(
					Objects.requireNonNull(
						userManager.getUser()).currentBusinessProfile.businessUnitId), ACTIONABLES,
					null, null);
			Disposable subscribe = getSaathiApi().getAllLookup(request)
				.compose(applyUiModel())
				.subscribe(uiModelList -> {
					renderActionables(uiModelList, id);
				});
		} else {
			noInternetConnectionDialog();
		}
	}

	private void dialogAddedSplAttendee() {
		final Dialog dialogAddedSplAttendee = new Dialog(requireContext());
		dialogAddedSplAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Objects.requireNonNull(dialogAddedSplAttendee.getWindow())
			.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialogAddedSplAttendee.setContentView(R.layout.dialog_addedd_spl_attendee_list);
		TextView tvEmpty = dialogAddedSplAttendee.findViewById(R.id.tvEmpty);
		dialogAddedSplAttendee.getWindow()
			.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dialogAddedSplAttendee.setCanceledOnTouchOutside(true);
		ListView lvAttendee = dialogAddedSplAttendee.findViewById(R.id.lvAttendee);
		lvAttendee.setEmptyView(tvEmpty);
		AddedSplAttendeeAdapter addedSplAttendeeAdapter =
			new AddedSplAttendeeAdapter(getActivity(), addedSplAttendeeModelList);
		lvAttendee.setAdapter(addedSplAttendeeAdapter);
		addedSplAttendeeAdapter.notifyDataSetChanged();
		dialogAddedSplAttendee.show();
	}

	private void dialogAddedAttendee() {
		final Dialog dialogAddedAttendee = new Dialog(requireContext());
		dialogAddedAttendee.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Objects.requireNonNull(dialogAddedAttendee.getWindow())
			.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialogAddedAttendee.setContentView(R.layout.dialog_addedd_attendee_list);
		TextView tvEmpty = dialogAddedAttendee.findViewById(R.id.tvEmpty);
		dialogAddedAttendee.getWindow()
			.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dialogAddedAttendee.setCanceledOnTouchOutside(true);
		ListView lvAttendee = dialogAddedAttendee.findViewById(R.id.lvAttendee);
		lvAttendee.setEmptyView(tvEmpty);
		AddedAttendeeAdapter addedAttendeeAdapter =
			new AddedAttendeeAdapter(getActivity(), addedAttendeeModelList);
		lvAttendee.setAdapter(addedAttendeeAdapter);
		addedAttendeeAdapter.notifyDataSetChanged();
		dialogAddedAttendee.show();
	}

	private void dialogAttendee() {
		final Dialog dialogAttendeeList = new Dialog(requireContext());
		dialogAttendeeList.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Objects.requireNonNull(dialogAttendeeList.getWindow())
			.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialogAttendeeList.setContentView(R.layout.dialog_attendee_list);
		dialogAttendeeList.getWindow()
			.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dialogAttendeeList.setCanceledOnTouchOutside(true);
		final DelayAutoCompleteTextView etSearchAttendee =
			dialogAttendeeList.findViewById(R.id.etSearchAttendee);
		etSearchAttendee
			.setImeOptions(EditorInfo.IME_ACTION_DONE);
		etSearchAttendee.setOnEditorActionListener(this);
		etSearchAttendee.setThreshold(3);
		attendeeAdapter = new AttendeeAdapter(requireContext());
		etSearchAttendee.setAdapter(attendeeAdapter);
		etSearchAttendee.setLoadingIndicator(
			dialogAttendeeList.findViewById(R.id.pbLoadingIndicator));
		etSearchAttendee.setOnItemClickListener((parent, view, position, id) -> {

			EmployeeModel clientModel = attendeeAdapter.getItem(position);
			boolean has = listHasAttende(addedAttendeeModelList, addedSplAttendeeModelList,
				clientModel.getAttendeeCode());
			if (!has) {
				if (clientModel.isNoData()) {
					return;
				}
				String attendeeType = "R"; //todo //R=Regular  & S=special
				addedAttendeeModelList.add(
					new AddedAttendeeModel(attendeeType, true, clientModel.getAttendeeName(),
						clientModel.getAttendeeCode()));
				addedAttendeeCount++;
				setCount(addedAttendeeCount);
				dialogAttendeeList.dismiss();
			} else {
				Toast.makeText(requireContext(), ATTENDEE_ALREADY_ADDED, Toast.LENGTH_SHORT)
					.show();
				etSearchAttendee.setText("");
			}
		});

		dialogAttendeeList.show();
	}

	private void dialogSplAttendee() {
		final Dialog dialogSplAttendeeList = new Dialog(requireContext());
		dialogSplAttendeeList.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Objects.requireNonNull(dialogSplAttendeeList.getWindow())
			.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialogSplAttendeeList.setContentView(R.layout.dialog_attendee_list);
		dialogSplAttendeeList.getWindow()
			.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		dialogSplAttendeeList.setCanceledOnTouchOutside(true);

		final DelayAutoCompleteTextView etSearchAttendee =
			dialogSplAttendeeList.findViewById(R.id.etSearchAttendee);
		etSearchAttendee
			.setImeOptions(EditorInfo.IME_ACTION_DONE);
		etSearchAttendee.setOnEditorActionListener(this);
		etSearchAttendee.setThreshold(3);
		splAttendeeAdapter = new SplAttendeeAdapter(requireContext());
		etSearchAttendee.setAdapter(splAttendeeAdapter);
		etSearchAttendee.setLoadingIndicator(
			dialogSplAttendeeList.findViewById(R.id.pbLoadingIndicator));
		etSearchAttendee.setOnItemClickListener((parent, view, position, id) -> {

			SplAttendeeModel clientModel = splAttendeeAdapter.getItem(position);
			boolean has = listHasAttende(addedAttendeeModelList, addedSplAttendeeModelList,
				clientModel.getAttendeeCode());

			if (!has) {
				if (clientModel.isNoData()) {
					return;
				}
				String attendeeType = "S"; //todo //R=Regular  & S=special
				addedSplAttendeeModelList.add(
					new AddedSplAttendeeModel(attendeeType, true, clientModel.getAttendeeName(),
						clientModel.getAttendeeCode()));
				addedSplAttendeeCount++;
				tvSplAttendeeCount.setText(
					String.format("(%s)", String.valueOf(addedSplAttendeeModelList.size())));
				dialogSplAttendeeList.dismiss();
			} else {
				Toast.makeText(requireContext(), SPL_ATTENDEE_ALREADY_ADDED,
					Toast.LENGTH_SHORT).show();
				etSearchAttendee.setText("");
			}
		});

		dialogSplAttendeeList.show();
	}

	private void updateMeetingSave(MeetingApptModel meetingApptModel) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			RequestBody request =
				RequestBody.create(MediaType.parse("text/plain"), meetingApptModel.getJson());
			Disposable subscribe = getSaathiApi().saveUpdateMeeting(request)
				.compose(applyUiModel())
				.subscribe(this::renderUpdateMeetingSave);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderUpdateMeetingSave(UiModel<Object> uiModel) {
		toggleProgress(uiModel.inProgress);
		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		Object response = uiModel.data;
		if (response != null) {
			alertDialog(getActivity(), 0,
				getResources().getString(R.string.motilal_oswal),
				UPDATED_MEETING, "OK", true,
				(action, data) -> {
					if (action == MSFDialog.Action.OK) {
						Bundle bundle = new Bundle();
						bundle.putString(LEAD_ID,
							Objects.requireNonNull(getArguments()).getString(LEAD_ID));
						bundle.putString(UCID,
							getArguments().getString(UCID));
						navigator.openPage(NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true);
					}
				});
		}
	}

	@Override
	public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
		if (view instanceof DelayAutoCompleteTextView) {
			hideKeyboard(view);
			return false;
		}
		if ((createMeetingView.findViewById(view.getId()))
			== (createMeetingView.findViewById(R.id.etMeetingDescription))) {
			etCustomerComment.requestFocus();
		}
		if ((createMeetingView.findViewById(view.getId()))
			== (createMeetingView.findViewById(R.id.etCustomerComment))) {
			if ((llActionables.getVisibility() == View.VISIBLE)) {
				etCategory.requestFocus();
			}
		}
		if ((createMeetingView.findViewById(view.getId()))
			== (createMeetingView.findViewById(R.id.etCategory))) {
			etSubCategory.requestFocus();
		}
		return false;
	}

	//Attende count
	private void setCount(int cnt) {
		if (cnt <= 0) {
			tvAttendeeCount.setText("");
		} else {
			tvAttendeeCount.setText(String.format("(%s)", String.valueOf(cnt)));
		}
	}

	//AttendeForSpecial  count
	private void setCountForSpecial(int cnt) {
		if (cnt <= 0) {
			tvSplAttendeeCount.setText("");
		} else {
			tvSplAttendeeCount.setText(String.format("(%s)", String.valueOf(cnt)));
		}
	}

	public class AttendeeAdapter extends BaseAdapter implements Filterable {
		Context context;
		private LayoutInflater inflater;
		private List<EmployeeModel> resultList = new ArrayList<>();

		public AttendeeAdapter(Context context) {
			this.inflater = LayoutInflater.from(context);
			this.context = context;
		}

		public void clearAll() {
			resultList.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public EmployeeModel getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final AttendeeAdapter.HolderView holderView;
			final EmployeeModel clientModel = resultList.get(position);
			if (convertView == null) {
				inflater =
					(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_list_attendee, parent, false);
				holderView = new AttendeeAdapter.HolderView(convertView);
				convertView.setTag(holderView);
			} else {
				holderView = (AttendeeAdapter.HolderView) convertView.getTag();
			}
			viewColor(position, convertView);
			if (clientModel.isNoData()) {
				holderView.tvAttendeeName.setText(clientModel.getAttendeeName());
				holderView.linaerLayout.setOnClickListener(null);
			} else {
				holderView.tvAttendeeName.setText(
					String.format("%s-%s-%s", clientModel.getAttendeeCode(),
						clientModel.getAttendeeName(), clientModel.getBranchcode()));
			}
			return convertView;
		}

		@Override
		public Filter getFilter() {
			return new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					final FilterResults filterResults = new FilterResults();
					ArrayList<EmployeeModel> modelList = new ArrayList<>();
					try {
						if (constraint != null) {
							if (CRMAppUtil.isNetworkEnabled(requireContext())) {
								AutoCompleteAttendeeRequest request =
									new AutoCompleteAttendeeRequest(
										URLEncoder.encode(constraint.toString(), "utf-8"),
										URLEncoder.encode(constraint.toString(), "utf-8")
									);
								try {
									List<AutoCompleteAttendeeItem> dataList =
										getSaathiApi().getAutoCompleteAttendee(request)
											.blockingFirst();
									for (AutoCompleteAttendeeItem item : dataList) {
										String employeeName = stringOrEmpty(item.employeeName);
										String employeeCode = stringOrEmpty(item.employeeCode);
										String BranchCode = stringOrEmpty(item.branchCode);
										if (employeeCode.equalsIgnoreCase(
											Objects.requireNonNull(
												userManager.getUser()).employeeCode)) {
											continue;
										}
										modelList.add(new EmployeeModel(employeeName, employeeCode,
											BranchCode, false));
										filterResults.values = modelList;
										filterResults.count = modelList.size();
									}
								} catch (Exception e) {
									modelList.add(EmployeeModel.getNoData(getErrorOnFailureNew(e)));
									filterResults.values = modelList;
									filterResults.count = modelList.size();
									return filterResults;
								}
							} else {
								noInternetConnectionDialog();
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					if (filterResults.count == 0) {
						modelList.add(EmployeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE));
						filterResults.values = modelList;
						filterResults.count = modelList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						resultList = (List<EmployeeModel>) results.values;
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
		}

		private void viewColor(int position, View convertView) {
			if (position % 2 == 1) {
				convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));
			} else {
				convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
		}

		private class HolderView {
			TextView tvAttendeeName;
			LinearLayout linaerLayout;

			private HolderView(View parent) {
				tvAttendeeName = parent.findViewById(R.id.tvAttendeeName);
				linaerLayout = parent.findViewById(R.id.linaerLayout);
			}
		}
	}

	public class SplAttendeeAdapter extends BaseAdapter implements Filterable {
		Context mContext;
		private LayoutInflater inflater;
		private List<SplAttendeeModel> attendeeModelList = new ArrayList<>();

		private SplAttendeeAdapter(Context c) {
			inflater = LayoutInflater.from(c);
			mContext = c;
		}

		@Override
		public int getCount() {
			return attendeeModelList.size();
		}

		@Override
		public SplAttendeeModel getItem(int position) {
			return attendeeModelList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final SplAttendeeAdapter.HolderView holderView;
			final SplAttendeeModel clientModel = attendeeModelList.get(position);
			if (convertView == null) {
				inflater =
					(LayoutInflater) requireContext().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_list_attendee, parent, false);
				holderView = new SplAttendeeAdapter.HolderView(convertView);
				convertView.setTag(holderView);
			} else {
				holderView = (SplAttendeeAdapter.HolderView) convertView.getTag();
			}
			viewColor(position, convertView);
			if (clientModel.isNoData()) {
				holderView.tvAttendeeName.setText(clientModel.getAttendeeName());
				holderView.linaerLayout.setOnClickListener(null);
			} else {
				holderView.tvAttendeeName.setText(
					String.format("%s-%s-%s", clientModel.getAttendeeCode(),
						clientModel.getAttendeeName(), clientModel.getBranchCode()));
			}

			return convertView;
		}

		@Override
		public Filter getFilter() {
			return new Filter() {
				final Filter.FilterResults filterResults = new Filter.FilterResults();

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {

					ArrayList<SplAttendeeModel> modelList = new ArrayList<>();
					try {
						if (constraint != null) {
							if (CRMAppUtil.isNetworkEnabled(requireContext())) {
								AutoCompleteAttendeeRequest request =
									new AutoCompleteAttendeeRequest(
										URLEncoder.encode(constraint.toString(), "utf-8"),
										URLEncoder.encode(constraint.toString(), "utf-8")
									);
								try {
									List<AutoCompleteAttendeeItem> dataList =
										getSaathiApi().getAutoCompleteSplAttendee(request)
											.blockingFirst();
									for (AutoCompleteAttendeeItem item : dataList) {
										String employeeName = stringOrEmpty(item.employeeName);
										String employeeCode = stringOrEmpty(item.employeeCode);
										String BranchCode = stringOrEmpty(item.branchCode);
										modelList.add(
											new SplAttendeeModel(employeeName, employeeCode,
												BranchCode));
										filterResults.values = modelList;
										filterResults.count = modelList.size();
									}
								} catch (Exception e) {
									modelList.add(
										SplAttendeeModel.getNoData(getErrorOnFailureNew(e)));
									filterResults.values = modelList;
									filterResults.count = modelList.size();
								}
							} else {
								noInternetConnectionDialog();
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					if (filterResults.count == 0) {
						modelList.add(SplAttendeeModel.getNoData(ATTENDEEE_NOT_FOUND_MESSAGE));
						filterResults.values = modelList;
						filterResults.count = modelList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					if (results != null && results.count > 0) {
						attendeeModelList = (List<SplAttendeeModel>) results.values;
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
		}

		private void viewColor(int position, View convertView) {
			if (position % 2 == 1) {
				convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));
			} else {
				convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
		}

		class HolderView {
			TextView tvAttendeeName;
			LinearLayout linaerLayout;

			private HolderView(View parent) {
				tvAttendeeName = parent.findViewById(R.id.tvAttendeeName);
				linaerLayout = parent.findViewById(R.id.linaerLayout);
			}
		}
	}

	public class AddedAttendeeAdapter extends BaseAdapter {
		private final LayoutInflater inflater;
		Context mContext;
		List<AddedAttendeeModel> attendeeModelList;

		private AddedAttendeeAdapter(Context c, List<AddedAttendeeModel> attendeeModelList) {
			inflater = LayoutInflater.from(c);
			mContext = c;
			this.attendeeModelList = attendeeModelList;
		}

		@Override
		public int getCount() {
			return attendeeModelList.size();
		}

		@Override
		public Object getItem(int position) {
			return attendeeModelList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ViewHolder") @Override
		public View getView(final int position, View view, ViewGroup parent) {
			AddedAttendeeAdapter.Holder holder = new AddedAttendeeAdapter.Holder();
			view = inflater.inflate(R.layout.item_list_added_attendee, parent, false);

			AddedAttendeeModel attendeeModel = attendeeModelList.get(position);

			holder.tvAttendeeName = view.findViewById(R.id.tvAttendeeName);
			holder.ivDelete = view.findViewById(R.id.ivDelete);

			holder.tvAttendeeName.setText(
				String.format("%s-%s", attendeeModel.getAttendeeEmplyeeCode(),
					attendeeModel.getAttendeeName()));
			holder.ivDelete.setOnClickListener(v -> {
				addedAttendeeCount--;
				setCount(addedAttendeeCount);
				attendeeModelList.remove(position);
				notifyDataSetChanged();
			});

			return view;
		}

		class Holder {
			TextView tvAttendeeName;
			ImageView ivDelete;
		}
	}

	public class AddedSplAttendeeAdapter extends BaseAdapter {
		private final LayoutInflater inflater;
		Context mContext;
		List<AddedSplAttendeeModel> attendeeModelList;

		private AddedSplAttendeeAdapter(Context c, List<AddedSplAttendeeModel> attendeeModelList) {
			inflater = LayoutInflater.from(c);
			mContext = c;
			this.attendeeModelList = attendeeModelList;
		}

		@Override
		public int getCount() {
			return attendeeModelList.size();
		}

		@Override
		public Object getItem(int position) {
			return attendeeModelList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ViewHolder") @Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			AddedSplAttendeeAdapter.Holder holder = new AddedSplAttendeeAdapter.Holder();
			view = inflater.inflate(R.layout.item_list_added_spl_attendee, parent, false);

			AddedSplAttendeeModel attendeeModel = attendeeModelList.get(position);

			holder.tvAttendeeName = view.findViewById(R.id.tvAttendeeName);
			holder.ivDelete = view.findViewById(R.id.ivDelete);

			holder.tvAttendeeName.setText(
				String.format("%s-%s", attendeeModel.getAttendeeEmplyeeCode(),
					attendeeModel.getAttendeeName()));
			holder.ivDelete.setOnClickListener(v -> {
				addedSplAttendeeCount--;
				setCountForSpecial(addedSplAttendeeCount);
				attendeeModelList.remove(position);
				notifyDataSetChanged();
			});

			return view;
		}

		class Holder {
			TextView tvAttendeeName;
			ImageView ivDelete;
		}
	}
}
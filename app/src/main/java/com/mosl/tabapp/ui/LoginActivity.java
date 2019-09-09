package com.mosl.tabapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.mosl.app.core.data.MOSLResponseWrapper;
import com.mosl.app.core.data.UiModel;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.saathi.api.model.common.SaathiCommonResponse;
import com.mosl.saathi.api.model.login.LoginRequest;
import com.mosl.saathi.api.model.login.LoginResponse;
import com.mosl.saathi.api.model.login.User;
import com.mosl.saathi.api.model.splash.ChildMenu;
import com.mosl.saathi.api.model.splash.ReportTypeResponse;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.LoginResponseData;
import com.mosl.tabapp.model.MenuReport;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import io.reactivex.disposables.Disposable;
import java.util.List;

import static com.mosl.app.core.data.UiModelHelper.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;
import static com.mosl.tabapp.common.CRMMOSLCommon.getDeviceID;
import static com.mosl.tabapp.common.CRMMOSLCommon.setPermisson;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 2/1/2017.
 */
public class LoginActivity extends FragmentActivity
	implements MOSLConstants, TextView.OnEditorActionListener, View.OnClickListener {
	private Button btnLogin;
	private EditText etEmpCode, etPassword;
	private LinearLayout customProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setPermisson(LoginActivity.this, getApplicationContext());
		btnLogin = findViewById(R.id.btnLogin);
		etEmpCode = findViewById(R.id.etEmpCode);
		etPassword = findViewById(R.id.etPassword);
		etPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
		etPassword.setOnEditorActionListener(this);
		customProgress = findViewById(R.id.customProgress);

		String empCode = getPrefs(this).getString("EmployeeCode", null);
		if (!CRMAppUtil.isBlank(empCode)) {
			etEmpCode.setText(empCode.trim());
		}
		btnLogin.setOnClickListener(LoginActivity.this);
	}

	private boolean validateForm() {
		if (etEmpCode.getText().toString().equalsIgnoreCase("")) {
			alertDialog(this, 0, getResources().getString(R.string.motilal_oswal),
				"Employee code cannot be left blank", OK, true, null);
			return false;
		} else if (etPassword.getText().toString().equalsIgnoreCase("")) {
			alertDialog(this, 0, getResources().getString(R.string.motilal_oswal),
				"Password cannot be left blank", OK, true, null);
			return false;
		} else {
			return true;
		}
	}

	public void performLogin(final String empCode, final String password) {
		if (CRMAppUtil.isNetworkEnabled(this)) {
			LoginRequest request =
				new LoginRequest(empCode, password, CRMAppUtil.getLocalIpAddress(), CLIENT_INFO,
					CRMMOSLCommon.getDeviceID(getApplicationContext(), getContentResolver()),
					"2");
			Disposable subscribe = CRMWebServiceHandler.getSaathiApi().login(request)
				.compose(applyUiModel())
				.subscribe(this::renderLogin);
		} else {
			noInternetDialog();
		}
	}

	public String stringOrEmpty(@Nullable String input) {
		if (input != null) {
			return input;
		} else {
			return "";
		}
	}

	private void toggleProgress(boolean show) {
		if (show) {
			customProgress.setVisibility(View.VISIBLE);
		} else {
			customProgress.setVisibility(View.GONE);
		}
	}

	public void showOnFailurePopup(Throwable t) {
		String message;
		if (t instanceof ServiceFailureException) {
			message = ((ServiceFailureException) t).errorMessage;
		} else {
			message = MOSLResponseWrapper.SERVER_ERROR_OCCURRED;
		}
		MSFDialog.alertDialog(LoginActivity.this, 0,
			getResources().getString(R.string.motilal_oswal),
			message, "OK", true, null);
	}

	private void renderLogin(UiModel<LoginResponse> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}

		LoginResponse data = uiModel.data;
		if (data != null) {
			boolean smtEnabled = data.isSMTEnabled();
			boolean ekycRevamp = data.isEkycRevamp();
			User user = data.user;
			String userId = stringOrEmpty(user.userId);
			String employeeCode = stringOrEmpty(user.employeeCode);
			String firstName = stringOrEmpty(user.firstName);
			String lastName = stringOrEmpty(user.lastName);
			String imageUrl = stringOrEmpty(user.imageUrl);
			String mobile = stringOrEmpty(user.mobile);
			String token = stringOrEmpty(user.token);
			String email = stringOrEmpty(user.email);
			String userTypeSequence = stringOrEmpty(user.userTypeSequence);

			String branchCode = stringOrEmpty(user.defaultBusinessProfile.branchCode);
			String businessProfileId = stringOrEmpty(user.currentBusinessProfile.businessProfileId);
			String businessProfileName = stringOrEmpty(user.currentBusinessProfile.name);
			String businessUnitId = stringOrEmpty(user.currentBusinessProfile.businessUnitId);
			boolean aufsb = userTypeSequence.equals("9");

			LoginResponseData loginData = new LoginResponseData();
			loginData.setUserId(userId);
			loginData.setEmployeeCode(employeeCode.trim());
			loginData.setFirstName(firstName.trim());
			loginData.setLastName(lastName.trim());
			loginData.setImageUrl(imageUrl.trim());
			loginData.setMobileNo(mobile.trim());
			loginData.setEmail(email.trim());
			loginData.setToken(token.trim());
			loginData.setBusinessProfileId(businessProfileId.trim());
			loginData.setBusinessProfileName(
				businessProfileName.trim());
			loginData.setBusinessUnitId(businessUnitId.trim());
			loginData.setBranchCode(branchCode.trim());
			loginData.setUserType(userTypeSequence.trim());
			loginData.setSmtEnabled(smtEnabled);
			loginData.setAufsb(aufsb);
			loginData.setEkycRevemp(ekycRevamp);

			storeLoginPrefData(loginData);
		}
	}

	private void noInternetDialog() {
		customProgress.setVisibility(View.GONE);
		MSFDialog.alertDialog(LoginActivity.this, 0,
			getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, OK, true,
			null);
	}

	private void storeLoginPrefData(LoginResponseData loginData) {
		SharedPreferences.Editor editor = getPrefs(this).edit();
		editor.putString("PASSWORD", etPassword.getText().toString().trim());
		editor.putString("EmployeeCode", loginData.getEmployeeCode().trim());
		editor.putString("token", loginData.getToken());
		editor.putString("userid", loginData.getUserId());
		editor.putString("ipaddress", CRMAppUtil.getLocalIpAddress());
		editor.putString("BusinessUnitId", loginData.getBusinessUnitId());
		editor.putString("macaddress", getDeviceID(getApplicationContext(), getContentResolver()));

		editor.apply();
		new AccountDetails(loginData.getUserId(), loginData.getFirstName(), loginData.getLastName(),
			loginData.getImageUrl().replaceAll(" ", "%20"), loginData.getMobileNo(),
			loginData.getEmail(), loginData.getBusinessProfileId(),
			loginData.getBusinessProfileName(), loginData.getEmployeeCode(), loginData.getToken(),
			loginData.getBusinessUnitId(), loginData.getBranchCode(), loginData.getUserType(),
			loginData.isSmtEnabled(), loginData.isAufsb(), loginData.isEkycRevemp());
		try {
			MOSLCommonActivity.sendRegistrationToServer(LoginActivity.this, getContentResolver(),
				false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		editor.apply();
		if (loginData.isAufsb()) {
			openMainActivity();
		} else {
			FetchReportTypes();
		}
	}

	private void renderReportData(UiModel<SaathiCommonResponse<List<ReportTypeResponse>>> uiModel) {
		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}

		SaathiCommonResponse<List<ReportTypeResponse>> commonResponse = uiModel.data;
		if (commonResponse != null) {
			List<ReportTypeResponse> response = commonResponse.response;
			if (response != null) {
				if (response.size() > 0) {
					for (int i = 0; i < response.size(); i++) {
						ReportTypeResponse reportTypeResponse = response.get(i);
						if (reportTypeResponse.isPreDefinedReports()) {
							List<ChildMenu> childMenuList = reportTypeResponse.childMenuList;
							for (int j = 0; j < childMenuList.size(); j++) {
								ChildMenu childMenu = childMenuList.get(i);
								if (childMenu.isBasicReports()) {
									List<ChildMenu> subChildMenuList = childMenu.ChildMenuList;
									for (int k = 0; k < subChildMenuList.size(); k++) {
										ChildMenu subChildMenu = subChildMenuList.get(k);
										String systemName = stringOrEmpty(subChildMenu.systemName);
										String name = stringOrEmpty(subChildMenu.name);
										SplashActivity.menuReportArrayList.add(
											new MenuReport(name, systemName));
									}
								}
							}
						}
					}
					Intent intent = new Intent(LoginActivity.this,
						MOSLCommonActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(LoginActivity.this,
						MOSLCommonActivity.class);
					startActivity(intent);
					finish();
					alertDialog(getApplicationContext(), 0,
						getResources().getString(R.string.motilal_oswal),
						commonResponse.message, "OK", true, null);
				}
			}
		}
	}

	public void FetchReportTypes() {
		if (CRMAppUtil.isNetworkEnabled(getApplicationContext())) {
			SplashActivity.menuReportArrayList.clear();
			Disposable subscribe = CRMWebServiceHandler.getSaathiApi().fetchReportTypes()
				.compose(applyUiModel())
				.subscribe(this::renderReportData);
		} else {
			noInternetDialog();
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		loginAction();
		return false;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btnLogin) {
			loginAction();
		}
	}

	private void loginAction() {
		if (validateForm()) {
			performLogin(etEmpCode.getText().toString().trim(),
				etPassword.getText().toString().trim());
		}
	}

	private void openMainActivity() {
		Intent intent =
			new Intent(LoginActivity.this, MOSLCommonActivity.class);
		startActivity(intent);
		finish();
	}
}

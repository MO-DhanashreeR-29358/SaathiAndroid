package com.mosl.tabapp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import com.mosl.app.core.data.UiModel;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.saathi.api.model.common.SaathiCommonResponse;
import com.mosl.saathi.api.model.login.LoginRequest;
import com.mosl.saathi.api.model.login.LoginResponse;
import com.mosl.saathi.api.model.login.User;
import com.mosl.saathi.api.model.splash.ChildMenu;
import com.mosl.saathi.api.model.splash.ReportTypeResponse;
import com.mosl.saathi.api.model.splash.VersionRequest;
import com.mosl.saathi.api.model.splash.VersionResponse;
import com.mosl.saathi.data.MOSLResponseWrapper;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.MenuReport;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

import static com.mosl.app.core.data.UiModelHelper.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;
import static com.mosl.tabapp.common.CRMMOSLCommon.getAppVersionCode;
import static com.mosl.tabapp.model.AccountDetails.setEkycUrl;
import static com.mosl.tabapp.model.AccountDetails.setNewEkyc;
import static com.mosl.tabapp.model.AccountDetails.setUserType;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class SplashActivity extends FragmentActivity implements MOSLConstants {
	public static ArrayList<MenuReport> menuReportArrayList = new ArrayList<>();

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		// Splash screen timer
		int SPLASH_TIME_OUT = 3000;
		new Handler().postDelayed(() -> {
			setUserType("");
			versionCheck();
		}, SPLASH_TIME_OUT);
	}

	private void renderLogin(UiModel<LoginResponse> uiModel) {
		//toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			Timber.d("error %s", error.getMessage());
			handleLoginError();
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

			new AccountDetails(userId,
				firstName, lastName,
				imageUrl.replaceAll(" ", "%20"),
				mobile, email,
				businessProfileId,
				businessProfileName,
				employeeCode, token,
				businessUnitId,
				branchCode, userTypeSequence,
				smtEnabled, aufsb, ekycRevamp);

			if (aufsb) {
				openMainActivity();
			} else {
				FetchReportTypes();
			}
		}
	}

	public String stringOrEmpty(@Nullable String input) {
		if (input != null) {
			return input;
		} else {
			return "";
		}
	}

	private void handleStartupPage() {
		String empCode = getPrefs(this).getString("EmployeeCode", null);
		String password = getPrefs(this).getString("PASSWORD", null);

		if (empCode != null && password != null) {
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
		} else {
			handleLoginError();
		}
	}

	public void FetchReportTypes() {
		if (CRMAppUtil.isNetworkEnabled(getApplicationContext())) {
			Disposable subscribe = CRMWebServiceHandler.getSaathiApi().fetchReportTypes()
				.compose(applyUiModel())
				.subscribe(this::renderReportData);
		} else {
			noInternetDialog();
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
					Intent intent = new Intent(SplashActivity.this,
						MOSLCommonActivity.class);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(SplashActivity.this,
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

	private void noInternetDialog() {
		MSFDialog.alertDialog(this, 0, getResources().getString(R.string.motilal_oswal),
			INTERNET_CONNECTION, "OK", false, (action, data) -> {
				if (action == MSFDialog.Action.OK) {
					finish();
				}
			});
	}

	private void handleLoginError() {
		clearLoginPrefData();
		Intent i = new Intent(SplashActivity.this, LoginActivity.class);
		startActivity(i);
		finish();
	}

	@SuppressLint("CommitPrefEdits") private void clearLoginPrefData() {

		getPrefs(this).edit().remove("EmployeeCode");
		getPrefs(this).edit().remove("PASSWORD");
	}

	private void versionCheck() {
		if (CRMAppUtil.isNetworkEnabled(SplashActivity.this)) {
			VersionRequest request =
				new VersionRequest(NEW_CRM, getAppVersionCode(SplashActivity.this), "Android", "",
					CRMMOSLCommon.getDeviceID(SplashActivity.this, getContentResolver()));
			Disposable subscribe = CRMWebServiceHandler.getSaathiApi().appVersion(request)
				.compose(applyUiModel())
				.subscribe(this::renderStartup);
		} else {
			noInternetDialog();
		}
	}

	public void showOnFailurePopup(Throwable t) {
		String message;
		if (t instanceof ServiceFailureException) {
			message = ((ServiceFailureException) t).errorMessage;
		} else {
			message = MOSLResponseWrapper.SERVER_ERROR_OCCURRED;
		}
		MSFDialog.alertDialog(SplashActivity.this, 0,
			getResources().getString(R.string.motilal_oswal),
			message, "OK", true, null);
	}

	private void renderStartup(UiModel<List<VersionResponse>> uiModel) {
		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}

		List<VersionResponse> data = uiModel.data;
		if (data != null && !data.isEmpty()) {
			VersionResponse response = data.get(0);
			int statusCode = response.statusCode;
			String url;
			if (response.hasUrl()) {
				url = response.url;
			} else {
				url = GOOGLE_PLAY_URL;
			}

			String newEkycUrl = stringOrEmpty(response.newEkycUrl);
			setEkycUrl(newEkycUrl.replaceAll(" ", "%20"));
			if (response.isNewEkyc()) {
				setNewEkyc(true);
			} else {
				setNewEkyc(false);
			}

			String msg = stringOrEmpty(response.msg);
			if (statusCode == 1) {
				handleStartupPage();
			} else if (statusCode == 0) {
				MSFDialog.alertDialog(SplashActivity.this, 0,
					getResources().getString(
						R.string.motilal_oswal), msg, "Update",
					false, new MSFDialog.DialogListener() {
						@Override public void alertDialogAction(
							MSFDialog.Action action,
							Object... data) {
							if (action == MSFDialog.Action.OK) {
								Intent i = new Intent(
									android.content.Intent.ACTION_VIEW);
								i.setData(
									Uri.parse(url));
								startActivity(i);
								finish();
							}
						}
					});
			} else if (statusCode == 3) {
				MSFDialog.alertDialog(SplashActivity.this, 0,
					getResources().getString(
						R.string.motilal_oswal), msg, "Update",
					"Continue", false,
					new MSFDialog.DialogListener() {
						@Override public void alertDialogAction(
							MSFDialog.Action action,
							Object... data) {
							if (action == MSFDialog.Action.OK) {
								Intent i = new Intent(
									android.content.Intent.ACTION_VIEW);
								i.setData(
									Uri.parse(url));
								startActivity(i);
								finish();
							} else if (action
								== MSFDialog.Action.CANCEL) {
								handleStartupPage();
							}
						}
					});
			} else if (statusCode == 2) {
				MSFDialog.alertDialog(SplashActivity.this, 0,
					getResources().getString(
						R.string.motilal_oswal), msg, "Ok",
					false, new MSFDialog.DialogListener() {
						@Override public void alertDialogAction(
							MSFDialog.Action action,
							Object... data) {
							if (action == MSFDialog.Action.OK) {
								finish();
							}
						}
					});
			}
		}
	}


	private void openMainActivity() {
		Intent intent =
			new Intent(SplashActivity.this, MOSLCommonActivity.class);
		startActivity(intent);
		finish();
	}
}

package com.mosl.tabapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.model.RoleSelectionModel;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.LoginResponseData;
import com.mosl.tabapp.model.MenuReport;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mosl.mobile.ekyc.constants.MOSLConstants.CRM;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppType;
import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;
import static com.mosl.tabapp.common.CRMMOSLCommon.getDeviceID;
import static com.mosl.tabapp.common.CRMMOSLCommon.setPermisson;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 2/1/2017.
 */
public class LoginActivity extends FragmentActivity
	implements MOSLConstants, TextView.OnEditorActionListener, View.OnClickListener {
	Button btnLogin;
	EditText etEmpCode, etPassword;
	LinearLayout customProgress;

	public static Map getHeader(Context context) {
		Map headerMap = new HashMap<>();
		headerMap.put("Content-Type", "application/json; charset=UTF-8");
		headerMap.put("token", AccountDetails.getToken());
		headerMap.put("userid", AccountDetails.getUserId());
		headerMap.put("clientinfo", "android");
		headerMap.put("ipaddress", CRMAppUtil.getLocalIpAddress());
		headerMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());
		headerMap.put("macaddress",
			CRMMOSLCommon.getDeviceID(context, context.getContentResolver()));
		headerMap.put("EmployeeCode", AccountDetails.getEmployeeCode());
		headerMap.put("AppId", "2");
		return headerMap;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setPermisson(LoginActivity.this, getApplicationContext());
		btnLogin = (Button) findViewById(R.id.btnLogin);
		etEmpCode = (EditText) findViewById(R.id.etEmpCode);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
		etPassword.setOnEditorActionListener(this);
		customProgress = (LinearLayout) findViewById(R.id.customProgress);

		String empCode = getPrefs(this).getString("EmployeeCode", null);
		if (!CRMAppUtil.isBlank(empCode)) {
			etEmpCode.setText(empCode.trim());
		}
		setAppType(CRM);
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
		//login parameters

		if (CRMAppUtil.isNetworkEnabled(this)) {
			InputMethodManager imm =
				(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(etEmpCode.getWindowToken(), 0);
			customProgress.setVisibility(View.VISIBLE);
			Map inputMap = new HashMap<>();
			inputMap.put("EmployeeCode", empCode);
			inputMap.put("Password", password);
			inputMap.put("IPAddress", CRMAppUtil.getLocalIpAddress());
			inputMap.put("ClientInfo", CLIENT_INFO);
			inputMap.put("Macaddress", getDeviceID(getApplicationContext(), getContentResolver()));
			inputMap.put("AppId", "2");

			Map headerMap = null;
			String serviceMethod = "Auth/Login";

			try {
				CRMWebServiceHandler.post(this, serviceMethod, headerMap, inputMap,
					new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							//after success code goes here
							try {
								if (statusCode == 200) {

									String status = response.getString(STATUS);
									if (status.equalsIgnoreCase("true")) {
										customProgress.setVisibility(View.GONE);
										JSONObject objDetail = response.getJSONObject(RESPONSE);
										boolean smtEnabled =
											objDetail.getString(SMT_ENABLED).equals("1");
										boolean isEkycRevemp =
											objDetail.getString(ISEKYCREVAMP).equals("1");
										JSONObject objDetailUser = objDetail.getJSONObject(USER);
										String userId = objDetailUser.isNull(USER_ID) ? ""
											: objDetailUser.getString(USER_ID);
										String employeeCode = objDetailUser.isNull(EMPLOYEE_CODE)
											? "" : objDetailUser.getString(EMPLOYEE_CODE);
										String firstName = objDetailUser.isNull(FIRST_NAME) ? ""
											: objDetailUser.getString(FIRST_NAME);
										String lastName = objDetailUser.isNull(LAST_NAME) ? ""
											: objDetailUser.getString(LAST_NAME);
										String imageUrl = objDetailUser.isNull(IMAGE_URL) ? ""
											: objDetailUser.getString(IMAGE_URL);
										String mobile = objDetailUser.isNull(MOBILE) ? ""
											: objDetailUser.getString(MOBILE);
										String email = objDetailUser.isNull(EMAIL) ? ""
											: objDetailUser.getString(EMAIL);
										String token = objDetailUser.isNull(TOKEN) ? ""
											: objDetailUser.getString(TOKEN);
										String userType = objDetailUser.isNull(USER_TYPE_SEQ)
											? "" : objDetailUser.getString(USER_TYPE_SEQ);

										// getting Branch code from DEFAULT_BUSINESS_PROFILE
										JSONObject objDetailDefaultBusinessProfile =
											objDetailUser.getJSONObject(DEFAULT_BUSINESS_PROFILE);
										String branchCode = objDetailDefaultBusinessProfile.isNull(
											BRANCHCODE_DEFAULT_BUSINESS_PROFILE) ? ""
											: objDetailDefaultBusinessProfile.getString(
												BRANCHCODE_DEFAULT_BUSINESS_PROFILE);
										String rmId =
											objDetailDefaultBusinessProfile.isNull(USER_ID) ? ""
												: objDetailDefaultBusinessProfile.getString(
													USER_ID);

										JSONObject objDetailBusinessProfile =
											objDetailUser.getJSONObject(CURRENT_BUSINESS_PROFILE);
										String businessProfileId =
											objDetailBusinessProfile.isNull(BUSINESS_PROFILE_ID)
												? ""
												: objDetailBusinessProfile.getString(
													BUSINESS_PROFILE_ID);

										String businessProfileName =
											objDetailBusinessProfile.isNull(
												CURRENT_BUSINESS_PROFILE_NAME) ? ""
												: objDetailBusinessProfile.getString(
													CURRENT_BUSINESS_PROFILE_NAME);
										String businessUnitId =
											objDetailBusinessProfile.isNull(BUSINESS_UNIT_ID) ? ""
												: objDetailBusinessProfile.getString(
													BUSINESS_UNIT_ID);
										setAppLoginID(employeeCode);
										setAppType(CRM);

										boolean aufsb = userType.equals("9");
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
										loginData.setUserType(userType.trim());
										loginData.setUserType(userType.trim());
										loginData.setSmtEnabled(smtEnabled);
										loginData.setAufsb(aufsb);
										loginData.setEkycRevemp(isEkycRevemp);
										new RoleSelectionModel("", "", "", "", employeeCode.trim(),
											rmId.trim(), businessUnitId.trim(), "",/*roleId*/"");

										storeLoginPrefData(loginData);
									} else {
										customProgress.setVisibility(View.GONE);
										String error =
											CRMMOSLCommon.getErrorMsgFromResponse(response);
										alertDialog(LoginActivity.this, 0,
											getResources().getString(R.string.motilal_oswal), error,
											OK, true, null);
									}
								} else {
									customProgress.setVisibility(View.GONE);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							customProgress.setVisibility(View.GONE);
						}

						@Override
						public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							customProgress.setVisibility(View.GONE);
						}

						@Override
						public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							customProgress.setVisibility(View.GONE);
							alertDialog(LoginActivity.this, 0, "Error", SOME_SERVER_ERROR_OCCURRED,
								OK, true, null);
						}

						@Override
						public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							customProgress.setVisibility(View.GONE);
							alertDialog(LoginActivity.this, 0, "Error", SOME_SERVER_ERROR_OCCURRED,
								OK, true, null);
						}

						@Override
						public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString,
							Throwable throwable) {
							super.onFailure(statusCode, headers, responseString, throwable);
							customProgress.setVisibility(View.GONE);
							alertDialog(LoginActivity.this, 0, "Error", SOME_SERVER_ERROR_OCCURRED,
								OK, true, null);
						}
					});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			customProgress.setVisibility(View.GONE);
			MSFDialog.alertDialog(LoginActivity.this, 0,
				getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, OK, true,
				null);
		}
	}

	private void storeLoginPrefData(LoginResponseData loginData) throws JSONException {
		SharedPreferences.Editor editor = getPrefs(this).edit();
		editor.putString("PASSWORD", etPassword.getText().toString().trim());
		editor.putString("EmployeeCode", loginData.getEmployeeCode().trim());
		editor.putString("token", loginData.getToken());
		editor.putString("userid", loginData.getUserId());
		editor.putString("ipaddress", CRMAppUtil.getLocalIpAddress());
		editor.putString("BusinessUnitId", loginData.getBusinessUnitId());
		editor.putString("macaddress", getDeviceID(getApplicationContext(), getContentResolver()));

		editor.commit();
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

	public void FetchReportTypes() {

		if (CRMAppUtil.isNetworkEnabled(getApplicationContext())) {
			Map inputMap = new HashMap();
			String serviceMethod = "Report/FetchReportTypes";

			SplashActivity.menuReportArrayList.clear();
			try {
				CRMWebServiceHandler.post(getApplicationContext(), serviceMethod,
					getHeader(getApplicationContext()), inputMap, new JsonHttpResponseHandler() {

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject json) {
							super.onSuccess(statusCode, headers, json);

							if (statusCode == 200) {
								try {
									String status = json.getString(STATUS);
									if (status.equalsIgnoreCase("true")) {

										// JSONObject jsonObject = response.getJSONObject(RESPONSE);
										JSONArray dataArray = json.getJSONArray(RESPONSE);
										if (dataArray.length() > 0) {
											for (int i = 0; i < dataArray.length(); i++) {
												String childSystemName = "";
												JSONObject ChildMenuListObj = null;
												JSONObject objDetail = dataArray.getJSONObject(i);
												String Name = (objDetail.isNull("Name")
													|| objDetail.getString("Name").equals("")) ? ""
													: objDetail.getString("Name");
												String SystemName = (objDetail.isNull("SystemName")
													|| objDetail.getString("SystemName").equals(""))
													? "" : objDetail.getString("SystemName");
												if (SystemName.equalsIgnoreCase(
													"PRE-DEFINED_REPORTS")) {
													JSONArray ChildMenuListArray =
														objDetail.getJSONArray("ChildMenuList");
													for (int j = 0; j < ChildMenuListArray.length();
														j++) {
														ChildMenuListObj =
															ChildMenuListArray.getJSONObject(j);
														String childName =
															(ChildMenuListObj.isNull("Name")
																|| ChildMenuListObj.getString(
																"Name").equals("")) ? ""
																: ChildMenuListObj.getString(
																	"Name");
														childSystemName =
															(ChildMenuListObj.isNull("SystemName")
																|| ChildMenuListObj.getString(
																"SystemName").equals("")) ? ""
																: ChildMenuListObj.getString(
																	"SystemName");
													}
												}
												if (childSystemName.equalsIgnoreCase(
													"BASIC_REPORTS")) {
													JSONArray subChildMenuListArray =
														ChildMenuListObj.getJSONArray(
															"ChildMenuList");
													for (int k = 0;
														k < subChildMenuListArray.length(); k++) {
														JSONObject subChildMenuListObj =
															subChildMenuListArray.getJSONObject(k);
														String subChildName =
															(subChildMenuListObj.isNull("Name")
																|| subChildMenuListObj.getString(
																"Name").equals("")) ? ""
																: subChildMenuListObj.getString(
																	"Name");
														String subChildSystemName =
															(subChildMenuListObj.isNull(
																"SystemName") || subChildMenuListObj
																.getString("SystemName")
																.equals("")) ? ""
																: subChildMenuListObj.getString(
																	"SystemName");
														SplashActivity.menuReportArrayList.add(
															new MenuReport(subChildName,
																subChildSystemName));
													}
												}
											}
											Intent intent = new Intent(LoginActivity.this,
												MOSLCommonActivity.class);
											startActivity(intent);
											finish();
										}
									} else {
										Intent intent = new Intent(LoginActivity.this,
											MOSLCommonActivity.class);
										startActivity(intent);
										finish();
										String errorMsg =
											CRMMOSLCommon.getErrorMsgFromResponse(json);
										alertDialog(getApplicationContext(), 0,
											getResources().getString(R.string.motilal_oswal),
											errorMsg, "OK", true, null);
									}
								} catch (Exception ex) {
									Intent intent =
										new Intent(LoginActivity.this, MOSLCommonActivity.class);
									startActivity(intent);
									finish();
									ex.printStackTrace();
									alertDialog(getApplicationContext(), 0,
										getResources().getString(R.string.motilal_oswal),
										SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
								}
							} else {
								Intent intent =
									new Intent(LoginActivity.this, MOSLCommonActivity.class);
								startActivity(intent);
								finish();
								alertDialog(getApplicationContext(), 0,
									getResources().getString(R.string.motilal_oswal),
									SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
							}
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
						}

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
						}

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							try {
								if (statusCode == UNAUTHORIZE_STATUS_CODE) {
									MSFDialog.alertDialog(getApplicationContext(), 0,
										getResources().getString(R.string.motilal_oswal),
										UNAUTHORIZE_USER, "OK", false,
										new MSFDialog.DialogListener() {
											@Override
											public void alertDialogAction(MSFDialog.Action action,
												Object... data) {
												if (action == MSFDialog.Action.OK) {
													Intent i = new Intent(getApplicationContext(),
														LoginActivity.class);
													startActivity(i);
													finish();
												}
											}
										});
									return;
								} else {
									Intent intent =
										new Intent(LoginActivity.this, MOSLCommonActivity.class);
									startActivity(intent);
									finish();
									String error =
										CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
									if (CRMAppUtil.isBlank(error)) {
										error = SOME_SERVER_ERROR_OCCURRED;
									}
									MSFDialog.alertDialog(getApplicationContext(), 0,
										getResources().getString(R.string.motilal_oswal), error,
										"OK", true, null);
									return;
								}
							} catch (Exception ex) {
								ex.printStackTrace();
								MSFDialog.alertDialog(getApplicationContext(), 0,
									getResources().getString(R.string.motilal_oswal),
									SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
							}
						}

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							alertDialog(getApplicationContext(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString,
							Throwable throwable) {
							super.onFailure(statusCode, headers, responseString, throwable);
							alertDialog(getApplicationContext(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}
					});
			} catch (Exception e) {
				e.printStackTrace();
				alertDialog(getApplicationContext(), 0,
					getResources().getString(R.string.motilal_oswal),
					SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
			}
		} else {
			MSFDialog.alertDialog(getApplicationContext(), 0,
				getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
				null);
		}
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		loginAction();
		return false;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnLogin: {
				loginAction();
			}
			break;
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

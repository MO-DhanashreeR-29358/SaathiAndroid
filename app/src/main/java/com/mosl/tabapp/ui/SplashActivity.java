package com.mosl.tabapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.model.RoleSelectionModel;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.MyExceptionHandler;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.MenuReport;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.mosl.mobile.ekyc.constants.MOSLConstants.CRM;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.setUserType;
import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;
import static com.mosl.tabapp.common.CRMMOSLCommon.getAppVersionCode;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class SplashActivity extends FragmentActivity implements MOSLConstants {
	public static ArrayList<MenuReport> menuReportArrayList = new ArrayList<>();
	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;

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

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		Thread.setDefaultUncaughtExceptionHandler(
			new MyExceptionHandler(this, SplashActivity.class));
		new Handler().postDelayed(new Runnable() {
			@Override public void run() {
				setAppType(CRM);
				setUserType("");
				versionCheck();
			}
		}, SPLASH_TIME_OUT);
	}

	private void handleStartupPage() {
		String empCode = getPrefs(this).getString("EmployeeCode", null);
		String password = getPrefs(this).getString("PASSWORD", null);

		if (empCode != null && password != null) {
			if (CRMAppUtil.isNetworkEnabled(this)) {

				Map inputMap = new HashMap<>();
				inputMap.put("EmployeeCode", empCode);
				inputMap.put("Password", password);
				inputMap.put("IPAddress", CRMAppUtil.getLocalIpAddress());
				inputMap.put("ClientInfo", CLIENT_INFO);
				inputMap.put("Macaddress",
					CRMMOSLCommon.getDeviceID(getApplicationContext(), getContentResolver()));
				inputMap.put("AppId", "2");

				Map headerMap = null;
				String serviceMethod = "Auth/Login";

				try {
					CRMWebServiceHandler.post(this, serviceMethod, headerMap, inputMap,
						new JsonHttpResponseHandler() {

							@Override public void onSuccess(int statusCode,
								cz.msebera.android.httpclient.Header[] headers,
								JSONObject response) {
								super.onSuccess(statusCode, headers, response);
								//after success code goes here
								try {
									if (statusCode == 200) {

										String status = response.getString(STATUS);
										if (status.equalsIgnoreCase("true")) {
											JSONObject objDetail = response.getJSONObject(RESPONSE);
											boolean smtEnabled =
												objDetail.getString(SMT_ENABLED).equals("1");
											boolean isEkycRevemp =
												objDetail.getString(ISEKYCREVAMP).equals("1");
											JSONObject objDetailUser =
												objDetail.getJSONObject(USER);
											String userId = objDetailUser.isNull(USER_ID) ?
												"" : objDetailUser.getString(USER_ID);
											String employeeCode =
												objDetailUser.isNull(EMPLOYEE_CODE) ?
													"" : objDetailUser.getString(EMPLOYEE_CODE);
											String firstName = objDetailUser.isNull(FIRST_NAME) ?
												"" : objDetailUser.getString(FIRST_NAME);
											String lastName = objDetailUser.isNull(LAST_NAME) ?
												"" : objDetailUser.getString(LAST_NAME);
											String imageUrl = objDetailUser.isNull(IMAGE_URL) ?
												"" : objDetailUser.getString(IMAGE_URL);
											String mobile = objDetailUser.isNull(MOBILE) ?
												"" : objDetailUser.getString(MOBILE);
											String email = objDetailUser.isNull(EMAIL) ?
												"" : objDetailUser.getString(EMAIL);
											String token = objDetailUser.isNull(TOKEN) ?
												"" : objDetailUser.getString(TOKEN);
											String userType = objDetailUser.isNull(USER_TYPE_SEQ) ?
												"" : objDetailUser.getString(USER_TYPE_SEQ);
											JSONObject objDetailDefaultBusinessProfile =
												objDetailUser.getJSONObject(
													DEFAULT_BUSINESS_PROFILE);
											String branchCode =
												objDetailDefaultBusinessProfile.isNull(
													BRANCHCODE_DEFAULT_BUSINESS_PROFILE) ? ""
													: objDetailDefaultBusinessProfile.getString(
														BRANCHCODE_DEFAULT_BUSINESS_PROFILE);
											String rmId =
												objDetailDefaultBusinessProfile.isNull(USER_ID) ? ""
													: objDetailDefaultBusinessProfile.getString(
														USER_ID);
											setAppLoginID(employeeCode);
											setAppType(CRM);
											setUserType("");
											JSONObject objDetailBusinessProfile =
												objDetailUser.getJSONObject(
													CURRENT_BUSINESS_PROFILE);
											String businessProfileId =
												objDetailBusinessProfile.isNull(BUSINESS_PROFILE_ID)
													?
													"" : objDetailBusinessProfile.getString(
													BUSINESS_PROFILE_ID);
											String businessProfileName =
												objDetailBusinessProfile.isNull(
													CURRENT_BUSINESS_PROFILE_NAME) ? ""
													: objDetailBusinessProfile.getString(
														CURRENT_BUSINESS_PROFILE_NAME);
											String businessUnitId =
												objDetailBusinessProfile.isNull(BUSINESS_UNIT_ID)
													? ""
													: objDetailBusinessProfile.getString(
														BUSINESS_UNIT_ID);

											boolean aufsb = userType.equals("9");
											AccountDetails.setNewEkyc(aufsb);
											new RoleSelectionModel("", "", "", "",
												employeeCode.trim(), rmId.trim(),
												businessUnitId.trim(), "",/*roleId*/"");
											new AccountDetails(userId,
												firstName, lastName,
												imageUrl.replaceAll(" ", "%20"), mobile,
												email,
												businessProfileId,
												businessProfileName,
												employeeCode, token,
												businessUnitId,
												branchCode, userType,
												smtEnabled, aufsb, isEkycRevemp);
											MOSLCommonActivity.sendRegistrationToServer(
												SplashActivity.this, getContentResolver(), false);
											if (aufsb) {
												openMainActivity();
											} else {
												FetchReportTypes();
											}
										} else {

											handleLoginError();
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							@Override public void onSuccess(int statusCode,
								cz.msebera.android.httpclient.Header[] headers,
								JSONArray response) {
								super.onSuccess(statusCode, headers, response);
							}

							@Override public void onSuccess(int statusCode,
								cz.msebera.android.httpclient.Header[] headers,
								String responseString) {
								super.onSuccess(statusCode, headers, responseString);
							}

							@Override public void onFailure(int statusCode,
								cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
								JSONObject errorResponse) {
								super.onFailure(statusCode, headers, throwable, errorResponse);
								handleLoginError();
							}

							@Override public void onFailure(int statusCode,
								cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
								JSONArray errorResponse) {
								super.onFailure(statusCode, headers, throwable, errorResponse);
							}

							@Override public void onFailure(int statusCode,
								cz.msebera.android.httpclient.Header[] headers,
								String responseString, Throwable throwable) {
								super.onFailure(statusCode, headers, responseString, throwable);
							}
						});
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				MSFDialog.alertDialog(this, 0, getResources().getString(R.string.motilal_oswal),
					INTERNET_CONNECTION, "OK", false, new MSFDialog.DialogListener() {
						@Override
						public void alertDialogAction(MSFDialog.Action action, Object... data) {
							if (action == MSFDialog.Action.OK) {
								finish();
							}
						}
					});
			}
		} else {

			handleLoginError();
		}
	}

	public void FetchReportTypes() {

		if (CRMAppUtil.isNetworkEnabled(getApplicationContext())) {
			Map inputMap = new HashMap();
			String serviceMethod = "Report/FetchReportTypes";

			menuReportArrayList.clear();
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
														menuReportArrayList.add(
															new MenuReport(subChildName,
																subChildSystemName));
													}
												}
											}
											Intent intent = new Intent(SplashActivity.this,
												MOSLCommonActivity.class);
											startActivity(intent);
											finish();
										}
									} else {
										Intent intent = new Intent(SplashActivity.this,
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
										new Intent(SplashActivity.this, MOSLCommonActivity.class);
									startActivity(intent);
									finish();
									ex.printStackTrace();
									alertDialog(getApplicationContext(), 0,
										getResources().getString(R.string.motilal_oswal),
										SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
								}
							} else {
								Intent intent =
									new Intent(SplashActivity.this, MOSLCommonActivity.class);
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
										new Intent(SplashActivity.this, MOSLCommonActivity.class);
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
			MSFDialog.alertDialog(this, 0, getResources().getString(R.string.motilal_oswal),
				INTERNET_CONNECTION, "OK", false, new MSFDialog.DialogListener() {
					@Override
					public void alertDialogAction(MSFDialog.Action action, Object... data) {
						if (action == MSFDialog.Action.OK) {
							finish();
						}
					}
				});
		}
	}

	private void handleLoginError() {
		clearLoginPrefData();
		Intent i = new Intent(SplashActivity.this, LoginActivity.class);
		startActivity(i);
		finish();
	}

	private void clearLoginPrefData() {

		getPrefs(this).edit().remove("EmployeeCode");
		getPrefs(this).edit().remove("PASSWORD");
	}

	private void versionCheck() {
		if (CRMAppUtil.isNetworkEnabled(SplashActivity.this)) {

			final Map inputMap = new HashMap<>();

			inputMap.put("ApplicationType", NEW_CRM);
			inputMap.put("AppVersion", "" + getAppVersionCode(SplashActivity.this));
			inputMap.put("RequestSource", "Android");
			inputMap.put("DisplayVersion", "");
			Map headerMap = new HashMap();
			headerMap.put("macaddress",
				CRMMOSLCommon.getDeviceID(SplashActivity.this, getContentResolver()));
			String serviceMethod = "Common/GetAppVersion";

			try {
				CRMWebServiceHandler.post(SplashActivity.this, serviceMethod, headerMap, inputMap,
					new JsonHttpResponseHandler() {

						@Override public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							//after success code goes here
							try {
								if (statusCode == 200) {
									String status = response.getString(STATUS);
									String message = response.getString(MESSAGE);
									if (status.equalsIgnoreCase("true")) {
										JSONObject objDetail = response.getJSONObject(RESPONSE);
										JSONArray jsonArray = objDetail.getJSONArray(DATA);
										if (jsonArray != null && jsonArray.length() != 0) {
											for (int i = 0; i < jsonArray.length(); i++) {
												JSONObject jsonObject = jsonArray.getJSONObject(0);
												String StatusCode =
													jsonObject.getString("StatusCode") == null ? ""
														: jsonObject.getString("StatusCode");
												String newEkyc =
													jsonObject.getString("NEWEKYC") == null ? ""
														: jsonObject.getString("NEWEKYC");
												String strURL = GOOGLE_PLAY_URL;
												if (jsonObject.has(URL)) {
													strURL = jsonObject.getString(URL) == null
														|| jsonObject.getString(URL).length() < 3
														? GOOGLE_PLAY_URL
														: jsonObject.getString(URL);
												}
												final String strURLFinal = strURL;
												String ekycUrl =
													jsonObject.getString(NEWEKYCURL) == null ? ""
														: jsonObject.getString(NEWEKYCURL);
												AccountDetails.setEkycUrl(
													ekycUrl.replaceAll(" ", "%20"));
												if (newEkyc.equalsIgnoreCase("1")) {
													AccountDetails.setNewEkyc(true);
												} else {
													AccountDetails.setNewEkyc(false);
												}

												String msg =
													jsonObject.getString("msg") == null ? ""
														: jsonObject.getString("msg");

												if (StatusCode.equalsIgnoreCase("1")) {
													handleStartupPage();
												} else if (StatusCode.equalsIgnoreCase("0")) {
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
																		Uri.parse(strURLFinal));
																	startActivity(i);
																	finish();
																}
															}
														});
												} else if (StatusCode.equalsIgnoreCase("3")) {
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
																		Uri.parse(strURLFinal));
																	startActivity(i);
																	finish();
																} else if (action
																	== MSFDialog.Action.CANCEL) {
																	handleStartupPage();
																}
															}
														});
												} else if (StatusCode.equalsIgnoreCase("2")) {
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
									} else {
										String errorMsg =
											CRMMOSLCommon.getErrorMsgFromResponse(response);
										MSFDialog.alertDialog(SplashActivity.this, 0,
											getResources().getString(R.string.motilal_oswal),
											errorMsg, "Ok", false, new MSFDialog.DialogListener() {
												@Override public void alertDialogAction(
													MSFDialog.Action action, Object... data) {
													if (action == MSFDialog.Action.OK) {
														finish();
													}
												}
											});
									}
								} else {
								}
							} catch (Exception e) {
								e.printStackTrace();
								MSFDialog.alertDialog(SplashActivity.this, 0,
									getResources().getString(R.string.motilal_oswal),
									SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
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
									MSFDialog.alertDialog(SplashActivity.this, 0,
										getResources().getString(R.string.motilal_oswal),
										UNAUTHORIZE_USER, "OK", false,
										new MSFDialog.DialogListener() {
											@Override
											public void alertDialogAction(MSFDialog.Action action,
												Object... data) {
												if (action == MSFDialog.Action.OK) {
													Intent i = new Intent(SplashActivity.this,
														LoginActivity.class);
													startActivity(i);
													finish();
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
									MSFDialog.alertDialog(SplashActivity.this, 0,
										getResources().getString(R.string.motilal_oswal), error,
										"OK", true, null);
									return;
								}
							} catch (Exception ex) {
								ex.printStackTrace();
								MSFDialog.alertDialog(SplashActivity.this, 0,
									getResources().getString(R.string.motilal_oswal),
									SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
							}
						}

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);

							MSFDialog.alertDialog(SplashActivity.this, 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}

						@Override public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString,
							Throwable throwable) {
							super.onFailure(statusCode, headers, responseString, throwable);

							MSFDialog.alertDialog(SplashActivity.this, 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
						}
					});
			} catch (Exception e) {
				e.printStackTrace();

				MSFDialog.alertDialog(SplashActivity.this, 0,
					getResources().getString(R.string.motilal_oswal),
					SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
			}
		} else {
			MSFDialog.alertDialog(SplashActivity.this, 0,
				getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
				new MSFDialog.DialogListener() {
					@Override
					public void alertDialogAction(MSFDialog.Action action, Object... data) {
						if (action == MSFDialog.Action.OK) {
							finish();
						}
					}
				});
		}
	}

	@Override public void onBackPressed() {

	}

	private void openMainActivity() {
		Intent intent =
			new Intent(SplashActivity.this, MOSLCommonActivity.class);
		startActivity(intent);
		finish();
	}
}

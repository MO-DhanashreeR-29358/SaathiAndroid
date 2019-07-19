package com.mosl.tabapp.service;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import androidx.work.Worker;
import com.mosl.tabapp.CrmApp;
import com.mosl.tabapp.common.CRMSecurityUtil;
import com.mosl.tabapp.common.MSFStatistics;
import com.mosl.tabapp.database.LocationData;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import java.io.IOException;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;

import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static java.util.Objects.requireNonNull;

public class DataSyncWorker extends Worker {
	private static final String TAG = "MyWorker";

	@NonNull
	@Override
	public Result doWork() {
		try {
			if (getPrefs(getApplicationContext()).getString("EmployeeCode", null) != null
				|| !requireNonNull(
				getPrefs(
					getApplicationContext()).getString("EmployeeCode", null)).isEmpty()) {
				if (MSFStatistics.isNetworkEnabled(getApplicationContext())) {
					if (CrmApp.get().getDB().daoAccess().getLocationList().size() != 0) {
						SaveGeoLocation();
					}
				}
			}
			return Result.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			Timber.d(e.toString());
			return Result.RETRY;
		}
	}

	private void SaveGeoLocation() {
		OkHttpClient client = new OkHttpClient();
		List<LocationData> locationDataList = CrmApp.get().getDB().daoAccess().getLocationList();
		SharedPreferences prefs = getPrefs(getApplicationContext());
		MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
		String url = CRMWebServiceHandler.CALL_BASE_URL;
		String serviceMethod = "GeoLocation/SaveGeoLocation";
		RequestBody body = RequestBody.create(MEDIA_TYPE,
			requireNonNull(CRMSecurityUtil.encrypt(getLocationMapping(locationDataList))));

		final Request request = new Request.Builder()
			.url(url + serviceMethod)
			.post(body)
			.addHeader("Content-Type",
				requireNonNull(CRMSecurityUtil.encrypt("application/json; charset=UTF-8")))
			.addHeader("AppId", requireNonNull(CRMSecurityUtil.encrypt("2")))
			.addHeader("clientinfo", requireNonNull(CRMSecurityUtil.encrypt("android")))
			.addHeader("EmployeeCode",
				requireNonNull(
					CRMSecurityUtil.encrypt(prefs.getString("EmployeeCode", null))))
			.addHeader("token",
				requireNonNull(CRMSecurityUtil.encrypt(prefs.getString("token", null))))
			.addHeader("userid",
				requireNonNull(CRMSecurityUtil.encrypt(prefs.getString("userid", null))))
			.addHeader("ipaddress",
				requireNonNull(CRMSecurityUtil.encrypt(prefs.getString("ipaddress", null))))
			.addHeader("BusinessUnitId",
				requireNonNull(
					CRMSecurityUtil.encrypt(prefs.getString("BusinessUnitId", null))))
			.addHeader("macaddress",
				requireNonNull(
					CRMSecurityUtil.encrypt(prefs.getString("macaddress", null))))
			.build();
		Timber.tag("Location Data Request").d(request.toString());
		Buffer buffer = new Buffer();
		try {
			body.writeTo(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Timber.tag("Location encrpt ReqBody").d(buffer.readUtf8());
		Timber.tag("Location Data ReqBody").d(getLocationMapping(locationDataList).toString());
		Timber.tag("Location Header").d(request.headers().toString());

		try {
			Response response = client.newCall(request).execute();
			String res = response.body() != null ? response.body().string() : null;
			Timber.tag("Responce w/o decrypt").d(res);
			assert res != null;
			Timber.tag("Responce with decrypt").d(new String(
				requireNonNull(CRMSecurityUtil.decrypt(new String(res)))));
			try {
				JSONObject jsonObject;
				jsonObject = new JSONObject(new String(
					requireNonNull(CRMSecurityUtil.decrypt(new String(res)))));
				String status = jsonObject.getString(STATUS);
				if (status.equalsIgnoreCase("true")) {
					CrmApp.get().getDB().daoAccess().deleteAll();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// Do something with the response.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getLocationMapping(List<LocationData> locationDataList) {
		String json = "{\"GeoList\":[";
		for (LocationData model : locationDataList) {
			json += "{";
			json += "\"EmployeeCode\":\"" + model.getEmpCode() + "\"";
			json += ",\"GeoArea\":\"" + model.getGeoArea() + "\"";
			json += ",\"GeoLongitude\":\"" + model.getLongitude() + "\"";
			json += ",\"GeoLatitude\":\"" + model.getLatitude() + "\"";
			json += ",\"Time\":\"" + model.getTime() + "\"";
			json += ",\"DeviceId\":\"" + model.getDeviceId() + "\"";
			json += ",\"Accuracy\":\"" + model.getAccuracy() + "\"";
			json += ",\"Id\":\"" + 0 + "\"";
			json += ",\"GeoPinCode\":\"" + model.getGeoPincode() + "\"";

			json += "},";
		}
		json = json.substring(0, json.length() - 1);

		json += "]}";
		return json;
	}
}

package com.mosl.tabapp.service;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.util.Log;
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

import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;

public class DataSyncWorker extends Worker {
  private static final String TAG = "MyWorker";

  @NonNull
  @Override
  public Result doWork() {
    try {
      if (getPrefs(getApplicationContext()).getString("EmployeeCode", null) != null || !getPrefs(
          getApplicationContext()).getString("EmployeeCode", null).isEmpty()) {
        if (MSFStatistics.isNetworkEnabled(getApplicationContext())) {
          if (CrmApp.get().getDB().daoAccess().getLocationList().size() != 0) {
            SaveGeoLocation();
          }
        }
      }
      return Result.SUCCESS;
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(TAG, e.toString());
      return Result.RETRY;
    }
  }

  public void SaveGeoLocation() {
    OkHttpClient client = new OkHttpClient();
    List<LocationData> locationDataList = CrmApp.get().getDB().daoAccess().getLocationList();
    SharedPreferences prefs = getPrefs(getApplicationContext());
    MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    String url = CRMWebServiceHandler.CALL_BASE_URL;
    String serviceMethod = "GeoLocation/SaveGeoLocation";
    RequestBody body = RequestBody.create(MEDIA_TYPE,
        CRMSecurityUtil.encrypt(getLocationMapping(locationDataList)));

    final Request request = new Request.Builder()
        .url(url + serviceMethod)
        .post(body)
        .addHeader("Content-Type", CRMSecurityUtil.encrypt("application/json; charset=UTF-8"))
        .addHeader("AppId", CRMSecurityUtil.encrypt("2"))
        .addHeader("clientinfo", CRMSecurityUtil.encrypt("android"))
        .addHeader("EmployeeCode", CRMSecurityUtil.encrypt(prefs.getString("EmployeeCode", null)))
        .addHeader("token", CRMSecurityUtil.encrypt(prefs.getString("token", null)))
        .addHeader("userid", CRMSecurityUtil.encrypt(prefs.getString("userid", null)))
        .addHeader("ipaddress", CRMSecurityUtil.encrypt(prefs.getString("ipaddress", null)))
        .addHeader("BusinessUnitId",
            CRMSecurityUtil.encrypt(prefs.getString("BusinessUnitId", null)))
        .addHeader("macaddress", CRMSecurityUtil.encrypt(prefs.getString("macaddress", null)))
        .build();
    Log.d("Location Data Request", request.toString());
    Buffer buffer = new Buffer();
    try {
      body.writeTo(buffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
    Log.d("Location encrpt ReqBody", buffer.readUtf8());
    Log.d("Location Data ReqBody", getLocationMapping(locationDataList).toString());
    Log.d("Location Header", request.headers().toString());

    try {
      Response response = client.newCall(request).execute();
      String res = response.body().string();
      Log.d("Responce w/o decrypt", res);
      Log.d("Responce with decrypt", new String(CRMSecurityUtil.decrypt(new String(res))));
      try {
        JSONObject jsonObject =
            new JSONObject(new String(CRMSecurityUtil.decrypt(new String(res))));
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

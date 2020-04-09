package com.mosl.tabapp.service;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.mosl.mobile.user.login.UserManager;
import com.mosl.saathi.api.SaathiApi;
import com.mosl.shared.Encryptor.AESEncryptor;
import com.mosl.tabapp.SaathiAppNew;
import com.mosl.tabapp.common.MSFStatistics;
import com.mosl.tabapp.database.LocationData;
import com.mosl.tabapp.urls.ServiceUrlsImpl;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;

import static com.mosl.app.core.utils.Utils.getLocalIpAddress;
import static com.mosl.shared.util.CRMAppUtilsKt.getDeviceID;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;

public class DataSyncWorkerOld extends Worker {
  @Inject UserManager userManager;
  @Inject SaathiApi saathiApi;
  @Inject AESEncryptor encryptor;

  public DataSyncWorkerOld(Context context,
      WorkerParameters workerParams) {
    super(context, workerParams);
    // Injector.obtain(context, AppComponent.class).inject(this);
  }

  @NonNull
  @Override
  public Result doWork() {
    try {
      if (userManager.isLoggedIn()) {
        if (MSFStatistics.isNetworkEnabled(getApplicationContext())) {
          if (SaathiAppNew.get().getDB().daoAccess()
              .getLocationList()
              .size() != 0) {
            SaveGeoLocation();
          }
        }
      }
      return Result.success();
    } catch (Exception e) {
      e.printStackTrace();
      Timber.d(e.toString());
      return Result.retry();
    }
  }

  private void SaveGeoLocation() {
    OkHttpClient client = new OkHttpClient();
    List<LocationData> locationDataList =
        SaathiAppNew.get().getDB().daoAccess().getLocationList();
    MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    String url = ServiceUrlsImpl.CRM_BASE_URL();
    String serviceMethod = "GeoLocation/SaveGeoLocation";
    RequestBody body = RequestBody.create(MEDIA_TYPE,
        encryptor.encrypt(getLocationMapping(locationDataList)));

    final Request request = new Request.Builder()
        .url(url + serviceMethod)
        .post(body)
        .addHeader("Content-Type",
            encryptor.encrypt("application/json; charset=UTF-8"))
        .addHeader("AppId", encryptor.encrypt("2"))
        .addHeader("clientinfo", encryptor.encrypt("android"))
        .addHeader("EmployeeCode",
            encryptor.encrypt(userManager.getUser().employeeCode))
        .addHeader("token",
            encryptor.encrypt(userManager.getUser().token))
        .addHeader("userid",
            encryptor.encrypt(userManager.getUser().userId))
        .addHeader("ipaddress",
            encryptor.encrypt(getLocalIpAddress()))
        .addHeader("BusinessUnitId",
            encryptor.encrypt(
                userManager.getUser().currentBusinessProfile.businessUnitId))
        .addHeader("macaddress",
            encryptor.encrypt(
                getDeviceID(getApplicationContext().getContentResolver())))
        .build();
    Timber.tag("Location Data Request").d(request.toString());
    Buffer buffer = new Buffer();
    try {
      body.writeTo(buffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
    Timber.tag("Location encrpt ReqBody").d(buffer.readUtf8());
    Timber.tag("Location Data ReqBody").d(getLocationMapping(locationDataList));
    Timber.tag("Location Header").d(request.headers().toString());

    try {
      Response response = client.newCall(request).execute();
      String res = response.body() != null ? response.body().string() : null;
      Timber.tag("Responce w/o decrypt").d(res);
      assert res != null;
      Timber.tag("Responce with decrypt").d(new String(
          encryptor.decrypt(res)));
      try {
        JSONObject jsonObject;
        jsonObject = new JSONObject(new String(
            encryptor.decrypt(res)));
        String status = jsonObject.getString(STATUS);
        if (status.equalsIgnoreCase("true")) {
          SaathiAppNew.get().getDB().daoAccess().deleteAll();
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

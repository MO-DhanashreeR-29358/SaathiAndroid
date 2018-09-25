package com.mosl.tabapp.ws;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.mosl.tabapp.common.CRMSecurityUtil;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import org.json.JSONObject;

import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;
import static com.mosl.tabapp.ws.CRMWebServiceHandler.getJsonString;

public class RefreeSyncWebServiceCall {

    private static String TAG = RefreeSyncWebServiceCall.class.getCanonicalName();

    private static String MSG_NORMAL = "Request_Normal";
    private static String MSG_ENCRYPT = "Request_Encrypt";

    public static Observable<JSONObject> getObservableData(Context context, String serviceMethod,
        Map inputParam, ProgressBar pbLoadingIndicatorRefreeCode) {
        return Observable.fromCallable(() -> {
            return getData(context, serviceMethod, inputParam, pbLoadingIndicatorRefreeCode);
        }).subscribeOn(Schedulers.io());
    }

    public static JSONObject getData(Context context, String serviceMethod, Map inputParam,
        ProgressBar pbLoadingIndicatorRefreeCode) {
        String url = CRMWebServiceHandler.CALL_BASE_URL;
        OkHttpClient client = new OkHttpClient();
        try {
            SharedPreferences prefs = getPrefs(context);
            MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
            RequestBody body =
                RequestBody.create(MEDIA_TYPE, CRMSecurityUtil.encrypt(getJsonString(inputParam)));

            final Request request = new Request.Builder().url(url + serviceMethod)
                .post(body)
                .addHeader("Content-Type",
                    CRMSecurityUtil.encrypt("application/json; charset=UTF-8"))
                .addHeader("AppId", CRMSecurityUtil.encrypt("2"))
                .addHeader("clientinfo", CRMSecurityUtil.encrypt("android"))
                .addHeader("EmployeeCode",
                    CRMSecurityUtil.encrypt(prefs.getString("EmployeeCode", null)))
                .addHeader("token", CRMSecurityUtil.encrypt(prefs.getString("token", null)))
                .addHeader("userid", CRMSecurityUtil.encrypt(prefs.getString("userid", null)))
                .addHeader("ipaddress", CRMSecurityUtil.encrypt(prefs.getString("ipaddress", null)))
                .addHeader("BusinessUnitId",
                    CRMSecurityUtil.encrypt(prefs.getString("BusinessUnitId", null)))
                .addHeader("macaddress",
                    CRMSecurityUtil.encrypt(prefs.getString("macaddress", null)))
                .build();
            Log.d("Location Data Request", request.toString());

            final int requestId = (int) (Math.random() * 5000);

            printLog(MSG_NORMAL, requestId, url, request.headers().toString());
            printLog(MSG_ENCRYPT, requestId, url, request.body().toString());

            // Send post request
            Buffer buffer = new Buffer();
            try {
                body.writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Location encrpt ReqBody", buffer.readUtf8());
            try {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.connectTimeout(90, TimeUnit.SECONDS);
                builder.readTimeout(90, TimeUnit.SECONDS);
                builder.writeTimeout(90, TimeUnit.SECONDS);
                client = builder.build();
                Response response = client.newCall(request).execute();

                String res = response.body().string();
                Log.d(TAG, "Responce w/o decrypt: [" + requestId + "] : " + res);
                Log.d(TAG, "Responce with decrypt: [" + requestId + "] : " + new String(
                    CRMSecurityUtil.decrypt(new String(res))));

                return new JSONObject(new String(CRMSecurityUtil.decrypt(new String(res))));
            } catch (IOException e) {
                e.printStackTrace();
                pbLoadingIndicatorRefreeCode.setVisibility(View.GONE);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            pbLoadingIndicatorRefreeCode.setVisibility(View.GONE);
            return null;
        }
    }

    private static void printLog(String headerMsg, int requestId, String url, String header) {
        Log.d(TAG, headerMsg + ": [" + requestId + "] URL=" + url + ", HEADER=" + header);
    }
}

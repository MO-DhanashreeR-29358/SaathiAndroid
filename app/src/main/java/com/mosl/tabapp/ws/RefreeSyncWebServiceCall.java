package com.mosl.tabapp.ws;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.mosl.mobile.user.login.UserManager;
import com.mosl.tabapp.common.CRMSecurityUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import timber.log.Timber;

import static com.mosl.app.core.utils.Utils.getLocalIpAddress;
import static com.mosl.shared.util.CRMAppUtilsKt.getDeviceID;
import static com.mosl.tabapp.ws.CRMWebServiceHandler.getJsonString;

public class RefreeSyncWebServiceCall {

	private static String TAG = RefreeSyncWebServiceCall.class.getCanonicalName();

	public static Observable<JSONObject> getObservableData(Context context, String serviceMethod,
		Map inputParam, ProgressBar pbLoadingIndicatorRefreeCode, UserManager userManager) {
		return Observable.fromCallable(() -> {
			return getData(context, serviceMethod, inputParam, pbLoadingIndicatorRefreeCode,
				userManager);
		}).subscribeOn(Schedulers.io());
	}

	private static JSONObject getData(Context context, String serviceMethod, Map inputParam,
		ProgressBar pbLoadingIndicatorRefreeCode, UserManager userManager) {
		String url = CRMWebServiceHandler.CALL_BASE_URL;
		OkHttpClient client = new OkHttpClient();
		try {
			MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
			RequestBody body =
				RequestBody.create(MEDIA_TYPE,
						CRMSecurityUtil.encrypt(getJsonString(inputParam)));

			final Request request = new Request.Builder().url(url + serviceMethod)
				.post(body)
				.addHeader("Content-Type",

						CRMSecurityUtil.encrypt("application/json; charset=UTF-8"))
					.addHeader("AppId", CRMSecurityUtil.encrypt("2"))
					.addHeader("clientinfo", CRMSecurityUtil.encrypt("android"))
				.addHeader("EmployeeCode",


						CRMSecurityUtil.encrypt(
								userManager.getUser().employeeCode))
					.addHeader("token", CRMSecurityUtil.encrypt(userManager.getUser().token))
					.addHeader("userid",
							CRMSecurityUtil.encrypt(userManager.getUser().userId))
					.addHeader("ipaddress",
							CRMSecurityUtil.encrypt(getLocalIpAddress()))
				.addHeader("BusinessUnitId",

						CRMSecurityUtil.encrypt(
								userManager.getUser().currentBusinessProfile.businessUnitId))
				.addHeader("macaddress",

						CRMSecurityUtil.encrypt(getDeviceID(context.getContentResolver())))
				.build();
			Timber.tag("Location Data Request").d(request.toString());

			final int requestId = (int) (Math.random() * 5000);

			String MSG_NORMAL = "Request_Normal";
			printLog(MSG_NORMAL, requestId, url, request.headers().toString());
			String MSG_ENCRYPT = "Request_Encrypt";
			printLog(MSG_ENCRYPT, requestId, url,
					request.body().toString());

			// Send post request
			Buffer buffer = new Buffer();
			try {
				body.writeTo(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Timber.tag("Location encrpt ReqBody").d(buffer.readUtf8());
			try {
				OkHttpClient.Builder builder = new OkHttpClient.Builder();
				builder.connectTimeout(90, TimeUnit.SECONDS);
				builder.readTimeout(90, TimeUnit.SECONDS);
				builder.writeTimeout(90, TimeUnit.SECONDS);
				client = builder.build();
				Response response = client.newCall(request).execute();

				String res = response.body().string();
				Timber.d("Responce w/o decrypt: [" + requestId + "] : " + res);
				Timber.d("Responce with decrypt: [" + requestId + "] : " + new String(
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
		Timber.d(headerMsg + ": [" + requestId + "] URL=" + url + ", HEADER=" + header);
	}
}

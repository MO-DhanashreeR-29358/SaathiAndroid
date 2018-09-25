package com.mosl.tabapp.ws;

import android.content.Context;
import android.content.SharedPreferences;
import com.mosl.tabapp.common.CRMSecurityUtil;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;

public class HeaderInterceptor implements Interceptor {
    private Context context;

    public HeaderInterceptor(Context context) {
        this.context = context;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        SharedPreferences prefs = getPrefs(context);
        request = request.newBuilder()
            .removeHeader("Content-Length")
            .removeHeader("Content-Type")
            .removeHeader("AppId")
            .removeHeader("clientinfo")
            .removeHeader("EmployeeCode")
            .removeHeader("token")
            .removeHeader("userid")
            .removeHeader("ipaddress")
            .removeHeader("BusinessUnitId")
            .removeHeader("macaddress")
            .header("Content-Type", CRMSecurityUtil.encrypt("application/json; charset=UTF-8"))
            //.header("Content-Type", "application/json; charset=UTF-8")
            .header("AppId", CRMSecurityUtil.encrypt("2"))
            .header("clientinfo", CRMSecurityUtil.encrypt("android"))
            .header("EmployeeCode", CRMSecurityUtil.encrypt(prefs.getString("EmployeeCode", null)))
            .header("token", CRMSecurityUtil.encrypt(prefs.getString("token", null)))
            .header("userid", CRMSecurityUtil.encrypt(prefs.getString("userid", null)))
            .header("ipaddress", CRMSecurityUtil.encrypt(prefs.getString("ipaddress", null)))
            .header("BusinessUnitId",
                CRMSecurityUtil.encrypt(prefs.getString("BusinessUnitId", null)))
            .header("macaddress", CRMSecurityUtil.encrypt(prefs.getString("macaddress", null)))
            .build();
        return chain.proceed(request);
    }
}

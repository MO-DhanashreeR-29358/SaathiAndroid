package com.mosl.tabapp.ws;

import android.content.Context;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.CRMSecurityUtil;
import com.mosl.tabapp.model.AccountDetails;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    private final Context context;

    public HeaderInterceptor(Context context) {
        this.context = context;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
            .header("Content-Type", CRMSecurityUtil.encrypt("application/json; charset=UTF-8"))
            .header("token", CRMSecurityUtil.encrypt(AccountDetails.getToken()))
            .header("userid", CRMSecurityUtil.encrypt(AccountDetails.getUserId()))
            .header("clientinfo", CRMSecurityUtil.encrypt("android"))
            .header("ipaddress", CRMSecurityUtil.encrypt(CRMAppUtil.getLocalIpAddress()))
            .header("macaddress", CRMSecurityUtil.encrypt(
                CRMMOSLCommon.getDeviceID(context, context.getContentResolver())))
            .header("BusinessUnitId", CRMSecurityUtil.encrypt(AccountDetails.getBusinessUnitId()))
            .header("EmployeeCode", CRMSecurityUtil.encrypt(AccountDetails.getEmployeeCode()))
            .header("AppId", CRMSecurityUtil.encrypt("2"))
            .build();

        return chain.proceed(request);
    }
}

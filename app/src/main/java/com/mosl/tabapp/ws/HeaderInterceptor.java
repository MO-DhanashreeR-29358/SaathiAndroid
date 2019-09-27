package com.mosl.tabapp.ws;

import android.content.Context;
import com.mosl.app.core.utils.Injector;
import com.mosl.mobile.user.login.UserManager;
import com.mosl.tabapp.AppComponent;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.CRMSecurityUtil;
import java.io.IOException;
import java.util.Objects;
import javax.inject.Inject;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class HeaderInterceptor implements Interceptor {
	private final Context context;
	@Inject
	UserManager userManager;

	HeaderInterceptor(Context context) {

		this.context = context;
		Injector.obtain(context, AppComponent.class).inject(this);
	}

	@NotNull @Override public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		request = request.newBuilder()
			.header("Content-Type",
				Objects.requireNonNull(CRMSecurityUtil.encrypt("application/json; charset=UTF-8")))
			.header("token",
				Objects.requireNonNull(CRMSecurityUtil.encrypt(
					Objects.requireNonNull(userManager.getUser()).token)))
			.header("userid",
				Objects.requireNonNull(
					CRMSecurityUtil.encrypt(userManager.getUser().userId)))
			.header("clientinfo", Objects.requireNonNull(CRMSecurityUtil.encrypt("android")))
			.header("ipaddress",
				Objects.requireNonNull(CRMSecurityUtil.encrypt(CRMAppUtil.getLocalIpAddress())))
			.header("macaddress", Objects.requireNonNull(CRMSecurityUtil.encrypt(
				CRMMOSLCommon.getDeviceID(context, context.getContentResolver()))))
			.header("BusinessUnitId",
				Objects.requireNonNull(CRMSecurityUtil.encrypt(
					userManager.getUser().currentBusinessProfile.businessUnitId)))
			.header("EmployeeCode",
				Objects.requireNonNull(CRMSecurityUtil.encrypt(userManager.getUser().employeeCode)))
			.header("AppId", Objects.requireNonNull(CRMSecurityUtil.encrypt("2")))
			.build();

		return chain.proceed(request);
	}
}

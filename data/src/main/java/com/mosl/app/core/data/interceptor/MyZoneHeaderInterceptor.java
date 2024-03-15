package com.mosl.app.core.data.interceptor;

import androidx.annotation.NonNull;

import com.mosl.core.annotations.ServerTime;
import com.mosl.mobile.domain.attendance.AppInfo;
import com.mosl.user.prefs.LongPreference;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lalkishore on 23-03-2018.
 */
@Singleton public class MyZoneHeaderInterceptor implements Interceptor {
    private static final String API_HEADER_DATE = "Date";
    public static final String NO_TIMEOUT = "No-Timeout";
    private final String userAgent;
    private final @ServerTime
    LongPreference serverTimePref;

    @Inject
    MyZoneHeaderInterceptor(@ServerTime LongPreference serverTimePref, AppInfo appInfo) {
        this.userAgent = appInfo.toString();
        //this.userAgent = "MyzOne 2.0/Android/3.0.37(3037)";
        this.serverTimePref = serverTimePref;
    }

    @Override public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder();

        builder.header("User-Agent", userAgent);

        Response response;
        if (original.header(NO_TIMEOUT) != null) {
            builder.removeHeader(NO_TIMEOUT);
            response = chain.withConnectTimeout(60000, TimeUnit.MILLISECONDS)
                .withReadTimeout(60000, TimeUnit.MILLISECONDS)
                .proceed(builder.build());
        } else {
            response = chain.proceed(builder.build());
        }

        Date date = response.headers().getDate(API_HEADER_DATE);
        if (date != null) {
            serverTimePref.set(date.getTime());
        }

        return response;
    }
}

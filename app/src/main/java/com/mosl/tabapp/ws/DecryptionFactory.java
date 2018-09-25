package com.mosl.tabapp.ws;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class DecryptionFactory implements Interceptor {

    @Override public Response intercept(Chain chain) throws IOException {

        final Request original = chain.request();
        final HttpUrl originalHttpUrl = original.url();

        final HttpUrl url = originalHttpUrl.newBuilder()
            //    .addQueryParameter("username", "demo")
            .build();

        // Request customization: add request headers
        final Request.Builder requestBuilder = original.newBuilder().url(url);

        Request request = requestBuilder.build();
        Response response = chain.proceed(request);

        Timber.v("Request url  : [ %s ] -> [ %s ]", request.method(), request.url().toString());
        Timber.v("Request body : %s", request.body());
        Timber.v("Request head : %s", request.headers());

        Timber.v("Response url  : [ %s ] -> [ %s ]", request.method(), request.url().toString());
        // Timber.v("Response body : [ %d ] -> %s",   response.code(),response.body());
        Timber.v("Response head : %s", response.headers());

          /*  Response response1=CRMSecurityUtil.decrypt(
                String.valueOf(response));*/

        return response;
    }
}

package com.mosl.tabapp.ws;

import android.content.Context;
import android.content.Intent;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonActivity;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

public class AuthorizationInterceptor implements Interceptor {
    private final Context context;

    AuthorizationInterceptor(Context context) {
        this.context = context;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder();
        Response response = chain.proceed(builder.build());
        int code = response.code();
        Timber.d("code is %s", code);
        HttpUrl url = response.request().url();
        String s = url.toString();

        if (s.equals("http://saathi.motilaloswal.com:81/api/Client/GetBirthdayAnniversary")) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            if (context instanceof MOSLCommonActivity) {
                ((MOSLCommonActivity) context).finish();
            }
        }
        Timber.d("path segments %s", url.toString());
        if (code == 401) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            if (context instanceof MOSLCommonActivity) {
                ((MOSLCommonActivity) context).finish();
            }
        }
        return response;
    }
}

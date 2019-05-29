package com.mosl.tabapp;

import com.mosl.tabapp.common.CRMSecurityUtil;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class EncryptionInterceptor implements Interceptor {
    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        RequestBody oldBody = request.body();
        Buffer buffer = new Buffer();
        oldBody.writeTo(buffer);
        String strOldBody = buffer.readUtf8();

        MediaType mediaType = MediaType.parse("text/plain; charset=utf-8");
        String strNewBody = CRMSecurityUtil.encrypt(strOldBody);
        RequestBody body = RequestBody.create(mediaType, strNewBody);

        request = request.newBuilder().method(request.method(), body).build();

        Response response = chain.proceed(request);

        String strResponse = response.body().string();

        ResponseBody decryptedBody = ResponseBody
            .create(MediaType.parse("application/json"), CRMSecurityUtil.decrypt(strResponse));

        return response.newBuilder().body(decryptedBody).build();
    }
}

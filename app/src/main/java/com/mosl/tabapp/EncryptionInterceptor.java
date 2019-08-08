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
        boolean isEncrypted = false;
        if (oldBody != null) {
            Buffer buffer = new Buffer();
            oldBody.writeTo(buffer);
            String strOldBody = buffer.readUtf8();

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            String strNewBody = CRMSecurityUtil.encrypt(strOldBody);
            RequestBody body = RequestBody.create(mediaType, strNewBody);
            request = request.newBuilder().method(request.method(), body).build();
            isEncrypted = true;
        }

        Response response = chain.proceed(request);
        if (isEncrypted && response.body() != null) {
            try {
                String strResponse = response.body().string();
                byte[] decrypted = CRMSecurityUtil.decrypt(strResponse);
                if (decrypted != null) {
                    ResponseBody decryptedBody = ResponseBody.create(
                        MediaType.parse("application/json"),
                        decrypted
                    );
                    return response.newBuilder().body(decryptedBody).build();
                } else {
                    return response;
                }
            } catch (Exception e) {
                return response;
            }
        } else {
            return response;
        }
    }
}

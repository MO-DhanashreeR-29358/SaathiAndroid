package com.mosl.tabapp.leadcreationold;

import io.reactivex.Observable;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface RefreeApi {
    @POST("Common/GetRefereeCode") Observable<String> getRefreeByCode(
        @HeaderMap Map<String, String> headers, @Body String request);

    @POST("Common/GetRefereeCode") Observable<String> getRefreeWithoutHeader(@Body String request);
}

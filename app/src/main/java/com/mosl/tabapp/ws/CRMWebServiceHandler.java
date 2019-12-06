package com.mosl.tabapp.ws;

import android.content.Context;

import com.mosl.app.core.rx.DispatchersKt;
import com.mosl.saathi.api.SaathiApi;
import com.mosl.saathi.data.MOSLResponseWrapper;
import com.mosl.tabapp.EncryptionInterceptor;
import com.mosl.tabapp.common.CRMSecurityUtil;
import com.mosl.tabapp.urls.ServiceUrlsImpl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by pramod on 23/03/17.
 */

public class CRMWebServiceHandler {
    private static String TAG = CRMWebServiceHandler.class.getName();
    private static String UAT_BASE_URL = "http://122.184.137.226:83/api/";
    private static String STAGING_BASE_URL = "http://122.184.137.226:85/api/";
    private static String LIVE_BASE_URL = "http://saathi.motilaloswal.com:81/api/";

    public static final String COLLATERAL_URL_UAT =
            "http://web20.motilaloswal.com:8000/Brochure/index.html";
    public static final String COLLATERAL_URL_PROD =
            "https://www.motilaloswal.com/EKYCCONTROLLER/Brochure/index.html";
    public static final String INCENTIVE_PDF_URL =
            "http://122.184.137.84:9876/Uploads/LeadDocuments/family%20Declaration%20-%20Annexure%201636293853847628119.pdf";

    private static final String BASE_IP_UAT_UPPERMOST = "http://122.184.137.89/";
    private static final String BASE_IP_PROD_UPPERMOST = "https://login.motilaloswal.com/";

    private static final String BASE_URL_UAT_UPPERMOST =
            BASE_IP_UAT_UPPERMOST + "Partner/PartnerMobileWebServices.asmx/";
    private static final String BASE_URL_PROD_UPPERMOST =
            BASE_IP_PROD_UPPERMOST + "Partner/PartnerMobileWebServices.asmx/";

    public static final String CALL_URL_UPPERMOST = ServiceUrlsImpl.UPPERMOST_URL();
    public static String CALL_BASE_URL = ServiceUrlsImpl.CRM_BASE_URL();
    public static String COLLATERAL_URL = ServiceUrlsImpl.CRM_COLLATERAL_URL();
    public static String SALES_STADIUM_PITCH_URL = ServiceUrlsImpl.SALES_STADIUM_PITCH_URL();

    public static String getJsonString(Map param) {
        return getJsonString(param, false);
    }


    public static String getJsonString(Map param, boolean encrypt) {
        String result = "";
        if (param != null && param.size() > 0) {
            result += "{";
            Set<String> keys = param.keySet();
            for (String key : keys) {
                Object obj = param.get(key);
                String value = "";

                if (obj instanceof String) {
                    value = param.get(key) == null ? "" : param.get(key).toString();
                    if (encrypt) {
                        value = CRMSecurityUtil.encrypt(value);
                    }
                    result += "\"" + key + "\"" + ":\"" + value + "\",";
                } else if (obj instanceof Map) {
                    Map map = (Map) obj;
                    value = getJsonString(map, encrypt);
                    //Tdo remove this after testing

                    result += "\"" + key + "\"" + ":" + value + ",";
                } else if (obj != null) {//default case in case not string or map consider int
                    result += "\"" + key + "\"" + ":" + String.valueOf(obj) + ",";
                    //result += "\"" + key + "\"" + ":" + CRMSecurityUtil.encrypt(String.valueOf(obj)) + ",";
                    //   return String.valueOf(obj);
                }
            }

            //remove last comma symbol
            result = result.substring(0, result.length() - 1);
            result += "}";
        }
        return result;
    }

    private static SaathiApi saathiApi;

    private static SaathiApi buildSaathiApi(Context context) {
        RxJava2CallAdapterFactory callAdapterFactory =
                RxJava2CallAdapterFactory.createWithScheduler(DispatchersKt.RxDispatchers.io);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client =
                new OkHttpClient.Builder()
                        .readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(new HeaderInterceptor(context))
                        .addInterceptor(new AuthorizationInterceptor(context))
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(new EncryptionInterceptor())
                        .build();

        return new Retrofit.Builder().baseUrl(CALL_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(new MOSLResponseWrapper())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(
                        callAdapterFactory)
                .client(client)
                .build()
                .create(SaathiApi.class);
    }

    public static void initSaathiApi(Context context) {
        if (saathiApi == null) {
            saathiApi = buildSaathiApi(context);
        }
    }

    public static SaathiApi getSaathiApi() {
        if (saathiApi == null) {
            throw new IllegalStateException("Must call initSaathiApi first");
        }
        return saathiApi;
    }
}

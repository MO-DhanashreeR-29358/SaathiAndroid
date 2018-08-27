package com.mosl.tabapp.ws;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mosl.tabapp.BuildConfig;
import com.mosl.tabapp.common.CRMSecurityUtil;

import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by pramod on 23/03/17.
 */

public class CRMWebServiceHandler {
    private static String TAG = CRMWebServiceHandler.class.getName();
    private static String UAT_BASE_URL = "http://122.184.137.226:83/api/";
    private static String STAGING_BASE_URL = "http://122.184.137.226:85/api/";
    private static String LIVE_BASE_URL = "http://saathi.motilaloswal.com:81/api/";

    public static final String COLLATERAL_URL_UAT = "http://web20.motilaloswal.com:8000/Brochure/index.html";
    public static final String COLLATERAL_URL_PROD = "https://www.motilaloswal.com/EKYCCONTROLLER/Brochure/index.html";
    public static final String INCENTIVE_PDF_URL = "http://122.184.137.84:9876/Uploads/LeadDocuments/family%20Declaration%20-%20Annexure%201636293853847628119.pdf";

    private static final String BASE_IP_UAT_UPPERMOST = "http://122.184.137.89/";
    private static final String BASE_IP_PROD_UPPERMOST = "https://login.motilaloswal.com/";

    private static final String BASE_URL_UAT_UPPERMOST = BASE_IP_UAT_UPPERMOST + "Partner/PartnerMobileWebServices.asmx/";
    private static final String BASE_URL_PROD_UPPERMOST = BASE_IP_PROD_UPPERMOST + "Partner/PartnerMobileWebServices.asmx/";

    private static String MSG_NORMAL = "Request_Normal";
    private static String MSG_ENCRYPT = "Request_Encrypt";


    /* public static final String CALL_URL_UPPERMOST = ServiceUrlsImpl.UPPERMOST_URL();
     public static String CALL_BASE_URL = ServiceUrlsImpl.CRM_BASE_URL();
     public static String COLLATERAL_URL = ServiceUrlsImpl.CRM_COLLATERAL_URL();*/
    public static String CALL_BASE_URL = BuildConfig.CRM_BASE_URL;
    public static final String CALL_URL_UPPERMOST = BuildConfig.UPPERMOST_URL;
    public static String COLLATERAL_URL = BuildConfig.CRM_COLLATERAL_URL;




    public static AsyncHttpClient getClient() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.setMaxRetriesAndTimeout(1, 10000);
        return client;
    }

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


    public static void post(Context context, String serviceMethod, Map<String, String> headerMap, Map inputParam, final AsyncHttpResponseHandler responseHandler) throws Exception {
        String requestJson = getJsonString(inputParam);
        post(context, serviceMethod, headerMap, requestJson, responseHandler);
    }

    public static void postWithRequestAndFilterObject(Context context, String serviceMethod, Map<String, String> headerMap, Map inputParam, final JsonHttpResponseHandler responseHandler) throws Exception {
        String requestJson = getJsonString(inputParam);
        String filter = "{\"Export\":null,\"ExportFilter\":null,\"Field\":null,\"Operator\":null,\"Value\":null,\"Logic\":null,\"Filters\":null},\"IsAdminMode\":true";
        String mainJson = "{\"Request\":" + requestJson + ",\"Filter\":" + filter + "}";
        post(context, serviceMethod, headerMap, mainJson, responseHandler);
    }


    public static void postWithRequestAndFilterObjectBirthdayAndAniversary(Context context, String serviceMethod, Map<String, String> headerMap, Map inputParam, String clientPotential, final JsonHttpResponseHandler responseHandler) throws Exception {
        String requestJson = getJsonString(inputParam);
        String filter = "{\"Logic\":\"and\",\"Filters\":[{\"Field\":\"ClientPotential\",\"Operator\":\"startswith\",\"Value\":[\"" +
                clientPotential +
                "\"]}]},\"IsAdminMode\":true";
        String mainJson = "{\"Request\":" + requestJson + ",\"Filter\":" + filter + "}";
        post(context, serviceMethod, headerMap, mainJson, responseHandler);
    }

    public static void post(final Context context, String serviceMethod, Map<String, String> headerMap, String requestJson, final AsyncHttpResponseHandler responseHandler) throws Exception {
        String contentType = "application/json";
        String headerJsonNormal = getJsonString(headerMap);
        String headerJsonEncrypted = getJsonString(headerMap, true);
        //this replacement is done to avoid error on server
        String dataToSend = requestJson.replace("\"null\"", "null");
        String requestJsonEncrypted = CRMSecurityUtil.encrypt(dataToSend);
        StringEntity entity = new StringEntity(requestJsonEncrypted);

        final String URL = CALL_BASE_URL + serviceMethod;

        final int requestId = (int) (Math.random() * 5000);
        printLog(MSG_NORMAL, requestId, URL, headerJsonNormal, requestJson);
        printLog(MSG_ENCRYPT, requestId, URL, headerJsonEncrypted, requestJsonEncrypted);

        AsyncHttpClient client = getClient();
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                client.addHeader(entry.getKey(), CRMSecurityUtil.encrypt(entry.getValue()));
            }
        }

        client.post(context, URL, entity, contentType, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "Response Encrypt: [" + requestId + "] : " + new String(responseBody));
                Log.d(TAG, "Response Normal: [" + requestId + "] : " + new String(CRMSecurityUtil.decrypt(new String(responseBody))));
                responseHandler.onSuccess(statusCode, headers, CRMSecurityUtil.decrypt(new String(responseBody)));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "Response: [" + requestId + "] : " + error);
                if (responseBody != null) {
                    responseHandler.onFailure(statusCode, headers, CRMSecurityUtil.decrypt(new String(responseBody)), error);
                } else {
                    // response body comes null
                    responseBody = "{}".getBytes();
                    responseHandler.onFailure(statusCode, headers, CRMSecurityUtil.decrypt(new String(responseBody)), error);
                }
            }
        });

    }


    private static void printLog(String headerMsg, int requestId, String url, String header, String body) {
        Log.d(TAG, headerMsg + ": [" + requestId + "] URL=" + url + ", HEADER=" + header + ", REQUEST=" + body);
    }

    //get
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        Log.d("ReqPath", CALL_URL_UPPERMOST + relativeUrl);
        return CALL_URL_UPPERMOST + relativeUrl;
    }

}

package com.mosl.mobile.ekyc.service;

import android.content.Context;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mosl.mobile.ekyc.common.EkycUtil;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import cz.msebera.android.httpclient.Header;
import java.util.Map;
import java.util.Set;

import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;

/**
 * Created by pramod on 29-08-2016.
 */
public class WebServiceHandler implements MOSLConstants {
    private static String TAG = "WebServiceHandler";
    private static final String BASE_IP_UAT = "https://web20.motilaloswal.com/";
   // private static final String BASE_IP_UAT = "http://web20.motilaloswal.com:8000/";
    private static final String BASE_IP_PROD = "https://www.motilaloswal.com/";

    //Without encrypt
//    private static final String BASE_URL_UAT = BASE_IP_UAT + "EKYCMOBILEAPPCONTROLLER/APIRequest.aspx";
//    private static final String BASE_URL_PROD = BASE_IP_PROD + "EKYCCONTROLLER/apirequest.aspx";

    //With encrypt
    private static final String BASE_URL_UAT = BASE_IP_UAT + "EKYCMOBILEAPPCONTROLLER/APIServiceResponse.aspx";
//    private static final String BASE_URL_UAT = BASE_IP_UAT + "EKYCMOBILEAPPCONTROLLER_ADV/APIServiceResponse.aspx";
    private static final String BASE_URL_PROD = BASE_IP_PROD + "EKYCCONTROLLER/APIServiceResponse.aspx";
    private static final String BASE_URL_UAT_NEW_CRM = BASE_IP_UAT + "EKYC_NEWCRMMOBILEAPPCONTROLLER/APIServiceResponse.aspx";
//    private static final String BASE_URL_UAT_NEW_CRM = BASE_URL_UAT;

//    private static final String BASE_URL_UAT_VALIDATE_ADHAR = BASE_IP_UAT + "ekycBiometric/api/Validate/ValidateAaddhar";
//    private static final String BASE_URL_UAT_GENERATE_OTP = BASE_IP_UAT + "ekycbiometric/api/biometric/generateotp";
//    private static final String BASE_URL_UAT_VERIFY_OTP = BASE_IP_UAT + "ekycbiometric/api/biometric/FetchAadharDetailsOTP";

    public static final String COLLATERAL_URL_UAT = "http://web20.motilaloswal.com:8000/Brochure/index.html";
    public static final String COLLATERAL_URL_PROD = "https://www.motilaloswal.com/EKYCCONTROLLER/Brochure/index.html";
//    public static final String COLLATERAL_URL = "http://drive.google.com/viewerng/viewer?embedded=true&url=http://www.pdf995.com/samples/pdf.pdf";
//    public static final String COLLATERAL_URL = "https://drive.google.com/viewerng/viewer?embedded=true&url=http://213.136.84.59/files/docs/collateral_ppt.pdf";


    //SignalR URL
//    private static final String SIGNALR_URL_UAT = "https://web20.motilaloswal.com/open-an-account_Adv/";
    private static final String SIGNALR_URL_UAT = "https://web20.motilaloswal.com/open-an-account/";
    private static final String SIGNALR_URL_UAT_NEW_CRM = "https://web20.motilaloswal.com/open-an-account_newcrm/";
//    private static final String SIGNALR_URL_UAT_NEW_CRM = SIGNALR_URL_UAT;
    private static final String SIGNALR_URL_PROD = "https://www.motilaloswal.com/open-an-account/";

    public static final String CRM_URL_UAT = "http://122.184.137.25:1234/frmLogin.aspx";
    public static final String CRM_URL_PROD = "http://crm.motilaloswal.com/frmlogin2.aspx";

    //UAT
	//    public static final String CALL_URL = BASE_URL_UAT;
	//    public static final String SIGNALR_URL = SIGNALR_URL_UAT;
	//    public static final String CRM_URL = CRM_URL_UAT;
	//    public static final String COLLATERAL_URL = COLLATERAL_URL_UAT;

    //UAT New CRM
    //public static final String CALL_URL = BASE_URL_UAT_NEW_CRM;
    //public static final String SIGNALR_URL = SIGNALR_URL_UAT_NEW_CRM;
    //public static final String CRM_URL = CRM_URL_UAT;
    //public static final String COLLATERAL_URL = COLLATERAL_URL_UAT;

    //PROD
	public static final String CALL_URL = BASE_URL_PROD;
	public static final String SIGNALR_URL = SIGNALR_URL_PROD;
	public static final String CRM_URL = CRM_URL_PROD;
	public static final String COLLATERAL_URL = COLLATERAL_URL_PROD;

    ////////////////////// WEB-RTC \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    private static String MEDIA_SERVER_UAT = "https://www.motilaloswal.com/webrtc/remotemobile.aspx";
    private static String MEDIA_SERVER_PROD = "https://www.motilaloswal.com/webrtc/remotemobile.aspx";
    //String MEDIA_SERVER = MEDIA_SERVER_LOCAL;
    public static String MEDIA_SERVER = MEDIA_SERVER_PROD;

    public static final String UID_ENV = CALL_URL.equals(BASE_URL_PROD) ? "P" : "PP";
    public static final String UID_ENV_NSDL = CALL_URL.equals(BASE_URL_PROD) ? "PROD" : "PREPROD";

    public static AsyncHttpClient getClient() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.setMaxRetriesAndTimeout(1, 10000);
        return client;
    }

    public static String getInputBody(Map param) {
        String result = "";
        if (param != null && param.size() > 0) {
            result += "{";
            Set<String> keys = param.keySet();
            for (String key : keys) {
                String value = param.get(key) == null ? null : param.get(key).toString();
                if (value == null) {
                    result += key + ":" + value + ",";
                } else {
                    result += key + ":\"" + value + "\",";
                }
            }

            //remove last comma symbol
            result = result.substring(0, result.length() - 1);
            result += "}";
        }
        return result;
    }

    private static void printLog(int requestId, String url, String flag, String category, String json) {

        Log.d(TAG, "Request_PLAIN: [" + requestId + "] URL=" + url + ", FLAG=" + flag + ", CATEGORY=" + category + ", INPUT=" + json);
        Log.d(TAG, "Request_ENCRYPT: [" + requestId + "] URL=" + url + ", FLAG=" + EkycUtil.encrypt(flag) + ", CATEGORY=" + EkycUtil.encrypt(category) + ", INPUT=" + EkycUtil.encrypt(json));
    }

    public static void post(Context context, String flag, String category, Map inputParam, final JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.add(FLAG, EkycUtil.encrypt(flag));
        params.add(CATEGORY, EkycUtil.encrypt(category));
        String inputBody = getInputBody(inputParam);
        params.add(INPUT, EkycUtil.encrypt(inputBody));

        final int requestId = (int) (Math.random() * 5000);
        printLog(requestId, CALL_URL, flag, category, inputBody);

        getClient().post(context, CALL_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                responseHandler.onSuccess(statusCode, headers, EkycUtil.decrypt(new String(responseBody)));
//                responseHandler.onSuccess(statusCode, headers, responseBody);
                Log.d(TAG, "Response_ENCRYPT: [" + requestId + "] : " + new String(responseBody));
                if (!isBlank(new String(responseBody))) {
                    Log.d(TAG, "Response_PLAIN: [" + requestId + "] : " + new String(EkycUtil.decrypt(new String(responseBody))));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                responseHandler.onFailure(statusCode, headers, responseBody, error);
                Log.d(TAG, "Response: [" + requestId + "] : " + error);
            }
        });
    }

    /*public static void post(Context context, String aadharNum, String userID, final JsonHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();

        params.add(AADHAR_NUMBER, aadharNum);
        params.add("Userid", userID);

        getClient().post(context, BASE_URL_UAT_VALIDATE_ADHAR, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                responseHandler.onSuccess(statusCode, headers, responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                responseHandler.onFailure(statusCode, headers, responseBody, error);
            }
        });

    }*/

    /*public static void postWithBody(Context context, Map requestBody, int api, AsyncHttpResponseHandler responseHandler) {
        String url = "";
        if (api == VALIDATE_AADHAR) {
            url = BASE_URL_UAT_VALIDATE_ADHAR;
        } else if (api == GENERATE_OTP) {
            url = BASE_URL_UAT_GENERATE_OTP;
        } else if (api == VERIFY_OTP) {
            url = BASE_URL_UAT_VERIFY_OTP;
        }
        try {
            String inputBody = getInputBody(requestBody);
            StringEntity entity = new StringEntity(inputBody);

            int requestId = (int)(Math.random()*5000);
            printLog(requestId,url, "N/A", "N/A", inputBody);
            getClient().post(context, url, entity, "application/json",
                    responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*public static void validateBiometricUser(Context context, AuthRequest requestModel, final JsonHttpResponseHandler responseHandler) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(requestModel);
        RequestParams params = new RequestParams();
        params.add(FLAG, EkycUtil.encrypt("FETCHAADHARDETAILSIRISBIOMETRIC"));
        params.add(CATEGORY, EkycUtil.encrypt("PANADDHARDETAILS"));
        params.add(INPUT, EkycUtil.encrypt(jsonBody));

        getClient().post(context, CALL_URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                responseHandler.onSuccess(statusCode, headers, responseBody);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                responseHandler.onFailure(statusCode, headers, responseBody, error);
            }
        });
    }*/
}

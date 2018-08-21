package com.mosl.tabapp.ws;


import android.util.Log;

import com.mosl.tabapp.common.CRMSecurityUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.mosl.tabapp.ws.CRMWebServiceHandler.getJsonString;


/**
 * Created by Pramod Kumar on 28-Mar-2017
 */
public class WebServiceDirectCall {
    private static String TAG=WebServiceDirectCall.class.getCanonicalName();

    private static String MSG_NORMAL="Request_Normal";
    private static String MSG_ENCRYPT="Request_Encrypt";


    public static JSONObject getData(String serviceMethod, Map <String,String>headerMap, Map inputParam) {
        String url = CRMWebServiceHandler.CALL_BASE_URL;
        URL obj = null;
        try {
            String requestJsonNormal = getJsonString(inputParam);


            String headerJsonNormal = getJsonString(headerMap);
            String headerJsonEncrypted = getJsonString(headerMap,true);

            String requestJsonEncrypted = CRMSecurityUtil.encrypt(requestJsonNormal);

            url = url+serviceMethod;

            obj = new URL(url);
            HttpURLConnection urlConn = (HttpURLConnection) obj.openConnection();
            urlConn.setDoInput (true);
            urlConn.setDoOutput (true);
            urlConn.setUseCaches (false);
            //add request header
            urlConn.setRequestMethod("POST");

            if(headerMap!=null){
                for (Map.Entry<String, String> entry : headerMap.entrySet())
                {
                    urlConn.setRequestProperty(entry.getKey(), CRMSecurityUtil.encrypt(entry.getValue()));
                }
            }

            //urlConn.connect();

            final int requestId = (int)(Math.random()*5000);
            //printLog(requestId,url,headerJson,requestJson);

            printLog(MSG_NORMAL,requestId, url, headerJsonNormal, requestJsonNormal);
            printLog(MSG_ENCRYPT,requestId, url, headerJsonEncrypted, requestJsonEncrypted);


            // Send post request
            OutputStream os = urlConn.getOutputStream();
            os.write(requestJsonEncrypted.getBytes("UTF-8"));
            os.close();

            StringBuffer response = new StringBuffer();

            if(urlConn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConn.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                //print result
                //Log.d(TAG, "Response: [" + requestId + "] : " + response.toString());

                Log.d(TAG, "Response Encrypt: [" + requestId + "] : " + new String (response.toString()));
                Log.d(TAG, "Response Normal: [" + requestId + "] : " + new String (CRMSecurityUtil.decrypt(new String(response.toString()))));

            }

            return new JSONObject(new String (CRMSecurityUtil.decrypt(new String(response.toString()))));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void printLog(String headerMsg, int requestId, String url, String header, String body) {
        Log.d(TAG, headerMsg+": [" + requestId + "] URL=" + url + ", HEADER=" + header + ", REQUEST=" + body);
    }

}

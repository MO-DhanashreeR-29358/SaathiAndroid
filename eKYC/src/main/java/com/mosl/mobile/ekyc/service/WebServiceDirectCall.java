package com.mosl.mobile.ekyc.service;


import android.util.Log;

import com.mosl.mobile.ekyc.common.EkycUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import timber.log.Timber;


/**
 * Created by srv_admin on 2/11/2016.
 */
public class WebServiceDirectCall {
    private static String TAG=WebServiceDirectCall.class.getCanonicalName();

    public static JSONObject getData(String input, String flag, String category) {
        String url = WebServiceHandler.CALL_URL;
        URL obj = null;
        try {
            obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //add reuqest header
            con.setRequestMethod("POST");
            //con.setRequestProperty("User-Agent", USER_AGENT);
            //con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters = "INPUT=" + input + "&FLAG=" + flag + "&CATEGORY=" + category;

            final int requestId = (int)(Math.random()*5000);
            printLog("Request_ENCRYPT",requestId,url,flag,category,input);

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            /*int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);*/

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //print result
            //Timber.v("Search lead", response.toString());
            String decryptResponse = new String(EkycUtil.decrypt(new String(response)));

            Log.d(TAG,"Response: ["+requestId+"] : "+decryptResponse);
//            Log.d(TAG,"Response: ["+requestId+"] : "+response.toString());
//            return new JSONObject(response.toString());
            return new JSONObject(decryptResponse);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void printLog(String logType,int requestId, String url, String flag, String category, String json) {
        Log.d(TAG, logType+": ["+requestId+"] URL=" + url + ", FLAG=" + flag + ", CATEGORY=" + category + ", INPUT=" + json);
    }
}

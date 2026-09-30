package com.mosl.app.core.data.interceptor;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mosl.core.annotations.ServerTime;
import com.mosl.mobile.domain.attendance.AppInfo;
import com.mosl.user.prefs.LongPreference;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by lalkishore on 23-03-2018.
 */
@Singleton public class MyZoneHeaderInterceptor implements Interceptor {
    private static final String API_HEADER_DATE = "Date";
    public static final String NO_TIMEOUT = "No-Timeout";
    private final String userAgent;
    private final @ServerTime
    LongPreference serverTimePref;

    private final Context context;

    @Inject
    MyZoneHeaderInterceptor(@ServerTime LongPreference serverTimePref, AppInfo appInfo, Context context) {
        this.userAgent = appInfo.toString();
        //this.userAgent = "MyzOne 2.0/Android/3.0.37(3037)";
        this.serverTimePref = serverTimePref;
        this.context = context;
    }

    @Override public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder();

        builder.header("User-Agent", userAgent);

        Response response;
        if (original.header(NO_TIMEOUT) != null) {
            builder.removeHeader(NO_TIMEOUT);
            response = chain.withConnectTimeout(60000, TimeUnit.MILLISECONDS)
                .withReadTimeout(60000, TimeUnit.MILLISECONDS)
                .proceed(builder.build());
        } else {
            response = chain.proceed(builder.build());
        }

        ResponseBody responseBody = null;
        String responseBodyString = "";
        // Log request and response details
        try {
            StringBuilder logBuilder = new StringBuilder();

            // Log URL and method
            logBuilder.append("Request URL: ").append(original.url()).append("\n");
            logBuilder.append("Request Method: ").append(original.method()).append("\n");

            // Log request headers
            logBuilder.append("Request Headers:\n");
            for (int i = 0; i < original.headers().size(); i++) {
                logBuilder.append(original.headers().name(i))
                        .append(": ")
                        .append(original.headers().value(i))
                        .append("\n");
            }

            // Log response code and message
            logBuilder.append("Response Code: ").append(response.code()).append("\n");
            logBuilder.append("Response Message: ").append(response.message()).append("\n");

             responseBody = response.body();
             responseBodyString = responseBody != null ? responseBody.string() : "";

            logBuilder.append("Response Body: ").append(responseBodyString).append("\n");

            // Write the log to file (make sure context is accessible here)
            writeLogToFile(context, logBuilder.toString());

        } catch (Exception e) {
            Log.e("Interceptor", "Failed to log request/response: " + e.getMessage());
        }


        Date date = response.headers().getDate(API_HEADER_DATE);
        if (date != null) {
            serverTimePref.set(date.getTime());
        }

        // Recreate the response so Retrofit can still read it
        MediaType contentType = responseBody != null ? responseBody.contentType() : null;
        ResponseBody newResponseBody = ResponseBody.create(contentType, responseBodyString);

        return response.newBuilder()
                .body(newResponseBody)
                .build();

    }

    public static File getDailyLogFile(Context context) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String fileName = "http_logs_" + date + ".txt";
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File logDir = new File(downloadsDir, "SAATHI_Logs");

        if (!logDir.exists()) {
            logDir.mkdirs();
        }

        return new File(logDir, fileName);
    }

    public static void writeLogToFile(Context context, String logText) {
        String timestamp = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String logLine = "[" + timestamp + "] " + logText;

        File logFile = getDailyLogFile(context);

        try (FileOutputStream fos = new FileOutputStream(logFile, true);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {
            writer.append(logLine);
            writer.newLine();
        } catch (IOException e) {
            Log.e("Logger", "Failed to write log: " + e.getLocalizedMessage());
        }
    }
}

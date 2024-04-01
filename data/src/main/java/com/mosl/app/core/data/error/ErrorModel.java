package com.mosl.app.core.data.error;

import android.content.Context;

import androidx.annotation.StringRes;

import com.mosl.app.core.R;

/**
 * Created by lalkishore on 1/23/2017.
 */

public class ErrorModel {
    @StringRes private final int message;
    private final String messageStr;
    private final int reason;
    private String resolvedMessage;

    public ErrorModel(int message, int reason) {
        this.message = message;
        this.messageStr = null;
        this.reason = reason;
    }

    public ErrorModel(String messageStr, int reason) {
        this.message = 0;
        this.messageStr = messageStr;
        this.reason = reason;
    }

    public static ErrorModel noData(int message) {
        return new ErrorModel(message, ErrorConstant.DATA_NOT_PRESENT);
    }

    public static ErrorModel noData() {
        return new ErrorModel(R.string.error_message_no_data_available,
            ErrorConstant.DATA_NOT_PRESENT);
    }

    public static ErrorModel emptyResult() {
        return new ErrorModel("No Result Found",
            ErrorConstant.DATA_NOT_PRESENT);
    }

    public String getResolvedMessage(Context context) {
        if (resolvedMessage == null) {
            resolvedMessage = message == 0 ? messageStr : context.getString(message);
        }
        return resolvedMessage;
    }

    public String getMessageStr() {
        return messageStr;
    }

    public int getMessage() {
        return message;
    }

    public int getReason() {
        return reason;
    }
}

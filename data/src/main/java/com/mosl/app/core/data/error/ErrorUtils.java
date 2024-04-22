package com.mosl.app.core.data.error;

import static com.mosl.app.core.data.error.ErrorConstant.DATA_NOT_PRESENT;
import static com.mosl.app.core.data.error.ErrorConstant.NETWORK_FAILURE_ERROR;
import static com.mosl.app.core.data.error.ErrorConstant.SERVICE_FAILURE_ERROR;
import static com.mosl.app.core.data.error.ErrorConstant.UNKNOWN_FAILURE;

import com.google.gson.Gson;
import com.mosl.app.core.R;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.saathi.api.model.attendance.HrmsErrorResponse;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by snehaganapuram on 31-08-2017.
 */

public class ErrorUtils {
    public static ErrorModel getErrorOnFailure(Throwable throwable) {
        Timber.e(throwable);
        int reason = checkException(throwable);
        String message = null;
        if (throwable instanceof ServiceFailureException) {
            message = ((ServiceFailureException) throwable).errorMessage;
        }
        if (throwable instanceof HttpException && ((HttpException) throwable).code() != 404
            && ((HttpException) throwable).code() != 401) {
            Gson gson = new Gson();
            Response<?> response = ((HttpException) throwable).response();
            if (response.errorBody() != null) {
                HrmsErrorResponse hrmsErrorResponse =
                    gson.fromJson(response.errorBody().charStream(), HrmsErrorResponse.class);
                return new ErrorModel(hrmsErrorResponse.errorMessage,
                    hrmsErrorResponse.errorSource);
            }
        }

        if (message != null) {
            return new ErrorModel(message, reason);
        }
        return new ErrorModel(getErrorMessages(reason), reason);
    }



    public static int checkException(Throwable t) {
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            return NETWORK_FAILURE_ERROR;
        } else if (t instanceof SocketTimeoutException) {
            return SERVICE_FAILURE_ERROR;
        } else if (t instanceof ServiceFailureException) {
            return DATA_NOT_PRESENT;
        } else {
            return UNKNOWN_FAILURE;
        }
    }

    public static int getErrorMessages(int typeOfError) {
        switch (typeOfError) {
            case NETWORK_FAILURE_ERROR:
                return R.string.network_failure;
            case SERVICE_FAILURE_ERROR:
                return R.string.service_failure;
            case DATA_NOT_PRESENT:
                return R.string.error_message_no_data_available;
            case UNKNOWN_FAILURE:
                return R.string.unknown_failure;
        }
        return R.string.unknown_failure;
    }
}

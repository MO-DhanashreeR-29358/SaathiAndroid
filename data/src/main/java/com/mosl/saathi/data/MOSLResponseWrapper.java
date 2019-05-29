package com.mosl.saathi.data;

import com.google.gson.reflect.TypeToken;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.saathi.saathiapi.model.common.SaathiCommonResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by lalkishore on 3/1/2017.
 */

public class MOSLResponseWrapper extends Converter.Factory {

    protected static Class<?> getRawType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) throw new IllegalArgumentException();
            return (Class<?>) rawType;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
        Retrofit retrofit) {
        if (isWrapped(type)) {
            return null;
        }
        Type envelopedType = TypeToken.getParameterized(SaathiCommonResponse.class, type).getType();
        final Converter<ResponseBody, SaathiCommonResponse<?>> delegate =
            retrofit.nextResponseBodyConverter(this, envelopedType, annotations);
        return (Converter<ResponseBody, Object>) value -> {
            SaathiCommonResponse<?> baseResponse = delegate.convert(value);
            String code = "ErrorOccurred";
            String message = "ErrorOccurred";
            if (baseResponse != null) {
                if (baseResponse.status) {
                    return baseResponse.response;
                }
                code = errorMessageFromResponse(baseResponse);
                message = code;
            }
            throw new ServiceFailureException(code, message);
        };
    }

    public static final String SERVER_ERROR_OCCURRED = "Something went wrong while getting response from server. Please try again.";

    protected String errorMessageFromResponse(SaathiCommonResponse<?> response) {
        if (response != null) {
            if (response.errors.isEmpty()) {
                return SERVER_ERROR_OCCURRED;
            } else {
                return response.errors.get(0).message;
            }
        } else {
            return SERVER_ERROR_OCCURRED;
        }
    }

    private boolean isWrapped(Type type) {
        if (type instanceof ParameterizedType) {
            Class<?> rawType = getRawType(type);
            return (rawType == SaathiCommonResponse.class);
        }
        return false;
    }
}

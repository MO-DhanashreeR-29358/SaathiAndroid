package com.mosl.app.core.data;

import com.google.gson.reflect.TypeToken;
import com.mosl.app.core.annotation.ExcludeWrapper;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.saathi.api.model.common.ExtraDataCommonResponse;
import com.mosl.saathi.api.model.common.ExtraDataWrapper;
import com.mosl.saathi.api.model.common.SaathiCommonResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by lalkishore on 3/1/2017.
 */

public class MOSLResponseWrapper extends Converter.Factory {

    public static final String SERVER_ERROR_OCCURRED =
        "Something went wrong while getting response from server. Please try again.";

    @Inject public MOSLResponseWrapper() {
    }

    @NotNull protected static Class<?> getRawType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) throw new IllegalArgumentException();
            return (Class<?>) rawType;
        }
        throw new IllegalArgumentException();
    }

    protected static String errorMessageFromResponse(SaathiCommonResponse<?> response) {
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

    public static boolean hasExcludeWrapper(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == ExcludeWrapper.class) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasExtraDataWrapper(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == ExtraDataWrapper.class) {
                return true;
            }
        }
        return false;
    }

    private static boolean isWrapped(Type type) {
        if (type instanceof ParameterizedType) {
            Class<?> rawType = getRawType(type);
            return (rawType == SaathiCommonResponse.class);
        }
        return false;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
        Retrofit retrofit) {
        if (isWrapped(type) || hasExcludeWrapper(annotations)) {
            return null;
        } else if (hasExtraDataWrapper(annotations)) {
            Type envelopedType =
                TypeToken.getParameterized(ExtraDataCommonResponse.class, type).getType();
            final Converter<ResponseBody, ExtraDataCommonResponse<?>> delegate =
                retrofit.nextResponseBodyConverter(this, envelopedType, annotations);
            return (Converter<ResponseBody, Object>) value -> {
                ExtraDataCommonResponse<?> baseResponse = delegate.convert(value);
                String code = "ErrorOccurred";
                String message = "ErrorOccurred";
                if (baseResponse != null) {
                    Object data = baseResponse.getData();
                    if (data != null) {
                        return data;
                    }
                }
                throw new ServiceFailureException(code, message);
            };
        } else {
            Type envelopedType =
                TypeToken.getParameterized(SaathiCommonResponse.class, type).getType();
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
    }
}

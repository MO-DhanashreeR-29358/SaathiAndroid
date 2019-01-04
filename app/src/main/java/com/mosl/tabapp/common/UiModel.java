package com.mosl.tabapp.common;

import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UiModel<T> {
    public final boolean inProgress;
    @Nullable public final T data;
    @Nullable public final Throwable error;

    public UiModel(boolean inProgress, @Nullable T data, @Nullable Throwable error) {
        this.inProgress = inProgress;
        this.data = data;
        this.error = error;
    }

    public static <T> UiModel<T> inProgress() {
        return new UiModel<>(true, null, null);
    }

    public static <T> UiModel<T> success(@NonNull T data) {
        return new UiModel<>(false, data, null);
    }

    public static <T> UiModel<T> failure(Throwable error) {
        return new UiModel<T>(false, null, error);
    }
}

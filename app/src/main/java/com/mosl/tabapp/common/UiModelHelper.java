package com.mosl.tabapp.common;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class UiModelHelper {
    public static <T> ObservableTransformer<T, UiModel<T>> applyUiModel() {
        return observable -> observable.map(UiModel::success)
            .onErrorReturn(UiModel::failure)
            .observeOn(AndroidSchedulers.mainThread())
            .startWith(UiModel.inProgress());
    }

    public static <T> ObservableTransformer<T, UiModel<T>> applyRvUiModel() {
        return observable -> observable.map(UiModel::success)
            .onErrorReturn(UiModel::failure)
            .startWith(UiModel.inProgress())
            .observeOn(AndroidSchedulers.mainThread());
    }
}

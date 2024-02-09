package com.mosl.user.data.login;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.ObservableTransformer;

/**
 * Created by krutikapatil on 22-02-2018.
 */

public class UiModelHelper {
    public static <T> ObservableTransformer<T, UiModel<T>> applyUiModel() {
        return observable -> observable.map(UiModel::success)
            .onErrorReturn(UiModel::error)
             .startWithItem(UiModel.inProgress())
            .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, UiModel<T>> applyRvUiModel() {
        return observable -> observable.map(UiModel::success)
            .onErrorReturn(UiModel::error)
            .startWithItem(UiModel.inProgress())
            .observeOn(AndroidSchedulers.mainThread());
    }
}
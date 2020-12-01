package com.mosl.tabapp;

import android.content.Context;

import com.mosl.tabapp.helper.shortcuts.ShortcutHelper;
import com.mosl.tabapp.helper.shortcuts.ShortcutHelperKt;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module public abstract class CommonUiModel {

	@Provides @Singleton

	static ShortcutHelper provideShortcutHelper(Context context) {

		return ShortcutHelperKt.getShortcutHelper(context);
	}

}

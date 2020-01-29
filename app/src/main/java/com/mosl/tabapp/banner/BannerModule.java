package com.mosl.tabapp.banner;

import android.os.Bundle;

import com.mosl.tabapp.navigation.ScreenNavigatorImpl;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import static com.mosl.tabapp.banner.BannerView.ARG_BANNER_SCREEN_DATA;

@Module
public abstract class BannerModule {

    @Provides
    static BannerScreenData provideBannerScreenData(BannerFragment fragment) {
        Bundle args = fragment.getArguments();
        BannerScreenData data = args.getParcelable(ARG_BANNER_SCREEN_DATA);
        return data;
    }

    @Binds
    abstract BannerView provideBannerView(BannerFragment fragment);

    @Binds
    abstract BannerNavigator provideBannerNavigator(ScreenNavigatorImpl navigator);
}

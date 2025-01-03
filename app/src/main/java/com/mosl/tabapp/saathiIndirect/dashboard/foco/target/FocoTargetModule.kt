package com.mosl.tabapp.saathiIndirect.dashboard.foco.target

import android.os.Bundle
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import dagger.Module
import dagger.Provides


@Module
class FocoTargetModule {

    @Provides
    internal fun provideTargetView(fragment: FocoTargetFragment): FocoTargetView {
        return fragment
    }



}
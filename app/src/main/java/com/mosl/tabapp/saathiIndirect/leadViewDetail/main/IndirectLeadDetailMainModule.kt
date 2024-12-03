package com.mosl.tabapp.saathiIndirect.leadViewDetail.main

import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailArgument
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class IndirectLeadDetailMainModule {
    @Binds
    abstract fun bindIndirectLeadDetailMainView(view: IndirectLeadDetailMainFragment): IndirectLeadDetailMainView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideIndirectLeadDetailArgument(fragment: IndirectLeadDetailMainFragment): LeadViewDetailArgument {
            val args = fragment.arguments
            return LeadViewDetailArgument(
                leadId = args?.getString(MOSLConstants.LEADID, "0") ?: "0",
                ucId = args?.getString(MOSLConstants.UCID, "0") ?: "0"

            )
        }
    }
}
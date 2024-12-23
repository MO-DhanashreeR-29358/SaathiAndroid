package com.mosl.tabapp.saathiIndirect.leadViewDetail.leadDetail

import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import com.mosl.tabapp.constants.MOSLConstants
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class IndirectLeadDetailModule {
    @Binds
    abstract fun bindIndirectLeadDetailView(fragment: IndirectLeadDetailFragment): IndirectLeadDetailView

    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideIndirectLeadDetailArgument(fragment: IndirectLeadDetailFragment): LeadDetailArgument {
            val args = fragment.arguments
            return LeadDetailArgument(
                position = args?.getInt(MOSLConstants.POSITION, 0) ?: 0,
                type = args?.getString(MOSLConstants.TYPE, "0") ?: "0",
                leadId = args?.getString(MOSLConstants.LEADID, "0") ?: "0",
                ucId = args?.getString(MOSLConstants.UCID, "0") ?: "0"
            )
        }
    }
}
package com.mosl.mobile.domain.lead


data class LeadGroupItemModel(

    @JvmField val groupId: String,
    @JvmField val childId: String,
    @JvmField val rowFilter: String?
)
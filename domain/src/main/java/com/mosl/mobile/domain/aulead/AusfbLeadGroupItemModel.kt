package com.mosl.mobile.domain.aulead


data class AusfbLeadGroupItemModel(

    @JvmField val groupId: String,
    @JvmField val childId: String,
    @JvmField val rowFilter: String?
)
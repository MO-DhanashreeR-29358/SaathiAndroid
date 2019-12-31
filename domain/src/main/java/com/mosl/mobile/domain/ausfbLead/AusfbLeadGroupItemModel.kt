package com.mosl.saathi.api.model.dashboard.ausfbLead


data class AusfbLeadGroupItemModel(

    @JvmField val groupId: String,
    @JvmField val childId: String,
    @JvmField val rowFilter: String?
)
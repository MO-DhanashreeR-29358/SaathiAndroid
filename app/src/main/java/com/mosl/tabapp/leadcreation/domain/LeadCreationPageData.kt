package com.mosl.tabapp.leadcreation.domain

import com.mosl.saathi.api.model.lead.LookupItem

data class LeadCreationPageData constructor(
  @JvmField val categoryLookupItems: List<LookupItem>,
  @JvmField val professionLookupItems: List<LookupItem>
)
package com.mosl.saathi.api.model.prospect

import com.google.gson.annotations.SerializedName

data class ProspectRequest(
  @SerializedName("Mobile") @JvmField val mobile: String?,
  @SerializedName("Email") @JvmField val email: String?,
  @SerializedName("BranchRm_EmpCode") @JvmField val branchEmpCode: String?,
  @SerializedName("SourceName") @JvmField val source: String?,
  @SerializedName("ProspectSourceName") @JvmField val prospectSource: String?,
  @SerializedName("ProspectSourceDetails") @JvmField val prospectSourceDetails: String?,
  @SerializedName("InterestedIn") @JvmField val interestedIn: String?
)
package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class StageWiseConfiguration(
  @JvmField @SerializedName("ForDefaultProductOnly") val forDefaultProductOnly: Boolean,
  @JvmField @SerializedName("IsMandetory") val isMandatory: Boolean,
  @JvmField @SerializedName("FieldSystemName") val fieldSystemName: String?,
  @JvmField @SerializedName("FieldDisplayName") val fieldDisplayName: String?
)
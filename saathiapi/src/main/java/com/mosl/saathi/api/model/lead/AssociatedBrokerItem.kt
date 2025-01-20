package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class AssociatedBrokerItem(

  @SerializedName("ExistingBrokerID")
  @JvmField val brokerId: String?,

  @SerializedName("BrokerName")
  @JvmField val brokerName: String,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: String

)
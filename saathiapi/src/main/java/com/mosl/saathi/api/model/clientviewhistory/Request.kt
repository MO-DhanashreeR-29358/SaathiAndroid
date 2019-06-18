package com.mosl.saathi.api.model.clientviewhistory

import com.google.gson.annotations.SerializedName

data class Request(

  @JvmField @SerializedName("FamilyUCID")
  val familyUCID: Int? = 0,

  @JvmField @SerializedName("MobileNo")
  val mobileNo: Any? = null,

  @JvmField @SerializedName("PageSize")
  val pageSize: String? = "50",

  @JvmField @SerializedName("CalculatedPage")
  val calculatedPage: Int? = 0,

  @JvmField @SerializedName("ProductId")
  val productId: Int? = 0,

  @JvmField @SerializedName("Page")
  val page: Int? = 1,

  @JvmField @SerializedName("RequestSource")
  val requestSource: Any? = null,

  @JvmField @SerializedName("CountType")
  val countType: Any? = null,

  @JvmField @SerializedName("UCID")
  var uCID: String? = "0",

  @JvmField @SerializedName("TotalCount")
  val totalCount: Int? = 0,

  @JvmField @SerializedName("clientType")
  val clientType: Any? = null,

  @JvmField @SerializedName("ClientCode")
  val clientCode: Any? = null,

  @JvmField @SerializedName("BranchCode")
  val branchCode: Any? = null,

  @JvmField @SerializedName("Mappingtype")
  val mappingtype: Any? = null
)
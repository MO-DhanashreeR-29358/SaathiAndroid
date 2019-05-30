package com.mosl.saathi.api.model.reports.report

import com.google.gson.annotations.SerializedName

data class DataItem(

  @SerializedName("LEAD ASSIGNED TO")
  @JvmField val lEADASSIGNEDTO: String,

  @SerializedName("3RD LEVEL REPORTING")
  @JvmField val jsonMember3RDLEVELREPORTING: String,

  @SerializedName("CAMPAIGN SOURCE")
  @JvmField val cAMPAIGNSOURCE: String,

  @SerializedName("LEAD NAME")
  @JvmField val lEADNAME: String,

  @SerializedName("STATE")
  @JvmField val sTATE: String,

  @SerializedName("INTERESTED PRODUCT")
  @JvmField val iNTERESTEDPRODUCT: String,

  @SerializedName("1ST LEVEL REPORTING")
  @JvmField val jsonMember1STLEVELREPORTING: String,

  @SerializedName("BUSINESS")
  @JvmField val bUSINESS: String,

  @SerializedName("LEAD ASSIGNMENT DATE")
  @JvmField val lEADASSIGNMENTDATE: String,

  @SerializedName("LEAD EMAIL ID")
  @JvmField val lEADEMAILID: String,

  @SerializedName("BRANCH CITY")
  @JvmField val bRANCHCITY: String,

  @SerializedName("LEAD CONTACT NUMBER")
  @JvmField val lEADCONTACTNUMBER: String,

  @SerializedName("BRANCH CODE")
  @JvmField val bRANCHCODE: String,

  @SerializedName("BRANCH REGION")
  @JvmField val bRANCHREGION: String,

  @SerializedName("LEAD ID")
  @JvmField val lEADID: Int,

  @SerializedName("CITY")
  @JvmField val cITY: String,

  @SerializedName("BRANCH NAME")
  @JvmField val bRANCHNAME: String,

  @SerializedName("SOURCE")
  @JvmField val sOURCE: String,

  @SerializedName("LEAD CREATION DATE")
  @JvmField val lEADCREATIONDATE: String,

  @SerializedName("LEAD AGEING")
  @JvmField val lEADAGEING: Int,

  @SerializedName("2ND LEVEL REPORTING")
  @JvmField val jsonMember2NDLEVELREPORTING: String,

  @SerializedName("LEAD STAGE")
  @JvmField val lEADSTAGE: String,

  @SerializedName("LEAD STATUS")
  @JvmField val lEADSTATUS: String,

  @SerializedName("CURRENT STAGE AGEING")
  @JvmField val cURRENTSTAGEAGEING: Int
)
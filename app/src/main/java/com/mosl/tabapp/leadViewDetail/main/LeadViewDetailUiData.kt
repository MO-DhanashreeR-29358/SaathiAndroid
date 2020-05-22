package com.mosl.tabapp.leadViewDetail.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LeadViewDetailArgument(
  @JvmField val leadId: String = "0",
  @JvmField val ucId: String = "0"
) : Parcelable
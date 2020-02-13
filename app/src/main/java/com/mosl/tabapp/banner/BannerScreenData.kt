package com.mosl.tabapp.banner

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/*

@Parcelize
data class SurveyScreenData(
  @JvmField val data: List<SurveyData>
) : Parcelable
*/

@Parcelize
data class BannerScreenData(
    @JvmField val isConfigured: Boolean,
    @JvmField val imageUrl: String,
    @JvmField var actionUrl: String,
    @JvmField var target: Int


) : Parcelable
package com.mosl.tabapp.urls

import com.mosl.mobile.data.util.CommonBaseUrls

class ApiBaseUrlImpl : CommonBaseUrls {
  override fun getSaathiApiUrl(): String {
    return "https://saathi.motilaloswal.com:84/api/"
  }

  override fun getUppermostApiUrl(): String {
    return "https://login.motilaloswal.com/Partner/PartnerMobileWebServices.asmx/"
  }

  override fun getCRMCollateralUrl(): String {
    return "http://onlinetrade.motilaloswal.com/emailers/TGS/2015/Landing_page/TGS_landing_page.html"
  }

  override fun getSalesStadiumPitchUrl(): String {
    return "https://saathi.motilaloswal.com/Stadium-Pitch-Objection-Handling/English/Stadium-Pitch/Index.html";
  }

  override fun getEkycExpressApiUrl(): String {
    TODO("Not yet implemented")
  }

  companion object {
    const val SAATHI_BASE_URL = "https://saathiuat.motilaloswal.com:1023/api/"
  }
}
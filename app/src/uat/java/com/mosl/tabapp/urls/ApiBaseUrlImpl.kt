package com.mosl.tabapp.urls

import com.mosl.mobile.data.util.CommonBaseUrls

class ApiBaseUrlImpl : CommonBaseUrls {
  override fun getSaathiApiUrl(): String {
    return "https://saathiuat.motilaloswaluat.com:1023/api/"
  }

  override fun getUppermostApiUrl(): String {
    return "http://122.184.137.89/Partner/PartnerMobileWebServices.asmx/"
  }

  override fun getCRMCollateralUrl(): String {
    return "http://onlinetrade.motilaloswal.com/emailers/TGS/2015/Landing_page/TGS_landing_page.html"
  }

  override fun getSalesStadiumPitchUrl(): String {
    return "https://saathi.motilaloswal.com/Stadium-Pitch-Objection-Handling/English/Stadium-Pitch/Index.html"
  }

  override fun getEkycExpressApiUrl(): String {
   return "https://ekycuat.motilaloswal.com/express/api/v1/"
    //return "https://ekycuat.motilaloswal.com/superjetApi/api/v1/"
  }

  override fun getQRCodeApiUrl(): String {
    return "https://saathiuat.motilaloswaluat.com/saathi_plus/api/DrishtiTransfer/GenerateQrCode_API/?Empcode="
  }

  override fun getMyZoneApiUrl(): String {
    return "https://myzonebeta.motilaloswal.com/"
  }

  companion object {
    const val SAATHI_BASE_URL = "https://saathiuat.motilaloswaluat.com:1023/api/"
  }

}
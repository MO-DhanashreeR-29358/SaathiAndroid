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
    return "https://www.motilaloswal.com/open-demat-account/api/v1/"
  }

  override fun getQRCodeApiUrl(): String {
    return "https://saathi.motilaloswal.com/saathi_plus/api/DrishtiTransfer/GenerateQrCode_API/?Empcode="
  }

  override fun getMyZoneApiUrl(): String {
    return "https://myzonehr.motilaloswal.com/"
  }

  override fun getSaathiIndirectUrl(): String {
    return "https://saathi.motilaloswal.com:85/api/"
  }

  companion object {
    const val SAATHI_BASE_URL = "https://saathi.motilaloswal.com:84/api/"
    const val EKYC_DEMAT_ACCOUNT_LINK = "https://www.motilaloswal.com/open-demat-account/?"
    const val EKYC_ONLY_MFD_LINK = "https://ekyc.motilaloswal.com/partner/mfd/?"
    const val EKYC_MFD_LINK =  "https://www.motilaloswal.com/open-demat-account/?"
    const val EKYC_NON_BROKING_PRODUCT_LINK = "https://ekyc.motilaloswal.com/partner/nonbroking/?"
  }
}
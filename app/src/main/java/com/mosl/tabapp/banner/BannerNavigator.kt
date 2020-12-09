package com.mosl.tabapp.banner

interface BannerNavigator {
    fun openBannerDialog(data: BannerScreenData)
    fun openUrlInWebView(url: String)
    fun openUrlInAppCrome(url: String)
    fun openUrlInBrowser(url: String)
    fun openSettingActivity()
}
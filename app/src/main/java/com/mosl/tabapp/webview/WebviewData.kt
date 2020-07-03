package com.mosl.tabapp.webview

sealed class WebViewRequest {
  data class Id(@JvmField val id: String, @JvmField val oaoUserId: String) : WebViewRequest()
    data class Url(@JvmField val url: String) : WebViewRequest()
}
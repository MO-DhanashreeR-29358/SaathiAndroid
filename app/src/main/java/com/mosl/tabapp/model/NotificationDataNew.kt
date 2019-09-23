package com.mosl.tabapp.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class NotificationDataNew() : Parcelable {

  var page: String? = null
  var section: String? = null
  var subSection: String? = null
  var paramMap: MutableMap<String, String>? = null

  constructor(parcel: Parcel) : this() {
    this.page = parcel.readString()
    this.section = parcel.readString()
    this.subSection = parcel.readString()
    //read map
    val size = parcel.readInt()
    this.paramMap = HashMap()
    for (i in 0 until size) {
      val key = parcel.readString()
      val value = parcel.readString()
      (paramMap as HashMap<String, String>)[key!!] = value!!
    }

  }

  constructor (jobj: String) : this() {
    try {
      val jobjTmp = JSONObject(jobj)

      page = jobjTmp.getString("Page") ?: ""
      section = jobjTmp.getString("Section") ?: ""
      subSection = jobjTmp.getString("SubSection") ?: ""

      this.paramMap = HashMap()
      if (!jobjTmp.isNull("Params")) {
        val jsonArray = jobjTmp.getJSONArray("Params")
        for (i in 0 until jsonArray.length()) {
          val jsonTempChild = jsonArray.getJSONObject(i)
          val jsonArrayNameList = jsonTempChild.names()
          for (j in 0 until jsonArrayNameList.length()) {
            val name = isEmpty(jsonArrayNameList.get(j).toString())
            (this.paramMap as HashMap<String, String>)[name] = jsonTempChild.getString(name)
          }
        }
      }

    } catch (je: JSONException) {
      this.page = ""//Page
      this.section = ""//Page
      this.subSection = ""//Page
      this.paramMap = HashMap()
    }

  }

  private fun isEmpty(data: String): String {

    if (data.isEmpty()) {
      return "0"
    }
    return data
  }

  override fun writeToParcel(
    out: Parcel,
    flags: Int
  ) {
    out.writeString(page)
    out.writeString(section)
    out.writeString(subSection)
    out.writeInt(paramMap?.size!!)
    paramMap?.forEach { (key, value) ->
      out.writeString(key)
      out.writeString(value)
    }
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Creator<NotificationDataNew> {
    override fun createFromParcel(parcel: Parcel): NotificationDataNew {
      return NotificationDataNew(parcel)
    }

    override fun newArray(size: Int): Array<NotificationDataNew?> {
      return arrayOfNulls(size)
    }
  }
}
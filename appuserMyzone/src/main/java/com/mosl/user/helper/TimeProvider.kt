package com.mosl.user.helper

import android.os.SystemClock
import com.mosl.user.prefs.LongPreference

interface Clock {
  fun getTimestamp(): Long
}

object RealClock : Clock {
  override fun getTimestamp(): Long {
    return SystemClock.elapsedRealtime()
  }
}

class ServerClock(private val timePref: LongPreference) : Clock {
  override fun getTimestamp(): Long {
    return timePref.get()
  }
}
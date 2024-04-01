package com.mosl.user.login.cache

import android.util.Log
import com.google.gson.Gson
import com.mosl.user.domain.model.User
import com.mosl.user.prefs.SafeStringPreference
import javax.inject.Inject

class UserCacheImpl @Inject constructor(
    val gson: Gson,
    val preference: SafeStringPreference
) : UserCache {

    override fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        Log.d("Json User %s", userJson)
        preference.set(userJson)
    }

    override fun getUser(): User? {
        return when {
            preference.isSet -> gson.fromJson(preference.get(), User::class.java)
            else -> null
        }
    }

    override fun clearUser() {
        preference.delete()
    }
}
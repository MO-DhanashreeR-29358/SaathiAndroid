package com.mosl.user.login

import com.mosl.user.domain.model.User
import com.mosl.user.login.cache.UserCache
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyZoneUserManagerImpl @Inject constructor(
    private val userCache: UserCache
) : UserManager {
    private var user: User?
    private val userChangeSubject: BehaviorSubject<UserEvent>

    init {
        val user = userCache.getUser()
        this.user = user
        userChangeSubject = BehaviorSubject.createDefault(
            if (user != null) UserEvent.LoggedIn(user) else UserEvent.OpenUser
        )
    }

    fun saveUser(user: User) {
        userCache.saveUser(user)
        this.user = user
        userChangeSubject.onNext(UserEvent.LoggedIn(user))
    }

    fun logout(reason: Int) {
        this.user = null
        userCache.clearUser()
        userChangeSubject.onNext(UserEvent.LoggedOut(reason))
    }

    override fun onUserChanged(): Observable<UserEvent> {
        return userChangeSubject
    }

    override fun getUser(): User? {
        return user
    }

    override fun isLoggedIn(): Boolean {
        return user != null
    }
}
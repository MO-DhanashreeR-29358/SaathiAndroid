package com.mosl.user.login

import com.mosl.user.domain.model.User
import io.reactivex.rxjava3.core.Observable

interface UserManager {
  fun onUserChanged(): Observable<UserEvent>
  fun getUser(): User?
  fun isLoggedIn(): Boolean
}
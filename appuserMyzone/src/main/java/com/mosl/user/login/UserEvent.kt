package com.mosl.user.login

import com.mosl.user.domain.model.User

abstract class UserEvent {
  object OpenUser : UserEvent()
  data class LoggedIn(@JvmField val user: User) : UserEvent()
  data class LoggedOut(@JvmField val reason: Int) : UserEvent()
}
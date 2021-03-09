package com.mosl.tabapp.ekyc

interface EkycNavigator {
  fun openNewEkycApp(
    isExistingUser: Boolean,
    userId: String
  )

  fun openEkyc()

}

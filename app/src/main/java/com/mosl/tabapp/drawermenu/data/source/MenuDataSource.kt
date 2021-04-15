package com.mosl.tabapp.drawermenu.data.source

import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.model.splash.ChildMenu
import com.mosl.tabapp.drawermenu.data.BaseMenu
import io.reactivex.rxjava3.core.Observable

/**
 * Created by lalkishore on 11/02/18.
 */

interface MenuDataSource {
  fun getMenuItems(
    userManager: UserManager,
    menuItems: List<ChildMenu>
  ): Observable<List<BaseMenu>>
}

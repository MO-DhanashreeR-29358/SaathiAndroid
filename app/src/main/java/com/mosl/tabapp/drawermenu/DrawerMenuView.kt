package com.mosl.tabapp.drawermenu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.core.utils.obtain
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.AppComponent
import com.mosl.tabapp.R
import com.mosl.tabapp.drawermenu.data.AppMenu
import com.mosl.tabapp.drawermenu.data.BaseMenu
import com.mosl.tabapp.drawermenu.data.ChildMenu
import com.mosl.tabapp.drawermenu.data.source.MenuDataSource
import com.mosl.tabapp.drawermenu.item.BaseMenuExpandableItem
import com.mosl.tabapp.drawermenu.item.BaseMenuItem
import com.mosl.tabapp.drawermenu.item.ChildMenuItem
import com.mosl.tabapp.drawermenu.item.MenuHeaderItem
import com.mosl.tabapp.drawermenu.item.SelectableMenu
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by lalkishore on 11/02/18.
 */

class DrawerMenuView : FrameLayout {

  private val menuSelectSubject = PublishSubject.create<AppMenu>()
  private val closeSelectSubject = PublishSubject.create<View>()
  private val selectableItemList = ArrayList<SelectableMenu>()
  private val expandableItems = ArrayList<BaseMenuExpandableItem>()

  val itemExpansionListener: com.xwray.groupie.OnItemClickListener =
    com.xwray.groupie.OnItemClickListener { item, _ ->
      expandableGroups.mapNotNull { it as? MenuExpandableGroup }
          .filter { it.header != item }
          .forEach {
            it.header.collapse()
          }
    }
  @Inject
  lateinit var source: MenuDataSource
  @Inject
  lateinit var userManager: UserManager
  lateinit var rvDrawerMenu: RecyclerView
  lateinit var disposable: CompositeDisposable
  lateinit var menuSection: Section
  private var selectedMenuItem: Item<*>? = null
  private var expandableGroup: ExpandableGroup? = null
  private val expandableGroups = mutableListOf<Group>()

  constructor(context: Context) : super(context, null) {}

  constructor(
    context: Context,
    attrs: AttributeSet?
  ) : super(context, attrs) {
    context.applicationContext.obtain<AppComponent>(AppComponent::class.java)
        .inject(this)

    LayoutInflater.from(context)
        .inflate(R.layout.view_drawer_menu, this)

    rvDrawerMenu = findViewById(R.id.rv_drawer_menu)

    val groupAdapter = GroupAdapter<ViewHolder>()
    rvDrawerMenu.layoutManager = LinearLayoutManager(context)
    rvDrawerMenu.addItemDecoration(
        DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
    )
    rvDrawerMenu.adapter = groupAdapter
    val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
    dividerItemDecoration.setDrawable(
        ContextCompat.getDrawable(context, R.drawable.grey_item_decorator)!!
    )
    rvDrawerMenu.addItemDecoration(dividerItemDecoration)
    groupAdapter.setOnItemClickListener({ item, _ ->
      if (item is SelectableMenu) {
        menuSelectSubject.onNext((item as SelectableMenu).getAppMenu())
      } else if (item is BaseMenuExpandableItem) {
        expandableGroup!!.onToggleExpanded()
      }
    })

    menuSection = Section()
    groupAdapter.add(menuSection)

    disposable = CompositeDisposable()

    source.getMenuItems(userManager, userManager.getReportMenuList())
        .compose(applyUiModel())
        .subscribe(this::render)
        .addTo(disposable)
  }

  override fun onDetachedFromWindow() {
    disposable.clear()
    super.onDetachedFromWindow()
  }

  fun onMenuClicked(): Observable<AppMenu> {
    return menuSelectSubject
  }

  fun onCloseClicked(): Observable<View> {

    return closeSelectSubject
  }

  fun selectItem(appMenu: AppMenu) {
    for (item in selectableItemList) {
      if (item.getAppMenu() == appMenu) {
        selectItem(item as Item<*>)
      }
    }
  }

  private fun selectItem(clickedItem: Item<*>) {
    if (clickedItem == selectedMenuItem) return
    selectedMenuItem = clickedItem
    for (item in selectableItemList) {
      item.setSelected(clickedItem == item)
    }
  }

  fun refreshHeader() {
    userManager.getUser()
        ?.let {
          val name = it.firstName.plus(it.lastName)
          menuSection.setHeader(
              MenuHeaderItem(name, it.currentBusinessProfile.name, it.imageUrl, closeSelectSubject)
          )
        }
  }

  internal fun render(uiModel: UiModel<List<BaseMenu>>) {
    uiModel.onFailure {
      Timber.d(it)
    }

    uiModel.onSuccess {
      refreshHeader()
      selectableItemList.clear()
      expandableGroups.clear()

      for (baseMenu in it) {
        if (baseMenu.children.isEmpty()) {
          val baseMenuItem = BaseMenuItem(baseMenu)
          selectableItemList.add(baseMenuItem)
          expandableGroups.add(baseMenuItem)
        } else {
          val expandableGroup = baseMenu.getExpandableGroup()
          selectableItemList.addAll(expandableGroup.children)
          expandableGroups.add(expandableGroup)
        }
      }
      menuSection.update(expandableGroups)
      rvDrawerMenu.scrollToPosition(0)

    }
  }

  fun BaseMenu.getExpandableGroup(): MenuExpandableGroup {
    val expandableItem = BaseMenuExpandableItem(this, itemExpansionListener)
    val childMenuItems = children.getChildMenuItems()
    return MenuExpandableGroup(expandableItem, childMenuItems)
  }

  fun List<ChildMenu>.getChildMenuItems(): List<ChildMenuItem> {
    return map { ChildMenuItem(it) }
  }

  fun setMenuSelection(@StringRes groupPos: Int) {
    val selectedPage = 0

    for (selectableMenu in selectableItemList) {
      if (selectedPage != 0) {
        if (selectableMenu.getAppMenu().pageId == selectedPage) {
          selectItem(selectableMenu.getAppMenu())
          expandableItems[0].expand()
        }
      }
    }
  }

  companion object {

    fun getChildMenuItems(childMenus: List<ChildMenu>): List<ChildMenuItem> {
      val menuItems = ArrayList<ChildMenuItem>()
      for (childMenu in childMenus) {
        menuItems.add(ChildMenuItem(childMenu))
      }
      return menuItems
    }
  }
}

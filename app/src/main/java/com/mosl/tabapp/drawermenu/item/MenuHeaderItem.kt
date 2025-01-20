package com.mosl.tabapp.drawermenu.item

import android.view.View
import androidx.constraintlayout.widget.Group
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ViewMenuHeaderBinding
import com.mosl.tabapp.drawermenu.data.AppMenu
import com.mosl.tabapp.drawermenu.data.OtherMenu
import com.mosl.tabapp.navigation.NavigationConstants
import com.squareup.picasso.Picasso
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.rxjava3.subjects.PublishSubject

class MenuHeaderItem(
    val partnerName: String,
    val branchCode: String?,
    val imageUrl: String,
    val userManager: UserManager,
    val closeSelectSubject: PublishSubject<View>,
    val subject: PublishSubject<AppMenu>
) : BindableItem<ViewMenuHeaderBinding>() {

    override fun getLayout(): Int {
        return R.layout.view_menu_header
    }

    override fun bind(
        viewBinding: ViewMenuHeaderBinding,
        position: Int
    ) {
        val context = viewBinding.root.context
        viewBinding.txtPartnerName.text = partnerName
        viewBinding.txtBusinessProfileName.text = branchCode

        viewBinding.group.setAllOnClickListener(View.OnClickListener {
            if(userManager.getUser()?.isIndirectUser() == false) {
                subject.onNext(
                    OtherMenu(NavigationConstants.NAVIGATE_TO_BRANCH_CODE)
                )
            }
        })

        viewBinding.imgClose.setOnClickListener { it ->
            closeSelectSubject.onNext(it);
        }

        if (imageUrl.isNotEmpty()) {
            Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.menu_profile_pic)
                .into(viewBinding.imgPartner)
        }

    }

    fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
        referencedIds.forEach { id ->
            rootView.findViewById<View>(id).setOnClickListener(listener)
        }
    }
}

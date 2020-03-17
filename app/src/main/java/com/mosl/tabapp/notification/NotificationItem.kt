package com.mosl.tabapp.notification

import android.graphics.Color
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.saathi.domain.DATE_TIME_UTC
import com.mosl.saathi.domain.parseOrDefault
import com.mosl.saathi.domain.toNewsDate
import com.mosl.shared.util.Utility.stringOrEmpty
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.NotificationListBinding
import com.xwray.groupie.databinding.BindableItem
import java.util.*

class NotificationItem(
    internal val model: NotificationResponseData
) : BindableItem<NotificationListBinding>() {

    override fun getLayout(): Int {
        return R.layout.notification_list
    }

    override fun bind(
        viewBinding: NotificationListBinding,
        position: Int
    ) {
        viewBinding.apply {

            if (position % 2 == 1) {
                linearLayoutOfListItem.setBackgroundColor(Color.parseColor("#FFFFFF"))
            } else {
                linearLayoutOfListItem.setBackgroundColor(Color.parseColor("#F5F5F5"))
            }

            val creationDate: Date = DATE_TIME_UTC.parseOrDefault(stringOrEmpty(model.creationDate))
            val (original, day, month, time) = creationDate.toNewsDate()

            txtMessage.text = model.message
            timestamp.text = time
            dateUpper.text = day
            dateLower.text = month
        }
    }

    fun getAdditionalData(): String {
        return model.additionalData
    }

    override fun hashCode(): Int {
        return model.hashCode()
    }
}
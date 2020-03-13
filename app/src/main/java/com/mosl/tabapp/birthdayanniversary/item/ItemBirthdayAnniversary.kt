package com.mosl.tabapp.birthdayanniversary.item

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryListData
import com.mosl.tabapp.R
import com.mosl.tabapp.R.string
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew.Companion.FILTER_TYPE_ONE
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew.Companion.FILTER_TYPE_THREE
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew.Companion.KEY_ANNIVERSARY
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragmentNew.Companion.KEY_BIRTHDAY
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent.SendEmailAll
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent.SendGift
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.constants.MOSLConstants.NO
import com.mosl.tabapp.constants.MOSLConstants.YES
import com.mosl.tabapp.databinding.DialogBirthdayAnniversaryNewBinding
import com.mosl.tabapp.databinding.ItemBirthdayAnniversaryBinding
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.subjects.PublishSubject

class ItemBirthdayAnniversary(
  private val data: BirthdayAnniversaryListData,
  private val filterType: String,
  private val context: Context,
  private val subject: PublishSubject<BirthdayAnniversaryUiEvent>,
  private val eventType: String
) : BindableItem<ItemBirthdayAnniversaryBinding>() {
  var isGiftFlag = false
  override fun getLayout(): Int {
    return R.layout.item_birthday_anniversary
  }

  override fun bind(viewBinding: ItemBirthdayAnniversaryBinding, position: Int) {
    viewBinding.apply {
      when (eventType) {
        KEY_BIRTHDAY -> {
          val birthdayCount =
            data.noOfYearBirthday + CRMAppUtil.getDayOfMonthSuffix(data.noOfYearBirthday.toInt()) + " " + eventType
          tvDate.text = splitDate(data.dob)
          tvEvent.text = birthdayCount
          isGiftFlag = data.birthdayFlag
          ivBirthdayAnniversary.setBackgroundResource(R.drawable.ic_bday)
        }
        KEY_ANNIVERSARY -> {
          val anniversaryCount =
            data.anniversaryCount + CRMAppUtil.getDayOfMonthSuffix(data.anniversaryCount.toInt()) + " " + eventType
          tvDate.text = splitDate(data.anniversaryDate)
          tvEvent.text = anniversaryCount

          isGiftFlag = data.anniversaryFlag
          ivBirthdayAnniversary.setBackgroundResource(R.drawable.ic_anniversary)
        }
      }

      tvClientName.text = data.clientName
      if (data.clientCode.equals("0", ignoreCase = true) || data.clientCode.isBlank()) {
        tvClientCode.text = data.ucid
      } else {
        tvClientCode.text = data.clientCode
      }

      //dont open it for belated
      if (!filterType.equals(FILTER_TYPE_ONE, ignoreCase = false)) {
        ivPopup.setOnClickListener {
          showPopupDialog()
        }

      }

    }

  }

  private fun splitDate(date: String): String {
    var newDate = ""
    if (date.isNotBlank()) {
      val birthDate: Array<String> =
        date.split("T").toTypedArray()
      val dateFormat = CRMAppUtil.getDate(birthDate[0], "yyyy-MM-dd")
      newDate = CRMAppUtil.getDateStr(dateFormat, "dd MMM yyyy")
    }
    return newDate
  }

  private fun Dialog.showCallDialog() {
    if (!filterType.equals(FILTER_TYPE_THREE, ignoreCase = true)) {
      MSFDialog.alertDialog(
        context, 0,
        context.resources.getString(string.motilal_oswal),
        context.resources.getString(string.call_alert_message), YES, NO, true
      ) { action, _ ->
        if (action == OK) {
          if (data.backOfficeMobileNumber.equals("", ignoreCase = true)
          ) {
            Toast.makeText(
              context,
              context.resources.getString(string.txt_contact_number_not_available),
              Toast.LENGTH_LONG
            ).show()
          } else {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse(
              "tel:"
                  + data.backOfficeMobileNumber
            )
            callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(callIntent)
          }
        }
      }
      dismiss()
    }
  }

  private fun Dialog.showSMSDialog() {
    if (!filterType.equals(FILTER_TYPE_THREE, ignoreCase = true)) {
      if (data.backOfficeMobileNumber.isNotBlank()) {
        val smsIntent = Intent("android.intent.action.VIEW")
        smsIntent.data = Uri.parse(
          "smsto:" + Uri.encode(
            data.backOfficeMobileNumber
          )
        )
        smsIntent.putExtra(
          "sms_body",
          if (eventType
              .equals(KEY_BIRTHDAY, ignoreCase = true)
          ) MOSLConstants.BIRTHDAY_MESSAGE else MOSLConstants.ANNIVERSARY_MESSAGE
        )
        smsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(smsIntent)
      } else {
        Toast.makeText(
          context,
          context.resources.getString(string.txt_contact_number_not_available),
          Toast.LENGTH_LONG
        )
          .show()
      }
      dismiss()
    }
  }

  private fun Dialog.showEmailDialog() {
    if (!filterType.equals(FILTER_TYPE_THREE, ignoreCase = true)) {
      MSFDialog.alertDialog(
        context, 0,
        context.resources.getString(string.motilal_oswal),
        MOSLConstants.EMAIL_MESSAGE_CLIENT, YES, NO, true
      ) { action: Action, _: Array<Any?>? ->
        if (action == OK) {
          subject.onNext(
            SendEmailAll(
              data.ucid,
              false
            )
          )
        }
      }
      dismiss()
    }
  }

  private fun showPopupDialog() {
    Dialog(context).apply {
      requestWindowFeature(Window.FEATURE_NO_TITLE)
      window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      val dialogBinding =
        DialogBirthdayAnniversaryNewBinding.bind(
          layoutInflater.inflate(
            R.layout.dialog_birthday_anniversary_new,
            null
          )
        )
      setContentView(dialogBinding.root)
      window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
      )
      setCanceledOnTouchOutside(true)

      dialogBinding.also {
        it.ivCall.setOnClickListener {
          this.showCallDialog()
        }

        it.ivSms.setOnClickListener {
          this.showSMSDialog()
        }
        it.ivEmail.setOnClickListener {
          this.showEmailDialog()
        }

        it.ivSendGift.setOnClickListener {
          if (!isGiftFlag) {
            subject.onNext(SendGift(data.ucid, eventType))
          }
          dismiss()
        }
        it.ivClose.setOnClickListener {
          dismiss()
        }
      }

    }.show()
  }
}
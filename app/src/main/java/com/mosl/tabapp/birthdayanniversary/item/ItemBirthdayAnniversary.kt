package com.mosl.tabapp.birthdayanniversary.item

import android.annotation.SuppressLint
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
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.databinding.DialogBirthdayAnniversaryNewBinding
import com.mosl.tabapp.databinding.ItemRecyclerBirthdayAnniversaryNewBinding
import com.mosl.tabapp.model.BirthdayAnniversaryModel
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.Action
import com.mosl.tabapp.ui.MSFDialog.Action.OK
import com.xwray.groupie.databinding.BindableItem
import timber.log.Timber

class ItemBirthdayAnniversary(
  private val data: BirthdayAnniversaryListData,
  private val filterType: String,
  private val context: Context
) : BindableItem<ItemRecyclerBirthdayAnniversaryNewBinding>() {
  override fun getLayout(): Int {
    return R.layout.item_recycler_birthday_anniversary_new
  }

  @SuppressLint("SetTextI18n")
  override fun bind(viewBinding: ItemRecyclerBirthdayAnniversaryNewBinding, position: Int) {
    viewBinding.apply {
     /* try {
        val birthDate: Array<String> =
          data.birthday.split("T").toTypedArray()
        val dateFormat = CRMAppUtil.getDate(birthDate[0], "yyyy-MM-dd")
        val date = CRMAppUtil.getDateStr(dateFormat, "dd MMM yyyy")
        tvDate.text = date
      } catch (e: Exception) {
        Timber.d(e)
      }*/
      tvClientName.text = data.clientName
      if (data.clientCode.equals("0", ignoreCase = true) || data.clientCode.isBlank()) {
        tvClientCode.text = data.ucid
      } else {
        tvClientCode.text = data.clientCode
      }
      /*tvEvent.text =
        data.totalYears + CRMAppUtil.getDayOfMonthSuffix(data.totalYears.toInt()) + data.title*/

      /*if (data.title.equals("Birthday", ignoreCase = true)) {
        ivBirthdayAnniversary.setBackgroundResource(R.drawable.ic_bday)
      } else {
        ivBirthdayAnniversary.setBackgroundResource(R.drawable.ic_anniversary)
      }*/
      //dont open it for belated
      if (!filterType.equals("1", ignoreCase = false)) {
        ivPopup.setOnClickListener { view ->
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
            dialogBinding.ivCall.setOnClickListener {
              if (!filterType.equals("3", ignoreCase = true)) {
                MSFDialog.alertDialog(
                  context, 0,
                  context.resources.getString(string.motilal_oswal),
                  "Do you want to call?", "Yes", "No", true
                ) { action, _ ->
                  if (action == OK) {
                    if (data.backOfficeMobileNumber.equals("", ignoreCase = true)
                    ) {
                      Toast.makeText(
                        context,
                        "Contact number not available",
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

            /*dialogBinding.ivSms.setOnClickListener {
              if (!filterType.equals("3", ignoreCase = true)) {
                if (data.backOfficeMobileNumber != "android.intent.action.VIEW") {
                  val smsIntent = Intent("")
                  smsIntent.data = Uri.parse(
                    "smsto:" + Uri.encode(
                      data.backOfficeMobileNumber
                    )
                  )
                  smsIntent.putExtra(
                    "sms_body",
                    if (data.title
                        .equals("birthday", ignoreCase = true)
                    ) MOSLConstants.BIRTHDAY_MESSAGE else MOSLConstants.ANNIVERSARY_MESSAGE
                  )
                  smsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                  context.startActivity(smsIntent)
                } else {
                  Toast.makeText(
                    context,
                    "Contact number not available", Toast.LENGTH_LONG
                  )
                    .show()
                }
                dismiss()
              }
            }*/

            dialogBinding.ivEmail.setOnClickListener {
              if (!filterType.equals("3", ignoreCase = true)) {
                MSFDialog.alertDialog(
                  context, 0,
                  context.resources.getString(string.motilal_oswal),
                  MOSLConstants.EMAIL_MESSAGE_CLIENT, "Yes", "No", true
                ) { action: Action, _: Array<Any?>? ->
                  if (action == OK) {
                    /*sendEmail(
                      data.ucid,
                      data.eventType
                    )*/
                  }
                }
                dismiss()
              }
            }

            /*dialogBinding.ivSendGift.setOnClickListener {
              if (!data.isGiftFlag) {
                *//*sendGift(
                  data.ucid,
                  data.eventType
                )*//*
              }
              dismiss()
            }*/

          }.show()

        }

      }

    }

  }
}
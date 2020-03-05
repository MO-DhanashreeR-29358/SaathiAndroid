package com.mosl.tabapp.leadViewDetail.document.item

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.mosl.mobile.domain.leadviewdetail.document.LeadDocumentsItemData
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ItemDocumentListBinding
import com.mosl.tabapp.databinding.ListEditPopupBinding
import com.mosl.tabapp.leadViewDetail.document.DocumentUiEvent
import com.mosl.tabapp.leadViewDetail.document.DocumentUiEvent.DeleteLeadDocuments
import com.mosl.tabapp.leadViewDetail.document.DocumentUiEvent.RenameLeadDocuments
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.alertDialog
import com.mosl.tabapp.util.downloadImage
import com.xwray.groupie.databinding.BindableItem
import io.fabric.sdk.android.services.common.CommonUtils.hideKeyboard
import io.reactivex.subjects.PublishSubject

class DocumentItem(
  private val data: LeadDocumentsItemData,
  private val subject: PublishSubject<DocumentUiEvent>
) : BindableItem<ItemDocumentListBinding>(), OnClickListener {

  var context: Context? = null

  override fun getLayout(): Int {
    return R.layout.item_document_list
  }

  override fun bind(
    viewBinding: ItemDocumentListBinding,
    position: Int
  ) {
    viewBinding.apply {
      context = root.context
      tvDocumentName.text = data.documentName
      tvDate.text = data.creationDate
      ivScan.setOnClickListener(this@DocumentItem)
      ivDeleteDoc.setOnClickListener(this@DocumentItem)
      ivUploadDoc.setOnClickListener(this@DocumentItem)
    }

  }

  override fun onClick(view: View) {

    when (view.id) {
      R.id.ivScan -> {
        Dialog(context!!).apply {
          val dialogBinding =
            ListEditPopupBinding.bind(layoutInflater.inflate(R.layout.list_edit_popup, null))
          setTitle("Rename")
          requestWindowFeature(Window.FEATURE_NO_TITLE)
          window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          setContentView(dialogBinding.root)
          window?.setLayout(
              ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
          )
          setCanceledOnTouchOutside(true)

          dialogBinding.btnCancel.setOnClickListener {
            dismiss()
          }

          dialogBinding.etDocumentName.setText(data.documentName)
          dialogBinding.btnOK.setOnClickListener {
            if (dialogBinding.etDocumentName.text.isNotEmpty()) {
              hideKeyboard(context, it)
              subject.onNext(
                  RenameLeadDocuments(data.id!!, dialogBinding.etDocumentName.text.toString())
              )
              dismiss()
            } else {
              alertDialog(
                  context, 0,
                  context.resources?.getString(R.string.motilal_oswal),
                  "File name cannot be blank.", "OK", true, null
              )
              dismiss()
            }

          }

          dialogBinding.imgClose.setOnClickListener {
            dismiss()
          }
        }
            .show()
      }
      R.id.ivDeleteDoc -> {
        alertDialog(
            context, 0,
            context?.resources?.getString(R.string.motilal_oswal),
            "Are you sure you want to delete:" + data.documentName + " ?",
            "Yes", "No", true
        ) { action, _ ->
          if (action == MSFDialog.Action.OK) {
            subject.onNext(DeleteLeadDocuments(data.id!!))
          }
        }
      }
      R.id.ivUploadDoc -> {
        context?.downloadImage(data.documentFullPath!!)
        Toast.makeText(context, "Downloading ${data.documentName}...", Toast.LENGTH_SHORT)
            .show()
      }
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as DocumentItem

    if (data != other.data) return false

    return true
  }

  override fun hashCode(): Int {
    return data.hashCode()
  }

}
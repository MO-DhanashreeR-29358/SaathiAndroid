package com.mosl.tabapp.leadViewDetail.document.item

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.mosl.mobile.domain.leadviewdetail.document.LeadDocumentsItemData
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ItemDocumentListBinding
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

  override fun bind(viewBinding: ItemDocumentListBinding, position: Int) {
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
    val dialog = Dialog(context!!)
    when (view.id) {
      R.id.ivScan -> {
        dialog.setTitle("Rename")
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!
          .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.list_edit_popup)
        dialog.window!!
          .setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
          )
        dialog.setCanceledOnTouchOutside(true)
        val btnOK = dialog.findViewById<Button>(R.id.btnOK)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)

        btnCancel.setOnClickListener { _ ->
          dialog.dismiss()
        }

        val etDocumentName = dialog.findViewById<View>(R.id.etDocumentName) as EditText
        etDocumentName.setText(data.documentName)
        btnOK.setOnClickListener {
          if (etDocumentName.text.isNotEmpty()) {
            hideKeyboard(context, it)
            subject.onNext(RenameLeadDocuments(data.id!!, etDocumentName.text.toString()))
            dialog.dismiss()
          } else {
            alertDialog(
              context, 0,
              context?.resources?.getString(R.string.motilal_oswal),
              "File name cannot be blank.", "OK", true, null
            )
            dialog.dismiss()
          }

        }

        val imgClose = dialog.findViewById<ImageView>(R.id.imgClose)
        imgClose.setOnClickListener {
          dialog.dismiss()
        }

        dialog.show()
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
        //downloadImage(data.documentFullPath!!)
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
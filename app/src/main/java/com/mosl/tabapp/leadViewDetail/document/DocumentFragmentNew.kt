package com.mosl.tabapp.leadViewDetail.document

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Base64OutputStream
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.jakewharton.rxbinding3.view.clicks
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.leadviewdetail.document.DocumentInput
import com.mosl.mobile.domain.leadviewdetail.document.FilterRequestInput
import com.mosl.mobile.domain.leadviewdetail.document.LeadDocumentsInput
import com.mosl.mobile.domain.leadviewdetail.document.LeadDocumentsItemData
import com.mosl.mobile.domain.leadviewdetail.document.UploadDocumentData
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants.DOCUMENT_DELETED
import com.mosl.tabapp.constants.MOSLConstants.DOCUMENT_RENAME
import com.mosl.tabapp.constants.MOSLConstants.DOCUMENT_UPLOADED
import com.mosl.tabapp.constants.MOSLConstants.FILE_LARGE_MESSAGE
import com.mosl.tabapp.constants.MOSLConstants.FILE_NOT_ALLOWED_MESSAGE
import com.mosl.tabapp.constants.MOSLConstants.FILE_UPLOAD_COUNT_LIMT
import com.mosl.tabapp.constants.MOSLConstants.LEAD_ID
import com.mosl.tabapp.constants.MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY
import com.mosl.tabapp.constants.MOSLConstants.UCID
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentDocumentNewBinding
import com.mosl.tabapp.leadViewDetail.document.DocumentUiEvent.DeleteLeadDocuments
import com.mosl.tabapp.leadViewDetail.document.DocumentUiEvent.RenameLeadDocuments
import com.mosl.tabapp.leadViewDetail.document.DocumentUiEvent.UploadImage
import com.mosl.tabapp.leadViewDetail.document.item.DocumentItem
import com.mosl.tabapp.permission.ImageUploadHelper
import com.mosl.tabapp.permission.ImageUploadHelper.ImageUploadCallback
import com.mosl.tabapp.ui.MSFDialog.alertDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.ArrayList
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class DocumentFragmentNew : CommonFragment(), DocumentView, ImageUploadCallback {
  lateinit var binding: FragmentDocumentNewBinding
  @Inject lateinit var imageUploadHelper: ImageUploadHelper
  private lateinit var section: DataManagerSection
  @Inject
  internal lateinit var presenter: DocumentPresenter
  private val subject = PublishSubject.create<DocumentUiEvent>()
  private val MY_PERMISSIONS_REQUEST_STORAGE_ACCESS = 31
  private val ALLOWED_FILE_TYPE = arrayOf(
    "TXT",
    "DOC",
    "DOCX",
    "XLS",
    "PDF",
    "CSV",
    "XLSX",
    "PNG",
    "JPEG",
    "JPG",
    "GIF",
    "PPT",
    "PPTX"
  )

  private lateinit var documentList: ArrayList<LeadDocumentsItemData>
  private val permissionsArr = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
  )

  lateinit var leadId: String
  lateinit var ucid: String
  lateinit var dialog: Dialog
  override fun getLayout(): Int {
    return R.layout.fragment_document_new
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentDocumentNewBinding.bind(view)
    imageUploadHelper.setImageCallback(this)
    setUpView()
    presenter.start()
    leadId = arguments?.getString(LEAD_ID) ?: ""
    ucid = arguments?.getString(UCID) ?: ""
    dataActionSubject.onNext(Fetch)

  }

  private fun hasAllPermission(context: Context, vararg mData: String): Boolean {
    var hasAllPermissions = true
    for (a in mData) {
      if (ContextCompat.checkSelfPermission(context, a) != PackageManager.PERMISSION_GRANTED) {
        hasAllPermissions = false
        break
      }
    }
    return hasAllPermissions
  }

  private fun setUpView() {
    setAppTitle("Documents")
    documentList = ArrayList()
    section = DataManagerSection(onRetryClick)
    val groupAdapter = GroupAdapter<ViewHolder>()
    binding.rvDocumentList.apply {
      layoutManager = LinearLayoutManager(requireContext())
      setHasFixedSize(false)
      adapter = groupAdapter
      (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
      isNestedScrollingEnabled = false
    }
    groupAdapter.add(section)

  }

  private fun showHideAddHeader() {
    if (documentList.size > 0) {
      binding.headerLayout.visibility = View.GONE
    } else {
      binding.headerLayout.visibility = View.VISIBLE
    }
  }

  override fun getLeadDocumentInput(): DocumentInput {
    return DocumentInput(
      requestLeadDocuments = LeadDocumentsInput(leadId, ucid),
      filterRequest = FilterRequestInput(null, null, null, null, null, null, null),
      adminMode = true
    )
  }

  override fun onAddDocumentClick(): Observable<Unit> {
    return binding.rlAddDoc.clicks()
  }

  override fun uploadImage(): Observable<UploadImage> {
    return subject.ofType()
  }

  override fun renderUploadDocument(uiModel: UiModel<UploadDocumentData>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      alertDialog(
        requireContext(), 0,
        resources.getString(R.string.motilal_oswal),
        DOCUMENT_UPLOADED, "OK", true, null
      )
      val id = stringOrEmpty(it.iD)
      val documentName = stringOrEmpty(it.documentName)
      val documentType = stringOrEmpty(it.documentType)
      val documentPath = stringOrEmpty(it.documentPath)
      val documentFullPath = stringOrEmpty(it.documentFullPath)
      var creationDate = stringOrEmpty(it.creationDate)
      creationDate = CRMAppUtil.getDateFromString(
        creationDate,
        "yyyy-MM-dd'T'kk:mm:ss", "MMM dd yyyy  hh:mm a"
      )
      creationDate = creationDate.replace("a.m.", "AM")
        .replace("p.m.", "PM")

      documentList.add(
        LeadDocumentsItemData(
          id, documentName, documentType,
          documentPath, creationDate, documentFullPath
        )
      )
      showHideAddHeader()
    }
  }

  override fun renderShowAddDocumentDialog() {
    if (documentList.size <= FILE_UPLOAD_COUNT_LIMT) {
      if (hasAllPermission(requireContext(), *permissionsArr)) {
        imageUploadHelper.showDialog()
      } else {
        ActivityCompat.requestPermissions(
          requireActivity(), permissionsArr,
          MY_PERMISSIONS_REQUEST_STORAGE_ACCESS
        )
      }
    } else {
      alertDialog(
        requireContext(), 0,
        resources.getString(R.string.motilal_oswal),
        "Maximum 5 documents can be uploaded. Delete some to upload new document.",
        "OK", true, null
      )
    }
  }

  override fun renderLeadDocuments(uiModel: UiModel<List<LeadDocumentsItemData>>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess { dataList ->
      val documentItems = dataList.map {
        documentList.add(it)
        DocumentItem(it, subject)
      }
      section.setContent(documentItems)
      showHideAddHeader()

    }

  }

  override fun renameLeadDocuments(): Observable<RenameLeadDocuments> {
    return subject.ofType()
  }

  override fun deleteLeadDocuments(): Observable<DeleteLeadDocuments> {
    return subject.ofType()
  }

  override fun renderDeleteDocument(uiModel: UiModel<String>, id: String) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {
      deleteDocument(id)
      alertDialog(
        requireContext(), 0,
        resources.getString(R.string.motilal_oswal),
        DOCUMENT_DELETED, "OK", true
      ) { _, _ -> }
    }
  }

  override fun renderUpdateDocuments(uiModel: UiModel<String>, id: String, documentName: String) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess {
      renameDocumentName(id, documentName)
      alertDialog(
        requireContext(), 0,
        resources.getString(R.string.motilal_oswal),
        DOCUMENT_RENAME, "OK", true, null
      )
    }
  }

  private fun renameDocumentName(id: String, newName: String) {
    for (model in documentList) {
      if (model.id == id) {
        model.documentName = newName
        break
      }
    }
    section.notifyChanged()
  }

  private fun deleteDocument(id: String) {
    val item = getDocumentById(id)
    documentList.remove(item)
    val documentItems = documentList.map {
      DocumentItem(it, subject)
    }

    section.setContent(documentItems)
    showHideAddHeader()
  }

  private fun getDocumentById(id: String): LeadDocumentsItemData? {
    for (itemData in documentList) {
      if (itemData.id == id) {
        return itemData
      }
    }
    return null
  }

  private fun uploadFile(fileUri: Uri, fileToUpload: File) {
    var filePath = ""
    var fileInputStream: FileInputStream? = null
    try {
      val pfd = requireContext().contentResolver.openFileDescriptor(fileUri, "r")
      val fd = pfd?.fileDescriptor
      fileInputStream = FileInputStream(fd)
      filePath = fileToUpload.absolutePath
    } catch (e: Exception) {
      e.printStackTrace()
      hideProgress()
      alertDialog(
        requireContext(), 0, resources.getString(R.string.motilal_oswal),
        "Can not access File", "OK", true, null
      )
      return
    }
    var fileName = filePath.substring(filePath.lastIndexOf("/") + 1)
    if (!fileName.contains(".jpg")) {
      fileName = CRMAppUtil.getFilenameWithoutExtension(fileName) + "." + "jpg"
    }


      try {
        val tokens =
          fileName.split("\\.(?=[^\\.]+$)".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (!isFileAllowed(tokens[1], ALLOWED_FILE_TYPE)) {
          alertDialog(
            requireContext(), 0,
            resources.getString(R.string.motilal_oswal),
            FILE_NOT_ALLOWED_MESSAGE, "OK", true, null
          )
          hideProgress()
          return
        }
      } catch (e: Exception) {
        alertDialog(
          requireContext(), 0,
          resources.getString(R.string.motilal_oswal),
          SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null
        )
        hideProgress()
        return
      }
      val strData = getBase64String(fileInputStream).toString()
      if (CRMAppUtil.isSizeAbove(strData.length.toLong())) {
        alertDialog(
          requireContext(), 0, resources.getString(R.string.motilal_oswal),
          FILE_LARGE_MESSAGE, "OK", true, null
        )
        hideProgress()
        return
      }
      subject.onNext(UploadImage(leadId, ucid, fileName.replace(":", "a"), strData))
    //}
  }

  private fun isFileAllowed(extension: String, extList: Array<String>): Boolean {
    var data = false
    for (ext in extList) {
      if (extension.equals(ext, ignoreCase = true)) {
        data = true
        break
      }
    }
    return data
  }

  private fun getBase64String(fileInputStream: FileInputStream): CharSequence {
    val output = ByteArrayOutputStream()
    val output64 = Base64OutputStream(output, Base64.DEFAULT)
    try {
      val buffer = ByteArray(8192)
      val bytesRead: Int = fileInputStream.read(buffer)
      while ((bytesRead) != -1) {
        output64.write(buffer, 0, bytesRead)
        output64.close()
        return output.toString()
      }
    } catch (ioe: IOException) {

    } catch (e: Exception) {

    } finally {
      try {
        fileInputStream.close()
        output64.close()
        output.close()
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
    return ""
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    Timber.d("image data--->${data?.data}")
    imageUploadHelper.onActivityResult(requestCode, resultCode, data)
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
      MY_PERMISSIONS_REQUEST_STORAGE_ACCESS -> {
        if (grantResults.isNotEmpty() && hasAllPermission(requireContext(), *permissionsArr)) {
          imageUploadHelper.showDialog()
        } else {
          Timber.d("Permission not granted")
        }
        return
      }
    }

  }

  override fun onImageCaptured(uri: Uri, file: File) {
    uploadFile(uri, file)

  }
}

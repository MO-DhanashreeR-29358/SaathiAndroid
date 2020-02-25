package com.mosl.tabapp.leadViewDetail.document

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mosl.app.core.events.DataAction.Fetch
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.groupiex.section.DataManagerSection
import com.mosl.mobile.domain.leadviewdetail.document.DocumentInput
import com.mosl.mobile.domain.leadviewdetail.document.FilterRequestInput
import com.mosl.mobile.domain.leadviewdetail.document.LeadDocumentsInput
import com.mosl.mobile.domain.leadviewdetail.document.LeadDocumentsItemData
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants.LEAD_ID
import com.mosl.tabapp.constants.MOSLConstants.UCID
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragmentDocumentNewBinding
import com.mosl.tabapp.leadViewDetail.document.DocumentUiEvent.DeleteLeadDocuments
import com.mosl.tabapp.leadViewDetail.document.DocumentUiEvent.RenameLeadDocuments
import com.mosl.tabapp.leadViewDetail.document.item.ItemDocumentFragment
import com.mosl.tabapp.model.DocumentModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.io.IOException
import java.util.ArrayList
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class DocumentFragmentNew : CommonFragment(), DocumentView {
  lateinit var binding: FragmentDocumentNewBinding
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
  private lateinit var documentList: ArrayList<DocumentModel>
  private val permissionsArr = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
  )
  private val SELECT_FILE = 1
  private val REQUEST_CAMERA = 0
  lateinit var leadId: String
  lateinit var ucid: String
  lateinit var dialog: Dialog
  private var imageToUploadUri: Uri? = null
  private var mCurrentPhotoPath = ""
  override fun getLayout(): Int {
    return R.layout.fragment_document_new
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragmentDocumentNewBinding.bind(view)
    setAppTitle("Documents")
    setUpView()
    try {
      leadId = arguments!!.getString(LEAD_ID)
      ucid = arguments!!.getString(UCID)
    } catch (e: Exception) {
      e.printStackTrace()
    }
    presenter.start()
    dataActionSubject.onNext(Fetch)

  }

  private fun hasAllPermission(context: Context, vararg mData: String): Boolean {
    var hasAllPermisions = true
    for (a in mData) {
      if (ContextCompat.checkSelfPermission(context, a) != PackageManager.PERMISSION_GRANTED) {
        hasAllPermisions = false
        break
      }
    }
    return hasAllPermisions
  }

  private fun galleryIntent() {
    val intent = Intent()
    intent.type = "*/*"
    intent.action = Intent.ACTION_GET_CONTENT//
    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)
  }

  private fun setUpView() {
    section = DataManagerSection(onRetryClick)
    val groupAdapter = GroupAdapter<ViewHolder>()
    binding.rvDocumentList.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = groupAdapter
      setHasFixedSize(true)
    }

  }

  private fun dialogAddDocument() {
    dialog = Dialog(requireContext())
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window!!
      .setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(R.layout.dialog_add_document)
    dialog.window!!
      .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    dialog.setCanceledOnTouchOutside(true)

    val tvTakePhoto = dialog.findViewById<TextView>(R.id.tvTakePhoto)
    val tvCancel = dialog.findViewById<TextView>(R.id.tvCancel)
    val tvAddFromDevice = dialog.findViewById<TextView>(R.id.tvAddFromDevice)

    tvCancel.text = getString(R.string.text_label_cancel)
    tvTakePhoto.text = getString(R.string.text_label_take_photo)
    tvAddFromDevice.text = getString(R.string.text_label_add_from_device)
    tvTakePhoto.setOnClickListener { view ->
      dialog.dismiss()
      cameraIntent()
    }
    tvAddFromDevice.setOnClickListener { view ->
      dialog.dismiss()
      galleryIntent()
    }
    tvCancel.setOnClickListener { view -> dialog.dismiss() }
    dialog.show()
  }

  private fun cameraIntent() {
    try {
      imageToUploadUri = null
      try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
          imageToUploadUri = FileProvider.getUriForFile(
            requireActivity(),
            requireActivity().applicationContext.packageName + ".provider",
            createImageFile()
          )
        } else {
          imageToUploadUri = Uri.fromFile(createImageFile()) // convert path to Uri
        }
      } catch (e: IOException) {
        e.printStackTrace()
      }

      val it = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
      it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageToUploadUri)
      startActivityForResult(it, REQUEST_CAMERA)
    } catch (e: Exception) {
    }

  }

  @Throws(IOException::class)
  private fun createImageFile(): File {
    // Create an image file name
    val imageFileName = "POST_IMAGE"
    val storageDir = File(
      Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
      "Camera"
    )
    val image = File.createTempFile(
      imageFileName, /* prefix */
      ".jpg", /* suffix */
      storageDir      /* directory */
    )
    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = "file:" + image.absolutePath
    return image
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

  override fun renderLeadDocuments(uiModel: UiModel<List<LeadDocumentsItemData>>) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }

    uiModel.onSuccess {

      for (i in it.indices) {
        val item = it[i]
        val id = stringOrEmpty(item.id)
        val documentName = stringOrEmpty(item.documentName)
        val documentType = stringOrEmpty(item.documentType)
        val documentPath = stringOrEmpty(item.documentPath)
        var creationDate = stringOrEmpty(item.creationDate)
        val documentFullPath = stringOrEmpty(item.documentFullPath)
        creationDate = CRMAppUtil.getDateFromString(
          creationDate,
          "yyyy-MM-dd'T'kk:mm:ss",
          "MMM dd yyyy  hh:mm a"
        )
        creationDate = creationDate.replace("a.m.", "AM")
          .replace("p.m.", "PM")
        documentList.add(
          DocumentModel(
            id, documentName, documentType,
            documentPath, creationDate, documentFullPath
          )
        )

        section.setContent(ItemDocumentFragment(item, requireContext(), subject))

      }
      showHideAddHeader()
      // documentAdapter.notifyDataSetChanged()

    }
  }

  override fun renameLeadDocuments(): Observable<RenameLeadDocuments> {
    return subject.ofType()
  }

  override fun deleteLeadDocuments(): Observable<DeleteLeadDocuments> {
    return subject.ofType()
  }

  override fun renderUpdateDocuments(uiModel: UiModel<String>, id: String, documentName: String) {
    toggleProgress(uiModel.inProgress)
    uiModel.onFailure {
      showOnFailurePopup(it)
    }
    uiModel.onSuccess {

    }
  }

}

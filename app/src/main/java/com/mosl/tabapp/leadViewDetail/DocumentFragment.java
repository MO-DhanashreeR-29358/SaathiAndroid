package com.mosl.tabapp.leadViewDetail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.documents.DeleteDocumentRequest;
import com.mosl.saathi.api.model.documents.DocumentsRequest;
import com.mosl.saathi.api.model.documents.FilterRequest;
import com.mosl.saathi.api.model.documents.LeadDocumentsItem;
import com.mosl.saathi.api.model.documents.LeadDocumentsRequest;
import com.mosl.saathi.api.model.documents.UpdateDocumentsRequest;
import com.mosl.saathi.api.model.documents.UploadDocumentItem;
import com.mosl.saathi.api.model.documents.UploadDocumentsRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.DocumentModel;
import com.mosl.tabapp.ui.MSFDialog;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.constants.MOSLConstants.DOCUMENT_DELETED;
import static com.mosl.tabapp.constants.MOSLConstants.DOCUMENT_RENAME;
import static com.mosl.tabapp.constants.MOSLConstants.DOCUMENT_UPLOADED;
import static com.mosl.tabapp.constants.MOSLConstants.FILE_DOWNLOADED;
import static com.mosl.tabapp.constants.MOSLConstants.FILE_LARGE_MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.FILE_NOT_ALLOWED_MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.FILE_UPLOAD_COUNT_LIMT;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD_ID;
import static com.mosl.tabapp.constants.MOSLConstants.MISSING_FILE_VIEWER;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_SERVER_ERROR_OCCURRED;
import static com.mosl.tabapp.constants.MOSLConstants.UCID;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class DocumentFragment extends CommonFragment implements View.OnClickListener {

	private static final int MY_PERMISSIONS_REQUEST_STORAGE_ACCESS = 31;
	private static final String[] ALLOWED_FILE_TYPE = {
		"TXT", "DOC", "DOCX", "XLS", "PDF", "CSV", "XLSX", "PNG", "JPEG", "JPG", "GIF", "PPT",
		"PPTX"
	};
	private final String[] permissionsArr = new String[] {
		Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
		Manifest.permission.WRITE_EXTERNAL_STORAGE
	};
	private View documentView;
	private ArrayList<DocumentModel> documentList;
	private DocumentAdapter documentAdapter;
	private Dialog dialog;
	private Uri imageToUploadUri;
	private int REQUEST_CAMERA = 0;
	private int SELECT_FILE = 1;
	private String leadId, ucid;
	private LinearLayout headerLayout;
	private String mCurrentPhotoPath = "";
	////////////////////
	// Progress Dialog
	private ProgressDialog pDialog;

	private static boolean hasAllPermission(Context context, String... mData) {
		boolean hasAllPermisions = true;
		for (String a : mData) {
			if (ContextCompat.checkSelfPermission(context, a)
				!= PackageManager.PERMISSION_GRANTED) {
				hasAllPermisions = false;
				break;
			}
		}
		return hasAllPermisions;
	}

	private void setUpView(View documentView) {
		RecyclerView rvDocumentList = documentView.findViewById(R.id.rvDocumentList);
		ImageView rlAddDoc = documentView.findViewById(R.id.rlAddDoc);
		headerLayout = documentView.findViewById(R.id.headerLayout);
		rlAddDoc.setOnClickListener(this);
		documentList = new ArrayList<>();
		documentAdapter = new DocumentAdapter(documentList);
		rvDocumentList.setLayoutManager(new GridLayoutManager(requireActivity(), 1));
		rvDocumentList.setAdapter(documentAdapter);
	}

	private void galleryIntent() {
		Intent intent = new Intent();
		intent.setType("*/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);//
		startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
	}

	@Override protected int getLayout() {
		return R.layout.frag_document;
	}

	@Override public View onCreateView(@NotNull final LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		documentView = super.onCreateView(inflater, container, savedInstanceState);
		container.post(() -> setAppTitle("Documents"));
		setUpView(Objects.requireNonNull(documentView));
		try {
			leadId = Objects.requireNonNull(this.getArguments()).getString(LEAD_ID);
			ucid = this.getArguments().getString(UCID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setUserVisibleHint(true);
		return documentView;
	}

	private void dialogAddDocument() {
		dialog = new Dialog(requireContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Objects.requireNonNull(dialog.getWindow())
			.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog_add_document);
		dialog.getWindow()
			.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		dialog.setCanceledOnTouchOutside(true);

		TextView tvTakePhoto = dialog.findViewById(R.id.tvTakePhoto);
		TextView tvCancel = dialog.findViewById(R.id.tvCancel);
		TextView tvAddFromDevice = dialog.findViewById(R.id.tvAddFromDevice);

		tvCancel.setText(getString(R.string.text_label_cancel));
		tvTakePhoto.setText(getString(R.string.text_label_take_photo));
		tvAddFromDevice.setText(getString(R.string.text_label_add_from_device));
		tvTakePhoto.setOnClickListener(view -> {
			dialog.dismiss();
			cameraIntent();
		});

		tvAddFromDevice.setOnClickListener(view -> {
			dialog.dismiss();
			galleryIntent();
		});

		tvCancel.setOnClickListener(view -> {
			dialog.dismiss();
		});

		dialog.show();
	}

	private void cameraIntent() {
		try {
			imageToUploadUri = null;
			try {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
					imageToUploadUri = FileProvider.getUriForFile(requireActivity(),
						requireActivity().getApplicationContext().getPackageName() + ".provider",
						createImageFile());
				} else {
					imageToUploadUri = Uri.fromFile(createImageFile()); // convert path to Uri
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Intent it = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			it.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageToUploadUri);
			startActivityForResult(it, REQUEST_CAMERA);
		} catch (Exception e) {
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			File actualImage;
			if (requestCode == REQUEST_CAMERA) {
				if (imageToUploadUri != null) {
					Uri selectedImage = imageToUploadUri;
					requireContext().getContentResolver().notifyChange(selectedImage, null);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
						Uri imageUri = Uri.parse(mCurrentPhotoPath);
						actualImage = new File(imageUri.getPath());
					} else {

						actualImage = new File(imageToUploadUri.getPath());
					}
					Uri imageUri = Uri.fromFile(actualImage);
					uploadFile(actualImage, imageUri);
				}
			} else if (requestCode == SELECT_FILE) {
				imageToUploadUri = data.getData();
				actualImage = new File(Objects.requireNonNull(imageToUploadUri).getPath());
				uploadFile(actualImage, imageToUploadUri);
			}
		}
	}

	@Override public void onClick(View view) {
		if (view.getId() == R.id.rlAddDoc) {
			if (documentAdapter.getCount() <= FILE_UPLOAD_COUNT_LIMT) {
				if (hasAllPermission(getActivity(), permissionsArr)) {
					dialogAddDocument();
				} else {
					ActivityCompat.requestPermissions(requireActivity(), permissionsArr,
						MY_PERMISSIONS_REQUEST_STORAGE_ACCESS);
				}
			} else {
				alertDialog(requireContext(), 0,
					getResources().getString(R.string.motilal_oswal),
					"Maximum 5 documents can be uploaded. Delete some to upload new document.",
					"OK", true, null);
			}
		}
	}

	private void renderLeadDocuments(UiModel<List<LeadDocumentsItem>> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		List<LeadDocumentsItem> data = uiModel.data;
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				LeadDocumentsItem item = data.get(i);
				String id = stringOrEmpty(item.id);
				String documentName = stringOrEmpty(item.documentName);
				String documentType = stringOrEmpty(item.documentType);
				String documentPath = stringOrEmpty(item.documentPath);
				String creationDate = stringOrEmpty(item.creationDate);
				String documentFullPath = stringOrEmpty(item.documentFullPath);
				creationDate =
					CRMAppUtil.getDateFromString(creationDate,
						"yyyy-MM-dd'T'kk:mm:ss",
						"MMM dd yyyy  hh:mm a");
				creationDate = creationDate.replace("a.m.", "AM")
					.replace("p.m.", "PM");
				documentList.add(
					new DocumentModel(id, documentName, documentType,
						documentPath, creationDate, documentFullPath));
			}
			showHideAddHeader();
			documentAdapter.notifyDataSetChanged();
		}
	}

	private void loadUploadedFiles() {

		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			DocumentsRequest request =
				new DocumentsRequest(new LeadDocumentsRequest(leadId, ucid, "10", "1", "0", "0"),
					new FilterRequest(null, null, null, null, null, null, null), true);
			Disposable subscribe = getSaathiApi().getLeadDocuments(request)
				.compose(applyUiModel())
				.subscribe(this::renderLeadDocuments);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderUploadDocument(UiModel<UploadDocumentItem> uiModel) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		UploadDocumentItem data = uiModel.data;
		if (data != null) {
			alertDialog(requireContext(), 0,
				getResources().getString(R.string.motilal_oswal),
				DOCUMENT_UPLOADED, "OK", true, null);
			String id = stringOrEmpty(data.iD);
			String documentName = stringOrEmpty(data.documentName);
			String documentType = stringOrEmpty(data.documentType);
			String documentPath = stringOrEmpty(data.documentPath);
			String documentFullPath = stringOrEmpty(data.documentFullPath);
			String creationDate = stringOrEmpty(data.creationDate);
			creationDate = CRMAppUtil.getDateFromString(creationDate,
				"yyyy-MM-dd'T'kk:mm:ss", "MMM dd yyyy  hh:mm a");
			creationDate = creationDate.replace("a.m.", "AM")
				.replace("p.m.", "PM");
			documentAdapter.addDocument(
				new DocumentModel(id, documentName, documentType,
					documentPath, creationDate, documentFullPath));
			showHideAddHeader();
		}
	}

	private void uploadFile(File fileToupload, Uri fileUri) {
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			showProgress();

			String filePath = "";
			FileInputStream is = null;
			try {

				ParcelFileDescriptor pfd =
					requireContext().getContentResolver().openFileDescriptor(fileUri, "r");
				FileDescriptor fd = Objects.requireNonNull(pfd).getFileDescriptor();
				is = new FileInputStream(fd);
				filePath = fileToupload.getAbsolutePath();
			} catch (Exception e) {

				e.printStackTrace();
				hideProgress();
				//TODO gett btter messages
				alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
					"Can not access File", "OK", true, null);
				return;
			}

			String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
			final String mimeType =
				Objects.requireNonNull(getActivity()).getContentResolver().getType(fileUri);
			if (mimeType != null) {
				String extention = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);

				fileName = CRMAppUtil.getFilenameWithoutExtension(fileName) + "." + extention;
			}
			try {
				String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
				if (!isFileAllowed(tokens[1], ALLOWED_FILE_TYPE)) {
					alertDialog(requireContext(), 0,
						getResources().getString(R.string.motilal_oswal),
						FILE_NOT_ALLOWED_MESSAGE, "OK", true, null);
					hideProgress();
					return;
				}
			} catch (Exception e) {
				alertDialog(requireContext(), 0,
					getResources().getString(R.string.motilal_oswal),
					SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
				hideProgress();
				return;
			}

			String strData = getBase64String(is).toString();
			if (CRMAppUtil.isSizeAbove(strData.length())) {
				alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
					FILE_LARGE_MESSAGE, "OK", true, null);
				hideProgress();
				return;
			}
			UploadDocumentsRequest request =
				new UploadDocumentsRequest(leadId, ucid, fileName.replace(":", "a"), strData);
			Disposable subscribe = getSaathiApi().postUploadDocuments(request)
				.compose(applyUiModel())
				.subscribe(this::renderUploadDocument);
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderUpdatedocuments(UiModel<String> uiModel, String id, String documentName) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		String data = uiModel.data;
		if (data != null) {
			documentAdapter.updateDocumentName(id, documentName);

			alertDialog(getActivity(), 0,
				getResources().getString(R.string.motilal_oswal),
				DOCUMENT_RENAME, "OK", true, null);
		}
	}

	private void renameLeadDocuments(final String id, final String documentName) {
		//login parameters
		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			UpdateDocumentsRequest request =
				new UpdateDocumentsRequest(id, documentName);
			Disposable subscribe = getSaathiApi().postUpdateDocuments(request)
				.compose(applyUiModel())
				.subscribe(uiModel -> {
					renderUpdatedocuments(uiModel, id, documentName);
				});
		} else {
			noInternetConnectionDialog();
		}
	}

	private void renderDeleteDocument(UiModel<String> uiModel, String id) {
		toggleProgress(uiModel.inProgress);

		Throwable error = uiModel.error;
		if (error != null) {
			showOnFailurePopup(error);
		}
		String data = uiModel.data;
		if (data != null) {
			documentAdapter.deleteDocument(id);
			alertDialog(getActivity(), 0,
				getResources().getString(R.string.motilal_oswal),
				DOCUMENT_DELETED, "OK", true,
				(action, data1) -> {
				});
		}
	}

	private void deleteLeadDocuments(final String id) {
		//login parameters

		if (CRMAppUtil.isNetworkEnabled(requireContext())) {
			DeleteDocumentRequest request =
				new DeleteDocumentRequest(id);
			Disposable subscribe = getSaathiApi().postDeleteDocuments(request)
				.compose(applyUiModel())
				.subscribe(uiModel -> {
					renderDeleteDocument(uiModel, id);
				});
		} else {
			noInternetConnectionDialog();
		}
	}

	//show addnew header if no document found else show header
	private void showHideAddHeader() {
		if (documentList != null && documentList.size() > 0) {
			headerLayout.setVisibility(View.GONE);
		} else {
			headerLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override public void setUserVisibleHint(boolean isVisibleToUser) {
		try {
			super.setUserVisibleHint(isVisibleToUser);
			if (isVisibleToUser && documentView != null) {
				loadUploadedFiles();
			}
		} catch (Exception e) {
			alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
				SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
		}
	}

	/**
	 * Showing Dialog
	 */
	private void showDialog() {
		pDialog = new ProgressDialog(requireContext());
		pDialog.setMessage("Downloading file. Please wait...");
		pDialog.setIndeterminate(false);
		pDialog.setMax(100);
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	private void showDownloadFile(File localFilePath) {
		try {
			Uri uri = FileProvider.getUriForFile(requireContext(), "com.mosl.tabapp.provider",
				localFilePath);
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

			intent.setDataAndType(uri, requireContext().getContentResolver().getType(uri));

			startActivity(intent);
		} catch (ActivityNotFoundException ex) {
			MSFDialog.alertDialog(requireContext(), 0,
				getResources().getString(R.string.motilal_oswal),
				MISSING_FILE_VIEWER + localFilePath.getAbsolutePath(), "OK", true, null);
		} catch (Exception ex) {
			MSFDialog.alertDialog(requireContext(), 0,
				getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY,
				"OK", true, null);
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String imageFileName = "POST_IMAGE";
		File storageDir =
			new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				"Camera");
		File image = File.createTempFile(imageFileName,  /* prefix */
			".jpg",         /* suffix */
			storageDir      /* directory */);
		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	//takes returns base 64string of input string
	private CharSequence getBase64String(InputStream fileInputStream) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

		try {
			byte[] buffer = new byte[8192];
			int bytesRead;

			while ((bytesRead = fileInputStream.read(buffer)) != -1) {
				output64.write(buffer, 0, bytesRead);
			}
			output64.close();
			return output.toString();
		} catch (IOException ioe) {
		} catch (Exception e) {
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (output64 != null) {
				try {
					output64.close();
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	/**
	 *
	 */
	private boolean isFileAllowed(String extension, String[] extList) {
		boolean data = false;
		for (String ext : extList) {
			if (extension.equalsIgnoreCase(ext)) {
				data = true;
				break;
			}
		}
		return data;
	}

	@Override public void onRequestPermissionsResult(int requestCode, String permissions[],
		int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_STORAGE_ACCESS: {
				if (grantResults.length > 0 && hasAllPermission(requireContext(), permissionsArr)) {
					dialogAddDocument();
				} else {
					//TODO show permisson not graned message
					Timber.tag("Document fragment").d("Permissin not granted");
				}
				return;
			}
		}
	}

	public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder1> {

		private List<DocumentModel> documentList;

		DocumentAdapter(List<DocumentModel> mdocumentList) {
			this.documentList = mdocumentList;
		}

		DocumentModel getDocumentById(String id) {
			for (DocumentModel model : documentList) {
				if (model.getId().equals(id)) {
					return model;
				}
			}
			return null;
		}

		/**
		 * return total documents in list
		 */
		public int getCount() {
			return documentList.size();
		}

		/**
		 * this will remove item from list and refresh the list
		 */
		void deleteDocument(String id) {
			DocumentModel model = getDocumentById(id);
			documentList.remove(model);
			showHideAddHeader();
			notifyDataSetChanged();
		}

		/**
		 * this will set the new document name and refersh the list
		 */
		void updateDocumentName(String id, String newName) {
			for (DocumentModel model : documentList) {
				if (model.getId().equals(id)) {
					model.setDocumentName(newName);
					break;
				}
			}
			notifyDataSetChanged();
		}

		void addDocument(DocumentModel doc) {
			if (this.documentList == null) {
				this.documentList = new ArrayList<>();
			}

			this.documentList.add(doc);
			notifyDataSetChanged();
		}

		@NotNull @Override
		public DocumentAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
			View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_upload_document, parent, false);

			return new DocumentAdapter.MyViewHolder1(itemView);
		}

		@Override
		public void onBindViewHolder(final DocumentAdapter.MyViewHolder1 holder, int position) {
			final DocumentModel documentModel = documentList.get(position);
			holder.tvDocumentName.setText(documentModel.getDocumentName());
			holder.tvDate.setText(documentModel.getCreationDate());

			holder.ivScan.setOnClickListener(v -> {

				dialog = new Dialog(requireContext());
				dialog.setTitle("Rename");
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				Objects.requireNonNull(dialog.getWindow())
					.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				dialog.setContentView(R.layout.list_edit_popup);
				dialog.getWindow()
					.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT);
				dialog.setCanceledOnTouchOutside(true);
				Button btnOK = dialog.findViewById(R.id.btnOK);
				Button btnCancel = dialog.findViewById(R.id.btnCancel);

				btnCancel.setOnClickListener(view -> {
					// TODO Auto-generated method stub
					dialog.dismiss();
				});
				final EditText etDocumentName =
					(EditText) dialog.findViewById(R.id.etDocumentName);
				etDocumentName.setText(documentModel.getDocumentName());
				btnOK.setOnClickListener(view -> {
					if (etDocumentName.getText().length() > 0) {
						//todo: hide keyboard done
						hideKeyboard(view);
						renameLeadDocuments(documentModel.getId(),
							etDocumentName.getText().toString());
						dialog.dismiss();
					} else {
						alertDialog(requireContext(), 0,
							getResources().getString(R.string.motilal_oswal),
							"File name cannot be blank.", "OK", true, null);
						dialog.dismiss();
					}
				});
				ImageView imgClose = dialog.findViewById(R.id.imgClose);
				imgClose.setOnClickListener(view -> {
					dialog.dismiss();
				});

				dialog.show();
			});

			holder.ivDeleteDoc.setOnClickListener(view -> {
				alertDialog(requireContext(), 0,
					getResources().getString(R.string.motilal_oswal),
					"Are you sure you want to delete:" + documentModel.getDocumentName() + " ?",
					"Yes", "No", true, new MSFDialog.DialogListener() {
						@Override
						public void alertDialogAction(MSFDialog.Action action, Object... data) {
							if (action == MSFDialog.Action.OK) {
								deleteLeadDocuments(documentModel.getId());
							}
						}
					});
			});
			holder.ivUploadDoc.setOnClickListener(view -> {
				new DownloadFileFromURL().execute(
					documentModel.getDocumentFullPath().replaceAll(" ", "%20"));
			});
		}

		@Override public int getItemCount() {
			return documentList.size();
		}

		class MyViewHolder1 extends RecyclerView.ViewHolder {
			ImageView ivCheck, ivScan, ivUploadDoc, ivDeleteDoc;
			TextView tvDocumentName, tvDate;

			MyViewHolder1(View view) {
				super(view);
				ivCheck = view.findViewById(R.id.ivCheck);
				tvDocumentName = view.findViewById(R.id.tvDocumentName);
				tvDate = view.findViewById(R.id.tvDate);
				ivScan = view.findViewById(R.id.ivScan);
				ivUploadDoc = view.findViewById(R.id.ivUploadDoc);
				ivDeleteDoc = view.findViewById(R.id.ivDeleteDoc);
			}
		}
	}

	/**
	 * Background Async Task to download file
	 */
	@SuppressLint("StaticFieldLeak") class DownloadFileFromURL
		extends AsyncTask<String, String, Boolean> {

		String fileName, remoteFilePath;

		/**
		 * Before starting background thread
		 * Show Progress Bar Dialog
		 */
		@Override protected void onPreExecute() {
			super.onPreExecute();
			showDialog();
		}

		/**
		 * Downloading file in background thread
		 */
		@Override protected Boolean doInBackground(String... params) {
			int count;
			try {
				remoteFilePath = params[0];
				fileName = CRMAppUtil.getFileNameFromPath(remoteFilePath);

				URL url = new URL(remoteFilePath);

				URLConnection conection = url.openConnection();
				conection.connect();
				// getting file length
				int lenghtOfFile = conection.getContentLength();

				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(url.openStream(), 8192);

				// Output stream to write file
				File baseFolder = new File(requireContext().getFilesDir(), "crmdocs");

				if (!baseFolder.exists()) {
					baseFolder.mkdir();
				}

				File localFile = new File(baseFolder, fileName);

				OutputStream output = new FileOutputStream(
					localFile.getAbsolutePath()); //Environment.getExternalStorageDirectory().toString()+"/"+fileName);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}

		/**
		 * Updating progress bar
		 */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			pDialog.setProgress(Integer.parseInt(progress[0]));
		}

		/**
		 * After completing background task
		 * Dismiss the progress dialog
		 **/
		@Override protected void onPostExecute(Boolean response) {
			pDialog.dismiss();
			if (response) {
				File baseFolder = new File(requireContext().getFilesDir(), "crmdocs");
				final File localFile = new File(baseFolder, fileName);

				alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
					FILE_DOWNLOADED + localFile.getAbsolutePath(), "Show", "Close", true,
					new MSFDialog.DialogListener() {
						@Override
						public void alertDialogAction(MSFDialog.Action action, Object... data) {
							if (action == MSFDialog.Action.OK) {//showfile
								showDownloadFile(localFile);
							}
						}
					});
			} else {
				alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
					SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
			}
		}
	}
}




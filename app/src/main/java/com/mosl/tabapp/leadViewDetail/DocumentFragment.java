package com.mosl.tabapp.leadViewDetail;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.DocumentModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Vishal on 06-Apr-17.
 */
public class DocumentFragment extends MOSLCommonFragment implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_STORAGE_ACCESS = 31;
    View documentView;
    private RecyclerView rvDocumentList;
    private ArrayList<DocumentModel> documentList;
    private DocumentAdapter documentAdapter;
    private static final String[] ALLOWED_FILE_TYPE = {
        "TXT", "DOC", "DOCX", "XLS", "PDF", "CSV", "XLSX", "PNG", "JPEG", "JPG", "GIF", "PPT",
        "PPTX"
    };
    private Dialog dialog;
    private ImageView rlAddDoc;
    private Uri imageToUploadUri;
    private int REQUEST_CAMERA = 0;
    private int SELECT_FILE = 1;
    private String leadId, ucid;

    private LinearLayout headerLayout;
    private String mCurrentPhotoPath = "";
    private File actualImage;
    public final String[] permissionsArr = new String[] {
        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private TextView tvCancel, tvTakePhoto, tvAddFromDevice;

    private void setUpView(View documentView) {
        rvDocumentList = (RecyclerView) documentView.findViewById(R.id.rvDocumentList);
        rlAddDoc = (ImageView) documentView.findViewById(R.id.rlAddDoc);
        headerLayout = (LinearLayout) documentView.findViewById(R.id.headerLayout);
        rlAddDoc.setOnClickListener(this);
        documentList = new ArrayList<>();
        documentAdapter = new DocumentAdapter(documentList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvDocumentList.setLayoutManager(new GridLayoutManager(getMainActivity(), 1));
        rvDocumentList.setAdapter(documentAdapter);
    }

    public static Map getUploadHeader(Context context) {
        Map headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        headerMap.put("token", AccountDetails.getToken());
        headerMap.put("userid", AccountDetails.getUserId());
        headerMap.put("clientinfo", "android");
        headerMap.put("ipaddress", CRMAppUtil.getLocalIpAddress());
        headerMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());
        headerMap.put("macaddress",
            CRMMOSLCommon.getDeviceID(context, context.getContentResolver()));
        headerMap.put("EmployeeCode", AccountDetails.getEmployeeCode());
        headerMap.put("AppId", "2");
        return headerMap;
    }

    public static boolean hasAllPermission(Context context, String... mData) {
        boolean hasAllPermisions = true;
        for (String a : mData) {
            if (ContextCompat.checkSelfPermission(context, a)
                != PackageManager.PERMISSION_GRANTED) {
                hasAllPermisions = false;
                break;
            } else {
                continue;
            }
        }
        return hasAllPermisions;
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override public View onCreateView(final LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        documentView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_document);
        setAppTitle(getClass().toString(), "Documents");
        setUpView(documentView);
        try {
            leadId = this.getArguments().getString(LEAD_ID);
            ucid = this.getArguments().getString(UCID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setUserVisibleHint(true);
        return documentView;
    }

    // Clear bitmap
    public static void clearBitmap(Bitmap bm) {
        bm.recycle();
        System.gc();
    }

    private void dialogAddDocument() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_add_document);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setCanceledOnTouchOutside(true);

        tvTakePhoto = (TextView) dialog.findViewById(R.id.tvTakePhoto);
        tvCancel = (TextView) dialog.findViewById(R.id.tvCancel);
        tvAddFromDevice = (TextView) dialog.findViewById(R.id.tvAddFromDevice);

        tvCancel.setText("Cancel");
        tvTakePhoto.setText("Take photo");
        tvAddFromDevice.setText("Add From Device");
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                dialog.dismiss();
                cameraIntent();
            }
        });

        tvAddFromDevice.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                dialog.dismiss();
                galleryIntent();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void cameraIntent() {
        try {
            imageToUploadUri = null;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        getActivity().getApplicationContext().getPackageName() + ".provider",
                        createImageFile());
                    imageToUploadUri = photoURI;
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

    // Read bitmap
    public Bitmap readBitmap(Uri selectedImage) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor =
                getMainActivity().getContentResolver().openAssetFileDescriptor(selectedImage, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null,
                    options);
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                if (imageToUploadUri != null) {
                    Uri selectedImage = imageToUploadUri;
                    getActivity().getContentResolver().notifyChange(selectedImage, null);
                    Bitmap reducedSizeBitmap = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //                            reducedSizeBitmap = getBitmapForNandAbove(mCurrentPhotoPath);
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
                actualImage = new File(imageToUploadUri.getPath());
                uploadFile(actualImage, imageToUploadUri);
            }
        }
    }

    @Override public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlAddDoc: {
                if (documentAdapter.getCount() <= FILE_UPLOAD_COUNT_LIMT) {
                    if (hasAllPermission(getActivity(), permissionsArr)) {
                        dialogAddDocument();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), permissionsArr,
                            MY_PERMISSIONS_REQUEST_STORAGE_ACCESS);
                    }
                } else {
                    alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        "Maximum 5 documents can be uploaded. Delete some to upload new document.",
                        "OK", true, null);
                }
            }
        }
    }

    public void loadUploadedFiles() {
        if (CRMAppUtil.isNetworkEnabled(getActivity())) {
            showProgress();

            Map inputMap = new HashMap();
            inputMap.put("LeadID", leadId);
            inputMap.put(UCID, ucid);
            inputMap.put("PageSize", "10");
            inputMap.put("Page", "1");
            inputMap.put("CalculatedPage", "0");
            inputMap.put("TotalCount", "0");

            String serviceMethod = "Lead/GetLeadDocuments";

            try {
                CRMWebServiceHandler.postWithRequestAndFilterObject(getMainActivity(),
                    serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject json) {
                            super.onSuccess(statusCode, headers, json);

                            if (statusCode == 200) {
                                try {
                                    String status = json.getString(STATUS);
                                    hideProgress();
                                    if (!isBlank(status) && status.equalsIgnoreCase("true")) {
                                        JSONObject response = json.getJSONObject("Response");
                                        JSONArray dataArray = response.getJSONArray("Data");
                                        for (int ctr = 0; ctr < dataArray.length(); ctr++) {
                                            JSONObject obj = (JSONObject) dataArray.get(ctr);

                                            String id =
                                                obj.isNull("ID") || obj.get("ID").equals("") ? ""
                                                    : obj.getString("ID");

                                            String documentName =
                                                obj.isNull("DocumentName") || obj.get(
                                                    "DocumentName").equals("") ? ""
                                                    : obj.getString("DocumentName");
                                            String documentType =
                                                obj.isNull("DocumentType") || obj.get(
                                                    "DocumentType").equals("") ? ""
                                                    : obj.getString("DocumentType");
                                            String documentPath =
                                                obj.isNull("DocumentPath") || obj.get(
                                                    "DocumentPath").equals("") ? ""
                                                    : obj.getString("DocumentPath");
                                            String creationDate =
                                                obj.isNull("CreationDate") || obj.get(
                                                    "CreationDate").equals("") ? ""
                                                    : obj.getString("CreationDate");
                                            String documentFullPath =
                                                obj.isNull("DocumentFullPath") || obj.get(
                                                    "DocumentFullPath").equals("") ? ""
                                                    : obj.getString("DocumentFullPath");

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
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(json);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } catch (Exception ex) {
                                    hideProgress();
                                    ex.printStackTrace();
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            } else {
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == 401) {

                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    public void uploadFile(File fileToupload, Uri fileUri) {
        if (CRMAppUtil.isNetworkEnabled(getActivity())) {
            showProgress();

            String filePath = "";
            FileInputStream is = null;
            try {

                ParcelFileDescriptor pfd =
                    getActivity().getContentResolver().openFileDescriptor(fileUri, "r");
                FileDescriptor fd = pfd.getFileDescriptor();
                is = new FileInputStream(fd);
                filePath = fileToupload.getAbsolutePath();
            } catch (Exception e) {

                e.printStackTrace();
                hideProgress();
                //TODO gett btter messages
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    "Can not access File", "OK", true, null);
                return;
            }

            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            final String mimeType = getActivity().getContentResolver().getType(fileUri);
            if (mimeType != null) {
                String extention = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);

                fileName = CRMAppUtil.getFilenameWithoutExtension(fileName) + "." + extention;
            }
            String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
            if (tokens != null) {
                try {
                    if (!isFileAllowed(tokens[1], ALLOWED_FILE_TYPE)) {
                        alertDialog(getMainActivity(), 0,
                            getResources().getString(R.string.motilal_oswal),
                            FILE_NOT_ALLOWED_MESSAGE, "OK", true, null);
                        hideProgress();
                        return;
                    }
                } catch (Exception e) {
                    alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                    hideProgress();
                    return;
                }
            }
            Map headerMap = getUploadHeader(getContext());
            headerMap.put("LeadId", leadId);
            headerMap.put(UCID, ucid);
            headerMap.put("filename", fileName.replace(":", "a"));

            String strData = getBase64String(is).toString();
            if (CRMAppUtil.isSizeAbove(strData.length())) {
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    FILE_LARGE_MESSAGE, "OK", true, null);
                hideProgress();
                return;
            }
            //body map
            Map boadyMap = new HashMap();
            boadyMap.put(LEADID, leadId);
            boadyMap.put(UCID, ucid);
            boadyMap.put("DocumentName", fileName.replace(":", "a"));
            boadyMap.put("Base64FileString", strData);
            String serviceMethod = "Lead/UploadDocument";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), boadyMap,
                    new JsonHttpResponseHandler() {

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                            if (statusCode == 200) {
                                String status = null;
                                try {
                                    status = response.getString(STATUS);
                                    if (!isBlank(status) && status.equalsIgnoreCase("true")) {
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            DOCUMENT_UPLOADED, "OK", true, null);
                                        JSONObject obj = response.getJSONObject(RESPONSE);

                                        String id =
                                            obj.isNull("ID") || obj.get("ID").equals("") ? ""
                                                : obj.getString("ID");

                                        String documentName =
                                            obj.isNull("DocumentName") || obj.get("DocumentName")
                                                .equals("") ? "" : obj.getString("DocumentName");
                                        String documentType =
                                            obj.isNull("DocumentType") || obj.get("DocumentType")
                                                .equals("") ? "" : obj.getString("DocumentType");
                                        String documentPath =
                                            obj.isNull("DocumentPath") || obj.get("DocumentPath")
                                                .equals("") ? "" : obj.getString("DocumentPath");
                                        String documentFullPath =
                                            obj.isNull("DocumentFullPath") || obj.get(
                                                "DocumentFullPath").equals("") ? ""
                                                : obj.getString("DocumentFullPath");
                                        String creationDate =
                                            obj.isNull("CreationDate") || obj.get("CreationDate")
                                                .equals("") ? "" : obj.getString("CreationDate");

                                        creationDate = CRMAppUtil.getDateFromString(creationDate,
                                            "yyyy-MM-dd'T'kk:mm:ss", "MMM dd yyyy  hh:mm a");
                                        creationDate = creationDate.replace("a.m.", "AM")
                                            .replace("p.m.", "PM");
                                        documentAdapter.addDocument(
                                            new DocumentModel(id, documentName, documentType,
                                                documentPath, creationDate, documentFullPath));
                                        showHideAddHeader();
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } catch (JSONException e) {
                                    hideProgress();
                                    e.printStackTrace();
                                }
                            } else {
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    public void renameLeadDocuments(final String id, final String documentName) {
        //login parameters
        if (CRMAppUtil.isNetworkEnabled(getActivity())) {
            showProgress();
            Map inputMap = new HashMap();
            inputMap.put("id", id);
            inputMap.put("DocumentName", documentName);
            String serviceMethod = "Lead/UpdateLeadDocuments";
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject json) {
                            super.onSuccess(statusCode, headers, json);

                            if (statusCode == 200) {
                                try {
                                    String status = json.getString(STATUS);
                                    hideProgress();
                                    if (!isBlank(status) && status.equalsIgnoreCase("true")) {
                                        documentAdapter.updateDocumentName(id, documentName);

                                        alertDialog(getActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            DOCUMENT_RENAME, "OK", true, null);
                                    } else {

                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(json);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } catch (Exception ex) {
                                    hideProgress();
                                    ex.printStackTrace();
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            } else {
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    public void deleteLeadDocuments(final String id) {
        //login parameters

        if (CRMAppUtil.isNetworkEnabled(getActivity())) {
            showProgress();

            Map inputMap = new HashMap();
            inputMap.put("id", id);

            String serviceMethod = "Lead/DeleteLeadDocuments";

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject json) {
                            super.onSuccess(statusCode, headers, json);

                            if (statusCode == 200) {
                                try {
                                    String status = json.getString(STATUS);
                                    hideProgress();
                                    if (!isBlank(status) && status.equalsIgnoreCase("true")) {
                                        documentAdapter.deleteDocument(id);
                                        alertDialog(getActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            DOCUMENT_DELETED, "OK", true,
                                            new MSFDialog.DialogListener() {
                                                @Override public void alertDialogAction(
                                                    MSFDialog.Action action, Object... data) {
                                                    if (action == MSFDialog.Action.OK) {

                                                    }
                                                }
                                            });
                                    } else {
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(json);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } catch (Exception ex) {
                                    hideProgress();
                                    ex.printStackTrace();
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            } else {
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
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

    ////////////////////
    // Progress Dialog
    private ProgressDialog pDialog;

    @Override public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && documentView != null) {
                loadUploadedFiles();
            }
        } catch (Exception e) {
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
        }
    }

    /**
     * Showing Dialog
     */
    public void showDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void showDownloadFile(File localFilePath) {
        try {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.mosl.tabapp.provider",
                localFilePath);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setDataAndType(uri, getActivity().getContentResolver().getType(uri));

            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal),
                MISSING_FILE_VIEWER + localFilePath.getAbsolutePath(), "OK", true, null);
        } catch (Exception ex) {
            MSFDialog.alertDialog(getMainActivity(), 0,
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
     * */
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
                if (grantResults.length > 0 && hasAllPermission(getActivity(), permissionsArr)) {
                    dialogAddDocument();
                } else {
                    //TODO show permisson not graned message
                    Log.i("Document fragment", "Permissin not granted");
                }
                return;
            }
        }
    }

    public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.MyViewHolder1> {

        private List<DocumentModel> documentList;

        public class MyViewHolder1 extends RecyclerView.ViewHolder {
            public ImageView ivCheck, ivScan, ivUploadDoc, ivDeleteDoc;
            public TextView tvDocumentName, tvDate;

            public MyViewHolder1(View view) {
                super(view);
                ivCheck = (ImageView) view.findViewById(R.id.ivCheck);
                tvDocumentName = (TextView) view.findViewById(R.id.tvDocumentName);
                tvDate = (TextView) view.findViewById(R.id.tvDate);
                ivScan = (ImageView) view.findViewById(R.id.ivScan);
                ivUploadDoc = (ImageView) view.findViewById(R.id.ivUploadDoc);
                ivDeleteDoc = (ImageView) view.findViewById(R.id.ivDeleteDoc);
            }
        }

        public DocumentAdapter(List<DocumentModel> mdocumentList) {
            this.documentList = mdocumentList;
        }

        public DocumentModel getDocumentById(String id) {
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
        public void deleteDocument(String id) {
            DocumentModel model = getDocumentById(id);
            documentList.remove(model);
            showHideAddHeader();
            notifyDataSetChanged();
        }

        /**
         * this will set the new document name and refersh the list
         */
        public void updateDocumentName(String id, String newName) {
            for (DocumentModel model : documentList) {
                if (model.getId().equals(id)) {
                    model.setDocumentName(newName);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        public void addDocument(DocumentModel doc) {
            if (this.documentList == null) {
                this.documentList = new ArrayList<>();
            }

            this.documentList.add(doc);
            notifyDataSetChanged();
        }

        @Override
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

            holder.ivScan.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    dialog = new Dialog(context);
                    dialog.setTitle("Rename");
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.list_edit_popup);
                    dialog.getWindow()
                        .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                    dialog.setCanceledOnTouchOutside(true);
                    Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
                    Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                    btnCancel.setOnClickListener(new Button.OnClickListener() {
                        @Override public void onClick(View v) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
                    final EditText etDocumentName =
                        (EditText) dialog.findViewById(R.id.etDocumentName);
                    etDocumentName.setText(documentModel.getDocumentName());
                    btnOK.setOnClickListener(new Button.OnClickListener() {
                        @Override public void onClick(View v) {
                            if (etDocumentName.getText().length() > 0) {
                                //todo: hide keyboard done
                                hideKeyboard(v);
                                renameLeadDocuments(documentModel.getId(),
                                    etDocumentName.getText().toString());
                                dialog.dismiss();
                            } else {
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    "File name cannot be blank.", "OK", true, null);
                                dialog.dismiss();
                            }
                        }
                    });
                    ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);
                    imgClose.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            holder.ivDeleteDoc.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    alertDialog(getMainActivity(), 0,
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
                }
            });
            holder.ivUploadDoc.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    //downloadPDF(documentModel.getDocumentFullPath().replaceAll(" ", "%20"));
                    new DownloadFileFromURL().execute(
                        documentModel.getDocumentFullPath().replaceAll(" ", "%20"));
                }
            });
        }

        @Override public int getItemCount() {
            return documentList.size();
        }
    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, Boolean> {

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
                File baseFolder = new File(context.getFilesDir(), "crmdocs");

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
                File baseFolder = new File(context.getFilesDir(), "crmdocs");
                final File localFile = new File(baseFolder, fileName);

                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
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
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
            }
        }
    }
}




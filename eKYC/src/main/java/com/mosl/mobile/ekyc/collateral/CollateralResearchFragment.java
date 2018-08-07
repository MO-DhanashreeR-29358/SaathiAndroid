package com.mosl.mobile.ekyc.collateral;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.MOSLCommon;

import com.mosl.mobile.ekyc.database.CollateralDBService;
import com.mosl.mobile.ekyc.model.CollateralModel;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.CustomDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.COLLATERALDATA_SERVICE_CATEGORY;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.COLLATERALDATA_SERVICE_FLAG;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.NO;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.SOME_SERVER_ERROR_OCCURRED;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.YES;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;

/**
 * Created by Owner on 7/18/2016.
 */
public class CollateralResearchFragment extends Fragment {
    private String TAG = CollateralResearchFragment.class.getCanonicalName();
    private String COLLATERAL_SECTION;
    CollateralDBService dbService;
    View collateralResearchView;
    ListView lvResearch;
    CollateralResearchAdapter collateralResearchAdapter;
    ProgressDialog progressDialog;
    ProgressBar pbLoadingIndicator;
    TextView tvHeader;
    boolean showInnerProgress;
    boolean loadLocalData;

    public void setSection(String section) {
        this.COLLATERAL_SECTION = section;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            collateralResearchView = inflater.inflate(R.layout.frag_collateral_research, container, false);
            setUpView(collateralResearchView);
            setPermisson(getActivity(),getContext());
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID()+"-"+getAppLoginID());
//            Crashlytics.logException(e);
        }
        return collateralResearchView;
    }

    private void setUpView(View collateralResearchView) {
        onCreateDialog();
        lvResearch = (ListView) collateralResearchView.findViewById(R.id.lvResearch);
        EditText searchText = (EditText) collateralResearchView.findViewById(R.id.searchText);
        pbLoadingIndicator = (ProgressBar) collateralResearchView.findViewById(R.id.pbLoadingIndicator);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int countNew) {
                //Log.d(TAG,s.toString());
                int id = 0;
                String searchValue = s.toString();
                int fullCount = searchValue.length();
                String type = "SEARCH";
                showInnerProgress = true;
                if (fullCount >= 3) {
                    searchValue = s.toString();
                    type = "SEARCH";
                } else if (fullCount > 0) {
                    searchValue = "T";
                    type = "SEARCHALL";
                }

                loadServerData(id, searchValue, type);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvHeader = (TextView) collateralResearchView.findViewById(R.id.txtHeader);
        tvHeader.setText(COLLATERAL_SECTION);

        collateralResearchAdapter = new CollateralResearchAdapter(getActivity());
        lvResearch.setAdapter(collateralResearchAdapter);

        int id = 0;
        String searchValue = "T";
        String type = "SEARCHALL";
        loadData(id, searchValue, type);
    }

    public void setDbService(CollateralDBService dbService) {
        this.dbService = dbService;
    }

    private void loadData(final int id, final String searchValue, final String type) {

        if (MOSLCommon.isNetworkEnabled(getActivity())) {
            loadServerData(id, searchValue, type);
        } else {
            if (loadLocalData) {
                loadLocalData(id, searchValue, type);
            } else {
               /* MOSLCommon.alertDialog(getActivity(), 0, "Oops!",
                        "No Internet, would you like to load the local stored document list?", "YES", "NO", false, new MOSLCommon.DialogListener() {
                            @Override
                            public void alertDialogAction(MOSLCommon.Action action, Object... data) {

                                if (action == MOSLCommon.Action.OK) {
                                    loadLocalData = true;
                                    loadLocalData(id, searchValue, type);

                                }
                            }
                        });*/

                CustomDialog localDocListDialog = new CustomDialog(getActivity(), "No Internet, would you like to load the local stored document list?", true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                    @Override
                    public void dialogAction(MOSLCommon.Action action, Object... data) {
                        if (action == MOSLCommon.Action.OK) {
                            loadLocalData = true;
                            loadLocalData(id, searchValue, type);

                        }
                    }
                });
                localDocListDialog.show();
            }
        }
    }

    private void loadLocalData(int id, String searchValue, String type) {
        //load data from local databas
        collateralResearchAdapter.clearData();
        List<CollateralModel> list = dbService.getCollateralBySection(COLLATERAL_SECTION);
        collateralResearchAdapter.addList(list);
        collateralResearchAdapter.notifyDataSetChanged();
    }

    private void loadServerData(int id, String searchValue, String type) {
        if (MOSLCommon.isNetworkEnabled(getContext())) {
            Map inputMap = new HashMap<>();
            inputMap.put("ID", id);
            inputMap.put("SearchVal", searchValue);
            inputMap.put("Type", type);
            inputMap.put("Section", COLLATERAL_SECTION);

            String flag = COLLATERALDATA_SERVICE_FLAG;
            String category = COLLATERALDATA_SERVICE_CATEGORY;
            if (showInnerProgress) {
                pbLoadingIndicator.setVisibility(View.VISIBLE);
                showInnerProgress = false;
            } else {
                CollateralActivity.customProgress.setVisibility(View.VISIBLE);
            }
            WebServiceHandler.post(getContext(), flag, category, inputMap, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    //after success code goes here
                    try {
                        if (statusCode == 200) {
                            if (response != null) {

                                if (response.has("Collaterallinks")) {
                                    JSONArray responseArray = response.getJSONArray("Collaterallinks");
                                    if (responseArray.length() != 0) {
                                        //first clear old data if any
                                        collateralResearchAdapter.clearData();

                                        for (int i = 0; i < responseArray.length(); i++) {
                                            JSONObject object = responseArray.getJSONObject(i);
                                            int Id = object.isNull("Id") ? -1 : object.getInt("Id");
                                            String Section = (object.isNull("Section") || object.getString("Section").equals("")) ? "" : object.getString("Section");
                                            String Title = (object.isNull("Title") || object.getString("Title").equals("")) ? "" : object.getString("Title");
                                            String FileName = (object.isNull("FileName") || object.getString("FileName").equals("")) ? "" : object.getString("FileName");
                                            String FileType = (object.isNull("FileType") || object.getString("FileType").equals("")) ? "" : object.getString("FileType");
                                            String Summary = (object.isNull("Summary") || object.getString("Summary").equals("")) ? "" : object.getString("Summary");
                                            String serverVersion = (object.isNull("Version") || object.getString("Version").equals("")) ? "" : object.getString("Version");
                                            String URL = (object.isNull("URL") || object.getString("URL").equals("")) ? "" : object.getString("URL");

                                            CollateralModel model = new CollateralModel(Id, Section, Title, FileName, FileType, Summary, null, serverVersion, URL, "N");
                                            model = dbService.addCollateral(model); //save/update into db
                                            collateralResearchAdapter.addItem(model);
                                        }

                                        collateralResearchAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                                    MOSLCommon.showToastMsg(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY);
                                }

                            }
                            CollateralActivity.customProgress.setVisibility(View.GONE);
                            pbLoadingIndicator.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                    MOSLCommon.alertDialog(getActivity(), 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                        CollateralActivity.customProgress.setVisibility(View.GONE);
                    }
                    CollateralActivity.customProgress.setVisibility(View.GONE);
                    pbLoadingIndicator.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    pbLoadingIndicator.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    pbLoadingIndicator.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    pbLoadingIndicator.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }
            });
        } else {
            MOSLCommon.alertDInternet(getContext());
        }
    }


    public class CollateralResearchAdapter extends BaseAdapter {
        private final LayoutInflater inflater;
        Context mContext;
        List<CollateralModel> data = new ArrayList<>();


        public CollateralResearchAdapter(Context c) {
            inflater = LayoutInflater.from(c);
            mContext = c;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public CollateralModel getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        public void addList(List<CollateralModel> list) {
            data.addAll(list);
        }

        public void addItem(CollateralModel model) {
            data.add(model);
        }

        public void clearData() {
            data.clear();
        }

        public void markDownloadStatus(int collateralId, String downloadStatus) {
            for (CollateralModel model : data) {
                if (model.getId() == collateralId) {
                    model.setDownloaded(downloadStatus);
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            final Holder holder = new Holder();
            view = inflater.inflate(R.layout.item_list_collateral_research, parent, false);

            final CollateralModel model = data.get(position);
            holder.txtResearchTitle = (TextView) view.findViewById(R.id.txtResearchTitle);
            holder.txtDownloadProgressMsg = (TextView) view.findViewById(R.id.txtDownloadProgressMsg);
            holder.txtResearchLocalVersion = (TextView) view.findViewById(R.id.txtResearchLocalVersion);
            holder.txtResearchServerVersion = (TextView) view.findViewById(R.id.txtResearchServerVersion);
            holder.imDownload = (ImageView) view.findViewById(R.id.imDownload);
            holder.imView = (ImageView) view.findViewById(R.id.imView);

            holder.txtResearchTitle.setText(model.getTitle());
            holder.txtResearchLocalVersion.setText("Local Ver: " + model.getLocalVersion());
            holder.txtResearchServerVersion.setText("Server Ver: " + model.getServerVersion());

            holder.imDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MOSLCommon.isNetworkEnabled(getActivity())) {
                        new DownloadFileFromURL(holder.txtDownloadProgressMsg, false).execute(Integer.toString(model.getId()), model.getFileName(), model.getUrl());
                    } else {
                        MOSLCommon.alertDInternet(getActivity());
                    }
                }
            });

            if (Integer.parseInt(model.getLocalVersion()) < Integer.parseInt(model.getServerVersion()) || !model.getDownloaded().equals("Y")) {
                holder.imDownload.setVisibility(View.VISIBLE);
            } else {
                holder.imDownload.setVisibility(View.GONE);
            }

            if (model.getFileType().equalsIgnoreCase("pdf")) {
                holder.imView.setImageResource(R.drawable.ic_pdf_icon);
            } else {
                holder.imView.setImageResource(R.drawable.ic_jpg_icon);
            }

            if (!MOSLCommon.isBlank(model.getDownloaded()) && model.getDownloaded().equals("Y")) {
                holder.imView.setVisibility(View.VISIBLE);
            } else {
                holder.imView.setVisibility(View.GONE);
            }

            holder.imView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (MOSLCommon.isBlank(model.getLocalFilePath())) {
                            MOSLCommon.showToastMsg(getActivity(), "File not found on local storage");
                        } else {
                            //if new version is available
                            if (Integer.parseInt(model.getLocalVersion()) < Integer.parseInt(model.getServerVersion())) {
                                /*MOSLCommon.alertDialog(getActivity(), 0, "New version alert!",
                                        "New version of this file is available on server, do you want to download?", "YES", "NO", false, new MOSLCommon.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MOSLCommon.Action action, Object... data) {

                                                if (action == MOSLCommon.Action.OK) {
                                                    //download new file
                                                    new DownloadFileFromURL(holder.txtDownloadProgressMsg, true).execute(Integer.toString(model.getId()), model.getFileName(), model.getUrl());
                                                } else {
                                                    //open existing file
                                                    viewFile(model.getLocalFilePath());
                                                }
                                            }
                                        });*/


                                CustomDialog newVerDialog = new CustomDialog(getActivity(), "New version of this file is available on server, do you want to download?", true, false, true, "New version alert!", YES, NO, new CustomDialog.CustomDialogListener() {
                                    @Override
                                    public void dialogAction(MOSLCommon.Action action, Object... data) {
                                        if (action == MOSLCommon.Action.OK) {
                                            //download new file
                                            new DownloadFileFromURL(holder.txtDownloadProgressMsg, true).execute(Integer.toString(model.getId()), model.getFileName(), model.getUrl());
                                        } else {
                                            //open existing file
                                            viewFile(model.getLocalFilePath());
                                        }
                                    }
                                });
                                newVerDialog.show();
                            } else {
                                viewFile(model.getLocalFilePath());
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        commonMsgDialog(getActivity(), SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                        MOSLCommon.showToastMsg(getActivity(), MOSLConstants.SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN);
                    }
                }

            });
            return view;
        }

        class Holder {
            TextView txtResearchTitle, txtResearchLocalVersion, txtResearchServerVersion, txtDownloadProgressMsg;
            ImageView imDownload, imView;
        }
    }

    private void viewFile(String fileLocalFilePath) {
        String extension = fileLocalFilePath.substring(fileLocalFilePath.lastIndexOf(".") + 1, fileLocalFilePath.length());
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("content://" + getActivity().getPackageName() + "/" + fileLocalFilePath);
            intent.setDataAndType(uri, mimeType);
            Intent intentChooser = Intent.createChooser(intent, "Choose File");
            startActivity(intentChooser);

        } catch (ActivityNotFoundException e) {
            MOSLCommon.showToastMsg(getActivity(), "No application available to view " + extension + " file");
        }
    }
    ////////////////////

    protected void onCreateDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Downloading file. Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ;
        //progressDialog.show();
    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        int collateralId;
        boolean viewAfterDownload;
        String fileLocalPath;
        TextView progressTextInList;

        public DownloadFileFromURL(TextView progressTextInList, boolean viewAfterDownload) {
            this.viewAfterDownload = viewAfterDownload;
            this.progressTextInList = progressTextInList;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String id = f_url[0];
                collateralId = Integer.parseInt(id);

                final String fileName = f_url[1];
                URL url = new URL(f_url[2]);
                //String viewAfterDownloadStr = f_url[3];
                //viewAfterDownload = viewAfterDownloadStr.equalsIgnoreCase("Y")?true:false;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.setMessage("Downloading file: " + fileName);
                    }
                });


                File file = new File(getActivity().getFilesDir(), fileName);
                fileLocalPath = file.getAbsolutePath();

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(file.getAbsolutePath());

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

                markDownload(collateralId, "Y");

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error: ", e.getMessage());
                //dbService.markDownloaded(collateralId, "N");
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            final int pct = Integer.parseInt(progress[0]);
            // setting progress percentage
            progressDialog.setProgress(pct);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressTextInList.setText("Downloading " + pct + "/100%");
                }
            });
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded

            MOSLCommon.showToastMsg(getActivity(), "File download completed");
            progressDialog.dismiss();
            progressTextInList.setText("");
            if (viewAfterDownload) {
                //view file
                viewFile(fileLocalPath);
            }
        }

    }

    private void markDownload(final int collateralId, final String downloadStatus) {
        dbService.markDownloaded(collateralId, downloadStatus);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                collateralResearchAdapter.markDownloadStatus(collateralId, downloadStatus);

            }
        });

    }
}

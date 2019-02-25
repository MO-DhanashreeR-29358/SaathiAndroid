package com.mosl.mobile.ekyc.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.adapter.ChatAdapter;
import com.mosl.mobile.ekyc.common.CustomTabsHelper;
import com.mosl.mobile.ekyc.common.CustomViewPager;
import com.mosl.mobile.ekyc.common.EkycUtil;
import com.mosl.mobile.ekyc.common.FileUtil;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.model.UploadDetaisModel;
import com.mosl.mobile.ekyc.service.SignalRService;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.view.DigitalSignature;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.viewpagerindicator.TabPageIndicator;
import cz.msebera.android.httpclient.Header;
import id.zelory.compressor.Compressor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;
import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getDeviceID;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isNomineeTabApplicable;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isVideoVerificationRequired;
import static com.mosl.mobile.ekyc.common.MOSLCommon.removeKeyboard;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setListViewHeightBasedOnChildren;
import static com.mosl.mobile.ekyc.common.MOSLCommon.setPermisson;
import static com.mosl.mobile.ekyc.model.CommonModel.getAadharNo;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;
import static com.mosl.mobile.ekyc.model.CommonModel.getBaFranchiseeCode;
import static com.mosl.mobile.ekyc.model.CommonModel.getBankTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getDematTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getDownloadPdfUrl;
import static com.mosl.mobile.ekyc.model.CommonModel.getEsignTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getInterestedInMF;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsAadharVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsCVLVerified;
import static com.mosl.mobile.ekyc.model.CommonModel.getIsEquitySelected;
import static com.mosl.mobile.ekyc.model.CommonModel.getPANName;
import static com.mosl.mobile.ekyc.model.CommonModel.getPersonalTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getTokenKey;
import static com.mosl.mobile.ekyc.model.CommonModel.getUploadTab;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserID;
import static com.mosl.mobile.ekyc.model.CommonModel.getUserType;
import static com.mosl.mobile.ekyc.model.CommonModel.isDirectProspect;
import static com.mosl.mobile.ekyc.model.CommonModel.isIPVDone;
import static com.mosl.mobile.ekyc.model.CommonModel.isMfIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.isOffline;
import static com.mosl.mobile.ekyc.model.CommonModel.isOfflineCliencodeGenrated;
import static com.mosl.mobile.ekyc.model.CommonModel.setDownloadPdfUrl;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsEquityIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsMfIpvDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsOffline;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsOfflineDocumentUploadedDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setIsVerificationDone;
import static com.mosl.mobile.ekyc.model.CommonModel.setTokenKey;
import static com.mosl.mobile.ekyc.ui.EkycMainActivity.customProgress;

/**
 * Created by raju on 6/15/2016.
 */
public class UploadDetailsFragment extends UploadFragment implements MOSLConstants {
    File actualImage;

    String TAG = "UploadDetailsFragment";
    //upload start
    String userChoosenTask = "";
    int REQUEST_CAMERA = 0;
    int SELECT_FILE = 1;
    String TAKE_PHOTO = "Take Photo";
    String CHOOSE_FROM_GALLERY = "Choose from Gallery";
    private String currentSelectedDocForUpload;
    //upload end
    //signalR start
    private boolean mBound = false;
    private SignalRReceiver signalRReceiver;
    private SignalRService mService;
    private String verifierName;
    private WebView webViewVideo;
    String consoleMsgUrl = "";
    String mCurrentPhotoPath;
    DigitalSignature signature;

    //signalr end

    //upload start

    //upload end

    ///video
    //VideoChildFragment videoDialog;
    //Button btnVideo;
    LinearLayout videoLayout;
    boolean videoEnabled;
    ////// video end

    Button btnVerifyProof;
    View uploadDetailView;
    RelativeLayout rlSubmit, rlVerifyProof;
    TextView txtFilechoosePAN, txtFilechooseAFS, tvPAN;
    //public static final int RESULT_OK = -1;
    int count = 0;

    private static int RESULT_LOAD_IMAGE = 1;
    private EditText messageET;
    private ListView messagesContainer;
    private Button btnSubmit;
    private ImageView sendBtn;
    ImageView ivAddNew;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatHistory;
    TextView tvCourierMsg, tvChatHeader, tvConnectionStatus;
    RelativeLayout tvExpand;
    private ImageView imgConnect;
    RelativeLayout reltiveExpand;
    LinearLayout llChat, llAddNew, llStepsToCompleteAccOpening, llCourierStep;
    String addressType;
    Context mContext;
    //Dialog dialog;

    File file;
    Dialog dialog;
    Bitmap bitmap;

    ListView lvUploadList, lvStepsToCompleteAccOpening;
    UploadDetailAdapter uploadDetailAdapter;
    CustomViewPager viewPager;
    TabPageIndicator tabPageIndicator;
    LinearLayout llDirectUploadInstructions, llIPVNote, llPreFillAccOpen;
    //    RadioGroup rgUpload;
    //    CheckBox cbCanDownload, cbCannotDownload;
    String preFillFormpdf = "";
    Context uploadDetailContext;
    private Integer MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private ImageView ivChat;
    private TextView tvInstruction1, tvInstruction2, tvInstruction3, tvInstruction4;
    private CustomTabsClient mClient;
    Button mClear, mGetSign;
    ImageView ivClose;

    public UploadDetailAdapter getUploadDetailAdapter() {
        return uploadDetailAdapter;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setTabPageIndicator(TabPageIndicator tabPageIndicator) {
        this.tabPageIndicator = tabPageIndicator;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            uploadDetailView = inflater.inflate(R.layout.frag_upload_detail, container, false);
            setUpView(uploadDetailView);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return uploadDetailView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        uploadDetailContext = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpView(View uploadDetailView) {
        viewPager = (CustomViewPager) getActivity().findViewById(R.id.vpApply);
        lvUploadList = (ListView) uploadDetailView.findViewById(R.id.lvUploadList);
        // lvStepsToCompleteAccOpening = (ListView) uploadDetailView.findViewById(R.id.lvStepsToCompleteAccOpening);
        btnVerifyProof = (Button) uploadDetailView.findViewById(R.id.btnVerifyProof);
        btnSubmit = (Button) uploadDetailView.findViewById(R.id.btnSubmit);

        ivAddNew = (ImageView) uploadDetailView.findViewById(R.id.ivAddNew);
        ivChat = (ImageView) uploadDetailView.findViewById(R.id.ivChat);
        // videoLayout = (LinearLayout) uploadDetailView.findViewById(R.id.videoLayout);
        // llStepsToCompleteAccOpening = (LinearLayout) uploadDetailView.findViewById(R.id.llStepsToCompleteAccOpening);
        llAddNew = (LinearLayout) uploadDetailView.findViewById(R.id.llAddNew);
        llDirectUploadInstructions = (LinearLayout) uploadDetailView.findViewById(R.id.llDirectUploadInstructions);
        llIPVNote = (LinearLayout) uploadDetailView.findViewById(R.id.llIPVNote);
        llPreFillAccOpen = (LinearLayout) uploadDetailView.findViewById(R.id.llPreFillAccOpen);
        dialog = new Dialog(uploadDetailContext);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_signature);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        rlSubmit = (RelativeLayout) uploadDetailView.findViewById(R.id.rlSubmit);
        rlVerifyProof = (RelativeLayout) uploadDetailView.findViewById(R.id.rlVerifyProof);
        tvExpand = (RelativeLayout) uploadDetailView.findViewById(R.id.tvExpand);

        imgConnect = (ImageView) uploadDetailView.findViewById(R.id.imgConnect);
        tvCourierMsg = (TextView) uploadDetailView.findViewById(R.id.tvCourierMsg);
        tvInstruction1 = (TextView) uploadDetailView.findViewById(R.id.tvInstruction1);
        tvInstruction2 = (TextView) uploadDetailView.findViewById(R.id.tvInstruction2);
        tvInstruction3 = (TextView) uploadDetailView.findViewById(R.id.tvInstruction3);
        tvInstruction4 = (TextView) uploadDetailView.findViewById(R.id.tvInstruction4);
        tvChatHeader = (TextView) uploadDetailView.findViewById(R.id.tvChatHeader);
        tvConnectionStatus = (TextView) uploadDetailView.findViewById(R.id.tvConnectionStatus);

        reltiveExpand = (RelativeLayout) uploadDetailView.findViewById(R.id.reltiveExpand);

        llCourierStep = (LinearLayout) uploadDetailView.findViewById(R.id.llCourierStep);
        llChat = (LinearLayout) uploadDetailView.findViewById(R.id.llChat);

        tvPAN = (TextView) uploadDetailView.findViewById(R.id.tvPAN);
        messagesContainer = (ListView) uploadDetailView.findViewById(R.id.messagesContainer);
        messageET = (EditText) uploadDetailView.findViewById(R.id.messageEdit);
        sendBtn = (ImageView) uploadDetailView.findViewById(R.id.chatSendButton);
        if (isIPVDone()) {
            llAddNew.setVisibility(View.GONE);
        }
        if (getAppType().equals(DIRECT) || getUserType().equalsIgnoreCase(IFAUSER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btnVerifyProof.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ipv_btn));
            } else {
                btnVerifyProof.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ipv_btn));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                btnVerifyProof.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ipv_btn_branch));
            } else {
                btnVerifyProof.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ipv_btn_branch));
            }
        }

       /* if (!videoEnabled) {
            showVideoSection();
        }*/
        if (getAppType().equals(DIRECT) && getInterestedInMF()) {
            llDirectUploadInstructions.setVisibility(View.VISIBLE);
            llIPVNote.setVisibility(View.VISIBLE);
        } else {
            llDirectUploadInstructions.setVisibility(View.GONE);
            llIPVNote.setVisibility(View.GONE);
        }

        if (getInterestedInMF() && (getIsCVLVerified() || getIsAadharVerified())) {
            tvInstruction1.setText(getResources().getString(R.string.acc_open_verified_step_1));
            tvInstruction2.setText(getResources().getString(R.string.acc_open_verified_step_2));
            tvInstruction3.setText(getResources().getString(R.string.acc_open_verified_step_3));
            tvInstruction4.setText(getResources().getString(R.string.acc_open_verified_step_4));
        } else if (getInterestedInMF() && !getIsCVLVerified() && !getIsAadharVerified()) {
            tvInstruction1.setText(getResources().getString(R.string.acc_open_step_1));
            tvInstruction2.setText(getResources().getString(R.string.acc_open_step_2));
            tvInstruction3.setText(getResources().getString(R.string.acc_open_step_3));
            tvInstruction4.setText(getResources().getString(R.string.acc_open_step_4));
        }

        if (getInterestedInMF()) {
            llDirectUploadInstructions.setVisibility(View.VISIBLE);
            llPreFillAccOpen.setVisibility(View.VISIBLE);
            disableEnableField();
            /* ArrayList<RowData> arrayList = new ArrayList<>();

            String[] steps = new String[]{"Step 1", "Step 2", "Step 3", "Step 4", "Step 5"};

            String[] descriptions = new String[]{
                    "Download your Pre-filled Account Opening form.",
                    "Take a Print of the form.",
                    "Sign the Account Opening Form ( at all places indicated on the form ) and self attest the requisite proofs.",
                    "Scan the signed copy of Account Opening Form and Proofs and Upload in the &quot;Upload Documents&quot; section below.",
                    "Courier/Post physical copy of signed Account Opening Form and self attested proofs at the address mentioned below."};


            for (int i = 0; i < steps.length; i++) {
                RowData item = new RowData(steps[i], descriptions[i]);

                arrayList.add(item);
            }

            UploadAccOpenStepsAdapter adapter = new UploadAccOpenStepsAdapter(mContext, arrayList);
            lvStepsToCompleteAccOpening.setAdapter(adapter);
            setListViewHeightBasedOnChildren(lvStepsToCompleteAccOpening);*/
            // llStepsToCompleteAccOpening.setVisibility(View.VISIBLE);
        } else {
            llDirectUploadInstructions.setVisibility(View.GONE);
            // llStepsToCompleteAccOpening.setVisibility(View.GONE);
            llPreFillAccOpen.setVisibility(View.GONE);
        }

        if (getInterestedInMF() && !getIsCVLVerified() && !getIsAadharVerified()) {
            tvCourierMsg.setVisibility(View.VISIBLE);
            llCourierStep.setVisibility(View.VISIBLE);
        } else {
            tvCourierMsg.setVisibility(View.GONE);
            llCourierStep.setVisibility(View.GONE);
        }
       /* btnVideo = (Button) uploadDetailView.findViewById(R.id.btnVideo);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoDialog.show(getActivity().getSupportFragmentManager(), "IPV");
            }
        });*/


        tvExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSectionExpansion(reltiveExpand);
            }
        });
        showSubmitButton();
//        rlVerifyProof.setVisibility(View.VISIBLE);
//        rlSubmit.setVisibility(View.GONE);
        llChat.setVisibility(View.GONE);
        // cbCanDownload.setChecked(true);

        customProgress.setVisibility(View.GONE);

        btnVerifyProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!uploadDetailAdapter.isMandatoryUploaded()) {
                    commonMsgDialog(mContext, getResources().getString(R.string.proof_upload_warning), false, "");
                } else if (!uploadDetailAdapter.isAdditionalUploaded()) {
                    commonMsgDialog(mContext, getResources().getString(R.string.proof_upload_warning), false, "");
                } else {
                    unBindSignalrService();
                    initSignalR();
                }

            }
        });
        llPreFillAccOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialog cdPreFormDownload = new CustomDialog(mContext, "Download Pre Filled Account Opening Form ?", true, true, false, "", YES, NO, new CustomDialog.CustomDialogListener() {
                    @Override
                    public void dialogAction(MOSLCommon.Action action, Object... data) {
                        if (action == MOSLCommon.Action.OK) {
                            if (!preFillFormpdf.equals("")) {
                                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                                    downloadPDF(preFillFormpdf, mContext);
                                    openWebUrl(Uri.parse(getDownloadPdfUrl() + EkycUtil.encrypt(preFillFormpdf + "|" + getUserID() + "|" + getTokenKey() + "|" + "")),
                                            preFillFormpdf.substring(preFillFormpdf.lastIndexOf("\\") + 1));
                                } else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        ((EkycMainActivity) mContext).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                                    }
                                }
                            } else {
                                CustomDialog cdNoPdf = new CustomDialog(mContext, "PDF not available", true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                    @Override
                                    public void dialogAction(MOSLCommon.Action action, Object... data) {

                                    }
                                });
                                cdNoPdf.show();
                            }
                        }
                    }
                });
                cdPreFormDownload.show();
            }
        });

        /*cbCanDownload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbCanDownload.setChecked(true);
                    cbCannotDownload.setChecked(false);
                } else {
                    cbCanDownload.setChecked(false);
                    cbCannotDownload.setChecked(true);
                }
            }
        });
        cbCannotDownload.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbCannotDownload.setChecked(true);
                    cbCanDownload.setChecked(false);
                } else {
                    cbCannotDownload.setChecked(false);
                    cbCanDownload.setChecked(true);
                }
            }
        });*/
        llAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDoc = uploadDetailAdapter.getAvailableAddnDoc();

                if (newDoc == null) {
                    MOSLCommon.showToastMsg(uploadDetailContext, "Limit Reached");
                    return;
                } else if (newDoc.equals("ALREADY_EXISTS")) {
                    MOSLCommon.showToastMsg(uploadDetailContext, "Kindly upload your proof document");
                    return;
                }

                UploadDetaisModel newModelToShow = uploadDetailAdapter.getItemByDocumentName(newDoc);
                newModelToShow.setNewUploaded(true);

                if (newModelToShow != null) {
                    newModelToShow.setVisible(true);
                    uploadDetailAdapter.notifyDataSetChanged();

                    lvUploadList.post(new Runnable() {
                        @Override
                        public void run() {
                            // Select the last row so it will scroll into view...
                            lvUploadList.setSelection(uploadDetailAdapter.getCount() - 1);
                        }
                    });
                    setListViewHeightBasedOnChildren(lvUploadList);

                }

                //}
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * if direct prospect user show submit button else normal flow
                * Note: If cvl Verified MF Direct prospect user then normal flow
                * if Offline process and isOfflineCliencodeGenrated flag is true then show message
                * */
                if (isOffline() && isOfflineCliencodeGenrated()) {
                    commonMsgDialog(getActivity(), getResources().getString(R.string.offline_client_code_msg), false, "");
                } else {
                    if (!isOffline() && isDirectProspect() && !isIPVDone() && !getUserType().equalsIgnoreCase(IFAUSER)) {
                        if ((getInterestedInMF() && getIsCVLVerified())) {
                            getProofUploadFinalSubmit(FinalSubmitMF);
                        } else {
                            getProofSubmitDirectBa();
                        }
                    } else {
                        if (getInterestedInMF()) {
                            getProofUploadFinalSubmit(FinalSubmitMF);
                        } else {
                            getProofUploadFinalSubmit("");
                        }
                    }
                }
                /*if (getInterestedInMF()) {
                    getProofUploadFinalSubmit(FinalSubmitMF);
                } else {
                    getProofUploadFinalSubmit("");
                }*/
            }
        });
        initChatSection();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageET.getText().toString();
                if (MOSLCommon.isNetworkEnabled(mContext)) {
                    if (TextUtils.isEmpty(messageText)) {
                        return;
                    }

                    if (mBound) {
                        mService.sendMessage(messageText);

                        ChatMessage chatMessage = new ChatMessage();
                        chatMessage.setId(Long.parseLong(getUserID()));
                        chatMessage.setMessage("Me: " + messageText);
                        chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                        chatMessage.setMe(true);

                        messageET.requestFocus();
                        messageET.setText("");
//                    messageET.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE|InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

//                        removeKeyboard(mContext, messageET);
//                        displayMessage(chatMessage, true);

                        chatAdapter.add(chatMessage);

                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            public void run() {
                                chatAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } else {
                    MOSLCommon.alertDInternet(mContext);
                }
            }
        });
        setUserVisibleHint(true);

        try {
            CustomTabsServiceConnection mConnection = new CustomTabsServiceConnection() {
                @Override
                public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                    mClient = customTabsClient;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    mClient = null;
                }
            };
            String packag = CustomTabsHelper.getPackageNameToUse(getContext());
            CustomTabsClient.bindCustomTabsService(getContext(), packag, mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    * if direct prospect user show submit button else normal flow
    * Note: If cvl Verified MF Direct prospect user then normal flow
    * */
    private void showSubmitButton() {
        if (isOffline()) {
            rlVerifyProof.setVisibility(View.GONE);
            rlSubmit.setVisibility(View.VISIBLE);
        } else {
            if (isDirectProspect() && !getUserType().equalsIgnoreCase(IFAUSER)) {
                if ((getInterestedInMF() && getIsCVLVerified())) {
                    rlVerifyProof.setVisibility(View.VISIBLE);
                    rlSubmit.setVisibility(View.GONE);
                } else {
                    rlVerifyProof.setVisibility(View.GONE);
                    rlSubmit.setVisibility(View.VISIBLE);
                }
            } else {
                rlVerifyProof.setVisibility(View.VISIBLE);
                rlSubmit.setVisibility(View.GONE);
            }
        }
    }
    public void informationDialog(String msg) {
        final Dialog dialog = new Dialog(uploadDetailContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
        Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);

        tvMsg.setText(msg);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customProgress.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                customProgress.setVisibility(View.GONE);

            }
        });

        dialog.show();
    }

    public void getProofUploadFinalSubmit(final String userStep) {
        if (MOSLCommon.isNetworkEnabled(uploadDetailContext)) {
            Map inputMap = new HashMap<>();
            inputMap.put("UserID", getUserID());
            inputMap.put("_IsOffline", isOffline() ? "1" : "0");

            String flag = "";
            if (getInterestedInMF()) {
                inputMap.put("UserStep", userStep);
                flag = GetProofUploadFinalSubmit_MF;
            } else {
                flag = GETPROOFUPLOADFINALSUBMIT;
            }
            String category = PROOFUPLOADS;
            customProgress.setVisibility(View.VISIBLE);

            WebServiceHandler.post(uploadDetailContext, flag, category, inputMap, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    //after success code goes here
                    try {
                        if (statusCode == 200) {

                            if (response.length() != 0) {

                                String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                                String message = (response.isNull("_Message") || response.getString("_Message").equals("")) ? "" : response.getString("_Message");
                                preFillFormpdf = (response.isNull("_Url") || response.getString("_Url").equals("")) ? "" : response.getString("_Url");


                                if (!status.equals("")) {
                                    int intStatus = Integer.parseInt(status);
                                    if (getInterestedInMF()) {
                                    /*{"calltype":null,"_ApplicationType":"DIRECT","_Status":"1",
                                            "_DocumentStatus":null,"_Message":"PDF Generated Successfully",
                                            "_Url":"http://web20.motilaloswal.com:8000/PDFUpload//Upload/PROOF/1630444/1630444_MF.pdf"}*/

                                        if (intStatus == 1) {
                                            if (userStep.equals(FinalSubmitMF)) {
                                                tabPageIndicator.enableTab(getEsignTab());
                                                viewPager.setCurrentItem(getEsignTab());
                                            }
                                        /*if (userStep.equals(FinalSubmitMF)) {
                                            if (!isAadharVerified) {
                                                //Manual case
                                               *//* Intent intent = new Intent(uploadDetailContext, EsignActivity.class);
                                                intent.putExtra(UPLOAD_FINAL_SUBMIT, true);
                                                startActivity(intent);*//*
//                                                setIsUploadFinalSubmit(true);
                                                viewPager.setCurrentItem(4);
                                            } else {
                                               *//* Intent intent = new Intent(uploadDetailContext, EsignActivity.class);
                                                startActivity(intent);*//*
                                                viewPager.setCurrentItem(4);
                                            }
                                        }*/
                                        } else {
                                            CustomDialog cdErrorMsg = new CustomDialog(mContext, message, true, false, false, "", OK, "", new CustomDialog.CustomDialogListener() {
                                                @Override
                                                public void dialogAction(MOSLCommon.Action action, Object... data) {

                                                }
                                            });
                                            cdErrorMsg.show();
                                        }

                                    } else {
                                        if (intStatus == 1) {

//                                    getEsignDetails();
                                       /* Intent intent = new Intent(uploadDetailContext, EsignActivity.class);
                                        startActivity(intent);*/
                                            tabPageIndicator.enableTab(getEsignTab());
                                            viewPager.setCurrentItem(getEsignTab());

                                        } else {
                                            informationDialog(message);
                                            btnSubmit.setVisibility(View.VISIBLE);
                                            btnVerifyProof.setVisibility(View.VISIBLE);
                                            llChat.setVisibility(View.GONE);
                                        }

                                    }
                                } else {
                                    customProgress.setVisibility(View.GONE);
                                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                                }
                                customProgress.setVisibility(View.GONE);

                            } else {
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            }

                        }
                        customProgress.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                    MOSLCommon.alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                    }
                    // MOSLCommon.showToastMsg(BranchLoginActivity.this, "Status Code :" + statusCode);
                    customProgress.setVisibility(View.GONE);
                    //  onLoginSuccess();
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");

                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    MOSLCommon.showToastMsg(mContext, responseString);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

            });
        } else {
            MOSLCommon.alertDInternet(uploadDetailContext);
        }
    }

    /*private void getEsignDetails() {


        Map inputMap = new HashMap<>();
        inputMap.put("UserID", getUserID());

        String flag = GETESIGNDETAILS;
        String category = ESIGN;


        WebServiceHandler.post(uploadDetailContext, flag, category, inputMap, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //after success code goes here

                customProgress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                 *//*[
                {
                    "ClientName":"Karuna Vasant Kamble",
                        "ApplicationFrom":"PP",
                        "AadharNo":"613785271500",
                        "EQPayment":"3",
                        "COMMPayment":"0",
                        "PRODUCT":"EQUITY",
                        "PDFPATH":"http:\/\/web20.motilaloswal.com:8000\/PDFUpload\/upload\/proof\/613785271500\/613785271500_EQFinal.pdf"
                }
                ]*//*
                if (statusCode == 200) {

                    if (response.length() != 0) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                String clientName = (object.isNull("ClientName") || object.getString("ClientName").equals("")) ? "" : object.getString("ClientName");
                                String getAadharNo() = (object.isNull("AadharNo") || object.getString("AadharNo").equals("")) ? "" : object.getString("AadharNo");
                                String pdfPATH = (object.isNull("PDFPATH") || object.getString("PDFPATH").equals("")) ? "" : object.getString("PDFPATH");
                                if (!pdfPATH.equals("")) {
                                    Intent intent = new Intent(uploadDetailContext, EsignActivity.class);
                                    intent.putExtra(PDF_PATH, pdfPATH);
                                    intent.putExtra(AADHAR, getAadharNo());
                                    startActivity(intent);
                                } else {
                                    Log.d("UploadDetail", "pre pdf path is empty");
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }

                customProgress.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                customProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                customProgress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                customProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                MOSLCommon.showToastMsg(mContext, responseString);
                customProgress.setVisibility(View.GONE);
            }

        });
    }*/

    public void loadUploadDetails() {
        if (viewPager == null || viewPager.getCurrentItem() != getUploadTab()) {
            return;
        }
        getProofUploads();
        if (!getIsCVLVerified() && !getIsAadharVerified()) {
            setProofUploadOptionMF();
        }
    }

    public void getProofUploads() {
        if (MOSLCommon.isNetworkEnabled(uploadDetailContext)) {
            customProgress.setVisibility(View.VISIBLE);
            final Map inputMap = new HashMap<>();
//            inputMap.put("UserID", "1590266");
            inputMap.put("UserID", getUserID());
            inputMap.put("_IsDirectBA", isDirectProspect() ? "1" : "0");

            String flag = GETPROOFUPLOADS;
            String category = PROOFUPLOADS;

            WebServiceHandler.post(uploadDetailContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            Map<String, UploadDetaisModel> uploadedMap = new HashMap<String, UploadDetaisModel>();

                            List<UploadDetaisModel> arrayListFull = new ArrayList<UploadDetaisModel>();
                            if (response.length() == 0) {
                                btnVerifyProof.setVisibility(View.GONE);
                            } else {
                                btnVerifyProof.setVisibility(View.VISIBLE);
                                llChat.setVisibility(View.GONE);
                            }

                            if (response.has("UPLOAD_PROOF_DETAILS")) {
                                JSONArray jsonArray = response.getJSONArray("UPLOAD_PROOF_DETAILS");
                                if (jsonArray.length() != 0) {
                                    //Log.d("data", String.valueOf(jsonArray));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        int srno = object.has("SRNO") ? object.getInt("SRNO") : 0;
                                        String key = (object.isNull("FileType") || object.getString("FileType").equals("")) ? "" : object.getString("FileType");
                                        String fileUploadedPath = (object.isNull("FILEPATH") || object.getString("FILEPATH").equals("")) ? "" : object.getString("FILEPATH");
                                        int proofStatus = object.isNull("ProofStatus") ? -1 : object.getInt("ProofStatus");
                                        String verifierRejectReason = (object.isNull("VerifierRejectReason") || object.getString("VerifierRejectReason").equals("")) ? "" : object.getString("VerifierRejectReason");
                                        boolean eCopy = (object.isNull("ECOPY")) ? false : object.getBoolean("ECOPY");
                                        String eCopyMessage = (object.isNull("ECOPYMESSAGE") || object.getString("ECOPYMESSAGE").equals("")) ? "" : object.getString("ECOPYMESSAGE");

                                        //we assume here that the file is uploaded
                                        if (proofStatus == 1) //rejected
                                            eCopy = false;
                                        verifierRejectReason = verifierRejectReason == null || verifierRejectReason.equals("") ? "Uploaded Successfully" : verifierRejectReason;
                                        UploadDetaisModel uploadedModel = new UploadDetaisModel(key, fileUploadedPath, proofStatus, verifierRejectReason, eCopy, eCopyMessage);
                                        uploadedModel.setUploadStatus(DOCUMENT_UPLOAD_STATUS_SUCCESS); //uploaded successfully
                                        uploadedMap.put(key, uploadedModel);
                                    }

                                }
                            }

                            if (response.has("UPLOAD_PROOF_DETAILS1")) {
                                JSONArray jsonArray = response.getJSONArray("UPLOAD_PROOF_DETAILS1");
                                if (jsonArray.length() != 0) {
                                    //Log.d("data", String.valueOf(jsonArray));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String key = (object.isNull("DOCUMENTKEY") || object.getString("DOCUMENTKEY").equals("")) ? "" : object.getString("DOCUMENTKEY");
                                        int srno = object.has("SRNO") ? object.getInt("SRNO") : 0;
                                        String displayText = (object.isNull("DISPLAYTEXT") || object.getString("DISPLAYTEXT").equals("")) ? "" : object.getString("DISPLAYTEXT");
                                        String displayNote = (object.isNull("DISPLAYNOTES") || object.getString("DISPLAYNOTES").equals("")) ? "" : object.getString("DISPLAYNOTES");
                                        String mandatory = (object.isNull("Mandatory") || object.getString("Mandatory").equals("")) ? "" : object.getString("Mandatory");
                                        String tokenKey = (object.isNull("TOKENKEY") || object.getString("TOKENKEY").equals("")) ? "" : object.getString("TOKENKEY");
                                        String downloadPdfUrl = (object.isNull("DOWNLOADPDFURL") || object.getString("DOWNLOADPDFURL").equals("")) ? "" : object.getString("DOWNLOADPDFURL");
                                        UploadDetaisModel docToBeUploaded = new UploadDetaisModel(srno, key, displayText, mandatory, displayNote);

                                        setTokenKey(tokenKey);
                                        setDownloadPdfUrl(downloadPdfUrl);
                                        UploadDetaisModel uploadedDoc = uploadedMap.get(key);

                                        if (uploadedDoc != null) {
                                            docToBeUploaded.setFileUploadedPath(uploadedDoc.getFileUploadedPath());
                                            docToBeUploaded.setProofStatus(uploadedDoc.getProofStatus());
                                            docToBeUploaded.setUploadStatus(uploadedDoc.getUploadStatus());
                                            docToBeUploaded.seteCopy(uploadedDoc.iseCopy());
                                            docToBeUploaded.seteCopyMessage(uploadedDoc.geteCopyMessage());
                                            if (uploadedDoc.getProofStatus() == 0) {
                                                if (uploadedDoc.getUploadStatus() == DOCUMENT_UPLOAD_STATUS_SUCCESS) {
                                                    if (uploadedDoc.iseCopy())
                                                        docToBeUploaded.setVerifierRejectReason(uploadedDoc.geteCopyMessage());
                                                    else
                                                        docToBeUploaded.setVerifierRejectReason("Uploaded Successfully");
                                                }
                                            } else {
                                                docToBeUploaded.setVerifierRejectReason(uploadedDoc.getVerifierRejectReason());
                                            }
                                            docToBeUploaded.setVisible(uploadedDoc.isVisible()
                                            );

                                        }

                                        arrayListFull.add(docToBeUploaded);
                                    }
                                    uploadDetailAdapter = new UploadDetailAdapter(uploadDetailContext, arrayListFull);
                                    lvUploadList.setAdapter(uploadDetailAdapter);
                                    setListViewHeightBasedOnChildren(lvUploadList);

                                    if (isIPVDone()) {
                                        rlSubmit.setVisibility(View.VISIBLE);
                                        rlVerifyProof.setVisibility(View.GONE);
                                        if (getIsEquitySelected()) {
                                            setIsEquityIpvDone(true);
                                        } else {
                                            setIsEquityIpvDone(false);
                                        }
                                        if (getInterestedInMF()) {
                                            setIsMfIpvDone(true);
                                            disableEnableField();
                                        } else {
                                            setIsMfIpvDone(false);
                                        }
                                    }

                                    getVerificationStatus(false);
                                    if (getInterestedInMF()) {
                                        getProofUploadFinalSubmit(ICanUpload);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        customProgress.setVisibility(View.GONE);
                    } else {
                        MOSLCommon.showToastMsg(uploadDetailContext, NO_RESPONSE);
                        customProgress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("Failure", "no resp in Data");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                }
            });
        } else {
            MOSLCommon.alertDInternet(uploadDetailContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    private void toggleSectionExpansion(View parent) {
        if (parent.getVisibility() == View.VISIBLE) {
            parent.setVisibility(View.GONE);
            removeKeyboard(mContext, parent);
            //icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0);
            ivChat.setImageResource(R.drawable.ic_upload_plus);
        } else {
            parent.setVisibility(View.VISIBLE);
            //icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0);
            ivChat.setImageResource(R.drawable.ic_upload_minus);
        }
    }

    public void displayMessage(ChatMessage message, boolean expand) {
        chatAdapter.add(message);

        if (expand && reltiveExpand.getVisibility() == View.GONE) {
            reltiveExpand.setVisibility(View.VISIBLE);

        } else if (!expand) {
            reltiveExpand.setVisibility(View.GONE);
        }
        /*else{
            reltiveExpand.setVisibility(View.GONE);
        }*/
        ((Activity) mContext).runOnUiThread(new Runnable() {
            public void run() {
                chatAdapter.notifyDataSetChanged();
            }
        });
        //adapter.notifyDataSetChanged();
        //scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void initChatSection() {

        chatHistory = new ArrayList<ChatMessage>();

        /*ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setMe(false);
        msg.setMessage("Hi");
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg);
        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        msg1.setMessage("How r u doing???");
        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);*/

        chatAdapter = new ChatAdapter(getActivity(), new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(chatAdapter);

        /*for (int i = 0; i < chatHistory.size(); i++) {
            ChatMessage message = chatHistory.get(i);
            displayMessage(message);
        }*/
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                if (requestCode == REQUEST_CAMERA) {
                    if (imageToUploadUri != null) {
                        Uri selectedImage = imageToUploadUri;
                        mContext.getContentResolver().notifyChange(selectedImage, null);
                        Bitmap reducedSizeBitmap = null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            reducedSizeBitmap = getBitmapForNandAbove(mCurrentPhotoPath);
                            Uri imageUri = Uri.parse(mCurrentPhotoPath);
                            actualImage = new File(imageUri.getPath());
                        } else {
//                            reducedSizeBitmap = getBitmap(imageToUploadUri.getPath());
                            actualImage = new File(imageToUploadUri.getPath());
                        }
                        compressImage();
                        /*if (reducedSizeBitmap != null) {
                            onCaptureImageResult(reducedSizeBitmap);
                        } else {
                            Log.w("Tag", "Bitmap null");
                        }*/
                    }
                } else if (requestCode == SELECT_FILE) {
                    //onSelectFromGalleryResult(data);
                    actualImage = FileUtil.from(mContext, data.getData());
                    previewGalleryImage(actualImage);
                    //Bitmap compressedImageBitmap = new Compressor(mContext).compressToBitmap(actualImage);
                    //uploadFile(compressedImageBitmap,currentSelectedDocForUpload,SELECT_FILE);
//                    compressImage();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void compressImage() {
            /*new Compressor(mContext)
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            uploadFile(file,currentSelectedDocForUpload,SELECT_FILE);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });*/
        try {
            File compressedImageFile = new Compressor(mContext).compressToFile(actualImage);//PNG image not supported but its handel internally
            uploadFile(compressedImageFile, currentSelectedDocForUpload);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void previewGalleryImage(File actualImage) {
        try {
            final Dialog dialog = new Dialog(uploadDetailContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_gallery_preview);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            ImageView ivPreviewImage = (ImageView) dialog.findViewById(R.id.ivPreviewImage);
            Button btnOK = (Button) dialog.findViewById(R.id.btnOK);
            Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

            ivPreviewImage.setImageURI(Uri.fromFile(actualImage));
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    compressImage();
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();// Out of memory issue in setImage So call upload method. 13-Nov-2017
            compressImage();
        }

    }

    private Bitmap getBitmapForNandAbove(String path) {
        Uri imageUri = Uri.parse(path);
        File file = new File(imageUri.getPath());
        try {
            InputStream ims = new FileInputStream(file);
            return BitmapFactory.decodeStream(ims);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private Bitmap getBitmap(String path) {

        Uri uri = Uri.fromFile(new File(path));
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
            in = mContext.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = mContext.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
                int height = b.getHeight();
                int width = b.getWidth();
                Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

            Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());
            return b;
        } catch (IOException e) {
            Log.e("", e.getMessage(), e);
            return null;
        }
    }

    /////////////////// UPLOAD DOCUMENT \\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void uploadFile(Bitmap bmp, String docName, int imageSource) {

    }

    public void uploadFile(File file, String docName) {
        if (MOSLCommon.isNetworkEnabled(uploadDetailContext)) {
            customProgress.setVisibility(View.VISIBLE);
            String data = null;
            try {
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
//                InputStream fileInputStream = new ByteArrayInputStream(bos.toByteArray());
                InputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
                byte[] buffer = new byte[8192];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    output64.write(buffer, 0, bytesRead);
                }
                output64.close();
                data = output.toString();
                fileInputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//            MOSLCommon.alertDialog(mContext, 0, "Motilal Oswal", "Some error occurred, please retry", "OK", true, null);
                return;
            } finally {
                //clearBitmap(bmp);
            }

            //check file size limit of 2 MB
            long fileSizeInKB = data.length() / 1024;
            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            long fileSizeInMB = fileSizeInKB / 1024;

            if (fileSizeInMB > 2) {
                commonMsgDialog(mContext, Size_limit_is_2MB, false, "");
                return;
            }

            Map inputMap = new HashMap<>();
            inputMap.put("UserID", getUserID());
            inputMap.put("calltype", docName);

            final String _ApplicationType = getAppType().equals(CRM) || getAppType().equals(PP) ? "PARTNER" : "DIRECT";
            inputMap.put("_ApplicationType", _ApplicationType);
            inputMap.put("AadharNo", getAadharNo());
            inputMap.put("IP", "0.0.0.0");
            inputMap.put("File", data);

            String flag = "UPLOAD";
            String category = "PROOFUPLOADS";
            WebServiceHandler.post(mContext, flag, category, inputMap, new JsonHttpResponseHandler() {


                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        // {"calltype":"PAN","_ApplicationType":null,"_Status":"2","_DocumentStatus":"3","_Message":"Uploaded Successfully","_Url":"~C:\\Vineet\\EKYCMOBILEAPP\\EKYCMobileAPI\\PDFUpload\\Upload\\PROOF\\\\_PAN.jpg"}
                        String docName = response.has("calltype") ? response.getString("calltype").toString() : null;
                        //String _ApplicationType = response.has("_ApplicationType") ? response.getString("_ApplicationType").toString() : null;

                        /**
                         * 0  Indicates error and need to try again
                         * 1 indicates validation message: example document is already approved.
                         * 2 indicates file is successfully uploaded.
                         */
                        String _Status = response.has("_Status") ? response.getString("_Status").toString() : null;

                        String _DocumentStatus = response.has("_DocumentStatus") ? response.getString("_DocumentStatus").toString() : null;
                        String _Message = response.has("_Message") ? response.getString("_Message").toString() : null;
                        String _Url = response.has("_Url") ? response.getString("_Url").toString() : null;

                        int statusInt = Integer.parseInt(_Status);
                        uploadDetailAdapter.updateUploadStatus(docName, statusInt, _Message, _Url);
                        //uploadDetailAdapter.notifyDataSetInvalidated();
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            public void run() {
                                uploadDetailAdapter.notifyDataSetChanged();
                            }
                        });

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                    MOSLCommon.alertDialog(mContext, 0, "Motilal Oswal", "Some error occurred, please retry", "OK", true, null);
                        return;
                    }
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

            });
        } else {
            MOSLCommon.alertDInternet(uploadDetailContext);
        }
    }

    ////////////////////// SIGNALR \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void initSignalR() {
        if (MOSLCommon.isNetworkEnabled(uploadDetailContext)) {
            customProgress.setVisibility(View.VISIBLE);
            rlVerifyProof.setVisibility(View.GONE);
            Map inputMap = new HashMap<>();
            inputMap.put("UserID", getUserID());
            final String requestFrom = getAppType().equals(CRM) || (getAppType().equals(PP) && !getUserType().equalsIgnoreCase(IFAUSER)) ? "PARTNER" : "DIRECT";
            inputMap.put("RequestFrom", requestFrom);

            String flag = GETVERIFIERROOMID;
            String category = PROOFUPLOADS;

            WebServiceHandler.post(uploadDetailContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        String adminId = response.has("AdminID") && !response.isNull("AdminID") ? response.getString("AdminID").toString() : null;
                        String roomId = response.has("RoomID") && !response.isNull("RoomID") ? response.getString("RoomID").toString() : null;
                        String emailId = response.has("EmailID") && !response.isNull("EmailID") ? response.getString("EmailID").toString() : null;
                        String userName = response.has("Username") && !response.isNull("Username") ? response.getString("Username").toString() : null;
                        String country = response.has("Country") && !response.isNull("Country") ? response.getString("Country").toString() : null;
                        String clientName = response.has("Clientname") && !response.isNull("Clientname") ? response.getString("Clientname").toString() : null;
                        String isSuccess = response.has("IsSuccess") && !response.isNull("IsSuccess") ? response.getString("IsSuccess").toString() : null;
                        String message = response.has("Message") && !response.isNull("Message") ? response.getString("Message").toString() : null;
                        String userid = response.has("Userid") && !response.isNull("Userid") ? response.getString("Userid").toString() : null;
                        String OldRoomID = response.has("OldRoomID") && !response.isNull("OldRoomID") ? response.getString("OldRoomID").toString() : null;
                        String stataus = response.has("Status") && !response.isNull("Status") ? response.getString("Status").toString() : null;

                        /*
                        * Status value
                        * 0 for Error
                        * 1 for Success
                        * 2 for Offline
                        * */
                        customProgress.setVisibility(View.GONE);
                        if (!isBlank(stataus) && stataus.equals("0")) {
                            commonMsgDialog(mContext, message, false, "");
                            rlVerifyProof.setVisibility(View.VISIBLE);
                        } else if (stataus.equals("1")) {
                            verifierName = userName;
                            MOSLCommon.showToastMsg(mContext, userName + " is available. Connecting to verifier, please wait.....");

                            Intent intent = new Intent();
                            intent.setClass(mContext, SignalRService.class);
                            intent.putExtra("requestFrom", requestFrom);
                            intent.putExtra("roomId", roomId);
                            intent.putExtra("userName", getPANName());
                            intent.putExtra("OldRoomID", OldRoomID);
                            mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                        } else if (stataus.equals("2")) {
                            CustomDialog customDialog = new CustomDialog(mContext, getResources().getString(R.string.offline_verification_msg), true, true, false, "", REALTIME, OFFLINE,
                                    new CustomDialog.CustomDialogListener() {
                                        @Override
                                        public void dialogAction(MOSLCommon.Action action, Object... data) {
                                            if (action == MOSLCommon.Action.NO) {
                                                if (getInterestedInMF()) {
                                                    setIsOffline(true);
                                                    setIsOfflineDocumentUploadedDone(true);
                                                    rlVerifyProof.setVisibility(View.GONE);
                                                    rlSubmit.setVisibility(View.VISIBLE);
                                                    getProofUploadFinalSubmit(FinalSubmitMF);
                                                } else {
                                                    setIsOffline(true);
                                                    setIsOfflineDocumentUploadedDone(true);
                                                    rlVerifyProof.setVisibility(View.GONE);
                                                    rlSubmit.setVisibility(View.VISIBLE);
                                                    getProofUploadFinalSubmit("");
                                                }
                                            }
                                        }
                                    });
                            customDialog.show();
                            rlVerifyProof.setVisibility(View.VISIBLE);
                        }
                        /*if (isBlank(adminId)) {
                            customProgress.setVisibility(View.GONE);

                            if (message == null || message.equals("")) {
                                message = "All executive are busy at this moment. Please retry after sometime";
                            }
                            commonMsgDialog(mContext, message, false, "");
//                            MOSLCommon.alertDialog(mContext, 0, "Motilal Oswal", message, "OK", true, null);
                        } else {
                            verifierName = userName;
                            MOSLCommon.showToastMsg(mContext, userName + " is available. Connecting to verifier, please wait.....");

                            Intent intent = new Intent();
                            intent.setClass(mContext, SignalRService.class);
                            intent.putExtra("requestFrom", requestFrom);
                            intent.putExtra("roomId", roomId);
                            intent.putExtra("userName", getPANName());
                            intent.putExtra("OldRoomID", OldRoomID);
                            mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


                        }*/
                    } catch (Exception ex) {
                        customProgress.setVisibility(View.GONE);
                        ex.printStackTrace();
                        commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        rlVerifyProof.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    rlVerifyProof.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

            });
        } else {
            MOSLCommon.alertDInternet(uploadDetailContext);
        }

    }

    public class SignalRReceiver extends BroadcastReceiver {
        private boolean statusUpdate;

        @Override
        public void onReceive(Context context, Intent intent) {
           /* tvExpand.callOnClick();
            tvExpand.performClick();*/

            boolean isServerDisconnected = intent.getBooleanExtra("isServerDisconnected", false);
            boolean isServerConnected = intent.getBooleanExtra("isServerConnected", false);
            boolean serverTimeoutCall = intent.getBooleanExtra("serverTimeoutCall", false);

            if (isServerConnected) {
                customProgress.setVisibility(View.GONE);
                chatMessageSending(true);
                if (getAppType().equals(DIRECT) || getUserType().equalsIgnoreCase(IFAUSER)) {
                    if (isVideoVerificationRequired())
                        commonMsgDialog(mContext, "You are now connected with verifier - " + verifierName + ". Kindly continue with verification process through Video Chat.", false, "");
                    else
                        commonMsgDialog(mContext, "You are now connected with verifier - " + verifierName + ". Kindly continue with verification process through Chat.", false, "");
                } else {
                    commonMsgDialog(mContext, "You are now connected with verifier - " + verifierName + ". Kindly continue with verification process through Chat.", false, "");
                }
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(1122);
                chatMessage.setMessage(verifierName + " is connected with you, wait for a moment while we are processing your data");
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(false);
                if (getAppType().equals(DIRECT) || getUserType().equalsIgnoreCase(IFAUSER)) {
                    displayMessage(chatMessage, false);
                } else {
                    displayMessage(chatMessage, true);
                }
                tvChatHeader.setText("Chat");
                uploadDetailAdapter.setVerifierConnected(true);

                //disable other tabs
                if (isNomineeTabApplicable()) {
                    tabPageIndicator.disableTab(getPersonalTab());
                    tabPageIndicator.disableTab(getDematTab());
                    tabPageIndicator.disableTab(getBankTab());
                    tabPageIndicator.disableTab(getEsignTab());
                } else {
                    tabPageIndicator.disableTab(getPersonalTab());
//                    tabPageIndicator.disableTab(getDematTab());
                    tabPageIndicator.disableTab(getBankTab());
                    tabPageIndicator.disableTab(getEsignTab());
                }

                rlVerifyProof.setVisibility(View.GONE);
                llChat.setVisibility(View.VISIBLE);
                ivChat.setImageResource(R.drawable.ic_upload_plus);
                //chatAdapter.clear();
            } else if (isServerDisconnected) {
                unBindSignalrService();
                chatMessageSending(false);
                uploadDetailAdapter.setVerifierConnected(false);
                //enable tabs
                if (isNomineeTabApplicable()) {
                    tabPageIndicator.enableTab(getPersonalTab());
                    tabPageIndicator.enableTab(getDematTab());
                    tabPageIndicator.enableTab(getBankTab());
                } else {
                    tabPageIndicator.enableTab(getPersonalTab());
//                    tabPageIndicator.enableTab(getDematTab());
                    tabPageIndicator.enableTab(getBankTab());
                }

                llChat.setVisibility(View.GONE);
                rlVerifyProof.setVisibility(View.VISIBLE);
                MOSLCommon.showToastMsg(mContext, "Verifier disconnected");
                if (!statusUpdate) {
                    getVerificationStatus(true);
                }
            } else if (serverTimeoutCall) {
                long remainTime = intent.getLongExtra("remainTime", 0);
                boolean reset = intent.getBooleanExtra("reset", false);
                if (reset) {
                    tvChatHeader.setText("Chat");
                    if (uploadDetailAdapter.isVerifierConnected()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            imgConnect.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_green));
                        } else {
                            imgConnect.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_green));
                        }
                        tvConnectionStatus.setText("Connected");
                        chatMessageSending(true);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            imgConnect.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_gray));
                        } else {
                            imgConnect.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_gray));
                        }
                        tvConnectionStatus.setText("Disconnected");
                        chatMessageSending(false);
                    }
                } else {
                    tvChatHeader.setText("Waiting to reconnect: " + remainTime);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        imgConnect.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_gray));
                    } else {
                        imgConnect.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_gray));
                    }
                    tvConnectionStatus.setText("Disconnected");
                    chatMessageSending(false);
                }
            } else {
                String notificationID = intent.getStringExtra("notificationID");
                String docName = intent.getStringExtra("docName");
                String message = intent.getStringExtra("message");
                boolean isVerifiedMsg = intent.getBooleanExtra("isVerifiedMsg", false);

                boolean showChatMsg = intent.getBooleanExtra("showChatMsg", false);

                if (isVerifiedMsg || (docName != null && !docName.equals(""))) {
                    statusUpdate = true;

                    uploadDetailAdapter.updateVerifierStatus(docName, notificationID, message, isVerifiedMsg);
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        public void run() {
                            uploadDetailAdapter.notifyDataSetChanged();
                        }
                    });

                }

                if (showChatMsg) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setId(1122);
                    chatMessage.setMessage(message);
                    chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                    chatMessage.setMe(false);
                    displayMessage(chatMessage, true);
                }
            }

        }
    }

    private void chatMessageSending(boolean chatUsageFlag) {
        if (chatUsageFlag) {
            messageET.setEnabled(true);
            sendBtn.setClickable(true);
        } else {
            messageET.setEnabled(false);
            sendBtn.setClickable(false);
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to SignalRService, cast the IBinder and get SignalRService instance
            SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
            mService = binder.getService();
           /* if (mService != null && videoEnabled) {
//                mService.startCameraForMobileUser(rtcClientId);//sending rtcClientId to server
                mService.startCameraForMobileUser(consoleMsgUrl);//sending rtcClientId to server
            }*/

            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    public void onResume() {

        super.onResume();
    }


    public class UploadDetailAdapter extends BaseAdapter implements MOSLConstants {
        private Uri fileUri;
        private List<UploadDetaisModel> data;
        private final LayoutInflater inflater;
        Context mContext;
        private DisplayImageOptions options;
        private boolean verifierConnected;


        public UploadDetailAdapter(Context c, List<UploadDetaisModel> data) {
            inflater = LayoutInflater.from(c);
            mContext = c;
            this.data = data;

            options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();

        }

        public List<UploadDetaisModel> getData() {
            return data;
        }

        public String getAvailableAddnDoc() {
            for (UploadDetaisModel model : data) {
                if (model.isAdditionalFile() && model.isVisible() && isBlank(model.getFileUploadedPath())) {
                    return "ALREADY_EXISTS";
                } else if (model.isAdditionalFile() && !model.isVisible()) {
                    return model.getKey();
                }
            }
            return null;
        }

        public void reset(String docName) {
            for (UploadDetaisModel model : data) {
                if (model.getKey().equalsIgnoreCase(docName)) {
                    MemoryCacheUtils.removeFromCache(model.getFileUploadedPath(), ImageLoader.getInstance().getMemoryCache());
                    DiskCacheUtils.removeFromCache(model.getFileUploadedPath(), ImageLoader.getInstance().getDiscCache());


                    model.setPDFFilePath("");

                    model.setFileUploadedPath("");
                    model.setProofStatus(0);
                    model.setUploadStatus(0);
                    model.setVerifierRejectReason("");

                    if (model.getKey().startsWith("ADDITIONALPROOF")) {
                        model.setVisible(false);
                    }
                    break;
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
        }

        public void updateUploadStatus(String key, int status, String message, String uploadedFilePath) {
            for (UploadDetaisModel model : data) {
                if (model.getKey().equals(key)) {

                    //remove from cache if already exists
                    if (model.getFileUploadedPath() != null) {
                        MemoryCacheUtils.removeFromCache(model.getFileUploadedPath(), ImageLoader.getInstance().getMemoryCache());
                        DiskCacheUtils.removeFromCache(model.getFileUploadedPath(), ImageLoader.getInstance().getDiscCache());
                    }

                    model.setUploadStatus(status);

                    //reset approve status on success upload
                    if (model.getUploadStatus() == DOCUMENT_UPLOAD_STATUS_SUCCESS) {
                        model.setProofStatus(0);
                    }

                    model.setFileUploadedPath(uploadedFilePath);

                    if (!isBlank(message)) {
                        model.setVerifierRejectReason(message);
                    }

                    if (mService != null) {
                        mService.notifyNewUploadClient(model.getDisplayText());
                    }
                    break;
                }
            }
        }

        public void updateVerifierStatus(String key, String status, String message, boolean isVerifiedMsg) {
            boolean uploadDisableSection = false;

            if (isVerifiedMsg) {

                getVerificationStatus(true);
                llChat.setVisibility(View.GONE);
                //disconnect signalr
                unBindSignalrService();

            } else {
                if (status.equals(Integer.toString(DOCUMENT_VERIFIED_STATUS_APPROVED))) {
                    status = DOCUMENT_VERIFIED_STATUS_APPROVED + "";
                    uploadDisableSection = true;
                } else if (status.equals(Integer.toString(DOCUMENT_VERIFIED_STATUS_REJECTED))) {
                    status = DOCUMENT_VERIFIED_STATUS_REJECTED + "";
                    uploadDisableSection = false;
                }
                for (UploadDetaisModel model : data) {
                    if (model.getKey().equals(key)) {
                        model.setProofStatus(Integer.parseInt(status));

                        if (message != null) {
                            model.setVerifierRejectReason(message);
                        }
                        model.setDisableSection(uploadDisableSection);
                        if (Integer.parseInt(status) == 1) //rejected
                            model.seteCopy(false);
                        break;
                    }
                }
            }
        }

        public boolean isAllApproved() {
            for (UploadDetaisModel model : data) {
                if (model.isMandatory()) {
                    if (model.getProofStatus() != DOCUMENT_VERIFIED_STATUS_APPROVED) {
                        return false;
                    }
                } else if (model.getUploadStatus() == DOCUMENT_UPLOAD_STATUS_SUCCESS && model.getProofStatus() != DOCUMENT_VERIFIED_STATUS_APPROVED) {
                    return false;
                }
            }
            return true;
        }

        public int getUploadedCount() {
            int counter = 0;
            for (UploadDetaisModel model : data) {
                if (model.getProofStatus() == 1) {
                    counter++;
                }
            }
            return counter;
        }

        public boolean isMandatoryUploaded() {
            for (UploadDetaisModel model : data) {
                if (model.isMandatory() && model.getUploadStatus() != DOCUMENT_UPLOAD_STATUS_SUCCESS) {
                    return false;
                }
            }
            return true;
        }

        /*
        * check if additional doc section is added but no doc uploaded
         */
        public boolean isAdditionalUploaded() {
            for (UploadDetaisModel model : data) {
                if (model.isAdditionalFile() && model.isVisible() && model.getUploadStatus() != DOCUMENT_UPLOAD_STATUS_SUCCESS) {
                    return false;
                }
            }
            return true;
        }

        public UploadDetaisModel getItemByDocumentName(String documentName) {
            for (UploadDetaisModel model : data) {
                if (model.getKey().equals(documentName)) {
                    return model;
                }
            }
            return null;
        }

        public boolean isVerifierConnected() {
            return verifierConnected;
        }

        private void resetNewDocStatus() {
            for (UploadDetaisModel model : data) {
                model.setNewUploaded(false);
            }
        }

        public void setVerifierConnected(boolean verifierConnected) {
            this.verifierConnected = verifierConnected;

            if (verifierConnected) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imgConnect.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_green));
                } else {
                    imgConnect.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_green));
                }
                tvConnectionStatus.setText("Connected");
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imgConnect.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_gray));
                } else {
                    imgConnect.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_gray));
                }
                tvConnectionStatus.setText("Disconnected");
                resetNewDocStatus();
            }
            IPVMessageVisibility(verifierConnected);
            notifyDataSetChanged();
        }

        private void IPVMessageVisibility(boolean verifierConnected) {
            if (getAppType().equals(DIRECT) && !getIsCVLVerified() && !getIsAadharVerified()) {
                if (verifierConnected) {
                    llDirectUploadInstructions.setVisibility(View.GONE);
                    llPreFillAccOpen.setVisibility(View.GONE);
                    llIPVNote.setVisibility(View.GONE);
                    tvCourierMsg.setVisibility(View.GONE);
                } else {
                    llDirectUploadInstructions.setVisibility(View.VISIBLE);
                    llPreFillAccOpen.setVisibility(View.VISIBLE);
                    llIPVNote.setVisibility(View.VISIBLE);
                    tvCourierMsg.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;

            final UploadDetaisModel model = data.get(position);

            //hide item if optional additional file
            if (!model.isVisible()) {
                view = inflater.inflate(R.layout.list_blank_item, parent, false);
                view.setVisibility(View.GONE);
                model.setVisible(false);
                return view;
            } else {
                model.setVisible(true);
            }

            view = inflater.inflate(R.layout.item_list_upload_detail, parent, false);

            final Holder holder = new Holder();

            holder.rlDocument = (RelativeLayout) view.findViewById(R.id.rlDocument);
            holder.rlDocumentSub = (RelativeLayout) view.findViewById(R.id.rlDocumentSub);

            holder.tvDocument = (TextView) view.findViewById(R.id.tvDocument);
            //holder.imDelete = (ImageView) view.findViewById(R.id.imDelete); //will be used to delete uploaded file on server
            //  holder.ivDocumentStatus = (ImageView) view.findViewById(R.id.ivDocumentStatus);
            holder.imDownload = (ImageView) view.findViewById(R.id.imDownload);
            holder.imDeleteRow = (ImageView) view.findViewById(R.id.imDeleteRow); //will be used to delete additional doc row
            holder.ivDocument = (ImageView) view.findViewById(R.id.ivDocument);
            holder.tvDocumentMessage = (TextView) view.findViewById(R.id.tvDocumentMessage);

            holder.tvDocument.setText(model.getDisplayText());
            final int docStatus = model.getProofStatus();


            holder.tvDocumentMessage.setText(model.getDisplayNote());

            //enable delete only for additional doc
            //hide if approved
            //show if not connected
            //show if new uplaoded during connection
            if (model.getProofStatus() != DOCUMENT_VERIFIED_STATUS_APPROVED
                    && ((model.isAdditionalFile() && !isVerifierConnected()) || (model.isAdditionalFile() && model.isNewUploaded()))) {
                holder.imDeleteRow.setVisibility(View.VISIBLE);
            } else {
                holder.imDeleteRow.setVisibility(View.GONE);
            }

            if (model.getVerifierRejectReason() != null) {
                if (!model.getVerifierRejectReason().equals("")) {
                    holder.tvDocumentMessage.setText(model.getVerifierRejectReason());
                }
            }

            if (model.getUploadStatus() == DOCUMENT_UPLOAD_STATUS_SUCCESS) { //uploaded
                holder.ivDocument.setImageResource(R.drawable.ic_upload_confirm);
                holder.imDownload.setVisibility(View.VISIBLE);
                //holder.imDelete.setVisibility(View.VISIBLE);
            } else {
                holder.ivDocument.setImageResource(R.drawable.ic_upload_4);
                holder.imDownload.setVisibility(View.GONE);
                //holder.imDelete.setVisibility(View.GONE);
            }
            if (docStatus == DOCUMENT_VERIFIED_STATUS_REJECTED) { //rejected
                //holder.tvDocumentMessage.setVisibility(View.VISIBLE);
//                holder.tvDocumentMessage.setText(model.getVerifierRejectReason() + ", please re-upload the proof");
                holder.tvDocumentMessage.setText("View rejected message");
                holder.tvDocumentMessage.setTextColor(Color.parseColor("#e41f28"));
                holder.ivDocument.setImageResource(R.drawable.ic_upload_cancel); //red
            } else if (docStatus == DOCUMENT_VERIFIED_STATUS_APPROVED) { //approved
                holder.tvDocumentMessage.setTextColor(Color.parseColor("#23b14d"));
                holder.ivDocument.setImageResource(R.drawable.ic_upload_confirm); //green
                // holder.ivDocument.setImageResource(R.drawable.ic_upload_checked);
            }

            holder.imDeleteRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!MOSLCommon.isBlank(model.getFileUploadedPath())) {
                        /*if (isVerifierConnected()) {
                            commonMsgDialog(mContext, "Cannot delete while connected with verifier", false, "");
                        } else */

                        if (model.getProofStatus() == DOCUMENT_VERIFIED_STATUS_APPROVED) {
                            commonMsgDialog(mContext, "Cannot delete an approved document", false, "");
                        } else {
                            CustomDialog delDialog = new CustomDialog(mContext, "Are you sure you want to delete the uploaded document", true, true, true, "Delete Confirmation", YES, NO, new CustomDialog.CustomDialogListener() {
                                @Override
                                public void dialogAction(MOSLCommon.Action action, Object... data) {
                                    if (action == MOSLCommon.Action.OK) {
                                        deleteDocument(model.getKey());
                                    }
                                }
                            });
                            delDialog.show();
                        }
                    } else {
                        model.setVisible(false);
                        notifyDataSetChanged();
                    }
                    setListViewHeightBasedOnChildren(lvUploadList);


                }
            });

            holder.rlDocumentSub.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (!model.isDisableSection() && !isOffline() && !model.iseCopy()) {
                        currentSelectedDocForUpload = model.getKey();
                        selectImage();
                    }
                    setListViewHeightBasedOnChildren(lvUploadList);
                }
            });

            holder.tvDocumentMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (docStatus == DOCUMENT_VERIFIED_STATUS_REJECTED) {
                        commonMsgDialog(mContext, model.getVerifierRejectReason() + ", please re-upload the proof", false, "");
                    }
                }
            });

            holder.imDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.image_viewer_layout, null);
                    dialog.setView(dialogLayout);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface d) {
                            //model.setFileUploadedPath("http://www.mapsofindia.com/maps/india/india-flag.jpg");
                            ImageView image = (ImageView) dialog.findViewById(R.id.img);
                            final ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress);

                            getImageBitmap(image, progressBar, model.getFileUploadedPath());
                            /*ImageLoader.getInstance()
                                    .displayImage(model.getFileUploadedPath(), image, options, new SimpleImageLoadingListener() {
                                        @Override
                                        public void onLoadingStarted(String imageUri, View view) {
                                            progressBar.setProgress(0);
                                            progressBar.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                            progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }, new ImageLoadingProgressListener() {
                                        @Override
                                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                                            progressBar.setProgress(Math.round(100.0f * current / total));
                                        }
                                    });*/

                        }
                    });

                    dialog.show();
                }
            });
            return view;
        }

        private void getImageBitmap(final ImageView imageView, final ProgressBar progressBar, String path) {
            if (MOSLCommon.isNetworkEnabled(uploadDetailContext)) {
                Map inputMap = new HashMap<>();
                inputMap.put("File", EkycUtil.encrypt(path + "|" + getUserID() + "|" + getTokenKey() + "|" + ""));

                String flag = CONVERTIMAGETOSTRING;
                String category = PROOFUPLOADS;
                progressBar.setVisibility(View.VISIBLE);

                WebServiceHandler.post(uploadDetailContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                if (response.length() != 0) {
                                    String imageFile = (response.isNull("File") || response.getString("File").equals("")) ? "" : response.getString("File");

                                    byte[] decodedString = Base64.decode(imageFile, Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                    imageView.setImageBitmap(decodedByte);

                                } else {
                                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressBar.setVisibility(View.GONE);
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");

                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressBar.setVisibility(View.GONE);
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        progressBar.setVisibility(View.GONE);
                        commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                    }

                });
            } else {
                MOSLCommon.alertDInternet(uploadDetailContext);
            }
        }

        public class Holder {
            TextView tvDocumentMessage, tvDocument;
            ImageView ivDocumentStatus, ivDocument, imDownload, imDeleteRow;
            //ImageView imDelete;
            RelativeLayout rlDocument, rlDocumentSub;
        }
    }


    public void getVerificationStatus(final boolean showMessageDialog) {
        if (MOSLCommon.isNetworkEnabled(uploadDetailContext)) {
            uploadDetailAdapter.setVerifierConnected(false);

            //enable tabs
            if (isNomineeTabApplicable()) {
                tabPageIndicator.enableTab(getPersonalTab());
                tabPageIndicator.enableTab(getDematTab());
                tabPageIndicator.enableTab(getBankTab());
            } else {
                tabPageIndicator.enableTab(getPersonalTab());
//                tabPageIndicator.enableTab(getDematTab());
                tabPageIndicator.enableTab(getBankTab());
            }
            Map inputMap = new HashMap<>();
            inputMap.put("UserID", getUserID());

            String flag = GETFINALVERIFICATIONSTATUS;
            String category = PROOFUPLOADS;
            customProgress.setVisibility(View.VISIBLE);

            WebServiceHandler.post(uploadDetailContext, flag, category, inputMap, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    //after success code goes here
                    try {
                        if (statusCode == 200) {
                            if (response.length() != 0) {
                                String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                                String message = (response.isNull("_Message") || response.getString("_Message").equals("")) ? "" : response.getString("_Message");
                                String offlineIpvStatus = (response.isNull("_OFFLINEIPVSTATUS") || response.getString("_OFFLINEIPVSTATUS").equals("")) ? "" : response.getString("_OFFLINEIPVSTATUS");
                                if (!status.equals("")) {
                                    int intStatus = Integer.parseInt(status);

                                    //not to show messsage when status is 1
                               /* if(!isBlank(message)) {
                                    informationDialog(message);
                                }*/

                                    if (intStatus == 1) {
                                        rlVerifyProof.setVisibility(View.GONE);
                                        rlSubmit.setVisibility(View.VISIBLE);
                                        llAddNew.setVisibility(View.GONE);
                                        chatAdapter.clear();
                                        setIsVerificationDone(true);
                                        if (getIsEquitySelected()) {
                                            setIsEquityIpvDone(true);
                                        } else {
                                            setIsEquityIpvDone(false);
                                        }
                                        if (getInterestedInMF()) {
                                            setIsMfIpvDone(true);
                                            disableEnableField();
                                        } else {
                                            setIsMfIpvDone(false);
                                        }
                                        if (!isBlank(message)) {
                                            if (showMessageDialog) {
                                                informationDialog(message);
                                            }
                                        }
                                    } else {
                                        if (isOffline() && offlineIpvStatus.equalsIgnoreCase("1"))
                                            setIsOffline(false);
                                        if (showMessageDialog) {
                                            informationDialog(message);
                                        }
                                        showSubmitButton();
//                                        rlVerifyProof.setVisibility(View.VISIBLE);
//                                        rlSubmit.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                    MOSLCommon.alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                    }
                    if (!getInterestedInMF())
                        customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");

                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    MOSLCommon.showToastMsg(mContext, responseString);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

            });
        } else {
            MOSLCommon.alertDInternet(uploadDetailContext);
        }
    }

    public void deleteDocument(final String documentName) {
        if (MOSLCommon.isNetworkEnabled(uploadDetailContext)) {
            Map inputMap = new HashMap<>();
            inputMap.put("UserID", getUserID());
            inputMap.put("AadharNo", getAadharNo());
            inputMap.put("calltype", documentName);
            inputMap.put("extension", ".jpg");
            inputMap.put(APPLICATION_TYPE, getAppType());
            inputMap.put("IP", getDeviceID(mContext, mContext.getContentResolver()));

            String flag = DELADDITIONALPROOF;
            String category = PROOFUPLOADS;
            customProgress.setVisibility(View.VISIBLE);

            WebServiceHandler.post(uploadDetailContext, flag, category, inputMap, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    //after success code goes here
                    try {
                        if (statusCode == 200) {

                            if (response.length() != 0) {

                                String status = (response.isNull("_Status") || response.getString("_Status").equals("")) ? "" : response.getString("_Status");
                                String message = (response.isNull("_Message") || response.getString("_Message").equals("")) ? "" : response.getString("_Message");
                                if (!status.equals("")) {
                                    int intStatus = Integer.parseInt(status);

                                    informationDialog(message);
                                    if (intStatus == 1) {
                                        //loadUploadDetails();
                                        //clear data in adapter
                                        uploadDetailAdapter.reset(documentName);

                                        //notify verifier for deleted doc
                                        if (uploadDetailAdapter.isVerifierConnected() && mService != null) {
                                            mService.notifyDocDeleted(documentName);
                                        }
                                    }
                                    customProgress.setVisibility(View.GONE);
                                }


                            } else {
                                customProgress.setVisibility(View.GONE);
                                commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        commonMsgDialog(mContext, SOME_ERROR_OCCURED_PLEASE_RETRY, false, "");
//                    MOSLCommon.alertDialog(mContext, 0, "Oops", SOMETHING_WENT_WRONG_PLEASE_TRY_AGAIN, "OK", true, null);
                    }
                    // MOSLCommon.showToastMsg(BranchLoginActivity.this, "Status Code :" + statusCode);
                    customProgress.setVisibility(View.GONE);
                    //  onLoginSuccess();
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");

                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    MOSLCommon.showToastMsg(mContext, responseString);
                    customProgress.setVisibility(View.GONE);
                    commonMsgDialog(getActivity(), SOME_SERVER_ERROR_OCCURRED, false, "");
                }

            });
        } else {
            MOSLCommon.alertDInternet(uploadDetailContext);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && uploadDetailView != null && viewPager.getCurrentItem() == getUploadTab()) {
                loadUploadDetails();
                setPermisson(getActivity(), uploadDetailContext);
                try {
                    if (signalRReceiver == null && mContext != null) {
                        signalRReceiver = new SignalRReceiver();
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction(SignalRService.MY_ACTION);
                        mContext.registerReceiver(signalRReceiver, intentFilter);
                        Log.d(TAG, "SignalR service receiver registered");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
    }

    /*  public void showVideoSection() {
          if (uploadDetailView == null) {
              return;
          } else if (videoLayout == null) {
              videoLayout = (LinearLayout) uploadDetailView.findViewById(R.id.videoLayout);
          }

          if (videoLayout == null) {
              return;
          }

          if (getAppType().equals(DIRECT)) {
              videoEnabled = true;
              videoLayout.setVisibility(View.VISIBLE);
          } else {
              videoLayout.setVisibility(View.GONE);
          }

      }
  */
    private void selectImage() {
        final Dialog dialog = new Dialog(uploadDetailContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.setContentView(R.layout.layout_dialog_select_image);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView txtTakePhoto = (TextView) dialog.findViewById(R.id.txtTakePhoto);
        LinearLayout llTakePhoto = (LinearLayout) dialog.findViewById(R.id.llTakePhoto);
        TextView txtGallery = (TextView) dialog.findViewById(R.id.txtGallery);
        LinearLayout llGallery = (LinearLayout) dialog.findViewById(R.id.llGallery);
        ImageView llCancel = (ImageView) dialog.findViewById(R.id.imgClose);
        LinearLayout llSignature = (LinearLayout) dialog.findViewById(R.id.llSignature);

        if (currentSelectedDocForUpload.equalsIgnoreCase("PANSIGN")) {
            llSignature.setVisibility(View.VISIBLE);
        } else {
            llSignature.setVisibility(View.GONE);
        }
        llTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                userChoosenTask = TAKE_PHOTO;
                cameraIntent();
            }
        });

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                userChoosenTask = CHOOSE_FROM_GALLERY;
                galleryIntent();
            }
        });

        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        llSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog_digital_signature();

            }
        });

        dialog.show();
    }

   /* private void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result= Utility.checkPermission(mContext);

                if (items[item].equals(TAKE_PHOTO)) {


                } else if (items[item].equals(CHOOSE_FROM_GALLERY)) {
                    userChoosenTask = CHOOSE_FROM_GALLERY;

                    galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }*/

    Uri imageToUploadUri;

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "POST_IMAGE";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void cameraIntent() {
        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(intent, REQUEST_CAMERA);
        imageToUploadUri = null;
//        String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/POST_IMAGE.jpg";
//        File imageFile = new File(imageFilePath);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", createImageFile());
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
        /*Intent chooserIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
        chooserIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        imageToUploadUri = Uri.fromFile(f);
        startActivityForResult(chooserIntent, REQUEST_CAMERA);*/
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {


                if (!isFileSizeQulified(data)) {
                    commonMsgDialog(getActivity(), "Cannot upload file of size more than 2 MB", false, "");
                } else {
                    bm = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), data.getData());
//                bm = readBitmap(data.getData());
                    uploadFile(bm, currentSelectedDocForUpload, SELECT_FILE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isFileSizeQulified(Intent data) {
        try {
            String filePath = MOSLCommon.getRealPathFromURI(getActivity(), data.getData());
            File f = new File(filePath);
            long fileSizeInBytes = f.length();
            long fileSizeInKB = fileSizeInBytes / 1024;
            long fileSizeInMB = fileSizeInKB / 1024;
            if (fileSizeInMB > 2) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    // Read bitmap
    public Bitmap readBitmap(Uri selectedImage) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = mContext.getContentResolver().openAssetFileDescriptor(selectedImage, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }
    // Clear bitmap

    public static void clearBitmap(Bitmap bm) {
        bm.recycle();
        System.gc();
    }

    private void onCaptureImageResult(Bitmap bitmap) {
        //Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        uploadFile(bitmap, currentSelectedDocForUpload, REQUEST_CAMERA);
    }


    private void unBindSignalrService() {
        try {
            if (mBound) {
                mContext.unbindService(mConnection);
                mBound = false;
                Log.d(TAG, "Signalr Service unbinded");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (uploadDetailContext != null && signalRReceiver != null) {
                uploadDetailContext.unregisterReceiver(signalRReceiver);
            }

            if (mService != null) {
                mService.disconnectConnection();
                mService.stopSelf();
                unBindSignalrService();
            }

            Log.d(TAG, "SignalR service stopped");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setProofUploadOptionMF() {
        if (MOSLCommon.isNetworkEnabled(uploadDetailContext)) {
//            customProgress.setVisibility(View.VISIBLE);
            final Map inputMap = new HashMap<>();
            inputMap.put("_userId", getUserID());

            String flag = PROOFUPLOADOPTION_MF_CANUPLD;
            String category = PROOFUPLOADS;

            WebServiceHandler.post(uploadDetailContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            if (response.length() == 0) {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        customProgress.setVisibility(View.GONE);
                    } else {
//                        MOSLCommon.showToastMsg(uploadDetailContext, NO_RESPONSE);
//                        customProgress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
//                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
//                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
//                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("Failure", "no resp in Data");
//                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
//                    customProgress.setVisibility(View.GONE);
                }
            });
        } else {
//            MOSLCommon.alertDInternet(uploadDetailContext);
//            customProgress.setVisibility(View.GONE);
        }
    }

    public void getProofSubmitDirectBa() {
        if (MOSLCommon.isNetworkEnabled(uploadDetailContext)) {
            customProgress.setVisibility(View.VISIBLE);
            final Map inputMap = new HashMap<>();
            inputMap.put("UserID", getUserID());
            inputMap.put("_BACODE", getBaFranchiseeCode());
            inputMap.put("_IsDirectBA", isDirectProspect() ? "1" : "0");

            String flag = GETPROOFSUBMITDIRECTBA;
            String category = PROOFUPLOADS;

            WebServiceHandler.post(uploadDetailContext, flag, category, inputMap, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (response != null) {
                        try {
                            if (response.has("DIRECTBADETAILS")) {
                                JSONArray jsonArray = response.getJSONArray("DIRECTBADETAILS");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        String status = (object.isNull("STATUS") || object.getString("STATUS").equals("")) ? "" : object.getString("STATUS");
                                        String message = (object.isNull("MESSAGE") || object.getString("MESSAGE").equals("")) ? "" : object.getString("MESSAGE");
                                        commonMsgDialog(mContext, message, false, "");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        customProgress.setVisibility(View.GONE);
                    } else {
                        MOSLCommon.showToastMsg(uploadDetailContext, NO_RESPONSE);
                        customProgress.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    super.onSuccess(statusCode, headers, responseString);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("Failure", "no resp in Data");
                    customProgress.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    customProgress.setVisibility(View.GONE);
                }
            });
        } else {
            MOSLCommon.alertDInternet(uploadDetailContext);
            customProgress.setVisibility(View.GONE);
        }
    }

    public void openWebUrl(Uri url, String fileName) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            String packag = CustomTabsHelper.getPackageNameToUse(getContext());
//            if (packag == null) {
//                //   openWebview(null, url);
        Intent intent = new Intent(mContext, WebActivityPDFDownload.class);
        intent.putExtra(URL_PATH, url.toString());
        intent.putExtra(FILE_NAME, fileName);
        startActivity(intent);
//            } else {
//                openCustomTabsIntent(url.toString());
//            }
//        } else {
//            openInBrowser(url);
//        }
    }

    private void openCustomTabsIntent(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(getResources().getColor(R.color.secondaryBackground));
        builder.setCloseButtonIcon(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_back_arrow));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
    }

    private CustomTabsSession getSession() {
        return mClient.newSession(new CustomTabsCallback() {
            @Override
            public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
                //   getPaymentStatus();
            }
        });
    }

    public void openInBrowser(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void disableEnableField() {
        if (isMfIpvDone()) {
            llPreFillAccOpen.setEnabled(false);
        } else {
            llPreFillAccOpen.setEnabled(true);
        }
    }

    //Digital Signiture
    public void dialog_digital_signature() {

        if (dialog != null) {
            dialog = new Dialog(mContext);
            // Removing the features of Normal Dialogs
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_signature);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
        }

        // Creating Separate Directory for saving Generated Images
        String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
        String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        final String StoredPath = DIRECTORY + pic_name + ".jpg";

        // Method to create Directory, if the Directory doesn't exists
        File file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }

        signature = (DigitalSignature) dialog.findViewById(R.id.linearLayout);
        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        ivClose = (ImageView) dialog.findViewById(R.id.ivClose);

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Signatured Cleared");
                signature.clear();
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetSign.setEnabled(true);
                Log.v("log_tag", "Signatured Saved");
                save(signature, StoredPath);
                Uri imageUri = Uri.parse(StoredPath);
                if (!isBlank(StoredPath)) {
                    actualImage = new File(imageUri.getPath());
                    compressImage();
                }
                dialog.dismiss();
            }
        });

        signature.addListener(new DigitalSignature.InkListener() {
            @Override
            public void onInkClear() {
                mGetSign.setEnabled(false);
            }

            @Override
            public void onInkDraw() {
                mGetSign.setEnabled(true);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void save(View v, String StoredPath) {
        Log.v("log_tag", "Width: " + v.getWidth());
        Log.v("log_tag", "Height: " + v.getHeight());
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        try {
            // Output the file
            FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
            v.draw(canvas);

            // Convert the output file to Image such as .png
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, mFileOutStream);
            mFileOutStream.flush();
            mFileOutStream.close();

        } catch (Exception e) {
            Log.v("log_tag", e.toString());
        }
    }
}



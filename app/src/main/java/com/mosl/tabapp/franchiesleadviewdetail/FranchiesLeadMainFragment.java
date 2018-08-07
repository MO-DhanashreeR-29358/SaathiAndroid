package com.mosl.tabapp.franchiesleadviewdetail;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.Adapter.FranchiesLeadAdapter;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CustomViewPager;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mosl.tabapp.leadViewDetail.SetAppointmentFragment.TAG;

import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Vijay on 15/03/2017.
 */

public class FranchiesLeadMainFragment extends MOSLCommonFragment implements View.OnClickListener {

    private View franchiesLeadMainView;
    private CustomViewPager vpFranchiesLead;
    private TabPageIndicator indicatorFranchiesLead;
    private LinearLayout mTabsLinearLayoutTarget;
    private FranchiesLeadDetailFragment franchiesLeadDetailFragment;
    private FranchiesLeadActivitiesFragment franchiesLeadActivitieFragment;
    private FranchiesLeadProfilingFragment franchiesLeadProfilingFragment;
    private ArrayList<Fragment> pageListFranchiesLead = new ArrayList<>();
    private ImageView ivLead, ivLeadViewArrow;
    private TextView tvClientCode, tvLeadId, tvBranchCode, tvCurrentStage, tvCurrentStageSince;
    private String leadId;
    private String mobileNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        franchiesLeadMainView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_franchies_lead_main);
        try {
            leadId = this.getArguments().getString(LEAD_ID);
            mobileNo = this.getArguments().getString(MOBILE);
            setAppTitle(getClass().toString(), this.getArguments().getString(NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setUserVisibleHint(true);
        setUpView(franchiesLeadMainView);
        return franchiesLeadMainView;
    }

    private void setUpView(View franchiesLeadMainView) {
        vpFranchiesLead = (CustomViewPager) franchiesLeadMainView.findViewById(R.id.vpFranchiesLead);
        indicatorFranchiesLead = (TabPageIndicator) franchiesLeadMainView.findViewById(R.id.indicatorFranchiesLead);

        ivLead = (ImageView) franchiesLeadMainView.findViewById(R.id.ivLead);
        ivLeadViewArrow = (ImageView) franchiesLeadMainView.findViewById(R.id.ivLeadViewArrow);
        tvClientCode = (TextView) franchiesLeadMainView.findViewById(R.id.tvClientCode);
        tvLeadId = (TextView) franchiesLeadMainView.findViewById(R.id.tvLeadId);
        tvBranchCode = (TextView) franchiesLeadMainView.findViewById(R.id.tvBranchCode);
        tvCurrentStage = (TextView) franchiesLeadMainView.findViewById(R.id.tvCurrentStage);
        tvCurrentStageSince = (TextView) franchiesLeadMainView.findViewById(R.id.tvCurrentStageSince);

        ivLeadViewArrow.setOnClickListener(this);


        Glide.with(context).load("https://login.motilaloswal.com/RelationshipManager/WhatsNewUpload/20-04-201-5101514_Screenshot_1.jpg").asBitmap().placeholder(R.drawable.ic_personal_select).centerCrop().into(new BitmapImageViewTarget(ivLead) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivLead.setImageDrawable(circularBitmapDrawable);
            }
        });

        String[] headingClient = new String[3];
        headingClient[0] = DETAILS;
        headingClient[1] = ACTIVITIES;
        headingClient[2] = PROFILING;

        franchiesLeadDetailFragment = FranchiesLeadDetailFragment.newInstance(0, DETAILS, this.getArguments().getString(LEAD_ID));
        franchiesLeadActivitieFragment = FranchiesLeadActivitiesFragment.newInstance(1, ACTIVITIES, this.getArguments().getString(LEAD_ID));
        franchiesLeadProfilingFragment = FranchiesLeadProfilingFragment.newInstance(2, PROFILING, this.getArguments().getString(LEAD_ID));
        franchiesLeadDetailFragment.setViewPager(vpFranchiesLead);
        franchiesLeadActivitieFragment.setViewPager(vpFranchiesLead);
        franchiesLeadProfilingFragment.setViewPager(vpFranchiesLead);
        pageListFranchiesLead.add(franchiesLeadDetailFragment);
        pageListFranchiesLead.add(franchiesLeadActivitieFragment);
        pageListFranchiesLead.add(franchiesLeadProfilingFragment);

        final FranchiesLeadAdapter adapterClient = new FranchiesLeadAdapter(getChildFragmentManager(), pageListFranchiesLead, headingClient);
        vpFranchiesLead.setAdapter(adapterClient);
        indicatorFranchiesLead.setViewPager(vpFranchiesLead);
        mTabsLinearLayoutTarget = ((LinearLayout) indicatorFranchiesLead.getChildAt(0));
        vpFranchiesLead.setCurrentItem(0);

        indicatorFranchiesLead.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && franchiesLeadMainView != null) {
                getLeadByIdFromServer(leadId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void getLeadByIdFromServer(CharSequence strLeadId) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {

            String serviceMethod = "Lead/GetLeadView";
            String tmpReq = String.format(ID_OBJECT, strLeadId);

            showProgress();

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), tmpReq, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        //    arrInterested = new ArrayList<>();
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
//                                hideProgress();
                                if (status.equalsIgnoreCase("true")) {
                                    JSONObject Response_obj = response.getJSONObject(RESPONSE);
                                    String leadID = (Response_obj.isNull(LEADID) || Response_obj.getString(LEADID).equals("")) ? "" : Response_obj.getString(LEADID);
                                    String branchcode = (Response_obj.isNull(BRANCHCODE) || Response_obj.getString(BRANCHCODE).equals("")) ? "" : Response_obj.getString(BRANCHCODE);
                                    JSONObject contact_obj = Response_obj.getJSONObject(CONTACT);
                                    String Name = (contact_obj.isNull(NAME) || contact_obj.getString(NAME).equals("")) ? "" : contact_obj.getString(NAME);
                                    JSONArray leadProductMapping = Response_obj.getJSONArray(LEADPRODUCTMAPPING);
                                    for (int leadProductMapping_i = 0; leadProductMapping_i < leadProductMapping.length(); leadProductMapping_i++) {
                                        JSONObject leadProductMappingObj = leadProductMapping.getJSONObject(leadProductMapping_i);
                                        String clientCode = (leadProductMappingObj.isNull(CLIENTCODE) || leadProductMappingObj.getString(CLIENTCODE).equals("")) ? "" : leadProductMappingObj.getString(CLIENTCODE);
                                        String StageName = (leadProductMappingObj.isNull(STAGENAME) || leadProductMappingObj.getString(STAGENAME).equals("")) ? "" : leadProductMappingObj.getString(STAGENAME);
                                        String isDefault = (leadProductMappingObj.isNull(IS_DEFAULT) || leadProductMappingObj.getString(IS_DEFAULT).equals("")) ? "" : leadProductMappingObj.getString(IS_DEFAULT);
                                        String pendingDays = (leadProductMappingObj.isNull(PENDING_DAYS) || leadProductMappingObj.getString(PENDING_DAYS).equals("")) ? "" : leadProductMappingObj.getString(PENDING_DAYS);
                                        if (isDefault.equalsIgnoreCase("true")) {
                                            tvClientCode.setText(clientCode);
                                            tvCurrentStage.setText(StageName);
                                            tvCurrentStageSince.setText(pendingDays + "days");
                                        }

                                    }
                                    //  tvName.setText(Name);
                                    tvLeadId.setText(leadID);
                                    tvBranchCode.setText(branchcode);
                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }
                            } else {
                                hideProgress();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            //eat it
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        hideProgress();
                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void leadViewDialog() {
        final Dialog dialog = new Dialog(getMainActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_leadview);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ImageView ivCreateMeeting = (ImageView) dialog.findViewById(R.id.ivCreateMeeting);
        ImageView ivSetAppointment = (ImageView) dialog.findViewById(R.id.ivSetAppointment);
        ImageView ivCall = (ImageView) dialog.findViewById(R.id.ivCall);
        ImageView ivSms = (ImageView) dialog.findViewById(R.id.ivSms);

        ivCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(LEAD_ID, getArguments().getString(LEAD_ID));
                navigateTo(NAV_TO_CREATE_MEETING, bundle, getFragmentManager(), false);
                dialog.dismiss();
            }
        });
        ivSetAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(LEAD_ID, getArguments().getString(LEAD_ID));
                navigateTo(NAV_TO_SET_APPOINTMENT, bundle, getFragmentManager(), false);
                dialog.dismiss();
            }
        });
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), "Do you want to call?", "Yes", "No", true, new MSFDialog.DialogListener() {
                    @Override
                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                        if (action == MSFDialog.Action.OK) {
                            if (mobileNo.equalsIgnoreCase("")) {
                                Toast.makeText(getMainActivity(), "Contact number not available", Toast.LENGTH_LONG).show();
                            } else {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + mobileNo));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    Toast.makeText(getMainActivity(), "No permission for call", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                getMainActivity().startActivity(callIntent);
                            }
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        ivSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(mobileNo.equals(""))) {
                    Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                    smsIntent.setData(Uri.parse("smsto:" + mobileNo));
                    smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getMainActivity().startActivity(smsIntent);
                    //                            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    //                            smsIntent.setType("vnd.android-dir/mms-sms");
                    //                            smsIntent.putExtra("address",model.getContactNo() );
                    //                            smsIntent.putExtra("sms_body", model.getTitle().equalsIgnoreCase("birthday") ? MOSLConstants.BIRTHDAY_MESSAGE : MOSLConstants.ANNIVERSARY_MESSAGE);
                    //                            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //                            getMainActivity().startActivity(smsIntent);
                } else {
                    Toast.makeText(getMainActivity(), "Contact number not available", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivLeadViewArrow:
                leadViewDialog();
                break;
        }
    }
}

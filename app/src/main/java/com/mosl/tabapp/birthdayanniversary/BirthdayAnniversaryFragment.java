package com.mosl.tabapp.birthdayanniversary;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.app.core.data.UiModel;
import com.mosl.saathi.api.model.lead.LookupItem;
import com.mosl.saathi.api.model.lead.LookupRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.BirthdayAnniversaryModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.mosl.app.core.data.UiModelHelper.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Vijay on 08/03/2017.
 */

public class BirthdayAnniversaryFragment extends MOSLCommonFragment
    implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    public static String BELATED_FLAG = "belated";
    public static String TODAY_FLAG = "today";
    public static String UPCOMING_FLAG = "upcoming";
    //    public static String UPCOMING_FLAG = "1";
    //    public static String UPCOMING_FLAG = "2";
    //    public static String UPCOMING_FLAG = "3";

    private View birthdayAnniversaryView;
    private RecyclerView rvBirthdayAnniversary;
    private Spinner spProfile, spEventType;
    private ArrayList<BirthdayAnniversaryModel> birthdayAnniversaryList;
    private BirthdayAnniversaryAdapter birthdayAnniversaryAdapter;
    private ArrayList<RowData> arrProfile;
    private ArrayList<String> arrEventType;
    private ArrayAdapter<RowData> arrAdapProfile;
    private ArrayAdapter<String> arrAdapEventType;
    private String profileCodeStr, profileNameStr = "";
    private TextView tvNoData;
    private ImageView ivEmailAll;
    boolean dataFoundFlag;
    private SegmentedGroup segmentedGroup;
    private String filterType = "2", dayType = TODAY_FLAG;

    private AdapterView.OnItemSelectedListener profileInListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    profileCodeStr = ((RowData) spProfile.getSelectedItem()).getKey();
                    profileNameStr = ((RowData) spProfile.getSelectedItem()).getValue();
                    getData();
                /* if (profileCodeStr.equalsIgnoreCase("-1")) {
                    getData("", spEventType.getSelectedItem().toString().equalsIgnoreCase("Birthday") ? "B" : "A");
                } else {
                    getData(profileNameStr, spEventType.getSelectedItem().toString().equalsIgnoreCase("Birthday") ? "B" : "A");
                }*/
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    private AdapterView.OnItemSelectedListener eventTypeInListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (profileCodeStr != null) {
                        getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        birthdayAnniversaryView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_birthday_anniversary);
        setUpView(birthdayAnniversaryView);
        setAppTitle(getClass().toString(), "Birthday & Anniversary");
        setUserVisibleHint(true);
        return birthdayAnniversaryView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && birthdayAnniversaryView != null) {
                getProfile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        /*    Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            Crashlytics.logException(e);*/
        }
    }

    private void setUpView(View birthdayAnniversaryView) {
        spProfile = (Spinner) birthdayAnniversaryView.findViewById(R.id.spProfile);
        spEventType = (Spinner) birthdayAnniversaryView.findViewById(R.id.spEventType);
        segmentedGroup = (SegmentedGroup) birthdayAnniversaryView.findViewById(R.id.segmentedGroup);
        spEventType.setOnItemSelectedListener(eventTypeInListener);
        rvBirthdayAnniversary =
            (RecyclerView) birthdayAnniversaryView.findViewById(R.id.rvBirthdayAnniversary);
        tvNoData = (TextView) birthdayAnniversaryView.findViewById(R.id.tvNoData);
        ivEmailAll = (ImageView) birthdayAnniversaryView.findViewById(R.id.ivEmailAll);
        String[] eventTypeArray =
            getActivity().getResources().getStringArray(R.array.BirthdayAniversary);
        arrEventType = new ArrayList<String>(Arrays.asList(eventTypeArray));
        arrAdapEventType =
            new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrEventType);
        arrAdapEventType.setDropDownViewResource(R.layout.mdropdown);
        spEventType.setAdapter(arrAdapEventType);

        ivEmailAll.setOnClickListener(this);
        segmentedGroup.setOnCheckedChangeListener(this);
        //        OverScrollDecoratorHelper.setUpOverScroll(rvBirthdayAnniversary, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    private void renderAllLookUp(UiModel<List<LookupItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrProfile = new ArrayList<>();
            arrProfile.add(new RowData(0, "-1", "All"));

            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                String lookUpId = stringOrEmpty(item.lookUpid);
                String lookUpName = stringOrEmpty(item.lookUpName);
                arrProfile.add(new RowData(i + 1, lookUpId, lookUpName));
            }
            arrAdapProfile =
                new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrProfile);
            arrAdapProfile.setDropDownViewResource(R.layout.mdropdown);
            spProfile.setAdapter(arrAdapProfile);
            spProfile.setOnItemSelectedListener(profileInListener);
        }
    }

    private void getProfile() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            LookupRequest request =
                new LookupRequest(AccountDetails.getBusinessUnitId(), LEADPROFILE);
            Disposable subscribe = saathiApi.postAllLookup(request)
                .compose(applyUiModel())
                .subscribe(this::renderAllLookUp);
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    private void getData() {
        final String profileId;
        birthdayAnniversaryList = new ArrayList<>();
        birthdayAnniversaryAdapter = new BirthdayAnniversaryAdapter(birthdayAnniversaryList);
        rvBirthdayAnniversary.setLayoutManager(new GridLayoutManager(getMainActivity(), 3));
        rvBirthdayAnniversary.setAdapter(birthdayAnniversaryAdapter);
        birthdayAnniversaryAdapter.notifyDataSetChanged();
        if (profileCodeStr.equalsIgnoreCase("-1")) {
            profileId = "";
        } else {
            profileId = profileNameStr;
        }
        final String eventType =
            spEventType.getSelectedItem().toString().equalsIgnoreCase("Birthday") ? "B" : "A";
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("daytype", filterType);
            inputMap.put("Occasion", eventType);
            inputMap.put("PageSize", REPORT_PAGE_SIZE);
            inputMap.put("Page", "1");
            inputMap.put("CalculatedPage", "0");
            inputMap.put("TotalCount", "0");
            String serviceMethod = "Client/GetBirthdayAnniversary";

            try {
                CRMWebServiceHandler.postWithRequestAndFilterObjectBirthdayAndAniversary(
                    getMainActivity(), serviceMethod, getHeader(), inputMap, profileId,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //after success code goes here
                            try {
                                if (statusCode == 200) {
                                    hideProgress();
                                    String status = response.getString(STATUS);
                                    String message = response.getString(MESSAGE);
                                    birthdayAnniversaryList = new ArrayList<>();
                                    if (status.equalsIgnoreCase("true")) {
                                        JSONObject jsonObject = response.getJSONObject("Response");
                                        if (jsonObject.has("Data")) {
                                            JSONArray jsonArray = jsonObject.getJSONArray("Data");
                                            if (jsonArray.length() != 0) {
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject objDetail =
                                                        jsonArray.getJSONObject(i);
                                                    String clientCode =
                                                        (objDetail.isNull("ClientCode") || objDetail
                                                            .getString("ClientCode")
                                                            .equals("")) ? "0"
                                                            : objDetail.getString("ClientCode")
                                                                .trim();
                                                    String clientType =
                                                        (objDetail.isNull("ClientType") || objDetail
                                                            .getString("ClientType")
                                                            .equals("")) ? ""
                                                            : objDetail.getString("ClientType");
                                                    String ucid = (objDetail.isNull("UCID")
                                                        || objDetail.getString("UCID").equals(""))
                                                        ? "0" : objDetail.getString("UCID");
                                                    String clientName =
                                                        (objDetail.isNull("ClientName") || objDetail
                                                            .getString("ClientName")
                                                            .equals("")) ? ""
                                                            : objDetail.getString("ClientName");
                                                    String dob = (objDetail.isNull("DOB")
                                                        || objDetail.getString("DOB").equals(""))
                                                        ? "" : objDetail.getString("DOB");
                                                    String aniversaryDate =
                                                        (objDetail.isNull("AniversaryDate")
                                                            || objDetail.getString("AniversaryDate")
                                                            .equals("")) ? ""
                                                            : objDetail.getString("AniversaryDate");
                                                    String noOfYearBirthday =
                                                        (objDetail.isNull("NoOfYearBirthday")
                                                            || objDetail.getString(
                                                            "NoOfYearBirthday").equals("")) ? ""
                                                            : objDetail.getString(
                                                                "NoOfYearBirthday");
                                                    String mobile =
                                                        (objDetail.isNull("BackOfficeMobileNumber")
                                                            || objDetail.getString(
                                                            "BackOfficeMobileNumber").equals(""))
                                                            ? "" : objDetail.getString(
                                                            "BackOfficeMobileNumber");
                                                    String email = (objDetail.isNull("Email")
                                                        || objDetail.getString("Email").equals(""))
                                                        ? "" : objDetail.getString("Email");
                                                    String birthdayCount =
                                                        (objDetail.isNull("BirthdayCount")
                                                            || objDetail.getString("BirthdayCount")
                                                            .equals("")) ? ""
                                                            : objDetail.getString("BirthdayCount");
                                                    String aniversaryCount =
                                                        (objDetail.isNull("AniversaryCount")
                                                            || objDetail.getString(
                                                            "AniversaryCount").equals("")) ? ""
                                                            : objDetail.getString(
                                                                "AniversaryCount");
                                                    String clientPotential =
                                                        (objDetail.isNull("ClientPotential")
                                                            || objDetail.getString(
                                                            "ClientPotential").equals("")) ? ""
                                                            : objDetail.getString(
                                                                "ClientPotential");
                                                    //  String eventType = (objDetail.isNull("EventType") || objDetail.getString("EventType").equals("")) ? "" : objDetail.getString("EventType");
                                                    boolean birthdayFlag =
                                                        objDetail.getBoolean("Birthday");
                                                    boolean anniversoryFlag =
                                                        objDetail.getBoolean("Anniversory");

                                                    birthdayAnniversaryList.add(
                                                        new BirthdayAnniversaryModel(clientName,
                                                            clientCode, ucid,
                                                            eventType.equals("B") ? dob
                                                                : aniversaryDate, email, mobile,
                                                            eventType.equals("B") ? noOfYearBirthday
                                                                : aniversaryCount,
                                                            eventType.equals("B") ? "Birthday"
                                                                : "Anniversary",
                                                            eventType.equals("B") ? birthdayFlag
                                                                : anniversoryFlag, eventType));
                                                }
                                                tvNoData.setVisibility(View.GONE);
                                                birthdayAnniversaryAdapter =
                                                    new BirthdayAnniversaryAdapter(
                                                        birthdayAnniversaryList);
                                                rvBirthdayAnniversary.setLayoutManager(
                                                    new GridLayoutManager(getMainActivity(), 3));
                                                rvBirthdayAnniversary.setAdapter(
                                                    birthdayAnniversaryAdapter);
                                                birthdayAnniversaryAdapter.notifyDataSetChanged();
                                                dataFoundFlag = true;
                                            } else {
                                                birthdayAnniversaryAdapter =
                                                    new BirthdayAnniversaryAdapter(
                                                        birthdayAnniversaryList);
                                                rvBirthdayAnniversary.setLayoutManager(
                                                    new GridLayoutManager(getMainActivity(), 3));
                                                rvBirthdayAnniversary.setAdapter(
                                                    birthdayAnniversaryAdapter);
                                                birthdayAnniversaryAdapter.notifyDataSetChanged();
                                                dataFoundFlag = false;
                                                hideProgress();
                                                tvNoData.setVisibility(View.VISIBLE);
                                                if (eventType.equalsIgnoreCase("B")) {
                                                    tvNoData.setText(
                                                        "No clients having Birthday " + dayType);
                                                } else {
                                                    tvNoData.setText(
                                                        "No clients having Anniversary " + dayType);
                                                }
                                            }
                                        } else {
                                            birthdayAnniversaryAdapter =
                                                new BirthdayAnniversaryAdapter(
                                                    birthdayAnniversaryList);
                                            rvBirthdayAnniversary.setLayoutManager(
                                                new GridLayoutManager(getMainActivity(), 3));
                                            rvBirthdayAnniversary.setAdapter(
                                                birthdayAnniversaryAdapter);
                                            birthdayAnniversaryAdapter.notifyDataSetChanged();
                                            dataFoundFlag = false;
                                            hideProgress();
                                            tvNoData.setVisibility(View.VISIBLE);
                                            if (eventType.equalsIgnoreCase("B")) {
                                                tvNoData.setText(
                                                    "No clients having Birthday " + dayType);
                                            } else {
                                                tvNoData.setText(
                                                    "No clients having Anniversary " + dayType);
                                            }
                                        }
                                    } else {
                                        birthdayAnniversaryAdapter =
                                            new BirthdayAnniversaryAdapter(birthdayAnniversaryList);
                                        rvBirthdayAnniversary.setLayoutManager(
                                            new GridLayoutManager(getMainActivity(), 3));
                                        rvBirthdayAnniversary.setAdapter(
                                            birthdayAnniversaryAdapter);
                                        birthdayAnniversaryAdapter.notifyDataSetChanged();
                                        dataFoundFlag = false;
                                        hideProgress();
                                        tvNoData.setVisibility(View.VISIBLE);
                                        if (eventType.equalsIgnoreCase("B")) {
                                            tvNoData.setText(
                                                "No clients having Birthday " + dayType);
                                        } else {
                                            tvNoData.setText(
                                                "No clients having Anniversary " + dayType);
                                        }
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {

                                    hideProgress();
                                }
                            } catch (Exception e) {
                                dataFoundFlag = false;
                                e.printStackTrace();
                                hideProgress();
                                tvNoData.setVisibility(View.VISIBLE);
                                if (eventType.equalsIgnoreCase("B")) {
                                    tvNoData.setText("No clients having Birthday " + dayType);
                                } else {
                                    tvNoData.setText("No clients having Anniversary " + dayType);
                                }
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                dataFoundFlag = false;
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
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
                                    return;
                                } else {
                                    tvNoData.setVisibility(View.VISIBLE);
                                    if (eventType.equalsIgnoreCase("B")) {
                                        tvNoData.setText("No clients having Birthday " + dayType);
                                    } else {
                                        tvNoData.setText(
                                            "No clients having Anniversary " + dayType);
                                    }
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                //eat it
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            tvNoData.setVisibility(View.VISIBLE);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            tvNoData.setVisibility(View.VISIBLE);
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                tvNoData.setVisibility(View.VISIBLE);
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void sendGift(String ucid, String eventType) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("SelectedUcids", ucid);
            inputMap.put("EventType", eventType);
            String serviceMethod = "Client/SendGiftRequest";
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //after success code goes here
                            arrProfile = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            GIFT_SEND, "Ok", true, null);
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
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
                                    return;
                                } else {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                //eat it
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void sendEmail(String ucid, String eventType) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("SelectedUcids", ucid);
            inputMap.put("MessageType", "E");
            inputMap.put("checkAll", false);
            inputMap.put("EventType", eventType + "-T");
            String serviceMethod = "Client/GetMailClientId";
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //after success code goes here
                            arrProfile = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            EMAIL_SEND, "Ok", true, null);
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
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
                                    return;
                                } else {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                //eat it
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    private void sendEmailAll() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put("EmployeeCode", AccountDetails.getEmployeeCode());
            inputMap.put("MessageType", "E");
            //            inputMap.put("EventType", (spEventType.getSelectedItem().toString().equalsIgnoreCase("Birthday") ? "B" : "A") + "-T");
            //dayType
            inputMap.put("EventType",
                (spEventType.getSelectedItem().toString().equalsIgnoreCase("Birthday") ? "B" : "A")
                    + (dayType.equalsIgnoreCase(BELATED_FLAG) ? "-L" : "-T"));
            inputMap.put("checkAll", true);
            inputMap.put("FilterType", filterType);
            String serviceMethod = "Client/GetMailClientId";
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //after success code goes here
                            arrProfile = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            EMAIL_SEND, "Ok", true, null);
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
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
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (isBlank(error)) {
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

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                INTERNET_CONNECTION, "OK", true, null);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivEmailAll:
                if (dataFoundFlag) {
                    if (!filterType.equalsIgnoreCase("3")) {
                        alertDialog(getMainActivity(), 0,
                            getResources().getString(R.string.motilal_oswal), EMAIL_ALL_MESSAGE,
                            "Yes", "No", true, new MSFDialog.DialogListener() {
                                @Override
                                public void alertDialogAction(MSFDialog.Action action,
                                    Object... data) {
                                    if (action == MSFDialog.Action.OK) {
                                        sendEmailAll();
                                    }
                                }
                            });
                    }
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btnBelated:
                filterType = "1";
                dayType = "belated";
                getData();
                // ivEmailAll.setVisibility(View.GONE);
                CRMAppUtil.collapse(ivEmailAll);
                break;
            case R.id.btnToday:
                filterType = "2";
                dayType = "today";
                getData();
                //ivEmailAll.setVisibility(View.VISIBLE);
                CRMAppUtil.expand(ivEmailAll);
                break;
            case R.id.btnUpcomming:
                filterType = "3";
                dayType = "upcoming";
                getData();
                //ivEmailAll.setVisibility(View.GONE);
                CRMAppUtil.collapse(ivEmailAll);
                break;
            default:
                // Nothing to do
        }
    }

    public class BirthdayAnniversaryAdapter
        extends RecyclerView.Adapter<BirthdayAnniversaryAdapter.MyViewHolder> {

        private List<BirthdayAnniversaryModel> birthdayAnniversaryList;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_birthday_anniversary, parent, false);

            return new MyViewHolder(itemView);
        }

        public BirthdayAnniversaryAdapter(List<BirthdayAnniversaryModel> birthdayAnniversaryList) {
            this.birthdayAnniversaryList = birthdayAnniversaryList;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final BirthdayAnniversaryModel birthdayAnniversaryModel =
                birthdayAnniversaryList.get(position);
            String[] birthDate = birthdayAnniversaryModel.getBirthday().split("T");
            Date dateformat = CRMAppUtil.getDate(birthDate[0], "yyyy-MM-dd");
            String date = CRMAppUtil.getDateStr(dateformat, "dd MMM yyyy");
            holder.tvClientName.setText(birthdayAnniversaryModel.getClientName());
            if (birthdayAnniversaryModel.getClientCode().equalsIgnoreCase("0") || isBlank(
                birthdayAnniversaryModel.getClientCode())) {
                holder.tvClientCode.setText(birthdayAnniversaryModel.getUcid());
            } else {
                holder.tvClientCode.setText(birthdayAnniversaryModel.getClientCode());
            }
            holder.tvDate.setText(date);
            //            holder.tvDate.setText(birthdayAnniversaryModel.getBirthday());
            holder.tvEvent.setText(
                birthdayAnniversaryModel.getTotalYears()
                    + CRMAppUtil.getDayOfMonthSuffix(
                    Integer.parseInt(birthdayAnniversaryModel.getTotalYears()))
                    + birthdayAnniversaryModel.getTitle());
            if (birthdayAnniversaryModel.getTitle().equals("Birthday")) {
                holder.ivBirthdayAnniversary.setBackgroundResource(R.drawable.ic_bday);
            } else {
                holder.ivBirthdayAnniversary.setBackgroundResource(R.drawable.ic_anniversary);
            }

            //dont open it for belated
            if (!filterType.equalsIgnoreCase("1")) {
                holder.ivPopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog = new Dialog(getMainActivity());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow()
                            .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setContentView(R.layout.dialog_birthday_anniversary);
                        dialog.getWindow()
                            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCanceledOnTouchOutside(false);

                        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
                        ImageView ivCall = (ImageView) dialog.findViewById(R.id.ivCall);
                        ImageView ivSms = (ImageView) dialog.findViewById(R.id.ivSms);
                        ImageView ivEmail = (ImageView) dialog.findViewById(R.id.ivEmail);
                        ImageView ivSendGift = (ImageView) dialog.findViewById(R.id.ivSendGift);

                        ivCall.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!filterType.equalsIgnoreCase("3")) {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        "Do you want to call?", "Yes", "No", true,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    if (birthdayAnniversaryModel.getContactNo()
                                                        .equalsIgnoreCase("")) {
                                                        Toast.makeText(getMainActivity(),
                                                            "Contact number not available",
                                                            Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Intent callIntent =
                                                            new Intent(Intent.ACTION_DIAL);
                                                        callIntent.setData(Uri.parse("tel:"
                                                            + birthdayAnniversaryModel.getContactNo()));
                                                        callIntent.setFlags(
                                                            Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        getMainActivity().startActivity(callIntent);
                                                    }
                                                }
                                            }
                                        });
                                    dialog.dismiss();
                                }
                            }
                        });
                        ivSms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!filterType.equalsIgnoreCase("3")) {
                                    if (!(birthdayAnniversaryModel.getContactNo()
                                        .equals("android.intent.action.VIEW"))) {
                                        Intent smsIntent = new Intent("");
                                        smsIntent.setData(Uri.parse("smsto:" + Uri.encode(
                                            birthdayAnniversaryModel.getContactNo())));
                                        smsIntent.putExtra("sms_body",
                                            birthdayAnniversaryModel.getTitle()
                                                .equalsIgnoreCase("birthday")
                                                ? MOSLConstants.BIRTHDAY_MESSAGE
                                                : MOSLConstants.ANNIVERSARY_MESSAGE);
                                        smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getMainActivity().startActivity(smsIntent);
                                    } else {
                                        Toast.makeText(getMainActivity(),
                                            "Contact number not available", Toast.LENGTH_LONG)
                                            .show();
                                    }
                                    dialog.dismiss();
                                }
                            }
                        });
                        ivEmail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!filterType.equalsIgnoreCase("3")) {
                                    alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        EMAIL_MESSAGE_CLIENT, "Yes", "No", true,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    sendEmail(birthdayAnniversaryModel.getUcid(),
                                                        birthdayAnniversaryModel.getEventType());
                                                }
                                            }
                                        });
                                    dialog.dismiss();
                                }
                            }
                        });
                        ivSendGift.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!birthdayAnniversaryModel.isGiftFlag()) {
                                    sendGift(birthdayAnniversaryModel.getUcid(),
                                        birthdayAnniversaryModel.getEventType());
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
                });
            }//iff end
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            //            private MSFPopupWindow menuPopupWindow;
            private TextView tvClientName, tvClientCode, tvDate, tvEvent;
            private ImageView ivPopup, ivBirthdayAnniversary;

            public MyViewHolder(View itemView) {
                super(itemView);
                tvClientName = (TextView) itemView.findViewById(R.id.tvClientName);
                tvClientCode = (TextView) itemView.findViewById(R.id.tvClientCode);
                tvDate = (TextView) itemView.findViewById(R.id.tvDate);
                tvEvent = (TextView) itemView.findViewById(R.id.tvEvent);
                ivBirthdayAnniversary =
                    (ImageView) itemView.findViewById(R.id.ivBirthdayAnniversary);
                ivPopup = (ImageView) itemView.findViewById(R.id.ivPopup);
                //                final BirthdayAnniversaryModel model = data.get(position);

            }
        }

        @Override
        public int getItemCount() {
            return birthdayAnniversaryList.size();
        }
    }
}

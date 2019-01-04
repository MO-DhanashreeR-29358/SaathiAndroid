package com.mosl.tabapp.leadViewDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.tablefixheader.TableFixHeaders;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import cz.msebera.android.httpclient.Header;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

import static com.mosl.tabapp.common.CRMAppUtil.getRefreshColumnName;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Sanket Gurav on 7/14/2017.
 */

public class LeadViewHistoryFragment extends MOSLCommonFragment implements AdapterView.OnItemSelectedListener {
    private View leadViewHistoryView;
    private NestedScrollView nestedScrollView;
    private ViewPager viewPager;
    private Spinner spHistory;
    private TableFixHeaders table;
    //  private String leadId;
    private String mUcid;
    public static final String LEAD_HISTORY_FILTER = "{\"Request\":{\"LeadID\":%s,\"PageSize\":" + REPORT_PAGE_SIZE + ",\"Page\":1,\"CalculatedPage\":0,\"TotalCount\":0},\"Filter\":{\"Export\":null,\"ExportFilter\":null,\"Field\":null,\"Operator\":null,\"Value\":null,\"Logic\":null,\"Filters\":null},\"IsAdminMode\":true}";
    public static final String CLIENT_HISTORY_FILTER = "{\"Request\":{\"ProductId\":0,\"Mappingtype\":null,\"UCID\":%s,\"FamilyUCID\":0,\"BranchCode\":null,\"CountType\":null,\"clientType\":null,\"RequestSource\":null,\"ClientCode\":null,\"MobileNo\":null,\"PageSize\":" + REPORT_PAGE_SIZE + ",\"Page\":1,\"CalculatedPage\":0,\"TotalCount\":0},\"Filter\":{\"Export\":null,\"ExportFilter\":null,\"Field\":null,\"Operator\":null,\"Value\":null,\"Logic\":null,\"Filters\":null},\"IsAdminMode\":true}";

    private String serviceUrl = "";

    private SecondClickTableAdapter secondClickTableAdapter;
    private TextView tvNochart, txtLables;
    static Context mContext;
    private boolean dataFoundFlag, isTabClick;
    String clickedOn = "";
    int click = 1;
    private String leadId;
    private String mobileNo;
    private ArrayList<String> arrEventType;
    private ArrayAdapter<String> arrAdapEventType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        leadViewHistoryView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_leadview_history);
        setUpView(leadViewHistoryView);
        setUserVisibleHint(true);

        return leadViewHistoryView;
    }

    public static LeadViewHistoryFragment newInstance(int position, String type, String leadID, String mUcid) {
        LeadViewHistoryFragment fragment = new LeadViewHistoryFragment();
        Bundle args = new Bundle();
        args.putInt("Position", position);
        args.putString("Type", type);
        args.putString(LEAD_ID, leadID);
        args.putString(UCID, mUcid);
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View leadViewDetailView) {
        leadId = this.getArguments().getString(MOSLConstants.LEAD_ID, "0");
        mUcid = this.getArguments().getString(MOSLConstants.UCID, "0");
        mUcid = (mUcid != null && !CRMAppUtil.isBlank(mUcid)) ? mUcid : "0";
        spHistory = (Spinner) leadViewDetailView.findViewById(R.id.spHistory);
        tvNochart = (TextView) leadViewDetailView.findViewById(R.id.tvNochart);
        table = (TableFixHeaders) leadViewDetailView.findViewById(R.id.table);
        nestedScrollView = (NestedScrollView) leadViewDetailView.findViewById(R.id.nestedScrollView);
        nestedScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        nestedScrollView.setFocusable(true);
        nestedScrollView.setFocusableInTouchMode(true);
        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        table.allowTouchEvent(false);
       /* String[] eventTypeArray = new String[0];
        if (!leadId.equalsIgnoreCase("0") && !mUcid.equalsIgnoreCase("0")) {
            eventTypeArray = getActivity().getResources().getStringArray(R.array.LeadClientHistory);
        } else if (!leadId.equalsIgnoreCase("0")) {
            eventTypeArray = getActivity().getResources().getStringArray(R.array.LeadHistory);
        } else if (!mUcid.equalsIgnoreCase("0")) {
            eventTypeArray = getActivity().getResources().getStringArray(R.array.ClientHistory);
        }
        arrEventType = new ArrayList<String>(Arrays.asList(eventTypeArray));
        arrAdapEventType = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrEventType);
        arrAdapEventType.setDropDownViewResource(R.layout.mdropdown);
        spHistory.setAdapter(arrAdapEventType);
        spHistory.setOnItemSelectedListener(LeadViewHistoryFragment.this);*/

    }

    //Spinner listner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getSelectedItem().toString()) {
            case "Lead Detail History": {
                serviceUrl = "Lead/GetLeadHistoryByLeadId";
                String tmpReq = String.format(LEAD_HISTORY_FILTER, leadId);
                if (isTabClick) {
                    getHistoryMapping(serviceUrl, tmpReq);
                }

            }
            break;
            case "Lead Product Mapping History": {
                serviceUrl = "Lead/GetLeadProductHistoryByLeadId";
                String tmpReq = String.format(LEAD_HISTORY_FILTER, leadId);
                getHistoryMapping(serviceUrl, tmpReq);
            }
            break;
            case "Client Current Mapping": {
                serviceUrl = "Client/GetCurrentMapping";
                String tmpReq = String.format(CLIENT_HISTORY_FILTER, mUcid);
                if (!leadId.equalsIgnoreCase("0") && !mUcid.equalsIgnoreCase("0")) {
                    getHistoryMapping(serviceUrl, tmpReq);
                } else {
                    if (isTabClick) {
                        getHistoryMapping(serviceUrl, tmpReq);
                    }
                }

            }
            break;
            case "Client Mapping History": {
                serviceUrl = "Client/GetMappingHistory";
                String tmpReq = String.format(CLIENT_HISTORY_FILTER, mUcid);
                getHistoryMapping(serviceUrl, tmpReq);
            }
            break;
        }
        /*switch (position) {
            case 0: {
                serviceUrl = "Lead/GetLeadHistoryByLeadId";
                String tmpReq = String.format(LEAD_HISTORY_FILTER, leadId);
                if (isTabClick) {

                    getHistoryMapping(serviceUrl, tmpReq);
                }

            }
            break;
            case 1: {
                serviceUrl = "Lead/GetLeadProductHistoryByLeadId";
                String tmpReq = String.format(LEAD_HISTORY_FILTER, leadId);
                getHistoryMapping(serviceUrl, tmpReq);
            }
            break;
            case 2: {
                serviceUrl = "Client/GetCurrentMapping";
                String tmpReq = String.format(CLIENT_HISTORY_FILTER, mUcid);
                getHistoryMapping(serviceUrl, tmpReq);

            }
            break;
            case 3: {
                serviceUrl = "Client/GetMappingHistory";
                String tmpReq = String.format(CLIENT_HISTORY_FILTER, mUcid);
                getHistoryMapping(serviceUrl, tmpReq);
            }
            break;
        }*/

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && leadViewHistoryView != null && (viewPager.getCurrentItem() == this.getArguments().getInt("Position"))) {
                String[] eventTypeArray = new String[0];
                if (!leadId.equalsIgnoreCase("0") && !mUcid.equalsIgnoreCase("0")) {
                    eventTypeArray = getActivity().getResources().getStringArray(R.array.LeadClientHistory);
                } else if (!leadId.equalsIgnoreCase("0")) {
                    eventTypeArray = getActivity().getResources().getStringArray(R.array.LeadHistory);
                } else if (!mUcid.equalsIgnoreCase("0")) {
                    eventTypeArray = getActivity().getResources().getStringArray(R.array.ClientHistory);
                }
                arrEventType = new ArrayList<String>(Arrays.asList(eventTypeArray));
                arrAdapEventType = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrEventType);
                arrAdapEventType.setDropDownViewResource(R.layout.mdropdown);
                spHistory.setAdapter(arrAdapEventType);
                spHistory.setOnItemSelectedListener(LeadViewHistoryFragment.this);
                if (!leadId.equalsIgnoreCase("0")) {
                    serviceUrl = "Lead/GetLeadHistoryByLeadId";
                    String tmpReq = String.format(LEAD_HISTORY_FILTER, leadId);
                    getHistoryMapping(serviceUrl, tmpReq);
                    isTabClick = false;
                } else if (!mUcid.equalsIgnoreCase("0")) {
                    serviceUrl = "Client/GetCurrentMapping";
                    String tmpReq = String.format(CLIENT_HISTORY_FILTER, mUcid);
                    getHistoryMapping(serviceUrl, tmpReq);
                    isTabClick = false;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        /*    Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
            Crashlytics.logException(e);*/
        }
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }


    private void getHistoryMapping(String serviceMethod, String request) {
        isTabClick = true;
        /*addedAttendeeModelList;
        addedSplAttendeeModelList;*/
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            // customProgress.setVisibility(View.VISIBLE);


            //  String serviceMethod = "Meeting/AddUpdateMeeting";
            showProgress();
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), request, new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseS) {
                        //super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                Gson mGson = new Gson();
                                Type type = new TypeToken<HashMap<String, Object>>() {
                                }.getType();
                                HashMap<String, Object> response = mGson.fromJson(responseS, type);
                                if (response.size() != 0) {
                                    String status = response.get(STATUS).toString();
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        secondClickTableAdapter = new SecondClickTableAdapter(getActivity(), new ArrayList<HashMap<String, String>>());
                                        table.setAdapter(secondClickTableAdapter);
                                        //  JSONObject responseJSONObject = response.getJSONObject("Response");
                                        Map<String, Object> responseJSONObject =
                                            (Map<String, Object>) response.get("Response");
                                        // JSONArray arrayDetail = responseJSONObject.getJSONArray("Data");
                                        List<Map<String, Object>> arrayDetail =
                                            (List<Map<String, Object>>) responseJSONObject.get(
                                                "Data");
                                        ArrayList<String> columns = new ArrayList<String>();
                                        ArrayList<String> columnsHeader = new ArrayList<String>();

                                        if (arrayDetail.size() != 0) {
                                            for (int y = 0; y < arrayDetail.size(); y++) {
                                                Map<String, Object> objDetail = arrayDetail.get(y);
                                                if (columns.size() == 0) {
                                                    //  Iterator iter = objDetail.keys();
                                                    for (Object columnName : objDetail.keySet()) {
                                                        //  String columnName = iter.next().toString();
                                                        columns.add(columnName.toString());
                                                        if (columnName.toString().equalsIgnoreCase("lead_id")) {
                                                            columnName = columnName.toString().toUpperCase();
                                                        }
                                                        columnsHeader.add(getRefreshColumnName(columnName.toString()));
                                                    }
                                                    String[] stockArr = new String[columnsHeader.size()];
                                                    stockArr = columnsHeader.toArray(stockArr);
                                                    secondClickTableAdapter.setHeaders(stockArr);
                                                }
                                                LinkedHashMap<String, String> hm = new LinkedHashMap<>();
                                                for (String column : columns) {
                                                    // String columnData = (objDetail.isNull(column) || objDetail.getString(column).equals("")) ? "" : objDetail.getString(column);
                                                    String columnData = (objDetail.get(column) == null || objDetail.get(column).toString() == null) ? "" : objDetail.get(column).toString();
                                                    if (column.equalsIgnoreCase("MOBILE")) {
                                                        mobileNo = columnData;
                                                    }
                                                    if (objDetail.get(column) instanceof Number) {
                                                        String strValue = objDetail.get(column).toString().trim();

                                                        if (strValue.indexOf(".") > -1 && !strValue.endsWith(".0") && !strValue.endsWith(".00")) {
                                                            columnData = String.format("%.2f", Double.parseDouble(objDetail.get(column).toString()));
                                                        } else {
                                                            columnData = String.format("%.0f", Double.parseDouble(objDetail.get(column).toString()));
                                                        }
                                                    }
                                                    if (columnData.contains("T")) {
                                                        columnData = CRMAppUtil.getDateStrT(columnData, mFormat, DATE_FORMAT_TO_DISPLAY);
                                                    }

                                                    if (column.equalsIgnoreCase("lead_id")) {
                                                        column = column.toUpperCase();
                                                    }
                                                    hm.put(getRefreshColumnName(column), columnData.trim());
                                                }
                                                secondClickTableAdapter.add(hm);
                                            }
                                            secondClickTableAdapter.notifyDataSetChanged();
                                            dataFoundFlag = true;
                                            table.setVisibility(View.VISIBLE);
                                            tvNochart.setVisibility(View.GONE);
                                        } else {
                                            dataFoundFlag = false;
                                            table.setVisibility(View.GONE);
                                            tvNochart.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        hideProgress();
                                        String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(new JSONObject(responseS));
                                        table.setVisibility(View.GONE);
                                        dataFoundFlag = false;
                                        tvNochart.setVisibility(View.VISIBLE);
                                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                    }
                                } else {
                                    dataFoundFlag = false;
                                    table.setVisibility(View.GONE);
                                    tvNochart.setVisibility(View.VISIBLE);
                                }

                            } else {
                                dataFoundFlag = false;
                                table.setVisibility(View.GONE);
                                tvNochart.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            dataFoundFlag = false;
                            table.setVisibility(View.GONE);
                            tvNochart.setVisibility(View.VISIBLE);
                        }
                        hideProgress();
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        try {
                            JSONObject errorResponse = new JSONObject(responseString);
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }


                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }


    ///SDTable
    private class CommonHeader {
        private final String name;
        private final List<Item> list;

        CommonHeader(String name) {
            this.name = name;
            list = new ArrayList<Item>();
        }

        public int size() {
            return list.size();
        }

        public Item get(int i) {
            return list.get(i);
        }
    }

    private class Item {
        private final String[] data;

        private Item(String[] rowData) {
            data = rowData;
        }
    }

    public class SecondClickTableAdapter extends BaseTableAdapter {
        private final CommonHeader commonHeaders[];
        private String headers[] = {};
        private final float density;
        HashMap hashMap;
        ArrayList<HashMap<String, String>> hashArray;
        private LayoutInflater inflater = null;

        public SecondClickTableAdapter(Context context, ArrayList<HashMap<String, String>> hashArray) {
            commonHeaders = new CommonHeader[]{new CommonHeader("asd")};
            this.hashArray = hashArray;
            density = context.getResources().getDisplayMetrics().density;


        }

        public String[] getHeaders() {
            return headers;
        }

        public void setHeaders(String[] headers) {
            this.headers = headers;
        }

        @Override
        public int getRowCount() {
            return commonHeaders[0].list.size();
        }

        public void clear() {
            //    hashArray.clear();
            //    commonHeaders[0].list.clear();
        }

        public void add(HashMap<String, String> row) {
            hashArray.add(row);

            String rowData[] = new String[headers.length];
            int ctr = 0;
            for (String columnName : headers) {
                String value = row.get(columnName).toString();
                rowData[ctr] = value;
                ctr++;
            }

            commonHeaders[0].list.add(new Item(rowData));

        }

        @Override
        public int getColumnCount() {
            return headers.length;
        }

        @Override
        public View getView(final int row, int column, View convertView, ViewGroup parent) {
            final View view;

            switch (getItemViewType(row, column)) {
                case 0:
                    view = getFirstHeader(row, column, convertView, parent);
                    break;
                case 1:
                    view = getHeader(row, column, convertView, parent);
                    break;
                case 2:
                    view = getFirstBody(row, column, convertView, parent);

               /*     view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            leadId = hashArray.get(row).get("LEAD ID").toString();
                            leadViewDialog();
                           *//* lMap = (LinkedHashMap) hashArray.get(row);
                            Log.d("Exc", lMap.toString());
                            Bundle args = new Bundle();
                            String[] parts = (lMap.get("AMC Name").toString()).split("#");
                            String amcName = parts[0];
                            String amcid = parts[1];
                            args.putString(AMCID, amcid);

                            navigateTo(NAV_TO_SCHEME_WISE_AUM_REPORT, args, getFragmentManager(), true);
*//*
                        }
                    });*/

                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
                   /* view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                         //   leadId = hashArray.get(row).get("LEAD ID").toString();
                         //   leadViewDialog();

                        }
                    });*/
                    break;
                case 4:
                    view = getFamilyView(row, column, convertView, parent);
                    break;
                default:
                    throw new RuntimeException("runtime exception");
            }
            return view;
        }

        //click on symbol header
        private View getFirstHeader(final int row, int column, View convertView, ViewGroup parent) {
            inflater = LayoutInflater.from(getActivity());
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header_first, parent, false);
            }
            RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
            TextView head = ((TextView) convertView.findViewById(android.R.id.text1));
            try {
                head.setText(headers[0]);
            } catch (Exception e) {
                tvNochart.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);

                e.printStackTrace();
            }

           /* try {
                if (clickedOn.equals(headers[0]) && click > 1) {
                    head.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_button, 0);
                } else {
                    head.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_button, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                tvNochart.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);

            }

            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("HeadClick", ((TextView) v).getText().toString());
                    TextView temp = (TextView) v;
                    clickedOn = temp.getText().toString();
                    if (click == 1) {
                        sortByAMCName(click, clickedOn);
                        click++;
                    } else if (click > 1) {
                        sortByAMCName(click, clickedOn);
                        click = 1;

                    }
                }
            });*/
            return convertView;
        }

        private void sortByAMCName(Integer sortVal, String clickedOn) {
            String sortCol = clickedOn; //((TextView) headerTextField).getText().toString();
            ArrayList<String> out = new ArrayList<String>();
            for (int j = 0; j < hashArray.size(); j++) {
                LinkedHashMap linkedHashMap = (LinkedHashMap) hashArray.get(j);
                String val = (String) linkedHashMap.get(sortCol);
                out.add(val);
            }

            if (sortVal == 1) {
                Collections.sort(out, new Comparator<String>() {
                    public int compare(String c1, String c2) {
                        return c1.compareTo(c2);
                    }
                });
            } else {
                Collections.reverse(out);
            }

            commonHeaders[0].list.clear();
            ArrayList<HashMap<String, String>> tempHashArray = (ArrayList<HashMap<String, String>>) hashArray.clone();
            hashArray.clear();

            for (int i = 0; i < out.size(); i++) {
                String sortedValue = out.get(i);
                for (int j = 0; j < tempHashArray.size(); j++) {
                    LinkedHashMap sortLinkedMap = (LinkedHashMap) tempHashArray.get(j);
                    String sortColVal = (String) sortLinkedMap.get(sortCol);

                    if (sortColVal.equals(sortedValue)) {
                        secondClickTableAdapter.add(sortLinkedMap);
                        tempHashArray.remove(sortLinkedMap);
                    }
                }
            }
            secondClickTableAdapter.notifyDataSetChanged();
        }


        //click on header other than symbol
        private View getHeader(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header, parent, false);
            }
            TextView head = ((TextView) convertView.findViewById(android.R.id.text1));
            head.setText(headers[column]);
           /* if (clickedOn.equals(headers[column]) && click > 1) {
                head.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_button, 0);
            } else {
                head.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_button, 0);
            }

            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("HeadClick", ((TextView) v).getText().toString());
                    String sortCol = ((TextView) v).getText().toString();


                    ArrayList<String> outStr = new ArrayList<String>();


                    for (int j = 0; j < hashArray.size(); j++) {
                        LinkedHashMap linkedHashMap = (LinkedHashMap) hashArray.get(j);
                        String val = (String) linkedHashMap.get(sortCol);
                        outStr.add(val);

                    }


                    TextView temp = (TextView) v;
                    clickedOn = temp.getText().toString();
                    if (click == 1) {
                        Collections.sort(outStr, new Comparator<String>() {
                            public int compare(String c1, String c2) {
                                return c1.compareTo(c2);
                            }
                        });
                        click++;
                    } else if (click > 1) {
                        Collections.reverse(outStr);
                        click = 1;
                    }

                    commonHeaders[0].list.clear();
                    ArrayList<HashMap<String, String>> tempHashArray = (ArrayList<HashMap<String, String>>) hashArray.clone();
                    hashArray.clear();


                    for (int i = 0; i < outStr.size(); i++) {
                        String sortedValue = outStr.get(i);
                        for (int j = 0; j < tempHashArray.size(); j++) {
                            LinkedHashMap sortLinkedMap = (LinkedHashMap) tempHashArray.get(j);
                            String sortColVal = (String) sortLinkedMap.get(sortCol);
                            if (sortedValue.equals(sortColVal)) {
                                secondClickTableAdapter.add(sortLinkedMap);
                                tempHashArray.remove(sortLinkedMap);
                            }
                        }
                    }

                    secondClickTableAdapter.notifyDataSetChanged();

                }
            });*/
            return convertView;
        }

        private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_first, parent, false);
            }
            LinearLayout listLayout = (LinearLayout) convertView.findViewById(R.id.item_table_first);
            listLayout.setBackgroundResource(row % 2 == 0 ? R.color.alternateBackground : R.color.white);
            final TextView tv_table_first = (TextView) convertView.findViewById(R.id.tv_table_first);
            tv_table_first.setText(getDevice(row).data[column + 1]);

            return convertView;
        }

        private View getBody(final int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table, parent, false);
            }
            LinearLayout listLayout = (LinearLayout) convertView.findViewById(R.id.item_table);
            listLayout.setBackgroundResource(row % 2 == 0 ? R.color.alternateBackground : R.color.white);
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(getDevice(row).data[column]);
            TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
            text1.setBackgroundResource(0);
            return convertView;
        }

        private View getFamilyView(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_family, parent, false);
            }
            final String string;
            if (column == -1) {
                string = getFamily(row).name;
            } else {
                string = "";
            }
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(string);
            return convertView;
        }

        @Override
        public int getWidth(int column) {
            int widthSize = 0;
            if (column == -1) {
                widthSize = getResources().getDimensionPixelSize(R.dimen._100sdp);
            }
            for (int i = 0; i < column; i++) {
                widthSize = getResources().getDimensionPixelSize(R.dimen._135sdp);
            }
            return widthSize;
        }

        @Override
        public int getHeight(int row) {
            final int height;
            if (row == -1) {
                height = getResources().getDimensionPixelSize(R.dimen._40sdp);
            } else if (isFamily(row)) {
                height = 0;
            } else {
                height = getResources().getDimensionPixelSize(R.dimen._40sdp);
            }
            return height;
        }

        @Override
        public int getItemViewType(int row, int column) {
            final int itemViewType;
            if (row == -1 && column == -1) {
                itemViewType = 0;
            } else if (row == -1) {
                itemViewType = 1;
            } else if (isFamily(row)) {
                itemViewType = 4;
            } else if (column == -1) {
                itemViewType = 2;
            } else {
                itemViewType = 3;
            }
            return itemViewType;
        }

        private boolean isFamily(int row) {

            if (row == -1 || row == 0) {
                int family = 0;
                while (row > 0) {
                    row -= commonHeaders[family].size();
                    family++;
                }
                return row == 1;
            }
            return row == 0;
        }

        private CommonHeader getFamily(int row) {
            int family = 0;
            while (row >= 0) {
                row -= commonHeaders[family].size();
            }
            return commonHeaders[family];
        }

        private Item getDevice(int row) {
            int family = 0;
            while (row >= 0) {
                row -= commonHeaders[family].size();
            }
            return commonHeaders[family].get(row + commonHeaders[family].size());
        }

        @Override
        public int getViewTypeCount() {
            return 5;
        }
    }


}

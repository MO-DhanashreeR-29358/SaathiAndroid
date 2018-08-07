package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.tablefixheader.TableFixHeaders;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 4/22/2017.
 */

public class TeamLeadShowFragment extends MOSLCommonFragment implements View.OnClickListener{
    private View leadShowView;
    private TableFixHeaders table;
    private ArrayList<String> arrProductType;
    private ArrayAdapter<String> productAdapter;
    private LeadTableAdapter leadTableAdapter;
    private ArrayList<String> arrSegmentName;
    private ArrayAdapter<String> segmentNameAdapter;
    private TextView tvNochart, txtDisclaimer,tvShowLess;
    private LinkedHashMap lMap;
    private String clickedOn = "";
    private int click = 1;
    private Bundle bundle = this.getArguments();
    private String ucid;
    private int source = 0;
    private boolean dataFoundFlag;
    static Context mContext;
    private DashboardModel dashboardModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        leadShowView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_table_lead_show);
        setUpView(leadShowView);
        hideAppTitle(getClass().toString());
        return leadShowView;
    }

    public static TeamLeadShowFragment newInstance(Context context) {
        TeamLeadShowFragment fragment = new TeamLeadShowFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View leadShowView) {
        table = (TableFixHeaders) leadShowView.findViewById(R.id.table);
        tvNochart = (TextView) leadShowView.findViewById(R.id.tvNochart);
        tvShowLess = (TextView) leadShowView.findViewById(R.id.tvShowLess);
         dashboardModel = (DashboardModel) this.getArguments().getSerializable(TEAM_LEAD);
        List<WidgetModel> actionableWidgetList = dashboardModel.getGroupModel(TEAM_LEAD).getWidgetList();
        for (WidgetModel model : actionableWidgetList) {
            webServiceCall(model.getSystemName());
        }
        tvShowLess.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.tvShowLess){
            Bundle args = new Bundle();
            TeamLeadsFragment clientsFragment = TeamLeadsFragment.newInstance(getContext());
            args.putSerializable(TEAM_LEAD, dashboardModel);
            clientsFragment.setArguments(args);
         //   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceTeamFragment, clientsFragment).commit();
        }

    }
    private void webServiceCall(String systemName) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Dashboard/LoadWidgetAsync";
            Map inputMap = new HashMap<>();
            inputMap.put("WidgetSystemName", systemName);

            //dashboard for
            inputMap.put("BusinessProfileId", dashboardModel.getBusinessProfileIdForDashboard());
            inputMap.put(PRODUCT_ID, dashboardModel.getProductId());
            inputMap.put(EMPLOYEE_CODE, dashboardModel.getEmployeeCode());

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    hideProgress();
                                    leadTableAdapter = new LeadTableAdapter(getActivity(), new ArrayList<HashMap<String, String>>());
                                    table.setAdapter(leadTableAdapter);
                                    JSONObject responseJsonObj = response.getJSONObject("Response");
                                    JSONArray arrayDetail = responseJsonObj.getJSONArray("Table");
                                    ArrayList<String> columns = new ArrayList<String>();
                                    ArrayList<String> columnsFirst = new ArrayList<String>();
                                    ArrayList<String> columnsSecond = new ArrayList<String>();
                                    ArrayList<String> header = new ArrayList<String>();
                                    ArrayList<String> headerSecond = new ArrayList<String>();

                                    if (arrayDetail.length() != 0) {

                                        for (int y = 2; y < arrayDetail.length(); y++) {

                                            JSONObject objDetailSecond = (JSONObject) arrayDetail.get(0);

                                            if (columnsSecond.size() == 0) {
                                                Iterator iter = objDetailSecond.keys();
                                                while (iter.hasNext()) {
                                                    String columnName = iter.next().toString();
                                                    if (!columnName.equalsIgnoreCase("Id")) {
                                                        columnsSecond.add(columnName);
                                                    }

                                                }
                                                for (String column : columnsSecond) {
                                                    String columnData = objDetailSecond.getString(column).equals("") ? "" : objDetailSecond.getString(column);
                                                    header.add(columnData);
                                                }
                                                String[] stockArr = new String[header.size()];
                                                stockArr = header.toArray(stockArr);
                                                leadTableAdapter.setHeadersSecond(stockArr);
                                            }

                                            JSONObject objDetailFirst = (JSONObject) arrayDetail.get(1);
                                            if (columnsFirst.size() == 0) {
                                                Iterator iter = objDetailFirst.keys();
                                                while (iter.hasNext()) {
                                                    String columnName = iter.next().toString();
                                                    if (!columnName.equalsIgnoreCase("Id")) {
                                                        columnsFirst.add(columnName);
                                                    }

                                                }

                                                for (String column : columnsFirst) {
                                                    String columnData = objDetailFirst.getString(column).equals("") ? "" : objDetailFirst.getString(column);
                                                    headerSecond.add(columnData);
                                                }
                                                String[] stockArr = new String[headerSecond.size()];
                                                stockArr = headerSecond.toArray(stockArr);
                                                leadTableAdapter.setHeaders(stockArr);
                                            }

                                            JSONObject objDetail = (JSONObject) arrayDetail.get(y);

                                            if (columns.size() == 0) {
                                                Iterator iter = objDetail.keys();
                                                while (iter.hasNext()) {
                                                    String columnName = iter.next().toString();
                                                    if (!columnName.equalsIgnoreCase("Id")) {
                                                        columns.add(columnName);
                                                    }
                                                }

                                                String[] stockArr = new String[columns.size()];
                                                stockArr = columns.toArray(stockArr);
                                                leadTableAdapter.setHeaders(stockArr);
                                            }

                                            LinkedHashMap<String, String> hm = new LinkedHashMap<>();

                                            for (String column : columns) {
                                                String columnData = objDetail.getString(column).equals("") ? "" : objDetail.getString(column);
                                                hm.put(column, columnData);
                                            }
                                            leadTableAdapter.add(hm);
                                        }
                                        leadTableAdapter.notifyDataSetChanged();
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
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                  //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                           // MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
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
                              //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
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
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), ERROR_LOADING_DATA_FOR+systemName, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }

    }


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

    public class LeadTableAdapter extends BaseTableAdapter {
        private final CommonHeader commonHeaders[];

        private String headers[] = {};

        private String headers2[] = {};


        private final float density;
        HashMap hashMap;
        ArrayList<HashMap<String, String>> hashArray;
        private LayoutInflater inflater = null;

        public LeadTableAdapter(Context context, ArrayList<HashMap<String, String>> hashArray) {
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

        public String[] getHeadersSecond() {
            return headers2;
        }

        public void setHeadersSecond(String[] headers) {
            this.headers2 = headers;
        }

        @Override
        public int getRowCount() {
            return commonHeaders[0].list.size();
        }

        public void clear() {
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
        public View getView(final int row, final int column, View convertView, ViewGroup parent) {
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

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle args = new Bundle();
                            args.putString("cliencode", "");
                            navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
                           /* lMap = (LinkedHashMap) hashArray.get(column);
                            Log.v("Exc", lMap.toString());*/
                           /* lMap = (LinkedHashMap) hashArray.get(row);
                            Log.d("Exc", lMap.toString());
                            Bundle args = new Bundle();
                            String[] parts = (lMap.get("AMC Name").toString()).split("#");
                            String amcName = parts[0];
                            String amcid = parts[1];
                            args.putString(AMCID, amcid);

                            navigateTo(NAV_TO_SCHEME_WISE_AUM_REPORT, args, getFragmentManager(), true);
*/
                        }
                    });

                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle args = new Bundle();
                            args.putString("cliencode", "");
                            navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
                          /*  lMap = (LinkedHashMap) hashArray.get(column);
                            Log.v("Exc", lMap.toString());*/
                        }
                    });
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
                convertView = inflater.inflate(R.layout.item_table_header_first_lead, parent, false);
            }

            TextView head = ((TextView) convertView.findViewById(android.R.id.text1));
            try {
                head.setText(headers[0]);
            } catch (Exception e) {
                e.printStackTrace();
                tvNochart.setVisibility(View.VISIBLE);
            }
            head.setTypeface(null, Typeface.BOLD);
            return convertView;
        }


        //click on header other than symbol
        private View getHeader(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header_lead, parent, false);
            }
            TextView head = ((TextView) convertView.findViewById(android.R.id.text1));

            TextView head1 = ((TextView) convertView.findViewById(android.R.id.text2));

            head1.setText(headers[column]);
            head.setText(headers2[column]);

            return convertView;
        }

        private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_first_lead, parent, false);
            }
            LinearLayout listLayout = (LinearLayout) convertView.findViewById(R.id.item_table_first_lead);

            if (row == 0 || row == 3) {
                listLayout.setBackgroundResource(R.color.lead_self_back);
            } else if (row == 1 || row == 2 || row == 4 || row == 5) {
                listLayout.setBackgroundResource(R.color.lead_ekyc_back);
            } else if (row == 6) {
                listLayout.setBackgroundResource(R.color.secondaryBackground);
            }
            TextView tv_table_first = (TextView) convertView.findViewById(R.id.tv_table_first);
           /* String[] parts = getDevice(row).data[column + 1].split("#");
            String amcName = parts[0];
            String ucid = parts[1];*/
            if (row == 0 || row == 3) {
                tv_table_first.setTypeface(null, Typeface.BOLD);
                tv_table_first.setTextColor(getResources().getColor(R.color.flush_orange));
            } else if (row == 6) {
                tv_table_first.setTypeface(null, Typeface.BOLD);
                tv_table_first.setTextColor(getResources().getColor(R.color.black));
            } else {
                tv_table_first.setTypeface(null, Typeface.BOLD);
                tv_table_first.setTextColor(getResources().getColor(R.color.black));
            }

            tv_table_first.setText(getDevice(row).data[column + 1]);
            return convertView;
        }

        private String getItem(int row, int pos) {
            if (pos == -1) {
                return hashArray.get(row).get(MOSLConstants.STAGE).toString();
            } else if (pos == 0) {
                return hashArray.get(row).get(MOSLConstants.LEAD_ALLOCATION).toString();
            } else if (pos == 1) {
                return hashArray.get(row).get(MOSLConstants.LEAD_FILTER).toString();
            } else if (pos == 2) {
                return hashArray.get(row).get(MOSLConstants.REJECTED).toString();
            }
            return "N/A";
        }

        private View getBody(final int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table, parent, false);
            }
            LinearLayout listLayout = (LinearLayout) convertView.findViewById(R.id.item_table);


            if (row == 0 || row == 3) {
                listLayout.setBackgroundResource(R.color.lead_self_back);
            } else if (row == 1 || row == 2 || row == 4 || row == 5) {
                listLayout.setBackgroundResource(R.color.lead_ekyc_back);
            } else if (row == 6) {
                listLayout.setBackgroundResource(R.color.lead_total_back);
            }
            try {
                ((TextView) convertView.findViewById(android.R.id.text1)).setText(getDevice(row).data[column]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
            if (row == 0 || row == 3) {
                text1.setTextColor(getResources().getColor(R.color.flush_orange));
            } else if (row == 6) {
                text1.setTypeface(null, Typeface.BOLD);
                text1.setTextColor(getResources().getColor(R.color.black));
            } else {
                text1.setTextColor(getResources().getColor(R.color.black));
            }

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
                widthSize = Math.round(150 * density);
            }
            for (int i = 0; i < column; i++) {
                widthSize = Math.round((int) getResources().getDimension(R.dimen.appTableWithWidth) * density);
            }
            return widthSize;
        }

        @Override
        public int getHeight(int row) {
            final int height;
            if (row == -1) {
                height = (int) getResources().getDimension(R.dimen.appTableWithSizeHeader);
            } else if (isFamily(row)) {
                height = 0;
            } else {
                height = (int) getResources().getDimension(R.dimen.appTableWithSize);
            }
            return Math.round(height * density);
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

package com.mosl.tabapp.leadViewDetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.app.coreui.spinner.AdapterUtils;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.leadinteraction.LeadInteractionRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CustomWebViewDialog;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.tablefixheader.TableFixHeaders;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import com.mosl.tabapp.util.SpinnerAdapterFactory;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.getRefreshColumnName;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY;
import static com.mosl.tabapp.constants.MOSLConstants.DETAILS;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD_ID;
import static com.mosl.tabapp.constants.MOSLConstants.MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static com.mosl.tabapp.constants.MOSLConstants.UCID;
import static com.mosl.tabapp.constants.MOSLConstants.mFormat;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Sanket Gurav on 10/7/2017.
 */

public class LeadViewInteractionFragment extends CommonFragment
        implements AdapterView.OnItemSelectedListener {
    private View leadViewInteractionView;
    private ViewPager viewPager;
    private Spinner spInteraction;
    private TableFixHeaders table;
    private String mUcid;
    private String serviceUrl = "";
    private TextView tvNochart;
    private boolean isTabClick;
    private String leadId = "";

    public static LeadViewInteractionFragment newInstance(int position, String type, String leadID,
                                                          String mUcid) {
        LeadViewInteractionFragment fragment = new LeadViewInteractionFragment();
        Bundle args = new Bundle();
        args.putInt("Position", position);
        args.putString("Type", type);
        args.putString(LEAD_ID, leadID);
        args.putString(UCID, mUcid);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * this is to remove line feeds an from jtml String
     */
    private static String getHtmlSafeString(String data) {
        return data.replace("\r", "").replace("\n", "").replace("\t", "");
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_leadview_interactionn;
    }

    //Case end
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        leadViewInteractionView = super.onCreateView(inflater, container, savedInstanceState);
        setUpView(leadViewInteractionView);
        setUserVisibleHint(true);

        return leadViewInteractionView;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpView(View leadViewDetailView) {
        leadId = this.getArguments().getString(LEAD_ID, "0");
        mUcid = this.getArguments().getString(UCID, "0");
        mUcid = (mUcid != null && !CRMAppUtil.isBlank(mUcid)) ? mUcid : "0";
        spInteraction = leadViewDetailView.findViewById(R.id.spHistory);
        tvNochart = leadViewDetailView.findViewById(R.id.tvNochart);
        table = leadViewDetailView.findViewById(R.id.table);
        NestedScrollView nestedScrollView =
                leadViewDetailView.findViewById(R.id.nestedScrollView);
        nestedScrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        nestedScrollView.setFocusable(true);
        nestedScrollView.setFocusableInTouchMode(true);
        nestedScrollView.setOnTouchListener((v, event) -> {
            v.requestFocusFromTouch();
            return false;
        });
        table.allowTouchEvent(false);
    }

    //region server call

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && leadViewInteractionView != null && (viewPager.getCurrentItem()
                    == this.requireArguments()
                    .getInt("Position"))) {
                String[] eventTypeArray = requireActivity()
                        .getResources()
                        .getStringArray(R.array.LeadInteraction);
                ArrayList<String> arrEventType =
                        new ArrayList<>(Arrays.asList(eventTypeArray));

                ArrayAdapter<String> arrAdapEventType = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                spInteraction.setAdapter(arrAdapEventType);
                AdapterUtils.setSpinnerData(arrAdapEventType, spInteraction, arrEventType);

                spInteraction.setOnItemSelectedListener(LeadViewInteractionFragment.this);
                serviceUrl = "TalismaInteractionRequest/TalismaRequestGet";
                getInteractionMapping(serviceUrl);
                isTabClick = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void render(UiModel<List<Map<String, Object>>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            error.printStackTrace();
            table.setVisibility(View.GONE);
            tvNochart.setVisibility(View.VISIBLE);
            showOnFailurePopup(error);
        }

        List<Map<String, Object>> data = uiModel.data;
        if (data != null) {
            try {
                renderTable(data);
            } catch (Exception e) {
                e.printStackTrace();
                table.setVisibility(View.GONE);
                tvNochart.setVisibility(View.VISIBLE);
                alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    private void getInteractionMapping(String serviceMethod) {
        isTabClick = true;
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            LeadInteractionRequest leadInteractionRequest = new LeadInteractionRequest();
            leadInteractionRequest.request.leadId = this.leadId;
            leadInteractionRequest.request.uCID = this.mUcid;
            Observable<String> interactiveObs;
            switch (serviceMethod) {
                case "TalismaInteractionRequest/TalismaRequestGet": {
                    interactiveObs = getSaathiApi().getInteraction(leadInteractionRequest);
                    break;
                }

                case "Client/GetClientBirthdayAnniversaryWishLog": {
                    interactiveObs =
                            getSaathiApi().getBirthdayAnniversaryEvent(leadInteractionRequest);
                    break;
                }

                default: {
                    interactiveObs = getSaathiApi().getSeminarEvent(leadInteractionRequest);
                }
            }

            Disposable subscribe = interactiveObs
                    .map(responseS -> {
                        Gson mGson = new Gson();
                        Type type = new TypeToken<HashMap<String, Object>>() {
                        }.getType();
                        HashMap<String, Object> response = mGson.fromJson(responseS, type);
                        Map<String, Object> responseJsonObj =
                                (Map<String, Object>) response.get("Response");
                        List<Map<String, Object>> tableResponseList =
                                (List<Map<String, Object>>) responseJsonObj.get(
                                        "Data");
                        String status = response.get(STATUS).toString();
                        if (status.equalsIgnoreCase("true")) {
                            return tableResponseList;
                        } else {
                            String message = response.get(MESSAGE).toString();
                            throw new ServiceFailureException(message, message);
                        }
                    })
                    .compose(applyUiModel())
                    .subscribe(this::render);
        } else {
            noInternetConnectionDialog();
        }
    }

    @SuppressLint("DefaultLocale")
    private void renderTable(List<Map<String, Object>> tableList) {
        SecondClickTableAdapter secondClickTableAdapter =
                new SecondClickTableAdapter(new ArrayList<>());
        table.setAdapter(secondClickTableAdapter);

        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> columnsHeader = new ArrayList<>();

        if (tableList.size() != 0) {
            for (int y = 0; y < tableList.size(); y++) {
                Map<String, Object> objDetail = tableList.get(y);

                if (columns.size() == 0) {
                    for (Object columnName : objDetail.keySet()) {
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
                    String columnData =
                            objDetail.get(column) == null
                                    ? "" : objDetail.get(column).toString();

                    if (objDetail.get(column) instanceof Number) {
                        String strValue =
                                objDetail.get(column).toString().trim();

                        if (strValue.contains(".")
                                && !strValue.endsWith(".0")
                                && !strValue.endsWith(".00")) {
                            columnData = String.format("%.2f",
                                    Double.parseDouble(
                                            objDetail.get(column).toString()));
                        } else {
                            columnData = String.format("%.0f",
                                    Double.parseDouble(
                                            objDetail.get(column).toString()));
                        }
                    }
                    if (column.equalsIgnoreCase("lead_id")) {
                        column = column.toUpperCase();
                    }
                    if (columnData.contains("T")) {
                        columnData =
                                CRMAppUtil.getDateStrT(columnData, mFormat, DATE_FORMAT_TO_DISPLAY);
                    }
                    hm.put(getRefreshColumnName(column), columnData.trim());
                }
                secondClickTableAdapter.add(hm);
            }
            secondClickTableAdapter.notifyDataSetChanged();
            table.setVisibility(View.VISIBLE);
            tvNochart.setVisibility(View.GONE);
        } else {
            table.setVisibility(View.GONE);
            tvNochart.setVisibility(View.VISIBLE);
        }
    }

    //endregion
    //region selection logic
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: {
                if (isTabClick) {
                    serviceUrl = "TalismaInteractionRequest/TalismaRequestGet";
                    getInteractionMapping(serviceUrl);
                }
            }
            break;
            case 1: {
                serviceUrl = "Client/GetClientBirthdayAnniversaryWishLog";
                getInteractionMapping(serviceUrl);
            }
            break;
            case 2: {
                serviceUrl = "Event/GetEventSeminarAttendedList";
                getInteractionMapping(serviceUrl);
            }
            break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //endregion
    void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    //endregion
    //region common addapter logic
    private class CommonHeader {
        private final String name;
        private final List<Item> list;

        CommonHeader(String name) {
            this.name = name;
            list = new ArrayList<>();
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
        private final CommonHeader[] commonHeaders;
        ArrayList<HashMap<String, String>> hashArray;
        private String[] headers = {};
        private LayoutInflater inflater = null;
        private int btnindex = -1;

        private SecondClickTableAdapter(
                ArrayList<HashMap<String, String>> hashArray) {
            commonHeaders = new CommonHeader[]{new CommonHeader("asd")};
            this.hashArray = hashArray;
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

        public void add(HashMap<String, String> row) {
            hashArray.add(row);

            String[] rowData = new String[headers.length];
            int ctr = 0;
            for (String columnName : headers) {
                String value = row.get(columnName);
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
                    view = getFirstHeader(convertView, parent);
                    break;
                case 1:
                    view = getHeader(column, convertView, parent);
                    break;
                case 2:
                    view = getFirstBody(row, column, convertView, parent);

                    break;
                case 3:
                    if (column == btnindex) {
                        view = getBodyWithButton(convertView, parent);
                    } else {
                        view = getBody(row, column, convertView, parent);
                    }

                    view.setOnClickListener(v -> {
                        if (!isBlank(getValue(row, DETAILS))) {
                            String strHtml = getValue(row, DETAILS);
                            final CustomWebViewDialog webViewTextDialog =
                                    new CustomWebViewDialog(requireContext());

                            webViewTextDialog.show();
                            webViewTextDialog.setTextHtml(getHtmlSafeString(strHtml));
                            webViewTextDialog.setTextTitle(DETAILS);
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

        private String getValue(int row, String... keys) {
            for (String key : keys) {
                String value = hashArray.get(row).get(key);
                if (value != null) {
                    return value;
                }
            }
            return "";
        }

        //click on symbol header
        private View getFirstHeader(View convertView, ViewGroup parent) {
            inflater = LayoutInflater.from(getActivity());
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header_first, parent, false);
            }
            RelativeLayout relativeLayout = convertView.findViewById(R.id.relativeLayout);
            TextView head = (convertView.findViewById(android.R.id.text1));
            try {
                head.setText(headers[0]);
            } catch (Exception e) {
                tvNochart.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);

                e.printStackTrace();
            }

            return convertView;
        }

        //click on header other than symbol
        private View getHeader(int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header, parent, false);
            }
            TextView head = (convertView.findViewById(android.R.id.text1));
            head.setText(headers[column]);

            //Getting colum index of detail button
            if (headers[column].equalsIgnoreCase(DETAILS)) {
                btnindex = column;
            }
            return convertView;
        }

        private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_first, parent, false);
            }
            LinearLayout listLayout = convertView.findViewById(R.id.item_table_first);
            listLayout.setBackgroundResource(
                    row % 2 == 0 ? R.color.alternateBackground : R.color.white);
            final TextView tv_table_first = convertView.findViewById(R.id.tv_table_first);
            tv_table_first.setText(getDevice(row).data[column + 1]);

            return convertView;
        }

        private View getBody(final int row, int column, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_table, parent, false);
            // }
            LinearLayout listLayout = convertView.findViewById(R.id.item_table);
            listLayout.setBackgroundResource(
                    row % 2 == 0 ? R.color.alternateBackground : R.color.white);
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(
                    getDevice(row).data[column]);
            TextView text1 = convertView.findViewById(android.R.id.text1);
            text1.setBackgroundResource(0);
            return convertView;
        }

        private View getBodyWithButton(View convertView,
                                       ViewGroup parent) {
            convertView = inflater.inflate(R.layout.item_table_button, parent, false);
            ((Button) convertView.findViewById(R.id.tButton)).setText(DETAILS);
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
                widthSize = getResources().getDimensionPixelSize(R.dimen._90sdp);
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
            } else if (column == -1) {
                itemViewType = 2;
            } else {
                itemViewType = 3;
            }
            return itemViewType;
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

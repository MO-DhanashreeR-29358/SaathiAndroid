package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.mosl.commons.data.UiModel;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.saathi.api.model.dashboard.client.ClientRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMDecimalValueFormater;
import com.mosl.tabapp.common.CRMValueFormatter;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.model.dashboard.WidgetType;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import io.reactivex.disposables.Disposable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 2/27/2017.
 */

public class TrendsTabFragment extends MOSLCommonFragment {
    private String CHART_TYPE_LINE = "SpLine";
    private String CHART_TYPE_COLUMN = "Coloumn";
    private String[] mMonths;

    View trendsView;
    private List<WidgetType> widgetTypeList = new ArrayList<>();
    private DashboardModel dashboardModel;
    private List<DashboardGroupModel> subWidgetModelList;
    static Context mContext;
    private RecyclerView horizontalRecyclerView;
    private HorizontalAdapter horizontalAdapter;
    private int[] VORDIPLOM_COLORS = {
            Color.rgb(255, 21, 0), Color.rgb(38, 127, 183), Color.rgb(0, 177, 207),
            Color.rgb(22, 225, 143), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
            Color.rgb(254, 46, 46), Color.rgb(255, 21, 0), Color.rgb(38, 127, 183), Color.rgb(0, 177, 207),
            Color.rgb(22, 225, 143), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
            Color.rgb(254, 46, 46), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
            Color.rgb(254, 46, 46)
    };
    private List<WidgetModel> widgetModelListNew;
    public final static String[] ignoreList = new String[]{"MonthOnMonthTrend", "ChartType"};
    private TableLayout tableLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        trendsView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_trends);
        hideAppTitle(getClass().toString());
        setUpView(trendsView);
        setUserVisibleHint(true);
        return trendsView;

    }

    public static TrendsTabFragment newInstance(Context context) {
        TrendsTabFragment fragment = new TrendsTabFragment();

        return fragment;
    }


    private void setUpView(View trendsView) {
        widgetModelListNew = new ArrayList<>();
        subWidgetModelList = new ArrayList<>();
        try {
            dashboardModel = (DashboardModel) this.getArguments().getSerializable(DASHBOARD_MODEL);
            subWidgetModelList = dashboardModel.getGroupModel(DashboardSystemModel.getMonthTrends()).getSubGroupList();
            if (subWidgetModelList.size() < 3) {
                // for blank tab adding
                subWidgetModelList.add(new DashboardGroupModel());
                subWidgetModelList.add(new DashboardGroupModel());
            } else if (subWidgetModelList.size() < 4) {
                subWidgetModelList.add(new DashboardGroupModel());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        horizontalRecyclerView = (RecyclerView) trendsView.findViewById(R.id.horizontalRecyclerView);
        horizontalAdapter = new HorizontalAdapter(subWidgetModelList);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getMainActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        horizontalRecyclerView.setAdapter(horizontalAdapter);
        horizontalAdapter.notifyDataSetChanged();
        tableLayout = (TableLayout) trendsView.findViewById(R.id.tableMData);

    }


    YAxis rightAxis;
    CombinedChart mChart;

    private void setChartData(List<List<ChartDataModel>> arrayLists) {
        try {
            LayoutInflater inFla = getActivity().getLayoutInflater();
            for (int i = 0; i < arrayLists.size(); i++) {
                View tableRow = inFla.inflate(R.layout.combined_chartlayout, null);
                mChart = (CombinedChart) tableRow.findViewById(R.id.mChart);

                mChart.getDescription().setEnabled(false);
                mChart.setBackgroundColor(Color.WHITE);
                mChart.setDrawGridBackground(false);
                mChart.setDrawBarShadow(false);
                mChart.setHighlightFullBarEnabled(false);
                mChart.setDrawOrder(new DrawOrder[]{
                        DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
                });
                Legend l = mChart.getLegend();
                l.setWordWrapEnabled(true);
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(false);
                l.setTextSize(13f);

                rightAxis = mChart.getAxisRight();
                rightAxis.setValueFormatter(new LargeValueFormatter());
                rightAxis.setDrawGridLines(false);
                rightAxis.setAxisMinimum(0f);
                YAxis leftAxis = mChart.getAxisLeft();
                leftAxis.setDrawGridLines(false);
                leftAxis.setValueFormatter(new LargeValueFormatter());
                leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                XAxis xAxis = mChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                xAxis.setDrawGridLines(false);
                xAxis.setAxisMinimum(0f);
                xAxis.setGranularity(1f);
                xAxis.setDrawAxisLine(true);
                CombinedData data = new CombinedData();

                LineData lineData = generateLineData(xAxis, arrayLists.get(i));
                if (lineData != null) {
                    data.setData(generateLineData(xAxis, arrayLists.get(i)));

                }

                BarData barData = generateBarData(xAxis, arrayLists.get(i));
                if (barData != null) {
                    data.setData(generateBarData(xAxis, arrayLists.get(i)));
                }

                xAxis.setAxisMaximum(data.getXMax() + 0.25f);
                mChart.setData(data);
                mChart.invalidate();

                tableLayout.addView(tableRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && trendsView != null) {
                //webServiceCall();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateAxisLabel(ChartDataModel data, XAxis xAxis) {
        if (data != null) {
            LinkedHashMap map = data.getMonthData();

            if (data.getMonthData() != null && map.size() != 0) {
                Set<String> monthNames = map.keySet();
                mMonths = monthNames.toArray(new String[monthNames.size()]);
            }
        }

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                try {
                    if (value < 0) {
                        return "";
                    } else {
                        return mMonths[(int) value % mMonths.length];
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private boolean dataFoundFlag;

    private LineData generateLineData(XAxis xAxis, List<ChartDataModel> arrayList) {
        List<ChartDataModel> dataList = arrayList;
        List<ChartDataModel> barDataList = new ArrayList<>();

        ChartDataModel lineData = null;
        for (ChartDataModel model : dataList) {

            if (model.getChartType().equals(CHART_TYPE_LINE)) {
                lineData = model;
            }
        }

        if (lineData == null) return null;
        generateAxisLabel(lineData, xAxis);
        int itemcount = lineData.getMonthData().size();

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();
        int index;
        LineDataSet set = null;
        for (index = 0; index < itemcount; index++) {
            entries.add(new Entry(index + 0.05f, lineData.getMonthData().get(mMonths[index])));

            set = new LineDataSet(entries, lineData.getLegendText());
            set.setColor(Color.rgb(64, 0, 255));
            set.setLineWidth(2.5f);
            set.setCircleColor(Color.rgb(240, 238, 70));
            set.setCircleRadius(5f);
            set.setFillColor(Color.rgb(64, 0, 255));
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setDrawValues(true);
            set.setValueTextSize(10f);
            set.setValueTextColor(Color.rgb(64, 0, 255));
            if (lineData.getMonthData().get(mMonths[index]) < 1.00) {
                rightAxis.setValueFormatter(new CRMIAxisValueFormatter());
                // if last value below 1
                dataFoundFlag = true;
            } else {
                if (dataFoundFlag) {
                    rightAxis.setValueFormatter(new CRMIAxisValueFormatter());
                    dataFoundFlag = false;
                } else {
                    rightAxis.setValueFormatter(new LargeValueFormatter());
                }
            }
            set.setValueFormatter(new CRMDecimalValueFormater());
            set.setAxisDependency(YAxis.AxisDependency.RIGHT);
        }
        d.addDataSet(set);

        return d;
    }


    private int getColor(int index) {
        switch (index) {
            case 0:
                return Color.rgb(60, 220, 78);
            case 1:
                return Color.rgb(61, 165, 255);
            default:
                return Color.rgb(61, 165, 255);
        }
    }

    private BarData generateBarData(XAxis xAxis, List<ChartDataModel> arrayList) {
        List<ChartDataModel> dataList = arrayList;
        List<ChartDataModel> barDataList = new ArrayList<>();

        for (ChartDataModel model : dataList) {

            if (model.getChartType().equals(CHART_TYPE_COLUMN)) {
                generateAxisLabel(model, xAxis);
                barDataList.add(model);
            }
        }

        if (barDataList.size() == 0) return null;

        BarData d = new BarData();

        List<ArrayList<BarEntry>> entryList = new ArrayList<>();

        for (int x = 0; x < barDataList.size(); x++) {
            ArrayList<BarEntry> entry = new ArrayList<BarEntry>();
            entryList.add(entry);
        }
        BarDataSet set1 = null;
        for (int x = 0; x < barDataList.size(); x++) {
            ChartDataModel model = barDataList.get(x);
            ArrayList<BarEntry> entries = entryList.get(x);
            for (int y = 0; y < mMonths.length; y++) {
                if (barDataList.size() == 1) {
                    entries.add(new BarEntry(y + 0.2f, model.getMonthData().get(mMonths[y])));
                } else {
                    entries.add(new BarEntry(y, model.getMonthData().get(mMonths[y])));
                }
                set1 = new BarDataSet(entries, model.getLegendText());
                set1.setColor(getColor(x));
                set1.setValueTextColor(getColor(x));
                set1.setValueTextSize(10f);
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                if (model.getMonthData().get(mMonths[y]) % 1 == 0) {
                    set1.setValueFormatter(new CRMValueFormatter());
                } else {
                    set1.setValueFormatter(new CRMDecimalValueFormater());
                }

            }
            d.addDataSet(set1);
        }

        float groupSpace = 0.1f;
        float barSpace = 0.025f; // x2 dataset
        float barWidth = 0.35f; // x2 dataset
        // make this BarData object grouped
        d.setBarWidth(barWidth);
        if (barDataList.size() > 1) {
            d.groupBars(0, groupSpace, barSpace); // start at x = 0

        } else {

        }

        return d;
    }


    private class ChartDataModel {
        private String legendText;
        private String chartType;
        private LinkedHashMap<String, Float> monthData = new LinkedHashMap<>();

        public String getLegendText() {
            return legendText;
        }

        public void setLegendText(String legendText) {
            this.legendText = legendText;
        }

        public String getChartType() {
            return chartType;
        }

        public void setChartType(String chartType) {
            this.chartType = chartType;
        }

        public LinkedHashMap<String, Float> getMonthData() {
            return monthData;
        }

        public void setMonthData(LinkedHashMap<String, Float> monthData) {
            this.monthData = monthData;
        }
    }

    private void webServiceCall(final String widgetSystemName) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            String businessProfileId = dashboardModel.getBusinessProfileIdForDashboard();
            ClientRequest request = new ClientRequest(
                businessProfileId,
                null,
                widgetSystemName,
                dashboardModel.getProductId(),
                dashboardModel.getEmployeeCode()
            );

            Disposable subscribe = saathiApi.getDashboardData(request)
                .map(responseS -> {

                    JSONObject response = new JSONObject(responseS);
                    String status = response.get(STATUS).toString();
                    if (status.equalsIgnoreCase("true")) {
                        return response.getJSONObject(RESPONSE).getJSONArray("Table");
                    } else {
                        String message = response.get(MESSAGE).toString();
                        throw new ServiceFailureException(message, message);
                    }
                })
                .compose(applyUiModel())
                .subscribe(this::renderTrends);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderTrends(UiModel<JSONArray> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        JSONArray jsonArray = uiModel.data;
        if (jsonArray != null) {
            try {
                renderGraph(jsonArray);
            } catch (Exception e) {
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    private void renderGraph(JSONArray jsonArray) throws JSONException {
        int j = 0;
        List<List<ChartDataModel>> dynamicList = new ArrayList<>();
        List<ChartDataModel> dataList = new ArrayList<ChartDataModel>();
        String paramValue;
        if (jsonArray.length() > 0) {
            dataList.clear();
            for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                ChartDataModel model = new ChartDataModel();

                JSONObject object = jsonArray.getJSONObject(ctr);
                JSONArray paramNameArr = object.names();
                for (int y = 0; y < paramNameArr.length(); y++) {
                    String paramName = paramNameArr.getString(y);
                    if (paramName.equals("Parameters")) {
                        paramValue = object.getString(paramName);
                        model.setLegendText(paramValue);
                    } else if (paramName.equals("ChartType")) {
                        paramValue = object.getString(paramName);
                        model.setChartType(paramValue);
                    } else {
                        if (paramName.equalsIgnoreCase("WidgetType")) {
                        } else {
                            paramValue = object.getString(paramName);
                            float monthData = Float.parseFloat(paramValue);
                            model.getMonthData().put(paramName, monthData);
                        }
                    }
                }

                dataList.add(model);
            }
        }

        dynamicList.add(dataList);
        setChartData(dynamicList);
    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private List<DashboardGroupModel> widgetModelList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvWidgetName;
            public ImageView imageView;
            public RelativeLayout llWidgetList;
            public CardView card_view;

            public MyViewHolder(View view) {
                super(view);
                tvWidgetName = (TextView) view.findViewById(R.id.tvWidgetName);
                imageView = (ImageView) view.findViewById(R.id.imageView);
                llWidgetList = (RelativeLayout) view.findViewById(R.id.llWidgetList);
                card_view = (CardView) view.findViewById(R.id.card_view);
            }
        }


        public HorizontalAdapter(List<DashboardGroupModel> widgetModelList) {
            this.widgetModelList = widgetModelList;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_target_achievement_list, parent, false);

            return new MyViewHolder(itemView);
        }

        public void selectCategory(String name) {
            for (DashboardGroupModel model : widgetModelList) {
                if (name.equals(model.getSystemName())) {
                    model.setSelected(true);
                } else {
                    model.setSelected(false);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final DashboardGroupModel widgetModel = widgetModelList.get(position);
            if (widgetModel.isSelected()) {

                holder.tvWidgetName.setTextColor(getResources().getColor(R.color.white));
                holder.imageView.setImageResource(R.drawable.mhovertrend);
                holder.tvWidgetName.setText(widgetModel.getDisplayName());
                holder.card_view.setCardBackgroundColor(getResources().getColor(R.color.secondaryBackground));
                widgetModelListNew = subWidgetModelList.get(position).getWidgetList();
                setImage(widgetModelListNew.get(0).getIconUrl2().replaceAll(" ", "%20"), holder.imageView);

                tableLayout.removeAllViews();

                for (WidgetModel model : widgetModelListNew) {
                    String widgetSystemName = model.getSystemName();
                    webServiceCall(widgetSystemName);
                }
            } else {
                holder.tvWidgetName.setTextColor(getResources().getColor(R.color.txt_dselect));
                if (!widgetModel.getDisplayName().isEmpty()) {
                    holder.imageView.setImageResource(R.drawable.mtrends);
                }
                holder.tvWidgetName.setText(widgetModel.getDisplayName());
                holder.card_view.setCardBackgroundColor(getResources().getColor(R.color.white));
                widgetModelListNew = subWidgetModelList.get(position).getWidgetList();
                setImage(widgetModelListNew.get(0).getIconUrl1().replaceAll(" ", "%20"), holder.imageView);

            }
            holder.llWidgetList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!widgetModel.getDisplayName().isEmpty()) {
                        selectCategory(widgetModel.getSystemName());
                        holder.imageView.setImageResource(R.drawable.mhovertrend);
                        notifyDataSetChanged();
                    }
                }
            });

        }

        public void setImage(String imageUrl, final ImageView imageView) {
            Glide.with(context).load(imageUrl)
                    //remove Chache
                    .asBitmap().placeholder(R.drawable.ic_white).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    //circularBitmapDrawable.setCircular(true);
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
        }

        @Override
        public int getItemCount() {
            return widgetModelList.size();
        }
    }

    public class CRMIAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        public CRMIAxisValueFormatter() {
            mFormat = new DecimalFormat("########0.00");
        }


        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value);
        }
    }

}


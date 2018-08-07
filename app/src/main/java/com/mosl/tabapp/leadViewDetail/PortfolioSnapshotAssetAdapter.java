package com.mosl.tabapp.leadViewDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mosl.tabapp.R;
import com.mosl.tabapp.constants.MOSLConstants;

import java.util.ArrayList;
import java.util.HashMap;


public class PortfolioSnapshotAssetAdapter extends BaseAdapter {

    private Context mContext;
    TextView tvProduct, tvInvestVal, tvMarketVal, tvGainLoss, tvPerGainLoss, tvProductType;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private static LayoutInflater inflater = null;
    String prod, gainLoss, gainLossPer, investVal, marketVal;

    public PortfolioSnapshotAssetAdapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
        mContext = c;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.item_list_assest_portfolio, parent, false);
        setupViews(view);

        prod = arrayList.get(position).get(MOSLConstants.PRODUCT).equals("") ? "" : arrayList.get(position).get(MOSLConstants.PRODUCT);
        gainLoss = arrayList.get(position).get(MOSLConstants.OVERALL_GAIN_LOSS).equals("") ? "" : MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.OVERALL_GAIN_LOSS);
        gainLossPer = arrayList.get(position).get(MOSLConstants.PERCENTAGE_GAIN_LOSS).equals("") ? "" : arrayList.get(position).get(MOSLConstants.PERCENTAGE_GAIN_LOSS);
        marketVal = arrayList.get(position).get(MOSLConstants.INVEST_AMT).equals("") ? "" : MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.INVEST_AMT);
        investVal = arrayList.get(position).get(MOSLConstants.PURCHASE_VALUE).equals("") ? "" : MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.PURCHASE_VALUE);

        tvProduct.setText(prod);
        tvGainLoss.setText(gainLoss);
        tvPerGainLoss.setText(gainLossPer);
        tvInvestVal.setText(investVal);
        tvMarketVal.setText(marketVal);
        if (tvProduct.getText().equals(MOSLConstants.MUTUAL_FUND)) {
            tvProductType.setText(arrayList.get(position).get(MOSLConstants.PRODUCT_TYPE));
        }


        return view;
    }

    private void setupViews(View view) {
        tvProduct = (TextView) view.findViewById(R.id.tvProduct);
        tvInvestVal = (TextView) view.findViewById(R.id.tvInvestVal);
        tvMarketVal = (TextView) view.findViewById(R.id.tvMarketVal);
        tvGainLoss = (TextView) view.findViewById(R.id.tvGainLoss);
        tvPerGainLoss = (TextView) view.findViewById(R.id.tvPerGainLoss);
        tvProductType = (TextView) view.findViewById(R.id.tvProductType);
    }
}


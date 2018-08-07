package com.mosl.tabapp.leadViewDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mosl.tabapp.R;
import com.mosl.tabapp.constants.MOSLConstants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Owner on 1/13/2016.
 */
public class PortfolioSnapshotBrokAdapter extends BaseAdapter implements MOSLConstants {

    private final LayoutInflater inflater;
    String prod, prodID, gainLoss, gainLossPer, investVal, marketVal;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private Context mContext;

    public PortfolioSnapshotBrokAdapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
        inflater = LayoutInflater.from(c);
        this.arrayList = arrayList;

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_list_broking_portfolio, parent, false);
            holder.tvProduct = (TextView) view.findViewById(R.id.tvProduct);
            holder.tvInvestVal = (TextView) view.findViewById(R.id.tvInvestVal);
            holder.tvMarketVal = (TextView) view.findViewById(R.id.tvMarketVal);
            holder.tvGainLoss = (TextView) view.findViewById(R.id.tvGainLoss);
            holder.tvPerGainLoss = (TextView) view.findViewById(R.id.tvPerGainLoss);
            holder.llListItem = (LinearLayout) view.findViewById(R.id.llListItem);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        prod = arrayList.get(position).get(MOSLConstants.PRODUCT).equals("") ? "" : arrayList.get(position).get(MOSLConstants.PRODUCT);
        prodID = arrayList.get(position).get(MOSLConstants.PRODUCT_ID).equals("") ? "" : arrayList.get(position).get(MOSLConstants.PRODUCT_ID);
        gainLoss = arrayList.get(position).get(MOSLConstants.OVERALL_GAIN_LOSS).equals("") ? "" : MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.OVERALL_GAIN_LOSS);
        gainLossPer = arrayList.get(position).get(MOSLConstants.PERCENTAGE_GAIN_LOSS).equals("") ? "" : arrayList.get(position).get(MOSLConstants.PERCENTAGE_GAIN_LOSS);
        investVal = arrayList.get(position).get(MOSLConstants.INVEST_AMT).equals("") ? "" : MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.INVEST_AMT);
        marketVal = arrayList.get(position).get(MOSLConstants.PURCHASE_VALUE).equals("") ? "" : MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.PURCHASE_VALUE);


        holder.tvProduct.setText(prod);
        holder.tvGainLoss.setText(gainLoss);
        holder.tvPerGainLoss.setText(gainLossPer);
        holder.tvInvestVal.setText(investVal);
        holder.tvMarketVal.setText(marketVal);

        return view;
    }

    public String getProdName() {
        return prod;

    }

    static class Holder {
        TextView tvProduct, tvInvestVal, tvMarketVal, tvGainLoss, tvPerGainLoss;
        LinearLayout llListItem;
    }
    /*private void setupViews(View view) {
        txt_Products = (TextView) view.findViewById(R.id.txtProducts);
        txt_Invest_Value = (TextView) view.findViewById(R.id.txtInvestValue);
        txt_Purchase_value = (TextView) view.findViewById(R.id.txtPurchaseValue);
        txt_Gain_Loss = (TextView) view.findViewById(R.id.txtGainLoss);
        txt_Per_Gain_Loss = (TextView) view.findViewById(R.id.txtPerGainLoss);
    }*/


}


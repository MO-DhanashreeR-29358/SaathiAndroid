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

/**
 * Created by Shefali on 2/10/2016.
 */
public class PortfolioPLAssetAdapter extends BaseAdapter implements MOSLConstants{

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private final LayoutInflater inflater;
    String prod, STGainLoss, LTGainLoss, totalGainLoss;

    public PortfolioPLAssetAdapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
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
            view = inflater.inflate(R.layout.item_lst_pl_asset_portfolio, parent, false);
            holder.tvProduct = (TextView) view.findViewById(R.id.tvProduct);
            holder.tvSTGainLoss = (TextView) view.findViewById(R.id.tvSTGainLoss);
            holder.tvLTGainLoss = (TextView) view.findViewById(R.id.tvLTGainLoss);
            holder.tvTotalGainLoss = (TextView) view.findViewById(R.id.tvTotalGainLoss);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        prod = arrayList.get(position).get(MOSLConstants.PRODUCT).equals("") ? "" : arrayList.get(position).get(MOSLConstants.PRODUCT);
        STGainLoss = arrayList.get(position).get(MOSLConstants.STGL).equals("") ? "" : MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.STGL);
        LTGainLoss = arrayList.get(position).get(MOSLConstants.LTGL).equals("") ? "" : MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.LTGL);
        totalGainLoss = arrayList.get(position).get(MOSLConstants.TOTAL_GAIN_LOSS).equals("") ? "" : MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.TOTAL_GAIN_LOSS);


        holder.tvProduct.setText(prod);
        holder.tvSTGainLoss.setText(STGainLoss);
        holder.tvLTGainLoss.setText(LTGainLoss);
        holder.tvTotalGainLoss.setText(totalGainLoss);

        return view;
    }

    static class Holder {
        TextView tvProduct, tvSTGainLoss, tvLTGainLoss, tvTotalGainLoss, tvProductType;
    }


}


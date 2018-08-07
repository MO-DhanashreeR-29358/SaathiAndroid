package com.mosl.tabapp.leadViewDetail;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mosl.tabapp.R;
import com.mosl.tabapp.constants.MOSLConstants;

import java.util.ArrayList;
import java.util.HashMap;


public class PortfolioPLLimitsAdapter extends BaseAdapter implements MOSLConstants{

    private final LayoutInflater inflater;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private Context mContext;

    public PortfolioPLLimitsAdapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
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
            view = inflater.inflate(R.layout.item_lst_pl_limits_portfolio, parent, false);
            holder.tvProduct = (TextView) view.findViewById(R.id.tvProduct);
            holder.tvAmt = (TextView) view.findViewById(R.id.tvAmt);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        if (arrayList.get(position).get(MOSLConstants.CASH).equalsIgnoreCase("Total")) {
            holder.tvProduct.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.tvAmt.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            holder.tvProduct.setText(arrayList.get(position).get(MOSLConstants.CASH));
            holder.tvAmt.setText(MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.AMOUNT));
        } else {
            holder.tvProduct.setText(arrayList.get(position).get(MOSLConstants.CASH));
            holder.tvAmt.setText(MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.AMOUNT));
        }


        return view;
    }

    static class Holder {
        TextView tvProduct, tvAmt;
    }


}

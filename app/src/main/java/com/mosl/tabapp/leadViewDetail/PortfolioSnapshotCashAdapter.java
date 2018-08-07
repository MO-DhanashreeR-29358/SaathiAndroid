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


public class PortfolioSnapshotCashAdapter extends BaseAdapter {

    private Context mContext;
    TextView tvProduct, tvTotalBalance;
    String prod, totalBal;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private static LayoutInflater inflater = null;

    public PortfolioSnapshotCashAdapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
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
        view = inflater.inflate(R.layout.item_list_cash_portfolio, parent, false);
        setupViews(view);
        prod = arrayList.get(position).get(MOSLConstants.PRODUCT).equals("") ? "" : arrayList.get(position).get(MOSLConstants.PRODUCT);
        totalBal = arrayList.get(position).get(MOSLConstants.TOTAL).equals("") ? "" : MOSLConstants.RUPEE + " " + arrayList.get(position).get(MOSLConstants.TOTAL);
        if (arrayList.get(position).get(MOSLConstants.PRODUCT).equalsIgnoreCase("Total")) {
            tvProduct.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvTotalBalance.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvProduct.setText(prod);
            tvTotalBalance.setText(totalBal);
        } else {
            tvProduct.setText(prod);
            tvTotalBalance.setText(totalBal);
        }


        return view;
    }

    private void setupViews(View view) {
        tvProduct = (TextView) view.findViewById(R.id.txtProducts);
        tvTotalBalance = (TextView) view.findViewById(R.id.txtTotalbalance);

    }
}


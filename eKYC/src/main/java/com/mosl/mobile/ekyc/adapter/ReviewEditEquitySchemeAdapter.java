package com.mosl.mobile.ekyc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.constants.MOSLConstants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Vijay on 10/14/2016.
 */

public class ReviewEditEquitySchemeAdapter extends BaseAdapter implements MOSLConstants {
    private final LayoutInflater inflater;
    Context mContext;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    String cat, profileName, product;

    public ReviewEditEquitySchemeAdapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
        inflater = LayoutInflater.from(c);
        mContext = c;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        view = inflater.inflate(R.layout.item_list_review_edit_scheme, parent, false);
        Holder holder = new Holder();
        holder.tvSchemeName = (TextView) view.findViewById(R.id.tvSchemeName);
        holder.tvSchemeCAT = (TextView) view.findViewById(R.id.tvSchemeCAT);
        holder.tvSchemeProfileName = (TextView) view.findViewById(R.id.tvSchemeProfileName);
        holder.tvSchemeProduct = (TextView) view.findViewById(R.id.tvSchemeProduct);
        holder.llItemListReview = (LinearLayout) view.findViewById(R.id.llItemListReview);

        if (position % 2 == 1) {
            holder.llItemListReview.setBackgroundColor(Color.parseColor("#f3eff0"));
        } else {
            holder.llItemListReview.setBackgroundColor(Color.parseColor("#f8f6f7"));
        }
        cat = arrayList.get(position).get(CAT).equals("") ? "" : arrayList.get(position).get(CAT);
        profileName = arrayList.get(position).get(PROFILE_NAME).equals("") ? "" : arrayList.get(position).get(PROFILE_NAME);
        product = arrayList.get(position).get(PRODUCT_REVIEW_SCHEME).equals("") ? "" : arrayList.get(position).get(PRODUCT_REVIEW_SCHEME);

        holder.tvSchemeName.setText(cat);
        holder.tvSchemeCAT.setText(profileName);
        holder.tvSchemeProfileName.setText(profileName);
        holder.tvSchemeProduct.setText(product);
        return view;
    }


    class Holder {
        TextView tvSchemeName, tvSchemeCAT, tvSchemeProfileName, tvSchemeProduct;
        LinearLayout llItemListReview;
    }
}

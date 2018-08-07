package com.mosl.mobile.ekyc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.constants.MOSLConstants;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mosl.mobile.ekyc.constants.MOSLConstants.MICR;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.PROFILE_NAME;

/**
 * Created by Shefali on 05/12/16.
 */

public class MFBankListAdapter extends BaseAdapter implements MOSLConstants {
    private final LayoutInflater inflater;
    Context mContext;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    public MFBankListAdapter(Context c, ArrayList<HashMap<String, String>> arrayList) {
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
        view = inflater.inflate(R.layout.item_list_mf_bank_saved, parent, false);
        Holder holder = new Holder();
        holder.tvBankName = (TextView) view.findViewById(R.id.tvBankName);
        holder.tvBranchName = (TextView) view.findViewById(R.id.tvBranchName);
        holder.tvMICR = (TextView) view.findViewById(R.id.tvMICR);
        holder.tvIFSC = (TextView) view.findViewById(R.id.tvIFSC);
        holder.llItemMFBankDetail = (LinearLayout) view.findViewById(R.id.llItemMFBankDetail);
        holder.rbBankSelect = (RadioButton) view.findViewById(R.id.rbBankSelect);
        holder.ivBankDelete = (ImageView) view.findViewById(R.id.ivBankDelete);

        if (position % 2 == 1) {
            holder.llItemMFBankDetail.setBackgroundColor(Color.parseColor("#f3eff0"));
        } else {
            holder.llItemMFBankDetail.setBackgroundColor(Color.parseColor("#f8f6f7"));
        }
        String bankName = arrayList.get(position).get(BANK_NAME).equals("") ? "" : arrayList.get(position).get(BANK_NAME);
        String branchName = arrayList.get(position).get(BRANCH_NAME).equals("") ? "" : arrayList.get(position).get(BRANCH_NAME);
        String MICR = arrayList.get(position).get(MOSLConstants.MICR).equals("") ? "" : arrayList.get(position).get(MOSLConstants.MICR);
        String IFSC = arrayList.get(position).get(MOSLConstants.IFSC).equals("") ? "" : arrayList.get(position).get(MOSLConstants.IFSC);

        holder.tvBankName.setText(bankName);
        holder.tvBranchName.setText(branchName);
        holder.tvMICR.setText(MICR);
        holder.tvIFSC.setText(IFSC);
        return view;
    }


    class Holder {
        TextView tvBankName, tvBranchName, tvMICR, tvIFSC;
        LinearLayout llItemMFBankDetail;
        RadioButton rbBankSelect;
        ImageView ivBankDelete;
    }
}


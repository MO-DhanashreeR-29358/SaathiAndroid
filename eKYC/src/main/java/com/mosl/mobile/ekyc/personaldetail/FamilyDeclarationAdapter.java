package com.mosl.mobile.ekyc.personaldetail;

import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.EkycAppUtil;
import com.mosl.mobile.ekyc.model.FamilyDeclarationModel;

/**
 * Created by Featsystem on 19-06-2017.
 */


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FamilyDeclarationAdapter extends RecyclerView.Adapter<FamilyDeclarationAdapter.MyViewHolder> {

    private List<FamilyDeclarationModel> declarationModelList;
    private ArrayAdapter arrayAdapter;
    String selectedFamilyMember;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtClientName, txtClientCode, txtDpid;
        public Spinner spRelation;


        public MyViewHolder(View view) {
            super(view);
            txtClientName = (TextView) view.findViewById(R.id.txtClientName);
            txtClientCode = (TextView) view.findViewById(R.id.txtClientCode);
            txtDpid = (TextView) view.findViewById(R.id.txtDpid);
            spRelation = (Spinner) view.findViewById(R.id.spinnerRelation);

        }
    }


    public FamilyDeclarationAdapter(List<FamilyDeclarationModel> declarationModelList, ArrayAdapter arrayAdapter) {
        this.arrayAdapter = arrayAdapter;
        this.declarationModelList = declarationModelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_family_decleration, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final FamilyDeclarationModel data = declarationModelList.get(position);
        holder.txtClientName.setText(data.getClientName());
        holder.txtClientCode.setText(data.getClientCode());
        holder.txtDpid.setText(data.getDpid());
        holder.spRelation.setAdapter(arrayAdapter);
        if (!(data.getEnable() == null)) {
            holder.spRelation.setEnabled(data.getEnable());
        }
        if (!EkycAppUtil.isBlank(data.getRelationship())) {
            int pos = arrayAdapter.getPosition(data.getRelationship());
            holder.spRelation.setSelection(pos);
        }

        // holder.spRelation.setEnabled(false);
        holder.spRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFamilyMember = (parent.getItemAtPosition(position).toString());
                setFamilyRelationData(data.getPosition(), selectedFamilyMember);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setFamilyRelationData(int position, String selectedFamilyMember) {

        for (FamilyDeclarationModel model : declarationModelList) {
            if (model.getPosition() == position) {
                model.setRelationship(selectedFamilyMember);
            }
        }
    }

    @Override
    public int getItemCount() {
        return declarationModelList.size();
    }
}

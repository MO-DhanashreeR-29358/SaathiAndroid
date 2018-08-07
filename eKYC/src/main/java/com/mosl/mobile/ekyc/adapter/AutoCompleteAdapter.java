package com.mosl.mobile.ekyc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.EkycUtil;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.mosl.mobile.ekyc.model.CommonModel;
import com.mosl.mobile.ekyc.model.LeadModel;
import com.mosl.mobile.ekyc.model.RoleSelectionModel;
import com.mosl.mobile.ekyc.service.WebServiceDirectCall;
import com.mosl.mobile.ekyc.service.WebServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;

/**
 * Created by Hiren on 17-09-2015.
 */
public class AutoCompleteAdapter extends BaseAdapter implements Filterable, MOSLConstants {
    FragmentActivity activity;
    Context context;
    private LayoutInflater inflater;
    private List<LeadModel> resultList = new ArrayList<>();

    public AutoCompleteAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        // this.activity = (FragmentActivity) context;
        this.context = context;
    }


    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public LeadModel getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.client_search_item, parent, false);
            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        viewColor(position, convertView);
        holderView.tvLeadName.setText(resultList.get(position).getLeadName());
        holderView.tvLeadID.setText(resultList.get(position).getId());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults filterResults = new FilterResults();
                try {
                    if (constraint != null) {
                        if (MOSLCommon.isNetworkEnabled(context)) {
                            String appType, loginID, roleMapping, businessUnit;
                            if (getAppType().equalsIgnoreCase(PP)) {
                                appType = PP;
                                roleMapping = "";
                                businessUnit = "";
                            } else {
                                appType = CRM;
                                roleMapping = RoleSelectionModel.getRoleMappingID();
                                businessUnit = RoleSelectionModel.getBusinessUnitID();
                            }

                            Map inputMap = new HashMap();
                            inputMap.put(APP_LOGIN_ID, getAppLoginID());
                            inputMap.put(SEARCH_DATA, constraint);
                            inputMap.put(APPLICATION_TYPE, appType);
                            inputMap.put(ROLE_MAPPING_ID, roleMapping);
                            inputMap.put(BUSINESS_UNIT_ID, businessUnit);

                                /*{
                                "EKYCSEARCHRESPONSE":[
                                {
                                    "ID":965972,
                                        "LEADNAME":"PANKAJ SURESH THAKKARSB OF SUNIL PANDURANG MANTRI "
                                }]}*/

                            String input = EkycUtil.encrypt(WebServiceHandler.getInputBody(inputMap));
                            String flag = EkycUtil.encrypt(EXISINGLEADSEARCHRESPONSE);
                            String category = EkycUtil.encrypt(SIGNUP);
                            JSONObject response = WebServiceDirectCall.getData(input, flag, category);
                            ArrayList<LeadModel> modelList = new ArrayList<>();
                            if (response.length() != 0) {
                                JSONArray jsonArray = response.getJSONArray(EKYCSEARCHRESPONSE);
                                if (jsonArray.length() == 0) {
                                    String id = "";
                                    String leadName = "No record found";
                                    LeadModel model = new LeadModel(id, leadName);
                                    modelList.add(model);
                                }
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("ID");
                                    String leadName = object.getString("DISPLAYTEXT");
                                    LeadModel model = new LeadModel(id, leadName);
                                    modelList.add(model);

                                }
                            }
                            filterResults.values = modelList;
                            filterResults.count = modelList.size();

                        } else {
                            MOSLCommon.alertDInternet(context);
                            // MOSLCommon.showToastMsg(context,"No result");
                        }


                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<LeadModel>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private void viewColor(int position, View convertView) {
        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    class HolderView {
        TextView tvLeadID, tvLeadName;

        public HolderView(View parent) {
            tvLeadID = (TextView) parent.findViewById(R.id.tvLeadID);
            tvLeadName = (TextView) parent.findViewById(R.id.tvLeadName);
        }
    }
}
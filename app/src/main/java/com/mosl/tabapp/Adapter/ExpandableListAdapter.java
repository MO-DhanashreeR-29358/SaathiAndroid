/*
package com.mosl.mobile.crm.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosl.mobile.crm.R;
import KeyValuePairModel;
import SecondClickHeaderModel;
import SecondClickModel;

import java.util.List;

*/
/**
 * Created by raju on 3/2/2017.
 *//*


public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    List<SecondClickModel> data;
    private int groupItemSelection = -1;
    private int childItemSelection = -1;

    public ExpandableListAdapter(Activity context, List<SecondClickModel> data) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    public KeyValuePairModel getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getBottomModel().get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ExpandableListAdapter.ChildViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.second_click_child_item, parent, false);
            holder = new ExpandableListAdapter.ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ExpandableListAdapter.ChildViewHolder) convertView.getTag();
        }
        KeyValuePairModel rowdata = getChild(groupPosition, childPosition);


        holder.tvTitle.setText(rowdata.getKey());
        holder.tvValue.setText(rowdata.getValue());
        */
/*if (isLastChild) {
            holder.arrowImageView.setImageResource(R.drawable.up_arrow_button);
        } else {
            holder.arrowImageView.setVisibility(View.GONE);
           // holder.arrowImageView.setImageResource(R.drawable.down_arrow_button);
        }*//*



        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getBottomModel().size(); //todo
    }

    public SecondClickHeaderModel getGroup(int groupPosition) {
        return data.get(groupPosition).getHeaderModel();
    }

    public int getGroupCount() {
        return data.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int position, boolean isExpanded, View convertView, ViewGroup parent) {
        ExpandableListAdapter.HeaderViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.second_click_group_row, parent, false);
            holder = new ExpandableListAdapter.HeaderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ExpandableListAdapter.HeaderViewHolder) convertView.getTag();
        }
        SecondClickHeaderModel header = getGroup(position);


        holder.arrowImageView.setImageDrawable(null);

        if (isExpanded) {
            //  holder.arrowImageView.setVisibility(View.GONE);
            holder.arrowImageView.setImageResource(R.drawable.up_arrow_button);
        } else {
            holder.arrowImageView.setImageResource(R.drawable.down_arrow_button);
        }

        holder.tvName.setText(header.getName());
        holder.tvID.setText(header.getId());
        holder.tvUCID.setText(header.getUcid());
        holder.tvFamilyUCID.setText(header.getFamilyUcid());
        holder.tvActDate.setText(header.getActDate());
        holder.tvCurrentStage.setText(header.getCurrentStage());
        holder.tvLastSpokenDate.setText(header.getLastSpokenDate());
        holder.tvCurrentStageSince.setText(header.getCurrentStageSince());
        return convertView;

    }

    public void setMenuSelection(int groupSel) {
        this.groupItemSelection = groupSel;
        this.childItemSelection = -1;
    }

    public void setChildMenuSelection(int groupSel, int childSelection) {
        this.groupItemSelection = groupSel;
        this.childItemSelection = childSelection;

    }

    class HeaderViewHolder {
        private final TextView tvName, tvID, tvUCID, tvFamilyUCID, tvActDate, tvCurrentStage, tvLastSpokenDate, tvCurrentStageSince;
        private final ImageView arrowImageView;


        public HeaderViewHolder(View row) {
            tvName = (TextView) row.findViewById(R.id.tvName);
            tvID = (TextView) row.findViewById(R.id.tvID);
            tvUCID = (TextView) row.findViewById(R.id.tvUCID);
            tvFamilyUCID = (TextView) row.findViewById(R.id.tvFamilyUCID);
            tvActDate = (TextView) row.findViewById(R.id.tvActDate);
            tvCurrentStage = (TextView) row.findViewById(R.id.tvCurrentStage);
            tvLastSpokenDate = (TextView) row.findViewById(R.id.tvLastSpokenDate);
            tvCurrentStageSince = (TextView) row.findViewById(R.id.tvCurrentStageSince);
            arrowImageView = (ImageView) row.findViewById(R.id.arrowImageView);
        }


    }


    class ChildViewHolder {
        private final TextView tvTitle, tvValue;
        private final RelativeLayout rlchilditem;
        //  private final ImageView arrowImageView;

        public ChildViewHolder(View row) {
            rlchilditem = (RelativeLayout) row.findViewById(R.id.rlchilditem);
            tvTitle = (TextView) row.findViewById(R.id.tvTitle);
            tvValue = (TextView) row.findViewById(R.id.tvValue);
            //   arrowImageView = (ImageView) row.findViewById(R.id.arrowImageView);
        }


    }
}
*/

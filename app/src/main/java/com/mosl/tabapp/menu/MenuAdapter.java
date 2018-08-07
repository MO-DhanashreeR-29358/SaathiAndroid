package com.mosl.tabapp.menu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosl.tabapp.R;

import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Shefali on 12/16/2015.
 */
public class MenuAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private final List<ScreenDetails> groupList;
    private final LayoutInflater mLayoutInflater;
    private final Map<String, Vector<SubMenu>> childCollection;
    private int groupItemSelection = -1;
    private int childItemSelection = -1;

    public MenuAdapter(Activity context, List<ScreenDetails> groups, Map<String, Vector<SubMenu>> menuCollections) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.groupList = groups;
        this.childCollection = menuCollections;
    }

    public Object getChild(int groupPosition, int childPosition) {
        ScreenDetails scr = groupList.get(groupPosition);
        return childCollection.get(scr.getTitle()).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.child_item, parent, false);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        SubMenu subMen = (SubMenu) getChild(groupPosition, childPosition);
        holder.item.setText(subMen.getTitle());
        if (subMen.isEnabled()) {
            holder.item.setTextColor(Color.BLACK);
            holder.arrowImageView.setVisibility(View.GONE);

            if (groupPosition == groupItemSelection && childPosition == childItemSelection) {
                holder.setMenuItemSelection();
            } else holder.setMenuItems();
        } else {
            holder.item.setTextColor(mContext.getResources().getColor(R.color.white_subheading));
            holder.arrowImageView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        ScreenDetails src = groupList.get(groupPosition);
        return childCollection.get(src.getTitle()).size();
    }

    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    public int getGroupCount() {
        return groupList.size();
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
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.menu_list_row, parent, false);
            holder = new HeaderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        ScreenDetails details = (ScreenDetails) getGroup(position);
        String subGroupName = details.getTitle();

        holder.groupImageView.setImageResource(details.getIcon());
        holder.headerTextView.setText(subGroupName);
        holder.arrowImageView.setImageDrawable(null);
        if (details.isParentView()) {
            if (isExpanded) {
                holder.arrowImageView.setImageResource(R.drawable.down_arrow);
            } else {
                holder.arrowImageView.setImageResource(R.drawable.left_arrow_button);
            }
        }

        if (!details.isEnabled()) {
            holder.arrowImageView.setImageResource(R.drawable.ico_lock);
        }

        if (groupItemSelection !=0 && position == groupItemSelection && !details.isParentView()) {
            holder.setHeaderSelection();
        } else {
            holder.setMenuHeaders();
        }
        if (holder.headerTextView.getText().equals("My Dashboard")){
            holder.setDashboardSelection();
        }
        return convertView;

    }

    public void setGroupMenuSelection(int groupSel) {
        this.groupItemSelection = groupSel;
        this.childItemSelection = -1;
        notifyDataSetChanged();
    }

    public void setChildMenuSelection(int groupSel, int childSelection) {
        this.groupItemSelection = groupSel;
        this.childItemSelection = childSelection;
        notifyDataSetChanged();
    }

    class HeaderViewHolder {
        private final RelativeLayout linearLay;
        private final TextView headerTextView;
        private final ImageView arrowImageView;
        private final ImageView groupImageView;

        public HeaderViewHolder(View row) {
            linearLay = (RelativeLayout) row.findViewById(R.id.main);
            groupImageView = (ImageView) row.findViewById(R.id.imgGroupImage);
            headerTextView = (TextView) row.findViewById(R.id.txtMenuNames);
            arrowImageView = (ImageView) row.findViewById(R.id.imgFocusImage);
        }

        public void setMenuHeaders() {
            if (headerTextView != null) {
                linearLay.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey_menu));
                headerTextView.setTypeface(null, Typeface.NORMAL);
            }
        }

        public void setDashboardSelection() {
            if (headerTextView != null) {
                linearLay.setBackgroundColor(mContext.getResources().getColor(R.color.secondaryBackground));
                headerTextView.setTypeface(null, Typeface.BOLD);
            }
        } public void setHeaderSelection() {
            if (headerTextView != null) {
                linearLay.setBackgroundColor(mContext.getResources().getColor(R.color.light_blue));
                headerTextView.setTypeface(null, Typeface.BOLD);
            }
        }


    }


    class ChildViewHolder {
        private final TextView item;
        private final ImageView arrowImageView;
        private final RelativeLayout linearLay;

        public ChildViewHolder(View row) {
            linearLay = (RelativeLayout) row.findViewById(R.id.childitemlayout);
            item = (TextView) row.findViewById(R.id.childitem);
            arrowImageView = (ImageView) row.findViewById(R.id.imgFocusImage);
        }

        public void setMenuItems() {
            if (item != null) {
                linearLay.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                item.setTypeface(null, Typeface.NORMAL);
            }
        }

        public void setMenuItemSelection() {
            if (item != null) {
                linearLay.setBackgroundColor(mContext.getResources().getColor(R.color.light_blue));
                item.setTypeface(null, Typeface.BOLD);
            }
        }
    }
}
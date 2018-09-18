package com.mosl.tabapp.leadViewDetail;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mosl.tabapp.R;
import com.mosl.tabapp.model.EmployeeModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sanket Gurav on 10/2/2017.
 */

public class AttendeeAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    private List<EmployeeModel> resultList = new ArrayList<>();

    public AttendeeAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public AttendeeAdapter(Context context, List<EmployeeModel> resultList) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.resultList = resultList;
    }

    public void clearAll() {
        resultList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public EmployeeModel getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final HolderView holderView;
        final EmployeeModel clientModel = resultList.get(position);
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_list_attendee, parent, false);
            holderView = new AttendeeAdapter.HolderView(convertView);
            convertView.setTag(holderView);
        } else {
            holderView = (AttendeeAdapter.HolderView) convertView.getTag();
        }
        viewColor(position, convertView);
        if (clientModel.isNoData()) {
            holderView.tvAttendeeName.setText(clientModel.getAttendeeName());
        } else {
            holderView.tvAttendeeName.setText(clientModel.getAttendeeCode() + "-" + clientModel.getAttendeeName() + "-" + clientModel.getBranchcode());

        }
        return convertView;
    }



    private void viewColor(int position, View convertView) {
        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    class HolderView {
        TextView tvAttendeeName;

        public HolderView(View parent) {
            tvAttendeeName = (TextView) parent.findViewById(R.id.tvAttendeeName);
        }
    }
}
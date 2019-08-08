package com.mosl.tabapp.bankmeeting;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mosl.tabapp.R;
import com.mosl.tabapp.model.bankmeeting.BankEmployeeData;
import java.util.ArrayList;
import java.util.List;

public class BankEmployeeSearchAdapter extends BaseAdapter {
  private Context context;
  private LayoutInflater inflater;
  private List<BankEmployeeData> resultList = new ArrayList<>();

  public BankEmployeeSearchAdapter(Context context) {
    this.inflater = LayoutInflater.from(context);
    this.context = context;
  }

  public BankEmployeeSearchAdapter(Context context, List<BankEmployeeData> resultList) {
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
  public BankEmployeeData getItem(int index) {
    return resultList.get(index);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final HolderView holderView;
    final BankEmployeeData clientModel = resultList.get(position);
    if (convertView == null) {
      inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView = inflater.inflate(R.layout.item_list_attendee, parent, false);
      holderView = new BankEmployeeSearchAdapter.HolderView(convertView);
      convertView.setTag(holderView);
    } else {
      holderView = (BankEmployeeSearchAdapter.HolderView) convertView.getTag();
    }
    viewColor(position, convertView);
    holderView.tvAttendeeName.setText(clientModel.getEmployeeName());
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

    HolderView(View parent) {
      tvAttendeeName = parent.findViewById(R.id.tvAttendeeName);
    }
  }
}
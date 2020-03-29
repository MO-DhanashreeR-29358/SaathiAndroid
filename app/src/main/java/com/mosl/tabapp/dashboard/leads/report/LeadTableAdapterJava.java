package com.mosl.tabapp.dashboard.leads.report;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.mosl.tabapp.R;
import com.mosl.tabapp.databinding.ItemTableBinding;
import com.mosl.tabapp.databinding.ItemTableFamilyBinding;
import com.mosl.tabapp.databinding.ItemTableFirstLeadBinding;
import com.mosl.tabapp.databinding.ItemTableHeaderFirstLeadBinding;
import com.mosl.tabapp.databinding.ItemTableHeaderLeadBinding;
import com.mosl.tabapp.navigation.NavigationConstants;
import com.mosl.tabapp.navigation.ScreenNavigatorImpl;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mosl.tabapp.constants.MOSLConstants.COL_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.LABEL;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD;
import static com.mosl.tabapp.constants.MOSLConstants.MAP;
import static com.mosl.tabapp.constants.MOSLConstants.ROW_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.SYSTEM_NAME;

public class LeadTableAdapterJava extends BaseTableAdapter {
  private final CommonHeader[] commonHeaders;

  private String[] headers = {};

  private String[] headers2 = {};

  private ArrayList<HashMap<String, String>> hashArray;
  private FragmentActivity requireActivity;
  private String widgetSystemsName;
  private ScreenNavigatorImpl navigator;
  private Map<String, String> map;
  private Map<Integer, List<Map>> tableHeaderMap;

  LeadTableAdapterJava(FragmentActivity requireActivity, Map<String, String> map,
      ScreenNavigatorImpl navigator,
      String widgetSystemsName) {
    commonHeaders = new CommonHeader[] { new CommonHeader("asd") };
    hashArray = new ArrayList<>();
    this.requireActivity = requireActivity;
    this.map = map;
    this.navigator = navigator;
    this.widgetSystemsName = widgetSystemsName;
  }

  public void setHeaders(String[] headers) {
    this.headers = headers;
  }

  void setTableMap(Map<Integer, List<Map>> tableHeaderMap) {
    this.tableHeaderMap = tableHeaderMap;
  }

  void setHeadersSecond(String[] headers) {
    this.headers2 = headers;
  }

  @Override
  public int getRowCount() {
    return commonHeaders[0].list.size();
  }

  public void clear() {
  }

  public void add(HashMap<String, String> row) {
    hashArray.add(row);

    String[] rowData = new String[headers.length];
    int ctr = 0;
    for (String columnName : headers) {
      String value = row.get(columnName);
      rowData[ctr] = value;
      ctr++;
    }

    commonHeaders[0].list.add(new Item(rowData));
  }

  private String getRowKeyName(double childId) {
    for (Map.Entry<Integer, List<Map>> entry : tableHeaderMap.entrySet()) {
      for (Map childMap : entry.getValue()) {
        Set<Integer> childListSet = childMap.keySet();
        for (Integer childIdFromMap : childListSet) {
          if (childId == childIdFromMap) {
            return (String) childMap.get(childIdFromMap);
          }
        }
      }
    }
    return null;
  }

  private boolean isGroup(double id) {
    for (Map.Entry<Integer, List<Map>> entry : tableHeaderMap.entrySet()) {
      int groupId = entry.getKey();
      if (groupId == id) return true;
    }
    return false;
  }

  @Override
  public int getColumnCount() {
    return headers.length - 1;
  }

  @Override
  public View getView(final int row, final int column, View convertView, ViewGroup parent) {
    final View view;

    switch (getItemViewType(row, column)) {
      case 0:
        view = getFirstHeader(requireActivity);
        break;
      case 1:
        view = getHeader(column, requireActivity);
        break;
      case 2:
        view = getFirstBody(row, column, requireActivity);

        break;
      case 3:
        view = getBody(row, column, requireActivity);
        view.setOnClickListener(view1 -> {
          int newColumn = column + 1;
          Set<String> stringSet = hashArray.get(row).keySet();
          String columnName = stringSet.toArray()[newColumn].toString();
          Map<String, String> addtionalParameterMap = new HashMap<>();
          addtionalParameterMap.put(COL_FILTER, columnName);
          addtionalParameterMap.put(ROW_FILTER, getRowKeyName(
              Double.parseDouble(hashArray.get(row).get("id"))));
          addtionalParameterMap.putAll(map);
          Bundle args = new Bundle();
          args.putString(SYSTEM_NAME, widgetSystemsName);
          args.putString(LABEL, LEAD);
          args.putSerializable(MAP, (Serializable) addtionalParameterMap);
          LinkedHashMap lMap = (LinkedHashMap) hashArray.get(row);
          String columnData = lMap.get(columnName).toString();
          if (!columnData.trim().equalsIgnoreCase("0")) {
            navigator.openPage(
                NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD,
                args,
                true, false);
          }
        });
        break;
      case 4:
        view = getFamilyView(row, column, requireActivity);
        break;
      default:
        throw new RuntimeException("runtime exception");
    }
    return view;
  }

  //click on symbol header
  private View getFirstHeader(FragmentActivity context) {
    LayoutInflater inflater = LayoutInflater.from(context);
    ItemTableHeaderFirstLeadBinding binding =
        ItemTableHeaderFirstLeadBinding.inflate(inflater);
    binding.text1.setText(headers[1]);
    binding.text1.setTypeface(null, Typeface.BOLD);
    return binding.getRoot();
  }

  //click on header other than symbol
  private View getHeader(int column, FragmentActivity context) {
    LayoutInflater inflater = LayoutInflater.from(context);
    ItemTableHeaderLeadBinding binding =
        ItemTableHeaderLeadBinding.inflate(inflater);
    binding.text1.setText(headers[column + 1]);
    binding.text2.setText(headers2[column + 1]);

    return binding.getRoot();
  }

  private View getFirstBody(int row, int column, FragmentActivity context) {
    LayoutInflater inflater = LayoutInflater.from(context);
    ItemTableFirstLeadBinding binding =
        ItemTableFirstLeadBinding.inflate(inflater);

    try {
      if (hashArray.get(row).get("id") != null && isGroup(
          Double.parseDouble(hashArray.get(row).get("id")))) {
        binding.tvTableFirst.setTypeface(null, Typeface.BOLD);
        binding.tvTableFirst.setTextColor(ContextCompat.getColor(context, R.color.flush_orange));
        binding.itemTableFirstLead.setBackgroundResource(R.color.lead_self_back);
      } else if (hashArray.get(row).get("stage") != null && (hashArray.get(row)
          .get("stage")).equalsIgnoreCase("total")) {
        binding.tvTableFirst.setTypeface(null, Typeface.BOLD);
        binding.tvTableFirst.setTextColor(ContextCompat.getColor(context, R.color.black));
        binding.itemTableFirstLead.setBackgroundResource(R.color.secondaryBackground);
      } else {
        binding.tvTableFirst.setTypeface(null, Typeface.BOLD);
        binding.tvTableFirst.setTextColor(ContextCompat.getColor(context, R.color.black));
        binding.itemTableFirstLead.setBackgroundResource(R.color.lead_ekyc_back);
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (Resources.NotFoundException e) {
      e.printStackTrace();
    }
    binding.tvTableFirst.setText(getDevice(row).data[column + 2]);
    return binding.getRoot();
  }

  private View getBody(final int row, int column, FragmentActivity context) {
    LayoutInflater inflater = LayoutInflater.from(context);
    ItemTableBinding binding =
        ItemTableBinding.inflate(inflater);
    try {
      if (hashArray.get(row).get("id") != null && isGroup(
          Double.parseDouble(hashArray.get(row).get("id")))) {
        binding.text1.setTextColor(ContextCompat.getColor(context, R.color.flush_orange));
        binding.itemTable.setBackgroundResource(R.color.lead_self_back);
      } else if (hashArray.get(row).get("stage") != null && hashArray.get(row)
          .get("stage")
          .equalsIgnoreCase("total")) {
        binding.text1.setTypeface(null, Typeface.BOLD);
        binding.text1.setTextColor(requireActivity.getResources().getColor(R.color.black));
        binding.itemTable.setBackgroundResource(R.color.lead_total_back);
      } else {
        binding.text1.setTextColor(requireActivity.getResources().getColor(R.color.black));
        binding.itemTable.setBackgroundResource(R.color.lead_ekyc_back);
      }
      binding.text1.setText(getDevice(row).data[column + 1]);
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (Resources.NotFoundException e) {
      e.printStackTrace();
    }
    return binding.getRoot();
  }

  private View getFamilyView(int row, int column, FragmentActivity context) {
    LayoutInflater inflater = LayoutInflater.from(context);
    ItemTableFamilyBinding binding =
        ItemTableFamilyBinding.inflate(inflater);

    String string;
    if (column == -1) {
      string = getFamily(row).name;
    } else {
      string = "";
    }
    binding.text1.setText(string);
    return binding.getRoot();
  }

  @Override
  public int getWidth(int column) {
    int widthSize = 0;
    if (column == -1) {
      widthSize = requireActivity.getResources().getDimensionPixelSize(R.dimen._90sdp);
    }
    for (int i = 0; i < column; i++) {
      widthSize = requireActivity.getResources().getDimensionPixelSize(R.dimen._135sdp);
    }
    return widthSize;
  }

  @Override
  public int getHeight(int row) {
    final int height;
    if (row == -1) {
      height = requireActivity.getResources().getDimensionPixelSize(R.dimen._40sdp);
    } else {
      height = requireActivity.getResources().getDimensionPixelSize(R.dimen._40sdp);
    }
    return height;
  }

  @Override
  public int getItemViewType(int row, int column) {
    final int itemViewType;
    if (row == -1 && column == -1) {
      itemViewType = 0;
    } else if (row == -1) {
      itemViewType = 1;
    } else if (column == -1) {
      itemViewType = 2;
    } else {
      itemViewType = 3;
    }
    return itemViewType;
  }

  private CommonHeader getFamily(int row) {
    int family = 0;
    while (row >= 0) {
      row -= commonHeaders[family].size();
    }
    return commonHeaders[family];
  }

  private Item getDevice(int row) {
    int family = 0;
    while (row >= 0) {
      row -= commonHeaders[family].size();
    }
    return commonHeaders[family].get(row + commonHeaders[family].size());
  }

  @Override
  public int getViewTypeCount() {
    return 5;
  }

  private class CommonHeader {
    private final String name;
    private final List<Item> list;

    CommonHeader(String name) {
      this.name = name;
      list = new ArrayList<>();
    }

    public int size() {
      return list.size();
    }

    public Item get(int i) {
      return list.get(i);
    }
  }

  private class Item {
    private final String[] data;

    private Item(String[] rowData) {
      data = rowData;
    }
  }
}

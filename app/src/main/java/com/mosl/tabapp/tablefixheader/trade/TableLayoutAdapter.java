package com.mosl.tabapp.tablefixheader.trade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mosl.tabapp.R;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;

/**
 * Created by Shefali on 1/1/2016.
 */
public class TableLayoutAdapter extends BaseTableAdapter {
    private final List<RowData> familys;
    private final String headers[];
    private final int[] widths;
    private final float density;
    private LayoutInflater inflater = null;
    private TableItemClickListener clickListener;

    public TableLayoutAdapter(Context context, String[] headers, int[] widths) {
        familys = new ArrayList<>();
        this.headers = headers;
        this.widths = widths;
        density = context.getResources().getDisplayMetrics().density;
        inflater = LayoutInflater.from(context);
    }

    public void clear() {
        familys.clear();
    }

    public void add(HashMap<String, String> row) {
        familys.add(new RowData(row.get("Scrip"), row.get("BF Qty"), row.get("Available for Sell"), row.get("Trade blocked"), row.get("LTP"), row.get("Value"), row.get("AvgBuyPrice"), row.get("ProfitLoss")));
    }

    public void add(String... data) {
        familys.add(new RowData(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]));
    }

    public void setOnRowClickListener(TableItemClickListener listener) {
        this.clickListener = listener;
    }

    @Override
    public int getRowCount() {
        return familys.size();
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    public View getView(final int row, int column, View convertView, ViewGroup parent) {
        final View view;

        switch (getItemViewType(row, column)) {
            case 0:
                view = getFirstHeader(convertView, parent);
                break;
            case 1:
                view = getHeader(column, convertView, parent);
                break;
            case 2:
                view = getFirstBody(row, column, convertView, parent);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null) clickListener.onClick(row, view);
                    }
                });
                break;
            case 3:
                view = getBody(row, column, convertView, parent);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null) clickListener.onClick(row, view);
                    }
                });
                break;
            default:
                throw new RuntimeException("runtime exception");
        }
        return view;
    }

    private View getFirstHeader(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_table_header_first, parent, false);
        }
        TextView head = ((TextView) convertView.findViewById(android.R.id.text1));
        head.setText(headers[0]);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Sorting on click
            }
        });
        return convertView;
    }

    private View getHeader(int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_table_header, parent, false);
        }
        TextView head = ((TextView) convertView.findViewById(android.R.id.text1));
        head.setText(headers[column + 1]);
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Sorting on click
            }
        });
        return convertView;
    }

    private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_table_first, parent, false);
        }
        convertView.setBackgroundResource(row % 2 == 0 ? R.color.alternateBackground : R.color.white);
        ((TextView) convertView.findViewById(R.id.tv_table_first)).setText(familys.get(row).data[column + 1]);


        return convertView;
    }

    private View getBody(final int row, int column, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_table, parent, false);
        }
        convertView.setBackgroundResource(row % 2 == 0 ? R.color.alternateBackground : R.color.white);
        TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
        text1.setText(familys.get(row).data[column + 1]);
        return convertView;
    }

    @Override
    public int getWidth(int column) {
        if (column < widths.length - 1) return Math.round(widths[column + 1] * density);
        else return 0;
    }

    @Override
    public int getHeight(int row) {
        int height = 0;
        if (row == -1) height = 35;
        else height = 45;
        return Math.round(height * density);
    }

    @Override
    public int getItemViewType(int row, int column) {
        int itemViewType;
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

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    public interface TableItemClickListener {
        void onClick(int row, View view);
    }

    public class RowData {
        private final String[] data;

        RowData(String col1, String col2, String col3, String col4, String col5, String col6, String col7, String col8) {
            data = new String[]{col1, col2, col3, col4, col5, col6, col7, col8};
        }
    }
}
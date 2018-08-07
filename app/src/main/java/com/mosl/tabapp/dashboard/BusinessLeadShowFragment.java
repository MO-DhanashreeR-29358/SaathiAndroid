package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mosl.tabapp.R;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.tablefixheader.TableFixHeaders;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import com.mosl.tabapp.ui.MOSLCommonFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by raju on 2/20/2017.
 */

public class BusinessLeadShowFragment extends MOSLCommonFragment {
    private View leadShowTeamView;
    private TableFixHeaders table;
    private ArrayList<String> arrProductType;
    private ArrayAdapter<String> productAdapter;
    private BusinessLeadShowFragment.LeadTableAdapter leadTableAdapter;
    private ArrayList<String> arrSegmentName;
    private ArrayAdapter<String> segmentNameAdapter;
    private TextView tvNochart, txtDisclaimer;
    private LinkedHashMap lMap;
    private String clickedOn = "";
    private int click = 1;
    private Bundle bundle = this.getArguments();
    private String ucid;
    private int source = 0;
    private boolean dataFoundFlag;
    static Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        leadShowTeamView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_table_lead_show_team);
        setUpView(leadShowTeamView);
        hideAppTitle(getClass().toString());
        return leadShowTeamView;
    }

    public static BusinessLeadShowFragment newInstance(Context context) {
        BusinessLeadShowFragment fragment = new BusinessLeadShowFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View leadShowTeamView) {
        table = (TableFixHeaders) leadShowTeamView.findViewById(R.id.table);
        webServiceCall();
    }

    private void webServiceCall() {
        leadTableAdapter = new BusinessLeadShowFragment.LeadTableAdapter(getActivity(), new ArrayList<HashMap<String, String>>());
        table.setAdapter(leadTableAdapter);
        LinkedHashMap<String, String> hm = new LinkedHashMap<>();

        hm.put(STAGE, "Self");
        hm.put(LEAD_ALLOCATION, "25");

        hm.put(LEAD_FILTER, "8");
        hm.put(REJECTED, "18");
        LinkedHashMap<String, String> hm1 = new LinkedHashMap<>();

        hm1.put(STAGE, "E-kyc");
        hm1.put(LEAD_ALLOCATION, "25");

        hm1.put(LEAD_FILTER, "8");
        hm1.put(REJECTED, "18");

        LinkedHashMap<String, String> hm2 = new LinkedHashMap<>();

        hm2.put(STAGE, "Physical");
        hm2.put(LEAD_ALLOCATION, "25");

        hm2.put(LEAD_FILTER, "8");
        hm2.put(REJECTED, "18");


        LinkedHashMap<String, String> hm3 = new LinkedHashMap<>();

        hm3.put(STAGE, "Assigned");
        hm3.put(LEAD_ALLOCATION, "12");

        hm3.put(LEAD_FILTER, "8");
        hm3.put(REJECTED, "18");
        LinkedHashMap<String, String> hm4 = new LinkedHashMap<>();

        hm4.put(STAGE, "E-kyc");
        hm4.put(LEAD_ALLOCATION, "25");

        hm4.put(LEAD_FILTER, "8");
        hm4.put(REJECTED, "18");

        LinkedHashMap<String, String> hm5 = new LinkedHashMap<>();

        hm5.put(STAGE, "Physical");
        hm5.put(LEAD_ALLOCATION, "25");

        hm5.put(LEAD_FILTER, "8");
        hm5.put(REJECTED, "18");
        LinkedHashMap<String, String> hm6 = new LinkedHashMap<>();

        hm6.put(STAGE, "Total");
        hm6.put(LEAD_ALLOCATION, "37");

        hm6.put(LEAD_FILTER, "11");
        hm6.put(REJECTED, "22");
        leadTableAdapter.add(hm);
        leadTableAdapter.add(hm1);
        leadTableAdapter.add(hm2);
        leadTableAdapter.add(hm3);
        leadTableAdapter.add(hm4);
        leadTableAdapter.add(hm5);
        leadTableAdapter.add(hm6);

        leadTableAdapter.notifyDataSetChanged();

    }


    private class CommonHeader {
        private final String name;
        private final List<BusinessLeadShowFragment.Item> list;

        CommonHeader(String name) {
            this.name = name;
            list = new ArrayList<BusinessLeadShowFragment.Item>();
        }

        public int size() {
            return list.size();
        }

        public BusinessLeadShowFragment.Item get(int i) {
            return list.get(i);
        }
    }

    private class Item {
        private final String[] data;

        private Item(String partyCodeName, String segmentAmount, String status, String requestType) {
            data = new String[]{
                    partyCodeName,
                    segmentAmount,
                    status,
                    requestType
            };
        }
    }

    public class LeadTableAdapter extends BaseTableAdapter {
        private final BusinessLeadShowFragment.CommonHeader commonHeaders[];


        private final String headers[] = {
                STAGE,
                LEAD_ALLOCATION,
                LEAD_FILTER,
                REJECTED

        };

        private final String headers2[] = {
                "C0",
                "C1",
                "C2",
        };
        private final int[] widths = {90, 150, 150, 90};
        private final float density;
        HashMap hashMap;
        ArrayList<HashMap<String, String>> hashArray;
        private LayoutInflater inflater = null;

        public LeadTableAdapter(Context context, ArrayList<HashMap<String, String>> hashArray) {
            commonHeaders = new BusinessLeadShowFragment.CommonHeader[]{new BusinessLeadShowFragment.CommonHeader("asd")};
            this.hashArray = hashArray;
            density = context.getResources().getDisplayMetrics().density;




            /*commonHeaders[1].list.add(new Item(
                    row.get("raj"),
                    row.get("123.40"),
                    row.get("123.56"),
                    row.get("12.5"),
                    row.get("123")
            ));

            commonHeaders[2].list.add(new Item(
                    row.get("raj"),
                    row.get("123.40"),
                    row.get("123.56"),
                    row.get("12.5"),
                    row.get("123")
            ));


            commonHeaders[3].list.add(new Item(
                    row.get("raj"),
                    row.get("123.40"),
                    row.get("123.56"),
                    row.get("12.5"),
                    row.get("123")
            ));


            commonHeaders[4].list.add(new Item(
                    row.get("raj"),
                    row.get("123.40"),
                    row.get("123.56"),
                    row.get("12.5"),
                    row.get("123")
            ));*/

        }

        @Override
        public int getRowCount() {
            return commonHeaders[0].list.size();
        }

        public void clear() {
            //    hashArray.clear();
            //    commonHeaders[0].list.clear();
        }

        public void add(HashMap<String, String> row) {
            hashArray.add(row);

            commonHeaders[0].list.add(new BusinessLeadShowFragment.Item(
                    row.get(STAGE),
                    row.get(LEAD_ALLOCATION),
                    row.get(LEAD_FILTER),
                    row.get(REJECTED)


            ));
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
                    view = getFirstHeader(row, column, convertView, parent);
                    break;
                case 1:
                    view = getHeader(row, column, convertView, parent);
                    break;
                case 2:
                    view = getFirstBody(row, column, convertView, parent);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle args = new Bundle();
                            args.putString("cliencode", "");
                            navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);

                           /* lMap = (LinkedHashMap) hashArray.get(row);
                            Log.d("Exc", lMap.toString());
                            Bundle args = new Bundle();
                            String[] parts = (lMap.get("AMC Name").toString()).split("#");
                            String amcName = parts[0];
                            String amcid = parts[1];
                            args.putString(AMCID, amcid);

                            navigateTo(NAV_TO_SCHEME_WISE_AUM_REPORT, args, getFragmentManager(), true);

*/
                        }
                    });

                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Bundle args = new Bundle();
                            args.putString("cliencode", "");
                            navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
                        }
                    });
                    break;
                case 4:
                    view = getFamilyView(row, column, convertView, parent);
                    break;
                default:
                    throw new RuntimeException("runtime exception");
            }
            return view;
        }

        //click on symbol header
        private View getFirstHeader(final int row, int column, View convertView, ViewGroup parent) {
            inflater = LayoutInflater.from(getActivity());
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header_first_lead, parent, false);
            }

            TextView head = ((TextView) convertView.findViewById(android.R.id.text1));
            head.setText(headers[0]);
            head.setTypeface(null, Typeface.BOLD);
            return convertView;
        }


        //click on header other than symbol
        private View getHeader(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header_lead, parent, false);
            }
            TextView head = ((TextView) convertView.findViewById(android.R.id.text1));

            TextView head1 = ((TextView) convertView.findViewById(android.R.id.text2));

            head1.setText(headers2[column]);
            head.setText(headers[column + 1]);

            return convertView;
        }

        private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_first_lead, parent, false);
            }
            LinearLayout listLayout = (LinearLayout) convertView.findViewById(R.id.item_table_first_lead);

            if (row == 0 || row == 3) {
                listLayout.setBackgroundResource(R.color.lead_self_back);
            } else if (row == 1 || row == 2 || row == 4 || row == 5) {
                listLayout.setBackgroundResource(R.color.lead_ekyc_back);
            } else if (row == 6) {
                listLayout.setBackgroundResource(R.color.secondaryBackground);
            }
            TextView tv_table_first = (TextView) convertView.findViewById(R.id.tv_table_first);
           /* String[] parts = getDevice(row).data[column + 1].split("#");
            String amcName = parts[0];
            String ucid = parts[1];*/
            if (row == 0 || row == 3) {
                tv_table_first.setTypeface(null, Typeface.BOLD);
                tv_table_first.setTextColor(getResources().getColor(R.color.flush_orange));
            } else if (row == 6) {
                tv_table_first.setTypeface(null, Typeface.BOLD);
                tv_table_first.setTextColor(getResources().getColor(R.color.black));
            } else {
                tv_table_first.setTypeface(null, Typeface.BOLD);
                tv_table_first.setTextColor(getResources().getColor(R.color.black));
            }

            tv_table_first.setText(getDevice(row).data[column + 1]);
            return convertView;
        }

        private String getItem(int row, int pos) {
            if (pos == -1) {
                return hashArray.get(row).get(MOSLConstants.STAGE).toString();
            } else if (pos == 0) {
                return hashArray.get(row).get(MOSLConstants.LEAD_ALLOCATION).toString();
            } else if (pos == 1) {
                return hashArray.get(row).get(MOSLConstants.LEAD_FILTER).toString();
            } else if (pos == 2) {
                return hashArray.get(row).get(MOSLConstants.REJECTED).toString();
            }
            return "N/A";
        }

        private View getBody(final int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table, parent, false);
            }
            LinearLayout listLayout = (LinearLayout) convertView.findViewById(R.id.item_table);


            if (row == 0 || row == 3) {
                listLayout.setBackgroundResource(R.color.lead_self_back);
            } else if (row == 1 || row == 2 || row == 4 || row == 5) {
                listLayout.setBackgroundResource(R.color.lead_ekyc_back);
            } else if (row == 6) {
                listLayout.setBackgroundResource(R.color.lead_total_back);
            }
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(getDevice(row).data[column + 1]);
            TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
            if (row == 0 || row == 3) {
                text1.setTextColor(getResources().getColor(R.color.flush_orange));
            } else if (row == 6) {
                text1.setTypeface(null, Typeface.BOLD);
                text1.setTextColor(getResources().getColor(R.color.black));
            } else {
                text1.setTextColor(getResources().getColor(R.color.black));
            }

            text1.setBackgroundResource(0);
            return convertView;
        }

        private View getFamilyView(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_family, parent, false);
            }
            final String string;
            if (column == -1) {
                string = getFamily(row).name;
            } else {
                string = "";
            }
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(string);
            return convertView;
        }

        @Override
        public int getWidth(int column) {
            int widthSize = 0;
            if (column == -1) {
                widthSize = Math.round(150 * density);
            }
            for (int i = 0; i < column; i++) {
                widthSize = Math.round((int) getResources().getDimension(R.dimen.appTableWithWidth) * density);
            }
            return widthSize;
        }

        @Override
        public int getHeight(int row) {
            final int height;
            if (row == -1) {
                height = (int) getResources().getDimension(R.dimen.appTableWithSizeHeader);
            } else if (isFamily(row)) {
                height = 0;
            } else {
                height = (int) getResources().getDimension(R.dimen.appTableWithSize);
            }
            return Math.round(height * density);
        }

        @Override
        public int getItemViewType(int row, int column) {
            final int itemViewType;
            if (row == -1 && column == -1) {
                itemViewType = 0;
            } else if (row == -1) {
                itemViewType = 1;
            } else if (isFamily(row)) {
                itemViewType = 4;
            } else if (column == -1) {
                itemViewType = 2;
            } else {
                itemViewType = 3;
            }
            return itemViewType;
        }

        private boolean isFamily(int row) {

            if (row == -1 || row == 0) {
                int family = 0;
                while (row > 0) {
                    row -= commonHeaders[family].size();
                    family++;
                }
                return row == 1;
            }
            return row == 0;
        }

        private BusinessLeadShowFragment.CommonHeader getFamily(int row) {
            int family = 0;
            while (row >= 0) {
                row -= commonHeaders[family].size();
            }
            return commonHeaders[family];
        }

        private BusinessLeadShowFragment.Item getDevice(int row) {
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
    }
}

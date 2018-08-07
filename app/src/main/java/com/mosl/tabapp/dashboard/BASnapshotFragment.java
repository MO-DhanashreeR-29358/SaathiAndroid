package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.mosl.tabapp.R;
import com.mosl.tabapp.ui.MOSLCommonFragment;

/**
 * Created by raju on 4/20/2017.
 */

public class BASnapshotFragment extends MOSLCommonFragment {
    private View basnapshotView;

    TextView tvParameter, tvNcr, tvPcr, tvOcr, tvTotal, tvMapped, tvMappedNcr, tvMappedPcr, tvMappedOcr, tvMappedTotal, tvMet, tvMetNcr, tvMetPcr, tvMetOcr, tvMetTotal,
            tvSpokenTo, tvSpokenToNcr, tvSpokenToPcr, tvSpokenToOcr, tvSpokenToTotal, tvTPP, tvTPPNcr, tvTPPPcr, tvTPPOcr, tvTPPTotal, tvCommodity, tvCommodityNcr, tvCommodityPcr,
            tvCommodityOcr, tvCommodityTotal, tvCurrency, tvCurrencyNcr, tvCurrencyPcr, tvCurrencyOcr, tvCurrencyTotal, tvRatio, tvRatioNcr, tvRatioPcr, tvRatioOcr, tvRatioTotal;

    Spinner spDate;

    static Context mContext;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        basnapshotView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_ba_snapshot);
        setUpView(basnapshotView);
        hideAppTitle(getClass().toString());
        return basnapshotView;
    }

    public static BASnapshotFragment newInstance(Context context) {
        BASnapshotFragment fragment = new BASnapshotFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View basnapshotView) {
        tvParameter = (TextView) basnapshotView.findViewById(R.id.tvParameter);
        tvNcr = (TextView) basnapshotView.findViewById(R.id.tvNcr);
        tvPcr = (TextView) basnapshotView.findViewById(R.id.tvPcr);
        tvOcr = (TextView) basnapshotView.findViewById(R.id.tvOcr);
        tvTotal = (TextView) basnapshotView.findViewById(R.id.tvTotal);
        tvMapped = (TextView) basnapshotView.findViewById(R.id.tvMapped);
        tvMappedNcr = (TextView) basnapshotView.findViewById(R.id.tvMappedNcr);
        tvMappedPcr = (TextView) basnapshotView.findViewById(R.id.tvMappedPcr);
        tvMappedOcr = (TextView) basnapshotView.findViewById(R.id.tvMappedOcr);
        tvMappedTotal = (TextView) basnapshotView.findViewById(R.id.tvMappedTotal);
        tvMet = (TextView) basnapshotView.findViewById(R.id.tvMet);
        tvMetNcr = (TextView) basnapshotView.findViewById(R.id.tvMetNcr);
        tvMetPcr = (TextView) basnapshotView.findViewById(R.id.tvMetPcr);
        tvMetOcr = (TextView) basnapshotView.findViewById(R.id.tvMetOcr);
        tvMetTotal = (TextView) basnapshotView.findViewById(R.id.tvMetTotal);
        tvSpokenTo = (TextView) basnapshotView.findViewById(R.id.tvSpokenTo);
        tvSpokenToNcr = (TextView) basnapshotView.findViewById(R.id.tvSpokenToNcr);
        tvSpokenToPcr = (TextView) basnapshotView.findViewById(R.id.tvSpokenToPcr);
        tvSpokenToOcr = (TextView) basnapshotView.findViewById(R.id.tvSpokenToOcr);
        tvSpokenToTotal = (TextView) basnapshotView.findViewById(R.id.tvSpokenToTotal);
        tvTPP = (TextView) basnapshotView.findViewById(R.id.tvTPP);
        tvTPPNcr = (TextView) basnapshotView.findViewById(R.id.tvTPPNcr);
        tvTPPPcr = (TextView) basnapshotView.findViewById(R.id.tvTPPPcr);
        tvTPPOcr = (TextView) basnapshotView.findViewById(R.id.tvTPPOcr);
        tvTPPTotal = (TextView) basnapshotView.findViewById(R.id.tvTPPTotal);
        tvCommodity = (TextView) basnapshotView.findViewById(R.id.tvCommodity);
        tvCommodityNcr = (TextView) basnapshotView.findViewById(R.id.tvCommodityNcr);
        tvCommodityPcr = (TextView) basnapshotView.findViewById(R.id.tvCommodityPcr);
        tvCommodityOcr = (TextView) basnapshotView.findViewById(R.id.tvCommodityOcr);
        tvCommodityTotal = (TextView) basnapshotView.findViewById(R.id.tvCommodityTotal);
        tvCurrency = (TextView) basnapshotView.findViewById(R.id.tvCurrency);
        tvCurrencyNcr = (TextView) basnapshotView.findViewById(R.id.tvCurrencyNcr);
        tvCurrencyPcr = (TextView) basnapshotView.findViewById(R.id.tvCurrencyPcr);
        tvCurrencyOcr = (TextView) basnapshotView.findViewById(R.id.tvCurrencyOcr);
        tvCurrencyTotal = (TextView) basnapshotView.findViewById(R.id.tvCurrencyTotal);
        tvRatio = (TextView) basnapshotView.findViewById(R.id.tvRatio);
        tvRatioNcr = (TextView) basnapshotView.findViewById(R.id.tvRatioNcr);
        tvRatioPcr = (TextView) basnapshotView.findViewById(R.id.tvRatioPcr);
        tvRatioOcr = (TextView) basnapshotView.findViewById(R.id.tvRatioOcr);
        tvRatioTotal = (TextView) basnapshotView.findViewById(R.id.tvRatioTotal);

        tvParameter.setText("Parameter");
        tvNcr.setText("NCR");
        tvPcr.setText("PCR");
        tvOcr.setText("OCR");
        tvTotal.setText("Total");
        tvMapped.setText("Mapped");
        tvMappedNcr.setText("1000");
        tvMappedPcr.setText("1000");
        tvMappedOcr.setText("1000");
        tvMappedTotal.setText("1000");
        tvMet.setText("Met");
        tvMetNcr.setText("1000");
        tvMetPcr.setText("1000");
        tvMetOcr.setText("1000");
        tvMetTotal.setText("1000");
        tvSpokenTo.setText("Spoken To");
        tvSpokenToNcr.setText("1000");
        tvSpokenToPcr.setText("1000");
        tvSpokenToOcr.setText("1000");
        tvSpokenToTotal.setText("1000");
        tvTPP.setText("Active In TPP");
        tvTPPNcr.setText("1000");
        tvTPPPcr.setText("1000");
        tvTPPOcr.setText("1000");
        tvTPPTotal.setText("1000");
        tvCommodity.setText("Active In Commodity");
        tvCommodityNcr.setText("1000");
        tvCommodityPcr.setText("1000");
        tvCommodityOcr.setText("1000");
        tvCommodityTotal.setText("1000");
        tvCurrency.setText("Active In Currency");
        tvCurrencyNcr.setText("1000");
        tvCurrencyPcr.setText("1000");
        tvCurrencyOcr.setText("1000");
        tvCurrencyTotal.setText("1000");
        tvRatio.setText("Client Active Ratio");
        tvRatioNcr.setText("1000");
        tvRatioPcr.setText("1000");
        tvRatioOcr.setText("1000");
        tvRatioTotal.setText("1000");
    }
}

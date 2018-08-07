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

public class BACategoryFragment extends MOSLCommonFragment {

    private View bacategoryView;
    static Context mContext;
    Spinner spDate;
    TextView tvParameter, tvTitanium, tvGold, tvSilver, tvBlue, tvMapped, tvMappedTitanium, tvMappedGold, tvMappedSilver, tvMappedBlue, tvMet, tvMetTitanium,
            tvMetGold, tvMetSilver, tvMetBlue, tvSpokenTo, tvSpokenToTitanium, tvSpokenToGold, tvSpokenToSilver, tvSpokenToBlue, tvTPP, tvTPPTitanium,
            tvTPPGold, tvTPPSilver, tvTPPBlue, tvCommodity, tvCommodityTitanium, tvCommodityGold, tvCommoditySilver, tvCommodityBlue, tvCurrency, tvCurrencyTitanium, tvCurrencyGold, tvCurrencySilver,
            tvCurrencyBlue, tvRatio, tvRatioTitanium, tvRatioGold, tvRatioSilver, tvRatioBlue;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bacategoryView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_ba_category);
        setUpView(bacategoryView);
        hideAppTitle(getClass().toString());
        return bacategoryView;
    }

    public static BACategoryFragment newInstance(Context context) {
        BACategoryFragment fragment = new BACategoryFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View bacategoryView) {

        tvParameter = (TextView) bacategoryView.findViewById(R.id.tvParameter);
        tvTitanium = (TextView) bacategoryView.findViewById(R.id.tvTitanium);
        tvGold = (TextView) bacategoryView.findViewById(R.id.tvGold);
        tvSilver = (TextView) bacategoryView.findViewById(R.id.tvSilver);
        tvBlue = (TextView) bacategoryView.findViewById(R.id.tvBlue);
        tvMapped = (TextView) bacategoryView.findViewById(R.id.tvMapped);
        tvMappedTitanium = (TextView) bacategoryView.findViewById(R.id.tvMappedTitanium);
        tvMappedGold = (TextView) bacategoryView.findViewById(R.id.tvMappedGold);
        tvMappedSilver = (TextView) bacategoryView.findViewById(R.id.tvMappedSilver);
        tvMappedBlue = (TextView) bacategoryView.findViewById(R.id.tvMappedBlue);
        tvMet = (TextView) bacategoryView.findViewById(R.id.tvMet);
        tvMetTitanium = (TextView) bacategoryView.findViewById(R.id.tvMetTitanium);
        tvMetGold = (TextView) bacategoryView.findViewById(R.id.tvMetGold);
        tvMetSilver = (TextView) bacategoryView.findViewById(R.id.tvMetSilver);
        tvMetBlue = (TextView) bacategoryView.findViewById(R.id.tvMetBlue);
        tvSpokenTo = (TextView) bacategoryView.findViewById(R.id.tvSpokenTo);
        tvSpokenToTitanium = (TextView) bacategoryView.findViewById(R.id.tvSpokenToTitanium);
        tvSpokenToGold = (TextView) bacategoryView.findViewById(R.id.tvSpokenToGold);
        tvSpokenToSilver = (TextView) bacategoryView.findViewById(R.id.tvSpokenToSilver);
        tvSpokenToBlue = (TextView) bacategoryView.findViewById(R.id.tvSpokenToBlue);
        tvTPP = (TextView) bacategoryView.findViewById(R.id.tvTPP);
        tvTPPTitanium = (TextView) bacategoryView.findViewById(R.id.tvTPPTitanium);
        tvTPPGold = (TextView) bacategoryView.findViewById(R.id.tvTPPGold);
        tvTPPSilver = (TextView) bacategoryView.findViewById(R.id.tvTPPSilver);
        tvTPPBlue = (TextView) bacategoryView.findViewById(R.id.tvTPPBlue);
        tvCommodity = (TextView) bacategoryView.findViewById(R.id.tvCommodity);
        tvCommodityTitanium = (TextView) bacategoryView.findViewById(R.id.tvCommodityTitanium);
        tvCommodityGold = (TextView) bacategoryView.findViewById(R.id.tvCommodityGold);
        tvCommoditySilver = (TextView) bacategoryView.findViewById(R.id.tvCommoditySilver);
        tvCommodityBlue = (TextView) bacategoryView.findViewById(R.id.tvCommodityBlue);
        tvCurrency = (TextView) bacategoryView.findViewById(R.id.tvCurrency);
        tvCurrencyTitanium = (TextView) bacategoryView.findViewById(R.id.tvCurrencyTitanium);
        tvCurrencyGold = (TextView) bacategoryView.findViewById(R.id.tvCurrencyGold);
        tvCurrencySilver = (TextView) bacategoryView.findViewById(R.id.tvCurrencySilver);
        tvCurrencyBlue = (TextView) bacategoryView.findViewById(R.id.tvCurrencyBlue);
        tvRatio = (TextView) bacategoryView.findViewById(R.id.tvRatio);
        tvRatioTitanium = (TextView) bacategoryView.findViewById(R.id.tvRatioTitanium);
        tvRatioGold = (TextView) bacategoryView.findViewById(R.id.tvRatioGold);
        tvRatioSilver = (TextView) bacategoryView.findViewById(R.id.tvRatioSilver);
        tvRatioBlue = (TextView) bacategoryView.findViewById(R.id.tvRatioBlue);

        tvParameter.setText("Parameter");
        tvTitanium.setText("Titanium");
        tvGold.setText("Gold");
        tvSilver.setText("Silver");
        tvBlue.setText("Blue");
        tvMapped.setText("Mapped");
        tvMappedTitanium.setText("1000");
        tvMappedGold.setText("1000");
        tvMappedSilver.setText("1000");
        tvMappedBlue.setText("1000");
        tvMet.setText("Met");
        tvMetTitanium.setText("1000");
        tvMetGold.setText("1000");
        tvMetSilver.setText("1000");
        tvMetBlue.setText("1000");
        tvSpokenTo.setText("Spoken To");
        tvSpokenToTitanium.setText("1000");
        tvSpokenToGold.setText("1000");
        tvSpokenToSilver.setText("1000");
        tvSpokenToBlue.setText("1000");
        tvTPP.setText("Active In TPP");
        tvTPPTitanium.setText("1000");
        tvTPPGold.setText("1000");
        tvTPPSilver.setText("1000");
        tvTPPBlue.setText("1000");
        tvCommodity.setText("Active In Commodity");
        tvCommodityTitanium.setText("1000");
        tvCommodityGold.setText("1000");
        tvCommoditySilver.setText("1000");
        tvCommodityBlue.setText("1000");
        tvCurrency.setText("Active In Currency");
        tvCurrencyTitanium.setText("1000");
        tvCurrencyGold.setText("1000");
        tvCurrencySilver.setText("1000");
        tvCurrencyBlue.setText("1000");
        tvRatio.setText("Client Active Ratio");
        tvRatioTitanium.setText("1000");
        tvRatioGold.setText("1000");
        tvRatioSilver.setText("1000");
        tvRatioBlue.setText("1000");


    }


}

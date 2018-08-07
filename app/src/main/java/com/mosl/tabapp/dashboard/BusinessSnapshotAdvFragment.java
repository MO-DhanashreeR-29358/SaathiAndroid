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

public class BusinessSnapshotAdvFragment extends MOSLCommonFragment {

    private View businesssnapshotadvView;
    static Context mContext;
    Spinner spDate;
    TextView tvProduct, tvAumasonDate, tvGrossSales, tvRedemptions, tvNetSales,tvRevenueGenerated,
            tvPms, tvPmsAumasonDate, tvPmsGrossSales, tvPmsRedemptions, tvPmsNetSales,tvPmsRevenueGenerated,
            tvMfLumpsum, tvMfLumpsumAumasonDate, tvMfLumpsumGrossSales, tvMfLumpsumRedemptions, tvMfLumpsumNetSales,tvMfLumpsumRevenueGenerated,
            tvBond,tvBondAumasonDate, tvBondGrossSales, tvBondRedemptions, tvBondNetSales, tvBondRevenueGenerated,
            tvFD, tvFDAumasonDate, tvFDGrossSales, tvFDRedemptions, tvFDNetSales,tvFDRevenueGenerated,
    
            tvPE, tvPEAumasonDate, tvPEGrossSales, tvPERedemptions, tvPENetSales, tvPERevenueGenerated,
            tvOther, tvOtherAumasonDate, tvOtherGrossSales, tvOtherRedemptions, tvOtherNetSales, tvOtherRevenueGenerated,
                    tvTotal, tvTotalAumasonDate, tvTotalGrossSales, tvTotalRedemptions, tvTotalNetSales,tvTotalRevenueGenerated;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        businesssnapshotadvView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_business_snapshot_adv);
        setUpView(businesssnapshotadvView);
        hideAppTitle(getClass().toString());
        return businesssnapshotadvView;
    }

    public static BusinessSnapshotAdvFragment newInstance(Context context) {
        BusinessSnapshotAdvFragment fragment = new BusinessSnapshotAdvFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View businesssnapshotadvView) {

        tvProduct = (TextView) businesssnapshotadvView.findViewById(R.id.tvProduct);
        tvAumasonDate = (TextView) businesssnapshotadvView.findViewById(R.id.tvAumasonDate);
        tvGrossSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvGrossSales);
        tvRedemptions = (TextView) businesssnapshotadvView.findViewById(R.id.tvRedemptions);
        tvNetSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvNetSales);
        tvRevenueGenerated = (TextView) businesssnapshotadvView.findViewById(R.id.tvRevenueGenerated);

        tvPms = (TextView) businesssnapshotadvView.findViewById(R.id.tvPms);
        tvPmsAumasonDate = (TextView) businesssnapshotadvView.findViewById(R.id.tvPmsAumasonDate);
        tvPmsGrossSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvPmsGrossSales);
        tvPmsRedemptions = (TextView) businesssnapshotadvView.findViewById(R.id.tvPmsRedemptions);
        tvPmsNetSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvPmsNetSales);
        tvPmsRevenueGenerated= (TextView) businesssnapshotadvView.findViewById(R.id.tvPmsRevenueGenerated);

        tvMfLumpsum = (TextView) businesssnapshotadvView.findViewById(R.id.tvMfLumpsum);
        tvMfLumpsumAumasonDate = (TextView) businesssnapshotadvView.findViewById(R.id.tvMfLumpsumAumasonDate);
        tvMfLumpsumGrossSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvMfLumpsumGrossSales);
        tvMfLumpsumRedemptions = (TextView) businesssnapshotadvView.findViewById(R.id.tvMfLumpsumRedemptions);
        tvMfLumpsumNetSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvMfLumpsumNetSales);
        tvMfLumpsumRevenueGenerated = (TextView) businesssnapshotadvView.findViewById(R.id.tvMfLumpsumRevenueGenerated);

        tvBond = (TextView) businesssnapshotadvView.findViewById(R.id.tvBond);
        tvBondAumasonDate = (TextView) businesssnapshotadvView.findViewById(R.id.tvBondAumasonDate);
        tvBondGrossSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvBondGrossSales);
        tvBondRedemptions = (TextView) businesssnapshotadvView.findViewById(R.id.tvBondRedemptions);
        tvBondNetSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvBondNetSales);
        tvBondRevenueGenerated = (TextView) businesssnapshotadvView.findViewById(R.id.tvBondRevenueGenerated);
        
        tvFD = (TextView) businesssnapshotadvView.findViewById(R.id.tvFD);
        tvFDAumasonDate = (TextView) businesssnapshotadvView.findViewById(R.id.tvFDAumasonDate);
        tvFDGrossSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvFDGrossSales);
        tvFDRedemptions = (TextView) businesssnapshotadvView.findViewById(R.id.tvFDRedemptions);
        tvFDNetSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvFDNetSales);
        tvFDRevenueGenerated = (TextView) businesssnapshotadvView.findViewById(R.id.tvFDRevenueGenerated);

        tvPE = (TextView) businesssnapshotadvView.findViewById(R.id.tvPE);
        tvPEAumasonDate = (TextView) businesssnapshotadvView.findViewById(R.id.tvPEAumasonDate);
        tvPEGrossSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvPEGrossSales);
        tvPERedemptions = (TextView) businesssnapshotadvView.findViewById(R.id.tvPERedemptions);
        tvPENetSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvPENetSales);
        tvPERevenueGenerated = (TextView) businesssnapshotadvView.findViewById(R.id.tvPERevenueGenerated);

        tvOther = (TextView) businesssnapshotadvView.findViewById(R.id.tvOther);
        tvOtherAumasonDate = (TextView) businesssnapshotadvView.findViewById(R.id.tvOtherAumasonDate);
        tvOtherGrossSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvOtherGrossSales);
        tvOtherRedemptions = (TextView) businesssnapshotadvView.findViewById(R.id.tvOtherRedemptions);
        tvOtherNetSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvOtherNetSales);
        tvOtherRevenueGenerated = (TextView) businesssnapshotadvView.findViewById(R.id.tvOtherRevenueGenerated);

        tvTotal = (TextView) businesssnapshotadvView.findViewById(R.id.tvTotal);
        tvTotalAumasonDate = (TextView) businesssnapshotadvView.findViewById(R.id.tvTotalAumasonDate);
        tvTotalGrossSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvTotalGrossSales);
        tvTotalRedemptions = (TextView) businesssnapshotadvView.findViewById(R.id.tvTotalRedemptions);
        tvTotalNetSales = (TextView) businesssnapshotadvView.findViewById(R.id.tvTotalNetSales);
        tvTotalRevenueGenerated = (TextView) businesssnapshotadvView.findViewById(R.id.tvTotalRevenueGenerated);

        tvProduct.setText("Product");
        tvAumasonDate.setText("AUM \n(AS on Date)");
        tvGrossSales.setText("Gross Sales");
        tvRedemptions.setText("Redemptions");
        tvNetSales.setText(" Net \n Sales");
        tvRevenueGenerated.setText("Revenue Generated");

        tvPms.setText("PMS");
        tvPmsAumasonDate.setText("1000");
        tvPmsGrossSales.setText("1000");
        tvPmsRedemptions.setText("1000");
        tvPmsNetSales.setText("1000");
        tvPmsRevenueGenerated.setText("1000");

        tvMfLumpsum.setText("MF-Lumpsum");
        tvMfLumpsumAumasonDate.setText("1000");
        tvMfLumpsumGrossSales.setText("1000");
        tvMfLumpsumRedemptions.setText("1000");
        tvMfLumpsumNetSales.setText("1000");
        tvMfLumpsumNetSales.setText("1000");

        tvBond.setText("Bond");
        tvBondAumasonDate.setText("1000");
        tvBondGrossSales.setText("1000");
        tvBondRedemptions.setText("1000");
        tvBondNetSales.setText("1000");
        tvBondRevenueGenerated.setText("1000");

        tvFD.setText("FD");
        tvFDAumasonDate.setText("1000");
        tvFDGrossSales.setText("1000");
        tvFDRedemptions.setText("1000");
        tvFDNetSales.setText("1000");
        tvFDRevenueGenerated.setText("1000");

        tvPE.setText("PE");
        tvPEAumasonDate.setText("1000");
        tvPEGrossSales.setText("1000");
        tvPERedemptions.setText("1000");
        tvPENetSales.setText("1000");
        tvPERevenueGenerated.setText("1000");

        tvOther.setText("Other");
        tvOtherAumasonDate.setText("1000");
        tvOtherGrossSales.setText("1000");
        tvOtherRedemptions.setText("1000");
        tvOtherNetSales.setText("1000");
        tvOtherRevenueGenerated.setText("1000");
        
        tvTotal.setText("Total");
        tvTotalAumasonDate.setText("1000");
        tvTotalGrossSales.setText("1000");
        tvTotalRedemptions.setText("1000");
        tvTotalNetSales.setText("1000");
        tvTotalRevenueGenerated.setText("1000");

    }


}

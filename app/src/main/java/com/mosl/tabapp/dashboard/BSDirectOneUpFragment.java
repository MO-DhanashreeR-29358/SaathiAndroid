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

public class BSDirectOneUpFragment extends MOSLCommonFragment {

    private View bsDirectOneUpView;
    static Context mContext;
    private Spinner spDate;
    private TextView tvBusiness, tvAumasonDate, tvGrossSales, tvGrossSalesActual, tvGrossSalesProjected, tvRedemptions, tvNetSales, tvSipSales, tvSipSalesCount, tvSipSalesAum, tvRevenue, tvRevenueActual, tvRevenueProjected,
            tvBranch, tvBranchAumasonDate, tvBranchGrossSalesActual, tvBranchGrossSalesProjected, tvBranchRedemptions, tvBranchNetSales, tvBranchSipSalesCount, tvBranchSipSalesAum, tvBranchRevenueActual, tvBranchRevenueProjected,
            tvPCG, tvPCGAumasonDate, tvPCGGrossSalesActual, tvPCGGrossSalesProjected, tvPCGRedemptions, tvPCGNetSales, tvPCGSipSalesCount, tvPCGSipSalesAum, tvPCGRevenueActual, tvPCGRevenueProjected,
            tvOAO, tvOAOAumasonDate, tvOAOGrossSalesActual, tvOAOGrossSalesProjected, tvOAORedemptions, tvOAONetSales, tvOAOSipSalesCount, tvOAOSipSalesAum, tvOAORevenueActual, tvOAORevenueProjected,
            tvOverAllTotal, tvOATAumasonDate, tvOATGrossSalesActual, tvOATGrossSalesProjected, tvOATRedemptions, tvOATNetSales, tvOATSipSalesCount, tvOATSipSalesAum, tvOATRevenueActual, tvOATRevenueProjected;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bsDirectOneUpView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_bs_direct_oneup);
        setUpView(bsDirectOneUpView);
        hideAppTitle(getClass().toString());
        return bsDirectOneUpView;
    }

    public static BSDirectOneUpFragment newInstance(Context context) {
        BSDirectOneUpFragment fragment = new BSDirectOneUpFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }


    private void setUpView(View bsDirectOneUpView) {

        tvBusiness = (TextView) bsDirectOneUpView.findViewById(R.id.tvBusiness);
        tvAumasonDate = (TextView) bsDirectOneUpView.findViewById(R.id.tvAumasonDate);
        tvGrossSales = (TextView) bsDirectOneUpView.findViewById(R.id.tvGrossSales);
        tvGrossSalesActual = (TextView) bsDirectOneUpView.findViewById(R.id.tvGrossSalesActual);
        tvGrossSalesProjected = (TextView) bsDirectOneUpView.findViewById(R.id.tvGrossSalesProjected);
        tvRedemptions = (TextView) bsDirectOneUpView.findViewById(R.id.tvRedemptions);
        tvNetSales = (TextView) bsDirectOneUpView.findViewById(R.id.tvNetSales);
        tvSipSales = (TextView) bsDirectOneUpView.findViewById(R.id.tvSipSales);
        tvSipSalesCount = (TextView) bsDirectOneUpView.findViewById(R.id.tvSipSalesCount);
        tvSipSalesAum = (TextView) bsDirectOneUpView.findViewById(R.id.tvSipSalesAum);
        tvRevenue = (TextView) bsDirectOneUpView.findViewById(R.id.tvRevenue);
        tvRevenueActual = (TextView) bsDirectOneUpView.findViewById(R.id.tvRevenueActual);
        tvRevenueProjected = (TextView) bsDirectOneUpView.findViewById(R.id.tvRevenueProjected);


        tvBranch = (TextView) bsDirectOneUpView.findViewById(R.id.tvBranch);
        tvBranchAumasonDate = (TextView) bsDirectOneUpView.findViewById(R.id.tvBranchAumasonDate);
        tvBranchGrossSalesActual = (TextView) bsDirectOneUpView.findViewById(R.id.tvBranchGrossSalesActual);
        tvBranchGrossSalesProjected = (TextView) bsDirectOneUpView.findViewById(R.id.tvBranchGrossSalesProjected);
        tvBranchRedemptions = (TextView) bsDirectOneUpView.findViewById(R.id.tvBranchRedemptions);
        tvBranchNetSales = (TextView) bsDirectOneUpView.findViewById(R.id.tvBranchNetSales);
        tvBranchSipSalesCount = (TextView) bsDirectOneUpView.findViewById(R.id.tvBranchSipSalesCount);
        tvBranchSipSalesAum = (TextView) bsDirectOneUpView.findViewById(R.id.tvBranchSipSalesAum);
        tvBranchRevenueActual = (TextView) bsDirectOneUpView.findViewById(R.id.tvBranchRevenueActual);
        tvBranchRevenueProjected = (TextView) bsDirectOneUpView.findViewById(R.id.tvBranchRevenueProjected);

        tvPCG = (TextView) bsDirectOneUpView.findViewById(R.id.tvPCG);
        tvPCGAumasonDate = (TextView) bsDirectOneUpView.findViewById(R.id.tvPCGAumasonDate);
        tvPCGGrossSalesActual = (TextView) bsDirectOneUpView.findViewById(R.id.tvPCGGrossSalesActual);
        tvPCGGrossSalesProjected = (TextView) bsDirectOneUpView.findViewById(R.id.tvPCGGrossSalesProjected);
        tvPCGRedemptions = (TextView) bsDirectOneUpView.findViewById(R.id.tvPCGRedemptions);
        tvPCGNetSales = (TextView) bsDirectOneUpView.findViewById(R.id.tvPCGNetSales);
        tvPCGSipSalesCount = (TextView) bsDirectOneUpView.findViewById(R.id.tvPCGSipSalesCount);
        tvPCGSipSalesAum = (TextView) bsDirectOneUpView.findViewById(R.id.tvPCGSipSalesAum);
        tvPCGRevenueActual = (TextView) bsDirectOneUpView.findViewById(R.id.tvPCGRevenueActual);
        tvPCGRevenueProjected = (TextView) bsDirectOneUpView.findViewById(R.id.tvPCGRevenueProjected);

        tvOAO = (TextView) bsDirectOneUpView.findViewById(R.id.tvOAO);
        tvOAOAumasonDate = (TextView) bsDirectOneUpView.findViewById(R.id.tvOAOAumasonDate);
        tvOAOGrossSalesActual = (TextView) bsDirectOneUpView.findViewById(R.id.tvOAOGrossSalesActual);
        tvOAOGrossSalesProjected = (TextView) bsDirectOneUpView.findViewById(R.id.tvOAOGrossSalesProjected);
        tvOAORedemptions = (TextView) bsDirectOneUpView.findViewById(R.id.tvOAORedemptions);
        tvOAONetSales = (TextView) bsDirectOneUpView.findViewById(R.id.tvOAONetSales);
        tvOAOSipSalesCount = (TextView) bsDirectOneUpView.findViewById(R.id.tvOAOSipSalesCount);
        tvOAOSipSalesAum = (TextView) bsDirectOneUpView.findViewById(R.id.tvOAOSipSalesAum);
        tvOAORevenueActual = (TextView) bsDirectOneUpView.findViewById(R.id.tvOAORevenueActual);
        tvOAORevenueProjected = (TextView) bsDirectOneUpView.findViewById(R.id.tvOAORevenueProjected);

        tvOverAllTotal = (TextView) bsDirectOneUpView.findViewById(R.id.tvOverAllTotal);
        tvOATAumasonDate = (TextView) bsDirectOneUpView.findViewById(R.id.tvOATAumasonDate);
        tvOATGrossSalesActual = (TextView) bsDirectOneUpView.findViewById(R.id.tvOATGrossSalesActual);
        tvOATGrossSalesProjected = (TextView) bsDirectOneUpView.findViewById(R.id.tvOATGrossSalesProjected);
        tvOATRedemptions = (TextView) bsDirectOneUpView.findViewById(R.id.tvOATRedemptions);
        tvOATNetSales = (TextView) bsDirectOneUpView.findViewById(R.id.tvOATNetSales);
        tvOATSipSalesCount = (TextView) bsDirectOneUpView.findViewById(R.id.tvOATSipSalesCount);
        tvOATSipSalesAum = (TextView) bsDirectOneUpView.findViewById(R.id.tvOATSipSalesAum);
        tvOATRevenueActual = (TextView) bsDirectOneUpView.findViewById(R.id.tvOATRevenueActual);
        tvOATRevenueProjected = (TextView) bsDirectOneUpView.findViewById(R.id.tvOATRevenueProjected);

        tvBusiness.setText("Business");
        tvAumasonDate.setText("AUM \n (AS on Date)");
        tvGrossSales.setText("Gross Sales");
        tvGrossSalesActual.setText("Actual");
        tvGrossSalesProjected.setText("Projected");
        tvRedemptions.setText("Redemptions");
        tvNetSales.setText(" Net Sales");
        tvSipSales.setText("Sip Sales ");
        tvSipSalesCount.setText("COUNT");
        tvSipSalesAum.setText("AUM");
        tvRevenue.setText("Revenue");
        tvRevenueActual.setText("Actual");
        tvRevenueProjected.setText("Projected");

        tvBranch.setText("Branch");
        tvBranchAumasonDate.setText("1000");
        tvBranchGrossSalesActual.setText("1000");
        tvBranchGrossSalesProjected.setText("1000");
        tvBranchRedemptions.setText("1000");
        tvBranchNetSales.setText("1000");
        tvBranchSipSalesCount.setText("1000");
        tvBranchSipSalesAum.setText("1000");
        tvBranchRevenueActual.setText("1000");
        tvBranchRevenueProjected.setText("1000");

        tvPCG.setText("PCG");
        tvPCGAumasonDate.setText("1000");
        tvPCGGrossSalesActual.setText("1000");
        tvPCGGrossSalesProjected.setText("1000");
        tvPCGRedemptions.setText("1000");
        tvPCGNetSales.setText("1000");
        tvPCGSipSalesCount.setText("1000");
        tvPCGSipSalesAum.setText("1000");
        tvPCGRevenueActual.setText("1000");
        tvPCGRevenueProjected.setText("1000");

        tvOAO.setText("OAO");
        tvOAOAumasonDate.setText("1000");
        tvOAOGrossSalesActual.setText("1000");
        tvOAOGrossSalesProjected.setText("1000");
        tvOAORedemptions.setText("1000");
        tvOAONetSales.setText("1000");
        tvOAOSipSalesCount.setText("1000");
        tvOAOSipSalesAum.setText("1000");
        tvOAORevenueActual.setText("1000");
        tvOAORevenueProjected.setText("1000");


        tvOverAllTotal.setText("OverAllTotal");
        tvOATAumasonDate.setText("3000");
        tvOATGrossSalesActual.setText("3000");
        tvOATGrossSalesProjected.setText("3000");
        tvOATRedemptions.setText("3000");
        tvOATNetSales.setText("3000");
        tvOATSipSalesCount.setText("3000");
        tvOATSipSalesAum.setText("3000");
        tvOATRevenueActual.setText("3000");
        tvOATRevenueProjected.setText("30000");


    }

}

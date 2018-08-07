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
 * Created by raju on 5/27/2017.
 */

public class BSFranchiseeOneUpFragment extends MOSLCommonFragment {

    private View bsFranchiseeOneUpView;
    static Context mContext;
    private Spinner spDate;
    private TextView tvBusiness, tvAumasonDate, tvGrossSales, tvGrossSalesActual, tvGrossSalesProjected, tvRedemptions, tvNetSales, tvSipSales, tvSipSalesCount, tvSipSalesAum, tvRevenue, tvRevenueActual, tvRevenueProjected,
            tvBranch, tvBranchAumasonDate, tvBranchGrossSalesActual, tvBranchGrossSalesProjected, tvBranchRedemptions, tvBranchNetSales, tvBranchSipSalesCount, tvBranchSipSalesAum, tvBranchRevenueActual, tvBranchRevenueProjected,
            tvPCG, tvPCGAumasonDate, tvPCGGrossSalesActual, tvPCGGrossSalesProjected, tvPCGRedemptions, tvPCGNetSales, tvPCGSipSalesCount, tvPCGSipSalesAum, tvPCGRevenueActual, tvPCGRevenueProjected,
            tvOAO, tvOAOAumasonDate, tvOAOGrossSalesActual, tvOAOGrossSalesProjected, tvOAORedemptions, tvOAONetSales, tvOAOSipSalesCount, tvOAOSipSalesAum, tvOAORevenueActual, tvOAORevenueProjected,
            tvOverAllTotal, tvOATAumasonDate, tvOATGrossSalesActual, tvOATGrossSalesProjected, tvOATRedemptions, tvOATNetSales, tvOATSipSalesCount, tvOATSipSalesAum, tvOATRevenueActual, tvOATRevenueProjected,
            tvIFAAcq, tvAcqActualCount, tvAcqActual, tvAcqProjected, tvAcqProjectedCount,tvIFAAct,tvActActualCount, tvActActual, tvActProjected, tvActProjectedCount;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bsFranchiseeOneUpView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_bs_franchisee_oneup);
        setUpView(bsFranchiseeOneUpView);
        hideAppTitle(getClass().toString());
        return bsFranchiseeOneUpView;
    }

    public static BSDirectOneUpFragment newInstance(Context context) {
        BSDirectOneUpFragment fragment = new BSDirectOneUpFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }


    private void setUpView(View bsFranchiseeOneUpView) {

        tvBusiness = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvBusiness);
        tvAumasonDate = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvAumasonDate);
        tvGrossSales = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvGrossSales);
        tvGrossSalesActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvGrossSalesActual);
        tvGrossSalesProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvGrossSalesProjected);
        tvRedemptions = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvRedemptions);
        tvNetSales = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvNetSales);
        tvSipSales = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvSipSales);
        tvSipSalesCount = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvSipSalesCount);
        tvSipSalesAum = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvSipSalesAum);
        tvRevenue = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvRevenue);
        tvRevenueActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvRevenueActual);
        tvRevenueProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvRevenueProjected);

        tvBranch = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvBranch);
        tvBranchAumasonDate = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvBranchAumasonDate);
        tvBranchGrossSalesActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvBranchGrossSalesActual);
        tvBranchGrossSalesProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvBranchGrossSalesProjected);
        tvBranchRedemptions = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvBranchRedemptions);
        tvBranchNetSales = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvBranchNetSales);
        tvBranchSipSalesCount = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvBranchSipSalesCount);
        tvBranchSipSalesAum = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvBranchSipSalesAum);
        tvBranchRevenueActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvBranchRevenueActual);
        tvBranchRevenueProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvBranchRevenueProjected);

        tvPCG = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvPCG);
        tvPCGAumasonDate = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvPCGAumasonDate);
        tvPCGGrossSalesActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvPCGGrossSalesActual);
        tvPCGGrossSalesProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvPCGGrossSalesProjected);
        tvPCGRedemptions = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvPCGRedemptions);
        tvPCGNetSales = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvPCGNetSales);
        tvPCGSipSalesCount = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvPCGSipSalesCount);
        tvPCGSipSalesAum = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvPCGSipSalesAum);
        tvPCGRevenueActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvPCGRevenueActual);
        tvPCGRevenueProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvPCGRevenueProjected);

        tvOAO = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOAO);
        tvOAOAumasonDate = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOAOAumasonDate);
        tvOAOGrossSalesActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOAOGrossSalesActual);
        tvOAOGrossSalesProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOAOGrossSalesProjected);
        tvOAORedemptions = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOAORedemptions);
        tvOAONetSales = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOAONetSales);
        tvOAOSipSalesCount = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOAOSipSalesCount);
        tvOAOSipSalesAum = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOAOSipSalesAum);
        tvOAORevenueActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOAORevenueActual);
        tvOAORevenueProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOAORevenueProjected);

        tvOverAllTotal = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOverAllTotal);
        tvOATAumasonDate = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOATAumasonDate);
        tvOATGrossSalesActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOATGrossSalesActual);
        tvOATGrossSalesProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOATGrossSalesProjected);
        tvOATRedemptions = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOATRedemptions);
        tvOATNetSales = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOATNetSales);
        tvOATSipSalesCount = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOATSipSalesCount);
        tvOATSipSalesAum = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOATSipSalesAum);
        tvOATRevenueActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOATRevenueActual);
        tvOATRevenueProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvOATRevenueProjected);

        tvIFAAcq = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvIFAAcq);
        tvAcqActualCount = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvAcqActualCount);
        tvAcqActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvAcqActual);
        tvAcqProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvAcqProjected);
        tvAcqProjectedCount = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvAcqProjectedCount);

        tvIFAAct = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvIFAAct);
        tvActActualCount = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvActActualCount);
        tvActActual = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvActActual);
        tvActProjected = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvActProjected);
        tvActProjectedCount = (TextView) bsFranchiseeOneUpView.findViewById(R.id.tvActProjectedCount);

        tvIFAAcq.setText("IFA'S Acquired");
        tvAcqActualCount.setText("8000");
        tvAcqActual.setText("Actual");
        tvAcqProjected.setText("9000");
        tvAcqProjectedCount.setText("Projected");

        tvIFAAct.setText("IFA'S Activated");
        tvActActualCount.setText("10000");
        tvActActual.setText("Actual");
        tvActProjected.setText("11000");
        tvActProjectedCount.setText("Projected");

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

package com.mosl.tabapp.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mosl.tabapp.R;
import com.mosl.tabapp.ui.MOSLCommonFragment;

/**
 * Created by raju on 2/24/2017.
 */

public class BusinessSnapshotHierarchyFragment extends MOSLCommonFragment {
    private View businessSnapshotView;
    private int tabPosition;
    private TextView tvCost, tvTarget, tvActual, tvBusinessChannel, tvLADAdvisory, tvLADAdvisoryActual, tvLADAdvisoryTarget,
            tvLADAdvisoryCost, tvTotal, tvTotalActual, tvTotalTarget, tvTotalCost, tvBranchSales, tvBranchSalesActual,
            tvBranchSalesTarget, tvBranchSalesCost, tvDirectReact, tvDirectReactActual, tvDirectReactTarget, tvDirectReactCost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        businessSnapshotView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_business_snapshot_tab);
        hideAppTitle(getClass().toString());
        setUpView(businessSnapshotView);
        return businessSnapshotView;
    }

    public static BusinessSnapshotHierarchyFragment newInstance(int tabPosition, String type) {
        BusinessSnapshotHierarchyFragment fragment = new BusinessSnapshotHierarchyFragment();
        Bundle args = new Bundle();
        args.putInt("position", tabPosition);
        args.putString("position", type);
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View businessSnapshotView) {
        tvBusinessChannel = (TextView) businessSnapshotView.findViewById(R.id.tvBusinessChannel);
        tvCost = (TextView) businessSnapshotView.findViewById(R.id.tvCost);
        tvTarget = (TextView) businessSnapshotView.findViewById(R.id.tvTarget);
        tvActual = (TextView) businessSnapshotView.findViewById(R.id.tvActual);
        tvLADAdvisory = (TextView) businessSnapshotView.findViewById(R.id.tvLADAdvisory);
        tvLADAdvisoryActual = (TextView) businessSnapshotView.findViewById(R.id.tvLADAdvisoryActual);
        tvLADAdvisoryTarget = (TextView) businessSnapshotView.findViewById(R.id.tvLADAdvisoryTarget);
        tvLADAdvisoryCost = (TextView) businessSnapshotView.findViewById(R.id.tvLADAdvisoryCost);
        tvTotal = (TextView) businessSnapshotView.findViewById(R.id.tvTotal);
        tvTotalActual = (TextView) businessSnapshotView.findViewById(R.id.tvTotalActual);
        tvTotalTarget = (TextView) businessSnapshotView.findViewById(R.id.tvTotalTarget);
        tvTotalCost = (TextView) businessSnapshotView.findViewById(R.id.tvTotalCost);
        tvBranchSales = (TextView) businessSnapshotView.findViewById(R.id.tvBranchSales);
        tvBranchSalesActual = (TextView) businessSnapshotView.findViewById(R.id.tvBranchSalesActual);
        tvBranchSalesTarget = (TextView) businessSnapshotView.findViewById(R.id.tvBranchSalesTarget);
        tvBranchSalesCost = (TextView) businessSnapshotView.findViewById(R.id.tvBranchSalesCost);
        tvDirectReact = (TextView) businessSnapshotView.findViewById(R.id.tvDirectReact);
        tvDirectReactActual = (TextView) businessSnapshotView.findViewById(R.id.tvDirectReactActual);
        tvDirectReactTarget = (TextView) businessSnapshotView.findViewById(R.id.tvDirectReactTarget);
        tvDirectReactCost = (TextView) businessSnapshotView.findViewById(R.id.tvDirectReactCost);

        tvBusinessChannel.setText("Business Channel");
        tvActual.setText("Actual");
        tvTarget.setText("Target");
        tvCost.setText("Cost");
        tvLADAdvisory.setText("LAD Advisory");
        tvLADAdvisoryActual.setText("11000");
        tvLADAdvisoryTarget.setText("10");
        tvLADAdvisoryCost.setText("10");
        tvTotal.setText("Total");
        tvTotalActual.setText("26,000");
        tvTotalTarget.setText("60");
        tvTotalCost.setText("60");
        tvBranchSales.setText("Branch Sales");
        tvBranchSalesActual.setText("13,000");
        tvBranchSalesTarget.setText("30");
        tvBranchSalesCost.setText("30");
        tvDirectReact.setText("Direct Reactivation");
        tvDirectReactActual.setText("12,000");
        tvDirectReactTarget.setText("20");
        tvDirectReactCost.setText("20");


    }


}

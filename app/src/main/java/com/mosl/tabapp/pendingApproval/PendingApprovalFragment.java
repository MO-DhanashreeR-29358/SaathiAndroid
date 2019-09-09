package com.mosl.tabapp.pendingApproval;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.pendingapproval.ApprovalSummaryByEmployeeItem;
import com.mosl.saathi.api.model.pendingapproval.ApprovalSummaryCountItem;
import com.mosl.saathi.api.model.pendingapproval.PendingApprovalRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.PendingApprovalModel;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;

/**
 * Created by sun on 3/8/2017.
 */

public class PendingApprovalFragment extends MOSLCommonFragment implements View.OnClickListener {
    View pendingApprovalView;
    TextView tvApproved, tvApprovedValue, tvPendingApproval, tvPendingApprovalValue;
    private RecyclerView RvPendApproval;
    private ArrayList<PendingApprovalModel> pendingApprovalList;
    private PendingApprovalAdapter pendingApprovalAdapter;

    Bundle args = new Bundle();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        pendingApprovalView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_pending_approvals);
        setAppTitle(getClass().toString(), "Pending Approvals");
        setUpView(pendingApprovalView);
        setUserVisibleHint(true);
        return pendingApprovalView;
    }

    private void setUpView(View pendingApprovalView) {
        tvApproved = (TextView) pendingApprovalView.findViewById(R.id.tvApproved);
        tvApprovedValue = (TextView) pendingApprovalView.findViewById(R.id.tvApprovedValue);
        tvPendingApproval = (TextView) pendingApprovalView.findViewById(R.id.tvPendingApproval);
        tvPendingApprovalValue =
            (TextView) pendingApprovalView.findViewById(R.id.tvPendingApprovalValue);
        RvPendApproval = (RecyclerView) pendingApprovalView.findViewById(R.id.RvPendApproval);
        tvApproved.setText("Approved");
        tvPendingApproval.setText("Pending Approvals");
    }

    public void onClick(View view) {
        switch (view.getId()) {
           /* case R.id.RvPendApproval:
                navigateTo(NAV_TO_Pending_For_Approval, args, getFragmentManager(), true);
                break;*/
        }
    }

    private void renderApprovalSummaryCount(UiModel<ApprovalSummaryCountItem> uiModel) {

        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        ApprovalSummaryCountItem data = uiModel.data;

        if (data != null) {
            String approveCount = stringOrZero(String.valueOf(data.approvedCount));
            String pendingCount = stringOrZero(String.valueOf(data.pendingCount));
            tvApprovedValue.setText(approveCount);
            tvPendingApprovalValue.setText(pendingCount);
        }
    }

    public void getAprovalCoutSummary() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            PendingApprovalRequest request =
                new PendingApprovalRequest(AccountDetails.getEmployeeCode());
            Disposable subscribe = saathiApi.postApprovalSummaryCount(request)
                .compose(applyUiModel())
                .subscribe(this::renderApprovalSummaryCount);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderApprovalSummaryByCount(
        UiModel<List<ApprovalSummaryByEmployeeItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        List<ApprovalSummaryByEmployeeItem> data = uiModel.data;

        if (data != null) {
            pendingApprovalList = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                ApprovalSummaryByEmployeeItem item = data.get(i);
                String pendingCount = stringOrEmpty(item.pendingCount);
                String referenceName = stringOrEmpty(item.approvedCount);
                String entityField = stringOrEmpty(item.entityField);

                pendingApprovalList.add(
                    new PendingApprovalModel(referenceName, pendingCount, entityField));
            }
            pendingApprovalAdapter = new PendingApprovalAdapter(pendingApprovalList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            RvPendApproval.setLayoutManager(mLayoutManager);
            RvPendApproval.setAdapter(pendingApprovalAdapter);
        }
    }

    public void getAprovalList() {

        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            PendingApprovalRequest request =
                new PendingApprovalRequest(AccountDetails.getEmployeeCode());
            Disposable subscribe = saathiApi.postApprovalSummaryByEmployee(request)
                .compose(applyUiModel())
                .subscribe(this::renderApprovalSummaryByCount);
        } else {
            noInternetConnectionDialog();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && pendingApprovalView != null) {
                getAprovalCoutSummary();
                getAprovalList();
            }
        } catch (Exception e) {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK",
                true, null);
        }
    }

    public class PendingApprovalAdapter
        extends RecyclerView.Adapter<PendingApprovalAdapter.MyViewHolder1> {

        private List<PendingApprovalModel> pendingApprovalList;

        public class MyViewHolder1 extends RecyclerView.ViewHolder {
            public TextView tvpendinglead;
            public TextView tvpendingcount;
            public LinearLayout llcounter;

            public MyViewHolder1(View view) {
                super(view);
                tvpendinglead = (TextView) view.findViewById(R.id.tvpendinglead);
                tvpendingcount = (TextView) view.findViewById(R.id.tvpendingcount);
                llcounter = (LinearLayout) view.findViewById(R.id.llcounter);
            }
        }

        public PendingApprovalAdapter(List<PendingApprovalModel> mpendingApprovalList) {
            this.pendingApprovalList = mpendingApprovalList;
        }

        @Override
        public PendingApprovalAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent,
            int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_pending_approvals, parent, false);

            return new PendingApprovalAdapter.MyViewHolder1(itemView);
        }

        @Override
        public void onBindViewHolder(PendingApprovalAdapter.MyViewHolder1 holder, int position) {
            final PendingApprovalModel pendingApprovalModel = pendingApprovalList.get(position);
            holder.tvpendinglead.setText(pendingApprovalModel.getmPendingLead());
            holder.tvpendingcount.setText(pendingApprovalModel.getmPendingLeadCount());
            holder.llcounter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putString(ENTITY_FIELD, pendingApprovalModel.getEntityField());
                    navigateTo(NAV_TO_PENDING_FOR_APPROVAL, args, getFragmentManager(), true);
                }
            });
        }

        @Override
        public int getItemCount() {
            return pendingApprovalList.size();
        }
    }
}


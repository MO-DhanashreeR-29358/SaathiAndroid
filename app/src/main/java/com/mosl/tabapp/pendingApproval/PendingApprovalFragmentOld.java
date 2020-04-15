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
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.PendingApprovalModel;
import com.mosl.tabapp.ui.MSFDialog;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.R.id;
import static com.mosl.tabapp.R.layout;
import static com.mosl.tabapp.R.string.motilal_oswal;
import static com.mosl.tabapp.R.string.txt_label_approved;
import static com.mosl.tabapp.R.string.txt_label_pending_approvals;
import static com.mosl.tabapp.constants.MOSLConstants.ENTITY_FIELD;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_SERVER_ERROR_OCCURRED;
import static com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_PENDING_FOR_APPROVAL;

public class PendingApprovalFragmentOld extends CommonFragment {
	private View pendingApprovalView;
	private TextView tvApprovedValue;
	private TextView tvPendingApprovalValue;
    private RecyclerView RvPendApproval;

    Bundle args = new Bundle();

    @Override
	public View onCreateView(@NotNull final LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        pendingApprovalView = super.onCreateView(inflater, container, savedInstanceState);
		container.post(() -> setAppTitle("Pending Approvals"));
        setUpView(pendingApprovalView);
        setUserVisibleHint(true);
        return pendingApprovalView;
    }

    @Override protected int getLayout() {
		return layout.frag_pending_approvals;
    }

    private void setUpView(View pendingApprovalView) {
		TextView tvApproved = pendingApprovalView.findViewById(id.tvApproved);
		tvApprovedValue = pendingApprovalView.findViewById(id.tvApprovedValue);
		TextView tvPendingApproval =
			pendingApprovalView.findViewById(id.tvPendingApproval);
        tvPendingApprovalValue =
			pendingApprovalView.findViewById(id.tvPendingApprovalValue);
      RvPendApproval = pendingApprovalView.findViewById(id.recyclerView);
		tvApproved.setText(getString(txt_label_approved));
		tvPendingApproval.setText(getString(txt_label_pending_approvals));
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

	private void getAprovalCoutSummary() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            PendingApprovalRequest request =
                    new PendingApprovalRequest(userManager.getUser().employeeCode);
            Disposable subscribe = getSaathiApi().postApprovalSummaryCount(request)
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
			ArrayList<PendingApprovalModel> pendingApprovalList = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                ApprovalSummaryByEmployeeItem item = data.get(i);
                String pendingCount = stringOrEmpty(item.pendingCount);
                String referenceName = stringOrEmpty(item.approvedCount);
                String entityField = stringOrEmpty(item.entityField);

                pendingApprovalList.add(
                    new PendingApprovalModel(referenceName, pendingCount, entityField));
            }
			PendingApprovalAdapter pendingApprovalAdapter =
				new PendingApprovalAdapter(pendingApprovalList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            RvPendApproval.setLayoutManager(mLayoutManager);
            RvPendApproval.setAdapter(pendingApprovalAdapter);
        }
    }

	private void getAprovalList() {

        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            PendingApprovalRequest request =
				new PendingApprovalRequest(
                        userManager.getUser().employeeCode);
            Disposable subscribe = getSaathiApi().postApprovalSummaryByEmployee(request)
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
            MSFDialog.alertDialog(requireContext(), 0,
				getResources().getString(motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK",
                true, null);
        }
    }

    public class PendingApprovalAdapter
        extends RecyclerView.Adapter<PendingApprovalAdapter.MyViewHolder1> {

        private List<PendingApprovalModel> pendingApprovalList;

		private PendingApprovalAdapter(List<PendingApprovalModel> mpendingApprovalList) {
            this.pendingApprovalList = mpendingApprovalList;
        }

		@NotNull @Override
        public PendingApprovalAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent,
            int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout.pending_approvals_list, parent, false);

            return new PendingApprovalAdapter.MyViewHolder1(itemView);
        }

        @Override
        public void onBindViewHolder(PendingApprovalAdapter.MyViewHolder1 holder, int position) {
            final PendingApprovalModel pendingApprovalModel = pendingApprovalList.get(position);
            holder.tvpendinglead.setText(pendingApprovalModel.getmPendingLead());
            holder.tvpendingcount.setText(pendingApprovalModel.getmPendingLeadCount());
			holder.llcounter.setOnClickListener(v -> {
				Bundle args = new Bundle();
				args.putString(ENTITY_FIELD, pendingApprovalModel.getEntityField());
				//navigateTo(NAV_TO_PENDING_FOR_APPROVAL, args, getFragmentManager(), true);
				navigator.openPage(NAVIGATE_TO_PENDING_FOR_APPROVAL, args, true, false);
			});
		}

		private class MyViewHolder1 extends RecyclerView.ViewHolder {
			private TextView tvpendinglead;
			private TextView tvpendingcount;
			private LinearLayout llcounter;

			private MyViewHolder1(View view) {
				super(view);
				tvpendinglead = view.findViewById(id.tvpendinglead);
				tvpendingcount = view.findViewById(id.tvpendingcount);
				llcounter = view.findViewById(id.llcounter);
			}
        }

        @Override
        public int getItemCount() {
            return pendingApprovalList.size();
        }
    }
}


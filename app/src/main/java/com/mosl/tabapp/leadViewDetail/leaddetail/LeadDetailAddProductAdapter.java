package com.mosl.tabapp.leadViewDetail.leaddetail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mosl.app.coreui.spinner.AdapterUtils;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.SaathiApi;
import com.mosl.saathi.api.model.crosssellingproduct.CrossSellingProductRequest;
import com.mosl.saathi.api.model.crosssellingproduct.DeleteLeadCrossSellingProductRequest;
import com.mosl.saathi.api.model.crosssellingproduct.SaveLeadCrossSellingProductRequest;
import com.mosl.saathi.api.model.lead.ConfigurationByStatusRequest;
import com.mosl.saathi.api.model.lead.CrossSellingProduct;
import com.mosl.saathi.api.model.lead.LeadProductMappingItem;
import com.mosl.saathi.api.model.lead.LookupItem;
import com.mosl.saathi.api.model.lead.LookupRequest;
import com.mosl.saathi.api.model.lead.StageMobileItem;
import com.mosl.saathi.api.model.lead.StageMobileRequest;
import com.mosl.saathi.api.model.lead.StageWiseConfiguration;
import com.mosl.saathi.api.model.lead.StatusItem;
import com.mosl.saathi.api.model.lead.StatusRequest;
import com.mosl.saathi.api.model.lead.request.UpdateLeadMappingRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CustomListPickerDialog;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.databinding.DialogLeadviewAddCrossSellingProductBinding;
import com.mosl.tabapp.databinding.DialogLeadviewAddProductBinding;
import com.mosl.tabapp.databinding.DialogLeadviewUpdateCrossSellingProductBinding;
import com.mosl.tabapp.databinding.IncludeEdittextLowmarginBinding;
import com.mosl.tabapp.model.EmployeeModel;
import com.mosl.tabapp.model.leaddetail.CrossSellingProductsModel;
import com.mosl.tabapp.model.leaddetail.LeadDetailAddProductModel;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.util.SpinnerAdapterFactory;

import io.reactivex.disposables.CompositeDisposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jakewharton.rxbinding3.widget.RxAdapterView.itemSelections;
import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.commons.rx.RxUtils.InvalidSelectionPredicate;
import static com.mosl.shared.util.Utility.stringOrEmpty;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.common.CRMMOSLCommon.setListViewHeightBasedOnChildren;
import static com.mosl.tabapp.common.CRMMOSLCommon.setListViewHeightBasedOnChildrenDetail;
import static com.mosl.tabapp.common.RowData.getSpinnerRowDataById;
import static com.mosl.tabapp.constants.MOSLConstants.NO;
import static com.mosl.tabapp.constants.MOSLConstants.POTENTIAL_INVEST_IN_PRODUCT;
import static com.mosl.tabapp.constants.MOSLConstants.SEARCH_LENTH;
import static com.mosl.tabapp.constants.MOSLConstants.YES;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class LeadDetailAddProductAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    Context mContext;
    private final SaathiApi saathiApi;
    private List<RowData> arrStatus, arrStage = new ArrayList<>();
    private List<RowData> arrCrossSellingProductId;
    private String employeeCode;
    private ListView lvAddProduct;
    private String etLeadId;
    private ArrayAdapter<RowData> arrAdapStatus, arrSatgeAdapter;
    private boolean isLowMargin;
    private List<LowMargin> lowMarginList = new ArrayList<>();
    private Boolean isLeadEditable = true;
    private Boolean isPMSProductStageStatus = false;
    private String userBusinessUnitId;

    private String statusId;
    private String pmsProductSpinnerValue = "";
    private List<LeadDetailAddProductModel> leadDetailAddProductModelList;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private String specialistEdit = "";

    @Override
    public int getCount() {
        return leadDetailAddProductModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return leadDetailAddProductModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private void addProduct(LeadDetailAddProductModel addProduct) {
        if (this.leadDetailAddProductModelList == null) {
            this.leadDetailAddProductModelList = new ArrayList<>();
        }

        this.leadDetailAddProductModelList.add(addProduct);
        notifyDataSetChanged();
        setListViewHeightBasedOnChildrenDetail(
                lvAddProduct);
    }

    private String newStageCodeStr = "";

    private LeadDetailAddProductModel getProduct(String id) {
        for (LeadDetailAddProductModel model : leadDetailAddProductModelList) {
            if (model.getInterestedIn().equals(id)) {
                return model;
            }
        }
        return null;
    }

    LeadDetailAddProductAdapter(SaathiApi api,
                                Context c,
                                List<LeadDetailAddProductModel> leadDetailAddProductModelList,
                                ListView addProductView,
                                String leadId,
                                Boolean isLeadEditable,
                                List<RowData> arrStatus,
                                ArrayAdapter<RowData> arrAdapStatus,
                                ArrayAdapter<RowData> arrSatgeAdapter,
                                String employeeCode,
                                String statusId,
                                String userBusinessUnitId,
                                String pmsProductSpinnerValue
    ) {
        inflater = LayoutInflater.from(c);
        saathiApi = api;
        mContext = c;
        lvAddProduct = addProductView;
        etLeadId = leadId;
        this.pmsProductSpinnerValue = pmsProductSpinnerValue;
        this.isLeadEditable = isLeadEditable;
        this.arrStatus = arrStatus;
        this.arrSatgeAdapter = arrSatgeAdapter;
        this.employeeCode = employeeCode;
        this.arrAdapStatus = arrAdapStatus;
        this.userBusinessUnitId = userBusinessUnitId;
        this.leadDetailAddProductModelList = leadDetailAddProductModelList;
    }

    private void updateProductData(String interestIn, String stageId, String stageValue,
                                   String statusId, String statusValue, String productSpecialist, String primaryRM) {
        for (LeadDetailAddProductModel model : leadDetailAddProductModelList) {
            if (model.getInterestedIn().equals(interestIn)) {
                model.setStageId(stageId);
                model.setStageValue(stageValue);
                model.setStatusId(statusId);
                model.setStatusValue(statusValue);
                model.setProductSpecialist(productSpecialist);
                model.setPrimaryRM(primaryRM);
                break;
            }
        }
        notifyDataSetChanged();
    }

    private String newStageNameStr = "";
    private boolean isPotentialPMSProduct = false;

    boolean checkProduct(String productName) {
        for (LeadDetailAddProductModel model : leadDetailAddProductModelList) {
            if (model.getInterestedIn().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final LeadDetailAddProductModel leadDetailAddProductModel =
                leadDetailAddProductModelList.get(i);
        //ItemListAddProductBinding holderBinding = ItemListAddProductBinding.inflate(inflater);

        view = inflater.inflate(R.layout.item_list_add_product, viewGroup, false);
        final LeadDetailAddProductAdapter.Holder
                holder = new LeadDetailAddProductAdapter.Holder();
        holder.tvProduct = view.findViewById(R.id.tvProduct);
        holder.tvProductSpecialist = view.findViewById(R.id.tvProductSpecialist);
        holder.tvPrimaryRM = view.findViewById(R.id.tvPrimaryRM);
        holder.tvStage = view.findViewById(R.id.tvStage);
        holder.tvProductID = view.findViewById(R.id.tvProductID);
        holder.tvStatus = view.findViewById(R.id.tvStatus);
        holder.llDeleteLeadDetail = view.findViewById(R.id.llDeleteLeadDetail);
        holder.llAddProduct = view.findViewById(R.id.llAddProduct);
        holder.llCrossSellingProducts =
                view.findViewById(R.id.llCrossSellingProducts);
        holder.llChildCrossSellingProduct =
                view.findViewById(R.id.llChildCrossSellingProduct);
        holder.tvAddInvestmentProduct =
                view.findViewById(R.id.tvAddInvestmentProduct);

        holder.tvProduct.setText(leadDetailAddProductModel.getInterestedInValue());
        holder.tvProductSpecialist.setText(leadDetailAddProductModel.getProductSpecialist());
        holder.tvPrimaryRM.setText(leadDetailAddProductModel.getPrimaryRM());
        holder.tvStage.setText(leadDetailAddProductModel.getStageValue());
        holder.tvProductID.setText(leadDetailAddProductModel.getInterestedInValue());
        holder.tvStatus.setText(leadDetailAddProductModel.getStatusValue());
        if (leadDetailAddProductModel.getInterestedInValue().equalsIgnoreCase("Equity")
                || leadDetailAddProductModel.getInterestedInValue().equalsIgnoreCase("MF")) {
            holder.llCrossSellingProducts.setVisibility(View.VISIBLE);
        } else {
            holder.llCrossSellingProducts.setVisibility(View.GONE);
        }

        for (final CrossSellingProductsModel mSection : leadDetailAddProductModel.getCrossSellingProductsModelList()) {
            @SuppressLint("ViewHolder") ViewGroup childSection =
                    (ViewGroup) inflater.inflate(R.layout.child_cross_selling_product, null);
            TextView tvCrossSellingProduct =
                    childSection.findViewById(R.id.tvCrossSellingProduct);
            TextView tvCrosellingStatus =
                    childSection.findViewById(R.id.tvCrosellingStatus);
            LinearLayout llCrossSellingUpdateProduct =
                    childSection.findViewById(R.id.llCrossSellingUpdateProduct);

            //ChildCrossSellingProductBinding childSectionBinding =
            //  ChildCrossSellingProductBinding.inflate(inflater);
            tvCrosellingStatus.setText(mSection.getCrossSellingStatusName());
            tvCrossSellingProduct.setText(
                    mSection.getCrossSellingProductName());
            llCrossSellingUpdateProduct.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(mContext);
                            DialogLeadviewUpdateCrossSellingProductBinding dialogBinding =
                                    DialogLeadviewUpdateCrossSellingProductBinding.inflate(inflater);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.getWindow()
                                    .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setContentView(dialogBinding.getRoot());
                            dialog.getWindow()
                                    .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                            dialog.getWindow()
                                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                            dialogBinding.spProductId.setEnabled(false);

                            newGetCrossSellingStatus(dialogBinding.spStatus,
                                    mSection.getCrossSellingStatusId(),
                                    dialogBinding.includeCustomProgress.customProgress);
                            newGetCrossSellingProductId(leadDetailAddProductModel.getInterestedIn(),
                                    dialogBinding.spProductId, mSection.getCrossSellingProductId(),
                                    dialogBinding.includeCustomProgress.customProgress);

                            disposable.add(
                                    itemSelections(dialogBinding.spStatus)
                                            .filter(InvalidSelectionPredicate)
                                            .subscribe(position -> {
                                                if (position > 0) {
                                                    dialogBinding.tvspStatusError.setVisibility(View.GONE);
                                                }
                                            }));

                            dialogBinding.btnSave.setOnClickListener(view14 -> {
                                if (dialogBinding.spStatus.getSelectedItemPosition() < 1) {
                                    dialogBinding.tvspStatusError.setVisibility(View.VISIBLE);
                                    dialogBinding.tvspStatusError.setText("Select Status");
                                    return;
                                }

                                if (CRMAppUtil.isNetworkEnabled(mContext)) {
                                    SaveLeadCrossSellingProductRequest productRequest =
                                            new SaveLeadCrossSellingProductRequest(
                                                    etLeadId,
                                                    leadDetailAddProductModel.getInterestedIn(),
                                                    ((RowData) dialogBinding.spStatus.getSelectedItem()).getKey(),
                                                    ((RowData) dialogBinding.spProductId.getSelectedItem()).getKey(),
                                                    mSection.getLeadCrossSellingProductId()
                                            );

                                    disposable.add(
                                            saathiApi.saveLeadCrossSellingProduct(productRequest)
                                                    .compose(applyUiModel())
                                                    .subscribe(uiModel -> {
                                                        //======  toggleProgress(uiModel.inProgress);

                                                        Throwable error = uiModel.error;
                                                        if (error != null) {
                                                            //=======    showOnFailurePopup(error);
                                                        }

                                                        Object data = uiModel.data;
                                                        if (data != null) {
                                                            alertDialog(mContext, 0,
                                                                    mContext.getString(
                                                                            R.string.motilal_oswal),
                                                                    "Cross Selling Product Saved successfully",
                                                                    "Ok",
                                                                    true,
                                                                    (action, data1) -> {
                                                                        if (action == MSFDialog.Action.OK) {
                                                                            //========    leadSection();
                                                                        }
                                                                    });
                                                        }
                                                    }));
                                } else {
                                    //=========  noInternetConnectionDialog();
                                }

                                dialog.dismiss();
                            });

                            dialogBinding.btnClose.setOnClickListener(view13 -> {
                                //======= hideKeyboard(leadViewDetailView);
                                dialog.dismiss();
                            });
                            dialog.show();
                        }
                    });
            holder.llChildCrossSellingProduct.addView(childSection);
        }
        holder.tvAddInvestmentProduct.setOnClickListener(v -> {
            if (isLeadEditable && !leadDetailAddProductModel.getIsProductDisabled()
                    .equals("1")) {
                final Dialog dialog = new Dialog(mContext);
                DialogLeadviewAddCrossSellingProductBinding dialogBinding =
                        DialogLeadviewAddCrossSellingProductBinding.inflate(inflater);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(dialogBinding.getRoot());
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow()
                        .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                newGetCrossSellingStatus(dialogBinding.spStatus, "",
                        dialogBinding.includeCustomProgress.customProgress);
                newGetCrossSellingProductId(leadDetailAddProductModel.getInterestedIn(),
                        dialogBinding.spProductId, "",
                        dialogBinding.includeCustomProgress.customProgress);

                disposable.add(
                        itemSelections(dialogBinding.spProductId)
                                .filter(InvalidSelectionPredicate)
                                .skip(1)
                                .subscribe(position -> {
                                    if (position > 0) {
                                        dialogBinding.tvspProductIdError.setVisibility(View.GONE);
                                    }
                                }));

                disposable.add(
                        itemSelections(dialogBinding.spStatus)
                                .filter(InvalidSelectionPredicate)
                                .subscribe(position -> {
                                    if (position > 0) {
                                        dialogBinding.tvspStatusError.setVisibility(View.GONE);
                                    }
                                }));

                dialogBinding.btnSave.setOnClickListener(view15 -> {
                    if (dialogBinding.spProductId.getSelectedItemPosition() < 1) {
                        dialogBinding.tvspProductIdError.setVisibility(View.VISIBLE);
                        dialogBinding.tvspProductIdError.setText("Select Product Id");
                        return;
                    } else if (dialogBinding.spStatus.getSelectedItemPosition() < 1) {
                        dialogBinding.tvspStatusError.setVisibility(View.VISIBLE);
                        dialogBinding.tvspStatusError.setText("Select Status");
                        return;
                    }

                    if (CRMAppUtil.isNetworkEnabled(mContext)) {
                        SaveLeadCrossSellingProductRequest productRequest =
                                new SaveLeadCrossSellingProductRequest(
                                        etLeadId,
                                        leadDetailAddProductModel.getInterestedIn(),
                                        ((RowData) dialogBinding.spStatus.getSelectedItem()).getKey(),
                                        ((RowData) dialogBinding.spProductId.getSelectedItem()).getKey()
                                );
                        disposable.add(
                                saathiApi.saveLeadCrossSellingProduct(productRequest)
                                        .compose(applyUiModel())
                                        .subscribe(uiModel -> {
                                            Object data = uiModel.data;
                                            if (data != null) {
                                                alertDialog(mContext, 0,
                                                        mContext.getString(
                                                                R.string.motilal_oswal),
                                                        "Cross Selling Product Saved Successfully",
                                                        "Ok", true,
                                                        (action, data12) -> {
                                                            if (action == MSFDialog.Action.OK) {
                                                                //======= leadSection();
                                                            }
                                                        });
                                            }
                                        }));
                    }
                    dialog.dismiss();
                });

                dialogBinding.btnClose.setOnClickListener(view12 -> {
                    dialog.dismiss();
                });
                dialog.show();
            }
        });
        if (leadDetailAddProductModel.isDefault()
                || leadDetailAddProductModel.isCrossSellingProduct()) {
            holder.llDeleteLeadDetail.setVisibility(View.GONE);
        } else {
            holder.llDeleteLeadDetail.setVisibility(View.VISIBLE);
        }
        holder.llDeleteLeadDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLeadEditable
                        && !leadDetailAddProductModel.getIsProductDisabled().equals("1")) {
                    if (!leadDetailAddProductModel.isDefault()) {
                        alertDialog(mContext, 0, "Delete Confirmation",
                                "Are you sure you want to delete the product", "Yes", "No", true,
                                new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action,
                                                                  Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            if (CRMAppUtil.isNetworkEnabled(mContext)) {
                                                DeleteLeadCrossSellingProductRequest
                                                        productRequest =
                                                        new DeleteLeadCrossSellingProductRequest(
                                                                etLeadId,
                                                                leadDetailAddProductModel.getInterestedIn(),
                                                                leadDetailAddProductModel.getLeadMappingProductId(),
                                                                leadDetailAddProductModel.getInterestedIn(),
                                                                leadDetailAddProductModel.getPrimaryRM(),
                                                                employeeCode,
                                                                CRMAppUtil.getDBDate(),
                                                                leadDetailAddProductModel.getStageId(),
                                                                leadDetailAddProductModel.getStatusId()
                                                        );
                                                disposable.add(
                                                        saathiApi.deleteLeadCrossSellingProduct(
                                                                productRequest)
                                                                .compose(applyUiModel())
                                                                .subscribe(
                                                                        this::renderDeleteCrossSellingProduct));
                                            }
                                        }
                                    }

                                    private void renderDeleteCrossSellingProduct(
                                            UiModel<Object> uiModel) {
                                        //=========== toggleProgress(uiModel.inProgress);

                                        Throwable error = uiModel.error;
                                        if (error != null) {
                                            //=====   showOnFailurePopup(error);
                                        }

                                        Object data = uiModel.data;
                                        if (data != null) {
                                            alertDialog(mContext, 0,
                                                    mContext.getString(R.string.motilal_oswal),
                                                    "Products Deleted Successfully",
                                                    "Ok", true, null);
                                            LeadDetailAddProductModel
                                                    model = getProduct(
                                                    leadDetailAddProductModel.getInterestedIn());
                                            leadDetailAddProductModelList.remove(model);
                                            notifyDataSetChanged();
                                            setListViewHeightBasedOnChildren(lvAddProduct);
                                        }
                                    }
                                });
                    } else {
                        alertDialog(mContext, 0,
                                mContext.getString(R.string.motilal_oswal),
                                "Can't Delete All Products", "OK", true, null);
                    }
                }
            }
        });

        holder.llAddProduct.setOnClickListener(view1 -> {
            if (isLeadEditable && !leadDetailAddProductModel.getIsProductDisabled()
                    .equals("1")) {
                editProduct(leadDetailAddProductModel.getInterestedIn(),
                        leadDetailAddProductModel.getStageId(),
                        leadDetailAddProductModel.getStatusId(),
                        leadDetailAddProductModel.getProductSpecialist(),
                        leadDetailAddProductModel.getLeadMappingProductId(),
                        leadDetailAddProductModel.getPrimaryRM(),
                        leadDetailAddProductModel.isDefault(),
                        leadDetailAddProductModel.getIsProSpeEditDisabled(),
                        leadDetailAddProductModel.getProductSpecialistId());
            }
        });
        return view;
    }

    private void getStageSelectList(final Spinner spStageDialog, final String stageId,
                                    final LinearLayout progressLayout) {
        if (CRMAppUtil.isNetworkEnabled(mContext)) {
            String stageID = isBlank(stageId) ? null : stageId;
            StageMobileRequest mobileRequest =
                    new StageMobileRequest(userBusinessUnitId, stageID);
            disposable.add(saathiApi.getStageMobileList(mobileRequest)
                    .compose(applyUiModel())
                    .subscribe(
                            uiModel -> renderStageMobile(uiModel, spStageDialog, progressLayout, stageID)));
        }
    }

    private void renderStageMobile(UiModel<List<StageMobileItem>> uiModel,
                                   final Spinner spStageDialog, final LinearLayout progressLayout, String stageId) {
        if (uiModel.inProgress) {
            progressLayout.setVisibility(View.VISIBLE);
        } else {
            progressLayout.setVisibility(View.GONE);
        }

        Throwable error = uiModel.error;
        if (error != null) {
            arrStage.clear();
//            showOnFailurePopup(error);
        }

        List<StageMobileItem> data = uiModel.data;
        if (data != null) {
            arrStage.clear();
            arrStage.add(new RowData(0, "-1", "Select", ""));
            for (int i = 0; i < data.size(); i++) {
                StageMobileItem item = data.get(i);
                arrStage.add(new RowData(i + 1,
                        String.valueOf(item.stageID),
                        stringOrEmpty(item.stageName),
                        stringOrEmpty(item.showPotentialPopup)
                ));
            }
            spStageDialog.setAdapter(arrSatgeAdapter);
            AdapterUtils.setSpinnerData(arrSatgeAdapter, spStageDialog, arrStage);
            RowData stageData = getSpinnerRowDataById(stageId, arrSatgeAdapter);
            if (stageData != null) {
                spStageDialog.setSelection(stageData.getPosition());
            }
        }
    }

    private void newGetStatus(String productId, final Spinner spStatus, final String statusId,
                              final LinearLayout progressLayout) {
        if (CRMAppUtil.isNetworkEnabled(mContext)) {
            disposable.add(
                    saathiApi.getStatusList(new StatusRequest(productId, "1"))
                            .compose(applyUiModel())
                            .subscribe(uiModel -> renderNewStatus(uiModel, spStatus, progressLayout,
                                    statusId)));
        }
    }

    private void renderNewStatus(UiModel<List<StatusItem>> uiModel,
                                 Spinner spStatus, final LinearLayout progressLayout, String statusId) {
        if (uiModel.inProgress) {
            progressLayout.setVisibility(View.VISIBLE);
        } else {
            progressLayout.setVisibility(View.GONE);
        }

        Throwable error = uiModel.error;
        if (error != null) {
            arrStatus.clear();
            //=======    showOnFailurePopup(error);
        }

        List<StatusItem> data = uiModel.data;
        if (data != null) {
            arrStatus.clear();
            arrStatus.add(new RowData(0, "-1", "Select"));
            for (int i = 0; i < data.size(); i++) {
                StatusItem statusItem = data.get(i);
                arrStatus.add(
                        new RowData(i + 1,
                                String.valueOf(statusItem.statusID),
                                stringOrEmpty(statusItem.statusName)
                        ));
            }
            spStatus.setAdapter(arrAdapStatus);
            AdapterUtils.setSpinnerData(arrAdapStatus, spStatus, arrStatus);
            RowData stageData = getSpinnerRowDataById(statusId, arrAdapStatus);
            if (stageData != null) {
                spStatus.setSelection(stageData.getPosition());
            }
        }
    }

    private void newGetCrossSellingProductId(final String intrestedIn, final Spinner spStatus,
                                             final String statusId, final LinearLayout progressLayout) {
        if (CRMAppUtil.isNetworkEnabled(mContext)) {
            CrossSellingProductRequest request =
                    new CrossSellingProductRequest(
                            userBusinessUnitId,
                            intrestedIn);
            disposable.add(saathiApi.getCrossSellingProduct(request)
                    .compose(applyUiModel())
                    .subscribe(uiModel ->
                            renderNewCrossSellingProduct(uiModel, statusId, spStatus, progressLayout)));
        } else {
            //====== noInternetConnectionDialog();
        }
    }

    private void renderNewCrossSellingProduct(UiModel<List<CrossSellingProduct>> uiModel,
                                              final String statusId, final Spinner spStatus, final LinearLayout progressLayout) {
        if (uiModel.inProgress) {
            progressLayout.setVisibility(View.VISIBLE);
        } else {
            progressLayout.setVisibility(View.GONE);
        }

        Throwable error = uiModel.error;
        if (error != null) {
            arrCrossSellingProductId.clear();
            //showOnFailurePopup(error);
        }

        List<CrossSellingProduct> data = uiModel.data;
        if (data != null) {
            arrCrossSellingProductId.clear();
            arrCrossSellingProductId.add(new RowData(0, "-1", "Select"));

            for (int i = 0; i < data.size(); i++) {
                CrossSellingProduct sellingProduct = data.get(i);
                arrCrossSellingProductId.add(
                        new RowData(i + 1,
                                stringOrEmpty(sellingProduct.productId),
                                stringOrEmpty(sellingProduct.productName)
                        ));
            }

            ArrayAdapter<RowData> arrAdapCrossSellingProductId =
                    SpinnerAdapterFactory.getDefaultAdapter(mContext);
            spStatus.setAdapter(arrAdapCrossSellingProductId);
            AdapterUtils.setSpinnerData(arrAdapCrossSellingProductId, spStatus,
                    arrCrossSellingProductId);
            RowData stageData = getSpinnerRowDataById(statusId, arrAdapCrossSellingProductId);
            if (stageData != null) {
                spStatus.setSelection(stageData.getPosition());
            }
        }
    }

    private void newGetCrossSellingStatus(final Spinner spStatus, final String statusId,
                                          final LinearLayout progressLayout) {
        if (CRMAppUtil.isNetworkEnabled(mContext)) {
            LookupRequest lookupRequest =
                    new LookupRequest(
                            userBusinessUnitId, 111,
                            null, null);
            disposable.add(saathiApi.getAllLookup(lookupRequest)
                    .compose(applyUiModel())
                    .subscribe(
                            uiModel -> renderCrossSellingStatus(uiModel, spStatus, progressLayout)));
        } else {
            //======== noInternetConnectionDialog();
        }
    }

    private void renderCrossSellingStatus(UiModel<List<LookupItem>> uiModel,
                                          final Spinner spStatus, final LinearLayout progressLayout) {
        if (uiModel.inProgress) {
            progressLayout.setVisibility(View.VISIBLE);
        } else {
            progressLayout.setVisibility(View.GONE);
        }

        Throwable error = uiModel.error;
        ArrayList<RowData> arrCrossSellingStatus;
        if (error != null) {
            //======  showOnFailurePopup(error);
        }

        List<LookupItem> data = uiModel.data;
        if (data != null) {
            arrCrossSellingStatus = new ArrayList<>();
            arrCrossSellingStatus.add(new RowData(0, "-1", "Select"));
            for (int i = 0; i < data.size(); i++) {
                LookupItem item = data.get(i);
                arrCrossSellingStatus.add(
                        new RowData(i + 1,
                                String.valueOf(item.lookUpid),
                                stringOrEmpty(item.lookUpName)
                        ));
            }
            ArrayAdapter<RowData> arrAdapCrossSellingStatus =
                    SpinnerAdapterFactory.getDefaultAdapter(mContext);
            spStatus.setAdapter(arrAdapCrossSellingStatus);
            AdapterUtils.setSpinnerData(arrAdapCrossSellingStatus, spStatus, arrCrossSellingStatus);

            RowData stageData = getSpinnerRowDataById(statusId, arrAdapCrossSellingStatus);
            if (stageData != null) {
                spStatus.setSelection(stageData.getPosition());
            }
        }
    }

    private void lowMargin(String productId, final LinearLayout progressLayout,
                           final LinearLayout llLowMarginAdd, final boolean isDefault) {
        if (CRMAppUtil.isNetworkEnabled(mContext)) {
            ConfigurationByStatusRequest request =
                    new ConfigurationByStatusRequest(productId);
            disposable.add(
                    saathiApi.getStageWiseConfigurationByStatus(request)
                            .compose(applyUiModel())
                            .subscribe(uiModel -> renderStageWiseConfiguration(uiModel, progressLayout,
                                    llLowMarginAdd, isDefault)));
        } else {
            //=========== noInternetConnectionDialog();
        }
    }

    String stringOrDefault(
            String input,
            String defaultVal
    ) {
        if (input != null && !input.isEmpty() && input.equalsIgnoreCase("null")) {
            return input;
        } else {
            return "0";
        }
    }

    private void renderStageWiseConfiguration(UiModel<List<StageWiseConfiguration>> uiModel,
                                              final LinearLayout progressLayout, final LinearLayout llLowMarginAdd,
                                              final boolean isDefault) {
        if (uiModel.inProgress) {
            progressLayout.setVisibility(View.VISIBLE);
        } else {
            progressLayout.setVisibility(View.GONE);
        }

        Throwable error = uiModel.error;
        if (error != null) {
            arrStatus.clear();
            llLowMarginAdd.removeAllViews();
            lowMarginList.clear();
            isLowMargin = false;
            //======== showOnFailurePopup(error);
        }

        List<StageWiseConfiguration> data = uiModel.data;
        if (data != null) {
            arrStatus.clear();
            lowMarginList.clear();
            if (data.size() > 0) {
                isLowMargin = true;
                for (StageWiseConfiguration configuration : data) {
                    lowMarginList.add(
                            new LowMargin(
                                    stringOrDefault(configuration.fieldDisplayName, "Low Margin"),
                                    stringOrEmpty(configuration.fieldSystemName),
                                    configuration.isMandatory,
                                    configuration.forDefaultProductOnly
                            ));
                }
                llLowMarginAdd.removeAllViews();

                for (LowMargin lowMargin : lowMarginList) {
                    if (isDefault == lowMargin.isDefaultProduct) {
                        isLowMargin = true;
                        IncludeEdittextLowmarginBinding childBinding =
                                IncludeEdittextLowmarginBinding.inflate(inflater);
                        if (lowMargin.isMandatory) {
                            childBinding.tvHead.setText(String.format("%s*", lowMargin.viewName));
                            childBinding.etSection.setHint("At least enter 3 digits");
                        } else {
                            childBinding.tvHead.setText(lowMargin.viewName);
                        }
                        lowMargin.setEditText(childBinding.etSection);
                        llLowMarginAdd.addView(childBinding.getRoot());
                    } else {
                        llLowMarginAdd.removeAllViews();
                        lowMarginList.clear();
                        isLowMargin = false;
                    }
                }
            } else {
                llLowMarginAdd.removeAllViews();
                lowMarginList.clear();
                isLowMargin = false;
            }
        }
    }

    private void editProduct(final String interestIn, final String stageId,
                             final String statusId, String productSpecialist, final String leadMappingId,
                             final String asignedTo, final boolean isDefault, String isProductSpecEnabled,
                             String productSpecialistId) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        DialogLeadviewAddProductBinding dialogBinding = DialogLeadviewAddProductBinding.bind(
                dialog.getLayoutInflater().inflate(
                        R.layout.dialog_leadview_add_product,
                        null
                )
        );
        dialog.setContentView(dialogBinding.getRoot());
        dialog.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialogBinding.dacProductSpecialist.setText(productSpecialist);
        specialistEdit = productSpecialistId;
        dialogBinding.dacProductSpecialist.setClearIconVisible(true);
        dialogBinding.dacProductSpecialist.setHint(mContext.getString(R.string.str_prod_spl));
        dialogBinding.dacProductSpecialist.setEnabled(isProductSpecEnabled.equals("1"));
        dialogBinding.dacProductSpecialist.setListener(() -> specialistEdit = "");
        dialogBinding.dacProductSpecialist.setOnClickListener(v -> {

            dialog.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            final CustomListPickerDialog searchListProductSpecialist =
                    new CustomListPickerDialog(mContext);

            searchListProductSpecialist.setThreshold(SEARCH_LENTH);
            searchListProductSpecialist.show();
            searchListProductSpecialist.setCustomTitle(
                    mContext.getString(R.string.str_prod_spl));
            CustomListPickerDialog.ListPickerListneable productSpecialistViewListner =
                    new CustomListPickerDialog.ListPickerListneable() {
                        @Override
                        public void onListItemSelect(Parcelable data) {
                            EmployeeModel clientModel = (EmployeeModel) data;
                            if (clientModel.isNoData()) {
                                return;
                            }
                            dialogBinding.dacProductSpecialist.setText(clientModel.getAttendeeName());
                            dialogBinding.dacProductSpecialist.setClearIconVisible(true);
                            specialistEdit = clientModel.getAttendeeCode();
                            searchListProductSpecialist.dismiss();
                        }

                        @Override
                        public void onSearchTextChanged(final CharSequence constraint) {
                            //========  searchProductSpecialist(constraint, searchListProductSpecialist);
                        }
                    };
            searchListProductSpecialist.setmListner(productSpecialistViewListner);
        });
        dialogBinding.actAssignTo.setText(asignedTo);
        dialogBinding.actAssignTo.setClearIconVisible(true);
        dialogBinding.actAssignTo.setHint(mContext.getString(R.string.str_assing));

        dialogBinding.actAssignTo.setOnClickListener(v -> {
            dialog.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            final CustomListPickerDialog searchList =
                    new CustomListPickerDialog(mContext);
            searchList.show();
            searchList.setCustomTitle(mContext.getString(R.string.str_assing));

            searchList.setThreshold(SEARCH_LENTH);
            CustomListPickerDialog.ListPickerListneable assingViewListner =
                    new CustomListPickerDialog.ListPickerListneable() {
                        @Override
                        public void onListItemSelect(Parcelable data) {
                            EmployeeModel clientModel = (EmployeeModel) data;
                            if (clientModel.isNoData()) {
                                return;
                            }
                            dialogBinding.actAssignTo.setText(clientModel.getAttendeeName());
                            dialogBinding.actAssignTo.setClearIconVisible(true);
                            searchList.dismiss();
                        }

                        @Override
                        public void onSearchTextChanged(final CharSequence constraint) {
                            //============  searchUserBusinessProfile(constraint, searchList);
                        }
                    };
            searchList.setmListner(assingViewListner);
        });
        dialogBinding.llInterestedIn.setVisibility(View.GONE);
        getStageSelectList(dialogBinding.spStage, stageId, dialogBinding.includeCustomProgress.customProgress);

        disposable.add(
                itemSelections(dialogBinding.spStage)
                        .skip(1)
                        .filter(InvalidSelectionPredicate)
                        .subscribe(position -> {
                            dialogBinding.tvspStageError.setVisibility(View.GONE);
                            dialogBinding.lowMarginAdd.removeAllViews();
                            lowMarginList.clear();
                            newStageCodeStr = ((RowData) dialogBinding.spStage.getSelectedItem()).getKey();
                            newStageNameStr =
                                    ((RowData) dialogBinding.spStage.getSelectedItem()).getValue();
                            if (((RowData) dialogBinding.spStage.getSelectedItem()).getShowPotentialPopup()
                                    .equalsIgnoreCase(YES)) {
                                if (pmsProductSpinnerValue.equals(NO)) {
                                    isPMSProductStageStatus = true;
                                }
                            } else {
                                isPMSProductStageStatus = false;
                            }
                            if (!newStageCodeStr.equalsIgnoreCase("-1")) {
                                newGetStatus(newStageCodeStr, dialogBinding.spStatus, statusId, dialogBinding.includeCustomProgress.customProgress);
                            }
                        }));

        disposable.add(
                itemSelections(dialogBinding.spStatus)
                        .filter(InvalidSelectionPredicate)
                        .subscribe(position -> {
                            dialogBinding.tvspStatusError.setVisibility(View.GONE);
                            dialogBinding.lowMarginAdd.removeAllViews();
                            lowMarginList.clear();
                            if (position != 0) {
                                lowMargin(((RowData) dialogBinding.spStatus.getSelectedItem()).getKey(),
                                        dialogBinding.includeCustomProgress.customProgress,
                                        dialogBinding.lowMarginAdd, isDefault);
                            }
                        }));

        dialogBinding.btnSave.setOnClickListener(view -> {
            if (dialogBinding.spStage.getSelectedItemPosition() < 1) {
                dialogBinding.tvspStageError.setVisibility(View.VISIBLE);
                dialogBinding.tvspStageError.setText("Select Stage");
                return;
            }
            if (dialogBinding.spStatus.getSelectedItemPosition() < 1) {
                dialogBinding.tvspStatusError.setVisibility(View.VISIBLE);
                dialogBinding.tvspStatusError.setText("Select Status");
                return;
            }
            Map dataMap = lowMarginSubmit(lowMarginList);
            if (isLowMargin && dataMap.size() == 0) {
                return;
            }
            final String primaryRM =
                    ""; // CRMAppUtil.isBlank(leadOwner.trim()) ? advisorCode : leadOwner.trim();

            RowData selectedStatus = (RowData) dialogBinding.spStatus.getSelectedItem();
            if (isPotentialPMSProduct || isPMSProductStageStatus) {
                MSFDialog.alertDialog(mContext, 0,
                        mContext.getString(R.string.motilal_oswal),
                        POTENTIAL_INVEST_IN_PRODUCT, YES, NO, true,
                        (action, data) -> {
                            if (action == MSFDialog.Action.OK) {
                                updateProduct(
                                        interestIn,
                                        leadMappingId,
                                        primaryRM,
                                        specialistEdit,
                                        newStageCodeStr,
                                        selectedStatus.getKey(),
                                        selectedStatus.getValue(), dialogBinding.actAssignTo.getText().toString(),
                                        YES, dataMap,
                                        dialogBinding.includeCustomProgress.customProgress
                                );
                            } else {
                                updateProduct(
                                        interestIn,
                                        leadMappingId,
                                        primaryRM,
                                        specialistEdit,
                                        newStageCodeStr,
                                        selectedStatus.getKey(),
                                        selectedStatus.getValue(),
                                        dialogBinding.actAssignTo.getText()
                                                .toString(), NO, dataMap,
                                        dialogBinding.includeCustomProgress.customProgress
                                );
                            }
                        });
            } else {
                updateProduct(

                        interestIn,
                        leadMappingId,
                        primaryRM,
                        specialistEdit,
                        newStageCodeStr,
                        selectedStatus.getKey(),
                        selectedStatus.getValue(),
                        dialogBinding.actAssignTo.getText().toString(), "", dataMap,
                        dialogBinding.includeCustomProgress.customProgress
                );
            }
            dialog.dismiss();
        });

        dialogBinding.btnClose.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private Map lowMarginSubmit(List<LowMargin> lowMarginList) {
        Map inputMap = new HashMap();
        for (LowMargin model : lowMarginList) {
            if (model.isMandatory) {
                if (model.getEditText().getText().toString().trim().length() < 3) {
                    model.getEditText().requestFocus();
                    model.getEditText().setError("Atleast enter 3 digits");
                    inputMap.clear();
                    return inputMap;
                }
                inputMap.put(model.keyName, model.getEditText().getText().toString().trim());
            }
        }
        return inputMap;
    }

    void updateProduct(String interestIn, String leadMappingId,
                       String primaryRM, String productSpecialistId, String newStageCodeStr,
                       String statusID, String statusValue, String actAssignTo, String pmsProductStatus,
                       Map dataMap,
                       final LinearLayout progressLayout) {
        if (CRMAppUtil.isNetworkEnabled(mContext)) {
            UpdateLeadMappingRequest mappingRequest = new UpdateLeadMappingRequest(
                    etLeadId,
                    interestIn,
                    leadMappingId,
                    interestIn,
                    primaryRM,
                    productSpecialistId,
                    employeeCode,
                    CRMAppUtil.getDBDate(),
                    newStageCodeStr,
                    newStageNameStr,
                    pmsProductStatus,
                    statusID,
                    statusValue,
                    actAssignTo,
                    dataMap
            );
            disposable.add(saathiApi.updateLeadProductMapping(mappingRequest)
                    .compose(applyUiModel())
                    .subscribe(uiModel -> renderUpdateLeadProductMapping(uiModel, mappingRequest, progressLayout)));
        }
    }

    private void renderUpdateLeadProductMapping(UiModel<List<LeadProductMappingItem>> uiModel,
                                                UpdateLeadMappingRequest mappingRequest, LinearLayout progressLayout) {
        if (uiModel.inProgress) {
            progressLayout.setVisibility(View.VISIBLE);
        } else {
            progressLayout.setVisibility(View.GONE);
        }
        Throwable error = uiModel.error;
        if (error != null) {
            //======     showOnFailurePopup(error);
        }

        List<LeadProductMappingItem> data = uiModel.data;
        if (data != null) {
            if (isLowMargin) {
                //====     navigator.openHome();
            } else {
                for (LeadProductMappingItem datum : data) {
                    String potentialPMSProduct = stringOrEmpty(datum.isPotentialPMSProduct);
                    isPotentialPMSProduct = potentialPMSProduct.isEmpty();
                    //=====   spPmsProduct.setSelection(getSpinnerPosition(potentialPMSProduct, arrPmsProduct));

                    if (String.valueOf(datum.interestedIn)
                            .equals(mappingRequest.interestedIn)) {
                        updateProductData(
                                mappingRequest.interestedIn,
                                mappingRequest.stageID,
                                mappingRequest.stageName,
                                mappingRequest.statusID,
                                mappingRequest.statusValue,
                                mappingRequest.productSpecialist,
                                mappingRequest.actAssignTo
                        );
                    } else {
                        //====== leadDetailAddProductAdapter.addProduct(getLeadDetailAddProductModel(datum));
                    }
                }

                alertDialog(mContext, 0,
                        mContext.getString(R.string.motilal_oswal),
                        "Products Updated Successfully", "Ok", true, null);
            }
        }
    }

    public class Holder {
        TextView tvProduct, tvProductSpecialist, tvPrimaryRM, tvStage, tvProductID, tvStatus,
                tvAddInvestmentProduct;
        LinearLayout llDeleteLeadDetail, llAddProduct, llCrossSellingProducts,
                llChildCrossSellingProduct;
    }
}

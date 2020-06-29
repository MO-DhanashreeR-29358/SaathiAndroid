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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.mosl.app.coreui.spinner.AdapterUtils;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.crosssellingproduct.CrossSellingProductRequest;
import com.mosl.saathi.api.model.crosssellingproduct.DeleteLeadCrossSellingProductRequest;
import com.mosl.saathi.api.model.crosssellingproduct.SaveLeadCrossSellingProductRequest;
import com.mosl.saathi.api.model.lead.ConfigurationByStatusRequest;
import com.mosl.saathi.api.model.lead.CrossSellingProduct;
import com.mosl.saathi.api.model.lead.LeadProductMappingItem;
import com.mosl.saathi.api.model.lead.LookupItem;
import com.mosl.saathi.api.model.lead.LookupRequest;
import com.mosl.saathi.api.model.lead.StageWiseConfiguration;
import com.mosl.saathi.api.model.lead.StatusItem;
import com.mosl.saathi.api.model.lead.StatusRequest;
import com.mosl.saathi.api.model.lead.request.UpdateLeadMappingRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CustomListPickerDialog;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.databinding.ChildCrossSellingProductBinding;
import com.mosl.tabapp.databinding.DialogLeadviewAddCrossSellingProductBinding;
import com.mosl.tabapp.databinding.DialogLeadviewUpdateCrossSellingProductBinding;
import com.mosl.tabapp.databinding.ItemListAddProductBinding;
import com.mosl.tabapp.leadViewDetail.LowMargin;
import com.mosl.tabapp.model.EmployeeModel;
import com.mosl.tabapp.model.leaddetail.CrossSellingProductsModel;
import com.mosl.tabapp.model.leaddetail.LeadDetailAddProductModel;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.util.SpinnerAdapterFactory;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.droidparts.widget.ClearableEditText;

import static com.jakewharton.rxbinding3.widget.RxAdapterView.itemSelections;
import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.commons.rx.RxUtils.InvalidSelectionPredicate;
import static com.mosl.tabapp.common.CRMMOSLCommon.setListViewHeightBasedOnChildren;
import static com.mosl.tabapp.common.CRMMOSLCommon.setListViewHeightBasedOnChildrenDetail;
import static com.mosl.tabapp.common.RowData.getSpinnerRowDataById;
import static com.mosl.tabapp.constants.MOSLConstants.SEARCH_LENTH;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;
import static com.mosl.tabapp.ws.CRMWebServiceHandler.getSaathiApi;

public class LeadDetailAddProductAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    Context mContext;
    private ListView lvAddProduct;
    private String etLeadId;
    private Boolean isLeadEditable = true;
    private List<LeadDetailAddProductModel> leadDetailAddProductModelList;
    private CompositeDisposable disposable = new CompositeDisposable();

    private LeadDetailAddProductAdapter(Context c,
        List<LeadDetailAddProductModel> leadDetailAddProductModelList,
        ListView addProductView,
        String leadId,
        Boolean isLeadEditable
    ) {
        inflater = LayoutInflater.from(c);
        mContext = c;
        lvAddProduct = addProductView;
        etLeadId = leadId;
        this.isLeadEditable = isLeadEditable;
        this.leadDetailAddProductModelList = leadDetailAddProductModelList;
    }

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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final LeadDetailAddProductModel leadDetailAddProductModel =
            leadDetailAddProductModelList.get(i);
        ItemListAddProductBinding holderBinding = ItemListAddProductBinding.inflate(inflater);

        //view = inflater.inflate(R.layout.item_list_add_product, viewGroup, false);

        return holderBinding.getRoot();

        /*final LeadViewDetailFragment.LeadDetailAddProductAdapter.Holder
            holder = new LeadViewDetailFragment.LeadDetailAddProductAdapter.Holder();
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
            view.findViewById(R.id.tvAddInvestmentProduct);*/

        holderBinding.tvProduct.setText(leadDetailAddProductModel.getInterestedInValue());
        holderBinding.tvProductSpecialist.setText(leadDetailAddProductModel.getProductSpecialist());
        holderBinding.tvPrimaryRM.setText(leadDetailAddProductModel.getPrimaryRM());
        holderBinding.tvStage.setText(leadDetailAddProductModel.getStageValue());
        holderBinding.tvProductID.setText(leadDetailAddProductModel.getInterestedInValue());
        holderBinding.tvStatus.setText(leadDetailAddProductModel.getStatusValue());
        if (leadDetailAddProductModel.getInterestedInValue().equalsIgnoreCase("Equity")
            || leadDetailAddProductModel.getInterestedInValue().equalsIgnoreCase("MF")) {
            holderBinding.llCrossSellingProducts.setVisibility(View.VISIBLE);
        } else {
            holderBinding.llCrossSellingProducts.setVisibility(View.GONE);
        }

        for (final CrossSellingProductsModel mSection : leadDetailAddProductModel.getCrossSellingProductsModelList()) {
            /*@SuppressLint("ViewHolder") ViewGroup childSection =
                (ViewGroup) requireActivity().getLayoutInflater()
                    .inflate(R.layout.child_cross_selling_product, null);
            TextView tvCrossSellingProduct =
                childSection.findViewById(R.id.tvCrossSellingProduct);
            TextView tvCrosellingStatus =
                childSection.findViewById(R.id.tvCrosellingStatus);
            LinearLayout llCrossSellingUpdateProduct =
                childSection.findViewById(R.id.llCrossSellingUpdateProduct);*/

            ChildCrossSellingProductBinding childSectionBinding =
                ChildCrossSellingProductBinding.inflate(inflater);
            childSectionBinding.tvCrosellingStatus.setText(mSection.getCrossSellingStatusName());
            childSectionBinding.tvCrossSellingProduct.setText(
                mSection.getCrossSellingProductName());
            childSectionBinding.llCrossSellingUpdateProduct.setOnClickListener(
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
                    /*final Spinner spProductIdDialog =
                        dialog.findViewById(R.id.spProductId);
                    final Spinner spStatusDialog = dialog.findViewById(R.id.spStatus);
                    final LinearLayout progressLayout =
                        dialog.findViewById(R.id.customProgress);*/
                        dialogBinding.spProductId.setEnabled(false);
                        //Text errors
                    /*tvCrossSellingProductIdError =
                        dialog.findViewById(R.id.tvspProductIdError);
                    tvCrossSellingStatusError =
                        dialog.findViewById(R.id.tvspStatusError);

                    Button btnSave = dialog.findViewById(R.id.btnSave);
                    Button btnClose = dialog.findViewById(R.id.btnClose);*/

                        newGetCrossSellingStatus(dialogBinding.spStatus,
                            mSection.getCrossSellingStatusId(),
                            dialogBinding.customProgress);
                        newGetCrossSellingProductId(leadDetailAddProductModel.getInterestedIn(),
                            dialogBinding.spProductId, mSection.getCrossSellingProductId(),
                            progressLayout);

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
                                //TODO Show Error
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
                                    getSaathiApi().saveLeadCrossSellingProduct(productRequest)
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
            holderBinding.llChildCrossSellingProduct.addView(childSectionBinding.getRoot());
        }
        holderBinding.tvAddInvestmentProduct.setOnClickListener(v -> {
            if (isLeadEditable && !leadDetailAddProductModel.getIsProductDisabled()
                .equals("1")) {
                final Dialog dialog = new Dialog(mContext);
                DialogLeadviewAddCrossSellingProductBinding dialogBinding =
                    DialogLeadviewAddCrossSellingProductBinding.inflate(inflater)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(dialogBinding.getRoot());
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow()
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
               /* final Spinner spProductIdDialog =
                    dialog.findViewById(R.id.spProductId);
                final Spinner spStatusDialog = dialog.findViewById(R.id.spStatus);
                final LinearLayout progressLayout =
                    dialog.findViewById(R.id.customProgress);
                //Text errors
                tvCrossSellingProductIdError =
                    dialog.findViewById(R.id.tvspProductIdError);
                tvCrossSellingStatusError =
                    dialog.findViewById(R.id.tvspStatusError);

                Button btnSave = dialog.findViewById(R.id.btnSave);
                Button btnClose = dialog.findViewById(R.id.btnClose);*/

                newGetCrossSellingStatus(dialogBinding.spStatus, "", dialogBinding.customProgress);
                newGetCrossSellingProductId(leadDetailAddProductModel.getInterestedIn(),
                    dialogBinding.spProductId, "", dialogBinding.customProgress);

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
                        //TODO Show Error
                        dialogBinding.tvspProductIdError.setVisibility(View.VISIBLE);
                        dialogBinding.tvspProductIdError.setText("Select Product Id");
                        return;
                    } else if (dialogBinding.spStatus.getSelectedItemPosition() < 1) {
                        //TODO Show Error
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
                            getSaathiApi().saveLeadCrossSellingProduct(productRequest)
                                .compose(applyUiModel())
                                .subscribe(uiModel -> {
                                    //========= toggleProgress(uiModel.inProgress);
                                    if (uiModel.inProgress) {
                                        //========   hideKeyboard(leadViewDetailView);
                                    }

                                    Throwable error = uiModel.error;
                                    if (error != null) {
                                        //=====  showOnFailurePopup(error);
                                    }

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
                    } else {
                        //============ noInternetConnectionDialog();
                    }

                    dialog.dismiss();
                });

                dialogBinding.btnClose.setOnClickListener(view12 -> {
                    //========= hideKeyboard(leadViewDetailView);
                    dialog.dismiss();
                });
                dialog.show();
            }
        });
        if (leadDetailAddProductModel.isDefault()
            || leadDetailAddProductModel.isCrossSellingProduct()) {
            holderBinding.llDeleteLeadDetail.setVisibility(View.GONE);
        } else {
            holderBinding.llDeleteLeadDetail.setVisibility(View.VISIBLE);
        }
        holderBinding.llDeleteLeadDetail.setOnClickListener(new View.OnClickListener() {
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
                                                    userManager.getUser().employeeCode,
                                                    CRMAppUtil.getDBDate(),
                                                    leadDetailAddProductModel.getStageId(),
                                                    leadDetailAddProductModel.getStatusId()
                                                );
                                            disposable.add(
                                                getSaathiApi().deleteLeadCrossSellingProduct(
                                                    productRequest)
                                                    .compose(applyUiModel())
                                                    .subscribe(
                                                        this::renderDeleteCrossSellingProduct));
                                        } else {
                                            //========== noInternetConnectionDialog();
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

        holderBinding.llAddProduct.setOnClickListener(view1 -> {
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

    private LeadDetailAddProductModel getProduct(String id) {
        for (LeadDetailAddProductModel model : leadDetailAddProductModelList) {
            if (model.getInterestedIn().equals(id)) {
                return model;
            }
        }
        return null;
    }

    private boolean checkProduct(String productName) {
        for (LeadDetailAddProductModel model : leadDetailAddProductModelList) {
            if (model.getInterestedIn().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
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

    private void newGetStatus(String productId, final Spinner spStatus, final String statusId,
        final LinearLayout progressLayout) {
        if (CRMAppUtil.isNetworkEnabled(mContext)) {
            disposable.add(
                getSaathiApi().getStatusList(new StatusRequest(productId, "1"))
                    .compose(applyUiModel())
                    .subscribe(uiModel -> renderNewStatus(uiModel, spStatus, progressLayout,
                        statusId)));
        } else {
            noInternetConnectionDialog();
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
            arrStatus = new ArrayList<>();
            showOnFailurePopup(error);
        }

        List<StatusItem> data = uiModel.data;
        if (data != null) {
            arrStatus = new ArrayList<>();
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
                    userManager.getUser().currentBusinessProfile.businessUnitId,
                    intrestedIn);
            disposable.add(getSaathiApi().getCrossSellingProduct(request)
                .compose(applyUiModel())
                .subscribe(uiModel ->
                    renderNewCrossSellingProduct(uiModel, statusId, spStatus, progressLayout)));
        } else {
            noInternetConnectionDialog();
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
            showOnFailurePopup(error);
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
                    userManager.getUser().currentBusinessProfile.businessUnitId, 111,
                    null, null);
            disposable.add(getSaathiApi().getAllLookup(lookupRequest)
                .compose(applyUiModel())
                .subscribe(
                    uiModel -> renderCrossSellingStatus(uiModel, spStatus, progressLayout)));
        } else {
            noInternetConnectionDialog();
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
            showOnFailurePopup(error);
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
                getSaathiApi().getStageWiseConfigurationByStatus(request)
                    .compose(applyUiModel())
                    .subscribe(uiModel -> renderStageWiseConfiguration(uiModel, progressLayout,
                        llLowMarginAdd, isDefault)));
        } else {
            noInternetConnectionDialog();
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
            arrStatus = new ArrayList<>();
            llLowMarginAdd.removeAllViews();
            lowMarginList.clear();
            isLowMargin = false;
            showOnFailurePopup(error);
        }

        List<StageWiseConfiguration> data = uiModel.data;
        if (data != null) {
            arrStatus = new ArrayList<>();
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
                    if (isDefault == lowMargin.isDefaultProduct()) {
                        isLowMargin = true;
                        @SuppressLint("InflateParams") View childValue = getLayoutInflater()
                            .inflate(R.layout.include_edittext_lowmargin, null);
                        TextView tvHead = childValue.findViewById(R.id.tvHead);
                        EditText edHead = childValue.findViewById(R.id.etSection);
                        if (lowMargin.isMandatory()) {
                            tvHead.setText(String.format("%s*", lowMargin.getViewName()));
                            edHead.setHint("At least enter 3 digits");
                        } else {
                            tvHead.setText(lowMargin.getViewName());
                        }
                        lowMargin.setEditText(edHead);
                        llLowMarginAdd.addView(childValue);
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
        dialog.setContentView(R.layout.dialog_leadview_add_product);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final Spinner spStageDialog = dialog.findViewById(R.id.spStage);
        final Spinner spStatusDialog = dialog.findViewById(R.id.spStatus);
        final LinearLayout llLowMarginAdd =
            dialog.findViewById(R.id.lowMarginAdd);
        tvspIntrestError = dialog.findViewById(R.id.tvspIntrestError);
        tvspStatusError = dialog.findViewById(R.id.tvspStatusError);
        tvspStageError = dialog.findViewById(R.id.tvspStageError);
        final LinearLayout progressLayout =
            dialog.findViewById(R.id.customProgress);
        LinearLayout llInterestedIn = dialog.findViewById(R.id.llInterestedIn);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        Button btnClose = dialog.findViewById(R.id.btnClose);
        final ClearableEditText productSpecialistEdit =
            dialog.findViewById(R.id.dacProductSpecialist);
        productSpecialistEdit.setText(productSpecialist);
        specialistEdit = productSpecialistId;
        productSpecialistEdit.setClearIconVisible(true);
        productSpecialistEdit.setHint(mContext.getString(R.string.str_prod_spl));
        if (isProductSpecEnabled.equals("1")) {
            productSpecialistEdit.setEnabled(false);
        } else {
            productSpecialistEdit.setEnabled(true);
        }
        productSpecialistEdit.setListener(() -> specialistEdit = "");
        productSpecialistEdit.setOnClickListener(v -> {

            requireActivity().getWindow()
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
                        productSpecialistEdit.setText(clientModel.getAttendeeName());
                        productSpecialistEdit.setClearIconVisible(true);
                        specialistEdit = clientModel.getAttendeeCode();
                        searchListProductSpecialist.dismiss();
                    }

                    @Override
                    public void onSearchTextChanged(final CharSequence constraint) {
                        searchProductSpecialist(constraint, searchListProductSpecialist);
                    }
                };
            searchListProductSpecialist.setmListner(productSpecialistViewListner);
        });
        final ClearableEditText actAssignTo =
            dialog.findViewById(R.id.actAssignTo);
        actAssignTo.setText(asignedTo);
        actAssignTo.setClearIconVisible(true);
        actAssignTo.setHint(mContext.getString(R.string.str_assing));

        actAssignTo.setOnClickListener(v -> {
            requireActivity().getWindow()
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
                        actAssignTo.setText(clientModel.getAttendeeName());
                        actAssignTo.setClearIconVisible(true);
                        searchList.dismiss();
                    }

                    @Override
                    public void onSearchTextChanged(final CharSequence constraint) {
                        searchUserBusinessProfile(constraint, searchList);
                    }
                };
            searchList.setmListner(assingViewListner);
        });
        llInterestedIn.setVisibility(View.GONE);
        GetStageSelectList(spStageDialog, stageId, progressLayout);

        disposable.add(
            itemSelections(spStageDialog)
                .filter(InvalidSelectionPredicate)
                .subscribe(position -> {
                    if (tvspStageError != null) {
                        tvspStageError.setVisibility(View.GONE);
                    }
                    llLowMarginAdd.removeAllViews();
                    lowMarginList.clear();
                    newStageCodeStr = ((RowData) spStageDialog.getSelectedItem()).getKey();
                    newStageNameStr = ((RowData) spStageDialog.getSelectedItem()).getValue();
                    if (((RowData) spStageDialog.getSelectedItem()).getSchmeID()
                        .equalsIgnoreCase(YES)) {
                        if (spPmsProduct.getSelectedItem().toString().equals(NO)) {
                            isPMSProductStageStatus = true;
                        }
                    } else {
                        isPMSProductStageStatus = false;
                    }
                    if (!newStageCodeStr.equalsIgnoreCase("-1")) {
                        newGetStatus(newStageCodeStr, spStatusDialog, statusId, progressLayout);
                    }
                }));

        disposable.add(
            itemSelections(spStatusDialog)
                .filter(InvalidSelectionPredicate)
                .subscribe(position -> {
                    if (tvspStatusError != null) {
                        tvspStatusError.setVisibility(View.GONE);
                    }
                    llLowMarginAdd.removeAllViews();
                    lowMarginList.clear();
                    if (position != 0) {
                        lowMargin(((RowData) spStatusDialog.getSelectedItem()).getKey(),
                            progressLayout, llLowMarginAdd, isDefault);
                    }
                }));

        btnSave.setOnClickListener(view -> {
            if (spStageDialog.getSelectedItemPosition() < 1) {
                tvspStageError.setVisibility(View.VISIBLE);
                tvspStageError.setText("Select Stage");
                //TODO Show Error
                return;
            }
            if (spStatusDialog.getSelectedItemPosition() < 1) {
                //TODO Show Error
                tvspStatusError.setVisibility(View.VISIBLE);
                tvspStatusError.setText("Select Status");
                return;
            }
            Map dataMap = lowMarginSubmit(lowMarginList);
            if (isLowMargin && dataMap.size() == 0) {
                return;
            }
            final String primaryRM =
                CRMAppUtil.isBlank(leadOwner.trim()) ? advisorCode : leadOwner.trim();

            RowData selectedStatus = (RowData) spStatusDialog.getSelectedItem();
            if (isPotentialPMSProduct || isPMSProductStageStatus) {
                MSFDialog.alertDialog(requireActivity(), 0,
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
                                selectedStatus.getValue(), actAssignTo.getText().toString(),
                                YES, dataMap
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
                                actAssignTo.getText()
                                    .toString(), NO, dataMap
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
                    actAssignTo.getText().toString(), "", dataMap
                );
            }
            dialog.dismiss();
        });

        btnClose.setOnClickListener(view -> {
            hideKeyboard(leadViewDetailView);
            dialog.dismiss();
        });
        dialog.show();
    }

    void updateProduct(String interestIn, String leadMappingId,
        String primaryRM, String productSpecialistId, String newStageCodeStr,
        String statusID, String statusValue, String actAssignTo, String pmsProductStatus,
        Map dataMap) {
        if (CRMAppUtil.isNetworkEnabled(mContext)) {
            showProgress();
            UpdateLeadMappingRequest mappingRequest = new UpdateLeadMappingRequest(
                etLeadID.getText().toString().trim(),
                interestIn,
                leadMappingId,
                interestIn,
                primaryRM,
                productSpecialistId,
                userManager.getUser().employeeCode,
                CRMAppUtil.getDBDate(),
                newStageCodeStr,
                newStageNameStr,
                pmsProductStatus,
                statusID,
                statusValue,
                actAssignTo,
                dataMap
            );
            disposable.add(getSaathiApi().updateLeadProductMapping(mappingRequest)
                .compose(applyUiModel())
                .subscribe(uiModel -> renderUpdateLeadProductMapping(uiModel, mappingRequest)));
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderUpdateLeadProductMapping(UiModel<List<LeadProductMappingItem>> uiModel,
        UpdateLeadMappingRequest mappingRequest) {
        toggleProgress(uiModel.inProgress);
        if (uiModel.inProgress) {
            hideKeyboard(leadViewDetailView);
        }

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        List<LeadProductMappingItem> data = uiModel.data;
        if (data != null) {
            if (isLowMargin) {
                navigator.openHome();
            } else {
                for (LeadProductMappingItem datum : data) {
                    String potentialPMSProduct = stringOrEmpty(datum.isPotentialPMSProduct);
                    isPotentialPMSProduct = potentialPMSProduct.isEmpty();
                    spPmsProduct.setSelection(
                        getSpinnerPosition(potentialPMSProduct, arrPmsProduct));

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
                        leadDetailAddProductAdapter.addProduct(
                            getLeadDetailAddProductModel(datum));
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

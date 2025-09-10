package com.mosl.tabapp.saathiIndirect.leadViewDetail.leadDetail

import android.view.View
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.model.leaddetail.CrossSellingProductsModel
import com.mosl.tabapp.model.leaddetail.LeadDetailAddProductModel

interface IndirectLeadDetailAddProductCallBack {
    fun newInvestmentProductAdded(
        position: Int,
        crossSellingProductsModel: CrossSellingProductsModel
    )

    fun updateInvestmentProduct(
        position: Int,
        selectedRowData: RowData,
        productId: String
    )

    fun showFailureDialog(error: Throwable)

    fun showNoInternetDialog()

    fun getErrorFailure(error: Throwable): String

    fun toggleProgressDialog(inProgress: Boolean)

    fun hideKeyBoard(view: View)

    fun openHome()
    fun updateProduct(leadDetailAddProductList: List<LeadDetailAddProductModel>)
}
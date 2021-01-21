package com.mosl.tabapp.leadViewDetail.leaddetail

import android.view.View
import com.mosl.tabapp.common.RowData
import com.mosl.tabapp.model.leaddetail.CrossSellingProductsModel

interface LeadDetailAddProductCallBack {
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
}
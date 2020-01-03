package com.mosl.tabapp.dashboard.ausfb.target

import android.content.Context
import android.view.View
import com.mosl.mobile.domain.autarget.TableItemModel
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.TargetTabelItemBinding
import com.xwray.groupie.databinding.BindableItem
import io.reactivex.subjects.PublishSubject

class TargetTabelItem(
    val tableItemModel: TableItemModel,
    private val publishSubject: PublishSubject<AuWidgetUiEvent>,
    val context: Context,
    val view: AuTargetView
) : BindableItem<TargetTabelItemBinding>() {

    override fun getLayout(): Int {
        return R.layout.target_tabel_item
    }


    override fun bind(viewBinding: TargetTabelItemBinding, position: Int) {
        viewBinding.apply {

            txtValue.setText(tableItemModel.value)

            if (tableItemModel.displayText.equals(TARGET) && (view.getPeriod()?.value.equals(DAY)
                        || view.getPeriod()?.value.equals(WTD))
            ) {
                txtName.setText(String.format("MTD %s", tableItemModel.displayText))
            } else {
                txtName.setText(tableItemModel.displayText)
            }
            if (tableItemModel.ord == 1) {
                verticalView.visibility = View.GONE
            } else {
                verticalView.visibility = View.VISIBLE

            }
        }
    }

    companion object {
        const val TARGET = "Target"
        const val DAY = "Day"
        const val WTD = "WTD"
    }

}
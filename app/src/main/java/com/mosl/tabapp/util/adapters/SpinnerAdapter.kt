package com.mosl.tabapp.util.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.mosl.mobile.domain.SpinnerData
import com.mosl.tabapp.R

class SpinnerAdapter<T : SpinnerData>(
    context: Context,
    private val spinnerData: List<T>,
    private val layoutResource: Int = R.layout.mdropdown,
    private val textViewResourceId: Int = R.layout.simple_spinner_item
) : ArrayAdapter<T>(context, layoutResource, spinnerData) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(
            position, parent, textViewResourceId,
            LayoutInflater.from(context)
        )
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createDropDownViewFromResource(
            position,
            convertView,
            parent,
            LayoutInflater.from(context)
        )
    }

    private fun createViewFromResource(
        position: Int,
        parent: ViewGroup?,
        res: Int,
        layoutInflater: LayoutInflater
    ): View {
        val view: TextView = layoutInflater.inflate(res, parent, false) as TextView
        view.text = context.getText(spinnerData[position].labelRes)
        return view
    }

    private fun createDropDownViewFromResource(
        position: Int,
        convertView: View?,
        parent: ViewGroup?,
        layoutInflater: LayoutInflater
    ): View {
        val view: TextView = convertView as TextView? ?: layoutInflater.inflate(
            layoutResource,
            parent,
            false
        ) as TextView
        view.text = context.getText(spinnerData[position].labelRes)
        return view
    }
}

package com.mosl.tabapp.util

import android.content.Context
import android.widget.ArrayAdapter
import com.mosl.mobile.domain.SpinnerData
import com.mosl.tabapp.R
import com.mosl.tabapp.util.adapters.SpinnerAdapter


object SpinnerAdapterFactory {
    @JvmStatic
    @JvmOverloads
    fun <T> getDefaultAdapter(
        context: Context,
        data: List<T> = mutableListOf()
    ): ArrayAdapter<T> {
        val adapter = ArrayAdapter<T>(context, R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(R.layout.mdropdown)
        return adapter
    }

    @JvmStatic
    @JvmOverloads
    fun <T : SpinnerData> getSpinnerAdapter(
        context: Context,
        data: List<T> = mutableListOf()
    ): ArrayAdapter<T> {
        return SpinnerAdapter(context, data)
    }


}
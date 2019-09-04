package com.mosl.tabapp.bankmeeting.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mosl.tabapp.R
import com.mosl.tabapp.bankmeeting.adapter.ClientNameAdapter.MyViewHolder1
import com.mosl.tabapp.bankmeeting.model.MeetingClientName
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ui.MSFDialog.alertDialog
import java.util.TreeSet

class ClientNameAdapter(
  private var productList: MutableList<MeetingClientName>,
  private val mContext: Context
) : RecyclerView.Adapter<MyViewHolder1>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MyViewHolder1 {
    val itemView = LayoutInflater.from(parent.context)
      .inflate(R.layout.list_create_meeting_product, parent, false)

    return MyViewHolder1(itemView)
  }

  override fun onBindViewHolder(
    holder: MyViewHolder1,
    position: Int
  ) {
    val (clientName) = productList[position]
    holder.tvProductName.text = clientName
    holder.ivDeleteProduct.setOnClickListener { view ->
      alertDialog(
        mContext, 0, mContext.resources.getString(R.string.motilal_oswal),
        "Are you sure you want to delete client name: "
            + clientName
            + " ?", "Yes", "No", true
      ) { action, data ->
        if (action == MSFDialog.Action.OK) {
          deleteDocument(clientName)
        }
      }
    }
  }

  override fun getItemCount(): Int {
    return productList.size
  }

  /**
   * return total documents in list
   */
  val count: Int
    get() = productList.size

  val listProduct: List<MeetingClientName>
    get() = this.productList

  fun getDocumentById(id: String): MeetingClientName? {
    for (model in productList) {
      if (model.clientName == id) {
        return model
      }
    }
    return null
  }

  /**
   * this will remove item from list and refresh the list
   */
  fun deleteDocument(id: String) {
    val model = getDocumentById(id)
    productList.remove(model)
    notifyDataSetChanged()
  }

  fun addProduct(meetingClientName: MeetingClientName) {
    if (this.productList == null) {
      this.productList = ArrayList()
    }
    val hs =
      TreeSet<MeetingClientName> { (a), (b) -> a.compareTo(b) }
    hs.addAll(this.productList)
    hs.add(meetingClientName)
    this.productList.clear()
    this.productList.addAll(hs)
    notifyDataSetChanged()
  }

  fun deleteAllProduct() {
    this.productList.clear()
    notifyDataSetChanged()
  }

  inner class MyViewHolder1(view: View) : RecyclerView.ViewHolder(view) {
    var ivDeleteProduct: ImageView = view.findViewById(R.id.ivDeleteProduct)
    var tvProductName: TextView = view.findViewById(R.id.tvProductName)

  }
}
package com.mosl.tabapp.bankmeeting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mosl.tabapp.R;
import com.mosl.tabapp.bankmeeting.model.MeetingClientName;
import com.mosl.tabapp.ui.MSFDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class ClientNameAdapter extends RecyclerView.Adapter<ClientNameAdapter.MyViewHolder1> {

  private List<MeetingClientName> productList;
  private Context mContext;

  public ClientNameAdapter(List<MeetingClientName> mproductList, Context context) {
    this.productList = mproductList;
    this.mContext = context;
  }

  public MeetingClientName getDocumentById(String id) {
    for (MeetingClientName model : productList) {
      if (model.clientName.equals(id)) {
        return model;
      }
    }
    return null;
  }

  /**
   * return total documents in list
   */
  public int getCount() {
    return productList.size();
  }

  /**
   * this will remove item from list and refresh the list
   */
  public void deleteDocument(String id) {
    MeetingClientName model = getDocumentById(id);
    productList.remove(model);
    notifyDataSetChanged();
  }

  public void addProduct(MeetingClientName meetingClientName) {
    if (this.productList == null) {
      this.productList = new ArrayList<>();
    }
    TreeSet<MeetingClientName> hs = new TreeSet<>((a, b) -> a.clientName.compareTo(b.clientName));
    hs.addAll(this.productList);
    hs.add(meetingClientName);
    this.productList.clear();
    this.productList.addAll(hs);
    notifyDataSetChanged();
  }

  public void deleteAllProduct() {
    this.productList.clear();
    notifyDataSetChanged();
  }

  public List<MeetingClientName> getListProduct() {
    return this.productList;
  }

  @Override
  public ClientNameAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent,
      int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_create_meeting_product, parent, false);

    return new ClientNameAdapter.MyViewHolder1(itemView);
  }

  @Override
  public void onBindViewHolder(final ClientNameAdapter.MyViewHolder1 holder,
      int position) {
    final MeetingClientName documentModel = productList.get(position);
    holder.tvProductName.setText(documentModel.clientName);
    holder.ivDeleteProduct.setOnClickListener(
        view -> alertDialog(mContext, 0, mContext.getResources().getString(R.string.motilal_oswal),
            "Are you sure you want to delete client name: "
                + documentModel.clientName
                + " ?", "Yes", "No", true, (action, data) -> {
              if (action == MSFDialog.Action.OK) {
                deleteDocument(documentModel.clientName);
              }
            }));
  }

  @Override
  public int getItemCount() {
    return productList.size();
  }

  public class MyViewHolder1 extends RecyclerView.ViewHolder {
    public ImageView ivDeleteProduct;
    public TextView tvProductName;

    public MyViewHolder1(View view) {
      super(view);
      tvProductName = view.findViewById(R.id.tvProductName);
      ivDeleteProduct = view.findViewById(R.id.ivDeleteProduct);
    }
  }
}

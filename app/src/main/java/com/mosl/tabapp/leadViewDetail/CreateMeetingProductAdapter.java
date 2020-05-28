package com.mosl.tabapp.leadViewDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.mosl.tabapp.R;
import com.mosl.tabapp.ui.MSFDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import org.jetbrains.annotations.NotNull;

import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Raju on 20-07-2018.
 */

public class CreateMeetingProductAdapter
    extends RecyclerView.Adapter<CreateMeetingProductAdapter.MyViewHolder1> {

  private List<CreateMeetingProductModel> productList = new ArrayList<CreateMeetingProductModel>();
  private Context mContext;

  public CreateMeetingProductAdapter(Context context) {
    this.mContext = context;
  }

  private CreateMeetingProductModel getDocumentById(String id) {
    for (CreateMeetingProductModel model : productList) {
      if (model.getId().equals(id)) {
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
  private void deleteDocument(String id) {
    CreateMeetingProductModel model = getDocumentById(id);
    productList.remove(model);
    notifyDataSetChanged();
  }

  public void addProduct(CreateMeetingProductModel createMeetingProductModel) {
    TreeSet<CreateMeetingProductModel> hs =
        new TreeSet<>((a, b) -> a.getDocumentName().compareTo(b.getDocumentName()));
    hs.addAll(this.productList);
    hs.add(createMeetingProductModel);
    this.productList.clear();
    this.productList.addAll(hs);
    notifyDataSetChanged();
  }

  public void deleteAllProduct() {
    this.productList.clear();
    notifyDataSetChanged();
  }

  public List<CreateMeetingProductModel> getListProduct() {
    return this.productList;
  }

  @NotNull @Override
  public CreateMeetingProductAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent,
      int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_create_meeting_product, parent, false);

    return new MyViewHolder1(itemView);
  }

  @Override
  public void onBindViewHolder(final CreateMeetingProductAdapter.MyViewHolder1 holder,
      int position) {
    final CreateMeetingProductModel documentModel = productList.get(position);
    holder.tvProductName.setText(documentModel.getDocumentName());
    holder.ivDeleteProduct.setOnClickListener(
        view -> alertDialog(mContext, 0, mContext.getResources().getString(R.string.motilal_oswal),
            "Are you sure you want to delete this product:"
                + documentModel.getDocumentName()
                + " ?", "Yes", "No", true, (action, data) -> {
              if (action == MSFDialog.Action.OK) {
                deleteDocument(documentModel.getId());
              }
            }));
  }

  @Override
  public int getItemCount() {
    return productList.size();
  }

  static class MyViewHolder1 extends RecyclerView.ViewHolder {
    ImageView ivDeleteProduct;
    TextView tvProductName;

    MyViewHolder1(View view) {
      super(view);
      tvProductName = view.findViewById(R.id.tvProductName);
      ivDeleteProduct = view.findViewById(R.id.ivDeleteProduct);
    }
  }
}

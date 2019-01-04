package com.mosl.tabapp.leadViewDetail;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mosl.tabapp.R;
import com.mosl.tabapp.ui.MSFDialog;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Raju on 20-07-2018.
 */

public class CreateMeetingProductAdapter extends RecyclerView.Adapter<CreateMeetingProductAdapter.MyViewHolder1> {

    private List<CreateMeetingProductModel> productList;
    private Context mContext;

    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        public ImageView ivDeleteProduct;
        public TextView tvProductName;


        public MyViewHolder1(View view) {
            super(view);
            tvProductName = (TextView) view.findViewById(R.id.tvProductName);
            ivDeleteProduct = (ImageView) view.findViewById(R.id.ivDeleteProduct);

        }
    }

    public CreateMeetingProductAdapter(List<CreateMeetingProductModel> mproductList, Context context) {
        this.productList = mproductList;
        this.mContext = context;
    }

    public CreateMeetingProductModel getDocumentById(String id) {
        for (CreateMeetingProductModel model : productList) {
            if (model.getId().equals(id)) {
                return model;
            }
        }
        return null;
    }

    /**
     * return total documents in list
     *
     * @return
     */
    public int getCount() {
        return productList.size();
    }

    /**
     * this will remove item from list and refresh the list
     *
     * @param id
     */
    public void deleteDocument(String id) {
        CreateMeetingProductModel model = getDocumentById(id);
        productList.remove(model);
        notifyDataSetChanged();
    }

    public void addProduct(CreateMeetingProductModel createMeetingProductModel) {
        if (this.productList == null) {
            this.productList = new ArrayList<>();
        }
        TreeSet<CreateMeetingProductModel> hs = new TreeSet<>(new Comparator<CreateMeetingProductModel>() {
            @Override
            public int compare(CreateMeetingProductModel a, CreateMeetingProductModel b) {
                return a.getDocumentName().compareTo(b.getDocumentName());
            }
        });
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

    @Override
    public CreateMeetingProductAdapter.MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_create_meeting_product, parent, false);

        return new CreateMeetingProductAdapter.MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(final CreateMeetingProductAdapter.MyViewHolder1 holder, int position) {
        final CreateMeetingProductModel documentModel = productList.get(position);
        holder.tvProductName.setText(documentModel.getDocumentName());
        holder.ivDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog(mContext, 0, mContext.getResources().getString(R.string.motilal_oswal), "Are you sure you want to delete this product:" + documentModel.getDocumentName() + " ?", "Yes", "No", true, new MSFDialog.DialogListener() {
                    @Override
                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                        if (action == MSFDialog.Action.OK) {
                            deleteDocument(documentModel.getId());
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


}

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
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static com.mosl.tabapp.ui.MSFDialog.alertDialog;


public class CreateMeetingTravellingModeAdapter extends RecyclerView.Adapter<CreateMeetingTravellingModeAdapter.MyViewHolder1> {

    private List<CreateMeetingTravellingModel> travellingList;
    private Context mContext;

    public CreateMeetingTravellingModeAdapter(List<CreateMeetingTravellingModel> mproductList, Context context) {
        this.travellingList = mproductList;
        this.mContext = context;
    }

    public CreateMeetingTravellingModel getDocumentById(String id) {
        for (CreateMeetingTravellingModel model : travellingList) {
            if (model.getTravellingId().equals(id)) {
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
        return travellingList.size();
    }

    /**
     * this will remove item from list and refresh the list
     *
     * @param id
     */
    public void deleteDocument(String id) {
        CreateMeetingTravellingModel model = getDocumentById(id);
        travellingList.remove(model);
        notifyDataSetChanged();
    }

    public void deleteAllProduct() {
        this.travellingList.clear();
        notifyDataSetChanged();
    }

    public void addTravellingMode(CreateMeetingTravellingModel createMeetingProductModel) {
        if (this.travellingList == null) {
            this.travellingList = new ArrayList<>();
        }
        TreeSet<CreateMeetingTravellingModel> hs = new TreeSet<>(new Comparator<CreateMeetingTravellingModel>() {
            @Override
            public int compare(CreateMeetingTravellingModel a, CreateMeetingTravellingModel b) {
                return a.getTravellingName().compareTo(b.getTravellingName());
            }
        });
        hs.addAll(this.travellingList);
        hs.add(createMeetingProductModel);
        this.travellingList.clear();
        this.travellingList.addAll(hs);
        notifyDataSetChanged();
    }

    public List<CreateMeetingTravellingModel> getTravellingList() {


        return this.travellingList;

    }

    @Override
    public MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_create_meeting_product, parent, false);

        return new MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder1 holder, int position) {
        final CreateMeetingTravellingModel documentModel = travellingList.get(position);
        holder.tvModeName.setText(documentModel.getTravellingName());
        holder.ivDeleteMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog(mContext, 0, mContext.getResources().getString(R.string.motilal_oswal), "Are you sure you want to delete this travelling mode:" + documentModel.getTravellingName() + " ?", "Yes", "No", true, new MSFDialog.DialogListener() {
                    @Override
                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                        if (action == MSFDialog.Action.OK) {
                            deleteDocument(documentModel.getTravellingId());
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return travellingList.size();
    }

    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        public ImageView ivDeleteMode;
        public TextView tvModeName;


        public MyViewHolder1(View view) {
            super(view);
            tvModeName = (TextView) view.findViewById(R.id.tvProductName);
            ivDeleteMode = (ImageView) view.findViewById(R.id.ivDeleteProduct);

        }
    }


}

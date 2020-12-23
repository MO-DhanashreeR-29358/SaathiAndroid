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

public class CreateMeetingTravellingModeAdapter
    extends RecyclerView.Adapter<CreateMeetingTravellingModeAdapter.MyViewHolder1> {

  private List<CreateMeetingTravellingModel> travellingList = new ArrayList<>();
  private Context mContext;

  public CreateMeetingTravellingModeAdapter(Context context) {
    this.mContext = context;
  }

  private CreateMeetingTravellingModel getDocumentById(String id) {
    for (CreateMeetingTravellingModel model : travellingList) {
      if (model.getTravellingId().equals(id)) {
        return model;
      }
    }
    return null;
  }

  /**
   * return total documents in list
   */
  public int getCount() {
    return travellingList.size();
  }

  /**
   * this will remove item from list and refresh the list
   */
  private void deleteDocument(String id) {
    CreateMeetingTravellingModel model = getDocumentById(id);
    travellingList.remove(model);
    notifyDataSetChanged();
  }

  public void deleteAllProduct() {
    this.travellingList.clear();
    notifyDataSetChanged();
  }

  public void addTravellingMode(CreateMeetingTravellingModel createMeetingProductModel) {
    TreeSet<CreateMeetingTravellingModel> hs = new TreeSet<>(
        (a, b) -> a.getTravellingName().compareTo(b.getTravellingName()));
    hs.addAll(this.travellingList);
    hs.add(createMeetingProductModel);
    this.travellingList.clear();
    this.travellingList.addAll(hs);
    notifyDataSetChanged();
  }

  public List<CreateMeetingTravellingModel> getTravellingList() {

    return this.travellingList;
  }

  @NotNull @Override
  public MyViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_create_meeting_product, parent, false);

    return new MyViewHolder1(itemView);
  }

  @Override
  public void onBindViewHolder(final MyViewHolder1 holder, int position) {
    final CreateMeetingTravellingModel documentModel = travellingList.get(position);
    holder.tvModeName.setText(documentModel.getTravellingName());
    holder.ivDeleteMode.setOnClickListener(
        view -> alertDialog(mContext, 0, mContext.getResources().getString(R.string.motilal_oswal),
            "Are you sure you want to delete this travelling mode:"
                + documentModel.getTravellingName()
                + " ?", "Yes", "No", true, (action, data) -> {
              if (action == MSFDialog.Action.OK) {
                deleteDocument(documentModel.getTravellingId());
              }
            }));
  }

  @Override
  public int getItemCount() {
    return travellingList.size();
  }

  static class MyViewHolder1 extends RecyclerView.ViewHolder {
    ImageView ivDeleteMode;
    TextView tvModeName;

    MyViewHolder1(View view) {
      super(view);
      tvModeName = view.findViewById(R.id.tvProductName);
      ivDeleteMode = view.findViewById(R.id.ivDeleteProduct);
    }
  }
}

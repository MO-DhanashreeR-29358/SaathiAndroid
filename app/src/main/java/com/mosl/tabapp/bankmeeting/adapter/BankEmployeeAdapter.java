package com.mosl.tabapp.bankmeeting.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.mosl.app.core.utils.Injector;
import com.mosl.mobile.saathi.AppComponent;
import com.mosl.mobile.user.login.UserManager;
import com.mosl.saathi.api.SaathiApi;
import com.mosl.saathi.api.model.aubankmeeting.BankEmployeeRequest;
import com.mosl.saathi.api.model.aubankmeeting.BankEmployeeResponse;
import com.mosl.tabapp.R;
import com.mosl.tabapp.bankmeeting.model.BankEmployeeModel;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

public class BankEmployeeAdapter extends BaseAdapter implements Filterable {
	private final SaathiApi saathiApi = CRMWebServiceHandler.getSaathiApi();
	private Context context;
	private LayoutInflater inflater;
	private List<BankEmployeeModel> resultList = new ArrayList<>();
	private CommonFragment fragment;
	@Inject UserManager userManager;
	public BankEmployeeAdapter(Context context, CommonFragment fragment) {
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.fragment = fragment;
		Injector.obtain(context, AppComponent.class).inject(this);

	}

	public void clearAll() {
		resultList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public BankEmployeeModel getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final HolderView holderView;
		final BankEmployeeModel clientModel = resultList.get(position);
		if (convertView == null) {
			inflater =
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_list_attendee, parent, false);
			holderView = new HolderView(convertView);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		viewColor(position, convertView);

		if (clientModel.noData) {
			holderView.tvAttendeeName.setText(clientModel.employeeName);
			holderView.linaerLayout.setOnClickListener(null);
		} else {
			holderView.tvAttendeeName.setText(
				String.format("%s-%s", clientModel.employeeCode, clientModel.employeeName));
		}
		return convertView;
	}

	@Override
	public Filter getFilter() {
		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				final FilterResults filterResults = new FilterResults();
				ArrayList<BankEmployeeModel> modelList = new ArrayList<>();
				try {
					if (constraint != null) {
						if (CRMAppUtil.isNetworkEnabled(context)) {
							BankEmployeeRequest request =
								new BankEmployeeRequest(
									URLEncoder.encode(
										Objects.requireNonNull(userManager.getUser()).employeeCode,
										"utf-8"),
									URLEncoder.encode(constraint.toString(), "utf-8"),
									URLEncoder.encode(Objects.requireNonNull(
										Objects.requireNonNull(
											userManager.getUser()).currentBusinessProfile.businessUnitId),
										"utf-8"),
									URLEncoder.encode(
										userManager.getUser().currentBusinessProfile.businessUnitId,
										"utf-8"),
									URLEncoder.encode(
										userManager.getUser().defaultBusinessProfile.branchCode,
										"utf-8")
								);
							try {
								List<BankEmployeeResponse> dataList =
									saathiApi.getBankEmployee(request)
										.blockingFirst();
								for (BankEmployeeResponse item : dataList) {
									String employeeName = fragment.stringOrEmpty(item.employeeName);
									String employeeCode = fragment.stringOrEmpty(item.employeeCode);

									modelList.add(
										new BankEmployeeModel(false, employeeCode, employeeName));
									filterResults.values = modelList;
									filterResults.count = modelList.size();
                  /*if (modelList.isEmpty()) {
                    modelList.add(BankEmployeeModel.getNoData(
                        ATTENDEEE_NOT_FOUND_MESSAGE));
                  }*/
								}
							} catch (Exception e) {
								//modelList.add(EmployeeModel.getNoData(getErrorOnFailure(e)));
								filterResults.values = modelList;
								filterResults.count = modelList.size();
								return filterResults;
							}
						} else {
							fragment.noInternetConnectionDialog();
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.count > 0) {
					resultList = (List<BankEmployeeModel>) results.values;
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};
	}

	private void viewColor(int position, View convertView) {
		if (position % 2 == 1) {
			convertView.setBackgroundColor(Color.parseColor("#F5F5F5"));
		} else {
			convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		}
	}

	class HolderView {
		TextView tvAttendeeName;
		LinearLayout linaerLayout;

		private HolderView(View parent) {
			tvAttendeeName = parent.findViewById(R.id.tvAttendeeName);
			linaerLayout = parent.findViewById(R.id.linaerLayout);
		}
	}
}

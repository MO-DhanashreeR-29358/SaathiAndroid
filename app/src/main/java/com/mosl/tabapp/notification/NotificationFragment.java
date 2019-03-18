package com.mosl.tabapp.notification;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.model.NotificationModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.mosl.tabapp.model.AccountDetails.isAufsb;

/**
 * Created by raju on 4/20/2017.
 */

public class NotificationFragment extends MOSLCommonFragment
	implements AdapterView.OnItemSelectedListener {
	private View notificationView;
	private RecyclerView recyclerView;
	private ArrayList<NotificationModel> notificationList;
	private NotificationAdapter notificationAdapter;
	private TextView tvNochart;
	private Spinner spCategory;
	private ArrayList<String> arrCategory;
	private ArrayAdapter<String> arrAdapCategory;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		notificationView = super.onCreateView(inflater, container, savedInstanceState);
		attachLayout(R.layout.frag_notification);
		setAppTitle(getClass().toString(), "Notification");
		setUpView(notificationView);
		setUserVisibleHint(true);
		return notificationView;
	}

	private void setUpView(View calendarView) {
		spCategory = calendarView.findViewById(R.id.spCategory);
		recyclerView = calendarView.findViewById(R.id.recyclerView);
		tvNochart = calendarView.findViewById(R.id.tvNochart);

		String[] eventTypeArray =
			getActivity().getResources().getStringArray(R.array.NotificationCategory);
		arrCategory = new ArrayList<String>(Arrays.asList(eventTypeArray));
		arrAdapCategory =
			new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrCategory);
		arrAdapCategory.setDropDownViewResource(R.layout.mdropdown);
		spCategory.setAdapter(arrAdapCategory);

		spCategory.setOnItemSelectedListener(NotificationFragment.this);

		if (isAufsb()) {
			spCategory.setVisibility(View.GONE);
			notification(OPERATIONAL);
		}
	}

	private void notification(String category) {
		if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
			showProgress();
			Map inputMap = new HashMap<>();
			inputMap.put("Category", category);

			String serviceMethod = "NotificationMessage/GetNotificationMessageMobile";

			try {
				CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
					new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							//after success code goes here
							try {
								if (statusCode == 200) {
									String status = response.getString(STATUS);
									hideProgress();
									if (status.equalsIgnoreCase("true")) {
										notificationList = new ArrayList<>();
										notificationList.clear();
										JSONObject jsonObject = response.getJSONObject(RESPONSE);
										JSONArray dataArray = jsonObject.getJSONArray(DATA);

										if (dataArray.length() != 0) {
											if (dataArray.length() != 0) {

												for (int i = 0; i < dataArray.length(); i++) {
													JSONObject objDetail =
														dataArray.getJSONObject(i);
													String Message = (objDetail.isNull(MESSAGE)
														|| objDetail.getString(MESSAGE).equals(""))
														? "" : objDetail.getString(MESSAGE);
													String CreationDate =
														(objDetail.isNull(CREATIONDATE) || objDetail
															.getString(CREATIONDATE)
															.equals("")) ? ""
															: objDetail.getString(CREATIONDATE);

													SimpleDateFormat existingUTCFormat =
														new SimpleDateFormat(
															"yyyy-MM-dd'T'HH:mm:ss");
													SimpleDateFormat requiredFormat =
														new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
													Date getDate =
														existingUTCFormat.parse(CreationDate);
													String mydate = requiredFormat.format(getDate);
													String date[] = mydate.split(" ");
													String timeStr = date[1];
													String month = null;
													String day = null;
													String additionalData = null;
													additionalData =
														(objDetail.isNull(ADDITIONALDATA)
															|| objDetail.getString(ADDITIONALDATA)
															.equals("")) ? "{}"
															: objDetail.getString(ADDITIONALDATA);
													try {
														String finalDate = date[0];
														Date fromDat = CRMAppUtil.getDate(finalDate,
															"yyyy-MM-dd");
														month =
															CRMAppUtil.getDateStr(fromDat, "MMM");
														Date fromDay = CRMAppUtil.getDate(finalDate,
															"yyyy-MM-dd");
														day = CRMAppUtil.getDateStr(fromDay, "dd");
													} catch (Exception e) {
														month = "";
														day = "";
														e.printStackTrace();
													}
													notificationList.add(
														new NotificationModel(Message, day, timeStr,
															month, additionalData));
												}
												notificationAdapter =
													new NotificationAdapter(notificationList);
												RecyclerView.LayoutManager mLayoutManager =
													new LinearLayoutManager(getActivity());
												recyclerView.setLayoutManager(mLayoutManager);
												recyclerView.setAdapter(notificationAdapter);
												tvNochart.setVisibility(View.GONE);
											} else {
												notificationAdapter =
													new NotificationAdapter(notificationList);
												RecyclerView.LayoutManager mLayoutManager =
													new LinearLayoutManager(getActivity());
												recyclerView.setLayoutManager(mLayoutManager);
												recyclerView.setAdapter(notificationAdapter);
												tvNochart.setVisibility(View.VISIBLE);
											}
										} else {
											notificationAdapter =
												new NotificationAdapter(notificationList);
											RecyclerView.LayoutManager mLayoutManager =
												new LinearLayoutManager(getActivity());
											recyclerView.setLayoutManager(mLayoutManager);
											recyclerView.setAdapter(notificationAdapter);
											tvNochart.setVisibility(View.VISIBLE);
										}
									} else {
										hideProgress();
										tvNochart.setVisibility(View.VISIBLE);
										String errorMsg =
											CRMMOSLCommon.getErrorMsgFromResponse(response);
										MSFDialog.alertDialog(getMainActivity(), 0,
											getResources().getString(R.string.motilal_oswal),
											errorMsg, OK, true, null);
									}
								} else {
									tvNochart.setVisibility(View.VISIBLE);
									hideProgress();
								}
							} catch (Exception e) {
								tvNochart.setVisibility(View.VISIBLE);
								e.printStackTrace();
								MSFDialog.alertDialog(getMainActivity(), 0,
									getResources().getString(R.string.motilal_oswal),
									SOME_ERROR_OCCURED_PLEASE_RETRY, OK, true, null);
							}
						}

						@Override
						public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
							super.onSuccess(statusCode, headers, response);
							hideProgress();
						}

						@Override
						public void onSuccess(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString) {
							super.onSuccess(statusCode, headers, responseString);
							hideProgress();
						}

						@Override
						public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							try {
								hideProgress();
								if (statusCode == UNAUTHORIZE_STATUS_CODE) {
									MSFDialog.alertDialog(getMainActivity(), 0,
										getResources().getString(R.string.motilal_oswal),
										UNAUTHORIZE_USER, OK, false,
										new MSFDialog.DialogListener() {
											@Override
											public void alertDialogAction(MSFDialog.Action action,
												Object... data) {
												if (action == MSFDialog.Action.OK) {
													Intent i = new Intent(getMainActivity(),
														LoginActivity.class);
													startActivity(i);
													getActivity().finish();
												}
											}
										});
									return;
								} else {
									tvNochart.setVisibility(View.VISIBLE);
									String error =
										CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
									if (CRMAppUtil.isBlank(error)) {
										error = SOME_SERVER_ERROR_OCCURRED;
									}
									MSFDialog.alertDialog(getMainActivity(), 0,
										getResources().getString(R.string.motilal_oswal), error,
										OK, true, null);
									return;
								}
							} catch (Exception ex) {
								ex.printStackTrace();
								MSFDialog.alertDialog(getMainActivity(), 0,
									getResources().getString(R.string.motilal_oswal),
									SOME_SERVER_ERROR_OCCURRED, OK, true, null);
							}
						}

						@Override
						public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
							JSONArray errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, OK, true, null);
						}

						@Override
						public void onFailure(int statusCode,
							cz.msebera.android.httpclient.Header[] headers, String responseString,
							Throwable throwable) {
							super.onFailure(statusCode, headers, responseString, throwable);
							hideProgress();
							MSFDialog.alertDialog(getMainActivity(), 0,
								getResources().getString(R.string.motilal_oswal),
								SOME_SERVER_ERROR_OCCURRED, OK, true, null);
						}
					});
			} catch (Exception e) {
				e.printStackTrace();
				MSFDialog.alertDialog(getMainActivity(), 0,
					getResources().getString(R.string.motilal_oswal),
					SOME_ERROR_OCCURED_PLEASE_RETRY, OK, true, null);
			}
		} else {
			MSFDialog.alertDialog(getMainActivity(), 0,
				getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, OK, true,
				null);
		}
	}

	//endregion
	//region selection logic
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
			case 0: {
				notification(OPERATIONAL);
			}
			break;
			case 1: {
				notification(BUSINESS);
			}
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	public class NotificationAdapter
		extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

		private List<NotificationModel> notificationList;

		public NotificationAdapter(List<NotificationModel> notificationList) {
			this.notificationList = notificationList;
		}

		@Override
		public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.notification_list, parent, false);

			return new NotificationAdapter.MyViewHolder(itemView);
		}

		@Override
		public void onBindViewHolder(NotificationAdapter.MyViewHolder holder, int position) {
			final NotificationModel model = notificationList.get(position);
			if (position % 2 == 1) {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
			} else {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));
			}
			holder.txtMessage.setText(model.getMessage());
			holder.timestamp.setText(model.getTime());
			holder.dateUpper.setText(model.getDate());
			holder.dateLower.setText(model.getMonth());
			holder.linearLayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					notificationFragment(model.getAdditionalData());
				}
			});
		}

		@Override
		public int getItemCount() {
			return notificationList.size();
		}

		public class MyViewHolder extends RecyclerView.ViewHolder {
			public LinearLayout linearLayout;
			TextView timestamp, txtMessage, dateUpper, dateLower;

			public MyViewHolder(View view) {
				super(view);
				timestamp = view.findViewById(R.id.timestamp);
				txtMessage = view.findViewById(R.id.txtMessage);
				dateUpper = view.findViewById(R.id.dateUpper);
				dateLower = view.findViewById(R.id.dateLower);
				linearLayout = view.findViewById(R.id.linearLayoutOfListItem);
			}
		}
	}
}

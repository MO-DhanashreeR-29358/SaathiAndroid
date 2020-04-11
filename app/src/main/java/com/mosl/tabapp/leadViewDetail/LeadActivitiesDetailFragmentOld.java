package com.mosl.tabapp.leadViewDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mosl.tabapp.R;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.core.CommonFragment;
import org.jetbrains.annotations.NotNull;

import static com.mosl.tabapp.constants.MOSLConstants.EMAIL;
import static com.mosl.tabapp.constants.MOSLConstants.MOBNO;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static com.mosl.tabapp.constants.MOSLConstants.TIME;

/**
 * Created by raju on 3/14/2017.
 */

public class LeadActivitiesDetailFragmentOld extends CommonFragment {
	@Override protected int getLayout() {
		return R.layout.frag_lead_act_detail;
	}

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View detailView = super.onCreateView(inflater, container, savedInstanceState);
		container.post(() -> setAppTitle(this.getArguments().getString("MeetingReason")));
		setUpView(detailView);
		return detailView;
	}

	private void setUpView(View detailView) {
		TextView tvMonthDate = detailView.findViewById(R.id.tvMonthDate);
		TextView tvTime = detailView.findViewById(R.id.tvTime);
		TextView tvMobile = detailView.findViewById(R.id.tvMobile);
		TextView tvEmail = detailView.findViewById(R.id.tvEmail);
		TextView tvAttendeeFirst = detailView.findViewById(R.id.tvAttendeeFirst);
		TextView tvAttendeeSecond = detailView.findViewById(R.id.tvAttendeeSecond);
		TextView tvAttendeeThird = detailView.findViewById(R.id.tvAttendeeThird);
		TextView tvProduct = detailView.findViewById(R.id.tvProduct);
		TextView tvStatus = detailView.findViewById(R.id.tvStatus);
		TextView tvActivityType = detailView.findViewById(R.id.tvActivityType);
		TextView tvMeetingReason = detailView.findViewById(R.id.tvMeetingReason);
		TextView tvMeetingDuration = detailView.findViewById(R.id.tvMeetingDuration);
		TextView tvActionable = detailView.findViewById(R.id.tvActionable);
		TextView tvCategory = detailView.findViewById(R.id.tvCategory);
		TextView tvQueryDesc = detailView.findViewById(R.id.tvQueryDesc);
		TextView tvCSR = detailView.findViewById(R.id.tvCSR);
		TextView tvComments = detailView.findViewById(R.id.tvComments);
		TextView tvLocation = detailView.findViewById(R.id.tvLocation);
		TextView tvMeetingAddress = detailView.findViewById(R.id.tvMeetingAddress);
		TextView tvPincode = detailView.findViewById(R.id.tvPincode);
		TextView tvTravellingMode = detailView.findViewById(R.id.tvTravellingMode);
		TextView tvAmount = detailView.findViewById(R.id.tvAmount);
		TextView tvGeoMeetingLocation = detailView.findViewById(R.id.tvGeoMeetingLocation);

		try {
			tvMonthDate.setText(
					this.getArguments().getString(MOSLConstants.DATE));
			tvTime.setText(this.getArguments().getString(TIME));
			tvMobile.setText(this.getArguments().getString(MOBNO));
			tvEmail.setText(this.getArguments().getString(EMAIL));
			if (this.getArguments().getString(STATUS)
				.equalsIgnoreCase("1")) {
				tvStatus.setText(getString(R.string.txt_label_active));
			} else if (this.getArguments().getString(STATUS)
				.equalsIgnoreCase("0")) {
				tvStatus.setText(getString(R.string.txt_label_inactive));
			}
			tvProduct.setText(this.getArguments().getString("Product"));
			tvActivityType.setText(this.getArguments().getString("ActivityType"));
			tvMeetingReason.setText(this.getArguments().getString("MeetingReason"));
			tvMeetingDuration.setText(this.getArguments().getString("MeetingDuration"));
			tvActionable.setText(this.getArguments().getString("Actionable"));
			tvCategory.setText(this.getArguments().getString("Category"));
			tvQueryDesc.setText(this.getArguments().getString("QueryDesc"));
			tvCSR.setText(this.getArguments().getString("CSR"));
			tvComments.setText(this.getArguments().getString("Comments"));
			tvLocation.setText(this.getArguments().getString("Location"));
			tvMeetingAddress.setText(this.getArguments().getString("MeetingAddress"));
			tvPincode.setText(this.getArguments().getString("Pincode"));
			tvTravellingMode.setText(this.getArguments().getString("TravellingMode"));
			tvAmount.setText(this.getArguments().getString("Amount"));
			tvGeoMeetingLocation.setText(this.getArguments().getString("GeoMeetingLocation"));
			if (!this.getArguments().getString("AttendeeEmp").isEmpty()) {
				try {
					String[] attendingArray =
							this.getArguments().getString("AttendeeEmp")
							.split(",");
					tvAttendeeFirst.setText(attendingArray[0]);
					tvAttendeeSecond.setText(attendingArray[1]);
					tvAttendeeThird.setText(attendingArray[2]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

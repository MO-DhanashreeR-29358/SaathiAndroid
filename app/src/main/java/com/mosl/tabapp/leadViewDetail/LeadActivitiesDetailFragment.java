package com.mosl.tabapp.leadViewDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mosl.tabapp.R;
import com.mosl.tabapp.ui.MOSLCommonFragment;

/**
 * Created by raju on 3/14/2017.
 */

public class LeadActivitiesDetailFragment extends MOSLCommonFragment {
    private View detailView;
    private TextView tvMonthDate, tvTime, tvMobile, tvEmail, tvAttendeeFirst, tvAttendeeSecond, tvAttendeeThird, tvStatus, tvActivityType, tvMeetingReason, tvMeetingDuration, tvProduct, tvActionable, tvCategory, tvQueryDesc, tvCSR, tvComments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        detailView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_lead_act_detail);
        setUpView(detailView);
        return detailView;
    }

    private void setUpView(View detailView) {

        setAppTitle(getClass().toString(), "");
        tvMonthDate = (TextView) detailView.findViewById(R.id.tvMonthDate);
        tvTime = (TextView) detailView.findViewById(R.id.tvTime);
        tvMobile = (TextView) detailView.findViewById(R.id.tvMobile);
        tvEmail = (TextView) detailView.findViewById(R.id.tvEmail);
        tvAttendeeFirst = (TextView) detailView.findViewById(R.id.tvAttendeeFirst);
        tvAttendeeSecond = (TextView) detailView.findViewById(R.id.tvAttendeeSecond);
        tvAttendeeThird = (TextView) detailView.findViewById(R.id.tvAttendeeThird);
        tvProduct = (TextView) detailView.findViewById(R.id.tvProduct);
        tvStatus = (TextView) detailView.findViewById(R.id.tvStatus);
        tvActivityType = (TextView) detailView.findViewById(R.id.tvActivityType);
        tvMeetingReason = (TextView) detailView.findViewById(R.id.tvMeetingReason);
        tvMeetingDuration = (TextView) detailView.findViewById(R.id.tvMeetingDuration);
        tvActionable = (TextView) detailView.findViewById(R.id.tvActionable);
        tvCategory = (TextView) detailView.findViewById(R.id.tvCategory);
        tvQueryDesc = (TextView) detailView.findViewById(R.id.tvQueryDesc);
        tvCSR = (TextView) detailView.findViewById(R.id.tvCSR);
        tvComments = (TextView) detailView.findViewById(R.id.tvComments);

        try {
            setAppTitle(getClass().toString(), this.getArguments().getString("MeetingReason"));
            tvMonthDate.setText(this.getArguments().getString(DATE));
            tvTime.setText(this.getArguments().getString(TIME));
            tvMobile.setText(this.getArguments().getString(MOBNO));
            tvEmail.setText(this.getArguments().getString(EMAIL));
            if (this.getArguments().getString(STATUS).equalsIgnoreCase("1")) {
                tvStatus.setText("Active");
            } else if (this.getArguments().getString(STATUS).equalsIgnoreCase("0")) {
                tvStatus.setText("InActive");
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
            if (!this.getArguments().getString("AttendeeEmp").isEmpty()) {
                try {
                    String attendingArray[] = this.getArguments().getString("AttendeeEmp").split(",");
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

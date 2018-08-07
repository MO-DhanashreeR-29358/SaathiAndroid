package com.mosl.tabapp.franchiesleadviewdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CustomViewPager;
import com.mosl.tabapp.ui.MOSLCommonFragment;

/**
 * Created by Vijay on 15/03/2017.
 */

public class FranchiesLeadDetailFragment extends MOSLCommonFragment implements View.OnClickListener {

    private View franchiesLeadViewDetailView;
    private CustomViewPager viewPager;
    private TextView tvClientDetail, tvLeadDetail, tvEditLeadDetail, tvMappingHistory, tvDocuments;
    private LinearLayout llClientDetail, llLeadDetail, llMappingHistory;
    private TextView tvPrefixCode, tvUCID, tvBAName, tvBranchCode, tvDefaultBank, tvClientAddress, tvSubBrokerCode, tvMapHistoryProduct, tvPrimaryAdviser, tvPACall,
            tvSecondaryAdviser, tvSACall, tvMappingDate;
    private Spinner spSource, spProspectSource, spBackground, spExistingBroker, spWorkExp;
    private EditText etLeadName, etLeadID, etBusinessName, etCreationDate, etOfficeAddress, etProspectSourceDetail, etCreatedThrough, etCreatedBy, etMobileNumber,
            etLandlineNumber, etEmail, etRefereeCode, etRefereeName, etRefereeEmail, etRefereeNumber, etAlternateAddress, etAlternateNumber, etAlternateEmail,
            etDOB, etAnniversary, etOccupation, etCurrentBrokerage, etLeadStage, etLeadStatus, etLastModified, etTanCard,
            etPan, etAadharNumber, etTMName, etRFMName, etLastMeetingDate;
    private Button btnSubmit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        franchiesLeadViewDetailView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_franchies_lead_detail);
        setUpView(franchiesLeadViewDetailView);
        return franchiesLeadViewDetailView;
    }

    public static FranchiesLeadDetailFragment newInstance(int position, String type, String leadId) {
        FranchiesLeadDetailFragment fragment = new FranchiesLeadDetailFragment();
        Bundle args = new Bundle();
        args.putInt("Position", position);
        args.putString("Type", type);
        args.putString(LEAD_ID, leadId);
        fragment.setArguments(args);
        return fragment;
    }

    public void setViewPager(CustomViewPager viewPager) {
        this.viewPager = viewPager;
    }

    private void setUpView(View franchiesLeadViewDetailView) {

        btnSubmit = (Button) franchiesLeadViewDetailView.findViewById(R.id.btnSubmit);
        tvClientDetail = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvClientDetail);
        tvLeadDetail = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvLeadDetail);
        tvEditLeadDetail = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvEditLeadDetail);
        tvMappingHistory = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvMappingHistory);
        tvDocuments = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvDocuments);

        llClientDetail = (LinearLayout) franchiesLeadViewDetailView.findViewById(R.id.llClientDetail);
        llLeadDetail = (LinearLayout) franchiesLeadViewDetailView.findViewById(R.id.llLeadDetail);
        llMappingHistory = (LinearLayout) franchiesLeadViewDetailView.findViewById(R.id.llMappingHistory);

        tvPrefixCode = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvPrefixCode);
        tvUCID = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvUCID);
        tvBAName = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvBAName);
        tvBranchCode = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvBranchCode);
        tvDefaultBank = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvDefaultBank);
        tvClientAddress = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvClientAddress);
        tvSubBrokerCode = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvSubBrokerCode);

        tvMapHistoryProduct = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvMapHistoryProduct);
        tvPrimaryAdviser = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvPrimaryAdviser);
        tvPACall = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvPACall);
        tvSecondaryAdviser = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvSecondaryAdviser);
        tvSACall = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvSACall);
        tvMappingDate = (TextView) franchiesLeadViewDetailView.findViewById(R.id.tvMappingDate);

        etLeadName = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etLeadName);
        etLeadID = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etLeadID);
        etBusinessName = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etBusinessName);
        etCreationDate = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etCreationDate);
        etOfficeAddress = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etOfficeAddress);
        etProspectSourceDetail = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etProspectSourceDetail);
        etCreatedThrough = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etCreatedThrough);
        etCreatedBy = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etCreatedBy);
        etMobileNumber = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etMobileNumber);
        etLandlineNumber = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etLandlineNumber);
        etEmail = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etEmail);
        etRefereeCode = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etRefereeCode);
        etRefereeName = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etRefereeName);
        etRefereeEmail = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etRefereeEmail);
        etRefereeNumber = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etRefereeNumber);
        etAlternateAddress = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etAlternateAddress);
        etAlternateNumber = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etAlternateNumber);
        etAlternateEmail = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etAlternateEmail);
        etDOB = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etDOB);
        etAnniversary = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etAnniversary);
        etOccupation = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etOccupation);
        etCurrentBrokerage = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etCurrentBrokerage);
        etLeadStage = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etLeadStage);
        etLeadStatus = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etLeadStatus);
        etLastModified = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etLastModified);
        etTanCard = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etTanCard);
        etPan = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etPan);
        etAadharNumber = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etAadharNumber);
        etTMName = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etTMName);
        etRFMName = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etRFMName);
        etLastMeetingDate = (EditText) franchiesLeadViewDetailView.findViewById(R.id.etLastMeetingDate);

        spSource = (Spinner) franchiesLeadViewDetailView.findViewById(R.id.spSource);
        spProspectSource = (Spinner) franchiesLeadViewDetailView.findViewById(R.id.spProspectSource);
        spBackground = (Spinner) franchiesLeadViewDetailView.findViewById(R.id.spBackground);
        spExistingBroker = (Spinner) franchiesLeadViewDetailView.findViewById(R.id.spExistingBroker);
        spWorkExp = (Spinner) franchiesLeadViewDetailView.findViewById(R.id.spWorkExp);

        setIcon(llClientDetail, tvClientDetail);
        setIcon(llLeadDetail, tvLeadDetail);
        setIcon(llMappingHistory, tvMappingHistory);

        btnSubmit.setOnClickListener(this);
        tvClientDetail.setOnClickListener(this);
        tvLeadDetail.setOnClickListener(this);
        tvEditLeadDetail.setOnClickListener(this);
        tvMappingHistory.setOnClickListener(this);
        tvDocuments.setOnClickListener(this);

        spSource.setEnabled(false);
        spProspectSource.setEnabled(false);
        spBackground.setEnabled(false);
        spExistingBroker.setEnabled(false);
        spWorkExp.setEnabled(false);

        tvPrefixCode.setText("12367");
        tvUCID.setText("Mike");
        tvBAName.setText("test@motilaloswal.com");
        tvBranchCode.setText("9898989898");
        tvDefaultBank.setText("HDFC Bank LTD");
        tvClientAddress.setText("test.test@motilaloswal.com");
        tvSubBrokerCode.setText("Individual");
        tvMapHistoryProduct.setText("Equity");
        tvPrimaryAdviser.setText("Payal Basu-8528");
        tvPACall.setText("9898989898");
        tvSecondaryAdviser.setText("Payal Basu-8528");
        tvSACall.setText("9898989898");
        tvMappingDate.setText("19.02.2017");

        etLeadName.setText("Mike");
        etLeadID.setText("12367");
        etBusinessName.setText(getResources().getString(R.string.motilal_oswal));
        etCreationDate.setText("test@motilaloswal.com");
        etOfficeAddress.setText("Motilal Tower, Parel, Mumbai");
        etProspectSourceDetail.setText("Individual");
        etCreatedThrough.setText("test@motilaloswal.com");
        etCreatedBy.setText("test@motilaloswal.com");
        etMobileNumber.setText("12367");
        etLandlineNumber.setText("Mike");
        etEmail.setText("test@motilaloswal.com");
        etRefereeCode.setText("9898989898");
        etRefereeName.setText("HDFC Bank LTD");
        etRefereeEmail.setText("test.test@motilaloswal.com");
        etRefereeNumber.setText("Individual");
        etAlternateAddress.setText("Motilal Tower, Parel, Mumbai");
        etAlternateNumber.setText("9898989898");
        etAlternateEmail.setText("test@motilaloswal.com");
        etDOB.setText("16.12.1992");
        etAnniversary.setText("16.12.1992");
        etOccupation.setText("Business");
        etCurrentBrokerage.setText("20,000,000");
        etLeadStage.setText("C1");
        etLeadStatus.setText("Pending");
        etLastModified.setText("16.12.1992");
        etTanCard.setText("12345678");
        etPan.setText("12345678");
        etAadharNumber.setText("16121992");
        etTMName.setText(getResources().getString(R.string.motilal_oswal));
        etRFMName.setText(getResources().getString(R.string.motilal_oswal));
        etLastMeetingDate.setText("16.12.1992");
    }

    @Override
    public void onClick(View view) {
        Bundle args = new Bundle();
        switch (view.getId()) {
            case R.id.tvClientDetail:
                toggleSectionExpansion(llClientDetail, tvClientDetail);
                break;
            case R.id.tvLeadDetail:
                toggleSectionExpansion(llLeadDetail, tvLeadDetail);
                break;
            case R.id.tvEditLeadDetail:
//                enableField(llLeadDetail);
                etLeadName.setEnabled(true);
                etLandlineNumber.setEnabled(true);
                etRefereeCode.setEnabled(true);
                etRefereeName.setEnabled(true);
                etDOB.setEnabled(true);
                etAnniversary.setEnabled(true);
                etAlternateNumber.setEnabled(true);
                etAlternateEmail.setEnabled(true);
                break;
            case R.id.tvMappingHistory:
                toggleSectionExpansion(llMappingHistory, tvMappingHistory);
                break;
            case R.id.tvDocuments:
                args.putString(LEAD_ID, this.getArguments().getString(LEAD_ID));
                navigateTo(NAV_TO_LEAD_DOCUMENT, args, getFragmentManager(), true);
                break;
            case R.id.btnSubmit:

                break;
        }
    }
}

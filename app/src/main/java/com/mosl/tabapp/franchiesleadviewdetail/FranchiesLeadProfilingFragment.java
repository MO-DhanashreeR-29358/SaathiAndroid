package com.mosl.tabapp.franchiesleadviewdetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CustomViewPager;
import com.mosl.tabapp.ui.MOSLCommonFragment;

/**
 * Created by Vijay on 20/03/2017.
 */

public class FranchiesLeadProfilingFragment extends MOSLCommonFragment implements View.OnClickListener {

    private View franchiesLeadProfilingView;
    private CustomViewPager viewPager;
    private LinearLayout llBAProfile, llIFAProfile;
    private Button btnCreate, btnCancel;
    private TextView tvPersonalDetail, tvBusinessDetails, tvInfrastructureDetails;
    private CheckBox cbBSECD, cbBSECM, cbBSEFAO, cbMCXSXCD, cbMCXSXCM, cbMCXSXFAO, cbNSECD, cbNSECM, cbNSEFAO, cbBrokingRelated, cbBroking, cbNonBroking, cbMainBroker,
            cbSubBroker, cbLifeInsurance, cbGeneralInsurance, cbMutualFund, cbFixedDeposit,
            cbBonds, cbCommodity, cbEquity, cbDerivatives, cbPrivateEquity, cbCategoriesOthers;
    private RadioButton rbExpYes, rbExpNo, rbBusinessYes, rbBusinessNo, rbReportYes, rbReportNo, rbProfessional, rbPostGraduate, rbGraduate, rbUnderGraduate, rbPartTime,
            rbFullTime, rbHome, rbOffice, rbBOSolutionOthers;
    private RadioGroup rgBrokingExp, rgShiftBusiness, rgBrokerageReport, rgQualification, rgWorkingPattern;
    private LinearLayout llPersonalDetail, llBusinessDetails, llInfrastructureDetails;
    private Spinner spBrokerName, spTotalWorkExp, spIndustryExp, spInvestmentExp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        franchiesLeadProfilingView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_franchies_lead_profiling);
        setUpView(franchiesLeadProfilingView);
        return franchiesLeadProfilingView;
    }

    public static FranchiesLeadProfilingFragment newInstance(int position, String type, String leadId) {
        FranchiesLeadProfilingFragment fragment = new FranchiesLeadProfilingFragment();
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

    private void setUpView(View franchiesLeadProfilingView) {
        llBAProfile = (LinearLayout) franchiesLeadProfilingView.findViewById(R.id.llBAProfile);
        llIFAProfile = (LinearLayout) franchiesLeadProfilingView.findViewById(R.id.llIFAProfile);

        btnCreate = (Button) franchiesLeadProfilingView.findViewById(R.id.btnCreate);
        btnCancel = (Button) franchiesLeadProfilingView.findViewById(R.id.btnCancel);

        //BA Profile
        spBrokerName = (Spinner) franchiesLeadProfilingView.findViewById(R.id.spBrokerName);

        cbBSECD = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbBSECD);
        cbBSECM = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbBSECM);
        cbBSEFAO = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbBSEFAO);
        cbMCXSXCD = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbMCXSXCD);
        cbMCXSXCM = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbMCXSXCM);
        cbMCXSXFAO = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbMCXSXFAO);
        cbNSECD = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbNSECD);
        cbNSECM = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbNSECM);
        cbNSEFAO = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbNSEFAO);
        cbBrokingRelated = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbBrokingRelated);
        cbBroking = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbBroking);
        cbNonBroking = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbNonBroking);
        cbMainBroker = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbMainBroker);
        cbSubBroker = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbSubBroker);

        rbExpYes = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbExpYes);
        rbExpNo = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbExpNo);
        rbBusinessYes = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbBusinessYes);
        rbBusinessNo = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbBusinessNo);
        rbReportYes = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbReportYes);
        rbReportNo = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbReportNo);

        rgBrokingExp = (RadioGroup) franchiesLeadProfilingView.findViewById(R.id.rgBrokingExp);
        rgShiftBusiness = (RadioGroup) franchiesLeadProfilingView.findViewById(R.id.rgShiftBusiness);
        rgBrokerageReport = (RadioGroup) franchiesLeadProfilingView.findViewById(R.id.rgBrokerageReport);

        //IFA Profile
        llPersonalDetail = (LinearLayout) franchiesLeadProfilingView.findViewById(R.id.llPersonalDetail);
        llBusinessDetails = (LinearLayout) franchiesLeadProfilingView.findViewById(R.id.llBusinessDetails);
        llInfrastructureDetails = (LinearLayout) franchiesLeadProfilingView.findViewById(R.id.llInfrastructureDetails);

        tvPersonalDetail = (TextView) franchiesLeadProfilingView.findViewById(R.id.tvPersonalDetail);
        tvBusinessDetails = (TextView) franchiesLeadProfilingView.findViewById(R.id.tvBusinessDetails);
        tvInfrastructureDetails = (TextView) franchiesLeadProfilingView.findViewById(R.id.tvInfrastructureDetails);

        rbProfessional = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbProfessional);
        rbPostGraduate = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbPostGraduate);
        rbGraduate = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbGraduate);
        rbUnderGraduate = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbUnderGraduate);
        rbPartTime = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbPartTime);
        rbFullTime = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbFullTime);
        rbHome = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbHome);
        rbOffice = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbOffice);
        rbBOSolutionOthers = (RadioButton) franchiesLeadProfilingView.findViewById(R.id.rbBOSolutionOthers);

        rgQualification = (RadioGroup) franchiesLeadProfilingView.findViewById(R.id.rgQualification);
        rgWorkingPattern = (RadioGroup) franchiesLeadProfilingView.findViewById(R.id.rgWorkingPattern);

        cbLifeInsurance = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbLifeInsurance);
        cbGeneralInsurance = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbGeneralInsurance);
        cbMutualFund = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbMutualFund);
        cbFixedDeposit = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbFixedDeposit);
        cbBonds = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbBonds);
        cbCommodity = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbCommodity);
        cbEquity = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbEquity);
        cbDerivatives = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbDerivatives);
        cbPrivateEquity = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbPrivateEquity);
        cbCategoriesOthers = (CheckBox) franchiesLeadProfilingView.findViewById(R.id.cbCategoriesOthers);

        spTotalWorkExp = (Spinner) franchiesLeadProfilingView.findViewById(R.id.spTotalWorkExp);
        spIndustryExp = (Spinner) franchiesLeadProfilingView.findViewById(R.id.spIndustryExp);
        spInvestmentExp = (Spinner) franchiesLeadProfilingView.findViewById(R.id.spInvestmentExp);

        btnCreate.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        tvPersonalDetail.setOnClickListener(this);
        tvBusinessDetails.setOnClickListener(this);
        tvInfrastructureDetails.setOnClickListener(this);

        setIcon(llPersonalDetail, tvPersonalDetail);
        setIcon(llBusinessDetails, tvBusinessDetails);
        setIcon(llInfrastructureDetails, tvInfrastructureDetails);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCreate:

                break;
            case R.id.btnCancel:

                break;
            case R.id.tvPersonalDetail:
                toggleSectionExpansion(llPersonalDetail, tvPersonalDetail);
                break;
            case R.id.tvBusinessDetails:
                toggleSectionExpansion(llBusinessDetails, tvBusinessDetails);
                break;
            case R.id.tvInfrastructureDetails:
                toggleSectionExpansion(llInfrastructureDetails, tvInfrastructureDetails);
                break;

        }
    }
}

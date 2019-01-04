package com.mosl.mobile.ekyc.bankdetail;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mosl.mobile.ekyc.R;
import com.mosl.mobile.ekyc.common.CustomViewPager;
import com.mosl.mobile.ekyc.constants.MOSLConstants;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import static com.mosl.mobile.ekyc.model.CommonModel.getAppType;

/**
 * Created by Owner on 7/8/2016.
 */
public class FundTransferFragment extends Fragment implements View.OnClickListener, MOSLConstants {

    private Context mContext;
    ArrayList<Fragment> arrFrag = new ArrayList<>();
    View fundTransferView;
    ImageButton btnBankBack;
    ImageView btnNetBank, btnDebitCard, btnCheque;
    LinearLayout llReplaceFragment, customProgress;
    boolean loadedAPIOnStartFund;

    static int mEquStatus, mCommStatus;
    CustomViewPager viewPager;
    TabPageIndicator tabPageIndicator;
   /* public FundTransferFragment(long equityStatus, long commodityStatus){
        mEquStatus = equityStatus;
        mCommStatus = commodityStatus;
    }*/

    public void setViewPager(CustomViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setTabPageIndicator(TabPageIndicator tabPageIndicator) {
        this.tabPageIndicator = tabPageIndicator;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        try {
            fundTransferView = inflater.inflate(R.layout.activity_fund_transfer, container, false);
            setUpView(fundTransferView);
        } catch (Exception e) {
            e.printStackTrace();
//            Crashlytics.setUserIdentifier(getUserID() + "-" + getAppLoginID());
//            Crashlytics.logException(e);
        }
        return fundTransferView;
    }

    private void setUpView(View fundTransferView) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mEquStatus = bundle.getInt(EQUITY, 0);
            mCommStatus = bundle.getInt(COMMODITY, 0);
        }
        llReplaceFragment = (LinearLayout) fundTransferView.findViewById(R.id.llReplaceFragment);
        customProgress = (LinearLayout) fundTransferView.findViewById(R.id.customProgress);

        btnNetBank = (ImageView) fundTransferView.findViewById(R.id.btnNetBank);
        btnDebitCard = (ImageView) fundTransferView.findViewById(R.id.btnDebitCard);
        btnCheque = (ImageView) fundTransferView.findViewById(R.id.btnCheque);
        //  btnBankBack = (Button) fundTransferView.findViewById(R.id.btnBankBack);

        btnNetBank.setOnClickListener(this);
        btnDebitCard.setOnClickListener(this);
        btnCheque.setOnClickListener(this);
        //  btnBankBack.setOnClickListener(this);

        if (getAppType().equalsIgnoreCase(DIRECT)) {
            btnCheque.setVisibility(View.GONE);
        } else if (getAppType().equalsIgnoreCase(CRM) || getAppType().equalsIgnoreCase(PP)) {
            btnCheque.setVisibility(View.VISIBLE);
        }
        loadTransferDetails();

    }

    public void loadTransferDetails() {
//        if (!loadedAPIOnStartFund) {
        replaceNetBankingFragment();
        // getPaymentStatus();
        loadedAPIOnStartFund = true;
//        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnNetBank) {

            replaceNetBankingFragment();
        }
        if (id == R.id.btnDebitCard) {
            FTDebitCardFragment ftDebitCardFragment = FTDebitCardFragment.newInstance(getContext(), mEquStatus, mCommStatus);
            ftDebitCardFragment.setTabPageIndicator(tabPageIndicator);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceFragment, ftDebitCardFragment).commit();

            btnDebitCard.setImageResource(R.drawable.icon_debit);
            btnNetBank.setImageResource(R.drawable.icon_net_1);
            btnCheque.setImageResource(R.drawable.icon_cheque_1);
        }
        if (id == R.id.btnCheque) {
            FTChequeFragment ftChequeFragment = FTChequeFragment.newInstance(getContext(), mEquStatus, mCommStatus);
            ftChequeFragment.setTabPageIndicator(tabPageIndicator);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceFragment, ftChequeFragment).commit();

            btnDebitCard.setImageResource(R.drawable.icon_debit_1);
            btnNetBank.setImageResource(R.drawable.icon_net_1);
            btnCheque.setImageResource(R.drawable.icon_cheque);
        }

    }

    public void replaceNetBankingFragment() {
        if (tabPageIndicator != null) {
            FTNetBankingFragment bd = FTNetBankingFragment.newInstance(getContext(), mEquStatus, mCommStatus);
            bd.setTabPageIndicator(tabPageIndicator);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceFragment, bd).commit();
            btnDebitCard.setImageResource(R.drawable.icon_debit_1);
            btnNetBank.setImageResource(R.drawable.icon_net);
            btnCheque.setImageResource(R.drawable.icon_cheque_1);
        }
    }
}

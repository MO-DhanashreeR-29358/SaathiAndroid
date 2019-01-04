package com.mosl.mobile.ekyc.bankdetail;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mosl.mobile.ekyc.R;

/**
 * Created by shefali on 14-06-2016.
 */
public class BankDetailsFragment extends Fragment {

    View bankDetailView;
    static Context mContext;
    LinearLayout llReplace;
    boolean loadedAPIOnStart;
    //    LinearLayout llTabButtob;
    Button btnMakePayment;
//    Button btnBankEquity, btnBankCommodity, btnMakePayment;

    public static BankDetailsFragment newInstance(Context context) {

        BankDetailsFragment fragment = new BankDetailsFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bankDetailView = inflater.inflate(R.layout.frag_bank_detail, container, false);
        setUpView(bankDetailView);

//        BankDetailsFormFragment bankDetailsFormFragment = new BankDetailsFormFragment();


       /* btnBankCommodity.setBackgroundColor(btnBankCommodity.getContext().getResources().getColor(R.color.buttonBackground));
        btnBankEquity.setBackgroundColor(btnBankEquity.getContext().getResources().getColor(R.color.secondaryBackground));*/

        return bankDetailView;
    }

    public void showBankDetailForm() {
        /*if (!loadedAPIOnStart) {
            loadedAPIOnStart = true;
        }else {
            BankDetailsFormFragment bankDetailsFormFragment = BankDetailsFormFragment.newInstance(mContext);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplace, bankDetailsFormFragment).commit();
        }*/

    }

    public void setUpView(View bankDetailView) {
        llReplace = (LinearLayout) bankDetailView.findViewById(R.id.llReplace);

    }

    @Override
    public void onResume() {
        BankDetailsFormFragment bankDetailsFormFragment = new BankDetailsFormFragment();
//        bankDetailsFormFragment.setContext(mContext);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplace, bankDetailsFormFragment).commit();
        super.onResume();
    }
}

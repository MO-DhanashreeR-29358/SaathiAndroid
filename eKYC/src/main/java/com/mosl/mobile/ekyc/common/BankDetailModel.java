package com.mosl.mobile.ekyc.common;

import com.mosl.mobile.ekyc.bankdetail.BankDetailsFormFragment;

import static com.mosl.mobile.ekyc.bankdetail.BankDetailsFormFragment.strCommAccNo;
import static com.mosl.mobile.ekyc.bankdetail.BankDetailsFormFragment.strCommBankName;
import static com.mosl.mobile.ekyc.bankdetail.BankDetailsFormFragment.strEquAccNo;
import static com.mosl.mobile.ekyc.bankdetail.BankDetailsFormFragment.strEquBankName;

/**
 * Created by Shefali on 23/10/16.
 */

public class BankDetailModel {

    public static String equBankName;
    public static String commBankName;
    public static String equAccNo;
    public static String commAccNo;
    public static String equMargin;
    public static String equTotalAmount;
    public static String commMargin;
    public static String commTotalAmount;

    public static String getEquBankName() {
        return equBankName;
    }

    public static void setEquBankName(String equBankName) {
        BankDetailModel.equBankName = equBankName;
    }

    public static String getCommBankName() {
        return commBankName;
    }

    public static void setCommBankName(String commBankName) {
        BankDetailModel.commBankName = commBankName;
    }

    public static String getEquAccNo() {
        return equAccNo;
    }

    public static void setEquAccNo(String equAccNo) {
        BankDetailModel.equAccNo = equAccNo;
    }

    public static String getCommAccNo() {
        return commAccNo;
    }

    public static void setCommAccNo(String commAccNo) {
        BankDetailModel.commAccNo = commAccNo;
    }

    public static String getEquMargin() {
        return equMargin;
    }

    public static void setEquMargin(String equMargin) {
        BankDetailModel.equMargin = equMargin;
    }

    public static String getEquTotalAmount() {
        return equTotalAmount;
    }

    public static void setEquTotalAmount(String equTotalAmount) {
        BankDetailModel.equTotalAmount = equTotalAmount;
    }

    public static String getCommMargin() {
        return commMargin;
    }

    public static void setCommMargin(String commMargin) {
        BankDetailModel.commMargin = commMargin;
    }

    public static String getCommTotalAmount() {
        return commTotalAmount;
    }

    public static void setCommTotalAmount(String commTotalAmount) {
        BankDetailModel.commTotalAmount = commTotalAmount;
    }
}

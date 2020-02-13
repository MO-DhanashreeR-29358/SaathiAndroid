package com.mosl.tabapp.urls;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;
public class ServiceUrlsImpl {

    // Todo for testing used http://122.184.137.226:83/api/

    @NonNull
    @Contract(pure = true)
    public static String CRM_BASE_URL()  {
        return
            "https://saathiuat.motilaloswal.com:1023/api/";
    }
    @NonNull
    @Contract(pure = true)
    public static String UPPERMOST_URL()  {
        return
            "http://122.184.137.89/Partner/PartnerMobileWebServices.asmx/";
    }
    @NonNull
    @Contract(pure = true)
    public static String CRM_COLLATERAL_URL()  {
        return
                "http://onlinetrade.motilaloswal.com/emailers/TGS/2015/Landing_page/TGS_landing_page.html";
    }

    @NonNull
    @Contract(pure = true)
    public static String SALES_STADIUM_PITCH_URL() {
        return
            "https://saathi.motilaloswal.com/Stadium-Pitch-Objection-Handling/English/Stadium-Pitch/Index.html";
    }
}
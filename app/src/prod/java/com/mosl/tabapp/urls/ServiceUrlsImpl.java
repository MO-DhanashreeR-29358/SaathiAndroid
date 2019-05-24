package com.mosl.tabapp.urls;

import android.support.annotation.NonNull;
import org.jetbrains.annotations.Contract;

public class ServiceUrlsImpl {
    @NonNull
    @Contract(pure = true)
    public static String CRM_BASE_URL() {
        return
            "https://saathi.motilaloswal.com:84/api/";

    }

    @NonNull
    @Contract(pure = true)
    public static String UPPERMOST_URL() {
        return
                "https://login.motilaloswal.com/Partner/PartnerMobileWebServices.asmx/";

    }

    @NonNull
    @Contract(pure = true)
    public static String CRM_COLLATERAL_URL() {
        return
            "https://www.motilaloswal.com/EKYCCONTROLLER/Brochure/index.html";
    }

    @NonNull
    @Contract(pure = true)
    public static String SALES_STADIUM_PITCH_URL() {
        return
            "http://onlinetrade.motilaloswal.com/emailers/Stadium-Pitch-Objection-Handling/English/Stadium-Pitch/Index.html";
    }
}

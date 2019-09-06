package com.mosl.tabapp.urls;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class ServiceUrlsImpl {
    @NonNull
    @Contract(pure = true)
    public static String CRM_BASE_URL() {
        return
                "http://saathi.motilaloswal.com:81/api/";

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
}

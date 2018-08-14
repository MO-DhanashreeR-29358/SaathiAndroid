package com.mosl.tabapp.urls;

import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;

public class ServiceUrlsImpl {
    @NonNull
    @Contract(pure = true)
    public static String CRM_BASE_URL()  {
        return
            "http://122.184.137.226:83/api/";

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
            "http://web20.motilaloswal.com:8000/Brochure/index.html";

    }
}

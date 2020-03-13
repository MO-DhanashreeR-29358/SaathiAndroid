package com.mosl.tabapp.urls;
import androidx.annotation.NonNull;
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
            "http://onlinetrade.motilaloswal.com/emailers/TGS/2015/Landing_page/TGS_landing_page.html";
    }
    @NonNull
    @Contract(pure = true)
    public static String SALES_STADIUM_PITCH_URL() {
        return
            "https://saathi.motilaloswal.com/Stadium-Pitch-Objection-Handling/English/Stadium-Pitch/Index.html";
    }
}
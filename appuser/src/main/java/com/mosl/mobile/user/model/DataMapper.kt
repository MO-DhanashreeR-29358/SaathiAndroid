package com.mosl.mobile.user.model

import com.mosl.saathi.api.model.login.BusinessProfile
import com.mosl.saathi.api.model.login.CurrentBusinessProfile
import com.mosl.saathi.api.model.login.LoginResponse
import com.mosl.saathi.api.model.splash.DigitalSalesKitData
import com.mosl.saathi.api.model.splash.DigitalSalesKitMenu
import com.mosl.saathi.api.model.splash.ReportTypeResponse
import com.mosl.saathi.api.model.splash.VersionResponse

fun LoginResponse.transform(
    password: String
): com.mosl.mobile.user.model.LoginResponse {

    val userProfile = user
    return LoginResponse(
        user = userProfile.transformUser(
            smtEnabled,
            password,
            isEkycEnable,
            isChequeApprovalEnable,
            isUseUrl,
            isRetrieveOTPEnable,
            isPrimeUser == "1",
            isBank == "1"
        ),
        smtEnabled = smtEnabled
    )
}

fun VersionResponse.transform(
): com.mosl.mobile.user.model.VersionResponse {

    return com.mosl.mobile.user.model.VersionResponse(
        statusCode = statusCode,
        msg = msg,
        url = url,
        newEkyc = newEkyc,
        newEkycUrl = newEkycUrl
    )
}

fun com.mosl.saathi.api.model.login.User.transformUser(
    smtEnabled: String,
    password: String,
    isEkycEnable: String?,
    isChequeApprovalEnable: String?,
    isUseUrl: String?,
    isRetrieveOTPEnable: String?,
    isPrimeUser: Boolean,
    isBank: Boolean

): User {
    val defaultBusinessProfile: BusinessProfile = defaultBusinessProfile
    val currentBusinessProfile: CurrentBusinessProfile = currentBusinessProfile

    return User(
        isEkycEnable = isEkycEnable ?: "0",
        smtEnabled = smtEnabled,
        isChequeApprovalEnable = isChequeApprovalEnable ?: "0",
        isUseUrl = isUseUrl ?: "0",
        userId = userId ?: "0",
        isPrimeUser = isPrimeUser,
        isRetrieveOTPEnable = isRetrieveOTPEnable ?: "0",
        employeeCode = employeeCode ?: "0",
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        imageUrl = imageUrl ?: "",
        mobile = mobile ?: "0",
        email = email ?: "",
        token = token ?: "0",
        userTypeSequence = userTypeSequence ?: "",
        defaultBusinessProfile = defaultBusinessProfile.transformDefaultBusinessProfile(),
        currentBusinessProfile = currentBusinessProfile.transformCurrentBusinessProfile(),
        password = password,
        isBank = isBank
    )
}

fun BusinessProfile.transformDefaultBusinessProfile(): BusinessProfileRequest {
    return BusinessProfileRequest(
        branchCode = branchCode ?: "0",
        userId = userId ?: "0"
    )
}

fun CurrentBusinessProfile.transformCurrentBusinessProfile(): CurrentBusinessProfileRequest {
    return CurrentBusinessProfileRequest(
        businessProfileId = businessProfileId ?: "0",
        name = name ?: "",
        businessUnitId = businessUnitId ?: "0",
        businessProfileName = businessProfileName
    )
}

fun List<ReportTypeResponse>.transformReportData(): List<ReportType> {
    return this.map {
        ReportType(name = it.name, systemName = it.systemName,
            childMenuList = it.childMenuList.map {
                ChildMenu(
                    name = it.name, systemName = it.systemName, ChildMenuList = it.ChildMenuList
                )
            })
    }
}

fun List<DigitalSalesKitMenu>.transformDigitalSalesKitData(): List<DigitalSalesKitData> {
    return this.map {
        DigitalSalesKitData(id = it.id, systemName = it.systemName,
        parentId = it.parentId,pdfPath = it.pdfPath)
    }
}

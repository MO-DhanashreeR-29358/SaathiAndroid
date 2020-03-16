package com.mosl.app.core.data.notification

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.addupdatemeeting.AppointmentAttendeeItemResponse
import com.mosl.mobile.domain.addupdatemeeting.SetAppointmentResponseData
import com.mosl.mobile.domain.createmeeting.AutoCompleteAttendeeItemResponse
import com.mosl.mobile.domain.createmeeting.MeetingAttendeeItemResponse
import com.mosl.mobile.domain.createmeeting.MeetingByIdItemResponse
import com.mosl.mobile.domain.notification.NotificationRequestInput
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.mobile.domain.setappointment.StageWiseFieldConfigurationItemResponse
import com.mosl.mobile.domain.setappointment.clientdetail.AdvisorsItemResponse
import com.mosl.mobile.domain.setappointment.clientdetail.AdvisoryMappingTypesItemResponse
import com.mosl.mobile.domain.setappointment.clientdetail.ClientDetailIResponseData
import com.mosl.mobile.domain.setappointment.clientdetail.ProductResponse
import com.mosl.mobile.domain.setappointment.clientdetail.ProductsItemResponse
import com.mosl.mobile.domain.setappointment.lead.ContactAddressItemResponse
import com.mosl.mobile.domain.setappointment.lead.ContactResponse
import com.mosl.mobile.domain.setappointment.lead.CrossSellingProductResponse
import com.mosl.mobile.domain.setappointment.lead.DedupeModelResponse
import com.mosl.mobile.domain.setappointment.lead.LeadFieldSectionResponse
import com.mosl.mobile.domain.setappointment.lead.LeadFieldValueDataResponse
import com.mosl.mobile.domain.setappointment.lead.LeadProductMappingItemResponse
import com.mosl.mobile.domain.setappointment.lead.LeadViewItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput
import com.mosl.saathi.api.model.addupdatemeeting.AppointmentAttendeeItem
import com.mosl.saathi.api.model.addupdatemeeting.SetAppointmentResponse
import com.mosl.saathi.api.model.clientdetail.AdvisorsItem
import com.mosl.saathi.api.model.clientdetail.AdvisoryMappingTypesItem
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.clientdetail.Product
import com.mosl.saathi.api.model.clientdetail.ProductsItem
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeItem
import com.mosl.saathi.api.model.createmeeting.MeetingAttendeeItem
import com.mosl.saathi.api.model.createmeeting.MeetingByIdItem
import com.mosl.saathi.api.model.lead.*
import com.mosl.saathi.api.model.notification.NotificationRequest
import com.mosl.saathi.api.model.notification.NotificationResponse

fun NotificationRequestInput.responseClientDetailItemTransform(): NotificationRequest {
    return NotificationRequest(category=category)
}

fun List<NotificationResponse>.responseNotificationDataTransform(): List<NotificationResponseData> {
    return safeMap {
        NotificationResponseData(
            message = it.message,

            creationDate = it.creationDate,

            additionalData = it.additionalData
        )
    }
}



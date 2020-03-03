package com.mosl.app.core.data.setappointment

import com.mosl.mobile.domain.setappointment.AutoCompleteAttendeeRequestInput
import com.mosl.mobile.domain.setappointment.LookupRequestInput
import com.mosl.mobile.domain.setappointment.MeetingByIdItemData
import com.mosl.mobile.domain.setappointment.SetAppointmentRepository
import com.mosl.mobile.domain.setappointment.clientdetail.ClientDetailIResponseData
import com.mosl.mobile.domain.setappointment.lead.LeadViewItemResponse
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest
import io.reactivex.Observable
import javax.inject.Inject

class SetAppointmentRepositoryImpl @Inject constructor(
  private val saathiApi: SaathiApi
) : SetAppointmentRepository {
  override fun getMeetingById(id: String): Observable<MeetingByIdItemData> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getClientDetail(uCID: String): Observable<ClientDetailIResponseData> {
    return saathiApi.getClientDetail(ClientDetailRequest(uCID.toInt())).map {
      ClientDetailIResponseData(
        familyUCID = it.familyUCID,
        bankName = it.bankName,
        creationDate = it.creationDate,
        isFamilyHead = it.isFamilyHead,
        products = it.products?.responseProductItemTransform(),
        advisoryType = it.advisoryType,
        email = it.email,
        address = it.address,
        productName = it.productName,
        clientProfile = it.clientProfile,
        sourcing = it.sourcing,
        accountOpeningDate = it.accountOpeningDate,
        uCID = it.uCID,
        profile = it.profile,
        clientName = it.clientName,
        pendingDays = it.pendingDays,
        pANNumber = it.pANNumber,
        primaryAdvisor = it.primaryAdvisor,
        dOB = it.dOB,
        subBrokerCode = it.subBrokerCode,
        createdBy = it.createdBy,
        employeeCode = it.employeeCode,
        oAOUserid = it.oAOUserid,
        mappedBy = it.mappedBy,
        accountNo = it.accountNo,
        stageName = it.stageName,
        channel = it.channel,
        defaultBank = it.defaultBank,
        secondaryAdvisor = it.secondaryAdvisor,
        familyHead = it.familyHead,
        accountType = it.accountType,
        mobile = it.mobile,
        vintagewithMotilalOswalYears = it.vintagewithMotilalOswalYears,
        aadhaarNumber = it.aadhaarNumber,
        clientProfileComplition = it.clientProfileComplition,
        clientCode = it.clientCode,
        areaCode = it.areaCode,
        bankAccountType = it.bankAccountType,
        leadID = it.leadID,
        isPotentialPMSProduct = it.isPotentialPMSProduct,
        mappingDate = it.mappingDate,
        branchCode = it.branchCode,
        bankAccountNumber = it.bankAccountNumber,
        isOnlyClient = it.isOnlyClient,
        createMeetingId = it.createMeetingId
      )
    }
  }

  override fun getClientImage(id: String): Observable<String> {
    return saathiApi.getClientImage(ClientImageRequest(id)).map {
      it.response
    }
  }

  override fun getLeadViewById(id: String): Observable<LeadViewItemResponse> {
    return saathiApi.getLeadView(LeadViewRequest(id)).map {
      LeadViewItemResponse(
        createdThrough = it.createdThrough,
        creationDate = it.creationDate,
        prospectRefereMobile = it.prospectRefereEmail,
        businessUnitID = it.businessUnitID,
        contactPerson = it.contactPerson,
        leadOwner = it.leadOwner,
        prospectSource = it.prospectSource,
        potentialPMSProductList = it.potentialPMSProductList,
        contact = it.contact?.responseContactTransform(),
        leadProductMapping = it.leadProductMapping?.responseLeadProductMappingItemTransform(),
        isAdminMode = it.isAdminMode,
        isPotentialPMSProductText = it.isPotentialPMSProductText,
        stageWiseFieldConfiguration = it.stageWiseFieldConfiguration.responseStageWiseFieldConfigurationItemTransform(),
        lastReCycleTransferDate = it.lastReCycleTransferDate,
        status = it.status,
        createdBy = it.createdBy,
        refereeEmail = it.refereeEmail,
        oAOUserid = it.oAOUserid,
        queryId = it.queryId,
        prospectRefereEmail = it.prospectRefereEmail,
        channelID = it.channelID,
        isProductSpecialist = it.isProductSpecialist,
        isAadhar = it.isAadhar,
        leadCategoryText = it.leadCategoryText,
        lastRecycleDate = it.lastRecycleDate,
        branchcode = it.branchcode,
        active = it.active,
        profession = it.profession,
        prospectRefereName = it.prospectRefereName,
        lastModificationDate = it.lastModificationDate,
        profileParamPercentage = it.profileParamPercentage,
        canEditLead = it.canEditLead,
        isRecycleLead = it.isRecycleLead,
        refereeMobile = it.refereeMobile,
        lastModifiedBy = it.lastModifiedBy,
        refereeName = it.refereeName,
        professionID = it.professionID,
        campaignName = it.campaignName,
        profile = it.profile,
        leadSourceDetails = it.leadSourceDetails,
        moneyControlPortfolioURL = it.moneyControlPortfolioURL,
        availableAddressTypes = it.availableAddressTypes,
        leadSourceName = it.leadSourceName,
        leadFieldSectionModels = it.leadFieldSectionModels?.responseLeadFieldSectionTransform(),
        profileID = it.profileID,
        dedupeModel = it.dedupeModel.responseDedupeModelTransform(),
        loginId = it.loginId,
        approvalMessage = it.approvalMessage,
        comments = it.comments,
        sourceID = it.sourceID,
        refereeCode = it.refereeCode,
        sourceName = it.sourceName,
        leadSourceID = it.leadSourceID,
        contactID = it.contactID,
        leadSourceDetailsID = it.leadSourceDetailsID,
        aMCCampaignID = it.aMCCampaignID,
        isAllowed = it.isAllowed,
        isPotentialPMSProductValue = it.isPotentialPMSProductValue,
        inApproval = it.inApproval,
        isReCycleTransfer = it.isReCycleTransfer,
        approvalFieldList = it.approvalFieldList,
        leadID = it.leadID,
        createMeetingId = it.createMeetingId,
        isPotentialPMSProduct = it.isPotentialPMSProduct,
        leadProfileParameters = it.leadProfileParameters,
        leadCategoryID = it.leadCategoryID,
        leadFieldValueModels = it.leadFieldValueModels?.responseLeadFieldValueDataTransform()
      )
    }
  }

  override fun getAllLookUp(request: LookupRequestInput) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun setAppointmentMeeting() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getAutoCompleteSplAttendee(request: AutoCompleteAttendeeRequestInput) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getAutoCompleteAttendee(request: AutoCompleteAttendeeRequestInput) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}
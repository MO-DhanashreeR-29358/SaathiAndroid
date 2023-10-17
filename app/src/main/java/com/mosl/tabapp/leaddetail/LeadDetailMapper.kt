package com.mosl.tabapp.leaddetail

import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.lead.*
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistItem
import com.mosl.tabapp.util.emptyString
import com.mosl.tabapp.util.nullSafe

fun List<ProductSpecialistItem>.productItemsToLeadEmployees(): List<LeadEmployees> {
  return mapNotNull {
    try {
      LeadEmployees(
          code = it.employeeCode,
          name = it.employeeName,
          branchCode = it.branchCode
      )
    } catch (_: Exception) {
      null
    }
  }
}

fun List<BusinessProfile>.profilesToLeadEmployees(): List<LeadEmployees> {
  return mapNotNull {
    try {
      LeadEmployees(
          code = it.employeeCode ?: emptyString(),
          name = it.employeeName ?: emptyString(),
          branchCode = it.branchCode ?: emptyString()
      )
    } catch (_: Exception) {
      null
    }
  }
}

fun LeadViewItem.leadViewItemToLeadDetail(): LeadDetail {
  return LeadDetail(
      leadOwner = leadOwner ?: emptyString(),
      contactPerson = contactPerson ?: emptyString(),
      sourceId = sourceID.toString(),
      leadProspectSourceId = prospectSource ?: emptyString(),
      refereeCode = refereeCode ?: emptyString(),
      refereeName = refereeName ?: emptyString(),
      profile = profile ?: emptyString(),
      brokingProfile = brokingFinal ?: emptyString(),
      ipProfile = ipProfile ?: emptyString(),
      finalProfile = finalProfie ?: emptyString(),
      profession = profession ?: emptyString(),
      createdDate = creationDate ?: emptyString(),
      isPotentialPMSProduct = isPotentialPMSProduct ?: emptyString(),
      leadId = leadID.toString(),
      leadCategoryText = leadCategoryText ?: emptyString(),
      sourceName = sourceName ?: emptyString(),
      referEmail = refereeEmail ?: emptyString(),
      referMobile = refereeMobile ?: emptyString(),
      createdBy = createdBy ?: emptyString(),
      bankName = bankName ?: emptyString(),
      branchName = branchcode ?: emptyString(),
      createdThrough = createdThrough ?: emptyString(),
      prospectSource = prospectSource ?: emptyString(),
      lastModifiedBy = lastModifiedBy ?: emptyString(),
      lastModifiedDate = lastModificationDate ?: emptyString(),
      comments = comments ?: emptyString(),
      leadSourceDetails = leadSourceDetails ?: emptyString(),
      canEditLead = canEditLead.toString(),
      isProductSpecialist = isProductSpecialist ?: emptyString(),
      prospectReferEmail = prospectRefereEmail ?: emptyString(),
      prospectRefereeMobile = prospectRefereMobile.nullSafe(),
      prospectReferName = prospectRefereName ?: emptyString(),
      contact = contact,
      leadProductMapping = leadProductMapping,
      leadSourceId = leadSourceID,
      isReferee = !(refereeCode?.isEmpty() ?: true && refereeName?.isEmpty() ?: true),
      sectorId = sectorId ?: emptyString(),
      sectorName = sector ?: emptyString(),
      designationId = desigId ?: emptyString(),
      designationName = desig ?: emptyString(),
      networthId = networthId ?: emptyString(),
      networthName = networth ?: emptyString(),
      referralEmpCode = referralEmpCode ?: emptyString(),
      referralEmpName = referralEmpName ?: emptyString(),
      clientType = clientType ?: emptyString(),
      productType = productType ?: emptyString(),
      expectedMargin = expectedMargin?: emptyString()
  )
}



fun List<LeadProductMappingItem>.leadProductMappingItemsToAddProductDatas(): List<AddProductData> {
  return mapNotNull {
    try {
      AddProductData(
          leadId = it.leadID?.toString() ?: emptyString(),
          ucId = it.uCID ?: emptyString(),
          potentialPmsProduct = it.isPotentialPMSProduct ?: emptyString(),
          stageId = it.stageID?.toString() ?: emptyString(),
          statusId = it.statusID?.toString() ?: emptyString(),
          interestedIn = it.interestedIn.toString(),
          prodName = it.productName ?: emptyString(),
          primRMName = it.primaryRMName ?: emptyString(),
          productSpecialistName = it.productSpecialistName ?: emptyString(),
          assignedBy = it.assignedBy ?: emptyString(),
          assignedByDate = it.assignedByDate ?: emptyString(),
          clientCode = it.clientCode ?: emptyString(),
          leadProductMappingId = it.leadProductMappingID?.toString() ?: emptyString(),
          stageName = it.stageName ?: emptyString(),
          statusName = it.statusName ?: emptyString(),
          crossSellingProducts = it.crossSellingProducts ?: emptyList(),
          isDefault = it.isDefault,
          isCrossSellingProduct = it.isCrossSellingProduct,
          isProdSpecEditDisabled = it.isProductSpecialistEditDisabled ?: emptyString(),
          isProdDisabled = it.isProductDisabled ?: emptyString(),
          prodSpecialist = it.productSpecialist ?: emptyString(),
          secondaryRm = it.secondaryRM?: emptyString()
      )
    } catch (_: Exception) {
      null
    }
  }
}

fun ClientDetailItem.clientDetailItemToClientDetail(): ClientDetail {

  val products = products?.map {
    Product(
        channel = it.channel ?: emptyString(),
        ucId = it.uCID.toString(),
        branchCode = it.branchCode,
        clientCode = it.holdingNumber ?: emptyString(),
        advisorCode = if (it.advisors?.isNotEmpty() == true) {
          it.advisors?.get(0)?.advisorCode ?: emptyString()
        } else emptyString()

    )
  } ?: emptyList<Product>()


  return ClientDetail(
      ucId = uCID.toString(),
      code = clientCode ?: emptyString(),
      name = clientName ?: emptyString(),
      address = address ?: emptyString(),
      pan = pANNumber ?: emptyString(),
      accountType = accountType ?: emptyString(),
      familyUcid = familyUCID,
      advisoryType = advisoryType ?: emptyString(),
      aadhar = aadhaarNumber ?: emptyString(),
      account = accountNo ?: emptyString(),
      profile = profile ?: emptyString(),
      defaultBank = defaultBank ?: emptyString(),
      products = products

  )

}

fun ClientSummaryResponse.summaryResponseToClientPortfolio(): ClientPortfolio {
  val portfolio = portfolioSnapshot[0]

  val brokingPortfolio = brokingProduct.map {
    BrokingPortfolio(
        productId = it.productId,
        productName = it.product,
        productType = it.productType,
        marketValue = it.marketValue,
        investmentValue = it.purchaseValue,
        overallGainLoss = it.overallGainLoss,
        percentageGainLoss = it.percentageGainLoss
    )
  }
  val assetPortfolio = assetProduct.map {
    AssetPortfolio(
        productId = it.productID,
        productName = it.product,
        productType = it.producttype,
        marketValue = it.marketValue,
        purchaseValue = it.purchaseValue,
        overallGainLoss = it.overallGainLoss,
        percentageGainLoss = it.percentageGainLoss
    )
  }
  val ledgers = cashPortfolio.map {
    AvailableLedger(
        productName = it.product,
        balanceLedger = it.totalLedgerBalance
    )
  }

  return ClientPortfolio(
      netWorth = portfolio.networth ?: emptyString(),
      dayChangeAmount = portfolio.dayChangeAmount ?: emptyString(),
      dayChangePer = portfolio.dayChangePercentage,
      dayFlag = portfolio.dayChangeUpdown.toInt(),
      overallFlag = portfolio.overallChangeUpdown.toInt(),
      overallChangeAmt = portfolio.overallChangeAmount ?: emptyString(),
      overallChangePer = portfolio.overallChangePercentage ?: emptyString(),
      brokingPortfolios = brokingPortfolio,
      assetPortfolios = assetPortfolio,
      availableLedgers = ledgers
  )
}

fun ClientGainLossSummaryResponse.gainLossSummaryToClientProfitLoss(): ClientProfitLoss {

  val product = totalGainLoss[0]

  val brokingProducts = brokingProductwiseGainloss.map {
    BrokingProduct(
        productId = it.assetSubTypeId,
        productName = it.assetSubTypeId,
        stGainLoss = it.assetSubTypeId,
        ltGainLoss = it.assetSubTypeId,
        totalGainLoss = it.assetSubTypeId
    )
  }

  val assetProducts = assetProductwiseGainloss.map {
    AssetProduct(
        productId = it.assetSubTypeId,
        productName = it.assetSubTypeId,
        stGainLoss = it.assetSubTypeId,
        ltGainLoss = it.assetSubTypeId,
        totalGainLoss = it.assetSubTypeId
    )
  }

  val limits = availableLimits.map {
    AvailableLimit(
        cashType = it.cASH ?: emptyString(),
        amount = it.aMOUNT ?: emptyString()
    )
  }
  return ClientProfitLoss(
      totalGainLoss = product.tOTALGAIN ?: emptyString(),
      stGainLoss = product.tOTALSTGAIN ?: emptyString(),
      ltGainLoss = product.tOTALLTGAIN ?: emptyString(),
      stGainLossPer = product.sTGAINPER ?: emptyString(),
      ltGainLossPer = product.lTGAINPER ?: emptyString(),
      brokingProducts = brokingProducts,
      assetProducts = assetProducts,
      availableLimits = limits,
      ltFlag = product.lTGAINUpdown.toInt(),
      stFlag = product.sTGAINUpdown
  )
}
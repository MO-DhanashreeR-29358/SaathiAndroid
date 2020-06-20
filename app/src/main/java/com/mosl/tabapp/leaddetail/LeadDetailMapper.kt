package com.mosl.tabapp.leaddetail

import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.lead.ClientGainLossSummaryResponse
import com.mosl.saathi.api.model.lead.ClientSummaryResponse
import com.mosl.saathi.api.model.lead.LeadViewItem
import com.mosl.tabapp.util.emptyString

fun LeadViewItem.leadViewItemToLeadDetail(): LeadDetail {
  return LeadDetail(
      leadOwner = leadOwner ?: emptyString(),
      contactPerson = contactPerson ?: emptyString(),
      sourceId = sourceID.toString(),
      leadProspectSourceId = prospectSource ?: emptyString(),
      refereeCode = refereeCode ?: emptyString(),
      refereeName = refereeName ?: emptyString(),
      profile = profile ?: emptyString(),
      profession = profession ?: emptyString(),
      createdDate = creationDate ?: emptyString(),
      isPotentialPMSProduct = isPotentialPMSProduct ?: emptyString(),
      leadId = leadID.toString(),
      leadCategoryText = leadCategoryText ?: emptyString(),
      sourceName = sourceName ?: emptyString(),
      referEmail = refereeEmail ?: emptyString(),
      referMobile = refereeMobile ?: emptyString(),
      createdBy = createdBy ?: emptyString(),
      createdThrough = createdThrough ?: emptyString(),
      prospectSource = prospectSource ?: emptyString(),
      lastModifiedBy = lastModifiedBy ?: emptyString(),
      lastModifiedDate = lastModificationDate ?: emptyString(),
      comments = comments ?: emptyString(),
      leadSourceDetails = leadSourceDetails ?: emptyString(),
      canEditLead = canEditLead.toString(),
      isProductSpecialist = isProductSpecialist ?: emptyString(),
      prospectReferEmail = prospectRefereEmail ?: emptyString(),
      prospectReferName = prospectRefereName ?: emptyString(),
      //contact = contact,
      //leadProductMapping = leadProductMapping,
      isReferee = !(refereeCode?.isEmpty() ?: true && refereeName?.isEmpty() ?: true)

  )
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
        marketValue = it.marketValue,
        purchaseValue = it.purchaseValue,
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
  val cashPortfolio = cashPortfolio.map {
    CashPortfolio(
        productName = it.product,
        totalLedgerBalance = it.totalLedgerBalance
    )
  }

  return ClientPortfolio(
      netWorth = portfolio.networth ?: emptyString(),
      dayChangeAmount = portfolio.dayChangeAmount ?: emptyString(),
      dayChangePer = portfolio.dayChangePercentage,
      overallChangeAmt = portfolio.overallChangeAmount ?: emptyString(),
      overallChangePer = portfolio.overallChangePercentage ?: emptyString(),
      brokingPortfolios = brokingPortfolio,
      assetPortfolios = assetPortfolio,
      cashPortfolios = cashPortfolio
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
        totalGainLoss = it.assetSubTypeId,
        assetSubType = it.assetSubTypeId
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
        cash = it.cASH,
        amount = it.aMOUNT
    )
  }
  return ClientProfitLoss(
      totalGain = product.tOTALGAIN ?: emptyString(),
      totalStGain = product.tOTALSTGAIN ?: emptyString(),
      totalLtGainLoss = product.tOTALLTGAIN ?: emptyString(),
      stGainPer = product.sTGAINPER ?: emptyString(),
      ltGainPer = product.lTGAINPER ?: emptyString(),
      brokingProducts = brokingProducts,
      assetProducts = assetProducts,
      availableLimits = limits
  )
}
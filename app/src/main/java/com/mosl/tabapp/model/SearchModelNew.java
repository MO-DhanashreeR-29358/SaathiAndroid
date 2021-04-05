package com.mosl.tabapp.model;

import java.io.Serializable;

import static com.mosl.tabapp.constants.MOSLConstants.SEARCH_TYPE_DASHBOARD_HINT;

/**
 * Created by srv_admin on 2/3/2016.
 */
public class SearchModelNew implements Serializable {
  //lead/client search result
  private String leadId;
  private String name;
  private String mobileNo;
  private String email;
  private String leadOwner;
  private String UCID = "0";
  private Integer oaoUserId = 0;
  private Integer diyCode = 0;
  private boolean expanded;
  private String sourceName;
  private String prospectSourceName;
  private String prospectSourceDetails;
  private String interestedIn;

  //Employee search result
  private String businessProfileId;
  private String businessProfileName;
  private String empCode;
  private String productId;
  private String searchType;

  private String branchCode;

  public SearchModelNew(String searchType, String leadId, String name, String mobileNo,
                        String email,
                        String UCID, String leadOwner, Integer oaoUserId, String sourceName,
                        String prospectSourceName, String prospectSourceDetails, String interestedIn,
                        String clientCode, int createMeetingId, int diyCode, String branchCode) {
    this.searchType = searchType;
    this.leadId = leadId;
    this.name = name;
    this.mobileNo = mobileNo;
    this.email = email;
    this.UCID = UCID;
    this.leadOwner = leadOwner;
    this.oaoUserId = oaoUserId;
    this.sourceName = sourceName;
    this.prospectSourceName = prospectSourceName;
    this.prospectSourceDetails = prospectSourceDetails;
    this.interestedIn = interestedIn;
    this.clientCode = clientCode;
    this.createMeetingId = createMeetingId;
    this.diyCode = diyCode;
    this.branchCode = branchCode;
  }

  public String getBranchCode() {
    return branchCode;
  }

  private String clientCode = "0";
  private int createMeetingId = 0;

  public SearchModelNew(String searchType, String empCode, String name, String businessProfileId,
                        String businessProfileName, String productId) {
    this.searchType = searchType;
    this.empCode = empCode;
    this.name = name;
    this.businessProfileId = businessProfileId;
    this.businessProfileName = businessProfileName;
    this.productId = productId;
  }

  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }

  public String getLeadId() {
    return leadId;
  }

  public void setLeadId(String leadId) {
    this.leadId = leadId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return in Case of employee it has ProfileID
   */
  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getGmail() {
    return email;
  }

  public void setGmail(String gmail) {
    this.email = gmail;
  }

  public String getUCID() {
    return UCID;
  }

  public void setUCID(String UCID) {
    this.UCID = UCID;
  }

  @Override
  public String toString() {
    return "";
  }

  public String getEmpCode() {
    return empCode;
  }

  public void setEmpCode(String empCode) {
    this.empCode = empCode;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBusinessProfileId() {
    return businessProfileId;
  }

  public void setBusinessProfileId(String businessProfileId) {
    this.businessProfileId = businessProfileId;
  }

  public String getBusinessProfileName() {
    return businessProfileName;
  }

  public void setBusinessProfileName(String businessProfileName) {
    this.businessProfileName = businessProfileName;
  }

  public String getLeadOwner() {
    return leadOwner;
  }

  public void setLeadOwner(String leadOwner) {
    this.leadOwner = leadOwner;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getSearchType() {
    return searchType;
  }

  public void setSearchType(String searchType) {
    this.searchType = searchType;
  }

  public boolean isEmpType(String searchType) {
    return searchType.equals(SEARCH_TYPE_DASHBOARD_HINT);
  }

  public Integer getOaoUserId() {
    return oaoUserId;
  }

  public void setOaoUserId(Integer oaoUserId) {
    this.oaoUserId = oaoUserId;
  }

  public boolean isOaoUserIdValid() {
    return oaoUserId != null && oaoUserId > 0;
  }

  public boolean isExpanded() {
    return expanded;
  }

  public void setExpanded(boolean expanded) {
    this.expanded = expanded;
  }

  public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  public String getProspectSourceName() {
    return prospectSourceName;
  }

  public void setProspectSourceName(String prospectSourceName) {
    this.prospectSourceName = prospectSourceName;
  }

  public String getProspectSourceDetails() {
    return prospectSourceDetails;
  }

  public void setProspectSourceDetails(String prospectSourceDetails) {
    this.prospectSourceDetails = prospectSourceDetails;
  }

  public String getInterestedIn() {
    return interestedIn;
  }

  public void setInterestedIn(String interestedIn) {
    this.interestedIn = interestedIn;
  }

  public String getClientCode() {
    return clientCode;
  }

  public void setClientCode(String clientCode) {
    this.clientCode = clientCode;
  }

  public boolean hasClientCode() {
    return !clientCode.isEmpty() && !clientCode.equals("0");
  }

  public int getCreateMeetingId() {
    return createMeetingId;
  }

  public void setCreateMeetingId(int createMeetingId) {
    this.createMeetingId = createMeetingId;
  }

  public Boolean isDiyLinkValid() {
    return diyCode > 0;
  }
}

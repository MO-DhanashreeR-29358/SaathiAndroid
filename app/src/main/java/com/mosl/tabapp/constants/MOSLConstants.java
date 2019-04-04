package com.mosl.tabapp.constants;

// interface MOSLConstants extends LabelConfig, ServiceDetails {
public interface MOSLConstants extends LabelConfig {
    String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=com.mosl.tabapp";
    String URL = "URL";
    String USER_TYPE_DIRECT = "1";

    String USER_TYPE_FRANCHISE = "2";

    int SUCCESS = 1;
    int FAILED = 2;
    int SEARCH_LENTH = 3;

    //Firebase notification constants
    String FCM_NOTIFICATION_SOURCE = "FCM_NOTIFICATION_SOURCE";
    String FCM_NOTIFICATION_CATEGORY = "FCM_NOTIFICATION_CATEGORY";
    String LOGIN_ID = "LOGIN";
    String PASSWORD = "PASSWORD";

    //Tab Constant
    int LEAD_DETAIL = 0;
    int LEAD_ACTIVITY = 1;
    int LEAD_PROFILING = 2;
    String YES = "YES";
    String NO = "NO";
    String POTENTIAL_INVEST_IN_PRODUCT = "Does the Lead have potential to invest in PMS product?";
    String SOURCE = "Source";
    //DRAWER MENU
    int MOSL_MENU_DASHBOARD = 0;
    int MOSL_MENU_EKYC = 1;
    int MOSL_MENU_FRESH_REGISTRATION = 0;
    int MOSL_MENU_EXISTINGS_APPLICATIONS = 1;
    int MOSL_MENU_MY_REPORTS = 2;
    int MOSL_MENU_CALENDAR = 3;
    int MOSL_MENU_PENDING_APPROVALS = 4;
    int MOSL_MENU_KNOWLEDGE_CENTRE = 5;
    int MOSL_MENU_BIRTHDAY_ANIVERSARY = 6;
    int MOSL_MENU_SUGGEST_ME = 7;
    int MOSL_MENU_QUERY = 8;
    int MOSL_MENU_LOGOUT = 9;
    int LOGOUT_CODE = 4;
    int CALL_PROSPECT_DETAILS = 2;
    int CALL_CONVERT_EKYC = 3;
    int NAV_TO_SET_APPOINTMENT = 2;
    int NAV_TO_PENDING_FOR_APPROVAL = 4;
    int NAV_TO_PENDING_APPROVAL = 3;
    int SECOND_CLICK_DASHBORD = 11;
    int SECOND_CLICK_SET_APPOINTMENT = 12;
    int NAV_TO_CREATE_MEETING = 13;
    int NAV_TO_CLIENT_MEETING = 14;
    int NAV_TO_UPDATE_MEETING = 15;
    int NAV_TO_LEAD_VIEW_MAIN = 16;
    int NAV_TO_LEAD_ACTIVITIES_DETAIL = 17;
    int NAV_TO_LEAD_DOCUMENT = 18;

    int NAV_TO_LEAD_CREATION = 21;
    int NAV_TO_ADVISORY_DASHBOARD = 22;

    int NAV_TO_SECOND_LEVEL_REPORT = 26;
    int NAV_TO_SECOND_LEVEL_HIERARCHY_REPORT = 27;
    int NAV_TO_WEB_VIEW = 28;

    // LookupType (GetAllLookup API)
    int CLIENTSTATUS = 1;
    int CLIENTNOTTRADEDSINCE = 2;
    int LEADCREATIONPRODUCT = 3;
    int REJECTIONREASON = 4;
    int INTERESTEDIN = 5;
    int PRODUCTTYPE = 6;
    int LEADCATEGORY = 7;
    int ACTIVITYTYPE = 13;
    int MEETINGREASON = 14;
    int MEETINGDURATION = 15;
    int BACLIENTMEETING = 16;
    int OUTSTATIONMEETING = 17;
    int MEETINGOBJECTIVE = 18;
    int ACTIONABLES = 20;
    int CATEGORY = 21;
    int SUBCATEGORY = 22;
    int LEADMODE = 12;
    int LEADPROFILE = 23;
    int WEEKS = 24;
    int DAYS = 25;
    int REPEATMODE = 26;
    int ADVISORYMAPPINGTYPE = 27;
    int BULKMAPPINGTYPE = 28;
    int MESSAGETYPE = 57;
    int BROKERNAMES = 64;
    int BACKGROUND = 65;
    int PROFESSIONID = 116;
    int MEETINGADDRESSID = 117;
    int TRAVELLINGMODEID = 118;
    //Contact Type Id
    int EMAILID = 1;
    int ALTERNATEEMAILID = 2;
    int MOBILENO = 3;
    int LANDLINENO = 4;
    int ALTERNATEMOBILENO = 5;
    int EKYCMOBILENO = 6;

    //Address Type Id
    int RESIDENCEADDRESS = 1;
    int OFFICEADDRESS = 2;

    //Contact Type Name
    String EMAILNAME = "Email Id";
    String ALTERNATEEMAILNAME = "Alternate Email Id";
    String MOBILENAME = "Mobile No.";
    String LANDLINENAME = "Landline No.";
    String ALTERNATEMOBILENAME = "Alternate Mobile No.";
    String EKYCMOBILENAME = "EKYC Mobile No.";
    String REPORT_PAGE_SIZE = "50";
    String DASHBOARD_REPORT_PAGE_SIZE = "100";

    //Common

    String SOME_ERROR_OCCURED_PLEASE_RETRY = "Some error occurred, please retry";
    String ERROR_LOADING_DATA_FOR = "Error loading data for: ";
    String MISSING_FILE = "File not found";
    String SOME_SERVER_ERROR_OCCURRED =
        "Something went wrong while getting response from server. Please try again.";
    String SOME_SEELECTION_METHOD = "Please select at least one answer.";
    String FILE_DOWNLOADED = "File downloaded at: ";
    String MISSING_FILE_VIEWER = "Need supported app to view file: ";
    String INTERNET_CONNECTION =
        "Operation could not be completed. Please check your network connections.";
    String UNAUTHORIZE_USER = "Session is expired, relogin is required";
    String CONFIRM_LEAD = "Are you sure want to create lead ?";
    String CONFIRM_LEAD_UPDATE = "Are you sure want to update lead ?";
    int UNAUTHORIZE_STATUS_CODE = 401;

    String CLIENT_INFO = "Android";
    String ADVISORY = "Advisory";
    String HIERARCHY = "Hierarchy";
    String TEAM = "Team";

    // Advisory
    String BUSINESS_LEAD_SNAPSHOT_TITLE = "My Business / Lead Snapshot";
    String ADVISORY_CLIENT_TITLE = "My Clients & Leads";
    String ADVISORY_TARGET_TITLE = "Target Vs Achievement";
    String ADVISORY_TEAM_PERFORMANCE_TITLE = "Performance & Incentives";
    String CLIENTS = "Clients";
    String LEAD = "Lead";
    String CLIENT_TRADED_YESTERDAY = "Client Traded Yesterday";
    String MAP = "map";
    String PERFORMANCE_INCENTIVE = "performance_incentive";
    String CURRENT_QUARTER_STATUS = "current_quarter_status";
    String MY_PERFORMANCE_INCENTIVE = "My_Performance_Incentive";
    String PERFORMANCE_INCENTIVE_ACHIEVEMNET = "Performance_Incentive_Achievemnet";
    String SALESSELF_PERFORMANCE_INCENTIVE = "SalesSelf-PerformanceandIncentive";
    String OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES =
        "OaoSalesOneup_TeamPerformanceAndIncentives";
    String SYS_MY_CLIENTS_SNAPSHOOT = "My_Clients_Snapshot";
    String MY_LEAD = "My_Lead";
    String MyClient_MyLead = "MyClient_MyLead";
    String ACTIONABLE = "Actionable";
    String TEAM_LEAD = "Team_Lead";
    String TEAM_TARGETVSACHIEVEMENT_TAB1CONTENT = "Team_target_achievemnet";
    String SELF_TARGET_ACHIEVEMNET = "target_achievemnet";
    String DASHBOARD_MODEL = "dashboardModel";
    String MONTH_ON_MONTH = "MonthOnMonth";
    String MONTH_ON_MONTH_ACCOUNT_REVENUE = "MonthOnMonthAccountRevenue";
    String SYS_LEAD_SNAPSHOT = "LeadSnapshot";
    String SYS_BUSINESS_SNAPSHOT = "BusinessSnapshot";
    String SYS_RH_BUSINESS_SNAPSHOT = "RH_BusinessSnapshot";
    String SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT =
        "ProductSalesDirect1UP_BusinessSnapshot";
    String SYS_TEAM_SNAPSHOT = "Team_Snapshot";
    String MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY = "MyClients_MySnapshot_Productivity";

    //Incentive performance
    String PERFORMANCE_PARAMETERS = "PerformanceParameters";
    String QUARTER_STATUS = "quarterstatus";
    String PERFORMANCE_COMPARISON = "performanceComparison";
    //Team
    String TEAM_SNAPSHOT_TITLE = "Team Snapshot & Leads";
    String TEAM_TARGET_TITLE = "Target Vs Achievement";
    String TEAM_PERFORMANCE_TITLE = "Performance Incentives";
    //hierarchy
    String BUSINESS_SNAPSHOT_TITLE = "Business Snapshot";
    String TRENDS_TITLE = "Month on Month Trend";

    //LeadView Tab
    String DETAILS = "Details";
    String ACTIVITIES = "Activities";
    String PROFILING = "Profiling";
    String MAPPING_HISTORY = "Mapping History";
    String MAPPING_INTERACTION = "Interaction & Events";

    //Lead Table
    String STAGE = "Stages";
    String LEAD_ALLOCATION = "Lead Allocation";
    String LEAD_FILTER = "Lead Filteration";
    String REJECTED = "Rejected";

    //Target tab
    String MEETING = "Meeting";
    String TALKTIME = "Talktime";
    String NEW_ACCOUNTS = "New Accounts";
    String REVENUE = "Revenue";
    String ONLINE_REVENUE = "Online Revenue";
    String IP_SALES = "IP Sales";
    String SIP_SALES = "SIP Sales";
    String BUSINESS = "Business On House Call";
    String TAB_POSITION = "Tab Position";

    //BusinessSnapshot tab
    String MAN_POWER = "Man Power";
    String MARGIN = "Margin";

    String LEADS_SEVEN = "Leads> 7Days";
    String LEADS_FIFTEEN = "Leads> 15Days";
    String FAMILY_SEVEN = "Family> 7Days";
    String DATE = "Date";
    String NAME = "Name";
    String TIME = "Time";
    String ENTITY_FIELD = "entity Field";

    String MONTH_NAME = "Month Name";
    String MONTH_DATE = "Month Date";
    String TYPE_MANAGEMENT = "Type of Mngnt";
    String TIME_RANGE = "Time Range";
    String ATTENDEE_FIRST_NAME = "Attendee First Name";
    String ATTENDEE_SECOND_NAME = "Attendee Second Name";
    String ATTENDEE_THIRD_NAME = "Attendee Third Name";

    String EMAIL_MESSAGE_CLIENT = "Do you want to send email to your client ?";
    String EMAIL_ALL_MESSAGE = "Do you want to send email to all clients ?";

    String ADDRESS = "Address";
    String CLIENT_NAME = "ClientName";
    String NEW_CLIENT_NAME = "Name";
    String MODULE_NAME = "Module Name";
    String CLIENT_CODE = "Client Code";
    String PRODUCT = "Product";

    String TRADING_CODE = "Trading Code";
    String UCID = "UCID";
    String ZERO = "0";
    String AMC_NAME = "AMC Name";
    String SCHEME_NAME = "Scheme Name";
    String FOLIO_NUMBER = "Folio Number";
    String SIP_AMOUNT = "Sip Amount";
    String START_DATE = "Start Date";
    String LST_INSTALLMENT_DATE = "Last Installment Date";
    String NO_OF_INSTALLMENT = "No Of Installment";
    String BRANCH_CODE = "Branch Code";
    String MARKET_VALUE = "Market Value";
    String SUB_BROKER_CODE = "Sub Broker Code";
    String STATUS = "Status";
    String MESSAGE = "Message";
    String Erros = "";
    String RESPONSE = "Response";
    String QUALIFIED = "Qualified";
    String REMARKS = "Remarks";

    //Login
    String USER = "User";
    String SMT_ENABLED = "SMTEnabled";
    String USER_ID = "Id";
    String BUSINESS_PROFILE_ID = "BusinessProfileId";
    String PROFILE_ID = "ProfileID";
    String PRODUCT_ID = "ProductId";
    String BUSINESS_PROFILE = "BusinessProfile";
    String EMPLOYEE_CODE = "EmployeeCode";
    String FIRST_NAME = "FirstName";
    String LAST_NAME = "LastName";
    String IMAGE_URL = "ImageUrl";
    String MOBILE = "Mobile";
    String LEAD_OWNER = "LeadOwner";
    String EMAIL = "Email";
    String OAOUSERID = "OAOUserID";
    String TOKEN = "Token";
    String CURRENT_BUSINESS_PROFILE = "CurrentBusinessProfile";
    String USER_TYPE_SEQ = "UserTypeSeq";
    //DefaultBusinessProfile
    String DEFAULT_BUSINESS_PROFILE = "DefaultBusinessProfile";
    String BRANCHCODE_DEFAULT_BUSINESS_PROFILE = "BranchCode";
    String CURRENT_BUSINESS_PROFILE_NAME = "Name";
    String BUSINESS_UNIT_ID = "BusinessUnitId";

    //GetAllLookup API
    String LOOKUPID = "LookUpid";
    String LOOKUPNAME = "LookUpName";
    String PARENTID = "ParentId";
    String LOOKUPTYPEID = "LookupTypeId";
    String LOOKUPTYPE = "LookupType";
    String BUSINESSUNITID = "BusinessUnitId";
    String SEQUENCE = "Sequence";
    String LOOKUPSTATUS = "Status";
    String ACTIVE = "Active";
    String SOURCEID = "SourceID";//SourceList
    String SOURCENAME = "SourceName";
    String IDENTIFIERPROPERTYNAME = "IdentifierPropertyName";
    String CREATEDBY = "CreatedBy";
    String CREATIONDATE = "CreationDate";
    String LASTMODIFIEDBY = "LastModifiedBy";
    String LASTMODIFICATIONDATE = "LastModificationDate";
    String ENTITYSTATUS = "EntityStatus";
    String INAPPROVAL = "InApproval";
    String APPROVALFIELDLIST = "ApprovalFieldList";
    String LEADSOURCEID = "LeadSourceID";//ProspectSourceList
    String LEADSOURCENAME = "LeadSourceName";
    String DRISHTILEADID = "DrishtiLeadID";
    String STAGEID = "StageID";//Stage
    String STAGECODE = "StageCode";
    String STAGENAME = "StageName";
    String SHOWPOTENTIALPOPUP = "ShowPotentialPopup";
    String STATUSID = "StatusID";//Status
    String STATUSNAME = "StatusName";
    String COUNTRYID = "CountryId";//Country
    String COUNTRYNAME = "CountryName";
    String STATEID = "StateId";//State
    String STATENAME = "StateName";
    String CITYID = "CityId";//City
    String CITYNAME = "CityName";

    String RESIDENCE = "Residence";//Address Type
    String OFFICE = "Office";

    //GetLeadDetail
    String LEADID = "LeadID";
    String CONTACTID = "ContactID";
    String CHANNELID = "ChannelID";
    String BRANCHCODE = "Branchcode";
    String LEADCATEGORYID = "LeadCategoryID";
    String CONTACTPERSON = "ContactPerson";
    String LEADSOURCEDETAILSID = "LeadSourceDetailsID";
    String REFEREECODE = "RefereeCode";
    String REFEREENAME = "RefereeName";
    String REFEREEEMAIL = "RefereeEmail";
    String REFEREEMOBILE = "RefereeMobile";

    String COMMENTS = "Comments";
    String ISPOTENTIALPMSPRODUCT = "IsPotentialPMSProduct";
    String ISONLYCLIENT = "IsOnlyClient";
    //Search
    String DATA = "Data";
    String METADATA = "MetaData";
    String LEAD_ID = "LeadID";
    String HIERARCHY_MAP = "HierarchyMap";
    String ADDITIONAL_MAP = "AdditionalMap";
    String NOTIFICATION_DATA = "NOTIFICATION_DATA";
    //  String MEETING_ID = "Meeting Id";
    String MEETING_ID = "MeetingID";
    String EMPLOYEE_NAME = "EmployeeName";
    String BUSINESSPROFILENAME = "BusinessProfileName";
    String MOBNO = "MOBNO";
    String UPDATED_MEETING = "Meeting updated successfully";
    String SET_APPOINTMENTS = "Appointment created successfully";
    String UPDATE_APPOINTMENTS = "Appointment updated successfully";
    String SET_USER_PROFILE = "User profile updated successfully";
    String CREATE_MEETING = "Meeting created successfully";
    String DOCUMENT_RENAME = "Document renamed successfully";
    String DOCUMENT_UPLOADED = "Document uploaded successfully";
    String DOCUMENT_DELETED = "Document deleted successfully";
    String APPT_CANCELLED = "Appointment cancelled successfully";
    String SELECT_MEETING_DURATION = "Select Meeting Duration";
    String SELECT_MEETING_REASON = "Select Meeting Reason";
    String SELECT_PRODUCT = "Select Product";
    String SELECT_PROFILE = "Select Profile";
    String SELECT_ACTIVITY_TYPE = "Select Activity Type";
    String SELECT_APPT_DATE_TYPE = "Appointment Date should be future Date";
    String SELECT_ACTIONABLES = "Select Actionables";
    String SELECT_BA_CLIENT_MEETING = "Select BA Client Meeting";
    String SELECT_OUT_STATION_MEETING = "Select Out Station Meeting";
    String SELECT_CATEGORY = "Select Category";
    String SELECT_MEETING_OBJECTIVE = "Select Meeting Objective";
    String SELECT_MEETING_LOCATION = "Select Meeting Location";
    String SELECT_TRAVELLING_MODE = "Select Travelling Mode";
    String SELECT_SUB_CATEGORY = "Select Sub Category";
    String SELECT_WEEK = "Select Week";
    String SELECT_WEEK_DAYS = "Select Week Day";
    String START_TIME_ERROR = "Start Date/Time should not be less than Appointment Date/Time";
    String END_TIME_ERROR = "End Date/Time should not be less than Start Date/Time";
    String END_TIME_EQUAL_ERROR = "End Date/Time should not be Equal to Start Date/Time";
    String EMAIL_SEND = "Email Send request is submitted successfully";
    String GIFT_SEND = "Send Gift request is submitted successfully";
    String ATLEAST_TEN_CHARACTERS = "Atleast 10 characters";
    String ATLEAST_FIFTY_CHARACTERS = "Atleast 50 characters";
    String ATLEAST_TWENTY_CHARACTERS = "Atleast 20 characters";
    String INVALID_PINCODE_NUMBER = "Invalid pincode number";
    String ATTENDEE_ALREADY_ADDED = "Attendee already added";
    String SPL_ATTENDEE_ALREADY_ADDED = "Special Attendee already added";
    String MEETING_DESCRIPTION_CANNOT_BE_LEFT_BLANK = "Meeting description cannot be left blank";
    String LOCATION_CANNOT_BE_LEFT_BLANK = "Meeting address cannot be left blank";

    //Birthday Message
    String BIRTHDAY_MESSAGE =
        "On your special day we wish you Strong Health, Great Success, and True Happiness in your life.";
    String ANNIVERSARY_MESSAGE =
        "May your marriage be blessed with love, joy and companionship for all the years of your lives! A very happy Wedding Anniversary to both of you!";

    //profiling constants
    String GROUPNAME = "GroupName";
    String PROFILEPARAMETERGROUPID = "ProfileParameterGroupID";
    String PROFILEPARAMETERLIST = "ProfileParameterList";
    String PROFILESUBPARAMETERLIST = "ProfileSubParameterList";
    String ISADMINMODE = "IsAdminMode";
    String SELECTEDPROFILESUBPARAMETERID = "SelectedProfileSubParameterID";
    String VALUE = "Value";
    String HIDEDEPEDENTQUESTION = "HideDepedentQuestion";
    String IsHide = "IsHide";
    String IS_SELECTED = "IsSelected";
    String PROFILEPARAMETERID = "ProfileParameterID";
    String QUESTIONTYPE = "QuestionType";
    String QUESTIONTEXT = "QuestionText";
    String IS_DEFAULTHIDDEN = "IsDefaultHidden";
    String SUBPARAMETERCONTROLTYPE = "SubParameterControlType";
    String PROFILESUBPARAMETERID = "ProfileSubParameterID";
    String PROFILESUBPARAMETER = "ProfileSubParameter";
    String QUESTIONSUBTEXT = "QuestionSubText";
    //TODO get propper message
    String PROFILE_EDIT_MESSAGE = "Please enter Profile details";
    String EXTRAINFO = "ExtraInfo";
    String EXTRAINFOROWCOUNT = "ExtraInfoRowCount";
    String EXTRAINFOFIELDNAME = "ExtraInfoFieldName";

    //Pending Approval

    String EMPLOYEE_CODE_APP = "Employee Code";
    String EMP_NAME = "Emp Name";
    String LEAD_ID_APP = "Lead ID";
    String FIELD_NAME = "Field Name";
    String REQUEST_DATE = "Request Date";
    String APPROVED_DATE = "Approved/Reject Date";
    String SUB_OPTION = "Sub Option";
    String APPROVAL_REQUEST_ID = "ApprovalRequestId";

    String IS_DEFAULT = "IsDefault";
    String PENDING_DAYS = "PendingDays";
    String CONTACT = "contact";
    String CLIENTCODE = "ClientCode";
    String ENCRYPTED_URL = "EncryptedURL";
    String LEADPRODUCTMAPPING = "leadProductMapping";
    //Size if lru chache
    int CHACHE_SIZE = 10;

    //franchise constants
    String FieldName = "FieldName";
    String ControlType = "ControlType";
    String DropdownValue = "DropdownValue";
    String SectionName = "SectionName";
    String SectionCode = "SectionCode";
    String SectionHeader = "SectionHeader";
    String LFSysName = "LF_SysName";
    String LeadFieldId = "LeadFieldId";
    String FieldSectionId = "FieldSectionId";
    String LeadFieldValue = "LeadField_Value";
    String LEADFIELDSECTIONMODELS = "leadFieldSectionModels";
    String leadFieldValueModels = "leadFieldValueModels";
    String MANDATORY = "Mandatory";
    String NO_DATA_FOUND = "No data found";
    String NO_RECORDS_FOUND = "No records found";
    String SYSTEM_NAME = "system_name";
    String FILTER_LIST = "filter_list";
    String SPINNER_LIST = "spinner_list";

    String PARAMETERS = "Parameters";
    String Q1 = "Q1";
    String Q2 = "Q2";
    String Q3 = "Q3";
    String Q4 = "Q4";
    String YTD = "YTD";
    String FAMILYSTATUS = "FAMILYSTATUS";
    String PARAMETER = "Parameter";
    String WIDGETTYPE = "WidgetType";
    String DAY_CNT = "DAY_CNT";
    String DAY = "Day";
    String MTD_CNT = "MTD_CNT";
    String MTD = "MTD";
    String YTD_CNT = "YTD_CNT";

    String CHANNEL = "Channel";
    String PANNUMBER = "PANNumber";
    String ACCOUNTTYPE = "BankAccountType";
    String FAMILYUCID = "FamilyUCID";
    String ADVISORYTYPE = "AdvisoryType";
    String AADHAARNUMBER = "AadhaarNumber";
    String PRODUCTS = "Products";
    String BRANCH_Code = "BranchCode";
    String DEFAULT_BANK = "BankName";
    String ACCOUNT_NO = "BankAccountNumber";
    String PROFILE = "ClientProfile";
    String ATTENDEEE_NOT_FOUND_MESSAGE = "Not found";

    //dashbord second level
    String COL_FILTER = "colfilter";
    String ROW_FILTER = "rowfilter";
    String WIDGETNAME = "Widget name";
    String LABEL = "Label";
    String KEY = "key";
    String FILTERFIRST = "filterFirst";
    String FILTERSECOND = "filterSecond";
    String FILTERTHIRD = "filterThird";
    //profile type
    String RETAIL = "retail";
    String FRANCHIES = "franchies";
    String IFA = "ifa";

    String URL_PATH = "URP PATH";

    String SEARCH_TYPE_LEAD_HINT = "Search Lead";
    String SEARCH_TYPE_CLIENT_HINT = "Search Client";
    String SEARCH_TYPE_DASHBOARD_HINT = "Dashboard for";

    String SELF_KEY = "flt_hierarchy";
    String REVENUE_TYPE = "RevenueType";
    String REVENUE_GENERATED = "REVENUE GENERATED";

    String FLT_DATE = "flt_date";
    String FLT_DATE_TIME = "flt_datetime";
    String ACHIEVED = "_achieved";
    String UNIQUE = "_unique";

    //portfolio

    String CURRENT_VALUE = "Current Value";
    String PURCHASE_VALUE = "Purchase Value";
    String OVERALL_GAIN_LOSS = "Overall gain Loss";
    String PERCENTAGE_GAIN_LOSS = "Percentage gain loss";
    String INVEST_AMT = "Investment Amount";
    String STGL = "ST G/L";
    String LTGL = "LT G/L";
    String CASH = "Cash";
    String RUPEE = "\u20B9";
    String TOTAL_GAIN_LOSS = "Total Gain Loss";
    String AMOUNT = "Amount";
    String PRODUCT_TYPE = "Product type";
    String TOTAL = "TOTAL";
    String ASSET_SUB_TYPE_ID = "Asset Sub Type ID";
    String MUTUAL_FUND = "Mutual Fund";

    String MeetingDate = "MeetingDate";
    String ActivityTypeID = "ActivityTypeID";
    String ProductTypeID = "ProductTypeID";
    String REPEATEVERYWEEKCODE = "repeatEveryWeekCode";
    String REPEATEVERYDAYCODE = "repeatEveryDayCode";
    String REPEATATIONSTARTON = "repeatationStartOn";
    String REPEATATION_END_SON = "repeatationEndsOn";
    String MEETING_ATTENDEE = "meetingAttendee";
    String ATTENDEETYPE = "AttendeeType";

    String ATTENDEE_EMPCODE = "AttendeeEmpcode";

    String ATTENDEE_EMPNAME = "AttendeeEmpName";
    String MEETING_ATTENDEE_CODE = "MeetingAttendeeID";
    String IS_ATTENDED = "Attended";
    String DEFAULTATTENDEE = "DefaultAttendee";
    String TAG_ATTENDEENAME = "AttendeeName";
    String MEETINGTYPE = "MeetingType";
    String ATTENDEECODE = "AttendeeCode";
    String REPEATONCODE = "repeatOnCode";
    String mFormat = "yyyy-MM-dd'T'kk:mm:ss";
    //   String CAL_DATE_FORMAT="dd/MMM/yyyy";
    String CAL_DATE_FORMAT = "dd-MM-yyyy";
    String mFormatWithSpace = "yyyy-MM-dd' 'kk:mm:ss";
    String mFormatWithDash = "yyyy-MM-dd'-'kk:mm:ss";
    String DATE_FORMAT_TO_DISPLAY = "dd MMM yyyy hh:mm a";
    String DATE_FORMAT_TO_SERVER = "yyyy-MM-dd'T'kk:mm:ssZZZ";
    String DATE_FORMAT_TO_SERVER_MEETING = "yyyy-MM-dd'T'kk:mm:ss";
    String DATE_FORMAT_TO_SERVER_MEETING_SEND = "yyyy-MM-dd";
    String DATE_FORMAT_OF_DD_MMM_YYYY = "dd-MMM-yyyy";
    String NEWDATE_FORMAT_OF_DD_MMM_YYYY = "dd MMM yyyy";

    int FILE_UPLOAD_SIZE_LIMT = 5;
    int FILE_UPLOAD_COUNT_LIMT = 4;
    String FILE_LARGE_MESSAGE =
        "User not allow to upload More then " + FILE_UPLOAD_SIZE_LIMT + " MB File.";
    String FILE_NOT_ALLOWED_MESSAGE = "File not valid";
    String SYS_NAME = "SysName";
    String SAME_MOBILEMESSAGE = "Alternate mobile No. cannot be same";
    String SAME_EMAILMESSAGE = "Alternate email Id cannot be same";
    String INVALID_TIME = "Meeting cannot be scheduled in future datetime";

    //region notification keys

    //    target_achievemnet
    String TARGET_ACHIEVEMENT = "target_achievemnet";
    String Actionable_1stMeetingNotDone = "Actionable_1stMeetingNotDone";
    //endregion

    String MESSAGE_DOB_ANNI_COMPARE = "Anniversary Should be less than Date of birth";
    String MESSAGE_DOB_ANNI_EQUAL = "Anniversary Should not be same as Date of birth";

    String CAUGHT_IN_C2 = "CaughtInC2";
    String CAUGHT_IN_C2_MESSAGE = "Do you want to assign the lead to yourself";
    String ADDITIONALDATA = "AdditionalData";
    String INVALID_DATE_RANGE = "Select correct from and to date";
    String ID_OBJECT = "{\"id\":%s}";

    String EKYC_SERVER_DOWN = "Ekyc services are down";
    String USERID = "Userid";

    String PROSPECT_SOURCENAME = "Prospect sourceName";
    String PROSPECT_SOURCEDETAIL = "Prospect sourceDetail";
    String INTRESTEDIN = "IntrestedIn";
    String NEW_CRM = "NEW_CRM";

    //newEkyc
    String NEW_USERID = "USERID";
    String BRANCHEMPCODE = "BRANCHEMPCODE";
    String COMMANDTYPE = "COMMANDTYPE";
    String APPLICATIONTYPE = "APPLICATIONTYPE";
    String OSTYPE = "OSTYPE";
    String APPLOGINID = "APPLOGINID";
    String OUTPUT = "output";
    String TOKENKEY = "TOKENKEY";
    String EKYC_APP_NOT_AVAILABLE = "Ekyc app not available";
    String NEWEKYC_PACKAGE_NAME = "com.mosl.mobile.modao";
    String WEBSITE_CLIENT_REFERRAL = "Website-Client Referral";
    String GETSTAGEMOBILESELECTLIST = "Stage/GetStageMobileSelectList";

}


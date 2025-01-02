package com.mosl.tabapp.constants;

// interface MOSLConstants extends LabelConfig, ServiceDetails {
public interface MOSLConstants extends LabelConfig {

    int SEARCH_LENTH = 3;

    //Firebase notification constants
    String FCM_NOTIFICATION_SOURCE = "FCM_NOTIFICATION_SOURCE";

    //Tab Constant
    int LEAD_DETAIL = 0;

    String YES = "YES";
    String NO = "NO";
    String POTENTIAL_INVEST_IN_PRODUCT = "Does the Lead have potential to invest in PMS product?";
    String SOURCE = "Source";
    //DRAWER MENU

    int LOGOUT_CODE = 4;
    // LookupType (GetAllLookup API)
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
    int LEADPROFILE = 23;
    int LEADBROKINGPROFILE = 129;
    int LEADIPPROFILE = 130;
    int LEADFINALPROFILE = 131;
    int BANKMEETINGTYPE = 128;
    int WEEKS = 24;
    int DAYS = 25;
    int REPEATMODE = 26;
    int PROFESSIONID = 116;
    int MEETINGADDRESSID = 117;
    int TRAVELLINGMODEID = 118;

    //Address Type Id
    int RESIDENCEADDRESS = 1;
    int OFFICEADDRESS = 2;

    //Contact Type Name
    String REPORT_PAGE_SIZE = "50";
    String DASHBOARD_REPORT_PAGE_SIZE = "100";

    int SECTOR = 137;
    int DESIGNATION = 136;
    int NETWORTH = 138;

    int CLIENTTYPE = 140;

    int LEADACCOUNTTYPE = 145;

    String SOME_ERROR_OCCURED_PLEASE_RETRY = "Some error occurred, please retry";
    String SOME_SERVER_ERROR_OCCURRED =
            "Something went wrong while getting response from server. Please try again.";
    String SOME_SEELECTION_METHOD = "Please select at least one answer.";
    String INTERNET_CONNECTION =
            "Operation could not be completed. Please check your network connections.";
    String CONFIRM_LEAD = "Are you sure want to create lead ?";
    String CONFIRM_LEAD_UPDATE = "Are you sure want to update lead ?";
    String ErrorMsgName = "Only alphabets are allowed";

    // Advisory
    String LEAD = "Lead";
    String MAP = "map";
    String PERFORMANCE_INCENTIVE = "performance_incentive";
    String CURRENT_QUARTER_STATUS = "current_quarter_status";
    String DASHBOARD_MODEL = "dashboardModel";
    String MONTH_ON_MONTH_ACCOUNT_REVENUE = "MonthOnMonthAccountRevenue";

    //Team
    String TEAM_TARGET_TITLE = "Target Vs Achievement";
    //hierarchy
    String BUSINESS_SNAPSHOT_TITLE = "Business Snapshot";

    //LeadView Tab
    String DETAILS = "Details";
    String ACTIVITIES = "Activities";
    String PROFILING = "Profiling";
    String MAPPING_HISTORY = "Mapping History";
    String MAPPING_INTERACTION = "Interaction & Events";

    //Lead Table
    String REJECTED = "Rejected";

    //Target tab
    //BusinessSnapshot tab
    String DATE = "Date";
    String NAME = "Name";
    String TIME = "Time";
    String ENTITY_FIELD = "entity Field";

    String EMAIL_MESSAGE_CLIENT = "Do you want to send email to your client ?";
    String EMAIL_ALL_MESSAGE = "Do you want to send email to all clients ?";

    String NEW_CLIENT_NAME = "Name";
    String MODULE_NAME = "Module Name";
    String CLIENT_CODE = "Client Code";
    String PRODUCT = "Product";

    String UCID = "UCID";
    String STATUS = "Status";
    String MESSAGE = "Message";
    String EQUITY = "Equity";
    String MF = "MF";

    //Login
    String PROFILE_ID = "ProfileID";
    String PRODUCT_ID = "ProductId";
    String EMPLOYEE_CODE = "EmployeeCode";
    String MOBILE = "Mobile";
    String CREATE_MEETING_ID = "CreateMeeting";
    String FLG_REACTIVATION_CLIENT = "flgReactivatedClient";
    //DefaultBusinessProfile

    //GetAllLookup API

    String RESIDENCE = "Residence";//Address Type
    String OFFICE = "Office";

    //GetLeadDetail
    String LEADID = "LeadID";

    String OK = "OK";

    //Search
    String LEAD_ID = "LeadID";
    String NOTIFICATION_DATA = "NOTIFICATION_DATA";
    String MEETING_ID = "MeetingID";
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
    String SELECT_BANK = "Select Bank";
    String SELECT_BRANCH = "Select Branch";
    String SELECT_BANK_BRANCH = "Select Bank Branch";
    String SELECT_MEETING_REASON = "Select Meeting Reason";
    String SELECT_PRODUCT = "Select Product";
    String SELECT_PROFILE = "Select Profile";
    String SELECT_ACTIVITY_TYPE = "Select Activity Type";
    String SELECT_SECTOR =  "Select Sector";
    String SELECT_DESIGNATION =  "Select Designation";
    String SELECT_NETWORTH =  "Select Networth";
    String SELECT_APPT_DATE_TYPE = "Appointment Date should be future Date";
    String SELECT_ACTIONABLES = "Select Actionables";
    String SELECT_CATEGORY = "Select Category";
    String SELECT_MEETING_OBJECTIVE = "Select Meeting Objective";
    String SELECT_SUB_CATEGORY = "Select Sub Category";
    String SELECT_WEEK = "Select Week";
    String SELECT_WEEK_DAYS = "Select Week Day";
    String START_TIME_ERROR = "Start Date/Time should not be less than Appointment Date/Time";
    String END_TIME_ERROR = "End Date/Time should not be less than Start Date/Time";
    String END_TIME_EQUAL_ERROR = "End Date/Time should not be Equal to Start Date/Time";
    String EMAIL_SEND = "Email Send request is submitted successfully";
    String GIFT_SEND = "Send Gift request is submitted successfully";
    String ATLEAST_TEN_CHARACTERS = "Atleast 10 characters";
    String ATTENDEE_ALREADY_ADDED = "Attendee already added";
    String EMPLOYEE_ALREADY_ADDED = "Employee already added";
    String SPL_ATTENDEE_ALREADY_ADDED = "Special Attendee already added";
    String MEETING_DESCRIPTION_CANNOT_BE_LEFT_BLANK = "Meeting description cannot be left blank";

    //Birthday Message
    String BIRTHDAY_MESSAGE =
            "On your special day we wish you Strong Health, Great Success, and True Happiness in your life.";
    String ANNIVERSARY_MESSAGE =
            "May your marriage be blessed with love, joy and companionship for all the years of your lives! A very happy Wedding Anniversary to both of you!";

    //Pending Approval

    String EMPLOYEE_CODE_APP = "Employee Code";
    String EMP_NAME = "Emp Name";
    String LEAD_ID_APP = "Lead ID";
    String FIELD_NAME = "Field Name";
    String REQUEST_DATE = "Request Date";
    String APPROVED_DATE = "Approved/Reject Date";
    String SUB_OPTION = "Sub Option";
    String APPROVAL_REQUEST_ID = "ApprovalRequestId";


    //franchise constants
    String SYSTEM_NAME = "system_name";
    String FILTER_LIST = "filter_list";
    String SPINNER_LIST = "spinner_list";

    String FAMILYSTATUS = "FAMILYSTATUS";
    String PARAMETER = "Parameter";
    String WIDGETTYPE = "WidgetType";
    String MTD_CNT = "MTD_CNT";
    String MTD = "MTD";
    String YTD_CNT = "YTD_CNT";
    String REVENUE_TYPE = "REVENUETYPE";
    String NO_OF_CLIENTS = "NOOFCLIENTS";
    String REVENUE_GENERATED = "REVENUEGENERATED";

    String ATTENDEEE_NOT_FOUND_MESSAGE = "Not found";

    //dashbord second level
    String COL_FILTER = "colfilter";
    String ROW_FILTER = "rowfilter";
    String WIDGETNAME = "Widget name";
    String LABEL = "Label";
    String KEY = "key";

    String TYPE = "Type";
    String POSITION = "Position";
    //profile type

    String URL_PATH = "URP PATH";

    String SEARCH_TYPE_DASHBOARD_HINT = "Dashboard for";

    String SELF_KEY = "flt_hierarchy";


    //portfolio

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

    String mFormat = "yyyy-MM-dd'T'kk:mm:ss";
    //   String CAL_DATE_FORMAT="dd/MMM/yyyy";
    String CAL_DATE_FORMAT = "dd-MM-yyyy";
    String CAL_DATE_FORMAT_WITH_TIME = "dd-MM-yyyy' 'kk:mm:ss";
    String mFormatWithSpace = "yyyy-MM-dd' 'kk:mm:ss";
    String mFormatWithDash = "yyyy-MM-dd'-'kk:mm:ss";
    String DATE_FORMAT_TO_DISPLAY = "dd MMM yyyy hh:mm a";
    String DATE_FORMAT_TO_SERVER = "yyyy-MM-dd'T'kk:mm:ssZZZ";
    String DATE_FORMAT_TO_SERVER_MEETING = "yyyy-MM-dd'T'kk:mm:ss";
    String DATE_FORMAT_TO_SERVER_MEETING_SEND = "yyyy-MM-dd";
    String DATE_FORMAT_OF_DD_MMM_YYYY = "dd-MMM-yyyy";

    int FILE_UPLOAD_SIZE_LIMT = 5;
    int FILE_UPLOAD_COUNT_LIMT = 4;
    String FILE_LARGE_MESSAGE =
            "User not allow to upload More then " + FILE_UPLOAD_SIZE_LIMT + " MB File.";
    String FILE_NOT_ALLOWED_MESSAGE = "File not valid";
    String INVALID_TIME = "Meeting cannot be scheduled in future datetime";

    //region notification keys
    String MESSAGE_DOB_ANNI_COMPARE = "Anniversary Should be less than Date of birth";
    String MESSAGE_DOB_ANNI_EQUAL = "Anniversary Should not be same as Date of birth";

    String INVALID_DATE_RANGE = "Select correct from and to date";

    String WEBSITE_CLIENT_REFERRAL = "Website-Client Referral";

    // Prospect Reffer changes
    String ENTER_PROSPECT_REFEREE_NAME = "Enter Prospect Referee Name";
    String INVALID_MOBILE_NO = "Invalid mobile number.";
    String INVALID_EMAIL_ID = "Invalid email id.";
    String ENTER_EMAIL_ID = "Enter email id.";
    String ENTER_MOBILE_NUMBER = "Enter mobile number";
    String WEBSITE_PROSPECT_REFERRAL = "Website-ProspectReferral";
    String BRANCH_CODE_FSFBMAIN = "FSFBMAIN";
    String BRANCH_CODE_CBIMAIN = "CBIMAIN";
    String MOCK_LOCATION_ENABLED_MSG =
            "Mocked location enabled. Please uninstall mock location apps to continue ";

    String STRING_MAP = "map";
    String STRING_TITLE = "title";
    String REACTIVATION_PERIOD = "period_filter";
    String REACTIVATION_TYPE = "reactivation_type";
    String REACTIVATION_EMP_CODE = "emp_code";
    String REACTIVATION_FIRST_REPORTING_TO = "first_reporting_to";

    String ROOTED_DEVICE_MESSAGE = "Your device is rooted, You will not be able to use the application!!";
    String REACTIVATION_SOURCE = "source_filter";

    int INDIRECT_CATEGORY = 23;

    //UAT
    //int INDIRECT_INTERESTED_IN = 183;
    //Live
    int INDIRECT_INTERESTED_IN = 179;

    int INDIRECT_WORK_EXP = 147;

    String INDIRECT_TALK_TIME = "Talk Time";
    String INDIRECT_TOP_INCENTIVE = "Top Incentive Clients";
    String INDIRECT_CLIENT_REACTIVATION = "Client Reactivation";
    String INDIRECT_TOP_CLIENT_LIST = "Top client list giving 25% or above revenue";

}


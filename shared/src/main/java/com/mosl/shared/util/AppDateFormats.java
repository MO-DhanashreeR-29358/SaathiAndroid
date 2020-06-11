package com.mosl.shared.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by lalkishore on 09-11-2017.
 */

public class AppDateFormats {
	public static final String JSON_DATE = "dd-MM-yyyy HH:mm:ss";
	public static final DateFormat DF_JSON_DATE = new SimpleDateFormat(JSON_DATE, Locale.ENGLISH);
	private static final String date1 = "dd MMM yyyy";
	public static final DateFormat df_Date1 = new SimpleDateFormat(date1, Locale.ENGLISH);
	private static final String date6 = "yyyy-MM-dd";
	public static final DateFormat df_Date6 = new SimpleDateFormat(date6, Locale.ENGLISH);

	private static final String date9 = "dd/MM/yyyy";
	public static final DateFormat df_Date9 = new SimpleDateFormat(date9, Locale.ENGLISH);

	private static final String date8 = "dd/MM/yyyy";
	public static final DateFormat df_Date8 = new SimpleDateFormat(date8, Locale.ENGLISH);
	/**
	 * This date format is returned from new Notification Service.
	 * From Subham. (24/08/2016)
	 */
	private static final String JSON_DATE_3 = "dd MMM yyyy HH:mm";
	public static final DateFormat DF_JSON_DATE3 =
		new SimpleDateFormat(JSON_DATE_3, Locale.ENGLISH);
	private static final String MMM = "MMM";
	public static final DateFormat DF_MONTH = new SimpleDateFormat(MMM, Locale.ENGLISH);
	private static final String DD = "dd";
	public static final DateFormat DF_DAY = new SimpleDateFormat(DD, Locale.ENGLISH);
	private static final String YYYY = "yyyy";
	public static final DateFormat DF_YEAR = new SimpleDateFormat(YYYY, Locale.ENGLISH);
	private static final String TIME = "h:mm a";
	public static final DateFormat DF_TIME = new SimpleDateFormat(TIME, Locale.ENGLISH);
	private static final String TIME_HH = "HH:mm a";
	public static final DateFormat DF_TIME_HH = new SimpleDateFormat(TIME_HH, Locale.ENGLISH);

	private static final String JSON_DATE4 = "dd-MM-yyyy";
	public static final DateFormat DF_JSON_DATE4 = new SimpleDateFormat(JSON_DATE4, Locale.ENGLISH);

	private static final String DATE = "yyyyMMddHHmmss";
	public static final DateFormat DF_DATE = new SimpleDateFormat(DATE, Locale.ENGLISH);
	private static final String DATE_1 = "yyyyMMddHHmm";
	public static final DateFormat DF_DATE_1 = new SimpleDateFormat(DATE_1, Locale.ENGLISH);
	private static final String JSON_DATE_4 = "yyyy-MM-dd'T'HH:mm:ss";
	public static final DateFormat DF_JSON_DATE_4 =
		new SimpleDateFormat(JSON_DATE_4, Locale.ENGLISH);

	private static final String JSON_CLIENT_DATE = "dd MMM,yyyy";
	public static final DateFormat df_Date = new SimpleDateFormat(JSON_CLIENT_DATE, Locale.ENGLISH);

	private static final String JSON_DATE5 = "dd/MM/yyyy HH:mm:ss a";
	public static final DateFormat DF_JSON_DATE5 = new SimpleDateFormat(JSON_DATE5, Locale.ENGLISH);

	private static final String JSON_DATE6 = "dd-MMM-yyyy";
	public static final DateFormat DF_JSON_DATE6 = new SimpleDateFormat(JSON_DATE6, Locale.ENGLISH);

	private static final String JSON_DATE7 = "MM/dd/yyyy HH:mm:ss a";
	public static final DateFormat DF_JSON_DATE7 = new SimpleDateFormat(JSON_DATE7, Locale.ENGLISH);

	private static final String JSON_DATE8 = "MMM yyyy";
	public static final DateFormat DF_JSON_DATE8 = new SimpleDateFormat(JSON_DATE8, Locale.ENGLISH);

	private static final String JSON_DATE9 = "yyyy-MM-dd' 'HH:mm:ss";
	public static final DateFormat DF_JSON_DATE9 = new SimpleDateFormat(JSON_DATE9, Locale.ENGLISH);

  private static final String JSON_DATE10 = "yyyy-MM-dd'T'kk:mm:ss";
  public static final DateFormat DF_JSON_DATE10 = new SimpleDateFormat(JSON_DATE10, Locale.ENGLISH);

    private static final String JSON_DATE11 = "yyyy-MM-dd'T'kk:mm:ssZZZ";
    public static final DateFormat DF_JSON_DATE11 =
        new SimpleDateFormat(JSON_DATE11, Locale.ENGLISH);

    private static final String JSON_DATE12 = "dd MMM yyyy hh:mm a";
    public static final DateFormat DF_JSON_DATE12 =
        new SimpleDateFormat(JSON_DATE12, Locale.ENGLISH);

  private static final String date10 = "MM/dd/yyyy";
  public static final DateFormat df_date10 = new SimpleDateFormat(date10, Locale.ENGLISH);
}

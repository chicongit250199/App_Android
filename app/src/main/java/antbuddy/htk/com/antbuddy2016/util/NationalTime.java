package antbuddy.htk.com.antbuddy2016.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class NationalTime {

	public NationalTime() {
	}

	public static String getCurrentTime() {
		Calendar ctaq = Calendar.getInstance();
		SimpleDateFormat dfaq = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String crntdt = dfaq.format(ctaq.getTime());
		return crntdt;
	}
	public static long getlongTime() {
		Calendar ctaq = Calendar.getInstance();
		ctaq.setTimeZone(TimeZone.getTimeZone("UTC"));
		return ctaq.getTimeInMillis();
	}

	public static String getCurrentTimezone() {
		Calendar cll = Calendar.getInstance();
		TimeZone z = cll.getTimeZone();
		return z.toString();
	}

	/**
	 * Input: Format time "yyyy-MM-dd'T'HH:mm:ss"
	 * 
	 * @return
	 */
	public static String convertLocalTimeToUTCTime(String localTime) {
		Date date = convertDateInStringtoDate(localTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String utcTime = sdf.format(date.getTime());
		return utcTime;
	}
	/**
	 * Input: Format time "yyyy-MM-dd'T'HH:mm:ss"
	 *
	 * @return
	 */
	public static String getLocalTimeToUTCTime() {

		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String utcTime = sdf.format(date.getTime());
		return utcTime;
	}

	/**
	 * String dateInString = "yyyy-MM-dd'T'HH:mm:ss"
	 * 
	 * @param dateInString
	 * @return
	 */
	public static Date convertDateInStringtoDate(String dateInString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		Date date = null;
		try {
			date = sdf.parse(dateInString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String convertDateInLongtoDate(long dateInLong) {
		Date date = new Date(dateInLong);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		return sdf.format(date);
	}

	public static String convertUTCTimeToCurrentTime(String utcTimeString) {
		Date dateUTC = convertDateInStringtoDate(utcTimeString);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		// Convert UTC to Local Time
		Date localDate = new Date(dateUTC.getTime() + TimeZone.getDefault().getOffset(dateUTC.getTime()));
		return sdf.format(localDate.getTime());
	}
	
	public static long convertUTCTimeToCurrentTimeDateInLong(String utcTime) {
		//Date dateUTC = convertDateInStringtoDate(utcTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		Date dateUTC = null;
		try {
			dateUTC = sdf.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// Convert UTC to Local Time
		Date localDate = new Date(dateUTC.getTime() + TimeZone.getDefault().getOffset(dateUTC.getTime()));
		return localDate.getTime();
	}

	public static long convertUTCTimeToDateInLong(String utcTime) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		Date d;
		long milliseconds = 0;
		try {
			d = f.parse(utcTime);
		    milliseconds = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return milliseconds;
	}

}

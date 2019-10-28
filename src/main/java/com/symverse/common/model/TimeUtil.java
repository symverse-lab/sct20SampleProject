package com.symverse.common.model;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 재현
 *
 */
public class TimeUtil {
    /**
     *<pre>
     *날짜 문자열의 년월일 사이에 지정된 구분자를 넣어준다. 
     *</pre>
     * @param strDate
     * @param delim
     * @return
     */
    public static String addDateDelim(String strDate, String delim) {
    	String result = "";
    	
    	String yyyy = "";
    	String mm = "";
    	String dd = "";
    	
    	if(strDate == null || strDate.length() < 8) {
    		return strDate;
    	}
    	
    	if(strDate.length() == 8) {
    		yyyy = strDate.substring(0, 4);
    		mm = strDate.substring(4, 6);
    		dd = strDate.substring(6, 8);
    	} else {
    		return strDate;
    	}
    	
    	result = yyyy + delim + mm + delim + dd;
    	
    	
    	return result;
    }
    
	/**
	 *<pre>
	 *일자 또는 일시 문자열을 지정한 포맷으로 바꿔준다.(format 은 SimpleDateFormat에 준하여 작성)
	 *</pre>
	 * @param str
	 * @param format
	 * @return
	 */
	public static String makeDateFormat(String str, String format) {
		if(str == null) return "";
		
		String date = str.replaceAll("\\D", "");
		int year = 0;
		int month = 0;
		int day = 0;
		int hour = 0;
		int minute = 0;
		int second = 0;
		
		if(date.length() == 6) {
			year = Integer.parseInt(date.substring(0, 4));
			month = Integer.parseInt(date.substring(4, 6)) - 1;
			day = 1;
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day);
			return makeDateFormat(cal, format);
		} else if(date.length() == 8){
			year = Integer.parseInt(date.substring(0, 4));
			month = Integer.parseInt(date.substring(4, 6)) - 1;
			day = Integer.parseInt(date.substring(6, 8));
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day);
			return makeDateFormat(cal, format);
		} else if(date.length() == 12){
			year = Integer.parseInt(date.substring(0, 4));
			month = Integer.parseInt(date.substring(4, 6)) - 1;
			day = Integer.parseInt(date.substring(6, 8));
			hour = Integer.parseInt(date.substring(8, 10));
			minute = Integer.parseInt(date.substring(10, 12));
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day, hour, minute);
			return makeDateFormat(cal, format);
		} else if(date.length() == 14){
			year = Integer.parseInt(date.substring(0, 4));
			month = Integer.parseInt(date.substring(4, 6)) - 1;
			day = Integer.parseInt(date.substring(6, 8));
			hour = Integer.parseInt(date.substring(8, 10));
			minute = Integer.parseInt(date.substring(10, 12));
			second = Integer.parseInt(date.substring(12, 14));
			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day, hour, minute, second);
			return makeDateFormat(cal, format);
		}
		return str;
	}


    
    /**
     *<pre>
	 *Calendar 객체를 지정한 포맷으로 바꿔준다.(format 은 SimpleDateFormat에 준하여 작성)
     *</pre>
     * @param cal
     * @param format
     * @return
     */
    public static String makeDateFormat(Calendar cal, String format) {
    	return makeDateFormat(cal.getTime(), format);
    }

	/**
	 *<pre>
	 *Date 객체를 지정한 포맷으로 바꿔준다.(format 은 SimpleDateFormat에 준하여 작성)
	 *</pre>
	 * @param time
	 * @param format
	 * @return
	 */
	public static String makeDateFormat(Date time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}


	/**
	 *<pre>
	 *현재 시간을 지정한 포맷으로 바꿔준다.(format 은 SimpleDateFormat에 준하여 작성)
	 *</pre>
	 * @param time
	 * @param format
	 * @return
	 */
	public static String currentDateFormat(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(Calendar.getInstance().getTime());
	}
	
	/**
	 *<pre>
	 *Date 형식에 입력한 숫자 만큼 일자를 더해준다
	 *</pre>
	 * @param date
	 * @param cnt
	 * @return
	 */
	public static Date addDays(Date date, int cnt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, cnt);
		return cal.getTime();
	}

	/**
	 *<pre>
	 *Date 형식에 입력한 숫자 만큼 월을 더해준다
	 *</pre>
	 * @param date
	 * @param cnt
	 * @return
	 */
	public static Date addMonths(Date date, int cnt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, cnt);
		return cal.getTime();
	}
	
	/**
	 *<pre>
	 *Date 형식에 입력한 숫자 만큼 분을 더해준다
	 *</pre>
	 * @param date
	 * @param cnt
	 * @return
	 */
	public static Date addMinute(Date date, int cnt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, cnt);
		return cal.getTime();
	}
	
	
	public static String getServerTimeYYYYMMDDHHmmss() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
		String nowTime = dateFormat.format(date);
		return nowTime;
	}
	
	
	public static String getCurrentTimeMillis() {
		long nowTime = System.currentTimeMillis() / 1000;
		return Integer.toString((int) nowTime );
	}
	
	

}

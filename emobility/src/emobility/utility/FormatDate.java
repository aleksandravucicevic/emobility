package emobility.utility;

import java.util.*;

/**
 * Utility class for working with dates. 
 * Provides methods with useful functionality, such as truncating time from dates and comparing dates.
 * 
 * @author Aleksandra Vucicevic
 * @version 1.0
 */
public class FormatDate {
	
	/**
	 * Truncates the time part of a {@link Date} object, setting the hour, minute, second, and millisecond fields to zero.
	 * @param date date from which the time is to be truncated
	 * @return new {@code Date} object with the time set to midnight
	 */
	public static Date truncateTime(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}
	
	/**
	 * Checks if two dates fall on the same day, ignoring the time part.
	 * @param date1 the first date to compare
	 * @param date2 the second date to compare
	 * @return {@code true} if the two dates are on the same day, otherwise {@code false}
	 */
	public static boolean isSameDay(Date date1, Date date2){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * Extracts the time component from a {@code Date} object, setting the date part to January 1, 1970.
	 * @param dateTime date from which the day, month and year are to be reset
	 * @return {@code Date} object representing the specified time but on January 1, 1970
	 */
	public static Date getTimeOnlyCalendar(Date dateTime){
		Calendar timeOnlyCalendar = Calendar.getInstance();
		
		if(dateTime == null){
			timeOnlyCalendar.set(Calendar.YEAR, 1970);
	        timeOnlyCalendar.set(Calendar.MONTH, Calendar.JANUARY);
	        timeOnlyCalendar.set(Calendar.DAY_OF_MONTH, 1);
	        timeOnlyCalendar.set(Calendar.HOUR_OF_DAY, 0);
	        timeOnlyCalendar.set(Calendar.MINUTE, 0);
	        timeOnlyCalendar.set(Calendar.SECOND, 0);
	        timeOnlyCalendar.set(Calendar.MILLISECOND, 0);
		} else{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateTime);
			
		    timeOnlyCalendar.set(Calendar.YEAR, 1970);
		    timeOnlyCalendar.set(Calendar.MONTH, Calendar.JANUARY);
		    timeOnlyCalendar.set(Calendar.DAY_OF_MONTH, 1);

		    timeOnlyCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
		    timeOnlyCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
		    timeOnlyCalendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
		    timeOnlyCalendar.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND));
		}
		
	    return timeOnlyCalendar.getTime();
	}
}

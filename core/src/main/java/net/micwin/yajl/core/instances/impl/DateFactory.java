/**
 * 
 */
package net.micwin.yajl.core.instances.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Provides some useful methods to create and manipulate Date objects.
 * 
 * @author michael.winkler@micwin.net
 * 
 */
public class DateFactory {

    /**
         * Creates a Date object that points to the first millisecond of the
         * date.
         * 
         * @param date
         *                a date to mark a day
         * @return
         */
    public static Date getFirstMilli(Date date) {
	Calendar cal = GregorianCalendar.getInstance();
	cal.setTime(date);
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	return cal.getTime();
    }

    /**
         * Creates a Date object that points to the last millisecond of the
         * date.
         * 
         * @param date
         *                a date to mark a day
         * @return
         */
    public static Date getLastMilli(Date date) {
	Calendar cal = GregorianCalendar.getInstance();
	cal.setTime(date);

	// set time
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);

	// raise date
	cal.add(Calendar.DAY_OF_YEAR, 1);

	// ... and reduce by one millisecond.
	cal.add(Calendar.MILLISECOND, -1);

	// thats it!
	return cal.getTime();
    }

    /**
         * Creates a date that has the specified offset, applied from left to
         * right.
         * 
         * @param fromDate
         *                baseDate
         * @return
         */
    public static Date offset(Date fromDate, int years, int months, int days,
	    int hours, int minutes, int seconds, int millis) {

	// initialize
	Calendar cal = Calendar.getInstance();
	cal.setTime(fromDate);

	cal.add(Calendar.YEAR, years);
	cal.add(Calendar.MONTH, months);
	cal.add(Calendar.DAY_OF_MONTH, days);
	cal.add(Calendar.HOUR_OF_DAY, hours);
	cal.add(Calendar.MINUTE, minutes);
	cal.add(Calendar.SECOND, seconds);
	cal.add(Calendar.MILLISECOND, millis);
	return cal.getTime();
    }

    /**
         * Checks wether or not <code>date</code> is between
         * <code>fromDate</code> and <code>toDate</code>.
         * 
         * @param fromDate
         *                a start date. If <code>null</code>, then
         *                <code>date</code> is considered to be afterwards.
         * @param toDate
         *                a end date. If <code>null</code>, then
         *                <code>date</code> is considered to be before.
         * @param date
         *                A date to compare. If <code>null</code>, then
         *                <code>false</code> is returned.
         * @return <code>true</code> if <code>date</code> ist betqween
         *         <code>fromDate</code> and <code>toDate</code>.
         */
    public static boolean between(Date fromDate, Date toDate, Date date) {
	if (fromDate != null && toDate != null && toDate.before(fromDate)) {
	    throw new IllegalArgumentException("argument 'toDate' (" + toDate
		    + ") is before 'fromDate' (" + fromDate + ")");
	} else if (date == null) {
	    // null is never between anything
	    return false;
	} else if (fromDate != null && date.before(fromDate)) {
	    return false;
	} else if (toDate != null && date.after(toDate)) {
	    return false;
	} else {
	    return true;
	}
    }
}

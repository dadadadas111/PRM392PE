package com.example.prm392pe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for date and time operations
 */
public class DateTimeUtils {
    
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_DISPLAY = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_DISPLAY = "dd/MM/yyyy HH:mm";
    public static final String TIME_FORMAT_DEFAULT = "HH:mm:ss";
    public static final String TIME_FORMAT_DISPLAY = "HH:mm";
    
    /**
     * Get current date as string
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date());
    }
    
    /**
     * Get current timestamp
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
    
    /**
     * Format date to string
     */
    public static String formatDate(Date date, String format) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }
    
    /**
     * Parse string to date
     */
    public static Date parseDate(String dateString, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Convert timestamp to date string
     */
    public static String timestampToDateString(long timestamp, String format) {
        Date date = new Date(timestamp);
        return formatDate(date, format);
    }
    
    /**
     * Get difference between two dates in days
     */
    public static long getDaysBetween(Date startDate, Date endDate) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Get difference between two dates in hours
     */
    public static long getHoursBetween(Date startDate, Date endDate) {
        long diffInMillies = Math.abs(endDate.getTime() - startDate.getTime());
        return TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Add days to date
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }
    
    /**
     * Add hours to date
     */
    public static Date addHours(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }
    
    /**
     * Check if date is today
     */
    public static boolean isToday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        
        return today.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR) &&
               today.get(Calendar.DAY_OF_YEAR) == dateCalendar.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * Check if date is yesterday
     */
    public static boolean isYesterday(Date date) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        
        return yesterday.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR) &&
               yesterday.get(Calendar.DAY_OF_YEAR) == dateCalendar.get(Calendar.DAY_OF_YEAR);
    }
    
    /**
     * Get age from birth date
     */
    public static int getAge(Date birthDate) {
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthDate);
        Calendar now = Calendar.getInstance();
        
        int age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
        
        if (now.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        
        return age;
    }
    
    /**
     * Get time ago string (e.g., "2 hours ago", "3 days ago")
     */
    public static String getTimeAgo(Date date) {
        long timeAgo = System.currentTimeMillis() - date.getTime();
        
        if (timeAgo < TimeUnit.MINUTES.toMillis(1)) {
            return "Just now";
        } else if (timeAgo < TimeUnit.HOURS.toMillis(1)) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeAgo);
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else if (timeAgo < TimeUnit.DAYS.toMillis(1)) {
            long hours = TimeUnit.MILLISECONDS.toHours(timeAgo);
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (timeAgo < TimeUnit.DAYS.toMillis(7)) {
            long days = TimeUnit.MILLISECONDS.toDays(timeAgo);
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        } else {
            return formatDate(date, DATE_FORMAT_DISPLAY);
        }
    }
}

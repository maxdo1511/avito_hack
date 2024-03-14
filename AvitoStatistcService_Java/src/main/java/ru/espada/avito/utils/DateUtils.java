package ru.espada.avito.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static long convertDateToLongDDMMYYYY(String dateString) {
        try {
            return convertDateToLong(dateString, "dd.MM.yyyy");
        } catch (ParseException e) {
            throw new RuntimeException("Date " + dateString + " can't be parse!");
        }
    }

    public static String convertDateToStringDDMMYYYY(long date) {
        return convertDateToString(date, "dd.MM.yyyy");
    }

    public static String convertDateToStringDDMM(long date) {
        return convertDateToString(date, "dd.MM");
    }

    public static String convertDateToString(long date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date(date));
    }

    public static long convertDateToLong(String dateString, String pattern) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = dateFormat.parse(dateString);
        return date.getTime();
    }

    public static int getDeltaDays(long d1, long d2) {
        long delta = d2 - d1;
        if (delta < 0) {
            throw new IllegalArgumentException("d2 < d1");
        }
        return (int) (delta / (60 * 60 * 24 * 1000));
    }

    public static long getDays(long date) {
        return date / (60 * 60 * 24 * 1000);
    }

}

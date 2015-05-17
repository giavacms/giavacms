package org.giavacms.base.util;

import org.jboss.logging.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    static Logger logger = Logger.getLogger(DateUtils.class);
    static DateFormat dateFormat = new SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss.SSS");

    public static Date getDateFromString(String dateString) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = df.parse(dateString);
        return date;
    }

    public static Date getDateFromStringAndTimezome(String dateString,
                                                    String timezone) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        TimeZone timeZone = TimeZone.getTimeZone(timezone);
        df.setTimeZone(timeZone);
        Date date = df.parse(dateString);
        return date;
    }

    public static String getStringFromDate(Date data) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String date = df.format(data);
        return date;
    }

    public static String getStringFromDate(Date data, String timezone)
            throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone(timezone));
        return df.format(data);
    }

    public static String getPrecisionStringDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String date = df.format(new Date());
        return date;
    }

    public static List<String> getStringFromDates(List<Date> dates)
            throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        List<String> result = new ArrayList<>();
        for (Date date : dates) {
            String data = df.format(date);
            result.add(data);
        }
        return result;
    }

    public static Date toBeginOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        logger.debug(dateFormat.format(date) + " -- toBeginOfDay --> "
                + cal.getTime());
        return cal.getTime();
    }

    public static Date toEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        logger.debug(dateFormat.format(date) + " -- toEndOfDay ----> "
                + cal.getTime());
        return cal.getTime();
    }

    public static Date convertCalendar(final Calendar calendar,
                                       final TimeZone timeZone) {
        Calendar ret = new GregorianCalendar(timeZone);
        logger.info("data ms:" + calendar.getTimeInMillis());
        logger.info("diff tz desired ms:"
                + timeZone.getOffset(calendar.getTimeInMillis()));
        logger.info("diff tz deafult ms:"
                + TimeZone.getDefault().getOffset(calendar.getTimeInMillis()));

        ret.setTimeInMillis(calendar.getTimeInMillis()
                + timeZone.getOffset(calendar.getTimeInMillis())
                - TimeZone.getDefault().getOffset(calendar.getTimeInMillis()));
        return ret.getTime();
    }

    public static Date convertDate(Date date, final TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return convertCalendar(calendar, timeZone);
    }

    public static String toLocalDateAsString(String clientWhen, String clientTimezoneAsString, String clientDstAsString)
            throws Exception {
        TimeZone clientTimezone = TimeZone.getTimeZone(clientTimezoneAsString);
        boolean clientDst = clientTimezone.useDaylightTime();
        if (clientDstAsString != null) {
            clientDst = Boolean.parseBoolean(clientDstAsString);
        }

        TimeZone localTimezone = TimeZone.getDefault();
        boolean localDst = TimeZone.getDefault().useDaylightTime();

        // return getStringFromDate(
        // DateUtils.getDateFromStringAndTimezome(clientDateAsString, clientTimezone), localTimezone);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(clientTimezone);
        Date clientDate = df.parse(clientWhen);
        if (clientDst && !localDst) {
            Calendar clientCalendar = Calendar.getInstance();
            clientCalendar.setTime(clientDate);
            clientCalendar.add(Calendar.HOUR, 1);
            clientDate = clientCalendar.getTime();
        } else if (!clientDst && localDst) {
            Calendar clientCalendar = Calendar.getInstance();
            clientCalendar.setTime(clientDate);
            clientCalendar.add(Calendar.HOUR, -1);
            clientDate = clientCalendar.getTime();
        }

        df.setTimeZone(localTimezone);
        return df.format(clientDate);

    }
}

package cn.weijia.tradingmonitor.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期帮助类
 */
public class DateUtils {

    public static final String FORMATE_DATE_DAY = "yyyy-MM-dd";
    public static final String FORMATE_DATE_MONTH = "yyyy-MM";
    public static final String FORMATE_DATE_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMATE_YYYYMMDD = "yyyyMMddHHmmss";

    /**
     * date 2 String
     *
     * @param date
     * @return
     */
    public static String date2String(Date date, String formatStr) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String timestr = format.format(date);
        return timestr;
    }

    /**
     * String 2 date
     *
     * @param str
     * @return
     */
    public static Date string2Date(String str, String formatStr)  {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date localDate2Date(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 将date转换成LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate date2LocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }


    /**
     * 相差多少年
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public static Double getDiscrepantDays(Date dateStart, Date dateEnd) {
        if (dateStart == null || dateEnd == null) {
            return null;
        }
        int days = (int) (dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24;
        return (double) days / 365.0;
    }

    /**
     * 获取一段日期里的每一天
     *
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws ParseException
     */
    public static List<String> getEveryday(String dateStart, String dateEnd) throws ParseException {
        long dateStartTime = string2Date(dateStart, FORMATE_DATE_DAY).getTime();
        long dateEndTime = string2Date(dateEnd, FORMATE_DATE_DAY).getTime();
        List<String> dateStrList = new ArrayList<>();

        while (dateStartTime <= dateEndTime) {
            dateStrList.add(date2String(new Date(dateStartTime), FORMATE_DATE_DAY));
            dateStartTime = dateStartTime + 1000 * 60 * 60 * 24;
        }
        return dateStrList;
    }

    /**
     * 获取日期之前的几天或之后几天
     *
     * @param startDate
     * @param n         正后  负前
     * @return
     * @throws ParseException
     */
    public static String getAfterDate(String startDate, Long n) throws ParseException {
        long endDate = string2Date(startDate, FORMATE_DATE_DAY).getTime() + n * 1000 * 60 * 60 * 24;
        return date2String(new Date(endDate), FORMATE_DATE_DAY);
    }

    public static Boolean isToday(Date date) {
        String dateString = date2String(date, FORMATE_DATE_DAY);
        String dateNow = date2String(new Date(), FORMATE_DATE_DAY);
        return dateString.equals(dateNow);
    }

    /**
     * 获取日期之前的几月或之后几月
     *
     * @param startDate
     * @param n         正后  负前
     * @return
     * @throws ParseException
     */
    public static String getAfterDateMonth(String startDate, int n) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(string2Date(startDate, FORMATE_DATE_MONTH));
        calendar.add(Calendar.MONTH, n);
        return date2String(calendar.getTime(), FORMATE_DATE_MONTH);
    }

    /**
     * 获取日期之前的几月或之后几月
     *
     * @param startDate
     * @param n         正后  负前
     * @return
     * @throws ParseException
     */
    public static String getAfterDateMin(Date startDate, int n, String formate) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.MINUTE, n);
        return date2String(calendar.getTime(), formate);
    }

    public static String getAfterDate(Date startDate, int n, String formate) throws ParseException {
        long endDate = startDate.getTime() + n * 1000 * 60 * 60 * 24;
        return date2String(new Date(endDate), formate);

    }

    public static void main(String[] args) throws ParseException {
        getEveryday("2018-07-01", "2018-07-04");
        getAfterDate("2018-07-01", -1L);
        getAfterDateMonth("2018-06", 1);


        getAfterDate(new Date(), -2, FORMATE_DATE_DAY);
    }
}

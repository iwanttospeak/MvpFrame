package com.app.baselib.Utils;


import com.blankj.utilcode.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具
 * Created by liuli on 2015/11/27.
 */
public class DateUtil {

    //时间格式
    private static final String FORMAT_STR = "yyyy-MM-dd HH:mm";

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static Date parse(String strDate, String pattern) {

        if (StringUtils.isSpace(strDate)) {
            return null;
        }
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */

    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }
    /**
     * 使用用户格式解析对应格式的日期字符串得到毫秒值
     *
     * @param data    日期字符串
     * @param pattern 日期格式
     * @return
     */

    public static String getTimeMills(String data, String pattern) {
        String timeMills = "";
        if (data != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            timeMills = df.format(data);
        }
        return (timeMills);
    }

    /**
     * 使用用户格式解析毫秒值得到对应格式的日期字符串
     * @param timeMills    日期毫秒值
     * @param pattern 日期格式
     * @return
     */

    public static String getData(long timeMills, String pattern) {
        String returnValue = "";
        if (timeMills != 0) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            Date date = new Date(timeMills);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 转换时间毫秒值
     * 将服务器的秒为单位的值转成对应的毫秒值
     */
    public static long convertTimeMills(String timeMills){
        if (StringUtils.isSpace(timeMills)) return 0;
        if (timeMills.length() < 11){
            timeMills +="000";//转成一毫秒为单位
        }
     return Long.parseLong(timeMills);
    }

    /**
     * 根据服务器的时间返回默认的时间格式
     */
    public static String getDataFromServerTime(String timeMills){
        if (StringUtils.isSpace(timeMills)) return "";
        if (timeMills.length() < 11){
            timeMills +="000";//转成一毫秒为单位
        }
        return getData(Long.parseLong(timeMills),FORMAT_STR);
    }

    /**
     * 根据默认格式得到时间的小时和分钟
     */
    public static String getDataHM(String data){
        if (StringUtils.isSpace(data)) return "";
        String dataHM = "";
            String[] dataArray = data.split(" ");
            if (dataArray.length == 1){
                dataHM = dataArray[0];
            }
            if (dataArray.length == 2){
                dataHM = dataArray[1];
            }
            return dataHM;
    }

    /**
     * 获取聊天时间，默认格式是：
     * 如果是当日的，只显示小时和分钟  例如  10：10
     * 如果是昨天的，加个“昨天”  例如   昨天 10：10
     * 如果是其他日期，加个具体日期 例如  2016-06-28 10：10
     */
    public static String getChatTime(String data){
        if (StringUtils.isSpace(data)) return "";
        String[] dataArray = data.split(" ");
        try {
            if (IsToday(data)){//今天
                return getTodayTime(dataArray);
            }
            if (IsYesterday(data)){//昨天
                return "昨天" + getTodayTime(dataArray);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        //之前直接返回数值
        return data;
    }

    private static String getTodayTime(String[] dataArray ){
        String dataHM = "";
        if (dataArray.length == 1){
            dataHM = dataArray[0];
        }
        if (dataArray.length == 2){
            dataHM = dataArray[1];
        }
        return dataHM;
    }
    /**
     * 判断是否为今天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     *
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (DateLocal.get() == null) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();
}

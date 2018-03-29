package com.app.baselib.Utils;

import android.content.Context;
import android.icu.util.Calendar;

import com.app.baselib.constant.BaseConstants;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String工具类
 * Created by admin on 2016/9/20.
 */
public class StringUtil {


    /** 将本地的(包含七牛返回的)图片List转成字符串 **/
    public static String imgListToString(List<String> imageListUrl) {
        if (imageListUrl.size() > 0) {//fileURL已经删除
            String imgs = "";
            for (int i = 0; i < imageListUrl.size(); i++) {
                if (imageListUrl.get(i).contains(BaseConstants.IMG_URL_PREFIX)) {
                    String ss = imageListUrl.get(i).substring(BaseConstants.IMG_URL_PREFIX.length(), imageListUrl.get(i).length());
                    imgs += ss;
                } else {
                    imgs += imageListUrl.get(i);
                }
                if (i != imageListUrl.size() - 1) {//最后一个不添加逗号
                    imgs += ",";
                }
            }
            return imgs;
        }
        return "";
    }



    /** dp转化为px **/
    public static int dip2px(Context context, float paramFloat) {
        return (int) (0.5F + paramFloat * context.getResources().getDisplayMetrics().density);
    }


    /** 精确到后两位 **/
    public static String numberFormat(double num) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(num);
    }

    /**
     * double转成String,可能是一位
     */
    public static String doubleToStr(double number){
        DecimalFormat format = null;
        if(Double.valueOf(number).intValue() == number){
            format = new DecimalFormat("#");
        }else{
            format = new DecimalFormat("#.##");
        }

        String str = format.format(number);

        return str;
    }

    /**
     * double转成钱数String,固定保留两位小数
     */
    public static String doubleToMoney(double number){
        DecimalFormat format   = new DecimalFormat("######0.00");
        double exact  = 1e-8;
        if (number > -exact && number  < exact ){//在允许范围内，认定其值为0
            return "0.00";
        }
        String str = format.format(number);
        return str;
    }

    /**
     * 固定保留两位小数,添加万，亿等单位
     */
    public static String getMoney(double number){
        DecimalFormat format   = new DecimalFormat("######0.00");
        double exact  = 1e-8;
        if (number > -exact && number  < exact ){//在允许范围内，认定其值为0
            return "0.00";
        }
        double newNumber = number;
        if (number >= 10000000000L){
            newNumber = number/100000000;
            String str = format.format(newNumber);
            return str+"亿";
        }
        if (number >= 100000000){//过亿显示万
            newNumber = number/10000;
            String str = format.format(newNumber);
            return str+"万";
        }
        String str = format.format(newNumber);
        return str;
    }

    /** 替换* 传入前面保留的长度 和后缀保留长度 **/
    public static String replaceByStar(String str, int behind, int last) {
        StringBuffer sb = new StringBuffer();
        StringBuffer strSb = new StringBuffer(str);
        for (int i = 0; i < str.length() - behind - last; i++) {
            sb.append("*");
        }
        return strSb.replace(behind, str.length() - last, sb.toString()).toString();
    }
    /**
     * 判断身份证格式
     * @param idNum 省份证号码
     * @return true 或者FALSE
     */
    public static boolean isIdNum(String idNum) {

        // 中国公民身份证格式：长度为15或18位，最后一位可以为字母
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");

        // 格式验证
        if (!idNumPattern.matcher(idNum).matches())
            return false;

        // 合法性验证

        int year = 0;
        int month = 0;
        int day = 0;

        if (idNum.length() == 15) {

            // 一代身份证

            System.out.println("一代身份证：" + idNum);

            // 提取身份证上的前6位以及出生年月日
            Pattern birthDatePattern = Pattern.compile("\\d{6}(\\d{2})(\\d{2})(\\d{2}).*");

            Matcher birthDateMather = birthDatePattern.matcher(idNum);

            if (birthDateMather.find()) {

                year = Integer.valueOf("19" + birthDateMather.group(1));
                month = Integer.valueOf(birthDateMather.group(2));
                day = Integer.valueOf(birthDateMather.group(3));

            }

        } else if (idNum.length() == 18) {

            // 二代身份证

            System.out.println("二代身份证：" + idNum);
            // 提取身份证上的前6位以及出生年月日
            Pattern birthDatePattern = Pattern.compile("\\d{6}(\\d{4})(\\d{2})(\\d{2}).*");

            Matcher birthDateMather = birthDatePattern.matcher(idNum);

            if (birthDateMather.find()) {

                year = Integer.valueOf(birthDateMather.group(1));
                month = Integer.valueOf(birthDateMather.group(2));
                day = Integer.valueOf(birthDateMather.group(3));
            }
        }
        // 年份判断，100年前至今
        Calendar cal = null;
        java.util.Calendar calendar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            cal = Calendar.getInstance();
        }else {
            calendar = java.util.Calendar.getInstance();
        }
        // 当前年份
        int currentYear = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            currentYear = cal.get(Calendar.YEAR);
        }else {
            currentYear = calendar.get(java.util.Calendar.YEAR);
        }
        if (year <= currentYear - 100 || year > currentYear)
            return false;
        // 月份判断
        if (month < 1 || month > 12)
            return false;
        // 日期判断
        // 计算月份天数
        int dayCount = 31;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                dayCount = 31;
                break;
            case 2:
                // 2月份判断是否为闰年
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    dayCount = 29;
                    break;
                } else {
                    dayCount = 28;
                    break;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                dayCount = 30;
                break;
        }
        if (day < 1 || day > dayCount)
            return false;
        return true;
    }

    /**
     * 添加字符串到现有字符串
     */
    public static String join(List<String> list, String seperator){
        if(seperator == null){
            seperator = ",";
        }
        StringBuilder builder = new StringBuilder();
        if(list != null && list.size() > 0){
            for(String s : list){
                builder.append(s + seperator);
            }
        }
        if(builder.length() > 0){
            int start = builder.length() - seperator.length() ;
            builder.delete(start , builder.length() );
        }

        return builder.toString();

    }

}

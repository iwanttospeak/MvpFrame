package com.app.baselib.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;

import com.blankj.utilcode.util.StringUtils;

/**
 * 图片工具类:处理头像，图片格式转换等
 */
public class BitmapUtil {

    private static final String PATTER_WIDTH = "#{width}";  //匹配宽格式
    private static final String PATTER_HEIGHT = "#{height}";    //匹配高格式
    private static final String LARGE = "1200";   //大图
    private static final String THUMBNAIL = "400";   //缩略图
    private static final String HEAD_PORTRAIT = "180";   //头像


    /** 连续平铺图片 **/
    public static Bitmap repeaterBitmap(int number, Bitmap src) {
        int width1 = src.getWidth() * number;
        int height  = src.getHeight();

        if (height <= 0 || width1<= 0){
            Bitmap bitmap1 = Bitmap.createBitmap(1,1, Bitmap.Config.ARGB_4444);
            return bitmap1;
        }
        Bitmap bitmap = Bitmap.createBitmap(width1,
                height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        for (int i = 0; i < number; i++) {
            canvas.drawBitmap(src, src.getWidth() * i, 0, null);
        }
        //回收原图
        if (recycleBitmap(src)) {
            src = null;
        }
        return bitmap;
    }


    /**
     *  平铺图片(空心，实心)
     * @param bmp1 实心
     * @param bmp2 空心
     */
    public static Bitmap repeaterBitmap(int level, Bitmap bmp1, Bitmap bmp2) {
        Bitmap bitmap1 = Bitmap.createBitmap(bmp1.getWidth() * 5,
                bmp1.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas1 = new Canvas(bitmap1);

        for (int i = 0; i < 5; i++) {
            if (i > level) {
                canvas1.drawBitmap(bmp2, bmp2.getWidth() * i, 0, null);
            } else {
                canvas1.drawBitmap(bmp1, bmp1.getWidth() * i, 0, null);
            }
        }

        //回收原图
        if (recycleBitmap(bmp1)) {
            bmp1 = null;
        }
        if (recycleBitmap(bmp2)) {
            bmp2 = null;
        }
        return bitmap1;
    }

    /** 获取图片Url地址 **/
    public static String getBitmapUrl(String url, boolean isThumbnail){
        if (TextUtils.isEmpty(url)) return url;
        String resultUrl;
        if (isThumbnail){
            resultUrl = url.replace(PATTER_WIDTH, THUMBNAIL)
                    .replace(PATTER_HEIGHT, THUMBNAIL);
        } else {
            resultUrl = url.replace(PATTER_WIDTH, LARGE)
                    .replace(PATTER_HEIGHT, LARGE);
        }
        return resultUrl;
    }
    /** 获取头像图片Url地址 **/
    public static String getHeadPortraitUrl(String url, String width , String height){
        if (TextUtils.isEmpty(url)) return url;
        String resultUrl;
        if (!StringUtils.isSpace(width) && !StringUtils.isSpace(height)){
            resultUrl = url.replace(PATTER_WIDTH, width)
                    .replace(PATTER_HEIGHT, height);
        } else {
            resultUrl = url.replace(PATTER_WIDTH, HEAD_PORTRAIT)
                    .replace(PATTER_HEIGHT, HEAD_PORTRAIT);
        }
        return resultUrl;
    }

    /** 回收bitmap(在调用后若返回的是true,建议将此bitmap设置为null) **/
    public static boolean recycleBitmap(Bitmap bitmap) {
//        if (bitmap != null) {
//            if (!bitmap.isRecycled()) {
//                bitmap.recycle();
//            }
//            System.gc();
//            return true;
//        }
        return false;
    }

}

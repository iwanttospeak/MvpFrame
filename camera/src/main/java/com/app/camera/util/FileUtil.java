package com.app.camera.util;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * =====================================
 * 作    者: 陈嘉桐
 * 版    本：1.1.4
 * 创建日期：2017/4/25
 * 描    述：
 * =====================================
 */
public class FileUtil {
    private static final String TAG = "CJT";
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static String DST_FOLDER_NAME = "JCamera";

    private static String initPath() {
        if (storagePath.equals("")) {
            storagePath = parentPath.getAbsolutePath() + File.separator + DST_FOLDER_NAME;
            File f = new File(storagePath);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        return storagePath;
    }

    public static String saveBitmap(String dir, Bitmap b) {
        DST_FOLDER_NAME = dir;
        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + File.separator + "picture_" + dataTake + ".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return jpegName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 自定义图片保存路径
     */
    public static String saveBitmapWithPath(String imgPath, Bitmap b) {
        try {
            FileOutputStream fout = new FileOutputStream(imgPath);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return imgPath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean deleteFile(String url) {
        boolean result = false;
        File file = new File(url);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     *  获取视频时长
     *  格式 11:70 （11秒700毫秒）
     */
    public static String getDurationTime(String path){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }//
        int duration = mediaPlayer.getDuration();
        StringBuilder builder = new StringBuilder(duration/1000);//秒
        builder.append(":");
        builder.append((duration%1000)/100);
        return builder.toString();
    }

    /**
     *  获取视频时长
     *  格式 11:70 （11秒700毫秒）
     */
    public static String getDurationTime(int time){
        String timeTxt;
        int mi = (time%1000)/10;
        if (mi<10){
            timeTxt = ""+ time/1000 + ":" + "0"+ mi ;
        }else {
            timeTxt = ""+ time/1000 + ":" + mi ;
        }
        return timeTxt;
    }

}

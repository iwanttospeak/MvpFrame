package com.app.baselib.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.app.baselib.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;


/**
 * image显示类，负责本地和网络图片的加载显示
 * Created by shen on 2017/5/9.
 */

public class ImageDisplayUtil {



    private static RequestOptions defaultRequestOptions;

    private static RequestOptions requestOptions(){
            if (defaultRequestOptions == null){
                defaultRequestOptions = new RequestOptions()
                        .placeholder(R.drawable.base_default_image_square)
                        .transform(new FitCenter());
            }
            return defaultRequestOptions;
        }

    private static RequestOptions requestOptions(Drawable placeholder){
        return new RequestOptions()
                    .placeholder(placeholder)
                    .transform(new FitCenter());
    }
    /**
     * 获取bitmap.
     *
     * @param url                    地址
     * @param onGetUrlBitmapListener 获取成功回调
     */
    public static void getImageBitmap(Context context, String url, @NonNull OnGetUrlBitmapListener onGetUrlBitmapListener) {
        Glide.with(context).asBitmap().load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        onGetUrlBitmapListener.getBitmap(resource);
                    }
                });
    }
    /**
     * 显示图片
     */
    public static void displayImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).apply(requestOptions()).into(imageView);
    }
    /**
     * 显示图片
     */
    public static void displayImage(ImageView imageView, int imgResource) {
        Glide.with(imageView.getContext()).load(imgResource).apply(requestOptions()).into(imageView);
    }
    /**
     * 显示头像
     */
    public static void displayHeadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).apply(requestOptions(ContextCompat.getDrawable(imageView.getContext(),R.drawable.base_default_image_square))).into(imageView);
    }
    /**
     * 可以设置默认图片
     */
    public static void displayImageWithDefault(ImageView imageView, String url, Drawable default_image) {
        Glide.with(imageView.getContext()).load(url).apply(requestOptions(default_image)).into(imageView);
    }


    /**
     * 轮播图.
     */
    public static void displayBanner(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    /**
     * 压缩
     */
    public static Bitmap compressBitmap(Bitmap bmp) {
        // 首先进行一次大范围的压缩
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        float zoom = (float)Math.sqrt(32 * 1024 / (float)output.toByteArray().length); //获取缩放比例
        // 设置矩阵数据
        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);
        // 根据矩阵数据进行新bitmap的创建
        Bitmap resultBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        output.reset();
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        // 如果进行了上面的压缩后，依旧大于300K，就进行小范围的微调压缩
        while(output.toByteArray().length > 30 * 1024){
            matrix.setScale(0.9f, 0.9f);//每次缩小 1/10
            resultBitmap = Bitmap.createBitmap(
                    resultBitmap, 0, 0,
                    resultBitmap.getWidth(), resultBitmap.getHeight(), matrix,true);

            output.reset();
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        }
        return resultBitmap;
    }

    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    //根据url获取bitmap
    public interface OnGetUrlBitmapListener {
        void getBitmap(Bitmap bitmap);
    }
}

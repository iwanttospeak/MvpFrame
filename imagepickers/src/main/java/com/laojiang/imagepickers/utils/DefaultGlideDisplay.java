package com.laojiang.imagepickers.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.laojiang.imagepickers.R;

/**
 * Glide实现的图片加载器
 */
public class DefaultGlideDisplay implements IImageDisplay {


    private static RequestOptions requestOptions(int placeholder){
        return new RequestOptions()
                .placeholder(placeholder)
                .error(placeholder)
                .transform(new FitCenter());
    }

    /**
     * 设置最大最小值和占位符
     */
    private static RequestOptions requestOptions(int placeholder,int maxWidth, int maxHeight){
        return new RequestOptions()
                .placeholder(placeholder)
                .error(placeholder)
                .override(maxWidth,maxHeight)
                .transform(new FitCenter());
    }
    private static RequestOptions requestOptions(int maxWidth, int maxHeight){
        return new RequestOptions()
                .override(maxWidth,maxHeight)
                .transform(new FitCenter());
    }
    /**
     * 显示图片
     */
    public static void displayImage(Context context,ImageView imageView, Object url, int placeHolder) {
        Glide.with(context)
                .load(url)
                .thumbnail(0.1f)
                .apply(requestOptions(placeHolder))
                .into(imageView);
    }

    @Override
    public void display(Context context, String url, ImageView imageView, int maxWidth, int maxHeight) {
        Glide.with(context)
                .load(url)
                .apply(requestOptions(maxWidth,maxHeight))
                .into(imageView);
    }

    @Override
    public void display(Context context, String url, ImageView imageView, int placeHolder, int errorHolder, int maxWidth, int maxHeight) {
        Glide.with(context)
                .load(url)
                .apply(requestOptions(placeHolder,maxWidth,maxHeight))
                .into(imageView);
    }

    @Override
    public void display(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }
}

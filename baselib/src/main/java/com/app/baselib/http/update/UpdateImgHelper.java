package com.app.baselib.http.update;

import android.content.Context;

import com.app.baselib.http.bean.UpdateImgResultBean;
import com.app.baselib.mvp.BaseView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by Wang on 2017/12/15.
 * 上传图片帮助类，简化上传图片操作
 */

public class UpdateImgHelper {


    //本地对应的图片文件
    private File photoFile;

    private BaseView mBaseView;
    private Context context;

    //是否上传图片完成,防止多次处理图片以及影响后续操作
    private boolean isUpdateImgOver = false;


    public UpdateImgHelper(BaseView baseView, Context context) {
        mBaseView = baseView;
        this.context = context;
    }
    /**
     * 上传单张图片到七牛
     */
    public void updateImage(String picUrl, OnSuccessListener onSuccessListener, boolean showLoading) {
        if (StringUtils.isSpace(picUrl)) return;
        if (showLoading) mBaseView.showLoading();
        photoFile = new File(picUrl);
        LogUtils.e("path", "path:" +  photoFile.getAbsolutePath());
        ArrayList<File> imgFiles = new ArrayList<>();
        imgFiles.add(photoFile);
        new UploadImage(imgFiles).networkError(() -> {
            if (showLoading) mBaseView.hideLoading();
        }).upload(new UploadImage.SuccessListener() {
            @Override
            public void onSuccess(boolean isSuccess, List<String> result, String error) {
                if (isSuccess) {
                    if (showLoading) mBaseView.hideLoading();
                    onSuccessListener.onSuccess(result);
                } else {
                    if (showLoading) mBaseView.hideLoading();
                    ToastUtils.showShort("上传图片失败");
                }
            }
        },context);
    }

    /**
     * 上传多张图片到七牛
     */
    public void updateImageList(List<String> picList, OnSuccessListener onSuccessListener, boolean showLoading) {
        if (picList == null || picList.size()==0) return;
        if (showLoading) mBaseView.showLoading();
        ArrayList<File> imgFiles = new ArrayList<>();
        for (int i = 0;i < picList.size();i++){
            //对应的图片文件
            File photoFile = new File(picList.get(i));
            LogUtils.e("path", "path:" +  photoFile.getAbsolutePath());
            imgFiles.add(photoFile);
        }
        new UploadImage(imgFiles)
                .networkError(() -> {
                    if (showLoading) mBaseView.hideLoading();
                    ToastUtils.showShort("上传图片失败");
                })
                .upload(new UploadImage.SuccessListener() {
                    @Override
                    public void onSuccess(boolean isSuccess, List<String> result, String error) {
                        if (showLoading) mBaseView.hideLoading();
                        if (isSuccess) {
                            if (!isUpdateImgOver) {
                                onSuccessListener.onSuccess(result);
                                isUpdateImgOver = true;
                            }
                        } else {
                            ToastUtils.showShort("上传图片失败");
                        }
                    }
                }, context);
    }
    /**
     * 上传多张图片到七牛,自行处理结果
     */
    public void updateImageListHandleResult(List<String> picList, OnSuccessHandlerListener onSuccessListener, boolean showLoading) {
        if (picList == null || picList.size()==0) return;
        if (showLoading) mBaseView.showLoading();
        ArrayList<File> imgFiles = new ArrayList<>();
        for (int i = 0;i < picList.size();i++){
            //对应的图片文件
            File photoFile = new File(picList.get(i));
            LogUtils.e("path", "path:" +  photoFile.getAbsolutePath());
            imgFiles.add(photoFile);
        }
        new UploadImage(imgFiles)
                .networkError(() -> {
                    if (showLoading) mBaseView.hideLoading();
                    ToastUtils.showShort("上传图片失败");
                })
                .upload(new UploadImage.SuccessResultListener() {
                    @Override
                    public void onSuccess(boolean isSuccess, List<UpdateImgResultBean> result, String error) {
                        if (showLoading) mBaseView.hideLoading();
                        if (isSuccess) {
                            if (!isUpdateImgOver) {
                                onSuccessListener.onSuccess(result);
                                isUpdateImgOver = true;
                            }
                        } else {
                            ToastUtils.showShort("上传图片失败");
                        }
                    }
                }, context);
    }

    public interface OnSuccessListener {
        void onSuccess(List<String> imgUrlList);
    }
    public interface OnSuccessHandlerListener {
        void onSuccess(List<UpdateImgResultBean> result);
    }
    public interface ErrorListener{
        void onError();
    }

}

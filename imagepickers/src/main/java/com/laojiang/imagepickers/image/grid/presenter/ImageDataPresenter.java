package com.laojiang.imagepickers.image.grid.presenter;

import android.content.Context;
import android.media.ExifInterface;
import android.util.Log;

import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.data.ImageContants;
import com.laojiang.imagepickers.data.MediaDataBean;
import com.laojiang.imagepickers.data.ImageDataModel;
import com.laojiang.imagepickers.data.ImageFolderBean;
import com.laojiang.imagepickers.data.PickerOptions;
import com.laojiang.imagepickers.image.grid.view.IImageDataView;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  ImageDataActivity的Presenter层
 */
public class ImageDataPresenter {
    private IImageDataView mViewImpl;
    private ExecutorService mCachedThreadService = Executors.newCachedThreadPool();
    private PickerOptions mOptions;
    public ImageDataPresenter(PickerOptions mOptions, IImageDataView view) {
        this.mOptions = mOptions;
        this.mViewImpl = view;
    }
    /**
     * 扫描本地图片
     */
    public void scanData(final Context context) {
        addNewRunnable(new Runnable() {

            private boolean success;

            @Override
            public void run() {
                mViewImpl.showLoading();
                if (mOptions.isNeedVideo() && !mOptions.isNeedImage()){//需要视频不需要图片
                    success = ImageDataModel.getInstance().scanAllDataNoImage(context);
                }else if (mOptions.isNeedVideo() && mOptions.isNeedImage()){//需要视频，需要图片
                    success = ImageDataModel.getInstance().scanAllData(context);
                }else {//不需要视频
                    success = ImageDataModel.getInstance().scanAllDataNoVideo(context);
                }

                mViewImpl.hideLoading();
                if (!success)
                    mViewImpl.showShortToast(R.string.error_imagepicker_scanfail);
                mViewImpl.onFolderChanged(ImageDataModel.getInstance().getAllFloderList().get(0));
            }
        });
    }

    /**
     * 切换文件夹
     * @param folderBean 文件夹对象
     */
    public void checkDataByFloder(final ImageFolderBean folderBean) {
        addNewRunnable(new Runnable() {
            @Override
            public void run() {
                mViewImpl.onDataChanged(ImageDataModel.getInstance().getImagesByFloder(folderBean));
            }
        });
    }
    /**
     * 根据新图片路径创建ImageBean
     *
     * @param path 新图片路径
     * @return MediaDataBean
     */
    public MediaDataBean getImageBeanByPath(String path) {
        if (path == null || path.length() == 0)
            return null;

        try {
            File file = new File(path);

            MediaDataBean mediaDataBean = new MediaDataBean();
            mediaDataBean.setMediaPath(path);
            mediaDataBean.setLastModified(Long.valueOf(file.lastModified()));
            ExifInterface exifInterface = new ExifInterface(path);
            int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
            int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
            mediaDataBean.setWidth(width);
            mediaDataBean.setHeight(height);
            return mediaDataBean;
        } catch (Exception e) {
            Log.e("ImagePicker", "ImageDataPresenter.getImageBeanByPath()--->" + e.toString());
        }

        return null;
    }

    //将子线程放到线程池中
    private void addNewRunnable(Runnable runnable) {
        mCachedThreadService.execute(runnable);
    }

    /**
     * 释放资源
     */
    public void onDestory() {
        ImageContants.CURRENT_SELECTED_TYPE = -1;
        ImageDataModel.getInstance().clear();
        mViewImpl = null;
    }
}

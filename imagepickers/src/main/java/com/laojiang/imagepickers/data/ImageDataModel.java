package com.laojiang.imagepickers.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.utils.CommonUtils;
import com.laojiang.imagepickers.utils.DefaultGlideDisplay;
import com.laojiang.imagepickers.utils.IImageDisplay;
import com.laojiang.imagepickers.utils.ImageComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 图片数据层
 */
public class ImageDataModel {
    private ImageDataModel() {
    }

    private static final class ImageDataModelHolder {
        private static final ImageDataModel instance = new ImageDataModel();
    }

    public static ImageDataModel getInstance() {
        return ImageDataModelHolder.instance;
    }

    //所有图片
    private List<MediaDataBean> mAllImgList = new ArrayList<>();
    //所有视频
    private List<MediaDataBean> mAllVideoList = new ArrayList<>();

    //所有文件夹List
    private List<ImageFolderBean> mAllFloderList = new ArrayList<>();

    //选中的图片List
    private List<MediaDataBean> mResultImageList = new ArrayList<>();
    //选中的视频list
    private List<MediaDataBean> mResultVideoList = new ArrayList<>();


    //图片显示器
    private IImageDisplay mDisplayer;

    /**
     * 获取图片加载器对象
     *
     * @return 如果未设置则默认为GlideImagePickerDisplayer
     */
    public IImageDisplay getDisplayer() {
        return mDisplayer != null ? mDisplayer : (mDisplayer = new DefaultGlideDisplay());
    }

    public List<MediaDataBean> getmResultImageList() {
        return mResultImageList;
    }

    public void setmResultImageList(List<MediaDataBean> mResultImageList) {
        this.mResultImageList = mResultImageList;
    }

    public void setmResultVideoList(List<MediaDataBean> mResultVideoList) {
        this.mResultVideoList = mResultVideoList;
    }
    /**
     * 设置图片加载器对象
     *
     * @param displayer 需要实现IImagePickerDisplayer接口
     */
    public void setDisplayer(IImageDisplay displayer) {
        this.mDisplayer = displayer;
    }

    /**
     * 获取所有图片数据List
     */
    public List<MediaDataBean> getAllImgList() {
        return mAllImgList;
    }

    /**
     * 获取所有视频数据List
     * @return
     */
    public List<MediaDataBean> getAllVideoList() {
        return mAllVideoList;
    }

    /**
     * 获取所有文件夹数据List
     */
    public List<ImageFolderBean> getAllFloderList() {
        return mAllFloderList;
    }

    /**
     * 获取选中的video数据
     */
    public List<MediaDataBean> getmResultVideoList() {
        return mResultVideoList;
    }
    /**
     * 获取选中的video数据
     */
    public List<MediaDataBean> getResultList() {
        if (ImageContants.CURRENT_SELECTED_TYPE== ImageContants.SELECTED_TYPE_IMAGE){
                return mResultImageList;
        }
        if (ImageContants.CURRENT_SELECTED_TYPE== ImageContants.SELECTED_TYPE_VIDEO){
                return mResultVideoList;
        }else {
            return null;
        }
    }
    /**
     * 添加新选中图片或者视频到结果中
     */
    public boolean addDataToResult(MediaDataBean mediaDataBean) {
        if (mediaDataBean.getType() == ImageContants.SELECTED_TYPE_IMAGE){
            if (mResultImageList != null)
                return mResultImageList.add(mediaDataBean);
        }
        if (mediaDataBean.getType() == ImageContants.SELECTED_TYPE_VIDEO){
            if (mResultVideoList != null)
                return mResultVideoList.add(mediaDataBean);
        }
        return false;
    }
    /**
     * 移除已选中的某图片或者视频
     */
    public boolean delDataFromResult(MediaDataBean mediaDataBean) {
        if (mediaDataBean.getType() == ImageContants.SELECTED_TYPE_IMAGE){
            if (mResultImageList != null)
                return mResultImageList.remove(mediaDataBean);
        }
        if (mediaDataBean.getType() == ImageContants.SELECTED_TYPE_VIDEO){
            if (mResultVideoList != null)
                return mResultVideoList.remove(mediaDataBean);
        }
        return false;
    }

    /**
     * 判断是否已选中某张图或者视频
     */
    public boolean hasDataInResult(MediaDataBean mediaDataBean) {
        if (mediaDataBean.getType() == ImageContants.SELECTED_TYPE_IMAGE){
            if (mResultImageList != null)
                return mResultImageList.contains(mediaDataBean);
        }
        if (mediaDataBean.getType() == ImageContants.SELECTED_TYPE_VIDEO){
            if (mResultVideoList != null)
                return mResultVideoList.contains(mediaDataBean);
        }
        return false;
    }

    /**
     * 判断是否已选中某张图或者视频
     */
    public boolean hasDataInResult(MediaDataBean mediaDataBean,String imageId) {
        if (mediaDataBean.getType() == ImageContants.SELECTED_TYPE_IMAGE){
            if (mResultImageList != null){
                for (MediaDataBean bean : mResultImageList){
                    if (bean.getMediaId().equals(imageId)){
                        return true;
                    }
                }
                return false;
            }
        }
        if (mediaDataBean.getType() == ImageContants.SELECTED_TYPE_VIDEO){
            if (mResultVideoList != null){
                for (MediaDataBean bean : mResultVideoList) {
                    if (bean.getMediaId().equals(imageId)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    /**
     * 获取已选中的图片数量
     */
    public int getResultNum() {
        return mResultImageList != null ? mResultImageList.size() : 0;
    }
    /**
     * 检查选择数据类型,保证数据类型的单一
     */
    public boolean checkSelectedDataType(int type) {
        if (ImageContants.CURRENT_SELECTED_TYPE == -1){
            ImageContants.CURRENT_SELECTED_TYPE  = type;
        }else {//已选择类型
            if (type !=ImageContants.CURRENT_SELECTED_TYPE){//已选择类型和当前选中类型不同
                ToastUtils.showShort(R.string.image_picker_no_selected_image_and_video);
                return false;
            }
        }
        return true;
    }
    /**
     * 检查选择数据是否为null
     */
    public void checkSelectedDataIsNull() {
        if (mResultVideoList.size() == 0
                && mResultImageList.size() == 0) {
            ImageContants.CURRENT_SELECTED_TYPE = -1;
        }
    }

    /**
     * 扫描图片数据
     * 需要视频
     * @param c context
     * @return 成功或失败
     */
    public boolean scanAllData(Context c) {
        try {
            Context context = c.getApplicationContext();
            //清空容器
            if (mAllImgList == null)
                mAllImgList = new ArrayList<>();
            if (mAllVideoList==null)
                mAllVideoList = new ArrayList<>();
            if (mAllFloderList == null)
                mAllFloderList = new ArrayList<>();
            if (mResultImageList == null)
                mResultImageList = new ArrayList<>();
            if (mResultVideoList==null)
                mResultVideoList = new ArrayList<>();
            mAllImgList.clear();
            mAllVideoList.clear();
            mAllFloderList.clear();
            mResultImageList.clear();
            mResultVideoList.clear();
            //创建“全部图片”的文件夹
            ImageFolderBean allImgFloder = new ImageFolderBean(
                    com.laojiang.imagepickers.data.ImageContants.ID_ALL_IMAGE_FLODER, context.getResources().getString(R.string.imagepicker_all_image_floder));

            ImageFolderBean allVideoFloder = new ImageFolderBean(
                    com.laojiang.imagepickers.data.ImageContants.ID_ALL_VIDEO_FLODER, context.getResources().getString(R.string.imagepicker_all_video_floder));
            mAllFloderList.add(allImgFloder);
            mAllFloderList.add(allVideoFloder);
            //临时存储所有文件夹对象的Map
            ArrayMap<String, ImageFolderBean> floderMap = new ArrayMap();

            //索引字段
            String columns[] =
                    new String[]{MediaStore.Images.Media._ID,//照片id
                            MediaStore.Images.Media.BUCKET_ID,//所属文件夹id
                            //                        MediaStore.Images.Media.PICASA_ID,
                            MediaStore.Images.Media.DATA,//图片地址
                            MediaStore.Images.Media.WIDTH,//图片宽度
                            MediaStore.Images.Media.HEIGHT,//图片高度
                            //                        MediaStore.Images.Media.DISPLAY_NAME,//图片全名，带后缀
                            //                        MediaStore.Images.Media.TITLE,
                            //                        MediaStore.Images.Media.DATE_ADDED,//创建时间？
                            MediaStore.Images.Media.DATE_MODIFIED,//最后修改时间
                            //                        MediaStore.Images.Media.DATE_TAKEN,
                            //                        MediaStore.Images.Media.SIZE,//图片文件大小
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,//所属文件夹名字
                    };


            //得到一个游标
            ContentResolver cr = context.getContentResolver();
            cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
            Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);

            if (cur != null && cur.moveToFirst()) {
                //图片总数
                allImgFloder.setNum(cur.getCount());
                allImgFloder.setFloderType(0);
                // 获取指定列的索引
                int imageIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int imagePathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int imageModifyIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
                int imageWidthIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);
                int imageHeightIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
                int floderIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                int floderNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);


                do {
                    String imageId = cur.getString(imageIDIndex);
                    String imagePath = cur.getString(imagePathIndex);
                    String lastModify = cur.getString(imageModifyIndex);
                    String width = cur.getString(imageWidthIndex);
                    String height = cur.getString(imageHeightIndex);
                    String floderId = cur.getString(floderIdIndex);
                    String floderName = cur.getString(floderNameIndex);
                    //                    Log.e("ImagePicker", "imageId=" + imageId + "\n"
                    //                            + "imagePath=" + imagePath + "\n"
                    //                            + "lastModify=" + lastModify + "\n"
                    //                            + "width=" + width + "\n"
                    //                            + "height=" + height + "\n"
                    //                            + "floderId=" + floderId + "\n"
                    //                            + "floderName=" + floderName);

                    if (new File(imagePath).exists()) {
                        //创建图片对象
                        MediaDataBean mediaDataBean = new MediaDataBean();
                        mediaDataBean.setMediaId(imageId);
                        mediaDataBean.setMediaPath(imagePath);
                        mediaDataBean.setLastModified(CommonUtils.isNotEmpty(lastModify) ? Long.valueOf(lastModify) : 0);
                        mediaDataBean.setWidth(CommonUtils.isNotEmpty(width) ? Integer.valueOf(width) : 0);
                        mediaDataBean.setHeight(CommonUtils.isNotEmpty(height) ? Integer.valueOf(height) : 0);
                        mediaDataBean.setFloderId(floderId);
                        mediaDataBean.setType(0);//图片的类型
                        mAllImgList.add(mediaDataBean);
                        //更新文件夹对象
                        ImageFolderBean floderBean = null;
                        if (floderMap.containsKey(floderId))
                            floderBean = floderMap.get(floderId);
                        else
                            floderBean = new ImageFolderBean(floderId, floderName);
                        floderBean.setFirstImgPath(imagePath);
                        floderBean.gainNum();
                        floderMap.put(floderId, floderBean);
                    }

                } while (cur.moveToNext());
                cur.close();
            }
            /**
             * 获取视频
             */
            //索引字段
            String columnsVideo[] =
                    new String[]{MediaStore.Video.Media._ID,//照片id
                            MediaStore.Video.Media.BUCKET_ID,//所属文件夹id
                            //                        MediaStore.Video.Media.PICASA_ID,
                            MediaStore.Video.Media.DATA,//图片地址
                            MediaStore.Video.Media.WIDTH,//图片宽度
                            MediaStore.Video.Media.HEIGHT,//图片高度
                            //                        MediaStore.Video.Media.DISPLAY_NAME,//图片全名，带后缀
                            //                        MediaStore.Video.Media.TITLE,
                            //                        MediaStore.Video.Media.DATE_ADDED,//创建时间？
                            MediaStore.Video.Media.DATE_MODIFIED,//最后修改时间
                            //                        MediaStore.Video.Media.DATE_TAKEN,
                            //                        MediaStore.Video.Media.SIZE,//图片文件大小
                            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,//所属文件夹名字
                            MediaStore.Video.Media.DURATION
                    };


            //得到一个游标
            ContentResolver crVideo = context.getContentResolver();
            crVideo.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columnsVideo, null, null, null);
            Cursor curVideo = crVideo.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columnsVideo, null, null, null);

            if (curVideo != null && curVideo.moveToFirst()) {
                //图片总数
                allVideoFloder.setNum(curVideo.getCount());
                allVideoFloder.setFloderType(1);//设置 文件夹类型 ：视频
                // 获取指定列的索引
                int imageIDIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                int imagePathIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                int imageModifyIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED);
                int imageWidthIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH);
                int imageHeightIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT);
                int floderIdIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID);
                int floderNameIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
                int durationLength = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);

                do {
                    String imageId = curVideo.getString(imageIDIndex);
                    String imagePath = curVideo.getString(imagePathIndex);
                    String lastModify = curVideo.getString(imageModifyIndex);
                    String width = curVideo.getString(imageWidthIndex);
                    String height = curVideo.getString(imageHeightIndex);
                    String floderId = curVideo.getString(floderIdIndex);
                    String floderName = curVideo.getString(floderNameIndex);
                    int duration = curVideo.getInt(durationLength);
                    //                    Log.e("ImagePicker", "imageId=" + imageId + "\n"
                    //                            + "imagePath=" + imagePath + "\n"
                    //                            + "lastModify=" + lastModify + "\n"
                    //                            + "width=" + width + "\n"
                    //                            + "height=" + height + "\n"
                    //                            + "floderId=" + floderId + "\n"
                    //                            + "floderName=" + floderName);
                    if (new File(imagePath).exists()) {
                        //创建图片对象
                        MediaDataBean mediaDataBean = new MediaDataBean();
                        mediaDataBean.setMediaId(imageId);
                        mediaDataBean.setMediaPath(imagePath);
                        mediaDataBean.setVideoLength(duration);
                        mediaDataBean.setLastModified(CommonUtils.isNotEmpty(lastModify) ? Long.valueOf(lastModify) : 0);
                        mediaDataBean.setWidth(CommonUtils.isNotEmpty(width) ? Integer.valueOf(width) : 0);
                        mediaDataBean.setHeight(CommonUtils.isNotEmpty(height) ? Integer.valueOf(height) : 0);
                        mediaDataBean.setFloderId(floderId);
                        mediaDataBean.setType(1);//视频的类型
                        mAllVideoList.add(mediaDataBean);
                        //更新文件夹对象
                        ImageFolderBean floderBean = null;
                        if (floderMap.containsKey(floderId))
                            floderBean = floderMap.get(floderId);
                        else
                            floderBean = new ImageFolderBean(floderId, floderName);
                        floderBean.setFirstImgPath(imagePath);
                        floderBean.gainNum();
                        floderMap.put(floderId, floderBean);
                    }
                } while (curVideo.moveToNext());
                curVideo.close();
            }
            //根据最后修改时间来降序排列所有图片
            Collections.sort(mAllImgList, new ImageComparator());
            Collections.sort(mAllVideoList,new ImageComparator());
            //设置“全部图片”文件夹的第一张图片
            allImgFloder.setFirstImgPath(mAllImgList.size() != 0 ? mAllImgList.get(0).getMediaPath() : null);
            allVideoFloder.setFirstImgPath(mAllVideoList.size()!=0?mAllVideoList.get(0).getMediaPath():null);
            //统一所有文件夹
            mAllFloderList.addAll(floderMap.values());

            return true;
        } catch (Exception e) {
            Log.e("ImagePicker", "ImagePicker scan data error:" + e);
            return false;
        }
    }
    /**
     * 扫描图片数据
     * 需要视频
     * @param c context
     * @return 成功或失败
     */
    public boolean scanAllDataNoImage(Context c) {
        try {
            Context context = c.getApplicationContext();
            //清空容器
            if (mAllImgList == null)
                mAllImgList = new ArrayList<>();
            if (mAllVideoList==null)
                mAllVideoList = new ArrayList<>();
            if (mAllFloderList == null)
                mAllFloderList = new ArrayList<>();
            if (mResultImageList == null)
                mResultImageList = new ArrayList<>();
            if (mResultVideoList==null)
                mResultVideoList = new ArrayList<>();
            mAllImgList.clear();
            mAllVideoList.clear();
            mAllFloderList.clear();
            mResultImageList.clear();
            mResultVideoList.clear();
            //创建“全部视频”的文件夹
            ImageFolderBean allVideoFloder = new ImageFolderBean(
                    com.laojiang.imagepickers.data.ImageContants.ID_ALL_VIDEO_FLODER, context.getResources().getString(R.string.imagepicker_all_video_floder));
            mAllFloderList.add(allVideoFloder);
            //临时存储所有文件夹对象的Map
            ArrayMap<String, ImageFolderBean> floderMap = new ArrayMap();
            /**
             * 获取视频
             */
            //索引字段
            String columnsVideo[] =
                    new String[]{MediaStore.Video.Media._ID,//照片id
                            MediaStore.Video.Media.BUCKET_ID,//所属文件夹id
                            //                        MediaStore.Video.Media.PICASA_ID,
                            MediaStore.Video.Media.DATA,//图片地址
                            MediaStore.Video.Media.WIDTH,//图片宽度
                            MediaStore.Video.Media.HEIGHT,//图片高度
                            //                        MediaStore.Video.Media.DISPLAY_NAME,//图片全名，带后缀
                            //                        MediaStore.Video.Media.TITLE,
                            //                        MediaStore.Video.Media.DATE_ADDED,//创建时间？
                            MediaStore.Video.Media.DATE_MODIFIED,//最后修改时间
                            //                        MediaStore.Video.Media.DATE_TAKEN,
                            //                        MediaStore.Video.Media.SIZE,//图片文件大小
                            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,//所属文件夹名字
                            MediaStore.Video.Media.DURATION
                    };

            //得到一个游标
            ContentResolver crVideo = context.getContentResolver();
            crVideo.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columnsVideo, null, null, null);
            Cursor curVideo = crVideo.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columnsVideo, null, null, null);

            if (curVideo != null && curVideo.moveToFirst()) {
                //图片总数
                allVideoFloder.setNum(curVideo.getCount());
                allVideoFloder.setFloderType(1);//设置 文件夹类型 ：视频
                // 获取指定列的索引
                int imageIDIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                int imagePathIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                int imageModifyIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED);
                int imageWidthIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH);
                int imageHeightIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT);
                int floderIdIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID);
                int floderNameIndex = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
                int durationLength = curVideo.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);

                do {
                    String imageId = curVideo.getString(imageIDIndex);
                    String imagePath = curVideo.getString(imagePathIndex);
                    String lastModify = curVideo.getString(imageModifyIndex);
                    String width = curVideo.getString(imageWidthIndex);
                    String height = curVideo.getString(imageHeightIndex);
                    String floderId = curVideo.getString(floderIdIndex);
                    String floderName = curVideo.getString(floderNameIndex);
                    int duration = curVideo.getInt(durationLength);
                    //                    Log.e("ImagePicker", "imageId=" + imageId + "\n"
                    //                            + "imagePath=" + imagePath + "\n"
                    //                            + "lastModify=" + lastModify + "\n"
                    //                            + "width=" + width + "\n"
                    //                            + "height=" + height + "\n"
                    //                            + "floderId=" + floderId + "\n"
                    //                            + "floderName=" + floderName);
                    if (new File(imagePath).exists()) {
                        //创建图片对象
                        MediaDataBean mediaDataBean = new MediaDataBean();
                        mediaDataBean.setMediaId(imageId);
                        mediaDataBean.setMediaPath(imagePath);
                        mediaDataBean.setVideoLength(duration);
                        mediaDataBean.setLastModified(CommonUtils.isNotEmpty(lastModify) ? Long.valueOf(lastModify) : 0);
                        mediaDataBean.setWidth(CommonUtils.isNotEmpty(width) ? Integer.valueOf(width) : 0);
                        mediaDataBean.setHeight(CommonUtils.isNotEmpty(height) ? Integer.valueOf(height) : 0);
                        mediaDataBean.setFloderId(floderId);
                        mediaDataBean.setType(1);//视频的类型
                        mAllVideoList.add(mediaDataBean);
                        //更新文件夹对象
                        ImageFolderBean floderBean = null;
                        if (floderMap.containsKey(floderId))
                            floderBean = floderMap.get(floderId);
                        else
                            floderBean = new ImageFolderBean(floderId, floderName);
                        floderBean.setFirstImgPath(imagePath);
                        floderBean.gainNum();
                        floderMap.put(floderId, floderBean);
                    }
                } while (curVideo.moveToNext());
                curVideo.close();
            }
            //根据最后修改时间来降序排列所有图片
            Collections.sort(mAllVideoList,new ImageComparator());
            //设置“全部图片”文件夹的第一张图片
            allVideoFloder.setFirstImgPath(mAllVideoList.size()!=0?mAllVideoList.get(0).getMediaPath():null);
            //统一所有文件夹
            mAllFloderList.addAll(floderMap.values());

            return true;
        } catch (Exception e) {
            Log.e("ImagePicker", "ImagePicker scan data error:" + e);
            return false;
        }
    }
    public boolean scanAllDataNoVideo(Context c) {
        try {
            Context context = c.getApplicationContext();
            //清空容器
            if (mAllImgList == null)
                mAllImgList = new ArrayList<>();
            if (mAllVideoList == null)
                mAllVideoList = new ArrayList<>();
            if (mAllFloderList == null)
                mAllFloderList = new ArrayList<>();
            if (mResultImageList == null)
                mResultImageList = new ArrayList<>();
            if (mResultVideoList == null)
                mResultVideoList = new ArrayList<>();
            mAllImgList.clear();
            mAllVideoList.clear();
            mAllFloderList.clear();
            mResultImageList.clear();
            mResultVideoList.clear();
            //创建“全部图片”的文件夹
            ImageFolderBean allImgFloder = new ImageFolderBean(
                    com.laojiang.imagepickers.data.ImageContants.ID_ALL_IMAGE_FLODER, context.getResources().getString(R.string.imagepicker_all_image_floder));
            mAllFloderList.add(allImgFloder);
            //临时存储所有文件夹对象的Map
            ArrayMap<String, ImageFolderBean> floderMap = new ArrayMap();

            //索引字段
            String columns[] =
                    new String[]{MediaStore.Images.Media._ID,//照片id
                            MediaStore.Images.Media.BUCKET_ID,//所属文件夹id
                            //                        MediaStore.Images.Media.PICASA_ID,
                            MediaStore.Images.Media.DATA,//图片地址
                            MediaStore.Images.Media.WIDTH,//图片宽度
                            MediaStore.Images.Media.HEIGHT,//图片高度
                            //                        MediaStore.Images.Media.DISPLAY_NAME,//图片全名，带后缀
                            //                        MediaStore.Images.Media.TITLE,
                            //                        MediaStore.Images.Media.DATE_ADDED,//创建时间？
                            MediaStore.Images.Media.DATE_MODIFIED,//最后修改时间
                            //                        MediaStore.Images.Media.DATE_TAKEN,
                            //                        MediaStore.Images.Media.SIZE,//图片文件大小
                            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,//所属文件夹名字
                    };


            //得到一个游标
            ContentResolver cr = context.getContentResolver();
            cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
            Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);

            if (cur != null && cur.moveToFirst()) {
                //图片总数
                allImgFloder.setNum(cur.getCount());
                allImgFloder.setFloderType(0);
                // 获取指定列的索引
                int imageIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int imagePathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int imageModifyIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED);
                int imageWidthIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);
                int imageHeightIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);
                int floderIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                int floderNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                do {
                    String imageId = cur.getString(imageIDIndex);
                    String imagePath = cur.getString(imagePathIndex);
                    String lastModify = cur.getString(imageModifyIndex);
                    String width = cur.getString(imageWidthIndex);
                    String height = cur.getString(imageHeightIndex);
                    String floderId = cur.getString(floderIdIndex);
                    String floderName = cur.getString(floderNameIndex);
                    //                    Log.e("ImagePicker", "imageId=" + imageId + "\n"
                    //                            + "imagePath=" + imagePath + "\n"
                    //                            + "lastModify=" + lastModify + "\n"
                    //                            + "width=" + width + "\n"
                    //                            + "height=" + height + "\n"
                    //                            + "floderId=" + floderId + "\n"
                    //                            + "floderName=" + floderName);
                    if (new File(imagePath).exists()) {
                        //创建图片对象
                        MediaDataBean mediaDataBean = new MediaDataBean();
                        mediaDataBean.setMediaId(imageId);
                        mediaDataBean.setMediaPath(imagePath);
                        mediaDataBean.setLastModified(CommonUtils.isNotEmpty(lastModify) ? Long.valueOf(lastModify) : 0);
                        mediaDataBean.setWidth(CommonUtils.isNotEmpty(width) ? Integer.valueOf(width) : 0);
                        mediaDataBean.setHeight(CommonUtils.isNotEmpty(height) ? Integer.valueOf(height) : 0);
                        mediaDataBean.setFloderId(floderId);
                        mediaDataBean.setType(0);//图片的类型
                        mAllImgList.add(mediaDataBean);
                        //更新文件夹对象
                        ImageFolderBean floderBean = null;
                        if (floderMap.containsKey(floderId))
                            floderBean = floderMap.get(floderId);
                        else
                            floderBean = new ImageFolderBean(floderId, floderName);
                        floderBean.setFirstImgPath(imagePath);
                        floderBean.gainNum();
                        floderMap.put(floderId, floderBean);
                    }
                } while (cur.moveToNext());
                cur.close();
            }
            //根据最后修改时间来降序排列所有图片
            Collections.sort(mAllImgList, new ImageComparator());
            //设置“全部图片”文件夹的第一张图片
            allImgFloder.setFirstImgPath(mAllImgList.size() != 0 ? mAllImgList.get(0).getMediaPath() : null);
            //统一所有文件夹
            mAllFloderList.addAll(floderMap.values());
            return true;
        } catch (Exception e) {
            Log.e("ImagePicker", "ImagePicker scan data error:" + e);
            return false;
        }
    }
    /**
     * 根据文件夹获取该文件夹下所有图片数据
     *
     * @param floderBean 文件夹对象
     * @return 图片数据list
     */
    public List<MediaDataBean> getImagesByFloder(ImageFolderBean floderBean) {
        if (floderBean == null)
            return null;

        String floderId = floderBean.getFloderId();
        if (CommonUtils.isEquals(com.laojiang.imagepickers.data.ImageContants.ID_ALL_IMAGE_FLODER, floderId)) {
            return mAllImgList;
        } else if (CommonUtils.isEquals(com.laojiang.imagepickers.data.ImageContants.ID_ALL_VIDEO_FLODER, floderId)) {
            return mAllVideoList;
        }else {
            ArrayList<MediaDataBean> resultList = new ArrayList<>();
            int size = mAllImgList.size();
            int sizeVideo = mAllVideoList.size();

            for (int i = 0; i < size; i++) {
                MediaDataBean mediaDataBean = mAllImgList.get(i);
                if (mediaDataBean != null && CommonUtils.isEquals(floderId, mediaDataBean.getFloderId()))
                    resultList.add(mediaDataBean);
            }
            for (int i = 0;i<sizeVideo;i++){
                MediaDataBean mediaDataBean = mAllVideoList.get(i);
                if (mediaDataBean != null && CommonUtils.isEquals(floderId, mediaDataBean.getFloderId()))
                    resultList.add(mediaDataBean);
            }
            return resultList;
        }
    }

    /**
     * 释放资源
     */
    public void clear() {
        mDisplayer = null;
        mAllImgList.clear();
        mAllFloderList.clear();
        mResultImageList.clear();
    }
}

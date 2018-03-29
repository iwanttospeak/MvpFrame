package com.laojiang.imagepickers.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片或者视频选择各参数
 */
public class PickerOptions implements Parcelable {
    private ImagePickType type = ImagePickType.SINGLE;
    //图片最多选择数量
    private int imageMaxNum = 1;
    //视频最多选择数量
    private int videoMaxNum = 1;
    //是否需要显示相机
    private boolean needCamera = false;
    //是否需要剪切，单选或者相机模式下支持剪切，多选不支持
    private boolean needCrop = false;
    //是否需要视频
    private boolean needVideo = false;
    //是否需要图片
    private boolean needImage = true;
    private ImagePickerCropParams cropParams;
    private String cachePath = ImageContants.DEF_CACHE_PATH;

    public boolean isNeedImage() {
        return needImage;
    }

    public void setNeedImage(boolean needImage) {
        this.needImage = needImage;
    }

    public ImagePickType getType() {
        return type;
    }
    public void setType(ImagePickType type) {
        this.type = type;
    }
    public int getImageMaxNum() {
        return imageMaxNum;
    }
    public void setImageMaxNum(int imageMaxNum) {
        if (imageMaxNum > 0)
            this.imageMaxNum = imageMaxNum;
    }
    public int getVideoMaxNum() {
        return videoMaxNum;
    }

    public void setVideoMaxNum(int videoMaxNum) {
        this.videoMaxNum = videoMaxNum;
    }
    public boolean isNeedCamera() {
        return needCamera;
    }
    public void setNeedCamera(boolean needCamera) {
        this.needCamera = needCamera;
    }

    public boolean isNeedCrop() {
        return needCrop;
    }

    public void setNeedCrop(boolean needCrop) {
        this.needCrop = needCrop;
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public ImagePickerCropParams getCropParams() {
        return cropParams;
    }

    public void setCropParams(ImagePickerCropParams cropParams) {
        this.cropParams = cropParams;
    }

    public boolean isNeedVideo() {
        return needVideo;
    }

    public void setNeedVideo(boolean needVideo) {
        this.needVideo = needVideo;
    }

    @Override
    public String toString() {
        return "ImagePickerOptions{" +
                "type=" + type +
                ", imageMaxNum=" + imageMaxNum +
                ", needCamera=" + needCamera +
                ", needCrop=" + needCrop +
                ", cropParams=" + cropParams +
                ", cachePath='" + cachePath + '\'' +
                ", needVideo=" + needVideo +
                ", needImage=" + needImage +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeInt(this.imageMaxNum);
        dest.writeByte(this.needCamera ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needCrop ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.cropParams, flags);
        dest.writeString(this.cachePath);
        dest.writeByte(this.needVideo ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needImage ? (byte) 1 : (byte) 0);
    }

    public PickerOptions() {
    }

    protected PickerOptions(Parcel in) {
        int tmpMode = in.readInt();
        this.type = tmpMode == -1 ? null : ImagePickType.values()[tmpMode];
        this.imageMaxNum = in.readInt();
        this.needCamera = in.readByte() != 0;
        this.needCrop = in.readByte() != 0;
        this.cropParams = in.readParcelable(ImagePickerCropParams.class.getClassLoader());
        this.cachePath = in.readString();
        this.needVideo = in.readByte() != 0;
        this.needImage = in.readByte() != 0;
    }

    public static final Parcelable.Creator<PickerOptions> CREATOR = new Parcelable.Creator<PickerOptions>() {
        @Override
        public PickerOptions createFromParcel(Parcel source) {
            return new PickerOptions(source);
        }

        @Override
        public PickerOptions[] newArray(int size) {
            return new PickerOptions[size];
        }
    };
}

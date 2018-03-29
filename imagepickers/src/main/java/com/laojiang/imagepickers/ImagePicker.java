package com.laojiang.imagepickers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;


import com.laojiang.imagepickers.data.ImageContants;
import com.laojiang.imagepickers.data.ImageDataModel;
import com.laojiang.imagepickers.data.ImagePickType;
import com.laojiang.imagepickers.data.ImagePickerCropParams;
import com.laojiang.imagepickers.data.PickerOptions;
import com.laojiang.imagepickers.image.grid.view.ImageDataActivity;
import com.laojiang.imagepickers.utils.IImageDisplay;
/**
 *  调用方法的入口
 */
public class ImagePicker {
    /**
     * 默认的ResultCode,不要轻易修改
     */
    public static final int DEF_RESULT_CODE = Activity.RESULT_OK;

    /**
     * 返回结果中包含图片数据的Intent的键值
     */
    public static final String INTENT_RESULT_DATA = "ImageBeans";

    private PickerOptions mOptions;

    public PickerOptions getmOptions() {
        return mOptions;
    }

    private ImagePicker() {
    }

    private ImagePicker(PickerOptions options) {
        this.mOptions = options;
    }

    /**
     * 发起选择图片
     * @param activity    发起的Activity
     * @param requestCode 请求码
     * @param resultCode  结果码
     */
    public void start(Activity activity, int requestCode,int resultCode) {
        Intent intent = new Intent(activity, ImageDataActivity.class);
        intent.putExtra(ImageContants.INTENT_KEY_OPTIONS, mOptions);
        intent.putExtra(ImageContants.INTENT_KEY_RESULTCODE, resultCode);
        activity.startActivityForResult(intent, requestCode);
    }
    /**
     * 发起选择图片
     *
     * @param fragment    发起的Fragment
     * @param requestCode 请求码
     * @param resultCode  结果码
     */
    public void start(Fragment fragment, int requestCode, int resultCode) {
        Intent intent = new Intent(fragment.getActivity(), ImageDataActivity.class);
        intent.putExtra(ImageContants.INTENT_KEY_OPTIONS, mOptions);
        intent.putExtra(ImageContants.INTENT_KEY_RESULTCODE, resultCode);
        fragment.startActivityForResult(intent, requestCode);
    }
    /**
     * 发起选择图片
     * @param context    发起的Activity
     */
    public void start(Context context) {
        Intent intent = new Intent(context, ChoosePicActivity.class);
        intent.putExtra(ImageContants.INTENT_KEY_OPTIONS, mOptions);
        context.startActivity(intent);
    }

    public static final class Builder {
        private PickerOptions mOptions;

        public Builder() {
            mOptions = new PickerOptions();
        }

        public Builder pickType(ImagePickType mode) {
            mOptions.setType(mode);
            return this;
        }

        public Builder ImageMaxNum(int maxNum) {
            mOptions.setImageMaxNum(maxNum);
            return this;
        }
        public Builder needCamera(boolean b) {
            mOptions.setNeedCamera(b);
            return this;
        }

        public Builder cachePath(String path) {
            mOptions.setCachePath(path);
            return this;
        }

        public Builder doCrop(ImagePickerCropParams cropParams) {
            mOptions.setNeedCrop(cropParams != null);
            mOptions.setCropParams(cropParams);
            return this;
        }

        public Builder doCrop(int aspectX, int aspectY, int outputX, int outputY) {
            mOptions.setNeedCrop(true);
            mOptions.setCropParams(new ImagePickerCropParams(aspectX, aspectY, outputX, outputY));
            return this;
        }

        public Builder displayer(IImageDisplay displayer) {
            ImageDataModel.getInstance().setDisplayer(displayer);
            return this;
        }
        public Builder needVideo(boolean need){
            mOptions.setNeedVideo(need);
            return this;
        }
        public Builder needImage(boolean need){
            mOptions.setNeedImage(need);
            return this;
        }
        public ImagePicker build() {
            return new ImagePicker(mOptions);
        }
    }
}

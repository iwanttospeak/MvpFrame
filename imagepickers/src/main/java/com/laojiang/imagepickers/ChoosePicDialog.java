package com.laojiang.imagepickers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.laojiang.imagepickers.data.ImagePickType;
import com.laojiang.imagepickers.utils.DefaultGlideDisplay;

import java.io.IOException;
import java.util.List;

/**
 * 选择图片dialog，不包括视频
 */
public class ChoosePicDialog extends Dialog {

    //是否多选
    private boolean isMultiple = false;
    //是否进行剪切,多选模式下会被忽略
    private boolean isCrop = false;
    //可以选择的数量
    private int chooseNumber;

    private static OnChoosePicListener onChoosePicListener;

    public ChoosePicDialog(@NonNull Context context) {
        this(context,R.style.image_dialog_style);
    }
    public ChoosePicDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context,themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_dialog_choose_pic);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(layoutParams);
        getWindow().setGravity(Gravity.BOTTOM);//底部出现
        initListener();
    }

    private void initListener() {
        //拍摄
        findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFromCamera();
            }
        });

        //从相册中选择
        findViewById(R.id.tv_choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFromGallery();
            }
        });

        //取消
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    /**
     * 拍照.
     */
    private void chooseFromCamera() {
        ImagePicker.Builder build = new ImagePicker.Builder();
        ImagePicker imagePicker;
        if (isCrop){//裁剪功能需要调用这个方法，多选模式下无效，参数：aspectX,aspectY,outputX,outputY
            build.doCrop(1, 1, 300, 300);
        }
        imagePicker = build
                .pickType(ImagePickType.ONLY_CAMERA) //设置选取类型(拍照ONLY_CAMERA、单选SINGLE、多选MUTIL)
                .displayer(new DefaultGlideDisplay()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                .build();
        imagePicker.start(getContext()); //自定义RequestCode和ResultCode

        dismiss();
    }
    /**
     * 从相册选择.
     */
    private void chooseFromGallery() {
        ImagePicker.Builder build = new ImagePicker.Builder();
        ImagePicker imagePicker;
        if (isMultiple){
            build.pickType(ImagePickType.MUTIL);
            build.ImageMaxNum(chooseNumber);
        }else {
            build.pickType(ImagePickType.SINGLE);
        }
        if (isCrop){
            build.needCamera(false) //是否需要在界面中显示相机入口(类似微信那样)
                    .doCrop(1, 1, 300, 300);//裁剪功能需要调用这个方法，多选模式下无效，参数：aspectX,aspectY,outputX,outputY
        }
        imagePicker =  build
                .displayer(new DefaultGlideDisplay()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                .needVideo(false)//是否需要视频
                .build();
        imagePicker.start(getContext()); //自定义RequestCode和ResultCode
        dismiss();
    }

    public ChoosePicDialog setMultiple(boolean multiple) {
        isMultiple = multiple;
        return this;
    }

    public ChoosePicDialog setCrop(boolean crop) {
        isCrop = crop;
        return this;
    }

    public ChoosePicDialog setChooseNumber(int chooseNumber) {
        this.chooseNumber = chooseNumber;
        return this;
    }

    public ChoosePicDialog setOnChoosePicListener(OnChoosePicListener onChoosePicListener) {
        ChoosePicDialog.onChoosePicListener = onChoosePicListener;
        return this;
    }

    public static void setResult(List<String> result) {
        if (onChoosePicListener != null){
            onChoosePicListener.onChoose(result);
        }
    }

    //返回图片的压缩路径字符串列表
    public interface OnChoosePicListener {
        public void onChoose(List<String> picList);
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotaingImageView(String path, Bitmap bitmap) {
        int angle = readPictureDegree(path);
        //旋转图片 动作
        Matrix matrix = new Matrix();

        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
}

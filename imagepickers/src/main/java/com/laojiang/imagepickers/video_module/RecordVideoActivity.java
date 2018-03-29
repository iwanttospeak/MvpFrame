package com.laojiang.imagepickers.video_module;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.app.camera.JCameraView;
import com.app.camera.lisenter.ClickListener;
import com.app.camera.lisenter.ErrorListener;
import com.app.camera.lisenter.JCameraListener;
import com.app.camera.util.FileUtil;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.base.activity.NoduleBaseActivity;
import com.laojiang.imagepickers.data.ImageContants;

import java.io.File;


/**
 * 录制视频或者拍照
 */
public class RecordVideoActivity extends NoduleBaseActivity {

    protected JCameraView jCameraView;
    protected String cacheVideoPath;
    protected String cacheImgPath;
    //是否需要摄像
    protected boolean isNeedVideo;
    @Override
    protected int getContentViewResId() {
        return R.layout.base_activity_camera;
    }

    @Override
    protected void initUI(View contentView) {
        jCameraView = (JCameraView) findViewById(R.id.jv_cameraview);
    }

    @Override
    protected void beforeSetContentView(Bundle savedInstanceState){
        super.beforeSetContentView(savedInstanceState);
        cacheVideoPath = getIntent().getStringExtra("recordVideoPath");
        cacheImgPath = getIntent().getStringExtra("recordImgPath");
        isNeedVideo = getIntent().getBooleanExtra("isNeedVideo",false);
        if (cacheVideoPath == null) {
            cacheVideoPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "record";
        } else {
//            cacheVideoPath = cacheVideoPath.substring(0, cacheVideoPath.length() - 1);
        }
    }
    public static void start(Activity context, String path,boolean isNeedVideo) {
        Intent intent = new Intent(context, RecordVideoActivity.class);
        intent.putExtra("cacheVideoPath", path);
        intent.putExtra("isNeedVideo", isNeedVideo);
        context.startActivityForResult(intent, ImageContants.CAMERA_REQUEST);
    }
    /**
     * 拍摄视频
     */
    @Override
    protected void initData() {
        //判断外部存储是否存在，以及是否有足够空间保存指定类型的文件
        //得到Video类型的文件夹路径
        if (StringUtils.isSpace(cacheVideoPath)){
            cacheVideoPath = ImageContants.DEF_CACHE_PATH;
        }
        //设置视频保存路径
        if (cacheVideoPath.endsWith(".mp4")){
            jCameraView.setSaveAbsVideoPath(cacheVideoPath);
        }else {
            jCameraView.setSaveVideoPath(cacheVideoPath);
        }
        //既可以拍照也可以录像
        if (isNeedVideo){
            jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
            jCameraView.setTip("点击拍照，长按摄像");
        }else {
            jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_CAPTURE);
            jCameraView.setTip("");
        }
        //录制视频比特率
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);
    }

    @Override
    protected void initListener() {
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //错误监听
                finish();
            }
            @Override
            public void AudioPermissionError() {
                ToastUtils.showShort("给点录音权限可以");
            }
        });
        //JCameraView监听
        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                capturePhotoSuccess(bitmap);
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                recordVideoSuccess(url,firstFrame);
            }
        });
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                killMySelf();
            }
        });
    }

    private void killMySelf() {
        finish();
    }

    /**
     * 拍照成功
     */
    protected void capturePhotoSuccess(Bitmap bitmap) {
        //获取图片bitmap
        String path = getImgPath( bitmap);
        Intent intent = new Intent();
        intent.putExtra(ImageContants.DIY_CAMERA_PATH, path);
        intent.putExtra(ImageContants.CAMERA_RECORD,ImageContants.RESULT_CODE_IMAGE);//区分拍照和摄像
        setResult(Activity.RESULT_OK, intent);
        Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
        finish();
    }
    public String getImgPath(Bitmap bitmap){
        String path;
        if (StringUtils.isSpace(cacheImgPath)){
            path = FileUtil.saveBitmap("record", bitmap);
        }else {
            path= FileUtil.saveBitmapWithPath(cacheImgPath, bitmap);
        }
        return path;
    }
    /**
     * 录制视频成功
     */
    protected void recordVideoSuccess(String url, Bitmap firstFrame) {
        Intent intent = new Intent();
        intent.putExtra(ImageContants.DIY_CAMERA_SHEXIANG_PATH, url);
        intent.putExtra(ImageContants.CAMERA_RECORD,ImageContants.RESULT_CODE_VIDEO);//区分拍照和摄像
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }


}
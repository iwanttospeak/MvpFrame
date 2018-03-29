package com.laojiang.imagepickers.image.crop;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.base.activity.NoduleBaseActivity;
import com.laojiang.imagepickers.data.ImageContants;
import com.laojiang.imagepickers.data.ImagePickerCropParams;
import com.laojiang.imagepickers.data.PickerOptions;
import com.laojiang.imagepickers.widget.crop.CropUtil;
import com.laojiang.imagepickers.widget.crop.CropView;

import java.io.File;

/**
 * 裁剪界面
 */
public class ImageCropActivity extends NoduleBaseActivity {
    /**
     * 跳转到该界面的公共方法
     *
     * @param activity   发起跳转的Activity
     * @param originPath 待裁剪图片路径
     * @param options    参数
     */
    public static void start(Activity activity, String originPath, PickerOptions options) {
        Intent intent = new Intent(activity, ImageCropActivity.class);
        intent.putExtra(ImageContants.INTENT_KEY_ORIGIN_PATH, originPath);
        intent.putExtra(ImageContants.INTENT_KEY_OPTIONS, options);
        activity.startActivityForResult(intent, ImageContants.REQUEST_CODE_CROP);
    }

    private PickerOptions mOptions;
    private String mOriginPath;
    private CropView mCropView;
    private Handler mHandler;
    private ProgressDialog mDialog;
    private ImagePickerCropParams mCropParams;

    TextView rightText;
    @Override
    protected void beforeSetContentView(Bundle savedInstanceState) {
        super.beforeSetContentView(savedInstanceState);
        Intent intent = getIntent();
        mOriginPath = intent.getStringExtra(ImageContants.INTENT_KEY_ORIGIN_PATH);
        mOptions = intent.getParcelableExtra(ImageContants.INTENT_KEY_OPTIONS);
    }

    @Override
    protected int getContentViewResId() {
        mHandler = new Handler(getMainLooper());
        return R.layout.activity_image_crop;
    }

    @Override
    protected void initUI(View contentView) {
        mCropView = findView(R.id.cv_crop);
        toolbar.setNavigationIcon(R.drawable.image_nav_close);
        rightText = findView(R.id.toolbar_text_right);
        rightText.setText("完成");
    }

    @Override
    protected void initData() {
        if (mOptions == null) {
            showShortToast(R.string.error_imagepicker_lack_params);
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        if (mOriginPath == null || mOriginPath.length() == 0) {
            showShortToast(R.string.imagepicker_crop_decode_fail);
            setResult(RESULT_CANCELED);
            finish();
            return;
        }

        File file = new File(mOriginPath);
        if (!file.exists()) {
            showShortToast(R.string.imagepicker_crop_decode_fail);
            finish();
            return;
        }

        mCropParams = mOptions.getCropParams();
        mCropView.load(mOriginPath)
                .setAspect(mCropParams.getAspectX(), mCropParams.getAspectY())
                .setOutputSize(mCropParams.getOutputX(), mCropParams.getOutputY())
                .start(this);
    }
    @Override
    protected void initListener() {
        rightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnCropedImage();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    //保存并返回数据
    private void returnCropedImage() {
        showDialog();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Bitmap bitmap = mCropView.getOutput();
                String cachePath = mOptions.getCachePath();
                String name = CropUtil.createCropName();
                String resultPath = CropUtil.saveBmp(bitmap, cachePath, name);
                closeDialog();
                if (TextUtils.isEmpty(resultPath)) {
                    showShortToast(R.string.imagepicker_crop_save_fail);
                    setResult(RESULT_CANCELED);
                    finish();
                } else {
                    //保存成功后返回给上级界面
                    Intent intent = new Intent();
                    intent.putExtra(ImageContants.INTENT_KEY_CROP_PATH, resultPath);
                    setResult(ImageContants.RESULT_CODE_CROP_OK, intent);
                    finish();
                }
            }
        }).start();
    }

    private void showDialog() {
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                mDialog = new ProgressDialog(ImageCropActivity.this);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setMessage(getString(R.string.imagepicker_crop_dialog));
                mDialog.show();
            }
        });
    }

    private void closeDialog() {
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (mDialog != null && mDialog.isShowing())
                    mDialog.dismiss();
                mDialog = null;
            }
        });
    }
}

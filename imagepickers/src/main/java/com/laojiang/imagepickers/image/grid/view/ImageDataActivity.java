package com.laojiang.imagepickers.image.grid.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laojiang.imagepickers.ChoosePicDialog;
import com.laojiang.imagepickers.ImagePicker;
import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.base.activity.NoduleBaseActivity;
import com.laojiang.imagepickers.compress.CompressConfig;
import com.laojiang.imagepickers.compress.CompressImage;
import com.laojiang.imagepickers.compress.CompressImageImpl;
import com.laojiang.imagepickers.compress.TImage;
import com.laojiang.imagepickers.data.ImageContants;
import com.laojiang.imagepickers.data.ImageDataModel;
import com.laojiang.imagepickers.data.ImageFolderBean;
import com.laojiang.imagepickers.data.ImagePickType;
import com.laojiang.imagepickers.data.PickerOptions;
import com.laojiang.imagepickers.data.MediaDataBean;
import com.laojiang.imagepickers.video_module.RecordVideoActivity;
import com.laojiang.imagepickers.image.crop.ImageCropActivity;
import com.laojiang.imagepickers.image.grid.adapter.ImageDataAdapter;
import com.laojiang.imagepickers.image.grid.presenter.ImageDataPresenter;
import com.laojiang.imagepickers.image.pager.ImagePagerActivity;
import com.laojiang.imagepickers.utils.CommonUtils;
import com.laojiang.imagepickers.utils.PermissionChecker;
import com.laojiang.imagepickers.video_module.SelectedPlayActivity;

import java.util.ArrayList;
import java.util.List;

import static com.laojiang.imagepickers.data.ImageContants.REQUEST_CODE_PERMISSION_CAMERA;
import static com.laojiang.imagepickers.data.ImageContants.REQUEST_CODE_PERMISSION_SDCARD;
import static com.laojiang.imagepickers.data.ImageContants.REQUEST_CODE_VIDEO;
import static com.laojiang.imagepickers.data.ImageContants.RESULT_CODE_OK;
import static com.laojiang.imagepickers.utils.PermissionChecker.checkPermissions;
/**
 * 展示图片数据的Activity
 */
public class ImageDataActivity extends NoduleBaseActivity implements IImageDataView
        , ImageFolderPop.onFolderItemClickListener,TakeResultListener {

    private ImageDataPresenter mPresenter;
    private PickerOptions mOptions;
    private ImageDataAdapter mAdapter;
    private GridView mGridView;
    private ProgressBar mPgbLoading;

    private View mViewBottom;
    private View mViewFolder;
    private TextView mTvFolderName;
    private ImageFolderBean mCurFolder;
    private String mPhotoPath;
    private TextView tvPreview;
    private ImageView ivTitleImg;
    private TextView tvRightText;
    private RelativeLayout rlToolBar;
    //自定义结果码
    private int mResultCode;

    @Override
    protected void beforeSetContentView(Bundle savedInstanceState) {
        super.beforeSetContentView(savedInstanceState);
        Intent intent = getIntent();
        mOptions = intent.getParcelableExtra(ImageContants.INTENT_KEY_OPTIONS);
        mResultCode = intent.getIntExtra(ImageContants.INTENT_KEY_RESULTCODE, ImagePicker.DEF_RESULT_CODE);
    }

    @Override
    protected int getContentViewResId() {
        mPresenter = new ImageDataPresenter(mOptions,this);
        return R.layout.activity_image_data;
    }

    @Override
    protected void initUI(View contentView) {
        if (mOptions == null) {
            showShortToast(R.string.error_imagepicker_lack_params);
            finish();
            return;
        }
        if (mOptions.getType() == ImagePickType.ONLY_CAMERA) {
            startTakePhoto();
            return;
        }
        tvRightText = findView(R.id.toolbar_text_right);
        rlToolBar = findView(R.id.rl_toolbar);
        ivTitleImg = findView(R.id.iv_title_bg);
        tvPreview = findView(R.id.tv_imagepicker_actionbar_preview);
        mViewBottom = findView(R.id.fl_image_data_bottom);
        //直接开启拍照
        mGridView = findView(R.id.gv_image_data);
        mPgbLoading = findView(R.id.pgb_image_data);
        mViewFolder = findView(R.id.ll_image_data_bottom_floder);
        mTvFolderName = findView(R.id.tv_image_data_bottom_flodername);
        if (mOptions.getType() == ImagePickType.SINGLE) {//单选
            mViewBottom.setVisibility(View.GONE);
        } else {
            mViewBottom.setVisibility(View.VISIBLE);
            onSelectNumChanged(0);
        }
    }
    @Override
    public void initListener(){
        if (mOptions.getType() == ImagePickType.ONLY_CAMERA) return;
        //去预览界面
        tvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageContants.CURRENT_SELECTED_TYPE == ImageContants.SELECTED_TYPE_IMAGE){
                    ImagePagerActivity.start(ImageDataActivity.this, (ArrayList<MediaDataBean>) ImageDataModel.getInstance().getmResultImageList()
                            , 0, mOptions, ImageContants.REQUEST_CODE_PREVIEW);
                }
                if (ImageContants.CURRENT_SELECTED_TYPE == ImageContants.SELECTED_TYPE_VIDEO){
                    SelectedPlayActivity.start(ImageDataActivity.this,ImageDataModel.getInstance().getmResultVideoList().get(0),mOptions,REQUEST_CODE_VIDEO);
                }
            }
        });
        //返回选中的图片
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnAllSelectedImages();
            }
        });
        //弹出文件夹切换菜单
        mViewFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new ImageFolderPop().showAtBottom(ImageDataActivity.this, mContentView, mCurFolder, ImageDataActivity.this);
                new ImageFolderPop().showMenuPop(rlToolBar,ImageDataActivity.this, mCurFolder, ImageDataActivity.this);
            }
        });
    }
    @Override
    protected void initData() {
        if (mOptions == null || mOptions.getType() == ImagePickType.ONLY_CAMERA) return;
        mAdapter = new ImageDataAdapter(this, this);
        mGridView.setAdapter(mAdapter);
        doScanData();
    }

    @Override
    public PickerOptions getOptions() {
        return mOptions;
    }

    @Override
    public void startTakePhoto() {
        if (!CommonUtils.isSdExist()) {
            showShortToast(R.string.error_no_sdcard);
            return;
        }
        boolean hasPermissions = checkPermissions(this
                , new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , REQUEST_CODE_PERMISSION_CAMERA, R.string.dialog_imagepicker_permission_camera_message);
        //有权限就直接拍照
        if (hasPermissions)
            doTakePhoto();
    }

    //执行拍照的方法
    private void doTakePhoto() {
        if (mOptions.isNeedVideo()){
            RecordVideoActivity.start(this,mOptions.getCachePath(),true);
        }else {
            RecordVideoActivity.start(this,mOptions.getCachePath(),false);
        }
        //调用系统拍照
//        mPhotoPath = TakePhotoCompatUtils.takePhoto(this, ImageContants.REQUEST_CODE_TAKE_PHOTO, mOptions.getCachePath());
    }

    //执行扫描sd卡的方法
    private void doScanData() {
        if (!CommonUtils.isSdExist()) {
            showShortToast(R.string.error_no_sdcard);
            return;
        }

        boolean hasPermission = checkPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSION_SDCARD, R.string.dialog_imagepicker_permission_sdcard_message);
        //有权限直接扫描
        if (hasPermission)
            mPresenter.scanData(this);
    }

    @Override
    public void showLoading() {
        if (mPgbLoading != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPgbLoading.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void hideLoading() {
        if (mPgbLoading != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mPgbLoading.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onDataChanged(final List<MediaDataBean> dataList) {
        if (mGridView != null && mAdapter != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mGridView.setVisibility(View.VISIBLE);
                    mAdapter.refreshDatas(dataList);
                    mGridView.setSelection(0);
                }
            });
        }
    }

    @Override
    public void onFolderChanged(ImageFolderBean folderBean) {
        if (mCurFolder != null && folderBean != null && mCurFolder.equals(folderBean))
            return;

        mCurFolder = folderBean;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mTvFolderName != null)
                    mTvFolderName.setText(mCurFolder.getFloderName());
            }
        });
        mPresenter.checkDataByFloder(folderBean);
    }

    @Override
    public void onImageClicked(MediaDataBean mediaDataBean, int position) {

            //当 类型是图片的时候
            if (mediaDataBean.getType() == 0) {
                //去查看大图的界面
                //如果有相机入口需要调整传递的数据
                int p = position;
                ArrayList<MediaDataBean> dataList = new ArrayList<>();
                List<MediaDataBean> datas = mAdapter.getDatas();

                if (mOptions.isNeedCamera()) {
                    p--;
                    for (int i = 1; i < datas.size(); i++) {
                        if (datas.get(i).getType() == 0) {
                            dataList.add(datas.get(i));
                        }
                    }
                } else {
                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).getType() == 0) {
                            dataList.add(datas.get(i));
                        }
                    }
                }
                ImagePagerActivity.start(this, dataList, p, mOptions, ImageContants.REQUEST_CODE_DETAIL);
            }else {//点击 进入视频
                SelectedPlayActivity.start(this, mediaDataBean,mOptions,REQUEST_CODE_VIDEO);
            }
    }

    @Override
    public void onSelectNumChanged(int curNum) {
        if (mOptions.getType() !=ImagePickType.MUTIL) return;
        if (curNum == 0) {
            tvRightText.setEnabled(false);
            tvRightText.setTextColor(ContextCompat.getColor(this,R.color.image_edit_text_hint));
            tvRightText.setText(getString(R.string.image_pager_next));
            tvPreview.setTextColor(ContextCompat.getColor(this,R.color.image_edit_text_hint));
            tvPreview.setEnabled(false);
        } else {
            tvRightText.setEnabled(true);
            tvRightText.setTextColor(ContextCompat.getColor(this,R.color.image_text_color_blue));
            tvRightText.setText(getString(R.string.btn_image_pager_next, String.valueOf(curNum)));
            tvPreview.setTextColor(ContextCompat.getColor(this,R.color.image_title_bar_text_color));
            tvPreview.setEnabled(true);
        }
    }

    @Override
    public void warningImageMaxNum() {
        showShortToast(getString(R.string.warning_imagepicker_max_num, String.valueOf(mOptions.getImageMaxNum())));
    }
    @Override
    public void warningVideoMaxNum() {
        showShortToast(getString(R.string.warning_video_picker_max_num, String.valueOf(mOptions.getVideoMaxNum())));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //裁剪
        if (requestCode == ImageContants.REQUEST_CODE_CROP) {
            if (resultCode == ImageContants.RESULT_CODE_CROP_OK) {
                //裁剪成功返回数据
                String cropPath = data.getStringExtra(ImageContants.INTENT_KEY_CROP_PATH);
                returnSingleImage(mPresenter.getImageBeanByPath(cropPath));
            } else if (mOptions.getType() == ImagePickType.ONLY_CAMERA) {
                finish();
            }
            //预览或者大图界面返回
        } else if (requestCode == ImageContants.REQUEST_CODE_PREVIEW
                || requestCode == ImageContants.REQUEST_CODE_DETAIL) {
            if (resultCode == RESULT_CODE_OK) {
                returnAllSelectedImages();
            } else {
                //刷新视图
                mAdapter.notifyDataSetChanged();
                onSelectedNumberChange();
            }
        } else if (requestCode == ImageContants.REQUEST_CODE_VIDEO) {
            if (resultCode == RESULT_CODE_OK) {
                returnVideoBack(null);
            } else {
                //刷新视图
                mAdapter.notifyDataSetChanged();
                onSelectedNumberChange();
            }
        } else if (resultCode == ImageContants.CAMERA_REQUEST) {//打开相机
            if (data == null) return;
            if (data.getIntExtra(ImageContants.CAMERA_RECORD, -1) == ImageContants.RESULT_CODE_IMAGE) {//拍照返回码
                String path = data.getStringExtra(ImageContants.DIY_CAMERA_PATH);
                MediaDataBean bean = new MediaDataBean();
                bean.setType(0);
                bean.setMediaPath(path);
                mPhotoPath = path;

                //非多选模式下需要判断是否有裁剪的需求
                if (mOptions.getType() != ImagePickType.MUTIL && mOptions.isNeedCrop()) {
                    //执行裁剪
                    ImageCropActivity.start(this, mPhotoPath, mOptions);
                } else {
                    returnSingleImage(mPresenter.getImageBeanByPath(mPhotoPath));
                }
            } else if (data.getIntExtra(ImageContants.CAMERA_RECORD, -1) == ImageContants.RESULT_CODE_IMAGE) {//摄像返回码
                returnVideoBack(data);
            } else {
                takeCancel();
                finish();
            }
        }
    }

    /**
     * 通知选中数量发生变化
     */
    public void onSelectedNumberChange() {
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_VIDEO) {
            onSelectNumChanged(ImageDataModel.getInstance().getmResultVideoList().size());
            return;
        }
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_IMAGE) {
            onSelectNumChanged(ImageDataModel.getInstance().getmResultImageList().size());
            return;
        }
        onSelectNumChanged(0);
    }

    /**
     * 视频选择完毕返回
     */
    private void returnVideoBack(Intent data) {
        ArrayList<MediaDataBean> resultList = new ArrayList<>();
        if (data!=null){
            String path = data.getStringExtra(ImageContants.DIY_CAMERA_SHEXIANG_PATH);
            MediaDataBean bean = new MediaDataBean();
            bean.setType(1);
            bean.setMediaPath(path);
            resultList.add(bean);
        }else {
            List<MediaDataBean> beans = ImageDataModel.getInstance().getResultList();
            if (beans != null && beans.size() >0) {
                resultList.addAll(beans);
            }
        }
        takeSuccess(resultList);
    }
    private CompressConfig compressConfig;

    /**
     * 压缩图片
     */
    private void compressImages(){
        ArrayList<TImage> images = new ArrayList<>();
        CompressImageImpl.of(this, compressConfig,images, new CompressImage.CompressListener() {
            @Override
            public void onCompressSuccess(ArrayList<TImage> images) {
                if(!compressConfig.isEnableReserveRaw()) {
                }
            }
            @Override
            public void onCompressFailed(ArrayList<TImage> images, String msg) {
                if(!compressConfig.isEnableReserveRaw()) {
                }
            }
        }).compress();
    }
    //返回单张图片数据
    private void returnSingleImage(MediaDataBean mediaDataBean) {
        ArrayList<MediaDataBean> list = new ArrayList<>();
        list.add(mediaDataBean);
        takeSuccess(list);
    }
    //返回所有已选中的图片
    private void returnAllSelectedImages() {
        ArrayList<MediaDataBean> resultList = new ArrayList<>();
        List<MediaDataBean> beans = ImageDataModel.getInstance().getResultList();
        if (beans != null && beans.size() >0) {
            resultList.addAll(beans);
        }
        takeSuccess(resultList);
    }

    @Override
    public void onFolderItemClicked(ImageFolderBean folderBean) {
        onFolderChanged(folderBean);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean[] result;
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CAMERA:
                if (mOptions.getType() == ImagePickType.ONLY_CAMERA) {
                    result = PermissionChecker.onRequestPermissionsResult(this, permissions, grantResults, true
                            , R.string.dialog_imagepicker_permission_camera_nerver_ask_message);
                    if (result[0])
                        doTakePhoto();
                    else if (!result[1])
                        finish();
                } else {
                    result = PermissionChecker.onRequestPermissionsResult(this, permissions, grantResults, false
                            , R.string.dialog_imagepicker_permission_camera_nerver_ask_message);
                    if (result[0])
                        doTakePhoto();
                }
                break;
            case REQUEST_CODE_PERMISSION_SDCARD:
                result = PermissionChecker.onRequestPermissionsResult(this, permissions, grantResults, false
                        , R.string.dialog_imagepicker_permission_sdcard_nerver_ask_message);
                //                if (result[0])
                //                    mPresenter.scanData(this);
                //无论成功失败都去扫描，以便更新视图
                mPresenter.scanData(this);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestory();
        super.onDestroy();
    }

    @Override
    public void takeSuccess(ArrayList<MediaDataBean> result) {
        //保存成功后返回给调用界面
        Intent intent = new Intent();
        intent.putExtra(ImagePicker.INTENT_RESULT_DATA, result);
        setResult(ImagePicker.DEF_RESULT_CODE, intent);
        finish();
    }

    @Override
    public void takeFail(List<MediaDataBean> result, String msg) {
    }

    @Override
    public void takeCancel() {
    }

}

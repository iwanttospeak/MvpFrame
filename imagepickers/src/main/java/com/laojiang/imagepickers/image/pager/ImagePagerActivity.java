package com.laojiang.imagepickers.image.pager;

import android.app.Activity;
import android.content.Intent;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.laojiang.imagepickers.ImagePicker;
import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.base.activity.NoduleBaseActivity;
import com.laojiang.imagepickers.data.ImagePickType;
import com.laojiang.imagepickers.data.MediaDataBean;
import com.laojiang.imagepickers.data.ImageContants;
import com.laojiang.imagepickers.data.ImageDataModel;
import com.laojiang.imagepickers.data.PickerOptions;
import com.laojiang.imagepickers.image.crop.ImageCropActivity;
import com.laojiang.imagepickers.utils.CommonUtils;
import com.laojiang.imagepickers.widget.ImagePickerViewPager;

import java.io.File;
import java.util.ArrayList;

/**
 * 滑动查看图片的Activity,单选或者多选或者浏览图片Pager页面
 * 支持图片选择体系内部图片浏览，不支持普通情况下的图片浏览，
 */
public class ImagePagerActivity extends NoduleBaseActivity {
    private ArrayList<MediaDataBean> mDataList;
    private int mCurPosition;

    private PickerOptions mOptions;
    private ImagePickerViewPager mViewPager;
    private CheckBox mCkSelected;
    private Button mBtnOk;
    private ImagePagerAdapter mAdapter;

    private RelativeLayout rlToolLayout;
    private TextView tvRightTitle;
    //是否仅仅只是作为展示用途
    private boolean mIsJustShow;

    /**
     * 跳转到该界面的公共方法
     * @param activity      发起跳转的Activity
     * @param dataList      数据List
     * @param startPosition 初始展示的位置
     * @param options       核心参数
     * @param requestCode   请求码
     */
    public static void start(Activity activity, ArrayList<MediaDataBean> dataList, int startPosition, PickerOptions options, int requestCode) {
        Intent intent = new Intent(activity, ImagePagerActivity.class);
        intent.putExtra(ImageContants.INTENT_KEY_START_POSITION, startPosition);
        intent.putExtra(ImageContants.INTENT_KEY_OPTIONS, options);
        intent.putExtra(ImageContants.INTENT_KEY_IS_PREVIEW, requestCode == ImageContants.REQUEST_CODE_PREVIEW);
        intent.putParcelableArrayListExtra(ImageContants.INTENT_KEY_DATA, dataList);
        intent.putExtra(ImageContants.INTENT_KEY_IS_JUST_SHOW,false);
        activity.startActivityForResult(intent, requestCode);
    }
    @Override
    protected void beforeSetContentView(Bundle savedInstanceState) {
        super.beforeSetContentView(savedInstanceState);
        Intent intent = getIntent();
        mCurPosition = intent.getIntExtra(ImageContants.INTENT_KEY_START_POSITION, 0);
        mDataList = intent.getParcelableArrayListExtra(ImageContants.INTENT_KEY_DATA);
        mOptions = intent.getParcelableExtra(ImageContants.INTENT_KEY_OPTIONS);
        mIsJustShow = intent.getBooleanExtra(ImageContants.INTENT_KEY_IS_JUST_SHOW,true);
    }
    @Override
    protected int getContentViewResId() {
        return R.layout.activity_image_pager;
    }
    @Override
    protected void initUI(View contentView) {
        mViewPager = findView(R.id.vp_image_pager);
        tvRightTitle = findViewById(R.id.toolbar_text_right);
        tvRightTitle.setTextColor(ContextCompat.getColor(this,R.color.image_text_color_blue));
        mCkSelected = findView(R.id.ck_image_pager);
        rlToolLayout = findViewById(R.id.rl_toolbar);
        if (rlToolLayout !=null){
            rlToolLayout.setVisibility(View.VISIBLE);
        }
        if (mOptions.getType() == ImagePickType.SINGLE) {
            setToolBarTitle("选择图片");
            tvRightTitle.setTextColor(ContextCompat.getColor(this,R.color.image_text_color_blue));
            mCkSelected.setVisibility(View.GONE);
            tvRightTitle.setText("确定");
        }
        if (mIsJustShow){
            mCkSelected.setVisibility(View.GONE);
            tvRightTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void initListener(){
        if (!mIsJustShow){
            findViewById(R.id.toolbar_text_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOptions.getType() == ImagePickType.SINGLE) {
                        MediaDataBean bean = mDataList.get(mCurPosition);
                        if (mOptions.isNeedCrop()) {
                                if (bean.getMediaPath().contains(".gif") || bean.getMediaPath().contains(".GIF")) {
                                    returnSingleImage(bean);
                                } else {
                                    //普通图片执行裁剪
                                    ImageCropActivity.start(ImagePagerActivity.this, bean.getMediaPath(), mOptions);
                                }
                            } else {
                                returnSingleImage(bean);
                            }
                    }else {
                        //返回上级界面选择完毕
                        setResult(ImageContants.RESULT_CODE_OK);
                        finish();
                    }
                }
            });
        }
    }


    @Override
    protected void initData() {
        mAdapter = new ImagePagerAdapter(this, mDataList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(mPageChangeListener);
        mViewPager.setCurrentItem(mCurPosition, false);
        mAdapter.setPhotoViewClickListener(new ImagePagerAdapter.PhotoViewClickListener() {
            @Override
            public void OnPhotoTapListener(View view, float v, float v1, int position) {
//                onImageSingleTap(position);
            }
        });
        updateCheckBoxStatus();
        updateRightText();
    }
    private ViewPager.SimpleOnPageChangeListener mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            mCurPosition = position;
            if (mDataList != null && position < mDataList.size()) {
                updateRightText();
                updateCheckBoxStatus();
            }
            Log.i("轮播详情图片类型==", mDataList.get(position).getType() + "");
        }
    };

    /**
     *    更新选中的数量
     */
    private void updateRightText() {
        if (mOptions.getType() == ImagePickType.SINGLE) return;
        int resultNum = 0;
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_VIDEO) {
            resultNum = ImageDataModel.getInstance().getmResultVideoList().size();
        }
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_IMAGE) {
            resultNum = ImageDataModel.getInstance().getmResultImageList().size();
        }
        setToolBarTitle(getString(R.string.imagepicker_pager_title_count
                , String.valueOf(mCurPosition + 1), String.valueOf(mDataList.size())));
        if (resultNum == 0) {
            tvRightTitle.setEnabled(false);
            tvRightTitle.setTextColor(ContextCompat.getColor(this,R.color.image_edit_text_hint));
            tvRightTitle.setText(getString(R.string.image_pager_next));
        } else {
            tvRightTitle.setEnabled(true);
            tvRightTitle.setTextColor(ContextCompat.getColor(this,R.color.image_text_color_blue));
            tvRightTitle.setText(getString(R.string.btn_image_pager_next, String.valueOf(resultNum)));
        }
    }

    private CompoundButton.OnCheckedChangeListener mCkChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (!ImageDataModel.getInstance().checkSelectedDataType(mDataList.get(mCurPosition).getType())) {
                    clickNoValid();
                    return;
                }
                if (!checkSelectedDataMax()){
                    clickNoValid();
                    return;
                }
                ImageDataModel.getInstance().addDataToResult(mDataList.get(mCurPosition));
            } else {
                ImageDataModel.getInstance().delDataFromResult(mDataList.get(mCurPosition));
                ImageDataModel.getInstance().checkSelectedDataIsNull();

            }
            updateRightText();
        }
    };
    /**
     * 点击无效处理
     */
    private void clickNoValid(){
        mCkSelected.setOnCheckedChangeListener(null);//取消监听，以免冲突
        mCkSelected.setChecked(false);
        mCkSelected.setOnCheckedChangeListener(mCkChangeListener);
    }
    //更新当前图片选中状态
    private void updateCheckBoxStatus() {
        if (mDataList == null || mDataList.size() == 0)return;
        if (mCkSelected != null) {
            mCkSelected.setOnCheckedChangeListener(null);//取消监听，以免冲突
            mCkSelected.setChecked(ImageDataModel.getInstance().hasDataInResult(mDataList.get(mCurPosition)));
            mCkSelected.setOnCheckedChangeListener(mCkChangeListener);
        }
    }
    /**
     * 检查选择数据类型最大值，保证在单一数据类型下的数据数量不会超值
     * @return 是否继续
     */
    public boolean checkSelectedDataMax() {
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_VIDEO) {
            int curNum = ImageDataModel.getInstance().getmResultVideoList().size();
            if (curNum == mOptions.getVideoMaxNum()) {
                showShortToast(getString(R.string.warning_video_picker_max_num, String.valueOf(mOptions.getVideoMaxNum())));
                return false;
            }
        }
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_IMAGE) {
            int curNum = ImageDataModel.getInstance().getmResultImageList().size();
            if (curNum == mOptions.getImageMaxNum()) {
                showShortToast(getString(R.string.warning_imagepicker_max_num, String.valueOf(mOptions.getVideoMaxNum())));
                return false;
            }
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImageContants.REQUEST_CODE_PREVIEW) {
            if (resultCode == ImageContants.RESULT_CODE_OK) {
                setResult(ImageContants.RESULT_CODE_OK);
                finish();
            } else {
                //从预览界面回来需要刷新视图
                updateCheckBoxStatus();
                updateRightText();
            }
        }
        //裁剪
        if (requestCode == ImageContants.REQUEST_CODE_CROP) {
            if (resultCode == ImageContants.RESULT_CODE_CROP_OK) {
                //裁剪成功返回数据
                String cropPath = data.getStringExtra(ImageContants.INTENT_KEY_CROP_PATH);
                returnSingleImage(getImageBeanByPath(cropPath));
            }else {
                finish();
            }
        }
    }
    /**
     * 根据新图片路径创建ImageBean
     * @param path 新图片路径
     * @return MediaDataBean
     */
    public MediaDataBean getImageBeanByPath(String path) {
        if (path == null || path.length() == 0)
            return null;

        try {
            File file = new File(path);

            MediaDataBean mediaDataBean = new MediaDataBean();
            mediaDataBean.setType(ImageContants.SELECTED_TYPE_IMAGE);
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
    //返回单张图片数据
    private void returnSingleImage(MediaDataBean mediaDataBean) {
        if (!ImageDataModel.getInstance().checkSelectedDataType(mediaDataBean.getType())) return;
        if (!checkSelectedDataMax()) return;
        ImageDataModel.getInstance().addDataToResult(mediaDataBean);
        setResult(ImageContants.RESULT_CODE_OK);
        finish();
    }
    //根据单击来隐藏/显示头部和底部的布局
    private void onImageSingleTap(int type) {
        if (rlToolLayout == null)
            return;
        if (rlToolLayout.getVisibility() == View.VISIBLE) {
            rlToolLayout.setAnimation(AnimationUtils.loadAnimation(this, com.laojiang.imagepickers.R.anim.imagepicker_actionbar_dismiss));
            rlToolLayout.setVisibility(View.GONE);
            //更改状态栏为透明
            CommonUtils.changeStatusBarColor(this, getResources().getColor(com.laojiang.imagepickers.R.color.imagepicker_transparent));
            //给最外层布局加上这个属性表示，Activity全屏显示，且状态栏被隐藏覆盖掉。
            if (Build.VERSION.SDK_INT >= 16)
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            rlToolLayout.setAnimation(AnimationUtils.loadAnimation(this, com.laojiang.imagepickers.R.anim.imagepicker_actionbar_show));
            rlToolLayout.setVisibility(View.VISIBLE);
            //改回状态栏颜色
            CommonUtils.changeStatusBarColor(this, getResources().getColor(com.laojiang.imagepickers.R.color.imagepicker_statusbar));
            //Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住
            if (Build.VERSION.SDK_INT >= 16)
                mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }


}

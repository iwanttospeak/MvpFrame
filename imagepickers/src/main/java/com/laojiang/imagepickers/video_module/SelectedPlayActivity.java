package com.laojiang.imagepickers.video_module;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.base.activity.NoduleBaseActivity;
import com.laojiang.imagepickers.data.ImageContants;
import com.laojiang.imagepickers.data.ImageDataModel;
import com.laojiang.imagepickers.data.ImagePickType;
import com.laojiang.imagepickers.data.PickerOptions;
import com.laojiang.imagepickers.data.MediaDataBean;
import com.laojiang.imagepickers.video_module.data.VideoModel;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

/**
 * 单独的视频播放页面
 */
public class SelectedPlayActivity extends NoduleBaseActivity {
    private static final String VIDEO_URL = "video_url";
    private static final String IMAGEBEAN = "imageBean";

    SampleVideoPlayer videoPlayer;
    OrientationUtils orientationUtils;
    private String videoStr;
    private MediaDataBean videoBean;
    private TextView tvRightText;
    private CheckBox mCkSelected;
    private PickerOptions mOptions;

    @Override
    protected int getContentViewResId() {
        return R.layout.base_activity_viewo_play;
    }
    /**
     * 选择视频
     */
    public static void start(Activity activity, MediaDataBean mediaDataBean, PickerOptions options, int requestCode) {
        Intent intent = new Intent(activity, SelectedPlayActivity.class);
        intent.putExtra(VIDEO_URL, mediaDataBean.getMediaPath());
        intent.putExtra(IMAGEBEAN, mediaDataBean);
        intent.putExtra(ImageContants.INTENT_KEY_OPTIONS, options);
        activity.startActivityForResult(intent, requestCode);
    }
    @Override
    protected void beforeSetContentView(Bundle savedInstanceState) {
        super.beforeSetContentView(savedInstanceState);
        Intent intent = getIntent();
        try {
            videoStr = intent.getStringExtra(VIDEO_URL);
            videoBean = intent.getParcelableExtra(IMAGEBEAN);
            mOptions = intent.getParcelableExtra(ImageContants.INTENT_KEY_OPTIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 隐藏标题栏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    @Override
    protected void initUI(View contentView) {
        tvRightText = (TextView) findView(R.id.toolbar_text_right);
        mCkSelected = findView(R.id.ck_image_pager);
        videoPlayer = (SampleVideoPlayer) findView(R.id.video_player);
    }
    /**
     *    更新选中的数量
     */
    private void updateRightText() {
        if (mOptions.getType() == ImagePickType.SINGLE){//单选
            tvRightText.setText("确定");
            tvRightText.setTextColor(ContextCompat.getColor(this,R.color.image_text_color_blue));
            mCkSelected.setVisibility(View.GONE);
        }
        if (mOptions.getType() != ImagePickType.MUTIL) return;
        int resultNum = 0;
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_VIDEO) {
            resultNum = ImageDataModel.getInstance().getmResultVideoList().size();
        }
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_IMAGE) {
            resultNum = ImageDataModel.getInstance().getmResultImageList().size();
        }
        if (resultNum == 0) {
            tvRightText.setText(getString(R.string.image_pager_next));
            tvRightText.setTextColor(ContextCompat.getColor(this,R.color.image_edit_text_hint));
            tvRightText.setEnabled(false);
        } else {
            tvRightText.setText(getString(R.string.btn_image_pager_next, String.valueOf(resultNum)));
            tvRightText.setTextColor(ContextCompat.getColor(this,R.color.image_text_color_blue));
            tvRightText.setEnabled(true);
        }

        if (ImageDataModel.getInstance().hasDataInResult(videoBean,videoBean.getMediaId())){
            mCkSelected.setChecked(true);
        }else {
            mCkSelected.setChecked(false);
        }
    }

    @Override
    protected void initData() {
        updateRightText();

//        String url = "https://res.exexm.com/cw_145225549855002";
//        String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
        VideoModel videoModel;
        if (StringUtils.isSpace(videoStr))return;

        videoModel = new VideoModel(videoStr);
        videoPlayer.setUp(videoModel, true, "");
        videoPlayer.loadCoverImage(videoStr,R.drawable.image_default_image_square);
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        videoPlayer.startPlayLogic();
    }

    private CompoundButton.OnCheckedChangeListener mCkChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (!ImageDataModel.getInstance().checkSelectedDataType(videoBean.getType())) {
                    mCkSelected.setChecked(false);
                    return;
                }
                if (!checkSelectedDataMax()){
                    mCkSelected.setChecked(false);
                    return;
                }
                ImageDataModel.getInstance().addDataToResult(videoBean);
            } else {
                ImageDataModel.getInstance().delDataFromResult(videoBean);
                ImageDataModel.getInstance().checkSelectedDataIsNull();
            }
            updateRightText();
        }
    };
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
        return true;
    }
    @Override
    protected void initListener() {
        tvRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickComplete();
            }
        });
        if (mCkSelected !=null){
            mCkSelected.setOnCheckedChangeListener(mCkChangeListener);
        }
    }
    /**
     * 选择视频完成
     */
    private void onClickComplete() {
        if (mOptions.getType() == ImagePickType.MUTIL){//多选可以点击说明已经有数据了
            returnResult();
            return;
        }
        if (ImageDataModel.getInstance().hasDataInResult(videoBean,videoBean.getMediaId())) {
            returnResult();
            return;
        }
        if (!ImageDataModel.getInstance().checkSelectedDataType(videoBean.getType())) return;
        if (!checkSelectedDataMax()) return;
        ImageDataModel.getInstance().addDataToResult(videoBean);
        returnResult();
    }
    public void returnResult(){
        setResult(ImageContants.RESULT_CODE_OK);
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //释放所有
        videoPlayer.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        super.onBackPressed();
    }


}

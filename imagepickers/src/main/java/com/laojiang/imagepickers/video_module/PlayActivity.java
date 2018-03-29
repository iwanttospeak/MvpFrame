package com.laojiang.imagepickers.video_module;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.base.activity.NoduleBaseActivity;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

/**
 * 单独的视频播放页面
 */
public class PlayActivity extends NoduleBaseActivity {
    private static final String VIDEO_URL = "video_url";
    private static final String IMAGEBEAN = "imageBean";

    protected SampleCoverVideo videoPlayer;
    protected OrientationUtils orientationUtils;
    protected String videoUrl;
    private RelativeLayout rlToolLayout;
    @Override
    protected int getContentViewResId() {
        return R.layout.image_activity_view_play;
    }
    /**
     * 播放视频
     */
    public static void start(Activity activity, String videoStr) {
        Intent intent = new Intent(activity, PlayActivity.class);
        intent.putExtra(VIDEO_URL, videoStr);
        activity.startActivity(intent);
    }
    @Override
    protected void initUI(View contentView) {
        rlToolLayout = (RelativeLayout) findView(R.id.rl_toolbar);
        videoPlayer = (SampleCoverVideo) findView(R.id.video_player);
    }

    @Override
    protected void beforeSetContentView(Bundle savedInstanceState) {
        super.beforeSetContentView(savedInstanceState);
        Intent intent = getIntent();
        try {
            videoUrl = intent.getStringExtra(VIDEO_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void initData() {
//        String url = "https://res.exexm.com/cw_145225549855002";
//        String source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4";
//        VideoModel videoModel;
//        if (StringUtils.isSpace(videoStr))return;
//
//        videoModel = new VideoModel(videoStr);
//        videoPlayer.setUp(videoModel, true, "测试视频");
//        //增加title
//        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
//        //暂停锁定效果
////        videoPlayer.setShowPauseCover(false);
//        //显示返回键
//        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
//        //设置旋转
//        orientationUtils = new OrientationUtils(this, videoPlayer);
//        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
//        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                orientationUtils.resolveByClick();
//            }
//        });
//        //是否可以滑动调整
//        videoPlayer.setIsTouchWiget(true);
//        //设置返回按键功能
//        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        videoPlayer.startPlayLogic();
    }

    @Override
    protected void initListener() {

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
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setStandardVideoAllCallBack(null);
        GSYVideoPlayer.releaseAllVideos();
        super.onBackPressed();
    }

}

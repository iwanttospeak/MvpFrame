package com.laojiang.imagepickers.video_module;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.video_module.data.VideoModel;
import com.shuyu.gsyvideoplayer.listener.GSYVideoShotListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import java.io.File;

import moe.codeest.enviews.ENDownloadView;

/**
 * 负责视频的播放处理，不带封面
 * 这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
 * xml中的一些布局信息在 GSYVideoControlView 中进行解析，但是布局中的名字是约定的名字
 */
public class SampleVideoPlayer extends StandardGSYVideoPlayer {

    private VideoModel mVideoModel;
    ImageView mCoverImage;
    String mCoverOriginUrl;

    int mDefaultRes;

    boolean isShowTop;
    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public SampleVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SampleVideoPlayer(Context context) {
        super(context);
    }

    public SampleVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }
    private void initAttrs(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet,R.styleable.VideoPlayer);
        initPointView(typedArray);
        typedArray.recycle();
    }

    private void initPointView(TypedArray typedArray) {
        isShowTop = typedArray.getBoolean(R.styleable.VideoPlayer_isShowTop,true);
    }
    @Override
    public int getLayoutId() {
        return R.layout.image_sample_video;
    }
    @Override
    protected void init(Context context) {
        super.init(context);
        mCoverImage = (ImageView) findViewById(R.id.thumbImage);

    }
    public void loadCoverImage(String url, int res) {
        mCoverOriginUrl = url;
        mDefaultRes = res;
        Glide.with(getContext())
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .fitCenter()
                                .error(res)
                                .placeholder(res))
                .load(url)
                .into(mCoverImage);
    }


    /**
     * 需要在尺寸发生变化的时候重新处理
     */
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        super.onSurfaceTextureSizeChanged(surface, width, height);
        resolveTransform();
    }

    /**
     * 处理镜像旋转
     * 注意，暂停时
     */
    protected void resolveTransform() {
        Matrix transform = new Matrix();
        transform.setScale(1, 1, mTextureView.getWidth() / 2, 0);
        mTextureView.setTransform(transform);
        mTextureView.invalidate();
    }
    /**
     * 设置播放URL
     * @param videoModel           播放数据
     * @param cacheWithPlay 是否边播边缓存
     * @param title         title
     */
    public boolean setUp(VideoModel videoModel, boolean cacheWithPlay, String title) {
        this.mVideoModel = videoModel;
        return setUp(videoModel.getUrl(), cacheWithPlay, title);
    }
    /**
     * 设置播放URL
     * @param videoModel          播放数据
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param title         title
     */
    public boolean setUp(VideoModel videoModel, boolean cacheWithPlay, File cachePath, String title) {
        this.mVideoModel = videoModel;
        return setUp(videoModel.getUrl(), cacheWithPlay, cachePath, title);
    }
    /**
     * 全屏时将对应处理参数逻辑赋给全屏播放器
     */
    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        SampleVideoPlayer sampleVideoPlayer = (SampleVideoPlayer) super.startWindowFullscreen(context, actionBar, statusBar);
        sampleVideoPlayer.mVideoModel = mVideoModel;
        //sampleVideo.resolveTransform();
        sampleVideoPlayer.loadCoverImage(mCoverOriginUrl, mDefaultRes);
        sampleVideoPlayer.resolveTypeUI();
//        sampleVideo.resolveRotateUI();
        //这个播放器的demo配置切换到全屏播放器
        //这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
        //比如已旋转角度之类的等等
        //可参考super中的实现
        return sampleVideoPlayer;
    }
    /**
     * 推出全屏时将对应处理参数逻辑返回给非播放器
     */
    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
        if (gsyVideoPlayer != null) {
            SampleVideoPlayer sampleVideoPlayer = (SampleVideoPlayer) gsyVideoPlayer;
            setUp(mVideoModel, mCache, mCachePath, mTitle);
            resolveTypeUI();
        }
    }

    /**
     * 处理显示逻辑
     */
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        super.onSurfaceTextureAvailable(surface, width, height);
        resolveRotateUI();
        resolveTransform();
    }
    /**
     * 旋转逻辑
     */
    private void resolveRotateUI() {
        if (!mHadPlay) {
            return;
        }
        mTextureView.setRotation(mRotate);
        mTextureView.requestLayout();
    }

    /**
     * 显示比例
     * 注意，GSYVideoType.setShowType是全局静态生效，除非重启APP。
     */
    private void resolveTypeUI() {
        if (!mHadPlay) {
            return;
        }
        //显示比例
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT);
        //调整TextureView去适应比例变化
        changeTextureViewShowType();
        if (mTextureView != null)
            mTextureView.requestLayout();
    }
    //播放状态改变UI
    @Override
    protected void hideAllWidget() {
        super.hideAllWidget();
        setViewShowState(mTopContainer, VISIBLE);
    }
    @Override
    protected void changeUiToNormal() {
        super.changeUiToNormal();
        setViewShowState(mTopContainer, VISIBLE);
    }
    @Override
    protected void changeUiToPreparingShow() {
        super.changeUiToPreparingShow();
        setViewShowState(mTopContainer, VISIBLE);
    }
    @Override
    protected void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
        setViewShowState(mTopContainer, VISIBLE);
    }
    @Override
    protected void changeUiToPauseShow() {
        super.changeUiToPauseShow();
        setViewShowState(mTopContainer, VISIBLE);
    }
    @Override
    protected void changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow();
        setViewShowState(mTopContainer, VISIBLE);
    }
    @Override
    protected void changeUiToCompleteShow() {
        super.changeUiToCompleteShow();
        setViewShowState(mTopContainer, VISIBLE);
    }
    @Override
    protected void changeUiToError() {
        super.changeUiToError();
        setViewShowState(mTopContainer, VISIBLE);
    }
    protected void changeUiToPrepareingClear() {
        super.changeUiToPrepareingClear();
        setViewShowState(mTopContainer, VISIBLE);
    }
    protected void changeUiToPlayingBufferingClear() {
        super.changeUiToPlayingBufferingClear();
        setViewShowState(mTopContainer, VISIBLE);
    }
    protected void changeUiToClear() {
        super.changeUiToClear();
        setViewShowState(mTopContainer, VISIBLE);
    }
    protected void changeUiToCompleteClear() {
        super.changeUiToCompleteClear();
        setViewShowState(mTopContainer, VISIBLE);
    }
}

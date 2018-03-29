package com.laojiang.imagepickers.video_module;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.laojiang.imagepickers.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

/**
 * 带封面
 * Created by guoshuyu on 2017/9/3.
 */

public class SampleCoverVideo extends StandardGSYVideoPlayer {

    ImageView mCoverImage;

    String mCoverOriginUrl;

    int mDefaultRes;

    public SampleCoverVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SampleCoverVideo(Context context) {
        super(context);
    }

    public SampleCoverVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mCoverImage = (ImageView) findViewById(R.id.thumbImage);
    }

    @Override
    public int getLayoutId() {
        return R.layout.image_video_layout_cover;
    }


    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
        SampleCoverVideo sampleCoverVideo = (SampleCoverVideo) gsyBaseVideoPlayer;
        sampleCoverVideo.loadCoverImage(mCoverOriginUrl, mDefaultRes);
        return gsyBaseVideoPlayer;
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
}

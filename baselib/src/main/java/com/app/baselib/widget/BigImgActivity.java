package com.app.baselib.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.baselib.R;
import com.app.baselib.Utils.ImageDisplayUtil;
import com.app.baselib.adapter.BigPicAdapter;
import com.app.baselib.mvp.BaseActivity;
import com.laojiang.imagepickers.utils.CommonUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;

/**
 * 查看大图
 */
public class BigImgActivity extends BaseActivity {


    private ArrayList<String> imgs;
    private BigPicAdapter bigImgAdapter;
    private int position =0;
    private RelativeLayout rlToolLayout;
    private Banner banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        CommonUtils.changeStatusBarColor(this, getResources().getColor(R.color.base_bg_activity));
        initStateTextColorBlack(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_activity_big_pic;
    }

    @Override
    public void init() {
        banner = findViewById(R.id.img_banner);
        rlToolLayout = findViewById(R.id.rl_head);
        rlToolLayout.setBackgroundColor(getResources().getColor(R.color.base_transparent));
        initData();

        onImageSingleTap();
    }

    public void initData() {
        imgs = (ArrayList<String>) getIntent().getSerializableExtra("imgs");
        if (imgs ==null ||  imgs.size() == 0){
            return;
        }
        setBanner();
    }

    public void setBanner() {
        //设置指示器样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImages(imgs);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                killMySelf();
                onImageSingleTap();
            }
        });
        banner.isAutoPlay(false);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        banner.start();
    }
    /**
     * 图片加载器
     **/
    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            String imgPath = (String) path;
            ImageDisplayUtil.displayImage(imageView,imgPath);
        }
    }
    //根据单击来隐藏/显示头部和底部的布局
    private void onImageSingleTap() {
        if (rlToolLayout == null)
            return;
        if (rlToolLayout.getVisibility() == View.VISIBLE) {
            rlToolLayout.setAnimation(AnimationUtils.loadAnimation(this, com.laojiang.imagepickers.R.anim.imagepicker_actionbar_dismiss));
            rlToolLayout.setVisibility(View.GONE);
            //设置全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            //更改状态栏为透明
//            CommonUtils.changeStatusBarColor(this, getResources().getColor(com.laojiang.imagepickers.R.color.imagepicker_transparent));
        } else {
            //取消全屏
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            CommonUtils.changeStatusBarColor(this, getResources().getColor(com.laojiang.imagepickers.R.color.imagepicker_transparent));
            rlToolLayout.setAnimation(AnimationUtils.loadAnimation(this, com.laojiang.imagepickers.R.anim.imagepicker_actionbar_show));
            rlToolLayout.setVisibility(View.VISIBLE);
        }
    }
}

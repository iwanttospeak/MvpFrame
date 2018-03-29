package com.app.baselib.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.baselib.R;
import com.app.baselib.Utils.ImageDisplayUtil;
import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;

import java.util.List;


/**
 * Created by wang
 */
public class BigPicAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> imgs;

    public BigPicAdapter(Context context, List<String> imgs) {
        this.mContext = context;
        this.imgs = imgs;
    }

    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        String imgUrl = imgs.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.base_item_big_pic, null);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);

        photoView.enable();
        ImageDisplayUtil.displayImage(photoView,imgUrl);
        photoView.setOnClickListener(v -> ((Activity) mContext).finish());
        container.addView(view);
        return view;
    }
}

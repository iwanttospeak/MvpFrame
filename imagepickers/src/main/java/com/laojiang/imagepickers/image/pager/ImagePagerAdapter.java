package com.laojiang.imagepickers.image.pager;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.data.ImageDataModel;
import com.laojiang.imagepickers.data.MediaDataBean;
import com.laojiang.imagepickers.utils.CommonUtils;

import java.util.ArrayList;

/**
 *  滑动查看图片的适配器
 */
public class ImagePagerAdapter extends PagerAdapter {
    private int mScreenWidth;
    private int mScreenHeight;
    private ArrayList<MediaDataBean> mAllmageList = new ArrayList<>();
    private Activity mActivity;
    public PhotoViewClickListener mListener;

    public ImagePagerAdapter(Activity activity, ArrayList<MediaDataBean> images) {
        this.mActivity = activity;
        this.mAllmageList.addAll(images);

        mScreenWidth = CommonUtils.getScreenWidth(activity);
        mScreenHeight = CommonUtils.getScreenHeight(activity);
    }

    public void setData(ArrayList<MediaDataBean> images) {
        mAllmageList.clear();
        this.mAllmageList.addAll(images);
    }

    public void setPhotoViewClickListener(PhotoViewClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.image_item_big_pic, null);
        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        photoView.setEnabled(true);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (mListener != null)
                    mListener.OnPhotoTapListener(view, x, y,position);
            }
        });
        MediaDataBean imageItem = mAllmageList.get(position);
        //加载图片
        ImageDataModel.getInstance().getDisplayer().display(mActivity, imageItem.getMediaPath(), photoView);
        Log.i("每个图片的属性==",mAllmageList.get(position).getType()+"");
        container.addView(view);
        return view;
    }

    @Override
    public int getCount()
    {
        return mAllmageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }

    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1, int position);
    }
}


package com.laojiang.imagepickers.image.grid.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.base.adapter.IImagePickerItemView;
import com.laojiang.imagepickers.base.adapter.ImagePickerViewHolder;
import com.laojiang.imagepickers.data.PickerOptions;
import com.laojiang.imagepickers.image.grid.view.IImageDataView;


/**
 *  相机入口ItemView
 */
public class ImageCameraItemView implements IImagePickerItemView {
    private IImageDataView mViewImpl;
    private PickerOptions mOptions;

    public ImageCameraItemView(IImageDataView viewImpl) {
        this.mViewImpl = viewImpl;
        this.mOptions = mViewImpl.getOptions();
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.layout_image_data_camera_listitem;
    }

    @Override
    public boolean isForViewType(Object item, int position) {
        return mOptions != null && mOptions.isNeedCamera() && position == 0;
    }

    @Override
    public void setData(ImagePickerViewHolder holder, Object o, int position, ViewGroup parent) {
        holder.setClickListener(R.id.fl_image_data_camera, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mViewImpl.startTakePhoto();
            }
        });
    }
}

package com.laojiang.imagepickers.image.grid.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.base.adapter.IImagePickerItemView;
import com.laojiang.imagepickers.base.adapter.ImagePickerViewHolder;
import com.laojiang.imagepickers.data.MediaDataBean;
import com.laojiang.imagepickers.data.ImageContants;
import com.laojiang.imagepickers.data.ImageDataModel;
import com.laojiang.imagepickers.data.ImagePickType;
import com.laojiang.imagepickers.data.PickerOptions;
import com.laojiang.imagepickers.image.grid.view.IImageDataView;


/**
 *  显示图片的GridItem
 */
public class ImageContentItemView implements IImagePickerItemView<MediaDataBean> {
    private IImageDataView mViewImpl;
    private PickerOptions mOptions;
    private ImageDataAdapter mAdapter;

    public ImageContentItemView(IImageDataView viewImpl, ImageDataAdapter adapter) {
        this.mViewImpl = viewImpl;
        this.mOptions = mViewImpl.getOptions();
        this.mAdapter = adapter;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.layout_image_data_content_listitem;
    }

    @Override
    public boolean isForViewType(MediaDataBean item, int position) {
        return mOptions != null && (!mOptions.isNeedCamera() || (mOptions.isNeedCamera() && position != 0));
    }

    @Override
    public void setData(ImagePickerViewHolder holder, final MediaDataBean mediaDataBean, final int position, ViewGroup parent) {
        ImageView imgContent = holder.findView(R.id.img_imagepicker_grid_content);
        FrameLayout flIndicator =  holder.findView(R.id.fl_imagepicker_grid_content);
        View viewIndicator = holder.findView(R.id.ck_imagepicker_grid_content);

        //显示UI
        if (mediaDataBean != null)
            ImageDataModel.getInstance().getDisplayer()
                    .display(holder.getContext(), mediaDataBean.getMediaPath(), imgContent
                            , R.drawable.glide_default_picture, R.drawable.glide_default_picture
                            , ImageContants.DISPLAY_THUMB_SIZE, ImageContants.DISPLAY_THUMB_SIZE);
        //判断照片还是视频
        imgContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewImpl != null)
                    mViewImpl.onImageClicked(mediaDataBean, position);
            }
        });

        if (mOptions.getType() == ImagePickType.SINGLE) {
            flIndicator.setVisibility(View.GONE);
        } else {
            flIndicator.setVisibility(View.VISIBLE);
            if (ImageDataModel.getInstance().hasDataInResult(mediaDataBean))
                viewIndicator.setBackgroundResource(R.drawable.image_selected_on);
            else
                viewIndicator.setBackgroundResource(R.drawable.image_selected_pic_off);
            flIndicator.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ImageDataModel.getInstance().hasDataInResult(mediaDataBean)){ //delete
                            ImageDataModel.getInstance().delDataFromResult(mediaDataBean);
                            checkSelectedDataIsNull();
                        } else{//add
                            if (!ImageDataModel.getInstance().checkSelectedDataType(mediaDataBean.getType())){
                                return;
                            }
                            if (!checkSelectedDataMax()){
                                return;
                            }
                            ImageDataModel.getInstance().addDataToResult(mediaDataBean);
                        }
                        mAdapter.notifyDataSetChanged();
                        onSelectedNumberChange();
                    }
                });
        }
        if (mOptions.getType() == ImagePickType.SINGLE)
            flIndicator.setVisibility(View.GONE);
        else
            flIndicator.setVisibility(View.VISIBLE);
    }
    /**
     * 通知选中数量发生变化,当没有数据的时候通知数量为0
     */
    public void onSelectedNumberChange() {
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_VIDEO) {
            mViewImpl.onSelectNumChanged(ImageDataModel.getInstance().getmResultVideoList().size());
            return;
        }
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_IMAGE) {
            mViewImpl.onSelectNumChanged(ImageDataModel.getInstance().getmResultImageList().size());
            return;
        }
        mViewImpl.onSelectNumChanged(0);
    }

    /**
     * 检查选择数据类型最大值，保证在单一数据类型下的数据数量不会超值
     * @return 是否继续
     */
    public boolean checkSelectedDataMax() {
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_VIDEO) {
            int curNum = ImageDataModel.getInstance().getmResultVideoList().size();
            if (curNum == mOptions.getVideoMaxNum()) {
                mViewImpl.warningVideoMaxNum();
                return false;
            }
        }
        if (ImageContants.CURRENT_SELECTED_TYPE ==ImageContants.SELECTED_TYPE_IMAGE) {
            int curNum = ImageDataModel.getInstance().getmResultImageList().size();
            if (curNum == mOptions.getImageMaxNum()) {
                mViewImpl.warningImageMaxNum();
                return false;
            }
        }
        return true;
    }

    /**
     * 检查选择数据是否为null
     */
    private void checkSelectedDataIsNull() {
        if (ImageDataModel.getInstance().getmResultVideoList().size() == 0
                && ImageDataModel.getInstance().getmResultImageList().size() == 0) {
            ImageContants.CURRENT_SELECTED_TYPE = -1;
        }
    }
}

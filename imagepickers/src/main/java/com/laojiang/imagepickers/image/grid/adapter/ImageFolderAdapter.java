package com.laojiang.imagepickers.image.grid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.base.adapter.IImagePickerItemView;
import com.laojiang.imagepickers.base.adapter.ImagePickerBaseAdapter;
import com.laojiang.imagepickers.base.adapter.ImagePickerViewHolder;
import com.laojiang.imagepickers.data.ImageDataModel;
import com.laojiang.imagepickers.data.ImageFolderBean;
/**
 *  文件夹适配器
 */
public class ImageFolderAdapter extends ImagePickerBaseAdapter<ImageFolderBean> {
    private int mCurFolderPosition;

    public ImageFolderAdapter(Context context, int position) {
        super(context, ImageDataModel.getInstance().getAllFloderList());
        this.mCurFolderPosition = position;
        addItemView(new ImageFolderItemView());
    }

    private class ImageFolderItemView implements IImagePickerItemView<ImageFolderBean> {

        @Override
        public int getItemViewLayoutId()
        {
            return R.layout.layout_image_floder_listitem;
        }

        @Override
        public boolean isForViewType(ImageFolderBean item, int position)
        {
            return true;
        }

        @Override
        public void setData(ImagePickerViewHolder holder, ImageFolderBean imageFolderBean, int position, ViewGroup parent) {
            ImageView imgFirst = holder.findView(R.id.img_floder_listitem_firstImg);

            if (imageFolderBean != null) {
                ImageDataModel.getInstance().getDisplayer().display(holder.getContext()
                        , imageFolderBean.getFirstImgPath(), imgFirst,
                        R.drawable.glide_default_picture, R.drawable.glide_default_picture,
                        300, 300);
                holder.setTvText(R.id.tv_floder_pop_listitem_name, imageFolderBean.getFloderName());
                holder.setTvText(R.id.tv_floder_pop_listitem_num ,holder.getContext().getResources().getString(R.string.imagepicker_floder_num
                                , String.valueOf(imageFolderBean.getNum())));
                if (position == mCurFolderPosition)
                    holder.setVisibility(R.id.img_floder_pop_listitem_selected, View.VISIBLE);
                else
                    holder.setVisibility(R.id.img_floder_pop_listitem_selected, View.GONE);
            }
        }
    }
}

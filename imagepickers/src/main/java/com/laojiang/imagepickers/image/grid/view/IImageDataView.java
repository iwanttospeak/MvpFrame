package com.laojiang.imagepickers.image.grid.view;


import com.laojiang.imagepickers.base.activity.IImageBaseView;
import com.laojiang.imagepickers.data.MediaDataBean;
import com.laojiang.imagepickers.data.ImageFolderBean;
import com.laojiang.imagepickers.data.PickerOptions;

import java.util.List;

/**
 *
 *  ImageDataActivity的View层接口
 */

public interface IImageDataView extends IImageBaseView {
    PickerOptions getOptions();

    void startTakePhoto();

    void showLoading();

    void hideLoading();

    void onDataChanged(List<MediaDataBean> dataList);

    void onFolderChanged(ImageFolderBean floderBean);

    void onImageClicked(MediaDataBean mediaDataBean, int position);

    void onSelectNumChanged(int curNum);

    void warningImageMaxNum();
    void warningVideoMaxNum();

}

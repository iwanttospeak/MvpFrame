package com.laojiang.imagepickers.image.grid.view;

import com.laojiang.imagepickers.data.MediaDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by Wang on 2018/1/29.
 * 拍照结果监听接口
 */
public interface TakeResultListener {
    void takeSuccess(ArrayList<MediaDataBean> result);

    void takeFail(List<MediaDataBean> result, String msg);

    void takeCancel();
}
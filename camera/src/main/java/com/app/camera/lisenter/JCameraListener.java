package com.app.camera.lisenter;

import android.graphics.Bitmap;

/**
 * =====================================
 * 作    者: 陈嘉桐
 * 版    本：1.1.4
 * 创建日期：2017/4/26
 * 描    述：
 * =====================================
 */
public interface JCameraListener {
    //拍照成功，不设置地址，保存位置由用户指定
    void captureSuccess(Bitmap bitmap);

    void recordSuccess(String url, Bitmap firstFrame);

}

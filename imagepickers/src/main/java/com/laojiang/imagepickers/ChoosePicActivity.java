package com.laojiang.imagepickers;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.laojiang.imagepickers.compress.CompressConfig;
import com.laojiang.imagepickers.data.MediaDataBean;
import com.laojiang.imagepickers.image.grid.view.ImageDataActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择图片
 */
public class ChoosePicActivity extends ImageDataActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //压缩配置,暂不使用
        CompressConfig config = new CompressConfig.Builder()
                .setMaxSize(1280 * 720)
                .setMaxPixel(1280)
                .create();
        takePhoto();
    }

    private void takePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
    }


    @Override
    public void takeSuccess(ArrayList<MediaDataBean> result) {
        List<String> resultList = new ArrayList<>();
        for (MediaDataBean bean : result){
            resultList.add(bean.getMediaPath());
        }
        ChoosePicDialog.setResult(resultList);
        finish();
    }

    @Override
    public void takeFail(List<MediaDataBean> result, String msg) {
        Toast.makeText(this, "拍照失败", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void takeCancel() {

    }

}

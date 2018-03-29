package com.yzl.moduleforum.ui.release_dynamic;

import android.app.Activity;
import android.content.Intent;

import com.app.baselib.mvp.BasePresent;
import com.app.baselib.mvp.BaseView;
import com.yzl.appres.adapter.DynamicReleaseContentGridAdapter;

import io.reactivex.Observable;


public interface ReleaseDynamicContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {

        void setContentAdapter(DynamicReleaseContentGridAdapter adapter);
        Activity getActivity();
        void choosePicture(int maxNumber);
        void notifyReleaseButtonState();
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    abstract class ReleasePresent<T> extends BasePresent<T> {

        public abstract boolean isHasUpdateInfo(String path);
    }

}

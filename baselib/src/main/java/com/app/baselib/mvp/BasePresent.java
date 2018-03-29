package com.app.baselib.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.app.baselib.R;
import com.app.baselib.http.Api;
import com.app.baselib.http.NetRequest;
import com.app.baselib.widget.state.StateUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;


/**
 * 泛型必须为activity或者fragment或者view的子类.
 * @author by Wang on 2017/7/15.
 */
public abstract class BasePresent<T> {

    protected T mView;
    protected Context mContext;

    protected AppCompatActivity mActivity;
    /**
     * 网络对象.
     */
    protected NetRequest mNetRequest;

    /*---------------------------------------基本方法，除了base不允许重写以及调用------------------------------------------------*/

    /**
     * 注入view.
     */
    public final void attach(T view) {
        this.mView = view;
        getContext();
    }

    public final void init(StateUtils stateUtils) {
        initNetRequest(stateUtils);
    }
    /**
     * 获取context.
     */
    private void getContext() {
        if (mView instanceof BaseActivity) {
            mContext = (Context) mView;
            mActivity = (AppCompatActivity) mView;
        } else if (mView instanceof BaseFragment) {
            mContext = ((BaseFragment) mView).getActivity();
            mActivity = (AppCompatActivity) ((BaseFragment) mView).getActivity();
        } else if (mView instanceof View) {
            mContext = ((View) mView).getContext();
        } else {
            throw new IllegalStateException("view must instanceof activity or fragment");
        }
    }

    /**
     * 销毁操作.
     */
    public  void detach() {
        if (useEventBus())
            EventBus.getDefault().unregister(this);//解除注册 Eventbus
        mContext = null;
        mView = null;
        if (mNetRequest != null) {
            mNetRequest.onDestroy();
        }
    }
    /**
     * 是否使用 {@link EventBus},默认为不使用(false)，
     *
     */
    public boolean useEventBus() {
        return false;
    }
    /*----------------------------------需要重写的方法---------------------------------------------------*/

    /**
     * 实例化开始执行方法（onCreate方法中layout设置完成后执行）.
     */
    public void start() {
        if (useEventBus())//如果要使用 Eventbus 请将此方法返回 true
            EventBus.getDefault().register(this);
    }

    /**
     * 当fragment可见后执行的事件(包括可见状态下经历resume).
     */
    public void setUserVisibleEvent() {
    }

    /**
     * 初始化对象.
     */
    private void initNetRequest(StateUtils stateUtils) {
        mNetRequest = new NetRequest(mContext);
        if (stateUtils != null) {
            mNetRequest.setStateUtils(stateUtils);
        }
    }

    /**
     * 显示toast.
     *
     * @param str 需要显示的文字
     */
    public final void showToast(String str) {
        Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }

    public final void showToast(int strId) {
        Toast.makeText(mContext, mContext.getString(strId), Toast.LENGTH_SHORT).show();
    }
    /**
     *  因为Retrofit强制只能反射的APIServer类只能是接口，而且不能进行继承，
     * 所以这里不能设置K继承什么基础接口之类的
     */
    public  <K> K getApiService(Class<K> serviceClass){
        return Api.getInstance().mRetrofit.create((serviceClass));
    }
}

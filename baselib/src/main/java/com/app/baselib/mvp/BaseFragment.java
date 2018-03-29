package com.app.baselib.mvp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.app.baselib.R;
import com.app.baselib.Utils.GenericityUtils;
import com.app.baselib.widget.LoadingDialog;
import com.app.baselib.widget.state.StateUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 * Created by wang on 2017/7/5.
 */

public abstract class BaseFragment<T extends BasePresent> extends Fragment {

    public T mPresent;
    public View mView;

    private Unbinder bind;

    private boolean isVisibleToUser;
    /**
     * 这个变量标识fragment的状态
     **/
    protected boolean isVisible;

    /**
     * 标志位，标志已经初始化完成
     **/
    protected boolean isPrepared;
    /**
     * 多状态.
     */
    protected StateUtils mModeStateView;

    /**
     * 是否显示多状态.
     */
    private boolean isShowStateView = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //提供两种设置布局的方式，一种使用注释，一种覆写方法，默认使用注释
        mView = inflater.inflate(getLayoutId(), container, false);
        bind = ButterKnife.bind(this, mView);
        initPresent();
        isPrepared = true;
        initStateView();
        lazyLoad();
        if (mPresent != null) mPresent.start();
        return mView;
    }

    private void initPresent() {
        GenericityUtils utils = new GenericityUtils();
        Class clazz = utils.getGenericClass(this);
        if (clazz == null) return;

        utils.checkGenericity(clazz, getClass().getName());
        mPresent = (T) utils.instantiationClass(clazz);
        mPresent.attach(this);
        mPresent.init(mModeStateView);
    }
    /**
     * 初始化多状态列表.
     */
    private void initStateView() {
        if (mModeStateView == null && isShowStateView) {
            mModeStateView = new StateUtils(mView,getActivity().findViewById(R.id.tool) != null);
        }
    }
    /**
     * fragment的懒加载，当fragment初始化完成并且可见的时候去加载数据
     * 但是注意首次fragment初始化的时候并不会调用setUserVisibleHint方法，
     * 因此如果一个activity里面只有一个fragment，要在调用此方法之前设置isVisible为true
     */
    public void lazyLoad() {
//        if (isPrepared && isVisible) {
            init();
//        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && isAdded() && mPresent != null) mPresent.setUserVisibleEvent();
        isVisible = isVisibleToUser;
        if (isPrepared && isVisible) {
            lazyLoad();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisibleToUser && isAdded() && mPresent != null) mPresent.setUserVisibleEvent();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresent != null) mPresent.detach();
        bind.unbind();
    }

    public void showProcessDialog(){
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity!=null) activity.showProcessDialog();
    }
    public void dismissProcessDialog(){
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity!=null) activity.dismissProcessDialog();
    }

    /*-----------------------------------------需要重写的方法-------------------------------------------------*/

    /**
     * 设置好present后执行的事.
     */
    public abstract void init();

    /**
     * 设置布局文件.
     */
    @LayoutRes
    public abstract int getLayoutId();
}

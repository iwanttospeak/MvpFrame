package com.app.baselib.widget.state;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.app.baselib.R;


/**
 * 网络加载状态工具类，可以实现加载动画，无网络点击重新加载等
 * Created by wang
 */

public class StateUtils {

    private StateView mStateView;
    /**
     * 是否显示了其他页面.
     */
    private boolean showOtherPager;

    public StateUtils(Activity activity, boolean hasActionBar) {
        mStateView = StateView.inject(activity, hasActionBar);
        mStateView.setLoadingResource(R.layout.view_loading);
    }

    public StateUtils(View view, boolean hasActionBar) {
        mStateView = StateView.inject(view, hasActionBar);
//        mStateView.setLoadingResource(R.layout.base_state_search_loading);
    }

    public void setLoadingLayout(@LayoutRes int loadingLayout) {
        mStateView.setLoadingResource(loadingLayout);
    }

    public void setEmptyLayout(@LayoutRes int emptyLayout) {
        mStateView.setEmptyResource(emptyLayout);
    }

    public void setRetryLayout(@LayoutRes int retryLayout) {
        mStateView.setRetryResource(retryLayout);
    }
    /**
     * 初始化状态.
     */
    public void initState() {
        showOtherPager = false;
    }

    /**
     * 显示加载.
     */
    public void showLoading() {
        showOtherPager = false;
        mStateView.showLoading();
    }

    /**
     * 显示数据为空.
     */
    public void showEmpty() {
        showOtherPager = true;
        mStateView.showEmpty();
    }

    /**
     * 显示内容.
     */
    public void showContent() {
        if (!showOtherPager) {
            mStateView.showContent();
        }
    }

    /**
     * 显示出错重试.
     */
    public void showErrorRetry() {
        showOtherPager = true;
        mStateView.showRetry();
    }

    /**
     * 出错重试事件.
     */
    public void setOnRetryListener(OnRetryListener listener) {
        mStateView.setOnRetryClickListener(listener::onRetry);
    }

    public interface OnRetryListener {
        public void onRetry();
    }

    public View getEmptyView() {
        return mStateView.getEmptyView();
    }
}

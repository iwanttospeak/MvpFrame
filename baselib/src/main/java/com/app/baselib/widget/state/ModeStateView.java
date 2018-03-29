package com.app.baselib.widget.state;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.app.baselib.R;


/**
 * Created by shen on 2017/5/5.
 * 多状态切换.
 */

public class ModeStateView {

    private StateView mStateView;

    /**
     * 是否显示了其他页面.
     */
    private boolean showOtherPager;

    public ModeStateView(View view, boolean hasActionBar) {
        mStateView = StateView.inject((ViewGroup) view, hasActionBar);
        setLoadingLayout(R.layout.view_loading);
//        mStateView.setAnimatorProvider(new AnimatorProvider() {
//            @Override
//            public Animator showAnimation(View view) {
//                ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.3f, 1f);
//                alpha.setDuration(200);
//                return alpha;
//            }
//
//            @Override
//            public Animator hideAnimation(View view) {
//                ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.3f);
//                alpha.setDuration(200);
//                return alpha;
//            }
//        });
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

    public void setOnRetryListener(OnRetryListener listener) {
        mStateView.setOnRetryClickListener(listener::onRetry);
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
    public View showEmpty() {
        showOtherPager = true;
        return mStateView.showEmpty();
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

    public interface OnRetryListener {
        void onRetry();
    }
}

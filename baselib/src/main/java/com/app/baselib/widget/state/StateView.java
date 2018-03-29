package com.app.baselib.widget.state;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ScrollingView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.app.baselib.R;
import com.github.nukc.stateview.AnimatorProvider;
import com.wang.avi.AVLoadingIndicatorView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * StateView is an invisible, zero-sized View that can be used
 * to lazily inflate loadingView/emptyView/retryView at runtime.
 *
 * @author Nukc
 *         https://github.com/nukc
 */
public class StateView extends View {

    @IntDef({EMPTY, RETRY, LOADING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewType {
    }

    public static final int EMPTY = 0x00000000;
    public static final int RETRY = 0x00000001;
    public static final int LOADING = 0x00000002;

    private int mEmptyResource;
    private int mRetryResource;
    private int mLoadingResource;

    private View mEmptyView;
    private View mRetryView;
    private View mLoadingView;

    private LayoutInflater mInflater;
    private OnRetryClickListener mRetryClickListener;
    private OnInflateListener mInflateListener;

    private RelativeLayout.LayoutParams mLayoutParams;
    private AnimatorProvider mProvider = null;

    /**
     * 注入到 activity 中
     *
     * @param activity Activity
     * @return StateView
     */
    public static StateView inject(@NonNull Activity activity) {
        return inject(activity, false);
    }

    /**
     * 注入到 activity 中
     *
     * @param activity     Activity
     * @param hasActionBar 是否有 actionbar/toolbar
     * @return StateView
     */
    public static StateView inject(@NonNull Activity activity, boolean hasActionBar) {
        ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        return inject(rootView, hasActionBar);
    }

    /**
     * 注入到 ViewGroup 中
     *
     * @param parent extends ViewGroup
     * @return StateView
     */
    public static StateView inject(@NonNull ViewGroup parent) {
        return inject(parent, false);
    }

    /**
     * 注入到 ViewGroup 中.
     * 因为 LinearLayout/ScrollView/AdapterView 的特性
     * 为了 StateView 能正常显示，自动再套一层（开发的时候就不用额外的工作量了）
     * SwipeRefreshLayout/NestedScrollView
     *
     * @param child        extends ViewGroup
     * @param hasActionBar 是否有 actionbar/toolbar,
     *                     true: 会 setMargin top, margin 大小是 actionbarSize
     *                     false: not set
     * @return StateView
     */
    public static StateView inject(@NonNull ViewGroup child, boolean hasActionBar) {
        Context context = child.getContext();
        ViewGroup viewParent = (ViewGroup) child.getParent();
        int screenHeight = 0;

        if (child instanceof FrameLayout || child instanceof RelativeLayout) {
            if (viewParent != null) {
                screenHeight = child.getMeasuredHeight();
            }
        } else if (viewParent == null) {
            FrameLayout wrapper = getFramLayout(context);
            if (child instanceof LinearLayout) {
                LinearLayout wrapLayout = new LinearLayout(child.getContext());
                wrapLayout.setLayoutParams(child.getLayoutParams());
                wrapLayout.setOrientation(((LinearLayout) child).getOrientation());

                for (int i = 0, childCount = child.getChildCount(); i < childCount; i++) {
                    View childView = child.getChildAt(0);
                    child.removeView(childView);
                    wrapLayout.addView(childView);
                }
                wrapper.addView(wrapLayout);
            } else if (child instanceof ScrollView || child instanceof ScrollingView) {
                View directView = child.getChildAt(0);
                child.removeView(directView);
                wrapper.addView(directView);
            } else if (child instanceof NestedScrollingParent && child instanceof NestedScrollingChild) {
                if (child.getChildCount() == 1) {
                    View targetView = child.getChildAt(0);
                    child.removeView(targetView);
                    wrapper.addView(targetView);
                } else if (child.getChildCount() == 2) {
                    View targetView = child.getChildAt(1);
                    child.removeView(targetView);
                    wrapper.addView(targetView);
                } else if (child.getChildCount() > 2) {
                    throw new RuntimeException("the view is not refresh layout? view = "
                            + child.toString());
                }
            } else {
                throw new RuntimeException("the view does not have parent, view = "
                        + child.toString());
            }

            child.addView(wrapper);
            child = wrapper;
        } else {
            if (viewParent instanceof FrameLayout || viewParent instanceof RelativeLayout) {
                child = viewParent;
            } else {
                FrameLayout root = new FrameLayout(child.getContext());
                root.setLayoutParams(child.getLayoutParams());
                root.addView(child);
                viewParent.addView(root);
                child = root;
            }
            screenHeight = child.getMeasuredHeight();
        }

        StateView stateView = new StateView(child.getContext());

        if (screenHeight > 0) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    hasActionBar ? screenHeight - stateView.getActionBarHeight() : screenHeight);
            child.addView(stateView, params);
        } else {
            child.addView(stateView);
        }
        if (hasActionBar) {
            stateView.setTopMargin();
        }
        return stateView;
    }

    /**
     * 获取包裹的Framlayout.
     */
    public static FrameLayout getFramLayout(Context context) {
        FrameLayout wrapper = new FrameLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wrapper.setLayoutParams(layoutParams);

        return wrapper;
    }

    /**
     * 注入到 View 中
     *
     * @param view instanceof ViewGroup
     * @return StateView
     */
    public static StateView inject(@NonNull View view) {
        return inject(view, false);
    }

    /**
     * 注入到 View 中
     *
     * @param view         instanceof ViewGroup
     * @param hasActionBar 是否有 actionbar/toolbar
     * @return StateView
     */
    public static StateView inject(@NonNull View view, boolean hasActionBar) {
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            return inject(parent, hasActionBar);
        } else {
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup) {
                return inject((ViewGroup) parent, hasActionBar);
            } else {
                throw new ClassCastException("view or view.getParent() must be ViewGroup");
            }
        }
    }

    public StateView(Context context) {
        this(context, null);
    }

    public StateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, com.github.nukc.stateview.R.styleable.StateView);
        mEmptyResource = a.getResourceId(com.github.nukc.stateview.R.styleable.StateView_emptyResource, 0);
        mRetryResource = a.getResourceId(com.github.nukc.stateview.R.styleable.StateView_retryResource, 0);
        mLoadingResource = a.getResourceId(com.github.nukc.stateview.R.styleable.StateView_loadingResource, 0);
        a.recycle();

        if (mEmptyResource == 0) {
            mEmptyResource = com.github.nukc.stateview.R.layout.base_empty;
        }
        if (mRetryResource == 0) {
            mRetryResource = com.github.nukc.stateview.R.layout.base_retry;
        }
        if (mLoadingResource == 0) {
            mLoadingResource = com.github.nukc.stateview.R.layout.base_loading;
        }

        if (attrs == null) {
            mLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            mLayoutParams = new RelativeLayout.LayoutParams(context, attrs);
        }

        setVisibility(GONE);
        setWillNotDraw(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(0, 0);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
    }

    @Override
    public void setVisibility(int visibility) {
        setVisibility(mEmptyView, visibility);
        setVisibility(mRetryView, visibility);
        setVisibility(mLoadingView, visibility);
    }

    private void setVisibility(View view, int visibility) {
        if (view != null && visibility != view.getVisibility()) {
            if (mProvider != null) {
                startAnimation(view, visibility);
            } else {
                view.setVisibility(visibility);
            }
        }
    }

    public void showContent() {
        setVisibility(GONE);
    }

    public View showEmpty() {
        if (mEmptyView == null) {
            mEmptyView = inflate(mEmptyResource, EMPTY);
        }

        showView(mEmptyView);
        Log.d("StateView", "mEmptyView.getVisibility() == VISIBLE:" + (mEmptyView.getVisibility() == VISIBLE));
        return mEmptyView;
    }

    public View showRetry() {
        if (mRetryView == null) {
            mRetryView = inflate(mRetryResource, RETRY);
            mRetryView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRetryClickListener != null) {
                        showLoading();
                        mRetryView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRetryClickListener.onRetryClick();
                            }
                        }, 200);
                    }
                }
            });
        }

        showView(mRetryView);
        return mRetryView;
    }

    public View showLoading() {
        if (mLoadingView == null) {
            mLoadingView = inflate(mLoadingResource, LOADING);
        }
        AVLoadingIndicatorView view = mLoadingView.findViewById(R.id.loading);
        view.show();

        showView(mLoadingView);
        return mLoadingView;
    }

    /**
     * show the state view
     */
    private void showView(View view) {
        setVisibility(view, VISIBLE);

        hideViews(view);
    }

    /**
     * hide other views after show view
     */
    private void hideViews(View showView) {
        if (mEmptyView == showView) {
            setVisibility(mLoadingView, GONE);
            setVisibility(mRetryView, GONE);
        } else if (mLoadingView == showView) {
            setVisibility(mEmptyView, GONE);
            setVisibility(mRetryView, GONE);
        } else {
            setVisibility(mEmptyView, GONE);
            setVisibility(mLoadingView, GONE);
        }
    }

    private void startAnimation(final View view, int visibility) {
        boolean isShow = (visibility == View.VISIBLE);

        view.clearAnimation();
        Animator animator = isShow ? mProvider.showAnimation(view) : mProvider.hideAnimation(view);

        if (animator == null) {
            view.setVisibility(visibility == View.VISIBLE ? VISIBLE : GONE);
            return;
        }

//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//                if (isShow) {
//                    view.setVisibility(VISIBLE);
//                }
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                if (!isShow) {
//                    view.setVisibility(GONE);
//                }
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//                if (!isShow) {
//                    view.setVisibility(GONE);
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });

        animator.start();
        view.setVisibility(visibility == View.VISIBLE ? VISIBLE : GONE);
    }

    /**
     * provider default is null
     *
     * @param provider {@link AnimatorProvider}
     */
    public void setAnimatorProvider(AnimatorProvider provider) {
        this.mProvider = provider;
        reset(mEmptyView);
        reset(mLoadingView);
        reset(mRetryView);
    }

    /**
     * reset view's property
     * 不然多次 setAnimatorProvider 后视图动画会混乱
     */
    private void reset(View view) {
        if (view != null) {
            view.setTranslationX(0f);
            view.setTranslationY(0f);
            view.setAlpha(1f);
            view.setRotation(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);
        }
    }

    private View inflate(@LayoutRes int layoutResource, @ViewType int viewType) {
        final ViewParent viewParent = getParent();

        if (viewParent != null && viewParent instanceof ViewGroup) {
            if (layoutResource != 0) {
                final ViewGroup parent = (ViewGroup) viewParent;
                final LayoutInflater factory;
                if (mInflater != null) {
                    factory = mInflater;
                } else {
                    factory = LayoutInflater.from(getContext());
                }
                final View view = factory.inflate(layoutResource, parent, false);

                final int index = parent.indexOfChild(this);
                // 防止还能触摸底下的 View
                view.setClickable(true);
                // 先不显示
                view.setVisibility(GONE);

                final ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (layoutParams != null) {
                    if (parent instanceof RelativeLayout) {
                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) layoutParams;
                        mLayoutParams.setMargins(lp.leftMargin, lp.topMargin,
                                lp.rightMargin, lp.bottomMargin);

                        parent.addView(view, index, mLayoutParams);
                    } else {
                        parent.addView(view, index, layoutParams);
                    }
                } else {
                    parent.addView(view, index);
                }

                if (mLoadingView != null && mRetryView != null && mEmptyView != null) {
                    parent.removeViewInLayout(this);
                }

                if (mInflateListener != null) {
                    mInflateListener.onInflate(viewType, view);
                }

                return view;
            } else {
                throw new IllegalArgumentException("StateView must have a valid layoutResource");
            }
        } else {
            throw new IllegalStateException("StateView must have a non-null ViewGroup viewParent");
        }
    }

    /**
     * 设置 topMargin, 当有 actionbar/toolbar 的时候
     */
    public void setTopMargin() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        layoutParams.topMargin = getActionBarHeight();
    }

    /**
     * @return actionBarSize
     */
    public int getActionBarHeight() {
        return dp2px(48);
    }

    public int dp2px(final float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置 emptyView 的自定义 Layout
     *
     * @param emptyResource emptyView 的 layoutResource
     */
    public void setEmptyResource(@LayoutRes int emptyResource) {
        this.mEmptyResource = emptyResource;
    }

    /**
     * 设置 retryView 的自定义 Layout
     *
     * @param retryResource retryView 的 layoutResource
     */
    public void setRetryResource(@LayoutRes int retryResource) {
        this.mRetryResource = retryResource;
    }

    /**
     * 设置 loadingView 的自定义 Layout
     *
     * @param loadingResource loadingView 的 layoutResource
     */
    public void setLoadingResource(@LayoutRes int loadingResource) {
        mLoadingResource = loadingResource;
    }

    /**
     * Get current {@link LayoutInflater} used in {@link #inflate(int, int)}.
     */
    public LayoutInflater getInflater() {
        return mInflater;
    }

    /**
     * Set {@link LayoutInflater} to use in {@link #inflate(int, int)}, or {@code null}
     * to use the default.
     */
    public void setInflater(LayoutInflater inflater) {
        this.mInflater = inflater;
    }

    /**
     * 监听重试
     *
     * @param listener {@link OnRetryClickListener}
     */
    public void setOnRetryClickListener(OnRetryClickListener listener) {
        this.mRetryClickListener = listener;
    }

    /**
     * Listener used to receive a notification after the RetryView is clicked.
     */
    public interface OnRetryClickListener {
        void onRetryClick();
    }

    /**
     * Specifies the inflate listener to be notified after this StateView successfully
     * inflated its layout resource.
     *
     * @param inflateListener The OnInflateListener to notify of successful inflation.
     * @see OnInflateListener
     */
    public void setOnInflateListener(OnInflateListener inflateListener) {
        mInflateListener = inflateListener;
    }

    /**
     * Listener used to receive a notification after a StateView has successfully
     * inflated its layout resource.
     *
     * @see StateView#setOnInflateListener(OnInflateListener)
     */
    public interface OnInflateListener {
        /**
         * @param view The inflated View.
         */
        void onInflate(@ViewType int viewType, View view);
    }

    public View getEmptyView() {
        return mEmptyView;
    }

}

package com.laojiang.imagepickers.image.grid.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.laojiang.imagepickers.R;
import com.laojiang.imagepickers.data.ImageDataModel;
import com.laojiang.imagepickers.data.ImageFolderBean;
import com.laojiang.imagepickers.image.grid.adapter.ImageFolderAdapter;
import com.laojiang.imagepickers.utils.PopupWindowUtil;

import java.lang.ref.WeakReference;

/**
 *  展示文件夹的PopupWindow
 */
public class ImageFolderPop {
    private WeakReference<Activity> mActReference;
    private PopupWindow mPopupWindow;
    private ListView mListView;
    private ImageFolderAdapter mAdapter;
    private onFolderItemClickListener mListener;
    private ValueAnimator mAnimator;
    private float mBgAlpha = 1f;
    private boolean mIsBright;
    private PopupWindowUtil popupWindowUtil;

    //获取Activity引用实例
    private Activity getActivity() {
        return mActReference != null ? mActReference.get() : null;
    }

    /**
     * 展示菜单弹出框
     */
    public void showMenuPop(View view, Activity activity, ImageFolderBean curFolder, onFolderItemClickListener listener) {
        this.mActReference = new WeakReference<>(activity);
        this.mListener = listener;

        View contentView = LayoutInflater.from(activity).inflate(R.layout.layout_image_floder_pop, null);
        LinearLayout llRootLayout = contentView.findViewById(R.id.ll_pop_layout);
        llRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowUtil.dismissPopuWindow();
            }
        });
        mListView =  contentView.findViewById(R.id.lv_image_folder_pop);
        final int position = ImageDataModel.getInstance().getAllFloderList().indexOf(curFolder);
        mAdapter = new ImageFolderAdapter(activity, position);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null)
                    mListener.onFolderItemClicked(mAdapter.getItem(position));
                popupWindowUtil.dismissPopuWindow();
            }
        });
        popupWindowUtil =  PopupWindowUtil.getInstance();
        popupWindowUtil.configPopwindow(activity, contentView,  ActionBar.LayoutParams.MATCH_PARENT,  ActionBar.LayoutParams.MATCH_PARENT);
        popupWindowUtil.showAsDropDown(view);
        // 增加绘制监听
        ViewTreeObserver vto = mListView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 移除监听
                mListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mListView.setSelection(position);
            }
        });
    }
    /**
     * 切换窗口透明度
     */
    private void toggleWindowAlpha() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
        mAnimator = ValueAnimator.ofFloat(0.5f, 1.0f);
        mAnimator.setDuration(200);//动画时间要和PopupWindow弹出动画的时间一致
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                float value = (float) animation.getAnimatedValue();
                mBgAlpha = mIsBright ? value : (1.5f - value);
                updateBgAlpha(mBgAlpha);
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                mIsBright = !mIsBright;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    /***
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void updateBgAlpha(float bgAlpha) {
        Activity activity = getActivity();
        if (activity != null) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = bgAlpha; //0.0-1.0
            window.setAttributes(lp);
        }
    }

    public interface onFolderItemClickListener {
        void onFolderItemClicked(ImageFolderBean FolderBean);
    }
}

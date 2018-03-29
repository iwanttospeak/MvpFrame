package com.laojiang.imagepickers.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;


/**
 *
 * PopuWindow工具类
 */
public class PopupWindowUtil {
    private static PopupWindowUtil popupWindowUtil;
    private PopupWindow popWindow = null;

    public static PopupWindowUtil getInstance() {
        if (popupWindowUtil == null) {
            popupWindowUtil = new PopupWindowUtil();
        }
        return popupWindowUtil;
    }


    private void initPopuWindow(final Context context) {
        if (popWindow == null) {
            popWindow = new PopupWindow(context);
            popWindow.setBackgroundDrawable(new BitmapDrawable());
            popWindow.setFocusable(true);
            popWindow.setOutsideTouchable(true);
            popWindow.update();
            popWindow.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
                    lp.alpha = 1f;
                    ((Activity) context).getWindow().setAttributes(lp);
                    popWindow = null;
                }
            });
        }
    }

    /**
     *
     * @param context context
     * @param content 内部view
     * @param width 宽度
     * @param height 高度
     */
    public void configPopwindow(Context context, View content, int width, int height) {
        initPopuWindow(context);
        popWindow.setContentView(content);
        popWindow.setHeight(height);
        popWindow.setWidth(width);
    }

    //显示成下拉样式
    public void showAsDropDown(View anchor) {
        popWindow.showAsDropDown(anchor);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        popWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        popWindow.showAtLocation(parent, gravity, x, y);
    }

    /**
     * popuwindow关闭
     */
    public void dismissPopuWindow() {
        if (popWindow != null) {
            popWindow.dismiss();
            popWindow = null;
        }

    }

    public void setAnimation(int animationStyle){
        if(popWindow!=null){
            popWindow.setAnimationStyle(animationStyle);
        }
    }
}

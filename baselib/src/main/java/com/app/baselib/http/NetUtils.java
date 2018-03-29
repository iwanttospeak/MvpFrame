package com.app.baselib.http;

import android.content.Context;

import com.app.baselib.http.callback.ErrorBack;
import com.app.baselib.widget.LoadingDialog;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

/**
 * Created by wang
 * 集中提供网络服务
 */

public class NetUtils {

    /**
     * 无网络提示.
     */
    private static final String NO_NET_HINT = "似乎网络出现了问题";

    /**
     * 网络是否可用.
     */
    protected static  boolean isNetworkAvailable(ErrorBack errorBack) {
        boolean hasNet = NetworkUtils.isConnected();
        if (!hasNet) {
            ToastUtils.showShort(NO_NET_HINT);
            if (errorBack != null) {
                errorBack.onFailure(null);
            }
        }
        return hasNet;
    }
    /**
     * 网络是否可用.
     */
    protected static  boolean isNetworkAvailable() {
        boolean hasNet = NetworkUtils.isConnected();
        if (!hasNet) {
            ToastUtils.showShort(NO_NET_HINT);
        }
        return hasNet;
    }

    /**
     * 判断并创建网络对象.
     */
    protected static RetrofitNetWork getNetword(RetrofitNetWork netWork) {
        if (netWork == null) {
            netWork = new RetrofitNetWork();
        }
        return netWork;
    }

    /**
     * 判断并创建弹窗对象.
     */
    protected static LoadingDialog getDialog(LoadingDialog loadingDialog, Context context) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        return loadingDialog;
    }



    /**
     * 消失弹窗.
     */
    protected static void showDialog(LoadingDialog loadingDialog, boolean showDialog) {
        if (showDialog && loadingDialog != null) {
            loadingDialog.show();
        }
    }

    /**
     * 消失弹窗.
     */
    protected static void dismissDialog(LoadingDialog loadingDialog, boolean showDialog) {
        if (showDialog && loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    protected static void destoryNetword(RetrofitNetWork netWork) {
        if (netWork != null) {
            netWork.cancelAllRequest();
        }
    }

    protected static void destoryDialog(LoadingDialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }



}

package com.app.baselib.http;

import android.content.Context;
import android.support.annotation.NonNull;


import com.app.baselib.http.bean.WrapDataBean;
import com.app.baselib.http.bean.WrapDataNoContent;
import com.app.baselib.http.callback.CallBack;
import com.app.baselib.http.callback.ErrorBack;
import com.app.baselib.widget.LoadingDialog;
import com.app.baselib.widget.state.StateUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import io.reactivex.Observable;


/**
 * @author wang
 * 在全局范围内集中提供网络请求服务
 */
public class NetRequest {

    /**
     * 上下文.
     */
    private Context mContext;

    /**
     * 网络对象.
     */
    private RetrofitNetWork mNetwork;

    /**
     * 多状态切换.
     */
    private StateUtils mStateUtils;

    /**
     * 加载弹窗.
     */
    private LoadingDialog mDialog;

    /**
     * 连接失败提示.
     */
    private static final String NET_ERROR = "连接网络出现了问题,请再试一下";

    /**
     * 构造参数,默认一个 context就够.
     * 如果不传多状态对象,则无法构造多状态切换.
     */
    public NetRequest(Context context) {
        mContext = context;
    }

    /**
     * 设置多状态.
     */
    public void setStateUtils(@NonNull StateUtils stateUtils) {
        mStateUtils = stateUtils;
    }

    /**
     * 请求网络(状态随之变换).
     */
    public <E> void requestWithState(Observable<WrapDataBean<E>> observable, CallBack<E> callBack) {
        requestWithState(observable, callBack, null);
    }

    /**
     * 请求网络(状态随之变换).
     *
     * @param observable 对应的observable对象
     * @param callBack   回调获取结果
     * @param errorBack  错误信息回调
     * @param <E>        需要返回的实例类型
     */
    public <E> void requestWithState(Observable<WrapDataBean<E>> observable, CallBack<E> callBack, ErrorBack errorBack) {
        if (!NetUtils.isNetworkAvailable(errorBack)) {
            mStateUtils.showErrorRetry();
            return;
    }
        mNetwork = NetUtils.getNetword(mNetwork);
        mStateUtils.showLoading();

        CallBack<E> requestCallBack = data -> {
            callBack.onResponse(data);
            mStateUtils.showContent();
        };

        ErrorBack requestErrorBack = baseResponse -> {
            if (baseResponse != null && errorBack != null) {
                errorBack.onFailure(baseResponse);
            }else {
                if (baseResponse !=null){
                    WrapDataBean wrapDataBean = (WrapDataBean) baseResponse;
                    ToastUtils.showShort(wrapDataBean.getMessage()==null ? NET_ERROR : wrapDataBean.getMessage());
                }
                ToastUtils.showShort(NET_ERROR);
            }
            mStateUtils.showErrorRetry();
        };

        mStateUtils.setOnRetryListener(() -> mNetwork.request(observable, requestCallBack, requestErrorBack));
        mNetwork.request(observable, requestCallBack, requestErrorBack);
    }


    public  void requestNoDataWithDialog(Observable<WrapDataNoContent> observable, CallBack<WrapDataNoContent> callBack, boolean showDialog) {
        requestNoDataWithDialog(observable, callBack, null, showDialog);
    }
    public  void requestNoDataWithDialog(Observable<WrapDataNoContent> observable, CallBack<WrapDataNoContent> callBack ,ErrorBack errorBack, boolean isShowDialog) {
        if (!NetUtils.isNetworkAvailable()) {
            return;
        }
        mNetwork = NetUtils.getNetword(mNetwork);
        if (isShowDialog) {
            mDialog = NetUtils.getDialog(mDialog, mContext);
            mDialog.show();
        }
        CallBack<WrapDataNoContent> requestCallBack = data -> {
            callBack.onResponse(null);
            NetUtils.dismissDialog(mDialog, isShowDialog);
        };
        ErrorBack requestErrorBack = baseResponse -> {
            if (baseResponse != null) {
                WrapDataNoContent wrapDataBean = (WrapDataNoContent) baseResponse;
                if (errorBack != null){
                    errorBack.onFailure(wrapDataBean);
                }else {
                    ToastUtils.showShort(wrapDataBean.getMessage());
                }
            }else {
                ToastUtils.showShort(NET_ERROR);
            }
            mStateUtils.showErrorRetry();
        };
        mNetwork.requestNoData(observable, requestCallBack, requestErrorBack);
    }
    /**
     * 网络请求(显示弹窗).
     *
     * @param observable        从retrofit获取的rxjava对象
     * @param callBack 成功回调
     * @param showDialog        是否显示dialog
     * @param <E>               需要获取的网络数据实体类
     */
    public <E> void requestWithDialog(Observable<WrapDataBean<E>> observable, CallBack<E> callBack, boolean showDialog) {
        requestWithDialog(observable, callBack, null, showDialog);
    }
    /**
     * 请求网络(显示弹窗).
     *
     * @param observable   对应的observable对象
     * @param callBack     回调获取结果
     * @param errorBack    错误回调
     * @param <E>          需要返回的实例类型
     * @param isShowDialog 是否显示弹窗
     */
    public <E> void requestWithDialog(Observable<WrapDataBean<E>> observable, CallBack<E> callBack, ErrorBack errorBack, boolean isShowDialog) {
        if (!NetUtils.isNetworkAvailable(errorBack)) {
            return;
        }

        mNetwork = NetUtils.getNetword(mNetwork);
        if (isShowDialog) {
            mDialog = NetUtils.getDialog(mDialog, mContext);
            mDialog.show();
        }

        CallBack<E> requestCallBack = data -> {
            callBack.onResponse(data);
            NetUtils.dismissDialog(mDialog, isShowDialog);
        };

        ErrorBack requestErrorBack = baseResponse -> {
            if (baseResponse != null) {
                WrapDataBean<E> wrapDataBean = (WrapDataBean<E>) baseResponse;
                if (errorBack != null){
                    errorBack.onFailure(wrapDataBean);
                }else {
                    ToastUtils.showShort(wrapDataBean.getMessage());
                }
            } else {
                ToastUtils.showShort(NET_ERROR);
            }
            NetUtils.dismissDialog(mDialog, isShowDialog);
        };

        mNetwork.request(observable, requestCallBack, requestErrorBack);
    }

    /**
     * 布局控制器.
     */
    public StateUtils getStateUtils() {
        return mStateUtils;
    }

    /**
     * 销毁资源.
     */
    public void onDestroy() {
        NetUtils.destoryNetword(mNetwork);
        NetUtils.destoryDialog(mDialog);
        mContext = null;
    }

}

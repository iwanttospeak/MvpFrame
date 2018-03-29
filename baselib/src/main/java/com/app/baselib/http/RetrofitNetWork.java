package com.app.baselib.http;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.telecom.Call;


import com.app.baselib.Utils.ARouterUtil;
import com.app.baselib.constant.BaseGlobal;
import com.app.baselib.http.bean.WrapDataBean;
import com.app.baselib.http.bean.WrapDataNoContent;
import com.app.baselib.http.callback.CallBack;
import com.app.baselib.http.callback.ErrorBack;
import com.app.baselib.http.callback.NoDataBack;
import com.app.baselib.http.error.ExceptionHandle;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 10488
 * @date 2017-08-06
 */
public final class RetrofitNetWork {

    /**
     * 正确返回码.
     */
    public static final int CORRECT_CODE = 0;

    /**
     * 登录过期码.
     */
    public static final int LOGIN_CODE = -99;

    /**
     * 存储网络请求.
     */
    private final CompositeDisposable mComposite = new CompositeDisposable();

    /**
     * 网络请求方法.
     *
     * @param observable 对应的observable对象
     * @param callBack   回调获取结果
     * @param errorBack  错误回调结果
     * @param <E>        需要返回的实例类型
     */
    public <E> void request(Observable<WrapDataBean<E>> observable, @NonNull CallBack<E> callBack, @NonNull ErrorBack errorBack) {
        mComposite.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eBaseResponse -> {
                    int code = eBaseResponse.getErrcode();
                    if (code == CORRECT_CODE) {
                        callBack.onResponse(eBaseResponse.getContent());
                    } else if (code == LOGIN_CODE) {
                        BaseGlobal.clearAll();
                        ARouterUtil.goActivity("/login/login_activity", Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    } else {
                        errorBack.onFailure(eBaseResponse);
                    }
                }, throwable -> {
                    ExceptionHandle.ResponseException responseException = ExceptionHandle.handleException(throwable);
                    responseException.printStackTrace();
                    errorBack.onFailure(null);
                }));
    }
    /**
     *  没有返回数据的网络请求方法.
     */
    public  void requestNoData(Observable<WrapDataNoContent> observable, @NonNull CallBack<WrapDataNoContent> callBack, @NonNull ErrorBack errorBack) {
        mComposite.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(eBaseResponse -> {
                    int code = eBaseResponse.getErrcode();
                    if (code == CORRECT_CODE) {
                        callBack.onResponse(null);
                    } else if (code == LOGIN_CODE) {
                        BaseGlobal.clearAll();
                        ARouterUtil.goActivity("/login/login_activity", Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    } else {
                        errorBack.onFailure(eBaseResponse);
                    }
                }, throwable -> {
                    ExceptionHandle.ResponseException responseException = ExceptionHandle.handleException(throwable);
                    responseException.printStackTrace();
                    errorBack.onFailure(null);
                }));
    }

    /**
     * 统一取消所有请求.
     */
    public void cancelAllRequest() {
        mComposite.clear();
    }
}

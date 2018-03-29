package com.app.baselib.http.callback;


import com.app.baselib.http.bean.WrapDataBean;

/**
 * Created by wang
 */

public interface ErrorBack {

    void onFailure(Object errorData);
}

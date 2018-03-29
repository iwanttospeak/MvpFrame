package com.app.baselib.http.callback;

/**
 * 获取到资源回调.
 *
 * @param <E> 需要回调的具体实体类
 * @author 10488
 */

public interface CallBack<E> {

    void onResponse(E data);

}

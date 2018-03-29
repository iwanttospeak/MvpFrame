package com.app.baselib.adapter.base;

/**
 * @author zhy on 16/6/22.
 * ItemViewDelegate是一个接口描述了一个ItemView的样板
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);

}

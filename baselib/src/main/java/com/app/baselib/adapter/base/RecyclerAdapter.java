package com.app.baselib.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.app.baselib.adapter.callback.DiffAdapterHelper;
import com.app.baselib.adapter.callback.DiffCallBack;
import com.app.baselib.adapter.callback.PagedList;

import java.util.List;

/**
 * RecyclerAdapter类屏蔽掉了多种类型的ItemView的功能，用于一种ItemView的显示，
 */
public abstract class RecyclerAdapter<T> extends MultiItemTypeAdapter<T> {

    protected Context mContext;
    /**
     * 布局.
     */
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    /**
     * DiffUtil的计算类.
     */
    private DiffAdapterHelper<T> mAdapterHelper;
    public RecyclerAdapter(Context context, int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType( T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                RecyclerAdapter.this.convert(holder, t, position);
            }
        });
    }


    /**
     * 创建对应diffUtil计算类.需要动态回调
     *
     * @param diffCallBack 需要的对比回调
     */
    public final void setDiffCallBack(@NonNull DiffCallBack<T> diffCallBack) {
        mDatas = getNewRegisterList(mDatas);
        mAdapterHelper = new DiffAdapterHelper<>(mDatas, this, diffCallBack);
    }

    /**
     * 设置新值.
     * 如果mAdapterHelper为空,则普通赋值
     *
     * @param listData 列表数据
     */
    public final void setListData(List<T> listData) {
        if (mAdapterHelper != null && listData instanceof PagedList) {
            mAdapterHelper.setListData(listData);
        } else {
            setTrueListData(listData);
        }
    }

    /**
     * 赋值给adapter的值.mNetRequest.getModeStateView().showEmpty();
     *
     * @param listData 对应的集合
     */
    public final void setTrueListData(List<T> listData) {
        this.mDatas = listData;
    }
    /**
     * 获取PagedList数据.
     */
    public static <T> List<T> getNewRegisterList() {
        return getNewRegisterList(null);
    }

    /**
     * 数据转换,将对应的集合转为PagedList.
     * @param list 对应的集合
     * @param <T>  adapter对应的 泛型
     * @return PagedList
     */
    public static <T> List<T> getNewRegisterList(List<T> list) {
        PagedList<T> pagedList = new PagedList<>();
        if (list != null && !list.isEmpty()) {
            pagedList.addAll(list);
        }
        return pagedList;
    }
    protected abstract void convert(ViewHolder holder, T t, int position);

}

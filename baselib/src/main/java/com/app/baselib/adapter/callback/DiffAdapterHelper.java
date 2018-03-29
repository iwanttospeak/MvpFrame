package com.app.baselib.adapter.callback;

import android.os.Handler;
import android.support.v7.util.DiffUtil;


import com.app.baselib.adapter.base.RecyclerAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 沈小建 on 2017-12-06.
 */

public class DiffAdapterHelper<T> {

    /**
     * 线程池(固定2个).
     */
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(2);

    /**
     * diff计算.
     */
    private DiffCallBack<T> mDiffCallback;

    /**
     * 对应的adapter.
     */
    private RecyclerAdapter<T> mBaseAdapter;

    /**
     * 数据.
     */
    private List<T> mListData;

    private Handler mHandler = new Handler();

    public DiffAdapterHelper(List<T> listData, RecyclerAdapter<T> baseAdapter, DiffCallBack<T> diffCallback) {
        this.mListData = listData;
        this.mBaseAdapter = baseAdapter;
        this.mDiffCallback = diffCallback;

        setPagedCallback();
    }

    /**
     * 设置pagedList的刷新回调.
     */
    private void setPagedCallback() {
        if (!(mListData instanceof PagedList)) {
            return;
        }

        ((PagedList) mListData).setCallback(new PagedList.Callback() {
            @Override
            public void onInserted(int position, int count) {
                mHandler.post(() -> {
                    mBaseAdapter.notifyItemRangeInserted(position, count);
                    mBaseAdapter.notifyItemRangeChanged(position, mListData.size() - position);
                });
            }

            @Override
            public void onRemoved(int position, int count) {
                mHandler.post(() -> {
                    mBaseAdapter.notifyItemRangeRemoved(position, count);
                    mBaseAdapter.notifyItemRangeChanged(0, mListData.size());
                });
            }
        });
    }

    /**
     * 设置新值.
     * <p>
     * 通过新旧数据对比计算,进入线程池计算
     *
     * @param listData 列表数据
     */
    public void setListData(final List<T> listData) {
        if (mDiffCallback == null) {
            mBaseAdapter.setTrueListData(listData);
            this.mListData = listData;
            setPagedCallback();
            return;
        }

        mDiffCallback.setNewData(listData);
        mDiffCallback.setOldData(mListData);

        mExecutorService.submit(() -> {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(mDiffCallback, true);

            mHandler.post(() -> {
                mBaseAdapter.setTrueListData(listData);
                diffResult.dispatchUpdatesTo(mBaseAdapter);

                this.mListData = listData;
                setPagedCallback();
            });
        });
    }
}

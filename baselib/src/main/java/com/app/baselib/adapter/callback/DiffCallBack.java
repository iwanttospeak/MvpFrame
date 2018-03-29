package com.app.baselib.adapter.callback;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * @author 沈小建 10488 on 2017-08-14.
 */

public abstract class DiffCallBack<T> extends DiffUtil.Callback {

    private List<T> mOldData;
    private List<T> mNewData;

    public DiffCallBack() {

    }

    public DiffCallBack(@NonNull List<T> oldData, @NonNull List<T> newData) {
        mOldData = oldData;
    mNewData = newData;
}

    public final void setOldData(List<T> oldData) {
        mOldData = oldData;
    }

    public final void setNewData(List<T> newData) {
        mNewData = newData;
    }

    @Override
    public final int getOldListSize() {
        return mOldData.size();
    }

    @Override
    public final int getNewListSize() {
        return mNewData.size();
    }

    @Override
    public final boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsTheSame(mOldData.get(oldItemPosition), mNewData.get(newItemPosition));
    }

    @Override
    public final boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areContentsTheSame(mOldData.get(oldItemPosition), mNewData.get(newItemPosition));
    }

    @Nullable
    @Override
    public final Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return getChangePayload(mOldData.get(oldItemPosition), mNewData.get(newItemPosition));
    }

    public abstract boolean areItemsTheSame(T oldItem, T newItem);

    public abstract boolean areContentsTheSame(T oldItem, T newItem);

    @Nullable
    public Object getChangePayload(T oldItem, T newItem) {
        return null;
    }
}

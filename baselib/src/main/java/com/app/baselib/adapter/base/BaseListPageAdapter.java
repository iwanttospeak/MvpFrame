package com.app.baselib.adapter.base;

import android.content.Context;
import android.support.annotation.Nullable;

import com.app.baselib.Utils.TwinklingRefreshLayoutUtils;
import com.app.baselib.http.Api;
import com.app.baselib.http.NetRequest;
import com.app.baselib.http.bean.WrapDataBean;
import com.app.baselib.http.callback.CallBack;
import com.app.baselib.http.callback.ErrorBack;
import com.app.baselib.http.params.BaseListParams;
import com.app.baselib.mvp.BasePresent;
import com.app.baselib.widget.state.StateUtils;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import io.reactivex.Observable;


/**
 * 用于网络加载数据分页的基类adapter
 * @author by Wang on 2017/8/15.
 */

public abstract class BaseListPageAdapter<T,E extends BaseListParams> extends RecyclerAdapter<T> {

    private BasePresent basePresent;

    private StateUtils mStateUtils;
    //分页信息
    private static final int PAGE_FIRST = 1;
    //当前页
    private int current_page = PAGE_FIRST;
    /**
     * 每页条数
     */
    public static final int MAX_PAGE_NUMBER = 5;

    public List<T> getBeanList() {
        return beanList;
    }

    //列表数据
    private List<T> beanList;
    /**
     * 网络对象.
     */
    protected NetRequest mNetRequest;
    private OnGetDataFinish onGetDataFinish;
    public interface OnGetDataFinish {
        public void onFinish();
    }
    private TwinklingRefreshLayout mLlState;
    public BaseListPageAdapter(Context context, int layoutId, List<T> datas,TwinklingRefreshLayout refreshLayout) {
        super(context, layoutId, datas);
        beanList = datas;
        mLlState = refreshLayout;
        initStates();
        initNetRequest(mStateUtils);
        initListener();
    }

    /**
     * 重新请求数据
     */
    public void reRequestList(){
        current_page = PAGE_FIRST;
        beanList.clear();
        requestListData();
    }
    /**开始请求数据*/
    public void startRequestList(){
        requestListData();
    }
    /**
     * 初始化状态显示.
     */
    private void initStates() {
        mStateUtils = new StateUtils(mLlState,false);
        TwinklingRefreshLayoutUtils.initTwinklingLayout(mLlState);

    }
    /**
     * 初始化对象.
     */
    private void initNetRequest(StateUtils stateUtils) {
        mNetRequest = new NetRequest(mContext);
        if (stateUtils != null) {
            mNetRequest.setStateUtils(stateUtils);
        }
    }

    /**
     * 初始化刷新
     */
    private void initListener(){
        //初始化刷新
        mLlState.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                openLoadMore();
                onGetDataFinish = new OnGetDataFinish() {
                    @Override
                    public void onFinish() {
                        //结束刷新
                        refreshLayout.finishRefreshing();
                    }
                };
                current_page = PAGE_FIRST;
                beanList.clear();
                requestListData();
            }
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                onGetDataFinish = refreshLayout::finishLoadmore;
                current_page++;
                requestListData();
            }
        });
    }

    private void closeLoadMore() {
        mLlState.setEnableLoadmore(false);
    }

    private void openLoadMore() {
        mLlState.setEnableLoadmore(true);
    }

    /**
     *开始请求列表数据
     */
    public void requestListData() {
        setParams(Integer.toString(current_page));

        mNetRequest.requestWithState(getObservable(), data -> {
            if (data!=null && data.size()>0){
                updateListData(data);
            }else {
                if (!noDataHandle(data)){//如果子类adapter没有处理
                    updateListData(null);
                }
            }
        }, errorData -> updateListData(null));

    }

    public boolean noDataHandle(List<T> data){
        return false;
    }
    /**
     * 请求成功更新列表.
     */
    public void updateListData(@Nullable List<T> refundListBeen) {
        if (refundListBeen == null){//refundListBeen为null不一定整个数据为null
            closeLoadMore();
            if (onGetDataFinish != null) onGetDataFinish.onFinish();
            //为空则显示空提示
            if (beanList.isEmpty()) {
                mStateUtils.showEmpty();
            }
            notifyDataSetChanged();
        }else {
            if (current_page ==PAGE_FIRST){
                beanList.clear();
            }
            beanList.addAll(refundListBeen);
            //为空则显示空提示
            if (beanList.isEmpty()) {
                mStateUtils.showEmpty();
                return;
            }
            //当本次请求返回的数据的集合小于分页的默认返回值的时候
            if (refundListBeen.size() < MAX_PAGE_NUMBER) {
                closeLoadMore();
            } else {
                openLoadMore();
            }
            notifyDataSetChanged();
            mStateUtils.showContent();
            //请求结束
            if (onGetDataFinish != null) onGetDataFinish.onFinish();
        }
    }
    /**
     *  因为Retrofit强制只能反射的APIServer类只能是接口，而且不能进行继承，
     * 所以这里不能设置K继承什么基础接口之类的
     */
    public  <K> K getApiService(Class<K> serviceClass){
        return Api.getInstance().mRetrofit.create((serviceClass));
    }
    /**设置请求参数*/
    public abstract E setParams(String current_page);
    /**得到请求体*/
    protected abstract Observable<WrapDataBean<List<T>>> getObservable();

}

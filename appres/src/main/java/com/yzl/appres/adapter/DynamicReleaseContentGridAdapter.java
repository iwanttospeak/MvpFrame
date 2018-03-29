package com.yzl.appres.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.baselib.Utils.ImageDisplayUtil;
import com.app.baselib.adapter.base.RecyclerAdapter;
import com.app.baselib.adapter.base.ViewHolder;
import com.blankj.utilcode.util.StringUtils;
import com.yzl.appres.R;
import com.yzl.appres.bean.DynamicContentBean;

import java.util.List;

/**
 * 动态发布内容GridAdapter , bean 是图片本地存储位置
 */
public class DynamicReleaseContentGridAdapter extends RecyclerAdapter<DynamicContentBean> {

    //图片最多可以放九张
    private static final int DYNAMIC_MAX_IMGS_NUMBER = 6;

    //动态内容最大数量限制
    private int dynamicMaxNumber = DYNAMIC_MAX_IMGS_NUMBER;

    private String contentType;

    public DynamicReleaseContentGridAdapter(Context context, int layoutId, List<DynamicContentBean> datas) {
        super(context, layoutId, datas);
    }
    private int itemHeight;
    public int getItemHeight(){ return itemHeight;}

    @Override
    public void onViewHolderCreated(ViewHolder holder, View itemView) {
        super.onViewHolderCreated(holder, itemView);
        itemView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                itemHeight=itemView.getMeasuredHeight();
                return true;
            }
        });
    }
    @Override
    protected void convert(ViewHolder holder, DynamicContentBean bean, int position) {
        ImageView ivCloseSelected =  holder.getView(R.id.iv_close_selected);
        ImageView ivRelectedContent =  holder.getView(R.id.iv_dynamic_content);
        ImageView ivAddContent =  holder.getView(R.id.iv_add_content);
        RelativeLayout layoutRoot =  holder.getView(R.id.rl_dynamic_content_root);

        if (position >= dynamicMaxNumber){//超过最大数量限制,例如6
            layoutRoot.setVisibility(View.GONE);
            return;
        }
        if (position == dynamicMaxNumber -1 ){ //达到最大数量限制
            if (StringUtils.isSpace(bean.getImgUrl())){//最后一个是null
                showAddButton(holder);
            }
        }else {//没有达到最大数量限制
            if (position == getItemCount() -1 && StringUtils.isSpace(bean.getType())){//最后一个是null
                showAddButton(holder);
            }
        }

        if (!StringUtils.isSpace(bean.getImgUrl())){//最后一张是选择的图片
            ImageDisplayUtil.displayImage(ivRelectedContent,bean.getImgUrl());
        }
        ivCloseSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListener.onClickClose(position);
            }
        });
        ivRelectedContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListener.onClickContent(position);
            }
        });
        ivAddContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventListener.onClickAdd();
            }
        });
    }
    /**
     * 显示添加按钮
     */
    private void showAddButton(ViewHolder holder) {
        ImageView ivCloseSelected =  holder.getView(R.id.iv_close_selected);
        ImageView ivRelectedContent =  holder.getView(R.id.iv_dynamic_content);
        ImageView ivAddContent =  holder.getView(R.id.iv_add_content);

        ivCloseSelected.setVisibility(View.GONE);
        ivRelectedContent.setVisibility(View.GONE);
        ivAddContent.setVisibility(View.VISIBLE);
    }
    /**
     * 显示内容
     */
    private void showContent(ViewHolder holder) {
        ImageView ivCloseSelected =  holder.getView(R.id.iv_close_selected);
        ImageView ivRelectedContent =  holder.getView(R.id.iv_dynamic_content);
        ImageView ivAddContent =  holder.getView(R.id.iv_add_content);
        ivCloseSelected.setVisibility(View.VISIBLE);
        ivRelectedContent.setVisibility(View.VISIBLE);
        ivAddContent.setVisibility(View.GONE);
    }
    public interface OnReleaseEventListener{
        void onClickClose(int position);
        void onClickAdd();
        void onClickContent(int position);
    }

    private OnReleaseEventListener eventListener;
    public void setOnReleaseEventListener(OnReleaseEventListener listener){
        eventListener = listener;
    }
}

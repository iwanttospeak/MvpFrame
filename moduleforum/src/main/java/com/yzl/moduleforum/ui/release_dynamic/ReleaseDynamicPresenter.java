package com.yzl.moduleforum.ui.release_dynamic;

import android.content.Intent;

import com.app.baselib.Utils.StringUtil;
import com.app.baselib.http.bean.WrapDataNoContent;
import com.app.baselib.http.callback.CallBack;
import com.app.baselib.http.update.UpdateImgHelper;
import com.app.baselib.widget.BigImgActivity;
import com.blankj.utilcode.util.StringUtils;
import com.laojiang.imagepickers.data.MediaDataBean;
import com.youth.banner.loader.ImageLoader;
import com.yzl.appres.adapter.DynamicReleaseContentGridAdapter;
import com.yzl.appres.bean.DynamicContentBean;
import com.yzl.moduleforum.ForumApiService;
import com.yzl.moduleforum.R;
import com.yzl.moduleforum.params.ReleaseDynamicParams;

import java.util.ArrayList;
import java.util.List;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class ReleaseDynamicPresenter extends ReleaseDynamicContract.ReleasePresent<ReleaseDynamicActivity> {
    private ImageLoader mImageLoader;
    public static final String MIME_JPEG = "image/jpeg";
    //图片最多可以放九张
    private static final int DYNAMIC_MAX_IMGS_NUMBER = 6;
    //当前已选的数量，和grid中数据保持一致（去除掉标志位）
    private int mCurrentNumber = 0;
    List<String> mTargetTypeList = new ArrayList<>();
    //动态内容
    DynamicReleaseContentGridAdapter mContentGridAdapter;
    List<DynamicContentBean> mContentGridDatas;
    ReleaseDynamicParams mDynamicParams;

    /**
     *初始化内容类型
     */
    public void initData(ReleaseDynamicParams params) {
        mDynamicParams = params;
    }
    /**
     * 获取内容回调
     */
    public void onTakeContent(List<MediaDataBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (MediaDataBean dataBean : list) {
            DynamicContentBean bean = new DynamicContentBean();
            bean.setImgUrl(dataBean.getMediaPath());
            addContentToGrid(bean);
        }
    }

    public void notifyDataChange(){
        mContentGridAdapter.notifyDataSetChanged();
        mView.notifyReleaseButtonState();
    }
    /**
     * 删除内容
     */
    private void deleteContentFromGrid(int position) {
        if (mContentGridDatas.size() == DYNAMIC_MAX_IMGS_NUMBER &&
                !StringUtils.isSpace(mContentGridDatas.get(DYNAMIC_MAX_IMGS_NUMBER-1).getType())){//最后一个有内容
            mContentGridDatas.add(new DynamicContentBean());//添加标志位
        }
        mContentGridDatas.remove(position);
    }
    /**
     * 添加内容
     */
    private void addContentToGrid(DynamicContentBean bean) {
        if ( mContentGridDatas.size() == DYNAMIC_MAX_IMGS_NUMBER){//去掉标志位
            mContentGridDatas.remove(mContentGridDatas.size() -1);
            mCurrentNumber += 1;
            mContentGridDatas.add(bean);
            return;
        }else if (mContentGridDatas.size() > DYNAMIC_MAX_IMGS_NUMBER){//大于限制
            showToast(mView.getActivity().getString(R.string.message_image_max_num,""+DYNAMIC_MAX_IMGS_NUMBER));
            return;
        }
        if (mContentGridDatas.size()>0 ){//数据数量大于0
            mContentGridDatas.add(mContentGridDatas.size()-1,bean);
            mCurrentNumber += 1;
        }else {
            mContentGridDatas.add(bean);
            mCurrentNumber += 1;
            mContentGridDatas.add(new DynamicContentBean());//添加提示位
        }
    }
    /**
     * 动态内容grid
     */
    public void initContentGridAdapter(){
        initGridContent();
        mContentGridAdapter = new DynamicReleaseContentGridAdapter(mView.getActivity(), R.layout.res_item_dynamic_content, mContentGridDatas);
        mContentGridAdapter.setOnReleaseEventListener(new DynamicReleaseContentGridAdapter.OnReleaseEventListener() {
            @Override
            public void onClickClose(int position) {
                closeGridContent(position);
            }
            @Override
            public void onClickAdd() {
                mView.choosePicture(DYNAMIC_MAX_IMGS_NUMBER - mCurrentNumber);
            }
            @Override
            public void onClickContent(int position) {
                showBigImage(mContentGridDatas);
            }
        });
        mView.setContentAdapter(mContentGridAdapter);
    }

    private void initGridContent() {
        if (mContentGridDatas.size() == 0){//没有内容，直接添加标志位
            mContentGridDatas.add(new DynamicContentBean());
            return;
        }
        if (!StringUtils.isSpace(mContentGridDatas.get(mContentGridDatas.size() - 1).getType())
                && mContentGridDatas.size() < DYNAMIC_MAX_IMGS_NUMBER){//没有标志位，并且大小不到限制
            mContentGridDatas.add(new DynamicContentBean());//添加标志位
        }
    }

    private void closeGridContent(int position) {
        deleteContentFromGrid(position);
        mCurrentNumber -= 1;
//        mContentGridAdapter.notifyItemRemoved(position);//删除动画
//        mContentGridAdapter.notifyItemRangeChanged(position, mContentGridDatas.size());
        mContentGridAdapter.notifyDataSetChanged();
        mView.notifyReleaseButtonState();
    }

    /**
     * 大图预览页面
     */
    private void showBigImage(List<DynamicContentBean> imgList) {
        if (imgList==null || imgList.size()==0)return;
        ArrayList<String> imagesList = new ArrayList<>();
        for (DynamicContentBean bean :imgList){
            if (StringUtils.isSpace(bean.getType()) || StringUtils.isSpace(bean.getImgUrl())){
            }else {
                imagesList.add(bean.getImgUrl());
            }
        }
        Intent intent = new Intent(mView.getActivity(), BigImgActivity.class);
        intent.putExtra("imgs", imagesList);
        intent.putExtra("position", 0);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        mView.getActivity().startActivity(intent);
    }
    /**
     * 点击发布动态
     */
    public void clickReleaseDynamic() {
        updateImageBody();
    }
    /**
     * 发布动态
     */
    public void releaseDynamic() {
        mNetRequest.requestNoDataWithDialog(getApiService(ForumApiService.class).forumPost(mDynamicParams), new CallBack<WrapDataNoContent>() {
            @Override
            public void onResponse(WrapDataNoContent data) {
                showToast("动态发布成功");
            }
        }, true);
    }

    /**
     * 上传动态图片
     */
    private void updateImageBody() {
        List<String> imgFileUrlList;
        imgFileUrlList = new ArrayList<>();
        UpdateImgHelper updateImgHelper = new UpdateImgHelper(mView,mView.getActivity());
        for (DynamicContentBean bean : mContentGridDatas){
            if (!StringUtils.isSpace(bean.getImgUrl())){
                imgFileUrlList.add(bean.getImgUrl());
            }
        }
        updateImgHelper.updateImageList(imgFileUrlList, new UpdateImgHelper.OnSuccessListener() {
            @Override
            public void onSuccess(List<String> imgUrlList) {

                mDynamicParams.setImages(StringUtil.imgListToString(imgUrlList));
                releaseDynamic();
            }
        }, true);
    }


    @Override
    public boolean isHasUpdateInfo(String path) {
        return false;
    }
}

package com.yzl.moduleforum.ui.release_dynamic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.baselib.adapter.WrapHeightGridLayoutManager;
import com.app.baselib.adapter.decoration.SpacingDecoration;
import com.app.baselib.mvp.BaseActivity;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.laojiang.imagepickers.ImagePicker;
import com.laojiang.imagepickers.data.ImagePickType;
import com.laojiang.imagepickers.data.MediaDataBean;
import com.laojiang.imagepickers.utils.DefaultGlideDisplay;
import com.luck.picture.lib.tools.ScreenUtils;
import com.yzl.appres.adapter.DynamicReleaseContentGridAdapter;
import com.yzl.appres.bean.DynamicContentBean;
import com.yzl.moduleforum.R;
import com.yzl.moduleforum.params.ReleaseDynamicParams;

import java.util.List;

/**
 * 发布动态
 */
public class ReleaseDynamicActivity extends BaseActivity<ReleaseDynamicPresenter> implements ReleaseDynamicContract.View {

    //图片或者视频都可以，当动态内有内容时使用
    private static final int EXTRA_REQUEST_PICK_ALL = 3005;

    TextView tvRight;
    EditText etDynamicText;
    EditText etTitle;
    RecyclerView rvDynamicImgOrVideoGrid;

    ReleaseDynamicParams mDynamicParams;
    //内容
    List<DynamicContentBean> mReleaseContentGridDatas;

    @Override
    public int getLayoutId() {
        return R.layout.forum_activity_release_dynamic;
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void init() {
        mDynamicParams = new ReleaseDynamicParams();
        tvRight =getRightTextView();
        tvRight.setText("发布");
        setToolTitle("发帖");
        etDynamicText = findViewById(R.id.et_content);
        etTitle = findViewById(R.id.et_title);

        etDynamicText.addTextChangedListener(new MyTextWatcher());

        rvDynamicImgOrVideoGrid = findViewById(R.id.rv_dynamic_img_grid);
        rvDynamicImgOrVideoGrid.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new WrapHeightGridLayoutManager(this, 3);
        gridLayoutManager.setAutoMeasureEnabled(true);//高度自适应
        PhotoLayoutManage photoLayoutManage = new PhotoLayoutManage(this,3);
        gridLayoutManager.setAutoMeasureEnabled(true);//高度自适应
        rvDynamicImgOrVideoGrid.setLayoutManager(photoLayoutManage);

        rvDynamicImgOrVideoGrid.addItemDecoration(new SpacingDecoration(ScreenUtils.dip2px(this,8), ScreenUtils.dip2px(this,8), true));

        mPresent.initData(mDynamicParams);
        mPresent.initContentGridAdapter();
        tvRight.setOnClickListener(v -> onClickRelease());
    }

    /**
     *     使GridLayoutManager高度自适应，但是注意删除item的时候只能使用notifyDataSetChanged
     *     不能使用notifyItemRemoved，会导致onMeasure不执行
     */
    public class PhotoLayoutManage extends GridLayoutManager {
        // RecyclerView高度随Item自适应
        public PhotoLayoutManage(Context context, int spanCount) {
            super(context,spanCount);
        }
        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, final int widthSpec, final int heightSpec) {
            try {
                DynamicReleaseContentGridAdapter adapter = (DynamicReleaseContentGridAdapter) rvDynamicImgOrVideoGrid.getAdapter();
                if(adapter !=null&&adapter.getItemHeight()>0) {
                    int measuredWidth = View.MeasureSpec.getSize(widthSpec);

                    int line = adapter.getItemCount() / getSpanCount();
                    if (adapter.getItemCount() % getSpanCount() > 0) line++;
                    int measuredHeight = adapter.getItemHeight()* line;
                    setMeasuredDimension(measuredWidth, measuredHeight);
                }else{
                    super.onMeasure(recycler,state,widthSpec,heightSpec);
                }

            }catch (Exception e){
                super.onMeasure(recycler,state,widthSpec,heightSpec);
            }
        }
    }
    /**
     * 发布
     */
    private void onClickRelease() {
        mDynamicParams.setContent(etDynamicText.getText().toString());
        mDynamicParams.setTitle(etTitle.getText().toString());
        mPresent.clickReleaseDynamic();
    }

    @Override
    public void setContentAdapter(DynamicReleaseContentGridAdapter adapter) {
        rvDynamicImgOrVideoGrid.setAdapter(adapter);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void notifyReleaseButtonState() {
        if (StringUtils.isSpace(etDynamicText.getText().toString()) && mReleaseContentGridDatas.size() == 0 ){//没有内容
            tvRight.setEnabled(false);
            tvRight.setTextColor(ContextCompat.getColor(this,R.color.base_tv_gray));
        }else if (StringUtils.isSpace(mDynamicParams.getTitle())){
            tvRight.setEnabled(false);
            tvRight.setTextColor(ContextCompat.getColor(this,R.color.base_tv_gray));
        }else if (StringUtils.isSpace(mDynamicParams.getContent())){
            tvRight.setEnabled(false);
            tvRight.setTextColor(ContextCompat.getColor(this,R.color.base_tv_gray));
        }else {
            tvRight.setEnabled(true);
            tvRight.setTextColor(ContextCompat.getColor(this,R.color.base_tv_black));
        }
    }
    /**
     * 只选择是图片
     */
    @Override
    public void choosePicture(int maxNumber) {
        ImagePicker builder = new ImagePicker.Builder()
                .pickType(ImagePickType.MUTIL)
                .ImageMaxNum(maxNumber)
                .needCamera(true) //是否需要在界面中显示相机入口(类似微信那样)
                .displayer(new DefaultGlideDisplay()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                .needVideo(false)//是否需要视频
                .build();
        builder.start(this, EXTRA_REQUEST_PICK_ALL,ImagePicker.DEF_RESULT_CODE); //自定义RequestCode和ResultCode
    }

    /**
     * 处理拍照或者选择图片回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            ToastUtils.showShort(getString(R.string.picker_content_error));
            return;
        }
        if (requestCode == EXTRA_REQUEST_PICK_ALL){ //请求所有的数据,图片或者视频
            if (resultCode == ImagePicker.DEF_RESULT_CODE){
                List<MediaDataBean> list = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
                mPresent.onTakeContent(list);
                mPresent.notifyDataChange();
            }else {
                ToastUtils.showShort(getString(R.string.picker_content_error));
            }
        }
    }
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            notifyReleaseButtonState();
        }
    }


    @Override
    public void showLoading() {
        showProcessDialog();
    }

    @Override
    public void hideLoading() {
        dismissProcessDialog();
    }


}

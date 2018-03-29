package com.app.baselib.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.baselib.R;
import com.app.baselib.Utils.BitmapUtil;
import com.app.baselib.Utils.ImageDisplayUtil;
import com.app.baselib.adapter.base.RecyclerAdapter;
import com.app.baselib.adapter.base.ViewHolder;
import com.app.baselib.widget.BigImgActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 图片列表
 */
public class ImageInfoAdapter extends RecyclerAdapter<String> {

    private List<String> list;

    private Context mContext;
    private int width;
    private int height;

    //是否可以显示大图
    private boolean isShowBigImg = true;

    //是否可以显示删除图标
    private boolean isShowDeleteImg = false;
    private OnDeleteListener deleteListener;
    public void setShowBigImg(boolean showBigImg) {
        isShowBigImg = showBigImg;
    }
    public void setShowDeleteImg(boolean showDeleteImg) {
        isShowDeleteImg = showDeleteImg;
    }
    public ImageInfoAdapter(Context context, int layoutId, List<String > datas) {
        super(context, layoutId, datas);
        list = datas;
        mContext = context;
    }

    public void setImageSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    @Override
    protected void convert(ViewHolder holder, String bean, int position) {
        ImageView imgView = holder.getView(R.id.base_img);
        ImageView deleteImg = holder.getView(R.id.iv_delete_img);
        if (isShowDeleteImg){
            deleteImg.setVisibility(View.VISIBLE);
        }else {
            deleteImg.setVisibility(View.GONE);
        }
        deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deleteListener !=null){
                    deleteListener.onDelete(position);
                }
            }
        });
        ImageDisplayUtil.displayImage(imgView,bean);
        if(width > 0){
            ViewGroup.LayoutParams params = imgView.getLayoutParams();
            params.width = width;
            params.height = height;
            imgView.setLayoutParams(params);
        }

        imgView.setOnClickListener(v -> {
            ArrayList<String> imagesList = new ArrayList<>();
            for(String img : list){
                imagesList.add(BitmapUtil.getBitmapUrl(img, false));
            }
            Intent intent = new Intent(mContext, BigImgActivity.class);
            intent.putExtra("imgs", imagesList);
            intent.putExtra("position", position);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        });
    }

    public interface OnDeleteListener{

        void onDelete(int position);
    }

    public void setOnDeleteListener(OnDeleteListener deleteListener){
        this.deleteListener  = deleteListener;
    }
}

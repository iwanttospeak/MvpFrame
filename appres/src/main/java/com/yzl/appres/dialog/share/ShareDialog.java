package com.yzl.appres.dialog.share;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.app.baselib.Utils.ReminderUtils;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.yzl.appres.R;
import com.yzl.appres.bean.ShareBean;


/**
 * 分享.
 * Created by wang
 */

public class ShareDialog extends BottomSheetDialogFragment implements WbShareCallback {

    /**
     * 分享内容.
     */
    private ShareBean mShareBean;
    public View mView;

    /**
     * 微博分享.
     */
    private WbShareHandler mWbShareHandler;

    public void setShareBean(ShareBean shareBean) {
        mShareBean = shareBean;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.res_dialog_share, container, false);
        return mView;
    }

    /**
     * 分享微信.
     */
    public View.OnClickListener onShareWeiChatListener() {
        return v -> {
            ShareUtil.shareWx(getActivity(), mShareBean);
            dismiss();
        };
    }

    /**
     * 分享朋友圈.
     */
    public View.OnClickListener onShareFriendListener() {
        return v -> {
            ShareUtil.shareFriend(getActivity(), mShareBean);
            dismiss();
        };
    }

    /**
     * 分享微博.
     */
    public View.OnClickListener onShareWeiBoListener() {
        return v -> {
            mWbShareHandler = ShareUtil.shareSina(getActivity(), mShareBean);
            dismiss();
        };
    }

    /**
     * 复制链接.
     */
    public View.OnClickListener onShareCopyListener() {
        return v -> {
            ShareUtil.CopyUrl(getActivity(), mShareBean);
            dismiss();
        };
    }

    /**
     * 确定事件.
     */
    public View.OnClickListener getOnSureListener() {
        return v -> dismiss();
    }

    public void onNewIntent(Intent intent) {
        if (mWbShareHandler != null) {
            mWbShareHandler.doResultIntent(intent, this);
        }
    }

    @Override
    public void onWbShareSuccess() {
        ReminderUtils.showMessage(R.string.share_to_success);
    }

    @Override
    public void onWbShareCancel() {
        ReminderUtils.showMessage(R.string.share_to_fail);
    }

    @Override
    public void onWbShareFail() {
        ReminderUtils.showMessage(R.string.share_to_fail);
    }
}

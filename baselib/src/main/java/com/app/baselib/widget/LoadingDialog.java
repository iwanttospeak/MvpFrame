package com.app.baselib.widget;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @author
 */
public class LoadingDialog {

    private Context mContext;
    private ProgressDialog mProgressDialog;

    public LoadingDialog(Context context) {
        mContext = context;
        initDialog();
    }

    private void initDialog() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("加载中");
    }

    public void show() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    public void dismiss() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}

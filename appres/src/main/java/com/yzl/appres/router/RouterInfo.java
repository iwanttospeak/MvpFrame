package com.yzl.appres.router;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 登陆页面所需要的跳转信息.
 */

public class RouterInfo implements Parcelable {

    private String mArouterName;
    private Bundle mBundle;
    private Intent mIntent;

    public String getArouterName() {
        return mArouterName;
    }

    public void setArouterName(String arouterName) {
        mArouterName = arouterName;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

    public Intent getIntent() {
        return mIntent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mArouterName);
        dest.writeBundle(this.mBundle);
        dest.writeParcelable(this.mIntent, flags);
    }

    public RouterInfo() {
    }

    protected RouterInfo(Parcel in) {
        this.mArouterName = in.readString();
        this.mBundle = in.readBundle();
        this.mIntent = in.readParcelable(Intent.class.getClassLoader());
    }

    public static final Creator<RouterInfo> CREATOR = new Creator<RouterInfo>() {
        @Override
        public RouterInfo createFromParcel(Parcel source) {
            return new RouterInfo(source);
        }

        @Override
        public RouterInfo[] newArray(int size) {
            return new RouterInfo[size];
        }
    };
}

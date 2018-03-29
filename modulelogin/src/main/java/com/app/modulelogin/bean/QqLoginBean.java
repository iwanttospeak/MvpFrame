package com.app.modulelogin.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yzl.appres.bean.LoginBean;
import com.yzl.appres.bean.PersonalInfoBean;

/**
 * Created by 10488 on 2017-06-22.
 */

public class QqLoginBean implements Parcelable {

    /**
     * errcode : 0
     * message : 成功
     * qq_open_id : FF571E98CF950D16964BB4BEE4E0B5D3
     * oauth : Qq
     * nickname :
     * content : false
     */

    private int errcode;
    private String message;
    private String qq_open_id;
    private String oauth;
    private String nickname;
    private PersonalInfoBean content;

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQq_open_id() {
        return qq_open_id;
    }

    public void setQq_open_id(String qq_open_id) {
        this.qq_open_id = qq_open_id;
    }

    public String getOauth() {
        return oauth;
    }

    public void setOauth(String oauth) {
        this.oauth = oauth;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public PersonalInfoBean getContent() {
        return content;
    }

    public void setContent(PersonalInfoBean content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.errcode);
        dest.writeString(this.message);
        dest.writeString(this.qq_open_id);
        dest.writeString(this.oauth);
        dest.writeString(this.nickname);
        dest.writeParcelable(this.content, flags);
    }

    public QqLoginBean() {
    }

    protected QqLoginBean(Parcel in) {
        this.errcode = in.readInt();
        this.message = in.readString();
        this.qq_open_id = in.readString();
        this.oauth = in.readString();
        this.nickname = in.readString();
        this.content = in.readParcelable(PersonalInfoBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<QqLoginBean> CREATOR = new Parcelable.Creator<QqLoginBean>() {
        @Override
        public QqLoginBean createFromParcel(Parcel source) {
            return new QqLoginBean(source);
        }

        @Override
        public QqLoginBean[] newArray(int size) {
            return new QqLoginBean[size];
        }
    };
}

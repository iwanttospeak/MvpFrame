package com.app.modulelogin.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.yzl.appres.bean.PersonalInfoBean;


public class WeiXinLoginBean implements Parcelable {
    private String province;
    private String city;
    private String oauth;
    private String nickname;
    private String wx_open_id;
    private PersonalInfoBean content;
    private String message;
    private int errcode;
    private String sex;
    private String wx_unionid;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getWx_open_id() {
        return wx_open_id;
    }

    public void setWx_open_id(String wx_open_id) {
        this.wx_open_id = wx_open_id;
    }

    public PersonalInfoBean getContent() {
        return content;
    }

    public void setContent(PersonalInfoBean content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWx_unionid() {
        return wx_unionid;
    }

    public void setWx_unionid(String wx_unionid) {
        this.wx_unionid = wx_unionid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.oauth);
        dest.writeString(this.nickname);
        dest.writeString(this.wx_open_id);
        dest.writeParcelable(this.content, flags);
        dest.writeString(this.message);
        dest.writeInt(this.errcode);
        dest.writeString(this.sex);
        dest.writeString(this.wx_unionid);
    }

    public WeiXinLoginBean() {
    }

    protected WeiXinLoginBean(Parcel in) {
        this.province = in.readString();
        this.city = in.readString();
        this.oauth = in.readString();
        this.nickname = in.readString();
        this.wx_open_id = in.readString();
        this.content = in.readParcelable(PersonalInfoBean.class.getClassLoader());
        this.message = in.readString();
        this.errcode = in.readInt();
        this.sex = in.readString();
        this.wx_unionid = in.readString();
    }

    public static final Parcelable.Creator<WeiXinLoginBean> CREATOR = new Parcelable.Creator<WeiXinLoginBean>() {
        @Override
        public WeiXinLoginBean createFromParcel(Parcel source) {
            return new WeiXinLoginBean(source);
        }

        @Override
        public WeiXinLoginBean[] newArray(int size) {
            return new WeiXinLoginBean[size];
        }
    };
}

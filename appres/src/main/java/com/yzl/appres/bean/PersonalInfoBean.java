package com.yzl.appres.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shen on 2017/5/11.
 */

public class PersonalInfoBean implements Parcelable {

    /**
     * province : null
     * city : null
     * birthday : 0
     * sex :
     * uuid : 943667600
     * nickname : 130****8731
     * realname : null
     * avater : null
     * account : 0.00
     * phone : 130******31
     * access_token : f2f2b0b39de7b09b060c8673150a8f3fbb03c159
     * agent_id : 0
     * active : 1
     * commission : 0.00
     * group_id : 1
     * hx_id : null
     * integration : 0.00
     * shopping_coin : 0
     * group_name : null
     * pname : 总店
     * real_phone : 13006198731
     * cart_num : 0
     * has_pwd : true
     * has_phone : true
     * unread_count : 0
     */

    private String province;
    private String city;
    private String birthday;
    private String sex;
    private String uuid;
    private String nickname;
    private String realname;
    private String avater;
    private String account;
    private String phone;
    private String access_token;
    private String agent_id;
    private String active;
    private String commission;
    private String group_id;
    private String hx_id;
    private String integration;
    private String shopping_coin;
    private String group_name;
    private String pname;
    private String real_phone;
    private String cart_num;
    private boolean has_pwd;
    private boolean has_phone;
    private String unread_count;
    private boolean result = true;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public void setHx_id(String hx_id) {
        this.hx_id = hx_id;
    }

    public void setIntegration(String integration) {
        this.integration = integration;
    }

    public void setShopping_coin(String shopping_coin) {
        this.shopping_coin = shopping_coin;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setReal_phone(String real_phone) {
        this.real_phone = real_phone;
    }

    public void setCart_num(String cart_num) {
        this.cart_num = cart_num;
    }

    public void setHas_pwd(boolean has_pwd) {
        this.has_pwd = has_pwd;
    }

    public void setHas_phone(boolean has_phone) {
        this.has_phone = has_phone;
    }

    public void setUnread_count(String unread_count) {
        this.unread_count = unread_count;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getSex() {
        return sex;
    }

    public String getUuid() {
        return uuid;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRealname() {
        return realname;
    }

    public String getAvater() {
        return avater;
    }

    public String getAccount() {
        return account;
    }

    public String getPhone() {
        return phone;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public String getActive() {
        return active;
    }

    public String getCommission() {
        return commission;
    }

    public String getGroup_id() {
        return group_id;
    }

    public String getHx_id() {
        return hx_id;
    }

    public String getIntegration() {
        return integration;
    }

    public String getShopping_coin() {
        return shopping_coin;
    }

    public String getGroup_name() {
        return group_name;
    }

    public String getPname() {
        return pname;
    }

    public String getReal_phone() {
        return real_phone;
    }

    public String getCart_num() {
        return cart_num;
    }

    public boolean getHas_pwd() {
        return has_pwd;
    }

    public boolean getHas_phone() {
        return has_phone;
    }

    public String getUnread_count() {
        return unread_count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.birthday);
        dest.writeString(this.sex);
        dest.writeString(this.uuid);
        dest.writeString(this.nickname);
        dest.writeString(this.realname);
        dest.writeString(this.avater);
        dest.writeString(this.account);
        dest.writeString(this.phone);
        dest.writeString(this.access_token);
        dest.writeString(this.agent_id);
        dest.writeString(this.active);
        dest.writeString(this.commission);
        dest.writeString(this.group_id);
        dest.writeString(this.hx_id);
        dest.writeString(this.integration);
        dest.writeString(this.shopping_coin);
        dest.writeString(this.group_name);
        dest.writeString(this.pname);
        dest.writeString(this.real_phone);
        dest.writeString(this.cart_num);
        dest.writeByte(this.has_pwd ? (byte) 1 : (byte) 0);
        dest.writeByte(this.has_phone ? (byte) 1 : (byte) 0);
        dest.writeString(this.unread_count);
        dest.writeByte(this.result ? (byte) 1 : (byte) 0);
    }

    public PersonalInfoBean() {
    }

    protected PersonalInfoBean(Parcel in) {
        this.province = in.readString();
        this.city = in.readString();
        this.birthday = in.readString();
        this.sex = in.readString();
        this.uuid = in.readString();
        this.nickname = in.readString();
        this.realname = in.readString();
        this.avater = in.readString();
        this.account = in.readString();
        this.phone = in.readString();
        this.access_token = in.readString();
        this.agent_id = in.readString();
        this.active = in.readString();
        this.commission = in.readString();
        this.group_id = in.readString();
        this.hx_id = in.readString();
        this.integration = in.readString();
        this.shopping_coin = in.readString();
        this.group_name = in.readString();
        this.pname = in.readString();
        this.real_phone = in.readString();
        this.cart_num = in.readString();
        this.has_pwd = in.readByte() != 0;
        this.has_phone = in.readByte() != 0;
        this.unread_count = in.readString();
        this.result = in.readByte() != 0;
    }

    public static final Creator<PersonalInfoBean> CREATOR = new Creator<PersonalInfoBean>() {
        @Override
        public PersonalInfoBean createFromParcel(Parcel source) {
            return new PersonalInfoBean(source);
        }

        @Override
        public PersonalInfoBean[] newArray(int size) {
            return new PersonalInfoBean[size];
        }
    };
}

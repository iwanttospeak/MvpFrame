package com.yzl.appres.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *需要暴露到公共的数据bean
 * @author by admin on 2017/7/8.
 */

public class LoginBean implements Parcelable{


    /**
     * customer : {"customer_id":"5","code":"100005","phone":"18767105143","nickname":"Alex","portrait":"","total_recharge":"0.00","balance":"0.00","token":"d64680224b7963bf199c74ecbe36600f"}
     */

    private CustomerBean customer;

    protected LoginBean(Parcel in) {
    }

    public static final Creator<LoginBean> CREATOR = new Creator<LoginBean>() {
        @Override
        public LoginBean createFromParcel(Parcel in) {
            return new LoginBean(in);
        }

        @Override
        public LoginBean[] newArray(int size) {
            return new LoginBean[size];
        }
    };

    public CustomerBean getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerBean customer) {
        this.customer = customer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static class CustomerBean {
        /**
         * customer_id : 5
         * code : 100005
         * phone : 18767105143
         * nickname : Alex
         * portrait :
         * total_recharge : 0.00
         * balance : 0.00
         * token : d64680224b7963bf199c74ecbe36600f
         */

        private String customer_id;
        private String code;
        private String phone;
        private String nickname;
        private String portrait;
        private String total_recharge;
        private String balance;
        private String token;

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public String getTotal_recharge() {
            return total_recharge;
        }

        public void setTotal_recharge(String total_recharge) {
            this.total_recharge = total_recharge;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}

package com.yzl.appres.bean;

import java.util.List;

/**
 * Created by 沈小建 on 2018-01-15.
 */

public class OrderPayBean {
    /**
     * order_sns : ["SH20180108183527880568"]
     * total_fee : 111
     * balance : 60.00
     */

    private double total_fee;
    private double balance;
    private List<String> order_sns;

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<String> getOrder_sns() {
        return order_sns;
    }

    public void setOrder_sns(List<String> order_sns) {
        this.order_sns = order_sns;
    }
}

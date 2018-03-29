package com.yzl.appres.bean;

/**
 * Created by 沈小建 on 2018-01-16.
 */

public class PayBean {
    /**
     * pay_code : account
     * order_sn : PO20180108183809221443
     * order_type : buy
     * body : 支付订单111元
     * subject : 支付订单
     */

    private String pay_code;
    private String order_sn;
    private String order_type;
    private String body;
    private String subject;

    public String getPay_code() {
        return pay_code;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

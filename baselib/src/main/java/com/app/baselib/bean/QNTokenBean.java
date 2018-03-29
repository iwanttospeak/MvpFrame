package com.app.baselib.bean;

/**
 * 获取七牛token
 * @author by Wang on 2017/7/31.
 */

public class QNTokenBean {

    /**
     * domain : static.zertone1.com
     * bucket : testpinet
     * zone : 0
     * token : DVQXzBtUPbOw8Fge-xI2gdYQzEQPNFx9FQcf9ti7:WyZiMpnPiRqzt3fSkcozq2fRrlU=:eyJzY29wZSI6InRlc3RwaW5ldCIsImRlYWRsaW5lIjoxNTAxNDg4MzM2fQ==
     * deadline : 1501488276
     * "uptoken": "HwEklakXRHiT-Un1RBaW6eC6sTNJJ9WrQvVSEw1R:Nc2v7PEXq6Ii_C9xx9hjo5eDLH8=:eyJzY29wZSI6ImNpZ2FycW4iLCJkZWFkbGluZSI6MTUyMTAxODMzOH0="
     */
    private String domain;
    private String bucket;
    private int zone;
    private String token;
    private int deadline;

    private String uptoken;

    public String getUptoken() {
        return uptoken;
    }

    public void setUptoken(String uptoken) {
        this.uptoken = uptoken;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getDeadline() {
        return deadline;
    }

    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }
}

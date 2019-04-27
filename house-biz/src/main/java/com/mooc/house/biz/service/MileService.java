package com.mooc.house.biz.service;

public interface MileService {
    public void sendMail(String type, String url, String email);

    void registerNotify(String email);

    boolean enable(String key);
}

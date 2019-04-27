package com.mooc.house.biz.service;

import com.mooc.house.common.model.User;

import java.util.List;

public interface UserService {
    boolean addAccount(User account);

    boolean enable(String key);

    User auth(String username, String password);

    void updateUser(User updateUser, String email);

    List<User> getUserByQuery(User query);
}

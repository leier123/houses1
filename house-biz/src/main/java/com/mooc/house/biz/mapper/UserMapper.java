package com.mooc.house.biz.mapper;

import com.mooc.house.common.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {


    void insert(User account);

    int delete(String email);

    int update(User updateUser);

    List<User> selectUsersByQuery(User user);

}

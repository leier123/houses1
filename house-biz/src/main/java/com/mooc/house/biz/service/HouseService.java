package com.mooc.house.biz.service;

import com.mooc.house.common.constants.HouseUserType;
import com.mooc.house.common.model.*;
import com.mooc.house.common.page.PageData;
import com.mooc.house.common.page.PageParams;

import java.util.List;

public interface HouseService {
    PageData<House> queryHouse(House query, PageParams build);

    House queryOneHouse(Long id);

    void addUserMsg(UserMsg userMsg);

    HouseUser getHouseUser(Long id);

    List<House> queryAndSetImg(House query, PageParams build);

    List<Community> getAllCommunitys();

    void add(House house, User user);

    void updateRating(Long id, Double rating);

    void bindUserZHouse(Long id, Long id1, boolean b);

    void unbindUserZHouse(Long id, Long id1, HouseUserType bookmark);
}

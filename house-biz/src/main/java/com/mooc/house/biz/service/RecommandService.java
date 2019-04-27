package com.mooc.house.biz.service;

import com.mooc.house.common.model.House;

import java.util.List;

public interface RecommandService {
    List<House> getHotHouse(Integer i);

    void increase(Long id);

    List<House> getLasttest();
}

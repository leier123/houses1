package com.mooc.house.biz.service.Impl;

import com.google.common.collect.Lists;
import com.mooc.house.biz.service.CityService;
import com.mooc.house.common.model.City;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    public List<City> getAllCitys(){
        City city = new City();
        city.setCityCode("110000");
        city.setCityName("北京市");
        city.setId(1);
        return Lists.newArrayList();
    }
}

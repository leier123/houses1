package com.mooc.house.biz.mapper;

import com.mooc.house.common.model.*;
import com.mooc.house.common.page.PageParams;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HouseMapper {

    public List<House> selectPageHouses(@Param("house") House house, @Param("pageParams")PageParams pageParams);

    public Long selectPageCount(@Param("house") House query);

    List<Community> selectCommunity(Community community);

    void insertUserMsg(UserMsg userMsg);

    HouseUser selectSaleHouseUser(Long id);

    int insert(House house);

    HouseUser selectHouseUser(@Param("userId") Long userId, @Param("id") Long houseId, @Param("type") Integer type);

    int insertHouseUser(HouseUser houseUser);

    int updateHouse(House updateHouse);

    int deleteHouseUser(@Param("id") Long id, @Param("userId") Long userId, @Param("type") Integer value);
}

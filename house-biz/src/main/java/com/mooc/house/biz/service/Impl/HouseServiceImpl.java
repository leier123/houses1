package com.mooc.house.biz.service.Impl;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mooc.house.biz.mapper.HouseMapper;
import com.mooc.house.biz.service.AgencyService;
import com.mooc.house.biz.service.FileService;
import com.mooc.house.biz.service.HouseService;
import com.mooc.house.biz.service.MileService;
import com.mooc.house.common.constants.HouseUserType;
import com.mooc.house.common.model.*;
import com.mooc.house.common.page.PageData;
import com.mooc.house.common.page.PageParams;
import com.mooc.house.common.utils.BeanHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Value("${file.prefix}")
    private String imgPrefix;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private MileService mileService;

    @Autowired
    private FileService fileService;


    /**
     * 查询小区
     * 添加图片服务器地址前缀
     * 构建分页结果
     * @param query
     * @param pageParams
     */
    @Override
    public PageData<House> queryHouse(House query, PageParams pageParams) {
        List<House> houses = Lists.newArrayList();
        if (!Strings.isNullOrEmpty(query.getName())){
            Community community = new Community();
            community.setName(query.getName());
            List<Community> communities = houseMapper.selectCommunity(community);
            if (!communities.isEmpty()){
                query.setCommunityId(communities.get(0).getId());
            }
        }
        houses = queryAndSetImg(query,pageParams);
        Long count = houseMapper.selectPageCount(query);
        return PageData.buildPage(houses,count,pageParams.getPageSize(),pageParams.getPageNum());
    }

    /**
     * 用户详情
     * @param id
     * @return
     */
    @Override
    public House queryOneHouse(Long id) {
        House query = new House();
        query.setId(id);
        List<House> houses = queryAndSetImg(query,PageParams.build(1,1));
        if (!houses.isEmpty()){
            return houses.get(0);
        }
        return null;
    }

    public List<House> queryAndSetImg(House query, PageParams pageParams) {
        List<House> houses = houseMapper.selectPageHouses(query, pageParams);
        houses.forEach(h->{
            h.setFirstImg(imgPrefix + h.getFirstImg());
            h.setImageList(h.getImageList().stream().map(img -> imgPrefix+img).collect(Collectors.toList()));
            h.setFloorPlanList(h.getFloorPlanList().stream().map(img ->imgPrefix+img).collect(Collectors.toList()));
        });
        return houses;
    }

    @Override
    public void addUserMsg(UserMsg userMsg) {
        BeanHelper.onInsert(userMsg);
        houseMapper.insertUserMsg(userMsg);
        User user = agencyService.getAgentDeail(userMsg.getAgentId());
        mileService.sendMail("来自用户"+userMsg.getEmail()+"的留言",userMsg.getMsg(),user.getEmail());
    }

    @Override
    public HouseUser getHouseUser(Long id) {
        HouseUser houseUser =  houseMapper.selectSaleHouseUser(id);
        return houseUser;
    }

    @Override
    public List<Community> getAllCommunitys() {
        Community community = new Community();
        return houseMapper.selectCommunity(community);
    }

    /**
     * 添加房产
     * 1、添加房产图片
     * 2、添加户型图片
     * 3、插入房产信息
     * 4、绑定用户到房产的关系
     * @param house
     * @param user
     */
    @Override
    public void add(House house, User user) {
        if (CollectionUtils.isNotEmpty(house.getHouseFiles())){
            String images = Joiner.on(",").join(fileService.getImgPath(house.getHouseFiles()));
            house.setImages(images);
        }
        if (CollectionUtils.isNotEmpty(house.getFloorPlanFiles())){
            String images = Joiner.on(",").join(fileService.getImgPath(house.getFloorPlanFiles()));
            house.setFloorPlan(images);
        }
        BeanHelper.onInsert(house);
        houseMapper.insert(house);
        bindUserZHouse(house.getId(),user.getId(),false);
    }

    /**
     * 评分
     */
    @Override
    public void updateRating(Long id, Double rating) {
        House house = queryOneHouse(id);
        Double oldRating = house.getRating();
        Double newRating = oldRating.equals(0D)? rating:Math.min((oldRating+rating)/2,5);
        House updateHouse = new House();
        updateHouse.setId(id);
        updateHouse.setRating(newRating);
        BeanHelper.onInsert(updateHouse);
        houseMapper.updateHouse(updateHouse);
    }

    @Override
    public void bindUserZHouse(Long houseId, Long userId, boolean isCollect) {
        HouseUser exitHouseUser = houseMapper.selectHouseUser(userId,houseId,isCollect? HouseUserType.BOOKMARK.value:HouseUserType.SALE.value);
        if (exitHouseUser!=null){
            return;
        }
        HouseUser houseUser = new HouseUser();
        houseUser.setHouseId(houseId);
        houseUser.setUserId(userId);
        houseUser.setType(isCollect?HouseUserType.BOOKMARK.value:HouseUserType.SALE.value);
        BeanHelper.setDefaultProp(houseUser,HouseUser.class);
        BeanHelper.onInsert(houseUser);
        houseMapper.insertHouseUser(houseUser);
    }

    @Override
    public void unbindUserZHouse(Long id, Long userId, HouseUserType type) {
        houseMapper.deleteHouseUser(id,userId,type.value);
    }
}

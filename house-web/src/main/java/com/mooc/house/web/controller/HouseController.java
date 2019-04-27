package com.mooc.house.web.controller;

import com.mooc.house.biz.service.AgencyService;
import com.mooc.house.biz.service.CityService;
import com.mooc.house.biz.service.HouseService;
import com.mooc.house.biz.service.RecommandService;
import com.mooc.house.common.constants.CommonConstants;
import com.mooc.house.common.constants.HouseUserType;
import com.mooc.house.common.model.House;
import com.mooc.house.common.model.HouseUser;
import com.mooc.house.common.model.User;
import com.mooc.house.common.model.UserMsg;
import com.mooc.house.common.page.PageData;
import com.mooc.house.common.page.PageParams;
import com.mooc.house.common.result.ResultMsg;
import com.mooc.house.common.utils.BeanHelper;
import com.mooc.house.web.interceptor.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "house")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private RecommandService recommandService;

    @Autowired
    private CityService cityService;

    /**
     * 实现分页
     *支持小区搜索
     * 支持排序
     * 支持显示图片、价格、标题、地址等信息
     * @param pageSize//分页参数
     * @param pageNum//分页参数
     * @param query//模糊接收的对象
     * @param modelMap//传值
     * @return
     */
    @RequestMapping(value = "list")
    public String houseList(Integer pageSize, Integer pageNum, House query, ModelMap modelMap){
        PageData<House> ps = houseService.queryHouse(query, PageParams.build(pageSize, pageNum));
        List<House> hotHouses = recommandService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses",hotHouses);
        modelMap.put("ps",ps);
        modelMap.put("vo",query);
        return "house/listing";
    }

    @RequestMapping(value = "detail")
    public String houseDetail(Long id,ModelMap modelMap){
        House house = houseService.queryOneHouse(id);
        HouseUser houseUser = houseService.getHouseUser(id);
        recommandService.increase(id);
        if (houseUser.getUserId()!=null && !houseUser.getUserId().equals(0)){
            User deail = agencyService.getAgentDeail(houseUser.getUserId());
            modelMap.put("agent",deail);
        }
        List<House> hotHouses = recommandService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses",hotHouses);
        modelMap.put("house",house);
        return "/house/detail";
    }

    @RequestMapping(value = "leaveMsg")
    public String houseMsg(UserMsg userMsg){
        houseService.addUserMsg(userMsg);
        return "redirect:/house/detail?id="+userMsg.getHouseId();
    }

    @RequestMapping(value = "/toAdd")
    public String toAdd(ModelMap modelMap){
        modelMap.put("citys",cityService.getAllCitys());
        modelMap.put("communitys",houseService.getAllCommunitys());
        return "/house/add";
    }

    /**
     * 获取用户
     * 设置房产状态
     * 添加房产
     * @param house
     * @return
     */
    @RequestMapping(value = "/add")
    public String doAdd(House house){
        User user = UserContext.getUser();
        house.setState(CommonConstants.HOUSE_STATE_UP);
        houseService.add(house,user);
        return "redirect:/house/ownlist";
    }

    @RequestMapping("ownlist")
    public String ownlist(House house,Integer pageNum,Integer pageSize,ModelMap modelMap){
        User user = UserContext.getUser();
        house.setUserId(user.getId());
        house.setBookmarked(false);
        modelMap.put("ps",houseService.queryHouse(house,PageParams.build(pageSize,pageNum)));
        modelMap.put("pageType","own");
        return "/house/ownlist";
    }
    /**
     * 房屋收藏
     */
    @RequestMapping(value = "bookmark")
    @ResponseBody
    public ResultMsg bookmark(Long id,ModelMap modelMap){
        User user = UserContext.getUser();
        houseService.bindUserZHouse(id,user.getId(),true);
        return ResultMsg.successMsg("ok");
    }

    /**
     * 取消收藏
     */
    @RequestMapping(value = "unbookmark")
    @ResponseBody
    public ResultMsg unbookmark(Long id){
        User user = UserContext.getUser();
        houseService.unbindUserZHouse(id,user.getId(), HouseUserType.BOOKMARK);
        return ResultMsg.successMsg("ok");
    }

    /**
     * 收藏列表
     */
    @RequestMapping(value = "bookmarked")
    public String bookmarked(House house,Integer pageSize,Integer pageNum,ModelMap modelMap){
        User user = UserContext.getUser();
        house.setBookmarked(true);
        house.setUserId(user.getId());
        modelMap.put("ps",houseService.queryHouse(house,PageParams.build(pageSize,pageNum)));
        modelMap.put("pageType","book");
        return "/house/ownlist";
    }
    /**
     * 评分
     */
    @RequestMapping(value = "rating")
    @ResponseBody
    public ResultMsg houseRate(Double rating,Long id){
        houseService.updateRating(id,rating);
        return ResultMsg.successMsg("ok");
    }
}

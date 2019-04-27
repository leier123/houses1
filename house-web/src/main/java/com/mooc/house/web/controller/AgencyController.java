package com.mooc.house.web.controller;

import com.mooc.house.biz.service.AgencyService;
import com.mooc.house.biz.service.HouseService;
import com.mooc.house.biz.service.RecommandService;
import com.mooc.house.common.constants.CommonConstants;
import com.mooc.house.common.model.House;
import com.mooc.house.common.model.User;
import com.mooc.house.common.page.PageData;
import com.mooc.house.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value ="agency" )
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private RecommandService recommandService;

    @RequestMapping(value = "agentList")
    public String agentList(Integer pageSize, Integer pageNum, ModelMap modelMap){
        PageData<User> ps = agencyService.getAllAgent(PageParams.build(pageSize,pageNum));
        List<House> hotHouses = recommandService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses",hotHouses);
        modelMap.put("ps",ps);
        return "/user/agent/agentList";
    }

    @RequestMapping(value = "agentDetail")
    public String agentDetail(Long id,ModelMap modelMap){
        User user = agencyService.getAgentDeail(id);
        House query = new House();
        query.setUserId(id);
        query.setBookmarked(false);
        PageData<House> bindHouse = houseService.queryHouse(query, new PageParams(3, 1));
        if (bindHouse != null){
            modelMap.put("bindHouses",bindHouse.getList());
        }
        List<House> hotHouses = recommandService.getHotHouse(CommonConstants.RECOM_SIZE);
        modelMap.put("recomHouses",hotHouses);
        modelMap.put("agent",user);
        return "/user/agent/agentDetail";
    }
}

package com.mooc.house.web.controller;

import com.mooc.house.biz.service.RecommandService;
import com.mooc.house.common.model.House;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomePageController {

    @Autowired
    private RecommandService recommandService;

    @RequestMapping(value = "index")
    public String index(ModelMap modelMap){
        List<House> houses = recommandService.getLasttest();
        modelMap.put("recomHouses",houses);
        return  "/homepage/index";
    }

    @RequestMapping(value = "")
    public String home(ModelMap modelMap){
        return "redirect:/index";
    }
}

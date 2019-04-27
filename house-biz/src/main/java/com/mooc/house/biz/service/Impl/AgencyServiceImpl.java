package com.mooc.house.biz.service.Impl;

import com.mooc.house.biz.mapper.AgencyMapper;
import com.mooc.house.biz.service.AgencyService;
import com.mooc.house.common.model.User;
import com.mooc.house.common.page.PageData;
import com.mooc.house.common.page.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyServiceImpl implements AgencyService {

    @Autowired
    private AgencyMapper agencyMapper;

    @Value("${file.prefix}")
    private String imgpreFix;

    /**
     * 访问user表获取详情
     * 添加用户头像
     * @param userId
     * @return
     */
    @Override
    public User getAgentDeail(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setType(2);
        List<User> list = agencyMapper.selectAgent(user, PageParams.build(1, 1));
        setImg(list);
        if (!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    public void setImg(List<User> list) {
        list.forEach(i ->{
            i.setAvatar(imgpreFix + i.getAvatar());
        });
    }

    @Override
    public PageData<User> getAllAgent(PageParams pageParams) {
        List<User> agents = agencyMapper.selectAgent(new User(),pageParams);
        setImg(agents);
        Long count = agencyMapper.selectAgentCount(new User());
        return PageData.buildPage(agents,count,pageParams.getPageSize(),pageParams.getPageNum());
    }
}

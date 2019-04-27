package com.mooc.house.biz.service;

import com.mooc.house.common.model.User;
import com.mooc.house.common.page.PageData;
import com.mooc.house.common.page.PageParams;

public interface AgencyService {
    User getAgentDeail(Long userId);

    PageData<User> getAllAgent(PageParams build);
}

package com.mooc.house.web.controller;

import com.mooc.house.common.model.User;
import com.mooc.house.common.result.ResultMsg;
import org.apache.commons.lang3.StringUtils;

public class UserHelper {

    public static ResultMsg validate(User account){
        if (StringUtils.isBlank(account.getEmail())){
            return ResultMsg.errorMsg("Email 有误");
        }
        if (StringUtils.isBlank(account.getEmail()) || StringUtils.isBlank(account.getPasswd()) || !account.getPasswd().equals(account.getConfirmPasswd())){
            return ResultMsg.errorMsg("两次密码不一致");
        }
        if (account.getPasswd().length()<6){
            return ResultMsg.errorMsg("密码小于6位");
        }
        return ResultMsg.successMsg("");
    }
}

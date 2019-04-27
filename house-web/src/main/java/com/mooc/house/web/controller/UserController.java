package com.mooc.house.web.controller;

import com.mooc.house.biz.service.UserService;
import com.mooc.house.common.constants.CommonConstants;
import com.mooc.house.common.model.User;
import com.mooc.house.common.result.ResultMsg;
import com.mooc.house.common.utils.HashUtils;
import com.mooc.house.web.interceptor.UserContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "accounts")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "index")
    public String index(){
        return "homepage/index";
    }

    /**
     * 注册
     * @param account //对象
     * @param modelMap //request的存值
     * @return
     */
    @RequestMapping(value = "/register")
    public String accountsRegister(User account, ModelMap modelMap){
        if (account==null || account.getName()==null){
            return "/user/accounts/register";
        }
        //用户验证
        ResultMsg resultMsg = UserHelper.validate(account);
        if (resultMsg.isSuccess()&&userService.addAccount(account)){
            modelMap.put("email",account.getEmail());
            return "/user/accounts/registerSubmit";
        }else {
            return "redirect:/register?"+resultMsg.asUrlParams();
        }
    }

    /**
     * 认证
     * @param key
     * @return
     */
    @RequestMapping("verify")
    public String verify(String key){
        boolean result = userService.enable(key);
        if (result){
            return "redirect:index?"+ResultMsg.successMsg("激活成功").asUrlParams();
        }else{
            return "redirect:/register?"+ResultMsg.errorMsg("请确认连接是否过期").asUrlParams();
        }
    }

    //------------------------登录流程------------------------
    /**
     * 登录接口
     */
    @RequestMapping(value = "signin")
    public String signin(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String target = request.getParameter("target");
        if (username == null || password == null){
            request.setAttribute("target",target);
            return "/user/accounts/signin";
        }
        User user = userService.auth(username,password);
        if (user == null){
            return "redirect:/accounts/signin?target="+target+"&username=" + username + "&"+ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
        }else {
            HttpSession session = request.getSession(true);
            session.setAttribute(CommonConstants.USER_ATTRIBUTE,user);
            //session.setAttribute(CommonConstants.PLAIN_USER_ATTRIBUTE,user);
            return StringUtils.isNoneBlank(target)?"redirect:"+target:"redirect:/index";
        }
    }

    /**
     * 退出
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(true);
        session.invalidate();
        return "redirect:/accounts/index";
    }
    //------------个人信息页面----------------

    /**
     * 用户请求页
     * 1.能够提供页面信息
     * 2.更新用户信息
     * @param updateUser
     * @param request
     * @return
     */
    @RequestMapping(value = "profile")
    public String profile(User updateUser,HttpServletRequest request,ModelMap modelMap){
        if (updateUser.getEmail() == null){
            return "/user/accounts/profile";
        }
        userService.updateUser(updateUser,updateUser.getEmail());
        User query = new User();
        query.setEmail(updateUser.getEmail());
        List<User> users = userService.getUserByQuery(query);
        request.getSession(true).setAttribute(CommonConstants.USER_ATTRIBUTE,users.get(0));
        return "redirect:/accounts/profile?"+ResultMsg.successMsg("更新成功").asUrlParams();
    }

    /**
     * 更改密码操作
     * @param email
     * @param password
     * @param newPassword
     * @param confirmPassword
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "changePassword")
    public String changePassword(String email,String password,String newPassword,String confirmPassword,ModelMap modelMap){
        User user = userService.auth(email,password);
        if (user == null || !confirmPassword.equals(newPassword)){
            return "redirect:/accounts/profile?"+ResultMsg.errorMsg("密码错误").asUrlParams();
        }
        User updateUser = new User();
        updateUser.setPasswd(HashUtils.encryPassword(newPassword));
        userService.updateUser(updateUser,email);
        return "redirect:/accounts/profile?"+ResultMsg.successMsg("更新成功").asUrlParams();
    }
}

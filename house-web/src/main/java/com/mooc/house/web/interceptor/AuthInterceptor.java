package com.mooc.house.web.interceptor;

import com.google.common.base.Joiner;
import com.mooc.house.common.constants.CommonConstants;
import com.mooc.house.common.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 拦截器
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) {
        Map<String,String[]> map = request.getParameterMap();
        map.forEach((k,v)->{
            if (k.equals("errorMsg") || k.equals("successMsg") || k.equals("target")){
                request.setAttribute(k, Joiner.on(",").join(v));
            }
        });

        //获取url
        String uri = request.getRequestURI();
        //如果为static和error页面放过
        if (uri.startsWith("/static") || uri.startsWith("/error")){
            return true;
        }
        //获取session
        HttpSession session = request.getSession(true);
        //从session中取出USER_ATTRIBUTE的user对象
        User user = (User) session.getAttribute(CommonConstants.USER_ATTRIBUTE);
        if (user!=null){
            UserContext.setUser(user);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        UserContext.remove();
    }
}

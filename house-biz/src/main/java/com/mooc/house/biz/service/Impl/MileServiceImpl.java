package com.mooc.house.biz.service.Impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.mooc.house.biz.mapper.UserMapper;
import com.mooc.house.biz.service.MileService;
import com.mooc.house.common.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class MileServiceImpl implements MileService {

    private final Cache<String,String> registerCache = CacheBuilder.newBuilder().maximumSize(100).
            expireAfterAccess(15, TimeUnit.MINUTES).removalListener(new RemovalListener<String, String>() {

        @Override
        public void onRemoval(RemovalNotification<String,String> notification){
            String email = notification.getValue();
            User user = new User();
            user.setEmail(email);
            List<User> targetUser = userMapper.selectUsersByQuery(user);
            if (!targetUser.isEmpty()&& Objects.equals(targetUser.get(0).getEnable(),0)){
                userMapper.delete(email);
            }

        }
    }).build();

    @Autowired
    private UserMapper userMapper;

    @Value("${domin.name}")
    private String dominName;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendMail(String title, String url, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject(title);
        message.setTo(email);
        message.setText(url);
        javaMailSender.send(message);
    }

    /**
     * 1.缓存key——email的关系
     * 2.利用spring main 发送邮件
     * 3.借用异步框架进行异步操作
     * @param email
     */
    @Async
    public void registerNotify(String email) {
        String randomKey = RandomStringUtils.randomAlphanumeric(10);
        registerCache.put(randomKey,email);
        String url = "http://" + dominName + "/accounts/verify?key=" + randomKey;
        sendMail("房产平台激活邮件", url, email);
    }

    @Override
    public boolean enable(String key) {
        String email = registerCache.getIfPresent(key);
        if (StringUtils.isBlank(email)){
            return false;
        }
        User updateUser = new User();
        updateUser.setEmail(email);
        updateUser.setEnable(1);
        userMapper.update(updateUser);
        registerCache.invalidate(key);
        return true;
    }
}

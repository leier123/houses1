package com.mooc.house.biz.service.Impl;

import com.google.common.collect.Lists;
import com.mooc.house.biz.mapper.UserMapper;
import com.mooc.house.biz.service.FileService;
import com.mooc.house.biz.service.MileService;
import com.mooc.house.biz.service.UserService;
import com.mooc.house.common.model.User;
import com.mooc.house.common.utils.BeanHelper;
import com.mooc.house.common.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MileService mileService;

    @Value("${file.prefix}")
    private String imgPrefix;

    @Autowired
    private FileService fileService;

    /**
     * 1.插入数据库（用户信息）非激活，密码加盐MD5：保存头像到本地
     * 2.生成Key，绑定email
     * 3.发送邮件给用户
     * @param account
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean addAccount(User account) {
        account.setPasswd(HashUtils.encryPassword(account.getPasswd()));
        List<String> imgList = fileService.getImgPath(Lists.newArrayList(account.getAvatarFile()));
        if (!imgList.isEmpty()){
            account.setAvatar(imgList.get(0));
        }
        BeanHelper.setDefaultProp(account,User.class);
        BeanHelper.onInsert(account);
        account.setEnable(0);
        userMapper.insert(account);
        mileService.registerNotify(account.getEmail());
        return true;
    }

    @Override
    public boolean enable(String key) {
        return mileService.enable(key);
    }

    /**
     * 用户名密码验证
     * @param username
     * @param password
     * @return
     */
    @Override
    public User auth(String username, String password) {
        User user = new User();
        user.setEmail(username);
        user.setPasswd(HashUtils.encryPassword(password));
        user.setEnable(1);
        List<User> list = getUserByQuery(user);
        if (!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    /**
     * 用户修改个人信息
     * @param updateUser
     * @param email
     */
    @Override
    public void updateUser(User updateUser, String email) {
        updateUser.setEmail(email);
        BeanHelper.onUpdate(updateUser);
        userMapper.update(updateUser);
    }

    public List<User> getUserByQuery(User user) {
        List<User> list = userMapper.selectUsersByQuery(user);
        list.forEach(u->{
            u.setAvatar(imgPrefix+u.getAvatar());
        });
        return list;
    }
}

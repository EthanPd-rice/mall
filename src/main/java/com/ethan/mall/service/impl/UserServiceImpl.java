package com.ethan.mall.service.impl;

import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.exception.EthanMallExceptionEnum;
import com.ethan.mall.model.dao.UserMapper;
import com.ethan.mall.model.pojo.User;
import com.ethan.mall.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public User getUser(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void register(String username, String password) throws EthanMailException {
        User result = userMapper.selectByName(username);
        //本应根据result是否为null，进行下一步判断并返回，但这是controller的工作，不可以在service实现类做响应success或fail
        //这时候可以抛出自定义异常的方法解决,将异常跑出来给controller
        if(result != null){
            throw new EthanMailException(EthanMallExceptionEnum.NAME_EXISTED);
        }
        //没有重名信息，写进数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        //使用userMapper.insert的话，要全部都有值，只录入用户名密码要用insertSelective，xml有对应方法
        int count = userMapper.insertSelective(user);
        if(count == 0){
            throw new EthanMailException(EthanMallExceptionEnum.INSERT_FAILED);
        }
    }
}

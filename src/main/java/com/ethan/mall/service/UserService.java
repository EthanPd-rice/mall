package com.ethan.mall.service;

import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.model.pojo.User;

public interface UserService {
    User getUser(Integer id);
    void register(String username,String password) throws EthanMailException;
}

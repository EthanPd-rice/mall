package com.ethan.mall.service;

import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.model.pojo.User;

public interface UserService {
    User getUser(Integer id);
    void register(String username,String password) throws EthanMailException;

    User login(String userName, String password) throws EthanMailException;

    void updateInformation(User user) throws EthanMailException;

    boolean checkAdminRole(User user);
}

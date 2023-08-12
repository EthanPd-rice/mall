package com.ethan.mall.service;

import com.ethan.mall.exception.EthanMailException;
import com.ethan.mall.model.pojo.User;

/**
 * 用户Service
 */
public interface UserService {
    User getUser(Integer id);
    void register(String username,String password,String emailAddress) throws EthanMailException;

    User login(String userName, String password) throws EthanMailException;

    void updateInformation(User user) throws EthanMailException;

    boolean checkAdminRole(User user);


    boolean checkEmailRegistered(String emailAddress);
}

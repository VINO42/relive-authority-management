package com.relive.service;

import com.relive.entity.User;

/**
 * @author: ReLive
 * @date: 2021/12/7 12:59 下午
 */
public interface UserService {

    User getUserByUsername(String username);
}

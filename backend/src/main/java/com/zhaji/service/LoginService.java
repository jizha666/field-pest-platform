package com.zhaji.service;

import com.zhaji.pojo.User;

public interface LoginService {
    User login(User user);

    Boolean identityCheck(User user);

    User queryUser(String username);

    Boolean usernameCheck(User user);

    void updateUser(User user);
}

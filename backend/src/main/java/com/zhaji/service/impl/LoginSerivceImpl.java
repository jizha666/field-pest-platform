package com.zhaji.service.impl;

import com.zhaji.mapper.LoginMapper;
import com.zhaji.pojo.User;
import com.zhaji.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginSerivceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;


    @Override
    public User login(User user) {
        return loginMapper.getByUsernameAndPassword(user);
    }

    @Override
    public Boolean identityCheck(User user) {
        if(Integer.valueOf(1).equals(user.getIdentity())){
            return true;
        }
        User result = loginMapper.identityCheck(user);
        return result != null;
    }

    @Override
    public User queryUser(String username) {
        return loginMapper.queryUser(username);
    }

    @Override
    public Boolean usernameCheck(User user) {
        User result = loginMapper.usernameCheck(user);
        return result != null;
    }

    @Override
    public void updateUser(User user) {
        loginMapper.updateUser(user);
    }
}


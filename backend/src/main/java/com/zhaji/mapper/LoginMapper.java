package com.zhaji.mapper;

import com.zhaji.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface LoginMapper {

    @Select("select * from user where username = #{username} and password = #{password}")
    User getByUsernameAndPassword(User user);

    @Select("select * from user where username = #{username} and identity = 0")
    User identityCheck(User user);

    @Select("select * from user where username = #{username}")
    User queryUser(String username);

    @Select("select * from user where username = #{username} and id != #{id}")
    User usernameCheck(User user);

    @Update("update user set username = #{username}, name = #{name}, password = #{password} where id = #{id}")
    void updateUser(User user);
}

package com.company.dao;

import com.company.domain.User;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public interface UserDao {
    public List<User> findAll();  // 用于查找所有用户信息

    int updateAll(User user);

    int updatePassword(String account, String password);

    int insert(User user);

    int delete(StringBuffer[] condition);

    int deleteByAccount(String account);

    User findUser(String account, String password);


    User findByAccount(String account);
}

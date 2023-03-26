package com.company.service;

import com.company.domain.User;
import redis.clients.jedis.Jedis;

public interface UserService {

    User findUser(String account, String password);

    User findByAccount(String account);

    int insert(User user);

    int deleteByAccount(String account);

    boolean saveUserInRedis(Jedis jedis, User user);

    Integer collectSpot(String account, Integer sid);

    Integer unCollectSpot(String account, Integer sid);
}

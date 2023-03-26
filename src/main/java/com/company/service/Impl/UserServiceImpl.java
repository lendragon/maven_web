package com.company.service.Impl;

import com.company.dao.Impl.CollectDaoImpl;
import com.company.dao.Impl.UserDaoImpl;
import com.company.domain.Page;
import com.company.domain.User;
import com.company.service.UserService;
import com.company.util.JedisUtils;
import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDaoImpl udi = new UserDaoImpl();

    /**
     * 判断用户名和密码是否正确
     * @param account 用户名
     * @param password 密码
     * @return 数据库中的该用户
     */
    @Override
    public User findUser(String account, String password) {
        Jedis jedis = JedisUtils.getJedis();
        User user = null;

        if (jedis.exists(account)) {
            // 如果内存中存在该数据, 则直接返回
            try {
                User userTmp = new User();
                // 将查询的结果封装为对象
                BeanUtils.populate(userTmp, jedis.hgetAll(account));
                // 密码相等在赋值
                if (userTmp.getPassword().equals(password)) {
                    user = userTmp;
                }
            } catch (Exception e) {
            }
        } else {
            // 如果不存在, 则查询数据库
            user = udi.findUser(account, password);
            // 将数据存储在redis中
            saveUserInRedis(jedis, user);
        }

        jedis.close();
        return user;
    }

    @Override
    public User findByAccount(String account) {
        if (account == null) {
            return null;
        }

        Jedis jedis = JedisUtils.getJedis();
        User user = null;
        if (jedis.exists(account)) {
            // 如果redis中存在则直接返回
            Map<String, String> map = jedis.hgetAll(account);
            try {
                User userTmp = new User();
                BeanUtils.populate(userTmp, map);
                user = userTmp;
            } catch (Exception e) {}
        } else {
            // redis中不存在, 查询数据库并存储数据进redis
            user = udi.findByAccount(account);
            saveUserInRedis(jedis, user);
        }

        jedis.close();
        return user;
    }
    @Override
    public int insert(User user) {
        Jedis jedis = JedisUtils.getJedis();

        // 需要更新redis数据库
        saveUserInRedis(jedis, user);

        jedis.close();
        return udi.insert(user);
    }

    @Override
    public int deleteByAccount(String account) {
        Jedis jedis = JedisUtils.getJedis();
        if (jedis.exists(account)) {
            // 如果redis中存在数据, 则删除
            jedis.del(account);
        }

        jedis.close();
        return udi.deleteByAccount(account);
    }

    @Override
    public boolean saveUserInRedis(Jedis jedis, User user) {
        if (user == null) {
            return false;
        }

        jedis.hset(user.getAccount(), "username", user.getUsername());
        jedis.hset(user.getAccount(), "account", user.getAccount());
        jedis.hset(user.getAccount(), "password", user.getPassword());
        if (user.getGender() != null) {
            jedis.hset(user.getAccount(), "gender", user.getGender());
        }
        if (user.getGender() != null) {
            jedis.hset(user.getAccount(), "age", user.getAge().toString());
        }
        if (user.getGender() != null) {
            jedis.hset(user.getAccount(), "email", user.getEmail());
        }
        return true;
    }

    @Override
    public Integer collectSpot(String account, Integer sid) {
        if (account == null || sid == null || "".equals(account)) {
            return null;
        }

        // 查找是否存在该用户和该sid
        if (udi.findByAccount(account) == null || (new SpotServiceImpl().findBySid(sid) == null)) {
            // 不存在则返回null
            return null;
        }

        // 如果存在, 则写入数据库并返回结果
        Integer result = new CollectDaoImpl().insertCollect(account, sid);

        Jedis jedis = JedisUtils.getJedis();
        // 如果redis中存在该数据则需要更新
        if (result != null && result == 1 && jedis.exists(account + "_collect")) {
            jedis.sadd(account + "_collect", String.valueOf(sid));
        }
        jedis.close();

        return result;
    }

    @Override
    public Integer unCollectSpot(String account, Integer sid) {
        if (account == null || sid == null || "".equals(account)) {
            return null;
        }

        // 查找收藏表中是否存在该用户和该sid
        CollectServiceImpl csi = new CollectServiceImpl();
        if (!csi.find(account, sid)) {
            // 不存在则返回null
            return null;
        }

        // 如果存在, 则写入数据库并返回结果
        Integer result = new CollectDaoImpl().deleteCollect(account, sid);

        Jedis jedis = JedisUtils.getJedis();
        // 如果redis中存在该数据则需要更新
        if (result != null && result == 1 && jedis.exists(account + "_collect")) {
            jedis.srem(account + "_collect", String.valueOf(sid));
        }
        jedis.close();

        return result;
    }
}

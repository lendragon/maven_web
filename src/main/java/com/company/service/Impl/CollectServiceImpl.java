package com.company.service.Impl;

import com.company.dao.Impl.CollectDaoImpl;
import com.company.service.CollectService;
import com.company.util.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectServiceImpl implements CollectService {
    private CollectDaoImpl cd = new CollectDaoImpl();

    @Override
    public boolean find(String account, Integer sid) {
        if (account == null || "".equals(account) || sid == null) {
            return false;
        }
        Jedis jedis = JedisUtils.getJedis();

        // 如果redis中存在该用户的收藏且
        if (jedis.exists(account)) {
            // 收藏中没有该景点, 则返回null
            if (!jedis.sismember(account + "_collect", String.valueOf(sid))) {
                jedis.close();
                return false;
            }
            // 收藏中有该景点, 则返回true
            jedis.close();
            return true;
        }

        // redis中不存在该用户的收藏, 则从数据库中查找, 并存入redis中
        Map<String, Object> map = null;
        try {
            map = cd.find(account, sid);
        } catch (Exception e) {}

        if (map != null) {
            jedis.sadd(account, (String) map.get(sid));
        }

        jedis.close();
        return map != null;
    }

    @Override
    public Set<String> findByAccount(String account) {
        if (account == null || "".equals(account)) {
            return null;
        }

        Jedis jedis = JedisUtils.getJedis();
        String keyStr = account + "_collect";
        Set<String> set = null;

        // 查看redis中是否存放
        if (jedis.exists(keyStr)) {
            set = jedis.smembers(keyStr);
        } else {
            // redis中没有数据, 从数据库中查找并存入redis中
            try {
                Set<String> setTmp = new HashSet<>();
                List<Map<String, Object>> list = cd.findByAccount(account);
                for (Map<String, Object> map : list) {
                    String mapStr = String.valueOf(map.get("sid"));
                    setTmp.add(mapStr);
                    jedis.sadd(keyStr, mapStr);
                }
                set = setTmp;
            } catch (Exception e) {}
        }

        jedis.close();
        return set;
    }


}

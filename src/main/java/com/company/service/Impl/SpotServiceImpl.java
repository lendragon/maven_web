package com.company.service.Impl;

import com.company.dao.Impl.SpotDaoImpl;
import com.company.domain.Spot;
import com.company.service.SpotService;
import com.company.util.JedisUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

import java.io.IOException;


public class SpotServiceImpl implements SpotService {
    private SpotDaoImpl sdi = new SpotDaoImpl();

    public String findAll() {
        Jedis jedis = JedisUtils.getJedis();
        String all = null;
        // 判断redis中是否存在数据
        if (jedis.exists("all")) {
            // 如果redis中存在, 则直接返回
            all = jedis.get("all");
        } else {
            // 不存在则从数据库中查找并以json格式存在jedis中
            try {
                all = new ObjectMapper().writeValueAsString(sdi.findAll());
                jedis.set("all", all);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        jedis.close();
        return all;
    }


    @Override
    public String findByPage(String page, int onePageRecord) {
        Jedis jedis = JedisUtils.getJedis();
        String p = null;
        // 判断redis中是否存在数据
        if (jedis.exists("page" + page)) {
            // 如果redis中存在, 则直接返回
            p = jedis.get("page" + page);
        } else {
            // 不存在则从数据库中查找并以json格式存在jedis中
            try {
                p = new ObjectMapper().writeValueAsString(sdi.findByPage(page, onePageRecord));
                jedis.set("page" + page, p);
            } catch (JsonProcessingException e) {}
        }

        jedis.close();
        return p;
    }

    @Override
    public int findAllRecord() {
        Jedis jedis = JedisUtils.getJedis();
        int allRecord = 0;
        // 判断redis中是否存在数据
        if (jedis.exists("allRecord")) {
            // 如果redis中存在, 则直接返回
            allRecord = Integer.parseInt(jedis.get("allRecord"));
        } else {
            // 不存在则从数据库中查找并以json格式存在jedis中
            allRecord = sdi.findByAllRecord();
            jedis.set("allRecord", String.valueOf(allRecord));
        }

        jedis.close();
        return allRecord;
    }


    public Spot findBySid(Integer sid) {
        if (sid == null) {
            return null;
        }

        Jedis jedis = JedisUtils.getJedis();
        Spot spot = null;

        // 如果redis中存在, 则直接返回
        if (jedis.exists("spot" + sid)) {
            try {
                spot = new ObjectMapper().readValue(jedis.get("spot" + sid), Spot.class);
            } catch (IOException e) {}
        } else {
            // redis中不存在, 则从数据库中查找并存入redis中
            try {
                spot = sdi.findBySid(sid);
                String s = new ObjectMapper().writeValueAsString(spot);
                jedis.set("spot" + sid, s);
            } catch (Exception e) {}
        }

        jedis.close();
        return spot;
    }
}

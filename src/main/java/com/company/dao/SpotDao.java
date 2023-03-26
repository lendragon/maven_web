package com.company.dao;

import com.company.domain.Spot;

import java.util.List;
import java.util.Map;

public interface SpotDao {
    int insert(String name, String position);

    Spot findBySid(Integer sid);

    List<Map<String, Object>> findAll();

    List<Map<String, Object>> find(StringBuffer[] condition);

    int delete(StringBuffer[] condition);

    int deleteBySid(int sid);

    List<Map<String, Object>> findByPage(String page, int onePageRecord);

    int findByAllRecord();
}

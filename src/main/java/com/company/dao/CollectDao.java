package com.company.dao;

import java.util.List;
import java.util.Map;

public interface CollectDao {
    int insertCollect(String account, Integer sid);

    Map<String, Object> find(String account, Integer sid);

    List<Map<String, Object>> findByAccount(String account);
}

package com.company.dao.Impl;

import com.company.dao.CollectDao;
import com.company.util.JDBCUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class CollectDaoImpl implements CollectDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDs());

    @Override
    public int insertCollect(String account, Integer sid) {

        String sql = "INSERT INTO collect VALUES(?, ?)";

        return template.update(sql, account, sid);
    }

    public int deleteCollect(String account, Integer sid) {
        String sql = "DELETE FROM collect WHERE account = ? AND sid = ?";

        return template.update(sql, account, sid);
    }

    @Override
    public Map<String, Object> find(String account, Integer sid) {

        String sql = "SELECT * FROM collect WHERE account = ? AND sid = ?";

        Map<String, Object> map = null;
        try {
            map = template.queryForMap(sql, account, sid);
        }catch (Exception e) {}

        return map;
    }


    @Override
    public List<Map<String, Object>> findByAccount(String account) {
        String sql = "SELECT * FROM collect WHERE account = ?";

        return template.queryForList(sql, account);
    }

}

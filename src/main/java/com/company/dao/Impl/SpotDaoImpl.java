package com.company.dao.Impl;

import com.company.dao.SpotDao;
import com.company.domain.Spot;
import com.company.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;

public class SpotDaoImpl implements SpotDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDs());

    @Override
    public int insert(String name, String position) {
        String sql = "INSERT INTO spot(name, position) VALUES(?, ?)";

        return template.update(sql, name, position);
    }


    @Override
    public Spot findBySid(Integer sid) {
        String sql = "SELECT * FROM spot WHERE sid = ? ";
        Spot spot = null;
        try {
            spot = template.queryForObject(sql, new BeanPropertyRowMapper<>(Spot.class), sid);
        } catch (Exception e) {}

        return spot;
    }

    @Override
    public List<Map<String, Object>> findAll() {
        String sql = "SELECT * FROM spot";
        return template.queryForList(sql);
    }


    @Override
    public List<Map<String, Object>> find(StringBuffer[] condition) {
        StringBuffer sql = new StringBuffer("SELECT* FROM spot WHERE a = a ");
        for (StringBuffer sb : condition) {
            sql.append(sb);
        }

        return template.queryForList(sql.toString());
    }


    @Override
    public int delete(StringBuffer[] condition) {
        StringBuffer sql = new StringBuffer("DELETE FROM spot WHERE a = a ");
        for (StringBuffer sb : condition) {
            sql.append(sb);
        }

        return template.update(sql.toString());
    }

    @Override
    public int deleteBySid(int sid) {
        String sql = "DELETE FROM spot WHERE sid = ? ";

        return template.update(sql, sid);
    }

    @Override
    public List<Map<String, Object>> findByPage(String page, int onePageRecord) {
        if (page == null || "".equals(page) || Integer.parseInt(page) < 1) {
            return null;
        }

        String sql = "SELECT * FROM spot LIMIT ?,?";

        return template.queryForList(sql, (Integer.parseInt(page) - 1) * onePageRecord , onePageRecord);
    }

    @Override
    public int findByAllRecord() {
        String sql = "SELECT COUNT(sid) AS count FROM spot";
        return Integer.parseInt(template.queryForMap(sql).get("count").toString());
    }
}

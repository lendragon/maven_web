package com.company.dao.Impl;


import com.company.dao.UserDao;
import com.company.domain.User;
import com.company.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDaoImpl implements UserDao {
    private static JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDs());

    /**
     * 查找所有用户信息
     * @return 返回结果
     */
    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user";

        return template.query(sql, new BeanPropertyRowMapper<User>());
    }

    /**
     * 更新用户信息
     * @param user 更新后的用户对象
     * @return 返回更新的个数
     */
    @Override
    public int updateAll(User user) {
        String sql = "UPDATE user SET " +
                "username = ?, " +
                "account = ?," +
                "password = ?," +
                "gender = ?," +
                "age = ?," +
                "email = ?," +
                "WHERE uid = ?";

        return template.update(sql, user.getUsername(), user.getAccount(),
                user.getPassword(), user.getAge(), user.getGender(),
                user.getEmail());
    }

    /**
     * 更新用户密码
     * @param password 用户更新的密码
     * @return 返回更新的个数
     */
    @Override
    public int updatePassword(String account, String password) {
        String sql = "UPDATE user SET password = ? WHERE account = ?";
        return template.update(sql, password, account);
    }

    /**
     * 插入一个新用户
     * @return 返回插入的个数
     */
    @Override
    public int insert(User user) {
        String sql = "INSERT INTO user(username, account, password, age, gender, email) VALUES(?, ?, ?, ?, ?, ?)";

        return template.update(sql, user.getUsername(), user.getAccount(),
                user.getPassword(), user.getAge(), user.getGender(),
                user.getEmail());
    }

    /**
     * 根据条件删除用户
     * @param condition 删除条件, 可自定义
     * @return 返回被删除的个数
     */
    @Override
    public int delete(StringBuffer[] condition) {
        StringBuffer sql = new StringBuffer("DELETE FROM user WHERE a = a AND ");
        for (int i = 0; i < condition.length; i++) {
            sql.append(condition[i]);
        }

        return template.update(sql.toString());
    }

    /**
     * 根据account删除用户
     * @param account 用户的账号
     * @return 返回被删除的个数
     */
    @Override
    public int deleteByAccount(String account) {
        String sql = "DELETE FROM user WHERE account = ? ";

        return template.update(sql);
    }

    /**
     * 通过用户名和密码查找用户
     * @param account 用户名
     * @param password 密码
     * @return 查找的结果
     */
    @Override
    public User findUser(String account, String password) {
        String sql = "SELECT * FROM user WHERE account = ? AND password = ?";
        User user = null;
        try {
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), account, password);
        } catch (Exception e) {

        }
        return user;
    }

    /**
     * 根据账号查找用户
     * @param account 账号
     * @return 返回结果集
     */
    @Override
    public User findByAccount(String account) {
        if ("".equals(account)) {
            return null;
        }

        String sql = "SELECT * FROM user WHERE account = ? ";
        User user = null;
        try {
            user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), String.valueOf(account));
        } catch (Exception e) {

        }

        return user;
    }

}
package com.company.dao.Impl;
;
import com.company.domain.User;
import com.company.util.JDBCUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UserDaoImplTest {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDs());
    private UserDaoImpl udi = new UserDaoImpl();

    /**
     * 查找所有用户信息
     * @return 返回结果
     */
    @Test
    public void findAll() {

    }

    /**
     * 更新用户信息
     * @return 返回更新的个数
     */
    @Test
    public void updateAll() {

    }

    /**
     * 更新用户密码
     * @return 返回更新的个数
     */
    @Test
    public void updatePassword() {

    }

    /**
     * 插入一个新用户
     * @return 返回结果
     */
    @Test
    public void insert() {

    }

    /**
     * 删除用户
     * @return 返回被删除的个数
     */
    @Test
    public void delete() {

    }

    @Test
    public void findByAccountTest() {
        String account = "";
        Assert.assertNull(udi.findByAccount(account));;

    }
}

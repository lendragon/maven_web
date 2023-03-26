package com.company.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.util.Properties;

public class JDBCUtils {    // 数据库连接工具类
    private static DataSource ds = null;
    static {
        Properties properties = new Properties();
        try {
            properties.load(new BufferedInputStream(JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties")));
            ds = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static DataSource getDs() {
        return ds;
    }

}

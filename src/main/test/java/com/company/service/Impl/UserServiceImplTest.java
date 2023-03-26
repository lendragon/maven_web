package com.company.service.Impl;

import com.company.domain.User;
import org.junit.Assert;
import org.junit.Test;

public class UserServiceImplTest {
    private UserServiceImpl usi = new UserServiceImpl();

    @Test
    public void findUserTest() {
        String account = "root";
        String password = "root";
        User user = usi.findUser(account, password);
        Assert.assertEquals(user, true);
    }

    @Test
    public void findByAccount() {
        User f = usi.findByAccount("");
    }
}

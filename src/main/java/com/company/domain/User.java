package com.company.domain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class User {
    private String username;
    private String account;
    private String password;
    private Integer age = null;
    private String gender = null;
    private String email = null;
    private Set collect = new HashSet();

    public Set getCollect() {
        return collect;
    }

    public void setCollect(Set collect) {
        this.collect = collect;
    }

    public User(String username, String account, String password, Integer age, String gender, String email, Set collect) {
        this.username = username;
        this.account = account;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.collect = collect;
    }

    public User() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

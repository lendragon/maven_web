package com.company.domain;

public class Spot { // 存储景点有关的信息
    private Integer sid;     // id
    private String name;    // 名字
    private String position;// 位置

    public Spot() {
    }

    public Spot(Integer sid, String name, String position) {
        this.sid = sid;
        this.name = name;
        this.position = position;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "sid=" + sid +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                '}';
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}

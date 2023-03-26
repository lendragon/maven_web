package com.company.domain;

public class ResultInfo {   // 从服务器返回前台的数据
    private boolean result;
    private String info;

    public ResultInfo() {
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "result=" + result +
                ", info='" + info + '\'' +
                '}';
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public ResultInfo(boolean result, String info) {
        this.result = result;
        this.info = info;
    }
}

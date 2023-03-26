package com.company.domain;

public class Page {
    private Integer currentPage;// 当前页面
    private Integer allRecord;  // 总共的记录
    private Integer onePageRecord = 9;  // 一页的记录


    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getAllRecord() {
        return allRecord;
    }

    public void setAllRecord(Integer allRecord) {
        this.allRecord = allRecord;
    }

    public Integer getOnePageRecord() {
        return onePageRecord;
    }

    public Page(Integer currentPage, Integer allRecord) {
        this.currentPage = currentPage;
        this.allRecord = allRecord;
    }

    public void setOnePageRecord(Integer onePageRecord) {
        this.onePageRecord = onePageRecord;
    }


    public Page() {
    }
}

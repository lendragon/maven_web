package com.company.service;

import java.util.Set;

public interface CollectService {
    boolean find(String account, Integer sid);

    Set<String> findByAccount(String account);
}

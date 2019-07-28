package com.service.impl;

import com.annotation.YLWService;
import com.service.MyService;

/**
 * Created by where
 * 2019/7/28 13:38
 */

@YLWService("MyServiceImpl")
public class MyServiceImpl implements MyService {
    @Override
    public String query(String name, String age) {
        return "name == " + name + "; age == " + age;
    }

    @Override
    public String insert(String param) {
        return "insert success ..." + param;
    }

    @Override
    public String update(String param) {
        return "update success ... " + param;
    }
}

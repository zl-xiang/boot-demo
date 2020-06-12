package com.shh.service;

import com.shh.persistence.dao.base.BaseIbatisDao;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImp implements IDemoService {
    private BaseIbatisDao baseDao;

    public DemoServiceImp(BaseIbatisDao baseDao) {
        this.baseDao = baseDao;
    }

    @Override
    public String hello(String str) {
        return "hello " + str;
    }
}

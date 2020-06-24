package com.shh.service;

import org.springframework.stereotype.Service;

@Service
public class DemoServiceImp implements IDemoService {
    private ICrudService _crudService;

    public DemoServiceImp(ICrudService crudService) {
        this._crudService = crudService;
    }

    @Override
    public String hello(String str) {

        return null;

    }
}

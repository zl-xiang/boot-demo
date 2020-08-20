package com.shh.service;

import com.shh.persistence.entity.DemoEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoServiceImp implements IDemoService {
    private ICrudService _crudService;

    public DemoServiceImp(ICrudService crudService) {
        this._crudService = crudService;
    }

    @Override
    public String hello(String str) {
        try {
            DemoEntity entity = new DemoEntity();
            entity.setId(str);
            List<DemoEntity> list = _crudService.findCon(DemoEntity.class, entity);
            if (list.size() > 0) {
                entity = list.get(0);
            }
            return entity.getStr();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

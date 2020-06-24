package com.shh.service;


import com.shh.common.utils.StringUtil;
import com.shh.persistence.dao.base.BaseIbatisDao;
import com.shh.persistence.dao.base.CrudWrapper;
import com.shh.persistence.entity.CrudEntity;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * 公用增删改查 service
 *
 * <p>
 * created by xiangzhiliang
 */
@Service
public class CrudService implements ICrudService {

    private BaseIbatisDao ibatisDao;

    public CrudService(BaseIbatisDao ibatisDao) {
        this.ibatisDao = ibatisDao;
    }

    @Override
    public <T> List<Map<String, Object>> find(Class<T> target, T obj) {
        return ibatisDao.find(CrudWrapper.readUnwrap(target, obj));
    }

    //TODO 返回String和Int主键的add区分
    @Override
    public <T> void add(Class<T> target, T obj) {
        CrudEntity entity = CrudWrapper.writeUnwrap(target, obj);
        if (entity.isAutoGenerate()) {
            ibatisDao.addAuto(entity);
        } else {
            ibatisDao.add(entity);
        }
    }

    @Override
    public <T> int updateOrDelete(Class<T> target, T obj) {
        CrudEntity entity = CrudWrapper.writeUnwrap(target, obj);
        return ibatisDao.update(entity);
    }

    @Override
    public <T> void delete(Class<T> target, T obj) {
        CrudEntity entity = CrudWrapper.writeUnwrap(target, obj);
        ibatisDao.deleteById(entity);
    }

//    @Override
//    public <T> PagedData<T> findByPage(Class<T> target, T obj, int currPage, int pageSize) throws Exception {
//        //开启分页查询
//        PageHelper.startPage(currPage, pageSize, true);
//        List<Map<String, Object>> dataList = this.find(target, obj);
//        //数据集转换
//        PageInfo info = new PageInfo<>(dataList);
//        List<T> realList = CrudWrapper.mapWrapper(target, dataList);
//        //设置返回集合
//        PagedData<T> data = new PagedData<>();
//        data.setCount(info.getTotal());
//        data.setPageCount((long) info.getPageNum());
//        data.setPageNo(currPage);
//        data.setPageSize(pageSize);
//        data.setList(realList);
//        return data;
//    }

    @Override
    public <T> List<T> findCon(Class<T> target, T obj) throws Exception {
        return CrudWrapper.mapWrapper(target, ibatisDao.find(CrudWrapper.readUnwrap(target, obj)));
    }

    @Override
    public <T> int addAutoInt(Class<T> target, T obj) {
        CrudEntity entity = CrudWrapper.writeUnwrap(target, obj);
        ibatisDao.addAuto(entity);
        int index;
        if (!StringUtil.isEmpty(entity.getPrimVal())) {
            BigInteger indexVal = (BigInteger) entity.getPrimVal();
            index = indexVal.intValue();
        } else {
            //自增长主键值找不到
            throw new IllegalArgumentException("Cannot find an auto generated value for primary key.");
        }
        return index;
    }


}

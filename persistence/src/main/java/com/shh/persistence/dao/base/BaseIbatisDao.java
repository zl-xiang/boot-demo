package com.shh.persistence.dao.base;

import com.shh.persistence.entity.CrudEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 基础Mybatis CRUD DAO
 *
 *
 * <p>
 * created by xiangzhiliang
 */
@Mapper
@Repository
public interface BaseIbatisDao {


    List<Map<String, Object>> find(CrudEntity entity);

    void add(CrudEntity entity);

    int addAuto(CrudEntity entity);

    int update(CrudEntity entity);

    int deleteById(CrudEntity entity);

}

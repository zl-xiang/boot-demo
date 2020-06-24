package com.shh.service;

import java.util.List;
import java.util.Map;

public interface ICrudService {
    <T> List<Map<String, Object>> find(Class<T> target, T obj);

    <T> void add(Class<T> target, T obj);

    <T> int updateOrDelete(Class<T> target, T obj);

    <T> void delete(Class<T> target, T obj);

    <T> List<T> findCon(Class<T> target, T obj) throws Exception;

    <T> int addAutoInt(Class<T> target, T obj);
}

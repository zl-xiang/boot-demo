package com.shh.persistence.dao.base;

import com.shh.common.utils.ReflectUtil;
import com.shh.common.utils.StringUtil;
import com.shh.persistence.annotation.Id;
import com.shh.persistence.annotation.Like;
import com.shh.persistence.entity.CrudEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 基本操作封装类
 * <p>
 * created by xiangzhiliang
 */
public class CrudWrapper {

    private static Logger log = LoggerFactory.getLogger(CrudWrapper.class);
    private static int QUERY = 0;
    private static int WRITE = 1;


    /**
     * 将目标对象解包封装成查询数据操作实体
     * @param target    目标类
     * @param obj       目标对象
     * @param <T>       目标类泛型
     * @return 解包封装数据实体
     */
    public static <T> CrudEntity readUnwrap(Class<T> target, T obj) {
        return objUnwrapped(target, obj, QUERY);
    }


    /**
     * 将目标对象解包封装成写入数据操作实体
     * @param target    目标类
     * @param obj       目标对象
     * @param <T>       目标类泛型
     * @return 解包封装数据实体
     */
    public static <T> CrudEntity writeUnwrap(Class<T> target, T obj) {
        return objUnwrapped(target, obj, WRITE);
    }


    /**
     * 将目标对象解包封装成数据操作实体
     *
     * @param target    目标类
     * @param obj       目标对象
     * @param operation 操作 0-查询,1-更新/插入
     * @param <T>       目标类泛型
     * @return 解包封装数据实体
     */
    private static <T> CrudEntity objUnwrapped(Class<T> target, T obj, int operation) {
        if(obj ==null){
            throw new IllegalArgumentException("target object cannot be null");
        }
        CrudEntity entity = new CrudEntity();
        String tableName = getTableName(target);
        //目标类
        entity.setClazz(target);
        //表名
        entity.setTableName(tableName);
        //参数键值对
        Map<String, Object> paraMap = new HashMap<>();
        //模糊参数键值对
        Map<String, Object> likeMap = new HashMap<>();

        List<String> colNames = new ArrayList<>();

        Field[] fields = target.getDeclaredFields();
        boolean idFlag = false;
        if (operation == 0) {
            boolean orderFlag = false;
            for (Field f : fields) {
                f.setAccessible(true);
                String fieldName = f.getName();
                Column col = f.getAnnotation(Column.class);
                Id id = f.getAnnotation(Id.class);
                OrderBy order = f.getAnnotation(OrderBy.class);
                Like like = f.getAnnotation(Like.class);
                if (col != null) {
                    //保存列名
                    colNames.add(col.name());
                    //获取值
                    Object val = ReflectUtil.getValueByKey(obj, fieldName, target);
                    //封装参数Map
                    if (!StringUtil.isEmpty(val) && like == null) {
                        paraMap.put(col.name(), val);
                    } else if (like != null && !StringUtil.isEmpty(val)) {
                        likeMap.put(col.name(), val);
                    }
                    idFlag = setProp(entity, idFlag, f, col, id, val);
                    if (order != null && !orderFlag) {
                        //保存排序字段
                        entity.setArrangeKey(col.name());
                        String orderStr;
                        if (StringUtil.isEmpty(order.value())) {
                            orderStr = "desc";

                        } else {
                            orderStr = order.value();
                        }
                        entity.setOrder(orderStr);
                        orderFlag = true;
                    }
                }
            }
            entity.setParaMap(paraMap);
            entity.setLikePairs(likeMap);
            entity.setColNames(colNames);
        } else {
            for (Field f : fields) {
                f.setAccessible(true);
                String fieldName = f.getName();
                Column col = f.getAnnotation(Column.class);
                Id id = f.getAnnotation(Id.class);
                if (col != null) {
                    //保存列名
                    colNames.add(col.name());
                    //获取值
                    Object val = ReflectUtil.getValueByKey(obj, fieldName, target);
                    //封装参数Map
                    if (!StringUtil.isEmpty(val)) {
                        paraMap.put(col.name(), val);
                    }
                    idFlag = setProp(entity, idFlag, f, col, id, val);
                }
            }
            entity.setParaMap(paraMap);
            entity.setColNames(colNames);
        }
        return entity;
    }


    private static boolean setProp(CrudEntity entity, boolean idFlag, Field f, Column col, Id id, Object val) {
        if (id != null && !idFlag) {
            //保存主键 相关属性
            entity.setPrimKey(col.name());
            entity.setPrimVal(val);
            entity.setAutoGenerate(id.autoGenerate());
            entity.setKeyType(id.keyType());
            entity.setPrimPropName(f.getName());
            idFlag = true;
        }
        return idFlag;
    }

    /**
     * Map包装为实体对象
     *
     * @param target  目标类
     * @param paraMap 属性键值对
     * @param <T>     目标类泛型
     * @return 实体对象
     */
    public static <T> T mapWrapper(Class<T> target, Map<String, Object> paraMap) throws Exception {
        T obj = target.newInstance();
        //获取Fields
        Field[] fields = target.getDeclaredFields();
        //遍历fields
        for (Field f : fields) {
            f.setAccessible(true);
            Column col = f.getAnnotation(Column.class);
            if (col != null) {
                //取对应map值
                Object val = paraMap.get(col.name());
                //设置到对象
                if (val != null) {
                    ReflectUtil.setFieldVal(obj, f, val);
                }
            }
        }
        return obj;
    }


    /**
     * Map集合包装为实体对象集合
     *
     * @param target      目标类
     * @param paraMapList 属性键值对
     * @param <T>         目标类泛型
     * @return 实体对象
     */
    public static <T> List<T> mapWrapper(Class<T> target, List<Map<String, Object>> paraMapList) throws Exception {
        List<T> tArray = new ArrayList<>();
        for (Map<String, Object> obj :
                paraMapList) {
            T tObj = CrudWrapper.mapWrapper(target, obj);
            tArray.add(tObj);
        }
        return tArray;
    }

    /**
     * 获取表名
     *
     * @param target 目标类
     * @param <T>    目标类泛型
     * @return 表名
     * @throws NoSuchElementException 无@table注解
     */
    private static <T> String getTableName(Class<T> target) {
        Table table = target.getAnnotation(Table.class);
        if (StringUtil.isEmpty(table)) {
            log.error("can not find a @Table annotation.");
            throw new NoSuchElementException("can not find a @Table annotation through the target class.");
        }
        return table.name();
    }


    //    public static <T> CrudEntity writeUnwrapped(Class<T> target, T obj) {
//        CrudEntity entity = new CrudEntity();
//        String tableName = getTableName(target);
//        //目标类
//        entity.setClazz(target);
//        //表名
//        entity.setTableName(tableName);
//        //参数键值对
//        Map<String, Object> paraMap = new HashMap<>();
//
//        List<String> colNames = new ArrayList<>();
//
//        Field[] fields = target.getDeclaredFields();
//        boolean idFlag = false;
//        for (Field f : fields) {
//            f.setAccessible(true);
//            String fieldName = f.getName();
//            Column col = f.getAnnotation(Column.class);
//            Id id = f.getAnnotation(Id.class);
//            if (col != null) {
//                //保存列名
//                colNames.add(col.name());
//                //获取值
//                Object val = null;
//                if (obj != null)
//                    val = ReflectUtil.getValueByKey(obj, fieldName, target);
//                //封装参数Map
//                if (!StringUtil.isEmpty(val)) {
//                    paraMap.put(col.name(), val);
//                }
//                idFlag = setProp(entity, idFlag, f, col, id, val);
//            }
//        }
//        entity.setParaMap(paraMap);
//        entity.setColNames(colNames);
//        return entity;
//    }

}

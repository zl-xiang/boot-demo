package com.shh.persistence.entity;

import java.util.List;
import java.util.Map;

/**
 * Ibatis查询封装类
 */
public class CrudEntity {


    /**
     * 表名
     */
    private String tableName;
    /**
     * 主键名
     */
    private String primKey;
    /**
     * 主键值
     */
    private Object primVal;
    /**
     * 是否自增长
     */
    private boolean autoGenerate;
    /**
     * 自增长类型
     */
    private int keyType;
    /**
     *
     */
    private String primPropName;
    /**
     * 列名
     */
    private List<String> colNames;

    private String arrangeKey;

    private String order;


    /**
     * 目标类型
     */
    private Class clazz;
    /**
     * where条件或插入参数map封装
     */
    private Map<String, Object> paraMap;

    private Map<String, Object> likePairs;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimKey() {
        return primKey;
    }

    public void setPrimKey(String primKey) {
        this.primKey = primKey;
    }

    public List<String> getColNames() {
        return colNames;
    }

    public void setColNames(List<String> colNames) {
        this.colNames = colNames;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Object getPrimVal() {
        return primVal;
    }

    public void setPrimVal(Object primVal) {
        this.primVal = primVal;
    }

    public Map<String, Object> getParaMap() {
        return paraMap;
    }

    public void setParaMap(Map<String, Object> paraMap) {
        this.paraMap = paraMap;
    }

    public String getArrangeKey() {
        return arrangeKey;
    }

    public void setArrangeKey(String arrangeKey) {
        this.arrangeKey = arrangeKey;
    }

    public Map<String, Object> getLikePairs() {
        return likePairs;
    }

    public void setLikePairs(Map<String, Object> likePairs) {
        this.likePairs = likePairs;
    }

    public boolean isAutoGenerate() {
        return autoGenerate;
    }

    public void setAutoGenerate(boolean autoGenerate) {
        this.autoGenerate = autoGenerate;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }


    public String getPrimPropName() {
        return primPropName;
    }

    public void setPrimPropName(String primPropName) {
        this.primPropName = primPropName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "CrudEntity{" +
                "tableName='" + tableName + '\'' +
                ", primKey='" + primKey + '\'' +
                ", primVal=" + primVal +
                ", autoGenerate=" + autoGenerate +
                ", keyType=" + keyType +
                ", colNames=" + colNames +
                ", arrangeKey='" + arrangeKey + '\'' +
                ", clazz=" + clazz +
                ", paraMap=" + paraMap +
                ", likePairs=" + likePairs +
                '}';
    }
}

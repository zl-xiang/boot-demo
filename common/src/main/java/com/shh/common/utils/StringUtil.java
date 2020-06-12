package com.shh.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * String工具类
 * <p>
 * created by xiangzhiliang
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils {
    /**
     * 数组是否包含指定的元素
     *
     * @param fields ,名称
     * @param name   ，元素
     * @return boolean, yes or no
     */
    public static boolean contains(String[] fields, String name) {
        if (getIndex(fields, name) == -1) {
            return false;
        }
        return true;
    }

    /**
     * 在数组中查找指定的串，如果找到，则返回索引，否则返回-1
     *
     * @param name ，索引名称
     * @return int, 索引号 不成功返回 -1
     */
    public static int getIndex(String[] fields, String name) {
        if ((fields == null) || (name == null)) {
            return -1;
        }
        for (int i = 0; i < fields.length; i++) {
            if (name.equalsIgnoreCase(fields[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 将普通文本字符串过滤成含 < br>
     * 的超文本字串
     *
     * @param src , 输入源串
     * @return String, 返回过滤结果
     */
    public static String strFilter(String src) {
        String ret = src.replaceAll("<", "&lt;");
        ret = ret.replaceAll(">", "&gt;");
        ret = ret.replaceAll("<", "&lt;");
        ret = ret.replaceAll("\r\n", "<br>");
        return ret;
    }

    /**
     * 将DATE转换为String类型 format "yyyy-MM-dd"
     *
     * @param date
     * @return
     */
    public static String dateToString(java.sql.Date date) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String s = "";
        if (date != null) {
            s = format.format(date);
        }
        return s;
    }

    /**
     * 判断对象是否为空<br>
     * 1,字符串(null或者"")都返回true<br>
     * 2,数字类型(null或者0)都返回true<br>
     * 3,集合类型(null或者不包含元素都返回true)<br>
     * 4,数组类型不包含元素返回true(包含null元素返回false)<br>
     * 5,其他对象仅null返回true
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Number) {
            Number num = (Number) obj;
            if (num.intValue() == 0) {
                return true;
            } else {
                return false;
            }
        } else if (obj instanceof String) {
            String str = (String) obj;
            if ((str == null) || str.equals("")) {
                return true;
            } else {
                return false;
            }
        } else if (obj instanceof Collection<?>) {
            Collection<?> c = (Collection<?>) obj;
            return c.isEmpty();
        } else if (obj instanceof Map<?, ?>) {
            Map<?, ?> m = (Map<?, ?>) obj;
            return m.isEmpty();
        } else if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            return length == 0;
        } else {
            return false;
        }
    }

    /**
     * 比较两个字符串是否相等
     */
    public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * clob 对象转为字符串
     *
     * @param data clob对象
     * @return 转换后的字符串
     */
    public static String clob2String(Clob data) throws SQLException, IOException {
        Reader reader = data.getCharacterStream();
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line = bufferedReader.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            sb.append(line);
            line = bufferedReader.readLine();
        }
        return sb.toString();

    }
}
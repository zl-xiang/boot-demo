package com.shh.common.utils;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 反射工具类
 * <p>
 * create by xiangzhiliang
 */
public class ReflectUtil {
    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";


    private static Logger log = LoggerFactory.getLogger(ReflectUtil.class);

    /**
     * @param obj   待转换对象
     * @param clazz 待转换对象所属类型
     * @param <T>   对象泛型
     */
    public static <T> Map<String, Object> getObjectAsMap(Object obj, Class<T> clazz) {
        Map<String, Object> map = new HashMap<>();
        //获取对象属性集合
        Field[] fields = clazz.getDeclaredFields();
        for (Field field :
                fields) {
            field.setAccessible(true);
            try {
                //获取属性值
                Object val = field.get(obj);
                map.put(field.getName(), val);
            } catch (IllegalMonitorStateException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * 单个对象的某个键的值
     *
     * @param obj 对象
     * @param key 键
     * @param <T> 对象泛型
     * @return Object 键在对象中所对应得值 没有查到时返回空字符串
     */
    public static <T> Object getValueByKey(Object obj, String key, Class<T> clazz) {
        // 得到类对象
        Field[] fields = clazz.getDeclaredFields();
        for (Field field :
                fields) {
            field.setAccessible(true); // 设置些属性是可以访问的
            try {
                if (field.getName().endsWith(key)) {
                    return field.get(obj);
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 没有查到时返回空字符串
        return "";
    }

    /**
     * @param obj
     * @param field
     * @param val
     * @param <T>
     * @throws IllegalAccessException
     */
    public static <T> void setFieldVal(T obj, Field field, Object val) throws IllegalAccessException {
        if (!StringUtil.isEmpty(val)) {
            field.setAccessible(true);
            // 如果类型是String
            if (field.getGenericType().toString().equals(
                    "class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                log.debug("==> type of String");

                field.set(obj, val);
            } else if (field.getGenericType().toString().equals(
                    "short")) {
                log.debug("==> type of short");
                Short intVal = (Short) field.getType().cast(val);
                field.setInt(obj, intVal);
            } else if (field.getGenericType().toString().equals(
                    "int")) {
                log.debug("==> type of int");
                int castVal;
                if (val instanceof String) {
                    String strVal = (String) val;
                    castVal = Integer.parseInt(strVal);
                } else {
                    castVal = (Integer) val;
                }

                field.setInt(obj, castVal);
            }

            // 如果类型是Double
            else if (field.getGenericType().toString().equals(
                    "double")) {
                log.debug("==> type of double");
                double castVal;
                if (val instanceof String) {
                    String strVal = (String) val;
                    castVal = Double.parseDouble(strVal);
                } else {
                    castVal = (Double) val;
                }
                field.setDouble(obj, castVal);
            } else if (field.getGenericType().toString().equals(
                    "boolean")) {
                log.debug("==> type of boolean");
                boolean castVal;
                if (val instanceof String) {
                    String strVal = (String) val;
                    castVal = Boolean.parseBoolean(strVal);
                } else {
                    castVal = (Boolean) val;
                }
                field.setBoolean(obj, castVal);
            }
            // 如果类型是Integer
            else if (field.getGenericType().toString().equals(
                    "class java.lang.Integer")) {
                log.debug("==> type of int");
                Integer intVal = (Integer) field.getType().cast(val);
                field.setInt(obj, intVal);
            }

            // 如果类型是Double
            else if (field.getGenericType().toString().equals(
                    "class java.lang.Double")) {
                log.debug("==> type of double");
                Double intVal = (Double) field.getType().cast(val);
                field.setDouble(obj, intVal);
            }

            // 如果类型是Boolean 是封装类
            else if (field.getGenericType().toString().equals(
                    "class java.lang.Boolean")) {
                log.debug("==> type of boolean");
                Boolean intVal = (Boolean) field.getType().cast(val);
                field.setBoolean(obj, intVal);
            }
/*
        // 如果类型是boolean 基本数据类型不一样 这里有点说名如果定义名是 isXXX的 那就全都是isXXX的
        // 反射找不到getter的具体名
        if (field.getGenericType().toString().equals("boolean")) {
            log.debug("==> type of boolean");
            Boolean intVal = (Boolean) field.getType().cast(val);
            field.setBoolean(obj, intVal);
        }*/
            // 如果类型是Short
            else if (field.getGenericType().toString().equals(
                    "class java.lang.Short")) {
                log.debug("==> type of boolean");
                Short intVal = (Short) field.getType().cast(val);
                field.setShort(obj, intVal);
            }
            // 如果还需要其他的类型请自己做扩展
            else {
                log.debug("==> type of other class");
                field.set(obj, val);
            }
        }
    }

    /**
     * 调用Getter方法. 支持多级，如：对象名.对象名.方法
     */
    public static Object invokeGetter(Object obj, String propertyName) {
        Object object = obj;
        String[] fieldArray = StringUtil.split(propertyName, ".");
        for (String name : fieldArray) {
            String getterMethodName = GETTER_PREFIX + StringUtil.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[]{}, new Object[]{});
        }
        return object;
    }

    /**
     * 调用Setter方法, 仅匹配方法名。 支持多级，如：对象名.对象名.方法
     */
    public static void invokeSetter(Object obj, String propertyName, Object value) {
        Object object = obj;
        String[] names = StringUtil.split(propertyName, ".");
        for (int i = 0; i < names.length; i++) {
            if (i < names.length - 1) {
                String getterMethodName = GETTER_PREFIX + StringUtil.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[]{}, new Object[]{});
            } else {
                String setterMethodName = SETTER_PREFIX + StringUtil.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[]{value});
            }
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符.
     * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用. 同时匹配方法名+参数类型，
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     * 匹配函数名+参数类型。
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
     * args)
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符，
     * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
     * 只匹配函数名，如果有多个同名函数调用第一个。
     */
    public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
        Method method = getAccessibleMethodByName(obj, methodName);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null. 只匹配函数名。
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object...
     * args)
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(methodName, "methodName can't be blank");

        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }


}

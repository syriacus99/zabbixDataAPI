package com.cqcnt.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class BeanCopyUtil {
    /**
            * 封装了一个copy单个对象属性的方法
     * @param source
     * @param clazz
     * @param <T>
     * @return
             */
    public static <T>T copyOne(Object source,Class<T> clazz){
        T t = null;
        try {
            t = clazz.newInstance();
            BeanUtils.copyProperties(source,t);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return t;
    }


    /**
     * 封装的一个复制list集合的方法
     * @param source
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(List source, Class<T> clazz){
        ArrayList list = new ArrayList();
        try {
            for (Object obj:source) {
                T t = clazz.newInstance();
                BeanUtils.copyProperties(obj,t);
                list.add(t);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return list;
    }
}

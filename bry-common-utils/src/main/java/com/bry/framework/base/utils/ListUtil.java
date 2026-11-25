//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.bry.framework.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListUtil {
    private static final Logger logger = LoggerFactory.getLogger(ListUtil.class);

    private ListUtil() {
    }

    public static <T> List<List<T>> chopped(List<T> list, int splitLength) {
        List<List<T>> parts = new ArrayList();
        int size = list.size();
        for (int i = 0; i < size; i += splitLength) {
            parts.add(new ArrayList(list.subList(i, Math.min(size, i + splitLength))));
        }

        return parts;
    }

    public static List<Integer> stringToIntegerList(String strArr) {
        return (List) Arrays.stream(strArr.split(",")).map(s ->
                Integer.parseInt(s.trim())
        ).collect(Collectors.toList());
    }

    public static List<Long> stringToLongList(String strArr) {
        return (List) Arrays.stream(strArr.split(",")).map((s) -> {
            return Long.parseLong(s.trim());
        }).collect(Collectors.toList());
    }

    public static <T> Map<String, T> listToMap(String keyName, List<T> list) {
        HashMap m = new HashMap();
        try {
            Iterator<T> var3 = list.iterator();
            while (var3.hasNext()) {
                T t = var3.next();
                PropertyDescriptor pd = new PropertyDescriptor(keyName, t.getClass());
                Method getMethod = pd.getReadMethod();
                Object o = getMethod.invoke(t);
                m.put(o.toString(), t);
            }
            return m;
        } catch (Exception e) {
            logger.error("Convert List to Map failed", e);
            return null;
        }
    }

    public static <T> Map<String, T> listToMap(List<T> list) {
        return listToMap("id", list);
    }

    public static <T> boolean isNullOrEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }
}

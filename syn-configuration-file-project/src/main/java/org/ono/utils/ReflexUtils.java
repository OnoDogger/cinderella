package org.ono.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Created by ono on 2018/11/20.
 */
public class ReflexUtils {

    public static<T> T generateObjectByClazz(Class<?> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (null == clazz){
            return null;
        }
        Constructor<?>[] cs = clazz.getConstructors();
        for (Constructor<?> c: cs){
            Class<?>[] arr = c.getParameterTypes();
            if (0 == arr.length){
                if (Modifier.PRIVATE == c.getModifiers()){
                    c.setAccessible(true);
                }
                return (T)c.newInstance();
            }

        }
        return null;
    }

    public static<T> T generateObjectByStrClazz(String clazz, ClassLoader classLoader) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {

        if (null == classLoader){
            return (T) generateObjectByClazz(Thread.currentThread().getContextClassLoader().loadClass(clazz));
        }
        return (T)generateObjectByClazz(classLoader.loadClass(clazz));
    }

}

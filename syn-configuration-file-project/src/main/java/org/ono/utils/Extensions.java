package org.ono.utils;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ono on 2018/11/20.
 */
public class Extensions {


    private static ConcurrentMap<Class<?>,Factory<?>> fc = new ConcurrentHashMap<>();

    public static <T>T getFactory(Class<T> clazz, String name){
        Factory<?> f = fc.get(clazz);
        if (null == f){
            fc.putIfAbsent(clazz, new Factory<T>());
            f = fc.get(clazz);
            loadLocal(clazz);
        }
        return (T) f.getForName(name);

    }

    private static void loadLocal(Class<?> clazz){
        try {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("META-INF/services/" + clazz.getName());
            Factory<?> f = fc.get(clazz);
            while(urls.hasMoreElements()){
                InputStream is = null;
                BufferedReader reader = null;
                try {
                   URL url = urls.nextElement();
                   is = url.openStream();
                   reader = new BufferedReader(new InputStreamReader(is));
                   String line;
                   while ((line = reader.readLine()) != null){
                       line = line.trim();
                       if (null == line || line.equals("")){
                           continue;
                       }
                       int pos = line.indexOf("=");
                       if (-1 == pos) continue;
                       String name = line.substring(0, pos);
                       String strClazz = line.substring(pos + 1);
                       f.addStrClazz(name, strClazz);
                   }
                }catch (Throwable e){

                }finally {
                    if (null != is){
                        is.close();
                    }
                    if (null != reader){
                        reader.close();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class Factory<T>{
        private ConcurrentMap<String,String> name4Clazz = new ConcurrentHashMap<String,String>();
        private ConcurrentMap<String,Object> name4Object = new ConcurrentHashMap<String,Object>();

        public void addStrClazz(String name,String strClazz){
            if (StringUtils.isBlank(name) || StringUtils.isBlank(strClazz)) return;
            name4Clazz.put(name, strClazz);
        }

        public synchronized T getForName(String name){
            Object obj = name4Object.get(name);
            if (null == obj){
                String clzz = name4Clazz.get(name);
                if (StringUtils.isNotBlank(clzz)){
                    try {
                       obj = ReflexUtils.generateObjectByStrClazz(clzz,null);
                       name4Object.put(name, obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return (T)obj;
        }
    }
}

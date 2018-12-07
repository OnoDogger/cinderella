package org.ono.support.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;



/**
 * Created by ono on 2018/11/19.
 */
public class AppContext {

    private static Logger logger = LoggerFactory.getLogger(AppContext.class);
    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext context){
        AppContext.context = context;
    }

    public static ApplicationContext getContext(){
        if (context == null){
            throw new IllegalStateException("applicationContext未注入,请在applicationContext.xml中定义AppContext");
        }
        return context;
    }
    public static<T> T getBean(String name){
        if (context == null){
            throw new IllegalStateException("applicationContext未注入,请在applicationContext.xml中定义AppContext");
        }
        try{
            return (T) context.getBean(name);
        }catch (Exception e){
            logger.error("can't find bean name");
        }
        return null;
    }
}

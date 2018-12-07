package org.ono.utils;

/**
 * Created by ono on 2018/11/28.
 */
public class StringUtils extends org.apache.commons.lang.StringUtils{

    public static String toLowerCaseFirstChar(String source){
        if (source == null || source.length() == 0){
            return source;
        }
        String f = source.substring(0, 1).toLowerCase();
        source = f + source.substring(1);
        return source;
    }
}

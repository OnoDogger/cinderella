package org.ono.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ono on 2018/11/30.
 */
public class XmlUtils {

    public static Map<String,Object> xml2Map(String xml){
        Document doc = null;
        try{
            doc = DocumentHelper.parseText(xml);
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        if (doc == null){
            return map;
        }
        Element rootElement = doc.getRootElement();
        element2Map(rootElement, map);
        return map;
    }

    private static void element2Map(Element elmt, Map<String,Object> map){
        if (null == elmt){
            return;
        }

        String name = elmt.getName();
        if (elmt.isTextOnly()){
            map.put(name, elmt.getText());
        }else {
            Map<String, Object> mapSub = new HashMap<>();
            List<Element> elements = elmt.elements();
            for (Element elmtSub: elements){
                element2Map(elmtSub, mapSub);
            }
            Object first = map.get(name);
            if (null == first){
                map.put(name, mapSub);
            }else {
                if (first instanceof List<?>){
                    ((List) first).add(mapSub);
                }else {
                    List<Object> listSub = new ArrayList<>();
                    listSub.add(first);
                    listSub.add(mapSub);
                    map.put(name, listSub);
                }
            }

        }
    }
}

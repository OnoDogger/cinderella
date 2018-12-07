package org.ono.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ono on 2018/11/23.
 */
public class Constants {

    public static final String ENCODING = "UTF-8";

    public static final String PROTOCOL_XML = "xml";
    public static final String PROTOCOL_PROPERTIES = "properties";
    public static final String PROTOCOL_YML="yml";
    public static final String PROTOCOL_ALL="all";


    public static final List<String> unmodifiableTypesList = Collections.unmodifiableList(new ArrayList<String>(){{
        add(PROTOCOL_PROPERTIES);
        add(PROTOCOL_XML);
        add(PROTOCOL_YML);
    }});


    public static final String STORAGE_PG = "pg";

}

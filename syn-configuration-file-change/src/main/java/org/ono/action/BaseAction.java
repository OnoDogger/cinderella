package org.ono.action;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ono on 2018/12/5.
 */
public class BaseAction {
    static final Map<String, String> responseMap4Success = new HashMap<>();
    static final Map<String, String> responseMap4Failure = new HashMap<>();

    static {
        responseMap4Success.put("result", "0");
        responseMap4Success.put("message", "operating successd");
        responseMap4Failure.put("result", "-1");
        responseMap4Failure.put("message", "operating failed");
    }


    public static Map<String, String> getResponseMap4Success() {
        return responseMap4Success;
    }

    public static Map<String, String> getResponseMap4Failure() {
        return responseMap4Failure;
    }
}

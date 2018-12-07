package org.ono.action;

import org.ono.domain.ConfigObject;
import org.ono.services.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ono on 2018/12/4.
 */
@RestController
public class ConfigAction {

    @Autowired
    private IConfigService configServiceImpl;

    @RequestMapping(value = "/findConfigData")
    public List<ConfigObject> findConfigData(String hostnameOrIp){
        return configServiceImpl.findByHostname(hostnameOrIp);
    }

}

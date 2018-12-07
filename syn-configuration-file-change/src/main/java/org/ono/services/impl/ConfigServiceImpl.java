package org.ono.services.impl;

import org.ono.dao.pg.IConfigDao;
import org.ono.domain.ConfigObject;
import org.ono.services.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ono on 2018/12/4.
 */
@Service
public class ConfigServiceImpl implements IConfigService {

    @Autowired
    private IConfigDao configDao;

    @Override
    public List<ConfigObject> findByHostname(String hostnameOrIp) {
        return configDao.findByHostname(hostnameOrIp);
    }
}

package org.ono.services;

import org.ono.domain.ConfigObject;

import java.util.List;

/**
 * Created by ono on 2018/12/4.
 */
public interface IConfigService {
    public List<ConfigObject> findByHostname(String hostnameOrIp);
}

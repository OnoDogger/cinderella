package org.ono.dao.pg;

import org.apache.ibatis.annotations.Mapper;
import org.ono.domain.ConfigObject;

import java.util.List;

/**
 * Created by ono on 2018/12/4.
 */
@Mapper
public interface IConfigDao {

    public List<ConfigObject> findByHostname(String hostnameOrIp);
}

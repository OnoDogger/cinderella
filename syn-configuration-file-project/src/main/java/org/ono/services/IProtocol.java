package org.ono.services;

import java.sql.SQLException;

/**
 * Created by ono on 2018/11/21.
 */
public interface IProtocol {

    public void saveData(String value) throws SQLException;

}

package org.ono.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created by ono on 2018/11/29.
 */
public class DBUtils {

    public static Connection getConnection(String jdbcDriver, String jdbcUrl, String userName, String password){

        Connection conn = null;
        try {
            Class.forName(jdbcDriver).newInstance();
            conn = DriverManager.getConnection(jdbcUrl,userName,password);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeQuietly(AutoCloseable... closeables){

        if (closeables == null || closeables.length < 1){
            return;
        }
        for (AutoCloseable closeable: closeables){
            try {
                if (closeable != null){
                    closeable.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

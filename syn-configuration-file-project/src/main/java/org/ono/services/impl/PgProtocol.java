package org.ono.services.impl;

import org.ono.support.spring.AppContext;
import org.ono.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ono on 2018/11/21.
 */
public class PgProtocol extends AbstractProtocol {

    private static final String DRIVER = "org.postgresql.Driver";

    private static final String INSERT_DATA = "insert into configurations (config_id, hostname_or_ip,config_files_map,create_time,update_time) values (nextval('config_id_seq'),?,?,?,?)";
    private static final String UPDATE_DATA = "update configurations set config_files_map = ?, update_time = ? where hostname_or_ip = ?";
    private static final String QUERY_DATA_ALL = "select * from configurations";
    private static final String QUERY_DATA_BY_HOSTNAME = "select * from configurations where hostname_or_ip = ?";
    private static final String QUERY_COUNT_BY_HOSTNAME = "select count(1) from configurations where hostname_or_ip = ?";


    private Connection connect() {
       Storage storage = AppContext.getBean("storage");
       String ip = storage.getAddress();
       return DBUtils.getConnection(DRIVER, "jdbc:postgresql://"+ip+"/postgres", storage.getUser(),storage.getPassword());
    }

    public void saveData(String value) {

        Connection conn = null;
        try{
            conn = connect();
            if (countByHostname(conn) > 0){
                updateData(conn, value);
            }else {
                insertData(conn, value);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtils.closeQuietly(conn);
        }
    }
    private void updateData(Connection conn, String value){
        Storage storage = AppContext.getBean("storage");
        PreparedStatement pstat = null;
        try{
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(UPDATE_DATA);
            pstat.setString(1, value);
            pstat.setLong(2, System.currentTimeMillis());
            pstat.setString(3, storage.getHostname());
            pstat.executeUpdate();
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtils.closeQuietly(pstat);
        }
    }

    private void insertData(Connection conn, String value){
        Storage storage = AppContext.getBean("storage");
        PreparedStatement pstat = null;
        try{
            conn.setAutoCommit(false);
            pstat = conn.prepareStatement(INSERT_DATA);
            pstat.setString(1, storage.getHostname());
            pstat.setString(2, value);
            pstat.setLong(3, System.currentTimeMillis());
            pstat.setLong(4, System.currentTimeMillis());
            pstat.execute();
            conn.commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtils.closeQuietly(pstat);
        }
    }

    private Map<String,String> queryByHostname(Connection conn){
        Map<String,String> result = new HashMap<>();
        Storage storage = AppContext.getBean("storage");
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try{
            pstat = conn.prepareStatement(QUERY_DATA_BY_HOSTNAME);
            pstat.setString(1, storage.getHostname());
            rs = pstat.executeQuery();
            while (rs.next()){
                result.put(rs.getString(1), rs.getString(2));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtils.closeQuietly(rs, pstat);
        }
        return result;
    }

    private long countByHostname(Connection conn){
        long count = 0l;
        Storage storage = AppContext.getBean("storage");
        PreparedStatement pstat = null;
        ResultSet rs = null;
        try{
            pstat = conn.prepareStatement(QUERY_COUNT_BY_HOSTNAME);
            pstat.setString(1, storage.getHostname());
            rs = pstat.executeQuery();
            if (rs.next() && rs.getLong(1) > 0){
                count = rs.getLong(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBUtils.closeQuietly(rs, pstat);
        }
        return count;
    }
}

package org.ono.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by ono on 2018/11/9.
 */
public interface IContextType {

    public Map<String, List<Map<String, List<Map<String,String>>>>> fetchFileMap();

    public void setFilesMap(Map<String, List<Map<String, List<Map<String,String>>>>> filesMap);

    public String resolve(byte[] bytes, String encoding) throws UnsupportedEncodingException;

    public void initConfig(List<Path> paths,List<String> excludeFiles, String type) throws IOException, org.ono.exception.InvalidPathException, SQLException;

    public void storageConfigMap() throws SQLException;

    public void updateConfig(Path path, List<String> excludeFiles, String type) throws IOException, org.ono.exception.InvalidPathException, SQLException;


}

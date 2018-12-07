package org.ono.services.impl;

import com.alibaba.fastjson.JSON;
import org.ono.exception.InvalidPathException;
import org.ono.services.IContextType;
import org.ono.services.IProtocol;
import org.ono.support.spring.AppContext;
import org.ono.utils.Constants;
import org.ono.utils.Extensions;
import org.ono.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by ono on 2018/11/21.
 */
public abstract class AbstractContextType implements IContextType {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractContextType.class);


    // key1 path, key2 filename, key3 file type, key4 field
    private static Map<String, List<Map<String, List<Map<String,String>>>>> filesMap = new HashMap<>();

    @Override
    public Map<String, List<Map<String, List<Map<String,String>>>>> fetchFileMap() {
        return AbstractContextType.filesMap;
    }

    public  void setFilesMap(Map<String, List<Map<String, List<Map<String,String>>>>> filesMap) {
        AbstractContextType.filesMap = filesMap;
    }

    public Map<String, Object> convertProp2Map(Properties props) {

        if (null == props) return new HashMap<>();

        Map<String, Object> propMap = new HashMap<>();
        for (String key : props.stringPropertyNames()) {
            propMap.put(key, props.getProperty(key));
        }

        return propMap;
    }

    @Override
    public String resolve(byte[] bytes, String encoding) throws UnsupportedEncodingException {
        return new String(bytes, encoding);
    }

    @Override
    public void initConfig(List<Path> paths,List<String> excludeFiles, String type) throws IOException, InvalidPathException, SQLException {
        for (Path path: paths){
            final Map<String, List<Map<String,String>>> res = read(path, excludeFiles, type);
            if (res != null && res.size() >0){
                if (filesMap.containsKey(path.toString())){
                    filesMap.get(path.toString()).add(res);
                }else {
                    filesMap.put(path.toString(), new ArrayList<Map<String, List<Map<String,String>>>>(){{
                        add(res);
                    }});
                }
            }
        }
    }

    @Override
    public void updateConfig(Path path, List<String> excludeFiles, String type) throws IOException, InvalidPathException, SQLException {

        Map<String, List<Map<String,String>>> newMap = read(path, excludeFiles, type);
        List<Map<String, List<Map<String,String>>>> list = filesMap.get(path.toString());
        Set<String> newKeys = newMap.keySet();
        for (Map<String, List<Map<String,String>>> orgMap : list){
            for (String newKey: newKeys){
                if (orgMap.containsKey(newKey)){
                    orgMap.put(newKey, newMap.get(newKey));
                }
            }
        }
    }

    private Map<String, List<Map<String,String>>> read(Path path,final List<String> excludeFiles,final String type) throws InvalidPathException, IOException {
        if (!Files.exists(path)) {
            throw new InvalidPathException("the file is not exists.");
        }

        final Map<String, List<Map<String,String>>> result =  new HashMap<>();

        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                String fileType = FileUtils.findFileType(file);
                if (Constants.unmodifiableTypesList.contains(fileType)
                        && (CollectionUtils.isEmpty(excludeFiles) || !excludeFiles.contains(file.getFileName()))
                        && type.toLowerCase().equals(fileType)) {

                    final Map<String,String> map = new HashMap<>();
                    byte[] bytes = Files.readAllBytes(file);
                    String m = resolve(bytes, Constants.ENCODING);
                    map.put(file.getFileName().toString(), m);

                    if (result.containsKey(fileType)){
                        result.get(fileType).add(map);
                    }else {
                        result.put(fileType, new ArrayList<Map<String,String>>(){{
                            add(map);
                        }});
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return result;
    }

    public void storageConfigMap() throws SQLException {
        if (filesMap.size() > 0){
            String jsonMapStr = JSON.toJSONString(filesMap);
            LOG.info("local config json : {}", jsonMapStr);
            IProtocol protocol = Extensions.getFactory(IProtocol.class, ((Storage)AppContext.getBean("storage")).getType());
            protocol.saveData(jsonMapStr);
        }
    }
}

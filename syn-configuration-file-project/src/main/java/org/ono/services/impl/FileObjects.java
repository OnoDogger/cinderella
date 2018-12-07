package org.ono.services.impl;

import org.apache.commons.lang.StringUtils;
import org.ono.exception.InvalidPathException;
import org.ono.listener.DirectoryChangeListener;
import org.ono.services.IContextType;
import org.ono.support.spring.AppContext;
import org.ono.utils.Constants;
import org.ono.utils.Extensions;
import org.ono.utils.FileUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ono on 2018/11/20.
 */
public class FileObjects implements ApplicationListener<ContextRefreshedEvent>{

    private String id;
    private String locations;
    private String excludes;
    private String type;
    private List<Path> filePaths;
    private List<String> excludeFiles;

    public List<String> getExcludeFiles(){
        if (StringUtils.isBlank(excludes)){
            throw new IllegalArgumentException("exclude is required!");
        }
        String[] excludeArr = excludes.split(",");
        return  Arrays.asList(excludeArr);
    }

    public List<String> getFilePaths() {
        if (StringUtils.isBlank(locations)){
            throw new IllegalArgumentException("location is required!");
        }
        String[] pathArr = locations.split(",");
        return  Arrays.asList(pathArr);
    }

    public FileObjects() {
    }

    public FileObjects(String locations, String excludes, String type) {
        this.locations = locations;
        this.excludes = excludes;
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) throws InvalidPathException {
        if (StringUtils.isBlank(locations)){
            throw new InvalidPathException("locations must not be blank!");
        }
        filePaths = new ArrayList<>();
        filePaths.addAll(FileUtils.convertString2Path(Arrays.asList(locations.split(","))));
        this.locations = locations;
    }

    public String getExcludes() {
        return excludes;
    }

    public void setExcludes(String excludes) {
        if (StringUtils.isNotEmpty(excludes)){
            excludeFiles = new ArrayList<>();
            excludeFiles.addAll(Arrays.asList(excludes.split(",")));
            this.excludes = excludes;
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.trim().toLowerCase();
    }


    @Override
    public String toString() {
        return "FileObjects{" +
                "id='" + id + '\'' +
                ", locations='" + locations + '\'' +
                ", excludes='" + excludes + '\'' +
                ", type='" + type + '\'' +
                ", filePaths=" + filePaths +
                ", excludeFiles=" + excludeFiles +
                '}';
    }

    private void initData() throws InvalidPathException, SQLException, IOException {

        IContextType contextType =null;
        if (Constants.unmodifiableTypesList.contains(this.type)){
            contextType = Extensions.getFactory(IContextType.class, type);
            contextType.initConfig(filePaths, excludeFiles, type);
        }else if (Constants.PROTOCOL_ALL.equals(this.type)) {
            for (String t: Constants.unmodifiableTypesList){
                contextType = Extensions.getFactory(IContextType.class, t);
                contextType.initConfig(filePaths, excludeFiles, t);
            }
        }
        contextType.storageConfigMap();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        try {

            ApplicationContext ac = event.getApplicationContext();
            if (null != ac.getParent()) return;
            AppContext.setApplicationContext(ac);

            initData();
            new Thread(new DirectoryChangeListener(filePaths, excludeFiles,type)).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

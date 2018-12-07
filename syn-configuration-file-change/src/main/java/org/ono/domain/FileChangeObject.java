package org.ono.domain;

/**
 * Created by ono on 2018/12/4.
 */
public class FileChangeObject {

    private String path;
    private String fileType;
    private String fileName;
    private String hostName;
    private String context;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "FileChangeObject{" +
                "path='" + path + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", hostName='" + hostName + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}

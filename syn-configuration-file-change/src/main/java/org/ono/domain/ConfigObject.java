package org.ono.domain;

/**
 * Created by ono on 2018/12/4.
 */
public class ConfigObject {

    private long configId;
    private String hostnameOrIp;
    private String configFilesMap;
    private long createTime;
    private long updateTime;

    public long getConfigId() {
        return configId;
    }

    public void setConfigId(long configId) {
        this.configId = configId;
    }

    public String getHostnameOrIp() {
        return hostnameOrIp;
    }

    public void setHostnameOrIp(String hostnameOrIp) {
        this.hostnameOrIp = hostnameOrIp;
    }

    public String getConfigFilesMap() {
        return configFilesMap;
    }

    public void setConfigFilesMap(String configFilesMap) {
        this.configFilesMap = configFilesMap;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}

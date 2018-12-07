package org.ono.services.impl;

/**
 * Created by ono on 2018/11/20.
 */
public class Storage {

    private String type;
    private String address;
    private String user;
    private String password;
    private String hostname;

    public Storage() {
    }

    public Storage(String type, String address, String hostname) {
        this.type = type;
        this.address = address;
        this.hostname = hostname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", hostname='" + hostname + '\'' +
                '}';
    }
}

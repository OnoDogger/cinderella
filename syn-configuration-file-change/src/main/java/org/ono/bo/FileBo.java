package org.ono.bo;

import org.ono.domain.FileChangeObject;

import java.util.List;

/**
 * Created by ono on 2018/12/4.
 */
public class FileBo {

    private FileChangeObject params;

    public FileChangeObject getParams() {
        return params;
    }

    public void setParams(FileChangeObject params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "FileBo{" +
                "params=" + params +
                '}';
    }
}

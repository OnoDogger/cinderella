package org.ono.services;

import org.ono.bo.FileBo;

import java.io.IOException;

/**
 * Created by ono on 2018/12/3.
 */
public interface IFileOperator {

    public void reconstructureFile(FileBo fileBo) throws Exception;
}

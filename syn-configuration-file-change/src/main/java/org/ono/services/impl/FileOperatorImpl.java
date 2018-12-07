package org.ono.services.impl;

import org.ono.bo.FileBo;
import org.ono.domain.FileChangeObject;
import org.ono.services.IFileOperator;
import org.ono.utils.FileUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by ono on 2018/12/3.
 */
@Service
public class FileOperatorImpl implements IFileOperator {

    private final Object LOCK = new Object();
    private int count = 1;

    @Override
    public void reconstructureFile(FileBo fileBo) throws IOException {
        System.out.println("count = "+ (count++));
        FileChangeObject fobj = fileBo.getParams();
        synchronized (LOCK){
            if (FileUtils.delFile(fobj.getPath())){
                FileUtils.newFile(fobj.getPath(), fobj.getContext());
            }
        }
    }
}

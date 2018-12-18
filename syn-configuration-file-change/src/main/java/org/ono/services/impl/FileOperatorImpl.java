package org.ono.services.impl;

import com.alibaba.druid.util.StringUtils;
import org.ono.bo.FileBo;
import org.ono.domain.FileChangeObject;
import org.ono.services.IFileOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ono on 2018/12/3.
 */
@Service
public class FileOperatorImpl implements IFileOperator {

    public static final Logger LOG = LoggerFactory.getLogger(FileOperatorImpl.class);

    private final Object LOCK = new Object();

    @Override
    public void reconstructureFile(FileBo fileBo) throws Exception {

        FileChangeObject fobj = fileBo.getParams();
        synchronized (LOCK) {
            if (delFile(fobj.getPath())) {
                if (!newFile(fobj.getPath(), fobj.getContext())){
                    throw new Exception("reconstructure file failed!");
                }
            }
        }
    }

    private boolean delFile(String filePathAndName) {
        boolean res = false;
        String filePath = filePathAndName.trim();
        File myDelFile = new File(filePath);
        if (myDelFile.exists()){
            res = myDelFile.delete();
            LOG.info("delete file {} successed!", filePathAndName);
        }else {
            res = true;
            LOG.info("file {} isn't existe!", filePathAndName);
        }
        return res;
    }

    private boolean newFile(String filePathAndName, String fileContent) throws IOException {
        boolean flag = false;
        String filePath = filePathAndName.trim();
        File myFilePath = new File(filePath);
        if (!myFilePath.exists()) {
            if (StringUtils.isEmpty(fileContent)){
                flag = myFilePath.mkdir();
            }else {
                flag = myFilePath.createNewFile();
                if (flag){
                    FileWriter resultFile = new FileWriter(myFilePath);
                    PrintWriter myFile = new PrintWriter(resultFile);
                    String strContent = fileContent;
                    myFile.print(strContent);
                    myFile.close();
                    resultFile.close();
                    LOG.info("create file {} successed!", filePathAndName);
                }
            }
        }
        return flag;
    }
}

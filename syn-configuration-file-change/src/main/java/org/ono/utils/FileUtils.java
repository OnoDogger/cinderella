package org.ono.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by ono on 2018/12/5.
 */
public class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 新建文件
     */
    public static void newFile(String filePathAndName, String fileContent) throws IOException {
        String filePath = filePathAndName.trim();
        File myFilePath = new File(filePath);
        if (!myFilePath.exists()) {
            myFilePath.createNewFile();
        }
        FileWriter resultFile = new FileWriter(myFilePath);
        PrintWriter myFile = new PrintWriter(resultFile);
        String strContent = fileContent;
        myFile.print(strContent);
        myFile.close();
        resultFile.close();
        LOG.info("create file {} successed!", filePathAndName);
    }

    /**
     * 删除文件
     */
    public static boolean delFile(String filePathAndName) {
        boolean res = false;
        String filePath = filePathAndName.trim();
        File myDelFile = new File(filePath);
        if (myDelFile.exists()){
            res = myDelFile.delete();
            LOG.info("delete file {} successed!", filePathAndName);
        }else {
            LOG.info("file {} isn't existe!", filePathAndName);
        }
        return res;
    }
}

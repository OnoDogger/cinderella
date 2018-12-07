package org.ono.action;

import org.ono.bo.FileBo;
import org.ono.services.IFileOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by ono on 2018/12/3.
 */
@RestController
public class FileOperateAction extends BaseAction{

    private static final Logger LOG = LoggerFactory.getLogger(FileOperateAction.class);

    @Autowired
    private IFileOperator fileOperatorImpl;

    @RequestMapping(value="/fileChange",method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String,String> fileChange(@RequestBody FileBo fileBo){
        try{
            fileOperatorImpl.reconstructureFile(fileBo);
        }catch (Exception e){
            LOG.error("fileChange error:",e.getMessage());
            return getResponseMap4Failure();
        }
        return getResponseMap4Success();
    }

}

package org.ono.test;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.ono.exception.*;
import org.ono.listener.DirectoryChangeListener;
import org.ono.services.IContextType;
import org.ono.services.impl.PropContextType;
import org.ono.services.impl.Storage;
import org.ono.services.impl.XmlContextType;
import org.ono.support.spring.AppContext;
import org.ono.utils.Constants;
import org.ono.utils.FileUtils;
import org.ono.utils.HttpClientUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ono on 2018/11/21.
 */
public class TestDemo {
    public static void main(String[] args) throws Exception {
//        Path path = Paths.get("/Users/ono/Downloads/git-project/syn-configuration-file-project/src/main/resources");
//        IContextType propContextType = new PropContextType();
//        IContextType xmlContextType = new XmlContextType();
        // System.out.println(path.getFileName());

//        List<Path> paths = new ArrayList<>();
//        paths.add(path);
//        propContextType.initConfig(paths, new ArrayList<>(), Constants.PROTOCOL_PROPERTIES);
//
          ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");


//        Storage storage = (Storage) context.getBean("storage");r
//
//        System.out.println(storage);

//        Path path = Paths.get("/Users/ono/Downloads/git-project/syn-configuration-file-project/src/main/resources/property-group1.properties");
//        System.out.println(path.toFile().toPath());
//        System.out.println(path.toFile().getAbsolutePath());
//        System.out.println(path.toFile().getAbsoluteFile());

//        Map<String,String> map = new HashMap<>();
//        map.put("a","b");
//        System.out.println(map.toString());
//
//        System.out.println(JSON.toJSONString(map));
//        Map<String,String> m = new HashMap<String,String>(){{
//            put("hostName", "localhost");
//            put("path", Paths.get("/Users/ono/Downloads/zookeeper-3.5.4-beta/ivy.xml").toString());
//            put("fileName", "ivy.xml");
//            put("fileType", "xml");
//            put("context", new String(Files.readAllBytes(Paths.get("/Users/ono/Downloads/zookeeper-3.5.4-beta/ivy.xml")), Constants.ENCODING));
//        }};
//        // m.put("param",)
//        sendData(JSON.toJSONString(new HashMap<String, Object>(){{
//            put("params", m);
//        }}), "http://localhost:8080/fileChange");

    }

    public static void sendData(String params, String url) throws Exception {
        CloseableHttpClient client = HttpClientUtils.buildHttpClient();
        CloseableHttpResponse response = null;
        HttpPost post = null;
        try {
            post = HttpClientUtils.buildHttpPost(url,params,new HashMap<String,String>());
            response = client.execute(post);
            HttpClientUtils.assertStatus(response);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                System.out.println("trigger successed!");
            }
        } catch (Exception e) {
            throw new Exception("trigger error"+e.getMessage());
        } finally {
            try{
                if (response != null){
                    response.close();
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new Exception("trigger error"+e.getMessage());
            }

            if (post != null){
                post.releaseConnection();
            }
        }
    }
}

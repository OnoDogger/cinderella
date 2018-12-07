package org.ono.services.impl;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.ono.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by ono on 2018/12/3.
 */
public class Reporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Reporter.class);

    // 127.0.0.1:8080,190.192.1.1:8080
    private String reporterAddress;
    private String url;

    private List<String> ips;

    public String getReporterAddress() {
        return reporterAddress;
    }

    public void setReporterAddress(String reporterAddress) {
        ips = new ArrayList<>();
        ips.addAll(Arrays.asList(reporterAddress.split(",")));
        this.reporterAddress = reporterAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> fetchFullUrl(){

        List<String> addresses = new ArrayList<>();
        for (String ip: ips){
            addresses.add("http://"+ip+"/"+url);
        }
        return addresses;
    }

    public void trigger(String params) throws Exception {
        for (String url: fetchFullUrl()){
            sendData(params, url);
        }
    }

    public void sendData(String params, String url) throws Exception {
        CloseableHttpClient client = HttpClientUtils.buildHttpClient();
        CloseableHttpResponse response = null;
        HttpPost post = null;
        try {
            post = HttpClientUtils.buildHttpPost(url,params,new HashMap<String,String>());
            response = client.execute(post);
            HttpClientUtils.assertStatus(response);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                LOGGER.info("trigger successed!");
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

    @Override
    public String toString() {
        return "Reporter{" +
                "reporterAddress='" + reporterAddress + '\'' +
                ", url='" + url + '\'' +
                ", ips=" + ips +
                '}';
    }
}

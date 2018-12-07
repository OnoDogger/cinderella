package org.ono.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;

/**
 * Created by ono on 2018/12/3.
 */
public class HttpClientUtils {

    private static CloseableHttpClient client = buildHttpClient();

    public static HttpPost buildHttpPost(String url, String param, Map<String,String> heads) {
        Assert.notNull(url, "when constructing HttpPost, url can not be null");
        HttpPost post = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectionRequestTimeout(120000).build();
        post.setConfig(requestConfig);

        if (heads != null && heads.size() > 0){
            for (Map.Entry<String, String> entry: heads.entrySet()){
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        setCommonHttpMethod(post);
        if (param != null){
            StringEntity se = new StringEntity(param, HTTP.UTF_8);
            post.setEntity(se);
        }
        return post;
    }

    public static HttpGet buildHttpGet(String url) {
        Assert.notNull(url, "when constructing httpGet, url can be not null");
        HttpGet get = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(120000).setConnectionRequestTimeout(120000).build();
        get.setConfig(requestConfig);
        setCommonHttpMethod(get);
        return get;
    }

    public static void setCommonHttpMethod(HttpRequestBase httpMethod) {
        httpMethod.setHeader(HTTP.CONTENT_TYPE,"application/json;charset=utf-8");
    }

    public static CloseableHttpClient buildHttpClient(){
        return HttpClientBuilder.create().setConnectionManager(new PoolingHttpClientConnectionManager()).build();
    }

    public static void assertStatus(HttpResponse res) throws IOException {
        Assert.notNull(res,"the response from http is null");
        Assert.notNull(res.getStatusLine(),"the status of response is null");
        switch (res.getStatusLine().getStatusCode()){
            case HttpStatus.SC_OK:
                break;
            default:
                throw new IOException("the response of services has exception");
        }
    }
}

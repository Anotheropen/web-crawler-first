package com.yang;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class HttpPoolTest {
    public static void main( String[] args ) throws IOException {

        //配置请求池
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

        cm.setMaxTotal(100);

        cm.setDefaultMaxPerRoute(10);


        //1,打开浏览器,创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        String uri="https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&wq=%E6%89%8B%E6%9C%BA&pvid=1fb0a6e1bc1e4afa8461bd306d18bbe5";

        //2,输入网址,发起get请求创建httpGet对象
        HttpGet httpGet = new HttpGet(uri);

        httpGet.setHeader("Accept","application/json, text/javascript, */*; q=0.01");
        httpGet.setHeader("Accept", "https//www.lagou.com/jobs/list_%E6%A3%AE%E6%9E%9C?labelWords=&fromSearch=true&suginput=");
        httpGet.setHeader( "User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");



        //3,按回车,发起请求,返回响应,使用HttpClient对象发起请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        //4,解析响应,获取数据
        //判断状态码是否为200
        if(response.getStatusLine().getStatusCode()==200){
            HttpEntity entity = response.getEntity();
            String utf8 = EntityUtils.toString(entity, "utf-8");

            System.out.println(utf8);
        }

    }
}

package com.yang.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpUtils {

    private PoolingHttpClientConnectionManager cm;

    public HttpUtils() {
        this.cm = new PoolingHttpClientConnectionManager();

        cm.setDefaultMaxPerRoute(100);

        cm.setMaxTotal(10);

    }

    public String doGetHtml(String url){
        //1,打开浏览器,创建httpclient对象

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        //2,输入网址,发起get请求创建httpGet对象
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response=null;

        try {
            //3,按回车,发起请求,返回响应,使用HttpClient对象发起请求
            response = httpClient.execute(httpGet);

            //4,解析响应,获取数据
            //判断状态码是否为200
            if(response.getStatusLine().getStatusCode()==200){
                if(response.getEntity()!=null){
                    HttpEntity entity = response.getEntity();
                    String utf8 = EntityUtils.toString(entity, "utf8");
                    return utf8;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String doGetImage(String url){
        //1,打开浏览器,创建httpclient对象

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        //2,输入网址,发起get请求创建httpGet对象
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response=null;

        try {
            //3,按回车,发起请求,返回响应,使用HttpClient对象发起请求
            response = httpClient.execute(httpGet);

            //4,解析响应,获取数据
            //判断状态码是否为200
            if(response.getStatusLine().getStatusCode()==200){
                if(response.getEntity()!=null){



//                    OutputStream outputStream=new FileOutputStream();
////
////                    response.getEntity().writeTo();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}

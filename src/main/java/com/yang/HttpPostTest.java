package com.yang;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class HttpPostTest {
    public static void main( String[] args ) throws IOException {

        //1,打开浏览器,创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String uri="";

        //2,输入网址,发起get请求创建httpGet对象
        HttpPost httpPost = new HttpPost(uri);


        //设置参数
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("str", "I am Post String"));

        //添加进请求中
        httpPost.setEntity(new UrlEncodedFormEntity(params,"utf8"));


        //3,按回车,发起请求,返回响应,使用HttpClient对象发起请求
        CloseableHttpResponse response = httpClient.execute(httpPost);

        //4,解析响应,获取数据
        //判断状态码是否为200
        if(response.getStatusLine().getStatusCode()==200){
            HttpEntity entity = response.getEntity();
            String utf8 = EntityUtils.toString(entity, "utf8");

            System.out.println(utf8);
        }

    }
}

package com.yang;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yang.utils.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import java.sql.Date;


import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App {

    static int all=0;

    public static void main( String[] args ) throws Exception {

        String url="https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E6%89%8B%E6%9C%BA&s=1&click=0&page=";

        //获取连接
        Properties properties=new Properties();
        InputStream inputStream=App.class.getClassLoader().getResourceAsStream("druidLocalhost.properties");
        properties.load(inputStream);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);

        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        HttpUtils httpUtils = new HttpUtils();
        ObjectMapper objectMapper = new ObjectMapper();



        for(int i=1;i<10;i=i+2){
            //获取页面
            String html = httpUtils.doGetHtml(url+i);

            //解析页面,获取商品数据并存储
            parse(html,httpUtils,objectMapper,connection);

            System.out.println("处理完第"+(i-1)+"页");

        }
        connection.close();
        System.out.println("over!!!");
    }

    //解析页面,获取商品数据并存储
    private static void parse(String html,HttpUtils httpUtils,ObjectMapper objectMapper,Connection connection) throws Exception {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        String sql="insert into  jd_item(spuId,skuId,title,price,pic,url,createTime,updateTime) values (?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        //获取页面
        Document document = Jsoup.parse(html);

        //获得所有的spu
        Elements spus = document.select("div#J_goodsList > ul > li");

        for(Element element:spus){
            //获得spu
            Long spu = Long.parseLong(element.attr("data-spu"));

            //获得所有的sku
            Elements skus = element.select("li.ps-item");

            for(Element ele:skus){

                Elements sku = ele.select("[data-sku]");
                //获得sku
                Long skuId = Long.parseLong(sku.attr("data-sku"));

                //获得商品详情url
                String itemUrl="https://item.jd.com/"+skuId+".html";

                //获取图片
                String imageUrl = sku.attr("data-lazy-img").equals("")?sku.attr("data-lazy-img-slave"):sku.attr("data-lazy-img");

                imageUrl="https:"+imageUrl.replace("/n9/","/n1/");
                //System.out.println(imageUrl);
//                document.querySelector("#J_goodsList > ul > li:nth-child(1) > div > div.p-img > a > img")
                //图片名字
                String imageName = httpUtils.doGetImage(imageUrl);

                //获取商品价格
                String priceUrl="https://p.3.cn/prices/mgets?skuIds=J_"+skuId;
                String priceJson = httpUtils.doGetHtml(priceUrl);
                Double node = objectMapper.readTree(priceJson).get(0).get("p").asDouble();

                //获取商品标题
                String s = httpUtils.doGetHtml(itemUrl);
                Document parse = Jsoup.parse(s);

                String text = parse.select("div.sku-name").text();

                ps.setLong(1,spu);
                ps.setLong(2,skuId);
                ps.setString(3,text);
                ps.setDouble(4,node);
                ps.setString(5,imageName);
                ps.setString(6,imageUrl);


                ps.setTimestamp(7, new java.sql.Timestamp(System.currentTimeMillis()));
                ps.setTimestamp(8, new java.sql.Timestamp(System.currentTimeMillis()));


                ps.addBatch();
                all++;

            }

            ps.executeBatch();
            connection.commit();
            ps.clearBatch();
            System.out.println("获取了："+all+"个");



        }
        ps.close();

    }




}

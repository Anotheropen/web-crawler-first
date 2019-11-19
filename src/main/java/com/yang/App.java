package com.yang;

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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws Exception {

        String url="https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E6%89%8B%E6%9C%BA&s=1&click=0&page=";

        HttpUtils httpUtils = new HttpUtils();
        ObjectMapper objectMapper = new ObjectMapper();

        for(int i=0;i<10;i=i+2){
            //获取页面
            String html = httpUtils.doGetHtml(url+i);

            //解析页面,获取商品数据并存储
            parse(html,httpUtils,objectMapper);

        }

    }

    //解析页面,获取商品数据并存储
    private static void parse(String html,HttpUtils httpUtils,ObjectMapper objectMapper) throws Exception {
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
                String itemUrl="https://item.jd.com/"+sku+".html";

                //获取图片
                String imageUrl = sku.attr("data-lazy-img");
                imageUrl=imageUrl.replace("/n9/","/n1/");

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


            }

        }

    }

    private static Connection getConnection(){
        Connection con = null; //定义一个MYSQL链接对象
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance(); //MYSQL驱动
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/jd", "root", "admin"); //链接本地MYSQL

        } catch (Exception e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }

        return con;
    }


}

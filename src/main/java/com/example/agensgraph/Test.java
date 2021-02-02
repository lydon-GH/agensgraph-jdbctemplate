package com.example.agensgraph;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.bitnine.agensgraph.graph.Vertex;
import net.bitnine.agensgraph.util.Jsonb;
import net.bitnine.agensgraph.util.JsonbUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        Statement stmt =getStatement();

        //先删除图
        try {
            stmt.execute("DROP GRAPH test CASCADE;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            stmt.execute("CREATE GRAPH test ");
            stmt.execute("SET graph_path = test");
            //创建顶点和边的标签
            stmt.execute("CREATE VLABEL IF NOT EXISTS \"person\"");
            stmt.execute("CREATE ELABEL IF NOT EXISTS \"know\"");
        }catch (Exception e){
            e.printStackTrace();
        }

        Test.insertBatch(stmt);
        Test.searchData();
    }

    public static void test1() {
        try{
            Class.forName("net.bitnine.agensgraph.Driver");
            Connection conn = DriverManager.getConnection("jdbc:agensgraph://127.0.0.1:5432/agens","agens","agens");
            System.out.println("connection success");
            Statement stmt = conn.createStatement();
            stmt.execute("SET graph_path = test");
            ResultSet rs = stmt.executeQuery("MATCH (:person {name: 'John'})-[:tttt]-(friend:person) RETURN friend");
            while (rs.next()) {
                Vertex person = (Vertex) rs.getObject(1);
                System.out.println(person.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test2() {
        try{
            Class.forName("net.bitnine.agensgraph.Driver");
            Connection conn = DriverManager.getConnection("jdbc:agensgraph://127.0.0.1:5432/agens","agens","agens");
            System.out.println("connection success");
            PreparedStatement pstmt = conn.prepareStatement("CREATE (:person ?)");
            Jsonb j = JsonbUtil.createObjectBuilder()
                    .add("name", "John")
                    .add("from", "USA")
                    .add("age", 17).build();
            pstmt.setObject(1, j);
            pstmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test3() {
        try{
            Class.forName("net.bitnine.agensgraph.Driver");
            Connection conn = DriverManager.getConnection("jdbc:agensgraph://127.0.0.1:5432/agens","agens","agens");
            Statement stmt = conn.createStatement();
            stmt.execute("SET graph_path = test");
            stmt.execute("CREATE (:person {name: 'John', from: 'USA', age: 17})");
            stmt.execute("CREATE (:person {name: 'Daniel', from: 'Korea', age: 20})");
            stmt.execute("MATCH (p:person {name: 'John'}),(k:person{name: 'Daniel'}) CREATE (p)-[:tttt]->(k)");
            System.out.println("-----------------insert successful--------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Statement getStatement() {
        try{
            Class.forName("net.bitnine.agensgraph.Driver");
            Connection conn = DriverManager.getConnection("jdbc:agensgraph://127.0.0.1:5432/agens","agens","agens");
            Statement stmt = conn.createStatement();
            return stmt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量插入测试
     */
    public static void insertBatch(Statement stmt) {
        try{
            String name="打工人";
            //循环多次测试插入性能
            long startTime=System.currentTimeMillis();
            //要插入的节点数量
            int nodeSize=1000000;
            //要插入的边数量
            int lineSize=2000000;

            for (int c =0 ;c<nodeSize;c++ ){
                String contentSql="{" +
                        "'birstyday': '2000年12月17日'," +
                        "'companyName': '国企'," +
                        "'borthAddress': '广东广州'," +
                        "'introduce': '大家好我是来自广东的"+name+c+"'," +
                        "'money': '-9999'," +
                        "'name': '"+name+c+"'," +
                        "'hobby': '上班、写代码、穿格子衫去大夫山踩单车'" +
                        "}";
                //创建人员节点
                stmt.execute("CREATE (:person "+contentSql+")");
            }

            for(int j=0;j<lineSize;j++) {
                //随机产生人员节点
                String person1=name+(int)(Math.random()*nodeSize);
                String person2=name+(int)(Math.random()*nodeSize);
                if(person1==person2){
                    person2=name+(int)(Math.random()*nodeSize);
                }
                // 创建边
                stmt.execute("MATCH (p:person {name: '"+person1+"'}),(k:person{name: '"+person2+"'})  CREATE (p)-[:know{place:'大夫山'}]->(k) ");
            }

            System.out.println("测试插入"+nodeSize+"个节点，"+lineSize+"条边的时间花费为"+(System.currentTimeMillis()-startTime)+"ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量插入测试
     */
    /*public static void insertBatch() {
        try{

            Statement stmt =getStatement();
            try{
                //先删除图
                stmt.execute("DROP GRAPH test CASCADE;");
            }catch (Exception e){
                e.printStackTrace();
            }

            //创建一个新图
            stmt.execute("CREATE GRAPH test ");
            stmt.execute("SET graph_path = test");
            //创建顶点和边的标签
            stmt.execute("CREATE VLABEL IF NOT EXISTS \"baidubaike\"");
            stmt.execute("CREATE VLABEL IF NOT EXISTS \"keywords\"");
            stmt.execute("CREATE ELABEL IF NOT EXISTS \"mapping\"");

            String baiketitle="新冠肺炎俄芭蕾舞團赴台後8人確診取消巡迴演出 (16:20) - 20201217 - 兩岸";
            String keywordtitle="新冠肺炎,確診,新冠肺炎疫情,新冠肺炎檢測,新冠病毒,新型冠狀病毒,新型冠狀病毒肺炎COVID-19,新型冠狀病毒肺炎,編輯推介";

            //循环多次测试插入性能
            long startTime=System.currentTimeMillis();
            //要插入的节点数量
            int nodeSize=10;
            //要插入的边数量
            int lineSize=30;

            for (int c =0 ;c<nodeSize;c++ ){
                String contentSql="{" +
                        "'paperUrl': 'https://news.mingpao.com/ins/"+c+"'," +
                        "'docContent': '台灣衛生當局今（17日）公布，計劃在台灣巡迴演出的莫斯科芭蕾舞團，繼昨日（16日）出現4人確診後，今證實再有4人感染新冠病毒，令該團確診者達8人，該表演團體在台的演出全部取消'," +
                        "'docTime': '2020年12月17日'," +
                        "'domainName': '明报新闻网'," +
                        "'docPublisher': '[\"https://www.facebook.com/mingpaoinews\"]'," +
                        "'abstract': '台灣衛生當局今（17日）公布，計劃在台灣巡迴演出的莫斯科芭蕾舞團，繼昨日（16日）出現4人確診後，今證實再有4人感染新冠病毒，令該團確診者達8人，該表演團體在台的演出全部取消。'," +
                        "'title': '"+baiketitle+c+"'," +
                        "'spiderName': 'mingpaoPaper_lianganSpider'" +
                        "}";
                //创建百科节点
                stmt.execute("CREATE (:baidubaike "+contentSql+")");
                //创建关键词节点
                String keySql="{" +
                        "'paperUrl': 'https://news.mingpao.com/ins/%e5%85%a9%e5%b2%b8/article/'," +
                        "'title': '新冠肺炎｜俄芭蕾舞團赴台後8人確診　取消巡迴演出 (16:20) - 兩岸"+c+"'," +
                        "'key': '"+keywordtitle+c+"'" +
                        "}";
                stmt.execute("CREATE (:keywords "+keySql+")");
            }

            for(int j=0;j<lineSize;j++) {
                //随机产生百科节点
                String baidubaikeNodeTitle=baiketitle+(int)(Math.random()*nodeSize);
                //随机产生关键词节点
                String keywordNodeTitle=keywordtitle+(int)(Math.random()*nodeSize);
                // 创建边
                stmt.execute("MATCH (p:baidubaike {title: '"+baidubaikeNodeTitle+"'}),(k:keywords{key: '"+keywordNodeTitle+"'})  CREATE (p)-[:mapping{dd:'1'}]->(k) ");
            }

            System.out.println("测试插入"+nodeSize*2+"个节点的时间花费为"+(System.currentTimeMillis()-startTime)+"ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 查询节点的花费
     */
    public static void searchData() {
        try{
            long startTime=System.currentTimeMillis();
            Statement stmt =getStatement();
            stmt.execute("SET graph_path = test");
            //查找打工人5第三层的节点
            ResultSet rs = stmt.executeQuery (" MATCH (p:person {name: '打工人5'})-[r:know*3..3]->(f:person) RETURN f;");
            while (rs.next()) {
                Vertex person = (Vertex) rs.getObject(1);
                //System.out.println(person.getString("name"));
            }

            System.out.println("查询的时间花费为"+(System.currentTimeMillis()-startTime)+"ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

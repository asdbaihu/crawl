package com.langchao.bigdata.hbase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class hbaseTest {
    private static String tableName = "newsinfo";
    
    public static void main(String[] args) {
        long startTime, endTime;
        System.out.println("========Hbase【启动】喽！=========");
        startTime = new Date().getTime();
        
        testCreatTable();
        testInsert();
        tetQueryAll();
        
        
        endTime = new Date().getTime();
        System.out.println("========Hbase【结束】喽！=========");
        System.out.println("用时为：" + (endTime - startTime)  + "ms");
    }
    
    private static void tetQueryAll() {
        HbaseUtil.QueryAll(tableName);        
    }

    private static void testCreatTable() {
        List<String> cl = new ArrayList();
        cl.add("url");
        cl.add("title");
        cl.add("cTime");
        cl.add("sInfo");
        HbaseUtil.createTable(tableName, cl);
    }
    
    @SuppressWarnings("rawtypes")
    private static void testInsert() {
        String pageKey = "111";
        String url = "http://www.baidu.com/";
        String title = "教育部工作网络视频会议的通知11";
        String cTime = "2016-11-18 13:34:57";
        String sInfo = "信息内容55";
        
        List<Map> cl = new ArrayList();        
        Map<String, String> m = new HashMap<String, String>();
        
        m.put("url", url);
        m.put("title", title);
        m.put("cTime", cTime);
        m.put("sInfo", sInfo);
        cl.add(m);
        HbaseUtil.insertData(tableName, pageKey, cl);
    }
    
}

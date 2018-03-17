package com.langchao.bigdata.util;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class SslTest {  
       
    public String getRequest(String url,int timeOut) throws Exception{  
        URL u = new URL(url);  
        if("https".equalsIgnoreCase(u.getProtocol())){
            SslUtils.ignoreSsl();  
        }  
        URLConnection conn = u.openConnection();  
        conn.setConnectTimeout(timeOut);  
        conn.setReadTimeout(timeOut);  
        return IOUtils.toString(conn.getInputStream());  
    }  
       
    public String postRequest(String urlAddress,String args,int timeOut) throws Exception{  
        URL url = new URL(urlAddress);  
        if("https".equalsIgnoreCase(url.getProtocol())){  
            SslUtils.ignoreSsl();  
        }  
        URLConnection u = url.openConnection();  
        u.setDoInput(true);  
        u.setDoOutput(true);  
        u.setConnectTimeout(timeOut);  
        u.setReadTimeout(timeOut);  
        OutputStreamWriter osw = new OutputStreamWriter(u.getOutputStream(), "UTF-8");  
        osw.write(args);  
        osw.flush();  
        osw.close();  
        u.getOutputStream();  
        return IOUtils.toString(u.getInputStream());  
    }  
       
    public static void main(String[] args) {
        Pattern p = Pattern.compile("((\\d{2}(([02468][048])|([13579][26]))[\\-\\-\\s]?((((0?" +"[13578])|(1[02]))[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))" +"|(((0?[469])|(11))[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(30)))|" +"(0?2[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12" +"35679])|([13579][01345789]))[\\-\\-\\s]?((((0?[13578])|(1[02]))" +"[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))" +"[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\-\\s]?((0?[" +"1-9])|(1[0-9])|(2[0-8]))))))"); 
        String s = "2018-03-05至无固定期限"; 
        Matcher ma = p.matcher(s);
        System.out.println(s + " " + ma.group(1)); 
        //        try {  
//            SslTest st = new SslTest();  
//            String a = st.getRequest("https://bj.tianyancha.com/search", 3000);  
//            System.out.println(a);  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
    }  
   
} 
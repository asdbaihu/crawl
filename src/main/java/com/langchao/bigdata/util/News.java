package com.langchao.bigdata.util;

public class News extends BaseObj {
    //private String pageKey;
    private String title;
    private String cTime;
    private String sInfo;
    
    public News(String pageKey, String title, String cTime, String sInfo) {
        super();
        this.id = pageKey;
        this.title = title;
        this.cTime = cTime;
        this.sInfo = sInfo;
    }
    
    public News() {
        super();
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getcTime() {
        return cTime;
    }
    
    public void setcTime(String cTime) {
        this.cTime = cTime;
    }
    
    public String getsInfo() {
        return sInfo;
    }
    
    public void setsInfo(String sInfo) {
        this.sInfo = sInfo;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" | pageKey:" + this.getId());
        sb.append(" | title:" + this.getTitle());
        sb.append(" | cTime:" + this.getcTime());
        sb.append(" | sInfo:" + this.getsInfo());
        return sb.toString();
    }
}

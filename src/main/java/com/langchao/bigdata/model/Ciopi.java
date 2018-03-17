package com.langchao.bigdata.model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.Formatter;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;
/**
 * 创建csdn数据模型
 * 注解方式实现
 * @author yuenbin
 */
@HelpUrl("//div[@class='pager']/a/@href  | //div[@class='news_list']/ul/li/a/@href")//相当于列表
@TargetUrl("http://www.tiekuangshi.com/news/\\d+.htm")//具体详情页面
public class Ciopi implements AfterExtractor {
	
	@ExtractBy(value="/html/body/div[4]/div[1]/dl/dt/h1/text()",notNull=true, type=ExtractBy.Type.XPath)
    private String title;// 标题

	@Formatter("yyyy-MM-dd HH:mm")
	@ExtractBy(value="//dd[@class='article-content']/p[2]/span/text() | //dd[@class='article-content']/p[3]/span/text()", notNull=true,type=ExtractBy.Type.XPath)
    private String content;// 日期
 
    @Override
    public String toString() {
        return "Ciopi [title=" + title + ", content=" + content + "]";
    }
    
    

	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}


	public void afterProcess(Page page) {
	}
}

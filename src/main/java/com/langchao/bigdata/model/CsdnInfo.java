package com.langchao.bigdata.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.Formatter;
import us.codecraft.webmagic.model.annotation.TargetUrl;
/**
 * 创建csdn数据模型
 * 注解方式实现
 * @author yuenbin
 *
 */
@TargetUrl("http://blog.csdn.net/*")
public class CsdnInfo implements AfterExtractor {
	
	@Formatter(value="",subClazz=Integer.class)
	private int id;// 编号
	
	@ExtractBy(value="//div[@class='article_title']/h1/span[@class='link_title']/a/text()",notNull=true, type=ExtractBy.Type.XPath)
    private String title;// 标题

	@Formatter("yyyy-MM-dd HH:mm")
	@ExtractBy(value="//div[@class='article_manage']/span[@class='link_postdate']/text()", notNull=true,type=ExtractBy.Type.XPath)
    private String date;// 日期

    private String tags="33";// 标签
	
    private String category="4r";// 分类

	@ExtractBy("//div[@class='article_manage']/span[@class='link_view']/regex(\\d+)")
    private int view;// 阅读人数

	@ExtractBy("//div[@class='article_manage']/span[@class='link_comments']/a/regex('\\((\\d+)\\)')")
    private int comments;// 评论人数

    private int copyright=1;// 是否原创

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getCopyright() {
        return copyright;
    }

    public void setCopyright(int copyright) {
        this.copyright = copyright;
    }

    @Override
    public String toString() {
        return "CsdnBlog [id=" + id + ", title=" + title + ", date=" + date + ", tags=" + tags + ", category="
                + category + ", view=" + view + ", comments=" + comments + ", copyright=" + copyright + "]";
    }

	public void afterProcess(Page page) {
	    String url = page.getUrl().toString();
	    Pattern p = Pattern.compile("http://\\w+.\\w+.\\w+/\\w+/\\w+/\\w+/$/\\d+");
	    Matcher matcher = p.matcher(url);
	    String id="10";
	    if(matcher.matches())
	    {
	    	id = url.substring(url.lastIndexOf("/")+1);
	    }
		
		this.setId(Integer.parseInt(id));
		this.setCopyright(17);
	}
}

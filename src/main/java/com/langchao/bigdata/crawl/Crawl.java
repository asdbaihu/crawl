package com.langchao.bigdata.crawl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

import com.langchao.bigdata.model.Ciopi;
import com.langchao.bigdata.model.CountryInfo;
import com.langchao.bigdata.pipeline.CiopiPipeLine;
import com.langchao.bigdata.pipeline.CountryInfoPipeLine;

/**
 * 爬虫的具体实现
 * @author yuenbin
 *
 */
@Component
public class Crawl {
	/**指明子类实例化*/
	@Qualifier("countryInfoPipeLine")
	@Autowired
	private CountryInfoPipeLine countryInfoPipeLine;
	
	public void crawl(){
		System.err.println("爬虫开始");
	    long startTime, endTime;
	    startTime = System.currentTimeMillis();
		OOSpider.create(Site.me()
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36"),
                countryInfoPipeLine, CountryInfo.class).addUrl("http://114.xixik.com/country/")
                .thread(5).run();
	
		  endTime = System.currentTimeMillis();
		System.err.println("爬虫结束,共耗时:"+(endTime-startTime)/1000+"秒,共抓取总数:"+countryInfoPipeLine.getArticleSize()+"篇!!!");
	}
	
	public static void main(String[] args) {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:resources/*.xml");
		final Crawl crawl = app.getBean(Crawl.class);
		crawl.crawl();
		
	}
		 
}

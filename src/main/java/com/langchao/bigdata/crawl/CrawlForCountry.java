package com.langchao.bigdata.crawl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;

import com.langchao.bigdata.model.Ciopi;
import com.langchao.bigdata.model.CountryInfo;
import com.langchao.bigdata.pipeline.CiopiPipeLine;
import com.langchao.bigdata.pipeline.CountryInfoPipeLine;
import com.langchao.bigdata.scheduler.JedisScheduler;

/**
 * 爬虫的具体实现
 * @author yuenbin
 *
 */
@Component
public class CrawlForCountry {
	/**指明子类实例化*/
	@Qualifier("countryInfoPipeLine")
	@Autowired
	private CountryInfoPipeLine countryInfoPipeLine;
	
	private static final Logger log = LoggerFactory.getLogger(CrawlForCountry.class);
	
	public void crawl(){
		log.info("爬虫开始");
	    long startTime, endTime;
	    startTime = System.currentTimeMillis();
	    OOSpider spider = OOSpider.create(Site.me()
                .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36"),
                countryInfoPipeLine, CountryInfo.class);
	    spider.addUrl("http://114.xixik.com/country");
	    spider.setScheduler(new JedisScheduler(log));
	    spider.thread(5).run();
		  endTime = System.currentTimeMillis();
		  log.info("爬虫结束,共耗时:"+(endTime-startTime)/1000+"秒,共抓取总数:"+countryInfoPipeLine.getArticleSize()+"篇!!!");
	}
	
	public static void main(String[] args) {
		ApplicationContext app = new ClassPathXmlApplicationContext("classpath:resources/*.xml");
		final CrawlForCountry crawl = app.getBean(CrawlForCountry.class);
		crawl.crawl();
		
	}
		 
}
